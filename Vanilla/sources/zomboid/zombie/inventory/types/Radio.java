package zombie.inventory.types;

import java.io.IOException;
import java.nio.ByteBuffer;
import zombie.GameTime;
import zombie.Lua.LuaEventManager;
import zombie.characters.IsoPlayer;
import zombie.characters.Talker;
import zombie.chat.ChatManager;
import zombie.chat.ChatMessage;
import zombie.core.properties.PropertyContainer;
import zombie.interfaces.IUpdater;
import zombie.iso.IsoGridSquare;
import zombie.iso.sprite.IsoSprite;
import zombie.iso.sprite.IsoSpriteManager;
import zombie.network.GameClient;
import zombie.network.GameServer;
import zombie.radio.ZomboidRadio;
import zombie.radio.devices.DeviceData;
import zombie.radio.devices.WaveSignalDevice;
import zombie.scripting.objects.Item;
import zombie.ui.UIFont;

public final class Radio extends Moveable implements Talker, IUpdater, WaveSignalDevice {
   protected DeviceData deviceData = null;
   protected GameTime gameTime;
   protected int lastMin = 0;
   protected boolean doPowerTick = false;
   protected int listenCnt = 0;

   public Radio(String var1, String var2, String var3, String var4) {
      super(var1, var2, var3, var4);
      this.deviceData = new DeviceData(this);
      this.gameTime = GameTime.getInstance();
      this.canBeDroppedOnFloor = true;
   }

   public DeviceData getDeviceData() {
      return this.deviceData;
   }

   public void setDeviceData(DeviceData var1) {
      if (var1 == null) {
         var1 = new DeviceData(this);
      }

      this.deviceData = var1;
      this.deviceData.setParent(this);
   }

   public void doReceiveSignal(int var1) {
      if (this.deviceData != null) {
         this.deviceData.doReceiveSignal(var1);
      }

   }

   public void AddDeviceText(String var1, float var2, float var3, float var4, String var5, int var6) {
      if (!ZomboidRadio.isStaticSound(var1)) {
         this.doReceiveSignal(var6);
      }

      IsoPlayer var7 = this.getPlayer();
      if (var7 != null && this.deviceData != null && this.deviceData.getDeviceVolume() > 0.0F && !var7.Traits.Deaf.isSet()) {
         var7.Say(var1, var2, var3, var4, UIFont.Medium, (float)this.deviceData.getDeviceVolumeRange(), "radio");
         if (var5 != null) {
            LuaEventManager.triggerEvent("OnDeviceText", var5, -1, -1, -1, var1, this);
         }
      }

   }

   public void AddDeviceText(ChatMessage var1, float var2, float var3, float var4, String var5, int var6) {
      if (!ZomboidRadio.isStaticSound(var1.getText())) {
         this.doReceiveSignal(var6);
      }

      IsoPlayer var7 = this.getPlayer();
      if (var7 != null && this.deviceData != null && this.deviceData.getDeviceVolume() > 0.0F) {
         ChatManager.getInstance().showRadioMessage(var1);
         if (var5 != null) {
            LuaEventManager.triggerEvent("OnDeviceText", var5, -1, -1, -1, var1, this);
         }
      }

   }

   public boolean HasPlayerInRange() {
      return false;
   }

   public boolean ReadFromWorldSprite(String var1) {
      if (var1 == null) {
         return false;
      } else {
         IsoSprite var2 = IsoSpriteManager.instance.getSprite(var1);
         if (var2 != null) {
            PropertyContainer var3 = var2.getProperties();
            if (var3.Is("IsMoveAble")) {
               if (var3.Is("CustomItem")) {
                  this.customItem = var3.Val("CustomItem");
               }

               this.worldSprite = var1;
               return true;
            }
         }

         System.out.println("Warning: Radio worldsprite not valid, sprite = " + (var1 == null ? "null" : var1));
         return false;
      }
   }

   public int getSaveType() {
      return Item.Type.Radio.ordinal();
   }

   public float getDelta() {
      return this.deviceData != null ? this.deviceData.getPower() : 0.0F;
   }

   public void setDelta(float var1) {
      if (this.deviceData != null) {
         this.deviceData.setPower(var1);
      }

   }

   public IsoGridSquare getSquare() {
      return this.container != null && this.container.parent != null && this.container.parent instanceof IsoPlayer ? this.container.parent.getSquare() : null;
   }

   public float getX() {
      IsoGridSquare var1 = this.getSquare();
      return var1 == null ? 0.0F : (float)var1.getX();
   }

   public float getY() {
      IsoGridSquare var1 = this.getSquare();
      return var1 == null ? 0.0F : (float)var1.getY();
   }

   public float getZ() {
      IsoGridSquare var1 = this.getSquare();
      return var1 == null ? 0.0F : (float)var1.getZ();
   }

   public IsoPlayer getPlayer() {
      return this.container != null && this.container.parent != null && this.container.parent instanceof IsoPlayer ? (IsoPlayer)this.container.parent : null;
   }

   public void render() {
   }

   public void renderlast() {
   }

   public void update() {
      if (this.deviceData != null) {
         if (!GameServer.bServer && !GameClient.bClient || GameClient.bClient) {
            IsoPlayer var1 = IsoPlayer.getInstance();
            if (var1.getEquipedRadio() == this) {
               this.deviceData.update(false, true);
            } else {
               this.deviceData.cleanSoundsAndEmitter();
            }
         }

      }
   }

   public boolean IsSpeaking() {
      return false;
   }

   public void Say(String var1) {
   }

   public String getSayLine() {
      return null;
   }

   public String getTalkerType() {
      return "radio";
   }

   public void save(ByteBuffer var1, boolean var2) throws IOException {
      super.save(var1, var2);
      if (this.deviceData != null) {
         var1.put((byte)1);
         this.deviceData.save(var1, var2);
      } else {
         var1.put((byte)0);
      }

   }

   public void load(ByteBuffer var1, int var2) throws IOException {
      super.load(var1, var2);
      if (this.deviceData == null) {
         this.deviceData = new DeviceData(this);
      }

      if (var1.get() == 1) {
         this.deviceData.load(var1, var2, false);
      }

      this.deviceData.setParent(this);
   }
}

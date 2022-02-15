package zombie.iso.objects;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import zombie.GameTime;
import zombie.GameWindow;
import zombie.Lua.LuaEventManager;
import zombie.characters.IsoPlayer;
import zombie.characters.Talker;
import zombie.chat.ChatElement;
import zombie.chat.ChatElementOwner;
import zombie.core.Core;
import zombie.core.Rand;
import zombie.core.properties.PropertyContainer;
import zombie.inventory.InventoryItem;
import zombie.inventory.InventoryItemFactory;
import zombie.inventory.types.Radio;
import zombie.iso.IsoCamera;
import zombie.iso.IsoCell;
import zombie.iso.IsoGridSquare;
import zombie.iso.IsoLightSource;
import zombie.iso.IsoObject;
import zombie.iso.IsoUtils;
import zombie.iso.IsoWorld;
import zombie.iso.sprite.IsoSprite;
import zombie.network.GameClient;
import zombie.network.GameServer;
import zombie.radio.ZomboidRadio;
import zombie.radio.devices.DeviceData;
import zombie.radio.devices.PresetEntry;
import zombie.radio.devices.WaveSignalDevice;
import zombie.ui.UIFont;

public class IsoWaveSignal extends IsoObject implements WaveSignalDevice, ChatElementOwner, Talker {
   protected IsoLightSource lightSource = null;
   protected boolean lightWasRemoved = false;
   protected int lightSourceRadius = 4;
   protected float nextLightUpdate = 0.0F;
   protected float lightUpdateCnt = 0.0F;
   protected DeviceData deviceData = null;
   protected boolean displayRange = false;
   protected boolean hasPlayerInRange = false;
   protected GameTime gameTime;
   protected ChatElement chatElement;
   protected String talkerType = "device";
   protected static Map deviceDataCache = new HashMap();

   public IsoWaveSignal(IsoCell var1) {
      super(var1);
      this.init(true);
   }

   public IsoWaveSignal(IsoCell var1, IsoGridSquare var2, IsoSprite var3) {
      super(var1, var2, var3);
      this.init(false);
   }

   protected void init(boolean var1) {
      this.chatElement = new ChatElement(this, 5, this.talkerType);
      this.gameTime = GameTime.getInstance();
      if (!var1) {
         if (this.sprite != null && this.sprite.getProperties() != null) {
            PropertyContainer var2 = this.sprite.getProperties();
            if (var2.Is("CustomItem") && var2.Val("CustomItem") != null) {
               this.deviceData = this.cloneDeviceDataFromItem(var2.Val("CustomItem"));
            }
         }

         if (!GameClient.bClient && this.deviceData != null) {
            this.deviceData.generatePresets();
            this.deviceData.setDeviceVolume(Rand.Next(0.1F, 1.0F));
            this.deviceData.setRandomChannel();
            if (Rand.Next(100) <= 35 && !"Tutorial".equals(Core.GameMode)) {
               this.deviceData.setTurnedOnRaw(true);
            }
         }
      }

      if (this.deviceData == null) {
         this.deviceData = new DeviceData(this);
      }

      this.deviceData.setParent(this);
   }

   public DeviceData cloneDeviceDataFromItem(String var1) {
      if (var1 != null) {
         if (deviceDataCache.containsKey(var1) && deviceDataCache.get(var1) != null) {
            return ((DeviceData)deviceDataCache.get(var1)).getClone();
         }

         InventoryItem var2 = InventoryItemFactory.CreateItem(var1);
         if (var2 != null && var2 instanceof Radio) {
            DeviceData var3 = ((Radio)var2).getDeviceData();
            if (var3 != null) {
               deviceDataCache.put(var1, var3);
               return var3.getClone();
            }
         }
      }

      return null;
   }

   public boolean hasChatToDisplay() {
      return this.chatElement.getHasChatToDisplay();
   }

   public boolean HasPlayerInRange() {
      return this.hasPlayerInRange;
   }

   public float getDelta() {
      return this.deviceData != null ? this.deviceData.getPower() : 0.0F;
   }

   public void setDelta(float var1) {
      if (this.deviceData != null) {
         this.deviceData.setPower(var1);
      }

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

   public boolean IsSpeaking() {
      return this.chatElement.IsSpeaking();
   }

   public String getTalkerType() {
      return this.chatElement.getTalkerType();
   }

   public void setTalkerType(String var1) {
      this.talkerType = var1 == null ? "" : var1;
      this.chatElement.setTalkerType(this.talkerType);
   }

   public String getSayLine() {
      return this.chatElement.getSayLine();
   }

   public void Say(String var1) {
      this.AddDeviceText(var1, 1.0F, 1.0F, 1.0F, (String)null, -1, false);
   }

   public void AddDeviceText(String var1, float var2, float var3, float var4, String var5, int var6) {
      this.AddDeviceText(var1, var2, var3, var4, var5, var6, true);
   }

   public void AddDeviceText(String var1, int var2, int var3, int var4, String var5, int var6) {
      this.AddDeviceText(var1, (float)var2 / 255.0F, (float)var3 / 255.0F, (float)(var4 / 255), var5, var6, true);
   }

   public void AddDeviceText(String var1, int var2, int var3, int var4, String var5, int var6, boolean var7) {
      this.AddDeviceText(var1, (float)var2 / 255.0F, (float)var3 / 255.0F, (float)var4 / 255.0F, var5, var6, var7);
   }

   public void AddDeviceText(String var1, float var2, float var3, float var4, String var5, int var6, boolean var7) {
      if (this.deviceData != null && this.deviceData.getIsTurnedOn()) {
         if (!ZomboidRadio.isStaticSound(var1)) {
            this.deviceData.doReceiveSignal(var6);
         }

         if (this.deviceData.getDeviceVolume() > 0.0F) {
            this.chatElement.addChatLine(var1, var2, var3, var4, UIFont.Medium, (float)this.deviceData.getDeviceVolumeRange(), "default", true, true, true, true, true, true);
            if (var5 != null) {
               LuaEventManager.triggerEvent("OnDeviceText", var5, this.getX(), this.getY(), this.getZ(), var1, this);
            }
         }
      }

   }

   public void renderlast() {
      if (this.chatElement.getHasChatToDisplay()) {
         if (this.getDeviceData() != null && !this.getDeviceData().getIsTurnedOn()) {
            this.chatElement.clear(IsoCamera.frameState.playerIndex);
            return;
         }

         float var1 = IsoUtils.XToScreen(this.getX(), this.getY(), this.getZ(), 0);
         float var2 = IsoUtils.YToScreen(this.getX(), this.getY(), this.getZ(), 0);
         var1 = var1 - IsoCamera.getOffX() - this.offsetX;
         var2 = var2 - IsoCamera.getOffY() - this.offsetY;
         var1 += (float)(32 * Core.TileScale);
         var2 += (float)(50 * Core.TileScale);
         var1 /= Core.getInstance().getZoom(IsoPlayer.getPlayerIndex());
         var2 /= Core.getInstance().getZoom(IsoPlayer.getPlayerIndex());
         this.chatElement.renderBatched(IsoPlayer.getPlayerIndex(), (int)var1, (int)var2);
      }

   }

   public void renderlastold2() {
      if (this.chatElement.getHasChatToDisplay()) {
         float var1 = IsoUtils.XToScreen(this.getX(), this.getY(), this.getZ(), 0);
         float var2 = IsoUtils.YToScreen(this.getX(), this.getY(), this.getZ(), 0);
         var1 = var1 - IsoCamera.getOffX() - this.offsetX;
         var2 = var2 - IsoCamera.getOffY() - this.offsetY;
         var1 += 28.0F;
         var2 += 180.0F;
         var1 /= Core.getInstance().getZoom(IsoPlayer.getPlayerIndex());
         var2 /= Core.getInstance().getZoom(IsoPlayer.getPlayerIndex());
         var1 += (float)IsoCamera.getScreenLeft(IsoPlayer.getPlayerIndex());
         var2 += (float)IsoCamera.getScreenTop(IsoPlayer.getPlayerIndex());
         this.chatElement.renderBatched(IsoPlayer.getPlayerIndex(), (int)var1, (int)var2);
      }

   }

   protected boolean playerWithinBounds(IsoPlayer var1, float var2) {
      if (var1 == null) {
         return false;
      } else {
         return (var1.getX() > this.getX() - var2 || var1.getX() < this.getX() + var2) && (var1.getY() > this.getY() - var2 || var1.getY() < this.getY() + var2);
      }
   }

   public void update() {
      if (this.deviceData != null) {
         if ((GameServer.bServer || GameClient.bClient) && !GameServer.bServer) {
            this.deviceData.updateSimple();
         } else {
            this.deviceData.update(true, this.hasPlayerInRange);
         }

         if (!GameServer.bServer) {
            this.hasPlayerInRange = false;
            if (this.deviceData.getIsTurnedOn()) {
               IsoPlayer var1 = IsoPlayer.getInstance();
               if (this.playerWithinBounds(var1, (float)this.deviceData.getDeviceVolumeRange() * 0.6F)) {
                  this.hasPlayerInRange = true;
               }

               this.updateLightSource();
            } else {
               this.removeLightSourceFromWorld();
            }

            this.chatElement.setHistoryRange((float)this.deviceData.getDeviceVolumeRange() * 0.6F);
            this.chatElement.update();
         } else {
            this.hasPlayerInRange = false;
         }

      }
   }

   protected void updateLightSource() {
   }

   protected void removeLightSourceFromWorld() {
      if (this.lightSource != null) {
         IsoWorld.instance.CurrentCell.removeLamppost(this.lightSource);
         this.lightSource = null;
      }

   }

   public void load(ByteBuffer var1, int var2, boolean var3) throws IOException {
      super.load(var1, var2, var3);
      if (this.deviceData == null) {
         this.deviceData = new DeviceData(this);
      }

      if (var1.get() == 1) {
         this.deviceData.load(var1, var2, true);
      }

      this.deviceData.setParent(this);
   }

   public void save(ByteBuffer var1, boolean var2) throws IOException {
      super.save(var1, var2);
      if (this.deviceData != null) {
         var1.put((byte)1);
         this.deviceData.save(var1, true);
      } else {
         var1.put((byte)0);
      }

   }

   public void addToWorld() {
      if (!GameServer.bServer) {
         ZomboidRadio.getInstance().RegisterDevice(this);
      }

      if (this.getCell() != null) {
         this.getCell().addToStaticUpdaterObjectList(this);
      }

      super.addToWorld();
   }

   public void removeFromWorld() {
      if (!GameServer.bServer) {
         ZomboidRadio.getInstance().UnRegisterDevice(this);
      }

      this.removeLightSourceFromWorld();
      this.lightSource = null;
      if (this.deviceData != null) {
         this.deviceData.cleanSoundsAndEmitter();
      }

      super.removeFromWorld();
   }

   public void removeFromSquare() {
      super.removeFromSquare();
      this.square = null;
   }

   public void saveState(ByteBuffer var1) throws IOException {
      if (this.deviceData != null) {
         ArrayList var2 = this.deviceData.getDevicePresets().getPresets();
         var1.putInt(var2.size());

         for(int var3 = 0; var3 < var2.size(); ++var3) {
            PresetEntry var4 = (PresetEntry)var2.get(var3);
            GameWindow.WriteString(var1, var4.getName());
            var1.putInt(var4.getFrequency());
         }

         var1.put((byte)(this.deviceData.getIsTurnedOn() ? 1 : 0));
         var1.putInt(this.deviceData.getChannel());
         var1.putFloat(this.deviceData.getDeviceVolume());
      }
   }

   public void loadState(ByteBuffer var1) throws IOException {
      ArrayList var2 = this.deviceData.getDevicePresets().getPresets();
      int var3 = var1.getInt();

      for(int var4 = 0; var4 < var3; ++var4) {
         String var5 = GameWindow.ReadString(var1);
         int var6 = var1.getInt();
         if (var4 < var2.size()) {
            PresetEntry var7 = (PresetEntry)var2.get(var4);
            var7.setName(var5);
            var7.setFrequency(var6);
         } else {
            this.deviceData.getDevicePresets().addPreset(var5, var6);
         }
      }

      while(var2.size() > var3) {
         this.deviceData.getDevicePresets().removePreset(var3);
      }

      this.deviceData.setTurnedOnRaw(var1.get() == 1);
      this.deviceData.setChannelRaw(var1.getInt());
      this.deviceData.setDeviceVolumeRaw(var1.getFloat());
   }
}

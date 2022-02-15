package zombie.iso.objects;

import java.io.IOException;
import java.io.PrintStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Iterator;
import zombie.GameTime;
import zombie.GameWindow;
import zombie.SandboxOptions;
import zombie.SystemDisabler;
import zombie.WorldSoundManager;
import zombie.characters.IsoGameCharacter;
import zombie.core.Color;
import zombie.core.Rand;
import zombie.core.network.ByteBufferWriter;
import zombie.core.raknet.UdpConnection;
import zombie.inventory.InventoryItem;
import zombie.inventory.InventoryItemFactory;
import zombie.inventory.types.DrainableComboItem;
import zombie.inventory.types.Moveable;
import zombie.iso.IsoCell;
import zombie.iso.IsoChunk;
import zombie.iso.IsoGridSquare;
import zombie.iso.IsoLightSource;
import zombie.iso.IsoObject;
import zombie.iso.IsoRoomLight;
import zombie.iso.IsoWorld;
import zombie.iso.areas.IsoRoom;
import zombie.iso.sprite.IsoSprite;
import zombie.network.GameClient;
import zombie.network.GameServer;
import zombie.network.PacketTypes;
import zombie.network.PacketTypesShort;

public class IsoLightSwitch extends IsoObject {
   boolean Activated = false;
   public final ArrayList lights = new ArrayList();
   public boolean lightRoom = false;
   public int RoomID = -1;
   public boolean bStreetLight = false;
   private boolean canBeModified = false;
   private boolean useBattery = false;
   private boolean hasBattery = false;
   private String bulbItem = "Base.LightBulb";
   private float power = 0.0F;
   private float delta = 2.5E-4F;
   private float primaryR = 1.0F;
   private float primaryG = 1.0F;
   private float primaryB = 1.0F;
   protected long lastMinuteStamp = -1L;
   protected int bulbBurnMinutes = -1;
   protected int lastMin = 0;
   protected int nextBreakUpdate = 60;

   public String getObjectName() {
      return "LightSwitch";
   }

   public IsoLightSwitch(IsoCell var1) {
      super(var1);
   }

   public IsoLightSwitch(IsoCell var1, IsoGridSquare var2, IsoSprite var3, int var4) {
      super(var1, var2, var3);
      this.RoomID = var4;
      if (var3 != null && var3.getProperties().Is("lightR")) {
         if (var3.getProperties().Is("IsMoveAble")) {
            this.canBeModified = true;
         }

         this.primaryR = Float.parseFloat(var3.getProperties().Val("lightR")) / 255.0F;
         this.primaryG = Float.parseFloat(var3.getProperties().Val("lightG")) / 255.0F;
         this.primaryB = Float.parseFloat(var3.getProperties().Val("lightB")) / 255.0F;
      } else {
         this.lightRoom = true;
      }

      this.bStreetLight = var3 != null && var3.getProperties().Is("streetlight");
      IsoRoom var5 = this.square.getRoom();
      if (var5 != null && this.lightRoom) {
         if (!var2.haveElectricity() && GameTime.instance.NightsSurvived >= SandboxOptions.instance.getElecShutModifier()) {
            var5.def.bLightsActive = false;
         }

         this.Activated = var5.def.bLightsActive;
         var5.lightSwitches.add(this);
      } else {
         this.Activated = true;
      }

   }

   public void addLightSourceFromSprite() {
      if (this.sprite != null && this.sprite.getProperties().Is("lightR")) {
         float var1 = Float.parseFloat(this.sprite.getProperties().Val("lightR")) / 255.0F;
         float var2 = Float.parseFloat(this.sprite.getProperties().Val("lightG")) / 255.0F;
         float var3 = Float.parseFloat(this.sprite.getProperties().Val("lightB")) / 255.0F;
         this.Activated = false;
         this.setActive(true, true);
         int var4 = 10;
         if (this.sprite.getProperties().Is("LightRadius") && Integer.parseInt(this.sprite.getProperties().Val("LightRadius")) > 0) {
            var4 = Integer.parseInt(this.sprite.getProperties().Val("LightRadius"));
         }

         IsoLightSource var5 = new IsoLightSource(this.square.getX(), this.square.getY(), this.square.getZ(), var1, var2, var3, var4);
         var5.bActive = this.Activated;
         var5.bHydroPowered = true;
         var5.switches.add(this);
         this.lights.add(var5);
      }

   }

   public boolean getCanBeModified() {
      return this.canBeModified;
   }

   public float getPower() {
      return this.power;
   }

   public void setPower(float var1) {
      this.power = var1;
   }

   public void setDelta(float var1) {
      this.delta = var1;
   }

   public float getDelta() {
      return this.delta;
   }

   public void setUseBattery(boolean var1) {
      this.setActive(false);
      this.useBattery = var1;
      if (GameClient.bClient) {
         this.syncCustomizedSettings((UdpConnection)null);
      }

   }

   public boolean getUseBattery() {
      return this.useBattery;
   }

   public boolean getHasBattery() {
      return this.hasBattery;
   }

   public void setHasBatteryRaw(boolean var1) {
      this.hasBattery = var1;
   }

   public void addBattery(IsoGameCharacter var1, InventoryItem var2) {
      if (this.canBeModified && this.useBattery && !this.hasBattery && var2 != null && var2.getFullType().equals("Base.Battery")) {
         this.power = ((DrainableComboItem)var2).getUsedDelta();
         this.hasBattery = true;
         var1.removeFromHands(var2);
         var1.getInventory().Remove(var2);
         if (GameClient.bClient) {
            this.syncCustomizedSettings((UdpConnection)null);
         }
      }

   }

   public DrainableComboItem removeBattery(IsoGameCharacter var1) {
      if (this.canBeModified && this.useBattery && this.hasBattery) {
         DrainableComboItem var2 = (DrainableComboItem)InventoryItemFactory.CreateItem("Base.Battery");
         if (var2 != null) {
            this.hasBattery = false;
            var2.setUsedDelta(this.power >= 0.0F ? this.power : 0.0F);
            this.power = 0.0F;
            this.setActive(false, false, true);
            var1.getInventory().AddItem((InventoryItem)var2);
            if (GameClient.bClient) {
               this.syncCustomizedSettings((UdpConnection)null);
            }

            return var2;
         }
      }

      return null;
   }

   public boolean hasLightBulb() {
      return this.bulbItem != null;
   }

   public String getBulbItem() {
      return this.bulbItem;
   }

   public void setBulbItemRaw(String var1) {
      this.bulbItem = var1;
   }

   public void addLightBulb(IsoGameCharacter var1, InventoryItem var2) {
      if (!this.hasLightBulb() && var2 != null && var2.getType().startsWith("LightBulb")) {
         IsoLightSource var3 = this.getPrimaryLight();
         if (var3 != null) {
            this.setPrimaryR(var2.getColorRed());
            this.setPrimaryG(var2.getColorGreen());
            this.setPrimaryB(var2.getColorBlue());
            this.bulbItem = var2.getFullType();
            var1.removeFromHands(var2);
            var1.getInventory().Remove(var2);
            if (GameClient.bClient) {
               this.syncCustomizedSettings((UdpConnection)null);
            }
         }
      }

   }

   public InventoryItem removeLightBulb(IsoGameCharacter var1) {
      IsoLightSource var2 = this.getPrimaryLight();
      if (var2 != null && this.hasLightBulb()) {
         InventoryItem var3 = InventoryItemFactory.CreateItem(this.bulbItem);
         if (var3 != null) {
            var3.setColorRed(this.getPrimaryR());
            var3.setColorGreen(this.getPrimaryG());
            var3.setColorBlue(this.getPrimaryB());
            var3.setColor(new Color(var2.r, var2.g, var2.b));
            this.bulbItem = null;
            var1.getInventory().AddItem(var3);
            this.setActive(false, false, true);
            if (GameClient.bClient) {
               this.syncCustomizedSettings((UdpConnection)null);
            }

            return var3;
         }
      }

      return null;
   }

   private IsoLightSource getPrimaryLight() {
      return this.lights.size() > 0 ? (IsoLightSource)this.lights.get(0) : null;
   }

   public float getPrimaryR() {
      return this.getPrimaryLight() != null ? this.getPrimaryLight().r : this.primaryR;
   }

   public float getPrimaryG() {
      return this.getPrimaryLight() != null ? this.getPrimaryLight().g : this.primaryG;
   }

   public float getPrimaryB() {
      return this.getPrimaryLight() != null ? this.getPrimaryLight().b : this.primaryB;
   }

   public void setPrimaryR(float var1) {
      this.primaryR = var1;
      if (this.getPrimaryLight() != null) {
         this.getPrimaryLight().r = var1;
      }

   }

   public void setPrimaryG(float var1) {
      this.primaryG = var1;
      if (this.getPrimaryLight() != null) {
         this.getPrimaryLight().g = var1;
      }

   }

   public void setPrimaryB(float var1) {
      this.primaryB = var1;
      if (this.getPrimaryLight() != null) {
         this.getPrimaryLight().b = var1;
      }

   }

   public void load(ByteBuffer var1, int var2, boolean var3) throws IOException {
      super.load(var1, var2, var3);
      this.lightRoom = var1.get() == 1;
      this.RoomID = var1.getInt();
      this.Activated = var1.get() == 1;
      if (var2 >= 76) {
         this.canBeModified = var1.get() == 1;
         if (this.canBeModified) {
            this.useBattery = var1.get() == 1;
            this.hasBattery = var1.get() == 1;
            if (var1.get() == 1) {
               this.bulbItem = GameWindow.ReadString(var1);
            } else {
               this.bulbItem = null;
            }

            this.power = var1.getFloat();
            this.delta = var1.getFloat();
            this.setPrimaryR(var1.getFloat());
            this.setPrimaryG(var1.getFloat());
            this.setPrimaryB(var1.getFloat());
         }
      }

      if (var2 >= 79) {
         this.lastMinuteStamp = var1.getLong();
         this.bulbBurnMinutes = var1.getInt();
      }

      this.bStreetLight = this.sprite != null && this.sprite.getProperties().Is("streetlight");
      if (this.square != null) {
         IsoRoom var4 = this.square.getRoom();
         if (var4 != null && this.lightRoom) {
            this.Activated = var4.def.bLightsActive;
            var4.lightSwitches.add(this);
         } else {
            float var5 = 0.9F;
            float var6 = 0.8F;
            float var7 = 0.7F;
            if (this.sprite != null && this.sprite.getProperties().Is("lightR")) {
               if (var2 >= 76 && this.canBeModified) {
                  var5 = this.primaryR;
                  var6 = this.primaryG;
                  var7 = this.primaryB;
               } else {
                  var5 = Float.parseFloat(this.sprite.getProperties().Val("lightR")) / 255.0F;
                  var6 = Float.parseFloat(this.sprite.getProperties().Val("lightG")) / 255.0F;
                  var7 = Float.parseFloat(this.sprite.getProperties().Val("lightB")) / 255.0F;
                  this.primaryR = var5;
                  this.primaryG = var6;
                  this.primaryB = var7;
               }
            }

            int var8 = 8;
            if (this.sprite.getProperties().Is("LightRadius") && Integer.parseInt(this.sprite.getProperties().Val("LightRadius")) > 0) {
               var8 = Integer.parseInt(this.sprite.getProperties().Val("LightRadius"));
            }

            IsoLightSource var9 = new IsoLightSource((int)this.getX(), (int)this.getY(), (int)this.getZ(), var5, var6, var7, var8);
            var9.bActive = this.Activated;
            var9.bWasActive = var9.bActive;
            var9.bHydroPowered = true;
            var9.switches.add(this);
            this.lights.add(var9);
         }

         if (SystemDisabler.doObjectStateSyncEnable && GameClient.bClient) {
            GameClient.instance.objectSyncReq.putRequestLoad(this.square);
         }

      }
   }

   public void save(ByteBuffer var1, boolean var2) throws IOException {
      super.save(var1, var2);
      var1.put((byte)(this.lightRoom ? 1 : 0));
      var1.putInt(this.RoomID);
      var1.put((byte)(this.Activated ? 1 : 0));
      var1.put((byte)(this.canBeModified ? 1 : 0));
      if (this.canBeModified) {
         var1.put((byte)(this.useBattery ? 1 : 0));
         var1.put((byte)(this.hasBattery ? 1 : 0));
         var1.put((byte)(this.hasLightBulb() ? 1 : 0));
         if (this.hasLightBulb()) {
            GameWindow.WriteString(var1, this.bulbItem);
         }

         var1.putFloat(this.power);
         var1.putFloat(this.delta);
         var1.putFloat(this.getPrimaryR());
         var1.putFloat(this.getPrimaryG());
         var1.putFloat(this.getPrimaryB());
      }

      var1.putLong(this.lastMinuteStamp);
      var1.putInt(this.bulbBurnMinutes);
   }

   public boolean onMouseLeftClick(int var1, int var2) {
      return false;
   }

   public boolean canSwitchLight() {
      if (this.bulbItem != null) {
         boolean var1 = GameTime.instance.NightsSurvived < SandboxOptions.instance.getElecShutModifier();
         boolean var2 = var1 ? this.square.getRoom() != null || this.bStreetLight : this.square.haveElectricity();
         if (!var2 && this.getCell() != null) {
            for(int var3 = 0; var3 >= (this.getZ() >= 1.0F ? -1 : 0); --var3) {
               for(int var4 = -1; var4 < 2; ++var4) {
                  for(int var5 = -1; var5 < 2; ++var5) {
                     if (var4 != 0 || var5 != 0 || var3 != 0) {
                        IsoGridSquare var6 = this.getCell().getGridSquare((double)(this.getX() + (float)var4), (double)(this.getY() + (float)var5), (double)(this.getZ() + (float)var3));
                        if (var6 != null && (var1 && var6.getRoom() != null || var6.haveElectricity())) {
                           var2 = true;
                           break;
                        }
                     }
                  }

                  if (var2) {
                     break;
                  }
               }
            }
         }

         if (!this.useBattery && var2 || this.canBeModified && this.useBattery && this.hasBattery && this.power > 0.0F) {
            return true;
         }
      }

      return false;
   }

   public boolean setActive(boolean var1) {
      return this.setActive(var1, false, false);
   }

   public boolean setActive(boolean var1, boolean var2) {
      return this.setActive(var1, var2, false);
   }

   public boolean setActive(boolean var1, boolean var2, boolean var3) {
      if (this.bulbItem == null) {
         var1 = false;
      }

      if (var1 == this.Activated) {
         return this.Activated;
      } else if (this.square.getRoom() == null && !this.canBeModified) {
         return this.Activated;
      } else {
         if (var3 || this.canSwitchLight()) {
            this.Activated = var1;
            if (!var2) {
               IsoWorld.instance.getFreeEmitter().playSound("LightSwitch", this.square);
               if (this.Activated && (GameTime.instance.getHour() > 22 || GameTime.instance.getHour() < 5)) {
                  WorldSoundManager.instance.addSound((Object)null, (int)this.getX(), (int)this.getY(), (int)this.getZ(), 50, 3);
               }

               this.switchLight(this.Activated);
               this.syncIsoObject(false, (byte)(this.Activated ? 1 : 0), (UdpConnection)null);
            }
         }

         return this.Activated;
      }
   }

   public boolean toggle() {
      return this.setActive(!this.Activated);
   }

   public void switchLight(boolean var1) {
      int var2;
      if (this.lightRoom && this.square.getRoom() != null) {
         this.square.getRoom().def.bLightsActive = var1;

         for(var2 = 0; var2 < this.square.getRoom().lightSwitches.size(); ++var2) {
            ((IsoLightSwitch)this.square.getRoom().lightSwitches.get(var2)).Activated = var1;
         }

         if (GameServer.bServer) {
            var2 = this.square.getX() / 300;
            int var3 = this.square.getY() / 300;
            int var4 = this.square.getRoom().def.ID;
            GameServer.sendMetaGrid(var2, var3, var4);
         }
      }

      for(var2 = 0; var2 < this.lights.size(); ++var2) {
         IsoLightSource var5 = (IsoLightSource)this.lights.get(var2);
         var5.bActive = var1;
      }

      IsoGridSquare.RecalcLightTime = -1;
      GameTime.instance.lightSourceUpdate = 100.0F;
      IsoGenerator.updateGenerator(this.getSquare());
   }

   public void getCustomSettingsFromItem(InventoryItem var1) {
      if (var1 instanceof Moveable) {
         Moveable var2 = (Moveable)var1;
         if (var2.isLight()) {
            this.useBattery = var2.isLightUseBattery();
            this.hasBattery = var2.isLightHasBattery();
            this.bulbItem = var2.getLightBulbItem();
            this.power = var2.getLightPower();
            this.delta = var2.getLightDelta();
            this.setPrimaryR(var2.getLightR());
            this.setPrimaryG(var2.getLightG());
            this.setPrimaryB(var2.getLightB());
         }
      }

   }

   public void setCustomSettingsToItem(InventoryItem var1) {
      if (var1 instanceof Moveable) {
         Moveable var2 = (Moveable)var1;
         var2.setLightUseBattery(this.useBattery);
         var2.setLightHasBattery(this.hasBattery);
         var2.setLightBulbItem(this.bulbItem);
         var2.setLightPower(this.power);
         var2.setLightDelta(this.delta);
         var2.setLightR(this.primaryR);
         var2.setLightG(this.primaryG);
         var2.setLightB(this.primaryB);
      }

   }

   public void syncCustomizedSettings(UdpConnection var1) {
      if (GameClient.bClient) {
         this.writeCustomizedSettingsPacket(GameClient.connection);
      } else if (GameServer.bServer) {
         Iterator var2 = GameServer.udpEngine.connections.iterator();

         while(true) {
            UdpConnection var3;
            do {
               if (!var2.hasNext()) {
                  return;
               }

               var3 = (UdpConnection)var2.next();
            } while(var1 != null && var3.getConnectedGUID() == var1.getConnectedGUID());

            this.writeCustomizedSettingsPacket(var3);
         }
      }

   }

   private void writeCustomizedSettingsPacket(UdpConnection var1) {
      if (var1 != null) {
         ByteBufferWriter var2 = var1.startPacket();
         PacketTypesShort.doPacket((short)1200, var2);
         this.writeLightSwitchObjectHeader(var2, (byte)(this.Activated ? 1 : 0));
         var2.putBoolean(this.canBeModified);
         var2.putBoolean(this.useBattery);
         var2.putBoolean(this.hasBattery);
         var2.putByte((byte)(this.bulbItem != null ? 1 : 0));
         if (this.bulbItem != null) {
            GameWindow.WriteString(var2.bb, this.bulbItem);
         }

         var2.putFloat(this.power);
         var2.putFloat(this.delta);
         var2.putFloat(this.primaryR);
         var2.putFloat(this.primaryG);
         var2.putFloat(this.primaryB);
         PacketTypes.PacketType.PacketTypeShort.send(var1);
      }

   }

   private void readCustomizedSettingsPacket(ByteBuffer var1) {
      this.Activated = var1.get() == 1;
      this.canBeModified = var1.get() == 1;
      this.useBattery = var1.get() == 1;
      this.hasBattery = var1.get() == 1;
      if (var1.get() == 1) {
         this.bulbItem = GameWindow.ReadString(var1);
      } else {
         this.bulbItem = null;
      }

      this.power = var1.getFloat();
      this.delta = var1.getFloat();
      this.setPrimaryR(var1.getFloat());
      this.setPrimaryG(var1.getFloat());
      this.setPrimaryB(var1.getFloat());
   }

   public void receiveSyncCustomizedSettings(ByteBuffer var1, UdpConnection var2) {
      if (GameClient.bClient) {
         this.readCustomizedSettingsPacket(var1);
      } else if (GameServer.bServer) {
         this.readCustomizedSettingsPacket(var1);
         this.syncCustomizedSettings(var2);
      }

      this.switchLight(this.Activated);
   }

   private void writeLightSwitchObjectHeader(ByteBufferWriter var1, byte var2) {
      var1.putInt(this.square.getX());
      var1.putInt(this.square.getY());
      var1.putInt(this.square.getZ());
      var1.putByte((byte)this.square.getObjects().indexOf(this));
      var1.putByte(var2);
   }

   public void syncIsoObjectSend(ByteBufferWriter var1) {
      var1.putInt(this.square.getX());
      var1.putInt(this.square.getY());
      var1.putInt(this.square.getZ());
      byte var2 = (byte)this.square.getObjects().indexOf(this);
      var1.putByte(var2);
      var1.putByte((byte)1);
      var1.putByte((byte)(this.Activated ? 1 : 0));
   }

   public void syncIsoObject(boolean var1, byte var2, UdpConnection var3, ByteBuffer var4) {
      this.syncIsoObject(var1, var2, var3);
   }

   public void syncIsoObject(boolean var1, byte var2, UdpConnection var3) {
      if (this.square == null) {
         System.out.println("ERROR: " + this.getClass().getSimpleName() + " square is null");
      } else if (this.getObjectIndex() == -1) {
         PrintStream var10000 = System.out;
         String var10001 = this.getClass().getSimpleName();
         var10000.println("ERROR: " + var10001 + " not found on square " + this.square.getX() + "," + this.square.getY() + "," + this.square.getZ());
      } else {
         if (GameClient.bClient && !var1) {
            ByteBufferWriter var8 = GameClient.connection.startPacket();
            PacketTypes.PacketType.SyncIsoObject.doPacket(var8);
            this.syncIsoObjectSend(var8);
            PacketTypes.PacketType.SyncIsoObject.send(GameClient.connection);
         } else if (var1) {
            if (var2 == 1) {
               this.switchLight(true);
               this.Activated = true;
            } else {
               this.switchLight(false);
               this.Activated = false;
            }

            if (GameServer.bServer) {
               Iterator var4 = GameServer.udpEngine.connections.iterator();

               while(var4.hasNext()) {
                  UdpConnection var5 = (UdpConnection)var4.next();
                  ByteBufferWriter var6;
                  if (var3 != null) {
                     if (var5.getConnectedGUID() != var3.getConnectedGUID()) {
                        var6 = var5.startPacket();
                        PacketTypes.PacketType.SyncIsoObject.doPacket(var6);
                        this.syncIsoObjectSend(var6);
                        PacketTypes.PacketType.SyncIsoObject.send(var5);
                     }
                  } else if (var5.RelevantTo((float)this.square.x, (float)this.square.y)) {
                     var6 = var5.startPacket();
                     PacketTypes.PacketType.SyncIsoObject.doPacket(var6);
                     var6.putInt(this.square.getX());
                     var6.putInt(this.square.getY());
                     var6.putInt(this.square.getZ());
                     byte var7 = (byte)this.square.getObjects().indexOf(this);
                     if (var7 != -1) {
                        var6.putByte(var7);
                     } else {
                        var6.putByte((byte)this.square.getObjects().size());
                     }

                     var6.putByte((byte)1);
                     var6.putByte((byte)(this.Activated ? 1 : 0));
                     PacketTypes.PacketType.SyncIsoObject.send(var5);
                  }
               }
            }
         }

      }
   }

   public void update() {
      if (!GameServer.bServer && !GameClient.bClient || GameServer.bServer) {
         boolean var1 = false;
         if (!this.Activated) {
            this.lastMinuteStamp = -1L;
         }

         if (!this.lightRoom && this.canBeModified && this.Activated) {
            if (this.lastMinuteStamp == -1L) {
               this.lastMinuteStamp = GameTime.instance.getMinutesStamp();
            }

            if (GameTime.instance.getMinutesStamp() > this.lastMinuteStamp) {
               if (this.bulbBurnMinutes == -1) {
                  int var2 = SandboxOptions.instance.getElecShutModifier() * 24 * 60;
                  if (this.lastMinuteStamp < (long)var2) {
                     this.bulbBurnMinutes = (int)this.lastMinuteStamp;
                  } else {
                     this.bulbBurnMinutes = var2;
                  }
               }

               long var6 = GameTime.instance.getMinutesStamp() - this.lastMinuteStamp;
               this.lastMinuteStamp = GameTime.instance.getMinutesStamp();
               if (this.Activated && this.hasLightBulb()) {
                  this.bulbBurnMinutes = (int)((long)this.bulbBurnMinutes + var6);
               }

               this.nextBreakUpdate = (int)((long)this.nextBreakUpdate - var6);
               if (this.nextBreakUpdate <= 0) {
                  if (this.Activated && this.hasLightBulb()) {
                     int var4 = Rand.Next(0, 1000);
                     int var5 = this.bulbBurnMinutes / 10000;
                     if (var4 < var5) {
                        this.bulbBurnMinutes = 0;
                        this.setActive(false, true, true);
                        this.bulbItem = null;
                        IsoWorld.instance.getFreeEmitter().playSound("LightbulbBurnedOut", this.square);
                        var1 = true;
                     }
                  }

                  this.nextBreakUpdate = 60;
               }

               if (this.useBattery && this.Activated && this.hasLightBulb() && this.hasBattery && this.power > 0.0F) {
                  float var7 = this.power - this.power % 0.01F;
                  this.power -= this.delta * (float)var6;
                  if (this.power < 0.0F) {
                     this.power = 0.0F;
                  }

                  if (var6 == 1L || this.power < var7) {
                     var1 = true;
                  }
               }
            }

            if (this.useBattery && this.Activated && (this.power <= 0.0F || !this.hasBattery)) {
               this.power = 0.0F;
               this.setActive(false, true, true);
               var1 = true;
            }
         }

         if (this.Activated && !this.hasLightBulb()) {
            this.setActive(false, true, true);
            var1 = true;
         }

         if (var1 && GameServer.bServer) {
            this.syncCustomizedSettings((UdpConnection)null);
         }
      }

   }

   public boolean isActivated() {
      return this.Activated;
   }

   public void addToWorld() {
      if (!this.Activated) {
         this.lastMinuteStamp = -1L;
      }

      if (!this.lightRoom && !this.lights.isEmpty()) {
         for(int var1 = 0; var1 < this.lights.size(); ++var1) {
            IsoWorld.instance.CurrentCell.getLamppostPositions().add((IsoLightSource)this.lights.get(var1));
         }
      }

      if (this.getCell() != null && this.canBeModified && !this.lightRoom && (!GameServer.bServer && !GameClient.bClient || GameServer.bServer)) {
         this.getCell().addToStaticUpdaterObjectList(this);
      }

   }

   public void removeFromWorld() {
      if (!this.lightRoom && !this.lights.isEmpty()) {
         for(int var1 = 0; var1 < this.lights.size(); ++var1) {
            ((IsoLightSource)this.lights.get(var1)).setActive(false);
            IsoWorld.instance.CurrentCell.removeLamppost((IsoLightSource)this.lights.get(var1));
         }

         this.lights.clear();
      }

      if (this.square != null && this.lightRoom) {
         IsoRoom var2 = this.square.getRoom();
         if (var2 != null) {
            var2.lightSwitches.remove(this);
         }
      }

      super.removeFromWorld();
   }

   public static void chunkLoaded(IsoChunk var0) {
      ArrayList var1 = new ArrayList();

      int var2;
      int var4;
      for(var2 = 0; var2 < 10; ++var2) {
         for(int var3 = 0; var3 < 10; ++var3) {
            for(var4 = 0; var4 < 8; ++var4) {
               IsoGridSquare var5 = var0.getGridSquare(var2, var3, var4);
               if (var5 != null) {
                  IsoRoom var6 = var5.getRoom();
                  if (var6 != null && var6.hasLightSwitches() && !var1.contains(var6)) {
                     var1.add(var6);
                  }
               }
            }
         }
      }

      for(var2 = 0; var2 < var1.size(); ++var2) {
         IsoRoom var7 = (IsoRoom)var1.get(var2);
         var7.createLights(var7.def.bLightsActive);

         for(var4 = 0; var4 < var7.roomLights.size(); ++var4) {
            IsoRoomLight var8 = (IsoRoomLight)var7.roomLights.get(var4);
            if (!var0.roomLights.contains(var8)) {
               var0.roomLights.add(var8);
            }
         }
      }

   }

   public ArrayList getLights() {
      return this.lights;
   }
}

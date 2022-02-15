package zombie.iso.objects;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Iterator;
import zombie.GameTime;
import zombie.SandboxOptions;
import zombie.core.Core;
import zombie.core.network.ByteBufferWriter;
import zombie.core.opengl.Shader;
import zombie.core.raknet.UdpConnection;
import zombie.core.skinnedmodel.model.WorldItemModelDrawer;
import zombie.core.textures.ColorInfo;
import zombie.core.textures.Texture;
import zombie.inventory.InventoryItem;
import zombie.inventory.types.DrainableComboItem;
import zombie.iso.IsoCamera;
import zombie.iso.IsoCell;
import zombie.iso.IsoGridSquare;
import zombie.iso.IsoObject;
import zombie.iso.IsoWorld;
import zombie.iso.sprite.IsoDirectionFrame;
import zombie.iso.sprite.IsoSprite;
import zombie.iso.sprite.IsoSpriteManager;
import zombie.network.GameClient;
import zombie.network.GameServer;
import zombie.network.PacketTypes;

public class IsoCarBatteryCharger extends IsoObject {
   protected InventoryItem item;
   protected InventoryItem battery;
   protected boolean activated;
   protected float lastUpdate = -1.0F;
   protected float chargeRate = 0.16666667F;
   protected IsoSprite chargerSprite;
   protected IsoSprite batterySprite;
   protected long sound = 0L;

   public IsoCarBatteryCharger(IsoCell var1) {
      super(var1);
   }

   public IsoCarBatteryCharger(InventoryItem var1, IsoCell var2, IsoGridSquare var3) {
      super(var2, var3, (IsoSprite)null);
      if (var1 == null) {
         throw new NullPointerException("item is null");
      } else {
         this.item = var1;
      }
   }

   public String getObjectName() {
      return "IsoCarBatteryCharger";
   }

   public void load(ByteBuffer var1, int var2, boolean var3) throws IOException {
      super.load(var1, var2, var3);
      if (var1.get() == 1) {
         try {
            this.item = InventoryItem.loadItem(var1, var2);
         } catch (Exception var6) {
            var6.printStackTrace();
         }
      }

      if (var1.get() == 1) {
         try {
            this.battery = InventoryItem.loadItem(var1, var2);
         } catch (Exception var5) {
            var5.printStackTrace();
         }
      }

      this.activated = var1.get() == 1;
      this.lastUpdate = var1.getFloat();
      this.chargeRate = var1.getFloat();
   }

   public void save(ByteBuffer var1, boolean var2) throws IOException {
      super.save(var1, var2);
      if (this.item == null) {
         assert false;

         var1.put((byte)0);
      } else {
         var1.put((byte)1);
         this.item.saveWithSize(var1, false);
      }

      if (this.battery == null) {
         var1.put((byte)0);
      } else {
         var1.put((byte)1);
         this.battery.saveWithSize(var1, false);
      }

      var1.put((byte)(this.activated ? 1 : 0));
      var1.putFloat(this.lastUpdate);
      var1.putFloat(this.chargeRate);
   }

   public void addToWorld() {
      super.addToWorld();
      this.getCell().addToProcessIsoObject(this);
   }

   public void removeFromWorld() {
      this.stopChargingSound();
      super.removeFromWorld();
   }

   public void update() {
      super.update();
      if (!(this.battery instanceof DrainableComboItem)) {
         this.battery = null;
      }

      if (this.battery == null) {
         this.lastUpdate = -1.0F;
         this.activated = false;
         this.stopChargingSound();
      } else {
         boolean var1 = this.square != null && (this.square.haveElectricity() || GameTime.instance.NightsSurvived < SandboxOptions.instance.getElecShutModifier() && this.square.getRoom() != null);
         if (!var1) {
            this.activated = false;
         }

         if (!this.activated) {
            this.lastUpdate = -1.0F;
            this.stopChargingSound();
         } else {
            this.startChargingSound();
            DrainableComboItem var2 = (DrainableComboItem)this.battery;
            if (!(var2.getUsedDelta() >= 1.0F)) {
               float var3 = (float)GameTime.getInstance().getWorldAgeHours();
               if (this.lastUpdate < 0.0F) {
                  this.lastUpdate = var3;
               }

               if (this.lastUpdate > var3) {
                  this.lastUpdate = var3;
               }

               float var4 = var3 - this.lastUpdate;
               if (var4 > 0.0F) {
                  var2.setUsedDelta(Math.min(1.0F, var2.getUsedDelta() + this.chargeRate * var4));
                  this.lastUpdate = var3;
               }

            }
         }
      }
   }

   public void render(float var1, float var2, float var3, ColorInfo var4, boolean var5, boolean var6, Shader var7) {
      this.chargerSprite = this.configureSprite(this.item, this.chargerSprite);
      if (this.chargerSprite.CurrentAnim != null && !this.chargerSprite.CurrentAnim.Frames.isEmpty()) {
         Texture var8 = ((IsoDirectionFrame)this.chargerSprite.CurrentAnim.Frames.get(0)).getTexture(this.dir);
         if (var8 != null) {
            float var9 = (float)var8.getWidthOrig() * this.chargerSprite.def.getScaleX() / 2.0F;
            float var10 = (float)var8.getHeightOrig() * this.chargerSprite.def.getScaleY() * 3.0F / 4.0F;
            this.offsetX = this.offsetY = 0.0F;
            this.setAlpha(IsoCamera.frameState.playerIndex, 1.0F);
            float var11 = 0.5F;
            float var12 = 0.5F;
            float var13 = 0.0F;
            this.sx = 0.0F;
            this.item.setWorldZRotation(315);
            if (!WorldItemModelDrawer.renderMain(this.getItem(), this.getSquare(), this.getX() + var11, this.getY() + var12, this.getZ() + var13, -1.0F)) {
               this.chargerSprite.render(this, var1 + var11, var2 + var12, var3 + var13, this.dir, this.offsetX + var9 + (float)(8 * Core.TileScale), this.offsetY + var10 + (float)(4 * Core.TileScale), var4, true);
            }

            if (this.battery != null) {
               this.batterySprite = this.configureSprite(this.battery, this.batterySprite);
               if (this.batterySprite != null && this.batterySprite.CurrentAnim != null && !this.batterySprite.CurrentAnim.Frames.isEmpty()) {
                  this.sx = 0.0F;
                  this.getBattery().setWorldZRotation(90);
                  if (!WorldItemModelDrawer.renderMain(this.getBattery(), this.getSquare(), this.getX() + 0.75F, this.getY() + 0.75F, this.getZ() + var13, -1.0F)) {
                     this.batterySprite.render(this, var1 + var11, var2 + var12, var3 + var13, this.dir, this.offsetX + var9 - 8.0F + (float)Core.TileScale, this.offsetY + var10 - (float)(4 * Core.TileScale), var4, true);
                  }
               }
            }

         }
      }
   }

   public void renderObjectPicker(float var1, float var2, float var3, ColorInfo var4) {
   }

   private IsoSprite configureSprite(InventoryItem var1, IsoSprite var2) {
      String var4 = var1.getWorldTexture();

      Texture var3;
      try {
         var3 = Texture.getSharedTexture(var4);
         if (var3 == null) {
            var4 = var1.getTex().getName();
         }
      } catch (Exception var7) {
         var4 = "media/inventory/world/WItem_Sack.png";
      }

      var3 = Texture.getSharedTexture(var4);
      boolean var5 = false;
      if (var2 == null) {
         var2 = IsoSprite.CreateSprite(IsoSpriteManager.instance);
      }

      if (var2.CurrentAnim == null) {
         var2.LoadFramesNoDirPageSimple(var4);
         var2.CurrentAnim.name = var4;
         var5 = true;
      } else if (!var4.equals(var2.CurrentAnim.name)) {
         var2.ReplaceCurrentAnimFrames(var4);
         var2.CurrentAnim.name = var4;
         var5 = true;
      }

      if (var5) {
         if (var1.getScriptItem() == null) {
            var2.def.scaleAspect((float)var3.getWidthOrig(), (float)var3.getHeightOrig(), (float)(16 * Core.TileScale), (float)(16 * Core.TileScale));
         } else if (this.battery != null && this.battery.getScriptItem() != null) {
            float var10001 = (float)Core.TileScale;
            float var6 = this.battery.getScriptItem().ScaleWorldIcon * (var10001 / 2.0F);
            var2.def.setScale(var6, var6);
         }
      }

      return var2;
   }

   public void syncIsoObjectSend(ByteBufferWriter var1) {
      byte var2 = (byte)this.getObjectIndex();
      var1.putInt(this.square.getX());
      var1.putInt(this.square.getY());
      var1.putInt(this.square.getZ());
      var1.putByte(var2);
      var1.putByte((byte)1);
      var1.putByte((byte)0);
      if (this.battery == null) {
         var1.putByte((byte)0);
      } else {
         var1.putByte((byte)1);

         try {
            this.battery.saveWithSize(var1.bb, false);
         } catch (IOException var4) {
            var4.printStackTrace();
         }
      }

      var1.putBoolean(this.activated);
      var1.putFloat(this.chargeRate);
   }

   public void syncIsoObject(boolean var1, byte var2, UdpConnection var3, ByteBuffer var4) {
      if (GameClient.bClient && !var1) {
         ByteBufferWriter var9 = GameClient.connection.startPacket();
         PacketTypes.PacketType.SyncIsoObject.doPacket(var9);
         this.syncIsoObjectSend(var9);
         PacketTypes.PacketType.SyncIsoObject.send(GameClient.connection);
      } else {
         Iterator var5;
         UdpConnection var6;
         ByteBufferWriter var7;
         if (GameServer.bServer && !var1) {
            var5 = GameServer.udpEngine.connections.iterator();

            while(var5.hasNext()) {
               var6 = (UdpConnection)var5.next();
               var7 = var6.startPacket();
               PacketTypes.PacketType.SyncIsoObject.doPacket(var7);
               this.syncIsoObjectSend(var7);
               PacketTypes.PacketType.SyncIsoObject.send(var6);
            }
         } else if (var1) {
            if (var4.get() == 1) {
               try {
                  this.battery = InventoryItem.loadItem(var4, 186);
               } catch (Exception var8) {
                  var8.printStackTrace();
               }
            } else {
               this.battery = null;
            }

            this.activated = var4.get() == 1;
            this.chargeRate = var4.getFloat();
            if (GameServer.bServer) {
               var5 = GameServer.udpEngine.connections.iterator();

               while(var5.hasNext()) {
                  var6 = (UdpConnection)var5.next();
                  if (var3 != null && var6 != var3) {
                     var7 = var6.startPacket();
                     PacketTypes.PacketType.SyncIsoObject.doPacket(var7);
                     this.syncIsoObjectSend(var7);
                     PacketTypes.PacketType.SyncIsoObject.send(var6);
                  }
               }
            }
         }
      }

   }

   public void sync() {
      this.syncIsoObject(false, (byte)0, (UdpConnection)null, (ByteBuffer)null);
   }

   public InventoryItem getItem() {
      return this.item;
   }

   public InventoryItem getBattery() {
      return this.battery;
   }

   public void setBattery(InventoryItem var1) {
      if (var1 != null) {
         if (!(var1 instanceof DrainableComboItem)) {
            throw new IllegalArgumentException("battery isn't DrainableComboItem");
         }

         if (this.battery != null) {
            throw new IllegalStateException("battery already inserted");
         }
      }

      this.battery = var1;
   }

   public boolean isActivated() {
      return this.activated;
   }

   public void setActivated(boolean var1) {
      this.activated = var1;
   }

   public float getChargeRate() {
      return this.chargeRate;
   }

   public void setChargeRate(float var1) {
      if (var1 <= 0.0F) {
         throw new IllegalArgumentException("chargeRate <= 0.0f");
      } else {
         this.chargeRate = var1;
      }
   }

   private void startChargingSound() {
      if (!GameServer.bServer) {
         if (this.getObjectIndex() != -1) {
            if (this.sound != -1L) {
               if (this.emitter == null) {
                  this.emitter = IsoWorld.instance.getFreeEmitter((float)this.square.x + 0.5F, (float)this.square.y + 0.5F, (float)this.square.z);
                  IsoWorld.instance.takeOwnershipOfEmitter(this.emitter);
               }

               if (!this.emitter.isPlaying(this.sound)) {
                  this.sound = this.emitter.playSound("CarBatteryChargerRunning");
                  if (this.sound == 0L) {
                     this.sound = -1L;
                  }
               }

               this.emitter.tick();
            }
         }
      }
   }

   private void stopChargingSound() {
      if (!GameServer.bServer) {
         if (this.emitter != null) {
            this.emitter.stopOrTriggerSound(this.sound);
            this.sound = 0L;
            IsoWorld.instance.returnOwnershipOfEmitter(this.emitter);
            this.emitter = null;
         }
      }
   }
}

package zombie.iso.objects;

import java.io.IOException;
import java.io.PrintStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import zombie.GameTime;
import zombie.SandboxOptions;
import zombie.WorldSoundManager;
import zombie.core.Rand;
import zombie.core.Translator;
import zombie.core.logger.ExceptionLogger;
import zombie.core.network.ByteBufferWriter;
import zombie.core.properties.PropertyContainer;
import zombie.core.raknet.UdpConnection;
import zombie.inventory.InventoryItem;
import zombie.inventory.ItemContainer;
import zombie.inventory.types.Food;
import zombie.iso.IsoCell;
import zombie.iso.IsoChunk;
import zombie.iso.IsoGridSquare;
import zombie.iso.IsoObject;
import zombie.iso.IsoUtils;
import zombie.iso.IsoWorld;
import zombie.iso.SpriteDetails.IsoFlagType;
import zombie.iso.sprite.IsoSpriteManager;
import zombie.network.GameClient;
import zombie.network.GameServer;
import zombie.network.PacketTypes;
import zombie.network.ServerMap;

public class IsoGenerator extends IsoObject {
   public float fuel = 0.0F;
   public boolean activated = false;
   public int condition = 0;
   private int lastHour = -1;
   public boolean connected = false;
   private int numberOfElectricalItems = 0;
   private boolean updateSurrounding = false;
   private final HashMap itemsPowered = new HashMap();
   private float totalPowerUsing = 0.0F;
   private static final ArrayList AllGenerators = new ArrayList();
   private static final int GENERATOR_RADIUS = 20;

   public IsoGenerator(IsoCell var1) {
      super(var1);
   }

   public IsoGenerator(InventoryItem var1, IsoCell var2, IsoGridSquare var3) {
      super(var2, var3, IsoSpriteManager.instance.getSprite("appliances_misc_01_0"));
      if (var1 != null) {
         this.setInfoFromItem(var1);
      }

      this.sprite = IsoSpriteManager.instance.getSprite("appliances_misc_01_0");
      this.square = var3;
      var3.AddSpecialObject(this);
      if (GameClient.bClient) {
         this.transmitCompleteItemToServer();
      }

   }

   public IsoGenerator(InventoryItem var1, IsoCell var2, IsoGridSquare var3, boolean var4) {
      super(var2, var3, IsoSpriteManager.instance.getSprite("appliances_misc_01_0"));
      if (var1 != null) {
         this.setInfoFromItem(var1);
      }

      this.sprite = IsoSpriteManager.instance.getSprite("appliances_misc_01_0");
      this.square = var3;
      var3.AddSpecialObject(this);
      if (GameClient.bClient && !var4) {
         this.transmitCompleteItemToServer();
      }

   }

   public void setInfoFromItem(InventoryItem var1) {
      this.condition = var1.getCondition();
      if (var1.getModData().rawget("fuel") instanceof Double) {
         this.fuel = ((Double)var1.getModData().rawget("fuel")).floatValue();
      }

   }

   public void update() {
      if (this.updateSurrounding && this.getSquare() != null) {
         this.setSurroundingElectricity();
         this.updateSurrounding = false;
      }

      if (this.isActivated()) {
         if (!GameServer.bServer && (this.emitter == null || !this.emitter.isPlaying("GeneratorLoop"))) {
            if (this.emitter == null) {
               this.emitter = IsoWorld.instance.getFreeEmitter(this.getX() + 0.5F, this.getY() + 0.5F, (float)((int)this.getZ()));
               IsoWorld.instance.takeOwnershipOfEmitter(this.emitter);
            }

            this.emitter.playSoundLoopedImpl("GeneratorLoop");
         }

         if (GameClient.bClient) {
            this.emitter.tick();
            return;
         }

         WorldSoundManager.instance.addSoundRepeating(this, (int)this.getX(), (int)this.getY(), (int)this.getZ(), 20, 1, false);
         if ((int)GameTime.getInstance().getWorldAgeHours() != this.lastHour) {
            if (!this.getSquare().getProperties().Is(IsoFlagType.exterior) && this.getSquare().getBuilding() != null) {
               this.getSquare().getBuilding().setToxic(false);
               this.getSquare().getBuilding().setToxic(this.isActivated());
            }

            int var1 = (int)GameTime.getInstance().getWorldAgeHours() - this.lastHour;
            float var2 = 0.0F;
            int var3 = 0;

            for(int var4 = 0; var4 < var1; ++var4) {
               float var5 = this.totalPowerUsing;
               var5 = (float)((double)var5 * SandboxOptions.instance.GeneratorFuelConsumption.getValue());
               var2 += var5;
               if (Rand.Next(30) == 0) {
                  var3 += Rand.Next(2) + 1;
               }

               if (this.fuel - var2 <= 0.0F || this.condition - var3 <= 0) {
                  break;
               }
            }

            this.fuel -= var2;
            if (this.fuel <= 0.0F) {
               this.setActivated(false);
               this.fuel = 0.0F;
            }

            this.condition -= var3;
            if (this.condition <= 0) {
               this.setActivated(false);
               this.condition = 0;
            }

            if (this.condition <= 20) {
               if (Rand.Next(10) == 0) {
                  IsoFireManager.StartFire(this.getCell(), this.square, true, 1000);
                  this.condition = 0;
                  this.setActivated(false);
               } else if (Rand.Next(20) == 0) {
                  this.square.explode();
                  this.condition = 0;
                  this.setActivated(false);
               }
            }

            this.lastHour = (int)GameTime.getInstance().getWorldAgeHours();
            if (GameServer.bServer) {
               this.syncIsoObject(false, (byte)0, (UdpConnection)null, (ByteBuffer)null);
            }
         }
      }

      if (this.emitter != null) {
         this.emitter.tick();
      }

   }

   public void setSurroundingElectricity() {
      this.itemsPowered.clear();
      this.totalPowerUsing = 0.02F;
      this.numberOfElectricalItems = 1;
      boolean var1 = SandboxOptions.getInstance().AllowExteriorGenerator.getValue();
      int var2 = this.square.getX() - 20;
      int var3 = this.square.getX() + 20;
      int var4 = this.square.getY() - 20;
      int var5 = this.square.getY() + 20;
      int var6 = Math.max(0, this.getSquare().getZ() - 3);
      int var7 = Math.min(8, this.getSquare().getZ() + 3);

      int var8;
      int var9;
      int var10;
      for(var8 = var6; var8 < var7; ++var8) {
         for(var9 = var2; var9 <= var3; ++var9) {
            for(var10 = var4; var10 <= var5; ++var10) {
               if (!(IsoUtils.DistanceToSquared((float)var9 + 0.5F, (float)var10 + 0.5F, (float)this.getSquare().getX() + 0.5F, (float)this.getSquare().getY() + 0.5F) > 400.0F)) {
                  IsoGridSquare var11 = this.getCell().getGridSquare(var9, var10, var8);
                  if (var11 != null) {
                     boolean var12 = this.isActivated();
                     if (!var1 && var11.Is(IsoFlagType.exterior)) {
                        var12 = false;
                     }

                     var11.setHaveElectricity(var12);

                     for(int var13 = 0; var13 < var11.getObjects().size(); ++var13) {
                        IsoObject var14 = (IsoObject)var11.getObjects().get(var13);
                        if (var14 != null && !(var14 instanceof IsoWorldInventoryObject)) {
                           if (var14 instanceof IsoTelevision && ((IsoTelevision)var14).getDeviceData().getIsTurnedOn()) {
                              this.addPoweredItem(var14, 0.03F);
                           }

                           if (var14 instanceof IsoRadio && ((IsoRadio)var14).getDeviceData().getIsTurnedOn()) {
                              this.addPoweredItem(var14, 0.01F);
                           }

                           if (var14 instanceof IsoStove && ((IsoStove)var14).Activated()) {
                              this.addPoweredItem(var14, 0.09F);
                           }

                           boolean var15 = var14.getContainerByType("fridge") != null;
                           boolean var16 = var14.getContainerByType("freezer") != null;
                           if (var15 && var16) {
                              this.addPoweredItem(var14, 0.13F);
                           } else if (var15 || var16) {
                              this.addPoweredItem(var14, 0.08F);
                           }

                           if (var14 instanceof IsoLightSwitch && ((IsoLightSwitch)var14).Activated) {
                              this.addPoweredItem(var14, 0.002F);
                           }

                           var14.checkHaveElectricity();
                        }
                     }
                  }
               }
            }
         }
      }

      if (this.square != null && this.square.chunk != null) {
         var8 = this.square.chunk.wx;
         var9 = this.square.chunk.wy;

         for(var10 = -2; var10 <= 2; ++var10) {
            for(int var17 = -2; var17 <= 2; ++var17) {
               IsoChunk var18 = GameServer.bServer ? ServerMap.instance.getChunk(var8 + var17, var9 + var10) : IsoWorld.instance.CurrentCell.getChunk(var8 + var17, var9 + var10);
               if (var18 != null && this.touchesChunk(var18)) {
                  if (this.isActivated()) {
                     var18.addGeneratorPos(this.square.x, this.square.y, this.square.z);
                  } else {
                     var18.removeGeneratorPos(this.square.x, this.square.y, this.square.z);
                  }
               }
            }
         }

      }
   }

   private void addPoweredItem(IsoObject var1, float var2) {
      String var3 = Translator.getText("IGUI_VehiclePartCatOther");
      PropertyContainer var4 = var1.getProperties();
      if (var4 != null && var4.Is("CustomName")) {
         String var5 = "Moveable Object";
         if (var4.Is("CustomName")) {
            if (var4.Is("GroupName")) {
               String var10000 = var4.Val("GroupName");
               var5 = var10000 + " " + var4.Val("CustomName");
            } else {
               var5 = var4.Val("CustomName");
            }
         }

         var3 = Translator.getMoveableDisplayName(var5);
      }

      if (var1 instanceof IsoLightSwitch) {
         var3 = Translator.getText("IGUI_Lights");
      }

      this.totalPowerUsing -= var2;
      int var8 = 1;
      Iterator var6 = this.itemsPowered.keySet().iterator();

      while(var6.hasNext()) {
         String var7 = (String)var6.next();
         if (var7.startsWith(var3)) {
            var8 = Integer.parseInt(var7.replaceAll("[\\D]", ""));
            ++var8;
            this.itemsPowered.remove(var7);
            break;
         }
      }

      this.itemsPowered.put(var3 + " x" + var8, " (" + var2 * (float)var8 + "L/h)");
      if (var8 == 1) {
         this.totalPowerUsing += var2 * (float)(var8 + 1);
      } else {
         this.totalPowerUsing += var2 * (float)var8;
      }

   }

   private void updateFridgeFreezerItems(IsoObject var1) {
      for(int var2 = 0; var2 < var1.getContainerCount(); ++var2) {
         ItemContainer var3 = var1.getContainerByIndex(var2);
         if ("fridge".equals(var3.getType()) || "freezer".equals(var3.getType())) {
            ArrayList var4 = var3.getItems();

            for(int var5 = 0; var5 < var4.size(); ++var5) {
               InventoryItem var6 = (InventoryItem)var4.get(var5);
               if (var6 instanceof Food) {
                  var6.updateAge();
               }
            }
         }
      }

   }

   private void updateFridgeFreezerItems(IsoGridSquare var1) {
      int var2 = var1.getObjects().size();
      IsoObject[] var3 = (IsoObject[])var1.getObjects().getElements();

      for(int var4 = 0; var4 < var2; ++var4) {
         IsoObject var5 = var3[var4];
         this.updateFridgeFreezerItems(var5);
      }

   }

   private void updateFridgeFreezerItems() {
      if (this.square != null) {
         int var1 = this.square.getX() - 20;
         int var2 = this.square.getX() + 20;
         int var3 = this.square.getY() - 20;
         int var4 = this.square.getY() + 20;
         int var5 = Math.max(0, this.square.getZ() - 3);
         int var6 = Math.min(8, this.square.getZ() + 3);

         for(int var7 = var5; var7 < var6; ++var7) {
            for(int var8 = var1; var8 <= var2; ++var8) {
               for(int var9 = var3; var9 <= var4; ++var9) {
                  if (IsoUtils.DistanceToSquared((float)var8, (float)var9, (float)this.square.x, (float)this.square.y) <= 400.0F) {
                     IsoGridSquare var10 = this.getCell().getGridSquare(var8, var9, var7);
                     if (var10 != null) {
                        this.updateFridgeFreezerItems(var10);
                     }
                  }
               }
            }
         }

      }
   }

   public void load(ByteBuffer var1, int var2, boolean var3) throws IOException {
      super.load(var1, var2, var3);
      this.connected = var1.get() == 1;
      this.activated = var1.get() == 1;
      if (var2 < 138) {
         this.fuel = (float)var1.getInt();
      } else {
         this.fuel = var1.getFloat();
      }

      this.condition = var1.getInt();
      this.lastHour = var1.getInt();
      this.numberOfElectricalItems = var1.getInt();
      this.updateSurrounding = true;
   }

   public void save(ByteBuffer var1, boolean var2) throws IOException {
      super.save(var1, var2);
      var1.put((byte)(this.isConnected() ? 1 : 0));
      var1.put((byte)(this.isActivated() ? 1 : 0));
      var1.putFloat(this.getFuel());
      var1.putInt(this.getCondition());
      var1.putInt(this.lastHour);
      var1.putInt(this.numberOfElectricalItems);
   }

   public void remove() {
      if (this.getSquare() != null) {
         this.getSquare().transmitRemoveItemFromSquare(this);
      }
   }

   public void addToWorld() {
      this.getCell().addToProcessIsoObject(this);
      if (!AllGenerators.contains(this)) {
         AllGenerators.add(this);
      }

      if (GameClient.bClient) {
         GameClient.instance.objectSyncReq.putRequest(this.square, this);
      }

   }

   public void removeFromWorld() {
      AllGenerators.remove(this);
      if (this.emitter != null) {
         this.emitter.stopAll();
         IsoWorld.instance.returnOwnershipOfEmitter(this.emitter);
         this.emitter = null;
      }

      super.removeFromWorld();
   }

   public String getObjectName() {
      return "IsoGenerator";
   }

   public float getFuel() {
      return this.fuel;
   }

   public void setFuel(float var1) {
      this.fuel = var1;
      if (this.fuel > 100.0F) {
         this.fuel = 100.0F;
      }

      if (this.fuel < 0.0F) {
         this.fuel = 0.0F;
      }

      if (GameServer.bServer) {
         this.syncIsoObject(false, (byte)0, (UdpConnection)null, (ByteBuffer)null);
      }

      if (GameClient.bClient) {
         this.syncIsoObject(false, (byte)0, (UdpConnection)null, (ByteBuffer)null);
      }

   }

   public boolean isActivated() {
      return this.activated;
   }

   public void setActivated(boolean var1) {
      if (var1 != this.activated) {
         if (!this.getSquare().getProperties().Is(IsoFlagType.exterior) && this.getSquare().getBuilding() != null) {
            this.getSquare().getBuilding().setToxic(false);
            this.getSquare().getBuilding().setToxic(var1);
         }

         if (!GameServer.bServer && this.emitter == null) {
            this.emitter = IsoWorld.instance.getFreeEmitter(this.getX() + 0.5F, this.getY() + 0.5F, this.getZ());
            IsoWorld.instance.takeOwnershipOfEmitter(this.emitter);
         }

         if (var1) {
            this.lastHour = (int)GameTime.getInstance().getWorldAgeHours();
            if (this.emitter != null) {
               this.emitter.playSound("GeneratorStarting");
            }
         } else if (this.emitter != null) {
            if (!this.emitter.isEmpty()) {
               this.emitter.stopAll();
            }

            this.emitter.playSound("GeneratorStopping");
         }

         try {
            this.updateFridgeFreezerItems();
         } catch (Throwable var3) {
            ExceptionLogger.logException(var3);
         }

         this.activated = var1;
         this.setSurroundingElectricity();
         if (GameClient.bClient) {
            this.syncIsoObject(false, (byte)0, (UdpConnection)null, (ByteBuffer)null);
         }

         if (GameServer.bServer) {
            this.syncIsoObject(false, (byte)0, (UdpConnection)null, (ByteBuffer)null);
         }

      }
   }

   public void failToStart() {
      if (!GameServer.bServer) {
         if (this.emitter == null) {
            this.emitter = IsoWorld.instance.getFreeEmitter(this.getX() + 0.5F, this.getY() + 0.5F, this.getZ());
            IsoWorld.instance.takeOwnershipOfEmitter(this.emitter);
         }

         this.emitter.playSound("GeneratorFailedToStart");
      }
   }

   public int getCondition() {
      return this.condition;
   }

   public void setCondition(int var1) {
      this.condition = var1;
      if (this.condition > 100) {
         this.condition = 100;
      }

      if (this.condition < 0) {
         this.condition = 0;
      }

      if (GameServer.bServer) {
         this.syncIsoObject(false, (byte)0, (UdpConnection)null, (ByteBuffer)null);
      }

      if (GameClient.bClient) {
         this.syncIsoObject(false, (byte)0, (UdpConnection)null, (ByteBuffer)null);
      }

   }

   public boolean isConnected() {
      return this.connected;
   }

   public void setConnected(boolean var1) {
      this.connected = var1;
      if (GameClient.bClient) {
         this.syncIsoObject(false, (byte)0, (UdpConnection)null, (ByteBuffer)null);
      }

   }

   public void syncIsoObjectSend(ByteBufferWriter var1) {
      byte var2 = (byte)this.getObjectIndex();
      var1.putInt(this.square.getX());
      var1.putInt(this.square.getY());
      var1.putInt(this.square.getZ());
      var1.putByte(var2);
      var1.putByte((byte)1);
      var1.putByte((byte)0);
      var1.putFloat(this.fuel);
      var1.putInt(this.condition);
      var1.putByte((byte)(this.activated ? 1 : 0));
      var1.putByte((byte)(this.connected ? 1 : 0));
   }

   public void syncIsoObject(boolean var1, byte var2, UdpConnection var3, ByteBuffer var4) {
      if (this.square == null) {
         System.out.println("ERROR: " + this.getClass().getSimpleName() + " square is null");
      } else if (this.getObjectIndex() == -1) {
         PrintStream var10000 = System.out;
         String var10001 = this.getClass().getSimpleName();
         var10000.println("ERROR: " + var10001 + " not found on square " + this.square.getX() + "," + this.square.getY() + "," + this.square.getZ());
      } else {
         if (GameClient.bClient && !var1) {
            ByteBufferWriter var13 = GameClient.connection.startPacket();
            PacketTypes.PacketType.SyncIsoObject.doPacket(var13);
            this.syncIsoObjectSend(var13);
            PacketTypes.PacketType.SyncIsoObject.send(GameClient.connection);
         } else if (GameServer.bServer && !var1) {
            Iterator var12 = GameServer.udpEngine.connections.iterator();

            while(var12.hasNext()) {
               UdpConnection var14 = (UdpConnection)var12.next();
               ByteBufferWriter var15 = var14.startPacket();
               PacketTypes.PacketType.SyncIsoObject.doPacket(var15);
               this.syncIsoObjectSend(var15);
               PacketTypes.PacketType.SyncIsoObject.send(var14);
            }
         } else if (var1) {
            float var5 = var4.getFloat();
            int var6 = var4.getInt();
            boolean var7 = var4.get() == 1;
            boolean var8 = var4.get() == 1;
            this.sync(var5, var6, var8, var7);
            if (GameServer.bServer) {
               Iterator var9 = GameServer.udpEngine.connections.iterator();

               while(var9.hasNext()) {
                  UdpConnection var10 = (UdpConnection)var9.next();
                  if (var3 != null && var10.getConnectedGUID() != var3.getConnectedGUID()) {
                     ByteBufferWriter var11 = var10.startPacket();
                     PacketTypes.PacketType.SyncIsoObject.doPacket(var11);
                     this.syncIsoObjectSend(var11);
                     PacketTypes.PacketType.SyncIsoObject.send(var10);
                  }
               }
            }
         }

      }
   }

   public void sync(float var1, int var2, boolean var3, boolean var4) {
      this.fuel = var1;
      this.condition = var2;
      this.connected = var3;
      if (this.activated != var4) {
         try {
            this.updateFridgeFreezerItems();
         } catch (Throwable var6) {
            ExceptionLogger.logException(var6);
         }

         this.activated = var4;
         if (var4) {
            this.lastHour = (int)GameTime.getInstance().getWorldAgeHours();
         } else if (this.emitter != null) {
            this.emitter.stopAll();
         }

         this.setSurroundingElectricity();
      }

   }

   private boolean touchesChunk(IsoChunk var1) {
      IsoGridSquare var2 = this.getSquare();

      assert var2 != null;

      if (var2 == null) {
         return false;
      } else {
         int var3 = var1.wx * 10;
         int var4 = var1.wy * 10;
         int var5 = var3 + 10 - 1;
         int var6 = var4 + 10 - 1;
         if (var2.x - 20 > var5) {
            return false;
         } else if (var2.x + 20 < var3) {
            return false;
         } else if (var2.y - 20 > var6) {
            return false;
         } else {
            return var2.y + 20 >= var4;
         }
      }
   }

   public static void chunkLoaded(IsoChunk var0) {
      var0.checkForMissingGenerators();

      int var1;
      for(var1 = -2; var1 <= 2; ++var1) {
         for(int var2 = -2; var2 <= 2; ++var2) {
            if (var2 != 0 || var1 != 0) {
               IsoChunk var3 = GameServer.bServer ? ServerMap.instance.getChunk(var0.wx + var2, var0.wy + var1) : IsoWorld.instance.CurrentCell.getChunk(var0.wx + var2, var0.wy + var1);
               if (var3 != null) {
                  var3.checkForMissingGenerators();
               }
            }
         }
      }

      for(var1 = 0; var1 < AllGenerators.size(); ++var1) {
         IsoGenerator var4 = (IsoGenerator)AllGenerators.get(var1);
         if (!var4.updateSurrounding && var4.touchesChunk(var0)) {
            var4.updateSurrounding = true;
         }
      }

   }

   public static void updateSurroundingNow() {
      for(int var0 = 0; var0 < AllGenerators.size(); ++var0) {
         IsoGenerator var1 = (IsoGenerator)AllGenerators.get(var0);
         if (var1.updateSurrounding && var1.getSquare() != null) {
            var1.updateSurrounding = false;
            var1.setSurroundingElectricity();
         }
      }

   }

   public static void updateGenerator(IsoGridSquare var0) {
      if (var0 != null) {
         for(int var1 = 0; var1 < AllGenerators.size(); ++var1) {
            IsoGenerator var2 = (IsoGenerator)AllGenerators.get(var1);
            if (var2.square.getBuilding() == var0.getBuilding()) {
               var2.setSurroundingElectricity();
               var2.updateSurrounding = false;
            }
         }

      }
   }

   public static void Reset() {
      assert AllGenerators.isEmpty();

      AllGenerators.clear();
   }

   public static boolean isPoweringSquare(int var0, int var1, int var2, int var3, int var4, int var5) {
      int var6 = Math.max(0, var2 - 3);
      int var7 = Math.min(8, var2 + 3);
      if (var5 >= var6 && var5 < var7) {
         return IsoUtils.DistanceToSquared((float)var0 + 0.5F, (float)var1 + 0.5F, (float)var3 + 0.5F, (float)var4 + 0.5F) <= 400.0F;
      } else {
         return false;
      }
   }

   public ArrayList getItemsPowered() {
      ArrayList var1 = new ArrayList();
      Iterator var2 = this.itemsPowered.keySet().iterator();

      while(var2.hasNext()) {
         String var3 = (String)var2.next();
         var1.add(var3 + (String)this.itemsPowered.get(var3));
      }

      return var1;
   }

   public float getTotalPowerUsing() {
      return this.totalPowerUsing;
   }

   public void setTotalPowerUsing(float var1) {
      this.totalPowerUsing = var1;
   }
}

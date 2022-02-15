package zombie.vehicles;

import java.util.ArrayList;
import java.util.Arrays;
import zombie.GameWindow;
import zombie.core.Core;
import zombie.debug.DebugLog;
import zombie.network.GameClient;

public final class VehicleIDMap {
   public static final VehicleIDMap instance = new VehicleIDMap();
   private static int MAX_IDS = 32767;
   private static int RESIZE_COUNT = 256;
   private int capacity = 256;
   private BaseVehicle[] idToVehicle;
   private short[] freeID;
   private short freeIDSize;
   private boolean noise = false;
   private int warnCount = 0;

   VehicleIDMap() {
      this.idToVehicle = new BaseVehicle[this.capacity];
      this.freeID = new short[this.capacity];

      for(int var1 = 0; var1 < this.capacity; ++var1) {
         short[] var10000 = this.freeID;
         short var10003 = this.freeIDSize;
         this.freeIDSize = (short)(var10003 + 1);
         var10000[var10003] = (short)var1;
      }

   }

   public void put(short var1, BaseVehicle var2) {
      if (Core.bDebug && this.noise) {
         DebugLog.log("VehicleIDMap.put()" + var1);
      }

      if (GameClient.bClient && var1 >= this.capacity) {
         this.resize((var1 / RESIZE_COUNT + 1) * RESIZE_COUNT);
      }

      if (var1 >= 0 && var1 < this.capacity) {
         if (this.idToVehicle[var1] != null) {
            throw new IllegalArgumentException("duplicate vehicle with id " + var1);
         } else if (var2 == null) {
            throw new IllegalArgumentException("vehicle is null");
         } else {
            this.idToVehicle[var1] = var2;
         }
      } else {
         throw new IllegalArgumentException("invalid vehicle id " + var1 + " max=" + this.capacity);
      }
   }

   public void remove(short var1) {
      if (Core.bDebug && this.noise) {
         DebugLog.log("VehicleIDMap.remove()" + var1);
      }

      if (var1 >= 0 && var1 < this.capacity) {
         if (this.idToVehicle[var1] == null) {
            throw new IllegalArgumentException("no vehicle with id " + var1);
         } else {
            this.idToVehicle[var1] = null;
            if (!GameClient.bClient && !GameWindow.bLoadedAsClient) {
               short[] var10000 = this.freeID;
               short var10003 = this.freeIDSize;
               this.freeIDSize = (short)(var10003 + 1);
               var10000[var10003] = var1;
            }
         }
      } else {
         throw new IllegalArgumentException("invalid vehicle id=" + var1 + " max=" + this.capacity);
      }
   }

   public BaseVehicle get(short var1) {
      return var1 >= 0 && var1 < this.capacity ? this.idToVehicle[var1] : null;
   }

   public boolean containsKey(short var1) {
      return var1 >= 0 && var1 < this.capacity && this.idToVehicle[var1] != null;
   }

   public void toArrayList(ArrayList var1) {
      for(int var2 = 0; var2 < this.capacity; ++var2) {
         if (this.idToVehicle[var2] != null) {
            var1.add(this.idToVehicle[var2]);
         }
      }

   }

   public void Reset() {
      Arrays.fill(this.idToVehicle, (Object)null);
      this.freeIDSize = (short)this.capacity;

      for(short var1 = 0; var1 < this.capacity; this.freeID[var1] = var1++) {
      }

   }

   public short allocateID() {
      if (GameClient.bClient) {
         throw new RuntimeException("client must not call this");
      } else if (this.freeIDSize > 0) {
         return this.freeID[--this.freeIDSize];
      } else if (this.capacity >= MAX_IDS) {
         if (this.warnCount < 100) {
            DebugLog.log("warning: ran out of unique vehicle ids");
            ++this.warnCount;
         }

         return -1;
      } else {
         this.resize(this.capacity + RESIZE_COUNT);
         return this.allocateID();
      }
   }

   private void resize(int var1) {
      int var2 = this.capacity;
      this.capacity = Math.min(var1, MAX_IDS);
      this.capacity = Math.min(var1, 32767);
      this.idToVehicle = (BaseVehicle[])Arrays.copyOf(this.idToVehicle, this.capacity);
      this.freeID = Arrays.copyOf(this.freeID, this.capacity);

      for(int var3 = var2; var3 < this.capacity; ++var3) {
         short[] var10000 = this.freeID;
         short var10003 = this.freeIDSize;
         this.freeIDSize = (short)(var10003 + 1);
         var10000[var10003] = (short)var3;
      }

   }
}

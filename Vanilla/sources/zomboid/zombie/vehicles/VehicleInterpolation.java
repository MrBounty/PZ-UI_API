package zombie.vehicles;

import java.io.BufferedWriter;
import java.nio.ByteBuffer;
import java.util.ArrayDeque;
import java.util.LinkedList;
import java.util.ListIterator;
import org.joml.Quaternionf;
import zombie.GameTime;

public class VehicleInterpolation {
   static final boolean PR = false;
   static final boolean DEBUG = false;
   BufferedWriter DebugInDataWriter;
   BufferedWriter DebugOutDataWriter;
   public int physicsDelayMs;
   public int physicsBufferMs;
   boolean buffering;
   long serverDelay;
   VehicleInterpolationData lastData;
   LinkedList dataList = new LinkedList();
   private static final ArrayDeque pool = new ArrayDeque();
   private float[] currentVehicleData = new float[27];
   private float[] tempVehicleData = new float[27];
   private boolean isSetCurrentVehicleData = false;
   private Quaternionf javaxQuat4f = new Quaternionf();

   VehicleInterpolation(int var1) {
      this.physicsDelayMs = var1;
      this.physicsBufferMs = var1;
      this.buffering = true;
   }

   protected void finalize() {
   }

   public void interpolationDataAdd(ByteBuffer var1, float var2, float var3, float var4) {
      VehicleInterpolationData var7 = pool.isEmpty() ? new VehicleInterpolationData() : (VehicleInterpolationData)pool.pop();
      var7.time = var1.getLong();
      var7.x = var2;
      var7.y = var3;
      var7.z = var4;
      var7.qx = var1.getFloat();
      var7.qy = var1.getFloat();
      var7.qz = var1.getFloat();
      var7.qw = var1.getFloat();
      var7.vx = var1.getFloat();
      var7.vy = var1.getFloat();
      var7.vz = var1.getFloat();
      var7.setNumWheels(var1.getShort());

      for(int var8 = 0; var8 < var7.w_count; ++var8) {
         var7.w_st[var8] = var1.getFloat();
         var7.w_rt[var8] = var1.getFloat();
         var7.w_si[var8] = var1.getFloat();
         var7.w_sl[var8] = var1.getFloat();
      }

      long var5 = GameTime.getServerTime() - var7.time;
      if (Math.abs(this.serverDelay - var5) > 2000000000L) {
         this.serverDelay = var5;
      }

      this.serverDelay = (long)((double)this.serverDelay + (double)(var5 - this.serverDelay) * 0.1D);
      ListIterator var12 = this.dataList.listIterator();
      long var9 = 0L;

      while(var12.hasNext()) {
         VehicleInterpolationData var11 = (VehicleInterpolationData)var12.next();
         if (var11.time > var7.time) {
            if (var12.hasPrevious()) {
               var12.previous();
               var12.add(var7);
            } else {
               this.dataList.addFirst(var7);
            }

            return;
         }

         if (var7.time - var11.time > (long)((this.physicsBufferMs + this.physicsDelayMs) * 1000000)) {
            pool.push(var11);
            var12.remove();
         } else if (var11.time > var9) {
            var9 = var11.time;
         }
      }

      if (var9 == 0L || var7.time - var9 > (long)((this.physicsBufferMs + this.physicsDelayMs) * 1000000)) {
         if (!this.dataList.isEmpty()) {
            pool.addAll(this.dataList);
            this.dataList.clear();
         }

         this.buffering = true;
      }

      this.dataList.addLast(var7);
   }

   public boolean interpolationDataGet(float[] var1) {
      VehicleInterpolationData var2 = null;
      VehicleInterpolationData var3 = null;
      long var4 = GameTime.getServerTime() - this.serverDelay - (long)(this.physicsDelayMs * 1000000);
      if (this.dataList.size() == 2 && ((VehicleInterpolationData)this.dataList.getFirst()).time == ((VehicleInterpolationData)this.dataList.getLast()).time) {
         this.dataList.removeFirst();
         var3 = (VehicleInterpolationData)this.dataList.getLast();
      } else {
         ListIterator var6;
         if (!this.buffering) {
            if (this.dataList.size() == 0) {
               this.buffering = true;
               return false;
            }
         } else {
            var6 = this.dataList.listIterator();
            long var7 = 0L;
            long var9 = 0L;

            while(true) {
               if (!var6.hasNext()) {
                  if (var7 != 0L && var9 - var7 >= (long)(this.physicsDelayMs * 1000000)) {
                     this.buffering = false;
                     break;
                  }

                  return false;
               }

               VehicleInterpolationData var11 = (VehicleInterpolationData)var6.next();
               if (var7 == 0L || var11.time < var7) {
                  var7 = var11.time;
               }

               if (var11.time > var9) {
                  var9 = var11.time;
               }
            }
         }

         if (this.physicsDelayMs <= 0) {
            var3 = (VehicleInterpolationData)this.dataList.getFirst();
         } else {
            var6 = this.dataList.listIterator();

            VehicleInterpolationData var15;
            while(var6.hasNext()) {
               var15 = (VehicleInterpolationData)var6.next();
               if (var15.time >= var4) {
                  var3 = var15;
                  if (!var6.hasPrevious()) {
                     return false;
                  }

                  var6.previous();
                  if (!var6.hasPrevious()) {
                     return false;
                  }

                  var2 = (VehicleInterpolationData)var6.previous();
                  break;
               }
            }

            while(var6.hasPrevious()) {
               var15 = (VehicleInterpolationData)var6.previous();
               pool.push(var15);
               var6.remove();
            }
         }

         if (var3 == null) {
            this.buffering = true;
            if (!this.dataList.isEmpty()) {
               pool.addAll(this.dataList);
               this.dataList.clear();
            }

            return false;
         }
      }

      int var17;
      if (var2 == null) {
         byte var13 = 0;
         int var14 = var13 + 1;
         var1[var13] = var3.x;
         var1[var14++] = var3.y;
         var1[var14++] = var3.z;
         var1[var14++] = var3.qx;
         var1[var14++] = var3.qy;
         var1[var14++] = var3.qz;
         var1[var14++] = var3.qw;
         var1[var14++] = var3.vx;
         var1[var14++] = var3.vy;
         var1[var14++] = var3.vz;
         var1[var14++] = (float)var3.w_count;

         for(var17 = 0; var17 < var3.w_count; ++var17) {
            var1[var14++] = var3.w_st[var17];
            var1[var14++] = var3.w_rt[var17];
            var1[var14++] = var3.w_si[var17];
            var1[var14++] = var3.w_sl[var17];
         }

         return true;
      } else {
         float var12 = (float)(var4 - var2.time) / (float)(var3.time - var2.time);
         byte var16 = 0;
         var17 = var16 + 1;
         var1[var16] = (var3.x - var2.x) * var12 + var2.x;
         var1[var17++] = (var3.y - var2.y) * var12 + var2.y;
         var1[var17++] = (var3.z - var2.z) * var12 + var2.z;
         float var8 = var3.qx * var2.qx + var3.qy * var2.qy + var3.qz * var2.qz + var3.qw * var2.qw;
         if (var8 < 0.0F) {
            var3.qx *= -1.0F;
            var3.qy *= -1.0F;
            var3.qz *= -1.0F;
            var3.qw *= -1.0F;
         }

         var1[var17++] = var2.qx * (1.0F - var12) + var3.qx * var12;
         var1[var17++] = var2.qy * (1.0F - var12) + var3.qy * var12;
         var1[var17++] = var2.qz * (1.0F - var12) + var3.qz * var12;
         var1[var17++] = var2.qw * (1.0F - var12) + var3.qw * var12;
         var1[var17++] = (var3.vx - var2.vx) * var12 + var2.vx;
         var1[var17++] = (var3.vy - var2.vy) * var12 + var2.vy;
         var1[var17++] = (var3.vz - var2.vz) * var12 + var2.vz;
         var1[var17++] = (float)var3.w_count;

         for(int var18 = 0; var18 < var3.w_count; ++var18) {
            var1[var17++] = (var3.w_st[var18] - var2.w_st[var18]) * var12 + var2.w_st[var18];
            var1[var17++] = (var3.w_rt[var18] - var2.w_rt[var18]) * var12 + var2.w_rt[var18];
            var1[var17++] = (var3.w_si[var18] - var2.w_si[var18]) * var12 + var2.w_si[var18];
            var1[var17++] = (var3.w_sl[var18] - var2.w_sl[var18]) * var12 + var2.w_sl[var18];
         }

         return true;
      }
   }

   public boolean interpolationDataGetPR(float[] var1) {
      return this.interpolationDataGet(var1);
   }

   public void setVehicleData(BaseVehicle var1) {
      if (!this.dataList.isEmpty()) {
         pool.addAll(this.dataList);
         this.dataList.clear();
      }

   }

   public void poolData() {
      if (!this.dataList.isEmpty()) {
         pool.addAll(this.dataList);
         this.dataList.clear();
      }
   }
}

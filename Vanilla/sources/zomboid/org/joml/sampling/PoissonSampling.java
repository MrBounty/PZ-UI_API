package org.joml.sampling;

import java.util.ArrayList;
import org.joml.Random;
import org.joml.Vector2f;

public class PoissonSampling {
   public static class Disk {
      private final Vector2f[] grid;
      private final float diskRadius;
      private final float diskRadiusSquared;
      private final float minDist;
      private final float minDistSquared;
      private final float cellSize;
      private final int numCells;
      private final Random rnd;
      private final ArrayList processList;

      public Disk(long var1, float var3, float var4, int var5, Callback2d var6) {
         this.diskRadius = var3;
         this.diskRadiusSquared = var3 * var3;
         this.minDist = var4;
         this.minDistSquared = var4 * var4;
         this.rnd = new Random(var1);
         this.cellSize = var4 / (float)Math.sqrt(2.0D);
         this.numCells = (int)(var3 * 2.0F / this.cellSize) + 1;
         this.grid = new Vector2f[this.numCells * this.numCells];
         this.processList = new ArrayList();
         this.compute(var5, var6);
      }

      private void compute(int var1, Callback2d var2) {
         float var3;
         float var4;
         do {
            var3 = this.rnd.nextFloat() * 2.0F - 1.0F;
            var4 = this.rnd.nextFloat() * 2.0F - 1.0F;
         } while(var3 * var3 + var4 * var4 > 1.0F);

         Vector2f var5 = new Vector2f(var3, var4);
         this.processList.add(var5);
         var2.onNewSample(var5.x, var5.y);
         this.insert(var5);

         while(!this.processList.isEmpty()) {
            int var6 = this.rnd.nextInt(this.processList.size());
            Vector2f var7 = (Vector2f)this.processList.get(var6);
            boolean var8 = false;

            for(int var9 = 0; var9 < var1; ++var9) {
               float var10 = this.rnd.nextFloat() * 6.2831855F;
               float var11 = this.minDist * (this.rnd.nextFloat() + 1.0F);
               var3 = (float)((double)var11 * Math.sin_roquen_9((double)var10 + 1.5707963267948966D));
               var4 = (float)((double)var11 * Math.sin_roquen_9((double)var10));
               var3 += var7.x;
               var4 += var7.y;
               if (!(var3 * var3 + var4 * var4 > this.diskRadiusSquared) && !this.searchNeighbors(var3, var4)) {
                  var8 = true;
                  var2.onNewSample(var3, var4);
                  Vector2f var12 = new Vector2f(var3, var4);
                  this.processList.add(var12);
                  this.insert(var12);
                  break;
               }
            }

            if (!var8) {
               this.processList.remove(var6);
            }
         }

      }

      private boolean searchNeighbors(float var1, float var2) {
         int var3 = (int)((var2 + this.diskRadius) / this.cellSize);
         int var4 = (int)((var1 + this.diskRadius) / this.cellSize);
         if (this.grid[var3 * this.numCells + var4] != null) {
            return true;
         } else {
            int var5 = Math.max(0, var4 - 1);
            int var6 = Math.max(0, var3 - 1);
            int var7 = Math.min(var4 + 1, this.numCells - 1);
            int var8 = Math.min(var3 + 1, this.numCells - 1);

            for(int var9 = var6; var9 <= var8; ++var9) {
               for(int var10 = var5; var10 <= var7; ++var10) {
                  Vector2f var11 = this.grid[var9 * this.numCells + var10];
                  if (var11 != null) {
                     float var12 = var11.x - var1;
                     float var13 = var11.y - var2;
                     if (var12 * var12 + var13 * var13 < this.minDistSquared) {
                        return true;
                     }
                  }
               }
            }

            return false;
         }
      }

      private void insert(Vector2f var1) {
         int var2 = (int)((var1.y + this.diskRadius) / this.cellSize);
         int var3 = (int)((var1.x + this.diskRadius) / this.cellSize);
         this.grid[var2 * this.numCells + var3] = var1;
      }
   }
}

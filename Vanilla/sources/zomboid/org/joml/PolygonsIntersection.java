package org.joml;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class PolygonsIntersection {
   private static final PolygonsIntersection.ByStartComparator byStartComparator = new PolygonsIntersection.ByStartComparator();
   private static final PolygonsIntersection.ByEndComparator byEndComparator = new PolygonsIntersection.ByEndComparator();
   protected final float[] verticesXY;
   private float minX;
   private float minY;
   private float maxX;
   private float maxY;
   private float centerX;
   private float centerY;
   private float radiusSquared;
   private PolygonsIntersection.IntervalTreeNode tree;

   public PolygonsIntersection(float[] var1, int[] var2, int var3) {
      this.verticesXY = var1;
      this.preprocess(var3, var2);
   }

   private PolygonsIntersection.IntervalTreeNode buildNode(List var1, float var2) {
      ArrayList var3 = null;
      ArrayList var4 = null;
      ArrayList var5 = null;
      ArrayList var6 = null;
      float var7 = 1.0E38F;
      float var8 = -1.0E38F;
      float var9 = 1.0E38F;
      float var10 = -1.0E38F;
      float var11 = 1.0E38F;
      float var12 = -1.0E38F;

      for(int var13 = 0; var13 < var1.size(); ++var13) {
         PolygonsIntersection.Interval var14 = (PolygonsIntersection.Interval)var1.get(var13);
         if (var14.start < var2 && var14.end < var2) {
            if (var3 == null) {
               var3 = new ArrayList();
            }

            var3.add(var14);
            var7 = var7 < var14.start ? var7 : var14.start;
            var8 = var8 > var14.end ? var8 : var14.end;
         } else if (var14.start > var2 && var14.end > var2) {
            if (var4 == null) {
               var4 = new ArrayList();
            }

            var4.add(var14);
            var9 = var9 < var14.start ? var9 : var14.start;
            var10 = var10 > var14.end ? var10 : var14.end;
         } else {
            if (var5 == null || var6 == null) {
               var5 = new ArrayList();
               var6 = new ArrayList();
            }

            var11 = var14.start < var11 ? var14.start : var11;
            var12 = var14.end > var12 ? var14.end : var12;
            var5.add(var14);
            var6.add(var14);
         }
      }

      if (var5 != null) {
         Collections.sort(var5, byStartComparator);
         Collections.sort(var6, byEndComparator);
      }

      PolygonsIntersection.IntervalTreeNode var15 = new PolygonsIntersection.IntervalTreeNode();
      var15.byBeginning = var5;
      var15.byEnding = var6;
      var15.center = var2;
      if (var3 != null) {
         var15.left = this.buildNode(var3, (var7 + var8) / 2.0F);
         var15.left.childrenMinMax = var8;
      }

      if (var4 != null) {
         var15.right = this.buildNode(var4, (var9 + var10) / 2.0F);
         var15.right.childrenMinMax = var9;
      }

      return var15;
   }

   private void preprocess(int var1, int[] var2) {
      int var4 = 0;
      this.minX = this.minY = 1.0E38F;
      this.maxX = this.maxY = -1.0E38F;
      ArrayList var5 = new ArrayList(var1);
      int var6 = 0;
      int var7 = 0;

      int var3;
      float var8;
      float var9;
      PolygonsIntersection.Interval var11;
      float var14;
      for(var3 = 1; var3 < var1; var4 = var3++) {
         if (var2 != null && var2.length > var7 && var2[var7] == var3) {
            var8 = this.verticesXY[2 * (var3 - 1) + 1];
            var9 = this.verticesXY[2 * var6 + 1];
            PolygonsIntersection.Interval var10 = new PolygonsIntersection.Interval();
            var10.start = var8 < var9 ? var8 : var9;
            var10.end = var9 > var8 ? var9 : var8;
            var10.i = var3 - 1;
            var10.j = var6;
            var10.polyIndex = var7;
            var5.add(var10);
            var6 = var3;
            ++var7;
            ++var3;
            var4 = var3 - 1;
         }

         var8 = this.verticesXY[2 * var3 + 1];
         var9 = this.verticesXY[2 * var3 + 0];
         var14 = this.verticesXY[2 * var4 + 1];
         this.minX = var9 < this.minX ? var9 : this.minX;
         this.minY = var8 < this.minY ? var8 : this.minY;
         this.maxX = var9 > this.maxX ? var9 : this.maxX;
         this.maxY = var8 > this.maxY ? var8 : this.maxY;
         var11 = new PolygonsIntersection.Interval();
         var11.start = var8 < var14 ? var8 : var14;
         var11.end = var14 > var8 ? var14 : var8;
         var11.i = var3;
         var11.j = var4;
         var11.polyIndex = var7;
         var5.add(var11);
      }

      var8 = this.verticesXY[2 * (var3 - 1) + 1];
      var9 = this.verticesXY[2 * (var3 - 1) + 0];
      var14 = this.verticesXY[2 * var6 + 1];
      this.minX = var9 < this.minX ? var9 : this.minX;
      this.minY = var8 < this.minY ? var8 : this.minY;
      this.maxX = var9 > this.maxX ? var9 : this.maxX;
      this.maxY = var8 > this.maxY ? var8 : this.maxY;
      var11 = new PolygonsIntersection.Interval();
      var11.start = var8 < var14 ? var8 : var14;
      var11.end = var14 > var8 ? var14 : var8;
      var11.i = var3 - 1;
      var11.j = var6;
      var11.polyIndex = var7;
      var5.add(var11);
      this.centerX = (this.maxX + this.minX) * 0.5F;
      this.centerY = (this.maxY + this.minY) * 0.5F;
      float var12 = this.maxX - this.centerX;
      float var13 = this.maxY - this.centerY;
      this.radiusSquared = var12 * var12 + var13 * var13;
      this.tree = this.buildNode(var5, this.centerY);
   }

   public boolean testPoint(float var1, float var2) {
      return this.testPoint(var1, var2, (BitSet)null);
   }

   public boolean testPoint(float var1, float var2, BitSet var3) {
      float var4 = var1 - this.centerX;
      float var5 = var2 - this.centerY;
      if (var3 != null) {
         var3.clear();
      }

      if (var4 * var4 + var5 * var5 > this.radiusSquared) {
         return false;
      } else if (!(this.maxX < var1) && !(this.maxY < var2) && !(this.minX > var1) && !(this.minY > var2)) {
         boolean var6 = this.tree.traverse(this.verticesXY, var1, var2, false, var3);
         return var6;
      } else {
         return false;
      }
   }

   static class Interval {
      float start;
      float end;
      int i;
      int j;
      int polyIndex;
   }

   static class ByStartComparator implements Comparator {
      public int compare(Object var1, Object var2) {
         PolygonsIntersection.Interval var3 = (PolygonsIntersection.Interval)var1;
         PolygonsIntersection.Interval var4 = (PolygonsIntersection.Interval)var2;
         return Float.compare(var3.start, var4.start);
      }
   }

   static class ByEndComparator implements Comparator {
      public int compare(Object var1, Object var2) {
         PolygonsIntersection.Interval var3 = (PolygonsIntersection.Interval)var1;
         PolygonsIntersection.Interval var4 = (PolygonsIntersection.Interval)var2;
         return Float.compare(var4.end, var3.end);
      }
   }

   static class IntervalTreeNode {
      float center;
      float childrenMinMax;
      PolygonsIntersection.IntervalTreeNode left;
      PolygonsIntersection.IntervalTreeNode right;
      List byBeginning;
      List byEnding;

      static boolean computeEvenOdd(float[] var0, PolygonsIntersection.Interval var1, float var2, float var3, boolean var4, BitSet var5) {
         boolean var6 = var4;
         int var7 = var1.i;
         int var8 = var1.j;
         float var9 = var0[2 * var7 + 1];
         float var10 = var0[2 * var8 + 1];
         float var11 = var0[2 * var7 + 0];
         float var12 = var0[2 * var8 + 0];
         if ((var9 < var3 && var10 >= var3 || var10 < var3 && var9 >= var3) && (var11 <= var2 || var12 <= var2)) {
            float var13 = var11 + (var3 - var9) / (var10 - var9) * (var12 - var11) - var2;
            var6 = var4 ^ var13 < 0.0F;
            if (var6 != var4 && var5 != null) {
               var5.flip(var1.polyIndex);
            }
         }

         return var6;
      }

      boolean traverse(float[] var1, float var2, float var3, boolean var4, BitSet var5) {
         boolean var6 = var4;
         int var7;
         int var8;
         PolygonsIntersection.Interval var9;
         if (var3 == this.center && this.byBeginning != null) {
            var7 = this.byBeginning.size();

            for(var8 = 0; var8 < var7; ++var8) {
               var9 = (PolygonsIntersection.Interval)this.byBeginning.get(var8);
               var6 = computeEvenOdd(var1, var9, var2, var3, var6, var5);
            }
         } else if (var3 < this.center) {
            if (this.left != null && this.left.childrenMinMax >= var3) {
               var6 = this.left.traverse(var1, var2, var3, var4, var5);
            }

            if (this.byBeginning != null) {
               var7 = this.byBeginning.size();

               for(var8 = 0; var8 < var7; ++var8) {
                  var9 = (PolygonsIntersection.Interval)this.byBeginning.get(var8);
                  if (var9.start > var3) {
                     break;
                  }

                  var6 = computeEvenOdd(var1, var9, var2, var3, var6, var5);
               }
            }
         } else if (var3 > this.center) {
            if (this.right != null && this.right.childrenMinMax <= var3) {
               var6 = this.right.traverse(var1, var2, var3, var4, var5);
            }

            if (this.byEnding != null) {
               var7 = this.byEnding.size();

               for(var8 = 0; var8 < var7; ++var8) {
                  var9 = (PolygonsIntersection.Interval)this.byEnding.get(var8);
                  if (var9.end < var3) {
                     break;
                  }

                  var6 = computeEvenOdd(var1, var9, var2, var3, var6, var5);
               }
            }
         }

         return var6;
      }
   }
}

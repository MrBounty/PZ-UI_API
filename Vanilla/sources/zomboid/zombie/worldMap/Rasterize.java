package zombie.worldMap;

import java.util.function.BiConsumer;

public final class Rasterize {
   final Rasterize.Edge edge1 = new Rasterize.Edge();
   final Rasterize.Edge edge2 = new Rasterize.Edge();
   final Rasterize.Edge edge3 = new Rasterize.Edge();

   void scanLine(int var1, int var2, int var3, BiConsumer var4) {
      for(int var5 = var1; var5 < var2; ++var5) {
         var4.accept(var5, var3);
      }

   }

   void scanSpan(Rasterize.Edge var1, Rasterize.Edge var2, int var3, int var4, BiConsumer var5) {
      int var6 = (int)Math.max((double)var3, Math.floor((double)var2.y0));
      int var7 = (int)Math.min((double)var4, Math.ceil((double)var2.y1));
      Rasterize.Edge var8;
      if (var1.x0 == var2.x0 && var1.y0 == var2.y0) {
         if (var1.x0 + var2.dy / var1.dy * var1.dx < var2.x1) {
            var8 = var1;
            var1 = var2;
            var2 = var8;
         }
      } else if (var1.x1 - var2.dy / var1.dy * var1.dx < var2.x0) {
         var8 = var1;
         var1 = var2;
         var2 = var8;
      }

      double var21 = (double)(var1.dx / var1.dy);
      double var10 = (double)(var2.dx / var2.dy);
      double var12 = var1.dx > 0.0F ? 1.0D : 0.0D;
      double var14 = var2.dx < 0.0F ? 1.0D : 0.0D;

      for(int var16 = var6; var16 < var7; ++var16) {
         double var17 = var21 * Math.max(0.0D, Math.min((double)var1.dy, (double)var16 + var12 - (double)var1.y0)) + (double)var1.x0;
         double var19 = var10 * Math.max(0.0D, Math.min((double)var2.dy, (double)var16 + var14 - (double)var2.y0)) + (double)var2.x0;
         this.scanLine((int)Math.floor(var19), (int)Math.ceil(var17), var16, var5);
      }

   }

   void scanTriangle(float var1, float var2, float var3, float var4, float var5, float var6, int var7, int var8, BiConsumer var9) {
      Rasterize.Edge var10 = this.edge1.init(var1, var2, var3, var4);
      Rasterize.Edge var11 = this.edge2.init(var3, var4, var5, var6);
      Rasterize.Edge var12 = this.edge3.init(var5, var6, var1, var2);
      Rasterize.Edge var13;
      if (var10.dy > var12.dy) {
         var13 = var10;
         var10 = var12;
         var12 = var13;
      }

      if (var11.dy > var12.dy) {
         var13 = var11;
         var11 = var12;
         var12 = var13;
      }

      if (var10.dy > 0.0F) {
         this.scanSpan(var12, var10, var7, var8, var9);
      }

      if (var11.dy > 0.0F) {
         this.scanSpan(var12, var11, var7, var8, var9);
      }

   }

   private static final class Edge {
      float x0;
      float y0;
      float x1;
      float y1;
      float dx;
      float dy;

      Rasterize.Edge init(float var1, float var2, float var3, float var4) {
         if (var2 > var4) {
            this.x0 = var3;
            this.y0 = var4;
            this.x1 = var1;
            this.y1 = var2;
         } else {
            this.x0 = var1;
            this.y0 = var2;
            this.x1 = var3;
            this.y1 = var4;
         }

         this.dx = this.x1 - this.x0;
         this.dy = this.y1 - this.y0;
         return this;
      }
   }
}

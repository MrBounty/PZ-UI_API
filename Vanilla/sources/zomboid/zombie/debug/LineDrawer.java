package zombie.debug;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.function.Consumer;
import zombie.characters.IsoPlayer;
import zombie.core.SpriteRenderer;
import zombie.core.textures.Texture;
import zombie.iso.IsoCamera;
import zombie.iso.IsoUtils;
import zombie.iso.PlayerCamera;
import zombie.iso.Vector2;

public final class LineDrawer {
   private static final long serialVersionUID = -8792265397633463907L;
   public static int red = 0;
   public static int green = 255;
   public static int blue = 0;
   public static int alpha = 255;
   static int idLayer = -1;
   static final ArrayList lines = new ArrayList();
   static final ArrayDeque pool = new ArrayDeque();
   private static int layer;
   static final Vector2 tempo = new Vector2();
   static final Vector2 tempo2 = new Vector2();

   static void DrawTexturedRect(Texture var0, float var1, float var2, float var3, float var4, int var5, float var6, float var7, float var8) {
      var1 = (float)((int)var1);
      var2 = (float)((int)var2);
      Vector2 var9 = new Vector2(var1, var2);
      Vector2 var10 = new Vector2(var1 + var3, var2);
      Vector2 var11 = new Vector2(var1 + var3, var2 + var4);
      Vector2 var12 = new Vector2(var1, var2 + var4);
      Vector2 var13 = new Vector2(IsoUtils.XToScreen(var9.x, var9.y, (float)var5, 0), IsoUtils.YToScreen(var9.x, var9.y, (float)var5, 0));
      Vector2 var14 = new Vector2(IsoUtils.XToScreen(var10.x, var10.y, (float)var5, 0), IsoUtils.YToScreen(var10.x, var10.y, (float)var5, 0));
      Vector2 var15 = new Vector2(IsoUtils.XToScreen(var11.x, var11.y, (float)var5, 0), IsoUtils.YToScreen(var11.x, var11.y, (float)var5, 0));
      Vector2 var16 = new Vector2(IsoUtils.XToScreen(var12.x, var12.y, (float)var5, 0), IsoUtils.YToScreen(var12.x, var12.y, (float)var5, 0));
      PlayerCamera var17 = IsoCamera.cameras[IsoPlayer.getPlayerIndex()];
      var13.x -= var17.OffX;
      var14.x -= var17.OffX;
      var15.x -= var17.OffX;
      var16.x -= var17.OffX;
      var13.y -= var17.OffY;
      var14.y -= var17.OffY;
      var15.y -= var17.OffY;
      var16.y -= var17.OffY;
      float var18 = -240.0F;
      var18 -= 128.0F;
      float var19 = -32.0F;
      var13.y -= var18;
      var14.y -= var18;
      var15.y -= var18;
      var16.y -= var18;
      var13.x -= var19;
      var14.x -= var19;
      var15.x -= var19;
      var16.x -= var19;
      SpriteRenderer.instance.renderdebug(var0, var13.x, var13.y, var14.x, var14.y, var15.x, var15.y, var16.x, var16.y, var6, var7, var8, 1.0F, var6, var7, var8, 1.0F, var6, var7, var8, 1.0F, var6, var7, var8, 1.0F, (Consumer)null);
   }

   static void DrawIsoLine(float var0, float var1, float var2, float var3, float var4, float var5, float var6, float var7, int var8) {
      tempo.set(var0, var1);
      tempo2.set(var2, var3);
      Vector2 var9 = new Vector2(IsoUtils.XToScreen(tempo.x, tempo.y, 0.0F, 0), IsoUtils.YToScreen(tempo.x, tempo.y, 0.0F, 0));
      Vector2 var10 = new Vector2(IsoUtils.XToScreen(tempo2.x, tempo2.y, 0.0F, 0), IsoUtils.YToScreen(tempo2.x, tempo2.y, 0.0F, 0));
      var9.x -= IsoCamera.getOffX();
      var10.x -= IsoCamera.getOffX();
      var9.y -= IsoCamera.getOffY();
      var10.y -= IsoCamera.getOffY();
      drawLine(var9.x, var9.y, var10.x, var10.y, var4, var5, var6, var7, var8);
   }

   public static void DrawIsoRect(float var0, float var1, float var2, float var3, int var4, float var5, float var6, float var7) {
      if (var2 < 0.0F) {
         var2 = -var2;
         var0 -= var2;
      }

      if (var3 < 0.0F) {
         var3 = -var3;
         var1 -= var3;
      }

      float var8 = IsoUtils.XToScreenExact(var0, var1, (float)var4, 0);
      float var9 = IsoUtils.YToScreenExact(var0, var1, (float)var4, 0);
      float var10 = IsoUtils.XToScreenExact(var0 + var2, var1, (float)var4, 0);
      float var11 = IsoUtils.YToScreenExact(var0 + var2, var1, (float)var4, 0);
      float var12 = IsoUtils.XToScreenExact(var0 + var2, var1 + var3, (float)var4, 0);
      float var13 = IsoUtils.YToScreenExact(var0 + var2, var1 + var3, (float)var4, 0);
      float var14 = IsoUtils.XToScreenExact(var0, var1 + var3, (float)var4, 0);
      float var15 = IsoUtils.YToScreenExact(var0, var1 + var3, (float)var4, 0);
      drawLine(var8, var9, var10, var11, var5, var6, var7);
      drawLine(var10, var11, var12, var13, var5, var6, var7);
      drawLine(var12, var13, var14, var15, var5, var6, var7);
      drawLine(var14, var15, var8, var9, var5, var6, var7);
   }

   public static void DrawIsoRectRotated(float var0, float var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8, float var9) {
      Vector2 var10 = tempo.setLengthAndDirection(var5, 1.0F);
      Vector2 var11 = tempo2.set(var10);
      var11.tangent();
      var10.x *= var4 / 2.0F;
      var10.y *= var4 / 2.0F;
      var11.x *= var3 / 2.0F;
      var11.y *= var3 / 2.0F;
      float var12 = var0 + var10.x;
      float var13 = var1 + var10.y;
      float var14 = var0 - var10.x;
      float var15 = var1 - var10.y;
      float var16 = var12 - var11.x;
      float var17 = var13 - var11.y;
      float var18 = var12 + var11.x;
      float var19 = var13 + var11.y;
      float var20 = var14 - var11.x;
      float var21 = var15 - var11.y;
      float var22 = var14 + var11.x;
      float var23 = var15 + var11.y;
      byte var24 = 1;
      DrawIsoLine(var16, var17, var2, var18, var19, var2, var6, var7, var8, var9, var24);
      DrawIsoLine(var16, var17, var2, var20, var21, var2, var6, var7, var8, var9, var24);
      DrawIsoLine(var18, var19, var2, var22, var23, var2, var6, var7, var8, var9, var24);
      DrawIsoLine(var20, var21, var2, var22, var23, var2, var6, var7, var8, var9, var24);
   }

   public static void DrawIsoLine(float var0, float var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8, float var9, int var10) {
      float var11 = IsoUtils.XToScreenExact(var0, var1, var2, 0);
      float var12 = IsoUtils.YToScreenExact(var0, var1, var2, 0);
      float var13 = IsoUtils.XToScreenExact(var3, var4, var5, 0);
      float var14 = IsoUtils.YToScreenExact(var3, var4, var5, 0);
      drawLine(var11, var12, var13, var14, var6, var7, var8, var9, var10);
   }

   public static void DrawIsoTransform(float var0, float var1, float var2, float var3, float var4, float var5, int var6, float var7, float var8, float var9, float var10, int var11) {
      DrawIsoCircle(var0, var1, var2, var5, var6, var7, var8, var9, var10);
      DrawIsoLine(var0, var1, var2, var0 + var3 + var5 / 2.0F, var1 + var4 + var5 / 2.0F, var2, var7, var8, var9, var10, var11);
   }

   public static void DrawIsoCircle(float var0, float var1, float var2, float var3, float var4, float var5, float var6, float var7) {
      byte var8 = 16;
      DrawIsoCircle(var0, var1, var2, var3, var8, var4, var5, var6, var7);
   }

   public static void DrawIsoCircle(float var0, float var1, float var2, float var3, int var4, float var5, float var6, float var7, float var8) {
      double var9 = (double)var0 + (double)var3 * Math.cos(Math.toRadians((double)(0.0F / (float)var4)));
      double var11 = (double)var1 + (double)var3 * Math.sin(Math.toRadians((double)(0.0F / (float)var4)));

      for(int var13 = 1; var13 <= var4; ++var13) {
         double var14 = (double)var0 + (double)var3 * Math.cos(Math.toRadians((double)((float)var13 * 360.0F / (float)var4)));
         double var16 = (double)var1 + (double)var3 * Math.sin(Math.toRadians((double)((float)var13 * 360.0F / (float)var4)));
         addLine((float)var9, (float)var11, var2, (float)var14, (float)var16, var2, var5, var6, var7, var8);
         var9 = var14;
         var11 = var16;
      }

   }

   static void drawLine(float var0, float var1, float var2, float var3, float var4, float var5, float var6) {
      SpriteRenderer.instance.renderline((Texture)null, (int)var0 - 1, (int)var1 - 1, (int)var2 - 1, (int)var3 - 1, 0.0F, 0.0F, 0.0F, 0.5F);
      SpriteRenderer.instance.renderline((Texture)null, (int)var0, (int)var1, (int)var2, (int)var3, var4, var5, var6, 1.0F);
   }

   public static void drawLine(float var0, float var1, float var2, float var3, float var4, float var5, float var6, float var7, int var8) {
      SpriteRenderer.instance.renderline((Texture)null, (int)var0, (int)var1, (int)var2, (int)var3, var4, var5, var6, var7);
   }

   public static void drawRect(float var0, float var1, float var2, float var3, float var4, float var5, float var6, float var7, int var8) {
      SpriteRenderer.instance.render((Texture)null, var0, var1 + (float)var8, (float)var8, var3 - (float)(var8 * 2), var4, var5, var6, var7, (Consumer)null);
      SpriteRenderer.instance.render((Texture)null, var0, var1, var2, (float)var8, var4, var5, var6, var7, (Consumer)null);
      SpriteRenderer.instance.render((Texture)null, var0 + var2 - (float)var8, var1 + (float)var8, 1.0F, var3 - (float)(var8 * 2), var4, var5, var6, var7, (Consumer)null);
      SpriteRenderer.instance.render((Texture)null, var0, var1 + var3 - (float)var8, var2, (float)var8, var4, var5, var6, var7, (Consumer)null);
   }

   public static void drawArc(float var0, float var1, float var2, float var3, float var4, float var5, int var6, float var7, float var8, float var9, float var10) {
      float var11 = var4 + (float)Math.acos((double)var5);
      float var12 = var4 - (float)Math.acos((double)var5);
      float var13 = var0 + (float)Math.cos((double)var11) * var3;
      float var14 = var1 + (float)Math.sin((double)var11) * var3;

      for(int var15 = 1; var15 <= var6; ++var15) {
         float var16 = var11 + (var12 - var11) * (float)var15 / (float)var6;
         float var17 = var0 + (float)Math.cos((double)var16) * var3;
         float var18 = var1 + (float)Math.sin((double)var16) * var3;
         DrawIsoLine(var13, var14, var2, var17, var18, var2, var7, var8, var9, var10, 1);
         var13 = var17;
         var14 = var18;
      }

   }

   public static void drawCircle(float var0, float var1, float var2, int var3, float var4, float var5, float var6) {
      double var7 = (double)var0 + (double)var2 * Math.cos(Math.toRadians((double)(0.0F / (float)var3)));
      double var9 = (double)var1 + (double)var2 * Math.sin(Math.toRadians((double)(0.0F / (float)var3)));

      for(int var11 = 1; var11 <= var3; ++var11) {
         double var12 = (double)var0 + (double)var2 * Math.cos(Math.toRadians((double)((float)var11 * 360.0F / (float)var3)));
         double var14 = (double)var1 + (double)var2 * Math.sin(Math.toRadians((double)((float)var11 * 360.0F / (float)var3)));
         drawLine((float)var7, (float)var9, (float)var12, (float)var14, var4, var5, var6, 1.0F, 1);
         var7 = var12;
         var9 = var14;
      }

   }

   public static void drawDirectionLine(float var0, float var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8, int var9) {
      float var10 = var0 + (float)Math.cos((double)var4) * var3;
      float var11 = var1 + (float)Math.sin((double)var4) * var3;
      DrawIsoLine(var0, var1, var2, var10, var11, var2, var5, var6, var7, var8, var9);
   }

   public static void drawDotLines(float var0, float var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8, float var9, int var10) {
      drawDirectionLine(var0, var1, var2, var3, var4 + (float)Math.acos((double)var5), var6, var7, var8, var9, var10);
      drawDirectionLine(var0, var1, var2, var3, var4 - (float)Math.acos((double)var5), var6, var7, var8, var9, var10);
   }

   public static void addLine(float var0, float var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8, float var9) {
      LineDrawer.DrawableLine var10 = pool.isEmpty() ? new LineDrawer.DrawableLine() : (LineDrawer.DrawableLine)pool.pop();
      lines.add(var10.init(var0, var1, var2, var3, var4, var5, var6, var7, var8, var9));
   }

   public static void addLine(float var0, float var1, float var2, float var3, float var4, float var5, int var6, int var7, int var8, String var9) {
      addLine(var0, var1, var2, var3, var4, var5, (float)var6, (float)var7, (float)var8, var9, true);
   }

   public static void addLine(float var0, float var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8, String var9, boolean var10) {
      LineDrawer.DrawableLine var11 = pool.isEmpty() ? new LineDrawer.DrawableLine() : (LineDrawer.DrawableLine)pool.pop();
      lines.add(var11.init(var0, var1, var2, var3, var4, var5, var6, var7, var8, var9, var10));
   }

   public static void addRect(float var0, float var1, float var2, float var3, float var4, float var5, float var6, float var7) {
      LineDrawer.DrawableLine var8 = pool.isEmpty() ? new LineDrawer.DrawableLine() : (LineDrawer.DrawableLine)pool.pop();
      lines.add(var8.init(var0, var1, var2, var0 + var3, var1 + var4, var2, var5, var6, var7, (String)null, false));
   }

   public static void clear() {
      if (!lines.isEmpty()) {
         for(int var0 = 0; var0 < lines.size(); ++var0) {
            pool.push((LineDrawer.DrawableLine)lines.get(var0));
         }

         lines.clear();
      }
   }

   public void removeLine(String var1) {
      for(int var2 = 0; var2 < lines.size(); ++var2) {
         if (((LineDrawer.DrawableLine)lines.get(var2)).name.equals(var1)) {
            lines.remove(lines.get(var2));
            --var2;
         }
      }

   }

   public static void render() {
      for(int var0 = 0; var0 < lines.size(); ++var0) {
         LineDrawer.DrawableLine var1 = (LineDrawer.DrawableLine)lines.get(var0);
         if (!var1.bLine) {
            DrawIsoRect(var1.xstart, var1.ystart, var1.xend - var1.xstart, var1.yend - var1.ystart, (int)var1.zstart, var1.red, var1.green, var1.blue);
         } else {
            DrawIsoLine(var1.xstart, var1.ystart, var1.zstart, var1.xend, var1.yend, var1.zend, var1.red, var1.green, var1.blue, var1.alpha, 1);
         }
      }

   }

   public static void drawLines() {
      clear();
   }

   static class DrawableLine {
      public boolean bLine = false;
      String name;
      float red;
      float green;
      float blue;
      float alpha;
      float xstart;
      float ystart;
      float zstart;
      float xend;
      float yend;
      float zend;

      public LineDrawer.DrawableLine init(float var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8, float var9, String var10) {
         this.xstart = var1;
         this.ystart = var2;
         this.zstart = var3;
         this.xend = var4;
         this.yend = var5;
         this.zend = var6;
         this.red = var7;
         this.green = var8;
         this.blue = var9;
         this.alpha = 1.0F;
         this.name = var10;
         return this;
      }

      public LineDrawer.DrawableLine init(float var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8, float var9, String var10, boolean var11) {
         this.xstart = var1;
         this.ystart = var2;
         this.zstart = var3;
         this.xend = var4;
         this.yend = var5;
         this.zend = var6;
         this.red = var7;
         this.green = var8;
         this.blue = var9;
         this.alpha = 1.0F;
         this.name = var10;
         this.bLine = var11;
         return this;
      }

      public LineDrawer.DrawableLine init(float var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8, float var9, float var10) {
         this.xstart = var1;
         this.ystart = var2;
         this.zstart = var3;
         this.xend = var4;
         this.yend = var5;
         this.zend = var6;
         this.red = var7;
         this.green = var8;
         this.blue = var9;
         this.alpha = var10;
         this.name = null;
         this.bLine = true;
         return this;
      }

      public boolean equals(Object var1) {
         if (var1 instanceof LineDrawer.DrawableLine) {
            return ((LineDrawer.DrawableLine)var1).name.equals(this.name);
         } else {
            return var1.equals(this);
         }
      }
   }
}

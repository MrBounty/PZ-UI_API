package zombie.ui;

import java.util.ArrayList;
import java.util.Vector;
import java.util.function.Consumer;
import se.krka.kahlua.vm.KahluaTable;
import zombie.IndieGL;
import zombie.Lua.LuaManager;
import zombie.core.BoxedStaticValues;
import zombie.core.Color;
import zombie.core.Core;
import zombie.core.SpriteRenderer;
import zombie.core.math.PZMath;
import zombie.core.textures.Texture;
import zombie.debug.DebugOptions;
import zombie.input.Mouse;

public class UIElement {
   static final Color tempcol = new Color(0, 0, 0, 0);
   static final ArrayList toAdd = new ArrayList(0);
   static Texture white;
   static int StencilLevel = 0;
   public boolean capture = false;
   public boolean IgnoreLossControl = false;
   public String clickedValue = null;
   public final ArrayList Controls = new ArrayList();
   public boolean defaultDraw = true;
   public boolean followGameWorld = false;
   private int renderThisPlayerOnly = -1;
   public float height = 256.0F;
   public UIElement Parent = null;
   public boolean visible = true;
   public float width = 256.0F;
   public double x = 0.0D;
   public double y = 0.0D;
   public KahluaTable table;
   public boolean alwaysBack;
   public boolean bScrollChildren = false;
   public boolean bScrollWithParent = true;
   private boolean bRenderClippedChildren = true;
   public boolean anchorTop = true;
   public boolean anchorLeft = true;
   public boolean anchorRight = false;
   public boolean anchorBottom = false;
   public int playerContext = -1;
   boolean alwaysOnTop = false;
   int maxDrawHeight = -1;
   Double yScroll = 0.0D;
   Double xScroll = 0.0D;
   int scrollHeight = 0;
   double lastheight = -1.0D;
   double lastwidth = -1.0D;
   boolean bResizeDirty = false;
   boolean enabled = true;
   private final ArrayList toTop = new ArrayList(0);
   private boolean bConsumeMouseEvents = true;
   private long leftDownTime = 0L;
   private boolean clicked;
   private double clickX;
   private double clickY;
   private String uiname = "";
   private boolean bWantKeyEvents = false;
   private boolean bForceCursorVisible = false;

   public UIElement() {
   }

   public UIElement(KahluaTable var1) {
      this.table = var1;
   }

   public Double getMaxDrawHeight() {
      return BoxedStaticValues.toDouble((double)this.maxDrawHeight);
   }

   public void setMaxDrawHeight(double var1) {
      this.maxDrawHeight = (int)var1;
   }

   public void clearMaxDrawHeight() {
      this.maxDrawHeight = -1;
   }

   public Double getXScroll() {
      return this.xScroll;
   }

   public void setXScroll(double var1) {
      this.xScroll = var1;
   }

   public Double getYScroll() {
      return this.yScroll;
   }

   public void setYScroll(double var1) {
      this.yScroll = var1;
   }

   public void setAlwaysOnTop(boolean var1) {
      this.alwaysOnTop = var1;
   }

   public void backMost() {
      this.alwaysBack = true;
   }

   public void AddChild(UIElement var1) {
      this.getControls().add(var1);
      var1.setParent(this);
   }

   public void RemoveChild(UIElement var1) {
      this.getControls().remove(var1);
      var1.setParent((UIElement)null);
   }

   public Double getScrollHeight() {
      return BoxedStaticValues.toDouble((double)this.scrollHeight);
   }

   public void setScrollHeight(double var1) {
      this.scrollHeight = (int)var1;
   }

   public boolean isConsumeMouseEvents() {
      return this.bConsumeMouseEvents;
   }

   public void setConsumeMouseEvents(boolean var1) {
      this.bConsumeMouseEvents = var1;
   }

   public void ClearChildren() {
      this.getControls().clear();
   }

   public void ButtonClicked(String var1) {
      this.setClickedValue(var1);
   }

   public void DrawText(UIFont var1, String var2, double var3, double var5, double var7, double var9, double var11, double var13, double var15) {
      TextManager.instance.DrawString(var1, var3 + this.getAbsoluteX() + this.xScroll, var5 + this.getAbsoluteY() + this.yScroll, (double)((float)var7), var2, var9, var11, var13, var15);
   }

   public void DrawText(String var1, double var2, double var4, double var6, double var8, double var10, double var12) {
      TextManager.instance.DrawString(var2 + this.getAbsoluteX() + this.xScroll, var4 + this.getAbsoluteY() + this.yScroll, var1, var6, var8, var10, var12);
   }

   public void DrawText(String var1, double var2, double var4, double var6, double var8, double var10, double var12, double var14, double var16) {
      TextManager.instance.DrawString(var2 + this.getAbsoluteX() + this.xScroll, var4 + this.getAbsoluteY() + this.yScroll, var1, var10, var12, var14, var16);
   }

   public void DrawText(UIFont var1, String var2, double var3, double var5, double var7, double var9, double var11, double var13) {
      if (var2 != null) {
         int var15 = (int)(var5 + this.getAbsoluteY() + this.yScroll);
         if (var15 + 100 >= 0 && var15 <= 4096) {
            TextManager.instance.DrawString(var1, var3 + this.getAbsoluteX() + this.xScroll, var5 + this.getAbsoluteY() + this.yScroll, var2, var7, var9, var11, var13);
         }
      }
   }

   public void DrawTextUntrimmed(UIFont var1, String var2, double var3, double var5, double var7, double var9, double var11, double var13) {
      if (var2 != null) {
         TextManager.instance.DrawStringUntrimmed(var1, var3 + this.getAbsoluteX() + this.xScroll, var5 + this.getAbsoluteY() + this.yScroll, var2, var7, var9, var11, var13);
      }
   }

   public void DrawTextCentre(String var1, double var2, double var4, double var6, double var8, double var10, double var12) {
      TextManager.instance.DrawStringCentre(var2 + this.getAbsoluteX() + this.xScroll, var4 + this.getAbsoluteY() + this.yScroll, var1, var6, var8, var10, var12);
   }

   public void DrawTextCentre(UIFont var1, String var2, double var3, double var5, double var7, double var9, double var11, double var13) {
      TextManager.instance.DrawStringCentre(var1, var3 + this.getAbsoluteX() + this.xScroll, var5 + this.getAbsoluteY() + this.yScroll, var2, var7, var9, var11, var13);
   }

   public void DrawTextRight(String var1, double var2, double var4, double var6, double var8, double var10, double var12) {
      TextManager.instance.DrawStringRight(var2 + this.getAbsoluteX() + this.xScroll, var4 + this.getAbsoluteY() + this.yScroll, var1, var6, var8, var10, var12);
   }

   public void DrawTextRight(UIFont var1, String var2, double var3, double var5, double var7, double var9, double var11, double var13) {
      TextManager.instance.DrawStringRight(var1, var3 + this.getAbsoluteX() + this.xScroll, var5 + this.getAbsoluteY() + this.yScroll, var2, var7, var9, var11, var13);
   }

   public void DrawTextureAngle(Texture var1, double var2, double var4, double var6, double var8, double var10, double var12, double var14) {
      if (this.isVisible()) {
         float var16 = (float)(var1.getWidth() / 2);
         float var17 = (float)(var1.getHeight() / 2);
         double var18 = Math.toRadians(180.0D + var6);
         double var20 = Math.cos(var18) * (double)var16;
         double var22 = Math.sin(var18) * (double)var16;
         double var24 = Math.cos(var18) * (double)var17;
         double var26 = Math.sin(var18) * (double)var17;
         double var28 = var20 - var26;
         double var30 = var24 + var22;
         double var32 = -var20 - var26;
         double var34 = var24 - var22;
         double var36 = -var20 + var26;
         double var38 = -var24 - var22;
         double var40 = var20 + var26;
         double var42 = -var24 + var22;
         var28 += this.getAbsoluteX() + var2;
         var30 += this.getAbsoluteY() + var4;
         var32 += this.getAbsoluteX() + var2;
         var34 += this.getAbsoluteY() + var4;
         var36 += this.getAbsoluteX() + var2;
         var38 += this.getAbsoluteY() + var4;
         var40 += this.getAbsoluteX() + var2;
         var42 += this.getAbsoluteY() + var4;
         SpriteRenderer.instance.render(var1, var28, var30, var32, var34, var36, var38, var40, var42, (float)var8, (float)var10, (float)var12, (float)var14, (float)var8, (float)var10, (float)var12, (float)var14, (float)var8, (float)var10, (float)var12, (float)var14, (float)var8, (float)var10, (float)var12, (float)var14, (Consumer)null);
      }
   }

   public void DrawTextureAngle(Texture var1, double var2, double var4, double var6) {
      this.DrawTextureAngle(var1, var2, var4, var6, 1.0D, 1.0D, 1.0D, 1.0D);
   }

   public void DrawTexture(Texture var1, double var2, double var4, double var6, double var8, double var10, double var12, double var14, double var16, double var18, double var20, double var22, double var24) {
      SpriteRenderer.instance.render(var1, var2, var4, var6, var8, var10, var12, var14, var16, (float)var18, (float)var20, (float)var22, (float)var24, (float)var18, (float)var20, (float)var22, (float)var24, (float)var18, (float)var20, (float)var22, (float)var24, (float)var18, (float)var20, (float)var22, (float)var24, (Consumer)null);
   }

   public void DrawTexture(Texture var1, double var2, double var4, double var6) {
      if (this.isVisible()) {
         double var8 = var2 + this.getAbsoluteX();
         double var10 = var4 + this.getAbsoluteY();
         var8 += (double)var1.offsetX;
         var10 += (double)var1.offsetY;
         int var12 = (int)(var10 + this.yScroll);
         if (var12 + var1.getHeight() >= 0 && var12 <= 4096) {
            SpriteRenderer.instance.renderi(var1, (int)(var8 + this.xScroll), (int)(var10 + this.yScroll), var1.getWidth(), var1.getHeight(), 1.0F, 1.0F, 1.0F, (float)var6, (Consumer)null);
         }
      }
   }

   public void DrawTextureCol(Texture var1, double var2, double var4, Color var6) {
      if (this.isVisible()) {
         double var7 = var2 + this.getAbsoluteX();
         double var9 = var4 + this.getAbsoluteY();
         int var11 = 0;
         int var12 = 0;
         if (var1 != null) {
            var7 += (double)var1.offsetX;
            var9 += (double)var1.offsetY;
            var11 = var1.getWidth();
            var12 = var1.getHeight();
         }

         int var13 = (int)(var9 + this.yScroll);
         if (var13 + var12 >= 0 && var13 <= 4096) {
            SpriteRenderer.instance.renderi(var1, (int)(var7 + this.xScroll), (int)(var9 + this.yScroll), var11, var12, var6.r, var6.g, var6.b, var6.a, (Consumer)null);
         }
      }
   }

   public void DrawTextureScaled(Texture var1, double var2, double var4, double var6, double var8, double var10) {
      if (this.isVisible()) {
         double var12 = var2 + this.getAbsoluteX();
         double var14 = var4 + this.getAbsoluteY();
         SpriteRenderer.instance.renderi(var1, (int)(var12 + this.xScroll), (int)(var14 + this.yScroll), (int)var6, (int)var8, 1.0F, 1.0F, 1.0F, (float)var10, (Consumer)null);
      }
   }

   public void DrawTextureScaledUniform(Texture var1, double var2, double var4, double var6, double var8, double var10, double var12, double var14) {
      if (this.isVisible() && var1 != null) {
         double var16 = var2 + this.getAbsoluteX();
         double var18 = var4 + this.getAbsoluteY();
         var16 += (double)var1.offsetX * var6;
         var18 += (double)var1.offsetY * var6;
         SpriteRenderer.instance.renderi(var1, (int)(var16 + this.xScroll), (int)(var18 + this.yScroll), (int)((double)var1.getWidth() * var6), (int)((double)var1.getHeight() * var6), (float)var8, (float)var10, (float)var12, (float)var14, (Consumer)null);
      }
   }

   public void DrawTextureScaledAspect(Texture var1, double var2, double var4, double var6, double var8, double var10, double var12, double var14, double var16) {
      if (this.isVisible() && var1 != null) {
         double var18 = var2 + this.getAbsoluteX();
         double var20 = var4 + this.getAbsoluteY();
         if (var1.getWidth() > 0 && var1.getHeight() > 0 && var6 > 0.0D && var8 > 0.0D) {
            double var22 = Math.min(var6 / (double)var1.getWidthOrig(), var8 / (double)var1.getHeightOrig());
            double var24 = var6;
            double var26 = var8;
            var6 = (double)var1.getWidth() * var22;
            var8 = (double)var1.getHeight() * var22;
            var18 -= (var6 - var24) / 2.0D;
            var20 -= (var8 - var26) / 2.0D;
         }

         SpriteRenderer.instance.renderi(var1, (int)(var18 + this.xScroll), (int)(var20 + this.yScroll), (int)var6, (int)var8, (float)var10, (float)var12, (float)var14, (float)var16, (Consumer)null);
      }
   }

   public void DrawTextureScaledAspect2(Texture var1, double var2, double var4, double var6, double var8, double var10, double var12, double var14, double var16) {
      if (this.isVisible() && var1 != null) {
         double var18 = var2 + this.getAbsoluteX();
         double var20 = var4 + this.getAbsoluteY();
         if (var1.getWidth() > 0 && var1.getHeight() > 0 && var6 > 0.0D && var8 > 0.0D) {
            double var22 = Math.min(var6 / (double)var1.getWidth(), var8 / (double)var1.getHeight());
            double var24 = var6;
            double var26 = var8;
            var6 = (double)var1.getWidth() * var22;
            var8 = (double)var1.getHeight() * var22;
            var18 -= (var6 - var24) / 2.0D;
            var20 -= (var8 - var26) / 2.0D;
         }

         SpriteRenderer.instance.render(var1, (float)((int)(var18 + this.xScroll)), (float)((int)(var20 + this.yScroll)), (float)((int)var6), (float)((int)var8), (float)var10, (float)var12, (float)var14, (float)var16, (Consumer)null);
      }
   }

   public void DrawTextureScaledCol(Texture var1, double var2, double var4, double var6, double var8, double var10, double var12, double var14, double var16) {
      if (var1 != null) {
         boolean var18 = false;
      }

      if (this.isVisible()) {
         double var23 = var2 + this.getAbsoluteX();
         double var20 = var4 + this.getAbsoluteY();
         int var22 = (int)(var20 + this.yScroll);
         if (!((double)var22 + var8 < 0.0D) && var22 <= 4096) {
            SpriteRenderer.instance.renderi(var1, (int)(var23 + this.xScroll), (int)(var20 + this.yScroll), (int)var6, (int)var8, (float)var10, (float)var12, (float)var14, (float)var16, (Consumer)null);
         }
      }
   }

   public void DrawTextureScaledCol(Texture var1, double var2, double var4, double var6, double var8, Color var10) {
      if (var1 != null) {
         boolean var11 = false;
      }

      if (this.isVisible()) {
         double var15 = var2 + this.getAbsoluteX();
         double var13 = var4 + this.getAbsoluteY();
         SpriteRenderer.instance.render(var1, (float)((int)(var15 + this.xScroll)), (float)((int)(var13 + this.yScroll)), (float)((int)var6), (float)((int)var8), var10.r, var10.g, var10.b, var10.a, (Consumer)null);
      }
   }

   public void DrawTextureScaledColor(Texture var1, Double var2, Double var3, Double var4, Double var5, Double var6, Double var7, Double var8, Double var9) {
      this.DrawTextureScaledCol(var1, var2, var3, var4, var5, var6, var7, var8, var9);
   }

   public void DrawTextureColor(Texture var1, double var2, double var4, double var6, double var8, double var10, double var12) {
      tempcol.r = (float)var6;
      tempcol.g = (float)var8;
      tempcol.b = (float)var10;
      tempcol.a = (float)var12;
      this.DrawTextureCol(var1, var2, var4, tempcol);
   }

   public void DrawSubTextureRGBA(Texture var1, double var2, double var4, double var6, double var8, double var10, double var12, double var14, double var16, double var18, double var20, double var22, double var24) {
      if (var1 != null && this.isVisible() && !(var6 <= 0.0D) && !(var8 <= 0.0D) && !(var14 <= 0.0D) && !(var16 <= 0.0D)) {
         double var26 = var10 + this.getAbsoluteX() + this.xScroll;
         double var28 = var12 + this.getAbsoluteY() + this.yScroll;
         var26 += (double)var1.offsetX;
         var28 += (double)var1.offsetY;
         if (!(var28 + var16 < 0.0D) && !(var28 > 4096.0D)) {
            float var30 = PZMath.clamp((float)var2, 0.0F, (float)var1.getWidth());
            float var31 = PZMath.clamp((float)var4, 0.0F, (float)var1.getHeight());
            float var32 = PZMath.clamp((float)((double)var30 + var6), 0.0F, (float)var1.getWidth()) - var30;
            float var33 = PZMath.clamp((float)((double)var31 + var8), 0.0F, (float)var1.getHeight()) - var31;
            float var34 = var30 / (float)var1.getWidth();
            float var35 = var31 / (float)var1.getHeight();
            float var36 = (var30 + var32) / (float)var1.getWidth();
            float var37 = (var31 + var33) / (float)var1.getHeight();
            float var38 = var1.getXEnd() - var1.getXStart();
            float var39 = var1.getYEnd() - var1.getYStart();
            var34 = var1.getXStart() + var34 * var38;
            var36 = var1.getXStart() + var36 * var38;
            var35 = var1.getYStart() + var35 * var39;
            var37 = var1.getYStart() + var37 * var39;
            SpriteRenderer.instance.render(var1, (float)var26, (float)var28, (float)var14, (float)var16, (float)var18, (float)var20, (float)var22, (float)var24, var34, var35, var36, var35, var36, var37, var34, var37);
         }
      }
   }

   public void DrawTextureTiled(Texture var1, double var2, double var4, double var6, double var8, double var10, double var12, double var14, double var16) {
      if (var1 != null && this.isVisible() && !(var6 <= 0.0D) && !(var8 <= 0.0D)) {
         for(double var18 = var4; var18 < var4 + var8; var18 += (double)var1.getHeight()) {
            for(double var20 = var2; var20 < var2 + var6; var20 += (double)var1.getWidth()) {
               double var22 = (double)var1.getWidth();
               double var24 = (double)var1.getHeight();
               if (var20 + var22 > var2 + var6) {
                  var22 = var2 + var6 - var20;
               }

               if (var18 + (double)var1.getHeight() > var4 + var8) {
                  var24 = var4 + var8 - var18;
               }

               this.DrawSubTextureRGBA(var1, 0.0D, 0.0D, var22, var24, var20, var18, var22, var24, var10, var12, var14, var16);
            }
         }

      }
   }

   public void DrawTextureTiledX(Texture var1, double var2, double var4, double var6, double var8, double var10, double var12, double var14, double var16) {
      if (var1 != null && this.isVisible() && !(var6 <= 0.0D) && !(var8 <= 0.0D)) {
         for(double var18 = var2; var18 < var2 + var6; var18 += (double)var1.getWidth()) {
            double var20 = (double)var1.getWidth();
            double var22 = (double)var1.getHeight();
            if (var18 + var20 > var2 + var6) {
               var20 = var2 + var6 - var18;
            }

            this.DrawSubTextureRGBA(var1, 0.0D, 0.0D, var20, var22, var18, var4, var20, var22, var10, var12, var14, var16);
         }

      }
   }

   public void DrawTextureTiledY(Texture var1, double var2, double var4, double var6, double var8, double var10, double var12, double var14, double var16) {
      if (var1 != null && this.isVisible() && !(var6 <= 0.0D) && !(var8 <= 0.0D)) {
         for(double var18 = var4; var18 < var4 + var8; var18 += (double)var1.getHeight()) {
            double var20 = (double)var1.getWidth();
            double var22 = (double)var1.getHeight();
            if (var18 + (double)var1.getHeight() > var4 + var8) {
               var22 = var4 + var8 - var18;
            }

            this.DrawSubTextureRGBA(var1, 0.0D, 0.0D, var20, var22, var2, var18, var20, var22, var10, var12, var14, var16);
         }

      }
   }

   public void DrawTextureIgnoreOffset(Texture var1, double var2, double var4, int var6, int var7, Color var8) {
      if (this.isVisible()) {
         double var9 = var2 + this.getAbsoluteX();
         double var11 = var4 + this.getAbsoluteY();
         SpriteRenderer.instance.render(var1, (float)((int)(var9 + this.xScroll)), (float)((int)(var11 + this.yScroll)), (float)var6, (float)var7, var8.r, var8.g, var8.b, var8.a, (Consumer)null);
      }
   }

   public void DrawTexture_FlippedX(Texture var1, double var2, double var4, int var6, int var7, Color var8) {
      if (this.isVisible()) {
         double var9 = var2 + this.getAbsoluteX();
         double var11 = var4 + this.getAbsoluteY();
         SpriteRenderer.instance.renderflipped(var1, (float)(var9 + this.xScroll), (float)(var11 + this.yScroll), (float)var6, (float)var7, var8.r, var8.g, var8.b, var8.a, (Consumer)null);
      }
   }

   public void DrawTexture_FlippedXIgnoreOffset(Texture var1, double var2, double var4, int var6, int var7, Color var8) {
      if (this.isVisible()) {
         double var9 = var2 + this.getAbsoluteX();
         double var11 = var4 + this.getAbsoluteY();
         SpriteRenderer.instance.renderflipped(var1, (float)(var9 + this.xScroll), (float)(var11 + this.yScroll), (float)var6, (float)var7, var8.r, var8.g, var8.b, var8.a, (Consumer)null);
      }
   }

   public void DrawUVSliceTexture(Texture var1, double var2, double var4, double var6, double var8, Color var10, double var11, double var13, double var15, double var17) {
      if (this.isVisible()) {
         double var19 = var2 + this.getAbsoluteX();
         double var21 = var4 + this.getAbsoluteY();
         var19 += (double)var1.offsetX;
         var21 += (double)var1.offsetY;
         Texture.lr = var10.r;
         Texture.lg = var10.g;
         Texture.lb = var10.b;
         Texture.la = var10.a;
         double var23 = (double)var1.getXStart();
         double var25 = (double)var1.getYStart();
         double var27 = (double)var1.getXEnd();
         double var29 = (double)var1.getYEnd();
         double var31 = var27 - var23;
         double var33 = var29 - var25;
         double var35 = var15 - var11;
         double var37 = var17 - var13;
         double var39 = var35 / 1.0D;
         double var41 = var37 / 1.0D;
         var23 += var11 * var31;
         var25 += var13 * var33;
         var27 -= (1.0D - var15) * var31;
         var29 -= (1.0D - var17) * var33;
         var23 = (double)((float)((int)(var23 * 1000.0D)) / 1000.0F);
         var27 = (double)((float)((int)(var27 * 1000.0D)) / 1000.0F);
         var25 = (double)((float)((int)(var25 * 1000.0D)) / 1000.0F);
         var29 = (double)((float)((int)(var29 * 1000.0D)) / 1000.0F);
         double var43 = var19 + var6;
         double var45 = var21 + var8;
         var19 += var11 * var6;
         var21 += var13 * var8;
         var43 -= (1.0D - var15) * var6;
         var45 -= (1.0D - var17) * var8;
         SpriteRenderer.instance.render(var1, (float)var19 + (float)this.getXScroll().intValue(), (float)var21 + (float)this.getYScroll().intValue(), (float)(var43 - var19), (float)(var45 - var21), var10.r, var10.g, var10.b, var10.a, (float)var23, (float)var25, (float)var27, (float)var25, (float)var27, (float)var29, (float)var23, (float)var29);
      }
   }

   public Boolean getScrollChildren() {
      return this.bScrollChildren ? Boolean.TRUE : Boolean.FALSE;
   }

   public void setScrollChildren(boolean var1) {
      this.bScrollChildren = var1;
   }

   public Boolean getScrollWithParent() {
      return this.bScrollWithParent ? Boolean.TRUE : Boolean.FALSE;
   }

   public void setScrollWithParent(boolean var1) {
      this.bScrollWithParent = var1;
   }

   public void setRenderClippedChildren(boolean var1) {
      this.bRenderClippedChildren = var1;
   }

   public Double getAbsoluteX() {
      if (this.getParent() != null) {
         return this.getParent().getScrollChildren() && this.getScrollWithParent() ? BoxedStaticValues.toDouble(this.getParent().getAbsoluteX() + (double)this.getX().intValue() + (double)this.getParent().getXScroll().intValue()) : BoxedStaticValues.toDouble(this.getParent().getAbsoluteX() + (double)this.getX().intValue());
      } else {
         return BoxedStaticValues.toDouble((double)this.getX().intValue());
      }
   }

   public Double getAbsoluteY() {
      if (this.getParent() != null) {
         return this.getParent().getScrollChildren() && this.getScrollWithParent() ? BoxedStaticValues.toDouble(this.getParent().getAbsoluteY() + (double)this.getY().intValue() + (double)this.getParent().getYScroll().intValue()) : BoxedStaticValues.toDouble(this.getParent().getAbsoluteY() + (double)this.getY().intValue());
      } else {
         return BoxedStaticValues.toDouble((double)this.getY().intValue());
      }
   }

   public String getClickedValue() {
      return this.clickedValue;
   }

   public void setClickedValue(String var1) {
      this.clickedValue = var1;
   }

   public void bringToTop() {
      UIManager.pushToTop(this);
      if (this.Parent != null) {
         this.Parent.addBringToTop(this);
      }

   }

   void onRightMouseUpOutside(double var1, double var3) {
      if (this.getTable() != null && this.getTable().rawget("onRightMouseUpOutside") != null) {
         LuaManager.caller.protectedCallVoid(UIManager.getDefaultThread(), this.getTable().rawget("onRightMouseUpOutside"), this.table, BoxedStaticValues.toDouble(var1 - this.xScroll), BoxedStaticValues.toDouble(var3 - this.yScroll));
      }

      for(int var5 = this.getControls().size() - 1; var5 >= 0; --var5) {
         UIElement var6 = (UIElement)this.getControls().get(var5);
         var6.onRightMouseUpOutside(var1 - (double)var6.getXScrolled(this).intValue(), var3 - (double)var6.getYScrolled(this).intValue());
      }

   }

   void onRightMouseDownOutside(double var1, double var3) {
      if (this.getTable() != null && this.getTable().rawget("onRightMouseDownOutside") != null) {
         LuaManager.caller.protectedCallVoid(UIManager.getDefaultThread(), this.getTable().rawget("onRightMouseDownOutside"), this.table, BoxedStaticValues.toDouble(var1 - this.xScroll), BoxedStaticValues.toDouble(var3 - this.yScroll));
      }

      for(int var5 = this.getControls().size() - 1; var5 >= 0; --var5) {
         UIElement var6 = (UIElement)this.getControls().get(var5);
         var6.onRightMouseDownOutside(var1 - (double)var6.getXScrolled(this).intValue(), var3 - (double)var6.getYScrolled(this).intValue());
      }

   }

   public void onMouseUpOutside(double var1, double var3) {
      if (this.getTable() != null && this.getTable().rawget("onMouseUpOutside") != null) {
         LuaManager.caller.protectedCallVoid(UIManager.getDefaultThread(), this.getTable().rawget("onMouseUpOutside"), this.table, BoxedStaticValues.toDouble(var1 - this.xScroll), BoxedStaticValues.toDouble(var3 - this.yScroll));
      }

      for(int var5 = this.getControls().size() - 1; var5 >= 0; --var5) {
         UIElement var6 = (UIElement)this.getControls().get(var5);
         var6.onMouseUpOutside(var1 - (double)var6.getXScrolled(this).intValue(), var3 - (double)var6.getYScrolled(this).intValue());
      }

   }

   void onMouseDownOutside(double var1, double var3) {
      if (this.getTable() != null && this.getTable().rawget("onMouseDownOutside") != null) {
         LuaManager.caller.protectedCallVoid(UIManager.getDefaultThread(), this.getTable().rawget("onMouseDownOutside"), this.table, BoxedStaticValues.toDouble(var1 - this.xScroll), BoxedStaticValues.toDouble(var3 - this.yScroll));
      }

      for(int var5 = this.getControls().size() - 1; var5 >= 0; --var5) {
         UIElement var6 = (UIElement)this.getControls().get(var5);
         var6.onMouseDownOutside(var1 - (double)var6.getX().intValue(), var3 - (double)var6.getY().intValue());
      }

   }

   public Boolean onMouseDown(double var1, double var3) {
      if (this.clicked && UIManager.isDoubleClick((double)((int)this.clickX), (double)((int)this.clickY), (double)((int)var1), (double)((int)var3), (double)this.leftDownTime) && this.getTable() != null && this.getTable().rawget("onMouseDoubleClick") != null) {
         this.clicked = false;
         return this.onMouseDoubleClick(var1, var3) ? Boolean.TRUE : Boolean.FALSE;
      } else {
         this.clicked = true;
         this.clickX = var1;
         this.clickY = var3;
         this.leftDownTime = System.currentTimeMillis();
         if (this.Parent != null && this.Parent.maxDrawHeight != -1 && (double)this.Parent.maxDrawHeight <= var3) {
            return Boolean.FALSE;
         } else if (this.maxDrawHeight != -1 && (double)this.maxDrawHeight <= var3) {
            return Boolean.FALSE;
         } else if (!this.visible) {
            return Boolean.FALSE;
         } else {
            if (this.getTable() != null && this.getTable().rawget("onFocus") != null) {
               LuaManager.caller.protectedCallVoid(UIManager.getDefaultThread(), this.getTable().rawget("onFocus"), this.table, BoxedStaticValues.toDouble(var1 - this.xScroll), BoxedStaticValues.toDouble(var3 - this.yScroll));
            }

            boolean var5 = false;

            for(int var6 = this.getControls().size() - 1; var6 >= 0; --var6) {
               UIElement var7 = (UIElement)this.getControls().get(var6);
               if (!var5 && (var1 > var7.getXScrolled(this) && var3 > var7.getYScrolled(this) && var1 < var7.getXScrolled(this) + var7.getWidth() && var3 < var7.getYScrolled(this) + var7.getHeight() || var7.isCapture())) {
                  if (var7.onMouseDown(var1 - (double)var7.getXScrolled(this).intValue(), var3 - (double)var7.getYScrolled(this).intValue())) {
                     var5 = true;
                  }
               } else if (var7.getTable() != null && var7.getTable().rawget("onMouseDownOutside") != null) {
                  LuaManager.caller.protectedCallVoid(UIManager.getDefaultThread(), var7.getTable().rawget("onMouseDownOutside"), var7.getTable(), BoxedStaticValues.toDouble(var1 - this.xScroll), BoxedStaticValues.toDouble(var3 - this.yScroll));
               }
            }

            if (this.getTable() != null) {
               Boolean var8;
               if (var5) {
                  if (this.getTable().rawget("onMouseDownOutside") != null) {
                     var8 = LuaManager.caller.protectedCallBoolean(UIManager.getDefaultThread(), this.getTable().rawget("onMouseDownOutside"), this.table, BoxedStaticValues.toDouble(var1 - this.xScroll), BoxedStaticValues.toDouble(var3 - this.yScroll));
                     if (var8 == null) {
                        return Boolean.TRUE;
                     }

                     if (var8 == Boolean.TRUE) {
                        return Boolean.TRUE;
                     }
                  }
               } else if (this.getTable().rawget("onMouseDown") != null) {
                  var8 = LuaManager.caller.protectedCallBoolean(UIManager.getDefaultThread(), this.getTable().rawget("onMouseDown"), this.table, BoxedStaticValues.toDouble(var1 - this.xScroll), BoxedStaticValues.toDouble(var3 - this.yScroll));
                  if (var8 == null) {
                     return Boolean.TRUE;
                  }

                  if (var8 == Boolean.TRUE) {
                     return Boolean.TRUE;
                  }
               }
            }

            return var5;
         }
      }
   }

   private Boolean onMouseDoubleClick(double var1, double var3) {
      if (this.Parent != null && this.Parent.maxDrawHeight != -1 && (double)this.Parent.maxDrawHeight <= this.y) {
         return Boolean.FALSE;
      } else if (this.maxDrawHeight != -1 && (double)this.maxDrawHeight <= this.y) {
         return Boolean.FALSE;
      } else if (!this.visible) {
         return Boolean.FALSE;
      } else {
         if (this.getTable().rawget("onMouseDoubleClick") != null) {
            Boolean var5 = LuaManager.caller.protectedCallBoolean(UIManager.getDefaultThread(), this.getTable().rawget("onMouseDoubleClick"), this.table, BoxedStaticValues.toDouble(var1 - this.xScroll), BoxedStaticValues.toDouble(var3 - this.yScroll));
            if (var5 == null) {
               return Boolean.TRUE;
            }

            if (var5 == Boolean.TRUE) {
               return Boolean.TRUE;
            }
         }

         return Boolean.TRUE;
      }
   }

   public Boolean onMouseWheel(double var1) {
      int var3 = Mouse.getXA();
      int var4 = Mouse.getYA();
      if (this.getTable() != null && this.getTable().rawget("onMouseWheel") != null) {
         Boolean var5 = LuaManager.caller.protectedCallBoolean(UIManager.getDefaultThread(), this.getTable().rawget("onMouseWheel"), this.table, BoxedStaticValues.toDouble(var1));
         if (var5 == Boolean.TRUE) {
            return Boolean.TRUE;
         }
      }

      for(int var7 = this.getControls().size() - 1; var7 >= 0; --var7) {
         UIElement var6 = (UIElement)this.getControls().get(var7);
         if (var6.isVisible() && ((double)var3 >= var6.getAbsoluteX() && (double)var4 >= var6.getAbsoluteY() && (double)var3 < var6.getAbsoluteX() + var6.getWidth() && (double)var4 < var6.getAbsoluteY() + var6.getHeight() || var6.isCapture()) && var6.onMouseWheel(var1)) {
            return this.bConsumeMouseEvents ? Boolean.TRUE : Boolean.FALSE;
         }
      }

      return Boolean.FALSE;
   }

   public Boolean onMouseMove(double var1, double var3) {
      int var5 = Mouse.getXA();
      int var6 = Mouse.getYA();
      if (this.Parent != null && this.Parent.maxDrawHeight != -1 && (double)this.Parent.maxDrawHeight <= this.y) {
         return Boolean.FALSE;
      } else if (this.maxDrawHeight != -1 && (double)this.maxDrawHeight <= (double)var6 - this.getAbsoluteY()) {
         return Boolean.FALSE;
      } else if (!this.visible) {
         return Boolean.FALSE;
      } else {
         if (this.getTable() != null && this.getTable().rawget("onMouseMove") != null) {
            LuaManager.caller.protectedCallVoid(UIManager.getDefaultThread(), this.getTable().rawget("onMouseMove"), this.table, BoxedStaticValues.toDouble(var1), BoxedStaticValues.toDouble(var3));
         }

         boolean var7 = false;

         for(int var8 = this.getControls().size() - 1; var8 >= 0; --var8) {
            UIElement var9 = (UIElement)this.getControls().get(var8);
            if ((!((double)var5 >= var9.getAbsoluteX()) || !((double)var6 >= var9.getAbsoluteY()) || !((double)var5 < var9.getAbsoluteX() + var9.getWidth()) || !((double)var6 < var9.getAbsoluteY() + var9.getHeight())) && !var9.isCapture()) {
               var9.onMouseMoveOutside(var1, var3);
            } else if (!var7 && var9.onMouseMove(var1, var3)) {
               var7 = true;
            }
         }

         return this.bConsumeMouseEvents ? Boolean.TRUE : Boolean.FALSE;
      }
   }

   public void onMouseMoveOutside(double var1, double var3) {
      if (this.getTable() != null && this.getTable().rawget("onMouseMoveOutside") != null) {
         LuaManager.caller.protectedCallVoid(UIManager.getDefaultThread(), this.getTable().rawget("onMouseMoveOutside"), this.table, BoxedStaticValues.toDouble(var1), BoxedStaticValues.toDouble(var3));
      }

      for(int var5 = this.getControls().size() - 1; var5 >= 0; --var5) {
         UIElement var6 = (UIElement)this.getControls().get(var5);
         var6.onMouseMoveOutside(var1, var3);
      }

   }

   public Boolean onMouseUp(double var1, double var3) {
      if (this.Parent != null && this.Parent.maxDrawHeight != -1 && (double)this.Parent.maxDrawHeight <= var3) {
         return Boolean.FALSE;
      } else if (this.maxDrawHeight != -1 && (double)this.maxDrawHeight <= var3) {
         return Boolean.FALSE;
      } else if (!this.visible) {
         return Boolean.FALSE;
      } else {
         boolean var5 = false;

         for(int var6 = this.getControls().size() - 1; var6 >= 0; --var6) {
            UIElement var7 = (UIElement)this.getControls().get(var6);
            if (!var5 && (var1 >= var7.getXScrolled(this) && var3 >= var7.getYScrolled(this) && var1 < var7.getXScrolled(this) + var7.getWidth() && var3 < var7.getYScrolled(this) + var7.getHeight() || var7.isCapture())) {
               if (var7.onMouseUp(var1 - (double)var7.getXScrolled(this).intValue(), var3 - (double)var7.getYScrolled(this).intValue())) {
                  var5 = true;
               }
            } else {
               var7.onMouseUpOutside(var1 - (double)var7.getXScrolled(this).intValue(), var3 - (double)var7.getYScrolled(this).intValue());
            }

            var6 = PZMath.min(var6, this.getControls().size());
         }

         if (this.getTable() != null) {
            Boolean var8;
            if (var5) {
               if (this.getTable().rawget("onMouseUpOutside") != null) {
                  var8 = LuaManager.caller.protectedCallBoolean(UIManager.getDefaultThread(), this.getTable().rawget("onMouseUpOutside"), this.table, BoxedStaticValues.toDouble(var1 - this.xScroll), BoxedStaticValues.toDouble(var3 - this.yScroll));
                  if (var8 == null) {
                     return Boolean.TRUE;
                  }

                  if (var8 == Boolean.TRUE) {
                     return Boolean.TRUE;
                  }
               }
            } else if (this.getTable().rawget("onMouseUp") != null) {
               var8 = LuaManager.caller.protectedCallBoolean(UIManager.getDefaultThread(), this.getTable().rawget("onMouseUp"), this.table, BoxedStaticValues.toDouble(var1 - this.xScroll), BoxedStaticValues.toDouble(var3 - this.yScroll));
               if (var8 == null) {
                  return Boolean.TRUE;
               }

               if (var8 == Boolean.TRUE) {
                  return Boolean.TRUE;
               }
            }
         }

         return var5 ? Boolean.TRUE : Boolean.FALSE;
      }
   }

   public void onresize() {
   }

   public void onResize() {
      if (this.Parent != null && this.Parent.bResizeDirty) {
         double var1 = this.Parent.getWidth() - this.Parent.lastwidth;
         double var3 = this.Parent.getHeight() - this.Parent.lastheight;
         if (!this.anchorTop && this.anchorBottom) {
            this.setY(this.getY() + var3);
         }

         if (this.anchorTop && this.anchorBottom) {
            this.setHeight(this.getHeight() + var3);
         }

         if (!this.anchorLeft && this.anchorRight) {
            this.setX(this.getX() + var1);
         }

         if (this.anchorLeft && this.anchorRight) {
            this.setWidth(this.getWidth() + var1);
         }
      }

      if (this.getTable() != null && this.getTable().rawget("onResize") != null) {
         LuaManager.caller.pcallvoid(UIManager.getDefaultThread(), this.getTable().rawget("onResize"), this.table, this.getWidth(), this.getHeight());
      }

      for(int var5 = this.getControls().size() - 1; var5 >= 0; --var5) {
         UIElement var2 = (UIElement)this.getControls().get(var5);
         if (var2 == null) {
            this.getControls().remove(var5);
         } else {
            var2.onResize();
         }
      }

      this.bResizeDirty = false;
      this.lastwidth = this.getWidth();
      this.lastheight = this.getHeight();
   }

   public Boolean onRightMouseDown(double var1, double var3) {
      if (!this.isVisible()) {
         return Boolean.FALSE;
      } else if (this.Parent != null && this.Parent.maxDrawHeight != -1 && (double)this.Parent.maxDrawHeight <= var3) {
         return Boolean.FALSE;
      } else if (this.maxDrawHeight != -1 && (double)this.maxDrawHeight <= var3) {
         return Boolean.FALSE;
      } else {
         boolean var5 = false;

         for(int var6 = this.getControls().size() - 1; var6 >= 0; --var6) {
            UIElement var7 = (UIElement)this.getControls().get(var6);
            if (!var5 && (var1 >= var7.getXScrolled(this) && var3 >= var7.getYScrolled(this) && var1 < var7.getXScrolled(this) + var7.getWidth() && var3 < var7.getYScrolled(this) + var7.getHeight() || var7.isCapture())) {
               if (var7.onRightMouseDown(var1 - (double)var7.getXScrolled(this).intValue(), var3 - (double)var7.getYScrolled(this).intValue())) {
                  var5 = true;
               }
            } else if (var7.getTable() != null && var7.getTable().rawget("onRightMouseDownOutside") != null) {
               LuaManager.caller.protectedCallVoid(UIManager.getDefaultThread(), var7.getTable().rawget("onRightMouseDownOutside"), var7.getTable(), BoxedStaticValues.toDouble(var1 - this.xScroll), BoxedStaticValues.toDouble(var3 - this.yScroll));
            }
         }

         if (this.getTable() != null) {
            Boolean var8;
            if (var5) {
               if (this.getTable().rawget("onRightMouseDownOutside") != null) {
                  var8 = LuaManager.caller.protectedCallBoolean(UIManager.getDefaultThread(), this.getTable().rawget("onRightMouseDownOutside"), this.table, BoxedStaticValues.toDouble(var1 - this.xScroll), BoxedStaticValues.toDouble(var3 - this.yScroll));
                  if (var8 == null) {
                     return Boolean.TRUE;
                  }

                  if (var8 == Boolean.TRUE) {
                     return Boolean.TRUE;
                  }
               }
            } else if (this.getTable().rawget("onRightMouseDown") != null) {
               var8 = LuaManager.caller.protectedCallBoolean(UIManager.getDefaultThread(), this.getTable().rawget("onRightMouseDown"), this.table, BoxedStaticValues.toDouble(var1 - this.xScroll), BoxedStaticValues.toDouble(var3 - this.yScroll));
               if (var8 == null) {
                  return Boolean.TRUE;
               }

               if (var8 == Boolean.TRUE) {
                  return Boolean.TRUE;
               }
            }
         }

         return var5 ? Boolean.TRUE : Boolean.FALSE;
      }
   }

   public Boolean onRightMouseUp(double var1, double var3) {
      if (!this.isVisible()) {
         return Boolean.FALSE;
      } else if (this.Parent != null && this.Parent.maxDrawHeight != -1 && (double)this.Parent.maxDrawHeight <= var3) {
         return Boolean.FALSE;
      } else if (this.maxDrawHeight != -1 && (double)this.maxDrawHeight <= var3) {
         return Boolean.FALSE;
      } else {
         boolean var5 = false;

         for(int var6 = this.getControls().size() - 1; var6 >= 0; --var6) {
            UIElement var7 = (UIElement)this.getControls().get(var6);
            if (!var5 && (var1 >= var7.getXScrolled(this) && var3 >= var7.getYScrolled(this) && var1 < var7.getXScrolled(this) + var7.getWidth() && var3 < var7.getYScrolled(this) + var7.getHeight() || var7.isCapture())) {
               if (var7.onRightMouseUp(var1 - (double)var7.getXScrolled(this).intValue(), var3 - (double)var7.getYScrolled(this).intValue())) {
                  var5 = true;
               }
            } else {
               var7.onRightMouseUpOutside(var1 - (double)var7.getXScrolled(this).intValue(), var3 - (double)var7.getYScrolled(this).intValue());
            }
         }

         if (this.getTable() != null) {
            Boolean var8;
            if (var5) {
               if (this.getTable().rawget("onRightMouseUpOutside") != null) {
                  var8 = LuaManager.caller.protectedCallBoolean(UIManager.getDefaultThread(), this.getTable().rawget("onRightMouseUpOutside"), this.table, BoxedStaticValues.toDouble(var1 - this.xScroll), BoxedStaticValues.toDouble(var3 - this.yScroll));
                  if (var8 == null) {
                     return Boolean.TRUE;
                  }

                  if (var8 == Boolean.TRUE) {
                     return Boolean.TRUE;
                  }
               }
            } else if (this.getTable().rawget("onRightMouseUp") != null) {
               var8 = LuaManager.caller.protectedCallBoolean(UIManager.getDefaultThread(), this.getTable().rawget("onRightMouseUp"), this.table, BoxedStaticValues.toDouble(var1 - this.xScroll), BoxedStaticValues.toDouble(var3 - this.yScroll));
               if (var8 == null) {
                  return Boolean.TRUE;
               }

               if (var8 == Boolean.TRUE) {
                  return Boolean.TRUE;
               }
            }
         }

         return var5 ? Boolean.TRUE : Boolean.FALSE;
      }
   }

   public void RemoveControl(UIElement var1) {
      this.getControls().remove(var1);
      var1.setParent((UIElement)null);
   }

   public void render() {
      if (this.enabled) {
         if (this.isVisible()) {
            if (this.Parent == null || this.Parent.maxDrawHeight == -1 || !((double)this.Parent.maxDrawHeight <= this.y)) {
               Double var1;
               if (this.Parent != null && !this.Parent.bRenderClippedChildren) {
                  var1 = this.Parent.getAbsoluteY();
                  double var2 = this.getAbsoluteY();
                  if (var2 + this.getHeight() <= var1 || var2 >= var1 + this.getParent().getHeight()) {
                     return;
                  }
               }

               if (this.getTable() != null && this.getTable().rawget("prerender") != null) {
                  try {
                     LuaManager.caller.pcallvoid(UIManager.getDefaultThread(), this.getTable().rawget("prerender"), (Object)this.table);
                  } catch (Exception var7) {
                     boolean var9 = false;
                  }
               }

               for(int var8 = 0; var8 < this.getControls().size(); ++var8) {
                  ((UIElement)this.getControls().get(var8)).render();
               }

               if (this.getTable() != null && this.getTable().rawget("render") != null) {
                  LuaManager.caller.pcallvoid(UIManager.getDefaultThread(), this.getTable().rawget("render"), (Object)this.table);
               }

               if (Core.bDebug && DebugOptions.instance.UIRenderOutline.getValue()) {
                  if (this.table != null && "ISScrollingListBox".equals(this.table.rawget("Type"))) {
                     this.repaintStencilRect(0.0D, 0.0D, (double)((int)this.width), (double)((int)this.height));
                  }

                  var1 = -this.getXScroll();
                  Double var10 = -this.getYScroll();
                  double var3 = 1.0D;
                  if (this.isMouseOver()) {
                     var3 = 0.0D;
                  }

                  double var5 = this.maxDrawHeight == -1 ? (double)this.height : (double)this.maxDrawHeight;
                  this.DrawTextureScaledColor((Texture)null, var1, var10, 1.0D, var5, var3, 1.0D, 1.0D, 0.5D);
                  this.DrawTextureScaledColor((Texture)null, var1 + 1.0D, var10, (double)this.width - 2.0D, 1.0D, var3, 1.0D, 1.0D, 0.5D);
                  this.DrawTextureScaledColor((Texture)null, var1 + (double)this.width - 1.0D, var10, 1.0D, var5, var3, 1.0D, 1.0D, 0.5D);
                  this.DrawTextureScaledColor((Texture)null, var1 + 1.0D, var10 + var5 - 1.0D, (double)this.width - 2.0D, 1.0D, var3, 1.0D, 1.0D, 0.5D);
               }

            }
         }
      }
   }

   public void update() {
      if (this.enabled) {
         int var1;
         for(var1 = 0; var1 < this.Controls.size(); ++var1) {
            if (this.toTop.contains(this.Controls.get(var1))) {
               UIElement var2 = (UIElement)this.Controls.remove(var1);
               --var1;
               toAdd.add(var2);
            }
         }

         this.Controls.addAll(toAdd);
         toAdd.clear();
         this.toTop.clear();
         if (UIManager.doTick && this.getTable() != null && this.getTable().rawget("update") != null) {
            LuaManager.caller.pcallvoid(UIManager.getDefaultThread(), this.getTable().rawget("update"), (Object)this.table);
         }

         if (this.bResizeDirty) {
            this.onResize();
            this.lastwidth = (double)this.width;
            this.lastheight = (double)this.height;
            this.bResizeDirty = false;
         }

         for(var1 = 0; var1 < this.getControls().size(); ++var1) {
            ((UIElement)this.getControls().get(var1)).update();
         }

      }
   }

   public void BringToTop(UIElement var1) {
      this.getControls().remove(var1);
      this.getControls().add(var1);
   }

   public Boolean isCapture() {
      return this.capture ? Boolean.TRUE : Boolean.FALSE;
   }

   public void setCapture(boolean var1) {
      this.capture = var1;
   }

   public Boolean isIgnoreLossControl() {
      return this.IgnoreLossControl ? Boolean.TRUE : Boolean.FALSE;
   }

   public void setIgnoreLossControl(boolean var1) {
      this.IgnoreLossControl = var1;
   }

   public ArrayList getControls() {
      return this.Controls;
   }

   public void setControls(Vector var1) {
      this.setControls(var1);
   }

   public Boolean isDefaultDraw() {
      return this.defaultDraw ? Boolean.TRUE : Boolean.FALSE;
   }

   public void setDefaultDraw(boolean var1) {
      this.defaultDraw = var1;
   }

   public Boolean isFollowGameWorld() {
      return this.followGameWorld ? Boolean.TRUE : Boolean.FALSE;
   }

   public void setFollowGameWorld(boolean var1) {
      this.followGameWorld = var1;
   }

   public int getRenderThisPlayerOnly() {
      return this.renderThisPlayerOnly;
   }

   public void setRenderThisPlayerOnly(int var1) {
      this.renderThisPlayerOnly = var1;
   }

   public Double getHeight() {
      return BoxedStaticValues.toDouble((double)this.height);
   }

   public void setHeight(double var1) {
      if ((double)this.height != var1) {
         this.bResizeDirty = true;
      }

      this.lastheight = (double)this.height;
      this.height = (float)var1;
   }

   public UIElement getParent() {
      return this.Parent;
   }

   public void setParent(UIElement var1) {
      this.Parent = var1;
   }

   public Boolean isVisible() {
      return this.visible ? Boolean.TRUE : Boolean.FALSE;
   }

   public void setVisible(boolean var1) {
      this.visible = var1;
   }

   public Double getWidth() {
      return BoxedStaticValues.toDouble((double)this.width);
   }

   public void setWidth(double var1) {
      if ((double)this.width != var1) {
         this.bResizeDirty = true;
      }

      this.lastwidth = (double)this.width;
      this.width = (float)var1;
   }

   public Double getX() {
      return BoxedStaticValues.toDouble(this.x);
   }

   public void setX(double var1) {
      this.x = (double)((float)var1);
   }

   public Double getXScrolled(UIElement var1) {
      return var1 != null && var1.bScrollChildren && this.bScrollWithParent ? BoxedStaticValues.toDouble(this.x + var1.getXScroll()) : BoxedStaticValues.toDouble(this.x);
   }

   public Double getYScrolled(UIElement var1) {
      return var1 != null && var1.bScrollChildren && this.bScrollWithParent ? BoxedStaticValues.toDouble(this.y + var1.getYScroll()) : BoxedStaticValues.toDouble(this.y);
   }

   public boolean isEnabled() {
      return this.enabled;
   }

   public void setEnabled(boolean var1) {
      this.enabled = var1;
   }

   public Double getY() {
      return BoxedStaticValues.toDouble(this.y);
   }

   public void setY(double var1) {
      this.y = (double)((float)var1);
   }

   public void suspendStencil() {
      IndieGL.disableStencilTest();
      IndieGL.disableAlphaTest();
   }

   public void resumeStencil() {
      IndieGL.enableStencilTest();
      IndieGL.enableAlphaTest();
   }

   public void setStencilRect(double var1, double var3, double var5, double var7) {
      var1 += this.getAbsoluteX();
      var3 += this.getAbsoluteY();
      IndieGL.glStencilMask(255);
      IndieGL.enableStencilTest();
      IndieGL.enableAlphaTest();
      ++StencilLevel;
      IndieGL.glStencilFunc(519, StencilLevel, 255);
      IndieGL.glStencilOp(7680, 7680, 7681);
      IndieGL.glColorMask(false, false, false, false);
      SpriteRenderer.instance.renderi((Texture)null, (int)var1, (int)var3, (int)var5, (int)var7, 1.0F, 0.0F, 0.0F, 1.0F, (Consumer)null);
      IndieGL.glColorMask(true, true, true, true);
      IndieGL.glStencilOp(7680, 7680, 7680);
      IndieGL.glStencilFunc(514, StencilLevel, 255);
   }

   public void clearStencilRect() {
      if (StencilLevel > 0) {
         --StencilLevel;
      }

      if (StencilLevel > 0) {
         IndieGL.glStencilFunc(514, StencilLevel, 255);
      } else {
         IndieGL.glAlphaFunc(519, 0.0F);
         IndieGL.disableStencilTest();
         IndieGL.disableAlphaTest();
         IndieGL.glStencilFunc(519, 255, 255);
         IndieGL.glStencilOp(7680, 7680, 7680);
         IndieGL.glClear(1280);
      }

   }

   public void repaintStencilRect(double var1, double var3, double var5, double var7) {
      if (StencilLevel > 0) {
         var1 += this.getAbsoluteX();
         var3 += this.getAbsoluteY();
         IndieGL.glStencilFunc(519, StencilLevel, 255);
         IndieGL.glStencilOp(7680, 7680, 7681);
         IndieGL.glColorMask(false, false, false, false);
         SpriteRenderer.instance.renderi((Texture)null, (int)var1, (int)var3, (int)var5, (int)var7, 1.0F, 0.0F, 0.0F, 1.0F, (Consumer)null);
         IndieGL.glColorMask(true, true, true, true);
         IndieGL.glStencilOp(7680, 7680, 7680);
         IndieGL.glStencilFunc(514, StencilLevel, 255);
      }
   }

   public KahluaTable getTable() {
      return this.table;
   }

   public void setTable(KahluaTable var1) {
      this.table = var1;
   }

   public void setHeightSilent(double var1) {
      this.lastheight = (double)this.height;
      this.height = (float)var1;
   }

   public void setWidthSilent(double var1) {
      this.lastwidth = (double)this.width;
      this.width = (float)var1;
   }

   public void setHeightOnly(double var1) {
      this.height = (float)var1;
   }

   public void setWidthOnly(double var1) {
      this.width = (float)var1;
   }

   public boolean isAnchorTop() {
      return this.anchorTop;
   }

   public void setAnchorTop(boolean var1) {
      this.anchorTop = var1;
      this.lastwidth = (double)this.width;
      this.lastheight = (double)this.height;
   }

   public void ignoreWidthChange() {
      this.lastwidth = (double)this.width;
   }

   public void ignoreHeightChange() {
      this.lastheight = (double)this.height;
   }

   public Boolean isAnchorLeft() {
      return this.anchorLeft ? Boolean.TRUE : Boolean.FALSE;
   }

   public void setAnchorLeft(boolean var1) {
      this.anchorLeft = var1;
      this.lastwidth = (double)this.width;
      this.lastheight = (double)this.height;
   }

   public Boolean isAnchorRight() {
      return this.anchorRight ? Boolean.TRUE : Boolean.FALSE;
   }

   public void setAnchorRight(boolean var1) {
      this.anchorRight = var1;
      this.lastwidth = (double)this.width;
      this.lastheight = (double)this.height;
   }

   public Boolean isAnchorBottom() {
      return this.anchorBottom ? Boolean.TRUE : Boolean.FALSE;
   }

   public void setAnchorBottom(boolean var1) {
      this.anchorBottom = var1;
      this.lastwidth = (double)this.width;
      this.lastheight = (double)this.height;
   }

   private void addBringToTop(UIElement var1) {
      this.toTop.add(var1);
   }

   public int getPlayerContext() {
      return this.playerContext;
   }

   public void setPlayerContext(int var1) {
      this.playerContext = var1;
   }

   public String getUIName() {
      return this.uiname;
   }

   public void setUIName(String var1) {
      this.uiname = var1 != null ? var1 : "";
   }

   public Double clampToParentX(double var1) {
      if (this.getParent() == null) {
         return BoxedStaticValues.toDouble(var1);
      } else {
         double var3 = this.getParent().clampToParentX(this.getParent().getAbsoluteX());
         double var5 = this.getParent().clampToParentX(var3 + (double)this.getParent().getWidth().intValue());
         if (var1 < var3) {
            var1 = var3;
         }

         if (var1 > var5) {
            var1 = var5;
         }

         return BoxedStaticValues.toDouble(var1);
      }
   }

   public Double clampToParentY(double var1) {
      if (this.getParent() == null) {
         return var1;
      } else {
         double var3 = this.getParent().clampToParentY(this.getParent().getAbsoluteY());
         double var5 = this.getParent().clampToParentY(var3 + (double)this.getParent().getHeight().intValue());
         if (var1 < var3) {
            var1 = var3;
         }

         if (var1 > var5) {
            var1 = var5;
         }

         return var1;
      }
   }

   public Boolean isPointOver(double var1, double var3) {
      if (!this.isVisible()) {
         return Boolean.FALSE;
      } else {
         int var5 = this.getHeight().intValue();
         if (this.maxDrawHeight != -1) {
            var5 = Math.min(var5, this.maxDrawHeight);
         }

         double var6 = var1 - this.getAbsoluteX();
         double var8 = var3 - this.getAbsoluteY();
         if (!(var6 < 0.0D) && !(var6 >= this.getWidth()) && !(var8 < 0.0D) && !(var8 >= (double)var5)) {
            if (this.Parent == null) {
               ArrayList var13 = UIManager.getUI();

               for(int var14 = var13.size() - 1; var14 >= 0; --var14) {
                  UIElement var12 = (UIElement)var13.get(var14);
                  if (var12 == this) {
                     break;
                  }

                  if (var12.isPointOver(var1, var3)) {
                     return Boolean.FALSE;
                  }
               }

               return Boolean.TRUE;
            } else {
               for(int var10 = this.Parent.Controls.size() - 1; var10 >= 0; --var10) {
                  UIElement var11 = (UIElement)this.Parent.Controls.get(var10);
                  if (var11 == this) {
                     break;
                  }

                  if (var11.isVisible()) {
                     var5 = var11.getHeight().intValue();
                     if (var11.maxDrawHeight != -1) {
                        var5 = Math.min(var5, var11.maxDrawHeight);
                     }

                     var6 = var1 - var11.getAbsoluteX();
                     var8 = var3 - var11.getAbsoluteY();
                     if (var6 >= 0.0D && var6 < var11.getWidth() && var8 >= 0.0D && var8 < (double)var5) {
                        return Boolean.FALSE;
                     }
                  }
               }

               return this.Parent.isPointOver(var1, var3) ? Boolean.TRUE : Boolean.FALSE;
            }
         } else {
            return Boolean.FALSE;
         }
      }
   }

   public Boolean isMouseOver() {
      return this.isPointOver((double)Mouse.getXA(), (double)Mouse.getYA()) ? Boolean.TRUE : Boolean.FALSE;
   }

   protected Object tryGetTableValue(String var1) {
      return this.getTable() == null ? null : this.getTable().rawget(var1);
   }

   public void setWantKeyEvents(boolean var1) {
      this.bWantKeyEvents = var1;
   }

   public boolean isWantKeyEvents() {
      return this.bWantKeyEvents;
   }

   public boolean isKeyConsumed(int var1) {
      Object var2 = this.tryGetTableValue("isKeyConsumed");
      return var2 == null ? false : LuaManager.caller.pcallBoolean(UIManager.getDefaultThread(), var2, this.getTable(), BoxedStaticValues.toDouble((double)var1));
   }

   public void onKeyPress(int var1) {
      Object var2 = this.tryGetTableValue("onKeyPress");
      if (var2 != null) {
         LuaManager.caller.pcallvoid(UIManager.getDefaultThread(), var2, this.getTable(), BoxedStaticValues.toDouble((double)var1));
      }
   }

   public void onKeyRepeat(int var1) {
      Object var2 = this.tryGetTableValue("onKeyRepeat");
      if (var2 != null) {
         LuaManager.caller.pcallvoid(UIManager.getDefaultThread(), var2, this.getTable(), BoxedStaticValues.toDouble((double)var1));
      }
   }

   public void onKeyRelease(int var1) {
      Object var2 = this.tryGetTableValue("onKeyRelease");
      if (var2 != null) {
         LuaManager.caller.pcallvoid(UIManager.getDefaultThread(), var2, this.getTable(), BoxedStaticValues.toDouble((double)var1));
      }
   }

   public boolean isForceCursorVisible() {
      return this.bForceCursorVisible;
   }

   public void setForceCursorVisible(boolean var1) {
      this.bForceCursorVisible = var1;
   }
}

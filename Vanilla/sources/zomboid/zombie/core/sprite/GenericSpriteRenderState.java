package zombie.core.sprite;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import zombie.core.Color;
import zombie.core.SpriteRenderer;
import zombie.core.Styles.AbstractStyle;
import zombie.core.Styles.Style;
import zombie.core.Styles.TransparentStyle;
import zombie.core.opengl.Shader;
import zombie.core.skinnedmodel.ModelManager;
import zombie.core.textures.Texture;
import zombie.core.textures.TextureDraw;
import zombie.core.textures.TextureFBO;

public abstract class GenericSpriteRenderState {
   public final int index;
   public TextureDraw[] sprite = new TextureDraw[2048];
   public Style[] style = new Style[2048];
   public int numSprites;
   public TextureFBO fbo;
   public boolean bRendered;
   private boolean m_isRendering;
   public final ArrayList postRender = new ArrayList();
   public AbstractStyle defaultStyle;
   public boolean bCursorVisible;
   public static final byte UVCA_NONE = -1;
   public static final byte UVCA_CIRCLE = 1;
   public static final byte UVCA_NOCIRCLE = 2;
   private byte useVertColorsArray;
   private int texture2_color0;
   private int texture2_color1;
   private int texture2_color2;
   private int texture2_color3;
   private SpriteRenderer.WallShaderTexRender wallShaderTexRender;
   private Texture texture1_cutaway;
   private int texture1_cutaway_x;
   private int texture1_cutaway_y;
   private int texture1_cutaway_w;
   private int texture1_cutaway_h;

   protected GenericSpriteRenderState(int var1) {
      this.defaultStyle = TransparentStyle.instance;
      this.bCursorVisible = true;
      this.useVertColorsArray = -1;
      this.index = var1;

      for(int var2 = 0; var2 < this.sprite.length; ++var2) {
         this.sprite[var2] = new TextureDraw();
      }

   }

   public void onRendered() {
      this.m_isRendering = false;
      this.bRendered = true;
   }

   public void onRenderAcquired() {
      this.m_isRendering = true;
   }

   public boolean isRendering() {
      return this.m_isRendering;
   }

   public void onReady() {
      this.bRendered = false;
   }

   public boolean isReady() {
      return !this.bRendered;
   }

   public boolean isRendered() {
      return this.bRendered;
   }

   public void CheckSpriteSlots() {
      if (this.numSprites == this.sprite.length) {
         TextureDraw[] var1 = this.sprite;
         this.sprite = new TextureDraw[this.numSprites * 3 / 2 + 1];

         for(int var2 = this.numSprites; var2 < this.sprite.length; ++var2) {
            this.sprite[var2] = new TextureDraw();
         }

         System.arraycopy(var1, 0, this.sprite, 0, this.numSprites);
         Style[] var3 = this.style;
         this.style = new Style[this.numSprites * 3 / 2 + 1];
         System.arraycopy(var3, 0, this.style, 0, this.numSprites);
      }
   }

   public static void clearSprites(List var0) {
      for(int var1 = 0; var1 < var0.size(); ++var1) {
         ((TextureDraw)var0.get(var1)).postRender();
      }

      var0.clear();
   }

   public void clear() {
      clearSprites(this.postRender);
      this.numSprites = 0;
   }

   public void glDepthMask(boolean var1) {
      this.CheckSpriteSlots();
      TextureDraw.glDepthMask(this.sprite[this.numSprites], var1);
      this.style[this.numSprites] = this.defaultStyle;
      ++this.numSprites;
   }

   public void renderflipped(Texture var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8, float var9, Consumer var10) {
      this.render(var1, var2, var3, var4, var5, var6, var7, var8, var9, var10);
      this.sprite[this.numSprites - 1].flipped = true;
   }

   public void drawSkyBox(Shader var1, int var2, int var3, int var4) {
      if (this.numSprites == this.sprite.length) {
         this.CheckSpriteSlots();
      }

      TextureDraw.drawSkyBox(this.sprite[this.numSprites], var1, var2, var3, var4);
      this.style[this.numSprites] = this.defaultStyle;
      ++this.numSprites;
   }

   public void drawWater(Shader var1, int var2, int var3, boolean var4) {
      if (this.numSprites == this.sprite.length) {
         this.CheckSpriteSlots();
      }

      var1.startMainThread(this.sprite[this.numSprites], var2);
      TextureDraw.drawWater(this.sprite[this.numSprites], var1, var2, var3, var4);
      this.style[this.numSprites] = this.defaultStyle;
      ++this.numSprites;
   }

   public void drawPuddles(Shader var1, int var2, int var3, int var4) {
      if (this.numSprites == this.sprite.length) {
         this.CheckSpriteSlots();
      }

      TextureDraw.drawPuddles(this.sprite[this.numSprites], var1, var2, var3, var4);
      this.style[this.numSprites] = this.defaultStyle;
      ++this.numSprites;
   }

   public void drawParticles(int var1, int var2, int var3) {
      if (this.numSprites == this.sprite.length) {
         this.CheckSpriteSlots();
      }

      TextureDraw.drawParticles(this.sprite[this.numSprites], var1, var2, var3);
      this.style[this.numSprites] = this.defaultStyle;
      ++this.numSprites;
   }

   public void glDisable(int var1) {
      if (this.numSprites == this.sprite.length) {
         this.CheckSpriteSlots();
      }

      TextureDraw.glDisable(this.sprite[this.numSprites], var1);
      this.style[this.numSprites] = this.defaultStyle;
      ++this.numSprites;
   }

   public void glEnable(int var1) {
      if (this.numSprites == this.sprite.length) {
         this.CheckSpriteSlots();
      }

      TextureDraw.glEnable(this.sprite[this.numSprites], var1);
      this.style[this.numSprites] = TransparentStyle.instance;
      ++this.numSprites;
   }

   public void glStencilMask(int var1) {
      if (this.numSprites == this.sprite.length) {
         this.CheckSpriteSlots();
      }

      TextureDraw.glStencilMask(this.sprite[this.numSprites], var1);
      this.style[this.numSprites] = TransparentStyle.instance;
      ++this.numSprites;
   }

   public void glClear(int var1) {
      if (this.numSprites == this.sprite.length) {
         this.CheckSpriteSlots();
      }

      TextureDraw.glClear(this.sprite[this.numSprites], var1);
      this.style[this.numSprites] = TransparentStyle.instance;
      ++this.numSprites;
   }

   public void glClearColor(int var1, int var2, int var3, int var4) {
      if (this.numSprites == this.sprite.length) {
         this.CheckSpriteSlots();
      }

      TextureDraw.glClearColor(this.sprite[this.numSprites], var1, var2, var3, var4);
      this.style[this.numSprites] = TransparentStyle.instance;
      ++this.numSprites;
   }

   public void glStencilFunc(int var1, int var2, int var3) {
      if (this.numSprites == this.sprite.length) {
         this.CheckSpriteSlots();
      }

      TextureDraw.glStencilFunc(this.sprite[this.numSprites], var1, var2, var3);
      this.style[this.numSprites] = TransparentStyle.instance;
      ++this.numSprites;
   }

   public void glStencilOp(int var1, int var2, int var3) {
      if (this.numSprites == this.sprite.length) {
         this.CheckSpriteSlots();
      }

      TextureDraw.glStencilOp(this.sprite[this.numSprites], var1, var2, var3);
      this.style[this.numSprites] = TransparentStyle.instance;
      ++this.numSprites;
   }

   public void glColorMask(int var1, int var2, int var3, int var4) {
      if (this.numSprites == this.sprite.length) {
         this.CheckSpriteSlots();
      }

      TextureDraw.glColorMask(this.sprite[this.numSprites], var1, var2, var3, var4);
      this.style[this.numSprites] = TransparentStyle.instance;
      ++this.numSprites;
   }

   public void glAlphaFunc(int var1, float var2) {
      if (SpriteRenderer.GL_BLENDFUNC_ENABLED) {
         if (this.numSprites == this.sprite.length) {
            this.CheckSpriteSlots();
         }

         TextureDraw.glAlphaFunc(this.sprite[this.numSprites], var1, var2);
         this.style[this.numSprites] = TransparentStyle.instance;
         ++this.numSprites;
      }
   }

   public void glBlendFunc(int var1, int var2) {
      if (SpriteRenderer.GL_BLENDFUNC_ENABLED) {
         if (this.numSprites == this.sprite.length) {
            this.CheckSpriteSlots();
         }

         TextureDraw.glBlendFunc(this.sprite[this.numSprites], var1, var2);
         this.style[this.numSprites] = TransparentStyle.instance;
         ++this.numSprites;
      }
   }

   public void glBlendFuncSeparate(int var1, int var2, int var3, int var4) {
      if (SpriteRenderer.GL_BLENDFUNC_ENABLED) {
         if (this.numSprites == this.sprite.length) {
            this.CheckSpriteSlots();
         }

         TextureDraw.glBlendFuncSeparate(this.sprite[this.numSprites], var1, var2, var3, var4);
         this.style[this.numSprites] = TransparentStyle.instance;
         ++this.numSprites;
      }
   }

   public void glBlendEquation(int var1) {
      if (this.numSprites == this.sprite.length) {
         this.CheckSpriteSlots();
      }

      TextureDraw.glBlendEquation(this.sprite[this.numSprites], var1);
      this.style[this.numSprites] = TransparentStyle.instance;
      ++this.numSprites;
   }

   public void render(Texture var1, double var2, double var4, double var6, double var8, double var10, double var12, double var14, double var16, float var18, float var19, float var20, float var21, Consumer var22) {
      this.render(var1, var2, var4, var6, var8, var10, var12, var14, var16, var18, var19, var20, var21, var18, var19, var20, var21, var18, var19, var20, var21, var18, var19, var20, var21, var22);
   }

   public void render(Texture var1, double var2, double var4, double var6, double var8, double var10, double var12, double var14, double var16, float var18, float var19, float var20, float var21, float var22, float var23, float var24, float var25, float var26, float var27, float var28, float var29, float var30, float var31, float var32, float var33, Consumer var34) {
      if (this.numSprites == this.sprite.length) {
         this.CheckSpriteSlots();
      }

      this.sprite[this.numSprites].reset();
      TextureDraw.Create(this.sprite[this.numSprites], var1, (float)var2, (float)var4, (float)var6, (float)var8, (float)var10, (float)var12, (float)var14, (float)var16, var18, var19, var20, var21, var22, var23, var24, var25, var26, var27, var28, var29, var30, var31, var32, var33, var34);
      if (this.useVertColorsArray != -1) {
         TextureDraw var35 = this.sprite[this.numSprites];
         var35.useAttribArray = this.useVertColorsArray;
         var35.tex1_col0 = this.texture2_color0;
         var35.tex1_col1 = this.texture2_color1;
         var35.tex1_col2 = this.texture2_color2;
         var35.tex1_col3 = this.texture2_color3;
      }

      this.style[this.numSprites] = this.defaultStyle;
      ++this.numSprites;
   }

   public void setUseVertColorsArray(byte var1, int var2, int var3, int var4, int var5) {
      this.useVertColorsArray = var1;
      this.texture2_color0 = var2;
      this.texture2_color1 = var3;
      this.texture2_color2 = var4;
      this.texture2_color3 = var5;
   }

   public void clearUseVertColorsArray() {
      this.useVertColorsArray = -1;
   }

   public void renderdebug(Texture var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8, float var9, float var10, float var11, float var12, float var13, float var14, float var15, float var16, float var17, float var18, float var19, float var20, float var21, float var22, float var23, float var24, float var25, Consumer var26) {
      if (this.numSprites == this.sprite.length) {
         this.CheckSpriteSlots();
      }

      this.sprite[this.numSprites].reset();
      TextureDraw.Create(this.sprite[this.numSprites], var1, var2, var3, var4, var5, var6, var7, var8, var9, var10, var11, var12, var13, var14, var15, var16, var17, var18, var19, var20, var21, var22, var23, var24, var25, var26);
      this.style[this.numSprites] = this.defaultStyle;
      ++this.numSprites;
   }

   public void renderline(Texture var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8, float var9, int var10) {
      if (this.numSprites == this.sprite.length) {
         this.CheckSpriteSlots();
      }

      this.sprite[this.numSprites].reset();
      if (var2 <= var4 && var3 <= var5) {
         TextureDraw.Create(this.sprite[this.numSprites], var1, var2 + (float)var10, var3 - (float)var10, var4 + (float)var10, var5 - (float)var10, var4 - (float)var10, var5 + (float)var10, var2 - (float)var10, var3 + (float)var10, var6, var7, var8, var9);
      } else if (var2 >= var4 && var3 >= var5) {
         TextureDraw.Create(this.sprite[this.numSprites], var1, var2 + (float)var10, var3 - (float)var10, var2 - (float)var10, var3 + (float)var10, var4 - (float)var10, var5 + (float)var10, var4 + (float)var10, var5 - (float)var10, var6, var7, var8, var9);
      } else if (var2 >= var4 && var3 <= var5) {
         TextureDraw.Create(this.sprite[this.numSprites], var1, var4 - (float)var10, var5 - (float)var10, var2 - (float)var10, var3 - (float)var10, var2 + (float)var10, var3 + (float)var10, var4 + (float)var10, var5 + (float)var10, var6, var7, var8, var9);
      } else if (var2 <= var4 && var3 >= var5) {
         TextureDraw.Create(this.sprite[this.numSprites], var1, var2 - (float)var10, var3 - (float)var10, var2 + (float)var10, var3 + (float)var10, var4 + (float)var10, var5 + (float)var10, var4 - (float)var10, var5 - (float)var10, var6, var7, var8, var9);
      }

      this.style[this.numSprites] = this.defaultStyle;
      ++this.numSprites;
   }

   public void renderline(Texture var1, int var2, int var3, int var4, int var5, float var6, float var7, float var8, float var9) {
      this.renderline(var1, (float)var2, (float)var3, (float)var4, (float)var5, var6, var7, var8, var9, 1);
   }

   public void render(Texture var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8, float var9, int var10, int var11, int var12, int var13) {
      if (this.numSprites == this.sprite.length) {
         this.CheckSpriteSlots();
      }

      this.sprite[this.numSprites].reset();
      TextureDraw.Create(this.sprite[this.numSprites], var1, var2, var3, var4, var5, var6, var7, var8, var9, var10, var11, var12, var13);
      this.style[this.numSprites] = this.defaultStyle;
      ++this.numSprites;
   }

   public void render(Texture var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8, float var9, Consumer var10) {
      if (var1 == null || var1.isReady()) {
         if (var9 != 0.0F) {
            if (this.numSprites == this.sprite.length) {
               this.CheckSpriteSlots();
            }

            this.sprite[this.numSprites].reset();
            int var12 = Color.colorToABGR(var6, var7, var8, var9);
            float var15 = var2 + var4;
            float var16 = var3 + var5;
            TextureDraw var11;
            if (this.wallShaderTexRender == null) {
               var11 = TextureDraw.Create(this.sprite[this.numSprites], var1, var2, var3, var15, var3, var15, var16, var2, var16, var12, var12, var12, var12, var10);
            } else {
               var11 = TextureDraw.Create(this.sprite[this.numSprites], var1, this.wallShaderTexRender, var2, var3, var15 - var2, var16 - var3, var6, var7, var8, var9, var10);
            }

            if (this.useVertColorsArray != -1) {
               var11.useAttribArray = this.useVertColorsArray;
               var11.tex1_col0 = this.texture2_color0;
               var11.tex1_col1 = this.texture2_color1;
               var11.tex1_col2 = this.texture2_color2;
               var11.tex1_col3 = this.texture2_color3;
            }

            if (this.texture1_cutaway != null) {
               var11.tex1 = this.texture1_cutaway;
               float var17 = this.texture1_cutaway.xEnd - this.texture1_cutaway.xStart;
               float var18 = this.texture1_cutaway.yEnd - this.texture1_cutaway.yStart;
               float var19 = (float)this.texture1_cutaway_x / (float)this.texture1_cutaway.getWidth();
               float var20 = (float)(this.texture1_cutaway_x + this.texture1_cutaway_w) / (float)this.texture1_cutaway.getWidth();
               float var21 = (float)this.texture1_cutaway_y / (float)this.texture1_cutaway.getHeight();
               float var22 = (float)(this.texture1_cutaway_y + this.texture1_cutaway_h) / (float)this.texture1_cutaway.getHeight();
               var11.tex1_u0 = var11.tex1_u3 = this.texture1_cutaway.xStart + var19 * var17;
               var11.tex1_v0 = var11.tex1_v1 = this.texture1_cutaway.yStart + var21 * var18;
               var11.tex1_u1 = var11.tex1_u2 = this.texture1_cutaway.xStart + var20 * var17;
               var11.tex1_v2 = var11.tex1_v3 = this.texture1_cutaway.yStart + var22 * var18;
            }

            this.style[this.numSprites] = this.defaultStyle;
            ++this.numSprites;
         }
      }
   }

   public void renderRect(int var1, int var2, int var3, int var4, float var5, float var6, float var7, float var8) {
      if (var8 != 0.0F) {
         if (this.numSprites == this.sprite.length) {
            this.CheckSpriteSlots();
         }

         this.sprite[this.numSprites].reset();
         TextureDraw.Create(this.sprite[this.numSprites], (Texture)null, (float)var1, (float)var2, (float)var3, (float)var4, var5, var6, var7, var8, (Consumer)null);
         this.style[this.numSprites] = this.defaultStyle;
         ++this.numSprites;
      }
   }

   public void renderPoly(float var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8, float var9, float var10, float var11, float var12) {
      if (this.numSprites == this.sprite.length) {
         this.CheckSpriteSlots();
      }

      this.sprite[this.numSprites].reset();
      TextureDraw.Create(this.sprite[this.numSprites], (Texture)null, var1, var2, var3, var4, var5, var6, var7, var8, var9, var10, var11, var12);
      this.style[this.numSprites] = this.defaultStyle;
      ++this.numSprites;
   }

   public void renderPoly(Texture var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8, float var9, float var10, float var11, float var12, float var13) {
      if (var1 == null || var1.isReady()) {
         if (this.numSprites == this.sprite.length) {
            this.CheckSpriteSlots();
         }

         this.sprite[this.numSprites].reset();
         TextureDraw.Create(this.sprite[this.numSprites], var1, var2, var3, var4, var5, var6, var7, var8, var9, var10, var11, var12, var13);
         if (var1 != null) {
            float var14 = var1.getXEnd();
            float var15 = var1.getXStart();
            float var16 = var1.getYEnd();
            float var17 = var1.getYStart();
            TextureDraw var18 = this.sprite[this.numSprites];
            var18.u0 = var15;
            var18.u1 = var14;
            var18.u2 = var14;
            var18.u3 = var15;
            var18.v0 = var17;
            var18.v1 = var17;
            var18.v2 = var16;
            var18.v3 = var16;
         }

         this.style[this.numSprites] = this.defaultStyle;
         ++this.numSprites;
      }
   }

   public void renderPoly(Texture var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8, float var9, float var10, float var11, float var12, float var13, float var14, float var15, float var16, float var17, float var18, float var19, float var20, float var21) {
      if (var1 == null || var1.isReady()) {
         if (this.numSprites == this.sprite.length) {
            this.CheckSpriteSlots();
         }

         this.sprite[this.numSprites].reset();
         TextureDraw.Create(this.sprite[this.numSprites], var1, var2, var3, var4, var5, var6, var7, var8, var9, var10, var11, var12, var13);
         if (var1 != null) {
            TextureDraw var22 = this.sprite[this.numSprites];
            var22.u0 = var14;
            var22.u1 = var16;
            var22.u2 = var18;
            var22.u3 = var20;
            var22.v0 = var15;
            var22.v1 = var17;
            var22.v2 = var19;
            var22.v3 = var21;
         }

         this.style[this.numSprites] = this.defaultStyle;
         ++this.numSprites;
      }
   }

   public void render(Texture var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8, float var9, float var10, float var11, float var12, float var13, float var14, float var15, float var16, float var17, Consumer var18) {
      if (var9 != 0.0F) {
         if (this.numSprites == this.sprite.length) {
            this.CheckSpriteSlots();
         }

         this.sprite[this.numSprites].reset();
         TextureDraw.Create(this.sprite[this.numSprites], var1, var2, var3, var4, var5, var6, var7, var8, var9, var10, var11, var12, var13, var14, var15, var16, var17, var18);
         this.style[this.numSprites] = this.defaultStyle;
         ++this.numSprites;
      }
   }

   public void glBuffer(int var1, int var2) {
      if (this.numSprites == this.sprite.length) {
         this.CheckSpriteSlots();
      }

      TextureDraw.glBuffer(this.sprite[this.numSprites], var1, var2);
      this.style[this.numSprites] = TransparentStyle.instance;
      ++this.numSprites;
   }

   public void glDoStartFrame(int var1, int var2, float var3, int var4) {
      this.glDoStartFrame(var1, var2, var3, var4, false);
   }

   public void glDoStartFrame(int var1, int var2, float var3, int var4, boolean var5) {
      if (this.numSprites == this.sprite.length) {
         this.CheckSpriteSlots();
      }

      TextureDraw.glDoStartFrame(this.sprite[this.numSprites], var1, var2, var3, var4, var5);
      this.style[this.numSprites] = TransparentStyle.instance;
      ++this.numSprites;
   }

   public void glDoStartFrameFx(int var1, int var2, int var3) {
      if (this.numSprites == this.sprite.length) {
         this.CheckSpriteSlots();
      }

      TextureDraw.glDoStartFrameFx(this.sprite[this.numSprites], var1, var2, var3);
      this.style[this.numSprites] = TransparentStyle.instance;
      ++this.numSprites;
   }

   public void glIgnoreStyles(boolean var1) {
      if (this.numSprites == this.sprite.length) {
         this.CheckSpriteSlots();
      }

      TextureDraw.glIgnoreStyles(this.sprite[this.numSprites], var1);
      this.style[this.numSprites] = TransparentStyle.instance;
      ++this.numSprites;
   }

   public void glDoEndFrame() {
      if (this.numSprites == this.sprite.length) {
         this.CheckSpriteSlots();
      }

      TextureDraw.glDoEndFrame(this.sprite[this.numSprites]);
      this.style[this.numSprites] = TransparentStyle.instance;
      ++this.numSprites;
   }

   public void glDoEndFrameFx(int var1) {
      if (this.numSprites == this.sprite.length) {
         this.CheckSpriteSlots();
      }

      TextureDraw.glDoEndFrameFx(this.sprite[this.numSprites], var1);
      this.style[this.numSprites] = TransparentStyle.instance;
      ++this.numSprites;
   }

   public void doCoreIntParam(int var1, float var2) {
      this.CheckSpriteSlots();
      TextureDraw.doCoreIntParam(this.sprite[this.numSprites], var1, var2);
      this.style[this.numSprites] = TransparentStyle.instance;
      ++this.numSprites;
   }

   public void glTexParameteri(int var1, int var2, int var3) {
      if (this.numSprites == this.sprite.length) {
         this.CheckSpriteSlots();
      }

      TextureDraw.glTexParameteri(this.sprite[this.numSprites], var1, var2, var3);
      this.style[this.numSprites] = TransparentStyle.instance;
      ++this.numSprites;
   }

   public void setCutawayTexture(Texture var1, int var2, int var3, int var4, int var5) {
      this.texture1_cutaway = var1;
      this.texture1_cutaway_x = var2;
      this.texture1_cutaway_y = var3;
      this.texture1_cutaway_w = var4;
      this.texture1_cutaway_h = var5;
   }

   public void clearCutawayTexture() {
      this.texture1_cutaway = null;
   }

   public void setExtraWallShaderParams(SpriteRenderer.WallShaderTexRender var1) {
      this.wallShaderTexRender = var1;
   }

   public void ShaderUpdate1i(int var1, int var2, int var3) {
      if (this.numSprites == this.sprite.length) {
         this.CheckSpriteSlots();
      }

      TextureDraw.ShaderUpdate1i(this.sprite[this.numSprites], var1, var2, var3);
      this.style[this.numSprites] = TransparentStyle.instance;
      ++this.numSprites;
   }

   public void ShaderUpdate1f(int var1, int var2, float var3) {
      if (this.numSprites == this.sprite.length) {
         this.CheckSpriteSlots();
      }

      TextureDraw.ShaderUpdate1f(this.sprite[this.numSprites], var1, var2, var3);
      this.style[this.numSprites] = TransparentStyle.instance;
      ++this.numSprites;
   }

   public void ShaderUpdate2f(int var1, int var2, float var3, float var4) {
      if (this.numSprites == this.sprite.length) {
         this.CheckSpriteSlots();
      }

      TextureDraw.ShaderUpdate2f(this.sprite[this.numSprites], var1, var2, var3, var4);
      this.style[this.numSprites] = TransparentStyle.instance;
      ++this.numSprites;
   }

   public void ShaderUpdate3f(int var1, int var2, float var3, float var4, float var5) {
      if (this.numSprites == this.sprite.length) {
         this.CheckSpriteSlots();
      }

      TextureDraw.ShaderUpdate3f(this.sprite[this.numSprites], var1, var2, var3, var4, var5);
      this.style[this.numSprites] = TransparentStyle.instance;
      ++this.numSprites;
   }

   public void ShaderUpdate4f(int var1, int var2, float var3, float var4, float var5, float var6) {
      if (this.numSprites == this.sprite.length) {
         this.CheckSpriteSlots();
      }

      TextureDraw.ShaderUpdate4f(this.sprite[this.numSprites], var1, var2, var3, var4, var5, var6);
      this.style[this.numSprites] = TransparentStyle.instance;
      ++this.numSprites;
   }

   public void glLoadIdentity() {
      if (this.numSprites == this.sprite.length) {
         this.CheckSpriteSlots();
      }

      TextureDraw.glLoadIdentity(this.sprite[this.numSprites]);
      this.style[this.numSprites] = TransparentStyle.instance;
      ++this.numSprites;
   }

   public void glGenerateMipMaps(int var1) {
      if (this.numSprites == this.sprite.length) {
         this.CheckSpriteSlots();
      }

      TextureDraw.glGenerateMipMaps(this.sprite[this.numSprites], var1);
      this.style[this.numSprites] = TransparentStyle.instance;
      ++this.numSprites;
   }

   public void glBind(int var1) {
      if (this.numSprites == this.sprite.length) {
         this.CheckSpriteSlots();
      }

      TextureDraw.glBind(this.sprite[this.numSprites], var1);
      this.style[this.numSprites] = this.defaultStyle;
      ++this.numSprites;
   }

   public void glViewport(int var1, int var2, int var3, int var4) {
      if (this.numSprites == this.sprite.length) {
         this.CheckSpriteSlots();
      }

      TextureDraw.glViewport(this.sprite[this.numSprites], var1, var2, var3, var4);
      this.style[this.numSprites] = this.defaultStyle;
      ++this.numSprites;
   }

   public void drawModel(ModelManager.ModelSlot var1) {
      if (this.numSprites == this.sprite.length) {
         this.CheckSpriteSlots();
      }

      TextureDraw.drawModel(this.sprite[this.numSprites], var1);

      assert this.sprite[this.numSprites].drawer != null;

      ArrayList var2 = this.postRender;
      var2.add(this.sprite[this.numSprites]);
      this.style[this.numSprites] = this.defaultStyle;
      ++this.numSprites;
      ++var1.renderRefCount;
   }

   public void drawGeneric(TextureDraw.GenericDrawer var1) {
      if (this.numSprites == this.sprite.length) {
         this.CheckSpriteSlots();
      }

      this.sprite[this.numSprites].type = TextureDraw.Type.DrawModel;
      this.sprite[this.numSprites].drawer = var1;
      this.style[this.numSprites] = this.defaultStyle;
      ArrayList var2 = this.postRender;
      var2.add(this.sprite[this.numSprites]);
      ++this.numSprites;
   }

   public void StartShader(int var1, int var2) {
      if (this.numSprites == this.sprite.length) {
         this.CheckSpriteSlots();
      }

      TextureDraw.StartShader(this.sprite[this.numSprites], var1);
      if (var1 != 0 && Shader.ShaderMap.containsKey(var1)) {
         ((Shader)Shader.ShaderMap.get(var1)).startMainThread(this.sprite[this.numSprites], var2);
         ArrayList var3 = this.postRender;
         var3.add(this.sprite[this.numSprites]);
      }

      this.style[this.numSprites] = TransparentStyle.instance;
      ++this.numSprites;
   }

   public void EndShader() {
      if (this.numSprites == this.sprite.length) {
         this.CheckSpriteSlots();
      }

      TextureDraw.StartShader(this.sprite[this.numSprites], 0);
      this.style[this.numSprites] = TransparentStyle.instance;
      ++this.numSprites;
   }
}

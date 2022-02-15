package zombie.core.textures;

import java.util.function.Consumer;
import org.lwjgl.opengl.ARBShaderObjects;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL14;
import zombie.IndieGL;
import zombie.core.Color;
import zombie.core.Core;
import zombie.core.SpriteRenderer;
import zombie.core.opengl.Shader;
import zombie.core.skinnedmodel.ModelManager;
import zombie.core.skinnedmodel.model.ModelSlotRenderData;
import zombie.iso.IsoWorld;
import zombie.iso.weather.fx.WeatherFxMask;
import zombie.ui.UIManager;
import zombie.util.list.PZArrayUtil;

public final class TextureDraw {
   public TextureDraw.Type type;
   public int a;
   public int b;
   public float f1;
   public float[] vars;
   public int c;
   public int d;
   public int col0;
   public int col1;
   public int col2;
   public int col3;
   public float x0;
   public float x1;
   public float x2;
   public float x3;
   public float y0;
   public float y1;
   public float y2;
   public float y3;
   public float u0;
   public float u1;
   public float u2;
   public float u3;
   public float v0;
   public float v1;
   public float v2;
   public float v3;
   public Texture tex;
   public Texture tex1;
   public byte useAttribArray;
   public float tex1_u0;
   public float tex1_u1;
   public float tex1_u2;
   public float tex1_u3;
   public float tex1_v0;
   public float tex1_v1;
   public float tex1_v2;
   public float tex1_v3;
   public int tex1_col0;
   public int tex1_col1;
   public int tex1_col2;
   public int tex1_col3;
   public boolean bSingleCol;
   public boolean flipped;
   public TextureDraw.GenericDrawer drawer;

   public TextureDraw() {
      this.type = TextureDraw.Type.glDraw;
      this.a = 0;
      this.b = 0;
      this.f1 = 0.0F;
      this.c = 0;
      this.d = 0;
      this.bSingleCol = false;
      this.flipped = false;
   }

   public static void glStencilFunc(TextureDraw var0, int var1, int var2, int var3) {
      var0.type = TextureDraw.Type.glStencilFunc;
      var0.a = var1;
      var0.b = var2;
      var0.c = var3;
   }

   public static void glBuffer(TextureDraw var0, int var1, int var2) {
      var0.type = TextureDraw.Type.glBuffer;
      var0.a = var1;
      var0.b = var2;
   }

   public static void glStencilOp(TextureDraw var0, int var1, int var2, int var3) {
      var0.type = TextureDraw.Type.glStencilOp;
      var0.a = var1;
      var0.b = var2;
      var0.c = var3;
   }

   public static void glDisable(TextureDraw var0, int var1) {
      var0.type = TextureDraw.Type.glDisable;
      var0.a = var1;
   }

   public static void glClear(TextureDraw var0, int var1) {
      var0.type = TextureDraw.Type.glClear;
      var0.a = var1;
   }

   public static void glClearColor(TextureDraw var0, int var1, int var2, int var3, int var4) {
      var0.type = TextureDraw.Type.glClearColor;
      var0.col0 = var1;
      var0.col1 = var2;
      var0.col2 = var3;
      var0.col3 = var4;
   }

   public static void glEnable(TextureDraw var0, int var1) {
      var0.type = TextureDraw.Type.glEnable;
      var0.a = var1;
   }

   public static void glAlphaFunc(TextureDraw var0, int var1, float var2) {
      var0.type = TextureDraw.Type.glAlphaFunc;
      var0.a = var1;
      var0.f1 = var2;
   }

   public static void glColorMask(TextureDraw var0, int var1, int var2, int var3, int var4) {
      var0.type = TextureDraw.Type.glColorMask;
      var0.a = var1;
      var0.b = var2;
      var0.c = var3;
      var0.x0 = (float)var4;
   }

   public static void glStencilMask(TextureDraw var0, int var1) {
      var0.type = TextureDraw.Type.glStencilMask;
      var0.a = var1;
   }

   public static void glBlendFunc(TextureDraw var0, int var1, int var2) {
      var0.type = TextureDraw.Type.glBlendFunc;
      var0.a = var1;
      var0.b = var2;
   }

   public static void glBlendFuncSeparate(TextureDraw var0, int var1, int var2, int var3, int var4) {
      var0.type = TextureDraw.Type.glBlendFuncSeparate;
      var0.a = var1;
      var0.b = var2;
      var0.c = var3;
      var0.d = var4;
   }

   public static void glBlendEquation(TextureDraw var0, int var1) {
      var0.type = TextureDraw.Type.glBlendEquation;
      var0.a = var1;
   }

   public static void glDoEndFrame(TextureDraw var0) {
      var0.type = TextureDraw.Type.glDoEndFrame;
   }

   public static void glDoEndFrameFx(TextureDraw var0, int var1) {
      var0.type = TextureDraw.Type.glDoEndFrameFx;
      var0.c = var1;
   }

   public static void glIgnoreStyles(TextureDraw var0, boolean var1) {
      var0.type = TextureDraw.Type.glIgnoreStyles;
      var0.a = var1 ? 1 : 0;
   }

   public static void glDoStartFrame(TextureDraw var0, int var1, int var2, float var3, int var4) {
      glDoStartFrame(var0, var1, var2, var3, var4, false);
   }

   public static void glDoStartFrame(TextureDraw var0, int var1, int var2, float var3, int var4, boolean var5) {
      if (var5) {
         var0.type = TextureDraw.Type.glDoStartFrameText;
      } else {
         var0.type = TextureDraw.Type.glDoStartFrame;
      }

      var0.a = var1;
      var0.b = var2;
      var0.f1 = var3;
      var0.c = var4;
   }

   public static void glDoStartFrameFx(TextureDraw var0, int var1, int var2, int var3) {
      var0.type = TextureDraw.Type.glDoStartFrameFx;
      var0.a = var1;
      var0.b = var2;
      var0.c = var3;
   }

   public static void glTexParameteri(TextureDraw var0, int var1, int var2, int var3) {
      var0.type = TextureDraw.Type.glTexParameteri;
      var0.a = var1;
      var0.b = var2;
      var0.c = var3;
   }

   public static void drawModel(TextureDraw var0, ModelManager.ModelSlot var1) {
      var0.type = TextureDraw.Type.DrawModel;
      var0.a = var1.ID;
      var0.drawer = ModelSlotRenderData.alloc().init(var1);
   }

   public static void drawSkyBox(TextureDraw var0, Shader var1, int var2, int var3, int var4) {
      var0.type = TextureDraw.Type.DrawSkyBox;
      var0.a = var1.getID();
      var0.b = var2;
      var0.c = var3;
      var0.d = var4;
      var0.drawer = null;
   }

   public static void drawWater(TextureDraw var0, Shader var1, int var2, int var3, boolean var4) {
      var0.type = TextureDraw.Type.DrawWater;
      var0.a = var1.getID();
      var0.b = var2;
      var0.c = var3;
      var0.d = var4 ? 1 : 0;
      var0.drawer = null;
   }

   public static void drawPuddles(TextureDraw var0, Shader var1, int var2, int var3, int var4) {
      var0.type = TextureDraw.Type.DrawPuddles;
      var0.a = var1.getID();
      var0.b = var2;
      var0.c = var3;
      var0.d = var4;
      var0.drawer = null;
   }

   public static void drawParticles(TextureDraw var0, int var1, int var2, int var3) {
      var0.type = TextureDraw.Type.DrawParticles;
      var0.b = var1;
      var0.c = var2;
      var0.d = var3;
      var0.drawer = null;
   }

   public static void StartShader(TextureDraw var0, int var1) {
      var0.type = TextureDraw.Type.StartShader;
      var0.a = var1;
   }

   public static void ShaderUpdate1i(TextureDraw var0, int var1, int var2, int var3) {
      var0.type = TextureDraw.Type.ShaderUpdate;
      var0.a = var1;
      var0.b = var2;
      var0.c = -1;
      var0.d = var3;
   }

   public static void ShaderUpdate1f(TextureDraw var0, int var1, int var2, float var3) {
      var0.type = TextureDraw.Type.ShaderUpdate;
      var0.a = var1;
      var0.b = var2;
      var0.c = 1;
      var0.u0 = var3;
   }

   public static void ShaderUpdate2f(TextureDraw var0, int var1, int var2, float var3, float var4) {
      var0.type = TextureDraw.Type.ShaderUpdate;
      var0.a = var1;
      var0.b = var2;
      var0.c = 2;
      var0.u0 = var3;
      var0.u1 = var4;
   }

   public static void ShaderUpdate3f(TextureDraw var0, int var1, int var2, float var3, float var4, float var5) {
      var0.type = TextureDraw.Type.ShaderUpdate;
      var0.a = var1;
      var0.b = var2;
      var0.c = 3;
      var0.u0 = var3;
      var0.u1 = var4;
      var0.u2 = var5;
   }

   public static void ShaderUpdate4f(TextureDraw var0, int var1, int var2, float var3, float var4, float var5, float var6) {
      var0.type = TextureDraw.Type.ShaderUpdate;
      var0.a = var1;
      var0.b = var2;
      var0.c = 4;
      var0.u0 = var3;
      var0.u1 = var4;
      var0.u2 = var5;
      var0.u3 = var6;
   }

   public void run() {
      switch(this.type) {
      case StartShader:
         ARBShaderObjects.glUseProgramObjectARB(this.a);
         if (Shader.ShaderMap.containsKey(this.a)) {
            ((Shader)Shader.ShaderMap.get(this.a)).startRenderThread(this);
         }

         if (this.a == 0) {
            SpriteRenderer.ringBuffer.checkShaderChangedTexture1();
         }
         break;
      case ShaderUpdate:
         if (this.c == 1) {
            ARBShaderObjects.glUniform1fARB(this.b, this.u0);
         }

         if (this.c == 2) {
            ARBShaderObjects.glUniform2fARB(this.b, this.u0, this.u1);
         }

         if (this.c == 3) {
            ARBShaderObjects.glUniform3fARB(this.b, this.u0, this.u1, this.u2);
         }

         if (this.c == 4) {
            ARBShaderObjects.glUniform4fARB(this.b, this.u0, this.u1, this.u2, this.u3);
         }

         if (this.c == -1) {
            ARBShaderObjects.glUniform1iARB(this.b, this.d);
         }
         break;
      case BindActiveTexture:
         GL13.glActiveTexture(this.a);
         if (this.b != -1) {
            GL11.glBindTexture(3553, this.b);
         }

         GL13.glActiveTexture(33984);
         break;
      case DrawModel:
         if (this.drawer != null) {
            this.drawer.render();
         }
         break;
      case DrawSkyBox:
         try {
            ModelManager.instance.RenderSkyBox(this, this.a, this.b, this.c, this.d);
         } catch (Exception var5) {
            var5.printStackTrace();
         }
         break;
      case DrawWater:
         try {
            ModelManager.instance.RenderWater(this, this.a, this.b, this.d == 1);
         } catch (Exception var4) {
            var4.printStackTrace();
         }
         break;
      case DrawPuddles:
         try {
            ModelManager.instance.RenderPuddles(this.a, this.b, this.d);
         } catch (Exception var3) {
            var3.printStackTrace();
         }
         break;
      case DrawParticles:
         try {
            ModelManager.instance.RenderParticles(this, this.b, this.c);
         } catch (Exception var2) {
            var2.printStackTrace();
         }
         break;
      case glClear:
         IndieGL.glClearA(this.a);
         break;
      case glClearColor:
         GL11.glClearColor((float)this.col0 / 255.0F, (float)this.col1 / 255.0F, (float)this.col2 / 255.0F, (float)this.col3 / 255.0F);
         break;
      case glBlendFunc:
         IndieGL.glBlendFuncA(this.a, this.b);
         break;
      case glBlendFuncSeparate:
         GL14.glBlendFuncSeparate(this.a, this.b, this.c, this.d);
         break;
      case glColorMask:
         IndieGL.glColorMaskA(this.a == 1, this.b == 1, this.c == 1, this.x0 == 1.0F);
         break;
      case glTexParameteri:
         IndieGL.glTexParameteriActual(this.a, this.b, this.c);
         break;
      case glStencilMask:
         IndieGL.glStencilMaskA(this.a);
         break;
      case glDoEndFrame:
         Core.getInstance().DoEndFrameStuff(this.a, this.b);
         break;
      case glDoEndFrameFx:
         Core.getInstance().DoEndFrameStuffFx(this.a, this.b, this.c);
         break;
      case glDoStartFrame:
         Core.getInstance().DoStartFrameStuff(this.a, this.b, this.f1, this.c);
         break;
      case glDoStartFrameText:
         Core.getInstance().DoStartFrameStuff(this.a, this.b, this.f1, this.c, true);
         break;
      case glDoStartFrameFx:
         Core.getInstance().DoStartFrameStuffSmartTextureFx(this.a, this.b, this.c);
         break;
      case glStencilFunc:
         IndieGL.glStencilFuncA(this.a, this.b, this.c);
         break;
      case glBuffer:
         if (Core.getInstance().supportsFBO()) {
            if (this.a == 1) {
               SpriteRenderer.instance.getRenderingState().fbo.startDrawing(false, false);
            } else if (this.a == 2) {
               UIManager.UIFBO.startDrawing(true, true);
            } else if (this.a == 3) {
               UIManager.UIFBO.endDrawing();
            } else if (this.a == 4) {
               WeatherFxMask.getFboMask().startDrawing(true, true);
            } else if (this.a == 5) {
               WeatherFxMask.getFboMask().endDrawing();
            } else if (this.a == 6) {
               WeatherFxMask.getFboParticles().startDrawing(true, true);
            } else if (this.a == 7) {
               WeatherFxMask.getFboParticles().endDrawing();
            } else {
               SpriteRenderer.instance.getRenderingState().fbo.endDrawing();
            }
         }
         break;
      case glStencilOp:
         IndieGL.glStencilOpA(this.a, this.b, this.c);
         break;
      case glLoadIdentity:
         GL11.glLoadIdentity();
         break;
      case glBind:
         GL11.glBindTexture(3553, this.a);
         Texture.lastlastTextureID = Texture.lastTextureID;
         Texture.lastTextureID = this.a;
         break;
      case glViewport:
         GL11.glViewport(this.a, this.b, this.c, this.d);
         break;
      case drawTerrain:
         IsoWorld.instance.renderTerrain();
         break;
      case doCoreIntParam:
         Core.getInstance().FloatParamMap.put(this.a, this.f1);
         break;
      case glDepthMask:
         GL11.glDepthMask(this.a == 1);
      case glGenerateMipMaps:
      default:
         break;
      case glAlphaFunc:
         IndieGL.glAlphaFuncA(this.a, this.f1);
         break;
      case glEnable:
         IndieGL.glEnableA(this.a);
         break;
      case glDisable:
         IndieGL.glDisableA(this.a);
         break;
      case glBlendEquation:
         GL14.glBlendEquation(this.a);
         break;
      case glIgnoreStyles:
         SpriteRenderer.RingBuffer.IGNORE_STYLES = this.a == 1;
      }

   }

   public static void glDepthMask(TextureDraw var0, boolean var1) {
      var0.type = TextureDraw.Type.glDepthMask;
      var0.a = var1 ? 1 : 0;
   }

   public static void doCoreIntParam(TextureDraw var0, int var1, float var2) {
      var0.type = TextureDraw.Type.doCoreIntParam;
      var0.a = var1;
      var0.f1 = var2;
   }

   public String toString() {
      String var10000 = this.getClass().getSimpleName();
      return var10000 + "{ " + this.type + ", a:" + this.a + ", b:" + this.b + ", f1:" + this.f1 + ", vars:" + (this.vars != null ? PZArrayUtil.arrayToString(this.vars, "{", "}", ", ") : "null") + ", c:" + this.c + ", d:" + this.d + ", col0:" + this.col0 + ", col1:" + this.col1 + ", col2:" + this.col2 + ", col3:" + this.col3 + ", x0:" + this.x0 + ", x1:" + this.x1 + ", x2:" + this.x2 + ", x3:" + this.x3 + ", x0:" + this.x0 + ", x1:" + this.x1 + ", x2:" + this.x2 + ", x3:" + this.x3 + ", y0:" + this.y0 + ", y1:" + this.y1 + ", y2:" + this.y2 + ", y3:" + this.y3 + ", u0:" + this.u0 + ", u1:" + this.u1 + ", u2:" + this.u2 + ", u3:" + this.u3 + ", v0:" + this.v0 + ", v1:" + this.v1 + ", v2:" + this.v2 + ", v3:" + this.v3 + ", tex:" + this.tex + ", tex1:" + this.tex1 + ", useAttribArray:" + this.useAttribArray + ", tex1_u0:" + this.tex1_u0 + ", tex1_u1:" + this.tex1_u1 + ", tex1_u2:" + this.tex1_u2 + ", tex1_u3:" + this.tex1_u3 + ", tex1_u0:" + this.tex1_u0 + ", tex1_u1:" + this.tex1_u1 + ", tex1_u2:" + this.tex1_u2 + ", tex1_u3:" + this.tex1_u3 + ", tex1_col0:" + this.tex1_col0 + ", tex1_col1:" + this.tex1_col1 + ", tex1_col2:" + this.tex1_col2 + ", tex1_col3:" + this.tex1_col3 + ", bSingleCol:" + this.bSingleCol + " }";
   }

   public static TextureDraw Create(TextureDraw var0, Texture var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8, float var9, Consumer var10) {
      int var11 = Color.colorToABGR(var6, var7, var8, var9);
      Create(var0, var1, var2, var3, var2 + var4, var3, var2 + var4, var3 + var5, var2, var3 + var5, var11, var11, var11, var11, var10);
      return var0;
   }

   public static TextureDraw Create(TextureDraw var0, Texture var1, SpriteRenderer.WallShaderTexRender var2, float var3, float var4, float var5, float var6, float var7, float var8, float var9, float var10, Consumer var11) {
      int var12 = Color.colorToABGR(var7, var8, var9, var10);
      float var21 = 0.0F;
      float var22 = 0.0F;
      float var23 = 1.0F;
      float var24 = 0.0F;
      float var25 = 1.0F;
      float var26 = 1.0F;
      float var27 = 0.0F;
      float var28 = 1.0F;
      float var13;
      float var14;
      float var15;
      float var16;
      float var17;
      float var18;
      float var19;
      float var20;
      float var29;
      float var30;
      float var31;
      float var32;
      float var33;
      switch(var2) {
      case LeftOnly:
         var19 = var3;
         var13 = var3;
         var16 = var4;
         var14 = var4;
         var15 = var17 = var3 + var5 / 2.0F;
         var18 = var20 = var4 + var6;
         if (var1 != null) {
            var29 = var1.getXEnd();
            var30 = var1.getXStart();
            var31 = var1.getYEnd();
            var32 = var1.getYStart();
            var33 = 0.5F * (var29 - var30);
            var21 = var30;
            var23 = var30 + var33;
            var25 = var30 + var33;
            var27 = var30;
            var22 = var32;
            var24 = var32;
            var26 = var31;
            var28 = var31;
         }
         break;
      case RightOnly:
         var13 = var19 = var3 + var5 / 2.0F;
         var16 = var4;
         var14 = var4;
         var15 = var17 = var3 + var5;
         var18 = var20 = var4 + var6;
         if (var1 != null) {
            var29 = var1.getXEnd();
            var30 = var1.getXStart();
            var31 = var1.getYEnd();
            var32 = var1.getYStart();
            var33 = 0.5F * (var29 - var30);
            var21 = var30 + var33;
            var23 = var29;
            var25 = var29;
            var27 = var30 + var33;
            var22 = var32;
            var24 = var32;
            var26 = var31;
            var28 = var31;
         }
         break;
      case All:
      default:
         var19 = var3;
         var13 = var3;
         var16 = var4;
         var14 = var4;
         var15 = var17 = var3 + var5;
         var18 = var20 = var4 + var6;
         if (var1 != null) {
            var29 = var1.getXEnd();
            var30 = var1.getXStart();
            var31 = var1.getYEnd();
            var32 = var1.getYStart();
            var21 = var30;
            var23 = var29;
            var25 = var29;
            var27 = var30;
            var22 = var32;
            var24 = var32;
            var26 = var31;
            var28 = var31;
         }
      }

      Create(var0, var1, var13, var14, var15, var16, var17, var18, var19, var20, var12, var12, var12, var12, var21, var22, var23, var24, var25, var26, var27, var28, var11);
      return var0;
   }

   public static TextureDraw Create(TextureDraw var0, Texture var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8, float var9, float var10, float var11, float var12, float var13, float var14, float var15, float var16, float var17, Consumer var18) {
      int var19 = Color.colorToABGR(var6, var7, var8, var9);
      Create(var0, var1, var2, var3, var2 + var4, var3, var2 + var4, var3 + var5, var2, var3 + var5, var19, var19, var19, var19, var10, var11, var12, var13, var14, var15, var16, var17, var18);
      return var0;
   }

   public static void Create(TextureDraw var0, Texture var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8, float var9, float var10, float var11, float var12, float var13, float var14, float var15, float var16, float var17, float var18, float var19, float var20, float var21, float var22, float var23, float var24, float var25, Consumer var26) {
      int var27 = Color.colorToABGR(var10, var11, var12, var13);
      int var28 = Color.colorToABGR(var14, var15, var16, var17);
      int var29 = Color.colorToABGR(var18, var19, var20, var21);
      int var30 = Color.colorToABGR(var22, var23, var24, var25);
      Create(var0, var1, var2, var3, var4, var5, var6, var7, var8, var9, var27, var28, var29, var30, var26);
   }

   public static void Create(TextureDraw var0, Texture var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8, float var9, float var10, float var11, float var12, float var13) {
      int var14 = Color.colorToABGR(var10, var11, var12, var13);
      Create(var0, var1, var2, var3, var4, var5, var6, var7, var8, var9, var14, var14, var14, var14, (Consumer)null);
   }

   public static void Create(TextureDraw var0, Texture var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8, float var9, int var10, int var11, int var12, int var13) {
      Create(var0, var1, var2, var3, var4, var5, var6, var7, var8, var9, var10, var11, var12, var13, (Consumer)null);
   }

   public static TextureDraw Create(TextureDraw var0, Texture var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8, float var9, int var10, int var11, int var12, int var13, Consumer var14) {
      float var15 = 0.0F;
      float var16 = 0.0F;
      float var17 = 1.0F;
      float var18 = 0.0F;
      float var19 = 1.0F;
      float var20 = 1.0F;
      float var21 = 0.0F;
      float var22 = 1.0F;
      if (var1 != null) {
         float var23 = var1.getXEnd();
         float var24 = var1.getXStart();
         float var25 = var1.getYEnd();
         float var26 = var1.getYStart();
         var15 = var24;
         var16 = var26;
         var17 = var23;
         var18 = var26;
         var19 = var23;
         var20 = var25;
         var21 = var24;
         var22 = var25;
      }

      return Create(var0, var1, var2, var3, var4, var5, var6, var7, var8, var9, var10, var11, var12, var13, var15, var16, var17, var18, var19, var20, var21, var22, var14);
   }

   public static TextureDraw Create(TextureDraw var0, Texture var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8, float var9, int var10, int var11, int var12, int var13, float var14, float var15, float var16, float var17, float var18, float var19, float var20, float var21, Consumer var22) {
      var0.bSingleCol = var10 == var11 && var10 == var12 && var10 == var13;
      var0.tex = var1;
      var0.x0 = var2;
      var0.y0 = var3;
      var0.x1 = var4;
      var0.y1 = var5;
      var0.x2 = var6;
      var0.y2 = var7;
      var0.x3 = var8;
      var0.y3 = var9;
      var0.col0 = var10;
      var0.col1 = var11;
      var0.col2 = var12;
      var0.col3 = var13;
      var0.u0 = var14;
      var0.u1 = var16;
      var0.u2 = var18;
      var0.u3 = var20;
      var0.v0 = var15;
      var0.v1 = var17;
      var0.v2 = var19;
      var0.v3 = var21;
      if (var1 != null) {
         var0.flipped = var1.flip;
      }

      if (var22 != null) {
         var22.accept(var0);
         var0.bSingleCol = var0.col0 == var0.col1 && var0.col0 == var0.col2 && var0.col0 == var0.col3;
      }

      return var0;
   }

   public int getColor(int var1) {
      if (this.bSingleCol) {
         return this.col0;
      } else if (var1 == 0) {
         return this.col0;
      } else if (var1 == 1) {
         return this.col1;
      } else if (var1 == 2) {
         return this.col2;
      } else {
         return var1 == 3 ? this.col3 : this.col0;
      }
   }

   public void reset() {
      this.type = TextureDraw.Type.glDraw;
      this.flipped = false;
      this.tex = null;
      this.tex1 = null;
      this.useAttribArray = -1;
      this.col0 = -1;
      this.col1 = -1;
      this.col2 = -1;
      this.col3 = -1;
      this.bSingleCol = true;
      this.x0 = this.x1 = this.x2 = this.x3 = this.y0 = this.y1 = this.y2 = this.y3 = -1.0F;
      this.drawer = null;
   }

   public static void glLoadIdentity(TextureDraw var0) {
      var0.type = TextureDraw.Type.glLoadIdentity;
   }

   public static void glGenerateMipMaps(TextureDraw var0, int var1) {
      var0.type = TextureDraw.Type.glGenerateMipMaps;
      var0.a = var1;
   }

   public static void glBind(TextureDraw var0, int var1) {
      var0.type = TextureDraw.Type.glBind;
      var0.a = var1;
   }

   public static void glViewport(TextureDraw var0, int var1, int var2, int var3, int var4) {
      var0.type = TextureDraw.Type.glViewport;
      var0.a = var1;
      var0.b = var2;
      var0.c = var3;
      var0.d = var4;
   }

   public void postRender() {
      if (this.type == TextureDraw.Type.StartShader) {
         Shader var1 = (Shader)Shader.ShaderMap.get(this.a);
         if (var1 != null) {
            var1.postRender(this);
         }
      }

      if (this.drawer != null) {
         this.drawer.postRender();
         this.drawer = null;
      }

   }

   public static enum Type {
      glDraw,
      glBuffer,
      glStencilFunc,
      glAlphaFunc,
      glStencilOp,
      glEnable,
      glDisable,
      glColorMask,
      glStencilMask,
      glClear,
      glBlendFunc,
      glDoStartFrame,
      glDoStartFrameText,
      glDoEndFrame,
      glTexParameteri,
      StartShader,
      glLoadIdentity,
      glGenerateMipMaps,
      glBind,
      glViewport,
      DrawModel,
      DrawSkyBox,
      DrawWater,
      DrawPuddles,
      DrawParticles,
      ShaderUpdate,
      BindActiveTexture,
      glBlendEquation,
      glDoStartFrameFx,
      glDoEndFrameFx,
      glIgnoreStyles,
      glClearColor,
      glBlendFuncSeparate,
      glDepthMask,
      doCoreIntParam,
      drawTerrain;

      // $FF: synthetic method
      private static TextureDraw.Type[] $values() {
         return new TextureDraw.Type[]{glDraw, glBuffer, glStencilFunc, glAlphaFunc, glStencilOp, glEnable, glDisable, glColorMask, glStencilMask, glClear, glBlendFunc, glDoStartFrame, glDoStartFrameText, glDoEndFrame, glTexParameteri, StartShader, glLoadIdentity, glGenerateMipMaps, glBind, glViewport, DrawModel, DrawSkyBox, DrawWater, DrawPuddles, DrawParticles, ShaderUpdate, BindActiveTexture, glBlendEquation, glDoStartFrameFx, glDoEndFrameFx, glIgnoreStyles, glClearColor, glBlendFuncSeparate, glDepthMask, doCoreIntParam, drawTerrain};
      }
   }

   public abstract static class GenericDrawer {
      public abstract void render();

      public void postRender() {
      }
   }
}

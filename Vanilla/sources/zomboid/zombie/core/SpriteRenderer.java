package zombie.core;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import zombie.GameProfiler;
import zombie.asset.Asset;
import zombie.core.Styles.AbstractStyle;
import zombie.core.Styles.AdditiveStyle;
import zombie.core.Styles.AlphaOp;
import zombie.core.Styles.LightingStyle;
import zombie.core.Styles.Style;
import zombie.core.Styles.TransparentStyle;
import zombie.core.VBO.GLVertexBufferObject;
import zombie.core.math.PZMath;
import zombie.core.opengl.GLState;
import zombie.core.opengl.RenderThread;
import zombie.core.opengl.Shader;
import zombie.core.profiling.PerformanceProfileProbe;
import zombie.core.skinnedmodel.ModelManager;
import zombie.core.sprite.SpriteRenderState;
import zombie.core.sprite.SpriteRendererStates;
import zombie.core.textures.Texture;
import zombie.core.textures.TextureAssetManager;
import zombie.core.textures.TextureDraw;
import zombie.core.textures.TextureFBO;
import zombie.debug.DebugLog;
import zombie.debug.DebugOptions;
import zombie.iso.IsoGridSquare;
import zombie.iso.IsoPuddles;
import zombie.iso.PlayerCamera;
import zombie.util.list.PZArrayUtil;

public final class SpriteRenderer {
   public static final SpriteRenderer instance = new SpriteRenderer();
   static final int VERTEX_SIZE = 32;
   static final int TEXTURE0_COORD_OFFSET = 8;
   static final int TEXTURE1_COORD_OFFSET = 16;
   static final int TEXTURE2_ATTRIB_OFFSET = 24;
   static final int COLOR_OFFSET = 28;
   public static final SpriteRenderer.RingBuffer ringBuffer = new SpriteRenderer.RingBuffer();
   public static final int NUM_RENDER_STATES = 3;
   public final SpriteRendererStates m_states = new SpriteRendererStates();
   private volatile boolean m_waitingForRenderState = false;
   public static boolean GL_BLENDFUNC_ENABLED = true;

   public void create() {
      ringBuffer.create();
   }

   public void clearSprites() {
      this.m_states.getPopulating().clear();
   }

   public void glDepthMask(boolean var1) {
      this.m_states.getPopulatingActiveState().glDepthMask(var1);
   }

   public void renderflipped(Texture var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8, float var9, Consumer var10) {
      this.m_states.getPopulatingActiveState().renderflipped(var1, var2, var3, var4, var5, var6, var7, var8, var9, var10);
   }

   public void drawModel(ModelManager.ModelSlot var1) {
      this.m_states.getPopulatingActiveState().drawModel(var1);
   }

   public void drawSkyBox(Shader var1, int var2, int var3, int var4) {
      this.m_states.getPopulatingActiveState().drawSkyBox(var1, var2, var3, var4);
   }

   public void drawWater(Shader var1, int var2, int var3, boolean var4) {
      this.m_states.getPopulatingActiveState().drawWater(var1, var2, var3, var4);
   }

   public void drawPuddles(Shader var1, int var2, int var3, int var4) {
      this.m_states.getPopulatingActiveState().drawPuddles(var1, var2, var3, var4);
   }

   public void drawParticles(int var1, int var2, int var3) {
      this.m_states.getPopulatingActiveState().drawParticles(var1, var2, var3);
   }

   public void drawGeneric(TextureDraw.GenericDrawer var1) {
      this.m_states.getPopulatingActiveState().drawGeneric(var1);
   }

   public void glDisable(int var1) {
      this.m_states.getPopulatingActiveState().glDisable(var1);
   }

   public void glEnable(int var1) {
      this.m_states.getPopulatingActiveState().glEnable(var1);
   }

   public void glStencilMask(int var1) {
      this.m_states.getPopulatingActiveState().glStencilMask(var1);
   }

   public void glClear(int var1) {
      this.m_states.getPopulatingActiveState().glClear(var1);
   }

   public void glClearColor(int var1, int var2, int var3, int var4) {
      this.m_states.getPopulatingActiveState().glClearColor(var1, var2, var3, var4);
   }

   public void glStencilFunc(int var1, int var2, int var3) {
      this.m_states.getPopulatingActiveState().glStencilFunc(var1, var2, var3);
   }

   public void glStencilOp(int var1, int var2, int var3) {
      this.m_states.getPopulatingActiveState().glStencilOp(var1, var2, var3);
   }

   public void glColorMask(int var1, int var2, int var3, int var4) {
      this.m_states.getPopulatingActiveState().glColorMask(var1, var2, var3, var4);
   }

   public void glAlphaFunc(int var1, float var2) {
      this.m_states.getPopulatingActiveState().glAlphaFunc(var1, var2);
   }

   public void glBlendFunc(int var1, int var2) {
      this.m_states.getPopulatingActiveState().glBlendFunc(var1, var2);
   }

   public void glBlendFuncSeparate(int var1, int var2, int var3, int var4) {
      this.m_states.getPopulatingActiveState().glBlendFuncSeparate(var1, var2, var3, var4);
   }

   public void glBlendEquation(int var1) {
      this.m_states.getPopulatingActiveState().glBlendEquation(var1);
   }

   public void render(Texture var1, double var2, double var4, double var6, double var8, double var10, double var12, double var14, double var16, float var18, float var19, float var20, float var21, Consumer var22) {
      this.m_states.getPopulatingActiveState().render(var1, var2, var4, var6, var8, var10, var12, var14, var16, var18, var19, var20, var21, var22);
   }

   public void render(Texture var1, double var2, double var4, double var6, double var8, double var10, double var12, double var14, double var16, float var18, float var19, float var20, float var21, float var22, float var23, float var24, float var25, float var26, float var27, float var28, float var29, float var30, float var31, float var32, float var33, Consumer var34) {
      this.m_states.getPopulatingActiveState().render(var1, var2, var4, var6, var8, var10, var12, var14, var16, var18, var19, var20, var21, var22, var23, var24, var25, var26, var27, var28, var29, var30, var31, var32, var33, var34);
   }

   public void renderdebug(Texture var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8, float var9, float var10, float var11, float var12, float var13, float var14, float var15, float var16, float var17, float var18, float var19, float var20, float var21, float var22, float var23, float var24, float var25, Consumer var26) {
      this.m_states.getPopulatingActiveState().renderdebug(var1, var2, var3, var4, var5, var6, var7, var8, var9, var10, var11, var12, var13, var14, var15, var16, var17, var18, var19, var20, var21, var22, var23, var24, var25, var26);
   }

   public void renderline(Texture var1, int var2, int var3, int var4, int var5, float var6, float var7, float var8, float var9, int var10) {
      this.m_states.getPopulatingActiveState().renderline(var1, (float)var2, (float)var3, (float)var4, (float)var5, var6, var7, var8, var9, var10);
   }

   public void renderline(Texture var1, int var2, int var3, int var4, int var5, float var6, float var7, float var8, float var9) {
      this.m_states.getPopulatingActiveState().renderline(var1, var2, var3, var4, var5, var6, var7, var8, var9);
   }

   public void render(Texture var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8, float var9, int var10, int var11, int var12, int var13) {
      this.m_states.getPopulatingActiveState().render(var1, var2, var3, var4, var5, var6, var7, var8, var9, var10, var11, var12, var13);
   }

   public void render(Texture var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8, float var9, Consumer var10) {
      float var11 = PZMath.floor(var2);
      float var12 = PZMath.floor(var3);
      float var13 = PZMath.ceil(var2 + var4);
      float var14 = PZMath.ceil(var3 + var5);
      this.m_states.getPopulatingActiveState().render(var1, var11, var12, var13 - var11, var14 - var12, var6, var7, var8, var9, var10);
   }

   public void renderi(Texture var1, int var2, int var3, int var4, int var5, float var6, float var7, float var8, float var9, Consumer var10) {
      this.m_states.getPopulatingActiveState().render(var1, (float)var2, (float)var3, (float)var4, (float)var5, var6, var7, var8, var9, var10);
   }

   public void renderClamped(Texture var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8, int var9, float var10, float var11, float var12, float var13, Consumer var14) {
      int var15 = PZMath.clamp(var2, var6, var6 + var8);
      int var16 = PZMath.clamp(var3, var7, var7 + var9);
      int var17 = PZMath.clamp(var2 + var4, var6, var6 + var8);
      int var18 = PZMath.clamp(var3 + var5, var7, var7 + var9);
      if (var15 != var17 && var16 != var18) {
         int var19 = var15 - var2;
         int var20 = var2 + var4 - var17;
         int var21 = var16 - var3;
         int var22 = var3 + var5 - var18;
         if (var19 == 0 && var20 == 0 && var21 == 0 && var22 == 0) {
            this.m_states.getPopulatingActiveState().render(var1, (float)var2, (float)var3, (float)var4, (float)var5, var10, var11, var12, var13, var14);
         } else {
            float var23 = 0.0F;
            float var24 = 0.0F;
            float var25 = 1.0F;
            float var26 = 0.0F;
            float var27 = 1.0F;
            float var28 = 1.0F;
            float var29 = 0.0F;
            float var30 = 1.0F;
            if (var1 != null) {
               var23 = (float)var19 / (float)var4;
               var24 = (float)var21 / (float)var5;
               var25 = (float)(var4 - var20) / (float)var4;
               var26 = (float)var21 / (float)var5;
               var27 = (float)(var4 - var20) / (float)var4;
               var28 = (float)(var5 - var22) / (float)var5;
               var29 = (float)var19 / (float)var4;
               var30 = (float)(var5 - var22) / (float)var5;
            }

            var4 = var17 - var15;
            var5 = var18 - var16;
            this.m_states.getPopulatingActiveState().render(var1, (float)var15, (float)var16, (float)var4, (float)var5, var10, var11, var12, var13, var23, var24, var25, var26, var27, var28, var29, var30, var14);
         }
      }
   }

   public void renderRect(int var1, int var2, int var3, int var4, float var5, float var6, float var7, float var8) {
      this.m_states.getPopulatingActiveState().renderRect(var1, var2, var3, var4, var5, var6, var7, var8);
   }

   public void renderPoly(float var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8, float var9, float var10, float var11, float var12) {
      this.m_states.getPopulatingActiveState().renderPoly(var1, var2, var3, var4, var5, var6, var7, var8, var9, var10, var11, var12);
   }

   public void renderPoly(Texture var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8, float var9, float var10, float var11, float var12, float var13) {
      this.m_states.getPopulatingActiveState().renderPoly(var1, var2, var3, var4, var5, var6, var7, var8, var9, var10, var11, var12, var13);
   }

   public void renderPoly(Texture var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8, float var9, float var10, float var11, float var12, float var13, float var14, float var15, float var16, float var17, float var18, float var19, float var20, float var21) {
      this.m_states.getPopulatingActiveState().renderPoly(var1, var2, var3, var4, var5, var6, var7, var8, var9, var10, var11, var12, var13, var14, var15, var16, var17, var18, var19, var20, var21);
   }

   public void render(Texture var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8, float var9, float var10, float var11, float var12, float var13, float var14, float var15, float var16, float var17) {
      this.m_states.getPopulatingActiveState().render(var1, var2, var3, var4, var5, var6, var7, var8, var9, var10, var11, var12, var13, var14, var15, var16, var17, (Consumer)null);
   }

   public void render(Texture var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8, float var9, float var10, float var11, float var12, float var13, float var14, float var15, float var16, float var17, Consumer var18) {
      this.m_states.getPopulatingActiveState().render(var1, var2, var3, var4, var5, var6, var7, var8, var9, var10, var11, var12, var13, var14, var15, var16, var17, var18);
   }

   private static void buildDrawBuffer(TextureDraw[] var0, Style[] var1, int var2, SpriteRenderer.RingBuffer var3) {
      for(int var4 = 0; var4 < var2; ++var4) {
         TextureDraw var5 = var0[var4];
         Style var6 = var1[var4];
         TextureDraw var7 = null;
         if (var4 > 0) {
            var7 = var0[var4 - 1];
         }

         var3.add(var5, var7, var6);
      }

   }

   public void prePopulating() {
      this.m_states.getPopulating().prePopulating();
   }

   public void postRender() {
      SpriteRenderState var1 = this.m_states.getRendering();
      if (var1.numSprites == 0 && var1.stateUI.numSprites == 0) {
         var1.onRendered();
      } else {
         TextureFBO.reset();
         IsoPuddles.VBOs.startFrame();
         GameProfiler.getInstance().invokeAndMeasure("buildStateUIDrawBuffer(UI)", this, var1, SpriteRenderer::buildStateUIDrawBuffer);
         GameProfiler.getInstance().invokeAndMeasure("buildStateDrawBuffer", this, var1, SpriteRenderer::buildStateDrawBuffer);
         var1.onRendered();
         Core.getInstance().setLastRenderedFBO(var1.fbo);
         this.notifyRenderStateQueue();
      }
   }

   protected void buildStateDrawBuffer(SpriteRenderState var1) {
      ringBuffer.begin();
      buildDrawBuffer(var1.sprite, var1.style, var1.numSprites, ringBuffer);
      GameProfiler.getInstance().invokeAndMeasure("ringBuffer.render", () -> {
         ringBuffer.render();
      });
   }

   protected void buildStateUIDrawBuffer(SpriteRenderState var1) {
      if (var1.stateUI.numSprites > 0) {
         ringBuffer.begin();
         var1.stateUI.bActive = true;
         buildDrawBuffer(var1.stateUI.sprite, var1.stateUI.style, var1.stateUI.numSprites, ringBuffer);
         ringBuffer.render();
      }

      var1.stateUI.bActive = false;
   }

   public void notifyRenderStateQueue() {
      synchronized(this.m_states) {
         this.m_states.notifyAll();
      }
   }

   public void glBuffer(int var1, int var2) {
      this.m_states.getPopulatingActiveState().glBuffer(var1, var2);
   }

   public void glDoStartFrame(int var1, int var2, float var3, int var4) {
      this.m_states.getPopulatingActiveState().glDoStartFrame(var1, var2, var3, var4);
   }

   public void glDoStartFrame(int var1, int var2, float var3, int var4, boolean var5) {
      this.m_states.getPopulatingActiveState().glDoStartFrame(var1, var2, var3, var4, var5);
   }

   public void glDoStartFrameFx(int var1, int var2, int var3) {
      this.m_states.getPopulatingActiveState().glDoStartFrameFx(var1, var2, var3);
   }

   public void glIgnoreStyles(boolean var1) {
      this.m_states.getPopulatingActiveState().glIgnoreStyles(var1);
   }

   public void glDoEndFrame() {
      this.m_states.getPopulatingActiveState().glDoEndFrame();
   }

   public void glDoEndFrameFx(int var1) {
      this.m_states.getPopulatingActiveState().glDoEndFrameFx(var1);
   }

   public void doCoreIntParam(int var1, float var2) {
      this.m_states.getPopulatingActiveState().doCoreIntParam(var1, var2);
   }

   public void glTexParameteri(int var1, int var2, int var3) {
      this.m_states.getPopulatingActiveState().glTexParameteri(var1, var2, var3);
   }

   public void StartShader(int var1, int var2) {
      this.m_states.getPopulatingActiveState().StartShader(var1, var2);
   }

   public void EndShader() {
      this.m_states.getPopulatingActiveState().EndShader();
   }

   public void setCutawayTexture(Texture var1, int var2, int var3, int var4, int var5) {
      this.m_states.getPopulatingActiveState().setCutawayTexture(var1, var2, var3, var4, var5);
   }

   public void clearCutawayTexture() {
      this.m_states.getPopulatingActiveState().clearCutawayTexture();
   }

   public void setUseVertColorsArray(byte var1, int var2, int var3, int var4, int var5) {
      this.m_states.getPopulatingActiveState().setUseVertColorsArray(var1, var2, var3, var4, var5);
   }

   public void clearUseVertColorsArray() {
      this.m_states.getPopulatingActiveState().clearUseVertColorsArray();
   }

   public void setExtraWallShaderParams(SpriteRenderer.WallShaderTexRender var1) {
      this.m_states.getPopulatingActiveState().setExtraWallShaderParams(var1);
   }

   public void ShaderUpdate1i(int var1, int var2, int var3) {
      this.m_states.getPopulatingActiveState().ShaderUpdate1i(var1, var2, var3);
   }

   public void ShaderUpdate1f(int var1, int var2, float var3) {
      this.m_states.getPopulatingActiveState().ShaderUpdate1f(var1, var2, var3);
   }

   public void ShaderUpdate2f(int var1, int var2, float var3, float var4) {
      this.m_states.getPopulatingActiveState().ShaderUpdate2f(var1, var2, var3, var4);
   }

   public void ShaderUpdate3f(int var1, int var2, float var3, float var4, float var5) {
      this.m_states.getPopulatingActiveState().ShaderUpdate3f(var1, var2, var3, var4, var5);
   }

   public void ShaderUpdate4f(int var1, int var2, float var3, float var4, float var5, float var6) {
      this.m_states.getPopulatingActiveState().ShaderUpdate4f(var1, var2, var3, var4, var5, var6);
   }

   public void glLoadIdentity() {
      this.m_states.getPopulatingActiveState().glLoadIdentity();
   }

   public void glGenerateMipMaps(int var1) {
      this.m_states.getPopulatingActiveState().glGenerateMipMaps(var1);
   }

   public void glBind(int var1) {
      this.m_states.getPopulatingActiveState().glBind(var1);
   }

   public void glViewport(int var1, int var2, int var3, int var4) {
      this.m_states.getPopulatingActiveState().glViewport(var1, var2, var3, var4);
   }

   public void startOffscreenUI() {
      this.m_states.getPopulating().stateUI.bActive = true;
      this.m_states.getPopulating().stateUI.defaultStyle = TransparentStyle.instance;
      GLState.startFrame();
   }

   public void stopOffscreenUI() {
      this.m_states.getPopulating().stateUI.bActive = false;
   }

   public void pushFrameDown() {
      synchronized(this.m_states) {
         this.waitForReadySlotToOpen();
         this.m_states.movePopulatingToReady();
         this.notifyRenderStateQueue();
      }
   }

   public SpriteRenderState acquireStateForRendering(BooleanSupplier var1) {
      synchronized(this.m_states) {
         if (!this.waitForReadyState(var1)) {
            return null;
         } else {
            this.m_states.moveReadyToRendering();
            this.notifyRenderStateQueue();
            return this.m_states.getRendering();
         }
      }
   }

   private boolean waitForReadyState(BooleanSupplier var1) {
      boolean var2;
      try {
         SpriteRenderer.s_performance.waitForReadyState.start();
         var2 = this.waitForReadyStateInternal(var1);
      } finally {
         SpriteRenderer.s_performance.waitForReadyState.end();
      }

      return var2;
   }

   private boolean waitForReadyStateInternal(BooleanSupplier var1) {
      if (RenderThread.isRunning() && this.m_states.getReady() == null) {
         if (!RenderThread.isWaitForRenderState() && !this.isWaitingForRenderState()) {
            return false;
         } else {
            while(this.m_states.getReady() == null) {
               try {
                  if (!var1.getAsBoolean()) {
                     return false;
                  }

                  this.m_states.wait();
               } catch (InterruptedException var3) {
               }
            }

            return true;
         }
      } else {
         return true;
      }
   }

   private void waitForReadySlotToOpen() {
      try {
         SpriteRenderer.s_performance.waitForReadySlotToOpen.start();
         this.waitForReadySlotToOpenInternal();
      } finally {
         SpriteRenderer.s_performance.waitForReadySlotToOpen.end();
      }

   }

   private void waitForReadySlotToOpenInternal() {
      if (this.m_states.getReady() != null && RenderThread.isRunning()) {
         this.m_waitingForRenderState = true;

         while(this.m_states.getReady() != null) {
            try {
               this.m_states.wait();
            } catch (InterruptedException var2) {
            }
         }

         this.m_waitingForRenderState = false;
      }
   }

   public int getMainStateIndex() {
      return this.m_states.getPopulatingActiveState().index;
   }

   public int getRenderStateIndex() {
      return this.m_states.getRenderingActiveState().index;
   }

   public boolean getDoAdditive() {
      return this.m_states.getPopulatingActiveState().defaultStyle == AdditiveStyle.instance;
   }

   public void setDefaultStyle(AbstractStyle var1) {
      this.m_states.getPopulatingActiveState().defaultStyle = var1;
   }

   public void setDoAdditive(boolean var1) {
      this.m_states.getPopulatingActiveState().defaultStyle = (AbstractStyle)(var1 ? AdditiveStyle.instance : TransparentStyle.instance);
   }

   public void initFromIsoCamera(int var1) {
      this.m_states.getPopulating().playerCamera[var1].initFromIsoCamera(var1);
   }

   public void setRenderingPlayerIndex(int var1) {
      this.m_states.getRendering().playerIndex = var1;
   }

   public int getRenderingPlayerIndex() {
      return this.m_states.getRendering().playerIndex;
   }

   public PlayerCamera getRenderingPlayerCamera(int var1) {
      return this.m_states.getRendering().playerCamera[var1];
   }

   public SpriteRenderState getRenderingState() {
      return this.m_states.getRendering();
   }

   public SpriteRenderState getPopulatingState() {
      return this.m_states.getPopulating();
   }

   public boolean isMaxZoomLevel() {
      return this.getPlayerZoomLevel() >= this.getPlayerMaxZoom();
   }

   public boolean isMinZoomLevel() {
      return this.getPlayerZoomLevel() <= this.getPlayerMinZoom();
   }

   public float getPlayerZoomLevel() {
      SpriteRenderState var1 = this.m_states.getRendering();
      int var2 = var1.playerIndex;
      return var1.zoomLevel[var2];
   }

   public float getPlayerMaxZoom() {
      SpriteRenderState var1 = this.m_states.getRendering();
      return var1.maxZoomLevel;
   }

   public float getPlayerMinZoom() {
      SpriteRenderState var1 = this.m_states.getRendering();
      return var1.minZoomLevel;
   }

   public boolean isWaitingForRenderState() {
      return this.m_waitingForRenderState;
   }

   public static final class RingBuffer {
      GLVertexBufferObject[] vbo;
      GLVertexBufferObject[] ibo;
      long bufferSize;
      long bufferSizeInVertices;
      long indexBufferSize;
      int numBuffers;
      int sequence = -1;
      int mark = -1;
      FloatBuffer currentVertices;
      ShortBuffer currentIndices;
      FloatBuffer[] vertices;
      ByteBuffer[] verticesBytes;
      ShortBuffer[] indices;
      ByteBuffer[] indicesBytes;
      Texture lastRenderedTexture0;
      Texture currentTexture0;
      Texture lastRenderedTexture1;
      Texture currentTexture1;
      boolean shaderChangedTexture1 = false;
      byte lastUseAttribArray;
      byte currentUseAttribArray;
      Style lastRenderedStyle;
      Style currentStyle;
      SpriteRenderer.RingBuffer.StateRun[] stateRun;
      public boolean restoreVBOs;
      public boolean restoreBoundTextures;
      int vertexCursor;
      int indexCursor;
      int numRuns;
      SpriteRenderer.RingBuffer.StateRun currentRun;
      public static boolean IGNORE_STYLES = false;

      RingBuffer() {
      }

      void create() {
         GL11.glEnableClientState(32884);
         GL11.glEnableClientState(32886);
         GL11.glEnableClientState(32888);
         this.bufferSize = 65536L;
         this.numBuffers = Core.bDebug ? 256 : 128;
         this.bufferSizeInVertices = this.bufferSize / 32L;
         this.indexBufferSize = this.bufferSizeInVertices * 3L;
         this.vertices = new FloatBuffer[this.numBuffers];
         this.verticesBytes = new ByteBuffer[this.numBuffers];
         this.indices = new ShortBuffer[this.numBuffers];
         this.indicesBytes = new ByteBuffer[this.numBuffers];
         this.stateRun = new SpriteRenderer.RingBuffer.StateRun[5000];

         int var1;
         for(var1 = 0; var1 < 5000; ++var1) {
            this.stateRun[var1] = new SpriteRenderer.RingBuffer.StateRun();
         }

         this.vbo = new GLVertexBufferObject[this.numBuffers];
         this.ibo = new GLVertexBufferObject[this.numBuffers];

         for(var1 = 0; var1 < this.numBuffers; ++var1) {
            this.vbo[var1] = new GLVertexBufferObject(this.bufferSize, GLVertexBufferObject.funcs.GL_ARRAY_BUFFER(), GLVertexBufferObject.funcs.GL_STREAM_DRAW());
            this.vbo[var1].create();
            this.ibo[var1] = new GLVertexBufferObject(this.indexBufferSize, GLVertexBufferObject.funcs.GL_ELEMENT_ARRAY_BUFFER(), GLVertexBufferObject.funcs.GL_STREAM_DRAW());
            this.ibo[var1].create();
         }

      }

      void add(TextureDraw var1, TextureDraw var2, Style var3) {
         if (var3 != null) {
            if ((long)(this.vertexCursor + 4) > this.bufferSizeInVertices || (long)(this.indexCursor + 6) > this.indexBufferSize) {
               this.render();
               this.next();
            }

            if (this.prepareCurrentRun(var1, var2, var3)) {
               FloatBuffer var4 = this.currentVertices;
               AlphaOp var5 = var3.getAlphaOp();
               var4.put(var1.x0);
               var4.put(var1.y0);
               if (var1.tex == null) {
                  var4.put(0.0F);
                  var4.put(0.0F);
               } else {
                  if (var1.flipped) {
                     var4.put(var1.u1);
                  } else {
                     var4.put(var1.u0);
                  }

                  var4.put(var1.v0);
               }

               if (var1.tex1 == null) {
                  var4.put(0.0F);
                  var4.put(0.0F);
               } else {
                  var4.put(var1.tex1_u0);
                  var4.put(var1.tex1_v0);
               }

               var4.put(Float.intBitsToFloat(var1.useAttribArray != -1 ? var1.tex1_col0 : 0));
               int var6 = var1.getColor(0);
               var5.op(var6, 255, var4);
               var4.put(var1.x1);
               var4.put(var1.y1);
               if (var1.tex == null) {
                  var4.put(0.0F);
                  var4.put(0.0F);
               } else {
                  if (var1.flipped) {
                     var4.put(var1.u0);
                  } else {
                     var4.put(var1.u1);
                  }

                  var4.put(var1.v1);
               }

               if (var1.tex1 == null) {
                  var4.put(0.0F);
                  var4.put(0.0F);
               } else {
                  var4.put(var1.tex1_u1);
                  var4.put(var1.tex1_v1);
               }

               var4.put(Float.intBitsToFloat(var1.useAttribArray != -1 ? var1.tex1_col1 : 0));
               var6 = var1.getColor(1);
               var5.op(var6, 255, var4);
               var4.put(var1.x2);
               var4.put(var1.y2);
               if (var1.tex == null) {
                  var4.put(0.0F);
                  var4.put(0.0F);
               } else {
                  if (var1.flipped) {
                     var4.put(var1.u3);
                  } else {
                     var4.put(var1.u2);
                  }

                  var4.put(var1.v2);
               }

               if (var1.tex1 == null) {
                  var4.put(0.0F);
                  var4.put(0.0F);
               } else {
                  var4.put(var1.tex1_u2);
                  var4.put(var1.tex1_v2);
               }

               var4.put(Float.intBitsToFloat(var1.useAttribArray != -1 ? var1.tex1_col2 : 0));
               var6 = var1.getColor(2);
               var5.op(var6, 255, var4);
               var4.put(var1.x3);
               var4.put(var1.y3);
               if (var1.tex == null) {
                  var4.put(0.0F);
                  var4.put(0.0F);
               } else {
                  if (var1.flipped) {
                     var4.put(var1.u2);
                  } else {
                     var4.put(var1.u3);
                  }

                  var4.put(var1.v3);
               }

               if (var1.tex1 == null) {
                  var4.put(0.0F);
                  var4.put(0.0F);
               } else {
                  var4.put(var1.tex1_u3);
                  var4.put(var1.tex1_v3);
               }

               var4.put(Float.intBitsToFloat(var1.useAttribArray != -1 ? var1.tex1_col3 : 0));
               var6 = var1.getColor(3);
               var5.op(var6, 255, var4);
               this.currentIndices.put((short)this.vertexCursor);
               this.currentIndices.put((short)(this.vertexCursor + 1));
               this.currentIndices.put((short)(this.vertexCursor + 2));
               this.currentIndices.put((short)this.vertexCursor);
               this.currentIndices.put((short)(this.vertexCursor + 2));
               this.currentIndices.put((short)(this.vertexCursor + 3));
               this.indexCursor += 6;
               this.vertexCursor += 4;
               SpriteRenderer.RingBuffer.StateRun var10000 = this.currentRun;
               var10000.endIndex += 6;
               var10000 = this.currentRun;
               var10000.length += 4;
            }
         }
      }

      private boolean prepareCurrentRun(TextureDraw var1, TextureDraw var2, Style var3) {
         Texture var4 = var1.tex;
         Texture var5 = var1.tex1;
         byte var6 = var1.useAttribArray;
         if (this.isStateChanged(var1, var2, var3, var4, var5, var6)) {
            this.currentRun = this.stateRun[this.numRuns];
            this.currentRun.start = this.vertexCursor;
            this.currentRun.length = 0;
            this.currentRun.style = var3;
            this.currentRun.texture0 = var4;
            this.currentRun.texture1 = var5;
            this.currentRun.useAttribArray = var6;
            this.currentRun.indices = this.currentIndices;
            this.currentRun.startIndex = this.indexCursor;
            this.currentRun.endIndex = this.indexCursor;
            ++this.numRuns;
            if (this.numRuns == this.stateRun.length) {
               this.growStateRuns();
            }

            this.currentStyle = var3;
            this.currentTexture0 = var4;
            this.currentTexture1 = var5;
            this.currentUseAttribArray = var6;
         }

         if (var1.type != TextureDraw.Type.glDraw) {
            this.currentRun.ops.add(var1);
            return false;
         } else {
            return true;
         }
      }

      private boolean isStateChanged(TextureDraw var1, TextureDraw var2, Style var3, Texture var4, Texture var5, byte var6) {
         if (this.currentRun == null) {
            return true;
         } else if (var1.type == TextureDraw.Type.DrawModel) {
            return true;
         } else if (var6 != this.currentUseAttribArray) {
            return true;
         } else if (var4 != this.currentTexture0) {
            return true;
         } else if (var5 != this.currentTexture1) {
            return true;
         } else {
            if (var2 != null) {
               if (var2.type == TextureDraw.Type.DrawModel) {
                  return true;
               }

               if (var1.type == TextureDraw.Type.glDraw && var2.type != TextureDraw.Type.glDraw) {
                  return true;
               }

               if (var1.type != TextureDraw.Type.glDraw && var2.type == TextureDraw.Type.glDraw) {
                  return true;
               }
            }

            if (var3 != this.currentStyle) {
               if (this.currentStyle == null) {
                  return true;
               }

               if (var3.getStyleID() != this.currentStyle.getStyleID()) {
                  return true;
               }
            }

            return false;
         }
      }

      private void next() {
         ++this.sequence;
         if (this.sequence == this.numBuffers) {
            this.sequence = 0;
         }

         if (this.sequence == this.mark) {
            DebugLog.General.error("Buffer overrun.");
         }

         this.vbo[this.sequence].bind();
         ByteBuffer var1 = this.vbo[this.sequence].map();
         if (this.vertices[this.sequence] == null || this.verticesBytes[this.sequence] != var1) {
            this.verticesBytes[this.sequence] = var1;
            this.vertices[this.sequence] = var1.asFloatBuffer();
         }

         this.ibo[this.sequence].bind();
         ByteBuffer var2 = this.ibo[this.sequence].map();
         if (this.indices[this.sequence] == null || this.indicesBytes[this.sequence] != var2) {
            this.indicesBytes[this.sequence] = var2;
            this.indices[this.sequence] = var2.asShortBuffer();
         }

         this.currentVertices = this.vertices[this.sequence];
         this.currentVertices.clear();
         this.currentIndices = this.indices[this.sequence];
         this.currentIndices.clear();
         this.vertexCursor = 0;
         this.indexCursor = 0;
         this.numRuns = 0;
         this.currentRun = null;
      }

      void begin() {
         this.currentStyle = null;
         this.currentTexture0 = null;
         this.currentTexture1 = null;
         this.currentUseAttribArray = -1;
         this.next();
         this.mark = this.sequence;
      }

      void render() {
         this.vbo[this.sequence].unmap();
         this.ibo[this.sequence].unmap();
         this.restoreVBOs = true;

         for(int var1 = 0; var1 < this.numRuns; ++var1) {
            this.stateRun[var1].render();
         }

      }

      void growStateRuns() {
         SpriteRenderer.RingBuffer.StateRun[] var1 = new SpriteRenderer.RingBuffer.StateRun[(int)((float)this.stateRun.length * 1.5F)];
         System.arraycopy(this.stateRun, 0, var1, 0, this.stateRun.length);

         for(int var2 = this.numRuns; var2 < var1.length; ++var2) {
            var1[var2] = new SpriteRenderer.RingBuffer.StateRun();
         }

         this.stateRun = var1;
      }

      public void shaderChangedTexture1() {
         this.shaderChangedTexture1 = true;
      }

      public void checkShaderChangedTexture1() {
         if (this.shaderChangedTexture1) {
            this.shaderChangedTexture1 = false;
            this.lastRenderedTexture1 = null;
            GL13.glActiveTexture(33985);
            GL13.glClientActiveTexture(33985);
            GL11.glDisable(3553);
            GL13.glActiveTexture(33984);
            GL13.glClientActiveTexture(33984);
         }

      }

      public void debugBoundTexture(Texture var1, int var2) {
         if (GL11.glGetInteger(34016) == var2) {
            int var3 = GL11.glGetInteger(32873);
            String var4 = null;
            Iterator var5;
            Asset var6;
            Texture var7;
            if (var1 == null && var3 != 0) {
               var5 = TextureAssetManager.instance.getAssetTable().values().iterator();

               while(var5.hasNext()) {
                  var6 = (Asset)var5.next();
                  var7 = (Texture)var6;
                  if (var7.getID() == var3) {
                     var4 = var7.getPath().getPath();
                     break;
                  }
               }

               DebugLog.General.error("SpriteRenderer.lastBoundTexture0=null doesn't match OpenGL texture id=" + var3 + " " + var4);
            } else if (var1 != null && var1.getID() != -1 && var3 != var1.getID()) {
               var5 = TextureAssetManager.instance.getAssetTable().values().iterator();

               while(var5.hasNext()) {
                  var6 = (Asset)var5.next();
                  var7 = (Texture)var6;
                  if (var7.getID() == var3) {
                     var4 = var7.getName();
                     break;
                  }
               }

               DebugLog.General.error("SpriteRenderer.lastBoundTexture0 id=" + var1.getID() + " doesn't match OpenGL texture id=" + var3 + " " + var4);
            }
         }

      }

      private class StateRun {
         Texture texture0;
         Texture texture1;
         byte useAttribArray = -1;
         Style style;
         int start;
         int length;
         ShortBuffer indices;
         int startIndex;
         int endIndex;
         final ArrayList ops = new ArrayList();

         public String toString() {
            String var1 = System.lineSeparator();
            String var10000 = this.getClass().getSimpleName();
            return var10000 + "{ " + var1 + "  ops:" + PZArrayUtil.arrayToString((Iterable)this.ops, "{", "}", ", ") + var1 + "  texture0:" + this.texture0 + var1 + "  texture1:" + this.texture1 + var1 + "  useAttribArray:" + this.useAttribArray + var1 + "  style:" + this.style + var1 + "  start:" + this.start + var1 + "  length:" + this.length + var1 + "  indices:" + this.indices + var1 + "  startIndex:" + this.startIndex + var1 + "  endIndex:" + this.endIndex + var1 + "}";
         }

         void render() {
            if (this.style != null) {
               int var1 = this.ops.size();
               int var2;
               if (var1 > 0) {
                  for(var2 = 0; var2 < var1; ++var2) {
                     ((TextureDraw)this.ops.get(var2)).run();
                  }

                  this.ops.clear();
               } else {
                  if (this.style != RingBuffer.this.lastRenderedStyle) {
                     if (RingBuffer.this.lastRenderedStyle != null && (!SpriteRenderer.RingBuffer.IGNORE_STYLES || RingBuffer.this.lastRenderedStyle != AdditiveStyle.instance && RingBuffer.this.lastRenderedStyle != TransparentStyle.instance && RingBuffer.this.lastRenderedStyle != LightingStyle.instance)) {
                        RingBuffer.this.lastRenderedStyle.resetState();
                     }

                     if (this.style != null && (!SpriteRenderer.RingBuffer.IGNORE_STYLES || this.style != AdditiveStyle.instance && this.style != TransparentStyle.instance && this.style != LightingStyle.instance)) {
                        this.style.setupState();
                     }

                     RingBuffer.this.lastRenderedStyle = this.style;
                  }

                  if (RingBuffer.this.lastRenderedTexture0 != null && RingBuffer.this.lastRenderedTexture0.getID() != Texture.lastTextureID) {
                     RingBuffer.this.restoreBoundTextures = true;
                  }

                  if (RingBuffer.this.restoreBoundTextures) {
                     Texture.lastTextureID = 0;
                     GL11.glBindTexture(3553, 0);
                     if (this.texture0 == null) {
                        GL11.glDisable(3553);
                     }

                     RingBuffer.this.lastRenderedTexture0 = null;
                     RingBuffer.this.lastRenderedTexture1 = null;
                     RingBuffer.this.restoreBoundTextures = false;
                  }

                  if (this.texture0 != RingBuffer.this.lastRenderedTexture0) {
                     if (this.texture0 != null) {
                        if (RingBuffer.this.lastRenderedTexture0 == null) {
                           GL11.glEnable(3553);
                        }

                        this.texture0.bind();
                     } else {
                        GL11.glDisable(3553);
                        Texture.lastTextureID = 0;
                        GL11.glBindTexture(3553, 0);
                     }

                     RingBuffer.this.lastRenderedTexture0 = this.texture0;
                  }

                  if (DebugOptions.instance.Checks.BoundTextures.getValue()) {
                     RingBuffer.this.debugBoundTexture(RingBuffer.this.lastRenderedTexture0, 33984);
                  }

                  if (this.texture1 != RingBuffer.this.lastRenderedTexture1) {
                     GL13.glActiveTexture(33985);
                     GL13.glClientActiveTexture(33985);
                     if (this.texture1 != null) {
                        GL11.glBindTexture(3553, this.texture1.getID());
                     } else {
                        GL11.glDisable(3553);
                     }

                     GL13.glActiveTexture(33984);
                     GL13.glClientActiveTexture(33984);
                     RingBuffer.this.lastRenderedTexture1 = this.texture1;
                  }

                  if (this.useAttribArray != RingBuffer.this.lastUseAttribArray) {
                     if (this.useAttribArray != -1) {
                        if (this.useAttribArray == 1) {
                           var2 = IsoGridSquare.CircleStencilShader.instance.a_wallShadeColor;
                           if (var2 != -1) {
                              GL20.glEnableVertexAttribArray(var2);
                           }
                        }

                        if (this.useAttribArray == 2) {
                           var2 = IsoGridSquare.NoCircleStencilShader.instance.a_wallShadeColor;
                           if (var2 != -1) {
                              GL20.glEnableVertexAttribArray(var2);
                           }
                        }
                     } else {
                        if (RingBuffer.this.lastUseAttribArray == 1) {
                           var2 = IsoGridSquare.CircleStencilShader.instance.a_wallShadeColor;
                           if (var2 != -1) {
                              GL20.glDisableVertexAttribArray(var2);
                           }
                        }

                        if (RingBuffer.this.lastUseAttribArray == 2) {
                           var2 = IsoGridSquare.NoCircleStencilShader.instance.a_wallShadeColor;
                           if (var2 != -1) {
                              GL20.glDisableVertexAttribArray(var2);
                           }
                        }
                     }

                     RingBuffer.this.lastUseAttribArray = this.useAttribArray;
                  }

                  if (this.length != 0) {
                     if (this.length == -1) {
                        RingBuffer.this.restoreVBOs = true;
                     } else {
                        if (RingBuffer.this.restoreVBOs) {
                           RingBuffer.this.restoreVBOs = false;
                           RingBuffer.this.vbo[RingBuffer.this.sequence].bind();
                           RingBuffer.this.ibo[RingBuffer.this.sequence].bind();
                           GL11.glVertexPointer(2, 5126, 32, 0L);
                           GL11.glTexCoordPointer(2, 5126, 32, 8L);
                           GL11.glColorPointer(4, 5121, 32, 28L);
                           GL13.glActiveTexture(33985);
                           GL13.glClientActiveTexture(33985);
                           GL11.glTexCoordPointer(2, 5126, 32, 16L);
                           GL11.glEnableClientState(32888);
                           var2 = IsoGridSquare.CircleStencilShader.instance.a_wallShadeColor;
                           if (var2 != -1) {
                              GL20.glVertexAttribPointer(var2, 4, 5121, true, 32, 24L);
                           }

                           var2 = IsoGridSquare.NoCircleStencilShader.instance.a_wallShadeColor;
                           if (var2 != -1) {
                              GL20.glVertexAttribPointer(var2, 4, 5121, true, 32, 24L);
                           }

                           GL13.glActiveTexture(33984);
                           GL13.glClientActiveTexture(33984);
                        }

                        assert GL11.glGetInteger(34964) == RingBuffer.this.vbo[RingBuffer.this.sequence].getID();

                        if (this.useAttribArray == 1) {
                           RingBuffer.this.vbo[RingBuffer.this.sequence].enableVertexAttribArray(IsoGridSquare.CircleStencilShader.instance.a_wallShadeColor);

                           assert GL20.glGetVertexAttribi(IsoGridSquare.CircleStencilShader.instance.a_wallShadeColor, 34975) != 0;
                        } else if (this.useAttribArray == 2) {
                           RingBuffer.this.vbo[RingBuffer.this.sequence].enableVertexAttribArray(IsoGridSquare.NoCircleStencilShader.instance.a_wallShadeColor);
                        } else {
                           RingBuffer.this.vbo[RingBuffer.this.sequence].disableVertexAttribArray();
                        }

                        if (this.style.getRenderSprite()) {
                           GL12.glDrawRangeElements(4, this.start, this.start + this.length, this.endIndex - this.startIndex, 5123, (long)this.startIndex * 2L);
                        } else {
                           this.style.render(this.start, this.startIndex);
                        }

                     }
                  }
               }
            }
         }
      }
   }

   public static enum WallShaderTexRender {
      All,
      LeftOnly,
      RightOnly;

      // $FF: synthetic method
      private static SpriteRenderer.WallShaderTexRender[] $values() {
         return new SpriteRenderer.WallShaderTexRender[]{All, LeftOnly, RightOnly};
      }
   }

   private static class s_performance {
      private static final PerformanceProfileProbe waitForReadyState = new PerformanceProfileProbe("waitForReadyState");
      private static final PerformanceProfileProbe waitForReadySlotToOpen = new PerformanceProfileProbe("waitForReadySlotToOpen");
   }
}

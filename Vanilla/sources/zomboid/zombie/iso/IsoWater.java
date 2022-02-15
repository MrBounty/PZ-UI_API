package zombie.iso;

import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import org.joml.Vector2f;
import org.joml.Vector4f;
import org.lwjgl.opengl.ARBShaderObjects;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL13;
import zombie.GameTime;
import zombie.core.PerformanceSettings;
import zombie.core.SpriteRenderer;
import zombie.core.opengl.RenderThread;
import zombie.core.opengl.Shader;
import zombie.core.textures.Texture;
import zombie.interfaces.ITexture;
import zombie.iso.weather.ClimateManager;

public final class IsoWater {
   public Shader Effect;
   private float WaterTime;
   private float WaterWindAngle;
   private float WaterWindIntensity;
   private float WaterRainIntensity;
   private Vector2f WaterParamWindINT;
   private Texture texBottom = Texture.getSharedTexture("media/textures/river_bottom.png");
   private int apiId;
   private static IsoWater instance;
   private static boolean isShaderEnable = false;
   private final IsoWater.RenderData[][] renderData = new IsoWater.RenderData[3][4];
   private final IsoWater.RenderData[][] renderDataShore = new IsoWater.RenderData[3][4];
   static final int BYTES_PER_FLOAT = 4;
   static final int FLOATS_PER_VERTEX = 7;
   static final int BYTES_PER_VERTEX = 28;
   static final int VERTICES_PER_SQUARE = 4;
   private final Vector4f shaderOffset = new Vector4f();

   public static synchronized IsoWater getInstance() {
      if (instance == null) {
         instance = new IsoWater();
      }

      return instance;
   }

   public boolean getShaderEnable() {
      return isShaderEnable;
   }

   public IsoWater() {
      RenderThread.invokeOnRenderContext(() -> {
         if (GL.getCapabilities().OpenGL30) {
            this.apiId = 1;
         }

         if (GL.getCapabilities().GL_ARB_framebuffer_object) {
            this.apiId = 2;
         }

         if (GL.getCapabilities().GL_EXT_framebuffer_object) {
            this.apiId = 3;
         }

      });

      for(int var1 = 0; var1 < this.renderData.length; ++var1) {
         for(int var2 = 0; var2 < 4; ++var2) {
            this.renderData[var1][var2] = new IsoWater.RenderData();
            this.renderDataShore[var1][var2] = new IsoWater.RenderData();
         }
      }

      this.applyWaterQuality();
      this.WaterParamWindINT = new Vector2f(0.0F);
   }

   public void applyWaterQuality() {
      if (PerformanceSettings.WaterQuality == 2) {
         isShaderEnable = false;
      }

      if (PerformanceSettings.WaterQuality == 1) {
         isShaderEnable = true;
         RenderThread.invokeOnRenderContext(() -> {
            ARBShaderObjects.glUseProgramObjectARB(0);
            this.Effect = new WaterShader("water");
            ARBShaderObjects.glUseProgramObjectARB(0);
         });
      }

      if (PerformanceSettings.WaterQuality == 0) {
         isShaderEnable = true;
         RenderThread.invokeOnRenderContext(() -> {
            this.Effect = new WaterShader("water_hq");
            this.Effect.Start();
            this.Effect.End();
         });
      }

   }

   public void render(ArrayList var1, boolean var2) {
      if (this.getShaderEnable()) {
         int var3 = IsoCamera.frameState.playerIndex;
         int var4 = SpriteRenderer.instance.getMainStateIndex();
         IsoWater.RenderData var5 = this.renderData[var4][var3];
         IsoWater.RenderData var6 = this.renderDataShore[var4][var3];
         if (var2) {
            if (var6.numSquares > 0) {
               SpriteRenderer.instance.drawWater(this.Effect, var3, this.apiId, true);
            }

         } else {
            var5.clear();
            var6.clear();

            for(int var7 = 0; var7 < var1.size(); ++var7) {
               IsoGridSquare var8 = (IsoGridSquare)var1.get(var7);
               if (var8.chunk == null || !var8.chunk.bLightingNeverDone[var3]) {
                  IsoWaterGeometry var9 = var8.getWater();
                  if (var9 != null) {
                     if (var9.bShore) {
                        var6.addSquare(var9);
                     } else if (var9.hasWater) {
                        var5.addSquare(var9);
                     }
                  }
               }
            }

            if (var5.numSquares != 0) {
               SpriteRenderer.instance.drawWater(this.Effect, var3, this.apiId, false);
            }
         }
      }
   }

   public void waterProjection() {
      int var1 = SpriteRenderer.instance.getRenderingPlayerIndex();
      PlayerCamera var2 = SpriteRenderer.instance.getRenderingPlayerCamera(var1);
      GL11.glOrtho((double)var2.getOffX(), (double)(var2.getOffX() + (float)var2.OffscreenWidth), (double)(var2.getOffY() + (float)var2.OffscreenHeight), (double)var2.getOffY(), -1.0D, 1.0D);
   }

   public void waterGeometry(boolean var1) {
      long var2 = System.nanoTime();
      int var4 = SpriteRenderer.instance.getRenderStateIndex();
      int var5 = SpriteRenderer.instance.getRenderingPlayerIndex();
      IsoWater.RenderData var6 = var1 ? this.renderDataShore[var4][var5] : this.renderData[var4][var5];
      int var7 = 0;

      int var9;
      for(int var8 = var6.numSquares; var8 > 0; var8 -= var9) {
         var9 = this.renderSome(var7, var8, var1);
         var7 += var9;
      }

      long var11 = System.nanoTime();
      SpriteRenderer.ringBuffer.restoreVBOs = true;
   }

   private int renderSome(int var1, int var2, boolean var3) {
      IsoPuddles.VBOs.next();
      FloatBuffer var4 = IsoPuddles.VBOs.vertices;
      ShortBuffer var5 = IsoPuddles.VBOs.indices;
      GL13.glActiveTexture(33985);
      GL13.glClientActiveTexture(33985);
      GL11.glTexCoordPointer(2, 5126, 28, 8L);
      GL11.glEnableClientState(32888);
      GL13.glActiveTexture(33984);
      GL13.glClientActiveTexture(33984);
      GL11.glTexCoordPointer(2, 5126, 28, 0L);
      GL11.glColorPointer(4, 5121, 28, 24L);
      GL11.glVertexPointer(2, 5126, 28, 16L);
      int var6 = SpriteRenderer.instance.getRenderStateIndex();
      int var7 = SpriteRenderer.instance.getRenderingPlayerIndex();
      IsoWater.RenderData var8 = var3 ? this.renderDataShore[var6][var7] : this.renderData[var6][var7];
      int var9 = Math.min(var2 * 4, IsoPuddles.VBOs.bufferSizeVertices);
      var4.put(var8.data, var1 * 4 * 7, var9 * 7);
      int var10 = 0;
      int var11 = 0;

      for(int var12 = 0; var12 < var9 / 4; ++var12) {
         var5.put((short)var10);
         var5.put((short)(var10 + 1));
         var5.put((short)(var10 + 2));
         var5.put((short)var10);
         var5.put((short)(var10 + 2));
         var5.put((short)(var10 + 3));
         var10 += 4;
         var11 += 6;
      }

      IsoPuddles.VBOs.unmap();
      byte var16 = 0;
      byte var14 = 0;
      GL12.glDrawRangeElements(4, var16, var16 + var10, var11 - var14, 5123, (long)(var14 * 2));
      return var9 / 4;
   }

   public ITexture getTextureBottom() {
      return this.texBottom;
   }

   public float getShaderTime() {
      return this.WaterTime;
   }

   public float getRainIntensity() {
      return this.WaterRainIntensity;
   }

   public void update(ClimateManager var1) {
      this.WaterWindAngle = var1.getCorrectedWindAngleIntensity();
      this.WaterWindIntensity = var1.getWindIntensity() * 5.0F;
      this.WaterRainIntensity = var1.getRainIntensity();
      this.WaterTime += 0.0166F * GameTime.getInstance().getMultiplier();
      this.WaterParamWindINT.add((float)Math.sin((double)(this.WaterWindAngle * 6.0F)) * this.WaterWindIntensity * 0.05F, (float)Math.cos((double)(this.WaterWindAngle * 6.0F)) * this.WaterWindIntensity * 0.15F);
   }

   public float getWaterWindX() {
      return this.WaterParamWindINT.x;
   }

   public float getWaterWindY() {
      return this.WaterParamWindINT.y;
   }

   public float getWaterWindSpeed() {
      return this.WaterWindIntensity * 2.0F;
   }

   public Vector4f getShaderOffset() {
      int var1 = SpriteRenderer.instance.getRenderingPlayerIndex();
      PlayerCamera var2 = SpriteRenderer.instance.getRenderingPlayerCamera(var1);
      return this.shaderOffset.set(var2.getOffX() - (float)IsoCamera.getOffscreenLeft(var1) * var2.zoom, var2.getOffY() + (float)IsoCamera.getOffscreenTop(var1) * var2.zoom, (float)var2.OffscreenWidth, (float)var2.OffscreenHeight);
   }

   public void FBOStart() {
      int var1 = IsoCamera.frameState.playerIndex;
   }

   public void FBOEnd() {
      int var1 = IsoCamera.frameState.playerIndex;
   }

   private static final class RenderData {
      int numSquares;
      int capacity = 512;
      float[] data;

      void clear() {
         this.numSquares = 0;
      }

      void addSquare(IsoWaterGeometry var1) {
         int var2 = IsoCamera.frameState.playerIndex;
         byte var3 = 4;
         if (this.data == null) {
            this.data = new float[this.capacity * var3 * 7];
         }

         if (this.numSquares + 1 > this.capacity) {
            this.capacity += 128;
            this.data = Arrays.copyOf(this.data, this.capacity * var3 * 7);
         }

         int var4 = this.numSquares * var3 * 7;

         for(int var5 = 0; var5 < 4; ++var5) {
            this.data[var4++] = var1.depth[var5];
            this.data[var4++] = var1.flow[var5];
            this.data[var4++] = var1.speed[var5];
            this.data[var4++] = var1.IsExternal;
            this.data[var4++] = var1.x[var5];
            this.data[var4++] = var1.y[var5];
            if (var1.square != null) {
               int var6 = var1.square.getVertLight((4 - var5) % 4, var2);
               this.data[var4++] = Float.intBitsToFloat(var6);
            } else {
               ++var4;
            }
         }

         ++this.numSquares;
      }
   }
}

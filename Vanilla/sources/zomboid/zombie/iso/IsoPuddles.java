package zombie.iso;

import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import org.joml.Vector2f;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL13;
import org.lwjglx.BufferUtils;
import zombie.GameTime;
import zombie.core.Core;
import zombie.core.PerformanceSettings;
import zombie.core.SpriteRenderer;
import zombie.core.opengl.RenderThread;
import zombie.core.opengl.Shader;
import zombie.core.opengl.SharedVertexBufferObjects;
import zombie.core.textures.Texture;
import zombie.debug.DebugLog;
import zombie.debug.DebugOptions;
import zombie.interfaces.ITexture;
import zombie.iso.weather.ClimateManager;
import zombie.network.GameServer;

public final class IsoPuddles {
   public Shader Effect;
   private float PuddlesWindAngle;
   private float PuddlesWindIntensity;
   private float PuddlesTime;
   private final Vector2f PuddlesParamWindINT;
   public static boolean leakingPuddlesInTheRoom = false;
   private Texture texHM;
   private int apiId;
   private static IsoPuddles instance;
   private static boolean isShaderEnable = false;
   static final int BYTES_PER_FLOAT = 4;
   static final int FLOATS_PER_VERTEX = 7;
   static final int BYTES_PER_VERTEX = 28;
   static final int VERTICES_PER_SQUARE = 4;
   public static final SharedVertexBufferObjects VBOs = new SharedVertexBufferObjects(28);
   private final IsoPuddles.RenderData[][] renderData = new IsoPuddles.RenderData[3][4];
   private final Vector4f shaderOffset = new Vector4f();
   private final Vector4f shaderOffsetMain = new Vector4f();
   private FloatBuffer floatBuffer = BufferUtils.createFloatBuffer(16);
   public static final int BOOL_MAX = 0;
   public static final int FLOAT_RAIN = 0;
   public static final int FLOAT_WETGROUND = 1;
   public static final int FLOAT_MUDDYPUDDLES = 2;
   public static final int FLOAT_PUDDLESSIZE = 3;
   public static final int FLOAT_RAININTENSITY = 4;
   public static final int FLOAT_MAX = 5;
   private IsoPuddles.PuddlesFloat rain;
   private IsoPuddles.PuddlesFloat wetGround;
   private IsoPuddles.PuddlesFloat muddyPuddles;
   private IsoPuddles.PuddlesFloat puddlesSize;
   private IsoPuddles.PuddlesFloat rainIntensity;
   private final IsoPuddles.PuddlesFloat[] climateFloats = new IsoPuddles.PuddlesFloat[5];

   public static synchronized IsoPuddles getInstance() {
      if (instance == null) {
         instance = new IsoPuddles();
      }

      return instance;
   }

   public boolean getShaderEnable() {
      return isShaderEnable;
   }

   public IsoPuddles() {
      if (GameServer.bServer) {
         Core.getInstance().setPerfPuddles(3);
         this.applyPuddlesQuality();
         this.PuddlesParamWindINT = new Vector2f(0.0F);
         this.setup();
      } else {
         this.texHM = Texture.getSharedTexture("media/textures/puddles_hm.png");
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
         this.applyPuddlesQuality();
         this.PuddlesParamWindINT = new Vector2f(0.0F);

         for(int var1 = 0; var1 < this.renderData.length; ++var1) {
            for(int var2 = 0; var2 < 4; ++var2) {
               this.renderData[var1][var2] = new IsoPuddles.RenderData();
            }
         }

         this.setup();
      }
   }

   public void applyPuddlesQuality() {
      leakingPuddlesInTheRoom = Core.getInstance().getPerfPuddles() == 0;
      if (Core.getInstance().getPerfPuddles() == 3) {
         isShaderEnable = false;
      } else {
         isShaderEnable = true;
         if (PerformanceSettings.PuddlesQuality == 2) {
            RenderThread.invokeOnRenderContext(() -> {
               this.Effect = new PuddlesShader("puddles_lq");
               this.Effect.Start();
               this.Effect.End();
            });
         }

         if (PerformanceSettings.PuddlesQuality == 1) {
            RenderThread.invokeOnRenderContext(() -> {
               this.Effect = new PuddlesShader("puddles_mq");
               this.Effect.Start();
               this.Effect.End();
            });
         }

         if (PerformanceSettings.PuddlesQuality == 0) {
            RenderThread.invokeOnRenderContext(() -> {
               this.Effect = new PuddlesShader("puddles_hq");
               this.Effect.Start();
               this.Effect.End();
            });
         }
      }

   }

   public Vector4f getShaderOffset() {
      int var1 = SpriteRenderer.instance.getRenderingPlayerIndex();
      PlayerCamera var2 = SpriteRenderer.instance.getRenderingPlayerCamera(var1);
      return this.shaderOffset.set(var2.getOffX() - (float)IsoCamera.getOffscreenLeft(var1) * var2.zoom, var2.getOffY() + (float)IsoCamera.getOffscreenTop(var1) * var2.zoom, (float)var2.OffscreenWidth, (float)var2.OffscreenHeight);
   }

   public Vector4f getShaderOffsetMain() {
      int var1 = IsoCamera.frameState.playerIndex;
      PlayerCamera var2 = IsoCamera.cameras[var1];
      return this.shaderOffsetMain.set(var2.getOffX() - (float)IsoCamera.getOffscreenLeft(var1) * var2.zoom, var2.getOffY() + (float)IsoCamera.getOffscreenTop(var1) * var2.zoom, (float)IsoCamera.getOffscreenWidth(var1), (float)IsoCamera.getOffscreenHeight(var1));
   }

   public void render(ArrayList var1, int var2) {
      if (DebugOptions.instance.Weather.WaterPuddles.getValue()) {
         int var3 = SpriteRenderer.instance.getMainStateIndex();
         int var4 = IsoCamera.frameState.playerIndex;
         IsoPuddles.RenderData var5 = this.renderData[var3][var4];
         if (var2 == 0) {
            var5.clear();
         }

         if (!var1.isEmpty()) {
            if (this.getShaderEnable()) {
               if (Core.getInstance().getUseShaders()) {
                  if (Core.getInstance().getPerfPuddles() != 3) {
                     if (var2 <= 0 || Core.getInstance().getPerfPuddles() <= 0) {
                        if ((double)this.wetGround.getFinalValue() != 0.0D || (double)this.puddlesSize.getFinalValue() != 0.0D) {
                           for(int var6 = 0; var6 < var1.size(); ++var6) {
                              IsoPuddlesGeometry var7 = ((IsoGridSquare)var1.get(var6)).getPuddles();
                              if (var7 != null && var7.shouldRender()) {
                                 var7.updateLighting(var4);
                                 var5.addSquare(var2, var7);
                              }
                           }

                           if (var5.squaresPerLevel[var2] > 0) {
                              SpriteRenderer.instance.drawPuddles(this.Effect, var4, this.apiId, var2);
                           }
                        }
                     }
                  }
               }
            }
         }
      }
   }

   public void puddlesProjection() {
      int var1 = SpriteRenderer.instance.getRenderingPlayerIndex();
      PlayerCamera var2 = SpriteRenderer.instance.getRenderingPlayerCamera(var1);
      GL11.glOrtho((double)var2.getOffX(), (double)(var2.getOffX() + (float)var2.OffscreenWidth), (double)(var2.getOffY() + (float)var2.OffscreenHeight), (double)var2.getOffY(), -1.0D, 1.0D);
   }

   public void puddlesGeometry(int var1) {
      int var2 = SpriteRenderer.instance.getRenderStateIndex();
      int var3 = SpriteRenderer.instance.getRenderingPlayerIndex();
      IsoPuddles.RenderData var4 = this.renderData[var2][var3];
      int var5 = 0;

      int var6;
      for(var6 = 0; var6 < var1; ++var6) {
         var5 += var4.squaresPerLevel[var6];
      }

      int var7;
      for(var6 = var4.squaresPerLevel[var1]; var6 > 0; var6 -= var7) {
         var7 = this.renderSome(var5, var6);
         var5 += var7;
      }

      SpriteRenderer.ringBuffer.restoreVBOs = true;
   }

   private int renderSome(int var1, int var2) {
      VBOs.next();
      FloatBuffer var3 = VBOs.vertices;
      ShortBuffer var4 = VBOs.indices;
      GL13.glActiveTexture(33985);
      GL13.glClientActiveTexture(33985);
      GL11.glTexCoordPointer(2, 5126, 28, 8L);
      GL11.glEnableClientState(32888);
      GL13.glActiveTexture(33984);
      GL13.glClientActiveTexture(33984);
      GL11.glTexCoordPointer(2, 5126, 28, 0L);
      GL11.glColorPointer(4, 5121, 28, 24L);
      GL11.glVertexPointer(2, 5126, 28, 16L);
      int var5 = SpriteRenderer.instance.getRenderStateIndex();
      int var6 = SpriteRenderer.instance.getRenderingPlayerIndex();
      IsoPuddles.RenderData var7 = this.renderData[var5][var6];
      int var8 = Math.min(var2 * 4, VBOs.bufferSizeVertices);
      var3.put(var7.data, var1 * 4 * 7, var8 * 7);
      int var9 = 0;
      int var10 = 0;

      for(int var11 = 0; var11 < var8 / 4; ++var11) {
         var4.put((short)var9);
         var4.put((short)(var9 + 1));
         var4.put((short)(var9 + 2));
         var4.put((short)var9);
         var4.put((short)(var9 + 2));
         var4.put((short)(var9 + 3));
         var9 += 4;
         var10 += 6;
      }

      VBOs.unmap();
      byte var15 = 0;
      byte var13 = 0;
      GL12.glDrawRangeElements(4, var15, var15 + var9, var10 - var13, 5123, (long)(var13 * 2));
      return var8 / 4;
   }

   public void update(ClimateManager var1) {
      this.PuddlesWindAngle = var1.getCorrectedWindAngleIntensity();
      this.PuddlesWindIntensity = var1.getWindIntensity();
      this.rain.setFinalValue(var1.getRainIntensity());
      float var2 = GameTime.getInstance().getMultiplier() / 1.6F;
      float var3 = 2.0E-5F * var2 * var1.getTemperature();
      float var4 = 2.0E-5F * var2;
      float var5 = 2.0E-4F * var2;
      float var6 = this.rain.getFinalValue();
      var6 = var6 * var6 * 0.05F * var2;
      this.rainIntensity.setFinalValue(this.rain.getFinalValue() * 2.0F);
      this.wetGround.addFinalValue(var6);
      this.muddyPuddles.addFinalValue(var6 * 2.0F);
      this.puddlesSize.addFinalValueForMax(var6 * 0.01F, 0.7F);
      if ((double)var6 == 0.0D) {
         this.wetGround.addFinalValue(-var3);
         this.muddyPuddles.addFinalValue(-var5);
      }

      if ((double)this.wetGround.getFinalValue() == 0.0D) {
         this.puddlesSize.addFinalValue(-var4);
      }

      this.PuddlesTime += 0.0166F * GameTime.getInstance().getMultiplier();
      this.PuddlesParamWindINT.add((float)Math.sin((double)(this.PuddlesWindAngle * 6.0F)) * this.PuddlesWindIntensity * 0.05F, (float)Math.cos((double)(this.PuddlesWindAngle * 6.0F)) * this.PuddlesWindIntensity * 0.05F);
   }

   public float getShaderTime() {
      return this.PuddlesTime;
   }

   public float getPuddlesSize() {
      return this.puddlesSize.getFinalValue();
   }

   public ITexture getHMTexture() {
      return this.texHM;
   }

   public FloatBuffer getPuddlesParams(int var1) {
      this.floatBuffer.clear();
      this.floatBuffer.put(this.PuddlesParamWindINT.x);
      this.floatBuffer.put(this.muddyPuddles.getFinalValue());
      this.floatBuffer.put(0.0F);
      this.floatBuffer.put(0.0F);
      this.floatBuffer.put(this.PuddlesParamWindINT.y);
      this.floatBuffer.put(this.wetGround.getFinalValue());
      this.floatBuffer.put(0.0F);
      this.floatBuffer.put(0.0F);
      this.floatBuffer.put(this.PuddlesWindIntensity * 1.0F);
      this.floatBuffer.put(this.puddlesSize.getFinalValue());
      this.floatBuffer.put(0.0F);
      this.floatBuffer.put(0.0F);
      this.floatBuffer.put((float)var1);
      this.floatBuffer.put(this.rainIntensity.getFinalValue());
      this.floatBuffer.put(0.0F);
      this.floatBuffer.put(0.0F);
      this.floatBuffer.flip();
      return this.floatBuffer;
   }

   public float getRainIntensity() {
      return this.rainIntensity.getFinalValue();
   }

   public int getFloatMax() {
      return 5;
   }

   public int getBoolMax() {
      return 0;
   }

   public IsoPuddles.PuddlesFloat getPuddlesFloat(int var1) {
      if (var1 >= 0 && var1 < 5) {
         return this.climateFloats[var1];
      } else {
         DebugLog.log("ERROR: Climate: cannot get float override id.");
         return null;
      }
   }

   private IsoPuddles.PuddlesFloat initClimateFloat(int var1, String var2) {
      if (var1 >= 0 && var1 < 5) {
         return this.climateFloats[var1].init(var1, var2);
      } else {
         DebugLog.log("ERROR: Climate: cannot get float override id.");
         return null;
      }
   }

   private void setup() {
      for(int var1 = 0; var1 < this.climateFloats.length; ++var1) {
         this.climateFloats[var1] = new IsoPuddles.PuddlesFloat();
      }

      this.rain = this.initClimateFloat(0, "INPUT: RAIN");
      this.wetGround = this.initClimateFloat(1, "Wet Ground");
      this.muddyPuddles = this.initClimateFloat(2, "Muddy Puddles");
      this.puddlesSize = this.initClimateFloat(3, "Puddles Size");
      this.rainIntensity = this.initClimateFloat(4, "Rain Intensity");
   }

   private static final class RenderData {
      final int[] squaresPerLevel = new int[8];
      int numSquares;
      int capacity = 512;
      float[] data;

      RenderData() {
      }

      void clear() {
         this.numSquares = 0;
         Arrays.fill(this.squaresPerLevel, 0);
      }

      void addSquare(int var1, IsoPuddlesGeometry var2) {
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
            this.data[var4++] = var2.pdne[var5];
            this.data[var4++] = var2.pdnw[var5];
            this.data[var4++] = var2.pda[var5];
            this.data[var4++] = var2.pnon[var5];
            this.data[var4++] = var2.x[var5];
            this.data[var4++] = var2.y[var5];
            this.data[var4++] = Float.intBitsToFloat(var2.color[var5]);
         }

         ++this.numSquares;
         int var10002 = this.squaresPerLevel[var1]++;
      }
   }

   public static class PuddlesFloat {
      protected float finalValue;
      private boolean isAdminOverride = false;
      private float adminValue;
      private float min = 0.0F;
      private float max = 1.0F;
      private float delta = 0.01F;
      private int ID;
      private String name;

      public IsoPuddles.PuddlesFloat init(int var1, String var2) {
         this.ID = var1;
         this.name = var2;
         return this;
      }

      public int getID() {
         return this.ID;
      }

      public String getName() {
         return this.name;
      }

      public float getMin() {
         return this.min;
      }

      public float getMax() {
         return this.max;
      }

      public void setEnableAdmin(boolean var1) {
         this.isAdminOverride = var1;
      }

      public boolean isEnableAdmin() {
         return this.isAdminOverride;
      }

      public void setAdminValue(float var1) {
         this.adminValue = Math.max(this.min, Math.min(this.max, var1));
      }

      public float getAdminValue() {
         return this.adminValue;
      }

      public void setFinalValue(float var1) {
         this.finalValue = Math.max(this.min, Math.min(this.max, var1));
      }

      public void addFinalValue(float var1) {
         this.finalValue = Math.max(this.min, Math.min(this.max, this.finalValue + var1));
      }

      public void addFinalValueForMax(float var1, float var2) {
         this.finalValue = Math.max(this.min, Math.min(var2, this.finalValue + var1));
      }

      public float getFinalValue() {
         return this.isAdminOverride ? this.adminValue : this.finalValue;
      }

      public void interpolateFinalValue(float var1) {
         if (Math.abs(this.finalValue - var1) < this.delta) {
            this.finalValue = var1;
         } else if (var1 > this.finalValue) {
            this.finalValue += this.delta;
         } else {
            this.finalValue -= this.delta;
         }

      }

      private void calculate() {
         if (this.isAdminOverride) {
            this.finalValue = this.adminValue;
         }
      }
   }
}

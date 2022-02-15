package zombie.core.skinnedmodel.shader;

import java.nio.FloatBuffer;
import org.joml.Math;
import org.joml.Vector4f;
import org.lwjgl.opengl.ARBShaderObjects;
import org.lwjgl.opengl.GL20;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjglx.BufferUtils;
import zombie.characters.IsoGameCharacter;
import zombie.characters.IsoPlayer;
import zombie.core.math.PZMath;
import zombie.core.opengl.PZGLUtil;
import zombie.core.opengl.ShaderProgram;
import zombie.core.skinnedmodel.model.ModelInstance;
import zombie.core.skinnedmodel.model.ModelInstanceRenderData;
import zombie.core.skinnedmodel.model.ModelSlotRenderData;
import zombie.core.textures.SmartTexture;
import zombie.core.textures.Texture;
import zombie.debug.DebugOptions;
import zombie.iso.IsoMovingObject;
import zombie.network.GameServer;
import zombie.network.ServerGUI;
import zombie.vehicles.BaseVehicle;

public final class Shader {
   private int HueChange;
   private int LightingAmount;
   private int MirrorXID;
   private int TransformMatrixID = 0;
   final String name;
   private final ShaderProgram m_shaderProgram;
   private int MatrixID = 0;
   private int Light0Direction;
   private int Light0Colour;
   private int Light1Direction;
   private int Light1Colour;
   private int Light2Direction;
   private int Light2Colour;
   private int Light3Direction;
   private int Light3Colour;
   private int Light4Direction;
   private int Light4Colour;
   private int TintColour;
   private int Texture0;
   private int TexturePainColor;
   private int TextureRust;
   private int TextureRustA;
   private int TextureMask;
   private int TextureLights;
   private int TextureDamage1Overlay;
   private int TextureDamage1Shell;
   private int TextureDamage2Overlay;
   private int TextureDamage2Shell;
   private int TextureUninstall1;
   private int TextureUninstall2;
   private int TextureLightsEnables1;
   private int TextureLightsEnables2;
   private int TextureDamage1Enables1;
   private int TextureDamage1Enables2;
   private int TextureDamage2Enables1;
   private int TextureDamage2Enables2;
   private int MatBlood1Enables1;
   private int MatBlood1Enables2;
   private int MatBlood2Enables1;
   private int MatBlood2Enables2;
   private int Alpha;
   private int TextureReflectionA;
   private int TextureReflectionB;
   private int ReflectionParam;
   public int BoneIndicesAttrib;
   public int BoneWeightsAttrib;
   private int UVScale;
   final boolean bStatic;
   private static FloatBuffer floatBuffer;
   private static final int MAX_BONES = 64;
   private static final Vector3f tempVec3f = new Vector3f();
   private final FloatBuffer floatBuffer2 = BufferUtils.createFloatBuffer(16);

   public Shader(String var1, boolean var2) {
      this.name = var1;
      this.m_shaderProgram = ShaderProgram.createShaderProgram(var1, var2, false);
      this.m_shaderProgram.addCompileListener(this::onProgramCompiled);
      this.bStatic = var2;
      this.compile();
   }

   public boolean isStatic() {
      return this.bStatic;
   }

   public ShaderProgram getShaderProgram() {
      return this.m_shaderProgram;
   }

   private void onProgramCompiled(ShaderProgram var1) {
      this.Start();
      int var2 = this.m_shaderProgram.getShaderID();
      if (!this.bStatic) {
         this.MatrixID = ARBShaderObjects.glGetUniformLocationARB(var2, "MatrixPalette");
      } else {
         this.TransformMatrixID = ARBShaderObjects.glGetUniformLocationARB(var2, "transform");
      }

      this.HueChange = ARBShaderObjects.glGetUniformLocationARB(var2, "HueChange");
      this.LightingAmount = ARBShaderObjects.glGetUniformLocationARB(var2, "LightingAmount");
      this.Light0Colour = ARBShaderObjects.glGetUniformLocationARB(var2, "Light0Colour");
      this.Light0Direction = ARBShaderObjects.glGetUniformLocationARB(var2, "Light0Direction");
      this.Light1Colour = ARBShaderObjects.glGetUniformLocationARB(var2, "Light1Colour");
      this.Light1Direction = ARBShaderObjects.glGetUniformLocationARB(var2, "Light1Direction");
      this.Light2Colour = ARBShaderObjects.glGetUniformLocationARB(var2, "Light2Colour");
      this.Light2Direction = ARBShaderObjects.glGetUniformLocationARB(var2, "Light2Direction");
      this.Light3Colour = ARBShaderObjects.glGetUniformLocationARB(var2, "Light3Colour");
      this.Light3Direction = ARBShaderObjects.glGetUniformLocationARB(var2, "Light3Direction");
      this.Light4Colour = ARBShaderObjects.glGetUniformLocationARB(var2, "Light4Colour");
      this.Light4Direction = ARBShaderObjects.glGetUniformLocationARB(var2, "Light4Direction");
      this.TintColour = ARBShaderObjects.glGetUniformLocationARB(var2, "TintColour");
      this.Texture0 = ARBShaderObjects.glGetUniformLocationARB(var2, "Texture0");
      this.TexturePainColor = ARBShaderObjects.glGetUniformLocationARB(var2, "TexturePainColor");
      this.TextureRust = ARBShaderObjects.glGetUniformLocationARB(var2, "TextureRust");
      this.TextureMask = ARBShaderObjects.glGetUniformLocationARB(var2, "TextureMask");
      this.TextureLights = ARBShaderObjects.glGetUniformLocationARB(var2, "TextureLights");
      this.TextureDamage1Overlay = ARBShaderObjects.glGetUniformLocationARB(var2, "TextureDamage1Overlay");
      this.TextureDamage1Shell = ARBShaderObjects.glGetUniformLocationARB(var2, "TextureDamage1Shell");
      this.TextureDamage2Overlay = ARBShaderObjects.glGetUniformLocationARB(var2, "TextureDamage2Overlay");
      this.TextureDamage2Shell = ARBShaderObjects.glGetUniformLocationARB(var2, "TextureDamage2Shell");
      this.TextureRustA = ARBShaderObjects.glGetUniformLocationARB(var2, "TextureRustA");
      this.TextureUninstall1 = ARBShaderObjects.glGetUniformLocationARB(var2, "TextureUninstall1");
      this.TextureUninstall2 = ARBShaderObjects.glGetUniformLocationARB(var2, "TextureUninstall2");
      this.TextureLightsEnables1 = ARBShaderObjects.glGetUniformLocationARB(var2, "TextureLightsEnables1");
      this.TextureLightsEnables2 = ARBShaderObjects.glGetUniformLocationARB(var2, "TextureLightsEnables2");
      this.TextureDamage1Enables1 = ARBShaderObjects.glGetUniformLocationARB(var2, "TextureDamage1Enables1");
      this.TextureDamage1Enables2 = ARBShaderObjects.glGetUniformLocationARB(var2, "TextureDamage1Enables2");
      this.TextureDamage2Enables1 = ARBShaderObjects.glGetUniformLocationARB(var2, "TextureDamage2Enables1");
      this.TextureDamage2Enables2 = ARBShaderObjects.glGetUniformLocationARB(var2, "TextureDamage2Enables2");
      this.MatBlood1Enables1 = ARBShaderObjects.glGetUniformLocationARB(var2, "MatBlood1Enables1");
      this.MatBlood1Enables2 = ARBShaderObjects.glGetUniformLocationARB(var2, "MatBlood1Enables2");
      this.MatBlood2Enables1 = ARBShaderObjects.glGetUniformLocationARB(var2, "MatBlood2Enables1");
      this.MatBlood2Enables2 = ARBShaderObjects.glGetUniformLocationARB(var2, "MatBlood2Enables2");
      this.Alpha = ARBShaderObjects.glGetUniformLocationARB(var2, "Alpha");
      this.TextureReflectionA = ARBShaderObjects.glGetUniformLocationARB(var2, "TextureReflectionA");
      this.TextureReflectionB = ARBShaderObjects.glGetUniformLocationARB(var2, "TextureReflectionB");
      this.ReflectionParam = ARBShaderObjects.glGetUniformLocationARB(var2, "ReflectionParam");
      this.UVScale = ARBShaderObjects.glGetUniformLocationARB(var2, "UVScale");
      this.m_shaderProgram.setSamplerUnit("Texture", 0);
      if (this.Texture0 != -1) {
         ARBShaderObjects.glUniform1iARB(this.Texture0, 0);
      }

      if (this.TextureRust != -1) {
         ARBShaderObjects.glUniform1iARB(this.TextureRust, 1);
      }

      if (this.TextureMask != -1) {
         ARBShaderObjects.glUniform1iARB(this.TextureMask, 2);
      }

      if (this.TextureLights != -1) {
         ARBShaderObjects.glUniform1iARB(this.TextureLights, 3);
      }

      if (this.TextureDamage1Overlay != -1) {
         ARBShaderObjects.glUniform1iARB(this.TextureDamage1Overlay, 4);
      }

      if (this.TextureDamage1Shell != -1) {
         ARBShaderObjects.glUniform1iARB(this.TextureDamage1Shell, 5);
      }

      if (this.TextureDamage2Overlay != -1) {
         ARBShaderObjects.glUniform1iARB(this.TextureDamage2Overlay, 6);
      }

      if (this.TextureDamage2Shell != -1) {
         ARBShaderObjects.glUniform1iARB(this.TextureDamage2Shell, 7);
      }

      if (this.TextureReflectionA != -1) {
         ARBShaderObjects.glUniform1iARB(this.TextureReflectionA, 8);
      }

      if (this.TextureReflectionB != -1) {
         ARBShaderObjects.glUniform1iARB(this.TextureReflectionB, 9);
      }

      this.MirrorXID = ARBShaderObjects.glGetUniformLocationARB(var2, "MirrorX");
      this.BoneIndicesAttrib = GL20.glGetAttribLocation(var2, "boneIndices");
      this.BoneWeightsAttrib = GL20.glGetAttribLocation(var2, "boneWeights");
      this.End();
   }

   private void compile() {
      this.m_shaderProgram.compile();
   }

   public void setTexture(Texture var1, String var2, int var3) {
      this.m_shaderProgram.setValue(var2, var1, var3);
   }

   private void setUVScale(float var1, float var2) {
      if (this.UVScale > 0) {
         this.m_shaderProgram.setVector2(this.UVScale, var1, var2);
      }

   }

   public int getID() {
      return this.m_shaderProgram.getShaderID();
   }

   public void Start() {
      this.m_shaderProgram.Start();
   }

   public void End() {
      this.m_shaderProgram.End();
   }

   public void startCharacter(ModelSlotRenderData var1, ModelInstanceRenderData var2) {
      if (this.bStatic) {
         this.setTransformMatrix(var2.xfrm, true);
      } else {
         this.setMatrixPalette(var2.matrixPalette);
      }

      float var3 = var1.ambientR * 0.45F;
      float var4 = var1.ambientG * 0.45F;
      float var5 = var1.ambientB * 0.45F;
      this.setLights(var1, 5);
      Texture var6 = var2.tex != null ? var2.tex : var2.model.tex;
      if (DebugOptions.instance.IsoSprite.CharacterMipmapColors.getValue()) {
         Texture var7 = var6 instanceof SmartTexture ? ((SmartTexture)var6).result : var6;
         if (var7 != null && var7.getTextureId() != null && var7.getTextureId().hasMipMaps()) {
            var6 = Texture.getEngineMipmapTexture();
         }
      }

      this.setTexture(var6, "Texture", 0);
      this.setDepthBias(var2.depthBias / 50.0F);
      this.setAmbient(var3, var4, var5);
      this.setLightingAmount(1.0F);
      this.setHueShift(var2.hue);
      this.setTint(var2.tintR, var2.tintG, var2.tintB);
      this.setAlpha(var1.alpha);
   }

   private void setLights(ModelSlotRenderData var1, int var2) {
      for(int var3 = 0; var3 < var2; ++var3) {
         ModelInstance.EffectLight var4 = var1.effectLights[var3];
         if (GameServer.bServer && ServerGUI.isCreated()) {
            var4.r = var4.g = var4.b = 1.0F;
         }

         this.setLight(var3, var4.x, var4.y, var4.z, var4.r, var4.g, var4.b, (float)var4.radius, var1.animPlayerAngle, var1.x, var1.y, var1.z, var1.object);
      }

   }

   public void updateAlpha(IsoGameCharacter var1, int var2) {
      if (var1 != null) {
         this.setAlpha(var1.getAlpha(var2));
      }

   }

   public void setAlpha(float var1) {
      ARBShaderObjects.glUniform1fARB(this.Alpha, var1);
   }

   public void updateParams() {
   }

   public void setMatrixPalette(Matrix4f[] var1) {
      if (!this.bStatic) {
         if (floatBuffer == null) {
            floatBuffer = BufferUtils.createFloatBuffer(1024);
         }

         floatBuffer.clear();
         Matrix4f[] var2 = var1;
         int var3 = var1.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            Matrix4f var5 = var2[var4];
            var5.store(floatBuffer);
         }

         floatBuffer.flip();
         ARBShaderObjects.glUniformMatrix4fvARB(this.MatrixID, true, floatBuffer);
      }
   }

   public void setMatrixPalette(FloatBuffer var1) {
      this.setMatrixPalette(var1, true);
   }

   public void setMatrixPalette(FloatBuffer var1, boolean var2) {
      if (!this.bStatic) {
         ARBShaderObjects.glUniformMatrix4fvARB(this.MatrixID, var2, var1);
      }
   }

   public void setMatrixPalette(org.joml.Matrix4f[] var1) {
      if (!this.bStatic) {
         if (floatBuffer == null) {
            floatBuffer = BufferUtils.createFloatBuffer(1024);
         }

         floatBuffer.clear();
         org.joml.Matrix4f[] var2 = var1;
         int var3 = var1.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            org.joml.Matrix4f var5 = var2[var4];
            var5.get(floatBuffer);
            floatBuffer.position(floatBuffer.position() + 16);
         }

         floatBuffer.flip();
         ARBShaderObjects.glUniformMatrix4fvARB(this.MatrixID, true, floatBuffer);
      }
   }

   public void setTint(float var1, float var2, float var3) {
      ARBShaderObjects.glUniform3fARB(this.TintColour, var1, var2, var3);
   }

   public void setTextureRustA(float var1) {
      ARBShaderObjects.glUniform1fARB(this.TextureRustA, var1);
   }

   public void setTexturePainColor(float var1, float var2, float var3, float var4) {
      ARBShaderObjects.glUniform4fARB(this.TexturePainColor, var1, var2, var3, var4);
   }

   public void setTexturePainColor(org.joml.Vector3f var1, float var2) {
      ARBShaderObjects.glUniform4fARB(this.TexturePainColor, var1.x(), var1.y(), var1.z(), var2);
   }

   public void setTexturePainColor(Vector4f var1) {
      ARBShaderObjects.glUniform4fARB(this.TexturePainColor, var1.x(), var1.y(), var1.z(), var1.w());
   }

   public void setReflectionParam(float var1, float var2, float var3) {
      ARBShaderObjects.glUniform3fARB(this.ReflectionParam, var1, var2, var3);
   }

   public void setTextureUninstall1(float[] var1) {
      this.setMatrix(this.TextureUninstall1, var1);
   }

   public void setTextureUninstall2(float[] var1) {
      this.setMatrix(this.TextureUninstall2, var1);
   }

   public void setTextureLightsEnables1(float[] var1) {
      this.setMatrix(this.TextureLightsEnables1, var1);
   }

   public void setTextureLightsEnables2(float[] var1) {
      this.setMatrix(this.TextureLightsEnables2, var1);
   }

   public void setTextureDamage1Enables1(float[] var1) {
      this.setMatrix(this.TextureDamage1Enables1, var1);
   }

   public void setTextureDamage1Enables2(float[] var1) {
      this.setMatrix(this.TextureDamage1Enables2, var1);
   }

   public void setTextureDamage2Enables1(float[] var1) {
      this.setMatrix(this.TextureDamage2Enables1, var1);
   }

   public void setTextureDamage2Enables2(float[] var1) {
      this.setMatrix(this.TextureDamage2Enables2, var1);
   }

   public void setMatrixBlood1(float[] var1, float[] var2) {
      if (this.MatBlood1Enables1 != -1 && this.MatBlood1Enables2 != -1) {
         this.setMatrix(this.MatBlood1Enables1, var1);
         this.setMatrix(this.MatBlood1Enables2, var2);
      }
   }

   public void setMatrixBlood2(float[] var1, float[] var2) {
      if (this.MatBlood2Enables1 != -1 && this.MatBlood2Enables2 != -1) {
         this.setMatrix(this.MatBlood2Enables1, var1);
         this.setMatrix(this.MatBlood2Enables2, var2);
      }
   }

   public void setShaderAlpha(float var1) {
      ARBShaderObjects.glUniform1fARB(this.Alpha, var1);
   }

   public void setLight(int var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8, float var9, ModelInstance var10) {
      float var11 = 0.0F;
      float var12 = 0.0F;
      float var13 = 0.0F;
      IsoMovingObject var14 = var10.object;
      if (var14 != null) {
         var11 = var14.x;
         var12 = var14.y;
         var13 = var14.z;
      }

      this.setLight(var1, var2, var3, var4, var5, var6, var7, var8, var9, var11, var12, var13, var14);
   }

   public void setLight(int var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8, float var9, float var10, float var11, float var12, IsoMovingObject var13) {
      PZGLUtil.checkGLError(true);
      int var14 = this.Light0Direction;
      int var15 = this.Light0Colour;
      if (var1 == 1) {
         var14 = this.Light1Direction;
         var15 = this.Light1Colour;
      }

      if (var1 == 2) {
         var14 = this.Light2Direction;
         var15 = this.Light2Colour;
      }

      if (var1 == 3) {
         var14 = this.Light3Direction;
         var15 = this.Light3Colour;
      }

      if (var1 == 4) {
         var14 = this.Light4Direction;
         var15 = this.Light4Colour;
      }

      if (var5 + var6 + var7 != 0.0F && !(var8 <= 0.0F)) {
         Vector3f var16 = tempVec3f;
         if (!Float.isNaN(var9)) {
            var16.set(var2, var3, var4);
            var16.x -= var10;
            var16.y -= var11;
            var16.z -= var12;
         } else {
            var16.set(var2, var3, var4);
         }

         float var17 = var16.length();
         if (var17 < 1.0E-4F) {
            var16.set(0.0F, 0.0F, 1.0F);
         } else {
            var16.normalise();
         }

         float var18;
         float var19;
         if (!Float.isNaN(var9)) {
            var18 = -var9;
            var19 = var16.x;
            float var20 = var16.y;
            var16.x = var19 * Math.cos(var18) - var20 * Math.sin(var18);
            var16.y = var19 * Math.sin(var18) + var20 * Math.cos(var18);
         }

         var18 = var16.y;
         var16.y = var16.z;
         var16.z = var18;
         if (var16.length() < 1.0E-4F) {
            var16.set(0.0F, 1.0F, 0.0F);
         }

         var16.normalise();
         var19 = 1.0F - var17 / var8;
         if (var19 < 0.0F) {
            var19 = 0.0F;
         }

         if (var19 > 1.0F) {
            var19 = 1.0F;
         }

         var5 *= var19;
         var6 *= var19;
         var7 *= var19;
         var5 = PZMath.clamp(var5, 0.0F, 1.0F);
         var6 = PZMath.clamp(var6, 0.0F, 1.0F);
         var7 = PZMath.clamp(var7, 0.0F, 1.0F);
         if (var13 instanceof BaseVehicle) {
            this.doVector3(var14, -var16.x, var16.y, var16.z);
         } else {
            this.doVector3(var14, -var16.x, var16.y, var16.z);
         }

         if (var13 instanceof IsoPlayer) {
            boolean var21 = false;
         }

         this.doVector3(var15, var5, var6, var7);
         PZGLUtil.checkGLErrorThrow("Shader.setLightInternal.");
      } else {
         this.doVector3(var14, 0.0F, 1.0F, 0.0F);
         this.doVector3(var15, 0.0F, 0.0F, 0.0F);
      }
   }

   private void doVector3(int var1, float var2, float var3, float var4) {
      this.m_shaderProgram.setVector3(var1, var2, var3, var4);
   }

   public void setHueShift(float var1) {
      if (this.HueChange > 0) {
         this.m_shaderProgram.setValue("HueChange", var1);
      }

   }

   public void setLightingAmount(float var1) {
      if (this.LightingAmount > 0) {
         this.m_shaderProgram.setValue("LightingAmount", var1);
      }

   }

   public void setDepthBias(float var1) {
      this.m_shaderProgram.setValue("DepthBias", var1 / 300.0F);
   }

   public void setAmbient(float var1) {
      this.m_shaderProgram.setVector3("AmbientColour", var1, var1, var1);
   }

   public void setAmbient(float var1, float var2, float var3) {
      this.m_shaderProgram.setVector3("AmbientColour", var1, var2, var3);
   }

   public void setTransformMatrix(Matrix4f var1, boolean var2) {
      if (floatBuffer == null) {
         floatBuffer = BufferUtils.createFloatBuffer(1024);
      }

      floatBuffer.clear();
      var1.store(floatBuffer);
      floatBuffer.flip();
      ARBShaderObjects.glUniformMatrix4fvARB(this.TransformMatrixID, var2, floatBuffer);
   }

   public void setTransformMatrix(org.joml.Matrix4f var1, boolean var2) {
      this.floatBuffer2.clear();
      var1.get(this.floatBuffer2);
      this.floatBuffer2.position(16);
      this.floatBuffer2.flip();
      ARBShaderObjects.glUniformMatrix4fvARB(this.TransformMatrixID, var2, this.floatBuffer2);
   }

   public void setMatrix(int var1, org.joml.Matrix4f var2) {
      this.floatBuffer2.clear();
      var2.get(this.floatBuffer2);
      this.floatBuffer2.position(16);
      this.floatBuffer2.flip();
      ARBShaderObjects.glUniformMatrix4fvARB(var1, true, this.floatBuffer2);
   }

   public void setMatrix(int var1, float[] var2) {
      this.floatBuffer2.clear();
      this.floatBuffer2.put(var2);
      this.floatBuffer2.flip();
      ARBShaderObjects.glUniformMatrix4fvARB(var1, true, this.floatBuffer2);
   }

   public boolean isVehicleShader() {
      return this.TextureRust != -1;
   }
}

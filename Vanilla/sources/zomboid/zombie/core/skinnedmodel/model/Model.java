package zombie.core.skinnedmodel.model;

import java.nio.FloatBuffer;
import org.joml.Matrix4f;
import org.joml.Matrix4fc;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjglx.BufferUtils;
import zombie.GameProfiler;
import zombie.asset.Asset;
import zombie.asset.AssetManager;
import zombie.asset.AssetPath;
import zombie.asset.AssetType;
import zombie.characters.IsoGameCharacter;
import zombie.characters.IsoPlayer;
import zombie.core.Color;
import zombie.core.Core;
import zombie.core.opengl.PZGLUtil;
import zombie.core.opengl.RenderThread;
import zombie.core.particle.MuzzleFlash;
import zombie.core.skinnedmodel.ModelCamera;
import zombie.core.skinnedmodel.ModelManager;
import zombie.core.skinnedmodel.animation.AnimationPlayer;
import zombie.core.skinnedmodel.shader.Shader;
import zombie.core.skinnedmodel.shader.ShaderManager;
import zombie.core.textures.Texture;
import zombie.debug.DebugOptions;
import zombie.input.GameKeyboard;
import zombie.iso.IsoLightSource;
import zombie.iso.Vector3;
import zombie.iso.sprite.SkyBox;
import zombie.scripting.objects.ModelAttachment;
import zombie.util.Lambda;
import zombie.util.StringUtils;
import zombie.util.Type;
import zombie.vehicles.BaseVehicle;

public final class Model extends Asset {
   public String Name;
   public final ModelMesh Mesh;
   public Shader Effect;
   public Object Tag;
   public boolean bStatic = false;
   public Texture tex = null;
   public SoftwareModelMesh softwareMesh;
   public static final FloatBuffer m_staticReusableFloatBuffer = BufferUtils.createFloatBuffer(128);
   private static final Matrix4f IDENTITY = new Matrix4f();
   public static final Color[] debugDrawColours = new Color[]{new Color(230, 25, 75), new Color(60, 180, 75), new Color(255, 225, 25), new Color(0, 130, 200), new Color(245, 130, 48), new Color(145, 30, 180), new Color(70, 240, 240), new Color(240, 50, 230), new Color(210, 245, 60), new Color(250, 190, 190), new Color(0, 128, 128), new Color(230, 190, 255), new Color(170, 110, 40), new Color(255, 250, 200), new Color(128, 0, 0), new Color(170, 255, 195), new Color(128, 128, 0), new Color(255, 215, 180), new Color(0, 0, 128), new Color(128, 128, 128), new Color(255, 255, 255), new Color(0, 0, 0)};
   public Model.ModelAssetParams assetParams;
   static Vector3 tempo = new Vector3();
   public static final AssetType ASSET_TYPE = new AssetType("Model");

   public Model(AssetPath var1, AssetManager var2, Model.ModelAssetParams var3) {
      super(var1, var2);
      this.assetParams = var3;
      this.bStatic = this.assetParams != null && this.assetParams.bStatic;
      ModelMesh.MeshAssetParams var4 = new ModelMesh.MeshAssetParams();
      var4.bStatic = this.bStatic;
      var4.animationsMesh = this.assetParams == null ? null : this.assetParams.animationsModel;
      this.Mesh = (ModelMesh)MeshAssetManager.instance.load(new AssetPath(var3.meshName), var4);
      if (!StringUtils.isNullOrWhitespace(var3.textureName)) {
         if (var3.textureName.contains("media/")) {
            this.tex = Texture.getSharedTexture(var3.textureName, var3.textureFlags);
         } else {
            this.tex = Texture.getSharedTexture("media/textures/" + var3.textureName + ".png", var3.textureFlags);
         }
      }

      if (!StringUtils.isNullOrWhitespace(var3.shaderName)) {
         this.CreateShader(var3.shaderName);
      }

      this.onCreated(this.Mesh.getState());
      this.addDependency(this.Mesh);
      if (this.isReady()) {
         this.Tag = this.Mesh.skinningData;
         this.softwareMesh = this.Mesh.softwareMesh;
         this.assetParams = null;
      }

   }

   public static void VectorToWorldCoords(IsoGameCharacter var0, Vector3 var1) {
      AnimationPlayer var2 = var0.getAnimationPlayer();
      float var3 = var2.getRenderedAngle();
      var1.x = -var1.x;
      var1.rotatey(var3);
      float var4 = var1.y;
      var1.y = var1.z;
      var1.z = var4 * 0.6F;
      var1.x *= 1.5F;
      var1.y *= 1.5F;
      var1.x += var0.x;
      var1.y += var0.y;
      var1.z += var0.z;
   }

   public static void BoneToWorldCoords(IsoGameCharacter var0, int var1, Vector3 var2) {
      AnimationPlayer var3 = var0.getAnimationPlayer();
      var2.x = var3.modelTransforms[var1].m03;
      var2.y = var3.modelTransforms[var1].m13;
      var2.z = var3.modelTransforms[var1].m23;
      VectorToWorldCoords(var0, var2);
   }

   public static void BoneYDirectionToWorldCoords(IsoGameCharacter var0, int var1, Vector3 var2, float var3) {
      AnimationPlayer var4 = var0.getAnimationPlayer();
      var2.x = var4.modelTransforms[var1].m01 * var3;
      var2.y = var4.modelTransforms[var1].m11 * var3;
      var2.z = var4.modelTransforms[var1].m21 * var3;
      var2.x += var4.modelTransforms[var1].m03;
      var2.y += var4.modelTransforms[var1].m13;
      var2.z += var4.modelTransforms[var1].m23;
      VectorToWorldCoords(var0, var2);
   }

   public static void VectorToWorldCoords(ModelSlotRenderData var0, Vector3 var1) {
      float var2 = var0.animPlayerAngle;
      var1.x = -var1.x;
      var1.rotatey(var2);
      float var3 = var1.y;
      var1.y = var1.z;
      var1.z = var3 * 0.6F;
      var1.x *= 1.5F;
      var1.y *= 1.5F;
      var1.x += var0.x;
      var1.y += var0.y;
      var1.z += var0.z;
   }

   public static void BoneToWorldCoords(ModelSlotRenderData var0, int var1, Vector3 var2) {
      AnimationPlayer var3 = var0.animPlayer;
      var2.x = var3.modelTransforms[var1].m03;
      var2.y = var3.modelTransforms[var1].m13;
      var2.z = var3.modelTransforms[var1].m23;
      VectorToWorldCoords(var0, var2);
   }

   public static void CharacterModelCameraBegin(ModelSlotRenderData var0) {
      ModelCamera.instance.Begin();
      if (var0.bInVehicle) {
         GL11.glMatrixMode(5888);
         GL11.glTranslatef(0.0F, var0.centerOfMassY, 0.0F);
         GL11.glMatrixMode(5888);
         GL11.glRotatef(var0.vehicleAngleZ, 0.0F, 0.0F, 1.0F);
         GL11.glRotatef(var0.vehicleAngleY, 0.0F, 1.0F, 0.0F);
         GL11.glRotatef(var0.vehicleAngleX, 1.0F, 0.0F, 0.0F);
         GL11.glRotatef(180.0F, 0.0F, 1.0F, 0.0F);
         byte var1 = -1;
         GL11.glTranslatef(var0.inVehicleX, var0.inVehicleY, var0.inVehicleZ * (float)var1);
         GL11.glScalef(1.5F, 1.5F, 1.5F);
      }

   }

   public static void CharacterModelCameraEnd() {
      ModelCamera.instance.End();
   }

   public void DrawChar(ModelSlotRenderData var1, ModelInstanceRenderData var2) {
      if (!DebugOptions.instance.Character.Debug.Render.SkipCharacters.getValue()) {
         if (var1.character == IsoPlayer.getInstance()) {
            boolean var3 = false;
         }

         if (!(var1.alpha < 0.01F)) {
            if (var1.animPlayer != null) {
               if (Core.bDebug && GameKeyboard.isKeyDown(199)) {
                  this.Effect = null;
               }

               if (this.Effect == null) {
                  this.CreateShader("basicEffect");
               }

               Shader var5 = this.Effect;
               GL11.glEnable(2884);
               GL11.glCullFace(1028);
               GL11.glEnable(2929);
               GL11.glEnable(3008);
               GL11.glDepthFunc(513);
               GL11.glAlphaFunc(516, 0.01F);
               GL11.glBlendFunc(770, 771);
               if (Core.bDebug && DebugOptions.instance.ModelRenderWireframe.getValue()) {
                  GL11.glPolygonMode(1032, 6913);
                  GL11.glEnable(2848);
                  GL11.glLineWidth(0.75F);
                  Shader var4 = ShaderManager.instance.getOrCreateShader("vehicle_wireframe", this.bStatic);
                  if (var4 != null) {
                     var4.Start();
                     if (this.bStatic) {
                        var4.setTransformMatrix(var2.xfrm, true);
                     } else {
                        var4.setMatrixPalette(var2.matrixPalette, true);
                     }

                     this.Mesh.Draw(var4);
                     var4.End();
                  }

                  GL11.glPolygonMode(1032, 6914);
                  GL11.glDisable(2848);
               } else {
                  if (var5 != null) {
                     var5.Start();
                     var5.startCharacter(var1, var2);
                  }

                  if (!DebugOptions.instance.DebugDraw_SkipDrawNonSkinnedModel.getValue()) {
                     GameProfiler.getInstance().invokeAndMeasure("Mesh.Draw.Call", var5, this.Mesh, (var0, var1x) -> {
                        var1x.Draw(var0);
                     });
                  }

                  if (var5 != null) {
                     var5.End();
                  }

                  this.drawMuzzleFlash(var2);
               }
            }
         }
      }
   }

   private void drawMuzzleFlash(ModelInstanceRenderData var1) {
      if (var1.m_muzzleFlash) {
         ModelAttachment var2 = var1.modelInstance.getAttachmentById("muzzle");
         if (var2 != null) {
            BaseVehicle.Matrix4fObjectPool var3 = (BaseVehicle.Matrix4fObjectPool)BaseVehicle.TL_matrix4f_pool.get();
            Matrix4f var4 = ((Matrix4f)var3.alloc()).set((Matrix4fc)var1.xfrm);
            var4.transpose();
            Matrix4f var5 = var1.modelInstance.getAttachmentMatrix(var2, (Matrix4f)var3.alloc());
            var4.mul((Matrix4fc)var5, var5);
            MuzzleFlash.render(var5);
            var3.release(var4);
            var3.release(var5);
         }
      }

   }

   private void drawVehicleLights(ModelSlotRenderData var1) {
      int var2;
      for(var2 = 7; var2 >= 0; --var2) {
         GL13.glActiveTexture('蓀' + var2);
         GL11.glDisable(3553);
      }

      GL11.glLineWidth(1.0F);
      GL11.glColor3f(1.0F, 1.0F, 0.0F);
      GL11.glDisable(2929);

      for(var2 = 0; var2 < 3; ++var2) {
         ModelInstance.EffectLight var3 = var1.effectLights[var2];
         if (!((float)var3.radius <= 0.0F)) {
            float var4 = var3.x;
            float var5 = var3.y;
            float var6 = var3.z;
            float var7 = var6;
            var6 = var5;
            var4 *= -54.0F;
            var5 = var7 * 54.0F;
            var6 *= 54.0F;
            GL11.glBegin(1);
            GL11.glVertex3f(var4, var5, var6);
            GL11.glVertex3f(0.0F, 0.0F, 0.0F);
            GL11.glEnd();
         }
      }

      GL11.glColor3f(1.0F, 1.0F, 1.0F);
      GL11.glEnable(3553);
      GL11.glEnable(2929);
   }

   public static void drawBoneMtx(org.lwjgl.util.vector.Matrix4f var0) {
      GL11.glDisable(2929);
      GL11.glDisable(3553);
      GL11.glBegin(1);
      drawBoneMtxInternal(var0);
      GL11.glEnd();
      GL11.glColor3f(1.0F, 1.0F, 1.0F);
      GL11.glEnable(2929);
   }

   private static void drawBoneMtxInternal(org.lwjgl.util.vector.Matrix4f var0) {
      float var1 = 0.5F;
      float var2 = 0.15F;
      float var3 = 0.1F;
      float var4 = var0.m03;
      float var5 = var0.m13;
      float var6 = var0.m23;
      float var7 = var0.m00;
      float var8 = var0.m10;
      float var9 = var0.m20;
      float var10 = var0.m01;
      float var11 = var0.m11;
      float var12 = var0.m21;
      float var13 = var0.m02;
      float var14 = var0.m12;
      float var15 = var0.m22;
      drawArrowInternal(var4, var5, var6, var7, var8, var9, var13, var14, var15, var1, var2, var3, 1.0F, 0.0F, 0.0F);
      drawArrowInternal(var4, var5, var6, var10, var11, var12, var13, var14, var15, var1, var2, var3, 0.0F, 1.0F, 0.0F);
      drawArrowInternal(var4, var5, var6, var13, var14, var15, var7, var8, var9, var1, var2, var3, 0.0F, 0.0F, 1.0F);
   }

   private static void drawArrowInternal(float var0, float var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8, float var9, float var10, float var11, float var12, float var13, float var14) {
      float var15 = 1.0F - var10;
      GL11.glColor3f(var12, var13, var14);
      GL11.glVertex3f(var0, var1, var2);
      GL11.glVertex3f(var0 + var3 * var9, var1 + var4 * var9, var2 + var5 * var9);
      GL11.glVertex3f(var0 + var3 * var9, var1 + var4 * var9, var2 + var5 * var9);
      GL11.glVertex3f(var0 + (var3 * var15 + var6 * var11) * var9, var1 + (var4 * var15 + var7 * var11) * var9, var2 + (var5 * var15 + var8 * var11) * var9);
      GL11.glVertex3f(var0 + var3 * var9, var1 + var4 * var9, var2 + var5 * var9);
      GL11.glVertex3f(var0 + (var3 * var15 - var6 * var11) * var9, var1 + (var4 * var15 - var7 * var11) * var9, var2 + (var5 * var15 - var8 * var11) * var9);
   }

   public void debugDrawLightSource(IsoLightSource var1, float var2, float var3, float var4, float var5) {
      debugDrawLightSource((float)var1.x, (float)var1.y, (float)var1.z, var2, var3, var4, var5);
   }

   public static void debugDrawLightSource(float var0, float var1, float var2, float var3, float var4, float var5, float var6) {
      float var7 = var0 - var3 + 0.5F;
      float var8 = var1 - var4 + 0.5F;
      float var9 = var2 - var5 + 0.0F;
      var7 *= 0.67F;
      var8 *= 0.67F;
      float var10 = var7;
      var7 = (float)((double)var7 * Math.cos((double)var6) - (double)var8 * Math.sin((double)var6));
      var8 = (float)((double)var10 * Math.sin((double)var6) + (double)var8 * Math.cos((double)var6));
      var7 *= -1.0F;
      GL11.glDisable(3553);
      GL11.glDisable(2929);
      GL11.glBegin(1);
      GL11.glColor3f(1.0F, 1.0F, 0.0F);
      GL11.glVertex3f(var7, var9, var8);
      GL11.glVertex3f(0.0F, 0.0F, 0.0F);
      GL11.glVertex3f(var7, var9, var8);
      GL11.glVertex3f(var7 + 0.1F, var9, var8);
      GL11.glVertex3f(var7, var9, var8);
      GL11.glVertex3f(var7, var9 + 0.1F, var8);
      GL11.glVertex3f(var7, var9, var8);
      GL11.glVertex3f(var7, var9, var8 + 0.1F);
      GL11.glEnd();
      GL11.glColor3f(1.0F, 1.0F, 1.0F);
      GL11.glEnable(3553);
      GL11.glEnable(2929);
   }

   public void DrawVehicle(ModelSlotRenderData var1, ModelInstanceRenderData var2) {
      if (!DebugOptions.instance.ModelRenderSkipVehicles.getValue()) {
         ModelInstance var3 = var2.modelInstance;
         float var4 = var1.ambientR;
         Texture var5 = var2.tex;
         float var6 = var2.tintR;
         float var7 = var2.tintG;
         float var8 = var2.tintB;
         PZGLUtil.checkGLErrorThrow("Model.drawVehicle Enter inst: %s, instTex: %s, slotData: %s", var3, var5, var1);
         GL11.glEnable(2884);
         GL11.glCullFace(var3.m_modelScript != null && var3.m_modelScript.invertX ? 1029 : 1028);
         GL11.glEnable(2929);
         GL11.glDepthFunc(513);
         ModelCamera.instance.Begin();
         GL11.glMatrixMode(5888);
         GL11.glTranslatef(0.0F, var1.centerOfMassY, 0.0F);
         Shader var9 = this.Effect;
         PZGLUtil.pushAndMultMatrix(5888, var2.xfrm);
         if (Core.bDebug && DebugOptions.instance.ModelRenderWireframe.getValue()) {
            GL11.glPolygonMode(1032, 6913);
            GL11.glEnable(2848);
            GL11.glLineWidth(0.75F);
            var9 = ShaderManager.instance.getOrCreateShader("vehicle_wireframe", this.bStatic);
            if (var9 != null) {
               var9.Start();
               if (this.bStatic) {
                  var9.setTransformMatrix(IDENTITY, false);
               } else {
                  var9.setMatrixPalette(var2.matrixPalette, true);
               }

               this.Mesh.Draw(var9);
               var9.End();
            }

            GL11.glDisable(2848);
            PZGLUtil.popMatrix(5888);
            ModelCamera.instance.End();
         } else {
            if (var9 != null) {
               var9.Start();
               this.setLights(var1, 3);
               if (var9.isVehicleShader()) {
                  VehicleModelInstance var10 = (VehicleModelInstance)Type.tryCastTo(var3, VehicleModelInstance.class);
                  if (var3 instanceof VehicleSubModelInstance) {
                     var10 = (VehicleModelInstance)Type.tryCastTo(var3.parent, VehicleModelInstance.class);
                  }

                  var9.setTexture(var10.tex, "Texture0", 0);
                  GL11.glTexEnvi(8960, 8704, 7681);
                  var9.setTexture(var10.textureRust, "TextureRust", 1);
                  GL11.glTexEnvi(8960, 8704, 7681);
                  var9.setTexture(var10.textureMask, "TextureMask", 2);
                  GL11.glTexEnvi(8960, 8704, 7681);
                  var9.setTexture(var10.textureLights, "TextureLights", 3);
                  GL11.glTexEnvi(8960, 8704, 7681);
                  var9.setTexture(var10.textureDamage1Overlay, "TextureDamage1Overlay", 4);
                  GL11.glTexEnvi(8960, 8704, 7681);
                  var9.setTexture(var10.textureDamage1Shell, "TextureDamage1Shell", 5);
                  GL11.glTexEnvi(8960, 8704, 7681);
                  var9.setTexture(var10.textureDamage2Overlay, "TextureDamage2Overlay", 6);
                  GL11.glTexEnvi(8960, 8704, 7681);
                  var9.setTexture(var10.textureDamage2Shell, "TextureDamage2Shell", 7);
                  GL11.glTexEnvi(8960, 8704, 7681);

                  try {
                     if (Core.getInstance().getPerfReflectionsOnLoad()) {
                        var9.setTexture((Texture)SkyBox.getInstance().getTextureCurrent(), "TextureReflectionA", 8);
                        GL11.glTexEnvi(8960, 8704, 7681);
                        GL11.glGetError();
                     }
                  } catch (Throwable var13) {
                  }

                  try {
                     if (Core.getInstance().getPerfReflectionsOnLoad()) {
                        var9.setTexture((Texture)SkyBox.getInstance().getTexturePrev(), "TextureReflectionB", 9);
                        GL11.glTexEnvi(8960, 8704, 7681);
                        GL11.glGetError();
                     }
                  } catch (Throwable var12) {
                  }

                  var9.setReflectionParam(SkyBox.getInstance().getTextureShift(), var10.refWindows, var10.refBody);
                  var9.setTextureUninstall1(var10.textureUninstall1);
                  var9.setTextureUninstall2(var10.textureUninstall2);
                  var9.setTextureLightsEnables1(var10.textureLightsEnables1);
                  var9.setTextureLightsEnables2(var10.textureLightsEnables2);
                  var9.setTextureDamage1Enables1(var10.textureDamage1Enables1);
                  var9.setTextureDamage1Enables2(var10.textureDamage1Enables2);
                  var9.setTextureDamage2Enables1(var10.textureDamage2Enables1);
                  var9.setTextureDamage2Enables2(var10.textureDamage2Enables2);
                  var9.setMatrixBlood1(var10.matrixBlood1Enables1, var10.matrixBlood1Enables2);
                  var9.setMatrixBlood2(var10.matrixBlood2Enables1, var10.matrixBlood2Enables2);
                  var9.setTextureRustA(var10.textureRustA);
                  var9.setTexturePainColor(var10.painColor, var1.alpha);
                  if (this.bStatic) {
                     var9.setTransformMatrix(IDENTITY, false);
                  } else {
                     var9.setMatrixPalette(var2.matrixPalette, true);
                  }
               } else if (var3 instanceof VehicleSubModelInstance) {
                  GL13.glActiveTexture(33984);
                  var9.setTexture(var5, "Texture", 0);
                  var9.setShaderAlpha(var1.alpha);
                  if (this.bStatic) {
                     var9.setTransformMatrix(IDENTITY, false);
                  }
               } else {
                  GL13.glActiveTexture(33984);
                  var9.setTexture(var5, "Texture", 0);
               }

               var9.setAmbient(var4);
               var9.setTint(var6, var7, var8);
               this.Mesh.Draw(var9);
               var9.End();
            }

            if (Core.bDebug && DebugOptions.instance.ModelRenderLights.getValue() && var2 == var1.modelData.get(0)) {
               this.drawVehicleLights(var1);
            }

            PZGLUtil.popMatrix(5888);
            ModelCamera.instance.End();
            PZGLUtil.checkGLErrorThrow("Model.drawVehicle Exit inst: %s, instTex: %s, slotData: %s", var3, var5, var1);
         }
      }
   }

   public static void debugDrawAxis(float var0, float var1, float var2, float var3, float var4) {
      for(int var5 = 0; var5 < 8; ++var5) {
         GL13.glActiveTexture('蓀' + var5);
         GL11.glDisable(3553);
      }

      GL11.glDisable(2929);
      GL11.glLineWidth(var4);
      GL11.glBegin(1);
      GL11.glColor3f(1.0F, 0.0F, 0.0F);
      GL11.glVertex3f(var0, var1, var2);
      GL11.glVertex3f(var0 + var3, var1, var2);
      GL11.glColor3f(0.0F, 1.0F, 0.0F);
      GL11.glVertex3f(var0, var1, var2);
      GL11.glVertex3f(var0, var1 + var3, var2);
      GL11.glColor3f(0.0F, 0.0F, 1.0F);
      GL11.glVertex3f(var0, var1, var2);
      GL11.glVertex3f(var0, var1, var2 + var3);
      GL11.glEnd();
      GL11.glColor3f(1.0F, 1.0F, 1.0F);
      GL11.glEnable(2929);
      GL13.glActiveTexture(33984);
      GL11.glEnable(3553);
   }

   private void setLights(ModelSlotRenderData var1, int var2) {
      for(int var3 = 0; var3 < var2; ++var3) {
         ModelInstance.EffectLight var4 = var1.effectLights[var3];
         this.Effect.setLight(var3, var4.x, var4.y, var4.z, var4.r, var4.g, var4.b, (float)var4.radius, var1.animPlayerAngle, var1.x, var1.y, var1.z, var1.object);
      }

   }

   public void CreateShader(String var1) {
      if (!ModelManager.NoOpenGL) {
         Lambda.invoke(RenderThread::invokeOnRenderContext, this, var1, (var0, var1x) -> {
            var0.Effect = ShaderManager.instance.getOrCreateShader(var1x, var0.bStatic);
         });
      }
   }

   public AssetType getType() {
      return ASSET_TYPE;
   }

   protected void onBeforeReady() {
      super.onBeforeReady();
      this.Tag = this.Mesh.skinningData;
      this.softwareMesh = this.Mesh.softwareMesh;
      this.assetParams = null;
   }

   public static final class ModelAssetParams extends AssetManager.AssetParams {
      public String meshName;
      public String textureName;
      public int textureFlags;
      public String shaderName;
      public boolean bStatic = false;
      public ModelMesh animationsModel;
   }
}

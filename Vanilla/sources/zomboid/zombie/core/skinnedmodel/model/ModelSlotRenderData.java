package zombie.core.skinnedmodel.model;

import java.util.ArrayList;
import org.joml.Math;
import org.joml.Matrix4f;
import org.joml.Matrix4fc;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL11;
import zombie.GameProfiler;
import zombie.characters.IsoGameCharacter;
import zombie.characters.IsoPlayer;
import zombie.core.Core;
import zombie.core.SpriteRenderer;
import zombie.core.opengl.PZGLUtil;
import zombie.core.opengl.RenderSettings;
import zombie.core.skinnedmodel.ModelCamera;
import zombie.core.skinnedmodel.ModelManager;
import zombie.core.skinnedmodel.animation.AnimationPlayer;
import zombie.core.skinnedmodel.shader.Shader;
import zombie.core.textures.ColorInfo;
import zombie.core.textures.Texture;
import zombie.core.textures.TextureDraw;
import zombie.debug.DebugOptions;
import zombie.iso.IsoCamera;
import zombie.iso.IsoLightSource;
import zombie.iso.IsoMovingObject;
import zombie.iso.Vector3;
import zombie.network.GameServer;
import zombie.network.ServerGUI;
import zombie.popman.ObjectPool;
import zombie.scripting.objects.ModelAttachment;
import zombie.scripting.objects.VehicleScript;
import zombie.util.IPooledObject;
import zombie.util.Pool;
import zombie.util.Type;
import zombie.vehicles.BaseVehicle;

public final class ModelSlotRenderData extends TextureDraw.GenericDrawer {
   public IsoGameCharacter character;
   public IsoMovingObject object;
   private ModelManager.ModelSlot modelSlot;
   public final ModelInstanceRenderDataList modelData = new ModelInstanceRenderDataList();
   private final ModelInstanceRenderDataList readyModelData = new ModelInstanceRenderDataList();
   public ModelInstanceTextureCreator textureCreator;
   public AnimationPlayer animPlayer;
   public float animPlayerAngle;
   public float x;
   public float y;
   public float z;
   public float ambientR;
   public float ambientG;
   public float ambientB;
   public boolean bOutside;
   public final Matrix4f vehicleTransform = new Matrix4f();
   public boolean bInVehicle;
   public float inVehicleX;
   public float inVehicleY;
   public float inVehicleZ;
   public float vehicleAngleX;
   public float vehicleAngleY;
   public float vehicleAngleZ;
   public float alpha;
   private boolean bRendered;
   private boolean bReady;
   public final ModelInstance.EffectLight[] effectLights = new ModelInstance.EffectLight[5];
   public float centerOfMassY;
   public boolean RENDER_TO_TEXTURE;
   private static Shader solidColor;
   private static Shader solidColorStatic;
   private boolean bCharacterOutline = false;
   private final ColorInfo outlineColor = new ColorInfo(1.0F, 0.0F, 0.0F, 1.0F);
   private ModelSlotDebugRenderData m_debugRenderData;
   private static final ObjectPool pool = new ObjectPool(ModelSlotRenderData::new);

   public ModelSlotRenderData() {
      for(int var1 = 0; var1 < this.effectLights.length; ++var1) {
         this.effectLights[var1] = new ModelInstance.EffectLight();
      }

   }

   public ModelSlotRenderData init(ModelManager.ModelSlot var1) {
      int var2 = IsoCamera.frameState.playerIndex;
      this.modelSlot = var1;
      this.object = var1.model.object;
      this.x = this.object.x;
      this.y = this.object.y;
      this.z = this.object.z;
      this.character = var1.character;
      BaseVehicle var3 = (BaseVehicle)Type.tryCastTo(this.object, BaseVehicle.class);
      int var6;
      Vector3f var15;
      if (var3 != null) {
         this.textureCreator = null;
         this.animPlayer = var3.getAnimationPlayer();
         this.animPlayerAngle = Float.NaN;
         this.centerOfMassY = var3.jniTransform.origin.y - BaseVehicle.CENTER_OF_MASS_MAGIC;
         if (BaseVehicle.RENDER_TO_TEXTURE) {
            this.centerOfMassY = 0.0F - BaseVehicle.CENTER_OF_MASS_MAGIC;
         }

         this.alpha = this.object.getAlpha(var2);
         VehicleModelInstance var4 = (VehicleModelInstance)var1.model;
         IsoLightSource[] var5 = var4.getLights();

         for(var6 = 0; var6 < this.effectLights.length; ++var6) {
            this.effectLights[var6].set(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0);
         }

         var15 = (Vector3f)((BaseVehicle.Vector3fObjectPool)BaseVehicle.TL_vector3f_pool.get()).alloc();

         for(int var7 = 0; var7 < var5.length; ++var7) {
            IsoLightSource var8 = var5[var7];
            if (var8 != null) {
               Vector3f var9 = var3.getLocalPos((float)var8.x + 0.5F, (float)var8.y + 0.5F, (float)var8.z + 0.75F, var15);
               var3.fixLightbarModelLighting(var8, var15);
               this.effectLights[var7].set(var9.x, var9.y, var9.z, var8.r, var8.g, var8.b, var8.radius);
            }
         }

         ((BaseVehicle.Vector3fObjectPool)BaseVehicle.TL_vector3f_pool.get()).release(var15);
         float var17 = 1.0F - Math.min(RenderSettings.getInstance().getPlayerSettings(var2).getDarkness() * 0.6F, 0.8F);
         var17 *= 0.9F;
         this.ambientR = this.ambientG = this.ambientB = var17;
         this.vehicleTransform.set((Matrix4fc)var3.vehicleTransform);
      } else {
         this.textureCreator = this.character.getTextureCreator();
         if (this.textureCreator != null && this.textureCreator.isRendered()) {
            this.textureCreator = null;
         }

         ModelInstance.PlayerData var10 = var1.model.playerData[var2];
         this.animPlayer = this.character.getAnimationPlayer();
         this.animPlayerAngle = this.animPlayer.getRenderedAngle();

         for(int var12 = 0; var12 < this.effectLights.length; ++var12) {
            ModelInstance.EffectLight var16 = var10.effectLightsMain[var12];
            this.effectLights[var12].set(var16.x, var16.y, var16.z, var16.r, var16.g, var16.b, var16.radius);
         }

         this.ambientR = var10.currentAmbient.x;
         this.ambientG = var10.currentAmbient.y;
         this.ambientB = var10.currentAmbient.z;
         this.bOutside = this.character.getCurrentSquare() != null && this.character.getCurrentSquare().isOutside();
         this.alpha = this.character.getAlpha(var2);
         if (Core.bDebug && DebugOptions.instance.DebugDraw_SkipWorldShading.getValue()) {
            this.ambientR = this.ambientG = this.ambientB = 1.0F;
         }

         if (GameServer.bServer && ServerGUI.isCreated()) {
            this.ambientR = this.ambientG = this.ambientB = 1.0F;
         }

         this.bCharacterOutline = this.character.bOutline[var2];
         if (this.bCharacterOutline) {
            this.outlineColor.set(this.character.outlineColor[var2]);
         }

         this.bInVehicle = this.character.isSeatedInVehicle();
         if (this.bInVehicle) {
            this.animPlayerAngle = 0.0F;
            BaseVehicle var13 = this.character.getVehicle();
            this.centerOfMassY = var13.jniTransform.origin.y - BaseVehicle.CENTER_OF_MASS_MAGIC;
            this.x = var13.x;
            this.y = var13.y;
            this.z = var13.z;
            var15 = (Vector3f)((BaseVehicle.Vector3fObjectPool)BaseVehicle.TL_vector3f_pool.get()).alloc();
            var13.getPassengerLocalPos(var13.getSeat(this.character), var15);
            this.inVehicleX = var15.x;
            this.inVehicleY = var15.y;
            this.inVehicleZ = var15.z;
            ((BaseVehicle.Vector3fObjectPool)BaseVehicle.TL_vector3f_pool.get()).release(var15);
            Vector3f var18 = var13.vehicleTransform.getEulerAnglesZYX((Vector3f)((BaseVehicle.Vector3fObjectPool)BaseVehicle.TL_vector3f_pool.get()).alloc());
            this.vehicleAngleZ = (float)java.lang.Math.toDegrees((double)var18.z);
            this.vehicleAngleY = (float)java.lang.Math.toDegrees((double)var18.y);
            this.vehicleAngleX = (float)java.lang.Math.toDegrees((double)var18.x);
            ((BaseVehicle.Vector3fObjectPool)BaseVehicle.TL_vector3f_pool.get()).release(var18);
         }
      }

      this.RENDER_TO_TEXTURE = BaseVehicle.RENDER_TO_TEXTURE;
      this.modelData.clear();
      ModelInstanceRenderData var11 = null;
      boolean var14;
      if (var1.model.model.isReady() && (var1.model.AnimPlayer == null || var1.model.AnimPlayer.isReady())) {
         var11 = ModelInstanceRenderData.alloc().init(var1.model);
         this.modelData.add(var11);
         if (var1.sub.size() != var1.model.sub.size()) {
            var14 = true;
         }
      }

      this.initRenderData(var1.model.sub, var11);
      var14 = false;

      for(var6 = 0; var6 < this.modelData.size(); ++var6) {
         ModelInstanceRenderData var20 = (ModelInstanceRenderData)this.modelData.get(var6);
         if (this.character != null && var20.modelInstance == this.character.primaryHandModel && this.character.isMuzzleFlash()) {
            var20.m_muzzleFlash = true;
         }

         if (var20.modelInstance != null && var20.modelInstance.hasTextureCreator()) {
            var14 = true;
         }
      }

      if (this.textureCreator != null) {
         ++this.textureCreator.renderRefCount;
      }

      if (this.character != null && (this.textureCreator != null || var14)) {
         assert this.readyModelData.isEmpty();

         ModelInstanceRenderData.release(this.readyModelData);
         this.readyModelData.clear();

         for(var6 = 0; var6 < this.character.getReadyModelData().size(); ++var6) {
            ModelInstance var21 = (ModelInstance)this.character.getReadyModelData().get(var6);
            ModelInstanceRenderData var19 = ModelInstanceRenderData.alloc().init(var21);
            var19.transformToParent(this.getParentData(var21));
            this.readyModelData.add(var19);
         }
      }

      if (Core.bDebug) {
         this.m_debugRenderData = ModelSlotDebugRenderData.alloc().init(this);
      }

      this.bRendered = false;
      return this;
   }

   private ModelInstanceRenderData getParentData(ModelInstance var1) {
      for(int var2 = 0; var2 < this.readyModelData.size(); ++var2) {
         ModelInstanceRenderData var3 = (ModelInstanceRenderData)this.readyModelData.get(var2);
         if (var3.modelInstance == var1.parent) {
            return var3;
         }
      }

      return null;
   }

   private ModelInstanceRenderData initRenderData(ModelInstance var1, ModelInstanceRenderData var2) {
      ModelInstanceRenderData var3 = ModelInstanceRenderData.alloc().init(var1);
      var3.transformToParent(var2);
      this.modelData.add(var3);
      this.initRenderData(var1.sub, var3);
      return var3;
   }

   private void initRenderData(ArrayList var1, ModelInstanceRenderData var2) {
      for(int var3 = 0; var3 < var1.size(); ++var3) {
         ModelInstance var4 = (ModelInstance)var1.get(var3);
         if (var4.model.isReady() && (var4.AnimPlayer == null || var4.AnimPlayer.isReady())) {
            this.initRenderData(var4, var2);
         }
      }

   }

   public void render() {
      if (this.character == null) {
         this.renderVehicle();
      } else {
         this.renderCharacter();
      }

   }

   public void renderDebug() {
      if (this.m_debugRenderData != null) {
         this.m_debugRenderData.render();
      }

   }

   private void renderCharacter() {
      this.bReady = true;
      if (this.textureCreator != null && !this.textureCreator.isRendered()) {
         this.textureCreator.render();
         if (!this.textureCreator.isRendered()) {
            this.bReady = false;
         }
      }

      int var1;
      for(var1 = 0; var1 < this.modelData.size(); ++var1) {
         ModelInstanceRenderData var2 = (ModelInstanceRenderData)this.modelData.get(var1);
         ModelInstanceTextureInitializer var3 = var2.modelInstance.getTextureInitializer();
         if (var3 != null && !var3.isRendered()) {
            var3.render();
            if (!var3.isRendered()) {
               this.bReady = false;
            }
         }
      }

      if (this.bReady || !this.readyModelData.isEmpty()) {
         if (this.bCharacterOutline) {
            ModelCamera.instance.bDepthMask = false;
            GameProfiler.getInstance().invokeAndMeasure("performRenderCharacterOutline", this, ModelSlotRenderData::performRenderCharacterOutline);
         }

         ModelCamera.instance.bDepthMask = true;
         GameProfiler.getInstance().invokeAndMeasure("renderCharacter", this, ModelSlotRenderData::performRenderCharacter);
         var1 = SpriteRenderer.instance.getRenderingPlayerIndex();
         IsoPlayer var4 = (IsoPlayer)Type.tryCastTo(this.character, IsoPlayer.class);
         if (var4 != null && !this.bCharacterOutline && var4 == IsoPlayer.players[var1]) {
            ModelOutlines.instance.setPlayerRenderData(this);
         }

         this.bRendered = this.bReady;
      }
   }

   private void renderVehicleDebug() {
      if (Core.bDebug) {
         Vector3 var1 = Model.tempo;
         ModelCamera.instance.Begin();
         GL11.glMatrixMode(5888);
         GL11.glTranslatef(0.0F, this.centerOfMassY, 0.0F);
         if (this.m_debugRenderData != null && !this.modelData.isEmpty()) {
            PZGLUtil.pushAndMultMatrix(5888, ((ModelInstanceRenderData)this.modelData.get(0)).xfrm);
            this.m_debugRenderData.render();
            PZGLUtil.popMatrix(5888);
         }

         BaseVehicle var2;
         if (DebugOptions.instance.ModelRenderAttachments.getValue()) {
            var2 = (BaseVehicle)this.object;
            ModelInstanceRenderData var3 = (ModelInstanceRenderData)this.modelData.get(0);
            PZGLUtil.pushAndMultMatrix(5888, this.vehicleTransform);
            float var4 = var2.getScript().getModelScale();
            float var5 = var3.modelInstance.scale;
            Matrix4f var6 = (Matrix4f)((BaseVehicle.Matrix4fObjectPool)BaseVehicle.TL_matrix4f_pool.get()).alloc();
            var6.scaling(1.0F / var4);
            Matrix4f var7 = (Matrix4f)((BaseVehicle.Matrix4fObjectPool)BaseVehicle.TL_matrix4f_pool.get()).alloc();

            for(int var8 = 0; var8 < var2.getScript().getAttachmentCount(); ++var8) {
               ModelAttachment var9 = var2.getScript().getAttachment(var8);
               var3.modelInstance.getAttachmentMatrix(var9, var7);
               var6.mul((Matrix4fc)var7, var7);
               PZGLUtil.pushAndMultMatrix(5888, var7);
               Model.debugDrawAxis(0.0F, 0.0F, 0.0F, 1.0F, 2.0F);
               PZGLUtil.popMatrix(5888);
            }

            ((BaseVehicle.Matrix4fObjectPool)BaseVehicle.TL_matrix4f_pool.get()).release(var7);
            ((BaseVehicle.Matrix4fObjectPool)BaseVehicle.TL_matrix4f_pool.get()).release(var6);
            PZGLUtil.popMatrix(5888);
         }

         if (Core.bDebug && DebugOptions.instance.ModelRenderAxis.getValue() && !this.modelData.isEmpty()) {
            var2 = (BaseVehicle)this.object;
            GL11.glMatrixMode(5888);
            Vector3f var10 = this.vehicleTransform.getEulerAnglesZYX((Vector3f)((BaseVehicle.Vector3fObjectPool)BaseVehicle.TL_vector3f_pool.get()).alloc());
            GL11.glRotatef((float)java.lang.Math.toDegrees((double)var10.z), 0.0F, 0.0F, 1.0F);
            GL11.glRotatef((float)java.lang.Math.toDegrees((double)var10.y), 0.0F, 1.0F, 0.0F);
            GL11.glRotatef((float)java.lang.Math.toDegrees((double)var10.x), 1.0F, 0.0F, 0.0F);
            ((BaseVehicle.Vector3fObjectPool)BaseVehicle.TL_vector3f_pool.get()).release(var10);
            Model.debugDrawAxis(0.0F, 0.0F, 0.0F, 1.0F, 4.0F);

            for(int var11 = 1; var11 < this.modelData.size(); ++var11) {
               VehicleSubModelInstance var12 = (VehicleSubModelInstance)Type.tryCastTo(((ModelInstanceRenderData)this.modelData.get(var11)).modelInstance, VehicleSubModelInstance.class);
               if (var12 != null && var12.modelInfo.wheelIndex >= 0) {
                  float var13 = 1.0F;
                  VehicleScript.Wheel var14 = var2.getScript().getWheel(var12.modelInfo.wheelIndex);
                  byte var15 = -1;
                  var1.set(var14.offset.x * (float)var15, var2.getScript().getModel().offset.y + var14.offset.y + var2.getScript().getSuspensionRestLength(), var14.offset.z);
                  Model.debugDrawAxis(var1.x / var13, var1.y / var13, var1.z / var13, var2.getScript().getSuspensionRestLength() / var13, 2.0F);
               }
            }
         }

         ModelCamera.instance.End();
      }
   }

   private void performRenderCharacter() {
      GL11.glPushClientAttrib(-1);
      GL11.glPushAttrib(1048575);
      GL11.glEnable(3042);
      GL11.glBlendFunc(770, 771);
      GL11.glEnable(3008);
      GL11.glAlphaFunc(516, 0.0F);
      GL11.glEnable(2929);
      GL11.glDisable(3089);
      ModelInstanceRenderDataList var1 = this.modelData;
      if (this.character != null && !this.bReady) {
         var1 = this.readyModelData;
      }

      Model.CharacterModelCameraBegin(this);

      int var2;
      ModelInstanceRenderData var3;
      for(var2 = 0; var2 < var1.size(); ++var2) {
         var3 = (ModelInstanceRenderData)var1.get(var2);
         var3.RenderCharacter(this);
      }

      if (Core.bDebug) {
         this.renderDebug();

         for(var2 = 0; var2 < var1.size(); ++var2) {
            var3 = (ModelInstanceRenderData)var1.get(var2);
            var3.renderDebug();
         }
      }

      Model.CharacterModelCameraEnd();
      GL11.glPopAttrib();
      GL11.glPopClientAttrib();
      Texture.lastTextureID = -1;
      GL11.glEnable(3553);
      SpriteRenderer.ringBuffer.restoreVBOs = true;
      GL11.glDisable(2929);
      GL11.glEnable(3042);
      GL11.glBlendFunc(770, 771);
      GL11.glEnable(3008);
      GL11.glAlphaFunc(516, 0.0F);
   }

   protected void performRenderCharacterOutline() {
      GL11.glPushClientAttrib(-1);
      GL11.glPushAttrib(1048575);
      GL11.glEnable(3042);
      GL11.glBlendFunc(770, 771);
      GL11.glEnable(3008);
      GL11.glAlphaFunc(516, 0.0F);
      GL11.glEnable(2929);
      GL11.glDisable(3089);
      ModelInstanceRenderDataList var1 = this.modelData;
      if (this.character != null && !this.bReady) {
         var1 = this.readyModelData;
      }

      if (solidColor == null) {
         solidColor = new Shader("aim_outline_solid", false);
         solidColorStatic = new Shader("aim_outline_solid", true);
      }

      solidColor.Start();
      solidColor.getShaderProgram().setVector4("u_color", this.outlineColor.r, this.outlineColor.g, this.outlineColor.b, this.outlineColor.a);
      solidColor.End();
      solidColorStatic.Start();
      solidColorStatic.getShaderProgram().setVector4("u_color", this.outlineColor.r, this.outlineColor.g, this.outlineColor.b, this.outlineColor.a);
      solidColorStatic.End();
      boolean var2 = ModelOutlines.instance.beginRenderOutline(this.outlineColor);
      ModelOutlines.instance.m_fboA.startDrawing(var2, true);
      Model.CharacterModelCameraBegin(this);

      for(int var3 = 0; var3 < var1.size(); ++var3) {
         ModelInstanceRenderData var4 = (ModelInstanceRenderData)var1.get(var3);
         Shader var5 = var4.model.Effect;

         try {
            var4.model.Effect = var4.model.bStatic ? solidColorStatic : solidColor;
            var4.RenderCharacter(this);
         } finally {
            var4.model.Effect = var5;
         }
      }

      Model.CharacterModelCameraEnd();
      ModelOutlines.instance.m_fboA.endDrawing();
      GL11.glPopAttrib();
      GL11.glPopClientAttrib();
      Texture.lastTextureID = -1;
      GL11.glEnable(3553);
      SpriteRenderer.ringBuffer.restoreVBOs = true;
      GL11.glDisable(2929);
      GL11.glEnable(3042);
      GL11.glBlendFunc(770, 771);
      GL11.glEnable(3008);
      GL11.glAlphaFunc(516, 0.0F);
   }

   private void renderVehicle() {
      GL11.glPushClientAttrib(-1);
      GL11.glPushAttrib(1048575);
      GL11.glEnable(3042);
      GL11.glBlendFunc(770, 771);
      GL11.glEnable(3008);
      GL11.glAlphaFunc(516, 0.0F);
      if (this.RENDER_TO_TEXTURE) {
         GL11.glClear(256);
      }

      GL11.glEnable(2929);
      GL11.glEnable(3042);
      GL11.glBlendFunc(770, 771);
      GL11.glEnable(3008);
      GL11.glAlphaFunc(516, 0.0F);
      GL11.glDisable(3089);
      if (this.RENDER_TO_TEXTURE) {
         ModelManager.instance.bitmap.startDrawing(true, true);
         GL11.glViewport(0, 0, ModelManager.instance.bitmap.getWidth(), ModelManager.instance.bitmap.getHeight());
      }

      for(int var1 = 0; var1 < this.modelData.size(); ++var1) {
         ModelInstanceRenderData var2 = (ModelInstanceRenderData)this.modelData.get(var1);
         var2.RenderVehicle(this);
      }

      this.renderVehicleDebug();
      if (this.RENDER_TO_TEXTURE) {
         ModelManager.instance.bitmap.endDrawing();
      }

      GL11.glPopAttrib();
      GL11.glPopClientAttrib();
      Texture.lastTextureID = -1;
      GL11.glEnable(3553);
      SpriteRenderer.ringBuffer.restoreBoundTextures = true;
      SpriteRenderer.ringBuffer.restoreVBOs = true;
      GL11.glDisable(2929);
      GL11.glEnable(3042);
      GL11.glBlendFunc(770, 771);
      GL11.glEnable(3008);
      GL11.glAlphaFunc(516, 0.0F);
   }

   private void doneWithTextureCreator(ModelInstanceTextureCreator var1) {
      if (var1 != null) {
         if (var1.testNotReady > 0) {
            --var1.testNotReady;
         }

         if (var1.renderRefCount <= 0) {
            if (var1.isRendered()) {
               var1.postRender();
               if (var1 == this.character.getTextureCreator()) {
                  this.character.setTextureCreator((ModelInstanceTextureCreator)null);
               }
            } else if (var1 != this.character.getTextureCreator()) {
               var1.postRender();
            }

         }
      }
   }

   public void postRender() {
      assert this.modelSlot.renderRefCount > 0;

      --this.modelSlot.renderRefCount;
      if (this.textureCreator != null) {
         --this.textureCreator.renderRefCount;
         this.doneWithTextureCreator(this.textureCreator);
         this.textureCreator = null;
      }

      ModelInstanceRenderData.release(this.readyModelData);
      this.readyModelData.clear();
      if (this.bRendered) {
         ModelManager.instance.derefModelInstances(this.character.getReadyModelData());
         this.character.getReadyModelData().clear();

         for(int var1 = 0; var1 < this.modelData.size(); ++var1) {
            ModelInstance var2 = ((ModelInstanceRenderData)this.modelData.get(var1)).modelInstance;
            ++var2.renderRefCount;
            this.character.getReadyModelData().add(var2);
         }
      }

      this.character = null;
      this.object = null;
      this.animPlayer = null;
      this.m_debugRenderData = (ModelSlotDebugRenderData)Pool.tryRelease((IPooledObject)this.m_debugRenderData);
      ModelInstanceRenderData.release(this.modelData);
      pool.release((Object)this);
   }

   public static ModelSlotRenderData alloc() {
      return (ModelSlotRenderData)pool.alloc();
   }
}

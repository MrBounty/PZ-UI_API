package zombie.core.skinnedmodel.model;

import gnu.trove.list.array.TFloatArrayList;
import java.util.ArrayList;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import zombie.core.Color;
import zombie.core.math.PZMath;
import zombie.core.opengl.PZGLUtil;
import zombie.core.skinnedmodel.HelperFunctions;
import zombie.core.skinnedmodel.animation.AnimationPlayer;
import zombie.debug.DebugOptions;
import zombie.inventory.types.HandWeapon;
import zombie.iso.IsoCamera;
import zombie.iso.IsoGridSquare;
import zombie.util.Pool;
import zombie.util.PooledObject;
import zombie.util.Type;
import zombie.vehicles.BaseVehicle;

public final class ModelSlotDebugRenderData extends PooledObject {
   private static final Pool s_pool = new Pool(ModelSlotDebugRenderData::new);
   private ModelSlotRenderData m_slotData;
   private final TFloatArrayList m_boneCoords = new TFloatArrayList();
   private final ArrayList m_boneMatrices = new ArrayList();
   private final TFloatArrayList m_squareLights = new TFloatArrayList();
   private Matrix4f m_weaponMatrix;
   private float m_weaponLength;

   public static ModelSlotDebugRenderData alloc() {
      return (ModelSlotDebugRenderData)s_pool.alloc();
   }

   public ModelSlotDebugRenderData init(ModelSlotRenderData var1) {
      this.m_slotData = var1;
      this.initBoneAxis();
      this.initSkeleton();
      this.initLights();
      this.initWeaponHitPoint();

      for(int var2 = 0; var2 < var1.modelData.size(); ++var2) {
         ModelInstanceRenderData var3 = (ModelInstanceRenderData)var1.modelData.get(var2);
         var3.m_debugRenderData = ModelInstanceDebugRenderData.alloc().init(var1, var3);
      }

      return this;
   }

   private void initBoneAxis() {
      for(int var1 = 0; var1 < this.m_boneMatrices.size(); ++var1) {
         HelperFunctions.returnMatrix((org.lwjgl.util.vector.Matrix4f)this.m_boneMatrices.get(var1));
      }

      this.m_boneMatrices.clear();
      if (this.m_slotData.animPlayer != null && this.m_slotData.animPlayer.hasSkinningData()) {
         if (DebugOptions.instance.Character.Debug.Render.Bip01.getValue()) {
            this.initBoneAxis("Bip01");
         }

         if (DebugOptions.instance.Character.Debug.Render.PrimaryHandBone.getValue()) {
            this.initBoneAxis("Bip01_Prop1");
         }

         if (DebugOptions.instance.Character.Debug.Render.SecondaryHandBone.getValue()) {
            this.initBoneAxis("Bip01_Prop2");
         }

         if (DebugOptions.instance.Character.Debug.Render.TranslationData.getValue()) {
            this.initBoneAxis("Translation_Data");
         }

      }
   }

   private void initBoneAxis(String var1) {
      Integer var2 = (Integer)this.m_slotData.animPlayer.getSkinningData().BoneIndices.get(var1);
      if (var2 != null) {
         org.lwjgl.util.vector.Matrix4f var3 = HelperFunctions.getMatrix();
         var3.load(this.m_slotData.animPlayer.modelTransforms[var2]);
         this.m_boneMatrices.add(var3);
      }

   }

   private void initSkeleton() {
      this.m_boneCoords.clear();
      if (DebugOptions.instance.ModelRenderBones.getValue()) {
         this.initSkeleton(this.m_slotData.animPlayer);
         if (this.m_slotData.object instanceof BaseVehicle) {
            for(int var1 = 0; var1 < this.m_slotData.modelData.size(); ++var1) {
               ModelInstanceRenderData var2 = (ModelInstanceRenderData)this.m_slotData.modelData.get(var1);
               VehicleSubModelInstance var3 = (VehicleSubModelInstance)Type.tryCastTo(var2.modelInstance, VehicleSubModelInstance.class);
               if (var3 != null) {
                  this.initSkeleton(var3.AnimPlayer);
               }
            }
         }

      }
   }

   private void initSkeleton(AnimationPlayer var1) {
      if (var1 != null && var1.hasSkinningData() && !var1.isBoneTransformsNeedFirstFrame()) {
         Integer var2 = (Integer)var1.getSkinningData().BoneIndices.get("Translation_Data");

         for(int var3 = 0; var3 < var1.modelTransforms.length; ++var3) {
            if (var2 == null || var3 != var2) {
               int var4 = (Integer)var1.getSkinningData().SkeletonHierarchy.get(var3);
               if (var4 >= 0) {
                  this.initSkeleton(var1.modelTransforms, var3);
                  this.initSkeleton(var1.modelTransforms, var4);
               }
            }
         }

      }
   }

   private void initSkeleton(org.lwjgl.util.vector.Matrix4f[] var1, int var2) {
      float var3 = var1[var2].m03;
      float var4 = var1[var2].m13;
      float var5 = var1[var2].m23;
      this.m_boneCoords.add(var3);
      this.m_boneCoords.add(var4);
      this.m_boneCoords.add(var5);
   }

   private void initLights() {
      this.m_squareLights.clear();
      if (DebugOptions.instance.ModelRenderLights.getValue()) {
         if (this.m_slotData.character != null) {
            if (this.m_slotData.character.getCurrentSquare() != null) {
               int var1 = IsoCamera.frameState.playerIndex;
               IsoGridSquare.ILighting var2 = this.m_slotData.character.getCurrentSquare().lighting[var1];

               for(int var3 = 0; var3 < var2.resultLightCount(); ++var3) {
                  IsoGridSquare.ResultLight var4 = var2.getResultLight(var3);
                  this.m_squareLights.add((float)var4.x);
                  this.m_squareLights.add((float)var4.y);
                  this.m_squareLights.add((float)var4.z);
               }

            }
         }
      }
   }

   private void initWeaponHitPoint() {
      if (this.m_weaponMatrix != null) {
         ((BaseVehicle.Matrix4fObjectPool)BaseVehicle.TL_matrix4f_pool.get()).release(this.m_weaponMatrix);
         this.m_weaponMatrix = null;
      }

      if (DebugOptions.instance.ModelRenderWeaponHitPoint.getValue()) {
         if (this.m_slotData.animPlayer != null && this.m_slotData.animPlayer.hasSkinningData()) {
            if (this.m_slotData.character != null) {
               Integer var1 = (Integer)this.m_slotData.animPlayer.getSkinningData().BoneIndices.get("Bip01_Prop1");
               if (var1 != null) {
                  HandWeapon var2 = (HandWeapon)Type.tryCastTo(this.m_slotData.character.getPrimaryHandItem(), HandWeapon.class);
                  if (var2 != null) {
                     this.m_weaponLength = var2.WeaponLength;
                     org.lwjgl.util.vector.Matrix4f var3 = this.m_slotData.animPlayer.modelTransforms[var1];
                     this.m_weaponMatrix = (Matrix4f)((BaseVehicle.Matrix4fObjectPool)BaseVehicle.TL_matrix4f_pool.get()).alloc();
                     PZMath.convertMatrix(var3, this.m_weaponMatrix);
                     this.m_weaponMatrix.transpose();
                  }
               }
            }
         }
      }
   }

   public void render() {
      this.renderBonesAxis();
      this.renderSkeleton();
      this.renderLights();
      this.renderWeaponHitPoint();
   }

   private void renderBonesAxis() {
      for(int var1 = 0; var1 < this.m_boneMatrices.size(); ++var1) {
         Model.drawBoneMtx((org.lwjgl.util.vector.Matrix4f)this.m_boneMatrices.get(var1));
      }

   }

   private void renderSkeleton() {
      if (!this.m_boneCoords.isEmpty()) {
         GL11.glDisable(2929);

         int var1;
         for(var1 = 7; var1 >= 0; --var1) {
            GL13.glActiveTexture('蓀' + var1);
            GL11.glDisable(3553);
         }

         GL11.glLineWidth(1.0F);
         GL11.glBegin(1);

         for(var1 = 0; var1 < this.m_boneCoords.size(); var1 += 6) {
            Color var2 = Model.debugDrawColours[var1 % Model.debugDrawColours.length];
            GL11.glColor3f(var2.r, var2.g, var2.b);
            float var3 = this.m_boneCoords.get(var1);
            float var4 = this.m_boneCoords.get(var1 + 1);
            float var5 = this.m_boneCoords.get(var1 + 2);
            GL11.glVertex3f(var3, var4, var5);
            var3 = this.m_boneCoords.get(var1 + 3);
            var4 = this.m_boneCoords.get(var1 + 4);
            var5 = this.m_boneCoords.get(var1 + 5);
            GL11.glVertex3f(var3, var4, var5);
         }

         GL11.glEnd();
         GL11.glColor3f(1.0F, 1.0F, 1.0F);
         GL11.glEnable(2929);
      }
   }

   private void renderLights() {
      for(int var1 = 0; var1 < this.m_squareLights.size(); var1 += 3) {
         float var2 = this.m_squareLights.get(var1);
         float var3 = this.m_squareLights.get(var1 + 1);
         float var4 = this.m_squareLights.get(var1 + 2);
         Model.debugDrawLightSource(var2, var3, var4, this.m_slotData.x, this.m_slotData.y, this.m_slotData.z, -this.m_slotData.animPlayerAngle);
      }

   }

   private void renderWeaponHitPoint() {
      if (this.m_weaponMatrix != null) {
         PZGLUtil.pushAndMultMatrix(5888, this.m_weaponMatrix);
         Model.debugDrawAxis(0.0F, this.m_weaponLength, 0.0F, 0.05F, 1.0F);
         PZGLUtil.popMatrix(5888);
      }
   }
}

package zombie.core.skinnedmodel.model;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;
import org.joml.Matrix4f;
import org.joml.Matrix4fc;
import org.lwjgl.util.vector.Vector3f;
import org.lwjglx.BufferUtils;
import zombie.core.Core;
import zombie.core.math.PZMath;
import zombie.core.skinnedmodel.ModelManager;
import zombie.core.skinnedmodel.animation.AnimationPlayer;
import zombie.core.textures.Texture;
import zombie.debug.DebugLog;
import zombie.popman.ObjectPool;
import zombie.scripting.objects.ModelAttachment;
import zombie.util.IPooledObject;
import zombie.util.Pool;
import zombie.util.StringUtils;
import zombie.util.Type;
import zombie.vehicles.BaseVehicle;

public final class ModelInstanceRenderData {
   private static final Vector3f tempVector3f = new Vector3f();
   public Model model;
   public Texture tex;
   public float depthBias;
   public float hue;
   public float tintR;
   public float tintG;
   public float tintB;
   public int parentBone;
   public FloatBuffer matrixPalette;
   public final Matrix4f xfrm = new Matrix4f();
   public SoftwareModelMeshInstance softwareMesh;
   public ModelInstance modelInstance;
   public boolean m_muzzleFlash = false;
   protected ModelInstanceDebugRenderData m_debugRenderData;
   private static final ObjectPool pool = new ObjectPool(ModelInstanceRenderData::new);

   public ModelInstanceRenderData init(ModelInstance var1) {
      this.model = var1.model;
      this.tex = var1.tex;
      this.depthBias = var1.depthBias;
      this.hue = var1.hue;
      this.parentBone = var1.parentBone;

      assert var1.character == null || var1.AnimPlayer != null;

      this.m_muzzleFlash = false;
      this.xfrm.identity();
      if (var1.AnimPlayer != null && !this.model.bStatic) {
         SkinningData var2 = (SkinningData)this.model.Tag;
         if (Core.bDebug && var2 == null) {
            DebugLog.General.warn("skinningData is null, matrixPalette may be invalid");
         }

         org.lwjgl.util.vector.Matrix4f[] var3 = var1.AnimPlayer.getSkinTransforms(var2);
         if (this.matrixPalette == null || this.matrixPalette.capacity() < var3.length * 16) {
            this.matrixPalette = BufferUtils.createFloatBuffer(var3.length * 16);
         }

         this.matrixPalette.clear();

         for(int var4 = 0; var4 < var3.length; ++var4) {
            var3[var4].store(this.matrixPalette);
         }

         this.matrixPalette.flip();
      }

      VehicleSubModelInstance var5 = (VehicleSubModelInstance)Type.tryCastTo(var1, VehicleSubModelInstance.class);
      if (var1 instanceof VehicleModelInstance || var5 != null) {
         if (var1 instanceof VehicleModelInstance) {
            this.xfrm.set((Matrix4fc)((BaseVehicle)var1.object).renderTransform);
         } else {
            this.xfrm.set((Matrix4fc)var5.modelInfo.renderTransform);
         }

         if (var1.model.Mesh != null && var1.model.Mesh.isReady() && var1.model.Mesh.m_transform != null) {
            var1.model.Mesh.m_transform.transpose();
            this.xfrm.mul((Matrix4fc)var1.model.Mesh.m_transform);
            var1.model.Mesh.m_transform.transpose();
         }
      }

      this.softwareMesh = var1.softwareMesh;
      this.modelInstance = var1;
      ++var1.renderRefCount;
      if (var1.getTextureInitializer() != null) {
         var1.getTextureInitializer().renderMain();
      }

      return this;
   }

   public void renderDebug() {
      if (this.m_debugRenderData != null) {
         this.m_debugRenderData.render();
      }

   }

   public void RenderCharacter(ModelSlotRenderData var1) {
      this.tintR = this.modelInstance.tintR;
      this.tintG = this.modelInstance.tintG;
      this.tintB = this.modelInstance.tintB;
      this.tex = this.modelInstance.tex;
      if (this.tex != null || this.modelInstance.model.tex != null) {
         this.model.DrawChar(var1, this);
      }
   }

   public void RenderVehicle(ModelSlotRenderData var1) {
      this.tintR = this.modelInstance.tintR;
      this.tintG = this.modelInstance.tintG;
      this.tintB = this.modelInstance.tintB;
      this.tex = this.modelInstance.tex;
      if (this.tex != null || this.modelInstance.model.tex != null) {
         this.model.DrawVehicle(var1, this);
      }
   }

   public static Matrix4f makeAttachmentTransform(ModelAttachment var0, Matrix4f var1) {
      var1.translation(var0.getOffset());
      org.joml.Vector3f var2 = var0.getRotate();
      var1.rotateXYZ(var2.x * 0.017453292F, var2.y * 0.017453292F, var2.z * 0.017453292F);
      return var1;
   }

   public static void applyBoneTransform(ModelInstance var0, String var1, Matrix4f var2) {
      if (var0 != null && var0.AnimPlayer != null) {
         if (!StringUtils.isNullOrWhitespace(var1)) {
            int var3 = var0.AnimPlayer.getSkinningBoneIndex(var1, -1);
            if (var3 != -1) {
               org.lwjgl.util.vector.Matrix4f var4 = var0.AnimPlayer.GetPropBoneMatrix(var3);
               Matrix4f var5 = (Matrix4f)((BaseVehicle.Matrix4fObjectPool)BaseVehicle.TL_matrix4f_pool.get()).alloc();
               PZMath.convertMatrix(var4, var5);
               var5.transpose();
               var2.mul((Matrix4fc)var5);
               ((BaseVehicle.Matrix4fObjectPool)BaseVehicle.TL_matrix4f_pool.get()).release(var5);
            }
         }
      }
   }

   public ModelInstanceRenderData transformToParent(ModelInstanceRenderData var1) {
      if (!(this.modelInstance instanceof VehicleModelInstance) && !(this.modelInstance instanceof VehicleSubModelInstance)) {
         if (var1 == null) {
            return this;
         } else {
            this.xfrm.set((Matrix4fc)var1.xfrm);
            this.xfrm.transpose();
            Matrix4f var2 = (Matrix4f)((BaseVehicle.Matrix4fObjectPool)BaseVehicle.TL_matrix4f_pool.get()).alloc();
            ModelAttachment var3 = var1.modelInstance.getAttachmentById(this.modelInstance.attachmentNameParent);
            if (var3 == null) {
               if (this.modelInstance.parentBoneName != null && var1.modelInstance.AnimPlayer != null) {
                  applyBoneTransform(var1.modelInstance, this.modelInstance.parentBoneName, this.xfrm);
               }
            } else {
               applyBoneTransform(var1.modelInstance, var3.getBone(), this.xfrm);
               makeAttachmentTransform(var3, var2);
               this.xfrm.mul((Matrix4fc)var2);
            }

            ModelAttachment var4 = this.modelInstance.getAttachmentById(this.modelInstance.attachmentNameSelf);
            if (var4 != null) {
               makeAttachmentTransform(var4, var2);
               var2.invert();
               this.xfrm.mul((Matrix4fc)var2);
            }

            if (this.modelInstance.model.Mesh != null && this.modelInstance.model.Mesh.isReady() && this.modelInstance.model.Mesh.m_transform != null) {
               this.xfrm.mul((Matrix4fc)this.modelInstance.model.Mesh.m_transform);
            }

            if (this.modelInstance.scale != 1.0F) {
               this.xfrm.scale(this.modelInstance.scale);
            }

            this.xfrm.transpose();
            ((BaseVehicle.Matrix4fObjectPool)BaseVehicle.TL_matrix4f_pool.get()).release(var2);
            return this;
         }
      } else {
         return this;
      }
   }

   private void testOnBackItem(ModelInstance var1) {
      if (var1.parent != null && var1.parent.m_modelScript != null) {
         AnimationPlayer var2 = var1.parent.AnimPlayer;
         ModelAttachment var3 = null;

         ModelAttachment var5;
         for(int var4 = 0; var4 < var1.parent.m_modelScript.getAttachmentCount(); ++var4) {
            var5 = var1.parent.getAttachment(var4);
            if (var5.getBone() != null && this.parentBone == var2.getSkinningBoneIndex(var5.getBone(), 0)) {
               var3 = var5;
               break;
            }
         }

         if (var3 != null) {
            Matrix4f var6 = (Matrix4f)((BaseVehicle.Matrix4fObjectPool)BaseVehicle.TL_matrix4f_pool.get()).alloc();
            makeAttachmentTransform(var3, var6);
            this.xfrm.transpose();
            this.xfrm.mul((Matrix4fc)var6);
            this.xfrm.transpose();
            var5 = var1.getAttachmentById(var3.getId());
            if (var5 != null) {
               makeAttachmentTransform(var5, var6);
               var6.invert();
               this.xfrm.transpose();
               this.xfrm.mul((Matrix4fc)var6);
               this.xfrm.transpose();
            }

            ((BaseVehicle.Matrix4fObjectPool)BaseVehicle.TL_matrix4f_pool.get()).release(var6);
         }
      }
   }

   public static ModelInstanceRenderData alloc() {
      return (ModelInstanceRenderData)pool.alloc();
   }

   public static void release(ArrayList var0) {
      for(int var1 = 0; var1 < var0.size(); ++var1) {
         ModelInstanceRenderData var2 = (ModelInstanceRenderData)var0.get(var1);
         if (var2.modelInstance.getTextureInitializer() != null) {
            var2.modelInstance.getTextureInitializer().postRender();
         }

         ModelManager.instance.derefModelInstance(var2.modelInstance);
         var2.modelInstance = null;
         var2.model = null;
         var2.tex = null;
         var2.softwareMesh = null;
         var2.m_debugRenderData = (ModelInstanceDebugRenderData)Pool.tryRelease((IPooledObject)var2.m_debugRenderData);
      }

      pool.release((List)var0);
   }
}

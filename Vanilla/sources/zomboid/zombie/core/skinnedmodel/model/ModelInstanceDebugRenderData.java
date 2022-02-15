package zombie.core.skinnedmodel.model;

import java.util.ArrayList;
import org.joml.Matrix4f;
import org.joml.Matrix4fc;
import zombie.core.math.PZMath;
import zombie.core.opengl.PZGLUtil;
import zombie.debug.DebugOptions;
import zombie.scripting.objects.ModelAttachment;
import zombie.scripting.objects.ModelScript;
import zombie.util.Pool;
import zombie.util.PooledObject;
import zombie.vehicles.BaseVehicle;

public final class ModelInstanceDebugRenderData extends PooledObject {
   private static final Pool s_pool = new Pool(ModelInstanceDebugRenderData::new);
   private final ArrayList m_attachmentMatrices = new ArrayList();

   public static ModelInstanceDebugRenderData alloc() {
      return (ModelInstanceDebugRenderData)s_pool.alloc();
   }

   public ModelInstanceDebugRenderData init(ModelSlotRenderData var1, ModelInstanceRenderData var2) {
      this.initAttachments(var1, var2);
      return this;
   }

   public void render() {
      this.renderAttachments();
      if (DebugOptions.instance.ModelRenderAxis.getValue()) {
         Model.debugDrawAxis(0.0F, 0.0F, 0.0F, 1.0F, 1.0F);
      }

   }

   private void initAttachments(ModelSlotRenderData var1, ModelInstanceRenderData var2) {
      BaseVehicle.Matrix4fObjectPool var3 = (BaseVehicle.Matrix4fObjectPool)BaseVehicle.TL_matrix4f_pool.get();
      var3.release(this.m_attachmentMatrices);
      this.m_attachmentMatrices.clear();
      if (DebugOptions.instance.ModelRenderAttachments.getValue()) {
         ModelScript var4 = var2.modelInstance.m_modelScript;
         if (var4 != null) {
            Matrix4f var5 = ((Matrix4f)var3.alloc()).set((Matrix4fc)var2.xfrm);
            Matrix4f var6 = (Matrix4f)var3.alloc();
            var5.transpose();

            for(int var7 = 0; var7 < var4.getAttachmentCount(); ++var7) {
               ModelAttachment var8 = var4.getAttachment(var7);
               Matrix4f var9 = (Matrix4f)var3.alloc();
               var2.modelInstance.getAttachmentMatrix(var8, var9);
               if (!var2.model.bStatic && var8.getBone() != null) {
                  if (var1.animPlayer != null && var1.animPlayer.hasSkinningData()) {
                     int var10 = var1.animPlayer.getSkinningBoneIndex(var8.getBone(), 0);
                     org.lwjgl.util.vector.Matrix4f var11 = var1.animPlayer.modelTransforms[var10];
                     PZMath.convertMatrix(var11, var6);
                     var6.transpose();
                     var6.mul((Matrix4fc)var9, var9);
                     var5.mul((Matrix4fc)var9, var9);
                  }
               } else {
                  var5.mul((Matrix4fc)var9, var9);
               }

               this.m_attachmentMatrices.add(var9);
            }

            var3.release(var6);
            var3.release(var5);
         }
      }
   }

   private void renderAttachments() {
      for(int var1 = 0; var1 < this.m_attachmentMatrices.size(); ++var1) {
         Matrix4f var2 = (Matrix4f)this.m_attachmentMatrices.get(var1);
         PZGLUtil.pushAndMultMatrix(5888, var2);
         Model.debugDrawAxis(0.0F, 0.0F, 0.0F, 0.05F, 1.0F);
         PZGLUtil.popMatrix(5888);
      }

   }
}

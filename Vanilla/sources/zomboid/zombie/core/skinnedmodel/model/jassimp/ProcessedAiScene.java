package zombie.core.skinnedmodel.model.jassimp;

import jassimp.AiBuiltInWrapperProvider;
import jassimp.AiMatrix4f;
import jassimp.AiMesh;
import jassimp.AiNode;
import jassimp.AiScene;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import org.lwjgl.util.vector.Matrix4f;
import zombie.core.math.PZMath;
import zombie.core.opengl.RenderThread;
import zombie.core.skinnedmodel.ModelManager;
import zombie.core.skinnedmodel.animation.AnimationClip;
import zombie.core.skinnedmodel.animation.Keyframe;
import zombie.core.skinnedmodel.model.AnimationAsset;
import zombie.core.skinnedmodel.model.ModelMesh;
import zombie.core.skinnedmodel.model.SkinningData;
import zombie.core.skinnedmodel.model.VertexBufferObject;
import zombie.debug.DebugLog;
import zombie.util.StringUtils;

public final class ProcessedAiScene {
   private ImportedSkeleton skeleton;
   private ImportedSkinnedMesh skinnedMesh;
   private ImportedStaticMesh staticMesh;
   private Matrix4f transform = null;

   private ProcessedAiScene() {
   }

   public static ProcessedAiScene process(ProcessedAiSceneParams var0) {
      ProcessedAiScene var1 = new ProcessedAiScene();
      var1.processAiScene(var0);
      return var1;
   }

   private void processAiScene(ProcessedAiSceneParams var1) {
      AiScene var2 = var1.scene;
      JAssImpImporter.LoadMode var3 = var1.mode;
      String var4 = var1.meshName;
      AiMesh var5 = this.findMesh(var2, var4);
      if (var5 == null) {
         DebugLog.General.error("No such mesh \"%s\"", var4);
      } else {
         if (var3 != JAssImpImporter.LoadMode.StaticMesh && var5.hasBones()) {
            ImportedSkeletonParams var6 = ImportedSkeletonParams.create(var1, var5);
            this.skeleton = ImportedSkeleton.process(var6);
            if (var3 != JAssImpImporter.LoadMode.AnimationOnly) {
               this.skinnedMesh = new ImportedSkinnedMesh(this.skeleton, var5);
            }
         } else {
            this.staticMesh = new ImportedStaticMesh(var5);
         }

         if (this.staticMesh != null || this.skinnedMesh != null) {
            AiBuiltInWrapperProvider var11 = new AiBuiltInWrapperProvider();
            AiNode var7 = (AiNode)var2.getSceneRoot(var11);
            AiNode var8 = this.findParentNodeForMesh(var2.getMeshes().indexOf(var5), var7);
            if (var8 != null) {
               this.transform = JAssImpImporter.getMatrixFromAiMatrix((AiMatrix4f)var8.getTransform(var11));

               for(AiNode var9 = var8.getParent(); var9 != null; var9 = var9.getParent()) {
                  Matrix4f var10 = JAssImpImporter.getMatrixFromAiMatrix((AiMatrix4f)var9.getTransform(var11));
                  Matrix4f.mul(var10, this.transform, this.transform);
               }

            }
         }
      }
   }

   private AiMesh findMesh(AiScene var1, String var2) {
      if (var1.getNumMeshes() == 0) {
         return null;
      } else {
         Iterator var3;
         AiMesh var4;
         if (StringUtils.isNullOrWhitespace(var2)) {
            var3 = var1.getMeshes().iterator();

            do {
               if (!var3.hasNext()) {
                  return (AiMesh)var1.getMeshes().get(0);
               }

               var4 = (AiMesh)var3.next();
            } while(!var4.hasBones());

            return var4;
         } else {
            var3 = var1.getMeshes().iterator();

            do {
               if (!var3.hasNext()) {
                  AiBuiltInWrapperProvider var7 = new AiBuiltInWrapperProvider();
                  AiNode var8 = (AiNode)var1.getSceneRoot(var7);
                  AiNode var5 = JAssImpImporter.FindNode(var2, var8);
                  if (var5 != null && var5.getNumMeshes() == 1) {
                     int var6 = var5.getMeshes()[0];
                     return (AiMesh)var1.getMeshes().get(var6);
                  }

                  return null;
               }

               var4 = (AiMesh)var3.next();
            } while(!var4.getName().equalsIgnoreCase(var2));

            return var4;
         }
      }
   }

   private AiNode findParentNodeForMesh(int var1, AiNode var2) {
      for(int var3 = 0; var3 < var2.getNumMeshes(); ++var3) {
         if (var2.getMeshes()[var3] == var1) {
            return var2;
         }
      }

      Iterator var6 = var2.getChildren().iterator();

      AiNode var5;
      do {
         if (!var6.hasNext()) {
            return null;
         }

         AiNode var4 = (AiNode)var6.next();
         var5 = this.findParentNodeForMesh(var1, var4);
      } while(var5 == null);

      return var5;
   }

   public void applyToMesh(ModelMesh var1, JAssImpImporter.LoadMode var2, boolean var3, SkinningData var4) {
      var1.m_transform = null;
      if (this.transform != null) {
         var1.m_transform = PZMath.convertMatrix(this.transform, new org.joml.Matrix4f());
      }

      VertexBufferObject.VertexArray var5;
      int[] var6;
      if (this.staticMesh != null && !ModelManager.NoOpenGL) {
         var5 = this.staticMesh.verticesUnskinned;
         var6 = this.staticMesh.elements;
         RenderThread.queueInvokeOnRenderContext(() -> {
            var1.SetVertexBuffer(new VertexBufferObject(var5, var6));
            if (ModelManager.instance.bCreateSoftwareMeshes) {
               var1.softwareMesh.vb = var1.vb;
            }

         });
      }

      if (var1.skinningData != null) {
         if (var4 == null || var1.skinningData.AnimationClips != var4.AnimationClips) {
            var1.skinningData.AnimationClips.clear();
         }

         var1.skinningData.InverseBindPose.clear();
         var1.skinningData.BindPose.clear();
         var1.skinningData.BoneOffset.clear();
         var1.skinningData.BoneIndices.clear();
         var1.skinningData.SkeletonHierarchy.clear();
         var1.skinningData = null;
      }

      if (this.skeleton != null) {
         ImportedSkeleton var7 = this.skeleton;
         HashMap var8 = var7.clips;
         if (var4 != null) {
            var7.clips.clear();
            var8 = var4.AnimationClips;
         }

         JAssImpImporter.replaceHashMapKeys(var7.boneIndices, "SkinningData.boneIndices");
         var1.skinningData = new SkinningData(var8, var7.bindPose, var7.invBindPose, var7.skinOffsetMatrices, var7.SkeletonHierarchy, var7.boneIndices);
      }

      if (this.skinnedMesh != null && !ModelManager.NoOpenGL) {
         var5 = this.skinnedMesh.vertices;
         var6 = this.skinnedMesh.elements;
         RenderThread.queueInvokeOnRenderContext(() -> {
            var1.SetVertexBuffer(new VertexBufferObject(var5, var6, var3));
            if (ModelManager.instance.bCreateSoftwareMeshes) {
               var1.softwareMesh.vb = var1.vb;
            }

         });
      }

      this.skeleton = null;
      this.skinnedMesh = null;
      this.staticMesh = null;
   }

   public void applyToAnimation(AnimationAsset var1) {
      Iterator var2 = this.skeleton.clips.entrySet().iterator();

      while(var2.hasNext()) {
         Entry var3 = (Entry)var2.next();
         Keyframe[] var4 = ((AnimationClip)var3.getValue()).getKeyframes();
         int var5 = var4.length;

         for(int var6 = 0; var6 < var5; ++var6) {
            Keyframe var7 = var4[var6];
            var7.BoneName = JAssImpImporter.getSharedString(var7.BoneName, "Keyframe.BoneName");
         }
      }

      var1.AnimationClips = this.skeleton.clips;
      this.skeleton = null;
   }
}

package zombie.core.skinnedmodel.model;

import jassimp.AiPostProcessSteps;
import jassimp.AiScene;
import jassimp.Jassimp;
import java.io.IOException;
import java.util.EnumSet;
import org.lwjgl.util.vector.Quaternion;
import org.lwjgl.util.vector.Vector4f;
import zombie.core.skinnedmodel.model.jassimp.JAssImpImporter;
import zombie.core.skinnedmodel.model.jassimp.ProcessedAiScene;
import zombie.core.skinnedmodel.model.jassimp.ProcessedAiSceneParams;
import zombie.debug.DebugLog;
import zombie.fileSystem.FileSystem;
import zombie.fileSystem.IFileTaskCallback;

public class FileTask_LoadMesh extends FileTask_AbstractLoadModel {
   ModelMesh mesh;

   public FileTask_LoadMesh(ModelMesh var1, FileSystem var2, IFileTaskCallback var3) {
      super(var2, var3, "media/models", "media/models_x");
      this.mesh = var1;
   }

   public String getErrorMessage() {
      return this.m_fileName;
   }

   public void done() {
      MeshAssetManager.instance.addWatchedFile(this.m_fileName);
      this.mesh.m_fullPath = this.m_fileName;
      this.m_fileName = null;
      this.mesh = null;
   }

   public String getRawFileName() {
      String var1 = this.mesh.getPath().getPath();
      int var2 = var1.indexOf(124);
      return var2 != -1 ? var1.substring(0, var2) : var1;
   }

   private String getMeshName() {
      String var1 = this.mesh.getPath().getPath();
      int var2 = var1.indexOf(124);
      return var2 != -1 ? var1.substring(var2 + 1) : null;
   }

   public ProcessedAiScene loadX() throws IOException {
      EnumSet var1 = EnumSet.of(AiPostProcessSteps.FIND_INSTANCES, AiPostProcessSteps.MAKE_LEFT_HANDED, AiPostProcessSteps.LIMIT_BONE_WEIGHTS, AiPostProcessSteps.TRIANGULATE, AiPostProcessSteps.OPTIMIZE_MESHES, AiPostProcessSteps.REMOVE_REDUNDANT_MATERIALS, AiPostProcessSteps.JOIN_IDENTICAL_VERTICES);
      AiScene var2 = Jassimp.importFile(this.m_fileName, var1);
      JAssImpImporter.LoadMode var3 = this.mesh.assetParams.bStatic ? JAssImpImporter.LoadMode.StaticMesh : JAssImpImporter.LoadMode.Normal;
      ModelMesh var4 = this.mesh.assetParams.animationsMesh;
      SkinningData var5 = var4 == null ? null : var4.skinningData;
      ProcessedAiSceneParams var6 = ProcessedAiSceneParams.create();
      var6.scene = var2;
      var6.mode = var3;
      var6.skinnedTo = var5;
      var6.meshName = this.getMeshName();
      ProcessedAiScene var7 = ProcessedAiScene.process(var6);
      JAssImpImporter.takeOutTheTrash(var2);
      return var7;
   }

   public ProcessedAiScene loadFBX() throws IOException {
      DebugLog.Animation.debugln("Loading: %s", this.m_fileName);
      EnumSet var1 = EnumSet.of(AiPostProcessSteps.FIND_INSTANCES, AiPostProcessSteps.MAKE_LEFT_HANDED, AiPostProcessSteps.LIMIT_BONE_WEIGHTS, AiPostProcessSteps.TRIANGULATE, AiPostProcessSteps.OPTIMIZE_MESHES, AiPostProcessSteps.REMOVE_REDUNDANT_MATERIALS, AiPostProcessSteps.JOIN_IDENTICAL_VERTICES);
      AiScene var2 = Jassimp.importFile(this.m_fileName, var1);
      JAssImpImporter.LoadMode var3 = this.mesh.assetParams.bStatic ? JAssImpImporter.LoadMode.StaticMesh : JAssImpImporter.LoadMode.Normal;
      ModelMesh var4 = this.mesh.assetParams.animationsMesh;
      SkinningData var5 = var4 == null ? null : var4.skinningData;
      Quaternion var6 = new Quaternion();
      Vector4f var7 = new Vector4f(1.0F, 0.0F, 0.0F, -1.5707964F);
      var6.setFromAxisAngle(var7);
      ProcessedAiSceneParams var8 = ProcessedAiSceneParams.create();
      var8.scene = var2;
      var8.mode = var3;
      var8.skinnedTo = var5;
      var8.meshName = this.getMeshName();
      var8.animBonesScaleModifier = 0.01F;
      var8.animBonesRotateModifier = var6;
      ProcessedAiScene var9 = ProcessedAiScene.process(var8);
      JAssImpImporter.takeOutTheTrash(var2);
      return var9;
   }

   public ModelTxt loadTxt() throws IOException {
      boolean var1 = this.mesh.assetParams.bStatic;
      boolean var2 = false;
      ModelMesh var3 = this.mesh.assetParams.animationsMesh;
      SkinningData var4 = var3 == null ? null : var3.skinningData;
      return ModelLoader.instance.loadTxt(this.m_fileName, var1, var2, var4);
   }

   static enum LoadMode {
      Assimp,
      Txt,
      Missing;

      // $FF: synthetic method
      private static FileTask_LoadMesh.LoadMode[] $values() {
         return new FileTask_LoadMesh.LoadMode[]{Assimp, Txt, Missing};
      }
   }
}

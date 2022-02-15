package zombie.core.skinnedmodel.model.jassimp;

import jassimp.AiMesh;

public class ImportedSkeletonParams extends ProcessedAiSceneParams {
   AiMesh mesh = null;

   ImportedSkeletonParams() {
   }

   public static ImportedSkeletonParams create(ProcessedAiSceneParams var0, AiMesh var1) {
      ImportedSkeletonParams var2 = new ImportedSkeletonParams();
      var2.set(var0);
      var2.mesh = var1;
      return var2;
   }
}

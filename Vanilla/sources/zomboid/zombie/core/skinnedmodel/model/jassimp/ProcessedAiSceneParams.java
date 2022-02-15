package zombie.core.skinnedmodel.model.jassimp;

import jassimp.AiScene;
import org.lwjgl.util.vector.Quaternion;
import zombie.core.skinnedmodel.model.SkinningData;

public class ProcessedAiSceneParams {
   public AiScene scene = null;
   public JAssImpImporter.LoadMode mode;
   public SkinningData skinnedTo;
   public String meshName;
   public float animBonesScaleModifier;
   public Quaternion animBonesRotateModifier;

   ProcessedAiSceneParams() {
      this.mode = JAssImpImporter.LoadMode.Normal;
      this.skinnedTo = null;
      this.meshName = null;
      this.animBonesScaleModifier = 1.0F;
      this.animBonesRotateModifier = null;
   }

   public static ProcessedAiSceneParams create() {
      return new ProcessedAiSceneParams();
   }

   protected void set(ProcessedAiSceneParams var1) {
      this.scene = var1.scene;
      this.mode = var1.mode;
      this.skinnedTo = var1.skinnedTo;
      this.meshName = var1.meshName;
      this.animBonesScaleModifier = var1.animBonesScaleModifier;
      this.animBonesRotateModifier = var1.animBonesRotateModifier;
   }
}

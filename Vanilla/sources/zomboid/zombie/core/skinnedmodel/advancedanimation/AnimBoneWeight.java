package zombie.core.skinnedmodel.advancedanimation;

public final class AnimBoneWeight {
   public String boneName;
   public float weight = 1.0F;
   public boolean includeDescendants = true;

   public AnimBoneWeight() {
   }

   public AnimBoneWeight(String var1, float var2) {
      this.boneName = var1;
      this.weight = var2;
      this.includeDescendants = true;
   }
}

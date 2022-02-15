package zombie.core.skinnedmodel.animation;

import java.util.function.Consumer;
import zombie.core.skinnedmodel.advancedanimation.AnimBoneWeight;
import zombie.core.skinnedmodel.model.SkinningBone;

public class AnimationBoneWeightBinding extends AnimationBoneBinding {
   private float m_weight;
   private boolean m_includeDescendants;

   public AnimationBoneWeightBinding(AnimBoneWeight var1) {
      this(var1.boneName, var1.weight, var1.includeDescendants);
   }

   public AnimationBoneWeightBinding(String var1, float var2, boolean var3) {
      super(var1);
      this.m_weight = 1.0F;
      this.m_includeDescendants = true;
      this.m_weight = var2;
      this.m_includeDescendants = var3;
   }

   public float getWeight() {
      return this.m_weight;
   }

   public void setWeight(float var1) {
      this.m_weight = var1;
   }

   public boolean getIncludeDescendants() {
      return this.m_includeDescendants;
   }

   public void setIncludeDescendants(boolean var1) {
      this.m_includeDescendants = var1;
   }

   public void forEachDescendant(Consumer var1) {
      if (this.m_includeDescendants) {
         SkinningBone var2 = this.getBone();
         if (var2 != null) {
            var2.forEachDescendant(var1);
         }
      }
   }
}

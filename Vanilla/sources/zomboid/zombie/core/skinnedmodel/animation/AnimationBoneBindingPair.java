package zombie.core.skinnedmodel.animation;

import zombie.core.skinnedmodel.model.SkinningBone;
import zombie.core.skinnedmodel.model.SkinningData;
import zombie.util.StringUtils;

public final class AnimationBoneBindingPair {
   public final AnimationBoneBinding boneBindingA;
   public final AnimationBoneBinding boneBindingB;

   public AnimationBoneBindingPair(String var1, String var2) {
      this.boneBindingA = new AnimationBoneBinding(var1);
      this.boneBindingB = new AnimationBoneBinding(var2);
   }

   public void setSkinningData(SkinningData var1) {
      this.boneBindingA.setSkinningData(var1);
      this.boneBindingB.setSkinningData(var1);
   }

   public SkinningBone getBoneA() {
      return this.boneBindingA.getBone();
   }

   public SkinningBone getBoneB() {
      return this.boneBindingB.getBone();
   }

   public boolean isValid() {
      return this.getBoneA() != null && this.getBoneB() != null;
   }

   public boolean matches(String var1, String var2) {
      return StringUtils.equalsIgnoreCase(this.boneBindingA.boneName, var1) && StringUtils.equalsIgnoreCase(this.boneBindingB.boneName, var2);
   }

   public int getBoneIdxA() {
      return getBoneIdx(this.getBoneA());
   }

   public int getBoneIdxB() {
      return getBoneIdx(this.getBoneB());
   }

   private static int getBoneIdx(SkinningBone var0) {
      return var0 != null ? var0.Index : -1;
   }

   public String toString() {
      String var1 = System.lineSeparator();
      String var10000 = this.getClass().getName();
      return var10000 + var1 + "{" + var1 + "\tboneBindingA:" + StringUtils.indent(String.valueOf(this.boneBindingA)) + var1 + "\tboneBindingB:" + StringUtils.indent(String.valueOf(this.boneBindingB)) + var1 + "}";
   }
}

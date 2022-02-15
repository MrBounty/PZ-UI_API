package zombie.core.skinnedmodel.animation;

import zombie.util.Pool;

public final class TwistableBoneTransform extends BoneTransform {
   public float BlendWeight = 0.0F;
   public float Twist = 0.0F;
   private static final Pool s_pool = new Pool(TwistableBoneTransform::new);

   protected TwistableBoneTransform() {
   }

   public static TwistableBoneTransform alloc() {
      return (TwistableBoneTransform)s_pool.alloc();
   }
}

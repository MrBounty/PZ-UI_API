package zombie.core.skinnedmodel.animation.sharedskele;

import java.util.HashMap;
import zombie.core.skinnedmodel.animation.AnimationClip;

public class SharedSkeleAnimationRepository {
   private final HashMap m_tracksMap = new HashMap();

   public SharedSkeleAnimationTrack getTrack(AnimationClip var1) {
      return (SharedSkeleAnimationTrack)this.m_tracksMap.get(var1);
   }

   public void setTrack(AnimationClip var1, SharedSkeleAnimationTrack var2) {
      this.m_tracksMap.put(var1, var2);
   }
}

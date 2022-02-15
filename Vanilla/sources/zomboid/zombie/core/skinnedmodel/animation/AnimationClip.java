package zombie.core.skinnedmodel.animation;

import java.util.ArrayList;
import java.util.List;
import org.lwjgl.util.vector.Quaternion;

public final class AnimationClip {
   public final String Name;
   public StaticAnimation staticClip;
   private final AnimationClip.KeyframeByBoneIndexElement[] m_KeyFramesByBoneIndex;
   public float Duration;
   private final List m_rootMotionKeyframes = new ArrayList();
   private final Keyframe[] KeyframeArray;
   private static final Quaternion orientation = new Quaternion(-0.07107F, 0.0F, 0.0F, 0.07107F);

   public AnimationClip(float var1, List var2, String var3, boolean var4) {
      this.Duration = var1;
      this.KeyframeArray = (Keyframe[])var2.toArray(new Keyframe[0]);
      this.Name = var3;
      this.m_KeyFramesByBoneIndex = new AnimationClip.KeyframeByBoneIndexElement[60];
      ArrayList var5 = new ArrayList();
      int var6 = this.KeyframeArray.length - (var4 ? 0 : 1);

      for(int var7 = 0; var7 < 60; ++var7) {
         var5.clear();

         for(int var8 = 0; var8 < var6; ++var8) {
            Keyframe var9 = this.KeyframeArray[var8];
            if (var9.Bone == var7) {
               var5.add(var9);
            }
         }

         this.m_KeyFramesByBoneIndex[var7] = new AnimationClip.KeyframeByBoneIndexElement(var5);
      }

   }

   public Keyframe[] getBoneFramesAt(int var1) {
      return this.m_KeyFramesByBoneIndex[var1].m_keyframes;
   }

   public int getRootMotionFrameCount() {
      return this.m_rootMotionKeyframes.size();
   }

   public Keyframe getRootMotionFrameAt(int var1) {
      return (Keyframe)this.m_rootMotionKeyframes.get(var1);
   }

   public Keyframe[] getKeyframes() {
      return this.KeyframeArray;
   }

   private static class KeyframeByBoneIndexElement {
      final Keyframe[] m_keyframes;

      KeyframeByBoneIndexElement(List var1) {
         this.m_keyframes = (Keyframe[])var1.toArray(new Keyframe[0]);
      }
   }
}

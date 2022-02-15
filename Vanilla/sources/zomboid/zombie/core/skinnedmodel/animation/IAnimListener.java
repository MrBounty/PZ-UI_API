package zombie.core.skinnedmodel.animation;

public interface IAnimListener {
   void onAnimStarted(AnimationTrack var1);

   void onLoopedAnim(AnimationTrack var1);

   void onNonLoopedAnimFadeOut(AnimationTrack var1);

   void onNonLoopedAnimFinished(AnimationTrack var1);

   void onTrackDestroyed(AnimationTrack var1);
}

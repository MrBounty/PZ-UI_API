package zombie.core.skinnedmodel.advancedanimation;

import java.util.ArrayList;
import java.util.List;
import zombie.core.Rand;
import zombie.core.math.PZMath;
import zombie.core.skinnedmodel.animation.AnimationTrack;
import zombie.core.skinnedmodel.animation.BoneAxis;
import zombie.core.skinnedmodel.animation.IAnimListener;
import zombie.debug.DebugOptions;
import zombie.util.Lambda;
import zombie.util.Pool;
import zombie.util.PooledObject;
import zombie.util.StringUtils;
import zombie.util.list.PZArrayUtil;

public class LiveAnimNode extends PooledObject implements IAnimListener {
   private AnimNode m_sourceNode;
   private AnimLayer m_animLayer;
   private boolean m_active;
   private boolean m_wasActive;
   boolean m_TransitioningOut;
   private float m_Weight;
   private float m_RawWeight;
   private boolean m_isNew;
   private int m_layerIdx;
   private final LiveAnimNode.TransitionIn m_transitionIn = new LiveAnimNode.TransitionIn();
   final List m_AnimationTracks = new ArrayList();
   float m_NodeAnimTime;
   float m_PrevNodeAnimTime;
   private boolean m_blendingIn;
   private boolean m_blendingOut;
   private AnimTransition m_transitionOut;
   private static final Pool s_pool = new Pool(LiveAnimNode::new);

   protected LiveAnimNode() {
   }

   public static LiveAnimNode alloc(AnimLayer var0, AnimNode var1, int var2) {
      LiveAnimNode var3 = (LiveAnimNode)s_pool.alloc();
      var3.reset();
      var3.m_sourceNode = var1;
      var3.m_animLayer = var0;
      var3.m_layerIdx = var2;
      return var3;
   }

   private void reset() {
      this.m_sourceNode = null;
      this.m_animLayer = null;
      this.m_active = false;
      this.m_wasActive = false;
      this.m_TransitioningOut = false;
      this.m_Weight = 0.0F;
      this.m_RawWeight = 0.0F;
      this.m_isNew = true;
      this.m_layerIdx = -1;
      this.m_transitionIn.reset();
      this.m_AnimationTracks.clear();
      this.m_NodeAnimTime = 0.0F;
      this.m_PrevNodeAnimTime = 0.0F;
      this.m_blendingIn = false;
      this.m_blendingOut = false;
      this.m_transitionOut = null;
   }

   public void onReleased() {
      this.reset();
   }

   public String getName() {
      return this.m_sourceNode.m_Name;
   }

   public boolean isTransitioningIn() {
      return this.m_transitionIn.m_active && this.m_transitionIn.m_track != null;
   }

   public void startTransitionIn(LiveAnimNode var1, AnimTransition var2, AnimationTrack var3) {
      this.startTransitionIn(var1.getSourceNode(), var2, var3);
   }

   public void startTransitionIn(AnimNode var1, AnimTransition var2, AnimationTrack var3) {
      this.m_transitionIn.m_active = var3 != null;
      this.m_transitionIn.m_transitionedFrom = var1.m_Name;
      this.m_transitionIn.m_data = var2;
      this.m_transitionIn.m_track = var3;
      this.m_transitionIn.m_weight = 0.0F;
      this.m_transitionIn.m_rawWeight = 0.0F;
      this.m_transitionIn.m_blendingIn = true;
      this.m_transitionIn.m_blendingOut = false;
      this.m_transitionIn.m_time = 0.0F;
      if (this.m_transitionIn.m_track != null) {
         this.m_transitionIn.m_track.addListener(this);
      }

      this.setMainTracksPlaying(false);
   }

   public void setTransitionOut(AnimTransition var1) {
      this.m_transitionOut = var1;
   }

   public void update(float var1) {
      this.m_isNew = false;
      if (this.m_active != this.m_wasActive) {
         this.m_blendingIn = this.m_active;
         this.m_blendingOut = !this.m_active;
         if (this.m_transitionIn.m_active) {
            this.m_transitionIn.m_blendingIn = this.m_active;
            this.m_transitionIn.m_blendingOut = !this.m_active;
         }

         this.m_wasActive = this.m_active;
      }

      boolean var2 = this.isMainAnimActive();
      if (this.isTransitioningIn()) {
         this.updateTransitioningIn(var1);
      }

      boolean var3 = this.isMainAnimActive();
      if (var3) {
         if (this.m_blendingOut && this.m_sourceNode.m_StopAnimOnExit) {
            this.setMainTracksPlaying(false);
         } else {
            this.setMainTracksPlaying(true);
         }
      } else {
         this.setMainTracksPlaying(false);
      }

      if (var3) {
         boolean var4 = !var2;
         if (var4 && this.isLooped()) {
            float var5 = this.getMainInitialRewindTime();
            PZArrayUtil.forEach(this.m_AnimationTracks, Lambda.consumer(var5, AnimationTrack::scaledRewind));
         }

         if (this.m_blendingIn) {
            this.updateBlendingIn(var1);
         } else if (this.m_blendingOut) {
            this.updateBlendingOut(var1);
         }

         this.m_PrevNodeAnimTime = this.m_NodeAnimTime;
         this.m_NodeAnimTime += var1;
         if (!this.m_transitionIn.m_active && this.m_transitionIn.m_track != null && this.m_transitionIn.m_track.BlendDelta <= 0.0F) {
            this.m_animLayer.getAnimationTrack().removeTrack(this.m_transitionIn.m_track);
            this.m_transitionIn.reset();
         }

      }
   }

   private void updateTransitioningIn(float var1) {
      float var2 = this.m_transitionIn.m_track.SpeedDelta;
      float var3 = this.m_transitionIn.m_track.getDuration();
      this.m_transitionIn.m_time = this.m_transitionIn.m_track.getCurrentTimeValue();
      if (this.m_transitionIn.m_time >= var3) {
         this.m_transitionIn.m_active = false;
         this.m_transitionIn.m_weight = 0.0F;
      } else {
         if (!this.m_transitionIn.m_blendingOut) {
            boolean var4 = AnimCondition.pass(this.m_animLayer.getVariableSource(), this.m_transitionIn.m_data.m_Conditions);
            if (!var4) {
               this.m_transitionIn.m_blendingIn = false;
               this.m_transitionIn.m_blendingOut = true;
            }
         }

         float var8 = this.getTransitionInBlendOutTime() * var2;
         if (this.m_transitionIn.m_time >= var3 - var8) {
            this.m_transitionIn.m_blendingIn = false;
            this.m_transitionIn.m_blendingOut = true;
         }

         float var5;
         float var6;
         float var7;
         if (this.m_transitionIn.m_blendingIn) {
            var5 = this.getTransitionInBlendInTime() * var2;
            var6 = this.incrementBlendTime(this.m_transitionIn.m_rawWeight, var5, var1 * var2);
            var7 = PZMath.clamp(var6 / var5, 0.0F, 1.0F);
            this.m_transitionIn.m_rawWeight = var7;
            this.m_transitionIn.m_weight = PZMath.lerpFunc_EaseOutInQuad(var7);
            this.m_transitionIn.m_blendingIn = var6 < var5;
            this.m_transitionIn.m_active = var6 < var3;
         }

         if (this.m_transitionIn.m_blendingOut) {
            var5 = this.getTransitionInBlendOutTime() * var2;
            var6 = this.incrementBlendTime(1.0F - this.m_transitionIn.m_rawWeight, var5, var1 * var2);
            var7 = PZMath.clamp(1.0F - var6 / var5, 0.0F, 1.0F);
            this.m_transitionIn.m_rawWeight = var7;
            this.m_transitionIn.m_weight = PZMath.lerpFunc_EaseOutInQuad(var7);
            this.m_transitionIn.m_blendingOut = var6 < var5;
            this.m_transitionIn.m_active = this.m_transitionIn.m_blendingOut;
         }

      }
   }

   public void addMainTrack(AnimationTrack var1) {
      if (!this.isLooped() && !this.m_sourceNode.m_StopAnimOnExit && this.m_sourceNode.m_EarlyTransitionOut) {
         float var2 = this.getBlendOutTime();
         if (var2 > 0.0F && Float.isFinite(var2)) {
            var1.earlyBlendOutTime = var2;
            var1.triggerOnNonLoopedAnimFadeOutEvent = true;
         }
      }

      this.m_AnimationTracks.add(var1);
   }

   private void setMainTracksPlaying(boolean var1) {
      Lambda.forEachFrom(PZArrayUtil::forEach, (List)this.m_AnimationTracks, var1, (var0, var1x) -> {
         var0.IsPlaying = var1x;
      });
   }

   private void updateBlendingIn(float var1) {
      float var2 = this.getBlendInTime();
      if (var2 <= 0.0F) {
         this.m_Weight = 1.0F;
         this.m_RawWeight = 1.0F;
         this.m_blendingIn = false;
      } else {
         float var3 = this.incrementBlendTime(this.m_RawWeight, var2, var1);
         float var4 = PZMath.clamp(var3 / var2, 0.0F, 1.0F);
         this.m_RawWeight = var4;
         this.m_Weight = PZMath.lerpFunc_EaseOutInQuad(var4);
         this.m_blendingIn = var3 < var2;
      }
   }

   private void updateBlendingOut(float var1) {
      float var2 = this.getBlendOutTime();
      if (var2 <= 0.0F) {
         this.m_Weight = 0.0F;
         this.m_RawWeight = 0.0F;
         this.m_blendingOut = false;
      } else {
         float var3 = this.incrementBlendTime(1.0F - this.m_RawWeight, var2, var1);
         float var4 = PZMath.clamp(1.0F - var3 / var2, 0.0F, 1.0F);
         this.m_RawWeight = var4;
         this.m_Weight = PZMath.lerpFunc_EaseOutInQuad(var4);
         this.m_blendingOut = var3 < var2;
      }
   }

   private float incrementBlendTime(float var1, float var2, float var3) {
      float var4 = var1 * var2;
      return var4 + var3;
   }

   public float getTransitionInBlendInTime() {
      return this.m_transitionIn.m_data != null && this.m_transitionIn.m_data.m_blendInTime != Float.POSITIVE_INFINITY ? this.m_transitionIn.m_data.m_blendInTime : 0.0F;
   }

   public float getMainInitialRewindTime() {
      float var1 = 0.0F;
      float var2;
      if (this.m_sourceNode.m_randomAdvanceFraction > 0.0F) {
         var2 = Rand.Next(0.0F, this.m_sourceNode.m_randomAdvanceFraction);
         var1 = var2 * this.getMaxDuration();
      }

      if (this.m_transitionIn.m_data == null) {
         return 0.0F - var1;
      } else {
         var2 = this.getTransitionInBlendOutTime();
         float var3 = this.m_transitionIn.m_data.m_SyncAdjustTime;
         return this.m_transitionIn.m_track != null ? var2 - var3 : var2 - var3 - var1;
      }
   }

   private float getMaxDuration() {
      float var1 = 0.0F;
      int var2 = 0;

      for(int var3 = this.m_AnimationTracks.size(); var2 < var3; ++var2) {
         AnimationTrack var4 = (AnimationTrack)this.m_AnimationTracks.get(var2);
         float var5 = var4.getDuration();
         var1 = PZMath.max(var5, var1);
      }

      return var1;
   }

   public float getTransitionInBlendOutTime() {
      return this.getBlendInTime();
   }

   public float getBlendInTime() {
      if (this.m_transitionIn.m_data == null) {
         return this.m_sourceNode.m_BlendTime;
      } else if (this.m_transitionIn.m_track != null && this.m_transitionIn.m_data.m_blendOutTime != Float.POSITIVE_INFINITY) {
         return this.m_transitionIn.m_data.m_blendOutTime;
      } else {
         if (this.m_transitionIn.m_track == null) {
            if (this.m_transitionIn.m_data.m_blendInTime != Float.POSITIVE_INFINITY) {
               return this.m_transitionIn.m_data.m_blendInTime;
            }

            if (this.m_transitionIn.m_data.m_blendOutTime != Float.POSITIVE_INFINITY) {
               return this.m_transitionIn.m_data.m_blendOutTime;
            }
         }

         return this.m_sourceNode.m_BlendTime;
      }
   }

   public float getBlendOutTime() {
      if (this.m_transitionOut == null) {
         return this.m_sourceNode.getBlendOutTime();
      } else if (!StringUtils.isNullOrWhitespace(this.m_transitionOut.m_AnimName) && this.m_transitionOut.m_blendInTime != Float.POSITIVE_INFINITY) {
         return this.m_transitionOut.m_blendInTime;
      } else {
         if (StringUtils.isNullOrWhitespace(this.m_transitionOut.m_AnimName)) {
            if (this.m_transitionOut.m_blendOutTime != Float.POSITIVE_INFINITY) {
               return this.m_transitionOut.m_blendOutTime;
            }

            if (this.m_transitionOut.m_blendInTime != Float.POSITIVE_INFINITY) {
               return this.m_transitionOut.m_blendInTime;
            }
         }

         return this.m_sourceNode.getBlendOutTime();
      }
   }

   public void onAnimStarted(AnimationTrack var1) {
      this.invokeAnimStartTimeEvent();
   }

   public void onLoopedAnim(AnimationTrack var1) {
      if (!this.m_TransitioningOut) {
         this.invokeAnimEndTimeEvent();
      }
   }

   public void onNonLoopedAnimFadeOut(AnimationTrack var1) {
      if (DebugOptions.instance.Animation.AllowEarlyTransitionOut.getValue()) {
         this.invokeAnimEndTimeEvent();
         this.m_TransitioningOut = true;
      }
   }

   public void onNonLoopedAnimFinished(AnimationTrack var1) {
      if (!this.m_TransitioningOut) {
         this.invokeAnimEndTimeEvent();
      }
   }

   public void onTrackDestroyed(AnimationTrack var1) {
      this.m_AnimationTracks.remove(var1);
      if (this.m_transitionIn.m_track == var1) {
         this.m_transitionIn.m_track = null;
         this.m_transitionIn.m_active = false;
         this.m_transitionIn.m_weight = 0.0F;
         this.setMainTracksPlaying(true);
      }

   }

   private void invokeAnimStartTimeEvent() {
      this.invokeAnimTimeEvent(AnimEvent.AnimEventTime.Start);
   }

   private void invokeAnimEndTimeEvent() {
      this.invokeAnimTimeEvent(AnimEvent.AnimEventTime.End);
   }

   private void invokeAnimTimeEvent(AnimEvent.AnimEventTime var1) {
      List var2 = this.getSourceNode().m_Events;
      int var3 = 0;

      for(int var4 = var2.size(); var3 < var4; ++var3) {
         AnimEvent var5 = (AnimEvent)var2.get(var3);
         if (var5.m_Time == var1) {
            this.m_animLayer.invokeAnimEvent(var5);
         }
      }

   }

   public AnimNode getSourceNode() {
      return this.m_sourceNode;
   }

   public boolean isIdleAnimActive() {
      return this.m_active && this.m_sourceNode.isIdleAnim();
   }

   public boolean isActive() {
      return this.m_active;
   }

   public void setActive(boolean var1) {
      this.m_active = var1;
   }

   public boolean isLooped() {
      return this.m_sourceNode.m_Looped;
   }

   public float getWeight() {
      return this.m_Weight;
   }

   public float getTransitionInWeight() {
      return this.m_transitionIn.m_weight;
   }

   public boolean wasActivated() {
      return this.m_active != this.m_wasActive && this.m_active;
   }

   public boolean wasDeactivated() {
      return this.m_active != this.m_wasActive && this.m_wasActive;
   }

   public boolean isNew() {
      return this.m_isNew;
   }

   public int getPlayingTrackCount() {
      int var1 = 0;
      if (this.isMainAnimActive()) {
         var1 += this.m_AnimationTracks.size();
      }

      if (this.isTransitioningIn()) {
         ++var1;
      }

      return var1;
   }

   public boolean isMainAnimActive() {
      return !this.isTransitioningIn() || this.m_transitionIn.m_blendingOut;
   }

   public AnimationTrack getPlayingTrackAt(int var1) {
      int var2 = this.getPlayingTrackCount();
      if (var1 >= 0 && var1 < var2) {
         return this.isTransitioningIn() && var1 == var2 - 1 ? this.m_transitionIn.m_track : (AnimationTrack)this.m_AnimationTracks.get(var1);
      } else {
         throw new IndexOutOfBoundsException("TrackIdx out of bounds 0 - " + this.getPlayingTrackCount());
      }
   }

   public String getTransitionFrom() {
      return this.m_transitionIn.m_transitionedFrom;
   }

   public void setTransitionInBlendDelta(float var1) {
      if (this.m_transitionIn.m_track != null) {
         this.m_transitionIn.m_track.BlendDelta = var1;
      }

   }

   public AnimationTrack getTransitionInTrack() {
      return this.m_transitionIn.m_track;
   }

   public int getTransitionLayerIdx() {
      return this.m_transitionIn.m_track != null ? this.m_transitionIn.m_track.getLayerIdx() : -1;
   }

   public int getLayerIdx() {
      return this.m_layerIdx;
   }

   public int getPriority() {
      return this.m_sourceNode.getPriority();
   }

   public String getDeferredBoneName() {
      return this.m_sourceNode.getDeferredBoneName();
   }

   public BoneAxis getDeferredBoneAxis() {
      return this.m_sourceNode.getDeferredBoneAxis();
   }

   public List getSubStateBoneWeights() {
      return this.m_sourceNode.m_SubStateBoneWeights;
   }

   public AnimTransition findTransitionTo(IAnimationVariableSource var1, String var2) {
      return this.m_sourceNode.findTransitionTo(var1, var2);
   }

   public float getSpeedScale(IAnimationVariableSource var1) {
      return this.m_sourceNode.getSpeedScale(var1);
   }

   private static class TransitionIn {
      private float m_time;
      private String m_transitionedFrom;
      private boolean m_active;
      private AnimationTrack m_track;
      private AnimTransition m_data;
      private float m_weight;
      private float m_rawWeight;
      private boolean m_blendingIn;
      private boolean m_blendingOut;

      private void reset() {
         this.m_time = 0.0F;
         this.m_transitionedFrom = null;
         this.m_active = false;
         this.m_track = null;
         this.m_data = null;
         this.m_weight = 0.0F;
         this.m_rawWeight = 0.0F;
         this.m_blendingIn = false;
         this.m_blendingOut = false;
      }
   }
}

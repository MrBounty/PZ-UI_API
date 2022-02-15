package zombie.core.skinnedmodel.animation;

import java.util.ArrayList;
import java.util.List;

public final class AnimationMultiTrack {
   private final ArrayList m_tracks = new ArrayList();
   private static final ArrayList tempTracks = new ArrayList();

   public AnimationTrack findTrack(String var1) {
      int var2 = 0;

      for(int var3 = this.m_tracks.size(); var2 < var3; ++var2) {
         AnimationTrack var4 = (AnimationTrack)this.m_tracks.get(var2);
         if (var4.name.equals(var1)) {
            return var4;
         }
      }

      return null;
   }

   public void addTrack(AnimationTrack var1) {
      this.m_tracks.add(var1);
   }

   public void removeTrack(AnimationTrack var1) {
      int var2 = this.getIndexOfTrack(var1);
      if (var2 > -1) {
         this.removeTrackAt(var2);
      }

   }

   public void removeTracks(List var1) {
      tempTracks.clear();
      tempTracks.addAll(var1);

      for(int var2 = 0; var2 < tempTracks.size(); ++var2) {
         this.removeTrack((AnimationTrack)tempTracks.get(var2));
      }

   }

   public void removeTrackAt(int var1) {
      ((AnimationTrack)this.m_tracks.remove(var1)).release();
   }

   public int getIndexOfTrack(AnimationTrack var1) {
      if (var1 == null) {
         return -1;
      } else {
         int var2 = -1;

         for(int var3 = 0; var3 < this.m_tracks.size(); ++var3) {
            AnimationTrack var4 = (AnimationTrack)this.m_tracks.get(var3);
            if (var4 == var1) {
               var2 = var3;
               break;
            }
         }

         return var2;
      }
   }

   public void Update(float var1) {
      for(int var2 = 0; var2 < this.m_tracks.size(); ++var2) {
         AnimationTrack var3 = (AnimationTrack)this.m_tracks.get(var2);
         var3.Update(var1);
         if (var3.CurrentClip == null) {
            this.removeTrackAt(var2);
            --var2;
         }
      }

   }

   public float getDuration() {
      float var1 = 0.0F;

      for(int var2 = 0; var2 < this.m_tracks.size(); ++var2) {
         AnimationTrack var3 = (AnimationTrack)this.m_tracks.get(var2);
         float var4 = var3.getDuration();
         if (var3.CurrentClip != null && var4 > var1) {
            var1 = var4;
         }
      }

      return var1;
   }

   public void reset() {
      int var1 = 0;

      for(int var2 = this.m_tracks.size(); var1 < var2; ++var1) {
         AnimationTrack var3 = (AnimationTrack)this.m_tracks.get(var1);
         var3.reset();
      }

      AnimationPlayer.releaseTracks(this.m_tracks);
      this.m_tracks.clear();
   }

   public List getTracks() {
      return this.m_tracks;
   }

   public int getTrackCount() {
      return this.m_tracks.size();
   }

   public AnimationTrack getTrackAt(int var1) {
      return (AnimationTrack)this.m_tracks.get(var1);
   }
}

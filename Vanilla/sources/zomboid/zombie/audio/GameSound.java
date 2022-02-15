package zombie.audio;

import java.util.ArrayList;
import zombie.SystemDisabler;
import zombie.core.Rand;

public final class GameSound {
   public String name;
   public String category = "General";
   public boolean loop = false;
   public boolean is3D = true;
   public final ArrayList clips = new ArrayList();
   private float userVolume = 1.0F;
   public GameSound.MasterVolume master;
   public short reloadEpoch;

   public GameSound() {
      this.master = GameSound.MasterVolume.Primary;
   }

   public String getName() {
      return this.name;
   }

   public String getCategory() {
      return this.category;
   }

   public boolean isLooped() {
      return this.loop;
   }

   public void setUserVolume(float var1) {
      this.userVolume = Math.max(0.0F, Math.min(2.0F, var1));
   }

   public float getUserVolume() {
      return !SystemDisabler.getEnableAdvancedSoundOptions() ? 1.0F : this.userVolume;
   }

   public GameSoundClip getRandomClip() {
      return (GameSoundClip)this.clips.get(Rand.Next(this.clips.size()));
   }

   public String getMasterName() {
      return this.master.name();
   }

   public void reset() {
      this.name = null;
      this.category = "General";
      this.loop = false;
      this.is3D = true;
      this.clips.clear();
      this.userVolume = 1.0F;
      this.master = GameSound.MasterVolume.Primary;
      ++this.reloadEpoch;
   }

   public static enum MasterVolume {
      Primary,
      Ambient,
      Music,
      VehicleEngine;

      // $FF: synthetic method
      private static GameSound.MasterVolume[] $values() {
         return new GameSound.MasterVolume[]{Primary, Ambient, Music, VehicleEngine};
      }
   }
}

package zombie.audio;

import fmod.fmod.FMOD_STUDIO_PARAMETER_DESCRIPTION;
import zombie.iso.IsoGridSquare;
import zombie.iso.IsoObject;

public abstract class BaseSoundEmitter {
   public abstract void randomStart();

   public abstract void setPos(float var1, float var2, float var3);

   public abstract int stopSound(long var1);

   public abstract int stopSoundByName(String var1);

   public abstract void stopOrTriggerSound(long var1);

   public abstract void stopOrTriggerSoundByName(String var1);

   public abstract void setVolume(long var1, float var3);

   public abstract void setPitch(long var1, float var3);

   public abstract boolean hasSustainPoints(long var1);

   public abstract void setParameterValue(long var1, FMOD_STUDIO_PARAMETER_DESCRIPTION var3, float var4);

   public abstract void setTimelinePosition(long var1, String var3);

   public abstract void triggerCue(long var1);

   public abstract void setVolumeAll(float var1);

   public abstract void stopAll();

   public abstract long playSound(String var1);

   public abstract long playSound(String var1, int var2, int var3, int var4);

   public abstract long playSound(String var1, IsoGridSquare var2);

   public abstract long playSoundImpl(String var1, IsoGridSquare var2);

   /** @deprecated */
   @Deprecated
   public abstract long playSound(String var1, boolean var2);

   /** @deprecated */
   @Deprecated
   public abstract long playSoundImpl(String var1, boolean var2, IsoObject var3);

   public abstract long playSoundLooped(String var1);

   public abstract long playSoundLoopedImpl(String var1);

   public abstract long playSound(String var1, IsoObject var2);

   public abstract long playSoundImpl(String var1, IsoObject var2);

   public abstract long playClip(GameSoundClip var1, IsoObject var2);

   public abstract long playAmbientSound(String var1);

   public abstract long playAmbientLoopedImpl(String var1);

   public abstract void set3D(long var1, boolean var3);

   public abstract void tick();

   public abstract boolean hasSoundsToStart();

   public abstract boolean isEmpty();

   public abstract boolean isPlaying(long var1);

   public abstract boolean isPlaying(String var1);

   public abstract boolean restart(long var1);
}

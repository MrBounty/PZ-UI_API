package zombie.audio;

import fmod.fmod.FMOD_STUDIO_PARAMETER_DESCRIPTION;
import zombie.iso.IsoGridSquare;
import zombie.iso.IsoObject;

public class DummySoundEmitter extends BaseSoundEmitter {
   public void randomStart() {
   }

   public void setPos(float var1, float var2, float var3) {
   }

   public int stopSound(long var1) {
      return 0;
   }

   public int stopSoundByName(String var1) {
      return 0;
   }

   public void stopOrTriggerSound(long var1) {
   }

   public void stopOrTriggerSoundByName(String var1) {
   }

   public void setVolume(long var1, float var3) {
   }

   public void setPitch(long var1, float var3) {
   }

   public boolean hasSustainPoints(long var1) {
      return false;
   }

   public void setParameterValue(long var1, FMOD_STUDIO_PARAMETER_DESCRIPTION var3, float var4) {
   }

   public void setTimelinePosition(long var1, String var3) {
   }

   public void triggerCue(long var1) {
   }

   public void set3D(long var1, boolean var3) {
   }

   public void setVolumeAll(float var1) {
   }

   public void stopAll() {
   }

   public long playSound(String var1) {
      return 0L;
   }

   public long playSound(String var1, int var2, int var3, int var4) {
      return 0L;
   }

   public long playSound(String var1, IsoGridSquare var2) {
      return 0L;
   }

   public long playSoundImpl(String var1, IsoGridSquare var2) {
      return 0L;
   }

   public long playSound(String var1, boolean var2) {
      return 0L;
   }

   public long playSoundImpl(String var1, boolean var2, IsoObject var3) {
      return 0L;
   }

   public long playSound(String var1, IsoObject var2) {
      return 0L;
   }

   public long playSoundImpl(String var1, IsoObject var2) {
      return 0L;
   }

   public long playClip(GameSoundClip var1, IsoObject var2) {
      return 0L;
   }

   public long playAmbientSound(String var1) {
      return 0L;
   }

   public void tick() {
   }

   public boolean hasSoundsToStart() {
      return false;
   }

   public boolean isEmpty() {
      return true;
   }

   public boolean isPlaying(long var1) {
      return false;
   }

   public boolean isPlaying(String var1) {
      return false;
   }

   public boolean restart(long var1) {
      return false;
   }

   public long playSoundLooped(String var1) {
      return 0L;
   }

   public long playSoundLoopedImpl(String var1) {
      return 0L;
   }

   public long playAmbientLoopedImpl(String var1) {
      return 0L;
   }
}

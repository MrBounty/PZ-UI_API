package zombie.characters;

import fmod.fmod.FMOD_STUDIO_PARAMETER_DESCRIPTION;
import zombie.iso.IsoObject;

public abstract class BaseCharacterSoundEmitter {
   protected final IsoGameCharacter character;

   public BaseCharacterSoundEmitter(IsoGameCharacter var1) {
      this.character = var1;
   }

   public abstract void register();

   public abstract void unregister();

   public abstract long playVocals(String var1);

   public abstract void playFootsteps(String var1, float var2);

   public abstract long playSound(String var1);

   public abstract long playSound(String var1, IsoObject var2);

   public abstract long playSoundImpl(String var1, IsoObject var2);

   public abstract void tick();

   public abstract void set(float var1, float var2, float var3);

   public abstract boolean isClear();

   public abstract void setPitch(long var1, float var3);

   public abstract void setVolume(long var1, float var3);

   public abstract int stopSound(long var1);

   public abstract int stopSoundByName(String var1);

   public abstract void stopOrTriggerSound(long var1);

   public abstract void stopOrTriggerSoundByName(String var1);

   public abstract void stopAll();

   public abstract boolean hasSoundsToStart();

   public abstract boolean isPlaying(long var1);

   public abstract boolean isPlaying(String var1);

   public abstract void setParameterValue(long var1, FMOD_STUDIO_PARAMETER_DESCRIPTION var3, float var4);
}

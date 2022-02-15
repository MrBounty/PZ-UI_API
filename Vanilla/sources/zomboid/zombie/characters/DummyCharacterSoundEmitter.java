package zombie.characters;

import fmod.fmod.FMOD_STUDIO_PARAMETER_DESCRIPTION;
import java.util.HashMap;
import java.util.Iterator;
import zombie.core.Rand;
import zombie.iso.IsoObject;
import zombie.network.GameClient;

public final class DummyCharacterSoundEmitter extends BaseCharacterSoundEmitter {
   public float x;
   public float y;
   public float z;
   private final HashMap sounds = new HashMap();

   public DummyCharacterSoundEmitter(IsoGameCharacter var1) {
      super(var1);
   }

   public void register() {
   }

   public void unregister() {
   }

   public long playVocals(String var1) {
      return 0L;
   }

   public void playFootsteps(String var1, float var2) {
   }

   public long playSound(String var1) {
      long var2 = (long)Rand.Next(Integer.MAX_VALUE);
      this.sounds.put(var2, var1);
      if (GameClient.bClient) {
         GameClient.instance.PlaySound(var1, false, this.character);
      }

      return var2;
   }

   public long playSound(String var1, IsoObject var2) {
      return this.playSound(var1);
   }

   public long playSoundImpl(String var1, IsoObject var2) {
      long var3 = Rand.Next(Long.MAX_VALUE);
      this.sounds.put(var3, var1);
      return var3;
   }

   public void tick() {
   }

   public void set(float var1, float var2, float var3) {
      this.x = var1;
      this.y = var2;
      this.z = var3;
   }

   public boolean isClear() {
      return this.sounds.isEmpty();
   }

   public void setPitch(long var1, float var3) {
   }

   public void setVolume(long var1, float var3) {
   }

   public int stopSound(long var1) {
      if (GameClient.bClient) {
         GameClient.instance.StopSound(this.character, (String)this.sounds.get(var1), false);
      }

      this.sounds.remove(var1);
      return 0;
   }

   public void stopOrTriggerSound(long var1) {
      if (GameClient.bClient) {
         GameClient.instance.StopSound(this.character, (String)this.sounds.get(var1), true);
      }

      this.sounds.remove(var1);
   }

   public void stopOrTriggerSoundByName(String var1) {
      this.sounds.values().remove(var1);
   }

   public void stopAll() {
      if (GameClient.bClient) {
         Iterator var1 = this.sounds.values().iterator();

         while(var1.hasNext()) {
            String var2 = (String)var1.next();
            GameClient.instance.StopSound(this.character, var2, false);
         }
      }

      this.sounds.clear();
   }

   public int stopSoundByName(String var1) {
      this.sounds.values().remove(var1);
      return 0;
   }

   public boolean hasSoundsToStart() {
      return false;
   }

   public boolean isPlaying(long var1) {
      return this.sounds.containsKey(var1);
   }

   public boolean isPlaying(String var1) {
      return this.sounds.containsValue(var1);
   }

   public void setParameterValue(long var1, FMOD_STUDIO_PARAMETER_DESCRIPTION var3, float var4) {
   }

   public boolean hasSustainPoints(long var1) {
      return false;
   }
}

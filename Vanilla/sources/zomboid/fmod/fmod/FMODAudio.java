package fmod.fmod;

import zombie.audio.BaseSoundEmitter;

public class FMODAudio implements Audio {
   public BaseSoundEmitter emitter;

   public FMODAudio(BaseSoundEmitter var1) {
      this.emitter = var1;
   }

   public boolean isPlaying() {
      return !this.emitter.isEmpty();
   }

   public void setVolume(float var1) {
      this.emitter.setVolumeAll(var1);
   }

   public void start() {
   }

   public void pause() {
   }

   public void stop() {
      this.emitter.stopAll();
   }

   public void setName(String var1) {
   }

   public String getName() {
      return null;
   }
}

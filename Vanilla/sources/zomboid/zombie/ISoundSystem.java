package zombie;

public interface ISoundSystem {
   void init();

   void update();

   void purge();

   void fadeOutAll(float var1);

   ISoundSystem.ISoundInstance playSound(ISoundSystem.SoundFormat var1, String var2, String var3, int var4, boolean var5, boolean var6, float var7);

   ISoundSystem.ISoundInstance playSound(ISoundSystem.SoundFormat var1, String var2, String var3, int var4, boolean var5, boolean var6, float var7, float var8);

   ISoundSystem.ISoundInstance playSound(ISoundSystem.SoundFormat var1, String var2, String var3, int var4, boolean var5, boolean var6, float var7, float var8, float var9);

   ISoundSystem.ISoundInstance playSound(ISoundSystem.SoundFormat var1, String var2, String var3, boolean var4, boolean var5, float var6);

   ISoundSystem.ISoundInstance playSound(ISoundSystem.SoundFormat var1, String var2, String var3, boolean var4, boolean var5, float var6, float var7);

   ISoundSystem.ISoundInstance playSound(ISoundSystem.SoundFormat var1, String var2, String var3, boolean var4, boolean var5, float var6, float var7, float var8);

   void cacheSound(ISoundSystem.SoundFormat var1, String var2, String var3, int var4);

   void cacheSound(ISoundSystem.SoundFormat var1, String var2, String var3);

   void clearSoundCache();

   int countInstances(String var1);

   void setInstanceLimit(String var1, int var2, ISoundSystem.InstanceFailAction var3);

   public static enum InstanceFailAction {
      FailToPlay,
      StopOldest,
      StopRandom;

      // $FF: synthetic method
      private static ISoundSystem.InstanceFailAction[] $values() {
         return new ISoundSystem.InstanceFailAction[]{FailToPlay, StopOldest, StopRandom};
      }
   }

   public interface ISoundInstance {
      boolean isStreamed();

      boolean isLooped();

      boolean isPlaying();

      int countInstances();

      void setLooped(boolean var1);

      void pause();

      void stop();

      void play();

      void blendVolume(float var1, float var2, boolean var3);

      void setVolume(float var1);

      float getVolume();

      void setPanning(float var1);

      float getPanning();

      void setPitch(float var1);

      float getPitch();

      boolean disposed();
   }

   public static enum SoundFormat {
      Ogg,
      Wav;

      // $FF: synthetic method
      private static ISoundSystem.SoundFormat[] $values() {
         return new ISoundSystem.SoundFormat[]{Ogg, Wav};
      }
   }
}

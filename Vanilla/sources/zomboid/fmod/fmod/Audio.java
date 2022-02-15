package fmod.fmod;

public interface Audio {
   boolean isPlaying();

   void setVolume(float var1);

   void start();

   void pause();

   void stop();

   void setName(String var1);

   String getName();
}

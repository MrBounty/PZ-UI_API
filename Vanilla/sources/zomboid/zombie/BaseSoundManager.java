package zombie;

import fmod.fmod.Audio;
import java.util.ArrayList;
import zombie.audio.BaseSoundEmitter;
import zombie.characters.IsoPlayer;
import zombie.iso.IsoGridSquare;

public abstract class BaseSoundManager {
   public boolean AllowMusic = true;

   public abstract boolean isRemastered();

   public abstract void update1();

   public abstract void update3();

   public abstract void update2();

   public abstract void update4();

   public abstract void CacheSound(String var1);

   public abstract void StopSound(Audio var1);

   public abstract void StopMusic();

   public abstract void Purge();

   public abstract void stop();

   protected abstract boolean HasMusic(Audio var1);

   public abstract void Update();

   public abstract Audio Start(Audio var1, float var2, String var3);

   public abstract Audio PrepareMusic(String var1);

   public abstract void PlayWorldSoundWav(String var1, IsoGridSquare var2, float var3, float var4, float var5, int var6, boolean var7);

   public abstract Audio PlayWorldSoundWav(String var1, boolean var2, IsoGridSquare var3, float var4, float var5, float var6, boolean var7);

   public abstract Audio PlayWorldSoundWav(String var1, IsoGridSquare var2, float var3, float var4, float var5, boolean var6);

   public abstract Audio PlayWorldSound(String var1, IsoGridSquare var2, float var3, float var4, float var5, int var6, boolean var7);

   public abstract Audio PlayWorldSound(String var1, boolean var2, IsoGridSquare var3, float var4, float var5, float var6, boolean var7);

   public abstract Audio PlayWorldSoundImpl(String var1, boolean var2, int var3, int var4, int var5, float var6, float var7, float var8, boolean var9);

   public abstract Audio PlayWorldSound(String var1, IsoGridSquare var2, float var3, float var4, float var5, boolean var6);

   public abstract void update3D();

   public abstract Audio PlaySoundWav(String var1, int var2, boolean var3, float var4);

   public abstract Audio PlaySoundWav(String var1, boolean var2, float var3);

   public abstract Audio PlaySoundWav(String var1, boolean var2, float var3, float var4);

   public abstract Audio PlayWorldSoundWavImpl(String var1, boolean var2, IsoGridSquare var3, float var4, float var5, float var6, boolean var7);

   public abstract Audio PlayJukeboxSound(String var1, boolean var2, float var3);

   public abstract Audio PlaySoundEvenSilent(String var1, boolean var2, float var3);

   public abstract Audio PlaySound(String var1, boolean var2, float var3);

   public abstract Audio PlaySound(String var1, boolean var2, float var3, float var4);

   public abstract Audio PlayMusic(String var1, String var2, boolean var3, float var4);

   public abstract void PlayAsMusic(String var1, Audio var2, boolean var3, float var4);

   public abstract void setMusicState(String var1);

   public abstract void setMusicWakeState(IsoPlayer var1, String var2);

   public abstract void DoMusic(String var1, boolean var2);

   public abstract float getMusicPosition();

   public abstract void CheckDoMusic();

   public abstract void stopMusic(String var1);

   public abstract void playMusicNonTriggered(String var1, float var2);

   public abstract void playAmbient(String var1);

   public abstract void playMusic(String var1);

   public abstract boolean isPlayingMusic();

   public abstract boolean IsMusicPlaying();

   public abstract String getCurrentMusicName();

   public abstract String getCurrentMusicLibrary();

   public abstract void PlayAsMusic(String var1, Audio var2, float var3, boolean var4);

   public abstract long playUISound(String var1);

   public abstract boolean isPlayingUISound(String var1);

   public abstract boolean isPlayingUISound(long var1);

   public abstract void stopUISound(long var1);

   public abstract void FadeOutMusic(String var1, int var2);

   public abstract Audio BlendThenStart(Audio var1, float var2, String var3);

   public abstract void BlendVolume(Audio var1, float var2, float var3);

   public abstract void BlendVolume(Audio var1, float var2);

   public abstract void setSoundVolume(float var1);

   public abstract float getSoundVolume();

   public abstract void setAmbientVolume(float var1);

   public abstract float getAmbientVolume();

   public abstract void setMusicVolume(float var1);

   public abstract float getMusicVolume();

   public abstract void setVehicleEngineVolume(float var1);

   public abstract float getVehicleEngineVolume();

   public abstract void playNightAmbient(String var1);

   public abstract ArrayList getAmbientPieces();

   public abstract void pauseSoundAndMusic();

   public abstract void resumeSoundAndMusic();

   public abstract void debugScriptSounds();

   public abstract void registerEmitter(BaseSoundEmitter var1);

   public abstract void unregisterEmitter(BaseSoundEmitter var1);

   public abstract boolean isListenerInRange(float var1, float var2, float var3);
}

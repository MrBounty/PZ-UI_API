package zombie;

import fmod.fmod.Audio;
import java.util.ArrayList;
import zombie.audio.BaseSoundEmitter;
import zombie.characters.IsoPlayer;
import zombie.iso.IsoGridSquare;

public final class DummySoundManager extends BaseSoundManager {
   private static ArrayList ambientPieces = new ArrayList();

   public boolean isRemastered() {
      return false;
   }

   public void update1() {
   }

   public void update3() {
   }

   public void update2() {
   }

   public void update4() {
   }

   public void CacheSound(String var1) {
   }

   public void StopSound(Audio var1) {
   }

   public void StopMusic() {
   }

   public void Purge() {
   }

   public void stop() {
   }

   protected boolean HasMusic(Audio var1) {
      return false;
   }

   public void Update() {
   }

   public Audio Start(Audio var1, float var2, String var3) {
      return null;
   }

   public Audio PrepareMusic(String var1) {
      return null;
   }

   public void PlayWorldSoundWav(String var1, IsoGridSquare var2, float var3, float var4, float var5, int var6, boolean var7) {
   }

   public Audio PlayWorldSoundWav(String var1, boolean var2, IsoGridSquare var3, float var4, float var5, float var6, boolean var7) {
      return null;
   }

   public Audio PlayWorldSoundWav(String var1, IsoGridSquare var2, float var3, float var4, float var5, boolean var6) {
      return null;
   }

   public Audio PlayWorldSound(String var1, IsoGridSquare var2, float var3, float var4, float var5, int var6, boolean var7) {
      return null;
   }

   public Audio PlayWorldSound(String var1, boolean var2, IsoGridSquare var3, float var4, float var5, float var6, boolean var7) {
      return null;
   }

   public Audio PlayWorldSoundImpl(String var1, boolean var2, int var3, int var4, int var5, float var6, float var7, float var8, boolean var9) {
      return null;
   }

   public Audio PlayWorldSound(String var1, IsoGridSquare var2, float var3, float var4, float var5, boolean var6) {
      return null;
   }

   public void update3D() {
   }

   public Audio PlaySoundWav(String var1, int var2, boolean var3, float var4) {
      return null;
   }

   public Audio PlaySoundWav(String var1, boolean var2, float var3) {
      return null;
   }

   public Audio PlaySoundWav(String var1, boolean var2, float var3, float var4) {
      return null;
   }

   public Audio PlayJukeboxSound(String var1, boolean var2, float var3) {
      return null;
   }

   public Audio PlaySoundEvenSilent(String var1, boolean var2, float var3) {
      return null;
   }

   public Audio PlaySound(String var1, boolean var2, float var3) {
      return null;
   }

   public Audio PlaySound(String var1, boolean var2, float var3, float var4) {
      return null;
   }

   public Audio PlayMusic(String var1, String var2, boolean var3, float var4) {
      return null;
   }

   public void PlayAsMusic(String var1, Audio var2, boolean var3, float var4) {
   }

   public void setMusicState(String var1) {
   }

   public void setMusicWakeState(IsoPlayer var1, String var2) {
   }

   public void DoMusic(String var1, boolean var2) {
   }

   public float getMusicPosition() {
      return 0.0F;
   }

   public void CheckDoMusic() {
   }

   public void stopMusic(String var1) {
   }

   public void playMusicNonTriggered(String var1, float var2) {
   }

   public void playAmbient(String var1) {
   }

   public void playMusic(String var1) {
   }

   public boolean isPlayingMusic() {
      return false;
   }

   public boolean IsMusicPlaying() {
      return false;
   }

   public void PlayAsMusic(String var1, Audio var2, float var3, boolean var4) {
   }

   public long playUISound(String var1) {
      return 0L;
   }

   public boolean isPlayingUISound(String var1) {
      return false;
   }

   public boolean isPlayingUISound(long var1) {
      return false;
   }

   public void stopUISound(long var1) {
   }

   public void FadeOutMusic(String var1, int var2) {
   }

   public Audio BlendThenStart(Audio var1, float var2, String var3) {
      return null;
   }

   public void BlendVolume(Audio var1, float var2, float var3) {
   }

   public void BlendVolume(Audio var1, float var2) {
   }

   public void setSoundVolume(float var1) {
   }

   public float getSoundVolume() {
      return 0.0F;
   }

   public void setMusicVolume(float var1) {
   }

   public float getMusicVolume() {
      return 0.0F;
   }

   public void setVehicleEngineVolume(float var1) {
   }

   public float getVehicleEngineVolume() {
      return 0.0F;
   }

   public void setAmbientVolume(float var1) {
   }

   public float getAmbientVolume() {
      return 0.0F;
   }

   public void playNightAmbient(String var1) {
   }

   public ArrayList getAmbientPieces() {
      return ambientPieces;
   }

   public void pauseSoundAndMusic() {
   }

   public void resumeSoundAndMusic() {
   }

   public void debugScriptSounds() {
   }

   public void registerEmitter(BaseSoundEmitter var1) {
   }

   public void unregisterEmitter(BaseSoundEmitter var1) {
   }

   public boolean isListenerInRange(float var1, float var2, float var3) {
      return false;
   }

   public Audio PlayWorldSoundWavImpl(String var1, boolean var2, IsoGridSquare var3, float var4, float var5, float var6, boolean var7) {
      return null;
   }

   public String getCurrentMusicName() {
      return null;
   }

   public String getCurrentMusicLibrary() {
      return null;
   }
}

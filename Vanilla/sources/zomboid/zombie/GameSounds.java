package zombie;

import fmod.javafmod;
import fmod.javafmodJNI;
import fmod.fmod.FMODFootstep;
import fmod.fmod.FMODManager;
import fmod.fmod.FMODSoundBank;
import fmod.fmod.FMODVoice;
import fmod.fmod.FMOD_STUDIO_EVENT_DESCRIPTION;
import fmod.fmod.FMOD_STUDIO_PLAYBACK_STATE;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import zombie.audio.BaseSoundBank;
import zombie.audio.GameSound;
import zombie.audio.GameSoundClip;
import zombie.characters.IsoPlayer;
import zombie.config.ConfigFile;
import zombie.config.ConfigOption;
import zombie.config.DoubleConfigOption;
import zombie.core.Core;
import zombie.core.logger.ExceptionLogger;
import zombie.core.math.PZMath;
import zombie.debug.DebugLog;
import zombie.network.GameClient;
import zombie.network.GameServer;
import zombie.scripting.ScriptManager;
import zombie.scripting.objects.GameSoundScript;
import zombie.util.StringUtils;

public final class GameSounds {
   public static final int VERSION = 1;
   protected static final HashMap soundByName = new HashMap();
   protected static final ArrayList sounds = new ArrayList();
   private static final GameSounds.BankPreviewSound previewBank = new GameSounds.BankPreviewSound();
   private static final GameSounds.FilePreviewSound previewFile = new GameSounds.FilePreviewSound();
   public static boolean soundIsPaused = false;
   private static GameSounds.IPreviewSound previewSound;

   public static void addSound(GameSound var0) {
      initClipEvents(var0);

      assert !sounds.contains(var0);

      int var1 = sounds.size();
      if (soundByName.containsKey(var0.getName())) {
         for(var1 = 0; var1 < sounds.size() && !((GameSound)sounds.get(var1)).getName().equals(var0.getName()); ++var1) {
         }

         sounds.remove(var1);
      }

      sounds.add(var1, var0);
      soundByName.put(var0.getName(), var0);
   }

   private static void initClipEvents(GameSound var0) {
      if (!GameServer.bServer) {
         Iterator var1 = var0.clips.iterator();

         while(var1.hasNext()) {
            GameSoundClip var2 = (GameSoundClip)var1.next();
            if (var2.event != null && var2.eventDescription == null) {
               var2.eventDescription = FMODManager.instance.getEventDescription("event:/" + var2.event);
               if (var2.eventDescription == null) {
                  DebugLog.Sound.warn("No such FMOD event \"%s\" for GameSound \"%s\"", var2.event, var0.getName());
               }
            }
         }

      }
   }

   public static boolean isKnownSound(String var0) {
      return soundByName.containsKey(var0);
   }

   public static GameSound getSound(String var0) {
      return getOrCreateSound(var0);
   }

   public static GameSound getOrCreateSound(String var0) {
      if (StringUtils.isNullOrEmpty(var0)) {
         return null;
      } else {
         GameSound var1 = (GameSound)soundByName.get(var0);
         if (var1 == null) {
            DebugLog.General.warn("no GameSound called \"" + var0 + "\", adding a new one");
            var1 = new GameSound();
            var1.name = var0;
            var1.category = "AUTO";
            GameSoundClip var2 = new GameSoundClip(var1);
            var1.clips.add(var2);
            sounds.add(var1);
            soundByName.put(var0.replace(".wav", "").replace(".ogg", ""), var1);
            if (BaseSoundBank.instance instanceof FMODSoundBank) {
               FMOD_STUDIO_EVENT_DESCRIPTION var3 = FMODManager.instance.getEventDescription("event:/" + var0);
               if (var3 != null) {
                  var2.event = var0;
                  var2.eventDescription = var3;
               } else {
                  String var4 = null;
                  if (ZomboidFileSystem.instance.getAbsolutePath("media/sound/" + var0 + ".ogg") != null) {
                     var4 = "media/sound/" + var0 + ".ogg";
                  } else if (ZomboidFileSystem.instance.getAbsolutePath("media/sound/" + var0 + ".wav") != null) {
                     var4 = "media/sound/" + var0 + ".wav";
                  }

                  if (var4 != null) {
                     long var5 = FMODManager.instance.loadSound(var4);
                     if (var5 != 0L) {
                        var2.file = var4;
                     }
                  }
               }

               if (var2.event == null && var2.file == null) {
                  DebugLog.General.warn("couldn't find an FMOD event or .ogg or .wav file for sound \"" + var0 + "\"");
               }
            }
         }

         return var1;
      }
   }

   private static void loadNonBankSounds() {
      if (BaseSoundBank.instance instanceof FMODSoundBank) {
         Iterator var0 = sounds.iterator();

         while(var0.hasNext()) {
            GameSound var1 = (GameSound)var0.next();
            Iterator var2 = var1.clips.iterator();

            while(var2.hasNext()) {
               GameSoundClip var3 = (GameSoundClip)var2.next();
               if (var3.getFile() != null && var3.getFile().isEmpty()) {
               }
            }
         }

      }
   }

   public static void ScriptsLoaded() {
      ArrayList var0 = ScriptManager.instance.getAllGameSounds();

      for(int var1 = 0; var1 < var0.size(); ++var1) {
         GameSoundScript var2 = (GameSoundScript)var0.get(var1);
         if (!var2.gameSound.clips.isEmpty()) {
            addSound(var2.gameSound);
         }
      }

      var0.clear();
      loadNonBankSounds();
      loadINI();
      if (Core.bDebug && BaseSoundBank.instance instanceof FMODSoundBank) {
         HashSet var12 = new HashSet();
         Iterator var13 = sounds.iterator();

         while(var13.hasNext()) {
            GameSound var3 = (GameSound)var13.next();
            Iterator var4 = var3.clips.iterator();

            while(var4.hasNext()) {
               GameSoundClip var5 = (GameSoundClip)var4.next();
               if (var5.getEvent() != null && !var5.getEvent().isEmpty()) {
                  var12.add(var5.getEvent());
               }
            }
         }

         FMODSoundBank var14 = (FMODSoundBank)BaseSoundBank.instance;
         Iterator var15 = var14.footstepMap.values().iterator();

         while(var15.hasNext()) {
            FMODFootstep var17 = (FMODFootstep)var15.next();
            var12.add(var17.wood);
            var12.add(var17.concrete);
            var12.add(var17.grass);
            var12.add(var17.upstairs);
            var12.add(var17.woodCreak);
         }

         var15 = var14.voiceMap.values().iterator();

         while(var15.hasNext()) {
            FMODVoice var18 = (FMODVoice)var15.next();
            var12.add(var18.sound);
         }

         ArrayList var16 = new ArrayList();
         long[] var19 = new long[32];
         long[] var20 = new long[1024];
         int var6 = javafmodJNI.FMOD_Studio_System_GetBankList(var19);

         for(int var7 = 0; var7 < var6; ++var7) {
            int var8 = javafmodJNI.FMOD_Studio_Bank_GetEventList(var19[var7], var20);

            for(int var9 = 0; var9 < var8; ++var9) {
               try {
                  String var10 = javafmodJNI.FMOD_Studio_EventDescription_GetPath(var20[var9]);
                  var10 = var10.replace("event:/", "");
                  if (!var12.contains(var10)) {
                     var16.add(var10);
                  }
               } catch (Exception var11) {
                  DebugLog.General.warn("FMOD cannot get path for " + var20[var9] + " event");
               }
            }
         }

         var16.sort(String::compareTo);
         Iterator var21 = var16.iterator();

         while(var21.hasNext()) {
            String var22 = (String)var21.next();
            DebugLog.General.warn("FMOD event \"%s\" not used by any GameSound", var22);
         }
      }

   }

   public static void ReloadFile(String var0) {
      try {
         ScriptManager.instance.LoadFile(var0, true);
         ArrayList var1 = ScriptManager.instance.getAllGameSounds();

         for(int var2 = 0; var2 < var1.size(); ++var2) {
            GameSoundScript var3 = (GameSoundScript)var1.get(var2);
            if (sounds.contains(var3.gameSound)) {
               initClipEvents(var3.gameSound);
            } else if (!var3.gameSound.clips.isEmpty()) {
               addSound(var3.gameSound);
            }
         }
      } catch (Throwable var4) {
         ExceptionLogger.logException(var4);
      }

   }

   public static ArrayList getCategories() {
      HashSet var0 = new HashSet();
      Iterator var1 = sounds.iterator();

      while(var1.hasNext()) {
         GameSound var2 = (GameSound)var1.next();
         var0.add(var2.getCategory());
      }

      ArrayList var3 = new ArrayList(var0);
      Collections.sort(var3);
      return var3;
   }

   public static ArrayList getSoundsInCategory(String var0) {
      ArrayList var1 = new ArrayList();
      Iterator var2 = sounds.iterator();

      while(var2.hasNext()) {
         GameSound var3 = (GameSound)var2.next();
         if (var3.getCategory().equals(var0)) {
            var1.add(var3);
         }
      }

      return var1;
   }

   public static void loadINI() {
      String var10000 = ZomboidFileSystem.instance.getCacheDir();
      String var0 = var10000 + File.separator + "sounds.ini";
      ConfigFile var1 = new ConfigFile();
      if (var1.read(var0)) {
         if (var1.getVersion() <= 1) {
            Iterator var2 = var1.getOptions().iterator();

            while(var2.hasNext()) {
               ConfigOption var3 = (ConfigOption)var2.next();
               GameSound var4 = (GameSound)soundByName.get(var3.getName());
               if (var4 != null) {
                  var4.setUserVolume(PZMath.tryParseFloat(var3.getValueAsString(), 1.0F));
               }
            }

         }
      }
   }

   public static void saveINI() {
      ArrayList var0 = new ArrayList();
      Iterator var1 = sounds.iterator();

      while(var1.hasNext()) {
         GameSound var2 = (GameSound)var1.next();
         DoubleConfigOption var3 = new DoubleConfigOption(var2.getName(), 0.0D, 2.0D, 0.0D);
         var3.setValue((double)var2.getUserVolume());
         var0.add(var3);
      }

      String var10000 = ZomboidFileSystem.instance.getCacheDir();
      String var4 = var10000 + File.separator + "sounds.ini";
      ConfigFile var5 = new ConfigFile();
      if (var5.write(var4, 1, var0)) {
         var0.clear();
      }
   }

   public static void previewSound(String var0) {
      if (!Core.SoundDisabled) {
         if (isKnownSound(var0)) {
            GameSound var1 = getSound(var0);
            if (var1 == null) {
               DebugLog.log("no such GameSound " + var0);
            } else {
               GameSoundClip var2 = var1.getRandomClip();
               if (var2 == null) {
                  DebugLog.log("GameSound.clips is empty");
               } else {
                  if (soundIsPaused) {
                     if (!GameClient.bClient) {
                        long var3 = javafmod.FMOD_System_GetMasterChannelGroup();
                        javafmod.FMOD_ChannelGroup_SetVolume(var3, 1.0F);
                     }

                     soundIsPaused = false;
                  }

                  if (previewSound != null) {
                     previewSound.stop();
                  }

                  if (var2.getEvent() != null) {
                     if (previewBank.play(var2)) {
                        previewSound = previewBank;
                     }
                  } else if (var2.getFile() != null && previewFile.play(var2)) {
                     previewSound = previewFile;
                  }

               }
            }
         }
      }
   }

   public static void stopPreview() {
      if (previewSound != null) {
         previewSound.stop();
         previewSound = null;
      }
   }

   public static boolean isPreviewPlaying() {
      if (previewSound == null) {
         return false;
      } else if (previewSound.update()) {
         previewSound = null;
         return false;
      } else {
         return previewSound.isPlaying();
      }
   }

   public static void fix3DListenerPosition(boolean var0) {
      if (!Core.SoundDisabled) {
         if (var0) {
            javafmod.FMOD_Studio_Listener3D(0, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -1.0F, 0.0F, 0.0F, 0.0F, 1.0F);
         } else {
            for(int var1 = 0; var1 < IsoPlayer.numPlayers; ++var1) {
               IsoPlayer var2 = IsoPlayer.players[var1];
               if (var2 != null && !var2.Traits.Deaf.isSet()) {
                  javafmod.FMOD_Studio_Listener3D(var1, var2.x, var2.y, var2.z * 3.0F, 0.0F, 0.0F, 0.0F, -1.0F / (float)Math.sqrt(2.0D), -1.0F / (float)Math.sqrt(2.0D), 0.0F, 0.0F, 0.0F, 1.0F);
               }
            }
         }

      }
   }

   public static void Reset() {
      sounds.clear();
      soundByName.clear();
      if (previewSound != null) {
         previewSound.stop();
         previewSound = null;
      }

   }

   private interface IPreviewSound {
      boolean play(GameSoundClip var1);

      boolean isPlaying();

      boolean update();

      void stop();
   }

   private static final class BankPreviewSound implements GameSounds.IPreviewSound {
      long instance;
      GameSoundClip clip;
      float effectiveGain;

      public boolean play(GameSoundClip var1) {
         if (var1.eventDescription == null) {
            DebugLog.log("failed to get event " + var1.getEvent());
            return false;
         } else {
            this.instance = javafmod.FMOD_Studio_System_CreateEventInstance(var1.eventDescription.address);
            if (this.instance < 0L) {
               DebugLog.log("failed to create EventInstance: error=" + this.instance);
               this.instance = 0L;
               return false;
            } else {
               this.clip = var1;
               this.effectiveGain = var1.getEffectiveVolumeInMenu();
               javafmod.FMOD_Studio_EventInstance_SetVolume(this.instance, this.effectiveGain);
               javafmod.FMOD_Studio_EventInstance_SetParameterByName(this.instance, "Occlusion", 0.0F);
               javafmod.FMOD_Studio_StartEvent(this.instance);
               if (var1.gameSound.master == GameSound.MasterVolume.Music) {
                  javafmod.FMOD_Studio_EventInstance_SetParameterByName(this.instance, "Volume", 10.0F);
               }

               return true;
            }
         }
      }

      public boolean isPlaying() {
         if (this.instance == 0L) {
            return false;
         } else {
            int var1 = javafmod.FMOD_Studio_GetPlaybackState(this.instance);
            if (var1 == FMOD_STUDIO_PLAYBACK_STATE.FMOD_STUDIO_PLAYBACK_STOPPING.index) {
               return true;
            } else {
               return var1 != FMOD_STUDIO_PLAYBACK_STATE.FMOD_STUDIO_PLAYBACK_STOPPED.index;
            }
         }
      }

      public boolean update() {
         if (this.instance == 0L) {
            return false;
         } else {
            int var1 = javafmod.FMOD_Studio_GetPlaybackState(this.instance);
            if (var1 == FMOD_STUDIO_PLAYBACK_STATE.FMOD_STUDIO_PLAYBACK_STOPPING.index) {
               return false;
            } else if (var1 == FMOD_STUDIO_PLAYBACK_STATE.FMOD_STUDIO_PLAYBACK_STOPPED.index) {
               javafmod.FMOD_Studio_ReleaseEventInstance(this.instance);
               this.instance = 0L;
               this.clip = null;
               return true;
            } else {
               float var2 = this.clip.getEffectiveVolumeInMenu();
               if (this.effectiveGain != var2) {
                  this.effectiveGain = var2;
                  javafmod.FMOD_Studio_EventInstance_SetVolume(this.instance, this.effectiveGain);
               }

               return false;
            }
         }
      }

      public void stop() {
         if (this.instance != 0L) {
            javafmod.FMOD_Studio_EventInstance_Stop(this.instance, false);
            javafmod.FMOD_Studio_ReleaseEventInstance(this.instance);
            this.instance = 0L;
            this.clip = null;
         }
      }
   }

   private static final class FilePreviewSound implements GameSounds.IPreviewSound {
      long channel;
      GameSoundClip clip;
      float effectiveGain;

      public boolean play(GameSoundClip var1) {
         GameSound var2 = var1.gameSound;
         long var3 = FMODManager.instance.loadSound(var1.getFile(), var2.isLooped());
         if (var3 == 0L) {
            return false;
         } else {
            this.channel = javafmod.FMOD_System_PlaySound(var3, true);
            this.clip = var1;
            this.effectiveGain = var1.getEffectiveVolumeInMenu();
            javafmod.FMOD_Channel_SetVolume(this.channel, this.effectiveGain);
            javafmod.FMOD_Channel_SetPitch(this.channel, var1.pitch);
            if (var2.isLooped()) {
               javafmod.FMOD_Channel_SetMode(this.channel, (long)FMODManager.FMOD_LOOP_NORMAL);
            }

            javafmod.FMOD_Channel_SetPaused(this.channel, false);
            return true;
         }
      }

      public boolean isPlaying() {
         return this.channel == 0L ? false : javafmod.FMOD_Channel_IsPlaying(this.channel);
      }

      public boolean update() {
         if (this.channel == 0L) {
            return false;
         } else if (!javafmod.FMOD_Channel_IsPlaying(this.channel)) {
            this.channel = 0L;
            this.clip = null;
            return true;
         } else {
            float var1 = this.clip.getEffectiveVolumeInMenu();
            if (this.effectiveGain != var1) {
               this.effectiveGain = var1;
               javafmod.FMOD_Channel_SetVolume(this.channel, this.effectiveGain);
            }

            return false;
         }
      }

      public void stop() {
         if (this.channel != 0L) {
            javafmod.FMOD_Channel_Stop(this.channel);
            this.channel = 0L;
            this.clip = null;
         }
      }
   }
}

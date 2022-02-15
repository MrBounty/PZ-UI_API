package zombie;

import fmod.javafmod;
import fmod.javafmodJNI;
import fmod.fmod.Audio;
import fmod.fmod.FMODAudio;
import fmod.fmod.FMODManager;
import fmod.fmod.FMODSoundEmitter;
import fmod.fmod.FMOD_STUDIO_EVENT_CALLBACK;
import fmod.fmod.FMOD_STUDIO_EVENT_CALLBACK_TYPE;
import fmod.fmod.FMOD_STUDIO_PARAMETER_DESCRIPTION;
import fmod.fmod.FMOD_STUDIO_PLAYBACK_STATE;
import fmod.fmod.IFMODParameterUpdater;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.HashSet;
import java.util.Iterator;
import zombie.audio.BaseSoundEmitter;
import zombie.audio.FMODParameter;
import zombie.audio.FMODParameterList;
import zombie.audio.GameSound;
import zombie.audio.GameSoundClip;
import zombie.audio.parameters.ParameterMusicLibrary;
import zombie.audio.parameters.ParameterMusicState;
import zombie.audio.parameters.ParameterMusicWakeState;
import zombie.audio.parameters.ParameterMusicZombiesTargeting;
import zombie.audio.parameters.ParameterMusicZombiesVisible;
import zombie.characters.IsoPlayer;
import zombie.core.Core;
import zombie.core.Rand;
import zombie.debug.DebugLog;
import zombie.gameStates.MainScreenState;
import zombie.iso.IsoGridSquare;
import zombie.iso.IsoObject;
import zombie.iso.IsoUtils;
import zombie.iso.IsoWorld;
import zombie.network.GameClient;
import zombie.network.GameServer;
import zombie.scripting.ScriptManager;
import zombie.scripting.objects.Item;
import zombie.scripting.objects.ScriptModule;
import zombie.util.StringUtils;

public final class SoundManager extends BaseSoundManager implements IFMODParameterUpdater {
   public float SoundVolume = 0.8F;
   public float MusicVolume = 0.36F;
   public float AmbientVolume = 0.8F;
   public float VehicleEngineVolume = 0.5F;
   private final ParameterMusicLibrary parameterMusicLibrary = new ParameterMusicLibrary();
   private final ParameterMusicState parameterMusicState = new ParameterMusicState();
   private final ParameterMusicWakeState parameterMusicWakeState = new ParameterMusicWakeState();
   private final ParameterMusicZombiesTargeting parameterMusicZombiesTargeting = new ParameterMusicZombiesTargeting();
   private final ParameterMusicZombiesVisible parameterMusicZombiesVisible = new ParameterMusicZombiesVisible();
   private final FMODParameterList fmodParameters = new FMODParameterList();
   private boolean initialized = false;
   private long inGameGroupBus = 0L;
   private long musicGroupBus = 0L;
   private FMODSoundEmitter musicEmitter = null;
   private long musicCombinedEvent = 0L;
   private FMODSoundEmitter uiEmitter = null;
   private final SoundManager.Music music = new SoundManager.Music();
   public ArrayList ambientPieces = new ArrayList();
   private boolean muted = false;
   private long[] bankList = new long[32];
   private long[] eventDescList = new long[256];
   private long[] eventInstList = new long[256];
   private long[] pausedEventInstances = new long[128];
   private float[] pausedEventVolumes = new float[128];
   private int pausedEventCount;
   private final HashSet emitters = new HashSet();
   private static ArrayList ambientSoundEffects = new ArrayList();
   public static BaseSoundManager instance;
   private String currentMusicName;
   private String currentMusicLibrary;
   private final FMOD_STUDIO_EVENT_CALLBACK musicEventCallback = new FMOD_STUDIO_EVENT_CALLBACK() {
      public void timelineMarker(long var1, String var3, int var4) {
         DebugLog.Sound.debugln("timelineMarker %s %d", var3, var4);
         if ("Lightning".equals(var3)) {
            MainScreenState.getInstance().lightningTimelineMarker = true;
         }

      }
   };

   public FMODParameterList getFMODParameters() {
      return this.fmodParameters;
   }

   public void startEvent(long var1, GameSoundClip var3, BitSet var4) {
      FMODParameterList var5 = this.getFMODParameters();
      ArrayList var6 = var3.eventDescription.parameters;

      for(int var7 = 0; var7 < var6.size(); ++var7) {
         FMOD_STUDIO_PARAMETER_DESCRIPTION var8 = (FMOD_STUDIO_PARAMETER_DESCRIPTION)var6.get(var7);
         if (!var4.get(var8.globalIndex)) {
            FMODParameter var9 = var5.get(var8);
            if (var9 != null) {
               var9.startEventInstance(var1);
            }
         }
      }

   }

   public void updateEvent(long var1, GameSoundClip var3) {
   }

   public void stopEvent(long var1, GameSoundClip var3, BitSet var4) {
      FMODParameterList var5 = this.getFMODParameters();
      ArrayList var6 = var3.eventDescription.parameters;

      for(int var7 = 0; var7 < var6.size(); ++var7) {
         FMOD_STUDIO_PARAMETER_DESCRIPTION var8 = (FMOD_STUDIO_PARAMETER_DESCRIPTION)var6.get(var7);
         if (!var4.get(var8.globalIndex)) {
            FMODParameter var9 = var5.get(var8);
            if (var9 != null) {
               var9.stopEventInstance(var1);
            }
         }
      }

   }

   public boolean isRemastered() {
      int var1 = Core.getInstance().getOptionMusicLibrary();
      return var1 == 1 || var1 == 3 && Rand.Next(2) == 0;
   }

   public void BlendVolume(Audio var1, float var2) {
   }

   public void BlendVolume(Audio var1, float var2, float var3) {
   }

   public Audio BlendThenStart(Audio var1, float var2, String var3) {
      return null;
   }

   public void FadeOutMusic(String var1, int var2) {
   }

   public void PlayAsMusic(String var1, Audio var2, float var3, boolean var4) {
   }

   public long playUISound(String var1) {
      GameSound var2 = GameSounds.getSound(var1);
      if (var2 != null && !var2.clips.isEmpty()) {
         GameSoundClip var3 = var2.getRandomClip();
         long var4 = this.uiEmitter.playClip(var3, (IsoObject)null);
         this.uiEmitter.tick();
         javafmod.FMOD_System_Update();
         return var4;
      } else {
         return 0L;
      }
   }

   public boolean isPlayingUISound(String var1) {
      return this.uiEmitter.isPlaying(var1);
   }

   public boolean isPlayingUISound(long var1) {
      return this.uiEmitter.isPlaying(var1);
   }

   public void stopUISound(long var1) {
      this.uiEmitter.stopSound(var1);
   }

   public boolean IsMusicPlaying() {
      return false;
   }

   public boolean isPlayingMusic() {
      return this.music.isPlaying();
   }

   public ArrayList getAmbientPieces() {
      return this.ambientPieces;
   }

   private void gatherInGameEventInstances() {
      this.pausedEventCount = 0;
      int var1 = javafmodJNI.FMOD_Studio_System_GetBankCount();
      if (this.bankList.length < var1) {
         this.bankList = new long[var1];
      }

      var1 = javafmodJNI.FMOD_Studio_System_GetBankList(this.bankList);

      for(int var2 = 0; var2 < var1; ++var2) {
         int var3 = javafmodJNI.FMOD_Studio_Bank_GetEventCount(this.bankList[var2]);
         if (this.eventDescList.length < var3) {
            this.eventDescList = new long[var3];
         }

         var3 = javafmodJNI.FMOD_Studio_Bank_GetEventList(this.bankList[var2], this.eventDescList);

         for(int var4 = 0; var4 < var3; ++var4) {
            int var5 = javafmodJNI.FMOD_Studio_EventDescription_GetInstanceCount(this.eventDescList[var4]);
            if (this.eventInstList.length < var5) {
               this.eventInstList = new long[var5];
            }

            var5 = javafmodJNI.FMOD_Studio_EventDescription_GetInstanceList(this.eventDescList[var4], this.eventInstList);

            for(int var6 = 0; var6 < var5; ++var6) {
               int var7 = javafmod.FMOD_Studio_GetPlaybackState(this.eventInstList[var6]);
               if (var7 != FMOD_STUDIO_PLAYBACK_STATE.FMOD_STUDIO_PLAYBACK_STOPPED.index) {
                  boolean var8 = javafmodJNI.FMOD_Studio_EventInstance_GetPaused(this.eventInstList[var6]);
                  if (!var8) {
                     if (this.pausedEventInstances.length < this.pausedEventCount + 1) {
                        this.pausedEventInstances = Arrays.copyOf(this.pausedEventInstances, this.pausedEventCount + 128);
                        this.pausedEventVolumes = Arrays.copyOf(this.pausedEventVolumes, this.pausedEventInstances.length);
                     }

                     this.pausedEventInstances[this.pausedEventCount] = this.eventInstList[var6];
                     this.pausedEventVolumes[this.pausedEventCount] = javafmodJNI.FMOD_Studio_EventInstance_GetVolume(this.eventInstList[var6]);
                     ++this.pausedEventCount;
                  }
               }
            }
         }
      }

   }

   public void pauseSoundAndMusic() {
      boolean var1 = true;
      if (GameClient.bClient) {
         this.muted = true;
         if (var1) {
            javafmod.FMOD_Studio_Bus_SetMute(this.inGameGroupBus, true);
            javafmod.FMOD_Studio_Bus_SetMute(this.musicGroupBus, true);
         } else {
            this.setSoundVolume(0.0F);
            this.setMusicVolume(0.0F);
            this.setAmbientVolume(0.0F);
            this.setVehicleEngineVolume(0.0F);
         }

         GameSounds.soundIsPaused = true;
      } else if (var1) {
         javafmod.FMOD_Studio_Bus_SetPaused(this.inGameGroupBus, true);
         javafmod.FMOD_Studio_Bus_SetPaused(this.musicGroupBus, true);
         javafmod.FMOD_Channel_SetPaused(FMODManager.instance.channelGroupInGameNonBankSounds, true);
         GameSounds.soundIsPaused = true;
      } else {
         long var2 = javafmod.FMOD_System_GetMasterChannelGroup();
         javafmod.FMOD_ChannelGroup_SetPaused(var2, true);
         javafmod.FMOD_ChannelGroup_SetVolume(var2, 0.0F);
         javafmodJNI.FMOD_Studio_System_FlushCommands();
         this.gatherInGameEventInstances();

         for(int var4 = 0; var4 < this.pausedEventCount; ++var4) {
            javafmodJNI.FMOD_Studio_EventInstance_SetPaused(this.pausedEventInstances[var4], true);
         }

         javafmod.FMOD_Channel_SetPaused(FMODManager.instance.channelGroupInGameNonBankSounds, true);
         javafmod.FMOD_ChannelGroup_SetPaused(var2, false);
         javafmodJNI.FMOD_Studio_System_FlushCommands();
         javafmod.FMOD_ChannelGroup_SetVolume(var2, 1.0F);
         GameSounds.soundIsPaused = true;
      }
   }

   public void resumeSoundAndMusic() {
      boolean var1 = true;
      if (this.muted) {
         this.muted = false;
         if (var1) {
            javafmod.FMOD_Studio_Bus_SetMute(this.inGameGroupBus, false);
            javafmod.FMOD_Studio_Bus_SetMute(this.musicGroupBus, false);
            javafmod.FMOD_ChannelGroup_SetPaused(FMODManager.instance.channelGroupInGameNonBankSounds, false);
         } else {
            this.setSoundVolume((float)Core.getInstance().getOptionSoundVolume() / 10.0F);
            this.setMusicVolume((float)Core.getInstance().getOptionMusicVolume() / 10.0F);
            this.setAmbientVolume((float)Core.getInstance().getOptionAmbientVolume() / 10.0F);
            this.setVehicleEngineVolume((float)Core.getInstance().getOptionVehicleEngineVolume() / 10.0F);
         }

         GameSounds.soundIsPaused = false;
      } else if (var1) {
         javafmod.FMOD_Studio_Bus_SetPaused(this.inGameGroupBus, false);
         javafmod.FMOD_Studio_Bus_SetPaused(this.musicGroupBus, false);
         javafmod.FMOD_ChannelGroup_SetPaused(FMODManager.instance.channelGroupInGameNonBankSounds, false);
         GameSounds.soundIsPaused = false;
      } else {
         long var2 = javafmod.FMOD_System_GetMasterChannelGroup();
         javafmod.FMOD_ChannelGroup_SetPaused(var2, true);
         javafmodJNI.FMOD_Studio_System_FlushCommands();

         for(int var4 = 0; var4 < this.pausedEventCount; ++var4) {
            try {
               javafmodJNI.FMOD_Studio_EventInstance_SetPaused(this.pausedEventInstances[var4], false);
            } catch (Throwable var6) {
               var6.printStackTrace();
            }
         }

         this.pausedEventCount = 0;
         javafmod.FMOD_ChannelGroup_SetPaused(var2, false);
         javafmod.FMOD_ChannelGroup_SetVolume(var2, 1.0F);
         javafmod.FMOD_ChannelGroup_SetPaused(FMODManager.instance.channelGroupInGameNonBankSounds, false);
         GameSounds.soundIsPaused = false;
      }
   }

   private void debugScriptSound(Item var1, String var2) {
      if (var2 != null && !var2.isEmpty()) {
         if (!GameSounds.isKnownSound(var2)) {
            DebugLog.General.warn("no such sound \"" + var2 + "\" in item " + var1.getFullName());
         }

      }
   }

   public void debugScriptSounds() {
      if (Core.bDebug) {
         Iterator var1 = ScriptManager.instance.ModuleMap.values().iterator();

         while(var1.hasNext()) {
            ScriptModule var2 = (ScriptModule)var1.next();
            Iterator var3 = var2.ItemMap.values().iterator();

            while(var3.hasNext()) {
               Item var4 = (Item)var3.next();
               this.debugScriptSound(var4, var4.getBreakSound());
               this.debugScriptSound(var4, var4.getBulletOutSound());
               this.debugScriptSound(var4, var4.getCloseSound());
               this.debugScriptSound(var4, var4.getCustomEatSound());
               this.debugScriptSound(var4, var4.getDoorHitSound());
               this.debugScriptSound(var4, var4.getCountDownSound());
               this.debugScriptSound(var4, var4.getExplosionSound());
               this.debugScriptSound(var4, var4.getImpactSound());
               this.debugScriptSound(var4, var4.getOpenSound());
               this.debugScriptSound(var4, var4.getPutInSound());
               this.debugScriptSound(var4, var4.getShellFallSound());
               this.debugScriptSound(var4, var4.getSwingSound());
            }
         }

      }
   }

   public void registerEmitter(BaseSoundEmitter var1) {
      this.emitters.add(var1);
   }

   public void unregisterEmitter(BaseSoundEmitter var1) {
      this.emitters.remove(var1);
   }

   public boolean isListenerInRange(float var1, float var2, float var3) {
      if (GameServer.bServer) {
         return false;
      } else {
         for(int var4 = 0; var4 < IsoPlayer.numPlayers; ++var4) {
            IsoPlayer var5 = IsoPlayer.players[var4];
            if (var5 != null && !var5.Traits.Deaf.isSet() && IsoUtils.DistanceToSquared(var5.x, var5.y, var1, var2) < var3 * var3) {
               return true;
            }
         }

         return false;
      }
   }

   public void playNightAmbient(String var1) {
      DebugLog.log("playNightAmbient: " + var1);

      for(int var2 = 0; var2 < ambientSoundEffects.size(); ++var2) {
         SoundManager.AmbientSoundEffect var3 = (SoundManager.AmbientSoundEffect)ambientSoundEffects.get(var2);
         if (var3.getName().equals(var1)) {
            var3.setVolume((float)Rand.Next(700, 1500) / 1000.0F);
            var3.start();
            this.ambientPieces.add(var3);
            return;
         }
      }

      SoundManager.AmbientSoundEffect var4 = new SoundManager.AmbientSoundEffect(var1);
      var4.setVolume((float)Rand.Next(700, 1500) / 1000.0F);
      var4.setName(var1);
      var4.start();
      this.ambientPieces.add(var4);
      ambientSoundEffects.add(var4);
   }

   public void playMusic(String var1) {
      this.DoMusic(var1, false);
   }

   public void playAmbient(String var1) {
   }

   public void playMusicNonTriggered(String var1, float var2) {
   }

   public void stopMusic(String var1) {
      if (this.isPlayingMusic()) {
         if (StringUtils.isNullOrWhitespace(var1) || var1.equalsIgnoreCase(this.getCurrentMusicName())) {
            this.StopMusic();
         }

      }
   }

   public void CheckDoMusic() {
   }

   public float getMusicPosition() {
      return this.isPlayingMusic() ? this.music.getPosition() : 0.0F;
   }

   public void DoMusic(String var1, boolean var2) {
      if (this.AllowMusic && Core.getInstance().getOptionMusicVolume() != 0) {
         if (this.isPlayingMusic()) {
            this.StopMusic();
         }

         int var3 = Core.getInstance().getOptionMusicLibrary();
         boolean var4 = var3 == 1;
         GameSound var5 = GameSounds.getSound(var1);
         GameSoundClip var6 = null;
         if (var5 != null && !var5.clips.isEmpty()) {
            var6 = var5.getRandomClip();
         }

         long var7;
         if (var6 != null && var6.getEvent() != null) {
            if (var6.eventDescription != null) {
               var7 = var6.eventDescription.address;
               javafmod.FMOD_Studio_LoadEventSampleData(var7);
               this.music.instance = javafmod.FMOD_Studio_System_CreateEventInstance(var7);
               this.music.clip = var6;
               this.music.effectiveVolume = var6.getEffectiveVolume();
               javafmod.FMOD_Studio_EventInstance_SetParameterByName(this.music.instance, "Volume", 10.0F);
               javafmod.FMOD_Studio_EventInstance_SetVolume(this.music.instance, this.music.effectiveVolume);
               javafmod.FMOD_Studio_StartEvent(this.music.instance);
            }
         } else if (var6 != null && var6.getFile() != null) {
            var7 = FMODManager.instance.loadSound(var6.getFile());
            if (var7 > 0L) {
               this.music.channel = javafmod.FMOD_System_PlaySound(var7, true);
               this.music.clip = var6;
               this.music.effectiveVolume = var6.getEffectiveVolume();
               javafmod.FMOD_Channel_SetVolume(this.music.channel, this.music.effectiveVolume);
               javafmod.FMOD_Channel_SetPitch(this.music.channel, var6.pitch);
               javafmod.FMOD_Channel_SetPaused(this.music.channel, false);
            }
         }

         this.currentMusicName = var1;
         this.currentMusicLibrary = var4 ? "official" : "earlyaccess";
      }
   }

   public void PlayAsMusic(String var1, Audio var2, boolean var3, float var4) {
   }

   public void setMusicState(String var1) {
      byte var3 = -1;
      switch(var1.hashCode()) {
      case -2101240105:
         if (var1.equals("InGame")) {
            var3 = 2;
         }
         break;
      case -1461787563:
         if (var1.equals("PauseMenu")) {
            var3 = 3;
         }
         break;
      case 55996120:
         if (var1.equals("MainMenu")) {
            var3 = 0;
         }
         break;
      case 257920894:
         if (var1.equals("Tutorial")) {
            var3 = 4;
         }
         break;
      case 2001303836:
         if (var1.equals("Loading")) {
            var3 = 1;
         }
      }

      switch(var3) {
      case 0:
         this.parameterMusicState.setState(ParameterMusicState.State.MainMenu);
         break;
      case 1:
         this.parameterMusicState.setState(ParameterMusicState.State.Loading);
         break;
      case 2:
         this.parameterMusicState.setState(ParameterMusicState.State.InGame);
         break;
      case 3:
         this.parameterMusicState.setState(ParameterMusicState.State.PauseMenu);
         break;
      case 4:
         this.parameterMusicState.setState(ParameterMusicState.State.Tutorial);
         break;
      default:
         DebugLog.General.warn("unknown MusicState \"%s\"", var1);
      }

   }

   public void setMusicWakeState(IsoPlayer var1, String var2) {
      byte var4 = -1;
      switch(var2.hashCode()) {
      case -1321219637:
         if (var2.equals("Sleeping")) {
            var4 = 1;
         }
         break;
      case 63670629:
         if (var2.equals("Awake")) {
            var4 = 0;
         }
         break;
      case 138562507:
         if (var2.equals("WakeNormal")) {
            var4 = 2;
         }
         break;
      case 1582036347:
         if (var2.equals("WakeNightmare")) {
            var4 = 3;
         }
         break;
      case 2055642281:
         if (var2.equals("WakeZombies")) {
            var4 = 4;
         }
      }

      switch(var4) {
      case 0:
         this.parameterMusicWakeState.setState(var1, ParameterMusicWakeState.State.Awake);
         break;
      case 1:
         this.parameterMusicWakeState.setState(var1, ParameterMusicWakeState.State.Sleeping);
         break;
      case 2:
         this.parameterMusicWakeState.setState(var1, ParameterMusicWakeState.State.WakeNormal);
         break;
      case 3:
         this.parameterMusicWakeState.setState(var1, ParameterMusicWakeState.State.WakeNightmare);
         break;
      case 4:
         this.parameterMusicWakeState.setState(var1, ParameterMusicWakeState.State.WakeZombies);
         break;
      default:
         DebugLog.General.warn("unknown MusicWakeState \"%s\"", var2);
      }

   }

   public Audio PlayMusic(String var1, String var2, boolean var3, float var4) {
      return null;
   }

   public Audio PlaySound(String var1, boolean var2, float var3, float var4) {
      return null;
   }

   public Audio PlaySound(String var1, boolean var2, float var3) {
      if (GameServer.bServer) {
         return null;
      } else if (IsoWorld.instance == null) {
         return null;
      } else {
         BaseSoundEmitter var4 = IsoWorld.instance.getFreeEmitter();
         var4.setPos(0.0F, 0.0F, 0.0F);
         long var5 = var4.playSound(var1);
         return var5 != 0L ? new FMODAudio(var4) : null;
      }
   }

   public Audio PlaySoundEvenSilent(String var1, boolean var2, float var3) {
      return null;
   }

   public Audio PlayJukeboxSound(String var1, boolean var2, float var3) {
      return null;
   }

   public Audio PlaySoundWav(String var1, boolean var2, float var3, float var4) {
      return null;
   }

   public Audio PlaySoundWav(String var1, boolean var2, float var3) {
      return null;
   }

   public Audio PlaySoundWav(String var1, int var2, boolean var3, float var4) {
      return null;
   }

   public void update3D() {
   }

   public Audio PlayWorldSound(String var1, IsoGridSquare var2, float var3, float var4, float var5, boolean var6) {
      return this.PlayWorldSound(var1, false, var2, var3, var4, var5, var6);
   }

   public Audio PlayWorldSound(String var1, boolean var2, IsoGridSquare var3, float var4, float var5, float var6, boolean var7) {
      if (!GameServer.bServer && var3 != null) {
         if (GameClient.bClient) {
            GameClient.instance.PlayWorldSound(var1, var3.x, var3.y, (byte)var3.z);
         }

         return this.PlayWorldSoundImpl(var1, var2, var3.getX(), var3.getY(), var3.getZ(), var4, var5, var6, var7);
      } else {
         return null;
      }
   }

   public Audio PlayWorldSoundImpl(String var1, boolean var2, int var3, int var4, int var5, float var6, float var7, float var8, boolean var9) {
      BaseSoundEmitter var10 = IsoWorld.instance.getFreeEmitter((float)var3 + 0.5F, (float)var4 + 0.5F, (float)var5);
      var10.playSoundImpl(var1, (IsoObject)null);
      return new FMODAudio(var10);
   }

   public Audio PlayWorldSound(String var1, IsoGridSquare var2, float var3, float var4, float var5, int var6, boolean var7) {
      return this.PlayWorldSound(var1, var2, var3, var4, var5, var7);
   }

   public Audio PlayWorldSoundWav(String var1, IsoGridSquare var2, float var3, float var4, float var5, boolean var6) {
      return this.PlayWorldSoundWav(var1, false, var2, var3, var4, var5, var6);
   }

   public Audio PlayWorldSoundWav(String var1, boolean var2, IsoGridSquare var3, float var4, float var5, float var6, boolean var7) {
      if (!GameServer.bServer && var3 != null) {
         if (GameClient.bClient) {
            GameClient.instance.PlayWorldSound(var1, var3.getX(), var3.getY(), (byte)var3.getZ());
         }

         return this.PlayWorldSoundWavImpl(var1, var2, var3, var4, var5, var6, var7);
      } else {
         return null;
      }
   }

   public Audio PlayWorldSoundWavImpl(String var1, boolean var2, IsoGridSquare var3, float var4, float var5, float var6, boolean var7) {
      BaseSoundEmitter var8 = IsoWorld.instance.getFreeEmitter((float)var3.getX() + 0.5F, (float)var3.getY() + 0.5F, (float)var3.getZ());
      var8.playSound(var1);
      return new FMODAudio(var8);
   }

   public void PlayWorldSoundWav(String var1, IsoGridSquare var2, float var3, float var4, float var5, int var6, boolean var7) {
      Integer var8 = Rand.Next(var6) + 1;
      this.PlayWorldSoundWav(var1 + var8.toString(), var2, var3, var4, var5, var7);
   }

   public Audio PrepareMusic(String var1) {
      return null;
   }

   public Audio Start(Audio var1, float var2, String var3) {
      return null;
   }

   public void Update() {
      if (!this.initialized) {
         this.initialized = true;
         this.inGameGroupBus = javafmod.FMOD_Studio_System_GetBus("bus:/InGame");
         this.musicGroupBus = javafmod.FMOD_Studio_System_GetBus("bus:/Music");
         this.musicEmitter = new FMODSoundEmitter();
         this.musicEmitter.parameterUpdater = this;
         this.fmodParameters.add(this.parameterMusicLibrary);
         this.fmodParameters.add(this.parameterMusicState);
         this.fmodParameters.add(this.parameterMusicWakeState);
         this.fmodParameters.add(this.parameterMusicZombiesTargeting);
         this.fmodParameters.add(this.parameterMusicZombiesVisible);
         this.uiEmitter = new FMODSoundEmitter();
      }

      FMODSoundEmitter.update();
      this.updateMusic();
      this.uiEmitter.tick();

      for(int var1 = 0; var1 < this.ambientPieces.size(); ++var1) {
         Audio var2 = (Audio)this.ambientPieces.get(var1);
         if (IsoPlayer.allPlayersDead()) {
            var2.stop();
         }

         if (!var2.isPlaying()) {
            var2.stop();
            this.ambientPieces.remove(var2);
            --var1;
         } else if (var2 instanceof SoundManager.AmbientSoundEffect) {
            ((SoundManager.AmbientSoundEffect)var2).update();
         }
      }

      AmbientStreamManager.instance.update();
      if (!this.AllowMusic) {
         this.StopMusic();
      }

      if (this.music.isPlaying()) {
         this.music.update();
      }

      FMODManager.instance.tick();
   }

   protected boolean HasMusic(Audio var1) {
      return false;
   }

   public void Purge() {
   }

   public void stop() {
      Iterator var1 = this.emitters.iterator();

      while(var1.hasNext()) {
         BaseSoundEmitter var2 = (BaseSoundEmitter)var1.next();
         var2.stopAll();
      }

      this.emitters.clear();
      long var3 = javafmod.FMOD_System_GetMasterChannelGroup();
      javafmod.FMOD_ChannelGroup_Stop(var3);
      this.pausedEventCount = 0;
   }

   public void StopMusic() {
      this.music.stop();
   }

   public void StopSound(Audio var1) {
      var1.stop();
   }

   public void CacheSound(String var1) {
   }

   public void update4() {
   }

   public void update2() {
   }

   public void update3() {
   }

   public void update1() {
   }

   public void setSoundVolume(float var1) {
      this.SoundVolume = var1;
   }

   public float getSoundVolume() {
      return this.SoundVolume;
   }

   public void setAmbientVolume(float var1) {
      this.AmbientVolume = var1;
   }

   public float getAmbientVolume() {
      return this.AmbientVolume;
   }

   public void setMusicVolume(float var1) {
      this.MusicVolume = var1;
      if (!this.muted) {
         ;
      }
   }

   public float getMusicVolume() {
      return this.MusicVolume;
   }

   public void setVehicleEngineVolume(float var1) {
      this.VehicleEngineVolume = var1;
   }

   public float getVehicleEngineVolume() {
      return this.VehicleEngineVolume;
   }

   public String getCurrentMusicName() {
      return this.isPlayingMusic() ? this.currentMusicName : null;
   }

   public String getCurrentMusicLibrary() {
      return this.isPlayingMusic() ? this.currentMusicLibrary : null;
   }

   private void updateMusic() {
      this.fmodParameters.update();
      if (!this.musicEmitter.isPlaying(this.musicCombinedEvent)) {
         this.musicCombinedEvent = this.musicEmitter.playSound("MusicCombined");
         if (this.musicCombinedEvent != 0L && !System.getProperty("os.name").contains("OS X")) {
            javafmod.FMOD_Studio_EventInstance_SetCallback(this.musicCombinedEvent, this.musicEventCallback, FMOD_STUDIO_EVENT_CALLBACK_TYPE.FMOD_STUDIO_EVENT_CALLBACK_TIMELINE_MARKER.bit);
         }
      }

      if (this.musicEmitter.isPlaying(this.musicCombinedEvent)) {
         this.musicEmitter.setVolume(this.musicCombinedEvent, this.AllowMusic ? this.getMusicVolume() : 0.0F);
      }

      this.musicEmitter.tick();
   }

   private static final class Music {
      public GameSoundClip clip;
      public long instance;
      public long channel;
      public long sound;
      public float effectiveVolume;

      public boolean isPlaying() {
         if (this.instance != 0L) {
            int var1 = javafmod.FMOD_Studio_GetPlaybackState(this.instance);
            return var1 != FMOD_STUDIO_PLAYBACK_STATE.FMOD_STUDIO_PLAYBACK_STOPPED.index && var1 != FMOD_STUDIO_PLAYBACK_STATE.FMOD_STUDIO_PLAYBACK_STOPPING.index;
         } else {
            return this.channel != 0L && javafmod.FMOD_Channel_IsPlaying(this.channel);
         }
      }

      public void update() {
         this.clip = this.clip.checkReloaded();
         float var1 = this.clip.getEffectiveVolume();
         if (this.effectiveVolume != var1) {
            this.effectiveVolume = var1;
            if (this.instance != 0L) {
               javafmod.FMOD_Studio_EventInstance_SetVolume(this.instance, this.effectiveVolume);
            }

            if (this.channel != 0L) {
               javafmod.FMOD_Channel_SetVolume(this.channel, this.effectiveVolume);
            }

         }
      }

      public float getPosition() {
         long var1;
         if (this.instance != 0L) {
            var1 = javafmod.FMOD_Studio_GetTimelinePosition(this.instance);
            return (float)var1;
         } else if (this.channel != 0L) {
            var1 = javafmod.FMOD_Channel_GetPosition(this.channel, FMODManager.FMOD_TIMEUNIT_MS);
            return (float)var1;
         } else {
            return 0.0F;
         }
      }

      public void stop() {
         if (this.instance != 0L) {
            javafmod.FMOD_Studio_EventInstance_Stop(this.instance, false);
            javafmod.FMOD_Studio_ReleaseEventInstance(this.instance);
            this.instance = 0L;
         }

         if (this.channel != 0L) {
            javafmod.FMOD_Channel_Stop(this.channel);
            this.channel = 0L;
            javafmod.FMOD_Sound_Release(this.sound);
            this.sound = 0L;
         }

      }
   }

   public static final class AmbientSoundEffect implements Audio {
      public String name;
      public long eventInstance;
      public float gain;
      public GameSoundClip clip;
      public float effectiveVolume;

      public AmbientSoundEffect(String var1) {
         GameSound var2 = GameSounds.getSound(var1);
         if (var2 != null && !var2.clips.isEmpty()) {
            GameSoundClip var3 = var2.getRandomClip();
            if (var3.getEvent() != null) {
               if (var3.eventDescription != null) {
                  this.eventInstance = javafmod.FMOD_Studio_System_CreateEventInstance(var3.eventDescription.address);
                  if (this.eventInstance >= 0L) {
                     this.clip = var3;
                  }
               }
            }
         }
      }

      public void setVolume(float var1) {
         if (this.eventInstance > 0L) {
            this.gain = var1;
            this.effectiveVolume = this.clip.getEffectiveVolume();
            javafmod.FMOD_Studio_EventInstance_SetVolume(this.eventInstance, this.gain * this.effectiveVolume);
         }
      }

      public void start() {
         if (this.eventInstance > 0L) {
            javafmod.FMOD_Studio_StartEvent(this.eventInstance);
         }
      }

      public void pause() {
      }

      public void stop() {
         DebugLog.log("stop ambient " + this.name);
         if (this.eventInstance > 0L) {
            javafmod.FMOD_Studio_EventInstance_Stop(this.eventInstance, false);
         }
      }

      public boolean isPlaying() {
         if (this.eventInstance <= 0L) {
            return false;
         } else {
            int var1 = javafmod.FMOD_Studio_GetPlaybackState(this.eventInstance);
            return var1 == FMOD_STUDIO_PLAYBACK_STATE.FMOD_STUDIO_PLAYBACK_STARTING.index || var1 == FMOD_STUDIO_PLAYBACK_STATE.FMOD_STUDIO_PLAYBACK_PLAYING.index || var1 == FMOD_STUDIO_PLAYBACK_STATE.FMOD_STUDIO_PLAYBACK_SUSTAINING.index;
         }
      }

      public void setName(String var1) {
         this.name = var1;
      }

      public String getName() {
         return this.name;
      }

      public void update() {
         if (this.clip != null) {
            this.clip = this.clip.checkReloaded();
            float var1 = this.clip.getEffectiveVolume();
            if (this.effectiveVolume != var1) {
               this.effectiveVolume = var1;
               javafmod.FMOD_Studio_EventInstance_SetVolume(this.eventInstance, this.gain * this.effectiveVolume);
            }

         }
      }
   }
}

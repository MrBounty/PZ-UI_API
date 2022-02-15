package zombie.core.raknet;

import fmod.FMODSoundBuffer;
import fmod.FMOD_DriverInfo;
import fmod.FMOD_RESULT;
import fmod.SoundBuffer;
import fmod.javafmod;
import fmod.fmod.FMODManager;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.Semaphore;
import java.util.stream.Collectors;
import se.krka.kahlua.vm.JavaFunction;
import se.krka.kahlua.vm.KahluaTable;
import se.krka.kahlua.vm.LuaCallFrame;
import se.krka.kahlua.vm.Platform;
import zombie.characters.IsoPlayer;
import zombie.core.Core;
import zombie.debug.DebugLog;
import zombie.input.GameKeyboard;
import zombie.inventory.InventoryItem;
import zombie.inventory.types.Radio;
import zombie.iso.IsoCell;
import zombie.iso.IsoGridSquare;
import zombie.iso.IsoUtils;
import zombie.iso.objects.IsoWorldInventoryObject;
import zombie.network.GameClient;
import zombie.network.GameServer;
import zombie.network.ServerOptions;

public class VoiceManager {
   public static VoiceManager instance = new VoiceManager();
   private static int qulity;
   private static final int pcmsize = 2;
   private static final int bufferSize = 192;
   private static boolean serverVOIPEnable = true;
   private static int discretizate = 16000;
   private static int period = 300;
   private static int complexity = 1;
   private static int buffering = 8000;
   private static float minDistance;
   private static float maxDistance;
   private static boolean is3D = false;
   private Radio myRadio = null;
   private RakVoice voice;
   private int voice_bufsize;
   private long recSound;
   private static FMODSoundBuffer recBuf;
   private boolean startInit = false;
   private boolean initialiseRecDev = false;
   private boolean initialisedRecDev = false;
   private Semaphore RecDevSemaphore;
   private boolean isModeVAD = false;
   private boolean isModePPT = false;
   private int vadMode = 3;
   private int volumeMic;
   private int volumePlayers;
   public static boolean VoipDisabled = false;
   private boolean isEnable = true;
   private boolean isDebug = false;
   private boolean isDebugLoopback = false;
   private boolean isDebugLoopbackLong = false;
   private long fmod_channel_group_voip = 0L;
   private int FMODVoiceRecordDriverId;
   private byte[] serverbuf = null;
   private Thread thread;
   private boolean bQuit;
   private long time_last;
   private boolean isServer;
   private long indicator_is_voice = 0L;
   public static long tem_t;
   public static final int modePPT = 1;
   public static final int modeVAD = 2;
   public static final int modeMute = 3;
   public static final int VADModeQuality = 1;
   public static final int VADModeLowBitrate = 2;
   public static final int VADModeAggressive = 3;
   public static final int VADModeVeryAggressive = 4;
   byte[] buf = new byte[192];
   private final Long recBuf_Current_read = new Long(0L);
   private final Object notifier = new Object();
   private boolean bIsClient = false;
   private boolean bTestingMicrophone = false;
   private long testingMicrophoneMS = 0L;
   private static long timestamp = 0L;

   public static VoiceManager getInstance() {
      return instance;
   }

   int VoiceInitClient() {
      DebugLog.log("[VOICE MANAGER] VoiceInit");
      this.isServer = false;
      this.voice = new RakVoice();
      if (this.voice == null) {
         return -1;
      } else {
         this.RecDevSemaphore = new Semaphore(1);
         recBuf = null;
         this.voice_bufsize = 192;
         RakVoice var10000 = this.voice;
         RakVoice.RVInit(this.voice_bufsize);
         var10000 = this.voice;
         RakVoice.SetComplexity(complexity);
         return 0;
      }
   }

   int VoiceInitServer(boolean var1, int var2, int var3, int var4, int var5, double var6, double var8, boolean var10) {
      DebugLog.log("[VOICE MANAGER] VoiceInit");
      this.isServer = true;
      if (!(var3 == 2 | var3 == 5 | var3 == 10 | var3 == 20 | var3 == 40 | var3 == 60)) {
         DebugLog.log("[VOICE MANAGER] VoiceInit ERROR: invalid period");
         return -1;
      } else if (!(var2 == 8000 | var2 == 16000 | var2 == 24000)) {
         DebugLog.log("[VOICE MANAGER] VoiceInit ERROR: invalid samplerate");
         return -1;
      } else if (var4 < 0 | var4 > 10) {
         DebugLog.log("[VOICE MANAGER] VoiceInit ERROR: invalid qulity");
         return -1;
      } else if (var5 < 0 | var5 > 32000) {
         DebugLog.log("[VOICE MANAGER] VoiceInit ERROR: invalid buffering");
         return -1;
      } else {
         this.voice = new RakVoice();
         if (this.voice == null) {
            DebugLog.log("[VOICE MANAGER] VoiceInit ERROR: RakVoice internal error");
            return -1;
         } else {
            discretizate = var2;
            RakVoice var10000 = this.voice;
            RakVoice.RVInitServer(var1, var2, var3, var4, var5, (float)var6, (float)var8, var10);
            return 0;
         }
      }
   }

   int VoiceDeinit() {
      DebugLog.log("[VOICE MANAGER] VoiceDeinit");
      RakVoice var10000 = this.voice;
      RakVoice.CloseAllChannels();
      var10000 = this.voice;
      RakVoice.RVDeinit();
      return 0;
   }

   int VoiceConnectAccept(long var1) {
      if (!this.isEnable) {
         return 0;
      } else {
         DebugLog.log("[VOICE MANAGER] VoiceConnectAccept uuid=" + var1);
         return 0;
      }
   }

   int InitRecDeviceForTest() {
      try {
         this.RecDevSemaphore.acquire();
      } catch (InterruptedException var2) {
         var2.printStackTrace();
      }

      this.recSound = javafmod.FMOD_System_CreateRecordSound((long)this.FMODVoiceRecordDriverId, (long)(FMODManager.FMOD_2D | FMODManager.FMOD_OPENUSER | FMODManager.FMOD_SOFTWARE), (long)FMODManager.FMOD_SOUND_FORMAT_PCM16, (long)discretizate);
      if (this.recSound == 0L) {
         DebugLog.log("[VOICE MANAGER] Error: FMOD_System_CreateSound return zero");
      }

      DebugLog.log("[VOICE MANAGER] FMOD_System_CreateSound OK");
      javafmod.FMOD_System_SetRecordVolume(1L - Math.round(Math.pow(1.4D, (double)(11 - this.volumeMic))));
      if (this.initialiseRecDev) {
         int var1 = javafmod.FMOD_System_RecordStart(this.FMODVoiceRecordDriverId, this.recSound, true);
         if (var1 != FMOD_RESULT.FMOD_OK.ordinal()) {
            DebugLog.log("[VOICE MANAGER] Error: FMOD_System_RecordStart return " + var1);
         }
      }

      javafmod.FMOD_System_SetVADMode(this.vadMode - 1);
      recBuf = new FMODSoundBuffer(this.recSound);
      this.initialisedRecDev = true;
      this.RecDevSemaphore.release();
      return 0;
   }

   int VoiceOpenChannelReply(long var1) {
      if (!this.isEnable) {
         return 0;
      } else {
         DebugLog.log("[VOICE MANAGER] VoiceOpenChannelReply uuid=" + var1);
         if (this.isServer) {
            return 0;
         } else {
            try {
               this.RecDevSemaphore.acquire();
            } catch (InterruptedException var6) {
               var6.printStackTrace();
            }

            this.initialisedRecDev = false;
            RakVoice var10000 = this.voice;
            serverVOIPEnable = RakVoice.GetServerVOIPEnable();
            var10000 = this.voice;
            discretizate = RakVoice.GetSampleRate();
            var10000 = this.voice;
            period = RakVoice.GetSendFramePeriod();
            var10000 = this.voice;
            buffering = RakVoice.GetBuffering();
            var10000 = this.voice;
            minDistance = RakVoice.GetMinDistance();
            var10000 = this.voice;
            maxDistance = RakVoice.GetMaxDistance();
            var10000 = this.voice;
            is3D = RakVoice.GetIs3D();
            ArrayList var3 = VoiceManagerData.data;

            int var4;
            for(var4 = 0; var4 < var3.size(); ++var4) {
               VoiceManagerData var5 = (VoiceManagerData)var3.get(var4);
               if (var5.userplaysound != 0L) {
                  if (is3D) {
                     javafmod.FMOD_Sound_SetMode(var5.userplaysound, FMODManager.FMOD_3D | FMODManager.FMOD_OPENUSER | FMODManager.FMOD_LOOP_NORMAL | FMODManager.FMOD_CREATESTREAM);
                  } else {
                     javafmod.FMOD_Sound_SetMode(var5.userplaysound, FMODManager.FMOD_OPENUSER | FMODManager.FMOD_LOOP_NORMAL | FMODManager.FMOD_CREATESTREAM);
                  }
               }
            }

            DebugLog.log("[VOICE MANAGER] VoiceOpenChannelReply discretizate=" + discretizate);
            DebugLog.log("[VOICE MANAGER] VoiceOpenChannelReply period=" + period);
            DebugLog.log("[VOICE MANAGER] VoiceOpenChannelReply buffering=" + buffering);
            if (javafmod.FMOD_System_SetRawPlayBufferingPeriod((long)buffering) != (long)FMOD_RESULT.FMOD_OK.ordinal()) {
               DebugLog.log("[VOICE MANAGER] Error: FMOD_System_SetRawPlayBufferingPeriod ");
            }

            if (this.recSound != 0L) {
               var4 = javafmod.FMOD_Sound_Release(this.recSound);
               if (var4 != FMOD_RESULT.FMOD_OK.ordinal()) {
                  DebugLog.log("[VOICE MANAGER] Error: FMOD_Sound_Release return " + var4);
               }

               this.recSound = 0L;
            }

            this.recSound = javafmod.FMOD_System_CreateRecordSound((long)this.FMODVoiceRecordDriverId, (long)(FMODManager.FMOD_2D | FMODManager.FMOD_OPENUSER | FMODManager.FMOD_SOFTWARE), (long)FMODManager.FMOD_SOUND_FORMAT_PCM16, (long)discretizate);
            if (this.recSound == 0L) {
               DebugLog.log("[VOICE MANAGER] Error: FMOD_System_CreateSound return zero");
            }

            DebugLog.log("[VOICE MANAGER] FMOD_System_CreateSound OK");
            javafmod.FMOD_System_SetRecordVolume(1L - Math.round(Math.pow(1.4D, (double)(11 - this.volumeMic))));
            if (this.initialiseRecDev) {
               var4 = javafmod.FMOD_System_RecordStart(this.FMODVoiceRecordDriverId, this.recSound, true);
               if (var4 != FMOD_RESULT.FMOD_OK.ordinal()) {
                  DebugLog.log("[VOICE MANAGER] Error: FMOD_System_RecordStart return " + var4);
               }
            }

            javafmod.FMOD_System_SetVADMode(this.vadMode - 1);
            recBuf = new FMODSoundBuffer(this.recSound);
            if (this.isDebug) {
               VoiceDebug.createAndShowGui();
            }

            this.initialisedRecDev = true;
            this.RecDevSemaphore.release();
            return 0;
         }
      }
   }

   int VoiceConnectReq(long var1) {
      if (!this.isEnable) {
         return 0;
      } else {
         DebugLog.log("[VOICE MANAGER] VoiceConnectReq uuid=" + var1);
         RakVoice var10000 = this.voice;
         RakVoice.RequestVoiceChannel(var1);
         return 0;
      }
   }

   int VoiceConnectClose(long var1) {
      if (!this.isEnable) {
         return 0;
      } else {
         DebugLog.log("[VOICE MANAGER] VoiceConnectClose uuid=" + var1);
         RakVoice var10000 = this.voice;
         RakVoice.CloseVoiceChannel(var1);
         return 0;
      }
   }

   public static void GetDataCallbackRnd(int var0, short[] var1) {
      Random var2 = new Random();

      for(int var3 = 0; var3 < var1.length; ++var3) {
         var1[var3] = (short)var2.nextInt();
      }

   }

   public static void GetDataCallback100Hz(short[] var0) {
      DebugLog.log("[VOICE MANAGER] GetDataCallback: datasize=" + var0.length);

      for(int var3 = 0; var3 < var0.length; ++var3) {
         double var1 = 6.283185307179586D * ((double)tem_t / 8000.0D) * 100.0D;
         var0[var3] = (short)((int)(Math.sin(var1) * 16000.0D));
         tem_t = (tem_t + 1L) % 8000L;
      }

   }

   public void setMode(int var1) {
      if (var1 == 3) {
         this.isModeVAD = false;
         this.isModePPT = false;
      } else if (var1 == 1) {
         this.isModeVAD = false;
         this.isModePPT = true;
      } else if (var1 == 2) {
         this.isModeVAD = true;
         this.isModePPT = false;
      }

   }

   public void setVADMode(int var1) {
      if (!(var1 < 1 | var1 > 4)) {
         this.vadMode = var1;
         if (this.initialisedRecDev) {
            javafmod.FMOD_System_SetVADMode(this.vadMode - 1);
         }
      }
   }

   public void setVolumePlayers(int var1) {
      if (!(var1 < 0 | var1 > 11)) {
         if (var1 <= 10) {
            this.volumePlayers = var1;
         } else {
            this.volumePlayers = 12;
         }

         if (this.initialisedRecDev) {
            ArrayList var2 = VoiceManagerData.data;

            for(int var3 = 0; var3 < var2.size(); ++var3) {
               VoiceManagerData var4 = (VoiceManagerData)var2.get(var3);
               if (var4 != null && var4.userplaychannel != 0L) {
                  javafmod.FMOD_Channel_SetVolume(var4.userplaychannel, (float)((double)this.volumePlayers * 0.2D));
               }
            }

         }
      }
   }

   public void setVolumeMic(int var1) {
      if (!(var1 < 0 | var1 > 11)) {
         if (var1 <= 10) {
            this.volumeMic = var1;
         } else {
            this.volumeMic = 12;
         }

         if (this.initialisedRecDev) {
            javafmod.FMOD_System_SetRecordVolume(1L - Math.round(Math.pow(1.4D, (double)(11 - this.volumeMic))));
         }
      }
   }

   public static void playerSetMute(String var0) {
      ArrayList var1 = GameClient.instance.getPlayers();

      for(int var2 = 0; var2 < var1.size(); ++var2) {
         IsoPlayer var3 = (IsoPlayer)var1.get(var2);
         if (var0.equals(var3.username)) {
            VoiceManagerData var4 = VoiceManagerData.get(var3.OnlineID);
            var4.userplaymute = !var4.userplaymute;
            var3.isVoiceMute = var4.userplaymute;
            break;
         }
      }

   }

   public static boolean playerGetMute(String var0) {
      ArrayList var2 = GameClient.instance.getPlayers();

      for(int var3 = 0; var3 < var2.size(); ++var3) {
         IsoPlayer var4 = (IsoPlayer)var2.get(var3);
         if (var0.equals(var4.username)) {
            boolean var1 = VoiceManagerData.get(var4.OnlineID).userplaymute;
            return var1;
         }
      }

      return true;
   }

   public void LuaRegister(Platform var1, KahluaTable var2) {
      KahluaTable var3 = var1.newTable();
      var3.rawset("playerSetMute", new JavaFunction() {
         public int call(LuaCallFrame var1, int var2) {
            Object var3 = var1.get(1);
            VoiceManager.playerSetMute((String)var3);
            return 1;
         }
      });
      var3.rawset("playerGetMute", new JavaFunction() {
         public int call(LuaCallFrame var1, int var2) {
            Object var3 = var1.get(1);
            var1.push(VoiceManager.playerGetMute((String)var3));
            return 1;
         }
      });
      var3.rawset("RecordDevices", new JavaFunction() {
         public int call(LuaCallFrame var1, int var2) {
            if (!Core.SoundDisabled && !VoiceManager.VoipDisabled) {
               int var7 = javafmod.FMOD_System_GetRecordNumDrivers();
               KahluaTable var4 = var1.getPlatform().newTable();

               for(int var5 = 0; var5 < var7; ++var5) {
                  FMOD_DriverInfo var6 = new FMOD_DriverInfo();
                  javafmod.FMOD_System_GetRecordDriverInfo(var5, var6);
                  var4.rawset(var5 + 1, var6.name);
               }

               var1.push(var4);
               return 1;
            } else {
               KahluaTable var3 = var1.getPlatform().newTable();
               var1.push(var3);
               return 1;
            }
         }
      });
      var2.rawset("VoiceManager", var3);
   }

   private long getuserplaysound(short var1) {
      VoiceManagerData var2 = VoiceManagerData.get(var1);
      if (var2.userplaychannel == 0L) {
         var2.userplaysound = 0L;
         if (is3D) {
            var2.userplaysound = javafmod.FMOD_System_CreateRAWPlaySound((long)(FMODManager.FMOD_3D | FMODManager.FMOD_OPENUSER | FMODManager.FMOD_LOOP_NORMAL | FMODManager.FMOD_CREATESTREAM), (long)FMODManager.FMOD_SOUND_FORMAT_PCM16, (long)discretizate);
         } else {
            var2.userplaysound = javafmod.FMOD_System_CreateRAWPlaySound((long)(FMODManager.FMOD_OPENUSER | FMODManager.FMOD_LOOP_NORMAL | FMODManager.FMOD_CREATESTREAM), (long)FMODManager.FMOD_SOUND_FORMAT_PCM16, (long)discretizate);
         }

         if (var2.userplaysound == 0L) {
            DebugLog.log("[VOICE MANAGER] Error: FMOD_System_CreateSound return zero");
         }

         var2.userplaychannel = javafmod.FMOD_System_PlaySound(var2.userplaysound, false);
         if (var2.userplaychannel == 0L) {
            DebugLog.log("[VOICE MANAGER] Error: FMOD_System_PlaySound return zero");
         }

         javafmod.FMOD_Channel_SetVolume(var2.userplaychannel, (float)((double)this.volumePlayers * 0.2D));
         javafmod.FMOD_Channel_Set3DMinMaxDistance(var2.userplaychannel, minDistance, maxDistance);
         javafmod.FMOD_Channel_SetChannelGroup(var2.userplaychannel, this.fmod_channel_group_voip);
      }

      return var2.userplaysound;
   }

   public void InitVMClient() {
      if (!Core.SoundDisabled && !VoipDisabled) {
         int var1 = javafmod.FMOD_System_GetRecordNumDrivers();
         this.FMODVoiceRecordDriverId = Core.getInstance().getOptionVoiceRecordDevice() - 1;
         if (this.FMODVoiceRecordDriverId < 0 && var1 > 0) {
            Core.getInstance().setOptionVoiceRecordDevice(1);
            this.FMODVoiceRecordDriverId = Core.getInstance().getOptionVoiceRecordDevice() - 1;
         }

         if (var1 < 1) {
            DebugLog.log("[VOICE MANAGER] Any microphone not found");
            this.initialiseRecDev = false;
         } else if (this.FMODVoiceRecordDriverId < 0 | this.FMODVoiceRecordDriverId >= var1) {
            DebugLog.log("[VOICE MANAGER] Invalid record device");
            this.initialiseRecDev = false;
         } else {
            this.initialiseRecDev = true;
         }

         DebugLog.log("[VOICE MANAGER] Init: Start");
         this.isEnable = Core.getInstance().getOptionVoiceEnable();
         this.setMode(Core.getInstance().getOptionVoiceMode());
         this.vadMode = Core.getInstance().getOptionVoiceVADMode();
         this.volumeMic = Core.getInstance().getOptionVoiceVolumeMic();
         this.volumePlayers = Core.getInstance().getOptionVoiceVolumePlayers();
         if (!this.isEnable) {
            DebugLog.log("[VOICE MANAGER] Disabled");
         } else {
            this.fmod_channel_group_voip = javafmod.FMOD_System_CreateChannelGroup("VOIP");
            this.VoiceInitClient();
            this.recSound = 0L;
            this.InitRecDeviceForTest();
            if (this.isDebug) {
               VoiceDebug.createAndShowGui();
            }

            DebugLog.log("[VOICE MANAGER] Init: End");
            this.time_last = System.currentTimeMillis();
            this.bQuit = false;
            this.thread = new Thread() {
               public void run() {
                  while(!VoiceManager.this.bQuit && !VoiceManager.this.bQuit) {
                     try {
                        VoiceManager.this.UpdateVMClient();
                        sleep((long)(VoiceManager.period / 2));
                     } catch (Exception var2) {
                        var2.printStackTrace();
                     }
                  }

               }
            };
            this.thread.setName("VoiceManagerClient");
            this.thread.start();
         }
      } else {
         this.isEnable = false;
         this.initialiseRecDev = false;
         this.initialisedRecDev = false;
         DebugLog.log("[VOICE MANAGER] Init: Disable");
      }
   }

   public void loadConfig() {
      this.isEnable = Core.getInstance().getOptionVoiceEnable();
      this.setMode(Core.getInstance().getOptionVoiceMode());
      this.vadMode = Core.getInstance().getOptionVoiceVADMode();
      this.volumeMic = Core.getInstance().getOptionVoiceVolumeMic();
      this.volumePlayers = Core.getInstance().getOptionVoiceVolumePlayers();
   }

   public void UpdateRecordDevice() {
      if (this.initialisedRecDev) {
         int var1 = javafmod.FMOD_System_RecordStop(this.FMODVoiceRecordDriverId);
         if (var1 != FMOD_RESULT.FMOD_OK.ordinal()) {
            DebugLog.log("[VOICE MANAGER] Error: FMOD_System_RecordStop return " + var1);
         }

         this.FMODVoiceRecordDriverId = Core.getInstance().getOptionVoiceRecordDevice() - 1;
         if (this.FMODVoiceRecordDriverId < 0) {
            DebugLog.log("[VOICE MANAGER] Error: No record device found");
         } else {
            var1 = javafmod.FMOD_System_RecordStart(this.FMODVoiceRecordDriverId, this.recSound, true);
            if (var1 != FMOD_RESULT.FMOD_OK.ordinal()) {
               DebugLog.log("[VOICE MANAGER] Error: FMOD_System_RecordStart return " + var1);
            }

         }
      }
   }

   public void DeinitVMClient() {
      if (this.thread != null) {
         this.bQuit = true;
         synchronized(this.notifier) {
            this.notifier.notify();
         }

         while(this.thread.isAlive()) {
            try {
               Thread.sleep(10L);
            } catch (InterruptedException var4) {
            }
         }

         this.thread = null;
      }

      if (this.recSound != 0L) {
         javafmod.FMOD_RecordSound_Release(this.recSound);
      }

      ArrayList var1 = VoiceManagerData.data;

      for(int var2 = 0; var2 < var1.size(); ++var2) {
         VoiceManagerData var3 = (VoiceManagerData)var1.get(var2);
         if (var3.userplaychannel != 0L) {
            javafmod.FMOD_Channel_Stop(var3.userplaychannel);
         }

         if (var3.userplaysound != 0L) {
            javafmod.FMOD_RAWPlaySound_Release(var3.userplaysound);
            var3.userplaysound = 0L;
         }
      }

      VoiceManagerData.data.clear();
   }

   void debug_print(byte[] var1, Long var2) {
      long[] var3 = new long[16];
      long var4 = var2 / 16L;

      for(int var6 = 0; var6 < 16; ++var6) {
         var3[var6] = 0L;

         for(int var7 = 0; (long)var7 < var4; ++var7) {
            var3[var6] += (long)var1[var6 * (int)var4 + var7];
         }
      }

      DebugLog.log("[VOICE MANAGER] UpdateVMClient: loc abuf1: " + var3[0] + ", " + var3[2] + ", " + var3[3] + ", " + var3[4] + ", " + var3[5] + ", " + var3[6] + ", " + var3[7] + ", " + var3[8] + ", " + var3[9] + ", " + var3[10] + ", " + var3[11] + ", " + var3[12] + ", " + var3[13] + ", " + var3[14] + ", " + var3[15]);
   }

   public void setTestingMicrophone(boolean var1) {
      if (var1) {
         this.testingMicrophoneMS = System.currentTimeMillis();
      }

      if (var1 != this.bTestingMicrophone) {
         this.bTestingMicrophone = var1;
         this.notifyThread();
      }

   }

   public void notifyThread() {
      synchronized(this.notifier) {
         this.notifier.notify();
      }
   }

   public void update() {
      if (!GameServer.bServer) {
         if (this.bTestingMicrophone) {
            long var1 = System.currentTimeMillis();
            if (var1 - this.testingMicrophoneMS > 1000L) {
               this.setTestingMicrophone(false);
            }
         }

         if (GameClient.bClient && GameClient.connection != null) {
            if (!this.bIsClient) {
               this.bIsClient = true;
               this.notifyThread();
            }
         } else if (this.bIsClient) {
            this.bIsClient = false;
            this.notifyThread();
         }

      }
   }

   synchronized void UpdateVMClient() throws InterruptedException {
      for(; !this.bQuit && !this.bIsClient && !this.bTestingMicrophone; DebugLog.log("[VOICE MANAGER] UpdateVMClient woke up")) {
         DebugLog.log("[VOICE MANAGER] UpdateVMClient going to sleep");
         synchronized(this.notifier) {
            try {
               this.notifier.wait();
            } catch (InterruptedException var10) {
            }
         }
      }

      if (serverVOIPEnable) {
         if (IsoPlayer.getInstance() != null) {
            IsoPlayer.getInstance().isSpeek = System.currentTimeMillis() - this.indicator_is_voice <= 300L;
         }

         GameClient var10000;
         RakVoice var16;
         if (this.initialiseRecDev) {
            this.RecDevSemaphore.acquire();
            javafmod.FMOD_System_GetRecordPosition(this.FMODVoiceRecordDriverId, this.recBuf_Current_read);
            if (recBuf != null) {
               label184:
               while(true) {
                  while(true) {
                     if (!recBuf.pull(this.recBuf_Current_read)) {
                        break label184;
                     }

                     if (IsoPlayer.getInstance() == null) {
                        break;
                     }

                     var10000 = GameClient.instance;
                     if (GameClient.connection == null) {
                        break;
                     }

                     if (!is3D || !IsoPlayer.getInstance().isDead()) {
                        if (this.isModePPT && GameKeyboard.isKeyDown(Core.getInstance().getKey("Enable voice transmit"))) {
                           var16 = this.voice;
                           var10000 = GameClient.instance;
                           RakVoice.SendFrame(GameClient.connection.connectedGUID, (long)IsoPlayer.getInstance().OnlineID, recBuf.buf(), recBuf.get_size());
                           this.indicator_is_voice = System.currentTimeMillis();
                        }

                        if (this.isModeVAD && recBuf.get_vad() != 0L) {
                           var16 = this.voice;
                           var10000 = GameClient.instance;
                           RakVoice.SendFrame(GameClient.connection.connectedGUID, (long)IsoPlayer.getInstance().OnlineID, recBuf.buf(), recBuf.get_size());
                           this.indicator_is_voice = System.currentTimeMillis();
                        }
                        break;
                     }
                  }

                  if (this.isDebug) {
                     if (GameClient.IDToPlayerMap.values().size() > 0) {
                        VoiceDebug.updateGui((SoundBuffer)null, recBuf);
                     } else if (this.isDebugLoopback) {
                        VoiceDebug.updateGui((SoundBuffer)null, recBuf);
                     } else {
                        VoiceDebug.updateGui((SoundBuffer)null, recBuf);
                     }
                  }

                  if (this.isDebugLoopback) {
                     javafmod.FMOD_System_RAWPlayData(this.getuserplaysound((short)0), recBuf.buf(), recBuf.get_size());
                  }
               }
            }

            this.RecDevSemaphore.release();
         }

         ArrayList var3 = GameClient.instance.getPlayers();
         ArrayList var4 = VoiceManagerData.data;

         int var5;
         for(var5 = 0; var5 < var4.size(); ++var5) {
            VoiceManagerData var6 = (VoiceManagerData)var4.get(var5);
            boolean var7 = false;

            for(int var8 = 0; var8 < var3.size(); ++var8) {
               IsoPlayer var9 = (IsoPlayer)var3.get(var8);
               if (var9.OnlineID == var6.index) {
                  var7 = true;
                  break;
               }
            }

            if (this.isDebugLoopback & var6.index == 0) {
               break;
            }

            if (var6.userplaychannel != 0L & !var7) {
               javafmod.FMOD_Channel_Stop(var6.userplaychannel);
               var6.userplaychannel = 0L;
            }
         }

         long var1 = System.currentTimeMillis() - this.time_last;
         if (var1 >= (long)period) {
            this.time_last += var1;
            if (IsoPlayer.getInstance() == null) {
               return;
            }

            for(var5 = 0; var5 < var3.size(); ++var5) {
               IsoPlayer var12 = (IsoPlayer)var3.get(var5);
               if (var12 != IsoPlayer.getInstance()) {
                  VoiceManagerData var13 = VoiceManagerData.get(var12.OnlineID);

                  while(true) {
                     var16 = this.voice;
                     if (!RakVoice.ReceiveFrame((long)var12.OnlineID, this.buf)) {
                        if (var13.voicetimeout == 0L) {
                           var12.isSpeek = false;
                        } else {
                           --var13.voicetimeout;
                           var12.isSpeek = true;
                        }
                        break;
                     }

                     var13.voicetimeout = 10L;
                     if (!var13.userplaymute) {
                        if (IsoPlayer.getInstance().isCanHearAll()) {
                           javafmod.FMOD_Channel_Set3DAttributes(var13.userplaychannel, IsoPlayer.getInstance().x, IsoPlayer.getInstance().y, IsoPlayer.getInstance().z, 0.0F, 0.0F, 0.0F);
                        } else if (is3D) {
                           ArrayList var14 = this.checkForNearbyRadios(var12);
                           boolean var15 = true;
                           logFrame(var12, this.myRadio);
                           if (this.myRadio != null) {
                              javafmod.FMOD_Channel_SetVolume(var13.userplaychannel, this.myRadio.getDeviceData().getDeviceVolume());
                           } else {
                              javafmod.FMOD_Channel_SetVolume(var13.userplaychannel, 1.0F);
                           }

                           if (!var14.isEmpty()) {
                              javafmod.FMOD_Channel_Set3DAttributes(var13.userplaychannel, (float)(Integer)var14.get(0), (float)(Integer)var14.get(1), (float)(Integer)var14.get(2), 0.0F, 0.0F, 0.0F);
                           } else {
                              javafmod.FMOD_Channel_Set3DAttributes(var13.userplaychannel, var12.x, var12.y, var12.z, 0.0F, 0.0F, 0.0F);
                           }
                        }

                        javafmod.FMOD_System_RAWPlayData(this.getuserplaysound(var12.OnlineID), this.buf, (long)this.buf.length);
                        if (this.isDebugLoopbackLong) {
                           var16 = this.voice;
                           var10000 = GameClient.instance;
                           RakVoice.SendFrame(GameClient.connection.connectedGUID, (long)IsoPlayer.getInstance().OnlineID, this.buf, (long)this.buf.length);
                        }
                     }
                  }
               }
            }
         }

      }
   }

   private static void logFrame(IsoPlayer var0, Radio var1) {
      if (GameClient.bClient && IsoPlayer.getInstance() != null && var0 != null) {
         float var2 = IsoUtils.DistanceTo(IsoPlayer.getInstance().getX(), IsoPlayer.getInstance().getY(), var0.getX(), var0.getY());
         if (var2 > maxDistance) {
            long var3 = System.currentTimeMillis();
            if (var3 > timestamp) {
               timestamp = var3 + 5000L;
               DebugLog.Multiplayer.warn(String.format("player \"%s\" (cheat=%b) freqs=[%s] received VOIP frame from distant player \"%s\" (cheat=%b) freqs=[%s] at distance=%f with radio=%b", IsoPlayer.getInstance().getUsername(), IsoPlayer.getInstance().isCanHearAll(), IsoPlayer.getInstance().invRadioFreq.stream().map(Object::toString).collect(Collectors.joining(", ")), var0.getUsername(), var0.isCanHearAll(), var0.invRadioFreq.stream().map(Object::toString).collect(Collectors.joining(", ")), var2, var1 != null));
            }
         }
      }

   }

   private ArrayList checkForNearbyRadios(IsoPlayer var1) {
      ArrayList var2 = new ArrayList();
      this.myRadio = null;
      IsoPlayer var3 = IsoPlayer.getInstance();

      int var4;
      for(var4 = 0; var4 < var3.getInventory().getItems().size(); ++var4) {
         InventoryItem var5 = (InventoryItem)var3.getInventory().getItems().get(var4);
         if (var5 instanceof Radio) {
            Radio var6 = (Radio)var5;
            if (var6.getDeviceData() != null && var6.getDeviceData().getIsTurnedOn() && var1.invRadioFreq.contains(var6.getDeviceData().getChannel())) {
               var2.add((int)var3.x);
               var2.add((int)var3.y);
               var2.add((int)var3.z);
               this.myRadio = var6;
               break;
            }
         }
      }

      if (var2.isEmpty()) {
         for(var4 = (int)var3.getX() - 4; (float)var4 < var3.getX() + 5.0F; ++var4) {
            for(int var11 = (int)var3.getY() - 4; (float)var11 < var3.getY() + 5.0F; ++var11) {
               for(int var12 = (int)var3.getZ() - 1; (float)var12 < var3.getZ() + 1.0F; ++var12) {
                  IsoGridSquare var7 = IsoCell.getInstance().getGridSquare(var4, var11, var12);
                  if (var7 != null && var7.getWorldObjects() != null) {
                     for(int var8 = 0; var8 < var7.getWorldObjects().size(); ++var8) {
                        IsoWorldInventoryObject var9 = (IsoWorldInventoryObject)var7.getWorldObjects().get(var8);
                        if (var9.getItem() != null && var9.getItem() instanceof Radio) {
                           Radio var10 = (Radio)var9.getItem();
                           if (var10.getDeviceData() != null && var10.getDeviceData().getIsTurnedOn() && var1.invRadioFreq.contains(var10.getDeviceData().getChannel())) {
                              var2.add(var7.x);
                              var2.add(var7.y);
                              var2.add(var7.z);
                              this.myRadio = var10;
                              break;
                           }
                        }
                     }
                  }
               }
            }
         }
      }

      return var2;
   }

   void InitVMServer() {
      this.VoiceInitServer(ServerOptions.instance.VoiceEnable.getValue(), ServerOptions.instance.VoiceSampleRate.getValue(), ServerOptions.instance.VoicePeriod.getValue(), ServerOptions.instance.VoiceComplexity.getValue(), ServerOptions.instance.VoiceBuffering.getValue(), ServerOptions.instance.VoiceMinDistance.getValue(), ServerOptions.instance.VoiceMaxDistance.getValue(), ServerOptions.instance.Voice3D.getValue());
   }

   public int getMicVolumeIndicator() {
      return recBuf == null ? 0 : (int)recBuf.get_loudness();
   }

   public boolean getMicVolumeError() {
      return recBuf == null ? true : recBuf.get_interror();
   }

   public boolean getServerVOIPEnable() {
      return serverVOIPEnable;
   }

   public void VMServerBan(short var1, boolean var2) {
      RakVoice var10000 = this.voice;
      RakVoice.SetVoiceBan((long)var1, var2);
   }
}

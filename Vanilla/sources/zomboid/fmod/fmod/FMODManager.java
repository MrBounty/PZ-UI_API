package fmod.fmod;

import fmod.javafmod;
import fmod.javafmodJNI;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import zombie.ZomboidFileSystem;
import zombie.characters.IsoPlayer;
import zombie.core.Core;
import zombie.core.Rand;
import zombie.core.logger.ExceptionLogger;
import zombie.debug.DebugLog;
import zombie.iso.Vector2;

public class FMODManager {
   public static FMODManager instance = new FMODManager();
   public static int FMOD_STUDIO_INIT_NORMAL = 0;
   public static int FMOD_STUDIO_INIT_LIVEUPDATE = 1;
   public static int FMOD_STUDIO_INIT_ALLOW_MISSING_PLUGINS = 2;
   public static int FMOD_STUDIO_INIT_SYNCHRONOUS_UPDATE = 4;
   public static int FMOD_STUDIO_INIT_DEFERRED_CALLBACKS = 8;
   public static int FMOD_INIT_NORMAL = 0;
   public static int FMOD_INIT_STREAM_FROM_UPDATE = 1;
   public static int FMOD_INIT_MIX_FROM_UPDATE = 2;
   public static int FMOD_INIT_3D_RIGHTHANDED = 4;
   public static int FMOD_INIT_CHANNEL_LOWPASS = 256;
   public static int FMOD_INIT_CHANNEL_DISTANCEFILTER = 512;
   public static int FMOD_INIT_PROFILE_ENABLE = 65536;
   public static int FMOD_INIT_VOL0_BECOMES_VIRTUAL = 131072;
   public static int FMOD_INIT_GEOMETRY_USECLOSEST = 262144;
   public static int FMOD_INIT_PREFER_DOLBY_DOWNMIX = 524288;
   public static int FMOD_INIT_THREAD_UNSAFE = 1048576;
   public static int FMOD_INIT_PROFILE_METER_ALL = 2097152;
   public static int FMOD_DEFAULT = 0;
   public static int FMOD_LOOP_OFF = 1;
   public static int FMOD_LOOP_NORMAL = 2;
   public static int FMOD_LOOP_BIDI = 4;
   public static int FMOD_2D = 8;
   public static int FMOD_3D = 16;
   public static int FMOD_HARDWARE = 32;
   public static int FMOD_SOFTWARE = 64;
   public static int FMOD_CREATESTREAM = 128;
   public static int FMOD_CREATESAMPLE = 256;
   public static int FMOD_CREATECOMPRESSEDSAMPLE = 512;
   public static int FMOD_OPENUSER = 1024;
   public static int FMOD_OPENMEMORY = 2048;
   public static int FMOD_OPENMEMORY_POINT = 268435456;
   public static int FMOD_OPENRAW = 4096;
   public static int FMOD_OPENONLY = 8192;
   public static int FMOD_ACCURATETIME = 16384;
   public static int FMOD_MPEGSEARCH = 32768;
   public static int FMOD_NONBLOCKING = 65536;
   public static int FMOD_UNIQUE = 131072;
   public static int FMOD_3D_HEADRELATIVE = 262144;
   public static int FMOD_3D_WORLDRELATIVE = 524288;
   public static int FMOD_3D_INVERSEROLLOFF = 1048576;
   public static int FMOD_3D_LINEARROLLOFF = 2097152;
   public static int FMOD_3D_LINEARSQUAREROLLOFF = 4194304;
   public static int FMOD_3D_INVERSETAPEREDROLLOFF = 8388608;
   public static int FMOD_3D_CUSTOMROLLOFF = 67108864;
   public static int FMOD_3D_IGNOREGEOMETRY = 1073741824;
   public static int FMOD_IGNORETAGS = 33554432;
   public static int FMOD_LOWMEM = 134217728;
   public static int FMOD_LOADSECONDARYRAM = 536870912;
   public static int FMOD_VIRTUAL_PLAYFROMSTART = Integer.MIN_VALUE;
   public static int FMOD_PRESET_OFF = 0;
   public static int FMOD_PRESET_GENERIC = 1;
   public static int FMOD_PRESET_PADDEDCELL = 2;
   public static int FMOD_PRESET_ROOM = 3;
   public static int FMOD_PRESET_BATHROOM = 4;
   public static int FMOD_PRESET_LIVINGROOM = 5;
   public static int FMOD_PRESET_STONEROOM = 6;
   public static int FMOD_PRESET_AUDITORIUM = 7;
   public static int FMOD_PRESET_CONCERTHALL = 8;
   public static int FMOD_PRESET_CAVE = 9;
   public static int FMOD_PRESET_ARENA = 10;
   public static int FMOD_PRESET_HANGAR = 11;
   public static int FMOD_PRESET_CARPETTEDHALLWAY = 12;
   public static int FMOD_PRESET_HALLWAY = 13;
   public static int FMOD_PRESET_STONECORRIDOR = 14;
   public static int FMOD_PRESET_ALLEY = 15;
   public static int FMOD_PRESET_FOREST = 16;
   public static int FMOD_PRESET_CITY = 17;
   public static int FMOD_PRESET_MOUNTAINS = 18;
   public static int FMOD_PRESET_QUARRY = 19;
   public static int FMOD_PRESET_PLAIN = 20;
   public static int FMOD_PRESET_PARKINGLOT = 21;
   public static int FMOD_PRESET_SEWERPIPE = 22;
   public static int FMOD_PRESET_UNDERWATER = 23;
   public static int FMOD_TIMEUNIT_MS = 1;
   public static int FMOD_TIMEUNIT_PCM = 2;
   public static int FMOD_TIMEUNIT_PCMBYTES = 4;
   public static int FMOD_STUDIO_PLAYBACK_PLAYING = 0;
   public static int FMOD_STUDIO_PLAYBACK_SUSTAINING = 1;
   public static int FMOD_STUDIO_PLAYBACK_STOPPED = 2;
   public static int FMOD_STUDIO_PLAYBACK_STARTING = 3;
   public static int FMOD_STUDIO_PLAYBACK_STOPPING = 4;
   public static int FMOD_SOUND_FORMAT_NONE = 0;
   public static int FMOD_SOUND_FORMAT_PCM8 = 1;
   public static int FMOD_SOUND_FORMAT_PCM16 = 2;
   public static int FMOD_SOUND_FORMAT_PCM24 = 3;
   public static int FMOD_SOUND_FORMAT_PCM32 = 4;
   public static int FMOD_SOUND_FORMAT_PCMFLOAT = 5;
   public static int FMOD_SOUND_FORMAT_BITSTREAM = 6;
   ArrayList Sounds = new ArrayList();
   ArrayList Instances = new ArrayList();
   public long FMOD_system = 0L;
   public long channelGroupInGameNonBankSounds = 0L;
   private final HashMap parameterDescriptionMap = new HashMap();
   private final HashMap eventDescriptionMap = new HashMap();
   int c = 0;
   Vector2 move = new Vector2(0.0F, 0.0F);
   Vector2 pos = new Vector2(-400.0F, -400.0F);
   float x = 0.0F;
   float y = 0.0F;
   float z = 0.0F;
   float vx = 0.02F;
   float vy = 0.01F;
   float vz = 0.0F;
   private int numListeners = 1;
   private final HashMap fileToSoundMap = new HashMap();
   private final int[] reverbPreset = new int[]{-1, -1, -1, -1};

   public void init() {
      javafmodJNI.init();
      this.FMOD_system = (long)javafmod.FMOD_System_Create();
      int var1 = Core.bDebug ? FMOD_STUDIO_INIT_LIVEUPDATE : 0;
      int var2 = Core.bDebug ? FMOD_INIT_PROFILE_ENABLE | FMOD_INIT_PROFILE_METER_ALL : 0;
      javafmod.FMOD_System_Init(1024, (long)(FMOD_STUDIO_INIT_NORMAL | var1), (long)(FMOD_INIT_NORMAL | FMOD_INIT_CHANNEL_DISTANCEFILTER | FMOD_INIT_CHANNEL_LOWPASS | FMOD_INIT_VOL0_BECOMES_VIRTUAL | var2));
      javafmod.FMOD_System_Set3DSettings(1.0F, 1.0F, 1.0F);
      this.loadBank("ZomboidSound.strings");
      this.loadBank("ZomboidMusic");
      this.loadBank("ZomboidSound");
      this.channelGroupInGameNonBankSounds = javafmod.FMOD_System_CreateChannelGroup("InGameNonBank");
      this.initGlobalParameters();
      this.initEvents();
   }

   private String bankPath(String var1) throws IOException {
      return (new File("./media/sound/banks/Desktop/" + var1 + ".bank")).getCanonicalFile().getPath();
   }

   private long loadBank(String var1) {
      try {
         String var2 = this.bankPath(var1);
         long var3 = javafmod.FMOD_Studio_System_LoadBankFile(var2);
         if (var3 > 0L) {
            javafmod.FMOD_Studio_LoadSampleData(var3);
         }

         return var3;
      } catch (Exception var5) {
         ExceptionLogger.logException(var5);
         return 0L;
      }
   }

   private void loadZombieTest() {
      long var1 = javafmod.FMOD_Studio_System_LoadBankFile("media/sound/banks/chopper.bank");
      long var3 = javafmod.FMOD_Studio_System_LoadBankFile("media/sound/banks/chopper.strings.bank");
      javafmod.FMOD_Studio_LoadSampleData(var1);
      long var5 = javafmod.FMOD_Studio_System_GetEvent("{5deff1b6-984c-42e0-98ec-c133a83d0513}");
      long var7 = javafmod.FMOD_Studio_System_GetEvent("{c00fed39-230a-4c6a-b9c0-b0924876f53a}");
      javafmod.FMOD_Studio_LoadEventSampleData(var5);
      javafmod.FMOD_Studio_LoadEventSampleData(var7);
      short var9 = 2000;
      short var10 = 2000;
      SoundListener var11 = new SoundListener(0);
      var11.x = (float)var9;
      var11.y = (float)var10;
      var11.tick();
      boolean var12 = false;
      ArrayList var13 = new ArrayList();
      ArrayList var14 = new ArrayList();
      FMODManager.TestZombieInfo var15 = new FMODManager.TestZombieInfo(var5, (float)(var9 - 5), (float)(var10 - 5));
      javafmod.FMOD_Studio_EventInstance_SetParameterByName(var15.inst, "Pitch", (float)Rand.Next(200) / 100.0F - 1.0F);
      javafmod.FMOD_Studio_EventInstance_SetParameterByName(var15.inst, "Aggitation", 0.0F);
      var13.add(var15);
      ++this.c;

      while(!var12) {
         int var16;
         FMODManager.TestZombieInfo var17;
         for(var16 = 0; var16 < var13.size(); ++var16) {
            var17 = (FMODManager.TestZombieInfo)var13.get(var16);
            if (Rand.Next(1000) == 0) {
               --this.c;
               var14.add(var17);
               var13.remove(var17);
               float var18 = (float)(Rand.Next(40) + 60) / 100.0F;
               javafmod.FMOD_Studio_EventInstance_SetParameterByName(var17.inst, "Aggitation", var18);
               --var16;
            }
         }

         for(var16 = 0; var16 < var14.size(); ++var16) {
            var17 = (FMODManager.TestZombieInfo)var14.get(var16);
            Vector2 var20 = new Vector2((float)var9 - var17.X, (float)var10 - var17.Y);
            if (var20.getLength() < 2.0F) {
               var14.remove(var17);
               javafmod.FMOD_Studio_EventInstance_Stop(var17.inst, false);
            }

            var20.setLength(0.01F);
            var17.X += var20.x;
            var17.Y += var20.y;
            javafmod.FMOD_Studio_EventInstance3D(var17.inst, var17.X, var17.Y, 0.0F);
         }

         javafmod.FMOD_System_Update();

         try {
            Thread.sleep(10L);
         } catch (InterruptedException var19) {
            var19.printStackTrace();
         }
      }

   }

   private void loadTestEvent() {
      long var1 = javafmod.FMOD_Studio_System_LoadBankFile("media/sound/banks/chopper.bank");
      javafmod.FMOD_Studio_LoadSampleData(var1);
      long var3 = javafmod.FMOD_Studio_System_GetEvent("{47d0c496-7d0a-48e9-9bad-1c8fcf292986}");
      javafmod.FMOD_Studio_LoadEventSampleData(var3);
      long var5 = javafmod.FMOD_Studio_System_CreateEventInstance(var3);
      javafmod.FMOD_Studio_EventInstance3D(var5, this.pos.x, this.pos.y, 100.0F);
      javafmod.FMOD_Studio_Listener3D(0, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -1.0F, 0.0F, 0.0F, 0.0F, 1.0F);
      javafmod.FMOD_Studio_StartEvent(var5);
      int var7 = 0;
      boolean var8 = false;

      while(!var8) {
         if (var7 > 200) {
            this.pos.x = (float)(Rand.Next(400) - 200);
            this.pos.y = (float)(Rand.Next(400) - 200);
            if (Rand.Next(3) == 0) {
               Vector2 var10000 = this.pos;
               var10000.x /= 4.0F;
               var10000 = this.pos;
               var10000.y /= 4.0F;
            }

            javafmod.FMOD_Studio_StartEvent(var5);
            javafmod.FMOD_Studio_EventInstance3D(var5, this.pos.x, this.pos.y, 0.0F);
            var7 = 0;
         }

         ++var7;
         javafmod.FMOD_System_Update();

         try {
            Thread.sleep(10L);
         } catch (InterruptedException var10) {
            var10.printStackTrace();
         }
      }

   }

   public void loadTest() {
      long var1 = javafmod.FMOD_System_CreateSound("media/sound/PZ_FemaleBeingEaten_Death.wav", (long)FMOD_3D);
      javafmod.FMOD_Sound_Set3DMinMaxDistance(var1, 0.005F, 100.0F);
      this.Sounds.add(var1);
      this.playTest();
   }

   public void playTest() {
      long var1 = (Long)this.Sounds.get(0);
      long var3 = javafmod.FMOD_System_PlaySound(var1, true);
      javafmod.FMOD_Channel_Set3DAttributes(var3, 10.0F, 10.0F, 0.0F, 0.0F, 0.0F, 0.0F);
      javafmod.FMOD_Channel_SetPaused(var3, false);
      this.Instances.add(var3);
   }

   public void applyDSP() {
      javafmod.FMOD_System_PlayDSP();
   }

   public void tick() {
      if (Rand.Next(100) == 0) {
         this.vx = -this.vx;
      }

      if (Rand.Next(100) == 0) {
         this.vy = -this.vy;
      }

      float var1 = this.x;
      float var2 = this.y;
      float var3 = this.z;
      this.x += this.vx;
      this.y += this.vy;
      this.z += this.vz;

      int var4;
      for(var4 = 0; var4 < this.Instances.size(); ++var4) {
         long var5 = (Long)this.Instances.get(var4);
         javafmod.FMOD_Channel_Set3DAttributes(var5, this.x, this.y, this.z, this.x - var1, this.y - var2, this.z - var3);
         float var7 = 40.0F;
         float var8 = (Math.abs(this.x) + Math.abs(this.y)) / var7;
         if (var8 < 0.1F) {
            var8 = 0.1F;
         }

         if (var8 > 1.0F) {
            var8 = 1.0F;
         }

         var8 *= var8;
         var8 *= 40.0F;
         javafmod.FMOD_Channel_SetReverbProperties(var5, 0, var8);
      }

      var4 = 0;

      for(int var9 = 0; var9 < IsoPlayer.numPlayers; ++var9) {
         IsoPlayer var6 = IsoPlayer.players[var9];
         if (var6 != null) {
            ++var4;
         }
      }

      if (var4 > 0) {
         if (var4 != this.numListeners) {
            javafmod.FMOD_Studio_SetNumListeners(var4);
         }
      } else if (this.numListeners != 1) {
         javafmod.FMOD_Studio_SetNumListeners(1);
      }

      this.numListeners = var4;
      this.updateReverbPreset();
      javafmod.FMOD_System_Update();
   }

   public int getNumListeners() {
      return this.numListeners;
   }

   public long loadSound(String var1) {
      var1 = ZomboidFileSystem.instance.getAbsolutePath(var1);
      if (var1 == null) {
         return 0L;
      } else {
         Long var2 = (Long)this.fileToSoundMap.get(var1);
         if (var2 != null) {
            return var2;
         } else {
            var2 = javafmod.FMOD_System_CreateSound(var1, (long)FMOD_3D);
            if (Core.bDebug && var2 == 0L) {
               DebugLog.log("ERROR: failed to load sound " + var1);
            }

            this.fileToSoundMap.put(var1, var2);
            return var2;
         }
      }
   }

   public void stopSound(long var1) {
      if (var1 != 0L) {
         javafmod.FMOD_Channel_Stop(var1);
      }
   }

   public boolean isPlaying(long var1) {
      return javafmod.FMOD_Channel_IsPlaying(var1);
   }

   public long loadSound(String var1, boolean var2) {
      var1 = ZomboidFileSystem.instance.getAbsolutePath(var1);
      if (var1 == null) {
         return 0L;
      } else {
         Long var3 = (Long)this.fileToSoundMap.get(var1);
         if (var3 != null) {
            return var3;
         } else {
            if (!var2) {
               var3 = javafmod.FMOD_System_CreateSound(var1, (long)FMOD_3D);
            } else {
               var3 = javafmod.FMOD_System_CreateSound(var1, (long)(FMOD_3D | FMOD_LOOP_NORMAL));
            }

            this.fileToSoundMap.put(var1, var3);
            return var3;
         }
      }
   }

   public void updateReverbPreset() {
      boolean var1 = true;
      boolean var2 = true;
      boolean var3 = true;
      int var5 = FMOD_PRESET_FOREST;
      int var6 = FMOD_PRESET_CITY;
      int var4 = FMOD_PRESET_OFF;
      var5 = FMOD_PRESET_OFF;
      var6 = FMOD_PRESET_OFF;
      if (this.reverbPreset[0] != var4) {
         javafmod.FMOD_System_SetReverbDefault(0, var4);
         this.reverbPreset[0] = var4;
      }

      if (this.reverbPreset[1] != var5) {
         javafmod.FMOD_System_SetReverbDefault(1, var5);
         this.reverbPreset[1] = var5;
      }

      if (this.reverbPreset[2] != var6) {
         javafmod.FMOD_System_SetReverbDefault(2, var6);
         this.reverbPreset[2] = var6;
      }

   }

   private void initGlobalParameters() {
      int var1 = javafmodJNI.FMOD_Studio_System_GetParameterDescriptionCount();
      if (var1 > 0) {
         long[] var2 = new long[var1];
         var1 = javafmodJNI.FMOD_Studio_System_GetParameterDescriptionList(var2);

         for(int var3 = 0; var3 < var1; ++var3) {
            long var4 = var2[var3];
            this.initParameterDescription(var4);
            javafmodJNI.FMOD_Studio_ParameterDescription_Free(var4);
         }

      }
   }

   private FMOD_STUDIO_PARAMETER_DESCRIPTION initParameterDescription(long var1) {
      String var3 = javafmodJNI.FMOD_Studio_ParameterDescription_GetName(var1);
      FMOD_STUDIO_PARAMETER_DESCRIPTION var4 = (FMOD_STUDIO_PARAMETER_DESCRIPTION)this.parameterDescriptionMap.get(var3);
      if (var4 == null) {
         int var5 = javafmodJNI.FMOD_Studio_ParameterDescription_GetFlags(var1);
         long var6 = javafmodJNI.FMOD_Studio_ParameterDescription_GetID(var1);
         FMOD_STUDIO_PARAMETER_ID var8 = new FMOD_STUDIO_PARAMETER_ID(var6);
         var4 = new FMOD_STUDIO_PARAMETER_DESCRIPTION(var3, var8, var5, this.parameterDescriptionMap.size());
         this.parameterDescriptionMap.put(var4.name, var4);
         boolean var9;
         if ((var4.flags & FMOD_STUDIO_PARAMETER_FLAGS.FMOD_STUDIO_PARAMETER_GLOBAL.bit) != 0) {
            var9 = true;
         } else {
            var9 = true;
         }
      }

      return var4;
   }

   private void initEvents() {
      int var1 = javafmodJNI.FMOD_Studio_System_GetBankCount();
      if (var1 > 0) {
         long[] var2 = new long[var1];
         var1 = javafmodJNI.FMOD_Studio_System_GetBankList(var2);

         for(int var3 = 0; var3 < var1; ++var3) {
            int var4 = javafmodJNI.FMOD_Studio_Bank_GetEventCount(var2[var3]);
            if (var4 > 0) {
               long[] var5 = new long[var4];
               var4 = javafmodJNI.FMOD_Studio_Bank_GetEventList(var2[var3], var5);

               for(int var6 = 0; var6 < var4; ++var6) {
                  this.initEvent(var5[var6]);
               }
            }
         }

      }
   }

   private void initEvent(long var1) {
      String var3 = javafmodJNI.FMOD_Studio_EventDescription_GetPath(var1);
      long var4 = javafmodJNI.FMOD_Studio_EventDescription_GetID(var1);
      FMOD_GUID var6 = new FMOD_GUID(var4);
      boolean var7 = javafmodJNI.FMOD_Studio_EventDescription_HasCue(var1);
      long var8 = javafmodJNI.FMOD_Studio_EventDescription_GetLength(var1);
      FMOD_STUDIO_EVENT_DESCRIPTION var10 = new FMOD_STUDIO_EVENT_DESCRIPTION(var1, var3, var6, var7, var8);
      this.eventDescriptionMap.put(var10.path.toLowerCase(Locale.ENGLISH), var10);
      int var11 = javafmodJNI.FMOD_Studio_EventDescription_GetParameterDescriptionCount(var1);

      for(int var12 = 0; var12 < var11; ++var12) {
         long var13 = javafmodJNI.FMOD_Studio_EventDescription_GetParameterDescriptionByIndex(var1, var12);
         FMOD_STUDIO_PARAMETER_DESCRIPTION var15 = this.initParameterDescription(var13);
         var10.parameters.add(var15);
         javafmodJNI.FMOD_Studio_ParameterDescription_Free(var13);
      }

   }

   public FMOD_STUDIO_EVENT_DESCRIPTION getEventDescription(String var1) {
      return (FMOD_STUDIO_EVENT_DESCRIPTION)this.eventDescriptionMap.get(var1.toLowerCase(Locale.ENGLISH));
   }

   public FMOD_STUDIO_PARAMETER_DESCRIPTION getParameterDescription(String var1) {
      return (FMOD_STUDIO_PARAMETER_DESCRIPTION)this.parameterDescriptionMap.get(var1);
   }

   public FMOD_STUDIO_PARAMETER_ID getParameterID(String var1) {
      FMOD_STUDIO_PARAMETER_DESCRIPTION var2 = this.getParameterDescription(var1);
      return var2 == null ? null : var2.id;
   }

   public int getParameterCount() {
      return this.parameterDescriptionMap.size();
   }

   public static class TestZombieInfo {
      public float X;
      public float Y;
      public long event;
      public long inst;

      public TestZombieInfo(long var1, float var3, float var4) {
         this.createZombieInstance(var1, var3, var4);
      }

      public long createZombieInstance(long var1, float var3, float var4) {
         long var5 = javafmod.FMOD_Studio_System_CreateEventInstance(var1);
         javafmod.FMOD_Studio_EventInstance3D(var5, var3, var4, 0.0F);
         javafmod.FMOD_Studio_StartEvent(var5);
         this.X = var3;
         this.Y = var4;
         this.event = var1;
         this.inst = var5;
         return var5;
      }
   }
}

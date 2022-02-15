package fmod;

import fmod.fmod.FMOD_STUDIO_EVENT_CALLBACK;
import java.io.File;
import java.math.BigInteger;
import zombie.debug.DebugLog;

public class javafmodJNI {
   public static void init() {
      DebugLog.log("[javafmodJNI] Init: Start");
      String var0 = "";
      if ("1".equals(System.getProperty("zomboid.debuglibs.fmod"))) {
         DebugLog.log("***** Loading debug version of fmodintegration");
         var0 = "d";
      }

      try {
         if (System.getProperty("os.name").contains("OS X")) {
            System.loadLibrary("fmod");
            System.loadLibrary("fmodstudio");
            System.loadLibrary("fmodintegration");
         } else if (System.getProperty("os.name").startsWith("Win")) {
            if (System.getProperty("sun.arch.data.model").equals("64")) {
               DebugLog.log("[javafmodJNI] Init: WIN 64");
               System.loadLibrary("fmod");
               System.loadLibrary("fmodstudio");
               System.loadLibrary("fmodintegration64" + var0);
            } else {
               System.loadLibrary("fmod");
               System.loadLibrary("fmodstudio");
               System.loadLibrary("fmodintegration32");
            }
         } else {
            loadLibrary("libfmod.so.12.7");
            loadLibrary("libfmodstudio.so.12.7");
            if (System.getProperty("sun.arch.data.model").equals("64")) {
               System.loadLibrary("fmodintegration64");
            } else {
               System.loadLibrary("fmodintegration32");
            }
         }

      } catch (UnsatisfiedLinkError var2) {
         System.out.println("Failed to load fmodintegration library");
         var2.printStackTrace();
         throw new UnsatisfiedLinkError("Can't load native libraries");
      }
   }

   private static void loadLibrary(String var0) {
      String[] var1 = System.getProperty("java.library.path", "").split(File.pathSeparator);
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         String var4 = var1[var3];
         File var5 = new File(var4 + "/" + var0);
         if (var5.exists()) {
            System.load(var5.getAbsolutePath());
            break;
         }
      }

   }

   private static void logPutsCallback(String var0) {
      long var1 = System.currentTimeMillis();
      System.out.print(var1 + " " + var0);
   }

   public static final native long FMOD_Studio_Create();

   public static final native long FMOD_Studio_GetEvent(String var0);

   public static final native long FMOD_Studio_CreateEventInstance(long var0);

   public static final native long FMOD_Studio_LoadBankFile(String var0);

   public static final native void FMOD_Studio_StartEvent(long var0);

   public static final native long FMOD_Studio_GetTimelinePosition(long var0);

   public static final native int FMOD_Studio_LoadSampleData(long var0);

   public static final native int FMOD_Studio_LoadEventSampleData(long var0);

   public static final native int FMOD_Memory_Initialize(long var0, int var2, long var3, long var5, long var7, long var9);

   public static final native int FMOD_Memory_GetStats(long var0, long var2, long var4);

   public static final native int FMOD_Debug_Initialize(long var0, long var2, long var4, String var6);

   public static final native int FMOD_File_SetDiskBusy(int var0);

   public static final native int FMOD_File_GetDiskBusy(long var0);

   public static final native int FMOD_System_Create();

   public static final native int FMOD_System_Release(long var0);

   public static final native int FMOD_System_SetOutput(long var0, long var2);

   public static final native int FMOD_System_GetOutput(long var0, long var2);

   public static final native int FMOD_System_GetNumDrivers(long var0, long var2);

   public static final native int FMOD_System_GetDriverInfo(long var0, int var2, String var3, int var4, long var5, long var7, long var9, long var11);

   public static final native int FMOD_System_SetDriver(long var0, int var2);

   public static final native int FMOD_System_GetDriver(long var0, long var2);

   public static final native int FMOD_System_SetSoftwareChannels(long var0, int var2);

   public static final native int FMOD_System_GetSoftwareChannels(long var0, long var2);

   public static final native int FMOD_System_SetSoftwareFormat(long var0, int var2, long var3, int var5);

   public static final native int FMOD_System_GetSoftwareFormat(long var0, long var2, long var4, long var6);

   public static final native int FMOD_System_SetDSPBufferSize(long var0, long var2, int var4);

   public static final native int FMOD_System_GetDSPBufferSize(long var0, long var2, long var4);

   public static final native int FMOD_System_SetFileSystem(long var0, long var2, long var4, long var6, long var8, long var10, long var12, int var14);

   public static final native int FMOD_System_AttachFileSystem(long var0, long var2, long var4, long var6, long var8);

   public static final native int FMOD_System_SetAdvancedSettings(long var0, long var2);

   public static final native int FMOD_System_GetAdvancedSettings(long var0, long var2);

   public static final native int FMOD_System_SetCallback(long var0, long var2, long var4);

   public static final native int FMOD_System_SetPluginPath(long var0, String var2);

   public static final native int FMOD_System_LoadPlugin(long var0, String var2, long var3, long var5);

   public static final native int FMOD_System_UnloadPlugin(long var0, long var2);

   public static final native int FMOD_System_GetNumPlugins(long var0, long var2, long var4);

   public static final native int FMOD_System_GetPluginHandle(long var0, long var2, int var4, long var5);

   public static final native int FMOD_System_GetPluginInfo(long var0, long var2, long var4, String var6, int var7, long var8);

   public static final native int FMOD_System_SetOutputByPlugin(long var0, long var2);

   public static final native int FMOD_System_GetOutputByPlugin(long var0, long var2);

   public static final native int FMOD_System_CreateDSPByPlugin(long var0, long var2, long var4);

   public static final native int FMOD_System_GetDSPInfoByPlugin(long var0, long var2, long var4);

   public static final native int FMOD_System_RegisterCodec(long var0, long var2, long var4, long var6);

   public static final native int FMOD_System_RegisterDSP(long var0, long var2, long var4);

   public static final native int FMOD_System_Init(int var0, long var1, long var3);

   public static final native int FMOD_System_Close(long var0);

   public static final native int FMOD_System_Update();

   public static final native int FMOD_System_SetSpeakerPosition(long var0, long var2, float var4, float var5, long var6);

   public static final native int FMOD_System_GetSpeakerPosition(long var0, long var2, long var4, long var6, long var8);

   public static final native int FMOD_System_SetStreamBufferSize(long var0, long var2, long var4);

   public static final native int FMOD_System_GetStreamBufferSize(long var0, long var2, long var4);

   public static final native int FMOD_System_Set3DSettings(float var0, float var1, float var2);

   public static final native int FMOD_System_Get3DSettings(long var0, long var2, long var4);

   public static final native int FMOD_System_Set3DNumListeners(int var0);

   public static final native int FMOD_System_Get3DNumListeners(long var0, long var2);

   public static final native int FMOD_System_Set3DListenerAttributes(int var0, float var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8, float var9, float var10, float var11, float var12);

   public static final native int FMOD_System_Get3DListenerAttributes(long var0, int var2, long var3, long var5, long var7, long var9);

   public static final native int FMOD_System_Set3DRolloffCallback(long var0, long var2);

   public static final native int FMOD_System_MixerSuspend(long var0);

   public static final native int FMOD_System_MixerResume(long var0);

   public static final native int FMOD_System_GetVersion(long var0, long var2);

   public static final native int FMOD_System_GetOutputHandle(long var0, long var2);

   public static final native int FMOD_System_GetChannelsPlaying(long var0, long var2);

   public static final native int FMOD_System_GetCPUUsage(long var0, long var2, long var4, long var6, long var8, long var10);

   public static final native long FMOD_System_CreateSound(String var0, long var1);

   public static final native long FMOD_System_CreateRecordSound(long var0, long var2, long var4, long var6);

   public static final native long FMOD_System_SetVADMode(int var0);

   public static final native int FMOD_System_SetRecordVolume(int var0);

   public static final native long FMOD_System_CreateRAWPlaySound(long var0, long var2, long var4);

   public static final native long FMOD_System_SetRawPlayBufferingPeriod(long var0);

   public static final native int FMOD_System_RAWPlayData(long var0, short[] var2, long var3);

   public static final native int FMOD_System_CreateStream(long var0, String var2, long var3, long var5, long var7);

   public static final native int FMOD_System_CreateDSP(long var0, long var2, long var4);

   public static final native int FMOD_System_CreateDSPByType(long var0, long var2, long var4);

   public static final native long FMOD_System_CreateChannelGroup(String var0);

   public static final native int FMOD_System_CreateSoundGroup(long var0, String var2, long var3);

   public static final native int FMOD_System_CreateReverb3D(long var0, long var2);

   public static final native long FMOD_System_PlaySound(long var0, long var2);

   public static final native int FMOD_System_PlayDSP();

   public static final native int FMOD_System_GetChannel(long var0, int var2, long var3);

   public static final native long FMOD_System_GetMasterChannelGroup();

   public static final native int FMOD_System_GetMasterSoundGroup(long var0, long var2);

   public static final native int FMOD_System_AttachChannelGroupToPort(long var0, long var2, long var4, long var6, long var8);

   public static final native int FMOD_System_DetachChannelGroupFromPort(long var0, long var2);

   public static final native int FMOD_System_SetReverbProperties(long var0, int var2, long var3);

   public static final native int FMOD_System_GetReverbProperties(long var0, int var2, long var3);

   public static final native int FMOD_System_LockDSP(long var0);

   public static final native int FMOD_System_UnlockDSP(long var0);

   public static final native int FMOD_System_GetRecordNumDrivers();

   public static final native int FMOD_System_GetRecordDriverInfo(int var0, FMOD_DriverInfo var1);

   public static final native int FMOD_System_GetRecordPosition(int var0, Long var1);

   public static final native int FMOD_System_RecordStart(int var0, long var1, boolean var3);

   public static final native int FMOD_System_RecordStop(int var0);

   public static final native int FMOD_System_IsRecording(long var0, int var2, long var3);

   public static final native int FMOD_System_CreateGeometry(long var0, int var2, int var3, long var4);

   public static final native int FMOD_System_SetGeometrySettings(long var0, float var2);

   public static final native int FMOD_System_GetGeometrySettings(long var0, long var2);

   public static final native int FMOD_System_LoadGeometry(long var0, long var2, int var4, long var5);

   public static final native int FMOD_System_GetGeometryOcclusion(long var0, long var2, long var4, long var6, long var8);

   public static final native int FMOD_System_SetNetworkProxy(long var0, String var2);

   public static final native int FMOD_System_GetNetworkProxy(long var0, String var2, int var3);

   public static final native int FMOD_System_SetNetworkTimeout(long var0, int var2);

   public static final native int FMOD_System_GetNetworkTimeout(long var0, long var2);

   public static final native int FMOD_System_SetUserData(long var0, long var2);

   public static final native int FMOD_System_GetUserData(long var0, long var2);

   public static final native int FMOD_Sound_Release(long var0);

   public static final native int FMOD_RAWPlaySound_Release(long var0);

   public static final native int FMOD_RecordSound_Release(long var0);

   public static final native int FMOD_Sound_GetSystemObject(long var0, long var2);

   public static final native int FMOD_Sound_Lock(long var0, long var2, long var4, byte[] var6, byte[] var7, Long var8, Long var9, long[] var10);

   public static final native int FMOD_Sound_GetData(long var0, byte[] var2, Long var3, Long var4, Long var5);

   public static final native int FMOD_Sound_Unlock(long var0, long[] var2);

   public static final native int FMOD_Sound_SetDefaults(long var0, float var2, int var3);

   public static final native int FMOD_Sound_GetDefaults(long var0, long var2, long var4);

   public static final native int FMOD_Sound_Set3DMinMaxDistance(long var0, float var2, float var3);

   public static final native int FMOD_Sound_Get3DMinMaxDistance(long var0, long var2, long var4);

   public static final native int FMOD_Sound_Set3DConeSettings(long var0, float var2, float var3, float var4);

   public static final native int FMOD_Sound_Get3DConeSettings(long var0, long var2, long var4, long var6);

   public static final native int FMOD_Sound_Set3DCustomRolloff(long var0, long var2, int var4);

   public static final native int FMOD_Sound_Get3DCustomRolloff(long var0, long var2, long var4);

   public static final native int FMOD_Sound_SetSubSound(long var0, int var2, long var3);

   public static final native int FMOD_Sound_GetSubSound(long var0, int var2, long var3);

   public static final native int FMOD_Sound_GetSubSoundParent(long var0, long var2);

   public static final native int FMOD_Sound_GetName(long var0, String var2, int var3);

   public static final native int FMOD_Sound_GetLength(long var0, long var2);

   public static final native int FMOD_Sound_GetFormat(long var0, long var2, long var4, long var6, long var8);

   public static final native int FMOD_Sound_GetNumSubSounds(long var0, long var2);

   public static final native int FMOD_Sound_GetNumTags(long var0, long var2, long var4);

   public static final native int FMOD_Sound_GetTag(long var0, String var2, int var3, long var4);

   public static final native int FMOD_Sound_GetOpenState(long var0, long var2, long var4, long var6, long var8);

   public static final native int FMOD_Sound_ReadData(long var0, long var2, long var4, long var6);

   public static final native int FMOD_Sound_SeekData(long var0, long var2);

   public static final native int FMOD_Sound_SetSoundGroup(long var0, long var2);

   public static final native int FMOD_Sound_GetSoundGroup(long var0, long var2);

   public static final native int FMOD_Sound_GetNumSyncPoints(long var0, long var2);

   public static final native int FMOD_Sound_GetSyncPoint(long var0, int var2, long var3);

   public static final native int FMOD_Sound_GetSyncPointInfo(long var0, long var2, String var4, int var5, long var6, long var8);

   public static final native int FMOD_Sound_AddSyncPoint(long var0, long var2, long var4, String var6, long var7);

   public static final native int FMOD_Sound_DeleteSyncPoint(long var0, long var2);

   public static final native int FMOD_Sound_SetMode(long var0, long var2);

   public static final native int FMOD_Sound_GetMode(long var0, long var2);

   public static final native int FMOD_Sound_SetLoopCount(long var0, int var2);

   public static final native int FMOD_Sound_GetLoopCount(long var0, long var2);

   public static final native int FMOD_Sound_SetLoopPoints(long var0, long var2, long var4, long var6, long var8);

   public static final native int FMOD_Sound_GetLoopPoints(long var0, long var2, long var4, long var6, long var8);

   public static final native int FMOD_Sound_GetMusicNumChannels(long var0, long var2);

   public static final native int FMOD_Sound_SetMusicChannelVolume(long var0, int var2, float var3);

   public static final native int FMOD_Sound_GetMusicChannelVolume(long var0, int var2, long var3);

   public static final native int FMOD_Sound_SetMusicSpeed(long var0, float var2);

   public static final native int FMOD_Sound_GetMusicSpeed(long var0, long var2);

   public static final native int FMOD_Sound_SetUserData(long var0, long var2);

   public static final native int FMOD_Sound_GetUserData(long var0, long var2);

   public static final native int FMOD_Channel_GetSystemObject(long var0, long var2);

   public static final native int FMOD_Channel_Stop(long var0);

   public static final native int FMOD_Channel_SetPaused(long var0, long var2);

   public static final native int FMOD_Channel_GetPaused(long var0, long var2);

   public static final native int FMOD_Channel_SetVolume(long var0, float var2);

   public static final native int FMOD_Channel_GetVolume(long var0, long var2);

   public static final native int FMOD_Channel_SetVolumeRamp(long var0, long var2);

   public static final native int FMOD_Channel_GetVolumeRamp(long var0, long var2);

   public static final native float FMOD_Channel_GetAudibility(long var0);

   public static final native int FMOD_Channel_SetPitch(long var0, float var2);

   public static final native int FMOD_Channel_GetPitch(long var0, long var2);

   public static final native int FMOD_Channel_SetMute(long var0, long var2);

   public static final native int FMOD_Channel_GetMute(long var0, long var2);

   public static final native int FMOD_Channel_SetReverbProperties(long var0, int var2, float var3);

   public static final native int FMOD_Channel_GetReverbProperties(long var0, int var2, long var3);

   public static final native int FMOD_Channel_SetLowPassGain(long var0, float var2);

   public static final native int FMOD_Channel_GetLowPassGain(long var0, long var2);

   public static final native int FMOD_Channel_SetMode(long var0, long var2);

   public static final native int FMOD_Channel_GetMode(long var0, long var2);

   public static final native int FMOD_Channel_SetCallback(long var0, long var2);

   public static final native boolean FMOD_Channel_IsPlaying(long var0);

   public static final native int FMOD_Channel_SetPan(long var0, float var2);

   public static final native int FMOD_Channel_SetMixLevelsOutput(long var0, float var2, float var3, float var4, float var5, float var6, float var7, float var8, float var9);

   public static final native int FMOD_Channel_SetMixLevelsInput(long var0, long var2, int var4);

   public static final native int FMOD_Channel_SetMixMatrix(long var0, long var2, int var4, int var5, int var6);

   public static final native int FMOD_Channel_GetMixMatrix(long var0, long var2, long var4, long var6, int var8);

   public static final native int FMOD_Channel_GetDSPClock(long var0, long var2, long var4);

   public static final native int FMOD_Channel_SetDelay(long var0, BigInteger var2, BigInteger var3, long var4);

   public static final native int FMOD_Channel_GetDelay(long var0, long var2, long var4, long var6);

   public static final native int FMOD_Channel_AddFadePoint(long var0, BigInteger var2, float var3);

   public static final native int FMOD_Channel_RemoveFadePoints(long var0, BigInteger var2, BigInteger var3);

   public static final native int FMOD_Channel_GetFadePoints(long var0, long var2, long var4, long var6);

   public static final native int FMOD_Channel_GetDSP(long var0, int var2, long var3);

   public static final native int FMOD_Channel_AddDSP(long var0, int var2, long var3);

   public static final native int FMOD_Channel_RemoveDSP(long var0, long var2);

   public static final native int FMOD_Channel_GetNumDSPs(long var0, long var2);

   public static final native int FMOD_Channel_SetDSPIndex(long var0, long var2, int var4);

   public static final native int FMOD_Channel_GetDSPIndex(long var0, long var2, long var4);

   public static final native int FMOD_Channel_OverridePanDSP(long var0, long var2);

   public static final native int FMOD_Channel_Set3DAttributes(long var0, float var2, float var3, float var4, float var5, float var6, float var7);

   public static final native int FMOD_Channel_Get3DAttributes(long var0, long var2, long var4);

   public static final native int FMOD_Channel_Set3DMinMaxDistance(long var0, float var2, float var3);

   public static final native int FMOD_Channel_Get3DMinMaxDistance(long var0, long var2, long var4);

   public static final native int FMOD_Channel_Set3DConeSettings(long var0, float var2, float var3, float var4);

   public static final native int FMOD_Channel_Get3DConeSettings(long var0, long var2, long var4, long var6);

   public static final native int FMOD_Channel_Set3DConeOrientation(long var0, long var2);

   public static final native int FMOD_Channel_Get3DConeOrientation(long var0, long var2);

   public static final native int FMOD_Channel_Set3DCustomRolloff(long var0, long var2, int var4);

   public static final native int FMOD_Channel_Get3DCustomRolloff(long var0, long var2, long var4);

   public static final native int FMOD_Channel_Set3DOcclusion(long var0, float var2, float var3);

   public static final native int FMOD_Channel_Get3DOcclusion(long var0, long var2, long var4);

   public static final native int FMOD_Channel_Set3DSpread(long var0, float var2);

   public static final native int FMOD_Channel_Get3DSpread(long var0, long var2);

   public static final native int FMOD_Channel_Set3DLevel(long var0, float var2);

   public static final native int FMOD_Channel_Get3DLevel(long var0, long var2);

   public static final native int FMOD_Channel_Set3DDopplerLevel(long var0, float var2);

   public static final native int FMOD_Channel_Get3DDopplerLevel(long var0, long var2);

   public static final native int FMOD_Channel_Set3DDistanceFilter(long var0, long var2, float var4, float var5);

   public static final native int FMOD_Channel_Get3DDistanceFilter(long var0, long var2, long var4, long var6);

   public static final native int FMOD_Channel_SetUserData(long var0, long var2);

   public static final native int FMOD_Channel_GetUserData(long var0, long var2);

   public static final native int FMOD_Channel_SetFrequency(long var0, float var2);

   public static final native int FMOD_Channel_GetFrequency(long var0, long var2);

   public static final native int FMOD_Channel_SetPriority(long var0, int var2);

   public static final native int FMOD_Channel_GetPriority(long var0, long var2);

   public static final native int FMOD_Channel_SetPosition(long var0, long var2);

   public static final native long FMOD_Channel_GetPosition(long var0, long var2);

   public static final native int FMOD_Channel_SetChannelGroup(long var0, long var2);

   public static final native int FMOD_Channel_GetChannelGroup(long var0, long var2);

   public static final native int FMOD_Channel_SetLoopCount(long var0, int var2);

   public static final native int FMOD_Channel_GetLoopCount(long var0, long var2);

   public static final native int FMOD_Channel_SetLoopPoints(long var0, long var2, long var4, long var6, long var8);

   public static final native int FMOD_Channel_GetLoopPoints(long var0, long var2, long var4, long var6, long var8);

   public static final native boolean FMOD_Channel_IsVirtual(long var0);

   public static final native int FMOD_Channel_GetCurrentSound(long var0, long var2);

   public static final native int FMOD_Channel_GetIndex(long var0, long var2);

   public static final native int FMOD_ChannelGroup_GetSystemObject(long var0, long var2);

   public static final native int FMOD_ChannelGroup_Stop(long var0);

   public static final native int FMOD_ChannelGroup_SetPaused(long var0, boolean var2);

   public static final native int FMOD_ChannelGroup_GetPaused(long var0, long var2);

   public static final native int FMOD_ChannelGroup_SetVolume(long var0, float var2);

   public static final native int FMOD_ChannelGroup_GetVolume(long var0, long var2);

   public static final native int FMOD_ChannelGroup_SetVolumeRamp(long var0, long var2);

   public static final native int FMOD_ChannelGroup_GetVolumeRamp(long var0, long var2);

   public static final native int FMOD_ChannelGroup_GetAudibility(long var0, long var2);

   public static final native int FMOD_ChannelGroup_SetPitch(long var0, float var2);

   public static final native int FMOD_ChannelGroup_GetPitch(long var0, long var2);

   public static final native int FMOD_ChannelGroup_SetMute(long var0, boolean var2);

   public static final native int FMOD_ChannelGroup_GetMute(long var0, long var2);

   public static final native int FMOD_ChannelGroup_SetReverbProperties(long var0, int var2, float var3);

   public static final native int FMOD_ChannelGroup_GetReverbProperties(long var0, int var2, long var3);

   public static final native int FMOD_ChannelGroup_SetLowPassGain(long var0, float var2);

   public static final native int FMOD_ChannelGroup_GetLowPassGain(long var0, long var2);

   public static final native int FMOD_ChannelGroup_SetMode(long var0, long var2);

   public static final native int FMOD_ChannelGroup_GetMode(long var0, long var2);

   public static final native int FMOD_ChannelGroup_SetCallback(long var0, long var2);

   public static final native int FMOD_ChannelGroup_IsPlaying(long var0, long var2);

   public static final native int FMOD_ChannelGroup_SetPan(long var0, float var2);

   public static final native int FMOD_ChannelGroup_SetMixLevelsOutput(long var0, float var2, float var3, float var4, float var5, float var6, float var7, float var8, float var9);

   public static final native int FMOD_ChannelGroup_SetMixLevelsInput(long var0, long var2, int var4);

   public static final native int FMOD_ChannelGroup_SetMixMatrix(long var0, long var2, int var4, int var5, int var6);

   public static final native int FMOD_ChannelGroup_GetMixMatrix(long var0, long var2, long var4, long var6, int var8);

   public static final native int FMOD_ChannelGroup_GetDSPClock(long var0, long var2, long var4);

   public static final native int FMOD_ChannelGroup_SetDelay(long var0, BigInteger var2, BigInteger var3, long var4);

   public static final native int FMOD_ChannelGroup_GetDelay(long var0, long var2, long var4, long var6);

   public static final native int FMOD_ChannelGroup_AddFadePoint(long var0, BigInteger var2, float var3);

   public static final native int FMOD_ChannelGroup_RemoveFadePoints(long var0, BigInteger var2, BigInteger var3);

   public static final native int FMOD_ChannelGroup_GetFadePoints(long var0, long var2, long var4, long var6);

   public static final native int FMOD_ChannelGroup_GetDSP(long var0, int var2, long var3);

   public static final native int FMOD_ChannelGroup_AddDSP(long var0, int var2, long var3);

   public static final native int FMOD_ChannelGroup_RemoveDSP(long var0, long var2);

   public static final native int FMOD_ChannelGroup_GetNumDSPs(long var0, long var2);

   public static final native int FMOD_ChannelGroup_SetDSPIndex(long var0, long var2, int var4);

   public static final native int FMOD_ChannelGroup_GetDSPIndex(long var0, long var2, long var4);

   public static final native int FMOD_ChannelGroup_OverridePanDSP(long var0, long var2);

   public static final native int FMOD_ChannelGroup_Set3DAttributes(long var0, long var2, long var4);

   public static final native int FMOD_ChannelGroup_Get3DAttributes(long var0, long var2, long var4);

   public static final native int FMOD_ChannelGroup_Set3DMinMaxDistance(long var0, float var2, float var3);

   public static final native int FMOD_ChannelGroup_Get3DMinMaxDistance(long var0, long var2, long var4);

   public static final native int FMOD_ChannelGroup_Set3DConeSettings(long var0, float var2, float var3, float var4);

   public static final native int FMOD_ChannelGroup_Get3DConeSettings(long var0, long var2, long var4, long var6);

   public static final native int FMOD_ChannelGroup_Set3DConeOrientation(long var0, long var2);

   public static final native int FMOD_ChannelGroup_Get3DConeOrientation(long var0, long var2);

   public static final native int FMOD_ChannelGroup_Set3DCustomRolloff(long var0, long var2, int var4);

   public static final native int FMOD_ChannelGroup_Get3DCustomRolloff(long var0, long var2, long var4);

   public static final native int FMOD_ChannelGroup_Set3DOcclusion(long var0, float var2, float var3);

   public static final native int FMOD_ChannelGroup_Get3DOcclusion(long var0, long var2, long var4);

   public static final native int FMOD_ChannelGroup_Set3DSpread(long var0, float var2);

   public static final native int FMOD_ChannelGroup_Get3DSpread(long var0, long var2);

   public static final native int FMOD_ChannelGroup_Set3DLevel(long var0, float var2);

   public static final native int FMOD_ChannelGroup_Get3DLevel(long var0, long var2);

   public static final native int FMOD_ChannelGroup_Set3DDopplerLevel(long var0, float var2);

   public static final native int FMOD_ChannelGroup_Get3DDopplerLevel(long var0, long var2);

   public static final native int FMOD_ChannelGroup_Set3DDistanceFilter(long var0, long var2, float var4, float var5);

   public static final native int FMOD_ChannelGroup_Get3DDistanceFilter(long var0, long var2, long var4, long var6);

   public static final native int FMOD_ChannelGroup_SetUserData(long var0, long var2);

   public static final native int FMOD_ChannelGroup_GetUserData(long var0, long var2);

   public static final native int FMOD_ChannelGroup_Release(long var0);

   public static final native int FMOD_ChannelGroup_AddGroup(long var0, long var2, long var4, long var6);

   public static final native int FMOD_ChannelGroup_GetNumGroups(long var0, long var2);

   public static final native int FMOD_ChannelGroup_GetGroup(long var0, int var2, long var3);

   public static final native int FMOD_ChannelGroup_GetParentGroup(long var0, long var2);

   public static final native int FMOD_ChannelGroup_GetName(long var0, String var2, int var3);

   public static final native int FMOD_ChannelGroup_GetNumChannels(long var0, long var2);

   public static final native int FMOD_ChannelGroup_GetChannel(long var0, int var2, long var3);

   public static final native int FMOD_SoundGroup_Release(long var0);

   public static final native int FMOD_SoundGroup_GetSystemObject(long var0, long var2);

   public static final native int FMOD_SoundGroup_SetMaxAudible(long var0, int var2);

   public static final native int FMOD_SoundGroup_GetMaxAudible(long var0, long var2);

   public static final native int FMOD_SoundGroup_SetMaxAudibleBehavior(long var0, long var2);

   public static final native int FMOD_SoundGroup_GetMaxAudibleBehavior(long var0, long var2);

   public static final native int FMOD_SoundGroup_SetMuteFadeSpeed(long var0, float var2);

   public static final native int FMOD_SoundGroup_GetMuteFadeSpeed(long var0, long var2);

   public static final native int FMOD_SoundGroup_SetVolume(long var0, float var2);

   public static final native int FMOD_SoundGroup_GetVolume(long var0, long var2);

   public static final native int FMOD_SoundGroup_Stop(long var0);

   public static final native int FMOD_SoundGroup_GetName(long var0, String var2, int var3);

   public static final native int FMOD_SoundGroup_GetNumSounds(long var0, long var2);

   public static final native int FMOD_SoundGroup_GetSound(long var0, int var2, long var3);

   public static final native int FMOD_SoundGroup_GetNumPlaying(long var0, long var2);

   public static final native int FMOD_SoundGroup_SetUserData(long var0, long var2);

   public static final native int FMOD_SoundGroup_GetUserData(long var0, long var2);

   public static final native int FMOD_DSP_Release(long var0);

   public static final native int FMOD_DSP_GetSystemObject(long var0, long var2);

   public static final native int FMOD_DSP_AddInput(long var0, long var2, long var4, long var6);

   public static final native int FMOD_DSP_DisconnectFrom(long var0, long var2, long var4);

   public static final native int FMOD_DSP_DisconnectAll(long var0, long var2, long var4);

   public static final native int FMOD_DSP_GetNumInputs(long var0, long var2);

   public static final native int FMOD_DSP_GetNumOutputs(long var0, long var2);

   public static final native int FMOD_DSP_GetInput(long var0, int var2, long var3, long var5);

   public static final native int FMOD_DSP_GetOutput(long var0, int var2, long var3, long var5);

   public static final native int FMOD_DSP_SetActive(long var0, long var2);

   public static final native int FMOD_DSP_GetActive(long var0, long var2);

   public static final native int FMOD_DSP_SetBypass(long var0, long var2);

   public static final native int FMOD_DSP_GetBypass(long var0, long var2);

   public static final native int FMOD_DSP_SetWetDryMix(long var0, float var2, float var3, float var4);

   public static final native int FMOD_DSP_GetWetDryMix(long var0, long var2, long var4, long var6);

   public static final native int FMOD_DSP_SetChannelFormat(long var0, long var2, int var4, long var5);

   public static final native int FMOD_DSP_GetChannelFormat(long var0, long var2, long var4, long var6);

   public static final native int FMOD_DSP_GetOutputChannelFormat(long var0, long var2, int var4, long var5, long var7, long var9, long var11);

   public static final native int FMOD_DSP_Reset(long var0);

   public static final native int FMOD_DSP_SetParameterFloat(long var0, int var2, float var3);

   public static final native int FMOD_DSP_SetParameterInt(long var0, int var2, int var3);

   public static final native int FMOD_DSP_SetParameterBool(long var0, int var2, long var3);

   public static final native int FMOD_DSP_SetParameterData(long var0, int var2, long var3, long var5);

   public static final native int FMOD_DSP_GetParameterFloat(long var0, int var2, long var3, String var5, int var6);

   public static final native int FMOD_DSP_GetParameterInt(long var0, int var2, long var3, String var5, int var6);

   public static final native int FMOD_DSP_GetParameterBool(long var0, int var2, long var3, String var5, int var6);

   public static final native int FMOD_DSP_GetParameterData(long var0, int var2, long var3, long var5, String var7, int var8);

   public static final native int FMOD_DSP_GetNumParameters(long var0, long var2);

   public static final native int FMOD_DSP_GetParameterInfo(long var0, int var2, long var3);

   public static final native int FMOD_DSP_GetDataParameterIndex(long var0, int var2, long var3);

   public static final native int FMOD_DSP_ShowConfigDialog(long var0, long var2, long var4);

   public static final native int FMOD_DSP_GetInfo(long var0, String var2, long var3, long var5, long var7, long var9);

   public static final native int FMOD_DSP_GetType(long var0, long var2);

   public static final native int FMOD_DSP_GetIdle(long var0, long var2);

   public static final native int FMOD_DSP_SetUserData(long var0, long var2);

   public static final native int FMOD_DSP_GetUserData(long var0, long var2);

   public static final native int FMOD_DSP_SetMeteringEnabled(long var0, long var2, long var4);

   public static final native int FMOD_DSP_GetMeteringEnabled(long var0, long var2, long var4);

   public static final native int FMOD_DSP_GetMeteringInfo(long var0, long var2, long var4);

   public static final native int FMOD_DSPConnection_GetInput(long var0, long var2);

   public static final native int FMOD_DSPConnection_GetOutput(long var0, long var2);

   public static final native int FMOD_DSPConnection_SetMix(long var0, float var2);

   public static final native int FMOD_DSPConnection_GetMix(long var0, long var2);

   public static final native int FMOD_DSPConnection_SetMixMatrix(long var0, long var2, int var4, int var5, int var6);

   public static final native int FMOD_DSPConnection_GetMixMatrix(long var0, long var2, long var4, long var6, int var8);

   public static final native int FMOD_DSPConnection_GetType(long var0, long var2);

   public static final native int FMOD_DSPConnection_SetUserData(long var0, long var2);

   public static final native int FMOD_DSPConnection_GetUserData(long var0, long var2);

   public static final native int FMOD_Geometry_Release(long var0);

   public static final native int FMOD_Geometry_AddPolygon(long var0, float var2, float var3, long var4, int var6, long var7, long var9);

   public static final native int FMOD_Geometry_GetNumPolygons(long var0, long var2);

   public static final native int FMOD_Geometry_GetMaxPolygons(long var0, long var2, long var4);

   public static final native int FMOD_Geometry_GetPolygonNumVertices(long var0, int var2, long var3);

   public static final native int FMOD_Geometry_SetPolygonVertex(long var0, int var2, int var3, long var4);

   public static final native int FMOD_Geometry_GetPolygonVertex(long var0, int var2, int var3, long var4);

   public static final native int FMOD_Geometry_SetPolygonAttributes(long var0, int var2, float var3, float var4, long var5);

   public static final native int FMOD_Geometry_GetPolygonAttributes(long var0, int var2, long var3, long var5, long var7);

   public static final native int FMOD_Geometry_SetActive(long var0, long var2);

   public static final native int FMOD_Geometry_GetActive(long var0, long var2);

   public static final native int FMOD_Geometry_SetRotation(long var0, long var2, long var4);

   public static final native int FMOD_Geometry_GetRotation(long var0, long var2, long var4);

   public static final native int FMOD_Geometry_SetPosition(long var0, long var2);

   public static final native int FMOD_Geometry_GetPosition(long var0, long var2);

   public static final native int FMOD_Geometry_SetScale(long var0, long var2);

   public static final native int FMOD_Geometry_GetScale(long var0, long var2);

   public static final native int FMOD_Geometry_Save(long var0, long var2, long var4);

   public static final native int FMOD_Geometry_SetUserData(long var0, long var2);

   public static final native int FMOD_Geometry_GetUserData(long var0, long var2);

   public static final native int FMOD_Reverb3D_Release(long var0);

   public static final native int FMOD_Reverb3D_Set3DAttributes(long var0, long var2, float var4, float var5);

   public static final native int FMOD_Reverb3D_Get3DAttributes(long var0, long var2, long var4, long var6);

   public static final native int FMOD_Reverb3D_SetProperties(long var0, long var2);

   public static final native int FMOD_Reverb3D_GetProperties(long var0, long var2);

   public static final native int FMOD_Reverb3D_SetActive(long var0, long var2);

   public static final native int FMOD_Reverb3D_GetActive(long var0, long var2);

   public static final native int FMOD_Reverb3D_SetUserData(long var0, long var2);

   public static final native int FMOD_Reverb3D_GetUserData(long var0, long var2);

   public static final native void FMOD_System_SetReverbDefault(int var0, int var1);

   public static final native int FMOD_Studio_EventInstance3D(long var0, float var2, float var3, float var4);

   public static final native int FMOD_Studio_SetNumListeners(int var0);

   public static final native void FMOD_Studio_Listener3D(int var0, float var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8, float var9, float var10, float var11, float var12);

   public static final native int FMOD_Studio_EventInstance_SetParameterByName(long var0, String var2, float var3);

   public static final native float FMOD_Studio_GetParameter(long var0, String var2);

   public static final native int FMOD_Studio_GetPlaybackState(long var0);

   public static final native int FMOD_Studio_EventInstance_SetVolume(long var0, float var2);

   public static final native float FMOD_Studio_EventInstance_GetVolume(long var0);

   public static final native int FMOD_Studio_ReleaseEventInstance(long var0);

   public static final native int FMOD_Studio_EventInstance_Stop(long var0, boolean var2);

   public static final native void FMOD_Studio_System_FlushCommands();

   public static final native int FMOD_Studio_System_GetBankCount();

   public static final native int FMOD_Studio_System_GetBankList(long[] var0);

   public static native long FMOD_Studio_System_GetBus(String var0);

   public static native int FMOD_Studio_System_GetParameterDescriptionCount();

   public static native int FMOD_Studio_System_GetParameterDescriptionList(long[] var0);

   public static native int FMOD_Studio_System_SetParameterByID(long var0, float var2, boolean var3);

   public static native int FMOD_Studio_System_SetParameterByName(String var0, float var1, boolean var2);

   public static final native int FMOD_Studio_Bank_GetEventCount(long var0);

   public static final native int FMOD_Studio_Bank_GetEventList(long var0, long[] var2);

   public static native boolean FMOD_Studio_Bus_GetMute(long var0);

   public static native boolean FMOD_Studio_Bus_GetPaused(long var0);

   public static native float FMOD_Studio_Bus_GetVolume(long var0);

   public static native int FMOD_Studio_Bus_SetMute(long var0, boolean var2);

   public static native int FMOD_Studio_Bus_SetPaused(long var0, boolean var2);

   public static native int FMOD_Studio_Bus_SetVolume(long var0, float var2);

   public static native int FMOD_Studio_Bus_StopAllEvents(long var0, boolean var2);

   public static native long FMOD_Studio_EventDescription_GetID(long var0);

   public static native long FMOD_Studio_EventDescription_GetLength(long var0);

   public static final native int FMOD_Studio_EventDescription_GetInstanceCount(long var0);

   public static final native int FMOD_Studio_EventDescription_GetInstanceList(long var0, long[] var2);

   public static native int FMOD_Studio_EventDescription_GetParameterDescriptionCount(long var0);

   public static native long FMOD_Studio_EventDescription_GetParameterDescriptionByIndex(long var0, int var2);

   public static final native String FMOD_Studio_EventDescription_GetPath(long var0);

   public static native boolean FMOD_Studio_EventDescription_HasCue(long var0);

   public static final native boolean FMOD_Studio_EventInstance_GetPaused(long var0);

   public static native int FMOD_Studio_EventInstance_SetCallback(long var0, FMOD_STUDIO_EVENT_CALLBACK var2, int var3);

   public static final native void FMOD_Studio_EventInstance_SetPaused(long var0, boolean var2);

   public static native int FMOD_Studio_EventInstance_SetParameterByID(long var0, long var2, float var4, boolean var5);

   public static final native void FMOD_Studio_EventInstance_SetProperty(long var0, int var2, float var3);

   public static native int FMOD_Studio_EventInstance_TriggerCue(long var0);

   public static native int FMOD_Studio_EventInstance_SetTimelinePosition(long var0, int var2);

   public static native String FMOD_Studio_ParameterDescription_GetName(long var0);

   public static native long FMOD_Studio_ParameterDescription_GetID(long var0);

   public static native int FMOD_Studio_ParameterDescription_GetFlags(long var0);

   public static native void FMOD_Studio_ParameterDescription_Free(long var0);
}

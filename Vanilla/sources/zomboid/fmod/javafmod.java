package fmod;

import fmod.fmod.FMOD_STUDIO_EVENT_CALLBACK;
import fmod.fmod.FMOD_STUDIO_PARAMETER_ID;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.HashMap;
import zombie.core.Core;
import zombie.debug.DebugLog;

public class javafmod {
   static HashMap map = new HashMap();
   private static int[] reverb = new int[4];

   public static int FMOD_Memory_Initialize(SWIGTYPE_p_void var0, int var1, SWIGTYPE_p_FMOD_MEMORY_ALLOC_CALLBACK var2, SWIGTYPE_p_FMOD_MEMORY_REALLOC_CALLBACK var3, SWIGTYPE_p_FMOD_MEMORY_FREE_CALLBACK var4, SWIGTYPE_p_FMOD_MEMORY_TYPE var5) {
      return javafmodJNI.FMOD_Memory_Initialize(SWIGTYPE_p_void.getCPtr(var0), var1, SWIGTYPE_p_FMOD_MEMORY_ALLOC_CALLBACK.getCPtr(var2), SWIGTYPE_p_FMOD_MEMORY_REALLOC_CALLBACK.getCPtr(var3), SWIGTYPE_p_FMOD_MEMORY_FREE_CALLBACK.getCPtr(var4), SWIGTYPE_p_FMOD_MEMORY_TYPE.getCPtr(var5));
   }

   public static int FMOD_Memory_GetStats(SWIGTYPE_p_int var0, SWIGTYPE_p_int var1, SWIGTYPE_p_FMOD_BOOL var2) {
      return javafmodJNI.FMOD_Memory_GetStats(SWIGTYPE_p_int.getCPtr(var0), SWIGTYPE_p_int.getCPtr(var1), SWIGTYPE_p_FMOD_BOOL.getCPtr(var2));
   }

   public static int FMOD_Debug_Initialize(SWIGTYPE_p_FMOD_DEBUG_FLAGS var0, SWIGTYPE_p_FMOD_DEBUG_MODE var1, SWIGTYPE_p_FMOD_DEBUG_CALLBACK var2, String var3) {
      return javafmodJNI.FMOD_Debug_Initialize(SWIGTYPE_p_FMOD_DEBUG_FLAGS.getCPtr(var0), SWIGTYPE_p_FMOD_DEBUG_MODE.getCPtr(var1), SWIGTYPE_p_FMOD_DEBUG_CALLBACK.getCPtr(var2), var3);
   }

   public static int FMOD_File_SetDiskBusy(int var0) {
      return javafmodJNI.FMOD_File_SetDiskBusy(var0);
   }

   public static int FMOD_File_GetDiskBusy(SWIGTYPE_p_int var0) {
      return javafmodJNI.FMOD_File_GetDiskBusy(SWIGTYPE_p_int.getCPtr(var0));
   }

   public static int FMOD_System_Create() {
      return javafmodJNI.FMOD_System_Create();
   }

   public static int FMOD_System_Release(SWIGTYPE_p_FMOD_SYSTEM var0) {
      return javafmodJNI.FMOD_System_Release(SWIGTYPE_p_FMOD_SYSTEM.getCPtr(var0));
   }

   public static int FMOD_System_SetOutput(SWIGTYPE_p_FMOD_SYSTEM var0, SWIGTYPE_p_FMOD_OUTPUTTYPE var1) {
      return javafmodJNI.FMOD_System_SetOutput(SWIGTYPE_p_FMOD_SYSTEM.getCPtr(var0), SWIGTYPE_p_FMOD_OUTPUTTYPE.getCPtr(var1));
   }

   public static int FMOD_System_GetOutput(SWIGTYPE_p_FMOD_SYSTEM var0, SWIGTYPE_p_FMOD_OUTPUTTYPE var1) {
      return javafmodJNI.FMOD_System_GetOutput(SWIGTYPE_p_FMOD_SYSTEM.getCPtr(var0), SWIGTYPE_p_FMOD_OUTPUTTYPE.getCPtr(var1));
   }

   public static int FMOD_System_GetNumDrivers(SWIGTYPE_p_FMOD_SYSTEM var0, SWIGTYPE_p_int var1) {
      return javafmodJNI.FMOD_System_GetNumDrivers(SWIGTYPE_p_FMOD_SYSTEM.getCPtr(var0), SWIGTYPE_p_int.getCPtr(var1));
   }

   public static int FMOD_System_GetDriverInfo(SWIGTYPE_p_FMOD_SYSTEM var0, int var1, String var2, int var3, SWIGTYPE_p_FMOD_GUID var4, SWIGTYPE_p_int var5, SWIGTYPE_p_FMOD_SPEAKERMODE var6, SWIGTYPE_p_int var7) {
      return javafmodJNI.FMOD_System_GetDriverInfo(SWIGTYPE_p_FMOD_SYSTEM.getCPtr(var0), var1, var2, var3, SWIGTYPE_p_FMOD_GUID.getCPtr(var4), SWIGTYPE_p_int.getCPtr(var5), SWIGTYPE_p_FMOD_SPEAKERMODE.getCPtr(var6), SWIGTYPE_p_int.getCPtr(var7));
   }

   public static int FMOD_System_SetDriver(SWIGTYPE_p_FMOD_SYSTEM var0, int var1) {
      return javafmodJNI.FMOD_System_SetDriver(SWIGTYPE_p_FMOD_SYSTEM.getCPtr(var0), var1);
   }

   public static int FMOD_System_GetDriver(SWIGTYPE_p_FMOD_SYSTEM var0, SWIGTYPE_p_int var1) {
      return javafmodJNI.FMOD_System_GetDriver(SWIGTYPE_p_FMOD_SYSTEM.getCPtr(var0), SWIGTYPE_p_int.getCPtr(var1));
   }

   public static int FMOD_System_SetSoftwareChannels(SWIGTYPE_p_FMOD_SYSTEM var0, int var1) {
      return javafmodJNI.FMOD_System_SetSoftwareChannels(SWIGTYPE_p_FMOD_SYSTEM.getCPtr(var0), var1);
   }

   public static int FMOD_System_GetSoftwareChannels(SWIGTYPE_p_FMOD_SYSTEM var0, SWIGTYPE_p_int var1) {
      return javafmodJNI.FMOD_System_GetSoftwareChannels(SWIGTYPE_p_FMOD_SYSTEM.getCPtr(var0), SWIGTYPE_p_int.getCPtr(var1));
   }

   public static int FMOD_System_SetSoftwareFormat(SWIGTYPE_p_FMOD_SYSTEM var0, int var1, SWIGTYPE_p_FMOD_SPEAKERMODE var2, int var3) {
      return javafmodJNI.FMOD_System_SetSoftwareFormat(SWIGTYPE_p_FMOD_SYSTEM.getCPtr(var0), var1, SWIGTYPE_p_FMOD_SPEAKERMODE.getCPtr(var2), var3);
   }

   public static int FMOD_System_GetSoftwareFormat(SWIGTYPE_p_FMOD_SYSTEM var0, SWIGTYPE_p_int var1, SWIGTYPE_p_FMOD_SPEAKERMODE var2, SWIGTYPE_p_int var3) {
      return javafmodJNI.FMOD_System_GetSoftwareFormat(SWIGTYPE_p_FMOD_SYSTEM.getCPtr(var0), SWIGTYPE_p_int.getCPtr(var1), SWIGTYPE_p_FMOD_SPEAKERMODE.getCPtr(var2), SWIGTYPE_p_int.getCPtr(var3));
   }

   public static int FMOD_System_SetDSPBufferSize(SWIGTYPE_p_FMOD_SYSTEM var0, long var1, int var3) {
      return javafmodJNI.FMOD_System_SetDSPBufferSize(SWIGTYPE_p_FMOD_SYSTEM.getCPtr(var0), var1, var3);
   }

   public static int FMOD_System_GetDSPBufferSize(SWIGTYPE_p_FMOD_SYSTEM var0, SWIGTYPE_p_unsigned_int var1, SWIGTYPE_p_int var2) {
      return javafmodJNI.FMOD_System_GetDSPBufferSize(SWIGTYPE_p_FMOD_SYSTEM.getCPtr(var0), SWIGTYPE_p_unsigned_int.getCPtr(var1), SWIGTYPE_p_int.getCPtr(var2));
   }

   public static int FMOD_System_SetFileSystem(SWIGTYPE_p_FMOD_SYSTEM var0, SWIGTYPE_p_FMOD_FILE_OPEN_CALLBACK var1, SWIGTYPE_p_FMOD_FILE_CLOSE_CALLBACK var2, SWIGTYPE_p_FMOD_FILE_READ_CALLBACK var3, SWIGTYPE_p_FMOD_FILE_SEEK_CALLBACK var4, SWIGTYPE_p_FMOD_FILE_ASYNCREAD_CALLBACK var5, SWIGTYPE_p_FMOD_FILE_ASYNCCANCEL_CALLBACK var6, int var7) {
      return javafmodJNI.FMOD_System_SetFileSystem(SWIGTYPE_p_FMOD_SYSTEM.getCPtr(var0), SWIGTYPE_p_FMOD_FILE_OPEN_CALLBACK.getCPtr(var1), SWIGTYPE_p_FMOD_FILE_CLOSE_CALLBACK.getCPtr(var2), SWIGTYPE_p_FMOD_FILE_READ_CALLBACK.getCPtr(var3), SWIGTYPE_p_FMOD_FILE_SEEK_CALLBACK.getCPtr(var4), SWIGTYPE_p_FMOD_FILE_ASYNCREAD_CALLBACK.getCPtr(var5), SWIGTYPE_p_FMOD_FILE_ASYNCCANCEL_CALLBACK.getCPtr(var6), var7);
   }

   public static int FMOD_System_AttachFileSystem(SWIGTYPE_p_FMOD_SYSTEM var0, SWIGTYPE_p_FMOD_FILE_OPEN_CALLBACK var1, SWIGTYPE_p_FMOD_FILE_CLOSE_CALLBACK var2, SWIGTYPE_p_FMOD_FILE_READ_CALLBACK var3, SWIGTYPE_p_FMOD_FILE_SEEK_CALLBACK var4) {
      return javafmodJNI.FMOD_System_AttachFileSystem(SWIGTYPE_p_FMOD_SYSTEM.getCPtr(var0), SWIGTYPE_p_FMOD_FILE_OPEN_CALLBACK.getCPtr(var1), SWIGTYPE_p_FMOD_FILE_CLOSE_CALLBACK.getCPtr(var2), SWIGTYPE_p_FMOD_FILE_READ_CALLBACK.getCPtr(var3), SWIGTYPE_p_FMOD_FILE_SEEK_CALLBACK.getCPtr(var4));
   }

   public static int FMOD_System_SetAdvancedSettings(SWIGTYPE_p_FMOD_SYSTEM var0, SWIGTYPE_p_FMOD_ADVANCEDSETTINGS var1) {
      return javafmodJNI.FMOD_System_SetAdvancedSettings(SWIGTYPE_p_FMOD_SYSTEM.getCPtr(var0), SWIGTYPE_p_FMOD_ADVANCEDSETTINGS.getCPtr(var1));
   }

   public static int FMOD_System_GetAdvancedSettings(SWIGTYPE_p_FMOD_SYSTEM var0, SWIGTYPE_p_FMOD_ADVANCEDSETTINGS var1) {
      return javafmodJNI.FMOD_System_GetAdvancedSettings(SWIGTYPE_p_FMOD_SYSTEM.getCPtr(var0), SWIGTYPE_p_FMOD_ADVANCEDSETTINGS.getCPtr(var1));
   }

   public static int FMOD_System_SetCallback(SWIGTYPE_p_FMOD_SYSTEM var0, SWIGTYPE_p_FMOD_SYSTEM_CALLBACK var1, SWIGTYPE_p_FMOD_SYSTEM_CALLBACK_TYPE var2) {
      return javafmodJNI.FMOD_System_SetCallback(SWIGTYPE_p_FMOD_SYSTEM.getCPtr(var0), SWIGTYPE_p_FMOD_SYSTEM_CALLBACK.getCPtr(var1), SWIGTYPE_p_FMOD_SYSTEM_CALLBACK_TYPE.getCPtr(var2));
   }

   public static int FMOD_System_SetPluginPath(SWIGTYPE_p_FMOD_SYSTEM var0, String var1) {
      return javafmodJNI.FMOD_System_SetPluginPath(SWIGTYPE_p_FMOD_SYSTEM.getCPtr(var0), var1);
   }

   public static int FMOD_System_LoadPlugin(SWIGTYPE_p_FMOD_SYSTEM var0, String var1, SWIGTYPE_p_unsigned_int var2, long var3) {
      return javafmodJNI.FMOD_System_LoadPlugin(SWIGTYPE_p_FMOD_SYSTEM.getCPtr(var0), var1, SWIGTYPE_p_unsigned_int.getCPtr(var2), var3);
   }

   public static int FMOD_System_UnloadPlugin(SWIGTYPE_p_FMOD_SYSTEM var0, long var1) {
      return javafmodJNI.FMOD_System_UnloadPlugin(SWIGTYPE_p_FMOD_SYSTEM.getCPtr(var0), var1);
   }

   public static int FMOD_System_GetNumPlugins(SWIGTYPE_p_FMOD_SYSTEM var0, SWIGTYPE_p_FMOD_PLUGINTYPE var1, SWIGTYPE_p_int var2) {
      return javafmodJNI.FMOD_System_GetNumPlugins(SWIGTYPE_p_FMOD_SYSTEM.getCPtr(var0), SWIGTYPE_p_FMOD_PLUGINTYPE.getCPtr(var1), SWIGTYPE_p_int.getCPtr(var2));
   }

   public static int FMOD_System_GetPluginHandle(SWIGTYPE_p_FMOD_SYSTEM var0, SWIGTYPE_p_FMOD_PLUGINTYPE var1, int var2, SWIGTYPE_p_unsigned_int var3) {
      return javafmodJNI.FMOD_System_GetPluginHandle(SWIGTYPE_p_FMOD_SYSTEM.getCPtr(var0), SWIGTYPE_p_FMOD_PLUGINTYPE.getCPtr(var1), var2, SWIGTYPE_p_unsigned_int.getCPtr(var3));
   }

   public static int FMOD_System_GetPluginInfo(SWIGTYPE_p_FMOD_SYSTEM var0, long var1, SWIGTYPE_p_FMOD_PLUGINTYPE var3, String var4, int var5, SWIGTYPE_p_unsigned_int var6) {
      return javafmodJNI.FMOD_System_GetPluginInfo(SWIGTYPE_p_FMOD_SYSTEM.getCPtr(var0), var1, SWIGTYPE_p_FMOD_PLUGINTYPE.getCPtr(var3), var4, var5, SWIGTYPE_p_unsigned_int.getCPtr(var6));
   }

   public static int FMOD_System_SetOutputByPlugin(SWIGTYPE_p_FMOD_SYSTEM var0, long var1) {
      return javafmodJNI.FMOD_System_SetOutputByPlugin(SWIGTYPE_p_FMOD_SYSTEM.getCPtr(var0), var1);
   }

   public static int FMOD_System_GetOutputByPlugin(SWIGTYPE_p_FMOD_SYSTEM var0, SWIGTYPE_p_unsigned_int var1) {
      return javafmodJNI.FMOD_System_GetOutputByPlugin(SWIGTYPE_p_FMOD_SYSTEM.getCPtr(var0), SWIGTYPE_p_unsigned_int.getCPtr(var1));
   }

   public static int FMOD_System_CreateDSPByPlugin(SWIGTYPE_p_FMOD_SYSTEM var0, long var1, SWIGTYPE_p_p_FMOD_DSP var3) {
      return javafmodJNI.FMOD_System_CreateDSPByPlugin(SWIGTYPE_p_FMOD_SYSTEM.getCPtr(var0), var1, SWIGTYPE_p_p_FMOD_DSP.getCPtr(var3));
   }

   public static int FMOD_System_GetDSPInfoByPlugin(SWIGTYPE_p_FMOD_SYSTEM var0, long var1, SWIGTYPE_p_p_FMOD_DSP_DESCRIPTION var3) {
      return javafmodJNI.FMOD_System_GetDSPInfoByPlugin(SWIGTYPE_p_FMOD_SYSTEM.getCPtr(var0), var1, SWIGTYPE_p_p_FMOD_DSP_DESCRIPTION.getCPtr(var3));
   }

   public static int FMOD_System_RegisterCodec(SWIGTYPE_p_FMOD_SYSTEM var0, SWIGTYPE_p_FMOD_CODEC_DESCRIPTION var1, SWIGTYPE_p_unsigned_int var2, long var3) {
      return javafmodJNI.FMOD_System_RegisterCodec(SWIGTYPE_p_FMOD_SYSTEM.getCPtr(var0), SWIGTYPE_p_FMOD_CODEC_DESCRIPTION.getCPtr(var1), SWIGTYPE_p_unsigned_int.getCPtr(var2), var3);
   }

   public static int FMOD_System_RegisterDSP(SWIGTYPE_p_FMOD_SYSTEM var0, SWIGTYPE_p_FMOD_DSP_DESCRIPTION var1, SWIGTYPE_p_unsigned_int var2) {
      return javafmodJNI.FMOD_System_RegisterDSP(SWIGTYPE_p_FMOD_SYSTEM.getCPtr(var0), SWIGTYPE_p_FMOD_DSP_DESCRIPTION.getCPtr(var1), SWIGTYPE_p_unsigned_int.getCPtr(var2));
   }

   public static int FMOD_System_Init(int var0, long var1, long var3) {
      return javafmodJNI.FMOD_System_Init(var0, var1, var3);
   }

   public static int FMOD_System_Close(SWIGTYPE_p_FMOD_SYSTEM var0) {
      return javafmodJNI.FMOD_System_Close(SWIGTYPE_p_FMOD_SYSTEM.getCPtr(var0));
   }

   public static int FMOD_System_Update() {
      return javafmodJNI.FMOD_System_Update();
   }

   public static int FMOD_System_SetSpeakerPosition(SWIGTYPE_p_FMOD_SYSTEM var0, SWIGTYPE_p_FMOD_SPEAKER var1, float var2, float var3, SWIGTYPE_p_FMOD_BOOL var4) {
      return javafmodJNI.FMOD_System_SetSpeakerPosition(SWIGTYPE_p_FMOD_SYSTEM.getCPtr(var0), SWIGTYPE_p_FMOD_SPEAKER.getCPtr(var1), var2, var3, SWIGTYPE_p_FMOD_BOOL.getCPtr(var4));
   }

   public static int FMOD_System_GetSpeakerPosition(SWIGTYPE_p_FMOD_SYSTEM var0, SWIGTYPE_p_FMOD_SPEAKER var1, SWIGTYPE_p_float var2, SWIGTYPE_p_float var3, SWIGTYPE_p_FMOD_BOOL var4) {
      return javafmodJNI.FMOD_System_GetSpeakerPosition(SWIGTYPE_p_FMOD_SYSTEM.getCPtr(var0), SWIGTYPE_p_FMOD_SPEAKER.getCPtr(var1), SWIGTYPE_p_float.getCPtr(var2), SWIGTYPE_p_float.getCPtr(var3), SWIGTYPE_p_FMOD_BOOL.getCPtr(var4));
   }

   public static int FMOD_System_SetStreamBufferSize(SWIGTYPE_p_FMOD_SYSTEM var0, long var1, SWIGTYPE_p_FMOD_TIMEUNIT var3) {
      return javafmodJNI.FMOD_System_SetStreamBufferSize(SWIGTYPE_p_FMOD_SYSTEM.getCPtr(var0), var1, SWIGTYPE_p_FMOD_TIMEUNIT.getCPtr(var3));
   }

   public static int FMOD_System_GetStreamBufferSize(SWIGTYPE_p_FMOD_SYSTEM var0, SWIGTYPE_p_unsigned_int var1, SWIGTYPE_p_FMOD_TIMEUNIT var2) {
      return javafmodJNI.FMOD_System_GetStreamBufferSize(SWIGTYPE_p_FMOD_SYSTEM.getCPtr(var0), SWIGTYPE_p_unsigned_int.getCPtr(var1), SWIGTYPE_p_FMOD_TIMEUNIT.getCPtr(var2));
   }

   public static int FMOD_System_Set3DSettings(float var0, float var1, float var2) {
      return javafmodJNI.FMOD_System_Set3DSettings(var0, var1, var2);
   }

   public static int FMOD_System_Get3DSettings(SWIGTYPE_p_float var0, SWIGTYPE_p_float var1, SWIGTYPE_p_float var2) {
      return javafmodJNI.FMOD_System_Get3DSettings(SWIGTYPE_p_float.getCPtr(var0), SWIGTYPE_p_float.getCPtr(var1), SWIGTYPE_p_float.getCPtr(var2));
   }

   public static int FMOD_System_Set3DNumListeners(int var0) {
      return javafmodJNI.FMOD_System_Set3DNumListeners(var0);
   }

   public static int FMOD_System_Get3DNumListeners(SWIGTYPE_p_FMOD_SYSTEM var0, SWIGTYPE_p_int var1) {
      return javafmodJNI.FMOD_System_Get3DNumListeners(SWIGTYPE_p_FMOD_SYSTEM.getCPtr(var0), SWIGTYPE_p_int.getCPtr(var1));
   }

   public static int FMOD_System_Set3DListenerAttributes(int var0, float var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8, float var9, float var10, float var11, float var12) {
      return javafmodJNI.FMOD_System_Set3DListenerAttributes(var0, var1, var2, var3, var4, var5, var6, var7, var8, var9, var10, var11, var12);
   }

   public static int FMOD_System_Get3DListenerAttributes(SWIGTYPE_p_FMOD_SYSTEM var0, int var1, SWIGTYPE_p_FMOD_VECTOR var2, SWIGTYPE_p_FMOD_VECTOR var3, SWIGTYPE_p_FMOD_VECTOR var4, SWIGTYPE_p_FMOD_VECTOR var5) {
      return javafmodJNI.FMOD_System_Get3DListenerAttributes(SWIGTYPE_p_FMOD_SYSTEM.getCPtr(var0), var1, SWIGTYPE_p_FMOD_VECTOR.getCPtr(var2), SWIGTYPE_p_FMOD_VECTOR.getCPtr(var3), SWIGTYPE_p_FMOD_VECTOR.getCPtr(var4), SWIGTYPE_p_FMOD_VECTOR.getCPtr(var5));
   }

   public static int FMOD_System_Set3DRolloffCallback(SWIGTYPE_p_FMOD_SYSTEM var0, SWIGTYPE_p_FMOD_3D_ROLLOFF_CALLBACK var1) {
      return javafmodJNI.FMOD_System_Set3DRolloffCallback(SWIGTYPE_p_FMOD_SYSTEM.getCPtr(var0), SWIGTYPE_p_FMOD_3D_ROLLOFF_CALLBACK.getCPtr(var1));
   }

   public static int FMOD_System_MixerSuspend(SWIGTYPE_p_FMOD_SYSTEM var0) {
      return javafmodJNI.FMOD_System_MixerSuspend(SWIGTYPE_p_FMOD_SYSTEM.getCPtr(var0));
   }

   public static int FMOD_System_MixerResume(SWIGTYPE_p_FMOD_SYSTEM var0) {
      return javafmodJNI.FMOD_System_MixerResume(SWIGTYPE_p_FMOD_SYSTEM.getCPtr(var0));
   }

   public static int FMOD_System_GetVersion(SWIGTYPE_p_FMOD_SYSTEM var0, SWIGTYPE_p_unsigned_int var1) {
      return javafmodJNI.FMOD_System_GetVersion(SWIGTYPE_p_FMOD_SYSTEM.getCPtr(var0), SWIGTYPE_p_unsigned_int.getCPtr(var1));
   }

   public static int FMOD_System_GetOutputHandle(SWIGTYPE_p_FMOD_SYSTEM var0, SWIGTYPE_p_p_void var1) {
      return javafmodJNI.FMOD_System_GetOutputHandle(SWIGTYPE_p_FMOD_SYSTEM.getCPtr(var0), SWIGTYPE_p_p_void.getCPtr(var1));
   }

   public static int FMOD_System_GetChannelsPlaying(SWIGTYPE_p_FMOD_SYSTEM var0, SWIGTYPE_p_int var1) {
      return javafmodJNI.FMOD_System_GetChannelsPlaying(SWIGTYPE_p_FMOD_SYSTEM.getCPtr(var0), SWIGTYPE_p_int.getCPtr(var1));
   }

   public static int FMOD_System_GetCPUUsage(SWIGTYPE_p_FMOD_SYSTEM var0, SWIGTYPE_p_float var1, SWIGTYPE_p_float var2, SWIGTYPE_p_float var3, SWIGTYPE_p_float var4, SWIGTYPE_p_float var5) {
      return javafmodJNI.FMOD_System_GetCPUUsage(SWIGTYPE_p_FMOD_SYSTEM.getCPtr(var0), SWIGTYPE_p_float.getCPtr(var1), SWIGTYPE_p_float.getCPtr(var2), SWIGTYPE_p_float.getCPtr(var3), SWIGTYPE_p_float.getCPtr(var4), SWIGTYPE_p_float.getCPtr(var5));
   }

   public static long FMOD_System_CreateSound(String var0, long var1) {
      return javafmodJNI.FMOD_System_CreateSound(var0, var1);
   }

   public static long FMOD_System_CreateRecordSound(long var0, long var2, long var4, long var6) {
      return javafmodJNI.FMOD_System_CreateRecordSound(var0, var2, var4, var6);
   }

   public static long FMOD_System_SetVADMode(int var0) {
      return javafmodJNI.FMOD_System_SetVADMode(var0);
   }

   public static long FMOD_System_SetRecordVolume(long var0) {
      return (long)javafmodJNI.FMOD_System_SetRecordVolume((int)var0);
   }

   public static long FMOD_System_CreateRAWPlaySound(long var0, long var2, long var4) {
      return javafmodJNI.FMOD_System_CreateRAWPlaySound(var0, var2, var4);
   }

   public static long FMOD_System_SetRawPlayBufferingPeriod(long var0) {
      return javafmodJNI.FMOD_System_SetRawPlayBufferingPeriod(var0);
   }

   public static int FMOD_System_RAWPlayData(long var0, short[] var2, long var3) {
      return javafmodJNI.FMOD_System_RAWPlayData(var0, var2, var3);
   }

   public static int FMOD_System_RAWPlayData(long var0, byte[] var2, long var3) {
      short[] var5 = new short[var2.length / 2];
      ByteBuffer.wrap(var2).order(ByteOrder.LITTLE_ENDIAN).asShortBuffer().get(var5);
      return javafmodJNI.FMOD_System_RAWPlayData(var0, var5, var3 / 2L);
   }

   public static int FMOD_Studio_LoadSampleData(long var0) {
      return javafmodJNI.FMOD_Studio_LoadSampleData(var0);
   }

   public static void FMOD_Studio_LoadEventSampleData(long var0) {
      javafmodJNI.FMOD_Studio_LoadEventSampleData(var0);
   }

   public static int FMOD_System_CreateStream(SWIGTYPE_p_FMOD_SYSTEM var0, String var1, SWIGTYPE_p_FMOD_MODE var2, SWIGTYPE_p_FMOD_CREATESOUNDEXINFO var3, SWIGTYPE_p_p_FMOD_SOUND var4) {
      return javafmodJNI.FMOD_System_CreateStream(SWIGTYPE_p_FMOD_SYSTEM.getCPtr(var0), var1, SWIGTYPE_p_FMOD_MODE.getCPtr(var2), SWIGTYPE_p_FMOD_CREATESOUNDEXINFO.getCPtr(var3), SWIGTYPE_p_p_FMOD_SOUND.getCPtr(var4));
   }

   public static int FMOD_System_CreateDSP(SWIGTYPE_p_FMOD_SYSTEM var0, SWIGTYPE_p_FMOD_DSP_DESCRIPTION var1, SWIGTYPE_p_p_FMOD_DSP var2) {
      return javafmodJNI.FMOD_System_CreateDSP(SWIGTYPE_p_FMOD_SYSTEM.getCPtr(var0), SWIGTYPE_p_FMOD_DSP_DESCRIPTION.getCPtr(var1), SWIGTYPE_p_p_FMOD_DSP.getCPtr(var2));
   }

   public static int FMOD_System_CreateDSPByType(SWIGTYPE_p_FMOD_SYSTEM var0, SWIGTYPE_p_FMOD_DSP_TYPE var1, SWIGTYPE_p_p_FMOD_DSP var2) {
      return javafmodJNI.FMOD_System_CreateDSPByType(SWIGTYPE_p_FMOD_SYSTEM.getCPtr(var0), SWIGTYPE_p_FMOD_DSP_TYPE.getCPtr(var1), SWIGTYPE_p_p_FMOD_DSP.getCPtr(var2));
   }

   public static long FMOD_System_CreateChannelGroup(String var0) {
      try {
         return javafmodJNI.FMOD_System_CreateChannelGroup(var0);
      } catch (Throwable var2) {
         DebugLog.log("ERROR: FMOD_System_CreateChannelGroup exception:" + var2.getMessage());
         return 0L;
      }
   }

   public static int FMOD_System_CreateSoundGroup(SWIGTYPE_p_FMOD_SYSTEM var0, String var1, SWIGTYPE_p_p_FMOD_SOUNDGROUP var2) {
      return javafmodJNI.FMOD_System_CreateSoundGroup(SWIGTYPE_p_FMOD_SYSTEM.getCPtr(var0), var1, SWIGTYPE_p_p_FMOD_SOUNDGROUP.getCPtr(var2));
   }

   public static int FMOD_System_CreateReverb3D(SWIGTYPE_p_FMOD_SYSTEM var0, SWIGTYPE_p_p_FMOD_REVERB3D var1) {
      return javafmodJNI.FMOD_System_CreateReverb3D(SWIGTYPE_p_FMOD_SYSTEM.getCPtr(var0), SWIGTYPE_p_p_FMOD_REVERB3D.getCPtr(var1));
   }

   public static long FMOD_System_PlaySound(long var0, boolean var2) {
      return javafmodJNI.FMOD_System_PlaySound(var0, var2 ? 1L : 0L);
   }

   public static int FMOD_System_PlayDSP() {
      return javafmodJNI.FMOD_System_PlayDSP();
   }

   public static int FMOD_System_GetChannel(SWIGTYPE_p_FMOD_SYSTEM var0, int var1, SWIGTYPE_p_p_FMOD_CHANNEL var2) {
      return javafmodJNI.FMOD_System_GetChannel(SWIGTYPE_p_FMOD_SYSTEM.getCPtr(var0), var1, SWIGTYPE_p_p_FMOD_CHANNEL.getCPtr(var2));
   }

   public static long FMOD_System_GetMasterChannelGroup() {
      return javafmodJNI.FMOD_System_GetMasterChannelGroup();
   }

   public static int FMOD_System_GetMasterSoundGroup(SWIGTYPE_p_FMOD_SYSTEM var0, SWIGTYPE_p_p_FMOD_SOUNDGROUP var1) {
      return javafmodJNI.FMOD_System_GetMasterSoundGroup(SWIGTYPE_p_FMOD_SYSTEM.getCPtr(var0), SWIGTYPE_p_p_FMOD_SOUNDGROUP.getCPtr(var1));
   }

   public static int FMOD_System_AttachChannelGroupToPort(SWIGTYPE_p_FMOD_SYSTEM var0, SWIGTYPE_p_FMOD_PORT_TYPE var1, SWIGTYPE_p_FMOD_PORT_INDEX var2, SWIGTYPE_p_FMOD_CHANNELGROUP var3, SWIGTYPE_p_FMOD_BOOL var4) {
      return javafmodJNI.FMOD_System_AttachChannelGroupToPort(SWIGTYPE_p_FMOD_SYSTEM.getCPtr(var0), SWIGTYPE_p_FMOD_PORT_TYPE.getCPtr(var1), SWIGTYPE_p_FMOD_PORT_INDEX.getCPtr(var2), SWIGTYPE_p_FMOD_CHANNELGROUP.getCPtr(var3), SWIGTYPE_p_FMOD_BOOL.getCPtr(var4));
   }

   public static int FMOD_System_DetachChannelGroupFromPort(SWIGTYPE_p_FMOD_SYSTEM var0, SWIGTYPE_p_FMOD_CHANNELGROUP var1) {
      return javafmodJNI.FMOD_System_DetachChannelGroupFromPort(SWIGTYPE_p_FMOD_SYSTEM.getCPtr(var0), SWIGTYPE_p_FMOD_CHANNELGROUP.getCPtr(var1));
   }

   public static int FMOD_System_SetReverbProperties(SWIGTYPE_p_FMOD_SYSTEM var0, int var1, SWIGTYPE_p_FMOD_REVERB_PROPERTIES var2) {
      return javafmodJNI.FMOD_System_SetReverbProperties(SWIGTYPE_p_FMOD_SYSTEM.getCPtr(var0), var1, SWIGTYPE_p_FMOD_REVERB_PROPERTIES.getCPtr(var2));
   }

   public static int FMOD_System_GetReverbProperties(SWIGTYPE_p_FMOD_SYSTEM var0, int var1, SWIGTYPE_p_FMOD_REVERB_PROPERTIES var2) {
      return javafmodJNI.FMOD_System_GetReverbProperties(SWIGTYPE_p_FMOD_SYSTEM.getCPtr(var0), var1, SWIGTYPE_p_FMOD_REVERB_PROPERTIES.getCPtr(var2));
   }

   public static int FMOD_System_LockDSP(SWIGTYPE_p_FMOD_SYSTEM var0) {
      return javafmodJNI.FMOD_System_LockDSP(SWIGTYPE_p_FMOD_SYSTEM.getCPtr(var0));
   }

   public static int FMOD_System_UnlockDSP(SWIGTYPE_p_FMOD_SYSTEM var0) {
      return javafmodJNI.FMOD_System_UnlockDSP(SWIGTYPE_p_FMOD_SYSTEM.getCPtr(var0));
   }

   public static int FMOD_System_GetRecordNumDrivers() {
      return javafmodJNI.FMOD_System_GetRecordNumDrivers();
   }

   public static int FMOD_System_GetRecordDriverInfo(int var0, FMOD_DriverInfo var1) {
      return javafmodJNI.FMOD_System_GetRecordDriverInfo(var0, var1);
   }

   public static int FMOD_System_GetRecordPosition(int var0, Long var1) {
      return javafmodJNI.FMOD_System_GetRecordPosition(var0, var1);
   }

   public static int FMOD_System_RecordStart(int var0, long var1, boolean var3) {
      return javafmodJNI.FMOD_System_RecordStart(var0, var1, var3);
   }

   public static int FMOD_System_RecordStop(int var0) {
      return javafmodJNI.FMOD_System_RecordStop(var0);
   }

   public static int FMOD_System_IsRecording(SWIGTYPE_p_FMOD_SYSTEM var0, int var1, SWIGTYPE_p_FMOD_BOOL var2) {
      return javafmodJNI.FMOD_System_IsRecording(SWIGTYPE_p_FMOD_SYSTEM.getCPtr(var0), var1, SWIGTYPE_p_FMOD_BOOL.getCPtr(var2));
   }

   public static int FMOD_System_CreateGeometry(SWIGTYPE_p_FMOD_SYSTEM var0, int var1, int var2, SWIGTYPE_p_p_FMOD_GEOMETRY var3) {
      return javafmodJNI.FMOD_System_CreateGeometry(SWIGTYPE_p_FMOD_SYSTEM.getCPtr(var0), var1, var2, SWIGTYPE_p_p_FMOD_GEOMETRY.getCPtr(var3));
   }

   public static int FMOD_System_SetGeometrySettings(SWIGTYPE_p_FMOD_SYSTEM var0, float var1) {
      return javafmodJNI.FMOD_System_SetGeometrySettings(SWIGTYPE_p_FMOD_SYSTEM.getCPtr(var0), var1);
   }

   public static int FMOD_System_GetGeometrySettings(SWIGTYPE_p_FMOD_SYSTEM var0, SWIGTYPE_p_float var1) {
      return javafmodJNI.FMOD_System_GetGeometrySettings(SWIGTYPE_p_FMOD_SYSTEM.getCPtr(var0), SWIGTYPE_p_float.getCPtr(var1));
   }

   public static int FMOD_System_LoadGeometry(SWIGTYPE_p_FMOD_SYSTEM var0, SWIGTYPE_p_void var1, int var2, SWIGTYPE_p_p_FMOD_GEOMETRY var3) {
      return javafmodJNI.FMOD_System_LoadGeometry(SWIGTYPE_p_FMOD_SYSTEM.getCPtr(var0), SWIGTYPE_p_void.getCPtr(var1), var2, SWIGTYPE_p_p_FMOD_GEOMETRY.getCPtr(var3));
   }

   public static int FMOD_System_GetGeometryOcclusion(SWIGTYPE_p_FMOD_SYSTEM var0, SWIGTYPE_p_FMOD_VECTOR var1, SWIGTYPE_p_FMOD_VECTOR var2, SWIGTYPE_p_float var3, SWIGTYPE_p_float var4) {
      return javafmodJNI.FMOD_System_GetGeometryOcclusion(SWIGTYPE_p_FMOD_SYSTEM.getCPtr(var0), SWIGTYPE_p_FMOD_VECTOR.getCPtr(var1), SWIGTYPE_p_FMOD_VECTOR.getCPtr(var2), SWIGTYPE_p_float.getCPtr(var3), SWIGTYPE_p_float.getCPtr(var4));
   }

   public static int FMOD_System_SetNetworkProxy(SWIGTYPE_p_FMOD_SYSTEM var0, String var1) {
      return javafmodJNI.FMOD_System_SetNetworkProxy(SWIGTYPE_p_FMOD_SYSTEM.getCPtr(var0), var1);
   }

   public static int FMOD_System_GetNetworkProxy(SWIGTYPE_p_FMOD_SYSTEM var0, String var1, int var2) {
      return javafmodJNI.FMOD_System_GetNetworkProxy(SWIGTYPE_p_FMOD_SYSTEM.getCPtr(var0), var1, var2);
   }

   public static int FMOD_System_SetNetworkTimeout(SWIGTYPE_p_FMOD_SYSTEM var0, int var1) {
      return javafmodJNI.FMOD_System_SetNetworkTimeout(SWIGTYPE_p_FMOD_SYSTEM.getCPtr(var0), var1);
   }

   public static int FMOD_System_GetNetworkTimeout(SWIGTYPE_p_FMOD_SYSTEM var0, SWIGTYPE_p_int var1) {
      return javafmodJNI.FMOD_System_GetNetworkTimeout(SWIGTYPE_p_FMOD_SYSTEM.getCPtr(var0), SWIGTYPE_p_int.getCPtr(var1));
   }

   public static int FMOD_System_SetUserData(SWIGTYPE_p_FMOD_SYSTEM var0, SWIGTYPE_p_void var1) {
      return javafmodJNI.FMOD_System_SetUserData(SWIGTYPE_p_FMOD_SYSTEM.getCPtr(var0), SWIGTYPE_p_void.getCPtr(var1));
   }

   public static int FMOD_System_GetUserData(SWIGTYPE_p_FMOD_SYSTEM var0, SWIGTYPE_p_p_void var1) {
      return javafmodJNI.FMOD_System_GetUserData(SWIGTYPE_p_FMOD_SYSTEM.getCPtr(var0), SWIGTYPE_p_p_void.getCPtr(var1));
   }

   public static int FMOD_Sound_Release(long var0) {
      return javafmodJNI.FMOD_Sound_Release(var0);
   }

   public static int FMOD_RAWPlaySound_Release(long var0) {
      return javafmodJNI.FMOD_RAWPlaySound_Release(var0);
   }

   public static int FMOD_RecordSound_Release(long var0) {
      return javafmodJNI.FMOD_RecordSound_Release(var0);
   }

   public static int FMOD_Sound_GetSystemObject(SWIGTYPE_p_FMOD_SOUND var0, SWIGTYPE_p_p_FMOD_SYSTEM var1) {
      return javafmodJNI.FMOD_Sound_GetSystemObject(SWIGTYPE_p_FMOD_SOUND.getCPtr(var0), SWIGTYPE_p_p_FMOD_SYSTEM.getCPtr(var1));
   }

   public static int FMOD_Sound_Lock(long var0, long var2, long var4, byte[] var6, byte[] var7, Long var8, Long var9, long[] var10) {
      return javafmodJNI.FMOD_Sound_Lock(var0, var2, var4, var6, var7, var8, var9, var10);
   }

   public static int FMOD_Sound_GetData(long var0, byte[] var2, Long var3, Long var4, Long var5) {
      return javafmodJNI.FMOD_Sound_GetData(var0, var2, var3, var4, var5);
   }

   public static int FMOD_Sound_Unlock(long var0, long[] var2) {
      return javafmodJNI.FMOD_Sound_Unlock(var0, var2);
   }

   public static int FMOD_Sound_SetDefaults(SWIGTYPE_p_FMOD_SOUND var0, float var1, int var2) {
      return javafmodJNI.FMOD_Sound_SetDefaults(SWIGTYPE_p_FMOD_SOUND.getCPtr(var0), var1, var2);
   }

   public static int FMOD_Sound_GetDefaults(SWIGTYPE_p_FMOD_SOUND var0, SWIGTYPE_p_float var1, SWIGTYPE_p_int var2) {
      return javafmodJNI.FMOD_Sound_GetDefaults(SWIGTYPE_p_FMOD_SOUND.getCPtr(var0), SWIGTYPE_p_float.getCPtr(var1), SWIGTYPE_p_int.getCPtr(var2));
   }

   public static int FMOD_Sound_Set3DMinMaxDistance(long var0, float var2, float var3) {
      return javafmodJNI.FMOD_Sound_Set3DMinMaxDistance(var0, var2, var3);
   }

   public static int FMOD_Sound_Get3DMinMaxDistance(SWIGTYPE_p_FMOD_SOUND var0, SWIGTYPE_p_float var1, SWIGTYPE_p_float var2) {
      return javafmodJNI.FMOD_Sound_Get3DMinMaxDistance(SWIGTYPE_p_FMOD_SOUND.getCPtr(var0), SWIGTYPE_p_float.getCPtr(var1), SWIGTYPE_p_float.getCPtr(var2));
   }

   public static long FMOD_Studio_System_Create() {
      return javafmodJNI.FMOD_Studio_Create();
   }

   public static void FMOD_Studio_StartEvent(long var0) {
      javafmodJNI.FMOD_Studio_StartEvent(var0);
   }

   public static long FMOD_Studio_GetTimelinePosition(long var0) {
      return javafmodJNI.FMOD_Studio_GetTimelinePosition(var0);
   }

   public static long FMOD_Studio_System_GetBus(String var0) {
      return javafmodJNI.FMOD_Studio_System_GetBus(var0);
   }

   public static long FMOD_Studio_System_GetEvent(String var0) {
      if (map.containsKey(var0)) {
         return (Long)map.get(var0);
      } else {
         long var1 = javafmodJNI.FMOD_Studio_GetEvent(var0);
         map.put(var0, var1);
         return var1;
      }
   }

   public static long FMOD_Studio_System_CreateEventInstance(long var0) {
      return javafmodJNI.FMOD_Studio_CreateEventInstance(var0);
   }

   public static long FMOD_Studio_System_LoadBankFile(String var0) {
      return javafmodJNI.FMOD_Studio_LoadBankFile(var0);
   }

   public static int FMOD_Studio_System_SetParameterByID(FMOD_STUDIO_PARAMETER_ID var0, float var1, boolean var2) {
      return var0 == null ? 0 : javafmodJNI.FMOD_Studio_System_SetParameterByID(var0.address(), var1, var2);
   }

   public static int FMOD_Studio_System_SetParameterByName(String var0, float var1, boolean var2) {
      return javafmodJNI.FMOD_Studio_System_SetParameterByName(var0, var1, var2);
   }

   public static boolean FMOD_Studio_Bus_GetMute(long var0) {
      return var0 == 0L ? false : javafmodJNI.FMOD_Studio_Bus_GetMute(var0);
   }

   public static boolean FMOD_Studio_Bus_GetPaused(long var0) {
      return var0 == 0L ? false : javafmodJNI.FMOD_Studio_Bus_GetMute(var0);
   }

   public static float FMOD_Studio_Bus_GetVolume(long var0) {
      return var0 == 0L ? 0.0F : javafmodJNI.FMOD_Studio_Bus_GetVolume(var0);
   }

   public static int FMOD_Studio_Bus_SetMute(long var0, boolean var2) {
      return var0 == 0L ? -1 : javafmodJNI.FMOD_Studio_Bus_SetMute(var0, var2);
   }

   public static int FMOD_Studio_Bus_SetPaused(long var0, boolean var2) {
      return var0 == 0L ? -1 : javafmodJNI.FMOD_Studio_Bus_SetPaused(var0, var2);
   }

   public static int FMOD_Studio_Bus_SetVolume(long var0, float var2) {
      return var0 == 0L ? -1 : javafmodJNI.FMOD_Studio_Bus_SetVolume(var0, var2);
   }

   public static int FMOD_Studio_Bus_StopAllEvents(long var0, boolean var2) {
      return var0 == 0L ? -1 : javafmodJNI.FMOD_Studio_Bus_StopAllEvents(var0, var2);
   }

   public static int FMOD_Sound_Set3DConeSettings(SWIGTYPE_p_FMOD_SOUND var0, float var1, float var2, float var3) {
      return javafmodJNI.FMOD_Sound_Set3DConeSettings(SWIGTYPE_p_FMOD_SOUND.getCPtr(var0), var1, var2, var3);
   }

   public static int FMOD_Sound_Get3DConeSettings(SWIGTYPE_p_FMOD_SOUND var0, SWIGTYPE_p_float var1, SWIGTYPE_p_float var2, SWIGTYPE_p_float var3) {
      return javafmodJNI.FMOD_Sound_Get3DConeSettings(SWIGTYPE_p_FMOD_SOUND.getCPtr(var0), SWIGTYPE_p_float.getCPtr(var1), SWIGTYPE_p_float.getCPtr(var2), SWIGTYPE_p_float.getCPtr(var3));
   }

   public static int FMOD_Sound_Set3DCustomRolloff(SWIGTYPE_p_FMOD_SOUND var0, SWIGTYPE_p_FMOD_VECTOR var1, int var2) {
      return javafmodJNI.FMOD_Sound_Set3DCustomRolloff(SWIGTYPE_p_FMOD_SOUND.getCPtr(var0), SWIGTYPE_p_FMOD_VECTOR.getCPtr(var1), var2);
   }

   public static int FMOD_Sound_Get3DCustomRolloff(SWIGTYPE_p_FMOD_SOUND var0, SWIGTYPE_p_p_FMOD_VECTOR var1, SWIGTYPE_p_int var2) {
      return javafmodJNI.FMOD_Sound_Get3DCustomRolloff(SWIGTYPE_p_FMOD_SOUND.getCPtr(var0), SWIGTYPE_p_p_FMOD_VECTOR.getCPtr(var1), SWIGTYPE_p_int.getCPtr(var2));
   }

   public static int FMOD_Sound_SetSubSound(SWIGTYPE_p_FMOD_SOUND var0, int var1, SWIGTYPE_p_FMOD_SOUND var2) {
      return javafmodJNI.FMOD_Sound_SetSubSound(SWIGTYPE_p_FMOD_SOUND.getCPtr(var0), var1, SWIGTYPE_p_FMOD_SOUND.getCPtr(var2));
   }

   public static int FMOD_Sound_GetSubSound(SWIGTYPE_p_FMOD_SOUND var0, int var1, SWIGTYPE_p_p_FMOD_SOUND var2) {
      return javafmodJNI.FMOD_Sound_GetSubSound(SWIGTYPE_p_FMOD_SOUND.getCPtr(var0), var1, SWIGTYPE_p_p_FMOD_SOUND.getCPtr(var2));
   }

   public static int FMOD_Sound_GetSubSoundParent(SWIGTYPE_p_FMOD_SOUND var0, SWIGTYPE_p_p_FMOD_SOUND var1) {
      return javafmodJNI.FMOD_Sound_GetSubSoundParent(SWIGTYPE_p_FMOD_SOUND.getCPtr(var0), SWIGTYPE_p_p_FMOD_SOUND.getCPtr(var1));
   }

   public static int FMOD_Sound_GetName(SWIGTYPE_p_FMOD_SOUND var0, String var1, int var2) {
      return javafmodJNI.FMOD_Sound_GetName(SWIGTYPE_p_FMOD_SOUND.getCPtr(var0), var1, var2);
   }

   public static long FMOD_Sound_GetLength(long var0, long var2) {
      return (long)javafmodJNI.FMOD_Sound_GetLength(var0, var2);
   }

   public static int FMOD_Sound_GetFormat(SWIGTYPE_p_FMOD_SOUND var0, SWIGTYPE_p_FMOD_SOUND_TYPE var1, SWIGTYPE_p_FMOD_SOUND_FORMAT var2, SWIGTYPE_p_int var3, SWIGTYPE_p_int var4) {
      return javafmodJNI.FMOD_Sound_GetFormat(SWIGTYPE_p_FMOD_SOUND.getCPtr(var0), SWIGTYPE_p_FMOD_SOUND_TYPE.getCPtr(var1), SWIGTYPE_p_FMOD_SOUND_FORMAT.getCPtr(var2), SWIGTYPE_p_int.getCPtr(var3), SWIGTYPE_p_int.getCPtr(var4));
   }

   public static int FMOD_Sound_GetNumSubSounds(SWIGTYPE_p_FMOD_SOUND var0, SWIGTYPE_p_int var1) {
      return javafmodJNI.FMOD_Sound_GetNumSubSounds(SWIGTYPE_p_FMOD_SOUND.getCPtr(var0), SWIGTYPE_p_int.getCPtr(var1));
   }

   public static int FMOD_Sound_GetNumTags(SWIGTYPE_p_FMOD_SOUND var0, SWIGTYPE_p_int var1, SWIGTYPE_p_int var2) {
      return javafmodJNI.FMOD_Sound_GetNumTags(SWIGTYPE_p_FMOD_SOUND.getCPtr(var0), SWIGTYPE_p_int.getCPtr(var1), SWIGTYPE_p_int.getCPtr(var2));
   }

   public static int FMOD_Sound_GetTag(SWIGTYPE_p_FMOD_SOUND var0, String var1, int var2, SWIGTYPE_p_FMOD_TAG var3) {
      return javafmodJNI.FMOD_Sound_GetTag(SWIGTYPE_p_FMOD_SOUND.getCPtr(var0), var1, var2, SWIGTYPE_p_FMOD_TAG.getCPtr(var3));
   }

   public static int FMOD_Sound_GetOpenState(SWIGTYPE_p_FMOD_SOUND var0, SWIGTYPE_p_FMOD_OPENSTATE var1, SWIGTYPE_p_unsigned_int var2, SWIGTYPE_p_FMOD_BOOL var3, SWIGTYPE_p_FMOD_BOOL var4) {
      return javafmodJNI.FMOD_Sound_GetOpenState(SWIGTYPE_p_FMOD_SOUND.getCPtr(var0), SWIGTYPE_p_FMOD_OPENSTATE.getCPtr(var1), SWIGTYPE_p_unsigned_int.getCPtr(var2), SWIGTYPE_p_FMOD_BOOL.getCPtr(var3), SWIGTYPE_p_FMOD_BOOL.getCPtr(var4));
   }

   public static int FMOD_Sound_ReadData(SWIGTYPE_p_FMOD_SOUND var0, SWIGTYPE_p_void var1, long var2, SWIGTYPE_p_unsigned_int var4) {
      return javafmodJNI.FMOD_Sound_ReadData(SWIGTYPE_p_FMOD_SOUND.getCPtr(var0), SWIGTYPE_p_void.getCPtr(var1), var2, SWIGTYPE_p_unsigned_int.getCPtr(var4));
   }

   public static int FMOD_Sound_SeekData(SWIGTYPE_p_FMOD_SOUND var0, long var1) {
      return javafmodJNI.FMOD_Sound_SeekData(SWIGTYPE_p_FMOD_SOUND.getCPtr(var0), var1);
   }

   public static int FMOD_Sound_SetSoundGroup(long var0, long var2) {
      return javafmodJNI.FMOD_Sound_SetSoundGroup(var0, var2);
   }

   public static int FMOD_Sound_GetSoundGroup(SWIGTYPE_p_FMOD_SOUND var0, SWIGTYPE_p_p_FMOD_SOUNDGROUP var1) {
      return javafmodJNI.FMOD_Sound_GetSoundGroup(SWIGTYPE_p_FMOD_SOUND.getCPtr(var0), SWIGTYPE_p_p_FMOD_SOUNDGROUP.getCPtr(var1));
   }

   public static int FMOD_Sound_GetNumSyncPoints(SWIGTYPE_p_FMOD_SOUND var0, SWIGTYPE_p_int var1) {
      return javafmodJNI.FMOD_Sound_GetNumSyncPoints(SWIGTYPE_p_FMOD_SOUND.getCPtr(var0), SWIGTYPE_p_int.getCPtr(var1));
   }

   public static int FMOD_Sound_GetSyncPoint(SWIGTYPE_p_FMOD_SOUND var0, int var1, SWIGTYPE_p_p_FMOD_SYNCPOINT var2) {
      return javafmodJNI.FMOD_Sound_GetSyncPoint(SWIGTYPE_p_FMOD_SOUND.getCPtr(var0), var1, SWIGTYPE_p_p_FMOD_SYNCPOINT.getCPtr(var2));
   }

   public static int FMOD_Sound_GetSyncPointInfo(SWIGTYPE_p_FMOD_SOUND var0, SWIGTYPE_p_FMOD_SYNCPOINT var1, String var2, int var3, SWIGTYPE_p_unsigned_int var4, SWIGTYPE_p_FMOD_TIMEUNIT var5) {
      return javafmodJNI.FMOD_Sound_GetSyncPointInfo(SWIGTYPE_p_FMOD_SOUND.getCPtr(var0), SWIGTYPE_p_FMOD_SYNCPOINT.getCPtr(var1), var2, var3, SWIGTYPE_p_unsigned_int.getCPtr(var4), SWIGTYPE_p_FMOD_TIMEUNIT.getCPtr(var5));
   }

   public static int FMOD_Sound_AddSyncPoint(SWIGTYPE_p_FMOD_SOUND var0, long var1, SWIGTYPE_p_FMOD_TIMEUNIT var3, String var4, SWIGTYPE_p_p_FMOD_SYNCPOINT var5) {
      return javafmodJNI.FMOD_Sound_AddSyncPoint(SWIGTYPE_p_FMOD_SOUND.getCPtr(var0), var1, SWIGTYPE_p_FMOD_TIMEUNIT.getCPtr(var3), var4, SWIGTYPE_p_p_FMOD_SYNCPOINT.getCPtr(var5));
   }

   public static int FMOD_Sound_DeleteSyncPoint(SWIGTYPE_p_FMOD_SOUND var0, SWIGTYPE_p_FMOD_SYNCPOINT var1) {
      return javafmodJNI.FMOD_Sound_DeleteSyncPoint(SWIGTYPE_p_FMOD_SOUND.getCPtr(var0), SWIGTYPE_p_FMOD_SYNCPOINT.getCPtr(var1));
   }

   public static int FMOD_Sound_SetMode(long var0, int var2) {
      return javafmodJNI.FMOD_Sound_SetMode(var0, (long)var2);
   }

   public static int FMOD_Sound_GetMode(SWIGTYPE_p_FMOD_SOUND var0, SWIGTYPE_p_FMOD_MODE var1) {
      return javafmodJNI.FMOD_Sound_GetMode(SWIGTYPE_p_FMOD_SOUND.getCPtr(var0), SWIGTYPE_p_FMOD_MODE.getCPtr(var1));
   }

   public static int FMOD_Sound_SetLoopCount(long var0, int var2) {
      return javafmodJNI.FMOD_Sound_SetLoopCount(var0, var2);
   }

   public static int FMOD_Sound_GetLoopCount(SWIGTYPE_p_FMOD_SOUND var0, SWIGTYPE_p_int var1) {
      return javafmodJNI.FMOD_Sound_GetLoopCount(SWIGTYPE_p_FMOD_SOUND.getCPtr(var0), SWIGTYPE_p_int.getCPtr(var1));
   }

   public static int FMOD_Sound_SetLoopPoints(SWIGTYPE_p_FMOD_SOUND var0, long var1, SWIGTYPE_p_FMOD_TIMEUNIT var3, long var4, SWIGTYPE_p_FMOD_TIMEUNIT var6) {
      return javafmodJNI.FMOD_Sound_SetLoopPoints(SWIGTYPE_p_FMOD_SOUND.getCPtr(var0), var1, SWIGTYPE_p_FMOD_TIMEUNIT.getCPtr(var3), var4, SWIGTYPE_p_FMOD_TIMEUNIT.getCPtr(var6));
   }

   public static int FMOD_Sound_GetLoopPoints(SWIGTYPE_p_FMOD_SOUND var0, SWIGTYPE_p_unsigned_int var1, SWIGTYPE_p_FMOD_TIMEUNIT var2, SWIGTYPE_p_unsigned_int var3, SWIGTYPE_p_FMOD_TIMEUNIT var4) {
      return javafmodJNI.FMOD_Sound_GetLoopPoints(SWIGTYPE_p_FMOD_SOUND.getCPtr(var0), SWIGTYPE_p_unsigned_int.getCPtr(var1), SWIGTYPE_p_FMOD_TIMEUNIT.getCPtr(var2), SWIGTYPE_p_unsigned_int.getCPtr(var3), SWIGTYPE_p_FMOD_TIMEUNIT.getCPtr(var4));
   }

   public static int FMOD_Sound_GetMusicNumChannels(SWIGTYPE_p_FMOD_SOUND var0, SWIGTYPE_p_int var1) {
      return javafmodJNI.FMOD_Sound_GetMusicNumChannels(SWIGTYPE_p_FMOD_SOUND.getCPtr(var0), SWIGTYPE_p_int.getCPtr(var1));
   }

   public static int FMOD_Sound_SetMusicChannelVolume(SWIGTYPE_p_FMOD_SOUND var0, int var1, float var2) {
      return javafmodJNI.FMOD_Sound_SetMusicChannelVolume(SWIGTYPE_p_FMOD_SOUND.getCPtr(var0), var1, var2);
   }

   public static int FMOD_Sound_GetMusicChannelVolume(SWIGTYPE_p_FMOD_SOUND var0, int var1, SWIGTYPE_p_float var2) {
      return javafmodJNI.FMOD_Sound_GetMusicChannelVolume(SWIGTYPE_p_FMOD_SOUND.getCPtr(var0), var1, SWIGTYPE_p_float.getCPtr(var2));
   }

   public static int FMOD_Sound_SetMusicSpeed(SWIGTYPE_p_FMOD_SOUND var0, float var1) {
      return javafmodJNI.FMOD_Sound_SetMusicSpeed(SWIGTYPE_p_FMOD_SOUND.getCPtr(var0), var1);
   }

   public static int FMOD_Sound_GetMusicSpeed(SWIGTYPE_p_FMOD_SOUND var0, SWIGTYPE_p_float var1) {
      return javafmodJNI.FMOD_Sound_GetMusicSpeed(SWIGTYPE_p_FMOD_SOUND.getCPtr(var0), SWIGTYPE_p_float.getCPtr(var1));
   }

   public static int FMOD_Sound_SetUserData(SWIGTYPE_p_FMOD_SOUND var0, SWIGTYPE_p_void var1) {
      return javafmodJNI.FMOD_Sound_SetUserData(SWIGTYPE_p_FMOD_SOUND.getCPtr(var0), SWIGTYPE_p_void.getCPtr(var1));
   }

   public static int FMOD_Sound_GetUserData(SWIGTYPE_p_FMOD_SOUND var0, SWIGTYPE_p_p_void var1) {
      return javafmodJNI.FMOD_Sound_GetUserData(SWIGTYPE_p_FMOD_SOUND.getCPtr(var0), SWIGTYPE_p_p_void.getCPtr(var1));
   }

   public static int FMOD_Channel_GetSystemObject(SWIGTYPE_p_FMOD_CHANNEL var0, SWIGTYPE_p_p_FMOD_SYSTEM var1) {
      return javafmodJNI.FMOD_Channel_GetSystemObject(SWIGTYPE_p_FMOD_CHANNEL.getCPtr(var0), SWIGTYPE_p_p_FMOD_SYSTEM.getCPtr(var1));
   }

   public static int FMOD_Channel_Stop(long var0) {
      return javafmodJNI.FMOD_Channel_Stop(var0);
   }

   public static int FMOD_Channel_SetPaused(long var0, boolean var2) {
      return javafmodJNI.FMOD_Channel_SetPaused(var0, var2 ? 1L : 0L);
   }

   public static int FMOD_Channel_GetPaused(SWIGTYPE_p_FMOD_CHANNEL var0, SWIGTYPE_p_FMOD_BOOL var1) {
      return javafmodJNI.FMOD_Channel_GetPaused(SWIGTYPE_p_FMOD_CHANNEL.getCPtr(var0), SWIGTYPE_p_FMOD_BOOL.getCPtr(var1));
   }

   public static int FMOD_Channel_SetVolume(long var0, float var2) {
      return javafmodJNI.FMOD_Channel_SetVolume(var0, var2);
   }

   public static int FMOD_Channel_GetVolume(SWIGTYPE_p_FMOD_CHANNEL var0, SWIGTYPE_p_float var1) {
      return javafmodJNI.FMOD_Channel_GetVolume(SWIGTYPE_p_FMOD_CHANNEL.getCPtr(var0), SWIGTYPE_p_float.getCPtr(var1));
   }

   public static int FMOD_Channel_SetVolumeRamp(SWIGTYPE_p_FMOD_CHANNEL var0, SWIGTYPE_p_FMOD_BOOL var1) {
      return javafmodJNI.FMOD_Channel_SetVolumeRamp(SWIGTYPE_p_FMOD_CHANNEL.getCPtr(var0), SWIGTYPE_p_FMOD_BOOL.getCPtr(var1));
   }

   public static int FMOD_Channel_GetVolumeRamp(SWIGTYPE_p_FMOD_CHANNEL var0, SWIGTYPE_p_FMOD_BOOL var1) {
      return javafmodJNI.FMOD_Channel_GetVolumeRamp(SWIGTYPE_p_FMOD_CHANNEL.getCPtr(var0), SWIGTYPE_p_FMOD_BOOL.getCPtr(var1));
   }

   public static float FMOD_Channel_GetAudibility(long var0) {
      return javafmodJNI.FMOD_Channel_GetAudibility(var0);
   }

   public static int FMOD_Channel_SetPitch(long var0, float var2) {
      return javafmodJNI.FMOD_Channel_SetPitch(var0, var2);
   }

   public static int FMOD_Channel_SetPitch(SWIGTYPE_p_FMOD_CHANNEL var0, float var1) {
      return javafmodJNI.FMOD_Channel_SetPitch(SWIGTYPE_p_FMOD_CHANNEL.getCPtr(var0), var1);
   }

   public static int FMOD_Channel_GetPitch(SWIGTYPE_p_FMOD_CHANNEL var0, SWIGTYPE_p_float var1) {
      return javafmodJNI.FMOD_Channel_GetPitch(SWIGTYPE_p_FMOD_CHANNEL.getCPtr(var0), SWIGTYPE_p_float.getCPtr(var1));
   }

   public static int FMOD_Channel_SetMute(SWIGTYPE_p_FMOD_CHANNEL var0, SWIGTYPE_p_FMOD_BOOL var1) {
      return javafmodJNI.FMOD_Channel_SetMute(SWIGTYPE_p_FMOD_CHANNEL.getCPtr(var0), SWIGTYPE_p_FMOD_BOOL.getCPtr(var1));
   }

   public static int FMOD_Channel_GetMute(SWIGTYPE_p_FMOD_CHANNEL var0, SWIGTYPE_p_FMOD_BOOL var1) {
      return javafmodJNI.FMOD_Channel_GetMute(SWIGTYPE_p_FMOD_CHANNEL.getCPtr(var0), SWIGTYPE_p_FMOD_BOOL.getCPtr(var1));
   }

   public static int FMOD_Channel_SetReverbProperties(long var0, int var2, float var3) {
      return javafmodJNI.FMOD_Channel_SetReverbProperties(var0, var2, var3);
   }

   public static int FMOD_Channel_GetReverbProperties(SWIGTYPE_p_FMOD_CHANNEL var0, int var1, SWIGTYPE_p_float var2) {
      return javafmodJNI.FMOD_Channel_GetReverbProperties(SWIGTYPE_p_FMOD_CHANNEL.getCPtr(var0), var1, SWIGTYPE_p_float.getCPtr(var2));
   }

   public static int FMOD_Channel_SetLowPassGain(long var0, float var2) {
      return javafmodJNI.FMOD_Channel_SetLowPassGain(var0, var2);
   }

   public static int FMOD_Channel_GetLowPassGain(SWIGTYPE_p_FMOD_CHANNEL var0, SWIGTYPE_p_float var1) {
      return javafmodJNI.FMOD_Channel_GetLowPassGain(SWIGTYPE_p_FMOD_CHANNEL.getCPtr(var0), SWIGTYPE_p_float.getCPtr(var1));
   }

   public static int FMOD_Channel_SetMode(long var0, long var2) {
      return javafmodJNI.FMOD_Channel_SetMode(var0, var2);
   }

   public static int FMOD_Channel_GetMode(SWIGTYPE_p_FMOD_CHANNEL var0, SWIGTYPE_p_FMOD_MODE var1) {
      return javafmodJNI.FMOD_Channel_GetMode(SWIGTYPE_p_FMOD_CHANNEL.getCPtr(var0), SWIGTYPE_p_FMOD_MODE.getCPtr(var1));
   }

   public static int FMOD_Channel_SetCallback(SWIGTYPE_p_FMOD_CHANNEL var0, SWIGTYPE_p_FMOD_CHANNELCONTROL_CALLBACK var1) {
      return javafmodJNI.FMOD_Channel_SetCallback(SWIGTYPE_p_FMOD_CHANNEL.getCPtr(var0), SWIGTYPE_p_FMOD_CHANNELCONTROL_CALLBACK.getCPtr(var1));
   }

   public static boolean FMOD_Channel_IsPlaying(long var0) {
      return javafmodJNI.FMOD_Channel_IsPlaying(var0);
   }

   public static int FMOD_Channel_SetPan(SWIGTYPE_p_FMOD_CHANNEL var0, float var1) {
      return javafmodJNI.FMOD_Channel_SetPan(SWIGTYPE_p_FMOD_CHANNEL.getCPtr(var0), var1);
   }

   public static int FMOD_Channel_SetMixLevelsOutput(SWIGTYPE_p_FMOD_CHANNEL var0, float var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8) {
      return javafmodJNI.FMOD_Channel_SetMixLevelsOutput(SWIGTYPE_p_FMOD_CHANNEL.getCPtr(var0), var1, var2, var3, var4, var5, var6, var7, var8);
   }

   public static int FMOD_Channel_SetMixLevelsInput(SWIGTYPE_p_FMOD_CHANNEL var0, SWIGTYPE_p_float var1, int var2) {
      return javafmodJNI.FMOD_Channel_SetMixLevelsInput(SWIGTYPE_p_FMOD_CHANNEL.getCPtr(var0), SWIGTYPE_p_float.getCPtr(var1), var2);
   }

   public static int FMOD_Channel_SetMixMatrix(SWIGTYPE_p_FMOD_CHANNEL var0, SWIGTYPE_p_float var1, int var2, int var3, int var4) {
      return javafmodJNI.FMOD_Channel_SetMixMatrix(SWIGTYPE_p_FMOD_CHANNEL.getCPtr(var0), SWIGTYPE_p_float.getCPtr(var1), var2, var3, var4);
   }

   public static int FMOD_Channel_GetMixMatrix(SWIGTYPE_p_FMOD_CHANNEL var0, SWIGTYPE_p_float var1, SWIGTYPE_p_int var2, SWIGTYPE_p_int var3, int var4) {
      return javafmodJNI.FMOD_Channel_GetMixMatrix(SWIGTYPE_p_FMOD_CHANNEL.getCPtr(var0), SWIGTYPE_p_float.getCPtr(var1), SWIGTYPE_p_int.getCPtr(var2), SWIGTYPE_p_int.getCPtr(var3), var4);
   }

   public static int FMOD_Channel_GetDSPClock(SWIGTYPE_p_FMOD_CHANNEL var0, SWIGTYPE_p_unsigned_long_long var1, SWIGTYPE_p_unsigned_long_long var2) {
      return javafmodJNI.FMOD_Channel_GetDSPClock(SWIGTYPE_p_FMOD_CHANNEL.getCPtr(var0), SWIGTYPE_p_unsigned_long_long.getCPtr(var1), SWIGTYPE_p_unsigned_long_long.getCPtr(var2));
   }

   public static int FMOD_Channel_SetDelay(SWIGTYPE_p_FMOD_CHANNEL var0, BigInteger var1, BigInteger var2, SWIGTYPE_p_FMOD_BOOL var3) {
      return javafmodJNI.FMOD_Channel_SetDelay(SWIGTYPE_p_FMOD_CHANNEL.getCPtr(var0), var1, var2, SWIGTYPE_p_FMOD_BOOL.getCPtr(var3));
   }

   public static int FMOD_Channel_GetDelay(SWIGTYPE_p_FMOD_CHANNEL var0, SWIGTYPE_p_unsigned_long_long var1, SWIGTYPE_p_unsigned_long_long var2, SWIGTYPE_p_FMOD_BOOL var3) {
      return javafmodJNI.FMOD_Channel_GetDelay(SWIGTYPE_p_FMOD_CHANNEL.getCPtr(var0), SWIGTYPE_p_unsigned_long_long.getCPtr(var1), SWIGTYPE_p_unsigned_long_long.getCPtr(var2), SWIGTYPE_p_FMOD_BOOL.getCPtr(var3));
   }

   public static int FMOD_Channel_AddFadePoint(SWIGTYPE_p_FMOD_CHANNEL var0, BigInteger var1, float var2) {
      return javafmodJNI.FMOD_Channel_AddFadePoint(SWIGTYPE_p_FMOD_CHANNEL.getCPtr(var0), var1, var2);
   }

   public static int FMOD_Channel_RemoveFadePoints(SWIGTYPE_p_FMOD_CHANNEL var0, BigInteger var1, BigInteger var2) {
      return javafmodJNI.FMOD_Channel_RemoveFadePoints(SWIGTYPE_p_FMOD_CHANNEL.getCPtr(var0), var1, var2);
   }

   public static int FMOD_Channel_GetFadePoints(SWIGTYPE_p_FMOD_CHANNEL var0, SWIGTYPE_p_unsigned_int var1, SWIGTYPE_p_unsigned_long_long var2, SWIGTYPE_p_float var3) {
      return javafmodJNI.FMOD_Channel_GetFadePoints(SWIGTYPE_p_FMOD_CHANNEL.getCPtr(var0), SWIGTYPE_p_unsigned_int.getCPtr(var1), SWIGTYPE_p_unsigned_long_long.getCPtr(var2), SWIGTYPE_p_float.getCPtr(var3));
   }

   public static int FMOD_Channel_GetDSP(SWIGTYPE_p_FMOD_CHANNEL var0, int var1, SWIGTYPE_p_p_FMOD_DSP var2) {
      return javafmodJNI.FMOD_Channel_GetDSP(SWIGTYPE_p_FMOD_CHANNEL.getCPtr(var0), var1, SWIGTYPE_p_p_FMOD_DSP.getCPtr(var2));
   }

   public static int FMOD_Channel_AddDSP(SWIGTYPE_p_FMOD_CHANNEL var0, int var1, SWIGTYPE_p_FMOD_DSP var2) {
      return javafmodJNI.FMOD_Channel_AddDSP(SWIGTYPE_p_FMOD_CHANNEL.getCPtr(var0), var1, SWIGTYPE_p_FMOD_DSP.getCPtr(var2));
   }

   public static int FMOD_Channel_RemoveDSP(SWIGTYPE_p_FMOD_CHANNEL var0, SWIGTYPE_p_FMOD_DSP var1) {
      return javafmodJNI.FMOD_Channel_RemoveDSP(SWIGTYPE_p_FMOD_CHANNEL.getCPtr(var0), SWIGTYPE_p_FMOD_DSP.getCPtr(var1));
   }

   public static int FMOD_Channel_GetNumDSPs(SWIGTYPE_p_FMOD_CHANNEL var0, SWIGTYPE_p_int var1) {
      return javafmodJNI.FMOD_Channel_GetNumDSPs(SWIGTYPE_p_FMOD_CHANNEL.getCPtr(var0), SWIGTYPE_p_int.getCPtr(var1));
   }

   public static int FMOD_Channel_SetDSPIndex(SWIGTYPE_p_FMOD_CHANNEL var0, SWIGTYPE_p_FMOD_DSP var1, int var2) {
      return javafmodJNI.FMOD_Channel_SetDSPIndex(SWIGTYPE_p_FMOD_CHANNEL.getCPtr(var0), SWIGTYPE_p_FMOD_DSP.getCPtr(var1), var2);
   }

   public static int FMOD_Channel_GetDSPIndex(SWIGTYPE_p_FMOD_CHANNEL var0, SWIGTYPE_p_FMOD_DSP var1, SWIGTYPE_p_int var2) {
      return javafmodJNI.FMOD_Channel_GetDSPIndex(SWIGTYPE_p_FMOD_CHANNEL.getCPtr(var0), SWIGTYPE_p_FMOD_DSP.getCPtr(var1), SWIGTYPE_p_int.getCPtr(var2));
   }

   public static int FMOD_Channel_OverridePanDSP(SWIGTYPE_p_FMOD_CHANNEL var0, SWIGTYPE_p_FMOD_DSP var1) {
      return javafmodJNI.FMOD_Channel_OverridePanDSP(SWIGTYPE_p_FMOD_CHANNEL.getCPtr(var0), SWIGTYPE_p_FMOD_DSP.getCPtr(var1));
   }

   public static int FMOD_Channel_Set3DAttributes(long var0, float var2, float var3, float var4, float var5, float var6, float var7) {
      return javafmodJNI.FMOD_Channel_Set3DAttributes(var0, var2, var3, var4, var5, var6, var7);
   }

   public static int FMOD_Channel_Get3DAttributes(SWIGTYPE_p_FMOD_CHANNEL var0, SWIGTYPE_p_FMOD_VECTOR var1, SWIGTYPE_p_FMOD_VECTOR var2) {
      return javafmodJNI.FMOD_Channel_Get3DAttributes(SWIGTYPE_p_FMOD_CHANNEL.getCPtr(var0), SWIGTYPE_p_FMOD_VECTOR.getCPtr(var1), SWIGTYPE_p_FMOD_VECTOR.getCPtr(var2));
   }

   public static int FMOD_Channel_Set3DMinMaxDistance(long var0, float var2, float var3) {
      return javafmodJNI.FMOD_Channel_Set3DMinMaxDistance(var0, var2, var3);
   }

   public static int FMOD_Channel_Get3DMinMaxDistance(SWIGTYPE_p_FMOD_CHANNEL var0, SWIGTYPE_p_float var1, SWIGTYPE_p_float var2) {
      return javafmodJNI.FMOD_Channel_Get3DMinMaxDistance(SWIGTYPE_p_FMOD_CHANNEL.getCPtr(var0), SWIGTYPE_p_float.getCPtr(var1), SWIGTYPE_p_float.getCPtr(var2));
   }

   public static int FMOD_Channel_Set3DConeSettings(SWIGTYPE_p_FMOD_CHANNEL var0, float var1, float var2, float var3) {
      return javafmodJNI.FMOD_Channel_Set3DConeSettings(SWIGTYPE_p_FMOD_CHANNEL.getCPtr(var0), var1, var2, var3);
   }

   public static int FMOD_Channel_Get3DConeSettings(SWIGTYPE_p_FMOD_CHANNEL var0, SWIGTYPE_p_float var1, SWIGTYPE_p_float var2, SWIGTYPE_p_float var3) {
      return javafmodJNI.FMOD_Channel_Get3DConeSettings(SWIGTYPE_p_FMOD_CHANNEL.getCPtr(var0), SWIGTYPE_p_float.getCPtr(var1), SWIGTYPE_p_float.getCPtr(var2), SWIGTYPE_p_float.getCPtr(var3));
   }

   public static int FMOD_Channel_Set3DConeOrientation(SWIGTYPE_p_FMOD_CHANNEL var0, SWIGTYPE_p_FMOD_VECTOR var1) {
      return javafmodJNI.FMOD_Channel_Set3DConeOrientation(SWIGTYPE_p_FMOD_CHANNEL.getCPtr(var0), SWIGTYPE_p_FMOD_VECTOR.getCPtr(var1));
   }

   public static int FMOD_Channel_Get3DConeOrientation(SWIGTYPE_p_FMOD_CHANNEL var0, SWIGTYPE_p_FMOD_VECTOR var1) {
      return javafmodJNI.FMOD_Channel_Get3DConeOrientation(SWIGTYPE_p_FMOD_CHANNEL.getCPtr(var0), SWIGTYPE_p_FMOD_VECTOR.getCPtr(var1));
   }

   public static int FMOD_Channel_Set3DCustomRolloff(SWIGTYPE_p_FMOD_CHANNEL var0, SWIGTYPE_p_FMOD_VECTOR var1, int var2) {
      return javafmodJNI.FMOD_Channel_Set3DCustomRolloff(SWIGTYPE_p_FMOD_CHANNEL.getCPtr(var0), SWIGTYPE_p_FMOD_VECTOR.getCPtr(var1), var2);
   }

   public static int FMOD_Channel_Get3DCustomRolloff(SWIGTYPE_p_FMOD_CHANNEL var0, SWIGTYPE_p_p_FMOD_VECTOR var1, SWIGTYPE_p_int var2) {
      return javafmodJNI.FMOD_Channel_Get3DCustomRolloff(SWIGTYPE_p_FMOD_CHANNEL.getCPtr(var0), SWIGTYPE_p_p_FMOD_VECTOR.getCPtr(var1), SWIGTYPE_p_int.getCPtr(var2));
   }

   public static int FMOD_Channel_Set3DOcclusion(long var0, float var2, float var3) {
      return javafmodJNI.FMOD_Channel_Set3DOcclusion(var0, var2, var3);
   }

   public static int FMOD_Channel_Get3DOcclusion(SWIGTYPE_p_FMOD_CHANNEL var0, SWIGTYPE_p_float var1, SWIGTYPE_p_float var2) {
      return javafmodJNI.FMOD_Channel_Get3DOcclusion(SWIGTYPE_p_FMOD_CHANNEL.getCPtr(var0), SWIGTYPE_p_float.getCPtr(var1), SWIGTYPE_p_float.getCPtr(var2));
   }

   public static int FMOD_Channel_Set3DSpread(SWIGTYPE_p_FMOD_CHANNEL var0, float var1) {
      return javafmodJNI.FMOD_Channel_Set3DSpread(SWIGTYPE_p_FMOD_CHANNEL.getCPtr(var0), var1);
   }

   public static int FMOD_Channel_Get3DSpread(SWIGTYPE_p_FMOD_CHANNEL var0, SWIGTYPE_p_float var1) {
      return javafmodJNI.FMOD_Channel_Get3DSpread(SWIGTYPE_p_FMOD_CHANNEL.getCPtr(var0), SWIGTYPE_p_float.getCPtr(var1));
   }

   public static int FMOD_Channel_Set3DLevel(SWIGTYPE_p_FMOD_CHANNEL var0, float var1) {
      return javafmodJNI.FMOD_Channel_Set3DLevel(SWIGTYPE_p_FMOD_CHANNEL.getCPtr(var0), var1);
   }

   public static int FMOD_Channel_Get3DLevel(SWIGTYPE_p_FMOD_CHANNEL var0, SWIGTYPE_p_float var1) {
      return javafmodJNI.FMOD_Channel_Get3DLevel(SWIGTYPE_p_FMOD_CHANNEL.getCPtr(var0), SWIGTYPE_p_float.getCPtr(var1));
   }

   public static int FMOD_Channel_Set3DDopplerLevel(SWIGTYPE_p_FMOD_CHANNEL var0, float var1) {
      return javafmodJNI.FMOD_Channel_Set3DDopplerLevel(SWIGTYPE_p_FMOD_CHANNEL.getCPtr(var0), var1);
   }

   public static int FMOD_Channel_Get3DDopplerLevel(SWIGTYPE_p_FMOD_CHANNEL var0, SWIGTYPE_p_float var1) {
      return javafmodJNI.FMOD_Channel_Get3DDopplerLevel(SWIGTYPE_p_FMOD_CHANNEL.getCPtr(var0), SWIGTYPE_p_float.getCPtr(var1));
   }

   public static int FMOD_Channel_Set3DDistanceFilter(SWIGTYPE_p_FMOD_CHANNEL var0, SWIGTYPE_p_FMOD_BOOL var1, float var2, float var3) {
      return javafmodJNI.FMOD_Channel_Set3DDistanceFilter(SWIGTYPE_p_FMOD_CHANNEL.getCPtr(var0), SWIGTYPE_p_FMOD_BOOL.getCPtr(var1), var2, var3);
   }

   public static int FMOD_Channel_Get3DDistanceFilter(SWIGTYPE_p_FMOD_CHANNEL var0, SWIGTYPE_p_FMOD_BOOL var1, SWIGTYPE_p_float var2, SWIGTYPE_p_float var3) {
      return javafmodJNI.FMOD_Channel_Get3DDistanceFilter(SWIGTYPE_p_FMOD_CHANNEL.getCPtr(var0), SWIGTYPE_p_FMOD_BOOL.getCPtr(var1), SWIGTYPE_p_float.getCPtr(var2), SWIGTYPE_p_float.getCPtr(var3));
   }

   public static int FMOD_Channel_SetUserData(SWIGTYPE_p_FMOD_CHANNEL var0, SWIGTYPE_p_void var1) {
      return javafmodJNI.FMOD_Channel_SetUserData(SWIGTYPE_p_FMOD_CHANNEL.getCPtr(var0), SWIGTYPE_p_void.getCPtr(var1));
   }

   public static int FMOD_Channel_GetUserData(SWIGTYPE_p_FMOD_CHANNEL var0, SWIGTYPE_p_p_void var1) {
      return javafmodJNI.FMOD_Channel_GetUserData(SWIGTYPE_p_FMOD_CHANNEL.getCPtr(var0), SWIGTYPE_p_p_void.getCPtr(var1));
   }

   public static int FMOD_Channel_SetFrequency(SWIGTYPE_p_FMOD_CHANNEL var0, float var1) {
      return javafmodJNI.FMOD_Channel_SetFrequency(SWIGTYPE_p_FMOD_CHANNEL.getCPtr(var0), var1);
   }

   public static int FMOD_Channel_GetFrequency(SWIGTYPE_p_FMOD_CHANNEL var0, SWIGTYPE_p_float var1) {
      return javafmodJNI.FMOD_Channel_GetFrequency(SWIGTYPE_p_FMOD_CHANNEL.getCPtr(var0), SWIGTYPE_p_float.getCPtr(var1));
   }

   public static int FMOD_Channel_SetPriority(long var0, int var2) {
      return javafmodJNI.FMOD_Channel_SetPriority(var0, var2);
   }

   public static int FMOD_Channel_GetPriority(SWIGTYPE_p_FMOD_CHANNEL var0, SWIGTYPE_p_int var1) {
      return javafmodJNI.FMOD_Channel_GetPriority(SWIGTYPE_p_FMOD_CHANNEL.getCPtr(var0), SWIGTYPE_p_int.getCPtr(var1));
   }

   public static int FMOD_Channel_SetPosition(long var0, long var2) {
      return javafmodJNI.FMOD_Channel_SetPosition(var0, var2);
   }

   public static long FMOD_Channel_GetPosition(long var0, int var2) {
      return javafmodJNI.FMOD_Channel_GetPosition(var0, (long)var2);
   }

   public static int FMOD_Channel_SetChannelGroup(long var0, long var2) {
      return javafmodJNI.FMOD_Channel_SetChannelGroup(var0, var2);
   }

   public static int FMOD_Channel_GetChannelGroup(SWIGTYPE_p_FMOD_CHANNEL var0, SWIGTYPE_p_p_FMOD_CHANNELGROUP var1) {
      return javafmodJNI.FMOD_Channel_GetChannelGroup(SWIGTYPE_p_FMOD_CHANNEL.getCPtr(var0), SWIGTYPE_p_p_FMOD_CHANNELGROUP.getCPtr(var1));
   }

   public static int FMOD_Channel_SetLoopCount(long var0, int var2) {
      return javafmodJNI.FMOD_Channel_SetLoopCount(var0, var2);
   }

   public static int FMOD_Channel_GetLoopCount(SWIGTYPE_p_FMOD_CHANNEL var0, SWIGTYPE_p_int var1) {
      return javafmodJNI.FMOD_Channel_GetLoopCount(SWIGTYPE_p_FMOD_CHANNEL.getCPtr(var0), SWIGTYPE_p_int.getCPtr(var1));
   }

   public static int FMOD_Channel_SetLoopPoints(SWIGTYPE_p_FMOD_CHANNEL var0, long var1, SWIGTYPE_p_FMOD_TIMEUNIT var3, long var4, SWIGTYPE_p_FMOD_TIMEUNIT var6) {
      return javafmodJNI.FMOD_Channel_SetLoopPoints(SWIGTYPE_p_FMOD_CHANNEL.getCPtr(var0), var1, SWIGTYPE_p_FMOD_TIMEUNIT.getCPtr(var3), var4, SWIGTYPE_p_FMOD_TIMEUNIT.getCPtr(var6));
   }

   public static int FMOD_Channel_GetLoopPoints(SWIGTYPE_p_FMOD_CHANNEL var0, SWIGTYPE_p_unsigned_int var1, SWIGTYPE_p_FMOD_TIMEUNIT var2, SWIGTYPE_p_unsigned_int var3, SWIGTYPE_p_FMOD_TIMEUNIT var4) {
      return javafmodJNI.FMOD_Channel_GetLoopPoints(SWIGTYPE_p_FMOD_CHANNEL.getCPtr(var0), SWIGTYPE_p_unsigned_int.getCPtr(var1), SWIGTYPE_p_FMOD_TIMEUNIT.getCPtr(var2), SWIGTYPE_p_unsigned_int.getCPtr(var3), SWIGTYPE_p_FMOD_TIMEUNIT.getCPtr(var4));
   }

   public static boolean FMOD_Channel_IsVirtual(long var0) {
      return javafmodJNI.FMOD_Channel_IsVirtual(var0);
   }

   public static int FMOD_Channel_GetCurrentSound(SWIGTYPE_p_FMOD_CHANNEL var0, SWIGTYPE_p_p_FMOD_SOUND var1) {
      return javafmodJNI.FMOD_Channel_GetCurrentSound(SWIGTYPE_p_FMOD_CHANNEL.getCPtr(var0), SWIGTYPE_p_p_FMOD_SOUND.getCPtr(var1));
   }

   public static int FMOD_Channel_GetIndex(SWIGTYPE_p_FMOD_CHANNEL var0, SWIGTYPE_p_int var1) {
      return javafmodJNI.FMOD_Channel_GetIndex(SWIGTYPE_p_FMOD_CHANNEL.getCPtr(var0), SWIGTYPE_p_int.getCPtr(var1));
   }

   public static int FMOD_ChannelGroup_GetSystemObject(SWIGTYPE_p_FMOD_CHANNELGROUP var0, SWIGTYPE_p_p_FMOD_SYSTEM var1) {
      return javafmodJNI.FMOD_ChannelGroup_GetSystemObject(SWIGTYPE_p_FMOD_CHANNELGROUP.getCPtr(var0), SWIGTYPE_p_p_FMOD_SYSTEM.getCPtr(var1));
   }

   public static int FMOD_ChannelGroup_Stop(long var0) {
      return javafmodJNI.FMOD_ChannelGroup_Stop(var0);
   }

   public static int FMOD_ChannelGroup_SetPaused(long var0, boolean var2) {
      return javafmodJNI.FMOD_ChannelGroup_SetPaused(var0, var2);
   }

   public static int FMOD_ChannelGroup_GetPaused(SWIGTYPE_p_FMOD_CHANNELGROUP var0, SWIGTYPE_p_FMOD_BOOL var1) {
      return javafmodJNI.FMOD_ChannelGroup_GetPaused(SWIGTYPE_p_FMOD_CHANNELGROUP.getCPtr(var0), SWIGTYPE_p_FMOD_BOOL.getCPtr(var1));
   }

   public static int FMOD_ChannelGroup_SetVolume(long var0, float var2) {
      return javafmodJNI.FMOD_ChannelGroup_SetVolume(var0, var2);
   }

   public static int FMOD_ChannelGroup_GetVolume(SWIGTYPE_p_FMOD_CHANNELGROUP var0, SWIGTYPE_p_float var1) {
      return javafmodJNI.FMOD_ChannelGroup_GetVolume(SWIGTYPE_p_FMOD_CHANNELGROUP.getCPtr(var0), SWIGTYPE_p_float.getCPtr(var1));
   }

   public static int FMOD_ChannelGroup_SetVolumeRamp(SWIGTYPE_p_FMOD_CHANNELGROUP var0, SWIGTYPE_p_FMOD_BOOL var1) {
      return javafmodJNI.FMOD_ChannelGroup_SetVolumeRamp(SWIGTYPE_p_FMOD_CHANNELGROUP.getCPtr(var0), SWIGTYPE_p_FMOD_BOOL.getCPtr(var1));
   }

   public static int FMOD_ChannelGroup_GetVolumeRamp(SWIGTYPE_p_FMOD_CHANNELGROUP var0, SWIGTYPE_p_FMOD_BOOL var1) {
      return javafmodJNI.FMOD_ChannelGroup_GetVolumeRamp(SWIGTYPE_p_FMOD_CHANNELGROUP.getCPtr(var0), SWIGTYPE_p_FMOD_BOOL.getCPtr(var1));
   }

   public static int FMOD_ChannelGroup_GetAudibility(SWIGTYPE_p_FMOD_CHANNELGROUP var0, SWIGTYPE_p_float var1) {
      return javafmodJNI.FMOD_ChannelGroup_GetAudibility(SWIGTYPE_p_FMOD_CHANNELGROUP.getCPtr(var0), SWIGTYPE_p_float.getCPtr(var1));
   }

   public static int FMOD_ChannelGroup_SetPitch(SWIGTYPE_p_FMOD_CHANNELGROUP var0, float var1) {
      return javafmodJNI.FMOD_ChannelGroup_SetPitch(SWIGTYPE_p_FMOD_CHANNELGROUP.getCPtr(var0), var1);
   }

   public static int FMOD_ChannelGroup_GetPitch(SWIGTYPE_p_FMOD_CHANNELGROUP var0, SWIGTYPE_p_float var1) {
      return javafmodJNI.FMOD_ChannelGroup_GetPitch(SWIGTYPE_p_FMOD_CHANNELGROUP.getCPtr(var0), SWIGTYPE_p_float.getCPtr(var1));
   }

   public static int FMOD_ChannelGroup_SetMute(long var0, boolean var2) {
      return javafmodJNI.FMOD_ChannelGroup_SetMute(var0, var2);
   }

   public static int FMOD_ChannelGroup_GetMute(SWIGTYPE_p_FMOD_CHANNELGROUP var0, SWIGTYPE_p_FMOD_BOOL var1) {
      return javafmodJNI.FMOD_ChannelGroup_GetMute(SWIGTYPE_p_FMOD_CHANNELGROUP.getCPtr(var0), SWIGTYPE_p_FMOD_BOOL.getCPtr(var1));
   }

   public static int FMOD_ChannelGroup_SetReverbProperties(SWIGTYPE_p_FMOD_CHANNELGROUP var0, int var1, float var2) {
      return javafmodJNI.FMOD_ChannelGroup_SetReverbProperties(SWIGTYPE_p_FMOD_CHANNELGROUP.getCPtr(var0), var1, var2);
   }

   public static int FMOD_ChannelGroup_GetReverbProperties(SWIGTYPE_p_FMOD_CHANNELGROUP var0, int var1, SWIGTYPE_p_float var2) {
      return javafmodJNI.FMOD_ChannelGroup_GetReverbProperties(SWIGTYPE_p_FMOD_CHANNELGROUP.getCPtr(var0), var1, SWIGTYPE_p_float.getCPtr(var2));
   }

   public static int FMOD_ChannelGroup_SetLowPassGain(SWIGTYPE_p_FMOD_CHANNELGROUP var0, float var1) {
      return javafmodJNI.FMOD_ChannelGroup_SetLowPassGain(SWIGTYPE_p_FMOD_CHANNELGROUP.getCPtr(var0), var1);
   }

   public static int FMOD_ChannelGroup_GetLowPassGain(SWIGTYPE_p_FMOD_CHANNELGROUP var0, SWIGTYPE_p_float var1) {
      return javafmodJNI.FMOD_ChannelGroup_GetLowPassGain(SWIGTYPE_p_FMOD_CHANNELGROUP.getCPtr(var0), SWIGTYPE_p_float.getCPtr(var1));
   }

   public static int FMOD_ChannelGroup_SetMode(SWIGTYPE_p_FMOD_CHANNELGROUP var0, SWIGTYPE_p_FMOD_MODE var1) {
      return javafmodJNI.FMOD_ChannelGroup_SetMode(SWIGTYPE_p_FMOD_CHANNELGROUP.getCPtr(var0), SWIGTYPE_p_FMOD_MODE.getCPtr(var1));
   }

   public static int FMOD_ChannelGroup_GetMode(SWIGTYPE_p_FMOD_CHANNELGROUP var0, SWIGTYPE_p_FMOD_MODE var1) {
      return javafmodJNI.FMOD_ChannelGroup_GetMode(SWIGTYPE_p_FMOD_CHANNELGROUP.getCPtr(var0), SWIGTYPE_p_FMOD_MODE.getCPtr(var1));
   }

   public static int FMOD_ChannelGroup_SetCallback(SWIGTYPE_p_FMOD_CHANNELGROUP var0, SWIGTYPE_p_FMOD_CHANNELCONTROL_CALLBACK var1) {
      return javafmodJNI.FMOD_ChannelGroup_SetCallback(SWIGTYPE_p_FMOD_CHANNELGROUP.getCPtr(var0), SWIGTYPE_p_FMOD_CHANNELCONTROL_CALLBACK.getCPtr(var1));
   }

   public static int FMOD_ChannelGroup_IsPlaying(SWIGTYPE_p_FMOD_CHANNELGROUP var0, SWIGTYPE_p_FMOD_BOOL var1) {
      return javafmodJNI.FMOD_ChannelGroup_IsPlaying(SWIGTYPE_p_FMOD_CHANNELGROUP.getCPtr(var0), SWIGTYPE_p_FMOD_BOOL.getCPtr(var1));
   }

   public static int FMOD_ChannelGroup_SetPan(SWIGTYPE_p_FMOD_CHANNELGROUP var0, float var1) {
      return javafmodJNI.FMOD_ChannelGroup_SetPan(SWIGTYPE_p_FMOD_CHANNELGROUP.getCPtr(var0), var1);
   }

   public static int FMOD_ChannelGroup_SetMixLevelsOutput(SWIGTYPE_p_FMOD_CHANNELGROUP var0, float var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8) {
      return javafmodJNI.FMOD_ChannelGroup_SetMixLevelsOutput(SWIGTYPE_p_FMOD_CHANNELGROUP.getCPtr(var0), var1, var2, var3, var4, var5, var6, var7, var8);
   }

   public static int FMOD_ChannelGroup_SetMixLevelsInput(SWIGTYPE_p_FMOD_CHANNELGROUP var0, SWIGTYPE_p_float var1, int var2) {
      return javafmodJNI.FMOD_ChannelGroup_SetMixLevelsInput(SWIGTYPE_p_FMOD_CHANNELGROUP.getCPtr(var0), SWIGTYPE_p_float.getCPtr(var1), var2);
   }

   public static int FMOD_ChannelGroup_SetMixMatrix(SWIGTYPE_p_FMOD_CHANNELGROUP var0, SWIGTYPE_p_float var1, int var2, int var3, int var4) {
      return javafmodJNI.FMOD_ChannelGroup_SetMixMatrix(SWIGTYPE_p_FMOD_CHANNELGROUP.getCPtr(var0), SWIGTYPE_p_float.getCPtr(var1), var2, var3, var4);
   }

   public static int FMOD_ChannelGroup_GetMixMatrix(SWIGTYPE_p_FMOD_CHANNELGROUP var0, SWIGTYPE_p_float var1, SWIGTYPE_p_int var2, SWIGTYPE_p_int var3, int var4) {
      return javafmodJNI.FMOD_ChannelGroup_GetMixMatrix(SWIGTYPE_p_FMOD_CHANNELGROUP.getCPtr(var0), SWIGTYPE_p_float.getCPtr(var1), SWIGTYPE_p_int.getCPtr(var2), SWIGTYPE_p_int.getCPtr(var3), var4);
   }

   public static int FMOD_ChannelGroup_GetDSPClock(SWIGTYPE_p_FMOD_CHANNELGROUP var0, SWIGTYPE_p_unsigned_long_long var1, SWIGTYPE_p_unsigned_long_long var2) {
      return javafmodJNI.FMOD_ChannelGroup_GetDSPClock(SWIGTYPE_p_FMOD_CHANNELGROUP.getCPtr(var0), SWIGTYPE_p_unsigned_long_long.getCPtr(var1), SWIGTYPE_p_unsigned_long_long.getCPtr(var2));
   }

   public static int FMOD_ChannelGroup_SetDelay(SWIGTYPE_p_FMOD_CHANNELGROUP var0, BigInteger var1, BigInteger var2, SWIGTYPE_p_FMOD_BOOL var3) {
      return javafmodJNI.FMOD_ChannelGroup_SetDelay(SWIGTYPE_p_FMOD_CHANNELGROUP.getCPtr(var0), var1, var2, SWIGTYPE_p_FMOD_BOOL.getCPtr(var3));
   }

   public static int FMOD_ChannelGroup_GetDelay(SWIGTYPE_p_FMOD_CHANNELGROUP var0, SWIGTYPE_p_unsigned_long_long var1, SWIGTYPE_p_unsigned_long_long var2, SWIGTYPE_p_FMOD_BOOL var3) {
      return javafmodJNI.FMOD_ChannelGroup_GetDelay(SWIGTYPE_p_FMOD_CHANNELGROUP.getCPtr(var0), SWIGTYPE_p_unsigned_long_long.getCPtr(var1), SWIGTYPE_p_unsigned_long_long.getCPtr(var2), SWIGTYPE_p_FMOD_BOOL.getCPtr(var3));
   }

   public static int FMOD_ChannelGroup_AddFadePoint(SWIGTYPE_p_FMOD_CHANNELGROUP var0, BigInteger var1, float var2) {
      return javafmodJNI.FMOD_ChannelGroup_AddFadePoint(SWIGTYPE_p_FMOD_CHANNELGROUP.getCPtr(var0), var1, var2);
   }

   public static int FMOD_ChannelGroup_RemoveFadePoints(SWIGTYPE_p_FMOD_CHANNELGROUP var0, BigInteger var1, BigInteger var2) {
      return javafmodJNI.FMOD_ChannelGroup_RemoveFadePoints(SWIGTYPE_p_FMOD_CHANNELGROUP.getCPtr(var0), var1, var2);
   }

   public static int FMOD_ChannelGroup_GetFadePoints(SWIGTYPE_p_FMOD_CHANNELGROUP var0, SWIGTYPE_p_unsigned_int var1, SWIGTYPE_p_unsigned_long_long var2, SWIGTYPE_p_float var3) {
      return javafmodJNI.FMOD_ChannelGroup_GetFadePoints(SWIGTYPE_p_FMOD_CHANNELGROUP.getCPtr(var0), SWIGTYPE_p_unsigned_int.getCPtr(var1), SWIGTYPE_p_unsigned_long_long.getCPtr(var2), SWIGTYPE_p_float.getCPtr(var3));
   }

   public static int FMOD_ChannelGroup_GetDSP(SWIGTYPE_p_FMOD_CHANNELGROUP var0, int var1, SWIGTYPE_p_p_FMOD_DSP var2) {
      return javafmodJNI.FMOD_ChannelGroup_GetDSP(SWIGTYPE_p_FMOD_CHANNELGROUP.getCPtr(var0), var1, SWIGTYPE_p_p_FMOD_DSP.getCPtr(var2));
   }

   public static int FMOD_ChannelGroup_AddDSP(SWIGTYPE_p_FMOD_CHANNELGROUP var0, int var1, SWIGTYPE_p_FMOD_DSP var2) {
      return javafmodJNI.FMOD_ChannelGroup_AddDSP(SWIGTYPE_p_FMOD_CHANNELGROUP.getCPtr(var0), var1, SWIGTYPE_p_FMOD_DSP.getCPtr(var2));
   }

   public static int FMOD_ChannelGroup_RemoveDSP(SWIGTYPE_p_FMOD_CHANNELGROUP var0, SWIGTYPE_p_FMOD_DSP var1) {
      return javafmodJNI.FMOD_ChannelGroup_RemoveDSP(SWIGTYPE_p_FMOD_CHANNELGROUP.getCPtr(var0), SWIGTYPE_p_FMOD_DSP.getCPtr(var1));
   }

   public static int FMOD_ChannelGroup_GetNumDSPs(SWIGTYPE_p_FMOD_CHANNELGROUP var0, SWIGTYPE_p_int var1) {
      return javafmodJNI.FMOD_ChannelGroup_GetNumDSPs(SWIGTYPE_p_FMOD_CHANNELGROUP.getCPtr(var0), SWIGTYPE_p_int.getCPtr(var1));
   }

   public static int FMOD_ChannelGroup_SetDSPIndex(SWIGTYPE_p_FMOD_CHANNELGROUP var0, SWIGTYPE_p_FMOD_DSP var1, int var2) {
      return javafmodJNI.FMOD_ChannelGroup_SetDSPIndex(SWIGTYPE_p_FMOD_CHANNELGROUP.getCPtr(var0), SWIGTYPE_p_FMOD_DSP.getCPtr(var1), var2);
   }

   public static int FMOD_ChannelGroup_GetDSPIndex(SWIGTYPE_p_FMOD_CHANNELGROUP var0, SWIGTYPE_p_FMOD_DSP var1, SWIGTYPE_p_int var2) {
      return javafmodJNI.FMOD_ChannelGroup_GetDSPIndex(SWIGTYPE_p_FMOD_CHANNELGROUP.getCPtr(var0), SWIGTYPE_p_FMOD_DSP.getCPtr(var1), SWIGTYPE_p_int.getCPtr(var2));
   }

   public static int FMOD_ChannelGroup_OverridePanDSP(SWIGTYPE_p_FMOD_CHANNELGROUP var0, SWIGTYPE_p_FMOD_DSP var1) {
      return javafmodJNI.FMOD_ChannelGroup_OverridePanDSP(SWIGTYPE_p_FMOD_CHANNELGROUP.getCPtr(var0), SWIGTYPE_p_FMOD_DSP.getCPtr(var1));
   }

   public static int FMOD_ChannelGroup_Set3DAttributes(SWIGTYPE_p_FMOD_CHANNELGROUP var0, SWIGTYPE_p_FMOD_VECTOR var1, SWIGTYPE_p_FMOD_VECTOR var2) {
      return javafmodJNI.FMOD_ChannelGroup_Set3DAttributes(SWIGTYPE_p_FMOD_CHANNELGROUP.getCPtr(var0), SWIGTYPE_p_FMOD_VECTOR.getCPtr(var1), SWIGTYPE_p_FMOD_VECTOR.getCPtr(var2));
   }

   public static int FMOD_ChannelGroup_Get3DAttributes(SWIGTYPE_p_FMOD_CHANNELGROUP var0, SWIGTYPE_p_FMOD_VECTOR var1, SWIGTYPE_p_FMOD_VECTOR var2) {
      return javafmodJNI.FMOD_ChannelGroup_Get3DAttributes(SWIGTYPE_p_FMOD_CHANNELGROUP.getCPtr(var0), SWIGTYPE_p_FMOD_VECTOR.getCPtr(var1), SWIGTYPE_p_FMOD_VECTOR.getCPtr(var2));
   }

   public static int FMOD_ChannelGroup_Set3DMinMaxDistance(SWIGTYPE_p_FMOD_CHANNELGROUP var0, float var1, float var2) {
      return javafmodJNI.FMOD_ChannelGroup_Set3DMinMaxDistance(SWIGTYPE_p_FMOD_CHANNELGROUP.getCPtr(var0), var1, var2);
   }

   public static int FMOD_ChannelGroup_Get3DMinMaxDistance(SWIGTYPE_p_FMOD_CHANNELGROUP var0, SWIGTYPE_p_float var1, SWIGTYPE_p_float var2) {
      return javafmodJNI.FMOD_ChannelGroup_Get3DMinMaxDistance(SWIGTYPE_p_FMOD_CHANNELGROUP.getCPtr(var0), SWIGTYPE_p_float.getCPtr(var1), SWIGTYPE_p_float.getCPtr(var2));
   }

   public static int FMOD_ChannelGroup_Set3DConeSettings(SWIGTYPE_p_FMOD_CHANNELGROUP var0, float var1, float var2, float var3) {
      return javafmodJNI.FMOD_ChannelGroup_Set3DConeSettings(SWIGTYPE_p_FMOD_CHANNELGROUP.getCPtr(var0), var1, var2, var3);
   }

   public static int FMOD_ChannelGroup_Get3DConeSettings(SWIGTYPE_p_FMOD_CHANNELGROUP var0, SWIGTYPE_p_float var1, SWIGTYPE_p_float var2, SWIGTYPE_p_float var3) {
      return javafmodJNI.FMOD_ChannelGroup_Get3DConeSettings(SWIGTYPE_p_FMOD_CHANNELGROUP.getCPtr(var0), SWIGTYPE_p_float.getCPtr(var1), SWIGTYPE_p_float.getCPtr(var2), SWIGTYPE_p_float.getCPtr(var3));
   }

   public static int FMOD_ChannelGroup_Set3DConeOrientation(SWIGTYPE_p_FMOD_CHANNELGROUP var0, SWIGTYPE_p_FMOD_VECTOR var1) {
      return javafmodJNI.FMOD_ChannelGroup_Set3DConeOrientation(SWIGTYPE_p_FMOD_CHANNELGROUP.getCPtr(var0), SWIGTYPE_p_FMOD_VECTOR.getCPtr(var1));
   }

   public static int FMOD_ChannelGroup_Get3DConeOrientation(SWIGTYPE_p_FMOD_CHANNELGROUP var0, SWIGTYPE_p_FMOD_VECTOR var1) {
      return javafmodJNI.FMOD_ChannelGroup_Get3DConeOrientation(SWIGTYPE_p_FMOD_CHANNELGROUP.getCPtr(var0), SWIGTYPE_p_FMOD_VECTOR.getCPtr(var1));
   }

   public static int FMOD_ChannelGroup_Set3DCustomRolloff(SWIGTYPE_p_FMOD_CHANNELGROUP var0, SWIGTYPE_p_FMOD_VECTOR var1, int var2) {
      return javafmodJNI.FMOD_ChannelGroup_Set3DCustomRolloff(SWIGTYPE_p_FMOD_CHANNELGROUP.getCPtr(var0), SWIGTYPE_p_FMOD_VECTOR.getCPtr(var1), var2);
   }

   public static int FMOD_ChannelGroup_Get3DCustomRolloff(SWIGTYPE_p_FMOD_CHANNELGROUP var0, SWIGTYPE_p_p_FMOD_VECTOR var1, SWIGTYPE_p_int var2) {
      return javafmodJNI.FMOD_ChannelGroup_Get3DCustomRolloff(SWIGTYPE_p_FMOD_CHANNELGROUP.getCPtr(var0), SWIGTYPE_p_p_FMOD_VECTOR.getCPtr(var1), SWIGTYPE_p_int.getCPtr(var2));
   }

   public static int FMOD_ChannelGroup_Set3DOcclusion(SWIGTYPE_p_FMOD_CHANNELGROUP var0, float var1, float var2) {
      return javafmodJNI.FMOD_ChannelGroup_Set3DOcclusion(SWIGTYPE_p_FMOD_CHANNELGROUP.getCPtr(var0), var1, var2);
   }

   public static int FMOD_ChannelGroup_Get3DOcclusion(SWIGTYPE_p_FMOD_CHANNELGROUP var0, SWIGTYPE_p_float var1, SWIGTYPE_p_float var2) {
      return javafmodJNI.FMOD_ChannelGroup_Get3DOcclusion(SWIGTYPE_p_FMOD_CHANNELGROUP.getCPtr(var0), SWIGTYPE_p_float.getCPtr(var1), SWIGTYPE_p_float.getCPtr(var2));
   }

   public static int FMOD_ChannelGroup_Set3DSpread(SWIGTYPE_p_FMOD_CHANNELGROUP var0, float var1) {
      return javafmodJNI.FMOD_ChannelGroup_Set3DSpread(SWIGTYPE_p_FMOD_CHANNELGROUP.getCPtr(var0), var1);
   }

   public static int FMOD_ChannelGroup_Get3DSpread(SWIGTYPE_p_FMOD_CHANNELGROUP var0, SWIGTYPE_p_float var1) {
      return javafmodJNI.FMOD_ChannelGroup_Get3DSpread(SWIGTYPE_p_FMOD_CHANNELGROUP.getCPtr(var0), SWIGTYPE_p_float.getCPtr(var1));
   }

   public static int FMOD_ChannelGroup_Set3DLevel(SWIGTYPE_p_FMOD_CHANNELGROUP var0, float var1) {
      return javafmodJNI.FMOD_ChannelGroup_Set3DLevel(SWIGTYPE_p_FMOD_CHANNELGROUP.getCPtr(var0), var1);
   }

   public static int FMOD_ChannelGroup_Get3DLevel(SWIGTYPE_p_FMOD_CHANNELGROUP var0, SWIGTYPE_p_float var1) {
      return javafmodJNI.FMOD_ChannelGroup_Get3DLevel(SWIGTYPE_p_FMOD_CHANNELGROUP.getCPtr(var0), SWIGTYPE_p_float.getCPtr(var1));
   }

   public static int FMOD_ChannelGroup_Set3DDopplerLevel(SWIGTYPE_p_FMOD_CHANNELGROUP var0, float var1) {
      return javafmodJNI.FMOD_ChannelGroup_Set3DDopplerLevel(SWIGTYPE_p_FMOD_CHANNELGROUP.getCPtr(var0), var1);
   }

   public static int FMOD_ChannelGroup_Get3DDopplerLevel(SWIGTYPE_p_FMOD_CHANNELGROUP var0, SWIGTYPE_p_float var1) {
      return javafmodJNI.FMOD_ChannelGroup_Get3DDopplerLevel(SWIGTYPE_p_FMOD_CHANNELGROUP.getCPtr(var0), SWIGTYPE_p_float.getCPtr(var1));
   }

   public static int FMOD_ChannelGroup_Set3DDistanceFilter(SWIGTYPE_p_FMOD_CHANNELGROUP var0, SWIGTYPE_p_FMOD_BOOL var1, float var2, float var3) {
      return javafmodJNI.FMOD_ChannelGroup_Set3DDistanceFilter(SWIGTYPE_p_FMOD_CHANNELGROUP.getCPtr(var0), SWIGTYPE_p_FMOD_BOOL.getCPtr(var1), var2, var3);
   }

   public static int FMOD_ChannelGroup_Get3DDistanceFilter(SWIGTYPE_p_FMOD_CHANNELGROUP var0, SWIGTYPE_p_FMOD_BOOL var1, SWIGTYPE_p_float var2, SWIGTYPE_p_float var3) {
      return javafmodJNI.FMOD_ChannelGroup_Get3DDistanceFilter(SWIGTYPE_p_FMOD_CHANNELGROUP.getCPtr(var0), SWIGTYPE_p_FMOD_BOOL.getCPtr(var1), SWIGTYPE_p_float.getCPtr(var2), SWIGTYPE_p_float.getCPtr(var3));
   }

   public static int FMOD_ChannelGroup_SetUserData(SWIGTYPE_p_FMOD_CHANNELGROUP var0, SWIGTYPE_p_void var1) {
      return javafmodJNI.FMOD_ChannelGroup_SetUserData(SWIGTYPE_p_FMOD_CHANNELGROUP.getCPtr(var0), SWIGTYPE_p_void.getCPtr(var1));
   }

   public static int FMOD_ChannelGroup_GetUserData(SWIGTYPE_p_FMOD_CHANNELGROUP var0, SWIGTYPE_p_p_void var1) {
      return javafmodJNI.FMOD_ChannelGroup_GetUserData(SWIGTYPE_p_FMOD_CHANNELGROUP.getCPtr(var0), SWIGTYPE_p_p_void.getCPtr(var1));
   }

   public static int FMOD_ChannelGroup_Release(SWIGTYPE_p_FMOD_CHANNELGROUP var0) {
      return javafmodJNI.FMOD_ChannelGroup_Release(SWIGTYPE_p_FMOD_CHANNELGROUP.getCPtr(var0));
   }

   public static int FMOD_ChannelGroup_AddGroup(SWIGTYPE_p_FMOD_CHANNELGROUP var0, SWIGTYPE_p_FMOD_CHANNELGROUP var1, SWIGTYPE_p_FMOD_BOOL var2, SWIGTYPE_p_p_FMOD_DSPCONNECTION var3) {
      return javafmodJNI.FMOD_ChannelGroup_AddGroup(SWIGTYPE_p_FMOD_CHANNELGROUP.getCPtr(var0), SWIGTYPE_p_FMOD_CHANNELGROUP.getCPtr(var1), SWIGTYPE_p_FMOD_BOOL.getCPtr(var2), SWIGTYPE_p_p_FMOD_DSPCONNECTION.getCPtr(var3));
   }

   public static int FMOD_ChannelGroup_GetNumGroups(SWIGTYPE_p_FMOD_CHANNELGROUP var0, SWIGTYPE_p_int var1) {
      return javafmodJNI.FMOD_ChannelGroup_GetNumGroups(SWIGTYPE_p_FMOD_CHANNELGROUP.getCPtr(var0), SWIGTYPE_p_int.getCPtr(var1));
   }

   public static int FMOD_ChannelGroup_GetGroup(SWIGTYPE_p_FMOD_CHANNELGROUP var0, int var1, SWIGTYPE_p_p_FMOD_CHANNELGROUP var2) {
      return javafmodJNI.FMOD_ChannelGroup_GetGroup(SWIGTYPE_p_FMOD_CHANNELGROUP.getCPtr(var0), var1, SWIGTYPE_p_p_FMOD_CHANNELGROUP.getCPtr(var2));
   }

   public static int FMOD_ChannelGroup_GetParentGroup(SWIGTYPE_p_FMOD_CHANNELGROUP var0, SWIGTYPE_p_p_FMOD_CHANNELGROUP var1) {
      return javafmodJNI.FMOD_ChannelGroup_GetParentGroup(SWIGTYPE_p_FMOD_CHANNELGROUP.getCPtr(var0), SWIGTYPE_p_p_FMOD_CHANNELGROUP.getCPtr(var1));
   }

   public static int FMOD_ChannelGroup_GetName(SWIGTYPE_p_FMOD_CHANNELGROUP var0, String var1, int var2) {
      return javafmodJNI.FMOD_ChannelGroup_GetName(SWIGTYPE_p_FMOD_CHANNELGROUP.getCPtr(var0), var1, var2);
   }

   public static int FMOD_ChannelGroup_GetNumChannels(SWIGTYPE_p_FMOD_CHANNELGROUP var0, SWIGTYPE_p_int var1) {
      return javafmodJNI.FMOD_ChannelGroup_GetNumChannels(SWIGTYPE_p_FMOD_CHANNELGROUP.getCPtr(var0), SWIGTYPE_p_int.getCPtr(var1));
   }

   public static int FMOD_ChannelGroup_GetChannel(SWIGTYPE_p_FMOD_CHANNELGROUP var0, int var1, SWIGTYPE_p_p_FMOD_CHANNEL var2) {
      return javafmodJNI.FMOD_ChannelGroup_GetChannel(SWIGTYPE_p_FMOD_CHANNELGROUP.getCPtr(var0), var1, SWIGTYPE_p_p_FMOD_CHANNEL.getCPtr(var2));
   }

   public static int FMOD_SoundGroup_Release(SWIGTYPE_p_FMOD_SOUNDGROUP var0) {
      return javafmodJNI.FMOD_SoundGroup_Release(SWIGTYPE_p_FMOD_SOUNDGROUP.getCPtr(var0));
   }

   public static int FMOD_SoundGroup_GetSystemObject(SWIGTYPE_p_FMOD_SOUNDGROUP var0, SWIGTYPE_p_p_FMOD_SYSTEM var1) {
      return javafmodJNI.FMOD_SoundGroup_GetSystemObject(SWIGTYPE_p_FMOD_SOUNDGROUP.getCPtr(var0), SWIGTYPE_p_p_FMOD_SYSTEM.getCPtr(var1));
   }

   public static int FMOD_SoundGroup_SetMaxAudible(SWIGTYPE_p_FMOD_SOUNDGROUP var0, int var1) {
      return javafmodJNI.FMOD_SoundGroup_SetMaxAudible(SWIGTYPE_p_FMOD_SOUNDGROUP.getCPtr(var0), var1);
   }

   public static int FMOD_SoundGroup_GetMaxAudible(SWIGTYPE_p_FMOD_SOUNDGROUP var0, SWIGTYPE_p_int var1) {
      return javafmodJNI.FMOD_SoundGroup_GetMaxAudible(SWIGTYPE_p_FMOD_SOUNDGROUP.getCPtr(var0), SWIGTYPE_p_int.getCPtr(var1));
   }

   public static int FMOD_SoundGroup_SetMaxAudibleBehavior(SWIGTYPE_p_FMOD_SOUNDGROUP var0, SWIGTYPE_p_FMOD_SOUNDGROUP_BEHAVIOR var1) {
      return javafmodJNI.FMOD_SoundGroup_SetMaxAudibleBehavior(SWIGTYPE_p_FMOD_SOUNDGROUP.getCPtr(var0), SWIGTYPE_p_FMOD_SOUNDGROUP_BEHAVIOR.getCPtr(var1));
   }

   public static int FMOD_SoundGroup_GetMaxAudibleBehavior(SWIGTYPE_p_FMOD_SOUNDGROUP var0, SWIGTYPE_p_FMOD_SOUNDGROUP_BEHAVIOR var1) {
      return javafmodJNI.FMOD_SoundGroup_GetMaxAudibleBehavior(SWIGTYPE_p_FMOD_SOUNDGROUP.getCPtr(var0), SWIGTYPE_p_FMOD_SOUNDGROUP_BEHAVIOR.getCPtr(var1));
   }

   public static int FMOD_SoundGroup_SetMuteFadeSpeed(SWIGTYPE_p_FMOD_SOUNDGROUP var0, float var1) {
      return javafmodJNI.FMOD_SoundGroup_SetMuteFadeSpeed(SWIGTYPE_p_FMOD_SOUNDGROUP.getCPtr(var0), var1);
   }

   public static int FMOD_SoundGroup_GetMuteFadeSpeed(SWIGTYPE_p_FMOD_SOUNDGROUP var0, SWIGTYPE_p_float var1) {
      return javafmodJNI.FMOD_SoundGroup_GetMuteFadeSpeed(SWIGTYPE_p_FMOD_SOUNDGROUP.getCPtr(var0), SWIGTYPE_p_float.getCPtr(var1));
   }

   public static int FMOD_SoundGroup_SetVolume(SWIGTYPE_p_FMOD_SOUNDGROUP var0, float var1) {
      return javafmodJNI.FMOD_SoundGroup_SetVolume(SWIGTYPE_p_FMOD_SOUNDGROUP.getCPtr(var0), var1);
   }

   public static int FMOD_SoundGroup_GetVolume(SWIGTYPE_p_FMOD_SOUNDGROUP var0, SWIGTYPE_p_float var1) {
      return javafmodJNI.FMOD_SoundGroup_GetVolume(SWIGTYPE_p_FMOD_SOUNDGROUP.getCPtr(var0), SWIGTYPE_p_float.getCPtr(var1));
   }

   public static int FMOD_SoundGroup_Stop(SWIGTYPE_p_FMOD_SOUNDGROUP var0) {
      return javafmodJNI.FMOD_SoundGroup_Stop(SWIGTYPE_p_FMOD_SOUNDGROUP.getCPtr(var0));
   }

   public static int FMOD_SoundGroup_GetName(SWIGTYPE_p_FMOD_SOUNDGROUP var0, String var1, int var2) {
      return javafmodJNI.FMOD_SoundGroup_GetName(SWIGTYPE_p_FMOD_SOUNDGROUP.getCPtr(var0), var1, var2);
   }

   public static int FMOD_SoundGroup_GetNumSounds(SWIGTYPE_p_FMOD_SOUNDGROUP var0, SWIGTYPE_p_int var1) {
      return javafmodJNI.FMOD_SoundGroup_GetNumSounds(SWIGTYPE_p_FMOD_SOUNDGROUP.getCPtr(var0), SWIGTYPE_p_int.getCPtr(var1));
   }

   public static int FMOD_SoundGroup_GetSound(SWIGTYPE_p_FMOD_SOUNDGROUP var0, int var1, SWIGTYPE_p_p_FMOD_SOUND var2) {
      return javafmodJNI.FMOD_SoundGroup_GetSound(SWIGTYPE_p_FMOD_SOUNDGROUP.getCPtr(var0), var1, SWIGTYPE_p_p_FMOD_SOUND.getCPtr(var2));
   }

   public static int FMOD_SoundGroup_GetNumPlaying(SWIGTYPE_p_FMOD_SOUNDGROUP var0, SWIGTYPE_p_int var1) {
      return javafmodJNI.FMOD_SoundGroup_GetNumPlaying(SWIGTYPE_p_FMOD_SOUNDGROUP.getCPtr(var0), SWIGTYPE_p_int.getCPtr(var1));
   }

   public static int FMOD_SoundGroup_SetUserData(SWIGTYPE_p_FMOD_SOUNDGROUP var0, SWIGTYPE_p_void var1) {
      return javafmodJNI.FMOD_SoundGroup_SetUserData(SWIGTYPE_p_FMOD_SOUNDGROUP.getCPtr(var0), SWIGTYPE_p_void.getCPtr(var1));
   }

   public static int FMOD_SoundGroup_GetUserData(SWIGTYPE_p_FMOD_SOUNDGROUP var0, SWIGTYPE_p_p_void var1) {
      return javafmodJNI.FMOD_SoundGroup_GetUserData(SWIGTYPE_p_FMOD_SOUNDGROUP.getCPtr(var0), SWIGTYPE_p_p_void.getCPtr(var1));
   }

   public static int FMOD_DSP_Release(SWIGTYPE_p_FMOD_DSP var0) {
      return javafmodJNI.FMOD_DSP_Release(SWIGTYPE_p_FMOD_DSP.getCPtr(var0));
   }

   public static int FMOD_DSP_GetSystemObject(SWIGTYPE_p_FMOD_DSP var0, SWIGTYPE_p_p_FMOD_SYSTEM var1) {
      return javafmodJNI.FMOD_DSP_GetSystemObject(SWIGTYPE_p_FMOD_DSP.getCPtr(var0), SWIGTYPE_p_p_FMOD_SYSTEM.getCPtr(var1));
   }

   public static int FMOD_DSP_AddInput(SWIGTYPE_p_FMOD_DSP var0, SWIGTYPE_p_FMOD_DSP var1, SWIGTYPE_p_p_FMOD_DSPCONNECTION var2, SWIGTYPE_p_FMOD_DSPCONNECTION_TYPE var3) {
      return javafmodJNI.FMOD_DSP_AddInput(SWIGTYPE_p_FMOD_DSP.getCPtr(var0), SWIGTYPE_p_FMOD_DSP.getCPtr(var1), SWIGTYPE_p_p_FMOD_DSPCONNECTION.getCPtr(var2), SWIGTYPE_p_FMOD_DSPCONNECTION_TYPE.getCPtr(var3));
   }

   public static int FMOD_DSP_DisconnectFrom(SWIGTYPE_p_FMOD_DSP var0, SWIGTYPE_p_FMOD_DSP var1, SWIGTYPE_p_FMOD_DSPCONNECTION var2) {
      return javafmodJNI.FMOD_DSP_DisconnectFrom(SWIGTYPE_p_FMOD_DSP.getCPtr(var0), SWIGTYPE_p_FMOD_DSP.getCPtr(var1), SWIGTYPE_p_FMOD_DSPCONNECTION.getCPtr(var2));
   }

   public static int FMOD_DSP_DisconnectAll(SWIGTYPE_p_FMOD_DSP var0, SWIGTYPE_p_FMOD_BOOL var1, SWIGTYPE_p_FMOD_BOOL var2) {
      return javafmodJNI.FMOD_DSP_DisconnectAll(SWIGTYPE_p_FMOD_DSP.getCPtr(var0), SWIGTYPE_p_FMOD_BOOL.getCPtr(var1), SWIGTYPE_p_FMOD_BOOL.getCPtr(var2));
   }

   public static int FMOD_DSP_GetNumInputs(SWIGTYPE_p_FMOD_DSP var0, SWIGTYPE_p_int var1) {
      return javafmodJNI.FMOD_DSP_GetNumInputs(SWIGTYPE_p_FMOD_DSP.getCPtr(var0), SWIGTYPE_p_int.getCPtr(var1));
   }

   public static int FMOD_DSP_GetNumOutputs(SWIGTYPE_p_FMOD_DSP var0, SWIGTYPE_p_int var1) {
      return javafmodJNI.FMOD_DSP_GetNumOutputs(SWIGTYPE_p_FMOD_DSP.getCPtr(var0), SWIGTYPE_p_int.getCPtr(var1));
   }

   public static int FMOD_DSP_GetInput(SWIGTYPE_p_FMOD_DSP var0, int var1, SWIGTYPE_p_p_FMOD_DSP var2, SWIGTYPE_p_p_FMOD_DSPCONNECTION var3) {
      return javafmodJNI.FMOD_DSP_GetInput(SWIGTYPE_p_FMOD_DSP.getCPtr(var0), var1, SWIGTYPE_p_p_FMOD_DSP.getCPtr(var2), SWIGTYPE_p_p_FMOD_DSPCONNECTION.getCPtr(var3));
   }

   public static int FMOD_DSP_GetOutput(SWIGTYPE_p_FMOD_DSP var0, int var1, SWIGTYPE_p_p_FMOD_DSP var2, SWIGTYPE_p_p_FMOD_DSPCONNECTION var3) {
      return javafmodJNI.FMOD_DSP_GetOutput(SWIGTYPE_p_FMOD_DSP.getCPtr(var0), var1, SWIGTYPE_p_p_FMOD_DSP.getCPtr(var2), SWIGTYPE_p_p_FMOD_DSPCONNECTION.getCPtr(var3));
   }

   public static int FMOD_DSP_SetActive(SWIGTYPE_p_FMOD_DSP var0, SWIGTYPE_p_FMOD_BOOL var1) {
      return javafmodJNI.FMOD_DSP_SetActive(SWIGTYPE_p_FMOD_DSP.getCPtr(var0), SWIGTYPE_p_FMOD_BOOL.getCPtr(var1));
   }

   public static int FMOD_DSP_GetActive(SWIGTYPE_p_FMOD_DSP var0, SWIGTYPE_p_FMOD_BOOL var1) {
      return javafmodJNI.FMOD_DSP_GetActive(SWIGTYPE_p_FMOD_DSP.getCPtr(var0), SWIGTYPE_p_FMOD_BOOL.getCPtr(var1));
   }

   public static int FMOD_DSP_SetBypass(SWIGTYPE_p_FMOD_DSP var0, SWIGTYPE_p_FMOD_BOOL var1) {
      return javafmodJNI.FMOD_DSP_SetBypass(SWIGTYPE_p_FMOD_DSP.getCPtr(var0), SWIGTYPE_p_FMOD_BOOL.getCPtr(var1));
   }

   public static int FMOD_DSP_GetBypass(SWIGTYPE_p_FMOD_DSP var0, SWIGTYPE_p_FMOD_BOOL var1) {
      return javafmodJNI.FMOD_DSP_GetBypass(SWIGTYPE_p_FMOD_DSP.getCPtr(var0), SWIGTYPE_p_FMOD_BOOL.getCPtr(var1));
   }

   public static int FMOD_DSP_SetWetDryMix(SWIGTYPE_p_FMOD_DSP var0, float var1, float var2, float var3) {
      return javafmodJNI.FMOD_DSP_SetWetDryMix(SWIGTYPE_p_FMOD_DSP.getCPtr(var0), var1, var2, var3);
   }

   public static int FMOD_DSP_GetWetDryMix(SWIGTYPE_p_FMOD_DSP var0, SWIGTYPE_p_float var1, SWIGTYPE_p_float var2, SWIGTYPE_p_float var3) {
      return javafmodJNI.FMOD_DSP_GetWetDryMix(SWIGTYPE_p_FMOD_DSP.getCPtr(var0), SWIGTYPE_p_float.getCPtr(var1), SWIGTYPE_p_float.getCPtr(var2), SWIGTYPE_p_float.getCPtr(var3));
   }

   public static int FMOD_DSP_SetChannelFormat(SWIGTYPE_p_FMOD_DSP var0, SWIGTYPE_p_FMOD_CHANNELMASK var1, int var2, SWIGTYPE_p_FMOD_SPEAKERMODE var3) {
      return javafmodJNI.FMOD_DSP_SetChannelFormat(SWIGTYPE_p_FMOD_DSP.getCPtr(var0), SWIGTYPE_p_FMOD_CHANNELMASK.getCPtr(var1), var2, SWIGTYPE_p_FMOD_SPEAKERMODE.getCPtr(var3));
   }

   public static int FMOD_DSP_GetChannelFormat(SWIGTYPE_p_FMOD_DSP var0, SWIGTYPE_p_FMOD_CHANNELMASK var1, SWIGTYPE_p_int var2, SWIGTYPE_p_FMOD_SPEAKERMODE var3) {
      return javafmodJNI.FMOD_DSP_GetChannelFormat(SWIGTYPE_p_FMOD_DSP.getCPtr(var0), SWIGTYPE_p_FMOD_CHANNELMASK.getCPtr(var1), SWIGTYPE_p_int.getCPtr(var2), SWIGTYPE_p_FMOD_SPEAKERMODE.getCPtr(var3));
   }

   public static int FMOD_DSP_GetOutputChannelFormat(SWIGTYPE_p_FMOD_DSP var0, SWIGTYPE_p_FMOD_CHANNELMASK var1, int var2, SWIGTYPE_p_FMOD_SPEAKERMODE var3, SWIGTYPE_p_FMOD_CHANNELMASK var4, SWIGTYPE_p_int var5, SWIGTYPE_p_FMOD_SPEAKERMODE var6) {
      return javafmodJNI.FMOD_DSP_GetOutputChannelFormat(SWIGTYPE_p_FMOD_DSP.getCPtr(var0), SWIGTYPE_p_FMOD_CHANNELMASK.getCPtr(var1), var2, SWIGTYPE_p_FMOD_SPEAKERMODE.getCPtr(var3), SWIGTYPE_p_FMOD_CHANNELMASK.getCPtr(var4), SWIGTYPE_p_int.getCPtr(var5), SWIGTYPE_p_FMOD_SPEAKERMODE.getCPtr(var6));
   }

   public static int FMOD_DSP_Reset(SWIGTYPE_p_FMOD_DSP var0) {
      return javafmodJNI.FMOD_DSP_Reset(SWIGTYPE_p_FMOD_DSP.getCPtr(var0));
   }

   public static int FMOD_DSP_SetParameterFloat(SWIGTYPE_p_FMOD_DSP var0, int var1, float var2) {
      return javafmodJNI.FMOD_DSP_SetParameterFloat(SWIGTYPE_p_FMOD_DSP.getCPtr(var0), var1, var2);
   }

   public static int FMOD_DSP_SetParameterInt(SWIGTYPE_p_FMOD_DSP var0, int var1, int var2) {
      return javafmodJNI.FMOD_DSP_SetParameterInt(SWIGTYPE_p_FMOD_DSP.getCPtr(var0), var1, var2);
   }

   public static int FMOD_DSP_SetParameterBool(SWIGTYPE_p_FMOD_DSP var0, int var1, SWIGTYPE_p_FMOD_BOOL var2) {
      return javafmodJNI.FMOD_DSP_SetParameterBool(SWIGTYPE_p_FMOD_DSP.getCPtr(var0), var1, SWIGTYPE_p_FMOD_BOOL.getCPtr(var2));
   }

   public static int FMOD_DSP_SetParameterData(SWIGTYPE_p_FMOD_DSP var0, int var1, SWIGTYPE_p_void var2, long var3) {
      return javafmodJNI.FMOD_DSP_SetParameterData(SWIGTYPE_p_FMOD_DSP.getCPtr(var0), var1, SWIGTYPE_p_void.getCPtr(var2), var3);
   }

   public static int FMOD_DSP_GetParameterFloat(SWIGTYPE_p_FMOD_DSP var0, int var1, SWIGTYPE_p_float var2, String var3, int var4) {
      return javafmodJNI.FMOD_DSP_GetParameterFloat(SWIGTYPE_p_FMOD_DSP.getCPtr(var0), var1, SWIGTYPE_p_float.getCPtr(var2), var3, var4);
   }

   public static int FMOD_DSP_GetParameterInt(SWIGTYPE_p_FMOD_DSP var0, int var1, SWIGTYPE_p_int var2, String var3, int var4) {
      return javafmodJNI.FMOD_DSP_GetParameterInt(SWIGTYPE_p_FMOD_DSP.getCPtr(var0), var1, SWIGTYPE_p_int.getCPtr(var2), var3, var4);
   }

   public static int FMOD_DSP_GetParameterBool(SWIGTYPE_p_FMOD_DSP var0, int var1, SWIGTYPE_p_FMOD_BOOL var2, String var3, int var4) {
      return javafmodJNI.FMOD_DSP_GetParameterBool(SWIGTYPE_p_FMOD_DSP.getCPtr(var0), var1, SWIGTYPE_p_FMOD_BOOL.getCPtr(var2), var3, var4);
   }

   public static int FMOD_DSP_GetParameterData(SWIGTYPE_p_FMOD_DSP var0, int var1, SWIGTYPE_p_p_void var2, SWIGTYPE_p_unsigned_int var3, String var4, int var5) {
      return javafmodJNI.FMOD_DSP_GetParameterData(SWIGTYPE_p_FMOD_DSP.getCPtr(var0), var1, SWIGTYPE_p_p_void.getCPtr(var2), SWIGTYPE_p_unsigned_int.getCPtr(var3), var4, var5);
   }

   public static int FMOD_DSP_GetNumParameters(SWIGTYPE_p_FMOD_DSP var0, SWIGTYPE_p_int var1) {
      return javafmodJNI.FMOD_DSP_GetNumParameters(SWIGTYPE_p_FMOD_DSP.getCPtr(var0), SWIGTYPE_p_int.getCPtr(var1));
   }

   public static int FMOD_DSP_GetParameterInfo(SWIGTYPE_p_FMOD_DSP var0, int var1, SWIGTYPE_p_p_FMOD_DSP_PARAMETER_DESC var2) {
      return javafmodJNI.FMOD_DSP_GetParameterInfo(SWIGTYPE_p_FMOD_DSP.getCPtr(var0), var1, SWIGTYPE_p_p_FMOD_DSP_PARAMETER_DESC.getCPtr(var2));
   }

   public static int FMOD_DSP_GetDataParameterIndex(SWIGTYPE_p_FMOD_DSP var0, int var1, SWIGTYPE_p_int var2) {
      return javafmodJNI.FMOD_DSP_GetDataParameterIndex(SWIGTYPE_p_FMOD_DSP.getCPtr(var0), var1, SWIGTYPE_p_int.getCPtr(var2));
   }

   public static int FMOD_DSP_ShowConfigDialog(SWIGTYPE_p_FMOD_DSP var0, SWIGTYPE_p_void var1, SWIGTYPE_p_FMOD_BOOL var2) {
      return javafmodJNI.FMOD_DSP_ShowConfigDialog(SWIGTYPE_p_FMOD_DSP.getCPtr(var0), SWIGTYPE_p_void.getCPtr(var1), SWIGTYPE_p_FMOD_BOOL.getCPtr(var2));
   }

   public static int FMOD_DSP_GetInfo(SWIGTYPE_p_FMOD_DSP var0, String var1, SWIGTYPE_p_unsigned_int var2, SWIGTYPE_p_int var3, SWIGTYPE_p_int var4, SWIGTYPE_p_int var5) {
      return javafmodJNI.FMOD_DSP_GetInfo(SWIGTYPE_p_FMOD_DSP.getCPtr(var0), var1, SWIGTYPE_p_unsigned_int.getCPtr(var2), SWIGTYPE_p_int.getCPtr(var3), SWIGTYPE_p_int.getCPtr(var4), SWIGTYPE_p_int.getCPtr(var5));
   }

   public static int FMOD_DSP_GetType(SWIGTYPE_p_FMOD_DSP var0, SWIGTYPE_p_FMOD_DSP_TYPE var1) {
      return javafmodJNI.FMOD_DSP_GetType(SWIGTYPE_p_FMOD_DSP.getCPtr(var0), SWIGTYPE_p_FMOD_DSP_TYPE.getCPtr(var1));
   }

   public static int FMOD_DSP_GetIdle(SWIGTYPE_p_FMOD_DSP var0, SWIGTYPE_p_FMOD_BOOL var1) {
      return javafmodJNI.FMOD_DSP_GetIdle(SWIGTYPE_p_FMOD_DSP.getCPtr(var0), SWIGTYPE_p_FMOD_BOOL.getCPtr(var1));
   }

   public static int FMOD_DSP_SetUserData(SWIGTYPE_p_FMOD_DSP var0, SWIGTYPE_p_void var1) {
      return javafmodJNI.FMOD_DSP_SetUserData(SWIGTYPE_p_FMOD_DSP.getCPtr(var0), SWIGTYPE_p_void.getCPtr(var1));
   }

   public static int FMOD_DSP_GetUserData(SWIGTYPE_p_FMOD_DSP var0, SWIGTYPE_p_p_void var1) {
      return javafmodJNI.FMOD_DSP_GetUserData(SWIGTYPE_p_FMOD_DSP.getCPtr(var0), SWIGTYPE_p_p_void.getCPtr(var1));
   }

   public static int FMOD_DSP_SetMeteringEnabled(SWIGTYPE_p_FMOD_DSP var0, SWIGTYPE_p_FMOD_BOOL var1, SWIGTYPE_p_FMOD_BOOL var2) {
      return javafmodJNI.FMOD_DSP_SetMeteringEnabled(SWIGTYPE_p_FMOD_DSP.getCPtr(var0), SWIGTYPE_p_FMOD_BOOL.getCPtr(var1), SWIGTYPE_p_FMOD_BOOL.getCPtr(var2));
   }

   public static int FMOD_DSP_GetMeteringEnabled(SWIGTYPE_p_FMOD_DSP var0, SWIGTYPE_p_FMOD_BOOL var1, SWIGTYPE_p_FMOD_BOOL var2) {
      return javafmodJNI.FMOD_DSP_GetMeteringEnabled(SWIGTYPE_p_FMOD_DSP.getCPtr(var0), SWIGTYPE_p_FMOD_BOOL.getCPtr(var1), SWIGTYPE_p_FMOD_BOOL.getCPtr(var2));
   }

   public static int FMOD_DSP_GetMeteringInfo(SWIGTYPE_p_FMOD_DSP var0, SWIGTYPE_p_FMOD_DSP_METERING_INFO var1, SWIGTYPE_p_FMOD_DSP_METERING_INFO var2) {
      return javafmodJNI.FMOD_DSP_GetMeteringInfo(SWIGTYPE_p_FMOD_DSP.getCPtr(var0), SWIGTYPE_p_FMOD_DSP_METERING_INFO.getCPtr(var1), SWIGTYPE_p_FMOD_DSP_METERING_INFO.getCPtr(var2));
   }

   public static int FMOD_DSPConnection_GetInput(SWIGTYPE_p_FMOD_DSPCONNECTION var0, SWIGTYPE_p_p_FMOD_DSP var1) {
      return javafmodJNI.FMOD_DSPConnection_GetInput(SWIGTYPE_p_FMOD_DSPCONNECTION.getCPtr(var0), SWIGTYPE_p_p_FMOD_DSP.getCPtr(var1));
   }

   public static int FMOD_DSPConnection_GetOutput(SWIGTYPE_p_FMOD_DSPCONNECTION var0, SWIGTYPE_p_p_FMOD_DSP var1) {
      return javafmodJNI.FMOD_DSPConnection_GetOutput(SWIGTYPE_p_FMOD_DSPCONNECTION.getCPtr(var0), SWIGTYPE_p_p_FMOD_DSP.getCPtr(var1));
   }

   public static int FMOD_DSPConnection_SetMix(SWIGTYPE_p_FMOD_DSPCONNECTION var0, float var1) {
      return javafmodJNI.FMOD_DSPConnection_SetMix(SWIGTYPE_p_FMOD_DSPCONNECTION.getCPtr(var0), var1);
   }

   public static int FMOD_DSPConnection_GetMix(SWIGTYPE_p_FMOD_DSPCONNECTION var0, SWIGTYPE_p_float var1) {
      return javafmodJNI.FMOD_DSPConnection_GetMix(SWIGTYPE_p_FMOD_DSPCONNECTION.getCPtr(var0), SWIGTYPE_p_float.getCPtr(var1));
   }

   public static int FMOD_DSPConnection_SetMixMatrix(SWIGTYPE_p_FMOD_DSPCONNECTION var0, SWIGTYPE_p_float var1, int var2, int var3, int var4) {
      return javafmodJNI.FMOD_DSPConnection_SetMixMatrix(SWIGTYPE_p_FMOD_DSPCONNECTION.getCPtr(var0), SWIGTYPE_p_float.getCPtr(var1), var2, var3, var4);
   }

   public static int FMOD_DSPConnection_GetMixMatrix(SWIGTYPE_p_FMOD_DSPCONNECTION var0, SWIGTYPE_p_float var1, SWIGTYPE_p_int var2, SWIGTYPE_p_int var3, int var4) {
      return javafmodJNI.FMOD_DSPConnection_GetMixMatrix(SWIGTYPE_p_FMOD_DSPCONNECTION.getCPtr(var0), SWIGTYPE_p_float.getCPtr(var1), SWIGTYPE_p_int.getCPtr(var2), SWIGTYPE_p_int.getCPtr(var3), var4);
   }

   public static int FMOD_DSPConnection_GetType(SWIGTYPE_p_FMOD_DSPCONNECTION var0, SWIGTYPE_p_FMOD_DSPCONNECTION_TYPE var1) {
      return javafmodJNI.FMOD_DSPConnection_GetType(SWIGTYPE_p_FMOD_DSPCONNECTION.getCPtr(var0), SWIGTYPE_p_FMOD_DSPCONNECTION_TYPE.getCPtr(var1));
   }

   public static int FMOD_DSPConnection_SetUserData(SWIGTYPE_p_FMOD_DSPCONNECTION var0, SWIGTYPE_p_void var1) {
      return javafmodJNI.FMOD_DSPConnection_SetUserData(SWIGTYPE_p_FMOD_DSPCONNECTION.getCPtr(var0), SWIGTYPE_p_void.getCPtr(var1));
   }

   public static int FMOD_DSPConnection_GetUserData(SWIGTYPE_p_FMOD_DSPCONNECTION var0, SWIGTYPE_p_p_void var1) {
      return javafmodJNI.FMOD_DSPConnection_GetUserData(SWIGTYPE_p_FMOD_DSPCONNECTION.getCPtr(var0), SWIGTYPE_p_p_void.getCPtr(var1));
   }

   public static int FMOD_Geometry_Release(SWIGTYPE_p_FMOD_GEOMETRY var0) {
      return javafmodJNI.FMOD_Geometry_Release(SWIGTYPE_p_FMOD_GEOMETRY.getCPtr(var0));
   }

   public static int FMOD_Geometry_AddPolygon(SWIGTYPE_p_FMOD_GEOMETRY var0, float var1, float var2, SWIGTYPE_p_FMOD_BOOL var3, int var4, SWIGTYPE_p_FMOD_VECTOR var5, SWIGTYPE_p_int var6) {
      return javafmodJNI.FMOD_Geometry_AddPolygon(SWIGTYPE_p_FMOD_GEOMETRY.getCPtr(var0), var1, var2, SWIGTYPE_p_FMOD_BOOL.getCPtr(var3), var4, SWIGTYPE_p_FMOD_VECTOR.getCPtr(var5), SWIGTYPE_p_int.getCPtr(var6));
   }

   public static int FMOD_Geometry_GetNumPolygons(SWIGTYPE_p_FMOD_GEOMETRY var0, SWIGTYPE_p_int var1) {
      return javafmodJNI.FMOD_Geometry_GetNumPolygons(SWIGTYPE_p_FMOD_GEOMETRY.getCPtr(var0), SWIGTYPE_p_int.getCPtr(var1));
   }

   public static int FMOD_Geometry_GetMaxPolygons(SWIGTYPE_p_FMOD_GEOMETRY var0, SWIGTYPE_p_int var1, SWIGTYPE_p_int var2) {
      return javafmodJNI.FMOD_Geometry_GetMaxPolygons(SWIGTYPE_p_FMOD_GEOMETRY.getCPtr(var0), SWIGTYPE_p_int.getCPtr(var1), SWIGTYPE_p_int.getCPtr(var2));
   }

   public static int FMOD_Geometry_GetPolygonNumVertices(SWIGTYPE_p_FMOD_GEOMETRY var0, int var1, SWIGTYPE_p_int var2) {
      return javafmodJNI.FMOD_Geometry_GetPolygonNumVertices(SWIGTYPE_p_FMOD_GEOMETRY.getCPtr(var0), var1, SWIGTYPE_p_int.getCPtr(var2));
   }

   public static int FMOD_Geometry_SetPolygonVertex(SWIGTYPE_p_FMOD_GEOMETRY var0, int var1, int var2, SWIGTYPE_p_FMOD_VECTOR var3) {
      return javafmodJNI.FMOD_Geometry_SetPolygonVertex(SWIGTYPE_p_FMOD_GEOMETRY.getCPtr(var0), var1, var2, SWIGTYPE_p_FMOD_VECTOR.getCPtr(var3));
   }

   public static int FMOD_Geometry_GetPolygonVertex(SWIGTYPE_p_FMOD_GEOMETRY var0, int var1, int var2, SWIGTYPE_p_FMOD_VECTOR var3) {
      return javafmodJNI.FMOD_Geometry_GetPolygonVertex(SWIGTYPE_p_FMOD_GEOMETRY.getCPtr(var0), var1, var2, SWIGTYPE_p_FMOD_VECTOR.getCPtr(var3));
   }

   public static int FMOD_Geometry_SetPolygonAttributes(SWIGTYPE_p_FMOD_GEOMETRY var0, int var1, float var2, float var3, SWIGTYPE_p_FMOD_BOOL var4) {
      return javafmodJNI.FMOD_Geometry_SetPolygonAttributes(SWIGTYPE_p_FMOD_GEOMETRY.getCPtr(var0), var1, var2, var3, SWIGTYPE_p_FMOD_BOOL.getCPtr(var4));
   }

   public static int FMOD_Geometry_GetPolygonAttributes(SWIGTYPE_p_FMOD_GEOMETRY var0, int var1, SWIGTYPE_p_float var2, SWIGTYPE_p_float var3, SWIGTYPE_p_FMOD_BOOL var4) {
      return javafmodJNI.FMOD_Geometry_GetPolygonAttributes(SWIGTYPE_p_FMOD_GEOMETRY.getCPtr(var0), var1, SWIGTYPE_p_float.getCPtr(var2), SWIGTYPE_p_float.getCPtr(var3), SWIGTYPE_p_FMOD_BOOL.getCPtr(var4));
   }

   public static int FMOD_Geometry_SetActive(SWIGTYPE_p_FMOD_GEOMETRY var0, SWIGTYPE_p_FMOD_BOOL var1) {
      return javafmodJNI.FMOD_Geometry_SetActive(SWIGTYPE_p_FMOD_GEOMETRY.getCPtr(var0), SWIGTYPE_p_FMOD_BOOL.getCPtr(var1));
   }

   public static int FMOD_Geometry_GetActive(SWIGTYPE_p_FMOD_GEOMETRY var0, SWIGTYPE_p_FMOD_BOOL var1) {
      return javafmodJNI.FMOD_Geometry_GetActive(SWIGTYPE_p_FMOD_GEOMETRY.getCPtr(var0), SWIGTYPE_p_FMOD_BOOL.getCPtr(var1));
   }

   public static int FMOD_Geometry_SetRotation(SWIGTYPE_p_FMOD_GEOMETRY var0, SWIGTYPE_p_FMOD_VECTOR var1, SWIGTYPE_p_FMOD_VECTOR var2) {
      return javafmodJNI.FMOD_Geometry_SetRotation(SWIGTYPE_p_FMOD_GEOMETRY.getCPtr(var0), SWIGTYPE_p_FMOD_VECTOR.getCPtr(var1), SWIGTYPE_p_FMOD_VECTOR.getCPtr(var2));
   }

   public static int FMOD_Geometry_GetRotation(SWIGTYPE_p_FMOD_GEOMETRY var0, SWIGTYPE_p_FMOD_VECTOR var1, SWIGTYPE_p_FMOD_VECTOR var2) {
      return javafmodJNI.FMOD_Geometry_GetRotation(SWIGTYPE_p_FMOD_GEOMETRY.getCPtr(var0), SWIGTYPE_p_FMOD_VECTOR.getCPtr(var1), SWIGTYPE_p_FMOD_VECTOR.getCPtr(var2));
   }

   public static int FMOD_Geometry_SetPosition(SWIGTYPE_p_FMOD_GEOMETRY var0, SWIGTYPE_p_FMOD_VECTOR var1) {
      return javafmodJNI.FMOD_Geometry_SetPosition(SWIGTYPE_p_FMOD_GEOMETRY.getCPtr(var0), SWIGTYPE_p_FMOD_VECTOR.getCPtr(var1));
   }

   public static int FMOD_Geometry_GetPosition(SWIGTYPE_p_FMOD_GEOMETRY var0, SWIGTYPE_p_FMOD_VECTOR var1) {
      return javafmodJNI.FMOD_Geometry_GetPosition(SWIGTYPE_p_FMOD_GEOMETRY.getCPtr(var0), SWIGTYPE_p_FMOD_VECTOR.getCPtr(var1));
   }

   public static int FMOD_Geometry_SetScale(SWIGTYPE_p_FMOD_GEOMETRY var0, SWIGTYPE_p_FMOD_VECTOR var1) {
      return javafmodJNI.FMOD_Geometry_SetScale(SWIGTYPE_p_FMOD_GEOMETRY.getCPtr(var0), SWIGTYPE_p_FMOD_VECTOR.getCPtr(var1));
   }

   public static int FMOD_Geometry_GetScale(SWIGTYPE_p_FMOD_GEOMETRY var0, SWIGTYPE_p_FMOD_VECTOR var1) {
      return javafmodJNI.FMOD_Geometry_GetScale(SWIGTYPE_p_FMOD_GEOMETRY.getCPtr(var0), SWIGTYPE_p_FMOD_VECTOR.getCPtr(var1));
   }

   public static int FMOD_Geometry_Save(SWIGTYPE_p_FMOD_GEOMETRY var0, SWIGTYPE_p_void var1, SWIGTYPE_p_int var2) {
      return javafmodJNI.FMOD_Geometry_Save(SWIGTYPE_p_FMOD_GEOMETRY.getCPtr(var0), SWIGTYPE_p_void.getCPtr(var1), SWIGTYPE_p_int.getCPtr(var2));
   }

   public static int FMOD_Geometry_SetUserData(SWIGTYPE_p_FMOD_GEOMETRY var0, SWIGTYPE_p_void var1) {
      return javafmodJNI.FMOD_Geometry_SetUserData(SWIGTYPE_p_FMOD_GEOMETRY.getCPtr(var0), SWIGTYPE_p_void.getCPtr(var1));
   }

   public static int FMOD_Geometry_GetUserData(SWIGTYPE_p_FMOD_GEOMETRY var0, SWIGTYPE_p_p_void var1) {
      return javafmodJNI.FMOD_Geometry_GetUserData(SWIGTYPE_p_FMOD_GEOMETRY.getCPtr(var0), SWIGTYPE_p_p_void.getCPtr(var1));
   }

   public static int FMOD_Reverb3D_Release(SWIGTYPE_p_FMOD_REVERB3D var0) {
      return javafmodJNI.FMOD_Reverb3D_Release(SWIGTYPE_p_FMOD_REVERB3D.getCPtr(var0));
   }

   public static int FMOD_Reverb3D_Set3DAttributes(SWIGTYPE_p_FMOD_REVERB3D var0, SWIGTYPE_p_FMOD_VECTOR var1, float var2, float var3) {
      return javafmodJNI.FMOD_Reverb3D_Set3DAttributes(SWIGTYPE_p_FMOD_REVERB3D.getCPtr(var0), SWIGTYPE_p_FMOD_VECTOR.getCPtr(var1), var2, var3);
   }

   public static int FMOD_Reverb3D_Get3DAttributes(SWIGTYPE_p_FMOD_REVERB3D var0, SWIGTYPE_p_FMOD_VECTOR var1, SWIGTYPE_p_float var2, SWIGTYPE_p_float var3) {
      return javafmodJNI.FMOD_Reverb3D_Get3DAttributes(SWIGTYPE_p_FMOD_REVERB3D.getCPtr(var0), SWIGTYPE_p_FMOD_VECTOR.getCPtr(var1), SWIGTYPE_p_float.getCPtr(var2), SWIGTYPE_p_float.getCPtr(var3));
   }

   public static int FMOD_Reverb3D_SetProperties(SWIGTYPE_p_FMOD_REVERB3D var0, SWIGTYPE_p_FMOD_REVERB_PROPERTIES var1) {
      return javafmodJNI.FMOD_Reverb3D_SetProperties(SWIGTYPE_p_FMOD_REVERB3D.getCPtr(var0), SWIGTYPE_p_FMOD_REVERB_PROPERTIES.getCPtr(var1));
   }

   public static int FMOD_Reverb3D_GetProperties(SWIGTYPE_p_FMOD_REVERB3D var0, SWIGTYPE_p_FMOD_REVERB_PROPERTIES var1) {
      return javafmodJNI.FMOD_Reverb3D_GetProperties(SWIGTYPE_p_FMOD_REVERB3D.getCPtr(var0), SWIGTYPE_p_FMOD_REVERB_PROPERTIES.getCPtr(var1));
   }

   public static int FMOD_Reverb3D_SetActive(SWIGTYPE_p_FMOD_REVERB3D var0, SWIGTYPE_p_FMOD_BOOL var1) {
      return javafmodJNI.FMOD_Reverb3D_SetActive(SWIGTYPE_p_FMOD_REVERB3D.getCPtr(var0), SWIGTYPE_p_FMOD_BOOL.getCPtr(var1));
   }

   public static int FMOD_Reverb3D_GetActive(SWIGTYPE_p_FMOD_REVERB3D var0, SWIGTYPE_p_FMOD_BOOL var1) {
      return javafmodJNI.FMOD_Reverb3D_GetActive(SWIGTYPE_p_FMOD_REVERB3D.getCPtr(var0), SWIGTYPE_p_FMOD_BOOL.getCPtr(var1));
   }

   public static int FMOD_Reverb3D_SetUserData(SWIGTYPE_p_FMOD_REVERB3D var0, SWIGTYPE_p_void var1) {
      return javafmodJNI.FMOD_Reverb3D_SetUserData(SWIGTYPE_p_FMOD_REVERB3D.getCPtr(var0), SWIGTYPE_p_void.getCPtr(var1));
   }

   public static int FMOD_Reverb3D_GetUserData(SWIGTYPE_p_FMOD_REVERB3D var0, SWIGTYPE_p_p_void var1) {
      return javafmodJNI.FMOD_Reverb3D_GetUserData(SWIGTYPE_p_FMOD_REVERB3D.getCPtr(var0), SWIGTYPE_p_p_void.getCPtr(var1));
   }

   public static void FMOD_System_SetReverbDefault(int var0, int var1) {
      if (reverb[var0] != var1) {
         if (Core.bDebug) {
            DebugLog.log("reverb instance=" + var0 + " preset=" + var1);
         }

         reverb[var0] = var1;
      }

      javafmodJNI.FMOD_System_SetReverbDefault(var0, var1);
   }

   public static int FMOD_Studio_EventInstance3D(long var0, float var2, float var3, float var4) {
      return javafmodJNI.FMOD_Studio_EventInstance3D(var0, var2, var3, var4);
   }

   public static int FMOD_Studio_SetNumListeners(int var0) {
      return javafmodJNI.FMOD_Studio_SetNumListeners(var0);
   }

   public static void FMOD_Studio_Listener3D(int var0, float var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8, float var9, float var10, float var11, float var12) {
      javafmodJNI.FMOD_Studio_Listener3D(var0, var1, var2, var3, var4, var5, var6, var7, var8, var9, var10, var11, var12);
   }

   public static int FMOD_Studio_EventInstance_SetCallback(long var0, FMOD_STUDIO_EVENT_CALLBACK var2, int var3) {
      return javafmodJNI.FMOD_Studio_EventInstance_SetCallback(var0, var2, var3);
   }

   public static int FMOD_Studio_EventInstance_SetParameterByID(long var0, FMOD_STUDIO_PARAMETER_ID var2, float var3, boolean var4) {
      return var2 == null ? 0 : javafmodJNI.FMOD_Studio_EventInstance_SetParameterByID(var0, var2.address(), var3, var4);
   }

   public static int FMOD_Studio_EventInstance_SetParameterByID(long var0, FMOD_STUDIO_PARAMETER_ID var2, float var3) {
      boolean var4 = false;
      return FMOD_Studio_EventInstance_SetParameterByID(var0, var2, var3, var4);
   }

   public static int FMOD_Studio_EventInstance_SetParameterByName(long var0, String var2, float var3) {
      return javafmodJNI.FMOD_Studio_EventInstance_SetParameterByName(var0, var2, var3);
   }

   public static float FMOD_Studio_GetParameter(long var0, String var2) {
      return javafmodJNI.FMOD_Studio_GetParameter(var0, var2);
   }

   public static int FMOD_Studio_GetPlaybackState(long var0) {
      return javafmodJNI.FMOD_Studio_GetPlaybackState(var0);
   }

   public static int FMOD_Studio_EventInstance_SetVolume(long var0, float var2) {
      return javafmodJNI.FMOD_Studio_EventInstance_SetVolume(var0, var2);
   }

   public static int FMOD_Studio_ReleaseEventInstance(long var0) {
      return javafmodJNI.FMOD_Studio_ReleaseEventInstance(var0);
   }

   public static int FMOD_Studio_EventInstance_Stop(long var0, boolean var2) {
      return javafmodJNI.FMOD_Studio_EventInstance_Stop(var0, var2);
   }
}

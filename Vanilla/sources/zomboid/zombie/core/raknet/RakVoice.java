package zombie.core.raknet;

public class RakVoice {
   public static native void RVInit(int var0);

   public static native void RVInitServer(boolean var0, int var1, int var2, int var3, int var4, float var5, float var6, boolean var7);

   public static native void RVDeinit();

   public static native int GetComplexity();

   public static native void SetComplexity(int var0);

   public static native void RequestVoiceChannel(long var0);

   public static native void CloseAllChannels();

   public static native int GetBufferSizeBytes();

   public static native boolean GetServerVOIPEnable();

   public static native int GetSampleRate();

   public static native int GetSendFramePeriod();

   public static native int GetBuffering();

   public static native float GetMinDistance();

   public static native float GetMaxDistance();

   public static native boolean GetIs3D();

   public static native void CloseVoiceChannel(long var0);

   public static native boolean ReceiveFrame(long var0, byte[] var2);

   public static native void SendFrame(long var0, long var2, byte[] var4, long var5);

   public static native void SetLoopbackMode(boolean var0);

   public static native void SetVoiceBan(long var0, boolean var2);

   public static native void SetPlayerCoordinate(long var0, float var2, float var3, float var4, boolean var5);
}

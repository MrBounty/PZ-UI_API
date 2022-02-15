package zombie.core.znet;

public class SteamGameServer {
   public static int STEAM_SERVERS_DISCONNECTED = 0;
   public static int STEAM_SERVERS_CONNECTED = 1;
   public static int STEAM_SERVERS_CONNECTFAILURE = 2;

   public static native boolean Init(String var0, int var1, int var2, int var3, int var4, String var5);

   public static native void SetProduct(String var0);

   public static native void SetGameDescription(String var0);

   public static native void SetModDir(String var0);

   public static native void SetDedicatedServer(boolean var0);

   public static native void LogOnAnonymous();

   public static native void EnableHeartBeats(boolean var0);

   public static native void SetMaxPlayerCount(int var0);

   public static native void SetServerName(String var0);

   public static native void SetMapName(String var0);

   public static native void SetKeyValue(String var0, String var1);

   public static native void SetGameTags(String var0);

   public static native void SetRegion(String var0);

   public static native boolean BUpdateUserData(long var0, String var2, int var3);

   public static native int GetSteamServersConnectState();

   public static native long GetSteamID();
}

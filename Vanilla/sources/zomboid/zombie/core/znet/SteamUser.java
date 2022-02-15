package zombie.core.znet;

public class SteamUser {
   public static long GetSteamID() {
      return SteamUtils.isSteamModeEnabled() ? n_GetSteamID() : 0L;
   }

   public static String GetSteamIDString() {
      if (SteamUtils.isSteamModeEnabled()) {
         long var0 = n_GetSteamID();
         return SteamUtils.convertSteamIDToString(var0);
      } else {
         return null;
      }
   }

   private static native long n_GetSteamID();
}

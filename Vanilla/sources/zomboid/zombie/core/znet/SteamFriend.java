package zombie.core.znet;

import zombie.core.textures.Texture;

public class SteamFriend {
   private String name = "";
   private long steamID;
   private String steamIDString;

   public SteamFriend() {
   }

   public SteamFriend(String var1, long var2) {
      this.steamID = var2;
      this.steamIDString = SteamUtils.convertSteamIDToString(var2);
      this.name = var1;
   }

   public String getName() {
      return this.name;
   }

   public String getSteamID() {
      return this.steamIDString;
   }

   public Texture getAvatar() {
      return Texture.getSteamAvatar(this.steamID);
   }

   public String getState() {
      int var1 = SteamFriends.GetFriendPersonaState(this.steamID);
      switch(var1) {
      case 0:
         return "Offline";
      case 1:
         return "Online";
      case 2:
         return "Busy";
      case 3:
         return "Away";
      case 4:
         return "Snooze";
      case 5:
         return "LookingToTrade";
      case 6:
         return "LookingToPlay";
      default:
         return "Unknown";
      }
   }
}

package zombie.core.znet;

public class GameServerDetails {
   public String address;
   public int port;
   public long steamId;
   public String name;
   public String gamedir;
   public String map;
   public String gameDescription;
   public String tags;
   public int ping;
   public int numPlayers;
   public int maxPlayers;
   public boolean passwordProtected;
   public int version;

   public GameServerDetails() {
   }

   public GameServerDetails(String var1, int var2, long var3, String var5, String var6, String var7, String var8, String var9, int var10, int var11, int var12, boolean var13, int var14) {
      this.address = var1;
      this.port = var2;
      this.steamId = var3;
      this.name = var5;
      this.gamedir = var6;
      this.map = var7;
      this.gameDescription = var8;
      this.tags = var9;
      this.ping = var10;
      this.numPlayers = var11;
      this.maxPlayers = var12;
      this.passwordProtected = var13;
      this.version = var14;
   }
}

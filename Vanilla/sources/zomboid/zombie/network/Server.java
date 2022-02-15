package zombie.network;

public class Server {
   private String name = "My Server";
   private String ip = "127.0.0.1";
   private String localIP = "";
   private String port = "16262";
   private String serverpwd = "";
   private String description = "";
   private String userName = "";
   private String pwd = "";
   private int lastUpdate = 0;
   private String players = null;
   private String maxPlayers = null;
   private boolean open = false;
   private boolean bPublic = true;
   private String version = null;
   private String mods = null;
   private boolean passwordProtected;
   private String steamId = null;
   private String ping = null;
   private boolean hosted = false;

   public String getPort() {
      return this.port;
   }

   public void setPort(String var1) {
      this.port = var1;
   }

   public String getIp() {
      return this.ip;
   }

   public void setIp(String var1) {
      this.ip = var1;
   }

   public String getLocalIP() {
      return this.localIP;
   }

   public void setLocalIP(String var1) {
      this.localIP = var1;
   }

   public String getServerPassword() {
      return this.serverpwd;
   }

   public void setServerPassword(String var1) {
      this.serverpwd = var1 == null ? "" : var1;
   }

   public String getDescription() {
      return this.description;
   }

   public void setDescription(String var1) {
      this.description = var1;
   }

   public String getUserName() {
      return this.userName;
   }

   public void setUserName(String var1) {
      this.userName = var1;
   }

   public String getPwd() {
      return this.pwd;
   }

   public void setPwd(String var1) {
      this.pwd = var1;
   }

   public int getLastUpdate() {
      return this.lastUpdate;
   }

   public void setLastUpdate(int var1) {
      this.lastUpdate = var1;
   }

   public String getPlayers() {
      return this.players;
   }

   public void setPlayers(String var1) {
      this.players = var1;
   }

   public boolean isOpen() {
      return this.open;
   }

   public void setOpen(boolean var1) {
      this.open = var1;
   }

   public boolean isPublic() {
      return this.bPublic;
   }

   public void setPublic(boolean var1) {
      this.bPublic = var1;
   }

   public String getVersion() {
      return this.version;
   }

   public void setVersion(String var1) {
      this.version = var1;
   }

   public String getMaxPlayers() {
      return this.maxPlayers;
   }

   public void setMaxPlayers(String var1) {
      this.maxPlayers = var1;
   }

   public String getMods() {
      return this.mods;
   }

   public void setMods(String var1) {
      this.mods = var1;
   }

   public String getName() {
      return this.name;
   }

   public void setName(String var1) {
      this.name = var1;
   }

   public String getPing() {
      return this.ping;
   }

   public void setPing(String var1) {
      this.ping = var1;
   }

   public boolean isPasswordProtected() {
      return this.passwordProtected;
   }

   public void setPasswordProtected(boolean var1) {
      this.passwordProtected = var1;
   }

   public String getSteamId() {
      return this.steamId;
   }

   public void setSteamId(String var1) {
      this.steamId = var1;
   }

   public boolean isHosted() {
      return this.hosted;
   }

   public void setHosted(boolean var1) {
      this.hosted = var1;
   }
}

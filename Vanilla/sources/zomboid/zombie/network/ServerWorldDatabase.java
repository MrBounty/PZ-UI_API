package zombie.network;

import java.io.File;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Scanner;
import se.krka.kahlua.vm.KahluaTable;
import se.krka.kahlua.vm.KahluaTableIterator;
import zombie.ZomboidFileSystem;
import zombie.core.Core;
import zombie.core.secure.PZcrypt;
import zombie.core.znet.SteamUtils;
import zombie.debug.DebugLog;
import zombie.util.PZSQLUtils;

public class ServerWorldDatabase {
   public static ServerWorldDatabase instance = new ServerWorldDatabase();
   public String CommandLineAdminUsername = "admin";
   public String CommandLineAdminPassword;
   public boolean doAdmin = true;
   public DBSchema dbSchema = null;
   static CharsetEncoder asciiEncoder = Charset.forName("US-ASCII").newEncoder();
   Connection conn;
   private static final String nullChar = String.valueOf('\u0000');

   public DBSchema getDBSchema() {
      if (this.dbSchema == null) {
         this.dbSchema = new DBSchema(this.conn);
      }

      return this.dbSchema;
   }

   public void executeQuery(String var1, KahluaTable var2) throws SQLException {
      PreparedStatement var3 = this.conn.prepareStatement(var1);
      KahluaTableIterator var4 = var2.iterator();
      int var5 = 1;

      while(var4.advance()) {
         var3.setString(var5++, (String)var4.getValue());
      }

      var3.executeUpdate();
   }

   public ArrayList getTableResult(String var1) throws SQLException {
      ArrayList var2 = new ArrayList();
      PreparedStatement var3 = this.conn.prepareStatement("SELECT * FROM " + var1);
      ResultSet var4 = var3.executeQuery();
      DatabaseMetaData var5 = this.conn.getMetaData();
      ResultSet var6 = var5.getColumns((String)null, (String)null, var1, (String)null);
      ArrayList var7 = new ArrayList();
      DBResult var8 = new DBResult();

      while(var6.next()) {
         String var9 = var6.getString(4);
         if (!var9.equals("world") && !var9.equals("moderator") && !var9.equals("admin") && !var9.equals("password") && !var9.equals("encryptedPwd") && !var9.equals("pwdEncryptType") && !var9.equals("transactionID")) {
            var7.add(var9);
         }
      }

      var8.setColumns(var7);
      var8.setTableName(var1);

      while(var4.next()) {
         for(int var12 = 0; var12 < var7.size(); ++var12) {
            String var10 = (String)var7.get(var12);
            String var11 = var4.getString(var10);
            if ("'false'".equals(var11)) {
               var11 = "false";
            }

            if ("'true'".equals(var11)) {
               var11 = "true";
            }

            if (var11 == null) {
               var11 = "";
            }

            var8.getValues().put(var10, var11);
         }

         var2.add(var8);
         var8 = new DBResult();
         var8.setColumns(var7);
         var8.setTableName(var1);
      }

      var3.close();
      return var2;
   }

   public void saveAllTransactionsID(HashMap var1) {
      try {
         Iterator var2 = var1.keySet().iterator();
         PreparedStatement var3 = null;

         while(var2.hasNext()) {
            String var4 = (String)var2.next();
            Integer var5 = (Integer)var1.get(var4);
            var3 = this.conn.prepareStatement("UPDATE whitelist SET transactionID = ? WHERE username = ?");
            var3.setString(1, var5.toString());
            var3.setString(2, var4);
            var3.executeUpdate();
            var3.close();
         }
      } catch (Exception var6) {
         var6.printStackTrace();
      }

   }

   public void saveTransactionID(String var1, Integer var2) {
      try {
         if (!this.containsUser(var1)) {
            this.addUser(var1, "");
         }

         PreparedStatement var3 = this.conn.prepareStatement("UPDATE whitelist SET transactionID = ? WHERE username = ?");
         var3.setString(1, var2.toString());
         var3.setString(2, var1);
         var3.executeUpdate();
         var3.close();
      } catch (Exception var4) {
         var4.printStackTrace();
      }

   }

   public boolean containsUser(String var1) {
      try {
         PreparedStatement var2 = this.conn.prepareStatement("SELECT * FROM whitelist WHERE username = ? AND world = ?");
         var2.setString(1, var1);
         var2.setString(2, Core.GameSaveWorld);
         ResultSet var3 = var2.executeQuery();
         if (var3.next()) {
            var2.close();
            return true;
         }

         var2.close();
      } catch (SQLException var4) {
         var4.printStackTrace();
      }

      return false;
   }

   public boolean containsCaseinsensitiveUser(String var1) {
      try {
         PreparedStatement var2 = this.conn.prepareStatement("SELECT * FROM whitelist WHERE LOWER(username) = LOWER(?) AND world = ?");
         var2.setString(1, var1);
         var2.setString(2, Core.GameSaveWorld);
         ResultSet var3 = var2.executeQuery();
         if (var3.next()) {
            var2.close();
            return true;
         }

         var2.close();
      } catch (SQLException var4) {
         var4.printStackTrace();
      }

      return false;
   }

   public String changeUsername(String var1, String var2) throws SQLException {
      PreparedStatement var3 = this.conn.prepareStatement("SELECT * FROM whitelist WHERE username = ? AND world = ?");
      var3.setString(1, var1);
      var3.setString(2, Core.GameSaveWorld);
      ResultSet var4 = var3.executeQuery();
      if (var4.next()) {
         String var5 = var4.getString("id");
         var3.close();
         var3 = this.conn.prepareStatement("UPDATE whitelist SET username = ? WHERE id = ?");
         var3.setString(1, var2);
         var3.setString(2, var5);
         var3.executeUpdate();
         var3.close();
         return "Changed " + var1 + " user's name into " + var2;
      } else {
         return !ServerOptions.instance.getBoolean("Open") ? "User \"" + var1 + "\" is not in the whitelist, use /adduser first" : "Changed's name " + var1 + " into " + var2;
      }
   }

   public String addUser(String var1, String var2) throws SQLException {
      if (this.containsCaseinsensitiveUser(var1)) {
         return "A user with this name already exists";
      } else {
         try {
            PreparedStatement var3 = this.conn.prepareStatement("SELECT * FROM whitelist WHERE username = ? AND world = ?");
            var3.setString(1, var1);
            var3.setString(2, Core.GameSaveWorld);
            ResultSet var4 = var3.executeQuery();
            if (var4.next()) {
               var3.close();
               return "User " + var1 + " already exist.";
            }

            var3.close();
            var3 = this.conn.prepareStatement("INSERT INTO whitelist (world, username, password, encryptedPwd, pwdEncryptType) VALUES (?, ?, ?, 'true', '2')");
            var3.setString(1, Core.GameSaveWorld);
            var3.setString(2, var1);
            var3.setString(3, var2);
            var3.executeUpdate();
            var3.close();
         } catch (SQLException var5) {
            var5.printStackTrace();
         }

         return "User " + var1 + " created with the password " + var2;
      }
   }

   public void updateDisplayName(String var1, String var2) {
      try {
         PreparedStatement var3 = this.conn.prepareStatement("SELECT * FROM whitelist WHERE username = ? AND world = ?");
         var3.setString(1, var1);
         var3.setString(2, Core.GameSaveWorld);
         ResultSet var4 = var3.executeQuery();
         if (var4.next()) {
            var3.close();
            var3 = this.conn.prepareStatement("UPDATE whitelist SET displayName = ? WHERE username = ?");
            var3.setString(1, var2);
            var3.setString(2, var1);
            var3.executeUpdate();
            var3.close();
         }

         var3.close();
      } catch (SQLException var5) {
         var5.printStackTrace();
      }

   }

   public String getDisplayName(String var1) {
      try {
         PreparedStatement var2 = this.conn.prepareStatement("SELECT * FROM whitelist WHERE username = ? AND world = ?");
         var2.setString(1, var1);
         var2.setString(2, Core.GameSaveWorld);
         ResultSet var3 = var2.executeQuery();
         if (var3.next()) {
            String var4 = var3.getString("displayName");
            var2.close();
            return var4;
         }

         var2.close();
      } catch (SQLException var5) {
         var5.printStackTrace();
      }

      return null;
   }

   public String removeUser(String var1) throws SQLException {
      try {
         PreparedStatement var2 = this.conn.prepareStatement("DELETE FROM whitelist WHERE world = ? and username = ?");
         var2.setString(1, Core.GameSaveWorld);
         var2.setString(2, var1);
         var2.executeUpdate();
         var2.close();
      } catch (SQLException var3) {
         var3.printStackTrace();
      }

      return "User " + var1 + " removed from white list";
   }

   public void removeUserLog(String var1, String var2, String var3) throws SQLException {
      try {
         PreparedStatement var4 = this.conn.prepareStatement("DELETE FROM userlog WHERE username = ? AND type = ? AND text = ?");
         var4.setString(1, var1);
         var4.setString(2, var2);
         var4.setString(3, var3);
         var4.executeUpdate();
         var4.close();
      } catch (SQLException var5) {
         var5.printStackTrace();
      }

   }

   public void create() throws SQLException, ClassNotFoundException {
      String var10002 = ZomboidFileSystem.instance.getCacheDir();
      File var1 = new File(var10002 + File.separator + "db");
      if (!var1.exists()) {
         var1.mkdirs();
      }

      var10002 = ZomboidFileSystem.instance.getCacheDir();
      File var2 = new File(var10002 + File.separator + "db" + File.separator + GameServer.ServerName + ".db");
      var2.setReadable(true, false);
      var2.setExecutable(true, false);
      var2.setWritable(true, false);
      DebugLog.log("user database \"" + var2.getPath() + "\"");
      if (!var2.exists()) {
         try {
            var2.createNewFile();
            this.conn = PZSQLUtils.getConnection(var2.getAbsolutePath());
            Statement var3 = this.conn.createStatement();
            var3.executeUpdate("CREATE TABLE [whitelist] ([id] INTEGER  PRIMARY KEY AUTOINCREMENT NOT NULL,[world] TEXT DEFAULT '" + GameServer.ServerName + "' NULL,[username] TEXT  NULL,[password] TEXT  NULL, [admin] BOOLEAN DEFAULT false NULL, [moderator] BOOLEAN DEFAULT false NULL, [banned] BOOLEAN DEFAULT false NULL, [lastConnection] TEXT NULL)");
            var3.executeUpdate("CREATE UNIQUE INDEX [id] ON [whitelist]([id]  ASC)");
            var3.executeUpdate("CREATE UNIQUE INDEX [username] ON [whitelist]([username]  ASC)");
            var3.executeUpdate("CREATE TABLE [bannedip] ([ip] TEXT NOT NULL,[username] TEXT NULL, [reason] TEXT NULL)");
            var3.close();
         } catch (Exception var12) {
            var12.printStackTrace();
            DebugLog.log("failed to create user database, server shut down");
            System.exit(1);
         }
      }

      if (this.conn == null) {
         try {
            this.conn = PZSQLUtils.getConnection(var2.getAbsolutePath());
         } catch (Exception var11) {
            var11.printStackTrace();
            DebugLog.log("failed to open user database, server shut down");
            System.exit(1);
         }
      }

      DatabaseMetaData var13 = this.conn.getMetaData();
      ResultSet var4 = var13.getColumns((String)null, (String)null, "whitelist", "admin");
      Statement var5 = this.conn.createStatement();
      if (!var4.next()) {
         var5.executeUpdate("ALTER TABLE 'whitelist' ADD 'admin' BOOLEAN NULL DEFAULT false");
      }

      var4.close();
      var4 = var13.getColumns((String)null, (String)null, "whitelist", "moderator");
      if (!var4.next()) {
         var5.executeUpdate("ALTER TABLE 'whitelist' ADD 'moderator' BOOLEAN NULL DEFAULT false");
      }

      var4.close();
      var4 = var13.getColumns((String)null, (String)null, "whitelist", "banned");
      if (!var4.next()) {
         var5.executeUpdate("ALTER TABLE 'whitelist' ADD 'banned' BOOLEAN NULL DEFAULT false");
      }

      var4.close();
      var4 = var13.getColumns((String)null, (String)null, "whitelist", "lastConnection");
      if (!var4.next()) {
         var5.executeUpdate("ALTER TABLE 'whitelist' ADD 'lastConnection' TEXT NULL");
      }

      var4.close();
      var4 = var13.getColumns((String)null, (String)null, "whitelist", "encryptedPwd");
      if (!var4.next()) {
         var5.executeUpdate("ALTER TABLE 'whitelist' ADD 'encryptedPwd' BOOLEAN NULL DEFAULT false");
      }

      var4.close();
      var4 = var13.getColumns((String)null, (String)null, "whitelist", "pwdEncryptType");
      if (!var4.next()) {
         var5.executeUpdate("ALTER TABLE 'whitelist' ADD 'pwdEncryptType' INTEGER NULL DEFAULT 1");
      }

      var4.close();
      if (SteamUtils.isSteamModeEnabled()) {
         var4 = var13.getColumns((String)null, (String)null, "whitelist", "steamid");
         if (!var4.next()) {
            var5.executeUpdate("ALTER TABLE 'whitelist' ADD 'steamid' TEXT NULL");
         }

         var4.close();
         var4 = var13.getColumns((String)null, (String)null, "whitelist", "ownerid");
         if (!var4.next()) {
            var5.executeUpdate("ALTER TABLE 'whitelist' ADD 'ownerid' TEXT NULL");
         }

         var4.close();
      }

      var4 = var13.getColumns((String)null, (String)null, "whitelist", "accesslevel");
      if (!var4.next()) {
         var5.executeUpdate("ALTER TABLE 'whitelist' ADD 'accesslevel' TEXT NULL");
      }

      var4.close();
      var4 = var13.getColumns((String)null, (String)null, "whitelist", "transactionID");
      if (!var4.next()) {
         var5.executeUpdate("ALTER TABLE 'whitelist' ADD 'transactionID' INTEGER NULL");
      }

      var4.close();
      var4 = var13.getColumns((String)null, (String)null, "whitelist", "displayName");
      if (!var4.next()) {
         var5.executeUpdate("ALTER TABLE 'whitelist' ADD 'displayName' TEXT NULL");
      }

      var4.close();
      var4 = var5.executeQuery("SELECT * FROM sqlite_master WHERE type = 'index' AND sql LIKE '%UNIQUE%' and name = 'username'");
      if (!var4.next()) {
         try {
            var5.executeUpdate("CREATE UNIQUE INDEX [username] ON [whitelist]([username]  ASC)");
         } catch (Exception var10) {
            System.out.println("Can't create the username index because some of the username in the database are in double, will drop the double username.");
            var5.executeUpdate("DELETE FROM whitelist WHERE whitelist.rowid > (SELECT rowid FROM whitelist dbl WHERE whitelist.rowid <> dbl.rowid AND  whitelist.username = dbl.username);");
            var5.executeUpdate("CREATE UNIQUE INDEX [username] ON [whitelist]([username]  ASC)");
         }
      }

      var4 = var13.getTables((String)null, (String)null, "bannedip", (String[])null);
      if (!var4.next()) {
         var5.executeUpdate("CREATE TABLE [bannedip] ([ip] TEXT NOT NULL,[username] TEXT NULL, [reason] TEXT NULL)");
      }

      var4.close();
      var4 = var13.getTables((String)null, (String)null, "bannedid", (String[])null);
      if (!var4.next()) {
         var5.executeUpdate("CREATE TABLE [bannedid] ([steamid] TEXT NOT NULL, [reason] TEXT NULL)");
      }

      var4.close();
      var4 = var13.getTables((String)null, (String)null, "userlog", (String[])null);
      if (!var4.next()) {
         var5.executeUpdate("CREATE TABLE [userlog] ([id] INTEGER  PRIMARY KEY AUTOINCREMENT NOT NULL,[username] TEXT  NULL,[type] TEXT  NULL, [text] TEXT  NULL, [issuedBy] TEXT  NULL, [amount] INTEGER NULL)");
      }

      var4.close();
      var4 = var13.getColumns((String)null, (String)null, "whitelist", "moderator");
      if (var4.next()) {
      }

      var4.close();
      var4 = var13.getColumns((String)null, (String)null, "whitelist", "admin");
      PreparedStatement var6;
      PreparedStatement var8;
      if (var4.next()) {
         var4.close();
         var6 = this.conn.prepareStatement("SELECT * FROM whitelist where admin = 'true'");
         ResultSet var7 = var6.executeQuery();

         while(var7.next()) {
            var8 = this.conn.prepareStatement("UPDATE whitelist set accesslevel = 'admin' where id = ?");
            var8.setString(1, var7.getString("id"));
            System.out.println(var7.getString("username"));
            var8.executeUpdate();
         }
      }

      var4 = var13.getTables((String)null, (String)null, "tickets", (String[])null);
      if (!var4.next()) {
         var5.executeUpdate("CREATE TABLE [tickets] ([id] INTEGER  PRIMARY KEY AUTOINCREMENT NOT NULL, [message] TEXT NOT NULL, [author] TEXT NOT NULL,[answeredID] INTEGER,[viewed] BOOLEAN NULL DEFAULT false)");
      }

      var4.close();
      var6 = this.conn.prepareStatement("SELECT * FROM whitelist WHERE username = ?");
      var6.setString(1, this.CommandLineAdminUsername);
      var4 = var6.executeQuery();
      String var14;
      if (!var4.next()) {
         var6.close();
         var14 = this.CommandLineAdminPassword;
         if (var14 == null || var14.isEmpty()) {
            Scanner var15 = new Scanner(new InputStreamReader(System.in));
            System.out.println("User 'admin' not found, creating it ");
            System.out.println("Command line admin password: " + this.CommandLineAdminPassword);
            System.out.println("Enter new administrator password: ");
            var14 = var15.nextLine();

            label124:
            while(true) {
               if (var14 != null && !"".equals(var14)) {
                  System.out.println("Confirm the password: ");
                  String var9 = var15.nextLine();

                  while(true) {
                     if (var9 != null && !"".equals(var9) && var14.equals(var9)) {
                        break label124;
                     }

                     System.out.println("Wrong password, confirm the password: ");
                     var9 = var15.nextLine();
                  }
               }

               System.out.println("Enter new administrator password: ");
               var14 = var15.nextLine();
            }
         }

         if (this.doAdmin) {
            var6 = this.conn.prepareStatement("INSERT INTO whitelist (username, password, accesslevel, encryptedPwd, pwdEncryptType) VALUES (?, ?, 'admin', 'true', '2')");
         } else {
            var6 = this.conn.prepareStatement("INSERT INTO whitelist (username, password, encryptedPwd, pwdEncryptType) VALUES (?, ?, 'true', '2')");
         }

         var6.setString(1, this.CommandLineAdminUsername);
         var6.setString(2, PZcrypt.hash(encrypt(var14)));
         var6.executeUpdate();
         var6.close();
         System.out.println("Administrator account '" + this.CommandLineAdminUsername + "' created.");
      } else {
         var6.close();
      }

      var5.close();
      if (this.CommandLineAdminPassword != null && !this.CommandLineAdminPassword.isEmpty()) {
         var14 = PZcrypt.hash(encrypt(this.CommandLineAdminPassword));
         var8 = this.conn.prepareStatement("SELECT * FROM whitelist WHERE username = ?");
         var8.setString(1, this.CommandLineAdminUsername);
         var4 = var8.executeQuery();
         if (var4.next()) {
            var8.close();
            var8 = this.conn.prepareStatement("UPDATE whitelist SET password = ? WHERE username = ?");
            var8.setString(1, var14);
            var8.setString(2, this.CommandLineAdminUsername);
            var8.executeUpdate();
            System.out.println("admin password changed via -adminpassword option");
         } else {
            System.out.println("ERROR: -adminpassword ignored, no '" + this.CommandLineAdminUsername + "' account in db");
         }

         var8.close();
      }

   }

   public void close() {
      try {
         if (this.conn != null) {
            this.conn.close();
         }
      } catch (SQLException var2) {
         var2.printStackTrace();
      }

   }

   public static boolean isValidUserName(String var0) {
      if (var0 != null && !var0.trim().isEmpty() && !var0.contains(";") && !var0.contains("@") && !var0.contains("$") && !var0.contains(",") && !var0.contains("/") && !var0.contains(".") && !var0.contains("'") && !var0.contains("?") && !var0.contains("\"") && var0.trim().length() >= 3 && var0.length() <= 20) {
         if (var0.contains(nullChar)) {
            return false;
         } else if (var0.trim().equals("admin")) {
            return true;
         } else {
            return !var0.trim().toLowerCase().startsWith("admin");
         }
      } else {
         return false;
      }
   }

   public ServerWorldDatabase.LogonResult authClient(String var1, String var2, String var3, long var4) {
      System.out.println("User " + var1 + " is trying to connect.");
      ServerWorldDatabase.LogonResult var6 = new ServerWorldDatabase.LogonResult();
      if (!ServerOptions.instance.AllowNonAsciiUsername.getValue() && !asciiEncoder.canEncode(var1)) {
         var6.bAuthorized = false;
         var6.dcReason = "NonAsciiCharacters";
         return var6;
      } else if (!isValidUserName(var1)) {
         var6.bAuthorized = false;
         var6.dcReason = "InvalidUsername";
         return var6;
      } else {
         try {
            PreparedStatement var7;
            ResultSet var8;
            if (!SteamUtils.isSteamModeEnabled() && !var3.equals("127.0.0.1")) {
               var7 = this.conn.prepareStatement("SELECT * FROM bannedip WHERE ip = ?");
               var7.setString(1, var3);
               var8 = var7.executeQuery();
               if (var8.next()) {
                  var6.bAuthorized = false;
                  var6.bannedReason = var8.getString("reason");
                  var6.banned = true;
                  var7.close();
                  return var6;
               }

               var7.close();
            }

            if (isNullOrEmpty(var2) && ServerOptions.instance.Open.getValue() && ServerOptions.instance.AutoCreateUserInWhiteList.getValue()) {
               var6.dcReason = "UserPasswordRequired";
               var6.bAuthorized = false;
               return var6;
            }

            var7 = this.conn.prepareStatement("SELECT * FROM whitelist WHERE LOWER(username) = LOWER(?) AND world = ?");
            var7.setString(1, var1);
            var7.setString(2, Core.GameSaveWorld);
            var8 = var7.executeQuery();
            if (var8.next()) {
               String var9;
               String var10;
               PreparedStatement var11;
               if (!isNullOrEmpty(var8.getString("password")) && (var8.getString("encryptedPwd").equals("false") || var8.getString("encryptedPwd").equals("N"))) {
                  var9 = var8.getString("password");
                  var10 = encrypt(var9);
                  var11 = this.conn.prepareStatement("UPDATE whitelist SET encryptedPwd = 'true' WHERE username = ? and password = ?");
                  var11.setString(1, var1);
                  var11.setString(2, var9);
                  var11.executeUpdate();
                  var11.close();
                  var11 = this.conn.prepareStatement("UPDATE whitelist SET password = ? WHERE username = ? AND password = ?");
                  var11.setString(1, var10);
                  var11.setString(2, var1);
                  var11.setString(3, var9);
                  var11.executeUpdate();
                  var11.close();
                  var8 = var7.executeQuery();
               }

               if (!isNullOrEmpty(var8.getString("password")) && var8.getInt("pwdEncryptType") == 1) {
                  var9 = var8.getString("password");
                  var10 = PZcrypt.hash(var9);
                  var11 = this.conn.prepareStatement("UPDATE whitelist SET pwdEncryptType = '2', password = ? WHERE username = ? AND password = ?");
                  var11.setString(1, var10);
                  var11.setString(2, var1);
                  var11.setString(3, var9);
                  var11.executeUpdate();
                  var11.close();
                  var8 = var7.executeQuery();
               }

               if (!isNullOrEmpty(var8.getString("password")) && !var8.getString("password").equals(var2)) {
                  var6.bAuthorized = false;
                  var7.close();
                  if (isNullOrEmpty(var2)) {
                     var6.dcReason = "DuplicateAccount";
                  } else {
                     var6.dcReason = "InvalidUsernamePassword";
                  }

                  return var6;
               }

               var6.bAuthorized = true;
               var6.admin = "true".equals(var8.getString("admin")) || "Y".equals(var8.getString("admin"));
               var6.accessLevel = var8.getString("accesslevel");
               if (var6.accessLevel == null) {
                  var6.accessLevel = "";
                  if (var6.admin) {
                     var6.accessLevel = "admin";
                  }

                  this.setAccessLevel(var1, var6.accessLevel);
               }

               var6.banned = "true".equals(var8.getString("banned")) || "Y".equals(var8.getString("banned"));
               if (var6.banned) {
                  var6.bAuthorized = false;
               }

               if (var8.getString("transactionID") == null) {
                  var6.transactionID = 0;
               } else {
                  var6.transactionID = Integer.parseInt(var8.getString("transactionID"));
               }

               var7.close();
               return var6;
            }

            if (ServerOptions.instance.Open.getValue()) {
               if (!this.isNewAccountAllowed(var3, var4)) {
                  var7.close();
                  var6.bAuthorized = false;
                  var6.dcReason = "MaxAccountsReached";
                  return var6;
               }

               var6.bAuthorized = true;
               var7.close();
               return var6;
            }

            var6.bAuthorized = false;
            var6.dcReason = "UnknownUsername";
            var7.close();
         } catch (Exception var12) {
            var12.printStackTrace();
         }

         return var6;
      }
   }

   public ServerWorldDatabase.LogonResult authClient(long var1) {
      String var3 = SteamUtils.convertSteamIDToString(var1);
      System.out.println("Steam client " + var3 + " is initiating a connection.");
      ServerWorldDatabase.LogonResult var4 = new ServerWorldDatabase.LogonResult();

      try {
         PreparedStatement var5 = this.conn.prepareStatement("SELECT * FROM bannedid WHERE steamid = ?");
         var5.setString(1, var3);
         ResultSet var6 = var5.executeQuery();
         if (var6.next()) {
            var4.bAuthorized = false;
            var4.bannedReason = var6.getString("reason");
            var4.banned = true;
            var5.close();
            return var4;
         }

         var5.close();
         var4.bAuthorized = true;
      } catch (Exception var7) {
         var7.printStackTrace();
      }

      return var4;
   }

   public ServerWorldDatabase.LogonResult authOwner(long var1, long var3) {
      String var5 = SteamUtils.convertSteamIDToString(var1);
      String var6 = SteamUtils.convertSteamIDToString(var3);
      System.out.println("Steam client " + var5 + " borrowed the game from " + var6);
      ServerWorldDatabase.LogonResult var7 = new ServerWorldDatabase.LogonResult();

      try {
         PreparedStatement var8 = this.conn.prepareStatement("SELECT * FROM bannedid WHERE steamid = ?");
         var8.setString(1, var6);
         ResultSet var9 = var8.executeQuery();
         if (var9.next()) {
            var7.bAuthorized = false;
            var7.bannedReason = var9.getString("reason");
            var7.banned = true;
            var8.close();
            return var7;
         }

         var8.close();
         var7.bAuthorized = true;
         var8 = this.conn.prepareStatement("UPDATE whitelist SET ownerid = ? where steamid = ?");
         var8.setString(1, var6);
         var8.setString(2, var5);
         var8.executeUpdate();
         var8.close();
      } catch (Exception var10) {
         var10.printStackTrace();
      }

      return var7;
   }

   private boolean isNewAccountAllowed(String var1, long var2) {
      int var4 = ServerOptions.instance.MaxAccountsPerUser.getValue();
      if (var4 <= 0) {
         return true;
      } else if (!SteamUtils.isSteamModeEnabled()) {
         return true;
      } else {
         String var5 = SteamUtils.convertSteamIDToString(var2);
         int var6 = 0;

         try {
            PreparedStatement var7 = this.conn.prepareStatement("SELECT * FROM whitelist WHERE steamid = ? AND accessLevel = ?");

            try {
               var7.setString(1, var5);
               var7.setString(2, "");

               for(ResultSet var8 = var7.executeQuery(); var8.next(); ++var6) {
               }
            } catch (Throwable var11) {
               if (var7 != null) {
                  try {
                     var7.close();
                  } catch (Throwable var10) {
                     var11.addSuppressed(var10);
                  }
               }

               throw var11;
            }

            if (var7 != null) {
               var7.close();
            }
         } catch (Exception var12) {
            var12.printStackTrace();
            return true;
         }

         return var6 < var4;
      }
   }

   public static String encrypt(String var0) {
      if (isNullOrEmpty(var0)) {
         return "";
      } else {
         byte[] var1 = null;

         try {
            var1 = MessageDigest.getInstance("MD5").digest(var0.getBytes());
         } catch (NoSuchAlgorithmException var5) {
            System.out.println("Can't encrypt password");
            var5.printStackTrace();
         }

         StringBuilder var2 = new StringBuilder();

         for(int var3 = 0; var3 < var1.length; ++var3) {
            String var4 = Integer.toHexString(var1[var3]);
            if (var4.length() == 1) {
               var2.append('0');
               var2.append(var4.charAt(var4.length() - 1));
            } else {
               var2.append(var4.substring(var4.length() - 2));
            }
         }

         return var2.toString();
      }
   }

   public String changePwd(String var1, String var2, String var3) throws SQLException {
      PreparedStatement var5 = this.conn.prepareStatement("SELECT * FROM whitelist WHERE username = ? AND password = ? AND world = ?");
      var5.setString(1, var1);
      var5.setString(2, var2);
      var5.setString(3, Core.GameSaveWorld);
      ResultSet var6 = var5.executeQuery();
      if (var6.next()) {
         var5.close();
         var5 = this.conn.prepareStatement("UPDATE whitelist SET pwdEncryptType = '2', password = ? WHERE username = ? and password = ?");
         var5.setString(1, var3);
         var5.setString(2, var1);
         var5.setString(3, var2);
         var5.executeUpdate();
         var5.close();
         return "Your new password is " + var3;
      } else {
         var5.close();
         return "Wrong password for user " + var1;
      }
   }

   public String grantAdmin(String var1, boolean var2) throws SQLException {
      PreparedStatement var3 = this.conn.prepareStatement("SELECT * FROM whitelist WHERE username = ? AND world = ?");
      var3.setString(1, var1);
      var3.setString(2, Core.GameSaveWorld);
      ResultSet var4 = var3.executeQuery();
      if (var4.next()) {
         var3.close();
         var3 = this.conn.prepareStatement("UPDATE whitelist SET admin = ? WHERE username = ?");
         var3.setString(1, var2 ? "true" : "false");
         var3.setString(2, var1);
         var3.executeUpdate();
         var3.close();
         return var2 ? "User " + var1 + " is now admin" : "User " + var1 + " is no longer admin";
      } else {
         var3.close();
         return "User \"" + var1 + "\" is not in the whitelist, use /adduser first";
      }
   }

   public String setAccessLevel(String var1, String var2) throws SQLException {
      var2 = var2.trim();
      if (!this.containsUser(var1)) {
         this.addUser(var1, "");
      }

      PreparedStatement var3 = this.conn.prepareStatement("SELECT * FROM whitelist WHERE username = ? AND world = ?");
      var3.setString(1, var1);
      var3.setString(2, Core.GameSaveWorld);
      ResultSet var4 = var3.executeQuery();
      if (var4.next()) {
         var3.close();
         var3 = this.conn.prepareStatement("UPDATE whitelist SET accesslevel = ? WHERE username = ?");
         var3.setString(1, var2);
         var3.setString(2, var1);
         var3.executeUpdate();
         var3.close();
         return var2.equals("") ? "User " + var1 + " no longer has access level" : "User " + var1 + " is now " + var2;
      } else {
         var3.close();
         return "User \"" + var1 + "\" is not in the whitelist, use /adduser first";
      }
   }

   public ArrayList getUserlog(String var1) {
      ArrayList var2 = new ArrayList();

      try {
         PreparedStatement var3 = this.conn.prepareStatement("SELECT * FROM userlog WHERE username = ?");
         var3.setString(1, var1);
         ResultSet var4 = var3.executeQuery();

         while(var4.next()) {
            var2.add(new Userlog(var1, var4.getString("type"), var4.getString("text"), var4.getString("issuedBy"), var4.getInt("amount")));
         }

         var3.close();
      } catch (SQLException var5) {
         var5.printStackTrace();
      }

      return var2;
   }

   public void addUserlog(String var1, Userlog.UserlogType var2, String var3, String var4, int var5) {
      try {
         boolean var6 = true;
         PreparedStatement var7;
         if (var2 == Userlog.UserlogType.LuaChecksum || var2 == Userlog.UserlogType.DupeItem) {
            var7 = this.conn.prepareStatement("SELECT * FROM userlog WHERE username = ? AND type = ?");
            var7.setString(1, var1);
            var7.setString(2, var2.toString());
            ResultSet var8 = var7.executeQuery();
            if (var8.next()) {
               var6 = false;
               var5 = Integer.parseInt(var8.getString("amount")) + 1;
               var7.close();
               PreparedStatement var9 = this.conn.prepareStatement("UPDATE userlog set amount = ? WHERE username = ? AND type = ?");
               var9.setString(1, (new Integer(var5)).toString());
               var9.setString(2, var1);
               var9.setString(3, var2.toString());
               var9.executeUpdate();
               var9.close();
            }
         }

         if (var6) {
            var7 = this.conn.prepareStatement("INSERT INTO userlog (username, type, text, issuedBy, amount) VALUES (?, ?, ?, ?, ?)");
            var7.setString(1, var1);
            var7.setString(2, var2.toString());
            var7.setString(3, var3);
            var7.setString(4, var4);
            var7.setString(5, (new Integer(var5)).toString());
            var7.executeUpdate();
            var7.close();
         }
      } catch (Exception var10) {
         var10.printStackTrace();
      }

   }

   public String banUser(String var1, boolean var2) throws SQLException {
      PreparedStatement var3 = this.conn.prepareStatement("SELECT * FROM whitelist WHERE username = ? AND world = ?");
      var3.setString(1, var1);
      var3.setString(2, Core.GameSaveWorld);
      ResultSet var4 = var3.executeQuery();
      boolean var5 = var4.next();
      if (var2 && !var5) {
         PreparedStatement var6 = this.conn.prepareStatement("INSERT INTO whitelist (world, username, password, encryptedPwd) VALUES (?, ?, 'bogus', 'false')");
         var6.setString(1, Core.GameSaveWorld);
         var6.setString(2, var1);
         var6.executeUpdate();
         var6.close();
         var4 = var3.executeQuery();
         var5 = true;
      }

      if (var5) {
         String var8 = "true";
         if (!var2) {
            var8 = "false";
         }

         var3.close();
         var3 = this.conn.prepareStatement("UPDATE whitelist SET banned = ? WHERE username = ?");
         var3.setString(1, var8);
         var3.setString(2, var1);
         var3.executeUpdate();
         var3.close();
         if (SteamUtils.isSteamModeEnabled()) {
            var3 = this.conn.prepareStatement("SELECT steamid FROM whitelist WHERE username = ? AND world = ?");
            var3.setString(1, var1);
            var3.setString(2, Core.GameSaveWorld);
            var4 = var3.executeQuery();
            if (var4.next()) {
               String var7 = var4.getString("steamid");
               var3.close();
               if (var7 != null && !var7.isEmpty()) {
                  this.banSteamID(var7, "", var2);
               }
            } else {
               var3.close();
            }
         }

         return var2 ? "User " + var1 + " is now banned" : "User " + var1 + " is now un-banned";
      } else {
         var3.close();
         return "User \"" + var1 + "\" is not in the whitelist, use /adduser first";
      }
   }

   public String banIp(String var1, String var2, String var3, boolean var4) throws SQLException {
      PreparedStatement var5;
      if (var4) {
         var5 = this.conn.prepareStatement("INSERT INTO bannedip (ip, username, reason) VALUES (?, ?, ?)");
         var5.setString(1, var1);
         var5.setString(2, var2);
         var5.setString(3, var3);
         var5.executeUpdate();
         var5.close();
      } else {
         if (var1 != null) {
            var5 = this.conn.prepareStatement("DELETE FROM bannedip WHERE ip = ?");
            var5.setString(1, var1);
            var5.executeUpdate();
            var5.close();
         }

         var5 = this.conn.prepareStatement("DELETE FROM bannedip WHERE username = ?");
         var5.setString(1, var2);
         var5.executeUpdate();
         var5.close();
      }

      return "";
   }

   public String banSteamID(String var1, String var2, boolean var3) throws SQLException {
      PreparedStatement var4;
      if (var3) {
         var4 = this.conn.prepareStatement("INSERT INTO bannedid (steamid, reason) VALUES (?, ?)");
         var4.setString(1, var1);
         var4.setString(2, var2);
         var4.executeUpdate();
         var4.close();
      } else {
         var4 = this.conn.prepareStatement("DELETE FROM bannedid WHERE steamid = ?");
         var4.setString(1, var1);
         var4.executeUpdate();
         var4.close();
      }

      return "";
   }

   public String setUserSteamID(String var1, String var2) {
      try {
         PreparedStatement var3 = this.conn.prepareStatement("SELECT * FROM whitelist WHERE username = ?");
         var3.setString(1, var1);
         ResultSet var4 = var3.executeQuery();
         if (!var4.next()) {
            var3.close();
            return "User " + var1 + " not found";
         }

         var3.close();
         var3 = this.conn.prepareStatement("UPDATE whitelist SET steamid = ? WHERE username = ?");
         var3.setString(1, var2);
         var3.setString(2, var1);
         var3.executeUpdate();
         var3.close();
      } catch (SQLException var5) {
         var5.printStackTrace();
      }

      return "User " + var1 + " SteamID set to " + var2;
   }

   public void setPassword(String var1, String var2) throws SQLException {
      try {
         PreparedStatement var3 = this.conn.prepareStatement("UPDATE whitelist SET pwdEncryptType = '2', password = ? WHERE username = ? and world = ?");
         var3.setString(1, var2);
         var3.setString(2, var1);
         var3.setString(3, Core.GameSaveWorld);
         var3.executeUpdate();
         var3.close();
      } catch (SQLException var4) {
         var4.printStackTrace();
      }

   }

   public void updateLastConnectionDate(String var1, String var2) {
      try {
         SimpleDateFormat var3 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
         PreparedStatement var4 = this.conn.prepareStatement("UPDATE whitelist SET lastConnection = ? WHERE username = ? AND password = ?");
         var4.setString(1, var3.format(Calendar.getInstance().getTime()));
         var4.setString(2, var1);
         var4.setString(3, var2);
         var4.executeUpdate();
         var4.close();
      } catch (SQLException var5) {
         var5.printStackTrace();
      }

   }

   private static boolean isNullOrEmpty(String var0) {
      return var0 == null || var0.isEmpty();
   }

   public String addWarningPoint(String var1, String var2, int var3, String var4) throws SQLException {
      PreparedStatement var5 = this.conn.prepareStatement("SELECT * FROM whitelist WHERE username = ? AND world = ?");
      var5.setString(1, var1);
      var5.setString(2, Core.GameSaveWorld);
      ResultSet var6 = var5.executeQuery();
      if (var6.next()) {
         this.addUserlog(var1, Userlog.UserlogType.WarningPoint, var2, var4, var3);
         return "Added a warning point on " + var1 + " reason: " + var2;
      } else {
         return "User " + var1 + " doesn't exist.";
      }
   }

   public void addTicket(String var1, String var2, int var3) throws SQLException {
      PreparedStatement var4;
      if (var3 > -1) {
         var4 = this.conn.prepareStatement("INSERT INTO tickets (author, message, answeredID) VALUES (?, ?, ?)");
         var4.setString(1, var1);
         var4.setString(2, var2);
         var4.setInt(3, var3);
         var4.executeUpdate();
         var4.close();
      } else {
         var4 = this.conn.prepareStatement("INSERT INTO tickets (author, message) VALUES (?, ?)");
         var4.setString(1, var1);
         var4.setString(2, var2);
         var4.executeUpdate();
         var4.close();
      }

   }

   public ArrayList getTickets(String var1) throws SQLException {
      ArrayList var2 = new ArrayList();
      PreparedStatement var3 = null;
      if (var1 != null) {
         var3 = this.conn.prepareStatement("SELECT * FROM tickets WHERE author = ? and answeredID is null");
         var3.setString(1, var1);
      } else {
         var3 = this.conn.prepareStatement("SELECT * FROM tickets where answeredID is null");
      }

      ResultSet var4 = var3.executeQuery();

      while(var4.next()) {
         DBTicket var5 = new DBTicket(var4.getString("author"), var4.getString("message"), var4.getInt("id"));
         var2.add(var5);
         DBTicket var6 = this.getAnswer(var5.getTicketID());
         if (var6 != null) {
            var5.setAnswer(var6);
         }
      }

      return var2;
   }

   private DBTicket getAnswer(int var1) throws SQLException {
      PreparedStatement var2 = null;
      var2 = this.conn.prepareStatement("SELECT * FROM tickets WHERE answeredID = ?");
      var2.setInt(1, var1);
      ResultSet var3 = var2.executeQuery();
      return var3.next() ? new DBTicket(var3.getString("author"), var3.getString("message"), var3.getInt("id")) : null;
   }

   public void removeTicket(int var1) throws SQLException {
      DBTicket var2 = this.getAnswer(var1);
      PreparedStatement var3;
      if (var2 != null) {
         var3 = this.conn.prepareStatement("DELETE FROM tickets WHERE id = ?");
         var3.setInt(1, var2.getTicketID());
         var3.executeUpdate();
         var3.close();
      }

      var3 = this.conn.prepareStatement("DELETE FROM tickets WHERE id = ?");
      var3.setInt(1, var1);
      var3.executeUpdate();
      var3.close();
   }

   public class LogonResult {
      public boolean bAuthorized = false;
      public int x;
      public int y;
      public int z;
      public boolean admin = false;
      public boolean banned = false;
      public String bannedReason = null;
      public String dcReason = null;
      public String accessLevel = "";
      public int transactionID = 0;
   }
}

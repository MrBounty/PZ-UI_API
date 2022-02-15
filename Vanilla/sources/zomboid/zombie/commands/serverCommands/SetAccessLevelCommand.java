package zombie.commands.serverCommands;

import java.sql.SQLException;
import zombie.characters.IsoPlayer;
import zombie.commands.CommandArgs;
import zombie.commands.CommandBase;
import zombie.commands.CommandHelp;
import zombie.commands.CommandName;
import zombie.commands.RequiredRight;
import zombie.core.logger.LoggerManager;
import zombie.core.raknet.UdpConnection;
import zombie.network.GameServer;
import zombie.network.ServerWorldDatabase;
import zombie.network.chat.ChatServer;

@CommandName(
   name = "setaccesslevel"
)
@CommandArgs(
   required = {"(.+)", "(\\w+)"}
)
@CommandHelp(
   helpText = "UI_ServerOptionDesc_SetAccessLevel"
)
@RequiredRight(
   requiredRights = 36
)
public class SetAccessLevelCommand extends CommandBase {
   public SetAccessLevelCommand(String var1, String var2, String var3, UdpConnection var4) {
      super(var1, var2, var3, var4);
   }

   protected String Command() throws SQLException {
      String var1 = this.getCommandArg(0);
      String var2 = "none".equals(this.getCommandArg(1)) ? "" : this.getCommandArg(1);
      return update(this.getExecutorUsername(), this.connection, var1, var2);
   }

   static String update(String var0, UdpConnection var1, String var2, String var3) throws SQLException {
      if ((var1 == null || !var1.isCoopHost) && !ServerWorldDatabase.instance.containsUser(var2) && var1 != null) {
         return "User \"" + var2 + "\" is not in the whitelist, use /adduser first";
      } else {
         IsoPlayer var4 = GameServer.getPlayerByUserName(var2);
         if (var1 != null && var1.accessLevel.equals("moderator") && var3.equals("admin")) {
            return "Moderators can't set Admin access level";
         } else if (!var3.equals("") && !var3.equals("admin") && !var3.equals("moderator") && !var3.equals("overseer") && !var3.equals("gm") && !var3.equals("observer")) {
            return "Access Level '" + var3 + "' unknown, list of access level: none, admin, moderator, overseer, gm, observer";
         } else {
            if (var4 != null) {
               UdpConnection var5 = GameServer.getConnectionFromPlayer(var4);
               String var6 = "";
               if (var5 != null) {
                  var6 = var5.accessLevel;
               } else {
                  var6 = var4.accessLevel;
               }

               if (!var6.equals(var3)) {
                  if (var3.equals("admin")) {
                     ChatServer.getInstance().joinAdminChat(var4.OnlineID);
                  } else if (var6.equals("admin") && !var3.equals("admin")) {
                     ChatServer.getInstance().leaveAdminChat(var4.OnlineID);
                  }
               }

               var4.accessLevel = var3;
               if (var5 != null) {
                  var5.accessLevel = var3;
               }

               if (!var3.equals("admin") && !var3.equals("moderator") && !var3.equals("overseer") && !var3.equals("gm") && !var3.equals("observer")) {
                  var4.setGodMod(false);
                  var4.setGhostMode(false);
                  var4.setInvisible(false);
               } else {
                  var4.setGodMod(true);
                  var4.setGhostMode(true);
                  var4.setInvisible(true);
               }

               GameServer.sendPlayerExtraInfo(var4, (UdpConnection)null);
            }

            LoggerManager.getLogger("admin").write(var0 + " granted " + var3 + " access level on " + var2);
            return var1 != null && var1.isCoopHost ? "Your access level is now: " + var3 : ServerWorldDatabase.instance.setAccessLevel(var2, var3);
         }
      }
   }
}

package zombie.commands.serverCommands;

import java.sql.SQLException;
import zombie.commands.CommandArgs;
import zombie.commands.CommandBase;
import zombie.commands.CommandHelp;
import zombie.commands.CommandName;
import zombie.commands.RequiredRight;
import zombie.core.logger.LoggerManager;
import zombie.core.logger.ZLogger;
import zombie.core.raknet.UdpConnection;
import zombie.network.GameServer;
import zombie.network.ServerWorldDatabase;

@CommandName(
   name = "addusertowhitelist"
)
@CommandArgs(
   required = {"(.+)"}
)
@CommandHelp(
   helpText = "UI_ServerOptionDesc_AddWhitelist"
)
@RequiredRight(
   requiredRights = 36
)
public class AddUserToWhiteListCommand extends CommandBase {
   public AddUserToWhiteListCommand(String var1, String var2, String var3, UdpConnection var4) {
      super(var1, var2, var3, var4);
   }

   protected String Command() throws SQLException {
      String var1 = this.getCommandArg(0);
      if (!ServerWorldDatabase.isValidUserName(var1)) {
         return "Invalid username \"" + var1 + "\"";
      } else {
         for(int var2 = 0; var2 < GameServer.udpEngine.connections.size(); ++var2) {
            UdpConnection var3 = (UdpConnection)GameServer.udpEngine.connections.get(var2);
            if (var3.username.equals(var1)) {
               if (var3.password != null && !var3.password.equals("")) {
                  ZLogger var10000 = LoggerManager.getLogger("admin");
                  String var10001 = this.getExecutorUsername();
                  var10000.write(var10001 + " created user " + var3.username + " with password " + var3.password);
                  return ServerWorldDatabase.instance.addUser(var3.username, var3.password);
               }

               return "User " + var1 + " doesn't have a password.";
            }
         }

         return "User " + var1 + " not found.";
      }
   }
}

package zombie.commands.serverCommands;

import java.sql.SQLException;
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
   name = "addalltowhitelist"
)
@CommandHelp(
   helpText = "UI_ServerOptionDesc_AddAllWhitelist"
)
@RequiredRight(
   requiredRights = 36
)
public class AddAllToWhiteListCommand extends CommandBase {
   public AddAllToWhiteListCommand(String var1, String var2, String var3, UdpConnection var4) {
      super(var1, var2, var3, var4);
   }

   protected String Command() {
      StringBuilder var1 = new StringBuilder("");

      for(int var2 = 0; var2 < GameServer.udpEngine.connections.size(); ++var2) {
         UdpConnection var3 = (UdpConnection)GameServer.udpEngine.connections.get(var2);
         if (var3.password != null && !var3.password.equals("")) {
            ZLogger var10000 = LoggerManager.getLogger("admin");
            String var10001 = this.getExecutorUsername();
            var10000.write(var10001 + " created user " + var3.username + " with password " + var3.password);

            try {
               var1.append(ServerWorldDatabase.instance.addUser(var3.username, var3.password)).append(" <LINE> ");
            } catch (SQLException var5) {
               var5.printStackTrace();
            }
         } else {
            var1.append("User ").append(var3.username).append(" doesn't have a password. <LINE> ");
         }
      }

      var1.append("Done.");
      return var1.toString();
   }
}

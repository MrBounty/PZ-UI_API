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
import zombie.network.ServerWorldDatabase;

@CommandName(
   name = "removeuserfromwhitelist"
)
@CommandArgs(
   required = {"(.+)"}
)
@CommandHelp(
   helpText = "UI_ServerOptionDesc_RemoveWhitelist"
)
@RequiredRight(
   requiredRights = 36
)
public class RemoveUserFromWhiteList extends CommandBase {
   public RemoveUserFromWhiteList(String var1, String var2, String var3, UdpConnection var4) {
      super(var1, var2, var3, var4);
   }

   protected String Command() throws SQLException {
      String var1 = this.getCommandArg(0);
      ZLogger var10000 = LoggerManager.getLogger("admin");
      String var10001 = this.getExecutorUsername();
      var10000.write(var10001 + " removed user " + var1 + " from whitelist");
      return ServerWorldDatabase.instance.removeUser(var1);
   }
}

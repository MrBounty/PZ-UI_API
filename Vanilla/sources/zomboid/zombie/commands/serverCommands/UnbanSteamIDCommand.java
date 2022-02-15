package zombie.commands.serverCommands;

import java.sql.SQLException;
import zombie.commands.CommandArgs;
import zombie.commands.CommandBase;
import zombie.commands.CommandHelp;
import zombie.commands.CommandName;
import zombie.commands.RequiredRight;
import zombie.core.logger.LoggerManager;
import zombie.core.raknet.UdpConnection;
import zombie.core.znet.SteamUtils;
import zombie.network.ServerWorldDatabase;

@CommandName(
   name = "unbanid"
)
@CommandArgs(
   required = {"(.+)"}
)
@CommandHelp(
   helpText = "UI_ServerOptionDesc_UnBanSteamId"
)
@RequiredRight(
   requiredRights = 36
)
public class UnbanSteamIDCommand extends CommandBase {
   public UnbanSteamIDCommand(String var1, String var2, String var3, UdpConnection var4) {
      super(var1, var2, var3, var4);
   }

   protected String Command() throws SQLException {
      String var1 = this.getCommandArg(0);
      if (!SteamUtils.isSteamModeEnabled()) {
         return "Server is not in Steam mode";
      } else if (!SteamUtils.isValidSteamID(var1)) {
         return "Expected SteamID but got \"" + var1 + "\"";
      } else {
         LoggerManager.getLogger("admin").write(this.getExecutorUsername() + " unbanned steamid " + var1, "IMPORTANT");
         ServerWorldDatabase.instance.banSteamID(var1, "", false);
         return "SteamID " + var1 + " is now unbanned";
      }
   }
}

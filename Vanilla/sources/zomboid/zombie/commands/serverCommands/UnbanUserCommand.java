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
import zombie.core.znet.SteamUtils;
import zombie.network.GameServer;
import zombie.network.ServerWorldDatabase;

@CommandName(
   name = "unbanuser"
)
@CommandArgs(
   required = {"(.+)"}
)
@CommandHelp(
   helpText = "UI_ServerOptionDesc_UnBanUser"
)
@RequiredRight(
   requiredRights = 36
)
public class UnbanUserCommand extends CommandBase {
   public UnbanUserCommand(String var1, String var2, String var3, UdpConnection var4) {
      super(var1, var2, var3, var4);
   }

   protected String Command() throws SQLException {
      String var1 = this.getCommandArg(0);
      String var2 = ServerWorldDatabase.instance.banUser(var1, false);
      ZLogger var10000 = LoggerManager.getLogger("admin");
      String var10001 = this.getExecutorUsername();
      var10000.write(var10001 + " unbanned user " + var1);
      if (!SteamUtils.isSteamModeEnabled()) {
         ServerWorldDatabase.instance.banIp((String)null, var1, (String)null, false);

         for(int var3 = 0; var3 < GameServer.udpEngine.connections.size(); ++var3) {
            UdpConnection var4 = (UdpConnection)GameServer.udpEngine.connections.get(var3);
            if (var4.username.equals(var1)) {
               ServerWorldDatabase.instance.banIp(var4.ip, var1, (String)null, false);
               break;
            }
         }
      }

      return var2;
   }
}

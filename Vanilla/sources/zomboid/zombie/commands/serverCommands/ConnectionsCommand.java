package zombie.commands.serverCommands;

import zombie.commands.CommandBase;
import zombie.commands.CommandHelp;
import zombie.commands.CommandName;
import zombie.commands.CommandNames;
import zombie.commands.DisabledCommand;
import zombie.commands.RequiredRight;
import zombie.core.raknet.UdpConnection;
import zombie.network.GameServer;

@DisabledCommand
@CommandNames({@CommandName(
   name = "connections"
), @CommandName(
   name = "list"
)})
@CommandHelp(
   helpText = "UI_ServerOptionDesc_Connections"
)
@RequiredRight(
   requiredRights = 44
)
public class ConnectionsCommand extends CommandBase {
   public ConnectionsCommand(String var1, String var2, String var3, UdpConnection var4) {
      super(var1, var2, var3, var4);
   }

   protected String Command() {
      String var1 = "";
      String var2 = " <LINE> ";
      if (this.connection == null) {
         var2 = "\n";
      }

      for(int var3 = 0; var3 < GameServer.udpEngine.connections.size(); ++var3) {
         UdpConnection var4 = (UdpConnection)GameServer.udpEngine.connections.get(var3);

         for(int var5 = 0; var5 < 4; ++var5) {
            if (var4.usernames[var5] != null) {
               var1 = var1 + "connection=" + (var3 + 1) + "/" + GameServer.udpEngine.connections.size() + " " + var4.idStr + " player=" + (var5 + 1) + "/4 id=" + var4.playerIDs[var5] + " username=\"" + var4.usernames[var5] + "\" fullyConnected=" + var4.isFullyConnected() + var2;
            }
         }
      }

      return var1 + "Players listed";
   }
}

package zombie.commands.serverCommands;

import zombie.characters.IsoPlayer;
import zombie.commands.CommandArgs;
import zombie.commands.CommandBase;
import zombie.commands.CommandName;
import zombie.commands.RequiredRight;
import zombie.core.raknet.UdpConnection;
import zombie.network.GameServer;

@CommandName(
   name = "debugplayer"
)
@CommandArgs(
   required = {"(.+)"}
)
@RequiredRight(
   requiredRights = 32
)
public class DebugPlayerCommand extends CommandBase {
   public DebugPlayerCommand(String var1, String var2, String var3, UdpConnection var4) {
      super(var1, var2, var3, var4);
   }

   protected String Command() {
      if (this.getCommandArgsCount() != 1) {
         return "/debugplayer \"username\"";
      } else {
         String var1 = this.getCommandArg(0);
         IsoPlayer var2 = GameServer.getPlayerByUserNameForCommand(var1);
         if (var2 == null) {
            return "no such user";
         } else {
            UdpConnection var3 = GameServer.getConnectionByPlayerOnlineID(var2.OnlineID);
            if (var3 == null) {
               return "no connection for user";
            } else if (GameServer.DebugPlayer.contains(var3)) {
               GameServer.DebugPlayer.remove(var3);
               return "debug off";
            } else {
               GameServer.DebugPlayer.add(var3);
               return "debug on";
            }
         }
      }
   }
}

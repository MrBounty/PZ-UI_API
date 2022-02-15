package zombie.commands.serverCommands;

import java.sql.SQLException;
import zombie.characters.IsoPlayer;
import zombie.commands.CommandBase;
import zombie.commands.CommandHelp;
import zombie.commands.CommandName;
import zombie.commands.RequiredRight;
import zombie.core.raknet.UdpConnection;
import zombie.iso.areas.SafeHouse;
import zombie.network.GameServer;

@CommandName(
   name = "releasesafehouse"
)
@CommandHelp(
   helpText = "UI_ServerOptionDesc_SafeHouse"
)
@RequiredRight(
   requiredRights = 63
)
public class ReleaseSafehouseCommand extends CommandBase {
   public ReleaseSafehouseCommand(String var1, String var2, String var3, UdpConnection var4) {
      super(var1, var2, var3, var4);
   }

   protected String Command() throws SQLException {
      if (this.isCommandComeFromServerConsole()) {
         return getCommandName(this.getClass()) + " can be executed only from the game";
      } else {
         String var1 = this.getExecutorUsername();
         IsoPlayer var2 = GameServer.getPlayerByUserNameForCommand(var1);
         SafeHouse var3 = SafeHouse.hasSafehouse(var1);
         if (var3 != null) {
            if (!var3.isOwner(var2)) {
               return "Only owner can release safehouse";
            } else {
               var3.removeSafeHouse(var2);
               return "Your safehouse was released";
            }
         } else {
            return "You have no safehouse";
         }
      }
   }
}

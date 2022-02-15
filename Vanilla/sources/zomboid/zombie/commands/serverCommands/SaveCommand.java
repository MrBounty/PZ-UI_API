package zombie.commands.serverCommands;

import zombie.commands.CommandBase;
import zombie.commands.CommandHelp;
import zombie.commands.CommandName;
import zombie.commands.RequiredRight;
import zombie.core.raknet.UdpConnection;
import zombie.network.GameServer;
import zombie.network.ServerMap;

@CommandName(
   name = "save"
)
@CommandHelp(
   helpText = "UI_ServerOptionDesc_Save"
)
@RequiredRight(
   requiredRights = 32
)
public class SaveCommand extends CommandBase {
   public SaveCommand(String var1, String var2, String var3, UdpConnection var4) {
      super(var1, var2, var3, var4);
   }

   protected String Command() {
      ServerMap.instance.QueueSaveAll();
      GameServer.PauseAllClients();
      return "World saved";
   }
}

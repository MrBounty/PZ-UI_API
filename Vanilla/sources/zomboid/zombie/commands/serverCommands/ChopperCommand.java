package zombie.commands.serverCommands;

import zombie.commands.CommandArgs;
import zombie.commands.CommandBase;
import zombie.commands.CommandHelp;
import zombie.commands.CommandName;
import zombie.commands.RequiredRight;
import zombie.core.logger.LoggerManager;
import zombie.core.raknet.UdpConnection;
import zombie.iso.IsoWorld;

@CommandName(
   name = "chopper"
)
@CommandArgs(
   optional = "([a-zA-Z0-9.-]*[a-zA-Z][a-zA-Z0-9_.-]*)"
)
@CommandHelp(
   helpText = "UI_ServerOptionDesc_Chopper"
)
@RequiredRight(
   requiredRights = 60
)
public class ChopperCommand extends CommandBase {
   public ChopperCommand(String var1, String var2, String var3, UdpConnection var4) {
      super(var1, var2, var3, var4);
   }

   protected String Command() {
      String var1;
      if (this.getCommandArgsCount() == 1) {
         if ("stop".equals(this.getCommandArg(0))) {
            IsoWorld.instance.helicopter.deactivate();
            var1 = "Chopper deactivated";
         } else if ("start".equals(this.getCommandArg(0))) {
            IsoWorld.instance.helicopter.pickRandomTarget();
            var1 = "Chopper activated";
         } else {
            var1 = this.getHelp();
         }
      } else {
         IsoWorld.instance.helicopter.pickRandomTarget();
         var1 = "Chopper launched";
      }

      LoggerManager.getLogger("admin").write(this.getExecutorUsername() + " did chopper");
      return var1;
   }
}

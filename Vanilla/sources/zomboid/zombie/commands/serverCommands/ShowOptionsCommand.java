package zombie.commands.serverCommands;

import java.util.Iterator;
import zombie.commands.CommandBase;
import zombie.commands.CommandHelp;
import zombie.commands.CommandName;
import zombie.commands.RequiredRight;
import zombie.core.raknet.UdpConnection;
import zombie.network.ServerOptions;

@CommandName(
   name = "showoptions"
)
@CommandHelp(
   helpText = "UI_ServerOptionDesc_ShowOptions"
)
@RequiredRight(
   requiredRights = 61
)
public class ShowOptionsCommand extends CommandBase {
   public ShowOptionsCommand(String var1, String var2, String var3, UdpConnection var4) {
      super(var1, var2, var3, var4);
   }

   protected String Command() {
      Iterator var1 = ServerOptions.instance.getPublicOptions().iterator();
      String var2 = null;
      String var3 = " <LINE> ";
      if (this.connection == null) {
         var3 = "\n";
      }

      String var4 = "List of Server Options:" + var3;

      while(var1.hasNext()) {
         var2 = (String)var1.next();
         if (!var2.equals("ServerWelcomeMessage")) {
            var4 = var4 + "* " + var2 + "=" + ServerOptions.instance.getOptionByName(var2).asConfigOption().getValueAsString() + var3;
         }
      }

      var4 = var4 + "* ServerWelcomeMessage=" + ServerOptions.instance.ServerWelcomeMessage.getValue();
      return var4;
   }
}

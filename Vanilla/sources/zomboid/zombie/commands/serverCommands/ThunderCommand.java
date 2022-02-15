package zombie.commands.serverCommands;

import zombie.commands.AltCommandArgs;
import zombie.commands.CommandArgs;
import zombie.commands.CommandBase;
import zombie.commands.CommandHelp;
import zombie.commands.CommandName;
import zombie.commands.DisabledCommand;
import zombie.commands.RequiredRight;
import zombie.core.logger.LoggerManager;
import zombie.core.raknet.UdpConnection;
import zombie.iso.weather.ClimateManager;

@DisabledCommand
@CommandName(
   name = "thunder"
)
@AltCommandArgs({@CommandArgs(
   required = {"(start)"},
   argName = "starts thunder"
), @CommandArgs(
   required = {"(stop)"},
   argName = "stops thunder"
)})
@CommandHelp(
   helpText = "UI_ServerOptionDesc_Thunder"
)
@RequiredRight(
   requiredRights = 60
)
public class ThunderCommand extends CommandBase {
   public static final String startThunder = "starts thunder";
   public static final String stopThunder = "stops thunder";

   public ThunderCommand(String var1, String var2, String var3, UdpConnection var4) {
      super(var1, var2, var3, var4);
   }

   protected String Command() {
      if ("starts thunder".equals(this.argsName)) {
         ClimateManager.getInstance().transmitServerTriggerStorm(24.0F);
         LoggerManager.getLogger("admin").write(this.getExecutorUsername() + " thunder start");
         return "Thunder started";
      } else if ("stops thunder".equals(this.argsName)) {
         ClimateManager.getInstance().transmitServerStopWeather();
         LoggerManager.getLogger("admin").write(this.getExecutorUsername() + " thunder stop");
         return "Thunder stopped";
      } else {
         LoggerManager.getLogger("admin").write(this.getExecutorUsername() + " missing/unknown argument to /thunder");
         return "missing/unknown argument to /thunder";
      }
   }
}

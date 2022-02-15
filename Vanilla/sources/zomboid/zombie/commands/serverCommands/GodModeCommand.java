package zombie.commands.serverCommands;

import zombie.characters.IsoPlayer;
import zombie.commands.AltCommandArgs;
import zombie.commands.CommandArgs;
import zombie.commands.CommandBase;
import zombie.commands.CommandHelp;
import zombie.commands.CommandName;
import zombie.commands.CommandNames;
import zombie.commands.RequiredRight;
import zombie.core.logger.LoggerManager;
import zombie.core.logger.ZLogger;
import zombie.core.raknet.UdpConnection;
import zombie.network.GameServer;

@CommandNames({@CommandName(
   name = "godmod"
), @CommandName(
   name = "godmode"
)})
@AltCommandArgs({@CommandArgs(
   required = {"(.+)"},
   optional = "(-true|-false)"
), @CommandArgs(
   optional = "(-true|-false)"
)})
@CommandHelp(
   helpText = "UI_ServerOptionDesc_GodMod"
)
@RequiredRight(
   requiredRights = 61
)
public class GodModeCommand extends CommandBase {
   public GodModeCommand(String var1, String var2, String var3, UdpConnection var4) {
      super(var1, var2, var3, var4);
   }

   protected String Command() {
      String var1 = this.getExecutorUsername();
      String var2 = this.getCommandArg(0);
      String var3 = this.getCommandArg(1);
      if (this.getCommandArgsCount() == 2 || this.getCommandArgsCount() == 1 && !var2.equals("-true") && !var2.equals("-false")) {
         var1 = var2;
         if (this.connection != null && this.connection.accessLevel.equals("observer") && !var2.equals(var2)) {
            return "An Observer can only toggle god mode on himself";
         }
      }

      IsoPlayer var4 = GameServer.getPlayerByUserNameForCommand(var1);
      if (var4 != null) {
         var1 = var4.getDisplayName();
         if (var3 != null) {
            var4.setGodMod("-true".equals(var3));
         } else {
            var4.setGodMod(!var4.isGodMod());
         }

         GameServer.sendPlayerExtraInfo(var4, this.connection);
         ZLogger var10000;
         String var10001;
         if (var4.isGodMod()) {
            var10000 = LoggerManager.getLogger("admin");
            var10001 = this.getExecutorUsername();
            var10000.write(var10001 + " enabled godmode on " + var1);
            return "User " + var1 + " is now invincible.";
         } else {
            var10000 = LoggerManager.getLogger("admin");
            var10001 = this.getExecutorUsername();
            var10000.write(var10001 + " disabled godmode on " + var1);
            return "User " + var1 + " is no more invincible.";
         }
      } else {
         return "User " + var1 + " not found.";
      }
   }
}

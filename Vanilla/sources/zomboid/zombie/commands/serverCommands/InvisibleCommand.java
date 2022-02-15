package zombie.commands.serverCommands;

import zombie.characters.IsoPlayer;
import zombie.commands.AltCommandArgs;
import zombie.commands.CommandArgs;
import zombie.commands.CommandBase;
import zombie.commands.CommandHelp;
import zombie.commands.CommandName;
import zombie.commands.RequiredRight;
import zombie.core.logger.LoggerManager;
import zombie.core.logger.ZLogger;
import zombie.core.raknet.UdpConnection;
import zombie.network.GameServer;

@CommandName(
   name = "invisible"
)
@AltCommandArgs({@CommandArgs(
   required = {"(.+)"},
   optional = "(-true|-false)"
), @CommandArgs(
   optional = "(-true|-false)"
)})
@CommandHelp(
   helpText = "UI_ServerOptionDesc_Invisible"
)
@RequiredRight(
   requiredRights = 61
)
public class InvisibleCommand extends CommandBase {
   public InvisibleCommand(String var1, String var2, String var3, UdpConnection var4) {
      super(var1, var2, var3, var4);
   }

   protected String Command() {
      String var1 = this.getExecutorUsername();
      String var2 = this.getCommandArg(0);
      String var3 = this.getCommandArg(1);
      if (this.getCommandArgsCount() == 2 || this.getCommandArgsCount() == 1 && !var2.equals("-true") && !var2.equals("-false")) {
         var1 = var2;
         if (this.connection.accessLevel.equals("observer") && !var2.equals(this.getExecutorUsername())) {
            return "An Observer can only toggle invisible on himself";
         }
      }

      boolean var4 = false;
      boolean var5 = true;
      if ("-false".equals(var3)) {
         var5 = false;
         var4 = true;
      } else if ("-true".equals(var3)) {
         var4 = true;
      }

      IsoPlayer var6 = GameServer.getPlayerByUserNameForCommand(var1);
      if (var6 != null) {
         if (!var4) {
            var5 = !var6.isInvisible();
         }

         var1 = var6.getDisplayName();
         if (var4) {
            var6.setInvisible(var5);
         } else {
            var6.setInvisible(!var6.isInvisible());
            var5 = var6.isInvisible();
         }

         var6.setGhostMode(var5);
         GameServer.sendPlayerExtraInfo(var6, this.connection);
         ZLogger var10000;
         String var10001;
         if (var5) {
            var10000 = LoggerManager.getLogger("admin");
            var10001 = this.getExecutorUsername();
            var10000.write(var10001 + " enabled invisibility on " + var1);
            return "User " + var1 + " is now invisible.";
         } else {
            var10000 = LoggerManager.getLogger("admin");
            var10001 = this.getExecutorUsername();
            var10000.write(var10001 + " disabled invisibility on " + var1);
            return "User " + var1 + " is no more invisible.";
         }
      } else {
         return "User " + var1 + " not found.";
      }
   }
}

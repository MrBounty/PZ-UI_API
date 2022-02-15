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
import zombie.core.raknet.VoiceManager;
import zombie.network.GameServer;

@CommandName(
   name = "voiceban"
)
@AltCommandArgs({@CommandArgs(
   required = {"(.+)"},
   optional = "(-true|-false)"
), @CommandArgs(
   optional = "(-true|-false)"
)})
@CommandHelp(
   helpText = "UI_ServerOptionDesc_VoiceBan"
)
@RequiredRight(
   requiredRights = 36
)
public class VoiceBanCommand extends CommandBase {
   public VoiceBanCommand(String var1, String var2, String var3, UdpConnection var4) {
      super(var1, var2, var3, var4);
   }

   protected String Command() {
      String var1 = this.getExecutorUsername();
      if (this.getCommandArgsCount() == 2 || this.getCommandArgsCount() == 1 && !this.getCommandArg(0).equals("-true") && !this.getCommandArg(0).equals("-false")) {
         var1 = this.getCommandArg(0);
      }

      boolean var2 = true;
      if (this.getCommandArgsCount() > 0) {
         var2 = !this.getCommandArg(this.getCommandArgsCount() - 1).equals("-false");
      }

      IsoPlayer var3 = GameServer.getPlayerByUserNameForCommand(var1);
      if (var3 != null) {
         var1 = var3.getDisplayName();
         VoiceManager.instance.VMServerBan(var3.OnlineID, var2);
         ZLogger var10000;
         String var10001;
         if (var2) {
            var10000 = LoggerManager.getLogger("admin");
            var10001 = this.getExecutorUsername();
            var10000.write(var10001 + " ban voice " + var1);
            return "User " + var1 + " voice is banned.";
         } else {
            var10000 = LoggerManager.getLogger("admin");
            var10001 = this.getExecutorUsername();
            var10000.write(var10001 + " unban voice " + var1);
            return "User " + var1 + " voice is unbanned.";
         }
      } else {
         return "User " + var1 + " not found.";
      }
   }
}

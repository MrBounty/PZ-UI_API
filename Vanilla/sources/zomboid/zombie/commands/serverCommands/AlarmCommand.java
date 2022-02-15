package zombie.commands.serverCommands;

import zombie.AmbientStreamManager;
import zombie.characters.IsoPlayer;
import zombie.commands.CommandBase;
import zombie.commands.CommandHelp;
import zombie.commands.CommandName;
import zombie.commands.RequiredRight;
import zombie.core.raknet.UdpConnection;
import zombie.network.GameServer;

@CommandName(
   name = "alarm"
)
@CommandHelp(
   helpText = "UI_ServerOptionDesc_Alarm"
)
@RequiredRight(
   requiredRights = 60
)
public class AlarmCommand extends CommandBase {
   public AlarmCommand(String var1, String var2, String var3, UdpConnection var4) {
      super(var1, var2, var3, var4);
   }

   protected String Command() {
      IsoPlayer var1 = GameServer.getPlayerByUserName(this.getExecutorUsername());
      if (var1 != null && var1.getSquare() != null && var1.getSquare().getBuilding() != null) {
         var1.getSquare().getBuilding().getDef().bAlarmed = true;
         AmbientStreamManager.instance.doAlarm(var1.getSquare().getRoom().def);
         return "Alarm sounded";
      } else {
         return "Not in a room";
      }
   }
}

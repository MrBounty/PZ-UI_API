package zombie.commands.serverCommands;

import zombie.VirtualZombieManager;
import zombie.ZombieSpawnRecorder;
import zombie.characters.IsoPlayer;
import zombie.characters.IsoZombie;
import zombie.commands.CommandArgs;
import zombie.commands.CommandBase;
import zombie.commands.CommandHelp;
import zombie.commands.CommandName;
import zombie.commands.RequiredRight;
import zombie.core.Rand;
import zombie.core.logger.LoggerManager;
import zombie.core.logger.ZLogger;
import zombie.core.raknet.UdpConnection;
import zombie.iso.IsoDirections;
import zombie.iso.IsoGridSquare;
import zombie.iso.IsoWorld;
import zombie.network.GameServer;

@CommandName(
   name = "createhorde"
)
@CommandArgs(
   required = {"(\\d+)"},
   optional = "(.+)"
)
@CommandHelp(
   helpText = "UI_ServerOptionDesc_CreateHorde"
)
@RequiredRight(
   requiredRights = 44
)
public class CreateHordeCommand extends CommandBase {
   public CreateHordeCommand(String var1, String var2, String var3, UdpConnection var4) {
      super(var1, var2, var3, var4);
   }

   protected String Command() {
      Integer var1 = Integer.parseInt(this.getCommandArg(0));
      String var2 = this.getCommandArg(1);
      IsoPlayer var3 = null;
      if (this.getCommandArgsCount() == 2) {
         var3 = GameServer.getPlayerByUserNameForCommand(var2);
         if (var3 == null) {
            return "User \"" + var2 + "\" not found";
         }
      } else if (this.connection != null) {
         var3 = GameServer.getAnyPlayerFromConnection(this.connection);
      }

      if (var1 == null) {
         return this.getHelp();
      } else {
         var1 = Math.min(var1, 500);
         if (var3 != null) {
            for(int var4 = 0; var4 < var1; ++var4) {
               VirtualZombieManager.instance.choices.clear();
               IsoGridSquare var5 = IsoWorld.instance.CurrentCell.getGridSquare((double)Rand.Next(var3.getX() - 10.0F, var3.getX() + 10.0F), (double)Rand.Next(var3.getY() - 10.0F, var3.getY() + 10.0F), (double)var3.getZ());
               VirtualZombieManager.instance.choices.add(var5);
               IsoZombie var6 = VirtualZombieManager.instance.createRealZombieAlways(IsoDirections.fromIndex(Rand.Next(IsoDirections.Max.index())).index(), false);
               if (var6 != null) {
                  ZombieSpawnRecorder.instance.record(var6, this.getClass().getSimpleName());
               }
            }

            ZLogger var10000 = LoggerManager.getLogger("admin");
            String var10001 = this.getExecutorUsername();
            var10000.write(var10001 + " created a horde of " + var1 + " zombies near " + var3.getX() + "," + var3.getY(), "IMPORTANT");
            return "Horde spawned.";
         } else {
            return "Specify a player to create the horde near to.";
         }
      }
   }
}

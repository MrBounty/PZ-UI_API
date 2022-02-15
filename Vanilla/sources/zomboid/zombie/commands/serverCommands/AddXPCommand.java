package zombie.commands.serverCommands;

import zombie.characters.IsoPlayer;
import zombie.characters.skills.PerkFactory;
import zombie.commands.CommandArgs;
import zombie.commands.CommandBase;
import zombie.commands.CommandHelp;
import zombie.commands.CommandName;
import zombie.commands.RequiredRight;
import zombie.core.logger.LoggerManager;
import zombie.core.network.ByteBufferWriter;
import zombie.core.raknet.UdpConnection;
import zombie.network.GameServer;
import zombie.network.PacketTypes;

@CommandName(
   name = "addxp"
)
@CommandArgs(
   required = {"(.+)", "(\\S+)"}
)
@CommandHelp(
   helpText = "UI_ServerOptionDesc_AddXp"
)
@RequiredRight(
   requiredRights = 60
)
public class AddXPCommand extends CommandBase {
   public AddXPCommand(String var1, String var2, String var3, UdpConnection var4) {
      super(var1, var2, var3, var4);
   }

   protected String Command() {
      String var1 = this.getCommandArg(0);
      String var2 = this.getCommandArg(1);
      IsoPlayer var3 = GameServer.getPlayerByUserNameForCommand(var1);
      if (var3 == null) {
         return "No such user";
      } else {
         String var4 = var3.getDisplayName();
         String var5 = null;
         boolean var6 = false;
         String[] var7 = var2.split("=", 2);
         if (var7.length != 2) {
            return this.getHelp();
         } else {
            var5 = var7[0].trim();
            if (PerkFactory.Perks.FromString(var5) == PerkFactory.Perks.MAX) {
               String var13 = this.connection == null ? "\n" : " LINE ";
               StringBuilder var14 = new StringBuilder();

               for(int var15 = 0; var15 < PerkFactory.PerkList.size(); ++var15) {
                  if (PerkFactory.PerkList.get(var15) != PerkFactory.Perks.Passiv) {
                     var14.append(PerkFactory.PerkList.get(var15));
                     if (var15 < PerkFactory.PerkList.size()) {
                        var14.append(var13);
                     }
                  }
               }

               return "List of available perks :" + var13 + var14.toString();
            } else {
               int var12;
               try {
                  var12 = Integer.parseInt(var7[1]);
               } catch (NumberFormatException var11) {
                  return this.getHelp();
               }

               IsoPlayer var8 = GameServer.getPlayerByUserNameForCommand(var4);
               if (var8 != null) {
                  var4 = var8.getDisplayName();
                  UdpConnection var9 = GameServer.getConnectionFromPlayer(var8);
                  if (var9 != null) {
                     ByteBufferWriter var10 = var9.startPacket();
                     PacketTypes.PacketType.AddXpCommand.doPacket(var10);
                     var10.putShort(var8.OnlineID);
                     var10.putInt(PerkFactory.Perks.FromString(var5).index());
                     var10.putInt(var12);
                     PacketTypes.PacketType.AddXpCommand.send(var9);
                     LoggerManager.getLogger("admin").write(this.getExecutorUsername() + " added " + var12 + " " + var5 + " xp's to " + var4);
                     return "Added " + var12 + " " + var5 + " xp's to " + var4;
                  }
               }

               return "User " + var4 + " not found.";
            }
         }
      }
   }
}

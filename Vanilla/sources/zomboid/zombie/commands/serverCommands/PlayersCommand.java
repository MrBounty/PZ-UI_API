package zombie.commands.serverCommands;

import java.util.ArrayList;
import java.util.Iterator;
import zombie.commands.CommandBase;
import zombie.commands.CommandHelp;
import zombie.commands.CommandName;
import zombie.commands.RequiredRight;
import zombie.core.raknet.UdpConnection;
import zombie.network.GameServer;

@CommandName(
   name = "players"
)
@CommandHelp(
   helpText = "UI_ServerOptionDesc_Players"
)
@RequiredRight(
   requiredRights = 61
)
public class PlayersCommand extends CommandBase {
   public PlayersCommand(String var1, String var2, String var3, UdpConnection var4) {
      super(var1, var2, var3, var4);
   }

   protected String Command() {
      ArrayList var1 = new ArrayList();

      for(int var2 = 0; var2 < GameServer.udpEngine.connections.size(); ++var2) {
         UdpConnection var3 = (UdpConnection)GameServer.udpEngine.connections.get(var2);

         for(int var4 = 0; var4 < 4; ++var4) {
            if (var3.usernames[var4] != null) {
               var1.add(var3.usernames[var4]);
            }
         }
      }

      StringBuilder var6 = new StringBuilder("Players connected (" + var1.size() + "): ");
      String var7 = " <LINE> ";
      if (this.connection == null) {
         var7 = "\n";
      }

      var6.append(var7);
      Iterator var8 = var1.iterator();

      while(var8.hasNext()) {
         String var5 = (String)var8.next();
         var6.append("-").append(var5).append(var7);
      }

      return var6.toString();
   }
}

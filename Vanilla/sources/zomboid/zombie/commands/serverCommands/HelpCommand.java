package zombie.commands.serverCommands;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeMap;
import java.util.Map.Entry;
import zombie.commands.CommandArgs;
import zombie.commands.CommandBase;
import zombie.commands.CommandHelp;
import zombie.commands.CommandName;
import zombie.commands.RequiredRight;
import zombie.core.raknet.UdpConnection;
import zombie.network.GameServer;
import zombie.network.ServerOptions;

@CommandName(
   name = "help"
)
@CommandArgs(
   optional = "(\\w+)"
)
@CommandHelp(
   helpText = "UI_ServerOptionDesc_Help"
)
@RequiredRight(
   requiredRights = 32
)
public class HelpCommand extends CommandBase {
   public HelpCommand(String var1, String var2, String var3, UdpConnection var4) {
      super(var1, var2, var3, var4);
   }

   protected String Command() {
      String var1 = this.getCommandArg(0);
      if (var1 != null) {
         Class var10 = findCommandCls(var1);
         return var10 != null ? getHelp(var10) : "Unknown command /" + var1;
      } else {
         String var2 = " <LINE> ";
         StringBuilder var3 = new StringBuilder();
         if (this.connection == null) {
            var2 = "\n";
         }

         if (!GameServer.bServer) {
            ArrayList var4 = ServerOptions.getClientCommandList(this.connection != null);
            Iterator var5 = var4.iterator();

            while(var5.hasNext()) {
               String var6 = (String)var5.next();
               var3.append(var6);
            }
         }

         var3.append("List of ").append("server").append(" commands : ");
         String var11 = "";
         TreeMap var12 = new TreeMap();
         Class[] var13 = getSubClasses();
         int var7 = var13.length;

         for(int var8 = 0; var8 < var7; ++var8) {
            Class var9 = var13[var8];
            if (!isDisabled(var9)) {
               var11 = getHelp(var9);
               if (var11 != null) {
                  var12.put(getCommandName(var9), var11);
               }
            }
         }

         Iterator var14 = var12.entrySet().iterator();

         while(var14.hasNext()) {
            Entry var15 = (Entry)var14.next();
            var3.append(var2).append("* ").append((String)var15.getKey()).append(" : ").append((String)var15.getValue());
         }

         return var3.toString();
      }
   }
}

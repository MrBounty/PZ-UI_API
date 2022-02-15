package zombie.commands.serverCommands;

import zombie.commands.CommandBase;
import zombie.commands.CommandName;
import zombie.commands.RequiredRight;
import zombie.core.raknet.UdpConnection;

@CommandName(
   name = "clear"
)
@RequiredRight(
   requiredRights = 32
)
public class ClearCommand extends CommandBase {
   public ClearCommand(String var1, String var2, String var3, UdpConnection var4) {
      super(var1, var2, var3, var4);
   }

   protected String Command() {
      String var1 = "Console cleared";
      if (this.connection == null) {
         for(int var2 = 0; var2 < 100; ++var2) {
            System.out.println();
         }
      } else {
         StringBuilder var4 = new StringBuilder();

         for(int var3 = 0; var3 < 50; ++var3) {
            var4.append("<LINE>");
         }

         String var10000 = var4.toString();
         var1 = var10000 + var1;
      }

      return var1;
   }
}

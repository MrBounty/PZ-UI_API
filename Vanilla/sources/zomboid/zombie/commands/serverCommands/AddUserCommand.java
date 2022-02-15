package zombie.commands.serverCommands;

import java.sql.SQLException;
import zombie.commands.CommandArgs;
import zombie.commands.CommandBase;
import zombie.commands.CommandHelp;
import zombie.commands.CommandName;
import zombie.commands.RequiredRight;
import zombie.core.logger.LoggerManager;
import zombie.core.logger.ZLogger;
import zombie.core.raknet.UdpConnection;
import zombie.core.secure.PZcrypt;
import zombie.network.ServerWorldDatabase;

@CommandName(
   name = "adduser"
)
@CommandArgs(
   required = {"(.+)", "(.+)"}
)
@CommandHelp(
   helpText = "UI_ServerOptionDesc_AddUser"
)
@RequiredRight(
   requiredRights = 36
)
public class AddUserCommand extends CommandBase {
   public AddUserCommand(String var1, String var2, String var3, UdpConnection var4) {
      super(var1, var2, var3, var4);
   }

   protected String Command() {
      String var1 = this.getCommandArg(0);
      String var2 = PZcrypt.hash(ServerWorldDatabase.encrypt(this.getCommandArg(1)));
      if (!ServerWorldDatabase.isValidUserName(var1)) {
         return "Invalid username \"" + var1 + "\"";
      } else {
         ZLogger var10000 = LoggerManager.getLogger("admin");
         String var10001 = this.getExecutorUsername();
         var10000.write(var10001 + " created user " + var1.trim() + " with password " + var2.trim());

         try {
            return ServerWorldDatabase.instance.addUser(var1.trim(), var2.trim());
         } catch (SQLException var4) {
            var4.printStackTrace();
            return "exception occurs";
         }
      }
   }
}

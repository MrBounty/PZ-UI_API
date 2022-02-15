package zombie.commands.serverCommands;

import zombie.commands.CommandArgs;
import zombie.commands.CommandBase;
import zombie.commands.CommandHelp;
import zombie.commands.CommandName;
import zombie.commands.RequiredRight;
import zombie.core.raknet.UdpConnection;
import zombie.network.chat.ChatServer;

@CommandName(
   name = "servermsg"
)
@CommandArgs(
   required = {"(.+)"}
)
@CommandHelp(
   helpText = "UI_ServerOptionDesc_ServerMsg"
)
@RequiredRight(
   requiredRights = 44
)
public class ServerMessageCommand extends CommandBase {
   public ServerMessageCommand(String var1, String var2, String var3, UdpConnection var4) {
      super(var1, var2, var3, var4);
   }

   protected String Command() {
      String var1 = this.getCommandArg(0);
      if (this.connection == null) {
         ChatServer.getInstance().sendServerAlertMessageToServerChat(var1);
      } else {
         String var2 = this.getExecutorUsername();
         ChatServer.getInstance().sendServerAlertMessageToServerChat(var2, var1);
      }

      return "Message sent.";
   }
}

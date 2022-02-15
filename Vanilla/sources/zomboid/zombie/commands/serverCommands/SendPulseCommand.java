package zombie.commands.serverCommands;

import zombie.commands.CommandBase;
import zombie.commands.CommandHelp;
import zombie.commands.CommandName;
import zombie.commands.RequiredRight;
import zombie.core.network.ByteBufferWriter;
import zombie.core.raknet.UdpConnection;
import zombie.network.PacketTypes;

@CommandName(
   name = "sendpulse"
)
@CommandHelp(
   helpText = "UI_ServerOptionDesc_SendPulse"
)
@RequiredRight(
   requiredRights = 32
)
public class SendPulseCommand extends CommandBase {
   public SendPulseCommand(String var1, String var2, String var3, UdpConnection var4) {
      super(var1, var2, var3, var4);
   }

   protected String Command() {
      if (this.connection != null) {
         this.connection.sendPulse = !this.connection.sendPulse;
         if (!this.connection.sendPulse) {
            ByteBufferWriter var1 = this.connection.startPacket();
            PacketTypes.PacketType.ServerPulse.doPacket(var1);
            var1.putLong(-1L);
            PacketTypes.PacketType.ServerPulse.send(this.connection);
         }

         return "Pulse " + (this.connection.sendPulse ? "on" : "off");
      } else {
         return "can't do this from the server console";
      }
   }
}

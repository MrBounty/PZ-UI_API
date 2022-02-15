package zombie.commands.serverCommands;

import java.sql.SQLException;
import zombie.commands.CommandArgs;
import zombie.commands.CommandBase;
import zombie.commands.CommandHelp;
import zombie.commands.CommandName;
import zombie.commands.RequiredRight;
import zombie.core.logger.LoggerManager;
import zombie.core.network.ByteBufferWriter;
import zombie.core.raknet.UdpConnection;
import zombie.core.znet.SteamUtils;
import zombie.network.GameServer;
import zombie.network.PacketTypes;
import zombie.network.ServerWorldDatabase;

@CommandName(
   name = "banid"
)
@CommandArgs(
   required = {"(.+)"}
)
@CommandHelp(
   helpText = "UI_ServerOptionDesc_BanSteamId"
)
@RequiredRight(
   requiredRights = 36
)
public class BanSteamIDCommand extends CommandBase {
   public BanSteamIDCommand(String var1, String var2, String var3, UdpConnection var4) {
      super(var1, var2, var3, var4);
   }

   protected String Command() throws SQLException {
      String var1 = this.getCommandArg(0);
      if (!SteamUtils.isSteamModeEnabled()) {
         return "Server is not in Steam mode";
      } else if (!SteamUtils.isValidSteamID(var1)) {
         return "Expected SteamID but got \"" + var1 + "\"";
      } else {
         LoggerManager.getLogger("admin").write(this.getExecutorUsername() + " banned SteamID " + var1, "IMPORTANT");
         ServerWorldDatabase.instance.banSteamID(var1, "", true);
         long var2 = SteamUtils.convertStringToSteamID(var1);

         for(int var4 = 0; var4 < GameServer.udpEngine.connections.size(); ++var4) {
            UdpConnection var5 = (UdpConnection)GameServer.udpEngine.connections.get(var4);
            if (var5.steamID == var2) {
               ByteBufferWriter var6 = var5.startPacket();
               PacketTypes.PacketType.Kicked.doPacket(var6);
               var6.putUTF("You have been banned from this server.");
               PacketTypes.PacketType.Kicked.send(var5);
               var5.forceDisconnect();
               break;
            }
         }

         return "SteamID " + var1 + " is now banned";
      }
   }
}

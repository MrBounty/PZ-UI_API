package zombie.commands.serverCommands;

import java.sql.SQLException;
import zombie.commands.AltCommandArgs;
import zombie.commands.CommandArgs;
import zombie.commands.CommandBase;
import zombie.commands.CommandHelp;
import zombie.commands.CommandName;
import zombie.commands.RequiredRight;
import zombie.core.logger.LoggerManager;
import zombie.core.logger.ZLogger;
import zombie.core.network.ByteBufferWriter;
import zombie.core.raknet.UdpConnection;
import zombie.core.znet.SteamUtils;
import zombie.network.GameServer;
import zombie.network.PacketTypes;
import zombie.network.ServerOptions;
import zombie.network.ServerWorldDatabase;
import zombie.network.Userlog;

@CommandName(
   name = "banuser"
)
@AltCommandArgs({@CommandArgs(
   required = {"(.+)"}
), @CommandArgs(
   required = {"(.+)", "-r", "(.+)"}
)})
@CommandHelp(
   helpText = "UI_ServerOptionDesc_BanUser"
)
@RequiredRight(
   requiredRights = 36
)
public class BanUserCommand extends CommandBase {
   private String reason = "";

   public BanUserCommand(String var1, String var2, String var3, UdpConnection var4) {
      super(var1, var2, var3, var4);
   }

   protected String Command() throws SQLException {
      String var1 = this.getCommandArg(0);
      if (this.hasOptionalArg(1)) {
         this.reason = this.getCommandArg(1);
      }

      String var2 = ServerWorldDatabase.instance.banUser(var1, true);
      ServerWorldDatabase.instance.addUserlog(var1, Userlog.UserlogType.Banned, this.reason, this.getExecutorUsername(), 1);
      ZLogger var10000 = LoggerManager.getLogger("admin");
      String var10001 = this.getExecutorUsername();
      var10000.write(var10001 + " banned user " + var1 + (this.reason != null ? this.reason : ""), "IMPORTANT");
      boolean var3 = false;

      for(int var4 = 0; var4 < GameServer.udpEngine.connections.size(); ++var4) {
         UdpConnection var5 = (UdpConnection)GameServer.udpEngine.connections.get(var4);
         if (var5.username.equals(var1)) {
            var3 = true;
            if (SteamUtils.isSteamModeEnabled()) {
               LoggerManager.getLogger("admin").write(this.getExecutorUsername() + " banned steamid " + var5.steamID + "(" + var5.username + ")" + (this.reason != null ? this.reason : ""), "IMPORTANT");
               String var6 = SteamUtils.convertSteamIDToString(var5.steamID);
               ServerWorldDatabase.instance.banSteamID(var6, this.reason, true);
            } else {
               LoggerManager.getLogger("admin").write(this.getExecutorUsername() + " banned ip " + var5.ip + "(" + var5.username + ")" + (this.reason != null ? this.reason : ""), "IMPORTANT");
               ServerWorldDatabase.instance.banIp(var5.ip, var1, this.reason, true);
            }

            ByteBufferWriter var7 = var5.startPacket();
            PacketTypes.PacketType.Kicked.doPacket(var7);
            if ("".equals(this.reason)) {
               var7.putUTF("You have been banned from this server.");
            } else {
               var7.putUTF("You have been banned from this server by reason: " + this.reason);
            }

            PacketTypes.PacketType.Kicked.send(var5);
            var5.forceDisconnect();
            break;
         }
      }

      if (var3 && ServerOptions.instance.BanKickGlobalSound.getValue()) {
         GameServer.PlaySoundAtEveryPlayer("Thunder");
      }

      return var2;
   }
}

package zombie.commands.serverCommands;

import zombie.characters.IsoPlayer;
import zombie.commands.AltCommandArgs;
import zombie.commands.CommandArgs;
import zombie.commands.CommandBase;
import zombie.commands.CommandHelp;
import zombie.commands.CommandName;
import zombie.commands.CommandNames;
import zombie.commands.RequiredRight;
import zombie.core.logger.LoggerManager;
import zombie.core.logger.ZLogger;
import zombie.core.network.ByteBufferWriter;
import zombie.core.raknet.UdpConnection;
import zombie.network.GameServer;
import zombie.network.PacketTypes;

@CommandNames({@CommandName(
   name = "teleport"
), @CommandName(
   name = "tp"
)})
@AltCommandArgs({@CommandArgs(
   required = {"(.+)"},
   argName = "just port to user"
), @CommandArgs(
   required = {"(.+)", "(.+)"},
   argName = "teleport user1 to user 2"
)})
@CommandHelp(
   helpText = "UI_ServerOptionDesc_Teleport"
)
@RequiredRight(
   requiredRights = 61
)
public class TeleportCommand extends CommandBase {
   public static final String justToUser = "just port to user";
   public static final String portUserToUser = "teleport user1 to user 2";
   private String username1;
   private String username2;

   public TeleportCommand(String var1, String var2, String var3, UdpConnection var4) {
      super(var1, var2, var3, var4);
   }

   protected String Command() {
      String var1 = this.argsName;
      byte var2 = -1;
      switch(var1.hashCode()) {
      case -1662648219:
         if (var1.equals("just port to user")) {
            var2 = 0;
         }
         break;
      case -1222311533:
         if (var1.equals("teleport user1 to user 2")) {
            var2 = 1;
         }
      }

      switch(var2) {
      case 0:
         this.username1 = this.getCommandArg(0);
         return this.TeleportMeToUser();
      case 1:
         this.username1 = this.getCommandArg(0);
         this.username2 = this.getCommandArg(1);
         return this.TeleportUser1ToUser2();
      default:
         return this.CommandArgumentsNotMatch();
      }
   }

   private String TeleportMeToUser() {
      if (this.connection == null) {
         return "Need player to teleport to, ex /teleport user1 user2";
      } else {
         IsoPlayer var1 = GameServer.getPlayerByUserNameForCommand(this.username1);
         if (var1 != null) {
            this.username1 = var1.getDisplayName();
            ByteBufferWriter var2 = this.connection.startPacket();
            PacketTypes.PacketType.Teleport.doPacket(var2);
            var2.putByte((byte)0);
            var2.putFloat(var1.getX());
            var2.putFloat(var1.getY());
            var2.putFloat(var1.getZ());
            PacketTypes.PacketType.Teleport.send(this.connection);
            ZLogger var10000 = LoggerManager.getLogger("admin");
            String var10001 = this.getExecutorUsername();
            var10000.write(var10001 + " teleport to " + this.username1);
            return "teleported to " + this.username1 + " please wait two seconds to show the map around you.";
         } else {
            return "Can't find player " + this.username1;
         }
      }
   }

   private String TeleportUser1ToUser2() {
      if (this.getAccessLevel() == 1 && !this.username1.equals(this.getExecutorUsername())) {
         return "An Observer can only teleport himself";
      } else {
         IsoPlayer var1 = GameServer.getPlayerByUserNameForCommand(this.username1);
         IsoPlayer var2 = GameServer.getPlayerByUserNameForCommand(this.username2);
         if (var1 == null) {
            return "Can't find player " + this.username1;
         } else if (var2 == null) {
            return "Can't find player " + this.username2;
         } else {
            this.username1 = var1.getDisplayName();
            this.username2 = var2.getDisplayName();
            UdpConnection var3 = GameServer.getConnectionFromPlayer(var1);
            if (var3 == null) {
               return "No connection for player " + this.username1;
            } else {
               ByteBufferWriter var4 = var3.startPacket();
               PacketTypes.PacketType.Teleport.doPacket(var4);
               var4.putByte((byte)var1.PlayerIndex);
               var4.putFloat(var2.getX());
               var4.putFloat(var2.getY());
               var4.putFloat(var2.getZ());
               PacketTypes.PacketType.Teleport.send(var3);
               ZLogger var10000 = LoggerManager.getLogger("admin");
               String var10001 = this.getExecutorUsername();
               var10000.write(var10001 + " teleported " + this.username1 + " to " + this.username2);
               return "teleported " + this.username1 + " to " + this.username2;
            }
         }
      }
   }

   private String CommandArgumentsNotMatch() {
      return this.getHelp();
   }
}

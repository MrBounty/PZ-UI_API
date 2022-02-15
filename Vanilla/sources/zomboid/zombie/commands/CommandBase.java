package zombie.commands;

import java.lang.annotation.Annotation;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import zombie.commands.serverCommands.AddAllToWhiteListCommand;
import zombie.commands.serverCommands.AddItemCommand;
import zombie.commands.serverCommands.AddUserCommand;
import zombie.commands.serverCommands.AddUserToWhiteListCommand;
import zombie.commands.serverCommands.AddVehicleCommand;
import zombie.commands.serverCommands.AddXPCommand;
import zombie.commands.serverCommands.AlarmCommand;
import zombie.commands.serverCommands.BanSteamIDCommand;
import zombie.commands.serverCommands.BanUserCommand;
import zombie.commands.serverCommands.ChangeOptionCommand;
import zombie.commands.serverCommands.ChopperCommand;
import zombie.commands.serverCommands.ClearCommand;
import zombie.commands.serverCommands.ConnectionsCommand;
import zombie.commands.serverCommands.CreateHorde2Command;
import zombie.commands.serverCommands.CreateHordeCommand;
import zombie.commands.serverCommands.DebugPlayerCommand;
import zombie.commands.serverCommands.GodModeCommand;
import zombie.commands.serverCommands.GrantAdminCommand;
import zombie.commands.serverCommands.GunShotCommand;
import zombie.commands.serverCommands.HelpCommand;
import zombie.commands.serverCommands.InvisibleCommand;
import zombie.commands.serverCommands.KickUserCommand;
import zombie.commands.serverCommands.NoClipCommand;
import zombie.commands.serverCommands.PlayersCommand;
import zombie.commands.serverCommands.QuitCommand;
import zombie.commands.serverCommands.ReleaseSafehouseCommand;
import zombie.commands.serverCommands.ReloadLuaCommand;
import zombie.commands.serverCommands.ReloadOptionsCommand;
import zombie.commands.serverCommands.RemoveAdminCommand;
import zombie.commands.serverCommands.RemoveUserFromWhiteList;
import zombie.commands.serverCommands.RemoveZombiesCommand;
import zombie.commands.serverCommands.ReplayCommands;
import zombie.commands.serverCommands.SaveCommand;
import zombie.commands.serverCommands.SendPulseCommand;
import zombie.commands.serverCommands.ServerMessageCommand;
import zombie.commands.serverCommands.SetAccessLevelCommand;
import zombie.commands.serverCommands.ShowOptionsCommand;
import zombie.commands.serverCommands.StartRainCommand;
import zombie.commands.serverCommands.StopRainCommand;
import zombie.commands.serverCommands.TeleportCommand;
import zombie.commands.serverCommands.TeleportToCommand;
import zombie.commands.serverCommands.ThunderCommand;
import zombie.commands.serverCommands.UnbanSteamIDCommand;
import zombie.commands.serverCommands.UnbanUserCommand;
import zombie.commands.serverCommands.VoiceBanCommand;
import zombie.core.Translator;
import zombie.core.raknet.UdpConnection;

public abstract class CommandBase {
   private final int playerType;
   private final String username;
   private final String command;
   private String[] commandArgs;
   private boolean parsingSuccessful = false;
   private boolean parsed = false;
   private String message = "";
   protected final UdpConnection connection;
   protected String argsName = "default args name. Nothing match";
   protected static final String defaultArgsName = "default args name. Nothing match";
   private static Class[] childrenClasses = new Class[]{SaveCommand.class, ServerMessageCommand.class, ConnectionsCommand.class, AddUserCommand.class, GrantAdminCommand.class, RemoveAdminCommand.class, DebugPlayerCommand.class, QuitCommand.class, AlarmCommand.class, ChopperCommand.class, AddAllToWhiteListCommand.class, KickUserCommand.class, TeleportCommand.class, TeleportToCommand.class, ReleaseSafehouseCommand.class, StartRainCommand.class, StopRainCommand.class, ThunderCommand.class, GunShotCommand.class, ReloadOptionsCommand.class, BanUserCommand.class, BanSteamIDCommand.class, UnbanUserCommand.class, UnbanSteamIDCommand.class, AddUserToWhiteListCommand.class, RemoveUserFromWhiteList.class, ChangeOptionCommand.class, ShowOptionsCommand.class, GodModeCommand.class, VoiceBanCommand.class, NoClipCommand.class, InvisibleCommand.class, HelpCommand.class, ClearCommand.class, PlayersCommand.class, AddItemCommand.class, AddXPCommand.class, AddVehicleCommand.class, CreateHordeCommand.class, CreateHorde2Command.class, ReloadLuaCommand.class, RemoveZombiesCommand.class, SendPulseCommand.class, SetAccessLevelCommand.class, ReplayCommands.class};

   public static Class[] getSubClasses() {
      return childrenClasses;
   }

   public static Class findCommandCls(String var0) {
      Class[] var2 = childrenClasses;
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         Class var5 = var2[var4];
         if (!isDisabled(var5)) {
            CommandName[] var1 = (CommandName[])var5.getAnnotationsByType(CommandName.class);
            CommandName[] var7 = var1;
            int var8 = var1.length;

            for(int var9 = 0; var9 < var8; ++var9) {
               CommandName var10 = var7[var9];
               Pattern var6 = Pattern.compile("^" + var10.name() + "\\b", 2);
               if (var6.matcher(var0).find()) {
                  return var5;
               }
            }
         }
      }

      return null;
   }

   public static String getHelp(Class var0) {
      CommandHelp var1 = (CommandHelp)getAnnotation(CommandHelp.class, var0);
      if (var1 == null) {
         return null;
      } else if (var1.shouldTranslated()) {
         String var2 = var1.helpText();
         return Translator.getText(var2);
      } else {
         return var1.helpText();
      }
   }

   public static String getCommandName(Class var0) {
      Annotation[] var1 = var0.getAnnotationsByType(CommandName.class);
      return ((CommandName)var1[0]).name();
   }

   public static boolean isDisabled(Class var0) {
      DisabledCommand var1 = (DisabledCommand)getAnnotation(DisabledCommand.class, var0);
      return var1 != null;
   }

   public static int accessLevelToInt(String var0) {
      byte var2 = -1;
      switch(var0.hashCode()) {
      case -2004703995:
         if (var0.equals("moderator")) {
            var2 = 2;
         }
         break;
      case 3302:
         if (var0.equals("gm")) {
            var2 = 4;
         }
         break;
      case 92668751:
         if (var0.equals("admin")) {
            var2 = 0;
         }
         break;
      case 348607190:
         if (var0.equals("observer")) {
            var2 = 1;
         }
         break;
      case 530022739:
         if (var0.equals("overseer")) {
            var2 = 3;
         }
      }

      switch(var2) {
      case 0:
         return 32;
      case 1:
         return 1;
      case 2:
         return 4;
      case 3:
         return 8;
      case 4:
         return 16;
      default:
         return 2;
      }
   }

   protected CommandBase(String var1, String var2, String var3, UdpConnection var4) {
      this.username = var1;
      this.command = var3;
      this.connection = var4;
      this.playerType = accessLevelToInt(var2);
      ArrayList var5 = new ArrayList();
      Matcher var6 = Pattern.compile("([^\"]\\S*|\".*?\")\\s*").matcher(var3);

      while(var6.find()) {
         var5.add(var6.group(1).replace("\"", ""));
      }

      this.commandArgs = new String[var5.size() - 1];

      for(int var7 = 1; var7 < var5.size(); ++var7) {
         this.commandArgs[var7 - 1] = (String)var5.get(var7);
      }

   }

   public String Execute() throws SQLException {
      return this.canBeExecuted() ? this.Command() : this.message;
   }

   public boolean canBeExecuted() {
      if (this.parsed) {
         return this.parsingSuccessful;
      } else if (!this.PlayerSatisfyRequiredRights()) {
         this.message = this.playerHasNoRightError();
         return false;
      } else {
         this.parsingSuccessful = this.parseCommand();
         return this.parsingSuccessful;
      }
   }

   public boolean isCommandComeFromServerConsole() {
      return this.connection == null;
   }

   protected RequiredRight getRequiredRights() {
      return (RequiredRight)this.getClass().getAnnotation(RequiredRight.class);
   }

   protected CommandArgs[] getCommandArgVariants() {
      Class var1 = this.getClass();
      return (CommandArgs[])var1.getAnnotationsByType(CommandArgs.class);
   }

   public boolean hasHelp() {
      Class var1 = this.getClass();
      CommandHelp var2 = (CommandHelp)var1.getAnnotation(CommandHelp.class);
      return var2 != null;
   }

   protected String getHelp() {
      Class var1 = this.getClass();
      return getHelp(var1);
   }

   public String getCommandArg(Integer var1) {
      return this.commandArgs != null && var1 >= 0 && var1 < this.commandArgs.length ? this.commandArgs[var1] : null;
   }

   public boolean hasOptionalArg(Integer var1) {
      return this.commandArgs != null && var1 >= 0 && var1 < this.commandArgs.length;
   }

   public int getCommandArgsCount() {
      return this.commandArgs.length;
   }

   protected abstract String Command() throws SQLException;

   public boolean parseCommand() {
      CommandArgs[] var1 = this.getCommandArgVariants();
      if (var1.length == 1 && var1[0].varArgs()) {
         this.parsed = true;
         return true;
      } else {
         boolean var2 = var1.length != 0 && this.commandArgs.length != 0 || var1.length == 0 && this.commandArgs.length == 0;
         ArrayList var3 = new ArrayList();
         CommandArgs[] var4 = var1;
         int var5 = var1.length;

         for(int var6 = 0; var6 < var5; ++var6) {
            CommandArgs var7 = var4[var6];
            var3.clear();
            this.message = "";
            int var8 = 0;
            var2 = true;

            for(int var9 = 0; var9 < var7.required().length; ++var9) {
               String var10 = var7.required()[var9];
               if (var8 == this.commandArgs.length) {
                  var2 = false;
                  break;
               }

               Matcher var11 = Pattern.compile(var10).matcher(this.commandArgs[var8]);
               if (!var11.matches()) {
                  var2 = false;
                  break;
               }

               for(int var12 = 0; var12 < var11.groupCount(); ++var12) {
                  var3.add(var11.group(var12 + 1));
               }

               ++var8;
            }

            if (var2) {
               if (var8 == this.commandArgs.length) {
                  this.argsName = var7.argName();
                  break;
               }

               if (!var7.optional().equals("no value")) {
                  Matcher var13 = Pattern.compile(var7.optional()).matcher(this.commandArgs[var8]);
                  if (var13.matches()) {
                     for(int var14 = 0; var14 < var13.groupCount(); ++var14) {
                        var3.add(var13.group(var14 + 1));
                     }
                  } else {
                     var2 = false;
                  }
               } else if (var8 < this.commandArgs.length) {
                  var2 = false;
               }

               if (var2) {
                  this.argsName = var7.argName();
                  break;
               }
            }
         }

         if (var2) {
            this.commandArgs = new String[var3.size()];
            this.commandArgs = (String[])var3.toArray(this.commandArgs);
         } else {
            this.message = this.invalidCommand();
            this.commandArgs = new String[0];
         }

         this.parsed = true;
         return var2;
      }
   }

   protected int getAccessLevel() {
      return this.playerType;
   }

   protected String getExecutorUsername() {
      return this.username;
   }

   protected String getCommand() {
      return this.command;
   }

   protected static Object getAnnotation(Class var0, Class var1) {
      return var1.getAnnotation(var0);
   }

   public boolean isParsingSuccessful() {
      if (!this.parsed) {
         this.parsingSuccessful = this.parseCommand();
      }

      return this.parsingSuccessful;
   }

   private boolean PlayerSatisfyRequiredRights() {
      RequiredRight var1 = this.getRequiredRights();
      return (this.playerType & var1.requiredRights()) != 0;
   }

   private String invalidCommand() {
      return this.hasHelp() ? this.getHelp() : Translator.getText("UI_command_arg_parse_failed", this.command);
   }

   private String playerHasNoRightError() {
      return Translator.getText("UI_has_no_right_to_execute_command", this.username, this.command);
   }
}

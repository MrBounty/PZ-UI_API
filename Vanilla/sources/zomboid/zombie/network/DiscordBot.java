package zombie.network;

import com.google.common.util.concurrent.FutureCallback;
import de.btobastian.javacord.DiscordAPI;
import de.btobastian.javacord.Javacord;
import de.btobastian.javacord.entities.Channel;
import de.btobastian.javacord.entities.message.Message;
import de.btobastian.javacord.listener.message.MessageCreateListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import zombie.debug.DebugLog;
import zombie.debug.DebugType;

public class DiscordBot {
   private DiscordAPI api;
   private Collection channels;
   private Channel current;
   private String currentChannelName;
   private String currentChannelID;
   private String name;
   private DiscordSender sender;

   public DiscordBot(String var1, DiscordSender var2) {
      this.name = var1;
      this.sender = var2;
      this.current = null;
   }

   public void connect(boolean var1, String var2, String var3, String var4) {
      if (var2 == null || var2.isEmpty()) {
         DebugLog.log(DebugType.Network, "DISCORD: token not configured");
         var1 = false;
      }

      if (!var1) {
         DebugLog.log(DebugType.Network, "*** DISCORD DISABLED ****");
         this.current = null;
      } else {
         this.api = Javacord.getApi(var2, true);
         this.api.connect(new DiscordBot.Connector());
         DebugLog.log(DebugType.Network, "*** DISCORD ENABLED ****");
         this.currentChannelName = var3;
         this.currentChannelID = var4;
      }
   }

   private void setChannel(String var1, String var2) {
      Collection var3 = this.getChannelNames();
      if ((var1 == null || var1.isEmpty()) && !var3.isEmpty()) {
         var1 = (String)var3.iterator().next();
         DebugLog.log(DebugType.Network, "DISCORD: set default channel name = \"" + var1 + "\"");
      }

      if (var2 != null && !var2.isEmpty()) {
         this.setChannelByID(var2);
      } else {
         if (var1 != null) {
            this.setChannelByName(var1);
         }

      }
   }

   public void sendMessage(String var1, String var2) {
      if (this.current != null) {
         this.current.sendMessage(var1 + ": " + var2);
         DebugLog.log(DebugType.Network, "DISCORD: User '" + var1 + "' send message: '" + var2 + "'");
      }

   }

   private Collection getChannelNames() {
      ArrayList var1 = new ArrayList();
      this.channels = this.api.getChannels();
      Iterator var2 = this.channels.iterator();

      while(var2.hasNext()) {
         Channel var3 = (Channel)var2.next();
         var1.add(var3.getName());
      }

      return var1;
   }

   private void setChannelByName(String var1) {
      this.current = null;
      Iterator var2 = this.channels.iterator();

      while(var2.hasNext()) {
         Channel var3 = (Channel)var2.next();
         if (var3.getName().equals(var1)) {
            if (this.current != null) {
               DebugLog.log(DebugType.Network, "Discord server has few channels with name '" + var1 + "'. Please, use channel ID instead");
               this.current = null;
               return;
            }

            this.current = var3;
         }
      }

      if (this.current == null) {
         DebugLog.log(DebugType.Network, "DISCORD: channel \"" + var1 + "\" is not found. Try to use channel ID instead");
      } else {
         DebugLog.log(DebugType.Network, "Discord enabled on channel: " + var1);
      }

   }

   private void setChannelByID(String var1) {
      this.current = null;
      Iterator var2 = this.channels.iterator();

      while(var2.hasNext()) {
         Channel var3 = (Channel)var2.next();
         if (var3.getId().equals(var1)) {
            DebugLog.log(DebugType.Network, "Discord enabled on channel with ID: " + var1);
            this.current = var3;
            break;
         }
      }

      if (this.current == null) {
         DebugLog.log(DebugType.Network, "DISCORD: channel with ID \"" + var1 + "\" not found");
      }

   }

   class Connector implements FutureCallback {
      public void onSuccess(DiscordAPI var1) {
         DebugLog.log(DebugType.Network, "*** DISCORD API CONNECTED ****");
         DiscordBot.this.setChannel(DiscordBot.this.currentChannelName, DiscordBot.this.currentChannelID);
         var1.registerListener(DiscordBot.this.new Listener());
         var1.updateUsername(DiscordBot.this.name);
         if (DiscordBot.this.current != null) {
            DebugLog.log(DebugType.Network, "*** DISCORD INITIALIZATION SUCCEEDED ****");
         } else {
            DebugLog.log(DebugType.Network, "*** DISCORD INITIALIZATION FAILED ****");
         }

      }

      public void onFailure(Throwable var1) {
         var1.printStackTrace();
      }
   }

   class Listener implements MessageCreateListener {
      public void onMessageCreate(DiscordAPI var1, Message var2) {
         if (DiscordBot.this.current != null) {
            if (!var1.getYourself().getId().equals(var2.getAuthor().getId())) {
               if (var2.getChannelReceiver().getId().equals(DiscordBot.this.current.getId())) {
                  DebugLog.log(DebugType.Network, "DISCORD: get message on current channel");
                  DebugType var10000 = DebugType.Network;
                  String var10001 = var2.getContent();
                  DebugLog.log(var10000, "DISCORD: send message = \"" + var10001 + "\" for " + var2.getAuthor().getName() + ")");
                  String var3 = this.replaceChannelIDByItsName(var1, var2);
                  var3 = this.removeSmilesAndImages(var3);
                  if (!var3.isEmpty() && !var3.matches("^\\s$")) {
                     DiscordBot.this.sender.sendMessageFromDiscord(var2.getAuthor().getName(), var3);
                  }
               }

            }
         }
      }

      private String replaceChannelIDByItsName(DiscordAPI var1, Message var2) {
         String var3 = var2.getContent();
         Pattern var4 = Pattern.compile("<#(\\d+)>");
         Matcher var5 = var4.matcher(var2.getContent());
         if (var5.find()) {
            for(int var6 = 1; var6 <= var5.groupCount(); ++var6) {
               Channel var7 = var1.getChannelById(var5.group(var6));
               if (var7 != null) {
                  var3 = var3.replaceAll("<#" + var5.group(var6) + ">", "#" + var7.getName());
               }
            }
         }

         return var3;
      }

      private String removeSmilesAndImages(String var1) {
         StringBuilder var2 = new StringBuilder();
         char[] var3 = var1.toCharArray();
         int var4 = var3.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            Character var6 = var3[var5];
            if (!Character.isLowSurrogate(var6) && !Character.isHighSurrogate(var6)) {
               var2.append(var6);
            }
         }

         return var2.toString();
      }
   }
}

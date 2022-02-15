package zombie.chat.defaultChats;

import java.nio.ByteBuffer;
import java.util.Iterator;
import zombie.Lua.LuaManager;
import zombie.characters.IsoPlayer;
import zombie.chat.ChatBase;
import zombie.chat.ChatMessage;
import zombie.chat.ChatSettings;
import zombie.chat.ChatTab;
import zombie.chat.ChatUtility;
import zombie.core.Color;
import zombie.core.Translator;
import zombie.core.network.ByteBufferWriter;
import zombie.debug.DebugLog;
import zombie.network.GameServer;
import zombie.network.chat.ChatType;

public class GeneralChat extends ChatBase {
   private boolean discordEnabled = false;
   private final Color discordMessageColor = new Color(114, 137, 218);

   public GeneralChat(ByteBuffer var1, ChatTab var2, IsoPlayer var3) {
      super(var1, ChatType.general, var2, var3);
      if (!this.isCustomSettings()) {
         this.setSettings(getDefaultSettings());
      }

   }

   public GeneralChat(int var1, ChatTab var2, boolean var3) {
      super(var1, ChatType.general, var2);
      this.discordEnabled = var3;
      if (!this.isCustomSettings()) {
         this.setSettings(getDefaultSettings());
      }

   }

   public GeneralChat() {
      super(ChatType.general);
   }

   public static ChatSettings getDefaultSettings() {
      ChatSettings var0 = new ChatSettings();
      var0.setBold(true);
      var0.setFontColor(new Color(255, 165, 0));
      var0.setShowAuthor(true);
      var0.setShowChatTitle(true);
      var0.setShowTimestamp(true);
      var0.setUnique(true);
      var0.setAllowColors(true);
      var0.setAllowFonts(true);
      var0.setAllowBBcode(true);
      return var0;
   }

   public void sendMessageToChatMembers(ChatMessage var1) {
      if (this.discordEnabled) {
         IsoPlayer var2 = ChatUtility.findPlayer(var1.getAuthor());
         Iterator var3;
         short var4;
         if (var1.isFromDiscord()) {
            var3 = this.members.iterator();

            while(var3.hasNext()) {
               var4 = (Short)var3.next();
               this.sendMessageToPlayer(var4, var1);
            }
         } else {
            GameServer.discordBot.sendMessage(var1.getAuthor(), var1.getText());
            var3 = this.members.iterator();

            label28:
            while(true) {
               do {
                  if (!var3.hasNext()) {
                     break label28;
                  }

                  var4 = (Short)var3.next();
               } while(var2 != null && var2.getOnlineID() == var4);

               this.sendMessageToPlayer(var4, var1);
            }
         }
      } else {
         super.sendMessageToChatMembers(var1);
      }

      DebugLog.log("New message '" + var1 + "' was sent members of chat '" + this.getID() + "'");
   }

   public void sendToDiscordGeneralChatDisabled() {
      GameServer.discordBot.sendMessage("Server", Translator.getText("UI_chat_general_chat_disabled"));
   }

   public String getMessagePrefix(ChatMessage var1) {
      StringBuilder var2 = new StringBuilder();
      if (var1.isFromDiscord()) {
         var2.append(this.getColorTag(this.discordMessageColor));
      } else {
         var2.append(this.getColorTag());
      }

      var2.append(" ").append(this.getFontSizeTag()).append(" ");
      if (this.isShowTimestamp()) {
         var2.append("[").append(LuaManager.getHourMinuteJava()).append("]");
      }

      if (this.isShowTitle()) {
         var2.append("[").append(this.getTitle()).append("]");
      }

      if (this.isShowAuthor()) {
         var2.append("[").append(var1.getAuthor()).append("]");
      }

      var2.append(": ");
      return var2.toString();
   }

   public void packMessage(ByteBufferWriter var1, ChatMessage var2) {
      super.packMessage(var1, var2);
      var1.putBoolean(var2.isFromDiscord());
   }

   public ChatMessage unpackMessage(ByteBuffer var1) {
      ChatMessage var2 = super.unpackMessage(var1);
      if (var1.get() == 1) {
         var2.makeFromDiscord();
      }

      return var2;
   }
}

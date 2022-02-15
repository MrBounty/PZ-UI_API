package zombie.chat.defaultChats;

import java.nio.ByteBuffer;
import zombie.characters.IsoPlayer;
import zombie.chat.ChatMessage;
import zombie.chat.ChatSettings;
import zombie.chat.ChatTab;
import zombie.chat.ChatUtility;
import zombie.core.Color;
import zombie.network.chat.ChatType;

public class SayChat extends RangeBasedChat {
   public SayChat(ByteBuffer var1, ChatTab var2, IsoPlayer var3) {
      super(var1, ChatType.say, var2, var3);
      if (!this.isCustomSettings()) {
         this.setSettings(getDefaultSettings());
      }

   }

   public SayChat(int var1, ChatTab var2) {
      super(var1, ChatType.say, var2);
      if (!this.isCustomSettings()) {
         this.setSettings(getDefaultSettings());
      }

   }

   public SayChat() {
      super(ChatType.say);
      this.setSettings(getDefaultSettings());
   }

   public static ChatSettings getDefaultSettings() {
      ChatSettings var0 = new ChatSettings();
      var0.setBold(true);
      var0.setFontColor(Color.white);
      var0.setShowAuthor(true);
      var0.setShowChatTitle(true);
      var0.setShowTimestamp(true);
      var0.setUnique(true);
      var0.setAllowColors(true);
      var0.setAllowChatIcons(true);
      var0.setAllowImages(true);
      var0.setAllowFonts(false);
      var0.setAllowBBcode(true);
      var0.setEqualizeLineHeights(true);
      var0.setRange(30.0F);
      var0.setZombieAttractionRange(15.0F);
      return var0;
   }

   public ChatMessage createInfoMessage(String var1) {
      ChatMessage var2 = this.createBubbleMessage(var1);
      var2.setLocal(true);
      var2.setShowInChat(false);
      return var2;
   }

   public ChatMessage createCalloutMessage(String var1) {
      ChatMessage var2 = this.createBubbleMessage(var1);
      var2.setLocal(false);
      var2.setShouldAttractZombies(true);
      return var2;
   }

   public String getMessageTextWithPrefix(ChatMessage var1) {
      String var10000 = this.getMessagePrefix(var1);
      return var10000 + " " + ChatUtility.parseStringForChatLog(var1.getTextWithReplacedParentheses());
   }
}

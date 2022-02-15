package zombie.chat.defaultChats;

import java.nio.ByteBuffer;
import zombie.Lua.LuaEventManager;
import zombie.Lua.LuaManager;
import zombie.characters.IsoPlayer;
import zombie.chat.ChatMessage;
import zombie.chat.ChatMode;
import zombie.chat.ChatSettings;
import zombie.chat.ChatTab;
import zombie.core.Color;
import zombie.core.network.ByteBufferWriter;
import zombie.network.chat.ChatType;
import zombie.ui.UIFont;

public class RadioChat extends RangeBasedChat {
   public RadioChat(ByteBuffer var1, ChatTab var2, IsoPlayer var3) {
      super(var1, ChatType.radio, var2, var3);
      if (!this.isCustomSettings()) {
         this.setSettings(getDefaultSettings());
      }

      this.customTag = "radio";
   }

   public RadioChat(int var1, ChatTab var2) {
      super(var1, ChatType.radio, var2);
      if (!this.isCustomSettings()) {
         this.setSettings(getDefaultSettings());
      }

      this.customTag = "radio";
   }

   public RadioChat() {
      super(ChatType.radio);
      this.setSettings(getDefaultSettings());
      this.customTag = "radio";
   }

   public static ChatSettings getDefaultSettings() {
      ChatSettings var0 = new ChatSettings();
      var0.setBold(true);
      var0.setFontColor(Color.lightGray);
      var0.setShowAuthor(false);
      var0.setShowChatTitle(true);
      var0.setShowTimestamp(true);
      var0.setUnique(true);
      var0.setAllowColors(true);
      var0.setAllowFonts(false);
      var0.setAllowBBcode(true);
      var0.setAllowImages(false);
      var0.setAllowChatIcons(true);
      return var0;
   }

   public ChatMessage createMessage(String var1) {
      ChatMessage var2 = super.createMessage(var1);
      if (this.getMode() == ChatMode.SinglePlayer) {
         var2.setOverHeadSpeech(true);
         var2.setShowInChat(false);
      }

      var2.setShouldAttractZombies(true);
      return var2;
   }

   public ChatMessage createBroadcastingMessage(String var1, int var2) {
      ChatMessage var3 = super.createBubbleMessage(var1);
      var3.setAuthor("");
      var3.setShouldAttractZombies(false);
      var3.setRadioChannel(var2);
      return var3;
   }

   public ChatMessage createStaticSoundMessage(String var1) {
      ChatMessage var2 = super.createBubbleMessage(var1);
      var2.setAuthor("");
      var2.setShouldAttractZombies(false);
      return var2;
   }

   protected void showInSpeechBubble(ChatMessage var1) {
      Color var2 = this.getColor();
      this.getSpeechBubble().addChatLine(var1.getText(), var2.r, var2.g, var2.b, UIFont.Dialogue, this.getRange(), this.customTag, this.isAllowBBcode(), this.isAllowImages(), this.isAllowChatIcons(), this.isAllowColors(), this.isAllowFonts(), this.isEqualizeLineHeights());
   }

   public void showMessage(ChatMessage var1) {
      if (this.isEnabled() && var1.isShowInChat() && this.hasChatTab()) {
         LuaEventManager.triggerEvent("OnAddMessage", var1, this.getTabID());
      }

   }

   public void sendToServer(ChatMessage var1) {
   }

   public ChatMessage unpackMessage(ByteBuffer var1) {
      ChatMessage var2 = super.unpackMessage(var1);
      var2.setRadioChannel(var1.getInt());
      var2.setOverHeadSpeech(var1.get() == 1);
      var2.setShowInChat(var1.get() == 1);
      var2.setShouldAttractZombies(var1.get() == 1);
      return var2;
   }

   public void packMessage(ByteBufferWriter var1, ChatMessage var2) {
      super.packMessage(var1, var2);
      var1.putInt(var2.getRadioChannel());
      var1.putBoolean(var2.isOverHeadSpeech());
      var1.putBoolean(var2.isShowInChat());
      var1.putBoolean(var2.isShouldAttractZombies());
   }

   public String getMessagePrefix(ChatMessage var1) {
      StringBuilder var2 = new StringBuilder(this.getChatSettingsTags());
      if (this.isShowTimestamp()) {
         var2.append("[").append(LuaManager.getHourMinuteJava()).append("]");
      }

      if (this.isShowTitle()) {
         var2.append("[").append(this.getTitle()).append("]");
      }

      if (this.isShowAuthor() && var1.getAuthor() != null && !var1.getAuthor().equals("")) {
         var2.append(" ").append(var1.getAuthor()).append(" ");
      } else {
         var2.append(" ").append("Radio").append(" ");
      }

      var2.append(" (").append(this.getRadioChannelStr(var1)).append("): ");
      return var2.toString();
   }

   private String getRadioChannelStr(ChatMessage var1) {
      StringBuilder var2 = new StringBuilder();
      int var3 = var1.getRadioChannel();

      int var4;
      for(var4 = var3 % 1000; var4 % 10 == 0 && var4 != 0; var4 /= 10) {
      }

      int var5 = var3 / 1000;
      var2.append(var5).append(".").append(var4).append(" MHz");
      return var2.toString();
   }
}

package zombie.chat.defaultChats;

import java.nio.ByteBuffer;
import zombie.characters.IsoPlayer;
import zombie.chat.ChatSettings;
import zombie.chat.ChatTab;
import zombie.core.Color;
import zombie.network.chat.ChatType;

public class ShoutChat extends RangeBasedChat {
   public ShoutChat(ByteBuffer var1, ChatTab var2, IsoPlayer var3) {
      super(var1, ChatType.shout, var2, var3);
      if (!this.isCustomSettings()) {
         this.setSettings(getDefaultSettings());
      }

   }

   public ShoutChat(int var1, ChatTab var2) {
      super(var1, ChatType.shout, var2);
      if (!this.isCustomSettings()) {
         this.setSettings(getDefaultSettings());
      }

   }

   public ShoutChat() {
      super(ChatType.shout);
      this.setSettings(getDefaultSettings());
   }

   public static ChatSettings getDefaultSettings() {
      ChatSettings var0 = new ChatSettings();
      var0.setBold(true);
      var0.setFontColor(new Color(255, 51, 51, 255));
      var0.setShowAuthor(true);
      var0.setShowChatTitle(true);
      var0.setShowTimestamp(true);
      var0.setUnique(true);
      var0.setAllowColors(false);
      var0.setAllowFonts(false);
      var0.setAllowBBcode(false);
      var0.setEqualizeLineHeights(true);
      var0.setRange(60.0F);
      return var0;
   }
}

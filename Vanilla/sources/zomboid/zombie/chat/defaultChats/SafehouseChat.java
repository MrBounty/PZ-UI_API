package zombie.chat.defaultChats;

import java.nio.ByteBuffer;
import zombie.characters.IsoPlayer;
import zombie.chat.ChatBase;
import zombie.chat.ChatSettings;
import zombie.chat.ChatTab;
import zombie.core.Color;
import zombie.network.chat.ChatType;

public class SafehouseChat extends ChatBase {
   public SafehouseChat(ByteBuffer var1, ChatTab var2, IsoPlayer var3) {
      super(var1, ChatType.safehouse, var2, var3);
      if (!this.isCustomSettings()) {
         this.setSettings(getDefaultSettings());
      }

   }

   public SafehouseChat(int var1, ChatTab var2) {
      super(var1, ChatType.safehouse, var2);
      if (!this.isCustomSettings()) {
         this.setSettings(getDefaultSettings());
      }

   }

   public static ChatSettings getDefaultSettings() {
      ChatSettings var0 = new ChatSettings();
      var0.setBold(true);
      var0.setFontColor(Color.lightGreen);
      var0.setShowAuthor(true);
      var0.setShowChatTitle(true);
      var0.setShowTimestamp(true);
      var0.setUnique(true);
      return var0;
   }
}

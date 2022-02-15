package zombie.chat.defaultChats;

import java.nio.ByteBuffer;
import zombie.GameWindow;
import zombie.Lua.LuaManager;
import zombie.characters.IsoPlayer;
import zombie.chat.ChatBase;
import zombie.chat.ChatMessage;
import zombie.chat.ChatSettings;
import zombie.chat.ChatTab;
import zombie.core.Color;
import zombie.core.Core;
import zombie.core.network.ByteBufferWriter;
import zombie.debug.DebugLog;
import zombie.network.chat.ChatType;

public class WhisperChat extends ChatBase {
   private String myName;
   private String companionName;
   private final String player1;
   private final String player2;
   private boolean isInited = false;

   public WhisperChat(int var1, ChatTab var2, String var3, String var4) {
      super(var1, ChatType.whisper, var2);
      if (!this.isCustomSettings()) {
         this.setSettings(getDefaultSettings());
      }

      this.player1 = var3;
      this.player2 = var4;
   }

   public WhisperChat(ByteBuffer var1, ChatTab var2, IsoPlayer var3) {
      super(var1, ChatType.whisper, var2, var3);
      if (!this.isCustomSettings()) {
         this.setSettings(getDefaultSettings());
      }

      this.player1 = GameWindow.ReadString(var1);
      this.player2 = GameWindow.ReadString(var1);
   }

   public static ChatSettings getDefaultSettings() {
      ChatSettings var0 = new ChatSettings();
      var0.setBold(true);
      var0.setFontColor(new Color(85, 26, 139));
      var0.setShowAuthor(true);
      var0.setShowChatTitle(true);
      var0.setShowTimestamp(true);
      var0.setUnique(true);
      var0.setAllowColors(true);
      var0.setAllowFonts(true);
      var0.setAllowBBcode(true);
      return var0;
   }

   public String getMessagePrefix(ChatMessage var1) {
      if (!this.isInited) {
         this.init();
      }

      StringBuilder var2 = new StringBuilder(this.getChatSettingsTags());
      if (this.isShowTimestamp()) {
         var2.append("[").append(LuaManager.getHourMinuteJava()).append("]");
      }

      if (this.isShowTitle()) {
         var2.append("[").append(this.getTitle()).append("]");
      }

      if (!this.myName.equalsIgnoreCase(var1.getAuthor())) {
         var2.append("[").append(this.companionName).append("]");
      } else {
         var2.append("[to ").append(this.companionName).append("]");
      }

      var2.append(": ");
      return var2.toString();
   }

   protected void packChat(ByteBufferWriter var1) {
      super.packChat(var1);
      var1.putUTF(this.player1);
      var1.putUTF(this.player2);
   }

   public String getCompanionName() {
      return this.companionName;
   }

   public void init() {
      if (this.player1.equals(IsoPlayer.getInstance().getUsername())) {
         this.myName = IsoPlayer.getInstance().getUsername();
         this.companionName = this.player2;
      } else {
         if (!this.player2.equals(IsoPlayer.getInstance().getUsername())) {
            if (Core.bDebug) {
               throw new RuntimeException("Wrong id");
            }

            DebugLog.log("Wrong id in whisper chat. Whisper chat not inited for players: " + this.player1 + " " + this.player2);
            return;
         }

         this.myName = IsoPlayer.getInstance().getUsername();
         this.companionName = this.player1;
      }

      this.isInited = true;
   }

   public static enum ChatStatus {
      None,
      Creating,
      PlayerNotFound;

      // $FF: synthetic method
      private static WhisperChat.ChatStatus[] $values() {
         return new WhisperChat.ChatStatus[]{None, Creating, PlayerNotFound};
      }
   }
}

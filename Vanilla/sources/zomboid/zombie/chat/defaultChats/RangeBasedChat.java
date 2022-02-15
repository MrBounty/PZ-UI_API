package zombie.chat.defaultChats;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Iterator;
import zombie.characters.IsoPlayer;
import zombie.chat.ChatBase;
import zombie.chat.ChatElement;
import zombie.chat.ChatMessage;
import zombie.chat.ChatMode;
import zombie.chat.ChatTab;
import zombie.chat.ChatUtility;
import zombie.core.Color;
import zombie.core.fonts.AngelCodeFont;
import zombie.debug.DebugLog;
import zombie.network.GameClient;
import zombie.network.chat.ChatType;
import zombie.ui.TextManager;
import zombie.ui.UIFont;

public abstract class RangeBasedChat extends ChatBase {
   private static ChatElement overHeadChat = null;
   private static HashMap players = null;
   private static String currentPlayerName = null;
   String customTag = "default";

   RangeBasedChat(ByteBuffer var1, ChatType var2, ChatTab var3, IsoPlayer var4) {
      super(var1, var2, var3, var4);
   }

   RangeBasedChat(ChatType var1) {
      super(var1);
   }

   RangeBasedChat(int var1, ChatType var2, ChatTab var3) {
      super(var1, var2, var3);
   }

   public void Init() {
      currentPlayerName = this.getChatOwnerName();
      if (players != null) {
         players.clear();
      }

      overHeadChat = this.getChatOwner().getChatElement();
   }

   public boolean isSendingToRadio() {
      return true;
   }

   public ChatMessage createMessage(String var1) {
      ChatMessage var2 = super.createMessage(var1);
      if (this.getMode() == ChatMode.SinglePlayer) {
         var2.setShowInChat(false);
      }

      var2.setOverHeadSpeech(true);
      var2.setShouldAttractZombies(true);
      return var2;
   }

   public ChatMessage createBubbleMessage(String var1) {
      ChatMessage var2 = super.createMessage(var1);
      var2.setOverHeadSpeech(true);
      var2.setShowInChat(false);
      return var2;
   }

   public void sendMessageToChatMembers(ChatMessage var1) {
      IsoPlayer var2 = ChatUtility.findPlayer(var1.getAuthor());
      if (this.getRange() == -1.0F) {
         String var10000 = this.getTitle();
         DebugLog.log("Range not set for '" + var10000 + "' chat. Message '" + var1.getText() + "' ignored");
      } else {
         Iterator var3 = this.members.iterator();

         while(var3.hasNext()) {
            short var4 = (Short)var3.next();
            IsoPlayer var5 = ChatUtility.findPlayer(var4);
            if (var5 != null && var2.getOnlineID() != var4 && ChatUtility.getDistance(var2, var5) < this.getRange()) {
               this.sendMessageToPlayer(var4, var1);
            }
         }

      }
   }

   public void showMessage(ChatMessage var1) {
      super.showMessage(var1);
      if (var1.isOverHeadSpeech()) {
         this.showInSpeechBubble(var1);
      }

   }

   protected ChatElement getSpeechBubble() {
      return overHeadChat;
   }

   protected UIFont selectFont(String var1) {
      char[] var2 = var1.toCharArray();
      UIFont var3 = UIFont.Dialogue;
      AngelCodeFont var4 = TextManager.instance.getFontFromEnum(var3);

      for(int var5 = 0; var5 < var2.length; ++var5) {
         if (var2[var5] > var4.chars.length) {
            var3 = UIFont.Medium;
            break;
         }
      }

      return var3;
   }

   protected void showInSpeechBubble(ChatMessage var1) {
      Color var2 = this.getColor();
      String var3 = var1.getAuthor();
      IsoPlayer var4 = this.getPlayer(var3);
      float var5 = var2.r;
      float var6 = var2.g;
      float var7 = var2.b;
      if (var4 != null) {
         var5 = var4.getSpeakColour().r;
         var6 = var4.getSpeakColour().g;
         var7 = var4.getSpeakColour().b;
      }

      String var8 = ChatUtility.parseStringForChatBubble(var1.getText());
      if (var3 != null && !"".equalsIgnoreCase(var3) && !var3.equalsIgnoreCase(currentPlayerName)) {
         if (!players.containsKey(var3)) {
            players.put(var3, this.getPlayer(var3));
         }

         IsoPlayer var9 = (IsoPlayer)players.get(var3);
         if (var9 != null) {
            if (var9.isDead()) {
               var9 = this.getPlayer(var3);
               players.replace(var3, var9);
            }

            var9.getChatElement().addChatLine(var8, var5, var6, var7, this.selectFont(var8), this.getRange(), this.customTag, this.isAllowBBcode(), this.isAllowImages(), this.isAllowChatIcons(), this.isAllowColors(), this.isAllowFonts(), this.isEqualizeLineHeights());
         }
      } else {
         overHeadChat.addChatLine(var8, var5, var6, var7, this.selectFont(var8), this.getRange(), this.customTag, this.isAllowBBcode(), this.isAllowImages(), this.isAllowChatIcons(), this.isAllowColors(), this.isAllowFonts(), this.isEqualizeLineHeights());
      }

   }

   private IsoPlayer getPlayer(String var1) {
      IsoPlayer var2 = GameClient.bClient ? GameClient.instance.getPlayerFromUsername(var1) : null;
      if (var2 != null) {
         return var2;
      } else {
         for(int var3 = 0; var3 < IsoPlayer.numPlayers; ++var3) {
            var2 = IsoPlayer.players[var3];
            if (var2 != null && var2.getUsername().equals(var1)) {
               return var2;
            }
         }

         return null;
      }
   }

   static {
      players = new HashMap();
   }
}

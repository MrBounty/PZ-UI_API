package zombie.chat.defaultChats;

import java.nio.ByteBuffer;
import java.util.Iterator;
import zombie.Lua.LuaEventManager;
import zombie.characters.IsoPlayer;
import zombie.chat.ChatBase;
import zombie.chat.ChatManager;
import zombie.chat.ChatMessage;
import zombie.chat.ChatSettings;
import zombie.chat.ChatTab;
import zombie.chat.ServerChatMessage;
import zombie.core.Color;
import zombie.core.Core;
import zombie.core.network.ByteBufferWriter;
import zombie.debug.DebugLog;
import zombie.network.GameClient;
import zombie.network.chat.ChatType;

public class ServerChat extends ChatBase {
   public ServerChat(ByteBuffer var1, ChatTab var2, IsoPlayer var3) {
      super(var1, ChatType.server, var2, var3);
      this.setSettings(getDefaultSettings());
   }

   public ServerChat(int var1, ChatTab var2) {
      super(var1, ChatType.server, var2);
      this.setSettings(getDefaultSettings());
   }

   public static ChatSettings getDefaultSettings() {
      ChatSettings var0 = new ChatSettings();
      var0.setBold(true);
      var0.setFontColor(new Color(0, 128, 255, 255));
      var0.setShowAuthor(false);
      var0.setShowChatTitle(true);
      var0.setShowTimestamp(false);
      var0.setAllowColors(true);
      var0.setAllowFonts(false);
      var0.setAllowBBcode(false);
      return var0;
   }

   public ChatMessage createMessage(String var1, String var2, boolean var3) {
      ChatMessage var4 = this.createMessage(var2);
      var4.setAuthor(var1);
      if (var3) {
         var4.setServerAlert(true);
      }

      return var4;
   }

   public ServerChatMessage createServerMessage(String var1, boolean var2) {
      ServerChatMessage var3 = this.createServerMessage(var1);
      var3.setServerAlert(var2);
      return var3;
   }

   public short getTabID() {
      return !GameClient.bClient ? super.getTabID() : ChatManager.getInstance().getFocusTab().getID();
   }

   public ChatMessage unpackMessage(ByteBuffer var1) {
      ChatMessage var2 = super.unpackMessage(var1);
      var2.setServerAlert(var1.get() == 1);
      var2.setServerAuthor(var1.get() == 1);
      return var2;
   }

   public void packMessage(ByteBufferWriter var1, ChatMessage var2) {
      super.packMessage(var1, var2);
      var1.putBoolean(var2.isServerAlert());
      var1.putBoolean(var2.isServerAuthor());
   }

   public String getMessagePrefix(ChatMessage var1) {
      StringBuilder var2 = new StringBuilder();
      var2.append(this.getChatSettingsTags());
      boolean var3 = false;
      if (this.isShowTitle()) {
         var2.append("[").append(this.getTitle()).append("]");
         var3 = true;
      }

      if (!var1.isServerAuthor() && this.isShowAuthor()) {
         var2.append("[").append(var1.getAuthor()).append("]");
         var3 = true;
      }

      if (var3) {
         var2.append(": ");
      }

      return var2.toString();
   }

   public String getMessageTextWithPrefix(ChatMessage var1) {
      String var10000 = this.getMessagePrefix(var1);
      return var10000 + " " + var1.getText();
   }

   public void showMessage(ChatMessage var1) {
      this.messages.add(var1);
      if (this.isEnabled()) {
         LuaEventManager.triggerEvent("OnAddMessage", var1, this.getTabID());
      }

   }

   public void sendMessageToChatMembers(ChatMessage var1) {
      Iterator var2 = this.members.iterator();

      while(var2.hasNext()) {
         short var3 = (Short)var2.next();
         this.sendMessageToPlayer(var3, var1);
      }

      if (Core.bDebug) {
         DebugLog.log("New message '" + var1 + "' was sent members of chat '" + this.getID() + "'");
      }

   }
}

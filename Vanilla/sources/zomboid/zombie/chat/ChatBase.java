package zombie.chat;

import java.nio.ByteBuffer;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import zombie.GameWindow;
import zombie.Lua.LuaEventManager;
import zombie.Lua.LuaManager;
import zombie.characters.IsoPlayer;
import zombie.core.Color;
import zombie.core.Core;
import zombie.core.Translator;
import zombie.core.network.ByteBufferWriter;
import zombie.core.raknet.UdpConnection;
import zombie.debug.DebugLog;
import zombie.network.GameClient;
import zombie.network.PacketTypes;
import zombie.network.chat.ChatType;

public abstract class ChatBase {
   private static final int ID_NOT_SET = -29048394;
   private int id;
   private final String titleID;
   private final ChatType type;
   private ChatSettings settings;
   private boolean customSettings;
   private ChatTab chatTab;
   private String translatedTitle;
   protected final ArrayList members;
   private final ArrayList justAddedMembers;
   private final ArrayList justRemovedMembers;
   protected final ArrayList messages;
   private UdpConnection serverConnection;
   private ChatMode mode;
   private IsoPlayer chatOwner;
   private final Lock memberLock;

   protected ChatBase(ChatType var1) {
      this.customSettings = false;
      this.chatTab = null;
      this.justAddedMembers = new ArrayList();
      this.justRemovedMembers = new ArrayList();
      this.memberLock = new ReentrantLock();
      this.settings = new ChatSettings();
      this.customSettings = false;
      this.messages = new ArrayList();
      this.id = -29048394;
      this.titleID = var1.getTitleID();
      this.type = var1;
      this.members = new ArrayList();
      this.mode = ChatMode.SinglePlayer;
      this.serverConnection = null;
      this.chatOwner = IsoPlayer.getInstance();
   }

   public ChatBase(ByteBuffer var1, ChatType var2, ChatTab var3, IsoPlayer var4) {
      this(var2);
      this.id = var1.getInt();
      this.customSettings = var1.get() == 1;
      if (this.customSettings) {
         this.settings = new ChatSettings(var1);
      }

      this.chatTab = var3;
      this.mode = ChatMode.ClientMultiPlayer;
      this.serverConnection = GameClient.connection;
      this.chatOwner = var4;
   }

   public ChatBase(int var1, ChatType var2, ChatTab var3) {
      this(var2);
      this.id = var1;
      this.chatTab = var3;
      this.mode = ChatMode.ServerMultiPlayer;
   }

   public boolean isEnabled() {
      return ChatUtility.chatStreamEnabled(this.type);
   }

   protected String getChatOwnerName() {
      if (this.chatOwner == null) {
         if (this.mode != ChatMode.ServerMultiPlayer) {
            if (Core.bDebug) {
               throw new NullPointerException("chat owner is null but name quired");
            }

            DebugLog.log("chat owner is null but name quired. Chat: " + this.getType());
         }

         return "";
      } else {
         return this.chatOwner.username;
      }
   }

   protected IsoPlayer getChatOwner() {
      if (this.chatOwner == null && this.mode != ChatMode.ServerMultiPlayer) {
         if (Core.bDebug) {
            throw new NullPointerException("chat owner is null");
         } else {
            DebugLog.log("chat owner is null. Chat: " + this.getType());
            return null;
         }
      } else {
         return this.chatOwner;
      }
   }

   public ChatMode getMode() {
      return this.mode;
   }

   public ChatType getType() {
      return this.type;
   }

   public int getID() {
      return this.id;
   }

   public String getTitleID() {
      return this.titleID;
   }

   public Color getColor() {
      return this.settings.getFontColor();
   }

   public short getTabID() {
      return this.chatTab.getID();
   }

   public float getRange() {
      return this.settings.getRange();
   }

   public boolean isSendingToRadio() {
      return false;
   }

   public float getZombieAttractionRange() {
      return this.settings.getZombieAttractionRange();
   }

   public void setSettings(ChatSettings var1) {
      this.settings = var1;
      this.customSettings = true;
   }

   public void setFontSize(String var1) {
      this.settings.setFontSize(var1.toLowerCase());
   }

   public void setShowTimestamp(boolean var1) {
      this.settings.setShowTimestamp(var1);
   }

   public void setShowTitle(boolean var1) {
      this.settings.setShowChatTitle(var1);
   }

   protected boolean isCustomSettings() {
      return this.customSettings;
   }

   protected boolean isAllowImages() {
      return this.settings.isAllowImages();
   }

   protected boolean isAllowChatIcons() {
      return this.settings.isAllowChatIcons();
   }

   protected boolean isAllowColors() {
      return this.settings.isAllowColors();
   }

   protected boolean isAllowFonts() {
      return this.settings.isAllowFonts();
   }

   protected boolean isAllowBBcode() {
      return this.settings.isAllowBBcode();
   }

   protected boolean isEqualizeLineHeights() {
      return this.settings.isEqualizeLineHeights();
   }

   protected boolean isShowAuthor() {
      return this.settings.isShowAuthor();
   }

   protected boolean isShowTimestamp() {
      return this.settings.isShowTimestamp();
   }

   protected boolean isShowTitle() {
      return this.settings.isShowChatTitle();
   }

   protected String getFontSize() {
      return this.settings.getFontSize().toString();
   }

   protected String getTitle() {
      if (this.translatedTitle == null) {
         this.translatedTitle = Translator.getText(this.titleID);
      }

      return this.translatedTitle;
   }

   public void close() {
      synchronized(this.memberLock) {
         ArrayList var2 = new ArrayList(this.members);
         Iterator var3 = var2.iterator();

         while(var3.hasNext()) {
            Short var4 = (Short)var3.next();
            this.leaveMember(var4);
         }

         this.members.clear();
      }
   }

   protected void packChat(ByteBufferWriter var1) {
      var1.putInt(this.type.getValue());
      var1.putShort(this.getTabID());
      var1.putInt(this.id);
      var1.putBoolean(this.customSettings);
      if (this.customSettings) {
         this.settings.pack(var1);
      }

   }

   public ChatMessage unpackMessage(ByteBuffer var1) {
      String var2 = GameWindow.ReadString(var1);
      String var3 = GameWindow.ReadString(var1);
      ChatMessage var4 = this.createMessage(var3);
      var4.setAuthor(var2);
      return var4;
   }

   public void packMessage(ByteBufferWriter var1, ChatMessage var2) {
      var1.putInt(this.id);
      var1.putUTF(var2.getAuthor());
      var1.putUTF(var2.getText());
   }

   public ChatMessage createMessage(String var1) {
      return this.createMessage(this.getChatOwnerName(), var1);
   }

   private ChatMessage createMessage(String var1, String var2) {
      ChatMessage var3 = new ChatMessage(this, var2);
      var3.setAuthor(var1);
      var3.setServerAuthor(false);
      return var3;
   }

   public ServerChatMessage createServerMessage(String var1) {
      ServerChatMessage var2 = new ServerChatMessage(this, var1);
      var2.setServerAuthor(true);
      return var2;
   }

   public void showMessage(String var1, String var2) {
      ChatMessage var3 = new ChatMessage(this, LocalDateTime.now(), var1);
      var3.setAuthor(var2);
      this.showMessage(var3);
   }

   public void showMessage(ChatMessage var1) {
      this.messages.add(var1);
      if (this.isEnabled() && var1.isShowInChat() && this.chatTab != null) {
         LuaEventManager.triggerEvent("OnAddMessage", var1, this.getTabID());
      }

   }

   public String getMessageTextWithPrefix(ChatMessage var1) {
      String var10000 = this.getMessagePrefix(var1);
      return var10000 + " " + var1.getTextWithReplacedParentheses();
   }

   public void sendMessageToChatMembers(ChatMessage var1) {
      IsoPlayer var2 = ChatUtility.findPlayer(var1.getAuthor());
      if (var2 == null) {
         DebugLog.log("Author '" + var1.getAuthor() + "' not found");
      } else {
         synchronized(this.memberLock) {
            Iterator var4 = this.members.iterator();

            while(true) {
               if (!var4.hasNext()) {
                  break;
               }

               short var5 = (Short)var4.next();
               IsoPlayer var6 = ChatUtility.findPlayer(var5);
               if (var6 != null && var2.getOnlineID() != var5) {
                  this.sendMessageToPlayer(var5, var1);
               }
            }
         }

         if (Core.bDebug) {
            DebugLog.log("New message '" + var1 + "' was sent members of chat '" + this.getID() + "'");
         }

      }
   }

   public void sendMessageToChatMembers(ServerChatMessage var1) {
      synchronized(this.memberLock) {
         Iterator var3 = this.members.iterator();

         while(true) {
            if (!var3.hasNext()) {
               break;
            }

            short var4 = (Short)var3.next();
            IsoPlayer var5 = ChatUtility.findPlayer(var4);
            if (var5 != null) {
               this.sendMessageToPlayer(var4, var1);
            }
         }
      }

      if (Core.bDebug) {
         DebugLog.log("New message '" + var1 + "' was sent members of chat '" + this.getID() + "'");
      }

   }

   public void sendMessageToPlayer(UdpConnection var1, ChatMessage var2) {
      synchronized(this.memberLock) {
         boolean var4 = false;
         short[] var5 = var1.playerIDs;
         int var6 = var5.length;

         for(int var7 = 0; var7 < var6; ++var7) {
            Short var8 = var5[var7];
            if (var4) {
               break;
            }

            var4 = this.members.contains(var8);
         }

         if (!var4) {
            throw new RuntimeException("Passed connection didn't contained member of chat");
         } else {
            this.sendChatMessageToPlayer(var1, var2);
         }
      }
   }

   public void sendMessageToPlayer(short var1, ChatMessage var2) {
      UdpConnection var3 = ChatUtility.findConnection(var1);
      if (var3 != null) {
         this.sendChatMessageToPlayer(var3, var2);
         DebugLog.log("Message '" + var2 + "' was sent to player with id '" + var1 + "' of chat '" + this.getID() + "'");
      }
   }

   public String getMessagePrefix(ChatMessage var1) {
      StringBuilder var2 = new StringBuilder(this.getChatSettingsTags());
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

   protected String getColorTag() {
      Color var1 = this.getColor();
      return this.getColorTag(var1);
   }

   protected String getColorTag(Color var1) {
      return "<RGB:" + var1.r + "," + var1.g + "," + var1.b + ">";
   }

   protected String getFontSizeTag() {
      return "<SIZE:" + this.settings.getFontSize() + ">";
   }

   protected String getChatSettingsTags() {
      String var10000 = this.getColorTag();
      return var10000 + " " + this.getFontSizeTag() + " ";
   }

   public void addMember(short var1) {
      synchronized(this.memberLock) {
         if (!this.hasMember(var1)) {
            this.members.add(var1);
            this.justAddedMembers.add(var1);
            UdpConnection var3 = ChatUtility.findConnection(var1);
            if (var3 != null) {
               this.sendPlayerJoinChatPacket(var3);
               this.chatTab.sendAddTabPacket(var3);
            } else if (Core.bDebug) {
               throw new RuntimeException("Connection should exist!");
            }
         }

      }
   }

   public void leaveMember(Short var1) {
      synchronized(this.memberLock) {
         if (this.hasMember(var1)) {
            this.justRemovedMembers.add(var1);
            UdpConnection var3 = ChatUtility.findConnection(var1);
            if (var3 != null) {
               this.sendPlayerLeaveChatPacket(var3);
            }

            this.members.remove(var1);
         }

      }
   }

   private boolean hasMember(Short var1) {
      return this.members.contains(var1);
   }

   public void removeMember(Short var1) {
      synchronized(this.memberLock) {
         if (this.hasMember(var1)) {
            this.members.remove(var1);
         }

      }
   }

   public void syncMembersByUsernames(ArrayList var1) {
      synchronized(this.memberLock) {
         this.justAddedMembers.clear();
         this.justRemovedMembers.clear();
         ArrayList var3 = new ArrayList(var1.size());
         IsoPlayer var4 = null;
         Iterator var5 = var1.iterator();

         while(var5.hasNext()) {
            String var6 = (String)var5.next();
            var4 = ChatUtility.findPlayer(var6);
            if (var4 != null) {
               var3.add(var4.getOnlineID());
            }
         }

         this.syncMembers(var3);
      }
   }

   public ArrayList getJustAddedMembers() {
      synchronized(this.memberLock) {
         return this.justAddedMembers;
      }
   }

   public ArrayList getJustRemovedMembers() {
      synchronized(this.memberLock) {
         return this.justRemovedMembers;
      }
   }

   private void syncMembers(ArrayList var1) {
      Iterator var2 = var1.iterator();

      while(var2.hasNext()) {
         Short var3 = (Short)var2.next();
         this.addMember(var3);
      }

      ArrayList var8 = new ArrayList();
      synchronized(this.memberLock) {
         Iterator var4 = this.members.iterator();

         Short var5;
         while(var4.hasNext()) {
            var5 = (Short)var4.next();
            if (!var1.contains(var5)) {
               var8.add(var5);
            }
         }

         var4 = var8.iterator();

         while(var4.hasNext()) {
            var5 = (Short)var4.next();
            this.leaveMember(var5);
         }

      }
   }

   public void sendPlayerJoinChatPacket(UdpConnection var1) {
      ByteBufferWriter var2 = var1.startPacket();
      PacketTypes.PacketType.PlayerJoinChat.doPacket(var2);
      this.packChat(var2);
      PacketTypes.PacketType.PlayerJoinChat.send(var1);
   }

   public void sendPlayerLeaveChatPacket(short var1) {
      UdpConnection var2 = ChatUtility.findConnection(var1);
      this.sendPlayerLeaveChatPacket(var2);
   }

   public void sendPlayerLeaveChatPacket(UdpConnection var1) {
      ByteBufferWriter var2 = var1.startPacket();
      PacketTypes.PacketType.PlayerLeaveChat.doPacket(var2);
      var2.putInt(this.getID());
      var2.putInt(this.getType().getValue());
      PacketTypes.PacketType.PlayerLeaveChat.send(var1);
   }

   public void sendToServer(ChatMessage var1) {
      if (this.serverConnection == null) {
         DebugLog.log("Connection to server is null in client chat");
      }

      this.sendChatMessageFromPlayer(this.serverConnection, var1);
   }

   private void sendChatMessageToPlayer(UdpConnection var1, ChatMessage var2) {
      ByteBufferWriter var3 = var1.startPacket();
      PacketTypes.PacketType.ChatMessageToPlayer.doPacket(var3);
      this.packMessage(var3, var2);
      PacketTypes.PacketType.ChatMessageToPlayer.send(var1);
   }

   private void sendChatMessageFromPlayer(UdpConnection var1, ChatMessage var2) {
      ByteBufferWriter var3 = var1.startPacket();
      PacketTypes.PacketType.ChatMessageFromPlayer.doPacket(var3);
      this.packMessage(var3, var2);
      PacketTypes.PacketType.ChatMessageFromPlayer.send(var1);
   }

   protected boolean hasChatTab() {
      return this.chatTab != null;
   }
}

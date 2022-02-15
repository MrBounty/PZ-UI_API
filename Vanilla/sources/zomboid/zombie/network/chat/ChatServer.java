package zombie.network.chat;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Stack;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import zombie.GameWindow;
import zombie.characters.Faction;
import zombie.characters.IsoPlayer;
import zombie.chat.ChatBase;
import zombie.chat.ChatMessage;
import zombie.chat.ChatTab;
import zombie.chat.ChatUtility;
import zombie.chat.ServerChatMessage;
import zombie.chat.defaultChats.AdminChat;
import zombie.chat.defaultChats.FactionChat;
import zombie.chat.defaultChats.GeneralChat;
import zombie.chat.defaultChats.RadioChat;
import zombie.chat.defaultChats.SafehouseChat;
import zombie.chat.defaultChats.SayChat;
import zombie.chat.defaultChats.ServerChat;
import zombie.chat.defaultChats.ShoutChat;
import zombie.chat.defaultChats.WhisperChat;
import zombie.core.Core;
import zombie.core.logger.LoggerManager;
import zombie.core.logger.ZLogger;
import zombie.core.network.ByteBufferWriter;
import zombie.core.raknet.UdpConnection;
import zombie.iso.areas.SafeHouse;
import zombie.network.PacketTypes;
import zombie.network.ServerOptions;

public class ChatServer {
   private static ChatServer instance = null;
   private static final Stack availableChatsID = new Stack();
   private static int lastChatId = -1;
   private static final HashMap defaultChats = new HashMap();
   private static final ConcurrentHashMap chats = new ConcurrentHashMap();
   private static final ConcurrentHashMap factionChats = new ConcurrentHashMap();
   private static final ConcurrentHashMap safehouseChats = new ConcurrentHashMap();
   private static AdminChat adminChat = null;
   private static GeneralChat generalChat = null;
   private static ServerChat serverChat = null;
   private static RadioChat radioChat = null;
   private static boolean inited = false;
   private static final HashSet players = new HashSet();
   private static final String logName = "chat";
   private static ZLogger logger;
   private static final HashMap tabs = new HashMap();
   private static final String mainTabID = "main";
   private static final String adminTabID = "admin";

   public static ChatServer getInstance() {
      if (instance == null) {
         instance = new ChatServer();
      }

      return instance;
   }

   public static boolean isInited() {
      return inited;
   }

   private ChatServer() {
   }

   public void init() {
      if (!inited) {
         LoggerManager.createLogger("chat", Core.bDebug);
         logger = LoggerManager.getLogger("chat");
         logger.write("Start chat server initialization...", "info");
         ChatTab var1 = new ChatTab((short)0, "UI_chat_main_tab_title_id");
         ChatTab var2 = new ChatTab((short)1, "UI_chat_admin_tab_title_id");
         boolean var3 = ServerOptions.getInstance().DiscordEnable.getValue();
         GeneralChat var4 = new GeneralChat(this.getNextChatID(), var1, var3);
         SayChat var5 = new SayChat(this.getNextChatID(), var1);
         ShoutChat var6 = new ShoutChat(this.getNextChatID(), var1);
         RadioChat var7 = new RadioChat(this.getNextChatID(), var1);
         AdminChat var8 = new AdminChat(this.getNextChatID(), var2);
         ServerChat var9 = new ServerChat(this.getNextChatID(), var1);
         chats.put(var4.getID(), var4);
         chats.put(var5.getID(), var5);
         chats.put(var6.getID(), var6);
         chats.put(var7.getID(), var7);
         chats.put(var8.getID(), var8);
         chats.put(var9.getID(), var9);
         defaultChats.put(var4.getType(), var4);
         defaultChats.put(var5.getType(), var5);
         defaultChats.put(var6.getType(), var6);
         defaultChats.put(var9.getType(), var9);
         defaultChats.put(var7.getType(), var7);
         tabs.put("main", var1);
         tabs.put("admin", var2);
         generalChat = var4;
         adminChat = var8;
         serverChat = var9;
         radioChat = var7;
         inited = true;
         logger.write("General chat has id = " + var4.getID(), "info");
         logger.write("Say chat has id = " + var5.getID(), "info");
         logger.write("Shout chat has id = " + var6.getID(), "info");
         logger.write("Radio chat has id = " + var7.getID(), "info");
         logger.write("Admin chat has id = " + var8.getID(), "info");
         logger.write("Server chat has id = " + serverChat.getID(), "info");
         logger.write("Chat server successfully initialized", "info");
      }
   }

   public void initPlayer(short var1) {
      logger.write("Player with id = '" + var1 + "' tries to connect", "info");
      synchronized(players) {
         if (players.contains(var1)) {
            logger.write("Player already connected!", "warning");
            return;
         }
      }

      logger.write("Adding player '" + var1 + "' to chat server", "info");
      IsoPlayer var2 = ChatUtility.findPlayer(var1);
      UdpConnection var3 = ChatUtility.findConnection(var1);
      if (var3 != null && var2 != null) {
         this.sendInitPlayerChatPacket(var3);
         this.addDefaultChats(var1);
         logger.write("Player joined to default chats", "info");
         if (var3.accessLevel.equals("admin")) {
            this.joinAdminChat(var1);
         }

         Faction var4 = Faction.getPlayerFaction(var2);
         if (var4 != null) {
            this.addMemberToFactionChat(var4.getName(), var1);
         }

         SafeHouse var5 = SafeHouse.hasSafehouse(var2);
         if (var5 != null) {
            this.addMemberToSafehouseChat(var5.getId(), var1);
         }

         ByteBufferWriter var6 = var3.startPacket();
         PacketTypes.PacketType.PlayerConnectedToChat.doPacket(var6);
         PacketTypes.PacketType.PlayerConnectedToChat.send(var3);
         synchronized(players) {
            players.add(var1);
         }

         logger.write("Player " + var2.getUsername() + "(" + var1 + ") joined to chat server successfully", "info");
      } else {
         logger.write("Player or connection is not found on server!", "error");
         logger.write((var3 == null ? "connection = null " : "") + (var2 == null ? "player = null" : ""), "error");
      }
   }

   public void processMessageFromPlayerPacket(ByteBuffer var1) {
      int var2 = var1.getInt();
      synchronized(chats) {
         ChatBase var4 = (ChatBase)chats.get(var2);
         ChatMessage var5 = var4.unpackMessage(var1);
         logger.write("Got message:" + var5, "info");
         if (!ChatUtility.chatStreamEnabled(var4.getType())) {
            logger.write("Message ignored by server because the chat disabled by server settings", "warning");
         } else {
            this.sendMessage(var5);
            logger.write("Message " + var5 + " sent to chat (id = " + var4.getID() + ") members", "info");
         }
      }
   }

   public void processPlayerStartWhisperChatPacket(ByteBuffer var1) {
      logger.write("Whisper chat starting...", "info");
      if (!ChatUtility.chatStreamEnabled(ChatType.whisper)) {
         logger.write("Message for whisper chat is ignored because whisper chat is disabled by server settings", "info");
      } else {
         String var2 = GameWindow.ReadString(var1);
         String var3 = GameWindow.ReadString(var1);
         logger.write("Player '" + var2 + "' attempt to start whispering with '" + var3 + "'", "info");
         IsoPlayer var4 = ChatUtility.findPlayer(var2);
         IsoPlayer var5 = ChatUtility.findPlayer(var3);
         if (var4 == null) {
            logger.write("Player '" + var2 + "' is not found!", "error");
            throw new RuntimeException("Player not found");
         } else if (var5 == null) {
            logger.write("Player '" + var2 + "' attempt to start whisper dialog with '" + var3 + "' but this player not found!", "info");
            UdpConnection var7 = ChatUtility.findConnection(var4.getOnlineID());
            this.sendPlayerNotFoundMessage(var7);
         } else {
            logger.write("Both players found", "info");
            WhisperChat var6 = new WhisperChat(this.getNextChatID(), (ChatTab)tabs.get("main"), var2, var3);
            var6.addMember(var4.getOnlineID());
            var6.addMember(var5.getOnlineID());
            chats.put(var6.getID(), var6);
            ZLogger var10000 = logger;
            int var10001 = var6.getID();
            var10000.write("Whisper chat (id = " + var10001 + ") between '" + var4.getUsername() + "' and '" + var5.getUsername() + "' started", "info");
         }
      }
   }

   private void sendPlayerNotFoundMessage(UdpConnection var1) {
      ByteBufferWriter var2 = var1.startPacket();
      PacketTypes.PacketType.PlayerNotFound.doPacket(var2);
      PacketTypes.PacketType.PlayerNotFound.send(var1);
      logger.write("'Player not found' packet was sent", "info");
   }

   public ChatMessage unpackChatMessage(ByteBuffer var1) {
      int var2 = var1.getInt();
      return ((ChatBase)chats.get(var2)).unpackMessage(var1);
   }

   public void disconnectPlayer(short var1) {
      logger.write("Player " + var1 + " disconnecting...", "info");
      synchronized(chats) {
         Iterator var3 = chats.values().iterator();

         while(var3.hasNext()) {
            ChatBase var4 = (ChatBase)var3.next();
            var4.removeMember(var1);
            if (var4.getType() == ChatType.whisper) {
               this.closeChat(var4.getID());
            }
         }
      }

      synchronized(players) {
         players.remove(var1);
      }

      logger.write("Disconnecting player " + var1 + " finished", "info");
   }

   private void closeChat(int var1) {
      synchronized(chats) {
         if (!chats.containsKey(var1)) {
            throw new RuntimeException("Chat '" + var1 + "' requested to close but it's not exists.");
         }

         ChatBase var3 = (ChatBase)chats.get(var1);
         var3.close();
         chats.remove(var1);
      }

      synchronized(availableChatsID) {
         availableChatsID.push(var1);
      }
   }

   public void joinAdminChat(short var1) {
      if (adminChat == null) {
         logger.write("Admin chat is null! Can't add player to it", "warning");
      } else {
         adminChat.addMember(var1);
         logger.write("Player joined admin chat", "info");
      }
   }

   public void leaveAdminChat(short var1) {
      logger.write("Player " + var1 + " are leaving admin chat...", "info");
      UdpConnection var2 = ChatUtility.findConnection(var1);
      if (adminChat == null) {
         logger.write("Admin chat is null. Can't leave it! ChatServer", "warning");
      } else if (var2 == null) {
         logger.write("Connection to player is null. Can't leave admin chat! ChatServer.leaveAdminChat", "warning");
      } else {
         adminChat.leaveMember(var1);
         ((ChatTab)tabs.get("admin")).sendRemoveTabPacket(var2);
         logger.write("Player " + var1 + " leaved admin chat", "info");
      }
   }

   public FactionChat createFactionChat(String var1) {
      logger.write("Creating faction chat '" + var1 + "'", "info");
      if (factionChats.containsKey(var1)) {
         logger.write("Faction chat '" + var1 + "' already exists!", "warning");
         return (FactionChat)factionChats.get(var1);
      } else {
         FactionChat var2 = new FactionChat(this.getNextChatID(), (ChatTab)tabs.get("main"));
         chats.put(var2.getID(), var2);
         factionChats.put(var1, var2);
         logger.write("Faction chat '" + var1 + "' created", "info");
         return var2;
      }
   }

   public SafehouseChat createSafehouseChat(String var1) {
      logger.write("Creating safehouse chat '" + var1 + "'", "info");
      if (safehouseChats.containsKey(var1)) {
         logger.write("Safehouse chat already has chat with name '" + var1 + "'", "warning");
         return (SafehouseChat)safehouseChats.get(var1);
      } else {
         SafehouseChat var2 = new SafehouseChat(this.getNextChatID(), (ChatTab)tabs.get("main"));
         chats.put(var2.getID(), var2);
         safehouseChats.put(var1, var2);
         logger.write("Safehouse chat '" + var1 + "' created", "info");
         return var2;
      }
   }

   public void removeFactionChat(String var1) {
      logger.write("Removing faction chat '" + var1 + "'...", "info");
      int var2;
      synchronized(factionChats) {
         if (!factionChats.containsKey(var1)) {
            String var8 = "Faction chat '" + var1 + "' tried to delete but it's not exists.";
            logger.write(var8, "error");
            RuntimeException var5 = new RuntimeException(var8);
            logger.write((Exception)var5);
            throw var5;
         }

         FactionChat var4 = (FactionChat)factionChats.get(var1);
         var2 = var4.getID();
         factionChats.remove(var1);
      }

      this.closeChat(var2);
      logger.write("Faction chat '" + var1 + "' removed", "info");
   }

   public void removeSafehouseChat(String var1) {
      logger.write("Removing safehouse chat '" + var1 + "'...", "info");
      int var2;
      synchronized(safehouseChats) {
         if (!safehouseChats.containsKey(var1)) {
            String var8 = "Safehouse chat '" + var1 + "' tried to delete but it's not exists.";
            logger.write(var8, "error");
            RuntimeException var5 = new RuntimeException(var8);
            logger.write((Exception)var5);
            throw var5;
         }

         SafehouseChat var4 = (SafehouseChat)safehouseChats.get(var1);
         var2 = var4.getID();
         safehouseChats.remove(var1);
      }

      this.closeChat(var2);
      logger.write("Safehouse chat '" + var1 + "' removed", "info");
   }

   public void syncFactionChatMembers(String var1, String var2, ArrayList var3) {
      logger.write("Start syncing faction chat '" + var1 + "'...", "info");
      if (var1 != null && var2 != null && var3 != null) {
         synchronized(factionChats) {
            if (!factionChats.containsKey(var1)) {
               logger.write("Faction chat '" + var1 + "' is not exist", "warning");
               return;
            }

            ArrayList var5 = new ArrayList(var3);
            var5.add(var2);
            FactionChat var6 = (FactionChat)factionChats.get(var1);
            var6.syncMembersByUsernames(var5);
            StringBuilder var7 = new StringBuilder("These members were added: ");
            Iterator var8 = var6.getJustAddedMembers().iterator();

            short var9;
            while(var8.hasNext()) {
               var9 = (Short)var8.next();
               var7.append("'").append(ChatUtility.findPlayerName(var9)).append("', ");
            }

            var7.append(". These members were removed: ");
            var8 = var6.getJustRemovedMembers().iterator();

            while(true) {
               if (!var8.hasNext()) {
                  logger.write(var7.toString(), "info");
                  break;
               }

               var9 = (Short)var8.next();
               var7.append("'").append(ChatUtility.findPlayerName(var9)).append("', ");
            }
         }

         logger.write("Syncing faction chat '" + var1 + "' finished", "info");
      } else {
         logger.write("Faction name or faction owner or players is null", "warning");
      }
   }

   public void syncSafehouseChatMembers(String var1, String var2, ArrayList var3) {
      logger.write("Start syncing safehouse chat '" + var1 + "'...", "info");
      if (var1 != null && var2 != null && var3 != null) {
         synchronized(safehouseChats) {
            if (!safehouseChats.containsKey(var1)) {
               logger.write("Safehouse chat '" + var1 + "' is not exist", "warning");
               return;
            }

            ArrayList var5 = new ArrayList(var3);
            var5.add(var2);
            SafehouseChat var6 = (SafehouseChat)safehouseChats.get(var1);
            var6.syncMembersByUsernames(var5);
            StringBuilder var7 = new StringBuilder("These members were added: ");
            Iterator var8 = var6.getJustAddedMembers().iterator();

            short var9;
            while(var8.hasNext()) {
               var9 = (Short)var8.next();
               var7.append("'").append(ChatUtility.findPlayerName(var9)).append("', ");
            }

            var7.append("These members were removed: ");
            var8 = var6.getJustRemovedMembers().iterator();

            while(true) {
               if (!var8.hasNext()) {
                  logger.write(var7.toString(), "info");
                  break;
               }

               var9 = (Short)var8.next();
               var7.append("'").append(ChatUtility.findPlayerName(var9)).append("', ");
            }
         }

         logger.write("Syncing safehouse chat '" + var1 + "' finished", "info");
      } else {
         logger.write("Safehouse name or Safehouse owner or players is null", "warning");
      }
   }

   private void addMemberToSafehouseChat(String var1, short var2) {
      if (!safehouseChats.containsKey(var1)) {
         logger.write("Safehouse chat is not initialized!", "warning");
      } else {
         synchronized(safehouseChats) {
            SafehouseChat var4 = (SafehouseChat)safehouseChats.get(var1);
            var4.addMember(var2);
         }

         logger.write("Player joined to chat of safehouse '" + var1 + "'", "info");
      }
   }

   private void addMemberToFactionChat(String var1, short var2) {
      if (!factionChats.containsKey(var1)) {
         logger.write("Faction chat is not initialized!", "warning");
      } else {
         synchronized(factionChats) {
            FactionChat var4 = (FactionChat)factionChats.get(var1);
            var4.addMember(var2);
         }

         logger.write("Player joined to chat of faction '" + var1 + "'", "info");
      }
   }

   public void sendServerAlertMessageToServerChat(String var1, String var2) {
      serverChat.sendMessageToChatMembers(serverChat.createMessage(var1, var2, true));
      logger.write("Server alert message: '" + var2 + "' by '" + var1 + "' sent.");
   }

   public void sendServerAlertMessageToServerChat(String var1) {
      serverChat.sendMessageToChatMembers(serverChat.createServerMessage(var1, true));
      logger.write("Server alert message: '" + var1 + "' sent.");
   }

   public ChatMessage createRadiostationMessage(String var1, int var2) {
      return radioChat.createBroadcastingMessage(var1, var2);
   }

   public void sendMessageToServerChat(UdpConnection var1, String var2) {
      ServerChatMessage var3 = serverChat.createServerMessage(var2, false);
      serverChat.sendMessageToPlayer(var1, var3);
   }

   public void sendMessageToServerChat(String var1) {
      ServerChatMessage var2 = serverChat.createServerMessage(var1, false);
      serverChat.sendMessageToChatMembers(var2);
   }

   public void sendMessageFromDiscordToGeneralChat(String var1, String var2) {
      if (var1 != null && var2 != null) {
         logger.write("Got message '" + var2 + "' by author '" + var1 + "' from discord");
      }

      ChatMessage var3 = generalChat.createMessage(var2);
      var3.makeFromDiscord();
      var3.setAuthor(var1);
      if (ChatUtility.chatStreamEnabled(ChatType.general)) {
         this.sendMessage(var3);
         logger.write("Message '" + var2 + "' send from discord to general chat members");
      } else {
         generalChat.sendToDiscordGeneralChatDisabled();
         logger.write("General chat disabled so error message sent to discord", "warning");
      }

   }

   private int getNextChatID() {
      synchronized(availableChatsID) {
         if (availableChatsID.isEmpty()) {
            ++lastChatId;
            availableChatsID.push(lastChatId);
         }

         return (Integer)availableChatsID.pop();
      }
   }

   private void sendMessage(ChatMessage var1) {
      synchronized(chats) {
         if (chats.containsKey(var1.getChatID())) {
            ChatBase var3 = (ChatBase)chats.get(var1.getChatID());
            var3.sendMessageToChatMembers(var1);
         }
      }
   }

   private void sendInitPlayerChatPacket(UdpConnection var1) {
      ByteBufferWriter var2 = var1.startPacket();
      PacketTypes.PacketType.InitPlayerChat.doPacket(var2);
      var2.putShort((short)tabs.size());
      Iterator var3 = tabs.values().iterator();

      while(var3.hasNext()) {
         ChatTab var4 = (ChatTab)var3.next();
         var2.putShort(var4.getID());
         var2.putUTF(var4.getTitleID());
      }

      PacketTypes.PacketType.InitPlayerChat.send(var1);
   }

   private void addDefaultChats(short var1) {
      Iterator var2 = defaultChats.entrySet().iterator();

      while(var2.hasNext()) {
         Entry var3 = (Entry)var2.next();
         ChatBase var4 = (ChatBase)var3.getValue();
         var4.addMember(var1);
      }

   }

   public void sendMessageToAdminChat(String var1) {
      ServerChatMessage var2 = adminChat.createServerMessage(var1);
      adminChat.sendMessageToChatMembers(var2);
   }
}

package zombie.chat;

import java.util.HashSet;
import zombie.core.Translator;
import zombie.core.network.ByteBufferWriter;
import zombie.core.raknet.UdpConnection;
import zombie.network.PacketTypes;

public class ChatTab {
   private short id;
   private String titleID;
   private String translatedTitle;
   private HashSet containedChats;
   private boolean enabled;

   public ChatTab(short var1, String var2) {
      this.enabled = false;
      this.id = var1;
      this.titleID = var2;
      this.translatedTitle = Translator.getText(var2);
      this.containedChats = new HashSet();
   }

   public ChatTab(short var1, String var2, int var3) {
      this(var1, var2);
      this.containedChats.add(var3);
   }

   public void RemoveChat(int var1) {
      if (!this.containedChats.contains(var1)) {
         throw new RuntimeException("Tab '" + this.id + "' doesn't contains a chat id: " + var1);
      } else {
         this.containedChats.remove(var1);
      }
   }

   public String getTitleID() {
      return this.titleID;
   }

   public String getTitle() {
      return this.translatedTitle;
   }

   public short getID() {
      return this.id;
   }

   public boolean isEnabled() {
      return this.enabled;
   }

   public void setEnabled(boolean var1) {
      this.enabled = var1;
   }

   public void sendAddTabPacket(UdpConnection var1) {
      ByteBufferWriter var2 = var1.startPacket();
      PacketTypes.PacketType.AddChatTab.doPacket(var2);
      var2.putShort(this.getID());
      PacketTypes.PacketType.AddChatTab.send(var1);
   }

   public void sendRemoveTabPacket(UdpConnection var1) {
      ByteBufferWriter var2 = var1.startPacket();
      PacketTypes.PacketType.RemoveChatTab.doPacket(var2);
      var2.putShort(this.getID());
      PacketTypes.PacketType.RemoveChatTab.send(var1);
   }
}

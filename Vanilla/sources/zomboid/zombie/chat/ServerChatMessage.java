package zombie.chat;

public class ServerChatMessage extends ChatMessage {
   public ServerChatMessage(ChatBase var1, String var2) {
      super(var1, var2);
      super.setAuthor("Server");
      this.setServerAuthor(true);
   }

   public String getAuthor() {
      return super.getAuthor();
   }

   public void setAuthor(String var1) {
      throw new UnsupportedOperationException();
   }
}

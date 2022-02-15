package zombie.network.chat;

public enum ChatType {
   notDefined(-1, ""),
   general(0, "UI_chat_general_chat_title_id"),
   whisper(1, "UI_chat_private_chat_title_id"),
   say(2, "UI_chat_local_chat_title_id"),
   shout(3, "UI_chat_local_chat_title_id"),
   faction(4, "UI_chat_faction_chat_title_id"),
   safehouse(5, "UI_chat_safehouse_chat_title_id"),
   radio(6, "UI_chat_radio_chat_title_id"),
   admin(7, "UI_chat_admin_chat_title_id"),
   server(8, "UI_chat_server_chat_title_id");

   private final int value;
   private final String titleID;

   public static ChatType valueOf(Integer var0) {
      if (general.value == var0) {
         return general;
      } else if (whisper.value == var0) {
         return whisper;
      } else if (say.value == var0) {
         return say;
      } else if (shout.value == var0) {
         return shout;
      } else if (faction.value == var0) {
         return faction;
      } else if (safehouse.value == var0) {
         return safehouse;
      } else if (radio.value == var0) {
         return radio;
      } else if (admin.value == var0) {
         return admin;
      } else {
         return server.value == var0 ? server : notDefined;
      }
   }

   private ChatType(Integer var3, String var4) {
      this.value = var3;
      this.titleID = var4;
   }

   public int getValue() {
      return this.value;
   }

   public String getTitleID() {
      return this.titleID;
   }

   // $FF: synthetic method
   private static ChatType[] $values() {
      return new ChatType[]{notDefined, general, whisper, say, shout, faction, safehouse, radio, admin, server};
   }
}

package zombie.chat;

public enum ChatMode {
   ServerMultiPlayer,
   ClientMultiPlayer,
   SinglePlayer;

   // $FF: synthetic method
   private static ChatMode[] $values() {
      return new ChatMode[]{ServerMultiPlayer, ClientMultiPlayer, SinglePlayer};
   }
}

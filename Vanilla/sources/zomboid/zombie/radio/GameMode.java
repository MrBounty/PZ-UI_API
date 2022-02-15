package zombie.radio;

public enum GameMode {
   SinglePlayer,
   Server,
   Client;

   // $FF: synthetic method
   private static GameMode[] $values() {
      return new GameMode[]{SinglePlayer, Server, Client};
   }
}

package zombie.fileSystem;

public enum FileSeekMode {
   BEGIN,
   END,
   CURRENT;

   // $FF: synthetic method
   private static FileSeekMode[] $values() {
      return new FileSeekMode[]{BEGIN, END, CURRENT};
   }
}

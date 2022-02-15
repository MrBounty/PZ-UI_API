package zombie.ui;

public enum UIFont {
   Small,
   Medium,
   Large,
   Massive,
   MainMenu1,
   MainMenu2,
   Cred1,
   Cred2,
   NewSmall,
   NewMedium,
   NewLarge,
   Code,
   MediumNew,
   AutoNormSmall,
   AutoNormMedium,
   AutoNormLarge,
   Dialogue,
   Intro,
   Handwritten,
   DebugConsole,
   Title;

   public static UIFont FromString(String var0) {
      try {
         return valueOf(var0);
      } catch (Exception var2) {
         return null;
      }
   }

   // $FF: synthetic method
   private static UIFont[] $values() {
      return new UIFont[]{Small, Medium, Large, Massive, MainMenu1, MainMenu2, Cred1, Cred2, NewSmall, NewMedium, NewLarge, Code, MediumNew, AutoNormSmall, AutoNormMedium, AutoNormLarge, Dialogue, Intro, Handwritten, DebugConsole, Title};
   }
}

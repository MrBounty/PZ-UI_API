package zombie.commands;

public final class PlayerType {
   public static final int observer = 1;
   public static final int player = 2;
   public static final int moderator = 4;
   public static final int overseer = 8;
   public static final int gm = 16;
   public static final int admin = 32;
   public static final int all = 63;
   public static final int allExceptPlayer = 61;

   private PlayerType() {
   }
}

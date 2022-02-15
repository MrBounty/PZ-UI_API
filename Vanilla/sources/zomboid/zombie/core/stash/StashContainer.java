package zombie.core.stash;

public final class StashContainer {
   public String room;
   public String containerSprite;
   public String containerType;
   public int contX = -1;
   public int contY = -1;
   public int contZ = -1;
   public String containerItem;

   public StashContainer(String var1, String var2, String var3) {
      if (var1 == null) {
         this.room = "all";
      } else {
         this.room = var1;
      }

      this.containerSprite = var2;
      this.containerType = var3;
   }
}

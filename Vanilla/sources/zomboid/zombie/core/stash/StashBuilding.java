package zombie.core.stash;

public final class StashBuilding {
   public int buildingX;
   public int buildingY;
   public String stashName;

   public StashBuilding(String var1, int var2, int var3) {
      this.stashName = var1;
      this.buildingX = var2;
      this.buildingY = var3;
   }

   public String getName() {
      return this.stashName;
   }
}

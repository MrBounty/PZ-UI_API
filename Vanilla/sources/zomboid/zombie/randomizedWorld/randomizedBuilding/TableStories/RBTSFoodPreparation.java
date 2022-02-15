package zombie.randomizedWorld.randomizedBuilding.TableStories;

import zombie.core.Rand;
import zombie.iso.BuildingDef;

public final class RBTSFoodPreparation extends RBTableStoryBase {
   public RBTSFoodPreparation() {
      this.chance = 8;
      this.ignoreAgainstWall = true;
      this.rooms.add("livingroom");
      this.rooms.add("kitchen");
   }

   public void randomizeBuilding(BuildingDef var1) {
      this.addWorldItem("Base.BakingTray", this.table1.getSquare(), 0.695F, 0.648F, this.table1.getSurfaceOffsetNoTable() / 96.0F, 1);
      String var2 = "Base.Chicken";
      int var3 = Rand.Next(0, 4);
      switch(var3) {
      case 0:
         var2 = "Base.Steak";
         break;
      case 1:
         var2 = "Base.MuttonChop";
         break;
      case 2:
         var2 = "Base.Smallbirdmeat";
      }

      this.addWorldItem(var2, this.table1.getSquare(), 0.531F, 0.625F, this.table1.getSurfaceOffsetNoTable() / 96.0F);
      this.addWorldItem(var2, this.table1.getSquare(), 0.836F, 0.627F, this.table1.getSurfaceOffsetNoTable() / 96.0F);
      this.addWorldItem(Rand.NextBool(2) ? "Base.Pepper" : "Base.Salt", this.table1.getSquare(), 0.492F, 0.94F, this.table1.getSurfaceOffsetNoTable() / 96.0F);
      this.addWorldItem("Base.KitchenKnife", this.table1.getSquare(), 0.492F, 0.29F, this.table1.getSurfaceOffsetNoTable() / 96.0F, 1);
      var2 = "farming.Tomato";
      var3 = Rand.Next(0, 4);
      switch(var3) {
      case 0:
         var2 = "Base.BellPepper";
         break;
      case 1:
         var2 = "Base.Broccoli";
         break;
      case 2:
         var2 = "Base.Carrots";
      }

      this.addWorldItem(var2, this.table1.getSquare(), 0.77F, 0.97F, this.table1.getSurfaceOffsetNoTable() / 96.0F, 70);
   }
}

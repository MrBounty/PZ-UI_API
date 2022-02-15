package zombie.randomizedWorld.randomizedBuilding.TableStories;

import zombie.core.Rand;
import zombie.iso.BuildingDef;

public final class RBTSSandwich extends RBTableStoryBase {
   public RBTSSandwich() {
      this.chance = 10;
      this.ignoreAgainstWall = true;
      this.rooms.add("livingroom");
      this.rooms.add("kitchen");
   }

   public void randomizeBuilding(BuildingDef var1) {
      this.addWorldItem("Base.Bread", this.table1.getSquare(), 0.804F, 0.726F, this.table1.getSurfaceOffsetNoTable() / 96.0F);
      this.addWorldItem("Base.BreadSlices", this.table1.getSquare(), 0.546F, 0.703F, this.table1.getSurfaceOffsetNoTable() / 96.0F);
      this.addWorldItem("Base.BreadKnife", this.table1.getSquare(), 0.648F, 0.414F, this.table1.getSurfaceOffsetNoTable() / 96.0F, Rand.Next(0, 30));
      this.addWorldItem("Base.PeanutButter", this.table1.getSquare(), 0.453F, 0.484F, this.table1.getSurfaceOffsetNoTable() / 96.0F);
      this.addWorldItem(Rand.NextBool(2) ? "Base.JamFruit" : "Base.JamMarmalade", this.table1.getSquare(), 0.351F, 0.836F, this.table1.getSurfaceOffsetNoTable() / 96.0F);
   }
}

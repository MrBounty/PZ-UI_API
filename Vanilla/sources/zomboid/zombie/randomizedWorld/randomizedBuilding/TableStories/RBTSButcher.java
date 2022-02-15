package zombie.randomizedWorld.randomizedBuilding.TableStories;

import zombie.core.Rand;
import zombie.iso.BuildingDef;

public final class RBTSButcher extends RBTableStoryBase {
   public RBTSButcher() {
      this.chance = 3;
      this.ignoreAgainstWall = true;
      this.rooms.add("livingroom");
      this.rooms.add("kitchen");
   }

   public void randomizeBuilding(BuildingDef var1) {
      String var2 = "Base.DeadRabbit";
      String var3 = "Base.Rabbitmeat";
      int var4 = Rand.Next(0, 4);
      switch(var4) {
      case 0:
         var2 = "Base.DeadBird";
         var3 = "Base.Smallbirdmeat";
         break;
      case 1:
         var2 = "Base.DeadSquirrel";
         var3 = "Base.Smallanimalmeat";
         break;
      case 2:
         var2 = "Base.Panfish";
         var3 = "Base.FishFillet";
         break;
      case 3:
         var2 = "Base.BaitFish";
         var3 = "Base.FishFillet";
         break;
      case 4:
         var2 = "Base.Catfish";
         var3 = "Base.FishFillet";
      }

      this.addWorldItem(var2, this.table1.getSquare(), 0.453F, 0.64F, this.table1.getSurfaceOffsetNoTable() / 96.0F, 1);
      this.addWorldItem(var3, this.table1.getSquare(), 0.835F, 0.851F, this.table1.getSurfaceOffsetNoTable() / 96.0F);
      this.addWorldItem("Base.KitchenKnife", this.table1.getSquare(), 0.742F, 0.445F, this.table1.getSurfaceOffsetNoTable() / 96.0F);
   }
}

package zombie.randomizedWorld.randomizedBuilding.TableStories;

import zombie.core.Rand;
import zombie.iso.BuildingDef;

public final class RBTSBreakfast extends RBTableStoryBase {
   public RBTSBreakfast() {
      this.need2Tables = true;
      this.chance = 10;
      this.ignoreAgainstWall = true;
      this.rooms.add("livingroom");
      this.rooms.add("kitchen");
   }

   public void randomizeBuilding(BuildingDef var1) {
      if (this.table2 != null) {
         String var2;
         int var3;
         if (this.westTable) {
            var2 = this.getBowlOrMug();
            this.addWorldItem(var2, this.table1.getSquare(), 0.6875F, 0.8437F, this.table1.getSurfaceOffsetNoTable() / 96.0F);
            if (Rand.NextBool(3)) {
               this.addWorldItem("Base.Spoon", this.table1.getSquare(), 0.3359F, 0.9765F, this.table1.getSurfaceOffsetNoTable() / 96.0F, Rand.Next(260, 275));
            }

            var2 = this.getBowlOrMug();
            this.addWorldItem(var2, this.table1.getSquare(), 0.6719F, 0.4531F, this.table1.getSurfaceOffsetNoTable() / 96.0F);
            if (Rand.NextBool(3)) {
               this.addWorldItem("Base.Spoon", this.table1.getSquare(), 0.375F, 0.2656F, this.table1.getSurfaceOffsetNoTable() / 96.0F, Rand.Next(75, 95));
            }

            if (Rand.Next(100) < 70) {
               var2 = this.getBowlOrMug();
               this.addWorldItem(var2, this.table2.getSquare(), 0.6484F, 0.8353F, this.table1.getSurfaceOffsetNoTable() / 96.0F);
               if (Rand.NextBool(3)) {
                  this.addWorldItem("Base.Spoon", this.table2.getSquare(), 0.8468F, 0.8906F, this.table1.getSurfaceOffsetNoTable() / 96.0F, Rand.Next(260, 275));
               }
            }

            if (Rand.Next(100) < 50) {
               var2 = this.getBowlOrMug();
               this.addWorldItem(var2, this.table2.getSquare(), 0.5859F, 0.3941F, this.table1.getSurfaceOffsetNoTable() / 96.0F);
               if (Rand.NextBool(3)) {
                  this.addWorldItem("Base.Spoon", this.table2.getSquare(), 0.7965F, 0.2343F, this.table1.getSurfaceOffsetNoTable() / 96.0F, Rand.Next(75, 95));
               }
            }

            this.addWorldItem("Base.Cereal", this.table2.getSquare(), 0.3281F, 0.6406F, this.table1.getSurfaceOffsetNoTable() / 96.0F, Rand.Next(0, 360));
            this.addWorldItem("Base.Milk", this.table1.getSquare(), 0.96F, 0.694F, this.table1.getSurfaceOffsetNoTable() / 96.0F, Rand.Next(0, 360));
            if (Rand.NextBool(0)) {
               var3 = Rand.Next(0, 4);
               switch(var3) {
               case 0:
                  this.addWorldItem("Base.Orange", this.table2.getSquare(), 0.6328F, 0.6484F, this.table1.getSurfaceOffsetNoTable() / 96.0F, Rand.Next(0, 360));
                  break;
               case 1:
                  this.addWorldItem("Base.Banana", this.table2.getSquare(), 0.6328F, 0.6484F, this.table1.getSurfaceOffsetNoTable() / 96.0F, Rand.Next(0, 360));
                  break;
               case 2:
                  this.addWorldItem("Base.Apple", this.table2.getSquare(), 0.6328F, 0.6484F, this.table1.getSurfaceOffsetNoTable() / 96.0F, Rand.Next(0, 360));
                  break;
               case 3:
                  this.addWorldItem("Base.Coffee2", this.table2.getSquare(), 0.6328F, 0.6484F, this.table1.getSurfaceOffsetNoTable() / 96.0F, Rand.Next(0, 360));
               }
            }
         } else {
            var2 = this.getBowlOrMug();
            this.addWorldItem(var2, this.table1.getSquare(), 0.906F, 0.718F, this.table1.getSurfaceOffsetNoTable() / 96.0F);
            if (Rand.NextBool(3)) {
               this.addWorldItem("Base.Spoon", this.table1.getSquare(), 0.945F, 0.336F, this.table1.getSurfaceOffsetNoTable() / 96.0F, Rand.Next(165, 185));
            }

            var2 = this.getBowlOrMug();
            this.addWorldItem(var2, this.table1.getSquare(), 0.406F, 0.562F, this.table1.getSurfaceOffsetNoTable() / 96.0F);
            if (Rand.NextBool(3)) {
               this.addWorldItem("Base.Spoon", this.table1.getSquare(), 0.265F, 0.299F, this.table1.getSurfaceOffsetNoTable() / 96.0F, Rand.Next(0, 15));
            }

            if (Rand.Next(100) < 70) {
               var2 = this.getBowlOrMug();
               this.addWorldItem(var2, this.table2.getSquare(), 0.929F, 0.726F, this.table1.getSurfaceOffsetNoTable() / 96.0F);
               if (Rand.NextBool(3)) {
                  this.addWorldItem("Base.Spoon", this.table2.getSquare(), 0.976F, 0.46F, this.table1.getSurfaceOffsetNoTable() / 96.0F, Rand.Next(165, 185));
               }
            }

            if (Rand.Next(100) < 50) {
               var2 = this.getBowlOrMug();
               this.addWorldItem(var2, this.table2.getSquare(), 0.382F, 0.78F, this.table1.getSurfaceOffsetNoTable() / 96.0F);
               if (Rand.NextBool(3)) {
                  this.addWorldItem("Base.Spoon", this.table2.getSquare(), 0.273F, 0.82F, this.table1.getSurfaceOffsetNoTable() / 96.0F, Rand.Next(0, 15));
               }
            }

            this.addWorldItem("Base.Cereal", this.table2.getSquare(), 0.7F, 0.273F, this.table1.getSurfaceOffsetNoTable() / 96.0F, Rand.Next(0, 360));
            this.addWorldItem("Base.Milk", this.table2.getSquare(), 0.648F, 0.539F, this.table1.getSurfaceOffsetNoTable() / 96.0F, Rand.Next(0, 360));
            if (Rand.NextBool(0)) {
               var3 = Rand.Next(0, 4);
               switch(var3) {
               case 0:
                  this.addWorldItem("Base.Orange", this.table1.getSquare(), 0.703F, 0.765F, this.table1.getSurfaceOffsetNoTable() / 96.0F, Rand.Next(0, 360));
                  break;
               case 1:
                  this.addWorldItem("Base.Banana", this.table1.getSquare(), 0.703F, 0.765F, this.table1.getSurfaceOffsetNoTable() / 96.0F, Rand.Next(0, 360));
                  break;
               case 2:
                  this.addWorldItem("Base.Apple", this.table1.getSquare(), 0.703F, 0.765F, this.table1.getSurfaceOffsetNoTable() / 96.0F, Rand.Next(0, 360));
                  break;
               case 3:
                  this.addWorldItem("Base.Coffee2", this.table1.getSquare(), 0.703F, 0.765F, this.table1.getSurfaceOffsetNoTable() / 96.0F, Rand.Next(0, 360));
               }
            }
         }
      }

   }

   private String getBowlOrMug() {
      boolean var1 = Rand.NextBool(2);
      if (var1) {
         return Rand.NextBool(4) ? "Base.Bowl" : "Base.CerealBowl";
      } else {
         int var2 = Rand.Next(0, 6);
         switch(var2) {
         case 0:
            return "Base.Mugl";
         case 1:
            return "Base.MugRed";
         case 2:
            return "Base.MugWhite";
         case 3:
            return "Base.HotDrink";
         case 4:
            return "Base.HotDrinkRed";
         case 5:
            return "Base.HotDrinkWhite";
         default:
            return "Base.Bowl";
         }
      }
   }
}

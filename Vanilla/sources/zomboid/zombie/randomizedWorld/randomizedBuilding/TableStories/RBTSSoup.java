package zombie.randomizedWorld.randomizedBuilding.TableStories;

import zombie.core.Rand;
import zombie.iso.BuildingDef;

public final class RBTSSoup extends RBTableStoryBase {
   public RBTSSoup() {
      this.need2Tables = true;
      this.chance = 10;
      this.ignoreAgainstWall = true;
      this.rooms.add("livingroom");
      this.rooms.add("kitchen");
   }

   public void randomizeBuilding(BuildingDef var1) {
      if (this.table2 != null) {
         if (this.westTable) {
            this.addWorldItem(Rand.NextBool(2) ? "Base.SoupBowl" : "Base.Bowl", this.table1.getSquare(), 0.6875F, 0.8437F, this.table1.getSurfaceOffsetNoTable() / 96.0F);
            if (Rand.NextBool(3)) {
               this.addWorldItem("Base.Spoon", this.table1.getSquare(), 0.3359F, 0.9765F, this.table1.getSurfaceOffsetNoTable() / 96.0F, Rand.Next(260, 275));
            }

            this.addWorldItem(Rand.NextBool(2) ? "Base.SoupBowl" : "Base.Bowl", this.table1.getSquare(), 0.6719F, 0.4531F, this.table1.getSurfaceOffsetNoTable() / 96.0F);
            if (Rand.NextBool(3)) {
               this.addWorldItem("Base.Spoon", this.table1.getSquare(), 0.375F, 0.2656F, this.table1.getSurfaceOffsetNoTable() / 96.0F, Rand.Next(75, 95));
            }

            if (Rand.Next(100) < 70) {
               this.addWorldItem(Rand.NextBool(2) ? "Base.SoupBowl" : "Base.Bowl", this.table2.getSquare(), 0.6484F, 0.8353F, this.table1.getSurfaceOffsetNoTable() / 96.0F);
               if (Rand.NextBool(3)) {
                  this.addWorldItem("Base.Spoon", this.table2.getSquare(), 0.8468F, 0.8906F, this.table1.getSurfaceOffsetNoTable() / 96.0F, Rand.Next(260, 275));
               }
            }

            if (Rand.Next(100) < 50) {
               this.addWorldItem(Rand.NextBool(2) ? "Base.SoupBowl" : "Base.Bowl", this.table2.getSquare(), 0.5859F, 0.3941F, this.table1.getSurfaceOffsetNoTable() / 96.0F);
               if (Rand.NextBool(3)) {
                  this.addWorldItem("Base.Spoon", this.table2.getSquare(), 0.7965F, 0.2343F, this.table1.getSurfaceOffsetNoTable() / 96.0F, Rand.Next(75, 95));
               }
            }

            this.addWorldItem(Rand.NextBool(2) ? "Base.PotOfSoup" : "Base.PotOfStew", this.table2.getSquare(), 0.289F, 0.585F, this.table1.getSurfaceOffsetNoTable() / 96.0F);
            this.addWorldItem("Base.Spoon", this.table2.getSquare(), 0.132F, 0.835F, this.table1.getSurfaceOffsetNoTable() / 96.0F, Rand.Next(0, 35));
            if (Rand.NextBool(3)) {
               this.addWorldItem("farming.Salad", this.table1.getSquare(), 0.992F, 0.726F, this.table1.getSurfaceOffsetNoTable() / 96.0F);
            }
         } else {
            this.addWorldItem(Rand.NextBool(2) ? "Base.SoupBowl" : "Base.Bowl", this.table1.getSquare(), 0.906F, 0.718F, this.table1.getSurfaceOffsetNoTable() / 96.0F);
            if (Rand.NextBool(3)) {
               this.addWorldItem("Base.Spoon", this.table1.getSquare(), 0.945F, 0.336F, this.table1.getSurfaceOffsetNoTable() / 96.0F, Rand.Next(165, 185));
            }

            this.addWorldItem(Rand.NextBool(2) ? "Base.SoupBowl" : "Base.Bowl", this.table1.getSquare(), 0.406F, 0.562F, this.table1.getSurfaceOffsetNoTable() / 96.0F);
            if (Rand.NextBool(3)) {
               this.addWorldItem("Base.Spoon", this.table1.getSquare(), 0.265F, 0.299F, this.table1.getSurfaceOffsetNoTable() / 96.0F, Rand.Next(0, 15));
            }

            if (Rand.Next(100) < 70) {
               this.addWorldItem(Rand.NextBool(2) ? "Base.SoupBowl" : "Base.Bowl", this.table2.getSquare(), 0.929F, 0.726F, this.table1.getSurfaceOffsetNoTable() / 96.0F);
               if (Rand.NextBool(3)) {
                  this.addWorldItem("Base.Spoon", this.table2.getSquare(), 0.976F, 0.46F, this.table1.getSurfaceOffsetNoTable() / 96.0F, Rand.Next(165, 185));
               }
            }

            if (Rand.Next(100) < 50) {
               this.addWorldItem(Rand.NextBool(2) ? "Base.SoupBowl" : "Base.Bowl", this.table2.getSquare(), 0.382F, 0.78F, this.table1.getSurfaceOffsetNoTable() / 96.0F);
               if (Rand.NextBool(3)) {
                  this.addWorldItem("Base.Spoon", this.table2.getSquare(), 0.273F, 0.82F, this.table1.getSurfaceOffsetNoTable() / 96.0F, Rand.Next(0, 15));
               }
            }

            this.addWorldItem(Rand.NextBool(2) ? "Base.PotOfSoup" : "Base.PotOfStew", this.table2.getSquare(), 0.679F, 0.289F, this.table1.getSurfaceOffsetNoTable() / 96.0F);
            this.addWorldItem("Base.Spoon", this.table2.getSquare(), 0.937F, 0.187F, this.table1.getSurfaceOffsetNoTable() / 96.0F, Rand.Next(85, 110));
            if (Rand.NextBool(3)) {
               this.addWorldItem("farming.Salad", this.table1.getSquare(), 0.679F, 0.882F, this.table1.getSurfaceOffsetNoTable() / 96.0F);
            }
         }
      }

   }
}

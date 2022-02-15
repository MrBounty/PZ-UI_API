package zombie.randomizedWorld.randomizedBuilding.TableStories;

import zombie.core.Rand;
import zombie.iso.BuildingDef;

public final class RBTSSewing extends RBTableStoryBase {
   public RBTSSewing() {
      this.chance = 5;
      this.rooms.add("livingroom");
      this.rooms.add("kitchen");
      this.rooms.add("bedroom");
   }

   public void randomizeBuilding(BuildingDef var1) {
      int var2 = Rand.Next(0, 2);
      if (var2 == 0) {
         this.addWorldItem(Rand.NextBool(2) ? "Base.Socks_Ankle" : "Base.Socks_Long", this.table1.getSquare(), 0.476F, 0.767F, this.table1.getSurfaceOffsetNoTable() / 96.0F);
         this.addWorldItem(Rand.NextBool(2) ? "Base.Socks_Ankle" : "Base.Socks_Long", this.table1.getSquare(), 0.656F, 0.775F, this.table1.getSurfaceOffsetNoTable() / 96.0F);
         if (Rand.NextBool(3)) {
            this.addWorldItem(Rand.NextBool(2) ? "Base.Socks_Ankle" : "Base.Socks_Long", this.table1.getSquare(), 0.437F, 0.469F, this.table1.getSurfaceOffsetNoTable() / 96.0F);
         }

         this.addWorldItem("Base.SewingKit", this.table1.getSquare(), 0.835F, 0.476F, this.table1.getSurfaceOffsetNoTable() / 96.0F, Rand.Next(75, 95));
         if (Rand.NextBool(2)) {
            this.addWorldItem("Base.Scissors", this.table1.getSquare(), 0.945F, 0.586F, this.table1.getSurfaceOffsetNoTable() / 96.0F, Rand.Next(75, 95));
         }

         if (Rand.NextBool(2)) {
            this.addWorldItem("Base.Thread", this.table1.getSquare(), 0.899F, 0.914F, this.table1.getSurfaceOffsetNoTable() / 96.0F, Rand.Next(75, 95));
         }
      } else if (var2 == 1) {
         String var3 = "Base.Jumper_DiamondPatternTINT";
         int var4 = Rand.Next(0, 4);
         switch(var4) {
         case 0:
            var3 = "Base.Jumper_TankTopDiamondTINT";
            break;
         case 1:
            var3 = "Base.Jumper_PoloNeck";
            break;
         case 2:
            var3 = "Base.Jumper_VNeck";
            break;
         case 3:
            var3 = "Base.Jumper_RoundNeck";
         }

         this.addWorldItem("Base.KnittingNeedles", this.table1.getSquare(), 0.531F, 0.625F, this.table1.getSurfaceOffsetNoTable() / 96.0F);
         this.addWorldItem(var3, this.table1.getSquare(), 0.687F, 0.687F, this.table1.getSurfaceOffsetNoTable() / 96.0F);
         this.addWorldItem("Base.Yarn", this.table1.getSquare(), 0.633F, 0.96F, this.table1.getSurfaceOffsetNoTable() / 96.0F);
         this.addWorldItem("Base.RippedSheets", this.table1.getSquare(), 0.875F, 0.91F, this.table1.getSurfaceOffsetNoTable() / 96.0F, 1);
      }

   }
}

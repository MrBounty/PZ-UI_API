package zombie.randomizedWorld.randomizedBuilding;

import zombie.core.Rand;
import zombie.iso.BuildingDef;
import zombie.iso.IsoCell;
import zombie.iso.IsoGridSquare;
import zombie.iso.IsoObject;
import zombie.iso.IsoWorld;

public final class RBPizzaWhirled extends RandomizedBuildingBase {
   public void randomizeBuilding(BuildingDef var1) {
      IsoCell var2 = IsoWorld.instance.CurrentCell;

      for(int var3 = var1.x - 1; var3 < var1.x2 + 1; ++var3) {
         for(int var4 = var1.y - 1; var4 < var1.y2 + 1; ++var4) {
            for(int var5 = 0; var5 < 8; ++var5) {
               IsoGridSquare var6 = var2.getGridSquare(var3, var4, var5);
               if (var6 != null && this.roomValid(var6)) {
                  for(int var7 = 0; var7 < var6.getObjects().size(); ++var7) {
                     IsoObject var8 = (IsoObject)var6.getObjects().get(var7);
                     if (Rand.NextBool(2) && this.isTableFor3DItems(var8, var6)) {
                        this.addWorldItem("Pizza", var6, var8);
                        if (Rand.NextBool(3)) {
                           this.addWorldItem("Fork", var6, var8);
                        }

                        if (Rand.NextBool(3)) {
                           this.addWorldItem("ButterKnife", var6, var8);
                        }
                     }
                  }
               }
            }
         }
      }

   }

   public boolean roomValid(IsoGridSquare var1) {
      return var1.getRoom() != null && ("pizzawhirled".equals(var1.getRoom().getName()) || "italianrestaurant".equals(var1.getRoom().getName()));
   }

   public boolean isValid(BuildingDef var1, boolean var2) {
      return var1.getRoom("pizzawhirled") != null || var1.getRoom("italianrestaurant") != null || var2;
   }

   public RBPizzaWhirled() {
      this.name = "Pizza Whirled Restaurant";
      this.setAlwaysDo(true);
   }
}

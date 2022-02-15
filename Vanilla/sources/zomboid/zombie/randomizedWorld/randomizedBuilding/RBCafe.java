package zombie.randomizedWorld.randomizedBuilding;

import zombie.core.Rand;
import zombie.iso.BuildingDef;
import zombie.iso.IsoCell;
import zombie.iso.IsoGridSquare;
import zombie.iso.IsoObject;
import zombie.iso.IsoWorld;

public final class RBCafe extends RandomizedBuildingBase {
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
                        if (Rand.NextBool(2)) {
                           int var9 = Rand.Next(3);
                           switch(var9) {
                           case 0:
                              this.addWorldItem("Mugl", var6, var8);
                              break;
                           case 1:
                              this.addWorldItem("MugWhite", var6, var8);
                              break;
                           case 2:
                              this.addWorldItem("MugRed", var6, var8);
                           }
                        }

                        if (Rand.NextBool(4)) {
                           this.addWorldItem("Cupcake", var6, var8);
                        }

                        if (Rand.NextBool(4)) {
                           this.addWorldItem("CookieJelly", var6, var8);
                        }

                        if (Rand.NextBool(4)) {
                           this.addWorldItem("CookieChocolateChip", var6, var8);
                        }

                        if (Rand.NextBool(4)) {
                           this.addWorldItem("Kettle", var6, var8);
                        }

                        if (Rand.NextBool(3)) {
                           this.addWorldItem("Sugar", var6, var8);
                        }

                        if (Rand.NextBool(2)) {
                           this.addWorldItem("Teabag2", var6, var8);
                        }
                     }
                  }
               }
            }
         }
      }

   }

   public boolean roomValid(IsoGridSquare var1) {
      return var1.getRoom() != null && "cafe".equals(var1.getRoom().getName());
   }

   public boolean isValid(BuildingDef var1, boolean var2) {
      return var1.getRoom("cafe") != null || var2;
   }

   public RBCafe() {
      this.name = "Cafe (Seahorse..)";
      this.setAlwaysDo(true);
   }
}

package zombie.randomizedWorld.randomizedBuilding;

import java.io.PrintStream;
import zombie.core.Rand;
import zombie.iso.BuildingDef;
import zombie.iso.IsoCell;
import zombie.iso.IsoGridSquare;
import zombie.iso.IsoObject;
import zombie.iso.IsoWorld;

public final class RBSpiffo extends RandomizedBuildingBase {
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
                        PrintStream var10000 = System.out;
                        String var10001 = var8.getSprite().getName();
                        var10000.println("adding item on table " + var10001 + " coords: " + var6.x + "," + var6.y);
                        if (Rand.NextBool(2)) {
                           this.addWorldItem("Burger", var6, var8);
                        }

                        if (Rand.NextBool(2)) {
                           this.addWorldItem("Fries", var6, var8);
                        }

                        if (Rand.NextBool(2)) {
                           this.addWorldItem("Ketchup", var6, var8);
                        }

                        if (Rand.NextBool(3)) {
                           this.addWorldItem("Fork", var6, var8);
                        }

                        if (Rand.NextBool(3)) {
                           this.addWorldItem("ButterKnife", var6, var8);
                        }

                        if (Rand.NextBool(30)) {
                           this.addWorldItem("MugSpiffo", var6, var8);
                        }
                        break;
                     }
                  }
               }
            }
         }
      }

   }

   public boolean roomValid(IsoGridSquare var1) {
      return var1.getRoom() != null && ("spiffo_dining".equals(var1.getRoom().getName()) || "burgerkitchen".equals(var1.getRoom().getName()));
   }

   public boolean isValid(BuildingDef var1, boolean var2) {
      return var1.getRoom("spiffo_dining") != null || var1.getRoom("burgerkitchen") != null || var2;
   }

   public RBSpiffo() {
      this.name = "Spiffo Restaurant";
      this.setAlwaysDo(true);
   }
}

package zombie.randomizedWorld.randomizedBuilding;

import zombie.core.Rand;
import zombie.iso.BuildingDef;
import zombie.iso.IsoCell;
import zombie.iso.IsoGridSquare;
import zombie.iso.IsoObject;
import zombie.iso.IsoWorld;

public final class RBBar extends RandomizedBuildingBase {
   public void randomizeBuilding(BuildingDef var1) {
      IsoCell var2 = IsoWorld.instance.CurrentCell;

      for(int var3 = var1.x - 1; var3 < var1.x2 + 1; ++var3) {
         for(int var4 = var1.y - 1; var4 < var1.y2 + 1; ++var4) {
            for(int var5 = 0; var5 < 8; ++var5) {
               IsoGridSquare var6 = var2.getGridSquare(var3, var4, var5);
               if (var6 != null && this.roomValid(var6)) {
                  for(int var7 = 0; var7 < var6.getObjects().size(); ++var7) {
                     IsoObject var8 = (IsoObject)var6.getObjects().get(var7);
                     if (var8.getSprite() != null && var8.getSprite().getName() != null && (var8.getSprite().getName().equals("recreational_01_6") || var8.getSprite().getName().equals("recreational_01_7"))) {
                        if (Rand.NextBool(3)) {
                           this.addWorldItem("PoolBall", var6, var8);
                        }

                        if (Rand.NextBool(3)) {
                           this.addWorldItem("Poolcue", var6, var8);
                        }
                     } else if (var8.isTableSurface() && Rand.NextBool(2)) {
                        if (Rand.NextBool(3)) {
                           this.addWorldItem("Cigarettes", var6, var8);
                           if (Rand.NextBool(2)) {
                              this.addWorldItem("Lighter", var6, var8);
                           }
                        }

                        int var9 = Rand.Next(7);
                        switch(var9) {
                        case 0:
                           this.addWorldItem("WhiskeyFull", var6, var8);
                           break;
                        case 1:
                           this.addWorldItem("Wine", var6, var8);
                           break;
                        case 2:
                           this.addWorldItem("Wine2", var6, var8);
                           break;
                        case 3:
                           this.addWorldItem("BeerCan", var6, var8);
                           break;
                        case 4:
                           this.addWorldItem("BeerBottle", var6, var8);
                        }

                        if (Rand.NextBool(3)) {
                           int var10 = Rand.Next(7);
                           switch(var10) {
                           case 0:
                              this.addWorldItem("Crisps", var6, var8);
                              break;
                           case 1:
                              this.addWorldItem("Crisps2", var6, var8);
                              break;
                           case 2:
                              this.addWorldItem("Crisps3", var6, var8);
                              break;
                           case 3:
                              this.addWorldItem("Crisps4", var6, var8);
                              break;
                           case 4:
                              this.addWorldItem("Peanuts", var6, var8);
                           }
                        }

                        if (Rand.NextBool(4)) {
                           this.addWorldItem("CardDeck", var6, var8);
                        }
                     }
                  }

                  if (Rand.NextBool(20) && var6.getRoom() != null && var6.getRoom().getName().equals("bar") && var6.getObjects().size() == 1 && Rand.NextBool(8)) {
                     this.addWorldItem("Dart", var6, (IsoObject)null);
                  }
               }
            }
         }
      }

   }

   public boolean roomValid(IsoGridSquare var1) {
      return var1.getRoom() != null && "bar".equals(var1.getRoom().getName());
   }

   public boolean isValid(BuildingDef var1, boolean var2) {
      return var1.getRoom("bar") != null && var1.getRoom("stripclub") == null || var2;
   }

   public RBBar() {
      this.name = "Bar";
      this.setAlwaysDo(true);
   }
}

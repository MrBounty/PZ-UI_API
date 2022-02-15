package zombie.randomizedWorld.randomizedBuilding;

import zombie.characters.IsoGameCharacter;
import zombie.core.Rand;
import zombie.inventory.ItemPickerJava;
import zombie.iso.BuildingDef;
import zombie.iso.IsoCell;
import zombie.iso.IsoGridSquare;
import zombie.iso.IsoObject;
import zombie.iso.IsoWorld;
import zombie.iso.objects.IsoDoor;
import zombie.iso.objects.IsoWindow;

public final class RBLooted extends RandomizedBuildingBase {
   public void randomizeBuilding(BuildingDef var1) {
      var1.bAlarmed = false;
      IsoCell var2 = IsoWorld.instance.CurrentCell;

      for(int var3 = var1.x - 1; var3 < var1.x2 + 1; ++var3) {
         for(int var4 = var1.y - 1; var4 < var1.y2 + 1; ++var4) {
            for(int var5 = 0; var5 < 8; ++var5) {
               IsoGridSquare var6 = var2.getGridSquare(var3, var4, var5);
               if (var6 != null) {
                  for(int var7 = 0; var7 < var6.getObjects().size(); ++var7) {
                     IsoObject var8 = (IsoObject)var6.getObjects().get(var7);
                     if (Rand.Next(100) >= 85 && var8 instanceof IsoDoor && ((IsoDoor)var8).isExteriorDoor((IsoGameCharacter)null)) {
                        ((IsoDoor)var8).destroy();
                     }

                     if (Rand.Next(100) >= 85 && var8 instanceof IsoWindow) {
                        ((IsoWindow)var8).smashWindow(false, false);
                     }

                     if (var8.getContainer() != null && var8.getContainer().getItems() != null) {
                        for(int var9 = 0; var9 < var8.getContainer().getItems().size(); ++var9) {
                           if (Rand.Next(100) < 80) {
                              var8.getContainer().getItems().remove(var9);
                              --var9;
                           }
                        }

                        ItemPickerJava.updateOverlaySprite(var8);
                        var8.getContainer().setExplored(true);
                     }
                  }
               }
            }
         }
      }

      var1.setAllExplored(true);
      var1.bAlarmed = false;
   }

   public RBLooted() {
      this.name = "Looted";
      this.setChance(10);
   }
}

package zombie.randomizedWorld.randomizedBuilding;

import java.util.ArrayList;
import zombie.characters.IsoZombie;
import zombie.core.Rand;
import zombie.iso.BuildingDef;
import zombie.iso.IsoCell;
import zombie.iso.IsoGridSquare;
import zombie.iso.IsoWorld;
import zombie.iso.RoomDef;
import zombie.iso.objects.IsoDeadBody;

public final class RBBurntCorpse extends RandomizedBuildingBase {
   public void randomizeBuilding(BuildingDef var1) {
      var1.bAlarmed = false;
      var1.setHasBeenVisited(true);
      IsoCell var2 = IsoWorld.instance.CurrentCell;

      int var4;
      for(int var3 = var1.x - 1; var3 < var1.x2 + 1; ++var3) {
         for(var4 = var1.y - 1; var4 < var1.y2 + 1; ++var4) {
            for(int var5 = 0; var5 < 8; ++var5) {
               IsoGridSquare var6 = var2.getGridSquare(var3, var4, var5);
               if (var6 != null && Rand.Next(100) < 60) {
                  var6.Burn(false);
               }
            }
         }
      }

      var1.setAllExplored(true);
      var1.bAlarmed = false;
      ArrayList var7 = this.addZombies(var1, Rand.Next(3, 7), (String)null, (Integer)null, (RoomDef)null);
      if (var7 != null) {
         for(var4 = 0; var4 < var7.size(); ++var4) {
            IsoZombie var8 = (IsoZombie)var7.get(var4);
            var8.setSkeleton(true);
            var8.getHumanVisual().setSkinTextureIndex(0);
            new IsoDeadBody(var8, false);
         }

      }
   }

   public RBBurntCorpse() {
      this.name = "Burnt with corpses";
      this.setChance(3);
   }
}

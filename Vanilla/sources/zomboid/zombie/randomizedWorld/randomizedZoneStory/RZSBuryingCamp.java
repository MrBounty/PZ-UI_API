package zombie.randomizedWorld.randomizedZoneStory;

import java.util.ArrayList;
import zombie.characters.IsoZombie;
import zombie.core.Rand;
import zombie.iso.IsoDirections;
import zombie.iso.IsoMetaGrid;
import zombie.iso.objects.IsoDeadBody;

public class RZSBuryingCamp extends RandomizedZoneStoryBase {
   public RZSBuryingCamp() {
      this.name = "Burying Camp";
      this.chance = 7;
      this.minZoneHeight = 6;
      this.minZoneWidth = 6;
      this.minimumDays = 20;
      this.zoneType.add(RandomizedZoneStoryBase.ZoneType.Forest.toString());
   }

   public void randomizeZoneStory(IsoMetaGrid.Zone var1) {
      this.cleanAreaForStory(this, var1);
      boolean var2 = Rand.NextBool(2);
      int var3 = var1.x + 1;
      int var4 = var1.y + 1;
      int var5 = 0;
      int var6 = 0;
      int var7 = Rand.Next(3, 7);

      for(int var8 = 0; var8 < var7; ++var8) {
         if (var2) {
            this.addTileObject(var3 + var8, var1.y + 2, var1.z, "location_community_cemetary_01_22");
            if (var8 == 2) {
               this.addTileObject(var3 + var8, var1.y + 3, var1.z, "location_community_cemetary_01_35");
               this.addTileObject(var3 + var8, var1.y + 4, var1.z, "location_community_cemetary_01_34");
               var5 = var3 + var8;
               var6 = var1.y + 5;
            } else {
               this.addTileObject(var3 + var8, var1.y + 3, var1.z, "location_community_cemetary_01_43");
               this.addTileObject(var3 + var8, var1.y + 4, var1.z, "location_community_cemetary_01_42");
               if (Rand.NextBool(2)) {
                  this.addTileObject(var3 + var8, var1.y + 6, var1.z, "vegetation_ornamental_01_" + Rand.Next(16, 19));
               }
            }
         } else {
            this.addTileObject(var1.x + 2, var4 + var8, var1.z, "location_community_cemetary_01_23");
            if (var8 == 2) {
               this.addTileObject(var1.x + 3, var4 + var8, var1.z, "location_community_cemetary_01_32");
               this.addTileObject(var1.x + 4, var4 + var8, var1.z, "location_community_cemetary_01_33");
               var5 = var1.x + 5;
               var6 = var4 + var8;
            } else {
               this.addTileObject(var1.x + 3, var4 + var8, var1.z, "location_community_cemetary_01_40");
               this.addTileObject(var1.x + 4, var4 + var8, var1.z, "location_community_cemetary_01_41");
               if (Rand.NextBool(2)) {
                  this.addTileObject(var1.x + 6, var4 + var8, var1.z, "vegetation_ornamental_01_" + Rand.Next(16, 19));
               }
            }
         }
      }

      this.addItemOnGround(this.getSq(var5 + 1, var6 + 1, var1.z), "Base.Shovel");
      ArrayList var11 = this.addZombiesOnSquare(1, (String)null, (Integer)null, this.getRandomFreeSquare(this, var1));
      if (var11 != null && !var11.isEmpty()) {
         IsoZombie var9 = (IsoZombie)var11.get(0);
         IsoDeadBody var10 = createRandomDeadBody(this.getSq(var5, var6, var1.z), (IsoDirections)null, Rand.Next(7, 12), 0, (String)null);
         if (var10 != null) {
            this.addBloodSplat(var10.getSquare(), 10);
            var9.faceLocationF(var10.x, var10.y);
            var9.setX(var10.x + 1.0F);
            var9.setY(var10.y);
            var9.setEatBodyTarget(var10, true);
         }
      }

      this.addItemOnGround(this.getRandomFreeSquare(this, var1), "Base.WhiskeyEmpty");
      this.addItemOnGround(this.getRandomFreeSquare(this, var1), "Base.WineEmpty");
   }
}

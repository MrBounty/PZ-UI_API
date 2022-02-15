package zombie.randomizedWorld.randomizedZoneStory;

import java.util.ArrayList;
import zombie.Lua.MapObjects;
import zombie.core.Rand;
import zombie.iso.IsoGridSquare;
import zombie.iso.IsoMetaGrid;

public class RZSTrapperCamp extends RandomizedZoneStoryBase {
   public RZSTrapperCamp() {
      this.name = "Trappers Forest Camp";
      this.chance = 7;
      this.minZoneHeight = 6;
      this.minZoneWidth = 6;
      this.zoneType.add(RandomizedZoneStoryBase.ZoneType.Forest.toString());
   }

   public static ArrayList getTrapList() {
      ArrayList var0 = new ArrayList();
      var0.add("constructedobjects_01_3");
      var0.add("constructedobjects_01_4");
      var0.add("constructedobjects_01_7");
      var0.add("constructedobjects_01_8");
      var0.add("constructedobjects_01_11");
      var0.add("constructedobjects_01_13");
      var0.add("constructedobjects_01_16");
      return var0;
   }

   public void randomizeZoneStory(IsoMetaGrid.Zone var1) {
      int var2 = var1.pickedXForZoneStory;
      int var3 = var1.pickedYForZoneStory;
      ArrayList var4 = getTrapList();
      this.cleanAreaForStory(this, var1);
      this.addTileObject(var2, var3, var1.z, "camping_01_6");
      int var5 = Rand.Next(-1, 2);
      int var6 = Rand.Next(-1, 2);
      this.addTentWestEast(var2 + var5 - 2, var3 + var6, var1.z);
      if (Rand.Next(100) < 70) {
         this.addTentNorthSouth(var2 + var5, var3 + var6 - 2, var1.z);
      }

      int var7 = Rand.Next(2, 5);

      for(int var8 = 0; var8 < var7; ++var8) {
         IsoGridSquare var9 = this.getRandomFreeSquare(this, var1);
         this.addTileObject(var9, (String)var4.get(Rand.Next(var4.size())));
         MapObjects.loadGridSquare(var9);
      }

      this.addZombiesOnSquare(Rand.Next(2, 5), "Hunter", 0, this.getRandomFreeSquare(this, var1));
   }
}

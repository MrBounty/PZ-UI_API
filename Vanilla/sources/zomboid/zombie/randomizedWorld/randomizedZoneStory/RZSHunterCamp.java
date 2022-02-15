package zombie.randomizedWorld.randomizedZoneStory;

import java.util.ArrayList;
import zombie.core.Rand;
import zombie.iso.IsoChunk;
import zombie.iso.IsoDirections;
import zombie.iso.IsoMetaGrid;

public class RZSHunterCamp extends RandomizedZoneStoryBase {
   public RZSHunterCamp() {
      this.name = "Hunter Forest Camp";
      this.chance = 5;
      this.minZoneHeight = 6;
      this.minZoneWidth = 6;
      this.zoneType.add(RandomizedZoneStoryBase.ZoneType.Forest.toString());
   }

   public static ArrayList getForestClutter() {
      ArrayList var0 = new ArrayList();
      var0.add("Base.VarmintRifle");
      var0.add("Base.223Box");
      var0.add("Base.HuntingRifle");
      var0.add("Base.308Box");
      var0.add("Base.Shotgun");
      var0.add("Base.ShotgunShellsBox");
      var0.add("Base.DoubleBarrelShotgun");
      var0.add("Base.AssaultRifle");
      var0.add("Base.556Box");
      return var0;
   }

   public void randomizeZoneStory(IsoMetaGrid.Zone var1) {
      int var2 = var1.pickedXForZoneStory;
      int var3 = var1.pickedYForZoneStory;
      ArrayList var4 = getForestClutter();
      this.cleanAreaForStory(this, var1);
      this.addVehicle(var1, this.getSq(var1.x, var1.y, var1.z), (IsoChunk)null, (String)null, "Base.OffRoad", (Integer)null, (IsoDirections)null, "Hunter");
      this.addTileObject(var2, var3, var1.z, "camping_01_6");
      int var5 = Rand.Next(-1, 2);
      int var6 = Rand.Next(-1, 2);
      this.addTentWestEast(var2 + var5 - 2, var3 + var6, var1.z);
      if (Rand.Next(100) < 70) {
         this.addTentNorthSouth(var2 + var5, var3 + var6 - 2, var1.z);
      }

      if (Rand.Next(100) < 30) {
         this.addTentNorthSouth(var2 + var5 + 1, var3 + var6 - 2, var1.z);
      }

      int var7 = Rand.Next(2, 5);

      for(int var8 = 0; var8 < var7; ++var8) {
         this.addItemOnGround(this.getRandomFreeSquare(this, var1), (String)var4.get(Rand.Next(var4.size())));
      }

      this.addZombiesOnSquare(Rand.Next(2, 5), "Hunter", 0, this.getRandomFreeSquare(this, var1));
   }
}

package zombie.randomizedWorld.randomizedZoneStory;

import zombie.core.Rand;
import zombie.iso.IsoMetaGrid;

public class RZSMusicFest extends RandomizedZoneStoryBase {
   public RZSMusicFest() {
      this.name = "Music Festival";
      this.chance = 100;
      this.zoneType.add(RandomizedZoneStoryBase.ZoneType.MusicFest.toString());
      this.alwaysDo = true;
   }

   public void randomizeZoneStory(IsoMetaGrid.Zone var1) {
      int var2 = Rand.Next(20, 50);

      for(int var3 = 0; var3 < var2; ++var3) {
         int var4 = Rand.Next(0, 4);
         switch(var4) {
         case 0:
            this.addItemOnGround(this.getRandomFreeSquareFullZone(this, var1), "Base.BeerCan");
            break;
         case 1:
            this.addItemOnGround(this.getRandomFreeSquareFullZone(this, var1), "Base.BeerBottle");
            break;
         case 2:
            this.addItemOnGround(this.getRandomFreeSquareFullZone(this, var1), "Base.BeerCanEmpty");
            break;
         case 3:
            this.addItemOnGround(this.getRandomFreeSquareFullZone(this, var1), "Base.BeerEmpty");
         }
      }

   }
}

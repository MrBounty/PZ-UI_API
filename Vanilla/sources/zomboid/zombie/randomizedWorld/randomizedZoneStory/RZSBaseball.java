package zombie.randomizedWorld.randomizedZoneStory;

import zombie.core.Rand;
import zombie.iso.IsoMetaGrid;

public class RZSBaseball extends RandomizedZoneStoryBase {
   public RZSBaseball() {
      this.name = "Baseball";
      this.chance = 100;
      this.zoneType.add(RandomizedZoneStoryBase.ZoneType.Baseball.toString());
      this.minZoneWidth = 20;
      this.minZoneHeight = 20;
      this.alwaysDo = true;
   }

   public void randomizeZoneStory(IsoMetaGrid.Zone var1) {
      int var2 = Rand.Next(0, 3);

      int var3;
      for(var3 = Rand.Next(0, 3); var2 == var3; var3 = Rand.Next(0, 3)) {
      }

      String var4 = "BaseballPlayer_KY";
      if (var2 == 1) {
         var4 = "BaseballPlayer_Rangers";
      }

      if (var2 == 2) {
         var4 = "BaseballPlayer_Z";
      }

      String var5 = "BaseballPlayer_KY";
      if (var3 == 1) {
         var5 = "BaseballPlayer_Rangers";
      }

      if (var3 == 2) {
         var5 = "BaseballPlayer_Z";
      }

      int var6;
      for(var6 = 0; var6 < 20; ++var6) {
         if (Rand.NextBool(4)) {
            this.addItemOnGround(this.getRandomFreeSquare(this, var1), "Base.BaseballBat");
         }

         if (Rand.NextBool(6)) {
            this.addItemOnGround(this.getRandomFreeSquare(this, var1), "Base.Baseball");
         }
      }

      for(var6 = 0; var6 <= 9; ++var6) {
         this.addZombiesOnSquare(1, var4, 0, this.getRandomFreeSquare(this, var1));
         this.addZombiesOnSquare(1, var5, 0, this.getRandomFreeSquare(this, var1));
      }

   }
}

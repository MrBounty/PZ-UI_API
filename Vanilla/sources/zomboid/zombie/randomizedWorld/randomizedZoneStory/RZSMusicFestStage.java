package zombie.randomizedWorld.randomizedZoneStory;

import zombie.core.Rand;
import zombie.iso.IsoMetaGrid;

public class RZSMusicFestStage extends RandomizedZoneStoryBase {
   public RZSMusicFestStage() {
      this.name = "Music Festival Stage";
      this.chance = 100;
      this.zoneType.add(RandomizedZoneStoryBase.ZoneType.MusicFestStage.toString());
      this.alwaysDo = true;
   }

   public void randomizeZoneStory(IsoMetaGrid.Zone var1) {
      int var2;
      for(var2 = 0; var2 < 2; ++var2) {
         int var3 = Rand.Next(0, 4);
         switch(var3) {
         case 0:
            this.addItemOnGround(this.getRandomFreeSquareFullZone(this, var1), "Base.GuitarAcoustic");
            break;
         case 1:
            this.addItemOnGround(this.getRandomFreeSquareFullZone(this, var1), "Base.GuitarElectricBlack");
            break;
         case 2:
            this.addItemOnGround(this.getRandomFreeSquareFullZone(this, var1), "Base.GuitarElectricBlue");
            break;
         case 3:
            this.addItemOnGround(this.getRandomFreeSquareFullZone(this, var1), "Base.GuitarElectricRed");
         }
      }

      var2 = Rand.Next(0, 3);
      switch(var2) {
      case 0:
         this.addItemOnGround(this.getRandomFreeSquareFullZone(this, var1), "Base.GuitarElectricBassBlack");
         break;
      case 1:
         this.addItemOnGround(this.getRandomFreeSquareFullZone(this, var1), "Base.GuitarElectricBassBlue");
         break;
      case 2:
         this.addItemOnGround(this.getRandomFreeSquareFullZone(this, var1), "Base.GuitarElectricBassRed");
      }

      if (Rand.NextBool(6)) {
         this.addItemOnGround(this.getRandomFreeSquareFullZone(this, var1), "Base.Keytar");
      }

      this.addItemOnGround(this.getRandomFreeSquareFullZone(this, var1), "Base.Speaker");
      this.addItemOnGround(this.getRandomFreeSquareFullZone(this, var1), "Base.Speaker");
      this.addItemOnGround(this.getRandomFreeSquareFullZone(this, var1), "Base.Drumstick");
      this.addZombiesOnSquare(1, "Punk", 0, this.getRandomFreeSquareFullZone(this, var1));
      this.addZombiesOnSquare(1, "Punk", 0, this.getRandomFreeSquareFullZone(this, var1));
      this.addZombiesOnSquare(1, "Punk", 0, this.getRandomFreeSquareFullZone(this, var1));
      this.addZombiesOnSquare(1, "Punk", 0, this.getRandomFreeSquareFullZone(this, var1));
      this.addZombiesOnSquare(1, "Punk", 100, this.getRandomFreeSquareFullZone(this, var1));
   }
}

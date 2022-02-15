package zombie.randomizedWorld.randomizedDeadSurvivor;

import java.util.ArrayList;
import zombie.characterTextures.BloodBodyPartType;
import zombie.characters.IsoZombie;
import zombie.core.Rand;
import zombie.iso.BuildingDef;
import zombie.iso.IsoDirections;
import zombie.iso.RoomDef;
import zombie.iso.objects.IsoDeadBody;

public final class RDSHockeyPsycho extends RandomizedDeadSurvivorBase {
   public RDSHockeyPsycho() {
      this.name = "Hockey Psycho (friday 13th!)";
      this.setUnique(true);
      this.setChance(1);
   }

   public void randomizeDeadSurvivor(BuildingDef var1) {
      RoomDef var2 = this.getLivingRoomOrKitchen(var1);
      ArrayList var3 = this.addZombies(var1, 1, "HockeyPsycho", 0, var2);
      if (var3 != null && !var3.isEmpty()) {
         IsoZombie var4 = (IsoZombie)var3.get(0);
         var4.addBlood(BloodBodyPartType.Head, true, true, true);

         for(int var5 = 0; var5 < 10; ++var5) {
            var4.addBlood((BloodBodyPartType)null, true, false, true);
            var4.addDirt((BloodBodyPartType)null, Rand.Next(0, 3), true);
         }
      }

      for(int var6 = 0; var6 < 10; ++var6) {
         IsoDeadBody var7 = createRandomDeadBody(this.getRandomRoom(var1, 2), Rand.Next(5, 20));
         if (var7 != null) {
            this.addTraitOfBlood(IsoDirections.getRandom(), 15, (int)var7.x, (int)var7.y, (int)var7.z);
            this.addTraitOfBlood(IsoDirections.getRandom(), 15, (int)var7.x, (int)var7.y, (int)var7.z);
            this.addTraitOfBlood(IsoDirections.getRandom(), 15, (int)var7.x, (int)var7.y, (int)var7.z);
         }
      }

   }
}

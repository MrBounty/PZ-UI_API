package zombie.randomizedWorld.randomizedDeadSurvivor;

import java.util.ArrayList;
import zombie.characterTextures.BloodBodyPartType;
import zombie.characters.IsoZombie;
import zombie.core.Rand;
import zombie.iso.BuildingDef;
import zombie.iso.RoomDef;
import zombie.iso.objects.IsoDeadBody;

public final class RDSCorpsePsycho extends RandomizedDeadSurvivorBase {
   public RDSCorpsePsycho() {
      this.name = "Corpse Psycho";
      this.setChance(1);
      this.setMinimumDays(120);
      this.setUnique(true);
   }

   public void randomizeDeadSurvivor(BuildingDef var1) {
      RoomDef var2 = this.getRoom(var1, "kitchen");
      int var3 = Rand.Next(3, 7);

      for(int var4 = 0; var4 < var3; ++var4) {
         IsoDeadBody var5 = RandomizedDeadSurvivorBase.createRandomDeadBody(var2, Rand.Next(5, 10));
         if (var5 != null) {
            super.addBloodSplat(var5.getCurrentSquare(), Rand.Next(7, 12));
         }
      }

      ArrayList var6 = super.addZombies(var1, 1, "Doctor", (Integer)null, var2);
      if (!var6.isEmpty()) {
         for(int var7 = 0; var7 < 8; ++var7) {
            ((IsoZombie)var6.get(0)).addBlood((BloodBodyPartType)null, false, true, false);
         }

      }
   }
}

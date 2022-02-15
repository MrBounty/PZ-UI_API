package zombie.randomizedWorld.randomizedDeadSurvivor;

import zombie.characters.IsoGameCharacter;
import zombie.iso.BuildingDef;
import zombie.iso.RoomDef;
import zombie.iso.objects.IsoDeadBody;

public final class RDSSuicidePact extends RandomizedDeadSurvivorBase {
   public RDSSuicidePact() {
      this.name = "Suicide Pact";
      this.setChance(7);
      this.setMinimumDays(60);
   }

   public void randomizeDeadSurvivor(BuildingDef var1) {
      RoomDef var2 = this.getLivingRoomOrKitchen(var1);
      IsoGameCharacter var3 = RandomizedDeadSurvivorBase.createRandomZombieForCorpse(var2);
      if (var3 != null) {
         var3.addVisualDamage("ZedDmg_HEAD_Bullet");
         IsoDeadBody var4 = RandomizedDeadSurvivorBase.createBodyFromZombie(var3);
         if (var4 != null) {
            this.addBloodSplat(var4.getSquare(), 4);
            var4.setPrimaryHandItem(this.addWeapon("Base.Pistol", true));
            var3 = RandomizedDeadSurvivorBase.createRandomZombieForCorpse(var2);
            if (var3 != null) {
               var3.addVisualDamage("ZedDmg_HEAD_Bullet");
               var4 = RandomizedDeadSurvivorBase.createBodyFromZombie(var3);
               if (var4 != null) {
                  this.addBloodSplat(var4.getSquare(), 4);
               }
            }
         }
      }
   }
}

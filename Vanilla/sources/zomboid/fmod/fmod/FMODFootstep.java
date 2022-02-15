package fmod.fmod;

import zombie.characters.IsoGameCharacter;
import zombie.characters.IsoPlayer;
import zombie.iso.IsoGridSquare;
import zombie.iso.IsoObject;

public class FMODFootstep {
   public String wood;
   public String concrete;
   public String grass;
   public String upstairs;
   public String woodCreak;

   public FMODFootstep(String var1, String var2, String var3, String var4) {
      this.grass = var1;
      this.wood = var2;
      this.concrete = var3;
      this.upstairs = var4;
      this.woodCreak = "HumanFootstepFloorCreaking";
   }

   public boolean isUpstairs(IsoGameCharacter var1) {
      IsoGridSquare var2 = IsoPlayer.getInstance().getCurrentSquare();
      return var2.getZ() < var1.getCurrentSquare().getZ();
   }

   public String getSoundToPlay(IsoGameCharacter var1) {
      if (FMODManager.instance.getNumListeners() == 1) {
         for(int var2 = 0; var2 < IsoPlayer.numPlayers; ++var2) {
            IsoPlayer var3 = IsoPlayer.players[var2];
            if (var3 != null && var3 != var1 && !var3.Traits.Deaf.isSet()) {
               if ((int)var3.getZ() < (int)var1.getZ()) {
                  return this.upstairs;
               }
               break;
            }
         }
      }

      IsoObject var5 = var1.getCurrentSquare().getFloor();
      if (var5 != null && var5.getSprite() != null && var5.getSprite().getName() != null) {
         String var6 = var5.getSprite().getName();
         if (var6.startsWith("blends_natural_01")) {
            return this.grass;
         } else if (var6.startsWith("floors_interior_tilesandwood_01_")) {
            int var4 = Integer.parseInt(var6.replaceFirst("floors_interior_tilesandwood_01_", ""));
            return var4 > 40 && var4 < 48 ? this.wood : this.concrete;
         } else if (var6.startsWith("carpentry_02_")) {
            return this.wood;
         } else {
            return var6.startsWith("floors_interior_carpet_") ? this.wood : this.concrete;
         }
      } else {
         return this.concrete;
      }
   }
}

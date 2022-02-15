package zombie.audio.parameters;

import fmod.fmod.FMODManager;
import zombie.audio.FMODLocalParameter;
import zombie.characters.IsoGameCharacter;
import zombie.characters.IsoPlayer;
import zombie.core.properties.PropertyContainer;
import zombie.iso.IsoGridSquare;
import zombie.iso.IsoObject;
import zombie.iso.SpriteDetails.IsoFlagType;
import zombie.iso.objects.IsoWorldInventoryObject;
import zombie.util.list.PZArrayList;

public final class ParameterFootstepMaterial extends FMODLocalParameter {
   private final IsoGameCharacter character;

   public ParameterFootstepMaterial(IsoGameCharacter var1) {
      super("FootstepMaterial");
      this.character = var1;
   }

   public float calculateCurrentValue() {
      return (float)this.getMaterial().label;
   }

   private ParameterFootstepMaterial.FootstepMaterial getMaterial() {
      if (FMODManager.instance.getNumListeners() == 1) {
         for(int var1 = 0; var1 < IsoPlayer.numPlayers; ++var1) {
            IsoPlayer var2 = IsoPlayer.players[var1];
            if (var2 != null && var2 != this.character && !var2.Traits.Deaf.isSet()) {
               if ((int)var2.getZ() < (int)this.character.getZ()) {
                  return ParameterFootstepMaterial.FootstepMaterial.Upstairs;
               }
               break;
            }
         }
      }

      Object var9 = null;
      IsoObject var10 = null;
      IsoGridSquare var3 = this.character.getCurrentSquare();
      if (var3 != null) {
         PZArrayList var4 = var3.getObjects();

         for(int var5 = 0; var5 < var4.size(); ++var5) {
            IsoObject var6 = (IsoObject)var4.get(var5);
            if (!(var6 instanceof IsoWorldInventoryObject)) {
               PropertyContainer var7 = var6.getProperties();
               if (var7 != null) {
                  if (var7.Is(IsoFlagType.solidfloor)) {
                     ;
                  }

                  if (var7.Is("FootstepMaterial")) {
                     var10 = var6;
                  }
               }
            }
         }
      }

      if (var10 != null) {
         try {
            String var11 = var10.getProperties().Val("FootstepMaterial");
            return ParameterFootstepMaterial.FootstepMaterial.valueOf(var11);
         } catch (IllegalArgumentException var8) {
            boolean var12 = true;
         }
      }

      return ParameterFootstepMaterial.FootstepMaterial.Concrete;
   }

   static enum FootstepMaterial {
      Upstairs(0),
      BrokenGlass(1),
      Concrete(2),
      Grass(3),
      Gravel(4),
      Puddle(5),
      Snow(6),
      Wood(7),
      Carpet(8),
      Dirt(9),
      Sand(10),
      Ceramic(11),
      Metal(12);

      final int label;

      private FootstepMaterial(int var3) {
         this.label = var3;
      }

      // $FF: synthetic method
      private static ParameterFootstepMaterial.FootstepMaterial[] $values() {
         return new ParameterFootstepMaterial.FootstepMaterial[]{Upstairs, BrokenGlass, Concrete, Grass, Gravel, Puddle, Snow, Wood, Carpet, Dirt, Sand, Ceramic, Metal};
      }
   }
}

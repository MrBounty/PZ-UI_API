package zombie.audio.parameters;

import zombie.audio.FMODLocalParameter;
import zombie.characters.IsoGameCharacter;
import zombie.iso.objects.IsoBrokenGlass;

public final class ParameterFootstepMaterial2 extends FMODLocalParameter {
   private final IsoGameCharacter character;

   public ParameterFootstepMaterial2(IsoGameCharacter var1) {
      super("FootstepMaterial2");
      this.character = var1;
   }

   public float calculateCurrentValue() {
      return (float)this.getMaterial().label;
   }

   private ParameterFootstepMaterial2.FootstepMaterial2 getMaterial() {
      if (this.character.getCurrentSquare() == null) {
         return ParameterFootstepMaterial2.FootstepMaterial2.None;
      } else {
         IsoBrokenGlass var1 = this.character.getCurrentSquare().getBrokenGlass();
         if (var1 != null) {
            return ParameterFootstepMaterial2.FootstepMaterial2.BrokenGlass;
         } else {
            float var2 = this.character.getCurrentSquare().getPuddlesInGround();
            if (var2 > 0.5F) {
               return ParameterFootstepMaterial2.FootstepMaterial2.PuddleDeep;
            } else {
               return var2 > 0.1F ? ParameterFootstepMaterial2.FootstepMaterial2.PuddleShallow : ParameterFootstepMaterial2.FootstepMaterial2.None;
            }
         }
      }
   }

   static enum FootstepMaterial2 {
      None(0),
      BrokenGlass(1),
      PuddleShallow(2),
      PuddleDeep(3);

      final int label;

      private FootstepMaterial2(int var3) {
         this.label = var3;
      }

      // $FF: synthetic method
      private static ParameterFootstepMaterial2.FootstepMaterial2[] $values() {
         return new ParameterFootstepMaterial2.FootstepMaterial2[]{None, BrokenGlass, PuddleShallow, PuddleDeep};
      }
   }
}

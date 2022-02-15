package zombie.audio.parameters;

import zombie.audio.FMODLocalParameter;
import zombie.characters.IsoGameCharacter;

public final class ParameterCharacterMovementSpeed extends FMODLocalParameter {
   private final IsoGameCharacter character;
   private ParameterCharacterMovementSpeed.MovementType movementType;

   public ParameterCharacterMovementSpeed(IsoGameCharacter var1) {
      super("CharacterMovementSpeed");
      this.movementType = ParameterCharacterMovementSpeed.MovementType.Walk;
      this.character = var1;
   }

   public float calculateCurrentValue() {
      return (float)this.movementType.label;
   }

   public void setMovementType(ParameterCharacterMovementSpeed.MovementType var1) {
      this.movementType = var1;
   }

   public static enum MovementType {
      SneakWalk(0),
      SneakRun(1),
      Strafe(2),
      Walk(3),
      Run(4),
      Sprint(5);

      public final int label;

      private MovementType(int var3) {
         this.label = var3;
      }

      // $FF: synthetic method
      private static ParameterCharacterMovementSpeed.MovementType[] $values() {
         return new ParameterCharacterMovementSpeed.MovementType[]{SneakWalk, SneakRun, Strafe, Walk, Run, Sprint};
      }
   }
}

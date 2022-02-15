package zombie.audio.parameters;

import zombie.audio.FMODLocalParameter;
import zombie.characters.IsoZombie;
import zombie.core.math.PZMath;

public final class ParameterPlayerDistance extends FMODLocalParameter {
   private final IsoZombie zombie;

   public ParameterPlayerDistance(IsoZombie var1) {
      super("PlayerDistance");
      this.zombie = var1;
   }

   public float calculateCurrentValue() {
      return this.zombie.target == null ? 1000.0F : (float)((int)PZMath.ceil(this.zombie.DistToProper(this.zombie.target)));
   }
}

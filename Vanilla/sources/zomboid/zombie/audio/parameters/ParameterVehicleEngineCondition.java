package zombie.audio.parameters;

import zombie.audio.FMODLocalParameter;
import zombie.core.math.PZMath;
import zombie.vehicles.BaseVehicle;
import zombie.vehicles.VehiclePart;

public class ParameterVehicleEngineCondition extends FMODLocalParameter {
   private final BaseVehicle vehicle;

   public ParameterVehicleEngineCondition(BaseVehicle var1) {
      super("VehicleEngineCondition");
      this.vehicle = var1;
   }

   public float calculateCurrentValue() {
      VehiclePart var1 = this.vehicle.getPartById("Engine");
      return var1 == null ? 100.0F : (float)PZMath.clamp(var1.getCondition(), 0, 100);
   }
}

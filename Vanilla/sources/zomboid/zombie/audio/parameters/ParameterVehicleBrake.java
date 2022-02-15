package zombie.audio.parameters;

import zombie.audio.FMODLocalParameter;
import zombie.vehicles.BaseVehicle;

public class ParameterVehicleBrake extends FMODLocalParameter {
   private final BaseVehicle vehicle;

   public ParameterVehicleBrake(BaseVehicle var1) {
      super("VehicleBrake");
      this.vehicle = var1;
   }

   public float calculateCurrentValue() {
      return this.vehicle.getController().isBrakePedalPressed() ? 1.0F : 0.0F;
   }
}

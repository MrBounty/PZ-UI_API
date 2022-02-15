package zombie.audio.parameters;

import zombie.audio.FMODLocalParameter;
import zombie.vehicles.BaseVehicle;

public class ParameterVehicleLoad extends FMODLocalParameter {
   private final BaseVehicle vehicle;

   public ParameterVehicleLoad(BaseVehicle var1) {
      super("VehicleLoad");
      this.vehicle = var1;
   }

   public float calculateCurrentValue() {
      return this.vehicle.getController().isGasPedalPressed() ? 1.0F : 0.0F;
   }
}

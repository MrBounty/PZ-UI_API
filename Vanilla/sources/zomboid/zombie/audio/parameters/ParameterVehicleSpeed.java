package zombie.audio.parameters;

import zombie.audio.FMODLocalParameter;
import zombie.vehicles.BaseVehicle;

public class ParameterVehicleSpeed extends FMODLocalParameter {
   private final BaseVehicle vehicle;

   public ParameterVehicleSpeed(BaseVehicle var1) {
      super("VehicleSpeed");
      this.vehicle = var1;
   }

   public float calculateCurrentValue() {
      return (float)Math.round(Math.abs(this.vehicle.getCurrentSpeedKmHour()));
   }
}

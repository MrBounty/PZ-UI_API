package zombie.audio.parameters;

import zombie.audio.FMODLocalParameter;
import zombie.vehicles.BaseVehicle;

public class ParameterVehicleGear extends FMODLocalParameter {
   private final BaseVehicle vehicle;

   public ParameterVehicleGear(BaseVehicle var1) {
      super("VehicleGear");
      this.vehicle = var1;
   }

   public float calculateCurrentValue() {
      return (float)(this.vehicle.getTransmissionNumber() + 1);
   }
}

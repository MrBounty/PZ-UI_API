package zombie.audio.parameters;

import zombie.audio.FMODLocalParameter;
import zombie.core.math.PZMath;
import zombie.vehicles.BaseVehicle;

public class ParameterVehicleRPM extends FMODLocalParameter {
   private final BaseVehicle vehicle;

   public ParameterVehicleRPM(BaseVehicle var1) {
      super("VehicleRPM");
      this.vehicle = var1;
   }

   public float calculateCurrentValue() {
      float var1 = PZMath.clamp((float)this.vehicle.getEngineSpeed(), 0.0F, 7000.0F);
      float var2 = this.vehicle.getScript().getEngineIdleSpeed();
      float var3 = var2 * 1.1F;
      float var5 = 800.0F;
      float var6 = 7000.0F;
      float var4;
      if (var1 < var3) {
         var4 = var1 / var3 * var5;
      } else {
         var4 = var5 + (var1 - var3) / (7000.0F - var3) * (var6 - var5);
      }

      return (float)((int)((var4 + 50.0F - 1.0F) / 50.0F)) * 50.0F;
   }
}

package zombie.audio.parameters;

import zombie.audio.FMODLocalParameter;
import zombie.core.math.PZMath;
import zombie.scripting.objects.VehicleScript;
import zombie.vehicles.BaseVehicle;

public class ParameterVehicleSteer extends FMODLocalParameter {
   private final BaseVehicle vehicle;

   public ParameterVehicleSteer(BaseVehicle var1) {
      super("VehicleSteer");
      this.vehicle = var1;
   }

   public float calculateCurrentValue() {
      float var1 = 0.0F;
      if (!this.vehicle.isEngineRunning()) {
         return var1;
      } else {
         VehicleScript var2 = this.vehicle.getScript();
         if (var2 == null) {
            return var1;
         } else {
            BaseVehicle.WheelInfo[] var3 = this.vehicle.wheelInfo;
            int var4 = 0;

            for(int var5 = var2.getWheelCount(); var4 < var5; ++var4) {
               var1 = PZMath.max(var1, Math.abs(var3[var4].steering));
            }

            return (float)((int)(PZMath.clamp(var1, 0.0F, 1.0F) * 100.0F)) / 100.0F;
         }
      }
   }
}

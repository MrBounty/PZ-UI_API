package zombie.audio.parameters;

import zombie.audio.FMODLocalParameter;
import zombie.core.math.PZMath;
import zombie.network.GameClient;
import zombie.scripting.objects.VehicleScript;
import zombie.vehicles.BaseVehicle;

public class ParameterVehicleSkid extends FMODLocalParameter {
   private final BaseVehicle vehicle;
   private final BaseVehicle.WheelInfo[] wheelInfo;

   public ParameterVehicleSkid(BaseVehicle var1) {
      super("VehicleSkid");
      this.vehicle = var1;
      this.wheelInfo = var1.wheelInfo;
   }

   public float calculateCurrentValue() {
      float var1 = 1.0F;
      if (GameClient.bClient && !this.vehicle.isLocalPhysicSim()) {
         return var1;
      } else {
         VehicleScript var2 = this.vehicle.getScript();
         if (var2 == null) {
            return var1;
         } else {
            int var3 = 0;

            for(int var4 = var2.getWheelCount(); var3 < var4; ++var3) {
               var1 = PZMath.min(var1, this.wheelInfo[var3].skidInfo);
            }

            return (float)((int)(100.0F - PZMath.clamp(var1, 0.0F, 1.0F) * 100.0F)) / 100.0F;
         }
      }
   }
}

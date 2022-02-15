package zombie.audio.parameters;

import zombie.audio.FMODLocalParameter;
import zombie.scripting.objects.VehicleScript;
import zombie.vehicles.BaseVehicle;
import zombie.vehicles.VehiclePart;

public class ParameterVehicleTireMissing extends FMODLocalParameter {
   private final BaseVehicle vehicle;

   public ParameterVehicleTireMissing(BaseVehicle var1) {
      super("VehicleTireMissing");
      this.vehicle = var1;
   }

   public float calculateCurrentValue() {
      boolean var1 = false;
      VehicleScript var2 = this.vehicle.getScript();
      if (var2 != null) {
         for(int var3 = 0; var3 < var2.getWheelCount(); ++var3) {
            VehicleScript.Wheel var4 = var2.getWheel(var3);
            VehiclePart var5 = this.vehicle.getPartById("Tire" + var4.getId());
            if (var5 == null || var5.getInventoryItem() == null) {
               var1 = true;
               break;
            }
         }
      }

      return var1 ? 1.0F : 0.0F;
   }
}

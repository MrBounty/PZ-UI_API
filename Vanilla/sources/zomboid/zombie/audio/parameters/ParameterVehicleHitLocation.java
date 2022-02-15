package zombie.audio.parameters;

import org.joml.Vector3f;
import zombie.audio.FMODLocalParameter;
import zombie.scripting.objects.VehicleScript;
import zombie.vehicles.BaseVehicle;

public class ParameterVehicleHitLocation extends FMODLocalParameter {
   private ParameterVehicleHitLocation.HitLocation location;

   public ParameterVehicleHitLocation() {
      super("VehicleHitLocation");
      this.location = ParameterVehicleHitLocation.HitLocation.Front;
   }

   public float calculateCurrentValue() {
      return (float)this.location.label;
   }

   public static ParameterVehicleHitLocation.HitLocation calculateLocation(BaseVehicle var0, float var1, float var2, float var3) {
      VehicleScript var4 = var0.getScript();
      if (var4 == null) {
         return ParameterVehicleHitLocation.HitLocation.Front;
      } else {
         Vector3f var5 = var0.getLocalPos(var1, var2, var3, (Vector3f)((BaseVehicle.Vector3fObjectPool)BaseVehicle.TL_vector3f_pool.get()).alloc());
         Vector3f var6 = var4.getExtents();
         Vector3f var7 = var4.getCenterOfMassOffset();
         float var8 = var7.z - var6.z / 2.0F;
         float var9 = var7.z + var6.z / 2.0F;
         var8 *= 0.9F;
         var9 *= 0.9F;
         ParameterVehicleHitLocation.HitLocation var10;
         if (var5.z >= var8 && var5.z <= var9) {
            var10 = ParameterVehicleHitLocation.HitLocation.Side;
         } else if (var5.z > 0.0F) {
            var10 = ParameterVehicleHitLocation.HitLocation.Front;
         } else {
            var10 = ParameterVehicleHitLocation.HitLocation.Rear;
         }

         ((BaseVehicle.Vector3fObjectPool)BaseVehicle.TL_vector3f_pool.get()).release(var5);
         return var10;
      }
   }

   public void setLocation(ParameterVehicleHitLocation.HitLocation var1) {
      this.location = var1;
   }

   public static enum HitLocation {
      Front(0),
      Rear(1),
      Side(2);

      final int label;

      private HitLocation(int var3) {
         this.label = var3;
      }

      // $FF: synthetic method
      private static ParameterVehicleHitLocation.HitLocation[] $values() {
         return new ParameterVehicleHitLocation.HitLocation[]{Front, Rear, Side};
      }
   }
}

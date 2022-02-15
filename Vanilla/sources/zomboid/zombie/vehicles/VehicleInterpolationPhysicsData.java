package zombie.vehicles;

public class VehicleInterpolationPhysicsData {
   long time = 0L;
   float force;
   float[] data = new float[23];

   VehicleInterpolationPhysicsData() {
   }

   void copy(VehicleInterpolationPhysicsData var1) {
      this.time = var1.time;

      for(int var2 = 0; var2 < this.data.length; ++var2) {
         this.data[var2] = var1.data[var2];
      }

   }
}

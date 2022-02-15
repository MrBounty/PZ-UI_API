package zombie.vehicles;

public class VehicleInterpolationData {
   long time = 0L;
   float x = 0.0F;
   float y = 0.0F;
   float z = 0.0F;
   float qx = 0.0F;
   float qy = 0.0F;
   float qz = 0.0F;
   float qw = 0.0F;
   float vx = 0.0F;
   float vy = 0.0F;
   float vz = 0.0F;
   short w_count = 4;
   float[] w_st = new float[4];
   float[] w_rt = new float[4];
   float[] w_si = new float[4];
   float[] w_sl = new float[4];

   VehicleInterpolationData() {
   }

   void setNumWheels(int var1) {
      this.w_count = (short)var1;
      if (var1 > this.w_st.length) {
         this.w_st = new float[var1];
         this.w_rt = new float[var1];
         this.w_si = new float[var1];
         this.w_sl = new float[var1];
      }

   }

   void copy(VehicleInterpolationData var1) {
      this.time = var1.time;
      this.x = var1.x;
      this.y = var1.y;
      this.z = var1.z;
      this.qx = var1.qx;
      this.qy = var1.qy;
      this.qz = var1.qz;
      this.qw = var1.qw;
      this.vx = var1.vx;
      this.vy = var1.vy;
      this.vz = var1.vz;
      this.setNumWheels(var1.w_count);

      for(int var2 = 0; var2 < var1.w_count; ++var2) {
         this.w_st[var2] = var1.w_st[var2];
         this.w_rt[var2] = var1.w_rt[var2];
         this.w_si[var2] = var1.w_si[var2];
         this.w_sl[var2] = var1.w_sl[var2];
      }

   }
}

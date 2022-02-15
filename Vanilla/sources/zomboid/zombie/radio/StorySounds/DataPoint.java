package zombie.radio.StorySounds;

public final class DataPoint {
   protected float time = 0.0F;
   protected float intensity = 0.0F;

   public DataPoint(float var1, float var2) {
      this.setTime(var1);
      this.setIntensity(var2);
   }

   public float getTime() {
      return this.time;
   }

   public void setTime(float var1) {
      if (var1 < 0.0F) {
         var1 = 0.0F;
      }

      if (var1 > 1.0F) {
         var1 = 1.0F;
      }

      this.time = var1;
   }

   public float getIntensity() {
      return this.intensity;
   }

   public void setIntensity(float var1) {
      if (var1 < 0.0F) {
         var1 = 0.0F;
      }

      if (var1 > 1.0F) {
         var1 = 1.0F;
      }

      this.intensity = var1;
   }
}

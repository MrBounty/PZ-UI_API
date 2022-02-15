package zombie.iso.weather.fx;

public class SteppedUpdateFloat {
   private float current;
   private float step;
   private float target;
   private float min;
   private float max;

   public SteppedUpdateFloat(float var1, float var2, float var3, float var4) {
      this.current = var1;
      this.step = var2;
      this.target = var1;
      this.min = var3;
      this.max = var4;
   }

   public float value() {
      return this.current;
   }

   public void setTarget(float var1) {
      this.target = this.clamp(this.min, this.max, var1);
   }

   public float getTarget() {
      return this.target;
   }

   public void overrideCurrentValue(float var1) {
      this.current = var1;
   }

   private float clamp(float var1, float var2, float var3) {
      var3 = Math.min(var2, var3);
      var3 = Math.max(var1, var3);
      return var3;
   }

   public void update(float var1) {
      if (this.current != this.target) {
         if (this.target > this.current) {
            this.current += this.step * var1;
            if (this.current > this.target) {
               this.current = this.target;
            }
         } else if (this.target < this.current) {
            this.current -= this.step * var1;
            if (this.current < this.target) {
               this.current = this.target;
            }
         }
      }

   }
}

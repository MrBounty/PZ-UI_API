package zombie.vehicles;

public final class EngineRPMData {
   public float gearChange;
   public float afterGearChange;

   public EngineRPMData() {
   }

   public EngineRPMData(float var1, float var2) {
      this.gearChange = var1;
      this.afterGearChange = var2;
   }

   public void reset() {
      this.gearChange = 0.0F;
      this.afterGearChange = 0.0F;
   }
}

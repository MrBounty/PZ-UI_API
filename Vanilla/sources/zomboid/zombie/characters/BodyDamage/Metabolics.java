package zombie.characters.BodyDamage;

public enum Metabolics {
   Sleeping(0.8F),
   SeatedResting(1.0F),
   StandingAtRest(1.2F),
   SedentaryActivity(1.2F),
   Default(1.6F),
   DrivingCar(1.4F),
   LightDomestic(1.9F),
   HeavyDomestic(2.9F),
   DefaultExercise(3.0F),
   UsingTools(3.4F),
   LightWork(4.3F),
   MediumWork(5.4F),
   DiggingSpade(6.5F),
   HeavyWork(7.0F),
   ForestryAxe(8.5F),
   Walking2kmh(1.9F),
   Walking5kmh(3.1F),
   Running10kmh(6.5F),
   Running15kmh(9.5F),
   JumpFence(4.0F),
   ClimbRope(8.0F),
   Fitness(6.0F),
   FitnessHeavy(9.0F),
   MAX(10.3F);

   private final float met;

   private Metabolics(float var3) {
      this.met = var3;
   }

   public float getMet() {
      return this.met;
   }

   public float getWm2() {
      return MetToWm2(this.met);
   }

   public float getW() {
      return MetToW(this.met);
   }

   public float getBtuHr() {
      return MetToBtuHr(this.met);
   }

   public static float MetToWm2(float var0) {
      return 58.0F * var0;
   }

   public static float MetToW(float var0) {
      return MetToWm2(var0) * 1.8F;
   }

   public static float MetToBtuHr(float var0) {
      return 356.0F * var0;
   }

   // $FF: synthetic method
   private static Metabolics[] $values() {
      return new Metabolics[]{Sleeping, SeatedResting, StandingAtRest, SedentaryActivity, Default, DrivingCar, LightDomestic, HeavyDomestic, DefaultExercise, UsingTools, LightWork, MediumWork, DiggingSpade, HeavyWork, ForestryAxe, Walking2kmh, Walking5kmh, Running10kmh, Running15kmh, JumpFence, ClimbRope, Fitness, FitnessHeavy, MAX};
   }
}

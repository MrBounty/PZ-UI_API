package zombie.characters.BodyDamage;

import java.nio.ByteBuffer;
import zombie.GameTime;
import zombie.SandboxOptions;
import zombie.ai.states.ClimbOverFenceState;
import zombie.ai.states.ClimbThroughWindowState;
import zombie.ai.states.SwipeStatePlayer;
import zombie.characters.IsoPlayer;
import zombie.characters.CharacterTimedActions.BaseAction;
import zombie.characters.skills.PerkFactory;
import zombie.network.GameClient;
import zombie.network.GameServer;

public final class Nutrition {
   private IsoPlayer parent;
   private float carbohydrates = 0.0F;
   private float lipids = 0.0F;
   private float proteins = 0.0F;
   private float calories = 0.0F;
   private float carbohydratesDecreraseFemale = 0.0032F;
   private float carbohydratesDecreraseMale = 0.0035F;
   private float lipidsDecreraseFemale = 7.0E-4F;
   private float lipidsDecreraseMale = 0.00113F;
   private float proteinsDecreraseFemale = 7.0E-4F;
   private float proteinsDecreraseMale = 8.6E-4F;
   private float caloriesDecreraseFemaleNormal = 1.0E-5F;
   private float caloriesDecreaseMaleNormal = 0.016F;
   private float caloriesDecreraseFemaleExercise = 1.0E-5F;
   private float caloriesDecreaseMaleExercise = 0.13F;
   private float caloriesDecreraseFemaleSleeping = 0.01F;
   private float caloriesDecreaseMaleSleeping = 0.003F;
   private int caloriesToGainWeightMale = 1100;
   private int caloriesToGainWeightMaxMale = 4000;
   private int caloriesToGainWeightFemale = 1000;
   private int caloriesToGainWeightMaxFemale = 3000;
   private int caloriesDecreaseMax = 2500;
   private float weightGain = 1.3E-5F;
   private float weightLoss = 8.5E-6F;
   private float weight = 60.0F;
   private int updatedWeight = 0;
   private boolean isFemale = false;
   private int syncWeightTimer = 0;
   private float caloriesMax = 0.0F;
   private float caloriesMin = 0.0F;
   private boolean incWeight = false;
   private boolean incWeightLot = false;
   private boolean decWeight = false;

   public Nutrition(IsoPlayer var1) {
      this.parent = var1;
      if (this.isFemale) {
         this.setWeight(60.0F);
      } else {
         this.setWeight(80.0F);
      }

      this.setCalories(800.0F);
   }

   public void update() {
      if (!GameServer.bServer) {
         if (SandboxOptions.instance.Nutrition.getValue()) {
            if (this.parent != null && !this.parent.isDead()) {
               if (!GameClient.bClient || this.parent.isLocalPlayer()) {
                  this.setCarbohydrates(this.getCarbohydrates() - (this.isFemale ? this.carbohydratesDecreraseFemale : this.carbohydratesDecreraseMale) * GameTime.getInstance().getGameWorldSecondsSinceLastUpdate());
                  this.setLipids(this.getLipids() - (this.isFemale ? this.lipidsDecreraseFemale : this.lipidsDecreraseMale) * GameTime.getInstance().getGameWorldSecondsSinceLastUpdate());
                  this.setProteins(this.getProteins() - (this.isFemale ? this.proteinsDecreraseFemale : this.proteinsDecreraseMale) * GameTime.getInstance().getGameWorldSecondsSinceLastUpdate());
                  this.updateCalories();
                  this.updateWeight();
               }
            }
         }
      }
   }

   private void updateCalories() {
      float var1 = 1.0F;
      if (!this.parent.getCharacterActions().isEmpty()) {
         var1 = ((BaseAction)this.parent.getCharacterActions().get(0)).caloriesModifier;
      }

      if (this.parent.isCurrentState(SwipeStatePlayer.instance()) || this.parent.isCurrentState(ClimbOverFenceState.instance()) || this.parent.isCurrentState(ClimbThroughWindowState.instance())) {
         var1 = 8.0F;
      }

      float var2 = 1.0F;
      if (this.parent.getBodyDamage() != null && this.parent.getBodyDamage().getThermoregulator() != null) {
         var2 = (float)this.parent.getBodyDamage().getThermoregulator().getEnergyMultiplier();
      }

      float var3 = this.getWeight() / 80.0F;
      if (this.parent.IsRunning()) {
         var1 = 1.0F;
         this.setCalories(this.getCalories() - (this.isFemale ? this.caloriesDecreraseFemaleExercise : this.caloriesDecreaseMaleExercise) * var1 * var3 * GameTime.getInstance().getGameWorldSecondsSinceLastUpdate());
      } else if (this.parent.isAsleep()) {
         this.setCalories(this.getCalories() - (this.isFemale ? this.caloriesDecreraseFemaleSleeping : this.caloriesDecreaseMaleSleeping) * var1 * var2 * var3 * GameTime.getInstance().getGameWorldSecondsSinceLastUpdate());
      } else {
         this.setCalories(this.getCalories() - (this.isFemale ? this.caloriesDecreraseFemaleNormal : this.caloriesDecreaseMaleNormal) * var1 * var2 * var3 * GameTime.getInstance().getGameWorldSecondsSinceLastUpdate());
      }

      if (this.getCalories() > this.caloriesMax) {
         this.caloriesMax = this.getCalories();
      }

      if (this.getCalories() < this.caloriesMin) {
         this.caloriesMin = this.getCalories();
      }

   }

   private void updateWeight() {
      if (this.parent.isGodMod()) {
         if (this.isFemale) {
            this.setWeight(60.0F);
         } else {
            this.setWeight(80.0F);
         }

         this.setCalories(1000.0F);
      }

      this.setIncWeight(false);
      this.setIncWeightLot(false);
      this.setDecWeight(false);
      float var1 = (float)this.caloriesToGainWeightMale;
      float var2 = (float)this.caloriesToGainWeightMaxMale;
      float var3 = 0.0F;
      if (this.isFemale) {
         var1 = (float)this.caloriesToGainWeightFemale;
         var2 = (float)this.caloriesToGainWeightMaxFemale;
      }

      float var4 = (this.getWeight() - 80.0F) * 40.0F;
      var1 = 1600.0F + var4;
      var3 = (this.getWeight() - 70.0F) * 30.0F;
      if (var3 > 0.0F) {
         var3 = 0.0F;
      }

      float var5;
      if (this.getCalories() > var1) {
         this.setIncWeight(true);
         var5 = this.getCalories() / var2;
         if (var5 > 1.0F) {
            var5 = 1.0F;
         }

         float var6 = this.weightGain;
         if (!(this.getCarbohydrates() > 700.0F) && !(this.getLipids() > 700.0F)) {
            if (this.getCarbohydrates() > 400.0F || this.getLipids() > 400.0F) {
               var6 *= 2.0F;
               this.setIncWeightLot(true);
            }
         } else {
            var6 *= 3.0F;
            this.setIncWeightLot(true);
         }

         this.setWeight(this.getWeight() + var6 * var5 * GameTime.getInstance().getGameWorldSecondsSinceLastUpdate());
      } else if (this.getCalories() < var3) {
         this.setDecWeight(true);
         var5 = Math.abs(this.getCalories()) / (float)this.caloriesDecreaseMax;
         if (var5 > 1.0F) {
            var5 = 1.0F;
         }

         this.setWeight(this.getWeight() - this.weightLoss * var5 * GameTime.getInstance().getGameWorldSecondsSinceLastUpdate());
      }

      ++this.updatedWeight;
      if (this.updatedWeight >= 2000) {
         this.applyTraitFromWeight();
         this.updatedWeight = 0;
      }

      if (GameClient.bClient) {
         ++this.syncWeightTimer;
         if (this.syncWeightTimer >= 5000) {
            GameClient.sendWeight(this.parent);
            this.syncWeightTimer = 0;
         }
      }

   }

   public void save(ByteBuffer var1) {
      var1.putFloat(this.getCalories());
      var1.putFloat(this.getProteins());
      var1.putFloat(this.getLipids());
      var1.putFloat(this.getCarbohydrates());
      var1.putFloat(this.getWeight());
   }

   public void load(ByteBuffer var1) {
      this.setCalories(var1.getFloat());
      this.setProteins(var1.getFloat());
      this.setLipids(var1.getFloat());
      this.setCarbohydrates(var1.getFloat());
      this.setWeight(var1.getFloat());
   }

   public void applyWeightFromTraits() {
      if (this.parent.getTraits() != null && !this.parent.getTraits().isEmpty()) {
         if (this.parent.Traits.Emaciated.isSet()) {
            this.setWeight(50.0F);
         }

         if (this.parent.Traits.VeryUnderweight.isSet()) {
            this.setWeight(60.0F);
         }

         if (this.parent.Traits.Underweight.isSet()) {
            this.setWeight(70.0F);
         }

         if (this.parent.Traits.Overweight.isSet()) {
            this.setWeight(95.0F);
         }

         if (this.parent.Traits.Obese.isSet()) {
            this.setWeight(105.0F);
         }
      }

   }

   public void applyTraitFromWeight() {
      this.parent.getTraits().remove("Underweight");
      this.parent.getTraits().remove("Very Underweight");
      this.parent.getTraits().remove("Emaciated");
      this.parent.getTraits().remove("Overweight");
      this.parent.getTraits().remove("Obese");
      if (this.getWeight() >= 100.0F) {
         this.parent.getTraits().add("Obese");
      }

      if (this.getWeight() >= 85.0F && this.getWeight() < 100.0F) {
         this.parent.getTraits().add("Overweight");
      }

      if (this.getWeight() > 65.0F && this.getWeight() <= 75.0F) {
         this.parent.getTraits().add("Underweight");
      }

      if (this.getWeight() > 50.0F && this.getWeight() <= 65.0F) {
         this.parent.getTraits().add("Very Underweight");
      }

      if (this.getWeight() <= 50.0F) {
         this.parent.getTraits().add("Emaciated");
      }

   }

   public boolean characterHaveWeightTrouble() {
      return this.parent.Traits.Emaciated.isSet() || this.parent.Traits.Obese.isSet() || this.parent.Traits.VeryUnderweight.isSet() || this.parent.Traits.Underweight.isSet() || this.parent.Traits.Overweight.isSet();
   }

   public boolean canAddFitnessXp() {
      if (this.parent.getPerkLevel(PerkFactory.Perks.Fitness) >= 9 && this.characterHaveWeightTrouble()) {
         return false;
      } else if (this.parent.getPerkLevel(PerkFactory.Perks.Fitness) < 6) {
         return true;
      } else {
         return !this.parent.Traits.Emaciated.isSet() && !this.parent.Traits.Obese.isSet() && !this.parent.Traits.VeryUnderweight.isSet();
      }
   }

   public float getCarbohydrates() {
      return this.carbohydrates;
   }

   public void setCarbohydrates(float var1) {
      if (var1 < -500.0F) {
         var1 = -500.0F;
      }

      if (var1 > 1000.0F) {
         var1 = 1000.0F;
      }

      this.carbohydrates = var1;
   }

   public float getProteins() {
      return this.proteins;
   }

   public void setProteins(float var1) {
      if (var1 < -500.0F) {
         var1 = -500.0F;
      }

      if (var1 > 1000.0F) {
         var1 = 1000.0F;
      }

      this.proteins = var1;
   }

   public float getCalories() {
      return this.calories;
   }

   public void setCalories(float var1) {
      if (var1 < -2200.0F) {
         var1 = -2200.0F;
      }

      if (var1 > 3700.0F) {
         var1 = 3700.0F;
      }

      this.calories = var1;
   }

   public float getLipids() {
      return this.lipids;
   }

   public void setLipids(float var1) {
      if (var1 < -500.0F) {
         var1 = -500.0F;
      }

      if (var1 > 1000.0F) {
         var1 = 1000.0F;
      }

      this.lipids = var1;
   }

   public float getWeight() {
      return this.weight;
   }

   public void setWeight(float var1) {
      if (var1 < 35.0F) {
         var1 = 35.0F;
         this.parent.getBodyDamage().ReduceGeneralHealth(this.parent.getBodyDamage().getHealthReductionFromSevereBadMoodles() * GameTime.instance.getMultiplier());
      }

      this.weight = var1;
   }

   public boolean isIncWeight() {
      return this.incWeight;
   }

   public void setIncWeight(boolean var1) {
      this.incWeight = var1;
   }

   public boolean isIncWeightLot() {
      return this.incWeightLot;
   }

   public void setIncWeightLot(boolean var1) {
      this.incWeightLot = var1;
   }

   public boolean isDecWeight() {
      return this.decWeight;
   }

   public void setDecWeight(boolean var1) {
      this.decWeight = var1;
   }
}

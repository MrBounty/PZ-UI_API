package zombie.characters.BodyDamage;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import zombie.GameTime;
import zombie.characterTextures.BloodBodyPartType;
import zombie.characterTextures.BloodClothingType;
import zombie.characters.IsoGameCharacter;
import zombie.characters.IsoPlayer;
import zombie.characters.Stats;
import zombie.characters.Moodles.MoodleType;
import zombie.core.math.PZMath;
import zombie.core.skinnedmodel.visual.ItemVisual;
import zombie.core.skinnedmodel.visual.ItemVisuals;
import zombie.debug.DebugLog;
import zombie.inventory.InventoryItem;
import zombie.inventory.types.Clothing;
import zombie.inventory.types.WeaponType;
import zombie.iso.weather.ClimateManager;
import zombie.iso.weather.Temperature;

public class Thermoregulator_tryouts {
   private final BodyDamage bodyDamage;
   private final IsoGameCharacter character;
   private final IsoPlayer player;
   private final Stats stats;
   private final Nutrition nutrition;
   private final ClimateManager climate;
   private static final ItemVisuals itemVisuals = new ItemVisuals();
   private static final ItemVisuals itemVisualsCache = new ItemVisuals();
   private static final ArrayList coveredParts = new ArrayList();
   private static float SIMULATION_MULTIPLIER = 1.0F;
   private float setPoint = 37.0F;
   private float metabolicRate;
   private float metabolicRateReal;
   private float metabolicTarget;
   private double fluidsMultiplier;
   private double energyMultiplier;
   private double fatigueMultiplier;
   private float bodyHeatDelta;
   private float coreHeatDelta;
   private boolean thermalChevronUp;
   private Thermoregulator_tryouts.ThermalNode core;
   private Thermoregulator_tryouts.ThermalNode[] nodes;
   private float totalHeatRaw;
   private float totalHeat;
   private float primTotal;
   private float secTotal;
   private float externalAirTemperature;
   private float airTemperature;
   private float airAndWindTemp;
   private float rateOfChangeCounter;
   private float coreCelciusCache;
   private float coreRateOfChange;
   private float thermalDamage;
   private float damageCounter;

   public Thermoregulator_tryouts(BodyDamage var1) {
      this.metabolicRate = Metabolics.Default.getMet();
      this.metabolicRateReal = this.metabolicRate;
      this.metabolicTarget = Metabolics.Default.getMet();
      this.fluidsMultiplier = 1.0D;
      this.energyMultiplier = 1.0D;
      this.fatigueMultiplier = 1.0D;
      this.bodyHeatDelta = 0.0F;
      this.coreHeatDelta = 0.0F;
      this.thermalChevronUp = true;
      this.totalHeatRaw = 0.0F;
      this.totalHeat = 0.0F;
      this.primTotal = 0.0F;
      this.secTotal = 0.0F;
      this.externalAirTemperature = 27.0F;
      this.rateOfChangeCounter = 0.0F;
      this.coreCelciusCache = 37.0F;
      this.coreRateOfChange = 0.0F;
      this.thermalDamage = 0.0F;
      this.damageCounter = 0.0F;
      this.bodyDamage = var1;
      this.character = var1.getParentChar();
      this.stats = this.character.getStats();
      if (this.character instanceof IsoPlayer) {
         this.player = (IsoPlayer)this.character;
         this.nutrition = ((IsoPlayer)this.character).getNutrition();
      } else {
         this.player = null;
         this.nutrition = null;
      }

      this.climate = ClimateManager.getInstance();
      this.initNodes();
   }

   public static void setSimulationMultiplier(float var0) {
      SIMULATION_MULTIPLIER = var0;
   }

   public void save(ByteBuffer var1) throws IOException {
      var1.putFloat(this.setPoint);
      var1.putFloat(this.metabolicRate);
      var1.putFloat(this.metabolicTarget);
      var1.putFloat(this.bodyHeatDelta);
      var1.putFloat(this.coreHeatDelta);
      var1.putFloat(this.thermalDamage);
      var1.putFloat(this.damageCounter);
      var1.putInt(this.nodes.length);

      for(int var3 = 0; var3 < this.nodes.length; ++var3) {
         Thermoregulator_tryouts.ThermalNode var2 = this.nodes[var3];
         var1.putInt(BodyPartType.ToIndex(var2.bodyPartType));
         var1.putFloat(var2.celcius);
         var1.putFloat(var2.skinCelcius);
         var1.putFloat(var2.heatDelta);
         var1.putFloat(var2.primaryDelta);
         var1.putFloat(var2.secondaryDelta);
      }

   }

   public void load(ByteBuffer var1, int var2) throws IOException {
      this.setPoint = var1.getFloat();
      this.metabolicRate = var1.getFloat();
      this.metabolicTarget = var1.getFloat();
      this.bodyHeatDelta = var1.getFloat();
      this.coreHeatDelta = var1.getFloat();
      this.thermalDamage = var1.getFloat();
      this.damageCounter = var1.getFloat();
      int var3 = var1.getInt();

      for(int var5 = 0; var5 < var3; ++var5) {
         int var6 = var1.getInt();
         float var7 = var1.getFloat();
         float var8 = var1.getFloat();
         float var9 = var1.getFloat();
         float var10 = var1.getFloat();
         float var11 = var1.getFloat();
         Thermoregulator_tryouts.ThermalNode var4 = this.getNodeForType(BodyPartType.FromIndex(var6));
         if (var4 != null) {
            var4.celcius = var7;
            var4.skinCelcius = var8;
            var4.heatDelta = var9;
            var4.primaryDelta = var10;
            var4.secondaryDelta = var11;
         } else {
            DebugLog.log("Couldnt load node: " + BodyPartType.ToString(BodyPartType.FromIndex(var6)));
         }
      }

   }

   public void reset() {
      this.setPoint = 37.0F;
      this.metabolicRate = Metabolics.Default.getMet();
      this.metabolicTarget = this.metabolicRate;
      this.core.celcius = 37.0F;
      this.bodyHeatDelta = 0.0F;
      this.coreHeatDelta = 0.0F;
      this.thermalDamage = 0.0F;

      for(int var2 = 0; var2 < this.nodes.length; ++var2) {
         Thermoregulator_tryouts.ThermalNode var1 = this.nodes[var2];
         if (var1 != this.core) {
            var1.celcius = 35.0F;
         }

         var1.primaryDelta = 0.0F;
         var1.secondaryDelta = 0.0F;
         var1.skinCelcius = 33.0F;
         var1.heatDelta = 0.0F;
      }

   }

   private void initNodes() {
      ArrayList var1 = new ArrayList();

      int var2;
      for(var2 = 0; var2 < this.bodyDamage.getBodyParts().size(); ++var2) {
         BodyPart var3 = (BodyPart)this.bodyDamage.getBodyParts().get(var2);
         Thermoregulator_tryouts.ThermalNode var4 = null;
         switch(var3.getType()) {
         case Torso_Upper:
            var4 = new Thermoregulator_tryouts.ThermalNode(true, 37.0F, var3, 0.25F);
            this.core = var4;
            break;
         case Head:
            var4 = new Thermoregulator_tryouts.ThermalNode(37.0F, var3, 1.0F);
            break;
         case Neck:
            var4 = new Thermoregulator_tryouts.ThermalNode(37.0F, var3, 0.5F);
            break;
         case Torso_Lower:
            var4 = new Thermoregulator_tryouts.ThermalNode(37.0F, var3, 0.25F);
            break;
         case Groin:
            var4 = new Thermoregulator_tryouts.ThermalNode(37.0F, var3, 0.5F);
            break;
         case UpperLeg_L:
         case UpperLeg_R:
            var4 = new Thermoregulator_tryouts.ThermalNode(37.0F, var3, 0.5F);
            break;
         case LowerLeg_L:
         case LowerLeg_R:
            var4 = new Thermoregulator_tryouts.ThermalNode(37.0F, var3, 0.5F);
            break;
         case Foot_L:
         case Foot_R:
            var4 = new Thermoregulator_tryouts.ThermalNode(37.0F, var3, 0.5F);
            break;
         case UpperArm_L:
         case UpperArm_R:
            var4 = new Thermoregulator_tryouts.ThermalNode(37.0F, var3, 0.25F);
            break;
         case ForeArm_L:
         case ForeArm_R:
            var4 = new Thermoregulator_tryouts.ThermalNode(37.0F, var3, 0.25F);
            break;
         case Hand_L:
         case Hand_R:
            var4 = new Thermoregulator_tryouts.ThermalNode(37.0F, var3, 1.0F);
            break;
         default:
            BodyPart var10000 = (BodyPart)this.bodyDamage.getBodyParts().get(var2);
            DebugLog.log("Warning: couldnt init thermal node for body part '" + var10000.getType() + "'.");
         }

         if (var4 != null) {
            var1.add(var4);
         }
      }

      this.nodes = new Thermoregulator_tryouts.ThermalNode[var1.size()];
      var1.toArray(this.nodes);

      for(var2 = 0; var2 < this.nodes.length; ++var2) {
         Thermoregulator_tryouts.ThermalNode var7 = this.nodes[var2];
         BodyPartType var8 = BodyPartContacts.getParent(var7.bodyPartType);
         if (var8 != null) {
            var7.upstream = this.getNodeForType(var8);
         }

         BodyPartType[] var5 = BodyPartContacts.getChildren(var7.bodyPartType);
         if (var5 != null && var5.length > 0) {
            var7.downstream = new Thermoregulator_tryouts.ThermalNode[var5.length];

            for(int var6 = 0; var6 < var5.length; ++var6) {
               var7.downstream[var6] = this.getNodeForType(var5[var6]);
            }
         }
      }

      this.core.celcius = this.setPoint;
   }

   public Thermoregulator_tryouts.ThermalNode getNodeForType(BodyPartType var1) {
      for(int var2 = 0; var2 < this.nodes.length; ++var2) {
         if (this.nodes[var2].bodyPartType == var1) {
            return this.nodes[var2];
         }
      }

      return null;
   }

   public Thermoregulator_tryouts.ThermalNode getNodeForBloodType(BloodBodyPartType var1) {
      for(int var2 = 0; var2 < this.nodes.length; ++var2) {
         if (this.nodes[var2].bloodBPT == var1) {
            return this.nodes[var2];
         }
      }

      return null;
   }

   public float getBodyHeatDelta() {
      return this.bodyHeatDelta;
   }

   public double getFluidsMultiplier() {
      return this.fluidsMultiplier;
   }

   public double getEnergyMultiplier() {
      return this.energyMultiplier;
   }

   public double getFatigueMultiplier() {
      return this.fatigueMultiplier;
   }

   public float getMovementModifier() {
      float var1 = 1.0F;
      if (this.player != null) {
         int var2 = this.player.getMoodles().getMoodleLevel(MoodleType.Hypothermia);
         if (var2 == 2) {
            var1 = 0.66F;
         } else if (var2 == 3) {
            var1 = 0.33F;
         } else if (var2 == 4) {
            var1 = 0.0F;
         }

         var2 = this.player.getMoodles().getMoodleLevel(MoodleType.Hyperthermia);
         if (var2 == 2) {
            var1 = 0.66F;
         } else if (var2 == 3) {
            var1 = 0.33F;
         } else if (var2 == 4) {
            var1 = 0.0F;
         }
      }

      return var1;
   }

   public float getCombatModifier() {
      float var1 = 1.0F;
      if (this.player != null) {
         int var2 = this.player.getMoodles().getMoodleLevel(MoodleType.Hypothermia);
         if (var2 == 2) {
            var1 = 0.66F;
         } else if (var2 == 3) {
            var1 = 0.33F;
         } else if (var2 == 4) {
            var1 = 0.1F;
         }

         var2 = this.player.getMoodles().getMoodleLevel(MoodleType.Hyperthermia);
         if (var2 == 2) {
            var1 = 0.66F;
         } else if (var2 == 3) {
            var1 = 0.33F;
         } else if (var2 == 4) {
            var1 = 0.1F;
         }
      }

      return var1;
   }

   public float getCoreTemperature() {
      return this.core.celcius;
   }

   public float getHeatGeneration() {
      return this.metabolicRateReal;
   }

   public float getMetabolicRate() {
      return this.metabolicRate;
   }

   public float getMetabolicTarget() {
      return this.metabolicTarget;
   }

   public float getMetabolicRateReal() {
      return this.metabolicRateReal;
   }

   public float getSetPoint() {
      return this.setPoint;
   }

   public float getCoreHeatDelta() {
      return this.coreHeatDelta;
   }

   public float getCoreRateOfChange() {
      return this.coreRateOfChange;
   }

   public float getExternalAirTemperature() {
      return this.externalAirTemperature;
   }

   public float getCoreTemperatureUI() {
      float var1 = PZMath.clamp(this.core.celcius, 20.0F, 42.0F);
      if (var1 < 37.0F) {
         var1 = (var1 - 20.0F) / 17.0F * 0.5F;
      } else {
         var1 = 0.5F + (var1 - 37.0F) / 5.0F * 0.5F;
      }

      return var1;
   }

   public float getHeatGenerationUI() {
      float var1 = PZMath.clamp(this.metabolicRateReal, 0.0F, Metabolics.MAX.getMet());
      if (var1 < Metabolics.Default.getMet()) {
         var1 = var1 / Metabolics.Default.getMet() * 0.5F;
      } else {
         var1 = 0.5F + (var1 - Metabolics.Default.getMet()) / (Metabolics.MAX.getMet() - Metabolics.Default.getMet()) * 0.5F;
      }

      return var1;
   }

   public boolean thermalChevronUp() {
      return this.thermalChevronUp;
   }

   public int thermalChevronCount() {
      if (this.coreRateOfChange > 0.01F) {
         return 3;
      } else if (this.coreRateOfChange > 0.001F) {
         return 2;
      } else {
         return this.coreRateOfChange > 1.0E-4F ? 1 : 0;
      }
   }

   public float getCatchAColdDelta() {
      float var1 = 0.0F;
      if (this.player.getMoodles().getMoodleLevel(MoodleType.Hypothermia) < 1) {
         return var1;
      } else {
         for(int var3 = 0; var3 < this.nodes.length; ++var3) {
            Thermoregulator_tryouts.ThermalNode var2 = this.nodes[var3];
            float var4 = 0.0F;
            if (var2.skinCelcius < 33.0F) {
               var4 = (var2.skinCelcius - 20.0F) / 13.0F;
               var4 = 1.0F - var4;
               var4 *= var4;
            }

            float var5 = 0.25F * var4 * var2.skinSurface;
            if (var2.bodyWetness > 0.0F) {
               var5 *= 1.0F + var2.bodyWetness * 1.0F;
            }

            if (var2.clothingWetness > 0.5F) {
               var5 *= 1.0F + (var2.clothingWetness - 0.5F) * 2.0F;
            }

            if (var2.bodyPartType == BodyPartType.Neck) {
               var5 *= 8.0F;
            } else if (var2.bodyPartType == BodyPartType.Torso_Upper) {
               var5 *= 16.0F;
            } else if (var2.bodyPartType == BodyPartType.Head) {
               var5 *= 4.0F;
            }

            var1 += var5;
         }

         if (this.player.getMoodles().getMoodleLevel(MoodleType.Hypothermia) > 1) {
            var1 *= (float)this.player.getMoodles().getMoodleLevel(MoodleType.Hypothermia);
         }

         return var1;
      }
   }

   public float getTimedActionTimeModifier() {
      float var1 = 1.0F;

      for(int var4 = 0; var4 < this.nodes.length; ++var4) {
         Thermoregulator_tryouts.ThermalNode var3 = this.nodes[var4];
         float var5 = 0.0F;
         if (var3.skinCelcius < 33.0F) {
            var5 = (var3.skinCelcius - 20.0F) / 13.0F;
            var5 = 1.0F - var5;
            var5 *= var5;
         }

         float var2 = 0.25F * var5 * var3.skinSurface;
         if (var3.bodyPartType != BodyPartType.Hand_R && var3.bodyPartType != BodyPartType.Hand_L) {
            if (var3.bodyPartType != BodyPartType.ForeArm_R && var3.bodyPartType != BodyPartType.ForeArm_L) {
               if (var3.bodyPartType == BodyPartType.UpperArm_R || var3.bodyPartType == BodyPartType.UpperArm_L) {
                  var1 += 0.1F * var2;
               }
            } else {
               var1 += 0.15F * var2;
            }
         } else {
            var1 += 0.3F * var2;
         }
      }

      return var1;
   }

   public static float getSkinCelciusMin() {
      return 20.0F;
   }

   public static float getSkinCelciusFavorable() {
      return 33.0F;
   }

   public static float getSkinCelciusMax() {
      return 42.0F;
   }

   public void setMetabolicTarget(Metabolics var1) {
      this.setMetabolicTarget(var1.getMet());
   }

   public void setMetabolicTarget(float var1) {
      if (!(var1 < 0.0F) && !(var1 < this.metabolicTarget)) {
         this.metabolicTarget = var1;
         if (this.metabolicTarget > Metabolics.MAX.getMet()) {
            this.metabolicTarget = Metabolics.MAX.getMet();
         }

      }
   }

   private void updateCoreRateOfChange() {
      this.rateOfChangeCounter += GameTime.instance.getMultiplier();
      if (this.rateOfChangeCounter > 100.0F) {
         this.rateOfChangeCounter = 0.0F;
         this.coreRateOfChange = this.core.celcius - this.coreCelciusCache;
         this.thermalChevronUp = this.coreRateOfChange >= 0.0F;
         this.coreRateOfChange = PZMath.abs(this.coreRateOfChange);
         this.coreCelciusCache = this.core.celcius;
      }

   }

   public float getSimulationMultiplier() {
      return SIMULATION_MULTIPLIER;
   }

   public float getDefaultMultiplier() {
      return this.getSimulationMultiplier(Thermoregulator_tryouts.Multiplier.Default);
   }

   public float getMetabolicRateIncMultiplier() {
      return this.getSimulationMultiplier(Thermoregulator_tryouts.Multiplier.MetabolicRateInc);
   }

   public float getMetabolicRateDecMultiplier() {
      return this.getSimulationMultiplier(Thermoregulator_tryouts.Multiplier.MetabolicRateDec);
   }

   public float getBodyHeatMultiplier() {
      return this.getSimulationMultiplier(Thermoregulator_tryouts.Multiplier.BodyHeat);
   }

   public float getCoreHeatExpandMultiplier() {
      return this.getSimulationMultiplier(Thermoregulator_tryouts.Multiplier.CoreHeatExpand);
   }

   public float getCoreHeatContractMultiplier() {
      return this.getSimulationMultiplier(Thermoregulator_tryouts.Multiplier.CoreHeatContract);
   }

   public float getSkinCelciusMultiplier() {
      return this.getSimulationMultiplier(Thermoregulator_tryouts.Multiplier.SkinCelcius);
   }

   public float getTemperatureAir() {
      return this.climate.getAirTemperatureForCharacter(this.character, false);
   }

   public float getTemperatureAirAndWind() {
      return this.climate.getAirTemperatureForCharacter(this.character, true);
   }

   public float getDbg_totalHeatRaw() {
      return this.totalHeatRaw;
   }

   public float getDbg_totalHeat() {
      return this.totalHeat;
   }

   public float getCoreCelcius() {
      return this.core != null ? this.core.celcius : 0.0F;
   }

   public float getDbg_primTotal() {
      return this.primTotal;
   }

   public float getDbg_secTotal() {
      return this.secTotal;
   }

   private float getSimulationMultiplier(Thermoregulator_tryouts.Multiplier var1) {
      float var2 = GameTime.instance.getMultiplier();
      switch(var1) {
      case MetabolicRateInc:
         var2 *= 0.001F;
         break;
      case MetabolicRateDec:
         var2 *= 4.0E-4F;
         break;
      case BodyHeat:
         var2 *= 2.5E-4F;
         break;
      case CoreHeatExpand:
         var2 *= 5.0E-5F;
         break;
      case CoreHeatContract:
         var2 *= 5.0E-4F;
         break;
      case SkinCelcius:
      case SkinCelciusExpand:
         var2 *= 0.0025F;
         break;
      case SkinCelciusContract:
         var2 *= 0.005F;
         break;
      case PrimaryDelta:
         var2 *= 5.0E-4F;
         break;
      case SecondaryDelta:
         var2 *= 2.5E-4F;
      case Default:
      }

      return var2 * SIMULATION_MULTIPLIER;
   }

   public float getThermalDamage() {
      return this.thermalDamage;
   }

   private void updateThermalDamage(float var1) {
      this.damageCounter += GameTime.instance.getRealworldSecondsSinceLastUpdate();
      if (this.damageCounter > 1.0F) {
         this.damageCounter = 0.0F;
         float var2;
         float var3;
         if (this.player.getMoodles().getMoodleLevel(MoodleType.Hypothermia) == 4 && var1 < 0.0F && this.core.celcius - this.coreCelciusCache <= 0.0F) {
            var2 = (this.core.celcius - 20.0F) / 5.0F;
            var2 = 1.0F - var2;
            var3 = 120.0F;
            var3 += 480.0F * var2;
            this.thermalDamage += 1.0F / var3 * PZMath.clamp_01(PZMath.abs(var1) / 10.0F);
         } else if (this.player.getMoodles().getMoodleLevel(MoodleType.Hyperthermia) == 4 && var1 > 37.0F && this.core.celcius - this.coreCelciusCache >= 0.0F) {
            var2 = (this.core.celcius - 41.0F) / 1.0F;
            var3 = 120.0F;
            var3 += 480.0F * var2;
            this.thermalDamage += 1.0F / var3 * PZMath.clamp_01((var1 - 37.0F) / 8.0F);
            this.thermalDamage = Math.min(this.thermalDamage, 0.3F);
         } else {
            this.thermalDamage -= 0.011111111F;
         }

         this.thermalDamage = PZMath.clamp_01(this.thermalDamage);
      }

      this.player.getBodyDamage().ColdDamageStage = this.thermalDamage;
   }

   public void update() {
      this.airTemperature = this.climate.getAirTemperatureForCharacter(this.character, false);
      this.airAndWindTemp = this.climate.getAirTemperatureForCharacter(this.character, true);
      this.externalAirTemperature = this.airTemperature;
      this.setPoint = 37.0F;
      if (this.stats.getSickness() > 0.0F) {
         this.setPoint += this.stats.getSickness() * 2.0F;
      }

      this.updateCoreRateOfChange();
      this.updateMetabolicRate();
      this.updateClothing();
      this.updateTest();
   }

   private void updateMetabolicRate() {
      this.setMetabolicTarget(Metabolics.Default.getMet());
      if (this.player != null) {
         if (this.player.isAttacking()) {
            WeaponType var1 = WeaponType.getWeaponType((IsoGameCharacter)this.player);
            switch(var1) {
            case barehand:
               this.setMetabolicTarget(Metabolics.MediumWork);
               break;
            case twohanded:
               this.setMetabolicTarget(Metabolics.HeavyWork);
               break;
            case onehanded:
               this.setMetabolicTarget(Metabolics.MediumWork);
               break;
            case heavy:
               this.setMetabolicTarget(Metabolics.Running15kmh);
               break;
            case knife:
               this.setMetabolicTarget(Metabolics.LightWork);
               break;
            case spear:
               this.setMetabolicTarget(Metabolics.MediumWork);
               break;
            case handgun:
               this.setMetabolicTarget(Metabolics.UsingTools);
               break;
            case firearm:
               this.setMetabolicTarget(Metabolics.LightWork);
               break;
            case throwing:
               this.setMetabolicTarget(Metabolics.MediumWork);
               break;
            case chainsaw:
               this.setMetabolicTarget(Metabolics.Running15kmh);
            }
         }

         if (this.player.isPlayerMoving()) {
            if (this.player.isSprinting()) {
               this.setMetabolicTarget(Metabolics.Running15kmh);
            } else if (this.player.isRunning()) {
               this.setMetabolicTarget(Metabolics.Running10kmh);
            } else if (this.player.isSneaking()) {
               this.setMetabolicTarget(Metabolics.Walking2kmh);
            } else if (this.player.CurrentSpeed > 0.0F) {
               this.setMetabolicTarget(Metabolics.Walking5kmh);
            }
         }
      }

      float var5 = PZMath.clamp_01(1.0F - this.stats.getEndurance()) * Metabolics.DefaultExercise.getMet();
      this.setMetabolicTarget(var5 * this.getEnergy());
      float var2 = PZMath.clamp_01(this.player.getInventory().getCapacityWeight() / (float)this.player.getMaxWeight());
      float var3 = 1.0F + var2 * var2 * 0.35F;
      this.setMetabolicTarget(this.metabolicTarget * var3);
      float var4;
      if (!PZMath.equal(this.metabolicRate, this.metabolicTarget)) {
         var4 = this.metabolicTarget - this.metabolicRate;
         if (this.metabolicTarget > this.metabolicRate) {
            this.metabolicRate += var4 * this.getSimulationMultiplier(Thermoregulator_tryouts.Multiplier.MetabolicRateInc);
         } else {
            this.metabolicRate += var4 * this.getSimulationMultiplier(Thermoregulator_tryouts.Multiplier.MetabolicRateDec);
         }
      }

      var4 = 1.0F;
      if (this.player.getMoodles().getMoodleLevel(MoodleType.Hypothermia) >= 1) {
         var4 = this.getMovementModifier();
      }

      this.metabolicRateReal = this.metabolicRate * (0.2F + 0.8F * this.getEnergy() * var4);
      this.metabolicTarget = -1.0F;
   }

   private void updateTest() {
      float var1 = 0.0F;

      for(int var3 = 0; var3 < this.nodes.length; ++var3) {
         Thermoregulator_tryouts.ThermalNode var2 = this.nodes[var3];
         var2.calculateInsulation();
         float var4 = this.airTemperature;
         if (this.airAndWindTemp < this.airTemperature) {
            var4 -= (this.airTemperature - this.airAndWindTemp) / (1.0F + var2.windresist);
         }

         var2.heatDelta = var4 - var2.skinCelcius;
         var2.heatDelta /= 1.0F + var2.insulation;
         var2.heatDelta /= 3.75F;
         float var5 = this.metabolicRateReal;
         var5 *= 1.0F + var2.insulation * 0.25F;
         var2.heatDelta += var5;
         float var6 = 1.0F;
         var1 += var2.heatDelta * var2.skinSurface * var6;
         float var7 = this.core.celcius - 20.0F;
         float var8 = this.core.celcius;
         if (var7 < this.airTemperature) {
            var7 = this.airTemperature;
         }

         if (var7 > var8) {
            var7 = var8;
         }

         float var9 = var2.skinCelcius + var2.heatDelta;
         var9 = PZMath.clamp(var9, var7, var8);
         float var10 = var9 - var2.skinCelcius;
         float var11 = 0.0025F;
         if (var2.skinCelcius < 33.0F && var10 < 0.0F || var2.skinCelcius > 33.0F && var10 > 0.0F) {
            var11 = 2.5E-4F;
         }

         var2.skinCelcius += var10 * var11;
      }

      this.coreHeatDelta = var1;
      float var12 = 0.0025F;
      if (this.core.celcius < this.setPoint && this.coreHeatDelta < 0.0F) {
         var12 = 5.0E-5F;
      }

      if (this.core.celcius > this.setPoint && this.coreHeatDelta > 0.0F) {
         var12 = 2.5E-4F;
      }

      Thermoregulator_tryouts.ThermalNode var10000 = this.core;
      var10000.celcius += this.coreHeatDelta * var12;
   }

   private void updateTest_4() {
      float var1 = 0.0F;

      float var4;
      for(int var3 = 0; var3 < this.nodes.length; ++var3) {
         Thermoregulator_tryouts.ThermalNode var2 = this.nodes[var3];
         var2.calculateInsulation();
         var4 = 1.0F - var2.getDistToCore();
         float var5 = this.airTemperature;
         if (this.airAndWindTemp < this.airTemperature) {
            var5 -= (this.airTemperature - this.airAndWindTemp) / (1.0F + var2.windresist);
         }

         var2.heatDelta = var5 - var2.skinCelcius;
         var2.heatDelta /= 1.0F + var2.insulation;
         var2.heatDelta /= 12.0F;
         float var6 = 1.0F;
         var1 += var2.heatDelta * var2.skinSurface * var6;
         float var7 = this.core.celcius - 20.0F;
         float var8 = this.core.celcius;
         if (var7 < this.airTemperature) {
            var7 = this.airTemperature;
         }

         if (var7 > var8) {
            var7 = var8;
         }

         float var9 = this.core.celcius - this.setPoint;
         if (var9 > 0.0F) {
            var9 *= 2.0F;
            var9 *= 1.0F + var4 * 0.25F;
         } else {
            var9 *= 1.0F + var2.distToCore * 0.25F;
         }

         float var10 = var2.skinCelcius + var2.heatDelta + var9;
         var10 = PZMath.clamp(var10, var7, var8);
         float var11 = var10 - var2.skinCelcius;
         float var12 = 0.0025F;
         if (var2.skinCelcius < 33.0F && var11 < 0.0F || var2.skinCelcius > 33.0F && var11 > 0.0F) {
            var12 = 2.5E-4F;
         }

         var2.skinCelcius += var11 * var12;
      }

      float var13 = this.metabolicRateReal / Metabolics.Default.getMet() - 1.0F;
      if (var13 > 0.0F) {
         var13 /= Metabolics.Default.getMet() * 2.0F;
      }

      this.coreHeatDelta = var1 + var13;
      var4 = 0.0025F;
      if (this.core.celcius < this.setPoint && this.coreHeatDelta < 0.0F) {
         var4 = 5.0E-5F;
      }

      if (this.core.celcius > this.setPoint && this.coreHeatDelta > 0.0F) {
         var4 = 2.5E-4F;
      }

      Thermoregulator_tryouts.ThermalNode var10000 = this.core;
      var10000.celcius += this.coreHeatDelta * var4;
   }

   private void updateTest_3() {
      for(int var2 = 0; var2 < this.nodes.length; ++var2) {
         Thermoregulator_tryouts.ThermalNode var1 = this.nodes[var2];
         var1.calculateInsulation();
         float var3 = this.airTemperature;
         if (this.airAndWindTemp < this.airTemperature) {
            var3 -= (this.airTemperature - this.airAndWindTemp) / (1.0F + var1.windresist);
         }

         var1.heatDelta = var3 - var1.skinCelcius;
         var1.heatDelta /= 1.0F + var1.insulation;
         float var4 = 1.0F + (1.0F - var1.getDistToCore() * 0.5F);
         float var5 = this.metabolicRateReal * var4;
         var5 *= 1.0F + var1.insulation * 0.25F;
         var1.heatDelta += var5;
         float var6 = this.core.celcius - 20.0F;
         float var7 = this.core.celcius;
         if (var6 < this.airTemperature) {
            var6 = this.airTemperature;
         }

         if (var6 > var7) {
            var6 = var7;
         }

         float var8 = var1.skinCelcius + var1.heatDelta / 12.0F;
         var8 = PZMath.clamp(var8, var6, var7);
         float var9 = var8 - var1.skinCelcius;
         var1.skinCelcius += var9 * this.getSimulationMultiplier(Thermoregulator_tryouts.Multiplier.SkinCelcius);
      }

   }

   private void updateTest_2() {
      Thermoregulator_tryouts.ThermalNode var1;
      int var2;
      float var3;
      float var4;
      float var5;
      float var6;
      for(var2 = 0; var2 < this.nodes.length; ++var2) {
         var1 = this.nodes[var2];
         var1.calculateInsulation();
         var3 = this.airTemperature;
         if (this.airAndWindTemp < this.airTemperature) {
            var3 = (this.airTemperature - this.airAndWindTemp) / (1.0F + var1.windresist);
         }

         var4 = var3 - 27.0F;
         var1.heatDelta = var4;
         var1.heatDelta /= 1.0F + var1.insulation;
         var5 = 1.0F + (1.0F - var1.getDistToCore() * 0.8F);
         var6 = this.metabolicRateReal * var5;
         var6 *= 1.0F + var1.insulation;
         var1.heatDelta += var6;
         float var7 = var1.heatDelta / 12.0F;
         var7 = PZMath.clamp(var7, -1.0F, 1.0F);
         float var8 = var7 - var1.primaryDelta;
         var1.primaryDelta += var8 * this.getSimulationMultiplier(Thermoregulator_tryouts.Multiplier.PrimaryDelta);
         var1.primaryDelta = PZMath.clamp(var1.primaryDelta, -1.0F, 1.0F);
      }

      for(var2 = 0; var2 < this.nodes.length; ++var2) {
         var1 = this.nodes[var2];
         var3 = this.core.celcius - 20.0F;
         var4 = this.core.celcius;
         if (var3 < this.airTemperature) {
            var3 = this.airTemperature;
         }

         var5 = this.core.celcius - 4.0F;
         if (var1.primaryDelta < 0.0F) {
            var5 = 33.0F - 20.0F * PZMath.abs(var1.primaryDelta);
         } else {
            var5 = 33.0F + 20.0F * var1.primaryDelta;
         }

         var5 = PZMath.clamp(var5, var3, var4);
         var6 = var5 - var1.skinCelcius;
         var1.skinCelcius += var6 * this.getSimulationMultiplier(Thermoregulator_tryouts.Multiplier.SkinCelcius);
         var1.skinCelcius = var5;
      }

   }

   private void updateTest_OLD() {
      Thermoregulator_tryouts.ThermalNode var1;
      int var2;
      float var3;
      float var4;
      float var5;
      float var6;
      for(var2 = 0; var2 < this.nodes.length; ++var2) {
         var1 = this.nodes[var2];
         var1.calculateInsulation();
         var3 = this.airTemperature;
         if (this.airAndWindTemp < this.airTemperature) {
            var3 = (this.airTemperature - this.airAndWindTemp) / (1.0F + var1.windresist);
         }

         var4 = var3 - var1.skinCelcius;
         var1.heatDelta = var4;
         var1.heatDelta /= 1.0F + var1.insulation;
         var5 = 1.0F + (1.0F - var1.getDistToCore() * 0.8F);
         var6 = this.metabolicRateReal * var5;
         var6 *= 1.0F + var1.insulation;
         var1.heatDelta += var6;
         var1.primaryDelta = var1.heatDelta / 12.0F;
         var1.primaryDelta = PZMath.clamp(var1.primaryDelta, -1.0F, 1.0F);
      }

      for(var2 = 0; var2 < this.nodes.length; ++var2) {
         var1 = this.nodes[var2];
         var3 = this.core.celcius - 20.0F;
         var4 = this.core.celcius;
         if (var3 < this.airTemperature) {
            var3 = this.airTemperature;
         }

         var5 = this.core.celcius - 4.0F;
         if (var1.primaryDelta < 0.0F) {
            var5 = 33.0F - 20.0F * PZMath.abs(var1.primaryDelta);
         } else {
            var5 = 33.0F + 20.0F * var1.primaryDelta;
         }

         var5 = PZMath.clamp(var5, var3, var4);
         var6 = var5 - var1.skinCelcius;
         var1.skinCelcius += var6 * this.getSimulationMultiplier(Thermoregulator_tryouts.Multiplier.SkinCelcius);
         var1.skinCelcius = var5;
      }

   }

   private void updateNodesHeatDelta() {
      float var1 = PZMath.clamp_01((this.player.getNutrition().getWeight() / 75.0F - 0.5F) * 0.666F);
      var1 = (var1 - 0.5F) * 2.0F;
      float var2 = this.stats.getFitness();
      float var3 = 1.0F;
      if (this.airAndWindTemp > this.setPoint - 2.0F) {
         if (this.airTemperature < this.setPoint + 2.0F) {
            var3 = (this.airTemperature - (this.setPoint - 2.0F)) / 4.0F;
            var3 = 1.0F - var3;
         } else {
            var3 = 0.0F;
         }
      }

      float var4 = 1.0F;
      float var5;
      if (this.climate.getHumidity() > 0.5F) {
         var5 = (this.climate.getHumidity() - 0.5F) * 2.0F;
         var4 -= var5;
      }

      var5 = 1.0F;
      if (this.core.celcius < 37.0F) {
         var5 = (this.core.celcius - 20.0F) / 17.0F;
         var5 *= var5;
      }

      float var6 = 0.0F;

      for(int var8 = 0; var8 < this.nodes.length; ++var8) {
         Thermoregulator_tryouts.ThermalNode var7 = this.nodes[var8];
         var7.calculateInsulation();
         float var9 = this.airTemperature;
         if (this.airAndWindTemp < this.airTemperature) {
            var9 = (this.airTemperature - this.airAndWindTemp) / (1.0F + var7.windresist);
         }

         float var10 = var9 - var7.skinCelcius;
         if (var10 <= 0.0F) {
            var10 *= 1.0F + 0.75F * var7.bodyWetness;
         } else {
            var10 /= 1.0F + 3.0F * var7.bodyWetness;
         }

         var10 *= 0.3F;
         var10 /= 1.0F + var7.insulation;
         var7.heatDelta = var10 * var7.skinSurface;
         float var11;
         float var12;
         if (var7.primaryDelta > 0.0F) {
            var12 = 0.2F + 0.8F * this.getBodyFluids();
            var11 = Metabolics.Default.getMet() * var7.primaryDelta * var7.skinSurface / var7.insulation;
            var11 *= var12 * (0.1F + 0.9F * var3);
            var11 *= var4;
            var11 *= 1.0F - 0.2F * var1;
            var11 *= 1.0F + 0.2F * var2;
            var7.heatDelta -= var11;
         } else {
            var12 = 0.2F + 0.8F * this.getEnergy();
            var11 = Metabolics.Default.getMet() * PZMath.abs(var7.primaryDelta) * var7.skinSurface;
            var11 *= var12;
            var11 *= 1.0F + 0.2F * var1;
            var11 *= 1.0F + 0.2F * var2;
            var7.heatDelta += var11;
         }

         if (var7.secondaryDelta > 0.0F) {
            var12 = 0.1F + 0.9F * this.getBodyFluids();
            var11 = Metabolics.MAX.getMet() * 0.75F * var7.secondaryDelta * var7.skinSurface / var7.insulation;
            var11 *= var12;
            var11 *= 0.85F + 0.15F * var4;
            var11 *= 1.0F - 0.2F * var1;
            var11 *= 1.0F + 0.2F * var2;
            var7.heatDelta -= var11;
         } else {
            var12 = 0.1F + 0.9F * this.getEnergy();
            var11 = Metabolics.Default.getMet() * PZMath.abs(var7.secondaryDelta) * var7.skinSurface;
            var11 *= var12;
            var11 *= 1.0F + 0.2F * var1;
            var11 *= 1.0F + 0.2F * var2;
            var7.heatDelta += var11;
         }

         var6 += var7.heatDelta;
      }

      this.totalHeatRaw = var6;
      var6 += this.metabolicRateReal;
      this.totalHeat = var6;
   }

   private void updateNodesHeatDelta_OLD() {
      float var1 = this.player.getNutrition().getWeight();
      float var2 = PZMath.clamp_01((var1 / 75.0F - 0.5F) * 0.666F);
      float var3 = (var2 - 0.5F) * 2.0F;
      float var4 = this.stats.getFitness();
      float var5 = 1.0F;
      if (this.airAndWindTemp > this.setPoint - 2.0F) {
         if (this.airTemperature < this.setPoint + 2.0F) {
            var5 = (this.airTemperature - (this.setPoint - 2.0F)) / 4.0F;
            var5 = 1.0F - var5;
         } else {
            var5 = 0.0F;
         }
      }

      float var6 = 1.0F;
      float var7;
      if (this.climate.getHumidity() > 0.5F) {
         var7 = (this.climate.getHumidity() - 0.5F) * 2.0F;
         var6 -= var7;
      }

      var7 = 1.0F;
      if (this.core.celcius < 37.0F) {
         var7 = (this.core.celcius - 20.0F) / 17.0F;
         var7 *= var7;
      }

      float var8 = 0.0F;

      for(int var10 = 0; var10 < this.nodes.length; ++var10) {
         Thermoregulator_tryouts.ThermalNode var9 = this.nodes[var10];
         var9.calculateInsulation();
         float var11 = this.airTemperature;
         float var12;
         if (this.airAndWindTemp < this.airTemperature) {
            var12 = this.airTemperature - this.airAndWindTemp;
            var12 /= 1.0F + var9.windresist;
            var11 -= var12;
         }

         var12 = 1.0F + var9.insulation;
         float var13 = var11 - var9.skinCelcius;
         if (var13 < 0.0F) {
            var13 *= 1.0F + 0.25F * var9.bodyWetness;
         } else if (var13 > 0.0F) {
            var13 /= 1.0F + var9.bodyWetness * 4.0F;
         }

         var13 *= 0.3F;
         var13 /= var12;
         var9.heatDelta = var13 * var9.skinSurface;
         float var14;
         float var15;
         if (var9.primaryDelta > 0.0F) {
            var15 = 0.2F + 0.8F * this.getBodyFluids();
            var14 = Metabolics.Default.getMet() * 1.0F * var9.primaryDelta * var9.skinSurface / var12;
            var14 *= var15 * (0.1F + 0.9F * var5);
            var14 *= var6;
            var14 *= 1.0F - 0.2F * var3;
            var14 *= 1.0F + 0.2F * var4;
            var9.heatDelta -= var14;
         } else {
            var15 = 0.2F + 0.8F * this.getEnergy();
            var14 = Metabolics.Default.getMet() * 1.0F * PZMath.abs(var9.primaryDelta) * var9.skinSurface;
            var14 *= var15;
            var14 *= 1.0F + 0.2F * var3;
            var14 *= 1.0F + 0.2F * var4;
            var14 *= 0.25F + 0.75F * var7;
            var9.heatDelta += var14;
         }

         if (var9.secondaryDelta > 0.0F) {
            var15 = 0.1F + 0.9F * this.getBodyFluids();
            var14 = Metabolics.MAX.getMet() * 0.75F * var9.secondaryDelta * var9.skinSurface / var12;
            var14 *= var15;
            var14 *= 0.75F + 0.25F * var6;
            var14 *= 1.0F - 0.2F * var3;
            var14 *= 1.0F + 0.2F * var4;
            var9.heatDelta -= var14;
         } else {
            var15 = 0.1F + 0.9F * this.getEnergy();
            var14 = Metabolics.Default.getMet() * 1.0F * PZMath.abs(var9.secondaryDelta) * var9.skinSurface;
            var14 *= var15;
            var14 *= 1.0F + 0.2F * var3;
            var14 *= 1.0F + 0.2F * var4;
            var14 *= 0.25F + 0.75F * var7;
            var9.heatDelta += var14;
         }

         var8 += var9.heatDelta;
      }

      this.totalHeatRaw = var8;
      var8 += this.metabolicRateReal;
      this.totalHeat = var8;
   }

   private void updateHeatDeltas() {
      this.coreHeatDelta = this.totalHeat * this.getSimulationMultiplier(Thermoregulator_tryouts.Multiplier.BodyHeat);
      if (this.coreHeatDelta < 0.0F) {
         if (this.core.celcius > this.setPoint) {
            this.coreHeatDelta *= 1.0F + (this.core.celcius - this.setPoint) / 2.0F;
         }
      } else if (this.core.celcius < this.setPoint) {
         this.coreHeatDelta *= 1.0F + (this.setPoint - this.core.celcius) / 4.0F;
      }

      Thermoregulator_tryouts.ThermalNode var10000 = this.core;
      var10000.celcius += this.coreHeatDelta;
      this.core.celcius = PZMath.clamp(this.core.celcius, 20.0F, 42.0F);
      this.bodyDamage.setTemperature(this.core.celcius);
      this.bodyHeatDelta = 0.0F;
      if (this.core.celcius > this.setPoint) {
         this.bodyHeatDelta = this.core.celcius - this.setPoint;
      } else if (this.core.celcius < this.setPoint) {
         this.bodyHeatDelta = this.core.celcius - this.setPoint;
      }

      if (this.bodyHeatDelta < 0.0F) {
         float var1 = PZMath.abs(this.bodyHeatDelta);
         if (var1 <= 1.0F) {
            this.bodyHeatDelta *= 0.8F;
         } else {
            var1 = PZMath.clamp(var1, 1.0F, 11.0F) - 1.0F;
            var1 /= 10.0F;
            this.bodyHeatDelta = -0.8F + -0.2F * var1;
         }
      }

      this.bodyHeatDelta = PZMath.clamp(this.bodyHeatDelta, -1.0F, 1.0F);
   }

   private void updateNodes() {
      float var2 = 0.0F;
      float var3 = 0.0F;

      for(int var4 = 0; var4 < this.nodes.length; ++var4) {
         Thermoregulator_tryouts.ThermalNode var1 = this.nodes[var4];
         float var5 = 1.0F + var1.insulation;
         float var6 = this.metabolicRateReal / Metabolics.MAX.getMet();
         var6 *= var6;
         float var7;
         if (this.bodyHeatDelta < 0.0F) {
            var7 = var1.distToCore;
            var1.primaryDelta = this.bodyHeatDelta * (1.0F + var7);
         } else {
            var1.primaryDelta = this.bodyHeatDelta * (1.0F + (1.0F - var1.distToCore));
         }

         var1.primaryDelta = PZMath.clamp(var1.primaryDelta, -1.0F, 1.0F);
         var1.secondaryDelta = var1.primaryDelta * PZMath.abs(var1.primaryDelta) * PZMath.abs(var1.primaryDelta);
         var2 += var1.primaryDelta * var1.skinSurface;
         var3 += var1.secondaryDelta * var1.skinSurface;
         if (this.stats.getDrunkenness() > 0.0F) {
            var1.primaryDelta += this.stats.getDrunkenness() * 0.02F;
         }

         var1.primaryDelta = PZMath.clamp(var1.primaryDelta, -1.0F, 1.0F);
         var7 = this.core.celcius - 20.0F;
         float var8 = this.core.celcius;
         float var9;
         float var10;
         if (var7 < this.airTemperature) {
            if (this.airTemperature < 33.0F) {
               var7 = this.airTemperature;
            } else {
               var9 = 0.4F + 0.6F * (1.0F - var1.distToCore);
               var10 = (this.airTemperature - 33.0F) / 6.0F;
               var7 = 33.0F;
               var7 += 4.0F * var10 * var9;
               var7 = PZMath.clamp(var7, 33.0F, this.airTemperature);
               if (var7 > var8) {
                  var7 = var8 - 0.25F;
               }
            }
         }

         var9 = this.core.celcius - 4.0F;
         float var11;
         if (var1.primaryDelta < 0.0F) {
            var10 = 0.4F + 0.6F * var1.distToCore;
            var11 = var9 - 12.0F * var10 / var5;
            var9 = PZMath.c_lerp(var9, var11, PZMath.abs(var1.primaryDelta));
         } else {
            var10 = 0.4F + 0.6F * (1.0F - var1.distToCore);
            float var12 = 4.0F * var10;
            var12 *= Math.max(var5 * 0.5F * var10, 1.0F);
            var11 = Math.min(var9 + var12, var8);
            var9 = PZMath.c_lerp(var9, var11, var1.primaryDelta);
         }

         var9 = PZMath.clamp(var9, var7, var8);
         var10 = var9 - var1.skinCelcius;
         var11 = 1.0F;
         if (var10 < 0.0F && var1.skinCelcius > 33.0F) {
            var11 = 3.0F;
         } else if (var10 > 0.0F && var1.skinCelcius < 33.0F) {
            var11 = 3.0F;
         }

         var1.skinCelcius += var10 * var11 * this.getSimulationMultiplier(Thermoregulator_tryouts.Multiplier.SkinCelcius);
         if (var1 != this.core) {
            if (var1.skinCelcius >= this.core.celcius) {
               var1.celcius = this.core.celcius;
            } else {
               var1.celcius = PZMath.lerp(var1.skinCelcius, this.core.celcius, 0.5F);
            }
         }
      }

      this.primTotal = var2;
      this.secTotal = var3;
   }

   private void updateBodyMultipliers() {
      this.energyMultiplier = 1.0D;
      this.fluidsMultiplier = 1.0D;
      this.fatigueMultiplier = 1.0D;
      float var2 = this.metabolicRateReal / Metabolics.Default.getMet();
      this.energyMultiplier = (double)PZMath.clamp(var2 * var2, 1.0F, 5.0F);
      float var1 = PZMath.abs(this.primTotal);
      var1 *= var1;
      if (this.primTotal < 0.0F) {
         this.energyMultiplier += (double)(0.75F * var1);
         this.fatigueMultiplier += (double)(0.5F * var1);
      } else if (this.primTotal > 0.0F) {
         this.fluidsMultiplier += (double)(0.75F * var1);
         this.fatigueMultiplier += (double)(0.5F * var1);
      }

      var1 = PZMath.abs(this.secTotal);
      var1 *= var1;
      if (this.secTotal < 0.0F) {
         this.energyMultiplier += (double)(8.0F * var1);
         this.fatigueMultiplier += (double)(3.5F * var1);
      } else if (this.secTotal > 0.0F) {
         this.fluidsMultiplier += (double)(6.0F * var1);
         this.fatigueMultiplier += (double)(3.5F * var1);
      }

   }

   private void updateClothing() {
      this.character.getItemVisuals(itemVisuals);
      boolean var1 = itemVisuals.size() != itemVisualsCache.size();
      int var2;
      if (!var1) {
         for(var2 = 0; var2 < itemVisuals.size(); ++var2) {
            if (var2 >= itemVisualsCache.size() || itemVisuals.get(var2) != itemVisualsCache.get(var2)) {
               var1 = true;
               break;
            }
         }
      }

      if (var1) {
         for(var2 = 0; var2 < this.nodes.length; ++var2) {
            this.nodes[var2].clothing.clear();
         }

         itemVisualsCache.clear();

         for(var2 = 0; var2 < itemVisuals.size(); ++var2) {
            ItemVisual var3 = (ItemVisual)itemVisuals.get(var2);
            InventoryItem var4 = var3.getInventoryItem();
            itemVisualsCache.add(var3);
            if (var4 instanceof Clothing) {
               Clothing var5 = (Clothing)var4;
               if (var5.getInsulation() > 0.0F || var5.getWindresistance() > 0.0F) {
                  boolean var6 = false;
                  ArrayList var7 = var4.getBloodClothingType();
                  if (var7 != null) {
                     coveredParts.clear();
                     BloodClothingType.getCoveredParts(var7, coveredParts);

                     for(int var8 = 0; var8 < coveredParts.size(); ++var8) {
                        BloodBodyPartType var9 = (BloodBodyPartType)coveredParts.get(var8);
                        if (var9.index() >= 0 && var9.index() < this.nodes.length) {
                           var6 = true;
                           this.nodes[var9.index()].clothing.add(var5);
                        }
                     }
                  }

                  if (!var6 && var5.getBodyLocation() != null) {
                     String var10 = var5.getBodyLocation().toLowerCase();
                     byte var11 = -1;
                     switch(var10.hashCode()) {
                     case 103067:
                        if (var10.equals("hat")) {
                           var11 = 0;
                        }
                        break;
                     case 3344108:
                        if (var10.equals("mask")) {
                           var11 = 1;
                        }
                     }

                     switch(var11) {
                     case 0:
                     case 1:
                        this.nodes[BodyPartType.ToIndex(BodyPartType.Head)].clothing.add(var5);
                     }
                  }
               }
            }
         }
      }

   }

   public float getEnergy() {
      float var1 = 1.0F - this.stats.getHunger() * this.stats.getHunger();
      float var2 = 1.0F - this.stats.getFatigue() * this.stats.getFatigue();
      return 0.6F * var1 + 0.4F * var2;
   }

   public float getBodyFluids() {
      return 1.0F - this.stats.getThirst();
   }

   public class ThermalNode {
      private final float distToCore;
      private final float skinSurface;
      private final BodyPartType bodyPartType;
      private final BloodBodyPartType bloodBPT;
      private final BodyPart bodyPart;
      private final boolean isCore;
      private final float insulationLayerMultiplierUI;
      private Thermoregulator_tryouts.ThermalNode upstream;
      private Thermoregulator_tryouts.ThermalNode[] downstream;
      private float insulation;
      private float windresist;
      private float celcius;
      private float skinCelcius;
      private float heatDelta;
      private float primaryDelta;
      private float secondaryDelta;
      private float clothingWetness;
      private float bodyWetness;
      private ArrayList clothing;

      public ThermalNode(float var2, BodyPart var3, float var4) {
         this(false, var2, var3, var4);
      }

      public ThermalNode(boolean var2, float var3, BodyPart var4, float var5) {
         this.celcius = 37.0F;
         this.skinCelcius = 33.0F;
         this.heatDelta = 0.0F;
         this.primaryDelta = 0.0F;
         this.secondaryDelta = 0.0F;
         this.clothingWetness = 0.0F;
         this.bodyWetness = 0.0F;
         this.clothing = new ArrayList();
         this.isCore = var2;
         this.celcius = var3;
         this.distToCore = BodyPartType.GetDistToCore(var4.Type);
         this.skinSurface = BodyPartType.GetSkinSurface(var4.Type);
         this.bodyPartType = var4.Type;
         this.bloodBPT = BloodBodyPartType.FromIndex(BodyPartType.ToIndex(var4.Type));
         this.bodyPart = var4;
         this.insulationLayerMultiplierUI = var5;
      }

      private void calculateInsulation() {
         int var1 = this.clothing.size();
         this.insulation = 0.0F;
         this.windresist = 0.0F;
         this.clothingWetness = 0.0F;
         this.bodyWetness = this.bodyPart != null ? this.bodyPart.getWetness() * 0.01F : 0.0F;
         this.bodyWetness = PZMath.clamp_01(this.bodyWetness);
         if (var1 > 0) {
            for(int var4 = 0; var4 < var1; ++var4) {
               Clothing var2 = (Clothing)this.clothing.get(var4);
               ItemVisual var3 = var2.getVisual();
               float var5 = PZMath.clamp(var2.getWetness() * 0.01F, 0.0F, 1.0F);
               this.clothingWetness += var5;
               boolean var6 = var3.getHole(this.bloodBPT) > 0.0F;
               if (!var6) {
                  float var7 = Temperature.getTrueInsulationValue(var2.getInsulation());
                  float var8 = Temperature.getTrueWindresistanceValue(var2.getWindresistance());
                  float var9 = PZMath.clamp(var2.getCurrentCondition() * 0.01F, 0.0F, 1.0F);
                  var9 = 0.5F + 0.5F * var9;
                  var7 *= (1.0F - var5 * 0.75F) * var9;
                  var8 *= (1.0F - var5 * 0.45F) * var9;
                  this.insulation += var7;
                  this.windresist += var8;
               }
            }

            this.clothingWetness /= (float)var1;
            this.insulation += (float)var1 * 0.05F;
            this.windresist += (float)var1 * 0.05F;
         }

      }

      public boolean hasUpstream() {
         return this.upstream != null;
      }

      public boolean hasDownstream() {
         return this.downstream != null && this.downstream.length > 0;
      }

      public float getDistToCore() {
         return this.distToCore;
      }

      public float getSkinSurface() {
         return this.skinSurface;
      }

      public boolean isCore() {
         return this.isCore;
      }

      public float getInsulation() {
         return this.insulation;
      }

      public float getWindresist() {
         return this.windresist;
      }

      public float getCelcius() {
         return this.celcius;
      }

      public float getSkinCelcius() {
         return this.skinCelcius;
      }

      public float getHeatDelta() {
         return this.heatDelta;
      }

      public float getPrimaryDelta() {
         return this.primaryDelta;
      }

      public float getSecondaryDelta() {
         return this.secondaryDelta;
      }

      public float getClothingWetness() {
         return this.clothingWetness;
      }

      public float getBodyWetness() {
         return this.bodyWetness;
      }

      public float getBodyResponse() {
         return PZMath.lerp(this.primaryDelta, this.secondaryDelta, 0.5F);
      }

      public float getSkinCelciusUI() {
         float var1 = PZMath.clamp(this.getSkinCelcius(), 20.0F, 42.0F);
         if (var1 < 33.0F) {
            var1 = (var1 - 20.0F) / 13.0F * 0.5F;
         } else {
            var1 = 0.5F + (var1 - 33.0F) / 9.0F;
         }

         return var1;
      }

      public float getHeatDeltaUI() {
         return PZMath.clamp((this.heatDelta * 0.2F + 1.0F) / 2.0F, 0.0F, 1.0F);
      }

      public float getPrimaryDeltaUI() {
         return PZMath.clamp((this.primaryDelta + 1.0F) / 2.0F, 0.0F, 1.0F);
      }

      public float getSecondaryDeltaUI() {
         return PZMath.clamp((this.secondaryDelta + 1.0F) / 2.0F, 0.0F, 1.0F);
      }

      public float getInsulationUI() {
         return PZMath.clamp(this.insulation * this.insulationLayerMultiplierUI, 0.0F, 1.0F);
      }

      public float getWindresistUI() {
         return PZMath.clamp(this.windresist * this.insulationLayerMultiplierUI, 0.0F, 1.0F);
      }

      public float getClothingWetnessUI() {
         return PZMath.clamp(this.clothingWetness, 0.0F, 1.0F);
      }

      public float getBodyWetnessUI() {
         return PZMath.clamp(this.bodyWetness, 0.0F, 1.0F);
      }

      public float getBodyResponseUI() {
         return PZMath.clamp((this.getBodyResponse() + 1.0F) / 2.0F, 0.0F, 1.0F);
      }
   }

   private static enum Multiplier {
      Default,
      MetabolicRateInc,
      MetabolicRateDec,
      BodyHeat,
      CoreHeatExpand,
      CoreHeatContract,
      SkinCelcius,
      SkinCelciusContract,
      SkinCelciusExpand,
      PrimaryDelta,
      SecondaryDelta;

      // $FF: synthetic method
      private static Thermoregulator_tryouts.Multiplier[] $values() {
         return new Thermoregulator_tryouts.Multiplier[]{Default, MetabolicRateInc, MetabolicRateDec, BodyHeat, CoreHeatExpand, CoreHeatContract, SkinCelcius, SkinCelciusContract, SkinCelciusExpand, PrimaryDelta, SecondaryDelta};
      }
   }
}

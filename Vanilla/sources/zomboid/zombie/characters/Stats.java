package zombie.characters;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import zombie.core.math.PZMath;

public final class Stats {
   public float Anger = 0.0F;
   public float boredom = 0.0F;
   public float endurance = 1.0F;
   public boolean enduranceRecharging = false;
   public float endurancelast = 1.0F;
   public float endurancedanger = 0.25F;
   public float endurancewarn = 0.5F;
   public float fatigue = 0.0F;
   public float fitness = 1.0F;
   public float hunger = 0.0F;
   public float idleboredom = 0.0F;
   public float morale = 0.5F;
   public float stress = 0.0F;
   public float Fear = 0.0F;
   public float Panic = 0.0F;
   public float Sanity = 1.0F;
   public float Sickness = 0.0F;
   public float Boredom = 0.0F;
   public float Pain = 0.0F;
   public float Drunkenness = 0.0F;
   public int NumVisibleZombies = 0;
   public int LastNumVisibleZombies = 0;
   public boolean Tripping = false;
   public float TrippingRotAngle = 0.0F;
   public float thirst = 0.0F;
   public int NumChasingZombies = 0;
   public int LastVeryCloseZombies = 0;
   public static int NumCloseZombies = 0;
   public int LastNumChasingZombies = 0;
   public float stressFromCigarettes = 0.0F;
   public float ChasingZombiesDanger;
   public int MusicZombiesVisible = 0;
   public int MusicZombiesTargeting = 0;

   public int getNumVisibleZombies() {
      return this.NumVisibleZombies;
   }

   public int getNumChasingZombies() {
      return this.LastNumChasingZombies;
   }

   public void load(DataInputStream var1) throws IOException {
      this.Anger = var1.readFloat();
      this.boredom = var1.readFloat();
      this.endurance = var1.readFloat();
      this.fatigue = var1.readFloat();
      this.fitness = var1.readFloat();
      this.hunger = var1.readFloat();
      this.morale = var1.readFloat();
      this.stress = var1.readFloat();
      this.Fear = var1.readFloat();
      this.Panic = var1.readFloat();
      this.Sanity = var1.readFloat();
      this.Sickness = var1.readFloat();
      this.Boredom = var1.readFloat();
      this.Pain = var1.readFloat();
      this.Drunkenness = var1.readFloat();
      this.thirst = var1.readFloat();
   }

   public void load(ByteBuffer var1, int var2) throws IOException {
      this.Anger = var1.getFloat();
      this.boredom = var1.getFloat();
      this.endurance = var1.getFloat();
      this.fatigue = var1.getFloat();
      this.fitness = var1.getFloat();
      this.hunger = var1.getFloat();
      this.morale = var1.getFloat();
      this.stress = var1.getFloat();
      this.Fear = var1.getFloat();
      this.Panic = var1.getFloat();
      this.Sanity = var1.getFloat();
      this.Sickness = var1.getFloat();
      this.Boredom = var1.getFloat();
      this.Pain = var1.getFloat();
      this.Drunkenness = var1.getFloat();
      this.thirst = var1.getFloat();
      if (var2 >= 97) {
         this.stressFromCigarettes = var1.getFloat();
      }

   }

   public void save(DataOutputStream var1) throws IOException {
      var1.writeFloat(this.Anger);
      var1.writeFloat(this.boredom);
      var1.writeFloat(this.endurance);
      var1.writeFloat(this.fatigue);
      var1.writeFloat(this.fitness);
      var1.writeFloat(this.hunger);
      var1.writeFloat(this.morale);
      var1.writeFloat(this.stress);
      var1.writeFloat(this.Fear);
      var1.writeFloat(this.Panic);
      var1.writeFloat(this.Sanity);
      var1.writeFloat(this.Sickness);
      var1.writeFloat(this.Boredom);
      var1.writeFloat(this.Pain);
      var1.writeFloat(this.Drunkenness);
      var1.writeFloat(this.thirst);
   }

   public void save(ByteBuffer var1) throws IOException {
      var1.putFloat(this.Anger);
      var1.putFloat(this.boredom);
      var1.putFloat(this.endurance);
      var1.putFloat(this.fatigue);
      var1.putFloat(this.fitness);
      var1.putFloat(this.hunger);
      var1.putFloat(this.morale);
      var1.putFloat(this.stress);
      var1.putFloat(this.Fear);
      var1.putFloat(this.Panic);
      var1.putFloat(this.Sanity);
      var1.putFloat(this.Sickness);
      var1.putFloat(this.Boredom);
      var1.putFloat(this.Pain);
      var1.putFloat(this.Drunkenness);
      var1.putFloat(this.thirst);
      var1.putFloat(this.stressFromCigarettes);
   }

   public float getAnger() {
      return this.Anger;
   }

   public void setAnger(float var1) {
      this.Anger = var1;
   }

   public float getBoredom() {
      return this.boredom;
   }

   public void setBoredom(float var1) {
      this.boredom = var1;
   }

   public float getEndurance() {
      return this.endurance;
   }

   public void setEndurance(float var1) {
      this.endurance = var1;
   }

   public float getEndurancelast() {
      return this.endurancelast;
   }

   public void setEndurancelast(float var1) {
      this.endurancelast = var1;
   }

   public float getEndurancedanger() {
      return this.endurancedanger;
   }

   public void setEndurancedanger(float var1) {
      this.endurancedanger = var1;
   }

   public float getEndurancewarn() {
      return this.endurancewarn;
   }

   public void setEndurancewarn(float var1) {
      this.endurancewarn = var1;
   }

   public boolean getEnduranceRecharging() {
      return this.enduranceRecharging;
   }

   public float getFatigue() {
      return this.fatigue;
   }

   public void setFatigue(float var1) {
      this.fatigue = var1;
   }

   public float getFitness() {
      return this.fitness;
   }

   public void setFitness(float var1) {
      this.fitness = var1;
   }

   public float getHunger() {
      return this.hunger;
   }

   public void setHunger(float var1) {
      this.hunger = var1;
   }

   public float getIdleboredom() {
      return this.idleboredom;
   }

   public void setIdleboredom(float var1) {
      this.idleboredom = var1;
   }

   public float getMorale() {
      return this.morale;
   }

   public void setMorale(float var1) {
      this.morale = var1;
   }

   public float getStress() {
      return this.stress + this.getStressFromCigarettes();
   }

   public void setStress(float var1) {
      this.stress = var1;
   }

   public float getStressFromCigarettes() {
      return this.stressFromCigarettes;
   }

   public void setStressFromCigarettes(float var1) {
      this.stressFromCigarettes = PZMath.clamp(var1, 0.0F, this.getMaxStressFromCigarettes());
   }

   public float getMaxStressFromCigarettes() {
      return 0.51F;
   }

   public float getFear() {
      return this.Fear;
   }

   public void setFear(float var1) {
      this.Fear = var1;
   }

   public float getPanic() {
      return this.Panic;
   }

   public void setPanic(float var1) {
      this.Panic = var1;
   }

   public float getSanity() {
      return this.Sanity;
   }

   public void setSanity(float var1) {
      this.Sanity = var1;
   }

   public float getSickness() {
      return this.Sickness;
   }

   public void setSickness(float var1) {
      this.Sickness = var1;
   }

   public float getPain() {
      return this.Pain;
   }

   public void setPain(float var1) {
      this.Pain = var1;
   }

   public float getDrunkenness() {
      return this.Drunkenness;
   }

   public void setDrunkenness(float var1) {
      this.Drunkenness = var1;
   }

   public int getVisibleZombies() {
      return this.NumVisibleZombies;
   }

   public void setNumVisibleZombies(int var1) {
      this.NumVisibleZombies = var1;
   }

   public boolean isTripping() {
      return this.Tripping;
   }

   public void setTripping(boolean var1) {
      this.Tripping = var1;
   }

   public float getTrippingRotAngle() {
      return this.TrippingRotAngle;
   }

   public void setTrippingRotAngle(float var1) {
      this.TrippingRotAngle = var1;
   }

   public float getThirst() {
      return this.thirst;
   }

   public void setThirst(float var1) {
      this.thirst = var1;
   }

   public void resetStats() {
      this.Anger = 0.0F;
      this.boredom = 0.0F;
      this.fatigue = 0.0F;
      this.hunger = 0.0F;
      this.idleboredom = 0.0F;
      this.morale = 0.5F;
      this.stress = 0.0F;
      this.Fear = 0.0F;
      this.Panic = 0.0F;
      this.Sanity = 1.0F;
      this.Sickness = 0.0F;
      this.Boredom = 0.0F;
      this.Pain = 0.0F;
      this.Drunkenness = 0.0F;
      this.thirst = 0.0F;
   }
}

package zombie.iso.weather;

import java.text.SimpleDateFormat;
import java.time.temporal.ChronoUnit;
import java.util.GregorianCalendar;
import java.util.Random;
import zombie.GameTime;
import zombie.SandboxOptions;
import zombie.debug.DebugLog;

public class ClimateValues {
   private double simplexOffsetA = 0.0D;
   private double simplexOffsetB = 0.0D;
   private double simplexOffsetC = 0.0D;
   private double simplexOffsetD = 0.0D;
   private ClimateManager clim;
   private GameTime gt;
   private float time = 0.0F;
   private float dawn = 0.0F;
   private float dusk = 0.0F;
   private float noon = 0.0F;
   private float dayMeanTemperature = 0.0F;
   private double airMassNoiseFrequencyMod = 0.0D;
   private float noiseAirmass = 0.0F;
   private float airMassTemperature = 0.0F;
   private float baseTemperature = 0.0F;
   private float dayLightLagged = 0.0F;
   private float nightLagged = 0.0F;
   private float temperature = 0.0F;
   private boolean temperatureIsSnow = false;
   private float humidity = 0.0F;
   private float windIntensity = 0.0F;
   private float windAngleIntensity = 0.0F;
   private float windAngleDegrees = 0.0F;
   private float nightStrength = 0.0F;
   private float dayLightStrength = 0.0F;
   private float ambient = 0.0F;
   private float desaturation = 0.0F;
   private float dayLightStrengthBase = 0.0F;
   private float lerpNight = 0.0F;
   private float cloudyT = 0.0F;
   private float cloudIntensity = 0.0F;
   private float airFrontAirmass = 0.0F;
   private boolean dayDoFog = false;
   private float dayFogStrength = 0.0F;
   private float dayFogDuration = 0.0F;
   private ClimateManager.DayInfo testCurrentDay;
   private ClimateManager.DayInfo testNextDay;
   private double cacheWorldAgeHours = 0.0D;
   private int cacheYear;
   private int cacheMonth;
   private int cacheDay;
   private Random seededRandom;

   public ClimateValues(ClimateManager var1) {
      this.simplexOffsetA = var1.getSimplexOffsetA();
      this.simplexOffsetB = var1.getSimplexOffsetB();
      this.simplexOffsetC = var1.getSimplexOffsetC();
      this.simplexOffsetD = var1.getSimplexOffsetD();
      this.clim = var1;
      this.gt = GameTime.getInstance();
      this.seededRandom = new Random(1984L);
   }

   public ClimateValues getCopy() {
      ClimateValues var1 = new ClimateValues(this.clim);
      this.CopyValues(var1);
      return var1;
   }

   public void CopyValues(ClimateValues var1) {
      if (var1 != this) {
         var1.time = this.time;
         var1.dawn = this.dawn;
         var1.dusk = this.dusk;
         var1.noon = this.noon;
         var1.dayMeanTemperature = this.dayMeanTemperature;
         var1.airMassNoiseFrequencyMod = this.airMassNoiseFrequencyMod;
         var1.noiseAirmass = this.noiseAirmass;
         var1.airMassTemperature = this.airMassTemperature;
         var1.baseTemperature = this.baseTemperature;
         var1.dayLightLagged = this.dayLightLagged;
         var1.nightLagged = this.nightLagged;
         var1.temperature = this.temperature;
         var1.temperatureIsSnow = this.temperatureIsSnow;
         var1.humidity = this.humidity;
         var1.windIntensity = this.windIntensity;
         var1.windAngleIntensity = this.windAngleIntensity;
         var1.windAngleDegrees = this.windAngleDegrees;
         var1.nightStrength = this.nightStrength;
         var1.dayLightStrength = this.dayLightStrength;
         var1.ambient = this.ambient;
         var1.desaturation = this.desaturation;
         var1.dayLightStrengthBase = this.dayLightStrengthBase;
         var1.lerpNight = this.lerpNight;
         var1.cloudyT = this.cloudyT;
         var1.cloudIntensity = this.cloudIntensity;
         var1.airFrontAirmass = this.airFrontAirmass;
         var1.dayDoFog = this.dayDoFog;
         var1.dayFogStrength = this.dayFogStrength;
         var1.dayFogDuration = this.dayFogDuration;
         var1.cacheWorldAgeHours = this.cacheWorldAgeHours;
         var1.cacheYear = this.cacheYear;
         var1.cacheMonth = this.cacheMonth;
         var1.cacheDay = this.cacheDay;
      }

   }

   public void print() {
      DebugLog.log("==================================================");
      DebugLog.log("Current time of day = " + this.gt.getTimeOfDay());
      DebugLog.log("Current Worldagehours = " + this.gt.getWorldAgeHours());
      DebugLog.log("--------------------------------------------------");
      if (this.testCurrentDay == null) {
         GregorianCalendar var1 = new GregorianCalendar(this.cacheYear, this.cacheMonth, this.cacheDay);
         DebugLog.log("Printing climate values for: " + (new SimpleDateFormat("yyyy MM dd")).format(var1.getTime()));
      } else {
         DebugLog.log("Printing climate values for: " + (new SimpleDateFormat("yyyy MM dd")).format(this.testCurrentDay.calendar.getTime()));
      }

      DebugLog.log("--------------------------------------------------");
      DebugLog.log("Poll Worldagehours = " + this.cacheWorldAgeHours);
      DebugLog.log("Poll time = " + this.time);
      DebugLog.log("dawn = " + this.dawn);
      DebugLog.log("dusk = " + this.dusk);
      DebugLog.log("noon = " + this.noon);
      DebugLog.log("daymeantemperature = " + this.dayMeanTemperature);
      DebugLog.log("airMassNoiseFrequencyMod = " + this.airMassNoiseFrequencyMod);
      DebugLog.log("noiseAirmass = " + this.noiseAirmass);
      DebugLog.log("airMassTemperature = " + this.airMassTemperature);
      DebugLog.log("baseTemperature = " + this.baseTemperature);
      DebugLog.log("dayLightLagged = " + this.dayLightLagged);
      DebugLog.log("nightLagged = " + this.nightLagged);
      DebugLog.log("temperature = " + this.temperature);
      DebugLog.log("temperatureIsSnow = " + this.temperatureIsSnow);
      DebugLog.log("humidity = " + this.humidity);
      DebugLog.log("windIntensity = " + this.windIntensity);
      DebugLog.log("windAngleIntensity = " + this.windAngleIntensity);
      DebugLog.log("windAngleDegrees = " + this.windAngleDegrees);
      DebugLog.log("nightStrength = " + this.nightStrength);
      DebugLog.log("dayLightStrength = " + this.dayLightStrength);
      DebugLog.log("ambient = " + this.ambient);
      DebugLog.log("desaturation = " + this.desaturation);
      DebugLog.log("dayLightStrengthBase = " + this.dayLightStrengthBase);
      DebugLog.log("lerpNight = " + this.lerpNight);
      DebugLog.log("cloudyT = " + this.cloudyT);
      DebugLog.log("cloudIntensity = " + this.cloudIntensity);
      DebugLog.log("airFrontAirmass = " + this.airFrontAirmass);
   }

   public void pollDate(int var1, int var2, int var3) {
      this.pollDate(var1, var2, var3, 0, 0);
   }

   public void pollDate(int var1, int var2, int var3, int var4) {
      this.pollDate(var1, var2, var3, var4, 0);
   }

   public void pollDate(int var1, int var2, int var3, int var4, int var5) {
      this.pollDate(new GregorianCalendar(var1, var2, var3, var4, var5));
   }

   public void pollDate(GregorianCalendar var1) {
      if (this.testCurrentDay == null) {
         this.testCurrentDay = new ClimateManager.DayInfo();
      }

      if (this.testNextDay == null) {
         this.testNextDay = new ClimateManager.DayInfo();
      }

      double var2 = this.gt.getWorldAgeHours();
      this.clim.setDayInfo(this.testCurrentDay, var1.get(5), var1.get(2), var1.get(1), 0);
      this.clim.setDayInfo(this.testNextDay, var1.get(5), var1.get(2), var1.get(1), 1);
      GregorianCalendar var4 = new GregorianCalendar(this.gt.getYear(), this.gt.getMonth(), this.gt.getDayPlusOne(), this.gt.getHour(), this.gt.getMinutes());
      double var5 = (double)ChronoUnit.MINUTES.between(var4.toInstant(), var1.toInstant());
      var5 /= 60.0D;
      double var7 = var2 + var5;
      float var9 = (float)var1.get(11) + (float)var1.get(12) / 60.0F;
      this.updateValues(var7, var9, this.testCurrentDay, this.testNextDay);
   }

   protected void updateValues(double var1, float var3, ClimateManager.DayInfo var4, ClimateManager.DayInfo var5) {
      float var11;
      float var12;
      if (var4.year != this.cacheYear || var4.month != this.cacheMonth || var4.day != this.cacheDay) {
         int var6 = (int)this.clim.getSimplexOffsetC();
         int var7 = (int)this.clim.getSimplexOffsetD();
         long var8 = (long)((var4.year - 1990) * 100000);
         var8 += (long)(var4.month * var4.day * 1234);
         var8 += (long)((var4.year - 1990) * var4.month * 10000);
         var8 += (long)((var7 - var6) * var4.day);
         this.seededRandom.setSeed(var8);
         this.dayFogStrength = 0.0F;
         this.dayDoFog = false;
         this.dayFogDuration = 0.0F;
         float var10 = (float)this.seededRandom.nextInt(1000);
         this.dayDoFog = var10 < 200.0F;
         if (this.dayDoFog) {
            this.dayFogDuration = 4.0F;
            if (var10 < 25.0F) {
               this.dayFogStrength = 1.0F;
               this.dayFogDuration += 2.0F;
            } else {
               this.dayFogStrength = this.seededRandom.nextFloat();
            }

            var11 = var4.season.getDayMeanTemperature();
            var12 = (float)SimplexNoise.noise(this.simplexOffsetA, (var1 + 12.0D - 48.0D) / this.clim.getAirMassNoiseFrequencyMod(SandboxOptions.instance.getRainModifier()));
            var11 += var12 * 8.0F;
            float var13 = this.seededRandom.nextFloat();
            if (var11 < 0.0F) {
               this.dayFogDuration += 5.0F * this.dayFogStrength;
               this.dayFogDuration += 8.0F * var13;
            } else if (var11 < 10.0F) {
               this.dayFogDuration += 2.5F * this.dayFogStrength;
               this.dayFogDuration += 5.0F * var13;
            } else if (var11 < 20.0F) {
               this.dayFogDuration += 1.5F * this.dayFogStrength;
               this.dayFogDuration += 2.5F * var13;
            } else {
               this.dayFogDuration += 1.0F * this.dayFogStrength;
               this.dayFogDuration += 1.0F * var13;
            }

            if (this.dayFogDuration > 24.0F - var4.season.getDawn()) {
               this.dayFogDuration = 24.0F - var4.season.getDawn() - 1.0F;
            }
         }
      }

      this.cacheWorldAgeHours = var1;
      this.cacheYear = var4.year;
      this.cacheMonth = var4.month;
      this.cacheDay = var4.day;
      this.time = var3;
      this.dawn = var4.season.getDawn();
      this.dusk = var4.season.getDusk();
      this.noon = var4.season.getDayHighNoon();
      this.dayMeanTemperature = var4.season.getDayMeanTemperature();
      float var28 = var3 / 24.0F;
      ClimateManager var10000 = this.clim;
      float var29 = ClimateManager.lerp(var28, var4.season.getCurDayPercent(), var5.season.getCurDayPercent());
      this.airMassNoiseFrequencyMod = this.clim.getAirMassNoiseFrequencyMod(SandboxOptions.instance.getRainModifier());
      this.noiseAirmass = (float)SimplexNoise.noise(this.simplexOffsetA, var1 / this.airMassNoiseFrequencyMod);
      float var30 = (float)SimplexNoise.noise(this.simplexOffsetC, var1 / this.airMassNoiseFrequencyMod);
      this.airMassTemperature = (float)SimplexNoise.noise(this.simplexOffsetA, (var1 - 48.0D) / this.airMassNoiseFrequencyMod);
      double var9 = Math.floor(var1) + 12.0D;
      this.airFrontAirmass = (float)SimplexNoise.noise(this.simplexOffsetA, var9 / this.airMassNoiseFrequencyMod);
      var10000 = this.clim;
      var11 = ClimateManager.clerp(var28, var4.season.getDayTemperature(), var5.season.getDayTemperature());
      var10000 = this.clim;
      var12 = ClimateManager.clerp(var28, var4.season.getDayMeanTemperature(), var5.season.getDayMeanTemperature());
      boolean var31 = var11 < var12;
      this.baseTemperature = var12 + this.airMassTemperature * 8.0F;
      float var14 = 4.0F;
      float var15 = this.dusk + var14;
      if (var15 >= 24.0F) {
         var15 -= 24.0F;
      }

      label49: {
         this.dayLightLagged = this.clim.getTimeLerpHours(var3, this.dawn + var14, var15, true);
         float var16 = 5.0F * (1.0F - this.dayLightLagged);
         this.nightLagged = this.clim.getTimeLerpHours(var3, var15, this.dawn + var14, true);
         var16 += 5.0F * this.nightLagged;
         this.temperature = this.baseTemperature + 1.0F - var16;
         if (!(this.temperature < 0.0F)) {
            var10000 = this.clim;
            if (!ClimateManager.WINTER_IS_COMING) {
               this.temperatureIsSnow = false;
               break label49;
            }
         }

         this.temperatureIsSnow = true;
      }

      float var17 = this.temperature;
      var17 = (45.0F - var17) / 90.0F;
      var10000 = this.clim;
      var17 = ClimateManager.clamp01(1.0F - var17);
      float var18 = (1.0F + var30) * 0.5F;
      this.humidity = var18 * var17;
      float var19 = 1.0F - (this.airMassTemperature + 1.0F) * 0.5F;
      float var20 = 1.0F - var29 * 0.4F;
      float var21 = (float)SimplexNoise.noise(var1 / 40.0D, this.simplexOffsetA);
      float var22 = (var21 + 1.0F) * 0.5F;
      var22 *= var19 * var20;
      var22 *= 0.65F;
      this.windIntensity = var22;
      float var23 = (float)SimplexNoise.noise(var1 / 80.0D, this.simplexOffsetB);
      this.windAngleIntensity = var23;
      float var24 = (float)SimplexNoise.noise(var1 / 40.0D, this.simplexOffsetD);
      var24 = (var24 + 1.0F) * 0.5F;
      this.windAngleDegrees = 360.0F * var24;
      this.lerpNight = this.clim.getTimeLerpHours(var3, this.dusk, this.dawn, true);
      ClimateManager var10001 = this.clim;
      this.lerpNight = ClimateManager.clamp(0.0F, 1.0F, this.lerpNight * 2.0F);
      this.nightStrength = this.lerpNight;
      this.dayLightStrengthBase = 1.0F - this.nightStrength;
      float var25 = 1.0F - 0.15F * var29 - 0.2F * this.windIntensity;
      this.dayLightStrengthBase *= var25;
      this.dayLightStrength = this.dayLightStrengthBase;
      this.ambient = this.dayLightStrength;
      float var26 = (1.0F - var4.season.getCurDayPercent()) * 0.4F;
      float var27 = (1.0F - var5.season.getCurDayPercent()) * 0.4F;
      var10001 = this.clim;
      this.desaturation = ClimateManager.lerp(var28, var26, var27);
      ClimateManager var10002 = this.clim;
      this.cloudyT = 1.0F - ClimateManager.clamp01((this.airMassTemperature + 0.8F) * 0.625F);
      this.cloudyT *= 0.8F;
      var10001 = this.clim;
      this.cloudyT = ClimateManager.clamp01(this.cloudyT + this.windIntensity);
      var10001 = this.clim;
      this.cloudIntensity = ClimateManager.clamp01(this.windIntensity * 2.0F);
      this.cloudIntensity -= this.cloudIntensity * 0.5F * this.nightStrength;
   }

   public float getTime() {
      return this.time;
   }

   public float getDawn() {
      return this.dawn;
   }

   public float getDusk() {
      return this.dusk;
   }

   public float getNoon() {
      return this.noon;
   }

   public double getAirMassNoiseFrequencyMod() {
      return this.airMassNoiseFrequencyMod;
   }

   public float getNoiseAirmass() {
      return this.noiseAirmass;
   }

   public float getAirMassTemperature() {
      return this.airMassTemperature;
   }

   public float getBaseTemperature() {
      return this.baseTemperature;
   }

   public float getDayLightLagged() {
      return this.dayLightLagged;
   }

   public float getNightLagged() {
      return this.nightLagged;
   }

   public float getTemperature() {
      return this.temperature;
   }

   public boolean isTemperatureIsSnow() {
      return this.temperatureIsSnow;
   }

   public float getHumidity() {
      return this.humidity;
   }

   public float getWindIntensity() {
      return this.windIntensity;
   }

   public float getWindAngleIntensity() {
      return this.windAngleIntensity;
   }

   public float getWindAngleDegrees() {
      return this.windAngleDegrees;
   }

   public float getNightStrength() {
      return this.nightStrength;
   }

   public float getDayLightStrength() {
      return this.dayLightStrength;
   }

   public float getAmbient() {
      return this.ambient;
   }

   public float getDesaturation() {
      return this.desaturation;
   }

   public float getDayLightStrengthBase() {
      return this.dayLightStrengthBase;
   }

   public float getLerpNight() {
      return this.lerpNight;
   }

   public float getCloudyT() {
      return this.cloudyT;
   }

   public float getCloudIntensity() {
      return this.cloudIntensity;
   }

   public float getAirFrontAirmass() {
      return this.airFrontAirmass;
   }

   public double getCacheWorldAgeHours() {
      return this.cacheWorldAgeHours;
   }

   public int getCacheYear() {
      return this.cacheYear;
   }

   public int getCacheMonth() {
      return this.cacheMonth;
   }

   public int getCacheDay() {
      return this.cacheDay;
   }

   public float getDayMeanTemperature() {
      return this.dayMeanTemperature;
   }

   public boolean isDayDoFog() {
      return this.dayDoFog;
   }

   public float getDayFogStrength() {
      return this.dayFogStrength;
   }

   public float getDayFogDuration() {
      return this.dayFogDuration;
   }
}

package zombie.iso.weather;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import zombie.GameTime;

public class ClimateForecaster {
   private static final int OffsetToday = 10;
   private ClimateValues climateValues;
   private ClimateForecaster.DayForecast[] forecasts = new ClimateForecaster.DayForecast[40];
   private ArrayList forecastList = new ArrayList(40);

   public ArrayList getForecasts() {
      return this.forecastList;
   }

   public ClimateForecaster.DayForecast getForecast() {
      return this.getForecast(0);
   }

   public ClimateForecaster.DayForecast getForecast(int var1) {
      int var2 = 10 + var1;
      return var2 >= 0 && var2 < this.forecasts.length ? this.forecasts[var2] : null;
   }

   private void populateForecastList() {
      this.forecastList.clear();

      for(int var1 = 0; var1 < this.forecasts.length; ++var1) {
         this.forecastList.add(this.forecasts[var1]);
      }

   }

   protected void init(ClimateManager var1) {
      this.climateValues = var1.getClimateValuesCopy();

      for(int var2 = 0; var2 < this.forecasts.length; ++var2) {
         int var3 = var2 - 10;
         ClimateForecaster.DayForecast var4 = new ClimateForecaster.DayForecast();
         var4.weatherPeriod = new WeatherPeriod(var1, var1.getThunderStorm());
         var4.weatherPeriod.setDummy(true);
         var4.indexOffset = var3;
         var4.airFront = new ClimateManager.AirFront();
         this.sampleDay(var1, var4, var3);
         this.forecasts[var2] = var4;
      }

      this.populateForecastList();
   }

   protected void updateDayChange(ClimateManager var1) {
      ClimateForecaster.DayForecast var2 = this.forecasts[0];

      for(int var3 = 0; var3 < this.forecasts.length; ++var3) {
         if (var3 > 0 && var3 < this.forecasts.length) {
            this.forecasts[var3].indexOffset = var3 - 1 - 10;
            this.forecasts[var3 - 1] = this.forecasts[var3];
         }
      }

      var2.reset();
      this.sampleDay(var1, var2, this.forecasts.length - 1 - 10);
      var2.indexOffset = this.forecasts.length - 1 - 10;
      this.forecasts[this.forecasts.length - 1] = var2;
      this.populateForecastList();
   }

   protected void sampleDay(ClimateManager var1, ClimateForecaster.DayForecast var2, int var3) {
      GameTime var4 = GameTime.getInstance();
      int var5 = var4.getYear();
      int var6 = var4.getMonth();
      int var7 = var4.getDayPlusOne();
      GregorianCalendar var8 = new GregorianCalendar(var5, var6, var7, 0, 0);
      var8.add(5, var3);
      boolean var9 = true;
      ClimateForecaster.DayForecast var10 = this.getWeatherOverlap(var3 + 10, 0.0F);
      var2.weatherOverlap = var10;
      var2.weatherPeriod.stopWeatherPeriod();
      int var10001 = var8.get(1);
      var2.name = "day: " + var10001 + " - " + (var8.get(2) + 1) + " - " + var8.get(5);

      for(int var11 = 0; var11 < 24; ++var11) {
         if (var11 != 0) {
            var8.add(11, 1);
         }

         this.climateValues.pollDate(var8);
         if (var11 == 0) {
            var9 = this.climateValues.getNoiseAirmass() >= 0.0F;
            var2.airFrontString = var9 ? "WARM" : "COLD";
            var2.dawn = this.climateValues.getDawn();
            var2.dusk = this.climateValues.getDusk();
            var2.dayLightHours = var2.dusk - var2.dawn;
         }

         float var14;
         if (!var2.weatherStarts && (var9 && this.climateValues.getNoiseAirmass() < 0.0F || !var9 && this.climateValues.getNoiseAirmass() >= 0.0F)) {
            int var12 = this.climateValues.getNoiseAirmass() >= 0.0F ? -1 : 1;
            var2.airFront.setFrontType(var12);
            var1.CalculateWeatherFrontStrength(var8.get(1), var8.get(2), var8.get(5), var2.airFront);
            var2.airFront.setFrontWind(this.climateValues.getWindAngleDegrees());
            if (var2.airFront.getStrength() >= 0.1F) {
               ClimateForecaster.DayForecast var13 = this.getWeatherOverlap(var3 + 10, (float)var11);
               var14 = var13 != null ? var13.weatherPeriod.getTotalStrength() : -1.0F;
               if (var14 < 0.1F) {
                  var2.weatherStarts = true;
                  var2.weatherStartTime = (float)var11;
                  var2.weatherPeriod.init(var2.airFront, this.climateValues.getCacheWorldAgeHours(), var8.get(1), var8.get(2), var8.get(5));
               }
            }

            if (!var2.weatherStarts) {
               var9 = !var9;
            }
         }

         boolean var20 = (float)var11 > this.climateValues.getDawn() && (float)var11 <= this.climateValues.getDusk();
         float var21 = this.climateValues.getTemperature();
         var14 = this.climateValues.getHumidity();
         float var15 = this.climateValues.getWindAngleDegrees();
         float var16 = this.climateValues.getWindIntensity();
         float var17 = this.climateValues.getCloudIntensity();
         if (var2.weatherStarts || var2.weatherOverlap != null) {
            WeatherPeriod var18 = var2.weatherStarts ? var2.weatherPeriod : var2.weatherOverlap.weatherPeriod;
            if (var18 != null) {
               var15 = var18.getWindAngleDegrees();
               WeatherPeriod.WeatherStage var19 = var18.getStageForWorldAge(this.climateValues.getCacheWorldAgeHours());
               if (var19 != null) {
                  if (!var2.weatherStages.contains(var19.getStageID())) {
                     var2.weatherStages.add(var19.getStageID());
                  }

                  switch(var19.getStageID()) {
                  case 1:
                     var2.hasHeavyRain = true;
                  case 4:
                  case 5:
                  case 6:
                  default:
                     var21 -= WeatherPeriod.getMaxTemperatureInfluence() * 0.25F;
                     var17 = 0.35F + 0.5F * var18.getTotalStrength();
                     break;
                  case 2:
                     var16 = 0.5F * var18.getTotalStrength();
                     var21 -= WeatherPeriod.getMaxTemperatureInfluence() * var16;
                     var17 = 0.5F + 0.5F * var16;
                     var2.hasHeavyRain = true;
                     break;
                  case 3:
                     var16 = 0.2F + 0.5F * var18.getTotalStrength();
                     var21 -= WeatherPeriod.getMaxTemperatureInfluence() * var16;
                     var17 = 0.5F + 0.5F * var16;
                     var2.hasStorm = true;
                     break;
                  case 7:
                     var2.chanceOnSnow = true;
                     var16 = 0.75F + 0.25F * var18.getTotalStrength();
                     var21 -= WeatherPeriod.getMaxTemperatureInfluence() * var16;
                     var17 = 0.5F + 0.5F * var16;
                     var2.hasBlizzard = true;
                     break;
                  case 8:
                     var16 = 0.4F + 0.6F * var18.getTotalStrength();
                     var21 -= WeatherPeriod.getMaxTemperatureInfluence() * var16;
                     var17 = 0.5F + 0.5F * var16;
                     var2.hasTropicalStorm = true;
                  }
               } else if (var2.weatherOverlap != null && (float)var11 < var2.weatherEndTime) {
                  var2.weatherEndTime = (float)var11;
               }
            }

            if (var21 < 0.0F) {
               var2.chanceOnSnow = true;
            }
         }

         var2.temperature.add(var21, var20);
         var2.humidity.add(var14, var20);
         var2.windDirection.add(var15, var20);
         var2.windPower.add(var16, var20);
         var2.cloudiness.add(var17, var20);
      }

      var2.temperature.calculate();
      var2.humidity.calculate();
      var2.windDirection.calculate();
      var2.windPower.calculate();
      var2.cloudiness.calculate();
      var2.hasFog = this.climateValues.isDayDoFog();
      var2.fogStrength = this.climateValues.getDayFogStrength();
      var2.fogDuration = this.climateValues.getDayFogDuration();
   }

   private ClimateForecaster.DayForecast getWeatherOverlap(int var1, float var2) {
      int var3 = Math.max(0, var1 - 10);
      if (var3 == var1) {
         return null;
      } else {
         for(int var4 = var3; var4 < var1; ++var4) {
            if (this.forecasts[var4].weatherStarts) {
               float var5 = (float)this.forecasts[var4].weatherPeriod.getDuration() / 24.0F;
               float var6 = (float)var4 + this.forecasts[var4].weatherStartTime / 24.0F;
               var6 += var5;
               float var7 = (float)var1 + var2 / 24.0F;
               if (var6 > var7) {
                  return this.forecasts[var4];
               }
            }
         }

         return null;
      }
   }

   public int getDaysTillFirstWeather() {
      int var1 = -1;

      for(int var2 = 10; var2 < this.forecasts.length - 1; ++var2) {
         if (this.forecasts[var2].weatherStarts && var1 < 0) {
            var1 = var2;
         }
      }

      return var1;
   }

   public static class DayForecast {
      private int indexOffset = 0;
      private String name = "Day x";
      private WeatherPeriod weatherPeriod;
      private ClimateForecaster.ForecastValue temperature = new ClimateForecaster.ForecastValue();
      private ClimateForecaster.ForecastValue humidity = new ClimateForecaster.ForecastValue();
      private ClimateForecaster.ForecastValue windDirection = new ClimateForecaster.ForecastValue();
      private ClimateForecaster.ForecastValue windPower = new ClimateForecaster.ForecastValue();
      private ClimateForecaster.ForecastValue cloudiness = new ClimateForecaster.ForecastValue();
      private boolean weatherStarts = false;
      private float weatherStartTime = 0.0F;
      private float weatherEndTime = 24.0F;
      private boolean chanceOnSnow = false;
      private String airFrontString = "";
      private boolean hasFog = false;
      private float fogStrength = 0.0F;
      private float fogDuration = 0.0F;
      private ClimateManager.AirFront airFront;
      private ClimateForecaster.DayForecast weatherOverlap;
      private boolean hasHeavyRain = false;
      private boolean hasStorm = false;
      private boolean hasTropicalStorm = false;
      private boolean hasBlizzard = false;
      private float dawn = 0.0F;
      private float dusk = 0.0F;
      private float dayLightHours = 0.0F;
      private ArrayList weatherStages = new ArrayList();

      public int getIndexOffset() {
         return this.indexOffset;
      }

      public String getName() {
         return this.name;
      }

      public ClimateForecaster.ForecastValue getTemperature() {
         return this.temperature;
      }

      public ClimateForecaster.ForecastValue getHumidity() {
         return this.humidity;
      }

      public ClimateForecaster.ForecastValue getWindDirection() {
         return this.windDirection;
      }

      public ClimateForecaster.ForecastValue getWindPower() {
         return this.windPower;
      }

      public ClimateForecaster.ForecastValue getCloudiness() {
         return this.cloudiness;
      }

      public WeatherPeriod getWeatherPeriod() {
         return this.weatherPeriod;
      }

      public boolean isWeatherStarts() {
         return this.weatherStarts;
      }

      public float getWeatherStartTime() {
         return this.weatherStartTime;
      }

      public float getWeatherEndTime() {
         return this.weatherEndTime;
      }

      public boolean isChanceOnSnow() {
         return this.chanceOnSnow;
      }

      public String getAirFrontString() {
         return this.airFrontString;
      }

      public boolean isHasFog() {
         return this.hasFog;
      }

      public ClimateManager.AirFront getAirFront() {
         return this.airFront;
      }

      public ClimateForecaster.DayForecast getWeatherOverlap() {
         return this.weatherOverlap;
      }

      public String getMeanWindAngleString() {
         return ClimateManager.getWindAngleString(this.windDirection.getTotalMean());
      }

      public float getFogStrength() {
         return this.fogStrength;
      }

      public float getFogDuration() {
         return this.fogDuration;
      }

      public boolean isHasHeavyRain() {
         return this.hasHeavyRain;
      }

      public boolean isHasStorm() {
         return this.hasStorm;
      }

      public boolean isHasTropicalStorm() {
         return this.hasTropicalStorm;
      }

      public boolean isHasBlizzard() {
         return this.hasBlizzard;
      }

      public ArrayList getWeatherStages() {
         return this.weatherStages;
      }

      public float getDawn() {
         return this.dawn;
      }

      public float getDusk() {
         return this.dusk;
      }

      public float getDayLightHours() {
         return this.dayLightHours;
      }

      private void reset() {
         this.weatherPeriod.stopWeatherPeriod();
         this.temperature.reset();
         this.humidity.reset();
         this.windDirection.reset();
         this.windPower.reset();
         this.cloudiness.reset();
         this.weatherStarts = false;
         this.weatherStartTime = 0.0F;
         this.weatherEndTime = 24.0F;
         this.chanceOnSnow = false;
         this.hasFog = false;
         this.fogStrength = 0.0F;
         this.fogDuration = 0.0F;
         this.weatherOverlap = null;
         this.hasHeavyRain = false;
         this.hasStorm = false;
         this.hasTropicalStorm = false;
         this.hasBlizzard = false;
         this.weatherStages.clear();
      }
   }

   public static class ForecastValue {
      private float dayMin;
      private float dayMax;
      private float dayMean;
      private int dayMeanTicks;
      private float nightMin;
      private float nightMax;
      private float nightMean;
      private int nightMeanTicks;
      private float totalMin;
      private float totalMax;
      private float totalMean;
      private int totalMeanTicks;

      public ForecastValue() {
         this.reset();
      }

      public float getDayMin() {
         return this.dayMin;
      }

      public float getDayMax() {
         return this.dayMax;
      }

      public float getDayMean() {
         return this.dayMean;
      }

      public float getNightMin() {
         return this.nightMin;
      }

      public float getNightMax() {
         return this.nightMax;
      }

      public float getNightMean() {
         return this.nightMean;
      }

      public float getTotalMin() {
         return this.totalMin;
      }

      public float getTotalMax() {
         return this.totalMax;
      }

      public float getTotalMean() {
         return this.totalMean;
      }

      protected void add(float var1, boolean var2) {
         if (var2) {
            if (var1 < this.dayMin) {
               this.dayMin = var1;
            }

            if (var1 > this.dayMax) {
               this.dayMax = var1;
            }

            this.dayMean += var1;
            ++this.dayMeanTicks;
         } else {
            if (var1 < this.nightMin) {
               this.nightMin = var1;
            }

            if (var1 > this.nightMax) {
               this.nightMax = var1;
            }

            this.nightMean += var1;
            ++this.nightMeanTicks;
         }

         if (var1 < this.totalMin) {
            this.totalMin = var1;
         }

         if (var1 > this.totalMax) {
            this.totalMax = var1;
         }

         this.totalMean += var1;
         ++this.totalMeanTicks;
      }

      protected void calculate() {
         if (this.totalMeanTicks <= 0) {
            this.totalMean = 0.0F;
         } else {
            this.totalMean /= (float)this.totalMeanTicks;
         }

         if (this.dayMeanTicks <= 0) {
            this.dayMin = this.totalMin;
            this.dayMax = this.totalMax;
            this.dayMean = this.totalMean;
         } else {
            this.dayMean /= (float)this.dayMeanTicks;
         }

         if (this.nightMeanTicks <= 0) {
            this.nightMin = this.totalMin;
            this.nightMax = this.totalMax;
            this.nightMean = this.totalMean;
         } else {
            this.nightMean /= (float)this.nightMeanTicks;
         }

      }

      protected void reset() {
         this.dayMin = 10000.0F;
         this.dayMax = -10000.0F;
         this.dayMean = 0.0F;
         this.dayMeanTicks = 0;
         this.nightMin = 10000.0F;
         this.nightMax = -10000.0F;
         this.nightMean = 0.0F;
         this.nightMeanTicks = 0;
         this.totalMin = 10000.0F;
         this.totalMax = -10000.0F;
         this.totalMean = 0.0F;
         this.totalMeanTicks = 0;
      }
   }
}

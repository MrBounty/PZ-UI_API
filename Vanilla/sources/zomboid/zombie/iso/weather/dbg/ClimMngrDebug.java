package zombie.iso.weather.dbg;

import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import zombie.GameTime;
import zombie.SandboxOptions;
import zombie.ZomboidFileSystem;
import zombie.core.Rand;
import zombie.debug.DebugLog;
import zombie.erosion.season.ErosionSeason;
import zombie.iso.weather.ClimateManager;
import zombie.iso.weather.SimplexNoise;
import zombie.iso.weather.ThunderStorm;
import zombie.iso.weather.WeatherPeriod;
import zombie.network.GameClient;

public class ClimMngrDebug extends ClimateManager {
   private GregorianCalendar calendar;
   private double worldAgeHours = 0.0D;
   private double worldAgeHoursStart = 0.0D;
   private double weatherPeriodTime = 0.0D;
   private double simplexOffsetA;
   private ClimateManager.AirFront currentFront = new ClimateManager.AirFront();
   private WeatherPeriod weatherPeriod = new WeatherPeriod(this, (ThunderStorm)null);
   private boolean tickIsDayChange = false;
   public ArrayList runs = new ArrayList();
   private ClimMngrDebug.RunInfo currentRun;
   private ErosionSeason season;
   private int TotalDaysPeriodIndexMod = 5;
   private boolean DoOverrideSandboxRainMod = false;
   private int SandboxRainModOverride = 3;
   private int durDays = 0;
   private static final int WEATHER_NORMAL = 0;
   private static final int WEATHER_STORM = 1;
   private static final int WEATHER_TROPICAL = 2;
   private static final int WEATHER_BLIZZARD = 3;
   private FileWriter writer;

   public ClimMngrDebug() {
      this.weatherPeriod.setPrintStuff(false);
   }

   public void setRainModOverride(int var1) {
      this.DoOverrideSandboxRainMod = true;
      this.SandboxRainModOverride = var1;
   }

   public void unsetRainModOverride() {
      this.DoOverrideSandboxRainMod = false;
      this.SandboxRainModOverride = 3;
   }

   public void SimulateDays(int var1, int var2) {
      this.durDays = var1;
      DebugLog.log("Starting " + var2 + " simulations of " + var1 + " days per run...");
      byte var3 = 0;
      byte var4 = 0;
      DebugLog.log("Year: " + GameTime.instance.getYear() + ", Month: " + var3 + ", Day: " + var4);

      for(int var5 = 0; var5 < var2; ++var5) {
         this.calendar = new GregorianCalendar(GameTime.instance.getYear(), var3, var4, 0, 0);
         this.season = ClimateManager.getInstance().getSeason().clone();
         this.season.init(this.season.getLat(), this.season.getTempMax(), this.season.getTempMin(), this.season.getTempDiff(), this.season.getSeasonLag(), this.season.getHighNoon(), Rand.Next(0, 255), Rand.Next(0, 255), Rand.Next(0, 255));
         this.simplexOffsetA = (double)Rand.Next(0, 8000);
         this.worldAgeHours = 250.0D;
         this.weatherPeriodTime = this.worldAgeHours;
         this.worldAgeHoursStart = this.worldAgeHours;
         double var6 = this.getAirMassNoiseFrequencyMod(SandboxOptions.instance.getRainModifier());
         float var8 = (float)SimplexNoise.noise(this.simplexOffsetA, this.worldAgeHours / var6);
         int var9 = var8 < 0.0F ? -1 : 1;
         this.currentFront.setFrontType(var9);
         this.weatherPeriod.stopWeatherPeriod();
         double var10 = this.worldAgeHours + 24.0D;
         int var12 = var1 * 24;
         this.currentRun = new ClimMngrDebug.RunInfo();
         this.currentRun.durationDays = var1;
         this.currentRun.durationHours = (double)var12;
         this.currentRun.seedA = this.simplexOffsetA;
         this.runs.add(this.currentRun);

         for(int var13 = 0; var13 < var12; ++var13) {
            this.tickIsDayChange = false;
            ++this.worldAgeHours;
            if (this.worldAgeHours >= var10) {
               this.tickIsDayChange = true;
               var10 += 24.0D;
               this.calendar.add(5, 1);
               int var14 = this.calendar.get(5);
               int var15 = this.calendar.get(2);
               int var16 = this.calendar.get(1);
               this.season.setDay(var14, var15, var16);
            }

            this.update_sim();
         }
      }

      this.saveData();
   }

   private void update_sim() {
      double var1 = this.getAirMassNoiseFrequencyMod(SandboxOptions.instance.getRainModifier());
      float var3 = (float)SimplexNoise.noise(this.simplexOffsetA, this.worldAgeHours / var1);
      int var4 = var3 < 0.0F ? -1 : 1;
      if (this.currentFront.getType() != var4) {
         if (this.worldAgeHours > this.weatherPeriodTime) {
            this.weatherPeriod.initSimulationDebug(this.currentFront, this.worldAgeHours);
            this.recordAndCloseWeatherPeriod();
         }

         this.currentFront.setFrontType(var4);
      }

      if (!WINTER_IS_COMING && !THE_DESCENDING_FOG && this.worldAgeHours >= this.worldAgeHoursStart + 72.0D && this.worldAgeHours <= this.worldAgeHoursStart + 96.0D && !this.weatherPeriod.isRunning() && this.worldAgeHours > this.weatherPeriodTime && Rand.Next(0, 1000) < 50) {
         this.triggerCustomWeatherStage(3, 10.0F);
      }

      if (this.tickIsDayChange) {
         double var5 = Math.floor(this.worldAgeHours) + 12.0D;
         float var7 = (float)SimplexNoise.noise(this.simplexOffsetA, var5 / var1);
         var4 = var7 < 0.0F ? -1 : 1;
         if (var4 == this.currentFront.getType()) {
            this.currentFront.addDaySample(var7);
         }
      }

   }

   private void recordAndCloseWeatherPeriod() {
      if (this.weatherPeriod.isRunning()) {
         if (this.worldAgeHours - this.weatherPeriodTime > 0.0D) {
            this.currentRun.addRecord(this.worldAgeHours - this.weatherPeriodTime);
         }

         this.weatherPeriodTime = this.worldAgeHours + Math.ceil(this.weatherPeriod.getDuration());
         boolean var1 = false;
         boolean var2 = false;
         boolean var3 = false;
         Iterator var4 = this.weatherPeriod.getWeatherStages().iterator();

         while(var4.hasNext()) {
            WeatherPeriod.WeatherStage var5 = (WeatherPeriod.WeatherStage)var4.next();
            if (var5.getStageID() == 3) {
               var1 = true;
            }

            if (var5.getStageID() == 8) {
               var2 = true;
            }

            if (var5.getStageID() == 7) {
               var3 = true;
            }
         }

         this.currentRun.addRecord(this.currentFront.getType(), this.weatherPeriod.getDuration(), this.weatherPeriod.getFrontCache().getStrength(), var1, var2, var3);
      }

      this.weatherPeriod.stopWeatherPeriod();
   }

   public boolean triggerCustomWeatherStage(int var1, float var2) {
      if (!GameClient.bClient && !this.weatherPeriod.isRunning()) {
         ClimateManager.AirFront var3 = new ClimateManager.AirFront();
         var3.setFrontType(1);
         var3.setStrength(0.95F);
         this.weatherPeriod.initSimulationDebug(var3, this.worldAgeHours, var1, var2);
         this.recordAndCloseWeatherPeriod();
         return true;
      } else {
         return false;
      }
   }

   protected double getAirMassNoiseFrequencyMod(int var1) {
      return this.DoOverrideSandboxRainMod ? super.getAirMassNoiseFrequencyMod(this.SandboxRainModOverride) : super.getAirMassNoiseFrequencyMod(var1);
   }

   protected float getRainTimeMultiplierMod(int var1) {
      return this.DoOverrideSandboxRainMod ? super.getRainTimeMultiplierMod(this.SandboxRainModOverride) : super.getRainTimeMultiplierMod(var1);
   }

   public ErosionSeason getSeason() {
      return this.season;
   }

   public float getDayMeanTemperature() {
      return this.season.getDayMeanTemperature();
   }

   public void resetOverrides() {
   }

   private ClimMngrDebug.RunInfo calculateTotal() {
      ClimMngrDebug.RunInfo var1 = new ClimMngrDebug.RunInfo();
      var1.totalDaysPeriod = new int[50];
      double var2 = 0.0D;
      double var4 = 0.0D;
      float var6 = 0.0F;
      float var7 = 0.0F;
      float var8 = 0.0F;
      Iterator var9 = this.runs.iterator();

      while(var9.hasNext()) {
         ClimMngrDebug.RunInfo var10 = (ClimMngrDebug.RunInfo)var9.next();
         if (var10.totalPeriodDuration < var1.mostDryPeriod) {
            var1.mostDryPeriod = var10.totalPeriodDuration;
         }

         if (var10.totalPeriodDuration > var1.mostWetPeriod) {
            var1.mostWetPeriod = var10.totalPeriodDuration;
         }

         var1.totalPeriodDuration += var10.totalPeriodDuration;
         if (var10.longestPeriod > var1.longestPeriod) {
            var1.longestPeriod = var10.longestPeriod;
         }

         if (var10.shortestPeriod < var1.shortestPeriod) {
            var1.shortestPeriod = var10.shortestPeriod;
         }

         var1.totalPeriods += var10.totalPeriods;
         var1.averagePeriod += var10.averagePeriod;
         if (var10.longestEmpty > var1.longestEmpty) {
            var1.longestEmpty = var10.longestEmpty;
         }

         if (var10.shortestEmpty < var1.shortestEmpty) {
            var1.shortestEmpty = var10.shortestEmpty;
         }

         var1.totalEmpty += var10.totalEmpty;
         var1.averageEmpty += var10.averageEmpty;
         if (var10.highestStrength > var1.highestStrength) {
            var1.highestStrength = var10.highestStrength;
         }

         if (var10.lowestStrength < var1.lowestStrength) {
            var1.lowestStrength = var10.lowestStrength;
         }

         var1.averageStrength += var10.averageStrength;
         if (var10.highestWarmStrength > var1.highestWarmStrength) {
            var1.highestWarmStrength = var10.highestWarmStrength;
         }

         if (var10.lowestWarmStrength < var1.lowestWarmStrength) {
            var1.lowestWarmStrength = var10.lowestWarmStrength;
         }

         var1.averageWarmStrength += var10.averageWarmStrength;
         if (var10.highestColdStrength > var1.highestColdStrength) {
            var1.highestColdStrength = var10.highestColdStrength;
         }

         if (var10.lowestColdStrength < var1.lowestColdStrength) {
            var1.lowestColdStrength = var10.lowestColdStrength;
         }

         var1.averageColdStrength += var10.averageColdStrength;
         var1.countNormalWarm += var10.countNormalWarm;
         var1.countNormalCold += var10.countNormalCold;
         var1.countStorm += var10.countStorm;
         var1.countTropical += var10.countTropical;
         var1.countBlizzard += var10.countBlizzard;

         int var11;
         int[] var10000;
         for(var11 = 0; var11 < var10.dayCountPeriod.length; ++var11) {
            var10000 = var1.dayCountPeriod;
            var10000[var11] += var10.dayCountPeriod[var11];
         }

         for(var11 = 0; var11 < var10.dayCountWarmPeriod.length; ++var11) {
            var10000 = var1.dayCountWarmPeriod;
            var10000[var11] += var10.dayCountWarmPeriod[var11];
         }

         for(var11 = 0; var11 < var10.dayCountColdPeriod.length; ++var11) {
            var10000 = var1.dayCountColdPeriod;
            var10000[var11] += var10.dayCountColdPeriod[var11];
         }

         for(var11 = 0; var11 < var10.dayCountEmpty.length; ++var11) {
            var10000 = var1.dayCountEmpty;
            var10000[var11] += var10.dayCountEmpty[var11];
         }

         for(var11 = 0; var11 < var10.exceedingPeriods.size(); ++var11) {
            var1.exceedingPeriods.add((Integer)var10.exceedingPeriods.get(var11));
         }

         for(var11 = 0; var11 < var10.exceedingEmpties.size(); ++var11) {
            var1.exceedingEmpties.add((Integer)var10.exceedingEmpties.get(var11));
         }

         var11 = (int)(var10.totalPeriodDuration / (double)(this.TotalDaysPeriodIndexMod * 24));
         if (var11 < var1.totalDaysPeriod.length) {
            int var10002 = var1.totalDaysPeriod[var11]++;
         } else {
            int var13 = var11 * this.TotalDaysPeriodIndexMod;
            DebugLog.log("Total days Period is longer than allowed array, days = " + var13);
         }
      }

      if (this.runs.size() > 0) {
         int var12 = this.runs.size();
         var1.totalPeriodDuration /= (double)var12;
         var1.averagePeriod /= (double)var12;
         var1.averageEmpty /= (double)var12;
         var1.averageStrength /= (float)var12;
         var1.averageWarmStrength /= (float)var12;
         var1.averageColdStrength /= (float)var12;
      }

      return var1;
   }

   private void saveData() {
      if (this.runs.size() > 0) {
         try {
            Iterator var1 = this.runs.iterator();

            while(var1.hasNext()) {
               ClimMngrDebug.RunInfo var2 = (ClimMngrDebug.RunInfo)var1.next();
               var2.calculate();
            }

            ClimMngrDebug.RunInfo var22 = this.calculateTotal();
            String var23 = (new SimpleDateFormat("yyyyMMddHHmmss")).format(new Date());
            ZomboidFileSystem.instance.getFileInCurrentSave("climate").mkdirs();
            File var3 = ZomboidFileSystem.instance.getFileInCurrentSave("climate");
            if (var3.exists() && var3.isDirectory()) {
               String var4 = ZomboidFileSystem.instance.getFileNameInCurrentSave("climate", var23 + ".txt");
               DebugLog.log("Attempting to save test data to: " + var4);
               File var5 = new File(var4);
               DebugLog.log("Saving climate test data: " + var4);

               FileWriter var6;
               try {
                  var6 = new FileWriter(var5, false);

                  try {
                     this.writer = var6;
                     int var7 = this.runs.size();
                     this.write("Simulation results." + System.lineSeparator());
                     int var10001 = this.runs.size();
                     this.write("Runs: " + var10001 + ", days per cycle: " + this.durDays);
                     if (this.DoOverrideSandboxRainMod) {
                        this.write("RainModifier used: " + this.SandboxRainModOverride);
                     } else {
                        this.write("RainModifier used: " + SandboxOptions.instance.getRainModifier());
                     }

                     this.write("");
                     this.write("===================================================================");
                     this.write(" TOTALS OVERVIEW");
                     this.write("===================================================================");
                     this.write("");
                     this.write("Total weather periods: " + var22.totalPeriods + ", average per cycle: " + var22.totalPeriods / var7);
                     String var24 = this.formatDuration(var22.longestPeriod);
                     this.write("Longest weather: " + var24);
                     var24 = this.formatDuration(var22.shortestPeriod);
                     this.write("Shortest weather: " + var24);
                     var24 = this.formatDuration(var22.averagePeriod);
                     this.write("Average weather: " + var24);
                     this.write("");
                     var24 = this.formatDuration(var22.totalPeriodDuration);
                     this.write("Average total weather days per cycle: " + var24);
                     this.write("");
                     var24 = this.formatDuration(var22.mostDryPeriod);
                     this.write("Driest cycle total weather days: " + var24);
                     var24 = this.formatDuration(var22.mostWetPeriod);
                     this.write("Wettest cycle total weather days: " + var24);
                     this.write("");
                     this.write("Total clear periods: " + var22.totalEmpty + ", average per cycle: " + var22.totalEmpty / var7);
                     var24 = this.formatDuration(var22.longestEmpty);
                     this.write("Longest clear: " + var24);
                     var24 = this.formatDuration(var22.shortestEmpty);
                     this.write("Shortest clear: " + var24);
                     var24 = this.formatDuration(var22.averageEmpty);
                     this.write("Average clear: " + var24);
                     this.write("");
                     this.write("Highest Front strength: " + var22.highestStrength);
                     this.write("Lowest Front strength: " + var22.lowestStrength);
                     this.write("Average Front strength: " + var22.averageStrength);
                     this.write("");
                     this.write("Highest WarmFront strength: " + var22.highestWarmStrength);
                     this.write("Lowest WarmFront strength: " + var22.lowestWarmStrength);
                     this.write("Average WarmFront strength: " + var22.averageWarmStrength);
                     this.write("");
                     this.write("Highest ColdFront strength: " + var22.highestColdStrength);
                     this.write("Lowest ColdFront strength: " + var22.lowestColdStrength);
                     this.write("Average ColdFront strength: " + var22.averageColdStrength);
                     this.write("");
                     this.write("Weather period types:");
                     double var8 = (double)var7;
                     var10001 = var22.countNormalWarm;
                     this.write("Normal warm: " + var10001 + ", average: " + this.round((double)var22.countNormalWarm / var8));
                     var10001 = var22.countNormalCold;
                     this.write("Normal cold: " + var10001 + ", average: " + this.round((double)var22.countNormalCold / var8));
                     var10001 = var22.countStorm;
                     this.write("Normal storm: " + var10001 + ", average: " + this.round((double)var22.countStorm / (double)var7));
                     var10001 = var22.countTropical;
                     this.write("Normal tropical: " + var10001 + ", average: " + this.round((double)var22.countTropical / var8));
                     var10001 = var22.countBlizzard;
                     this.write("Normal blizzard: " + var10001 + ", average: " + this.round((double)var22.countBlizzard / var8));
                     this.write("");
                     this.write("Distribution duration in days (total periods)");
                     this.printCountTable(var6, var22.dayCountPeriod);
                     this.write("");
                     this.write("Distribution duration in days (WARM periods)");
                     this.printCountTable(var6, var22.dayCountWarmPeriod);
                     this.write("");
                     this.write("Distribution duration in days (COLD periods)");
                     this.printCountTable(var6, var22.dayCountColdPeriod);
                     this.write("");
                     this.write("Distribution duration in days (clear periods)");
                     this.printCountTable(var6, var22.dayCountEmpty);
                     this.write("");
                     this.write("Amount of weather periods exceeding threshold: " + var22.exceedingPeriods.size());
                     Iterator var10;
                     Integer var11;
                     if (var22.exceedingPeriods.size() > 0) {
                        var10 = var22.exceedingPeriods.iterator();

                        while(var10.hasNext()) {
                           var11 = (Integer)var10.next();
                           this.writer.write(var11 + " days, ");
                        }
                     }

                     this.write("");
                     this.write("");
                     this.write("Amount of clear periods exceeding threshold: " + var22.exceedingEmpties.size());
                     if (var22.exceedingEmpties.size() > 0) {
                        var10 = var22.exceedingEmpties.iterator();

                        while(var10.hasNext()) {
                           var11 = (Integer)var10.next();
                           this.writer.write(var11 + " days, ");
                        }
                     }

                     this.write("");
                     this.write("");
                     this.write("Distribution duration total weather days:");
                     this.printCountTable(this.writer, var22.totalDaysPeriod, this.TotalDaysPeriodIndexMod);
                     this.writeDataExtremes();
                     this.writer = null;
                  } catch (Throwable var19) {
                     try {
                        var6.close();
                     } catch (Throwable var18) {
                        var19.addSuppressed(var18);
                     }

                     throw var19;
                  }

                  var6.close();
               } catch (Exception var20) {
                  var20.printStackTrace();
               }

               var5 = ZomboidFileSystem.instance.getFileInCurrentSave("climate", var23 + "_DATA.txt");

               try {
                  var6 = new FileWriter(var5, false);

                  try {
                     this.writer = var6;
                     this.writeData();
                     this.writer = null;
                  } catch (Throwable var16) {
                     try {
                        var6.close();
                     } catch (Throwable var15) {
                        var16.addSuppressed(var15);
                     }

                     throw var16;
                  }

                  var6.close();
               } catch (Exception var17) {
                  var17.printStackTrace();
               }

               var5 = ZomboidFileSystem.instance.getFileInCurrentSave("climate", var23 + "_PATTERNS.txt");

               try {
                  var6 = new FileWriter(var5, false);

                  try {
                     this.writer = var6;
                     this.writePatterns();
                     this.writer = null;
                  } catch (Throwable var13) {
                     try {
                        var6.close();
                     } catch (Throwable var12) {
                        var13.addSuppressed(var12);
                     }

                     throw var13;
                  }

                  var6.close();
               } catch (Exception var14) {
                  var14.printStackTrace();
               }
            }
         } catch (Exception var21) {
            var21.printStackTrace();
         }

      }
   }

   private double round(double var1) {
      return (double)Math.round(var1 * 100.0D) / 100.0D;
   }

   private void writeRunInfo(ClimMngrDebug.RunInfo var1, int var2) throws Exception {
      this.write("===================================================================");
      this.write(" RUN NR: " + var2);
      this.write("===================================================================");
      this.write("");
      this.write("Total weather periods: " + var1.totalPeriods);
      String var10001 = this.formatDuration(var1.longestPeriod);
      this.write("Longest weather: " + var10001);
      var10001 = this.formatDuration(var1.shortestPeriod);
      this.write("Shortest weather: " + var10001);
      var10001 = this.formatDuration(var1.averagePeriod);
      this.write("Average weather: " + var10001);
      this.write("");
      var10001 = this.formatDuration(var1.totalPeriodDuration);
      this.write("Total weather days for cycle: " + var10001);
      this.write("");
      this.write("Total clear periods: " + var1.totalEmpty);
      var10001 = this.formatDuration(var1.longestEmpty);
      this.write("Longest clear: " + var10001);
      var10001 = this.formatDuration(var1.shortestEmpty);
      this.write("Shortest clear: " + var10001);
      var10001 = this.formatDuration(var1.averageEmpty);
      this.write("Average clear: " + var10001);
      this.write("");
      this.write("Highest Front strength: " + var1.highestStrength);
      this.write("Lowest Front strength: " + var1.lowestStrength);
      this.write("Average Front strength: " + var1.averageStrength);
      this.write("");
      this.write("Highest WarmFront strength: " + var1.highestWarmStrength);
      this.write("Lowest WarmFront strength: " + var1.lowestWarmStrength);
      this.write("Average WarmFront strength: " + var1.averageWarmStrength);
      this.write("");
      this.write("Highest ColdFront strength: " + var1.highestColdStrength);
      this.write("Lowest ColdFront strength: " + var1.lowestColdStrength);
      this.write("Average ColdFront strength: " + var1.averageColdStrength);
      this.write("");
      this.write("Weather period types:");
      this.write("Normal warm: " + var1.countNormalWarm);
      this.write("Normal cold: " + var1.countNormalCold);
      this.write("Normal storm: " + var1.countStorm);
      this.write("Normal tropical: " + var1.countTropical);
      this.write("Normal blizzard: " + var1.countBlizzard);
      this.write("");
      this.write("Distribution duration in days (total periods)");
      this.printCountTable(this.writer, var1.dayCountPeriod);
      this.write("");
      this.write("Distribution duration in days (WARM periods)");
      this.printCountTable(this.writer, var1.dayCountWarmPeriod);
      this.write("");
      this.write("Distribution duration in days (COLD periods)");
      this.printCountTable(this.writer, var1.dayCountColdPeriod);
      this.write("");
      this.write("Distribution duration in days (clear periods)");
      this.printCountTable(this.writer, var1.dayCountEmpty);
      this.write("");
      this.write("Amount of weather periods exceeding threshold: " + var1.exceedingPeriods.size());
      Iterator var3;
      Integer var4;
      if (var1.exceedingPeriods.size() > 0) {
         var3 = var1.exceedingPeriods.iterator();

         while(var3.hasNext()) {
            var4 = (Integer)var3.next();
            this.write(var4 + " days.");
         }
      }

      this.write("");
      this.write("Amount of clear periods exceeding threshold: " + var1.exceedingEmpties.size());
      if (var1.exceedingEmpties.size() > 0) {
         var3 = var1.exceedingEmpties.iterator();

         while(var3.hasNext()) {
            var4 = (Integer)var3.next();
            this.write(var4 + " days.");
         }
      }

   }

   private void write(String var1) throws Exception {
      this.writer.write(var1 + System.lineSeparator());
   }

   private void writeDataExtremes() throws Exception {
      int var1 = 0;
      int var2 = -1;
      int var3 = -1;
      ClimMngrDebug.RunInfo var4 = null;
      ClimMngrDebug.RunInfo var5 = null;
      Iterator var6 = this.runs.iterator();

      while(true) {
         ClimMngrDebug.RunInfo var7;
         do {
            if (!var6.hasNext()) {
               this.write("");
               this.write("MOST DRY RUN:");
               if (var4 != null) {
                  this.writeRunInfo(var4, var2);
               }

               this.write("");
               this.write("MOST WET RUN:");
               if (var5 != null) {
                  this.writeRunInfo(var5, var3);
               }

               return;
            }

            var7 = (ClimMngrDebug.RunInfo)var6.next();
            ++var1;
            if (var4 == null || var7.totalPeriodDuration < var4.totalPeriodDuration) {
               var4 = var7;
               var2 = var1;
            }
         } while(var5 != null && !(var7.totalPeriodDuration > var5.totalPeriodDuration));

         var5 = var7;
         var3 = var1;
      }
   }

   private void writeData() throws Exception {
      int var1 = 0;
      Iterator var2 = this.runs.iterator();

      while(var2.hasNext()) {
         ClimMngrDebug.RunInfo var3 = (ClimMngrDebug.RunInfo)var2.next();
         ++var1;
         this.writeRunInfo(var3, var1);
      }

   }

   private void writePatterns() throws Exception {
      String var1 = "-";
      String var2 = "#";
      String var3 = "S";
      String var4 = "T";
      String var5 = "B";
      boolean var7 = false;
      boolean var8 = false;
      Iterator var9 = this.runs.iterator();

      while(var9.hasNext()) {
         ClimMngrDebug.RunInfo var10 = (ClimMngrDebug.RunInfo)var9.next();
         int var14 = 0;

         for(Iterator var11 = var10.records.iterator(); var11.hasNext(); ++var14) {
            ClimMngrDebug.RecordInfo var12 = (ClimMngrDebug.RecordInfo)var11.next();
            int var13 = (int)Math.ceil(var12.durationHours / 24.0D);
            String var6;
            if (var12.isWeather && var12.weatherType == 1) {
               var6 = (new String(new char[var13])).replace("\u0000", var3);
            } else if (var12.isWeather && var12.weatherType == 2) {
               var6 = (new String(new char[var13])).replace("\u0000", var4);
            } else if (var12.isWeather && var12.weatherType == 3) {
               var6 = (new String(new char[var13])).replace("\u0000", var5);
            } else if (var14 == 0 && !var12.isWeather && var13 >= 2) {
               var6 = (new String(new char[var13 - 1])).replace("\u0000", var1);
            } else {
               var6 = (new String(new char[var13])).replace("\u0000", var12.isWeather ? var2 : var1);
            }

            this.writer.write(var6);
         }

         this.writer.write(System.lineSeparator());
      }

   }

   private void printCountTable(FileWriter var1, int[] var2) throws Exception {
      this.printCountTable(var1, var2, 1);
   }

   private void printCountTable(FileWriter var1, int[] var2, int var3) throws Exception {
      if (var2 != null && var2.length > 0) {
         int var4 = 0;

         for(int var5 = 0; var5 < var2.length; ++var5) {
            if (var2[var5] > var4) {
               var4 = var2[var5];
            }
         }

         this.write("    DAYS   COUNT GRAPH");
         float var6 = 50.0F / (float)var4;
         if (var4 > 0) {
            for(int var7 = 0; var7 < var2.length; ++var7) {
               String var10 = "";
               var10 = var10 + String.format("%1$8s", var7 * var3 + "-" + (var7 * var3 + var3));
               int var8 = var2[var7];
               var10 = var10 + String.format("%1$8s", var8);
               var10 = var10 + " ";
               int var9 = (int)((float)var8 * var6);
               if (var9 > 0) {
                  var10 = var10 + (new String(new char[var9])).replace("\u0000", "#");
               } else if (var8 > 0) {
                  var10 = var10 + "*";
               }

               this.write(var10);
            }
         }

      }
   }

   private String formatDuration(double var1) {
      int var3 = (int)(var1 / 24.0D);
      int var4 = (int)(var1 - (double)(var3 * 24));
      return var3 + " days, " + var4 + " hours.";
   }

   private class RunInfo {
      public double seedA;
      public int durationDays;
      public double durationHours;
      public ArrayList records = new ArrayList();
      public double totalPeriodDuration = 0.0D;
      public double longestPeriod = 0.0D;
      public double shortestPeriod = 9.99999999E8D;
      public int totalPeriods = 0;
      public double averagePeriod = 0.0D;
      public double longestEmpty = 0.0D;
      public double shortestEmpty = 9.99999999E8D;
      public int totalEmpty = 0;
      public double averageEmpty = 0.0D;
      public float highestStrength = 0.0F;
      public float lowestStrength = 1.0F;
      public float averageStrength = 0.0F;
      public float highestWarmStrength = 0.0F;
      public float lowestWarmStrength = 1.0F;
      public float averageWarmStrength = 0.0F;
      public float highestColdStrength = 0.0F;
      public float lowestColdStrength = 1.0F;
      public float averageColdStrength = 0.0F;
      public int countNormalWarm = 0;
      public int countNormalCold = 0;
      public int countStorm = 0;
      public int countTropical = 0;
      public int countBlizzard = 0;
      public int[] dayCountPeriod = new int[16];
      public int[] dayCountWarmPeriod = new int[16];
      public int[] dayCountColdPeriod = new int[16];
      public int[] dayCountEmpty = new int[75];
      public ArrayList exceedingPeriods = new ArrayList();
      public ArrayList exceedingEmpties = new ArrayList();
      public double mostWetPeriod = 0.0D;
      public double mostDryPeriod = 9.99999999E8D;
      public int[] totalDaysPeriod;

      public ClimMngrDebug.RecordInfo addRecord(double var1) {
         ClimMngrDebug.RecordInfo var3 = ClimMngrDebug.this.new RecordInfo();
         var3.durationHours = var1;
         var3.isWeather = false;
         this.records.add(var3);
         return var3;
      }

      public ClimMngrDebug.RecordInfo addRecord(int var1, double var2, float var4, boolean var5, boolean var6, boolean var7) {
         ClimMngrDebug.RecordInfo var8 = ClimMngrDebug.this.new RecordInfo();
         var8.durationHours = var2;
         var8.isWeather = true;
         var8.airType = var1;
         var8.strength = var4;
         var8.weatherType = 0;
         if (var5) {
            var8.weatherType = 1;
         } else if (var6) {
            var8.weatherType = 2;
         } else if (var7) {
            var8.weatherType = 3;
         }

         this.records.add(var8);
         return var8;
      }

      public void calculate() {
         double var1 = 0.0D;
         double var3 = 0.0D;
         float var5 = 0.0F;
         float var6 = 0.0F;
         float var7 = 0.0F;
         int var8 = 0;
         int var9 = 0;
         Iterator var10 = this.records.iterator();

         while(var10.hasNext()) {
            ClimMngrDebug.RecordInfo var11 = (ClimMngrDebug.RecordInfo)var10.next();
            int var12 = (int)(var11.durationHours / 24.0D);
            int var10002;
            if (var11.isWeather) {
               this.totalPeriodDuration += var11.durationHours;
               if (var11.durationHours > this.longestPeriod) {
                  this.longestPeriod = var11.durationHours;
               }

               if (var11.durationHours < this.shortestPeriod) {
                  this.shortestPeriod = var11.durationHours;
               }

               ++this.totalPeriods;
               var1 += var11.durationHours;
               if (var11.strength > this.highestStrength) {
                  this.highestStrength = var11.strength;
               }

               if (var11.strength < this.lowestStrength) {
                  this.lowestStrength = var11.strength;
               }

               var5 += var11.strength;
               if (var11.airType == 1) {
                  ++var8;
                  if (var11.strength > this.highestWarmStrength) {
                     this.highestWarmStrength = var11.strength;
                  }

                  if (var11.strength < this.lowestWarmStrength) {
                     this.lowestWarmStrength = var11.strength;
                  }

                  var6 += var11.strength;
                  if (var11.weatherType == 1) {
                     ++this.countStorm;
                  } else if (var11.weatherType == 2) {
                     ++this.countTropical;
                  } else if (var11.weatherType == 3) {
                     ++this.countBlizzard;
                  } else {
                     ++this.countNormalWarm;
                  }

                  if (var12 < this.dayCountWarmPeriod.length) {
                     var10002 = this.dayCountWarmPeriod[var12]++;
                  }
               } else {
                  ++var9;
                  if (var11.strength > this.highestColdStrength) {
                     this.highestColdStrength = var11.strength;
                  }

                  if (var11.strength < this.lowestColdStrength) {
                     this.lowestColdStrength = var11.strength;
                  }

                  var7 += var11.strength;
                  ++this.countNormalCold;
                  if (var12 < this.dayCountColdPeriod.length) {
                     var10002 = this.dayCountColdPeriod[var12]++;
                  }
               }

               if (var12 < this.dayCountPeriod.length) {
                  var10002 = this.dayCountPeriod[var12]++;
               } else {
                  DebugLog.log("Period is longer than allowed array, days = " + var12);
                  this.exceedingPeriods.add(var12);
               }
            } else {
               if (var11.durationHours > this.longestEmpty) {
                  this.longestEmpty = var11.durationHours;
               }

               if (var11.durationHours < this.shortestEmpty) {
                  this.shortestEmpty = var11.durationHours;
               }

               ++this.totalEmpty;
               var3 += var11.durationHours;
               if (var12 < this.dayCountEmpty.length) {
                  var10002 = this.dayCountEmpty[var12]++;
               } else {
                  DebugLog.log("No-Weather period is longer than allowed array, days = " + var12);
                  this.exceedingEmpties.add(var12);
               }
            }
         }

         if (this.totalPeriods > 0) {
            this.averagePeriod = var1 / (double)this.totalPeriods;
            this.averageStrength = var5 / (float)this.totalPeriods;
            if (var8 > 0) {
               this.averageWarmStrength = var6 / (float)var8;
            }

            if (var9 > 0) {
               this.averageColdStrength = var7 / (float)var9;
            }
         }

         if (this.totalEmpty > 0) {
            this.averageEmpty = var3 / (double)this.totalEmpty;
         }

      }
   }

   private class RecordInfo {
      public boolean isWeather;
      public float strength;
      public int airType;
      public double durationHours;
      public int weatherType = 0;
   }
}

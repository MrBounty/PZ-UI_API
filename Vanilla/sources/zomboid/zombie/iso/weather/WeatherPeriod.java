package zombie.iso.weather;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;
import java.util.Stack;
import zombie.GameTime;
import zombie.GameWindow;
import zombie.SandboxOptions;
import zombie.Lua.LuaEventManager;
import zombie.core.PerformanceSettings;
import zombie.core.Rand;
import zombie.core.math.PZMath;
import zombie.core.raknet.UdpConnection;
import zombie.debug.DebugLog;
import zombie.erosion.season.ErosionSeason;
import zombie.iso.IsoWorld;
import zombie.network.GameClient;
import zombie.network.GameServer;

public class WeatherPeriod {
   public static final int STAGE_START = 0;
   public static final int STAGE_SHOWERS = 1;
   public static final int STAGE_HEAVY_PRECIP = 2;
   public static final int STAGE_STORM = 3;
   public static final int STAGE_CLEARING = 4;
   public static final int STAGE_MODERATE = 5;
   public static final int STAGE_DRIZZLE = 6;
   public static final int STAGE_BLIZZARD = 7;
   public static final int STAGE_TROPICAL_STORM = 8;
   public static final int STAGE_INTERMEZZO = 9;
   public static final int STAGE_MODDED = 10;
   public static final int STAGE_KATEBOB_STORM = 11;
   public static final int STAGE_MAX = 12;
   public static final float FRONT_STRENGTH_THRESHOLD = 0.1F;
   private ClimateManager climateManager;
   private ClimateManager.AirFront frontCache = new ClimateManager.AirFront();
   private double startTime;
   private double duration;
   private double currentTime;
   private WeatherPeriod.WeatherStage currentStage;
   private ArrayList weatherStages = new ArrayList(20);
   private int weatherStageIndex = 0;
   private Stack stagesPool = new Stack();
   private boolean isRunning = false;
   private float totalProgress = 0.0F;
   private float stageProgress = 0.0F;
   private float weatherNoise;
   private static float maxTemperatureInfluence = 7.0F;
   private float temperatureInfluence = 0.0F;
   private float currentStrength;
   private float rainThreshold;
   private float windAngleDirMod = 1.0F;
   private boolean isThunderStorm = false;
   private boolean isTropicalStorm = false;
   private boolean isBlizzard = false;
   private float precipitationFinal = 0.0F;
   private ThunderStorm thunderStorm;
   private ClimateColorInfo cloudColor = new ClimateColorInfo(0.4F, 0.2F, 0.2F, 0.4F);
   private ClimateColorInfo cloudColorReddish = new ClimateColorInfo(0.66F, 0.12F, 0.12F, 0.4F);
   private ClimateColorInfo cloudColorGreenish = new ClimateColorInfo(0.32F, 0.48F, 0.12F, 0.4F);
   private ClimateColorInfo cloudColorBlueish = new ClimateColorInfo(0.16F, 0.48F, 0.48F, 0.4F);
   private ClimateColorInfo cloudColorPurplish = new ClimateColorInfo(0.66F, 0.12F, 0.66F, 0.4F);
   private ClimateColorInfo cloudColorTropical = new ClimateColorInfo(0.4F, 0.2F, 0.2F, 0.4F);
   private ClimateColorInfo cloudColorBlizzard = new ClimateColorInfo(0.12F, 0.13F, 0.21F, 0.5F, 0.38F, 0.4F, 0.5F, 0.8F);
   private static boolean PRINT_STUFF = false;
   private static float kateBobStormProgress = 0.45F;
   private int kateBobStormX = 2000;
   private int kateBobStormY = 2000;
   private Random seededRandom;
   private ClimateValues climateValues;
   private boolean isDummy = false;
   private boolean hasStartedInit = false;
   private static final HashMap cache = new HashMap();

   public WeatherPeriod(ClimateManager var1, ThunderStorm var2) {
      this.climateManager = var1;
      this.thunderStorm = var2;

      for(int var3 = 0; var3 < 30; ++var3) {
         this.stagesPool.push(new WeatherPeriod.WeatherStage());
      }

      PRINT_STUFF = true;
      this.seededRandom = new Random(1984L);
      this.climateValues = var1.getClimateValuesCopy();
   }

   public void setDummy(boolean var1) {
      this.isDummy = var1;
   }

   public static float getMaxTemperatureInfluence() {
      return maxTemperatureInfluence;
   }

   public void setKateBobStormProgress(float var1) {
      kateBobStormProgress = PZMath.clamp_01(var1);
   }

   public void setKateBobStormCoords(int var1, int var2) {
      this.kateBobStormX = var1;
      this.kateBobStormY = var2;
   }

   public ClimateColorInfo getCloudColorReddish() {
      return this.cloudColorReddish;
   }

   public ClimateColorInfo getCloudColorGreenish() {
      return this.cloudColorGreenish;
   }

   public ClimateColorInfo getCloudColorBlueish() {
      return this.cloudColorBlueish;
   }

   public ClimateColorInfo getCloudColorPurplish() {
      return this.cloudColorPurplish;
   }

   public ClimateColorInfo getCloudColorTropical() {
      return this.cloudColorTropical;
   }

   public ClimateColorInfo getCloudColorBlizzard() {
      return this.cloudColorBlizzard;
   }

   public boolean isRunning() {
      return this.isRunning;
   }

   public double getDuration() {
      return this.duration;
   }

   public ClimateManager.AirFront getFrontCache() {
      return this.frontCache;
   }

   public int getCurrentStageID() {
      return this.currentStage != null ? this.currentStage.stageID : -1;
   }

   public WeatherPeriod.WeatherStage getCurrentStage() {
      return this.currentStage;
   }

   public double getWeatherNoise() {
      return (double)this.weatherNoise;
   }

   public float getCurrentStrength() {
      return this.currentStrength;
   }

   public float getRainThreshold() {
      return this.rainThreshold;
   }

   public boolean isThunderStorm() {
      return this.isThunderStorm;
   }

   public boolean isTropicalStorm() {
      return this.isTropicalStorm;
   }

   public boolean isBlizzard() {
      return this.isBlizzard;
   }

   public float getPrecipitationFinal() {
      return this.precipitationFinal;
   }

   public ClimateColorInfo getCloudColor() {
      return this.cloudColor;
   }

   public void setCloudColor(ClimateColorInfo var1) {
      this.cloudColor = var1;
   }

   public float getTotalProgress() {
      return this.totalProgress;
   }

   public float getStageProgress() {
      return this.stageProgress;
   }

   public boolean hasTropical() {
      for(int var1 = 0; var1 < this.weatherStages.size(); ++var1) {
         if (((WeatherPeriod.WeatherStage)this.weatherStages.get(var1)).getStageID() == 8) {
            return true;
         }
      }

      return false;
   }

   public boolean hasStorm() {
      for(int var1 = 0; var1 < this.weatherStages.size(); ++var1) {
         if (((WeatherPeriod.WeatherStage)this.weatherStages.get(var1)).getStageID() == 3) {
            return true;
         }
      }

      return false;
   }

   public boolean hasBlizzard() {
      for(int var1 = 0; var1 < this.weatherStages.size(); ++var1) {
         if (((WeatherPeriod.WeatherStage)this.weatherStages.get(var1)).getStageID() == 7) {
            return true;
         }
      }

      return false;
   }

   public boolean hasHeavyRain() {
      for(int var1 = 0; var1 < this.weatherStages.size(); ++var1) {
         if (((WeatherPeriod.WeatherStage)this.weatherStages.get(var1)).getStageID() == 2) {
            return true;
         }
      }

      return false;
   }

   public float getTotalStrength() {
      return this.frontCache.getStrength();
   }

   public WeatherPeriod.WeatherStage getStageForWorldAge(double var1) {
      for(int var3 = 0; var3 < this.weatherStages.size(); ++var3) {
         if (var1 >= ((WeatherPeriod.WeatherStage)this.weatherStages.get(var3)).getStageStart() && var1 < ((WeatherPeriod.WeatherStage)this.weatherStages.get(var3)).getStageEnd()) {
            return (WeatherPeriod.WeatherStage)this.weatherStages.get(var3);
         }
      }

      return null;
   }

   public float getWindAngleDegrees() {
      return this.frontCache.getAngleDegrees();
   }

   public int getFrontType() {
      return this.frontCache.getType();
   }

   private void print(String var1) {
      if (PRINT_STUFF && !this.isDummy) {
         DebugLog.log(var1);
      }

   }

   public void setPrintStuff(boolean var1) {
      PRINT_STUFF = var1;
   }

   public boolean getPrintStuff() {
      return PRINT_STUFF;
   }

   public void initSimulationDebug(ClimateManager.AirFront var1, double var2) {
      GameTime var4 = GameTime.getInstance();
      this.init(var1, var2, var4.getYear(), var4.getMonth(), var4.getDayPlusOne(), -1, -1.0F);
   }

   public void initSimulationDebug(ClimateManager.AirFront var1, double var2, int var4, float var5) {
      GameTime var6 = GameTime.getInstance();
      this.init(var1, var2, var6.getYear(), var6.getMonth(), var6.getDayPlusOne(), var4, var5);
   }

   protected void init(ClimateManager.AirFront var1, double var2, int var4, int var5, int var6) {
      this.init(var1, var2, var4, var5, var6, -1, -1.0F);
   }

   protected void init(ClimateManager.AirFront var1, double var2, int var4, int var5, int var6, int var7, float var8) {
      this.climateValues.pollDate(var4, var5, var6);
      this.reseed(var4, var5, var6);
      this.hasStartedInit = false;
      if (this.startInit(var1, var2)) {
         if (var7 >= 0 && var7 < 12) {
            this.createSingleStage(var7, var8);
         } else {
            this.createWeatherPattern();
         }

         LuaEventManager.triggerEvent("OnWeatherPeriodStart", this);
         this.endInit();
      }
   }

   protected void reseed(int var1, int var2, int var3) {
      int var4 = (int)this.climateManager.getSimplexOffsetA();
      int var5 = (int)this.climateManager.getSimplexOffsetB();
      long var6 = (long)((var1 - 1990) * 100000);
      var6 += (long)(var2 * var3 * 1234);
      var6 += (long)((var1 - 1990) * var2 * 10000);
      var6 += (long)((var5 - var4) * var3);
      this.print("Reseeding weather period, new seed: " + var6);
      this.seededRandom.setSeed(var6);
   }

   private float RandNext(float var1, float var2) {
      if (var1 == var2) {
         return var1;
      } else {
         if (var1 > var2) {
            var1 = var2;
            var2 = var2;
         }

         return var1 + this.seededRandom.nextFloat() * (var2 - var1);
      }
   }

   private float RandNext(float var1) {
      return this.seededRandom.nextFloat() * var1;
   }

   private int RandNext(int var1, int var2) {
      if (var1 == var2) {
         return var1;
      } else {
         if (var1 > var2) {
            var1 = var2;
            var2 = var2;
         }

         return var1 + this.seededRandom.nextInt(var2 - var1);
      }
   }

   private int RandNext(int var1) {
      return this.seededRandom.nextInt(var1);
   }

   public boolean startCreateModdedPeriod(boolean var1, float var2, float var3) {
      double var4 = GameTime.getInstance().getWorldAgeHours();
      ClimateManager.AirFront var6 = new ClimateManager.AirFront();
      float var7 = ClimateManager.clamp(0.0F, 360.0F, var3);
      var6.setFrontType(var1 ? 1 : -1);
      var6.setFrontWind(var7);
      var6.setStrength(ClimateManager.clamp01(var2));
      GameTime var8 = GameTime.getInstance();
      this.reseed(var8.getYear(), var8.getMonth(), var8.getDayPlusOne());
      this.hasStartedInit = false;
      if (!this.startInit(var6, var4)) {
         return false;
      } else {
         this.print("WeatherPeriod: Creating MODDED weather pattern with strength = " + this.frontCache.getStrength());
         this.clearCurrentWeatherStages();
         return true;
      }
   }

   public boolean endCreateModdedPeriod() {
      if (!this.endInit()) {
         return false;
      } else {
         this.linkWeatherStages();
         this.duration = 0.0D;

         for(int var1 = 0; var1 < this.weatherStages.size(); ++var1) {
            this.duration += ((WeatherPeriod.WeatherStage)this.weatherStages.get(var1)).stageDuration;
         }

         this.print("WeatherPeriod: Duration = " + this.duration + ".");
         this.weatherStageIndex = 0;
         this.currentStage = ((WeatherPeriod.WeatherStage)this.weatherStages.get(this.weatherStageIndex)).startStage(this.startTime);
         this.print("WeatherPeriod: PATTERN GENERATION FINISHED.");
         return true;
      }
   }

   private boolean startInit(ClimateManager.AirFront var1, double var2) {
      if (!this.isRunning && !GameClient.bClient && !(var1.getStrength() < 0.1F)) {
         this.startTime = var2;
         this.frontCache.copyFrom(var1);
         if (this.frontCache.getAngleDegrees() >= 90.0F && this.frontCache.getAngleDegrees() < 270.0F) {
            this.windAngleDirMod = 1.0F;
         } else {
            this.windAngleDirMod = -1.0F;
         }

         this.hasStartedInit = true;
         return true;
      } else {
         return false;
      }
   }

   private boolean endInit() {
      if (this.hasStartedInit && !this.isRunning && !GameClient.bClient && this.weatherStages.size() > 0) {
         this.currentStrength = 0.0F;
         this.totalProgress = 0.0F;
         this.stageProgress = 0.0F;
         this.isRunning = true;
         if (GameServer.bServer && !this.isDummy) {
            this.climateManager.transmitClimatePacket(ClimateManager.ClimateNetAuth.ServerOnly, (byte)1, (UdpConnection)null);
         }

         this.hasStartedInit = false;
         return true;
      } else {
         this.hasStartedInit = false;
         return false;
      }
   }

   public void stopWeatherPeriod() {
      this.clearCurrentWeatherStages();
      this.currentStage = null;
      this.resetClimateManagerOverrides();
      this.isRunning = false;
      this.totalProgress = 0.0F;
      this.stageProgress = 0.0F;
      LuaEventManager.triggerEvent("OnWeatherPeriodStop", this);
   }

   public void writeNetWeatherData(ByteBuffer var1) throws IOException {
      var1.put((byte)(this.isRunning ? 1 : 0));
      if (this.isRunning) {
         var1.put((byte)(this.isThunderStorm ? 1 : 0));
         var1.put((byte)(this.isTropicalStorm ? 1 : 0));
         var1.put((byte)(this.isBlizzard ? 1 : 0));
         var1.putFloat(this.currentStrength);
         var1.putDouble(this.duration);
         var1.putFloat(this.totalProgress);
         var1.putFloat(this.stageProgress);
      }

   }

   public void readNetWeatherData(ByteBuffer var1) throws IOException {
      this.isRunning = var1.get() == 1;
      if (this.isRunning) {
         this.isThunderStorm = var1.get() == 1;
         this.isTropicalStorm = var1.get() == 1;
         this.isBlizzard = var1.get() == 1;
         this.currentStrength = var1.getFloat();
         this.duration = var1.getDouble();
         this.totalProgress = var1.getFloat();
         this.stageProgress = var1.getFloat();
      } else {
         this.isThunderStorm = false;
         this.isTropicalStorm = false;
         this.isBlizzard = false;
         this.currentStrength = 0.0F;
         this.duration = 0.0D;
         this.totalProgress = 0.0F;
         this.stageProgress = 0.0F;
      }

   }

   public ArrayList getWeatherStages() {
      return this.weatherStages;
   }

   private void linkWeatherStages() {
      WeatherPeriod.WeatherStage var1 = null;
      WeatherPeriod.WeatherStage var2 = null;
      WeatherPeriod.WeatherStage var3 = null;

      for(int var4 = 0; var4 < this.weatherStages.size(); ++var4) {
         var3 = (WeatherPeriod.WeatherStage)this.weatherStages.get(var4);
         var2 = null;
         if (var4 + 1 < this.weatherStages.size()) {
            var2 = (WeatherPeriod.WeatherStage)this.weatherStages.get(var4 + 1);
         }

         var3.previousStage = var1;
         var3.nextStage = var2;
         var3.creationFinished = true;
         var1 = var3;
      }

   }

   private void clearCurrentWeatherStages() {
      this.print("WeatherPeriod: Clearing existing stages...");
      Iterator var1 = this.weatherStages.iterator();

      while(var1.hasNext()) {
         WeatherPeriod.WeatherStage var2 = (WeatherPeriod.WeatherStage)var1.next();
         var2.reset();
         this.stagesPool.push(var2);
      }

      this.weatherStages.clear();
   }

   private void createSingleStage(int var1, float var2) {
      this.print("WeatherPeriod: Creating single stage weather pattern with strength = " + this.frontCache.getStrength());
      if (var1 == 8) {
         this.cloudColor = this.cloudColorTropical;
      } else if (var1 == 7) {
         this.cloudColor = this.cloudColorBlizzard;
      }

      this.clearCurrentWeatherStages();
      this.createAndAddStage(0, 1.0D);
      this.createAndAddStage(var1, (double)var2);
      this.createAndAddStage(4, 1.0D);
      this.linkWeatherStages();
      this.duration = 0.0D;

      for(int var3 = 0; var3 < this.weatherStages.size(); ++var3) {
         this.duration += ((WeatherPeriod.WeatherStage)this.weatherStages.get(var3)).stageDuration;
      }

      this.print("WeatherPeriod: Duration = " + var2 + ".");
      this.weatherStageIndex = 0;
      this.currentStage = ((WeatherPeriod.WeatherStage)this.weatherStages.get(this.weatherStageIndex)).startStage(this.startTime);
      this.print("WeatherPeriod: PATTERN GENERATION FINISHED.");
   }

   private void createWeatherPattern() {
      this.print("WeatherPeriod: Creating weather pattern with strength = " + this.frontCache.getStrength());
      this.clearCurrentWeatherStages();
      ErosionSeason var1 = this.climateManager.getSeason();
      float var2 = this.climateValues.getDayMeanTemperature();
      this.print("WeatherPeriod: Day mean temperature = " + var2 + " C.");
      this.print("WeatherPeriod: season = " + var1.getSeasonName());
      float var3 = 0.0F;
      float var4 = 0.0F;
      float var5 = 0.0F;
      float var6 = 1.0F;
      float var7 = this.RandNext(0.0F, 100.0F);
      int var8 = var1.getSeason();
      boolean var9 = IsoWorld.instance.getGameMode().equals("Winter is Coming");
      if (var9) {
         var8 = 5;
      }

      switch(var8) {
      case 1:
         if (var7 < 75.0F) {
            this.cloudColor = this.cloudColorGreenish;
         } else {
            this.cloudColor = this.cloudColorBlueish;
         }

         var3 = 75.0F;
         var4 = 10.0F;
         var5 = 0.0F;
         var6 = 1.25F;
         break;
      case 2:
         if (var7 < 25.0F) {
            this.cloudColor = this.cloudColorGreenish;
         } else {
            this.cloudColor = this.cloudColorReddish;
         }

         var3 = 60.0F;
         var4 = 55.0F;
         var5 = 0.0F;
         break;
      case 3:
         this.cloudColor = this.cloudColorReddish;
         var3 = 75.0F;
         var4 = 80.0F;
         var5 = 0.0F;
         var6 = 1.15F;
         break;
      case 4:
         if (var7 < 50.0F) {
            this.cloudColor = this.cloudColorReddish;
         } else if (var7 < 75.0F) {
            this.cloudColor = this.cloudColorPurplish;
         } else {
            this.cloudColor = this.cloudColorBlueish;
         }

         var3 = 100.0F;
         var4 = 25.0F;
         var5 = 0.0F;
         var6 = 1.35F;
         break;
      case 5:
         if (var7 < 45.0F) {
            this.cloudColor = this.cloudColorPurplish;
         } else {
            this.cloudColor = this.cloudColorBlueish;
         }

         var3 = 10.0F;
         var4 = 0.0F;
         if (var2 < 5.5F) {
            var5 = ClimateManager.clamp(0.0F, 85.0F, (5.5F - var2) * 3.0F);
            var5 += 25.0F;
            if (var2 < 2.5F) {
               var5 += 55.0F;
            } else if (var2 < 0.0F) {
               var5 += 75.0F;
            }

            if (var5 > 95.0F) {
               var5 = 95.0F;
            }
         } else {
            var5 = 0.0F;
         }

         if (var9) {
            if (this.frontCache.getStrength() > 0.75F) {
               var5 = 100.0F;
            } else {
               var5 = 75.0F;
            }

            if (this.frontCache.getStrength() > 0.5F) {
               var6 = 1.45F;
            }
         }
      }

      var6 *= this.climateManager.getRainTimeMultiplierMod(SandboxOptions.instance.getRainModifier());
      float var10001 = this.cloudColor.getExterior().r;
      this.print("WeatherPeriod: cloudColor r=" + var10001 + ", g=" + this.cloudColor.getExterior().g + ", b=" + this.cloudColor.getExterior().b);
      this.print("WeatherPeriod: chances, storm=" + var3 + ", tropical=" + var4 + ", blizzard=" + var5 + ". rainTimeMulti=" + var6);
      ArrayList var10 = new ArrayList();
      WeatherPeriod.WeatherStage var11 = null;
      float var18;
      if (this.frontCache.getType() == 1) {
         this.print("WeatherPeriod: Warm to cold front selected.");
         boolean var12 = false;
         boolean var13 = false;
         boolean var14 = false;
         if (this.frontCache.getStrength() > 0.75F) {
            if (var4 > 0.0F && this.RandNext(0.0F, 100.0F) < var4) {
               this.print("WeatherPeriod: tropical storm triggered.");
               var13 = true;
            } else if (var5 > 0.0F && this.RandNext(0.0F, 100.0F) < var5) {
               this.print("WeatherPeriod: blizzard triggered.");
               var12 = true;
            }
         }

         if (!var12 && !var13 && this.frontCache.getStrength() > 0.5F && var3 > 0.0F && this.RandNext(0.0F, 100.0F) < var3) {
            this.print("WeatherPeriod: storm triggered.");
            var14 = true;
         }

         float var15 = this.RandNext(24.0F, 48.0F) * this.frontCache.getStrength();
         float var16 = 0.0F;
         if (var13) {
            var10.add(this.createStage(8, (double)(8.0F + this.RandNext(0.0F, 16.0F * this.frontCache.getStrength()))));
            this.cloudColor = this.cloudColorTropical;
            if (this.RandNext(0.0F, 100.0F) < 60.0F * this.frontCache.getStrength()) {
               var10.add(this.createStage(3, (double)(5.0F + this.RandNext(0.0F, 5.0F * this.frontCache.getStrength()))));
            }

            if (this.RandNext(0.0F, 100.0F) < 30.0F * this.frontCache.getStrength()) {
               var10.add(this.createStage(3, (double)(5.0F + this.RandNext(0.0F, 5.0F * this.frontCache.getStrength()))));
            }
         } else if (var12) {
            var10.add(this.createStage(7, (double)(24.0F + this.RandNext(0.0F, 24.0F * this.frontCache.getStrength()))));
            this.cloudColor = this.cloudColorBlizzard;
         } else if (var14) {
            var10.add(this.createStage(3, (double)(5.0F + this.RandNext(0.0F, 5.0F * this.frontCache.getStrength()))));
            if (this.RandNext(0.0F, 100.0F) < 70.0F * this.frontCache.getStrength()) {
               var10.add(this.createStage(3, (double)(4.0F + this.RandNext(0.0F, 4.0F * this.frontCache.getStrength()))));
            }

            if (this.RandNext(0.0F, 100.0F) < 50.0F * this.frontCache.getStrength()) {
               var10.add(this.createStage(3, (double)(4.0F + this.RandNext(0.0F, 4.0F * this.frontCache.getStrength()))));
            }

            if (this.RandNext(0.0F, 100.0F) < 25.0F * this.frontCache.getStrength()) {
               var10.add(this.createStage(3, (double)(4.0F + this.RandNext(0.0F, 3.0F * this.frontCache.getStrength()))));
            }

            if (this.RandNext(0.0F, 100.0F) < 12.5F * this.frontCache.getStrength()) {
               var10.add(this.createStage(3, (double)(4.0F + this.RandNext(0.0F, 2.0F * this.frontCache.getStrength()))));
            }
         }

         for(int var17 = 0; var17 < var10.size(); ++var17) {
            var16 = (float)((double)var16 + ((WeatherPeriod.WeatherStage)var10.get(var17)).getStageDuration());
         }

         while(var16 < var15) {
            switch(this.RandNext(0, 10)) {
            case 0:
               var11 = this.createStage(5, (double)(1.0F + this.RandNext(0.0F, 3.0F * this.frontCache.getStrength())));
               break;
            case 1:
            case 2:
            case 3:
               var11 = this.createStage(1, (double)(2.0F + this.RandNext(0.0F, 4.0F * this.frontCache.getStrength())));
               break;
            default:
               var11 = this.createStage(2, (double)(2.0F + this.RandNext(0.0F, 4.0F * this.frontCache.getStrength())));
            }

            var16 = (float)((double)var16 + var11.getStageDuration());
            var10.add(var11);
         }
      } else {
         this.print("WeatherPeriod: Cold to warm front selected.");
         if (this.cloudColor == this.cloudColorReddish) {
            var7 = this.RandNext(0.0F, 100.0F);
            if (var7 < 50.0F) {
               this.cloudColor = this.cloudColorBlueish;
            } else {
               this.cloudColor = this.cloudColorPurplish;
            }
         }

         var18 = this.RandNext(12.0F, 24.0F) * this.frontCache.getStrength();
         float var19 = 0.0F;

         while(var19 < var18) {
            switch(this.RandNext(0, 10)) {
            case 0:
               var11 = this.createStage(1, (double)(2.0F + this.RandNext(0.0F, 3.0F * this.frontCache.getStrength())));
               break;
            case 1:
            case 2:
            case 3:
            case 4:
               var11 = this.createStage(6, (double)(2.0F + this.RandNext(0.0F, 3.0F * this.frontCache.getStrength())));
               break;
            default:
               var11 = this.createStage(5, (double)(2.0F + this.RandNext(0.0F, 3.0F * this.frontCache.getStrength())));
            }

            var19 = (float)((double)var19 + var11.getStageDuration());
            var10.add(var11);
         }
      }

      Collections.shuffle(var10, this.seededRandom);
      var18 = this.RandNext(30.0F, 60.0F);
      this.weatherStages.add(this.createStage(0, (double)(1.0F + this.RandNext(0.0F, 2.0F * this.frontCache.getStrength()))));

      int var20;
      for(var20 = 0; var20 < var10.size(); ++var20) {
         this.weatherStages.add((WeatherPeriod.WeatherStage)var10.get(var20));
         if (var20 < var10.size() - 1 && this.RandNext(0.0F, 100.0F) < var18) {
            this.weatherStages.add(this.createStage(4, (double)(1.0F + this.RandNext(0.0F, 2.0F * this.frontCache.getStrength()))));
            this.weatherStages.add(this.createStage(9, (double)(1.0F + this.RandNext(0.0F, 3.0F * this.frontCache.getStrength()))));
            var18 = this.RandNext(30.0F, 60.0F);
         }
      }

      if (((WeatherPeriod.WeatherStage)this.weatherStages.get(this.weatherStages.size() - 1)).getStageID() != 9) {
         this.weatherStages.add(this.createStage(4, (double)(2.0F + this.RandNext(0.0F, 3.0F * this.frontCache.getStrength()))));
      }

      for(var20 = 0; var20 < this.weatherStages.size(); ++var20) {
         WeatherPeriod.WeatherStage var10000 = (WeatherPeriod.WeatherStage)this.weatherStages.get(var20);
         var10000.stageDuration *= (double)var6;
      }

      this.linkWeatherStages();
      this.duration = 0.0D;

      for(var20 = 0; var20 < this.weatherStages.size(); ++var20) {
         this.duration += ((WeatherPeriod.WeatherStage)this.weatherStages.get(var20)).stageDuration;
      }

      this.print("WeatherPeriod: Duration = " + this.duration + ".");
      double var22 = this.startTime;

      for(int var21 = 0; var21 < this.weatherStages.size(); ++var21) {
         var22 = ((WeatherPeriod.WeatherStage)this.weatherStages.get(var21)).setStageStart(var22);
      }

      this.weatherStageIndex = 0;
      this.currentStage = ((WeatherPeriod.WeatherStage)this.weatherStages.get(this.weatherStageIndex)).startStage(this.startTime);
      this.print("WeatherPeriod: PATTERN GENERATION FINISHED.");
   }

   public WeatherPeriod.WeatherStage createAndAddModdedStage(String var1, double var2) {
      WeatherPeriod.WeatherStage var4 = this.createAndAddStage(10, var2, var1);
      return var4;
   }

   public WeatherPeriod.WeatherStage createAndAddStage(int var1, double var2) {
      return this.createAndAddStage(var1, var2, (String)null);
   }

   private WeatherPeriod.WeatherStage createAndAddStage(int var1, double var2, String var4) {
      if (!this.isRunning && this.hasStartedInit && (var1 != 10 || var4 != null)) {
         WeatherPeriod.WeatherStage var5 = this.createStage(var1, var2, var4);
         this.weatherStages.add(var5);
         return var5;
      } else {
         return null;
      }
   }

   private WeatherPeriod.WeatherStage createStage(int var1, double var2) {
      return this.createStage(var1, var2, (String)null);
   }

   private WeatherPeriod.WeatherStage createStage(int var1, double var2, String var4) {
      WeatherPeriod.WeatherStage var5 = null;
      if (!this.stagesPool.isEmpty()) {
         var5 = (WeatherPeriod.WeatherStage)this.stagesPool.pop();
      } else {
         var5 = new WeatherPeriod.WeatherStage();
      }

      var5.stageID = var1;
      var5.modID = var4;
      var5.setStageDuration(var2);
      switch(var1) {
      case 0:
         this.print("WeatherPeriod: Adding stage 'START' with duration: " + var2 + "%.");
         var5.lerpEntryTo(WeatherPeriod.StrLerpVal.NextTarget);
         break;
      case 1:
         this.print("WeatherPeriod: Adding stage 'SHOWERS' with duration: " + var2 + "%.");
         var5.targetStrength = this.frontCache.getStrength() * 0.5F;
         var5.lerpEntryTo(WeatherPeriod.StrLerpVal.Target, WeatherPeriod.StrLerpVal.NextTarget);
         break;
      case 2:
         this.print("WeatherPeriod: Adding stage 'HEAVY_PRECIP' with duration: " + var2 + "%.");
         var5.targetStrength = this.frontCache.getStrength();
         var5.lerpEntryTo(WeatherPeriod.StrLerpVal.Target, WeatherPeriod.StrLerpVal.Target);
         break;
      case 3:
      case 11:
         this.print("WeatherPeriod: Adding stage 'STORM' with duration: " + var2 + "%.");
         if (var1 == 11) {
            this.print("WeatherPeriod: this storm is a kate and bob storm...");
         }

         var5.targetStrength = this.frontCache.getStrength();
         var5.lerpEntryTo(WeatherPeriod.StrLerpVal.Target, WeatherPeriod.StrLerpVal.Target);
         if (this.RandNext(0, 100) < 33) {
            var5.fogStrength = 0.1F + this.RandNext(0.0F, 0.4F);
         }
         break;
      case 4:
         this.print("WeatherPeriod: Adding stage 'CLEARING' with duration: " + var2 + "%.");
         var5.targetStrength = this.frontCache.getStrength() * 0.25F;
         var5.lerpEntryTo(WeatherPeriod.StrLerpVal.Target, WeatherPeriod.StrLerpVal.None);
         break;
      case 5:
         this.print("WeatherPeriod: Adding stage 'MODERATE' with duration: " + var2 + "%.");
         var5.targetStrength = this.frontCache.getStrength() * 0.5F;
         var5.lerpEntryTo(WeatherPeriod.StrLerpVal.Target, WeatherPeriod.StrLerpVal.NextTarget);
         break;
      case 6:
         this.print("WeatherPeriod: Adding stage 'DRIZZLE' with duration: " + var2 + "%.");
         var5.targetStrength = this.frontCache.getStrength() * 0.25F;
         var5.lerpEntryTo(WeatherPeriod.StrLerpVal.Target, WeatherPeriod.StrLerpVal.NextTarget);
         break;
      case 7:
         this.print("WeatherPeriod: Adding stage 'BLIZZARD' with duration: " + var2 + "%.");
         var5.targetStrength = 1.0F;
         var5.lerpEntryTo(WeatherPeriod.StrLerpVal.Target, WeatherPeriod.StrLerpVal.Target);
         var5.fogStrength = 0.55F + this.RandNext(0.0F, 0.2F);
         break;
      case 8:
         this.print("WeatherPeriod: Adding stage 'TROPICAL_STORM' with duration: " + var2 + "%.");
         var5.targetStrength = 1.0F;
         var5.lerpEntryTo(WeatherPeriod.StrLerpVal.Target, WeatherPeriod.StrLerpVal.Target);
         var5.fogStrength = 0.6F + this.RandNext(0.0F, 0.4F);
         break;
      case 9:
         this.print("WeatherPeriod: Adding stage 'INTERMEZZO' with duration: " + var2 + "%.");
         var5.targetStrength = 0.0F;
         var5.lerpEntryTo(WeatherPeriod.StrLerpVal.Target, WeatherPeriod.StrLerpVal.NextTarget);
         break;
      case 10:
         this.print("WeatherPeriod: Adding stage 'MODDED' with duration: " + var2 + "%.");
         LuaEventManager.triggerEvent("OnInitModdedWeatherStage", this, var5, this.frontCache.getStrength());
         break;
      default:
         this.print("WeatherPeriod Warning: trying to _INIT_ state that is not recognized, state id=" + var1);
      }

      return var5;
   }

   private void updateCurrentStage() {
      if (!this.isDummy) {
         this.isBlizzard = false;
         this.isThunderStorm = false;
         this.isTropicalStorm = false;
         float var1;
         float var2;
         switch(this.currentStage.stageID) {
         case 0:
            this.rainThreshold = 0.35F - this.frontCache.getStrength() * 0.2F;
            this.climateManager.fogIntensity.setOverride(0.0F, this.currentStage.linearT);
            break;
         case 1:
            this.climateManager.fogIntensity.setOverride(0.0F, 1.0F);
            var2 = ClimateManager.clamp01(this.currentStage.parabolicT * 3.0F);
            this.climateManager.windIntensity.setOverride(0.1F * this.weatherNoise, var2);
            this.climateManager.windAngleIntensity.setOverride(0.0F, var2);
            break;
         case 2:
            var1 = this.frontCache.getStrength() * 0.5F;
            if (this.currentStage.linearT < 0.1F) {
               var1 = ClimateManager.clerp((float)((this.currentTime - this.currentStage.stageStart) / (this.currentStage.stageDuration * 0.1D)), 0.0F, this.frontCache.getStrength() * 0.5F);
            } else if (this.currentStage.linearT > 0.9F) {
               var1 = ClimateManager.clerp(1.0F - (float)((this.currentStage.stageEnd - this.currentTime) / (this.currentStage.stageDuration * 0.1D)), this.frontCache.getStrength() * 0.5F, 0.0F);
            }

            this.weatherNoise = var1 + this.weatherNoise * (1.0F - var1);
            this.climateManager.fogIntensity.setOverride(0.0F, 1.0F);
            var2 = ClimateManager.clamp01(this.currentStage.parabolicT * 3.0F);
            this.climateManager.windIntensity.setOverride(0.5F * this.weatherNoise, var2);
            this.climateManager.windAngleIntensity.setOverride(0.7F * this.weatherNoise * this.windAngleDirMod, var2);
            break;
         case 4:
            this.climateManager.fogIntensity.setOverride(0.0F, 1.0F - this.currentStage.linearT);
            break;
         case 5:
            this.climateManager.fogIntensity.setOverride(0.0F, 1.0F);
            break;
         case 6:
            this.climateManager.fogIntensity.setOverride(0.0F, 1.0F);
            break;
         case 7:
            this.isBlizzard = true;
            var1 = this.frontCache.getStrength() * 0.5F;
            if (this.currentStage.linearT < 0.1F) {
               var1 = ClimateManager.clerp((float)((this.currentTime - this.currentStage.stageStart) / (this.currentStage.stageDuration * 0.1D)), 0.0F, this.frontCache.getStrength() * 0.5F);
            } else if (this.currentStage.linearT > 0.9F) {
               var1 = ClimateManager.clerp(1.0F - (float)((this.currentStage.stageEnd - this.currentTime) / (this.currentStage.stageDuration * 0.1D)), this.frontCache.getStrength() * 0.5F, 0.0F);
            }

            this.weatherNoise = var1 + this.weatherNoise * (1.0F - var1);
            var2 = ClimateManager.clamp01(this.currentStage.parabolicT * 3.0F);
            this.climateManager.windIntensity.setOverride(0.75F + 0.25F * this.weatherNoise, var2);
            this.climateManager.windAngleIntensity.setOverride(0.7F * this.weatherNoise * this.windAngleDirMod, var2);
            if (PerformanceSettings.FogQuality != 2) {
               if (this.currentStage.fogStrength > 0.0F) {
                  this.climateManager.fogIntensity.setOverride(this.currentStage.fogStrength, var2);
               } else {
                  this.climateManager.fogIntensity.setOverride(1.0F, var2);
               }
            }
            break;
         case 8:
            this.isTropicalStorm = true;
         case 3:
         case 11:
            this.isThunderStorm = !this.isTropicalStorm;
            if (!this.currentStage.hasStartedCloud) {
               float var3 = this.frontCache.getAngleDegrees();
               float var4 = this.frontCache.getStrength();
               float var5 = 8000.0F * var4;
               float var6 = var4;
               float var7 = 0.6F * var4;
               double var8 = this.currentStage.stageDuration;
               boolean var10 = (double)var4 > 0.7D;
               int var11 = Rand.Next(1, 3);
               if (this.currentStage.stageID == 8) {
                  var11 = 1;
                  var5 = 15000.0F;
                  var7 = 0.8F;
                  var10 = true;
                  var4 = 1.0F;
               }

               for(int var12 = 0; var12 < var11; ++var12) {
                  ThunderStorm.ThunderCloud var13 = this.thunderStorm.startThunderCloud(var4, var3, var5, var6, var7, var8, var10, this.currentStage.stageID == 11 ? kateBobStormProgress : 0.0F);
                  if (this.currentStage.stageID == 11 && var10 && var13 != null) {
                     var13.setCenter(this.kateBobStormX, this.kateBobStormY, var3);
                  }

                  var10 = false;
               }

               this.currentStage.hasStartedCloud = true;
            }

            var1 = this.frontCache.getStrength() * 0.5F;
            if (this.currentStage.linearT < 0.1F) {
               var1 = ClimateManager.clerp((float)((this.currentTime - this.currentStage.stageStart) / (this.currentStage.stageDuration * 0.1D)), 0.0F, this.frontCache.getStrength() * 0.5F);
            } else if (this.currentStage.linearT > 0.9F) {
               var1 = ClimateManager.clerp(1.0F - (float)((this.currentStage.stageEnd - this.currentTime) / (this.currentStage.stageDuration * 0.1D)), this.frontCache.getStrength() * 0.5F, 0.0F);
            }

            this.weatherNoise = var1 + this.weatherNoise * (1.0F - var1);
            var2 = ClimateManager.clamp01(this.currentStage.parabolicT * 3.0F);
            if (this.currentStage.stageID == 8) {
               this.climateManager.windIntensity.setOverride(0.4F + 0.6F * this.weatherNoise, var2);
            } else {
               this.climateManager.windIntensity.setOverride(0.2F + 0.5F * this.weatherNoise, var2);
            }

            this.climateManager.windAngleIntensity.setOverride(0.7F * this.weatherNoise * this.windAngleDirMod, var2);
            if (PerformanceSettings.FogQuality != 2) {
               if (this.currentStage.fogStrength > 0.0F) {
                  this.climateManager.fogIntensity.setOverride(this.currentStage.fogStrength, var2);
                  if (this.currentStage.stageID == 8) {
                     this.climateManager.colorNewFog.setOverride(this.climateManager.getFogTintTropical(), var2);
                  } else {
                     this.climateManager.colorNewFog.setOverride(this.climateManager.getFogTintStorm(), var2);
                  }
               } else {
                  this.climateManager.fogIntensity.setOverride(0.0F, 1.0F);
               }
            }
            break;
         case 9:
            this.climateManager.fogIntensity.setOverride(0.0F, 1.0F);
            break;
         case 10:
            LuaEventManager.triggerEvent("OnUpdateModdedWeatherStage", this, this.currentStage, this.frontCache.getStrength());
            break;
         default:
            this.print("WeatherPeriod Warning: trying to _UPDATE_ state that is not recognized, state id=" + this.currentStage.stageID);
            this.resetClimateManagerOverrides();
            this.isRunning = false;
            if (GameServer.bServer) {
               this.climateManager.transmitClimatePacket(ClimateManager.ClimateNetAuth.ServerOnly, (byte)1, (UdpConnection)null);
            }
         }

      }
   }

   public void update(double var1) {
      if (!GameClient.bClient && !this.isDummy) {
         if (this.isRunning && this.currentStage != null && this.weatherStageIndex >= 0 && this.weatherStages.size() != 0) {
            if (this.currentTime > this.currentStage.stageEnd) {
               ++this.weatherStageIndex;
               LuaEventManager.triggerEvent("OnWeatherPeriodStage", this);
               if (this.weatherStageIndex >= this.weatherStages.size()) {
                  this.isRunning = false;
                  this.currentStage = null;
                  this.resetClimateManagerOverrides();
                  if (GameServer.bServer) {
                     this.climateManager.transmitClimatePacket(ClimateManager.ClimateNetAuth.ServerOnly, (byte)1, (UdpConnection)null);
                  }

                  return;
               }

               if (this.currentStage != null) {
                  this.currentStage.exitStrength = this.currentStrength;
               }

               this.currentStage = (WeatherPeriod.WeatherStage)this.weatherStages.get(this.weatherStageIndex);
               this.currentStage.entryStrength = this.currentStrength;
               this.currentStage.startStage(var1);
            }

            this.currentTime = var1;
            this.weatherNoise = 0.3F * this.frontCache.getStrength() + (float)SimplexNoise.noise(var1, 24000.0D) * (1.0F - 0.3F * this.frontCache.getStrength());
            this.weatherNoise = (this.weatherNoise + 1.0F) * 0.5F;
            this.currentStage.updateT(this.currentTime);
            this.stageProgress = this.currentStage.linearT;
            this.totalProgress = (float)(this.currentTime - ((WeatherPeriod.WeatherStage)this.weatherStages.get(0)).stageStart) / (float)this.duration;
            this.totalProgress = ClimateManager.clamp01(this.totalProgress);
            this.currentStrength = this.currentStage.getStageCurrentStrength();
            this.updateCurrentStage();
            float var3 = ClimateManager.clamp(-1.0F, 1.0F, this.currentStrength * 2.0F) * maxTemperatureInfluence;
            if (this.frontCache.getType() == 1) {
               this.temperatureInfluence = this.climateManager.temperature.internalValue - var3;
            } else {
               this.temperatureInfluence = this.climateManager.temperature.internalValue + var3;
            }

            if (this.isRunning) {
               if (this.weatherNoise > this.rainThreshold) {
                  this.precipitationFinal = (this.weatherNoise - this.rainThreshold) / (1.0F - this.rainThreshold);
                  this.precipitationFinal *= this.currentStrength;
               } else {
                  this.precipitationFinal = 0.0F;
               }

               float var4 = this.precipitationFinal;
               float var5 = var4 * (1.0F - this.climateManager.nightStrength.internalValue);
               float var6 = 0.5F;
               var6 += 0.5F * (1.0F - this.climateManager.nightStrength.internalValue);
               var6 = Math.max(var6, this.climateManager.cloudIntensity.internalValue);
               float var7 = 0.55F;
               if (PerformanceSettings.FogQuality != 2 && this.currentStage.stageID == 8) {
                  var7 += 0.35F * this.currentStage.parabolicT;
               }

               float var8 = 1.0F - var7 * var4;
               var8 = Math.min(var8, 1.0F - this.climateManager.nightStrength.internalValue);
               if (PerformanceSettings.FogQuality != 2 && this.currentStage.stageID == 7) {
                  float var9 = 1.0F - 0.75F * this.currentStage.parabolicT;
                  var6 *= var9;
               }

               this.climateManager.cloudIntensity.setOverride(var6, this.currentStrength);
               this.climateManager.precipitationIntensity.setOverride(this.precipitationFinal, 1.0F);
               this.climateManager.globalLight.setOverride(this.cloudColor, var5);
               this.climateManager.globalLightIntensity.setOverride(0.4F, var5);
               this.climateManager.desaturation.setOverride(0.3F, this.currentStrength);
               this.climateManager.temperature.setOverride(this.temperatureInfluence, this.currentStrength);
               this.climateManager.ambient.setOverride(var8, var4);
               this.climateManager.dayLightStrength.setOverride(var8, var4);
               if ((!(this.climateManager.getTemperature() < 0.0F) || !this.climateManager.getSeason().isSeason(5)) && !ClimateManager.WINTER_IS_COMING) {
                  this.climateManager.precipitationIsSnow.setEnableOverride(false);
               } else {
                  this.climateManager.precipitationIsSnow.setOverride(true);
               }
            }

         } else {
            if (this.isRunning) {
               this.resetClimateManagerOverrides();
               this.isRunning = false;
               LuaEventManager.triggerEvent("OnWeatherPeriodComplete", this);
               if (GameServer.bServer) {
                  this.climateManager.transmitClimatePacket(ClimateManager.ClimateNetAuth.ServerOnly, (byte)1, (UdpConnection)null);
               }
            }

         }
      }
   }

   private void resetClimateManagerOverrides() {
      if (this.climateManager != null && !this.isDummy) {
         this.climateManager.resetOverrides();
      }

   }

   public void save(DataOutputStream var1) throws IOException {
      if (GameClient.bClient && !GameServer.bServer) {
         var1.writeByte(0);
      } else {
         var1.writeByte(1);
         var1.writeBoolean(this.isRunning);
         if (this.isRunning) {
            var1.writeInt(this.weatherStageIndex);
            var1.writeFloat(this.currentStrength);
            var1.writeFloat(this.rainThreshold);
            var1.writeBoolean(this.isThunderStorm);
            var1.writeBoolean(this.isTropicalStorm);
            var1.writeBoolean(this.isBlizzard);
            this.frontCache.save(var1);
            var1.writeInt(this.weatherStages.size());

            for(int var2 = 0; var2 < this.weatherStages.size(); ++var2) {
               WeatherPeriod.WeatherStage var3 = (WeatherPeriod.WeatherStage)this.weatherStages.get(var2);
               var1.writeInt(var3.stageID);
               var1.writeDouble(var3.stageDuration);
               var3.save(var1);
            }

            this.cloudColor.save(var1);
         }
      }

   }

   public void load(DataInputStream var1, int var2) throws IOException {
      byte var3 = var1.readByte();
      if (var3 == 1) {
         this.isRunning = var1.readBoolean();
         if (this.isRunning) {
            this.weatherStageIndex = var1.readInt();
            this.currentStrength = var1.readFloat();
            this.rainThreshold = var1.readFloat();
            this.isThunderStorm = var1.readBoolean();
            this.isTropicalStorm = var1.readBoolean();
            this.isBlizzard = var1.readBoolean();
            this.frontCache.load(var1);
            if (this.frontCache.getAngleDegrees() >= 90.0F && this.frontCache.getAngleDegrees() < 270.0F) {
               this.windAngleDirMod = 1.0F;
            } else {
               this.windAngleDirMod = -1.0F;
            }

            this.print("WeatherPeriod: Loading weather pattern with strength = " + this.frontCache.getStrength());
            this.clearCurrentWeatherStages();
            int var4 = var1.readInt();

            int var5;
            for(var5 = 0; var5 < var4; ++var5) {
               int var6 = var1.readInt();
               double var7 = var1.readDouble();
               WeatherPeriod.WeatherStage var9 = !this.stagesPool.isEmpty() ? (WeatherPeriod.WeatherStage)this.stagesPool.pop() : new WeatherPeriod.WeatherStage();
               var9.stageID = var6;
               var9.setStageDuration(var7);
               var9.load(var1, var2);
               this.weatherStages.add(var9);
            }

            if (var2 >= 170) {
               this.cloudColor.load(var1, var2);
            }

            this.linkWeatherStages();
            this.duration = 0.0D;

            for(var5 = 0; var5 < this.weatherStages.size(); ++var5) {
               this.duration += ((WeatherPeriod.WeatherStage)this.weatherStages.get(var5)).stageDuration;
            }

            if (this.weatherStageIndex >= 0 && this.weatherStageIndex < this.weatherStages.size()) {
               this.currentStage = (WeatherPeriod.WeatherStage)this.weatherStages.get(this.weatherStageIndex);
               this.print("WeatherPeriod: Pattern loaded!");
            } else {
               this.print("WeatherPeriod: Couldnt load stages correctly.");
               this.isRunning = false;
            }
         }
      }

   }

   public static class WeatherStage {
      protected WeatherPeriod.WeatherStage previousStage;
      protected WeatherPeriod.WeatherStage nextStage;
      private double stageStart;
      private double stageEnd;
      private double stageDuration;
      protected int stageID;
      protected float entryStrength;
      protected float exitStrength;
      protected float targetStrength;
      protected WeatherPeriod.StrLerpVal lerpMidVal;
      protected WeatherPeriod.StrLerpVal lerpEndVal;
      protected boolean hasStartedCloud = false;
      protected float fogStrength = 0.0F;
      protected float linearT;
      protected float parabolicT;
      protected boolean isCycleFirstHalf = true;
      protected boolean creationFinished = false;
      protected String modID;
      private float m;
      private float e;

      public WeatherStage() {
      }

      public WeatherStage(int var1) {
         this.stageID = var1;
      }

      public void setStageID(int var1) {
         this.stageID = var1;
      }

      public double getStageStart() {
         return this.stageStart;
      }

      public double getStageEnd() {
         return this.stageEnd;
      }

      public double getStageDuration() {
         return this.stageDuration;
      }

      public int getStageID() {
         return this.stageID;
      }

      public String getModID() {
         return this.modID;
      }

      public float getLinearT() {
         return this.linearT;
      }

      public float getParabolicT() {
         return this.parabolicT;
      }

      public void setTargetStrength(float var1) {
         this.targetStrength = var1;
      }

      public boolean getHasStartedCloud() {
         return this.hasStartedCloud;
      }

      public void setHasStartedCloud(boolean var1) {
         this.hasStartedCloud = true;
      }

      public void save(DataOutputStream var1) throws IOException {
         var1.writeDouble(this.stageStart);
         var1.writeFloat(this.entryStrength);
         var1.writeFloat(this.exitStrength);
         var1.writeFloat(this.targetStrength);
         var1.writeInt(this.lerpMidVal.getValue());
         var1.writeInt(this.lerpEndVal.getValue());
         var1.writeBoolean(this.hasStartedCloud);
         var1.writeByte(this.modID != null ? 1 : 0);
         if (this.modID != null) {
            GameWindow.WriteString(var1, this.modID);
         }

         var1.writeFloat(this.fogStrength);
      }

      public void load(DataInputStream var1, int var2) throws IOException {
         this.stageStart = var1.readDouble();
         this.stageEnd = this.stageStart + this.stageDuration;
         this.entryStrength = var1.readFloat();
         this.exitStrength = var1.readFloat();
         this.targetStrength = var1.readFloat();
         this.lerpMidVal = WeatherPeriod.StrLerpVal.fromValue(var1.readInt());
         this.lerpEndVal = WeatherPeriod.StrLerpVal.fromValue(var1.readInt());
         this.hasStartedCloud = var1.readBoolean();
         if (var2 >= 141 && var1.readByte() == 1) {
            this.modID = GameWindow.ReadString(var1);
         }

         if (var2 >= 170) {
            this.fogStrength = var1.readFloat();
         }

      }

      protected void reset() {
         this.previousStage = null;
         this.nextStage = null;
         this.isCycleFirstHalf = true;
         this.hasStartedCloud = false;
         this.lerpMidVal = WeatherPeriod.StrLerpVal.None;
         this.lerpEndVal = WeatherPeriod.StrLerpVal.None;
         this.entryStrength = 0.0F;
         this.exitStrength = 0.0F;
         this.modID = null;
         this.creationFinished = false;
         this.fogStrength = 0.0F;
      }

      protected WeatherPeriod.WeatherStage startStage(double var1) {
         this.stageStart = var1;
         this.stageEnd = var1 + this.stageDuration;
         this.hasStartedCloud = false;
         return this;
      }

      protected double setStageStart(double var1) {
         this.stageStart = var1;
         this.stageEnd = var1 + this.stageDuration;
         return this.stageEnd;
      }

      protected WeatherPeriod.WeatherStage setStageDuration(double var1) {
         this.stageDuration = var1;
         if (this.stageDuration < 1.0D) {
            this.stageDuration = 1.0D;
         }

         return this;
      }

      protected WeatherPeriod.WeatherStage overrideStageDuration(double var1) {
         this.stageDuration = var1;
         return this;
      }

      public void lerpEntryTo(int var1, int var2) {
         if (!this.creationFinished) {
            this.lerpEntryTo(WeatherPeriod.StrLerpVal.fromValue(var1), WeatherPeriod.StrLerpVal.fromValue(var2));
         }

      }

      protected void lerpEntryTo(WeatherPeriod.StrLerpVal var1) {
         this.lerpEntryTo(WeatherPeriod.StrLerpVal.None, var1);
      }

      protected void lerpEntryTo(WeatherPeriod.StrLerpVal var1, WeatherPeriod.StrLerpVal var2) {
         if (!this.creationFinished) {
            this.lerpMidVal = var1;
            this.lerpEndVal = var2;
         }

      }

      public float getStageCurrentStrength() {
         this.m = this.getLerpValue(this.lerpMidVal);
         this.e = this.getLerpValue(this.lerpEndVal);
         if (this.lerpMidVal == WeatherPeriod.StrLerpVal.None) {
            return ClimateManager.clerp(this.linearT, this.entryStrength, this.e);
         } else {
            return this.isCycleFirstHalf ? ClimateManager.clerp(this.parabolicT, this.entryStrength, this.m) : ClimateManager.clerp(this.parabolicT, this.e, this.m);
         }
      }

      private float getLerpValue(WeatherPeriod.StrLerpVal var1) {
         switch(var1) {
         case Entry:
            return this.entryStrength;
         case Target:
            return this.targetStrength;
         case NextTarget:
            return this.nextStage != null ? this.nextStage.targetStrength : 0.0F;
         case None:
            return 0.0F;
         default:
            return 0.0F;
         }
      }

      private WeatherPeriod.WeatherStage updateT(double var1) {
         this.linearT = this.getPeriodLerpT(var1);
         if (this.stageID == 11) {
            this.linearT = WeatherPeriod.kateBobStormProgress + (1.0F - WeatherPeriod.kateBobStormProgress) * this.linearT;
         }

         if (this.linearT < 0.5F) {
            this.parabolicT = this.linearT * 2.0F;
            this.isCycleFirstHalf = true;
         } else {
            this.parabolicT = 2.0F - this.linearT * 2.0F;
            this.isCycleFirstHalf = false;
         }

         return this;
      }

      private float getPeriodLerpT(double var1) {
         if (var1 < this.stageStart) {
            return 0.0F;
         } else {
            return var1 > this.stageEnd ? 1.0F : (float)((var1 - this.stageStart) / this.stageDuration);
         }
      }
   }

   public static enum StrLerpVal {
      Entry(1),
      Target(2),
      NextTarget(3),
      None(0);

      private final int value;

      private StrLerpVal(int var3) {
         this.value = var3;
         if (WeatherPeriod.cache.containsKey(var3)) {
            DebugLog.log("StrLerpVal WARNING: trying to add id twice. id=" + var3);
         }

         WeatherPeriod.cache.put(var3, this);
      }

      public int getValue() {
         return this.value;
      }

      public static WeatherPeriod.StrLerpVal fromValue(int var0) {
         if (WeatherPeriod.cache.containsKey(var0)) {
            return (WeatherPeriod.StrLerpVal)WeatherPeriod.cache.get(var0);
         } else {
            DebugLog.log("StrLerpVal, trying to get from invalid id: " + var0);
            return None;
         }
      }

      // $FF: synthetic method
      private static WeatherPeriod.StrLerpVal[] $values() {
         return new WeatherPeriod.StrLerpVal[]{Entry, Target, NextTarget, None};
      }
   }
}

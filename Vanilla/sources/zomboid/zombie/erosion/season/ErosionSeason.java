package zombie.erosion.season;

import java.util.GregorianCalendar;
import zombie.debug.DebugLog;
import zombie.erosion.utils.Noise2D;

public final class ErosionSeason {
   public static final int SEASON_DEFAULT = 0;
   public static final int SEASON_SPRING = 1;
   public static final int SEASON_SUMMER = 2;
   public static final int SEASON_SUMMER2 = 3;
   public static final int SEASON_AUTUMN = 4;
   public static final int SEASON_WINTER = 5;
   public static final int NUM_SEASONS = 6;
   private int lat = 38;
   private int tempMax = 25;
   private int tempMin = 0;
   private int tempDiff = 7;
   private float highNoon = 12.5F;
   private float highNoonCurrent = 12.5F;
   private int seasonLag = 31;
   private final float[] rain = new float[]{0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F};
   private double suSol;
   private double wiSol;
   private final GregorianCalendar zeroDay = new GregorianCalendar(1970, 0, 1, 0, 0);
   private int day;
   private int month;
   private int year;
   private boolean isH1;
   private ErosionSeason.YearData[] yearData = new ErosionSeason.YearData[3];
   private int curSeason;
   private float curSeasonDay;
   private float curSeasonDays;
   private float curSeasonStrength;
   private float curSeasonProgression;
   private float dayMeanTemperature;
   private float dayTemperature;
   private float dayNoiseVal;
   private boolean isRainDay;
   private float rainYearAverage;
   private float rainDayStrength;
   private boolean isThunderDay;
   private boolean isSunnyDay;
   private float dayDusk;
   private float dayDawn;
   private float dayDaylight;
   private float winterMod;
   private float summerMod;
   private float summerTilt;
   private float curDayPercent = 0.0F;
   private Noise2D per = new Noise2D();
   private int seedA = 64;
   private int seedB = 128;
   private int seedC = 255;
   String[] names = new String[]{"Default", "Spring", "Early Summer", "Late Summer", "Autumn", "Winter"};

   public void init(int var1, int var2, int var3, int var4, int var5, float var6, int var7, int var8, int var9) {
      this.lat = var1;
      this.tempMax = var2;
      this.tempMin = var3;
      this.tempDiff = var4;
      this.seasonLag = var5;
      this.highNoon = var6;
      this.highNoonCurrent = var6;
      this.seedA = var7;
      this.seedB = var8;
      this.seedC = var9;
      this.summerTilt = 2.0F;
      this.winterMod = this.tempMin < 0 ? 0.05F * (float)(-this.tempMin) : 0.02F * (float)(-this.tempMin);
      this.summerMod = this.tempMax < 0 ? 0.05F * (float)this.tempMax : 0.02F * (float)this.tempMax;
      this.suSol = 2.0D * this.degree(Math.acos(-Math.tan(this.radian((double)this.lat)) * Math.tan(this.radian(23.44D)))) / 15.0D;
      this.wiSol = 2.0D * this.degree(Math.acos(Math.tan(this.radian((double)this.lat)) * Math.tan(this.radian(23.44D)))) / 15.0D;
      this.per.reset();
      this.per.addLayer(var7, 8.0F, 2.0F);
      this.per.addLayer(var8, 6.0F, 4.0F);
      this.per.addLayer(var9, 4.0F, 6.0F);
      this.yearData[0] = new ErosionSeason.YearData();
      this.yearData[1] = new ErosionSeason.YearData();
      this.yearData[2] = new ErosionSeason.YearData();
   }

   public int getLat() {
      return this.lat;
   }

   public int getTempMax() {
      return this.tempMax;
   }

   public int getTempMin() {
      return this.tempMin;
   }

   public int getTempDiff() {
      return this.tempDiff;
   }

   public int getSeasonLag() {
      return this.seasonLag;
   }

   public float getHighNoon() {
      return this.highNoon;
   }

   public int getSeedA() {
      return this.seedA;
   }

   public int getSeedB() {
      return this.seedB;
   }

   public int getSeedC() {
      return this.seedC;
   }

   public void setRain(float var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8, float var9, float var10, float var11, float var12) {
      this.rain[0] = var1;
      this.rain[1] = var2;
      this.rain[2] = var3;
      this.rain[3] = var4;
      this.rain[4] = var5;
      this.rain[5] = var6;
      this.rain[6] = var7;
      this.rain[7] = var8;
      this.rain[8] = var9;
      this.rain[9] = var10;
      this.rain[10] = var11;
      this.rain[11] = var12;
      float var13 = 0.0F;
      float[] var14 = this.rain;
      int var15 = var14.length;

      for(int var16 = 0; var16 < var15; ++var16) {
         float var17 = var14[var16];
         var13 += var17;
      }

      this.rainYearAverage = (float)((int)Math.floor((double)(365.0F * (var13 / (float)this.rain.length))));
   }

   public ErosionSeason clone() {
      ErosionSeason var1 = new ErosionSeason();
      var1.init(this.lat, this.tempMax, this.tempMin, this.tempDiff, this.seasonLag, this.highNoon, this.seedA, this.seedB, this.seedC);
      var1.setRain(this.rain[0], this.rain[1], this.rain[2], this.rain[3], this.rain[4], this.rain[5], this.rain[6], this.rain[7], this.rain[8], this.rain[9], this.rain[10], this.rain[11]);
      return var1;
   }

   public float getCurDayPercent() {
      return this.curDayPercent;
   }

   public double getMaxDaylightWinter() {
      return this.wiSol;
   }

   public double getMaxDaylightSummer() {
      return this.suSol;
   }

   public float getDusk() {
      return this.dayDusk;
   }

   public float getDawn() {
      return this.dayDawn;
   }

   public float getDaylight() {
      return this.dayDaylight;
   }

   public float getDayTemperature() {
      return this.dayTemperature;
   }

   public float getDayMeanTemperature() {
      return this.dayMeanTemperature;
   }

   public int getSeason() {
      return this.curSeason;
   }

   public float getDayHighNoon() {
      return this.highNoonCurrent;
   }

   public String getSeasonName() {
      return this.names[this.curSeason];
   }

   public boolean isSeason(int var1) {
      return var1 == this.curSeason;
   }

   public GregorianCalendar getWinterStartDay(int var1, int var2, int var3) {
      GregorianCalendar var4 = new GregorianCalendar(var3, var2, var1);
      long var5 = var4.getTime().getTime();
      return var5 < this.yearData[0].winterEndDayUnx ? this.yearData[0].winterStartDay : this.yearData[1].winterStartDay;
   }

   public float getSeasonDay() {
      return this.curSeasonDay;
   }

   public float getSeasonDays() {
      return this.curSeasonDays;
   }

   public float getSeasonStrength() {
      return this.curSeasonStrength;
   }

   public float getSeasonProgression() {
      return this.curSeasonProgression;
   }

   public float getDayNoiseVal() {
      return this.dayNoiseVal;
   }

   public boolean isRainDay() {
      return this.isRainDay;
   }

   public float getRainDayStrength() {
      return this.rainDayStrength;
   }

   public float getRainYearAverage() {
      return this.rainYearAverage;
   }

   public boolean isThunderDay() {
      return this.isThunderDay;
   }

   public boolean isSunnyDay() {
      return this.isSunnyDay;
   }

   public void setDay(int var1, int var2, int var3) {
      if (var3 == 0) {
         DebugLog.log("NOTICE: year value is 0?");
      }

      GregorianCalendar var4 = new GregorianCalendar(var3, var2, var1, 0, 0);
      long var5 = var4.getTime().getTime();
      this.setYearData(var3);
      this.setSeasonData((float)var5, var4, var3, var2);
      this.setDaylightData(var5, var4);
   }

   private void setYearData(int var1) {
      if (this.yearData[1].year != var1) {
         for(int var2 = 0; var2 < 3; ++var2) {
            int var3 = var2 - 1;
            int var4 = var1 + var3;
            this.yearData[var2].year = var4;
            this.yearData[var2].winSols = new GregorianCalendar(var4, 11, 22);
            this.yearData[var2].sumSols = new GregorianCalendar(var4, 5, 22);
            this.yearData[var2].winSolsUnx = this.yearData[var2].winSols.getTime().getTime();
            this.yearData[var2].sumSolsUnx = this.yearData[var2].sumSols.getTime().getTime();
            this.yearData[var2].hottestDay = new GregorianCalendar(var4, 5, 22);
            this.yearData[var2].coldestDay = new GregorianCalendar(var4, 11, 22);
            this.yearData[var2].hottestDay.add(5, this.seasonLag);
            this.yearData[var2].coldestDay.add(5, this.seasonLag);
            this.yearData[var2].hottestDayUnx = this.yearData[var2].hottestDay.getTime().getTime();
            this.yearData[var2].coldestDayUnx = this.yearData[var2].coldestDay.getTime().getTime();
            this.yearData[var2].winterS = this.per.layeredNoise((float)(64 + var4), 64.0F);
            this.yearData[var2].winterE = this.per.layeredNoise(64.0F, (float)(64 + var4));
            this.yearData[var2].winterStartDay = new GregorianCalendar(var4, 11, 22);
            this.yearData[var2].winterEndDay = new GregorianCalendar(var4, 11, 22);
            this.yearData[var2].winterStartDay.add(5, (int)(-Math.floor((double)(40.0F + 40.0F * this.winterMod + 20.0F * this.yearData[var2].winterS))));
            this.yearData[var2].winterEndDay.add(5, (int)Math.floor((double)(40.0F + 40.0F * this.winterMod + 20.0F * this.yearData[var2].winterE)));
            this.yearData[var2].winterStartDayUnx = this.yearData[var2].winterStartDay.getTime().getTime();
            this.yearData[var2].winterEndDayUnx = this.yearData[var2].winterEndDay.getTime().getTime();
            this.yearData[var2].summerS = this.per.layeredNoise((float)(128 + var4), 128.0F);
            this.yearData[var2].summerE = this.per.layeredNoise(128.0F, (float)(128 + var4));
            this.yearData[var2].summerStartDay = new GregorianCalendar(var4, 5, 22);
            this.yearData[var2].summerEndDay = new GregorianCalendar(var4, 5, 22);
            this.yearData[var2].summerStartDay.add(5, (int)(-Math.floor((double)(40.0F + 40.0F * this.summerMod + 20.0F * this.yearData[var2].summerS))));
            this.yearData[var2].summerEndDay.add(5, (int)Math.floor((double)(40.0F + 40.0F * this.summerMod + 20.0F * this.yearData[var2].summerE)));
            this.yearData[var2].summerStartDayUnx = this.yearData[var2].summerStartDay.getTime().getTime();
            this.yearData[var2].summerEndDayUnx = this.yearData[var2].summerEndDay.getTime().getTime();
         }

         this.yearData[1].lastSummerStr = this.yearData[0].summerS + this.yearData[0].summerE - 1.0F;
         this.yearData[1].lastWinterStr = this.yearData[0].winterS + this.yearData[0].winterE - 1.0F;
         this.yearData[1].summerStr = this.yearData[1].summerS + this.yearData[1].summerE - 1.0F;
         this.yearData[1].winterStr = this.yearData[1].winterS + this.yearData[1].winterE - 1.0F;
         this.yearData[1].nextSummerStr = this.yearData[2].summerS + this.yearData[2].summerE - 1.0F;
         this.yearData[1].nextWinterStr = this.yearData[2].winterS + this.yearData[2].winterE - 1.0F;
      }
   }

   private void setSeasonData(float var1, GregorianCalendar var2, int var3, int var4) {
      GregorianCalendar var5;
      GregorianCalendar var6;
      if (var1 < (float)this.yearData[0].winterEndDayUnx) {
         this.curSeason = 5;
         var5 = this.yearData[0].winterStartDay;
         var6 = this.yearData[0].winterEndDay;
      } else if (var1 < (float)this.yearData[1].summerStartDayUnx) {
         this.curSeason = 1;
         var5 = this.yearData[0].winterEndDay;
         var6 = this.yearData[1].summerStartDay;
      } else if (var1 < (float)this.yearData[1].summerEndDayUnx) {
         this.curSeason = 2;
         var5 = this.yearData[1].summerStartDay;
         var6 = this.yearData[1].summerEndDay;
      } else if (var1 < (float)this.yearData[1].winterStartDayUnx) {
         this.curSeason = 4;
         var5 = this.yearData[1].summerEndDay;
         var6 = this.yearData[1].winterStartDay;
      } else {
         this.curSeason = 5;
         var5 = this.yearData[1].winterStartDay;
         var6 = this.yearData[1].winterEndDay;
      }

      this.curSeasonDay = this.dayDiff(var2, var5);
      this.curSeasonDays = this.dayDiff(var5, var6);
      this.curSeasonStrength = this.curSeasonDays / 90.0F - 1.0F;
      this.curSeasonProgression = this.curSeasonDay / this.curSeasonDays;
      float var7;
      float var8;
      float var9;
      if (var1 < (float)this.yearData[0].coldestDayUnx && var1 >= (float)this.yearData[0].hottestDayUnx) {
         var7 = (float)this.tempMax + (float)(this.tempDiff / 2) * this.yearData[1].lastSummerStr;
         var8 = (float)this.tempMin + (float)(this.tempDiff / 2) * this.yearData[1].lastWinterStr;
         var9 = this.dayDiff(var2, this.yearData[0].hottestDay) / this.dayDiff(this.yearData[0].hottestDay, this.yearData[0].coldestDay);
      } else if (var1 < (float)this.yearData[1].hottestDayUnx && var1 >= (float)this.yearData[0].coldestDayUnx) {
         var7 = (float)this.tempMin + (float)(this.tempDiff / 2) * this.yearData[1].lastWinterStr;
         var8 = (float)this.tempMax + (float)(this.tempDiff / 2) * this.yearData[1].summerStr;
         var9 = this.dayDiff(var2, this.yearData[0].coldestDay) / this.dayDiff(this.yearData[1].hottestDay, this.yearData[0].coldestDay);
      } else if (var1 < (float)this.yearData[1].coldestDayUnx && var1 >= (float)this.yearData[1].hottestDayUnx) {
         var7 = (float)this.tempMax + (float)(this.tempDiff / 2) * this.yearData[1].summerStr;
         var8 = (float)this.tempMin + (float)(this.tempDiff / 2) * this.yearData[1].winterStr;
         var9 = this.dayDiff(var2, this.yearData[1].hottestDay) / this.dayDiff(this.yearData[1].hottestDay, this.yearData[1].coldestDay);
      } else {
         var7 = (float)this.tempMin + (float)(this.tempDiff / 2) * this.yearData[1].winterStr;
         var8 = (float)this.tempMax + (float)(this.tempDiff / 2) * this.yearData[1].nextSummerStr;
         var9 = this.dayDiff(var2, this.yearData[1].coldestDay) / this.dayDiff(this.yearData[1].coldestDay, this.yearData[2].hottestDay);
      }

      float var10 = (float)this.clerp((double)var9, (double)var7, (double)var8);
      float var11 = this.dayDiff(this.zeroDay, var2) / 20.0F;
      this.dayNoiseVal = this.per.layeredNoise(var11, 0.0F);
      float var12 = this.dayNoiseVal * 2.0F - 1.0F;
      this.dayTemperature = var10 + (float)this.tempDiff * var12;
      this.dayMeanTemperature = var10;
      this.isThunderDay = false;
      this.isRainDay = false;
      this.isSunnyDay = false;
      float var13 = 0.1F + this.rain[var4] <= 1.0F ? 0.1F + this.rain[var4] : 1.0F;
      if (var13 > 0.0F && this.dayNoiseVal < var13) {
         this.isRainDay = true;
         this.rainDayStrength = 1.0F - this.dayNoiseVal / var13;
         float var14 = this.per.layeredNoise(0.0F, var11);
         if ((double)var14 > 0.6D) {
            this.isThunderDay = true;
         }
      }

      if ((double)this.dayNoiseVal > 0.6D) {
         this.isSunnyDay = true;
      }

   }

   private void setDaylightData(long var1, GregorianCalendar var3) {
      GregorianCalendar var4;
      GregorianCalendar var5;
      if (var1 < this.yearData[1].winSolsUnx && var1 >= this.yearData[1].sumSolsUnx) {
         this.isH1 = false;
         var4 = this.yearData[1].sumSols;
         var5 = this.yearData[1].winSols;
      } else {
         this.isH1 = true;
         if (var1 >= this.yearData[1].winSolsUnx) {
            var4 = this.yearData[1].winSols;
            var5 = this.yearData[2].sumSols;
         } else {
            var4 = this.yearData[0].winSols;
            var5 = this.yearData[1].sumSols;
         }
      }

      float var6 = this.dayDiff(var3, var4) / this.dayDiff(var4, var5);
      float var7 = var6;
      if (this.isH1) {
         this.dayDaylight = (float)this.clerp((double)var6, this.wiSol, this.suSol);
      } else {
         this.dayDaylight = (float)this.clerp((double)var6, this.suSol, this.wiSol);
         var7 = 1.0F - var6;
      }

      this.curDayPercent = var7;
      this.highNoonCurrent = this.highNoon + this.summerTilt * var7;
      this.dayDawn = this.highNoonCurrent - this.dayDaylight / 2.0F;
      this.dayDusk = this.highNoonCurrent + this.dayDaylight / 2.0F;
   }

   private float dayDiff(GregorianCalendar var1, GregorianCalendar var2) {
      long var3 = var1.getTime().getTime() - var2.getTime().getTime();
      return (float)Math.abs(var3 / 86400000L);
   }

   private double clerp(double var1, double var3, double var5) {
      double var7 = (1.0D - Math.cos(var1 * 3.141592653589793D)) / 2.0D;
      return var3 * (1.0D - var7) + var5 * var7;
   }

   private double lerp(double var1, double var3, double var5) {
      return var3 + var1 * (var5 - var3);
   }

   private double radian(double var1) {
      return var1 * 0.017453292519943295D;
   }

   private double degree(double var1) {
      return var1 * 57.29577951308232D;
   }

   public static void Reset() {
   }

   public void setCurSeason(int var1) {
      this.curSeason = var1;
   }

   private static class YearData {
      public int year = Integer.MIN_VALUE;
      public GregorianCalendar winSols;
      public GregorianCalendar sumSols;
      public long winSolsUnx;
      public long sumSolsUnx;
      public GregorianCalendar hottestDay;
      public GregorianCalendar coldestDay;
      public long hottestDayUnx;
      public long coldestDayUnx;
      public float winterS;
      public float winterE;
      public GregorianCalendar winterStartDay;
      public GregorianCalendar winterEndDay;
      public long winterStartDayUnx;
      public long winterEndDayUnx;
      public float summerS;
      public float summerE;
      public GregorianCalendar summerStartDay;
      public GregorianCalendar summerEndDay;
      public long summerStartDayUnx;
      public long summerEndDayUnx;
      public float lastSummerStr;
      public float lastWinterStr;
      public float summerStr;
      public float winterStr;
      public float nextSummerStr;
      public float nextWinterStr;
   }
}

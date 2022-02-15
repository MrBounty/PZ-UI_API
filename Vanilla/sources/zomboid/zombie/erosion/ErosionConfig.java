package zombie.erosion;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.nio.ByteBuffer;
import zombie.core.Core;
import zombie.debug.DebugLog;

public final class ErosionConfig {
   public final ErosionConfig.Seeds seeds = new ErosionConfig.Seeds();
   public final ErosionConfig.Time time = new ErosionConfig.Time();
   public final ErosionConfig.Debug debug = new ErosionConfig.Debug();
   public final ErosionConfig.Season season = new ErosionConfig.Season();

   public void save(ByteBuffer var1) {
      var1.putInt(this.seeds.seedMain_0);
      var1.putInt(this.seeds.seedMain_1);
      var1.putInt(this.seeds.seedMain_2);
      var1.putInt(this.seeds.seedMoisture_0);
      var1.putInt(this.seeds.seedMoisture_1);
      var1.putInt(this.seeds.seedMoisture_2);
      var1.putInt(this.seeds.seedMinerals_0);
      var1.putInt(this.seeds.seedMinerals_1);
      var1.putInt(this.seeds.seedMinerals_2);
      var1.putInt(this.seeds.seedKudzu_0);
      var1.putInt(this.seeds.seedKudzu_1);
      var1.putInt(this.seeds.seedKudzu_2);
      var1.putInt(this.time.tickunit);
      var1.putInt(this.time.ticks);
      var1.putInt(this.time.eticks);
      var1.putInt(this.time.epoch);
      var1.putInt(this.season.lat);
      var1.putInt(this.season.tempMax);
      var1.putInt(this.season.tempMin);
      var1.putInt(this.season.tempDiff);
      var1.putInt(this.season.seasonLag);
      var1.putFloat(this.season.noon);
      var1.putInt(this.season.seedA);
      var1.putInt(this.season.seedB);
      var1.putInt(this.season.seedC);
      var1.putFloat(this.season.jan);
      var1.putFloat(this.season.feb);
      var1.putFloat(this.season.mar);
      var1.putFloat(this.season.apr);
      var1.putFloat(this.season.may);
      var1.putFloat(this.season.jun);
      var1.putFloat(this.season.jul);
      var1.putFloat(this.season.aug);
      var1.putFloat(this.season.sep);
      var1.putFloat(this.season.oct);
      var1.putFloat(this.season.nov);
      var1.putFloat(this.season.dec);
   }

   public void load(ByteBuffer var1) {
      this.seeds.seedMain_0 = var1.getInt();
      this.seeds.seedMain_1 = var1.getInt();
      this.seeds.seedMain_2 = var1.getInt();
      this.seeds.seedMoisture_0 = var1.getInt();
      this.seeds.seedMoisture_1 = var1.getInt();
      this.seeds.seedMoisture_2 = var1.getInt();
      this.seeds.seedMinerals_0 = var1.getInt();
      this.seeds.seedMinerals_1 = var1.getInt();
      this.seeds.seedMinerals_2 = var1.getInt();
      this.seeds.seedKudzu_0 = var1.getInt();
      this.seeds.seedKudzu_1 = var1.getInt();
      this.seeds.seedKudzu_2 = var1.getInt();
      this.time.tickunit = var1.getInt();
      this.time.ticks = var1.getInt();
      this.time.eticks = var1.getInt();
      this.time.epoch = var1.getInt();
      this.season.lat = var1.getInt();
      this.season.tempMax = var1.getInt();
      this.season.tempMin = var1.getInt();
      this.season.tempDiff = var1.getInt();
      this.season.seasonLag = var1.getInt();
      this.season.noon = var1.getFloat();
      this.season.seedA = var1.getInt();
      this.season.seedB = var1.getInt();
      this.season.seedC = var1.getInt();
      this.season.jan = var1.getFloat();
      this.season.feb = var1.getFloat();
      this.season.mar = var1.getFloat();
      this.season.apr = var1.getFloat();
      this.season.may = var1.getFloat();
      this.season.jun = var1.getFloat();
      this.season.jul = var1.getFloat();
      this.season.aug = var1.getFloat();
      this.season.sep = var1.getFloat();
      this.season.oct = var1.getFloat();
      this.season.nov = var1.getFloat();
      this.season.dec = var1.getFloat();
   }

   public void writeFile(String var1) {
      try {
         if (Core.getInstance().isNoSave()) {
            return;
         }

         File var2 = new File(var1);
         if (!var2.exists()) {
            var2.createNewFile();
         }

         FileWriter var3 = new FileWriter(var2, false);
         var3.write("seeds.seedMain_0 = " + this.seeds.seedMain_0 + "\n");
         var3.write("seeds.seedMain_1 = " + this.seeds.seedMain_1 + "\n");
         var3.write("seeds.seedMain_2 = " + this.seeds.seedMain_2 + "\n");
         var3.write("seeds.seedMoisture_0 = " + this.seeds.seedMoisture_0 + "\n");
         var3.write("seeds.seedMoisture_1 = " + this.seeds.seedMoisture_1 + "\n");
         var3.write("seeds.seedMoisture_2 = " + this.seeds.seedMoisture_2 + "\n");
         var3.write("seeds.seedMinerals_0 = " + this.seeds.seedMinerals_0 + "\n");
         var3.write("seeds.seedMinerals_1 = " + this.seeds.seedMinerals_1 + "\n");
         var3.write("seeds.seedMinerals_2 = " + this.seeds.seedMinerals_2 + "\n");
         var3.write("seeds.seedKudzu_0 = " + this.seeds.seedKudzu_0 + "\n");
         var3.write("seeds.seedKudzu_1 = " + this.seeds.seedKudzu_1 + "\n");
         var3.write("seeds.seedKudzu_2 = " + this.seeds.seedKudzu_2 + "\n");
         var3.write("\n");
         var3.write("time.tickunit = " + this.time.tickunit + "\n");
         var3.write("time.ticks = " + this.time.ticks + "\n");
         var3.write("time.eticks = " + this.time.eticks + "\n");
         var3.write("time.epoch = " + this.time.epoch + "\n");
         var3.write("\n");
         var3.write("season.lat = " + this.season.lat + "\n");
         var3.write("season.tempMax = " + this.season.tempMax + "\n");
         var3.write("season.tempMin = " + this.season.tempMin + "\n");
         var3.write("season.tempDiff = " + this.season.tempDiff + "\n");
         var3.write("season.seasonLag = " + this.season.seasonLag + "\n");
         var3.write("season.noon = " + this.season.noon + "\n");
         var3.write("season.seedA = " + this.season.seedA + "\n");
         var3.write("season.seedB = " + this.season.seedB + "\n");
         var3.write("season.seedC = " + this.season.seedC + "\n");
         var3.write("season.jan = " + this.season.jan + "\n");
         var3.write("season.feb = " + this.season.feb + "\n");
         var3.write("season.mar = " + this.season.mar + "\n");
         var3.write("season.apr = " + this.season.apr + "\n");
         var3.write("season.may = " + this.season.may + "\n");
         var3.write("season.jun = " + this.season.jun + "\n");
         var3.write("season.jul = " + this.season.jul + "\n");
         var3.write("season.aug = " + this.season.aug + "\n");
         var3.write("season.sep = " + this.season.sep + "\n");
         var3.write("season.oct = " + this.season.oct + "\n");
         var3.write("season.nov = " + this.season.nov + "\n");
         var3.write("season.dec = " + this.season.dec + "\n");
         var3.write("\n");
         var3.write("debug.enabled = " + this.debug.enabled + "\n");
         var3.write("debug.startday = " + this.debug.startday + "\n");
         var3.write("debug.startmonth = " + this.debug.startmonth + "\n");
         var3.close();
      } catch (Exception var4) {
         var4.printStackTrace();
      }

   }

   public boolean readFile(String var1) {
      try {
         File var2 = new File(var1);
         if (!var2.exists()) {
            return false;
         } else {
            BufferedReader var3 = new BufferedReader(new FileReader(var2));

            while(true) {
               String var4 = var3.readLine();
               if (var4 == null) {
                  var3.close();
                  return true;
               }

               if (!var4.trim().startsWith("--")) {
                  if (!var4.contains("=")) {
                     if (!var4.trim().isEmpty()) {
                        DebugLog.log("ErosionConfig: unknown \"" + var4 + "\"");
                     }
                  } else {
                     String[] var5 = var4.split("=");
                     if (var5.length != 2) {
                        DebugLog.log("ErosionConfig: unknown \"" + var4 + "\"");
                     } else {
                        String var6 = var5[0].trim();
                        String var7 = var5[1].trim();
                        if (var6.startsWith("seeds.")) {
                           if ("seeds.seedMain_0".equals(var6)) {
                              this.seeds.seedMain_0 = Integer.parseInt(var7);
                           } else if ("seeds.seedMain_1".equals(var6)) {
                              this.seeds.seedMain_1 = Integer.parseInt(var7);
                           } else if ("seeds.seedMain_2".equals(var6)) {
                              this.seeds.seedMain_2 = Integer.parseInt(var7);
                           } else if ("seeds.seedMoisture_0".equals(var6)) {
                              this.seeds.seedMoisture_0 = Integer.parseInt(var7);
                           } else if ("seeds.seedMoisture_1".equals(var6)) {
                              this.seeds.seedMoisture_1 = Integer.parseInt(var7);
                           } else if ("seeds.seedMoisture_2".equals(var6)) {
                              this.seeds.seedMoisture_2 = Integer.parseInt(var7);
                           } else if ("seeds.seedMinerals_0".equals(var6)) {
                              this.seeds.seedMinerals_0 = Integer.parseInt(var7);
                           } else if ("seeds.seedMinerals_1".equals(var6)) {
                              this.seeds.seedMinerals_1 = Integer.parseInt(var7);
                           } else if ("seeds.seedMinerals_2".equals(var6)) {
                              this.seeds.seedMinerals_2 = Integer.parseInt(var7);
                           } else if ("seeds.seedKudzu_0".equals(var6)) {
                              this.seeds.seedKudzu_0 = Integer.parseInt(var7);
                           } else if ("seeds.seedKudzu_1".equals(var6)) {
                              this.seeds.seedKudzu_1 = Integer.parseInt(var7);
                           } else if ("seeds.seedKudzu_2".equals(var6)) {
                              this.seeds.seedKudzu_2 = Integer.parseInt(var7);
                           } else {
                              DebugLog.log("ErosionConfig: unknown \"" + var4 + "\"");
                           }
                        } else if (var6.startsWith("time.")) {
                           if ("time.tickunit".equals(var6)) {
                              this.time.tickunit = Integer.parseInt(var7);
                           } else if ("time.ticks".equals(var6)) {
                              this.time.ticks = Integer.parseInt(var7);
                           } else if ("time.eticks".equals(var6)) {
                              this.time.eticks = Integer.parseInt(var7);
                           } else if ("time.epoch".equals(var6)) {
                              this.time.epoch = Integer.parseInt(var7);
                           } else {
                              DebugLog.log("ErosionConfig: unknown \"" + var4 + "\"");
                           }
                        } else if (var6.startsWith("season.")) {
                           if ("season.lat".equals(var6)) {
                              this.season.lat = Integer.parseInt(var7);
                           } else if ("season.tempMax".equals(var6)) {
                              this.season.tempMax = Integer.parseInt(var7);
                           } else if ("season.tempMin".equals(var6)) {
                              this.season.tempMin = Integer.parseInt(var7);
                           } else if ("season.tempDiff".equals(var6)) {
                              this.season.tempDiff = Integer.parseInt(var7);
                           } else if ("season.seasonLag".equals(var6)) {
                              this.season.seasonLag = Integer.parseInt(var7);
                           } else if ("season.noon".equals(var6)) {
                              this.season.noon = Float.parseFloat(var7);
                           } else if ("season.seedA".equals(var6)) {
                              this.season.seedA = Integer.parseInt(var7);
                           } else if ("season.seedB".equals(var6)) {
                              this.season.seedB = Integer.parseInt(var7);
                           } else if ("season.seedC".equals(var6)) {
                              this.season.seedC = Integer.parseInt(var7);
                           } else if ("season.jan".equals(var6)) {
                              this.season.jan = Float.parseFloat(var7);
                           } else if ("season.feb".equals(var6)) {
                              this.season.feb = Float.parseFloat(var7);
                           } else if ("season.mar".equals(var6)) {
                              this.season.mar = Float.parseFloat(var7);
                           } else if ("season.apr".equals(var6)) {
                              this.season.apr = Float.parseFloat(var7);
                           } else if ("season.may".equals(var6)) {
                              this.season.may = Float.parseFloat(var7);
                           } else if ("season.jun".equals(var6)) {
                              this.season.jun = Float.parseFloat(var7);
                           } else if ("season.jul".equals(var6)) {
                              this.season.jul = Float.parseFloat(var7);
                           } else if ("season.aug".equals(var6)) {
                              this.season.aug = Float.parseFloat(var7);
                           } else if ("season.sep".equals(var6)) {
                              this.season.sep = Float.parseFloat(var7);
                           } else if ("season.oct".equals(var6)) {
                              this.season.oct = Float.parseFloat(var7);
                           } else if ("season.nov".equals(var6)) {
                              this.season.nov = Float.parseFloat(var7);
                           } else if ("season.dec".equals(var6)) {
                              this.season.dec = Float.parseFloat(var7);
                           } else {
                              DebugLog.log("ErosionConfig: unknown \"" + var4 + "\"");
                           }
                        } else if (var6.startsWith("debug.")) {
                           if ("debug.enabled".equals(var6)) {
                              this.debug.enabled = Boolean.parseBoolean(var7);
                           } else if ("debug.startday".equals(var6)) {
                              this.debug.startday = Integer.parseInt(var7);
                           } else if ("debug.startmonth".equals(var6)) {
                              this.debug.startmonth = Integer.parseInt(var7);
                           }
                        } else {
                           DebugLog.log("ErosionConfig: unknown \"" + var4 + "\"");
                        }
                     }
                  }
               }
            }
         }
      } catch (Exception var8) {
         var8.printStackTrace();
         return false;
      }
   }

   public ErosionConfig.Debug getDebug() {
      return this.debug;
   }

   public void consolePrint() {
   }

   public static final class Seeds {
      int seedMain_0 = 16;
      int seedMain_1 = 32;
      int seedMain_2 = 64;
      int seedMoisture_0 = 96;
      int seedMoisture_1 = 128;
      int seedMoisture_2 = 144;
      int seedMinerals_0 = 196;
      int seedMinerals_1 = 255;
      int seedMinerals_2 = 0;
      int seedKudzu_0 = 200;
      int seedKudzu_1 = 125;
      int seedKudzu_2 = 50;
   }

   public static final class Time {
      int tickunit = 144;
      int ticks = 0;
      int eticks = 0;
      int epoch = 0;
   }

   public static final class Debug {
      boolean enabled = false;
      int startday = 26;
      int startmonth = 11;

      public boolean getEnabled() {
         return this.enabled;
      }

      public int getStartDay() {
         return this.startday;
      }

      public int getStartMonth() {
         return this.startmonth;
      }
   }

   public static final class Season {
      int lat = 38;
      int tempMax = 25;
      int tempMin = 0;
      int tempDiff = 7;
      int seasonLag = 31;
      float noon = 12.5F;
      int seedA = 64;
      int seedB = 128;
      int seedC = 255;
      float jan = 0.39F;
      float feb = 0.35F;
      float mar = 0.39F;
      float apr = 0.4F;
      float may = 0.35F;
      float jun = 0.37F;
      float jul = 0.29F;
      float aug = 0.26F;
      float sep = 0.23F;
      float oct = 0.23F;
      float nov = 0.3F;
      float dec = 0.32F;
   }
}

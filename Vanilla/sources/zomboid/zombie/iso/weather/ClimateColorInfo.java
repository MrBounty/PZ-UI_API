package zombie.iso.weather;

import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.Date;
import zombie.ZomboidFileSystem;
import zombie.core.Color;
import zombie.debug.DebugLog;

public class ClimateColorInfo {
   private Color interior;
   private Color exterior;
   private static BufferedWriter writer;

   public ClimateColorInfo() {
      this.interior = new Color(0, 0, 0, 1);
      this.exterior = new Color(0, 0, 0, 1);
   }

   public ClimateColorInfo(float var1, float var2, float var3, float var4) {
      this(var1, var2, var3, var4, var1, var2, var3, var4);
   }

   public ClimateColorInfo(float var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8) {
      this.interior = new Color(0, 0, 0, 1);
      this.exterior = new Color(0, 0, 0, 1);
      this.interior.r = var1;
      this.interior.g = var2;
      this.interior.b = var3;
      this.interior.a = var4;
      this.exterior.r = var5;
      this.exterior.g = var6;
      this.exterior.b = var7;
      this.exterior.a = var8;
   }

   public void setInterior(Color var1) {
      this.interior.set(var1);
   }

   public void setInterior(float var1, float var2, float var3, float var4) {
      this.interior.r = var1;
      this.interior.g = var2;
      this.interior.b = var3;
      this.interior.a = var4;
   }

   public Color getInterior() {
      return this.interior;
   }

   public void setExterior(Color var1) {
      this.exterior.set(var1);
   }

   public void setExterior(float var1, float var2, float var3, float var4) {
      this.exterior.r = var1;
      this.exterior.g = var2;
      this.exterior.b = var3;
      this.exterior.a = var4;
   }

   public Color getExterior() {
      return this.exterior;
   }

   public void setTo(ClimateColorInfo var1) {
      this.interior.set(var1.interior);
      this.exterior.set(var1.exterior);
   }

   public ClimateColorInfo interp(ClimateColorInfo var1, float var2, ClimateColorInfo var3) {
      this.interior.interp(var1.interior, var2, var3.interior);
      this.exterior.interp(var1.exterior, var2, var3.exterior);
      return var3;
   }

   public void scale(float var1) {
      this.interior.scale(var1);
      this.exterior.scale(var1);
   }

   public static ClimateColorInfo interp(ClimateColorInfo var0, ClimateColorInfo var1, float var2, ClimateColorInfo var3) {
      return var0.interp(var1, var2, var3);
   }

   public void write(ByteBuffer var1) {
      var1.putFloat(this.interior.r);
      var1.putFloat(this.interior.g);
      var1.putFloat(this.interior.b);
      var1.putFloat(this.interior.a);
      var1.putFloat(this.exterior.r);
      var1.putFloat(this.exterior.g);
      var1.putFloat(this.exterior.b);
      var1.putFloat(this.exterior.a);
   }

   public void read(ByteBuffer var1) {
      this.interior.r = var1.getFloat();
      this.interior.g = var1.getFloat();
      this.interior.b = var1.getFloat();
      this.interior.a = var1.getFloat();
      this.exterior.r = var1.getFloat();
      this.exterior.g = var1.getFloat();
      this.exterior.b = var1.getFloat();
      this.exterior.a = var1.getFloat();
   }

   public void save(DataOutputStream var1) throws IOException {
      var1.writeFloat(this.interior.r);
      var1.writeFloat(this.interior.g);
      var1.writeFloat(this.interior.b);
      var1.writeFloat(this.interior.a);
      var1.writeFloat(this.exterior.r);
      var1.writeFloat(this.exterior.g);
      var1.writeFloat(this.exterior.b);
      var1.writeFloat(this.exterior.a);
   }

   public void load(DataInputStream var1, int var2) throws IOException {
      this.interior.r = var1.readFloat();
      this.interior.g = var1.readFloat();
      this.interior.b = var1.readFloat();
      this.interior.a = var1.readFloat();
      this.exterior.r = var1.readFloat();
      this.exterior.g = var1.readFloat();
      this.exterior.b = var1.readFloat();
      this.exterior.a = var1.readFloat();
   }

   public static boolean writeColorInfoConfig() {
      boolean var0 = false;

      try {
         String var1 = (new SimpleDateFormat("yyyyMMdd_HHmmss")).format(new Date());
         String var2 = "ClimateMain_" + var1;
         String var10000 = ZomboidFileSystem.instance.getCacheDir();
         String var3 = var10000 + File.separator + var2 + ".lua";
         DebugLog.log("Attempting to save color config to: " + var3);
         File var4 = new File(var3);

         try {
            BufferedWriter var5 = new BufferedWriter(new FileWriter(var4, false));

            try {
               writer = var5;
               ClimateManager var6 = ClimateManager.getInstance();
               write("--[[");
               write("-- Generated file (" + var2 + ")");
               write("-- Climate color configuration");
               write("-- File should be placed in: media/lua/server/Climate/ClimateMain.lua (remove date stamp)");
               write("--]]");
               var5.newLine();
               write("ClimateMain = {};");
               write("ClimateMain.versionStamp = \"" + var1 + "\";");
               var5.newLine();
               write("local WARM,NORMAL,CLOUDY = 0,1,2;");
               var5.newLine();
               write("local SUMMER,FALL,WINTER,SPRING = 0,1,2,3;");
               var5.newLine();
               write("function ClimateMain.onClimateManagerInit(_clim)");
               byte var7 = 1;
               write(var7, "local c;");
               write(var7, "c = _clim:getColNightNoMoon();");
               writeColor(var7, var6.getColNightNoMoon());
               var5.newLine();
               write(var7, "c = _clim:getColNightMoon();");
               writeColor(var7, var6.getColNightMoon());
               var5.newLine();
               write(var7, "c = _clim:getColFog();");
               writeColor(var7, var6.getColFog());
               var5.newLine();
               write(var7, "c = _clim:getColFogLegacy();");
               writeColor(var7, var6.getColFogLegacy());
               var5.newLine();
               write(var7, "c = _clim:getColFogNew();");
               writeColor(var7, var6.getColFogNew());
               var5.newLine();
               write(var7, "c = _clim:getFogTintStorm();");
               writeColor(var7, var6.getFogTintStorm());
               var5.newLine();
               write(var7, "c = _clim:getFogTintTropical();");
               writeColor(var7, var6.getFogTintTropical());
               var5.newLine();
               WeatherPeriod var8 = var6.getWeatherPeriod();
               write(var7, "local w = _clim:getWeatherPeriod();");
               var5.newLine();
               write(var7, "c = w:getCloudColorReddish();");
               writeColor(var7, var8.getCloudColorReddish());
               var5.newLine();
               write(var7, "c = w:getCloudColorGreenish();");
               writeColor(var7, var8.getCloudColorGreenish());
               var5.newLine();
               write(var7, "c = w:getCloudColorBlueish();");
               writeColor(var7, var8.getCloudColorBlueish());
               var5.newLine();
               write(var7, "c = w:getCloudColorPurplish();");
               writeColor(var7, var8.getCloudColorPurplish());
               var5.newLine();
               write(var7, "c = w:getCloudColorTropical();");
               writeColor(var7, var8.getCloudColorTropical());
               var5.newLine();
               write(var7, "c = w:getCloudColorBlizzard();");
               writeColor(var7, var8.getCloudColorBlizzard());
               var5.newLine();
               String[] var9 = new String[]{"Dawn", "Day", "Dusk"};
               String[] var10 = new String[]{"SUMMER", "FALL", "WINTER", "SPRING"};
               String[] var11 = new String[]{"WARM", "NORMAL", "CLOUDY"};

               for(int var13 = 0; var13 < 3; ++var13) {
                  write(var7, "-- ###################### " + var9[var13] + " ######################");

                  for(int var14 = 0; var14 < 4; ++var14) {
                     for(int var15 = 0; var15 < 3; ++var15) {
                        if (var15 == 0 || var15 == 2 || var15 == 1 && var13 == 2) {
                           ClimateColorInfo var12 = var6.getSeasonColor(var13, var15, var14);
                           writeSeasonColor(var7, var12, var9[var13], var10[var14], var11[var15]);
                           var5.newLine();
                        }
                     }
                  }
               }

               write("end");
               var5.newLine();
               write("Events.OnClimateManagerInit.Add(ClimateMain.onClimateManagerInit);");
               writer = null;
               var5.flush();
               var5.close();
            } catch (Throwable var17) {
               try {
                  var5.close();
               } catch (Throwable var16) {
                  var17.addSuppressed(var16);
               }

               throw var17;
            }

            var5.close();
         } catch (Exception var18) {
            var18.printStackTrace();
         }
      } catch (Exception var19) {
         var19.printStackTrace();
      }

      return var0;
   }

   private static void writeSeasonColor(int var0, ClimateColorInfo var1, String var2, String var3, String var4) throws IOException {
      Color var5 = var1.exterior;
      write(var0, "_clim:setSeasonColor" + var2 + "(" + var4 + "," + var3 + "," + var5.r + "," + var5.g + "," + var5.b + "," + var5.a + ",true);\t\t--exterior");
      var5 = var1.interior;
      write(var0, "_clim:setSeasonColor" + var2 + "(" + var4 + "," + var3 + "," + var5.r + "," + var5.g + "," + var5.b + "," + var5.a + ",false);\t\t--interior");
   }

   private static void writeColor(int var0, ClimateColorInfo var1) throws IOException {
      Color var2 = var1.exterior;
      write(var0, "c:setExterior(" + var2.r + "," + var2.g + "," + var2.b + "," + var2.a + ");");
      var2 = var1.interior;
      write(var0, "c:setInterior(" + var2.r + "," + var2.g + "," + var2.b + "," + var2.a + ");");
   }

   private static void write(int var0, String var1) throws IOException {
      String var2 = (new String(new char[var0])).replace("\u0000", "\t");
      writer.write(var2);
      writer.write(var1);
      writer.newLine();
   }

   private static void write(String var0) throws IOException {
      writer.write(var0);
      writer.newLine();
   }
}

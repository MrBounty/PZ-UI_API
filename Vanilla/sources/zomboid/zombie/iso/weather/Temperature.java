package zombie.iso.weather;

import zombie.SandboxOptions;
import zombie.characters.IsoPlayer;
import zombie.core.Color;
import zombie.core.Core;

public class Temperature {
   public static boolean DO_DEFAULT_BASE = false;
   public static boolean DO_DAYLEN_MOD = true;
   public static String CELSIUS_POSTFIX = "°C";
   public static String FAHRENHEIT_POSTFIX = "°F";
   public static final float skinCelciusMin = 20.0F;
   public static final float skinCelciusFavorable = 33.0F;
   public static final float skinCelciusMax = 42.0F;
   public static final float homeostasisDefault = 37.0F;
   public static final float FavorableNakedTemp = 27.0F;
   public static final float FavorableRoomTemp = 22.0F;
   public static final float coreCelciusMin = 20.0F;
   public static final float coreCelciusMax = 42.0F;
   public static final float neutralZone = 27.0F;
   public static final float Hypothermia_1 = 36.5F;
   public static final float Hypothermia_2 = 35.0F;
   public static final float Hypothermia_3 = 30.0F;
   public static final float Hypothermia_4 = 25.0F;
   public static final float Hyperthermia_1 = 37.5F;
   public static final float Hyperthermia_2 = 39.0F;
   public static final float Hyperthermia_3 = 40.0F;
   public static final float Hyperthermia_4 = 41.0F;
   public static final float TrueInsulationMultiplier = 2.0F;
   public static final float TrueWindresistMultiplier = 1.0F;
   public static final float BodyMinTemp = 20.0F;
   public static final float BodyMaxTemp = 42.0F;
   private static String cacheTempString = "";
   private static float cacheTemp = -9000.0F;
   private static Color tempColor = new Color(1.0F, 1.0F, 1.0F, 1.0F);
   private static Color col_0 = new Color(29, 34, 237);
   private static Color col_25 = new Color(0, 255, 234);
   private static Color col_50 = new Color(84, 255, 55);
   private static Color col_75 = new Color(255, 246, 0);
   private static Color col_100 = new Color(255, 0, 0);

   public static String getCelsiusPostfix() {
      return CELSIUS_POSTFIX;
   }

   public static String getFahrenheitPostfix() {
      return FAHRENHEIT_POSTFIX;
   }

   public static String getTemperaturePostfix() {
      return Core.OptionTemperatureDisplayCelsius ? CELSIUS_POSTFIX : FAHRENHEIT_POSTFIX;
   }

   public static String getTemperatureString(float var0) {
      float var1 = Core.OptionTemperatureDisplayCelsius ? var0 : CelsiusToFahrenheit(var0);
      var1 = (float)Math.round(var1 * 10.0F) / 10.0F;
      if (cacheTemp != var1) {
         cacheTemp = var1;
         cacheTempString = var1 + " " + getTemperaturePostfix();
      }

      return cacheTempString;
   }

   public static float CelsiusToFahrenheit(float var0) {
      return var0 * 1.8F + 32.0F;
   }

   public static float FahrenheitToCelsius(float var0) {
      return (var0 - 32.0F) / 1.8F;
   }

   public static float WindchillCelsiusKph(float var0, float var1) {
      float var2 = 13.12F + 0.6215F * var0 - 11.37F * (float)Math.pow((double)var1, 0.1599999964237213D) + 0.3965F * var0 * (float)Math.pow((double)var1, 0.1599999964237213D);
      return var2 < var0 ? var2 : var0;
   }

   public static float getTrueInsulationValue(float var0) {
      return var0 * 2.0F + 0.5F * var0 * var0 * var0;
   }

   public static float getTrueWindresistanceValue(float var0) {
      return var0 * 1.0F + 0.5F * var0 * var0;
   }

   public static void reset() {
   }

   public static float getFractionForRealTimeRatePerMin(float var0) {
      if (DO_DEFAULT_BASE) {
         return var0 / (1440.0F / (float)SandboxOptions.instance.getDayLengthMinutesDefault());
      } else if (!DO_DAYLEN_MOD) {
         return var0 / (1440.0F / (float)SandboxOptions.instance.getDayLengthMinutes());
      } else {
         float var1 = (float)SandboxOptions.instance.getDayLengthMinutes() / (float)SandboxOptions.instance.getDayLengthMinutesDefault();
         if (var1 < 1.0F) {
            var1 = 0.5F + 0.5F * var1;
         } else if (var1 > 1.0F) {
            var1 = 1.0F + var1 / 16.0F;
         }

         return var0 / (1440.0F / (float)SandboxOptions.instance.getDayLengthMinutes()) * var1;
      }
   }

   public static Color getValueColor(float var0) {
      var0 = ClimateManager.clamp(0.0F, 1.0F, var0);
      tempColor.set(0.0F, 0.0F, 0.0F, 1.0F);
      float var1 = 0.0F;
      if (var0 < 0.25F) {
         var1 = var0 / 0.25F;
         col_0.interp(col_25, var1, tempColor);
      } else if (var0 < 0.5F) {
         var1 = (var0 - 0.25F) / 0.25F;
         col_25.interp(col_50, var1, tempColor);
      } else if (var0 < 0.75F) {
         var1 = (var0 - 0.5F) / 0.25F;
         col_50.interp(col_75, var1, tempColor);
      } else {
         var1 = (var0 - 0.75F) / 0.25F;
         col_75.interp(col_100, var1, tempColor);
      }

      return tempColor;
   }

   public static float getWindChillAmountForPlayer(IsoPlayer var0) {
      if (var0.getVehicle() == null && (var0.getSquare() == null || !var0.getSquare().isInARoom())) {
         ClimateManager var1 = ClimateManager.getInstance();
         float var2 = var1.getAirTemperatureForCharacter(var0, true);
         float var3 = 0.0F;
         if (var2 < var1.getTemperature()) {
            var3 = var1.getTemperature() - var2;
         }

         return var3;
      } else {
         return 0.0F;
      }
   }
}

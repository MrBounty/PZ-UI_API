package zombie.audio.parameters;

import zombie.audio.FMODGlobalParameter;
import zombie.iso.weather.ClimateManager;
import zombie.iso.weather.WeatherPeriod;

public final class ParameterStorm extends FMODGlobalParameter {
   public ParameterStorm() {
      super("Storm");
   }

   public float calculateCurrentValue() {
      WeatherPeriod var1 = ClimateManager.getInstance().getWeatherPeriod();
      if (var1.isRunning()) {
         if (var1.isThunderStorm()) {
            return 1.0F;
         }

         if (var1.isTropicalStorm()) {
            return 2.0F;
         }

         if (var1.isBlizzard()) {
            return 3.0F;
         }
      }

      return 0.0F;
   }
}

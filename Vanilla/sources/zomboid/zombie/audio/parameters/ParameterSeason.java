package zombie.audio.parameters;

import zombie.audio.FMODGlobalParameter;
import zombie.iso.weather.ClimateManager;

public final class ParameterSeason extends FMODGlobalParameter {
   public ParameterSeason() {
      super("Season");
   }

   public float calculateCurrentValue() {
      ClimateManager.DayInfo var1 = ClimateManager.getInstance().getCurrentDay();
      if (var1 == null) {
         return 0.0F;
      } else {
         float var10000;
         switch(var1.season.getSeason()) {
         case 1:
            var10000 = 0.0F;
            break;
         case 2:
         case 3:
            var10000 = 1.0F;
            break;
         case 4:
            var10000 = 2.0F;
            break;
         case 5:
            var10000 = 3.0F;
            break;
         default:
            var10000 = 1.0F;
         }

         return var10000;
      }
   }
}

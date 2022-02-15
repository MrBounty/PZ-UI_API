package zombie.audio.parameters;

import zombie.GameTime;
import zombie.audio.FMODGlobalParameter;
import zombie.iso.weather.ClimateManager;

public final class ParameterTimeOfDay extends FMODGlobalParameter {
   public ParameterTimeOfDay() {
      super("TimeOfDay");
   }

   public float calculateCurrentValue() {
      ClimateManager.DayInfo var1 = ClimateManager.getInstance().getCurrentDay();
      if (var1 == null) {
         return 1.0F;
      } else {
         float var2 = var1.season.getDawn();
         float var3 = var1.season.getDusk();
         float var4 = var1.season.getDayHighNoon();
         float var5 = GameTime.instance.getTimeOfDay();
         if (var5 >= var2 - 1.0F && var5 < var2 + 1.0F) {
            return 0.0F;
         } else if (var5 >= var2 + 1.0F && var5 < var2 + 2.0F) {
            return 1.0F;
         } else if (var5 >= var2 + 2.0F && var5 < var3 - 2.0F) {
            return 2.0F;
         } else if (var5 >= var3 - 2.0F && var5 < var3 - 1.0F) {
            return 3.0F;
         } else {
            return var5 >= var3 - 1.0F && var5 < var3 + 1.0F ? 4.0F : 5.0F;
         }
      }
   }
}

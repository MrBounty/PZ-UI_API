package zombie.audio.parameters;

import zombie.audio.FMODGlobalParameter;
import zombie.iso.weather.ClimateManager;

public final class ParameterTemperature extends FMODGlobalParameter {
   public ParameterTemperature() {
      super("Temperature");
   }

   public float calculateCurrentValue() {
      return (float)((int)(ClimateManager.getInstance().getTemperature() * 100.0F)) / 100.0F;
   }
}

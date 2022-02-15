package zombie.audio.parameters;

import zombie.audio.FMODGlobalParameter;
import zombie.iso.weather.ClimateManager;

public final class ParameterRainIntensity extends FMODGlobalParameter {
   public ParameterRainIntensity() {
      super("RainIntensity");
   }

   public float calculateCurrentValue() {
      return ClimateManager.getInstance().isRaining() ? ClimateManager.getInstance().getPrecipitationIntensity() : 0.0F;
   }
}

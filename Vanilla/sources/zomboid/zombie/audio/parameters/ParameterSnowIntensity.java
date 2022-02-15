package zombie.audio.parameters;

import zombie.audio.FMODGlobalParameter;
import zombie.iso.weather.ClimateManager;

public final class ParameterSnowIntensity extends FMODGlobalParameter {
   public ParameterSnowIntensity() {
      super("SnowIntensity");
   }

   public float calculateCurrentValue() {
      return ClimateManager.getInstance().isSnowing() ? ClimateManager.getInstance().getPrecipitationIntensity() : 0.0F;
   }
}

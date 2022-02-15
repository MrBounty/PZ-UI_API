package zombie.audio.parameters;

import zombie.audio.FMODGlobalParameter;
import zombie.iso.weather.ClimateManager;

public final class ParameterWindIntensity extends FMODGlobalParameter {
   public ParameterWindIntensity() {
      super("WindIntensity");
   }

   public float calculateCurrentValue() {
      float var1 = ClimateManager.getInstance().getWindIntensity();
      return (float)((int)(var1 * 1000.0F)) / 1000.0F;
   }
}

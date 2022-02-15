package zombie.audio.parameters;

import zombie.SandboxOptions;
import zombie.audio.FMODGlobalParameter;
import zombie.iso.weather.ClimateManager;

public final class ParameterWeatherEvent extends FMODGlobalParameter {
   private ParameterWeatherEvent.Event event;

   public ParameterWeatherEvent() {
      super("WeatherEvent");
      this.event = ParameterWeatherEvent.Event.None;
   }

   public float calculateCurrentValue() {
      float var1 = ClimateManager.getInstance().getSnowFracNow();
      if (!SandboxOptions.instance.EnableSnowOnGround.getValue()) {
         var1 = 0.0F;
      }

      return (float)this.event.value;
   }

   public static enum Event {
      None(0),
      FreshSnow(1);

      final int value;

      private Event(int var3) {
         this.value = var3;
      }

      // $FF: synthetic method
      private static ParameterWeatherEvent.Event[] $values() {
         return new ParameterWeatherEvent.Event[]{None, FreshSnow};
      }
   }
}

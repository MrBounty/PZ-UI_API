package zombie.debug.options;

import zombie.debug.BooleanDebugOption;

public final class Weather extends OptionGroup {
   public final BooleanDebugOption Fx;
   public final BooleanDebugOption Snow;
   public final BooleanDebugOption WaterPuddles;

   public Weather() {
      super("Weather");
      this.Fx = newDebugOnlyOption(this.Group, "Fx", true);
      this.Snow = newDebugOnlyOption(this.Group, "Snow", true);
      this.WaterPuddles = newDebugOnlyOption(this.Group, "WaterPuddles", true);
   }
}

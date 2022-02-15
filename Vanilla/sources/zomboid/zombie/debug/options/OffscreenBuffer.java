package zombie.debug.options;

import zombie.debug.BooleanDebugOption;

public final class OffscreenBuffer extends OptionGroup {
   public final BooleanDebugOption Render;

   public OffscreenBuffer() {
      super("OffscreenBuffer");
      this.Render = newDebugOnlyOption(this.Group, "Render", true);
   }
}

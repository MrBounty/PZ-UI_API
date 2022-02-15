package zombie.debug.options;

import zombie.debug.BooleanDebugOption;

public final class Animation extends OptionGroup {
   public final BooleanDebugOption Debug;
   public final BooleanDebugOption AllowEarlyTransitionOut;
   public final Animation.AnimLayerOG AnimLayer;
   public final Animation.SharedSkelesOG SharedSkeles;
   public final BooleanDebugOption AnimRenderPicker;
   public final BooleanDebugOption BlendUseFbx;

   public Animation() {
      super("Animation");
      this.Debug = newDebugOnlyOption(this.Group, "Debug", false);
      this.AllowEarlyTransitionOut = newDebugOnlyOption(this.Group, "AllowEarlyTransitionOut", true);
      this.AnimLayer = new Animation.AnimLayerOG(this.Group);
      this.SharedSkeles = new Animation.SharedSkelesOG(this.Group);
      this.AnimRenderPicker = newDebugOnlyOption(this.Group, "Render.Picker", false);
      this.BlendUseFbx = newDebugOnlyOption(this.Group, "BlendUseFbx", false);
   }

   public static final class AnimLayerOG extends OptionGroup {
      public final BooleanDebugOption LogStateChanges;
      public final BooleanDebugOption AllowAnimNodeOverride;

      AnimLayerOG(IDebugOptionGroup var1) {
         super(var1, "AnimLayer");
         this.LogStateChanges = newDebugOnlyOption(this.Group, "Debug.LogStateChanges", false);
         this.AllowAnimNodeOverride = newDebugOnlyOption(this.Group, "Debug.AllowAnimNodeOverride", false);
      }
   }

   public static final class SharedSkelesOG extends OptionGroup {
      public final BooleanDebugOption Enabled;
      public final BooleanDebugOption AllowLerping;

      SharedSkelesOG(IDebugOptionGroup var1) {
         super(var1, "SharedSkeles");
         this.Enabled = newDebugOnlyOption(this.Group, "Enabled", true);
         this.AllowLerping = newDebugOnlyOption(this.Group, "AllowLerping", true);
      }
   }
}

package zombie.audio.parameters;

import zombie.audio.FMODLocalParameter;
import zombie.characters.IsoPlayer;

public final class ParameterLocalPlayer extends FMODLocalParameter {
   private final IsoPlayer player;

   public ParameterLocalPlayer(IsoPlayer var1) {
      super("LocalPlayer");
      this.player = var1;
   }

   public float calculateCurrentValue() {
      return this.player.isLocalPlayer() ? 1.0F : 0.0F;
   }
}

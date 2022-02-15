package zombie.audio.parameters;

import zombie.audio.FMODGlobalParameter;
import zombie.characters.IsoPlayer;
import zombie.core.Core;

public final class ParameterCameraZoom extends FMODGlobalParameter {
   public ParameterCameraZoom() {
      super("CameraZoom");
   }

   public float calculateCurrentValue() {
      IsoPlayer var1 = this.getPlayer();
      if (var1 == null) {
         return 0.0F;
      } else {
         float var2 = Core.getInstance().getZoom(var1.PlayerIndex) - Core.getInstance().OffscreenBuffer.getMinZoom();
         float var3 = Core.getInstance().OffscreenBuffer.getMaxZoom() - Core.getInstance().OffscreenBuffer.getMinZoom();
         return var2 / var3;
      }
   }

   private IsoPlayer getPlayer() {
      IsoPlayer var1 = null;

      for(int var2 = 0; var2 < IsoPlayer.numPlayers; ++var2) {
         IsoPlayer var3 = IsoPlayer.players[var2];
         if (var3 != null && (var1 == null || var1.isDead() && var3.isAlive())) {
            var1 = var3;
         }
      }

      return var1;
   }
}

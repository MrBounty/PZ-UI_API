package zombie.audio.parameters;

import fmod.fmod.FMODSoundEmitter;
import zombie.audio.FMODLocalParameter;
import zombie.characters.IsoPlayer;
import zombie.core.math.PZMath;
import zombie.iso.IsoGridSquare;
import zombie.iso.IsoWorld;

public final class ParameterOcclusion extends FMODLocalParameter {
   private final FMODSoundEmitter emitter;
   private float currentValue = Float.NaN;

   public ParameterOcclusion(FMODSoundEmitter var1) {
      super("Occlusion");
      this.emitter = var1;
   }

   public float calculateCurrentValue() {
      float var1 = 1.0F;

      for(int var2 = 0; var2 < 4; ++var2) {
         float var3 = this.calculateValueForPlayer(var2);
         var1 = PZMath.min(var1, var3);
      }

      this.currentValue = var1;
      return (float)((int)(this.currentValue * 1000.0F)) / 1000.0F;
   }

   public void resetToDefault() {
      this.currentValue = Float.NaN;
   }

   private float calculateValueForPlayer(int var1) {
      IsoPlayer var2 = IsoPlayer.players[var1];
      if (var2 == null) {
         return 1.0F;
      } else {
         IsoGridSquare var3 = var2.getCurrentSquare();
         IsoGridSquare var4 = IsoWorld.instance.getCell().getGridSquare((double)this.emitter.x, (double)this.emitter.y, (double)this.emitter.z);
         if (var4 == null) {
            boolean var5 = true;
         }

         float var6 = 0.0F;
         if (var3 != null && var4 != null && !var4.isCouldSee(var1)) {
            var6 = 1.0F;
         }

         return var6;
      }
   }
}

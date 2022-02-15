package zombie.ai;

import zombie.GameTime;
import zombie.iso.IsoUtils;

public final class WalkingOnTheSpot {
   private float x;
   private float y;
   private float time;

   public boolean check(float var1, float var2) {
      if (IsoUtils.DistanceToSquared(this.x, this.y, var1, var2) < 0.010000001F) {
         this.time += GameTime.getInstance().getMultiplier();
      } else {
         this.x = var1;
         this.y = var2;
         this.time = 0.0F;
      }

      return this.time > 400.0F;
   }

   public void reset(float var1, float var2) {
      this.x = var1;
      this.y = var2;
      this.time = 0.0F;
   }
}

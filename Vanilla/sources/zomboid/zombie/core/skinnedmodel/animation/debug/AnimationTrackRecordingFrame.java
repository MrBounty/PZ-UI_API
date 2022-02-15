package zombie.core.skinnedmodel.animation.debug;

import java.util.List;
import zombie.core.skinnedmodel.animation.AnimationTrack;
import zombie.iso.Vector2;

public final class AnimationTrackRecordingFrame extends GenericNameWeightRecordingFrame {
   private Vector2 m_deferredMovement = new Vector2();

   public AnimationTrackRecordingFrame(String var1) {
      super(var1);
   }

   public void reset() {
      super.reset();
      this.m_deferredMovement.set(0.0F, 0.0F);
   }

   public void logAnimWeights(List var1, int[] var2, float[] var3, Vector2 var4) {
      for(int var5 = 0; var5 < var2.length; ++var5) {
         int var6 = var2[var5];
         if (var6 < 0) {
            break;
         }

         float var7 = var3[var5];
         AnimationTrack var8 = (AnimationTrack)var1.get(var6);
         String var9 = var8.name;
         int var10 = var8.getLayerIdx();
         this.logWeight(var9, var10, var7);
      }

      this.m_deferredMovement.set(var4);
   }

   public Vector2 getDeferredMovement() {
      return this.m_deferredMovement;
   }

   public void writeHeader(StringBuilder var1) {
      var1.append(",");
      var1.append("dm.x").append(",").append("dm.y");
      super.writeHeader(var1);
   }

   protected void writeData(StringBuilder var1) {
      var1.append(",");
      var1.append(this.getDeferredMovement().x).append(",").append(this.getDeferredMovement().y);
      super.writeData(var1);
   }
}

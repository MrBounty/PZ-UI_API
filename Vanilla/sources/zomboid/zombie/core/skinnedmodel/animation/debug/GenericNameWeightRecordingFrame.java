package zombie.core.skinnedmodel.animation.debug;

import zombie.util.list.PZArrayUtil;

public class GenericNameWeightRecordingFrame extends GenericNameValueRecordingFrame {
   private float[] m_weights = new float[0];

   public GenericNameWeightRecordingFrame(String var1) {
      super(var1, "_weights");
   }

   protected void onColumnAdded() {
      this.m_weights = PZArrayUtil.add(this.m_weights, 0.0F);
   }

   public void logWeight(String var1, int var2, float var3) {
      int var4 = this.getOrCreateColumn(var1, var2);
      float[] var10000 = this.m_weights;
      var10000[var4] += var3;
   }

   public int getOrCreateColumn(String var1, int var2) {
      String var3 = var2 != 0 ? var2 + ":" : "";
      String var4 = String.format("%s%s", var3, var1);
      int var5 = super.getOrCreateColumn(var4);
      if (this.m_weights[var5] == 0.0F) {
         return var5;
      } else {
         int var6 = 1;

         while(true) {
            String var7 = String.format("%s%s-%d", var3, var1, var6);
            var5 = super.getOrCreateColumn(var7);
            if (this.m_weights[var5] == 0.0F) {
               return var5;
            }

            ++var6;
         }
      }
   }

   public float getWeightAt(int var1) {
      return this.m_weights[var1];
   }

   public String getValueAt(int var1) {
      return String.valueOf(this.getWeightAt(var1));
   }

   public void reset() {
      int var1 = 0;

      for(int var2 = this.m_weights.length; var1 < var2; ++var1) {
         this.m_weights[var1] = 0.0F;
      }

   }
}

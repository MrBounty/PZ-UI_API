package zombie.core.Styles;

import java.nio.FloatBuffer;
import org.lwjgl.util.ReadableColor;

public enum AlphaOp {
   PREMULTIPLY {
      protected int calc(ReadableColor var1, int var2) {
         float var3 = (float)(var1.getAlpha() * var2) * 0.003921569F;
         float var4 = var3 * 0.003921569F;
         return (int)((float)var1.getRed() * var4) << 0 | (int)((float)var1.getGreen() * var4) << 8 | (int)((float)var1.getBlue() * var4) << 16 | (int)var3 << 24;
      }
   },
   KEEP {
      protected int calc(ReadableColor var1, int var2) {
         return var1.getRed() << 0 | var1.getGreen() << 8 | var1.getBlue() << 16 | var1.getAlpha() << 24;
      }
   },
   ZERO {
      protected int calc(ReadableColor var1, int var2) {
         float var3 = (float)(var1.getAlpha() * var2) * 0.003921569F;
         float var4 = var3 * 0.003921569F;
         return (int)((float)var1.getRed() * var4) << 0 | (int)((float)var1.getGreen() * var4) << 8 | (int)((float)var1.getBlue() * var4) << 16;
      }
   };

   private static final float PREMULT_ALPHA = 0.003921569F;

   public final void op(ReadableColor var1, int var2, FloatBuffer var3) {
      var3.put(Float.intBitsToFloat(this.calc(var1, var2)));
   }

   public final void op(int var1, int var2, FloatBuffer var3) {
      var3.put(Float.intBitsToFloat(var1));
   }

   protected abstract int calc(ReadableColor var1, int var2);

   // $FF: synthetic method
   private static AlphaOp[] $values() {
      return new AlphaOp[]{PREMULTIPLY, KEEP, ZERO};
   }
}

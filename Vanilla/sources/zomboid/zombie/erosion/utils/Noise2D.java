package zombie.erosion.utils;

import java.util.ArrayList;

public class Noise2D {
   private ArrayList layers = new ArrayList(3);
   private static final int[] perm = new int[]{151, 160, 137, 91, 90, 15, 131, 13, 201, 95, 96, 53, 194, 233, 7, 225, 140, 36, 103, 30, 69, 142, 8, 99, 37, 240, 21, 10, 23, 190, 6, 148, 247, 120, 234, 75, 0, 26, 197, 62, 94, 252, 219, 203, 117, 35, 11, 32, 57, 177, 33, 88, 237, 149, 56, 87, 174, 20, 125, 136, 171, 168, 68, 175, 74, 165, 71, 134, 139, 48, 27, 166, 77, 146, 158, 231, 83, 111, 229, 122, 60, 211, 133, 230, 220, 105, 92, 41, 55, 46, 245, 40, 244, 102, 143, 54, 65, 25, 63, 161, 1, 216, 80, 73, 209, 76, 132, 187, 208, 89, 18, 169, 200, 196, 135, 130, 116, 188, 159, 86, 164, 100, 109, 198, 173, 186, 3, 64, 52, 217, 226, 250, 124, 123, 5, 202, 38, 147, 118, 126, 255, 82, 85, 212, 207, 206, 59, 227, 47, 16, 58, 17, 182, 189, 28, 42, 223, 183, 170, 213, 119, 248, 152, 2, 44, 154, 163, 70, 221, 153, 101, 155, 167, 43, 172, 9, 129, 22, 39, 253, 19, 98, 108, 110, 79, 113, 224, 232, 178, 185, 112, 104, 218, 246, 97, 228, 251, 34, 242, 193, 238, 210, 144, 12, 191, 179, 162, 241, 81, 51, 145, 235, 249, 14, 239, 107, 49, 192, 214, 31, 181, 199, 106, 157, 184, 84, 204, 176, 115, 121, 50, 45, 127, 4, 150, 254, 138, 236, 205, 93, 222, 114, 67, 29, 24, 72, 243, 141, 128, 195, 78, 66, 215, 61, 156, 180};

   private float lerp(float var1, float var2, float var3) {
      return var2 + var1 * (var3 - var2);
   }

   private float fade(float var1) {
      return var1 * var1 * var1 * (var1 * (var1 * 6.0F - 15.0F) + 10.0F);
   }

   private float noise(float var1, float var2, int[] var3) {
      int var4 = (int)Math.floor((double)var1 - Math.floor((double)(var1 / 255.0F)) * 255.0D);
      int var5 = (int)Math.floor((double)var2 - Math.floor((double)(var2 / 255.0F)) * 255.0D);
      float var6 = this.fade(var1 - (float)Math.floor((double)var1));
      float var7 = this.fade(var2 - (float)Math.floor((double)var2));
      int var8 = var3[var4] + var5;
      int var9 = var3[var4] + var5 + 1;
      int var10 = var3[var4 + 1] + var5;
      int var11 = var3[var4 + 1] + var5 + 1;
      return this.lerp(var7, this.lerp(var6, (float)perm[var3[var8]], (float)perm[var3[var10]]), this.lerp(var6, (float)perm[var3[var9]], (float)perm[var3[var11]]));
   }

   public float layeredNoise(float var1, float var2) {
      float var3 = 0.0F;
      float var4 = 0.0F;

      for(int var5 = 0; var5 < this.layers.size(); ++var5) {
         Noise2D.Layer var6 = (Noise2D.Layer)this.layers.get(var5);
         var4 += var6.amp;
         var3 += this.noise(var1 * var6.freq, var2 * var6.freq, var6.p) * var6.amp;
      }

      return var3 / var4 / 255.0F;
   }

   public void addLayer(int var1, float var2, float var3) {
      int var4 = (int)Math.floor((double)var1 - Math.floor((double)((float)var1 / 256.0F)) * 256.0D);
      Noise2D.Layer var5 = new Noise2D.Layer();
      var5.freq = var2;
      var5.amp = var3;

      for(int var6 = 0; var6 < 256; ++var6) {
         int var7 = (int)Math.floor((double)(var4 + var6) - Math.floor((double)((float)(var4 + var6) / 256.0F)) * 256.0D);
         var5.p[var7] = perm[var6];
         var5.p[256 + var7] = var5.p[var7];
      }

      this.layers.add(var5);
   }

   public void reset() {
      if (this.layers.size() > 0) {
         this.layers.clear();
      }

   }

   private class Layer {
      public float freq;
      public float amp;
      public int[] p = new int[512];
   }
}

package de.jarnbjo.util.audio;

import java.io.IOException;
import javax.sound.sampled.AudioInputStream;

public class FadeableAudioInputStream extends AudioInputStream {
   private AudioInputStream stream;
   private boolean fading = false;
   private double phi = 0.0D;

   public FadeableAudioInputStream(AudioInputStream var1) throws IOException {
      super(var1, var1.getFormat(), -1L);
   }

   public void fadeOut() {
      this.fading = true;
      this.phi = 0.0D;
   }

   public int read(byte[] var1) throws IOException {
      return this.read(var1, 0, var1.length);
   }

   public int read(byte[] var1, int var2, int var3) throws IOException {
      int var4 = super.read(var1, var2, var3);
      if (this.fading) {
         boolean var5 = false;
         boolean var6 = false;
         boolean var7 = false;
         double var8 = 0.0D;

         for(int var10 = var2; var10 < var2 + var4; var10 += 4) {
            int var11 = var10 + 1;
            int var12 = var1[var10] & 255;
            var12 |= var1[var11++] << 8;
            int var13 = var1[var11++] & 255;
            var13 |= var1[var11] << 8;
            if (this.phi < 1.5707963267948966D) {
               this.phi += 1.5E-5D;
            }

            var8 = Math.cos(this.phi);
            var12 = (int)((double)var12 * var8);
            var13 = (int)((double)var13 * var8);
            var11 = var10 + 1;
            var1[var10] = (byte)(var12 & 255);
            var1[var11++] = (byte)(var12 >> 8 & 255);
            var1[var11++] = (byte)(var13 & 255);
            var1[var11++] = (byte)(var13 >> 8 & 255);
         }
      }

      return var4;
   }
}

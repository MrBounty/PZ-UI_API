package zombie.core;

import java.io.Serializable;
import zombie.core.math.PZMath;

public final class Color implements Serializable {
   private static final long serialVersionUID = 1393939L;
   public static final Color transparent = new Color(0.0F, 0.0F, 0.0F, 0.0F);
   public static final Color white = new Color(1.0F, 1.0F, 1.0F, 1.0F);
   public static final Color yellow = new Color(1.0F, 1.0F, 0.0F, 1.0F);
   public static final Color red = new Color(1.0F, 0.0F, 0.0F, 1.0F);
   public static final Color purple = new Color(196.0F, 0.0F, 171.0F);
   public static final Color blue = new Color(0.0F, 0.0F, 1.0F, 1.0F);
   public static final Color green = new Color(0.0F, 1.0F, 0.0F, 1.0F);
   public static final Color black = new Color(0.0F, 0.0F, 0.0F, 1.0F);
   public static final Color gray = new Color(0.5F, 0.5F, 0.5F, 1.0F);
   public static final Color cyan = new Color(0.0F, 1.0F, 1.0F, 1.0F);
   public static final Color darkGray = new Color(0.3F, 0.3F, 0.3F, 1.0F);
   public static final Color lightGray = new Color(0.7F, 0.7F, 0.7F, 1.0F);
   public static final Color pink = new Color(255, 175, 175, 255);
   public static final Color orange = new Color(255, 200, 0, 255);
   public static final Color magenta = new Color(255, 0, 255, 255);
   public static final Color darkGreen = new Color(22, 113, 20, 255);
   public static final Color lightGreen = new Color(55, 148, 53, 255);
   public float a = 1.0F;
   public float b;
   public float g;
   public float r;

   public Color() {
   }

   public Color(Color var1) {
      if (var1 == null) {
         this.r = 0.0F;
         this.g = 0.0F;
         this.b = 0.0F;
         this.a = 1.0F;
      } else {
         this.r = var1.r;
         this.g = var1.g;
         this.b = var1.b;
         this.a = var1.a;
      }
   }

   public Color(float var1, float var2, float var3) {
      this.r = var1;
      this.g = var2;
      this.b = var3;
      this.a = 1.0F;
   }

   public Color(float var1, float var2, float var3, float var4) {
      this.r = PZMath.clamp(var1, 0.0F, 1.0F);
      this.g = PZMath.clamp(var2, 0.0F, 1.0F);
      this.b = PZMath.clamp(var3, 0.0F, 1.0F);
      this.a = PZMath.clamp(var4, 0.0F, 1.0F);
   }

   public Color(Color var1, Color var2, float var3) {
      float var4 = (var2.r - var1.r) * var3;
      float var5 = (var2.g - var1.g) * var3;
      float var6 = (var2.b - var1.b) * var3;
      float var7 = (var2.a - var1.a) * var3;
      this.r = var1.r + var4;
      this.g = var1.g + var5;
      this.b = var1.b + var6;
      this.a = var1.a + var7;
   }

   public void setColor(Color var1, Color var2, float var3) {
      float var4 = (var2.r - var1.r) * var3;
      float var5 = (var2.g - var1.g) * var3;
      float var6 = (var2.b - var1.b) * var3;
      float var7 = (var2.a - var1.a) * var3;
      this.r = var1.r + var4;
      this.g = var1.g + var5;
      this.b = var1.b + var6;
      this.a = var1.a + var7;
   }

   public Color(int var1, int var2, int var3) {
      this.r = (float)var1 / 255.0F;
      this.g = (float)var2 / 255.0F;
      this.b = (float)var3 / 255.0F;
      this.a = 1.0F;
   }

   public Color(int var1, int var2, int var3, int var4) {
      this.r = (float)var1 / 255.0F;
      this.g = (float)var2 / 255.0F;
      this.b = (float)var3 / 255.0F;
      this.a = (float)var4 / 255.0F;
   }

   public Color(int var1) {
      int var2 = (var1 & 16711680) >> 16;
      int var3 = (var1 & '\uff00') >> 8;
      int var4 = var1 & 255;
      int var5 = (var1 & -16777216) >> 24;
      if (var5 < 0) {
         var5 += 256;
      }

      if (var5 == 0) {
         var5 = 255;
      }

      this.r = (float)var4 / 255.0F;
      this.g = (float)var3 / 255.0F;
      this.b = (float)var2 / 255.0F;
      this.a = (float)var5 / 255.0F;
   }

   /** @deprecated */
   @Deprecated
   public void fromColor(int var1) {
      int var2 = (var1 & 16711680) >> 16;
      int var3 = (var1 & '\uff00') >> 8;
      int var4 = var1 & 255;
      int var5 = (var1 & -16777216) >> 24;
      if (var5 < 0) {
         var5 += 256;
      }

      if (var5 == 0) {
         var5 = 255;
      }

      this.r = (float)var4 / 255.0F;
      this.g = (float)var3 / 255.0F;
      this.b = (float)var2 / 255.0F;
      this.a = (float)var5 / 255.0F;
   }

   public void setABGR(int var1) {
      abgrToColor(var1, this);
   }

   public static Color abgrToColor(int var0, Color var1) {
      int var2 = var0 >> 24 & 255;
      int var3 = var0 >> 16 & 255;
      int var4 = var0 >> 8 & 255;
      int var5 = var0 & 255;
      float var6 = 0.003921569F * (float)var5;
      float var7 = 0.003921569F * (float)var4;
      float var8 = 0.003921569F * (float)var3;
      float var9 = 0.003921569F * (float)var2;
      var1.r = var6;
      var1.g = var7;
      var1.b = var8;
      var1.a = var9;
      return var1;
   }

   public static int colorToABGR(Color var0) {
      return colorToABGR(var0.r, var0.g, var0.b, var0.a);
   }

   public static int colorToABGR(float var0, float var1, float var2, float var3) {
      var0 = PZMath.clamp(var0, 0.0F, 1.0F);
      var1 = PZMath.clamp(var1, 0.0F, 1.0F);
      var2 = PZMath.clamp(var2, 0.0F, 1.0F);
      var3 = PZMath.clamp(var3, 0.0F, 1.0F);
      int var4 = (int)(var0 * 255.0F);
      int var5 = (int)(var1 * 255.0F);
      int var6 = (int)(var2 * 255.0F);
      int var7 = (int)(var3 * 255.0F);
      int var8 = (var7 & 255) << 24 | (var6 & 255) << 16 | (var5 & 255) << 8 | var4 & 255;
      return var8;
   }

   public static int multiplyABGR(int var0, int var1) {
      float var2 = getRedChannelFromABGR(var0);
      float var3 = getGreenChannelFromABGR(var0);
      float var4 = getBlueChannelFromABGR(var0);
      float var5 = getAlphaChannelFromABGR(var0);
      float var6 = getRedChannelFromABGR(var1);
      float var7 = getGreenChannelFromABGR(var1);
      float var8 = getBlueChannelFromABGR(var1);
      float var9 = getAlphaChannelFromABGR(var1);
      return colorToABGR(var2 * var6, var3 * var7, var4 * var8, var5 * var9);
   }

   public static int multiplyBGR(int var0, int var1) {
      float var2 = getRedChannelFromABGR(var0);
      float var3 = getGreenChannelFromABGR(var0);
      float var4 = getBlueChannelFromABGR(var0);
      float var5 = getAlphaChannelFromABGR(var0);
      float var6 = getRedChannelFromABGR(var1);
      float var7 = getGreenChannelFromABGR(var1);
      float var8 = getBlueChannelFromABGR(var1);
      return colorToABGR(var2 * var6, var3 * var7, var4 * var8, var5);
   }

   public static int blendBGR(int var0, int var1) {
      float var2 = getRedChannelFromABGR(var0);
      float var3 = getGreenChannelFromABGR(var0);
      float var4 = getBlueChannelFromABGR(var0);
      float var5 = getAlphaChannelFromABGR(var0);
      float var6 = getRedChannelFromABGR(var1);
      float var7 = getGreenChannelFromABGR(var1);
      float var8 = getBlueChannelFromABGR(var1);
      float var9 = getAlphaChannelFromABGR(var1);
      return colorToABGR(var2 * (1.0F - var9) + var6 * var9, var3 * (1.0F - var9) + var7 * var9, var4 * (1.0F - var9) + var8 * var9, var5);
   }

   public static int blendABGR(int var0, int var1) {
      float var2 = getRedChannelFromABGR(var0);
      float var3 = getGreenChannelFromABGR(var0);
      float var4 = getBlueChannelFromABGR(var0);
      float var5 = getAlphaChannelFromABGR(var0);
      float var6 = getRedChannelFromABGR(var1);
      float var7 = getGreenChannelFromABGR(var1);
      float var8 = getBlueChannelFromABGR(var1);
      float var9 = getAlphaChannelFromABGR(var1);
      return colorToABGR(var2 * (1.0F - var9) + var6 * var9, var3 * (1.0F - var9) + var7 * var9, var4 * (1.0F - var9) + var8 * var9, var5 * (1.0F - var9) + var9 * var9);
   }

   public static int tintABGR(int var0, int var1) {
      float var2 = getRedChannelFromABGR(var1);
      float var3 = getGreenChannelFromABGR(var1);
      float var4 = getBlueChannelFromABGR(var1);
      float var5 = getAlphaChannelFromABGR(var1);
      float var6 = getRedChannelFromABGR(var0);
      float var7 = getGreenChannelFromABGR(var0);
      float var8 = getBlueChannelFromABGR(var0);
      float var9 = getAlphaChannelFromABGR(var0);
      return colorToABGR(var2 * var5 + var6 * (1.0F - var5), var3 * var5 + var7 * (1.0F - var5), var4 * var5 + var8 * (1.0F - var5), var9);
   }

   public static int lerpABGR(int var0, int var1, float var2) {
      float var3 = getRedChannelFromABGR(var0);
      float var4 = getGreenChannelFromABGR(var0);
      float var5 = getBlueChannelFromABGR(var0);
      float var6 = getAlphaChannelFromABGR(var0);
      float var7 = getRedChannelFromABGR(var1);
      float var8 = getGreenChannelFromABGR(var1);
      float var9 = getBlueChannelFromABGR(var1);
      float var10 = getAlphaChannelFromABGR(var1);
      return colorToABGR(var3 * (1.0F - var2) + var7 * var2, var4 * (1.0F - var2) + var8 * var2, var5 * (1.0F - var2) + var9 * var2, var6 * (1.0F - var2) + var10 * var2);
   }

   public static float getAlphaChannelFromABGR(int var0) {
      int var1 = var0 >> 24 & 255;
      float var2 = 0.003921569F * (float)var1;
      return var2;
   }

   public static float getBlueChannelFromABGR(int var0) {
      int var1 = var0 >> 16 & 255;
      float var2 = 0.003921569F * (float)var1;
      return var2;
   }

   public static float getGreenChannelFromABGR(int var0) {
      int var1 = var0 >> 8 & 255;
      float var2 = 0.003921569F * (float)var1;
      return var2;
   }

   public static float getRedChannelFromABGR(int var0) {
      int var1 = var0 & 255;
      float var2 = 0.003921569F * (float)var1;
      return var2;
   }

   public static int setAlphaChannelToABGR(int var0, float var1) {
      var1 = PZMath.clamp(var1, 0.0F, 1.0F);
      int var2 = (int)(var1 * 255.0F);
      int var3 = (var2 & 255) << 24 | var0 & 16777215;
      return var3;
   }

   public static int setBlueChannelToABGR(int var0, float var1) {
      var1 = PZMath.clamp(var1, 0.0F, 1.0F);
      int var2 = (int)(var1 * 255.0F);
      int var3 = (var2 & 255) << 16 | var0 & -16711681;
      return var3;
   }

   public static int setGreenChannelToABGR(int var0, float var1) {
      var1 = PZMath.clamp(var1, 0.0F, 1.0F);
      int var2 = (int)(var1 * 255.0F);
      int var3 = (var2 & 255) << 8 | var0 & -65281;
      return var3;
   }

   public static int setRedChannelToABGR(int var0, float var1) {
      var1 = PZMath.clamp(var1, 0.0F, 1.0F);
      int var2 = (int)(var1 * 255.0F);
      int var3 = var2 & 255 | var0 & -256;
      return var3;
   }

   public static Color random() {
      return Colors.GetRandomColor();
   }

   public static Color decode(String var0) {
      return new Color(Integer.decode(var0));
   }

   public void add(Color var1) {
      this.r += var1.r;
      this.g += var1.g;
      this.b += var1.b;
      this.a += var1.a;
   }

   public Color addToCopy(Color var1) {
      Color var2 = new Color(this.r, this.g, this.b, this.a);
      var2.r += var1.r;
      var2.g += var1.g;
      var2.b += var1.b;
      var2.a += var1.a;
      return var2;
   }

   public Color brighter() {
      return this.brighter(0.2F);
   }

   public Color brighter(float var1) {
      this.r = this.r += var1;
      this.g = this.g += var1;
      this.b = this.b += var1;
      return this;
   }

   public Color darker() {
      return this.darker(0.5F);
   }

   public Color darker(float var1) {
      this.r = this.r -= var1;
      this.g = this.g -= var1;
      this.b = this.b -= var1;
      return this;
   }

   public boolean equals(Object var1) {
      if (!(var1 instanceof Color)) {
         return false;
      } else {
         Color var2 = (Color)var1;
         return var2.r == this.r && var2.g == this.g && var2.b == this.b && var2.a == this.a;
      }
   }

   public Color set(Color var1) {
      this.r = var1.r;
      this.g = var1.g;
      this.b = var1.b;
      this.a = var1.a;
      return this;
   }

   public Color set(float var1, float var2, float var3) {
      this.r = var1;
      this.g = var2;
      this.b = var3;
      this.a = 1.0F;
      return this;
   }

   public Color set(float var1, float var2, float var3, float var4) {
      this.r = var1;
      this.g = var2;
      this.b = var3;
      this.a = var4;
      return this;
   }

   public int getAlpha() {
      return (int)(this.a * 255.0F);
   }

   public float getAlphaFloat() {
      return this.a;
   }

   public float getRedFloat() {
      return this.r;
   }

   public float getGreenFloat() {
      return this.g;
   }

   public float getBlueFloat() {
      return this.b;
   }

   public int getAlphaByte() {
      return (int)(this.a * 255.0F);
   }

   public int getBlue() {
      return (int)(this.b * 255.0F);
   }

   public int getBlueByte() {
      return (int)(this.b * 255.0F);
   }

   public int getGreen() {
      return (int)(this.g * 255.0F);
   }

   public int getGreenByte() {
      return (int)(this.g * 255.0F);
   }

   public int getRed() {
      return (int)(this.r * 255.0F);
   }

   public int getRedByte() {
      return (int)(this.r * 255.0F);
   }

   public int hashCode() {
      return (int)(this.r + this.g + this.b + this.a) * 255;
   }

   public Color multiply(Color var1) {
      return new Color(this.r * var1.r, this.g * var1.g, this.b * var1.b, this.a * var1.a);
   }

   public Color scale(float var1) {
      this.r *= var1;
      this.g *= var1;
      this.b *= var1;
      this.a *= var1;
      return this;
   }

   public Color scaleCopy(float var1) {
      Color var2 = new Color(this.r, this.g, this.b, this.a);
      var2.r *= var1;
      var2.g *= var1;
      var2.b *= var1;
      var2.a *= var1;
      return var2;
   }

   public String toString() {
      return "Color (" + this.r + "," + this.g + "," + this.b + "," + this.a + ")";
   }

   public void interp(Color var1, float var2, Color var3) {
      float var4 = var1.r - this.r;
      float var5 = var1.g - this.g;
      float var6 = var1.b - this.b;
      float var7 = var1.a - this.a;
      var4 *= var2;
      var5 *= var2;
      var6 *= var2;
      var7 *= var2;
      var3.r = this.r + var4;
      var3.g = this.g + var5;
      var3.b = this.b + var6;
      var3.a = this.a + var7;
   }

   public void changeHSBValue(float var1, float var2, float var3) {
      float[] var4 = java.awt.Color.RGBtoHSB(this.getRedByte(), this.getGreenByte(), this.getBlueByte(), (float[])null);
      int var5 = java.awt.Color.HSBtoRGB(var4[0] * var1, var4[1] * var2, var4[2] * var3);
      this.r = (float)(var5 >> 16 & 255) / 255.0F;
      this.g = (float)(var5 >> 8 & 255) / 255.0F;
      this.b = (float)(var5 & 255) / 255.0F;
   }

   public static Color HSBtoRGB(float var0, float var1, float var2, Color var3) {
      int var4 = 0;
      int var5 = 0;
      int var6 = 0;
      if (var1 == 0.0F) {
         var4 = var5 = var6 = (int)(var2 * 255.0F + 0.5F);
      } else {
         float var7 = (var0 - (float)Math.floor((double)var0)) * 6.0F;
         float var8 = var7 - (float)Math.floor((double)var7);
         float var9 = var2 * (1.0F - var1);
         float var10 = var2 * (1.0F - var1 * var8);
         float var11 = var2 * (1.0F - var1 * (1.0F - var8));
         switch((int)var7) {
         case 0:
            var4 = (int)(var2 * 255.0F + 0.5F);
            var5 = (int)(var11 * 255.0F + 0.5F);
            var6 = (int)(var9 * 255.0F + 0.5F);
            break;
         case 1:
            var4 = (int)(var10 * 255.0F + 0.5F);
            var5 = (int)(var2 * 255.0F + 0.5F);
            var6 = (int)(var9 * 255.0F + 0.5F);
            break;
         case 2:
            var4 = (int)(var9 * 255.0F + 0.5F);
            var5 = (int)(var2 * 255.0F + 0.5F);
            var6 = (int)(var11 * 255.0F + 0.5F);
            break;
         case 3:
            var4 = (int)(var9 * 255.0F + 0.5F);
            var5 = (int)(var10 * 255.0F + 0.5F);
            var6 = (int)(var2 * 255.0F + 0.5F);
            break;
         case 4:
            var4 = (int)(var11 * 255.0F + 0.5F);
            var5 = (int)(var9 * 255.0F + 0.5F);
            var6 = (int)(var2 * 255.0F + 0.5F);
            break;
         case 5:
            var4 = (int)(var2 * 255.0F + 0.5F);
            var5 = (int)(var9 * 255.0F + 0.5F);
            var6 = (int)(var10 * 255.0F + 0.5F);
         }
      }

      return var3.set((float)var4 / 255.0F, (float)var5 / 255.0F, (float)var6 / 255.0F);
   }

   public static Color HSBtoRGB(float var0, float var1, float var2) {
      return HSBtoRGB(var0, var1, var2, new Color());
   }
}

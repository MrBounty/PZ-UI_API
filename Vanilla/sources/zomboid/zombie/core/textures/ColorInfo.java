package zombie.core.textures;

import zombie.core.Color;
import zombie.core.ImmutableColor;

public final class ColorInfo {
   public float a = 1.0F;
   public float b = 1.0F;
   public float g = 1.0F;
   public float r = 1.0F;

   public ColorInfo() {
      this.r = 1.0F;
      this.g = 1.0F;
      this.b = 1.0F;
      this.a = 1.0F;
   }

   public ColorInfo(float var1, float var2, float var3, float var4) {
      this.r = var1;
      this.g = var2;
      this.b = var3;
      this.a = var4;
   }

   public ColorInfo set(ColorInfo var1) {
      this.r = var1.r;
      this.g = var1.g;
      this.b = var1.b;
      this.a = var1.a;
      return this;
   }

   public ColorInfo set(float var1, float var2, float var3, float var4) {
      this.r = var1;
      this.g = var2;
      this.b = var3;
      this.a = var4;
      return this;
   }

   public float getR() {
      return this.r;
   }

   public float getG() {
      return this.g;
   }

   public float getB() {
      return this.b;
   }

   public Color toColor() {
      return new Color(this.r, this.g, this.b, this.a);
   }

   public ImmutableColor toImmutableColor() {
      return new ImmutableColor(this.r, this.g, this.b, this.a);
   }

   public float getA() {
      return this.a;
   }

   public void desaturate(float var1) {
      float var2 = this.r * 0.3086F + this.g * 0.6094F + this.b * 0.082F;
      this.r = var2 * var1 + this.r * (1.0F - var1);
      this.g = var2 * var1 + this.g * (1.0F - var1);
      this.b = var2 * var1 + this.b * (1.0F - var1);
   }

   public void interp(ColorInfo var1, float var2, ColorInfo var3) {
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

   public String toString() {
      return "Color (" + this.r + "," + this.g + "," + this.b + "," + this.a + ")";
   }
}

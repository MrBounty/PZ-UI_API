package zombie.core;

import zombie.core.math.PZMath;

public final class ImmutableColor {
   public static final ImmutableColor transparent = new ImmutableColor(0.0F, 0.0F, 0.0F, 0.0F);
   public static final ImmutableColor white = new ImmutableColor(1.0F, 1.0F, 1.0F, 1.0F);
   public static final ImmutableColor yellow = new ImmutableColor(1.0F, 1.0F, 0.0F, 1.0F);
   public static final ImmutableColor red = new ImmutableColor(1.0F, 0.0F, 0.0F, 1.0F);
   public static final ImmutableColor purple = new ImmutableColor(196.0F, 0.0F, 171.0F);
   public static final ImmutableColor blue = new ImmutableColor(0.0F, 0.0F, 1.0F, 1.0F);
   public static final ImmutableColor green = new ImmutableColor(0.0F, 1.0F, 0.0F, 1.0F);
   public static final ImmutableColor black = new ImmutableColor(0.0F, 0.0F, 0.0F, 1.0F);
   public static final ImmutableColor gray = new ImmutableColor(0.5F, 0.5F, 0.5F, 1.0F);
   public static final ImmutableColor cyan = new ImmutableColor(0.0F, 1.0F, 1.0F, 1.0F);
   public static final ImmutableColor darkGray = new ImmutableColor(0.3F, 0.3F, 0.3F, 1.0F);
   public static final ImmutableColor lightGray = new ImmutableColor(0.7F, 0.7F, 0.7F, 1.0F);
   public static final ImmutableColor pink = new ImmutableColor(255, 175, 175, 255);
   public static final ImmutableColor orange = new ImmutableColor(255, 200, 0, 255);
   public static final ImmutableColor magenta = new ImmutableColor(255, 0, 255, 255);
   public static final ImmutableColor darkGreen = new ImmutableColor(22, 113, 20, 255);
   public static final ImmutableColor lightGreen = new ImmutableColor(55, 148, 53, 255);
   public final float a;
   public final float b;
   public final float g;
   public final float r;

   public ImmutableColor(ImmutableColor var1) {
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

   public ImmutableColor(Color var1) {
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

   public Color toMutableColor() {
      return new Color(this.r, this.g, this.b, this.a);
   }

   public ImmutableColor(float var1, float var2, float var3) {
      this.r = PZMath.clamp(var1, 0.0F, 1.0F);
      this.g = PZMath.clamp(var2, 0.0F, 1.0F);
      this.b = PZMath.clamp(var3, 0.0F, 1.0F);
      this.a = 1.0F;
   }

   public ImmutableColor(float var1, float var2, float var3, float var4) {
      this.r = Math.min(var1, 1.0F);
      this.g = Math.min(var2, 1.0F);
      this.b = Math.min(var3, 1.0F);
      this.a = Math.min(var4, 1.0F);
   }

   public ImmutableColor(Color var1, Color var2, float var3) {
      float var4 = (var2.r - var1.r) * var3;
      float var5 = (var2.g - var1.g) * var3;
      float var6 = (var2.b - var1.b) * var3;
      float var7 = (var2.a - var1.a) * var3;
      this.r = var1.r + var4;
      this.g = var1.g + var5;
      this.b = var1.b + var6;
      this.a = var1.a + var7;
   }

   public ImmutableColor(int var1, int var2, int var3) {
      this.r = (float)var1 / 255.0F;
      this.g = (float)var2 / 255.0F;
      this.b = (float)var3 / 255.0F;
      this.a = 1.0F;
   }

   public ImmutableColor(int var1, int var2, int var3, int var4) {
      this.r = (float)var1 / 255.0F;
      this.g = (float)var2 / 255.0F;
      this.b = (float)var3 / 255.0F;
      this.a = (float)var4 / 255.0F;
   }

   public ImmutableColor(int var1) {
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

   public static ImmutableColor random() {
      float var0 = Rand.Next(0.0F, 1.0F);
      float var1 = Rand.Next(0.0F, 0.6F);
      float var2 = Rand.Next(0.0F, 0.9F);
      Color var3 = Color.HSBtoRGB(var0, var1, var2);
      return new ImmutableColor(var3);
   }

   public static ImmutableColor decode(String var0) {
      return new ImmutableColor(Integer.decode(var0));
   }

   public ImmutableColor add(ImmutableColor var1) {
      return new ImmutableColor(this.r + var1.r, this.g + var1.g, this.b + var1.b, this.a + var1.a);
   }

   public ImmutableColor brighter() {
      return this.brighter(0.2F);
   }

   public ImmutableColor brighter(float var1) {
      return new ImmutableColor(this.r + var1, this.g + var1, this.b + var1);
   }

   public ImmutableColor darker() {
      return this.darker(0.5F);
   }

   public ImmutableColor darker(float var1) {
      return new ImmutableColor(this.r - var1, this.g - var1, this.b - var1);
   }

   public boolean equals(Object var1) {
      if (!(var1 instanceof ImmutableColor)) {
         return false;
      } else {
         ImmutableColor var2 = (ImmutableColor)var1;
         return var2.r == this.r && var2.g == this.g && var2.b == this.b && var2.a == this.a;
      }
   }

   public int getAlphaInt() {
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

   public byte getAlphaByte() {
      return (byte)((int)(this.a * 255.0F) & 255);
   }

   public int getBlueInt() {
      return (int)(this.b * 255.0F);
   }

   public byte getBlueByte() {
      return (byte)((int)(this.b * 255.0F) & 255);
   }

   public int getGreenInt() {
      return (int)(this.g * 255.0F);
   }

   public byte getGreenByte() {
      return (byte)((int)(this.g * 255.0F) & 255);
   }

   public int getRedInt() {
      return (int)(this.r * 255.0F);
   }

   public byte getRedByte() {
      return (byte)((int)(this.r * 255.0F) & 255);
   }

   public int hashCode() {
      return (int)(this.r + this.g + this.b + this.a) * 255;
   }

   public ImmutableColor multiply(Color var1) {
      return new ImmutableColor(this.r * var1.r, this.g * var1.g, this.b * var1.b, this.a * var1.a);
   }

   public ImmutableColor scale(float var1) {
      return new ImmutableColor(this.r * var1, this.g * var1, this.b * var1, this.a * var1);
   }

   public String toString() {
      return "ImmutableColor (" + this.r + "," + this.g + "," + this.b + "," + this.a + ")";
   }

   public ImmutableColor interp(ImmutableColor var1, float var2) {
      float var3 = var1.r - this.r;
      float var4 = var1.g - this.g;
      float var5 = var1.b - this.b;
      float var6 = var1.a - this.a;
      var3 *= var2;
      var4 *= var2;
      var5 *= var2;
      var6 *= var2;
      return new ImmutableColor(this.r + var3, this.g + var4, this.b + var5, this.a + var6);
   }

   public static Integer[] HSBtoRGB(float var0, float var1, float var2) {
      int var3 = 0;
      int var4 = 0;
      int var5 = 0;
      if (var1 == 0.0F) {
         var3 = var4 = var5 = (int)(var2 * 255.0F + 0.5F);
      } else {
         float var6 = (var0 - (float)Math.floor((double)var0)) * 6.0F;
         float var7 = var6 - (float)Math.floor((double)var6);
         float var8 = var2 * (1.0F - var1);
         float var9 = var2 * (1.0F - var1 * var7);
         float var10 = var2 * (1.0F - var1 * (1.0F - var7));
         switch((int)var6) {
         case 0:
            var3 = (int)(var2 * 255.0F + 0.5F);
            var4 = (int)(var10 * 255.0F + 0.5F);
            var5 = (int)(var8 * 255.0F + 0.5F);
            break;
         case 1:
            var3 = (int)(var9 * 255.0F + 0.5F);
            var4 = (int)(var2 * 255.0F + 0.5F);
            var5 = (int)(var8 * 255.0F + 0.5F);
            break;
         case 2:
            var3 = (int)(var8 * 255.0F + 0.5F);
            var4 = (int)(var2 * 255.0F + 0.5F);
            var5 = (int)(var10 * 255.0F + 0.5F);
            break;
         case 3:
            var3 = (int)(var8 * 255.0F + 0.5F);
            var4 = (int)(var9 * 255.0F + 0.5F);
            var5 = (int)(var2 * 255.0F + 0.5F);
            break;
         case 4:
            var3 = (int)(var10 * 255.0F + 0.5F);
            var4 = (int)(var8 * 255.0F + 0.5F);
            var5 = (int)(var2 * 255.0F + 0.5F);
            break;
         case 5:
            var3 = (int)(var2 * 255.0F + 0.5F);
            var4 = (int)(var8 * 255.0F + 0.5F);
            var5 = (int)(var9 * 255.0F + 0.5F);
         }
      }

      Integer[] var11 = new Integer[]{var3, var4, var5};
      return var11;
   }
}

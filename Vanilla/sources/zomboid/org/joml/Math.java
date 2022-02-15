package org.joml;

public class Math {
   public static final double PI = 3.141592653589793D;
   static final double PI2 = 6.283185307179586D;
   static final float PI_f = 3.1415927F;
   static final float PI2_f = 6.2831855F;
   static final double PIHalf = 1.5707963267948966D;
   static final float PIHalf_f = 1.5707964F;
   static final double PI_4 = 0.7853981633974483D;
   static final double PI_INV = 0.3183098861837907D;
   private static final int lookupBits;
   private static final int lookupTableSize;
   private static final int lookupTableSizeMinus1;
   private static final int lookupTableSizeWithMargin;
   private static final float pi2OverLookupSize;
   private static final float lookupSizeOverPi2;
   private static final float[] sinTable;
   private static final double c1;
   private static final double c2;
   private static final double c3;
   private static final double c4;
   private static final double c5;
   private static final double c6;
   private static final double c7;
   private static final double s5;
   private static final double s4;
   private static final double s3;
   private static final double s2;
   private static final double s1;
   private static final double k1;
   private static final double k2;
   private static final double k3;
   private static final double k4;
   private static final double k5;
   private static final double k6;
   private static final double k7;

   static double sin_theagentd_arith(double var0) {
      double var2 = floor((var0 + 0.7853981633974483D) * 0.3183098861837907D);
      double var4 = var0 - var2 * 3.141592653589793D;
      double var6 = (double)(((int)var2 & 1) * -2 + 1);
      double var8 = var4 * var4;
      double var12 = var4 * var8;
      double var10 = var4 + var12 * c1;
      var12 *= var8;
      var10 += var12 * c2;
      var12 *= var8;
      var10 += var12 * c3;
      var12 *= var8;
      var10 += var12 * c4;
      var12 *= var8;
      var10 += var12 * c5;
      var12 *= var8;
      var10 += var12 * c6;
      var12 *= var8;
      var10 += var12 * c7;
      return var6 * var10;
   }

   static double sin_roquen_arith(double var0) {
      double var2 = floor((var0 + 0.7853981633974483D) * 0.3183098861837907D);
      double var4 = var0 - var2 * 3.141592653589793D;
      double var6 = (double)(((int)var2 & 1) * -2 + 1);
      double var8 = var4 * var4;
      var4 = var6 * var4;
      double var10 = c7;
      var10 = var10 * var8 + c6;
      var10 = var10 * var8 + c5;
      var10 = var10 * var8 + c4;
      var10 = var10 * var8 + c3;
      var10 = var10 * var8 + c2;
      var10 = var10 * var8 + c1;
      return var4 + var4 * var8 * var10;
   }

   static double sin_roquen_9(double var0) {
      double var2 = java.lang.Math.rint(var0 * 0.3183098861837907D);
      double var4 = var0 - var2 * 3.141592653589793D;
      double var6 = (double)(1 - 2 * ((int)var2 & 1));
      double var8 = var4 * var4;
      var4 = var6 * var4;
      double var10 = s5;
      var10 = var10 * var8 + s4;
      var10 = var10 * var8 + s3;
      var10 = var10 * var8 + s2;
      var10 = var10 * var8 + s1;
      return var4 * var10;
   }

   static double sin_roquen_newk(double var0) {
      double var2 = java.lang.Math.rint(var0 * 0.3183098861837907D);
      double var4 = var0 - var2 * 3.141592653589793D;
      double var6 = (double)(1 - 2 * ((int)var2 & 1));
      double var8 = var4 * var4;
      var4 = var6 * var4;
      double var10 = k7;
      var10 = var10 * var8 + k6;
      var10 = var10 * var8 + k5;
      var10 = var10 * var8 + k4;
      var10 = var10 * var8 + k3;
      var10 = var10 * var8 + k2;
      var10 = var10 * var8 + k1;
      return var4 + var4 * var8 * var10;
   }

   static float sin_theagentd_lookup(float var0) {
      float var1 = var0 * lookupSizeOverPi2;
      int var2 = (int)java.lang.Math.floor((double)var1);
      float var3 = var1 - (float)var2;
      int var4 = var2 & lookupTableSizeMinus1;
      float var5 = sinTable[var4];
      float var6 = sinTable[var4 + 1];
      return var5 + (var6 - var5) * var3;
   }

   public static float sin(float var0) {
      return (float)java.lang.Math.sin((double)var0);
   }

   public static double sin(double var0) {
      if (Options.FASTMATH) {
         return Options.SIN_LOOKUP ? (double)sin_theagentd_lookup((float)var0) : sin_roquen_newk(var0);
      } else {
         return java.lang.Math.sin(var0);
      }
   }

   public static float cos(float var0) {
      return Options.FASTMATH ? sin(var0 + 1.5707964F) : (float)java.lang.Math.cos((double)var0);
   }

   public static double cos(double var0) {
      return Options.FASTMATH ? sin(var0 + 1.5707963267948966D) : java.lang.Math.cos(var0);
   }

   public static float cosFromSin(float var0, float var1) {
      return Options.FASTMATH ? sin(var1 + 1.5707964F) : cosFromSinInternal(var0, var1);
   }

   private static float cosFromSinInternal(float var0, float var1) {
      float var2 = sqrt(1.0F - var0 * var0);
      float var3 = var1 + 1.5707964F;
      float var4 = var3 - (float)((int)(var3 / 6.2831855F)) * 6.2831855F;
      if ((double)var4 < 0.0D) {
         var4 += 6.2831855F;
      }

      return var4 >= 3.1415927F ? -var2 : var2;
   }

   public static double cosFromSin(double var0, double var2) {
      if (Options.FASTMATH) {
         return sin(var2 + 1.5707963267948966D);
      } else {
         double var4 = sqrt(1.0D - var0 * var0);
         double var6 = var2 + 1.5707963267948966D;
         double var8 = var6 - (double)((int)(var6 / 6.283185307179586D)) * 6.283185307179586D;
         if (var8 < 0.0D) {
            var8 += 6.283185307179586D;
         }

         return var8 >= 3.141592653589793D ? -var4 : var4;
      }
   }

   public static float sqrt(float var0) {
      return (float)java.lang.Math.sqrt((double)var0);
   }

   public static double sqrt(double var0) {
      return java.lang.Math.sqrt(var0);
   }

   public static float invsqrt(float var0) {
      return 1.0F / (float)java.lang.Math.sqrt((double)var0);
   }

   public static double invsqrt(double var0) {
      return 1.0D / java.lang.Math.sqrt(var0);
   }

   public static float tan(float var0) {
      return (float)java.lang.Math.tan((double)var0);
   }

   public static double tan(double var0) {
      return java.lang.Math.tan(var0);
   }

   public static float acos(float var0) {
      return (float)java.lang.Math.acos((double)var0);
   }

   public static double acos(double var0) {
      return java.lang.Math.acos(var0);
   }

   public static float safeAcos(float var0) {
      if (var0 < -1.0F) {
         return 3.1415927F;
      } else {
         return var0 > 1.0F ? 0.0F : acos(var0);
      }
   }

   public static double safeAcos(double var0) {
      if (var0 < -1.0D) {
         return 3.141592653589793D;
      } else {
         return var0 > 1.0D ? 0.0D : acos(var0);
      }
   }

   private static double fastAtan2(double var0, double var2) {
      double var4 = var2 >= 0.0D ? var2 : -var2;
      double var6 = var0 >= 0.0D ? var0 : -var0;
      double var8 = min(var4, var6) / max(var4, var6);
      double var10 = var8 * var8;
      double var12 = ((-0.0464964749D * var10 + 0.15931422D) * var10 - 0.327622764D) * var10 * var8 + var8;
      if (var6 > var4) {
         var12 = 1.57079637D - var12;
      }

      if (var2 < 0.0D) {
         var12 = 3.14159274D - var12;
      }

      return var0 >= 0.0D ? var12 : -var12;
   }

   public static float atan2(float var0, float var1) {
      return (float)java.lang.Math.atan2((double)var0, (double)var1);
   }

   public static double atan2(double var0, double var2) {
      return Options.FASTMATH ? fastAtan2(var0, var2) : java.lang.Math.atan2(var0, var2);
   }

   public static float asin(float var0) {
      return (float)java.lang.Math.asin((double)var0);
   }

   public static double asin(double var0) {
      return java.lang.Math.asin(var0);
   }

   public static float safeAsin(float var0) {
      return var0 <= -1.0F ? -1.5707964F : (var0 >= 1.0F ? 1.5707964F : asin(var0));
   }

   public static double safeAsin(double var0) {
      return var0 <= -1.0D ? -1.5707963267948966D : (var0 >= 1.0D ? 1.5707963267948966D : asin(var0));
   }

   public static float abs(float var0) {
      return java.lang.Math.abs(var0);
   }

   public static double abs(double var0) {
      return java.lang.Math.abs(var0);
   }

   static boolean absEqualsOne(float var0) {
      return (Float.floatToRawIntBits(var0) & Integer.MAX_VALUE) == 1065353216;
   }

   static boolean absEqualsOne(double var0) {
      return (Double.doubleToRawLongBits(var0) & Long.MAX_VALUE) == 4607182418800017408L;
   }

   public static int abs(int var0) {
      return java.lang.Math.abs(var0);
   }

   public static int max(int var0, int var1) {
      return java.lang.Math.max(var0, var1);
   }

   public static int min(int var0, int var1) {
      return java.lang.Math.min(var0, var1);
   }

   public static double min(double var0, double var2) {
      return var0 < var2 ? var0 : var2;
   }

   public static float min(float var0, float var1) {
      return var0 < var1 ? var0 : var1;
   }

   public static float max(float var0, float var1) {
      return var0 > var1 ? var0 : var1;
   }

   public static double max(double var0, double var2) {
      return var0 > var2 ? var0 : var2;
   }

   public static float clamp(float var0, float var1, float var2) {
      return max(var0, min(var1, var2));
   }

   public static double clamp(double var0, double var2, double var4) {
      return max(var0, min(var2, var4));
   }

   public static int clamp(int var0, int var1, int var2) {
      return max(var0, min(var1, var2));
   }

   public static float toRadians(float var0) {
      return (float)java.lang.Math.toRadians((double)var0);
   }

   public static double toRadians(double var0) {
      return java.lang.Math.toRadians(var0);
   }

   public static double toDegrees(double var0) {
      return java.lang.Math.toDegrees(var0);
   }

   public static double floor(double var0) {
      return java.lang.Math.floor(var0);
   }

   public static float floor(float var0) {
      return (float)java.lang.Math.floor((double)var0);
   }

   public static double ceil(double var0) {
      return java.lang.Math.ceil(var0);
   }

   public static float ceil(float var0) {
      return (float)java.lang.Math.ceil((double)var0);
   }

   public static long round(double var0) {
      return java.lang.Math.round(var0);
   }

   public static int round(float var0) {
      return java.lang.Math.round(var0);
   }

   public static double exp(double var0) {
      return java.lang.Math.exp(var0);
   }

   public static boolean isFinite(double var0) {
      return abs(var0) <= Double.MAX_VALUE;
   }

   public static boolean isFinite(float var0) {
      return abs(var0) <= Float.MAX_VALUE;
   }

   public static float fma(float var0, float var1, float var2) {
      return Runtime.HAS_Math_fma ? java.lang.Math.fma(var0, var1, var2) : var0 * var1 + var2;
   }

   public static double fma(double var0, double var2, double var4) {
      return Runtime.HAS_Math_fma ? java.lang.Math.fma(var0, var2, var4) : var0 * var2 + var4;
   }

   public static int roundUsing(float var0, int var1) {
      switch(var1) {
      case 0:
         return (int)var0;
      case 1:
         return (int)java.lang.Math.ceil((double)var0);
      case 2:
         return (int)java.lang.Math.floor((double)var0);
      case 3:
         return roundHalfEven(var0);
      case 4:
         return roundHalfDown(var0);
      case 5:
         return roundHalfUp(var0);
      default:
         throw new UnsupportedOperationException();
      }
   }

   public static int roundUsing(double var0, int var2) {
      switch(var2) {
      case 0:
         return (int)var0;
      case 1:
         return (int)java.lang.Math.ceil(var0);
      case 2:
         return (int)java.lang.Math.floor(var0);
      case 3:
         return roundHalfEven(var0);
      case 4:
         return roundHalfDown(var0);
      case 5:
         return roundHalfUp(var0);
      default:
         throw new UnsupportedOperationException();
      }
   }

   public static float lerp(float var0, float var1, float var2) {
      return fma(var1 - var0, var2, var0);
   }

   public static double lerp(double var0, double var2, double var4) {
      return fma(var2 - var0, var4, var0);
   }

   public static float biLerp(float var0, float var1, float var2, float var3, float var4, float var5) {
      float var6 = lerp(var0, var1, var4);
      float var7 = lerp(var2, var3, var4);
      return lerp(var6, var7, var5);
   }

   public static double biLerp(double var0, double var2, double var4, double var6, double var8, double var10) {
      double var12 = lerp(var0, var2, var8);
      double var14 = lerp(var4, var6, var8);
      return lerp(var12, var14, var10);
   }

   public static float triLerp(float var0, float var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8, float var9, float var10) {
      float var11 = lerp(var0, var1, var8);
      float var12 = lerp(var2, var3, var8);
      float var13 = lerp(var4, var5, var8);
      float var14 = lerp(var6, var7, var8);
      float var15 = lerp(var11, var12, var9);
      float var16 = lerp(var13, var14, var9);
      return lerp(var15, var16, var10);
   }

   public static double triLerp(double var0, double var2, double var4, double var6, double var8, double var10, double var12, double var14, double var16, double var18, double var20) {
      double var22 = lerp(var0, var2, var16);
      double var24 = lerp(var4, var6, var16);
      double var26 = lerp(var8, var10, var16);
      double var28 = lerp(var12, var14, var16);
      double var30 = lerp(var22, var24, var18);
      double var32 = lerp(var26, var28, var18);
      return lerp(var30, var32, var20);
   }

   public static int roundHalfEven(float var0) {
      return (int)java.lang.Math.rint((double)var0);
   }

   public static int roundHalfDown(float var0) {
      return var0 > 0.0F ? (int)java.lang.Math.ceil((double)var0 - 0.5D) : (int)java.lang.Math.floor((double)var0 + 0.5D);
   }

   public static int roundHalfUp(float var0) {
      return var0 > 0.0F ? (int)java.lang.Math.floor((double)var0 + 0.5D) : (int)java.lang.Math.ceil((double)var0 - 0.5D);
   }

   public static int roundHalfEven(double var0) {
      return (int)java.lang.Math.rint(var0);
   }

   public static int roundHalfDown(double var0) {
      return var0 > 0.0D ? (int)java.lang.Math.ceil(var0 - 0.5D) : (int)java.lang.Math.floor(var0 + 0.5D);
   }

   public static int roundHalfUp(double var0) {
      return var0 > 0.0D ? (int)java.lang.Math.floor(var0 + 0.5D) : (int)java.lang.Math.ceil(var0 - 0.5D);
   }

   public static double random() {
      return java.lang.Math.random();
   }

   public static double signum(double var0) {
      return java.lang.Math.signum(var0);
   }

   public static float signum(float var0) {
      return java.lang.Math.signum(var0);
   }

   public static int signum(int var0) {
      int var1 = Integer.signum(var0);
      return var1;
   }

   public static int signum(long var0) {
      int var2 = Long.signum(var0);
      return var2;
   }

   static {
      lookupBits = Options.SIN_LOOKUP_BITS;
      lookupTableSize = 1 << lookupBits;
      lookupTableSizeMinus1 = lookupTableSize - 1;
      lookupTableSizeWithMargin = lookupTableSize + 1;
      pi2OverLookupSize = 6.2831855F / (float)lookupTableSize;
      lookupSizeOverPi2 = (float)lookupTableSize / 6.2831855F;
      if (Options.FASTMATH && Options.SIN_LOOKUP) {
         sinTable = new float[lookupTableSizeWithMargin];

         for(int var0 = 0; var0 < lookupTableSizeWithMargin; ++var0) {
            double var1 = (double)((float)var0 * pi2OverLookupSize);
            sinTable[var0] = (float)java.lang.Math.sin(var1);
         }
      } else {
         sinTable = null;
      }

      c1 = Double.longBitsToDouble(-4628199217061079772L);
      c2 = Double.longBitsToDouble(4575957461383582011L);
      c3 = Double.longBitsToDouble(-4671919876300759001L);
      c4 = Double.longBitsToDouble(4523617214285661942L);
      c5 = Double.longBitsToDouble(-4730215272828025532L);
      c6 = Double.longBitsToDouble(4460272573143870633L);
      c7 = Double.longBitsToDouble(-4797767418267846529L);
      s5 = Double.longBitsToDouble(4523227044276562163L);
      s4 = Double.longBitsToDouble(-4671934770969572232L);
      s3 = Double.longBitsToDouble(4575957211482072852L);
      s2 = Double.longBitsToDouble(-4628199223918090387L);
      s1 = Double.longBitsToDouble(4607182418589157889L);
      k1 = Double.longBitsToDouble(-4628199217061079959L);
      k2 = Double.longBitsToDouble(4575957461383549981L);
      k3 = Double.longBitsToDouble(-4671919876307284301L);
      k4 = Double.longBitsToDouble(4523617213632129738L);
      k5 = Double.longBitsToDouble(-4730215344060517252L);
      k6 = Double.longBitsToDouble(4460268259291226124L);
      k7 = Double.longBitsToDouble(-4798040743777455072L);
   }
}

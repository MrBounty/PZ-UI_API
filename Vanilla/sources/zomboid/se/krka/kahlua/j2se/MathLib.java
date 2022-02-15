package se.krka.kahlua.j2se;

import se.krka.kahlua.vm.JavaFunction;
import se.krka.kahlua.vm.KahluaTable;
import se.krka.kahlua.vm.KahluaUtil;
import se.krka.kahlua.vm.LuaCallFrame;
import se.krka.kahlua.vm.Platform;

public class MathLib implements JavaFunction {
   private static final int ABS = 0;
   private static final int ACOS = 1;
   private static final int ASIN = 2;
   private static final int ATAN = 3;
   private static final int ATAN2 = 4;
   private static final int CEIL = 5;
   private static final int COS = 6;
   private static final int COSH = 7;
   private static final int DEG = 8;
   private static final int EXP = 9;
   private static final int FLOOR = 10;
   private static final int FMOD = 11;
   private static final int FREXP = 12;
   private static final int LDEXP = 13;
   private static final int LOG = 14;
   private static final int LOG10 = 15;
   private static final int MODF = 16;
   private static final int POW = 17;
   private static final int RAD = 18;
   private static final int SIN = 19;
   private static final int SINH = 20;
   private static final int SQRT = 21;
   private static final int TAN = 22;
   private static final int TANH = 23;
   private static final int NUM_FUNCTIONS = 24;
   private static final String[] names = new String[24];
   private static final MathLib[] functions;
   private final int index;
   private static final double LN2_INV;

   public MathLib(int var1) {
      this.index = var1;
   }

   public static void register(Platform var0, KahluaTable var1) {
      KahluaTable var2 = var0.newTable();
      var1.rawset("math", var2);
      var2.rawset("pi", KahluaUtil.toDouble(3.141592653589793D));
      var2.rawset("huge", KahluaUtil.toDouble(Double.POSITIVE_INFINITY));

      for(int var3 = 0; var3 < 24; ++var3) {
         var2.rawset(names[var3], functions[var3]);
      }

   }

   public String toString() {
      String var10000 = names[this.index];
      return "math." + var10000;
   }

   public int call(LuaCallFrame var1, int var2) {
      switch(this.index) {
      case 0:
         return abs(var1, var2);
      case 1:
         return acos(var1, var2);
      case 2:
         return asin(var1, var2);
      case 3:
         return atan(var1, var2);
      case 4:
         return atan2(var1, var2);
      case 5:
         return ceil(var1, var2);
      case 6:
         return cos(var1, var2);
      case 7:
         return cosh(var1, var2);
      case 8:
         return deg(var1, var2);
      case 9:
         return exp(var1, var2);
      case 10:
         return floor(var1, var2);
      case 11:
         return fmod(var1, var2);
      case 12:
         return frexp(var1, var2);
      case 13:
         return ldexp(var1, var2);
      case 14:
         return log(var1, var2);
      case 15:
         return log10(var1, var2);
      case 16:
         return modf(var1, var2);
      case 17:
         return pow(var1, var2);
      case 18:
         return rad(var1, var2);
      case 19:
         return sin(var1, var2);
      case 20:
         return sinh(var1, var2);
      case 21:
         return sqrt(var1, var2);
      case 22:
         return tan(var1, var2);
      case 23:
         return tanh(var1, var2);
      default:
         return 0;
      }
   }

   private static int abs(LuaCallFrame var0, int var1) {
      KahluaUtil.luaAssert(var1 >= 1, "Not enough arguments");
      double var2 = KahluaUtil.getDoubleArg(var0, 1, names[0]);
      var0.push(KahluaUtil.toDouble(Math.abs(var2)));
      return 1;
   }

   private static int ceil(LuaCallFrame var0, int var1) {
      KahluaUtil.luaAssert(var1 >= 1, "Not enough arguments");
      double var2 = KahluaUtil.getDoubleArg(var0, 1, names[5]);
      var0.push(KahluaUtil.toDouble(Math.ceil(var2)));
      return 1;
   }

   private static int floor(LuaCallFrame var0, int var1) {
      KahluaUtil.luaAssert(var1 >= 1, "Not enough arguments");
      double var2 = KahluaUtil.getDoubleArg(var0, 1, names[10]);
      var0.push(KahluaUtil.toDouble(Math.floor(var2)));
      return 1;
   }

   public static boolean isNegative(double var0) {
      return Double.doubleToLongBits(var0) < 0L;
   }

   public static double round(double var0) {
      if (var0 < 0.0D) {
         return -round(-var0);
      } else {
         var0 += 0.5D;
         double var2 = Math.floor(var0);
         return var2 == var0 ? var2 - (double)((long)var2 & 1L) : var2;
      }
   }

   private static int modf(LuaCallFrame var0, int var1) {
      KahluaUtil.luaAssert(var1 >= 1, "Not enough arguments");
      double var2 = KahluaUtil.getDoubleArg(var0, 1, names[16]);
      boolean var4 = false;
      if (isNegative(var2)) {
         var4 = true;
         var2 = -var2;
      }

      double var5 = Math.floor(var2);
      double var7;
      if (Double.isInfinite(var5)) {
         var7 = 0.0D;
      } else {
         var7 = var2 - var5;
      }

      if (var4) {
         var5 = -var5;
         var7 = -var7;
      }

      var0.push(KahluaUtil.toDouble(var5), KahluaUtil.toDouble(var7));
      return 2;
   }

   private static int fmod(LuaCallFrame var0, int var1) {
      KahluaUtil.luaAssert(var1 >= 2, "Not enough arguments");
      double var2 = KahluaUtil.getDoubleArg(var0, 1, names[11]);
      double var4 = KahluaUtil.getDoubleArg(var0, 2, names[11]);
      double var6;
      if (!Double.isInfinite(var2) && !Double.isNaN(var2)) {
         if (Double.isInfinite(var4)) {
            var6 = var2;
         } else {
            var4 = Math.abs(var4);
            boolean var8 = false;
            if (isNegative(var2)) {
               var8 = true;
               var2 = -var2;
            }

            var6 = var2 - Math.floor(var2 / var4) * var4;
            if (var8) {
               var6 = -var6;
            }
         }
      } else {
         var6 = Double.NaN;
      }

      var0.push(KahluaUtil.toDouble(var6));
      return 1;
   }

   private static int cosh(LuaCallFrame var0, int var1) {
      KahluaUtil.luaAssert(var1 >= 1, "Not enough arguments");
      double var2 = KahluaUtil.getDoubleArg(var0, 1, names[7]);
      var0.push(KahluaUtil.toDouble(Math.cosh(var2)));
      return 1;
   }

   private static int sinh(LuaCallFrame var0, int var1) {
      KahluaUtil.luaAssert(var1 >= 1, "Not enough arguments");
      double var2 = KahluaUtil.getDoubleArg(var0, 1, names[20]);
      var0.push(KahluaUtil.toDouble(Math.sinh(var2)));
      return 1;
   }

   private static int tanh(LuaCallFrame var0, int var1) {
      KahluaUtil.luaAssert(var1 >= 1, "Not enough arguments");
      double var2 = KahluaUtil.getDoubleArg(var0, 1, names[23]);
      var0.push(KahluaUtil.toDouble(Math.tanh(var2)));
      return 1;
   }

   private static int deg(LuaCallFrame var0, int var1) {
      KahluaUtil.luaAssert(var1 >= 1, "Not enough arguments");
      double var2 = KahluaUtil.getDoubleArg(var0, 1, names[8]);
      var0.push(KahluaUtil.toDouble(Math.toDegrees(var2)));
      return 1;
   }

   private static int rad(LuaCallFrame var0, int var1) {
      KahluaUtil.luaAssert(var1 >= 1, "Not enough arguments");
      double var2 = KahluaUtil.getDoubleArg(var0, 1, names[18]);
      var0.push(KahluaUtil.toDouble(Math.toRadians(var2)));
      return 1;
   }

   private static int acos(LuaCallFrame var0, int var1) {
      KahluaUtil.luaAssert(var1 >= 1, "Not enough arguments");
      double var2 = KahluaUtil.getDoubleArg(var0, 1, names[1]);
      var0.push(KahluaUtil.toDouble(Math.acos(var2)));
      return 1;
   }

   private static int asin(LuaCallFrame var0, int var1) {
      KahluaUtil.luaAssert(var1 >= 1, "Not enough arguments");
      double var2 = KahluaUtil.getDoubleArg(var0, 1, names[2]);
      var0.push(KahluaUtil.toDouble(Math.asin(var2)));
      return 1;
   }

   private static int atan(LuaCallFrame var0, int var1) {
      KahluaUtil.luaAssert(var1 >= 1, "Not enough arguments");
      double var2 = KahluaUtil.getDoubleArg(var0, 1, names[3]);
      var0.push(KahluaUtil.toDouble(Math.atan(var2)));
      return 1;
   }

   private static int atan2(LuaCallFrame var0, int var1) {
      KahluaUtil.luaAssert(var1 >= 2, "Not enough arguments");
      double var2 = KahluaUtil.getDoubleArg(var0, 1, names[4]);
      double var4 = KahluaUtil.getDoubleArg(var0, 2, names[4]);
      var0.push(KahluaUtil.toDouble(Math.atan2(var2, var4)));
      return 1;
   }

   private static int cos(LuaCallFrame var0, int var1) {
      KahluaUtil.luaAssert(var1 >= 1, "Not enough arguments");
      double var2 = KahluaUtil.getDoubleArg(var0, 1, names[6]);
      var0.push(KahluaUtil.toDouble(Math.cos(var2)));
      return 1;
   }

   private static int sin(LuaCallFrame var0, int var1) {
      KahluaUtil.luaAssert(var1 >= 1, "Not enough arguments");
      double var2 = KahluaUtil.getDoubleArg(var0, 1, names[19]);
      var0.push(KahluaUtil.toDouble(Math.sin(var2)));
      return 1;
   }

   private static int tan(LuaCallFrame var0, int var1) {
      KahluaUtil.luaAssert(var1 >= 1, "Not enough arguments");
      double var2 = KahluaUtil.getDoubleArg(var0, 1, names[22]);
      var0.push(KahluaUtil.toDouble(Math.tan(var2)));
      return 1;
   }

   private static int sqrt(LuaCallFrame var0, int var1) {
      KahluaUtil.luaAssert(var1 >= 1, "Not enough arguments");
      double var2 = KahluaUtil.getDoubleArg(var0, 1, names[21]);
      var0.push(KahluaUtil.toDouble(Math.sqrt(var2)));
      return 1;
   }

   private static int exp(LuaCallFrame var0, int var1) {
      KahluaUtil.luaAssert(var1 >= 1, "Not enough arguments");
      double var2 = KahluaUtil.getDoubleArg(var0, 1, names[9]);
      var0.push(KahluaUtil.toDouble(Math.exp(var2)));
      return 1;
   }

   private static int pow(LuaCallFrame var0, int var1) {
      KahluaUtil.luaAssert(var1 >= 2, "Not enough arguments");
      double var2 = KahluaUtil.getDoubleArg(var0, 1, names[17]);
      double var4 = KahluaUtil.getDoubleArg(var0, 2, names[17]);
      var0.push(KahluaUtil.toDouble(Math.pow(var2, var4)));
      return 1;
   }

   private static int log(LuaCallFrame var0, int var1) {
      KahluaUtil.luaAssert(var1 >= 1, "Not enough arguments");
      double var2 = KahluaUtil.getDoubleArg(var0, 1, names[14]);
      var0.push(KahluaUtil.toDouble(Math.log(var2)));
      return 1;
   }

   private static int log10(LuaCallFrame var0, int var1) {
      KahluaUtil.luaAssert(var1 >= 1, "Not enough arguments");
      double var2 = KahluaUtil.getDoubleArg(var0, 1, names[15]);
      var0.push(KahluaUtil.toDouble(Math.log10(var2)));
      return 1;
   }

   private static int frexp(LuaCallFrame var0, int var1) {
      KahluaUtil.luaAssert(var1 >= 1, "Not enough arguments");
      double var2 = KahluaUtil.getDoubleArg(var0, 1, names[12]);
      double var4;
      double var6;
      if (!Double.isInfinite(var2) && !Double.isNaN(var2)) {
         var4 = Math.ceil(Math.log(var2) * LN2_INV);
         int var8 = 1 << (int)var4;
         var6 = var2 / (double)var8;
      } else {
         var4 = 0.0D;
         var6 = var2;
      }

      var0.push(KahluaUtil.toDouble(var6), KahluaUtil.toDouble(var4));
      return 2;
   }

   private static int ldexp(LuaCallFrame var0, int var1) {
      KahluaUtil.luaAssert(var1 >= 2, "Not enough arguments");
      double var2 = KahluaUtil.getDoubleArg(var0, 1, names[13]);
      double var4 = KahluaUtil.getDoubleArg(var0, 2, names[13]);
      double var8 = var2 + var4;
      double var6;
      if (!Double.isInfinite(var8) && !Double.isNaN(var8)) {
         int var10 = (int)var4;
         var6 = var2 * (double)(1 << var10);
      } else {
         var6 = var2;
      }

      var0.push(KahluaUtil.toDouble(var6));
      return 1;
   }

   static {
      names[0] = "abs";
      names[1] = "acos";
      names[2] = "asin";
      names[3] = "atan";
      names[4] = "atan2";
      names[5] = "ceil";
      names[6] = "cos";
      names[7] = "cosh";
      names[8] = "deg";
      names[9] = "exp";
      names[10] = "floor";
      names[11] = "fmod";
      names[12] = "frexp";
      names[13] = "ldexp";
      names[14] = "log";
      names[15] = "log10";
      names[16] = "modf";
      names[17] = "pow";
      names[18] = "rad";
      names[19] = "sin";
      names[20] = "sinh";
      names[21] = "sqrt";
      names[22] = "tan";
      names[23] = "tanh";
      functions = new MathLib[24];

      for(int var0 = 0; var0 < 24; ++var0) {
         functions[var0] = new MathLib(var0);
      }

      LN2_INV = 1.0D / Math.log(2.0D);
   }
}

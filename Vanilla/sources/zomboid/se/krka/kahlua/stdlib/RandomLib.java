package se.krka.kahlua.stdlib;

import java.util.Random;
import se.krka.kahlua.vm.JavaFunction;
import se.krka.kahlua.vm.KahluaTable;
import se.krka.kahlua.vm.KahluaUtil;
import se.krka.kahlua.vm.LuaCallFrame;
import se.krka.kahlua.vm.Platform;

public class RandomLib implements JavaFunction {
   private static final Class RANDOM_CLASS = (new Random()).getClass();
   private static final int RANDOM = 0;
   private static final int RANDOMSEED = 1;
   private static final int NEWRANDOM = 2;
   private static final int NUM_FUNCTIONS = 3;
   private static final String[] names = new String[3];
   private static final RandomLib[] functions;
   private static final RandomLib NEWRANDOM_FUN;
   private final int index;

   public RandomLib(int var1) {
      this.index = var1;
   }

   public static void register(Platform var0, KahluaTable var1) {
      KahluaTable var2 = var0.newTable();

      for(int var3 = 0; var3 < 2; ++var3) {
         var2.rawset(names[var3], functions[var3]);
      }

      var2.rawset("__index", var2);
      KahluaTable var4 = KahluaUtil.getClassMetatables(var0, var1);
      var4.rawset(RANDOM_CLASS, var2);
      var1.rawset("newrandom", NEWRANDOM_FUN);
   }

   public int call(LuaCallFrame var1, int var2) {
      switch(this.index) {
      case 0:
         return this.random(var1, var2);
      case 1:
         return this.randomSeed(var1, var2);
      case 2:
         return this.newRandom(var1);
      default:
         return 0;
      }
   }

   private int randomSeed(LuaCallFrame var1, int var2) {
      Random var3 = this.getRandom(var1, "seed");
      Object var4 = var1.get(1);
      int var5 = var4 == null ? 0 : var4.hashCode();
      var3.setSeed((long)var5);
      return 0;
   }

   private int random(LuaCallFrame var1, int var2) {
      Random var3 = this.getRandom(var1, "random");
      Double var4 = KahluaUtil.getOptionalNumberArg(var1, 2);
      Double var5 = KahluaUtil.getOptionalNumberArg(var1, 3);
      if (var4 == null) {
         return var1.push(KahluaUtil.toDouble(var3.nextDouble()));
      } else {
         int var6 = var4.intValue();
         int var7;
         if (var5 == null) {
            var7 = var6;
            var6 = 1;
         } else {
            var7 = var5.intValue();
         }

         return var1.push(KahluaUtil.toDouble((long)(var6 + var3.nextInt(var7 - var6 + 1))));
      }
   }

   private Random getRandom(LuaCallFrame var1, String var2) {
      Object var3 = KahluaUtil.getArg(var1, 1, var2);
      if (!(var3 instanceof Random)) {
         KahluaUtil.fail("First argument to " + var2 + " must be an object of type random.");
      }

      return (Random)var3;
   }

   private int newRandom(LuaCallFrame var1) {
      return var1.push(new Random());
   }

   static {
      names[0] = "random";
      names[1] = "seed";
      names[2] = "newrandom";
      functions = new RandomLib[3];

      for(int var0 = 0; var0 < 3; ++var0) {
         functions[var0] = new RandomLib(var0);
      }

      NEWRANDOM_FUN = new RandomLib(2);
   }
}

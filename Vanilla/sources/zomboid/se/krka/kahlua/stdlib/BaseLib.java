package se.krka.kahlua.stdlib;

import java.util.function.Consumer;
import se.krka.kahlua.vm.Coroutine;
import se.krka.kahlua.vm.JavaFunction;
import se.krka.kahlua.vm.KahluaException;
import se.krka.kahlua.vm.KahluaTable;
import se.krka.kahlua.vm.KahluaThread;
import se.krka.kahlua.vm.KahluaUtil;
import se.krka.kahlua.vm.LuaCallFrame;
import se.krka.kahlua.vm.LuaClosure;
import zombie.debug.DebugLog;

public final class BaseLib implements JavaFunction {
   private static final Runtime RUNTIME = Runtime.getRuntime();
   private static final int PCALL = 0;
   private static final int PRINT = 1;
   private static final int SELECT = 2;
   private static final int TYPE = 3;
   private static final int TOSTRING = 4;
   private static final int TONUMBER = 5;
   private static final int GETMETATABLE = 6;
   private static final int SETMETATABLE = 7;
   private static final int ERROR = 8;
   private static final int UNPACK = 9;
   private static final int SETFENV = 10;
   private static final int GETFENV = 11;
   private static final int RAWEQUAL = 12;
   private static final int RAWSET = 13;
   private static final int RAWGET = 14;
   private static final int COLLECTGARBAGE = 15;
   private static final int DEBUGSTACKTRACE = 16;
   private static final int BYTECODELOADER = 17;
   private static final int NUM_FUNCTIONS = 18;
   private static final String[] names = new String[18];
   private static final Object DOUBLE_ONE = new Double(1.0D);
   private static final BaseLib[] functions;
   private final int index;
   private static Consumer PRINT_CALLBACK;

   public BaseLib(int var1) {
      this.index = var1;
   }

   public static void register(KahluaTable var0) {
      for(int var1 = 0; var1 < 18; ++var1) {
         var0.rawset(names[var1], functions[var1]);
      }

   }

   public String toString() {
      return names[this.index];
   }

   public int call(LuaCallFrame var1, int var2) {
      switch(this.index) {
      case 0:
         return pcall(var1, var2);
      case 1:
         return print(var1, var2);
      case 2:
         return select(var1, var2);
      case 3:
         return type(var1, var2);
      case 4:
         return tostring(var1, var2);
      case 5:
         return tonumber(var1, var2);
      case 6:
         return getmetatable(var1, var2);
      case 7:
         return setmetatable(var1, var2);
      case 8:
         return this.error(var1, var2);
      case 9:
         return this.unpack(var1, var2);
      case 10:
         return this.setfenv(var1, var2);
      case 11:
         return this.getfenv(var1, var2);
      case 12:
         return this.rawequal(var1, var2);
      case 13:
         return this.rawset(var1, var2);
      case 14:
         return this.rawget(var1, var2);
      case 15:
         return collectgarbage(var1, var2);
      case 16:
         return this.debugstacktrace(var1, var2);
      case 17:
         return bytecodeloader(var1, var2);
      default:
         return 0;
      }
   }

   private int debugstacktrace(LuaCallFrame var1, int var2) {
      Coroutine var3 = (Coroutine)KahluaUtil.getOptionalArg(var1, 1);
      if (var3 == null) {
         var3 = var1.coroutine;
      }

      Double var4 = KahluaUtil.getOptionalNumberArg(var1, 2);
      int var5 = 0;
      if (var4 != null) {
         var5 = var4.intValue();
      }

      Double var6 = KahluaUtil.getOptionalNumberArg(var1, 3);
      int var7 = Integer.MAX_VALUE;
      if (var6 != null) {
         var7 = var6.intValue();
      }

      Double var8 = KahluaUtil.getOptionalNumberArg(var1, 4);
      int var9 = 0;
      if (var8 != null) {
         var9 = var8.intValue();
      }

      return var1.push(var3.getCurrentStackTrace(var5, var7, var9));
   }

   private int rawget(LuaCallFrame var1, int var2) {
      KahluaUtil.luaAssert(var2 >= 2, "Not enough arguments");
      KahluaTable var3 = (KahluaTable)var1.get(0);
      Object var4 = var1.get(1);
      var1.push(var3.rawget(var4));
      return 1;
   }

   private int rawset(LuaCallFrame var1, int var2) {
      KahluaUtil.luaAssert(var2 >= 3, "Not enough arguments");
      KahluaTable var3 = (KahluaTable)var1.get(0);
      Object var4 = var1.get(1);
      Object var5 = var1.get(2);
      var3.rawset(var4, var5);
      var1.setTop(1);
      return 1;
   }

   private int rawequal(LuaCallFrame var1, int var2) {
      KahluaUtil.luaAssert(var2 >= 2, "Not enough arguments");
      Object var3 = var1.get(0);
      Object var4 = var1.get(1);
      var1.push(KahluaUtil.toBoolean(luaEquals(var3, var4)));
      return 1;
   }

   private int setfenv(LuaCallFrame var1, int var2) {
      KahluaUtil.luaAssert(var2 >= 2, "Not enough arguments");
      KahluaTable var3 = (KahluaTable)var1.get(1);
      KahluaUtil.luaAssert(var3 != null, "expected a table");
      LuaClosure var4 = null;
      Object var5 = var1.get(0);
      if (var5 instanceof LuaClosure) {
         var4 = (LuaClosure)var5;
      } else {
         Double var8 = KahluaUtil.rawTonumber(var5);
         KahluaUtil.luaAssert(var8 != null, "expected a lua function or a number");
         int var6 = ((Double)var8).intValue();
         if (var6 == 0) {
            var1.coroutine.environment = var3;
            return 0;
         }

         LuaCallFrame var7 = var1.coroutine.getParent(var6);
         if (!var7.isLua()) {
            KahluaUtil.fail("No closure found at this level: " + var6);
         }

         var4 = var7.closure;
      }

      var4.env = var3;
      var1.setTop(1);
      return 1;
   }

   private int getfenv(LuaCallFrame var1, int var2) {
      Object var3 = DOUBLE_ONE;
      if (var2 >= 1) {
         var3 = var1.get(0);
      }

      KahluaTable var4 = null;
      if (var3 != null && !(var3 instanceof JavaFunction)) {
         if (var3 instanceof LuaClosure) {
            LuaClosure var5 = (LuaClosure)var3;
            var4 = var5.env;
         } else {
            Double var8 = KahluaUtil.rawTonumber(var3);
            KahluaUtil.luaAssert(var8 != null, "Expected number");
            int var6 = var8.intValue();
            KahluaUtil.luaAssert(var6 >= 0, "level must be non-negative");
            LuaCallFrame var7 = var1.coroutine.getParent(var6);
            var4 = var7.getEnvironment();
         }
      } else {
         var4 = var1.coroutine.environment;
      }

      var1.push(var4);
      return 1;
   }

   private int unpack(LuaCallFrame var1, int var2) {
      KahluaUtil.luaAssert(var2 >= 1, "Not enough arguments");
      KahluaTable var3 = (KahluaTable)var1.get(0);
      Object var4 = null;
      Object var5 = null;
      if (var2 >= 2) {
         var4 = var1.get(1);
      }

      if (var2 >= 3) {
         var5 = var1.get(2);
      }

      int var6;
      if (var4 != null) {
         var6 = (int)KahluaUtil.fromDouble(var4);
      } else {
         var6 = 1;
      }

      int var7;
      if (var5 != null) {
         var7 = (int)KahluaUtil.fromDouble(var5);
      } else {
         var7 = var3.len();
      }

      int var8 = 1 + var7 - var6;
      if (var8 <= 0) {
         var1.setTop(0);
         return 0;
      } else {
         var1.setTop(var8);

         for(int var9 = 0; var9 < var8; ++var9) {
            var1.set(var9, var3.rawget(KahluaUtil.toDouble((long)(var6 + var9))));
         }

         return var8;
      }
   }

   private int error(LuaCallFrame var1, int var2) {
      if (var2 >= 1) {
         String var3 = KahluaUtil.getOptionalStringArg(var1, 2);
         if (var3 == null) {
            var3 = "";
         }

         var1.coroutine.stackTrace = var3;
         throw new KahluaException(var1.get(0));
      } else {
         return 0;
      }
   }

   public static int pcall(LuaCallFrame var0, int var1) {
      return var0.getThread().pcall(var1 - 1);
   }

   private static int print(LuaCallFrame var0, int var1) {
      KahluaThread var2 = var0.getThread();
      KahluaTable var3 = var2.getEnvironment();
      Object var4 = var2.tableget(var3, "tostring");
      StringBuilder var5 = new StringBuilder();

      for(int var6 = 0; var6 < var1; ++var6) {
         if (var6 > 0) {
            var5.append("\t");
         }

         Object var7 = var2.call(var4, var0.get(var6), (Object)null, (Object)null);
         var5.append(var7);
      }

      String var8 = var5.toString();
      DebugLog.log(var8);
      if (PRINT_CALLBACK != null) {
         PRINT_CALLBACK.accept(var8);
      }

      return 0;
   }

   public static void setPrintCallback(Consumer var0) {
      PRINT_CALLBACK = var0;
   }

   private static int select(LuaCallFrame var0, int var1) {
      KahluaUtil.luaAssert(var1 >= 1, "Not enough arguments");
      Object var2 = var0.get(0);
      if (var2 instanceof String && ((String)var2).startsWith("#")) {
         var0.push(KahluaUtil.toDouble((long)(var1 - 1)));
         return 1;
      } else {
         Double var3 = KahluaUtil.rawTonumber(var2);
         double var4 = KahluaUtil.fromDouble(var3);
         int var6 = (int)var4;
         if (var6 >= 1 && var6 <= var1 - 1) {
            int var7 = var1 - var6;
            return var7;
         } else {
            return 0;
         }
      }
   }

   private static int getmetatable(LuaCallFrame var0, int var1) {
      KahluaUtil.luaAssert(var1 >= 1, "Not enough arguments");
      Object var2 = var0.get(0);
      Object var3 = var0.getThread().getmetatable(var2, false);
      var0.push(var3);
      return 1;
   }

   private static int setmetatable(LuaCallFrame var0, int var1) {
      KahluaUtil.luaAssert(var1 >= 2, "Not enough arguments");
      Object var2 = var0.get(0);
      KahluaTable var3 = (KahluaTable)var0.get(1);
      setmetatable(var0.getThread(), var2, var3, false);
      var0.setTop(1);
      return 1;
   }

   public static void setmetatable(KahluaThread var0, Object var1, KahluaTable var2, boolean var3) {
      KahluaUtil.luaAssert(var1 != null, "Expected table, got nil");
      Object var4 = var0.getmetatable(var1, true);
      if (!var3 && var4 != null && var0.tableget(var4, "__metatable") != null) {
         throw new RuntimeException("cannot change a protected metatable");
      } else {
         var0.setmetatable(var1, var2);
      }
   }

   private static int type(LuaCallFrame var0, int var1) {
      KahluaUtil.luaAssert(var1 >= 1, "Not enough arguments");
      Object var2 = var0.get(0);
      var0.push(KahluaUtil.type(var2));
      return 1;
   }

   private static int tostring(LuaCallFrame var0, int var1) {
      KahluaUtil.luaAssert(var1 >= 1, "Not enough arguments");
      Object var2 = var0.get(0);
      String var3 = KahluaUtil.tostring(var2, var0.getThread());
      var0.push(var3);
      return 1;
   }

   private static int tonumber(LuaCallFrame var0, int var1) {
      KahluaUtil.luaAssert(var1 >= 1, "Not enough arguments");
      Object var2 = var0.get(0);
      if (var1 == 1) {
         var0.push(KahluaUtil.rawTonumber(var2));
         return 1;
      } else {
         String var3 = (String)var2;
         Object var4 = var0.get(1);
         Double var5 = KahluaUtil.rawTonumber(var4);
         if (var5 == null) {
            var0.push((Object)null);
            return 1;
         } else {
            KahluaUtil.luaAssert(var5 != null, "Argument 2 must be a number");
            double var6 = KahluaUtil.fromDouble(var5);
            int var8 = (int)var6;
            if ((double)var8 != var6) {
               var0.push((Object)null);
               return 1;
            } else if ((double)var8 != var6) {
               throw new RuntimeException("base is not an integer");
            } else {
               Double var9 = KahluaUtil.tonumber(var3, var8);
               var0.push(var9);
               return 1;
            }
         }
      }
   }

   public static int collectgarbage(LuaCallFrame var0, int var1) {
      Object var2 = null;
      if (var1 > 0) {
         var2 = var0.get(0);
      }

      if (var2 != null && !var2.equals("step") && !var2.equals("collect")) {
         if (var2.equals("count")) {
            long var3 = RUNTIME.freeMemory();
            long var5 = RUNTIME.totalMemory();
            var0.setTop(3);
            var0.set(0, toKiloBytes(var5 - var3));
            var0.set(1, toKiloBytes(var3));
            var0.set(2, toKiloBytes(var5));
            return 3;
         } else {
            throw new RuntimeException("invalid option: " + var2);
         }
      } else {
         System.gc();
         return 0;
      }
   }

   private static Double toKiloBytes(long var0) {
      return KahluaUtil.toDouble((double)var0 / 1024.0D);
   }

   private static int bytecodeloader(LuaCallFrame var0, int var1) {
      String var2 = KahluaUtil.getStringArg(var0, 1, "loader");
      KahluaTable var3 = (KahluaTable)var0.getEnvironment().rawget("package");
      String var4 = (String)var3.rawget("classpath");

      int var6;
      for(int var5 = 0; var5 < var4.length(); var5 = var6) {
         var6 = var4.indexOf(";", var5);
         if (var6 == -1) {
            var6 = var4.length();
         }

         String var7 = var4.substring(var5, var6);
         if (var7.length() > 0) {
            if (!var7.endsWith("/")) {
               var7 = var7 + "/";
            }

            LuaClosure var8 = KahluaUtil.loadByteCodeFromResource(var7 + var2, var0.getEnvironment());
            if (var8 != null) {
               return var0.push(var8);
            }
         }
      }

      return var0.push("Could not find the bytecode for '" + var2 + "' in classpath");
   }

   public static boolean luaEquals(Object var0, Object var1) {
      if (var0 != null && var1 != null) {
         if (var0 instanceof Double && var1 instanceof Double) {
            Double var2 = (Double)var0;
            Double var3 = (Double)var1;
            return var2 == var3;
         } else {
            return var0 == var1;
         }
      } else {
         return var0 == var1;
      }
   }

   static {
      names[0] = "pcall";
      names[1] = "print";
      names[2] = "select";
      names[3] = "type";
      names[4] = "tostring";
      names[5] = "tonumber";
      names[6] = "getmetatable";
      names[7] = "setmetatable";
      names[8] = "error";
      names[9] = "unpack";
      names[10] = "setfenv";
      names[11] = "getfenv";
      names[12] = "rawequal";
      names[13] = "rawset";
      names[14] = "rawget";
      names[15] = "collectgarbage";
      names[16] = "debugstacktrace";
      names[17] = "bytecodeloader";
      functions = new BaseLib[18];

      for(int var0 = 0; var0 < 18; ++var0) {
         functions[var0] = new BaseLib(var0);
      }

      PRINT_CALLBACK = null;
   }
}

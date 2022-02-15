package se.krka.kahlua.vm;

import java.io.IOException;
import java.io.InputStream;
import se.krka.kahlua.integration.expose.LuaJavaInvoker;
import se.krka.kahlua.integration.expose.MethodDebugInformation;
import zombie.Lua.LuaManager;
import zombie.core.BoxedStaticValues;
import zombie.core.Core;
import zombie.core.textures.Texture;
import zombie.debug.DebugLog;
import zombie.ui.UIManager;

public class KahluaUtil {
   private static final Object WORKER_THREAD_KEY = new Object();
   private static final String TYPE_NIL = "nil";
   private static final String TYPE_STRING = "string";
   private static final String TYPE_NUMBER = "number";
   private static final String TYPE_BOOLEAN = "boolean";
   private static final String TYPE_FUNCTION = "function";
   private static final String TYPE_TABLE = "table";
   private static final String TYPE_COROUTINE = "coroutine";
   private static final String TYPE_USERDATA = "userdata";

   public static double fromDouble(Object var0) {
      return (Double)var0;
   }

   public static Double toDouble(double var0) {
      return BoxedStaticValues.toDouble(var0);
   }

   public static Double toDouble(long var0) {
      return BoxedStaticValues.toDouble((double)var0);
   }

   public static Boolean toBoolean(boolean var0) {
      return var0 ? Boolean.TRUE : Boolean.FALSE;
   }

   public static boolean boolEval(Object var0) {
      return var0 != null && var0 != Boolean.FALSE;
   }

   public static LuaClosure loadByteCodeFromResource(String var0, KahluaTable var1) {
      try {
         InputStream var2 = var1.getClass().getResourceAsStream(var0 + ".lbc");

         LuaClosure var3;
         label48: {
            try {
               if (var2 == null) {
                  var3 = null;
                  break label48;
               }

               var3 = Prototype.loadByteCode(var2, var1);
            } catch (Throwable var6) {
               if (var2 != null) {
                  try {
                     var2.close();
                  } catch (Throwable var5) {
                     var6.addSuppressed(var5);
                  }
               }

               throw var6;
            }

            if (var2 != null) {
               var2.close();
            }

            return var3;
         }

         if (var2 != null) {
            var2.close();
         }

         return var3;
      } catch (IOException var7) {
         throw new RuntimeException(var7.getMessage());
      }
   }

   public static void luaAssert(boolean var0, String var1) {
      if (!var0) {
         fail(var1);
      }

   }

   public static void fail(String var0) {
      if (Core.bDebug && UIManager.defaultthread == LuaManager.thread) {
         DebugLog.log(var0);
         UIManager.debugBreakpoint(LuaManager.thread.currentfile, (long)(LuaManager.thread.currentLine - 1));
      }

      throw new RuntimeException(var0);
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

   public static long ipow(long var0, int var2) {
      if (var2 <= 0) {
         return 1L;
      } else {
         long var3 = 1L;
         var3 = (var2 & 1) != 0 ? var0 : 1L;

         for(var2 >>= 1; var2 != 0; var2 >>= 1) {
            var0 *= var0;
            if ((var2 & 1) != 0) {
               var3 *= var0;
            }
         }

         return var3;
      }
   }

   public static boolean isNegative(double var0) {
      return Double.doubleToLongBits(var0) < 0L;
   }

   public static KahluaTable getClassMetatables(Platform var0, KahluaTable var1) {
      return getOrCreateTable(var0, var1, "__classmetatables");
   }

   public static KahluaThread getWorkerThread(Platform var0, KahluaTable var1) {
      Object var2 = var1.rawget(WORKER_THREAD_KEY);
      if (var2 == null) {
         var2 = new KahluaThread(var0, var1);
         var1.rawset(WORKER_THREAD_KEY, var2);
      }

      return (KahluaThread)var2;
   }

   public static void setWorkerThread(KahluaTable var0, KahluaThread var1) {
      var0.rawset(WORKER_THREAD_KEY, var1);
   }

   public static KahluaTable getOrCreateTable(Platform var0, KahluaTable var1, String var2) {
      Object var3 = var1.rawget(var2);
      if (var3 == null || !(var3 instanceof KahluaTable)) {
         var3 = var0.newTable();
         var1.rawset(var2, var3);
      }

      return (KahluaTable)var3;
   }

   public static void setupLibrary(KahluaTable var0, KahluaThread var1, String var2) {
      LuaClosure var3 = loadByteCodeFromResource(var2, var0);
      if (var3 == null) {
         fail("Could not load " + var2 + ".lbc");
      }

      var1.call(var3, (Object)null, (Object)null, (Object)null);
   }

   public static String numberToString(Double var0) {
      if (var0.isNaN()) {
         return "nan";
      } else if (var0.isInfinite()) {
         return isNegative(var0) ? "-inf" : "inf";
      } else {
         double var1 = var0;
         return Math.floor(var1) == var1 && Math.abs(var1) < 1.0E14D ? String.valueOf(var0.longValue()) : var0.toString();
      }
   }

   public static String type(Object var0) {
      if (var0 == null) {
         return "nil";
      } else if (var0 instanceof String) {
         return "string";
      } else if (var0 instanceof Double) {
         return "number";
      } else if (var0 instanceof Boolean) {
         return "boolean";
      } else if (!(var0 instanceof JavaFunction) && !(var0 instanceof LuaClosure)) {
         if (var0 instanceof KahluaTable) {
            return "table";
         } else {
            return var0 instanceof Coroutine ? "coroutine" : "userdata";
         }
      } else {
         return "function";
      }
   }

   public static String tostring(Object var0, KahluaThread var1) {
      if (var0 == null) {
         return "nil";
      } else if (var0 instanceof String) {
         return (String)var0;
      } else if (var0 instanceof Double) {
         return rawTostring(var0);
      } else if (var0 instanceof Boolean) {
         return var0 == Boolean.TRUE ? "true" : "false";
      } else if (var0 instanceof LuaClosure) {
         return "closure 0x" + System.identityHashCode(var0);
      } else if (var0 instanceof JavaFunction) {
         return "function 0x" + System.identityHashCode(var0);
      } else {
         if (var1 != null) {
            Object var2 = var1.getMetaOp(var0, "__tostring");
            if (var2 != null) {
               String var3 = (String)var1.call(var2, var0, (Object)null, (Object)null);
               return var3;
            }
         }

         return var0.toString();
      }
   }

   public static Double tonumber(String var0) {
      return tonumber(var0, 10);
   }

   public static Double tonumber(String var0, int var1) {
      if (var1 >= 2 && var1 <= 36) {
         try {
            return var1 == 10 ? Double.valueOf(var0) : toDouble((long)Integer.parseInt(var0, var1));
         } catch (NumberFormatException var3) {
            var0 = var0.toLowerCase();
            if (var0.endsWith("nan")) {
               return toDouble(Double.NaN);
            } else if (var0.endsWith("inf")) {
               return var0.charAt(0) == '-' ? toDouble(Double.NEGATIVE_INFINITY) : toDouble(Double.POSITIVE_INFINITY);
            } else {
               return null;
            }
         }
      } else {
         throw new RuntimeException("base out of range");
      }
   }

   public static String rawTostring(Object var0) {
      if (var0 instanceof String) {
         return (String)var0;
      } else {
         return var0 instanceof Double ? numberToString((Double)var0) : null;
      }
   }

   public static String rawTostring2(Object var0) {
      if (var0 instanceof String) {
         return "\"" + (String)var0 + "\"";
      } else if (var0 instanceof Texture) {
         return "Texture: \"" + ((Texture)var0).getName() + "\"";
      } else if (var0 instanceof Double) {
         return numberToString((Double)var0);
      } else if (var0 instanceof LuaClosure) {
         LuaClosure var6 = (LuaClosure)var0;
         return var6.toString2(0);
      } else if (var0 instanceof LuaCallFrame) {
         LuaCallFrame var5 = (LuaCallFrame)var0;
         return var5.toString2();
      } else if (var0 instanceof LuaJavaInvoker) {
         if (var0.toString().equals("breakpoint")) {
            return null;
         } else {
            LuaJavaInvoker var1 = (LuaJavaInvoker)var0;
            MethodDebugInformation var2 = var1.getMethodDebugData();
            String var3 = "";

            for(int var4 = 0; var4 < var2.getParameters().size(); ++var4) {
               if (var2.getParameters().get(var4) != null) {
                  var3 = var3 + var2.getParameters().get(var4);
               }
            }

            String var10000 = var2.getReturnType();
            return "Java: " + var10000 + " " + var0.toString() + "(" + var3 + ")";
         }
      } else {
         return var0 != null ? var0.toString() : null;
      }
   }

   public static Double rawTonumber(Object var0) {
      if (var0 instanceof Double) {
         return (Double)var0;
      } else {
         return var0 instanceof String ? tonumber((String)var0) : null;
      }
   }

   public static String getStringArg(LuaCallFrame var0, int var1, String var2) {
      Object var3 = getArg(var0, var1, var2);
      String var4 = rawTostring(var3);
      if (var4 == null) {
         fail(var1, var2, "string", type(var4));
      }

      return var4;
   }

   public static String getOptionalStringArg(LuaCallFrame var0, int var1) {
      Object var2 = getOptionalArg(var0, var1);
      return rawTostring(var2);
   }

   public static Double getNumberArg(LuaCallFrame var0, int var1, String var2) {
      Object var3 = getArg(var0, var1, var2);
      Double var4 = rawTonumber(var3);
      if (var4 == null) {
         fail(var1, var2, "double", type(var4));
      }

      return var4;
   }

   public static Double getOptionalNumberArg(LuaCallFrame var0, int var1) {
      Object var2 = getOptionalArg(var0, var1);
      return rawTonumber(var2);
   }

   private static void fail(int var0, String var1, String var2, String var3) {
      throw new RuntimeException("bad argument #" + var0 + " to '" + var1 + "' (" + var2 + " expected, got " + var3 + ")");
   }

   public static void assertArgNotNull(Object var0, int var1, String var2, String var3) {
      if (var0 == null) {
         fail(var1, var3, var2, "null");
      }

   }

   public static Object getOptionalArg(LuaCallFrame var0, int var1) {
      int var2 = var0.getTop();
      int var3 = var1 - 1;
      return var3 >= var2 ? null : var0.get(var1 - 1);
   }

   public static Object getArg(LuaCallFrame var0, int var1, String var2) {
      Object var3 = getOptionalArg(var0, var1);
      if (var3 == null) {
         throw new RuntimeException("missing argument #" + var1 + " to '" + var2 + "'");
      } else {
         return var3;
      }
   }

   public static int len(KahluaTable var0, int var1, int var2) {
      while(var1 < var2) {
         int var3 = var2 + var1 + 1 >> 1;
         Object var4 = var0.rawget(var3);
         if (var4 == null) {
            var2 = var3 - 1;
         } else {
            var1 = var3;
         }
      }

      while(var0.rawget(var1 + 1) != null) {
         ++var1;
      }

      return var1;
   }

   public static double getDoubleArg(LuaCallFrame var0, int var1, String var2) {
      return getNumberArg(var0, var1, var2);
   }
}

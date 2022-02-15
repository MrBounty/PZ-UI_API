package se.krka.kahlua.integration.expose;

import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import se.krka.kahlua.converter.KahluaConverterManager;
import se.krka.kahlua.integration.expose.caller.Caller;
import se.krka.kahlua.vm.JavaFunction;
import se.krka.kahlua.vm.LuaCallFrame;

public class LuaJavaInvoker implements JavaFunction {
   private final LuaJavaClassExposer exposer;
   private final KahluaConverterManager manager;
   private final Class clazz;
   private final String name;
   private final Caller caller;
   private final Class[] parameterTypes;
   private final int numMethodParams;
   private final Class varargType;
   private final boolean hasSelf;
   private final boolean needsReturnValues;
   private final boolean hasVarargs;

   public LuaJavaInvoker(LuaJavaClassExposer var1, KahluaConverterManager var2, Class var3, String var4, Caller var5) {
      this.exposer = var1;
      this.manager = var2;
      this.clazz = var3;
      this.name = var4;
      this.caller = var5;
      this.parameterTypes = var5.getParameterTypes();
      this.varargType = var5.getVarargType();
      this.hasSelf = var5.hasSelf();
      this.needsReturnValues = var5.needsMultipleReturnValues();
      this.hasVarargs = var5.hasVararg();
      this.numMethodParams = this.parameterTypes.length + this.toInt(this.needsReturnValues) + this.toInt(this.hasVarargs);
   }

   private int toInt(boolean var1) {
      return var1 ? 1 : 0;
   }

   public MethodArguments prepareCall(LuaCallFrame var1, int var2) {
      MethodArguments var3 = MethodArguments.get(this.numMethodParams);
      int var4 = 0;
      int var5 = 0;
      int var6 = this.toInt(this.hasSelf);
      if (this.hasSelf) {
         Object var7 = var2 <= 0 ? null : var1.get(0);
         if (var7 == null || !this.clazz.isInstance(var7)) {
            var3.fail(this.syntaxErrorMessage(this.name + ": Expected a method call but got a function call."));
            return var3;
         }

         var3.setSelf(var7);
         ++var5;
      }

      ReturnValues var14 = ReturnValues.get(this.manager, var1);
      var3.setReturnValues(var14);
      if (this.needsReturnValues) {
         var3.getParams()[var4] = var14;
         ++var4;
      }

      int var8;
      int var10000;
      if (var2 - var5 < this.parameterTypes.length) {
         var8 = this.parameterTypes.length;
         var10000 = var2 - var6;
         var3.fail((String)null);
         return var3;
      } else if (var5 != 0 && this.parameterTypes.length < var2 - var5) {
         var8 = this.parameterTypes.length;
         var10000 = var2 - var6;
         var3.fail((String)null);
         return var3;
      } else {
         int var10;
         for(var8 = 0; var8 < this.parameterTypes.length; ++var8) {
            Object var9 = var1.get(var5 + var8);
            var10 = var5 + var8 - var6;
            Class var11 = this.parameterTypes[var8];
            Object var12 = var9;
            if (!var11.isInstance(var9)) {
               var12 = this.convert(var9, var11);
            }

            if (var9 != null && var12 == null) {
               var3.fail("");
               return var3;
            }

            var3.getParams()[var4 + var8] = var12;
         }

         var4 += this.parameterTypes.length;
         var5 += this.parameterTypes.length;
         if (this.hasVarargs) {
            var8 = var2 - var5;
            if (var8 < 0) {
            }

            Object[] var15 = (Object[])Array.newInstance(this.varargType, var8);

            for(var10 = 0; var10 < var8; ++var10) {
               Object var16 = var1.get(var5 + var10);
               int var17 = var5 + var10 - var6;
               Object var13 = this.convert(var16, this.varargType);
               var15[var10] = var13;
               if (var16 != null && var13 == null) {
                  var3.fail("");
                  return var3;
               }
            }

            var3.getParams()[var4] = var15;
            ++var4;
            var10000 = var5 + var8;
         }

         return var3;
      }
   }

   public int call(LuaCallFrame var1, int var2) {
      MethodArguments var3 = this.prepareCall(var1, var2);
      var3.assertValid();
      int var4 = this.call(var3);
      ReturnValues.put(var3.getReturnValues());
      MethodArguments.put(var3);
      return var4;
   }

   public int call(MethodArguments var1) {
      try {
         ReturnValues var2 = var1.getReturnValues();
         this.caller.call(var1.getSelf(), var2, var1.getParams());
         return var2.getNArguments();
      } catch (IllegalArgumentException var3) {
         throw new RuntimeException(var3);
      } catch (IllegalAccessException var4) {
         throw new RuntimeException(var4);
      } catch (InvocationTargetException var5) {
         throw new RuntimeException(var5.getCause());
      } catch (InstantiationException var6) {
         throw new RuntimeException(var6);
      }
   }

   private Object convert(Object var1, Class var2) {
      if (var1 == null) {
         return null;
      } else {
         Object var3 = this.manager.fromLuaToJava(var1, var2);
         return var3;
      }
   }

   private String syntaxErrorMessage(String var1) {
      String var2 = this.getFunctionSyntax();
      if (var2 != null) {
         var1 = var1 + " Correct syntax: " + var2;
      }

      return var1;
   }

   private String newError(int var1, String var2) {
      int var3 = var1 + 1;
      String var4 = var2 + " at argument #" + var3;
      String var5 = this.getParameterName(var1);
      if (var5 != null) {
         var4 = var4 + ", " + var5;
      }

      return var4;
   }

   private String getFunctionSyntax() {
      MethodDebugInformation var1 = this.getMethodDebugData();
      return var1 != null ? var1.getLuaDescription() : null;
   }

   public MethodDebugInformation getMethodDebugData() {
      ClassDebugInformation var1 = this.exposer.getDebugdata(this.clazz);
      return var1 == null ? null : (MethodDebugInformation)var1.getMethods().get(this.caller.getDescriptor());
   }

   private String getParameterName(int var1) {
      MethodDebugInformation var2 = this.getMethodDebugData();
      return var2 != null ? ((MethodParameter)var2.getParameters().get(var1)).getName() : null;
   }

   public String toString() {
      return this.name;
   }

   public int getNumMethodParams() {
      return this.numMethodParams;
   }

   public boolean equals(Object var1) {
      if (this == var1) {
         return true;
      } else if (var1 != null && this.getClass() == var1.getClass()) {
         LuaJavaInvoker var2 = (LuaJavaInvoker)var1;
         if (!this.caller.equals(var2.caller)) {
            return false;
         } else if (!this.clazz.equals(var2.clazz)) {
            return false;
         } else {
            return this.name.equals(var2.name);
         }
      } else {
         return false;
      }
   }

   public int hashCode() {
      int var1 = this.clazz.hashCode();
      var1 = 31 * var1 + this.name.hashCode();
      var1 = 31 * var1 + this.caller.hashCode();
      return var1;
   }

   public boolean matchesArgumentTypes(LuaCallFrame var1, int var2) {
      int var3 = 0;
      if (this.hasSelf) {
         Object var4 = var2 <= 0 ? null : var1.get(0);
         if (var4 == null || !this.clazz.isInstance(var4)) {
            return false;
         }

         ++var3;
      }

      if (this.parameterTypes.length != var2 - var3) {
         return false;
      } else {
         for(int var7 = 0; var7 < this.parameterTypes.length; ++var7) {
            Object var5 = var1.get(var3 + var7);
            Class var6 = this.parameterTypes[var7];
            if (!var6.isInstance(var5)) {
               return false;
            }
         }

         return true;
      }
   }

   public boolean matchesArgumentTypesOrPrimitives(LuaCallFrame var1, int var2) {
      int var3 = 0;
      if (this.hasSelf) {
         Object var4 = var2 <= 0 ? null : var1.get(0);
         if (var4 == null || !this.clazz.isInstance(var4)) {
            return false;
         }

         ++var3;
      }

      if (this.parameterTypes.length != var2 - var3) {
         return false;
      } else {
         for(int var7 = 0; var7 < this.parameterTypes.length; ++var7) {
            Object var5 = var1.get(var3 + var7);
            Class var6 = this.parameterTypes[var7];
            if (!var6.isInstance(var5)) {
               if (var6.isPrimitive()) {
                  if (var5 == null) {
                     return false;
                  }

                  if (var5 instanceof Double) {
                     if (var6 == Void.TYPE || var6 == Boolean.TYPE) {
                        return false;
                     }
                  } else if (!(var5 instanceof Boolean) || var6 != Boolean.TYPE) {
                     return false;
                  }
               } else if (var5 != null) {
                  return false;
               }
            }
         }

         return true;
      }
   }

   public boolean isAllInt() {
      if (this.parameterTypes != null && this.parameterTypes.length != 0) {
         Class[] var1 = this.parameterTypes;
         int var2 = var1.length;

         for(int var3 = 0; var3 < var2; ++var3) {
            Class var4 = var1[var3];
            if (var4 != Integer.TYPE) {
               return false;
            }
         }

         return true;
      } else {
         return false;
      }
   }
}

package se.krka.kahlua.integration.expose;

import java.util.ArrayList;
import zombie.Lua.LuaManager;
import zombie.core.Core;
import zombie.ui.UIManager;

public class MethodArguments {
   private ReturnValues returnValues;
   private Object self;
   private Object[] params;
   private String failure;
   private boolean bValid = true;
   static ArrayList[] Lists = new ArrayList[30];

   public static MethodArguments get(int var0) {
      MethodArguments var1 = null;
      if (Lists[var0].isEmpty()) {
         var1 = new MethodArguments(var0);
      } else {
         var1 = (MethodArguments)Lists[var0].get(0);
         Lists[var0].remove(var1);
      }

      return var1;
   }

   public static void put(MethodArguments var0) {
      if (!Lists[var0.params.length].contains(var0)) {
         Lists[var0.params.length].add(var0);
         var0.bValid = true;
         var0.self = null;
         var0.failure = null;
         var0.returnValues = null;

         for(int var1 = 0; var1 < var0.params.length; ++var1) {
            var0.params[var1] = null;
         }

      }
   }

   public MethodArguments(int var1) {
      this.params = new Object[var1];
   }

   public ReturnValues getReturnValues() {
      return this.returnValues;
   }

   public Object getSelf() {
      return this.self;
   }

   public Object[] getParams() {
      return this.params;
   }

   public void fail(String var1) {
      this.failure = var1;
      this.bValid = false;
   }

   public void setSelf(Object var1) {
      this.self = var1;
   }

   public String getFailure() {
      return this.failure;
   }

   public void setReturnValues(ReturnValues var1) {
      this.returnValues = var1;
   }

   public void assertValid() {
      if (!this.isValid()) {
         if (Core.bDebug && UIManager.defaultthread == LuaManager.thread) {
            UIManager.debugBreakpoint(LuaManager.thread.currentfile, (long)(LuaManager.thread.currentLine - 1));
         }

         throw new RuntimeException(this.failure);
      }
   }

   public boolean isValid() {
      return this.bValid;
   }

   static {
      for(int var0 = 0; var0 < 30; ++var0) {
         Lists[var0] = new ArrayList(1000);

         for(int var1 = 0; var1 < 1000; ++var1) {
            Lists[var0].add(new MethodArguments(var0));
         }
      }

   }
}

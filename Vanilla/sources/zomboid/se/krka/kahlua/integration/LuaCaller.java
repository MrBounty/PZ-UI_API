package se.krka.kahlua.integration;

import se.krka.kahlua.converter.KahluaConverterManager;
import se.krka.kahlua.vm.KahluaThread;

public class LuaCaller {
   private final KahluaConverterManager converterManager;

   public LuaCaller(KahluaConverterManager var1) {
      this.converterManager = var1;
   }

   public void pcallvoid(KahluaThread var1, Object var2, Object var3) {
      var1.pcallvoid(var2, var3);
   }

   public void pcallvoid(KahluaThread var1, Object var2, Object var3, Object var4) {
      var1.pcallvoid(var2, var3, var4);
   }

   public void pcallvoid(KahluaThread var1, Object var2, Object var3, Object var4, Object var5) {
      var1.pcallvoid(var2, var3, var4, var5);
   }

   public Boolean pcallBoolean(KahluaThread var1, Object var2, Object var3, Object var4) {
      return var1.pcallBoolean(var2, var3, var4);
   }

   public Boolean pcallBoolean(KahluaThread var1, Object var2, Object var3, Object var4, Object var5) {
      return var1.pcallBoolean(var2, var3, var4, var5);
   }

   public void pcallvoid(KahluaThread var1, Object var2, Object[] var3) {
      if (var3 != null) {
         for(int var4 = var3.length - 1; var4 >= 0; --var4) {
            var3[var4] = this.converterManager.fromJavaToLua(var3[var4]);
         }
      }

      var1.pcallvoid(var2, var3);
   }

   public Object[] pcall(KahluaThread var1, Object var2, Object... var3) {
      if (var3 != null) {
         for(int var4 = var3.length - 1; var4 >= 0; --var4) {
            var3[var4] = this.converterManager.fromJavaToLua(var3[var4]);
         }
      }

      Object[] var5 = var1.pcall(var2, var3);
      return var5;
   }

   public Object[] pcall(KahluaThread var1, Object var2, Object var3) {
      if (var3 != null) {
         var3 = this.converterManager.fromJavaToLua(var3);
      }

      Object[] var4 = var1.pcall(var2, new Object[]{var3});
      return var4;
   }

   public Boolean protectedCallBoolean(KahluaThread var1, Object var2, Object var3) {
      var3 = this.converterManager.fromJavaToLua(var3);
      return var1.pcallBoolean(var2, var3);
   }

   public Boolean protectedCallBoolean(KahluaThread var1, Object var2, Object var3, Object var4) {
      var3 = this.converterManager.fromJavaToLua(var3);
      var4 = this.converterManager.fromJavaToLua(var4);
      return var1.pcallBoolean(var2, var3, var4);
   }

   public Boolean protectedCallBoolean(KahluaThread var1, Object var2, Object var3, Object var4, Object var5) {
      var3 = this.converterManager.fromJavaToLua(var3);
      var4 = this.converterManager.fromJavaToLua(var4);
      var5 = this.converterManager.fromJavaToLua(var5);
      return var1.pcallBoolean(var2, var3, var4, var5);
   }

   public Boolean pcallBoolean(KahluaThread var1, Object var2, Object[] var3) {
      if (var3 != null) {
         for(int var4 = var3.length - 1; var4 >= 0; --var4) {
            var3[var4] = this.converterManager.fromJavaToLua(var3[var4]);
         }
      }

      return var1.pcallBoolean(var2, var3);
   }

   public LuaReturn protectedCall(KahluaThread var1, Object var2, Object... var3) {
      return LuaReturn.createReturn(this.pcall(var1, var2, var3));
   }

   public void protectedCallVoid(KahluaThread var1, Object var2, Object var3) {
      var3 = this.converterManager.fromJavaToLua(var3);
      var1.pcallvoid(var2, var3);
   }

   public void protectedCallVoid(KahluaThread var1, Object var2, Object var3, Object var4) {
      var3 = this.converterManager.fromJavaToLua(var3);
      var4 = this.converterManager.fromJavaToLua(var4);
      var1.pcallvoid(var2, var3, var4);
   }

   public void protectedCallVoid(KahluaThread var1, Object var2, Object var3, Object var4, Object var5) {
      var3 = this.converterManager.fromJavaToLua(var3);
      var4 = this.converterManager.fromJavaToLua(var4);
      var5 = this.converterManager.fromJavaToLua(var5);
      var1.pcallvoid(var2, var3, var4, var5);
   }

   public void protectedCallVoid(KahluaThread var1, Object var2, Object[] var3) {
      this.pcallvoid(var1, var2, var3);
   }

   public Boolean protectedCallBoolean(KahluaThread var1, Object var2, Object[] var3) {
      return this.pcallBoolean(var1, var2, var3);
   }
}

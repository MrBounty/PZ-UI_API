package se.krka.kahlua.integration.expose;

import java.util.ArrayList;
import se.krka.kahlua.converter.KahluaConverterManager;
import se.krka.kahlua.vm.LuaCallFrame;

public class ReturnValues {
   private KahluaConverterManager manager;
   private LuaCallFrame callFrame;
   private int args;
   static ArrayList[] Lists = new ArrayList[1];

   public static ReturnValues get(KahluaConverterManager var0, LuaCallFrame var1) {
      ReturnValues var2 = null;
      if (Lists[0].isEmpty()) {
         var2 = new ReturnValues(var0, var1);
      } else {
         var2 = (ReturnValues)Lists[0].get(0);
         Lists[0].remove(var2);
      }

      var2.manager = var0;
      var2.callFrame = var1;
      return var2;
   }

   public static void put(ReturnValues var0) {
      var0.callFrame = null;
      var0.manager = null;
      var0.args = 0;
      if (!Lists[0].contains(var0)) {
         Lists[0].add(var0);
      }
   }

   ReturnValues(KahluaConverterManager var1, LuaCallFrame var2) {
      this.manager = var1;
      this.callFrame = var2;
   }

   public ReturnValues push(Object var1) {
      this.args += this.callFrame.push(this.manager.fromJavaToLua(var1));
      return this;
   }

   public ReturnValues push(Object... var1) {
      Object[] var2 = var1;
      int var3 = var1.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         Object var5 = var2[var4];
         this.push(var5);
      }

      return this;
   }

   int getNArguments() {
      return this.args;
   }

   static {
      for(int var0 = 0; var0 < 1; ++var0) {
         Lists[var0] = new ArrayList(100);

         for(int var1 = 0; var1 < 100; ++var1) {
            Lists[var0].add(new ReturnValues((KahluaConverterManager)null, (LuaCallFrame)null));
         }
      }

   }
}

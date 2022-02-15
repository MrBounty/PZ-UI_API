package se.krka.kahlua.integration.expose;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import se.krka.kahlua.vm.JavaFunction;
import se.krka.kahlua.vm.LuaCallFrame;

public class MultiLuaJavaInvoker implements JavaFunction {
   private final List invokers = new ArrayList();
   private static final Comparator COMPARATOR = new Comparator() {
      public int compare(LuaJavaInvoker var1, LuaJavaInvoker var2) {
         if (var2.getNumMethodParams() == var1.getNumMethodParams()) {
            boolean var3 = var1.isAllInt();
            boolean var4 = var2.isAllInt();
            return var3 ? 1 : (var4 ? -1 : 0);
         } else {
            return var2.getNumMethodParams() - var1.getNumMethodParams();
         }
      }
   };

   public int call(LuaCallFrame var1, int var2) {
      MethodArguments var3 = null;
      int var4 = this.invokers.size();
      int var5 = -1;

      int var6;
      LuaJavaInvoker var7;
      boolean var8;
      int var9;
      for(var6 = 0; var6 < var4; ++var6) {
         var7 = (LuaJavaInvoker)this.invokers.get(var6);
         if (var7.matchesArgumentTypes(var1, var2)) {
            var3 = var7.prepareCall(var1, var2);
            var8 = var3.isValid();
            if (var8) {
               var9 = var7.call(var3);
               ReturnValues.put(var3.getReturnValues());
               MethodArguments.put(var3);
               return var9;
            }

            var5 = var6;
            break;
         }
      }

      if (var5 == -1) {
         for(var6 = 0; var6 < var4; ++var6) {
            var7 = (LuaJavaInvoker)this.invokers.get(var6);
            if (var7.matchesArgumentTypesOrPrimitives(var1, var2)) {
               var3 = var7.prepareCall(var1, var2);
               var8 = var3.isValid();
               if (var8) {
                  var9 = var7.call(var3);
                  ReturnValues.put(var3.getReturnValues());
                  MethodArguments.put(var3);
                  return var9;
               }

               var5 = var6;
               break;
            }
         }
      }

      for(var6 = 0; var6 < var4; ++var6) {
         if (var6 != var5) {
            var7 = (LuaJavaInvoker)this.invokers.get(var6);
            var3 = var7.prepareCall(var1, var2);
            var8 = var3.isValid();
            if (var8) {
               var9 = var7.call(var3);
               ReturnValues.put(var3.getReturnValues());
               MethodArguments.put(var3);
               return var9;
            }

            MethodArguments.put(var3);
         }
      }

      if (var3 != null) {
         var3.assertValid();
         MethodArguments.put(var3);
      }

      throw new RuntimeException("No implementation found");
   }

   public void addInvoker(LuaJavaInvoker var1) {
      if (!this.invokers.contains(var1)) {
         this.invokers.add(var1);
         Collections.sort(this.invokers, COMPARATOR);
      }

   }

   public List getInvokers() {
      return this.invokers;
   }

   public boolean equals(Object var1) {
      if (this == var1) {
         return true;
      } else if (var1 != null && this.getClass() == var1.getClass()) {
         MultiLuaJavaInvoker var2 = (MultiLuaJavaInvoker)var1;
         return this.invokers.equals(var2.invokers);
      } else {
         return false;
      }
   }

   public int hashCode() {
      return this.invokers.hashCode();
   }
}

package se.krka.kahlua.integration.expose.caller;

import se.krka.kahlua.integration.expose.ReturnValues;

public abstract class AbstractCaller implements Caller {
   protected final Class[] parameters;
   protected final boolean needsMultipleReturnValues;
   protected final Class varargType;

   protected AbstractCaller(Class[] var1, boolean var2) {
      boolean var3 = false;
      Class var4;
      if (var1.length > 0) {
         var4 = var1[0];
         if (var4 == ReturnValues.class) {
            var3 = true;
         }
      }

      if (var2) {
         var4 = var1[var1.length - 1];
         this.varargType = var4.getComponentType();
      } else {
         this.varargType = null;
      }

      this.needsMultipleReturnValues = var3;
      int var7 = var3 ? 1 : 0;
      int var5 = var1.length - (this.varargType == null ? 0 : 1);
      int var6 = var5 - var7;
      this.parameters = new Class[var6];
      System.arraycopy(var1, var7, this.parameters, 0, var6);
   }

   public final Class[] getParameterTypes() {
      return this.parameters;
   }

   public final Class getVarargType() {
      return this.varargType;
   }

   public final boolean hasVararg() {
      return this.varargType != null;
   }

   public final boolean needsMultipleReturnValues() {
      return this.needsMultipleReturnValues;
   }
}

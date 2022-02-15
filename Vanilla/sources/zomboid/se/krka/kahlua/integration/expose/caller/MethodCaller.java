package se.krka.kahlua.integration.expose.caller;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import se.krka.kahlua.integration.expose.ReturnValues;
import se.krka.kahlua.integration.processor.DescriptorUtil;
import zombie.core.logger.ExceptionLogger;
import zombie.ui.UIManager;

public class MethodCaller extends AbstractCaller {
   private final Method method;
   private final Object owner;
   private final boolean hasSelf;
   private final boolean hasReturnValue;

   public MethodCaller(Method var1, Object var2, boolean var3) {
      super(var1.getParameterTypes(), var1.isVarArgs());
      this.method = var1;
      this.owner = var2;
      this.hasSelf = var3;
      var1.setAccessible(true);
      this.hasReturnValue = !var1.getReturnType().equals(Void.TYPE);
      if (this.hasReturnValue && this.needsMultipleReturnValues()) {
         throw new IllegalArgumentException("Must have a void return type if first argument is a ReturnValues: got: " + var1.getReturnType());
      }
   }

   public void call(Object var1, ReturnValues var2, Object[] var3) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
      if (!this.hasSelf) {
         var1 = this.owner;
      }

      try {
         Object var4 = this.method.invoke(var1, var3);
         if (this.hasReturnValue) {
            var2.push(var4);
         }
      } catch (Exception var5) {
         UIManager.defaultthread.doStacktraceProper();
         UIManager.defaultthread.debugException(var5);
         ExceptionLogger.logException(var5);
      }

   }

   public boolean hasSelf() {
      return this.hasSelf;
   }

   public String getDescriptor() {
      return DescriptorUtil.getDescriptor(this.method);
   }

   public boolean equals(Object var1) {
      if (this == var1) {
         return true;
      } else if (var1 != null && this.getClass() == var1.getClass()) {
         MethodCaller var2 = (MethodCaller)var1;
         if (!this.method.equals(var2.method)) {
            return false;
         } else {
            if (this.owner != null) {
               if (!this.owner.equals(var2.owner)) {
                  return false;
               }
            } else if (var2.owner != null) {
               return false;
            }

            return true;
         }
      } else {
         return false;
      }
   }

   public int hashCode() {
      int var1 = this.method.hashCode();
      var1 = 31 * var1 + (this.owner != null ? this.owner.hashCode() : 0);
      return var1;
   }
}

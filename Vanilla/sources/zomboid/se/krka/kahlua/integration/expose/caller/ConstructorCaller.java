package se.krka.kahlua.integration.expose.caller;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import se.krka.kahlua.integration.expose.ReturnValues;
import se.krka.kahlua.integration.processor.DescriptorUtil;

public class ConstructorCaller extends AbstractCaller {
   private final Constructor constructor;

   public ConstructorCaller(Constructor var1) {
      super(var1.getParameterTypes(), var1.isVarArgs());
      this.constructor = var1;
      var1.setAccessible(true);
      if (this.needsMultipleReturnValues()) {
         throw new RuntimeException("Constructor can not return multiple values");
      }
   }

   public void call(Object var1, ReturnValues var2, Object[] var3) throws IllegalArgumentException, InstantiationException, IllegalAccessException, InvocationTargetException {
      var2.push(this.constructor.newInstance(var3));
   }

   public boolean hasSelf() {
      return false;
   }

   public String getDescriptor() {
      return DescriptorUtil.getDescriptor(this.constructor);
   }

   public boolean equals(Object var1) {
      if (this == var1) {
         return true;
      } else if (var1 != null && this.getClass() == var1.getClass()) {
         ConstructorCaller var2 = (ConstructorCaller)var1;
         return this.constructor.equals(var2.constructor);
      } else {
         return false;
      }
   }

   public int hashCode() {
      return this.constructor.hashCode();
   }
}

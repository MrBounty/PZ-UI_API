package zombie.util;

import java.util.function.Function;

public class PooledArrayObject extends PooledObject {
   private Object[] m_array = null;

   public Object[] array() {
      return this.m_array;
   }

   public int length() {
      return this.m_array.length;
   }

   public Object get(int var1) {
      return this.m_array[var1];
   }

   public void set(int var1, Object var2) {
      this.m_array[var1] = var2;
   }

   protected void initCapacity(int var1, Function var2) {
      if (this.m_array == null || this.m_array.length != var1) {
         this.m_array = (Object[])var2.apply(var1);
      }

   }

   public boolean isEmpty() {
      return this.m_array == null || this.m_array.length == 0;
   }
}

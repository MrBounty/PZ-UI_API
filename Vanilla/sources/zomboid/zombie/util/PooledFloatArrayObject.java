package zombie.util;

import zombie.util.list.PZArrayUtil;

public final class PooledFloatArrayObject extends PooledObject {
   private static final Pool s_pool = new Pool(PooledFloatArrayObject::new);
   private float[] m_array;

   public PooledFloatArrayObject() {
      this.m_array = PZArrayUtil.emptyFloatArray;
   }

   public static PooledFloatArrayObject alloc(int var0) {
      PooledFloatArrayObject var1 = (PooledFloatArrayObject)s_pool.alloc();
      var1.initCapacity(var0);
      return var1;
   }

   public static PooledFloatArrayObject toArray(PooledFloatArrayObject var0) {
      if (var0 == null) {
         return null;
      } else {
         int var1 = var0.length();
         PooledFloatArrayObject var2 = alloc(var1);
         if (var1 > 0) {
            System.arraycopy(var0.array(), 0, var2.array(), 0, var1);
         }

         return var2;
      }
   }

   private void initCapacity(int var1) {
      if (this.m_array.length != var1) {
         this.m_array = new float[var1];
      }

   }

   public float[] array() {
      return this.m_array;
   }

   public float get(int var1) {
      return this.m_array[var1];
   }

   public void set(int var1, float var2) {
      this.m_array[var1] = var2;
   }

   public int length() {
      return this.m_array.length;
   }
}

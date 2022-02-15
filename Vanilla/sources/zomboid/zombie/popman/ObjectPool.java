package zombie.popman;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;

public class ObjectPool {
   private final ObjectPool.Allocator allocator;
   private final ArrayList pool;

   public ObjectPool() {
      this((ObjectPool.Allocator)null);
   }

   public ObjectPool(ObjectPool.Allocator var1) {
      this.pool = new ArrayList() {
         public boolean contains(Object var1) {
            for(int var2 = 0; var2 < ObjectPool.this.pool.size(); ++var2) {
               if (ObjectPool.this.pool.get(var2) == var1) {
                  return true;
               }
            }

            return false;
         }
      };
      this.allocator = var1;
   }

   public Object alloc() {
      return this.pool.isEmpty() ? this.makeObject() : this.pool.remove(this.pool.size() - 1);
   }

   public void release(Object var1) {
      assert var1 != null;

      assert !this.pool.contains(var1);

      this.pool.add(var1);
   }

   public void release(List var1) {
      for(int var2 = 0; var2 < var1.size(); ++var2) {
         if (var1.get(var2) != null) {
            this.release(var1.get(var2));
         }
      }

   }

   public void release(Iterable var1) {
      Iterator var2 = var1.iterator();

      while(var2.hasNext()) {
         Object var3 = var2.next();
         if (var3 != null) {
            this.release(var3);
         }
      }

   }

   public void release(Object[] var1) {
      if (var1 != null) {
         for(int var2 = 0; var2 < var1.length; ++var2) {
            if (var1[var2] != null) {
               this.release(var1[var2]);
            }
         }

      }
   }

   public void releaseAll(List var1) {
      for(int var2 = 0; var2 < var1.size(); ++var2) {
         if (var1.get(var2) != null) {
            this.release(var1.get(var2));
         }
      }

   }

   public void clear() {
      this.pool.clear();
   }

   protected Object makeObject() {
      if (this.allocator != null) {
         return this.allocator.allocate();
      } else {
         throw new UnsupportedOperationException("Allocator is null. The ObjectPool is intended to be used with an allocator, or with the function makeObject overridden in a subclass.");
      }
   }

   public void forEach(Consumer var1) {
      for(int var2 = 0; var2 < this.pool.size(); ++var2) {
         var1.accept(this.pool.get(var2));
      }

   }

   public interface Allocator {
      Object allocate();
   }
}

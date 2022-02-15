package zombie.util;

public abstract class PooledObject implements IPooledObject {
   private boolean m_isFree = true;
   private Pool m_pool;

   public final Pool getPool() {
      return this.m_pool;
   }

   public final void setPool(Pool var1) {
      this.m_pool = var1;
   }

   public final void release() {
      if (this.m_pool != null) {
         this.m_pool.release(this);
      } else {
         this.onReleased();
      }

   }

   public final boolean isFree() {
      return this.m_isFree;
   }

   public final void setFree(boolean var1) {
      this.m_isFree = var1;
   }
}

package zombie.util.lambda;

import zombie.util.Pool;
import zombie.util.PooledObject;

public final class ReturnValueContainer extends PooledObject {
   public Object ReturnVal;
   private static final Pool s_pool = new Pool(ReturnValueContainer::new);

   public void onReleased() {
      this.ReturnVal = null;
   }

   public static ReturnValueContainer alloc() {
      return (ReturnValueContainer)s_pool.alloc();
   }
}

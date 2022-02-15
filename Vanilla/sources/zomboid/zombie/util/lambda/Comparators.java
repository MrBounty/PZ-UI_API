package zombie.util.lambda;

import java.util.Comparator;
import zombie.util.Pool;
import zombie.util.PooledObject;

public final class Comparators {
   public static final class Params2 {
      public static final class CallbackStackItem extends Comparators.Params2.StackItem implements Comparator {
         private Comparators.Params2.ICallback comparator;
         private static final Pool s_pool = new Pool(Comparators.Params2.CallbackStackItem::new);

         public int compare(Object var1, Object var2) {
            return this.comparator.compare(var1, var2, this.val1, this.val2);
         }

         public static Comparators.Params2.CallbackStackItem alloc(Object var0, Object var1, Comparators.Params2.ICallback var2) {
            Comparators.Params2.CallbackStackItem var3 = (Comparators.Params2.CallbackStackItem)s_pool.alloc();
            var3.val1 = var0;
            var3.val2 = var1;
            var3.comparator = var2;
            return var3;
         }

         public void onReleased() {
            this.val1 = null;
            this.val2 = null;
            this.comparator = null;
         }
      }

      private static class StackItem extends PooledObject {
         Object val1;
         Object val2;
      }

      public interface ICallback {
         int compare(Object var1, Object var2, Object var3, Object var4);
      }
   }

   public static final class Params1 {
      public static final class CallbackStackItem extends Comparators.Params1.StackItem implements Comparator {
         private Comparators.Params1.ICallback comparator;
         private static final Pool s_pool = new Pool(Comparators.Params1.CallbackStackItem::new);

         public int compare(Object var1, Object var2) {
            return this.comparator.compare(var1, var2, this.val1);
         }

         public static Comparators.Params1.CallbackStackItem alloc(Object var0, Comparators.Params1.ICallback var1) {
            Comparators.Params1.CallbackStackItem var2 = (Comparators.Params1.CallbackStackItem)s_pool.alloc();
            var2.val1 = var0;
            var2.comparator = var1;
            return var2;
         }

         public void onReleased() {
            this.val1 = null;
            this.comparator = null;
         }
      }

      private static class StackItem extends PooledObject {
         Object val1;
      }

      public interface ICallback {
         int compare(Object var1, Object var2, Object var3);
      }
   }
}

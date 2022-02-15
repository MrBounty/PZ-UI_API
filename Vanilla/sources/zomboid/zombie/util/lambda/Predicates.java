package zombie.util.lambda;

import java.util.function.Predicate;
import zombie.util.Pool;
import zombie.util.PooledObject;

public final class Predicates {
   public static final class Params3 {
      public static final class CallbackStackItem extends Predicates.Params3.StackItem implements Predicate {
         private Predicates.Params3.ICallback predicate;
         private static final Pool s_pool = new Pool(Predicates.Params3.CallbackStackItem::new);

         public boolean test(Object var1) {
            return this.predicate.test(var1, this.val1, this.val2, this.val3);
         }

         public static Predicates.Params3.CallbackStackItem alloc(Object var0, Object var1, Object var2, Predicates.Params3.ICallback var3) {
            Predicates.Params3.CallbackStackItem var4 = (Predicates.Params3.CallbackStackItem)s_pool.alloc();
            var4.val1 = var0;
            var4.val2 = var1;
            var4.val3 = var2;
            var4.predicate = var3;
            return var4;
         }

         public void onReleased() {
            this.val1 = null;
            this.val2 = null;
            this.val3 = null;
            this.predicate = null;
         }
      }

      private static class StackItem extends PooledObject {
         Object val1;
         Object val2;
         Object val3;
      }

      public interface ICallback {
         boolean test(Object var1, Object var2, Object var3, Object var4);
      }
   }

   public static final class Params2 {
      public static final class CallbackStackItem extends Predicates.Params2.StackItem implements Predicate {
         private Predicates.Params2.ICallback predicate;
         private static final Pool s_pool = new Pool(Predicates.Params2.CallbackStackItem::new);

         public boolean test(Object var1) {
            return this.predicate.test(var1, this.val1, this.val2);
         }

         public static Predicates.Params2.CallbackStackItem alloc(Object var0, Object var1, Predicates.Params2.ICallback var2) {
            Predicates.Params2.CallbackStackItem var3 = (Predicates.Params2.CallbackStackItem)s_pool.alloc();
            var3.val1 = var0;
            var3.val2 = var1;
            var3.predicate = var2;
            return var3;
         }

         public void onReleased() {
            this.val1 = null;
            this.val2 = null;
            this.predicate = null;
         }
      }

      private static class StackItem extends PooledObject {
         Object val1;
         Object val2;
      }

      public interface ICallback {
         boolean test(Object var1, Object var2, Object var3);
      }
   }

   public static final class Params1 {
      public static final class CallbackStackItem extends Predicates.Params1.StackItem implements Predicate {
         private Predicates.Params1.ICallback predicate;
         private static final Pool s_pool = new Pool(Predicates.Params1.CallbackStackItem::new);

         public boolean test(Object var1) {
            return this.predicate.test(var1, this.val1);
         }

         public static Predicates.Params1.CallbackStackItem alloc(Object var0, Predicates.Params1.ICallback var1) {
            Predicates.Params1.CallbackStackItem var2 = (Predicates.Params1.CallbackStackItem)s_pool.alloc();
            var2.val1 = var0;
            var2.predicate = var1;
            return var2;
         }

         public void onReleased() {
            this.val1 = null;
            this.predicate = null;
         }
      }

      private static class StackItem extends PooledObject {
         Object val1;
      }

      public interface ICallback {
         boolean test(Object var1, Object var2);
      }
   }
}

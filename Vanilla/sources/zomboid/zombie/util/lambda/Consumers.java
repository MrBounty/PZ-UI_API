package zombie.util.lambda;

import java.util.function.Consumer;
import zombie.util.Pool;
import zombie.util.PooledObject;

public final class Consumers {
   public static final class Params5 {
      public static final class CallbackStackItem extends Consumers.Params5.StackItem implements Consumer {
         private Consumers.Params5.ICallback consumer;
         private static final Pool s_pool = new Pool(Consumers.Params5.CallbackStackItem::new);

         public void accept(Object var1) {
            this.consumer.accept(var1, this.val1, this.val2, this.val3, this.val4, this.val5);
         }

         public static Consumers.Params5.CallbackStackItem alloc(Object var0, Object var1, Object var2, Object var3, Object var4, Consumers.Params5.ICallback var5) {
            Consumers.Params5.CallbackStackItem var6 = (Consumers.Params5.CallbackStackItem)s_pool.alloc();
            var6.val1 = var0;
            var6.val2 = var1;
            var6.val3 = var2;
            var6.val4 = var3;
            var6.val5 = var4;
            var6.consumer = var5;
            return var6;
         }

         public void onReleased() {
            this.val1 = null;
            this.val2 = null;
            this.val3 = null;
            this.val4 = null;
            this.val5 = null;
            this.consumer = null;
         }
      }

      private static class StackItem extends PooledObject {
         Object val1;
         Object val2;
         Object val3;
         Object val4;
         Object val5;
      }

      public interface ICallback {
         void accept(Object var1, Object var2, Object var3, Object var4, Object var5, Object var6);
      }
   }

   public static final class Params4 {
      public static final class CallbackStackItem extends Consumers.Params4.StackItem implements Consumer {
         private Consumers.Params4.ICallback consumer;
         private static final Pool s_pool = new Pool(Consumers.Params4.CallbackStackItem::new);

         public void accept(Object var1) {
            this.consumer.accept(var1, this.val1, this.val2, this.val3, this.val4);
         }

         public static Consumers.Params4.CallbackStackItem alloc(Object var0, Object var1, Object var2, Object var3, Consumers.Params4.ICallback var4) {
            Consumers.Params4.CallbackStackItem var5 = (Consumers.Params4.CallbackStackItem)s_pool.alloc();
            var5.val1 = var0;
            var5.val2 = var1;
            var5.val3 = var2;
            var5.val4 = var3;
            var5.consumer = var4;
            return var5;
         }

         public void onReleased() {
            this.val1 = null;
            this.val2 = null;
            this.val3 = null;
            this.val4 = null;
            this.consumer = null;
         }
      }

      private static class StackItem extends PooledObject {
         Object val1;
         Object val2;
         Object val3;
         Object val4;
      }

      public interface ICallback {
         void accept(Object var1, Object var2, Object var3, Object var4, Object var5);
      }
   }

   public static final class Params3 {
      public static final class CallbackStackItem extends Consumers.Params3.StackItem implements Consumer {
         private Consumers.Params3.ICallback consumer;
         private static final Pool s_pool = new Pool(Consumers.Params3.CallbackStackItem::new);

         public void accept(Object var1) {
            this.consumer.accept(var1, this.val1, this.val2, this.val3);
         }

         public static Consumers.Params3.CallbackStackItem alloc(Object var0, Object var1, Object var2, Consumers.Params3.ICallback var3) {
            Consumers.Params3.CallbackStackItem var4 = (Consumers.Params3.CallbackStackItem)s_pool.alloc();
            var4.val1 = var0;
            var4.val2 = var1;
            var4.val3 = var2;
            var4.consumer = var3;
            return var4;
         }

         public void onReleased() {
            this.val1 = null;
            this.val2 = null;
            this.val3 = null;
            this.consumer = null;
         }
      }

      private static class StackItem extends PooledObject {
         Object val1;
         Object val2;
         Object val3;
      }

      public interface ICallback {
         void accept(Object var1, Object var2, Object var3, Object var4);
      }
   }

   public static class Params2 {
      public static final class CallbackStackItem extends Consumers.Params2.StackItem implements Consumer {
         private Consumers.Params2.ICallback consumer;
         private static final Pool s_pool = new Pool(Consumers.Params2.CallbackStackItem::new);

         public void accept(Object var1) {
            this.consumer.accept(var1, this.val1, this.val2);
         }

         public static Consumers.Params2.CallbackStackItem alloc(Object var0, Object var1, Consumers.Params2.ICallback var2) {
            Consumers.Params2.CallbackStackItem var3 = (Consumers.Params2.CallbackStackItem)s_pool.alloc();
            var3.val1 = var0;
            var3.val2 = var1;
            var3.consumer = var2;
            return var3;
         }

         public void onReleased() {
            this.val1 = null;
            this.val2 = null;
            this.consumer = null;
         }
      }

      private static class StackItem extends PooledObject {
         Object val1;
         Object val2;
      }

      public interface ICallback {
         void accept(Object var1, Object var2, Object var3);
      }
   }

   public static final class Params1 {
      public static final class CallbackStackItem extends Consumers.Params1.StackItem implements Consumer {
         private Consumers.Params1.ICallback consumer;
         private static final Pool s_pool = new Pool(Consumers.Params1.CallbackStackItem::new);

         public void accept(Object var1) {
            this.consumer.accept(var1, this.val1);
         }

         public static Consumers.Params1.CallbackStackItem alloc(Object var0, Consumers.Params1.ICallback var1) {
            Consumers.Params1.CallbackStackItem var2 = (Consumers.Params1.CallbackStackItem)s_pool.alloc();
            var2.val1 = var0;
            var2.consumer = var1;
            return var2;
         }

         public void onReleased() {
            this.val1 = null;
            this.consumer = null;
         }
      }

      private static class StackItem extends PooledObject {
         Object val1;
      }

      public interface ICallback {
         void accept(Object var1, Object var2);
      }
   }
}

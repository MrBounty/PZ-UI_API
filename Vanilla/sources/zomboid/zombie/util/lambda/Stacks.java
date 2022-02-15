package zombie.util.lambda;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;
import zombie.util.IPooledObject;
import zombie.util.Lambda;
import zombie.util.Pool;
import zombie.util.PooledObject;

public final class Stacks {
   public static final class Params6 {
      public static final class CallbackStackItem extends Stacks.Params6.StackItem {
         private Stacks.Params6.ICallback callback;
         private static final Pool s_pool = new Pool(Stacks.Params6.CallbackStackItem::new);

         public void invoke() {
            this.callback.accept(this, this.val1, this.val2, this.val3, this.val4, this.val5, this.val6);
         }

         public static Stacks.Params6.CallbackStackItem alloc(Object var0, Object var1, Object var2, Object var3, Object var4, Object var5, Stacks.Params6.ICallback var6) {
            Stacks.Params6.CallbackStackItem var7 = (Stacks.Params6.CallbackStackItem)s_pool.alloc();
            var7.val1 = var0;
            var7.val2 = var1;
            var7.val3 = var2;
            var7.val4 = var3;
            var7.val5 = var4;
            var7.val6 = var5;
            var7.callback = var6;
            return var7;
         }

         public void onReleased() {
            this.val1 = null;
            this.val2 = null;
            this.val3 = null;
            this.val4 = null;
            this.val5 = null;
            this.val6 = null;
            this.callback = null;
            super.onReleased();
         }
      }

      private abstract static class StackItem extends Stacks.GenericStack {
         Object val1;
         Object val2;
         Object val3;
         Object val4;
         Object val5;
         Object val6;
      }

      public interface ICallback {
         void accept(Stacks.GenericStack var1, Object var2, Object var3, Object var4, Object var5, Object var6, Object var7);
      }
   }

   public static final class Params5 {
      public static final class CallbackStackItem extends Stacks.Params5.StackItem {
         private Stacks.Params5.ICallback callback;
         private static final Pool s_pool = new Pool(Stacks.Params5.CallbackStackItem::new);

         public void invoke() {
            this.callback.accept(this, this.val1, this.val2, this.val3, this.val4, this.val5);
         }

         public static Stacks.Params5.CallbackStackItem alloc(Object var0, Object var1, Object var2, Object var3, Object var4, Stacks.Params5.ICallback var5) {
            Stacks.Params5.CallbackStackItem var6 = (Stacks.Params5.CallbackStackItem)s_pool.alloc();
            var6.val1 = var0;
            var6.val2 = var1;
            var6.val3 = var2;
            var6.val4 = var3;
            var6.val5 = var4;
            var6.callback = var5;
            return var6;
         }

         public void onReleased() {
            this.val1 = null;
            this.val2 = null;
            this.val3 = null;
            this.val4 = null;
            this.val5 = null;
            this.callback = null;
            super.onReleased();
         }
      }

      private abstract static class StackItem extends Stacks.GenericStack {
         Object val1;
         Object val2;
         Object val3;
         Object val4;
         Object val5;
      }

      public interface ICallback {
         void accept(Stacks.GenericStack var1, Object var2, Object var3, Object var4, Object var5, Object var6);
      }
   }

   public static final class Params4 {
      public static final class CallbackStackItem extends Stacks.Params4.StackItem {
         private Stacks.Params4.ICallback callback;
         private static final Pool s_pool = new Pool(Stacks.Params4.CallbackStackItem::new);

         public void invoke() {
            this.callback.accept(this, this.val1, this.val2, this.val3, this.val4);
         }

         public static Stacks.Params4.CallbackStackItem alloc(Object var0, Object var1, Object var2, Object var3, Stacks.Params4.ICallback var4) {
            Stacks.Params4.CallbackStackItem var5 = (Stacks.Params4.CallbackStackItem)s_pool.alloc();
            var5.val1 = var0;
            var5.val2 = var1;
            var5.val3 = var2;
            var5.val4 = var3;
            var5.callback = var4;
            return var5;
         }

         public void onReleased() {
            this.val1 = null;
            this.val2 = null;
            this.val3 = null;
            this.val4 = null;
            this.callback = null;
            super.onReleased();
         }
      }

      private abstract static class StackItem extends Stacks.GenericStack {
         Object val1;
         Object val2;
         Object val3;
         Object val4;
      }

      public interface ICallback {
         void accept(Stacks.GenericStack var1, Object var2, Object var3, Object var4, Object var5);
      }
   }

   public static final class Params3 {
      public static final class CallbackStackItem extends Stacks.Params3.StackItem {
         private Stacks.Params3.ICallback callback;
         private static final Pool s_pool = new Pool(Stacks.Params3.CallbackStackItem::new);

         public void invoke() {
            this.callback.accept(this, this.val1, this.val2, this.val3);
         }

         public static Stacks.Params3.CallbackStackItem alloc(Object var0, Object var1, Object var2, Stacks.Params3.ICallback var3) {
            Stacks.Params3.CallbackStackItem var4 = (Stacks.Params3.CallbackStackItem)s_pool.alloc();
            var4.val1 = var0;
            var4.val2 = var1;
            var4.val3 = var2;
            var4.callback = var3;
            return var4;
         }

         public void onReleased() {
            this.val1 = null;
            this.val2 = null;
            this.val3 = null;
            this.callback = null;
            super.onReleased();
         }
      }

      private abstract static class StackItem extends Stacks.GenericStack {
         Object val1;
         Object val2;
         Object val3;
      }

      public interface ICallback {
         void accept(Stacks.GenericStack var1, Object var2, Object var3, Object var4);
      }
   }

   public static final class Params2 {
      public static final class CallbackStackItem extends Stacks.Params2.StackItem {
         private Stacks.Params2.ICallback callback;
         private static final Pool s_pool = new Pool(Stacks.Params2.CallbackStackItem::new);

         public void invoke() {
            this.callback.accept(this, this.val1, this.val2);
         }

         public static Stacks.Params2.CallbackStackItem alloc(Object var0, Object var1, Stacks.Params2.ICallback var2) {
            Stacks.Params2.CallbackStackItem var3 = (Stacks.Params2.CallbackStackItem)s_pool.alloc();
            var3.val1 = var0;
            var3.val2 = var1;
            var3.callback = var2;
            return var3;
         }

         public void onReleased() {
            this.val1 = null;
            this.val2 = null;
            this.callback = null;
            super.onReleased();
         }
      }

      private abstract static class StackItem extends Stacks.GenericStack {
         Object val1;
         Object val2;
      }

      public interface ICallback {
         void accept(Stacks.GenericStack var1, Object var2, Object var3);
      }
   }

   public static final class Params1 {
      public static final class CallbackStackItem extends Stacks.Params1.StackItem {
         private Stacks.Params1.ICallback callback;
         private static final Pool s_pool = new Pool(Stacks.Params1.CallbackStackItem::new);

         public void invoke() {
            this.callback.accept(this, this.val1);
         }

         public static Stacks.Params1.CallbackStackItem alloc(Object var0, Stacks.Params1.ICallback var1) {
            Stacks.Params1.CallbackStackItem var2 = (Stacks.Params1.CallbackStackItem)s_pool.alloc();
            var2.val1 = var0;
            var2.callback = var1;
            return var2;
         }

         public void onReleased() {
            this.val1 = null;
            this.callback = null;
            super.onReleased();
         }
      }

      private abstract static class StackItem extends Stacks.GenericStack {
         Object val1;
      }

      public interface ICallback {
         void accept(Stacks.GenericStack var1, Object var2);
      }
   }

   public abstract static class GenericStack extends PooledObject {
      private final List m_stackItems = new ArrayList();

      public abstract void invoke();

      public void invokeAndRelease() {
         try {
            this.invoke();
         } finally {
            this.release();
         }

      }

      private Object push(Object var1) {
         this.m_stackItems.add((IPooledObject)var1);
         return var1;
      }

      public void onReleased() {
         this.m_stackItems.forEach(Pool::tryRelease);
         this.m_stackItems.clear();
      }

      public Predicate predicate(Object var1, Predicates.Params1.ICallback var2) {
         return (Predicate)this.push(Lambda.predicate(var1, var2));
      }

      public Predicate predicate(Object var1, Object var2, Predicates.Params2.ICallback var3) {
         return (Predicate)this.push(Lambda.predicate(var1, var2, var3));
      }

      public Predicate predicate(Object var1, Object var2, Object var3, Predicates.Params3.ICallback var4) {
         return (Predicate)this.push(Lambda.predicate(var1, var2, var3, var4));
      }

      public Comparator comparator(Object var1, Comparators.Params1.ICallback var2) {
         return (Comparator)this.push(Lambda.comparator(var1, var2));
      }

      public Comparator comparator(Object var1, Object var2, Comparators.Params2.ICallback var3) {
         return (Comparator)this.push(Lambda.comparator(var1, var2, var3));
      }

      public Consumer consumer(Object var1, Consumers.Params1.ICallback var2) {
         return (Consumer)this.push(Lambda.consumer(var1, var2));
      }

      public Consumer consumer(Object var1, Object var2, Consumers.Params2.ICallback var3) {
         return (Consumer)this.push(Lambda.consumer(var1, var2, var3));
      }

      public Runnable invoker(Object var1, Invokers.Params1.ICallback var2) {
         return (Runnable)this.push(Lambda.invoker(var1, var2));
      }

      public Runnable invoker(Object var1, Object var2, Invokers.Params2.ICallback var3) {
         return (Runnable)this.push(Lambda.invoker(var1, var2, var3));
      }

      public Runnable invoker(Object var1, Object var2, Object var3, Invokers.Params3.ICallback var4) {
         return (Runnable)this.push(Lambda.invoker(var1, var2, var3, var4));
      }

      public Runnable invoker(Object var1, Object var2, Object var3, Object var4, Invokers.Params4.ICallback var5) {
         return (Runnable)this.push(Lambda.invoker(var1, var2, var3, var4, var5));
      }
   }
}

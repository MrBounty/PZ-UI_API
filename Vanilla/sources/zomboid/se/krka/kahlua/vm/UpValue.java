package se.krka.kahlua.vm;

public final class UpValue {
   private Coroutine coroutine;
   private final int index;
   private Object value;

   public UpValue(Coroutine var1, int var2) {
      this.coroutine = var1;
      this.index = var2;
   }

   public int getIndex() {
      return this.index;
   }

   public final Object getValue() {
      return this.coroutine == null ? this.value : this.coroutine.objectStack[this.index];
   }

   public final void setValue(Object var1) {
      if (this.coroutine == null) {
         this.value = var1;
      } else {
         this.coroutine.objectStack[this.index] = var1;
      }
   }

   public void close() {
      this.value = this.coroutine.objectStack[this.index];
      this.coroutine = null;
   }
}

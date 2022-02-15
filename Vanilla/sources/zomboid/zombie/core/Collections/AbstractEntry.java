package zombie.core.Collections;

import java.util.Map.Entry;

abstract class AbstractEntry implements Entry {
   protected final Object _key;
   protected Object _val;

   public AbstractEntry(Object var1, Object var2) {
      this._key = var1;
      this._val = var2;
   }

   public AbstractEntry(Entry var1) {
      this._key = var1.getKey();
      this._val = var1.getValue();
   }

   public String toString() {
      return this._key + "=" + this._val;
   }

   public Object getKey() {
      return this._key;
   }

   public Object getValue() {
      return this._val;
   }

   public boolean equals(Object var1) {
      if (!(var1 instanceof Entry)) {
         return false;
      } else {
         Entry var2 = (Entry)var1;
         return eq(this._key, var2.getKey()) && eq(this._val, var2.getValue());
      }
   }

   public int hashCode() {
      return (this._key == null ? 0 : this._key.hashCode()) ^ (this._val == null ? 0 : this._val.hashCode());
   }

   private static boolean eq(Object var0, Object var1) {
      return var0 == null ? var1 == null : var0.equals(var1);
   }
}

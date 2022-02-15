package zombie.core.Collections;

import java.io.Serializable;
import java.util.AbstractCollection;
import java.util.AbstractSet;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

public abstract class ZomboidAbstractMap implements Map {
   transient volatile Set keySet = null;
   transient volatile Collection values = null;

   protected ZomboidAbstractMap() {
   }

   public int size() {
      return this.entrySet().size();
   }

   public boolean isEmpty() {
      return this.size() == 0;
   }

   public boolean containsValue(Object var1) {
      Iterator var2 = this.entrySet().iterator();
      Entry var3;
      if (var1 == null) {
         while(var2.hasNext()) {
            var3 = (Entry)var2.next();
            if (var3.getValue() == null) {
               return true;
            }
         }
      } else {
         while(var2.hasNext()) {
            var3 = (Entry)var2.next();
            if (var1.equals(var3.getValue())) {
               return true;
            }
         }
      }

      return false;
   }

   public boolean containsKey(Object var1) {
      Iterator var2 = this.entrySet().iterator();
      Entry var3;
      if (var1 == null) {
         while(var2.hasNext()) {
            var3 = (Entry)var2.next();
            if (var3.getKey() == null) {
               return true;
            }
         }
      } else {
         while(var2.hasNext()) {
            var3 = (Entry)var2.next();
            if (var1.equals(var3.getKey())) {
               return true;
            }
         }
      }

      return false;
   }

   public Object get(Object var1) {
      Iterator var2 = this.entrySet().iterator();
      Entry var3;
      if (var1 == null) {
         while(var2.hasNext()) {
            var3 = (Entry)var2.next();
            if (var3.getKey() == null) {
               return var3.getValue();
            }
         }
      } else {
         while(var2.hasNext()) {
            var3 = (Entry)var2.next();
            if (var1.equals(var3.getKey())) {
               return var3.getValue();
            }
         }
      }

      return null;
   }

   public Object put(Object var1, Object var2) {
      throw new UnsupportedOperationException();
   }

   public Object remove(Object var1) {
      Iterator var2 = this.entrySet().iterator();
      Entry var3 = null;
      Entry var4;
      if (var1 == null) {
         while(var3 == null && var2.hasNext()) {
            var4 = (Entry)var2.next();
            if (var4.getKey() == null) {
               var3 = var4;
            }
         }
      } else {
         while(var3 == null && var2.hasNext()) {
            var4 = (Entry)var2.next();
            if (var1.equals(var4.getKey())) {
               var3 = var4;
            }
         }
      }

      Object var5 = null;
      if (var3 != null) {
         var5 = var3.getValue();
         var2.remove();
      }

      return var5;
   }

   public void putAll(Map var1) {
      Iterator var2 = var1.entrySet().iterator();

      while(var2.hasNext()) {
         Entry var3 = (Entry)var2.next();
         this.put(var3.getKey(), var3.getValue());
      }

   }

   public void clear() {
      this.entrySet().clear();
   }

   public Set keySet() {
      if (this.keySet == null) {
         this.keySet = new AbstractSet() {
            public Iterator iterator() {
               return new Iterator() {
                  private Iterator i = ZomboidAbstractMap.this.entrySet().iterator();

                  public boolean hasNext() {
                     return this.i.hasNext();
                  }

                  public Object next() {
                     return ((Entry)this.i.next()).getKey();
                  }

                  public void remove() {
                     this.i.remove();
                  }
               };
            }

            public int size() {
               return ZomboidAbstractMap.this.size();
            }

            public boolean contains(Object var1) {
               return ZomboidAbstractMap.this.containsKey(var1);
            }
         };
      }

      return this.keySet;
   }

   public Collection values() {
      if (this.values == null) {
         this.values = new AbstractCollection() {
            public Iterator iterator() {
               return new Iterator() {
                  private Iterator i = ZomboidAbstractMap.this.entrySet().iterator();

                  public boolean hasNext() {
                     return this.i.hasNext();
                  }

                  public Object next() {
                     return ((Entry)this.i.next()).getValue();
                  }

                  public void remove() {
                     this.i.remove();
                  }
               };
            }

            public int size() {
               return ZomboidAbstractMap.this.size();
            }

            public boolean contains(Object var1) {
               return ZomboidAbstractMap.this.containsValue(var1);
            }
         };
      }

      return this.values;
   }

   public abstract Set entrySet();

   public boolean equals(Object var1) {
      if (var1 == this) {
         return true;
      } else if (!(var1 instanceof Map)) {
         return false;
      } else {
         Map var2 = (Map)var1;
         if (var2.size() != this.size()) {
            return false;
         } else {
            try {
               Iterator var3 = this.entrySet().iterator();

               Object var5;
               label43:
               do {
                  Object var6;
                  do {
                     if (!var3.hasNext()) {
                        return true;
                     }

                     Entry var4 = (Entry)var3.next();
                     var5 = var4.getKey();
                     var6 = var4.getValue();
                     if (var6 == null) {
                        continue label43;
                     }
                  } while(var6.equals(var2.get(var5)));

                  return false;
               } while(var2.get(var5) == null && var2.containsKey(var5));

               return false;
            } catch (ClassCastException var7) {
               return false;
            } catch (NullPointerException var8) {
               return false;
            }
         }
      }
   }

   public int hashCode() {
      int var1 = 0;

      for(Iterator var2 = this.entrySet().iterator(); var2.hasNext(); var1 += ((Entry)var2.next()).hashCode()) {
      }

      return var1;
   }

   public String toString() {
      Iterator var1 = this.entrySet().iterator();
      if (!var1.hasNext()) {
         return "{}";
      } else {
         StringBuilder var2 = new StringBuilder();
         var2.append('{');

         while(true) {
            Entry var3 = (Entry)var1.next();
            Object var4 = var3.getKey();
            Object var5 = var3.getValue();
            var2.append(var4 == this ? "(this Map)" : var4);
            var2.append('=');
            var2.append(var5 == this ? "(this Map)" : var5);
            if (!var1.hasNext()) {
               return var2.append('}').toString();
            }

            var2.append(", ");
         }
      }
   }

   protected Object clone() throws CloneNotSupportedException {
      ZomboidAbstractMap var1 = (ZomboidAbstractMap)super.clone();
      var1.keySet = null;
      var1.values = null;
      return var1;
   }

   private static boolean eq(Object var0, Object var1) {
      return var0 == null ? var1 == null : var0.equals(var1);
   }

   public static class SimpleImmutableEntry implements Entry, Serializable {
      private static final long serialVersionUID = 7138329143949025153L;
      private final Object key;
      private final Object value;

      public SimpleImmutableEntry(Object var1, Object var2) {
         this.key = var1;
         this.value = var2;
      }

      public SimpleImmutableEntry(Entry var1) {
         this.key = var1.getKey();
         this.value = var1.getValue();
      }

      public Object getKey() {
         return this.key;
      }

      public Object getValue() {
         return this.value;
      }

      public Object setValue(Object var1) {
         throw new UnsupportedOperationException();
      }

      public boolean equals(Object var1) {
         if (!(var1 instanceof Entry)) {
            return false;
         } else {
            Entry var2 = (Entry)var1;
            return ZomboidAbstractMap.eq(this.key, var2.getKey()) && ZomboidAbstractMap.eq(this.value, var2.getValue());
         }
      }

      public int hashCode() {
         return (this.key == null ? 0 : this.key.hashCode()) ^ (this.value == null ? 0 : this.value.hashCode());
      }

      public String toString() {
         return this.key + "=" + this.value;
      }
   }

   public static class SimpleEntry implements Entry, Serializable {
      private static final long serialVersionUID = -8499721149061103585L;
      private final Object key;
      private Object value;

      public SimpleEntry(Object var1, Object var2) {
         this.key = var1;
         this.value = var2;
      }

      public SimpleEntry(Entry var1) {
         this.key = var1.getKey();
         this.value = var1.getValue();
      }

      public Object getKey() {
         return this.key;
      }

      public Object getValue() {
         return this.value;
      }

      public Object setValue(Object var1) {
         Object var2 = this.value;
         this.value = var1;
         return var2;
      }

      public boolean equals(Object var1) {
         if (!(var1 instanceof Entry)) {
            return false;
         } else {
            Entry var2 = (Entry)var1;
            return ZomboidAbstractMap.eq(this.key, var2.getKey()) && ZomboidAbstractMap.eq(this.value, var2.getValue());
         }
      }

      public int hashCode() {
         return (this.key == null ? 0 : this.key.hashCode()) ^ (this.value == null ? 0 : this.value.hashCode());
      }

      public String toString() {
         return this.key + "=" + this.value;
      }
   }
}

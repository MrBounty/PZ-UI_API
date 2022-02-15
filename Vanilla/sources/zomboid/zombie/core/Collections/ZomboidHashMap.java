package zombie.core.Collections;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.AbstractCollection;
import java.util.AbstractSet;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.Stack;

public class ZomboidHashMap extends ZomboidAbstractMap implements Map, Cloneable, Serializable {
   static final int DEFAULT_INITIAL_CAPACITY = 16;
   static final int MAXIMUM_CAPACITY = 1073741824;
   static final float DEFAULT_LOAD_FACTOR = 0.75F;
   transient ZomboidHashMap.Entry[] table;
   transient int size;
   int threshold;
   final float loadFactor;
   transient volatile int modCount;
   Stack entryStore;
   private transient Set entrySet;
   private static final long serialVersionUID = 362498820763181265L;

   public ZomboidHashMap(int var1, float var2) {
      this.entryStore = new Stack();
      this.entrySet = null;
      if (var1 < 0) {
         throw new IllegalArgumentException("Illegal initial capacity: " + var1);
      } else {
         if (var1 > 1073741824) {
            var1 = 1073741824;
         }

         if (!(var2 <= 0.0F) && !Float.isNaN(var2)) {
            int var3;
            for(var3 = 1; var3 < var1; var3 <<= 1) {
            }

            for(int var4 = 0; var4 < 100; ++var4) {
               this.entryStore.add(new ZomboidHashMap.Entry(0, (Object)null, (Object)null, (ZomboidHashMap.Entry)null));
            }

            this.loadFactor = var2;
            this.threshold = (int)((float)var3 * var2);
            this.table = new ZomboidHashMap.Entry[var3];
            this.init();
         } else {
            throw new IllegalArgumentException("Illegal load factor: " + var2);
         }
      }
   }

   public ZomboidHashMap(int var1) {
      this(var1, 0.75F);
   }

   public ZomboidHashMap() {
      this.entryStore = new Stack();
      this.entrySet = null;
      this.loadFactor = 0.75F;
      this.threshold = 12;
      this.table = new ZomboidHashMap.Entry[16];
      this.init();
   }

   public ZomboidHashMap(Map var1) {
      this(Math.max((int)((float)var1.size() / 0.75F) + 1, 16), 0.75F);
      this.putAllForCreate(var1);
   }

   void init() {
   }

   static int hash(int var0) {
      var0 ^= var0 >>> 20 ^ var0 >>> 12;
      return var0 ^ var0 >>> 7 ^ var0 >>> 4;
   }

   static int indexFor(int var0, int var1) {
      return var0 & var1 - 1;
   }

   public int size() {
      return this.size;
   }

   public boolean isEmpty() {
      return this.size == 0;
   }

   public Object get(Object var1) {
      if (var1 == null) {
         return this.getForNullKey();
      } else {
         int var2 = hash(var1.hashCode());

         for(ZomboidHashMap.Entry var3 = this.table[indexFor(var2, this.table.length)]; var3 != null; var3 = var3.next) {
            Object var4;
            if (var3.hash == var2 && ((var4 = var3.key) == var1 || var1.equals(var4))) {
               return var3.value;
            }
         }

         return null;
      }
   }

   private Object getForNullKey() {
      for(ZomboidHashMap.Entry var1 = this.table[0]; var1 != null; var1 = var1.next) {
         if (var1.key == null) {
            return var1.value;
         }
      }

      return null;
   }

   public boolean containsKey(Object var1) {
      return this.getEntry(var1) != null;
   }

   final ZomboidHashMap.Entry getEntry(Object var1) {
      int var2 = var1 == null ? 0 : hash(var1.hashCode());

      for(ZomboidHashMap.Entry var3 = this.table[indexFor(var2, this.table.length)]; var3 != null; var3 = var3.next) {
         Object var4;
         if (var3.hash == var2 && ((var4 = var3.key) == var1 || var1 != null && var1.equals(var4))) {
            return var3;
         }
      }

      return null;
   }

   public Object put(Object var1, Object var2) {
      if (var1 == null) {
         return this.putForNullKey(var2);
      } else {
         int var3 = hash(var1.hashCode());
         int var4 = indexFor(var3, this.table.length);

         for(ZomboidHashMap.Entry var5 = this.table[var4]; var5 != null; var5 = var5.next) {
            Object var6;
            if (var5.hash == var3 && ((var6 = var5.key) == var1 || var1.equals(var6))) {
               Object var7 = var5.value;
               var5.value = var2;
               var5.recordAccess(this);
               return var7;
            }
         }

         ++this.modCount;
         this.addEntry(var3, var1, var2, var4);
         return null;
      }
   }

   private Object putForNullKey(Object var1) {
      for(ZomboidHashMap.Entry var2 = this.table[0]; var2 != null; var2 = var2.next) {
         if (var2.key == null) {
            Object var3 = var2.value;
            var2.value = var1;
            var2.recordAccess(this);
            return var3;
         }
      }

      ++this.modCount;
      this.addEntry(0, (Object)null, var1, 0);
      return null;
   }

   private void putForCreate(Object var1, Object var2) {
      int var3 = var1 == null ? 0 : hash(var1.hashCode());
      int var4 = indexFor(var3, this.table.length);

      for(ZomboidHashMap.Entry var5 = this.table[var4]; var5 != null; var5 = var5.next) {
         Object var6;
         if (var5.hash == var3 && ((var6 = var5.key) == var1 || var1 != null && var1.equals(var6))) {
            var5.value = var2;
            return;
         }
      }

      this.createEntry(var3, var1, var2, var4);
   }

   private void putAllForCreate(Map var1) {
      Iterator var2 = var1.entrySet().iterator();

      while(var2.hasNext()) {
         java.util.Map.Entry var3 = (java.util.Map.Entry)var2.next();
         this.putForCreate(var3.getKey(), var3.getValue());
      }

   }

   void resize(int var1) {
      ZomboidHashMap.Entry[] var2 = this.table;
      int var3 = var2.length;
      if (var3 == 1073741824) {
         this.threshold = Integer.MAX_VALUE;
      } else {
         ZomboidHashMap.Entry[] var4 = new ZomboidHashMap.Entry[var1];
         this.transfer(var4);
         this.table = var4;
         this.threshold = (int)((float)var1 * this.loadFactor);
      }
   }

   void transfer(ZomboidHashMap.Entry[] var1) {
      ZomboidHashMap.Entry[] var2 = this.table;
      int var3 = var1.length;

      for(int var4 = 0; var4 < var2.length; ++var4) {
         ZomboidHashMap.Entry var5 = var2[var4];
         if (var5 != null) {
            var2[var4] = null;

            ZomboidHashMap.Entry var6;
            do {
               var6 = var5.next;
               int var7 = indexFor(var5.hash, var3);
               var5.next = var1[var7];
               var1[var7] = var5;
               var5 = var6;
            } while(var6 != null);
         }
      }

   }

   public void putAll(Map var1) {
      int var2 = var1.size();
      if (var2 != 0) {
         if (var2 > this.threshold) {
            int var3 = (int)((float)var2 / this.loadFactor + 1.0F);
            if (var3 > 1073741824) {
               var3 = 1073741824;
            }

            int var4;
            for(var4 = this.table.length; var4 < var3; var4 <<= 1) {
            }

            if (var4 > this.table.length) {
               this.resize(var4);
            }
         }

         Iterator var5 = var1.entrySet().iterator();

         while(var5.hasNext()) {
            java.util.Map.Entry var6 = (java.util.Map.Entry)var5.next();
            this.put(var6.getKey(), var6.getValue());
         }

      }
   }

   public Object remove(Object var1) {
      ZomboidHashMap.Entry var2 = this.removeEntryForKey(var1);
      return var2 == null ? null : var2.value;
   }

   final ZomboidHashMap.Entry removeEntryForKey(Object var1) {
      int var2 = var1 == null ? 0 : hash(var1.hashCode());
      int var3 = indexFor(var2, this.table.length);
      ZomboidHashMap.Entry var4 = this.table[var3];

      ZomboidHashMap.Entry var5;
      ZomboidHashMap.Entry var6;
      for(var5 = var4; var5 != null; var5 = var6) {
         var6 = var5.next;
         Object var7;
         if (var5.hash == var2 && ((var7 = var5.key) == var1 || var1 != null && var1.equals(var7))) {
            ++this.modCount;
            --this.size;
            if (var4 == var5) {
               this.table[var3] = var6;
            } else {
               var4.next = var6;
            }

            var5.recordRemoval(this);
            var5.value = null;
            var5.next = null;
            this.entryStore.push(var5);
            return var5;
         }

         var4 = var5;
      }

      return var5;
   }

   final ZomboidHashMap.Entry removeMapping(Object var1) {
      if (!(var1 instanceof java.util.Map.Entry)) {
         return null;
      } else {
         java.util.Map.Entry var2 = (java.util.Map.Entry)var1;
         Object var3 = var2.getKey();
         int var4 = var3 == null ? 0 : hash(var3.hashCode());
         int var5 = indexFor(var4, this.table.length);
         ZomboidHashMap.Entry var6 = this.table[var5];

         ZomboidHashMap.Entry var7;
         ZomboidHashMap.Entry var8;
         for(var7 = var6; var7 != null; var7 = var8) {
            var8 = var7.next;
            if (var7.hash == var4 && var7.equals(var2)) {
               ++this.modCount;
               --this.size;
               if (var6 == var7) {
                  this.table[var5] = var8;
               } else {
                  var6.next = var8;
               }

               var7.recordRemoval(this);
               var7.value = null;
               var7.next = null;
               this.entryStore.push(var7);
               return var7;
            }

            var6 = var7;
         }

         return var7;
      }
   }

   public void clear() {
      ++this.modCount;
      ZomboidHashMap.Entry[] var1 = this.table;

      for(int var2 = 0; var2 < var1.length; ++var2) {
         if (var1[var2] != null) {
            var1[var2].value = null;
            var1[var2].next = null;
            this.entryStore.push(var1[var2]);
         }

         var1[var2] = null;
      }

      this.size = 0;
   }

   public boolean containsValue(Object var1) {
      if (var1 == null) {
         return this.containsNullValue();
      } else {
         ZomboidHashMap.Entry[] var2 = this.table;

         for(int var3 = 0; var3 < var2.length; ++var3) {
            for(ZomboidHashMap.Entry var4 = var2[var3]; var4 != null; var4 = var4.next) {
               if (var1.equals(var4.value)) {
                  return true;
               }
            }
         }

         return false;
      }
   }

   private boolean containsNullValue() {
      ZomboidHashMap.Entry[] var1 = this.table;

      for(int var2 = 0; var2 < var1.length; ++var2) {
         for(ZomboidHashMap.Entry var3 = var1[var2]; var3 != null; var3 = var3.next) {
            if (var3.value == null) {
               return true;
            }
         }
      }

      return false;
   }

   public Object clone() {
      ZomboidHashMap var1 = null;

      try {
         var1 = (ZomboidHashMap)super.clone();
      } catch (CloneNotSupportedException var3) {
      }

      var1.table = new ZomboidHashMap.Entry[this.table.length];
      var1.entrySet = null;
      var1.modCount = 0;
      var1.size = 0;
      var1.init();
      var1.putAllForCreate(this);
      return var1;
   }

   void addEntry(int var1, Object var2, Object var3, int var4) {
      ZomboidHashMap.Entry var5 = this.table[var4];
      if (this.entryStore.isEmpty()) {
         for(int var6 = 0; var6 < 100; ++var6) {
            this.entryStore.add(new ZomboidHashMap.Entry(0, (Object)null, (Object)null, (ZomboidHashMap.Entry)null));
         }
      }

      ZomboidHashMap.Entry var7 = (ZomboidHashMap.Entry)this.entryStore.pop();
      var7.hash = var1;
      var7.key = var2;
      var7.value = var3;
      var7.next = var5;
      this.table[var4] = var7;
      if (this.size++ >= this.threshold) {
         this.resize(2 * this.table.length);
      }

   }

   void createEntry(int var1, Object var2, Object var3, int var4) {
      ZomboidHashMap.Entry var5 = this.table[var4];
      if (this.entryStore.isEmpty()) {
         for(int var6 = 0; var6 < 100; ++var6) {
            this.entryStore.add(new ZomboidHashMap.Entry(0, (Object)null, (Object)null, (ZomboidHashMap.Entry)null));
         }
      }

      ZomboidHashMap.Entry var7 = (ZomboidHashMap.Entry)this.entryStore.pop();
      var7.hash = var1;
      var7.key = var2;
      var7.value = var3;
      var7.next = var5;
      this.table[var4] = var7;
      ++this.size;
   }

   Iterator newKeyIterator() {
      return new ZomboidHashMap.KeyIterator();
   }

   Iterator newValueIterator() {
      return new ZomboidHashMap.ValueIterator();
   }

   Iterator newEntryIterator() {
      return new ZomboidHashMap.EntryIterator();
   }

   public Set keySet() {
      Set var1 = this.keySet;
      return var1 != null ? var1 : (this.keySet = new ZomboidHashMap.KeySet());
   }

   public Collection values() {
      Collection var1 = this.values;
      return var1 != null ? var1 : (this.values = new ZomboidHashMap.Values());
   }

   public Set entrySet() {
      return this.entrySet0();
   }

   private Set entrySet0() {
      Set var1 = this.entrySet;
      return var1 != null ? var1 : (this.entrySet = new ZomboidHashMap.EntrySet());
   }

   private void writeObject(ObjectOutputStream var1) throws IOException {
      Iterator var2 = this.size > 0 ? this.entrySet0().iterator() : null;
      var1.defaultWriteObject();
      var1.writeInt(this.table.length);
      var1.writeInt(this.size);
      if (var2 != null) {
         while(var2.hasNext()) {
            java.util.Map.Entry var3 = (java.util.Map.Entry)var2.next();
            var1.writeObject(var3.getKey());
            var1.writeObject(var3.getValue());
         }
      }

   }

   private void readObject(ObjectInputStream var1) throws IOException, ClassNotFoundException {
      var1.defaultReadObject();
      int var2 = var1.readInt();
      this.table = new ZomboidHashMap.Entry[var2];
      this.init();
      int var3 = var1.readInt();

      for(int var4 = 0; var4 < var3; ++var4) {
         Object var5 = var1.readObject();
         Object var6 = var1.readObject();
         this.putForCreate(var5, var6);
      }

   }

   int capacity() {
      return this.table.length;
   }

   float loadFactor() {
      return this.loadFactor;
   }

   static class Entry implements java.util.Map.Entry {
      Object key;
      Object value;
      ZomboidHashMap.Entry next;
      int hash;

      Entry(int var1, Object var2, Object var3, ZomboidHashMap.Entry var4) {
         this.value = var3;
         this.next = var4;
         this.key = var2;
         this.hash = var1;
      }

      public final Object getKey() {
         return this.key;
      }

      public final Object getValue() {
         return this.value;
      }

      public final Object setValue(Object var1) {
         Object var2 = this.value;
         this.value = var1;
         return var2;
      }

      public final boolean equals(Object var1) {
         if (!(var1 instanceof java.util.Map.Entry)) {
            return false;
         } else {
            java.util.Map.Entry var2 = (java.util.Map.Entry)var1;
            Object var3 = this.getKey();
            Object var4 = var2.getKey();
            if (var3 == var4 || var3 != null && var3.equals(var4)) {
               Object var5 = this.getValue();
               Object var6 = var2.getValue();
               if (var5 == var6 || var5 != null && var5.equals(var6)) {
                  return true;
               }
            }

            return false;
         }
      }

      public final int hashCode() {
         return (this.key == null ? 0 : this.key.hashCode()) ^ (this.value == null ? 0 : this.value.hashCode());
      }

      public final String toString() {
         Object var10000 = this.getKey();
         return var10000 + "=" + this.getValue();
      }

      void recordAccess(ZomboidHashMap var1) {
      }

      void recordRemoval(ZomboidHashMap var1) {
      }
   }

   private final class KeyIterator extends ZomboidHashMap.HashIterator {
      private KeyIterator() {
         super();
      }

      public Object next() {
         return this.nextEntry().getKey();
      }
   }

   private final class ValueIterator extends ZomboidHashMap.HashIterator {
      private ValueIterator() {
         super();
      }

      public Object next() {
         return this.nextEntry().value;
      }
   }

   private final class EntryIterator extends ZomboidHashMap.HashIterator {
      private EntryIterator() {
         super();
      }

      public java.util.Map.Entry next() {
         return this.nextEntry();
      }
   }

   private final class KeySet extends AbstractSet {
      public Iterator iterator() {
         return ZomboidHashMap.this.newKeyIterator();
      }

      public int size() {
         return ZomboidHashMap.this.size;
      }

      public boolean contains(Object var1) {
         return ZomboidHashMap.this.containsKey(var1);
      }

      public boolean remove(Object var1) {
         return ZomboidHashMap.this.removeEntryForKey(var1) != null;
      }

      public void clear() {
         ZomboidHashMap.this.clear();
      }
   }

   private final class Values extends AbstractCollection {
      public Iterator iterator() {
         return ZomboidHashMap.this.newValueIterator();
      }

      public int size() {
         return ZomboidHashMap.this.size;
      }

      public boolean contains(Object var1) {
         return ZomboidHashMap.this.containsValue(var1);
      }

      public void clear() {
         ZomboidHashMap.this.clear();
      }
   }

   private final class EntrySet extends AbstractSet {
      public Iterator iterator() {
         return ZomboidHashMap.this.newEntryIterator();
      }

      public boolean contains(Object var1) {
         if (!(var1 instanceof java.util.Map.Entry)) {
            return false;
         } else {
            java.util.Map.Entry var2 = (java.util.Map.Entry)var1;
            ZomboidHashMap.Entry var3 = ZomboidHashMap.this.getEntry(var2.getKey());
            return var3 != null && var3.equals(var2);
         }
      }

      public boolean remove(Object var1) {
         return ZomboidHashMap.this.removeMapping(var1) != null;
      }

      public int size() {
         return ZomboidHashMap.this.size;
      }

      public void clear() {
         ZomboidHashMap.this.clear();
      }
   }

   private abstract class HashIterator implements Iterator {
      ZomboidHashMap.Entry next;
      int expectedModCount;
      int index;
      ZomboidHashMap.Entry current;

      HashIterator() {
         this.expectedModCount = ZomboidHashMap.this.modCount;
         if (ZomboidHashMap.this.size > 0) {
            ZomboidHashMap.Entry[] var2 = ZomboidHashMap.this.table;

            while(this.index < var2.length && (this.next = var2[this.index++]) == null) {
            }
         }

      }

      public final boolean hasNext() {
         return this.next != null;
      }

      final ZomboidHashMap.Entry nextEntry() {
         if (ZomboidHashMap.this.modCount != this.expectedModCount) {
            throw new ConcurrentModificationException();
         } else {
            ZomboidHashMap.Entry var1 = this.next;
            if (var1 == null) {
               throw new NoSuchElementException();
            } else {
               if ((this.next = var1.next) == null) {
                  ZomboidHashMap.Entry[] var2 = ZomboidHashMap.this.table;

                  while(this.index < var2.length && (this.next = var2[this.index++]) == null) {
                  }
               }

               this.current = var1;
               return var1;
            }
         }
      }

      public void remove() {
         if (this.current == null) {
            throw new IllegalStateException();
         } else if (ZomboidHashMap.this.modCount != this.expectedModCount) {
            throw new ConcurrentModificationException();
         } else {
            Object var1 = this.current.key;
            this.current = null;
            ZomboidHashMap.this.removeEntryForKey(var1);
            this.expectedModCount = ZomboidHashMap.this.modCount;
         }
      }
   }
}

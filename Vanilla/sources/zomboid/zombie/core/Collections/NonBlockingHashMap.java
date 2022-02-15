package zombie.core.Collections;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.AbstractCollection;
import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLongFieldUpdater;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;
import sun.misc.Unsafe;

public class NonBlockingHashMap extends AbstractMap implements ConcurrentMap, Cloneable, Serializable {
   private static final long serialVersionUID = 1234123412341234123L;
   private static final int REPROBE_LIMIT = 10;
   private static final Unsafe _unsafe = UtilUnsafe.getUnsafe();
   private static final int _Obase;
   private static final int _Oscale;
   private static final long _kvs_offset;
   private transient Object[] _kvs;
   private transient long _last_resize_milli;
   private static final int MIN_SIZE_LOG = 3;
   private static final int MIN_SIZE = 8;
   private static final Object NO_MATCH_OLD;
   private static final Object MATCH_ANY;
   private static final Object TOMBSTONE;
   private static final NonBlockingHashMap.Prime TOMBPRIME;
   private transient Counter _reprobes;

   private static long rawIndex(Object[] var0, int var1) {
      assert var1 >= 0 && var1 < var0.length;

      return (long)(_Obase + var1 * _Oscale);
   }

   private final boolean CAS_kvs(Object[] var1, Object[] var2) {
      return _unsafe.compareAndSwapObject(this, _kvs_offset, var1, var2);
   }

   private static final int hash(Object var0) {
      int var1 = var0.hashCode();
      var1 += var1 << 15 ^ -12931;
      var1 ^= var1 >>> 10;
      var1 += var1 << 3;
      var1 ^= var1 >>> 6;
      var1 += (var1 << 2) + (var1 << 14);
      return var1 ^ var1 >>> 16;
   }

   private static final NonBlockingHashMap.CHM chm(Object[] var0) {
      return (NonBlockingHashMap.CHM)var0[0];
   }

   private static final int[] hashes(Object[] var0) {
      return (int[])var0[1];
   }

   private static final int len(Object[] var0) {
      return var0.length - 2 >> 1;
   }

   private static final Object key(Object[] var0, int var1) {
      return var0[(var1 << 1) + 2];
   }

   private static final Object val(Object[] var0, int var1) {
      return var0[(var1 << 1) + 3];
   }

   private static final boolean CAS_key(Object[] var0, int var1, Object var2, Object var3) {
      return _unsafe.compareAndSwapObject(var0, rawIndex(var0, (var1 << 1) + 2), var2, var3);
   }

   private static final boolean CAS_val(Object[] var0, int var1, Object var2, Object var3) {
      return _unsafe.compareAndSwapObject(var0, rawIndex(var0, (var1 << 1) + 3), var2, var3);
   }

   public final void print() {
      System.out.println("=========");
      this.print2(this._kvs);
      System.out.println("=========");
   }

   private final void print(Object[] var1) {
      for(int var2 = 0; var2 < len(var1); ++var2) {
         Object var3 = key(var1, var2);
         if (var3 != null) {
            String var4 = var3 == TOMBSTONE ? "XXX" : var3.toString();
            Object var5 = val(var1, var2);
            Object var6 = NonBlockingHashMap.Prime.unbox(var5);
            String var7 = var5 == var6 ? "" : "prime_";
            String var8 = var6 == TOMBSTONE ? "tombstone" : var6.toString();
            System.out.println(var2 + " (" + var4 + "," + var7 + var8 + ")");
         }
      }

      Object[] var9 = chm(var1)._newkvs;
      if (var9 != null) {
         System.out.println("----");
         this.print(var9);
      }

   }

   private final void print2(Object[] var1) {
      for(int var2 = 0; var2 < len(var1); ++var2) {
         Object var3 = key(var1, var2);
         Object var4 = val(var1, var2);
         Object var5 = NonBlockingHashMap.Prime.unbox(var4);
         if (var3 != null && var3 != TOMBSTONE && var4 != null && var5 != TOMBSTONE) {
            String var6 = var4 == var5 ? "" : "prime_";
            System.out.println(var2 + " (" + var3 + "," + var6 + var4 + ")");
         }
      }

      Object[] var7 = chm(var1)._newkvs;
      if (var7 != null) {
         System.out.println("----");
         this.print2(var7);
      }

   }

   public long reprobes() {
      long var1 = this._reprobes.get();
      this._reprobes = new Counter();
      return var1;
   }

   private static final int reprobe_limit(int var0) {
      return 10 + (var0 >> 2);
   }

   public NonBlockingHashMap() {
      this(8);
   }

   public NonBlockingHashMap(int var1) {
      this._reprobes = new Counter();
      this.initialize(var1);
   }

   private final void initialize(int var1) {
      if (var1 < 0) {
         throw new IllegalArgumentException();
      } else {
         if (var1 > 1048576) {
            var1 = 1048576;
         }

         int var2;
         for(var2 = 3; 1 << var2 < var1 << 2; ++var2) {
         }

         this._kvs = new Object[(1 << var2 << 1) + 2];
         this._kvs[0] = new NonBlockingHashMap.CHM(new Counter());
         this._kvs[1] = new int[1 << var2];
         this._last_resize_milli = System.currentTimeMillis();
      }
   }

   protected final void initialize() {
      this.initialize(8);
   }

   public int size() {
      return chm(this._kvs).size();
   }

   public boolean isEmpty() {
      return this.size() == 0;
   }

   public boolean containsKey(Object var1) {
      return this.get(var1) != null;
   }

   public boolean contains(Object var1) {
      return this.containsValue(var1);
   }

   public Object put(Object var1, Object var2) {
      return this.putIfMatch(var1, var2, NO_MATCH_OLD);
   }

   public Object putIfAbsent(Object var1, Object var2) {
      return this.putIfMatch(var1, var2, TOMBSTONE);
   }

   public Object remove(Object var1) {
      return this.putIfMatch(var1, TOMBSTONE, NO_MATCH_OLD);
   }

   public boolean remove(Object var1, Object var2) {
      Object var3 = this.putIfMatch(var1, TOMBSTONE, var2);
      return var2 == null ? var3 == var2 : var2.equals(var3);
   }

   public Object replace(Object var1, Object var2) {
      return this.putIfMatch(var1, var2, MATCH_ANY);
   }

   public boolean replace(Object var1, Object var2, Object var3) {
      Object var4 = this.putIfMatch(var1, var3, var2);
      return var2 == null ? var4 == var2 : var2.equals(var4);
   }

   private final Object putIfMatch(Object var1, Object var2, Object var3) {
      if (var3 != null && var2 != null) {
         Object var4 = putIfMatch(this, this._kvs, var1, var2, var3);

         assert !(var4 instanceof NonBlockingHashMap.Prime);

         assert var4 != null;

         return var4 == TOMBSTONE ? null : var4;
      } else {
         throw new NullPointerException();
      }
   }

   public void putAll(Map var1) {
      Iterator var2 = var1.entrySet().iterator();

      while(var2.hasNext()) {
         Entry var3 = (Entry)var2.next();
         this.put(var3.getKey(), var3.getValue());
      }

   }

   public void clear() {
      Object[] var1 = (new NonBlockingHashMap(8))._kvs;

      while(!this.CAS_kvs(this._kvs, var1)) {
      }

   }

   public boolean containsValue(Object var1) {
      if (var1 == null) {
         throw new NullPointerException();
      } else {
         Iterator var2 = this.values().iterator();

         Object var3;
         do {
            if (!var2.hasNext()) {
               return false;
            }

            var3 = var2.next();
         } while(var3 != var1 && !var3.equals(var1));

         return true;
      }
   }

   protected void rehash() {
   }

   public Object clone() {
      try {
         NonBlockingHashMap var1 = (NonBlockingHashMap)super.clone();
         var1.clear();
         Iterator var2 = this.keySet().iterator();

         while(var2.hasNext()) {
            Object var3 = var2.next();
            Object var4 = this.get(var3);
            var1.put(var3, var4);
         }

         return var1;
      } catch (CloneNotSupportedException var5) {
         throw new InternalError();
      }
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

   private static boolean keyeq(Object var0, Object var1, int[] var2, int var3, int var4) {
      return var0 == var1 || (var2[var3] == 0 || var2[var3] == var4) && var0 != TOMBSTONE && var1.equals(var0);
   }

   public Object get(Object var1) {
      int var2 = hash(var1);
      Object var3 = get_impl(this, this._kvs, var1, var2);

      assert !(var3 instanceof NonBlockingHashMap.Prime);

      return var3;
   }

   private static final Object get_impl(NonBlockingHashMap var0, Object[] var1, Object var2, int var3) {
      int var4 = len(var1);
      NonBlockingHashMap.CHM var5 = chm(var1);
      int[] var6 = hashes(var1);
      int var7 = var3 & var4 - 1;
      int var8 = 0;

      while(true) {
         Object var9 = key(var1, var7);
         Object var10 = val(var1, var7);
         if (var9 == null) {
            return null;
         }

         Object[] var11 = var5._newkvs;
         if (keyeq(var9, var2, var6, var7, var3)) {
            if (!(var10 instanceof NonBlockingHashMap.Prime)) {
               return var10 == TOMBSTONE ? null : var10;
            }

            return get_impl(var0, var5.copy_slot_and_check(var0, var1, var7, var2), var2, var3);
         }

         ++var8;
         if (var8 >= reprobe_limit(var4) || var2 == TOMBSTONE) {
            return var11 == null ? null : get_impl(var0, var0.help_copy(var11), var2, var3);
         }

         var7 = var7 + 1 & var4 - 1;
      }
   }

   private static final Object putIfMatch(NonBlockingHashMap var0, Object[] var1, Object var2, Object var3, Object var4) {
      assert var3 != null;

      assert !(var3 instanceof NonBlockingHashMap.Prime);

      assert !(var4 instanceof NonBlockingHashMap.Prime);

      int var5 = hash(var2);
      int var6 = len(var1);
      NonBlockingHashMap.CHM var7 = chm(var1);
      int[] var8 = hashes(var1);
      int var9 = var5 & var6 - 1;
      int var10 = 0;
      Object var11 = null;
      Object var12 = null;
      Object[] var13 = null;

      while(true) {
         var12 = val(var1, var9);
         var11 = key(var1, var9);
         if (var11 == null) {
            if (var3 == TOMBSTONE) {
               return var3;
            }

            if (CAS_key(var1, var9, (Object)null, var2)) {
               var7._slots.add(1L);
               var8[var9] = var5;
               break;
            }

            var11 = key(var1, var9);

            assert var11 != null;
         }

         var13 = var7._newkvs;
         if (!keyeq(var11, var2, var8, var9, var5)) {
            ++var10;
            if (var10 < reprobe_limit(var6) && var2 != TOMBSTONE) {
               var9 = var9 + 1 & var6 - 1;
               continue;
            }

            var13 = var7.resize(var0, var1);
            if (var4 != null) {
               var0.help_copy(var13);
            }

            return putIfMatch(var0, var13, var2, var3, var4);
         }
         break;
      }

      if (var3 == var12) {
         return var12;
      } else {
         if (var13 == null && (var12 == null && var7.tableFull(var10, var6) || var12 instanceof NonBlockingHashMap.Prime)) {
            var13 = var7.resize(var0, var1);
         }

         if (var13 != null) {
            return putIfMatch(var0, var7.copy_slot_and_check(var0, var1, var9, var4), var2, var3, var4);
         } else {
            do {
               assert !(var12 instanceof NonBlockingHashMap.Prime);

               if (var4 != NO_MATCH_OLD && var12 != var4 && (var4 != MATCH_ANY || var12 == TOMBSTONE || var12 == null) && (var12 != null || var4 != TOMBSTONE) && (var4 == null || !var4.equals(var12))) {
                  return var12;
               }

               if (CAS_val(var1, var9, var12, var3)) {
                  if (var4 != null) {
                     if ((var12 == null || var12 == TOMBSTONE) && var3 != TOMBSTONE) {
                        var7._size.add(1L);
                     }

                     if (var12 != null && var12 != TOMBSTONE && var3 == TOMBSTONE) {
                        var7._size.add(-1L);
                     }
                  }

                  return var12 == null && var4 != null ? TOMBSTONE : var12;
               }

               var12 = val(var1, var9);
            } while(!(var12 instanceof NonBlockingHashMap.Prime));

            return putIfMatch(var0, var7.copy_slot_and_check(var0, var1, var9, var4), var2, var3, var4);
         }
      }
   }

   private final Object[] help_copy(Object[] var1) {
      Object[] var2 = this._kvs;
      NonBlockingHashMap.CHM var3 = chm(var2);
      if (var3._newkvs == null) {
         return var1;
      } else {
         var3.help_copy_impl(this, var2, false);
         return var1;
      }
   }

   public Enumeration elements() {
      return new NonBlockingHashMap.SnapshotV();
   }

   public Collection values() {
      return new AbstractCollection() {
         public void clear() {
            NonBlockingHashMap.this.clear();
         }

         public int size() {
            return NonBlockingHashMap.this.size();
         }

         public boolean contains(Object var1) {
            return NonBlockingHashMap.this.containsValue(var1);
         }

         public Iterator iterator() {
            return NonBlockingHashMap.this.new SnapshotV();
         }
      };
   }

   public Enumeration keys() {
      return new NonBlockingHashMap.SnapshotK();
   }

   public Set keySet() {
      return new AbstractSet() {
         public void clear() {
            NonBlockingHashMap.this.clear();
         }

         public int size() {
            return NonBlockingHashMap.this.size();
         }

         public boolean contains(Object var1) {
            return NonBlockingHashMap.this.containsKey(var1);
         }

         public boolean remove(Object var1) {
            return NonBlockingHashMap.this.remove(var1) != null;
         }

         public Iterator iterator() {
            return NonBlockingHashMap.this.new SnapshotK();
         }
      };
   }

   public Set entrySet() {
      return new AbstractSet() {
         public void clear() {
            NonBlockingHashMap.this.clear();
         }

         public int size() {
            return NonBlockingHashMap.this.size();
         }

         public boolean remove(Object var1) {
            if (!(var1 instanceof Entry)) {
               return false;
            } else {
               Entry var2 = (Entry)var1;
               return NonBlockingHashMap.this.remove(var2.getKey(), var2.getValue());
            }
         }

         public boolean contains(Object var1) {
            if (!(var1 instanceof Entry)) {
               return false;
            } else {
               Entry var2 = (Entry)var1;
               Object var3 = NonBlockingHashMap.this.get(var2.getKey());
               return var3.equals(var2.getValue());
            }
         }

         public Iterator iterator() {
            return NonBlockingHashMap.this.new SnapshotE();
         }
      };
   }

   private void writeObject(ObjectOutputStream var1) throws IOException {
      var1.defaultWriteObject();
      Iterator var2 = this.keySet().iterator();

      while(var2.hasNext()) {
         Object var3 = var2.next();
         Object var4 = this.get(var3);
         var1.writeObject(var3);
         var1.writeObject(var4);
      }

      var1.writeObject((Object)null);
      var1.writeObject((Object)null);
   }

   private void readObject(ObjectInputStream var1) throws IOException, ClassNotFoundException {
      var1.defaultReadObject();
      this.initialize(8);

      while(true) {
         Object var2 = var1.readObject();
         Object var3 = var1.readObject();
         if (var2 == null) {
            return;
         }

         this.put(var2, var3);
      }
   }

   static {
      _Obase = _unsafe.arrayBaseOffset(Object[].class);
      _Oscale = _unsafe.arrayIndexScale(Object[].class);
      Field var0 = null;

      try {
         var0 = NonBlockingHashMap.class.getDeclaredField("_kvs");
      } catch (NoSuchFieldException var2) {
         throw new RuntimeException(var2);
      }

      _kvs_offset = _unsafe.objectFieldOffset(var0);
      NO_MATCH_OLD = new Object();
      MATCH_ANY = new Object();
      TOMBSTONE = new Object();
      TOMBPRIME = new NonBlockingHashMap.Prime(TOMBSTONE);
   }

   private static final class CHM {
      private final Counter _size;
      private final Counter _slots;
      volatile Object[] _newkvs;
      private final AtomicReferenceFieldUpdater _newkvsUpdater = AtomicReferenceFieldUpdater.newUpdater(NonBlockingHashMap.CHM.class, Object[].class, "_newkvs");
      volatile long _resizers;
      private static final AtomicLongFieldUpdater _resizerUpdater = AtomicLongFieldUpdater.newUpdater(NonBlockingHashMap.CHM.class, "_resizers");
      volatile long _copyIdx = 0L;
      private static final AtomicLongFieldUpdater _copyIdxUpdater = AtomicLongFieldUpdater.newUpdater(NonBlockingHashMap.CHM.class, "_copyIdx");
      volatile long _copyDone = 0L;
      private static final AtomicLongFieldUpdater _copyDoneUpdater = AtomicLongFieldUpdater.newUpdater(NonBlockingHashMap.CHM.class, "_copyDone");

      public int size() {
         return (int)this._size.get();
      }

      public int slots() {
         return (int)this._slots.get();
      }

      boolean CAS_newkvs(Object[] var1) {
         while(true) {
            if (this._newkvs == null) {
               if (!this._newkvsUpdater.compareAndSet(this, (Object)null, var1)) {
                  continue;
               }

               return true;
            }

            return false;
         }
      }

      CHM(Counter var1) {
         this._size = var1;
         this._slots = new Counter();
      }

      private final boolean tableFull(int var1, int var2) {
         return var1 >= 10 && this._slots.estimate_get() >= (long)NonBlockingHashMap.reprobe_limit(var2);
      }

      private final Object[] resize(NonBlockingHashMap var1, Object[] var2) {
         assert NonBlockingHashMap.chm(var2) == this;

         Object[] var3 = this._newkvs;
         if (var3 != null) {
            return var3;
         } else {
            int var4 = NonBlockingHashMap.len(var2);
            int var5 = this.size();
            int var6 = var5;
            if (var5 >= var4 >> 2) {
               var6 = var4 << 1;
               if (var5 >= var4 >> 1) {
                  var6 = var4 << 2;
               }
            }

            long var7 = System.currentTimeMillis();
            long var9 = 0L;
            if (var6 <= var4 && var7 <= var1._last_resize_milli + 10000L && this._slots.estimate_get() >= (long)(var5 << 1)) {
               var6 = var4 << 1;
            }

            if (var6 < var4) {
               var6 = var4;
            }

            int var11;
            for(var11 = 3; 1 << var11 < var6; ++var11) {
            }

            long var12;
            for(var12 = this._resizers; !_resizerUpdater.compareAndSet(this, var12, var12 + 1L); var12 = this._resizers) {
            }

            int var14 = (1 << var11 << 1) + 4 << 3 >> 20;
            if (var12 >= 2L && var14 > 0) {
               var3 = this._newkvs;
               if (var3 != null) {
                  return var3;
               }

               try {
                  Thread.sleep((long)(8 * var14));
               } catch (Exception var16) {
               }
            }

            var3 = this._newkvs;
            if (var3 != null) {
               return var3;
            } else {
               var3 = new Object[(1 << var11 << 1) + 2];
               var3[0] = new NonBlockingHashMap.CHM(this._size);
               var3[1] = new int[1 << var11];
               if (this._newkvs != null) {
                  return this._newkvs;
               } else {
                  if (this.CAS_newkvs(var3)) {
                     var1.rehash();
                  } else {
                     var3 = this._newkvs;
                  }

                  return var3;
               }
            }
         }
      }

      private final void help_copy_impl(NonBlockingHashMap var1, Object[] var2, boolean var3) {
         assert NonBlockingHashMap.chm(var2) == this;

         Object[] var4 = this._newkvs;

         assert var4 != null;

         int var5 = NonBlockingHashMap.len(var2);
         int var6 = Math.min(var5, 1024);
         int var7 = -1;
         int var8 = -9999;

         do {
            if (this._copyDone >= (long)var5) {
               this.copy_check_and_promote(var1, var2, 0);
               return;
            }

            if (var7 == -1) {
               for(var8 = (int)this._copyIdx; var8 < var5 << 1 && !_copyIdxUpdater.compareAndSet(this, (long)var8, (long)(var8 + var6)); var8 = (int)this._copyIdx) {
               }

               if (var8 >= var5 << 1) {
                  var7 = var8;
               }
            }

            int var9 = 0;

            for(int var10 = 0; var10 < var6; ++var10) {
               if (this.copy_slot(var1, var8 + var10 & var5 - 1, var2, var4)) {
                  ++var9;
               }
            }

            if (var9 > 0) {
               this.copy_check_and_promote(var1, var2, var9);
            }

            var8 += var6;
         } while(var3 || var7 != -1);

      }

      private final Object[] copy_slot_and_check(NonBlockingHashMap var1, Object[] var2, int var3, Object var4) {
         assert NonBlockingHashMap.chm(var2) == this;

         Object[] var5 = this._newkvs;

         assert var5 != null;

         if (this.copy_slot(var1, var3, var2, this._newkvs)) {
            this.copy_check_and_promote(var1, var2, 1);
         }

         return var4 == null ? var5 : var1.help_copy(var5);
      }

      private final void copy_check_and_promote(NonBlockingHashMap var1, Object[] var2, int var3) {
         assert NonBlockingHashMap.chm(var2) == this;

         int var4 = NonBlockingHashMap.len(var2);
         long var5 = this._copyDone;

         assert var5 + (long)var3 <= (long)var4;

         if (var3 > 0) {
            while(!_copyDoneUpdater.compareAndSet(this, var5, var5 + (long)var3)) {
               var5 = this._copyDone;

               assert var5 + (long)var3 <= (long)var4;
            }
         }

         if (var5 + (long)var3 == (long)var4 && var1._kvs == var2 && var1.CAS_kvs(var2, this._newkvs)) {
            var1._last_resize_milli = System.currentTimeMillis();
         }

      }

      private boolean copy_slot(NonBlockingHashMap var1, int var2, Object[] var3, Object[] var4) {
         Object var5;
         while((var5 = NonBlockingHashMap.key(var3, var2)) == null) {
            NonBlockingHashMap.CAS_key(var3, var2, (Object)null, NonBlockingHashMap.TOMBSTONE);
         }

         Object var6;
         for(var6 = NonBlockingHashMap.val(var3, var2); !(var6 instanceof NonBlockingHashMap.Prime); var6 = NonBlockingHashMap.val(var3, var2)) {
            NonBlockingHashMap.Prime var7 = var6 != null && var6 != NonBlockingHashMap.TOMBSTONE ? new NonBlockingHashMap.Prime(var6) : NonBlockingHashMap.TOMBPRIME;
            if (NonBlockingHashMap.CAS_val(var3, var2, var6, var7)) {
               if (var7 == NonBlockingHashMap.TOMBPRIME) {
                  return true;
               }

               var6 = var7;
               break;
            }
         }

         if (var6 == NonBlockingHashMap.TOMBPRIME) {
            return false;
         } else {
            Object var9 = ((NonBlockingHashMap.Prime)var6)._V;

            assert var9 != NonBlockingHashMap.TOMBSTONE;

            boolean var8;
            for(var8 = NonBlockingHashMap.putIfMatch(var1, var4, var5, var9, (Object)null) == null; !NonBlockingHashMap.CAS_val(var3, var2, var6, NonBlockingHashMap.TOMBPRIME); var6 = NonBlockingHashMap.val(var3, var2)) {
            }

            return var8;
         }
      }
   }

   private static final class Prime {
      final Object _V;

      Prime(Object var1) {
         this._V = var1;
      }

      static Object unbox(Object var0) {
         return var0 instanceof NonBlockingHashMap.Prime ? ((NonBlockingHashMap.Prime)var0)._V : var0;
      }
   }

   private class SnapshotV implements Iterator, Enumeration {
      final Object[] _sskvs;
      private int _idx;
      private Object _nextK;
      private Object _prevK;
      private Object _nextV;
      private Object _prevV;

      public SnapshotV() {
         while(true) {
            Object[] var2 = NonBlockingHashMap.this._kvs;
            NonBlockingHashMap.CHM var3 = NonBlockingHashMap.chm(var2);
            if (var3._newkvs == null) {
               this._sskvs = var2;
               this.next();
               return;
            }

            var3.help_copy_impl(NonBlockingHashMap.this, var2, true);
         }
      }

      int length() {
         return NonBlockingHashMap.len(this._sskvs);
      }

      Object key(int var1) {
         return NonBlockingHashMap.key(this._sskvs, var1);
      }

      public boolean hasNext() {
         return this._nextV != null;
      }

      public Object next() {
         if (this._idx != 0 && this._nextV == null) {
            throw new NoSuchElementException();
         } else {
            this._prevK = this._nextK;
            this._prevV = this._nextV;
            this._nextV = null;

            while(this._idx < this.length()) {
               this._nextK = this.key(this._idx++);
               if (this._nextK != null && this._nextK != NonBlockingHashMap.TOMBSTONE && (this._nextV = NonBlockingHashMap.this.get(this._nextK)) != null) {
                  break;
               }
            }

            return this._prevV;
         }
      }

      public void remove() {
         if (this._prevV == null) {
            throw new IllegalStateException();
         } else {
            NonBlockingHashMap.putIfMatch(NonBlockingHashMap.this, this._sskvs, this._prevK, NonBlockingHashMap.TOMBSTONE, this._prevV);
            this._prevV = null;
         }
      }

      public Object nextElement() {
         return this.next();
      }

      public boolean hasMoreElements() {
         return this.hasNext();
      }
   }

   private class SnapshotK implements Iterator, Enumeration {
      final NonBlockingHashMap.SnapshotV _ss = NonBlockingHashMap.this.new SnapshotV();

      public SnapshotK() {
      }

      public void remove() {
         this._ss.remove();
      }

      public Object next() {
         this._ss.next();
         return this._ss._prevK;
      }

      public boolean hasNext() {
         return this._ss.hasNext();
      }

      public Object nextElement() {
         return this.next();
      }

      public boolean hasMoreElements() {
         return this.hasNext();
      }
   }

   private class SnapshotE implements Iterator {
      final NonBlockingHashMap.SnapshotV _ss = NonBlockingHashMap.this.new SnapshotV();

      public SnapshotE() {
      }

      public void remove() {
         this._ss.remove();
      }

      public Entry next() {
         this._ss.next();
         return NonBlockingHashMap.this.new NBHMEntry(this._ss._prevK, this._ss._prevV);
      }

      public boolean hasNext() {
         return this._ss.hasNext();
      }
   }

   private class NBHMEntry extends AbstractEntry {
      NBHMEntry(Object var2, Object var3) {
         super(var2, var3);
      }

      public Object setValue(Object var1) {
         if (var1 == null) {
            throw new NullPointerException();
         } else {
            this._val = var1;
            return NonBlockingHashMap.this.put(this._key, var1);
         }
      }
   }
}

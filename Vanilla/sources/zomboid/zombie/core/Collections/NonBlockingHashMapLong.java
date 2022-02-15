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
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLongFieldUpdater;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;
import sun.misc.Unsafe;

public class NonBlockingHashMapLong extends AbstractMap implements ConcurrentMap, Serializable {
   private static final long serialVersionUID = 1234123412341234124L;
   private static final int REPROBE_LIMIT = 10;
   private static final Unsafe _unsafe = UtilUnsafe.getUnsafe();
   private static final int _Obase;
   private static final int _Oscale;
   private static final int _Lbase;
   private static final int _Lscale;
   private static final long _chm_offset;
   private static final long _val_1_offset;
   private transient NonBlockingHashMapLong.CHM _chm;
   private transient Object _val_1;
   private transient long _last_resize_milli;
   private final boolean _opt_for_space;
   private static final int MIN_SIZE_LOG = 4;
   private static final int MIN_SIZE = 16;
   private static final Object NO_MATCH_OLD;
   private static final Object MATCH_ANY;
   private static final Object TOMBSTONE;
   private static final NonBlockingHashMapLong.Prime TOMBPRIME;
   private static final long NO_KEY = 0L;
   private transient Counter _reprobes;

   private static long rawIndex(Object[] var0, int var1) {
      assert var1 >= 0 && var1 < var0.length;

      return (long)(_Obase + var1 * _Oscale);
   }

   private static long rawIndex(long[] var0, int var1) {
      assert var1 >= 0 && var1 < var0.length;

      return (long)(_Lbase + var1 * _Lscale);
   }

   private final boolean CAS(long var1, Object var3, Object var4) {
      return _unsafe.compareAndSwapObject(this, var1, var3, var4);
   }

   public final void print() {
      System.out.println("=========");
      print_impl(-99, 0L, this._val_1);
      this._chm.print();
      System.out.println("=========");
   }

   private static final void print_impl(int var0, long var1, Object var3) {
      String var4 = var3 instanceof NonBlockingHashMapLong.Prime ? "prime_" : "";
      Object var5 = NonBlockingHashMapLong.Prime.unbox(var3);
      String var6 = var5 == TOMBSTONE ? "tombstone" : var5.toString();
      System.out.println("[" + var0 + "]=(" + var1 + "," + var4 + var6 + ")");
   }

   private final void print2() {
      System.out.println("=========");
      print2_impl(-99, 0L, this._val_1);
      this._chm.print();
      System.out.println("=========");
   }

   private static final void print2_impl(int var0, long var1, Object var3) {
      if (var3 != null && NonBlockingHashMapLong.Prime.unbox(var3) != TOMBSTONE) {
         print_impl(var0, var1, var3);
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

   public NonBlockingHashMapLong() {
      this(16, true);
   }

   public NonBlockingHashMapLong(int var1) {
      this(var1, true);
   }

   public NonBlockingHashMapLong(boolean var1) {
      this(1, var1);
   }

   public NonBlockingHashMapLong(int var1, boolean var2) {
      this._reprobes = new Counter();
      this._opt_for_space = var2;
      this.initialize(var1);
   }

   private final void initialize(int var1) {
      if (var1 < 0) {
         throw new IllegalArgumentException();
      } else {
         int var2;
         for(var2 = 4; 1 << var2 < var1; ++var2) {
         }

         this._chm = new NonBlockingHashMapLong.CHM(this, new Counter(), var2);
         this._val_1 = TOMBSTONE;
         this._last_resize_milli = System.currentTimeMillis();
      }
   }

   public int size() {
      return (this._val_1 == TOMBSTONE ? 0 : 1) + this._chm.size();
   }

   public boolean containsKey(long var1) {
      return this.get(var1) != null;
   }

   public boolean contains(Object var1) {
      return this.containsValue(var1);
   }

   public Object put(long var1, Object var3) {
      return this.putIfMatch(var1, var3, NO_MATCH_OLD);
   }

   public Object putIfAbsent(long var1, Object var3) {
      return this.putIfMatch(var1, var3, TOMBSTONE);
   }

   public Object remove(long var1) {
      return this.putIfMatch(var1, TOMBSTONE, NO_MATCH_OLD);
   }

   public boolean remove(long var1, Object var3) {
      return this.putIfMatch(var1, TOMBSTONE, var3) == var3;
   }

   public Object replace(long var1, Object var3) {
      return this.putIfMatch(var1, var3, MATCH_ANY);
   }

   public boolean replace(long var1, Object var3, Object var4) {
      return this.putIfMatch(var1, var4, var3) == var3;
   }

   private final Object putIfMatch(long var1, Object var3, Object var4) {
      if (var4 != null && var3 != null) {
         Object var5;
         if (var1 != 0L) {
            var5 = this._chm.putIfMatch(var1, var3, var4);

            assert !(var5 instanceof NonBlockingHashMapLong.Prime);

            assert var5 != null;

            return var5 == TOMBSTONE ? null : var5;
         } else {
            var5 = this._val_1;
            if (var4 == NO_MATCH_OLD || var5 == var4 || var4 == MATCH_ANY && var5 != TOMBSTONE || var4.equals(var5)) {
               this.CAS(_val_1_offset, var5, var3);
            }

            return var5 == TOMBSTONE ? null : var5;
         }
      } else {
         throw new NullPointerException();
      }
   }

   public void clear() {
      NonBlockingHashMapLong.CHM var1 = new NonBlockingHashMapLong.CHM(this, new Counter(), 4);

      while(!this.CAS(_chm_offset, this._chm, var1)) {
      }

      this.CAS(_val_1_offset, this._val_1, TOMBSTONE);
   }

   public boolean containsValue(Object var1) {
      if (var1 == null) {
         return false;
      } else if (var1 == this._val_1) {
         return true;
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

   public final Object get(long var1) {
      Object var3;
      if (var1 == 0L) {
         var3 = this._val_1;
         return var3 == TOMBSTONE ? null : var3;
      } else {
         var3 = this._chm.get_impl(var1);

         assert !(var3 instanceof NonBlockingHashMapLong.Prime);

         assert var3 != TOMBSTONE;

         return var3;
      }
   }

   public Object get(Object var1) {
      return var1 instanceof Long ? this.get((Long)var1) : null;
   }

   public Object remove(Object var1) {
      return var1 instanceof Long ? this.remove((Long)var1) : null;
   }

   public boolean remove(Object var1, Object var2) {
      return var1 instanceof Long ? this.remove((Long)var1, var2) : false;
   }

   public boolean containsKey(Object var1) {
      return var1 instanceof Long ? this.containsKey((Long)var1) : false;
   }

   public Object putIfAbsent(Long var1, Object var2) {
      return this.putIfAbsent(var1, var2);
   }

   public Object replace(Long var1, Object var2) {
      return this.replace(var1, var2);
   }

   public Object put(Long var1, Object var2) {
      return this.put(var1, var2);
   }

   public boolean replace(Long var1, Object var2, Object var3) {
      return this.replace(var1, var2, var3);
   }

   private final void help_copy() {
      NonBlockingHashMapLong.CHM var1 = this._chm;
      if (var1._newchm != null) {
         var1.help_copy_impl(false);
      }
   }

   public Enumeration elements() {
      return new NonBlockingHashMapLong.SnapshotV();
   }

   public Collection values() {
      return new AbstractCollection() {
         public void clear() {
            NonBlockingHashMapLong.this.clear();
         }

         public int size() {
            return NonBlockingHashMapLong.this.size();
         }

         public boolean contains(Object var1) {
            return NonBlockingHashMapLong.this.containsValue(var1);
         }

         public Iterator iterator() {
            return NonBlockingHashMapLong.this.new SnapshotV();
         }
      };
   }

   public Enumeration keys() {
      return new NonBlockingHashMapLong.IteratorLong();
   }

   public Set keySet() {
      return new AbstractSet() {
         public void clear() {
            NonBlockingHashMapLong.this.clear();
         }

         public int size() {
            return NonBlockingHashMapLong.this.size();
         }

         public boolean contains(Object var1) {
            return NonBlockingHashMapLong.this.containsKey(var1);
         }

         public boolean remove(Object var1) {
            return NonBlockingHashMapLong.this.remove(var1) != null;
         }

         public NonBlockingHashMapLong.IteratorLong iterator() {
            return NonBlockingHashMapLong.this.new IteratorLong();
         }
      };
   }

   public Set entrySet() {
      return new AbstractSet() {
         public void clear() {
            NonBlockingHashMapLong.this.clear();
         }

         public int size() {
            return NonBlockingHashMapLong.this.size();
         }

         public boolean remove(Object var1) {
            if (!(var1 instanceof Entry)) {
               return false;
            } else {
               Entry var2 = (Entry)var1;
               return NonBlockingHashMapLong.this.remove(var2.getKey(), var2.getValue());
            }
         }

         public boolean contains(Object var1) {
            if (!(var1 instanceof Entry)) {
               return false;
            } else {
               Entry var2 = (Entry)var1;
               Object var3 = NonBlockingHashMapLong.this.get(var2.getKey());
               return var3.equals(var2.getValue());
            }
         }

         public Iterator iterator() {
            return NonBlockingHashMapLong.this.new SnapshotE();
         }
      };
   }

   private void writeObject(ObjectOutputStream var1) throws IOException {
      var1.defaultWriteObject();
      Iterator var2 = this.keySet().iterator();

      while(var2.hasNext()) {
         long var3 = (Long)var2.next();
         Object var5 = this.get(var3);
         var1.writeLong(var3);
         var1.writeObject(var5);
      }

      var1.writeLong(0L);
      var1.writeObject((Object)null);
   }

   private void readObject(ObjectInputStream var1) throws IOException, ClassNotFoundException {
      var1.defaultReadObject();
      this.initialize(16);

      while(true) {
         long var2 = var1.readLong();
         Object var4 = var1.readObject();
         if (var2 == 0L && var4 == null) {
            return;
         }

         this.put(var2, var4);
      }
   }

   static {
      _Obase = _unsafe.arrayBaseOffset(Object[].class);
      _Oscale = _unsafe.arrayIndexScale(Object[].class);
      _Lbase = _unsafe.arrayBaseOffset(long[].class);
      _Lscale = _unsafe.arrayIndexScale(long[].class);
      Field var0 = null;

      try {
         var0 = NonBlockingHashMapLong.class.getDeclaredField("_chm");
      } catch (NoSuchFieldException var3) {
         throw new RuntimeException(var3);
      }

      _chm_offset = _unsafe.objectFieldOffset(var0);

      try {
         var0 = NonBlockingHashMapLong.class.getDeclaredField("_val_1");
      } catch (NoSuchFieldException var2) {
         throw new RuntimeException(var2);
      }

      _val_1_offset = _unsafe.objectFieldOffset(var0);
      NO_MATCH_OLD = new Object();
      MATCH_ANY = new Object();
      TOMBSTONE = new Object();
      TOMBPRIME = new NonBlockingHashMapLong.Prime(TOMBSTONE);
   }

   private static final class CHM implements Serializable {
      final NonBlockingHashMapLong _nbhml;
      private final Counter _size;
      private final Counter _slots;
      volatile NonBlockingHashMapLong.CHM _newchm;
      private static final AtomicReferenceFieldUpdater _newchmUpdater = AtomicReferenceFieldUpdater.newUpdater(NonBlockingHashMapLong.CHM.class, NonBlockingHashMapLong.CHM.class, "_newchm");
      volatile long _resizers;
      private static final AtomicLongFieldUpdater _resizerUpdater = AtomicLongFieldUpdater.newUpdater(NonBlockingHashMapLong.CHM.class, "_resizers");
      final long[] _keys;
      final Object[] _vals;
      volatile long _copyIdx = 0L;
      private static final AtomicLongFieldUpdater _copyIdxUpdater = AtomicLongFieldUpdater.newUpdater(NonBlockingHashMapLong.CHM.class, "_copyIdx");
      volatile long _copyDone = 0L;
      private static final AtomicLongFieldUpdater _copyDoneUpdater = AtomicLongFieldUpdater.newUpdater(NonBlockingHashMapLong.CHM.class, "_copyDone");

      public int size() {
         return (int)this._size.get();
      }

      public int slots() {
         return (int)this._slots.get();
      }

      boolean CAS_newchm(NonBlockingHashMapLong.CHM var1) {
         return _newchmUpdater.compareAndSet(this, (Object)null, var1);
      }

      private final boolean CAS_key(int var1, long var2, long var4) {
         return NonBlockingHashMapLong._unsafe.compareAndSwapLong(this._keys, NonBlockingHashMapLong.rawIndex(this._keys, var1), var2, var4);
      }

      private final boolean CAS_val(int var1, Object var2, Object var3) {
         return NonBlockingHashMapLong._unsafe.compareAndSwapObject(this._vals, NonBlockingHashMapLong.rawIndex(this._vals, var1), var2, var3);
      }

      CHM(NonBlockingHashMapLong var1, Counter var2, int var3) {
         this._nbhml = var1;
         this._size = var2;
         this._slots = new Counter();
         this._keys = new long[1 << var3];
         this._vals = new Object[1 << var3];
      }

      private final void print() {
         for(int var1 = 0; var1 < this._keys.length; ++var1) {
            long var2 = this._keys[var1];
            if (var2 != 0L) {
               NonBlockingHashMapLong.print_impl(var1, var2, this._vals[var1]);
            }
         }

         NonBlockingHashMapLong.CHM var4 = this._newchm;
         if (var4 != null) {
            System.out.println("----");
            var4.print();
         }

      }

      private final void print2() {
         for(int var1 = 0; var1 < this._keys.length; ++var1) {
            long var2 = this._keys[var1];
            if (var2 != 0L) {
               NonBlockingHashMapLong.print2_impl(var1, var2, this._vals[var1]);
            }
         }

         NonBlockingHashMapLong.CHM var4 = this._newchm;
         if (var4 != null) {
            System.out.println("----");
            var4.print2();
         }

      }

      private final Object get_impl(long var1) {
         int var3 = this._keys.length;
         int var4 = (int)(var1 & (long)(var3 - 1));
         int var5 = 0;

         while(true) {
            long var6 = this._keys[var4];
            Object var8 = this._vals[var4];
            if (var6 == 0L) {
               return null;
            }

            if (var1 == var6) {
               if (!(var8 instanceof NonBlockingHashMapLong.Prime)) {
                  if (var8 == NonBlockingHashMapLong.TOMBSTONE) {
                     return null;
                  }

                  NonBlockingHashMapLong.CHM var9 = this._newchm;
                  return var8;
               }

               return this.copy_slot_and_check(var4, var1).get_impl(var1);
            }

            ++var5;
            if (var5 >= NonBlockingHashMapLong.reprobe_limit(var3)) {
               return this._newchm == null ? null : this.copy_slot_and_check(var4, var1).get_impl(var1);
            }

            var4 = var4 + 1 & var3 - 1;
         }
      }

      private final Object putIfMatch(long var1, Object var3, Object var4) {
         assert var3 != null;

         assert !(var3 instanceof NonBlockingHashMapLong.Prime);

         assert !(var4 instanceof NonBlockingHashMapLong.Prime);

         int var5 = this._keys.length;
         int var6 = (int)(var1 & (long)(var5 - 1));
         int var7 = 0;
         long var8 = 0L;
         Object var10 = null;

         while(true) {
            var10 = this._vals[var6];
            var8 = this._keys[var6];
            if (var8 == 0L) {
               if (var3 == NonBlockingHashMapLong.TOMBSTONE) {
                  return var3;
               }

               if (this.CAS_key(var6, 0L, var1)) {
                  this._slots.add(1L);
                  break;
               }

               var8 = this._keys[var6];

               assert var8 != 0L;
            }

            if (var8 == var1) {
               break;
            }

            ++var7;
            if (var7 >= NonBlockingHashMapLong.reprobe_limit(var5)) {
               NonBlockingHashMapLong.CHM var11 = this.resize();
               if (var4 != null) {
                  this._nbhml.help_copy();
               }

               return var11.putIfMatch(var1, var3, var4);
            }

            var6 = var6 + 1 & var5 - 1;
         }

         if (var3 == var10) {
            return var10;
         } else if ((var10 != null || !this.tableFull(var7, var5)) && !(var10 instanceof NonBlockingHashMapLong.Prime)) {
            do {
               assert !(var10 instanceof NonBlockingHashMapLong.Prime);

               if (var4 != NonBlockingHashMapLong.NO_MATCH_OLD && var10 != var4 && (var4 != NonBlockingHashMapLong.MATCH_ANY || var10 == NonBlockingHashMapLong.TOMBSTONE || var10 == null) && (var10 != null || var4 != NonBlockingHashMapLong.TOMBSTONE) && (var4 == null || !var4.equals(var10))) {
                  return var10;
               }

               if (this.CAS_val(var6, var10, var3)) {
                  if (var4 != null) {
                     if ((var10 == null || var10 == NonBlockingHashMapLong.TOMBSTONE) && var3 != NonBlockingHashMapLong.TOMBSTONE) {
                        this._size.add(1L);
                     }

                     if (var10 != null && var10 != NonBlockingHashMapLong.TOMBSTONE && var3 == NonBlockingHashMapLong.TOMBSTONE) {
                        this._size.add(-1L);
                     }
                  }

                  return var10 == null && var4 != null ? NonBlockingHashMapLong.TOMBSTONE : var10;
               }

               var10 = this._vals[var6];
            } while(!(var10 instanceof NonBlockingHashMapLong.Prime));

            return this.copy_slot_and_check(var6, var4).putIfMatch(var1, var3, var4);
         } else {
            this.resize();
            return this.copy_slot_and_check(var6, var4).putIfMatch(var1, var3, var4);
         }
      }

      private final boolean tableFull(int var1, int var2) {
         return var1 >= 10 && this._slots.estimate_get() >= (long)NonBlockingHashMapLong.reprobe_limit(var2);
      }

      private final NonBlockingHashMapLong.CHM resize() {
         NonBlockingHashMapLong.CHM var1 = this._newchm;
         if (var1 != null) {
            return var1;
         } else {
            int var2 = this._keys.length;
            int var3 = this.size();
            int var4 = var3;
            if (this._nbhml._opt_for_space) {
               if (var3 >= var2 >> 1) {
                  var4 = var2 << 1;
               }
            } else if (var3 >= var2 >> 2) {
               var4 = var2 << 1;
               if (var3 >= var2 >> 1) {
                  var4 = var2 << 2;
               }
            }

            long var5 = System.currentTimeMillis();
            long var7 = 0L;
            if (var4 <= var2 && var5 <= this._nbhml._last_resize_milli + 10000L) {
               var4 = var2 << 1;
            }

            if (var4 < var2) {
               var4 = var2;
            }

            int var9;
            for(var9 = 4; 1 << var9 < var4; ++var9) {
            }

            long var10;
            for(var10 = this._resizers; !_resizerUpdater.compareAndSet(this, var10, var10 + 1L); var10 = this._resizers) {
            }

            int var12 = (1 << var9 << 1) + 4 << 3 >> 20;
            if (var10 >= 2L && var12 > 0) {
               var1 = this._newchm;
               if (var1 != null) {
                  return var1;
               }

               try {
                  Thread.sleep((long)(8 * var12));
               } catch (Exception var14) {
               }
            }

            var1 = this._newchm;
            if (var1 != null) {
               return var1;
            } else {
               var1 = new NonBlockingHashMapLong.CHM(this._nbhml, this._size, var9);
               if (this._newchm != null) {
                  return this._newchm;
               } else {
                  if (!this.CAS_newchm(var1)) {
                     var1 = this._newchm;
                  }

                  return var1;
               }
            }
         }
      }

      private final void help_copy_impl(boolean var1) {
         NonBlockingHashMapLong.CHM var2 = this._newchm;

         assert var2 != null;

         int var3 = this._keys.length;
         int var4 = Math.min(var3, 1024);
         int var5 = -1;
         int var6 = -9999;

         do {
            if (this._copyDone >= (long)var3) {
               this.copy_check_and_promote(0);
               return;
            }

            if (var5 == -1) {
               for(var6 = (int)this._copyIdx; var6 < var3 << 1 && !_copyIdxUpdater.compareAndSet(this, (long)var6, (long)(var6 + var4)); var6 = (int)this._copyIdx) {
               }

               if (var6 >= var3 << 1) {
                  var5 = var6;
               }
            }

            int var7 = 0;

            for(int var8 = 0; var8 < var4; ++var8) {
               if (this.copy_slot(var6 + var8 & var3 - 1)) {
                  ++var7;
               }
            }

            if (var7 > 0) {
               this.copy_check_and_promote(var7);
            }

            var6 += var4;
         } while(var1 || var5 != -1);

      }

      private final NonBlockingHashMapLong.CHM copy_slot_and_check(int var1, Object var2) {
         assert this._newchm != null;

         if (this.copy_slot(var1)) {
            this.copy_check_and_promote(1);
         }

         if (var2 != null) {
            this._nbhml.help_copy();
         }

         return this._newchm;
      }

      private final void copy_check_and_promote(int var1) {
         int var2 = this._keys.length;
         long var3 = this._copyDone;
         long var5 = var3 + (long)var1;

         assert var5 <= (long)var2;

         if (var1 > 0) {
            while(!_copyDoneUpdater.compareAndSet(this, var3, var5)) {
               var3 = this._copyDone;
               var5 = var3 + (long)var1;

               assert var5 <= (long)var2;
            }
         }

         if (var5 == (long)var2 && this._nbhml._chm == this && this._nbhml.CAS(NonBlockingHashMapLong._chm_offset, this, this._newchm)) {
            this._nbhml._last_resize_milli = System.currentTimeMillis();
         }

      }

      private boolean copy_slot(int var1) {
         long var2;
         while((var2 = this._keys[var1]) == 0L) {
            this.CAS_key(var1, 0L, (long)(var1 + this._keys.length));
         }

         Object var4;
         for(var4 = this._vals[var1]; !(var4 instanceof NonBlockingHashMapLong.Prime); var4 = this._vals[var1]) {
            NonBlockingHashMapLong.Prime var5 = var4 != null && var4 != NonBlockingHashMapLong.TOMBSTONE ? new NonBlockingHashMapLong.Prime(var4) : NonBlockingHashMapLong.TOMBPRIME;
            if (this.CAS_val(var1, var4, var5)) {
               if (var5 == NonBlockingHashMapLong.TOMBPRIME) {
                  return true;
               }

               var4 = var5;
               break;
            }
         }

         if (var4 == NonBlockingHashMapLong.TOMBPRIME) {
            return false;
         } else {
            Object var7 = ((NonBlockingHashMapLong.Prime)var4)._V;

            assert var7 != NonBlockingHashMapLong.TOMBSTONE;

            boolean var6;
            for(var6 = this._newchm.putIfMatch(var2, var7, (Object)null) == null; !this.CAS_val(var1, var4, NonBlockingHashMapLong.TOMBPRIME); var4 = this._vals[var1]) {
            }

            return var6;
         }
      }
   }

   private static final class Prime {
      final Object _V;

      Prime(Object var1) {
         this._V = var1;
      }

      static Object unbox(Object var0) {
         return var0 instanceof NonBlockingHashMapLong.Prime ? ((NonBlockingHashMapLong.Prime)var0)._V : var0;
      }
   }

   private class SnapshotV implements Iterator, Enumeration {
      final NonBlockingHashMapLong.CHM _sschm;
      private int _idx;
      private long _nextK;
      private long _prevK;
      private Object _nextV;
      private Object _prevV;

      public SnapshotV() {
         while(true) {
            NonBlockingHashMapLong.CHM var2 = NonBlockingHashMapLong.this._chm;
            if (var2._newchm == null) {
               this._sschm = var2;
               this._idx = -1;
               this.next();
               return;
            }

            var2.help_copy_impl(true);
         }
      }

      int length() {
         return this._sschm._keys.length;
      }

      long key(int var1) {
         return this._sschm._keys[var1];
      }

      public boolean hasNext() {
         return this._nextV != null;
      }

      public Object next() {
         if (this._idx != -1 && this._nextV == null) {
            throw new NoSuchElementException();
         } else {
            this._prevK = this._nextK;
            this._prevV = this._nextV;
            this._nextV = null;
            if (this._idx == -1) {
               this._idx = 0;
               this._nextK = 0L;
               if ((this._nextV = NonBlockingHashMapLong.this.get(this._nextK)) != null) {
                  return this._prevV;
               }
            }

            while(this._idx < this.length()) {
               this._nextK = this.key(this._idx++);
               if (this._nextK != 0L && (this._nextV = NonBlockingHashMapLong.this.get(this._nextK)) != null) {
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
            this._sschm.putIfMatch(this._prevK, NonBlockingHashMapLong.TOMBSTONE, this._prevV);
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

   public class IteratorLong implements Iterator, Enumeration {
      private final NonBlockingHashMapLong.SnapshotV _ss = NonBlockingHashMapLong.this.new SnapshotV();

      public void remove() {
         this._ss.remove();
      }

      public Long next() {
         this._ss.next();
         return this._ss._prevK;
      }

      public long nextLong() {
         this._ss.next();
         return this._ss._prevK;
      }

      public boolean hasNext() {
         return this._ss.hasNext();
      }

      public Long nextElement() {
         return this.next();
      }

      public boolean hasMoreElements() {
         return this.hasNext();
      }
   }

   private class SnapshotE implements Iterator {
      final NonBlockingHashMapLong.SnapshotV _ss = NonBlockingHashMapLong.this.new SnapshotV();

      public SnapshotE() {
      }

      public void remove() {
         this._ss.remove();
      }

      public Entry next() {
         this._ss.next();
         return NonBlockingHashMapLong.this.new NBHMLEntry(this._ss._prevK, this._ss._prevV);
      }

      public boolean hasNext() {
         return this._ss.hasNext();
      }
   }

   private class NBHMLEntry extends AbstractEntry {
      NBHMLEntry(Long var2, Object var3) {
         super(var2, var3);
      }

      public Object setValue(Object var1) {
         if (var1 == null) {
            throw new NullPointerException();
         } else {
            this._val = var1;
            return NonBlockingHashMapLong.this.put((Long)this._key, var1);
         }
      }
   }
}

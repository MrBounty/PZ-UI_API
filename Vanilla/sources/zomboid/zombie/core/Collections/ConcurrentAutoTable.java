package zombie.core.Collections;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicLongFieldUpdater;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;
import sun.misc.Unsafe;

public class ConcurrentAutoTable implements Serializable {
   private volatile ConcurrentAutoTable.CAT _cat = new ConcurrentAutoTable.CAT((ConcurrentAutoTable.CAT)null, 4, 0L);
   private static final AtomicReferenceFieldUpdater _catUpdater = AtomicReferenceFieldUpdater.newUpdater(ConcurrentAutoTable.class, ConcurrentAutoTable.CAT.class, "_cat");

   public void add(long var1) {
      this.add_if_mask(var1, 0L);
   }

   public void decrement() {
      this.add_if_mask(-1L, 0L);
   }

   public void increment() {
      this.add_if_mask(1L, 0L);
   }

   public void set(long var1) {
      ConcurrentAutoTable.CAT var3 = new ConcurrentAutoTable.CAT((ConcurrentAutoTable.CAT)null, 4, var1);

      while(!this.CAS_cat(this._cat, var3)) {
      }

   }

   public long get() {
      return this._cat.sum(0L);
   }

   public int intValue() {
      return (int)this._cat.sum(0L);
   }

   public long longValue() {
      return this._cat.sum(0L);
   }

   public long estimate_get() {
      return this._cat.estimate_sum(0L);
   }

   public String toString() {
      return this._cat.toString(0L);
   }

   public void print() {
      this._cat.print();
   }

   public int internal_size() {
      return this._cat._t.length;
   }

   private long add_if_mask(long var1, long var3) {
      return this._cat.add_if_mask(var1, var3, hash(), this);
   }

   private boolean CAS_cat(ConcurrentAutoTable.CAT var1, ConcurrentAutoTable.CAT var2) {
      return _catUpdater.compareAndSet(this, var1, var2);
   }

   private static final int hash() {
      int var0 = System.identityHashCode(Thread.currentThread());
      var0 ^= var0 >>> 20 ^ var0 >>> 12;
      var0 ^= var0 >>> 7 ^ var0 >>> 4;
      return var0 << 2;
   }

   private static class CAT implements Serializable {
      private static final Unsafe _unsafe = UtilUnsafe.getUnsafe();
      private static final int _Lbase;
      private static final int _Lscale;
      volatile long _resizers;
      private static final AtomicLongFieldUpdater _resizerUpdater;
      private final ConcurrentAutoTable.CAT _next;
      private volatile long _sum_cache;
      private volatile long _fuzzy_sum_cache;
      private volatile long _fuzzy_time;
      private static final int MAX_SPIN = 2;
      private long[] _t;

      private static long rawIndex(long[] var0, int var1) {
         assert var1 >= 0 && var1 < var0.length;

         return (long)(_Lbase + var1 * _Lscale);
      }

      private static final boolean CAS(long[] var0, int var1, long var2, long var4) {
         return _unsafe.compareAndSwapLong(var0, rawIndex(var0, var1), var2, var4);
      }

      CAT(ConcurrentAutoTable.CAT var1, int var2, long var3) {
         this._next = var1;
         this._sum_cache = Long.MIN_VALUE;
         this._t = new long[var2];
         this._t[0] = var3;
      }

      public long add_if_mask(long var1, long var3, int var5, ConcurrentAutoTable var6) {
         long[] var7 = this._t;
         int var8 = var5 & var7.length - 1;
         long var9 = var7[var8];
         boolean var11 = CAS(var7, var8, var9 & ~var3, var9 + var1);
         if (this._sum_cache != Long.MIN_VALUE) {
            this._sum_cache = Long.MIN_VALUE;
         }

         if (var11) {
            return var9;
         } else if ((var9 & var3) != 0L) {
            return var9;
         } else {
            int var12 = 0;

            while(true) {
               var9 = var7[var8];
               if ((var9 & var3) != 0L) {
                  return var9;
               }

               if (CAS(var7, var8, var9, var9 + var1)) {
                  if (var12 < 2) {
                     return var9;
                  }

                  if (var7.length >= 1048576) {
                     return var9;
                  }

                  long var13 = this._resizers;

                  int var15;
                  for(var15 = var7.length << 1 << 3; !_resizerUpdater.compareAndSet(this, var13, var13 + (long)var15); var13 = this._resizers) {
                  }

                  var13 += (long)var15;
                  if (var6._cat != this) {
                     return var9;
                  }

                  if (var13 >> 17 != 0L) {
                     try {
                        Thread.sleep(var13 >> 17);
                     } catch (InterruptedException var17) {
                     }

                     if (var6._cat != this) {
                        return var9;
                     }
                  }

                  ConcurrentAutoTable.CAT var16 = new ConcurrentAutoTable.CAT(this, var7.length * 2, 0L);
                  var6.CAS_cat(this, var16);
                  return var9;
               }

               ++var12;
            }
         }
      }

      public long sum(long var1) {
         long var3 = this._sum_cache;
         if (var3 != Long.MIN_VALUE) {
            return var3;
         } else {
            var3 = this._next == null ? 0L : this._next.sum(var1);
            long[] var5 = this._t;

            for(int var6 = 0; var6 < var5.length; ++var6) {
               var3 += var5[var6] & ~var1;
            }

            this._sum_cache = var3;
            return var3;
         }
      }

      public long estimate_sum(long var1) {
         if (this._t.length <= 64) {
            return this.sum(var1);
         } else {
            long var3 = System.currentTimeMillis();
            if (this._fuzzy_time != var3) {
               this._fuzzy_sum_cache = this.sum(var1);
               this._fuzzy_time = var3;
            }

            return this._fuzzy_sum_cache;
         }
      }

      public void all_or(long var1) {
         long[] var3 = this._t;

         long var6;
         for(int var4 = 0; var4 < var3.length; ++var4) {
            for(boolean var5 = false; !var5; var5 = CAS(var3, var4, var6, var6 | var1)) {
               var6 = var3[var4];
            }
         }

         if (this._next != null) {
            this._next.all_or(var1);
         }

         if (this._sum_cache != Long.MIN_VALUE) {
            this._sum_cache = Long.MIN_VALUE;
         }

      }

      public void all_and(long var1) {
         long[] var3 = this._t;

         long var6;
         for(int var4 = 0; var4 < var3.length; ++var4) {
            for(boolean var5 = false; !var5; var5 = CAS(var3, var4, var6, var6 & var1)) {
               var6 = var3[var4];
            }
         }

         if (this._next != null) {
            this._next.all_and(var1);
         }

         if (this._sum_cache != Long.MIN_VALUE) {
            this._sum_cache = Long.MIN_VALUE;
         }

      }

      public void all_set(long var1) {
         long[] var3 = this._t;

         for(int var4 = 0; var4 < var3.length; ++var4) {
            var3[var4] = var1;
         }

         if (this._next != null) {
            this._next.all_set(var1);
         }

         if (this._sum_cache != Long.MIN_VALUE) {
            this._sum_cache = Long.MIN_VALUE;
         }

      }

      String toString(long var1) {
         return Long.toString(this.sum(var1));
      }

      public void print() {
         long[] var1 = this._t;
         System.out.print("[sum=" + this._sum_cache + "," + var1[0]);

         for(int var2 = 1; var2 < var1.length; ++var2) {
            System.out.print("," + var1[var2]);
         }

         System.out.print("]");
         if (this._next != null) {
            this._next.print();
         }

      }

      static {
         _Lbase = _unsafe.arrayBaseOffset(long[].class);
         _Lscale = _unsafe.arrayIndexScale(long[].class);
         _resizerUpdater = AtomicLongFieldUpdater.newUpdater(ConcurrentAutoTable.CAT.class, "_resizers");
      }
   }
}

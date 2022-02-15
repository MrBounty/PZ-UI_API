package zombie.core.Collections;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.AbstractSet;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.concurrent.atomic.AtomicInteger;
import sun.misc.Unsafe;

public class NonBlockingSetInt extends AbstractSet implements Serializable {
   private static final long serialVersionUID = 1234123412341234123L;
   private static final Unsafe _unsafe = UtilUnsafe.getUnsafe();
   private static final long _nbsi_offset;
   private transient NonBlockingSetInt.NBSI _nbsi;

   private final boolean CAS_nbsi(NonBlockingSetInt.NBSI var1, NonBlockingSetInt.NBSI var2) {
      return _unsafe.compareAndSwapObject(this, _nbsi_offset, var1, var2);
   }

   public NonBlockingSetInt() {
      this._nbsi = new NonBlockingSetInt.NBSI(63, new Counter(), this);
   }

   private NonBlockingSetInt(NonBlockingSetInt var1, NonBlockingSetInt var2) {
      this._nbsi = new NonBlockingSetInt.NBSI(var1._nbsi, var2._nbsi, new Counter(), this);
   }

   public boolean add(Integer var1) {
      return this.add(var1);
   }

   public boolean contains(Object var1) {
      return var1 instanceof Integer ? this.contains((Integer)var1) : false;
   }

   public boolean remove(Object var1) {
      return var1 instanceof Integer ? this.remove((Integer)var1) : false;
   }

   public boolean add(int var1) {
      if (var1 < 0) {
         throw new IllegalArgumentException(var1.makeConcatWithConstants<invokedynamic>(var1));
      } else {
         return this._nbsi.add(var1);
      }
   }

   public boolean contains(int var1) {
      return var1 < 0 ? false : this._nbsi.contains(var1);
   }

   public boolean remove(int var1) {
      return var1 < 0 ? false : this._nbsi.remove(var1);
   }

   public int size() {
      return this._nbsi.size();
   }

   public void clear() {
      NonBlockingSetInt.NBSI var1 = new NonBlockingSetInt.NBSI(63, new Counter(), this);

      while(!this.CAS_nbsi(this._nbsi, var1)) {
      }

   }

   public int sizeInBytes() {
      return this._nbsi.sizeInBytes();
   }

   public NonBlockingSetInt intersect(NonBlockingSetInt var1) {
      NonBlockingSetInt var2 = new NonBlockingSetInt(this, var1);
      var2._nbsi.intersect(var2._nbsi, this._nbsi, var1._nbsi);
      return var2;
   }

   public NonBlockingSetInt union(NonBlockingSetInt var1) {
      NonBlockingSetInt var2 = new NonBlockingSetInt(this, var1);
      var2._nbsi.union(var2._nbsi, this._nbsi, var1._nbsi);
      return var2;
   }

   public void print() {
      this._nbsi.print(0);
   }

   public Iterator iterator() {
      return new NonBlockingSetInt.iter();
   }

   public IntIterator intIterator() {
      return new NonBlockingSetInt.NBSIIntIterator();
   }

   private void writeObject(ObjectOutputStream var1) throws IOException {
      var1.defaultWriteObject();
      NonBlockingSetInt.NBSI var2 = this._nbsi;
      int var3 = this._nbsi._bits.length << 6;
      var1.writeInt(var3);

      for(int var4 = 0; var4 < var3; ++var4) {
         var1.writeBoolean(this._nbsi.contains(var4));
      }

   }

   private void readObject(ObjectInputStream var1) throws IOException, ClassNotFoundException {
      var1.defaultReadObject();
      int var2 = var1.readInt();
      this._nbsi = new NonBlockingSetInt.NBSI(var2, new Counter(), this);

      for(int var3 = 0; var3 < var2; ++var3) {
         if (var1.readBoolean()) {
            this._nbsi.add(var3);
         }
      }

   }

   static {
      Field var0 = null;

      try {
         var0 = NonBlockingSetInt.class.getDeclaredField("_nbsi");
      } catch (NoSuchFieldException var2) {
      }

      _nbsi_offset = _unsafe.objectFieldOffset(var0);
   }

   private static final class NBSI {
      private final transient NonBlockingSetInt _non_blocking_set_int;
      private final transient Counter _size;
      private final long[] _bits;
      private static final int _Lbase;
      private static final int _Lscale;
      private NonBlockingSetInt.NBSI _new;
      private static final long _new_offset;
      private final transient AtomicInteger _copyIdx;
      private final transient AtomicInteger _copyDone;
      private final transient int _sum_bits_length;
      private final NonBlockingSetInt.NBSI _nbsi64;

      private static long rawIndex(long[] var0, int var1) {
         assert var1 >= 0 && var1 < var0.length;

         return (long)(_Lbase + var1 * _Lscale);
      }

      private final boolean CAS(int var1, long var2, long var4) {
         return NonBlockingSetInt._unsafe.compareAndSwapLong(this._bits, rawIndex(this._bits, var1), var2, var4);
      }

      private final boolean CAS_new(NonBlockingSetInt.NBSI var1) {
         return NonBlockingSetInt._unsafe.compareAndSwapObject(this, _new_offset, (Object)null, var1);
      }

      private static final long mask(int var0) {
         return 1L << (var0 & 63);
      }

      private NBSI(int var1, Counter var2, NonBlockingSetInt var3) {
         this._non_blocking_set_int = var3;
         this._size = var2;
         this._copyIdx = var2 == null ? null : new AtomicInteger();
         this._copyDone = var2 == null ? null : new AtomicInteger();
         this._bits = new long[(int)((long)var1 + 63L >>> 6)];
         this._nbsi64 = var1 + 1 >>> 6 == 0 ? null : new NonBlockingSetInt.NBSI(var1 + 1 >>> 6, (Counter)null, (NonBlockingSetInt)null);
         this._sum_bits_length = this._bits.length + (this._nbsi64 == null ? 0 : this._nbsi64._sum_bits_length);
      }

      private NBSI(NonBlockingSetInt.NBSI var1, NonBlockingSetInt.NBSI var2, Counter var3, NonBlockingSetInt var4) {
         this._non_blocking_set_int = var4;
         this._size = var3;
         this._copyIdx = var3 == null ? null : new AtomicInteger();
         this._copyDone = var3 == null ? null : new AtomicInteger();
         if (!has_bits(var1) && !has_bits(var2)) {
            this._bits = null;
            this._nbsi64 = null;
            this._sum_bits_length = 0;
         } else {
            if (!has_bits(var1)) {
               this._bits = new long[var2._bits.length];
               this._nbsi64 = new NonBlockingSetInt.NBSI((NonBlockingSetInt.NBSI)null, var2._nbsi64, (Counter)null, (NonBlockingSetInt)null);
            } else if (!has_bits(var2)) {
               this._bits = new long[var1._bits.length];
               this._nbsi64 = new NonBlockingSetInt.NBSI((NonBlockingSetInt.NBSI)null, var1._nbsi64, (Counter)null, (NonBlockingSetInt)null);
            } else {
               int var5 = var1._bits.length > var2._bits.length ? var1._bits.length : var2._bits.length;
               this._bits = new long[var5];
               this._nbsi64 = new NonBlockingSetInt.NBSI(var1._nbsi64, var2._nbsi64, (Counter)null, (NonBlockingSetInt)null);
            }

            this._sum_bits_length = this._bits.length + this._nbsi64._sum_bits_length;
         }
      }

      private static boolean has_bits(NonBlockingSetInt.NBSI var0) {
         return var0 != null && var0._bits != null;
      }

      public boolean add(int var1) {
         if (var1 >> 6 >= this._bits.length) {
            return this.install_larger_new_bits(var1).help_copy().add(var1);
         } else {
            NonBlockingSetInt.NBSI var2 = this;

            int var3;
            for(var3 = var1; (var3 & 63) == 63; var3 >>= 6) {
               var2 = var2._nbsi64;
            }

            long var4 = mask(var3);

            long var6;
            do {
               var6 = var2._bits[var3 >> 6];
               if (var6 < 0L) {
                  return this.help_copy_impl(var1).help_copy().add(var1);
               }

               if ((var6 & var4) != 0L) {
                  return false;
               }
            } while(!var2.CAS(var3 >> 6, var6, var6 | var4));

            this._size.add(1L);
            return true;
         }
      }

      public boolean remove(int var1) {
         if (var1 >> 6 >= this._bits.length) {
            return this._new == null ? false : this.help_copy().remove(var1);
         } else {
            NonBlockingSetInt.NBSI var2 = this;

            int var3;
            for(var3 = var1; (var3 & 63) == 63; var3 >>= 6) {
               var2 = var2._nbsi64;
            }

            long var4 = mask(var3);

            long var6;
            do {
               var6 = var2._bits[var3 >> 6];
               if (var6 < 0L) {
                  return this.help_copy_impl(var1).help_copy().remove(var1);
               }

               if ((var6 & var4) == 0L) {
                  return false;
               }
            } while(!var2.CAS(var3 >> 6, var6, var6 & ~var4));

            this._size.add(-1L);
            return true;
         }
      }

      public boolean contains(int var1) {
         if (var1 >> 6 >= this._bits.length) {
            return this._new == null ? false : this.help_copy().contains(var1);
         } else {
            NonBlockingSetInt.NBSI var2 = this;

            int var3;
            for(var3 = var1; (var3 & 63) == 63; var3 >>= 6) {
               var2 = var2._nbsi64;
            }

            long var4 = mask(var3);
            long var6 = var2._bits[var3 >> 6];
            if (var6 < 0L) {
               return this.help_copy_impl(var1).help_copy().contains(var1);
            } else {
               return (var6 & var4) != 0L;
            }
         }
      }

      public boolean intersect(NonBlockingSetInt.NBSI var1, NonBlockingSetInt.NBSI var2, NonBlockingSetInt.NBSI var3) {
         if (has_bits(var2) && has_bits(var3)) {
            for(int var4 = 0; var4 < var1._bits.length; ++var4) {
               long var5 = var2.safe_read_word(var4, 0L);
               long var7 = var3.safe_read_word(var4, 0L);
               var1._bits[var4] = var5 & var7 & Long.MAX_VALUE;
            }

            return this.intersect(var1._nbsi64, var2._nbsi64, var3._nbsi64);
         } else {
            return true;
         }
      }

      public boolean union(NonBlockingSetInt.NBSI var1, NonBlockingSetInt.NBSI var2, NonBlockingSetInt.NBSI var3) {
         if (!has_bits(var2) && !has_bits(var3)) {
            return true;
         } else {
            if (has_bits(var2) || has_bits(var3)) {
               for(int var4 = 0; var4 < var1._bits.length; ++var4) {
                  long var5 = var2.safe_read_word(var4, 0L);
                  long var7 = var3.safe_read_word(var4, 0L);
                  var1._bits[var4] = (var5 | var7) & Long.MAX_VALUE;
               }
            }

            return this.union(var1._nbsi64, var2._nbsi64, var3._nbsi64);
         }
      }

      private long safe_read_word(int var1, long var2) {
         if (var1 >= this._bits.length) {
            return var2;
         } else {
            long var4 = this._bits[var1];
            if (var4 < 0L) {
               var4 = this.help_copy_impl(var1).help_copy()._bits[var1];
            }

            return var4;
         }
      }

      public int sizeInBytes() {
         return this._bits.length;
      }

      public int size() {
         return (int)this._size.get();
      }

      private NonBlockingSetInt.NBSI install_larger_new_bits(int var1) {
         if (this._new == null) {
            int var2 = this._bits.length << 6 << 1;
            this.CAS_new(new NonBlockingSetInt.NBSI(var2, this._size, this._non_blocking_set_int));
         }

         return this;
      }

      private NonBlockingSetInt.NBSI help_copy() {
         NonBlockingSetInt.NBSI var1 = this._non_blocking_set_int._nbsi;
         int var2 = var1._copyIdx.getAndAdd(512);

         for(int var3 = 0; var3 < 8; ++var3) {
            int var4 = var2 + var3 * 64;
            var4 %= var1._bits.length << 6;
            var1.help_copy_impl(var4);
            var1.help_copy_impl(var4 + 63);
         }

         if (var1._copyDone.get() == var1._sum_bits_length && this._non_blocking_set_int.CAS_nbsi(var1, var1._new)) {
         }

         return this._new;
      }

      private NonBlockingSetInt.NBSI help_copy_impl(int var1) {
         NonBlockingSetInt.NBSI var2 = this;
         NonBlockingSetInt.NBSI var3 = this._new;
         if (var3 == null) {
            return this;
         } else {
            int var4;
            for(var4 = var1; (var4 & 63) == 63; var4 >>= 6) {
               var2 = var2._nbsi64;
               var3 = var3._nbsi64;
            }

            long var5;
            long var7;
            for(var5 = var2._bits[var4 >> 6]; var5 >= 0L; var5 = var2._bits[var4 >> 6]) {
               var7 = var5;
               var5 |= mask(63);
               if (var2.CAS(var4 >> 6, var7, var5)) {
                  if (var7 == 0L) {
                     this._copyDone.addAndGet(1);
                  }
                  break;
               }
            }

            if (var5 != mask(63)) {
               var7 = var3._bits[var4 >> 6];
               if (var7 == 0L) {
                  var7 = var5 & ~mask(63);
                  if (!var3.CAS(var4 >> 6, 0L, var7)) {
                     var7 = var3._bits[var4 >> 6];
                  }

                  assert var7 != 0L;
               }

               if (var2.CAS(var4 >> 6, var5, mask(63))) {
                  this._copyDone.addAndGet(1);
               }
            }

            return this;
         }
      }

      private void print(int var1, String var2) {
         for(int var3 = 0; var3 < var1; ++var3) {
            System.out.print("  ");
         }

         System.out.println(var2);
      }

      private void print(int var1) {
         StringBuffer var2 = new StringBuffer();
         var2.append("NBSI - _bits.len=");

         NonBlockingSetInt.NBSI var3;
         for(var3 = this; var3 != null; var3 = var3._nbsi64) {
            var2.append(" " + var3._bits.length);
         }

         this.print(var1, var2.toString());
         var3 = this;

         while(var3 != null) {
            for(int var4 = 0; var4 < var3._bits.length; ++var4) {
               long var10001 = var3._bits[var4];
               System.out.print(Long.toHexString(var10001) + " ");
            }

            var3 = var3._nbsi64;
            System.out.println();
         }

         if (this._copyIdx.get() != 0 || this._copyDone.get() != 0) {
            int var10002 = this._copyIdx.get();
            this.print(var1, "_copyIdx=" + var10002 + " _copyDone=" + this._copyDone.get() + " _words_to_cpy=" + this._sum_bits_length);
         }

         if (this._new != null) {
            this.print(var1, "__has_new - ");
            this._new.print(var1 + 1);
         }

      }

      static {
         _Lbase = NonBlockingSetInt._unsafe.arrayBaseOffset(long[].class);
         _Lscale = NonBlockingSetInt._unsafe.arrayIndexScale(long[].class);
         Field var0 = null;

         try {
            var0 = NonBlockingSetInt.NBSI.class.getDeclaredField("_new");
         } catch (NoSuchFieldException var2) {
         }

         _new_offset = NonBlockingSetInt._unsafe.objectFieldOffset(var0);
      }
   }

   private class iter implements Iterator {
      NonBlockingSetInt.NBSIIntIterator intIterator = NonBlockingSetInt.this.new NBSIIntIterator();

      iter() {
      }

      public boolean hasNext() {
         return this.intIterator.hasNext();
      }

      public Integer next() {
         return this.intIterator.next();
      }

      public void remove() {
         this.intIterator.remove();
      }
   }

   private class NBSIIntIterator implements IntIterator {
      NonBlockingSetInt.NBSI nbsi;
      int index = -1;
      int prev = -1;

      NBSIIntIterator() {
         this.nbsi = NonBlockingSetInt.this._nbsi;
         this.advance();
      }

      private void advance() {
         do {
            ++this.index;

            while(this.index >> 6 >= this.nbsi._bits.length) {
               if (this.nbsi._new == null) {
                  this.index = -2;
                  return;
               }

               this.nbsi = this.nbsi._new;
            }
         } while(!this.nbsi.contains(this.index));

      }

      public int next() {
         if (this.index == -1) {
            throw new NoSuchElementException();
         } else {
            this.prev = this.index;
            this.advance();
            return this.prev;
         }
      }

      public boolean hasNext() {
         return this.index != -2;
      }

      public void remove() {
         if (this.prev == -1) {
            throw new IllegalStateException();
         } else {
            this.nbsi.remove(this.prev);
            this.prev = -1;
         }
      }
   }
}

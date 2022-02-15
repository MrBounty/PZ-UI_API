package zombie.util.map;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.AbstractCollection;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import zombie.util.IntIterator;
import zombie.util.hash.DefaultIntHashFunction;
import zombie.util.hash.IntHashFunction;
import zombie.util.hash.Primes;
import zombie.util.set.AbstractIntSet;
import zombie.util.set.IntSet;
import zombie.util.util.Exceptions;

public class IntKeyOpenHashMap extends AbstractIntKeyMap implements IntKeyMap, Cloneable, Serializable {
   private static final long serialVersionUID = 1L;
   private static final int GROWTH_POLICY_RELATIVE = 0;
   private static final int GROWTH_POLICY_ABSOLUTE = 1;
   private static final int DEFAULT_GROWTH_POLICY = 0;
   public static final double DEFAULT_GROWTH_FACTOR = 1.0D;
   public static final int DEFAULT_GROWTH_CHUNK = 10;
   public static final int DEFAULT_CAPACITY = 11;
   public static final double DEFAULT_LOAD_FACTOR = 0.75D;
   private IntHashFunction keyhash;
   private int size;
   private transient int[] keys;
   private transient Object[] values;
   private transient byte[] states;
   private static final byte EMPTY = 0;
   private static final byte OCCUPIED = 1;
   private static final byte REMOVED = 2;
   private transient int used;
   private int growthPolicy;
   private double growthFactor;
   private int growthChunk;
   private double loadFactor;
   private int expandAt;
   private transient IntSet ckeys;
   private transient Collection cvalues;

   private IntKeyOpenHashMap(IntHashFunction var1, int var2, int var3, double var4, int var6, double var7) {
      if (var1 == null) {
         Exceptions.nullArgument("hash function");
      }

      if (var2 < 0) {
         Exceptions.negativeArgument("capacity", String.valueOf(var2));
      }

      if (var4 <= 0.0D) {
         Exceptions.negativeOrZeroArgument("growthFactor", String.valueOf(var4));
      }

      if (var6 <= 0) {
         Exceptions.negativeOrZeroArgument("growthChunk", String.valueOf(var6));
      }

      if (var7 <= 0.0D) {
         Exceptions.negativeOrZeroArgument("loadFactor", String.valueOf(var7));
      }

      this.keyhash = var1;
      var2 = Primes.nextPrime(var2);
      this.keys = new int[var2];
      this.values = new Object[var2];
      this.states = new byte[var2];
      this.size = 0;
      this.expandAt = (int)Math.round(var7 * (double)var2);
      this.used = 0;
      this.growthPolicy = var3;
      this.growthFactor = var4;
      this.growthChunk = var6;
      this.loadFactor = var7;
   }

   private IntKeyOpenHashMap(int var1, int var2, double var3, int var5, double var6) {
      this(DefaultIntHashFunction.INSTANCE, var1, var2, var3, var5, var6);
   }

   public IntKeyOpenHashMap() {
      this(11);
   }

   public IntKeyOpenHashMap(IntKeyMap var1) {
      this();
      this.putAll(var1);
   }

   public IntKeyOpenHashMap(int var1) {
      this(var1, 0, 1.0D, 10, 0.75D);
   }

   public IntKeyOpenHashMap(double var1) {
      this(11, 0, 1.0D, 10, var1);
   }

   public IntKeyOpenHashMap(int var1, double var2) {
      this(var1, 0, 1.0D, 10, var2);
   }

   public IntKeyOpenHashMap(int var1, double var2, double var4) {
      this(var1, 0, var4, 10, var2);
   }

   public IntKeyOpenHashMap(int var1, double var2, int var4) {
      this(var1, 1, 1.0D, var4, var2);
   }

   public IntKeyOpenHashMap(IntHashFunction var1) {
      this(var1, 11, 0, 1.0D, 10, 0.75D);
   }

   public IntKeyOpenHashMap(IntHashFunction var1, int var2) {
      this(var1, var2, 0, 1.0D, 10, 0.75D);
   }

   public IntKeyOpenHashMap(IntHashFunction var1, double var2) {
      this(var1, 11, 0, 1.0D, 10, var2);
   }

   public IntKeyOpenHashMap(IntHashFunction var1, int var2, double var3) {
      this(var1, var2, 0, 1.0D, 10, var3);
   }

   public IntKeyOpenHashMap(IntHashFunction var1, int var2, double var3, double var5) {
      this(var1, var2, 0, var5, 10, var3);
   }

   public IntKeyOpenHashMap(IntHashFunction var1, int var2, double var3, int var5) {
      this(var1, var2, 1, 1.0D, var5, var3);
   }

   private void ensureCapacity(int var1) {
      if (var1 >= this.expandAt) {
         int var2;
         if (this.growthPolicy == 0) {
            var2 = (int)((double)this.keys.length * (1.0D + this.growthFactor));
         } else {
            var2 = this.keys.length + this.growthChunk;
         }

         if ((double)var2 * this.loadFactor < (double)var1) {
            var2 = (int)Math.round((double)var1 / this.loadFactor);
         }

         var2 = Primes.nextPrime(var2);
         this.expandAt = (int)Math.round(this.loadFactor * (double)var2);
         int[] var3 = new int[var2];
         Object[] var4 = new Object[var2];
         byte[] var5 = new byte[var2];
         this.used = 0;

         for(int var6 = 0; var6 < this.keys.length; ++var6) {
            if (this.states[var6] == 1) {
               ++this.used;
               int var7 = this.keys[var6];
               Object var8 = this.values[var6];
               int var9 = Math.abs(this.keyhash.hash(var7));
               int var10 = var9 % var2;
               if (var5[var10] == 1) {
                  int var11 = 1 + var9 % (var2 - 2);

                  do {
                     var10 -= var11;
                     if (var10 < 0) {
                        var10 += var2;
                     }
                  } while(var5[var10] != 0);
               }

               var5[var10] = 1;
               var4[var10] = var8;
               var3[var10] = var7;
            }
         }

         this.keys = var3;
         this.values = var4;
         this.states = var5;
      }

   }

   public IntSet keySet() {
      if (this.ckeys == null) {
         this.ckeys = new IntKeyOpenHashMap.KeySet();
      }

      return this.ckeys;
   }

   public Object put(int var1, Object var2) {
      int var3 = Math.abs(this.keyhash.hash(var1));
      int var4 = var3 % this.keys.length;
      if (this.states[var4] == 1) {
         if (this.keys[var4] == var1) {
            Object var7 = this.values[var4];
            this.values[var4] = var2;
            return var7;
         }

         int var5 = 1 + var3 % (this.keys.length - 2);

         while(true) {
            var4 -= var5;
            if (var4 < 0) {
               var4 += this.keys.length;
            }

            if (this.states[var4] == 0 || this.states[var4] == 2) {
               break;
            }

            if (this.states[var4] == 1 && this.keys[var4] == var1) {
               Object var6 = this.values[var4];
               this.values[var4] = var2;
               return var6;
            }
         }
      }

      if (this.states[var4] == 0) {
         ++this.used;
      }

      this.states[var4] = 1;
      this.keys[var4] = var1;
      this.values[var4] = var2;
      ++this.size;
      this.ensureCapacity(this.used);
      return null;
   }

   public Collection values() {
      if (this.cvalues == null) {
         this.cvalues = new IntKeyOpenHashMap.ValueCollection();
      }

      return this.cvalues;
   }

   public Object clone() {
      try {
         IntKeyOpenHashMap var1 = (IntKeyOpenHashMap)super.clone();
         var1.keys = new int[this.keys.length];
         System.arraycopy(this.keys, 0, var1.keys, 0, this.keys.length);
         var1.values = new Object[this.values.length];
         System.arraycopy(this.values, 0, var1.values, 0, this.values.length);
         var1.states = new byte[this.states.length];
         System.arraycopy(this.states, 0, var1.states, 0, this.states.length);
         var1.cvalues = null;
         var1.ckeys = null;
         return var1;
      } catch (CloneNotSupportedException var2) {
         Exceptions.cloning();
         return null;
      }
   }

   public IntKeyMapIterator entries() {
      return new IntKeyMapIterator() {
         int nextEntry = this.nextEntry(0);
         int lastEntry = -1;

         int nextEntry(int var1) {
            while(var1 < IntKeyOpenHashMap.this.keys.length && IntKeyOpenHashMap.this.states[var1] != 1) {
               ++var1;
            }

            return var1;
         }

         public boolean hasNext() {
            return this.nextEntry < IntKeyOpenHashMap.this.keys.length;
         }

         public void next() {
            if (!this.hasNext()) {
               Exceptions.endOfIterator();
            }

            this.lastEntry = this.nextEntry;
            this.nextEntry = this.nextEntry(this.nextEntry + 1);
         }

         public void remove() {
            if (this.lastEntry == -1) {
               Exceptions.noElementToRemove();
            }

            IntKeyOpenHashMap.this.states[this.lastEntry] = 2;
            IntKeyOpenHashMap.this.values[this.lastEntry] = null;
            --IntKeyOpenHashMap.this.size;
            this.lastEntry = -1;
         }

         public int getKey() {
            if (this.lastEntry == -1) {
               Exceptions.noElementToGet();
            }

            return IntKeyOpenHashMap.this.keys[this.lastEntry];
         }

         public Object getValue() {
            if (this.lastEntry == -1) {
               Exceptions.noElementToGet();
            }

            return IntKeyOpenHashMap.this.values[this.lastEntry];
         }
      };
   }

   public void clear() {
      Arrays.fill(this.states, (byte)0);
      Arrays.fill(this.values, (Object)null);
      this.size = 0;
      this.used = 0;
   }

   public boolean containsKey(int var1) {
      int var2 = Math.abs(this.keyhash.hash(var1));
      int var3 = var2 % this.keys.length;
      if (this.states[var3] == 0) {
         return false;
      } else if (this.states[var3] == 1 && this.keys[var3] == var1) {
         return true;
      } else {
         int var4 = 1 + var2 % (this.keys.length - 2);

         do {
            var3 -= var4;
            if (var3 < 0) {
               var3 += this.keys.length;
            }

            if (this.states[var3] == 0) {
               return false;
            }
         } while(this.states[var3] != 1 || this.keys[var3] != var1);

         return true;
      }
   }

   public boolean containsValue(Object var1) {
      int var2;
      if (var1 == null) {
         for(var2 = 0; var2 < this.states.length; ++var2) {
            if (this.states[var2] == 1 && this.values[var2] == null) {
               return true;
            }
         }
      } else {
         for(var2 = 0; var2 < this.states.length; ++var2) {
            if (this.states[var2] == 1 && var1.equals(this.values[var2])) {
               return true;
            }
         }
      }

      return false;
   }

   public Object get(int var1) {
      int var2 = Math.abs(this.keyhash.hash(var1));
      int var3 = var2 % this.keys.length;
      if (this.states[var3] == 0) {
         return null;
      } else if (this.states[var3] == 1 && this.keys[var3] == var1) {
         return this.values[var3];
      } else {
         int var4 = 1 + var2 % (this.keys.length - 2);

         do {
            var3 -= var4;
            if (var3 < 0) {
               var3 += this.keys.length;
            }

            if (this.states[var3] == 0) {
               return null;
            }
         } while(this.states[var3] != 1 || this.keys[var3] != var1);

         return this.values[var3];
      }
   }

   public boolean isEmpty() {
      return this.size == 0;
   }

   public Object remove(int var1) {
      int var2 = Math.abs(this.keyhash.hash(var1));
      int var3 = var2 % this.keys.length;
      if (this.states[var3] == 0) {
         return null;
      } else if (this.states[var3] == 1 && this.keys[var3] == var1) {
         Object var6 = this.values[var3];
         this.values[var3] = null;
         this.states[var3] = 2;
         --this.size;
         return var6;
      } else {
         int var4 = 1 + var2 % (this.keys.length - 2);

         do {
            var3 -= var4;
            if (var3 < 0) {
               var3 += this.keys.length;
            }

            if (this.states[var3] == 0) {
               return null;
            }
         } while(this.states[var3] != 1 || this.keys[var3] != var1);

         Object var5 = this.values[var3];
         this.values[var3] = null;
         this.states[var3] = 2;
         --this.size;
         return var5;
      }
   }

   public int size() {
      return this.size;
   }

   private void writeObject(ObjectOutputStream var1) throws IOException {
      var1.defaultWriteObject();
      var1.writeInt(this.keys.length);
      IntKeyMapIterator var2 = this.entries();

      while(var2.hasNext()) {
         var2.next();
         var1.writeInt(var2.getKey());
         var1.writeObject(var2.getValue());
      }

   }

   private void readObject(ObjectInputStream var1) throws IOException, ClassNotFoundException {
      var1.defaultReadObject();
      this.keys = new int[var1.readInt()];
      this.states = new byte[this.keys.length];
      this.values = new Object[this.keys.length];
      this.used = this.size;

      for(int var2 = 0; var2 < this.size; ++var2) {
         int var3 = var1.readInt();
         Object var4 = var1.readObject();
         int var5 = Math.abs(this.keyhash.hash(var3));
         int var6 = var5 % this.keys.length;
         if (this.states[var6] != 0) {
            int var7 = 1 + var5 % (this.keys.length - 2);

            do {
               var6 -= var7;
               if (var6 < 0) {
                  var6 += this.keys.length;
               }
            } while(this.states[var6] != 0);
         }

         this.states[var6] = 1;
         this.keys[var6] = var3;
         this.values[var6] = var4;
      }

   }

   private class KeySet extends AbstractIntSet {
      public void clear() {
         IntKeyOpenHashMap.this.clear();
      }

      public boolean contains(int var1) {
         return IntKeyOpenHashMap.this.containsKey(var1);
      }

      public IntIterator iterator() {
         return new IntIterator() {
            int nextEntry = this.nextEntry(0);
            int lastEntry = -1;

            int nextEntry(int var1) {
               while(var1 < IntKeyOpenHashMap.this.keys.length && IntKeyOpenHashMap.this.states[var1] != 1) {
                  ++var1;
               }

               return var1;
            }

            public boolean hasNext() {
               return this.nextEntry < IntKeyOpenHashMap.this.keys.length;
            }

            public int next() {
               if (!this.hasNext()) {
                  Exceptions.endOfIterator();
               }

               this.lastEntry = this.nextEntry;
               this.nextEntry = this.nextEntry(this.nextEntry + 1);
               return IntKeyOpenHashMap.this.keys[this.lastEntry];
            }

            public void remove() {
               if (this.lastEntry == -1) {
                  Exceptions.noElementToRemove();
               }

               IntKeyOpenHashMap.this.states[this.lastEntry] = 2;
               IntKeyOpenHashMap.this.values[this.lastEntry] = null;
               --IntKeyOpenHashMap.this.size;
               this.lastEntry = -1;
            }
         };
      }

      public boolean remove(int var1) {
         boolean var2 = IntKeyOpenHashMap.this.containsKey(var1);
         if (var2) {
            IntKeyOpenHashMap.this.remove(var1);
         }

         return var2;
      }

      public int size() {
         return IntKeyOpenHashMap.this.size;
      }
   }

   private class ValueCollection extends AbstractCollection {
      public void clear() {
         IntKeyOpenHashMap.this.clear();
      }

      public boolean contains(Object var1) {
         return IntKeyOpenHashMap.this.containsValue(var1);
      }

      public Iterator iterator() {
         return new Iterator() {
            int nextEntry = this.nextEntry(0);
            int lastEntry = -1;

            int nextEntry(int var1) {
               while(var1 < IntKeyOpenHashMap.this.keys.length && IntKeyOpenHashMap.this.states[var1] != 1) {
                  ++var1;
               }

               return var1;
            }

            public boolean hasNext() {
               return this.nextEntry < IntKeyOpenHashMap.this.keys.length;
            }

            public Object next() {
               if (!this.hasNext()) {
                  Exceptions.endOfIterator();
               }

               this.lastEntry = this.nextEntry;
               this.nextEntry = this.nextEntry(this.nextEntry + 1);
               return IntKeyOpenHashMap.this.values[this.lastEntry];
            }

            public void remove() {
               if (this.lastEntry == -1) {
                  Exceptions.noElementToRemove();
               }

               IntKeyOpenHashMap.this.states[this.lastEntry] = 2;
               IntKeyOpenHashMap.this.values[this.lastEntry] = null;
               --IntKeyOpenHashMap.this.size;
               this.lastEntry = -1;
            }
         };
      }

      public int size() {
         return IntKeyOpenHashMap.this.size;
      }
   }
}

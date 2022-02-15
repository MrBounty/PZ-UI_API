package org.lwjglx.util.glu.tessellation;

class PriorityQSort extends PriorityQ {
   PriorityQHeap heap;
   Object[] keys;
   int[] order;
   int size;
   int max;
   boolean initialized;
   PriorityQ.Leq leq;

   PriorityQSort(PriorityQ.Leq var1) {
      this.heap = new PriorityQHeap(var1);
      this.keys = new Object[32];
      this.size = 0;
      this.max = 32;
      this.initialized = false;
      this.leq = var1;
   }

   void pqDeletePriorityQ() {
      if (this.heap != null) {
         this.heap.pqDeletePriorityQ();
      }

      this.order = null;
      this.keys = null;
   }

   private static boolean LT(PriorityQ.Leq var0, Object var1, Object var2) {
      return !PriorityQHeap.LEQ(var0, var2, var1);
   }

   private static boolean GT(PriorityQ.Leq var0, Object var1, Object var2) {
      return !PriorityQHeap.LEQ(var0, var1, var2);
   }

   private static void Swap(int[] var0, int var1, int var2) {
      int var3 = var0[var1];
      var0[var1] = var0[var2];
      var0[var2] = var3;
   }

   boolean pqInit() {
      PriorityQSort.Stack[] var6 = new PriorityQSort.Stack[50];

      int var7;
      for(var7 = 0; var7 < var6.length; ++var7) {
         var6[var7] = new PriorityQSort.Stack();
      }

      byte var10 = 0;
      int var8 = 2016473283;
      this.order = new int[this.size + 1];
      byte var1 = 0;
      int var2 = this.size - 1;
      int var5 = 0;

      int var3;
      for(var3 = var1; var3 <= var2; ++var3) {
         this.order[var3] = var5++;
      }

      var6[var10].p = var1;
      var6[var10].r = var2;
      var7 = var10 + 1;

      while(true) {
         --var7;
         if (var7 < 0) {
            this.max = this.size;
            this.initialized = true;
            this.heap.pqInit();
            return true;
         }

         int var9 = var6[var7].p;
         var2 = var6[var7].r;

         int var4;
         while(var2 > var9 + 10) {
            var8 = Math.abs(var8 * 1539415821 + 1);
            var3 = var9 + var8 % (var2 - var9 + 1);
            var5 = this.order[var3];
            this.order[var3] = this.order[var9];
            this.order[var9] = var5;
            var3 = var9 - 1;
            var4 = var2 + 1;

            while(true) {
               ++var3;
               if (!GT(this.leq, this.keys[this.order[var3]], this.keys[var5])) {
                  do {
                     --var4;
                  } while(LT(this.leq, this.keys[this.order[var4]], this.keys[var5]));

                  Swap(this.order, var3, var4);
                  if (var3 >= var4) {
                     Swap(this.order, var3, var4);
                     if (var3 - var9 < var2 - var4) {
                        var6[var7].p = var4 + 1;
                        var6[var7].r = var2;
                        ++var7;
                        var2 = var3 - 1;
                     } else {
                        var6[var7].p = var9;
                        var6[var7].r = var3 - 1;
                        ++var7;
                        var9 = var4 + 1;
                     }
                     break;
                  }
               }
            }
         }

         for(var3 = var9 + 1; var3 <= var2; ++var3) {
            var5 = this.order[var3];

            for(var4 = var3; var4 > var9 && LT(this.leq, this.keys[this.order[var4 - 1]], this.keys[var5]); --var4) {
               this.order[var4] = this.order[var4 - 1];
            }

            this.order[var4] = var5;
         }
      }
   }

   int pqInsert(Object var1) {
      if (this.initialized) {
         return this.heap.pqInsert(var1);
      } else {
         int var2 = this.size;
         if (++this.size >= this.max) {
            Object[] var3 = this.keys;
            this.max <<= 1;
            Object[] var4 = new Object[this.max];
            System.arraycopy(this.keys, 0, var4, 0, this.keys.length);
            this.keys = var4;
            if (this.keys == null) {
               this.keys = var3;
               return Integer.MAX_VALUE;
            }
         }

         assert var2 != Integer.MAX_VALUE;

         this.keys[var2] = var1;
         return -(var2 + 1);
      }
   }

   Object pqExtractMin() {
      if (this.size == 0) {
         return this.heap.pqExtractMin();
      } else {
         Object var1 = this.keys[this.order[this.size - 1]];
         if (!this.heap.pqIsEmpty()) {
            Object var2 = this.heap.pqMinimum();
            if (LEQ(this.leq, var2, var1)) {
               return this.heap.pqExtractMin();
            }
         }

         do {
            --this.size;
         } while(this.size > 0 && this.keys[this.order[this.size - 1]] == null);

         return var1;
      }
   }

   Object pqMinimum() {
      if (this.size == 0) {
         return this.heap.pqMinimum();
      } else {
         Object var1 = this.keys[this.order[this.size - 1]];
         if (!this.heap.pqIsEmpty()) {
            Object var2 = this.heap.pqMinimum();
            if (PriorityQHeap.LEQ(this.leq, var2, var1)) {
               return var2;
            }
         }

         return var1;
      }
   }

   boolean pqIsEmpty() {
      return this.size == 0 && this.heap.pqIsEmpty();
   }

   void pqDelete(int var1) {
      if (var1 >= 0) {
         this.heap.pqDelete(var1);
      } else {
         var1 = -(var1 + 1);

         assert var1 < this.max && this.keys[var1] != null;

         for(this.keys[var1] = null; this.size > 0 && this.keys[this.order[this.size - 1]] == null; --this.size) {
         }

      }
   }

   private static class Stack {
      int p;
      int r;
   }
}

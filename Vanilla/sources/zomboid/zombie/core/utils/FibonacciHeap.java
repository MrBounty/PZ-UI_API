package zombie.core.utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import zombie.iso.IsoGridSquare;

public final class FibonacciHeap {
   private FibonacciHeap.Entry mMin = null;
   private int mSize = 0;
   List treeTable = new ArrayList(300);
   List toVisit = new ArrayList(300);

   public void empty() {
      this.mMin = null;
      this.mSize = 0;
   }

   public FibonacciHeap.Entry enqueue(Object var1, double var2) {
      this.checkPriority(var2);
      FibonacciHeap.Entry var4 = new FibonacciHeap.Entry(var1, var2);
      this.mMin = mergeLists(this.mMin, var4);
      ++this.mSize;
      return var4;
   }

   public FibonacciHeap.Entry min() {
      if (this.isEmpty()) {
         throw new NoSuchElementException("Heap is empty.");
      } else {
         return this.mMin;
      }
   }

   public boolean isEmpty() {
      return this.mMin == null;
   }

   public int size() {
      return this.mSize;
   }

   public static FibonacciHeap merge(FibonacciHeap var0, FibonacciHeap var1) {
      FibonacciHeap var2 = new FibonacciHeap();
      var2.mMin = mergeLists(var0.mMin, var1.mMin);
      var2.mSize = var0.mSize + var1.mSize;
      var0.mSize = var1.mSize = 0;
      var0.mMin = null;
      var1.mMin = null;
      return var2;
   }

   public FibonacciHeap.Entry dequeueMin() {
      if (this.isEmpty()) {
         throw new NoSuchElementException("Heap is empty.");
      } else {
         --this.mSize;
         FibonacciHeap.Entry var1 = this.mMin;
         if (this.mMin.mNext == this.mMin) {
            this.mMin = null;
         } else {
            this.mMin.mPrev.mNext = this.mMin.mNext;
            this.mMin.mNext.mPrev = this.mMin.mPrev;
            this.mMin = this.mMin.mNext;
         }

         FibonacciHeap.Entry var2;
         if (var1.mChild != null) {
            var2 = var1.mChild;

            do {
               var2.mParent = null;
               var2 = var2.mNext;
            } while(var2 != var1.mChild);
         }

         this.mMin = mergeLists(this.mMin, var1.mChild);
         if (this.mMin == null) {
            return var1;
         } else {
            this.treeTable.clear();
            this.toVisit.clear();

            for(var2 = this.mMin; this.toVisit.isEmpty() || this.toVisit.get(0) != var2; var2 = var2.mNext) {
               this.toVisit.add(var2);
            }

            Iterator var7 = this.toVisit.iterator();

            label57:
            while(var7.hasNext()) {
               FibonacciHeap.Entry var3 = (FibonacciHeap.Entry)var7.next();

               while(true) {
                  while(var3.mDegree < this.treeTable.size()) {
                     if (this.treeTable.get(var3.mDegree) == null) {
                        this.treeTable.set(var3.mDegree, var3);
                        if (var3.mPriority <= this.mMin.mPriority) {
                           this.mMin = var3;
                        }
                        continue label57;
                     }

                     FibonacciHeap.Entry var4 = (FibonacciHeap.Entry)this.treeTable.get(var3.mDegree);
                     this.treeTable.set(var3.mDegree, (Object)null);
                     FibonacciHeap.Entry var5 = var4.mPriority < var3.mPriority ? var4 : var3;
                     FibonacciHeap.Entry var6 = var4.mPriority < var3.mPriority ? var3 : var4;
                     var6.mNext.mPrev = var6.mPrev;
                     var6.mPrev.mNext = var6.mNext;
                     var6.mNext = var6.mPrev = var6;
                     var5.mChild = mergeLists(var5.mChild, var6);
                     var6.mParent = var5;
                     var6.mIsMarked = false;
                     ++var5.mDegree;
                     var3 = var5;
                  }

                  this.treeTable.add((Object)null);
               }
            }

            return var1;
         }
      }
   }

   public void decreaseKey(FibonacciHeap.Entry var1, double var2) {
      this.checkPriority(var2);
      if (var2 > var1.mPriority) {
         throw new IllegalArgumentException("New priority exceeds old.");
      } else {
         this.decreaseKeyUnchecked(var1, var2);
      }
   }

   public void delete(FibonacciHeap.Entry var1) {
      this.decreaseKeyUnchecked(var1, Double.NEGATIVE_INFINITY);
      this.dequeueMin();
   }

   public void delete(int var1, IsoGridSquare var2) {
   }

   private void checkPriority(double var1) {
      if (Double.isNaN(var1)) {
         throw new IllegalArgumentException(var1 + " is invalid.");
      }
   }

   private static FibonacciHeap.Entry mergeLists(FibonacciHeap.Entry var0, FibonacciHeap.Entry var1) {
      if (var0 == null && var1 == null) {
         return null;
      } else if (var0 != null && var1 == null) {
         return var0;
      } else if (var0 == null && var1 != null) {
         return var1;
      } else {
         FibonacciHeap.Entry var2 = var0.mNext;
         var0.mNext = var1.mNext;
         var0.mNext.mPrev = var0;
         var1.mNext = var2;
         var1.mNext.mPrev = var1;
         return var0.mPriority < var1.mPriority ? var0 : var1;
      }
   }

   private void decreaseKeyUnchecked(FibonacciHeap.Entry var1, double var2) {
      var1.mPriority = var2;
      if (var1.mParent != null && var1.mPriority <= var1.mParent.mPriority) {
         this.cutNode(var1);
      }

      if (var1.mPriority <= this.mMin.mPriority) {
         this.mMin = var1;
      }

   }

   private void decreaseKeyUncheckedNode(FibonacciHeap.Entry var1, double var2) {
      var1.mPriority = var2;
      if (var1.mParent != null && var1.mPriority <= var1.mParent.mPriority) {
         this.cutNodeNode(var1);
      }

      if (var1.mPriority <= this.mMin.mPriority) {
         this.mMin = var1;
      }

   }

   private void cutNode(FibonacciHeap.Entry var1) {
      var1.mIsMarked = false;
      if (var1.mParent != null) {
         if (var1.mNext != var1) {
            var1.mNext.mPrev = var1.mPrev;
            var1.mPrev.mNext = var1.mNext;
         }

         if (var1.mParent.mChild == var1) {
            if (var1.mNext != var1) {
               var1.mParent.mChild = var1.mNext;
            } else {
               var1.mParent.mChild = null;
            }
         }

         --var1.mParent.mDegree;
         var1.mPrev = var1.mNext = var1;
         this.mMin = mergeLists(this.mMin, var1);
         if (var1.mParent.mIsMarked) {
            this.cutNode(var1.mParent);
         } else {
            var1.mParent.mIsMarked = true;
         }

         var1.mParent = null;
      }
   }

   private void cutNodeNode(FibonacciHeap.Entry var1) {
      var1.mIsMarked = false;
      if (var1.mParent != null) {
         if (var1.mNext != var1) {
            var1.mNext.mPrev = var1.mPrev;
            var1.mPrev.mNext = var1.mNext;
         }

         if (var1.mParent.mChild == var1) {
            if (var1.mNext != var1) {
               var1.mParent.mChild = var1.mNext;
            } else {
               var1.mParent.mChild = null;
            }
         }

         --var1.mParent.mDegree;
         var1.mPrev = var1.mNext = var1;
         this.mMin = mergeLists(this.mMin, var1);
         if (var1.mParent.mIsMarked) {
            this.cutNode(var1.mParent);
         } else {
            var1.mParent.mIsMarked = true;
         }

         var1.mParent = null;
      }
   }

   public static final class Entry {
      private int mDegree = 0;
      private boolean mIsMarked = false;
      private FibonacciHeap.Entry mNext;
      private FibonacciHeap.Entry mPrev;
      private FibonacciHeap.Entry mParent;
      private FibonacciHeap.Entry mChild;
      private Object mElem;
      private double mPriority;

      public Object getValue() {
         return this.mElem;
      }

      public void setValue(Object var1) {
         this.mElem = var1;
      }

      public double getPriority() {
         return this.mPriority;
      }

      private Entry(Object var1, double var2) {
         this.mNext = this.mPrev = this;
         this.mElem = var1;
         this.mPriority = var2;
      }
   }
}

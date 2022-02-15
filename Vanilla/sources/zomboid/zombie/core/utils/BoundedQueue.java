package zombie.core.utils;

import java.util.NoSuchElementException;

public class BoundedQueue {
   private int numElements;
   private int front;
   private int rear;
   private Object[] elements;

   public BoundedQueue(int var1) {
      this.numElements = var1;
      int var2 = Math.max(var1, 16);
      var2 = Integer.highestOneBit(var2 - 1) << 1;
      this.elements = new Object[var2];
   }

   public void add(Object var1) {
      if (var1 == null) {
         throw new NullPointerException();
      } else {
         if (this.size() == this.numElements) {
            this.removeFirst();
         }

         this.elements[this.rear] = var1;
         this.rear = this.rear + 1 & this.elements.length - 1;
      }
   }

   public Object removeFirst() {
      Object var1 = this.elements[this.front];
      if (var1 == null) {
         throw new NoSuchElementException();
      } else {
         this.elements[this.front] = null;
         this.front = this.front + 1 & this.elements.length - 1;
         return var1;
      }
   }

   public Object remove(int var1) {
      int var2 = this.front + var1 & this.elements.length - 1;
      Object var3 = this.elements[var2];
      if (var3 == null) {
         throw new NoSuchElementException();
      } else {
         int var4;
         int var5;
         for(var4 = var2; var4 != this.front; var4 = var5) {
            var5 = var4 - 1 & this.elements.length - 1;
            this.elements[var4] = this.elements[var5];
         }

         this.front = this.front + 1 & this.elements.length - 1;
         this.elements[var4] = null;
         return var3;
      }
   }

   public Object get(int var1) {
      int var2 = this.front + var1 & this.elements.length - 1;
      Object var3 = this.elements[var2];
      if (var3 == null) {
         throw new NoSuchElementException();
      } else {
         return var3;
      }
   }

   public void clear() {
      while(this.front != this.rear) {
         this.elements[this.front] = null;
         this.front = this.front + 1 & this.elements.length - 1;
      }

      this.front = this.rear = 0;
   }

   public int capacity() {
      return this.numElements;
   }

   public int size() {
      return this.front <= this.rear ? this.rear - this.front : this.rear + this.elements.length - this.front;
   }

   public boolean isEmpty() {
      return this.front == this.rear;
   }

   public boolean isFull() {
      return this.size() == this.capacity();
   }
}

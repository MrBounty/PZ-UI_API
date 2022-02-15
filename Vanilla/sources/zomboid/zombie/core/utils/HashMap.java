package zombie.core.utils;

public class HashMap {
   private int capacity = 2;
   private int elements = 0;
   private HashMap.Bucket[] buckets;

   public HashMap() {
      this.buckets = new HashMap.Bucket[this.capacity];

      for(int var1 = 0; var1 < this.capacity; ++var1) {
         this.buckets[var1] = new HashMap.Bucket();
      }

   }

   public void clear() {
      this.elements = 0;

      for(int var1 = 0; var1 < this.capacity; ++var1) {
         this.buckets[var1].clear();
      }

   }

   private void grow() {
      HashMap.Bucket[] var1 = this.buckets;
      this.capacity *= 2;
      this.elements = 0;
      this.buckets = new HashMap.Bucket[this.capacity];

      int var2;
      for(var2 = 0; var2 < this.capacity; ++var2) {
         this.buckets[var2] = new HashMap.Bucket();
      }

      for(var2 = 0; var2 < var1.length; ++var2) {
         HashMap.Bucket var3 = var1[var2];

         for(int var4 = 0; var4 < var3.size(); ++var4) {
            if (var3.keys[var4] != null) {
               this.put(var3.keys[var4], var3.values[var4]);
            }
         }
      }

   }

   public Object get(Object var1) {
      HashMap.Bucket var2 = this.buckets[Math.abs(var1.hashCode()) % this.capacity];

      for(int var3 = 0; var3 < var2.size(); ++var3) {
         if (var2.keys[var3] != null && var2.keys[var3].equals(var1)) {
            return var2.values[var3];
         }
      }

      return null;
   }

   public Object remove(Object var1) {
      HashMap.Bucket var2 = this.buckets[Math.abs(var1.hashCode()) % this.capacity];
      Object var3 = var2.remove(var1);
      if (var3 != null) {
         --this.elements;
         return var3;
      } else {
         return null;
      }
   }

   public Object put(Object var1, Object var2) {
      if (this.elements + 1 >= this.buckets.length) {
         this.grow();
      }

      Object var3 = this.remove(var1);
      HashMap.Bucket var4 = this.buckets[Math.abs(var1.hashCode()) % this.capacity];
      var4.put(var1, var2);
      ++this.elements;
      return var3;
   }

   public int size() {
      return this.elements;
   }

   public boolean isEmpty() {
      return this.size() == 0;
   }

   public HashMap.Iterator iterator() {
      return new HashMap.Iterator(this);
   }

   public String toString() {
      String var1 = new String();

      for(int var2 = 0; var2 < this.buckets.length; ++var2) {
         HashMap.Bucket var3 = this.buckets[var2];

         for(int var4 = 0; var4 < var3.size(); ++var4) {
            if (var3.keys[var4] != null) {
               if (var1.length() > 0) {
                  var1 = var1 + ", ";
               }

               var1 = var1 + var3.keys[var4] + "=" + var3.values[var4];
            }
         }
      }

      var1 = "HashMap[" + var1 + "]";
      return var1;
   }

   private static class Bucket {
      public Object[] keys;
      public Object[] values;
      public int count;
      public int nextIndex;

      public void put(Object var1, Object var2) throws IllegalStateException {
         if (this.keys == null) {
            this.grow();
            this.keys[0] = var1;
            this.values[0] = var2;
            this.nextIndex = 1;
            this.count = 1;
         } else {
            if (this.count == this.keys.length) {
               this.grow();
            }

            for(int var3 = 0; var3 < this.keys.length; ++var3) {
               if (this.keys[var3] == null) {
                  this.keys[var3] = var1;
                  this.values[var3] = var2;
                  ++this.count;
                  this.nextIndex = Math.max(this.nextIndex, var3 + 1);
                  return;
               }
            }

            throw new IllegalStateException("bucket is full");
         }
      }

      public Object remove(Object var1) {
         for(int var2 = 0; var2 < this.nextIndex; ++var2) {
            if (this.keys[var2] != null && this.keys[var2].equals(var1)) {
               Object var3 = this.values[var2];
               this.keys[var2] = null;
               this.values[var2] = null;
               --this.count;
               return var3;
            }
         }

         return null;
      }

      private void grow() {
         if (this.keys == null) {
            this.keys = new Object[2];
            this.values = new Object[2];
         } else {
            Object[] var1 = this.keys;
            Object[] var2 = this.values;
            this.keys = new Object[var1.length * 2];
            this.values = new Object[var2.length * 2];
            System.arraycopy(var1, 0, this.keys, 0, var1.length);
            System.arraycopy(var2, 0, this.values, 0, var2.length);
         }

      }

      public int size() {
         return this.nextIndex;
      }

      public void clear() {
         for(int var1 = 0; var1 < this.nextIndex; ++var1) {
            this.keys[var1] = null;
            this.values[var1] = null;
         }

         this.count = 0;
         this.nextIndex = 0;
      }
   }

   public static class Iterator {
      private HashMap hashMap;
      private int bucketIdx;
      private int keyValuePairIdx;
      private int elementIdx;
      private Object currentKey;
      private Object currentValue;

      public Iterator(HashMap var1) {
         this.hashMap = var1;
         this.reset();
      }

      public HashMap.Iterator reset() {
         this.bucketIdx = 0;
         this.keyValuePairIdx = 0;
         this.elementIdx = 0;
         this.currentKey = null;
         this.currentValue = null;
         return this;
      }

      public boolean hasNext() {
         return this.elementIdx < this.hashMap.elements;
      }

      public boolean advance() {
         while(this.bucketIdx < this.hashMap.buckets.length) {
            HashMap.Bucket var1 = this.hashMap.buckets[this.bucketIdx];
            if (this.keyValuePairIdx == var1.size()) {
               this.keyValuePairIdx = 0;
               ++this.bucketIdx;
            } else {
               while(this.keyValuePairIdx < var1.size()) {
                  if (var1.keys[this.keyValuePairIdx] != null) {
                     this.currentKey = var1.keys[this.keyValuePairIdx];
                     this.currentValue = var1.values[this.keyValuePairIdx];
                     ++this.keyValuePairIdx;
                     ++this.elementIdx;
                     return true;
                  }

                  ++this.keyValuePairIdx;
               }

               this.keyValuePairIdx = 0;
               ++this.bucketIdx;
            }
         }

         return false;
      }

      public Object getKey() {
         return this.currentKey;
      }

      public Object getValue() {
         return this.currentValue;
      }
   }
}

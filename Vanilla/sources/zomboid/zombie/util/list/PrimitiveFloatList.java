package zombie.util.list;

import java.util.AbstractList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Objects;
import java.util.RandomAccess;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;

public class PrimitiveFloatList extends AbstractList implements RandomAccess {
   private final float[] m_array;

   public PrimitiveFloatList(float[] var1) {
      this.m_array = (float[])Objects.requireNonNull(var1);
   }

   public int size() {
      return this.m_array.length;
   }

   public Object[] toArray() {
      return Arrays.asList(this.m_array).toArray();
   }

   public Object[] toArray(Object[] var1) {
      int var2 = this.size();

      for(int var3 = 0; var3 < var2 && var3 < var1.length; ++var3) {
         Float var4 = this.m_array[var3];
         var1[var3] = var4;
      }

      if (var1.length > var2) {
         var1[var2] = null;
      }

      return var1;
   }

   public Float get(int var1) {
      return this.m_array[var1];
   }

   public Float set(int var1, Float var2) {
      return this.set(var1, var2);
   }

   public float set(int var1, float var2) {
      float var3 = this.m_array[var1];
      this.m_array[var1] = var2;
      return var3;
   }

   public int indexOf(Object var1) {
      if (var1 == null) {
         return -1;
      } else {
         return var1 instanceof Number ? this.indexOf(((Number)var1).floatValue()) : -1;
      }
   }

   public int indexOf(float var1) {
      int var2 = -1;
      int var3 = 0;

      for(int var4 = this.size(); var3 < var4; ++var3) {
         if (this.m_array[var3] == var1) {
            var2 = var3;
            break;
         }
      }

      return var2;
   }

   public boolean contains(Object var1) {
      return this.indexOf(var1) != -1;
   }

   public boolean contains(float var1) {
      return this.indexOf(var1) != -1;
   }

   public void forEach(Consumer var1) {
      Objects.requireNonNull(var1);
      Objects.requireNonNull(var1);
      this.forEach(var1::accept);
   }

   public void forEach(FloatConsumer var1) {
      int var2 = 0;

      for(int var3 = this.size(); var2 < var3; ++var2) {
         var1.accept(this.m_array[var2]);
      }

   }

   public void replaceAll(UnaryOperator var1) {
      Objects.requireNonNull(var1);
      float[] var2 = this.m_array;

      for(int var3 = 0; var3 < var2.length; ++var3) {
         var2[var3] = (Float)var1.apply(var2[var3]);
      }

   }

   public void sort(Comparator var1) {
      this.sort();
   }

   public void sort() {
      Arrays.sort(this.m_array);
   }
}

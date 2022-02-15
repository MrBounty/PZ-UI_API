package zombie.util.list;

import java.util.AbstractList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.RandomAccess;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.UnaryOperator;

public final class PZConvertList extends AbstractList implements RandomAccess {
   private final List m_list;
   private final Function m_converterST;
   private final Function m_converterTS;

   public PZConvertList(List var1, Function var2) {
      this(var1, var2, (Function)null);
   }

   public PZConvertList(List var1, Function var2, Function var3) {
      this.m_list = (List)Objects.requireNonNull(var1);
      this.m_converterST = var2;
      this.m_converterTS = var3;
   }

   public boolean isReadonly() {
      return this.m_converterTS == null;
   }

   public int size() {
      return this.m_list.size();
   }

   public Object[] toArray() {
      return this.m_list.toArray();
   }

   public Object[] toArray(Object[] var1) {
      int var2 = this.size();

      for(int var3 = 0; var3 < var2 && var3 < var1.length; ++var3) {
         Object var4 = this.get(var3);
         var1[var3] = var4;
      }

      if (var1.length > var2) {
         var1[var2] = null;
      }

      return var1;
   }

   public Object get(int var1) {
      return this.convertST(this.m_list.get(var1));
   }

   public Object set(int var1, Object var2) {
      Object var3 = this.get(var1);
      this.setS(var1, this.convertTS(var2));
      return var3;
   }

   public Object setS(int var1, Object var2) {
      Object var3 = this.m_list.get(var1);
      this.m_list.set(var1, var2);
      return var3;
   }

   public int indexOf(Object var1) {
      int var2 = -1;
      int var3 = 0;

      for(int var4 = this.size(); var3 < var4; ++var3) {
         if (objectsEqual(var1, this.get(var3))) {
            var2 = var3;
            break;
         }
      }

      return var2;
   }

   private static boolean objectsEqual(Object var0, Object var1) {
      return var0 == var1 || var0 != null && var0.equals(var1);
   }

   public boolean contains(Object var1) {
      return this.indexOf(var1) != -1;
   }

   public void forEach(Consumer var1) {
      int var2 = 0;

      for(int var3 = this.size(); var2 < var3; ++var2) {
         var1.accept(this.get(var2));
      }

   }

   public void replaceAll(UnaryOperator var1) {
      Objects.requireNonNull(var1);
      int var2 = 0;

      for(int var3 = this.size(); var2 < var3; ++var2) {
         Object var4 = this.get(var2);
         Object var5 = var1.apply(var4);
         this.set(var2, var5);
      }

   }

   public void sort(Comparator var1) {
      this.m_list.sort((var2, var3) -> {
         return var1.compare(this.convertST(var2), this.convertST(var3));
      });
   }

   private Object convertST(Object var1) {
      return this.m_converterST.apply(var1);
   }

   private Object convertTS(Object var1) {
      return this.m_converterTS.apply(var1);
   }
}

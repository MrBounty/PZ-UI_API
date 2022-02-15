package zombie.util.list;

import java.util.Iterator;
import java.util.function.Function;

public final class PZConvertIterable implements Iterable {
   private final Iterable m_srcIterable;
   private final Function m_converter;

   public PZConvertIterable(Iterable var1, Function var2) {
      this.m_srcIterable = var1;
      this.m_converter = var2;
   }

   public Iterator iterator() {
      return new Iterator() {
         private Iterator m_srcIterator;

         {
            this.m_srcIterator = PZConvertIterable.this.m_srcIterable.iterator();
         }

         public boolean hasNext() {
            return this.m_srcIterator.hasNext();
         }

         public Object next() {
            return PZConvertIterable.this.m_converter.apply(this.m_srcIterator.next());
         }
      };
   }
}

package zombie.core.Collections;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.AbstractSet;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

public class ZomboidHashSet extends AbstractSet implements Set, Cloneable, Serializable {
   static final long serialVersionUID = -5024744406713321676L;
   private transient ZomboidHashMap map;
   private static final Object PRESENT = new Object();

   public ZomboidHashSet() {
      this.map = new ZomboidHashMap();
   }

   public ZomboidHashSet(Collection var1) {
      this.map = new ZomboidHashMap(Math.max((int)((float)var1.size() / 0.75F) + 1, 16));
      this.addAll(var1);
   }

   public ZomboidHashSet(int var1, float var2) {
      this.map = new ZomboidHashMap(var1);
   }

   public ZomboidHashSet(int var1) {
      this.map = new ZomboidHashMap(var1);
   }

   public Iterator iterator() {
      return this.map.keySet().iterator();
   }

   public int size() {
      return this.map.size();
   }

   public boolean isEmpty() {
      return this.map.isEmpty();
   }

   public boolean contains(Object var1) {
      return this.map.containsKey(var1);
   }

   public boolean add(Object var1) {
      return this.map.put(var1, PRESENT) == null;
   }

   public boolean remove(Object var1) {
      return this.map.remove(var1) == PRESENT;
   }

   public void clear() {
      this.map.clear();
   }

   public Object clone() {
      try {
         ZomboidHashSet var1 = (ZomboidHashSet)super.clone();
         var1.map = (ZomboidHashMap)this.map.clone();
         return var1;
      } catch (CloneNotSupportedException var2) {
         throw new InternalError();
      }
   }

   private void writeObject(ObjectOutputStream var1) throws IOException {
      var1.defaultWriteObject();
      var1.writeInt(this.map.size());
      Iterator var2 = this.map.keySet().iterator();

      while(var2.hasNext()) {
         var1.writeObject(var2.next());
      }

   }

   private void readObject(ObjectInputStream var1) throws IOException, ClassNotFoundException {
   }
}

package astar.datastructures;

import java.util.Comparator;
import java.util.HashMap;
import java.util.TreeMap;
import java.util.Map.Entry;

public class HashPriorityQueue {
   HashMap hashMap = new HashMap();
   TreeMap treeMap;

   public HashPriorityQueue(Comparator var1) {
      this.treeMap = new TreeMap(var1);
   }

   public int size() {
      return this.treeMap.size();
   }

   public boolean isEmpty() {
      return this.treeMap.isEmpty();
   }

   public boolean contains(Object var1) {
      return this.hashMap.containsKey(var1);
   }

   public Object get(Object var1) {
      return this.hashMap.get(var1);
   }

   public boolean add(Object var1, Object var2) {
      this.hashMap.put(var1, var2);
      this.treeMap.put(var2, var1);
      return true;
   }

   public boolean remove(Object var1, Object var2) {
      if (var2 == null) {
         var2 = this.hashMap.get(var1);
      }

      this.hashMap.remove(var1);
      this.treeMap.remove(var2);
      return true;
   }

   public Object poll() {
      Entry var1 = this.treeMap.pollFirstEntry();
      return var1.getKey();
   }

   public void clear() {
      this.hashMap.clear();
      this.treeMap.clear();
   }

   public HashMap getHashMap() {
      return this.hashMap;
   }

   public TreeMap getTreeMap() {
      return this.treeMap;
   }
}

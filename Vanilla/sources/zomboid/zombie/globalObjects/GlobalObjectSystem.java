package zombie.globalObjects;

import java.util.ArrayDeque;
import java.util.ArrayList;
import se.krka.kahlua.vm.KahluaTable;
import zombie.Lua.LuaManager;

public abstract class GlobalObjectSystem {
   private static final ArrayDeque objectListPool = new ArrayDeque();
   protected final String name;
   protected final KahluaTable modData;
   protected final ArrayList objects = new ArrayList();
   protected final GlobalObjectLookup lookup = new GlobalObjectLookup(this);

   GlobalObjectSystem(String var1) {
      this.name = var1;
      this.modData = LuaManager.platform.newTable();
   }

   public String getName() {
      return this.name;
   }

   public final KahluaTable getModData() {
      return this.modData;
   }

   protected abstract GlobalObject makeObject(int var1, int var2, int var3);

   public final GlobalObject newObject(int var1, int var2, int var3) {
      if (this.getObjectAt(var1, var2, var3) != null) {
         throw new IllegalStateException("already an object at " + var1 + "," + var2 + "," + var3);
      } else {
         GlobalObject var4 = this.makeObject(var1, var2, var3);
         this.objects.add(var4);
         this.lookup.addObject(var4);
         return var4;
      }
   }

   public final void removeObject(GlobalObject var1) throws IllegalArgumentException, IllegalStateException {
      if (var1 == null) {
         throw new NullPointerException("object is null");
      } else if (var1.system != this) {
         throw new IllegalStateException("object not in this system");
      } else {
         this.objects.remove(var1);
         this.lookup.removeObject(var1);
         var1.Reset();
      }
   }

   public final GlobalObject getObjectAt(int var1, int var2, int var3) {
      return this.lookup.getObjectAt(var1, var2, var3);
   }

   public final boolean hasObjectsInChunk(int var1, int var2) {
      return this.lookup.hasObjectsInChunk(var1, var2);
   }

   public final ArrayList getObjectsInChunk(int var1, int var2) {
      return this.lookup.getObjectsInChunk(var1, var2, this.allocList());
   }

   public final ArrayList getObjectsAdjacentTo(int var1, int var2, int var3) {
      return this.lookup.getObjectsAdjacentTo(var1, var2, var3, this.allocList());
   }

   public final int getObjectCount() {
      return this.objects.size();
   }

   public final GlobalObject getObjectByIndex(int var1) {
      return var1 >= 0 && var1 < this.objects.size() ? (GlobalObject)this.objects.get(var1) : null;
   }

   public final ArrayList allocList() {
      return objectListPool.isEmpty() ? new ArrayList() : (ArrayList)objectListPool.pop();
   }

   public final void finishedWithList(ArrayList var1) {
      if (var1 != null && !objectListPool.contains(var1)) {
         var1.clear();
         objectListPool.add(var1);
      }

   }

   public void Reset() {
      for(int var1 = 0; var1 < this.objects.size(); ++var1) {
         GlobalObject var2 = (GlobalObject)this.objects.get(var1);
         var2.Reset();
      }

      this.objects.clear();
      this.modData.wipe();
   }
}

package zombie.iso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import se.krka.kahlua.j2se.KahluaTableImpl;
import se.krka.kahlua.vm.KahluaTable;
import se.krka.kahlua.vm.KahluaTableIterator;
import zombie.MapCollisionData;
import zombie.iso.areas.isoregion.IsoRegions;
import zombie.iso.sprite.IsoSprite;
import zombie.iso.sprite.IsoSpriteManager;
import zombie.vehicles.PolygonalMap2;

public class BentFences {
   private static final BentFences instance = new BentFences();
   private final ArrayList m_entries = new ArrayList();
   private final HashMap m_bentMap = new HashMap();
   private final HashMap m_unbentMap = new HashMap();

   public static BentFences getInstance() {
      return instance;
   }

   private void tableToTiles(KahluaTableImpl var1, ArrayList var2) {
      if (var1 != null) {
         KahluaTableIterator var3 = var1.iterator();

         while(var3.advance()) {
            var2.add(var3.getValue().toString());
         }

      }
   }

   private void tableToTiles(KahluaTable var1, ArrayList var2, String var3) {
      this.tableToTiles((KahluaTableImpl)var1.rawget(var3), var2);
   }

   public void addFenceTiles(int var1, KahluaTableImpl var2) {
      KahluaTableIterator var3 = var2.iterator();

      while(true) {
         BentFences.Entry var5;
         do {
            do {
               if (!var3.advance()) {
                  return;
               }

               KahluaTableImpl var4 = (KahluaTableImpl)var3.getValue();
               var5 = new BentFences.Entry();
               var5.dir = IsoDirections.valueOf(var4.rawgetStr("dir"));
               this.tableToTiles(var4, var5.unbent, "unbent");
               this.tableToTiles(var4, var5.bent, "bent");
            } while(var5.unbent.isEmpty());
         } while(var5.unbent.size() != var5.bent.size());

         this.m_entries.add(var5);

         Iterator var6;
         String var7;
         ArrayList var8;
         for(var6 = var5.unbent.iterator(); var6.hasNext(); var8.add(var5)) {
            var7 = (String)var6.next();
            var8 = (ArrayList)this.m_unbentMap.get(var7);
            if (var8 == null) {
               var8 = new ArrayList();
               this.m_unbentMap.put(var7, var8);
            }
         }

         for(var6 = var5.bent.iterator(); var6.hasNext(); var8.add(var5)) {
            var7 = (String)var6.next();
            var8 = (ArrayList)this.m_bentMap.get(var7);
            if (var8 == null) {
               var8 = new ArrayList();
               this.m_bentMap.put(var7, var8);
            }
         }
      }
   }

   public boolean isBentObject(IsoObject var1) {
      return this.getEntryForObject(var1, (IsoDirections)null) != null;
   }

   public boolean isUnbentObject(IsoObject var1) {
      return this.getEntryForObject(var1, IsoDirections.Max) != null;
   }

   private BentFences.Entry getEntryForObject(IsoObject var1, IsoDirections var2) {
      if (var1 != null && var1.sprite != null && var1.sprite.name != null) {
         boolean var3 = var2 != null;
         ArrayList var4 = var3 ? (ArrayList)this.m_unbentMap.get(var1.sprite.name) : (ArrayList)this.m_bentMap.get(var1.sprite.name);
         if (var4 != null) {
            for(int var5 = 0; var5 < var4.size(); ++var5) {
               BentFences.Entry var6 = (BentFences.Entry)var4.get(var5);
               if ((!var3 || var2 == IsoDirections.Max || var2 == var6.dir) && this.isValidObject(var1, var6, var3)) {
                  return var6;
               }
            }
         }

         return null;
      } else {
         return null;
      }
   }

   private boolean isValidObject(IsoObject var1, BentFences.Entry var2, boolean var3) {
      IsoCell var4 = IsoWorld.instance.CurrentCell;
      ArrayList var5 = var3 ? var2.unbent : var2.bent;
      int var6 = ((String)var5.get(2)).equals(var1.sprite.name) ? 2 : (((String)var5.get(3)).equals(var1.sprite.name) ? 3 : -1);
      if (var6 == -1) {
         return false;
      } else {
         for(int var7 = 0; var7 < var5.size(); ++var7) {
            int var8 = var1.square.x + (var2.isNorth() ? var7 - var6 : 0);
            int var9 = var1.square.y + (var2.isNorth() ? 0 : var7 - var6);
            IsoGridSquare var10 = var4.getGridSquare(var8, var9, var1.square.z);
            if (var10 == null) {
               return false;
            }

            if (var6 != var7 && this.getObjectForEntry(var10, var5, var7) == null) {
               return false;
            }
         }

         return true;
      }
   }

   IsoObject getObjectForEntry(IsoGridSquare var1, ArrayList var2, int var3) {
      for(int var4 = 0; var4 < var1.getObjects().size(); ++var4) {
         IsoObject var5 = (IsoObject)var1.getObjects().get(var4);
         if (var5.sprite != null && var5.sprite.name != null && ((String)var2.get(var3)).equals(var5.sprite.name)) {
            return var5;
         }
      }

      return null;
   }

   public void swapTiles(IsoObject var1, IsoDirections var2) {
      boolean var3 = var2 != null;
      BentFences.Entry var4 = this.getEntryForObject(var1, var2);
      if (var4 != null) {
         if (var3) {
            if (var4.isNorth() && var2 != IsoDirections.N && var2 != IsoDirections.S) {
               return;
            }

            if (!var4.isNorth() && var2 != IsoDirections.W && var2 != IsoDirections.E) {
               return;
            }
         }

         IsoCell var5 = IsoWorld.instance.CurrentCell;
         ArrayList var6 = var3 ? var4.unbent : var4.bent;
         int var7 = ((String)var6.get(2)).equals(var1.sprite.name) ? 2 : (((String)var6.get(3)).equals(var1.sprite.name) ? 3 : -1);

         for(int var8 = 0; var8 < var6.size(); ++var8) {
            int var9 = var1.square.x + (var4.isNorth() ? var8 - var7 : 0);
            int var10 = var1.square.y + (var4.isNorth() ? 0 : var8 - var7);
            IsoGridSquare var11 = var5.getGridSquare(var9, var10, var1.square.z);
            if (var11 != null) {
               IsoObject var12 = this.getObjectForEntry(var11, var6, var8);
               if (var12 != null) {
                  String var13 = var3 ? (String)var4.bent.get(var8) : (String)var4.unbent.get(var8);
                  IsoSprite var14 = IsoSpriteManager.instance.getSprite(var13);
                  var14.name = var13;
                  var12.setSprite(var14);
                  var12.transmitUpdatedSprite();
                  var11.RecalcAllWithNeighbours(true);
                  MapCollisionData.instance.squareChanged(var11);
                  PolygonalMap2.instance.squareChanged(var11);
                  IsoRegions.squareChanged(var11);
               }
            }
         }

      }
   }

   public void bendFence(IsoObject var1, IsoDirections var2) {
      this.swapTiles(var1, var2);
   }

   public void unbendFence(IsoObject var1) {
      this.swapTiles(var1, (IsoDirections)null);
   }

   public void Reset() {
      this.m_entries.clear();
      this.m_bentMap.clear();
      this.m_unbentMap.clear();
   }

   private static final class Entry {
      IsoDirections dir;
      final ArrayList unbent;
      final ArrayList bent;

      private Entry() {
         this.dir = IsoDirections.Max;
         this.unbent = new ArrayList();
         this.bent = new ArrayList();
      }

      boolean isNorth() {
         return this.dir == IsoDirections.N || this.dir == IsoDirections.S;
      }
   }
}

package zombie.iso;

import gnu.trove.map.hash.THashMap;
import java.util.ArrayList;
import java.util.List;
import se.krka.kahlua.j2se.KahluaTableImpl;
import se.krka.kahlua.vm.KahluaTable;
import se.krka.kahlua.vm.KahluaTableIterator;
import zombie.MapCollisionData;
import zombie.SoundManager;
import zombie.core.Rand;
import zombie.core.properties.PropertyContainer;
import zombie.inventory.InventoryItemFactory;
import zombie.iso.SpriteDetails.IsoFlagType;
import zombie.iso.areas.isoregion.IsoRegions;
import zombie.iso.objects.IsoThumpable;
import zombie.iso.sprite.IsoSprite;
import zombie.iso.sprite.IsoSpriteManager;
import zombie.network.GameServer;
import zombie.util.list.PZArrayUtil;
import zombie.vehicles.PolygonalMap2;

public class BrokenFences {
   private static final BrokenFences instance = new BrokenFences();
   private final THashMap s_unbrokenMap = new THashMap();
   private final THashMap s_brokenLeftMap = new THashMap();
   private final THashMap s_brokenRightMap = new THashMap();
   private final THashMap s_allMap = new THashMap();

   public static BrokenFences getInstance() {
      return instance;
   }

   private ArrayList tableToTiles(KahluaTableImpl var1) {
      if (var1 == null) {
         return null;
      } else {
         ArrayList var2 = null;

         for(KahluaTableIterator var3 = var1.iterator(); var3.advance(); var2.add(var3.getValue().toString())) {
            if (var2 == null) {
               var2 = new ArrayList();
            }
         }

         return var2;
      }
   }

   private ArrayList tableToTiles(KahluaTable var1, String var2) {
      return this.tableToTiles((KahluaTableImpl)var1.rawget(var2));
   }

   public void addBrokenTiles(KahluaTableImpl var1) {
      KahluaTableIterator var2 = var1.iterator();

      while(var2.advance()) {
         String var3 = var2.getKey().toString();
         if (!"VERSION".equalsIgnoreCase(var3)) {
            KahluaTableImpl var4 = (KahluaTableImpl)var2.getValue();
            BrokenFences.Tile var5 = new BrokenFences.Tile();
            var5.self = this.tableToTiles(var4, "self");
            var5.left = this.tableToTiles(var4, "left");
            var5.right = this.tableToTiles(var4, "right");
            this.s_unbrokenMap.put(var3, var5);
            PZArrayUtil.forEach((List)var5.left, (var2x) -> {
               this.s_brokenLeftMap.put(var2x, var5);
            });
            PZArrayUtil.forEach((List)var5.right, (var2x) -> {
               this.s_brokenRightMap.put(var2x, var5);
            });
         }
      }

      this.s_allMap.putAll(this.s_unbrokenMap);
      this.s_allMap.putAll(this.s_brokenLeftMap);
      this.s_allMap.putAll(this.s_brokenRightMap);
   }

   public void addDebrisTiles(KahluaTableImpl var1) {
      KahluaTableIterator var2 = var1.iterator();

      while(var2.advance()) {
         String var3 = var2.getKey().toString();
         if (!"VERSION".equalsIgnoreCase(var3)) {
            KahluaTableImpl var4 = (KahluaTableImpl)var2.getValue();
            BrokenFences.Tile var5 = (BrokenFences.Tile)this.s_unbrokenMap.get(var3);
            if (var5 == null) {
               throw new IllegalArgumentException("addDebrisTiles() with unknown tile");
            }

            var5.debrisN = this.tableToTiles(var4, "north");
            var5.debrisS = this.tableToTiles(var4, "south");
            var5.debrisW = this.tableToTiles(var4, "west");
            var5.debrisE = this.tableToTiles(var4, "east");
         }
      }

   }

   public void setDestroyed(IsoObject var1) {
      var1.RemoveAttachedAnims();
      this.updateSprite(var1, true, true);
   }

   public void setDamagedLeft(IsoObject var1) {
      this.updateSprite(var1, true, false);
   }

   public void setDamagedRight(IsoObject var1) {
      this.updateSprite(var1, false, true);
   }

   public void updateSprite(IsoObject var1, boolean var2, boolean var3) {
      if (this.isBreakableObject(var1)) {
         BrokenFences.Tile var4 = (BrokenFences.Tile)this.s_allMap.get(var1.sprite.name);
         String var5 = null;
         if (var2 && var3) {
            var5 = var4.pickRandom(var4.self);
         } else if (var2) {
            var5 = var4.pickRandom(var4.left);
         } else if (var3) {
            var5 = var4.pickRandom(var4.right);
         }

         if (var5 != null) {
            IsoSprite var6 = IsoSpriteManager.instance.getSprite(var5);
            var6.name = var5;
            var1.setSprite(var6);
            var1.transmitUpdatedSprite();
            var1.getSquare().RecalcAllWithNeighbours(true);
            MapCollisionData.instance.squareChanged(var1.getSquare());
            PolygonalMap2.instance.squareChanged(var1.getSquare());
            IsoRegions.squareChanged(var1.getSquare());
         }

      }
   }

   private boolean isNW(IsoObject var1) {
      PropertyContainer var2 = var1.getProperties();
      return var2.Is(IsoFlagType.collideN) && var2.Is(IsoFlagType.collideW);
   }

   private void damageAdjacent(IsoGridSquare var1, IsoDirections var2, IsoDirections var3) {
      IsoGridSquare var4 = var1.getAdjacentSquare(var2);
      if (var4 != null) {
         boolean var5 = var2 == IsoDirections.W || var2 == IsoDirections.E;
         IsoObject var6 = this.getBreakableObject(var4, var5);
         if (var6 != null) {
            boolean var7 = var2 == IsoDirections.N || var2 == IsoDirections.E;
            boolean var8 = var2 == IsoDirections.S || var2 == IsoDirections.W;
            if (!this.isNW(var6) || var2 != IsoDirections.S && var2 != IsoDirections.E) {
               if (var7 && this.isBrokenRight(var6)) {
                  this.destroyFence(var6, var3);
               } else if (var8 && this.isBrokenLeft(var6)) {
                  this.destroyFence(var6, var3);
               } else {
                  this.updateSprite(var6, var7, var8);
               }
            }
         }
      }
   }

   public void destroyFence(IsoObject var1, IsoDirections var2) {
      if (this.isBreakableObject(var1)) {
         IsoGridSquare var3 = var1.getSquare();
         if (GameServer.bServer) {
            GameServer.PlayWorldSoundServer("BreakObject", false, var3, 1.0F, 20.0F, 1.0F, true);
         } else {
            SoundManager.instance.PlayWorldSound("BreakObject", var3, 1.0F, 20.0F, 1.0F, true);
         }

         boolean var4 = var1.getProperties().Is(IsoFlagType.collideN);
         boolean var5 = var1.getProperties().Is(IsoFlagType.collideW);
         if (var1 instanceof IsoThumpable) {
            IsoObject var6 = IsoObject.getNew();
            var6.setSquare(var3);
            var6.setSprite(var1.getSprite());
            int var7 = var1.getObjectIndex();
            var3.transmitRemoveItemFromSquare(var1);
            var3.transmitAddObjectToSquare(var6, var7);
            var1 = var6;
         }

         this.addDebrisObject(var1, var2);
         this.setDestroyed(var1);
         if (var4 && var5) {
            this.damageAdjacent(var3, IsoDirections.S, var2);
            this.damageAdjacent(var3, IsoDirections.E, var2);
         } else if (var4) {
            this.damageAdjacent(var3, IsoDirections.W, var2);
            this.damageAdjacent(var3, IsoDirections.E, var2);
         } else if (var5) {
            this.damageAdjacent(var3, IsoDirections.N, var2);
            this.damageAdjacent(var3, IsoDirections.S, var2);
         }

         var3.RecalcAllWithNeighbours(true);
         MapCollisionData.instance.squareChanged(var3);
         PolygonalMap2.instance.squareChanged(var3);
         IsoRegions.squareChanged(var3);
      }
   }

   private boolean isUnbroken(IsoObject var1) {
      return var1 != null && var1.sprite != null && var1.sprite.name != null ? this.s_unbrokenMap.contains(var1.sprite.name) : false;
   }

   private boolean isBrokenLeft(IsoObject var1) {
      return var1 != null && var1.sprite != null && var1.sprite.name != null ? this.s_brokenLeftMap.contains(var1.sprite.name) : false;
   }

   private boolean isBrokenRight(IsoObject var1) {
      return var1 != null && var1.sprite != null && var1.sprite.name != null ? this.s_brokenRightMap.contains(var1.sprite.name) : false;
   }

   public boolean isBreakableObject(IsoObject var1) {
      return var1 != null && var1.sprite != null && var1.sprite.name != null ? this.s_allMap.containsKey(var1.sprite.name) : false;
   }

   private IsoObject getBreakableObject(IsoGridSquare var1, boolean var2) {
      for(int var3 = 0; var3 < var1.Objects.size(); ++var3) {
         IsoObject var4 = (IsoObject)var1.Objects.get(var3);
         if (this.isBreakableObject(var4) && (var2 && var4.getProperties().Is(IsoFlagType.collideN) || !var2 && var4.getProperties().Is(IsoFlagType.collideW))) {
            return var4;
         }
      }

      return null;
   }

   private void addItems(IsoObject var1, IsoGridSquare var2) {
      PropertyContainer var3 = var1.getProperties();
      if (var3 != null) {
         String var4 = var3.Val("Material");
         String var5 = var3.Val("Material2");
         String var6 = var3.Val("Material3");
         if ("Wood".equals(var4) || "Wood".equals(var5) || "Wood".equals(var6)) {
            var2.AddWorldInventoryItem(InventoryItemFactory.CreateItem("Base.Plank"), Rand.Next(0.0F, 0.5F), Rand.Next(0.0F, 0.5F), 0.0F);
            if (Rand.NextBool(5)) {
               var2.AddWorldInventoryItem(InventoryItemFactory.CreateItem("Base.Plank"), Rand.Next(0.0F, 0.5F), Rand.Next(0.0F, 0.5F), 0.0F);
            }
         }

         if (("MetalBars".equals(var4) || "MetalBars".equals(var5) || "MetalBars".equals(var6)) && Rand.NextBool(2)) {
            var2.AddWorldInventoryItem(InventoryItemFactory.CreateItem("Base.MetalBar"), Rand.Next(0.0F, 0.5F), Rand.Next(0.0F, 0.5F), 0.0F);
         }

         if (("MetalWire".equals(var4) || "MetalWire".equals(var5) || "MetalWire".equals(var6)) && Rand.NextBool(3)) {
            var2.AddWorldInventoryItem(InventoryItemFactory.CreateItem("Base.Wire"), Rand.Next(0.0F, 0.5F), Rand.Next(0.0F, 0.5F), 0.0F);
         }

         if (("Nails".equals(var4) || "Nails".equals(var5) || "Nails".equals(var6)) && Rand.NextBool(2)) {
            var2.AddWorldInventoryItem(InventoryItemFactory.CreateItem("Base.Nails"), Rand.Next(0.0F, 0.5F), Rand.Next(0.0F, 0.5F), 0.0F);
         }

         if (("Screws".equals(var4) || "Screws".equals(var5) || "Screws".equals(var6)) && Rand.NextBool(2)) {
            var2.AddWorldInventoryItem(InventoryItemFactory.CreateItem("Base.Screws"), Rand.Next(0.0F, 0.5F), Rand.Next(0.0F, 0.5F), 0.0F);
         }

      }
   }

   private void addDebrisObject(IsoObject var1, IsoDirections var2) {
      if (this.isBreakableObject(var1)) {
         BrokenFences.Tile var3 = (BrokenFences.Tile)this.s_allMap.get(var1.sprite.name);
         IsoGridSquare var5 = var1.getSquare();
         String var4;
         switch(var2) {
         case N:
            var4 = var3.pickRandom(var3.debrisN);
            var5 = var5.getAdjacentSquare(var2);
            break;
         case S:
            var4 = var3.pickRandom(var3.debrisS);
            break;
         case W:
            var4 = var3.pickRandom(var3.debrisW);
            var5 = var5.getAdjacentSquare(var2);
            break;
         case E:
            var4 = var3.pickRandom(var3.debrisE);
            break;
         default:
            throw new IllegalArgumentException("invalid direction");
         }

         if (var4 != null && var5 != null && var5.TreatAsSolidFloor()) {
            IsoObject var6 = IsoObject.getNew(var5, var4, (String)null, false);
            var5.transmitAddObjectToSquare(var6, var5 == var1.getSquare() ? var1.getObjectIndex() : -1);
            this.addItems(var1, var5);
         }

      }
   }

   public void Reset() {
      this.s_unbrokenMap.clear();
      this.s_brokenLeftMap.clear();
      this.s_brokenRightMap.clear();
      this.s_allMap.clear();
   }

   private static final class Tile {
      ArrayList self = null;
      ArrayList left = null;
      ArrayList right = null;
      ArrayList debrisN = null;
      ArrayList debrisS = null;
      ArrayList debrisW = null;
      ArrayList debrisE = null;

      String pickRandom(ArrayList var1) {
         return var1 == null ? null : (String)PZArrayUtil.pickRandom((List)var1);
      }
   }
}

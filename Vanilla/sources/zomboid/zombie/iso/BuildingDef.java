package zombie.iso;

import gnu.trove.list.array.TShortArrayList;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import se.krka.kahlua.vm.KahluaTable;
import zombie.Lua.LuaManager;
import zombie.core.Rand;
import zombie.core.stash.StashSystem;
import zombie.inventory.InventoryItem;
import zombie.inventory.ItemContainer;
import zombie.inventory.types.Food;
import zombie.iso.areas.IsoRoom;
import zombie.network.GameServer;
import zombie.network.ServerMap;

public final class BuildingDef {
   static final ArrayList squareChoices = new ArrayList();
   public final ArrayList emptyoutside = new ArrayList();
   public KahluaTable table = null;
   public boolean seen = false;
   public boolean hasBeenVisited = false;
   public String stash = null;
   public int lootRespawnHour = -1;
   public TShortArrayList overlappedChunks;
   public boolean bAlarmed = false;
   public int x = 10000000;
   public int y = 10000000;
   public int x2 = -10000000;
   public int y2 = -10000000;
   public final ArrayList rooms = new ArrayList();
   public IsoMetaGrid.Zone zone;
   public int food;
   public ArrayList items = new ArrayList();
   public HashSet itemTypes = new HashSet();
   int ID = 0;
   private int keySpawned = 0;
   private int keyId = -1;

   public BuildingDef() {
      this.table = LuaManager.platform.newTable();
      this.setKeyId(Rand.Next(100000000));
   }

   public KahluaTable getTable() {
      return this.table;
   }

   public ArrayList getRooms() {
      return this.rooms;
   }

   public RoomDef getRoom(String var1) {
      for(int var2 = 0; var2 < this.rooms.size(); ++var2) {
         RoomDef var3 = (RoomDef)this.rooms.get(var2);
         if (var3.getName().equalsIgnoreCase(var1)) {
            return var3;
         }
      }

      return null;
   }

   public boolean isAllExplored() {
      for(int var1 = 0; var1 < this.rooms.size(); ++var1) {
         if (!((RoomDef)this.rooms.get(var1)).bExplored) {
            return false;
         }
      }

      return true;
   }

   public void setAllExplored(boolean var1) {
      for(int var2 = 0; var2 < this.rooms.size(); ++var2) {
         RoomDef var3 = (RoomDef)this.rooms.get(var2);
         var3.setExplored(var1);
      }

   }

   public RoomDef getFirstRoom() {
      return (RoomDef)this.rooms.get(0);
   }

   public int getChunkX() {
      return this.x / 10;
   }

   public int getChunkY() {
      return this.y / 10;
   }

   public int getX() {
      return this.x;
   }

   public int getY() {
      return this.y;
   }

   public int getX2() {
      return this.x2;
   }

   public int getY2() {
      return this.y2;
   }

   public int getW() {
      return this.x2 - this.x;
   }

   public int getH() {
      return this.y2 - this.y;
   }

   public int getID() {
      return this.ID;
   }

   public void refreshSquares() {
      for(int var1 = 0; var1 < this.rooms.size(); ++var1) {
         RoomDef var2 = (RoomDef)this.rooms.get(var1);
         var2.refreshSquares();
      }

   }

   public void CalculateBounds(ArrayList var1) {
      int var2;
      RoomDef var3;
      int var4;
      RoomDef.RoomRect var5;
      for(var2 = 0; var2 < this.rooms.size(); ++var2) {
         var3 = (RoomDef)this.rooms.get(var2);

         for(var4 = 0; var4 < var3.rects.size(); ++var4) {
            var5 = (RoomDef.RoomRect)var3.rects.get(var4);
            if (var5.x < this.x) {
               this.x = var5.x;
            }

            if (var5.y < this.y) {
               this.y = var5.y;
            }

            if (var5.x + var5.w > this.x2) {
               this.x2 = var5.x + var5.w;
            }

            if (var5.y + var5.h > this.y2) {
               this.y2 = var5.y + var5.h;
            }
         }
      }

      for(var2 = 0; var2 < this.emptyoutside.size(); ++var2) {
         var3 = (RoomDef)this.emptyoutside.get(var2);

         for(var4 = 0; var4 < var3.rects.size(); ++var4) {
            var5 = (RoomDef.RoomRect)var3.rects.get(var4);
            if (var5.x < this.x) {
               this.x = var5.x;
            }

            if (var5.y < this.y) {
               this.y = var5.y;
            }

            if (var5.x + var5.w > this.x2) {
               this.x2 = var5.x + var5.w;
            }

            if (var5.y + var5.h > this.y2) {
               this.y2 = var5.y + var5.h;
            }
         }
      }

      var2 = this.x / 10;
      int var12 = this.y / 10;
      var4 = (this.x2 + 0) / 10;
      int var13 = (this.y2 + 0) / 10;
      this.overlappedChunks = new TShortArrayList((var4 - var2 + 1) * (var13 - var12 + 1) * 2);
      this.overlappedChunks.clear();
      var1.clear();
      var1.addAll(this.rooms);
      var1.addAll(this.emptyoutside);

      for(int var6 = 0; var6 < var1.size(); ++var6) {
         RoomDef var7 = (RoomDef)var1.get(var6);

         for(int var8 = 0; var8 < var7.rects.size(); ++var8) {
            RoomDef.RoomRect var9 = (RoomDef.RoomRect)var7.rects.get(var8);
            var2 = var9.x / 10;
            var12 = var9.y / 10;
            var4 = (var9.x + var9.w + 0) / 10;
            var13 = (var9.y + var9.h + 0) / 10;

            for(int var10 = var12; var10 <= var13; ++var10) {
               for(int var11 = var2; var11 <= var4; ++var11) {
                  if (!this.overlapsChunk(var11, var10)) {
                     this.overlappedChunks.add((short)var11);
                     this.overlappedChunks.add((short)var10);
                  }
               }
            }
         }
      }

   }

   public void recalculate() {
      this.food = 0;
      this.items.clear();
      this.itemTypes.clear();

      for(int var1 = 0; var1 < this.rooms.size(); ++var1) {
         IsoRoom var2 = ((RoomDef)this.rooms.get(var1)).getIsoRoom();

         for(int var3 = 0; var3 < var2.Containers.size(); ++var3) {
            ItemContainer var4 = (ItemContainer)var2.Containers.get(var3);

            for(int var5 = 0; var5 < var4.Items.size(); ++var5) {
               InventoryItem var6 = (InventoryItem)var4.Items.get(var5);
               this.items.add(var6);
               this.itemTypes.add(var6.getFullType());
               if (var6 instanceof Food) {
                  ++this.food;
               }
            }
         }
      }

   }

   public boolean overlapsChunk(int var1, int var2) {
      for(int var3 = 0; var3 < this.overlappedChunks.size(); var3 += 2) {
         if (var1 == this.overlappedChunks.get(var3) && var2 == this.overlappedChunks.get(var3 + 1)) {
            return true;
         }
      }

      return false;
   }

   public IsoGridSquare getFreeSquareInRoom() {
      squareChoices.clear();

      for(int var1 = 0; var1 < this.rooms.size(); ++var1) {
         RoomDef var2 = (RoomDef)this.rooms.get(var1);

         for(int var3 = 0; var3 < var2.rects.size(); ++var3) {
            RoomDef.RoomRect var4 = (RoomDef.RoomRect)var2.rects.get(var3);

            for(int var5 = var4.getX(); var5 < var4.getX2(); ++var5) {
               for(int var6 = var4.getY(); var6 < var4.getY2(); ++var6) {
                  IsoGridSquare var7 = IsoWorld.instance.CurrentCell.getGridSquare(var5, var6, var2.getZ());
                  if (var7 != null && var7.isFree(false)) {
                     squareChoices.add(var7);
                  }
               }
            }
         }
      }

      if (!squareChoices.isEmpty()) {
         return (IsoGridSquare)squareChoices.get(Rand.Next(squareChoices.size()));
      } else {
         return null;
      }
   }

   public boolean containsRoom(String var1) {
      for(int var2 = 0; var2 < this.rooms.size(); ++var2) {
         RoomDef var3 = (RoomDef)this.rooms.get(var2);
         if (var3.name.equals(var1)) {
            return true;
         }
      }

      return false;
   }

   public boolean isFullyStreamedIn() {
      for(int var1 = 0; var1 < this.overlappedChunks.size(); var1 += 2) {
         short var2 = this.overlappedChunks.get(var1);
         short var3 = this.overlappedChunks.get(var1 + 1);
         IsoChunk var4 = GameServer.bServer ? ServerMap.instance.getChunk(var2, var3) : IsoWorld.instance.CurrentCell.getChunk(var2, var3);
         if (var4 == null) {
            return false;
         }
      }

      return true;
   }

   public IsoMetaGrid.Zone getZone() {
      return this.zone;
   }

   public int getKeyId() {
      return this.keyId;
   }

   public void setKeyId(int var1) {
      this.keyId = var1;
   }

   public int getKeySpawned() {
      return this.keySpawned;
   }

   public void setKeySpawned(int var1) {
      this.keySpawned = var1;
   }

   public boolean isHasBeenVisited() {
      return this.hasBeenVisited;
   }

   public void setHasBeenVisited(boolean var1) {
      if (var1 && !this.hasBeenVisited) {
         StashSystem.visitedBuilding(this);
      }

      this.hasBeenVisited = var1;
   }

   public boolean isAlarmed() {
      return this.bAlarmed;
   }

   public void setAlarmed(boolean var1) {
      this.bAlarmed = var1;
   }

   public RoomDef getRandomRoom(int var1) {
      RoomDef var2 = (RoomDef)this.getRooms().get(Rand.Next(0, this.getRooms().size()));
      if (var1 > 0 && var2.area >= var1) {
         return var2;
      } else {
         int var3 = 0;

         do {
            if (var3 > 20) {
               return var2;
            }

            ++var3;
            var2 = (RoomDef)this.getRooms().get(Rand.Next(0, this.getRooms().size()));
         } while(var2.area < var1);

         return var2;
      }
   }

   public void Dispose() {
      Iterator var1 = this.rooms.iterator();

      while(var1.hasNext()) {
         RoomDef var2 = (RoomDef)var1.next();
         var2.Dispose();
      }

      this.emptyoutside.clear();
      this.rooms.clear();
   }
}

package zombie.iso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import zombie.iso.areas.IsoBuilding;
import zombie.iso.areas.IsoRoom;

public final class LotHeader {
   public int width = 0;
   public int height = 0;
   public int levels = 0;
   public int version = 0;
   public final HashMap Rooms = new HashMap();
   public final ArrayList RoomList = new ArrayList();
   public final ArrayList Buildings = new ArrayList();
   public final HashMap isoRooms = new HashMap();
   public final HashMap isoBuildings = new HashMap();
   public boolean bFixed2x;
   protected final ArrayList tilesUsed = new ArrayList();

   public int getHeight() {
      return this.height;
   }

   public int getWidth() {
      return this.width;
   }

   public int getLevels() {
      return this.levels;
   }

   public IsoRoom getRoom(int var1) {
      RoomDef var2 = (RoomDef)this.Rooms.get(var1);
      IsoRoom var3;
      if (!this.isoRooms.containsKey(var1)) {
         var3 = new IsoRoom();
         var3.rects.addAll(var2.rects);
         var3.RoomDef = var2.name;
         var3.def = var2;
         var3.layer = var2.level;
         IsoWorld.instance.CurrentCell.getRoomList().add(var3);
         if (var2.building == null) {
            var2.building = new BuildingDef();
            var2.building.ID = this.Buildings.size();
            var2.building.rooms.add(var2);
            var2.building.CalculateBounds(new ArrayList());
            this.Buildings.add(var2.building);
         }

         int var4 = var2.building.ID;
         this.isoRooms.put(var1, var3);
         if (!this.isoBuildings.containsKey(var4)) {
            var3.building = new IsoBuilding();
            var3.building.def = var2.building;
            this.isoBuildings.put(var4, var3.building);
            var3.building.CreateFrom(var2.building, this);
         } else {
            var3.building = (IsoBuilding)this.isoBuildings.get(var4);
         }

         return var3;
      } else {
         var3 = (IsoRoom)this.isoRooms.get(var1);
         return var3;
      }
   }

   /** @deprecated */
   @Deprecated
   public int getRoomAt(int var1, int var2, int var3) {
      Iterator var4 = this.Rooms.entrySet().iterator();

      while(var4.hasNext()) {
         Entry var5 = (Entry)var4.next();
         RoomDef var6 = (RoomDef)var5.getValue();

         for(int var7 = 0; var7 < var6.rects.size(); ++var7) {
            RoomDef.RoomRect var8 = (RoomDef.RoomRect)var6.rects.get(var7);
            if (var8.x <= var1 && var8.y <= var2 && var6.level == var3 && var8.getX2() > var1 && var8.getY2() > var2) {
               return (Integer)var5.getKey();
            }
         }
      }

      return -1;
   }
}

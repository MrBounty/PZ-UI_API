package zombie.popman;

import java.util.ArrayList;
import zombie.iso.BuildingDef;
import zombie.iso.IsoGridSquare;
import zombie.iso.IsoWorld;
import zombie.iso.RoomDef;

final class PlayerSpawns {
   private final ArrayList playerSpawns = new ArrayList();

   public void addSpawn(int var1, int var2, int var3) {
      PlayerSpawns.PlayerSpawn var4 = new PlayerSpawns.PlayerSpawn(var1, var2, var3);
      if (var4.building != null) {
         this.playerSpawns.add(var4);
      }

   }

   public void update() {
      long var1 = System.currentTimeMillis();

      for(int var3 = 0; var3 < this.playerSpawns.size(); ++var3) {
         PlayerSpawns.PlayerSpawn var4 = (PlayerSpawns.PlayerSpawn)this.playerSpawns.get(var3);
         if (var4.counter == -1L) {
            var4.counter = var1;
         }

         if (var4.counter + 10000L <= var1) {
            this.playerSpawns.remove(var3--);
         }
      }

   }

   public boolean allowZombie(IsoGridSquare var1) {
      for(int var2 = 0; var2 < this.playerSpawns.size(); ++var2) {
         PlayerSpawns.PlayerSpawn var3 = (PlayerSpawns.PlayerSpawn)this.playerSpawns.get(var2);
         if (!var3.allowZombie(var1)) {
            return false;
         }
      }

      return true;
   }

   private static class PlayerSpawn {
      public int x;
      public int y;
      public long counter;
      public BuildingDef building;

      public PlayerSpawn(int var1, int var2, int var3) {
         this.x = var1;
         this.y = var2;
         this.counter = -1L;
         RoomDef var4 = IsoWorld.instance.getMetaGrid().getRoomAt(var1, var2, var3);
         if (var4 != null) {
            this.building = var4.getBuilding();
         }

      }

      public boolean allowZombie(IsoGridSquare var1) {
         if (this.building == null) {
            return true;
         } else if (var1.getBuilding() != null && this.building == var1.getBuilding().getDef()) {
            return false;
         } else {
            return var1.getX() < this.building.getX() - 15 || var1.getX() >= this.building.getX2() + 15 || var1.getY() < this.building.getY() - 15 || var1.getY() >= this.building.getY2() + 15;
         }
      }
   }
}

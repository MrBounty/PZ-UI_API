package zombie.randomizedWorld.randomizedDeadSurvivor;

import java.util.ArrayList;
import zombie.characters.IsoPlayer;
import zombie.core.Rand;
import zombie.iso.BuildingDef;
import zombie.iso.RoomDef;
import zombie.network.GameClient;
import zombie.network.GameServer;

public final class RDSHouseParty extends RandomizedDeadSurvivorBase {
   final ArrayList items = new ArrayList();

   public RDSHouseParty() {
      this.name = "House Party";
      this.setChance(4);
      this.items.add("Base.Crisps");
      this.items.add("Base.Crisps2");
      this.items.add("Base.Crisps3");
      this.items.add("Base.Pop");
      this.items.add("Base.Pop2");
      this.items.add("Base.Pop3");
      this.items.add("Base.Cupcake");
      this.items.add("Base.Cupcake");
      this.items.add("Base.CakeSlice");
      this.items.add("Base.CakeSlice");
      this.items.add("Base.CakeSlice");
      this.items.add("Base.CakeSlice");
      this.items.add("Base.CakeSlice");
   }

   public boolean isValid(BuildingDef var1, boolean var2) {
      this.debugLine = "";
      if (GameClient.bClient) {
         return false;
      } else if (var1.isAllExplored() && !var2) {
         return false;
      } else {
         if (!var2) {
            for(int var3 = 0; var3 < GameServer.Players.size(); ++var3) {
               IsoPlayer var4 = (IsoPlayer)GameServer.Players.get(var3);
               if (var4.getSquare() != null && var4.getSquare().getBuilding() != null && var4.getSquare().getBuilding().def == var1) {
                  return false;
               }
            }
         }

         if (this.getRoom(var1, "livingroom") != null) {
            return true;
         } else {
            this.debugLine = "No living room";
            return false;
         }
      }
   }

   public void randomizeDeadSurvivor(BuildingDef var1) {
      RoomDef var2 = this.getRoom(var1, "livingroom");
      this.addZombies(var1, Rand.Next(5, 8), "Party", (Integer)null, var2);
      this.addRandomItemsOnGround(var2, this.items, Rand.Next(4, 7));
   }
}

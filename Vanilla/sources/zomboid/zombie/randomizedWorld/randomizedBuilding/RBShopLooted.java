package zombie.randomizedWorld.randomizedBuilding;

import java.util.ArrayList;
import zombie.characters.IsoPlayer;
import zombie.core.Rand;
import zombie.iso.BuildingDef;
import zombie.iso.IsoDirections;
import zombie.iso.IsoGridSquare;
import zombie.iso.RoomDef;
import zombie.network.GameClient;
import zombie.network.GameServer;

public final class RBShopLooted extends RandomizedBuildingBase {
   private final ArrayList buildingList = new ArrayList();

   public void randomizeBuilding(BuildingDef var1) {
      var1.bAlarmed = false;
      var1.setAllExplored(true);
      RoomDef var2 = null;

      int var3;
      for(var3 = 0; var3 < var1.rooms.size(); ++var3) {
         RoomDef var4 = (RoomDef)var1.rooms.get(var3);
         if (this.buildingList.contains(var4.name)) {
            var2 = var4;
            break;
         }
      }

      if (var2 != null) {
         var3 = Rand.Next(3, 8);

         int var7;
         for(var7 = 0; var7 < var3; ++var7) {
            this.addZombiesOnSquare(1, "Bandit", (Integer)null, var2.getFreeSquare());
         }

         this.addZombiesOnSquare(2, "Police", (Integer)null, var2.getFreeSquare());
         var7 = Rand.Next(3, 8);

         for(int var5 = 0; var5 < var7; ++var5) {
            IsoGridSquare var6 = getRandomSquareForCorpse(var2);
            createRandomDeadBody(var6, (IsoDirections)null, Rand.Next(5, 10), 5, (String)null);
         }

      }
   }

   public boolean isValid(BuildingDef var1, boolean var2) {
      this.debugLine = "";
      if (GameClient.bClient) {
         return false;
      } else if (!this.isTimeValid(var2)) {
         return false;
      } else if (var1.isAllExplored() && !var2) {
         return false;
      } else {
         int var3;
         if (!var2) {
            if (Rand.Next(100) > this.getChance()) {
               return false;
            }

            for(var3 = 0; var3 < GameServer.Players.size(); ++var3) {
               IsoPlayer var4 = (IsoPlayer)GameServer.Players.get(var3);
               if (var4.getSquare() != null && var4.getSquare().getBuilding() != null && var4.getSquare().getBuilding().def == var1) {
                  return false;
               }
            }
         }

         for(var3 = 0; var3 < var1.rooms.size(); ++var3) {
            RoomDef var5 = (RoomDef)var1.rooms.get(var3);
            if (this.buildingList.contains(var5.name)) {
               return true;
            }
         }

         this.debugLine = this.debugLine + "not a shop";
         return false;
      }
   }

   public RBShopLooted() {
      this.name = "Looted Shop";
      this.setChance(2);
      this.setAlwaysDo(true);
      this.setMaximumDays(30);
      this.buildingList.add("conveniencestore");
      this.buildingList.add("warehouse");
      this.buildingList.add("medclinic");
      this.buildingList.add("grocery");
      this.buildingList.add("zippeestore");
      this.buildingList.add("gigamart");
      this.buildingList.add("fossoil");
      this.buildingList.add("spiffo_dining");
      this.buildingList.add("pizzawhirled");
      this.buildingList.add("bookstore");
      this.buildingList.add("grocers");
      this.buildingList.add("library");
      this.buildingList.add("toolstore");
      this.buildingList.add("bar");
      this.buildingList.add("pharmacy");
      this.buildingList.add("gunstore");
      this.buildingList.add("mechanic");
      this.buildingList.add("bakery");
      this.buildingList.add("aesthetic");
      this.buildingList.add("clothesstore");
      this.buildingList.add("restaurant");
      this.buildingList.add("poststorage");
      this.buildingList.add("generalstore");
      this.buildingList.add("furniturestore");
      this.buildingList.add("fishingstorage");
      this.buildingList.add("cornerstore");
      this.buildingList.add("housewarestore");
      this.buildingList.add("shoestore");
      this.buildingList.add("sportstore");
      this.buildingList.add("giftstore");
      this.buildingList.add("candystore");
      this.buildingList.add("toystore");
      this.buildingList.add("electronicsstore");
      this.buildingList.add("sewingstore");
      this.buildingList.add("medical");
      this.buildingList.add("medicaloffice");
      this.buildingList.add("jewelrystore");
      this.buildingList.add("musicstore");
      this.buildingList.add("departmentstore");
      this.buildingList.add("gasstore");
      this.buildingList.add("gardenstore");
      this.buildingList.add("farmstorage");
      this.buildingList.add("hunting");
      this.buildingList.add("camping");
      this.buildingList.add("butcher");
      this.buildingList.add("optometrist");
      this.buildingList.add("knoxbutcher");
   }
}

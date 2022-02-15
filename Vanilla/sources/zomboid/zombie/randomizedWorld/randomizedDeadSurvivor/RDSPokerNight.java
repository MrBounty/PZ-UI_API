package zombie.randomizedWorld.randomizedDeadSurvivor;

import java.util.ArrayList;
import zombie.characters.IsoPlayer;
import zombie.core.Rand;
import zombie.iso.BuildingDef;
import zombie.iso.RoomDef;
import zombie.network.GameClient;
import zombie.network.GameServer;

public final class RDSPokerNight extends RandomizedDeadSurvivorBase {
   private final ArrayList items = new ArrayList();
   private String money = null;
   private String card = null;

   public RDSPokerNight() {
      this.name = "Poker Night";
      this.setChance(4);
      this.setMaximumDays(60);
      this.items.add("Base.Cigarettes");
      this.items.add("Base.WhiskeyFull");
      this.items.add("Base.Wine");
      this.items.add("Base.Wine2");
      this.items.add("Base.Crisps");
      this.items.add("Base.Crisps2");
      this.items.add("Base.Crisps3");
      this.items.add("Base.Pop");
      this.items.add("Base.Pop2");
      this.items.add("Base.Pop3");
      this.money = "Base.Money";
      this.card = "Base.CardDeck";
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

         if (this.getRoom(var1, "kitchen") != null) {
            return true;
         } else {
            this.debugLine = "No kitchen";
            return false;
         }
      }
   }

   public void randomizeDeadSurvivor(BuildingDef var1) {
      RoomDef var2 = this.getRoom(var1, "kitchen");
      this.addZombies(var1, Rand.Next(3, 5), (String)null, 10, var2);
      this.addZombies(var1, 1, "PokerDealer", 0, var2);
      this.addRandomItemsOnGround(var2, this.items, Rand.Next(3, 7));
      this.addRandomItemsOnGround(var2, this.money, Rand.Next(8, 13));
      this.addRandomItemsOnGround(var2, this.card, 1);
   }
}

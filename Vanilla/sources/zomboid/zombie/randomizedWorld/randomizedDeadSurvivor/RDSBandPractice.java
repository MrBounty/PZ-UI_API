package zombie.randomizedWorld.randomizedDeadSurvivor;

import java.util.ArrayList;
import java.util.List;
import zombie.characters.IsoPlayer;
import zombie.core.Rand;
import zombie.iso.BuildingDef;
import zombie.iso.IsoGridSquare;
import zombie.iso.RoomDef;
import zombie.network.GameClient;
import zombie.network.GameServer;
import zombie.util.list.PZArrayUtil;

public final class RDSBandPractice extends RandomizedDeadSurvivorBase {
   private final ArrayList instrumentsList = new ArrayList();

   public RDSBandPractice() {
      this.name = "Band Practice";
      this.setChance(10);
      this.setMaximumDays(60);
      this.instrumentsList.add("GuitarAcoustic");
      this.instrumentsList.add("GuitarElectricBlack");
      this.instrumentsList.add("GuitarElectricBlue");
      this.instrumentsList.add("GuitarElectricRed");
      this.instrumentsList.add("GuitarElectricBassBlue");
      this.instrumentsList.add("GuitarElectricBassBlack");
      this.instrumentsList.add("GuitarElectricBassRed");
   }

   public void randomizeDeadSurvivor(BuildingDef var1) {
      this.spawnItemsInContainers(var1, "BandPractice", 90);
      RoomDef var2 = this.getRoom(var1, "garagestorage");
      if (var2 == null) {
         var2 = this.getRoom(var1, "shed");
      }

      if (var2 == null) {
         var2 = this.getRoom(var1, "garage");
      }

      this.addZombies(var1, Rand.Next(2, 4), "Rocker", 20, var2);
      IsoGridSquare var3 = getRandomSpawnSquare(var2);
      if (var3 != null) {
         var3.AddWorldInventoryItem((String)PZArrayUtil.pickRandom((List)this.instrumentsList), Rand.Next(0.0F, 0.5F), Rand.Next(0.0F, 0.5F), 0.0F);
         if (Rand.Next(4) == 0) {
            var3.AddWorldInventoryItem((String)PZArrayUtil.pickRandom((List)this.instrumentsList), Rand.Next(0.0F, 0.5F), Rand.Next(0.0F, 0.5F), 0.0F);
         }

         if (Rand.Next(4) == 0) {
            var3.AddWorldInventoryItem((String)PZArrayUtil.pickRandom((List)this.instrumentsList), Rand.Next(0.0F, 0.5F), Rand.Next(0.0F, 0.5F), 0.0F);
         }

      }
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

         boolean var6 = false;

         for(int var7 = 0; var7 < var1.rooms.size(); ++var7) {
            RoomDef var5 = (RoomDef)var1.rooms.get(var7);
            if (("garagestorage".equals(var5.name) || "shed".equals(var5.name) || "garage".equals(var5.name)) && var5.area >= 9) {
               var6 = true;
               break;
            }
         }

         if (!var6) {
            this.debugLine = "No shed/garage or is too small";
         }

         return var6;
      }
   }
}

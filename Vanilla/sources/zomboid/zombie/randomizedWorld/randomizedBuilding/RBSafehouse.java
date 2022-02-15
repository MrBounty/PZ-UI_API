package zombie.randomizedWorld.randomizedBuilding;

import zombie.characters.IsoGameCharacter;
import zombie.core.Rand;
import zombie.inventory.InventoryItem;
import zombie.inventory.ItemPickerJava;
import zombie.iso.BuildingDef;
import zombie.iso.IsoCell;
import zombie.iso.IsoGridSquare;
import zombie.iso.IsoObject;
import zombie.iso.IsoWorld;
import zombie.iso.RoomDef;
import zombie.iso.objects.IsoBarricade;
import zombie.iso.objects.IsoDoor;
import zombie.iso.objects.IsoWindow;
import zombie.network.GameServer;

public final class RBSafehouse extends RandomizedBuildingBase {
   public void randomizeBuilding(BuildingDef var1) {
      var1.bAlarmed = false;
      var1.setHasBeenVisited(true);
      ItemPickerJava.ItemPickerRoom var2 = (ItemPickerJava.ItemPickerRoom)ItemPickerJava.rooms.get("SafehouseLoot");
      IsoCell var3 = IsoWorld.instance.CurrentCell;

      for(int var4 = var1.x - 1; var4 < var1.x2 + 1; ++var4) {
         for(int var5 = var1.y - 1; var5 < var1.y2 + 1; ++var5) {
            for(int var6 = 0; var6 < 8; ++var6) {
               IsoGridSquare var7 = var3.getGridSquare(var4, var5, var6);
               if (var7 != null) {
                  for(int var8 = 0; var8 < var7.getObjects().size(); ++var8) {
                     IsoObject var9 = (IsoObject)var7.getObjects().get(var8);
                     IsoGridSquare var10;
                     boolean var11;
                     IsoBarricade var12;
                     int var13;
                     int var14;
                     if (var9 instanceof IsoDoor && ((IsoDoor)var9).isBarricadeAllowed()) {
                        var10 = var7.getRoom() == null ? var7 : ((IsoDoor)var9).getOppositeSquare();
                        if (var10 != null && var10.getRoom() == null) {
                           var11 = var10 != var7;
                           var12 = IsoBarricade.AddBarricadeToObject((IsoDoor)var9, var11);
                           if (var12 != null) {
                              var13 = Rand.Next(1, 4);

                              for(var14 = 0; var14 < var13; ++var14) {
                                 var12.addPlank((IsoGameCharacter)null, (InventoryItem)null);
                              }

                              if (GameServer.bServer) {
                                 var12.transmitCompleteItemToClients();
                              }
                           }
                        }
                     }

                     if (var9 instanceof IsoWindow) {
                        var10 = var7.getRoom() == null ? var7 : ((IsoWindow)var9).getOppositeSquare();
                        if (((IsoWindow)var9).isBarricadeAllowed() && var6 == 0 && var10 != null && var10.getRoom() == null) {
                           var11 = var10 != var7;
                           var12 = IsoBarricade.AddBarricadeToObject((IsoWindow)var9, var11);
                           if (var12 != null) {
                              var13 = Rand.Next(1, 4);

                              for(var14 = 0; var14 < var13; ++var14) {
                                 var12.addPlank((IsoGameCharacter)null, (InventoryItem)null);
                              }

                              if (GameServer.bServer) {
                                 var12.transmitCompleteItemToClients();
                              }
                           }
                        } else {
                           ((IsoWindow)var9).addSheet((IsoGameCharacter)null);
                           ((IsoWindow)var9).HasCurtains().ToggleDoor((IsoGameCharacter)null);
                        }
                     }

                     if (var9.getContainer() != null && var7.getRoom() != null && var7.getRoom().getBuilding().getDef() == var1 && Rand.Next(100) <= 70 && var7.getRoom().getName() != null && var2.Containers.containsKey(var9.getContainer().getType())) {
                        var9.getContainer().clear();
                        ItemPickerJava.fillContainerType(var2, var9.getContainer(), "", (IsoGameCharacter)null);
                        ItemPickerJava.updateOverlaySprite(var9);
                        var9.getContainer().setExplored(true);
                     }
                  }
               }
            }
         }
      }

      var1.setAllExplored(true);
      var1.bAlarmed = false;
      this.addZombies(var1);
   }

   private void addZombies(BuildingDef var1) {
      this.addZombies(var1, 0, (String)null, (Integer)null, (RoomDef)null);
      if (Rand.Next(5) == 0) {
         this.addZombies(var1, 1, "Survivalist", (Integer)null, (RoomDef)null);
      }

      if (Rand.Next(100) <= 60) {
         RandomizedBuildingBase.createRandomDeadBody(this.getLivingRoomOrKitchen(var1), Rand.Next(3, 7));
      }

      if (Rand.Next(100) <= 60) {
         RandomizedBuildingBase.createRandomDeadBody(this.getLivingRoomOrKitchen(var1), Rand.Next(3, 7));
      }

   }

   public RBSafehouse() {
      this.name = "Safehouse";
      this.setChance(10);
   }
}

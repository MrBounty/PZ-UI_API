package zombie.inventory;

import java.util.ArrayList;
import zombie.characters.IsoGameCharacter;
import zombie.debug.DebugLog;
import zombie.inventory.types.Clothing;
import zombie.inventory.types.DrainableComboItem;
import zombie.iso.IsoObject;
import zombie.iso.objects.IsoDeadBody;
import zombie.iso.objects.IsoMannequin;
import zombie.iso.objects.IsoWorldInventoryObject;
import zombie.network.GameClient;
import zombie.scripting.ScriptManager;
import zombie.scripting.objects.Item;
import zombie.util.Type;
import zombie.vehicles.VehiclePart;

public final class ItemUser {
   private static final ArrayList tempItems = new ArrayList();

   public static void UseItem(InventoryItem var0) {
      DrainableComboItem var1 = (DrainableComboItem)Type.tryCastTo(var0, DrainableComboItem.class);
      if (var1 != null) {
         var1.setDelta(var1.getDelta() - var1.getUseDelta());
         InventoryItem var4;
         if (var1.uses > 1) {
            int var2 = var1.uses - 1;
            var1.uses = 1;
            CreateItem(var1.getFullType(), tempItems);
            byte var3 = 0;
            if (var3 < tempItems.size()) {
               var4 = (InventoryItem)tempItems.get(var3);
               var4.setUses(var2);
               AddItem(var1, var4);
            }
         }

         if (var1.getDelta() <= 1.0E-4F) {
            var1.setDelta(0.0F);
            if (var1.getReplaceOnDeplete() == null) {
               UseItem(var1, false, false);
            } else {
               String var5 = var1.getReplaceOnDepleteFullType();
               CreateItem(var5, tempItems);

               for(int var6 = 0; var6 < tempItems.size(); ++var6) {
                  var4 = (InventoryItem)tempItems.get(var6);
                  var4.setFavorite(var1.isFavorite());
                  AddItem(var1, var4);
               }

               RemoveItem(var1);
            }
         }

         var1.updateWeight();
      } else {
         UseItem(var0, false, false);
      }

   }

   public static void UseItem(InventoryItem var0, boolean var1, boolean var2) {
      if (var0.isDisappearOnUse() || var1) {
         --var0.uses;
         if (var0.replaceOnUse != null && !var2 && !var1) {
            String var3 = var0.replaceOnUse;
            if (!var3.contains(".")) {
               var3 = var0.module + "." + var3;
            }

            CreateItem(var3, tempItems);

            for(int var4 = 0; var4 < tempItems.size(); ++var4) {
               InventoryItem var5 = (InventoryItem)tempItems.get(var4);
               var5.setConditionFromModData(var0);
               AddItem(var0, var5);
               var5.setFavorite(var0.isFavorite());
            }
         }

         if (var0.uses <= 0) {
            if (var0.keepOnDeplete) {
               return;
            }

            RemoveItem(var0);
         } else if (GameClient.bClient && !var0.isInPlayerInventory()) {
            GameClient.instance.sendItemStats(var0);
         }

      }
   }

   public static void CreateItem(String var0, ArrayList var1) {
      var1.clear();
      Item var2 = ScriptManager.instance.FindItem(var0);
      if (var2 == null) {
         DebugLog.General.warn("ERROR: ItemUses.CreateItem: can't find " + var0);
      } else {
         int var3 = var2.getCount();

         for(int var4 = 0; var4 < var3; ++var4) {
            InventoryItem var5 = InventoryItemFactory.CreateItem(var0);
            if (var5 == null) {
               return;
            }

            var1.add(var5);
         }

      }
   }

   public static void AddItem(InventoryItem var0, InventoryItem var1) {
      IsoWorldInventoryObject var2 = var0.getWorldItem();
      if (var2 != null && var2.getWorldObjectIndex() == -1) {
         var2 = null;
      }

      if (var2 != null) {
         var2.getSquare().AddWorldInventoryItem(var1, 0.0F, 0.0F, 0.0F, true);
      } else {
         if (var0.container != null) {
            VehiclePart var3 = var0.container.vehiclePart;
            if (!var0.isInPlayerInventory() && GameClient.bClient) {
               var0.container.addItemOnServer(var1);
            }

            var0.container.AddItem(var1);
            if (var3 != null) {
               var3.setContainerContentAmount(var3.getItemContainer().getCapacityWeight());
            }
         }

      }
   }

   public static void RemoveItem(InventoryItem var0) {
      IsoWorldInventoryObject var1 = var0.getWorldItem();
      if (var1 != null && var1.getWorldObjectIndex() == -1) {
         var1 = null;
      }

      if (var1 != null) {
         var1.getSquare().transmitRemoveItemFromSquare(var1);
      } else {
         if (var0.container != null) {
            IsoObject var2 = var0.container.parent;
            VehiclePart var3 = var0.container.vehiclePart;
            if (var2 instanceof IsoGameCharacter) {
               IsoGameCharacter var4 = (IsoGameCharacter)var2;
               if (var0 instanceof Clothing) {
                  ((Clothing)var0).Unwear();
               }

               var4.removeFromHands(var0);
               if (var4.getClothingItem_Back() == var0) {
                  var4.setClothingItem_Back((InventoryItem)null);
               }
            } else if (!var0.isInPlayerInventory() && GameClient.bClient) {
               var0.container.removeItemOnServer(var0);
            }

            var0.container.Items.remove(var0);
            var0.container.setDirty(true);
            var0.container.setDrawDirty(true);
            var0.container = null;
            if (var2 instanceof IsoDeadBody) {
               ((IsoDeadBody)var2).checkClothing(var0);
            }

            if (var2 instanceof IsoMannequin) {
               ((IsoMannequin)var2).checkClothing(var0);
            }

            if (var3 != null) {
               var3.setContainerContentAmount(var3.getItemContainer().getCapacityWeight());
            }
         }

      }
   }
}

package zombie.inventory;

import zombie.core.Core;
import zombie.core.Rand;
import zombie.debug.DebugLog;
import zombie.inventory.types.Drainable;
import zombie.inventory.types.Food;
import zombie.inventory.types.Moveable;
import zombie.inventory.types.Radio;
import zombie.network.GameClient;
import zombie.scripting.ScriptManager;
import zombie.scripting.objects.Item;
import zombie.util.Type;
import zombie.world.ItemInfo;
import zombie.world.WorldDictionary;

public final class InventoryItemFactory {
   public static InventoryItem CreateItem(String var0) {
      return CreateItem(var0, 1.0F);
   }

   public static InventoryItem CreateItem(String var0, Food var1) {
      InventoryItem var2 = CreateItem(var0, 1.0F);
      Food var3 = (Food)Type.tryCastTo(var2, Food.class);
      if (var3 == null) {
         return null;
      } else {
         var3.setBaseHunger(var1.getBaseHunger());
         var3.setHungChange(var1.getHungChange());
         var3.setBoredomChange(var1.getBoredomChange());
         var3.setUnhappyChange(var1.getUnhappyChange());
         var3.setCarbohydrates(var1.getCarbohydrates());
         var3.setLipids(var1.getLipids());
         var3.setProteins(var1.getProteins());
         var3.setCalories(var1.getCalories());
         return var2;
      }
   }

   public static InventoryItem CreateItem(String var0, float var1) {
      InventoryItem var2 = null;
      Item var3 = null;
      boolean var4 = false;
      String var5 = null;

      try {
         if (var0.startsWith("Moveables.") && !var0.equalsIgnoreCase("Moveables.Moveable")) {
            String[] var6 = var0.split("\\.");
            var5 = var6[1];
            var4 = true;
            var0 = "Moveables.Moveable";
         }

         var3 = ScriptManager.instance.FindItem(var0);
      } catch (Exception var7) {
         DebugLog.log("couldn't find item " + var0);
      }

      if (var3 == null) {
         return null;
      } else {
         var2 = var3.InstanceItem((String)null);
         if (GameClient.bClient && (Core.getInstance().getPoisonousBerry() == null || Core.getInstance().getPoisonousBerry().isEmpty())) {
            Core.getInstance().setPoisonousBerry(GameClient.poisonousBerry);
         }

         if (GameClient.bClient && (Core.getInstance().getPoisonousMushroom() == null || Core.getInstance().getPoisonousMushroom().isEmpty())) {
            Core.getInstance().setPoisonousMushroom(GameClient.poisonousMushroom);
         }

         if (var0.equals(Core.getInstance().getPoisonousBerry())) {
            ((Food)var2).Poison = true;
            ((Food)var2).setPoisonLevelForRecipe(1);
            ((Food)var2).setPoisonDetectionLevel(1);
            ((Food)var2).setPoisonPower(5);
            ((Food)var2).setUseForPoison((new Float(Math.abs(((Food)var2).getHungChange()) * 100.0F)).intValue());
         }

         if (var0.equals(Core.getInstance().getPoisonousMushroom())) {
            ((Food)var2).Poison = true;
            ((Food)var2).setPoisonLevelForRecipe(2);
            ((Food)var2).setPoisonDetectionLevel(2);
            ((Food)var2).setPoisonPower(10);
            ((Food)var2).setUseForPoison((new Float(Math.abs(((Food)var2).getHungChange()) * 100.0F)).intValue());
         }

         var2.id = Rand.Next(2146250223) + 1233423;
         if (var2 instanceof Drainable) {
            ((Drainable)var2).setUsedDelta(var1);
         }

         if (var4) {
            var2.type = var5;
            var2.fullType = var2.module + "." + var5;
            if (var2 instanceof Moveable && !((Moveable)var2).ReadFromWorldSprite(var5) && var2 instanceof Radio) {
               DebugLog.log("InventoryItemFactory -> Radio item = " + (var0 != null ? var0 : "unknown"));
            }
         }

         return var2;
      }
   }

   public static InventoryItem CreateItem(String var0, float var1, String var2) {
      InventoryItem var3 = null;
      Item var4 = ScriptManager.instance.getItem(var0);
      if (var4 == null) {
         DebugLog.log(var0 + " item not found.");
         return null;
      } else {
         var3 = var4.InstanceItem(var2);
         if (var3 == null) {
         }

         if (var3 instanceof Drainable) {
            ((Drainable)var3).setUsedDelta(var1);
         }

         return var3;
      }
   }

   public static InventoryItem CreateItem(String var0, String var1, String var2, String var3) {
      InventoryItem var4 = new InventoryItem(var0, var1, var2, var3);
      var4.id = Rand.Next(2146250223) + 1233423;
      return var4;
   }

   public static InventoryItem CreateItem(short var0) {
      ItemInfo var1 = WorldDictionary.getItemInfoFromID(var0);
      if (var1 != null && var1.isValid()) {
         String var2 = var1.getFullType();
         if (var2 != null) {
            InventoryItem var3 = CreateItem(var2);
            if (var3 != null) {
               return var3;
            }

            DebugLog.log("InventoryItemFactory.CreateItem() unknown item type \"" + (var2 != null ? var2 : "unknown") + "\", registry id = \"" + var0 + "\". Make sure all mods used in save are installed.");
         } else {
            DebugLog.log("InventoryItemFactory.CreateItem() unknown item with registry ID \"" + var0 + "\". Make sure all mods used in save are installed.");
         }
      } else if (var1 == null) {
         DebugLog.log("InventoryItemFactory.CreateItem() unknown item with registry ID \"" + var0 + "\". Make sure all mods used in save are installed.");
      } else {
         DebugLog.log("InventoryItemFactory.CreateItem() cannot create item: " + var1.ToString());
      }

      return null;
   }
}

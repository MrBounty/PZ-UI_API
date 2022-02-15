package zombie.inventory;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.Stack;
import zombie.Lua.LuaEventManager;
import zombie.Lua.LuaManager;
import zombie.characters.IsoGameCharacter;
import zombie.characters.skills.PerkFactory;
import zombie.debug.DebugLog;
import zombie.inventory.types.Clothing;
import zombie.inventory.types.Drainable;
import zombie.inventory.types.DrainableComboItem;
import zombie.inventory.types.Food;
import zombie.inventory.types.Moveable;
import zombie.iso.IsoGridSquare;
import zombie.iso.IsoObject;
import zombie.network.GameClient;
import zombie.scripting.ScriptManager;
import zombie.scripting.objects.EvolvedRecipe;
import zombie.scripting.objects.Item;
import zombie.scripting.objects.MovableRecipe;
import zombie.scripting.objects.Recipe;
import zombie.scripting.objects.ScriptModule;
import zombie.util.StringUtils;

public final class RecipeManager {
   private static final ArrayList RecipeList = new ArrayList();

   public static void Loaded() {
      ArrayList var0 = ScriptManager.instance.getAllRecipes();
      HashSet var1 = new HashSet();

      for(int var2 = 0; var2 < var0.size(); ++var2) {
         Recipe var3 = (Recipe)var0.get(var2);

         for(int var4 = 0; var4 < var3.getSource().size(); ++var4) {
            Recipe.Source var5 = (Recipe.Source)var3.getSource().get(var4);

            for(int var6 = 0; var6 < var5.getItems().size(); ++var6) {
               String var7 = (String)var5.getItems().get(var6);
               if (!"Water".equals(var7) && !var7.contains(".") && !var7.startsWith("[")) {
                  Item var8 = resolveItemModuleDotType(var3, var7, var1, "recipe source");
                  if (var8 == null) {
                     var5.getItems().set(var6, "???." + var7);
                  } else {
                     var5.getItems().set(var6, var8.getFullName());
                  }
               }
            }
         }

         if (var3.getResult() != null && var3.getResult().getModule() == null) {
            Item var9 = resolveItemModuleDotType(var3, var3.getResult().getType(), var1, "recipe result");
            if (var9 == null) {
               var3.getResult().module = "???";
            } else {
               var3.getResult().module = var9.getModule().getName();
            }
         }
      }

   }

   private static Item resolveItemModuleDotType(Recipe var0, String var1, Set var2, String var3) {
      ScriptModule var4 = var0.getModule();
      Item var5 = var4.getItem(var1);
      if (var5 != null && !var5.getObsolete()) {
         return var5;
      } else {
         for(int var6 = 0; var6 < ScriptManager.instance.ModuleList.size(); ++var6) {
            ScriptModule var7 = (ScriptModule)ScriptManager.instance.ModuleList.get(var6);
            var5 = var7.getItem(var1);
            if (var5 != null && !var5.getObsolete()) {
               String var8 = var0.getModule().getName();
               if (!var2.contains(var8)) {
                  var2.add(var8);
                  DebugLog.Recipe.warn("WARNING: module \"%s\" may have forgot to import module Base", var8);
               }

               return var5;
            }
         }

         DebugLog.Recipe.warn("ERROR: can't find %s \"%s\" in recipe \"%s\"", var3, var1, var0.getOriginalname());
         return null;
      }
   }

   public static void LoadedAfterLua() {
      ArrayList var0 = new ArrayList();
      ArrayList var1 = ScriptManager.instance.getAllRecipes();

      for(int var2 = 0; var2 < var1.size(); ++var2) {
         Recipe var3 = (Recipe)var1.get(var2);
         LoadedAfterLua(var3, var0);
      }

      var0.clear();
   }

   private static void LoadedAfterLua(Recipe var0, ArrayList var1) {
      LoadedAfterLua(var0, var0.LuaCreate, "LuaCreate");
      LoadedAfterLua(var0, var0.LuaGiveXP, "LuaGiveXP");
      LoadedAfterLua(var0, var0.LuaTest, "LuaTest");

      for(int var2 = 0; var2 < var0.getSource().size(); ++var2) {
         Recipe.Source var3 = (Recipe.Source)var0.getSource().get(var2);
         LoadedAfterLua(var3, var1);
      }

   }

   private static void LoadedAfterLua(Recipe var0, String var1, String var2) {
      if (!StringUtils.isNullOrWhitespace(var1)) {
         Object var3 = LuaManager.getFunctionObject(var1);
         if (var3 == null) {
            DebugLog.General.error("no such function %s = \"%s\" in recipe \"%s\"", var2, var1, var0.name);
         }

      }
   }

   private static void LoadedAfterLua(Recipe.Source var0, ArrayList var1) {
      for(int var2 = var0.getItems().size() - 1; var2 >= 0; --var2) {
         String var3 = (String)var0.getItems().get(var2);
         if (var3.startsWith("[")) {
            var0.getItems().remove(var2);
            String var4 = var3.substring(1, var3.indexOf("]"));
            Object var5 = LuaManager.getFunctionObject(var4);
            if (var5 != null) {
               var1.clear();
               LuaManager.caller.protectedCallVoid(LuaManager.thread, var5, (Object)var1);

               for(int var6 = 0; var6 < var1.size(); ++var6) {
                  Item var7 = (Item)var1.get(var6);
                  var0.getItems().add(var2 + var6, var7.getFullName());
               }
            }
         }
      }

   }

   public static boolean DoesWipeUseDelta(String var0, String var1) {
      return true;
   }

   public static int getKnownRecipesNumber(IsoGameCharacter var0) {
      int var1 = 0;
      ArrayList var2 = ScriptManager.instance.getAllRecipes();

      for(int var3 = 0; var3 < var2.size(); ++var3) {
         Recipe var4 = (Recipe)var2.get(var3);
         if (var0.isRecipeKnown(var4)) {
            ++var1;
         }
      }

      return var1;
   }

   public static boolean DoesUseItemUp(String var0, Recipe var1) {
      assert "Water".equals(var0) || var0.contains(".");

      for(int var2 = 0; var2 < var1.Source.size(); ++var2) {
         if (((Recipe.Source)var1.Source.get(var2)).keep) {
            ArrayList var3 = ((Recipe.Source)var1.Source.get(var2)).getItems();

            for(int var4 = 0; var4 < var3.size(); ++var4) {
               if (var0.equals(var3.get(var4))) {
                  return false;
               }
            }
         }
      }

      return true;
   }

   public static boolean IsItemDestroyed(String var0, Recipe var1) {
      assert "Water".equals(var0) || var0.contains(".");

      for(int var2 = 0; var2 < var1.Source.size(); ++var2) {
         Recipe.Source var3 = (Recipe.Source)var1.getSource().get(var2);
         if (var3.destroy) {
            for(int var4 = 0; var4 < var3.getItems().size(); ++var4) {
               if (var0.equals(var3.getItems().get(var4))) {
                  return true;
               }
            }
         }
      }

      return false;
   }

   public static float UseAmount(String var0, Recipe var1, IsoGameCharacter var2) {
      Recipe.Source var3 = var1.findSource(var0);
      return var3.getCount();
   }

   public static ArrayList getUniqueRecipeItems(InventoryItem var0, IsoGameCharacter var1, ArrayList var2) {
      RecipeList.clear();
      ArrayList var3 = ScriptManager.instance.getAllRecipes();

      for(int var4 = 0; var4 < var3.size(); ++var4) {
         Recipe var5 = (Recipe)var3.get(var4);
         if (IsRecipeValid(var5, var1, var0, var2) && (!(var0 instanceof Clothing) || var0.getCondition() > 0 || !var5.getOriginalname().equalsIgnoreCase("rip clothing"))) {
            RecipeList.add(var5);
         }
      }

      if (var0 instanceof Moveable && RecipeList.size() == 0 && ((Moveable)var0).getWorldSprite() != null) {
         if (var0.type != null && var0.type.equalsIgnoreCase(((Moveable)var0).getWorldSprite())) {
            MovableRecipe var6 = new MovableRecipe();
            LuaEventManager.triggerEvent("OnDynamicMovableRecipe", ((Moveable)var0).getWorldSprite(), var6, var0, var1);
            if (var6.isValid() && IsRecipeValid(var6, var1, var0, var2)) {
               RecipeList.add(var6);
            }
         } else {
            DebugLog.log("RecipeManager -> Cannot create recipe for this movable item: " + var0.getFullType());
         }
      }

      return RecipeList;
   }

   public static boolean IsRecipeValid(Recipe var0, IsoGameCharacter var1, InventoryItem var2, ArrayList var3) {
      if (var0.Result == null) {
         return false;
      } else if (!var1.isRecipeKnown(var0)) {
         return false;
      } else if (var2 != null && !RecipeContainsItem(var0, var2)) {
         return false;
      } else if (!HasAllRequiredItems(var0, var1, var2, var3)) {
         return false;
      } else if (!HasRequiredSkill(var0, var1)) {
         return false;
      } else if (!isNearItem(var0, var1)) {
         return false;
      } else if (!hasHeat(var0, var2, var3, var1)) {
         return false;
      } else {
         return CanPerform(var0, var1, var2);
      }
   }

   private static boolean isNearItem(Recipe var0, IsoGameCharacter var1) {
      if (var0.getNearItem() != null && !var0.getNearItem().equals("")) {
         for(int var2 = var1.getSquare().getX() - 2; var2 < var1.getSquare().getX() + 2; ++var2) {
            for(int var3 = var1.getSquare().getY() - 2; var3 < var1.getSquare().getY() + 2; ++var3) {
               IsoGridSquare var4 = var1.getCell().getGridSquare(var2, var3, 0);
               if (var4 != null) {
                  for(int var5 = 0; var5 < var4.getObjects().size(); ++var5) {
                     if (var0.getNearItem().equals(((IsoObject)var4.getObjects().get(var5)).getName())) {
                        return true;
                     }
                  }
               }
            }
         }

         return false;
      } else {
         return true;
      }
   }

   private static boolean CanPerform(Recipe var0, IsoGameCharacter var1, InventoryItem var2) {
      if (StringUtils.isNullOrWhitespace(var0.getCanPerform())) {
         return true;
      } else {
         Object var3 = LuaManager.getFunctionObject(var0.getCanPerform());
         if (var3 == null) {
            return false;
         } else {
            Boolean var4 = LuaManager.caller.protectedCallBoolean(LuaManager.thread, var3, var0, var1, var2);
            return var4 == Boolean.TRUE;
         }
      }
   }

   private static boolean HasRequiredSkill(Recipe var0, IsoGameCharacter var1) {
      if (var0.getRequiredSkillCount() == 0) {
         return true;
      } else {
         for(int var2 = 0; var2 < var0.getRequiredSkillCount(); ++var2) {
            Recipe.RequiredSkill var3 = var0.getRequiredSkill(var2);
            if (var1.getPerkLevel(var3.getPerk()) < var3.getLevel()) {
               return false;
            }
         }

         return true;
      }
   }

   private static boolean RecipeContainsItem(Recipe var0, InventoryItem var1) {
      for(int var2 = 0; var2 < var0.Source.size(); ++var2) {
         Recipe.Source var3 = (Recipe.Source)var0.getSource().get(var2);

         for(int var4 = 0; var4 < var3.getItems().size(); ++var4) {
            String var5 = (String)var3.getItems().get(var4);
            if ("Water".equals(var5) && var1.isWaterSource()) {
               return true;
            }

            if (var5.equals(var1.getFullType())) {
               return true;
            }
         }
      }

      return false;
   }

   public static boolean HasAllRequiredItems(Recipe var0, IsoGameCharacter var1, InventoryItem var2, ArrayList var3) {
      ArrayList var4 = getAvailableItemsNeeded(var0, var1, var3, var2, (ArrayList)null);
      return !var4.isEmpty();
   }

   public static boolean hasHeat(Recipe var0, InventoryItem var1, ArrayList var2, IsoGameCharacter var3) {
      if (var0.getHeat() == 0.0F) {
         return true;
      } else {
         InventoryItem var4 = null;
         Iterator var5 = getAvailableItemsNeeded(var0, var3, var2, var1, (ArrayList)null).iterator();

         while(var5.hasNext()) {
            InventoryItem var6 = (InventoryItem)var5.next();
            if (var6 instanceof DrainableComboItem) {
               var4 = var6;
               break;
            }
         }

         if (var4 != null) {
            var5 = var2.iterator();

            while(var5.hasNext()) {
               ItemContainer var9 = (ItemContainer)var5.next();
               Iterator var7 = var9.getItems().iterator();

               while(var7.hasNext()) {
                  InventoryItem var8 = (InventoryItem)var7.next();
                  if (var8.getName().equals(var4.getName())) {
                     if (var0.getHeat() < 0.0F) {
                        if (var8.getInvHeat() <= var0.getHeat()) {
                           return true;
                        }
                     } else if (var0.getHeat() > 0.0F && var8.getInvHeat() + 1.0F >= var0.getHeat()) {
                        return true;
                     }
                  }
               }
            }
         }

         return false;
      }
   }

   public static ArrayList getAvailableItemsAll(Recipe var0, IsoGameCharacter var1, ArrayList var2, InventoryItem var3, ArrayList var4) {
      return getAvailableItems(var0, var1, var2, var3, var4, true).allItems;
   }

   public static ArrayList getAvailableItemsNeeded(Recipe var0, IsoGameCharacter var1, ArrayList var2, InventoryItem var3, ArrayList var4) {
      return getAvailableItems(var0, var1, var2, var3, var4, false).allItems;
   }

   private static RecipeManager.SourceItems getAvailableItems(Recipe var0, IsoGameCharacter var1, ArrayList var2, InventoryItem var3, ArrayList var4, boolean var5) {
      if (var3 != null && (var3.getContainer() == null || !var3.getContainer().contains(var3))) {
         DebugLog.Recipe.warn("recipe: item appears to have been used already, ignoring " + var3.getFullType());
         var3 = null;
      }

      RecipeManager.SourceItems var6 = new RecipeManager.SourceItems(var0, var1, var3, var4);
      if (var2 == null) {
         var2 = new ArrayList();
         var2.add(var1.getInventory());
      }

      if (var3 != null && !RecipeContainsItem(var0, var3)) {
         String var10002 = var3.getFullType();
         throw new RuntimeException("item " + var10002 + " isn't used in recipe " + var0.getOriginalname());
      } else {
         RecipeManager.RMRecipe var7 = RecipeManager.RMRecipe.alloc(var0);
         var7.getItemsFromContainers(var1, var2, var3);
         if (var5 || var7.hasItems()) {
            var7.getAvailableItems(var6, var5);
         }

         RecipeManager.RMRecipe.release(var7);
         return var6;
      }
   }

   public static ArrayList getSourceItemsAll(Recipe var0, int var1, IsoGameCharacter var2, ArrayList var3, InventoryItem var4, ArrayList var5) {
      if (var1 >= 0 && var1 < var0.getSource().size()) {
         RecipeManager.SourceItems var6 = getAvailableItems(var0, var2, var3, var4, var5, true);
         return var6.itemsPerSource[var1];
      } else {
         return null;
      }
   }

   public static ArrayList getSourceItemsNeeded(Recipe var0, int var1, IsoGameCharacter var2, ArrayList var3, InventoryItem var4, ArrayList var5) {
      if (var1 >= 0 && var1 < var0.getSource().size()) {
         RecipeManager.SourceItems var6 = getAvailableItems(var0, var2, var3, var4, var5, false);
         return var6.itemsPerSource[var1];
      } else {
         return null;
      }
   }

   public static int getNumberOfTimesRecipeCanBeDone(Recipe var0, IsoGameCharacter var1, ArrayList var2, InventoryItem var3) {
      int var4 = 0;
      RecipeManager.RMRecipe var5 = RecipeManager.RMRecipe.alloc(var0);
      if (var2 == null) {
         var2 = new ArrayList();
         var2.add(var1.getInventory());
      }

      var5.getItemsFromContainers(var1, var2, var3);
      ArrayList var6 = new ArrayList();

      for(ArrayList var7 = new ArrayList(); var5.hasItems(); ++var4) {
         var7.clear();
         var5.Use(var7);
         if (var6.containsAll(var7)) {
            var4 = -1;
            break;
         }

         var6.addAll(var7);

         for(int var8 = 0; var8 < var7.size(); ++var8) {
            InventoryItem var9 = (InventoryItem)var7.get(var8);
            if (var9 instanceof Food && ((Food)var9).isFrozen()) {
               --var4;
               break;
            }
         }
      }

      RecipeManager.RMRecipe.release(var5);
      return var4;
   }

   public static InventoryItem GetMovableRecipeTool(boolean var0, Recipe var1, InventoryItem var2, IsoGameCharacter var3, ArrayList var4) {
      if (!(var1 instanceof MovableRecipe)) {
         return null;
      } else {
         MovableRecipe var5 = (MovableRecipe)var1;
         Recipe.Source var6 = var0 ? var5.getPrimaryTools() : var5.getSecondaryTools();
         if (var6 != null && var6.getItems() != null && var6.getItems().size() != 0) {
            RecipeManager.SourceItems var7 = getAvailableItems(var1, var3, var4, var2, (ArrayList)null, false);
            if (var7.allItems != null && var7.allItems.size() != 0) {
               for(int var8 = 0; var8 < var7.allItems.size(); ++var8) {
                  InventoryItem var9 = (InventoryItem)var7.allItems.get(var8);

                  for(int var10 = 0; var10 < var6.getItems().size(); ++var10) {
                     if (var9.getFullType().equalsIgnoreCase((String)var6.getItems().get(var10))) {
                        return var9;
                     }
                  }
               }

               return null;
            } else {
               return null;
            }
         } else {
            return null;
         }
      }
   }

   public static InventoryItem PerformMakeItem(Recipe var0, InventoryItem var1, IsoGameCharacter var2, ArrayList var3) {
      boolean var4 = var2.getPrimaryHandItem() == var1;
      boolean var5 = var2.getSecondaryHandItem() == var1;
      RecipeManager.SourceItems var6 = getAvailableItems(var0, var2, var3, var1, (ArrayList)null, false);
      ArrayList var7 = var6.allItems;
      if (var7.isEmpty()) {
         throw new RuntimeException("getAvailableItems() didn't return the required number of items");
      } else {
         var2.removeFromHands(var1);
         Recipe.Result var8 = var0.getResult();
         InventoryItem var9 = InventoryItemFactory.CreateItem(var8.getFullType());
         boolean var10 = false;
         boolean var11 = false;
         int var12 = -1;
         int var13 = 0;
         boolean var14 = false;
         boolean var15 = false;
         float var16 = 0.0F;
         float var17 = 0.0F;
         int var18 = 0;
         int var19 = 0;

         int var20;
         label182:
         for(var20 = 0; var20 < var0.getSource().size(); ++var20) {
            Recipe.Source var21 = (Recipe.Source)var0.getSource().get(var20);
            if (!var21.isKeep()) {
               ArrayList var22 = var6.itemsPerSource[var20];
               int var24;
               int var25;
               int var26;
               InventoryItem var31;
               switch(var6.typePerSource[var20]) {
               case DRAINABLE:
                  int var23 = (int)var21.getCount();
                  var24 = 0;

                  for(; var24 < var22.size(); ++var24) {
                     InventoryItem var33 = (InventoryItem)var22.get(var24);
                     var26 = AvailableUses(var33);
                     if (var26 >= var23) {
                        ReduceUses(var33, (float)var23, var2);
                        var23 = 0;
                     } else {
                        ReduceUses(var33, (float)var26, var2);
                        var23 -= var26;
                     }
                  }

                  if (var23 > 0) {
                     throw new RuntimeException("required amount of " + var21.getItems() + " wasn't available");
                  }
                  break;
               case FOOD:
                  var24 = (int)var21.use;
                  var25 = 0;

                  while(true) {
                     if (var25 >= var22.size()) {
                        continue label182;
                     }

                     var31 = (InventoryItem)var22.get(var25);
                     int var32 = AvailableUses(var31);
                     if (var32 >= var24) {
                        ReduceUses(var31, (float)var24, var2);
                        var24 = 0;
                     } else {
                        ReduceUses(var31, (float)var32, var2);
                        var24 -= var32;
                     }

                     ++var25;
                  }
               case DESTROY:
                  var25 = 0;

                  while(true) {
                     if (var25 >= var22.size()) {
                        continue label182;
                     }

                     var31 = (InventoryItem)var22.get(var25);
                     ItemUser.RemoveItem(var31);
                     ++var25;
                  }
               case OTHER:
                  var25 = 0;

                  while(true) {
                     if (var25 >= var22.size()) {
                        continue label182;
                     }

                     var31 = (InventoryItem)var22.get(var25);
                     ItemUser.UseItem(var31, true, false);
                     ++var25;
                  }
               case WATER:
                  var25 = var0.getWaterAmountNeeded();

                  for(var26 = 0; var26 < var22.size(); ++var26) {
                     InventoryItem var27 = (InventoryItem)var22.get(var26);
                     int var28 = AvailableUses(var27);
                     if (var28 >= var25) {
                        ReduceUses(var27, (float)var25, var2);
                        var25 = 0;
                     } else {
                        ReduceUses(var27, (float)var28, var2);
                        var25 -= var28;
                     }
                  }

                  if (var25 > 0) {
                     throw new RuntimeException("required amount of water wasn't available");
                  }
               }
            }
         }

         InventoryItem var29;
         for(var20 = 0; var20 < var7.size(); ++var20) {
            var29 = (InventoryItem)var7.get(var20);
            if (var29 instanceof Food) {
               if (((Food)var29).isCooked()) {
                  var10 = true;
               }

               if (((Food)var29).isBurnt()) {
                  var11 = true;
               }

               var12 = ((Food)var29).getPoisonDetectionLevel();
               var13 = ((Food)var29).getPoisonPower();
               ++var19;
               if (var29.getAge() > (float)var29.getOffAgeMax()) {
                  var14 = true;
               } else if (!var14 && var29.getOffAgeMax() < 1000000000) {
                  if (var29.getAge() < (float)var29.getOffAge()) {
                     var17 += 0.5F * var29.getAge() / (float)var29.getOffAge();
                  } else {
                     var15 = true;
                     var17 += 0.5F + 0.5F * (var29.getAge() - (float)var29.getOffAge()) / (float)(var29.getOffAgeMax() - var29.getOffAge());
                  }
               }
            }

            if (var9 instanceof Food && var29.isTaintedWater()) {
               var9.setTaintedWater(true);
            }

            if (var9.getScriptItem() == var29.getScriptItem() && var29.isFavorite()) {
               var9.setFavorite(true);
            }

            var16 += (float)var29.getCondition() / (float)var29.getConditionMax();
            ++var18;
         }

         var17 /= (float)var19;
         if (var9 instanceof Food && ((Food)var9).IsCookable) {
            ((Food)var9).setCooked(var10);
            ((Food)var9).setBurnt(var11);
            ((Food)var9).setPoisonDetectionLevel(var12);
            ((Food)var9).setPoisonPower(var13);
         }

         if ((double)var9.getOffAgeMax() != 1.0E9D) {
            if (var14) {
               var9.setAge((float)var9.getOffAgeMax());
            } else {
               if (var15 && var17 < 0.5F) {
                  var17 = 0.5F;
               }

               if (var17 < 0.5F) {
                  var9.setAge(2.0F * var17 * (float)var9.getOffAge());
               } else {
                  var9.setAge((float)var9.getOffAge() + 2.0F * (var17 - 0.5F) * (float)(var9.getOffAgeMax() - var9.getOffAge()));
               }
            }
         }

         var9.setCondition(Math.round((float)var9.getConditionMax() * (var16 / (float)var18)));

         for(var20 = 0; var20 < var7.size(); ++var20) {
            var29 = (InventoryItem)var7.get(var20);
            var9.setConditionFromModData(var29);
         }

         GivePlayerExperience(var0, var7, var9, var2);
         if (var0.LuaCreate != null) {
            Object var30 = LuaManager.getFunctionObject(var0.LuaCreate);
            if (var30 != null) {
               LuaManager.caller.protectedCall(LuaManager.thread, var30, var7, var9, var2, var1, var4, var5);
            }
         }

         if (!var0.isRemoveResultItem()) {
            return var9;
         } else {
            return null;
         }
      }
   }

   private static boolean ReduceUses(InventoryItem var0, float var1, IsoGameCharacter var2) {
      float var4;
      if (var0 instanceof DrainableComboItem) {
         DrainableComboItem var3 = (DrainableComboItem)var0;
         var4 = var3.getUseDelta() * var1;
         var3.setUsedDelta(var3.getUsedDelta() - var4);
         if (AvailableUses(var0) < 1) {
            var3.setUsedDelta(0.0F);
            ItemUser.UseItem(var3);
            return true;
         }

         if (GameClient.bClient && !var0.isInPlayerInventory()) {
            GameClient.instance.sendItemStats(var0);
         }
      }

      if (var0 instanceof Food) {
         Food var6 = (Food)var0;
         if (var6.getHungerChange() < 0.0F) {
            var4 = Math.min(-var6.getHungerChange() * 100.0F, var1);
            float var5 = var4 / (-var6.getHungerChange() * 100.0F);
            if (var5 < 0.0F) {
               var5 = 0.0F;
            }

            if (var5 > 1.0F) {
               var5 = 1.0F;
            }

            var6.setHungChange(var6.getHungChange() - var6.getHungChange() * var5);
            var6.setCalories(var6.getCalories() - var6.getCalories() * var5);
            var6.setCarbohydrates(var6.getCarbohydrates() - var6.getCarbohydrates() * var5);
            var6.setLipids(var6.getLipids() - var6.getLipids() * var5);
            var6.setProteins(var6.getProteins() - var6.getProteins() * var5);
            var6.setThirstChange(var6.getThirstChange() - var6.getThirstChange() * var5);
            var6.setFluReduction(var6.getFluReduction() - (int)((float)var6.getFluReduction() * var5));
            var6.setPainReduction(var6.getPainReduction() - var6.getPainReduction() * var5);
            var6.setEndChange(var6.getEnduranceChange() - var6.getEnduranceChange() * var5);
            var6.setReduceFoodSickness(var6.getReduceFoodSickness() - (int)((float)var6.getReduceFoodSickness() * var5));
            var6.setStressChange(var6.getStressChange() - var6.getStressChange() * var5);
            var6.setFatigueChange(var6.getFatigueChange() - var6.getFatigueChange() * var5);
            if ((double)var6.getHungerChange() > -0.01D) {
               ItemUser.UseItem(var6);
               return true;
            }

            if (GameClient.bClient && !var0.isInPlayerInventory()) {
               GameClient.instance.sendItemStats(var0);
            }
         }
      }

      return false;
   }

   private static int AvailableUses(InventoryItem var0) {
      if (var0 instanceof DrainableComboItem) {
         DrainableComboItem var2 = (DrainableComboItem)var0;
         return var2.getDrainableUsesInt();
      } else if (var0 instanceof Food) {
         Food var1 = (Food)var0;
         return (int)(-var1.getHungerChange() * 100.0F);
      } else {
         return 0;
      }
   }

   private static void GivePlayerExperience(Recipe var0, ArrayList var1, InventoryItem var2, IsoGameCharacter var3) {
      String var4 = var0.LuaGiveXP;
      if (var4 == null) {
         var4 = "Recipe.OnGiveXP.Default";
      }

      Object var5 = LuaManager.getFunctionObject(var4);
      if (var5 == null) {
         DebugLog.Recipe.warn("ERROR: Lua method \"" + var4 + "\" not found (in RecipeManager.GivePlayerExperience())");
      } else {
         LuaManager.caller.protectedCall(LuaManager.thread, var5, var0, var1, var2, var3);
      }
   }

   public static ArrayList getAllEvolvedRecipes() {
      Stack var0 = ScriptManager.instance.getAllEvolvedRecipes();
      ArrayList var1 = new ArrayList();

      for(int var2 = 0; var2 < var0.size(); ++var2) {
         var1.add((EvolvedRecipe)var0.get(var2));
      }

      return var1;
   }

   public static ArrayList getEvolvedRecipe(InventoryItem var0, IsoGameCharacter var1, ArrayList var2, boolean var3) {
      ArrayList var4 = new ArrayList();
      if (var0 instanceof Food && ((Food)var0).isRotten() && var1.getPerkLevel(PerkFactory.Perks.Cooking) < 7) {
         return var4;
      } else if (var0 instanceof Food && ((Food)var0).isFrozen()) {
         return var4;
      } else {
         Stack var5 = ScriptManager.instance.getAllEvolvedRecipes();

         for(int var6 = 0; var6 < var5.size(); ++var6) {
            EvolvedRecipe var7 = (EvolvedRecipe)var5.get(var6);
            if ((var0.isCooked() && var7.addIngredientIfCooked || !var0.isCooked()) && (var0.getType().equals(var7.baseItem) || var0.getType().equals(var7.getResultItem())) && (!var0.getType().equals("WaterPot") || !((double)((Drainable)var0).getUsedDelta() < 0.75D))) {
               if (var3) {
                  ArrayList var8 = var7.getItemsCanBeUse(var1, var0, var2);
                  if (!var8.isEmpty()) {
                     var4.add(var7);
                  }
               } else {
                  var4.add(var7);
               }
            }
         }

         return var4;
      }
   }

   private static void DebugPrintAllRecipes() {
      ArrayList var0 = ScriptManager.instance.getAllRecipes();

      for(int var1 = 0; var1 < var0.size(); ++var1) {
         Recipe var2 = (Recipe)var0.get(var1);
         if (var2 == null) {
            DebugLog.Recipe.println("Null recipe.");
         } else if (var2.Result == null) {
            DebugLog.Recipe.println("Null result.");
         } else {
            DebugLog.Recipe.println(var2.Result.type);
            DebugLog.Recipe.println("-----");

            for(int var3 = 0; var3 < var2.Source.size(); ++var3) {
               if (var2.Source.get(var3) == null) {
                  DebugLog.Recipe.println("Null ingredient.");
               } else if (((Recipe.Source)var2.Source.get(var3)).getItems().isEmpty()) {
                  DebugLog.Recipe.println(((Recipe.Source)var2.Source.get(var3)).getItems().toString());
               }
            }
         }
      }

   }

   public static Recipe getDismantleRecipeFor(String var0) {
      RecipeList.clear();
      ArrayList var1 = ScriptManager.instance.getAllRecipes();

      for(int var2 = 0; var2 < var1.size(); ++var2) {
         Recipe var3 = (Recipe)var1.get(var2);
         ArrayList var4 = var3.getSource();
         if (var4.size() > 0) {
            for(int var5 = 0; var5 < var4.size(); ++var5) {
               Recipe.Source var6 = (Recipe.Source)var4.get(var5);

               for(int var7 = 0; var7 < var6.getItems().size(); ++var7) {
                  if (((String)var6.getItems().get(var7)).equalsIgnoreCase(var0) && var3.name.toLowerCase().startsWith("dismantle ")) {
                     return var3;
                  }
               }
            }
         }
      }

      return null;
   }

   private static final class SourceItems {
      InventoryItem selectedItem;
      final ArrayList allItems = new ArrayList();
      final ArrayList[] itemsPerSource;
      final RecipeManager.RMRecipeItemList.Type[] typePerSource;

      SourceItems(Recipe var1, IsoGameCharacter var2, InventoryItem var3, ArrayList var4) {
         this.itemsPerSource = new ArrayList[var1.getSource().size()];

         for(int var5 = 0; var5 < this.itemsPerSource.length; ++var5) {
            this.itemsPerSource[var5] = new ArrayList();
         }

         this.typePerSource = new RecipeManager.RMRecipeItemList.Type[var1.getSource().size()];
         this.selectedItem = var3;
      }

      public ArrayList getItems() {
         return this.allItems;
      }
   }

   private static final class RMRecipe {
      Recipe recipe;
      final ArrayList sources = new ArrayList();
      final ArrayList allItems = new ArrayList();
      boolean usesWater;
      final HashSet allSourceTypes = new HashSet();
      static ArrayDeque pool = new ArrayDeque();

      RecipeManager.RMRecipe init(Recipe var1) {
         assert this.allItems.isEmpty();

         assert this.sources.isEmpty();

         assert this.allSourceTypes.isEmpty();

         this.recipe = var1;
         this.usesWater = false;

         for(int var2 = 0; var2 < var1.getSource().size(); ++var2) {
            RecipeManager.RMRecipeSource var3 = RecipeManager.RMRecipeSource.alloc(this, var2);
            if (var3.usesWater) {
               this.usesWater = true;
            }

            this.allSourceTypes.addAll(var3.source.getItems());
            this.sources.add(var3);
         }

         return this;
      }

      RecipeManager.RMRecipe reset() {
         this.recipe = null;

         int var1;
         for(var1 = 0; var1 < this.allItems.size(); ++var1) {
            RecipeManager.RMRecipeItem.release((RecipeManager.RMRecipeItem)this.allItems.get(var1));
         }

         this.allItems.clear();

         for(var1 = 0; var1 < this.sources.size(); ++var1) {
            RecipeManager.RMRecipeSource.release((RecipeManager.RMRecipeSource)this.sources.get(var1));
         }

         this.sources.clear();
         this.allSourceTypes.clear();
         return this;
      }

      void getItemsFromContainers(IsoGameCharacter var1, ArrayList var2, InventoryItem var3) {
         int var4;
         for(var4 = 0; var4 < var2.size(); ++var4) {
            this.getItemsFromContainer(var1, (ItemContainer)var2.get(var4), var3);
         }

         if (this.Test(var3)) {
            for(var4 = 0; var4 < this.sources.size(); ++var4) {
               RecipeManager.RMRecipeSource var5 = (RecipeManager.RMRecipeSource)this.sources.get(var4);
               var5.getItemsFrom(this.allItems, this);
            }

         }
      }

      void getItemsFromContainer(IsoGameCharacter var1, ItemContainer var2, InventoryItem var3) {
         for(int var4 = 0; var4 < var2.getItems().size(); ++var4) {
            InventoryItem var5 = (InventoryItem)var2.getItems().get(var4);
            if (var3 != null && var3 == var5 || !var1.isEquippedClothing(var5) || this.isKeep(var5.getFullType())) {
               if (this.usesWater && var5 instanceof DrainableComboItem && var5.isWaterSource()) {
                  this.allItems.add(RecipeManager.RMRecipeItem.alloc(var5));
               } else if (this.allSourceTypes.contains(var5.getFullType())) {
                  this.allItems.add(RecipeManager.RMRecipeItem.alloc(var5));
               }
            }
         }

      }

      boolean Test(InventoryItem var1) {
         if (var1 != null && this.recipe.LuaTest != null) {
            Object var2 = LuaManager.getFunctionObject(this.recipe.LuaTest);
            if (var2 == null) {
               return false;
            } else {
               Boolean var3 = LuaManager.caller.protectedCallBoolean(LuaManager.thread, var2, var1, this.recipe.getResult());
               return var3 == Boolean.TRUE;
            }
         } else {
            return true;
         }
      }

      boolean hasItems() {
         for(int var1 = 0; var1 < this.sources.size(); ++var1) {
            RecipeManager.RMRecipeSource var2 = (RecipeManager.RMRecipeSource)this.sources.get(var1);
            if (!var2.hasItems()) {
               return false;
            }
         }

         return true;
      }

      boolean isKeep(String var1) {
         for(int var2 = 0; var2 < this.sources.size(); ++var2) {
            RecipeManager.RMRecipeSource var3 = (RecipeManager.RMRecipeSource)this.sources.get(var2);
            if (var3.isKeep(var1)) {
               return true;
            }
         }

         return false;
      }

      void getAvailableItems(RecipeManager.SourceItems var1, boolean var2) {
         assert var2 || this.hasItems();

         for(int var3 = 0; var3 < this.sources.size(); ++var3) {
            RecipeManager.RMRecipeSource var4 = (RecipeManager.RMRecipeSource)this.sources.get(var3);

            assert var2 || var4.hasItems();

            var4.getAvailableItems(var1, var2);
         }

      }

      void Use(ArrayList var1) {
         assert this.hasItems();

         for(int var2 = 0; var2 < this.sources.size(); ++var2) {
            RecipeManager.RMRecipeSource var3 = (RecipeManager.RMRecipeSource)this.sources.get(var2);

            assert var3.hasItems();

            var3.Use(var1);
         }

      }

      static RecipeManager.RMRecipe alloc(Recipe var0) {
         return pool.isEmpty() ? (new RecipeManager.RMRecipe()).init(var0) : ((RecipeManager.RMRecipe)pool.pop()).init(var0);
      }

      static void release(RecipeManager.RMRecipe var0) {
         assert !pool.contains(var0);

         pool.push(var0.reset());
      }
   }

   private static final class RMRecipeItemList {
      RecipeManager.RMRecipeSource source;
      final ArrayList items = new ArrayList();
      int index;
      int usesNeeded;
      RecipeManager.RMRecipeItemList.Type type;
      static ArrayDeque pool = new ArrayDeque();

      private RMRecipeItemList() {
         this.type = RecipeManager.RMRecipeItemList.Type.NONE;
      }

      RecipeManager.RMRecipeItemList init(RecipeManager.RMRecipeSource var1, int var2) {
         assert this.items.isEmpty();

         this.source = var1;
         this.index = var2;
         String var3 = (String)var1.source.getItems().get(var2);
         this.usesNeeded = (int)var1.source.getCount();
         if ("Water".equals(var3)) {
            this.type = RecipeManager.RMRecipeItemList.Type.WATER;
         } else if (var1.source.isDestroy()) {
            this.type = RecipeManager.RMRecipeItemList.Type.DESTROY;
         } else if (ScriptManager.instance.isDrainableItemType(var3)) {
            this.type = RecipeManager.RMRecipeItemList.Type.DRAINABLE;
         } else if (var1.source.use > 0.0F) {
            this.usesNeeded = (int)var1.source.use;
            this.type = RecipeManager.RMRecipeItemList.Type.FOOD;
         } else {
            this.type = RecipeManager.RMRecipeItemList.Type.OTHER;
         }

         return this;
      }

      RecipeManager.RMRecipeItemList reset() {
         this.source = null;
         this.items.clear();
         return this;
      }

      void getItemsFrom(ArrayList var1, RecipeManager.RMRecipe var2) {
         String var3 = (String)this.source.source.getItems().get(this.index);

         for(int var4 = 0; var4 < var1.size(); ++var4) {
            RecipeManager.RMRecipeItem var5 = (RecipeManager.RMRecipeItem)var1.get(var4);
            DrainableComboItem var6 = (DrainableComboItem)zombie.util.Type.tryCastTo(var5.item, DrainableComboItem.class);
            Food var7 = (Food)zombie.util.Type.tryCastTo(var5.item, Food.class);
            if ("Water".equals(var3)) {
               if (var2.Test(var5.item) && var5.item instanceof DrainableComboItem && var5.item.isWaterSource()) {
                  var5.water = RecipeManager.AvailableUses(var5.item);
                  this.items.add(var5);
               }
            } else if (var3.equals(var5.item.getFullType()) && (!(var2.recipe.getHeat() > 0.0F) || var6 == null || !var5.item.IsCookable || !(var5.item.getInvHeat() + 1.0F < var2.recipe.getHeat())) && (!(var2.recipe.getHeat() < 0.0F) || var6 == null || !var5.item.IsCookable || !(var5.item.getInvHeat() > var2.recipe.getHeat())) && (var7 == null || !(var7.getFreezingTime() > 0.0F)) && (!var2.recipe.noBrokenItems() || !var5.item.isBroken()) && (!"Clothing".equals(var5.item.getCategory()) || !var5.item.isFavorite()) && var2.Test(var5.item)) {
               if (this.source.source.isDestroy()) {
                  var5.uses = 1;
                  this.items.add(var5);
               } else if (var6 != null) {
                  var5.uses = RecipeManager.AvailableUses(var5.item);
                  this.items.add(var5);
               } else if (this.source.source.use > 0.0F) {
                  if (var5.item instanceof Food) {
                     var5.uses = RecipeManager.AvailableUses(var5.item);
                     this.items.add(var5);
                  }
               } else {
                  var5.uses = var5.item.getUses();
                  this.items.add(var5);
               }
            }
         }

      }

      boolean hasItems() {
         String var1 = (String)this.source.source.getItems().get(this.index);
         int var2 = 0;

         for(int var3 = 0; var3 < this.items.size(); ++var3) {
            if ("Water".equals(var1)) {
               var2 += ((RecipeManager.RMRecipeItem)this.items.get(var3)).water;
            } else {
               var2 += ((RecipeManager.RMRecipeItem)this.items.get(var3)).uses;
            }
         }

         return var2 >= this.usesNeeded;
      }

      int indexOf(InventoryItem var1) {
         for(int var2 = 0; var2 < this.items.size(); ++var2) {
            RecipeManager.RMRecipeItem var3 = (RecipeManager.RMRecipeItem)this.items.get(var2);
            if (var3.item == var1) {
               return var2;
            }
         }

         return -1;
      }

      void getAvailableItems(RecipeManager.SourceItems var1, boolean var2) {
         if (var2) {
            this.Use(var1.itemsPerSource[this.source.index]);
            var1.typePerSource[this.source.index] = this.type;
            var1.allItems.addAll(var1.itemsPerSource[this.source.index]);
         } else {
            assert this.hasItems();

            if (var1.selectedItem != null) {
               int var3 = this.indexOf(var1.selectedItem);
               if (var3 != -1) {
                  RecipeManager.RMRecipeItem var4 = (RecipeManager.RMRecipeItem)this.items.remove(var3);
                  this.items.add(0, var4);
               }
            }

            this.Use(var1.itemsPerSource[this.source.index]);
            var1.typePerSource[this.source.index] = this.type;
            var1.allItems.addAll(var1.itemsPerSource[this.source.index]);
         }
      }

      void Use(ArrayList var1) {
         String var2 = (String)this.source.source.getItems().get(this.index);
         int var3 = this.usesNeeded;

         for(int var4 = 0; var4 < this.items.size(); ++var4) {
            RecipeManager.RMRecipeItem var5 = (RecipeManager.RMRecipeItem)this.items.get(var4);
            if ("Water".equals(var2) && var5.water > 0) {
               var3 -= var5.UseWater(var3);
               var1.add(var5.item);
            } else if (this.source.source.isKeep() && var5.uses > 0) {
               var3 -= Math.min(var5.uses, var3);
               var1.add(var5.item);
            } else if (var5.uses > 0) {
               var3 -= var5.Use(var3);
               var1.add(var5.item);
            }

            if (var3 <= 0) {
               break;
            }
         }

      }

      static RecipeManager.RMRecipeItemList alloc(RecipeManager.RMRecipeSource var0, int var1) {
         return pool.isEmpty() ? (new RecipeManager.RMRecipeItemList()).init(var0, var1) : ((RecipeManager.RMRecipeItemList)pool.pop()).init(var0, var1);
      }

      static void release(RecipeManager.RMRecipeItemList var0) {
         assert !pool.contains(var0);

         pool.push(var0.reset());
      }

      static enum Type {
         NONE,
         WATER,
         DRAINABLE,
         FOOD,
         OTHER,
         DESTROY;

         // $FF: synthetic method
         private static RecipeManager.RMRecipeItemList.Type[] $values() {
            return new RecipeManager.RMRecipeItemList.Type[]{NONE, WATER, DRAINABLE, FOOD, OTHER, DESTROY};
         }
      }
   }

   private static final class RMRecipeItem {
      InventoryItem item;
      int uses;
      int water;
      static ArrayDeque pool = new ArrayDeque();

      RecipeManager.RMRecipeItem init(InventoryItem var1) {
         this.item = var1;
         return this;
      }

      RecipeManager.RMRecipeItem reset() {
         this.item = null;
         this.uses = 0;
         this.water = 0;
         return this;
      }

      int Use(int var1) {
         int var2 = Math.min(this.uses, var1);
         this.uses -= var2;
         return var2;
      }

      int UseWater(int var1) {
         int var2 = Math.min(this.water, var1);
         this.water -= var2;
         return var2;
      }

      static RecipeManager.RMRecipeItem alloc(InventoryItem var0) {
         return pool.isEmpty() ? (new RecipeManager.RMRecipeItem()).init(var0) : ((RecipeManager.RMRecipeItem)pool.pop()).init(var0);
      }

      static void release(RecipeManager.RMRecipeItem var0) {
         assert !pool.contains(var0);

         pool.push(var0.reset());
      }
   }

   private static final class RMRecipeSource {
      RecipeManager.RMRecipe recipe;
      Recipe.Source source;
      int index;
      final ArrayList itemLists = new ArrayList();
      boolean usesWater;
      static ArrayDeque pool = new ArrayDeque();

      RecipeManager.RMRecipeSource init(RecipeManager.RMRecipe var1, int var2) {
         this.recipe = var1;
         this.source = (Recipe.Source)var1.recipe.getSource().get(var2);
         this.index = var2;

         assert this.itemLists.isEmpty();

         for(int var3 = 0; var3 < this.source.getItems().size(); ++var3) {
            this.itemLists.add(RecipeManager.RMRecipeItemList.alloc(this, var3));
         }

         this.usesWater = this.source.getItems().contains("Water");
         return this;
      }

      RecipeManager.RMRecipeSource reset() {
         for(int var1 = 0; var1 < this.itemLists.size(); ++var1) {
            RecipeManager.RMRecipeItemList.release((RecipeManager.RMRecipeItemList)this.itemLists.get(var1));
         }

         this.itemLists.clear();
         return this;
      }

      void getItemsFrom(ArrayList var1, RecipeManager.RMRecipe var2) {
         for(int var3 = 0; var3 < this.itemLists.size(); ++var3) {
            RecipeManager.RMRecipeItemList var4 = (RecipeManager.RMRecipeItemList)this.itemLists.get(var3);
            var4.getItemsFrom(var1, var2);
         }

      }

      boolean hasItems() {
         for(int var1 = 0; var1 < this.itemLists.size(); ++var1) {
            RecipeManager.RMRecipeItemList var2 = (RecipeManager.RMRecipeItemList)this.itemLists.get(var1);
            if (var2.hasItems()) {
               return true;
            }
         }

         return false;
      }

      boolean isKeep(String var1) {
         return this.source.getItems().contains(var1) ? this.source.keep : false;
      }

      void getAvailableItems(RecipeManager.SourceItems var1, boolean var2) {
         int var3;
         if (var2) {
            for(var3 = 0; var3 < this.itemLists.size(); ++var3) {
               RecipeManager.RMRecipeItemList var6 = (RecipeManager.RMRecipeItemList)this.itemLists.get(var3);
               var6.getAvailableItems(var1, var2);
            }

         } else {
            var3 = -1;

            for(int var4 = 0; var4 < this.itemLists.size(); ++var4) {
               RecipeManager.RMRecipeItemList var5 = (RecipeManager.RMRecipeItemList)this.itemLists.get(var4);
               if (var5.hasItems()) {
                  if (var1.selectedItem != null && var5.indexOf(var1.selectedItem) != -1) {
                     var3 = var4;
                     break;
                  }

                  if (var3 == -1) {
                     var3 = var4;
                  }
               }
            }

            ((RecipeManager.RMRecipeItemList)this.itemLists.get(var3)).getAvailableItems(var1, var2);
         }
      }

      void Use(ArrayList var1) {
         assert this.hasItems();

         for(int var2 = 0; var2 < this.itemLists.size(); ++var2) {
            RecipeManager.RMRecipeItemList var3 = (RecipeManager.RMRecipeItemList)this.itemLists.get(var2);
            if (var3.hasItems()) {
               var3.Use(var1);
               return;
            }
         }

         assert false;

      }

      static RecipeManager.RMRecipeSource alloc(RecipeManager.RMRecipe var0, int var1) {
         return pool.isEmpty() ? (new RecipeManager.RMRecipeSource()).init(var0, var1) : ((RecipeManager.RMRecipeSource)pool.pop()).init(var0, var1);
      }

      static void release(RecipeManager.RMRecipeSource var0) {
         assert !pool.contains(var0);

         pool.push(var0.reset());
      }
   }
}

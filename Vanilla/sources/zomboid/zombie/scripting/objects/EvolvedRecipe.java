package zombie.scripting.objects;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Stack;
import zombie.characters.IsoGameCharacter;
import zombie.characters.skills.PerkFactory;
import zombie.core.Translator;
import zombie.inventory.InventoryItem;
import zombie.inventory.InventoryItemFactory;
import zombie.inventory.ItemContainer;
import zombie.inventory.types.Food;
import zombie.inventory.types.HandWeapon;
import zombie.scripting.ScriptManager;
import zombie.util.StringUtils;

public final class EvolvedRecipe extends BaseScriptObject {
   public String name = null;
   public String DisplayName = null;
   private String originalname;
   public int maxItems = 0;
   public final Map itemsList = new HashMap();
   public String resultItem = null;
   public String baseItem = null;
   public boolean cookable = false;
   public boolean addIngredientIfCooked = false;
   public boolean canAddSpicesEmpty = false;
   public String addIngredientSound = null;

   public EvolvedRecipe(String var1) {
      this.name = var1;
   }

   public void Load(String var1, String[] var2) {
      this.DisplayName = Translator.getRecipeName(var1);
      this.originalname = var1;

      for(int var3 = 0; var3 < var2.length; ++var3) {
         if (!var2[var3].trim().isEmpty() && var2[var3].contains(":")) {
            String[] var4 = var2[var3].split(":");
            String var5 = var4[0].trim();
            String var6 = var4[1].trim();
            if (var5.equals("BaseItem")) {
               this.baseItem = var6;
            } else if (var5.equals("Name")) {
               this.DisplayName = Translator.getRecipeName(var6);
               this.originalname = var6;
            } else if (var5.equals("ResultItem")) {
               this.resultItem = var6;
               if (!var6.contains(".")) {
                  this.resultItem = var6;
               }
            } else if (var5.equals("Cookable")) {
               this.cookable = true;
            } else if (var5.equals("MaxItems")) {
               this.maxItems = Integer.parseInt(var6);
            } else if (var5.equals("AddIngredientIfCooked")) {
               this.addIngredientIfCooked = Boolean.parseBoolean(var6);
            } else if (var5.equals("AddIngredientSound")) {
               this.addIngredientSound = StringUtils.discardNullOrWhitespace(var6);
            } else if (var5.equals("CanAddSpicesEmpty")) {
               this.canAddSpicesEmpty = Boolean.parseBoolean(var6);
            }
         }
      }

   }

   public boolean needToBeCooked(InventoryItem var1) {
      ItemRecipe var2 = this.getItemRecipe(var1);
      if (var2 == null) {
         return true;
      } else {
         return var2.cooked == var1.isCooked() || !var2.cooked;
      }
   }

   public ArrayList getItemsCanBeUse(IsoGameCharacter var1, InventoryItem var2, ArrayList var3) {
      int var4 = var1.getPerkLevel(PerkFactory.Perks.Cooking);
      if (var3 == null) {
         var3 = new ArrayList();
      }

      ArrayList var5 = new ArrayList();
      Iterator var6 = this.itemsList.keySet().iterator();
      if (!var3.contains(var1.getInventory())) {
         var3.add(var1.getInventory());
      }

      while(var6.hasNext()) {
         String var7 = (String)var6.next();
         Iterator var8 = var3.iterator();

         while(var8.hasNext()) {
            ItemContainer var9 = (ItemContainer)var8.next();
            this.checkItemCanBeUse(var9, var7, var2, var4, var5);
         }
      }

      if (var2.haveExtraItems() && var2.getExtraItems().size() >= 3) {
         for(int var11 = 0; var11 < var3.size(); ++var11) {
            ItemContainer var12 = (ItemContainer)var3.get(var11);

            for(int var13 = 0; var13 < var12.getItems().size(); ++var13) {
               InventoryItem var10 = (InventoryItem)var12.getItems().get(var13);
               if (var10 instanceof Food && ((Food)var10).getPoisonLevelForRecipe() != null && var1.isKnownPoison(var10) && !var5.contains(var10)) {
                  var5.add(var10);
               }
            }
         }
      }

      return var5;
   }

   private void checkItemCanBeUse(ItemContainer var1, String var2, InventoryItem var3, int var4, ArrayList var5) {
      ArrayList var6 = var1.getItemsFromType(var2);

      for(int var7 = 0; var7 < var6.size(); ++var7) {
         InventoryItem var8 = (InventoryItem)var6.get(var7);
         boolean var9 = false;
         if (var8 instanceof Food && ((ItemRecipe)this.itemsList.get(var2)).use != -1) {
            Food var10 = (Food)var8;
            if (var10.isSpice()) {
               if (this.isResultItem(var3)) {
                  var9 = !this.isSpiceAdded(var3, var10);
               } else if (this.canAddSpicesEmpty) {
                  var9 = true;
               }

               if (var10.isRotten() && var4 < 7) {
                  var9 = false;
               }
            } else if ((!var3.haveExtraItems() || var3.extraItems.size() < this.maxItems) && (!var10.isRotten() || var4 >= 7)) {
               var9 = true;
            }

            if (var10.isFrozen()) {
               var9 = false;
            }
         } else {
            var9 = true;
         }

         this.getItemRecipe(var8);
         if (var9) {
            var5.add(var8);
         }
      }

   }

   public InventoryItem addItem(InventoryItem var1, InventoryItem var2, IsoGameCharacter var3) {
      int var4 = var3.getPerkLevel(PerkFactory.Perks.Cooking);
      if (!this.isResultItem(var1)) {
         InventoryItem var5 = var1 instanceof Food ? var1 : null;
         InventoryItem var6 = InventoryItemFactory.CreateItem(this.resultItem);
         if (var6 != null) {
            if (var1 instanceof HandWeapon) {
               var6.getModData().rawset("condition:" + var1.getType(), (double)var1.getCondition() / (double)var1.getConditionMax());
            }

            var3.getInventory().Remove(var1);
            var3.getInventory().AddItem(var6);
            InventoryItem var7 = var1;
            var1 = var6;
            if (var6 instanceof Food) {
               ((Food)var6).setCalories(0.0F);
               ((Food)var6).setCarbohydrates(0.0F);
               ((Food)var6).setProteins(0.0F);
               ((Food)var6).setLipids(0.0F);
               if (var2 instanceof Food && ((Food)var2).getPoisonLevelForRecipe() != null) {
                  this.addPoison(var2, var6, var3);
               }

               ((Food)var6).setIsCookable(this.cookable);
               if (var5 != null) {
                  ((Food)var6).setHungChange(((Food)var5).getHungChange());
                  ((Food)var6).setBaseHunger(((Food)var5).getBaseHunger());
               } else {
                  ((Food)var6).setHungChange(0.0F);
                  ((Food)var6).setBaseHunger(0.0F);
               }

               if (var7.isTaintedWater()) {
                  var6.setTaintedWater(true);
               }

               if (var7 instanceof Food && var7.getOffAgeMax() != 1000000000 && var6.getOffAgeMax() != 1000000000) {
                  float var8 = var7.getAge() / (float)var7.getOffAgeMax();
                  var6.setAge((float)var6.getOffAgeMax() * var8);
               }

               if (var5 instanceof Food) {
                  ((Food)var6).setCalories(((Food)var5).getCalories());
                  ((Food)var6).setProteins(((Food)var5).getProteins());
                  ((Food)var6).setLipids(((Food)var5).getLipids());
                  ((Food)var6).setCarbohydrates(((Food)var5).getCarbohydrates());
                  ((Food)var6).setThirstChange(((Food)var5).getThirstChange());
               }
            }

            var6.setUnhappyChange(0.0F);
            var6.setBoredomChange(0.0F);
         }
      }

      if (this.itemsList.get(var2.getType()) != null && ((ItemRecipe)this.itemsList.get(var2.getType())).use > -1) {
         if (var2 instanceof Food) {
            float var13 = (float)((ItemRecipe)this.itemsList.get(var2.getType())).use / 100.0F;
            Food var14 = (Food)var2;
            if (var14.isSpice() && var1 instanceof Food) {
               this.useSpice(var14, (Food)var1, var13, var4);
               return var1;
            }

            boolean var15 = false;
            DecimalFormat var16;
            if (var14.isRotten()) {
               var16 = new DecimalFormat("#.##");
               if (var4 != 7 && var4 != 8) {
                  if (var4 == 9 || var4 == 10) {
                     var13 = Float.parseFloat(var16.format((double)Math.abs(var14.getBaseHunger() - (var14.getBaseHunger() - 0.1F * var14.getBaseHunger()))).replace(",", "."));
                  }
               } else {
                  var13 = Float.parseFloat(var16.format((double)Math.abs(var14.getBaseHunger() - (var14.getBaseHunger() - 0.05F * var14.getBaseHunger()))).replace(",", "."));
               }

               var15 = true;
            }

            if (Math.abs(var14.getHungerChange()) < var13) {
               var16 = new DecimalFormat("#.##");
               var16.setRoundingMode(RoundingMode.DOWN);
               var13 = Math.abs(Float.parseFloat(var16.format((double)var14.getHungerChange()).replace(",", ".")));
               var15 = true;
            }

            if (var1 instanceof Food) {
               Food var17 = (Food)var1;
               if (var2 instanceof Food && ((Food)var2).getPoisonLevelForRecipe() != null) {
                  this.addPoison(var2, var1, var3);
               }

               var17.setHungChange(var17.getHungerChange() - var13);
               var17.setBaseHunger(var17.getBaseHunger() - var13);
               if (var14.isbDangerousUncooked() && !var14.isCooked()) {
                  var17.setbDangerousUncooked(true);
               }

               int var9 = 0;
               if (var1.extraItems != null) {
                  for(int var10 = 0; var10 < var1.extraItems.size(); ++var10) {
                     if (((String)var1.extraItems.get(var10)).equals(var2.getFullType())) {
                        ++var9;
                     }
                  }
               }

               if (var1.extraItems != null && var1.extraItems.size() - 2 > var4) {
                  var9 += var1.extraItems.size() - 2 - var4 * 3;
               }

               float var18 = var13 - (float)(3 * var4) / 100.0F * var13;
               float var11 = Math.abs(var18 / var14.getHungChange());
               if (var11 > 1.0F) {
                  var11 = 1.0F;
               }

               var1.setUnhappyChange(var1.getUnhappyChange() - (float)(5 - var9 * 5));
               if (var1.getUnhappyChange() > 25.0F) {
                  var1.setUnhappyChange(25.0F);
               }

               float var12 = (float)var4 / 15.0F + 1.0F;
               var17.setCalories(var17.getCalories() + var14.getCalories() * var12 * var11);
               var17.setProteins(var17.getProteins() + var14.getProteins() * var12 * var11);
               var17.setCarbohydrates(var17.getCarbohydrates() + var14.getCarbohydrates() * var12 * var11);
               var17.setLipids(var17.getLipids() + var14.getLipids() * var12 * var11);
               var17.setThirstChange(var17.getThirstChange() + var14.getThirstChange() * var12 * var11);
               if (var14.isCooked()) {
                  var18 = (float)((double)var18 / 1.3D);
               }

               var14.setHungChange(var14.getHungChange() + var18);
               var14.setBaseHunger(var14.getBaseHunger() + var18);
               var14.setCalories(var14.getCalories() - var14.getCalories() * var11);
               var14.setProteins(var14.getProteins() - var14.getProteins() * var11);
               var14.setCarbohydrates(var14.getCarbohydrates() - var14.getCarbohydrates() * var11);
               var14.setLipids(var14.getLipids() - var14.getLipids() * var11);
               if ((double)var14.getHungerChange() >= -0.02D || var15) {
                  var2.Use();
               }

               if (var14.getFatigueChange() < 0.0F) {
                  var1.setFatigueChange(var14.getFatigueChange() * var11);
                  var14.setFatigueChange(var14.getFatigueChange() - var14.getFatigueChange() * var11);
               }
            }
         } else {
            var2.Use();
         }

         var1.addExtraItem(var2.getFullType());
      } else if (var2 instanceof Food && ((Food)var2).getPoisonLevelForRecipe() != null) {
         this.addPoison(var2, var1, var3);
      }

      this.checkUniqueRecipe(var1);
      var3.getXp().AddXP(PerkFactory.Perks.Cooking, 3.0F);
      return var1;
   }

   private void checkUniqueRecipe(InventoryItem var1) {
      if (var1 instanceof Food) {
         Food var2 = (Food)var1;
         Stack var3 = ScriptManager.instance.getAllUniqueRecipes();

         for(int var4 = 0; var4 < var3.size(); ++var4) {
            ArrayList var5 = new ArrayList();
            UniqueRecipe var6 = (UniqueRecipe)var3.get(var4);
            if (var6.getBaseRecipe().equals(var1.getType())) {
               boolean var7 = true;

               for(int var8 = 0; var8 < var6.getItems().size(); ++var8) {
                  boolean var9 = false;

                  for(int var10 = 0; var10 < var2.getExtraItems().size(); ++var10) {
                     if (!var5.contains(var10) && ((String)var2.getExtraItems().get(var10)).equals(var6.getItems().get(var8))) {
                        var9 = true;
                        var5.add(var10);
                        break;
                     }
                  }

                  if (!var9) {
                     var7 = false;
                     break;
                  }
               }

               if (var2.getExtraItems().size() == var6.getItems().size() && var7) {
                  var2.setName(var6.getName());
                  var2.setBaseHunger(var2.getBaseHunger() - (float)var6.getHungerBonus() / 100.0F);
                  var2.setHungChange(var2.getBaseHunger());
                  var2.setBoredomChange(var2.getBoredomChange() - (float)var6.getBoredomBonus());
                  var2.setUnhappyChange(var2.getUnhappyChange() - (float)var6.getHapinessBonus());
                  var2.setCustomName(true);
               }
            }
         }
      }

   }

   private void addPoison(InventoryItem var1, InventoryItem var2, IsoGameCharacter var3) {
      Food var4 = (Food)var1;
      if (var2 instanceof Food) {
         Food var5 = (Food)var2;
         int var6 = var4.getPoisonLevelForRecipe() - var3.getPerkLevel(PerkFactory.Perks.Cooking);
         if (var6 < 1) {
            var6 = 1;
         }

         Float var7 = 0.0F;
         float var8;
         if (var4.getThirstChange() <= -0.01F) {
            var8 = (float)var4.getUseForPoison() / 100.0F;
            if (Math.abs(var4.getThirstChange()) < var8) {
               var8 = Math.abs(var4.getThirstChange());
            }

            var7 = Math.abs(var8 / var4.getThirstChange());
            var7 = new Float((double)Math.round(var7.doubleValue() * 100.0D) / 100.0D);
            var4.setThirstChange(var4.getThirstChange() + var8);
            if ((double)var4.getThirstChange() > -0.01D) {
               var4.Use();
            }
         } else if (var4.getBaseHunger() <= -0.01F) {
            var8 = (float)var4.getUseForPoison() / 100.0F;
            if (Math.abs(var4.getBaseHunger()) < var8) {
               var8 = Math.abs(var4.getThirstChange());
            }

            var7 = Math.abs(var8 / var4.getBaseHunger());
            var7 = new Float((double)Math.round(var7.doubleValue() * 100.0D) / 100.0D);
         }

         if (var5.getPoisonDetectionLevel() == -1) {
            var5.setPoisonDetectionLevel(0);
         }

         var5.setPoisonDetectionLevel(var5.getPoisonDetectionLevel() + var6);
         if (var5.getPoisonDetectionLevel() > 10) {
            var5.setPoisonDetectionLevel(10);
         }

         int var9 = (new Float(var7 * ((float)var4.getPoisonPower() / 100.0F) * 100.0F)).intValue();
         var5.setPoisonPower(var5.getPoisonPower() + var9);
         var4.setPoisonPower(var4.getPoisonPower() - var9);
      }

   }

   private void useSpice(Food var1, Food var2, float var3, int var4) {
      if (!this.isSpiceAdded(var2, var1)) {
         if (var2.spices == null) {
            var2.spices = new ArrayList();
         }

         var2.spices.add(var1.getFullType());
         float var5 = var3;
         if (var1.isRotten()) {
            DecimalFormat var6 = new DecimalFormat("#.##");
            if (var4 != 7 && var4 != 8) {
               if (var4 == 9 || var4 == 10) {
                  var3 = Float.parseFloat(var6.format((double)Math.abs(var1.getBaseHunger() - (var1.getBaseHunger() - 0.1F * var1.getBaseHunger()))).replace(",", "."));
               }
            } else {
               var3 = Float.parseFloat(var6.format((double)Math.abs(var1.getBaseHunger() - (var1.getBaseHunger() - 0.05F * var1.getBaseHunger()))).replace(",", "."));
            }
         }

         float var8 = Math.abs(var3 / var1.getHungChange());
         if (var8 > 1.0F) {
            var8 = 1.0F;
         }

         float var7 = (float)var4 / 15.0F + 1.0F;
         var2.setUnhappyChange(var2.getUnhappyChange() - var3 * 200.0F);
         var2.setBoredomChange(var2.getBoredomChange() - var3 * 200.0F);
         var2.setCalories(var2.getCalories() + var1.getCalories() * var7 * var8);
         var2.setProteins(var2.getProteins() + var1.getProteins() * var7 * var8);
         var2.setCarbohydrates(var2.getCarbohydrates() + var1.getCarbohydrates() * var7 * var8);
         var2.setLipids(var2.getLipids() + var1.getLipids() * var7 * var8);
         var8 = Math.abs(var5 / var1.getHungChange());
         if (var8 > 1.0F) {
            var8 = 1.0F;
         }

         var1.setCalories(var1.getCalories() - var1.getCalories() * var8);
         var1.setProteins(var1.getProteins() - var1.getProteins() * var8);
         var1.setCarbohydrates(var1.getCarbohydrates() - var1.getCarbohydrates() * var8);
         var1.setLipids(var1.getLipids() - var1.getLipids() * var8);
         var1.setHungChange(var1.getHungChange() + var5);
         if ((double)var1.getHungerChange() > -0.01D) {
            var1.Use();
         }
      }

   }

   public ItemRecipe getItemRecipe(InventoryItem var1) {
      return (ItemRecipe)this.itemsList.get(var1.getType());
   }

   public String getName() {
      return this.DisplayName;
   }

   public String getOriginalname() {
      return this.originalname;
   }

   public String getUntranslatedName() {
      return this.name;
   }

   public String getBaseItem() {
      return this.baseItem;
   }

   public Map getItemsList() {
      return this.itemsList;
   }

   public ArrayList getPossibleItems() {
      ArrayList var1 = new ArrayList();
      Iterator var2 = this.itemsList.values().iterator();

      while(var2.hasNext()) {
         ItemRecipe var3 = (ItemRecipe)var2.next();
         var1.add(var3);
      }

      return var1;
   }

   public String getResultItem() {
      return !this.resultItem.contains(".") ? this.resultItem : this.resultItem.split("\\.")[1];
   }

   public String getFullResultItem() {
      return this.resultItem;
   }

   public boolean isCookable() {
      return this.cookable;
   }

   public int getMaxItems() {
      return this.maxItems;
   }

   public boolean isResultItem(InventoryItem var1) {
      return var1 == null ? false : this.getResultItem().equals(var1.getType());
   }

   public boolean isSpiceAdded(InventoryItem var1, InventoryItem var2) {
      if (!this.isResultItem(var1)) {
         return false;
      } else if (var1 instanceof Food && var2 instanceof Food) {
         if (!((Food)var2).isSpice()) {
            return false;
         } else {
            ArrayList var3 = ((Food)var1).getSpices();
            return var3 == null ? false : var3.contains(var2.getFullType());
         }
      } else {
         return false;
      }
   }

   public String getAddIngredientSound() {
      return this.addIngredientSound;
   }
}

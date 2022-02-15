package zombie.scripting.objects;

import java.util.ArrayList;

public final class UniqueRecipe extends BaseScriptObject {
   private String name = null;
   private String baseRecipe = null;
   private final ArrayList items = new ArrayList();
   private int hungerBonus = 0;
   private int hapinessBonus = 0;
   private int boredomBonus = 0;

   public UniqueRecipe(String var1) {
      this.setName(var1);
   }

   public void Load(String var1, String[] var2) {
      for(int var3 = 0; var3 < var2.length; ++var3) {
         if (!var2[var3].trim().isEmpty() && var2[var3].contains(":")) {
            String[] var4 = var2[var3].split(":");
            String var5 = var4[0].trim();
            String var6 = var4[1].trim();
            if (var5.equals("BaseRecipeItem")) {
               this.setBaseRecipe(var6);
            } else if (var5.equals("Item")) {
               this.items.add(var6);
            } else if (var5.equals("Hunger")) {
               this.setHungerBonus(Integer.parseInt(var6));
            } else if (var5.equals("Hapiness")) {
               this.setHapinessBonus(Integer.parseInt(var6));
            } else if (var5.equals("Boredom")) {
               this.setBoredomBonus(Integer.parseInt(var6));
            }
         }
      }

   }

   public String getName() {
      return this.name;
   }

   public void setName(String var1) {
      this.name = var1;
   }

   public String getBaseRecipe() {
      return this.baseRecipe;
   }

   public void setBaseRecipe(String var1) {
      this.baseRecipe = var1;
   }

   public int getHungerBonus() {
      return this.hungerBonus;
   }

   public void setHungerBonus(int var1) {
      this.hungerBonus = var1;
   }

   public int getHapinessBonus() {
      return this.hapinessBonus;
   }

   public void setHapinessBonus(int var1) {
      this.hapinessBonus = var1;
   }

   public ArrayList getItems() {
      return this.items;
   }

   public int getBoredomBonus() {
      return this.boredomBonus;
   }

   public void setBoredomBonus(int var1) {
      this.boredomBonus = var1;
   }
}

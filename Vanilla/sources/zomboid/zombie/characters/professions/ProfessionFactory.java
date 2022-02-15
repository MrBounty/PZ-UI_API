package zombie.characters.professions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Stack;
import zombie.characters.skills.PerkFactory;
import zombie.core.textures.Texture;
import zombie.debug.DebugLog;
import zombie.interfaces.IListBoxItem;

public final class ProfessionFactory {
   public static LinkedHashMap ProfessionMap = new LinkedHashMap();

   public static void init() {
   }

   public static ProfessionFactory.Profession addProfession(String var0, String var1, String var2, int var3) {
      ProfessionFactory.Profession var4 = new ProfessionFactory.Profession(var0, var1, var2, var3, "");
      ProfessionMap.put(var0, var4);
      return var4;
   }

   public static ProfessionFactory.Profession getProfession(String var0) {
      Iterator var1 = ProfessionMap.values().iterator();

      ProfessionFactory.Profession var2;
      do {
         if (!var1.hasNext()) {
            return null;
         }

         var2 = (ProfessionFactory.Profession)var1.next();
      } while(!var2.type.equals(var0));

      return var2;
   }

   public static ArrayList getProfessions() {
      ArrayList var0 = new ArrayList();
      Iterator var1 = ProfessionMap.values().iterator();

      while(var1.hasNext()) {
         ProfessionFactory.Profession var2 = (ProfessionFactory.Profession)var1.next();
         var0.add(var2);
      }

      return var0;
   }

   public static void Reset() {
      ProfessionMap.clear();
   }

   public static class Profession implements IListBoxItem {
      public String type;
      public String name;
      public int cost;
      public String description;
      public String IconPath;
      public Texture texture = null;
      public Stack FreeTraitStack = new Stack();
      private List freeRecipes = new ArrayList();
      public HashMap XPBoostMap = new HashMap();

      public Profession(String var1, String var2, String var3, int var4, String var5) {
         this.type = var1;
         this.name = var2;
         this.IconPath = var3;
         if (!var3.equals("")) {
            this.texture = Texture.trygetTexture(var3);
         }

         this.cost = var4;
         this.description = var5;
      }

      public Texture getTexture() {
         return this.texture;
      }

      public void addFreeTrait(String var1) {
         this.FreeTraitStack.add(var1);
      }

      public ArrayList getFreeTraits() {
         ArrayList var1 = new ArrayList();
         var1.addAll(this.FreeTraitStack);
         return var1;
      }

      public String getLabel() {
         return this.getName();
      }

      public String getIconPath() {
         return this.IconPath;
      }

      public String getLeftLabel() {
         return this.getName();
      }

      public String getRightLabel() {
         int var1 = this.getCost();
         if (var1 == 0) {
            return "";
         } else {
            String var2 = "+";
            if (var1 > 0) {
               var2 = "-";
            } else if (var1 == 0) {
               var2 = "";
            }

            if (var1 < 0) {
               var1 = -var1;
            }

            return var2 + var1;
         }
      }

      public String getType() {
         return this.type;
      }

      public void setType(String var1) {
         this.type = var1;
      }

      public String getName() {
         return this.name;
      }

      public void setName(String var1) {
         this.name = var1;
      }

      public int getCost() {
         return this.cost;
      }

      public void setCost(int var1) {
         this.cost = var1;
      }

      public String getDescription() {
         return this.description;
      }

      public void setDescription(String var1) {
         this.description = var1;
      }

      public void setIconPath(String var1) {
         this.IconPath = var1;
      }

      public Stack getFreeTraitStack() {
         return this.FreeTraitStack;
      }

      public void addXPBoost(PerkFactory.Perk var1, int var2) {
         if (var1 != null && var1 != PerkFactory.Perks.None && var1 != PerkFactory.Perks.MAX) {
            this.XPBoostMap.put(var1, var2);
         } else {
            DebugLog.General.warn("invalid perk passed to Profession.addXPBoost profession=%s perk=%s", this.name, var1);
         }
      }

      public HashMap getXPBoostMap() {
         return this.XPBoostMap;
      }

      public void setFreeTraitStack(Stack var1) {
         this.FreeTraitStack = var1;
      }

      public List getFreeRecipes() {
         return this.freeRecipes;
      }

      public void setFreeRecipes(List var1) {
         this.freeRecipes = var1;
      }
   }
}

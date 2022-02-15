package zombie.characters.traits;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map.Entry;
import zombie.characters.skills.PerkFactory;
import zombie.core.textures.Texture;
import zombie.debug.DebugLog;
import zombie.interfaces.IListBoxItem;

public final class TraitFactory {
   public static LinkedHashMap TraitMap = new LinkedHashMap();

   public static void init() {
   }

   public static void setMutualExclusive(String var0, String var1) {
      ((TraitFactory.Trait)TraitMap.get(var0)).MutuallyExclusive.add(var1);
      ((TraitFactory.Trait)TraitMap.get(var1)).MutuallyExclusive.add(var0);
   }

   public static void sortList() {
      LinkedList var0 = new LinkedList(TraitMap.entrySet());
      Collections.sort(var0, new Comparator() {
         public int compare(Entry var1, Entry var2) {
            return ((TraitFactory.Trait)var1.getValue()).name.compareTo(((TraitFactory.Trait)var2.getValue()).name);
         }
      });
      LinkedHashMap var1 = new LinkedHashMap();
      Iterator var2 = var0.iterator();

      while(var2.hasNext()) {
         Entry var3 = (Entry)var2.next();
         var1.put((String)var3.getKey(), (TraitFactory.Trait)var3.getValue());
      }

      TraitMap = var1;
   }

   public static TraitFactory.Trait addTrait(String var0, String var1, int var2, String var3, boolean var4) {
      TraitFactory.Trait var5 = new TraitFactory.Trait(var0, var1, var2, var3, var4, false);
      TraitMap.put(var0, var5);
      return var5;
   }

   public static TraitFactory.Trait addTrait(String var0, String var1, int var2, String var3, boolean var4, boolean var5) {
      TraitFactory.Trait var6 = new TraitFactory.Trait(var0, var1, var2, var3, var4, var5);
      TraitMap.put(var0, var6);
      return var6;
   }

   public static ArrayList getTraits() {
      ArrayList var0 = new ArrayList();
      Iterator var1 = TraitMap.values().iterator();

      while(var1.hasNext()) {
         TraitFactory.Trait var2 = (TraitFactory.Trait)var1.next();
         var0.add(var2);
      }

      return var0;
   }

   public static TraitFactory.Trait getTrait(String var0) {
      return TraitMap.containsKey(var0) ? (TraitFactory.Trait)TraitMap.get(var0) : null;
   }

   public static void Reset() {
      TraitMap.clear();
   }

   public static class Trait implements IListBoxItem {
      public String traitID;
      public String name;
      public int cost;
      public String description;
      public boolean prof;
      public Texture texture = null;
      private boolean removeInMP = false;
      private List freeRecipes = new ArrayList();
      public ArrayList MutuallyExclusive = new ArrayList(0);
      public HashMap XPBoostMap = new HashMap();

      public void addXPBoost(PerkFactory.Perk var1, int var2) {
         if (var1 != null && var1 != PerkFactory.Perks.None && var1 != PerkFactory.Perks.MAX) {
            this.XPBoostMap.put(var1, var2);
         } else {
            DebugLog.General.warn("invalid perk passed to Trait.addXPBoost trait=%s perk=%s", this.name, var1);
         }
      }

      public List getFreeRecipes() {
         return this.freeRecipes;
      }

      public void setFreeRecipes(List var1) {
         this.freeRecipes = var1;
      }

      public Trait(String var1, String var2, int var3, String var4, boolean var5, boolean var6) {
         this.traitID = var1;
         this.name = var2;
         this.cost = var3;
         this.description = var4;
         this.prof = var5;
         this.texture = Texture.getSharedTexture("media/ui/Traits/trait_" + this.traitID.toLowerCase(Locale.ENGLISH) + ".png");
         if (this.texture == null) {
            this.texture = Texture.getSharedTexture("media/ui/Traits/trait_generic.png");
         }

         this.removeInMP = var6;
      }

      public String getType() {
         return this.traitID;
      }

      public Texture getTexture() {
         return this.texture;
      }

      public String getLabel() {
         return this.name;
      }

      public String getLeftLabel() {
         return this.name;
      }

      public String getRightLabel() {
         int var1 = this.cost;
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

            return var2 + (new Integer(var1)).toString();
         }
      }

      public int getCost() {
         return this.cost;
      }

      public boolean isFree() {
         return this.prof;
      }

      public String getDescription() {
         return this.description;
      }

      public void setDescription(String var1) {
         this.description = var1;
      }

      public ArrayList getMutuallyExclusiveTraits() {
         return this.MutuallyExclusive;
      }

      public HashMap getXPBoostMap() {
         return this.XPBoostMap;
      }

      public boolean isRemoveInMP() {
         return this.removeInMP;
      }

      public void setRemoveInMP(boolean var1) {
         this.removeInMP = var1;
      }
   }
}

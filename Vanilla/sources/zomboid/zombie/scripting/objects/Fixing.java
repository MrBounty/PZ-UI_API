package zombie.scripting.objects;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Predicate;
import zombie.GameWindow;
import zombie.characters.IsoGameCharacter;
import zombie.inventory.InventoryItem;
import zombie.inventory.ItemContainer;
import zombie.inventory.types.DrainableComboItem;
import zombie.util.Type;

public final class Fixing extends BaseScriptObject {
   private String name = null;
   private ArrayList require = null;
   private final LinkedList fixers = new LinkedList();
   private Fixing.Fixer globalItem = null;
   private float conditionModifier = 1.0F;
   private static final Fixing.PredicateRequired s_PredicateRequired = new Fixing.PredicateRequired();
   private static final ArrayList s_InventoryItems = new ArrayList();

   public void Load(String var1, String[] var2) {
      this.setName(var1);

      for(int var3 = 0; var3 < var2.length; ++var3) {
         if (!var2[var3].trim().isEmpty() && var2[var3].contains(":")) {
            String[] var4 = var2[var3].split(":");
            String var5 = var4[0].trim();
            String var6 = var4[1].trim();
            if (var5.equals("Require")) {
               List var11 = Arrays.asList(var6.split(";"));

               for(int var12 = 0; var12 < var11.size(); ++var12) {
                  this.addRequiredItem(((String)var11.get(var12)).trim());
               }
            } else if (!var5.equals("Fixer")) {
               if (var5.equals("GlobalItem")) {
                  if (var6.contains("=")) {
                     this.setGlobalItem(new Fixing.Fixer(var6.split("=")[0], (LinkedList)null, Integer.parseInt(var6.split("=")[1])));
                  } else {
                     this.setGlobalItem(new Fixing.Fixer(var6, (LinkedList)null, 1));
                  }
               } else if (var5.equals("ConditionModifier")) {
                  this.setConditionModifier(Float.parseFloat(var6.trim()));
               }
            } else if (!var6.contains(";")) {
               if (var6.contains("=")) {
                  this.fixers.add(new Fixing.Fixer(var6.split("=")[0], (LinkedList)null, Integer.parseInt(var6.split("=")[1])));
               } else {
                  this.fixers.add(new Fixing.Fixer(var6, (LinkedList)null, 1));
               }
            } else {
               LinkedList var7 = new LinkedList();
               List var8 = Arrays.asList(var6.split(";"));

               for(int var9 = 1; var9 < var8.size(); ++var9) {
                  String[] var10 = ((String)var8.get(var9)).trim().split("=");
                  var7.add(new Fixing.FixerSkill(var10[0].trim(), Integer.parseInt(var10[1].trim())));
               }

               if (var6.split(";")[0].trim().contains("=")) {
                  String[] var13 = var6.split(";")[0].trim().split("=");
                  this.fixers.add(new Fixing.Fixer(var13[0], var7, Integer.parseInt(var13[1])));
               } else {
                  this.fixers.add(new Fixing.Fixer(var6.split(";")[0].trim(), var7, 1));
               }
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

   public ArrayList getRequiredItem() {
      return this.require;
   }

   public void addRequiredItem(String var1) {
      if (this.require == null) {
         this.require = new ArrayList();
      }

      this.require.add(var1);
   }

   public LinkedList getFixers() {
      return this.fixers;
   }

   public Fixing.Fixer usedInFixer(InventoryItem var1, IsoGameCharacter var2) {
      for(int var3 = 0; var3 < this.getFixers().size(); ++var3) {
         Fixing.Fixer var4 = (Fixing.Fixer)this.getFixers().get(var3);
         if (var4.getFixerName().equals(var1.getType())) {
            if (var1 instanceof DrainableComboItem) {
               DrainableComboItem var5 = (DrainableComboItem)var1;
               if (!(var5.getUsedDelta() < 1.0F)) {
                  return var4;
               }

               if (var5.getDrainableUsesInt() >= var4.getNumberOfUse()) {
                  return var4;
               }
            } else {
               ItemContainer var10000 = var2.getInventory();
               String var10001 = this.getModule().getName();
               if (var10000.getCountTypeRecurse(var10001 + "." + var4.getFixerName()) >= var4.getNumberOfUse()) {
                  return var4;
               }
            }
         }
      }

      return null;
   }

   public InventoryItem haveGlobalItem(IsoGameCharacter var1) {
      s_InventoryItems.clear();
      ArrayList var2 = this.getRequiredFixerItems(var1, this.getGlobalItem(), (InventoryItem)null, s_InventoryItems);
      return var2 == null ? null : (InventoryItem)var2.get(0);
   }

   public InventoryItem haveThisFixer(IsoGameCharacter var1, Fixing.Fixer var2, InventoryItem var3) {
      s_InventoryItems.clear();
      ArrayList var4 = this.getRequiredFixerItems(var1, var2, var3, s_InventoryItems);
      return var4 == null ? null : (InventoryItem)var4.get(0);
   }

   public int countUses(IsoGameCharacter var1, Fixing.Fixer var2, InventoryItem var3) {
      s_InventoryItems.clear();
      s_PredicateRequired.uses = 0;
      this.getRequiredFixerItems(var1, var2, var3, s_InventoryItems);
      return s_PredicateRequired.uses;
   }

   private static int countUses(InventoryItem var0) {
      DrainableComboItem var1 = (DrainableComboItem)Type.tryCastTo(var0, DrainableComboItem.class);
      return var1 != null ? var1.getDrainableUsesInt() : 1;
   }

   public ArrayList getRequiredFixerItems(IsoGameCharacter var1, Fixing.Fixer var2, InventoryItem var3, ArrayList var4) {
      if (var2 == null) {
         return null;
      } else {
         assert Thread.currentThread() == GameWindow.GameThread;

         Fixing.PredicateRequired var5 = s_PredicateRequired;
         var5.fixer = var2;
         var5.brokenItem = var3;
         var5.uses = 0;
         var1.getInventory().getAllRecurse(var5, var4);
         return var5.uses >= var2.getNumberOfUse() ? var4 : null;
      }
   }

   public ArrayList getRequiredItems(IsoGameCharacter var1, Fixing.Fixer var2, InventoryItem var3) {
      ArrayList var4 = new ArrayList();
      if (this.getRequiredFixerItems(var1, var2, var3, var4) == null) {
         var4.clear();
         return null;
      } else if (this.getGlobalItem() != null && this.getRequiredFixerItems(var1, this.getGlobalItem(), var3, var4) == null) {
         var4.clear();
         return null;
      } else {
         return var4;
      }
   }

   public Fixing.Fixer getGlobalItem() {
      return this.globalItem;
   }

   public void setGlobalItem(Fixing.Fixer var1) {
      this.globalItem = var1;
   }

   public float getConditionModifier() {
      return this.conditionModifier;
   }

   public void setConditionModifier(float var1) {
      this.conditionModifier = var1;
   }

   public static final class Fixer {
      private String fixerName = null;
      private LinkedList skills = null;
      private int numberOfUse = 1;

      public Fixer(String var1, LinkedList var2, int var3) {
         this.fixerName = var1;
         this.skills = var2;
         this.numberOfUse = var3;
      }

      public String getFixerName() {
         return this.fixerName;
      }

      public LinkedList getFixerSkills() {
         return this.skills;
      }

      public int getNumberOfUse() {
         return this.numberOfUse;
      }
   }

   public static final class FixerSkill {
      private String skillName = null;
      private int skillLvl = 0;

      public FixerSkill(String var1, int var2) {
         this.skillName = var1;
         this.skillLvl = var2;
      }

      public String getSkillName() {
         return this.skillName;
      }

      public int getSkillLevel() {
         return this.skillLvl;
      }
   }

   private static final class PredicateRequired implements Predicate {
      Fixing.Fixer fixer;
      InventoryItem brokenItem;
      int uses;

      public boolean test(InventoryItem var1) {
         if (this.uses >= this.fixer.getNumberOfUse()) {
            return false;
         } else if (var1 == this.brokenItem) {
            return false;
         } else if (!this.fixer.getFixerName().equals(var1.getType())) {
            return false;
         } else {
            int var2 = Fixing.countUses(var1);
            if (var2 > 0) {
               this.uses += var2;
               return true;
            } else {
               return false;
            }
         }
      }
   }
}

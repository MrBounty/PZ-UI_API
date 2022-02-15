package zombie.inventory;

import java.util.ArrayList;
import java.util.List;
import zombie.characters.IsoGameCharacter;
import zombie.characters.skills.PerkFactory;
import zombie.core.Rand;
import zombie.inventory.types.DrainableComboItem;
import zombie.scripting.ScriptManager;
import zombie.scripting.objects.Fixing;

public final class FixingManager {
   public static ArrayList getFixes(InventoryItem var0) {
      ArrayList var1 = new ArrayList();
      List var2 = ScriptManager.instance.getAllFixing(new ArrayList());

      for(int var3 = 0; var3 < var2.size(); ++var3) {
         Fixing var4 = (Fixing)var2.get(var3);
         if (var4.getRequiredItem().contains(var0.getType())) {
            var1.add(var4);
         }
      }

      return var1;
   }

   public static InventoryItem fixItem(InventoryItem var0, IsoGameCharacter var1, Fixing var2, Fixing.Fixer var3) {
      if ((double)Rand.Next(100) >= getChanceOfFail(var0, var1, var2, var3)) {
         double var4 = getCondRepaired(var0, var1, var2, var3);
         int var6 = var0.getConditionMax() - var0.getCondition();
         Double var7 = new Double((double)var6 * (var4 / 100.0D));
         int var8 = (int)Math.round(var7);
         if (var8 == 0) {
            var8 = 1;
         }

         var0.setCondition(var0.getCondition() + var8);
         var0.setHaveBeenRepaired(var0.getHaveBeenRepaired() + 1);
      } else if (var0.getCondition() > 0 && Rand.Next(5) == 0) {
         var0.setCondition(var0.getCondition() - 1);
         var1.getEmitter().playSound("FixingItemFailed");
      }

      useFixer(var1, var3, var0);
      if (var2.getGlobalItem() != null) {
         useFixer(var1, var2.getGlobalItem(), var0);
      }

      addXp(var1, var3);
      return var0;
   }

   private static void addXp(IsoGameCharacter var0, Fixing.Fixer var1) {
      if (var1.getFixerSkills() != null) {
         for(int var2 = 0; var2 < var1.getFixerSkills().size(); ++var2) {
            Fixing.FixerSkill var3 = (Fixing.FixerSkill)var1.getFixerSkills().get(var2);
            var0.getXp().AddXP(PerkFactory.Perks.FromString(var3.getSkillName()), (float)Rand.Next(3, 6));
         }

      }
   }

   public static void useFixer(IsoGameCharacter var0, Fixing.Fixer var1, InventoryItem var2) {
      int var3 = var1.getNumberOfUse();

      for(int var4 = 0; var4 < var0.getInventory().getItems().size(); ++var4) {
         if (var2 != var0.getInventory().getItems().get(var4)) {
            InventoryItem var5 = (InventoryItem)var0.getInventory().getItems().get(var4);
            if (var5 != null && var5.getType().equals(var1.getFixerName())) {
               if (!(var5 instanceof DrainableComboItem)) {
                  var0.getInventory().Remove(var5);
                  --var4;
                  --var3;
               } else {
                  if ("DuctTape".equals(var5.getType()) || "Scotchtape".equals(var5.getType())) {
                     var0.getEmitter().playSound("FixWithTape");
                  }

                  int var6 = ((DrainableComboItem)var5).getDrainableUsesInt();
                  int var7 = Math.min(var6, var3);

                  for(int var8 = 0; var8 < var7; ++var8) {
                     var5.Use();
                     --var3;
                     if (!var0.getInventory().getItems().contains(var5)) {
                        --var4;
                        break;
                     }
                  }
               }
            }

            if (var3 == 0) {
               break;
            }
         }
      }

   }

   public static double getChanceOfFail(InventoryItem var0, IsoGameCharacter var1, Fixing var2, Fixing.Fixer var3) {
      double var4 = 3.0D;
      if (var3.getFixerSkills() != null) {
         for(int var6 = 0; var6 < var3.getFixerSkills().size(); ++var6) {
            if (var1.getPerkLevel(PerkFactory.Perks.FromString(((Fixing.FixerSkill)var3.getFixerSkills().get(var6)).getSkillName())) < ((Fixing.FixerSkill)var3.getFixerSkills().get(var6)).getSkillLevel()) {
               var4 += (double)((((Fixing.FixerSkill)var3.getFixerSkills().get(var6)).getSkillLevel() - var1.getPerkLevel(PerkFactory.Perks.FromString(((Fixing.FixerSkill)var3.getFixerSkills().get(var6)).getSkillName()))) * 30);
            } else {
               var4 -= (double)((var1.getPerkLevel(PerkFactory.Perks.FromString(((Fixing.FixerSkill)var3.getFixerSkills().get(var6)).getSkillName())) - ((Fixing.FixerSkill)var3.getFixerSkills().get(var6)).getSkillLevel()) * 5);
            }
         }
      }

      var4 += (double)(var0.getHaveBeenRepaired() * 2);
      if (var1.Traits.Lucky.isSet()) {
         var4 -= 5.0D;
      }

      if (var1.Traits.Unlucky.isSet()) {
         var4 += 5.0D;
      }

      if (var4 > 100.0D) {
         var4 = 100.0D;
      }

      if (var4 < 0.0D) {
         var4 = 0.0D;
      }

      return var4;
   }

   public static double getCondRepaired(InventoryItem var0, IsoGameCharacter var1, Fixing var2, Fixing.Fixer var3) {
      double var4 = 0.0D;
      switch(var2.getFixers().indexOf(var3)) {
      case 0:
         var4 = 50.0D * (1.0D / (double)var0.getHaveBeenRepaired());
         break;
      case 1:
         var4 = 20.0D * (1.0D / (double)var0.getHaveBeenRepaired());
         break;
      default:
         var4 = 10.0D * (1.0D / (double)var0.getHaveBeenRepaired());
      }

      if (var3.getFixerSkills() != null) {
         for(int var6 = 0; var6 < var3.getFixerSkills().size(); ++var6) {
            Fixing.FixerSkill var7 = (Fixing.FixerSkill)var3.getFixerSkills().get(var6);
            int var8 = var1.getPerkLevel(PerkFactory.Perks.FromString(var7.getSkillName()));
            if (var8 > var7.getSkillLevel()) {
               var4 += (double)Math.min((var8 - var7.getSkillLevel()) * 5, 25);
            } else {
               var4 -= (double)((var7.getSkillLevel() - var8) * 15);
            }
         }
      }

      var4 *= (double)var2.getConditionModifier();
      var4 = Math.max(0.0D, var4);
      var4 = Math.min(100.0D, var4);
      return var4;
   }
}

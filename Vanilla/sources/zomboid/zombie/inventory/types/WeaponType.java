package zombie.inventory.types;

import java.util.Arrays;
import java.util.List;
import zombie.characters.IsoGameCharacter;
import zombie.inventory.InventoryItem;

public enum WeaponType {
   barehand("", Arrays.asList(""), true, false),
   twohanded("2handed", Arrays.asList("default", "default", "overhead", "uppercut"), true, false),
   onehanded("1handed", Arrays.asList("default", "default", "overhead", "uppercut"), true, false),
   heavy("heavy", Arrays.asList("default", "default", "overhead"), true, false),
   knife("knife", Arrays.asList("default", "default", "overhead", "uppercut"), true, false),
   spear("spear", Arrays.asList("default"), true, false),
   handgun("handgun", Arrays.asList(""), false, true),
   firearm("firearm", Arrays.asList(""), false, true),
   throwing("throwing", Arrays.asList(""), false, true),
   chainsaw("chainsaw", Arrays.asList("default"), true, false);

   public String type = "";
   public List possibleAttack;
   public boolean canMiss = true;
   public boolean isRanged = false;

   private WeaponType(String var3, List var4, boolean var5, boolean var6) {
      this.type = var3;
      this.possibleAttack = var4;
      this.canMiss = var5;
      this.isRanged = var6;
   }

   public static WeaponType getWeaponType(HandWeapon var0) {
      WeaponType var1 = null;
      if (var0.getSwingAnim().equalsIgnoreCase("Stab")) {
         return knife;
      } else if (var0.getSwingAnim().equalsIgnoreCase("Heavy")) {
         return heavy;
      } else if (var0.getSwingAnim().equalsIgnoreCase("Throw")) {
         return throwing;
      } else {
         if (!var0.isRanged()) {
            var1 = onehanded;
            if (var0.isTwoHandWeapon()) {
               var1 = twohanded;
               if (var0.getSwingAnim().equalsIgnoreCase("Spear")) {
                  return spear;
               }

               if ("Chainsaw".equals(var0.getType())) {
                  return chainsaw;
               }
            }
         } else {
            var1 = handgun;
            if (var0.isTwoHandWeapon()) {
               var1 = firearm;
            }
         }

         if (var1 == null) {
            var1 = barehand;
         }

         return var1;
      }
   }

   public static WeaponType getWeaponType(IsoGameCharacter var0) {
      if (var0 == null) {
         return null;
      } else {
         WeaponType var1 = null;
         var0.setVariable("rangedWeapon", false);
         InventoryItem var2 = var0.getPrimaryHandItem();
         InventoryItem var3 = var0.getSecondaryHandItem();
         if (var2 != null && var2 instanceof HandWeapon) {
            if (var2.getSwingAnim().equalsIgnoreCase("Stab")) {
               return knife;
            }

            if (var2.getSwingAnim().equalsIgnoreCase("Heavy")) {
               return heavy;
            }

            if (var2.getSwingAnim().equalsIgnoreCase("Throw")) {
               var0.setVariable("rangedWeapon", true);
               return throwing;
            }

            if (!((HandWeapon)var2).isRanged()) {
               var1 = onehanded;
               if (var2 == var3 && var2.isTwoHandWeapon()) {
                  var1 = twohanded;
                  if (var2.getSwingAnim().equalsIgnoreCase("Spear")) {
                     return spear;
                  }

                  if ("Chainsaw".equals(var2.getType())) {
                     return chainsaw;
                  }
               }
            } else {
               var1 = handgun;
               if (var2 == var3 && var2.isTwoHandWeapon()) {
                  var1 = firearm;
               }
            }
         }

         if (var1 == null) {
            var1 = barehand;
         }

         var0.setVariable("rangedWeapon", var1 == handgun || var1 == firearm);
         return var1;
      }
   }

   public String getType() {
      return this.type;
   }

   // $FF: synthetic method
   private static WeaponType[] $values() {
      return new WeaponType[]{barehand, twohanded, onehanded, heavy, knife, spear, handgun, firearm, throwing, chainsaw};
   }
}

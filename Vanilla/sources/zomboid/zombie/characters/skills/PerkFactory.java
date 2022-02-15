package zombie.characters.skills;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import zombie.core.Translator;
import zombie.core.math.PZMath;

public final class PerkFactory {
   public static final ArrayList PerkList = new ArrayList();
   private static final HashMap PerkById = new HashMap();
   private static final HashMap PerkByName = new HashMap();
   private static final PerkFactory.Perk[] PerkByIndex = new PerkFactory.Perk[256];
   private static int NextPerkID = 0;
   static float PerkXPReqMultiplier = 1.5F;

   public static String getPerkName(PerkFactory.Perk var0) {
      return var0.getName();
   }

   public static PerkFactory.Perk getPerkFromName(String var0) {
      return (PerkFactory.Perk)PerkByName.get(var0);
   }

   public static PerkFactory.Perk getPerk(PerkFactory.Perk var0) {
      return var0;
   }

   public static PerkFactory.Perk AddPerk(PerkFactory.Perk var0, String var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8, int var9, int var10, int var11) {
      return AddPerk(var0, var1, PerkFactory.Perks.None, var2, var3, var4, var5, var6, var7, var8, var9, var10, var11, false);
   }

   public static PerkFactory.Perk AddPerk(PerkFactory.Perk var0, String var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8, int var9, int var10, int var11, boolean var12) {
      return AddPerk(var0, var1, PerkFactory.Perks.None, var2, var3, var4, var5, var6, var7, var8, var9, var10, var11, var12);
   }

   public static PerkFactory.Perk AddPerk(PerkFactory.Perk var0, String var1, PerkFactory.Perk var2, int var3, int var4, int var5, int var6, int var7, int var8, int var9, int var10, int var11, int var12) {
      return AddPerk(var0, var1, var2, var3, var4, var5, var6, var7, var8, var9, var10, var11, var12, false);
   }

   public static PerkFactory.Perk AddPerk(PerkFactory.Perk var0, String var1, PerkFactory.Perk var2, int var3, int var4, int var5, int var6, int var7, int var8, int var9, int var10, int var11, int var12, boolean var13) {
      var0.translation = var1;
      var0.name = Translator.getText("IGUI_perks_" + var1);
      var0.parent = var2;
      var0.passiv = var13;
      var0.xp1 = (int)((float)var3 * PerkXPReqMultiplier);
      var0.xp2 = (int)((float)var4 * PerkXPReqMultiplier);
      var0.xp3 = (int)((float)var5 * PerkXPReqMultiplier);
      var0.xp4 = (int)((float)var6 * PerkXPReqMultiplier);
      var0.xp5 = (int)((float)var7 * PerkXPReqMultiplier);
      var0.xp6 = (int)((float)var8 * PerkXPReqMultiplier);
      var0.xp7 = (int)((float)var9 * PerkXPReqMultiplier);
      var0.xp8 = (int)((float)var10 * PerkXPReqMultiplier);
      var0.xp9 = (int)((float)var11 * PerkXPReqMultiplier);
      var0.xp10 = (int)((float)var12 * PerkXPReqMultiplier);
      PerkByName.put(var0.getName(), var0);
      PerkList.add(var0);
      return var0;
   }

   public static void init() {
      PerkFactory.Perks.None.parent = PerkFactory.Perks.None;
      PerkFactory.Perks.MAX.parent = PerkFactory.Perks.None;
      AddPerk(PerkFactory.Perks.Combat, "Combat", 50, 100, 200, 500, 1000, 2000, 3000, 4000, 5000, 6000);
      AddPerk(PerkFactory.Perks.Axe, "Axe", PerkFactory.Perks.Combat, 50, 100, 200, 500, 1000, 2000, 3000, 4000, 5000, 6000);
      AddPerk(PerkFactory.Perks.Blunt, "Blunt", PerkFactory.Perks.Combat, 50, 100, 200, 500, 1000, 2000, 3000, 4000, 5000, 6000);
      AddPerk(PerkFactory.Perks.SmallBlunt, "SmallBlunt", PerkFactory.Perks.Combat, 50, 100, 200, 500, 1000, 2000, 3000, 4000, 5000, 6000);
      AddPerk(PerkFactory.Perks.LongBlade, "LongBlade", PerkFactory.Perks.Combat, 50, 100, 200, 500, 1000, 2000, 3000, 4000, 5000, 6000);
      AddPerk(PerkFactory.Perks.SmallBlade, "SmallBlade", PerkFactory.Perks.Combat, 50, 100, 200, 500, 1000, 2000, 3000, 4000, 5000, 6000);
      AddPerk(PerkFactory.Perks.Spear, "Spear", PerkFactory.Perks.Combat, 50, 100, 200, 500, 1000, 2000, 3000, 4000, 5000, 6000);
      AddPerk(PerkFactory.Perks.Maintenance, "Maintenance", PerkFactory.Perks.Combat, 50, 100, 200, 500, 1000, 2000, 3000, 4000, 5000, 6000);
      AddPerk(PerkFactory.Perks.Firearm, "Firearm", 50, 100, 200, 500, 1000, 2000, 3000, 4000, 5000, 6000);
      AddPerk(PerkFactory.Perks.Aiming, "Aiming", PerkFactory.Perks.Firearm, 50, 100, 200, 500, 1000, 2000, 3000, 4000, 5000, 6000);
      AddPerk(PerkFactory.Perks.Reloading, "Reloading", PerkFactory.Perks.Firearm, 50, 100, 200, 500, 1000, 2000, 3000, 4000, 5000, 6000);
      AddPerk(PerkFactory.Perks.Crafting, "Crafting", 50, 100, 200, 500, 1000, 2000, 3000, 4000, 5000, 6000);
      AddPerk(PerkFactory.Perks.Woodwork, "Carpentry", PerkFactory.Perks.Crafting, 50, 100, 200, 500, 1000, 2000, 3000, 4000, 5000, 6000);
      AddPerk(PerkFactory.Perks.Cooking, "Cooking", PerkFactory.Perks.Crafting, 50, 100, 200, 500, 1000, 2000, 3000, 4000, 5000, 6000);
      AddPerk(PerkFactory.Perks.Farming, "Farming", PerkFactory.Perks.Crafting, 50, 100, 200, 500, 1000, 2000, 3000, 4000, 5000, 6000);
      AddPerk(PerkFactory.Perks.Doctor, "Doctor", PerkFactory.Perks.Crafting, 50, 100, 200, 500, 1000, 2000, 3000, 4000, 5000, 6000);
      AddPerk(PerkFactory.Perks.Electricity, "Electricity", PerkFactory.Perks.Crafting, 50, 100, 200, 500, 1000, 2000, 3000, 4000, 5000, 6000);
      AddPerk(PerkFactory.Perks.MetalWelding, "MetalWelding", PerkFactory.Perks.Crafting, 50, 100, 200, 500, 1000, 2000, 3000, 4000, 5000, 6000);
      AddPerk(PerkFactory.Perks.Mechanics, "Mechanics", PerkFactory.Perks.Crafting, 50, 100, 200, 500, 1000, 2000, 3000, 4000, 5000, 6000);
      AddPerk(PerkFactory.Perks.Tailoring, "Tailoring", PerkFactory.Perks.Crafting, 50, 100, 200, 500, 1000, 2000, 3000, 4000, 5000, 6000);
      AddPerk(PerkFactory.Perks.Survivalist, "Survivalist", 50, 100, 200, 500, 1000, 2000, 3000, 4000, 5000, 6000);
      AddPerk(PerkFactory.Perks.Fishing, "Fishing", PerkFactory.Perks.Survivalist, 50, 100, 200, 500, 1000, 2000, 3000, 4000, 5000, 6000);
      AddPerk(PerkFactory.Perks.Trapping, "Trapping", PerkFactory.Perks.Survivalist, 50, 100, 200, 500, 1000, 2000, 3000, 4000, 5000, 6000);
      AddPerk(PerkFactory.Perks.PlantScavenging, "Foraging", PerkFactory.Perks.Survivalist, 50, 100, 200, 500, 1000, 2000, 3000, 4000, 5000, 6000);
      AddPerk(PerkFactory.Perks.Passiv, "Passive", 50, 100, 200, 500, 1000, 2000, 3000, 4000, 5000, 6000, true);
      AddPerk(PerkFactory.Perks.Fitness, "Fitness", PerkFactory.Perks.Passiv, 1000, 2000, 4000, 6000, 12000, 20000, 40000, 60000, 80000, 100000, true);
      AddPerk(PerkFactory.Perks.Strength, "Strength", PerkFactory.Perks.Passiv, 1000, 2000, 4000, 6000, 12000, 20000, 40000, 60000, 80000, 100000, true);
      AddPerk(PerkFactory.Perks.Agility, "Agility", 50, 100, 200, 500, 1000, 2000, 3000, 4000, 5000, 6000);
      AddPerk(PerkFactory.Perks.Sprinting, "Sprinting", PerkFactory.Perks.Agility, 50, 100, 200, 500, 1000, 2000, 3000, 4000, 5000, 6000);
      AddPerk(PerkFactory.Perks.Lightfoot, "Lightfooted", PerkFactory.Perks.Agility, 50, 100, 200, 500, 1000, 2000, 3000, 4000, 5000, 6000);
      AddPerk(PerkFactory.Perks.Nimble, "Nimble", PerkFactory.Perks.Agility, 50, 100, 200, 500, 1000, 2000, 3000, 4000, 5000, 6000);
      AddPerk(PerkFactory.Perks.Sneak, "Sneaking", PerkFactory.Perks.Agility, 50, 100, 200, 500, 1000, 2000, 3000, 4000, 5000, 6000);
   }

   public static void initTranslations() {
      PerkByName.clear();
      Iterator var0 = PerkList.iterator();

      while(var0.hasNext()) {
         PerkFactory.Perk var1 = (PerkFactory.Perk)var0.next();
         var1.name = Translator.getText("IGUI_perks_" + var1.translation);
         PerkByName.put(var1.name, var1);
      }

   }

   public static void Reset() {
      NextPerkID = 0;

      for(int var0 = PerkByIndex.length - 1; var0 >= 0; --var0) {
         PerkFactory.Perk var1 = PerkByIndex[var0];
         if (var1 != null) {
            if (var1.isCustom()) {
               PerkList.remove(var1);
               PerkById.remove(var1.getId());
               PerkByName.remove(var1.getName());
               PerkByIndex[var1.index] = null;
            } else if (var1 != PerkFactory.Perks.MAX && NextPerkID == 0) {
               NextPerkID = var0 + 1;
            }
         }
      }

      PerkFactory.Perks.MAX.index = NextPerkID;
   }

   public static final class Perk {
      private final String id;
      private int index;
      private boolean bCustom;
      public String translation;
      public String name;
      public boolean passiv;
      public int xp1;
      public int xp2;
      public int xp3;
      public int xp4;
      public int xp5;
      public int xp6;
      public int xp7;
      public int xp8;
      public int xp9;
      public int xp10;
      public PerkFactory.Perk parent;

      public Perk(String var1) {
         this.bCustom = false;
         this.passiv = false;
         this.parent = PerkFactory.Perks.None;
         this.id = var1;
         this.index = PerkFactory.NextPerkID++;
         this.translation = var1;
         this.name = var1;
         PerkFactory.PerkById.put(var1, this);
         PerkFactory.PerkByIndex[this.index] = this;
         if (PerkFactory.Perks.MAX != null) {
            PerkFactory.Perks.MAX.index = PZMath.max(PerkFactory.Perks.MAX.index, this.index + 1);
         }

      }

      public Perk(String var1, PerkFactory.Perk var2) {
         this(var1);
         this.parent = var2;
      }

      public String getId() {
         return this.id;
      }

      public int index() {
         return this.index;
      }

      public void setCustom() {
         this.bCustom = true;
      }

      public boolean isCustom() {
         return this.bCustom;
      }

      public boolean isPassiv() {
         return this.passiv;
      }

      public PerkFactory.Perk getParent() {
         return this.parent;
      }

      public String getName() {
         return this.name;
      }

      public PerkFactory.Perk getType() {
         return this;
      }

      public int getXp1() {
         return this.xp1;
      }

      public int getXp2() {
         return this.xp2;
      }

      public int getXp3() {
         return this.xp3;
      }

      public int getXp4() {
         return this.xp4;
      }

      public int getXp5() {
         return this.xp5;
      }

      public int getXp6() {
         return this.xp6;
      }

      public int getXp7() {
         return this.xp7;
      }

      public int getXp8() {
         return this.xp8;
      }

      public int getXp9() {
         return this.xp9;
      }

      public int getXp10() {
         return this.xp10;
      }

      public float getXpForLevel(int var1) {
         if (var1 == 1) {
            return (float)this.xp1;
         } else if (var1 == 2) {
            return (float)this.xp2;
         } else if (var1 == 3) {
            return (float)this.xp3;
         } else if (var1 == 4) {
            return (float)this.xp4;
         } else if (var1 == 5) {
            return (float)this.xp5;
         } else if (var1 == 6) {
            return (float)this.xp6;
         } else if (var1 == 7) {
            return (float)this.xp7;
         } else if (var1 == 8) {
            return (float)this.xp8;
         } else if (var1 == 9) {
            return (float)this.xp9;
         } else {
            return var1 == 10 ? (float)this.xp10 : -1.0F;
         }
      }

      public float getTotalXpForLevel(int var1) {
         int var2 = 0;

         for(int var3 = 1; var3 <= var1; ++var3) {
            float var4 = this.getXpForLevel(var3);
            if (var4 != -1.0F) {
               var2 = (int)((float)var2 + var4);
            }
         }

         return (float)var2;
      }

      public String toString() {
         return this.id;
      }
   }

   public static final class Perks {
      public static final PerkFactory.Perk None = new PerkFactory.Perk("None");
      public static final PerkFactory.Perk Agility = new PerkFactory.Perk("Agility");
      public static final PerkFactory.Perk Cooking = new PerkFactory.Perk("Cooking");
      public static final PerkFactory.Perk Melee = new PerkFactory.Perk("Melee");
      public static final PerkFactory.Perk Crafting = new PerkFactory.Perk("Crafting");
      public static final PerkFactory.Perk Fitness = new PerkFactory.Perk("Fitness");
      public static final PerkFactory.Perk Strength = new PerkFactory.Perk("Strength");
      public static final PerkFactory.Perk Blunt = new PerkFactory.Perk("Blunt");
      public static final PerkFactory.Perk Axe = new PerkFactory.Perk("Axe");
      public static final PerkFactory.Perk Sprinting = new PerkFactory.Perk("Sprinting");
      public static final PerkFactory.Perk Lightfoot = new PerkFactory.Perk("Lightfoot");
      public static final PerkFactory.Perk Nimble = new PerkFactory.Perk("Nimble");
      public static final PerkFactory.Perk Sneak = new PerkFactory.Perk("Sneak");
      public static final PerkFactory.Perk Woodwork = new PerkFactory.Perk("Woodwork");
      public static final PerkFactory.Perk Aiming = new PerkFactory.Perk("Aiming");
      public static final PerkFactory.Perk Reloading = new PerkFactory.Perk("Reloading");
      public static final PerkFactory.Perk Farming = new PerkFactory.Perk("Farming");
      public static final PerkFactory.Perk Survivalist = new PerkFactory.Perk("Survivalist");
      public static final PerkFactory.Perk Fishing = new PerkFactory.Perk("Fishing");
      public static final PerkFactory.Perk Trapping = new PerkFactory.Perk("Trapping");
      public static final PerkFactory.Perk Passiv = new PerkFactory.Perk("Passiv");
      public static final PerkFactory.Perk Firearm = new PerkFactory.Perk("Firearm");
      public static final PerkFactory.Perk PlantScavenging = new PerkFactory.Perk("PlantScavenging");
      public static final PerkFactory.Perk Doctor = new PerkFactory.Perk("Doctor");
      public static final PerkFactory.Perk Electricity = new PerkFactory.Perk("Electricity");
      public static final PerkFactory.Perk Blacksmith = new PerkFactory.Perk("Blacksmith");
      public static final PerkFactory.Perk MetalWelding = new PerkFactory.Perk("MetalWelding");
      public static final PerkFactory.Perk Melting = new PerkFactory.Perk("Melting");
      public static final PerkFactory.Perk Mechanics = new PerkFactory.Perk("Mechanics");
      public static final PerkFactory.Perk Spear = new PerkFactory.Perk("Spear");
      public static final PerkFactory.Perk Maintenance = new PerkFactory.Perk("Maintenance");
      public static final PerkFactory.Perk SmallBlade = new PerkFactory.Perk("SmallBlade");
      public static final PerkFactory.Perk LongBlade = new PerkFactory.Perk("LongBlade");
      public static final PerkFactory.Perk SmallBlunt = new PerkFactory.Perk("SmallBlunt");
      public static final PerkFactory.Perk Combat = new PerkFactory.Perk("Combat");
      public static final PerkFactory.Perk Tailoring = new PerkFactory.Perk("Tailoring");
      public static final PerkFactory.Perk MAX = new PerkFactory.Perk("MAX");

      public static int getMaxIndex() {
         return MAX.index();
      }

      public static PerkFactory.Perk fromIndex(int var0) {
         return var0 >= 0 && var0 <= PerkFactory.NextPerkID ? PerkFactory.PerkByIndex[var0] : null;
      }

      public static PerkFactory.Perk FromString(String var0) {
         return (PerkFactory.Perk)PerkFactory.PerkById.getOrDefault(var0, MAX);
      }
   }
}

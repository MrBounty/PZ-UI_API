package zombie.characters;

import java.util.ArrayList;
import java.util.Arrays;
import se.krka.kahlua.j2se.KahluaTableImpl;
import se.krka.kahlua.vm.KahluaTableIterator;
import zombie.Lua.LuaManager;
import zombie.core.ImmutableColor;
import zombie.core.skinnedmodel.population.BeardStyle;
import zombie.core.skinnedmodel.population.HairStyle;
import zombie.core.skinnedmodel.population.OutfitRNG;
import zombie.iso.IsoWorld;
import zombie.util.StringUtils;
import zombie.util.Type;

public final class HairOutfitDefinitions {
   public static final HairOutfitDefinitions instance = new HairOutfitDefinitions();
   public boolean m_dirty = true;
   public String hairStyle;
   public int minWorldAge;
   public final ArrayList m_haircutDefinition = new ArrayList();
   public final ArrayList m_outfitDefinition = new ArrayList();
   private final ThreadLocal m_tempHairStyles = ThreadLocal.withInitial(ArrayList::new);

   public void checkDirty() {
      if (this.m_dirty) {
         this.m_dirty = false;
         this.init();
      }

   }

   private void init() {
      this.m_haircutDefinition.clear();
      this.m_outfitDefinition.clear();
      KahluaTableImpl var1 = (KahluaTableImpl)LuaManager.env.rawget("HairOutfitDefinitions");
      if (var1 != null) {
         KahluaTableImpl var2 = (KahluaTableImpl)Type.tryCastTo(var1.rawget("haircutDefinition"), KahluaTableImpl.class);
         if (var2 != null) {
            KahluaTableIterator var3 = var2.iterator();

            KahluaTableImpl var4;
            while(var3.advance()) {
               var4 = (KahluaTableImpl)Type.tryCastTo(var3.getValue(), KahluaTableImpl.class);
               if (var4 != null) {
                  HairOutfitDefinitions.HaircutDefinition var5 = new HairOutfitDefinitions.HaircutDefinition(var4.rawgetStr("name"), var4.rawgetInt("minWorldAge"), new ArrayList(Arrays.asList(var4.rawgetStr("onlyFor").split(","))));
                  this.m_haircutDefinition.add(var5);
               }
            }

            var4 = (KahluaTableImpl)Type.tryCastTo(var1.rawget("haircutOutfitDefinition"), KahluaTableImpl.class);
            if (var4 != null) {
               var3 = var4.iterator();

               while(var3.advance()) {
                  KahluaTableImpl var7 = (KahluaTableImpl)Type.tryCastTo(var3.getValue(), KahluaTableImpl.class);
                  if (var7 != null) {
                     HairOutfitDefinitions.HaircutOutfitDefinition var6 = new HairOutfitDefinitions.HaircutOutfitDefinition(var7.rawgetStr("outfit"), initStringChance(var7.rawgetStr("haircut")), initStringChance(var7.rawgetStr("beard")), initStringChance(var7.rawgetStr("haircutColor")));
                     this.m_outfitDefinition.add(var6);
                  }
               }

            }
         }
      }
   }

   public boolean isHaircutValid(String var1, String var2) {
      instance.checkDirty();
      if (StringUtils.isNullOrEmpty(var1)) {
         return true;
      } else {
         for(int var3 = 0; var3 < instance.m_haircutDefinition.size(); ++var3) {
            HairOutfitDefinitions.HaircutDefinition var4 = (HairOutfitDefinitions.HaircutDefinition)instance.m_haircutDefinition.get(var3);
            if (var4.hairStyle.equals(var2)) {
               if (!var4.onlyFor.contains(var1)) {
                  return false;
               }

               if (IsoWorld.instance.getWorldAgeDays() < (float)var4.minWorldAge) {
                  return false;
               }
            }
         }

         return true;
      }
   }

   public void getValidHairStylesForOutfit(String var1, ArrayList var2, ArrayList var3) {
      var3.clear();

      for(int var4 = 0; var4 < var2.size(); ++var4) {
         HairStyle var5 = (HairStyle)var2.get(var4);
         if (!var5.isNoChoose() && this.isHaircutValid(var1, var5.name)) {
            var3.add(var5);
         }
      }

   }

   public String getRandomHaircut(String var1, ArrayList var2) {
      ArrayList var3 = (ArrayList)this.m_tempHairStyles.get();
      this.getValidHairStylesForOutfit(var1, var2, var3);
      if (var3.isEmpty()) {
         return "";
      } else {
         String var4 = ((HairStyle)OutfitRNG.pickRandom(var3)).name;
         boolean var5 = false;

         for(int var6 = 0; var6 < instance.m_outfitDefinition.size() && !var5; ++var6) {
            HairOutfitDefinitions.HaircutOutfitDefinition var7 = (HairOutfitDefinitions.HaircutOutfitDefinition)instance.m_outfitDefinition.get(var6);
            if (var7.outfit.equals(var1) && var7.haircutChance != null) {
               float var8 = OutfitRNG.Next(0.0F, 100.0F);
               float var9 = 0.0F;

               for(int var10 = 0; var10 < var7.haircutChance.size(); ++var10) {
                  HairOutfitDefinitions.StringChance var11 = (HairOutfitDefinitions.StringChance)var7.haircutChance.get(var10);
                  var9 += var11.chance;
                  if (var8 < var9) {
                     var4 = var11.str;
                     if ("null".equalsIgnoreCase(var11.str)) {
                        var4 = "";
                     }

                     if ("random".equalsIgnoreCase(var11.str)) {
                        var4 = ((HairStyle)OutfitRNG.pickRandom(var3)).name;
                     }

                     var5 = true;
                     break;
                  }
               }
            }
         }

         return var4;
      }
   }

   public ImmutableColor getRandomHaircutColor(String var1) {
      ImmutableColor var2 = (ImmutableColor)SurvivorDesc.HairCommonColors.get(OutfitRNG.Next(SurvivorDesc.HairCommonColors.size()));
      String var3 = null;
      boolean var4 = false;

      for(int var5 = 0; var5 < instance.m_outfitDefinition.size() && !var4; ++var5) {
         HairOutfitDefinitions.HaircutOutfitDefinition var6 = (HairOutfitDefinitions.HaircutOutfitDefinition)instance.m_outfitDefinition.get(var5);
         if (var6.outfit.equals(var1) && var6.haircutColor != null) {
            float var7 = OutfitRNG.Next(0.0F, 100.0F);
            float var8 = 0.0F;

            for(int var9 = 0; var9 < var6.haircutColor.size(); ++var9) {
               HairOutfitDefinitions.StringChance var10 = (HairOutfitDefinitions.StringChance)var6.haircutColor.get(var9);
               var8 += var10.chance;
               if (var7 < var8) {
                  var3 = var10.str;
                  if ("random".equalsIgnoreCase(var10.str)) {
                     var2 = (ImmutableColor)SurvivorDesc.HairCommonColors.get(OutfitRNG.Next(SurvivorDesc.HairCommonColors.size()));
                     var3 = null;
                  }

                  var4 = true;
                  break;
               }
            }
         }
      }

      if (!StringUtils.isNullOrEmpty(var3)) {
         String[] var11 = var3.split(",");
         var2 = new ImmutableColor(Float.parseFloat(var11[0]), Float.parseFloat(var11[1]), Float.parseFloat(var11[2]));
      }

      return var2;
   }

   public String getRandomBeard(String var1, ArrayList var2) {
      String var3 = ((BeardStyle)OutfitRNG.pickRandom(var2)).name;
      boolean var4 = false;

      for(int var5 = 0; var5 < instance.m_outfitDefinition.size() && !var4; ++var5) {
         HairOutfitDefinitions.HaircutOutfitDefinition var6 = (HairOutfitDefinitions.HaircutOutfitDefinition)instance.m_outfitDefinition.get(var5);
         if (var6.outfit.equals(var1) && var6.beardChance != null) {
            float var7 = OutfitRNG.Next(0.0F, 100.0F);
            float var8 = 0.0F;

            for(int var9 = 0; var9 < var6.beardChance.size(); ++var9) {
               HairOutfitDefinitions.StringChance var10 = (HairOutfitDefinitions.StringChance)var6.beardChance.get(var9);
               var8 += var10.chance;
               if (var7 < var8) {
                  var3 = var10.str;
                  if ("null".equalsIgnoreCase(var10.str)) {
                     var3 = "";
                  }

                  if ("random".equalsIgnoreCase(var10.str)) {
                     var3 = ((BeardStyle)OutfitRNG.pickRandom(var2)).name;
                  }

                  var4 = true;
                  break;
               }
            }
         }
      }

      return var3;
   }

   private static ArrayList initStringChance(String var0) {
      if (StringUtils.isNullOrWhitespace(var0)) {
         return null;
      } else {
         ArrayList var1 = new ArrayList();
         String[] var2 = var0.split(";");
         int var3 = 0;
         String[] var4 = var2;
         int var5 = var2.length;

         for(int var6 = 0; var6 < var5; ++var6) {
            String var7 = var4[var6];
            String[] var8 = var7.split(":");
            HairOutfitDefinitions.StringChance var9 = new HairOutfitDefinitions.StringChance();
            var9.str = var8[0];
            var9.chance = Float.parseFloat(var8[1]);
            var3 = (int)((float)var3 + var9.chance);
            var1.add(var9);
         }

         if (var3 < 100) {
            HairOutfitDefinitions.StringChance var10 = new HairOutfitDefinitions.StringChance();
            var10.str = "random";
            var10.chance = (float)(100 - var3);
            var1.add(var10);
         }

         return var1;
      }
   }

   public static final class HaircutDefinition {
      public String hairStyle;
      public int minWorldAge;
      public ArrayList onlyFor;

      public HaircutDefinition(String var1, int var2, ArrayList var3) {
         this.hairStyle = var1;
         this.minWorldAge = var2;
         this.onlyFor = var3;
      }
   }

   public static final class HaircutOutfitDefinition {
      public String outfit;
      public ArrayList haircutChance;
      public ArrayList beardChance;
      public ArrayList haircutColor;

      public HaircutOutfitDefinition(String var1, ArrayList var2, ArrayList var3, ArrayList var4) {
         this.outfit = var1;
         this.haircutChance = var2;
         this.beardChance = var3;
         this.haircutColor = var4;
      }
   }

   private static final class StringChance {
      String str;
      float chance;
   }
}

package zombie.characters;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import se.krka.kahlua.j2se.KahluaTableImpl;
import se.krka.kahlua.vm.KahluaTable;
import se.krka.kahlua.vm.KahluaTableIterator;
import zombie.PersistentOutfits;
import zombie.Lua.LuaManager;
import zombie.characters.AttachedItems.AttachedWeaponDefinitions;
import zombie.core.Rand;
import zombie.core.skinnedmodel.ModelManager;
import zombie.core.skinnedmodel.population.Outfit;
import zombie.core.skinnedmodel.population.OutfitManager;
import zombie.core.skinnedmodel.population.OutfitRNG;
import zombie.iso.IsoGridSquare;
import zombie.iso.IsoMetaGrid;
import zombie.iso.IsoWorld;
import zombie.network.GameServer;
import zombie.util.StringUtils;
import zombie.util.Type;
import zombie.util.list.PZArrayUtil;

public final class ZombiesZoneDefinition {
   private static final ArrayList s_zoneList = new ArrayList();
   private static final HashMap s_zoneMap = new HashMap();
   public static boolean bDirty = true;
   private static final ZombiesZoneDefinition.PickDefinition pickDef = new ZombiesZoneDefinition.PickDefinition();
   private static final HashMap s_customOutfitMap = new HashMap();

   private static void checkDirty() {
      if (bDirty) {
         bDirty = false;
         init();
      }

   }

   private static void init() {
      s_zoneList.clear();
      s_zoneMap.clear();
      KahluaTableImpl var0 = (KahluaTableImpl)Type.tryCastTo(LuaManager.env.rawget("ZombiesZoneDefinition"), KahluaTableImpl.class);
      if (var0 != null) {
         KahluaTableIterator var1 = var0.iterator();

         while(var1.advance()) {
            KahluaTableImpl var2 = (KahluaTableImpl)Type.tryCastTo(var1.getValue(), KahluaTableImpl.class);
            if (var2 != null) {
               ZombiesZoneDefinition.ZZDZone var3 = initZone(var1.getKey().toString(), var2);
               if (var3 != null) {
                  s_zoneList.add(var3);
                  s_zoneMap.put(var3.name, var3);
               }
            }
         }

      }
   }

   private static ZombiesZoneDefinition.ZZDZone initZone(String var0, KahluaTableImpl var1) {
      ZombiesZoneDefinition.ZZDZone var2 = new ZombiesZoneDefinition.ZZDZone();
      var2.name = var0;
      var2.femaleChance = var1.rawgetInt("femaleChance");
      var2.maleChance = var1.rawgetInt("maleChance");
      var2.chanceToSpawn = var1.rawgetInt("chanceToSpawn");
      var2.toSpawn = var1.rawgetInt("toSpawn");
      KahluaTableIterator var3 = var1.iterator();

      while(var3.advance()) {
         KahluaTableImpl var4 = (KahluaTableImpl)Type.tryCastTo(var3.getValue(), KahluaTableImpl.class);
         if (var4 != null) {
            ZombiesZoneDefinition.ZZDOutfit var5 = initOutfit(var4);
            if (var5 != null) {
               var5.customName = "ZZD." + var2.name + "." + var5.name;
               var2.outfits.add(var5);
            }
         }
      }

      return var2;
   }

   private static ZombiesZoneDefinition.ZZDOutfit initOutfit(KahluaTableImpl var0) {
      ZombiesZoneDefinition.ZZDOutfit var1 = new ZombiesZoneDefinition.ZZDOutfit();
      var1.name = var0.rawgetStr("name");
      var1.chance = var0.rawgetFloat("chance");
      var1.gender = var0.rawgetStr("gender");
      var1.toSpawn = var0.rawgetInt("toSpawn");
      var1.mandatory = var0.rawgetStr("mandatory");
      var1.room = var0.rawgetStr("room");
      var1.femaleHairStyles = initStringChance(var0.rawgetStr("femaleHairStyles"));
      var1.maleHairStyles = initStringChance(var0.rawgetStr("maleHairStyles"));
      var1.beardStyles = initStringChance(var0.rawgetStr("beardStyles"));
      return var1;
   }

   private static ArrayList initStringChance(String var0) {
      if (StringUtils.isNullOrWhitespace(var0)) {
         return null;
      } else {
         ArrayList var1 = new ArrayList();
         String[] var2 = var0.split(";");
         String[] var3 = var2;
         int var4 = var2.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            String var6 = var3[var5];
            String[] var7 = var6.split(":");
            ZombiesZoneDefinition.StringChance var8 = new ZombiesZoneDefinition.StringChance();
            var8.str = var7[0];
            var8.chance = Float.parseFloat(var7[1]);
            var1.add(var8);
         }

         return var1;
      }
   }

   public static void dressInRandomOutfit(IsoZombie var0) {
      if (!var0.isSkeleton()) {
         IsoGridSquare var1 = var0.getCurrentSquare();
         if (var1 != null) {
            ZombiesZoneDefinition.PickDefinition var2 = pickDefinition(var1.x, var1.y, var1.z, var0.isFemale());
            if (var2 == null) {
               String var3 = var1.getRoom() == null ? null : var1.getRoom().getName();
               Outfit var4 = getRandomDefaultOutfit(var0.isFemale(), var3);
               var0.dressInPersistentOutfit(var4.m_Name);
               UnderwearDefinition.addRandomUnderwear(var0);
            } else {
               applyDefinition(var0, var2.zone, var2.table, var2.bFemale);
               UnderwearDefinition.addRandomUnderwear(var0);
            }
         }
      }
   }

   public static IsoMetaGrid.Zone getDefinitionZoneAt(int var0, int var1, int var2) {
      ArrayList var3 = IsoWorld.instance.MetaGrid.getZonesAt(var0, var1, var2);

      for(int var4 = var3.size() - 1; var4 >= 0; --var4) {
         IsoMetaGrid.Zone var5 = (IsoMetaGrid.Zone)var3.get(var4);
         if ("ZombiesType".equalsIgnoreCase(var5.type) || s_zoneMap.containsKey(var5.type)) {
            return var5;
         }
      }

      return null;
   }

   public static ZombiesZoneDefinition.PickDefinition pickDefinition(int var0, int var1, int var2, boolean var3) {
      IsoGridSquare var4 = IsoWorld.instance.CurrentCell.getGridSquare(var0, var1, var2);
      if (var4 == null) {
         return null;
      } else {
         String var5 = var4.getRoom() == null ? null : var4.getRoom().getName();
         checkDirty();
         IsoMetaGrid.Zone var6 = getDefinitionZoneAt(var0, var1, var2);
         if (var6 == null) {
            return null;
         } else if (var6.spawnSpecialZombies == Boolean.FALSE) {
            return null;
         } else {
            String var7 = StringUtils.isNullOrEmpty(var6.name) ? var6.type : var6.name;
            ZombiesZoneDefinition.ZZDZone var8 = (ZombiesZoneDefinition.ZZDZone)s_zoneMap.get(var7);
            if (var8 == null) {
               return null;
            } else {
               if (var8.chanceToSpawn != -1) {
                  int var9 = var8.chanceToSpawn;
                  int var10 = var8.toSpawn;
                  ArrayList var11 = (ArrayList)IsoWorld.instance.getSpawnedZombieZone().get(var6.getName());
                  if (var11 == null) {
                     var11 = new ArrayList();
                     IsoWorld.instance.getSpawnedZombieZone().put(var6.getName(), var11);
                  }

                  if (var11.contains(var6.id)) {
                     var6.spawnSpecialZombies = true;
                  }

                  if (var10 == -1 || var6.spawnSpecialZombies == null && var11.size() < var10) {
                     if (Rand.Next(100) < var9) {
                        var6.spawnSpecialZombies = true;
                        var11.add(var6.id);
                     } else {
                        var6.spawnSpecialZombies = false;
                        var6 = null;
                     }
                  }
               }

               if (var6 == null) {
                  return null;
               } else {
                  ArrayList var20 = new ArrayList();
                  ArrayList var21 = new ArrayList();
                  int var22 = var8.maleChance;
                  int var12 = var8.femaleChance;
                  if (var22 > 0 && Rand.Next(100) < var22) {
                     var3 = false;
                  }

                  if (var12 > 0 && Rand.Next(100) < var12) {
                     var3 = true;
                  }

                  for(int var13 = 0; var13 < var8.outfits.size(); ++var13) {
                     ZombiesZoneDefinition.ZZDOutfit var14 = (ZombiesZoneDefinition.ZZDOutfit)var8.outfits.get(var13);
                     String var15 = var14.gender;
                     String var16 = var14.room;
                     if ((var16 == null || var5 != null && var16.contains(var5)) && (!"male".equalsIgnoreCase(var15) || !var3) && (!"female".equalsIgnoreCase(var15) || var3)) {
                        String var17 = var14.name;
                        boolean var18 = Boolean.parseBoolean(var14.mandatory);
                        if (var18) {
                           Integer var19 = (Integer)var6.spawnedZombies.get(var17);
                           if (var19 == null) {
                              var19 = 0;
                           }

                           if (var19 < var14.toSpawn) {
                              var20.add(var14);
                           }
                        } else {
                           var21.add(var14);
                        }
                     }
                  }

                  ZombiesZoneDefinition.ZZDOutfit var23;
                  if (!var20.isEmpty()) {
                     var23 = (ZombiesZoneDefinition.ZZDOutfit)PZArrayUtil.pickRandom((List)var20);
                  } else {
                     var23 = getRandomOutfitInSetList(var21, true);
                  }

                  if (var23 == null) {
                     return null;
                  } else {
                     pickDef.table = var23;
                     pickDef.bFemale = var3;
                     pickDef.zone = var6;
                     return pickDef;
                  }
               }
            }
         }
      }
   }

   public static void applyDefinition(IsoZombie var0, IsoMetaGrid.Zone var1, ZombiesZoneDefinition.ZZDOutfit var2, boolean var3) {
      var0.setFemaleEtc(var3);
      Outfit var4 = null;
      if (!var3) {
         var4 = OutfitManager.instance.FindMaleOutfit(var2.name);
      } else {
         var4 = OutfitManager.instance.FindFemaleOutfit(var2.name);
      }

      String var5 = var2.customName;
      if (var4 == null) {
         var4 = OutfitManager.instance.GetRandomOutfit(var3);
         var5 = var4.m_Name;
      } else if (var1 != null) {
         Integer var6 = (Integer)var1.spawnedZombies.get(var4.m_Name);
         if (var6 == null) {
            var6 = 1;
         }

         var1.spawnedZombies.put(var4.m_Name, var6 + 1);
      }

      if (var4 != null) {
         var0.dressInPersistentOutfit(var4.m_Name);
      }

      ModelManager.instance.ResetNextFrame(var0);
      var0.advancedAnimator.OnAnimDataChanged(false);
   }

   public static Outfit getRandomDefaultOutfit(boolean var0, String var1) {
      ArrayList var2 = new ArrayList();
      KahluaTable var4 = (KahluaTable)LuaManager.env.rawget("ZombiesZoneDefinition");
      ZombiesZoneDefinition.ZZDZone var5 = (ZombiesZoneDefinition.ZZDZone)s_zoneMap.get("Default");

      ZombiesZoneDefinition.ZZDOutfit var3;
      for(int var6 = 0; var6 < var5.outfits.size(); ++var6) {
         var3 = (ZombiesZoneDefinition.ZZDOutfit)var5.outfits.get(var6);
         String var7 = var3.gender;
         String var8 = var3.room;
         if ((var8 == null || var1 != null && var8.contains(var1)) && (var7 == null || "male".equalsIgnoreCase(var7) && !var0 || "female".equalsIgnoreCase(var7) && var0)) {
            var2.add(var3);
         }
      }

      var3 = getRandomOutfitInSetList(var2, false);
      Outfit var9 = null;
      if (var3 != null) {
         if (var0) {
            var9 = OutfitManager.instance.FindFemaleOutfit(var3.name);
         } else {
            var9 = OutfitManager.instance.FindMaleOutfit(var3.name);
         }
      }

      if (var9 == null) {
         var9 = OutfitManager.instance.GetRandomOutfit(var0);
      }

      return var9;
   }

   public static ZombiesZoneDefinition.ZZDOutfit getRandomOutfitInSetList(ArrayList var0, boolean var1) {
      float var2 = 0.0F;

      for(int var3 = 0; var3 < var0.size(); ++var3) {
         ZombiesZoneDefinition.ZZDOutfit var4 = (ZombiesZoneDefinition.ZZDOutfit)var0.get(var3);
         var2 += var4.chance;
      }

      float var7 = Rand.Next(0.0F, 100.0F);
      if (!var1 || var2 > 100.0F) {
         var7 = Rand.Next(0.0F, var2);
      }

      float var8 = 0.0F;

      for(int var5 = 0; var5 < var0.size(); ++var5) {
         ZombiesZoneDefinition.ZZDOutfit var6 = (ZombiesZoneDefinition.ZZDOutfit)var0.get(var5);
         var8 += var6.chance;
         if (var7 < var8) {
            return var6;
         }
      }

      return null;
   }

   private static String getRandomHairOrBeard(ArrayList var0) {
      float var1 = OutfitRNG.Next(0.0F, 100.0F);
      float var2 = 0.0F;

      for(int var3 = 0; var3 < var0.size(); ++var3) {
         ZombiesZoneDefinition.StringChance var4 = (ZombiesZoneDefinition.StringChance)var0.get(var3);
         var2 += var4.chance;
         if (var1 < var2) {
            if ("null".equalsIgnoreCase(var4.str)) {
               return "";
            }

            return var4.str;
         }
      }

      return null;
   }

   public static void registerCustomOutfits() {
      checkDirty();
      s_customOutfitMap.clear();
      Iterator var0 = s_zoneList.iterator();

      while(var0.hasNext()) {
         ZombiesZoneDefinition.ZZDZone var1 = (ZombiesZoneDefinition.ZZDZone)var0.next();
         Iterator var2 = var1.outfits.iterator();

         while(var2.hasNext()) {
            ZombiesZoneDefinition.ZZDOutfit var3 = (ZombiesZoneDefinition.ZZDOutfit)var2.next();
            PersistentOutfits.instance.registerOutfitter(var3.customName, true, ZombiesZoneDefinition::ApplyCustomOutfit);
            s_customOutfitMap.put(var3.customName, var3);
         }
      }

   }

   private static void ApplyCustomOutfit(int var0, String var1, IsoGameCharacter var2) {
      ZombiesZoneDefinition.ZZDOutfit var3 = (ZombiesZoneDefinition.ZZDOutfit)s_customOutfitMap.get(var1);
      boolean var4 = (var0 & Integer.MIN_VALUE) != 0;
      IsoZombie var5 = (IsoZombie)Type.tryCastTo(var2, IsoZombie.class);
      if (var5 != null) {
         var5.setFemaleEtc(var4);
      }

      var2.dressInNamedOutfit(var3.name);
      if (var5 == null) {
         PersistentOutfits.instance.removeFallenHat(var0, var2);
      } else {
         AttachedWeaponDefinitions.instance.addRandomAttachedWeapon(var5);
         var5.addRandomBloodDirtHolesEtc();
         boolean var6 = var2.isFemale();
         if (var6 && var3.femaleHairStyles != null) {
            var5.getHumanVisual().setHairModel(getRandomHairOrBeard(var3.femaleHairStyles));
         }

         if (!var6 && var3.maleHairStyles != null) {
            var5.getHumanVisual().setHairModel(getRandomHairOrBeard(var3.maleHairStyles));
         }

         if (!var6 && var3.beardStyles != null) {
            var5.getHumanVisual().setBeardModel(getRandomHairOrBeard(var3.beardStyles));
         }

         PersistentOutfits.instance.removeFallenHat(var0, var2);
      }
   }

   public static int pickPersistentOutfit(IsoGridSquare var0) {
      if (!GameServer.bServer) {
         return 0;
      } else {
         boolean var2 = Rand.Next(2) == 0;
         ZombiesZoneDefinition.PickDefinition var3 = pickDefinition(var0.x, var0.y, var0.z, var2);
         Outfit var1;
         String var4;
         if (var3 == null) {
            var4 = var0.getRoom() == null ? null : var0.getRoom().getName();
            var1 = getRandomDefaultOutfit(var2, var4);
         } else {
            var2 = var3.bFemale;
            var4 = var3.table.name;
            if (var2) {
               var1 = OutfitManager.instance.FindFemaleOutfit(var4);
            } else {
               var1 = OutfitManager.instance.FindMaleOutfit(var4);
            }
         }

         if (var1 == null) {
            boolean var6 = true;
         } else {
            int var7 = PersistentOutfits.instance.pickOutfit(var1.m_Name, var2);
            if (var7 != 0) {
               return var7;
            }

            boolean var5 = true;
         }

         return 0;
      }
   }

   private static final class ZZDZone {
      String name;
      int femaleChance;
      int maleChance;
      int chanceToSpawn;
      int toSpawn;
      final ArrayList outfits = new ArrayList();
   }

   private static final class ZZDOutfit {
      String name;
      String customName;
      float chance;
      int toSpawn;
      String gender;
      String mandatory;
      String room;
      ArrayList femaleHairStyles;
      ArrayList maleHairStyles;
      ArrayList beardStyles;
   }

   private static final class StringChance {
      String str;
      float chance;
   }

   public static final class PickDefinition {
      IsoMetaGrid.Zone zone;
      ZombiesZoneDefinition.ZZDOutfit table;
      boolean bFemale;
   }
}

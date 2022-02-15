package zombie.characters.AttachedItems;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map.Entry;
import se.krka.kahlua.j2se.KahluaTableImpl;
import se.krka.kahlua.vm.KahluaTable;
import se.krka.kahlua.vm.KahluaTableIterator;
import zombie.Lua.LuaManager;
import zombie.characterTextures.BloodBodyPartType;
import zombie.characters.IsoZombie;
import zombie.core.Core;
import zombie.core.skinnedmodel.population.Outfit;
import zombie.core.skinnedmodel.population.OutfitRNG;
import zombie.core.skinnedmodel.visual.ItemVisual;
import zombie.core.skinnedmodel.visual.ItemVisuals;
import zombie.inventory.InventoryItem;
import zombie.inventory.InventoryItemFactory;
import zombie.inventory.types.HandWeapon;
import zombie.iso.IsoWorld;
import zombie.scripting.ScriptManager;
import zombie.scripting.objects.Item;
import zombie.util.StringUtils;

public final class AttachedWeaponDefinitions {
   public static final AttachedWeaponDefinitions instance = new AttachedWeaponDefinitions();
   public boolean m_dirty = true;
   public int m_chanceOfAttachedWeapon;
   public final ArrayList m_definitions = new ArrayList();
   public final ArrayList m_outfitDefinitions = new ArrayList();

   public void checkDirty() {
      if (this.m_dirty) {
         this.m_dirty = false;
         this.init();
      }

   }

   public void addRandomAttachedWeapon(IsoZombie var1) {
      if (!"Tutorial".equals(Core.getInstance().getGameMode())) {
         this.checkDirty();
         if (!this.m_definitions.isEmpty()) {
            ArrayList var2 = AttachedWeaponDefinitions.L_addRandomAttachedWeapon.definitions;
            var2.clear();
            int var3 = 1;
            AttachedWeaponCustomOutfit var4 = null;
            Outfit var5 = var1.getHumanVisual().getOutfit();
            if (var5 != null) {
               for(int var6 = 0; var6 < this.m_outfitDefinitions.size(); ++var6) {
                  var4 = (AttachedWeaponCustomOutfit)this.m_outfitDefinitions.get(var6);
                  if (var4.outfit.equals(var5.m_Name) && OutfitRNG.Next(100) < var4.chance) {
                     var2.addAll(var4.weapons);
                     var3 = var4.maxitem > -1 ? var4.maxitem : 1;
                     break;
                  }

                  var4 = null;
               }
            }

            if (var2.isEmpty()) {
               if (OutfitRNG.Next(100) > this.m_chanceOfAttachedWeapon) {
                  return;
               }

               var2.addAll(this.m_definitions);
            }

            do {
               if (var3 <= 0) {
                  return;
               }

               AttachedWeaponDefinition var7 = this.pickRandomInList(var2, var1);
               if (var7 == null) {
                  return;
               }

               var2.remove(var7);
               --var3;
               this.addAttachedWeapon(var7, var1);
            } while(var4 == null || OutfitRNG.Next(100) < var4.chance);

         }
      }
   }

   private void addAttachedWeapon(AttachedWeaponDefinition var1, IsoZombie var2) {
      String var3 = (String)OutfitRNG.pickRandom(var1.weapons);
      InventoryItem var4 = InventoryItemFactory.CreateItem(var3);
      if (var4 != null) {
         if (var4 instanceof HandWeapon) {
            ((HandWeapon)var4).randomizeBullets();
         }

         var4.setCondition(OutfitRNG.Next(Math.max(2, var4.getConditionMax() - 5), var4.getConditionMax()));
         var2.setAttachedItem((String)OutfitRNG.pickRandom(var1.weaponLocation), var4);
         if (var1.ensureItem != null && !this.outfitHasItem(var2, var1.ensureItem)) {
            Item var5 = ScriptManager.instance.FindItem(var1.ensureItem);
            if (var5 != null && var5.getClothingItemAsset() != null) {
               var2.getHumanVisual().addClothingItem(var2.getItemVisuals(), var5);
            } else {
               var2.addItemToSpawnAtDeath(InventoryItemFactory.CreateItem(var1.ensureItem));
            }
         }

         if (!var1.bloodLocations.isEmpty()) {
            for(int var7 = 0; var7 < var1.bloodLocations.size(); ++var7) {
               BloodBodyPartType var6 = (BloodBodyPartType)var1.bloodLocations.get(var7);
               var2.addBlood(var6, true, true, true);
               var2.addBlood(var6, true, true, true);
               var2.addBlood(var6, true, true, true);
               if (var1.addHoles) {
                  var2.addHole(var6);
                  var2.addHole(var6);
                  var2.addHole(var6);
                  var2.addHole(var6);
               }
            }
         }

      }
   }

   private AttachedWeaponDefinition pickRandomInList(ArrayList var1, IsoZombie var2) {
      AttachedWeaponDefinition var3 = null;
      int var4 = 0;
      ArrayList var5 = AttachedWeaponDefinitions.L_addRandomAttachedWeapon.possibilities;
      var5.clear();

      int var6;
      for(var6 = 0; var6 < var1.size(); ++var6) {
         AttachedWeaponDefinition var7 = (AttachedWeaponDefinition)var1.get(var6);
         if (var7.daySurvived > 0) {
            if (IsoWorld.instance.getWorldAgeDays() > (float)var7.daySurvived) {
               var4 += var7.chance;
               var5.add(var7);
            }
         } else if (!var7.outfit.isEmpty()) {
            if (var2.getHumanVisual().getOutfit() != null && var7.outfit.contains(var2.getHumanVisual().getOutfit().m_Name)) {
               var4 += var7.chance;
               var5.add(var7);
            }
         } else {
            var4 += var7.chance;
            var5.add(var7);
         }
      }

      var6 = OutfitRNG.Next(var4);
      int var10 = 0;

      for(int var8 = 0; var8 < var5.size(); ++var8) {
         AttachedWeaponDefinition var9 = (AttachedWeaponDefinition)var5.get(var8);
         var10 += var9.chance;
         if (var6 < var10) {
            var3 = var9;
            break;
         }
      }

      return var3;
   }

   public boolean outfitHasItem(IsoZombie var1, String var2) {
      assert var2.contains(".");

      ItemVisuals var3 = var1.getItemVisuals();

      for(int var4 = 0; var4 < var3.size(); ++var4) {
         ItemVisual var5 = (ItemVisual)var3.get(var4);
         if (StringUtils.equals(var5.getItemType(), var2)) {
            return true;
         }

         if ("Base.HolsterSimple".equals(var2) && StringUtils.equals(var5.getItemType(), "Base.HolsterDouble")) {
            return true;
         }

         if ("Base.HolsterDouble".equals(var2) && StringUtils.equals(var5.getItemType(), "Base.HolsterSimple")) {
            return true;
         }
      }

      return false;
   }

   private void init() {
      this.m_definitions.clear();
      this.m_outfitDefinitions.clear();
      KahluaTableImpl var1 = (KahluaTableImpl)LuaManager.env.rawget("AttachedWeaponDefinitions");
      if (var1 != null) {
         this.m_chanceOfAttachedWeapon = var1.rawgetInt("chanceOfAttachedWeapon");
         Iterator var2 = var1.delegate.entrySet().iterator();

         while(true) {
            while(true) {
               Entry var3;
               do {
                  if (!var2.hasNext()) {
                     Collections.sort(this.m_definitions, (var0, var1x) -> {
                        return var0.id.compareTo(var1x.id);
                     });
                     return;
                  }

                  var3 = (Entry)var2.next();
               } while(!(var3.getValue() instanceof KahluaTableImpl));

               KahluaTableImpl var4 = (KahluaTableImpl)var3.getValue();
               if ("attachedWeaponCustomOutfit".equals(var3.getKey())) {
                  KahluaTableImpl var9 = (KahluaTableImpl)var3.getValue();
                  Iterator var6 = var9.delegate.entrySet().iterator();

                  while(var6.hasNext()) {
                     Entry var7 = (Entry)var6.next();
                     AttachedWeaponCustomOutfit var8 = this.initOutfit((String)var7.getKey(), (KahluaTableImpl)var7.getValue());
                     if (var8 != null) {
                        this.m_outfitDefinitions.add(var8);
                     }
                  }
               } else {
                  AttachedWeaponDefinition var5 = this.init((String)var3.getKey(), var4);
                  if (var5 != null) {
                     this.m_definitions.add(var5);
                  }
               }
            }
         }
      }
   }

   private AttachedWeaponCustomOutfit initOutfit(String var1, KahluaTableImpl var2) {
      AttachedWeaponCustomOutfit var3 = new AttachedWeaponCustomOutfit();
      var3.outfit = var1;
      var3.chance = var2.rawgetInt("chance");
      var3.maxitem = var2.rawgetInt("maxitem");
      KahluaTableImpl var4 = (KahluaTableImpl)var2.rawget("weapons");
      Iterator var5 = var4.delegate.entrySet().iterator();

      while(var5.hasNext()) {
         Entry var6 = (Entry)var5.next();
         KahluaTableImpl var7 = (KahluaTableImpl)var6.getValue();
         AttachedWeaponDefinition var8 = this.init(var7.rawgetStr("id"), var7);
         if (var8 != null) {
            var3.weapons.add(var8);
         }
      }

      return var3;
   }

   private AttachedWeaponDefinition init(String var1, KahluaTableImpl var2) {
      AttachedWeaponDefinition var3 = new AttachedWeaponDefinition();
      var3.id = var1;
      var3.chance = var2.rawgetInt("chance");
      this.tableToArrayList(var2, "outfit", var3.outfit);
      this.tableToArrayList(var2, "weaponLocation", var3.weaponLocation);
      KahluaTableImpl var4 = (KahluaTableImpl)var2.rawget("bloodLocations");
      if (var4 != null) {
         KahluaTableIterator var5 = var4.iterator();

         while(var5.advance()) {
            BloodBodyPartType var6 = BloodBodyPartType.FromString(var5.getValue().toString());
            if (var6 != BloodBodyPartType.MAX) {
               var3.bloodLocations.add(var6);
            }
         }
      }

      var3.addHoles = var2.rawgetBool("addHoles");
      var3.daySurvived = var2.rawgetInt("daySurvived");
      var3.ensureItem = var2.rawgetStr("ensureItem");
      this.tableToArrayList(var2, "weapons", var3.weapons);
      Collections.sort(var3.weaponLocation);
      Collections.sort(var3.bloodLocations);
      Collections.sort(var3.weapons);
      return var3;
   }

   private void tableToArrayList(KahluaTable var1, String var2, ArrayList var3) {
      KahluaTableImpl var4 = (KahluaTableImpl)var1.rawget(var2);
      if (var4 != null) {
         int var5 = 1;

         for(int var6 = var4.len(); var5 <= var6; ++var5) {
            Object var7 = var4.rawget(var5);
            if (var7 != null) {
               var3.add(var7.toString());
            }
         }

      }
   }

   private static final class L_addRandomAttachedWeapon {
      static final ArrayList possibilities = new ArrayList();
      static final ArrayList definitions = new ArrayList();
   }
}

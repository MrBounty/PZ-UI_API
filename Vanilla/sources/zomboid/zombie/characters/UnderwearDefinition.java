package zombie.characters;

import java.util.ArrayList;
import se.krka.kahlua.j2se.KahluaTableImpl;
import se.krka.kahlua.vm.KahluaTableIterator;
import zombie.Lua.LuaManager;
import zombie.core.Rand;
import zombie.core.skinnedmodel.population.OutfitRNG;
import zombie.core.skinnedmodel.visual.ItemVisual;
import zombie.scripting.ScriptManager;
import zombie.scripting.objects.Item;
import zombie.util.Type;

public class UnderwearDefinition {
   public static final UnderwearDefinition instance = new UnderwearDefinition();
   public boolean m_dirty = true;
   private static final ArrayList m_outfitDefinition = new ArrayList();
   private static int baseChance = 50;

   public void checkDirty() {
      this.init();
   }

   private void init() {
      m_outfitDefinition.clear();
      KahluaTableImpl var1 = (KahluaTableImpl)LuaManager.env.rawget("UnderwearDefinition");
      if (var1 != null) {
         baseChance = var1.rawgetInt("baseChance");
         KahluaTableIterator var2 = var1.iterator();

         while(true) {
            ArrayList var3;
            KahluaTableImpl var4;
            do {
               if (!var2.advance()) {
                  return;
               }

               var3 = null;
               var4 = (KahluaTableImpl)Type.tryCastTo(var2.getValue(), KahluaTableImpl.class);
            } while(var4 == null);

            KahluaTableImpl var5 = (KahluaTableImpl)Type.tryCastTo(var4.rawget("top"), KahluaTableImpl.class);
            if (var5 != null) {
               var3 = new ArrayList();
               KahluaTableIterator var6 = var5.iterator();

               while(var6.advance()) {
                  KahluaTableImpl var7 = (KahluaTableImpl)Type.tryCastTo(var6.getValue(), KahluaTableImpl.class);
                  if (var7 != null) {
                     var3.add(new UnderwearDefinition.StringChance(var7.rawgetStr("name"), var7.rawgetFloat("chance")));
                  }
               }
            }

            UnderwearDefinition.OutfitUnderwearDefinition var8 = new UnderwearDefinition.OutfitUnderwearDefinition(var3, var4.rawgetStr("bottom"), var4.rawgetInt("chanceToSpawn"), var4.rawgetStr("gender"));
            m_outfitDefinition.add(var8);
         }
      }
   }

   public static void addRandomUnderwear(IsoZombie var0) {
      instance.checkDirty();
      if (Rand.Next(100) <= baseChance) {
         ArrayList var1 = new ArrayList();
         int var2 = 0;

         int var3;
         UnderwearDefinition.OutfitUnderwearDefinition var4;
         for(var3 = 0; var3 < m_outfitDefinition.size(); ++var3) {
            var4 = (UnderwearDefinition.OutfitUnderwearDefinition)m_outfitDefinition.get(var3);
            if (var0.isFemale() && var4.female || !var0.isFemale() && !var4.female) {
               var1.add(var4);
               var2 += var4.chanceToSpawn;
            }
         }

         var3 = OutfitRNG.Next(var2);
         var4 = null;
         int var5 = 0;

         for(int var6 = 0; var6 < var1.size(); ++var6) {
            UnderwearDefinition.OutfitUnderwearDefinition var7 = (UnderwearDefinition.OutfitUnderwearDefinition)var1.get(var6);
            var5 += var7.chanceToSpawn;
            if (var3 < var5) {
               var4 = var7;
               break;
            }
         }

         if (var4 != null) {
            Item var11 = ScriptManager.instance.FindItem(var4.bottom);
            ItemVisual var12 = null;
            if (var11 != null) {
               var12 = var0.getHumanVisual().addClothingItem(var0.getItemVisuals(), var11);
            }

            if (var4.top != null) {
               String var8 = null;
               var3 = OutfitRNG.Next(var4.topTotalChance);
               var5 = 0;

               for(int var9 = 0; var9 < var4.top.size(); ++var9) {
                  UnderwearDefinition.StringChance var10 = (UnderwearDefinition.StringChance)var4.top.get(var9);
                  var5 = (int)((float)var5 + var10.chance);
                  if (var3 < var5) {
                     var8 = var10.str;
                     break;
                  }
               }

               if (var8 != null) {
                  var11 = ScriptManager.instance.FindItem(var8);
                  if (var11 != null) {
                     ItemVisual var13 = var0.getHumanVisual().addClothingItem(var0.getItemVisuals(), var11);
                     if (Rand.Next(100) < 60 && var13 != null && var12 != null) {
                        var13.setTint(var12.getTint());
                     }
                  }
               }
            }
         }

      }
   }

   private static final class StringChance {
      String str;
      float chance;

      public StringChance(String var1, float var2) {
         this.str = var1;
         this.chance = var2;
      }
   }

   public static final class OutfitUnderwearDefinition {
      public ArrayList top;
      public int topTotalChance = 0;
      public String bottom;
      public int chanceToSpawn;
      public boolean female = false;

      public OutfitUnderwearDefinition(ArrayList var1, String var2, int var3, String var4) {
         this.top = var1;
         if (var1 != null) {
            for(int var5 = 0; var5 < var1.size(); ++var5) {
               this.topTotalChance = (int)((float)this.topTotalChance + ((UnderwearDefinition.StringChance)var1.get(var5)).chance);
            }
         }

         this.bottom = var2;
         this.chanceToSpawn = var3;
         if ("female".equals(var4)) {
            this.female = true;
         }

      }
   }
}

package zombie.core.skinnedmodel.population;

import java.util.ArrayList;
import se.krka.kahlua.j2se.KahluaTableImpl;
import se.krka.kahlua.vm.KahluaTable;
import zombie.Lua.LuaManager;
import zombie.util.Type;

public final class DefaultClothing {
   public static final DefaultClothing instance = new DefaultClothing();
   public final DefaultClothing.Clothing Pants = new DefaultClothing.Clothing();
   public final DefaultClothing.Clothing TShirt = new DefaultClothing.Clothing();
   public final DefaultClothing.Clothing TShirtDecal = new DefaultClothing.Clothing();
   public final DefaultClothing.Clothing Vest = new DefaultClothing.Clothing();
   public boolean m_dirty = true;

   private void checkDirty() {
      if (this.m_dirty) {
         this.m_dirty = false;
         this.init();
      }

   }

   private void init() {
      this.Pants.clear();
      this.TShirt.clear();
      this.TShirtDecal.clear();
      this.Vest.clear();
      KahluaTable var1 = (KahluaTable)Type.tryCastTo(LuaManager.env.rawget("DefaultClothing"), KahluaTable.class);
      if (var1 != null) {
         this.initClothing(var1, this.Pants, "Pants");
         this.initClothing(var1, this.TShirt, "TShirt");
         this.initClothing(var1, this.TShirtDecal, "TShirtDecal");
         this.initClothing(var1, this.Vest, "Vest");
      }
   }

   private void initClothing(KahluaTable var1, DefaultClothing.Clothing var2, String var3) {
      KahluaTable var4 = (KahluaTable)Type.tryCastTo(var1.rawget(var3), KahluaTable.class);
      if (var4 != null) {
         this.tableToArrayList(var4, "hue", var2.hue);
         this.tableToArrayList(var4, "texture", var2.texture);
         this.tableToArrayList(var4, "tint", var2.tint);
      }
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

   public String pickPantsHue() {
      this.checkDirty();
      return (String)OutfitRNG.pickRandom(this.Pants.hue);
   }

   public String pickPantsTexture() {
      this.checkDirty();
      return (String)OutfitRNG.pickRandom(this.Pants.texture);
   }

   public String pickPantsTint() {
      this.checkDirty();
      return (String)OutfitRNG.pickRandom(this.Pants.tint);
   }

   public String pickTShirtTexture() {
      this.checkDirty();
      return (String)OutfitRNG.pickRandom(this.TShirt.texture);
   }

   public String pickTShirtTint() {
      this.checkDirty();
      return (String)OutfitRNG.pickRandom(this.TShirt.tint);
   }

   public String pickTShirtDecalTexture() {
      this.checkDirty();
      return (String)OutfitRNG.pickRandom(this.TShirtDecal.texture);
   }

   public String pickTShirtDecalTint() {
      this.checkDirty();
      return (String)OutfitRNG.pickRandom(this.TShirtDecal.tint);
   }

   public String pickVestTexture() {
      this.checkDirty();
      return (String)OutfitRNG.pickRandom(this.Vest.texture);
   }

   public String pickVestTint() {
      this.checkDirty();
      return (String)OutfitRNG.pickRandom(this.Vest.tint);
   }

   private static final class Clothing {
      final ArrayList hue = new ArrayList();
      final ArrayList texture = new ArrayList();
      final ArrayList tint = new ArrayList();

      void clear() {
         this.hue.clear();
         this.texture.clear();
         this.tint.clear();
      }
   }
}

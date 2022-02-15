package zombie.core.skinnedmodel.population;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.xml.bind.annotation.XmlTransient;
import zombie.characters.HairOutfitDefinitions;
import zombie.core.ImmutableColor;
import zombie.core.skinnedmodel.model.CharacterMask;
import zombie.debug.DebugLog;
import zombie.util.list.PZArrayUtil;

public class Outfit implements Cloneable {
   public String m_Name = "Outfit";
   public boolean m_Top = true;
   public boolean m_Pants = true;
   public final ArrayList m_TopTextures = new ArrayList();
   public final ArrayList m_PantsTextures = new ArrayList();
   public final ArrayList m_items = new ArrayList();
   public boolean m_AllowPantsHue = true;
   public boolean m_AllowPantsTint = false;
   public boolean m_AllowTopTint = true;
   public boolean m_AllowTShirtDecal = true;
   @XmlTransient
   public String m_modID;
   @XmlTransient
   public boolean m_Immutable = false;
   @XmlTransient
   public final Outfit.RandomData RandomData = new Outfit.RandomData();

   public void setModID(String var1) {
      this.m_modID = var1;
      Iterator var2 = this.m_items.iterator();

      while(var2.hasNext()) {
         ClothingItemReference var3 = (ClothingItemReference)var2.next();
         var3.setModID(var1);
      }

   }

   public void AddItem(ClothingItemReference var1) {
      this.m_items.add(var1);
   }

   public void Randomize() {
      if (this.m_Immutable) {
         throw new RuntimeException("trying to randomize an immutable Outfit");
      } else {
         for(int var1 = 0; var1 < this.m_items.size(); ++var1) {
            ClothingItemReference var2 = (ClothingItemReference)this.m_items.get(var1);
            var2.randomize();
         }

         this.RandomData.m_hairColor = HairOutfitDefinitions.instance.getRandomHaircutColor(this.m_Name);
         this.RandomData.m_femaleHairName = HairStyles.instance.getRandomFemaleStyle(this.m_Name);
         this.RandomData.m_maleHairName = HairStyles.instance.getRandomMaleStyle(this.m_Name);
         this.RandomData.m_beardName = BeardStyles.instance.getRandomStyle(this.m_Name);
         this.RandomData.m_topTint = OutfitRNG.randomImmutableColor();
         this.RandomData.m_pantsTint = OutfitRNG.randomImmutableColor();
         if (OutfitRNG.Next(4) == 0) {
            this.RandomData.m_pantsHue = (float)OutfitRNG.Next(200) / 100.0F - 1.0F;
         } else {
            this.RandomData.m_pantsHue = 0.0F;
         }

         this.RandomData.m_hasTop = OutfitRNG.Next(16) != 0;
         this.RandomData.m_hasTShirt = OutfitRNG.Next(2) == 0;
         this.RandomData.m_hasTShirtDecal = OutfitRNG.Next(4) == 0;
         if (this.m_Top) {
            this.RandomData.m_hasTop = true;
         }

         this.RandomData.m_topTexture = (String)OutfitRNG.pickRandom(this.m_TopTextures);
         this.RandomData.m_pantsTexture = (String)OutfitRNG.pickRandom(this.m_PantsTextures);
      }
   }

   public void randomizeItem(String var1) {
      ClothingItemReference var2 = (ClothingItemReference)PZArrayUtil.find((List)this.m_items, (var1x) -> {
         return var1x.itemGUID.equals(var1);
      });
      if (var2 != null) {
         var2.randomize();
      } else {
         DebugLog.Clothing.println("Outfit.randomizeItem> Could not find itemGuid: " + var1);
      }

   }

   public CharacterMask GetMask() {
      CharacterMask var1 = new CharacterMask();

      for(int var2 = 0; var2 < this.m_items.size(); ++var2) {
         ClothingItemReference var3 = (ClothingItemReference)this.m_items.get(var2);
         if (var3.RandomData.m_Active) {
            ClothingItem.tryGetCombinedMask(var3, var1);
         }
      }

      return var1;
   }

   public boolean containsItemGuid(String var1) {
      boolean var2 = false;

      for(int var3 = 0; var3 < this.m_items.size(); ++var3) {
         ClothingItemReference var4 = (ClothingItemReference)this.m_items.get(var3);
         if (var4.itemGUID.equals(var1)) {
            var2 = true;
            break;
         }
      }

      return var2;
   }

   public ClothingItemReference findItemByGUID(String var1) {
      for(int var2 = 0; var2 < this.m_items.size(); ++var2) {
         ClothingItemReference var3 = (ClothingItemReference)this.m_items.get(var2);
         if (var3.itemGUID.equals(var1)) {
            return var3;
         }
      }

      return null;
   }

   public Outfit clone() {
      try {
         Outfit var1 = new Outfit();
         var1.m_Name = this.m_Name;
         var1.m_Top = this.m_Top;
         var1.m_Pants = this.m_Pants;
         var1.m_PantsTextures.addAll(this.m_PantsTextures);
         var1.m_TopTextures.addAll(this.m_TopTextures);
         PZArrayUtil.copy(var1.m_items, this.m_items, ClothingItemReference::clone);
         var1.m_AllowPantsHue = this.m_AllowPantsHue;
         var1.m_AllowPantsTint = this.m_AllowPantsTint;
         var1.m_AllowTopTint = this.m_AllowTopTint;
         var1.m_AllowTShirtDecal = this.m_AllowTShirtDecal;
         return var1;
      } catch (CloneNotSupportedException var2) {
         throw new RuntimeException("Outfit clone failed.", var2);
      }
   }

   public ClothingItemReference findHat() {
      Iterator var1 = this.m_items.iterator();

      while(var1.hasNext()) {
         ClothingItemReference var2 = (ClothingItemReference)var1.next();
         if (var2.RandomData.m_Active) {
            ClothingItem var3 = var2.getClothingItem();
            if (var3 != null && var3.isHat()) {
               return var2;
            }
         }
      }

      return null;
   }

   public boolean isEmpty() {
      for(int var1 = 0; var1 < this.m_items.size(); ++var1) {
         ClothingItemReference var2 = (ClothingItemReference)this.m_items.get(var1);
         ClothingItem var3 = OutfitManager.instance.getClothingItem(var2.itemGUID);
         if (var3 != null && var3.isEmpty()) {
            return true;
         }

         for(int var4 = 0; var4 < var2.subItems.size(); ++var4) {
            ClothingItemReference var5 = (ClothingItemReference)var2.subItems.get(var4);
            var3 = OutfitManager.instance.getClothingItem(var5.itemGUID);
            if (var3 != null && var3.isEmpty()) {
               return true;
            }
         }
      }

      return false;
   }

   public void loadItems() {
      for(int var1 = 0; var1 < this.m_items.size(); ++var1) {
         ClothingItemReference var2 = (ClothingItemReference)this.m_items.get(var1);
         OutfitManager.instance.getClothingItem(var2.itemGUID);

         for(int var3 = 0; var3 < var2.subItems.size(); ++var3) {
            ClothingItemReference var4 = (ClothingItemReference)var2.subItems.get(var3);
            OutfitManager.instance.getClothingItem(var4.itemGUID);
         }
      }

   }

   public static class RandomData {
      public ImmutableColor m_hairColor;
      public String m_maleHairName;
      public String m_femaleHairName;
      public String m_beardName;
      public ImmutableColor m_topTint;
      public ImmutableColor m_pantsTint;
      public float m_pantsHue;
      public boolean m_hasTop;
      public boolean m_hasTShirt;
      public boolean m_hasTShirtDecal;
      public String m_topTexture;
      public String m_pantsTexture;
   }
}

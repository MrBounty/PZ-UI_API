package zombie.core.skinnedmodel.population;

import java.util.ArrayList;
import java.util.Iterator;
import javax.xml.bind.annotation.XmlTransient;
import zombie.core.ImmutableColor;
import zombie.util.StringUtils;
import zombie.util.list.PZArrayUtil;

public class ClothingItemReference implements Cloneable {
   public float probability = 1.0F;
   public String itemGUID;
   public ArrayList subItems = new ArrayList();
   public boolean bRandomized = false;
   @XmlTransient
   public boolean m_Immutable = false;
   @XmlTransient
   public final ClothingItemReference.RandomData RandomData = new ClothingItemReference.RandomData();

   public void setModID(String var1) {
      this.itemGUID = var1 + "-" + this.itemGUID;
      Iterator var2 = this.subItems.iterator();

      while(var2.hasNext()) {
         ClothingItemReference var3 = (ClothingItemReference)var2.next();
         var3.setModID(var1);
      }

   }

   public ClothingItem getClothingItem() {
      String var1 = this.itemGUID;
      if (!this.bRandomized) {
         throw new RuntimeException("not randomized yet");
      } else {
         if (this.RandomData.m_PickedItemRef != null) {
            var1 = this.RandomData.m_PickedItemRef.itemGUID;
         }

         return OutfitManager.instance.getClothingItem(var1);
      }
   }

   public void randomize() {
      if (this.m_Immutable) {
         throw new RuntimeException("trying to randomize an immutable ClothingItemReference");
      } else {
         this.RandomData.reset();

         for(int var1 = 0; var1 < this.subItems.size(); ++var1) {
            ClothingItemReference var2 = (ClothingItemReference)this.subItems.get(var1);
            var2.randomize();
         }

         this.RandomData.m_PickedItemRef = this.pickRandomItemInternal();
         this.bRandomized = true;
         ClothingItem var3 = this.getClothingItem();
         if (var3 == null) {
            this.RandomData.m_Active = false;
         } else {
            this.RandomData.m_Active = OutfitRNG.Next(0.0F, 1.0F) <= this.probability;
            if (var3.m_AllowRandomHue) {
               this.RandomData.m_Hue = (float)OutfitRNG.Next(200) / 100.0F - 1.0F;
            }

            if (var3.m_AllowRandomTint) {
               this.RandomData.m_Tint = OutfitRNG.randomImmutableColor();
            } else {
               this.RandomData.m_Tint = ImmutableColor.white;
            }

            this.RandomData.m_BaseTexture = (String)OutfitRNG.pickRandom(var3.m_BaseTextures);
            this.RandomData.m_TextureChoice = (String)OutfitRNG.pickRandom(var3.textureChoices);
            if (!StringUtils.isNullOrWhitespace(var3.m_DecalGroup)) {
               this.RandomData.m_Decal = ClothingDecals.instance.getRandomDecal(var3.m_DecalGroup);
            }

         }
      }
   }

   private ClothingItemReference pickRandomItemInternal() {
      if (this.subItems.isEmpty()) {
         return this;
      } else {
         int var1 = OutfitRNG.Next(this.subItems.size() + 1);
         if (var1 == 0) {
            return this;
         } else {
            ClothingItemReference var2 = (ClothingItemReference)this.subItems.get(var1 - 1);
            return var2.RandomData.m_PickedItemRef;
         }
      }
   }

   public ClothingItemReference clone() {
      try {
         ClothingItemReference var1 = new ClothingItemReference();
         var1.probability = this.probability;
         var1.itemGUID = this.itemGUID;
         PZArrayUtil.copy(var1.subItems, this.subItems, ClothingItemReference::clone);
         return var1;
      } catch (CloneNotSupportedException var2) {
         throw new RuntimeException("ClothingItemReference clone failed.", var2);
      }
   }

   public static class RandomData {
      public boolean m_Active = true;
      public float m_Hue = 0.0F;
      public ImmutableColor m_Tint;
      public String m_BaseTexture;
      public ClothingItemReference m_PickedItemRef;
      public String m_TextureChoice;
      public String m_Decal;

      public RandomData() {
         this.m_Tint = ImmutableColor.white;
      }

      public void reset() {
         this.m_Active = true;
         this.m_Hue = 0.0F;
         this.m_Tint = ImmutableColor.white;
         this.m_BaseTexture = null;
         this.m_PickedItemRef = null;
         this.m_TextureChoice = null;
         this.m_Decal = null;
      }
   }
}

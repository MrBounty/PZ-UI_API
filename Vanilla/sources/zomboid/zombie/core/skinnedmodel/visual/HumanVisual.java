package zombie.core.skinnedmodel.visual;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import zombie.GameWindow;
import zombie.characterTextures.BloodBodyPartType;
import zombie.characters.HairOutfitDefinitions;
import zombie.characters.SurvivorDesc;
import zombie.characters.WornItems.BodyLocation;
import zombie.characters.WornItems.BodyLocationGroup;
import zombie.characters.WornItems.BodyLocations;
import zombie.core.ImmutableColor;
import zombie.core.skinnedmodel.model.CharacterMask;
import zombie.core.skinnedmodel.model.ModelInstance;
import zombie.core.skinnedmodel.population.BeardStyles;
import zombie.core.skinnedmodel.population.ClothingItem;
import zombie.core.skinnedmodel.population.ClothingItemReference;
import zombie.core.skinnedmodel.population.DefaultClothing;
import zombie.core.skinnedmodel.population.HairStyles;
import zombie.core.skinnedmodel.population.Outfit;
import zombie.core.skinnedmodel.population.OutfitManager;
import zombie.core.skinnedmodel.population.OutfitRNG;
import zombie.core.skinnedmodel.population.PopTemplateManager;
import zombie.debug.DebugLog;
import zombie.debug.DebugType;
import zombie.iso.IsoWorld;
import zombie.scripting.ScriptManager;
import zombie.scripting.objects.Item;
import zombie.util.StringUtils;

public final class HumanVisual extends BaseVisual {
   private final IHumanVisual owner;
   private ImmutableColor skinColor;
   private int skinTexture;
   private String skinTextureName;
   public int zombieRotStage;
   private ImmutableColor hairColor;
   private ImmutableColor beardColor;
   private String hairModel;
   private String beardModel;
   private int bodyHair;
   private final byte[] blood;
   private final byte[] dirt;
   private final byte[] holes;
   private final ItemVisuals bodyVisuals;
   private Outfit outfit;
   private String nonAttachedHair;
   private static final ArrayList itemVisualLocations = new ArrayList();
   private static final int LASTSTAND_VERSION1 = 1;
   private static final int LASTSTAND_VERSION = 1;

   public HumanVisual(IHumanVisual var1) {
      this.skinColor = ImmutableColor.white;
      this.skinTexture = -1;
      this.skinTextureName = null;
      this.zombieRotStage = -1;
      this.bodyHair = -1;
      this.blood = new byte[BloodBodyPartType.MAX.index()];
      this.dirt = new byte[BloodBodyPartType.MAX.index()];
      this.holes = new byte[BloodBodyPartType.MAX.index()];
      this.bodyVisuals = new ItemVisuals();
      this.outfit = null;
      this.nonAttachedHair = null;
      this.owner = var1;
      Arrays.fill(this.blood, (byte)0);
      Arrays.fill(this.dirt, (byte)0);
      Arrays.fill(this.holes, (byte)0);
   }

   public boolean isFemale() {
      return this.owner.isFemale();
   }

   public boolean isZombie() {
      return this.owner.isZombie();
   }

   public boolean isSkeleton() {
      return this.owner.isSkeleton();
   }

   public void setSkinColor(ImmutableColor var1) {
      this.skinColor = var1;
   }

   public ImmutableColor getSkinColor() {
      if (this.skinColor == null) {
         this.skinColor = new ImmutableColor(SurvivorDesc.getRandomSkinColor());
      }

      return this.skinColor;
   }

   public void setBodyHairIndex(int var1) {
      this.bodyHair = var1;
   }

   public int getBodyHairIndex() {
      return this.bodyHair;
   }

   public void setSkinTextureIndex(int var1) {
      this.skinTexture = var1;
   }

   public int getSkinTextureIndex() {
      return this.skinTexture;
   }

   public void setSkinTextureName(String var1) {
      this.skinTextureName = var1;
   }

   public float lerp(float var1, float var2, float var3) {
      if (var3 < 0.0F) {
         var3 = 0.0F;
      }

      if (var3 >= 1.0F) {
         var3 = 1.0F;
      }

      float var4 = var2 - var1;
      float var5 = var4 * var3;
      return var1 + var5;
   }

   public int pickRandomZombieRotStage() {
      int var1 = Math.max((int)IsoWorld.instance.getWorldAgeDays(), 0);
      float var2 = 20.0F;
      float var3 = 90.0F;
      float var4 = 100.0F;
      float var5 = 20.0F;
      float var6 = 10.0F;
      float var7 = 30.0F;
      if (var1 >= 180) {
         var5 = 0.0F;
         var7 = 10.0F;
      }

      float var8 = (float)var1 - var2;
      float var9 = var8 / (var3 - var2);
      float var10 = this.lerp(var4, var5, var9);
      float var11 = this.lerp(var6, var7, var9);
      float var12 = (float)OutfitRNG.Next(100);
      if (var12 < var10) {
         return 1;
      } else {
         return var12 < var11 + var10 ? 2 : 3;
      }
   }

   public String getSkinTexture() {
      if (this.skinTextureName != null) {
         return this.skinTextureName;
      } else {
         String var1 = "";
         ArrayList var2 = this.owner.isFemale() ? PopTemplateManager.instance.m_FemaleSkins : PopTemplateManager.instance.m_MaleSkins;
         if (this.owner.isZombie() && this.owner.isSkeleton()) {
            if (this.owner.isFemale()) {
               var2 = PopTemplateManager.instance.m_SkeletonFemaleSkins_Zombie;
            } else {
               var2 = PopTemplateManager.instance.m_SkeletonMaleSkins_Zombie;
            }
         } else if (this.owner.isZombie()) {
            if (this.zombieRotStage < 1 || this.zombieRotStage > 3) {
               this.zombieRotStage = this.pickRandomZombieRotStage();
            }

            switch(this.zombieRotStage) {
            case 1:
               var2 = this.owner.isFemale() ? PopTemplateManager.instance.m_FemaleSkins_Zombie1 : PopTemplateManager.instance.m_MaleSkins_Zombie1;
               break;
            case 2:
               var2 = this.owner.isFemale() ? PopTemplateManager.instance.m_FemaleSkins_Zombie2 : PopTemplateManager.instance.m_MaleSkins_Zombie2;
               break;
            case 3:
               var2 = this.owner.isFemale() ? PopTemplateManager.instance.m_FemaleSkins_Zombie3 : PopTemplateManager.instance.m_MaleSkins_Zombie3;
            }
         } else if (!this.owner.isFemale()) {
            var1 = !this.owner.isZombie() && this.bodyHair >= 0 ? "a" : "";
         }

         if (this.skinTexture < 0 || this.skinTexture >= var2.size()) {
            this.skinTexture = OutfitRNG.Next(var2.size());
         }

         String var10000 = (String)var2.get(this.skinTexture);
         return var10000 + var1;
      }
   }

   public void setHairColor(ImmutableColor var1) {
      this.hairColor = var1;
   }

   public ImmutableColor getHairColor() {
      if (this.hairColor == null) {
         this.hairColor = HairOutfitDefinitions.instance.getRandomHaircutColor(this.outfit != null ? this.outfit.m_Name : null);
      }

      return this.hairColor;
   }

   public void setBeardColor(ImmutableColor var1) {
      this.beardColor = var1;
   }

   public ImmutableColor getBeardColor() {
      if (this.beardColor == null) {
         this.beardColor = this.getHairColor();
      }

      return this.beardColor;
   }

   public void setHairModel(String var1) {
      this.hairModel = var1;
   }

   public String getHairModel() {
      if (this.owner.isFemale()) {
         if (HairStyles.instance.FindFemaleStyle(this.hairModel) == null) {
            this.hairModel = HairStyles.instance.getRandomFemaleStyle(this.outfit != null ? this.outfit.m_Name : null);
         }
      } else if (HairStyles.instance.FindMaleStyle(this.hairModel) == null) {
         this.hairModel = HairStyles.instance.getRandomMaleStyle(this.outfit != null ? this.outfit.m_Name : null);
      }

      return this.hairModel;
   }

   public void setBeardModel(String var1) {
      this.beardModel = var1;
   }

   public String getBeardModel() {
      if (this.owner.isFemale()) {
         this.beardModel = null;
      } else if (BeardStyles.instance.FindStyle(this.beardModel) == null) {
         this.beardModel = BeardStyles.instance.getRandomStyle(this.outfit != null ? this.outfit.m_Name : null);
      }

      return this.beardModel;
   }

   public void setBlood(BloodBodyPartType var1, float var2) {
      var2 = Math.max(0.0F, Math.min(1.0F, var2));
      this.blood[var1.index()] = (byte)((int)(var2 * 255.0F));
   }

   public float getBlood(BloodBodyPartType var1) {
      return (float)(this.blood[var1.index()] & 255) / 255.0F;
   }

   public void setDirt(BloodBodyPartType var1, float var2) {
      var2 = Math.max(0.0F, Math.min(1.0F, var2));
      this.dirt[var1.index()] = (byte)((int)(var2 * 255.0F));
   }

   public float getDirt(BloodBodyPartType var1) {
      return (float)(this.dirt[var1.index()] & 255) / 255.0F;
   }

   public void setHole(BloodBodyPartType var1) {
      this.holes[var1.index()] = -1;
   }

   public float getHole(BloodBodyPartType var1) {
      return (float)(this.holes[var1.index()] & 255) / 255.0F;
   }

   public void removeBlood() {
      Arrays.fill(this.blood, (byte)0);
   }

   public void removeDirt() {
      Arrays.fill(this.dirt, (byte)0);
   }

   public void randomBlood() {
      for(int var1 = 0; var1 < BloodBodyPartType.MAX.index(); ++var1) {
         this.setBlood(BloodBodyPartType.FromIndex(var1), OutfitRNG.Next(0.0F, 1.0F));
      }

   }

   public void randomDirt() {
      for(int var1 = 0; var1 < BloodBodyPartType.MAX.index(); ++var1) {
         this.setDirt(BloodBodyPartType.FromIndex(var1), OutfitRNG.Next(0.0F, 1.0F));
      }

   }

   public float getTotalBlood() {
      float var1 = 0.0F;

      for(int var2 = 0; var2 < this.blood.length; ++var2) {
         var1 += (float)(this.blood[var2] & 255) / 255.0F;
      }

      return var1;
   }

   public void clear() {
      this.skinColor = ImmutableColor.white;
      this.skinTexture = -1;
      this.skinTextureName = null;
      this.zombieRotStage = -1;
      this.hairColor = null;
      this.beardColor = null;
      this.hairModel = null;
      this.nonAttachedHair = null;
      this.beardModel = null;
      this.bodyHair = -1;
      Arrays.fill(this.blood, (byte)0);
      Arrays.fill(this.dirt, (byte)0);
      Arrays.fill(this.holes, (byte)0);
      this.bodyVisuals.clear();
   }

   public void copyFrom(HumanVisual var1) {
      if (var1 == null) {
         this.clear();
      } else {
         var1.getHairColor();
         var1.getHairModel();
         var1.getBeardModel();
         var1.getSkinTexture();
         this.skinColor = var1.skinColor;
         this.skinTexture = var1.skinTexture;
         this.skinTextureName = var1.skinTextureName;
         this.zombieRotStage = var1.zombieRotStage;
         this.hairColor = var1.hairColor;
         this.beardColor = var1.beardColor;
         this.hairModel = var1.hairModel;
         this.nonAttachedHair = var1.nonAttachedHair;
         this.beardModel = var1.beardModel;
         this.bodyHair = var1.bodyHair;
         this.outfit = var1.outfit;
         System.arraycopy(var1.blood, 0, this.blood, 0, this.blood.length);
         System.arraycopy(var1.dirt, 0, this.dirt, 0, this.dirt.length);
         System.arraycopy(var1.holes, 0, this.holes, 0, this.holes.length);
         this.bodyVisuals.clear();
         this.bodyVisuals.addAll(var1.bodyVisuals);
      }
   }

   public void save(ByteBuffer var1) throws IOException {
      byte var2 = 0;
      if (this.hairColor != null) {
         var2 = (byte)(var2 | 4);
      }

      if (this.beardColor != null) {
         var2 = (byte)(var2 | 2);
      }

      if (this.skinColor != null) {
         var2 = (byte)(var2 | 8);
      }

      if (this.beardModel != null) {
         var2 = (byte)(var2 | 16);
      }

      if (this.hairModel != null) {
         var2 = (byte)(var2 | 32);
      }

      if (this.skinTextureName != null) {
         var2 = (byte)(var2 | 64);
      }

      var1.put(var2);
      if (this.hairColor != null) {
         var1.put(this.hairColor.getRedByte());
         var1.put(this.hairColor.getGreenByte());
         var1.put(this.hairColor.getBlueByte());
      }

      if (this.beardColor != null) {
         var1.put(this.beardColor.getRedByte());
         var1.put(this.beardColor.getGreenByte());
         var1.put(this.beardColor.getBlueByte());
      }

      if (this.skinColor != null) {
         var1.put(this.skinColor.getRedByte());
         var1.put(this.skinColor.getGreenByte());
         var1.put(this.skinColor.getBlueByte());
      }

      var1.put((byte)this.bodyHair);
      var1.put((byte)this.skinTexture);
      var1.put((byte)this.zombieRotStage);
      if (this.skinTextureName != null) {
         GameWindow.WriteString(var1, this.skinTextureName);
      }

      if (this.beardModel != null) {
         GameWindow.WriteString(var1, this.beardModel);
      }

      if (this.hairModel != null) {
         GameWindow.WriteString(var1, this.hairModel);
      }

      var1.put((byte)this.blood.length);

      int var3;
      for(var3 = 0; var3 < this.blood.length; ++var3) {
         var1.put(this.blood[var3]);
      }

      var1.put((byte)this.dirt.length);

      for(var3 = 0; var3 < this.dirt.length; ++var3) {
         var1.put(this.dirt[var3]);
      }

      var1.put((byte)this.holes.length);

      for(var3 = 0; var3 < this.holes.length; ++var3) {
         var1.put(this.holes[var3]);
      }

      var1.put((byte)this.bodyVisuals.size());

      for(var3 = 0; var3 < this.bodyVisuals.size(); ++var3) {
         ItemVisual var4 = (ItemVisual)this.bodyVisuals.get(var3);
         var4.save(var1);
      }

      GameWindow.WriteString(var1, this.getNonAttachedHair());
   }

   public void load(ByteBuffer var1, int var2) throws IOException {
      int var3 = var1.get() & 255;
      int var4;
      int var5;
      int var6;
      if ((var3 & 4) != 0) {
         var4 = var1.get() & 255;
         var5 = var1.get() & 255;
         var6 = var1.get() & 255;
         this.hairColor = new ImmutableColor(var4, var5, var6);
      }

      if ((var3 & 2) != 0) {
         var4 = var1.get() & 255;
         var5 = var1.get() & 255;
         var6 = var1.get() & 255;
         this.beardColor = new ImmutableColor(var4, var5, var6);
      }

      if ((var3 & 8) != 0) {
         var4 = var1.get() & 255;
         var5 = var1.get() & 255;
         var6 = var1.get() & 255;
         this.skinColor = new ImmutableColor(var4, var5, var6);
      }

      this.bodyHair = var1.get();
      this.skinTexture = var1.get();
      if (var2 >= 156) {
         this.zombieRotStage = var1.get();
      }

      if ((var3 & 64) != 0) {
         this.skinTextureName = GameWindow.ReadString(var1);
      }

      if ((var3 & 16) != 0) {
         this.beardModel = GameWindow.ReadString(var1);
      }

      if ((var3 & 32) != 0) {
         this.hairModel = GameWindow.ReadString(var1);
      }

      byte var7 = var1.get();

      byte var8;
      for(var5 = 0; var5 < var7; ++var5) {
         var8 = var1.get();
         if (var5 < this.blood.length) {
            this.blood[var5] = var8;
         }
      }

      if (var2 >= 163) {
         var7 = var1.get();

         for(var5 = 0; var5 < var7; ++var5) {
            var8 = var1.get();
            if (var5 < this.dirt.length) {
               this.dirt[var5] = var8;
            }
         }
      }

      var7 = var1.get();

      for(var5 = 0; var5 < var7; ++var5) {
         var8 = var1.get();
         if (var5 < this.holes.length) {
            this.holes[var5] = var8;
         }
      }

      var7 = var1.get();

      for(var5 = 0; var5 < var7; ++var5) {
         ItemVisual var9 = new ItemVisual();
         var9.load(var1, var2);
         this.bodyVisuals.add(var9);
      }

      this.setNonAttachedHair(GameWindow.ReadString(var1));
   }

   public ModelInstance createModelInstance() {
      return null;
   }

   public static CharacterMask GetMask(ItemVisuals var0) {
      CharacterMask var1 = new CharacterMask();

      for(int var2 = var0.size() - 1; var2 >= 0; --var2) {
         ((ItemVisual)var0.get(var2)).getClothingItemCombinedMask(var1);
      }

      return var1;
   }

   public void synchWithOutfit(Outfit var1) {
      if (var1 != null) {
         this.hairColor = var1.RandomData.m_hairColor;
         this.beardColor = this.hairColor;
         this.hairModel = this.owner.isFemale() ? var1.RandomData.m_femaleHairName : var1.RandomData.m_maleHairName;
         this.beardModel = this.owner.isFemale() ? null : var1.RandomData.m_beardName;
         this.getSkinTexture();
      }
   }

   public void dressInNamedOutfit(String var1, ItemVisuals var2) {
      var2.clear();
      if (!StringUtils.isNullOrWhitespace(var1)) {
         Outfit var3 = this.owner.isFemale() ? OutfitManager.instance.FindFemaleOutfit(var1) : OutfitManager.instance.FindMaleOutfit(var1);
         if (var3 != null) {
            Outfit var4 = var3.clone();
            var4.Randomize();
            this.dressInOutfit(var4, var2);
         }
      }
   }

   public void dressInClothingItem(String var1, ItemVisuals var2) {
      this.dressInClothingItem(var1, var2, true);
   }

   public void dressInClothingItem(String var1, ItemVisuals var2, boolean var3) {
      if (var3) {
         this.clear();
         var2.clear();
      }

      ClothingItem var4 = OutfitManager.instance.getClothingItem(var1);
      if (var4 != null) {
         Outfit var5 = new Outfit();
         ClothingItemReference var6 = new ClothingItemReference();
         var6.itemGUID = var1;
         var5.m_items.add(var6);
         var5.m_Pants = false;
         var5.m_Top = false;
         var5.Randomize();
         this.dressInOutfit(var5, var2);
      }
   }

   private void dressInOutfit(Outfit var1, ItemVisuals var2) {
      this.setOutfit(var1);
      this.getItemVisualLocations(var2, itemVisualLocations);
      String var3;
      if (var1.m_Pants) {
         var3 = var1.m_AllowPantsHue ? DefaultClothing.instance.pickPantsHue() : (var1.m_AllowPantsTint ? DefaultClothing.instance.pickPantsTint() : DefaultClothing.instance.pickPantsTexture());
         this.addClothingItem(var2, itemVisualLocations, var3, (ClothingItemReference)null);
      }

      if (var1.m_Top && var1.RandomData.m_hasTop) {
         if (var1.RandomData.m_hasTShirt) {
            if (var1.RandomData.m_hasTShirtDecal && var1.GetMask().isTorsoVisible() && var1.m_AllowTShirtDecal) {
               var3 = var1.m_AllowTopTint ? DefaultClothing.instance.pickTShirtDecalTint() : DefaultClothing.instance.pickTShirtDecalTexture();
            } else {
               var3 = var1.m_AllowTopTint ? DefaultClothing.instance.pickTShirtTint() : DefaultClothing.instance.pickTShirtTexture();
            }
         } else {
            var3 = var1.m_AllowTopTint ? DefaultClothing.instance.pickVestTint() : DefaultClothing.instance.pickVestTexture();
         }

         this.addClothingItem(var2, itemVisualLocations, var3, (ClothingItemReference)null);
      }

      for(int var6 = 0; var6 < var1.m_items.size(); ++var6) {
         ClothingItemReference var4 = (ClothingItemReference)var1.m_items.get(var6);
         ClothingItem var5 = var4.getClothingItem();
         if (var5 != null && var5.isReady()) {
            this.addClothingItem(var2, itemVisualLocations, var5.m_Name, var4);
         }
      }

      var1.m_Pants = false;
      var1.m_Top = false;
      var1.RandomData.m_topTexture = null;
      var1.RandomData.m_pantsTexture = null;
   }

   public ItemVisuals getBodyVisuals() {
      return this.bodyVisuals;
   }

   public ItemVisual addBodyVisual(String var1) {
      if (StringUtils.isNullOrWhitespace(var1)) {
         return null;
      } else {
         Item var2 = ScriptManager.instance.getItemForClothingItem(var1);
         if (var2 == null) {
            return null;
         } else {
            ClothingItem var3 = var2.getClothingItemAsset();
            if (var3 == null) {
               return null;
            } else {
               for(int var4 = 0; var4 < this.bodyVisuals.size(); ++var4) {
                  if (((ItemVisual)this.bodyVisuals.get(var4)).getClothingItemName().equals(var1)) {
                     return null;
                  }
               }

               ClothingItemReference var6 = new ClothingItemReference();
               var6.itemGUID = var3.m_GUID;
               var6.randomize();
               ItemVisual var5 = new ItemVisual();
               var5.setItemType(var2.getFullName());
               var5.synchWithOutfit(var6);
               this.bodyVisuals.add(var5);
               return var5;
            }
         }
      }
   }

   private void getItemVisualLocations(ItemVisuals var1, ArrayList var2) {
      var2.clear();

      for(int var3 = 0; var3 < var1.size(); ++var3) {
         ItemVisual var4 = (ItemVisual)var1.get(var3);
         Item var5 = var4.getScriptItem();
         if (var5 == null) {
            var2.add((Object)null);
         } else {
            String var6 = var5.getBodyLocation();
            if (StringUtils.isNullOrWhitespace(var6)) {
               var6 = var5.CanBeEquipped;
            }

            var2.add(var6);
         }
      }

   }

   public ItemVisual addClothingItem(ItemVisuals var1, Item var2) {
      if (var2 == null) {
         return null;
      } else {
         ClothingItem var3 = var2.getClothingItemAsset();
         if (var3 == null) {
            return null;
         } else if (!var3.isReady()) {
            return null;
         } else {
            this.getItemVisualLocations(var1, itemVisualLocations);
            return this.addClothingItem(var1, itemVisualLocations, var3.m_Name, (ClothingItemReference)null);
         }
      }
   }

   private ItemVisual addClothingItem(ItemVisuals var1, ArrayList var2, String var3, ClothingItemReference var4) {
      assert var1.size() == var2.size();

      if (var4 != null && !var4.RandomData.m_Active) {
         return null;
      } else if (StringUtils.isNullOrWhitespace(var3)) {
         return null;
      } else {
         Item var5 = ScriptManager.instance.getItemForClothingItem(var3);
         if (var5 == null) {
            if (DebugLog.isEnabled(DebugType.Clothing)) {
               DebugLog.Clothing.warn("Could not find item type for %s", var3);
            }

            return null;
         } else {
            ClothingItem var6 = var5.getClothingItemAsset();
            if (var6 == null) {
               return null;
            } else if (!var6.isReady()) {
               return null;
            } else {
               String var7 = var5.getBodyLocation();
               if (StringUtils.isNullOrWhitespace(var7)) {
                  var7 = var5.CanBeEquipped;
               }

               if (StringUtils.isNullOrWhitespace(var7)) {
                  return null;
               } else {
                  if (var4 == null) {
                     var4 = new ClothingItemReference();
                     var4.itemGUID = var6.m_GUID;
                     var4.randomize();
                  }

                  if (!var4.RandomData.m_Active) {
                     return null;
                  } else {
                     BodyLocationGroup var8 = BodyLocations.getGroup("Human");
                     BodyLocation var9 = var8.getLocation(var7);
                     if (var9 == null) {
                        DebugLog.General.error("The game can't found location '" + var7 + "' for the item '" + var5.name + "'");
                        return null;
                     } else {
                        int var10;
                        if (!var9.isMultiItem()) {
                           var10 = var2.indexOf(var7);
                           if (var10 != -1) {
                              var1.remove(var10);
                              var2.remove(var10);
                           }
                        }

                        for(var10 = 0; var10 < var1.size(); ++var10) {
                           if (var8.isExclusive(var7, (String)var2.get(var10))) {
                              var1.remove(var10);
                              var2.remove(var10);
                              --var10;
                           }
                        }

                        assert var1.size() == var2.size();

                        var10 = var8.indexOf(var7);
                        int var11 = var1.size();

                        for(int var12 = 0; var12 < var1.size(); ++var12) {
                           if (var8.indexOf((String)var2.get(var12)) > var10) {
                              var11 = var12;
                              break;
                           }
                        }

                        ItemVisual var13 = new ItemVisual();
                        var13.setItemType(var5.getFullName());
                        var13.synchWithOutfit(var4);
                        var1.add(var11, var13);
                        var2.add(var11, var7);
                        return var13;
                     }
                  }
               }
            }
         }
      }
   }

   public Outfit getOutfit() {
      return this.outfit;
   }

   public void setOutfit(Outfit var1) {
      this.outfit = var1;
   }

   public String getNonAttachedHair() {
      return this.nonAttachedHair;
   }

   public void setNonAttachedHair(String var1) {
      if (StringUtils.isNullOrWhitespace(var1)) {
         var1 = null;
      }

      this.nonAttachedHair = var1;
   }

   private static StringBuilder toString(ImmutableColor var0, StringBuilder var1) {
      var1.append(var0.getRedByte() & 255);
      var1.append(",");
      var1.append(var0.getGreenByte() & 255);
      var1.append(",");
      var1.append(var0.getBlueByte() & 255);
      return var1;
   }

   private static ImmutableColor colorFromString(String var0) {
      String[] var1 = var0.split(",");
      if (var1.length == 3) {
         try {
            int var2 = Integer.parseInt(var1[0]);
            int var3 = Integer.parseInt(var1[1]);
            int var4 = Integer.parseInt(var1[2]);
            return new ImmutableColor((float)var2 / 255.0F, (float)var3 / 255.0F, (float)var4 / 255.0F);
         } catch (NumberFormatException var5) {
         }
      }

      return null;
   }

   public String getLastStandString() {
      StringBuilder var1 = new StringBuilder();
      var1.append("version=");
      var1.append(1);
      var1.append(";");
      if (this.getHairColor() != null) {
         var1.append("hairColor=");
         toString(this.getHairColor(), var1);
         var1.append(";");
      }

      if (this.getBeardColor() != null) {
         var1.append("beardColor=");
         toString(this.getBeardColor(), var1);
         var1.append(";");
      }

      if (this.getSkinColor() != null) {
         var1.append("skinColor=");
         toString(this.getSkinColor(), var1);
         var1.append(";");
      }

      var1.append("bodyHair=");
      var1.append(this.getBodyHairIndex());
      var1.append(";");
      var1.append("skinTexture=");
      var1.append(this.getSkinTextureIndex());
      var1.append(";");
      if (this.getSkinTexture() != null) {
         var1.append("skinTextureName=");
         var1.append(this.getSkinTexture());
         var1.append(";");
      }

      if (this.getHairModel() != null) {
         var1.append("hairModel=");
         var1.append(this.getHairModel());
         var1.append(";");
      }

      if (this.getBeardModel() != null) {
         var1.append("beardModel=");
         var1.append(this.getBeardModel());
         var1.append(";");
      }

      return var1.toString();
   }

   public boolean loadLastStandString(String var1) {
      var1 = var1.trim();
      if (!StringUtils.isNullOrWhitespace(var1) && var1.startsWith("version=")) {
         boolean var2 = true;
         String[] var3 = var1.split(";");

         for(int var4 = 0; var4 < var3.length; ++var4) {
            int var5 = var3[var4].indexOf(61);
            if (var5 != -1) {
               String var6 = var3[var4].substring(0, var5).trim();
               String var7 = var3[var4].substring(var5 + 1).trim();
               byte var9 = -1;
               switch(var6.hashCode()) {
               case -1669441005:
                  if (var6.equals("beardColor")) {
                     var9 = 1;
                  }
                  break;
               case -1660213799:
                  if (var6.equals("beardModel")) {
                     var9 = 2;
                  }
                  break;
               case -1427300215:
                  if (var6.equals("skinTextureName")) {
                     var9 = 8;
                  }
                  break;
               case 284826785:
                  if (var6.equals("hairColor")) {
                     var9 = 4;
                  }
                  break;
               case 294053991:
                  if (var6.equals("hairModel")) {
                     var9 = 5;
                  }
                  break;
               case 351608024:
                  if (var6.equals("version")) {
                     var9 = 0;
                  }
                  break;
               case 1702284452:
                  if (var6.equals("bodyHair")) {
                     var9 = 3;
                  }
                  break;
               case 2011381734:
                  if (var6.equals("skinColor")) {
                     var9 = 6;
                  }
                  break;
               case 2130170078:
                  if (var6.equals("skinTexture")) {
                     var9 = 7;
                  }
               }

               ImmutableColor var10;
               switch(var9) {
               case 0:
                  int var13 = Integer.parseInt(var7);
                  if (var13 < 1 || var13 > 1) {
                     return false;
                  }
                  break;
               case 1:
                  var10 = colorFromString(var7);
                  if (var10 != null) {
                     this.setBeardColor(var10);
                  }
                  break;
               case 2:
                  this.setBeardModel(var7);
                  break;
               case 3:
                  try {
                     this.setBodyHairIndex(Integer.parseInt(var7));
                  } catch (NumberFormatException var12) {
                  }
                  break;
               case 4:
                  var10 = colorFromString(var7);
                  if (var10 != null) {
                     this.setHairColor(var10);
                  }
                  break;
               case 5:
                  this.setHairModel(var7);
                  break;
               case 6:
                  var10 = colorFromString(var7);
                  if (var10 != null) {
                     this.setSkinColor(var10);
                  }
                  break;
               case 7:
                  try {
                     this.setSkinTextureIndex(Integer.parseInt(var7));
                  } catch (NumberFormatException var11) {
                  }
                  break;
               case 8:
                  this.setSkinTextureName(var7);
               }
            }
         }

         return true;
      } else {
         return false;
      }
   }
}

package zombie.core.skinnedmodel.population;

import java.util.ArrayList;
import zombie.asset.Asset;
import zombie.asset.AssetManager;
import zombie.asset.AssetPath;
import zombie.asset.AssetType;
import zombie.core.skinnedmodel.model.CharacterMask;
import zombie.util.StringUtils;

public final class ClothingItem extends Asset {
   public String m_GUID;
   public String m_MaleModel;
   public String m_FemaleModel;
   public boolean m_Static = false;
   public ArrayList m_BaseTextures = new ArrayList();
   public String m_AttachBone;
   public ArrayList m_Masks = new ArrayList();
   public String m_MasksFolder = "media/textures/Body/Masks";
   public String m_UnderlayMasksFolder = "media/textures/Body/Masks";
   public ArrayList textureChoices = new ArrayList();
   public boolean m_AllowRandomHue = false;
   public boolean m_AllowRandomTint = false;
   public String m_DecalGroup = null;
   public String m_Shader = null;
   public String m_HatCategory = null;
   public static final String s_masksFolderDefault = "media/textures/Body/Masks";
   public String m_Name;
   public static final AssetType ASSET_TYPE = new AssetType("ClothingItem");

   public ClothingItem(AssetPath var1, AssetManager var2) {
      super(var1, var2);
   }

   public ArrayList getBaseTextures() {
      return this.m_BaseTextures;
   }

   public ArrayList getTextureChoices() {
      return this.textureChoices;
   }

   public String GetATexture() {
      return this.textureChoices.size() == 0 ? null : (String)OutfitRNG.pickRandom(this.textureChoices);
   }

   public boolean getAllowRandomHue() {
      return this.m_AllowRandomHue;
   }

   public boolean getAllowRandomTint() {
      return this.m_AllowRandomTint;
   }

   public String getDecalGroup() {
      return this.m_DecalGroup;
   }

   public boolean isHat() {
      return !StringUtils.isNullOrWhitespace(this.m_HatCategory) && !"nobeard".equals(this.m_HatCategory);
   }

   public boolean isMask() {
      return !StringUtils.isNullOrWhitespace(this.m_HatCategory) && !this.m_HatCategory.contains("hair");
   }

   public void getCombinedMask(CharacterMask var1) {
      var1.setPartsVisible(this.m_Masks, false);
   }

   public boolean hasModel() {
      return !StringUtils.isNullOrWhitespace(this.m_MaleModel) && !StringUtils.isNullOrWhitespace(this.m_FemaleModel);
   }

   public String getModel(boolean var1) {
      return var1 ? this.m_FemaleModel : this.m_MaleModel;
   }

   public String getFemaleModel() {
      return this.m_FemaleModel;
   }

   public String getMaleModel() {
      return this.m_MaleModel;
   }

   public String toString() {
      String var10000 = this.getClass().getSimpleName();
      return var10000 + "{ Name:" + this.m_Name + ", GUID:" + this.m_GUID + "}";
   }

   public static void tryGetCombinedMask(ClothingItemReference var0, CharacterMask var1) {
      tryGetCombinedMask(var0.getClothingItem(), var1);
   }

   public static void tryGetCombinedMask(ClothingItem var0, CharacterMask var1) {
      if (var0 != null) {
         var0.getCombinedMask(var1);
      }

   }

   public AssetType getType() {
      return ASSET_TYPE;
   }
}

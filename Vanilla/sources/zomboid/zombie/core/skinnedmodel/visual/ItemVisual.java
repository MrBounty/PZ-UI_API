package zombie.core.skinnedmodel.visual;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Objects;
import zombie.GameWindow;
import zombie.characterTextures.BloodBodyPartType;
import zombie.core.ImmutableColor;
import zombie.core.skinnedmodel.model.CharacterMask;
import zombie.core.skinnedmodel.model.ModelInstance;
import zombie.core.skinnedmodel.population.ClothingDecals;
import zombie.core.skinnedmodel.population.ClothingItem;
import zombie.core.skinnedmodel.population.ClothingItemReference;
import zombie.core.skinnedmodel.population.OutfitRNG;
import zombie.inventory.InventoryItem;
import zombie.inventory.InventoryItemFactory;
import zombie.scripting.ScriptManager;
import zombie.scripting.objects.Item;
import zombie.util.StringUtils;

public final class ItemVisual extends BaseVisual {
   private String m_fullType;
   private String m_clothingItemName;
   private String m_alternateModelName;
   public static final float NULL_HUE = Float.POSITIVE_INFINITY;
   public float m_Hue = Float.POSITIVE_INFINITY;
   public ImmutableColor m_Tint = null;
   public int m_BaseTexture = -1;
   public int m_TextureChoice = -1;
   public String m_Decal = null;
   private byte[] blood;
   private byte[] dirt;
   private byte[] holes;
   private byte[] basicPatches;
   private byte[] denimPatches;
   private byte[] leatherPatches;
   private InventoryItem inventoryItem = null;
   private static final int LASTSTAND_VERSION1 = 1;
   private static final int LASTSTAND_VERSION = 1;

   public ItemVisual() {
   }

   public ItemVisual(ItemVisual var1) {
      this.copyFrom(var1);
   }

   public void setItemType(String var1) {
      Objects.requireNonNull(var1);

      assert var1.contains(".");

      this.m_fullType = var1;
   }

   public String getItemType() {
      return this.m_fullType;
   }

   public void setAlternateModelName(String var1) {
      this.m_alternateModelName = var1;
   }

   public String getAlternateModelName() {
      return this.m_alternateModelName;
   }

   public String toString() {
      String var10000 = this.getClass().getSimpleName();
      return var10000 + "{ m_clothingItemName:\"" + this.m_clothingItemName + "\"}";
   }

   public String getClothingItemName() {
      return this.m_clothingItemName;
   }

   public void setClothingItemName(String var1) {
      this.m_clothingItemName = var1;
   }

   public Item getScriptItem() {
      return StringUtils.isNullOrWhitespace(this.m_fullType) ? null : ScriptManager.instance.getItem(this.m_fullType);
   }

   public ClothingItem getClothingItem() {
      Item var1 = this.getScriptItem();
      if (var1 == null) {
         return null;
      } else {
         if (!StringUtils.isNullOrWhitespace(this.m_alternateModelName)) {
            if ("LeftHand".equalsIgnoreCase(this.m_alternateModelName)) {
               return var1.replaceSecondHand.clothingItem;
            }

            if ("RightHand".equalsIgnoreCase(this.m_alternateModelName)) {
               return var1.replacePrimaryHand.clothingItem;
            }
         }

         return var1.getClothingItemAsset();
      }
   }

   public void getClothingItemCombinedMask(CharacterMask var1) {
      ClothingItem.tryGetCombinedMask(this.getClothingItem(), var1);
   }

   public void setHue(float var1) {
      var1 = Math.max(var1, -1.0F);
      var1 = Math.min(var1, 1.0F);
      this.m_Hue = var1;
   }

   public float getHue(ClothingItem var1) {
      if (var1.m_AllowRandomHue) {
         if (this.m_Hue == Float.POSITIVE_INFINITY) {
            this.m_Hue = (float)OutfitRNG.Next(200) / 100.0F - 1.0F;
         }

         return this.m_Hue;
      } else {
         return this.m_Hue = 0.0F;
      }
   }

   public void setTint(ImmutableColor var1) {
      this.m_Tint = var1;
   }

   public ImmutableColor getTint(ClothingItem var1) {
      if (var1.m_AllowRandomTint) {
         if (this.m_Tint == null) {
            this.m_Tint = OutfitRNG.randomImmutableColor();
         }

         return this.m_Tint;
      } else {
         return this.m_Tint = ImmutableColor.white;
      }
   }

   public ImmutableColor getTint() {
      return this.m_Tint;
   }

   public String getBaseTexture(ClothingItem var1) {
      if (var1.m_BaseTextures.isEmpty()) {
         this.m_BaseTexture = -1;
         return null;
      } else {
         if (this.m_BaseTexture < 0 || this.m_BaseTexture >= var1.m_BaseTextures.size()) {
            this.m_BaseTexture = OutfitRNG.Next(var1.m_BaseTextures.size());
         }

         return (String)var1.m_BaseTextures.get(this.m_BaseTexture);
      }
   }

   public String getTextureChoice(ClothingItem var1) {
      if (var1.textureChoices.isEmpty()) {
         this.m_TextureChoice = -1;
         return null;
      } else {
         if (this.m_TextureChoice < 0 || this.m_TextureChoice >= var1.textureChoices.size()) {
            this.m_TextureChoice = OutfitRNG.Next(var1.textureChoices.size());
         }

         return (String)var1.textureChoices.get(this.m_TextureChoice);
      }
   }

   public void setDecal(String var1) {
      this.m_Decal = var1;
   }

   public String getDecal(ClothingItem var1) {
      if (StringUtils.isNullOrWhitespace(var1.m_DecalGroup)) {
         return this.m_Decal = null;
      } else {
         if (this.m_Decal == null) {
            this.m_Decal = ClothingDecals.instance.getRandomDecal(var1.m_DecalGroup);
         }

         return this.m_Decal;
      }
   }

   public void pickUninitializedValues(ClothingItem var1) {
      if (var1 != null && var1.isReady()) {
         this.getHue(var1);
         this.getTint(var1);
         this.getBaseTexture(var1);
         this.getTextureChoice(var1);
         this.getDecal(var1);
      }
   }

   public void synchWithOutfit(ClothingItemReference var1) {
      ClothingItem var2 = var1.getClothingItem();
      this.m_clothingItemName = var2.m_Name;
      this.m_Hue = var1.RandomData.m_Hue;
      this.m_Tint = var1.RandomData.m_Tint;
      this.m_BaseTexture = var2.m_BaseTextures.indexOf(var1.RandomData.m_BaseTexture);
      this.m_TextureChoice = var2.textureChoices.indexOf(var1.RandomData.m_TextureChoice);
      this.m_Decal = var1.RandomData.m_Decal;
   }

   public void copyFrom(ItemVisual var1) {
      if (var1 == null) {
         this.m_fullType = null;
         this.m_clothingItemName = null;
         this.m_alternateModelName = null;
         this.m_Hue = Float.POSITIVE_INFINITY;
         this.m_Tint = null;
         this.m_BaseTexture = -1;
         this.m_TextureChoice = -1;
         this.m_Decal = null;
         if (this.blood != null) {
            Arrays.fill(this.blood, (byte)0);
         }

         if (this.dirt != null) {
            Arrays.fill(this.dirt, (byte)0);
         }

         if (this.holes != null) {
            Arrays.fill(this.holes, (byte)0);
         }

         if (this.basicPatches != null) {
            Arrays.fill(this.basicPatches, (byte)0);
         }

         if (this.denimPatches != null) {
            Arrays.fill(this.denimPatches, (byte)0);
         }

         if (this.leatherPatches != null) {
            Arrays.fill(this.leatherPatches, (byte)0);
         }

      } else {
         ClothingItem var2 = var1.getClothingItem();
         if (var2 != null) {
            var1.pickUninitializedValues(var2);
         }

         this.m_fullType = var1.m_fullType;
         this.m_clothingItemName = var1.m_clothingItemName;
         this.m_alternateModelName = var1.m_alternateModelName;
         this.m_Hue = var1.m_Hue;
         this.m_Tint = var1.m_Tint;
         this.m_BaseTexture = var1.m_BaseTexture;
         this.m_TextureChoice = var1.m_TextureChoice;
         this.m_Decal = var1.m_Decal;
         this.copyBlood(var1);
         this.copyHoles(var1);
         this.copyPatches(var1);
      }
   }

   public void save(ByteBuffer var1) throws IOException {
      byte var2 = 0;
      if (this.m_Tint != null) {
         var2 = (byte)(var2 | 1);
      }

      if (this.m_BaseTexture != -1) {
         var2 = (byte)(var2 | 2);
      }

      if (this.m_TextureChoice != -1) {
         var2 = (byte)(var2 | 4);
      }

      if (this.m_Hue != Float.POSITIVE_INFINITY) {
         var2 = (byte)(var2 | 8);
      }

      if (!StringUtils.isNullOrWhitespace(this.m_Decal)) {
         var2 = (byte)(var2 | 16);
      }

      var1.put(var2);
      GameWindow.WriteString(var1, this.m_fullType);
      GameWindow.WriteString(var1, this.m_alternateModelName);
      GameWindow.WriteString(var1, this.m_clothingItemName);
      if (this.m_Tint != null) {
         var1.put(this.m_Tint.getRedByte());
         var1.put(this.m_Tint.getGreenByte());
         var1.put(this.m_Tint.getBlueByte());
      }

      if (this.m_BaseTexture != -1) {
         var1.put((byte)this.m_BaseTexture);
      }

      if (this.m_TextureChoice != -1) {
         var1.put((byte)this.m_TextureChoice);
      }

      if (this.m_Hue != Float.POSITIVE_INFINITY) {
         var1.putFloat(this.m_Hue);
      }

      if (!StringUtils.isNullOrWhitespace(this.m_Decal)) {
         GameWindow.WriteString(var1, this.m_Decal);
      }

      int var3;
      if (this.blood != null) {
         var1.put((byte)this.blood.length);

         for(var3 = 0; var3 < this.blood.length; ++var3) {
            var1.put(this.blood[var3]);
         }
      } else {
         var1.put((byte)0);
      }

      if (this.dirt != null) {
         var1.put((byte)this.dirt.length);

         for(var3 = 0; var3 < this.dirt.length; ++var3) {
            var1.put(this.dirt[var3]);
         }
      } else {
         var1.put((byte)0);
      }

      if (this.holes != null) {
         var1.put((byte)this.holes.length);

         for(var3 = 0; var3 < this.holes.length; ++var3) {
            var1.put(this.holes[var3]);
         }
      } else {
         var1.put((byte)0);
      }

      if (this.basicPatches != null) {
         var1.put((byte)this.basicPatches.length);

         for(var3 = 0; var3 < this.basicPatches.length; ++var3) {
            var1.put(this.basicPatches[var3]);
         }
      } else {
         var1.put((byte)0);
      }

      if (this.denimPatches != null) {
         var1.put((byte)this.denimPatches.length);

         for(var3 = 0; var3 < this.denimPatches.length; ++var3) {
            var1.put(this.denimPatches[var3]);
         }
      } else {
         var1.put((byte)0);
      }

      if (this.leatherPatches != null) {
         var1.put((byte)this.leatherPatches.length);

         for(var3 = 0; var3 < this.leatherPatches.length; ++var3) {
            var1.put(this.leatherPatches[var3]);
         }
      } else {
         var1.put((byte)0);
      }

   }

   public void load(ByteBuffer var1, int var2) throws IOException {
      int var3 = var1.get() & 255;
      if (var2 >= 164) {
         this.m_fullType = GameWindow.ReadString(var1);
         this.m_alternateModelName = GameWindow.ReadString(var1);
      }

      this.m_clothingItemName = GameWindow.ReadString(var1);
      if (var2 < 164) {
         this.m_fullType = ScriptManager.instance.getItemTypeForClothingItem(this.m_clothingItemName);
      }

      int var5;
      if ((var3 & 1) != 0) {
         int var4 = var1.get() & 255;
         var5 = var1.get() & 255;
         int var6 = var1.get() & 255;
         this.m_Tint = new ImmutableColor(var4, var5, var6);
      }

      if ((var3 & 2) != 0) {
         this.m_BaseTexture = var1.get();
      }

      if ((var3 & 4) != 0) {
         this.m_TextureChoice = var1.get();
      }

      if (var2 >= 146) {
         if ((var3 & 8) != 0) {
            this.m_Hue = var1.getFloat();
         }

         if ((var3 & 16) != 0) {
            this.m_Decal = GameWindow.ReadString(var1);
         }
      }

      byte var7 = var1.get();
      if (var7 > 0 && this.blood == null) {
         this.blood = new byte[BloodBodyPartType.MAX.index()];
      }

      byte var8;
      for(var5 = 0; var5 < var7; ++var5) {
         var8 = var1.get();
         if (var5 < this.blood.length) {
            this.blood[var5] = var8;
         }
      }

      if (var2 >= 163) {
         var7 = var1.get();
         if (var7 > 0 && this.dirt == null) {
            this.dirt = new byte[BloodBodyPartType.MAX.index()];
         }

         for(var5 = 0; var5 < var7; ++var5) {
            var8 = var1.get();
            if (var5 < this.dirt.length) {
               this.dirt[var5] = var8;
            }
         }
      }

      var7 = var1.get();
      if (var7 > 0 && this.holes == null) {
         this.holes = new byte[BloodBodyPartType.MAX.index()];
      }

      for(var5 = 0; var5 < var7; ++var5) {
         var8 = var1.get();
         if (var5 < this.holes.length) {
            this.holes[var5] = var8;
         }
      }

      if (var2 >= 154) {
         var7 = var1.get();
         if (var7 > 0 && this.basicPatches == null) {
            this.basicPatches = new byte[BloodBodyPartType.MAX.index()];
         }

         for(var5 = 0; var5 < var7; ++var5) {
            var8 = var1.get();
            if (var5 < this.basicPatches.length) {
               this.basicPatches[var5] = var8;
            }
         }
      }

      if (var2 >= 155) {
         var7 = var1.get();
         if (var7 > 0 && this.denimPatches == null) {
            this.denimPatches = new byte[BloodBodyPartType.MAX.index()];
         }

         for(var5 = 0; var5 < var7; ++var5) {
            var8 = var1.get();
            if (var5 < this.denimPatches.length) {
               this.denimPatches[var5] = var8;
            }
         }

         var7 = var1.get();
         if (var7 > 0 && this.leatherPatches == null) {
            this.leatherPatches = new byte[BloodBodyPartType.MAX.index()];
         }

         for(var5 = 0; var5 < var7; ++var5) {
            var8 = var1.get();
            if (var5 < this.leatherPatches.length) {
               this.leatherPatches[var5] = var8;
            }
         }
      }

   }

   public ModelInstance createModelInstance() {
      return null;
   }

   public void setDenimPatch(BloodBodyPartType var1) {
      if (this.denimPatches == null) {
         this.denimPatches = new byte[BloodBodyPartType.MAX.index()];
      }

      this.denimPatches[var1.index()] = -1;
   }

   public float getDenimPatch(BloodBodyPartType var1) {
      return this.denimPatches == null ? 0.0F : (float)(this.denimPatches[var1.index()] & 255) / 255.0F;
   }

   public void setLeatherPatch(BloodBodyPartType var1) {
      if (this.leatherPatches == null) {
         this.leatherPatches = new byte[BloodBodyPartType.MAX.index()];
      }

      this.leatherPatches[var1.index()] = -1;
   }

   public float getLeatherPatch(BloodBodyPartType var1) {
      return this.leatherPatches == null ? 0.0F : (float)(this.leatherPatches[var1.index()] & 255) / 255.0F;
   }

   public void setBasicPatch(BloodBodyPartType var1) {
      if (this.basicPatches == null) {
         this.basicPatches = new byte[BloodBodyPartType.MAX.index()];
      }

      this.basicPatches[var1.index()] = -1;
   }

   public float getBasicPatch(BloodBodyPartType var1) {
      return this.basicPatches == null ? 0.0F : (float)(this.basicPatches[var1.index()] & 255) / 255.0F;
   }

   public int getBasicPatchesNumber() {
      if (this.basicPatches == null) {
         return 0;
      } else {
         int var1 = 0;

         for(int var2 = 0; var2 < this.basicPatches.length; ++var2) {
            if (this.basicPatches[var2] != 0) {
               ++var1;
            }
         }

         return var1;
      }
   }

   public void setHole(BloodBodyPartType var1) {
      if (this.holes == null) {
         this.holes = new byte[BloodBodyPartType.MAX.index()];
      }

      this.holes[var1.index()] = -1;
   }

   public float getHole(BloodBodyPartType var1) {
      return this.holes == null ? 0.0F : (float)(this.holes[var1.index()] & 255) / 255.0F;
   }

   public int getHolesNumber() {
      if (this.holes == null) {
         return 0;
      } else {
         int var1 = 0;

         for(int var2 = 0; var2 < this.holes.length; ++var2) {
            if (this.holes[var2] != 0) {
               ++var1;
            }
         }

         return var1;
      }
   }

   public void setBlood(BloodBodyPartType var1, float var2) {
      if (this.blood == null) {
         this.blood = new byte[BloodBodyPartType.MAX.index()];
      }

      var2 = Math.max(0.0F, Math.min(1.0F, var2));
      this.blood[var1.index()] = (byte)((int)(var2 * 255.0F));
   }

   public float getBlood(BloodBodyPartType var1) {
      return this.blood == null ? 0.0F : (float)(this.blood[var1.index()] & 255) / 255.0F;
   }

   public float getDirt(BloodBodyPartType var1) {
      return this.dirt == null ? 0.0F : (float)(this.dirt[var1.index()] & 255) / 255.0F;
   }

   public void setDirt(BloodBodyPartType var1, float var2) {
      if (this.dirt == null) {
         this.dirt = new byte[BloodBodyPartType.MAX.index()];
      }

      var2 = Math.max(0.0F, Math.min(1.0F, var2));
      this.dirt[var1.index()] = (byte)((int)(var2 * 255.0F));
   }

   public void copyBlood(ItemVisual var1) {
      if (var1.blood != null) {
         if (this.blood == null) {
            this.blood = new byte[BloodBodyPartType.MAX.index()];
         }

         System.arraycopy(var1.blood, 0, this.blood, 0, this.blood.length);
      } else if (this.blood != null) {
         Arrays.fill(this.blood, (byte)0);
      }

   }

   public void copyDirt(ItemVisual var1) {
      if (var1.dirt != null) {
         if (this.dirt == null) {
            this.dirt = new byte[BloodBodyPartType.MAX.index()];
         }

         System.arraycopy(var1.dirt, 0, this.dirt, 0, this.dirt.length);
      } else if (this.dirt != null) {
         Arrays.fill(this.dirt, (byte)0);
      }

   }

   public void copyHoles(ItemVisual var1) {
      if (var1.holes != null) {
         if (this.holes == null) {
            this.holes = new byte[BloodBodyPartType.MAX.index()];
         }

         System.arraycopy(var1.holes, 0, this.holes, 0, this.holes.length);
      } else if (this.holes != null) {
         Arrays.fill(this.holes, (byte)0);
      }

   }

   public void copyPatches(ItemVisual var1) {
      if (var1.basicPatches != null) {
         if (this.basicPatches == null) {
            this.basicPatches = new byte[BloodBodyPartType.MAX.index()];
         }

         System.arraycopy(var1.basicPatches, 0, this.basicPatches, 0, this.basicPatches.length);
      } else if (this.basicPatches != null) {
         Arrays.fill(this.basicPatches, (byte)0);
      }

      if (var1.denimPatches != null) {
         if (this.denimPatches == null) {
            this.denimPatches = new byte[BloodBodyPartType.MAX.index()];
         }

         System.arraycopy(var1.denimPatches, 0, this.denimPatches, 0, this.denimPatches.length);
      } else if (this.denimPatches != null) {
         Arrays.fill(this.denimPatches, (byte)0);
      }

      if (var1.leatherPatches != null) {
         if (this.leatherPatches == null) {
            this.leatherPatches = new byte[BloodBodyPartType.MAX.index()];
         }

         System.arraycopy(var1.leatherPatches, 0, this.leatherPatches, 0, this.leatherPatches.length);
      } else if (this.leatherPatches != null) {
         Arrays.fill(this.leatherPatches, (byte)0);
      }

   }

   public void removeHole(int var1) {
      if (this.holes != null) {
         this.holes[var1] = 0;
      }

   }

   public void removePatch(int var1) {
      if (this.basicPatches != null) {
         this.basicPatches[var1] = 0;
      }

      if (this.denimPatches != null) {
         this.denimPatches[var1] = 0;
      }

      if (this.leatherPatches != null) {
         this.leatherPatches[var1] = 0;
      }

   }

   public void removeBlood() {
      if (this.blood != null) {
         Arrays.fill(this.blood, (byte)0);
      }

   }

   public void removeDirt() {
      if (this.dirt != null) {
         Arrays.fill(this.dirt, (byte)0);
      }

   }

   public float getTotalBlood() {
      float var1 = 0.0F;
      if (this.blood != null) {
         for(int var2 = 0; var2 < this.blood.length; ++var2) {
            var1 += (float)(this.blood[var2] & 255) / 255.0F;
         }
      }

      return var1;
   }

   public InventoryItem getInventoryItem() {
      return this.inventoryItem;
   }

   public void setInventoryItem(InventoryItem var1) {
      this.inventoryItem = var1;
   }

   public void setBaseTexture(int var1) {
      this.m_BaseTexture = var1;
   }

   public int getBaseTexture() {
      return this.m_BaseTexture;
   }

   public void setTextureChoice(int var1) {
      this.m_TextureChoice = var1;
   }

   public int getTextureChoice() {
      return this.m_TextureChoice;
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
      Item var1 = this.getScriptItem();
      if (var1 == null) {
         return null;
      } else {
         ClothingItem var2 = this.getClothingItem();
         if (var2 == null) {
            return null;
         } else {
            StringBuilder var3 = new StringBuilder();
            var3.append("version=");
            var3.append(1);
            var3.append(";");
            var3.append("type=");
            var3.append(this.inventoryItem.getFullType());
            var3.append(";");
            ImmutableColor var4 = this.getTint(var2);
            var3.append("tint=");
            toString(var4, var3);
            var3.append(";");
            int var5 = this.getBaseTexture();
            if (var5 != -1) {
               var3.append("baseTexture=");
               var3.append(var5);
               var3.append(";");
            }

            int var6 = this.getTextureChoice();
            if (var6 != -1) {
               var3.append("textureChoice=");
               var3.append(var6);
               var3.append(";");
            }

            float var7 = this.getHue(var2);
            if (var7 != 0.0F) {
               var3.append("hue=");
               var3.append(var7);
               var3.append(";");
            }

            String var8 = this.getDecal(var2);
            if (!StringUtils.isNullOrWhitespace(var8)) {
               var3.append("decal=");
               var3.append(var8);
               var3.append(";");
            }

            return var3.toString();
         }
      }
   }

   public static InventoryItem createLastStandItem(String var0) {
      var0 = var0.trim();
      if (!StringUtils.isNullOrWhitespace(var0) && var0.startsWith("version=")) {
         InventoryItem var1 = null;
         ItemVisual var2 = null;
         boolean var3 = true;
         String[] var4 = var0.split(";");
         if (var4.length >= 2 && var4[1].trim().startsWith("type=")) {
            for(int var5 = 0; var5 < var4.length; ++var5) {
               int var6 = var4[var5].indexOf(61);
               if (var6 != -1) {
                  String var7 = var4[var5].substring(0, var6).trim();
                  String var8 = var4[var5].substring(var6 + 1).trim();
                  byte var10 = -1;
                  switch(var7.hashCode()) {
                  case -174809444:
                     if (var7.equals("textureChoice")) {
                        var10 = 4;
                     }
                     break;
                  case 103672:
                     if (var7.equals("hue")) {
                        var10 = 3;
                     }
                     break;
                  case 3560187:
                     if (var7.equals("tint")) {
                        var10 = 5;
                     }
                     break;
                  case 3575610:
                     if (var7.equals("type")) {
                        var10 = 6;
                     }
                     break;
                  case 95459245:
                     if (var7.equals("decal")) {
                        var10 = 2;
                     }
                     break;
                  case 351608024:
                     if (var7.equals("version")) {
                        var10 = 0;
                     }
                     break;
                  case 883640586:
                     if (var7.equals("baseTexture")) {
                        var10 = 1;
                     }
                  }

                  switch(var10) {
                  case 0:
                     int var15 = Integer.parseInt(var8);
                     if (var15 < 1 || var15 > 1) {
                        return null;
                     }
                     break;
                  case 1:
                     try {
                        var2.setBaseTexture(Integer.parseInt(var8));
                     } catch (NumberFormatException var14) {
                     }
                     break;
                  case 2:
                     if (!StringUtils.isNullOrWhitespace(var8)) {
                        var2.setDecal(var8);
                     }
                     break;
                  case 3:
                     try {
                        var2.setHue(Float.parseFloat(var8));
                     } catch (NumberFormatException var13) {
                     }
                     break;
                  case 4:
                     try {
                        var2.setTextureChoice(Integer.parseInt(var8));
                     } catch (NumberFormatException var12) {
                     }
                     break;
                  case 5:
                     ImmutableColor var11 = colorFromString(var8);
                     if (var11 != null) {
                        var2.setTint(var11);
                     }
                     break;
                  case 6:
                     var1 = InventoryItemFactory.CreateItem(var8);
                     if (var1 == null) {
                        return null;
                     }

                     var2 = var1.getVisual();
                     if (var2 == null) {
                        return null;
                     }
                  }
               }
            }

            return var1;
         } else {
            return null;
         }
      } else {
         return null;
      }
   }
}

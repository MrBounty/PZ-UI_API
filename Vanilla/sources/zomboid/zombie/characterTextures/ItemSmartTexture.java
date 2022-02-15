package zombie.characterTextures;

import zombie.core.textures.SmartTexture;
import zombie.core.textures.TextureCombinerCommand;
import zombie.core.textures.TextureCombinerShaderParam;
import zombie.util.StringUtils;

public final class ItemSmartTexture extends SmartTexture {
   public static final int DecalOverlayCategory = 300;
   private String m_texName = null;

   public ItemSmartTexture(String var1) {
      if (var1 != null) {
         this.add(var1);
         this.m_texName = var1;
      }
   }

   public ItemSmartTexture(String var1, float var2) {
      this.addHue("media/textures/" + var1 + ".png", 300, var2);
      this.m_texName = var1;
   }

   public void setDenimPatches(BloodBodyPartType var1) {
      if (!StringUtils.isNullOrEmpty(CharacterSmartTexture.DenimPatchesMaskFiles[var1.index()])) {
         String[] var10000 = CharacterSmartTexture.DenimPatchesMaskFiles;
         String var2 = "media/textures/patches/" + var10000[var1.index()] + ".png";
         int var3 = CharacterSmartTexture.DecalOverlayCategory + var1.index();
         this.addOverlayPatches(var2, "media/textures/patches/patchesmask.png", var3);
      }
   }

   public void setLeatherPatches(BloodBodyPartType var1) {
      if (!StringUtils.isNullOrEmpty(CharacterSmartTexture.LeatherPatchesMaskFiles[var1.index()])) {
         String[] var10000 = CharacterSmartTexture.LeatherPatchesMaskFiles;
         String var2 = "media/textures/patches/" + var10000[var1.index()] + ".png";
         int var3 = CharacterSmartTexture.DecalOverlayCategory + var1.index();
         this.addOverlayPatches(var2, "media/textures/patches/patchesmask.png", var3);
      }
   }

   public void setBasicPatches(BloodBodyPartType var1) {
      if (!StringUtils.isNullOrEmpty(CharacterSmartTexture.BasicPatchesMaskFiles[var1.index()])) {
         String[] var10000 = CharacterSmartTexture.BasicPatchesMaskFiles;
         String var2 = "media/textures/patches/" + var10000[var1.index()] + ".png";
         int var3 = CharacterSmartTexture.DecalOverlayCategory + var1.index();
         this.addOverlayPatches(var2, "media/textures/patches/patchesmask.png", var3);
      }
   }

   public void setBlood(String var1, BloodBodyPartType var2, float var3) {
      String[] var10000 = CharacterSmartTexture.MaskFiles;
      String var4 = "media/textures/BloodTextures/" + var10000[var2.index()] + ".png";
      int var5 = CharacterSmartTexture.DecalOverlayCategory + var2.index();
      this.setBlood(var1, var4, var3, var5);
   }

   public void setBlood(String var1, String var2, float var3, int var4) {
      var3 = Math.max(0.0F, Math.min(1.0F, var3));
      TextureCombinerCommand var5 = this.getFirstFromCategory(var4);
      if (var5 != null) {
         for(int var6 = 0; var6 < var5.shaderParams.size(); ++var6) {
            TextureCombinerShaderParam var7 = (TextureCombinerShaderParam)var5.shaderParams.get(var6);
            if (var7.name.equals("intensity") && (var7.min != var3 || var7.max != var3)) {
               var7.min = var7.max = var3;
               this.setDirty();
            }
         }
      } else if (var3 > 0.0F) {
         this.addOverlay(var1, var2, var3, var4);
      }

   }

   public float addBlood(String var1, BloodBodyPartType var2, float var3) {
      String[] var10000 = CharacterSmartTexture.MaskFiles;
      String var4 = "media/textures/BloodTextures/" + var10000[var2.index()] + ".png";
      int var5 = CharacterSmartTexture.DecalOverlayCategory + var2.index();
      return this.addBlood(var1, var4, var3, var5);
   }

   public float addDirt(String var1, BloodBodyPartType var2, float var3) {
      String[] var10000 = CharacterSmartTexture.MaskFiles;
      String var4 = "media/textures/BloodTextures/" + var10000[var2.index()] + ".png";
      int var5 = CharacterSmartTexture.DirtOverlayCategory + var2.index();
      return this.addDirt(var1, var4, var3, var5);
   }

   public float addBlood(String var1, String var2, float var3, int var4) {
      TextureCombinerCommand var5 = this.getFirstFromCategory(var4);
      if (var5 == null) {
         this.addOverlay(var1, var2, var3, var4);
         return var3;
      } else {
         for(int var6 = 0; var6 < var5.shaderParams.size(); ++var6) {
            TextureCombinerShaderParam var7 = (TextureCombinerShaderParam)var5.shaderParams.get(var6);
            if (var7.name.equals("intensity")) {
               float var8 = var7.min;
               var8 += var3;
               var8 = Math.min(1.0F, var8);
               if (var7.min != var8 || var7.max != var8) {
                  var7.min = var7.max = var8;
                  this.setDirty();
               }

               return var8;
            }
         }

         this.addOverlay(var1, var2, var3, var4);
         return var3;
      }
   }

   public float addDirt(String var1, String var2, float var3, int var4) {
      TextureCombinerCommand var5 = this.getFirstFromCategory(var4);
      if (var5 == null) {
         this.addDirtOverlay(var1, var2, var3, var4);
         return var3;
      } else {
         for(int var6 = 0; var6 < var5.shaderParams.size(); ++var6) {
            TextureCombinerShaderParam var7 = (TextureCombinerShaderParam)var5.shaderParams.get(var6);
            if (var7.name.equals("intensity")) {
               float var8 = var7.min;
               var8 += var3;
               var8 = Math.min(1.0F, var8);
               if (var7.min != var8 || var7.max != var8) {
                  var7.min = var7.max = var8;
                  this.setDirty();
               }

               return var8;
            }
         }

         this.addOverlay(var1, var2, var3, var4);
         return var3;
      }
   }

   public void removeBlood() {
      for(int var1 = 0; var1 < BloodBodyPartType.MAX.index(); ++var1) {
         this.removeBlood(BloodBodyPartType.FromIndex(var1));
      }

   }

   public void removeDirt() {
      for(int var1 = 0; var1 < BloodBodyPartType.MAX.index(); ++var1) {
         this.removeDirt(BloodBodyPartType.FromIndex(var1));
      }

   }

   public void removeBlood(BloodBodyPartType var1) {
      TextureCombinerCommand var2 = this.getFirstFromCategory(CharacterSmartTexture.DecalOverlayCategory + var1.index());
      if (var2 != null) {
         for(int var3 = 0; var3 < var2.shaderParams.size(); ++var3) {
            TextureCombinerShaderParam var4 = (TextureCombinerShaderParam)var2.shaderParams.get(var3);
            if (var4.name.equals("intensity") && (var4.min != 0.0F || var4.max != 0.0F)) {
               var4.min = var4.max = 0.0F;
               this.setDirty();
            }
         }
      }

   }

   public void removeDirt(BloodBodyPartType var1) {
      TextureCombinerCommand var2 = this.getFirstFromCategory(CharacterSmartTexture.DirtOverlayCategory + var1.index());
      if (var2 != null) {
         for(int var3 = 0; var3 < var2.shaderParams.size(); ++var3) {
            TextureCombinerShaderParam var4 = (TextureCombinerShaderParam)var2.shaderParams.get(var3);
            if (var4.name.equals("intensity") && (var4.min != 0.0F || var4.max != 0.0F)) {
               var4.min = var4.max = 0.0F;
               this.setDirty();
            }
         }
      }

   }

   public String getTexName() {
      return this.m_texName;
   }
}

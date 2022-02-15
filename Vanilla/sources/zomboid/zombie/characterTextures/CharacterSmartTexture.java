package zombie.characterTextures;

import org.lwjgl.opengl.GL11;
import zombie.characters.IsoGameCharacter;
import zombie.core.skinnedmodel.model.ModelInstance;
import zombie.core.textures.SmartTexture;
import zombie.core.textures.TextureCombinerCommand;
import zombie.core.textures.TextureCombinerShaderParam;

public final class CharacterSmartTexture extends SmartTexture {
   public static int BodyCategory = 0;
   public static int ClothingBottomCategory = 1;
   public static int ClothingTopCategory = 2;
   public static int ClothingItemCategory = 3;
   public static int DecalOverlayCategory = 300;
   public static int DirtOverlayCategory = 400;
   public static final String[] MaskFiles = new String[]{"BloodMaskHandL", "BloodMaskHandR", "BloodMaskLArmL", "BloodMaskLArmR", "BloodMaskUArmL", "BloodMaskUArmR", "BloodMaskChest", "BloodMaskStomach", "BloodMaskHead", "BloodMaskNeck", "BloodMaskGroin", "BloodMaskULegL", "BloodMaskULegR", "BloodMaskLLegL", "BloodMaskLLegR", "BloodMaskFootL", "BloodMaskFootR", "BloodMaskBack"};
   public static final String[] BasicPatchesMaskFiles = new String[]{"patches_left_hand_sheet", "patches_right_hand_sheet", "patches_left_lower_arm_sheet", "patches_right_lower_arm_sheet", "patches_left_upper_arm_sheet", "patches_right_upper_arm_sheet", "patches_chest_sheet", "patches_abdomen_sheet", "", "", "patches_groin_sheet", "patches_left_upper_leg_sheet", "patches_right_upper_leg_sheet", "patches_left_lower_leg_sheet", "patches_right_lower_leg_sheet", "", "", "patches_back_sheet"};
   public static final String[] DenimPatchesMaskFiles = new String[]{"patches_left_hand_denim", "patches_right_hand_denim", "patches_left_lower_arm_denim", "patches_right_lower_arm_denim", "patches_left_upper_arm_denim", "patches_right_upper_arm_denim", "patches_chest_denim", "patches_abdomen_denim", "", "", "patches_groin_denim", "patches_left_upper_leg_denim", "patches_right_upper_leg_denim", "patches_left_lower_leg_denim", "patches_right_lower_leg_denim", "", "", "patches_back_denim"};
   public static final String[] LeatherPatchesMaskFiles = new String[]{"patches_left_hand_leather", "patches_right_hand_leather", "patches_left_lower_arm_leather", "patches_right_lower_arm_leather", "patches_left_upper_arm_leather", "patches_right_upper_arm_leather", "patches_chest_leather", "patches_abdomen_leather", "", "", "patches_groin_leather", "patches_left_upper_leg_leather", "patches_right_upper_leg_leather", "patches_left_lower_leg_leather", "patches_right_lower_leg_leather", "", "", "patches_back_leather"};

   public void setBlood(BloodBodyPartType var1, float var2) {
      var2 = Math.max(0.0F, Math.min(1.0F, var2));
      int var3 = DecalOverlayCategory + var1.index();
      TextureCombinerCommand var4 = this.getFirstFromCategory(var3);
      if (var4 != null) {
         for(int var5 = 0; var5 < var4.shaderParams.size(); ++var5) {
            TextureCombinerShaderParam var6 = (TextureCombinerShaderParam)var4.shaderParams.get(var5);
            if (var6.name.equals("intensity") && (var6.min != var2 || var6.max != var2)) {
               var6.min = var6.max = var2;
               this.setDirty();
            }
         }
      } else if (var2 > 0.0F) {
         String[] var10000 = MaskFiles;
         String var7 = "media/textures/BloodTextures/" + var10000[var1.index()] + ".png";
         this.addOverlay("media/textures/BloodTextures/BloodOverlay.png", var7, var2, var3);
      }

   }

   public void setDirt(BloodBodyPartType var1, float var2) {
      var2 = Math.max(0.0F, Math.min(1.0F, var2));
      int var3 = DirtOverlayCategory + var1.index();
      TextureCombinerCommand var4 = this.getFirstFromCategory(var3);
      if (var4 != null) {
         for(int var5 = 0; var5 < var4.shaderParams.size(); ++var5) {
            TextureCombinerShaderParam var6 = (TextureCombinerShaderParam)var4.shaderParams.get(var5);
            if (var6.name.equals("intensity") && (var6.min != var2 || var6.max != var2)) {
               var6.min = var6.max = var2;
               this.setDirty();
            }
         }
      } else if (var2 > 0.0F) {
         String[] var10000 = MaskFiles;
         String var7 = "media/textures/BloodTextures/" + var10000[var1.index()] + ".png";
         this.addDirtOverlay("media/textures/BloodTextures/GrimeOverlay.png", var7, var2, var3);
      }

   }

   public void removeBlood() {
      for(int var1 = 0; var1 < BloodBodyPartType.MAX.index(); ++var1) {
         this.removeBlood(BloodBodyPartType.FromIndex(var1));
      }

   }

   public void removeBlood(BloodBodyPartType var1) {
      TextureCombinerCommand var2 = this.getFirstFromCategory(DecalOverlayCategory + var1.index());
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

   public float addBlood(BloodBodyPartType var1, float var2, IsoGameCharacter var3) {
      int var4 = DecalOverlayCategory + var1.index();
      TextureCombinerCommand var5 = this.getFirstFromCategory(var4);
      if (var1 == BloodBodyPartType.Head && var3 != null) {
         ModelInstance var10000;
         if (var3.hair != null) {
            var10000 = var3.hair;
            var10000.tintR -= 0.022F;
            if (var3.hair.tintR < 0.0F) {
               var3.hair.tintR = 0.0F;
            }

            var10000 = var3.hair;
            var10000.tintG -= 0.03F;
            if (var3.hair.tintG < 0.0F) {
               var3.hair.tintG = 0.0F;
            }

            var10000 = var3.hair;
            var10000.tintB -= 0.03F;
            if (var3.hair.tintB < 0.0F) {
               var3.hair.tintB = 0.0F;
            }
         }

         if (var3.beard != null) {
            var10000 = var3.beard;
            var10000.tintR -= 0.022F;
            if (var3.beard.tintR < 0.0F) {
               var3.beard.tintR = 0.0F;
            }

            var10000 = var3.beard;
            var10000.tintG -= 0.03F;
            if (var3.beard.tintG < 0.0F) {
               var3.beard.tintG = 0.0F;
            }

            var10000 = var3.beard;
            var10000.tintB -= 0.03F;
            if (var3.beard.tintB < 0.0F) {
               var3.beard.tintB = 0.0F;
            }
         }
      }

      if (var5 != null) {
         for(int var6 = 0; var6 < var5.shaderParams.size(); ++var6) {
            TextureCombinerShaderParam var7 = (TextureCombinerShaderParam)var5.shaderParams.get(var6);
            if (var7.name.equals("intensity")) {
               float var8 = var7.min;
               var8 += var2;
               var8 = Math.min(1.0F, var8);
               if (var7.min != var8 || var7.max != var8) {
                  var7.min = var7.max = var8;
                  this.setDirty();
               }

               return var8;
            }
         }
      } else {
         String[] var10 = MaskFiles;
         String var9 = "media/textures/BloodTextures/" + var10[var1.index()] + ".png";
         this.addOverlay("media/textures/BloodTextures/BloodOverlay.png", var9, var2, var4);
      }

      return var2;
   }

   public float addDirt(BloodBodyPartType var1, float var2, IsoGameCharacter var3) {
      int var4 = DirtOverlayCategory + var1.index();
      TextureCombinerCommand var5 = this.getFirstFromCategory(var4);
      if (var1 == BloodBodyPartType.Head && var3 != null) {
         ModelInstance var10000;
         if (var3.hair != null) {
            var10000 = var3.hair;
            var10000.tintR -= 0.022F;
            if (var3.hair.tintR < 0.0F) {
               var3.hair.tintR = 0.0F;
            }

            var10000 = var3.hair;
            var10000.tintG -= 0.03F;
            if (var3.hair.tintG < 0.0F) {
               var3.hair.tintG = 0.0F;
            }

            var10000 = var3.hair;
            var10000.tintB -= 0.03F;
            if (var3.hair.tintB < 0.0F) {
               var3.hair.tintB = 0.0F;
            }
         }

         if (var3.beard != null) {
            var10000 = var3.beard;
            var10000.tintR -= 0.022F;
            if (var3.beard.tintR < 0.0F) {
               var3.beard.tintR = 0.0F;
            }

            var10000 = var3.beard;
            var10000.tintG -= 0.03F;
            if (var3.beard.tintG < 0.0F) {
               var3.beard.tintG = 0.0F;
            }

            var10000 = var3.beard;
            var10000.tintB -= 0.03F;
            if (var3.beard.tintB < 0.0F) {
               var3.beard.tintB = 0.0F;
            }
         }
      }

      if (var5 != null) {
         for(int var6 = 0; var6 < var5.shaderParams.size(); ++var6) {
            TextureCombinerShaderParam var7 = (TextureCombinerShaderParam)var5.shaderParams.get(var6);
            if (var7.name.equals("intensity")) {
               float var8 = var7.min;
               var8 += var2;
               var8 = Math.min(1.0F, var8);
               if (var7.min != var8 || var7.max != var8) {
                  var7.min = var7.max = var8;
                  this.setDirty();
               }

               return var8;
            }
         }
      } else {
         String[] var10 = MaskFiles;
         String var9 = "media/textures/BloodTextures/" + var10[var1.index()] + ".png";
         this.addDirtOverlay("media/textures/BloodTextures/GrimeOverlay.png", var9, var2, var4);
      }

      return var2;
   }

   public void addShirtDecal(String var1) {
      GL11.glTexParameteri(3553, 10241, 9729);
      GL11.glTexParameteri(3553, 10240, 9729);
      this.addRect(var1, 102, 118, 52, 52);
   }
}

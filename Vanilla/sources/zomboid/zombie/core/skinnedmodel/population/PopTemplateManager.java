package zombie.core.skinnedmodel.population;

import java.util.ArrayList;
import java.util.Locale;
import zombie.characters.IsoGameCharacter;
import zombie.characters.WornItems.BodyLocationGroup;
import zombie.core.ImmutableColor;
import zombie.core.skinnedmodel.ModelManager;
import zombie.core.skinnedmodel.model.CharacterMask;
import zombie.core.skinnedmodel.model.ModelInstance;
import zombie.core.skinnedmodel.visual.HumanVisual;
import zombie.core.skinnedmodel.visual.IHumanVisual;
import zombie.core.skinnedmodel.visual.ItemVisual;
import zombie.core.skinnedmodel.visual.ItemVisuals;
import zombie.debug.DebugLog;
import zombie.debug.DebugLogStream;
import zombie.debug.DebugType;
import zombie.scripting.objects.Item;
import zombie.util.StringUtils;

public class PopTemplateManager {
   public static final PopTemplateManager instance = new PopTemplateManager();
   public final ArrayList m_MaleSkins = new ArrayList();
   public final ArrayList m_FemaleSkins = new ArrayList();
   public final ArrayList m_MaleSkins_Zombie1 = new ArrayList();
   public final ArrayList m_FemaleSkins_Zombie1 = new ArrayList();
   public final ArrayList m_MaleSkins_Zombie2 = new ArrayList();
   public final ArrayList m_FemaleSkins_Zombie2 = new ArrayList();
   public final ArrayList m_MaleSkins_Zombie3 = new ArrayList();
   public final ArrayList m_FemaleSkins_Zombie3 = new ArrayList();
   public final ArrayList m_SkeletonMaleSkins_Zombie = new ArrayList();
   public final ArrayList m_SkeletonFemaleSkins_Zombie = new ArrayList();
   public static final int SKELETON_BURNED_SKIN_INDEX = 0;
   public static final int SKELETON_NORMAL_SKIN_INDEX = 1;
   public static final int SKELETON_MUSCLE_SKIN_INDEX = 2;

   public void init() {
      ItemManager.init();

      int var1;
      for(var1 = 1; var1 <= 5; ++var1) {
         this.m_MaleSkins.add("MaleBody0" + var1);
      }

      for(var1 = 1; var1 <= 5; ++var1) {
         this.m_FemaleSkins.add("FemaleBody0" + var1);
      }

      for(var1 = 1; var1 <= 4; ++var1) {
         this.m_MaleSkins_Zombie1.add("M_ZedBody0" + var1 + "_level1");
         this.m_FemaleSkins_Zombie1.add("F_ZedBody0" + var1 + "_level1");
         this.m_MaleSkins_Zombie2.add("M_ZedBody0" + var1 + "_level2");
         this.m_FemaleSkins_Zombie2.add("F_ZedBody0" + var1 + "_level2");
         this.m_MaleSkins_Zombie3.add("M_ZedBody0" + var1 + "_level3");
         this.m_FemaleSkins_Zombie3.add("F_ZedBody0" + var1 + "_level3");
      }

      this.m_SkeletonMaleSkins_Zombie.add("SkeletonBurned");
      this.m_SkeletonMaleSkins_Zombie.add("Skeleton");
      this.m_SkeletonMaleSkins_Zombie.add("SkeletonMuscle");
      this.m_SkeletonFemaleSkins_Zombie.add("SkeletonBurned");
      this.m_SkeletonFemaleSkins_Zombie.add("Skeleton");
      this.m_SkeletonFemaleSkins_Zombie.add("SkeletonMuscle");
   }

   public ModelInstance addClothingItem(IsoGameCharacter var1, ModelManager.ModelSlot var2, ItemVisual var3, ClothingItem var4) {
      String var5 = var4.getModel(var1.isFemale());
      if (StringUtils.isNullOrWhitespace(var5)) {
         if (DebugLog.isEnabled(DebugType.Clothing)) {
            DebugLog.Clothing.debugln("No model specified by item: " + var4.m_Name);
         }

         return null;
      } else {
         var5 = this.processModelFileName(var5);
         String var6 = var3.getTextureChoice(var4);
         ImmutableColor var7 = var3.getTint(var4);
         var3.getHue(var4);
         String var9 = var4.m_AttachBone;
         String var10 = var4.m_Shader;
         ModelInstance var11;
         if (var9 != null && var9.length() > 0) {
            var11 = ModelManager.instance.newStaticInstance(var2, var5, var6, var9, var10);
         } else {
            var11 = ModelManager.instance.newAdditionalModelInstance(var5, var6, var1, var2.model.AnimPlayer, var10);
         }

         if (var11 == null) {
            return null;
         } else {
            this.postProcessNewItemInstance(var11, var2, var7);
            var11.setItemVisual(var3);
            return var11;
         }
      }
   }

   private void addHeadHairItem(IsoGameCharacter var1, ModelManager.ModelSlot var2, String var3, String var4, ImmutableColor var5) {
      if (StringUtils.isNullOrWhitespace(var3)) {
         if (DebugLog.isEnabled(DebugType.Clothing)) {
            DebugLog.Clothing.warn("No model specified.");
         }

      } else {
         var3 = this.processModelFileName(var3);
         ModelInstance var6 = ModelManager.instance.newAdditionalModelInstance(var3, var4, var1, var2.model.AnimPlayer, (String)null);
         if (var6 != null) {
            this.postProcessNewItemInstance(var6, var2, var5);
         }
      }
   }

   private void addHeadHair(IsoGameCharacter var1, ModelManager.ModelSlot var2, HumanVisual var3, ItemVisual var4, boolean var5) {
      ImmutableColor var6 = var3.getHairColor();
      if (var5) {
         var6 = var3.getBeardColor();
      }

      HairStyle var7;
      if (var1.isFemale()) {
         if (!var5) {
            var7 = HairStyles.instance.FindFemaleStyle(var3.getHairModel());
            if (var7 != null && var4 != null && var4.getClothingItem() != null) {
               var7 = HairStyles.instance.getAlternateForHat(var7, var4.getClothingItem().m_HatCategory);
            }

            if (var7 != null && var7.isValid()) {
               if (DebugLog.isEnabled(DebugType.Clothing)) {
                  DebugLog.Clothing.debugln("  Adding female hair: " + var7.name);
               }

               this.addHeadHairItem(var1, var2, var7.model, var7.texture, var6);
            }
         }
      } else if (!var5) {
         var7 = HairStyles.instance.FindMaleStyle(var3.getHairModel());
         if (var7 != null && var4 != null && var4.getClothingItem() != null) {
            var7 = HairStyles.instance.getAlternateForHat(var7, var4.getClothingItem().m_HatCategory);
         }

         if (var7 != null && var7.isValid()) {
            if (DebugLog.isEnabled(DebugType.Clothing)) {
               DebugLog.Clothing.debugln("  Adding male hair: " + var7.name);
            }

            this.addHeadHairItem(var1, var2, var7.model, var7.texture, var6);
         }
      } else {
         BeardStyle var8 = BeardStyles.instance.FindStyle(var3.getBeardModel());
         if (var8 != null && var8.isValid()) {
            if (var4 != null && var4.getClothingItem() != null && !StringUtils.isNullOrEmpty(var4.getClothingItem().m_HatCategory) && var4.getClothingItem().m_HatCategory.contains("nobeard")) {
               return;
            }

            if (DebugLog.isEnabled(DebugType.Clothing)) {
               DebugLog.Clothing.debugln("  Adding beard: " + var8.name);
            }

            this.addHeadHairItem(var1, var2, var8.model, var8.texture, var6);
         }
      }

   }

   public void populateCharacterModelSlot(IsoGameCharacter var1, ModelManager.ModelSlot var2) {
      if (!(var1 instanceof IHumanVisual)) {
         DebugLog.Clothing.warn("Supplied character is not an IHumanVisual. Ignored. " + var1);
      } else {
         HumanVisual var3 = ((IHumanVisual)var1).getHumanVisual();
         ItemVisuals var4 = new ItemVisuals();
         var1.getItemVisuals(var4);
         CharacterMask var5 = HumanVisual.GetMask(var4);
         if (DebugLog.isEnabled(DebugType.Clothing)) {
            DebugLogStream var10000 = DebugLog.Clothing;
            String var10001 = var1.getClass().getName();
            var10000.debugln("characterType:" + var10001 + ", name:" + var1.getName());
         }

         if (var5.isPartVisible(CharacterMask.Part.Head)) {
            this.addHeadHair(var1, var2, var3, var4.findHat(), false);
            this.addHeadHair(var1, var2, var3, var4.findMask(), true);
         }

         int var6;
         ItemVisual var7;
         ClothingItem var8;
         for(var6 = var4.size() - 1; var6 >= 0; --var6) {
            var7 = (ItemVisual)var4.get(var6);
            var8 = var7.getClothingItem();
            if (var8 == null) {
               if (DebugLog.isEnabled(DebugType.Clothing)) {
                  DebugLog.Clothing.warn("ClothingItem not found for ItemVisual:" + var7);
               }
            } else if (!this.isItemModelHidden(var1.getBodyLocationGroup(), var4, var7)) {
               this.addClothingItem(var1, var2, var7, var8);
            }
         }

         for(var6 = var3.getBodyVisuals().size() - 1; var6 >= 0; --var6) {
            var7 = (ItemVisual)var3.getBodyVisuals().get(var6);
            var8 = var7.getClothingItem();
            if (var8 == null) {
               if (DebugLog.isEnabled(DebugType.Clothing)) {
                  DebugLog.Clothing.warn("ClothingItem not found for ItemVisual:" + var7);
               }
            } else {
               this.addClothingItem(var1, var2, var7, var8);
            }
         }

         var1.postUpdateModelTextures();
         var1.updateSpeedModifiers();
      }
   }

   public boolean isItemModelHidden(BodyLocationGroup var1, ItemVisuals var2, ItemVisual var3) {
      Item var4 = var3.getScriptItem();
      if (var4 != null && var1.getLocation(var4.getBodyLocation()) != null) {
         for(int var5 = 0; var5 < var2.size(); ++var5) {
            if (var2.get(var5) != var3) {
               Item var6 = ((ItemVisual)var2.get(var5)).getScriptItem();
               if (var6 != null && var1.getLocation(var6.getBodyLocation()) != null && var1.isHideModel(var6.getBodyLocation(), var4.getBodyLocation())) {
                  return true;
               }
            }
         }

         return false;
      } else {
         return false;
      }
   }

   private String processModelFileName(String var1) {
      var1 = var1.replaceAll("\\\\", "/");
      var1 = var1.toLowerCase(Locale.ENGLISH);
      return var1;
   }

   private void postProcessNewItemInstance(ModelInstance var1, ModelManager.ModelSlot var2, ImmutableColor var3) {
      var1.depthBias = 0.0F;
      var1.matrixModel = var2.model;
      var1.tintR = var3.r;
      var1.tintG = var3.g;
      var1.tintB = var3.b;
      var1.parent = var2.model;
      var1.AnimPlayer = var2.model.AnimPlayer;
      var2.model.sub.add(0, var1);
      var2.sub.add(0, var1);
      var1.setOwner(var2);
   }
}

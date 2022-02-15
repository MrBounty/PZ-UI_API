package zombie.core.skinnedmodel.advancedanimation;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import org.joml.Math;
import org.joml.Matrix4fc;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjglx.BufferUtils;
import zombie.GameProfiler;
import zombie.GameTime;
import zombie.GameWindow;
import zombie.characters.IsoGameCharacter;
import zombie.characters.SurvivorDesc;
import zombie.characters.AttachedItems.AttachedModelName;
import zombie.characters.AttachedItems.AttachedModelNames;
import zombie.characters.WornItems.BodyLocationGroup;
import zombie.characters.WornItems.BodyLocations;
import zombie.characters.action.ActionContext;
import zombie.characters.action.ActionGroup;
import zombie.characters.action.IActionStateChanged;
import zombie.core.Color;
import zombie.core.Core;
import zombie.core.ImmutableColor;
import zombie.core.SpriteRenderer;
import zombie.core.skinnedmodel.ModelCamera;
import zombie.core.skinnedmodel.ModelManager;
import zombie.core.skinnedmodel.animation.AnimationPlayer;
import zombie.core.skinnedmodel.animation.AnimationTrack;
import zombie.core.skinnedmodel.animation.debug.AnimationPlayerRecorder;
import zombie.core.skinnedmodel.model.CharacterMask;
import zombie.core.skinnedmodel.model.Model;
import zombie.core.skinnedmodel.model.ModelInstance;
import zombie.core.skinnedmodel.model.ModelInstanceRenderData;
import zombie.core.skinnedmodel.model.ModelInstanceTextureCreator;
import zombie.core.skinnedmodel.model.ModelInstanceTextureInitializer;
import zombie.core.skinnedmodel.model.SkinningData;
import zombie.core.skinnedmodel.model.VehicleModelInstance;
import zombie.core.skinnedmodel.model.VehicleSubModelInstance;
import zombie.core.skinnedmodel.population.BeardStyle;
import zombie.core.skinnedmodel.population.BeardStyles;
import zombie.core.skinnedmodel.population.ClothingItem;
import zombie.core.skinnedmodel.population.HairStyle;
import zombie.core.skinnedmodel.population.HairStyles;
import zombie.core.skinnedmodel.population.PopTemplateManager;
import zombie.core.skinnedmodel.shader.Shader;
import zombie.core.skinnedmodel.visual.HumanVisual;
import zombie.core.skinnedmodel.visual.IHumanVisual;
import zombie.core.skinnedmodel.visual.ItemVisual;
import zombie.core.skinnedmodel.visual.ItemVisuals;
import zombie.core.textures.ColorInfo;
import zombie.core.textures.Texture;
import zombie.debug.DebugLog;
import zombie.debug.DebugOptions;
import zombie.debug.DebugType;
import zombie.iso.IsoGridSquare;
import zombie.iso.IsoMovingObject;
import zombie.iso.Vector2;
import zombie.popman.ObjectPool;
import zombie.scripting.ScriptManager;
import zombie.scripting.objects.ModelAttachment;
import zombie.scripting.objects.ModelScript;
import zombie.ui.UIManager;
import zombie.util.IPooledObject;
import zombie.util.Lambda;
import zombie.util.Pool;
import zombie.util.StringUtils;
import zombie.util.list.PZArrayUtil;
import zombie.vehicles.BaseVehicle;

public final class AnimatedModel extends AnimationVariableSource implements IAnimatable, IAnimEventCallback, IActionStateChanged, IHumanVisual {
   private String animSetName = "player-avatar";
   private String outfitName;
   private IsoGameCharacter character;
   private final HumanVisual humanVisual = new HumanVisual(this);
   private final ItemVisuals itemVisuals = new ItemVisuals();
   private String primaryHandModelName;
   private String secondaryHandModelName;
   private final AttachedModelNames attachedModelNames = new AttachedModelNames();
   private ModelInstance modelInstance;
   private boolean bFemale = false;
   private boolean bZombie = false;
   private boolean bSkeleton = false;
   private String state;
   private final Vector2 angle = new Vector2();
   private final Vector3f offset = new Vector3f(0.0F, -0.45F, 0.0F);
   private boolean bIsometric = true;
   private boolean flipY = false;
   private float m_alpha = 1.0F;
   private AnimationPlayer animPlayer = null;
   private final ActionContext actionContext = new ActionContext(this);
   private final AdvancedAnimator advancedAnimator = new AdvancedAnimator();
   private float trackTime = 0.0F;
   private final String m_UID = String.format("%s-%s", this.getClass().getSimpleName(), UUID.randomUUID().toString());
   private float lightsOriginX;
   private float lightsOriginY;
   private float lightsOriginZ;
   private final IsoGridSquare.ResultLight[] lights = new IsoGridSquare.ResultLight[5];
   private final ColorInfo ambient = new ColorInfo();
   private boolean bOutside = true;
   private boolean bRoom = false;
   private boolean bUpdateTextures;
   private boolean bClothingChanged;
   private boolean bAnimate = true;
   private ModelInstanceTextureCreator textureCreator;
   private final AnimatedModel.StateInfo[] stateInfos = new AnimatedModel.StateInfo[3];
   private boolean bReady;
   private static final ObjectPool instDataPool = new ObjectPool(AnimatedModel.AnimatedModelInstanceRenderData::new);
   private final AnimatedModel.UIModelCamera uiModelCamera = new AnimatedModel.UIModelCamera();
   private static final AnimatedModel.WorldModelCamera worldModelCamera = new AnimatedModel.WorldModelCamera();

   public AnimatedModel() {
      this.advancedAnimator.init(this);
      this.advancedAnimator.animCallbackHandlers.add(this);
      this.actionContext.onStateChanged.add(this);

      int var1;
      for(var1 = 0; var1 < this.lights.length; ++var1) {
         this.lights[var1] = new IsoGridSquare.ResultLight();
      }

      for(var1 = 0; var1 < this.stateInfos.length; ++var1) {
         this.stateInfos[var1] = new AnimatedModel.StateInfo();
      }

   }

   public HumanVisual getHumanVisual() {
      return this.humanVisual;
   }

   public void getItemVisuals(ItemVisuals var1) {
      var1.clear();
   }

   public boolean isFemale() {
      return this.bFemale;
   }

   public boolean isZombie() {
      return this.bZombie;
   }

   public boolean isSkeleton() {
      return this.bSkeleton;
   }

   public void setAnimSetName(String var1) {
      if (StringUtils.isNullOrWhitespace(var1)) {
         throw new IllegalArgumentException("invalid AnimSet \"" + var1 + "\"");
      } else {
         this.animSetName = var1;
      }
   }

   public void setOutfitName(String var1, boolean var2, boolean var3) {
      this.outfitName = var1;
      this.bFemale = var2;
      this.bZombie = var3;
   }

   public void setCharacter(IsoGameCharacter var1) {
      this.outfitName = null;
      this.humanVisual.clear();
      this.itemVisuals.clear();
      if (var1 instanceof IHumanVisual) {
         var1.getItemVisuals(this.itemVisuals);
         this.character = var1;
         this.attachedModelNames.initFrom(var1.getAttachedItems());
         this.setModelData(((IHumanVisual)var1).getHumanVisual(), this.itemVisuals);
      }
   }

   public void setSurvivorDesc(SurvivorDesc var1) {
      this.outfitName = null;
      this.humanVisual.clear();
      this.itemVisuals.clear();
      var1.getWornItems().getItemVisuals(this.itemVisuals);
      this.attachedModelNames.clear();
      this.setModelData(var1.getHumanVisual(), this.itemVisuals);
   }

   public void setPrimaryHandModelName(String var1) {
      this.primaryHandModelName = var1;
   }

   public void setSecondaryHandModelName(String var1) {
      this.secondaryHandModelName = var1;
   }

   public void setAttachedModelNames(AttachedModelNames var1) {
      this.attachedModelNames.copyFrom(var1);
   }

   public void setModelData(HumanVisual var1, ItemVisuals var2) {
      AnimationPlayer var3 = this.animPlayer;
      Model var4 = this.animPlayer == null ? null : var3.getModel();
      if (this.humanVisual != var1) {
         this.humanVisual.copyFrom(var1);
      }

      if (this.itemVisuals != var2) {
         this.itemVisuals.clear();
         this.itemVisuals.addAll(var2);
      }

      this.bFemale = var1.isFemale();
      this.bZombie = var1.isZombie();
      this.bSkeleton = var1.isSkeleton();
      if (this.modelInstance != null) {
         ModelManager.instance.resetModelInstanceRecurse(this.modelInstance, this);
      }

      Model var5;
      if (this.isSkeleton()) {
         var5 = this.bFemale ? ModelManager.instance.m_skeletonFemaleModel : ModelManager.instance.m_skeletonMaleModel;
      } else {
         var5 = this.bFemale ? ModelManager.instance.m_femaleModel : ModelManager.instance.m_maleModel;
      }

      this.modelInstance = ModelManager.instance.newInstance(var5, (IsoGameCharacter)null, this.getAnimationPlayer());
      this.modelInstance.m_modelScript = ScriptManager.instance.getModelScript(this.bFemale ? "FemaleBody" : "MaleBody");
      this.modelInstance.setOwner(this);
      this.populateCharacterModelSlot();
      this.DoCharacterModelEquipped();
      boolean var6 = false;
      if (this.bAnimate) {
         AnimationSet var7 = AnimationSet.GetAnimationSet(this.GetAnimSetName(), false);
         if (var7 != this.advancedAnimator.animSet || var3 != this.getAnimationPlayer() || var4 != var5) {
            var6 = true;
         }
      } else {
         var6 = true;
      }

      if (var6) {
         this.advancedAnimator.OnAnimDataChanged(false);
      }

      if (this.bAnimate) {
         ActionGroup var11 = ActionGroup.getActionGroup(this.GetAnimSetName());
         if (var11 != this.actionContext.getGroup()) {
            this.actionContext.setGroup(var11);
         }

         this.advancedAnimator.SetState(this.actionContext.getCurrentStateName(), PZArrayUtil.listConvert(this.actionContext.getChildStates(), (var0) -> {
            return var0.name;
         }));
      } else if (!StringUtils.isNullOrWhitespace(this.state)) {
         this.advancedAnimator.SetState(this.state);
      }

      if (var6) {
         float var12 = GameTime.getInstance().FPSMultiplier;
         GameTime.getInstance().FPSMultiplier = 100.0F;

         try {
            this.advancedAnimator.update();
         } finally {
            GameTime.getInstance().FPSMultiplier = var12;
         }
      }

      if (Core.bDebug && !this.bAnimate && this.stateInfoMain().readyData.isEmpty()) {
         this.getAnimationPlayer().resetBoneModelTransforms();
      }

      this.trackTime = 0.0F;
      this.stateInfoMain().bModelsReady = this.isReadyToRender();
   }

   public void setAmbient(ColorInfo var1, boolean var2, boolean var3) {
      this.ambient.set(var1.r, var1.g, var1.b, 1.0F);
      this.bOutside = var2;
      this.bRoom = var3;
   }

   public void setLights(IsoGridSquare.ResultLight[] var1, float var2, float var3, float var4) {
      this.lightsOriginX = var2;
      this.lightsOriginY = var3;
      this.lightsOriginZ = var4;

      for(int var5 = 0; var5 < var1.length; ++var5) {
         this.lights[var5].copyFrom(var1[var5]);
      }

   }

   public void setState(String var1) {
      this.state = var1;
   }

   public String getState() {
      return this.state;
   }

   public void setAngle(Vector2 var1) {
      this.angle.set(var1);
   }

   public void setOffset(float var1, float var2, float var3) {
      this.offset.set(var1, var2, var3);
   }

   public void setIsometric(boolean var1) {
      this.bIsometric = var1;
   }

   public boolean isIsometric() {
      return this.bIsometric;
   }

   public void setFlipY(boolean var1) {
      this.flipY = var1;
   }

   public void setAlpha(float var1) {
      this.m_alpha = var1;
   }

   public void setTrackTime(float var1) {
      this.trackTime = var1;
   }

   public void clothingItemChanged(String var1) {
      this.bClothingChanged = true;
   }

   public void setAnimate(boolean var1) {
      this.bAnimate = var1;
   }

   private void initOutfit() {
      String var1 = this.outfitName;
      this.outfitName = null;
      if (!StringUtils.isNullOrWhitespace(var1)) {
         ModelManager.instance.create();
         this.humanVisual.dressInNamedOutfit(var1, this.itemVisuals);
         this.setModelData(this.humanVisual, this.itemVisuals);
      }
   }

   private void populateCharacterModelSlot() {
      CharacterMask var1 = HumanVisual.GetMask(this.itemVisuals);
      if (var1.isPartVisible(CharacterMask.Part.Head)) {
         this.addHeadHair(this.itemVisuals.findHat());
      }

      int var2;
      ItemVisual var3;
      ClothingItem var4;
      for(var2 = this.itemVisuals.size() - 1; var2 >= 0; --var2) {
         var3 = (ItemVisual)this.itemVisuals.get(var2);
         var4 = var3.getClothingItem();
         if (var4 != null && var4.isReady() && !this.isItemModelHidden(this.itemVisuals, var3)) {
            this.addClothingItem(var3, var4);
         }
      }

      for(var2 = this.humanVisual.getBodyVisuals().size() - 1; var2 >= 0; --var2) {
         var3 = (ItemVisual)this.humanVisual.getBodyVisuals().get(var2);
         var4 = var3.getClothingItem();
         if (var4 != null && var4.isReady()) {
            this.addClothingItem(var3, var4);
         }
      }

      this.bUpdateTextures = true;
      Lambda.forEachFrom(PZArrayUtil::forEach, (List)this.modelInstance.sub, this.modelInstance, (var0, var1x) -> {
         var0.AnimPlayer = var1x.AnimPlayer;
      });
   }

   private void addHeadHair(ItemVisual var1) {
      ImmutableColor var2 = this.humanVisual.getHairColor();
      ImmutableColor var3 = this.humanVisual.getBeardColor();
      HairStyle var4;
      if (this.isFemale()) {
         var4 = HairStyles.instance.FindFemaleStyle(this.humanVisual.getHairModel());
         if (var4 != null && var1 != null && var1.getClothingItem() != null) {
            var4 = HairStyles.instance.getAlternateForHat(var4, var1.getClothingItem().m_HatCategory);
         }

         if (var4 != null && var4.isValid()) {
            if (DebugLog.isEnabled(DebugType.Clothing)) {
               DebugLog.Clothing.debugln("  Adding female hair: " + var4.name);
            }

            this.addHeadHairItem(var4.model, var4.texture, var2);
         }
      } else {
         var4 = HairStyles.instance.FindMaleStyle(this.humanVisual.getHairModel());
         if (var4 != null && var1 != null && var1.getClothingItem() != null) {
            var4 = HairStyles.instance.getAlternateForHat(var4, var1.getClothingItem().m_HatCategory);
         }

         if (var4 != null && var4.isValid()) {
            if (DebugLog.isEnabled(DebugType.Clothing)) {
               DebugLog.Clothing.debugln("  Adding male hair: " + var4.name);
            }

            this.addHeadHairItem(var4.model, var4.texture, var2);
         }

         BeardStyle var5 = BeardStyles.instance.FindStyle(this.humanVisual.getBeardModel());
         if (var5 != null && var5.isValid()) {
            if (var1 != null && var1.getClothingItem() != null && !StringUtils.isNullOrEmpty(var1.getClothingItem().m_HatCategory) && var1.getClothingItem().m_HatCategory.contains("nobeard")) {
               return;
            }

            if (DebugLog.isEnabled(DebugType.Clothing)) {
               DebugLog.Clothing.debugln("  Adding beard: " + var5.name);
            }

            this.addHeadHairItem(var5.model, var5.texture, var3);
         }
      }

   }

   private void addHeadHairItem(String var1, String var2, ImmutableColor var3) {
      if (StringUtils.isNullOrWhitespace(var1)) {
         if (DebugLog.isEnabled(DebugType.Clothing)) {
            DebugLog.Clothing.warn("No model specified.");
         }

      } else {
         var1 = this.processModelFileName(var1);
         ModelInstance var4 = ModelManager.instance.newAdditionalModelInstance(var1, var2, (IsoGameCharacter)null, this.modelInstance.AnimPlayer, (String)null);
         if (var4 != null) {
            this.postProcessNewItemInstance(var4, var3);
         }
      }
   }

   private void addClothingItem(ItemVisual var1, ClothingItem var2) {
      String var3 = var2.getModel(this.bFemale);
      if (StringUtils.isNullOrWhitespace(var3)) {
         if (DebugLog.isEnabled(DebugType.Clothing)) {
            DebugLog.Clothing.debugln("No model specified by item: " + var2.m_Name);
         }

      } else {
         var3 = this.processModelFileName(var3);
         String var4 = var1.getTextureChoice(var2);
         ImmutableColor var5 = var1.getTint(var2);
         String var6 = var2.m_AttachBone;
         String var7 = var2.m_Shader;
         ModelInstance var8;
         if (var6 != null && var6.length() > 0) {
            var8 = this.addStatic(var3, var4, var6, var7);
         } else {
            var8 = ModelManager.instance.newAdditionalModelInstance(var3, var4, (IsoGameCharacter)null, this.modelInstance.AnimPlayer, var7);
         }

         if (var8 != null) {
            this.postProcessNewItemInstance(var8, var5);
            var8.setItemVisual(var1);
         }
      }
   }

   private boolean isItemModelHidden(ItemVisuals var1, ItemVisual var2) {
      BodyLocationGroup var3 = BodyLocations.getGroup("Human");
      return PopTemplateManager.instance.isItemModelHidden(var3, var1, var2);
   }

   private String processModelFileName(String var1) {
      var1 = var1.replaceAll("\\\\", "/");
      var1 = var1.toLowerCase(Locale.ENGLISH);
      return var1;
   }

   private void postProcessNewItemInstance(ModelInstance var1, ImmutableColor var2) {
      var1.depthBias = 0.0F;
      var1.matrixModel = this.modelInstance;
      var1.tintR = var2.r;
      var1.tintG = var2.g;
      var1.tintB = var2.b;
      var1.AnimPlayer = this.modelInstance.AnimPlayer;
      this.modelInstance.sub.add(var1);
      var1.setOwner(this);
   }

   private void DoCharacterModelEquipped() {
      ModelInstance var1;
      if (!StringUtils.isNullOrWhitespace(this.primaryHandModelName)) {
         var1 = this.addStatic(this.primaryHandModelName, "Bip01_Prop1");
         this.postProcessNewItemInstance(var1, ImmutableColor.white);
      }

      if (!StringUtils.isNullOrWhitespace(this.secondaryHandModelName)) {
         var1 = this.addStatic(this.secondaryHandModelName, "Bip01_Prop2");
         this.postProcessNewItemInstance(var1, ImmutableColor.white);
      }

      for(int var5 = 0; var5 < this.attachedModelNames.size(); ++var5) {
         AttachedModelName var2 = this.attachedModelNames.get(var5);
         ModelInstance var3 = ModelManager.instance.addStatic((ModelInstance)null, var2.modelName, var2.attachmentName, var2.attachmentName);
         this.postProcessNewItemInstance(var3, ImmutableColor.white);
         if (var2.bloodLevel > 0.0F && !Core.getInstance().getOptionSimpleWeaponTextures()) {
            ModelInstanceTextureInitializer var4 = ModelInstanceTextureInitializer.alloc();
            var4.init(var3, var2.bloodLevel);
            var3.setTextureInitializer(var4);
         }
      }

   }

   private ModelInstance addStatic(String var1, String var2) {
      String var3 = var1;
      String var4 = var1;
      String var5 = null;
      ModelScript var6 = ScriptManager.instance.getModelScript(var1);
      if (var6 != null) {
         var3 = var6.getMeshName();
         var4 = var6.getTextureName();
         var5 = var6.getShaderName();
      }

      return this.addStatic(var3, var4, var2, var5);
   }

   private ModelInstance addStatic(String var1, String var2, String var3, String var4) {
      if (DebugLog.isEnabled(DebugType.Animation)) {
         DebugLog.Animation.debugln("Adding Static Model:" + var1);
      }

      Model var5 = ModelManager.instance.tryGetLoadedModel(var1, var2, true, var4, false);
      if (var5 == null) {
         ModelManager.instance.loadStaticModel(var1.toLowerCase(), var2, var4);
         var5 = ModelManager.instance.getLoadedModel(var1, var2, true, var4);
         if (var5 == null) {
            DebugLog.General.error("ModelManager.addStatic> Model not found. model:" + var1 + " tex:" + var2);
            return null;
         }
      }

      ModelInstance var6 = ModelManager.instance.newInstance(var5, (IsoGameCharacter)null, this.modelInstance.AnimPlayer);
      var6.parent = this.modelInstance;
      if (this.modelInstance.AnimPlayer != null) {
         var6.parentBone = this.modelInstance.AnimPlayer.getSkinningBoneIndex(var3, var6.parentBone);
         var6.parentBoneName = var3;
      }

      return var6;
   }

   private AnimatedModel.StateInfo stateInfoMain() {
      int var1 = SpriteRenderer.instance.getMainStateIndex();
      return this.stateInfos[var1];
   }

   private AnimatedModel.StateInfo stateInfoRender() {
      int var1 = SpriteRenderer.instance.getRenderStateIndex();
      return this.stateInfos[var1];
   }

   public void update() {
      GameProfiler.getInstance().invokeAndMeasure("AnimatedModel.Update", this, AnimatedModel::updateInternal);
   }

   private void updateInternal() {
      this.initOutfit();
      if (this.bClothingChanged) {
         this.bClothingChanged = false;
         this.setModelData(this.humanVisual, this.itemVisuals);
      }

      this.modelInstance.SetForceDir(this.angle);
      GameTime var1 = GameTime.getInstance();
      float var2 = var1.FPSMultiplier;
      if (this.bAnimate) {
         if (UIManager.useUIFBO) {
            var1.FPSMultiplier *= GameWindow.averageFPS / (float)Core.OptionUIRenderFPS;
         }

         this.actionContext.update();
         this.advancedAnimator.update();
         this.animPlayer.Update();
         int var3 = SpriteRenderer.instance.getMainStateIndex();
         AnimatedModel.StateInfo var4 = this.stateInfos[var3];
         if (!var4.readyData.isEmpty()) {
            ModelInstance var5 = ((AnimatedModel.AnimatedModelInstanceRenderData)var4.readyData.get(0)).modelInstance;
            if (var5 != this.modelInstance && var5.AnimPlayer != this.modelInstance.AnimPlayer) {
               var5.Update();
            }
         }

         var1.FPSMultiplier = var2;
      } else {
         var1.FPSMultiplier = 100.0F;

         try {
            this.advancedAnimator.update();
         } finally {
            var1.FPSMultiplier = var2;
         }

         if (this.trackTime > 0.0F && this.animPlayer.getMultiTrack().getTrackCount() > 0) {
            ((AnimationTrack)this.animPlayer.getMultiTrack().getTracks().get(0)).setCurrentTimeValue(this.trackTime);
         }

         this.animPlayer.Update(0.0F);
      }

   }

   private boolean isModelInstanceReady(ModelInstance var1) {
      if (var1.model != null && var1.model.isReady()) {
         return var1.model.Mesh.isReady() && var1.model.Mesh.vb != null;
      } else {
         return false;
      }
   }

   public boolean isReadyToRender() {
      if (!this.animPlayer.isReady()) {
         return false;
      } else if (!this.isModelInstanceReady(this.modelInstance)) {
         return false;
      } else {
         for(int var1 = 0; var1 < this.modelInstance.sub.size(); ++var1) {
            ModelInstance var2 = (ModelInstance)this.modelInstance.sub.get(var1);
            if (!this.isModelInstanceReady(var2)) {
               return false;
            }
         }

         return true;
      }
   }

   public int renderMain() {
      AnimatedModel.StateInfo var1 = this.stateInfoMain();
      int var2;
      if (this.modelInstance != null) {
         if (this.bUpdateTextures) {
            this.bUpdateTextures = false;
            this.textureCreator = ModelInstanceTextureCreator.alloc();
            this.textureCreator.init(this.humanVisual, this.itemVisuals, this.modelInstance);
         }

         ++this.modelInstance.renderRefCount;

         for(var2 = 0; var2 < this.modelInstance.sub.size(); ++var2) {
            ModelInstance var3 = (ModelInstance)this.modelInstance.sub.get(var2);
            ++var3.renderRefCount;
         }

         instDataPool.release((List)var1.instData);
         var1.instData.clear();
         if (!var1.bModelsReady && this.isReadyToRender()) {
            float var8 = GameTime.getInstance().FPSMultiplier;
            GameTime.getInstance().FPSMultiplier = 100.0F;

            try {
               this.advancedAnimator.update();
            } finally {
               GameTime.getInstance().FPSMultiplier = var8;
            }

            this.animPlayer.Update(0.0F);
            var1.bModelsReady = true;
         }

         AnimatedModel.AnimatedModelInstanceRenderData var9 = ((AnimatedModel.AnimatedModelInstanceRenderData)instDataPool.alloc()).init(this.modelInstance);
         var1.instData.add(var9);

         for(int var10 = 0; var10 < this.modelInstance.sub.size(); ++var10) {
            ModelInstance var4 = (ModelInstance)this.modelInstance.sub.get(var10);
            AnimatedModel.AnimatedModelInstanceRenderData var5 = ((AnimatedModel.AnimatedModelInstanceRenderData)instDataPool.alloc()).init(var4);
            var5.transformToParent(var9);
            var1.instData.add(var5);
         }
      }

      var1.modelInstance = this.modelInstance;
      var1.textureCreator = this.textureCreator != null && !this.textureCreator.isRendered() ? this.textureCreator : null;

      for(var2 = 0; var2 < var1.readyData.size(); ++var2) {
         AnimatedModel.AnimatedModelInstanceRenderData var11 = (AnimatedModel.AnimatedModelInstanceRenderData)var1.readyData.get(var2);
         var11.init(var11.modelInstance);
         var11.transformToParent(var1.getParentData(var11.modelInstance));
      }

      var1.bRendered = false;
      return SpriteRenderer.instance.getMainStateIndex();
   }

   public boolean isRendered() {
      return this.stateInfoRender().bRendered;
   }

   private void doneWithTextureCreator(ModelInstanceTextureCreator var1) {
      if (var1 != null) {
         for(int var2 = 0; var2 < this.stateInfos.length; ++var2) {
            if (this.stateInfos[var2].textureCreator == var1) {
               return;
            }
         }

         if (var1.isRendered()) {
            var1.postRender();
            if (var1 == this.textureCreator) {
               this.textureCreator = null;
            }
         } else if (var1 != this.textureCreator) {
            var1.postRender();
         }

      }
   }

   private void release(ArrayList var1) {
      for(int var2 = 0; var2 < var1.size(); ++var2) {
         AnimatedModel.AnimatedModelInstanceRenderData var3 = (AnimatedModel.AnimatedModelInstanceRenderData)var1.get(var2);
         if (var3.modelInstance.getTextureInitializer() != null) {
            var3.modelInstance.getTextureInitializer().postRender();
         }

         ModelManager.instance.derefModelInstance(var3.modelInstance);
      }

      instDataPool.release((List)var1);
   }

   public void postRender(boolean var1) {
      int var2 = SpriteRenderer.instance.getMainStateIndex();
      AnimatedModel.StateInfo var3 = this.stateInfos[var2];
      ModelInstanceTextureCreator var4 = var3.textureCreator;
      var3.textureCreator = null;
      this.doneWithTextureCreator(var4);
      var3.modelInstance = null;
      if (this.bAnimate && var3.bRendered) {
         this.release(var3.readyData);
         var3.readyData.clear();
         var3.readyData.addAll(var3.instData);
         var3.instData.clear();
      } else if (!this.bAnimate) {
      }

      this.release(var3.instData);
      var3.instData.clear();
   }

   public void DoRender(ModelCamera var1) {
      int var2 = SpriteRenderer.instance.getRenderStateIndex();
      AnimatedModel.StateInfo var3 = this.stateInfos[var2];
      this.bReady = true;
      ModelInstanceTextureCreator var4 = var3.textureCreator;
      if (var4 != null && !var4.isRendered()) {
         var4.render();
         if (!var4.isRendered()) {
            this.bReady = false;
         }
      }

      if (this.modelInstance.model == null || !this.modelInstance.model.isReady() || !this.modelInstance.model.Mesh.isReady() || this.modelInstance.model.Mesh.vb == null) {
         this.bReady = false;
      }

      int var5;
      for(var5 = 0; var5 < this.modelInstance.sub.size(); ++var5) {
         ModelInstance var6 = (ModelInstance)this.modelInstance.sub.get(var5);
         if (var6.model == null || !var6.model.isReady() || !var6.model.Mesh.isReady() || var6.model.Mesh.vb == null) {
            this.bReady = false;
         }
      }

      for(var5 = 0; var5 < var3.instData.size(); ++var5) {
         AnimatedModel.AnimatedModelInstanceRenderData var8 = (AnimatedModel.AnimatedModelInstanceRenderData)var3.instData.get(var5);
         ModelInstanceTextureInitializer var7 = var8.modelInstance.getTextureInitializer();
         if (var7 != null && !var7.isRendered()) {
            var7.render();
            if (!var7.isRendered()) {
               this.bReady = false;
            }
         }
      }

      if (this.bReady && !var3.bModelsReady) {
         this.bReady = false;
      }

      if (this.bReady || !var3.readyData.isEmpty()) {
         GL11.glPushClientAttrib(-1);
         GL11.glPushAttrib(1048575);
         GL11.glEnable(3042);
         GL11.glBlendFunc(770, 771);
         GL11.glEnable(3008);
         GL11.glAlphaFunc(516, 0.0F);
         var1.Begin();
         this.StartCharacter();
         this.Render();
         this.EndCharacter();
         var1.End();
         GL11.glDepthFunc(519);
         GL11.glPopAttrib();
         GL11.glPopClientAttrib();
         Texture.lastTextureID = -1;
         SpriteRenderer.ringBuffer.restoreVBOs = true;
         var3.bRendered = this.bReady;
      }
   }

   public void DoRender(int var1, int var2, int var3, int var4, float var5, float var6) {
      GL11.glClear(256);
      this.uiModelCamera.x = var1;
      this.uiModelCamera.y = var2;
      this.uiModelCamera.w = var3;
      this.uiModelCamera.h = var4;
      this.uiModelCamera.sizeV = var5;
      this.uiModelCamera.m_animPlayerAngle = var6;
      this.DoRender(this.uiModelCamera);
   }

   public void DoRenderToWorld(float var1, float var2, float var3, float var4) {
      worldModelCamera.x = var1;
      worldModelCamera.y = var2;
      worldModelCamera.z = var3;
      worldModelCamera.angle = var4;
      this.DoRender(worldModelCamera);
   }

   private void debugDrawAxes() {
      if (Core.bDebug && DebugOptions.instance.ModelRenderAxis.getValue()) {
         Model.debugDrawAxis(0.0F, 0.0F, 0.0F, 1.0F, 4.0F);
      }

   }

   private void StartCharacter() {
      GL11.glEnable(2929);
      GL11.glEnable(3042);
      GL11.glBlendFunc(770, 771);
      GL11.glEnable(3008);
      GL11.glAlphaFunc(516, 0.0F);
      GL11.glDisable(3089);
      GL11.glDepthMask(true);
   }

   private void EndCharacter() {
      GL11.glDepthMask(false);
      GL11.glViewport(0, 0, Core.width, Core.height);
   }

   private void Render() {
      int var1 = SpriteRenderer.instance.getRenderStateIndex();
      AnimatedModel.StateInfo var2 = this.stateInfos[var1];
      ModelInstance var3 = var2.modelInstance;
      if (var3 == null) {
         boolean var4 = true;
      } else {
         ArrayList var7 = this.bReady ? var2.instData : var2.readyData;

         for(int var5 = 0; var5 < var7.size(); ++var5) {
            AnimatedModel.AnimatedModelInstanceRenderData var6 = (AnimatedModel.AnimatedModelInstanceRenderData)var7.get(var5);
            this.DrawChar(var6);
         }
      }

      this.debugDrawAxes();
   }

   private void DrawChar(AnimatedModel.AnimatedModelInstanceRenderData var1) {
      ModelInstance var2 = var1.modelInstance;
      FloatBuffer var3 = var1.matrixPalette;
      if (var2 != null) {
         if (var2.AnimPlayer != null) {
            if (var2.AnimPlayer.hasSkinningData()) {
               if (var2.model != null) {
                  if (var2.model.isReady()) {
                     if (var2.tex != null || var2.model.tex != null) {
                        GL11.glEnable(2884);
                        GL11.glCullFace(1028);
                        GL11.glEnable(2929);
                        GL11.glEnable(3008);
                        GL11.glDepthFunc(513);
                        GL11.glDepthRange(0.0D, 1.0D);
                        GL11.glAlphaFunc(516, 0.01F);
                        if (var2.model.Effect == null) {
                           var2.model.CreateShader("basicEffect");
                        }

                        Shader var4 = var2.model.Effect;
                        int var6;
                        if (var4 != null) {
                           var4.Start();
                           if (var2.model.bStatic) {
                              var4.setTransformMatrix(var1.xfrm, true);
                           } else {
                              var4.setMatrixPalette(var3, true);
                           }

                           var4.setLight(0, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, Float.NaN, var2);
                           var4.setLight(1, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, Float.NaN, var2);
                           var4.setLight(2, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, Float.NaN, var2);
                           var4.setLight(3, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, Float.NaN, var2);
                           var4.setLight(4, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, Float.NaN, var2);
                           float var5 = 0.7F;

                           for(var6 = 0; var6 < this.lights.length; ++var6) {
                              IsoGridSquare.ResultLight var7 = this.lights[var6];
                              if (var7.radius > 0) {
                                 var4.setLight(var6, (float)var7.x + 0.5F, (float)var7.y + 0.5F, (float)var7.z + 0.5F, var7.r * var5, var7.g * var5, var7.b * var5, (float)var7.radius, var1.m_animPlayerAngle, this.lightsOriginX, this.lightsOriginY, this.lightsOriginZ, (IsoMovingObject)null);
                              }
                           }

                           if (var2.tex != null) {
                              var4.setTexture(var2.tex, "Texture", 0);
                           } else if (var2.model.tex != null) {
                              var4.setTexture(var2.model.tex, "Texture", 0);
                           }

                           float var10;
                           if (this.bOutside) {
                              var10 = ModelInstance.MODEL_LIGHT_MULT_OUTSIDE;
                              var4.setLight(3, this.lightsOriginX - 2.0F, this.lightsOriginY - 2.0F, this.lightsOriginZ + 1.0F, this.ambient.r * var10 / 4.0F, this.ambient.g * var10 / 4.0F, this.ambient.b * var10 / 4.0F, 5000.0F, var1.m_animPlayerAngle, this.lightsOriginX, this.lightsOriginY, this.lightsOriginZ, (IsoMovingObject)null);
                              var4.setLight(4, this.lightsOriginX + 2.0F, this.lightsOriginY + 2.0F, this.lightsOriginZ + 1.0F, this.ambient.r * var10 / 4.0F, this.ambient.g * var10 / 4.0F, this.ambient.b * var10 / 4.0F, 5000.0F, var1.m_animPlayerAngle, this.lightsOriginX, this.lightsOriginY, this.lightsOriginZ, (IsoMovingObject)null);
                           } else if (this.bRoom) {
                              var10 = ModelInstance.MODEL_LIGHT_MULT_ROOM;
                              var4.setLight(4, this.lightsOriginX + 2.0F, this.lightsOriginY + 2.0F, this.lightsOriginZ + 1.0F, this.ambient.r * var10 / 4.0F, this.ambient.g * var10 / 4.0F, this.ambient.b * var10 / 4.0F, 5000.0F, var1.m_animPlayerAngle, this.lightsOriginX, this.lightsOriginY, this.lightsOriginZ, (IsoMovingObject)null);
                           }

                           var4.setDepthBias(var2.depthBias / 50.0F);
                           var4.setAmbient(this.ambient.r * 0.45F, this.ambient.g * 0.45F, this.ambient.b * 0.45F);
                           var4.setLightingAmount(1.0F);
                           var4.setHueShift(var2.hue);
                           var4.setTint(var2.tintR, var2.tintG, var2.tintB);
                           var4.setAlpha(this.m_alpha);
                        }

                        var2.model.Mesh.Draw(var4);
                        if (var4 != null) {
                           var4.End();
                        }

                        if (Core.bDebug && DebugOptions.instance.ModelRenderLights.getValue() && var2.parent == null) {
                           Model var10000;
                           if (this.lights[0].radius > 0) {
                              var10000 = var2.model;
                              Model.debugDrawLightSource((float)this.lights[0].x, (float)this.lights[0].y, (float)this.lights[0].z, 0.0F, 0.0F, 0.0F, -var1.m_animPlayerAngle);
                           }

                           if (this.lights[1].radius > 0) {
                              var10000 = var2.model;
                              Model.debugDrawLightSource((float)this.lights[1].x, (float)this.lights[1].y, (float)this.lights[1].z, 0.0F, 0.0F, 0.0F, -var1.m_animPlayerAngle);
                           }

                           if (this.lights[2].radius > 0) {
                              var10000 = var2.model;
                              Model.debugDrawLightSource((float)this.lights[2].x, (float)this.lights[2].y, (float)this.lights[2].z, 0.0F, 0.0F, 0.0F, -var1.m_animPlayerAngle);
                           }
                        }

                        if (Core.bDebug && DebugOptions.instance.ModelRenderBones.getValue()) {
                           GL11.glDisable(2929);
                           GL11.glDisable(3553);
                           GL11.glLineWidth(1.0F);
                           GL11.glBegin(1);

                           for(int var9 = 0; var9 < var2.AnimPlayer.modelTransforms.length; ++var9) {
                              var6 = (Integer)var2.AnimPlayer.getSkinningData().SkeletonHierarchy.get(var9);
                              if (var6 >= 0) {
                                 Color var11 = Model.debugDrawColours[var9 % Model.debugDrawColours.length];
                                 GL11.glColor3f(var11.r, var11.g, var11.b);
                                 Matrix4f var8 = var2.AnimPlayer.modelTransforms[var9];
                                 GL11.glVertex3f(var8.m03, var8.m13, var8.m23);
                                 var8 = var2.AnimPlayer.modelTransforms[var6];
                                 GL11.glVertex3f(var8.m03, var8.m13, var8.m23);
                              }
                           }

                           GL11.glEnd();
                           GL11.glColor3f(1.0F, 1.0F, 1.0F);
                           GL11.glEnable(2929);
                        }

                     }
                  }
               }
            }
         }
      }
   }

   public void releaseAnimationPlayer() {
      if (this.animPlayer != null) {
         this.animPlayer = (AnimationPlayer)Pool.tryRelease((IPooledObject)this.animPlayer);
      }

   }

   public void OnAnimEvent(AnimLayer var1, AnimEvent var2) {
      if (!StringUtils.isNullOrWhitespace(var2.m_EventName)) {
         int var3 = var1.getDepth();
         this.actionContext.reportEvent(var3, var2.m_EventName);
      }
   }

   public AnimationPlayer getAnimationPlayer() {
      Model var1;
      if (this.isSkeleton()) {
         var1 = this.bFemale ? ModelManager.instance.m_skeletonFemaleModel : ModelManager.instance.m_skeletonMaleModel;
      } else {
         var1 = this.bFemale ? ModelManager.instance.m_femaleModel : ModelManager.instance.m_maleModel;
      }

      if (this.animPlayer != null && this.animPlayer.getModel() != var1) {
         this.animPlayer = (AnimationPlayer)Pool.tryRelease((IPooledObject)this.animPlayer);
      }

      if (this.animPlayer == null) {
         this.animPlayer = AnimationPlayer.alloc(var1);
      }

      return this.animPlayer;
   }

   public void actionStateChanged(ActionContext var1) {
      this.advancedAnimator.SetState(var1.getCurrentStateName(), PZArrayUtil.listConvert(var1.getChildStates(), (var0) -> {
         return var0.name;
      }));
   }

   public AnimationPlayerRecorder getAnimationPlayerRecorder() {
      return null;
   }

   public boolean isAnimationRecorderActive() {
      return false;
   }

   public ActionContext getActionContext() {
      return this.actionContext;
   }

   public AdvancedAnimator getAdvancedAnimator() {
      return this.advancedAnimator;
   }

   public ModelInstance getModelInstance() {
      return this.modelInstance;
   }

   public String GetAnimSetName() {
      return this.animSetName;
   }

   public String getUID() {
      return this.m_UID;
   }

   public static final class StateInfo {
      ModelInstance modelInstance;
      ModelInstanceTextureCreator textureCreator;
      final ArrayList instData = new ArrayList();
      final ArrayList readyData = new ArrayList();
      boolean bModelsReady;
      boolean bRendered;

      AnimatedModel.AnimatedModelInstanceRenderData getParentData(ModelInstance var1) {
         for(int var2 = 0; var2 < this.readyData.size(); ++var2) {
            AnimatedModel.AnimatedModelInstanceRenderData var3 = (AnimatedModel.AnimatedModelInstanceRenderData)this.readyData.get(var2);
            if (var3.modelInstance == var1.parent) {
               return var3;
            }
         }

         return null;
      }
   }

   private final class UIModelCamera extends ModelCamera {
      int x;
      int y;
      int w;
      int h;
      float sizeV;
      float m_animPlayerAngle;

      public void Begin() {
         GL11.glViewport(this.x, this.y, this.w, this.h);
         GL11.glMatrixMode(5889);
         GL11.glPushMatrix();
         GL11.glLoadIdentity();
         float var1 = (float)this.w / (float)this.h;
         if (AnimatedModel.this.flipY) {
            GL11.glOrtho((double)(-this.sizeV * var1), (double)(this.sizeV * var1), (double)this.sizeV, (double)(-this.sizeV), -100.0D, 100.0D);
         } else {
            GL11.glOrtho((double)(-this.sizeV * var1), (double)(this.sizeV * var1), (double)(-this.sizeV), (double)this.sizeV, -100.0D, 100.0D);
         }

         float var2 = Math.sqrt(2048.0F);
         GL11.glScalef(-var2, var2, var2);
         GL11.glMatrixMode(5888);
         GL11.glPushMatrix();
         GL11.glLoadIdentity();
         GL11.glTranslatef(AnimatedModel.this.offset.x(), AnimatedModel.this.offset.y(), AnimatedModel.this.offset.z());
         if (AnimatedModel.this.bIsometric) {
            GL11.glRotatef(30.0F, 1.0F, 0.0F, 0.0F);
            GL11.glRotated((double)(this.m_animPlayerAngle * 57.295776F + 45.0F), 0.0D, 1.0D, 0.0D);
         } else {
            GL11.glRotated((double)(this.m_animPlayerAngle * 57.295776F), 0.0D, 1.0D, 0.0D);
         }

      }

      public void End() {
         GL11.glMatrixMode(5889);
         GL11.glPopMatrix();
         GL11.glMatrixMode(5888);
         GL11.glPopMatrix();
      }
   }

   private static final class AnimatedModelInstanceRenderData {
      ModelInstance modelInstance;
      FloatBuffer matrixPalette;
      public final org.joml.Matrix4f xfrm = new org.joml.Matrix4f();
      float m_animPlayerAngle;

      AnimatedModel.AnimatedModelInstanceRenderData init(ModelInstance var1) {
         this.modelInstance = var1;
         this.xfrm.identity();
         this.m_animPlayerAngle = Float.NaN;
         if (var1.AnimPlayer != null) {
            this.m_animPlayerAngle = var1.AnimPlayer.getRenderedAngle();
            if (!var1.model.bStatic) {
               SkinningData var2 = (SkinningData)var1.model.Tag;
               if (Core.bDebug && var2 == null) {
                  DebugLog.General.warn("skinningData is null, matrixPalette may be invalid");
               }

               Matrix4f[] var3 = var1.AnimPlayer.getSkinTransforms(var2);
               if (this.matrixPalette == null || this.matrixPalette.capacity() < var3.length * 16) {
                  this.matrixPalette = BufferUtils.createFloatBuffer(var3.length * 16);
               }

               this.matrixPalette.clear();

               for(int var4 = 0; var4 < var3.length; ++var4) {
                  var3[var4].store(this.matrixPalette);
               }

               this.matrixPalette.flip();
            }
         }

         if (var1.getTextureInitializer() != null) {
            var1.getTextureInitializer().renderMain();
         }

         return this;
      }

      public AnimatedModel.AnimatedModelInstanceRenderData transformToParent(AnimatedModel.AnimatedModelInstanceRenderData var1) {
         if (!(this.modelInstance instanceof VehicleModelInstance) && !(this.modelInstance instanceof VehicleSubModelInstance)) {
            if (var1 == null) {
               return this;
            } else {
               this.xfrm.set((Matrix4fc)var1.xfrm);
               this.xfrm.transpose();
               org.joml.Matrix4f var2 = (org.joml.Matrix4f)((BaseVehicle.Matrix4fObjectPool)BaseVehicle.TL_matrix4f_pool.get()).alloc();
               ModelAttachment var3 = var1.modelInstance.getAttachmentById(this.modelInstance.attachmentNameParent);
               if (var3 == null) {
                  if (this.modelInstance.parentBoneName != null && var1.modelInstance.AnimPlayer != null) {
                     ModelInstanceRenderData.applyBoneTransform(var1.modelInstance, this.modelInstance.parentBoneName, this.xfrm);
                  }
               } else {
                  ModelInstanceRenderData.applyBoneTransform(var1.modelInstance, var3.getBone(), this.xfrm);
                  ModelInstanceRenderData.makeAttachmentTransform(var3, var2);
                  this.xfrm.mul((Matrix4fc)var2);
               }

               ModelAttachment var4 = this.modelInstance.getAttachmentById(this.modelInstance.attachmentNameSelf);
               if (var4 != null) {
                  ModelInstanceRenderData.makeAttachmentTransform(var4, var2);
                  var2.invert();
                  this.xfrm.mul((Matrix4fc)var2);
               }

               if (this.modelInstance.model.Mesh != null && this.modelInstance.model.Mesh.isReady() && this.modelInstance.model.Mesh.m_transform != null) {
                  this.xfrm.mul((Matrix4fc)this.modelInstance.model.Mesh.m_transform);
               }

               if (this.modelInstance.scale != 1.0F) {
                  this.xfrm.scale(this.modelInstance.scale);
               }

               this.xfrm.transpose();
               ((BaseVehicle.Matrix4fObjectPool)BaseVehicle.TL_matrix4f_pool.get()).release(var2);
               return this;
            }
         } else {
            return this;
         }
      }
   }

   private static final class WorldModelCamera extends ModelCamera {
      float x;
      float y;
      float z;
      float angle;

      public void Begin() {
         Core.getInstance().DoPushIsoStuff(this.x, this.y, this.z, this.angle, false);
         GL11.glDepthMask(true);
      }

      public void End() {
         Core.getInstance().DoPopIsoStuff();
      }
   }
}

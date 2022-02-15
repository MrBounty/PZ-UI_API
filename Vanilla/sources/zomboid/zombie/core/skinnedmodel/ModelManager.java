package zombie.core.skinnedmodel;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;
import java.util.Stack;
import java.util.TreeMap;
import java.util.Map.Entry;
import org.lwjgl.opengl.ARBFramebufferObject;
import org.lwjgl.opengl.ARBShaderObjects;
import org.lwjgl.opengl.EXTFramebufferObject;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;
import org.lwjglx.opengl.Display;
import org.lwjglx.opengl.Util;
import zombie.DebugFileWatcher;
import zombie.GameWindow;
import zombie.PredicatedFileWatcher;
import zombie.ZomboidFileSystem;
import zombie.asset.AssetPath;
import zombie.characters.IsoGameCharacter;
import zombie.characters.IsoPlayer;
import zombie.characters.IsoZombie;
import zombie.characters.AttachedItems.AttachedItem;
import zombie.characters.AttachedItems.AttachedModels;
import zombie.characters.CharacterTimedActions.BaseAction;
import zombie.core.Core;
import zombie.core.PerformanceSettings;
import zombie.core.SpriteRenderer;
import zombie.core.logger.ExceptionLogger;
import zombie.core.opengl.PZGLUtil;
import zombie.core.opengl.RenderThread;
import zombie.core.opengl.Shader;
import zombie.core.skinnedmodel.advancedanimation.AdvancedAnimator;
import zombie.core.skinnedmodel.animation.AnimationClip;
import zombie.core.skinnedmodel.animation.AnimationPlayer;
import zombie.core.skinnedmodel.animation.SoftwareSkinnedModelAnim;
import zombie.core.skinnedmodel.animation.StaticAnimation;
import zombie.core.skinnedmodel.model.AnimationAsset;
import zombie.core.skinnedmodel.model.AnimationAssetManager;
import zombie.core.skinnedmodel.model.MeshAssetManager;
import zombie.core.skinnedmodel.model.Model;
import zombie.core.skinnedmodel.model.ModelAssetManager;
import zombie.core.skinnedmodel.model.ModelInstance;
import zombie.core.skinnedmodel.model.ModelInstanceTextureInitializer;
import zombie.core.skinnedmodel.model.ModelMesh;
import zombie.core.skinnedmodel.model.SkinningData;
import zombie.core.skinnedmodel.model.VehicleModelInstance;
import zombie.core.skinnedmodel.model.VehicleSubModelInstance;
import zombie.core.skinnedmodel.population.PopTemplateManager;
import zombie.core.skinnedmodel.visual.ItemVisual;
import zombie.core.textures.Texture;
import zombie.core.textures.TextureDraw;
import zombie.core.textures.TextureFBO;
import zombie.core.textures.TextureID;
import zombie.debug.DebugLog;
import zombie.debug.DebugOptions;
import zombie.debug.DebugType;
import zombie.gameStates.ChooseGameInfo;
import zombie.inventory.InventoryItem;
import zombie.inventory.types.HandWeapon;
import zombie.inventory.types.WeaponPart;
import zombie.iso.FireShader;
import zombie.iso.IsoLightSource;
import zombie.iso.IsoMovingObject;
import zombie.iso.IsoPuddles;
import zombie.iso.IsoUtils;
import zombie.iso.IsoWater;
import zombie.iso.IsoWorld;
import zombie.iso.LightingJNI;
import zombie.iso.LosUtil;
import zombie.iso.ParticlesFire;
import zombie.iso.PlayerCamera;
import zombie.iso.PuddlesShader;
import zombie.iso.SmokeShader;
import zombie.iso.Vector2;
import zombie.iso.WaterShader;
import zombie.iso.sprite.SkyBox;
import zombie.network.GameClient;
import zombie.network.GameServer;
import zombie.network.ServerGUI;
import zombie.popman.ObjectPool;
import zombie.scripting.ScriptManager;
import zombie.scripting.objects.ItemReplacement;
import zombie.scripting.objects.ModelScript;
import zombie.scripting.objects.ModelWeaponPart;
import zombie.scripting.objects.VehicleScript;
import zombie.util.Lambda;
import zombie.util.StringUtils;
import zombie.util.Type;
import zombie.util.list.PZArrayUtil;
import zombie.vehicles.BaseVehicle;

public final class ModelManager {
   public static boolean NoOpenGL = false;
   public static final ModelManager instance = new ModelManager();
   private final HashMap m_modelMap = new HashMap();
   public Model m_maleModel;
   public Model m_femaleModel;
   public Model m_skeletonMaleModel;
   public Model m_skeletonFemaleModel;
   public TextureFBO bitmap;
   private boolean m_bCreated = false;
   public boolean bDebugEnableModels = true;
   public boolean bCreateSoftwareMeshes = false;
   public final HashMap SoftwareMeshAnims = new HashMap();
   private final ArrayList m_modelSlots = new ArrayList();
   private final ObjectPool m_modelInstancePool = new ObjectPool(ModelInstance::new);
   private ModelMesh m_animModel;
   private final HashMap m_animationAssets = new HashMap();
   private final ModelManager.ModAnimations m_gameAnimations = new ModelManager.ModAnimations("game");
   private final HashMap m_modAnimations = new HashMap();
   private final ArrayList m_cachedAnims = new ArrayList();
   private final HashSet m_contains = new HashSet();
   private final ArrayList m_torches = new ArrayList();
   private final Stack m_freeLights = new Stack();
   private final ArrayList m_torchLights = new ArrayList();
   private final ArrayList ToRemove = new ArrayList();
   private final ArrayList ToResetNextFrame = new ArrayList();
   private final ArrayList ToResetEquippedNextFrame = new ArrayList();
   private final ArrayList m_resetAfterRender = new ArrayList();
   private final Stack m_lights = new Stack();
   private final Stack m_lightsTemp = new Stack();
   private final Vector2 m_tempVec2 = new Vector2();
   private final Vector2 m_tempVec2_2 = new Vector2();
   private static final TreeMap modelMetaData;
   static String basicEffect;
   static String isStaticTrue;
   static String shaderEquals;
   static String texA;
   static String amp;
   static HashMap toLower;
   static HashMap toLowerTex;
   static HashMap toLowerKeyRoot;
   static StringBuilder builder;

   public boolean isCreated() {
      return this.m_bCreated;
   }

   public void create() {
      if (!this.m_bCreated) {
         if (!GameServer.bServer || ServerGUI.isCreated()) {
            Texture var1 = new Texture(1024, 1024, 16);
            PerformanceSettings.UseFBOs = false;

            try {
               this.bitmap = new TextureFBO(var1, false);
            } catch (Exception var9) {
               var9.printStackTrace();
               PerformanceSettings.UseFBOs = false;
               DebugLog.Animation.error("FBO not compatible with gfx card at this time.");
               return;
            }
         }

         DebugLog.Animation.println("Loading 3D models");
         ModelMesh.MeshAssetParams var10 = new ModelMesh.MeshAssetParams();
         var10.bStatic = false;
         var10.animationsMesh = null;
         ModelMesh var2 = (ModelMesh)MeshAssetManager.instance.load(new AssetPath("skinned/malebody"), var10);
         var2.m_animationsMesh = var2;
         this.m_modAnimations.put(this.m_gameAnimations.m_modID, this.m_gameAnimations);

         while(var2.isEmpty()) {
            GameWindow.fileSystem.updateAsyncTransactions();

            try {
               Thread.sleep(10L);
            } catch (InterruptedException var8) {
            }

            if (!GameServer.bServer) {
               Core.getInstance().StartFrame();
               Core.getInstance().EndFrame();
               Core.getInstance().StartFrameUI();
               Core.getInstance().EndFrameUI();
            }
         }

         try {
            this.loadAnimsFromDir("media/anims/", var2);
            this.loadAnimsFromDir("media/anims_X/", var2);
         } catch (Exception var7) {
            var7.printStackTrace();
         }

         if (!NoOpenGL && this.bCreateSoftwareMeshes) {
            SoftwareSkinnedModelAnim var3 = new SoftwareSkinnedModelAnim((StaticAnimation[])this.m_cachedAnims.toArray(new StaticAnimation[0]), var2.softwareMesh, var2.skinningData);
            this.SoftwareMeshAnims.put(var2.getPath().getPath(), var3);
         }

         Model var11 = this.loadModel("skinned/malebody", (String)null, var2);
         Model var4 = this.loadModel("skinned/femalebody", (String)null, var2);
         Model var5 = this.loadModel("skinned/Male_Skeleton", (String)null, var2);
         Model var6 = this.loadModel("skinned/Female_Skeleton", (String)null, var2);
         this.m_animModel = var2;
         this.loadModAnimations();
         var11.addDependency(this.getAnimationAssetRequired("bob/bob_idle"));
         var11.addDependency(this.getAnimationAssetRequired("bob/bob_walk"));
         var11.addDependency(this.getAnimationAssetRequired("bob/bob_run"));
         var4.addDependency(this.getAnimationAssetRequired("bob/bob_idle"));
         var4.addDependency(this.getAnimationAssetRequired("bob/bob_walk"));
         var4.addDependency(this.getAnimationAssetRequired("bob/bob_run"));
         this.m_animModel = var2;
         this.m_maleModel = var11;
         this.m_femaleModel = var4;
         this.m_skeletonMaleModel = var5;
         this.m_skeletonFemaleModel = var6;
         this.m_bCreated = true;
         AdvancedAnimator.systemInit();
         PopTemplateManager.instance.init();
      }
   }

   public void loadAdditionalModel(String var1, String var2, boolean var3, String var4) {
      boolean var5 = this.bCreateSoftwareMeshes;
      if (DebugLog.isEnabled(DebugType.Animation)) {
         DebugLog.Animation.debugln("createSoftwareMesh: %B, model: %s", var5, var1);
      }

      Model var6 = this.loadModelInternal(var1, var2, var4, this.m_animModel, var3);
      if (var5) {
         SoftwareSkinnedModelAnim var7 = new SoftwareSkinnedModelAnim((StaticAnimation[])this.m_cachedAnims.toArray(new StaticAnimation[0]), var6.softwareMesh, (SkinningData)var6.Tag);
         this.SoftwareMeshAnims.put(var1.toLowerCase(), var7);
      }

   }

   public ModelInstance newAdditionalModelInstance(String var1, String var2, IsoGameCharacter var3, AnimationPlayer var4, String var5) {
      Model var6 = this.tryGetLoadedModel(var1, var2, false, var5, false);
      if (var6 == null) {
         boolean var7 = false;
         instance.loadAdditionalModel(var1, var2, var7, var5);
      }

      var6 = this.getLoadedModel(var1, var2, false, var5);
      return this.newInstance(var6, var3, var4);
   }

   private void loadAnimsFromDir(String var1, ModelMesh var2) {
      File var3 = new File(ZomboidFileSystem.instance.base, var1);
      this.loadAnimsFromDir(ZomboidFileSystem.instance.baseURI, ZomboidFileSystem.instance.getMediaRootFile().toURI(), var3, var2, this.m_gameAnimations);
   }

   private void loadAnimsFromDir(URI var1, URI var2, File var3, ModelMesh var4, ModelManager.ModAnimations var5) {
      if (!var3.exists()) {
         DebugLog.General.error("ERROR: %s", var3.getPath());

         for(File var6 = var3.getParentFile(); var6 != null; var6 = var6.getParentFile()) {
            DebugLog.General.error(" - Parent exists: %B, %s", var6.exists(), var6.getPath());
         }
      }

      if (var3.isDirectory()) {
         File[] var13 = var3.listFiles();
         if (var13 != null) {
            boolean var7 = false;
            File[] var8 = var13;
            int var9 = var13.length;

            for(int var10 = 0; var10 < var9; ++var10) {
               File var11 = var8[var10];
               if (var11.isDirectory()) {
                  this.loadAnimsFromDir(var1, var2, var11, var4, var5);
               } else {
                  String var12 = ZomboidFileSystem.instance.getAnimName(var2, var11);
                  this.loadAnim(var12, var4, var5);
                  var7 = true;
                  if (!NoOpenGL && RenderThread.RenderThread == null) {
                     Display.processMessages();
                  }
               }
            }

            if (var7) {
               DebugFileWatcher.instance.add((new ModelManager.AnimDirReloader(var1, var2, var3.getPath(), var4, var5)).GetFileWatcher());
            }

         }
      }
   }

   public void RenderSkyBox(TextureDraw var1, int var2, int var3, int var4, int var5) {
      int var6 = TextureFBO.getCurrentID();
      switch(var4) {
      case 1:
         GL30.glBindFramebuffer(36160, var5);
         break;
      case 2:
         ARBFramebufferObject.glBindFramebuffer(36160, var5);
         break;
      case 3:
         EXTFramebufferObject.glBindFramebufferEXT(36160, var5);
      }

      GL11.glPushClientAttrib(-1);
      GL11.glPushAttrib(1048575);
      GL11.glMatrixMode(5889);
      GL11.glPushMatrix();
      GL11.glLoadIdentity();
      GL11.glOrtho(0.0D, 1.0D, 1.0D, 0.0D, -1.0D, 1.0D);
      GL11.glViewport(0, 0, 512, 512);
      GL11.glMatrixMode(5888);
      GL11.glPushMatrix();
      GL11.glLoadIdentity();
      ARBShaderObjects.glUseProgramObjectARB(var2);
      if (Shader.ShaderMap.containsKey(var2)) {
         ((Shader)Shader.ShaderMap.get(var2)).startRenderThread(var1);
      }

      GL11.glColor4f(0.13F, 0.96F, 0.13F, 1.0F);
      GL11.glBegin(7);
      GL11.glTexCoord2f(0.0F, 1.0F);
      GL11.glVertex2f(0.0F, 0.0F);
      GL11.glTexCoord2f(1.0F, 1.0F);
      GL11.glVertex2f(0.0F, 1.0F);
      GL11.glTexCoord2f(1.0F, 0.0F);
      GL11.glVertex2f(1.0F, 1.0F);
      GL11.glTexCoord2f(0.0F, 0.0F);
      GL11.glVertex2f(1.0F, 0.0F);
      GL11.glEnd();
      ARBShaderObjects.glUseProgramObjectARB(0);
      GL11.glMatrixMode(5888);
      GL11.glPopMatrix();
      GL11.glMatrixMode(5889);
      GL11.glPopMatrix();
      GL11.glPopAttrib();
      GL11.glPopClientAttrib();
      Texture.lastTextureID = -1;
      PlayerCamera var7 = SpriteRenderer.instance.getRenderingPlayerCamera(var3);
      GL11.glViewport(0, 0, var7.OffscreenWidth, var7.OffscreenHeight);
      switch(var4) {
      case 1:
         GL30.glBindFramebuffer(36160, var6);
         break;
      case 2:
         ARBFramebufferObject.glBindFramebuffer(36160, var6);
         break;
      case 3:
         EXTFramebufferObject.glBindFramebufferEXT(36160, var6);
      }

      SkyBox.getInstance().swapTextureFBO();
   }

   public void RenderWater(TextureDraw var1, int var2, int var3, boolean var4) {
      try {
         Util.checkGLError();
      } catch (Throwable var7) {
      }

      GL11.glPushClientAttrib(-1);
      GL11.glPushAttrib(1048575);
      GL11.glMatrixMode(5889);
      GL11.glPushMatrix();
      GL11.glLoadIdentity();
      IsoWater.getInstance().waterProjection();
      PlayerCamera var5 = SpriteRenderer.instance.getRenderingPlayerCamera(var3);
      GL11.glMatrixMode(5888);
      GL11.glPushMatrix();
      GL11.glLoadIdentity();
      ARBShaderObjects.glUseProgramObjectARB(var2);
      Shader var6 = (Shader)Shader.ShaderMap.get(var2);
      if (var6 instanceof WaterShader) {
         ((WaterShader)var6).updateWaterParams(var1, var3);
      }

      IsoWater.getInstance().waterGeometry(var4);
      ARBShaderObjects.glUseProgramObjectARB(0);
      GL11.glMatrixMode(5888);
      GL11.glPopMatrix();
      GL11.glMatrixMode(5889);
      GL11.glPopMatrix();
      GL11.glPopAttrib();
      GL11.glPopClientAttrib();
      Texture.lastTextureID = -1;
      if (!PZGLUtil.checkGLError(true)) {
         DebugLog.General.println("DEBUG: EXCEPTION RenderWater");
         PZGLUtil.printGLState(DebugLog.General);
      }

   }

   public void RenderPuddles(int var1, int var2, int var3) {
      PZGLUtil.checkGLError(true);
      GL11.glPushClientAttrib(-1);
      GL11.glPushAttrib(1048575);
      GL11.glMatrixMode(5889);
      GL11.glPushMatrix();
      GL11.glLoadIdentity();
      IsoPuddles.getInstance().puddlesProjection();
      GL11.glMatrixMode(5888);
      GL11.glPushMatrix();
      GL11.glLoadIdentity();
      ARBShaderObjects.glUseProgramObjectARB(var1);
      Shader var4 = (Shader)Shader.ShaderMap.get(var1);
      if (var4 instanceof PuddlesShader) {
         ((PuddlesShader)var4).updatePuddlesParams(var2, var3);
      }

      IsoPuddles.getInstance().puddlesGeometry(var3);
      ARBShaderObjects.glUseProgramObjectARB(0);
      GL11.glMatrixMode(5888);
      GL11.glPopMatrix();
      GL11.glMatrixMode(5889);
      GL11.glPopMatrix();
      GL11.glPopAttrib();
      GL11.glPopClientAttrib();
      Texture.lastTextureID = -1;
      if (!PZGLUtil.checkGLError(true)) {
         DebugLog.General.println("DEBUG: EXCEPTION RenderPuddles");
         PZGLUtil.printGLState(DebugLog.General);
      }

   }

   public void RenderParticles(TextureDraw var1, int var2, int var3) {
      int var4 = ParticlesFire.getInstance().getFireShaderID();
      int var5 = ParticlesFire.getInstance().getSmokeShaderID();
      int var6 = ParticlesFire.getInstance().getVapeShaderID();

      try {
         Util.checkGLError();
      } catch (Throwable var9) {
      }

      GL11.glPushClientAttrib(-1);
      GL11.glPushAttrib(1048575);
      GL11.glMatrixMode(5889);
      GL11.glPushMatrix();
      GL11.glLoadIdentity();
      GL11.glViewport(0, 0, SpriteRenderer.instance.getRenderingPlayerCamera(var2).OffscreenWidth, SpriteRenderer.instance.getRenderingPlayerCamera(var2).OffscreenHeight);
      GL11.glMatrixMode(5888);
      GL11.glPushMatrix();
      GL11.glLoadIdentity();
      float var7 = ParticlesFire.getInstance().getShaderTime();
      GL11.glBlendFunc(770, 1);
      ARBShaderObjects.glUseProgramObjectARB(var4);
      Shader var8 = (Shader)Shader.ShaderMap.get(var4);
      if (var8 instanceof FireShader) {
         ((FireShader)var8).updateFireParams(var1, var2, var7);
      }

      ParticlesFire.getInstance().getGeometryFire(var3);
      GL11.glBlendFunc(770, 771);
      ARBShaderObjects.glUseProgramObjectARB(var5);
      var8 = (Shader)Shader.ShaderMap.get(var5);
      if (var8 instanceof SmokeShader) {
         ((SmokeShader)var8).updateSmokeParams(var1, var2, var7);
      }

      ParticlesFire.getInstance().getGeometry(var3);
      ARBShaderObjects.glUseProgramObjectARB(0);
      GL11.glMatrixMode(5888);
      GL11.glPopMatrix();
      GL11.glMatrixMode(5889);
      GL11.glPopMatrix();
      GL11.glPopAttrib();
      GL11.glPopClientAttrib();
      Texture.lastTextureID = -1;
      GL11.glViewport(0, 0, SpriteRenderer.instance.getRenderingPlayerCamera(var2).OffscreenWidth, SpriteRenderer.instance.getRenderingPlayerCamera(var2).OffscreenHeight);
      if (!PZGLUtil.checkGLError(true)) {
         DebugLog.General.println("DEBUG: EXCEPTION RenderParticles");
         PZGLUtil.printGLState(DebugLog.General);
      }

   }

   public void Reset(IsoGameCharacter var1) {
      if (var1.legsSprite != null && var1.legsSprite.modelSlot != null) {
         ModelManager.ModelSlot var2 = var1.legsSprite.modelSlot;
         this.resetModelInstance(var2.model, var2);

         for(int var3 = 0; var3 < var2.sub.size(); ++var3) {
            ModelInstance var4 = (ModelInstance)var2.sub.get(var3);
            if (var4 != var1.primaryHandModel && var4 != var1.secondaryHandModel && !var2.attachedModels.contains(var4)) {
               this.resetModelInstanceRecurse(var4, var2);
            }
         }

         this.derefModelInstances(var1.getReadyModelData());
         var1.getReadyModelData().clear();
         this.dressInRandomOutfit(var1);
         Model var5 = this.getBodyModel(var1);
         var2.model = this.newInstance(var5, var1, var1.getAnimationPlayer());
         var2.model.setOwner(var2);
         var2.model.m_modelScript = ScriptManager.instance.getModelScript(var1.isFemale() ? "FemaleBody" : "MaleBody");
         this.DoCharacterModelParts(var1, var2);
      }
   }

   public void reloadAllOutfits() {
      Iterator var1 = this.m_contains.iterator();

      while(var1.hasNext()) {
         IsoGameCharacter var2 = (IsoGameCharacter)var1.next();
         var2.reloadOutfit();
      }

   }

   public void Add(IsoGameCharacter var1) {
      if (this.m_bCreated) {
         if (var1.isSceneCulled()) {
            if (this.ToRemove.contains(var1)) {
               this.ToRemove.remove(var1);
               var1.legsSprite.modelSlot.bRemove = false;
            } else {
               ModelManager.ModelSlot var2 = this.getSlot(var1);
               var2.framesSinceStart = 0;
               if (var2.model != null) {
                  ModelInstance var10000 = var2.model;
                  Objects.requireNonNull(var10000);
                  RenderThread.invokeOnRenderContext(var10000::destroySmartTextures);
               }

               this.dressInRandomOutfit(var1);
               Model var3 = this.getBodyModel(var1);
               var2.model = this.newInstance(var3, var1, var1.getAnimationPlayer());
               var2.model.setOwner(var2);
               var2.model.m_modelScript = ScriptManager.instance.getModelScript(var1.isFemale() ? "FemaleBody" : "MaleBody");
               this.DoCharacterModelParts(var1, var2);
               var2.active = true;
               var2.character = var1;
               var2.model.character = var1;
               var2.model.object = var1;
               var2.model.SetForceDir(var2.model.character.getForwardDirection());

               for(int var4 = 0; var4 < var2.sub.size(); ++var4) {
                  ModelInstance var5 = (ModelInstance)var2.sub.get(var4);
                  var5.character = var1;
                  var5.object = var1;
               }

               var1.legsSprite.modelSlot = var2;
               this.m_contains.add(var1);
               var1.onCullStateChanged(this, false);
               if (var2.model.AnimPlayer != null && var2.model.AnimPlayer.isBoneTransformsNeedFirstFrame()) {
                  try {
                     var2.Update();
                  } catch (Throwable var6) {
                     ExceptionLogger.logException(var6);
                  }
               }

            }
         }
      }
   }

   public void dressInRandomOutfit(IsoGameCharacter var1) {
      IsoZombie var2 = (IsoZombie)Type.tryCastTo(var1, IsoZombie.class);
      if (var2 != null && !var2.isReanimatedPlayer() && !var2.wasFakeDead()) {
         if (DebugOptions.instance.ZombieOutfitRandom.getValue() && !var1.isPersistentOutfitInit()) {
            var2.bDressInRandomOutfit = true;
         }

         if (var2.bDressInRandomOutfit) {
            var2.bDressInRandomOutfit = false;
            var2.dressInRandomOutfit();
         }

         if (!var1.isPersistentOutfitInit()) {
            var2.dressInPersistentOutfitID(var1.getPersistentOutfitID());
         }

      } else {
         if (GameClient.bClient && var2 != null && !var1.isPersistentOutfitInit() && var1.getPersistentOutfitID() != 0) {
            var2.dressInPersistentOutfitID(var1.getPersistentOutfitID());
         }

      }
   }

   public Model getBodyModel(IsoGameCharacter var1) {
      if (var1.isZombie() && ((IsoZombie)var1).isSkeleton()) {
         return var1.isFemale() ? this.m_skeletonFemaleModel : this.m_skeletonMaleModel;
      } else {
         return var1.isFemale() ? this.m_femaleModel : this.m_maleModel;
      }
   }

   public boolean ContainsChar(IsoGameCharacter var1) {
      return this.m_contains.contains(var1) && !this.ToRemove.contains(var1);
   }

   public void ResetCharacterEquippedHands(IsoGameCharacter var1) {
      if (var1 != null && var1.legsSprite != null && var1.legsSprite.modelSlot != null) {
         this.DoCharacterModelEquipped(var1, var1.legsSprite.modelSlot);
      }
   }

   private void DoCharacterModelEquipped(IsoGameCharacter var1, ModelManager.ModelSlot var2) {
      if (var1.primaryHandModel != null) {
         var1.clearVariable("RightHandMask");
         var1.primaryHandModel.maskVariableValue = null;
         this.resetModelInstanceRecurse(var1.primaryHandModel, var2);
         var2.sub.remove(var1.primaryHandModel);
         var2.model.sub.remove(var1.primaryHandModel);
         var1.primaryHandModel = null;
      }

      if (var1.secondaryHandModel != null) {
         var1.clearVariable("LeftHandMask");
         var1.secondaryHandModel.maskVariableValue = null;
         this.resetModelInstanceRecurse(var1.secondaryHandModel, var2);
         var2.sub.remove(var1.secondaryHandModel);
         var2.model.sub.remove(var1.secondaryHandModel);
         var1.secondaryHandModel = null;
      }

      int var3;
      for(var3 = 0; var3 < var2.attachedModels.size(); ++var3) {
         ModelInstance var4 = (ModelInstance)var2.attachedModels.get(var3);
         this.resetModelInstanceRecurse(var4, var2);
         var2.sub.remove(var4);
         var2.model.sub.remove(var4);
      }

      var2.attachedModels.clear();

      for(var3 = 0; var3 < var1.getAttachedItems().size(); ++var3) {
         AttachedItem var10 = var1.getAttachedItems().get(var3);
         String var5 = var10.getItem().getStaticModel();
         if (!StringUtils.isNullOrWhitespace(var5)) {
            String var6 = var1.getAttachedItems().getGroup().getLocation(var10.getLocation()).getAttachmentName();
            ModelInstance var7 = this.addStatic(var2.model, var5, var6, var6);
            if (var7 != null) {
               var7.setOwner(var2);
               var2.sub.add(var7);
               HandWeapon var8 = (HandWeapon)Type.tryCastTo(var10.getItem(), HandWeapon.class);
               if (var8 != null) {
                  this.addWeaponPartModels(var2, var8, var7);
                  if (!Core.getInstance().getOptionSimpleWeaponTextures()) {
                     ModelInstanceTextureInitializer var9 = ModelInstanceTextureInitializer.alloc();
                     var9.init(var7, var8);
                     var7.setTextureInitializer(var9);
                  }
               }

               var2.attachedModels.add(var7);
            }
         }
      }

      if (var1 instanceof IsoZombie) {
      }

      InventoryItem var11 = var1.getPrimaryHandItem();
      InventoryItem var12 = var1.getSecondaryHandItem();
      if (var1.isHideWeaponModel()) {
         var11 = null;
         var12 = null;
      }

      if (var1 instanceof IsoPlayer && var1.forceNullOverride) {
         var11 = null;
         var12 = null;
         var1.forceNullOverride = false;
      }

      boolean var13 = false;
      BaseAction var14 = var1.getCharacterActions().isEmpty() ? null : (BaseAction)var1.getCharacterActions().get(0);
      if (var14 != null && var14.overrideHandModels) {
         var13 = true;
         var11 = null;
         if (var14.getPrimaryHandItem() != null) {
            var11 = var14.getPrimaryHandItem();
         } else if (var14.getPrimaryHandMdl() != null) {
            var1.primaryHandModel = this.addStatic(var2, var14.getPrimaryHandMdl(), "Bip01_Prop1");
         }

         var12 = null;
         if (var14.getSecondaryHandItem() != null) {
            var12 = var14.getSecondaryHandItem();
         } else if (var14.getSecondaryHandMdl() != null) {
            var1.secondaryHandModel = this.addStatic(var2, var14.getSecondaryHandMdl(), "Bip01_Prop2");
         }
      }

      if (!StringUtils.isNullOrEmpty(var1.overridePrimaryHandModel)) {
         var13 = true;
         var1.primaryHandModel = this.addStatic(var2, var1.overridePrimaryHandModel, "Bip01_Prop1");
      }

      if (!StringUtils.isNullOrEmpty(var1.overrideSecondaryHandModel)) {
         var13 = true;
         var1.secondaryHandModel = this.addStatic(var2, var1.overrideSecondaryHandModel, "Bip01_Prop2");
      }

      ItemReplacement var15;
      if (var11 != null) {
         var15 = var11.getItemReplacementPrimaryHand();
         var1.primaryHandModel = this.addEquippedModelInstance(var1, var2, var11, "Bip01_Prop1", var15, var13);
      }

      if (var12 != null && var11 != var12) {
         var15 = var12.getItemReplacementSecondHand();
         var1.secondaryHandModel = this.addEquippedModelInstance(var1, var2, var12, "Bip01_Prop2", var15, var13);
      }

   }

   private ModelInstance addEquippedModelInstance(IsoGameCharacter var1, ModelManager.ModelSlot var2, InventoryItem var3, String var4, ItemReplacement var5, boolean var6) {
      HandWeapon var8 = (HandWeapon)Type.tryCastTo(var3, HandWeapon.class);
      ModelInstance var7;
      if (var8 != null) {
         String var9 = var8.getStaticModel();
         var7 = this.addStatic(var2, var9, var4);
         this.addWeaponPartModels(var2, var8, var7);
         if (Core.getInstance().getOptionSimpleWeaponTextures()) {
            return var7;
         } else {
            ModelInstanceTextureInitializer var10 = ModelInstanceTextureInitializer.alloc();
            var10.init(var7, var8);
            var7.setTextureInitializer(var10);
            return var7;
         }
      } else {
         if (var3 != null) {
            if (var5 != null && !StringUtils.isNullOrEmpty(var5.maskVariableValue) && (var5.clothingItem != null || !StringUtils.isNullOrWhitespace(var3.getStaticModel()))) {
               var7 = this.addMaskingModel(var2, var1, var3, var5, var5.maskVariableValue, var5.attachment, var4);
               return var7;
            }

            if (var6 && !StringUtils.isNullOrWhitespace(var3.getStaticModel())) {
               var7 = this.addStatic(var2, var3.getStaticModel(), var4);
               return var7;
            }
         }

         return null;
      }
   }

   private ModelInstance addMaskingModel(ModelManager.ModelSlot var1, IsoGameCharacter var2, InventoryItem var3, ItemReplacement var4, String var5, String var6, String var7) {
      ModelInstance var8 = null;
      ItemVisual var9 = var3.getVisual();
      if (var4.clothingItem != null && var9 != null) {
         var8 = PopTemplateManager.instance.addClothingItem(var2, var1, var9, var4.clothingItem);
      } else {
         if (StringUtils.isNullOrWhitespace(var3.getStaticModel())) {
            return null;
         }

         String var10 = null;
         if (var9 != null && var3.getClothingItem() != null) {
            var10 = (String)var3.getClothingItem().getTextureChoices().get(var9.getTextureChoice());
         }

         if (!StringUtils.isNullOrEmpty(var6)) {
            var8 = this.addStaticForcedTex(var1.model, var3.getStaticModel(), var6, var6, var10);
         } else {
            var8 = this.addStaticForcedTex(var1, var3.getStaticModel(), var7, var10);
         }

         var8.maskVariableValue = var5;
         if (var9 != null) {
            var8.tintR = var9.m_Tint.r;
            var8.tintG = var9.m_Tint.g;
            var8.tintB = var9.m_Tint.b;
         }
      }

      if (!StringUtils.isNullOrEmpty(var5)) {
         var2.setVariable(var4.maskVariableName, var5);
         var2.bUpdateEquippedTextures = true;
      }

      return var8;
   }

   private void addWeaponPartModels(ModelManager.ModelSlot var1, HandWeapon var2, ModelInstance var3) {
      ArrayList var4 = var2.getModelWeaponPart();
      if (var4 != null) {
         ArrayList var5 = var2.getAllWeaponParts();

         for(int var6 = 0; var6 < var5.size(); ++var6) {
            WeaponPart var7 = (WeaponPart)var5.get(var6);

            for(int var8 = 0; var8 < var4.size(); ++var8) {
               ModelWeaponPart var9 = (ModelWeaponPart)var4.get(var8);
               if (var7.getFullType().equals(var9.partType)) {
                  ModelInstance var10 = this.addStatic(var3, var9.modelName, var9.attachmentNameSelf, var9.attachmentParent);
                  var10.setOwner(var1);
               }
            }
         }
      }

   }

   public void resetModelInstance(ModelInstance var1, Object var2) {
      if (var1 != null) {
         var1.clearOwner(var2);
         if (var1.isRendering()) {
            var1.bResetAfterRender = true;
         } else {
            if (var1 instanceof VehicleModelInstance) {
               return;
            }

            if (var1 instanceof VehicleSubModelInstance) {
               return;
            }

            var1.reset();
            this.m_modelInstancePool.release((Object)var1);
         }

      }
   }

   public void resetModelInstanceRecurse(ModelInstance var1, Object var2) {
      if (var1 != null) {
         this.resetModelInstancesRecurse(var1.sub, var2);
         this.resetModelInstance(var1, var2);
      }
   }

   public void resetModelInstancesRecurse(ArrayList var1, Object var2) {
      for(int var3 = 0; var3 < var1.size(); ++var3) {
         ModelInstance var4 = (ModelInstance)var1.get(var3);
         this.resetModelInstance(var4, var2);
      }

   }

   public void derefModelInstance(ModelInstance var1) {
      if (var1 != null) {
         assert var1.renderRefCount > 0;

         --var1.renderRefCount;
         if (var1.bResetAfterRender && !var1.isRendering()) {
            assert var1.getOwner() == null;

            if (var1 instanceof VehicleModelInstance) {
               return;
            }

            if (var1 instanceof VehicleSubModelInstance) {
               return;
            }

            var1.reset();
            this.m_modelInstancePool.release((Object)var1);
         }

      }
   }

   public void derefModelInstances(ArrayList var1) {
      for(int var2 = 0; var2 < var1.size(); ++var2) {
         ModelInstance var3 = (ModelInstance)var1.get(var2);
         this.derefModelInstance(var3);
      }

   }

   private void DoCharacterModelParts(IsoGameCharacter var1, ModelManager.ModelSlot var2) {
      if (var2.isRendering()) {
         boolean var3 = false;
      }

      if (DebugLog.isEnabled(DebugType.Clothing)) {
         DebugLog.Clothing.debugln("Char: " + var1 + " Slot: " + var2);
      }

      var2.sub.clear();
      PopTemplateManager.instance.populateCharacterModelSlot(var1, var2);
      this.DoCharacterModelEquipped(var1, var2);
   }

   public void update() {
      int var1;
      IsoGameCharacter var2;
      for(var1 = 0; var1 < this.ToResetNextFrame.size(); ++var1) {
         var2 = (IsoGameCharacter)this.ToResetNextFrame.get(var1);
         this.Reset(var2);
      }

      this.ToResetNextFrame.clear();

      for(var1 = 0; var1 < this.ToResetEquippedNextFrame.size(); ++var1) {
         var2 = (IsoGameCharacter)this.ToResetEquippedNextFrame.get(var1);
         this.ResetCharacterEquippedHands(var2);
      }

      this.ToResetEquippedNextFrame.clear();

      for(var1 = 0; var1 < this.ToRemove.size(); ++var1) {
         var2 = (IsoGameCharacter)this.ToRemove.get(var1);
         this.DoRemove(var2);
      }

      this.ToRemove.clear();

      for(var1 = 0; var1 < this.m_resetAfterRender.size(); ++var1) {
         ModelManager.ModelSlot var4 = (ModelManager.ModelSlot)this.m_resetAfterRender.get(var1);
         if (!var4.isRendering()) {
            var4.reset();
            this.m_resetAfterRender.remove(var1--);
         }
      }

      this.m_lights.clear();
      if (IsoWorld.instance != null && IsoWorld.instance.CurrentCell != null) {
         this.m_lights.addAll(IsoWorld.instance.CurrentCell.getLamppostPositions());
         ArrayList var8 = IsoWorld.instance.CurrentCell.getVehicles();

         for(int var5 = 0; var5 < var8.size(); ++var5) {
            BaseVehicle var3 = (BaseVehicle)var8.get(var5);
            if (var3.sprite != null && var3.sprite.hasActiveModel()) {
               ((VehicleModelInstance)var3.sprite.modelSlot.model).UpdateLights();
            }
         }
      }

      this.m_freeLights.addAll(this.m_torchLights);
      this.m_torchLights.clear();
      this.m_torches.clear();
      LightingJNI.getTorches(this.m_torches);

      for(var1 = 0; var1 < this.m_torches.size(); ++var1) {
         IsoGameCharacter.TorchInfo var7 = (IsoGameCharacter.TorchInfo)this.m_torches.get(var1);
         IsoLightSource var6 = this.m_freeLights.isEmpty() ? new IsoLightSource(0, 0, 0, 1.0F, 1.0F, 1.0F, 1) : (IsoLightSource)this.m_freeLights.pop();
         var6.x = (int)var7.x;
         var6.y = (int)var7.y;
         var6.z = (int)var7.z;
         var6.r = 1.0F;
         var6.g = 0.85F;
         var6.b = 0.6F;
         var6.radius = (int)Math.ceil((double)var7.dist);
         this.m_torchLights.add(var6);
      }

   }

   private ModelManager.ModelSlot addNewSlot(IsoGameCharacter var1) {
      ModelManager.ModelSlot var2 = new ModelManager.ModelSlot(this.m_modelSlots.size(), (ModelInstance)null, var1);
      this.m_modelSlots.add(var2);
      return var2;
   }

   public ModelManager.ModelSlot getSlot(IsoGameCharacter var1) {
      for(int var2 = 0; var2 < this.m_modelSlots.size(); ++var2) {
         ModelManager.ModelSlot var3 = (ModelManager.ModelSlot)this.m_modelSlots.get(var2);
         if (!var3.bRemove && !var3.isRendering() && !var3.active) {
            return var3;
         }
      }

      return this.addNewSlot(var1);
   }

   private boolean DoRemove(IsoGameCharacter var1) {
      if (!this.m_contains.contains(var1)) {
         return false;
      } else {
         boolean var2 = false;

         for(int var3 = 0; var3 < this.m_modelSlots.size(); ++var3) {
            ModelManager.ModelSlot var4 = (ModelManager.ModelSlot)this.m_modelSlots.get(var3);
            if (var4.character == var1) {
               var1.legsSprite.modelSlot = null;
               this.m_contains.remove(var1);
               if (!var1.isSceneCulled()) {
                  var1.onCullStateChanged(this, true);
               }

               if (!this.m_resetAfterRender.contains(var4)) {
                  this.m_resetAfterRender.add(var4);
               }

               var2 = true;
            }
         }

         return var2;
      }
   }

   public void Remove(IsoGameCharacter var1) {
      if (!var1.isSceneCulled()) {
         if (!this.ToRemove.contains(var1)) {
            var1.legsSprite.modelSlot.bRemove = true;
            this.ToRemove.add(var1);
            var1.onCullStateChanged(this, true);
         } else if (this.ContainsChar(var1)) {
            throw new IllegalStateException("IsoGameCharacter.isSceneCulled() = true inconsistent with ModelManager.ContainsChar() = true");
         }

      }
   }

   public void Remove(BaseVehicle var1) {
      if (var1.sprite != null && var1.sprite.modelSlot != null) {
         ModelManager.ModelSlot var2 = var1.sprite.modelSlot;
         if (!this.m_resetAfterRender.contains(var2)) {
            this.m_resetAfterRender.add(var2);
         }

         var1.sprite.modelSlot = null;
      }

   }

   public void ResetNextFrame(IsoGameCharacter var1) {
      if (!this.ToResetNextFrame.contains(var1)) {
         this.ToResetNextFrame.add(var1);
      }
   }

   public void ResetEquippedNextFrame(IsoGameCharacter var1) {
      if (!this.ToResetEquippedNextFrame.contains(var1)) {
         this.ToResetEquippedNextFrame.add(var1);
      }
   }

   public void Reset() {
      RenderThread.invokeOnRenderContext(() -> {
         Iterator var1 = this.ToRemove.iterator();

         while(var1.hasNext()) {
            IsoGameCharacter var2 = (IsoGameCharacter)var1.next();
            this.DoRemove(var2);
         }

         this.ToRemove.clear();

         try {
            if (!this.m_contains.isEmpty()) {
               IsoGameCharacter[] var7 = (IsoGameCharacter[])this.m_contains.toArray(new IsoGameCharacter[0]);
               IsoGameCharacter[] var8 = var7;
               int var3 = var7.length;

               for(int var4 = 0; var4 < var3; ++var4) {
                  IsoGameCharacter var5 = var8[var4];
                  this.DoRemove(var5);
               }
            }

            this.m_modelSlots.clear();
         } catch (Exception var6) {
            DebugLog.Animation.error("Exception thrown removing Models.");
            var6.printStackTrace();
         }

      });
      this.m_lights.clear();
      this.m_lightsTemp.clear();
   }

   public void getClosestThreeLights(IsoMovingObject var1, IsoLightSource[] var2) {
      this.m_lightsTemp.clear();
      Iterator var3 = this.m_lights.iterator();

      while(true) {
         IsoLightSource var4;
         do {
            do {
               do {
                  if (!var3.hasNext()) {
                     if (var1 instanceof BaseVehicle) {
                        for(int var8 = 0; var8 < this.m_torches.size(); ++var8) {
                           IsoGameCharacter.TorchInfo var9 = (IsoGameCharacter.TorchInfo)this.m_torches.get(var8);
                           if (!(IsoUtils.DistanceTo(var1.x, var1.y, var9.x, var9.y) >= var9.dist) && LosUtil.lineClear(IsoWorld.instance.CurrentCell, (int)var1.x, (int)var1.y, (int)var1.z, (int)var9.x, (int)var9.y, (int)var9.z, false) != LosUtil.TestResults.Blocked) {
                              if (var9.bCone) {
                                 Vector2 var5 = this.m_tempVec2;
                                 var5.x = var9.x - var1.x;
                                 var5.y = var9.y - var1.y;
                                 var5.normalize();
                                 Vector2 var6 = this.m_tempVec2_2;
                                 var6.x = var9.angleX;
                                 var6.y = var9.angleY;
                                 var6.normalize();
                                 float var7 = var5.dot(var6);
                                 if (var7 >= -0.92F) {
                                    continue;
                                 }
                              }

                              this.m_lightsTemp.add((IsoLightSource)this.m_torchLights.get(var8));
                           }
                        }
                     }

                     PZArrayUtil.sort(this.m_lightsTemp, Lambda.comparator(var1, (var0, var1x, var2x) -> {
                        float var3 = var2x.DistTo(var0.x, var0.y);
                        float var4 = var2x.DistTo(var1x.x, var1x.y);
                        if (var3 > var4) {
                           return 1;
                        } else {
                           return var3 < var4 ? -1 : 0;
                        }
                     }));
                     var2[0] = var2[1] = var2[2] = null;
                     if (this.m_lightsTemp.size() > 0) {
                        var2[0] = (IsoLightSource)this.m_lightsTemp.get(0);
                     }

                     if (this.m_lightsTemp.size() > 1) {
                        var2[1] = (IsoLightSource)this.m_lightsTemp.get(1);
                     }

                     if (this.m_lightsTemp.size() > 2) {
                        var2[2] = (IsoLightSource)this.m_lightsTemp.get(2);
                     }

                     return;
                  }

                  var4 = (IsoLightSource)var3.next();
               } while(!var4.bActive);
            } while(var4.life == 0);
         } while(var4.localToBuilding != null && var1.getCurrentBuilding() != var4.localToBuilding);

         if (!(IsoUtils.DistanceTo(var1.x, var1.y, (float)var4.x + 0.5F, (float)var4.y + 0.5F) >= (float)var4.radius) && LosUtil.lineClear(IsoWorld.instance.CurrentCell, (int)var1.x, (int)var1.y, (int)var1.z, var4.x, var4.y, var4.z, false) != LosUtil.TestResults.Blocked) {
            this.m_lightsTemp.add(var4);
         }
      }
   }

   public void addVehicle(BaseVehicle var1) {
      if (this.m_bCreated) {
         if (!GameServer.bServer || ServerGUI.isCreated()) {
            if (var1 != null && var1.getScript() != null) {
               VehicleScript var2 = var1.getScript();
               String var3 = var1.getScript().getModel().file;
               Model var4 = this.getLoadedModel(var3);
               if (var4 == null) {
                  DebugLog.Animation.error("Failed to find vehicle model: %s", var3);
               } else {
                  if (DebugLog.isEnabled(DebugType.Animation)) {
                     DebugLog.Animation.debugln("%s", var3);
                  }

                  VehicleModelInstance var5 = new VehicleModelInstance();
                  var5.init(var4, (IsoGameCharacter)null, var1.getAnimationPlayer());
                  var5.applyModelScriptScale(var3);
                  var1.getSkin();
                  VehicleScript.Skin var6 = var2.getTextures();
                  if (var1.getSkinIndex() >= 0 && var1.getSkinIndex() < var2.getSkinCount()) {
                     var6 = var2.getSkin(var1.getSkinIndex());
                  }

                  var5.LoadTexture(var6.texture);
                  var5.tex = var6.textureData;
                  var5.textureMask = var6.textureDataMask;
                  var5.textureDamage1Overlay = var6.textureDataDamage1Overlay;
                  var5.textureDamage1Shell = var6.textureDataDamage1Shell;
                  var5.textureDamage2Overlay = var6.textureDataDamage2Overlay;
                  var5.textureDamage2Shell = var6.textureDataDamage2Shell;
                  var5.textureLights = var6.textureDataLights;
                  var5.textureRust = var6.textureDataRust;
                  if (var5.tex != null) {
                     var5.tex.bindAlways = true;
                  } else {
                     DebugLog.Animation.error("texture not found:", var1.getSkin());
                  }

                  ModelManager.ModelSlot var7 = this.getSlot((IsoGameCharacter)null);
                  var7.model = var5;
                  var5.setOwner(var7);
                  var5.object = var1;
                  var7.sub.clear();

                  for(int var8 = 0; var8 < var1.models.size(); ++var8) {
                     BaseVehicle.ModelInfo var9 = (BaseVehicle.ModelInfo)var1.models.get(var8);
                     Model var10 = this.getLoadedModel(var9.scriptModel.file);
                     if (var10 == null) {
                        DebugLog.Animation.error("vehicle.models[%d] not found: %s", var8, var9.scriptModel.file);
                     } else {
                        VehicleSubModelInstance var11 = new VehicleSubModelInstance();
                        var11.init(var10, (IsoGameCharacter)null, var9.getAnimationPlayer());
                        var11.setOwner(var7);
                        var11.applyModelScriptScale(var9.scriptModel.file);
                        var11.object = var1;
                        var11.parent = var5;
                        var5.sub.add(var11);
                        var11.modelInfo = var9;
                        if (var11.tex == null) {
                           var11.tex = var5.tex;
                        }

                        var7.sub.add(var11);
                        var9.modelInstance = var11;
                     }
                  }

                  var7.active = true;
                  var1.sprite.modelSlot = var7;
               }
            }
         }
      }
   }

   public ModelInstance addStatic(ModelManager.ModelSlot var1, String var2, String var3, String var4, String var5) {
      ModelInstance var6 = this.newStaticInstance(var1, var2, var3, var4, var5);
      if (var6 == null) {
         return null;
      } else {
         var1.sub.add(var6);
         var6.setOwner(var1);
         var1.model.sub.add(var6);
         return var6;
      }
   }

   public ModelInstance newStaticInstance(ModelManager.ModelSlot var1, String var2, String var3, String var4, String var5) {
      if (DebugLog.isEnabled(DebugType.Animation)) {
         DebugLog.Animation.debugln("Adding Static Model:" + var2);
      }

      Model var6 = this.tryGetLoadedModel(var2, var3, true, var5, false);
      if (var6 == null && var2 != null) {
         this.loadStaticModel(var2, var3, var5);
         var6 = this.getLoadedModel(var2, var3, true, var5);
         if (var6 == null) {
            if (DebugLog.isEnabled(DebugType.Animation)) {
               DebugLog.Animation.error("Model not found. model:" + var2 + " tex:" + var3);
            }

            return null;
         }
      }

      if (var2 == null) {
         var6 = this.tryGetLoadedModel("vehicles_wheel02", "vehicles/vehicle_wheel02", true, "vehiclewheel", false);
      }

      ModelInstance var7 = this.newInstance(var6, var1.character, var1.model.AnimPlayer);
      var7.parent = var1.model;
      if (var1.model.AnimPlayer != null) {
         var7.parentBone = var1.model.AnimPlayer.getSkinningBoneIndex(var4, var7.parentBone);
         var7.parentBoneName = var4;
      }

      var7.AnimPlayer = var1.model.AnimPlayer;
      return var7;
   }

   private ModelInstance addStatic(ModelManager.ModelSlot var1, String var2, String var3) {
      return this.addStaticForcedTex(var1, var2, var3, (String)null);
   }

   private ModelInstance addStaticForcedTex(ModelManager.ModelSlot var1, String var2, String var3, String var4) {
      String var5 = var2;
      String var6 = var2;
      String var7 = null;
      ModelManager.ModelMetaData var8 = (ModelManager.ModelMetaData)modelMetaData.get(var2);
      if (var8 != null) {
         if (!StringUtils.isNullOrWhitespace(var8.meshName)) {
            var5 = var8.meshName;
         }

         if (!StringUtils.isNullOrWhitespace(var8.textureName)) {
            var6 = var8.textureName;
         }

         if (!StringUtils.isNullOrWhitespace(var8.shaderName)) {
            var7 = var8.shaderName;
         }
      }

      if (!StringUtils.isNullOrEmpty(var4)) {
         var6 = var4;
      }

      ModelScript var9 = ScriptManager.instance.getModelScript(var2);
      if (var9 != null) {
         var5 = var9.getMeshName();
         var6 = var9.getTextureName();
         var7 = var9.getShaderName();
         ModelInstance var10 = this.addStatic(var1, var5, var6, var3, var7);
         if (var10 != null) {
            var10.applyModelScriptScale(var2);
         }

         return var10;
      } else {
         return this.addStatic(var1, var5, var6, var3, var7);
      }
   }

   public ModelInstance addStatic(ModelInstance var1, String var2, String var3, String var4) {
      return this.addStaticForcedTex(var1, var2, var3, var4, (String)null);
   }

   public ModelInstance addStaticForcedTex(ModelInstance var1, String var2, String var3, String var4, String var5) {
      String var6 = var2;
      String var7 = var2;
      String var8 = null;
      ModelScript var9 = ScriptManager.instance.getModelScript(var2);
      if (var9 != null) {
         var6 = var9.getMeshName();
         var7 = var9.getTextureName();
         var8 = var9.getShaderName();
      }

      if (!StringUtils.isNullOrEmpty(var5)) {
         var7 = var5;
      }

      Model var10 = this.tryGetLoadedModel(var6, var7, true, var8, false);
      if (var10 == null && var6 != null) {
         this.loadStaticModel(var6, var7, var8);
         var10 = this.getLoadedModel(var6, var7, true, var8);
         if (var10 == null) {
            if (DebugLog.isEnabled(DebugType.Animation)) {
               DebugLog.Animation.error("Model not found. model:" + var6 + " tex:" + var7);
            }

            return null;
         }
      }

      if (var6 == null) {
         var10 = this.tryGetLoadedModel("vehicles_wheel02", "vehicles/vehicle_wheel02", true, "vehiclewheel", false);
      }

      if (var10 == null) {
         return null;
      } else {
         ModelInstance var11 = (ModelInstance)this.m_modelInstancePool.alloc();
         if (var1 != null) {
            var11.init(var10, var1.character, var1.AnimPlayer);
            var11.parent = var1;
            var1.sub.add(var11);
         } else {
            var11.init(var10, (IsoGameCharacter)null, (AnimationPlayer)null);
         }

         if (var9 != null) {
            var11.applyModelScriptScale(var2);
         }

         var11.attachmentNameSelf = var3;
         var11.attachmentNameParent = var4;
         return var11;
      }
   }

   private String modifyShaderName(String var1) {
      if ((StringUtils.equals(var1, "vehicle") || StringUtils.equals(var1, "vehicle_multiuv") || StringUtils.equals(var1, "vehicle_norandom_multiuv")) && !Core.getInstance().getPerfReflectionsOnLoad()) {
         var1 = var1 + "_noreflect";
      }

      return var1;
   }

   private Model loadModelInternal(String var1, String var2, String var3, ModelMesh var4, boolean var5) {
      var3 = this.modifyShaderName(var3);
      Model.ModelAssetParams var6 = new Model.ModelAssetParams();
      var6.animationsModel = var4;
      var6.bStatic = var5;
      var6.meshName = var1;
      var6.shaderName = var3;
      var6.textureName = var2;
      var6.textureFlags = this.getTextureFlags();
      String var7 = this.createModelKey(var1, var2, var5, var3);
      Model var8 = (Model)ModelAssetManager.instance.load(new AssetPath(var7), var6);
      if (var8 != null) {
         this.putLoadedModel(var1, var2, var5, var3, var8);
      }

      return var8;
   }

   public int getTextureFlags() {
      int var1 = TextureID.bUseCompression ? 4 : 0;
      if (Core.OptionModelTextureMipmaps) {
      }

      return var1;
   }

   public void setModelMetaData(String var1, String var2, String var3, boolean var4) {
      this.setModelMetaData(var1, var1, var2, var3, var4);
   }

   public void setModelMetaData(String var1, String var2, String var3, String var4, boolean var5) {
      ModelManager.ModelMetaData var6 = new ModelManager.ModelMetaData();
      var6.meshName = var2;
      var6.textureName = var3;
      var6.shaderName = var4;
      var6.bStatic = var5;
      modelMetaData.put(var1, var6);
   }

   public Model loadStaticModel(String var1, String var2, String var3) {
      String var4 = this.modifyShaderName(var3);
      return this.loadModelInternal(var1, var2, var4, (ModelMesh)null, true);
   }

   private Model loadModel(String var1, String var2, ModelMesh var3) {
      return this.loadModelInternal(var1, var2, "basicEffect", var3, false);
   }

   public Model getLoadedModel(String var1) {
      ModelScript var2 = ScriptManager.instance.getModelScript(var1);
      if (var2 != null) {
         if (var2.loadedModel != null) {
            return var2.loadedModel;
         } else {
            var2.shaderName = this.modifyShaderName(var2.shaderName);
            Model var10 = var2.loadedModel = this.tryGetLoadedModel(var2.getMeshName(), var2.getTextureName(), var2.bStatic, var2.getShaderName(), false);
            if (var10 != null) {
               return var10;
            } else {
               var10 = var2.bStatic ? this.loadModelInternal(var2.getMeshName(), var2.getTextureName(), var2.getShaderName(), (ModelMesh)null, true) : this.loadModelInternal(var2.getMeshName(), var2.getTextureName(), var2.getShaderName(), (ModelMesh)null, false);
               return var10;
            }
         }
      } else {
         ModelManager.ModelMetaData var3 = (ModelManager.ModelMetaData)modelMetaData.get(var1);
         Model var4;
         if (var3 != null) {
            var3.shaderName = this.modifyShaderName(var3.shaderName);
            var4 = this.tryGetLoadedModel(var3.meshName, var3.textureName, var3.bStatic, var3.shaderName, false);
            if (var4 != null) {
               return var4;
            } else {
               return var3.bStatic ? this.loadStaticModel(var3.meshName, var3.textureName, var3.shaderName) : this.loadModel(var3.meshName, var3.textureName, this.m_animModel);
            }
         } else {
            var4 = this.tryGetLoadedModel(var1, (String)null, false, (String)null, false);
            if (var4 != null) {
               return var4;
            } else {
               String var5 = var1.toLowerCase().trim();
               Iterator var6 = this.m_modelMap.entrySet().iterator();

               while(var6.hasNext()) {
                  Entry var7 = (Entry)var6.next();
                  String var8 = (String)var7.getKey();
                  if (var8.startsWith(var5)) {
                     Model var9 = (Model)var7.getValue();
                     if (var9 != null && (var8.length() == var5.length() || var8.charAt(var5.length()) == '&')) {
                        var4 = var9;
                        break;
                     }
                  }
               }

               if (var4 == null && DebugLog.isEnabled(DebugType.Animation)) {
                  DebugLog.Animation.error("ModelManager.getLoadedModel> Model missing for key=\"" + var5 + "\"");
               }

               return var4;
            }
         }
      }
   }

   public Model getLoadedModel(String var1, String var2, boolean var3, String var4) {
      return this.tryGetLoadedModel(var1, var2, var3, var4, true);
   }

   public Model tryGetLoadedModel(String var1, String var2, boolean var3, String var4, boolean var5) {
      String var6 = this.createModelKey(var1, var2, var3, var4);
      if (var6 == null) {
         return null;
      } else {
         Model var7 = (Model)this.m_modelMap.get(var6);
         if (var7 == null && var5 && DebugLog.isEnabled(DebugType.Animation)) {
            DebugLog.Animation.error("ModelManager.getLoadedModel> Model missing for key=\"" + var6 + "\"");
         }

         return var7;
      }
   }

   public void putLoadedModel(String var1, String var2, boolean var3, String var4, Model var5) {
      String var6 = this.createModelKey(var1, var2, var3, var4);
      if (var6 != null) {
         Model var7 = (Model)this.m_modelMap.get(var6);
         if (var7 != var5) {
            if (var7 != null) {
               DebugLog.Animation.debugln("Override key=\"%s\" old=%s new=%s", var6, var7, var5);
            } else {
               DebugLog.Animation.debugln("key=\"%s\" model=%s", var6, var5);
            }

            this.m_modelMap.put(var6, var5);
            var5.Name = var6;
         }
      }
   }

   private String createModelKey(String var1, String var2, boolean var3, String var4) {
      builder.delete(0, builder.length());
      if (var1 == null) {
         return null;
      } else {
         if (!toLowerKeyRoot.containsKey(var1)) {
            toLowerKeyRoot.put(var1, var1.toLowerCase(Locale.ENGLISH).trim());
         }

         builder.append((String)toLowerKeyRoot.get(var1));
         builder.append(amp);
         if (StringUtils.isNullOrWhitespace(var4)) {
            var4 = basicEffect;
         }

         builder.append(shaderEquals);
         if (!toLower.containsKey(var4)) {
            toLower.put(var4, var4.toLowerCase().trim());
         }

         builder.append((String)toLower.get(var4));
         if (!StringUtils.isNullOrWhitespace(var2)) {
            builder.append(texA);
            if (!toLowerTex.containsKey(var2)) {
               toLowerTex.put(var2, var2.toLowerCase().trim());
            }

            builder.append((String)toLowerTex.get(var2));
         }

         if (var3) {
            builder.append(isStaticTrue);
         }

         return builder.toString();
      }
   }

   private String createModelKey2(String var1, String var2, boolean var3, String var4) {
      if (var1 == null) {
         return null;
      } else {
         if (StringUtils.isNullOrWhitespace(var4)) {
            var4 = "basicEffect";
         }

         String var5 = "shader=" + var4.toLowerCase().trim();
         if (!StringUtils.isNullOrWhitespace(var2)) {
            var5 = var5 + ";tex=" + var2.toLowerCase().trim();
         }

         if (var3) {
            var5 = var5 + ";isStatic=true";
         }

         String var6 = var1.toLowerCase(Locale.ENGLISH).trim();
         return var6 + "&" + var5;
      }
   }

   private AnimationAsset loadAnim(String var1, ModelMesh var2, ModelManager.ModAnimations var3) {
      DebugLog.Animation.debugln("Adding asset to queue: %s", var1);
      AnimationAsset.AnimationAssetParams var4 = new AnimationAsset.AnimationAssetParams();
      var4.animationsMesh = var2;
      AnimationAsset var5 = (AnimationAsset)AnimationAssetManager.instance.load(new AssetPath(var1), var4);
      var5.skinningData = var2.skinningData;
      this.putAnimationAsset(var1, var5, var3);
      return var5;
   }

   private void putAnimationAsset(String var1, AnimationAsset var2, ModelManager.ModAnimations var3) {
      String var4 = var1.toLowerCase();
      AnimationAsset var5 = (AnimationAsset)var3.m_animationAssetMap.getOrDefault(var4, (Object)null);
      if (var5 != null) {
         DebugLog.Animation.debugln("Overwriting asset: %s", this.animAssetToString(var5));
         DebugLog.Animation.debugln("New asset        : %s", this.animAssetToString(var2));
         var3.m_animationAssetList.remove(var5);
      }

      var2.modelManagerKey = var4;
      var2.modAnimations = var3;
      var3.m_animationAssetMap.put(var4, var2);
      var3.m_animationAssetList.add(var2);
   }

   private String animAssetToString(AnimationAsset var1) {
      if (var1 == null) {
         return "null";
      } else {
         AssetPath var2 = var1.getPath();
         return var2 == null ? "null-path" : String.valueOf(var2.getPath());
      }
   }

   private AnimationAsset getAnimationAsset(String var1) {
      String var2 = var1.toLowerCase(Locale.ENGLISH);
      return (AnimationAsset)this.m_animationAssets.get(var2);
   }

   private AnimationAsset getAnimationAssetRequired(String var1) {
      AnimationAsset var2 = this.getAnimationAsset(var1);
      if (var2 == null) {
         throw new NullPointerException("Required Animation Asset not found: " + var1);
      } else {
         return var2;
      }
   }

   public void addAnimationClip(String var1, AnimationClip var2) {
      this.m_animModel.skinningData.AnimationClips.put(var1, var2);
   }

   public AnimationClip getAnimationClip(String var1) {
      return (AnimationClip)this.m_animModel.skinningData.AnimationClips.get(var1);
   }

   public Collection getAllAnimationClips() {
      return this.m_animModel.skinningData.AnimationClips.values();
   }

   public ModelInstance newInstance(Model var1, IsoGameCharacter var2, AnimationPlayer var3) {
      if (var1 == null) {
         System.err.println("ModelManager.newInstance> Model is null.");
         return null;
      } else {
         ModelInstance var4 = (ModelInstance)this.m_modelInstancePool.alloc();
         var4.init(var1, var2, var3);
         return var4;
      }
   }

   public boolean isLoadingAnimations() {
      Iterator var1 = this.m_animationAssets.values().iterator();

      AnimationAsset var2;
      do {
         if (!var1.hasNext()) {
            return false;
         }

         var2 = (AnimationAsset)var1.next();
      } while(!var2.isEmpty());

      return true;
   }

   public void reloadModelsMatching(String var1) {
      var1 = var1.toLowerCase(Locale.ENGLISH);
      Set var2 = this.m_modelMap.keySet();
      Iterator var3 = var2.iterator();

      while(var3.hasNext()) {
         String var4 = (String)var3.next();
         if (var4.contains(var1)) {
            Model var5 = (Model)this.m_modelMap.get(var4);
            if (!var5.isEmpty()) {
               DebugLog.General.printf("reloading model %s\n", var4);
               ModelMesh.MeshAssetParams var6 = new ModelMesh.MeshAssetParams();
               var6.animationsMesh = null;
               if (var5.Mesh.vb == null) {
                  var6.bStatic = var4.contains(";isStatic=true");
               } else {
                  var6.bStatic = var5.Mesh.vb.bStatic;
               }

               MeshAssetManager.instance.reload(var5.Mesh, var6);
            }
         }
      }

   }

   public void loadModAnimations() {
      Iterator var1 = this.m_modAnimations.values().iterator();

      while(var1.hasNext()) {
         ModelManager.ModAnimations var2 = (ModelManager.ModAnimations)var1.next();
         var2.setPriority(var2 == this.m_gameAnimations ? 0 : -1);
      }

      ArrayList var6 = ZomboidFileSystem.instance.getModIDs();

      for(int var7 = 0; var7 < var6.size(); ++var7) {
         String var3 = (String)var6.get(var7);
         ChooseGameInfo.Mod var4 = ChooseGameInfo.getAvailableModDetails(var3);
         if (var4 != null && var4.animsXFile.isDirectory()) {
            ModelManager.ModAnimations var5 = (ModelManager.ModAnimations)this.m_modAnimations.get(var3);
            if (var5 != null) {
               var5.setPriority(var7 + 1);
            } else {
               var5 = new ModelManager.ModAnimations(var3);
               var5.setPriority(var7 + 1);
               this.m_modAnimations.put(var3, var5);
               this.loadAnimsFromDir(var4.baseFile.toURI(), var4.mediaFile.toURI(), var4.animsXFile, this.m_animModel, var5);
            }
         }
      }

      this.setActiveAnimations();
   }

   void setActiveAnimations() {
      this.m_animationAssets.clear();
      this.m_animModel.skinningData.AnimationClips.clear();
      Iterator var1 = this.m_modAnimations.values().iterator();

      label39:
      while(true) {
         ModelManager.ModAnimations var2;
         do {
            if (!var1.hasNext()) {
               return;
            }

            var2 = (ModelManager.ModAnimations)var1.next();
         } while(!var2.isActive());

         Iterator var3 = var2.m_animationAssetList.iterator();

         while(true) {
            AnimationAsset var4;
            AnimationAsset var5;
            do {
               if (!var3.hasNext()) {
                  continue label39;
               }

               var4 = (AnimationAsset)var3.next();
               var5 = (AnimationAsset)this.m_animationAssets.get(var4.modelManagerKey);
            } while(var5 != null && var5 != var4 && var5.modAnimations.m_priority > var2.m_priority);

            this.m_animationAssets.put(var4.modelManagerKey, var4);
            if (var4.isReady()) {
               var4.skinningData.AnimationClips.putAll(var4.AnimationClips);
            }
         }
      }
   }

   public void animationAssetLoaded(AnimationAsset var1) {
      if (var1.modAnimations.isActive()) {
         AnimationAsset var2 = (AnimationAsset)this.m_animationAssets.get(var1.modelManagerKey);
         if (var2 == null || var2 == var1 || var2.modAnimations.m_priority <= var1.modAnimations.m_priority) {
            this.m_animationAssets.put(var1.modelManagerKey, var1);
            var1.skinningData.AnimationClips.putAll(var1.AnimationClips);
         }
      }
   }

   static {
      modelMetaData = new TreeMap(String.CASE_INSENSITIVE_ORDER);
      basicEffect = "basicEffect";
      isStaticTrue = ";isStatic=true";
      shaderEquals = "shader=";
      texA = ";tex=";
      amp = "&";
      toLower = new HashMap();
      toLowerTex = new HashMap();
      toLowerKeyRoot = new HashMap();
      builder = new StringBuilder();
   }

   public static final class ModAnimations {
      public final String m_modID;
      public final ArrayList m_animationAssetList = new ArrayList();
      public final HashMap m_animationAssetMap = new HashMap();
      public int m_priority;

      public ModAnimations(String var1) {
         this.m_modID = var1;
      }

      public void setPriority(int var1) {
         assert var1 >= -1;

         this.m_priority = var1;
      }

      public boolean isActive() {
         return this.m_priority != -1;
      }
   }

   class AnimDirReloader implements PredicatedFileWatcher.IPredicatedFileWatcherCallback {
      URI m_baseURI;
      URI m_mediaURI;
      String m_dir;
      String m_dirSecondary;
      String m_dirAbsolute;
      String m_dirSecondaryAbsolute;
      ModelMesh m_animationsModel;
      ModelManager.ModAnimations m_modAnimations;

      public AnimDirReloader(URI var2, URI var3, String var4, ModelMesh var5, ModelManager.ModAnimations var6) {
         var4 = ZomboidFileSystem.instance.getRelativeFile(var2, var4);
         this.m_baseURI = var2;
         this.m_mediaURI = var3;
         this.m_dir = ZomboidFileSystem.instance.normalizeFolderPath(var4);
         this.m_dirAbsolute = ZomboidFileSystem.instance.normalizeFolderPath((new File(new File(this.m_baseURI), this.m_dir)).toString());
         if (this.m_dir.contains("/anims/")) {
            this.m_dirSecondary = this.m_dir.replace("/anims/", "/anims_X/");
            this.m_dirSecondaryAbsolute = ZomboidFileSystem.instance.normalizeFolderPath((new File(new File(this.m_baseURI), this.m_dirSecondary)).toString());
         }

         this.m_animationsModel = var5;
         this.m_modAnimations = var6;
      }

      private boolean IsInDir(String var1) {
         var1 = ZomboidFileSystem.instance.normalizeFolderPath(var1);

         try {
            if (this.m_dirSecondary == null) {
               return var1.startsWith(this.m_dirAbsolute);
            } else {
               return var1.startsWith(this.m_dirAbsolute) || var1.startsWith(this.m_dirSecondaryAbsolute);
            }
         } catch (Exception var3) {
            var3.printStackTrace();
            return false;
         }
      }

      public void call(String var1) {
         String var2 = var1.toLowerCase();
         if (var2.endsWith(".fbx") || var2.endsWith(".x") || var2.endsWith(".txt")) {
            String var3 = ZomboidFileSystem.instance.getAnimName(this.m_mediaURI, new File(var1));
            AnimationAsset var4 = ModelManager.this.getAnimationAsset(var3);
            if (var4 != null) {
               if (!var4.isEmpty()) {
                  DebugLog.General.debugln("Reloading animation: %s", ModelManager.this.animAssetToString(var4));

                  assert var4.getRefCount() == 1;

                  AnimationAsset.AnimationAssetParams var5 = new AnimationAsset.AnimationAssetParams();
                  var5.animationsMesh = this.m_animationsModel;
                  AnimationAssetManager.instance.reload(var4, var5);
               }

            } else {
               ModelManager.this.loadAnim(var3, this.m_animationsModel, this.m_modAnimations);
            }
         }
      }

      public PredicatedFileWatcher GetFileWatcher() {
         return new PredicatedFileWatcher(this.m_dir, this::IsInDir, this);
      }
   }

   public static class ModelSlot {
      public int ID;
      public ModelInstance model;
      public IsoGameCharacter character;
      public final ArrayList sub = new ArrayList();
      protected final AttachedModels attachedModels = new AttachedModels();
      public boolean active;
      public boolean bRemove;
      public int renderRefCount = 0;
      public int framesSinceStart;

      public ModelSlot(int var1, ModelInstance var2, IsoGameCharacter var3) {
         this.ID = var1;
         this.model = var2;
         this.character = var3;
      }

      public void Update() {
         if (this.character != null && !this.bRemove) {
            ++this.framesSinceStart;
            if (this != this.character.legsSprite.modelSlot) {
               boolean var1 = false;
            }

            if (this.model.AnimPlayer != this.character.getAnimationPlayer()) {
               this.model.AnimPlayer = this.character.getAnimationPlayer();
            }

            synchronized(this.model.m_lock) {
               this.model.UpdateDir();
               this.model.Update();

               for(int var2 = 0; var2 < this.sub.size(); ++var2) {
                  ((ModelInstance)this.sub.get(var2)).AnimPlayer = this.model.AnimPlayer;
               }

            }
         }
      }

      public boolean isRendering() {
         return this.renderRefCount > 0;
      }

      public void reset() {
         ModelManager.instance.resetModelInstanceRecurse(this.model, this);
         if (this.character != null) {
            this.character.primaryHandModel = null;
            this.character.secondaryHandModel = null;
            ModelManager.instance.derefModelInstances(this.character.getReadyModelData());
            this.character.getReadyModelData().clear();
         }

         this.active = false;
         this.character = null;
         this.bRemove = false;
         this.renderRefCount = 0;
         this.model = null;
         this.sub.clear();
         this.attachedModels.clear();
      }
   }

   private static final class ModelMetaData {
      String meshName;
      String textureName;
      String shaderName;
      boolean bStatic;
   }
}

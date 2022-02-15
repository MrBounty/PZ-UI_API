package zombie.iso.sprite;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Stack;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;
import zombie.GameTime;
import zombie.GameWindow;
import zombie.characters.IsoPlayer;
import zombie.core.Core;
import zombie.core.SpriteRenderer;
import zombie.core.opengl.CharacterModelCamera;
import zombie.core.properties.PropertyContainer;
import zombie.core.skinnedmodel.ModelCameraRenderData;
import zombie.core.skinnedmodel.ModelManager;
import zombie.core.textures.ColorInfo;
import zombie.core.textures.Mask;
import zombie.core.textures.Texture;
import zombie.debug.DebugOptions;
import zombie.debug.LineDrawer;
import zombie.iso.IsoCamera;
import zombie.iso.IsoDirections;
import zombie.iso.IsoGridSquare;
import zombie.iso.IsoMovingObject;
import zombie.iso.IsoObject;
import zombie.iso.IsoObjectPicker;
import zombie.iso.IsoUtils;
import zombie.iso.IsoWater;
import zombie.iso.Vector2;
import zombie.iso.Vector3;
import zombie.iso.WorldConverter;
import zombie.iso.SpriteDetails.IsoObjectType;
import zombie.util.StringUtils;
import zombie.vehicles.BaseVehicle;
import zombie.vehicles.VehicleModelCamera;

public final class IsoSprite {
   public static int maxCount = 0;
   public static float alphaStep = 0.05F;
   public static float globalOffsetX = -1.0F;
   public static float globalOffsetY = -1.0F;
   private static final ColorInfo info = new ColorInfo();
   private static final HashMap AnimNameSet = new HashMap();
   public int firerequirement;
   public String burntTile;
   public boolean forceAmbient;
   public boolean solidfloor;
   public boolean canBeRemoved;
   public boolean attachedFloor;
   public boolean cutW;
   public boolean cutN;
   public boolean solid;
   public boolean solidTrans;
   public boolean invisible;
   public boolean alwaysDraw;
   public boolean forceRender;
   public boolean moveWithWind = false;
   public boolean isBush = false;
   public static final byte RL_DEFAULT = 0;
   public static final byte RL_FLOOR = 1;
   public byte renderLayer = 0;
   public int windType = 1;
   public boolean Animate = true;
   public IsoAnim CurrentAnim = null;
   public boolean DeleteWhenFinished = false;
   public boolean Loop = true;
   public short soffX = 0;
   public short soffY = 0;
   public final PropertyContainer Properties = new PropertyContainer();
   public final ColorInfo TintMod = new ColorInfo(1.0F, 1.0F, 1.0F, 1.0F);
   public final HashMap AnimMap = new HashMap(2);
   public final ArrayList AnimStack = new ArrayList(1);
   public String name;
   public int tileSheetIndex = 0;
   public int ID = 20000000;
   public IsoSpriteInstance def;
   public ModelManager.ModelSlot modelSlot;
   IsoSpriteManager parentManager;
   private IsoObjectType type;
   private String parentObjectName;
   private IsoSpriteGrid spriteGrid;
   public boolean treatAsWallOrder;
   private boolean hideForWaterRender;

   public void setHideForWaterRender() {
      this.hideForWaterRender = true;
   }

   public IsoSprite() {
      this.type = IsoObjectType.MAX;
      this.parentObjectName = null;
      this.treatAsWallOrder = false;
      this.hideForWaterRender = false;
      this.parentManager = IsoSpriteManager.instance;
      this.def = IsoSpriteInstance.get(this);
   }

   public IsoSprite(IsoSpriteManager var1) {
      this.type = IsoObjectType.MAX;
      this.parentObjectName = null;
      this.treatAsWallOrder = false;
      this.hideForWaterRender = false;
      this.parentManager = var1;
      this.def = IsoSpriteInstance.get(this);
   }

   public static IsoSprite CreateSprite(IsoSpriteManager var0) {
      IsoSprite var1 = new IsoSprite(var0);
      return var1;
   }

   public static IsoSprite CreateSpriteUsingCache(String var0, String var1, int var2) {
      IsoSprite var3 = CreateSprite(IsoSpriteManager.instance);
      return var3.setFromCache(var0, var1, var2);
   }

   public static IsoSprite getSprite(IsoSpriteManager var0, int var1) {
      if (WorldConverter.instance.TilesetConversions != null && !WorldConverter.instance.TilesetConversions.isEmpty() && WorldConverter.instance.TilesetConversions.containsKey(var1)) {
         var1 = (Integer)WorldConverter.instance.TilesetConversions.get(var1);
      }

      return var0.IntMap.containsKey(var1) ? (IsoSprite)var0.IntMap.get(var1) : null;
   }

   public static void setSpriteID(IsoSpriteManager var0, int var1, IsoSprite var2) {
      if (var0.IntMap.containsKey(var2.ID)) {
         var0.IntMap.remove(var2.ID);
         var2.ID = var1;
         var0.IntMap.put(var1, var2);
      }

   }

   public static IsoSprite getSprite(IsoSpriteManager var0, IsoSprite var1, int var2) {
      if (var1.name.contains("_")) {
         String[] var3 = var1.name.split("_");
         int var4 = Integer.parseInt(var3[var3.length - 1].trim());
         var4 += var2;
         HashMap var10000 = var0.NamedMap;
         String var10001 = var1.name.substring(0, var1.name.lastIndexOf("_"));
         return (IsoSprite)var10000.get(var10001 + "_" + var4);
      } else {
         return null;
      }
   }

   public static IsoSprite getSprite(IsoSpriteManager var0, String var1, int var2) {
      IsoSprite var3 = (IsoSprite)var0.NamedMap.get(var1);
      String var4 = var3.name.substring(0, var3.name.lastIndexOf(95));
      String var5 = var3.name.substring(var3.name.lastIndexOf(95) + 1);
      if (var3.name.contains("_")) {
         int var6 = Integer.parseInt(var5.trim());
         var6 += var2;
         return var0.getSprite(var4 + "_" + var6);
      } else {
         return null;
      }
   }

   public static void DisposeAll() {
      AnimNameSet.clear();
   }

   public static boolean HasCache(String var0) {
      return AnimNameSet.containsKey(var0);
   }

   public IsoSpriteInstance newInstance() {
      return IsoSpriteInstance.get(this);
   }

   public PropertyContainer getProperties() {
      return this.Properties;
   }

   public String getParentObjectName() {
      return this.parentObjectName;
   }

   public void setParentObjectName(String var1) {
      this.parentObjectName = var1;
   }

   public void save(DataOutputStream var1) throws IOException {
      GameWindow.WriteString(var1, this.name);
   }

   public void load(DataInputStream var1) throws IOException {
      this.name = GameWindow.ReadString(var1);
      this.LoadFramesNoDirPageSimple(this.name);
   }

   public void Dispose() {
      Iterator var1 = this.AnimMap.values().iterator();

      while(var1.hasNext()) {
         IsoAnim var2 = (IsoAnim)var1.next();
         var2.Dispose();
      }

      this.AnimMap.clear();
      this.AnimStack.clear();
      this.CurrentAnim = null;
   }

   public boolean isMaskClicked(IsoDirections var1, int var2, int var3) {
      try {
         Texture var4 = ((IsoDirectionFrame)this.CurrentAnim.Frames.get((int)this.def.Frame)).directions[var1.index()];
         if (var4 == null) {
            return false;
         } else {
            Mask var5 = var4.getMask();
            if (var5 == null) {
               return false;
            } else {
               var2 = (int)((float)var2 - var4.offsetX);
               var3 = (int)((float)var3 - var4.offsetY);
               return var5.get(var2, var3);
            }
         }
      } catch (Exception var6) {
         Logger.getLogger(GameWindow.class.getName()).log(Level.SEVERE, (String)null, var6);
         return true;
      }
   }

   public boolean isMaskClicked(IsoDirections var1, int var2, int var3, boolean var4) {
      if (this.CurrentAnim == null) {
         return false;
      } else {
         this.initSpriteInstance();

         try {
            if (this.CurrentAnim != null && this.CurrentAnim.Frames != null && !(this.def.Frame >= (float)this.CurrentAnim.Frames.size())) {
               Texture var5 = ((IsoDirectionFrame)this.CurrentAnim.Frames.get((int)this.def.Frame)).directions[var1.index()];
               if (var5 == null) {
                  return false;
               } else {
                  Mask var6 = var5.getMask();
                  if (var6 == null) {
                     return false;
                  } else {
                     if (var4) {
                        var2 = (int)((float)var2 - ((float)(var5.getWidthOrig() - var5.getWidth()) - var5.offsetX));
                        var3 = (int)((float)var3 - var5.offsetY);
                        var2 = var5.getWidth() - var2;
                     } else {
                        var2 = (int)((float)var2 - var5.offsetX);
                        var3 = (int)((float)var3 - var5.offsetY);
                     }

                     return var2 >= 0 && var3 >= 0 && var2 <= var5.getWidth() && var3 <= var5.getHeight() ? var6.get(var2, var3) : false;
                  }
               }
            } else {
               return false;
            }
         } catch (Exception var7) {
            Logger.getLogger(GameWindow.class.getName()).log(Level.SEVERE, (String)null, var7);
            return true;
         }
      }
   }

   public float getMaskClickedY(IsoDirections var1, int var2, int var3, boolean var4) {
      try {
         Texture var5 = ((IsoDirectionFrame)this.CurrentAnim.Frames.get((int)this.def.Frame)).directions[var1.index()];
         if (var5 == null) {
            return 10000.0F;
         } else {
            Mask var6 = var5.getMask();
            if (var6 == null) {
               return 10000.0F;
            } else {
               if (var4) {
                  var2 = (int)((float)var2 - ((float)(var5.getWidthOrig() - var5.getWidth()) - var5.offsetX));
                  var3 = (int)((float)var3 - var5.offsetY);
                  var2 = var5.getWidth() - var2;
               } else {
                  var2 = (int)((float)var2 - var5.offsetX);
                  var3 = (int)((float)var3 - var5.offsetY);
                  var2 = var5.getWidth() - var2;
               }

               return (float)var3;
            }
         }
      } catch (Exception var7) {
         Logger.getLogger(GameWindow.class.getName()).log(Level.SEVERE, (String)null, var7);
         return 10000.0F;
      }
   }

   public Texture LoadFrameExplicit(String var1) {
      this.CurrentAnim = new IsoAnim();
      this.AnimMap.put("default", this.CurrentAnim);
      this.CurrentAnim.ID = this.AnimStack.size();
      this.AnimStack.add(this.CurrentAnim);
      return this.CurrentAnim.LoadFrameExplicit(var1);
   }

   public void LoadFrames(String var1, String var2, int var3) {
      if (!this.AnimMap.containsKey(var2)) {
         this.CurrentAnim = new IsoAnim();
         this.AnimMap.put(var2, this.CurrentAnim);
         this.CurrentAnim.ID = this.AnimStack.size();
         this.AnimStack.add(this.CurrentAnim);
         this.CurrentAnim.LoadFrames(var1, var2, var3);
      }
   }

   public void LoadFramesReverseAltName(String var1, String var2, String var3, int var4) {
      if (!this.AnimMap.containsKey(var3)) {
         this.CurrentAnim = new IsoAnim();
         this.AnimMap.put(var3, this.CurrentAnim);
         this.CurrentAnim.ID = this.AnimStack.size();
         this.AnimStack.add(this.CurrentAnim);
         this.CurrentAnim.LoadFramesReverseAltName(var1, var2, var3, var4);
      }
   }

   public void LoadFramesNoDirPage(String var1, String var2, int var3) {
      this.CurrentAnim = new IsoAnim();
      this.AnimMap.put(var2, this.CurrentAnim);
      this.CurrentAnim.ID = this.AnimStack.size();
      this.AnimStack.add(this.CurrentAnim);
      this.CurrentAnim.LoadFramesNoDirPage(var1, var2, var3);
   }

   public void LoadFramesNoDirPageDirect(String var1, String var2, int var3) {
      this.CurrentAnim = new IsoAnim();
      this.AnimMap.put(var2, this.CurrentAnim);
      this.CurrentAnim.ID = this.AnimStack.size();
      this.AnimStack.add(this.CurrentAnim);
      this.CurrentAnim.LoadFramesNoDirPageDirect(var1, var2, var3);
   }

   public void LoadFramesNoDirPageSimple(String var1) {
      if (this.AnimMap.containsKey("default")) {
         IsoAnim var2 = (IsoAnim)this.AnimMap.get("default");
         this.AnimStack.remove(var2);
         this.AnimMap.remove("default");
      }

      this.CurrentAnim = new IsoAnim();
      this.AnimMap.put("default", this.CurrentAnim);
      this.CurrentAnim.ID = this.AnimStack.size();
      this.AnimStack.add(this.CurrentAnim);
      this.CurrentAnim.LoadFramesNoDirPage(var1);
   }

   public void ReplaceCurrentAnimFrames(String var1) {
      if (this.CurrentAnim != null) {
         this.CurrentAnim.Frames.clear();
         this.CurrentAnim.LoadFramesNoDirPage(var1);
      }
   }

   public void LoadFramesPageSimple(String var1, String var2, String var3, String var4) {
      this.CurrentAnim = new IsoAnim();
      this.AnimMap.put("default", this.CurrentAnim);
      this.CurrentAnim.ID = this.AnimStack.size();
      this.AnimStack.add(this.CurrentAnim);
      this.CurrentAnim.LoadFramesPageSimple(var1, var2, var3, var4);
   }

   public void LoadFramesPcx(String var1, String var2, int var3) {
      if (!this.AnimMap.containsKey(var2)) {
         this.CurrentAnim = new IsoAnim();
         this.AnimMap.put(var2, this.CurrentAnim);
         this.CurrentAnim.ID = this.AnimStack.size();
         this.AnimStack.add(this.CurrentAnim);
         this.CurrentAnim.LoadFramesPcx(var1, var2, var3);
      }
   }

   public void PlayAnim(IsoAnim var1) {
      if (this.CurrentAnim == null || this.CurrentAnim != var1) {
         this.CurrentAnim = var1;
      }

   }

   public void PlayAnim(String var1) {
      if ((this.CurrentAnim == null || !this.CurrentAnim.name.equals(var1)) && this.AnimMap.containsKey(var1)) {
         this.CurrentAnim = (IsoAnim)this.AnimMap.get(var1);
      }

   }

   public void PlayAnimUnlooped(String var1) {
      if (this.AnimMap.containsKey(var1)) {
         if (this.CurrentAnim == null || !this.CurrentAnim.name.equals(var1)) {
            this.CurrentAnim = (IsoAnim)this.AnimMap.get(var1);
         }

         this.CurrentAnim.looped = false;
      }

   }

   public void ChangeTintMod(ColorInfo var1) {
      this.TintMod.r = var1.r;
      this.TintMod.g = var1.g;
      this.TintMod.b = var1.b;
      this.TintMod.a = var1.a;
   }

   public void RenderGhostTile(int var1, int var2, int var3) {
      IsoSpriteInstance var4 = IsoSpriteInstance.get(this);
      var4.alpha = var4.targetAlpha = 0.6F;
      this.render(var4, (IsoObject)null, (float)var1, (float)var2, (float)var3, IsoDirections.N, (float)(32 * Core.TileScale), (float)(96 * Core.TileScale), IsoGridSquare.getDefColorInfo(), true);
   }

   public void RenderGhostTileRed(int var1, int var2, int var3) {
      IsoSpriteInstance var4 = IsoSpriteInstance.get(this);
      var4.tintr = 0.65F;
      var4.tintg = 0.2F;
      var4.tintb = 0.2F;
      var4.alpha = var4.targetAlpha = 0.6F;
      this.render(var4, (IsoObject)null, (float)var1, (float)var2, (float)var3, IsoDirections.N, (float)(32 * Core.TileScale), (float)(96 * Core.TileScale), IsoGridSquare.getDefColorInfo(), true);
   }

   public void RenderGhostTileColor(int var1, int var2, int var3, float var4, float var5, float var6, float var7) {
      this.RenderGhostTileColor(var1, var2, var3, 0.0F, 0.0F, var4, var5, var6, var7);
   }

   public void RenderGhostTileColor(int var1, int var2, int var3, float var4, float var5, float var6, float var7, float var8, float var9) {
      IsoSpriteInstance var10 = IsoSpriteInstance.get(this);
      var10.tintr = var6;
      var10.tintg = var7;
      var10.tintb = var8;
      var10.alpha = var10.targetAlpha = var9;
      IsoGridSquare.getDefColorInfo().r = IsoGridSquare.getDefColorInfo().g = IsoGridSquare.getDefColorInfo().b = IsoGridSquare.getDefColorInfo().a = 1.0F;
      int var11 = Core.TileScale;
      this.render(var10, (IsoObject)null, (float)var1, (float)var2, (float)var3, IsoDirections.N, (float)(32 * var11) + var4, (float)(96 * var11) + var5, IsoGridSquare.getDefColorInfo(), true);
   }

   public boolean hasActiveModel() {
      if (!ModelManager.instance.bDebugEnableModels) {
         return false;
      } else if (!ModelManager.instance.isCreated()) {
         return false;
      } else {
         return this.modelSlot != null && this.modelSlot.active;
      }
   }

   public void renderVehicle(IsoSpriteInstance var1, IsoObject var2, float var3, float var4, float var5, float var6, float var7, ColorInfo var8, boolean var9) {
      if (var1 != null) {
         if (this.hasActiveModel()) {
            SpriteRenderer.instance.drawGeneric(((ModelCameraRenderData)ModelCameraRenderData.s_pool.alloc()).init(VehicleModelCamera.instance, this.modelSlot));
            SpriteRenderer.instance.drawModel(this.modelSlot);
            if (!BaseVehicle.RENDER_TO_TEXTURE) {
               return;
            }
         }

         info.r = var8.r;
         info.g = var8.g;
         info.b = var8.b;
         info.a = var8.a;

         try {
            if (var9) {
               var1.renderprep(var2);
            }

            float var10 = 0.0F;
            float var11 = 0.0F;
            if (globalOffsetX == -1.0F) {
               globalOffsetX = -IsoCamera.frameState.OffX;
               globalOffsetY = -IsoCamera.frameState.OffY;
            }

            if (var2 == null || var2.sx == 0.0F || var2 instanceof IsoMovingObject) {
               var10 = IsoUtils.XToScreen(var3 + var1.offX, var4 + var1.offY, var5 + var1.offZ, 0);
               var11 = IsoUtils.YToScreen(var3 + var1.offX, var4 + var1.offY, var5 + var1.offZ, 0);
               var10 -= var6;
               var11 -= var7;
               if (var2 != null) {
                  var2.sx = var10;
                  var2.sy = var11;
               }
            }

            if (var2 != null) {
               var10 = var2.sx + globalOffsetX;
               var11 = var2.sy + globalOffsetY;
               var10 += (float)this.soffX;
               var11 += (float)this.soffY;
            } else {
               var10 += globalOffsetX;
               var11 += globalOffsetY;
               var10 += (float)this.soffX;
               var11 += (float)this.soffY;
            }

            ColorInfo var10000;
            if (var9) {
               if (var1.tintr != 1.0F || var1.tintg != 1.0F || var1.tintb != 1.0F) {
                  var10000 = info;
                  var10000.r *= var1.tintr;
                  var10000 = info;
                  var10000.g *= var1.tintg;
                  var10000 = info;
                  var10000.b *= var1.tintb;
               }

               info.a = var1.alpha;
            }

            if (!this.hasActiveModel() && (this.TintMod.r != 1.0F || this.TintMod.g != 1.0F || this.TintMod.b != 1.0F)) {
               var10000 = info;
               var10000.r *= this.TintMod.r;
               var10000 = info;
               var10000.g *= this.TintMod.g;
               var10000 = info;
               var10000.b *= this.TintMod.b;
            }

            if (this.hasActiveModel()) {
               float var12 = var1.getScaleX() * (float)Core.TileScale;
               float var13 = -var1.getScaleY() * (float)Core.TileScale;
               float var14 = 0.666F;
               var12 /= 4.0F * var14;
               var13 /= 4.0F * var14;
               int var15 = ModelManager.instance.bitmap.getTexture().getWidth();
               int var16 = ModelManager.instance.bitmap.getTexture().getHeight();
               var10 -= (float)var15 * var12 / 2.0F;
               var11 -= (float)var16 * var13 / 2.0F;
               float var17 = ((BaseVehicle)var2).jniTransform.origin.y / 2.46F;
               var11 += 96.0F * var17 / var13 / var14;
               var11 += 27.84F / var13 / var14;
               if (Core.getInstance().RenderShader != null && Core.getInstance().getOffscreenBuffer() != null) {
                  SpriteRenderer.instance.render((Texture)ModelManager.instance.bitmap.getTexture(), var10, var11, (float)var15 * var12, (float)var16 * var13, 1.0F, 1.0F, 1.0F, info.a, (Consumer)null);
               } else {
                  SpriteRenderer.instance.render((Texture)ModelManager.instance.bitmap.getTexture(), var10, var11, (float)var15 * var12, (float)var16 * var13, info.r, info.g, info.b, info.a, (Consumer)null);
               }

               if (Core.bDebug && DebugOptions.instance.ModelRenderBounds.getValue()) {
                  LineDrawer.drawRect(var10, var11, (float)var15 * var12, (float)var16 * var13, 1.0F, 1.0F, 1.0F, 1.0F, 1);
               }
            }

            info.r = 1.0F;
            info.g = 1.0F;
            info.b = 1.0F;
         } catch (Exception var18) {
            Logger.getLogger(GameWindow.class.getName()).log(Level.SEVERE, (String)null, var18);
         }

      }
   }

   private IsoSpriteInstance getSpriteInstance() {
      this.initSpriteInstance();
      return this.def;
   }

   private void initSpriteInstance() {
      if (this.def == null) {
         this.def = IsoSpriteInstance.get(this);
      }

   }

   public final void render(IsoObject var1, float var2, float var3, float var4, IsoDirections var5, float var6, float var7, ColorInfo var8, boolean var9) {
      this.render(var1, var2, var3, var4, var5, var6, var7, var8, var9, (Consumer)null);
   }

   public final void render(IsoObject var1, float var2, float var3, float var4, IsoDirections var5, float var6, float var7, ColorInfo var8, boolean var9, Consumer var10) {
      this.render(this.getSpriteInstance(), var1, var2, var3, var4, var5, var6, var7, var8, var9, var10);
   }

   public final void render(IsoSpriteInstance var1, IsoObject var2, float var3, float var4, float var5, IsoDirections var6, float var7, float var8, ColorInfo var9, boolean var10) {
      this.render(var1, var2, var3, var4, var5, var6, var7, var8, var9, var10, (Consumer)null);
   }

   public void render(IsoSpriteInstance var1, IsoObject var2, float var3, float var4, float var5, IsoDirections var6, float var7, float var8, ColorInfo var9, boolean var10, Consumer var11) {
      if (this.hasActiveModel()) {
         this.renderActiveModel();
      } else {
         this.renderCurrentAnim(var1, var2, var3, var4, var5, var6, var7, var8, var9, var10, var11);
      }

   }

   public void renderCurrentAnim(IsoSpriteInstance var1, IsoObject var2, float var3, float var4, float var5, IsoDirections var6, float var7, float var8, ColorInfo var9, boolean var10, Consumer var11) {
      if (DebugOptions.instance.IsoSprite.RenderSprites.getValue()) {
         if (this.CurrentAnim != null && !this.CurrentAnim.Frames.isEmpty()) {
            float var12 = this.getCurrentSpriteFrame(var1);
            info.set(var9);
            Vector3 var13 = IsoSprite.l_renderCurrentAnim.colorInfoBackup.set(info.r, info.g, info.b);
            Vector2 var14 = IsoSprite.l_renderCurrentAnim.spritePos.set(0.0F, 0.0F);
            this.prepareToRenderSprite(var1, var2, var3, var4, var5, var6, var7, var8, var10, (int)var12, var14);
            this.performRenderFrame(var1, var2, var6, (int)var12, var14.x, var14.y, var11);
            info.r = var13.x;
            info.g = var13.y;
            info.b = var13.z;
         }
      }
   }

   private float getCurrentSpriteFrame(IsoSpriteInstance var1) {
      if (this.CurrentAnim.FramesArray == null) {
         this.CurrentAnim.FramesArray = (IsoDirectionFrame[])this.CurrentAnim.Frames.toArray(new IsoDirectionFrame[0]);
      }

      if (this.CurrentAnim.FramesArray.length != this.CurrentAnim.Frames.size()) {
         this.CurrentAnim.FramesArray = (IsoDirectionFrame[])this.CurrentAnim.Frames.toArray(this.CurrentAnim.FramesArray);
      }

      float var2;
      if (var1.Frame >= (float)this.CurrentAnim.Frames.size()) {
         var2 = (float)(this.CurrentAnim.FramesArray.length - 1);
      } else if (var1.Frame < 0.0F) {
         var1.Frame = 0.0F;
         var2 = 0.0F;
      } else {
         var2 = var1.Frame;
      }

      return var2;
   }

   private void prepareToRenderSprite(IsoSpriteInstance var1, IsoObject var2, float var3, float var4, float var5, IsoDirections var6, float var7, float var8, boolean var9, int var10, Vector2 var11) {
      if (var9) {
         var1.renderprep(var2);
      }

      float var12 = 0.0F;
      float var13 = 0.0F;
      if (globalOffsetX == -1.0F) {
         globalOffsetX = -IsoCamera.frameState.OffX;
         globalOffsetY = -IsoCamera.frameState.OffY;
      }

      if (var2 != null && var2.sx != 0.0F && !(var2 instanceof IsoMovingObject)) {
         if (var2 != null) {
            var12 = var2.sx + globalOffsetX;
            var13 = var2.sy + globalOffsetY;
            var12 += (float)this.soffX;
            var13 += (float)this.soffY;
         } else {
            var12 += globalOffsetX;
            var13 += globalOffsetY;
            var12 += (float)this.soffX;
            var13 += (float)this.soffY;
         }
      } else {
         var12 = IsoUtils.XToScreen(var3 + var1.offX, var4 + var1.offY, var5 + var1.offZ, 0);
         var13 = IsoUtils.YToScreen(var3 + var1.offX, var4 + var1.offY, var5 + var1.offZ, 0);
         var12 -= var7;
         var13 -= var8;
         if (var2 != null) {
            var2.sx = var12;
            var2.sy = var13;
         }

         var12 += globalOffsetX;
         var13 += globalOffsetY;
         var12 += (float)this.soffX;
         var13 += (float)this.soffY;
      }

      if (var2 instanceof IsoMovingObject && this.CurrentAnim != null && this.CurrentAnim.FramesArray[var10].getTexture(var6) != null) {
         var12 -= (float)(this.CurrentAnim.FramesArray[var10].getTexture(var6).getWidthOrig() / 2) * var1.getScaleX();
         var13 -= (float)this.CurrentAnim.FramesArray[var10].getTexture(var6).getHeightOrig() * var1.getScaleY();
      }

      ColorInfo var10000;
      if (var9) {
         if (var1.tintr != 1.0F || var1.tintg != 1.0F || var1.tintb != 1.0F) {
            var10000 = info;
            var10000.r *= var1.tintr;
            var10000 = info;
            var10000.g *= var1.tintg;
            var10000 = info;
            var10000.b *= var1.tintb;
         }

         info.a = var1.alpha;
         if (var1.bMultiplyObjectAlpha && var2 != null) {
            var10000 = info;
            var10000.a *= var2.getAlpha(IsoCamera.frameState.playerIndex);
         }
      }

      if (this.TintMod.r != 1.0F || this.TintMod.g != 1.0F || this.TintMod.b != 1.0F) {
         var10000 = info;
         var10000.r *= this.TintMod.r;
         var10000 = info;
         var10000.g *= this.TintMod.g;
         var10000 = info;
         var10000.b *= this.TintMod.b;
      }

      var11.set(var12, var13);
   }

   private void performRenderFrame(IsoSpriteInstance var1, IsoObject var2, IsoDirections var3, int var4, float var5, float var6, Consumer var7) {
      if (var4 < this.CurrentAnim.FramesArray.length) {
         IsoDirectionFrame var8 = this.CurrentAnim.FramesArray[var4];
         Texture var9 = var8.getTexture(var3);
         if (var9 != null) {
            if (Core.TileScale == 2 && var9.getWidthOrig() == 64 && var9.getHeightOrig() == 128) {
               var1.setScale(2.0F, 2.0F);
            }

            if (Core.TileScale == 2 && var1.scaleX == 2.0F && var1.scaleY == 2.0F && var9.getWidthOrig() == 128 && var9.getHeightOrig() == 256) {
               var1.setScale(1.0F, 1.0F);
            }

            if (!(var1.scaleX <= 0.0F) && !(var1.scaleY <= 0.0F)) {
               float var10 = (float)var9.getWidth();
               float var11 = (float)var9.getHeight();
               float var12 = var1.scaleX;
               float var13 = var1.scaleY;
               if (var12 != 1.0F) {
                  var5 += var9.getOffsetX() * (var12 - 1.0F);
                  var10 *= var12;
               }

               if (var13 != 1.0F) {
                  var6 += var9.getOffsetY() * (var13 - 1.0F);
                  var11 *= var13;
               }

               if (DebugOptions.instance.IsoSprite.MovingObjectEdges.getValue() && var2 instanceof IsoMovingObject) {
                  this.renderSpriteOutline(var5, var6, var9, var12, var13);
               }

               if (DebugOptions.instance.IsoSprite.DropShadowEdges.getValue() && StringUtils.equals(var9.getName(), "dropshadow")) {
                  this.renderSpriteOutline(var5, var6, var9, var12, var13);
               }

               if (!this.hideForWaterRender || !IsoWater.getInstance().getShaderEnable()) {
                  if (var2 != null && var2.getObjectRenderEffectsToApply() != null) {
                     var8.render(var2.getObjectRenderEffectsToApply(), var5, var6, var10, var11, var3, info, var1.Flip, var7);
                  } else {
                     var8.render(var5, var6, var10, var11, var3, info, var1.Flip, var7);
                  }
               }

               if (var4 < this.CurrentAnim.FramesArray.length && IsoObjectPicker.Instance.wasDirty && IsoCamera.frameState.playerIndex == 0 && var2 != null) {
                  boolean var14 = var3 == IsoDirections.W || var3 == IsoDirections.SW || var3 == IsoDirections.S;
                  if (var1.Flip) {
                     var14 = !var14;
                  }

                  var5 = var2.sx + globalOffsetX;
                  var6 = var2.sy + globalOffsetY;
                  if (var2 instanceof IsoMovingObject) {
                     var5 -= (float)(var9.getWidthOrig() / 2) * var12;
                     var6 -= (float)var9.getHeightOrig() * var13;
                  }

                  IsoObjectPicker.Instance.Add((int)var5, (int)var6, (int)((float)var9.getWidthOrig() * var12), (int)((float)var9.getHeightOrig() * var13), var2.square, var2, var14, var12, var13);
               }

            }
         }
      }
   }

   private void renderSpriteOutline(float var1, float var2, Texture var3, float var4, float var5) {
      LineDrawer.drawRect(var1, var2, (float)var3.getWidthOrig() * var4, (float)var3.getHeightOrig() * var5, 1.0F, 1.0F, 1.0F, 1.0F, 1);
      LineDrawer.drawRect(var1 + var3.getOffsetX() * var4, var2 + var3.getOffsetY() * var5, (float)var3.getWidth() * var4, (float)var3.getHeight() * var5, 1.0F, 1.0F, 1.0F, 1.0F, 1);
   }

   public void renderActiveModel() {
      if (DebugOptions.instance.IsoSprite.RenderModels.getValue()) {
         this.modelSlot.model.updateLights();
         SpriteRenderer.instance.drawGeneric(((ModelCameraRenderData)ModelCameraRenderData.s_pool.alloc()).init(CharacterModelCamera.instance, this.modelSlot));
         SpriteRenderer.instance.drawModel(this.modelSlot);
      }
   }

   public void renderBloodSplat(float var1, float var2, float var3, ColorInfo var4) {
      if (this.CurrentAnim != null && !this.CurrentAnim.Frames.isEmpty()) {
         boolean var5 = true;
         boolean var6 = true;
         byte var10 = 0;
         byte var11 = 0;

         try {
            if (globalOffsetX == -1.0F) {
               globalOffsetX = -IsoCamera.frameState.OffX;
               globalOffsetY = -IsoCamera.frameState.OffY;
            }

            float var7 = IsoUtils.XToScreen(var1, var2, var3, 0);
            float var8 = IsoUtils.YToScreen(var1, var2, var3, 0);
            var7 = (float)((int)var7);
            var8 = (float)((int)var8);
            var7 -= (float)var10;
            var8 -= (float)var11;
            var7 += globalOffsetX;
            var8 += globalOffsetY;
            if (!(var7 >= (float)IsoCamera.frameState.OffscreenWidth) && !(var7 + 64.0F <= 0.0F)) {
               if (!(var8 >= (float)IsoCamera.frameState.OffscreenHeight) && !(var8 + 64.0F <= 0.0F)) {
                  info.r = var4.r;
                  info.g = var4.g;
                  info.b = var4.b;
                  info.a = var4.a;
                  ((IsoDirectionFrame)this.CurrentAnim.Frames.get(0)).render(var7, var8, IsoDirections.N, info, false, (Consumer)null);
               }
            }
         } catch (Exception var9) {
            Logger.getLogger(GameWindow.class.getName()).log(Level.SEVERE, (String)null, var9);
         }
      }
   }

   public void renderObjectPicker(IsoSpriteInstance var1, IsoObject var2, IsoDirections var3) {
      if (this.CurrentAnim != null) {
         if (var1 != null) {
            if (IsoPlayer.getInstance() == IsoPlayer.players[0]) {
               if (!this.CurrentAnim.Frames.isEmpty()) {
                  if (var1.Frame >= (float)this.CurrentAnim.Frames.size()) {
                     var1.Frame = 0.0F;
                  }

                  if (((IsoDirectionFrame)this.CurrentAnim.Frames.get((int)var1.Frame)).getTexture(var3) != null) {
                     float var4 = var2.sx + globalOffsetX;
                     float var5 = var2.sy + globalOffsetY;
                     if (var2 instanceof IsoMovingObject) {
                        var4 -= (float)(((IsoDirectionFrame)this.CurrentAnim.Frames.get((int)var1.Frame)).getTexture(var3).getWidthOrig() / 2) * var1.getScaleX();
                        var5 -= (float)((IsoDirectionFrame)this.CurrentAnim.Frames.get((int)var1.Frame)).getTexture(var3).getHeightOrig() * var1.getScaleY();
                     }

                     if (var1.Frame < (float)this.CurrentAnim.Frames.size() && IsoObjectPicker.Instance.wasDirty && IsoCamera.frameState.playerIndex == 0) {
                        Texture var6 = ((IsoDirectionFrame)this.CurrentAnim.Frames.get((int)var1.Frame)).getTexture(var3);
                        boolean var7 = var3 == IsoDirections.W || var3 == IsoDirections.SW || var3 == IsoDirections.S;
                        if (var1.Flip) {
                           var7 = !var7;
                        }

                        IsoObjectPicker.Instance.Add((int)var4, (int)var5, (int)((float)var6.getWidthOrig() * var1.getScaleX()), (int)((float)var6.getHeightOrig() * var1.getScaleY()), var2.square, var2, var7, var1.getScaleX(), var1.getScaleY());
                     }

                  }
               }
            }
         }
      }
   }

   public Texture getTextureForFrame(int var1, IsoDirections var2) {
      if (this.CurrentAnim != null && !this.CurrentAnim.Frames.isEmpty()) {
         if (this.CurrentAnim.FramesArray == null) {
            this.CurrentAnim.FramesArray = (IsoDirectionFrame[])this.CurrentAnim.Frames.toArray(new IsoDirectionFrame[0]);
         }

         if (this.CurrentAnim.FramesArray.length != this.CurrentAnim.Frames.size()) {
            this.CurrentAnim.FramesArray = (IsoDirectionFrame[])this.CurrentAnim.Frames.toArray(this.CurrentAnim.FramesArray);
         }

         if (var1 >= this.CurrentAnim.FramesArray.length) {
            var1 = this.CurrentAnim.FramesArray.length - 1;
         }

         if (var1 < 0) {
            var1 = 0;
         }

         return this.CurrentAnim.FramesArray[var1].getTexture(var2);
      } else {
         return null;
      }
   }

   public Texture getTextureForCurrentFrame(IsoDirections var1) {
      this.initSpriteInstance();
      return this.getTextureForFrame((int)this.def.Frame, var1);
   }

   public void update() {
      this.update(this.def);
   }

   public void update(IsoSpriteInstance var1) {
      if (var1 == null) {
         var1 = IsoSpriteInstance.get(this);
      }

      if (this.CurrentAnim != null) {
         if (this.Animate && !var1.Finished) {
            float var2 = var1.Frame;
            if (!GameTime.isGamePaused()) {
               var1.Frame += var1.AnimFrameIncrease * GameTime.instance.getMultipliedSecondsSinceLastUpdate() * 60.0F;
            }

            if ((int)var1.Frame >= this.CurrentAnim.Frames.size() && this.Loop && var1.Looped) {
               var1.Frame = 0.0F;
            }

            if ((int)var2 != (int)var1.Frame) {
               var1.NextFrame = true;
            }

            if ((int)var1.Frame >= this.CurrentAnim.Frames.size() && (!this.Loop || !var1.Looped)) {
               var1.Finished = true;
               var1.Frame = (float)this.CurrentAnim.FinishUnloopedOnFrame;
               if (this.DeleteWhenFinished) {
                  this.Dispose();
                  this.Animate = false;
               }
            }
         }

      }
   }

   public void CacheAnims(String var1) {
      this.name = var1;
      Stack var2 = new Stack();

      for(int var3 = 0; var3 < this.AnimStack.size(); ++var3) {
         IsoAnim var4 = (IsoAnim)this.AnimStack.get(var3);
         String var5 = var1 + var4.name;
         var2.add(var5);
         if (!IsoAnim.GlobalAnimMap.containsKey(var5)) {
            IsoAnim.GlobalAnimMap.put(var5, var4);
         }
      }

      AnimNameSet.put(var1, var2.toArray());
   }

   public void LoadCache(String var1) {
      Object[] var2 = (Object[])AnimNameSet.get(var1);
      this.name = var1;

      for(int var3 = 0; var3 < var2.length; ++var3) {
         String var4 = (String)var2[var3];
         IsoAnim var5 = (IsoAnim)IsoAnim.GlobalAnimMap.get(var4);
         this.AnimMap.put(var5.name, var5);
         this.AnimStack.add(var5);
         this.CurrentAnim = var5;
      }

   }

   public IsoSprite setFromCache(String var1, String var2, int var3) {
      String var4 = var1 + var2;
      if (HasCache(var4)) {
         this.LoadCache(var4);
      } else {
         this.LoadFramesNoDirPage(var1, var2, var3);
         this.CacheAnims(var4);
      }

      return this;
   }

   public IsoObjectType getType() {
      return this.type;
   }

   public void setType(IsoObjectType var1) {
      this.type = var1;
   }

   public void AddProperties(IsoSprite var1) {
      this.getProperties().AddProperties(var1.getProperties());
   }

   public int getID() {
      return this.ID;
   }

   public String getName() {
      return this.name;
   }

   public void setName(String var1) {
      this.name = var1;
   }

   public ColorInfo getTintMod() {
      return this.TintMod;
   }

   public void setTintMod(ColorInfo var1) {
      this.TintMod.set(var1);
   }

   public void setAnimate(boolean var1) {
      this.Animate = var1;
   }

   public IsoSpriteGrid getSpriteGrid() {
      return this.spriteGrid;
   }

   public void setSpriteGrid(IsoSpriteGrid var1) {
      this.spriteGrid = var1;
   }

   public boolean isMoveWithWind() {
      return this.moveWithWind;
   }

   public int getSheetGridIdFromName() {
      return this.name != null ? getSheetGridIdFromName(this.name) : -1;
   }

   public static int getSheetGridIdFromName(String var0) {
      if (var0 != null) {
         int var1 = var0.lastIndexOf(95);
         if (var1 > 0 && var1 + 1 < var0.length()) {
            return Integer.parseInt(var0.substring(var1 + 1));
         }
      }

      return -1;
   }

   private static class l_renderCurrentAnim {
      static final Vector3 colorInfoBackup = new Vector3();
      static final Vector2 spritePos = new Vector2();
   }
}

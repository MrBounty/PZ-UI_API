package zombie.core.skinnedmodel;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Stack;
import java.util.function.Consumer;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;
import zombie.characters.IsoPlayer;
import zombie.characters.IsoZombie;
import zombie.characters.AttachedItems.AttachedModelName;
import zombie.characters.AttachedItems.AttachedModelNames;
import zombie.core.Core;
import zombie.core.ImmutableColor;
import zombie.core.SpriteRenderer;
import zombie.core.logger.ExceptionLogger;
import zombie.core.math.PZMath;
import zombie.core.opengl.RenderThread;
import zombie.core.skinnedmodel.advancedanimation.AnimatedModel;
import zombie.core.skinnedmodel.population.ClothingItem;
import zombie.core.skinnedmodel.visual.HumanVisual;
import zombie.core.skinnedmodel.visual.IHumanVisual;
import zombie.core.skinnedmodel.visual.ItemVisual;
import zombie.core.skinnedmodel.visual.ItemVisuals;
import zombie.core.textures.ColorInfo;
import zombie.core.textures.Texture;
import zombie.core.textures.TextureDraw;
import zombie.core.textures.TextureFBO;
import zombie.debug.DebugOptions;
import zombie.iso.IsoDirections;
import zombie.iso.IsoGridSquare;
import zombie.iso.Vector2;
import zombie.iso.objects.IsoDeadBody;
import zombie.iso.objects.IsoMannequin;
import zombie.util.StringUtils;

public final class DeadBodyAtlas {
   public static final int ATLAS_SIZE = 1024;
   public static final int ENTRY_WID;
   public static final int ENTRY_HGT;
   private TextureFBO fbo;
   public static final DeadBodyAtlas instance;
   private static final Vector2 tempVector2;
   private final HashMap EntryMap = new HashMap();
   private final ArrayList AtlasList = new ArrayList();
   private final DeadBodyAtlas.BodyParams bodyParams = new DeadBodyAtlas.BodyParams();
   private int updateCounter = -1;
   private final DeadBodyAtlas.Checksummer checksummer = new DeadBodyAtlas.Checksummer();
   private static final Stack JobPool;
   private final DeadBodyAtlas.DebugDrawInWorld[] debugDrawInWorld = new DeadBodyAtlas.DebugDrawInWorld[3];
   private long debugDrawTime;
   private final ArrayList RenderJobs = new ArrayList();
   private final DeadBodyAtlas.CharacterTextureVisual characterTextureVisualFemale = new DeadBodyAtlas.CharacterTextureVisual(true);
   private final DeadBodyAtlas.CharacterTextureVisual characterTextureVisualMale = new DeadBodyAtlas.CharacterTextureVisual(false);
   private final CharacterTextures characterTexturesFemale = new CharacterTextures();
   private final CharacterTextures characterTexturesMale = new CharacterTextures();

   public void lightingUpdate(int var1, boolean var2) {
      if (var1 != this.updateCounter && var2) {
         this.updateCounter = var1;
      }

   }

   public Texture getBodyTexture(IsoDeadBody var1) {
      this.bodyParams.init(var1);
      return this.getBodyTexture(this.bodyParams);
   }

   public Texture getBodyTexture(IsoZombie var1) {
      this.bodyParams.init(var1);
      return this.getBodyTexture(this.bodyParams);
   }

   public Texture getBodyTexture(IsoMannequin var1) {
      this.bodyParams.init(var1);
      return this.getBodyTexture(this.bodyParams);
   }

   public Texture getBodyTexture(boolean var1, String var2, String var3, IsoDirections var4, int var5, float var6) {
      CharacterTextures var7 = var1 ? this.characterTexturesFemale : this.characterTexturesMale;
      Texture var8 = var7.getTexture(var2, var3, var4, var5);
      if (var8 != null) {
         return var8;
      } else {
         this.bodyParams.init(var1 ? this.characterTextureVisualFemale : this.characterTextureVisualMale, var4, var2, var3, var6);
         this.bodyParams.variables.put("zombieWalkType", "1");
         Texture var9 = this.getBodyTexture(this.bodyParams);
         var7.addTexture(var2, var3, var4, var5, var9);
         return var9;
      }
   }

   public Texture getBodyTexture(DeadBodyAtlas.BodyParams var1) {
      String var2 = this.getBodyKey(var1);
      DeadBodyAtlas.AtlasEntry var3 = (DeadBodyAtlas.AtlasEntry)this.EntryMap.get(var2);
      if (var3 != null) {
         return var3.tex;
      } else {
         DeadBodyAtlas.Atlas var4 = null;

         for(int var5 = 0; var5 < this.AtlasList.size(); ++var5) {
            DeadBodyAtlas.Atlas var6 = (DeadBodyAtlas.Atlas)this.AtlasList.get(var5);
            if (!var6.isFull()) {
               var4 = var6;
               break;
            }
         }

         if (var4 == null) {
            var4 = new DeadBodyAtlas.Atlas(1024, 1024);
            if (this.fbo == null) {
               return null;
            }

            this.AtlasList.add(var4);
         }

         var3 = var4.addBody(var2);
         var3.lightKey = this.getLightKey(var1);
         var3.updateCounter = this.updateCounter;
         this.EntryMap.put(var2, var3);
         this.RenderJobs.add(DeadBodyAtlas.RenderJob.getNew().init(var1, var3));
         return var3.tex;
      }
   }

   public void checkLights(Texture var1, IsoDeadBody var2) {
      if (var1 != null) {
         DeadBodyAtlas.AtlasEntry var3 = (DeadBodyAtlas.AtlasEntry)this.EntryMap.get(var1.getName());
         if (var3 != null && var3.tex == var1) {
            if (var3.updateCounter != this.updateCounter) {
               var3.updateCounter = this.updateCounter;
               this.bodyParams.init(var2);
               String var4 = this.getLightKey(this.bodyParams);
               if (!var3.lightKey.equals(var4)) {
                  this.EntryMap.remove(var3.key);
                  var3.key = this.getBodyKey(this.bodyParams);
                  var3.lightKey = var4;
                  var1.setNameOnly(var3.key);
                  this.EntryMap.put(var3.key, var3);
                  DeadBodyAtlas.RenderJob var5 = DeadBodyAtlas.RenderJob.getNew().init(this.bodyParams, var3);
                  var5.bClearThisSlotOnly = true;
                  this.RenderJobs.add(var5);
                  this.render();
               }
            }
         }
      }
   }

   public void checkLights(Texture var1, IsoZombie var2) {
      if (var1 != null) {
         DeadBodyAtlas.AtlasEntry var3 = (DeadBodyAtlas.AtlasEntry)this.EntryMap.get(var1.getName());
         if (var3 != null && var3.tex == var1) {
            if (var3.updateCounter != this.updateCounter) {
               var3.updateCounter = this.updateCounter;
               this.bodyParams.init(var2);
               String var4 = this.getLightKey(this.bodyParams);
               if (!var3.lightKey.equals(var4)) {
                  this.EntryMap.remove(var3.key);
                  var3.key = this.getBodyKey(this.bodyParams);
                  var3.lightKey = var4;
                  var1.setNameOnly(var3.key);
                  this.EntryMap.put(var3.key, var3);
                  DeadBodyAtlas.RenderJob var5 = DeadBodyAtlas.RenderJob.getNew().init(this.bodyParams, var3);
                  var5.bClearThisSlotOnly = true;
                  this.RenderJobs.add(var5);
                  this.render();
               }
            }
         }
      }
   }

   private String getBodyKey(DeadBodyAtlas.BodyParams var1) {
      if (var1.humanVisual == this.characterTextureVisualFemale.humanVisual) {
         return "SZF_" + var1.animSetName + "_" + var1.stateName + "_" + var1.dir + "_" + var1.trackTime;
      } else if (var1.humanVisual == this.characterTextureVisualMale.humanVisual) {
         return "SZM_" + var1.animSetName + "_" + var1.stateName + "_" + var1.dir + "_" + var1.trackTime;
      } else {
         try {
            this.checksummer.reset();
            HumanVisual var2 = var1.humanVisual;
            this.checksummer.update((byte)var1.dir.index());
            this.checksummer.update((int)(PZMath.wrap(var1.angle, 0.0F, 6.2831855F) * 57.295776F));
            this.checksummer.update(var2.getHairModel());
            this.checksummer.update(var2.getBeardModel());
            this.checksummer.update(var2.getSkinColor());
            this.checksummer.update(var2.getSkinTexture());
            this.checksummer.update((int)(var2.getTotalBlood() * 100.0F));
            this.checksummer.update(var1.primaryHandItem);
            this.checksummer.update(var1.secondaryHandItem);

            for(int var3 = 0; var3 < var1.attachedModelNames.size(); ++var3) {
               AttachedModelName var4 = var1.attachedModelNames.get(var3);
               this.checksummer.update(var4.attachmentName);
               this.checksummer.update(var4.modelName);
               this.checksummer.update((int)(var4.bloodLevel * 100.0F));
            }

            this.checksummer.update(var1.bFemale);
            this.checksummer.update(var1.bZombie);
            this.checksummer.update(var1.bSkeleton);
            ItemVisuals var9 = var1.itemVisuals;

            for(int var10 = 0; var10 < var9.size(); ++var10) {
               ItemVisual var5 = (ItemVisual)var9.get(var10);
               ClothingItem var6 = var5.getClothingItem();
               if (var6 != null) {
                  this.checksummer.update(var5.getBaseTexture(var6));
                  this.checksummer.update(var5.getTextureChoice(var6));
                  this.checksummer.update(var5.getTint(var6));
                  this.checksummer.update(var6.getModel(var2.isFemale()));
                  this.checksummer.update((int)(var5.getTotalBlood() * 100.0F));
               }
            }

            this.checksummer.update(var1.fallOnFront);
            this.checksummer.update(var1.bStanding);
            this.checksummer.update(var1.bOutside);
            this.checksummer.update(var1.bRoom);
            float var11 = (float)((int)(var1.ambient.r * 10.0F)) / 10.0F;
            this.checksummer.update((byte)((int)(var11 * 255.0F)));
            float var12 = (float)((int)(var1.ambient.g * 10.0F)) / 10.0F;
            this.checksummer.update((byte)((int)(var12 * 255.0F)));
            float var13 = (float)((int)(var1.ambient.b * 10.0F)) / 10.0F;
            this.checksummer.update((byte)((int)(var13 * 255.0F)));
            this.checksummer.update((int)var1.trackTime);

            for(int var7 = 0; var7 < var1.lights.length; ++var7) {
               this.checksummer.update(var1.lights[var7], var1.x, var1.y, var1.z);
            }

            return this.checksummer.checksumToString();
         } catch (Throwable var8) {
            ExceptionLogger.logException(var8);
            return "bogus";
         }
      }
   }

   private String getLightKey(DeadBodyAtlas.BodyParams var1) {
      try {
         this.checksummer.reset();
         this.checksummer.update(var1.bOutside);
         this.checksummer.update(var1.bRoom);
         float var2 = (float)((int)(var1.ambient.r * 10.0F)) / 10.0F;
         this.checksummer.update((byte)((int)(var2 * 255.0F)));
         float var3 = (float)((int)(var1.ambient.g * 10.0F)) / 10.0F;
         this.checksummer.update((byte)((int)(var3 * 255.0F)));
         float var4 = (float)((int)(var1.ambient.b * 10.0F)) / 10.0F;
         this.checksummer.update((byte)((int)(var4 * 255.0F)));

         for(int var5 = 0; var5 < var1.lights.length; ++var5) {
            this.checksummer.update(var1.lights[var5], var1.x, var1.y, var1.z);
         }

         return this.checksummer.checksumToString();
      } catch (Throwable var6) {
         ExceptionLogger.logException(var6);
         return "bogus";
      }
   }

   public void render() {
      int var1;
      for(var1 = 0; var1 < this.AtlasList.size(); ++var1) {
         DeadBodyAtlas.Atlas var2 = (DeadBodyAtlas.Atlas)this.AtlasList.get(var1);
         if (var2.clear) {
            SpriteRenderer.instance.drawGeneric(new DeadBodyAtlas.ClearAtlasTexture(var2));
         }
      }

      if (!this.RenderJobs.isEmpty()) {
         for(var1 = 0; var1 < this.RenderJobs.size(); ++var1) {
            DeadBodyAtlas.RenderJob var3 = (DeadBodyAtlas.RenderJob)this.RenderJobs.get(var1);
            if (var3.done != 1 || var3.renderRefCount <= 0) {
               if (var3.done == 1 && var3.renderRefCount == 0) {
                  this.RenderJobs.remove(var1--);

                  assert !JobPool.contains(var3);

                  JobPool.push(var3);
               } else if (var3.renderMain()) {
                  ++var3.renderRefCount;
                  SpriteRenderer.instance.drawGeneric(var3);
               }
            }
         }

      }
   }

   public void renderDebug() {
      if (Core.bDebug && DebugOptions.instance.DeadBodyAtlasRender.getValue()) {
         if (JobPool.isEmpty()) {
            return;
         }

         if (((DeadBodyAtlas.RenderJob)JobPool.get(JobPool.size() - 1)).entry.atlas == null) {
            return;
         }

         int var1;
         if (this.debugDrawInWorld[0] == null) {
            for(var1 = 0; var1 < this.debugDrawInWorld.length; ++var1) {
               this.debugDrawInWorld[var1] = new DeadBodyAtlas.DebugDrawInWorld();
            }
         }

         var1 = SpriteRenderer.instance.getMainStateIndex();
         long var2 = System.currentTimeMillis();
         DeadBodyAtlas.RenderJob var4;
         if (var2 - this.debugDrawTime < 500L) {
            var4 = (DeadBodyAtlas.RenderJob)JobPool.pop();
            var4.done = 0;
            var4.bClearThisSlotOnly = true;
            this.RenderJobs.add(var4);
         } else if (var2 - this.debugDrawTime < 1000L) {
            var4 = (DeadBodyAtlas.RenderJob)JobPool.pop();
            var4.done = 0;
            var4.renderMain();
            this.debugDrawInWorld[var1].init(var4);
            SpriteRenderer.instance.drawGeneric(this.debugDrawInWorld[var1]);
         } else {
            this.debugDrawTime = var2;
         }
      }

   }

   public void renderUI() {
      if (Core.bDebug && DebugOptions.instance.DeadBodyAtlasRender.getValue()) {
         int var1 = 512 / Core.TileScale;
         int var2 = 0;
         int var3 = 0;

         for(int var4 = 0; var4 < this.AtlasList.size(); ++var4) {
            SpriteRenderer.instance.renderi((Texture)null, var2, var3, var1, var1, 1.0F, 1.0F, 1.0F, 0.75F, (Consumer)null);
            SpriteRenderer.instance.renderi(((DeadBodyAtlas.Atlas)this.AtlasList.get(var4)).tex, var2, var3, var1, var1, 1.0F, 1.0F, 1.0F, 1.0F, (Consumer)null);
            float var5 = (float)var1 / (float)((DeadBodyAtlas.Atlas)this.AtlasList.get(var4)).tex.getWidth();

            int var6;
            for(var6 = 0; var6 < ((DeadBodyAtlas.Atlas)this.AtlasList.get(var4)).tex.getWidth() / ENTRY_WID; ++var6) {
               SpriteRenderer.instance.renderline((Texture)null, (int)((float)var2 + (float)(var6 * ENTRY_WID) * var5), var3, (int)((float)var2 + (float)(var6 * ENTRY_WID) * var5), var3 + var1, 0.5F, 0.5F, 0.5F, 1.0F);
            }

            for(var6 = 0; var6 < ((DeadBodyAtlas.Atlas)this.AtlasList.get(var4)).tex.getHeight() / ENTRY_HGT; ++var6) {
               SpriteRenderer.instance.renderline((Texture)null, var2, (int)((float)var3 + (float)(var6 * ENTRY_HGT) * var5), var2 + var1, (int)((float)var3 + (float)(var6 * ENTRY_HGT) * var5), 0.5F, 0.5F, 0.5F, 1.0F);
            }

            var3 += var1;
            if (var3 + var1 > Core.getInstance().getScreenHeight()) {
               var3 = 0;
               var2 += var1;
            }
         }

         SpriteRenderer.instance.renderi((Texture)null, var2, var3, var1, var1, 1.0F, 1.0F, 1.0F, 0.5F, (Consumer)null);
         SpriteRenderer.instance.renderi((Texture)ModelManager.instance.bitmap.getTexture(), var2, var3, var1, var1, 1.0F, 1.0F, 1.0F, 1.0F, (Consumer)null);
      }

   }

   public void Reset() {
      if (this.fbo != null) {
         this.fbo.destroyLeaveTexture();
         this.fbo = null;
      }

      this.AtlasList.forEach(DeadBodyAtlas.Atlas::Reset);
      this.AtlasList.clear();
      this.EntryMap.clear();
      this.characterTexturesFemale.clear();
      this.characterTexturesMale.clear();
      JobPool.forEach(DeadBodyAtlas.RenderJob::Reset);
      JobPool.clear();
      this.RenderJobs.clear();
   }

   private void toBodyAtlas(DeadBodyAtlas.RenderJob var1) {
      GL11.glPushAttrib(2048);
      if (this.fbo.getTexture() != var1.entry.atlas.tex) {
         this.fbo.setTexture(var1.entry.atlas.tex);
      }

      this.fbo.startDrawing();
      GL11.glViewport(0, 0, this.fbo.getWidth(), this.fbo.getHeight());
      GL11.glMatrixMode(5889);
      GL11.glPushMatrix();
      GL11.glLoadIdentity();
      int var2 = var1.entry.atlas.tex.getWidth();
      int var3 = var1.entry.atlas.tex.getHeight();
      GLU.gluOrtho2D(0.0F, (float)var2, (float)var3, 0.0F);
      GL11.glMatrixMode(5888);
      GL11.glPushMatrix();
      GL11.glLoadIdentity();
      GL11.glEnable(3553);
      GL11.glDisable(3089);
      if (var1.entry.atlas.clear) {
         GL11.glClearColor(0.0F, 0.0F, 0.0F, 0.0F);
         GL11.glClear(16640);
         GL11.glClearColor(0.0F, 0.0F, 0.0F, 1.0F);
         var1.entry.atlas.clear = false;
      }

      int var4;
      int var5;
      int var6;
      int var7;
      if (var1.bClearThisSlotOnly) {
         GL11.glEnable(3089);
         GL11.glScissor(var1.entry.x, 1024 - var1.entry.y - var1.entry.h, var1.entry.w, var1.entry.h);
         GL11.glClearColor(0.0F, 0.0F, 0.0F, 0.0F);
         GL11.glClear(16640);
         GL11.glClearColor(0.0F, 0.0F, 0.0F, 1.0F);
         var4 = SpriteRenderer.instance.getRenderingPlayerIndex();
         var5 = var4 != 0 && var4 != 2 ? Core.getInstance().getOffscreenTrueWidth() / 2 : 0;
         var6 = var4 != 0 && var4 != 1 ? Core.getInstance().getOffscreenTrueHeight() / 2 : 0;
         var7 = Core.getInstance().getOffscreenTrueWidth();
         int var8 = Core.getInstance().getOffscreenTrueHeight();
         if (IsoPlayer.numPlayers > 1) {
            var7 /= 2;
         }

         if (IsoPlayer.numPlayers > 2) {
            var8 /= 2;
         }

         GL11.glScissor(var5, var6, var7, var8);
         GL11.glDisable(3089);
      }

      var4 = ModelManager.instance.bitmap.getTexture().getWidth() / 8 * Core.TileScale;
      var5 = ModelManager.instance.bitmap.getTexture().getHeight() / 8 * Core.TileScale;
      var6 = var1.entry.x - (var4 - ENTRY_WID) / 2;
      var7 = var1.entry.y - (var5 - ENTRY_HGT) / 2;
      ModelManager.instance.bitmap.getTexture().bind();
      GL11.glBegin(7);
      GL11.glColor3f(1.0F, 1.0F, 1.0F);
      GL11.glTexCoord2f(0.0F, 0.0F);
      GL11.glVertex2i(var6, var7);
      GL11.glTexCoord2f(1.0F, 0.0F);
      GL11.glVertex2i(var6 + var4, var7);
      GL11.glTexCoord2f(1.0F, 1.0F);
      GL11.glVertex2i(var6 + var4, var7 + var5);
      GL11.glTexCoord2f(0.0F, 1.0F);
      GL11.glVertex2i(var6, var7 + var5);
      GL11.glEnd();
      Texture.lastTextureID = 0;
      GL11.glBindTexture(3553, 0);
      this.fbo.endDrawing();
      GL11.glEnable(3089);
      GL11.glMatrixMode(5889);
      GL11.glPopMatrix();
      GL11.glMatrixMode(5888);
      GL11.glPopMatrix();
      GL11.glPopAttrib();
      var1.entry.ready = true;
      var1.done = 1;
   }

   static {
      ENTRY_WID = 102 * Core.TileScale;
      ENTRY_HGT = 102 * Core.TileScale;
      instance = new DeadBodyAtlas();
      tempVector2 = new Vector2();
      JobPool = new Stack();
   }

   private static final class BodyParams {
      HumanVisual humanVisual;
      final ItemVisuals itemVisuals = new ItemVisuals();
      IsoDirections dir;
      float angle;
      boolean bFemale;
      boolean bZombie;
      boolean bSkeleton;
      String animSetName;
      String stateName;
      final HashMap variables = new HashMap();
      boolean bStanding;
      String primaryHandItem;
      String secondaryHandItem;
      final AttachedModelNames attachedModelNames = new AttachedModelNames();
      float x;
      float y;
      float z;
      float trackTime;
      boolean bOutside;
      boolean bRoom;
      final ColorInfo ambient = new ColorInfo();
      boolean fallOnFront = false;
      final IsoGridSquare.ResultLight[] lights = new IsoGridSquare.ResultLight[5];

      BodyParams() {
         for(int var1 = 0; var1 < this.lights.length; ++var1) {
            this.lights[var1] = new IsoGridSquare.ResultLight();
         }

      }

      void init(DeadBodyAtlas.BodyParams var1) {
         this.humanVisual = var1.humanVisual;
         this.itemVisuals.clear();
         this.itemVisuals.addAll(var1.itemVisuals);
         this.dir = var1.dir;
         this.angle = var1.angle;
         this.bFemale = var1.bFemale;
         this.bZombie = var1.bZombie;
         this.bSkeleton = var1.bSkeleton;
         this.animSetName = var1.animSetName;
         this.stateName = var1.stateName;
         this.variables.clear();
         this.variables.putAll(var1.variables);
         this.bStanding = var1.bStanding;
         this.primaryHandItem = var1.primaryHandItem;
         this.secondaryHandItem = var1.secondaryHandItem;
         this.attachedModelNames.copyFrom(var1.attachedModelNames);
         this.x = var1.x;
         this.y = var1.y;
         this.z = var1.z;
         this.trackTime = var1.trackTime;
         this.fallOnFront = var1.fallOnFront;
         this.bOutside = var1.bOutside;
         this.bRoom = var1.bRoom;
         this.ambient.set(var1.ambient.r, var1.ambient.g, var1.ambient.b, 1.0F);

         for(int var2 = 0; var2 < this.lights.length; ++var2) {
            this.lights[var2].copyFrom(var1.lights[var2]);
         }

      }

      void init(IsoDeadBody var1) {
         this.humanVisual = var1.getHumanVisual();
         var1.getItemVisuals(this.itemVisuals);
         this.dir = var1.dir;
         this.angle = var1.getAngle();
         this.bFemale = var1.isFemale();
         this.bZombie = var1.isZombie();
         this.bSkeleton = var1.isSkeleton();
         this.primaryHandItem = null;
         this.secondaryHandItem = null;
         this.attachedModelNames.initFrom(var1.getAttachedItems());
         this.animSetName = "zombie";
         this.stateName = "onground";
         this.variables.clear();
         this.bStanding = false;
         if (var1.getPrimaryHandItem() != null || var1.getSecondaryHandItem() != null) {
            if (var1.getPrimaryHandItem() != null && !StringUtils.isNullOrEmpty(var1.getPrimaryHandItem().getStaticModel())) {
               this.primaryHandItem = var1.getPrimaryHandItem().getStaticModel();
            }

            if (var1.getSecondaryHandItem() != null && !StringUtils.isNullOrEmpty(var1.getSecondaryHandItem().getStaticModel())) {
               this.secondaryHandItem = var1.getSecondaryHandItem().getStaticModel();
            }

            this.animSetName = "player";
            this.stateName = "deadbody";
         }

         this.x = var1.x;
         this.y = var1.y;
         this.z = var1.z;
         this.trackTime = 0.0F;
         this.fallOnFront = var1.isFallOnFront();
         this.bOutside = var1.square != null && var1.square.isOutside();
         this.bRoom = var1.square != null && var1.square.getRoom() != null;
         this.initAmbient(var1.square);
         this.initLights(var1.square);
      }

      void init(IsoZombie var1) {
         this.humanVisual = var1.getHumanVisual();
         var1.getItemVisuals(this.itemVisuals);
         this.dir = var1.dir;
         this.angle = var1.getAnimAngleRadians();
         this.bFemale = var1.isFemale();
         this.bZombie = true;
         this.bSkeleton = var1.isSkeleton();
         this.primaryHandItem = null;
         this.secondaryHandItem = null;
         this.attachedModelNames.initFrom(var1.getAttachedItems());
         this.animSetName = "zombie";
         this.stateName = "onground";
         this.variables.clear();
         this.bStanding = false;
         this.x = var1.x;
         this.y = var1.y;
         this.z = var1.z;
         this.trackTime = 0.0F;
         this.fallOnFront = var1.isFallOnFront();
         this.bOutside = var1.getCurrentSquare() != null && var1.getCurrentSquare().isOutside();
         this.bRoom = var1.getCurrentSquare() != null && var1.getCurrentSquare().getRoom() != null;
         this.initAmbient(var1.getCurrentSquare());
         this.initLights(var1.getCurrentSquare());
      }

      void init(IsoMannequin var1) {
         this.humanVisual = var1.getHumanVisual();
         var1.getItemVisuals(this.itemVisuals);
         this.dir = var1.dir;
         this.angle = this.dir.ToVector().getDirection();
         this.bFemale = var1.isFemale();
         this.bZombie = var1.isZombie();
         this.bSkeleton = var1.isSkeleton();
         this.primaryHandItem = null;
         this.secondaryHandItem = null;
         this.attachedModelNames.clear();
         this.animSetName = "mannequin";
         this.stateName = var1.isFemale() ? "female" : "male";
         this.variables.clear();
         var1.getVariables(this.variables);
         this.bStanding = true;
         this.x = var1.getX();
         this.y = var1.getY();
         this.z = var1.getZ();
         this.trackTime = 0.0F;
         this.fallOnFront = false;
         this.bOutside = var1.square != null && var1.square.isOutside();
         this.bRoom = var1.square != null && var1.square.getRoom() != null;
         this.initAmbient(var1.square);
         this.initLights((IsoGridSquare)null);
      }

      void init(IHumanVisual var1, IsoDirections var2, String var3, String var4, float var5) {
         this.humanVisual = var1.getHumanVisual();
         var1.getItemVisuals(this.itemVisuals);
         this.dir = var2;
         this.angle = var2.ToVector().getDirection();
         this.bFemale = var1.isFemale();
         this.bZombie = var1.isZombie();
         this.bSkeleton = var1.isSkeleton();
         this.primaryHandItem = null;
         this.secondaryHandItem = null;
         this.attachedModelNames.clear();
         this.animSetName = var3;
         this.stateName = var4;
         this.variables.clear();
         this.bStanding = true;
         this.x = 0.0F;
         this.y = 0.0F;
         this.z = 0.0F;
         this.trackTime = var5;
         this.fallOnFront = false;
         this.bOutside = true;
         this.bRoom = false;
         this.ambient.set(1.0F, 1.0F, 1.0F, 1.0F);
         this.initLights((IsoGridSquare)null);
      }

      void initAmbient(IsoGridSquare var1) {
         this.ambient.set(1.0F, 1.0F, 1.0F, 1.0F);
      }

      void initLights(IsoGridSquare var1) {
         for(int var2 = 0; var2 < this.lights.length; ++var2) {
            this.lights[var2].radius = 0;
         }

         if (var1 != null) {
            IsoGridSquare.ILighting var5 = var1.lighting[0];
            int var3 = var5.resultLightCount();

            for(int var4 = 0; var4 < var3; ++var4) {
               this.lights[var4].copyFrom(var5.getResultLight(var4));
            }
         }

      }

      void Reset() {
         this.humanVisual = null;
         this.itemVisuals.clear();
         Arrays.fill(this.lights, (Object)null);
      }
   }

   private static final class Checksummer {
      private MessageDigest md;
      private final StringBuilder sb = new StringBuilder();

      public void reset() throws NoSuchAlgorithmException {
         if (this.md == null) {
            this.md = MessageDigest.getInstance("MD5");
         }

         this.md.reset();
      }

      public void update(byte var1) {
         this.md.update(var1);
      }

      public void update(boolean var1) {
         this.md.update((byte)(var1 ? 1 : 0));
      }

      public void update(int var1) {
         this.md.update((byte)(var1 & 255));
         this.md.update((byte)(var1 >> 8 & 255));
         this.md.update((byte)(var1 >> 16 & 255));
         this.md.update((byte)(var1 >> 24 & 255));
      }

      public void update(String var1) {
         if (var1 != null && !var1.isEmpty()) {
            this.md.update(var1.getBytes());
         }
      }

      public void update(ImmutableColor var1) {
         this.update((byte)((int)(var1.r * 255.0F)));
         this.update((byte)((int)(var1.g * 255.0F)));
         this.update((byte)((int)(var1.b * 255.0F)));
      }

      public void update(IsoGridSquare.ResultLight var1, float var2, float var3, float var4) {
         if (var1 != null && var1.radius > 0) {
            this.update((int)((float)var1.x - var2));
            this.update((int)((float)var1.y - var3));
            this.update((int)((float)var1.z - var4));
            this.update((byte)((int)(var1.r * 255.0F)));
            this.update((byte)((int)(var1.g * 255.0F)));
            this.update((byte)((int)(var1.b * 255.0F)));
            this.update((byte)var1.radius);
         }
      }

      public String checksumToString() {
         byte[] var1 = this.md.digest();
         this.sb.setLength(0);

         for(int var2 = 0; var2 < var1.length; ++var2) {
            this.sb.append(var1[var2] & 255);
         }

         return this.sb.toString();
      }
   }

   private static final class DebugDrawInWorld extends TextureDraw.GenericDrawer {
      DeadBodyAtlas.RenderJob job;
      boolean bRendered;

      public void init(DeadBodyAtlas.RenderJob var1) {
         this.job = var1;
         this.bRendered = false;
      }

      public void render() {
         this.job.animatedModel.DoRenderToWorld(this.job.body.x, this.job.body.y, this.job.body.z, this.job.m_animPlayerAngle);
         this.bRendered = true;
      }

      public void postRender() {
         if (this.bRendered) {
            assert !DeadBodyAtlas.JobPool.contains(this.job);

            DeadBodyAtlas.JobPool.push(this.job);
         } else {
            assert !DeadBodyAtlas.JobPool.contains(this.job);

            DeadBodyAtlas.JobPool.push(this.job);
         }

         this.job.animatedModel.postRender(this.bRendered);
      }
   }

   private static final class CharacterTextureVisual implements IHumanVisual {
      final HumanVisual humanVisual = new HumanVisual(this);
      boolean bFemale;

      CharacterTextureVisual(boolean var1) {
         this.bFemale = var1;
         this.humanVisual.setHairModel("");
         this.humanVisual.setBeardModel("");
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
         return true;
      }

      public boolean isSkeleton() {
         return false;
      }
   }

   private static final class AtlasEntry {
      public DeadBodyAtlas.Atlas atlas;
      public String key;
      public String lightKey;
      public int updateCounter;
      public int x;
      public int y;
      public int w;
      public int h;
      public Texture tex;
      public boolean ready = false;

      public void Reset() {
         this.atlas = null;
         this.tex.destroy();
         this.tex = null;
         this.ready = false;
      }
   }

   private final class Atlas {
      public Texture tex;
      public final ArrayList EntryList = new ArrayList();
      public boolean clear = true;

      public Atlas(int var2, int var3) {
         this.tex = new Texture(var2, var3, 16);
         if (DeadBodyAtlas.this.fbo == null) {
            DeadBodyAtlas.this.fbo = new TextureFBO(this.tex, false);
         }
      }

      public boolean isFull() {
         int var1 = this.tex.getWidth() / DeadBodyAtlas.ENTRY_WID;
         int var2 = this.tex.getHeight() / DeadBodyAtlas.ENTRY_HGT;
         return this.EntryList.size() >= var1 * var2;
      }

      public DeadBodyAtlas.AtlasEntry addBody(String var1) {
         int var2 = this.tex.getWidth() / DeadBodyAtlas.ENTRY_WID;
         int var3 = this.EntryList.size();
         int var4 = var3 % var2;
         int var5 = var3 / var2;
         DeadBodyAtlas.AtlasEntry var6 = new DeadBodyAtlas.AtlasEntry();
         var6.atlas = this;
         var6.key = var1;
         var6.x = var4 * DeadBodyAtlas.ENTRY_WID;
         var6.y = var5 * DeadBodyAtlas.ENTRY_HGT;
         var6.w = DeadBodyAtlas.ENTRY_WID;
         var6.h = DeadBodyAtlas.ENTRY_HGT;
         var6.tex = this.tex.split(var1, var6.x, this.tex.getHeight() - (var6.y + DeadBodyAtlas.ENTRY_HGT), var6.w, var6.h);
         var6.tex.setName(var1);
         this.EntryList.add(var6);
         return var6;
      }

      public void Reset() {
         this.EntryList.forEach(DeadBodyAtlas.AtlasEntry::Reset);
         this.EntryList.clear();
         if (!this.tex.isDestroyed()) {
            RenderThread.invokeOnRenderContext(() -> {
               GL11.glDeleteTextures(this.tex.getID());
            });
         }

         this.tex = null;
      }
   }

   private static final class RenderJob extends TextureDraw.GenericDrawer {
      public final DeadBodyAtlas.BodyParams body = new DeadBodyAtlas.BodyParams();
      public DeadBodyAtlas.AtlasEntry entry;
      public AnimatedModel animatedModel;
      public float m_animPlayerAngle;
      public int done = 0;
      public int renderRefCount;
      public boolean bClearThisSlotOnly;

      public static DeadBodyAtlas.RenderJob getNew() {
         return DeadBodyAtlas.JobPool.isEmpty() ? new DeadBodyAtlas.RenderJob() : (DeadBodyAtlas.RenderJob)DeadBodyAtlas.JobPool.pop();
      }

      public DeadBodyAtlas.RenderJob init(DeadBodyAtlas.BodyParams var1, DeadBodyAtlas.AtlasEntry var2) {
         this.body.init(var1);
         this.entry = var2;
         if (this.animatedModel == null) {
            this.animatedModel = new AnimatedModel();
            this.animatedModel.setAnimate(false);
         }

         if (var1.bStanding) {
            this.animatedModel.setOffset(0.0F, -0.45F, 0.0F);
         } else {
            this.animatedModel.setOffset(0.0F, 0.0F, 0.0F);
         }

         this.animatedModel.setAnimSetName(var1.animSetName);
         this.animatedModel.setState(var1.stateName);
         this.animatedModel.setPrimaryHandModelName(var1.primaryHandItem);
         this.animatedModel.setSecondaryHandModelName(var1.secondaryHandItem);
         this.animatedModel.setAttachedModelNames(var1.attachedModelNames);
         this.animatedModel.setAmbient(var1.ambient, var1.bOutside, var1.bRoom);
         this.animatedModel.setLights(var1.lights, var1.x, var1.y, var1.z);
         this.animatedModel.setModelData(var1.humanVisual, var1.itemVisuals);
         this.animatedModel.setAngle(DeadBodyAtlas.tempVector2.setLengthAndDirection(var1.angle, 1.0F));
         this.animatedModel.setVariable("FallOnFront", var1.fallOnFront);
         var1.variables.forEach((var1x, var2x) -> {
            this.animatedModel.setVariable(var1x, var2x);
         });
         this.animatedModel.setTrackTime(var1.trackTime);
         this.animatedModel.update();
         this.bClearThisSlotOnly = false;
         this.done = 0;
         this.renderRefCount = 0;
         return this;
      }

      public boolean renderMain() {
         if (this.animatedModel.isReadyToRender()) {
            this.animatedModel.renderMain();
            this.m_animPlayerAngle = this.animatedModel.getAnimationPlayer().getRenderedAngle();
            return true;
         } else {
            return false;
         }
      }

      public void render() {
         if (this.done != 1) {
            GL11.glDepthMask(true);
            GL11.glColorMask(true, true, true, true);
            GL11.glDisable(3089);
            GL11.glPushAttrib(2048);
            ModelManager.instance.bitmap.startDrawing(true, true);
            GL11.glViewport(0, 0, ModelManager.instance.bitmap.getWidth(), ModelManager.instance.bitmap.getHeight());
            this.animatedModel.DoRender(0, 0, ModelManager.instance.bitmap.getTexture().getWidth(), ModelManager.instance.bitmap.getTexture().getHeight(), 42.75F, this.m_animPlayerAngle);
            ModelManager.instance.bitmap.endDrawing();
            GL11.glPopAttrib();
            if (this.animatedModel.isRendered()) {
               DeadBodyAtlas.instance.toBodyAtlas(this);
            }
         }
      }

      public void postRender() {
         this.animatedModel.postRender(this.done == 1);

         assert this.renderRefCount > 0;

         --this.renderRefCount;
      }

      public void Reset() {
         this.body.Reset();
         this.entry = null;
         if (this.animatedModel != null) {
            this.animatedModel.releaseAnimationPlayer();
            this.animatedModel = null;
         }

      }
   }

   private static final class ClearAtlasTexture extends TextureDraw.GenericDrawer {
      DeadBodyAtlas.Atlas m_atlas;

      ClearAtlasTexture(DeadBodyAtlas.Atlas var1) {
         this.m_atlas = var1;
      }

      public void render() {
         TextureFBO var1 = DeadBodyAtlas.instance.fbo;
         if (var1 != null && this.m_atlas.tex != null) {
            if (this.m_atlas.clear) {
               if (var1.getTexture() != this.m_atlas.tex) {
                  var1.setTexture(this.m_atlas.tex);
               }

               var1.startDrawing(false, false);
               GL11.glPushAttrib(2048);
               GL11.glViewport(0, 0, var1.getWidth(), var1.getHeight());
               GL11.glMatrixMode(5889);
               GL11.glPushMatrix();
               GL11.glLoadIdentity();
               int var2 = this.m_atlas.tex.getWidth();
               int var3 = this.m_atlas.tex.getHeight();
               GLU.gluOrtho2D(0.0F, (float)var2, (float)var3, 0.0F);
               GL11.glMatrixMode(5888);
               GL11.glPushMatrix();
               GL11.glLoadIdentity();
               GL11.glDisable(3089);
               GL11.glClearColor(0.0F, 0.0F, 0.0F, 0.0F);
               GL11.glClear(16640);
               GL11.glClearColor(0.0F, 0.0F, 0.0F, 1.0F);
               var1.endDrawing();
               GL11.glEnable(3089);
               GL11.glMatrixMode(5889);
               GL11.glPopMatrix();
               GL11.glMatrixMode(5888);
               GL11.glPopMatrix();
               GL11.glPopAttrib();
               this.m_atlas.clear = false;
            }
         }
      }
   }
}

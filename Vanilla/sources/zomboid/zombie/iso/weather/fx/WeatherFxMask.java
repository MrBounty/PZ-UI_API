package zombie.iso.weather.fx;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.function.Consumer;
import org.joml.Vector2i;
import org.joml.Vector3f;
import zombie.IndieGL;
import zombie.Lua.LuaManager;
import zombie.characters.IsoGameCharacter;
import zombie.characters.IsoPlayer;
import zombie.core.Color;
import zombie.core.Core;
import zombie.core.PerformanceSettings;
import zombie.core.SpriteRenderer;
import zombie.core.opengl.RenderSettings;
import zombie.core.textures.ColorInfo;
import zombie.core.textures.Texture;
import zombie.core.textures.TextureFBO;
import zombie.debug.DebugLog;
import zombie.debug.DebugOptions;
import zombie.input.GameKeyboard;
import zombie.iso.DiamondMatrixIterator;
import zombie.iso.IsoCamera;
import zombie.iso.IsoChunk;
import zombie.iso.IsoChunkMap;
import zombie.iso.IsoDirections;
import zombie.iso.IsoGridSquare;
import zombie.iso.IsoObject;
import zombie.iso.IsoUtils;
import zombie.iso.IsoWorld;
import zombie.iso.SpriteDetails.IsoFlagType;
import zombie.iso.SpriteDetails.IsoObjectType;
import zombie.iso.areas.isoregion.IsoRegions;
import zombie.iso.areas.isoregion.regions.IWorldRegion;
import zombie.iso.areas.isoregion.regions.IsoWorldRegion;
import zombie.iso.sprite.IsoSprite;
import zombie.iso.sprite.IsoSpriteManager;
import zombie.network.GameServer;

public class WeatherFxMask {
   private static boolean DEBUG_KEYS = false;
   private static TextureFBO fboMask;
   private static TextureFBO fboParticles;
   public static IsoSprite floorSprite;
   public static IsoSprite wallNSprite;
   public static IsoSprite wallWSprite;
   public static IsoSprite wallNWSprite;
   public static IsoSprite wallSESprite;
   private static Texture texWhite;
   private static int curPlayerIndex;
   public static final int BIT_FLOOR = 0;
   public static final int BIT_WALLN = 1;
   public static final int BIT_WALLW = 2;
   public static final int BIT_IS_CUT = 4;
   public static final int BIT_CHARS = 8;
   public static final int BIT_OBJECTS = 16;
   public static final int BIT_WALL_SE = 32;
   public static final int BIT_DOOR = 64;
   public static float offsetX;
   public static float offsetY;
   public static ColorInfo defColorInfo;
   private static int DIAMOND_ROWS;
   public int x;
   public int y;
   public int z;
   public int flags;
   public IsoGridSquare gs;
   public boolean enabled;
   private static WeatherFxMask.PlayerFxMask[] playerMasks;
   private static DiamondMatrixIterator dmiter;
   private static final Vector2i diamondMatrixPos;
   private static Vector3f tmpVec;
   private static IsoGameCharacter.TorchInfo tmpTorch;
   private static ColorInfo tmpColInfo;
   private static int[] test;
   private static String[] testNames;
   private static int var1;
   private static int var2;
   private static float var3;
   private static int SCR_MASK_ADD;
   private static int DST_MASK_ADD;
   private static int SCR_MASK_SUB;
   private static int DST_MASK_SUB;
   private static int SCR_PARTICLES;
   private static int DST_PARTICLES;
   private static int SCR_MERGE;
   private static int DST_MERGE;
   private static int SCR_FINAL;
   private static int DST_FINAL;
   private static int ID_SCR_MASK_ADD;
   private static int ID_DST_MASK_ADD;
   private static int ID_SCR_MASK_SUB;
   private static int ID_DST_MASK_SUB;
   private static int ID_SCR_MERGE;
   private static int ID_DST_MERGE;
   private static int ID_SCR_FINAL;
   private static int ID_DST_FINAL;
   private static int ID_SCR_PARTICLES;
   private static int ID_DST_PARTICLES;
   private static int TARGET_BLEND;
   private static boolean DEBUG_MASK;
   public static boolean MASKING_ENABLED;
   private static boolean DEBUG_MASK_AND_PARTICLES;
   private static final boolean DEBUG_THROTTLE_KEYS = true;
   private static int keypause;

   public static TextureFBO getFboMask() {
      return fboMask;
   }

   public static TextureFBO getFboParticles() {
      return fboParticles;
   }

   public static void init() throws Exception {
      if (!GameServer.bServer) {
         for(int var0 = 0; var0 < playerMasks.length; ++var0) {
            playerMasks[var0] = new WeatherFxMask.PlayerFxMask();
         }

         playerMasks[0].init();
         initGlIds();
         floorSprite = IsoSpriteManager.instance.getSprite("floors_interior_tilesandwood_01_16");
         wallNSprite = IsoSpriteManager.instance.getSprite("walls_interior_house_01_21");
         wallWSprite = IsoSpriteManager.instance.getSprite("walls_interior_house_01_20");
         wallNWSprite = IsoSpriteManager.instance.getSprite("walls_interior_house_01_22");
         wallSESprite = IsoSpriteManager.instance.getSprite("walls_interior_house_01_23");
         texWhite = Texture.getSharedTexture("media/textures/weather/fogwhite.png");
      }
   }

   public static boolean checkFbos() {
      if (GameServer.bServer) {
         return false;
      } else {
         TextureFBO var0 = Core.getInstance().getOffscreenBuffer();
         if (Core.getInstance().getOffscreenBuffer() == null) {
            DebugLog.log("fbo=" + (var0 != null));
            return false;
         } else {
            int var1x = Core.getInstance().getScreenWidth();
            int var2x = Core.getInstance().getScreenHeight();
            if (fboMask != null && fboParticles != null && fboMask.getTexture().getWidth() == var1x && fboMask.getTexture().getHeight() == var2x) {
               return fboMask != null && fboParticles != null;
            } else {
               if (fboMask != null) {
                  fboMask.destroy();
               }

               if (fboParticles != null) {
                  fboParticles.destroy();
               }

               fboMask = null;
               fboParticles = null;

               Texture var3x;
               try {
                  var3x = new Texture(var1x, var2x, 16);
                  fboMask = new TextureFBO(var3x);
               } catch (Exception var5) {
                  DebugLog.log((Object)var5.getStackTrace());
                  var5.printStackTrace();
               }

               try {
                  var3x = new Texture(var1x, var2x, 16);
                  fboParticles = new TextureFBO(var3x);
               } catch (Exception var4) {
                  DebugLog.log((Object)var4.getStackTrace());
                  var4.printStackTrace();
               }

               return fboMask != null && fboParticles != null;
            }
         }
      }
   }

   public static void destroy() {
      if (fboMask != null) {
         fboMask.destroy();
      }

      fboMask = null;
      if (fboParticles != null) {
         fboParticles.destroy();
      }

      fboParticles = null;
   }

   public static void initMask() {
      if (!GameServer.bServer) {
         curPlayerIndex = IsoCamera.frameState.playerIndex;
         playerMasks[curPlayerIndex].initMask();
      }
   }

   private static boolean isOnScreen(int var0, int var1x, int var2x) {
      float var3x = (float)((int)IsoUtils.XToScreenInt(var0, var1x, var2x, 0));
      float var4 = (float)((int)IsoUtils.YToScreenInt(var0, var1x, var2x, 0));
      var3x -= (float)((int)IsoCamera.frameState.OffX);
      var4 -= (float)((int)IsoCamera.frameState.OffY);
      if (var3x + (float)(32 * Core.TileScale) <= 0.0F) {
         return false;
      } else if (var4 + (float)(32 * Core.TileScale) <= 0.0F) {
         return false;
      } else if (var3x - (float)(32 * Core.TileScale) >= (float)IsoCamera.frameState.OffscreenWidth) {
         return false;
      } else {
         return !(var4 - (float)(96 * Core.TileScale) >= (float)IsoCamera.frameState.OffscreenHeight);
      }
   }

   public boolean isLoc(int var1x, int var2x, int var3x) {
      return this.x == var1x && this.y == var2x && this.z == var3x;
   }

   public static boolean playerHasMaskToDraw(int var0) {
      return var0 < playerMasks.length ? playerMasks[var0].hasMaskToDraw : false;
   }

   public static void setDiamondIterDone(int var0) {
      if (var0 < playerMasks.length) {
         playerMasks[var0].DIAMOND_ITER_DONE = true;
      }

   }

   public static void forceMaskUpdate(int var0) {
      if (var0 < playerMasks.length) {
         playerMasks[var0].plrSquare = null;
      }

   }

   public static void forceMaskUpdateAll() {
      if (!GameServer.bServer) {
         for(int var0 = 0; var0 < playerMasks.length; ++var0) {
            playerMasks[var0].plrSquare = null;
         }

      }
   }

   private static boolean getIsStairs(IsoGridSquare var0) {
      return var0 != null && (var0.Has(IsoObjectType.stairsBN) || var0.Has(IsoObjectType.stairsBW) || var0.Has(IsoObjectType.stairsMN) || var0.Has(IsoObjectType.stairsMW) || var0.Has(IsoObjectType.stairsTN) || var0.Has(IsoObjectType.stairsTW));
   }

   private static boolean getHasDoor(IsoGridSquare var0) {
      return var0 != null && (var0.Is(IsoFlagType.cutN) || var0.Is(IsoFlagType.cutW)) && (var0.Is(IsoFlagType.DoorWallN) || var0.Is(IsoFlagType.DoorWallW)) && !var0.Is(IsoFlagType.doorN) && !var0.Is(IsoFlagType.doorW) ? var0.getCanSee(curPlayerIndex) : false;
   }

   public static void addMaskLocation(IsoGridSquare var0, int var1x, int var2x, int var3x) {
      if (!GameServer.bServer) {
         WeatherFxMask.PlayerFxMask var4 = playerMasks[curPlayerIndex];
         if (var4.requiresUpdate) {
            if (var4.hasMaskToDraw && var4.playerZ == var3x) {
               IsoGridSquare var5;
               boolean var6;
               boolean var7;
               if (isInPlayerBuilding(var0, var1x, var2x, var3x)) {
                  var5 = IsoWorld.instance.getCell().getChunkMap(curPlayerIndex).getGridSquare(var1x, var2x - 1, var3x);
                  var6 = !isInPlayerBuilding(var5, var1x, var2x - 1, var3x);
                  var5 = IsoWorld.instance.getCell().getChunkMap(curPlayerIndex).getGridSquare(var1x - 1, var2x, var3x);
                  var7 = !isInPlayerBuilding(var5, var1x - 1, var2x, var3x);
                  var5 = IsoWorld.instance.getCell().getChunkMap(curPlayerIndex).getGridSquare(var1x - 1, var2x - 1, var3x);
                  boolean var8 = !isInPlayerBuilding(var5, var1x - 1, var2x - 1, var3x);
                  int var9 = 0;
                  if (var6) {
                     var9 |= 1;
                  }

                  if (var7) {
                     var9 |= 2;
                  }

                  if (var8) {
                     var9 |= 32;
                  }

                  boolean var10 = false;
                  boolean var11 = getIsStairs(var0);
                  if (var0 != null && (var6 || var7 || var8)) {
                     byte var12 = 24;
                     if (var6 && !var0.getProperties().Is(IsoFlagType.WallN) && !var0.Is(IsoFlagType.WallNW)) {
                        var4.addMask(var1x - 1, var2x, var3x, (IsoGridSquare)null, 8, false);
                        var4.addMask(var1x, var2x, var3x, var0, var12);
                        var4.addMask(var1x + 1, var2x, var3x, (IsoGridSquare)null, var12, false);
                        var4.addMask(var1x + 2, var2x, var3x, (IsoGridSquare)null, 8, false);
                        var4.addMask(var1x, var2x + 1, var3x, (IsoGridSquare)null, 8, false);
                        var4.addMask(var1x + 1, var2x + 1, var3x, (IsoGridSquare)null, var12, false);
                        var4.addMask(var1x + 2, var2x + 1, var3x, (IsoGridSquare)null, var12, false);
                        var4.addMask(var1x + 2, var2x + 2, var3x, (IsoGridSquare)null, 16, false);
                        var4.addMask(var1x + 3, var2x + 2, var3x, (IsoGridSquare)null, 16, false);
                        var10 = true;
                     }

                     if (var7 && !var0.getProperties().Is(IsoFlagType.WallW) && !var0.getProperties().Is(IsoFlagType.WallNW)) {
                        var4.addMask(var1x, var2x - 1, var3x, (IsoGridSquare)null, 8, false);
                        var4.addMask(var1x, var2x, var3x, var0, var12);
                        var4.addMask(var1x, var2x + 1, var3x, (IsoGridSquare)null, var12, false);
                        var4.addMask(var1x, var2x + 2, var3x, (IsoGridSquare)null, 8, false);
                        var4.addMask(var1x + 1, var2x, var3x, (IsoGridSquare)null, 8, false);
                        var4.addMask(var1x + 1, var2x + 1, var3x, (IsoGridSquare)null, var12, false);
                        var4.addMask(var1x + 1, var2x + 2, var3x, (IsoGridSquare)null, var12, false);
                        var4.addMask(var1x + 2, var2x + 2, var3x, (IsoGridSquare)null, 16, false);
                        var4.addMask(var1x + 2, var2x + 3, var3x, (IsoGridSquare)null, 16, false);
                        var10 = true;
                     }

                     if (var8) {
                        int var13 = var11 ? var12 : var9;
                        var4.addMask(var1x, var2x, var3x, var0, var13);
                        var10 = true;
                     }
                  }

                  if (!var10) {
                     int var15 = var11 ? 24 : var9;
                     var4.addMask(var1x, var2x, var3x, var0, var15);
                  }
               } else {
                  var5 = IsoWorld.instance.getCell().getChunkMap(curPlayerIndex).getGridSquare(var1x, var2x - 1, var3x);
                  var6 = isInPlayerBuilding(var5, var1x, var2x - 1, var3x);
                  var5 = IsoWorld.instance.getCell().getChunkMap(curPlayerIndex).getGridSquare(var1x - 1, var2x, var3x);
                  var7 = isInPlayerBuilding(var5, var1x - 1, var2x, var3x);
                  if (!var6 && !var7) {
                     var5 = IsoWorld.instance.getCell().getChunkMap(curPlayerIndex).getGridSquare(var1x - 1, var2x - 1, var3x);
                     if (isInPlayerBuilding(var5, var1x - 1, var2x - 1, var3x)) {
                        var4.addMask(var1x, var2x, var3x, var0, 4);
                     }
                  } else {
                     int var14 = 4;
                     if (var6) {
                        var14 |= 1;
                     }

                     if (var7) {
                        var14 |= 2;
                     }

                     if (getHasDoor(var0)) {
                        var14 |= 64;
                     }

                     var4.addMask(var1x, var2x, var3x, var0, var14);
                  }
               }

            }
         }
      }
   }

   private static boolean isInPlayerBuilding(IsoGridSquare var0, int var1x, int var2x, int var3x) {
      WeatherFxMask.PlayerFxMask var4 = playerMasks[curPlayerIndex];
      if (var0 != null && var0.Is(IsoFlagType.solidfloor)) {
         if (var0.getBuilding() != null && var0.getBuilding() == var4.player.getBuilding()) {
            return true;
         }

         if (var0.getBuilding() == null) {
            return var4.curIsoWorldRegion != null && var0.getIsoWorldRegion() != null && var0.getIsoWorldRegion().isFogMask() && (var0.getIsoWorldRegion() == var4.curIsoWorldRegion || var4.curConnectedRegions.contains(var0.getIsoWorldRegion()));
         }
      } else {
         if (isInteriorLocation(var1x, var2x, var3x)) {
            return true;
         }

         if (var0 != null && var0.getBuilding() == null) {
            return var4.curIsoWorldRegion != null && var0.getIsoWorldRegion() != null && var0.getIsoWorldRegion().isFogMask() && (var0.getIsoWorldRegion() == var4.curIsoWorldRegion || var4.curConnectedRegions.contains(var0.getIsoWorldRegion()));
         }

         if (var0 == null && var4.curIsoWorldRegion != null) {
            IWorldRegion var5 = IsoRegions.getIsoWorldRegion(var1x, var2x, var3x);
            return var5 != null && var5.isFogMask() && (var5 == var4.curIsoWorldRegion || var4.curConnectedRegions.contains(var5));
         }
      }

      return false;
   }

   private static boolean isInteriorLocation(int var0, int var1x, int var2x) {
      WeatherFxMask.PlayerFxMask var3x = playerMasks[curPlayerIndex];

      for(int var5 = var2x; var5 >= 0; --var5) {
         IsoGridSquare var4 = IsoWorld.instance.getCell().getChunkMap(curPlayerIndex).getGridSquare(var0, var1x, var5);
         if (var4 != null) {
            if (var4.getBuilding() != null && var4.getBuilding() == var3x.player.getBuilding()) {
               return true;
            }

            if (var4.Is(IsoFlagType.exterior)) {
               return false;
            }
         }
      }

      return false;
   }

   private static void scanForTiles(int var0) {
      WeatherFxMask.PlayerFxMask var1x = playerMasks[curPlayerIndex];
      if (!var1x.DIAMOND_ITER_DONE) {
         IsoPlayer var2x = IsoPlayer.players[var0];
         int var3x = (int)var2x.getZ();
         byte var4 = 0;
         byte var5 = 0;
         int var6 = var4 + IsoCamera.getOffscreenWidth(var0);
         int var7 = var5 + IsoCamera.getOffscreenHeight(var0);
         float var8 = IsoUtils.XToIso((float)var4, (float)var5, 0.0F);
         float var9 = IsoUtils.YToIso((float)var6, (float)var5, 0.0F);
         float var10 = IsoUtils.XToIso((float)var6, (float)var7, 6.0F);
         float var11 = IsoUtils.YToIso((float)var4, (float)var7, 6.0F);
         float var12 = IsoUtils.XToIso((float)var6, (float)var5, 0.0F);
         int var13 = (int)var9;
         int var14 = (int)var11;
         int var15 = (int)var8;
         int var16 = (int)var10;
         DIAMOND_ROWS = (int)var12 * 4;
         var15 -= 2;
         var13 -= 2;
         dmiter.reset(var16 - var15);
         Vector2i var18 = diamondMatrixPos;
         IsoChunkMap var19 = IsoWorld.instance.getCell().getChunkMap(var0);

         while(dmiter.next(var18)) {
            if (var18 != null) {
               IsoGridSquare var17 = var19.getGridSquare(var18.x + var15, var18.y + var13, var3x);
               if (var17 == null) {
                  addMaskLocation((IsoGridSquare)null, var18.x + var15, var18.y + var13, var3x);
               } else {
                  IsoChunk var20 = var17.getChunk();
                  if (var20 != null && var17.IsOnScreen()) {
                     addMaskLocation(var17, var18.x + var15, var18.y + var13, var3x);
                  }
               }
            }
         }

      }
   }

   private static void renderMaskFloor(int var0, int var1x, int var2x) {
      floorSprite.render((IsoObject)null, (float)var0, (float)var1x, (float)var2x, IsoDirections.N, offsetX, offsetY, defColorInfo, false);
   }

   private static void renderMaskWall(IsoGridSquare var0, int var1x, int var2x, int var3x, boolean var4, boolean var5, int var6) {
      if (var0 != null) {
         IsoGridSquare var7 = var0.nav[IsoDirections.S.index()];
         IsoGridSquare var8 = var0.nav[IsoDirections.E.index()];
         long var9 = System.currentTimeMillis();
         boolean var11 = var7 != null && var7.getPlayerCutawayFlag(var6, var9);
         boolean var12 = var0.getPlayerCutawayFlag(var6, var9);
         boolean var13 = var8 != null && var8.getPlayerCutawayFlag(var6, var9);
         IsoSprite var14;
         IsoDirections var15;
         if (var4 && var5) {
            var14 = wallNWSprite;
            var15 = IsoDirections.NW;
         } else if (var4) {
            var14 = wallNSprite;
            var15 = IsoDirections.N;
         } else if (var5) {
            var14 = wallWSprite;
            var15 = IsoDirections.W;
         } else {
            var14 = wallSESprite;
            var15 = IsoDirections.W;
         }

         var0.DoCutawayShaderSprite(var14, var15, var11, var12, var13);
      }
   }

   private static void renderMaskWallNoCuts(int var0, int var1x, int var2x, boolean var3x, boolean var4) {
      if (var3x && var4) {
         wallNWSprite.render((IsoObject)null, (float)var0, (float)var1x, (float)var2x, IsoDirections.N, offsetX, offsetY, defColorInfo, false);
      } else if (var3x) {
         wallNSprite.render((IsoObject)null, (float)var0, (float)var1x, (float)var2x, IsoDirections.N, offsetX, offsetY, defColorInfo, false);
      } else if (var4) {
         wallWSprite.render((IsoObject)null, (float)var0, (float)var1x, (float)var2x, IsoDirections.N, offsetX, offsetY, defColorInfo, false);
      } else {
         wallSESprite.render((IsoObject)null, (float)var0, (float)var1x, (float)var2x, IsoDirections.N, offsetX, offsetY, defColorInfo, false);
      }

   }

   public static void renderFxMask(int var0) {
      if (DebugOptions.instance.Weather.Fx.getValue()) {
         if (!GameServer.bServer) {
            if (IsoWeatherFX.instance != null) {
               if (LuaManager.thread == null || !LuaManager.thread.bStep) {
                  if (DEBUG_KEYS && Core.bDebug) {
                     updateDebugKeys();
                  }

                  if (playerMasks[var0].maskEnabled) {
                     WeatherFxMask.PlayerFxMask var1x = playerMasks[curPlayerIndex];
                     if (var1x.maskEnabled) {
                        if (MASKING_ENABLED && !checkFbos()) {
                           MASKING_ENABLED = false;
                        }

                        if (MASKING_ENABLED && var1x.hasMaskToDraw) {
                           scanForTiles(var0);
                           int var2x = IsoCamera.getOffscreenLeft(var0);
                           int var3x = IsoCamera.getOffscreenTop(var0);
                           int var4 = IsoCamera.getOffscreenWidth(var0);
                           int var5 = IsoCamera.getOffscreenHeight(var0);
                           int var6 = IsoCamera.getScreenWidth(var0);
                           int var7 = IsoCamera.getScreenHeight(var0);
                           SpriteRenderer.instance.glIgnoreStyles(true);
                           if (MASKING_ENABLED) {
                              SpriteRenderer.instance.glBuffer(4, var0);
                              SpriteRenderer.instance.glDoStartFrameFx(var4, var5, var0);
                              if (PerformanceSettings.LightingFrameSkip < 3) {
                                 IsoWorld.instance.getCell().DrawStencilMask();
                                 SpriteRenderer.instance.glClearColor(0, 0, 0, 0);
                                 SpriteRenderer.instance.glClear(16640);
                                 SpriteRenderer.instance.glClearColor(0, 0, 0, 255);
                              }

                              boolean var8 = true;
                              boolean var9 = false;
                              WeatherFxMask[] var14 = playerMasks[var0].masks;
                              int var15 = playerMasks[var0].maskPointer;

                              for(int var16 = 0; var16 < var15; ++var16) {
                                 WeatherFxMask var17 = var14[var16];
                                 if (var17.enabled) {
                                    boolean var11;
                                    boolean var12;
                                    if ((var17.flags & 4) == 4) {
                                       SpriteRenderer.GL_BLENDFUNC_ENABLED = true;
                                       SpriteRenderer.instance.glBlendFunc(SCR_MASK_SUB, DST_MASK_SUB);
                                       SpriteRenderer.instance.glBlendEquation(32779);
                                       IndieGL.enableAlphaTest();
                                       IndieGL.glAlphaFunc(516, 0.02F);
                                       SpriteRenderer.GL_BLENDFUNC_ENABLED = false;
                                       var11 = (var17.flags & 1) == 1;
                                       var12 = (var17.flags & 2) == 2;
                                       renderMaskWall(var17.gs, var17.x, var17.y, var17.z, var11, var12, var0);
                                       SpriteRenderer.GL_BLENDFUNC_ENABLED = true;
                                       SpriteRenderer.instance.glBlendEquation(32774);
                                       SpriteRenderer.GL_BLENDFUNC_ENABLED = false;
                                       boolean var13 = (var17.flags & 64) == 64;
                                       if (var13 && var17.gs != null) {
                                          SpriteRenderer.GL_BLENDFUNC_ENABLED = true;
                                          SpriteRenderer.instance.glBlendFunc(SCR_MASK_ADD, DST_MASK_ADD);
                                          SpriteRenderer.GL_BLENDFUNC_ENABLED = false;
                                          var17.gs.RenderOpenDoorOnly();
                                       }
                                    } else {
                                       SpriteRenderer.GL_BLENDFUNC_ENABLED = true;
                                       SpriteRenderer.instance.glBlendFunc(SCR_MASK_ADD, DST_MASK_ADD);
                                       SpriteRenderer.GL_BLENDFUNC_ENABLED = false;
                                       renderMaskFloor(var17.x, var17.y, var17.z);
                                       var9 = (var17.flags & 16) == 16;
                                       boolean var10 = (var17.flags & 8) == 8;
                                       if (!var9) {
                                          var11 = (var17.flags & 1) == 1;
                                          var12 = (var17.flags & 2) == 2;
                                          if (!var11 && !var12) {
                                             if ((var17.flags & 32) == 32) {
                                                renderMaskWall(var17.gs, var17.x, var17.y, var17.z, false, false, var0);
                                             }
                                          } else {
                                             renderMaskWall(var17.gs, var17.x, var17.y, var17.z, var11, var12, var0);
                                          }
                                       }

                                       if (var9 && var17.gs != null) {
                                          var17.gs.RenderMinusFloorFxMask(var17.z + 1, false, false);
                                       }

                                       if (var10 && var17.gs != null) {
                                          var17.gs.renderCharacters(var17.z + 1, false, false);
                                          SpriteRenderer.GL_BLENDFUNC_ENABLED = true;
                                          SpriteRenderer.instance.glBlendFunc(SCR_MASK_ADD, DST_MASK_ADD);
                                          SpriteRenderer.GL_BLENDFUNC_ENABLED = false;
                                       }
                                    }
                                 }
                              }

                              SpriteRenderer.instance.glBlendFunc(770, 771);
                              SpriteRenderer.instance.glBuffer(5, var0);
                              SpriteRenderer.instance.glDoEndFrameFx(var0);
                           }

                           if (DEBUG_MASK_AND_PARTICLES) {
                              SpriteRenderer.instance.glClearColor(0, 0, 0, 255);
                              SpriteRenderer.instance.glClear(16640);
                              SpriteRenderer.instance.glClearColor(0, 0, 0, 255);
                           } else if (DEBUG_MASK) {
                              SpriteRenderer.instance.glClearColor(0, 255, 0, 255);
                              SpriteRenderer.instance.glClear(16640);
                              SpriteRenderer.instance.glClearColor(0, 0, 0, 255);
                           }

                           if (!RenderSettings.getInstance().getPlayerSettings(var0).isExterior()) {
                              drawFxLayered(var0, false, false, false);
                           }

                           if (IsoWeatherFX.instance.hasCloudsToRender()) {
                              drawFxLayered(var0, true, false, false);
                           }

                           if (IsoWeatherFX.instance.hasFogToRender() && PerformanceSettings.FogQuality == 2) {
                              drawFxLayered(var0, false, true, false);
                           }

                           if (Core.OptionRenderPrecipitation == 1 && IsoWeatherFX.instance.hasPrecipitationToRender()) {
                              drawFxLayered(var0, false, false, true);
                           }

                           SpriteRenderer.GL_BLENDFUNC_ENABLED = true;
                           SpriteRenderer.instance.glIgnoreStyles(false);
                        } else {
                           if (IsoWorld.instance.getCell() != null && IsoWorld.instance.getCell().getWeatherFX() != null) {
                              SpriteRenderer.instance.glIgnoreStyles(true);
                              SpriteRenderer.instance.glBlendFunc(770, 771);
                              IsoWorld.instance.getCell().getWeatherFX().render();
                              SpriteRenderer.instance.glIgnoreStyles(false);
                           }

                        }
                     }
                  }
               }
            }
         }
      }
   }

   private static void drawFxLayered(int var0, boolean var1x, boolean var2x, boolean var3x) {
      int var4 = IsoCamera.getOffscreenLeft(var0);
      int var5 = IsoCamera.getOffscreenTop(var0);
      int var6 = IsoCamera.getOffscreenWidth(var0);
      int var7 = IsoCamera.getOffscreenHeight(var0);
      int var8 = IsoCamera.getScreenLeft(var0);
      int var9 = IsoCamera.getScreenTop(var0);
      int var10 = IsoCamera.getScreenWidth(var0);
      int var11 = IsoCamera.getScreenHeight(var0);
      SpriteRenderer.instance.glBuffer(6, var0);
      SpriteRenderer.instance.glDoStartFrameFx(var6, var7, var0);
      if (!var1x && !var2x && !var3x) {
         Color var12 = RenderSettings.getInstance().getMaskClearColorForPlayer(var0);
         SpriteRenderer.GL_BLENDFUNC_ENABLED = true;
         SpriteRenderer.instance.glBlendFuncSeparate(SCR_PARTICLES, DST_PARTICLES, 1, 771);
         SpriteRenderer.GL_BLENDFUNC_ENABLED = false;
         SpriteRenderer.instance.renderi(texWhite, 0, 0, var6, var7, var12.r, var12.g, var12.b, var12.a, (Consumer)null);
         SpriteRenderer.GL_BLENDFUNC_ENABLED = true;
      } else if (IsoWorld.instance.getCell() != null && IsoWorld.instance.getCell().getWeatherFX() != null) {
         SpriteRenderer.GL_BLENDFUNC_ENABLED = true;
         SpriteRenderer.instance.glBlendFuncSeparate(SCR_PARTICLES, DST_PARTICLES, 1, 771);
         SpriteRenderer.GL_BLENDFUNC_ENABLED = false;
         IsoWorld.instance.getCell().getWeatherFX().renderLayered(var1x, var2x, var3x);
         SpriteRenderer.GL_BLENDFUNC_ENABLED = true;
      }

      if (MASKING_ENABLED) {
         SpriteRenderer.instance.glBlendFunc(SCR_MERGE, DST_MERGE);
         SpriteRenderer.instance.glBlendEquation(32779);
         ((Texture)fboMask.getTexture()).rendershader2(0.0F, 0.0F, (float)var6, (float)var7, var8, var9, var10, var11, 1.0F, 1.0F, 1.0F, 1.0F);
         SpriteRenderer.instance.glBlendEquation(32774);
      }

      SpriteRenderer.instance.glBlendFunc(770, 771);
      SpriteRenderer.instance.glBuffer(7, var0);
      SpriteRenderer.instance.glDoEndFrameFx(var0);
      Texture var25;
      if ((DEBUG_MASK || DEBUG_MASK_AND_PARTICLES) && !DEBUG_MASK_AND_PARTICLES) {
         var25 = (Texture)fboMask.getTexture();
         SpriteRenderer.instance.glBlendFunc(770, 771);
      } else {
         var25 = (Texture)fboParticles.getTexture();
         SpriteRenderer.instance.glBlendFunc(SCR_FINAL, DST_FINAL);
      }

      float var13 = 1.0F;
      float var14 = 1.0F;
      float var15 = 1.0F;
      float var16 = 1.0F;
      float var21 = (float)var8 / (float)var25.getWidthHW();
      float var22 = (float)var9 / (float)var25.getHeightHW();
      float var23 = (float)(var8 + var10) / (float)var25.getWidthHW();
      float var24 = (float)(var9 + var11) / (float)var25.getHeightHW();
      SpriteRenderer.instance.render(var25, 0.0F, 0.0F, (float)var6, (float)var7, var13, var14, var15, var16, var21, var24, var23, var24, var23, var22, var21, var22);
   }

   private static void initGlIds() {
      for(int var0 = 0; var0 < test.length; ++var0) {
         if (test[var0] == SCR_MASK_ADD) {
            ID_SCR_MASK_ADD = var0;
         } else if (test[var0] == DST_MASK_ADD) {
            ID_DST_MASK_ADD = var0;
         } else if (test[var0] == SCR_MASK_SUB) {
            ID_SCR_MASK_SUB = var0;
         } else if (test[var0] == DST_MASK_SUB) {
            ID_DST_MASK_SUB = var0;
         } else if (test[var0] == SCR_PARTICLES) {
            ID_SCR_PARTICLES = var0;
         } else if (test[var0] == DST_PARTICLES) {
            ID_DST_PARTICLES = var0;
         } else if (test[var0] == SCR_MERGE) {
            ID_SCR_MERGE = var0;
         } else if (test[var0] == DST_MERGE) {
            ID_DST_MERGE = var0;
         } else if (test[var0] == SCR_FINAL) {
            ID_SCR_FINAL = var0;
         } else if (test[var0] == DST_FINAL) {
            ID_DST_FINAL = var0;
         }
      }

   }

   private static void updateDebugKeys() {
      if (keypause > 0) {
         --keypause;
      }

      if (keypause == 0) {
         boolean var0 = false;
         boolean var1x = false;
         boolean var2x = false;
         boolean var3x = false;
         boolean var4 = false;
         if (TARGET_BLEND == 0) {
            var1 = ID_SCR_MASK_ADD;
            var2 = ID_DST_MASK_ADD;
         } else if (TARGET_BLEND == 1) {
            var1 = ID_SCR_MASK_SUB;
            var2 = ID_DST_MASK_SUB;
         } else if (TARGET_BLEND == 2) {
            var1 = ID_SCR_MERGE;
            var2 = ID_DST_MERGE;
         } else if (TARGET_BLEND == 3) {
            var1 = ID_SCR_FINAL;
            var2 = ID_DST_FINAL;
         } else if (TARGET_BLEND == 4) {
            var1 = ID_SCR_PARTICLES;
            var2 = ID_DST_PARTICLES;
         }

         if (GameKeyboard.isKeyDown(79)) {
            --var1;
            if (var1 < 0) {
               var1 = test.length - 1;
            }

            var0 = true;
         } else if (GameKeyboard.isKeyDown(81)) {
            ++var1;
            if (var1 >= test.length) {
               var1 = 0;
            }

            var0 = true;
         } else if (GameKeyboard.isKeyDown(75)) {
            --var2;
            if (var2 < 0) {
               var2 = test.length - 1;
            }

            var0 = true;
         } else if (GameKeyboard.isKeyDown(77)) {
            ++var2;
            if (var2 >= test.length) {
               var2 = 0;
            }

            var0 = true;
         } else if (GameKeyboard.isKeyDown(71)) {
            --TARGET_BLEND;
            if (TARGET_BLEND < 0) {
               TARGET_BLEND = 4;
            }

            var0 = true;
            var1x = true;
         } else if (GameKeyboard.isKeyDown(73)) {
            ++TARGET_BLEND;
            if (TARGET_BLEND >= 5) {
               TARGET_BLEND = 0;
            }

            var0 = true;
            var1x = true;
         } else if (MASKING_ENABLED && GameKeyboard.isKeyDown(82)) {
            DEBUG_MASK = !DEBUG_MASK;
            var0 = true;
            var2x = true;
         } else if (MASKING_ENABLED && GameKeyboard.isKeyDown(80)) {
            DEBUG_MASK_AND_PARTICLES = !DEBUG_MASK_AND_PARTICLES;
            var0 = true;
            var3x = true;
         } else if (!GameKeyboard.isKeyDown(72) && GameKeyboard.isKeyDown(76)) {
            MASKING_ENABLED = !MASKING_ENABLED;
            var0 = true;
            var4 = true;
         }

         if (var0) {
            if (var1x) {
               if (TARGET_BLEND == 0) {
                  DebugLog.log("TargetBlend = MASK_ADD");
               } else if (TARGET_BLEND == 1) {
                  DebugLog.log("TargetBlend = MASK_SUB");
               } else if (TARGET_BLEND == 2) {
                  DebugLog.log("TargetBlend = MERGE");
               } else if (TARGET_BLEND == 3) {
                  DebugLog.log("TargetBlend = FINAL");
               } else if (TARGET_BLEND == 4) {
                  DebugLog.log("TargetBlend = PARTICLES");
               }
            } else if (var2x) {
               DebugLog.log("DEBUG_MASK = " + DEBUG_MASK);
            } else if (var3x) {
               DebugLog.log("DEBUG_MASK_AND_PARTICLES = " + DEBUG_MASK_AND_PARTICLES);
            } else if (var4) {
               DebugLog.log("MASKING_ENABLED = " + MASKING_ENABLED);
            } else {
               if (TARGET_BLEND == 0) {
                  ID_SCR_MASK_ADD = var1;
                  ID_DST_MASK_ADD = var2;
                  SCR_MASK_ADD = test[ID_SCR_MASK_ADD];
                  DST_MASK_ADD = test[ID_DST_MASK_ADD];
               } else if (TARGET_BLEND == 1) {
                  ID_SCR_MASK_SUB = var1;
                  ID_DST_MASK_SUB = var2;
                  SCR_MASK_SUB = test[ID_SCR_MASK_SUB];
                  DST_MASK_SUB = test[ID_DST_MASK_SUB];
               } else if (TARGET_BLEND == 2) {
                  ID_SCR_MERGE = var1;
                  ID_DST_MERGE = var2;
                  SCR_MERGE = test[ID_SCR_MERGE];
                  DST_MERGE = test[ID_DST_MERGE];
               } else if (TARGET_BLEND == 3) {
                  ID_SCR_FINAL = var1;
                  ID_DST_FINAL = var2;
                  SCR_FINAL = test[ID_SCR_FINAL];
                  DST_FINAL = test[ID_DST_FINAL];
               } else if (TARGET_BLEND == 4) {
                  ID_SCR_PARTICLES = var1;
                  ID_DST_PARTICLES = var2;
                  SCR_PARTICLES = test[ID_SCR_PARTICLES];
                  DST_PARTICLES = test[ID_DST_PARTICLES];
               }

               String var10000 = testNames[var1];
               DebugLog.log("Blendmode = " + var10000 + " -> " + testNames[var2]);
            }

            keypause = 30;
         }
      }

   }

   static {
      offsetX = (float)(32 * Core.TileScale);
      offsetY = (float)(96 * Core.TileScale);
      defColorInfo = new ColorInfo();
      DIAMOND_ROWS = 1000;
      playerMasks = new WeatherFxMask.PlayerFxMask[4];
      dmiter = new DiamondMatrixIterator(0);
      diamondMatrixPos = new Vector2i();
      tmpVec = new Vector3f();
      tmpTorch = new IsoGameCharacter.TorchInfo();
      tmpColInfo = new ColorInfo();
      test = new int[]{0, 1, 768, 769, 774, 775, 770, 771, 772, 773, 32769, 32770, 32771, 32772, 776, 35065, 35066, 34185, 35067};
      testNames = new String[]{"GL_ZERO", "GL_ONE", "GL_SRC_COLOR", "GL_ONE_MINUS_SRC_COLOR", "GL_DST_COLOR", "GL_ONE_MINUS_DST_COLOR", "GL_SRC_ALPHA", "GL_ONE_MINUS_SRC_ALPHA", "GL_DST_ALPHA", "GL_ONE_MINUS_DST_ALPHA", "GL_CONSTANT_COLOR", "GL_ONE_MINUS_CONSTANT_COLOR", "GL_CONSTANT_ALPHA", "GL_ONE_MINUS_CONSTANT_ALPHA", "GL_SRC_ALPHA_SATURATE", "GL_SRC1_COLOR (33)", "GL_ONE_MINUS_SRC1_COLOR (33)", "GL_SRC1_ALPHA (15)", "GL_ONE_MINUS_SRC1_ALPHA (33)"};
      var1 = 1;
      var2 = 1;
      var3 = 1.0F;
      SCR_MASK_ADD = 770;
      DST_MASK_ADD = 771;
      SCR_MASK_SUB = 0;
      DST_MASK_SUB = 0;
      SCR_PARTICLES = 1;
      DST_PARTICLES = 771;
      SCR_MERGE = 770;
      DST_MERGE = 771;
      SCR_FINAL = 770;
      DST_FINAL = 771;
      TARGET_BLEND = 0;
      DEBUG_MASK = false;
      MASKING_ENABLED = true;
      DEBUG_MASK_AND_PARTICLES = false;
      keypause = 0;
   }

   public static class PlayerFxMask {
      private WeatherFxMask[] masks;
      private int maskPointer = 0;
      private boolean maskEnabled = false;
      private IsoGridSquare plrSquare;
      private int DISABLED_MASKS = 0;
      private boolean requiresUpdate = false;
      private boolean hasMaskToDraw = true;
      private int playerIndex;
      private IsoPlayer player;
      private int playerZ;
      private IWorldRegion curIsoWorldRegion;
      private ArrayList curConnectedRegions = new ArrayList();
      private final ArrayList isoWorldRegionTemp = new ArrayList();
      private boolean DIAMOND_ITER_DONE = false;
      private boolean isFirstSquare = true;
      private IsoGridSquare firstSquare;

      private void init() {
         this.masks = new WeatherFxMask[30000];

         for(int var1 = 0; var1 < this.masks.length; ++var1) {
            if (this.masks[var1] == null) {
               this.masks[var1] = new WeatherFxMask();
            }
         }

         this.maskEnabled = true;
      }

      private void initMask() {
         if (!GameServer.bServer) {
            if (!this.maskEnabled) {
               this.init();
            }

            this.playerIndex = IsoCamera.frameState.playerIndex;
            this.player = IsoPlayer.players[this.playerIndex];
            this.playerZ = (int)this.player.getZ();
            this.DIAMOND_ITER_DONE = false;
            this.requiresUpdate = false;
            if (this.player != null) {
               if (this.isFirstSquare || this.plrSquare == null || this.plrSquare != this.player.getSquare()) {
                  this.plrSquare = this.player.getSquare();
                  this.maskPointer = 0;
                  this.DISABLED_MASKS = 0;
                  this.requiresUpdate = true;
                  if (this.firstSquare == null) {
                     this.firstSquare = this.plrSquare;
                  }

                  if (this.firstSquare != null && this.firstSquare != this.plrSquare) {
                     this.isFirstSquare = false;
                  }
               }

               this.curIsoWorldRegion = this.player.getMasterRegion();
               this.curConnectedRegions.clear();
               if (this.curIsoWorldRegion != null && this.player.getMasterRegion().isFogMask()) {
                  this.isoWorldRegionTemp.clear();
                  this.isoWorldRegionTemp.add(this.curIsoWorldRegion);

                  label79:
                  while(true) {
                     IWorldRegion var1;
                     do {
                        if (this.isoWorldRegionTemp.size() <= 0) {
                           break label79;
                        }

                        var1 = (IWorldRegion)this.isoWorldRegionTemp.remove(0);
                        this.curConnectedRegions.add(var1);
                     } while(var1.getNeighbors().size() == 0);

                     Iterator var2 = var1.getNeighbors().iterator();

                     while(var2.hasNext()) {
                        IsoWorldRegion var3 = (IsoWorldRegion)var2.next();
                        if (!this.isoWorldRegionTemp.contains(var3) && !this.curConnectedRegions.contains(var3) && var3.isFogMask()) {
                           this.isoWorldRegionTemp.add(var3);
                        }
                     }
                  }
               } else {
                  this.curIsoWorldRegion = null;
               }
            }

            if (IsoWeatherFX.instance == null) {
               this.hasMaskToDraw = false;
            } else {
               this.hasMaskToDraw = true;
               if (this.hasMaskToDraw) {
                  if ((this.player.getSquare() == null || this.player.getSquare().getBuilding() == null && this.player.getSquare().Is(IsoFlagType.exterior)) && (this.curIsoWorldRegion == null || !this.curIsoWorldRegion.isFogMask())) {
                     this.hasMaskToDraw = false;
                  } else {
                     this.hasMaskToDraw = true;
                  }
               }

            }
         }
      }

      private void addMask(int var1, int var2, int var3, IsoGridSquare var4, int var5) {
         this.addMask(var1, var2, var3, var4, var5, true);
      }

      private void addMask(int var1, int var2, int var3, IsoGridSquare var4, int var5, boolean var6) {
         if (this.hasMaskToDraw && this.requiresUpdate) {
            if (!this.maskEnabled) {
               this.init();
            }

            WeatherFxMask var7 = this.getMask(var1, var2, var3);
            WeatherFxMask var8;
            if (var7 == null) {
               var8 = this.getFreeMask();
               var8.x = var1;
               var8.y = var2;
               var8.z = var3;
               var8.flags = var5;
               var8.gs = var4;
               var8.enabled = var6;
               if (!var6 && this.DISABLED_MASKS < WeatherFxMask.DIAMOND_ROWS) {
                  ++this.DISABLED_MASKS;
               }
            } else {
               if (var7.flags != var5) {
                  var7.flags |= var5;
               }

               if (!var7.enabled && var6) {
                  var8 = this.getFreeMask();
                  var8.x = var1;
                  var8.y = var2;
                  var8.z = var3;
                  var8.flags = var7.flags;
                  var8.gs = var4;
                  var8.enabled = var6;
               } else {
                  var7.enabled = var7.enabled ? var7.enabled : var6;
                  if (var6 && var4 != null && var7.gs == null) {
                     var7.gs = var4;
                  }
               }
            }

         }
      }

      private WeatherFxMask getFreeMask() {
         if (this.maskPointer >= this.masks.length) {
            DebugLog.log("Weather Mask buffer out of bounds. Increasing cache.");
            WeatherFxMask[] var1 = this.masks;
            this.masks = new WeatherFxMask[this.masks.length + 10000];

            for(int var2 = 0; var2 < this.masks.length; ++var2) {
               if (var1[var2] != null) {
                  this.masks[var2] = var1[var2];
               } else {
                  this.masks[var2] = new WeatherFxMask();
               }
            }
         }

         return this.masks[this.maskPointer++];
      }

      private boolean masksContains(int var1, int var2, int var3) {
         return this.getMask(var1, var2, var3) != null;
      }

      private WeatherFxMask getMask(int var1, int var2, int var3) {
         if (this.maskPointer <= 0) {
            return null;
         } else {
            int var4 = this.maskPointer - 1 - (WeatherFxMask.DIAMOND_ROWS + this.DISABLED_MASKS);
            if (var4 < 0) {
               var4 = 0;
            }

            for(int var5 = this.maskPointer - 1; var5 >= var4; --var5) {
               if (this.masks[var5].isLoc(var1, var2, var3)) {
                  return this.masks[var5];
               }
            }

            return null;
         }
      }
   }
}

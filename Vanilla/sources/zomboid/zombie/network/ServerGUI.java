package zombie.network;

import java.util.ArrayList;
import java.util.function.Consumer;
import org.lwjgl.opengl.GL11;
import org.lwjglx.opengl.Display;
import org.lwjglx.opengl.DisplayMode;
import org.lwjglx.opengl.PixelFormat;
import zombie.GameWindow;
import zombie.IndieGL;
import zombie.WorldSoundManager;
import zombie.Lua.LuaEventManager;
import zombie.characters.IsoGameCharacter;
import zombie.characters.IsoPlayer;
import zombie.characters.IsoZombie;
import zombie.core.Core;
import zombie.core.PerformanceSettings;
import zombie.core.SpriteRenderer;
import zombie.core.VBO.GLVertexBufferObject;
import zombie.core.opengl.RenderThread;
import zombie.core.opengl.Shader;
import zombie.core.physics.WorldSimulation;
import zombie.core.properties.PropertyContainer;
import zombie.core.raknet.UdpConnection;
import zombie.core.textures.ColorInfo;
import zombie.core.textures.Texture;
import zombie.core.textures.TextureDraw;
import zombie.core.textures.TexturePackPage;
import zombie.debug.LineDrawer;
import zombie.gameStates.MainScreenState;
import zombie.input.GameKeyboard;
import zombie.input.Mouse;
import zombie.iso.IsoCamera;
import zombie.iso.IsoCell;
import zombie.iso.IsoChunk;
import zombie.iso.IsoDirections;
import zombie.iso.IsoGridSquare;
import zombie.iso.IsoMovingObject;
import zombie.iso.IsoObject;
import zombie.iso.IsoObjectPicker;
import zombie.iso.IsoUtils;
import zombie.iso.IsoWorld;
import zombie.iso.PlayerCamera;
import zombie.iso.SpriteDetails.IsoFlagType;
import zombie.iso.SpriteDetails.IsoObjectType;
import zombie.iso.areas.IsoRoom;
import zombie.iso.objects.IsoBarricade;
import zombie.iso.objects.IsoCurtain;
import zombie.iso.objects.IsoDeadBody;
import zombie.iso.objects.IsoTree;
import zombie.iso.objects.IsoWindow;
import zombie.iso.sprite.IsoSprite;
import zombie.ui.TextManager;
import zombie.vehicles.BaseVehicle;

public class ServerGUI {
   private static boolean created;
   private static int minX;
   private static int minY;
   private static int maxX;
   private static int maxY;
   private static int maxZ;
   private static final ArrayList GridStack = new ArrayList();
   private static final ArrayList MinusFloorCharacters = new ArrayList(1000);
   private static final ArrayList SolidFloor = new ArrayList(5000);
   private static final ArrayList VegetationCorpses = new ArrayList(5000);
   private static final ColorInfo defColorInfo = new ColorInfo();

   public static boolean isCreated() {
      return created;
   }

   public static void init() {
      created = true;

      try {
         Display.setFullscreen(false);
         Display.setResizable(false);
         Display.setVSyncEnabled(false);
         Display.setTitle("Project Zomboid Server");
         System.setProperty("org.lwjgl.opengl.Window.undecorated", "false");
         Core.width = 1366;
         Core.height = 768;
         Display.setDisplayMode(new DisplayMode(Core.width, Core.height));
         Display.create(new PixelFormat(32, 0, 24, 8, 0));
         Display.setIcon(MainScreenState.loadIcons());
         GLVertexBufferObject.init();
         Display.makeCurrent();
         SpriteRenderer.instance.create();
         TextManager.instance.Init();

         while(TextManager.instance.font.isEmpty()) {
            GameWindow.fileSystem.updateAsyncTransactions();

            try {
               Thread.sleep(10L);
            } catch (InterruptedException var1) {
            }
         }

         TexturePackPage.bIgnoreWorldItemTextures = true;
         byte var0 = 2;
         GameWindow.LoadTexturePack("UI", var0);
         GameWindow.LoadTexturePack("UI2", var0);
         GameWindow.LoadTexturePack("IconsMoveables", var0);
         GameWindow.LoadTexturePack("RadioIcons", var0);
         GameWindow.LoadTexturePack("ApComUI", var0);
         GameWindow.LoadTexturePack("WeatherFx", var0);
         TexturePackPage.bIgnoreWorldItemTextures = false;
         var0 = 0;
         GameWindow.LoadTexturePack("Tiles2x", var0);
         GameWindow.LoadTexturePack("JumboTrees2x", var0);
         GameWindow.LoadTexturePack("Overlays2x", var0);
         GameWindow.LoadTexturePack("Tiles2x.floor", 0);
         GameWindow.DoLoadingText("");
         GameWindow.setTexturePackLookup();
         IsoObjectPicker.Instance.Init();
         Display.makeCurrent();
         GL11.glClearColor(0.0F, 0.0F, 0.0F, 1.0F);
         Display.releaseContext();
         RenderThread.initServerGUI();
         RenderThread.startRendering();
         Core.getInstance().initFBOs();
      } catch (Exception var2) {
         var2.printStackTrace();
         created = false;
      }
   }

   public static void init2() {
      if (created) {
         BaseVehicle.LoadAllVehicleTextures();
      }
   }

   public static void shutdown() {
      if (created) {
         RenderThread.shutdown();
      }
   }

   public static void update() {
      if (created) {
         Mouse.update();
         GameKeyboard.update();
         Display.processMessages();
         if (RenderThread.isCloseRequested()) {
         }

         int var0 = Mouse.getWheelState();
         if (var0 != 0) {
            int var1 = var0 - 0 < 0 ? 1 : -1;
            Core.getInstance().doZoomScroll(0, var1);
         }

         byte var2 = 0;
         IsoPlayer var3 = getPlayerToFollow();
         if (var3 == null) {
            Core.getInstance().StartFrame();
            Core.getInstance().EndFrame();
            Core.getInstance().StartFrameUI();
            SpriteRenderer.instance.renderi((Texture)null, 0, 0, Core.getInstance().getScreenWidth(), Core.getInstance().getScreenHeight(), 0.0F, 0.0F, 0.0F, 1.0F, (Consumer)null);
            Core.getInstance().EndFrameUI();
         } else {
            IsoPlayer.setInstance(var3);
            IsoPlayer.players[var2] = var3;
            IsoCamera.CamCharacter = var3;
            Core.getInstance().StartFrame(var2, true);
            renderWorld();
            Core.getInstance().EndFrame(var2);
            Core.getInstance().RenderOffScreenBuffer();
            Core.getInstance().StartFrameUI();
            renderUI();
            Core.getInstance().EndFrameUI();
         }
      }
   }

   private static IsoPlayer getPlayerToFollow() {
      for(int var0 = 0; var0 < GameServer.udpEngine.connections.size(); ++var0) {
         UdpConnection var1 = (UdpConnection)GameServer.udpEngine.connections.get(var0);
         if (var1.isFullyConnected()) {
            for(int var2 = 0; var2 < 4; ++var2) {
               IsoPlayer var3 = var1.players[var2];
               if (var3 != null && var3.OnlineID != -1) {
                  return var3;
               }
            }
         }
      }

      return null;
   }

   private static void updateCamera(IsoPlayer var0) {
      byte var2 = 0;
      PlayerCamera var3 = IsoCamera.cameras[var2];
      float var4 = IsoUtils.XToScreen(var0.x + var3.DeferedX, var0.y + var3.DeferedY, var0.z, 0);
      float var5 = IsoUtils.YToScreen(var0.x + var3.DeferedX, var0.y + var3.DeferedY, var0.z, 0);
      var4 -= (float)(IsoCamera.getOffscreenWidth(var2) / 2);
      var5 -= (float)(IsoCamera.getOffscreenHeight(var2) / 2);
      var5 -= var0.getOffsetY() * 1.5F;
      var4 += (float)IsoCamera.PLAYER_OFFSET_X;
      var5 += (float)IsoCamera.PLAYER_OFFSET_Y;
      var3.OffX = var4;
      var3.OffY = var5;
      IsoCamera.FrameState var6 = IsoCamera.frameState;
      var6.Paused = false;
      var6.playerIndex = var2;
      var6.CamCharacter = var0;
      var6.CamCharacterX = IsoCamera.CamCharacter.getX();
      var6.CamCharacterY = IsoCamera.CamCharacter.getY();
      var6.CamCharacterZ = IsoCamera.CamCharacter.getZ();
      var6.CamCharacterSquare = IsoCamera.CamCharacter.getCurrentSquare();
      var6.CamCharacterRoom = var6.CamCharacterSquare == null ? null : var6.CamCharacterSquare.getRoom();
      var6.OffX = IsoCamera.getOffX();
      var6.OffY = IsoCamera.getOffY();
      var6.OffscreenWidth = IsoCamera.getOffscreenWidth(var2);
      var6.OffscreenHeight = IsoCamera.getOffscreenHeight(var2);
   }

   private static void renderWorld() {
      IsoPlayer var0 = getPlayerToFollow();
      if (var0 != null) {
         byte var1 = 0;
         IsoPlayer.setInstance(var0);
         IsoPlayer.players[0] = var0;
         IsoCamera.CamCharacter = var0;
         updateCamera(var0);
         SpriteRenderer.instance.doCoreIntParam(0, IsoCamera.CamCharacter.x);
         SpriteRenderer.instance.doCoreIntParam(1, IsoCamera.CamCharacter.y);
         SpriteRenderer.instance.doCoreIntParam(2, IsoCamera.CamCharacter.z);
         IsoWorld.instance.sceneCullZombies();
         IsoSprite.globalOffsetX = -1.0F;
         byte var2 = 0;
         byte var3 = 0;
         int var4 = var2 + IsoCamera.getOffscreenWidth(var1);
         int var5 = var3 + IsoCamera.getOffscreenHeight(var1);
         float var6 = IsoUtils.XToIso((float)var2, (float)var3, 0.0F);
         float var7 = IsoUtils.YToIso((float)var4, (float)var3, 0.0F);
         float var8 = IsoUtils.XToIso((float)var4, (float)var5, 6.0F);
         float var9 = IsoUtils.YToIso((float)var2, (float)var5, 6.0F);
         minY = (int)var7;
         maxY = (int)var9;
         minX = (int)var6;
         maxX = (int)var8;
         minX -= 2;
         minY -= 2;
         maxZ = (int)var0.getZ();
         IsoCell var10 = IsoWorld.instance.CurrentCell;
         var10.DrawStencilMask();
         IsoObjectPicker.Instance.StartRender();
         RenderTiles();

         int var11;
         for(var11 = 0; var11 < var10.getObjectList().size(); ++var11) {
            IsoMovingObject var12 = (IsoMovingObject)var10.getObjectList().get(var11);
            var12.renderlast();
         }

         for(var11 = 0; var11 < var10.getStaticUpdaterObjectList().size(); ++var11) {
            IsoObject var13 = (IsoObject)var10.getStaticUpdaterObjectList().get(var11);
            var13.renderlast();
         }

         if (WorldSimulation.instance.created) {
            TextureDraw.GenericDrawer var14 = WorldSimulation.getDrawer(var1);
            SpriteRenderer.instance.drawGeneric(var14);
         }

         WorldSoundManager.instance.render();
         LineDrawer.clear();
      }
   }

   private static void RenderTiles() {
      IsoCell var0 = IsoWorld.instance.CurrentCell;
      if (IsoCell.perPlayerRender[0] == null) {
         IsoCell.perPlayerRender[0] = new IsoCell.PerPlayerRender();
      }

      IsoCell.PerPlayerRender var1 = IsoCell.perPlayerRender[0];
      if (var1 == null) {
         IsoCell.perPlayerRender[0] = new IsoCell.PerPlayerRender();
      }

      var1.setSize(maxX - minX + 1, maxY - minY + 1);
      short[][][] var2 = var1.StencilValues;

      for(int var3 = 0; var3 <= maxZ; ++var3) {
         GridStack.clear();

         int var4;
         int var7;
         label104:
         for(var4 = minY; var4 < maxY; ++var4) {
            int var5 = minX;
            IsoGridSquare var6 = ServerMap.instance.getGridSquare(var5, var4, var3);
            var7 = IsoDirections.E.index();

            while(true) {
               while(true) {
                  if (var5 >= maxX) {
                     continue label104;
                  }

                  if (var3 == 0) {
                     var2[var5 - minX][var4 - minY][0] = 0;
                     var2[var5 - minX][var4 - minY][1] = 0;
                  }

                  if (var6 != null && var6.getY() != var4) {
                     var6 = null;
                  }

                  if (var6 == null) {
                     var6 = ServerMap.instance.getGridSquare(var5, var4, var3);
                     if (var6 == null) {
                        ++var5;
                        continue;
                     }
                  }

                  IsoChunk var8 = var6.getChunk();
                  if (var8 != null && var6.IsOnScreen()) {
                     GridStack.add(var6);
                  }

                  var6 = var6.nav[var7];
                  ++var5;
               }
            }
         }

         SolidFloor.clear();
         VegetationCorpses.clear();
         MinusFloorCharacters.clear();

         IsoGridSquare var11;
         for(var4 = 0; var4 < GridStack.size(); ++var4) {
            var11 = (IsoGridSquare)GridStack.get(var4);
            var11.setLightInfoServerGUIOnly(defColorInfo);
            int var12 = renderFloor(var11);
            if (!var11.getStaticMovingObjects().isEmpty()) {
               var12 |= 2;
            }

            for(var7 = 0; var7 < var11.getMovingObjects().size(); ++var7) {
               IsoMovingObject var13 = (IsoMovingObject)var11.getMovingObjects().get(var7);
               boolean var9 = var13.isOnFloor();
               if (var9 && var13 instanceof IsoZombie) {
                  IsoZombie var10 = (IsoZombie)var13;
                  var9 = var10.bCrawling || var10.legsSprite.CurrentAnim != null && var10.legsSprite.CurrentAnim.name.equals("ZombieDeath") && var10.def.isFinished();
               }

               if (var9) {
                  var12 |= 2;
               } else {
                  var12 |= 4;
               }
            }

            if ((var12 & 1) != 0) {
               SolidFloor.add(var11);
            }

            if ((var12 & 2) != 0) {
               VegetationCorpses.add(var11);
            }

            if ((var12 & 4) != 0) {
               MinusFloorCharacters.add(var11);
            }
         }

         LuaEventManager.triggerEvent("OnPostFloorLayerDraw", var3);

         for(var4 = 0; var4 < VegetationCorpses.size(); ++var4) {
            var11 = (IsoGridSquare)VegetationCorpses.get(var4);
            renderMinusFloor(var11, false, true);
            renderCharacters(var11, true);
         }

         for(var4 = 0; var4 < MinusFloorCharacters.size(); ++var4) {
            var11 = (IsoGridSquare)MinusFloorCharacters.get(var4);
            boolean var14 = renderMinusFloor(var11, false, false);
            renderCharacters(var11, false);
            if (var14) {
               renderMinusFloor(var11, true, false);
            }
         }
      }

      MinusFloorCharacters.clear();
      SolidFloor.clear();
      VegetationCorpses.clear();
   }

   private static int renderFloor(IsoGridSquare var0) {
      int var1 = 0;
      byte var2 = 0;

      for(int var3 = 0; var3 < var0.getObjects().size(); ++var3) {
         IsoObject var4 = (IsoObject)var0.getObjects().get(var3);
         boolean var5 = true;
         if (var4.sprite != null && !var4.sprite.Properties.Is(IsoFlagType.solidfloor)) {
            var5 = false;
            var1 |= 4;
         }

         if (var5) {
            IndieGL.glAlphaFunc(516, 0.0F);
            var4.setAlphaAndTarget(var2, 1.0F);
            var4.render((float)var0.x, (float)var0.y, (float)var0.z, defColorInfo, true, false, (Shader)null);
            var4.renderObjectPicker((float)var0.x, (float)var0.y, (float)var0.z, defColorInfo);
            if ((var4.highlightFlags & 2) != 0) {
               var4.highlightFlags &= -2;
            }

            var1 |= 1;
         }

         if (!var5 && var4.sprite != null && (var4.sprite.Properties.Is(IsoFlagType.canBeRemoved) || var4.sprite.Properties.Is(IsoFlagType.attachedFloor))) {
            var1 |= 2;
         }
      }

      return var1;
   }

   private static boolean isSpriteOnSouthOrEastWall(IsoObject var0) {
      if (var0 instanceof IsoBarricade) {
         return var0.getDir() == IsoDirections.S || var0.getDir() == IsoDirections.E;
      } else if (var0 instanceof IsoCurtain) {
         IsoCurtain var2 = (IsoCurtain)var0;
         return var2.getType() == IsoObjectType.curtainS || var2.getType() == IsoObjectType.curtainE;
      } else {
         PropertyContainer var1 = var0.getProperties();
         return var1 != null && (var1.Is(IsoFlagType.attachedE) || var1.Is(IsoFlagType.attachedS));
      }
   }

   private static int DoWallLightingN(IsoGridSquare var0, IsoObject var1, int var2) {
      var1.render((float)var0.x, (float)var0.y, (float)var0.z, defColorInfo, true, false, (Shader)null);
      return var2;
   }

   private static int DoWallLightingW(IsoGridSquare var0, IsoObject var1, int var2) {
      var1.render((float)var0.x, (float)var0.y, (float)var0.z, defColorInfo, true, false, (Shader)null);
      return var2;
   }

   private static int DoWallLightingNW(IsoGridSquare var0, IsoObject var1, int var2) {
      var1.render((float)var0.x, (float)var0.y, (float)var0.z, defColorInfo, true, false, (Shader)null);
      return var2;
   }

   private static boolean renderMinusFloor(IsoGridSquare var0, boolean var1, boolean var2) {
      int var3 = var1 ? var0.getObjects().size() - 1 : 0;
      int var4 = var1 ? 0 : var0.getObjects().size() - 1;
      int var5 = IsoCamera.frameState.playerIndex;
      IsoGridSquare var6 = IsoCamera.frameState.CamCharacterSquare;
      IsoRoom var7 = IsoCamera.frameState.CamCharacterRoom;
      int var8 = (int)(IsoUtils.XToScreenInt(var0.x, var0.y, var0.z, 0) - IsoCamera.frameState.OffX);
      int var9 = (int)(IsoUtils.YToScreenInt(var0.x, var0.y, var0.z, 0) - IsoCamera.frameState.OffY);
      boolean var10 = true;
      IsoCell var11 = var0.getCell();
      if (var8 + 32 * Core.TileScale <= var11.StencilX1 || var8 - 32 * Core.TileScale >= var11.StencilX2 || var9 + 32 * Core.TileScale <= var11.StencilY1 || var9 - 96 * Core.TileScale >= var11.StencilY2) {
         var10 = false;
      }

      int var12 = 0;
      boolean var13 = false;
      int var14 = var3;

      while(true) {
         if (var1) {
            if (var14 < var4) {
               break;
            }
         } else if (var14 > var4) {
            break;
         }

         IsoObject var15 = (IsoObject)var0.getObjects().get(var14);
         boolean var16 = true;
         IsoGridSquare.CircleStencil = false;
         if (var15.sprite != null && var15.sprite.getProperties().Is(IsoFlagType.solidfloor)) {
            var16 = false;
         }

         if ((!var2 || var15.sprite == null || var15.sprite.Properties.Is(IsoFlagType.canBeRemoved) || var15.sprite.Properties.Is(IsoFlagType.attachedFloor)) && (var2 || var15.sprite == null || !var15.sprite.Properties.Is(IsoFlagType.canBeRemoved) && !var15.sprite.Properties.Is(IsoFlagType.attachedFloor))) {
            if (var15.sprite != null && (var15.sprite.getType() == IsoObjectType.WestRoofB || var15.sprite.getType() == IsoObjectType.WestRoofM || var15.sprite.getType() == IsoObjectType.WestRoofT) && var0.z == maxZ && var0.z == (int)IsoCamera.CamCharacter.getZ()) {
               var16 = false;
            }

            if (IsoCamera.CamCharacter.isClimbing() && var15.sprite != null && !var15.sprite.getProperties().Is(IsoFlagType.solidfloor)) {
               var16 = true;
            }

            if (isSpriteOnSouthOrEastWall(var15)) {
               if (!var1) {
                  var16 = false;
               }

               var13 = true;
            } else if (var1) {
               var16 = false;
            }

            if (var16) {
               IndieGL.glAlphaFunc(516, 0.0F);
               IsoGridSquare var21;
               if (var15.sprite != null && !var0.getProperties().Is(IsoFlagType.blueprint) && (var15.sprite.getType() == IsoObjectType.doorFrW || var15.sprite.getType() == IsoObjectType.doorFrN || var15.sprite.getType() == IsoObjectType.doorW || var15.sprite.getType() == IsoObjectType.doorN || var15.sprite.getProperties().Is(IsoFlagType.cutW) || var15.sprite.getProperties().Is(IsoFlagType.cutN)) && PerformanceSettings.LightingFrameSkip < 3) {
                  if (var15.getTargetAlpha(var5) < 1.0F) {
                     boolean var20 = false;
                     if (var20) {
                        if (var15.sprite.getProperties().Is(IsoFlagType.cutW) && var0.getProperties().Is(IsoFlagType.WallSE)) {
                           var21 = var0.nav[IsoDirections.NW.index()];
                           if (var21 == null || var21.getRoom() == null) {
                              var20 = false;
                           }
                        } else if (var15.sprite.getType() != IsoObjectType.doorFrW && var15.sprite.getType() != IsoObjectType.doorW && !var15.sprite.getProperties().Is(IsoFlagType.cutW)) {
                           if (var15.sprite.getType() == IsoObjectType.doorFrN || var15.sprite.getType() == IsoObjectType.doorN || var15.sprite.getProperties().Is(IsoFlagType.cutN)) {
                              var21 = var0.nav[IsoDirections.N.index()];
                              if (var21 == null || var21.getRoom() == null) {
                                 var20 = false;
                              }
                           }
                        } else {
                           var21 = var0.nav[IsoDirections.W.index()];
                           if (var21 == null || var21.getRoom() == null) {
                              var20 = false;
                           }
                        }
                     }

                     if (!var20) {
                        IsoGridSquare.CircleStencil = var10;
                     }

                     var15.setAlphaAndTarget(var5, 1.0F);
                  }

                  if (var15.sprite.getProperties().Is(IsoFlagType.cutW) && var15.sprite.getProperties().Is(IsoFlagType.cutN)) {
                     var12 = DoWallLightingNW(var0, var15, var12);
                  } else if (var15.sprite.getType() != IsoObjectType.doorFrW && var15.sprite.getType() != IsoObjectType.doorW && !var15.sprite.getProperties().Is(IsoFlagType.cutW)) {
                     if (var15.sprite.getType() == IsoObjectType.doorFrN || var15.sprite.getType() == IsoObjectType.doorN || var15.sprite.getProperties().Is(IsoFlagType.cutN)) {
                        var12 = DoWallLightingN(var0, var15, var12);
                     }
                  } else {
                     var12 = DoWallLightingW(var0, var15, var12);
                  }
               } else {
                  if (var6 != null) {
                  }

                  var15.setTargetAlpha(var5, 1.0F);
                  if (IsoCamera.CamCharacter != null && var15.getProperties() != null && (var15.getProperties().Is(IsoFlagType.solid) || var15.getProperties().Is(IsoFlagType.solidtrans))) {
                     int var17 = var0.getX() - (int)IsoCamera.CamCharacter.getX();
                     int var18 = var0.getY() - (int)IsoCamera.CamCharacter.getY();
                     if (var17 > 0 && var17 < 3 && var18 >= 0 && var18 < 3 || var18 > 0 && var18 < 3 && var17 >= 0 && var17 < 3) {
                        var15.setTargetAlpha(var5, 0.99F);
                     }
                  }

                  if (var15 instanceof IsoWindow && var15.getTargetAlpha(var5) < 1.0E-4F) {
                     IsoWindow var19 = (IsoWindow)var15;
                     var21 = var19.getOppositeSquare();
                     if (var21 != null && var21 != var0 && var21.lighting[var5].bSeen()) {
                        var15.setTargetAlpha(var5, var21.lighting[var5].darkMulti() * 2.0F);
                     }
                  }

                  if (var15 instanceof IsoTree) {
                     if (var10 && var0.x >= (int)IsoCamera.frameState.CamCharacterX && var0.y >= (int)IsoCamera.frameState.CamCharacterY && var6 != null && var6.Is(IsoFlagType.exterior)) {
                        ((IsoTree)var15).bRenderFlag = true;
                     } else {
                        ((IsoTree)var15).bRenderFlag = false;
                     }
                  }

                  var15.render((float)var0.x, (float)var0.y, (float)var0.z, defColorInfo, true, false, (Shader)null);
               }

               if (var15.sprite != null) {
                  var15.renderObjectPicker((float)var0.x, (float)var0.y, (float)var0.z, defColorInfo);
               }

               if ((var15.highlightFlags & 2) != 0) {
                  var15.highlightFlags &= -2;
               }
            }
         }

         var14 += var1 ? -1 : 1;
      }

      return var13;
   }

   private static void renderCharacters(IsoGridSquare var0, boolean var1) {
      int var2 = var0.getStaticMovingObjects().size();

      int var3;
      IsoMovingObject var4;
      for(var3 = 0; var3 < var2; ++var3) {
         var4 = (IsoMovingObject)var0.getStaticMovingObjects().get(var3);
         if (var4.sprite != null && (!var1 || var4 instanceof IsoDeadBody) && (var1 || !(var4 instanceof IsoDeadBody))) {
            var4.render(var4.getX(), var4.getY(), var4.getZ(), defColorInfo, true, false, (Shader)null);
            var4.renderObjectPicker(var4.getX(), var4.getY(), var4.getZ(), defColorInfo);
         }
      }

      var2 = var0.getMovingObjects().size();

      for(var3 = 0; var3 < var2; ++var3) {
         var4 = (IsoMovingObject)var0.getMovingObjects().get(var3);
         if (var4 != null && var4.sprite != null) {
            boolean var5 = var4.isOnFloor();
            if (var5 && var4 instanceof IsoZombie) {
               IsoZombie var6 = (IsoZombie)var4;
               var5 = var6.bCrawling || var6.legsSprite.CurrentAnim != null && var6.legsSprite.CurrentAnim.name.equals("ZombieDeath") && var6.def.isFinished();
            }

            if ((!var1 || var5) && (var1 || !var5)) {
               var4.setAlphaAndTarget(0, 1.0F);
               if (var4 instanceof IsoGameCharacter) {
                  IsoGameCharacter var7 = (IsoGameCharacter)var4;
                  var7.renderServerGUI();
               } else {
                  var4.render(var4.getX(), var4.getY(), var4.getZ(), defColorInfo, true, false, (Shader)null);
               }

               var4.renderObjectPicker(var4.getX(), var4.getY(), var4.getZ(), defColorInfo);
            }
         }
      }

   }

   private static void renderUI() {
   }
}

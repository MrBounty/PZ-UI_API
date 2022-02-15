package zombie.gameStates;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.Consumer;
import zombie.AmbientStreamManager;
import zombie.DebugFileWatcher;
import zombie.FliesSound;
import zombie.GameSounds;
import zombie.GameTime;
import zombie.GameWindow;
import zombie.IndieGL;
import zombie.LootRespawn;
import zombie.MapCollisionData;
import zombie.ReanimatedPlayers;
import zombie.SandboxOptions;
import zombie.SoundManager;
import zombie.SystemDisabler;
import zombie.VirtualZombieManager;
import zombie.WorldSoundManager;
import zombie.ZombieSpawnRecorder;
import zombie.ZomboidFileSystem;
import zombie.ZomboidGlobals;
import zombie.Lua.LuaEventManager;
import zombie.Lua.LuaHookManager;
import zombie.Lua.LuaManager;
import zombie.Lua.MapObjects;
import zombie.characters.IsoPlayer;
import zombie.characters.SurvivorFactory;
import zombie.characters.AttachedItems.AttachedLocations;
import zombie.characters.WornItems.BodyLocations;
import zombie.characters.professions.ProfessionFactory;
import zombie.characters.skills.CustomPerks;
import zombie.characters.skills.PerkFactory;
import zombie.characters.traits.TraitFactory;
import zombie.chat.ChatElement;
import zombie.core.BoxedStaticValues;
import zombie.core.Core;
import zombie.core.Languages;
import zombie.core.Rand;
import zombie.core.SpriteRenderer;
import zombie.core.Translator;
import zombie.core.logger.ExceptionLogger;
import zombie.core.opengl.RenderSettings;
import zombie.core.opengl.RenderThread;
import zombie.core.physics.WorldSimulation;
import zombie.core.profiling.PerformanceProfileProbe;
import zombie.core.skinnedmodel.DeadBodyAtlas;
import zombie.core.skinnedmodel.ModelManager;
import zombie.core.skinnedmodel.advancedanimation.AdvancedAnimator;
import zombie.core.skinnedmodel.advancedanimation.AnimationSet;
import zombie.core.skinnedmodel.model.ModelOutlines;
import zombie.core.skinnedmodel.population.BeardStyles;
import zombie.core.skinnedmodel.population.ClothingDecals;
import zombie.core.skinnedmodel.population.HairStyles;
import zombie.core.skinnedmodel.population.OutfitManager;
import zombie.core.stash.StashSystem;
import zombie.core.textures.Texture;
import zombie.core.znet.SteamFriends;
import zombie.core.znet.SteamUtils;
import zombie.debug.DebugLog;
import zombie.debug.DebugOptions;
import zombie.debug.LineDrawer;
import zombie.erosion.ErosionGlobals;
import zombie.globalObjects.CGlobalObjects;
import zombie.globalObjects.SGlobalObjects;
import zombie.input.GameKeyboard;
import zombie.input.JoypadManager;
import zombie.input.Mouse;
import zombie.inventory.ItemSoundManager;
import zombie.iso.BentFences;
import zombie.iso.BrokenFences;
import zombie.iso.BuildingDef;
import zombie.iso.ContainerOverlays;
import zombie.iso.IsoCamera;
import zombie.iso.IsoCell;
import zombie.iso.IsoChunk;
import zombie.iso.IsoChunkMap;
import zombie.iso.IsoGridSquare;
import zombie.iso.IsoMarkers;
import zombie.iso.IsoMetaCell;
import zombie.iso.IsoMetaGrid;
import zombie.iso.IsoUtils;
import zombie.iso.IsoWorld;
import zombie.iso.LightingThread;
import zombie.iso.LotHeader;
import zombie.iso.SearchMode;
import zombie.iso.TileOverlays;
import zombie.iso.WorldMarkers;
import zombie.iso.WorldStreamer;
import zombie.iso.SpriteDetails.IsoFlagType;
import zombie.iso.SpriteDetails.IsoObjectType;
import zombie.iso.areas.isoregion.IsoRegions;
import zombie.iso.objects.IsoFireManager;
import zombie.iso.objects.IsoGenerator;
import zombie.iso.objects.RainManager;
import zombie.iso.sprite.CorpseFlies;
import zombie.iso.sprite.IsoSprite;
import zombie.iso.sprite.SkyBox;
import zombie.iso.weather.ClimateManager;
import zombie.iso.weather.Temperature;
import zombie.iso.weather.fx.WeatherFxMask;
import zombie.meta.Meta;
import zombie.modding.ActiveMods;
import zombie.network.BodyDamageSync;
import zombie.network.ChunkChecksum;
import zombie.network.ClientServerMap;
import zombie.network.GameClient;
import zombie.network.GameServer;
import zombie.network.MPStatistics;
import zombie.network.PassengerMap;
import zombie.network.ServerGUI;
import zombie.network.ServerOptions;
import zombie.popman.ZombiePopulationManager;
import zombie.radio.ZomboidRadio;
import zombie.sandbox.CustomSandboxOptions;
import zombie.savefile.ClientPlayerDB;
import zombie.savefile.PlayerDB;
import zombie.scripting.ScriptManager;
import zombie.spnetwork.SinglePlayerClient;
import zombie.spnetwork.SinglePlayerServer;
import zombie.ui.ActionProgressBar;
import zombie.ui.FPSGraph;
import zombie.ui.ServerPulseGraph;
import zombie.ui.TextDrawObject;
import zombie.ui.TextManager;
import zombie.ui.TutorialManager;
import zombie.ui.UIElement;
import zombie.ui.UIManager;
import zombie.util.StringUtils;
import zombie.vehicles.EditVehicleState;
import zombie.vehicles.PolygonalMap2;
import zombie.vehicles.VehicleCache;
import zombie.vehicles.VehicleIDMap;
import zombie.vehicles.VehicleType;
import zombie.vehicles.VehiclesDB2;
import zombie.worldMap.WorldMap;
import zombie.worldMap.WorldMapVisited;
import zombie.worldMap.editor.WorldMapEditorState;

public final class IngameState extends GameState {
   public static int WaitMul = 20;
   public static IngameState instance;
   public static float draww;
   public static float drawh;
   public static Long GameID = 0L;
   static int last = -1;
   static float xPos;
   static float yPos;
   static float offx;
   static float offy;
   static float zoom;
   static HashMap ContainerTypes = new HashMap();
   static int nSaveCycle = 1800;
   static boolean bDoChars = false;
   static boolean keySpacePreviousFrame = false;
   public long numberTicks = 0L;
   public boolean Paused = false;
   public float SaveDelay = 0.0F;
   boolean alt = false;
   int insanityScareCount = 5;
   boolean MDebounce = false;
   int insanitypic = -1;
   int timesincelastinsanity = 10000000;
   GameState RedirectState = null;
   boolean bDidServerDisconnectState = false;
   boolean fpsKeyDown = false;
   private final ArrayList debugTimes = new ArrayList();
   private int tickCount = 0;
   private float SadisticMusicDirectorTime;
   public boolean showAnimationViewer = false;
   public boolean showAttachmentEditor = false;
   public boolean showChunkDebugger = false;
   public boolean showGlobalObjectDebugger = false;
   public String showVehicleEditor = null;
   public String showWorldMapEditor = null;

   public IngameState() {
      instance = this;
   }

   public static void renderDebugOverhead(IsoCell var0, int var1, int var2, int var3, int var4) {
      Mouse.update();
      int var5 = Mouse.getX();
      int var6 = Mouse.getY();
      var5 -= var3;
      var6 -= var4;
      var5 /= var2;
      var6 /= var2;
      SpriteRenderer.instance.renderi((Texture)null, var3, var4, var2 * var0.getWidthInTiles(), var2 * var0.getHeightInTiles(), 0.7F, 0.7F, 0.7F, 1.0F, (Consumer)null);
      IsoGridSquare var7 = var0.getGridSquare(var5 + var0.ChunkMap[0].getWorldXMinTiles(), var6 + var0.ChunkMap[0].getWorldYMinTiles(), 0);
      int var11;
      int var12;
      if (var7 != null) {
         byte var8 = 48;
         byte var9 = 48;
         TextManager.instance.DrawString((double)var9, (double)var8, "SQUARE FLAGS", 1.0D, 1.0D, 1.0D, 1.0D);
         var11 = var8 + 20;
         var12 = var9 + 8;

         int var10;
         for(var10 = 0; var10 < IsoFlagType.MAX.index(); ++var10) {
            if (var7.Is(IsoFlagType.fromIndex(var10))) {
               TextManager.instance.DrawString((double)var12, (double)var11, IsoFlagType.fromIndex(var10).toString(), 0.6D, 0.6D, 0.8D, 1.0D);
               var11 += 18;
            }
         }

         var9 = 48;
         var11 += 16;
         TextManager.instance.DrawString((double)var9, (double)var11, "SQUARE OBJECT TYPES", 1.0D, 1.0D, 1.0D, 1.0D);
         var11 += 20;
         var12 = var9 + 8;

         for(var10 = 0; var10 < 64; ++var10) {
            if (var7.getHasTypes().isSet(var10)) {
               TextManager.instance.DrawString((double)var12, (double)var11, IsoObjectType.fromIndex(var10).toString(), 0.6D, 0.6D, 0.8D, 1.0D);
               var11 += 18;
            }
         }
      }

      for(var11 = 0; var11 < var0.getWidthInTiles(); ++var11) {
         for(var12 = 0; var12 < var0.getHeightInTiles(); ++var12) {
            IsoGridSquare var13 = var0.getGridSquare(var11 + var0.ChunkMap[0].getWorldXMinTiles(), var12 + var0.ChunkMap[0].getWorldYMinTiles(), var1);
            if (var13 != null) {
               if (!var13.getProperties().Is(IsoFlagType.solid) && !var13.getProperties().Is(IsoFlagType.solidtrans)) {
                  if (!var13.getProperties().Is(IsoFlagType.exterior)) {
                     SpriteRenderer.instance.renderi((Texture)null, var3 + var11 * var2, var4 + var12 * var2, var2, var2, 0.8F, 0.8F, 0.8F, 1.0F, (Consumer)null);
                  }
               } else {
                  SpriteRenderer.instance.renderi((Texture)null, var3 + var11 * var2, var4 + var12 * var2, var2, var2, 0.5F, 0.5F, 0.5F, 255.0F, (Consumer)null);
               }

               if (var13.Has(IsoObjectType.tree)) {
                  SpriteRenderer.instance.renderi((Texture)null, var3 + var11 * var2, var4 + var12 * var2, var2, var2, 0.4F, 0.8F, 0.4F, 1.0F, (Consumer)null);
               }

               if (var13.getProperties().Is(IsoFlagType.collideN)) {
                  SpriteRenderer.instance.renderi((Texture)null, var3 + var11 * var2, var4 + var12 * var2, var2, 1, 0.2F, 0.2F, 0.2F, 1.0F, (Consumer)null);
               }

               if (var13.getProperties().Is(IsoFlagType.collideW)) {
                  SpriteRenderer.instance.renderi((Texture)null, var3 + var11 * var2, var4 + var12 * var2, 1, var2, 0.2F, 0.2F, 0.2F, 1.0F, (Consumer)null);
               }
            }
         }
      }

   }

   public static float translatePointX(float var0, float var1, float var2, float var3) {
      var0 -= var1;
      var0 *= var2;
      var0 += var3;
      var0 += draww / 2.0F;
      return var0;
   }

   public static float invTranslatePointX(float var0, float var1, float var2, float var3) {
      var0 -= draww / 2.0F;
      var0 -= var3;
      var0 /= var2;
      var0 += var1;
      return var0;
   }

   public static float invTranslatePointY(float var0, float var1, float var2, float var3) {
      var0 -= drawh / 2.0F;
      var0 -= var3;
      var0 /= var2;
      var0 += var1;
      return var0;
   }

   public static float translatePointY(float var0, float var1, float var2, float var3) {
      var0 -= var1;
      var0 *= var2;
      var0 += var3;
      var0 += drawh / 2.0F;
      return var0;
   }

   public static void renderRect(float var0, float var1, float var2, float var3, float var4, float var5, float var6, float var7) {
      float var8 = translatePointX(var0, xPos, zoom, offx);
      float var9 = translatePointY(var1, yPos, zoom, offy);
      float var10 = translatePointX(var0 + var2, xPos, zoom, offx);
      float var11 = translatePointY(var1 + var3, yPos, zoom, offy);
      var2 = var10 - var8;
      var3 = var11 - var9;
      if (!(var8 >= (float)Core.getInstance().getScreenWidth()) && !(var10 < 0.0F) && !(var9 >= (float)Core.getInstance().getScreenHeight()) && !(var11 < 0.0F)) {
         SpriteRenderer.instance.render((Texture)null, var8, var9, var2, var3, var4, var5, var6, var7, (Consumer)null);
      }
   }

   public static void renderLine(float var0, float var1, float var2, float var3, float var4, float var5, float var6, float var7) {
      float var8 = translatePointX(var0, xPos, zoom, offx);
      float var9 = translatePointY(var1, yPos, zoom, offy);
      float var10 = translatePointX(var2, xPos, zoom, offx);
      float var11 = translatePointY(var3, yPos, zoom, offy);
      if ((!(var8 >= (float)Core.getInstance().getScreenWidth()) || !(var10 >= (float)Core.getInstance().getScreenWidth())) && (!(var9 >= (float)Core.getInstance().getScreenHeight()) || !(var11 >= (float)Core.getInstance().getScreenHeight())) && (!(var8 < 0.0F) || !(var10 < 0.0F)) && (!(var9 < 0.0F) || !(var11 < 0.0F))) {
         SpriteRenderer.instance.renderline((Texture)null, (int)var8, (int)var9, (int)var10, (int)var11, var4, var5, var6, var7);
      }
   }

   public static void renderDebugOverhead2(IsoCell var0, int var1, float var2, int var3, int var4, float var5, float var6, int var7, int var8) {
      draww = (float)var7;
      drawh = (float)var8;
      xPos = var5;
      yPos = var6;
      offx = (float)var3;
      offy = (float)var4;
      zoom = var2;
      float var9 = (float)var0.ChunkMap[0].getWorldXMinTiles();
      float var10 = (float)var0.ChunkMap[0].getWorldYMinTiles();
      float var11 = (float)var0.ChunkMap[0].getWorldXMaxTiles();
      float var12 = (float)var0.ChunkMap[0].getWorldYMaxTiles();
      renderRect(var9, var10, (float)var0.getWidthInTiles(), (float)var0.getWidthInTiles(), 0.7F, 0.7F, 0.7F, 1.0F);

      int var14;
      for(int var13 = 0; var13 < var0.getWidthInTiles(); ++var13) {
         for(var14 = 0; var14 < var0.getHeightInTiles(); ++var14) {
            IsoGridSquare var15 = var0.getGridSquare(var13 + var0.ChunkMap[0].getWorldXMinTiles(), var14 + var0.ChunkMap[0].getWorldYMinTiles(), var1);
            float var16 = (float)var13 + var9;
            float var17 = (float)var14 + var10;
            if (var15 != null) {
               if (!var15.getProperties().Is(IsoFlagType.solid) && !var15.getProperties().Is(IsoFlagType.solidtrans)) {
                  if (!var15.getProperties().Is(IsoFlagType.exterior)) {
                     renderRect(var16, var17, 1.0F, 1.0F, 0.8F, 0.8F, 0.8F, 1.0F);
                  }
               } else {
                  renderRect(var16, var17, 1.0F, 1.0F, 0.5F, 0.5F, 0.5F, 1.0F);
               }

               if (var15.Has(IsoObjectType.tree)) {
                  renderRect(var16, var17, 1.0F, 1.0F, 0.4F, 0.8F, 0.4F, 1.0F);
               }

               if (var15.getProperties().Is(IsoFlagType.collideN)) {
                  renderRect(var16, var17, 1.0F, 0.2F, 0.2F, 0.2F, 0.2F, 1.0F);
               }

               if (var15.getProperties().Is(IsoFlagType.collideW)) {
                  renderRect(var16, var17, 0.2F, 1.0F, 0.2F, 0.2F, 0.2F, 1.0F);
               }
            }
         }
      }

      IsoMetaGrid var20 = IsoWorld.instance.MetaGrid;
      renderRect((float)(var20.minX * 300), (float)(var20.minY * 300), (float)(var20.getWidth() * 300), (float)(var20.getHeight() * 300), 1.0F, 1.0F, 1.0F, 0.05F);
      if ((double)var2 > 0.1D) {
         for(var14 = var20.minY; var14 <= var20.maxY; ++var14) {
            renderLine((float)(var20.minX * 300), (float)(var14 * 300), (float)((var20.maxX + 1) * 300), (float)(var14 * 300), 1.0F, 1.0F, 1.0F, 0.15F);
         }

         for(var14 = var20.minX; var14 <= var20.maxX; ++var14) {
            renderLine((float)(var14 * 300), (float)(var20.minY * 300), (float)(var14 * 300), (float)((var20.maxY + 1) * 300), 1.0F, 1.0F, 1.0F, 0.15F);
         }
      }

      IsoMetaCell[][] var22 = IsoWorld.instance.MetaGrid.Grid;

      for(int var21 = 0; var21 < var22.length; ++var21) {
         for(int var23 = 0; var23 < var22[0].length; ++var23) {
            LotHeader var24 = var22[var21][var23].info;
            if (var24 == null) {
               renderRect((float)((var20.minX + var21) * 300 + 1), (float)((var20.minY + var23) * 300 + 1), 298.0F, 298.0F, 0.2F, 0.0F, 0.0F, 0.3F);
            } else {
               for(int var18 = 0; var18 < var24.Buildings.size(); ++var18) {
                  BuildingDef var19 = (BuildingDef)var24.Buildings.get(var18);
                  if (var19.bAlarmed) {
                     renderRect((float)var19.getX(), (float)var19.getY(), (float)var19.getW(), (float)var19.getH(), 0.8F, 0.8F, 0.5F, 0.3F);
                  } else {
                     renderRect((float)var19.getX(), (float)var19.getY(), (float)var19.getW(), (float)var19.getH(), 0.5F, 0.5F, 0.8F, 0.3F);
                  }
               }
            }
         }
      }

   }

   public static void copyWorld(String var0, String var1) {
      String var10000 = ZomboidFileSystem.instance.getGameModeCacheDir();
      String var2 = var10000 + File.separator + var0 + File.separator;
      var2 = var2.replace("/", File.separator);
      var2 = var2.replace("\\", File.separator);
      String var3 = var2.substring(0, var2.lastIndexOf(File.separator));
      var3 = var3.replace("\\", "/");
      File var4 = new File(var3);
      var10000 = ZomboidFileSystem.instance.getGameModeCacheDir();
      var2 = var10000 + File.separator + var1 + File.separator;
      var2 = var2.replace("/", File.separator);
      var2 = var2.replace("\\", File.separator);
      String var5 = var2.substring(0, var2.lastIndexOf(File.separator));
      var5 = var5.replace("\\", "/");
      File var6 = new File(var5);

      try {
         copyDirectory(var4, var6);
      } catch (IOException var8) {
         var8.printStackTrace();
      }

   }

   public static void copyDirectory(File var0, File var1) throws IOException {
      if (var0.isDirectory()) {
         if (!var1.exists()) {
            var1.mkdir();
         }

         String[] var2 = var0.list();
         boolean var3 = GameLoadingState.convertingFileMax == -1;
         if (var3) {
            GameLoadingState.convertingFileMax = var2.length;
         }

         for(int var4 = 0; var4 < var2.length; ++var4) {
            if (var3) {
               ++GameLoadingState.convertingFileCount;
            }

            copyDirectory(new File(var0, var2[var4]), new File(var1, var2[var4]));
         }
      } else {
         FileInputStream var5 = new FileInputStream(var0);
         FileOutputStream var6 = new FileOutputStream(var1);
         var6.getChannel().transferFrom(var5.getChannel(), 0L, var0.length());
         var5.close();
         var6.close();
      }

   }

   public static void createWorld(String var0) {
      var0 = var0.replace(" ", "_").trim();
      String var10000 = ZomboidFileSystem.instance.getGameModeCacheDir();
      String var1 = var10000 + File.separator + var0 + File.separator;
      var1 = var1.replace("/", File.separator);
      var1 = var1.replace("\\", File.separator);
      String var2 = var1.substring(0, var1.lastIndexOf(File.separator));
      var2 = var2.replace("\\", "/");
      File var3 = new File(var2);
      if (!var3.exists()) {
         var3.mkdirs();
      }

      Core.GameSaveWorld = var0;
   }

   public void debugFullyStreamedIn(int var1, int var2) {
      IsoGridSquare var3 = IsoWorld.instance.CurrentCell.getGridSquare(var1, var2, 0);
      if (var3 != null) {
         if (var3.getBuilding() != null) {
            BuildingDef var4 = var3.getBuilding().getDef();
            if (var4 != null) {
               boolean var5 = var4.isFullyStreamedIn();

               for(int var6 = 0; var6 < var4.overlappedChunks.size(); var6 += 2) {
                  short var7 = var4.overlappedChunks.get(var6);
                  short var8 = var4.overlappedChunks.get(var6 + 1);
                  if (var5) {
                     renderRect((float)(var7 * 10), (float)(var8 * 10), 10.0F, 10.0F, 0.0F, 1.0F, 0.0F, 0.5F);
                  } else {
                     renderRect((float)(var7 * 10), (float)(var8 * 10), 10.0F, 10.0F, 1.0F, 0.0F, 0.0F, 0.5F);
                  }
               }

            }
         }
      }
   }

   public void UpdateStuff() {
      GameClient.bIngame = true;
      this.SaveDelay += GameTime.instance.getMultiplier();
      if (this.SaveDelay / 60.0F > 30.0F) {
         this.SaveDelay = 0.0F;
      }

      GameTime.instance.LastLastTimeOfDay = GameTime.instance.getLastTimeOfDay();
      GameTime.instance.setLastTimeOfDay(GameTime.getInstance().getTimeOfDay());
      boolean var1 = false;
      if (!GameServer.bServer && IsoPlayer.getInstance() != null) {
         var1 = IsoPlayer.allPlayersAsleep();
      }

      GameTime.getInstance().update(var1 && UIManager.getFadeAlpha() == 1.0D);
      if (!this.Paused) {
         ScriptManager.instance.update();
      }

      if (!this.Paused) {
         long var2 = System.nanoTime();

         try {
            WorldSoundManager.instance.update();
         } catch (Exception var14) {
            ExceptionLogger.logException(var14);
         }

         try {
            IsoFireManager.Update();
         } catch (Exception var13) {
            ExceptionLogger.logException(var13);
         }

         try {
            RainManager.Update();
         } catch (Exception var12) {
            ExceptionLogger.logException(var12);
         }

         Meta.instance.update();

         try {
            VirtualZombieManager.instance.update();
            MapCollisionData.instance.updateMain();
            ZombiePopulationManager.instance.updateMain();
            PolygonalMap2.instance.updateMain();
         } catch (Exception var10) {
            ExceptionLogger.logException(var10);
         } catch (Error var11) {
            var11.printStackTrace();
         }

         try {
            LootRespawn.update();
         } catch (Exception var9) {
            ExceptionLogger.logException(var9);
         }

         if (GameServer.bServer) {
            try {
               AmbientStreamManager.instance.update();
            } catch (Exception var8) {
               ExceptionLogger.logException(var8);
            }
         }

         if (GameClient.bClient) {
            try {
               BodyDamageSync.instance.update();
            } catch (Exception var7) {
               ExceptionLogger.logException(var7);
            }
         }

         if (!GameServer.bServer) {
            try {
               ItemSoundManager.update();
               FliesSound.instance.update();
               CorpseFlies.update();
               LuaManager.call("SadisticMusicDirectorTick", (Object)null);
               WorldMapVisited.update();
            } catch (Exception var6) {
               ExceptionLogger.logException(var6);
            }
         }

         SearchMode.getInstance().update();
         RenderSettings.getInstance().update();
         long var4 = System.nanoTime();
      }

   }

   public void enter() {
      UIManager.useUIFBO = Core.getInstance().supportsFBO() && Core.OptionUIFBO;
      if (!Core.getInstance().getUseShaders()) {
         Core.getInstance().RenderShader = null;
      }

      GameSounds.fix3DListenerPosition(false);
      IsoPlayer.getInstance().updateUsername();
      IsoPlayer.getInstance().setSceneCulled(false);
      IsoPlayer.getInstance().getInventory().addItemsToProcessItems();
      GameID = (long)Rand.Next(10000000);
      GameID = GameID + (long)Rand.Next(10000000);
      GameID = GameID + (long)Rand.Next(10000000);
      GameID = GameID + (long)Rand.Next(10000000);
      GameID = GameID + (long)Rand.Next(10000000);
      GameID = GameID + (long)Rand.Next(10000000);
      GameID = GameID + (long)Rand.Next(10000000);
      GameID = GameID + (long)Rand.Next(10000000);
      GameID = GameID + (long)Rand.Next(10000000);
      GameID = GameID + (long)Rand.Next(10000000);
      GameID = GameID + (long)Rand.Next(10000000);
      GameID = GameID + (long)Rand.Next(10000000);
      GameID = GameID + (long)Rand.Next(10000000);
      GameID = GameID + (long)Rand.Next(10000000);
      GameID = GameID + (long)Rand.Next(10000000);
      GameID = GameID + (long)Rand.Next(10000000);
      GameID = GameID + (long)Rand.Next(10000000);
      GameID = GameID + (long)Rand.Next(10000000);
      GameID = GameID + (long)Rand.Next(10000000);
      GameID = GameID + (long)Rand.Next(10000000);
      GameID = GameID + (long)Rand.Next(10000000);
      GameID = GameID + (long)Rand.Next(10000000);
      ZombieSpawnRecorder.instance.init();
      if (!GameServer.bServer) {
         IsoWorld.instance.CurrentCell.ChunkMap[0].processAllLoadGridSquare();
         IsoWorld.instance.CurrentCell.ChunkMap[0].update();
         if (!GameClient.bClient) {
            LightingThread.instance.GameLoadingUpdate();
         }
      }

      try {
         MapCollisionData.instance.startGame();
      } catch (Throwable var3) {
         ExceptionLogger.logException(var3);
      }

      IsoWorld.instance.CurrentCell.putInVehicle(IsoPlayer.getInstance());
      SoundManager.instance.setMusicState("Tutorial".equals(Core.GameMode) ? "Tutorial" : "InGame");
      ClimateManager.getInstance().update();
      LuaEventManager.triggerEvent("OnGameStart");
      LuaEventManager.triggerEvent("OnLoad");
      if (GameClient.bClient) {
         GameClient.instance.sendPlayerConnect(IsoPlayer.getInstance());
         DebugLog.log("Waiting for player-connect response from server");

         for(; IsoPlayer.getInstance().OnlineID == -1; GameClient.instance.update()) {
            try {
               Thread.sleep(10L);
            } catch (InterruptedException var2) {
               var2.printStackTrace();
            }
         }

         ClimateManager.getInstance().update();
         LightingThread.instance.GameLoadingUpdate();
      }

      if (GameClient.bClient && SteamUtils.isSteamModeEnabled()) {
         SteamFriends.UpdateRichPresenceConnectionInfo("In game", "+connect " + GameClient.ip + ":" + GameClient.port);
      }

   }

   public void exit() {
      DebugLog.log("EXITDEBUG: IngameState.exit 1");
      if (SteamUtils.isSteamModeEnabled()) {
         SteamFriends.UpdateRichPresenceConnectionInfo("", "");
      }

      UIManager.useUIFBO = false;
      if (ServerPulseGraph.instance != null) {
         ServerPulseGraph.instance.setVisible(false);
      }

      if (FPSGraph.instance != null) {
         FPSGraph.instance.setVisible(false);
      }

      UIManager.updateBeforeFadeOut();
      SoundManager.instance.setMusicState("MainMenu");
      long var1 = System.currentTimeMillis();
      boolean var3 = UIManager.useUIFBO;
      UIManager.useUIFBO = false;
      DebugLog.log("EXITDEBUG: IngameState.exit 2");

      while(true) {
         float var4 = Math.min(1.0F, (float)(System.currentTimeMillis() - var1) / 500.0F);
         boolean var5 = true;

         int var6;
         for(var6 = 0; var6 < IsoPlayer.numPlayers; ++var6) {
            if (IsoPlayer.players[var6] != null) {
               IsoPlayer.setInstance(IsoPlayer.players[var6]);
               IsoCamera.CamCharacter = IsoPlayer.players[var6];
               IsoSprite.globalOffsetX = -1.0F;
               Core.getInstance().StartFrame(var6, var5);
               IsoCamera.frameState.set(var6);
               IsoWorld.instance.render();
               Core.getInstance().EndFrame(var6);
               var5 = false;
            }
         }

         Core.getInstance().RenderOffScreenBuffer();
         Core.getInstance().StartFrameUI();
         UIManager.render();
         UIManager.DrawTexture(UIManager.getBlack(), 0.0D, 0.0D, (double)Core.getInstance().getScreenWidth(), (double)Core.getInstance().getScreenHeight(), (double)var4);
         Core.getInstance().EndFrameUI();
         DebugLog.log("EXITDEBUG: IngameState.exit 3 (alpha=" + var4 + ")");
         if (var4 >= 1.0F) {
            UIManager.useUIFBO = var3;
            DebugLog.log("EXITDEBUG: IngameState.exit 4");
            RenderThread.setWaitForRenderState(false);
            SpriteRenderer.instance.notifyRenderStateQueue();

            while(WorldStreamer.instance.isBusy()) {
               try {
                  Thread.sleep(1L);
               } catch (InterruptedException var10) {
                  var10.printStackTrace();
               }
            }

            DebugLog.log("EXITDEBUG: IngameState.exit 5");
            WorldStreamer.instance.stop();
            LightingThread.instance.stop();
            MapCollisionData.instance.stop();
            ZombiePopulationManager.instance.stop();
            PolygonalMap2.instance.stop();
            DebugLog.log("EXITDEBUG: IngameState.exit 6");

            int var12;
            for(var12 = 0; var12 < IsoWorld.instance.CurrentCell.ChunkMap.length; ++var12) {
               IsoChunkMap var13 = IsoWorld.instance.CurrentCell.ChunkMap[var12];

               for(var6 = 0; var6 < IsoChunkMap.ChunkGridWidth * IsoChunkMap.ChunkGridWidth; ++var6) {
                  IsoChunk var7 = var13.getChunk(var6 % IsoChunkMap.ChunkGridWidth, var6 / IsoChunkMap.ChunkGridWidth);
                  if (var7 != null && var7.refs.contains(var13)) {
                     var7.refs.remove(var13);
                     if (var7.refs.isEmpty()) {
                        var7.removeFromWorld();
                        var7.doReuseGridsquares();
                     }
                  }
               }
            }

            ModelManager.instance.Reset();

            for(var12 = 0; var12 < 4; ++var12) {
               IsoPlayer.players[var12] = null;
            }

            ZombieSpawnRecorder.instance.quit();
            DebugLog.log("EXITDEBUG: IngameState.exit 7");
            IsoPlayer.numPlayers = 1;
            Core.getInstance().OffscreenBuffer.destroy();
            WeatherFxMask.destroy();
            IsoRegions.reset();
            Temperature.reset();
            WorldMarkers.instance.reset();
            IsoMarkers.instance.reset();
            SearchMode.reset();
            ZomboidRadio.getInstance().Reset();
            ErosionGlobals.Reset();
            IsoGenerator.Reset();
            StashSystem.Reset();
            LootRespawn.Reset();
            VehicleCache.Reset();
            VehicleIDMap.instance.Reset();
            IsoWorld.instance.KillCell();
            ItemSoundManager.Reset();
            IsoChunk.Reset();
            ChunkChecksum.Reset();
            ClientServerMap.Reset();
            SinglePlayerClient.Reset();
            SinglePlayerServer.Reset();
            PassengerMap.Reset();
            DeadBodyAtlas.instance.Reset();
            CorpseFlies.Reset();
            if (PlayerDB.isAvailable()) {
               PlayerDB.getInstance().close();
            }

            VehiclesDB2.instance.Reset();
            WorldMap.Reset();
            WorldStreamer.instance = new WorldStreamer();
            WorldSimulation.instance.destroy();
            WorldSimulation.instance = new WorldSimulation();
            DebugLog.log("EXITDEBUG: IngameState.exit 8");
            VirtualZombieManager.instance.Reset();
            VirtualZombieManager.instance = new VirtualZombieManager();
            ReanimatedPlayers.instance = new ReanimatedPlayers();
            ScriptManager.instance.Reset();
            GameSounds.Reset();
            VehicleType.Reset();
            LuaEventManager.Reset();
            MapObjects.Reset();
            CGlobalObjects.Reset();
            SGlobalObjects.Reset();
            AmbientStreamManager.instance.stop();
            SoundManager.instance.stop();
            IsoPlayer.setInstance((IsoPlayer)null);
            IsoCamera.CamCharacter = null;
            TutorialManager.instance.StealControl = false;
            UIManager.init();
            ScriptManager.instance.Reset();
            ClothingDecals.Reset();
            BeardStyles.Reset();
            HairStyles.Reset();
            OutfitManager.Reset();
            AnimationSet.Reset();
            GameSounds.Reset();
            SurvivorFactory.Reset();
            ProfessionFactory.Reset();
            TraitFactory.Reset();
            ChooseGameInfo.Reset();
            AttachedLocations.Reset();
            BodyLocations.Reset();
            ContainerOverlays.instance.Reset();
            BentFences.getInstance().Reset();
            BrokenFences.getInstance().Reset();
            TileOverlays.instance.Reset();
            LuaHookManager.Reset();
            CustomPerks.Reset();
            PerkFactory.Reset();
            CustomSandboxOptions.Reset();
            SandboxOptions.Reset();
            LuaManager.init();
            JoypadManager.instance.Reset();
            GameKeyboard.doLuaKeyPressed = true;
            GameWindow.ActivatedJoyPad = null;
            GameWindow.OkToSaveOnExit = false;
            GameWindow.bLoadedAsClient = false;
            Core.bLastStand = false;
            Core.ChallengeID = null;
            Core.bTutorial = false;
            Core.getInstance().setChallenge(false);
            Core.getInstance().setForceSnow(false);
            Core.getInstance().setZombieGroupSound(true);
            Core.getInstance().setFlashIsoCursor(false);
            SystemDisabler.Reset();
            Texture.nullTextures.clear();
            DebugLog.log("EXITDEBUG: IngameState.exit 9");
            ZomboidFileSystem.instance.Reset();

            try {
               ZomboidFileSystem.instance.init();
            } catch (IOException var9) {
               ExceptionLogger.logException(var9);
            }

            Core.OptionModsEnabled = true;
            DebugLog.log("EXITDEBUG: IngameState.exit 10");
            ZomboidFileSystem.instance.loadMods("default");
            ZomboidFileSystem.instance.loadModPackFiles();
            ModelManager.instance.loadModAnimations();
            Languages.instance.init();
            Translator.loadFiles();
            DebugLog.log("EXITDEBUG: IngameState.exit 11");
            CustomPerks.instance.init();
            CustomPerks.instance.initLua();
            CustomSandboxOptions.instance.init();
            CustomSandboxOptions.instance.initInstance(SandboxOptions.instance);
            ScriptManager.instance.Load();
            ClothingDecals.init();
            BeardStyles.init();
            HairStyles.init();
            OutfitManager.init();
            DebugLog.log("EXITDEBUG: IngameState.exit 12");

            try {
               TextManager.instance.Init();
               LuaManager.LoadDirBase();
            } catch (Exception var8) {
               ExceptionLogger.logException(var8);
            }

            ZomboidGlobals.Load();
            DebugLog.log("EXITDEBUG: IngameState.exit 13");
            LuaEventManager.triggerEvent("OnGameBoot");
            SoundManager.instance.resumeSoundAndMusic();
            IsoPlayer[] var14 = IsoPlayer.players;
            int var15 = var14.length;

            for(var6 = 0; var6 < var15; ++var6) {
               IsoPlayer var16 = var14[var6];
               if (var16 != null) {
                  var16.dirtyRecalcGridStack = true;
               }
            }

            RenderThread.setWaitForRenderState(true);
            DebugLog.log("EXITDEBUG: IngameState.exit 14");
            return;
         }

         try {
            Thread.sleep(33L);
         } catch (Exception var11) {
         }
      }
   }

   public void yield() {
      SoundManager.instance.setMusicState("PauseMenu");
   }

   public GameState redirectState() {
      if (this.RedirectState != null) {
         GameState var1 = this.RedirectState;
         this.RedirectState = null;
         return var1;
      } else {
         return new MainScreenState();
      }
   }

   public void reenter() {
      SoundManager.instance.setMusicState("InGame");
   }

   public void renderframetext(int var1) {
      IngameState.s_performance.renderFrameText.invokeAndMeasure(this, var1, IngameState::renderFrameTextInternal);
   }

   private void renderFrameTextInternal(int var1) {
      IndieGL.disableAlphaTest();
      IndieGL.glDisable(2929);
      ArrayList var2 = UIManager.getUI();

      for(int var3 = 0; var3 < var2.size(); ++var3) {
         UIElement var4 = (UIElement)var2.get(var3);
         if (!(var4 instanceof ActionProgressBar) && var4.isVisible() && var4.isFollowGameWorld() && (var4.getRenderThisPlayerOnly() == -1 || var4.getRenderThisPlayerOnly() == var1)) {
            var4.render();
         }
      }

      ActionProgressBar var6 = UIManager.getProgressBar((double)var1);
      if (var6 != null && var6.isVisible()) {
         var6.render();
      }

      WorldMarkers.instance.render();
      IsoMarkers.instance.render();
      TextDrawObject.RenderBatch(var1);
      ChatElement.RenderBatch(var1);

      try {
         Core.getInstance().EndFrameText(var1);
      } catch (Exception var5) {
      }

   }

   public void renderframe(int var1) {
      IngameState.s_performance.renderFrame.invokeAndMeasure(this, var1, IngameState::renderFrameInternal);
   }

   private void renderFrameInternal(int var1) {
      if (IsoPlayer.getInstance() == null) {
         IsoPlayer.setInstance(IsoPlayer.players[0]);
         IsoCamera.CamCharacter = IsoPlayer.getInstance();
      }

      RenderSettings.getInstance().applyRenderSettings(var1);
      ActionProgressBar var2 = UIManager.getProgressBar((double)var1);
      if (var2 != null) {
         if (var2.getValue() > 0.0F && var2.getValue() < 1.0F) {
            var2.setVisible(true);
            var2.delayHide = 2;
         } else if (var2.isVisible() && var2.delayHide > 0 && --var2.delayHide == 0) {
            var2.setVisible(false);
         }

         if (var2.isVisible()) {
            float var3 = IsoUtils.XToScreen(IsoPlayer.getInstance().getX(), IsoPlayer.getInstance().getY(), IsoPlayer.getInstance().getZ(), 0);
            float var4 = IsoUtils.YToScreen(IsoPlayer.getInstance().getX(), IsoPlayer.getInstance().getY(), IsoPlayer.getInstance().getZ(), 0);
            var3 = var3 - IsoCamera.getOffX() - IsoPlayer.getInstance().offsetX;
            var4 = var4 - IsoCamera.getOffY() - IsoPlayer.getInstance().offsetY;
            var4 -= (float)(128 / (2 / Core.TileScale));
            var3 /= Core.getInstance().getZoom(var1);
            var4 /= Core.getInstance().getZoom(var1);
            var3 -= var2.width / 2.0F;
            var4 -= var2.height;
            IsoPlayer var5 = IsoPlayer.players[var1];
            if (var5 != null && var5.getUserNameHeight() > 0) {
               var4 -= (float)(var5.getUserNameHeight() + 2);
            }

            var2.setX((double)var3);
            var2.setY((double)var4);
         }

         if (!UIManager.VisibleAllUI) {
            var2.setVisible(false);
         }
      }

      IndieGL.disableAlphaTest();
      IndieGL.glDisable(2929);
      if (IsoPlayer.getInstance() != null && !IsoPlayer.getInstance().isAsleep() || UIManager.getFadeAlpha((double)var1) < 1.0F) {
         ModelOutlines.instance.startFrameMain(var1);
         IsoWorld.instance.render();
         ModelOutlines.instance.endFrameMain(var1);
         RenderSettings.getInstance().legacyPostRender(var1);
         LuaEventManager.triggerEvent("OnPostRender");
      }

      LineDrawer.clear();
      if (Core.bDebug && GameKeyboard.isKeyPressed(Core.getInstance().getKey("ToggleAnimationText"))) {
         DebugOptions.instance.Animation.Debug.setValue(!DebugOptions.instance.Animation.Debug.getValue());
      }

      try {
         Core.getInstance().EndFrame(var1);
      } catch (Exception var6) {
      }

   }

   public void renderframeui() {
      IngameState.s_performance.renderFrameUI.invokeAndMeasure(this, IngameState::renderFrameUI);
   }

   private void renderFrameUI() {
      if (Core.getInstance().StartFrameUI()) {
         TextManager.instance.DrawTextFromGameWorld();
         SkyBox.getInstance().draw();
         UIManager.render();
         ZomboidRadio.getInstance().render();
         if (Core.bDebug && IsoPlayer.getInstance() != null && IsoPlayer.getInstance().isGhostMode()) {
            IsoWorld.instance.CurrentCell.ChunkMap[0].drawDebugChunkMap();
         }

         DeadBodyAtlas.instance.renderUI();
         if (GameClient.bClient && GameClient.accessLevel.equals("admin")) {
            if (ServerPulseGraph.instance == null) {
               ServerPulseGraph.instance = new ServerPulseGraph();
            }

            ServerPulseGraph.instance.update();
            ServerPulseGraph.instance.render();
         }

         if (Core.bDebug) {
            if (GameKeyboard.isKeyDown(Core.getInstance().getKey("Display FPS"))) {
               if (!this.fpsKeyDown) {
                  this.fpsKeyDown = true;
                  if (FPSGraph.instance == null) {
                     FPSGraph.instance = new FPSGraph();
                  }

                  FPSGraph.instance.setVisible(!FPSGraph.instance.isVisible());
               }
            } else {
               this.fpsKeyDown = false;
            }

            if (FPSGraph.instance != null) {
               FPSGraph.instance.render();
            }
         }

         if (!GameServer.bServer) {
            for(int var1 = 0; var1 < IsoPlayer.numPlayers; ++var1) {
               IsoPlayer var2 = IsoPlayer.players[var1];
               if (var2 != null && !var2.isDead() && var2.isAsleep()) {
                  float var3 = GameClient.bFastForward ? GameTime.getInstance().ServerTimeOfDay : GameTime.getInstance().getTimeOfDay();
                  float var4 = (var3 - (float)((int)var3)) * 60.0F;
                  String var5 = "media/ui/SleepClock" + (int)var4 / 10 + ".png";
                  Texture var6 = Texture.getSharedTexture(var5);
                  if (var6 == null) {
                     break;
                  }

                  int var7 = IsoCamera.getScreenLeft(var1);
                  int var8 = IsoCamera.getScreenTop(var1);
                  int var9 = IsoCamera.getScreenWidth(var1);
                  int var10 = IsoCamera.getScreenHeight(var1);
                  SpriteRenderer.instance.renderi(var6, var7 + var9 / 2 - var6.getWidth() / 2, var8 + var10 / 2 - var6.getHeight() / 2, var6.getWidth(), var6.getHeight(), 1.0F, 1.0F, 1.0F, 1.0F, (Consumer)null);
               }
            }
         }

         ActiveMods.renderUI();
         JoypadManager.instance.renderUI();
      }

      if (Core.bDebug && DebugOptions.instance.Animation.AnimRenderPicker.getValue() && IsoPlayer.players[0] != null) {
         IsoPlayer.players[0].advancedAnimator.render();
      }

      if (Core.bDebug) {
         ModelOutlines.instance.renderDebug();
      }

      Core.getInstance().EndFrameUI();
   }

   public void render() {
      IngameState.s_performance.render.invokeAndMeasure(this, IngameState::renderInternal);
   }

   private void renderInternal() {
      boolean var1 = true;

      int var2;
      for(var2 = 0; var2 < IsoPlayer.numPlayers; ++var2) {
         if (IsoPlayer.players[var2] == null) {
            if (var2 == 0) {
               SpriteRenderer.instance.prePopulating();
            }
         } else {
            IsoPlayer.setInstance(IsoPlayer.players[var2]);
            IsoCamera.CamCharacter = IsoPlayer.players[var2];
            Core.getInstance().StartFrame(var2, var1);
            IsoCamera.frameState.set(var2);
            var1 = false;
            IsoSprite.globalOffsetX = -1.0F;
            this.renderframe(var2);
         }
      }

      if (DebugOptions.instance.OffscreenBuffer.Render.getValue()) {
         Core.getInstance().RenderOffScreenBuffer();
      }

      for(var2 = 0; var2 < IsoPlayer.numPlayers; ++var2) {
         if (IsoPlayer.players[var2] != null) {
            IsoPlayer.setInstance(IsoPlayer.players[var2]);
            IsoCamera.CamCharacter = IsoPlayer.players[var2];
            IsoCamera.frameState.set(var2);
            Core.getInstance().StartFrameText(var2);
            this.renderframetext(var2);
         }
      }

      UIManager.resize();
      this.renderframeui();
   }

   public GameStateMachine.StateAction update() {
      GameStateMachine.StateAction var1;
      try {
         IngameState.s_performance.update.start();
         var1 = this.updateInternal();
      } finally {
         IngameState.s_performance.update.end();
      }

      return var1;
   }

   private GameStateMachine.StateAction updateInternal() {
      ++this.tickCount;
      int var1;
      if (this.tickCount < 60) {
         for(var1 = 0; var1 < IsoPlayer.numPlayers; ++var1) {
            if (IsoPlayer.players[var1] != null) {
               IsoPlayer.players[var1].dirtyRecalcGridStackTime = 20.0F;
            }
         }
      }

      LuaEventManager.triggerEvent("OnTickEvenPaused", BoxedStaticValues.toDouble((double)this.numberTicks));
      DebugFileWatcher.instance.update();
      AdvancedAnimator.checkModifiedFiles();
      if (Core.bDebug) {
         this.debugTimes.clear();
         this.debugTimes.add(System.nanoTime());
      }

      if (Core.bExiting) {
         DebugLog.log("EXITDEBUG: IngameState.updateInternal 1");
         Core.bExiting = false;
         if (GameClient.bClient) {
            for(var1 = 0; var1 < IsoPlayer.numPlayers; ++var1) {
               IsoPlayer var14 = IsoPlayer.players[var1];
               if (var14 != null) {
                  ClientPlayerDB.getInstance().clientSendNetworkPlayerInt(var14);
               }
            }

            try {
               Thread.sleep(500L);
            } catch (InterruptedException var6) {
            }

            WorldStreamer.instance.stop();
            GameClient.instance.doDisconnect("Quitting");
         }

         DebugLog.log("EXITDEBUG: IngameState.updateInternal 2");
         if (PlayerDB.isAllow()) {
            PlayerDB.getInstance().saveLocalPlayersForce();
            PlayerDB.getInstance().m_canSavePlayers = false;
         }

         if (ClientPlayerDB.isAllow()) {
            ClientPlayerDB.getInstance().canSavePlayers = false;
         }

         try {
            GameWindow.save(true);
         } catch (Throwable var5) {
            ExceptionLogger.logException(var5);
         }

         DebugLog.log("EXITDEBUG: IngameState.updateInternal 3");

         try {
            LuaEventManager.triggerEvent("OnPostSave");
         } catch (Exception var4) {
            ExceptionLogger.logException(var4);
         }

         if (ClientPlayerDB.isAllow()) {
            ClientPlayerDB.getInstance().close();
         }

         return GameStateMachine.StateAction.Continue;
      } else if (GameWindow.bServerDisconnected) {
         TutorialManager.instance.StealControl = true;
         if (!this.bDidServerDisconnectState) {
            this.bDidServerDisconnectState = true;
            this.RedirectState = new ServerDisconnectState();
            return GameStateMachine.StateAction.Yield;
         } else {
            GameClient.connection = null;
            GameClient.instance.bConnected = false;
            GameClient.bClient = false;
            GameWindow.bServerDisconnected = false;
            return GameStateMachine.StateAction.Continue;
         }
      } else {
         if (Core.bDebug) {
            label395: {
               if (this.showGlobalObjectDebugger || GameKeyboard.isKeyPressed(60) && GameKeyboard.isKeyDown(29)) {
                  this.showGlobalObjectDebugger = false;
                  DebugLog.General.debugln("Activating DebugGlobalObjectState.");
                  this.RedirectState = new DebugGlobalObjectState();
                  return GameStateMachine.StateAction.Yield;
               }

               if (this.showChunkDebugger || GameKeyboard.isKeyPressed(60)) {
                  this.showChunkDebugger = false;
                  DebugLog.General.debugln("Activating DebugChunkState.");
                  this.RedirectState = DebugChunkState.checkInstance();
                  return GameStateMachine.StateAction.Yield;
               }

               if (this.showAnimationViewer || GameKeyboard.isKeyPressed(65) && GameKeyboard.isKeyDown(29)) {
                  this.showAnimationViewer = false;
                  DebugLog.General.debugln("Activating AnimationViewerState.");
                  AnimationViewerState var16 = AnimationViewerState.checkInstance();
                  this.RedirectState = var16;
                  return GameStateMachine.StateAction.Yield;
               }

               if (this.showAttachmentEditor || GameKeyboard.isKeyPressed(65) && GameKeyboard.isKeyDown(42)) {
                  this.showAttachmentEditor = false;
                  DebugLog.General.debugln("Activating AttachmentEditorState.");
                  AttachmentEditorState var15 = AttachmentEditorState.checkInstance();
                  this.RedirectState = var15;
                  return GameStateMachine.StateAction.Yield;
               }

               if (this.showVehicleEditor == null && !GameKeyboard.isKeyPressed(65)) {
                  if (this.showWorldMapEditor == null && !GameKeyboard.isKeyPressed(66)) {
                     break label395;
                  }

                  WorldMapEditorState var12 = WorldMapEditorState.checkInstance();
                  this.showWorldMapEditor = null;
                  this.RedirectState = var12;
                  return GameStateMachine.StateAction.Yield;
               }

               DebugLog.General.debugln("Activating EditVehicleState.");
               EditVehicleState var11 = EditVehicleState.checkInstance();
               if (!StringUtils.isNullOrWhitespace(this.showVehicleEditor)) {
                  var11.setScript(this.showVehicleEditor);
               }

               this.showVehicleEditor = null;
               this.RedirectState = var11;
               return GameStateMachine.StateAction.Yield;
            }
         }

         if (Core.bDebug) {
            this.debugTimes.add(System.nanoTime());
         }

         ++this.timesincelastinsanity;
         if (!GameServer.bServer && GameKeyboard.isKeyDown(Core.getInstance().getKey("Toggle Music")) && !this.MDebounce) {
            this.MDebounce = true;
            SoundManager.instance.AllowMusic = !SoundManager.instance.AllowMusic;
            if (!SoundManager.instance.AllowMusic) {
               SoundManager.instance.StopMusic();
               TutorialManager.instance.PrefMusic = null;
            }
         } else if (!GameServer.bServer && !GameKeyboard.isKeyDown(Core.getInstance().getKey("Toggle Music"))) {
            this.MDebounce = false;
         }

         if (Core.bDebug) {
            this.debugTimes.add(System.nanoTime());
         }

         try {
            if (!GameServer.bServer && IsoPlayer.getInstance() != null && IsoPlayer.allPlayersDead()) {
               if (IsoPlayer.getInstance() != null) {
                  UIManager.getSpeedControls().SetCurrentGameSpeed(1);
               }

               IsoCamera.update();
            }

            this.alt = !this.alt;
            if (!GameServer.bServer) {
               WaitMul = 1;
               if (UIManager.getSpeedControls() != null) {
                  if (UIManager.getSpeedControls().getCurrentGameSpeed() == 2) {
                     WaitMul = 15;
                  }

                  if (UIManager.getSpeedControls().getCurrentGameSpeed() == 3) {
                     WaitMul = 30;
                  }
               }
            }

            if (Core.bDebug) {
               this.debugTimes.add(System.nanoTime());
            }

            if (GameServer.bServer) {
               if (GameServer.Players.isEmpty() && ServerOptions.instance.PauseEmpty.getValue()) {
                  this.Paused = true;
               } else {
                  this.Paused = false;
               }
            }

            if (!this.Paused || GameClient.bClient) {
               try {
                  if (IsoCamera.CamCharacter != null && IsoWorld.instance.bDoChunkMapUpdate) {
                     for(var1 = 0; var1 < IsoPlayer.numPlayers; ++var1) {
                        if (IsoPlayer.players[var1] != null && !IsoWorld.instance.CurrentCell.ChunkMap[var1].ignore) {
                           if (!GameServer.bServer) {
                              IsoCamera.CamCharacter = IsoPlayer.players[var1];
                              IsoPlayer.setInstance(IsoPlayer.players[var1]);
                           }

                           if (!GameServer.bServer) {
                              IsoWorld.instance.CurrentCell.ChunkMap[var1].ProcessChunkPos(IsoCamera.CamCharacter);
                           }
                        }
                     }
                  }

                  if (Core.bDebug) {
                     this.debugTimes.add(System.nanoTime());
                  }

                  IsoWorld.instance.update();
                  if (Core.bDebug) {
                     this.debugTimes.add(System.nanoTime());
                  }

                  ZomboidRadio.getInstance().update();
                  this.UpdateStuff();
                  LuaEventManager.triggerEvent("OnTick", (double)this.numberTicks);
                  this.numberTicks = Math.max(this.numberTicks + 1L, 0L);
               } catch (Exception var9) {
                  ExceptionLogger.logException(var9);
                  if (!GameServer.bServer) {
                     if (GameClient.bClient) {
                        for(int var2 = 0; var2 < IsoPlayer.numPlayers; ++var2) {
                           IsoPlayer var3 = IsoPlayer.players[var2];
                           if (var3 != null) {
                              ClientPlayerDB.getInstance().clientSendNetworkPlayerInt(var3);
                           }
                        }

                        WorldStreamer.instance.stop();
                     }

                     String var13 = Core.GameSaveWorld;
                     createWorld(Core.GameSaveWorld + "_crash");
                     copyWorld(var13, Core.GameSaveWorld);
                     if (GameClient.bClient) {
                        if (PlayerDB.isAllow()) {
                           PlayerDB.getInstance().saveLocalPlayersForce();
                           PlayerDB.getInstance().m_canSavePlayers = false;
                        }

                        if (ClientPlayerDB.isAllow()) {
                           ClientPlayerDB.getInstance().canSavePlayers = false;
                        }
                     }

                     try {
                        GameWindow.save(true);
                     } catch (Throwable var8) {
                        ExceptionLogger.logException(var8);
                     }

                     if (GameClient.bClient) {
                        try {
                           LuaEventManager.triggerEvent("OnPostSave");
                        } catch (Exception var7) {
                           ExceptionLogger.logException(var7);
                        }

                        if (ClientPlayerDB.isAllow()) {
                           ClientPlayerDB.getInstance().close();
                        }
                     }
                  }

                  if (GameClient.bClient) {
                     GameClient.instance.doDisconnect("Quitting");
                  }

                  return GameStateMachine.StateAction.Continue;
               }
            }
         } catch (Exception var10) {
            System.err.println("IngameState.update caught an exception.");
            ExceptionLogger.logException(var10);
         }

         if (Core.bDebug) {
            this.debugTimes.add(System.nanoTime());
         }

         if (!GameServer.bServer || ServerGUI.isCreated()) {
            ModelManager.instance.update();
         }

         if (Core.bDebug && FPSGraph.instance != null) {
            FPSGraph.instance.addUpdate(System.currentTimeMillis());
            FPSGraph.instance.update();
         }

         if (GameClient.bClient || GameServer.bServer) {
            MPStatistics.Update();
         }

         return GameStateMachine.StateAction.Remain;
      }
   }

   private static class s_performance {
      static final PerformanceProfileProbe render = new PerformanceProfileProbe("IngameState.render");
      static final PerformanceProfileProbe renderFrame = new PerformanceProfileProbe("IngameState.renderFrame");
      static final PerformanceProfileProbe renderFrameText = new PerformanceProfileProbe("IngameState.renderFrameText");
      static final PerformanceProfileProbe renderFrameUI = new PerformanceProfileProbe("IngameState.renderFrameUI");
      static final PerformanceProfileProbe update = new PerformanceProfileProbe("IngameState.update");
   }
}

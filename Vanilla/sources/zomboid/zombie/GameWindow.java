package zombie;

import fmod.fmod.FMODManager;
import fmod.fmod.FMODSoundBank;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.CoderResult;
import java.nio.charset.CodingErrorAction;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjglx.LWJGLException;
import org.lwjglx.input.Controller;
import org.lwjglx.opengl.Display;
import org.lwjglx.opengl.OpenGLException;
import zombie.Lua.LuaEventManager;
import zombie.Lua.LuaManager;
import zombie.asset.AssetManagers;
import zombie.audio.BaseSoundBank;
import zombie.audio.DummySoundBank;
import zombie.characters.IsoPlayer;
import zombie.characters.professions.ProfessionFactory;
import zombie.characters.skills.CustomPerks;
import zombie.characters.skills.PerkFactory;
import zombie.characters.traits.TraitFactory;
import zombie.core.Core;
import zombie.core.Languages;
import zombie.core.PerformanceSettings;
import zombie.core.Rand;
import zombie.core.SpriteRenderer;
import zombie.core.ThreadGroups;
import zombie.core.Translator;
import zombie.core.input.Input;
import zombie.core.logger.ExceptionLogger;
import zombie.core.logger.ZipLogs;
import zombie.core.math.PZMath;
import zombie.core.opengl.RenderThread;
import zombie.core.particle.MuzzleFlash;
import zombie.core.physics.Bullet;
import zombie.core.profiling.PerformanceProfileFrameProbe;
import zombie.core.profiling.PerformanceProfileProbe;
import zombie.core.raknet.RakNetPeerInterface;
import zombie.core.raknet.VoiceManager;
import zombie.core.skinnedmodel.ModelManager;
import zombie.core.skinnedmodel.population.BeardStyles;
import zombie.core.skinnedmodel.population.ClothingDecals;
import zombie.core.skinnedmodel.population.HairStyles;
import zombie.core.skinnedmodel.population.OutfitManager;
import zombie.core.textures.Texture;
import zombie.core.textures.TextureID;
import zombie.core.textures.TexturePackPage;
import zombie.core.znet.ServerBrowser;
import zombie.core.znet.SteamFriends;
import zombie.core.znet.SteamUtils;
import zombie.core.znet.SteamWorkshop;
import zombie.debug.DebugLog;
import zombie.debug.DebugOptions;
import zombie.debug.LineDrawer;
import zombie.fileSystem.FileSystem;
import zombie.fileSystem.FileSystemImpl;
import zombie.gameStates.GameLoadingState;
import zombie.gameStates.GameStateMachine;
import zombie.gameStates.IngameState;
import zombie.gameStates.MainScreenState;
import zombie.gameStates.TISLogoState;
import zombie.globalObjects.SGlobalObjects;
import zombie.input.GameKeyboard;
import zombie.input.JoypadManager;
import zombie.input.Mouse;
import zombie.inventory.types.MapItem;
import zombie.iso.IsoCamera;
import zombie.iso.IsoObjectPicker;
import zombie.iso.IsoWorld;
import zombie.iso.LightingJNI;
import zombie.iso.LightingThread;
import zombie.iso.SliceY;
import zombie.iso.WorldStreamer;
import zombie.network.CoopMaster;
import zombie.network.GameClient;
import zombie.network.GameServer;
import zombie.popman.ZombiePopulationManager;
import zombie.radio.ZomboidRadio;
import zombie.sandbox.CustomSandboxOptions;
import zombie.savefile.ClientPlayerDB;
import zombie.savefile.PlayerDB;
import zombie.savefile.SavefileThumbnail;
import zombie.scripting.ScriptManager;
import zombie.spnetwork.SinglePlayerClient;
import zombie.spnetwork.SinglePlayerServer;
import zombie.ui.TextManager;
import zombie.ui.UIDebugConsole;
import zombie.ui.UIManager;
import zombie.util.PZSQLUtils;
import zombie.util.PublicServerUtil;
import zombie.vehicles.Clipper;
import zombie.vehicles.PolygonalMap2;
import zombie.world.moddata.GlobalModData;
import zombie.worldMap.WorldMapJNI;
import zombie.worldMap.WorldMapVisited;

public final class GameWindow {
   private static final String GAME_TITLE = "Project Zomboid";
   private static final FPSTracking s_fpsTracking = new FPSTracking();
   private static final ThreadLocal stringUTF = ThreadLocal.withInitial(GameWindow.StringUTF::new);
   public static final Input GameInput = new Input();
   public static boolean DEBUG_SAVE = false;
   public static boolean OkToSaveOnExit = false;
   public static String lastP = null;
   public static GameStateMachine states = new GameStateMachine();
   public static boolean bServerDisconnected;
   public static boolean bLoadedAsClient = false;
   public static String kickReason;
   public static boolean DrawReloadingLua = false;
   public static JoypadManager.Joypad ActivatedJoyPad = null;
   public static String version = "RC3";
   public static volatile boolean closeRequested;
   public static float averageFPS = (float)PerformanceSettings.getLockFPS();
   private static boolean doRenderEvent = false;
   public static boolean bLuaDebuggerKeyDown = false;
   public static FileSystem fileSystem = new FileSystemImpl();
   public static AssetManagers assetManagers;
   public static boolean bGameThreadExited;
   public static Thread GameThread;
   public static final ArrayList texturePacks;
   public static final FileSystem.TexturePackTextures texturePackTextures;

   private static void initShared() throws Exception {
      String var10000 = ZomboidFileSystem.instance.getCacheDir();
      String var0 = var10000 + File.separator;
      File var1 = new File(var0);
      if (!var1.exists()) {
         var1.mkdirs();
      }

      TexturePackPage.bIgnoreWorldItemTextures = true;
      byte var2 = 2;
      LoadTexturePack("UI", var2);
      LoadTexturePack("UI2", var2);
      LoadTexturePack("IconsMoveables", var2);
      LoadTexturePack("RadioIcons", var2);
      LoadTexturePack("ApComUI", var2);
      LoadTexturePack("Mechanics", var2);
      LoadTexturePack("WeatherFx", var2);
      setTexturePackLookup();
      PerkFactory.init();
      CustomPerks.instance.init();
      DoLoadingText(Translator.getText("UI_Loading_Scripts"));
      ScriptManager.instance.Load();
      DoLoadingText(Translator.getText("UI_Loading_Clothing"));
      ClothingDecals.init();
      BeardStyles.init();
      HairStyles.init();
      OutfitManager.init();
      DoLoadingText("");
      TraitFactory.init();
      ProfessionFactory.init();
      Rand.init();
      TexturePackPage.bIgnoreWorldItemTextures = false;
      TextureID.bUseCompression = TextureID.bUseCompressionOption;
      MuzzleFlash.init();
      Mouse.initCustomCursor();
      if (!Core.bDebug) {
         states.States.add(new TISLogoState());
      }

      states.States.add(new MainScreenState());
      if (!Core.bDebug) {
         states.LoopToState = 1;
      }

      GameInput.initControllers();
      int var3 = GameInput.getControllerCount();
      DebugLog.Input.println("----------------------------------------------");
      DebugLog.Input.println("--    Controller setup - use this info to     ");
      DebugLog.Input.println("--    edit joypad.ini in save directory       ");
      DebugLog.Input.println("----------------------------------------------");

      for(int var4 = 0; var4 < var3; ++var4) {
         Controller var5 = GameInput.getController(var4);
         if (var5 != null) {
            DebugLog.Input.println("----------------------------------------------");
            DebugLog.Input.println("--  Joypad: " + var5.getGamepadName());
            DebugLog.Input.println("----------------------------------------------");
            int var6 = var5.getAxisCount();
            int var7;
            String var8;
            if (var6 > 1) {
               DebugLog.Input.println("----------------------------------------------");
               DebugLog.Input.println("--    Axis definitions for controller " + var4);
               DebugLog.Input.println("----------------------------------------------");

               for(var7 = 0; var7 < var6; ++var7) {
                  var8 = var5.getAxisName(var7);
                  DebugLog.Input.println("Axis: " + var8);
               }
            }

            var6 = var5.getButtonCount();
            if (var6 > 1) {
               DebugLog.Input.println("----------------------------------------------");
               DebugLog.Input.println("--    Button definitions for controller " + var4);
               DebugLog.Input.println("----------------------------------------------");

               for(var7 = 0; var7 < var6; ++var7) {
                  var8 = var5.getButtonName(var7);
                  DebugLog.Input.println("Button: " + var8);
               }
            }
         }
      }

   }

   private static void logic() {
      if (GameClient.bClient) {
         try {
            GameClient.instance.update();
         } catch (Exception var3) {
            ExceptionLogger.logException(var3);
         }
      }

      try {
         SinglePlayerServer.update();
         SinglePlayerClient.update();
      } catch (Throwable var2) {
         ExceptionLogger.logException(var2);
      }

      SteamUtils.runLoop();
      Mouse.update();
      GameKeyboard.update();
      GameInput.updateGameThread();
      if (CoopMaster.instance != null) {
         CoopMaster.instance.update();
      }

      if (IsoPlayer.players[0] != null) {
         IsoPlayer.setInstance(IsoPlayer.players[0]);
         IsoCamera.CamCharacter = IsoPlayer.players[0];
      }

      UIManager.update();
      VoiceManager.instance.update();
      LineDrawer.clear();
      if (JoypadManager.instance.isAPressed(-1)) {
         for(int var0 = 0; var0 < JoypadManager.instance.JoypadList.size(); ++var0) {
            JoypadManager.Joypad var1 = (JoypadManager.Joypad)JoypadManager.instance.JoypadList.get(var0);
            if (var1.isAPressed()) {
               if (ActivatedJoyPad == null) {
                  ActivatedJoyPad = var1;
               }

               if (IsoPlayer.getInstance() != null) {
                  LuaEventManager.triggerEvent("OnJoypadActivate", var1.getID());
               } else {
                  LuaEventManager.triggerEvent("OnJoypadActivateUI", var1.getID());
               }
               break;
            }
         }
      }

      SoundManager.instance.Update();
      boolean var4 = true;
      if (GameTime.isGamePaused()) {
         var4 = false;
      }

      MapCollisionData.instance.updateGameState();
      Mouse.setCursorVisible(true);
      if (var4) {
         states.update();
      } else {
         IsoCamera.updateAll();
         if (IngameState.instance != null && (states.current == IngameState.instance || states.States.contains(IngameState.instance))) {
            LuaEventManager.triggerEvent("OnTickEvenPaused", 0.0D);
         }
      }

      UIManager.resize();
      fileSystem.updateAsyncTransactions();
      if (GameKeyboard.isKeyPressed(Core.getInstance().getKey("Take screenshot"))) {
         Core.getInstance().TakeFullScreenshot((String)null);
      }

   }

   public static void render() {
      ++IsoCamera.frameState.frameCount;
      renderInternal();
   }

   protected static void renderInternal() {
      if (!PerformanceSettings.LightingThread && LightingJNI.init && !LightingJNI.WaitingForMain()) {
         LightingJNI.DoLightingUpdateNew(System.nanoTime());
      }

      IsoObjectPicker.Instance.StartRender();
      GameWindow.s_performance.statesRender.invokeAndMeasure(states, GameStateMachine::render);
   }

   public static void InitDisplay() throws IOException, LWJGLException {
      Display.setTitle("Project Zomboid");
      int var0;
      if (!Core.getInstance().loadOptions()) {
         var0 = Runtime.getRuntime().availableProcessors();
         if (var0 == 1) {
            PerformanceSettings.LightingFrameSkip = 3;
         } else if (var0 == 2) {
            PerformanceSettings.LightingFrameSkip = 2;
         } else if (var0 <= 4) {
            PerformanceSettings.LightingFrameSkip = 1;
         }

         Display.setFullscreen(false);
         Display.setResizable(false);
         if (Display.getDesktopDisplayMode().getWidth() > 1280 && Display.getDesktopDisplayMode().getHeight() > 1080) {
            Core.getInstance().init(1280, 720);
            Core.getInstance().saveOptions();
         } else {
            Core.getInstance().init(Core.width, Core.height);
         }

         if (!GL.getCapabilities().GL_ATI_meminfo && !GL.getCapabilities().GL_NVX_gpu_memory_info) {
            DebugLog.General.warn("Unable to determine available GPU memory, texture compression defaults to on");
            TextureID.bUseCompressionOption = true;
            TextureID.bUseCompression = true;
         }

         DebugLog.log("Init language : " + System.getProperty("user.language"));
         Core.getInstance().setOptionLanguageName(System.getProperty("user.language").toUpperCase());
      } else {
         Core.getInstance().init(Core.getInstance().getScreenWidth(), Core.getInstance().getScreenHeight());
      }

      if (GL.getCapabilities().GL_ATI_meminfo) {
         var0 = GL11.glGetInteger(34812);
         DebugLog.log("ATI: available texture memory is " + var0 / 1024 + " MB");
      }

      if (GL.getCapabilities().GL_NVX_gpu_memory_info) {
         var0 = GL11.glGetInteger(36937);
         DebugLog.log("NVIDIA: current available GPU memory is " + var0 / 1024 + " MB");
         var0 = GL11.glGetInteger(36935);
         DebugLog.log("NVIDIA: dedicated available GPU memory is " + var0 / 1024 + " MB");
         var0 = GL11.glGetInteger(36936);
         DebugLog.log("NVIDIA: total available GPU memory is " + var0 / 1024 + " MB");
      }

      SpriteRenderer.instance.create();
   }

   public static void InitGameThread() {
      Thread.setDefaultUncaughtExceptionHandler(GameWindow::uncaughtGlobalException);
      Thread var0 = new Thread(ThreadGroups.Main, GameWindow::mainThread, "MainThread");
      var0.setUncaughtExceptionHandler(GameWindow::uncaughtExceptionMainThread);
      GameThread = var0;
      var0.start();
   }

   private static void uncaughtExceptionMainThread(Thread var0, Throwable var1) {
      if (var1 instanceof ThreadDeath) {
         DebugLog.General.println("Game Thread exited: ", var0.getName());
      } else {
         try {
            uncaughtException(var0, var1);
         } finally {
            onGameThreadExited();
         }

      }
   }

   private static void uncaughtGlobalException(Thread var0, Throwable var1) {
      if (var1 instanceof ThreadDeath) {
         DebugLog.General.println("External Thread exited: ", var0.getName());
      } else {
         uncaughtException(var0, var1);
      }
   }

   public static void uncaughtException(Thread var0, Throwable var1) {
      if (var1 instanceof ThreadDeath) {
         DebugLog.General.println("Internal Thread exited: ", var0.getName());
      } else {
         String var2 = String.format("Unhandled %s thrown by thread %s.", var1.getClass().getName(), var0.getName());
         DebugLog.General.error(var2);
         ExceptionLogger.logException(var1, var2);
      }
   }

   private static void mainThread() {
      mainThreadInit();
      enter();
      RenderThread.setWaitForRenderState(true);
      run_ez();
   }

   private static void mainThreadInit() {
      String var0 = System.getProperty("debug");
      String var1 = System.getProperty("nosave");
      if (var1 != null) {
         Core.getInstance().setNoSave(true);
      }

      if (var0 != null) {
         Core.bDebug = true;
      }

      if (!Core.SoundDisabled) {
         FMODManager.instance.init();
      }

      DebugOptions.instance.init();
      GameProfiler.init();
      SoundManager.instance = (BaseSoundManager)(Core.SoundDisabled ? new DummySoundManager() : new SoundManager());
      AmbientStreamManager.instance = (BaseAmbientStreamManager)(Core.SoundDisabled ? new DummyAmbientStreamManager() : new AmbientStreamManager());
      BaseSoundBank.instance = (BaseSoundBank)(Core.SoundDisabled ? new DummySoundBank() : new FMODSoundBank());
      VoiceManager.instance.loadConfig();
      TextureID.bUseCompressionOption = Core.SafeModeForced || Core.getInstance().getOptionTextureCompression();
      TextureID.bUseCompression = TextureID.bUseCompressionOption;
      SoundManager.instance.setSoundVolume((float)Core.getInstance().getOptionSoundVolume() / 10.0F);
      SoundManager.instance.setMusicVolume((float)Core.getInstance().getOptionMusicVolume() / 10.0F);
      SoundManager.instance.setAmbientVolume((float)Core.getInstance().getOptionAmbientVolume() / 10.0F);
      SoundManager.instance.setVehicleEngineVolume((float)Core.getInstance().getOptionVehicleEngineVolume() / 10.0F);

      try {
         ZomboidFileSystem.instance.init();
      } catch (Exception var7) {
         throw new RuntimeException(var7);
      }

      DebugFileWatcher.instance.init();
      String var2 = System.getProperty("server");
      String var3 = System.getProperty("client");
      String var4 = System.getProperty("nozombies");
      if (var4 != null) {
         IsoWorld.NoZombies = true;
      }

      if (var2 != null && var2.equals("true")) {
         GameServer.bServer = true;
      }

      try {
         renameSaveFolders();
         init();
      } catch (Exception var6) {
         throw new RuntimeException(var6);
      }
   }

   private static void renameSaveFolders() {
      String var0 = ZomboidFileSystem.instance.getSaveDir();
      File var1 = new File(var0);
      if (var1.exists() && var1.isDirectory()) {
         File var2 = new File(var1, "Fighter");
         File var3 = new File(var1, "Survivor");
         if (var2.exists() && var2.isDirectory() && var3.exists() && var3.isDirectory()) {
            DebugLog.log("RENAMING Saves/Survivor to Saves/Apocalypse");
            DebugLog.log("RENAMING Saves/Fighter to Saves/Survivor");
            var3.renameTo(new File(var1, "Apocalypse"));
            var2.renameTo(new File(var1, "Survivor"));
            String var10002 = ZomboidFileSystem.instance.getCacheDir();
            File var4 = new File(var10002 + File.separator + "latestSave.ini");
            if (var4.exists()) {
               var4.delete();
            }

         }
      }
   }

   public static long readLong(DataInputStream var0) throws IOException {
      int var1 = var0.read();
      int var2 = var0.read();
      int var3 = var0.read();
      int var4 = var0.read();
      int var5 = var0.read();
      int var6 = var0.read();
      int var7 = var0.read();
      int var8 = var0.read();
      if ((var1 | var2 | var3 | var4 | var5 | var6 | var7 | var8) < 0) {
         throw new EOFException();
      } else {
         return (long)(var1 + (var2 << 8) + (var3 << 16) + (var4 << 24) + (var5 << 32) + (var6 << 40) + (var7 << 48) + (var8 << 56));
      }
   }

   public static int readInt(DataInputStream var0) throws IOException {
      int var1 = var0.read();
      int var2 = var0.read();
      int var3 = var0.read();
      int var4 = var0.read();
      if ((var1 | var2 | var3 | var4) < 0) {
         throw new EOFException();
      } else {
         return var1 + (var2 << 8) + (var3 << 16) + (var4 << 24);
      }
   }

   private static void run_ez() {
      long var0 = System.nanoTime();
      long var2 = 0L;

      while(!RenderThread.isCloseRequested() && !closeRequested) {
         long var4 = System.nanoTime();
         if (var4 < var0) {
            var0 = var4;
         } else {
            long var6 = var4 - var0;
            var0 = var4;
            if (PerformanceSettings.isUncappedFPS()) {
               frameStep();
            } else {
               var2 += var6;
               long var8 = PZMath.secondsToNanos / (long)PerformanceSettings.getLockFPS();
               if (var2 >= var8) {
                  frameStep();
                  var2 %= var8;
               }
            }

            if (Core.bDebug && DebugOptions.instance.ThreadCrash_Enabled.getValue()) {
               DebugOptions.testThreadCrash(0);
               RenderThread.invokeOnRenderContext(() -> {
                  DebugOptions.testThreadCrash(1);
               });
            }

            Thread.yield();
         }
      }

      exit();
   }

   private static void enter() {
      Core.TileScale = Core.getInstance().getOptionTexture2x() ? 2 : 1;
      if (Core.SafeModeForced) {
         Core.TileScale = 1;
      }

      IsoCamera.init();
      int var0 = TextureID.bUseCompression ? 4 : 0;
      var0 |= 64;
      if (Core.TileScale == 1) {
         LoadTexturePack("Tiles1x", var0);
         LoadTexturePack("Overlays1x", var0);
         LoadTexturePack("JumboTrees1x", var0);
         LoadTexturePack("Tiles1x.floor", var0 & -5);
      }

      if (Core.TileScale == 2) {
         LoadTexturePack("Tiles2x", var0);
         LoadTexturePack("Overlays2x", var0);
         LoadTexturePack("JumboTrees2x", var0);
         LoadTexturePack("Tiles2x.floor", var0 & -5);
      }

      setTexturePackLookup();
      if (Texture.getSharedTexture("TileIndieStoneTentFrontLeft") == null) {
         throw new RuntimeException("Rebuild Tiles.pack with \"1 Include This in .pack\" as individual images not tilesheets");
      } else {
         DebugLog.log("LOADED UP A TOTAL OF " + Texture.totalTextureID + " TEXTURES");
         s_fpsTracking.init();
         DoLoadingText(Translator.getText("UI_Loading_ModelsAnimations"));
         ModelManager.instance.create();
         if (!SteamUtils.isSteamModeEnabled()) {
            DoLoadingText(Translator.getText("UI_Loading_InitPublicServers"));
            PublicServerUtil.init();
         }

         VoiceManager.instance.InitVMClient();
         DoLoadingText(Translator.getText("UI_Loading_OnGameBoot"));
         LuaEventManager.triggerEvent("OnGameBoot");
      }
   }

   private static void frameStep() {
      try {
         ++IsoCamera.frameState.frameCount;
         GameWindow.s_performance.frameStep.start();
         s_fpsTracking.frameStep();
         GameWindow.s_performance.logic.invokeAndMeasure(GameWindow::logic);
         Core.getInstance().setScreenSize(RenderThread.getDisplayWidth(), RenderThread.getDisplayHeight());
         renderInternal();
         if (doRenderEvent) {
            LuaEventManager.triggerEvent("OnRenderTick");
         }

         Core.getInstance().DoFrameReady();
         LightingThread.instance.update();
         if (Core.bDebug) {
            if (GameKeyboard.isKeyDown(Core.getInstance().getKey("Toggle Lua Debugger"))) {
               if (!bLuaDebuggerKeyDown) {
                  UIManager.setShowLuaDebuggerOnError(true);
                  LuaManager.thread.bStep = true;
                  LuaManager.thread.bStepInto = true;
                  bLuaDebuggerKeyDown = true;
               }
            } else {
               bLuaDebuggerKeyDown = false;
            }

            if (GameKeyboard.isKeyPressed(Core.getInstance().getKey("ToggleLuaConsole"))) {
               UIDebugConsole var0 = UIManager.getDebugConsole();
               if (var0 != null) {
                  var0.setVisible(!var0.isVisible());
               }
            }
         }
      } catch (OpenGLException var5) {
         RenderThread.logGLException(var5);
      } catch (Exception var6) {
         ExceptionLogger.logException(var6);
      } finally {
         GameWindow.s_performance.frameStep.end();
      }

   }

   private static void exit() {
      DebugLog.log("EXITDEBUG: GameWindow.exit 1");
      if (GameClient.bClient) {
         for(int var0 = 0; var0 < IsoPlayer.numPlayers; ++var0) {
            IsoPlayer var1 = IsoPlayer.players[var0];
            if (var1 != null) {
               ClientPlayerDB.getInstance().clientSendNetworkPlayerInt(var1);
            }
         }

         WorldStreamer.instance.stop();
         GameClient.instance.doDisconnect("Quitting");
         VoiceManager.instance.DeinitVMClient();
      }

      if (OkToSaveOnExit) {
         try {
            WorldStreamer.instance.quit();
         } catch (Exception var7) {
            var7.printStackTrace();
         }

         if (PlayerDB.isAllow()) {
            PlayerDB.getInstance().saveLocalPlayersForce();
            PlayerDB.getInstance().m_canSavePlayers = false;
         }

         if (ClientPlayerDB.isAllow()) {
            ClientPlayerDB.getInstance().canSavePlayers = false;
         }

         try {
            if (GameClient.bClient && GameClient.connection != null) {
               GameClient.connection.username = null;
            }

            save(true);
         } catch (Throwable var6) {
            var6.printStackTrace();
         }

         try {
            if (IsoWorld.instance.CurrentCell != null) {
               LuaEventManager.triggerEvent("OnPostSave");
            }
         } catch (Exception var5) {
            var5.printStackTrace();
         }

         try {
            if (IsoWorld.instance.CurrentCell != null) {
               LuaEventManager.triggerEvent("OnPostSave");
            }
         } catch (Exception var4) {
            var4.printStackTrace();
         }

         try {
            LightingThread.instance.stop();
            MapCollisionData.instance.stop();
            ZombiePopulationManager.instance.stop();
            PolygonalMap2.instance.stop();
            ZombieSpawnRecorder.instance.quit();
         } catch (Exception var3) {
            var3.printStackTrace();
         }
      }

      DebugLog.log("EXITDEBUG: GameWindow.exit 2");
      if (GameClient.bClient) {
         WorldStreamer.instance.stop();
         GameClient.instance.doDisconnect("Quitting");

         try {
            Thread.sleep(500L);
         } catch (InterruptedException var2) {
            var2.printStackTrace();
         }
      }

      DebugLog.log("EXITDEBUG: GameWindow.exit 3");
      if (PlayerDB.isAvailable()) {
         PlayerDB.getInstance().close();
      }

      if (ClientPlayerDB.isAvailable()) {
         ClientPlayerDB.getInstance().close();
      }

      DebugLog.log("EXITDEBUG: GameWindow.exit 4");
      GameClient.instance.Shutdown();
      SteamUtils.shutdown();
      ZipLogs.addZipFile(true);
      onGameThreadExited();
      DebugLog.log("EXITDEBUG: GameWindow.exit 5");
   }

   private static void onGameThreadExited() {
      bGameThreadExited = true;
      RenderThread.onGameThreadExited();
   }

   public static void setTexturePackLookup() {
      texturePackTextures.clear();

      for(int var0 = texturePacks.size() - 1; var0 >= 0; --var0) {
         GameWindow.TexturePack var1 = (GameWindow.TexturePack)texturePacks.get(var0);
         if (var1.modID == null) {
            texturePackTextures.putAll(var1.textures);
         }
      }

      ArrayList var3 = ZomboidFileSystem.instance.getModIDs();

      for(int var4 = texturePacks.size() - 1; var4 >= 0; --var4) {
         GameWindow.TexturePack var2 = (GameWindow.TexturePack)texturePacks.get(var4);
         if (var2.modID != null && var3.contains(var2.modID)) {
            texturePackTextures.putAll(var2.textures);
         }
      }

      Texture.onTexturePacksChanged();
   }

   public static void LoadTexturePack(String var0, int var1) {
      LoadTexturePack(var0, var1, (String)null);
   }

   public static void LoadTexturePack(String var0, int var1, String var2) {
      DebugLog.General.println("texturepack: loading " + var0);
      DoLoadingText(Translator.getText("UI_Loading_Texturepack", var0));
      String var3 = ZomboidFileSystem.instance.getString("media/texturepacks/" + var0 + ".pack");
      GameWindow.TexturePack var4 = new GameWindow.TexturePack();
      var4.packName = var0;
      var4.fileName = var3;
      var4.modID = var2;
      fileSystem.mountTexturePack(var0, var4.textures, var1);
      texturePacks.add(var4);
   }

   /** @deprecated */
   @Deprecated
   public static void LoadTexturePackDDS(String var0) {
      DebugLog.log("texturepack: loading " + var0);
      if (SpriteRenderer.instance != null) {
         Core.getInstance().StartFrame();
         Core.getInstance().EndFrame(0);
         Core.getInstance().StartFrameUI();
         SpriteRenderer.instance.renderi((Texture)null, 0, 0, Core.getInstance().getScreenWidth(), Core.getInstance().getScreenHeight(), 0.0F, 0.0F, 0.0F, 1.0F, (Consumer)null);
         TextManager.instance.DrawStringCentre((double)(Core.getInstance().getScreenWidth() / 2), (double)(Core.getInstance().getScreenHeight() / 2), Translator.getText("UI_Loading_Texturepack", var0), 1.0D, 1.0D, 1.0D, 1.0D);
         Core.getInstance().EndFrameUI();
      }

      FileInputStream var1 = null;

      try {
         var1 = new FileInputStream(ZomboidFileSystem.instance.getString("media/texturepacks/" + var0 + ".pack"));
      } catch (FileNotFoundException var7) {
         Logger.getLogger(GameLoadingState.class.getName()).log(Level.SEVERE, (String)null, var7);
      }

      try {
         BufferedInputStream var2 = new BufferedInputStream(var1);

         try {
            int var3 = TexturePackPage.readInt((InputStream)var2);
            int var4 = 0;

            while(true) {
               if (var4 >= var3) {
                  DebugLog.log("texturepack: finished loading " + var0);
                  break;
               }

               TexturePackPage var5 = new TexturePackPage();
               if (var4 % 100 == 0 && SpriteRenderer.instance != null) {
                  Core.getInstance().StartFrame();
                  Core.getInstance().EndFrame();
                  Core.getInstance().StartFrameUI();
                  TextManager.instance.DrawStringCentre((double)(Core.getInstance().getScreenWidth() / 2), (double)(Core.getInstance().getScreenHeight() / 2), Translator.getText("UI_Loading_Texturepack", var0), 1.0D, 1.0D, 1.0D, 1.0D);
                  Core.getInstance().EndFrameUI();
                  RenderThread.invokeOnRenderContext(Display::update);
               }

               var5.loadFromPackFileDDS(var2);
               ++var4;
            }
         } catch (Throwable var8) {
            try {
               var2.close();
            } catch (Throwable var6) {
               var8.addSuppressed(var6);
            }

            throw var8;
         }

         var2.close();
      } catch (Exception var9) {
         DebugLog.log("media/texturepacks/" + var0 + ".pack");
         var9.printStackTrace();
      }

      Texture.nullTextures.clear();
   }

   private static void installRequiredLibrary(String var0, String var1) {
      if ((new File(var0)).exists()) {
         DebugLog.log("Attempting to install " + var1);
         DebugLog.log("Running " + var0 + ".");
         ProcessBuilder var2 = new ProcessBuilder(new String[]{var0, "/quiet", "/norestart"});

         try {
            Process var3 = var2.start();
            int var4 = var3.waitFor();
            DebugLog.log("Process exited with code " + var4);
            return;
         } catch (InterruptedException | IOException var5) {
            var5.printStackTrace();
         }
      }

      DebugLog.log("Please install " + var1);
   }

   private static void checkRequiredLibraries() {
      if (System.getProperty("os.name").startsWith("Win")) {
         String var0;
         String var1;
         String var2;
         String var3;
         if (System.getProperty("sun.arch.data.model").equals("64")) {
            var0 = "Lighting64";
            var1 = "_CommonRedist\\vcredist\\2010\\vcredist_x64.exe";
            var2 = "_CommonRedist\\vcredist\\2012\\vcredist_x64.exe";
            var3 = "_CommonRedist\\vcredist\\2013\\vcredist_x64.exe";
         } else {
            var0 = "Lighting32";
            var1 = "_CommonRedist\\vcredist\\2010\\vcredist_x86.exe";
            var2 = "_CommonRedist\\vcredist\\2012\\vcredist_x86.exe";
            var3 = "_CommonRedist\\vcredist\\2013\\vcredist_x86.exe";
         }

         if ("1".equals(System.getProperty("zomboid.debuglibs.lighting"))) {
            DebugLog.log("***** Loading debug version of Lighting");
            var0 = var0 + "d";
         }

         try {
            System.loadLibrary(var0);
         } catch (UnsatisfiedLinkError var5) {
            DebugLog.log("Error loading " + var0 + ".dll.  Your system may be missing a required DLL.");
            installRequiredLibrary(var1, "the Microsoft Visual C++ 2010 Redistributable.");
            installRequiredLibrary(var2, "the Microsoft Visual C++ 2012 Redistributable.");
            installRequiredLibrary(var3, "the Microsoft Visual C++ 2013 Redistributable.");
         }
      }

   }

   private static void init() throws Exception {
      initFonts();
      checkRequiredLibraries();
      SteamUtils.init();
      ServerBrowser.init();
      SteamFriends.init();
      SteamWorkshop.init();
      RakNetPeerInterface.init();
      LightingJNI.init();
      ZombiePopulationManager.init();
      PZSQLUtils.init();
      Clipper.init();
      WorldMapJNI.init();
      Bullet.init();
      int var0 = Runtime.getRuntime().availableProcessors();
      if (Core.bMultithreadedRendering) {
         Core.bMultithreadedRendering = var0 > 1;
      }

      String var10000 = ZomboidFileSystem.instance.getCacheDir();
      String var1 = var10000 + File.separator;
      File var2 = new File(var1);
      if (!var2.exists()) {
         var2.mkdirs();
      }

      DoLoadingText("Loading Mods");
      ZomboidFileSystem.instance.resetDefaultModsForNewRelease("41_51");
      ZomboidFileSystem.instance.loadMods("default");
      ZomboidFileSystem.instance.loadModPackFiles();
      DoLoadingText("Loading Translations");
      Languages.instance.init();
      Translator.language = null;
      initFonts();
      Translator.loadFiles();
      initShared();
      DoLoadingText(Translator.getText("UI_Loading_Lua"));
      LuaManager.init();
      CustomPerks.instance.initLua();
      CustomSandboxOptions.instance.init();
      CustomSandboxOptions.instance.initInstance(SandboxOptions.instance);
      LuaManager.LoadDirBase();
      ZomboidGlobals.Load();
      LuaEventManager.triggerEvent("OnLoadSoundBanks");
   }

   private static void initFonts() throws FileNotFoundException {
      TextManager.instance.Init();

      while(TextManager.instance.font.isEmpty()) {
         fileSystem.updateAsyncTransactions();

         try {
            Thread.sleep(10L);
         } catch (InterruptedException var1) {
         }
      }

   }

   public static void savePlayer() {
   }

   public static void save(boolean var0) throws IOException {
      if (!Core.getInstance().isNoSave()) {
         if (IsoWorld.instance.CurrentCell != null && !"LastStand".equals(Core.getInstance().getGameMode()) && !"Tutorial".equals(Core.getInstance().getGameMode())) {
            File var1 = ZomboidFileSystem.instance.getFileInCurrentSave("map_ver.bin");
            FileOutputStream var2 = new FileOutputStream(var1);

            DataOutputStream var3;
            try {
               var3 = new DataOutputStream(var2);

               try {
                  var3.writeInt(186);
                  WriteString(var3, Core.GameMap);
                  WriteString(var3, IsoWorld.instance.getDifficulty());
               } catch (Throwable var18) {
                  try {
                     var3.close();
                  } catch (Throwable var7) {
                     var18.addSuppressed(var7);
                  }

                  throw var18;
               }

               var3.close();
            } catch (Throwable var19) {
               try {
                  var2.close();
               } catch (Throwable var6) {
                  var19.addSuppressed(var6);
               }

               throw var19;
            }

            var2.close();
            var1 = ZomboidFileSystem.instance.getFileInCurrentSave("map_sand.bin");
            var2 = new FileOutputStream(var1);

            try {
               BufferedOutputStream var22 = new BufferedOutputStream(var2);

               try {
                  SliceY.SliceBuffer.clear();
                  SandboxOptions.instance.save(SliceY.SliceBuffer);
                  var22.write(SliceY.SliceBuffer.array(), 0, SliceY.SliceBuffer.position());
               } catch (Throwable var16) {
                  try {
                     var22.close();
                  } catch (Throwable var9) {
                     var16.addSuppressed(var9);
                  }

                  throw var16;
               }

               var22.close();
            } catch (Throwable var17) {
               try {
                  var2.close();
               } catch (Throwable var8) {
                  var17.addSuppressed(var8);
               }

               throw var17;
            }

            var2.close();
            LuaEventManager.triggerEvent("OnSave");

            try {
               try {
                  try {
                     if (Thread.currentThread() == GameThread) {
                        SavefileThumbnail.create();
                     }
                  } catch (Exception var14) {
                     ExceptionLogger.logException(var14);
                  }

                  var1 = ZomboidFileSystem.instance.getFileInCurrentSave("map.bin");

                  try {
                     var2 = new FileOutputStream(var1);

                     try {
                        var3 = new DataOutputStream(var2);
                        IsoWorld.instance.CurrentCell.save(var3, var0);
                     } catch (Throwable var12) {
                        try {
                           var2.close();
                        } catch (Throwable var11) {
                           var12.addSuppressed(var11);
                        }

                        throw var12;
                     }

                     var2.close();
                  } catch (Exception var13) {
                     ExceptionLogger.logException(var13);
                  }

                  try {
                     MapCollisionData.instance.save();
                     if (!bLoadedAsClient) {
                        SGlobalObjects.save();
                     }
                  } catch (Exception var10) {
                     ExceptionLogger.logException(var10);
                  }

                  ZomboidRadio.getInstance().Save();
                  GlobalModData.instance.save();
                  MapItem.SaveWorldMap();
                  WorldMapVisited.SaveAll();
               } catch (IOException var15) {
                  throw new RuntimeException(var15);
               }
            } catch (RuntimeException var20) {
               Throwable var21 = var20.getCause();
               if (var21 instanceof IOException) {
                  throw (IOException)var21;
               } else {
                  throw var20;
               }
            }
         }
      }
   }

   public static String getCoopServerHome() {
      File var0 = new File(ZomboidFileSystem.instance.getCacheDir());
      return var0.getParent();
   }

   public static void WriteString(ByteBuffer var0, String var1) {
      WriteStringUTF(var0, var1);
   }

   public static void WriteStringUTF(ByteBuffer var0, String var1) {
      ((GameWindow.StringUTF)stringUTF.get()).save(var0, var1);
   }

   public static void WriteString(DataOutputStream var0, String var1) throws IOException {
      if (var1 == null) {
         var0.writeInt(0);
      } else {
         var0.writeInt(var1.length());
         if (var1 != null && var1.length() >= 0) {
            var0.writeChars(var1);
         }

      }
   }

   public static String ReadStringUTF(ByteBuffer var0) {
      return ((GameWindow.StringUTF)stringUTF.get()).load(var0);
   }

   public static String ReadString(ByteBuffer var0) {
      return ReadStringUTF(var0);
   }

   public static String ReadString(DataInputStream var0) throws IOException {
      int var1 = var0.readInt();
      if (var1 == 0) {
         return "";
      } else if (var1 > 65536) {
         throw new RuntimeException("GameWindow.ReadString: string is too long, corrupted save?");
      } else {
         StringBuilder var2 = new StringBuilder(var1);

         for(int var3 = 0; var3 < var1; ++var3) {
            var2.append(var0.readChar());
         }

         return var2.toString();
      }
   }

   public static void doRenderEvent(boolean var0) {
      doRenderEvent = var0;
   }

   public static void DoLoadingText(String var0) {
      if (SpriteRenderer.instance != null && TextManager.instance.font != null) {
         Core.getInstance().StartFrame();
         Core.getInstance().EndFrame();
         Core.getInstance().StartFrameUI();
         SpriteRenderer.instance.renderi((Texture)null, 0, 0, Core.getInstance().getScreenWidth(), Core.getInstance().getScreenHeight(), 0.0F, 0.0F, 0.0F, 1.0F, (Consumer)null);
         TextManager.instance.DrawStringCentre((double)(Core.getInstance().getScreenWidth() / 2), (double)(Core.getInstance().getScreenHeight() / 2), var0, 1.0D, 1.0D, 1.0D, 1.0D);
         Core.getInstance().EndFrameUI();
      }

   }

   static {
      assetManagers = new AssetManagers(fileSystem);
      bGameThreadExited = false;
      texturePacks = new ArrayList();
      texturePackTextures = new FileSystem.TexturePackTextures();
   }

   private static class s_performance {
      static final PerformanceProfileFrameProbe frameStep = new PerformanceProfileFrameProbe("GameWindow.frameStep");
      static final PerformanceProfileProbe statesRender = new PerformanceProfileProbe("GameWindow.states.render");
      static final PerformanceProfileProbe logic = new PerformanceProfileProbe("GameWindow.logic");
   }

   private static final class TexturePack {
      String packName;
      String fileName;
      String modID;
      final FileSystem.TexturePackTextures textures = new FileSystem.TexturePackTextures();
   }

   private static class StringUTF {
      private char[] chars;
      private ByteBuffer byteBuffer;
      private CharBuffer charBuffer;
      private CharsetEncoder ce;
      private CharsetDecoder cd;

      private int encode(String var1) {
         int var2;
         if (this.chars == null || this.chars.length < var1.length()) {
            var2 = (var1.length() + 128 - 1) / 128 * 128;
            this.chars = new char[var2];
            this.charBuffer = CharBuffer.wrap(this.chars);
         }

         var1.getChars(0, var1.length(), this.chars, 0);
         this.charBuffer.limit(var1.length());
         this.charBuffer.position(0);
         if (this.ce == null) {
            this.ce = StandardCharsets.UTF_8.newEncoder().onMalformedInput(CodingErrorAction.REPLACE).onUnmappableCharacter(CodingErrorAction.REPLACE);
         }

         this.ce.reset();
         var2 = (int)((double)var1.length() * (double)this.ce.maxBytesPerChar());
         var2 = (var2 + 128 - 1) / 128 * 128;
         if (this.byteBuffer == null || this.byteBuffer.capacity() < var2) {
            this.byteBuffer = ByteBuffer.allocate(var2);
         }

         this.byteBuffer.clear();
         CoderResult var3 = this.ce.encode(this.charBuffer, this.byteBuffer, true);
         return this.byteBuffer.position();
      }

      private String decode(int var1) {
         if (this.cd == null) {
            this.cd = StandardCharsets.UTF_8.newDecoder().onMalformedInput(CodingErrorAction.REPLACE).onUnmappableCharacter(CodingErrorAction.REPLACE);
         }

         this.cd.reset();
         int var2 = (int)((double)var1 * (double)this.cd.maxCharsPerByte());
         if (this.chars == null || this.chars.length < var2) {
            int var3 = (var2 + 128 - 1) / 128 * 128;
            this.chars = new char[var3];
            this.charBuffer = CharBuffer.wrap(this.chars);
         }

         this.charBuffer.clear();
         CoderResult var4 = this.cd.decode(this.byteBuffer, this.charBuffer, true);
         return new String(this.chars, 0, this.charBuffer.position());
      }

      void save(ByteBuffer var1, String var2) {
         if (var2 != null && !var2.isEmpty()) {
            int var3 = this.encode(var2);
            var1.putShort((short)var3);
            this.byteBuffer.flip();
            var1.put(this.byteBuffer);
         } else {
            var1.putShort((short)0);
         }
      }

      String load(ByteBuffer var1) {
         short var2 = var1.getShort();
         if (var2 <= 0) {
            return "";
         } else {
            int var3 = (var2 + 128 - 1) / 128 * 128;
            if (this.byteBuffer == null || this.byteBuffer.capacity() < var3) {
               this.byteBuffer = ByteBuffer.allocate(var3);
            }

            this.byteBuffer.clear();
            if (var1.remaining() < var2) {
               DebugLog.General.error("GameWindow.StringUTF.load> numBytes:" + var2 + " is higher than the remaining bytes in the buffer:" + var1.remaining());
            }

            int var4 = var1.limit();
            var1.limit(var1.position() + var2);
            this.byteBuffer.put(var1);
            var1.limit(var4);
            this.byteBuffer.flip();
            return this.decode(var2);
         }
      }
   }

   public static class OSValidator {
      private static String OS = System.getProperty("os.name").toLowerCase();

      public static boolean isWindows() {
         return OS.indexOf("win") >= 0;
      }

      public static boolean isMac() {
         return OS.indexOf("mac") >= 0;
      }

      public static boolean isUnix() {
         return OS.indexOf("nix") >= 0 || OS.indexOf("nux") >= 0 || OS.indexOf("aix") > 0;
      }

      public static boolean isSolaris() {
         return OS.indexOf("sunos") >= 0;
      }
   }
}

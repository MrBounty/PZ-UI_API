package zombie.core;

import fmod.FMOD_DriverInfo;
import fmod.javafmod;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.function.Consumer;
import javax.imageio.ImageIO;
import org.joml.Matrix4f;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL32;
import org.lwjgl.system.MemoryUtil;
import org.lwjgl.util.glu.GLU;
import org.lwjglx.LWJGLException;
import org.lwjglx.input.Controller;
import org.lwjglx.input.Keyboard;
import org.lwjglx.opengl.Display;
import org.lwjglx.opengl.DisplayMode;
import org.lwjglx.opengl.OpenGLException;
import org.lwjglx.opengl.PixelFormat;
import se.krka.kahlua.vm.KahluaTable;
import zombie.GameSounds;
import zombie.GameTime;
import zombie.GameWindow;
import zombie.IndieGL;
import zombie.MovingObjectUpdateScheduler;
import zombie.SandboxOptions;
import zombie.SoundManager;
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
import zombie.core.VBO.GLVertexBufferObject;
import zombie.core.logger.ExceptionLogger;
import zombie.core.math.PZMath;
import zombie.core.opengl.PZGLUtil;
import zombie.core.opengl.RenderThread;
import zombie.core.opengl.Shader;
import zombie.core.raknet.VoiceManager;
import zombie.core.skinnedmodel.ModelManager;
import zombie.core.skinnedmodel.advancedanimation.AnimationSet;
import zombie.core.skinnedmodel.population.BeardStyles;
import zombie.core.skinnedmodel.population.ClothingDecals;
import zombie.core.skinnedmodel.population.HairStyles;
import zombie.core.skinnedmodel.population.OutfitManager;
import zombie.core.sprite.SpriteRenderState;
import zombie.core.textures.ColorInfo;
import zombie.core.textures.MultiTextureFBO2;
import zombie.core.textures.Texture;
import zombie.core.textures.TextureFBO;
import zombie.core.znet.SteamUtils;
import zombie.debug.DebugLog;
import zombie.debug.DebugOptions;
import zombie.gameStates.ChooseGameInfo;
import zombie.gameStates.IngameState;
import zombie.input.GameKeyboard;
import zombie.input.JoypadManager;
import zombie.input.Mouse;
import zombie.iso.BentFences;
import zombie.iso.BrokenFences;
import zombie.iso.ContainerOverlays;
import zombie.iso.IsoCamera;
import zombie.iso.IsoPuddles;
import zombie.iso.IsoWater;
import zombie.iso.PlayerCamera;
import zombie.iso.TileOverlays;
import zombie.iso.weather.WeatherShader;
import zombie.modding.ActiveMods;
import zombie.network.GameClient;
import zombie.network.GameServer;
import zombie.sandbox.CustomSandboxOptions;
import zombie.scripting.ScriptManager;
import zombie.scripting.objects.Item;
import zombie.ui.FPSGraph;
import zombie.ui.ObjectTooltip;
import zombie.ui.TextManager;
import zombie.ui.UIManager;
import zombie.ui.UITextBox2;
import zombie.util.StringUtils;
import zombie.vehicles.VehicleType;
import zombie.worldMap.WorldMap;

public final class Core {
   public static final boolean bDemo = false;
   public static boolean bTutorial;
   private static boolean fakefullscreen = false;
   private static final GameVersion gameVersion = new GameVersion(41, 65, "");
   public String steamServerVersion = "1.0.0.0";
   public static boolean bMultithreadedRendering = true;
   public static boolean bAltMoveMethod = false;
   private boolean rosewoodSpawnDone = false;
   private final ColorInfo objectHighlitedColor = new ColorInfo(0.98F, 0.56F, 0.11F, 1.0F);
   private boolean flashIsoCursor = false;
   private int isoCursorVisibility = 5;
   public static boolean OptionShowCursorWhileAiming = false;
   private boolean collideZombies = true;
   public final MultiTextureFBO2 OffscreenBuffer = new MultiTextureFBO2();
   private String saveFolder = null;
   public static boolean OptionZoom = true;
   public static boolean OptionModsEnabled = true;
   public static int OptionFontSize = 1;
   public static String OptionContextMenuFont = "Medium";
   public static String OptionInventoryFont = "Medium";
   public static String OptionTooltipFont = "Small";
   public static String OptionMeasurementFormat = "Metric";
   public static int OptionClockFormat = 1;
   public static int OptionClockSize = 2;
   public static boolean OptionClock24Hour = true;
   public static boolean OptionVSync = false;
   public static int OptionSoundVolume = 8;
   public static int OptionMusicVolume = 6;
   public static int OptionAmbientVolume = 5;
   public static int OptionMusicLibrary = 1;
   public static boolean OptionVoiceEnable = true;
   public static int OptionVoiceMode = 3;
   public static int OptionVoiceVADMode = 3;
   public static String OptionVoiceRecordDeviceName = "";
   public static int OptionVoiceVolumeMic = 10;
   public static int OptionVoiceVolumePlayers = 5;
   public static int OptionVehicleEngineVolume = 5;
   public static int OptionReloadDifficulty = 2;
   public static boolean OptionRackProgress = true;
   public static int OptionBloodDecals = 10;
   public static boolean OptionBorderlessWindow = false;
   public static boolean OptionLockCursorToWindow = false;
   public static boolean OptionTextureCompression = false;
   public static boolean OptionModelTextureMipmaps = false;
   public static boolean OptionTexture2x = true;
   private static String OptionZoomLevels1x = "";
   private static String OptionZoomLevels2x = "";
   public static boolean OptionEnableContentTranslations = true;
   public static boolean OptionUIFBO = true;
   public static int OptionUIRenderFPS = 20;
   public static boolean OptionRadialMenuKeyToggle = true;
   public static boolean OptionReloadRadialInstant = false;
   public static boolean OptionPanCameraWhileAiming = true;
   public static boolean OptionPanCameraWhileDriving = false;
   public static boolean OptionShowChatTimestamp = false;
   public static boolean OptionShowChatTitle = false;
   public static String OptionChatFontSize = "medium";
   public static float OptionMinChatOpaque = 1.0F;
   public static float OptionMaxChatOpaque = 1.0F;
   public static float OptionChatFadeTime = 0.0F;
   public static boolean OptionChatOpaqueOnFocus = true;
   public static boolean OptionTemperatureDisplayCelsius = false;
   public static boolean OptionDoWindSpriteEffects = true;
   public static boolean OptionDoDoorSpriteEffects = true;
   public static boolean OptionRenderPrecipIndoors = true;
   public static boolean OptionAutoProneAtk = true;
   public static boolean Option3DGroundItem = true;
   public static int OptionRenderPrecipitation = 1;
   public static boolean OptionUpdateSneakButton = true;
   public static boolean OptiondblTapJogToSprint = false;
   private static int OptionAimOutline = 1;
   private static String OptionCycleContainerKey = "shift";
   private static boolean OptionDropItemsOnSquareCenter = false;
   private static boolean OptionTimedActionGameSpeedReset = false;
   private static int OptionShoulderButtonContainerSwitch = 1;
   private static boolean OptionProgressBar = false;
   private static String OptionLanguageName = null;
   private static final boolean[] OptionSingleContextMenu = new boolean[4];
   private static boolean OptionCorpseShadows = true;
   private static int OptionSimpleClothingTextures = 1;
   private static boolean OptionSimpleWeaponTextures = false;
   private static boolean OptionAutoDrink = true;
   private static boolean OptionLeaveKeyInIgnition = false;
   private static int OptionSearchModeOverlayEffect = 1;
   private static int OptionIgnoreProneZombieRange = 2;
   private boolean showPing = true;
   private boolean forceSnow = false;
   private boolean zombieGroupSound = true;
   private String blinkingMoodle = null;
   private boolean tutorialDone = false;
   private boolean vehiclesWarningShow = false;
   private String poisonousBerry = null;
   private String poisonousMushroom = null;
   private boolean doneNewSaveFolder = false;
   private static String difficulty = "Hardcore";
   public static int TileScale = 2;
   private boolean isSelectingAll = false;
   private boolean showYourUsername = true;
   private ColorInfo mpTextColor = null;
   private boolean isAzerty = false;
   private String seenUpdateText = "";
   private boolean toggleToAim = false;
   private boolean toggleToRun = false;
   private boolean toggleToSprint = true;
   private boolean celsius = false;
   private boolean riversideDone = false;
   private boolean noSave = false;
   private boolean showFirstTimeVehicleTutorial = false;
   private boolean showFirstTimeWeatherTutorial = false;
   private boolean showFirstTimeSneakTutorial = true;
   private boolean newReloading = true;
   private boolean gotNewBelt = false;
   private boolean bAnimPopupDone = false;
   private boolean bModsPopupDone = false;
   public static float blinkAlpha = 1.0F;
   public static boolean blinkAlphaIncrease = false;
   private static HashMap optionsOnStartup = new HashMap();
   private boolean bChallenge;
   public static int width = 1280;
   public static int height = 720;
   public static int MaxJukeBoxesActive = 10;
   public static int NumJukeBoxesActive = 0;
   public static String GameMode = "Sandbox";
   private static String glVersion;
   private static int glMajorVersion = -1;
   private static Core core = new Core();
   public static boolean bDebug = false;
   public static UITextBox2 CurrentTextEntryBox = null;
   public Shader RenderShader;
   private Map keyMaps = null;
   public final boolean bUseShaders = true;
   private int iPerfSkybox = 1;
   private int iPerfSkybox_new = 1;
   public static final int iPerfSkybox_High = 0;
   public static final int iPerfSkybox_Medium = 1;
   public static final int iPerfSkybox_Static = 2;
   private int iPerfPuddles = 0;
   private int iPerfPuddles_new = 0;
   public static final int iPerfPuddles_None = 3;
   public static final int iPerfPuddles_GroundOnly = 2;
   public static final int iPerfPuddles_GroundWithRuts = 1;
   public static final int iPerfPuddles_All = 0;
   private boolean bPerfReflections = true;
   private boolean bPerfReflections_new = true;
   public int vidMem = 3;
   private boolean bSupportsFBO = true;
   public float UIRenderAccumulator = 0.0F;
   public boolean UIRenderThisFrame = true;
   public int version = 1;
   public int fileversion = 7;
   private static boolean fullScreen = false;
   private static final boolean[] bAutoZoom = new boolean[4];
   public static String GameMap = "DEFAULT";
   public static String GameSaveWorld = "";
   public static boolean SafeMode = false;
   public static boolean SafeModeForced = false;
   public static boolean SoundDisabled = false;
   public int frameStage = 0;
   private int stack = 0;
   public static int xx = 0;
   public static int yy = 0;
   public static int zz = 0;
   public final HashMap FloatParamMap = new HashMap();
   private final Matrix4f tempMatrix4f = new Matrix4f();
   private static final float isoAngle = 62.65607F;
   private static final float scale = 0.047085002F;
   public static boolean bLastStand = false;
   public static String ChallengeID = null;
   public static boolean bLoadedWithMultithreaded = false;
   public static boolean bExiting = false;
   private String m_delayResetLua_activeMods = null;
   private String m_delayResetLua_reason = null;

   public boolean isMultiThread() {
      return bMultithreadedRendering;
   }

   public void setChallenge(boolean var1) {
      this.bChallenge = var1;
   }

   public boolean isChallenge() {
      return this.bChallenge;
   }

   public String getChallengeID() {
      return ChallengeID;
   }

   public boolean getOptionTieredZombieUpdates() {
      return MovingObjectUpdateScheduler.instance.isEnabled();
   }

   public void setOptionTieredZombieUpdates(boolean var1) {
      MovingObjectUpdateScheduler.instance.setEnabled(var1);
   }

   public void setFramerate(int var1) {
      PerformanceSettings.setUncappedFPS(var1 == 1);
      switch(var1) {
      case 1:
         PerformanceSettings.setLockFPS(60);
         break;
      case 2:
         PerformanceSettings.setLockFPS(244);
         break;
      case 3:
         PerformanceSettings.setLockFPS(240);
         break;
      case 4:
         PerformanceSettings.setLockFPS(165);
         break;
      case 5:
         PerformanceSettings.setLockFPS(120);
         break;
      case 6:
         PerformanceSettings.setLockFPS(95);
         break;
      case 7:
         PerformanceSettings.setLockFPS(90);
         break;
      case 8:
         PerformanceSettings.setLockFPS(75);
         break;
      case 9:
         PerformanceSettings.setLockFPS(60);
         break;
      case 10:
         PerformanceSettings.setLockFPS(55);
         break;
      case 11:
         PerformanceSettings.setLockFPS(45);
         break;
      case 12:
         PerformanceSettings.setLockFPS(30);
         break;
      case 13:
         PerformanceSettings.setLockFPS(24);
      }

   }

   public void setMultiThread(boolean var1) {
      bMultithreadedRendering = var1;

      try {
         this.saveOptions();
      } catch (IOException var3) {
         var3.printStackTrace();
      }

   }

   public boolean loadedShader() {
      return this.RenderShader != null;
   }

   public static int getGLMajorVersion() {
      if (glMajorVersion == -1) {
         getOpenGLVersions();
      }

      return glMajorVersion;
   }

   public boolean getUseShaders() {
      return true;
   }

   public int getPerfSkybox() {
      return this.iPerfSkybox_new;
   }

   public int getPerfSkyboxOnLoad() {
      return this.iPerfSkybox;
   }

   public void setPerfSkybox(int var1) {
      this.iPerfSkybox_new = var1;
   }

   public boolean getPerfReflections() {
      return this.bPerfReflections_new;
   }

   public boolean getPerfReflectionsOnLoad() {
      return this.bPerfReflections;
   }

   public void setPerfReflections(boolean var1) {
      this.bPerfReflections_new = var1;
   }

   public int getPerfPuddles() {
      return this.iPerfPuddles_new;
   }

   public int getPerfPuddlesOnLoad() {
      return this.iPerfPuddles;
   }

   public void setPerfPuddles(int var1) {
      this.iPerfPuddles_new = var1;
   }

   public int getVidMem() {
      return SafeMode ? 5 : this.vidMem;
   }

   public void setVidMem(int var1) {
      if (SafeMode) {
         this.vidMem = 5;
      }

      this.vidMem = var1;

      try {
         this.saveOptions();
      } catch (IOException var3) {
         var3.printStackTrace();
      }

   }

   public void setUseShaders(boolean var1) {
   }

   public void shadersOptionChanged() {
      RenderThread.invokeOnRenderContext(() -> {
         if (!SafeModeForced) {
            try {
               if (this.RenderShader == null) {
                  this.RenderShader = new WeatherShader("screen");
               }

               if (this.RenderShader != null && !this.RenderShader.isCompiled()) {
                  this.RenderShader = null;
               }
            } catch (Exception var3) {
               this.RenderShader = null;
            }
         } else if (this.RenderShader != null) {
            try {
               this.RenderShader.destroy();
            } catch (Exception var2) {
               var2.printStackTrace();
            }

            this.RenderShader = null;
         }

      });
   }

   public void initShaders() {
      try {
         if (this.RenderShader == null && !SafeMode && !SafeModeForced) {
            RenderThread.invokeOnRenderContext(() -> {
               this.RenderShader = new WeatherShader("screen");
            });
         }

         if (this.RenderShader == null || !this.RenderShader.isCompiled()) {
            this.RenderShader = null;
         }
      } catch (Exception var2) {
         this.RenderShader = null;
         var2.printStackTrace();
      }

      IsoPuddles.getInstance();
      IsoWater.getInstance();
   }

   public static String getGLVersion() {
      if (glVersion == null) {
         getOpenGLVersions();
      }

      return glVersion;
   }

   public String getGameMode() {
      return GameMode;
   }

   public static Core getInstance() {
      return core;
   }

   public static void getOpenGLVersions() {
      glVersion = GL11.glGetString(7938);
      glMajorVersion = glVersion.charAt(0) - 48;
   }

   public boolean getDebug() {
      return bDebug;
   }

   public static void setFullScreen(boolean var0) {
      fullScreen = var0;
   }

   public static int[] flipPixels(int[] var0, int var1, int var2) {
      int[] var3 = null;
      if (var0 != null) {
         var3 = new int[var1 * var2];

         for(int var4 = 0; var4 < var2; ++var4) {
            for(int var5 = 0; var5 < var1; ++var5) {
               var3[(var2 - var4 - 1) * var1 + var5] = var0[var4 * var1 + var5];
            }
         }
      }

      return var3;
   }

   public void TakeScreenshot() {
      this.TakeScreenshot(256, 256, 1028);
   }

   public void TakeScreenshot(int var1, int var2, int var3) {
      byte var4 = 0;
      int var5 = IsoCamera.getScreenWidth(var4);
      int var6 = IsoCamera.getScreenHeight(var4);
      var1 = PZMath.min(var1, var5);
      var2 = PZMath.min(var2, var6);
      int var7 = IsoCamera.getScreenLeft(var4) + var5 / 2 - var1 / 2;
      int var8 = IsoCamera.getScreenTop(var4) + var6 / 2 - var2 / 2;
      this.TakeScreenshot(var7, var8, var1, var2, var3);
   }

   public void TakeScreenshot(int var1, int var2, int var3, int var4, int var5) {
      GL11.glPixelStorei(3333, 1);
      GL11.glReadBuffer(var5);
      byte var6 = 3;
      ByteBuffer var7 = MemoryUtil.memAlloc(var3 * var4 * var6);
      GL11.glReadPixels(var1, var2, var3, var4, 6407, 5121, var7);
      int[] var8 = new int[var3 * var4];
      File var10 = ZomboidFileSystem.instance.getFileInCurrentSave("thumb.png");
      String var11 = "png";

      for(int var12 = 0; var12 < var8.length; ++var12) {
         int var9 = var12 * 3;
         var8[var12] = -16777216 | (var7.get(var9) & 255) << 16 | (var7.get(var9 + 1) & 255) << 8 | (var7.get(var9 + 2) & 255) << 0;
      }

      MemoryUtil.memFree(var7);
      var8 = flipPixels(var8, var3, var4);
      BufferedImage var15 = new BufferedImage(var3, var4, 2);
      var15.setRGB(0, 0, var3, var4, var8, 0, var3);

      try {
         ImageIO.write(var15, "png", var10);
      } catch (IOException var14) {
         var14.printStackTrace();
      }

      String var10000 = ZomboidFileSystem.instance.getGameModeCacheDir();
      Texture.reload(var10000 + GameSaveWorld + File.separator + "thumb.png");
   }

   public void TakeFullScreenshot(String var1) {
      RenderThread.invokeOnRenderContext(var1, (var0) -> {
         GL11.glPixelStorei(3333, 1);
         GL11.glReadBuffer(1028);
         int var1 = Display.getDisplayMode().getWidth();
         int var2 = Display.getDisplayMode().getHeight();
         byte var3 = 0;
         byte var4 = 0;
         byte var5 = 3;
         ByteBuffer var6 = MemoryUtil.memAlloc(var1 * var2 * var5);
         GL11.glReadPixels(var3, var4, var1, var2, 6407, 5121, var6);
         int[] var7 = new int[var1 * var2];
         if (var0 == null) {
            SimpleDateFormat var9 = new SimpleDateFormat("dd-MM-yyyy_HH-mm-ss");
            var0 = "screenshot_" + var9.format(Calendar.getInstance().getTime()) + ".png";
         }

         String var10002 = ZomboidFileSystem.instance.getScreenshotDir();
         File var13 = new File(var10002 + File.separator + var0);

         for(int var10 = 0; var10 < var7.length; ++var10) {
            int var8 = var10 * 3;
            var7[var10] = -16777216 | (var6.get(var8) & 255) << 16 | (var6.get(var8 + 1) & 255) << 8 | (var6.get(var8 + 2) & 255) << 0;
         }

         MemoryUtil.memFree(var6);
         var7 = flipPixels(var7, var1, var2);
         BufferedImage var14 = new BufferedImage(var1, var2, 2);
         var14.setRGB(0, 0, var1, var2, var7, 0, var1);

         try {
            ImageIO.write(var14, "png", var13);
         } catch (IOException var12) {
            var12.printStackTrace();
         }

      });
   }

   public static boolean supportNPTTexture() {
      return false;
   }

   public boolean supportsFBO() {
      if (SafeMode) {
         this.OffscreenBuffer.bZoomEnabled = false;
         return false;
      } else if (!this.bSupportsFBO) {
         return false;
      } else if (this.OffscreenBuffer.Current != null) {
         return true;
      } else {
         try {
            if (TextureFBO.checkFBOSupport() && this.setupMultiFBO()) {
               return true;
            } else {
               this.bSupportsFBO = false;
               SafeMode = true;
               this.OffscreenBuffer.bZoomEnabled = false;
               return false;
            }
         } catch (Exception var2) {
            var2.printStackTrace();
            this.bSupportsFBO = false;
            SafeMode = true;
            this.OffscreenBuffer.bZoomEnabled = false;
            return false;
         }
      }
   }

   private void sharedInit() {
      this.supportsFBO();
   }

   public void MoveMethodToggle() {
      bAltMoveMethod = !bAltMoveMethod;
   }

   public void EndFrameText(int var1) {
      if (!LuaManager.thread.bStep) {
         if (this.OffscreenBuffer.Current != null) {
         }

         IndieGL.glDoEndFrame();
         this.frameStage = 2;
      }
   }

   public void EndFrame(int var1) {
      if (!LuaManager.thread.bStep) {
         if (this.OffscreenBuffer.Current != null) {
            SpriteRenderer.instance.glBuffer(0, var1);
         }

         IndieGL.glDoEndFrame();
         this.frameStage = 2;
      }
   }

   public void EndFrame() {
      IndieGL.glDoEndFrame();
      if (this.OffscreenBuffer.Current != null) {
         SpriteRenderer.instance.glBuffer(0, 0);
      }

   }

   public void EndFrameUI() {
      if (!blinkAlphaIncrease) {
         blinkAlpha -= 0.07F * (GameTime.getInstance().getMultiplier() / 1.6F);
         if (blinkAlpha < 0.15F) {
            blinkAlpha = 0.15F;
            blinkAlphaIncrease = true;
         }
      } else {
         blinkAlpha += 0.07F * (GameTime.getInstance().getMultiplier() / 1.6F);
         if (blinkAlpha > 1.0F) {
            blinkAlpha = 1.0F;
            blinkAlphaIncrease = false;
         }
      }

      if (UIManager.useUIFBO && UIManager.UIFBO == null) {
         UIManager.CreateFBO(width, height);
      }

      if (LuaManager.thread != null && LuaManager.thread.bStep) {
         SpriteRenderer.instance.clearSprites();
      } else {
         ExceptionLogger.render();
         if (UIManager.useUIFBO && this.UIRenderThisFrame) {
            SpriteRenderer.instance.glBuffer(3, 0);
            IndieGL.glDoEndFrame();
            SpriteRenderer.instance.stopOffscreenUI();
            IndieGL.glDoStartFrame(width, height, 1.0F, -1);
            float var1 = (float)((int)(1.0F / (float)OptionUIRenderFPS * 100.0F)) / 100.0F;
            int var2 = (int)(this.UIRenderAccumulator / var1);
            this.UIRenderAccumulator -= (float)var2 * var1;
            if (FPSGraph.instance != null) {
               FPSGraph.instance.addUI(System.currentTimeMillis());
            }
         }

         if (UIManager.useUIFBO) {
            SpriteRenderer.instance.setDoAdditive(true);
            SpriteRenderer.instance.renderi((Texture)UIManager.UIFBO.getTexture(), 0, height, width, -height, 1.0F, 1.0F, 1.0F, 1.0F, (Consumer)null);
            SpriteRenderer.instance.setDoAdditive(false);
         }

         if (getInstance().getOptionLockCursorToWindow()) {
            Mouse.renderCursorTexture();
         }

         IndieGL.glDoEndFrame();
         RenderThread.Ready();
         this.frameStage = 0;
      }
   }

   public static void UnfocusActiveTextEntryBox() {
      if (CurrentTextEntryBox != null && !CurrentTextEntryBox.getUIName().contains("chat text entry")) {
         CurrentTextEntryBox.DoingTextEntry = false;
         if (CurrentTextEntryBox.Frame != null) {
            CurrentTextEntryBox.Frame.Colour = CurrentTextEntryBox.StandardFrameColour;
         }

         CurrentTextEntryBox = null;
      }

   }

   public int getOffscreenWidth(int var1) {
      if (this.OffscreenBuffer == null) {
         return IsoPlayer.numPlayers > 1 ? this.getScreenWidth() / 2 : this.getScreenWidth();
      } else {
         return this.OffscreenBuffer.getWidth(var1);
      }
   }

   public int getOffscreenHeight(int var1) {
      if (this.OffscreenBuffer == null) {
         return IsoPlayer.numPlayers > 2 ? this.getScreenHeight() / 2 : this.getScreenHeight();
      } else {
         return this.OffscreenBuffer.getHeight(var1);
      }
   }

   public int getOffscreenTrueWidth() {
      return this.OffscreenBuffer != null && this.OffscreenBuffer.Current != null ? this.OffscreenBuffer.getTexture(0).getWidth() : this.getScreenWidth();
   }

   public int getOffscreenTrueHeight() {
      return this.OffscreenBuffer != null && this.OffscreenBuffer.Current != null ? this.OffscreenBuffer.getTexture(0).getHeight() : this.getScreenHeight();
   }

   public int getScreenHeight() {
      return height;
   }

   public int getScreenWidth() {
      return width;
   }

   public void setResolutionAndFullScreen(int var1, int var2, boolean var3) {
      setDisplayMode(var1, var2, var3);
      this.setScreenSize(Display.getWidth(), Display.getHeight());
   }

   public void setResolution(String var1) {
      String[] var2 = var1.split("x");
      int var3 = Integer.parseInt(var2[0].trim());
      int var4 = Integer.parseInt(var2[1].trim());
      if (fullScreen) {
         setDisplayMode(var3, var4, true);
      } else {
         setDisplayMode(var3, var4, false);
      }

      this.setScreenSize(Display.getWidth(), Display.getHeight());

      try {
         this.saveOptions();
      } catch (IOException var6) {
         var6.printStackTrace();
      }

   }

   public boolean loadOptions() throws IOException {
      String var10002 = ZomboidFileSystem.instance.getCacheDir();
      File var1 = new File(var10002 + File.separator + "options.ini");
      int var6;
      int var27;
      if (!var1.exists()) {
         this.saveFolder = getMyDocumentFolder();
         File var20 = new File(this.saveFolder);
         var20.mkdir();
         this.copyPasteFolders("mods");
         this.setOptionLanguageName(System.getProperty("user.language").toUpperCase());
         if (Translator.getAzertyMap().contains(Translator.getLanguage().name())) {
            this.setAzerty(true);
         }

         if (!GameServer.bServer) {
            try {
               int var25 = 0;
               var27 = 0;
               DisplayMode[] var21 = Display.getAvailableDisplayModes();

               for(var6 = 0; var6 < var21.length; ++var6) {
                  if (var21[var6].getWidth() > var25 && var21[var6].getWidth() <= 1920) {
                     var25 = var21[var6].getWidth();
                     var27 = var21[var6].getHeight();
                  }
               }

               width = var25;
               height = var27;
            } catch (LWJGLException var15) {
               var15.printStackTrace();
            }
         }

         this.setOptionZoomLevels2x("50;75;125;150;175;200");
         this.setOptionZoomLevels1x("50;75;125;150;175;200");
         this.saveOptions();
         return false;
      } else {
         for(int var2 = 0; var2 < 4; ++var2) {
            this.setAutoZoom(var2, false);
         }

         OptionLanguageName = null;
         BufferedReader var19 = new BufferedReader(new FileReader(var1));

         try {
            String var3;
            while((var3 = var19.readLine()) != null) {
               if (var3.startsWith("version=")) {
                  this.version = new Integer(var3.replaceFirst("version=", ""));
               } else if (var3.startsWith("width=")) {
                  width = new Integer(var3.replaceFirst("width=", ""));
               } else if (var3.startsWith("height=")) {
                  height = new Integer(var3.replaceFirst("height=", ""));
               } else if (var3.startsWith("fullScreen=")) {
                  fullScreen = Boolean.parseBoolean(var3.replaceFirst("fullScreen=", ""));
               } else if (var3.startsWith("frameRate=")) {
                  PerformanceSettings.setLockFPS(Integer.parseInt(var3.replaceFirst("frameRate=", "")));
               } else if (var3.startsWith("uncappedFPS=")) {
                  PerformanceSettings.setUncappedFPS(Boolean.parseBoolean(var3.replaceFirst("uncappedFPS=", "")));
               } else if (var3.startsWith("iso_cursor=")) {
                  getInstance().setIsoCursorVisibility(Integer.parseInt(var3.replaceFirst("iso_cursor=", "")));
               } else if (var3.startsWith("showCursorWhileAiming=")) {
                  OptionShowCursorWhileAiming = Boolean.parseBoolean(var3.replaceFirst("showCursorWhileAiming=", ""));
               } else if (var3.startsWith("water=")) {
                  PerformanceSettings.WaterQuality = Integer.parseInt(var3.replaceFirst("water=", ""));
               } else if (var3.startsWith("puddles=")) {
                  PerformanceSettings.PuddlesQuality = Integer.parseInt(var3.replaceFirst("puddles=", ""));
               } else if (var3.startsWith("lighting=")) {
                  PerformanceSettings.LightingFrameSkip = Integer.parseInt(var3.replaceFirst("lighting=", ""));
               } else if (var3.startsWith("lightFPS=")) {
                  PerformanceSettings.instance.setLightingFPS(Integer.parseInt(var3.replaceFirst("lightFPS=", "")));
               } else if (var3.startsWith("perfSkybox=")) {
                  this.iPerfSkybox = Integer.parseInt(var3.replaceFirst("perfSkybox=", ""));
                  this.iPerfSkybox_new = this.iPerfSkybox;
               } else if (var3.startsWith("perfPuddles=")) {
                  this.iPerfPuddles = Integer.parseInt(var3.replaceFirst("perfPuddles=", ""));
                  this.iPerfPuddles_new = this.iPerfPuddles;
               } else if (var3.startsWith("bPerfReflections=")) {
                  this.bPerfReflections = Boolean.parseBoolean(var3.replaceFirst("bPerfReflections=", ""));
                  this.bPerfReflections_new = this.bPerfReflections;
               } else if (var3.startsWith("bMultithreadedRendering=")) {
                  bMultithreadedRendering = Boolean.parseBoolean(var3.replaceFirst("bMultithreadedRendering=", ""));
               } else if (var3.startsWith("language=")) {
                  OptionLanguageName = var3.replaceFirst("language=", "").trim();
               } else if (var3.startsWith("zoom=")) {
                  OptionZoom = Boolean.parseBoolean(var3.replaceFirst("zoom=", ""));
               } else {
                  String[] var4;
                  if (var3.startsWith("autozoom=")) {
                     var4 = var3.replaceFirst("autozoom=", "").split(",");

                     for(var27 = 0; var27 < var4.length; ++var27) {
                        if (!var4[var27].isEmpty()) {
                           var6 = Integer.parseInt(var4[var27]);
                           if (var6 >= 1 && var6 <= 4) {
                              this.setAutoZoom(var6 - 1, true);
                           }
                        }
                     }
                  } else if (var3.startsWith("fontSize=")) {
                     this.setOptionFontSize(Integer.parseInt(var3.replaceFirst("fontSize=", "").trim()));
                  } else if (var3.startsWith("contextMenuFont=")) {
                     OptionContextMenuFont = var3.replaceFirst("contextMenuFont=", "").trim();
                  } else if (var3.startsWith("inventoryFont=")) {
                     OptionInventoryFont = var3.replaceFirst("inventoryFont=", "").trim();
                  } else if (var3.startsWith("tooltipFont=")) {
                     OptionTooltipFont = var3.replaceFirst("tooltipFont=", "").trim();
                  } else if (var3.startsWith("measurementsFormat=")) {
                     OptionMeasurementFormat = var3.replaceFirst("measurementsFormat=", "").trim();
                  } else if (var3.startsWith("clockFormat=")) {
                     OptionClockFormat = Integer.parseInt(var3.replaceFirst("clockFormat=", ""));
                  } else if (var3.startsWith("clockSize=")) {
                     OptionClockSize = Integer.parseInt(var3.replaceFirst("clockSize=", ""));
                  } else if (var3.startsWith("clock24Hour=")) {
                     OptionClock24Hour = Boolean.parseBoolean(var3.replaceFirst("clock24Hour=", ""));
                  } else if (var3.startsWith("vsync=")) {
                     OptionVSync = Boolean.parseBoolean(var3.replaceFirst("vsync=", ""));
                  } else if (var3.startsWith("voiceEnable=")) {
                     OptionVoiceEnable = Boolean.parseBoolean(var3.replaceFirst("voiceEnable=", ""));
                  } else if (var3.startsWith("voiceMode=")) {
                     OptionVoiceMode = Integer.parseInt(var3.replaceFirst("voiceMode=", ""));
                  } else if (var3.startsWith("voiceVADMode=")) {
                     OptionVoiceVADMode = Integer.parseInt(var3.replaceFirst("voiceVADMode=", ""));
                  } else if (var3.startsWith("voiceVolumeMic=")) {
                     OptionVoiceVolumeMic = Integer.parseInt(var3.replaceFirst("voiceVolumeMic=", ""));
                  } else if (var3.startsWith("voiceVolumePlayers=")) {
                     OptionVoiceVolumePlayers = Integer.parseInt(var3.replaceFirst("voiceVolumePlayers=", ""));
                  } else if (var3.startsWith("voiceRecordDeviceName=")) {
                     OptionVoiceRecordDeviceName = var3.replaceFirst("voiceRecordDeviceName=", "");
                  } else if (var3.startsWith("soundVolume=")) {
                     OptionSoundVolume = Integer.parseInt(var3.replaceFirst("soundVolume=", ""));
                  } else if (var3.startsWith("musicVolume=")) {
                     OptionMusicVolume = Integer.parseInt(var3.replaceFirst("musicVolume=", ""));
                  } else if (var3.startsWith("ambientVolume=")) {
                     OptionAmbientVolume = Integer.parseInt(var3.replaceFirst("ambientVolume=", ""));
                  } else if (var3.startsWith("musicLibrary=")) {
                     OptionMusicLibrary = Integer.parseInt(var3.replaceFirst("musicLibrary=", ""));
                  } else if (var3.startsWith("vehicleEngineVolume=")) {
                     OptionVehicleEngineVolume = Integer.parseInt(var3.replaceFirst("vehicleEngineVolume=", ""));
                  } else if (var3.startsWith("reloadDifficulty=")) {
                     OptionReloadDifficulty = Integer.parseInt(var3.replaceFirst("reloadDifficulty=", ""));
                  } else if (var3.startsWith("rackProgress=")) {
                     OptionRackProgress = Boolean.parseBoolean(var3.replaceFirst("rackProgress=", ""));
                  } else if (var3.startsWith("controller=")) {
                     String var22 = var3.replaceFirst("controller=", "");
                     if (!var22.isEmpty()) {
                        JoypadManager.instance.setControllerActive(var22, true);
                     }
                  } else if (var3.startsWith("tutorialDone=")) {
                     this.tutorialDone = Boolean.parseBoolean(var3.replaceFirst("tutorialDone=", ""));
                  } else if (var3.startsWith("vehiclesWarningShow=")) {
                     this.vehiclesWarningShow = Boolean.parseBoolean(var3.replaceFirst("vehiclesWarningShow=", ""));
                  } else if (var3.startsWith("bloodDecals=")) {
                     this.setOptionBloodDecals(Integer.parseInt(var3.replaceFirst("bloodDecals=", "")));
                  } else if (var3.startsWith("borderless=")) {
                     OptionBorderlessWindow = Boolean.parseBoolean(var3.replaceFirst("borderless=", ""));
                  } else if (var3.startsWith("lockCursorToWindow=")) {
                     OptionLockCursorToWindow = Boolean.parseBoolean(var3.replaceFirst("lockCursorToWindow=", ""));
                  } else if (var3.startsWith("textureCompression=")) {
                     OptionTextureCompression = Boolean.parseBoolean(var3.replaceFirst("textureCompression=", ""));
                  } else if (var3.startsWith("modelTextureMipmaps=")) {
                     OptionModelTextureMipmaps = Boolean.parseBoolean(var3.replaceFirst("modelTextureMipmaps=", ""));
                  } else if (var3.startsWith("texture2x=")) {
                     OptionTexture2x = Boolean.parseBoolean(var3.replaceFirst("texture2x=", ""));
                  } else if (var3.startsWith("zoomLevels1x=")) {
                     OptionZoomLevels1x = var3.replaceFirst("zoomLevels1x=", "");
                  } else if (var3.startsWith("zoomLevels2x=")) {
                     OptionZoomLevels2x = var3.replaceFirst("zoomLevels2x=", "");
                  } else if (var3.startsWith("showChatTimestamp=")) {
                     OptionShowChatTimestamp = Boolean.parseBoolean(var3.replaceFirst("showChatTimestamp=", ""));
                  } else if (var3.startsWith("showChatTitle=")) {
                     OptionShowChatTitle = Boolean.parseBoolean(var3.replaceFirst("showChatTitle=", ""));
                  } else if (var3.startsWith("chatFontSize=")) {
                     OptionChatFontSize = var3.replaceFirst("chatFontSize=", "");
                  } else if (var3.startsWith("minChatOpaque=")) {
                     OptionMinChatOpaque = Float.parseFloat(var3.replaceFirst("minChatOpaque=", ""));
                  } else if (var3.startsWith("maxChatOpaque=")) {
                     OptionMaxChatOpaque = Float.parseFloat(var3.replaceFirst("maxChatOpaque=", ""));
                  } else if (var3.startsWith("chatFadeTime=")) {
                     OptionChatFadeTime = Float.parseFloat(var3.replaceFirst("chatFadeTime=", ""));
                  } else if (var3.startsWith("chatOpaqueOnFocus=")) {
                     OptionChatOpaqueOnFocus = Boolean.parseBoolean(var3.replaceFirst("chatOpaqueOnFocus=", ""));
                  } else if (var3.startsWith("doneNewSaveFolder=")) {
                     this.doneNewSaveFolder = Boolean.parseBoolean(var3.replaceFirst("doneNewSaveFolder=", ""));
                  } else if (var3.startsWith("contentTranslationsEnabled=")) {
                     OptionEnableContentTranslations = Boolean.parseBoolean(var3.replaceFirst("contentTranslationsEnabled=", ""));
                  } else if (var3.startsWith("showYourUsername=")) {
                     this.showYourUsername = Boolean.parseBoolean(var3.replaceFirst("showYourUsername=", ""));
                  } else if (var3.startsWith("riversideDone=")) {
                     this.riversideDone = Boolean.parseBoolean(var3.replaceFirst("riversideDone=", ""));
                  } else if (var3.startsWith("rosewoodSpawnDone=")) {
                     this.rosewoodSpawnDone = Boolean.parseBoolean(var3.replaceFirst("rosewoodSpawnDone=", ""));
                  } else if (var3.startsWith("gotNewBelt=")) {
                     this.gotNewBelt = Boolean.parseBoolean(var3.replaceFirst("gotNewBelt=", ""));
                  } else {
                     float var23;
                     float var26;
                     float var28;
                     if (var3.startsWith("mpTextColor=")) {
                        var4 = var3.replaceFirst("mpTextColor=", "").split(",");
                        var23 = Float.parseFloat(var4[0]);
                        var26 = Float.parseFloat(var4[1]);
                        var28 = Float.parseFloat(var4[2]);
                        if (var23 < 0.19F) {
                           var23 = 0.19F;
                        }

                        if (var26 < 0.19F) {
                           var26 = 0.19F;
                        }

                        if (var28 < 0.19F) {
                           var28 = 0.19F;
                        }

                        this.mpTextColor = new ColorInfo(var23, var26, var28, 1.0F);
                     } else if (var3.startsWith("objHighlightColor=")) {
                        var4 = var3.replaceFirst("objHighlightColor=", "").split(",");
                        var23 = Float.parseFloat(var4[0]);
                        var26 = Float.parseFloat(var4[1]);
                        var28 = Float.parseFloat(var4[2]);
                        if (var23 < 0.19F) {
                           var23 = 0.19F;
                        }

                        if (var26 < 0.19F) {
                           var26 = 0.19F;
                        }

                        if (var28 < 0.19F) {
                           var28 = 0.19F;
                        }

                        this.objectHighlitedColor.set(var23, var26, var28, 1.0F);
                     } else if (var3.startsWith("seenNews=")) {
                        this.setSeenUpdateText(var3.replaceFirst("seenNews=", ""));
                     } else if (var3.startsWith("toggleToAim=")) {
                        this.setToggleToAim(Boolean.parseBoolean(var3.replaceFirst("toggleToAim=", "")));
                     } else if (var3.startsWith("toggleToRun=")) {
                        this.setToggleToRun(Boolean.parseBoolean(var3.replaceFirst("toggleToRun=", "")));
                     } else if (var3.startsWith("toggleToSprint=")) {
                        this.setToggleToSprint(Boolean.parseBoolean(var3.replaceFirst("toggleToSprint=", "")));
                     } else if (var3.startsWith("celsius=")) {
                        this.setCelsius(Boolean.parseBoolean(var3.replaceFirst("celsius=", "")));
                     } else if (!var3.startsWith("mapOrder=")) {
                        if (var3.startsWith("showFirstTimeSneakTutorial=")) {
                           this.setShowFirstTimeSneakTutorial(Boolean.parseBoolean(var3.replaceFirst("showFirstTimeSneakTutorial=", "")));
                        } else if (var3.startsWith("uiRenderOffscreen=")) {
                           OptionUIFBO = Boolean.parseBoolean(var3.replaceFirst("uiRenderOffscreen=", ""));
                        } else if (var3.startsWith("uiRenderFPS=")) {
                           OptionUIRenderFPS = Integer.parseInt(var3.replaceFirst("uiRenderFPS=", ""));
                        } else if (var3.startsWith("radialMenuKeyToggle=")) {
                           OptionRadialMenuKeyToggle = Boolean.parseBoolean(var3.replaceFirst("radialMenuKeyToggle=", ""));
                        } else if (var3.startsWith("reloadRadialInstant=")) {
                           OptionReloadRadialInstant = Boolean.parseBoolean(var3.replaceFirst("reloadRadialInstant=", ""));
                        } else if (var3.startsWith("panCameraWhileAiming=")) {
                           OptionPanCameraWhileAiming = Boolean.parseBoolean(var3.replaceFirst("panCameraWhileAiming=", ""));
                        } else if (var3.startsWith("panCameraWhileDriving=")) {
                           OptionPanCameraWhileDriving = Boolean.parseBoolean(var3.replaceFirst("panCameraWhileDriving=", ""));
                        } else if (var3.startsWith("temperatureDisplayCelsius=")) {
                           OptionTemperatureDisplayCelsius = Boolean.parseBoolean(var3.replaceFirst("temperatureDisplayCelsius=", ""));
                        } else if (var3.startsWith("doWindSpriteEffects=")) {
                           OptionDoWindSpriteEffects = Boolean.parseBoolean(var3.replaceFirst("doWindSpriteEffects=", ""));
                        } else if (var3.startsWith("doDoorSpriteEffects=")) {
                           OptionDoDoorSpriteEffects = Boolean.parseBoolean(var3.replaceFirst("doDoorSpriteEffects=", ""));
                        } else if (var3.startsWith("updateSneakButton2=")) {
                           OptionUpdateSneakButton = true;
                        } else if (var3.startsWith("updateSneakButton=")) {
                           OptionUpdateSneakButton = Boolean.parseBoolean(var3.replaceFirst("updateSneakButton=", ""));
                        } else if (var3.startsWith("dblTapJogToSprint=")) {
                           OptiondblTapJogToSprint = Boolean.parseBoolean(var3.replaceFirst("dblTapJogToSprint=", ""));
                        } else if (var3.startsWith("aimOutline=")) {
                           this.setOptionAimOutline(PZMath.tryParseInt(var3.replaceFirst("aimOutline=", ""), 2));
                        } else if (var3.startsWith("cycleContainerKey=")) {
                           OptionCycleContainerKey = var3.replaceFirst("cycleContainerKey=", "");
                        } else if (var3.startsWith("dropItemsOnSquareCenter=")) {
                           OptionDropItemsOnSquareCenter = Boolean.parseBoolean(var3.replaceFirst("dropItemsOnSquareCenter=", ""));
                        } else if (var3.startsWith("timedActionGameSpeedReset=")) {
                           OptionTimedActionGameSpeedReset = Boolean.parseBoolean(var3.replaceFirst("timedActionGameSpeedReset=", ""));
                        } else if (var3.startsWith("shoulderButtonContainerSwitch=")) {
                           OptionShoulderButtonContainerSwitch = Integer.parseInt(var3.replaceFirst("shoulderButtonContainerSwitch=", ""));
                        } else if (var3.startsWith("singleContextMenu=")) {
                           this.readPerPlayerBoolean(var3.replaceFirst("singleContextMenu=", ""), OptionSingleContextMenu);
                        } else if (var3.startsWith("renderPrecipIndoors=")) {
                           OptionRenderPrecipIndoors = Boolean.parseBoolean(var3.replaceFirst("renderPrecipIndoors=", ""));
                        } else if (var3.startsWith("autoProneAtk=")) {
                           OptionAutoProneAtk = Boolean.parseBoolean(var3.replaceFirst("autoProneAtk=", ""));
                        } else if (var3.startsWith("3DGroundItem=")) {
                           Option3DGroundItem = Boolean.parseBoolean(var3.replaceFirst("3DGroundItem=", ""));
                        } else if (var3.startsWith("tieredZombieUpdates=")) {
                           this.setOptionTieredZombieUpdates(Boolean.parseBoolean(var3.replaceFirst("tieredZombieUpdates=", "")));
                        } else if (var3.startsWith("progressBar=")) {
                           this.setOptionProgressBar(Boolean.parseBoolean(var3.replaceFirst("progressBar=", "")));
                        } else if (var3.startsWith("corpseShadows=")) {
                           OptionCorpseShadows = Boolean.parseBoolean(var3.replaceFirst("corpseShadows=", ""));
                        } else if (var3.startsWith("simpleClothingTextures=")) {
                           this.setOptionSimpleClothingTextures(PZMath.tryParseInt(var3.replaceFirst("simpleClothingTextures=", ""), 1));
                        } else if (var3.startsWith("simpleWeaponTextures=")) {
                           OptionSimpleWeaponTextures = Boolean.parseBoolean(var3.replaceFirst("simpleWeaponTextures=", ""));
                        } else if (var3.startsWith("autoDrink=")) {
                           OptionAutoDrink = Boolean.parseBoolean(var3.replaceFirst("autoDrink=", ""));
                        } else if (var3.startsWith("leaveKeyInIgnition=")) {
                           OptionLeaveKeyInIgnition = Boolean.parseBoolean(var3.replaceFirst("leaveKeyInIgnition=", ""));
                        } else if (var3.startsWith("searchModeOverlayEffect=")) {
                           OptionSearchModeOverlayEffect = Integer.parseInt(var3.replaceFirst("searchModeOverlayEffect=", ""));
                        } else if (var3.startsWith("ignoreProneZombieRange=")) {
                           this.setOptionIgnoreProneZombieRange(PZMath.tryParseInt(var3.replaceFirst("ignoreProneZombieRange=", ""), 1));
                        } else if (var3.startsWith("fogQuality=")) {
                           PerformanceSettings.FogQuality = Integer.parseInt(var3.replaceFirst("fogQuality=", ""));
                        } else if (var3.startsWith("renderPrecipitation=")) {
                           OptionRenderPrecipitation = Integer.parseInt(var3.replaceFirst("renderPrecipitation=", ""));
                        }
                     } else {
                        if (this.version < 7) {
                           var3 = "mapOrder=";
                        }

                        var4 = var3.replaceFirst("mapOrder=", "").split(";");
                        String[] var5 = var4;
                        var6 = var4.length;

                        for(int var7 = 0; var7 < var6; ++var7) {
                           String var8 = var5[var7];
                           var8 = var8.trim();
                           if (!var8.isEmpty()) {
                              ActiveMods.getById("default").getMapOrder().add(var8);
                           }
                        }

                        ZomboidFileSystem.instance.saveModsFile();
                     }
                  }
               }
            }

            if (OptionLanguageName == null) {
               OptionLanguageName = System.getProperty("user.language").toUpperCase();
            }

            if (!this.doneNewSaveFolder) {
               File var24 = new File(ZomboidFileSystem.instance.getSaveDir());
               var24.mkdir();
               ArrayList var29 = new ArrayList();
               var29.add("Beginner");
               var29.add("Survival");
               var29.add("A Really CD DA");
               var29.add("LastStand");
               var29.add("Opening Hours");
               var29.add("Sandbox");
               var29.add("Tutorial");
               var29.add("Winter is Coming");
               var29.add("You Have One Day");
               File var30 = null;
               File var32 = null;

               try {
                  Iterator var31 = var29.iterator();

                  while(var31.hasNext()) {
                     String var9 = (String)var31.next();
                     var10002 = ZomboidFileSystem.instance.getCacheDir();
                     var30 = new File(var10002 + File.separator + var9);
                     var10002 = ZomboidFileSystem.instance.getSaveDir();
                     var32 = new File(var10002 + File.separator + var9);
                     if (var30.exists()) {
                        var32.mkdir();
                        Files.move(var30.toPath(), var32.toPath(), StandardCopyOption.REPLACE_EXISTING);
                     }
                  }
               } catch (Exception var16) {
               }

               this.doneNewSaveFolder = true;
            }
         } catch (Exception var17) {
            var17.printStackTrace();
         } finally {
            var19.close();
         }

         this.saveOptions();
         return true;
      }
   }

   public boolean isDedicated() {
      return GameServer.bServer;
   }

   private void copyPasteFolders(String var1) {
      File var2 = (new File(var1)).getAbsoluteFile();
      if (var2.exists()) {
         this.searchFolders(var2, var1);
      }

   }

   private void searchFolders(File var1, String var2) {
      if (var1.isDirectory()) {
         File var3 = new File(this.saveFolder + File.separator + var2);
         var3.mkdir();
         String[] var4 = var1.list();

         for(int var5 = 0; var5 < var4.length; ++var5) {
            String var10003 = var1.getAbsolutePath();
            this.searchFolders(new File(var10003 + File.separator + var4[var5]), var2 + File.separator + var4[var5]);
         }
      } else {
         this.copyPasteFile(var1, var2);
      }

   }

   private void copyPasteFile(File var1, String var2) {
      FileOutputStream var3 = null;
      FileInputStream var4 = null;

      try {
         File var5 = new File(this.saveFolder + File.separator + var2);
         var5.createNewFile();
         var3 = new FileOutputStream(var5);
         var4 = new FileInputStream(var1);
         var3.getChannel().transferFrom(var4.getChannel(), 0L, var1.length());
      } catch (Exception var14) {
         var14.printStackTrace();
      } finally {
         try {
            if (var4 != null) {
               var4.close();
            }

            if (var3 != null) {
               var3.close();
            }
         } catch (IOException var13) {
            var13.printStackTrace();
         }

      }

   }

   public static String getMyDocumentFolder() {
      return ZomboidFileSystem.instance.getCacheDir();
   }

   public void saveOptions() throws IOException {
      String var10002 = ZomboidFileSystem.instance.getCacheDir();
      File var1 = new File(var10002 + File.separator + "options.ini");
      if (!var1.exists()) {
         var1.createNewFile();
      }

      FileWriter var2 = new FileWriter(var1);

      try {
         var2.write("version=" + this.fileversion + "\r\n");
         var2.write("width=" + this.getScreenWidth() + "\r\n");
         var2.write("height=" + this.getScreenHeight() + "\r\n");
         var2.write("fullScreen=" + fullScreen + "\r\n");
         var2.write("frameRate=" + PerformanceSettings.getLockFPS() + "\r\n");
         var2.write("uncappedFPS=" + PerformanceSettings.isUncappedFPS() + "\r\n");
         var2.write("iso_cursor=" + getInstance().getIsoCursorVisibility() + "\r\n");
         var2.write("showCursorWhileAiming=" + OptionShowCursorWhileAiming + "\r\n");
         var2.write("water=" + PerformanceSettings.WaterQuality + "\r\n");
         var2.write("puddles=" + PerformanceSettings.PuddlesQuality + "\r\n");
         var2.write("lighting=" + PerformanceSettings.LightingFrameSkip + "\r\n");
         var2.write("lightFPS=" + PerformanceSettings.LightingFPS + "\r\n");
         var2.write("perfSkybox=" + this.iPerfSkybox_new + "\r\n");
         var2.write("perfPuddles=" + this.iPerfPuddles_new + "\r\n");
         var2.write("bPerfReflections=" + this.bPerfReflections_new + "\r\n");
         var2.write("vidMem=" + this.vidMem + "\r\n");
         var2.write("bMultithreadedRendering=" + bMultithreadedRendering + "\r\n");
         var2.write("language=" + this.getOptionLanguageName() + "\r\n");
         var2.write("zoom=" + OptionZoom + "\r\n");
         var2.write("fontSize=" + OptionFontSize + "\r\n");
         var2.write("contextMenuFont=" + OptionContextMenuFont + "\r\n");
         var2.write("inventoryFont=" + OptionInventoryFont + "\r\n");
         var2.write("tooltipFont=" + OptionTooltipFont + "\r\n");
         var2.write("clockFormat=" + OptionClockFormat + "\r\n");
         var2.write("clockSize=" + OptionClockSize + "\r\n");
         var2.write("clock24Hour=" + OptionClock24Hour + "\r\n");
         var2.write("measurementsFormat=" + OptionMeasurementFormat + "\r\n");
         String var3 = "";

         for(int var4 = 0; var4 < 4; ++var4) {
            if (bAutoZoom[var4]) {
               if (!var3.isEmpty()) {
                  var3 = var3 + ",";
               }

               var3 = var3 + (var4 + 1);
            }
         }

         var2.write("autozoom=" + var3 + "\r\n");
         var2.write("vsync=" + OptionVSync + "\r\n");
         var2.write("soundVolume=" + OptionSoundVolume + "\r\n");
         var2.write("ambientVolume=" + OptionAmbientVolume + "\r\n");
         var2.write("musicVolume=" + OptionMusicVolume + "\r\n");
         var2.write("musicLibrary=" + OptionMusicLibrary + "\r\n");
         var2.write("vehicleEngineVolume=" + OptionVehicleEngineVolume + "\r\n");
         var2.write("voiceEnable=" + OptionVoiceEnable + "\r\n");
         var2.write("voiceMode=" + OptionVoiceMode + "\r\n");
         var2.write("voiceVADMode=" + OptionVoiceVADMode + "\r\n");
         var2.write("voiceVolumeMic=" + OptionVoiceVolumeMic + "\r\n");
         var2.write("voiceVolumePlayers=" + OptionVoiceVolumePlayers + "\r\n");
         var2.write("voiceRecordDeviceName=" + OptionVoiceRecordDeviceName + "\r\n");
         var2.write("reloadDifficulty=" + OptionReloadDifficulty + "\r\n");
         var2.write("rackProgress=" + OptionRackProgress + "\r\n");
         Iterator var11 = JoypadManager.instance.ActiveControllerGUIDs.iterator();

         while(var11.hasNext()) {
            String var5 = (String)var11.next();
            var2.write("controller=" + var5 + "\r\n");
         }

         var2.write("tutorialDone=" + this.isTutorialDone() + "\r\n");
         var2.write("vehiclesWarningShow=" + this.isVehiclesWarningShow() + "\r\n");
         var2.write("bloodDecals=" + OptionBloodDecals + "\r\n");
         var2.write("borderless=" + OptionBorderlessWindow + "\r\n");
         var2.write("lockCursorToWindow=" + OptionLockCursorToWindow + "\r\n");
         var2.write("textureCompression=" + OptionTextureCompression + "\r\n");
         var2.write("modelTextureMipmaps=" + OptionModelTextureMipmaps + "\r\n");
         var2.write("texture2x=" + OptionTexture2x + "\r\n");
         var2.write("zoomLevels1x=" + OptionZoomLevels1x + "\r\n");
         var2.write("zoomLevels2x=" + OptionZoomLevels2x + "\r\n");
         var2.write("showChatTimestamp=" + OptionShowChatTimestamp + "\r\n");
         var2.write("showChatTitle=" + OptionShowChatTitle + "\r\n");
         var2.write("chatFontSize=" + OptionChatFontSize + "\r\n");
         var2.write("minChatOpaque=" + OptionMinChatOpaque + "\r\n");
         var2.write("maxChatOpaque=" + OptionMaxChatOpaque + "\r\n");
         var2.write("chatFadeTime=" + OptionChatFadeTime + "\r\n");
         var2.write("chatOpaqueOnFocus=" + OptionChatOpaqueOnFocus + "\r\n");
         var2.write("doneNewSaveFolder=" + this.doneNewSaveFolder + "\r\n");
         var2.write("contentTranslationsEnabled=" + OptionEnableContentTranslations + "\r\n");
         var2.write("showYourUsername=" + this.showYourUsername + "\r\n");
         var2.write("rosewoodSpawnDone=" + this.rosewoodSpawnDone + "\r\n");
         if (this.mpTextColor != null) {
            var2.write("mpTextColor=" + this.mpTextColor.r + "," + this.mpTextColor.g + "," + this.mpTextColor.b + "\r\n");
         }

         var2.write("objHighlightColor=" + this.objectHighlitedColor.r + "," + this.objectHighlitedColor.g + "," + this.objectHighlitedColor.b + "\r\n");
         var2.write("seenNews=" + this.getSeenUpdateText() + "\r\n");
         var2.write("toggleToAim=" + this.isToggleToAim() + "\r\n");
         var2.write("toggleToRun=" + this.isToggleToRun() + "\r\n");
         var2.write("toggleToSprint=" + this.isToggleToSprint() + "\r\n");
         var2.write("celsius=" + this.isCelsius() + "\r\n");
         var2.write("riversideDone=" + this.isRiversideDone() + "\r\n");
         var2.write("showFirstTimeSneakTutorial=" + this.isShowFirstTimeSneakTutorial() + "\r\n");
         var2.write("uiRenderOffscreen=" + OptionUIFBO + "\r\n");
         var2.write("uiRenderFPS=" + OptionUIRenderFPS + "\r\n");
         var2.write("radialMenuKeyToggle=" + OptionRadialMenuKeyToggle + "\r\n");
         var2.write("reloadRadialInstant=" + OptionReloadRadialInstant + "\r\n");
         var2.write("panCameraWhileAiming=" + OptionPanCameraWhileAiming + "\r\n");
         var2.write("panCameraWhileDriving=" + OptionPanCameraWhileDriving + "\r\n");
         var2.write("temperatureDisplayCelsius=" + OptionTemperatureDisplayCelsius + "\r\n");
         var2.write("doWindSpriteEffects=" + OptionDoWindSpriteEffects + "\r\n");
         var2.write("doDoorSpriteEffects=" + OptionDoDoorSpriteEffects + "\r\n");
         var2.write("updateSneakButton=" + OptionUpdateSneakButton + "\r\n");
         var2.write("dblTapJogToSprint=" + OptiondblTapJogToSprint + "\r\n");
         var2.write("gotNewBelt=" + this.gotNewBelt + "\r\n");
         var2.write("aimOutline=" + OptionAimOutline + "\r\n");
         var2.write("cycleContainerKey=" + OptionCycleContainerKey + "\r\n");
         var2.write("dropItemsOnSquareCenter=" + OptionDropItemsOnSquareCenter + "\r\n");
         var2.write("timedActionGameSpeedReset=" + OptionTimedActionGameSpeedReset + "\r\n");
         var2.write("shoulderButtonContainerSwitch=" + OptionShoulderButtonContainerSwitch + "\r\n");
         var2.write("singleContextMenu=" + this.getPerPlayerBooleanString(OptionSingleContextMenu) + "\r\n");
         var2.write("renderPrecipIndoors=" + OptionRenderPrecipIndoors + "\r\n");
         var2.write("autoProneAtk=" + OptionAutoProneAtk + "\r\n");
         var2.write("3DGroundItem=" + Option3DGroundItem + "\r\n");
         var2.write("tieredZombieUpdates=" + this.getOptionTieredZombieUpdates() + "\r\n");
         var2.write("progressBar=" + this.isOptionProgressBar() + "\r\n");
         var2.write("corpseShadows=" + this.getOptionCorpseShadows() + "\r\n");
         var2.write("simpleClothingTextures=" + this.getOptionSimpleClothingTextures() + "\r\n");
         var2.write("simpleWeaponTextures=" + this.getOptionSimpleWeaponTextures() + "\r\n");
         var2.write("autoDrink=" + this.getOptionAutoDrink() + "\r\n");
         var2.write("leaveKeyInIgnition=" + this.getOptionLeaveKeyInIgnition() + "\r\n");
         var2.write("searchModeOverlayEffect=" + this.getOptionSearchModeOverlayEffect() + "\r\n");
         var2.write("ignoreProneZombieRange=" + this.getOptionIgnoreProneZombieRange() + "\r\n");
         var2.write("fogQuality=" + PerformanceSettings.FogQuality + "\r\n");
         var2.write("renderPrecipitation=" + OptionRenderPrecipitation + "\r\n");
      } catch (Exception var9) {
         var9.printStackTrace();
      } finally {
         var2.close();
      }

   }

   public void setWindowed(boolean var1) {
      RenderThread.invokeOnRenderContext(() -> {
         if (var1 != fullScreen) {
            setDisplayMode(this.getScreenWidth(), this.getScreenHeight(), var1);
         }

         fullScreen = var1;
         if (fakefullscreen) {
            Display.setResizable(false);
         } else {
            Display.setResizable(!var1);
         }

         try {
            this.saveOptions();
         } catch (IOException var3) {
            var3.printStackTrace();
         }

      });
   }

   public boolean isFullScreen() {
      return fullScreen;
   }

   public KahluaTable getScreenModes() {
      ArrayList var1 = new ArrayList();
      KahluaTable var2 = LuaManager.platform.newTable();
      String var10002 = LuaManager.getLuaCacheDir();
      File var3 = new File(var10002 + File.separator + "screenresolution.ini");
      int var4 = 1;

      try {
         Integer var6;
         if (!var3.exists()) {
            var3.createNewFile();
            FileWriter var5 = new FileWriter(var3);
            var6 = 0;
            Integer var7 = 0;
            DisplayMode[] var8 = Display.getAvailableDisplayModes();

            for(int var9 = 0; var9 < var8.length; ++var9) {
               var6 = var8[var9].getWidth();
               var7 = var8[var9].getHeight();
               if (!var1.contains(var6 + " x " + var7)) {
                  var2.rawset(var4, var6 + " x " + var7);
                  var5.write(var6 + " x " + var7 + " \r\n");
                  var1.add(var6 + " x " + var7);
                  ++var4;
               }
            }

            var5.close();
         } else {
            BufferedReader var11 = new BufferedReader(new FileReader(var3));

            String var12;
            for(var6 = null; (var12 = var11.readLine()) != null; ++var4) {
               var2.rawset(var4, var12.trim());
            }

            var11.close();
         }
      } catch (Exception var10) {
         var10.printStackTrace();
      }

      return var2;
   }

   public static void setDisplayMode(int var0, int var1, boolean var2) {
      RenderThread.invokeOnRenderContext(() -> {
         if (Display.getWidth() != var0 || Display.getHeight() != var1 || Display.isFullscreen() != var2 || Display.isBorderlessWindow() != OptionBorderlessWindow) {
            fullScreen = var2;

            try {
               DisplayMode var3 = null;
               if (!var2) {
                  if (OptionBorderlessWindow) {
                     if (Display.getWindow() != 0L && Display.isFullscreen()) {
                        Display.setFullscreen(false);
                     }

                     long var12 = GLFW.glfwGetPrimaryMonitor();
                     GLFWVidMode var13 = GLFW.glfwGetVideoMode(var12);
                     var3 = new DisplayMode(var13.width(), var13.height());
                  } else {
                     var3 = new DisplayMode(var0, var1);
                  }
               } else {
                  DisplayMode[] var4 = Display.getAvailableDisplayModes();
                  int var5 = 0;
                  DisplayMode var6 = null;
                  DisplayMode[] var7 = var4;
                  int var8 = var4.length;

                  for(int var9 = 0; var9 < var8; ++var9) {
                     DisplayMode var10 = var7[var9];
                     if (var10.getWidth() == var0 && var10.getHeight() == var1 && var10.isFullscreenCapable()) {
                        if ((var3 == null || var10.getFrequency() >= var5) && (var3 == null || var10.getBitsPerPixel() > var3.getBitsPerPixel())) {
                           var3 = var10;
                           var5 = var10.getFrequency();
                        }

                        if (var10.getBitsPerPixel() == Display.getDesktopDisplayMode().getBitsPerPixel() && var10.getFrequency() == Display.getDesktopDisplayMode().getFrequency()) {
                           var3 = var10;
                           break;
                        }
                     }

                     if (var10.isFullscreenCapable() && (var6 == null || Math.abs(var10.getWidth() - var0) < Math.abs(var6.getWidth() - var0) || var10.getWidth() == var6.getWidth() && var10.getFrequency() > var5)) {
                        var6 = var10;
                        var5 = var10.getFrequency();
                        PrintStream var10000 = System.out;
                        int var10001 = var10.getWidth();
                        var10000.println("closest width=" + var10001 + " freq=" + var10.getFrequency());
                     }
                  }

                  if (var3 == null && var6 != null) {
                     var3 = var6;
                  }
               }

               if (var3 == null) {
                  DebugLog.log("Failed to find value mode: " + var0 + "x" + var1 + " fs=" + var2);
                  return;
               }

               Display.setBorderlessWindow(OptionBorderlessWindow);
               if (var2) {
                  Display.setDisplayModeAndFullscreen(var3);
               } else {
                  Display.setDisplayMode(var3);
                  Display.setFullscreen(false);
               }

               if (!var2 && OptionBorderlessWindow) {
                  Display.setResizable(false);
               } else if (!var2 && !fakefullscreen) {
                  Display.setResizable(false);
                  Display.setResizable(true);
               }

               if (Display.isCreated()) {
                  int var14 = Display.getWidth();
                  DebugLog.log("Display mode changed to " + var14 + "x" + Display.getHeight() + " freq=" + Display.getDisplayMode().getFrequency() + " fullScreen=" + Display.isFullscreen());
               }
            } catch (LWJGLException var11) {
               DebugLog.log("Unable to setup mode " + var0 + "x" + var1 + " fullscreen=" + var2 + var11);
            }

         }
      });
   }

   private boolean isFunctionKey(int var1) {
      return var1 >= 59 && var1 <= 68 || var1 >= 87 && var1 <= 105 || var1 == 113;
   }

   public boolean isDoingTextEntry() {
      if (CurrentTextEntryBox == null) {
         return false;
      } else if (!CurrentTextEntryBox.IsEditable) {
         return false;
      } else {
         return CurrentTextEntryBox.DoingTextEntry;
      }
   }

   private void updateKeyboardAux(UITextBox2 var1, int var2) {
      boolean var3 = Keyboard.isKeyDown(29) || Keyboard.isKeyDown(157);
      boolean var4 = Keyboard.isKeyDown(42) || Keyboard.isKeyDown(54);
      int var7;
      if (var2 != 28 && var2 != 156) {
         if (var2 == 1) {
            var1.onOtherKey(1);
            GameKeyboard.eatKeyPress(1);
         } else if (var2 == 15) {
            var1.onOtherKey(15);
            LuaEventManager.triggerEvent("SwitchChatStream");
         } else if (var2 != 58) {
            if (var2 == 199) {
               var1.TextEntryCursorPos = 0;
               if (!var1.Lines.isEmpty()) {
                  var1.TextEntryCursorPos = var1.TextOffsetOfLineStart.get(var1.CursorLine);
               }

               if (!var4) {
                  var1.ToSelectionIndex = var1.TextEntryCursorPos;
               }

               var1.resetBlink();
            } else if (var2 == 207) {
               var1.TextEntryCursorPos = var1.internalText.length();
               if (!var1.Lines.isEmpty()) {
                  var1.TextEntryCursorPos = var1.TextOffsetOfLineStart.get(var1.CursorLine) + ((String)var1.Lines.get(var1.CursorLine)).length();
               }

               if (!var4) {
                  var1.ToSelectionIndex = var1.TextEntryCursorPos;
               }

               var1.resetBlink();
            } else {
               int var11;
               if (var2 == 200) {
                  if (var1.CursorLine > 0) {
                     var11 = var1.TextEntryCursorPos - var1.TextOffsetOfLineStart.get(var1.CursorLine);
                     --var1.CursorLine;
                     if (var11 > ((String)var1.Lines.get(var1.CursorLine)).length()) {
                        var11 = ((String)var1.Lines.get(var1.CursorLine)).length();
                     }

                     var1.TextEntryCursorPos = var1.TextOffsetOfLineStart.get(var1.CursorLine) + var11;
                     if (!var4) {
                        var1.ToSelectionIndex = var1.TextEntryCursorPos;
                     }
                  }

                  var1.onPressUp();
               } else if (var2 == 208) {
                  if (var1.Lines.size() - 1 > var1.CursorLine && var1.CursorLine + 1 < var1.getMaxLines()) {
                     var11 = var1.TextEntryCursorPos - var1.TextOffsetOfLineStart.get(var1.CursorLine);
                     ++var1.CursorLine;
                     if (var11 > ((String)var1.Lines.get(var1.CursorLine)).length()) {
                        var11 = ((String)var1.Lines.get(var1.CursorLine)).length();
                     }

                     var1.TextEntryCursorPos = var1.TextOffsetOfLineStart.get(var1.CursorLine) + var11;
                     if (!var4) {
                        var1.ToSelectionIndex = var1.TextEntryCursorPos;
                     }
                  }

                  var1.onPressDown();
               } else if (var2 != 29) {
                  if (var2 != 157) {
                     if (var2 != 42) {
                        if (var2 != 54) {
                           if (var2 != 56) {
                              if (var2 != 184) {
                                 if (var2 == 203) {
                                    --var1.TextEntryCursorPos;
                                    if (var1.TextEntryCursorPos < 0) {
                                       var1.TextEntryCursorPos = 0;
                                    }

                                    if (!var4) {
                                       var1.ToSelectionIndex = var1.TextEntryCursorPos;
                                    }

                                    var1.resetBlink();
                                 } else if (var2 == 205) {
                                    ++var1.TextEntryCursorPos;
                                    if (var1.TextEntryCursorPos > var1.internalText.length()) {
                                       var1.TextEntryCursorPos = var1.internalText.length();
                                    }

                                    if (!var4) {
                                       var1.ToSelectionIndex = var1.TextEntryCursorPos;
                                    }

                                    var1.resetBlink();
                                 } else if (!this.isFunctionKey(var2)) {
                                    int var6;
                                    String var10001;
                                    if ((var2 == 211 || var2 == 14) && var1.TextEntryCursorPos != var1.ToSelectionIndex) {
                                       var11 = Math.min(var1.TextEntryCursorPos, var1.ToSelectionIndex);
                                       var6 = Math.max(var1.TextEntryCursorPos, var1.ToSelectionIndex);
                                       var10001 = var1.internalText.substring(0, var11);
                                       var1.internalText = var10001 + var1.internalText.substring(var6);
                                       var1.CursorLine = var1.toDisplayLine(var11);
                                       var1.ToSelectionIndex = var11;
                                       var1.TextEntryCursorPos = var11;
                                       var1.onTextChange();
                                    } else if (var2 == 211) {
                                       if (var1.internalText.length() != 0 && var1.TextEntryCursorPos < var1.internalText.length()) {
                                          if (var1.TextEntryCursorPos > 0) {
                                             var10001 = var1.internalText.substring(0, var1.TextEntryCursorPos);
                                             var1.internalText = var10001 + var1.internalText.substring(var1.TextEntryCursorPos + 1);
                                          } else {
                                             var1.internalText = var1.internalText.substring(1);
                                          }

                                          var1.onTextChange();
                                       }
                                    } else if (var2 == 14) {
                                       if (var1.internalText.length() != 0 && var1.TextEntryCursorPos > 0) {
                                          if (var1.TextEntryCursorPos > var1.internalText.length()) {
                                             var1.internalText = var1.internalText.substring(0, var1.internalText.length() - 1);
                                          } else {
                                             var11 = var1.TextEntryCursorPos;
                                             var10001 = var1.internalText.substring(0, var11 - 1);
                                             var1.internalText = var10001 + var1.internalText.substring(var11);
                                          }

                                          --var1.TextEntryCursorPos;
                                          var1.ToSelectionIndex = var1.TextEntryCursorPos;
                                          var1.onTextChange();
                                       }
                                    } else if (var3 && var2 == 47) {
                                       String var12 = Clipboard.getClipboard();
                                       if (var12 != null) {
                                          if (var1.TextEntryCursorPos != var1.ToSelectionIndex) {
                                             var6 = Math.min(var1.TextEntryCursorPos, var1.ToSelectionIndex);
                                             var7 = Math.max(var1.TextEntryCursorPos, var1.ToSelectionIndex);
                                             var1.internalText = var1.internalText.substring(0, var6) + var12 + var1.internalText.substring(var7);
                                             var1.ToSelectionIndex = var6 + var12.length();
                                             var1.TextEntryCursorPos = var6 + var12.length();
                                          } else {
                                             if (var1.TextEntryCursorPos < var1.internalText.length()) {
                                                var1.internalText = var1.internalText.substring(0, var1.TextEntryCursorPos) + var12 + var1.internalText.substring(var1.TextEntryCursorPos);
                                             } else {
                                                var1.internalText = var1.internalText + var12;
                                             }

                                             var1.TextEntryCursorPos += var12.length();
                                             var1.ToSelectionIndex += var12.length();
                                          }

                                          var1.onTextChange();
                                       }
                                    } else {
                                       String var13;
                                       if (var3 && var2 == 46) {
                                          if (var1.TextEntryCursorPos != var1.ToSelectionIndex) {
                                             var1.updateText();
                                             var11 = Math.min(var1.TextEntryCursorPos, var1.ToSelectionIndex);
                                             var6 = Math.max(var1.TextEntryCursorPos, var1.ToSelectionIndex);
                                             var13 = var1.Text.substring(var11, var6);
                                             if (var13 != null && var13.length() > 0) {
                                                Clipboard.setClipboard(var13);
                                             }

                                          }
                                       } else if (var3 && var2 == 45) {
                                          if (var1.TextEntryCursorPos != var1.ToSelectionIndex) {
                                             var1.updateText();
                                             var11 = Math.min(var1.TextEntryCursorPos, var1.ToSelectionIndex);
                                             var6 = Math.max(var1.TextEntryCursorPos, var1.ToSelectionIndex);
                                             var13 = var1.Text.substring(var11, var6);
                                             if (var13 != null && var13.length() > 0) {
                                                Clipboard.setClipboard(var13);
                                             }

                                             var10001 = var1.internalText.substring(0, var11);
                                             var1.internalText = var10001 + var1.internalText.substring(var6);
                                             var1.ToSelectionIndex = var11;
                                             var1.TextEntryCursorPos = var11;
                                          }
                                       } else if (var3 && var2 == 30) {
                                          var1.selectAll();
                                       } else if (!var1.ignoreFirst) {
                                          if (var1.internalText.length() < var1.TextEntryMaxLength) {
                                             char var10 = Keyboard.getEventCharacter();
                                             if (var10 != 0) {
                                                if (var1.isOnlyNumbers() && var10 != '.' && var10 != '-') {
                                                   try {
                                                      Double.parseDouble(String.valueOf(var10));
                                                   } catch (Exception var9) {
                                                      return;
                                                   }
                                                }

                                                if (var1.TextEntryCursorPos == var1.ToSelectionIndex) {
                                                   var6 = var1.TextEntryCursorPos;
                                                   if (var6 < var1.internalText.length()) {
                                                      var1.internalText = var1.internalText.substring(0, var6) + var10 + var1.internalText.substring(var6);
                                                   } else {
                                                      var1.internalText = var1.internalText + var10;
                                                   }

                                                   ++var1.TextEntryCursorPos;
                                                   ++var1.ToSelectionIndex;
                                                   var1.onTextChange();
                                                } else {
                                                   var6 = Math.min(var1.TextEntryCursorPos, var1.ToSelectionIndex);
                                                   var7 = Math.max(var1.TextEntryCursorPos, var1.ToSelectionIndex);
                                                   if (var1.internalText.length() > 0) {
                                                      var1.internalText = var1.internalText.substring(0, var6) + var10 + var1.internalText.substring(var7);
                                                   } else {
                                                      var1.internalText = var10.makeConcatWithConstants<invokedynamic>(var10);
                                                   }

                                                   var1.ToSelectionIndex = var6 + 1;
                                                   var1.TextEntryCursorPos = var6 + 1;
                                                   var1.onTextChange();
                                                }

                                             }
                                          }
                                       }
                                    }
                                 }
                              }
                           }
                        }
                     }
                  }
               }
            }
         }
      } else {
         boolean var5 = false;
         if (UIManager.getDebugConsole() != null && var1 == UIManager.getDebugConsole().CommandLine) {
            var5 = true;
         }

         if (var1.multipleLine) {
            if (var1.Lines.size() < var1.getMaxLines()) {
               if (var1.TextEntryCursorPos != var1.ToSelectionIndex) {
                  var7 = Math.min(var1.TextEntryCursorPos, var1.ToSelectionIndex);
                  int var8 = Math.max(var1.TextEntryCursorPos, var1.ToSelectionIndex);
                  if (var1.internalText.length() > 0) {
                     var1.internalText = var1.internalText.substring(0, var7) + "\n" + var1.internalText.substring(var8);
                  } else {
                     var1.internalText = "\n";
                  }

                  var1.TextEntryCursorPos = var7 + 1;
               } else {
                  var7 = var1.TextEntryCursorPos;
                  String var14 = var1.internalText.substring(0, var7) + "\n" + var1.internalText.substring(var7);
                  var1.SetText(var14);
                  var1.TextEntryCursorPos = var7 + 1;
               }

               var1.ToSelectionIndex = var1.TextEntryCursorPos;
               var1.CursorLine = var1.toDisplayLine(var1.TextEntryCursorPos);
            }
         } else {
            var1.onCommandEntered();
         }

         if (var5 && (!GameClient.bClient || !GameClient.accessLevel.equals("") || GameClient.connection != null && GameClient.connection.isCoopHost)) {
            UIManager.getDebugConsole().ProcessCommand();
         }

      }
   }

   public void updateKeyboard() {
      if (this.isDoingTextEntry()) {
         while(Keyboard.next()) {
            if (this.isDoingTextEntry() && Keyboard.getEventKeyState()) {
               int var1 = Keyboard.getEventKey();
               this.updateKeyboardAux(CurrentTextEntryBox, var1);
            }
         }

         if (CurrentTextEntryBox != null && CurrentTextEntryBox.ignoreFirst) {
            CurrentTextEntryBox.ignoreFirst = false;
         }

      }
   }

   public void quit() {
      DebugLog.log("EXITDEBUG: Core.quit 1");
      if (IsoPlayer.getInstance() != null) {
         DebugLog.log("EXITDEBUG: Core.quit 2");
         bExiting = true;
      } else {
         DebugLog.log("EXITDEBUG: Core.quit 3");

         try {
            this.saveOptions();
         } catch (IOException var2) {
            var2.printStackTrace();
         }

         GameClient.instance.Shutdown();
         SteamUtils.shutdown();
         DebugLog.log("EXITDEBUG: Core.quit 4");
         System.exit(0);
      }

   }

   public void exitToMenu() {
      DebugLog.log("EXITDEBUG: Core.exitToMenu");
      bExiting = true;
   }

   public void quitToDesktop() {
      DebugLog.log("EXITDEBUG: Core.quitToDesktop");
      GameWindow.closeRequested = true;
   }

   public boolean supportRes(int var1, int var2) throws LWJGLException {
      DisplayMode[] var3 = Display.getAvailableDisplayModes();
      boolean var4 = false;

      for(int var5 = 0; var5 < var3.length; ++var5) {
         if (var3[var5].getWidth() == var1 && var3[var5].getHeight() == var2 && var3[var5].isFullscreenCapable()) {
            return true;
         }
      }

      return false;
   }

   public void init(int var1, int var2) throws LWJGLException {
      System.setProperty("org.lwjgl.opengl.Window.undecorated", OptionBorderlessWindow ? "true" : "false");
      if (!System.getProperty("os.name").contains("OS X") && !System.getProperty("os.name").startsWith("Win")) {
         DebugLog.log("Creating display. If this fails, you may need to install xrandr.");
      }

      setDisplayMode(var1, var2, fullScreen);

      try {
         Display.create(new PixelFormat(32, 0, 24, 8, 0));
      } catch (LWJGLException var4) {
         Display.destroy();
         Display.setDisplayModeAndFullscreen(Display.getDesktopDisplayMode());
         Display.create(new PixelFormat(32, 0, 24, 8, 0));
      }

      fullScreen = Display.isFullscreen();
      String var10000 = GL11.glGetString(7936);
      DebugLog.log("GraphicsCard: " + var10000 + " " + GL11.glGetString(7937));
      var10000 = GL11.glGetString(7938);
      DebugLog.log("OpenGL version: " + var10000);
      int var5 = Display.getDesktopDisplayMode().getWidth();
      DebugLog.log("Desktop resolution " + var5 + "x" + Display.getDesktopDisplayMode().getHeight());
      var5 = width;
      DebugLog.log("Initial resolution " + var5 + "x" + height + " fullScreen=" + fullScreen);
      GLVertexBufferObject.init();
      DebugLog.General.println("VSync: %s", OptionVSync ? "ON" : "OFF");
      Display.setVSyncEnabled(OptionVSync);
      GL11.glEnable(3553);
      IndieGL.glBlendFunc(770, 771);
      GL32.glClearColor(0.0F, 0.0F, 0.0F, 1.0F);
   }

   private boolean setupMultiFBO() {
      try {
         if (!this.OffscreenBuffer.test()) {
            return false;
         } else {
            this.OffscreenBuffer.setZoomLevelsFromOption(TileScale == 2 ? OptionZoomLevels2x : OptionZoomLevels1x);
            this.OffscreenBuffer.create(Display.getWidth(), Display.getHeight());
            return true;
         }
      } catch (Exception var2) {
         var2.printStackTrace();
         return false;
      }
   }

   public void setScreenSize(int var1, int var2) {
      if (width != var1 || var2 != height) {
         int var3 = width;
         int var4 = height;
         DebugLog.log("Screen resolution changed from " + var3 + "x" + var4 + " to " + var1 + "x" + var2 + " fullScreen=" + fullScreen);
         width = var1;
         height = var2;
         if (this.OffscreenBuffer != null && this.OffscreenBuffer.Current != null) {
            this.OffscreenBuffer.destroy();

            try {
               this.OffscreenBuffer.setZoomLevelsFromOption(TileScale == 2 ? OptionZoomLevels2x : OptionZoomLevels1x);
               this.OffscreenBuffer.create(var1, var2);
            } catch (Exception var8) {
               var8.printStackTrace();
            }
         }

         try {
            LuaEventManager.triggerEvent("OnResolutionChange", var3, var4, var1, var2);
         } catch (Exception var7) {
            var7.printStackTrace();
         }

         for(int var5 = 0; var5 < IsoPlayer.numPlayers; ++var5) {
            IsoPlayer var6 = IsoPlayer.players[var5];
            if (var6 != null) {
               var6.dirtyRecalcGridStackTime = 2.0F;
            }
         }
      }

   }

   public static boolean supportCompressedTextures() {
      return GL.getCapabilities().GL_EXT_texture_compression_latc;
   }

   public void StartFrame() {
      if (LuaManager.thread == null || !LuaManager.thread.bStep) {
         if (this.RenderShader != null && this.OffscreenBuffer.Current != null) {
            this.RenderShader.setTexture(this.OffscreenBuffer.getTexture(0));
         }

         SpriteRenderer.instance.prePopulating();
         UIManager.resize();
         boolean var1 = false;
         Texture.BindCount = 0;
         if (!var1) {
            IndieGL.glClear(18176);
            if (DebugOptions.instance.Terrain.RenderTiles.HighContrastBg.getValue()) {
               SpriteRenderer.instance.glClearColor(255, 0, 255, 255);
               SpriteRenderer.instance.glClear(16384);
            }
         }

         if (this.OffscreenBuffer.Current != null) {
            SpriteRenderer.instance.glBuffer(1, 0);
         }

         IndieGL.glDoStartFrame(this.getScreenWidth(), this.getScreenWidth(), this.getCurrentPlayerZoom(), 0);
         this.frameStage = 1;
      }
   }

   public void StartFrame(int var1, boolean var2) {
      if (!LuaManager.thread.bStep) {
         this.OffscreenBuffer.update();
         if (this.RenderShader != null && this.OffscreenBuffer.Current != null) {
            this.RenderShader.setTexture(this.OffscreenBuffer.getTexture(var1));
         }

         if (var2) {
            SpriteRenderer.instance.prePopulating();
         }

         if (!var2) {
            SpriteRenderer.instance.initFromIsoCamera(var1);
         }

         Texture.BindCount = 0;
         IndieGL.glLoadIdentity();
         if (this.OffscreenBuffer.Current != null) {
            SpriteRenderer.instance.glBuffer(1, var1);
         }

         IndieGL.glDoStartFrame(this.getScreenWidth(), this.getScreenHeight(), this.getZoom(var1), var1);
         IndieGL.glClear(17664);
         if (DebugOptions.instance.Terrain.RenderTiles.HighContrastBg.getValue()) {
            SpriteRenderer.instance.glClearColor(255, 0, 255, 255);
            SpriteRenderer.instance.glClear(16384);
         }

         this.frameStage = 1;
      }
   }

   public TextureFBO getOffscreenBuffer() {
      return this.OffscreenBuffer.getCurrent(0);
   }

   public TextureFBO getOffscreenBuffer(int var1) {
      return this.OffscreenBuffer.getCurrent(var1);
   }

   public void setLastRenderedFBO(TextureFBO var1) {
      this.OffscreenBuffer.FBOrendered = var1;
   }

   public void DoStartFrameStuff(int var1, int var2, float var3, int var4) {
      this.DoStartFrameStuff(var1, var2, var3, var4, false);
   }

   public void DoStartFrameStuff(int var1, int var2, float var3, int var4, boolean var5) {
      this.DoStartFrameStuffInternal(var1, var2, var3, var4, var5, false, false);
   }

   public void DoEndFrameStuffFx(int var1, int var2, int var3) {
      GL11.glPopAttrib();
      --this.stack;
      GL11.glMatrixMode(5889);
      GL11.glPopMatrix();
      --this.stack;
      GL11.glMatrixMode(5888);
      GL11.glLoadIdentity();
   }

   public void DoStartFrameStuffSmartTextureFx(int var1, int var2, int var3) {
      this.DoStartFrameStuffInternal(var1, var2, 1.0F, var3, false, true, true);
   }

   private void DoStartFrameStuffInternal(int var1, int var2, float var3, int var4, boolean var5, boolean var6, boolean var7) {
      GL32.glEnable(3042);
      GL32.glDepthFunc(519);
      int var8 = this.getScreenWidth();
      int var9 = this.getScreenHeight();
      if (!var7 && !var6) {
         var1 = var8;
      }

      if (!var7 && !var6) {
         var2 = var9;
      }

      if (!var7 && var4 != -1) {
         var1 /= IsoPlayer.numPlayers > 1 ? 2 : 1;
         var2 /= IsoPlayer.numPlayers > 2 ? 2 : 1;
      }

      GL32.glMatrixMode(5889);
      int var11;
      int var12;
      if (!var6) {
         while(this.stack > 0) {
            try {
               GL11.glPopMatrix();
               GL11.glPopAttrib();
               this.stack -= 2;
            } catch (OpenGLException var16) {
               var11 = GL11.glGetInteger(2992);

               while(var11-- > 0) {
                  GL11.glPopAttrib();
               }

               var12 = GL11.glGetInteger(2980);

               while(var12-- > 1) {
                  GL11.glPopMatrix();
               }

               this.stack = 0;
            }
         }
      }

      GL11.glAlphaFunc(516, 0.0F);
      GL11.glPushAttrib(2048);
      ++this.stack;
      GL11.glPushMatrix();
      ++this.stack;
      GL11.glLoadIdentity();
      if (!var7 && !var5) {
         GLU.gluOrtho2D(0.0F, (float)var1 * var3, (float)var2 * var3, 0.0F);
      } else {
         GLU.gluOrtho2D(0.0F, (float)var1, (float)var2, 0.0F);
      }

      GL11.glMatrixMode(5888);
      GL11.glLoadIdentity();
      if (var4 != -1) {
         var12 = var1;
         int var13 = var2;
         int var10;
         if (var5) {
            var10 = var1;
            var11 = var2;
         } else {
            var10 = var8;
            var11 = var9;
            if (IsoPlayer.numPlayers > 1) {
               var10 = var8 / 2;
            }

            if (IsoPlayer.numPlayers > 2) {
               var11 = var9 / 2;
            }
         }

         if (var6) {
            var12 = var10;
            var13 = var11;
         }

         float var14 = 0.0F;
         float var15 = (float)(var10 * (var4 % 2));
         if (var4 >= 2) {
            var14 += (float)var11;
         }

         if (var5) {
            var14 = (float)(getInstance().getScreenHeight() - var13) - var14;
         }

         GL11.glViewport((int)var15, (int)var14, var12, var13);
         GL11.glEnable(3089);
         GL11.glScissor((int)var15, (int)var14, var12, var13);
         SpriteRenderer.instance.setRenderingPlayerIndex(var4);
      } else {
         GL11.glViewport(0, 0, var1, var2);
      }

   }

   public void DoPushIsoStuff(float var1, float var2, float var3, float var4, boolean var5) {
      float var6 = (Float)getInstance().FloatParamMap.get(0);
      float var7 = (Float)getInstance().FloatParamMap.get(1);
      float var8 = (Float)getInstance().FloatParamMap.get(2);
      double var9 = (double)var6;
      double var11 = (double)var7;
      double var13 = (double)var8;
      SpriteRenderState var15 = SpriteRenderer.instance.getRenderingState();
      int var16 = var15.playerIndex;
      PlayerCamera var17 = var15.playerCamera[var16];
      float var18 = var17.RightClickX;
      float var19 = var17.RightClickY;
      float var20 = var17.getTOffX();
      float var21 = var17.getTOffY();
      float var22 = var17.DeferedX;
      float var23 = var17.DeferedY;
      var9 -= (double)var17.XToIso(-var20 - var18, -var21 - var19, 0.0F);
      var11 -= (double)var17.YToIso(-var20 - var18, -var21 - var19, 0.0F);
      var9 += (double)var22;
      var11 += (double)var23;
      double var24 = (double)((float)var17.OffscreenWidth / 1920.0F);
      double var26 = (double)((float)var17.OffscreenHeight / 1920.0F);
      Matrix4f var28 = this.tempMatrix4f;
      var28.setOrtho(-((float)var24) / 2.0F, (float)var24 / 2.0F, -((float)var26) / 2.0F, (float)var26 / 2.0F, -10.0F, 10.0F);
      PZGLUtil.pushAndLoadMatrix(5889, var28);
      Matrix4f var29 = this.tempMatrix4f;
      float var30 = (float)(2.0D / Math.sqrt(2048.0D));
      var29.scaling(0.047085002F);
      var29.scale((float)TileScale / 2.0F);
      var29.rotate(0.5235988F, 1.0F, 0.0F, 0.0F);
      var29.rotate(2.3561945F, 0.0F, 1.0F, 0.0F);
      double var31 = (double)var1 - var9;
      double var33 = (double)var2 - var11;
      var29.translate(-((float)var31), (float)((double)var3 - var13) * 2.5F, -((float)var33));
      if (var5) {
         var29.scale(-1.0F, 1.0F, 1.0F);
      } else {
         var29.scale(-1.5F, 1.5F, 1.5F);
      }

      var29.rotate(var4 + 3.1415927F, 0.0F, 1.0F, 0.0F);
      if (!var5) {
         var29.translate(0.0F, -0.48F, 0.0F);
      }

      PZGLUtil.pushAndLoadMatrix(5888, var29);
      GL11.glDepthRange(0.0D, 1.0D);
   }

   public void DoPushIsoParticleStuff(float var1, float var2, float var3) {
      GL11.glMatrixMode(5889);
      GL11.glPushMatrix();
      float var4 = (Float)getInstance().FloatParamMap.get(0);
      float var5 = (Float)getInstance().FloatParamMap.get(1);
      float var6 = (Float)getInstance().FloatParamMap.get(2);
      GL11.glLoadIdentity();
      double var7 = (double)var4;
      double var9 = (double)var5;
      double var11 = (double)var6;
      double var13 = (double)((float)Math.abs(getInstance().getOffscreenWidth(0)) / 1920.0F);
      double var15 = (double)((float)Math.abs(getInstance().getOffscreenHeight(0)) / 1080.0F);
      GL11.glLoadIdentity();
      GL11.glOrtho(-var13 / 2.0D, var13 / 2.0D, -var15 / 2.0D, var15 / 2.0D, -10.0D, 10.0D);
      GL11.glMatrixMode(5888);
      GL11.glPushMatrix();
      GL11.glLoadIdentity();
      GL11.glScaled(0.047085002064704895D, 0.047085002064704895D, 0.047085002064704895D);
      GL11.glRotatef(62.65607F, 1.0F, 0.0F, 0.0F);
      GL11.glTranslated(0.0D, -2.7200000286102295D, 0.0D);
      GL11.glRotatef(135.0F, 0.0F, 1.0F, 0.0F);
      GL11.glScalef(1.7099999F, 14.193F, 1.7099999F);
      GL11.glScalef(0.59F, 0.59F, 0.59F);
      GL11.glTranslated(-((double)var1 - var7), (double)var3 - var11, -((double)var2 - var9));
      GL11.glDepthRange(0.0D, 1.0D);
   }

   public void DoPopIsoStuff() {
      GL11.glEnable(3008);
      GL11.glDepthFunc(519);
      GL11.glDepthMask(false);
      GL11.glMatrixMode(5889);
      GL11.glPopMatrix();
      GL11.glMatrixMode(5888);
      GL11.glPopMatrix();
   }

   public void DoEndFrameStuff(int var1, int var2) {
      try {
         GL11.glPopAttrib();
         --this.stack;
         GL11.glMatrixMode(5889);
         GL11.glPopMatrix();
         --this.stack;
      } catch (Exception var6) {
         int var4 = GL11.glGetInteger(2992);

         while(var4-- > 0) {
            GL11.glPopAttrib();
         }

         GL11.glMatrixMode(5889);
         int var5 = GL11.glGetInteger(2980);

         while(var5-- > 1) {
            GL11.glPopMatrix();
         }

         this.stack = 0;
      }

      GL11.glMatrixMode(5888);
      GL11.glLoadIdentity();
      GL11.glDisable(3089);
   }

   public void RenderOffScreenBuffer() {
      if (LuaManager.thread == null || !LuaManager.thread.bStep) {
         if (this.OffscreenBuffer.Current != null) {
            IndieGL.disableStencilTest();
            IndieGL.glDoStartFrame(width, height, 1.0F, -1);
            IndieGL.glDisable(3042);
            this.OffscreenBuffer.render();
            IndieGL.glDoEndFrame();
         }
      }
   }

   public void StartFrameText(int var1) {
      if (LuaManager.thread == null || !LuaManager.thread.bStep) {
         IndieGL.glDoStartFrame(IsoCamera.getScreenWidth(var1), IsoCamera.getScreenHeight(var1), 1.0F, var1, true);
         this.frameStage = 2;
      }
   }

   public boolean StartFrameUI() {
      if (LuaManager.thread != null && LuaManager.thread.bStep) {
         return false;
      } else {
         boolean var1 = true;
         if (UIManager.useUIFBO) {
            if (UIManager.defaultthread == LuaManager.debugthread) {
               this.UIRenderThisFrame = true;
            } else {
               this.UIRenderAccumulator += GameTime.getInstance().getMultiplier() / 1.6F;
               this.UIRenderThisFrame = this.UIRenderAccumulator >= 30.0F / (float)OptionUIRenderFPS;
            }

            if (this.UIRenderThisFrame) {
               SpriteRenderer.instance.startOffscreenUI();
               SpriteRenderer.instance.glBuffer(2, 0);
            } else {
               var1 = false;
            }
         }

         IndieGL.glDoStartFrame(width, height, 1.0F, -1);
         IndieGL.glClear(1024);
         UIManager.resize();
         this.frameStage = 3;
         return var1;
      }
   }

   public Map getKeyMaps() {
      return this.keyMaps;
   }

   public void setKeyMaps(Map var1) {
      this.keyMaps = var1;
   }

   public void reinitKeyMaps() {
      this.keyMaps = new HashMap();
   }

   public int getKey(String var1) {
      if (this.keyMaps == null) {
         return 0;
      } else {
         return this.keyMaps.get(var1) != null ? (Integer)this.keyMaps.get(var1) : 0;
      }
   }

   public void addKeyBinding(String var1, Integer var2) {
      if (this.keyMaps == null) {
         this.keyMaps = new HashMap();
      }

      this.keyMaps.put(var1, var2);
   }

   public static boolean isLastStand() {
      return bLastStand;
   }

   public String getVersionNumber() {
      return gameVersion.toString();
   }

   public GameVersion getGameVersion() {
      return gameVersion;
   }

   public String getSteamServerVersion() {
      return this.steamServerVersion;
   }

   public void DoFrameReady() {
      this.updateKeyboard();
   }

   public float getCurrentPlayerZoom() {
      int var1 = IsoCamera.frameState.playerIndex;
      return this.getZoom(var1);
   }

   public float getZoom(int var1) {
      return this.OffscreenBuffer != null ? this.OffscreenBuffer.zoom[var1] * ((float)TileScale / 2.0F) : 1.0F;
   }

   public float getNextZoom(int var1, int var2) {
      return this.OffscreenBuffer != null ? this.OffscreenBuffer.getNextZoom(var1, var2) : 1.0F;
   }

   public float getMinZoom() {
      return this.OffscreenBuffer != null ? this.OffscreenBuffer.getMinZoom() * ((float)TileScale / 2.0F) : 1.0F;
   }

   public float getMaxZoom() {
      return this.OffscreenBuffer != null ? this.OffscreenBuffer.getMaxZoom() * ((float)TileScale / 2.0F) : 1.0F;
   }

   public void doZoomScroll(int var1, int var2) {
      if (this.OffscreenBuffer != null) {
         this.OffscreenBuffer.doZoomScroll(var1, var2);
      }

   }

   public String getSaveFolder() {
      return this.saveFolder;
   }

   public void setSaveFolder(String var1) {
      if (!this.saveFolder.equals(var1)) {
         File var2 = (new File(var1)).getAbsoluteFile();
         if (!var2.exists()) {
            var2.mkdir();
         }

         var2 = new File(var2, "mods");
         if (!var2.exists()) {
            var2.mkdir();
         }

         String var3 = this.saveFolder + File.separator;
         this.saveFolder = var1;
         this.copyPasteFolders(var3 + "mods");
         this.deleteDirectoryRecusrively(var3);
      }

   }

   public void deleteDirectoryRecusrively(String var1) {
      File var2 = new File(var1);
      String[] var3 = var2.list();

      for(int var4 = 0; var4 < var3.length; ++var4) {
         File var5 = new File(var1 + File.separator + var3[var4]);
         if (var5.isDirectory()) {
            this.deleteDirectoryRecusrively(var1 + File.separator + var3[var4]);
         } else {
            var5.delete();
         }
      }

      var2.delete();
   }

   public boolean getOptionZoom() {
      return OptionZoom;
   }

   public void setOptionZoom(boolean var1) {
      OptionZoom = var1;
   }

   public void zoomOptionChanged(boolean var1) {
      if (var1) {
         RenderThread.invokeOnRenderContext(() -> {
            if (OptionZoom && !SafeModeForced) {
               SafeMode = false;
               this.bSupportsFBO = true;
               this.OffscreenBuffer.bZoomEnabled = true;
               this.supportsFBO();
            } else {
               this.OffscreenBuffer.destroy();
               SafeMode = true;
               this.bSupportsFBO = false;
               this.OffscreenBuffer.bZoomEnabled = false;
            }

         });
         DebugLog.log("SafeMode is " + (SafeMode ? "on" : "off"));
      } else {
         SafeMode = SafeModeForced;
         this.OffscreenBuffer.bZoomEnabled = OptionZoom && !SafeModeForced;
      }
   }

   public void zoomLevelsChanged() {
      if (this.OffscreenBuffer.Current != null) {
         RenderThread.invokeOnRenderContext(() -> {
            this.OffscreenBuffer.destroy();
            this.zoomOptionChanged(true);
         });
      }

   }

   public boolean isZoomEnabled() {
      return this.OffscreenBuffer.bZoomEnabled;
   }

   public void initFBOs() {
      if (OptionZoom && !SafeModeForced) {
         RenderThread.invokeOnRenderContext(this::supportsFBO);
      } else {
         SafeMode = true;
         this.OffscreenBuffer.bZoomEnabled = false;
      }

      DebugLog.log("SafeMode is " + (SafeMode ? "on" : "off"));
   }

   public boolean getAutoZoom(int var1) {
      return bAutoZoom[var1];
   }

   public void setAutoZoom(int var1, boolean var2) {
      bAutoZoom[var1] = var2;
      if (this.OffscreenBuffer != null) {
         this.OffscreenBuffer.bAutoZoom[var1] = var2;
      }

   }

   public boolean getOptionVSync() {
      return OptionVSync;
   }

   public void setOptionVSync(boolean var1) {
      OptionVSync = var1;
      RenderThread.invokeOnRenderContext(() -> {
         Display.setVSyncEnabled(var1);
      });
   }

   public int getOptionSoundVolume() {
      return OptionSoundVolume;
   }

   public float getRealOptionSoundVolume() {
      return (float)OptionSoundVolume / 10.0F;
   }

   public void setOptionSoundVolume(int var1) {
      OptionSoundVolume = Math.max(0, Math.min(10, var1));
      if (!GameClient.bClient || !GameSounds.soundIsPaused) {
         if (SoundManager.instance != null) {
            SoundManager.instance.setSoundVolume((float)var1 / 10.0F);
         }

      }
   }

   public int getOptionMusicVolume() {
      return OptionMusicVolume;
   }

   public void setOptionMusicVolume(int var1) {
      OptionMusicVolume = Math.max(0, Math.min(10, var1));
      if (!GameClient.bClient || !GameSounds.soundIsPaused) {
         if (SoundManager.instance != null) {
            SoundManager.instance.setMusicVolume((float)var1 / 10.0F);
         }

      }
   }

   public int getOptionAmbientVolume() {
      return OptionAmbientVolume;
   }

   public void setOptionAmbientVolume(int var1) {
      OptionAmbientVolume = Math.max(0, Math.min(10, var1));
      if (!GameClient.bClient || !GameSounds.soundIsPaused) {
         if (SoundManager.instance != null) {
            SoundManager.instance.setAmbientVolume((float)var1 / 10.0F);
         }

      }
   }

   public int getOptionMusicLibrary() {
      return OptionMusicLibrary;
   }

   public void setOptionMusicLibrary(int var1) {
      if (var1 < 1) {
         var1 = 1;
      }

      if (var1 > 3) {
         var1 = 3;
      }

      OptionMusicLibrary = var1;
   }

   public int getOptionVehicleEngineVolume() {
      return OptionVehicleEngineVolume;
   }

   public void setOptionVehicleEngineVolume(int var1) {
      OptionVehicleEngineVolume = Math.max(0, Math.min(10, var1));
      if (!GameClient.bClient || !GameSounds.soundIsPaused) {
         if (SoundManager.instance != null) {
            SoundManager.instance.setVehicleEngineVolume((float)OptionVehicleEngineVolume / 10.0F);
         }

      }
   }

   public boolean getOptionVoiceEnable() {
      return OptionVoiceEnable;
   }

   public void setOptionVoiceEnable(boolean var1) {
      OptionVoiceEnable = var1;
   }

   public int getOptionVoiceMode() {
      return OptionVoiceMode;
   }

   public void setOptionVoiceMode(int var1) {
      OptionVoiceMode = var1;
      VoiceManager.instance.setMode(var1);
   }

   public int getOptionVoiceVADMode() {
      return OptionVoiceVADMode;
   }

   public void setOptionVoiceVADMode(int var1) {
      OptionVoiceVADMode = var1;
      VoiceManager.instance.setVADMode(var1);
   }

   public int getOptionVoiceVolumeMic() {
      return OptionVoiceVolumeMic;
   }

   public void setOptionVoiceVolumeMic(int var1) {
      OptionVoiceVolumeMic = var1;
      VoiceManager.instance.setVolumeMic(var1);
   }

   public int getOptionVoiceVolumePlayers() {
      return OptionVoiceVolumePlayers;
   }

   public void setOptionVoiceVolumePlayers(int var1) {
      OptionVoiceVolumePlayers = var1;
      VoiceManager.instance.setVolumePlayers(var1);
   }

   public String getOptionVoiceRecordDeviceName() {
      return OptionVoiceRecordDeviceName;
   }

   public void setOptionVoiceRecordDeviceName(String var1) {
      OptionVoiceRecordDeviceName = var1;
      VoiceManager.instance.UpdateRecordDevice();
   }

   public int getOptionVoiceRecordDevice() {
      if (!SoundDisabled && !VoiceManager.VoipDisabled) {
         int var1 = javafmod.FMOD_System_GetRecordNumDrivers();

         for(int var2 = 0; var2 < var1; ++var2) {
            FMOD_DriverInfo var3 = new FMOD_DriverInfo();
            javafmod.FMOD_System_GetRecordDriverInfo(var2, var3);
            if (var3.name.equals(OptionVoiceRecordDeviceName)) {
               return var2 + 1;
            }
         }

         return 0;
      } else {
         return 0;
      }
   }

   public void setOptionVoiceRecordDevice(int var1) {
      if (!SoundDisabled && !VoiceManager.VoipDisabled) {
         if (var1 >= 1) {
            FMOD_DriverInfo var2 = new FMOD_DriverInfo();
            javafmod.FMOD_System_GetRecordDriverInfo(var1 - 1, var2);
            OptionVoiceRecordDeviceName = var2.name;
            VoiceManager.instance.UpdateRecordDevice();
         }
      }
   }

   public int getMicVolumeIndicator() {
      return VoiceManager.instance.getMicVolumeIndicator();
   }

   public boolean getMicVolumeError() {
      return VoiceManager.instance.getMicVolumeError();
   }

   public boolean getServerVOIPEnable() {
      return VoiceManager.instance.getServerVOIPEnable();
   }

   public void setTestingMicrophone(boolean var1) {
      VoiceManager.instance.setTestingMicrophone(var1);
   }

   public int getOptionReloadDifficulty() {
      return 2;
   }

   public void setOptionReloadDifficulty(int var1) {
      OptionReloadDifficulty = Math.max(1, Math.min(3, var1));
   }

   public boolean getOptionRackProgress() {
      return OptionRackProgress;
   }

   public void setOptionRackProgress(boolean var1) {
      OptionRackProgress = var1;
   }

   public int getOptionFontSize() {
      return OptionFontSize;
   }

   public void setOptionFontSize(int var1) {
      OptionFontSize = PZMath.clamp(var1, 1, 5);
   }

   public String getOptionContextMenuFont() {
      return OptionContextMenuFont;
   }

   public void setOptionContextMenuFont(String var1) {
      OptionContextMenuFont = var1;
   }

   public String getOptionInventoryFont() {
      return OptionInventoryFont;
   }

   public void setOptionInventoryFont(String var1) {
      OptionInventoryFont = var1;
   }

   public String getOptionTooltipFont() {
      return OptionTooltipFont;
   }

   public void setOptionTooltipFont(String var1) {
      OptionTooltipFont = var1;
      ObjectTooltip.checkFont();
   }

   public String getOptionMeasurementFormat() {
      return OptionMeasurementFormat;
   }

   public void setOptionMeasurementFormat(String var1) {
      OptionMeasurementFormat = var1;
   }

   public int getOptionClockFormat() {
      return OptionClockFormat;
   }

   public int getOptionClockSize() {
      return OptionClockSize;
   }

   public void setOptionClockFormat(int var1) {
      if (var1 < 1) {
         var1 = 1;
      }

      if (var1 > 2) {
         var1 = 2;
      }

      OptionClockFormat = var1;
   }

   public void setOptionClockSize(int var1) {
      if (var1 < 1) {
         var1 = 1;
      }

      if (var1 > 2) {
         var1 = 2;
      }

      OptionClockSize = var1;
   }

   public boolean getOptionClock24Hour() {
      return OptionClock24Hour;
   }

   public void setOptionClock24Hour(boolean var1) {
      OptionClock24Hour = var1;
   }

   public boolean getOptionModsEnabled() {
      return OptionModsEnabled;
   }

   public void setOptionModsEnabled(boolean var1) {
      OptionModsEnabled = var1;
   }

   public int getOptionBloodDecals() {
      return OptionBloodDecals;
   }

   public void setOptionBloodDecals(int var1) {
      if (var1 < 0) {
         var1 = 0;
      }

      if (var1 > 10) {
         var1 = 10;
      }

      OptionBloodDecals = var1;
   }

   public boolean getOptionBorderlessWindow() {
      return OptionBorderlessWindow;
   }

   public void setOptionBorderlessWindow(boolean var1) {
      OptionBorderlessWindow = var1;
   }

   public boolean getOptionLockCursorToWindow() {
      return OptionLockCursorToWindow;
   }

   public void setOptionLockCursorToWindow(boolean var1) {
      OptionLockCursorToWindow = var1;
   }

   public boolean getOptionTextureCompression() {
      return OptionTextureCompression;
   }

   public void setOptionTextureCompression(boolean var1) {
      OptionTextureCompression = var1;
   }

   public boolean getOptionTexture2x() {
      return OptionTexture2x;
   }

   public void setOptionTexture2x(boolean var1) {
      OptionTexture2x = var1;
   }

   public boolean getOptionModelTextureMipmaps() {
      return OptionModelTextureMipmaps;
   }

   public void setOptionModelTextureMipmaps(boolean var1) {
      OptionModelTextureMipmaps = var1;
   }

   public String getOptionZoomLevels1x() {
      return OptionZoomLevels1x;
   }

   public void setOptionZoomLevels1x(String var1) {
      OptionZoomLevels1x = var1 == null ? "" : var1;
   }

   public String getOptionZoomLevels2x() {
      return OptionZoomLevels2x;
   }

   public void setOptionZoomLevels2x(String var1) {
      OptionZoomLevels2x = var1 == null ? "" : var1;
   }

   public ArrayList getDefaultZoomLevels() {
      return this.OffscreenBuffer.getDefaultZoomLevels();
   }

   public void setOptionActiveController(int var1, boolean var2) {
      if (var1 >= 0 && var1 < GameWindow.GameInput.getControllerCount()) {
         Controller var3 = GameWindow.GameInput.getController(var1);
         if (var3 != null) {
            JoypadManager.instance.setControllerActive(var3.getGUID(), var2);
         }

      }
   }

   public boolean getOptionActiveController(String var1) {
      return JoypadManager.instance.ActiveControllerGUIDs.contains(var1);
   }

   public boolean isOptionShowChatTimestamp() {
      return OptionShowChatTimestamp;
   }

   public void setOptionShowChatTimestamp(boolean var1) {
      OptionShowChatTimestamp = var1;
   }

   public boolean isOptionShowChatTitle() {
      return OptionShowChatTitle;
   }

   public String getOptionChatFontSize() {
      return OptionChatFontSize;
   }

   public void setOptionChatFontSize(String var1) {
      OptionChatFontSize = var1;
   }

   public void setOptionShowChatTitle(boolean var1) {
      OptionShowChatTitle = var1;
   }

   public float getOptionMinChatOpaque() {
      return OptionMinChatOpaque;
   }

   public void setOptionMinChatOpaque(float var1) {
      OptionMinChatOpaque = var1;
   }

   public float getOptionMaxChatOpaque() {
      return OptionMaxChatOpaque;
   }

   public void setOptionMaxChatOpaque(float var1) {
      OptionMaxChatOpaque = var1;
   }

   public float getOptionChatFadeTime() {
      return OptionChatFadeTime;
   }

   public void setOptionChatFadeTime(float var1) {
      OptionChatFadeTime = var1;
   }

   public boolean getOptionChatOpaqueOnFocus() {
      return OptionChatOpaqueOnFocus;
   }

   public void setOptionChatOpaqueOnFocus(boolean var1) {
      OptionChatOpaqueOnFocus = var1;
   }

   public boolean getOptionUIFBO() {
      return OptionUIFBO;
   }

   public void setOptionUIFBO(boolean var1) {
      OptionUIFBO = var1;
      if (GameWindow.states.current == IngameState.instance) {
         UIManager.useUIFBO = getInstance().supportsFBO() && OptionUIFBO;
      }

   }

   public int getOptionAimOutline() {
      return OptionAimOutline;
   }

   public void setOptionAimOutline(int var1) {
      OptionAimOutline = PZMath.clamp(var1, 1, 3);
   }

   public int getOptionUIRenderFPS() {
      return OptionUIRenderFPS;
   }

   public void setOptionUIRenderFPS(int var1) {
      OptionUIRenderFPS = var1;
   }

   public void setOptionRadialMenuKeyToggle(boolean var1) {
      OptionRadialMenuKeyToggle = var1;
   }

   public boolean getOptionRadialMenuKeyToggle() {
      return OptionRadialMenuKeyToggle;
   }

   public void setOptionReloadRadialInstant(boolean var1) {
      OptionReloadRadialInstant = var1;
   }

   public boolean getOptionReloadRadialInstant() {
      return OptionReloadRadialInstant;
   }

   public void setOptionPanCameraWhileAiming(boolean var1) {
      OptionPanCameraWhileAiming = var1;
   }

   public boolean getOptionPanCameraWhileAiming() {
      return OptionPanCameraWhileAiming;
   }

   public void setOptionPanCameraWhileDriving(boolean var1) {
      OptionPanCameraWhileDriving = var1;
   }

   public boolean getOptionPanCameraWhileDriving() {
      return OptionPanCameraWhileDriving;
   }

   public String getOptionCycleContainerKey() {
      return OptionCycleContainerKey;
   }

   public void setOptionCycleContainerKey(String var1) {
      OptionCycleContainerKey = var1;
   }

   public boolean getOptionDropItemsOnSquareCenter() {
      return OptionDropItemsOnSquareCenter;
   }

   public void setOptionDropItemsOnSquareCenter(boolean var1) {
      OptionDropItemsOnSquareCenter = var1;
   }

   public boolean getOptionTimedActionGameSpeedReset() {
      return OptionTimedActionGameSpeedReset;
   }

   public void setOptionTimedActionGameSpeedReset(boolean var1) {
      OptionTimedActionGameSpeedReset = var1;
   }

   public int getOptionShoulderButtonContainerSwitch() {
      return OptionShoulderButtonContainerSwitch;
   }

   public void setOptionShoulderButtonContainerSwitch(int var1) {
      OptionShoulderButtonContainerSwitch = var1;
   }

   public boolean getOptionSingleContextMenu(int var1) {
      return OptionSingleContextMenu[var1];
   }

   public void setOptionSingleContextMenu(int var1, boolean var2) {
      OptionSingleContextMenu[var1] = var2;
   }

   public boolean getOptionAutoDrink() {
      return OptionAutoDrink;
   }

   public void setOptionAutoDrink(boolean var1) {
      OptionAutoDrink = var1;
   }

   public boolean getOptionCorpseShadows() {
      return OptionCorpseShadows;
   }

   public void setOptionCorpseShadows(boolean var1) {
      OptionCorpseShadows = var1;
   }

   public boolean getOptionLeaveKeyInIgnition() {
      return OptionLeaveKeyInIgnition;
   }

   public void setOptionLeaveKeyInIgnition(boolean var1) {
      OptionLeaveKeyInIgnition = var1;
   }

   public int getOptionSearchModeOverlayEffect() {
      return OptionSearchModeOverlayEffect;
   }

   public void setOptionSearchModeOverlayEffect(int var1) {
      OptionSearchModeOverlayEffect = var1;
   }

   public int getOptionSimpleClothingTextures() {
      return OptionSimpleClothingTextures;
   }

   public void setOptionSimpleClothingTextures(int var1) {
      OptionSimpleClothingTextures = PZMath.clamp(var1, 1, 3);
   }

   public boolean isOptionSimpleClothingTextures(boolean var1) {
      switch(OptionSimpleClothingTextures) {
      case 1:
         return false;
      case 2:
         return var1;
      default:
         return true;
      }
   }

   public boolean getOptionSimpleWeaponTextures() {
      return OptionSimpleWeaponTextures;
   }

   public void setOptionSimpleWeaponTextures(boolean var1) {
      OptionSimpleWeaponTextures = var1;
   }

   public int getOptionIgnoreProneZombieRange() {
      return OptionIgnoreProneZombieRange;
   }

   public void setOptionIgnoreProneZombieRange(int var1) {
      OptionIgnoreProneZombieRange = PZMath.clamp(var1, 1, 5);
   }

   public float getIgnoreProneZombieRange() {
      switch(OptionIgnoreProneZombieRange) {
      case 1:
         return -1.0F;
      case 2:
         return 1.5F;
      case 3:
         return 2.0F;
      case 4:
         return 2.5F;
      case 5:
         return 3.0F;
      default:
         return -1.0F;
      }
   }

   private void readPerPlayerBoolean(String var1, boolean[] var2) {
      Arrays.fill(var2, false);
      String[] var3 = var1.split(",");

      for(int var4 = 0; var4 < var3.length && var4 != 4; ++var4) {
         var2[var4] = StringUtils.tryParseBoolean(var3[var4]);
      }

   }

   private String getPerPlayerBooleanString(boolean[] var1) {
      return String.format("%b,%b,%b,%b", var1[0], var1[1], var1[2], var1[3]);
   }

   /** @deprecated */
   @Deprecated
   public void ResetLua(boolean var1, String var2) throws IOException {
      this.ResetLua("default", var2);
   }

   public void ResetLua(String var1, String var2) throws IOException {
      if (SpriteRenderer.instance != null) {
         GameWindow.DrawReloadingLua = true;
         GameWindow.render();
         GameWindow.DrawReloadingLua = false;
      }

      RenderThread.setWaitForRenderState(false);
      SpriteRenderer.instance.notifyRenderStateQueue();
      ScriptManager.instance.Reset();
      ClothingDecals.Reset();
      BeardStyles.Reset();
      HairStyles.Reset();
      OutfitManager.Reset();
      AnimationSet.Reset();
      GameSounds.Reset();
      VehicleType.Reset();
      LuaEventManager.Reset();
      MapObjects.Reset();
      UIManager.init();
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
      WorldMap.Reset();
      LuaManager.init();
      JoypadManager.instance.Reset();
      GameKeyboard.doLuaKeyPressed = true;
      Texture.nullTextures.clear();
      ZomboidFileSystem.instance.Reset();
      ZomboidFileSystem.instance.init();
      ZomboidFileSystem.instance.loadMods(var1);
      ZomboidFileSystem.instance.loadModPackFiles();
      ModelManager.instance.loadModAnimations();
      Languages.instance.init();
      Translator.loadFiles();
      CustomPerks.instance.init();
      CustomPerks.instance.initLua();
      CustomSandboxOptions.instance.init();
      CustomSandboxOptions.instance.initInstance(SandboxOptions.instance);
      ScriptManager.instance.Load();
      ClothingDecals.init();
      BeardStyles.init();
      HairStyles.init();
      OutfitManager.init();

      try {
         TextManager.instance.Init();
         LuaManager.LoadDirBase();
      } catch (Exception var6) {
         ExceptionLogger.logException(var6);
         GameWindow.DoLoadingText("Reloading Lua - ERRORS!");

         try {
            Thread.sleep(2000L);
         } catch (InterruptedException var5) {
         }
      }

      ZomboidGlobals.Load();
      RenderThread.setWaitForRenderState(true);
      LuaEventManager.triggerEvent("OnGameBoot");
      LuaEventManager.triggerEvent("OnMainMenuEnter");
      LuaEventManager.triggerEvent("OnResetLua", var2);
   }

   public void DelayResetLua(String var1, String var2) {
      this.m_delayResetLua_activeMods = var1;
      this.m_delayResetLua_reason = var2;
   }

   public void CheckDelayResetLua() throws IOException {
      if (this.m_delayResetLua_activeMods != null) {
         String var1 = this.m_delayResetLua_activeMods;
         String var2 = this.m_delayResetLua_reason;
         this.m_delayResetLua_activeMods = null;
         this.m_delayResetLua_reason = null;
         this.ResetLua(var1, var2);
      }

   }

   public boolean isShowPing() {
      return this.showPing;
   }

   public void setShowPing(boolean var1) {
      this.showPing = var1;
   }

   public boolean isForceSnow() {
      return this.forceSnow;
   }

   public void setForceSnow(boolean var1) {
      this.forceSnow = var1;
   }

   public boolean isZombieGroupSound() {
      return this.zombieGroupSound;
   }

   public void setZombieGroupSound(boolean var1) {
      this.zombieGroupSound = var1;
   }

   public String getBlinkingMoodle() {
      return this.blinkingMoodle;
   }

   public void setBlinkingMoodle(String var1) {
      this.blinkingMoodle = var1;
   }

   public boolean isTutorialDone() {
      return this.tutorialDone;
   }

   public void setTutorialDone(boolean var1) {
      this.tutorialDone = var1;
   }

   public boolean isVehiclesWarningShow() {
      return this.vehiclesWarningShow;
   }

   public void setVehiclesWarningShow(boolean var1) {
      this.vehiclesWarningShow = var1;
   }

   public void initPoisonousBerry() {
      ArrayList var1 = new ArrayList();
      var1.add("Base.BerryGeneric1");
      var1.add("Base.BerryGeneric2");
      var1.add("Base.BerryGeneric3");
      var1.add("Base.BerryGeneric4");
      var1.add("Base.BerryGeneric5");
      var1.add("Base.BerryPoisonIvy");
      this.setPoisonousBerry((String)var1.get(Rand.Next(0, var1.size() - 1)));
   }

   public void initPoisonousMushroom() {
      ArrayList var1 = new ArrayList();
      var1.add("Base.MushroomGeneric1");
      var1.add("Base.MushroomGeneric2");
      var1.add("Base.MushroomGeneric3");
      var1.add("Base.MushroomGeneric4");
      var1.add("Base.MushroomGeneric5");
      var1.add("Base.MushroomGeneric6");
      var1.add("Base.MushroomGeneric7");
      this.setPoisonousMushroom((String)var1.get(Rand.Next(0, var1.size() - 1)));
   }

   public String getPoisonousBerry() {
      return this.poisonousBerry;
   }

   public void setPoisonousBerry(String var1) {
      this.poisonousBerry = var1;
   }

   public String getPoisonousMushroom() {
      return this.poisonousMushroom;
   }

   public void setPoisonousMushroom(String var1) {
      this.poisonousMushroom = var1;
   }

   public static String getDifficulty() {
      return difficulty;
   }

   public static void setDifficulty(String var0) {
      difficulty = var0;
   }

   public boolean isDoneNewSaveFolder() {
      return this.doneNewSaveFolder;
   }

   public void setDoneNewSaveFolder(boolean var1) {
      this.doneNewSaveFolder = var1;
   }

   public static int getTileScale() {
      return TileScale;
   }

   public boolean isSelectingAll() {
      return this.isSelectingAll;
   }

   public void setIsSelectingAll(boolean var1) {
      this.isSelectingAll = var1;
   }

   public boolean getContentTranslationsEnabled() {
      return OptionEnableContentTranslations;
   }

   public void setContentTranslationsEnabled(boolean var1) {
      OptionEnableContentTranslations = var1;
   }

   public boolean isShowYourUsername() {
      return this.showYourUsername;
   }

   public void setShowYourUsername(boolean var1) {
      this.showYourUsername = var1;
   }

   public ColorInfo getMpTextColor() {
      if (this.mpTextColor == null) {
         this.mpTextColor = new ColorInfo((float)(Rand.Next(135) + 120) / 255.0F, (float)(Rand.Next(135) + 120) / 255.0F, (float)(Rand.Next(135) + 120) / 255.0F, 1.0F);
      }

      return this.mpTextColor;
   }

   public void setMpTextColor(ColorInfo var1) {
      if (var1.r < 0.19F) {
         var1.r = 0.19F;
      }

      if (var1.g < 0.19F) {
         var1.g = 0.19F;
      }

      if (var1.b < 0.19F) {
         var1.b = 0.19F;
      }

      this.mpTextColor = var1;
   }

   public boolean isAzerty() {
      return this.isAzerty;
   }

   public void setAzerty(boolean var1) {
      this.isAzerty = var1;
   }

   public ColorInfo getObjectHighlitedColor() {
      return this.objectHighlitedColor;
   }

   public void setObjectHighlitedColor(ColorInfo var1) {
      this.objectHighlitedColor.set(var1);
   }

   public String getSeenUpdateText() {
      return this.seenUpdateText;
   }

   public void setSeenUpdateText(String var1) {
      this.seenUpdateText = var1;
   }

   public boolean isToggleToAim() {
      return this.toggleToAim;
   }

   public void setToggleToAim(boolean var1) {
      this.toggleToAim = var1;
   }

   public boolean isToggleToRun() {
      return this.toggleToRun;
   }

   public void setToggleToRun(boolean var1) {
      this.toggleToRun = var1;
   }

   public int getXAngle(int var1, float var2) {
      double var3 = Math.toRadians((double)(225.0F + var2));
      int var5 = (new Long(Math.round((Math.sqrt(2.0D) * Math.cos(var3) + 1.0D) * (double)(var1 / 2)))).intValue();
      return var5;
   }

   public int getYAngle(int var1, float var2) {
      double var3 = Math.toRadians((double)(225.0F + var2));
      int var5 = (new Long(Math.round((Math.sqrt(2.0D) * Math.sin(var3) + 1.0D) * (double)(var1 / 2)))).intValue();
      return var5;
   }

   public boolean isCelsius() {
      return this.celsius;
   }

   public void setCelsius(boolean var1) {
      this.celsius = var1;
   }

   public boolean isInDebug() {
      return bDebug;
   }

   public boolean isRiversideDone() {
      return this.riversideDone;
   }

   public void setRiversideDone(boolean var1) {
      this.riversideDone = var1;
   }

   public boolean isNoSave() {
      return this.noSave;
   }

   public void setNoSave(boolean var1) {
      this.noSave = var1;
   }

   public boolean isShowFirstTimeVehicleTutorial() {
      return this.showFirstTimeVehicleTutorial;
   }

   public void setShowFirstTimeVehicleTutorial(boolean var1) {
      this.showFirstTimeVehicleTutorial = var1;
   }

   public boolean getOptionDisplayAsCelsius() {
      return OptionTemperatureDisplayCelsius;
   }

   public void setOptionDisplayAsCelsius(boolean var1) {
      OptionTemperatureDisplayCelsius = var1;
   }

   public boolean isShowFirstTimeWeatherTutorial() {
      return this.showFirstTimeWeatherTutorial;
   }

   public void setShowFirstTimeWeatherTutorial(boolean var1) {
      this.showFirstTimeWeatherTutorial = var1;
   }

   public boolean getOptionDoWindSpriteEffects() {
      return OptionDoWindSpriteEffects;
   }

   public void setOptionDoWindSpriteEffects(boolean var1) {
      OptionDoWindSpriteEffects = var1;
   }

   public boolean getOptionDoDoorSpriteEffects() {
      return OptionDoDoorSpriteEffects;
   }

   public void setOptionDoDoorSpriteEffects(boolean var1) {
      OptionDoDoorSpriteEffects = var1;
   }

   public void setOptionUpdateSneakButton(boolean var1) {
      OptionUpdateSneakButton = var1;
   }

   public boolean getOptionUpdateSneakButton() {
      return OptionUpdateSneakButton;
   }

   public boolean isNewReloading() {
      return this.newReloading;
   }

   public void setNewReloading(boolean var1) {
      this.newReloading = var1;
   }

   public boolean isShowFirstTimeSneakTutorial() {
      return this.showFirstTimeSneakTutorial;
   }

   public void setShowFirstTimeSneakTutorial(boolean var1) {
      this.showFirstTimeSneakTutorial = var1;
   }

   public void setOptiondblTapJogToSprint(boolean var1) {
      OptiondblTapJogToSprint = var1;
   }

   public boolean isOptiondblTapJogToSprint() {
      return OptiondblTapJogToSprint;
   }

   public boolean isToggleToSprint() {
      return this.toggleToSprint;
   }

   public void setToggleToSprint(boolean var1) {
      this.toggleToSprint = var1;
   }

   public int getIsoCursorVisibility() {
      return this.isoCursorVisibility;
   }

   public void setIsoCursorVisibility(int var1) {
      this.isoCursorVisibility = var1;
   }

   public boolean getOptionShowCursorWhileAiming() {
      return OptionShowCursorWhileAiming;
   }

   public void setOptionShowCursorWhileAiming(boolean var1) {
      OptionShowCursorWhileAiming = var1;
   }

   public boolean gotNewBelt() {
      return this.gotNewBelt;
   }

   public void setGotNewBelt(boolean var1) {
      this.gotNewBelt = var1;
   }

   public void setAnimPopupDone(boolean var1) {
      this.bAnimPopupDone = var1;
   }

   public boolean isAnimPopupDone() {
      return this.bAnimPopupDone;
   }

   public void setModsPopupDone(boolean var1) {
      this.bModsPopupDone = var1;
   }

   public boolean isModsPopupDone() {
      return this.bModsPopupDone;
   }

   public boolean isRenderPrecipIndoors() {
      return OptionRenderPrecipIndoors;
   }

   public void setRenderPrecipIndoors(boolean var1) {
      OptionRenderPrecipIndoors = var1;
   }

   public boolean isCollideZombies() {
      return this.collideZombies;
   }

   public void setCollideZombies(boolean var1) {
      this.collideZombies = var1;
   }

   public boolean isFlashIsoCursor() {
      return this.flashIsoCursor;
   }

   public void setFlashIsoCursor(boolean var1) {
      this.flashIsoCursor = var1;
   }

   public boolean isOptionProgressBar() {
      return true;
   }

   public void setOptionProgressBar(boolean var1) {
      OptionProgressBar = var1;
   }

   public void setOptionLanguageName(String var1) {
      OptionLanguageName = var1;
   }

   public String getOptionLanguageName() {
      return OptionLanguageName;
   }

   public int getOptionRenderPrecipitation() {
      return OptionRenderPrecipitation;
   }

   public void setOptionRenderPrecipitation(int var1) {
      OptionRenderPrecipitation = var1;
   }

   public void setOptionAutoProneAtk(boolean var1) {
      OptionAutoProneAtk = var1;
   }

   public boolean isOptionAutoProneAtk() {
      return OptionAutoProneAtk;
   }

   public void setOption3DGroundItem(boolean var1) {
      Option3DGroundItem = var1;
   }

   public boolean isOption3DGroundItem() {
      return Option3DGroundItem;
   }

   public Object getOptionOnStartup(String var1) {
      return optionsOnStartup.get(var1);
   }

   public void setOptionOnStartup(String var1, Object var2) {
      optionsOnStartup.put(var1, var2);
   }

   public void countMissing3DItems() {
      ArrayList var1 = ScriptManager.instance.getAllItems();
      int var2 = 0;
      Iterator var3 = var1.iterator();

      while(var3.hasNext()) {
         Item var4 = (Item)var3.next();
         if (var4.type != Item.Type.Weapon && var4.type != Item.Type.Moveable && !var4.name.contains("ZedDmg") && !var4.name.contains("Wound") && !var4.name.contains("MakeUp") && !var4.name.contains("Bandage") && !var4.name.contains("Hat") && !var4.getObsolete() && StringUtils.isNullOrEmpty(var4.worldObjectSprite) && StringUtils.isNullOrEmpty(var4.worldStaticModel)) {
            System.out.println("Missing: " + var4.name);
            ++var2;
         }
      }

      System.out.println("total missing: " + var2 + "/" + var1.size());
   }
}

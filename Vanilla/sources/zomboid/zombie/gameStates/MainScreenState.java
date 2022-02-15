package zombie.gameStates;

import fmod.fmod.Audio;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.Map;
import java.util.function.Consumer;
import javax.imageio.ImageIO;
import org.lwjgl.glfw.GLFWImage;
import org.lwjgl.glfw.GLFWImage.Buffer;
import org.lwjglx.opengl.Display;
import org.lwjglx.opengl.OpenGLException;
import zombie.DebugFileWatcher;
import zombie.GameTime;
import zombie.GameWindow;
import zombie.IndieGL;
import zombie.SoundManager;
import zombie.ZomboidFileSystem;
import zombie.Lua.LuaEventManager;
import zombie.asset.AssetManagers;
import zombie.characters.IsoPlayer;
import zombie.core.BoxedStaticValues;
import zombie.core.Color;
import zombie.core.Core;
import zombie.core.ProxyPrintStream;
import zombie.core.Rand;
import zombie.core.SpriteRenderer;
import zombie.core.Translator;
import zombie.core.logger.ExceptionLogger;
import zombie.core.logger.LoggerManager;
import zombie.core.logger.ZipLogs;
import zombie.core.opengl.RenderThread;
import zombie.core.raknet.VoiceManager;
import zombie.core.skinnedmodel.advancedanimation.AnimNodeAsset;
import zombie.core.skinnedmodel.advancedanimation.AnimNodeAssetManager;
import zombie.core.skinnedmodel.model.AiSceneAsset;
import zombie.core.skinnedmodel.model.AiSceneAssetManager;
import zombie.core.skinnedmodel.model.AnimationAsset;
import zombie.core.skinnedmodel.model.AnimationAssetManager;
import zombie.core.skinnedmodel.model.MeshAssetManager;
import zombie.core.skinnedmodel.model.Model;
import zombie.core.skinnedmodel.model.ModelAssetManager;
import zombie.core.skinnedmodel.model.ModelMesh;
import zombie.core.skinnedmodel.model.jassimp.JAssImpImporter;
import zombie.core.skinnedmodel.population.ClothingItem;
import zombie.core.skinnedmodel.population.ClothingItemAssetManager;
import zombie.core.textures.Texture;
import zombie.core.textures.TextureAssetManager;
import zombie.core.textures.TextureID;
import zombie.core.textures.TextureIDAssetManager;
import zombie.debug.DebugLog;
import zombie.debug.DebugType;
import zombie.input.JoypadManager;
import zombie.modding.ActiveMods;
import zombie.ui.TextManager;
import zombie.ui.UIFont;
import zombie.ui.UIManager;
import zombie.worldMap.UIWorldMap;
import zombie.worldMap.WorldMapData;
import zombie.worldMap.WorldMapDataAssetManager;

public final class MainScreenState extends GameState {
   public static String Version = "RC 3";
   public static Audio ambient;
   public static float totalScale = 1.0F;
   public float alpha = 1.0F;
   public float alphaStep = 0.03F;
   private int RestartDebounceClickTimer = 10;
   public final ArrayList Elements = new ArrayList(16);
   public float targetAlpha = 1.0F;
   int lastH;
   int lastW;
   MainScreenState.ScreenElement Logo;
   public static MainScreenState instance;
   public boolean showLogo = false;
   private float FadeAlpha = 0.0F;
   public boolean lightningTimelineMarker = false;
   float lightningTime = 0.0F;
   public UIWorldMap m_worldMap;
   public float lightningDelta = 0.0F;
   public float lightningTargetDelta = 0.0F;
   public float lightningFullTimer = 0.0F;
   public float lightningCount = 0.0F;
   public float lightOffCount = 0.0F;
   private ConnectToServerState connectToServerState;
   private static GLFWImage windowIcon1;
   private static GLFWImage windowIcon2;
   private static ByteBuffer windowIconBB1;
   private static ByteBuffer windowIconBB2;

   public static void main(String[] var0) {
      for(int var1 = 0; var1 < var0.length; ++var1) {
         if (var0[var1] != null && var0[var1].startsWith("-cachedir=")) {
            ZomboidFileSystem.instance.setCacheDir(var0[var1].replace("-cachedir=", "").trim());
         }
      }

      ZipLogs.addZipFile(false);

      try {
         String var10000 = ZomboidFileSystem.instance.getCacheDir();
         String var12 = var10000 + File.separator + "console.txt";
         FileOutputStream var2 = new FileOutputStream(var12);
         PrintStream var3 = new PrintStream(var2, true);
         System.setOut(new ProxyPrintStream(System.out, var3));
         System.setErr(new ProxyPrintStream(System.err, var3));
      } catch (FileNotFoundException var10) {
         var10.printStackTrace();
      }

      DebugLog.init();
      LoggerManager.init();
      DebugLog.log("cachedir set to \"" + ZomboidFileSystem.instance.getCacheDir() + "\"");
      JAssImpImporter.Init();
      SimpleDateFormat var13 = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
      System.out.println(var13.format(Calendar.getInstance().getTime()));
      System.out.println("cachedir is \"" + ZomboidFileSystem.instance.getCacheDir() + "\"");
      System.out.println("LogFileDir is \"" + LoggerManager.getLogsDir() + "\"");
      printSpecs();
      System.getProperties().list(System.out);
      System.out.println("-----");
      System.out.println("versionNumber=" + Core.getInstance().getVersionNumber() + " demo=false");
      DebugLog.General.println("svnRevision=%s date=%s time=%s", "", "", "");
      Display.setIcon(loadIcons());
      Rand.init();

      for(int var14 = 0; var14 < var0.length; ++var14) {
         if (var0[var14] != null) {
            if (var0[var14].contains("safemode")) {
               Core.SafeMode = true;
               Core.SafeModeForced = true;
            } else if (var0[var14].equals("-nosound")) {
               Core.SoundDisabled = true;
            } else if (var0[var14].equals("-aitest")) {
               IsoPlayer.isTestAIMode = true;
            } else if (var0[var14].equals("-novoip")) {
               VoiceManager.VoipDisabled = true;
            } else if (var0[var14].equals("-debug")) {
               Core.bDebug = true;
            } else if (!var0[var14].startsWith("-debuglog=")) {
               if (!var0[var14].startsWith("-cachedir=")) {
                  if (var0[var14].equals("+connect")) {
                     if (var14 + 1 < var0.length) {
                        System.setProperty("args.server.connect", var0[var14 + 1]);
                     }

                     ++var14;
                  } else if (var0[var14].equals("+password")) {
                     if (var14 + 1 < var0.length) {
                        System.setProperty("args.server.password", var0[var14 + 1]);
                     }

                     ++var14;
                  } else if (var0[var14].contains("-debugtranslation")) {
                     Translator.debug = true;
                  } else if ("-modfolders".equals(var0[var14])) {
                     if (var14 + 1 < var0.length) {
                        ZomboidFileSystem.instance.setModFoldersOrder(var0[var14 + 1]);
                     }

                     ++var14;
                  } else if (var0[var14].equals("-nosteam")) {
                     System.setProperty("zomboid.steam", "0");
                  } else {
                     DebugLog.log("unknown option \"" + var0[var14] + "\"");
                  }
               }
            } else {
               String[] var15 = var0[var14].replace("-debuglog=", "").split(",");
               int var4 = var15.length;

               for(int var5 = 0; var5 < var4; ++var5) {
                  String var6 = var15[var5];

                  try {
                     char var7 = var6.charAt(0);
                     var6 = var7 != '+' && var7 != '-' ? var6 : var6.substring(1);
                     DebugLog.setLogEnabled(DebugType.valueOf(var6), var7 != '-');
                  } catch (IllegalArgumentException var11) {
                  }
               }
            }
         }
      }

      try {
         RenderThread.init();
         AssetManagers var17 = GameWindow.assetManagers;
         AiSceneAssetManager.instance.create(AiSceneAsset.ASSET_TYPE, var17);
         AnimationAssetManager.instance.create(AnimationAsset.ASSET_TYPE, var17);
         AnimNodeAssetManager.instance.create(AnimNodeAsset.ASSET_TYPE, var17);
         ClothingItemAssetManager.instance.create(ClothingItem.ASSET_TYPE, var17);
         MeshAssetManager.instance.create(ModelMesh.ASSET_TYPE, var17);
         ModelAssetManager.instance.create(Model.ASSET_TYPE, var17);
         TextureIDAssetManager.instance.create(TextureID.ASSET_TYPE, var17);
         TextureAssetManager.instance.create(Texture.ASSET_TYPE, var17);
         WorldMapDataAssetManager.instance.create(WorldMapData.ASSET_TYPE, var17);
         GameWindow.InitGameThread();
         RenderThread.renderLoop();
      } catch (OpenGLException var8) {
         String var10002 = ZomboidFileSystem.instance.getCacheDir();
         File var16 = new File(var10002 + File.separator + "options2.bin");
         var16.delete();
         var8.printStackTrace();
      } catch (Exception var9) {
         var9.printStackTrace();
      }

   }

   public static void DrawTexture(Texture var0, int var1, int var2, int var3, int var4, float var5) {
      SpriteRenderer.instance.renderi(var0, var1, var2, var3, var4, 1.0F, 1.0F, 1.0F, var5, (Consumer)null);
   }

   public static void DrawTexture(Texture var0, int var1, int var2, int var3, int var4, Color var5) {
      SpriteRenderer.instance.renderi(var0, var1, var2, var3, var4, var5.r, var5.g, var5.b, var5.a, (Consumer)null);
   }

   public void enter() {
      DebugLog.log("EXITDEBUG: MainScreenState.enter 1");
      this.Elements.clear();
      this.targetAlpha = 1.0F;
      TextureID.UseFiltering = true;
      this.RestartDebounceClickTimer = 100;
      totalScale = (float)Core.getInstance().getOffscreenHeight(0) / 1080.0F;
      this.lastW = Core.getInstance().getOffscreenWidth(0);
      this.lastH = Core.getInstance().getOffscreenHeight(0);
      this.alpha = 1.0F;
      this.showLogo = false;
      SoundManager.instance.setMusicState("MainMenu");
      int var1 = (int)((float)Core.getInstance().getOffscreenHeight(0) * 0.7F);
      MainScreenState.ScreenElement var2 = new MainScreenState.ScreenElement(Texture.getSharedTexture("media/ui/PZ_Logo.png"), Core.getInstance().getOffscreenWidth(0) / 2 - (int)((float)Texture.getSharedTexture("media/ui/PZ_Logo.png").getWidth() * totalScale) / 2, var1 - (int)(350.0F * totalScale), 0.0F, 0.0F, 1);
      var2.targetAlpha = 1.0F;
      var2.alphaStep *= 0.9F;
      this.Logo = var2;
      this.Elements.add(var2);
      TextureID.UseFiltering = false;
      LuaEventManager.triggerEvent("OnMainMenuEnter");
      instance = this;
      float var3 = TextureID.totalMemUsed / 1024.0F;
      float var4 = var3 / 1024.0F;
      DebugLog.log("EXITDEBUG: MainScreenState.enter 2");
   }

   public static MainScreenState getInstance() {
      return instance;
   }

   public boolean ShouldShowLogo() {
      return this.showLogo;
   }

   public void exit() {
      DebugLog.log("EXITDEBUG: MainScreenState.exit 1");
      DebugLog.log("LOADED UP A TOTAL OF " + Texture.totalTextureID + " TEXTURES");
      float var1 = (float)Core.getInstance().getOptionMusicVolume() / 10.0F;
      long var2 = Calendar.getInstance().getTimeInMillis();

      while(true) {
         this.FadeAlpha = Math.min(1.0F, (float)(Calendar.getInstance().getTimeInMillis() - var2) / 250.0F);
         this.render();
         if (this.FadeAlpha >= 1.0F) {
            SoundManager.instance.stopMusic("");
            SoundManager.instance.setMusicVolume(var1);
            DebugLog.log("EXITDEBUG: MainScreenState.exit 2");
            return;
         }

         try {
            Thread.sleep(33L);
         } catch (Exception var5) {
         }

         SoundManager.instance.Update();
      }
   }

   public void render() {
      this.lightningTime += 1.0F * GameTime.instance.getMultipliedSecondsSinceLastUpdate();
      Core.getInstance().StartFrame();
      Core.getInstance().EndFrame();
      boolean var1 = UIManager.useUIFBO;
      UIManager.useUIFBO = false;
      Core.getInstance().StartFrameUI();
      IndieGL.glBlendFunc(770, 771);
      SpriteRenderer.instance.renderi((Texture)null, 0, 0, Core.getInstance().getScreenWidth(), Core.getInstance().getScreenHeight(), 0.0F, 0.0F, 0.0F, 1.0F, (Consumer)null);
      IndieGL.glBlendFunc(770, 770);
      if (this.lightningTargetDelta == 0.0F && this.lightningDelta != 0.0F && this.lightningDelta < 0.6F && this.lightningCount == 0.0F) {
         this.lightningTargetDelta = 1.0F;
         this.lightningCount = 1.0F;
      }

      if (this.lightningTimelineMarker) {
         this.lightningTimelineMarker = false;
         this.lightningTargetDelta = 1.0F;
      }

      if (this.lightningTargetDelta == 1.0F && this.lightningDelta == 1.0F && (this.lightningFullTimer > 1.0F && this.lightningCount == 0.0F || this.lightningFullTimer > 10.0F)) {
         this.lightningTargetDelta = 0.0F;
         this.lightningFullTimer = 0.0F;
      }

      if (this.lightningTargetDelta == 1.0F && this.lightningDelta == 1.0F) {
         this.lightningFullTimer += GameTime.getInstance().getMultiplier();
      }

      if (this.lightningDelta != this.lightningTargetDelta) {
         if (this.lightningDelta < this.lightningTargetDelta) {
            this.lightningDelta += 0.17F * GameTime.getInstance().getMultiplier();
            if (this.lightningDelta > this.lightningTargetDelta) {
               this.lightningDelta = this.lightningTargetDelta;
               if (this.lightningDelta == 1.0F) {
                  this.showLogo = true;
               }
            }
         }

         if (this.lightningDelta > this.lightningTargetDelta) {
            this.lightningDelta -= 0.025F * GameTime.getInstance().getMultiplier();
            if (this.lightningCount == 0.0F) {
               this.lightningDelta -= 0.1F;
            }

            if (this.lightningDelta < this.lightningTargetDelta) {
               this.lightningDelta = this.lightningTargetDelta;
               this.lightningCount = 0.0F;
            }
         }
      }

      Texture var2 = Texture.getSharedTexture("media/ui/Title.png");
      Texture var3 = Texture.getSharedTexture("media/ui/Title2.png");
      Texture var4 = Texture.getSharedTexture("media/ui/Title3.png");
      Texture var5 = Texture.getSharedTexture("media/ui/Title4.png");
      if (Rand.Next(150) == 0) {
         this.lightOffCount = 10.0F;
      }

      Texture var6 = Texture.getSharedTexture("media/ui/Title_lightning.png");
      Texture var7 = Texture.getSharedTexture("media/ui/Title_lightning2.png");
      Texture var8 = Texture.getSharedTexture("media/ui/Title_lightning3.png");
      Texture var9 = Texture.getSharedTexture("media/ui/Title_lightning4.png");
      float var10 = (float)Core.getInstance().getScreenHeight() / 1080.0F;
      float var11 = (float)var2.getWidth() * var10;
      float var12 = (float)var3.getWidth() * var10;
      float var13 = (float)Core.getInstance().getScreenWidth() - (var11 + var12);
      if (var13 >= 0.0F) {
         var13 = 0.0F;
      }

      float var14 = 1.0F - this.lightningDelta * 0.6F;
      float var15 = 1024.0F * var10;
      float var16 = 56.0F * var10;
      DrawTexture(var2, (int)var13, 0, (int)var11, (int)var15, var14);
      DrawTexture(var3, (int)var13 + (int)var11, 0, (int)var11, (int)var15, var14);
      DrawTexture(var4, (int)var13, (int)var15, (int)var11, (int)((float)var4.getHeight() * var10), var14);
      DrawTexture(var5, (int)var13 + (int)var11, (int)var15, (int)var11, (int)((float)var4.getHeight() * var10), var14);
      IndieGL.glBlendFunc(770, 1);
      DrawTexture(var6, (int)var13, 0, (int)var11, (int)var15, this.lightningDelta);
      DrawTexture(var7, (int)var13 + (int)var11, 0, (int)var11, (int)var15, this.lightningDelta);
      DrawTexture(var8, (int)var13, (int)var15, (int)var11, (int)var15, this.lightningDelta);
      DrawTexture(var9, (int)var13 + (int)var11, (int)var15, (int)var11, (int)var15, this.lightningDelta);
      IndieGL.glBlendFunc(770, 771);
      UIManager.render();
      if (GameWindow.DrawReloadingLua) {
         int var17 = TextManager.instance.MeasureStringX(UIFont.Small, "Reloading Lua") + 32;
         int var18 = TextManager.instance.font.getLineHeight();
         int var19 = (int)Math.ceil((double)var18 * 1.5D);
         SpriteRenderer.instance.renderi((Texture)null, Core.getInstance().getScreenWidth() - var17 - 12, 12, var17, var19, 0.0F, 0.5F, 0.75F, 1.0F, (Consumer)null);
         TextManager.instance.DrawStringCentre((double)(Core.getInstance().getScreenWidth() - var17 / 2 - 12), (double)(12 + (var19 - var18) / 2), "Reloading Lua", 1.0D, 1.0D, 1.0D, 1.0D);
      }

      if (this.FadeAlpha > 0.0F) {
         UIManager.DrawTexture(UIManager.getBlack(), 0.0D, 0.0D, (double)Core.getInstance().getScreenWidth(), (double)Core.getInstance().getScreenHeight(), (double)this.FadeAlpha);
      }

      ActiveMods.renderUI();
      JoypadManager.instance.renderUI();
      Core.getInstance().EndFrameUI();
      UIManager.useUIFBO = var1;
   }

   public GameStateMachine.StateAction update() {
      if (this.connectToServerState != null) {
         GameStateMachine.StateAction var1 = this.connectToServerState.update();
         if (var1 == GameStateMachine.StateAction.Continue) {
            this.connectToServerState.exit();
            this.connectToServerState = null;
            return GameStateMachine.StateAction.Remain;
         }
      }

      LuaEventManager.triggerEvent("OnFETick", BoxedStaticValues.toDouble(0.0D));
      if (this.RestartDebounceClickTimer > 0) {
         --this.RestartDebounceClickTimer;
      }

      for(int var4 = 0; var4 < this.Elements.size(); ++var4) {
         MainScreenState.ScreenElement var2 = (MainScreenState.ScreenElement)this.Elements.get(var4);
         var2.update();
      }

      this.lastW = Core.getInstance().getOffscreenWidth(0);
      this.lastH = Core.getInstance().getOffscreenHeight(0);
      DebugFileWatcher.instance.update();
      ZomboidFileSystem.instance.update();

      try {
         Core.getInstance().CheckDelayResetLua();
      } catch (Exception var3) {
         ExceptionLogger.logException(var3);
      }

      return GameStateMachine.StateAction.Remain;
   }

   public void setConnectToServerState(ConnectToServerState var1) {
      this.connectToServerState = var1;
   }

   public GameState redirectState() {
      return null;
   }

   public static Buffer loadIcons() {
      Buffer var2 = null;
      String var4 = System.getProperty("os.name").toUpperCase(Locale.ENGLISH);
      BufferedImage var0;
      ByteBuffer var1;
      GLFWImage var3;
      if (var4.contains("WIN")) {
         try {
            var2 = GLFWImage.create(2);
            var0 = ImageIO.read((new File("media" + File.separator + "ui" + File.separator + "zomboidIcon16.png")).getAbsoluteFile());
            windowIconBB1 = var1 = loadInstance(var0, 16);
            windowIcon1 = var3 = GLFWImage.create().set(16, 16, var1);
            var2.put(0, var3);
            var0 = ImageIO.read((new File("media" + File.separator + "ui" + File.separator + "zomboidIcon32.png")).getAbsoluteFile());
            windowIconBB2 = var1 = loadInstance(var0, 32);
            windowIcon2 = var3 = GLFWImage.create().set(32, 32, var1);
            var2.put(1, var3);
         } catch (IOException var8) {
            var8.printStackTrace();
         }
      } else if (var4.contains("MAC")) {
         try {
            var2 = GLFWImage.create(1);
            var0 = ImageIO.read((new File("media" + File.separator + "ui" + File.separator + "zomboidIcon128.png")).getAbsoluteFile());
            windowIconBB1 = var1 = loadInstance(var0, 128);
            windowIcon1 = var3 = GLFWImage.create().set(128, 128, var1);
            var2.put(0, var3);
         } catch (IOException var7) {
            var7.printStackTrace();
         }
      } else {
         try {
            var2 = GLFWImage.create(1);
            var0 = ImageIO.read((new File("media" + File.separator + "ui" + File.separator + "zomboidIcon32.png")).getAbsoluteFile());
            windowIconBB1 = var1 = loadInstance(var0, 32);
            windowIcon1 = var3 = GLFWImage.create().set(32, 32, var1);
            var2.put(0, var3);
         } catch (IOException var6) {
            var6.printStackTrace();
         }
      }

      return var2;
   }

   private static ByteBuffer loadInstance(BufferedImage var0, int var1) {
      BufferedImage var2 = new BufferedImage(var1, var1, 3);
      Graphics2D var3 = var2.createGraphics();
      double var4 = getIconRatio(var0, var2);
      double var6 = (double)var0.getWidth() * var4;
      double var8 = (double)var0.getHeight() * var4;
      var3.drawImage(var0, (int)(((double)var2.getWidth() - var6) / 2.0D), (int)(((double)var2.getHeight() - var8) / 2.0D), (int)var6, (int)var8, (ImageObserver)null);
      var3.dispose();
      return convertToByteBuffer(var2);
   }

   private static double getIconRatio(BufferedImage var0, BufferedImage var1) {
      double var2 = 1.0D;
      if (var0.getWidth() > var1.getWidth()) {
         var2 = (double)var1.getWidth() / (double)var0.getWidth();
      } else {
         var2 = (double)(var1.getWidth() / var0.getWidth());
      }

      double var4;
      if (var0.getHeight() > var1.getHeight()) {
         var4 = (double)var1.getHeight() / (double)var0.getHeight();
         if (var4 < var2) {
            var2 = var4;
         }
      } else {
         var4 = (double)(var1.getHeight() / var0.getHeight());
         if (var4 < var2) {
            var2 = var4;
         }
      }

      return var2;
   }

   public static ByteBuffer convertToByteBuffer(BufferedImage var0) {
      byte[] var1 = new byte[var0.getWidth() * var0.getHeight() * 4];
      int var2 = 0;

      for(int var3 = 0; var3 < var0.getHeight(); ++var3) {
         for(int var4 = 0; var4 < var0.getWidth(); ++var4) {
            int var5 = var0.getRGB(var4, var3);
            var1[var2 + 0] = (byte)(var5 << 8 >> 24);
            var1[var2 + 1] = (byte)(var5 << 16 >> 24);
            var1[var2 + 2] = (byte)(var5 << 24 >> 24);
            var1[var2 + 3] = (byte)(var5 >> 24);
            var2 += 4;
         }
      }

      ByteBuffer var6 = ByteBuffer.allocateDirect(var1.length);
      var6.put(var1);
      var6.flip();
      return var6;
   }

   private static void printSpecs() {
      try {
         System.out.println("===== System specs =====");
         long var0 = 1024L;
         long var2 = var0 * 1024L;
         long var4 = var2 * 1024L;
         Map var6 = System.getenv();
         PrintStream var10000 = System.out;
         String var10001 = System.getProperty("os.name");
         var10000.println("OS: " + var10001 + ", version: " + System.getProperty("os.version") + ", arch: " + System.getProperty("os.arch"));
         if (var6.containsKey("PROCESSOR_IDENTIFIER")) {
            System.out.println("Processor: " + (String)var6.get("PROCESSOR_IDENTIFIER"));
         }

         if (var6.containsKey("NUMBER_OF_PROCESSORS")) {
            System.out.println("Processor cores: " + (String)var6.get("NUMBER_OF_PROCESSORS"));
         }

         System.out.println("Available processors (cores): " + Runtime.getRuntime().availableProcessors());
         System.out.println("Memory free: " + (float)Runtime.getRuntime().freeMemory() / (float)var2 + " MB");
         long var7 = Runtime.getRuntime().maxMemory();
         Object var15 = var7 == Long.MAX_VALUE ? "no limit" : (float)var7 / (float)var2;
         System.out.println("Memory max: " + var15 + " MB");
         System.out.println("Memory  total available to JVM: " + (float)Runtime.getRuntime().totalMemory() / (float)var2 + " MB");
         File[] var9 = File.listRoots();
         File[] var10 = var9;
         int var11 = var9.length;

         for(int var12 = 0; var12 < var11; ++var12) {
            File var13 = var10[var12];
            var10000 = System.out;
            var10001 = var13.getAbsolutePath();
            var10000.println(var10001 + ", Total: " + (float)var13.getTotalSpace() / (float)var4 + " GB, Free: " + (float)var13.getFreeSpace() / (float)var4 + " GB");
         }

         if (System.getProperty("os.name").toLowerCase().contains("windows")) {
            var10000 = System.out;
            String[] var10002 = new String[]{"Product"};
            var10000.println("Mobo = " + wmic("baseboard", var10002));
            var10000 = System.out;
            var10002 = new String[]{"Manufacturer", "MaxClockSpeed", "Name"};
            var10000.println("CPU = " + wmic("cpu", var10002));
            var10000 = System.out;
            var10002 = new String[]{"AdapterRAM", "DriverVersion", "Name"};
            var10000.println("Graphics = " + wmic("path Win32_videocontroller", var10002));
            var10000 = System.out;
            var10002 = new String[]{"VideoModeDescription"};
            var10000.println("VideoMode = " + wmic("path Win32_videocontroller", var10002));
            var10000 = System.out;
            var10002 = new String[]{"Manufacturer", "Name"};
            var10000.println("Sound = " + wmic("path Win32_sounddevice", var10002));
            var10000 = System.out;
            var10002 = new String[]{"Capacity", "Manufacturer"};
            var10000.println("Memory RAM = " + wmic("memorychip", var10002));
         }

         System.out.println("========================");
      } catch (Exception var14) {
         var14.printStackTrace();
      }

   }

   private static String wmic(String var0, String[] var1) {
      String var2 = "";

      try {
         String var3 = "WMIC " + var0 + " GET";

         for(int var4 = 0; var4 < var1.length; ++var4) {
            var3 = var3 + " " + var1[var4];
            if (var4 < var1.length - 1) {
               var3 = var3 + ",";
            }
         }

         Process var14 = Runtime.getRuntime().exec(new String[]{"CMD", "/C", var3});
         var14.getOutputStream().close();
         BufferedReader var5 = new BufferedReader(new InputStreamReader(var14.getInputStream()));

         String var6;
         String var7;
         for(var6 = ""; (var7 = var5.readLine()) != null; var6 = var6 + var7) {
         }

         String[] var8 = var1;
         int var9 = var1.length;

         int var10;
         for(var10 = 0; var10 < var9; ++var10) {
            String var11 = var8[var10];
            var6 = var6.replaceAll(var11, "");
         }

         var6 = var6.trim().replaceAll(" ( )+", "=");
         var8 = var6.split("=");
         if (var8.length > var1.length) {
            var2 = "{ ";
            var9 = var8.length / var1.length;

            for(var10 = 0; var10 < var9; ++var10) {
               var2 = var2 + "[";

               for(int var15 = 0; var15 < var1.length; ++var15) {
                  int var12 = var10 * var1.length + var15;
                  var2 = var2 + var1[var15] + "=" + var8[var12];
                  if (var15 < var1.length - 1) {
                     var2 = var2 + ",";
                  }
               }

               var2 = var2 + "]";
               if (var10 < var9 - 1) {
                  var2 = var2 + ", ";
               }
            }

            var2 = var2 + " }";
         } else {
            var2 = "[";

            for(var9 = 0; var9 < var8.length; ++var9) {
               var2 = var2 + var1[var9] + "=" + var8[var9];
               if (var9 < var8.length - 1) {
                  var2 = var2 + ",";
               }
            }

            var2 = var2 + "]";
         }

         return var2;
      } catch (Exception var13) {
         return "Couldnt get info...";
      }
   }

   public static class ScreenElement {
      public float alpha = 0.0F;
      public float alphaStep = 0.2F;
      public boolean jumpBack = true;
      public float sx = 0.0F;
      public float sy = 0.0F;
      public float targetAlpha = 0.0F;
      public Texture tex;
      public int TicksTillTargetAlpha = 0;
      public float x = 0.0F;
      public int xCount = 1;
      public float xVel = 0.0F;
      public float xVelO = 0.0F;
      public float y = 0.0F;
      public float yVel = 0.0F;
      public float yVelO = 0.0F;

      public ScreenElement(Texture var1, int var2, int var3, float var4, float var5, int var6) {
         this.x = this.sx = (float)var2;
         this.y = this.sy = (float)var3 - (float)var1.getHeight() * MainScreenState.totalScale;
         this.xVel = var4;
         this.yVel = var5;
         this.tex = var1;
         this.xCount = var6;
      }

      public void render() {
         int var1 = (int)this.x;
         int var2 = (int)this.y;

         for(int var3 = 0; var3 < this.xCount; ++var3) {
            MainScreenState.DrawTexture(this.tex, var1, var2, (int)((float)this.tex.getWidth() * MainScreenState.totalScale), (int)((float)this.tex.getHeight() * MainScreenState.totalScale), this.alpha);
            var1 = (int)((float)var1 + (float)this.tex.getWidth() * MainScreenState.totalScale);
         }

         TextManager.instance.DrawStringRight((double)(Core.getInstance().getOffscreenWidth(0) - 5), (double)(Core.getInstance().getOffscreenHeight(0) - 15), "Version: " + MainScreenState.Version, 1.0D, 1.0D, 1.0D, 1.0D);
      }

      public void setY(float var1) {
         this.y = this.sy = var1 - (float)this.tex.getHeight() * MainScreenState.totalScale;
      }

      public void update() {
         this.x += this.xVel * MainScreenState.totalScale;
         this.y += this.yVel * MainScreenState.totalScale;
         --this.TicksTillTargetAlpha;
         if (this.TicksTillTargetAlpha <= 0) {
            this.targetAlpha = 1.0F;
         }

         if (this.jumpBack && this.sx - this.x > (float)this.tex.getWidth() * MainScreenState.totalScale) {
            this.x += (float)this.tex.getWidth() * MainScreenState.totalScale;
         }

         if (this.alpha < this.targetAlpha) {
            this.alpha += this.alphaStep;
            if (this.alpha > this.targetAlpha) {
               this.alpha = this.targetAlpha;
            }
         } else if (this.alpha > this.targetAlpha) {
            this.alpha -= this.alphaStep;
            if (this.alpha < this.targetAlpha) {
               this.alpha = this.targetAlpha;
            }
         }

      }
   }

   public class Credit {
      public int disappearDelay = 200;
      public Texture name;
      public float nameAlpha = 0.0F;
      public float nameAppearDelay = 40.0F;
      public float nameTargetAlpha = 0.0F;
      public Texture title;
      public float titleAlpha = 0.0F;
      public float titleTargetAlpha = 1.0F;

      public Credit(Texture var2, Texture var3) {
         this.title = var2;
         this.name = var3;
      }
   }
}

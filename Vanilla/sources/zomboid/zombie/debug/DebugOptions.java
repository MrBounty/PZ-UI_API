package zombie.debug;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import zombie.DebugFileWatcher;
import zombie.GameWindow;
import zombie.PredicatedFileWatcher;
import zombie.ZomboidFileSystem;
import zombie.config.ConfigFile;
import zombie.config.ConfigOption;
import zombie.core.Core;
import zombie.core.logger.ExceptionLogger;
import zombie.core.opengl.RenderThread;
import zombie.debug.options.Animation;
import zombie.debug.options.Character;
import zombie.debug.options.IDebugOption;
import zombie.debug.options.IDebugOptionGroup;
import zombie.debug.options.IsoSprite;
import zombie.debug.options.Network;
import zombie.debug.options.OffscreenBuffer;
import zombie.debug.options.OptionGroup;
import zombie.debug.options.Terrain;
import zombie.debug.options.Weather;
import zombie.gameStates.GameLoadingState;
import zombie.util.PZXmlParserException;
import zombie.util.PZXmlUtil;
import zombie.util.Type;
import zombie.util.list.PZArrayUtil;

public final class DebugOptions implements IDebugOptionGroup {
   public static final int VERSION = 1;
   public static final DebugOptions instance = new DebugOptions();
   private final ArrayList options = new ArrayList();
   private final ArrayList m_options = new ArrayList();
   public final BooleanDebugOption AssetSlowLoad = this.newOption("Asset.SlowLoad", false);
   public final BooleanDebugOption MultiplayerShowZombieMultiplier = this.newDebugOnlyOption("Multiplayer.Debug.ZombieMultiplier", false);
   public final BooleanDebugOption MultiplayerShowZombieOwner = this.newDebugOnlyOption("Multiplayer.Debug.ZombieOwner", false);
   public final BooleanDebugOption MultiplayerShowPosition = this.newDebugOnlyOption("Multiplayer.Debug.Position", false);
   public final BooleanDebugOption MultiplayerShowTeleport = this.newDebugOnlyOption("Multiplayer.Debug.Teleport", false);
   public final BooleanDebugOption MultiplayerShowHit = this.newDebugOnlyOption("Multiplayer.Debug.Hit", false);
   public final BooleanDebugOption MultiplayerLogPrediction = this.newDebugOnlyOption("Multiplayer.Debug.LogPrediction", false);
   public final BooleanDebugOption MultiplayerShowPlayerPrediction = this.newDebugOnlyOption("Multiplayer.Debug.PlayerPrediction", false);
   public final BooleanDebugOption MultiplayerShowPlayerStatus = this.newDebugOnlyOption("Multiplayer.Debug.PlayerStatus", false);
   public final BooleanDebugOption MultiplayerShowZombiePrediction = this.newDebugOnlyOption("Multiplayer.Debug.ZombiePrediction", false);
   public final BooleanDebugOption MultiplayerShowZombieDesync = this.newDebugOnlyOption("Multiplayer.Debug.ZombieDesync", false);
   public final BooleanDebugOption MultiplayerShowZombieStatus = this.newDebugOnlyOption("Multiplayer.Debug.ZombieStatus", false);
   public final BooleanDebugOption MultiplayerLightAmbient = this.newDebugOnlyOption("Multiplayer.Debug.LightAmbient", false);
   public final BooleanDebugOption MultiplayerCriticalHit = this.newDebugOnlyOption("Multiplayer.Debug.CriticalHit", false);
   public final BooleanDebugOption MultiplayerTorsoHit = this.newOption("Multiplayer.Debug.TorsoHit", false);
   public final BooleanDebugOption MultiplayerZombieCrawler = this.newDebugOnlyOption("Multiplayer.Debug.ZombieCrawler", false);
   public final BooleanDebugOption MultiplayerSpawnZombie = this.newDebugOnlyOption("Multiplayer.Debug.SpawnZombie", false);
   public final BooleanDebugOption MultiplayerPlayerZombie = this.newDebugOnlyOption("Multiplayer.Debug.PlayerZombie", false);
   public final BooleanDebugOption MultiplayerAttackPlayer = this.newDebugOnlyOption("Multiplayer.Debug.Attack.Player", false);
   public final BooleanDebugOption MultiplayerFollowPlayer = this.newDebugOnlyOption("Multiplayer.Debug.Follow.Player", false);
   public final BooleanDebugOption MultiplayerAutoEquip = this.newDebugOnlyOption("Multiplayer.Debug.AutoEquip", false);
   public final BooleanDebugOption MultiplayerPing = this.newOption("Multiplayer.Debug.Ping", false);
   public final BooleanDebugOption CheatClockVisible = this.newDebugOnlyOption("Cheat.Clock.Visible", false);
   public final BooleanDebugOption CheatDoorUnlock = this.newDebugOnlyOption("Cheat.Door.Unlock", false);
   public final BooleanDebugOption CheatPlayerStartInvisible = this.newDebugOnlyOption("Cheat.Player.StartInvisible", false);
   public final BooleanDebugOption CheatPlayerInvisibleSprint = this.newDebugOnlyOption("Cheat.Player.InvisibleSprint", false);
   public final BooleanDebugOption CheatPlayerSeeEveryone = this.newDebugOnlyOption("Cheat.Player.SeeEveryone", false);
   public final BooleanDebugOption CheatUnlimitedAmmo = this.newDebugOnlyOption("Cheat.Player.UnlimitedAmmo", false);
   public final BooleanDebugOption CheatRecipeKnowAll = this.newDebugOnlyOption("Cheat.Recipe.KnowAll", false);
   public final BooleanDebugOption CheatTimedActionInstant = this.newDebugOnlyOption("Cheat.TimedAction.Instant", false);
   public final BooleanDebugOption CheatVehicleMechanicsAnywhere = this.newDebugOnlyOption("Cheat.Vehicle.MechanicsAnywhere", false);
   public final BooleanDebugOption CheatVehicleStartWithoutKey = this.newDebugOnlyOption("Cheat.Vehicle.StartWithoutKey", false);
   public final BooleanDebugOption CheatWindowUnlock = this.newDebugOnlyOption("Cheat.Window.Unlock", false);
   public final BooleanDebugOption CollideWithObstaclesRenderRadius = this.newOption("CollideWithObstacles.Render.Radius", false);
   public final BooleanDebugOption CollideWithObstaclesRenderObstacles = this.newOption("CollideWithObstacles.Render.Obstacles", false);
   public final BooleanDebugOption CollideWithObstaclesRenderNormals = this.newOption("CollideWithObstacles.Render.Normals", false);
   public final BooleanDebugOption DeadBodyAtlasRender = this.newOption("DeadBodyAtlas.Render", false);
   public final BooleanDebugOption DebugScenarioForceLaunch = this.newOption("DebugScenario.ForceLaunch", false);
   public final BooleanDebugOption MechanicsRenderHitbox = this.newOption("Mechanics.Render.Hitbox", false);
   public final BooleanDebugOption JoypadRenderUI = this.newDebugOnlyOption("Joypad.Render.UI", false);
   public final BooleanDebugOption ModelRenderAttachments = this.newOption("Model.Render.Attachments", false);
   public final BooleanDebugOption ModelRenderAxis = this.newOption("Model.Render.Axis", false);
   public final BooleanDebugOption ModelRenderBones = this.newOption("Model.Render.Bones", false);
   public final BooleanDebugOption ModelRenderBounds = this.newOption("Model.Render.Bounds", false);
   public final BooleanDebugOption ModelRenderLights = this.newOption("Model.Render.Lights", false);
   public final BooleanDebugOption ModelRenderMuzzleflash = this.newOption("Model.Render.Muzzleflash", false);
   public final BooleanDebugOption ModelRenderSkipVehicles = this.newOption("Model.Render.SkipVehicles", false);
   public final BooleanDebugOption ModelRenderWeaponHitPoint = this.newOption("Model.Render.WeaponHitPoint", false);
   public final BooleanDebugOption ModelRenderWireframe = this.newOption("Model.Render.Wireframe", false);
   public final BooleanDebugOption ModelSkeleton = this.newOption("Model.Force.Skeleton", false);
   public final BooleanDebugOption ModRenderLoaded = this.newDebugOnlyOption("Mod.Render.Loaded", false);
   public final BooleanDebugOption PathfindPathToMouseAllowCrawl = this.newOption("Pathfind.PathToMouse.AllowCrawl", false);
   public final BooleanDebugOption PathfindPathToMouseAllowThump = this.newOption("Pathfind.PathToMouse.AllowThump", false);
   public final BooleanDebugOption PathfindPathToMouseEnable = this.newOption("Pathfind.PathToMouse.Enable", false);
   public final BooleanDebugOption PathfindPathToMouseIgnoreCrawlCost = this.newOption("Pathfind.PathToMouse.IgnoreCrawlCost", false);
   public final BooleanDebugOption PathfindRenderPath = this.newOption("Pathfind.Render.Path", false);
   public final BooleanDebugOption PathfindRenderWaiting = this.newOption("Pathfind.Render.Waiting", false);
   public final BooleanDebugOption PhysicsRender = this.newOption("Physics.Render", false);
   public final BooleanDebugOption PolymapRenderClusters = this.newOption("Pathfind.Render.Clusters", false);
   public final BooleanDebugOption PolymapRenderConnections = this.newOption("Pathfind.Render.Connections", false);
   public final BooleanDebugOption PolymapRenderCrawling = this.newOption("Pathfind.Render.Crawling", false);
   public final BooleanDebugOption PolymapRenderLineClearCollide = this.newOption("Pathfind.Render.LineClearCollide", false);
   public final BooleanDebugOption PolymapRenderNodes = this.newOption("Pathfind.Render.Nodes", false);
   public final BooleanDebugOption TooltipInfo = this.newOption("Tooltip.Info", false);
   public final BooleanDebugOption TooltipModName = this.newDebugOnlyOption("Tooltip.ModName", false);
   public final BooleanDebugOption TranslationPrefix = this.newOption("Translation.Prefix", false);
   public final BooleanDebugOption UIRenderOutline = this.newOption("UI.Render.Outline", false);
   public final BooleanDebugOption UIDebugConsoleStartVisible = this.newOption("UI.DebugConsole.StartVisible", true);
   public final BooleanDebugOption UIDebugConsoleDebugLog = this.newOption("UI.DebugConsole.DebugLog", true);
   public final BooleanDebugOption UIDebugConsoleEchoCommand = this.newOption("UI.DebugConsole.EchoCommand", true);
   public final BooleanDebugOption VehicleCycleColor = this.newDebugOnlyOption("Vehicle.CycleColor", false);
   public final BooleanDebugOption VehicleRenderBlood0 = this.newDebugOnlyOption("Vehicle.Render.Blood0", false);
   public final BooleanDebugOption VehicleRenderBlood50 = this.newDebugOnlyOption("Vehicle.Render.Blood50", false);
   public final BooleanDebugOption VehicleRenderBlood100 = this.newDebugOnlyOption("Vehicle.Render.Blood100", false);
   public final BooleanDebugOption VehicleRenderDamage0 = this.newDebugOnlyOption("Vehicle.Render.Damage0", false);
   public final BooleanDebugOption VehicleRenderDamage1 = this.newDebugOnlyOption("Vehicle.Render.Damage1", false);
   public final BooleanDebugOption VehicleRenderDamage2 = this.newDebugOnlyOption("Vehicle.Render.Damage2", false);
   public final BooleanDebugOption VehicleRenderRust0 = this.newDebugOnlyOption("Vehicle.Render.Rust0", false);
   public final BooleanDebugOption VehicleRenderRust50 = this.newDebugOnlyOption("Vehicle.Render.Rust50", false);
   public final BooleanDebugOption VehicleRenderRust100 = this.newDebugOnlyOption("Vehicle.Render.Rust100", false);
   public final BooleanDebugOption VehicleRenderOutline = this.newOption("Vehicle.Render.Outline", false);
   public final BooleanDebugOption VehicleRenderArea = this.newOption("Vehicle.Render.Area", false);
   public final BooleanDebugOption VehicleRenderAuthorizations = this.newOption("Vehicle.Render.Authorizations", false);
   public final BooleanDebugOption VehicleRenderAttackPositions = this.newOption("Vehicle.Render.AttackPositions", false);
   public final BooleanDebugOption VehicleRenderExit = this.newOption("Vehicle.Render.Exit", false);
   public final BooleanDebugOption VehicleRenderIntersectedSquares = this.newOption("Vehicle.Render.IntersectedSquares", false);
   public final BooleanDebugOption VehicleRenderTrailerPositions = this.newDebugOnlyOption("Vehicle.Render.TrailerPositions", false);
   public final BooleanDebugOption VehicleSpawnEverywhere = this.newDebugOnlyOption("Vehicle.Spawn.Everywhere", false);
   public final BooleanDebugOption WorldSoundRender = this.newOption("WorldSound.Render", false);
   public final BooleanDebugOption LightingRender = this.newOption("Lighting.Render", false);
   public final BooleanDebugOption SkyboxShow = this.newOption("Skybox.Show", false);
   public final BooleanDebugOption WorldStreamerSlowLoad = this.newOption("WorldStreamer.SlowLoad", false);
   public final BooleanDebugOption DebugDraw_SkipVBODraw = this.newOption("DebugDraw.SkipVBODraw", false);
   public final BooleanDebugOption DebugDraw_SkipDrawNonSkinnedModel = this.newOption("DebugDraw.SkipDrawNonSkinnedModel", false);
   public final BooleanDebugOption DebugDraw_SkipWorldShading = this.newOption("DebugDraw.SkipWorldShading", false);
   public final BooleanDebugOption GameProfilerEnabled = this.newOption("GameProfiler.Enabled", false);
   public final BooleanDebugOption GameTimeSpeedHalf = this.newOption("GameTime.Speed.Half", false);
   public final BooleanDebugOption GameTimeSpeedQuarter = this.newOption("GameTime.Speed.Quarter", false);
   public final BooleanDebugOption ThreadCrash_Enabled = this.newDebugOnlyOption("ThreadCrash.Enable", false);
   public final BooleanDebugOption[] ThreadCrash_GameThread = new BooleanDebugOption[]{this.newDebugOnlyOption("ThreadCrash.MainThread.0", false), this.newDebugOnlyOption("ThreadCrash.MainThread.1", false), this.newDebugOnlyOption("ThreadCrash.MainThread.2", false)};
   public final BooleanDebugOption[] ThreadCrash_GameLoadingThread = new BooleanDebugOption[]{this.newDebugOnlyOption("ThreadCrash.GameLoadingThread.0", false)};
   public final BooleanDebugOption[] ThreadCrash_RenderThread = new BooleanDebugOption[]{this.newDebugOnlyOption("ThreadCrash.RenderThread.0", false), this.newDebugOnlyOption("ThreadCrash.RenderThread.1", false), this.newDebugOnlyOption("ThreadCrash.RenderThread.2", false)};
   public final BooleanDebugOption WorldChunkMap5x5 = this.newDebugOnlyOption("World.ChunkMap.5x5", false);
   public final BooleanDebugOption ZombieRenderCanCrawlUnderVehicle = this.newDebugOnlyOption("Zombie.Render.CanCrawlUnderVehicle", false);
   public final BooleanDebugOption ZombieRenderFakeDead = this.newDebugOnlyOption("Zombie.Render.FakeDead", false);
   public final BooleanDebugOption ZombieRenderMemory = this.newDebugOnlyOption("Zombie.Render.Memory", false);
   public final BooleanDebugOption ZombieOutfitRandom = this.newDebugOnlyOption("Zombie.Outfit.Random", false);
   public final DebugOptions.Checks Checks = (DebugOptions.Checks)this.newOptionGroup(new DebugOptions.Checks());
   public final IsoSprite IsoSprite = (IsoSprite)this.newOptionGroup(new IsoSprite());
   public final Network Network = (Network)this.newOptionGroup(new Network());
   public final OffscreenBuffer OffscreenBuffer = (OffscreenBuffer)this.newOptionGroup(new OffscreenBuffer());
   public final Terrain Terrain = (Terrain)this.newOptionGroup(new Terrain());
   public final Weather Weather = (Weather)this.newOptionGroup(new Weather());
   public final Animation Animation = (Animation)this.newOptionGroup(new Animation());
   public final Character Character = (Character)this.newOptionGroup(new Character());
   private static PredicatedFileWatcher m_triggerWatcher;

   public void init() {
      this.load();
      this.initMessaging();
   }

   private void initMessaging() {
      if (m_triggerWatcher == null) {
         m_triggerWatcher = new PredicatedFileWatcher(ZomboidFileSystem.instance.getMessagingDirSub("Trigger_SetDebugOptions.xml"), this::onTrigger_SetDebugOptions);
         DebugFileWatcher.instance.add(m_triggerWatcher);
      }

      DebugOptionsXml var1 = new DebugOptionsXml();
      var1.setDebugMode = true;
      var1.debugMode = Core.bDebug;
      Iterator var2 = this.options.iterator();

      while(var2.hasNext()) {
         BooleanDebugOption var3 = (BooleanDebugOption)var2.next();
         var1.options.add(new DebugOptionsXml.OptionNode(var3.getName(), var3.getValue()));
      }

      String var4 = ZomboidFileSystem.instance.getMessagingDirSub("DebugOptions_list.xml");
      PZXmlUtil.tryWrite((Object)var1, new File(var4));
   }

   private void onTrigger_SetDebugOptions(String var1) {
      try {
         DebugOptionsXml var2 = (DebugOptionsXml)PZXmlUtil.parse(DebugOptionsXml.class, ZomboidFileSystem.instance.getMessagingDirSub("Trigger_SetDebugOptions.xml"));
         Iterator var3 = var2.options.iterator();

         while(var3.hasNext()) {
            DebugOptionsXml.OptionNode var4 = (DebugOptionsXml.OptionNode)var3.next();
            this.setBoolean(var4.name, var4.value);
         }

         if (var2.setDebugMode) {
            DebugLog.General.println("DebugMode: %s", var2.debugMode ? "ON" : "OFF");
            Core.bDebug = var2.debugMode;
         }
      } catch (PZXmlParserException var5) {
         ExceptionLogger.logException(var5, "Exception thrown parsing Trigger_SetDebugOptions.xml");
      }

   }

   public Iterable getChildren() {
      return PZArrayUtil.listConvert(this.options, (var0) -> {
         return var0;
      });
   }

   public void addChild(IDebugOption var1) {
      this.m_options.add(var1);
      var1.setParent(this);
      this.onChildAdded(var1);
   }

   public void onChildAdded(IDebugOption var1) {
      this.onDescendantAdded(var1);
   }

   public void onDescendantAdded(IDebugOption var1) {
      this.addOption(var1);
   }

   private void addOption(IDebugOption var1) {
      BooleanDebugOption var2 = (BooleanDebugOption)Type.tryCastTo(var1, BooleanDebugOption.class);
      if (var2 != null) {
         this.options.add(var2);
      }

      IDebugOptionGroup var3 = (IDebugOptionGroup)Type.tryCastTo(var1, IDebugOptionGroup.class);
      if (var3 != null) {
         this.addDescendantOptions(var3);
      }

   }

   private void addDescendantOptions(IDebugOptionGroup var1) {
      Iterator var2 = var1.getChildren().iterator();

      while(var2.hasNext()) {
         IDebugOption var3 = (IDebugOption)var2.next();
         this.addOption(var3);
      }

   }

   public String getName() {
      return "DebugOptions";
   }

   public IDebugOptionGroup getParent() {
      return null;
   }

   public void setParent(IDebugOptionGroup var1) {
      throw new UnsupportedOperationException("DebugOptions is a root not. Cannot have a parent.");
   }

   private BooleanDebugOption newOption(String var1, boolean var2) {
      BooleanDebugOption var3 = OptionGroup.newOption(var1, var2);
      this.addChild(var3);
      return var3;
   }

   private BooleanDebugOption newDebugOnlyOption(String var1, boolean var2) {
      BooleanDebugOption var3 = OptionGroup.newDebugOnlyOption(var1, var2);
      this.addChild(var3);
      return var3;
   }

   private IDebugOptionGroup newOptionGroup(IDebugOptionGroup var1) {
      this.addChild(var1);
      return var1;
   }

   public BooleanDebugOption getOptionByName(String var1) {
      for(int var2 = 0; var2 < this.options.size(); ++var2) {
         BooleanDebugOption var3 = (BooleanDebugOption)this.options.get(var2);
         if (var3.getName().equals(var1)) {
            return var3;
         }
      }

      return null;
   }

   public int getOptionCount() {
      return this.options.size();
   }

   public BooleanDebugOption getOptionByIndex(int var1) {
      return (BooleanDebugOption)this.options.get(var1);
   }

   public void setBoolean(String var1, boolean var2) {
      BooleanDebugOption var3 = this.getOptionByName(var1);
      if (var3 != null) {
         var3.setValue(var2);
      }

   }

   public boolean getBoolean(String var1) {
      BooleanDebugOption var2 = this.getOptionByName(var1);
      return var2 != null ? var2.getValue() : false;
   }

   public void save() {
      String var1 = ZomboidFileSystem.instance.getCacheDirSub("debug-options.ini");
      ConfigFile var2 = new ConfigFile();
      var2.write(var1, 1, this.options);
   }

   public void load() {
      String var1 = ZomboidFileSystem.instance.getCacheDirSub("debug-options.ini");
      ConfigFile var2 = new ConfigFile();
      if (var2.read(var1)) {
         for(int var3 = 0; var3 < var2.getOptions().size(); ++var3) {
            ConfigOption var4 = (ConfigOption)var2.getOptions().get(var3);
            BooleanDebugOption var5 = this.getOptionByName(var4.getName());
            if (var5 != null) {
               var5.parse(var4.getValueAsString());
            }
         }
      }

   }

   public static void testThreadCrash(int var0) {
      instance.testThreadCrashInternal(var0);
   }

   private void testThreadCrashInternal(int var1) {
      if (Core.bDebug) {
         if (this.ThreadCrash_Enabled.getValue()) {
            Thread var2 = Thread.currentThread();
            BooleanDebugOption[] var3;
            if (var2 == RenderThread.RenderThread) {
               var3 = this.ThreadCrash_RenderThread;
            } else if (var2 == GameWindow.GameThread) {
               var3 = this.ThreadCrash_GameThread;
            } else {
               if (var2 != GameLoadingState.loader) {
                  return;
               }

               var3 = this.ThreadCrash_GameLoadingThread;
            }

            if (var3[var1].getValue()) {
               throw new Error("ThreadCrash Test! " + var2.getName());
            }
         }
      }
   }

   public static final class Checks extends OptionGroup {
      public final BooleanDebugOption BoundTextures;
      public final BooleanDebugOption SlowLuaEvents;

      public Checks() {
         super("Checks");
         this.BoundTextures = newDebugOnlyOption(this.Group, "BoundTextures", false);
         this.SlowLuaEvents = newDebugOnlyOption(this.Group, "SlowLuaEvents", false);
      }
   }
}

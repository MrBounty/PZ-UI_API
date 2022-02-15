package zombie.iso;

import fmod.fmod.FMODSoundEmitter;
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;
import se.krka.kahlua.vm.KahluaTable;
import zombie.CollisionManager;
import zombie.DebugFileWatcher;
import zombie.FliesSound;
import zombie.GameTime;
import zombie.GameWindow;
import zombie.MapCollisionData;
import zombie.PersistentOutfits;
import zombie.PredicatedFileWatcher;
import zombie.ReanimatedPlayers;
import zombie.SandboxOptions;
import zombie.SharedDescriptors;
import zombie.SoundManager;
import zombie.SystemDisabler;
import zombie.VirtualZombieManager;
import zombie.WorldSoundManager;
import zombie.ZomboidFileSystem;
import zombie.ZomboidGlobals;
import zombie.Lua.LuaEventManager;
import zombie.Lua.LuaManager;
import zombie.ai.ZombieGroupManager;
import zombie.ai.states.FakeDeadZombieState;
import zombie.audio.BaseSoundEmitter;
import zombie.audio.DummySoundEmitter;
import zombie.characters.HaloTextHelper;
import zombie.characters.IsoGameCharacter;
import zombie.characters.IsoPlayer;
import zombie.characters.IsoSurvivor;
import zombie.characters.IsoZombie;
import zombie.characters.SurvivorDesc;
import zombie.characters.TriggerSetAnimationRecorderFile;
import zombie.characters.professions.ProfessionFactory;
import zombie.characters.traits.TraitFactory;
import zombie.core.Core;
import zombie.core.PerformanceSettings;
import zombie.core.SpriteRenderer;
import zombie.core.TilePropertyAliasMap;
import zombie.core.Translator;
import zombie.core.logger.ExceptionLogger;
import zombie.core.physics.WorldSimulation;
import zombie.core.profiling.PerformanceProfileProbe;
import zombie.core.skinnedmodel.DeadBodyAtlas;
import zombie.core.stash.StashSystem;
import zombie.core.textures.Texture;
import zombie.core.utils.OnceEvery;
import zombie.debug.DebugLog;
import zombie.debug.LineDrawer;
import zombie.erosion.ErosionGlobals;
import zombie.gameStates.GameLoadingState;
import zombie.globalObjects.GlobalObjectLookup;
import zombie.input.Mouse;
import zombie.inventory.ItemPickerJava;
import zombie.inventory.types.MapItem;
import zombie.iso.SpriteDetails.IsoFlagType;
import zombie.iso.SpriteDetails.IsoObjectType;
import zombie.iso.areas.IsoBuilding;
import zombie.iso.areas.SafeHouse;
import zombie.iso.areas.isoregion.IsoRegions;
import zombie.iso.objects.IsoDeadBody;
import zombie.iso.objects.IsoFireManager;
import zombie.iso.objects.ObjectRenderEffects;
import zombie.iso.objects.RainManager;
import zombie.iso.sprite.IsoDirectionFrame;
import zombie.iso.sprite.IsoSprite;
import zombie.iso.sprite.IsoSpriteGrid;
import zombie.iso.sprite.IsoSpriteManager;
import zombie.iso.sprite.SkyBox;
import zombie.iso.weather.ClimateManager;
import zombie.iso.weather.WorldFlares;
import zombie.iso.weather.fog.ImprovedFog;
import zombie.iso.weather.fx.WeatherFxMask;
import zombie.network.BodyDamageSync;
import zombie.network.ClientServerMap;
import zombie.network.GameClient;
import zombie.network.GameServer;
import zombie.network.NetChecksum;
import zombie.network.PassengerMap;
import zombie.network.ServerMap;
import zombie.network.ServerOptions;
import zombie.popman.ZombiePopulationManager;
import zombie.radio.ZomboidRadio;
import zombie.randomizedWorld.randomizedBuilding.RBBar;
import zombie.randomizedWorld.randomizedBuilding.RBBasic;
import zombie.randomizedWorld.randomizedBuilding.RBBurnt;
import zombie.randomizedWorld.randomizedBuilding.RBBurntCorpse;
import zombie.randomizedWorld.randomizedBuilding.RBBurntFireman;
import zombie.randomizedWorld.randomizedBuilding.RBCafe;
import zombie.randomizedWorld.randomizedBuilding.RBClinic;
import zombie.randomizedWorld.randomizedBuilding.RBHairSalon;
import zombie.randomizedWorld.randomizedBuilding.RBKateAndBaldspot;
import zombie.randomizedWorld.randomizedBuilding.RBLooted;
import zombie.randomizedWorld.randomizedBuilding.RBOffice;
import zombie.randomizedWorld.randomizedBuilding.RBOther;
import zombie.randomizedWorld.randomizedBuilding.RBPileOCrepe;
import zombie.randomizedWorld.randomizedBuilding.RBPizzaWhirled;
import zombie.randomizedWorld.randomizedBuilding.RBSafehouse;
import zombie.randomizedWorld.randomizedBuilding.RBSchool;
import zombie.randomizedWorld.randomizedBuilding.RBShopLooted;
import zombie.randomizedWorld.randomizedBuilding.RBSpiffo;
import zombie.randomizedWorld.randomizedBuilding.RBStripclub;
import zombie.randomizedWorld.randomizedBuilding.RandomizedBuildingBase;
import zombie.randomizedWorld.randomizedVehicleStory.RVSAmbulanceCrash;
import zombie.randomizedWorld.randomizedVehicleStory.RVSBanditRoad;
import zombie.randomizedWorld.randomizedVehicleStory.RVSBurntCar;
import zombie.randomizedWorld.randomizedVehicleStory.RVSCarCrash;
import zombie.randomizedWorld.randomizedVehicleStory.RVSCarCrashCorpse;
import zombie.randomizedWorld.randomizedVehicleStory.RVSChangingTire;
import zombie.randomizedWorld.randomizedVehicleStory.RVSConstructionSite;
import zombie.randomizedWorld.randomizedVehicleStory.RVSCrashHorde;
import zombie.randomizedWorld.randomizedVehicleStory.RVSFlippedCrash;
import zombie.randomizedWorld.randomizedVehicleStory.RVSPoliceBlockade;
import zombie.randomizedWorld.randomizedVehicleStory.RVSPoliceBlockadeShooting;
import zombie.randomizedWorld.randomizedVehicleStory.RVSTrailerCrash;
import zombie.randomizedWorld.randomizedVehicleStory.RVSUtilityVehicle;
import zombie.randomizedWorld.randomizedVehicleStory.RandomizedVehicleStoryBase;
import zombie.randomizedWorld.randomizedZoneStory.RZSBBQParty;
import zombie.randomizedWorld.randomizedZoneStory.RZSBaseball;
import zombie.randomizedWorld.randomizedZoneStory.RZSBeachParty;
import zombie.randomizedWorld.randomizedZoneStory.RZSBuryingCamp;
import zombie.randomizedWorld.randomizedZoneStory.RZSFishingTrip;
import zombie.randomizedWorld.randomizedZoneStory.RZSForestCamp;
import zombie.randomizedWorld.randomizedZoneStory.RZSForestCampEaten;
import zombie.randomizedWorld.randomizedZoneStory.RZSHunterCamp;
import zombie.randomizedWorld.randomizedZoneStory.RZSMusicFest;
import zombie.randomizedWorld.randomizedZoneStory.RZSMusicFestStage;
import zombie.randomizedWorld.randomizedZoneStory.RZSSexyTime;
import zombie.randomizedWorld.randomizedZoneStory.RZSTrapperCamp;
import zombie.savefile.ClientPlayerDB;
import zombie.savefile.PlayerDB;
import zombie.savefile.PlayerDBHelper;
import zombie.savefile.ServerPlayerDB;
import zombie.ui.TutorialManager;
import zombie.util.AddCoopPlayer;
import zombie.util.SharedStrings;
import zombie.util.Type;
import zombie.vehicles.PolygonalMap2;
import zombie.vehicles.VehicleIDMap;
import zombie.vehicles.VehicleManager;
import zombie.vehicles.VehiclesDB2;
import zombie.world.WorldDictionary;
import zombie.world.WorldDictionaryException;
import zombie.world.moddata.GlobalModData;

public final class IsoWorld {
   private String weather = "sunny";
   public final IsoMetaGrid MetaGrid = new IsoMetaGrid();
   private final ArrayList randomizedBuildingList = new ArrayList();
   private final ArrayList randomizedZoneList = new ArrayList();
   private final ArrayList randomizedVehicleStoryList = new ArrayList();
   private final RandomizedBuildingBase RBBasic = new RBBasic();
   private final HashMap spawnedZombieZone = new HashMap();
   private final HashMap allTiles = new HashMap();
   private float flashIsoCursorA = 1.0F;
   private boolean flashIsoCursorInc = false;
   public SkyBox sky = null;
   private static PredicatedFileWatcher m_setAnimationRecordingTriggerWatcher;
   private static boolean m_animationRecorderActive = false;
   private static boolean m_animationRecorderDiscard = false;
   private int timeSinceLastSurvivorInHorde = 4000;
   private int m_frameNo = 0;
   public final Helicopter helicopter = new Helicopter();
   public final ArrayList Characters = new ArrayList();
   private final ArrayDeque freeEmitters = new ArrayDeque();
   private final ArrayList currentEmitters = new ArrayList();
   private final HashMap emitterOwners = new HashMap();
   public int x = 50;
   public int y = 50;
   public IsoCell CurrentCell;
   public static IsoWorld instance = new IsoWorld();
   public int TotalSurvivorsDead = 0;
   public int TotalSurvivorNights = 0;
   public int SurvivorSurvivalRecord = 0;
   public HashMap SurvivorDescriptors = new HashMap();
   public ArrayList AddCoopPlayers = new ArrayList();
   private static final IsoWorld.CompScoreToPlayer compScoreToPlayer = new IsoWorld.CompScoreToPlayer();
   static IsoWorld.CompDistToPlayer compDistToPlayer = new IsoWorld.CompDistToPlayer();
   public static String mapPath = "media/";
   public static boolean mapUseJar = true;
   boolean bLoaded = false;
   public static final HashMap PropertyValueMap = new HashMap();
   private static int WorldX = 0;
   private static int WorldY = 0;
   private SurvivorDesc luaDesc;
   private ArrayList luatraits;
   private int luaSpawnCellX = -1;
   private int luaSpawnCellY = -1;
   private int luaPosX = -1;
   private int luaPosY = -1;
   private int luaPosZ = -1;
   public static final int WorldVersion = 186;
   public static final int WorldVersion_Barricade = 87;
   public static final int WorldVersion_SandboxOptions = 88;
   public static final int WorldVersion_FliesSound = 121;
   public static final int WorldVersion_LootRespawn = 125;
   public static final int WorldVersion_OverlappingGenerators = 127;
   public static final int WorldVersion_ItemContainerIdenticalItems = 128;
   public static final int WorldVersion_VehicleSirenStartTime = 129;
   public static final int WorldVersion_CompostLastUpdated = 130;
   public static final int WorldVersion_DayLengthHours = 131;
   public static final int WorldVersion_LampOnPillar = 132;
   public static final int WorldVersion_AlarmClockRingSince = 134;
   public static final int WorldVersion_ClimateAdded = 135;
   public static final int WorldVersion_VehicleLightFocusing = 135;
   public static final int WorldVersion_GeneratorFuelFloat = 138;
   public static final int WorldVersion_InfectionTime = 142;
   public static final int WorldVersion_ClimateColors = 143;
   public static final int WorldVersion_BodyLocation = 144;
   public static final int WorldVersion_CharacterModelData = 145;
   public static final int WorldVersion_CharacterModelData2 = 146;
   public static final int WorldVersion_CharacterModelData3 = 147;
   public static final int WorldVersion_HumanVisualBlood = 148;
   public static final int WorldVersion_ItemContainerIdenticalItemsInt = 149;
   public static final int WorldVersion_PerkName = 152;
   public static final int WorldVersion_Thermos = 153;
   public static final int WorldVersion_AllPatches = 155;
   public static final int WorldVersion_ZombieRotStage = 156;
   public static final int WorldVersion_NewSandboxLootModifier = 157;
   public static final int WorldVersion_KateBobStorm = 158;
   public static final int WorldVersion_DeadBodyAngle = 159;
   public static final int WorldVersion_ChunkSpawnedRooms = 160;
   public static final int WorldVersion_DeathDragDown = 161;
   public static final int WorldVersion_CanUpgradePerk = 162;
   public static final int WorldVersion_ItemVisualFullType = 164;
   public static final int WorldVersion_VehicleBlood = 165;
   public static final int WorldVersion_DeadBodyZombieRotStage = 166;
   public static final int WorldVersion_Fitness = 167;
   public static final int WorldVersion_DeadBodyFakeDead = 168;
   public static final int WorldVersion_Fitness2 = 169;
   public static final int WorldVersion_NewFog = 170;
   public static final int WorldVersion_DeadBodyPersistentOutfitID = 171;
   public static final int WorldVersion_VehicleTowingID = 172;
   public static final int WorldVersion_VehicleJNITransform = 173;
   public static final int WorldVersion_VehicleTowAttachment = 174;
   public static final int WorldVersion_ContainerMaxCapacity = 175;
   public static final int WorldVersion_TimedActionInstantCheat = 176;
   public static final int WorldVersion_ClothingPatchSaveLoad = 178;
   public static final int WorldVersion_AttachedSlotType = 179;
   public static final int WorldVersion_NoiseMakerDuration = 180;
   public static final int WorldVersion_ChunkVehicles = 91;
   public static final int WorldVersion_PlayerVehicleSeat = 91;
   public static final int WorldVersion_MediaDisksAndTapes = 181;
   public static final int WorldVersion_AlreadyReadBooks1 = 182;
   public static final int WorldVersion_LampOnPillar2 = 183;
   public static final int WorldVersion_AlreadyReadBooks2 = 184;
   public static final int WorldVersion_PolygonZone = 185;
   public static final int WorldVersion_PolylineZone = 186;
   public static int SavedWorldVersion = -1;
   private boolean bDrawWorld = true;
   private final ArrayList zombieWithModel = new ArrayList();
   private final ArrayList zombieWithoutModel = new ArrayList();
   public static boolean NoZombies = false;
   public static int TotalWorldVersion = -1;
   public static int saveoffsetx;
   public static int saveoffsety;
   public boolean bDoChunkMapUpdate = true;
   private long emitterUpdateMS;
   public boolean emitterUpdate;

   public IsoMetaGrid getMetaGrid() {
      return this.MetaGrid;
   }

   public IsoMetaGrid.Zone registerZone(String var1, String var2, int var3, int var4, int var5, int var6, int var7) {
      return this.MetaGrid.registerZone(var1, var2, var3, var4, var5, var6, var7);
   }

   public IsoMetaGrid.Zone registerZoneNoOverlap(String var1, String var2, int var3, int var4, int var5, int var6, int var7) {
      return this.MetaGrid.registerZoneNoOverlap(var1, var2, var3, var4, var5, var6, var7);
   }

   public void removeZonesForLotDirectory(String var1) {
      this.MetaGrid.removeZonesForLotDirectory(var1);
   }

   public BaseSoundEmitter getFreeEmitter() {
      Object var1 = null;
      if (this.freeEmitters.isEmpty()) {
         var1 = Core.SoundDisabled ? new DummySoundEmitter() : new FMODSoundEmitter();
      } else {
         var1 = (BaseSoundEmitter)this.freeEmitters.pop();
      }

      this.currentEmitters.add(var1);
      return (BaseSoundEmitter)var1;
   }

   public BaseSoundEmitter getFreeEmitter(float var1, float var2, float var3) {
      BaseSoundEmitter var4 = this.getFreeEmitter();
      var4.setPos(var1, var2, var3);
      return var4;
   }

   public void takeOwnershipOfEmitter(BaseSoundEmitter var1) {
      this.currentEmitters.remove(var1);
   }

   public void setEmitterOwner(BaseSoundEmitter var1, IsoObject var2) {
      if (var1 != null && var2 != null) {
         if (!this.emitterOwners.containsKey(var1)) {
            this.emitterOwners.put(var1, var2);
         }
      }
   }

   public void returnOwnershipOfEmitter(BaseSoundEmitter var1) {
      if (var1 != null) {
         if (!this.currentEmitters.contains(var1) && !this.freeEmitters.contains(var1)) {
            if (var1.isEmpty()) {
               FMODSoundEmitter var2 = (FMODSoundEmitter)Type.tryCastTo(var1, FMODSoundEmitter.class);
               if (var2 != null) {
                  var2.clearParameters();
               }

               this.freeEmitters.add(var1);
            } else {
               this.currentEmitters.add(var1);
            }

         }
      }
   }

   public IsoMetaGrid.Zone registerVehiclesZone(String var1, String var2, int var3, int var4, int var5, int var6, int var7, KahluaTable var8) {
      return this.MetaGrid.registerVehiclesZone(var1, var2, var3, var4, var5, var6, var7, var8);
   }

   public IsoMetaGrid.Zone registerMannequinZone(String var1, String var2, int var3, int var4, int var5, int var6, int var7, KahluaTable var8) {
      return this.MetaGrid.registerMannequinZone(var1, var2, var3, var4, var5, var6, var7, var8);
   }

   public void registerSpawnOrigin(int var1, int var2, int var3, int var4, KahluaTable var5) {
      ZombiePopulationManager.instance.registerSpawnOrigin(var1, var2, var3, var4, var5);
   }

   public void registerWaterFlow(float var1, float var2, float var3, float var4) {
      IsoWaterFlow.addFlow(var1, var2, var3, var4);
   }

   public void registerWaterZone(float var1, float var2, float var3, float var4, float var5, float var6) {
      IsoWaterFlow.addZone(var1, var2, var3, var4, var5, var6);
   }

   public void checkVehiclesZones() {
      this.MetaGrid.checkVehiclesZones();
   }

   public void setGameMode(String var1) {
      Core.GameMode = var1;
      Core.bLastStand = "LastStand".equals(var1);
      Core.getInstance().setChallenge(false);
      Core.ChallengeID = null;
   }

   public String getGameMode() {
      return Core.GameMode;
   }

   public void setWorld(String var1) {
      Core.GameSaveWorld = var1.trim();
   }

   public void setMap(String var1) {
      Core.GameMap = var1;
   }

   public String getMap() {
      return Core.GameMap;
   }

   public void renderTerrain() {
   }

   public int getFrameNo() {
      return this.m_frameNo;
   }

   public IsoObject getItemFromXYZIndexBuffer(ByteBuffer var1) {
      int var2 = var1.getInt();
      int var3 = var1.getInt();
      int var4 = var1.getInt();
      IsoGridSquare var5 = this.CurrentCell.getGridSquare(var2, var3, var4);
      if (var5 == null) {
         return null;
      } else {
         byte var6 = var1.get();
         return var6 >= 0 && var6 < var5.getObjects().size() ? (IsoObject)var5.getObjects().get(var6) : null;
      }
   }

   public IsoWorld() {
      if (!GameServer.bServer) {
      }

   }

   private static void initMessaging() {
      if (m_setAnimationRecordingTriggerWatcher == null) {
         m_setAnimationRecordingTriggerWatcher = new PredicatedFileWatcher(ZomboidFileSystem.instance.getMessagingDirSub("Trigger_AnimationRecorder.xml"), TriggerSetAnimationRecorderFile.class, IsoWorld::onTrigger_setAnimationRecorderTriggerFile);
         DebugFileWatcher.instance.add(m_setAnimationRecordingTriggerWatcher);
      }

   }

   private static void onTrigger_setAnimationRecorderTriggerFile(TriggerSetAnimationRecorderFile var0) {
      m_animationRecorderActive = var0.isRecording;
      m_animationRecorderDiscard = var0.discard;
   }

   public static boolean isAnimRecorderActive() {
      return m_animationRecorderActive;
   }

   public static boolean isAnimRecorderDiscardTriggered() {
      return m_animationRecorderDiscard;
   }

   public IsoSurvivor CreateRandomSurvivor(SurvivorDesc var1, IsoGridSquare var2, IsoPlayer var3) {
      return null;
   }

   public void CreateSwarm(int var1, int var2, int var3, int var4, int var5) {
   }

   public void ForceKillAllZombies() {
      GameTime.getInstance().RemoveZombiesIndiscriminate(1000);
   }

   public static int readInt(RandomAccessFile var0) throws EOFException, IOException {
      int var1 = var0.read();
      int var2 = var0.read();
      int var3 = var0.read();
      int var4 = var0.read();
      if ((var1 | var2 | var3 | var4) < 0) {
         throw new EOFException();
      } else {
         return (var1 << 0) + (var2 << 8) + (var3 << 16) + (var4 << 24);
      }
   }

   public static String readString(RandomAccessFile var0) throws EOFException, IOException {
      String var1 = var0.readLine();
      return var1;
   }

   public static int readInt(InputStream var0) throws EOFException, IOException {
      int var1 = var0.read();
      int var2 = var0.read();
      int var3 = var0.read();
      int var4 = var0.read();
      if ((var1 | var2 | var3 | var4) < 0) {
         throw new EOFException();
      } else {
         return (var1 << 0) + (var2 << 8) + (var3 << 16) + (var4 << 24);
      }
   }

   public static String readString(InputStream var0) throws IOException {
      StringBuilder var1 = new StringBuilder();
      int var2 = -1;
      boolean var3 = false;

      while(!var3) {
         switch(var2 = var0.read()) {
         case -1:
         case 10:
            var3 = true;
            break;
         case 13:
            throw new IllegalStateException("\r\n unsupported");
         default:
            var1.append((char)var2);
         }
      }

      if (var2 == -1 && var1.length() == 0) {
         return null;
      } else {
         return var1.toString();
      }
   }

   public void LoadTileDefinitions(IsoSpriteManager var1, String var2, int var3) {
      DebugLog.log("tiledef: loading " + var2);
      boolean var4 = var2.endsWith(".patch.tiles");

      try {
         FileInputStream var5 = new FileInputStream(var2);

         try {
            BufferedInputStream var6 = new BufferedInputStream(var5);

            try {
               int var7 = readInt((InputStream)var6);
               int var8 = readInt((InputStream)var6);
               int var9 = readInt((InputStream)var6);
               SharedStrings var10 = new SharedStrings();
               boolean var11 = false;
               boolean var12 = false;
               ArrayList var13 = new ArrayList();
               HashMap var14 = new HashMap();
               HashMap var15 = new HashMap();
               String[] var16 = new String[]{"N", "E", "S", "W"};

               for(int var17 = 0; var17 < var16.length; ++var17) {
                  var15.put(var16[var17], new ArrayList());
               }

               ArrayList var56 = new ArrayList();
               HashMap var18 = new HashMap();
               int var19 = 0;
               int var20 = 0;
               int var21 = 0;
               int var22 = 0;
               HashSet var23 = new HashSet();
               int var24 = 0;

               label699:
               while(true) {
                  String var26;
                  if (var24 >= var9) {
                     if (var12) {
                        ArrayList var57 = new ArrayList(var23);
                        Collections.sort(var57);
                        Iterator var58 = var57.iterator();

                        while(var58.hasNext()) {
                           var26 = (String)var58.next();
                           PrintStream var85 = System.out;
                           String var10001 = var26.replaceAll(" ", "_").replaceAll("-", "_").replaceAll("'", "").replaceAll("\\.", "");
                           var85.println(var10001 + " = \"" + var26 + "\",");
                        }
                     }

                     if (var11) {
                        try {
                           this.saveMovableStats(var18, var3, var20, var21, var22, var19);
                        } catch (Exception var52) {
                        }
                     }
                     break;
                  }

                  String var25 = readString((InputStream)var6);
                  var26 = var25.trim();
                  String var27 = readString((InputStream)var6);
                  int var28 = readInt((InputStream)var6);
                  int var29 = readInt((InputStream)var6);
                  int var30 = readInt((InputStream)var6);
                  int var31 = readInt((InputStream)var6);

                  IsoSprite var33;
                  int var39;
                  for(int var32 = 0; var32 < var31; ++var32) {
                     if (var4) {
                        var33 = (IsoSprite)var1.NamedMap.get(var26 + "_" + var32);
                        if (var33 == null) {
                           continue;
                        }
                     } else if (var3 < 2) {
                        var33 = var1.AddSprite(var26 + "_" + var32, var3 * 100 * 1000 + 10000 + var30 * 1000 + var32);
                     } else {
                        var33 = var1.AddSprite(var26 + "_" + var32, var3 * 512 * 512 + var30 * 512 + var32);
                     }

                     if (Core.bDebug) {
                        if (this.allTiles.containsKey(var26)) {
                           if (!var4) {
                              ((ArrayList)this.allTiles.get(var26)).add(var26 + "_" + var32);
                           }
                        } else {
                           ArrayList var34 = new ArrayList();
                           var34.add(var26 + "_" + var32);
                           this.allTiles.put(var26, var34);
                        }
                     }

                     var13.add(var33);
                     if (!var4) {
                        var33.setName(var26 + "_" + var32);
                        var33.tileSheetIndex = var32;
                     }

                     if (var33.name.contains("damaged") || var33.name.contains("trash_")) {
                        var33.attachedFloor = true;
                        var33.getProperties().Set("attachedFloor", "true");
                     }

                     if (var33.name.startsWith("f_bushes") && var32 <= 31) {
                        var33.isBush = true;
                        var33.attachedFloor = true;
                     }

                     int var60 = readInt((InputStream)var6);

                     for(int var35 = 0; var35 < var60; ++var35) {
                        var25 = readString((InputStream)var6);
                        String var36 = var25.trim();
                        var25 = readString((InputStream)var6);
                        String var37 = var25.trim();
                        IsoObjectType var38 = IsoObjectType.FromString(var36);
                        if (var38 == IsoObjectType.MAX) {
                           var36 = var10.get(var36);
                           if (var36.equals("firerequirement")) {
                              var33.firerequirement = Integer.parseInt(var37);
                           } else if (var36.equals("fireRequirement")) {
                              var33.firerequirement = Integer.parseInt(var37);
                           } else if (var36.equals("BurntTile")) {
                              var33.burntTile = var37;
                           } else if (var36.equals("ForceAmbient")) {
                              var33.forceAmbient = true;
                              var33.getProperties().Set(var36, var37);
                           } else if (var36.equals("solidfloor")) {
                              var33.solidfloor = true;
                              var33.getProperties().Set(var36, var37);
                           } else if (var36.equals("canBeRemoved")) {
                              var33.canBeRemoved = true;
                              var33.getProperties().Set(var36, var37);
                           } else if (var36.equals("attachedFloor")) {
                              var33.attachedFloor = true;
                              var33.getProperties().Set(var36, var37);
                           } else if (var36.equals("cutW")) {
                              var33.cutW = true;
                              var33.getProperties().Set(var36, var37);
                           } else if (var36.equals("cutN")) {
                              var33.cutN = true;
                              var33.getProperties().Set(var36, var37);
                           } else if (var36.equals("solid")) {
                              var33.solid = true;
                              var33.getProperties().Set(var36, var37);
                           } else if (var36.equals("solidTrans")) {
                              var33.solidTrans = true;
                              var33.getProperties().Set(var36, var37);
                           } else if (var36.equals("invisible")) {
                              var33.invisible = true;
                              var33.getProperties().Set(var36, var37);
                           } else if (var36.equals("alwaysDraw")) {
                              var33.alwaysDraw = true;
                              var33.getProperties().Set(var36, var37);
                           } else if (var36.equals("forceRender")) {
                              var33.forceRender = true;
                              var33.getProperties().Set(var36, var37);
                           } else if ("FloorHeight".equals(var36)) {
                              if ("OneThird".equals(var37)) {
                                 var33.getProperties().Set(IsoFlagType.FloorHeightOneThird);
                              } else if ("TwoThirds".equals(var37)) {
                                 var33.getProperties().Set(IsoFlagType.FloorHeightTwoThirds);
                              }
                           } else if (var36.equals("MoveWithWind")) {
                              var33.moveWithWind = true;
                              var33.getProperties().Set(var36, var37);
                           } else if (var36.equals("WindType")) {
                              var33.windType = Integer.parseInt(var37);
                              var33.getProperties().Set(var36, var37);
                           } else if (var36.equals("RenderLayer")) {
                              var33.getProperties().Set(var36, var37);
                              if ("Default".equals(var37)) {
                                 var33.renderLayer = 0;
                              } else if ("Floor".equals(var37)) {
                                 var33.renderLayer = 1;
                              }
                           } else if (var36.equals("TreatAsWallOrder")) {
                              var33.treatAsWallOrder = true;
                              var33.getProperties().Set(var36, var37);
                           } else {
                              var33.getProperties().Set(var36, var37);
                              if ("WindowN".equals(var36) || "WindowW".equals(var36)) {
                                 var33.getProperties().Set(var36, var37, false);
                              }
                           }
                        } else {
                           if (var33.getType() != IsoObjectType.doorW && var33.getType() != IsoObjectType.doorN || var38 != IsoObjectType.wall) {
                              var33.setType(var38);
                           }

                           if (var38 == IsoObjectType.doorW) {
                              var33.getProperties().Set(IsoFlagType.doorW);
                           } else if (var38 == IsoObjectType.doorN) {
                              var33.getProperties().Set(IsoFlagType.doorN);
                           }
                        }

                        if (var38 == IsoObjectType.tree) {
                           if (var33.name.equals("e_riverbirch_1_1")) {
                              var37 = "1";
                           }

                           var33.getProperties().Set("tree", var37);
                           var33.getProperties().UnSet(IsoFlagType.solid);
                           var33.getProperties().Set(IsoFlagType.blocksight);
                           var39 = Integer.parseInt(var37);
                           if (var26.startsWith("vegetation_trees")) {
                              var39 = 4;
                           }

                           if (var39 < 1) {
                              var39 = 1;
                           }

                           if (var39 > 4) {
                              var39 = 4;
                           }

                           if (var39 == 1 || var39 == 2) {
                              var33.getProperties().UnSet(IsoFlagType.blocksight);
                           }
                        }

                        if (var36.equals("interior") && var37.equals("false")) {
                           var33.getProperties().Set(IsoFlagType.exterior);
                        }

                        if (var36.equals("HoppableN")) {
                           var33.getProperties().Set(IsoFlagType.collideN);
                           var33.getProperties().Set(IsoFlagType.canPathN);
                           var33.getProperties().Set(IsoFlagType.transparentN);
                        }

                        if (var36.equals("HoppableW")) {
                           var33.getProperties().Set(IsoFlagType.collideW);
                           var33.getProperties().Set(IsoFlagType.canPathW);
                           var33.getProperties().Set(IsoFlagType.transparentW);
                        }

                        if (var36.equals("WallN")) {
                           var33.getProperties().Set(IsoFlagType.collideN);
                           var33.getProperties().Set(IsoFlagType.cutN);
                           var33.setType(IsoObjectType.wall);
                           var33.cutN = true;
                           var33.getProperties().Set("WallN", "", false);
                        }

                        if (var36.equals("CantClimb")) {
                           var33.getProperties().Set(IsoFlagType.CantClimb);
                        } else if (var36.equals("container")) {
                           var33.getProperties().Set(var36, var37, false);
                        } else if (var36.equals("WallNTrans")) {
                           var33.getProperties().Set(IsoFlagType.collideN);
                           var33.getProperties().Set(IsoFlagType.cutN);
                           var33.getProperties().Set(IsoFlagType.transparentN);
                           var33.setType(IsoObjectType.wall);
                           var33.cutN = true;
                           var33.getProperties().Set("WallNTrans", "", false);
                        } else if (var36.equals("WallW")) {
                           var33.getProperties().Set(IsoFlagType.collideW);
                           var33.getProperties().Set(IsoFlagType.cutW);
                           var33.setType(IsoObjectType.wall);
                           var33.cutW = true;
                           var33.getProperties().Set("WallW", "", false);
                        } else if (var36.equals("windowN")) {
                           var33.getProperties().Set("WindowN", "WindowN");
                           var33.getProperties().Set(IsoFlagType.transparentN);
                           var33.getProperties().Set("WindowN", "WindowN", false);
                        } else if (var36.equals("windowW")) {
                           var33.getProperties().Set("WindowW", "WindowW");
                           var33.getProperties().Set(IsoFlagType.transparentW);
                           var33.getProperties().Set("WindowW", "WindowW", false);
                        } else if (var36.equals("cutW")) {
                           var33.getProperties().Set(IsoFlagType.cutW);
                           var33.cutW = true;
                        } else if (var36.equals("cutN")) {
                           var33.getProperties().Set(IsoFlagType.cutN);
                           var33.cutN = true;
                        } else if (var36.equals("WallWTrans")) {
                           var33.getProperties().Set(IsoFlagType.collideW);
                           var33.getProperties().Set(IsoFlagType.transparentW);
                           var33.getProperties().Set(IsoFlagType.cutW);
                           var33.setType(IsoObjectType.wall);
                           var33.cutW = true;
                           var33.getProperties().Set("WallWTrans", "", false);
                        } else if (var36.equals("DoorWallN")) {
                           var33.getProperties().Set(IsoFlagType.cutN);
                           var33.cutN = true;
                           var33.getProperties().Set("DoorWallN", "", false);
                        } else if (var36.equals("DoorWallW")) {
                           var33.getProperties().Set(IsoFlagType.cutW);
                           var33.cutW = true;
                           var33.getProperties().Set("DoorWallW", "", false);
                        } else if (var36.equals("WallNW")) {
                           var33.getProperties().Set(IsoFlagType.collideN);
                           var33.getProperties().Set(IsoFlagType.cutN);
                           var33.getProperties().Set(IsoFlagType.collideW);
                           var33.getProperties().Set(IsoFlagType.cutW);
                           var33.setType(IsoObjectType.wall);
                           var33.cutW = true;
                           var33.cutN = true;
                           var33.getProperties().Set("WallNW", "", false);
                        } else if (var36.equals("WallNWTrans")) {
                           var33.getProperties().Set(IsoFlagType.collideN);
                           var33.getProperties().Set(IsoFlagType.cutN);
                           var33.getProperties().Set(IsoFlagType.collideW);
                           var33.getProperties().Set(IsoFlagType.transparentN);
                           var33.getProperties().Set(IsoFlagType.transparentW);
                           var33.getProperties().Set(IsoFlagType.cutW);
                           var33.setType(IsoObjectType.wall);
                           var33.cutW = true;
                           var33.cutN = true;
                           var33.getProperties().Set("WallNWTrans", "", false);
                        } else if (var36.equals("WallSE")) {
                           var33.getProperties().Set(IsoFlagType.cutW);
                           var33.getProperties().Set(IsoFlagType.WallSE);
                           var33.getProperties().Set("WallSE", "WallSE");
                           var33.cutW = true;
                        } else if (var36.equals("WindowW")) {
                           var33.getProperties().Set(IsoFlagType.canPathW);
                           var33.getProperties().Set(IsoFlagType.collideW);
                           var33.getProperties().Set(IsoFlagType.cutW);
                           var33.getProperties().Set(IsoFlagType.transparentW);
                           var33.setType(IsoObjectType.windowFW);
                           if (var33.getProperties().Is(IsoFlagType.HoppableW)) {
                              if (Core.bDebug) {
                                 DebugLog.log("ERROR: WindowW sprite shouldn't have HoppableW (" + var33.getName() + ")");
                              }

                              var33.getProperties().UnSet(IsoFlagType.HoppableW);
                           }

                           var33.cutW = true;
                        } else if (var36.equals("WindowN")) {
                           var33.getProperties().Set(IsoFlagType.canPathN);
                           var33.getProperties().Set(IsoFlagType.collideN);
                           var33.getProperties().Set(IsoFlagType.cutN);
                           var33.getProperties().Set(IsoFlagType.transparentN);
                           var33.setType(IsoObjectType.windowFN);
                           if (var33.getProperties().Is(IsoFlagType.HoppableN)) {
                              if (Core.bDebug) {
                                 DebugLog.log("ERROR: WindowN sprite shouldn't have HoppableN (" + var33.getName() + ")");
                              }

                              var33.getProperties().UnSet(IsoFlagType.HoppableN);
                           }

                           var33.cutN = true;
                        } else if (var36.equals("UnbreakableWindowW")) {
                           var33.getProperties().Set(IsoFlagType.canPathW);
                           var33.getProperties().Set(IsoFlagType.collideW);
                           var33.getProperties().Set(IsoFlagType.cutW);
                           var33.getProperties().Set(IsoFlagType.transparentW);
                           var33.getProperties().Set(IsoFlagType.collideW);
                           var33.setType(IsoObjectType.wall);
                           var33.cutW = true;
                        } else if (var36.equals("UnbreakableWindowN")) {
                           var33.getProperties().Set(IsoFlagType.canPathN);
                           var33.getProperties().Set(IsoFlagType.collideN);
                           var33.getProperties().Set(IsoFlagType.cutN);
                           var33.getProperties().Set(IsoFlagType.transparentN);
                           var33.getProperties().Set(IsoFlagType.collideN);
                           var33.setType(IsoObjectType.wall);
                           var33.cutN = true;
                        } else if (var36.equals("UnbreakableWindowNW")) {
                           var33.getProperties().Set(IsoFlagType.cutN);
                           var33.getProperties().Set(IsoFlagType.transparentN);
                           var33.getProperties().Set(IsoFlagType.collideN);
                           var33.getProperties().Set(IsoFlagType.cutN);
                           var33.getProperties().Set(IsoFlagType.collideW);
                           var33.getProperties().Set(IsoFlagType.cutW);
                           var33.setType(IsoObjectType.wall);
                           var33.cutW = true;
                           var33.cutN = true;
                        } else if ("NoWallLighting".equals(var36)) {
                           var33.getProperties().Set(IsoFlagType.NoWallLighting);
                        } else if ("ForceAmbient".equals(var36)) {
                           var33.getProperties().Set(IsoFlagType.ForceAmbient);
                        }

                        if (var36.equals("name")) {
                           var33.setParentObjectName(var37);
                        }
                     }

                     if (var33.getProperties().Is("lightR") || var33.getProperties().Is("lightG") || var33.getProperties().Is("lightB")) {
                        if (!var33.getProperties().Is("lightR")) {
                           var33.getProperties().Set("lightR", "0");
                        }

                        if (!var33.getProperties().Is("lightG")) {
                           var33.getProperties().Set("lightG", "0");
                        }

                        if (!var33.getProperties().Is("lightB")) {
                           var33.getProperties().Set("lightB", "0");
                        }
                     }

                     var33.getProperties().CreateKeySet();
                     if (Core.bDebug && var33.getProperties().Is("SmashedTileOffset") && !var33.getProperties().Is("GlassRemovedOffset")) {
                        DebugLog.General.error("Window sprite has SmashedTileOffset but no GlassRemovedOffset (" + var33.getName() + ")");
                     }
                  }

                  var14.clear();
                  Iterator var59 = var13.iterator();

                  while(true) {
                     while(true) {
                        String var62;
                        do {
                           if (!var59.hasNext()) {
                              var59 = var14.entrySet().iterator();

                              while(true) {
                                 while(true) {
                                    while(true) {
                                       String var41;
                                       ArrayList var63;
                                       boolean var65;
                                       int var66;
                                       Iterator var68;
                                       boolean var69;
                                       IsoSprite var74;
                                       do {
                                          if (!var59.hasNext()) {
                                             var13.clear();
                                             ++var24;
                                             continue label699;
                                          }

                                          Entry var61 = (Entry)var59.next();
                                          var62 = (String)var61.getKey();
                                          if (!var18.containsKey(var26)) {
                                             var18.put(var26, new ArrayList());
                                          }

                                          if (!((ArrayList)var18.get(var26)).contains(var62)) {
                                             ((ArrayList)var18.get(var26)).add(var62);
                                          }

                                          var63 = (ArrayList)var61.getValue();
                                          if (var63.size() == 1) {
                                             DebugLog.log("MOVABLES: Object has only one face defined for group: (" + var62 + ") sheet = " + var26);
                                          }

                                          if (var63.size() == 3) {
                                             DebugLog.log("MOVABLES: Object only has 3 sprites, _might_ have a error in settings, group: (" + var62 + ") sheet = " + var26);
                                          }

                                          String[] var64 = var16;
                                          int var67 = var16.length;

                                          for(var66 = 0; var66 < var67; ++var66) {
                                             String var72 = var64[var66];
                                             ((ArrayList)var15.get(var72)).clear();
                                          }

                                          var65 = ((IsoSprite)var63.get(0)).getProperties().Is("SpriteGridPos") && !((IsoSprite)var63.get(0)).getProperties().Val("SpriteGridPos").equals("None");
                                          var69 = true;
                                          var68 = var63.iterator();

                                          while(var68.hasNext()) {
                                             var74 = (IsoSprite)var68.next();
                                             boolean var40 = var74.getProperties().Is("SpriteGridPos") && !var74.getProperties().Val("SpriteGridPos").equals("None");
                                             if (var65 != var40) {
                                                var69 = false;
                                                DebugLog.log("MOVABLES: Difference in SpriteGrid settings for members of group: (" + var62 + ") sheet = " + var26);
                                                break;
                                             }

                                             if (!var74.getProperties().Is("Facing")) {
                                                var69 = false;
                                             } else {
                                                var41 = var74.getProperties().Val("Facing");
                                                byte var42 = -1;
                                                switch(var41.hashCode()) {
                                                case 69:
                                                   if (var41.equals("E")) {
                                                      var42 = 1;
                                                   }
                                                   break;
                                                case 78:
                                                   if (var41.equals("N")) {
                                                      var42 = 0;
                                                   }
                                                   break;
                                                case 83:
                                                   if (var41.equals("S")) {
                                                      var42 = 2;
                                                   }
                                                   break;
                                                case 87:
                                                   if (var41.equals("W")) {
                                                      var42 = 3;
                                                   }
                                                }

                                                switch(var42) {
                                                case 0:
                                                   ((ArrayList)var15.get("N")).add(var74);
                                                   break;
                                                case 1:
                                                   ((ArrayList)var15.get("E")).add(var74);
                                                   break;
                                                case 2:
                                                   ((ArrayList)var15.get("S")).add(var74);
                                                   break;
                                                case 3:
                                                   ((ArrayList)var15.get("W")).add(var74);
                                                   break;
                                                default:
                                                   DebugLog.log("MOVABLES: Invalid face (" + var74.getProperties().Val("Facing") + ") for group: (" + var62 + ") sheet = " + var26);
                                                   var69 = false;
                                                }
                                             }

                                             if (!var69) {
                                                DebugLog.log("MOVABLES: Not all members have a valid face defined for group: (" + var62 + ") sheet = " + var26);
                                                break;
                                             }
                                          }
                                       } while(!var69);

                                       int var70;
                                       int var79;
                                       if (!var65) {
                                          if (var63.size() > 4) {
                                             DebugLog.log("MOVABLES: Object has too many faces defined for group: (" + var62 + ") sheet = " + var26);
                                          } else {
                                             String[] var71 = var16;
                                             var39 = var16.length;

                                             for(var70 = 0; var70 < var39; ++var70) {
                                                var41 = var71[var70];
                                                if (((ArrayList)var15.get(var41)).size() > 1) {
                                                   DebugLog.log("MOVABLES: " + var41 + " face defined more than once for group: (" + var62 + ") sheet = " + var26);
                                                   var69 = false;
                                                }
                                             }

                                             if (var69) {
                                                ++var21;
                                                var68 = var63.iterator();

                                                while(var68.hasNext()) {
                                                   var74 = (IsoSprite)var68.next();
                                                   String[] var77 = var16;
                                                   int var78 = var16.length;

                                                   for(var79 = 0; var79 < var78; ++var79) {
                                                      String var80 = var77[var79];
                                                      ArrayList var83 = (ArrayList)var15.get(var80);
                                                      if (var83.size() > 0 && var83.get(0) != var74) {
                                                         var74.getProperties().Set(var80 + "offset", Integer.toString(var13.indexOf(var83.get(0)) - var13.indexOf(var74)));
                                                      }
                                                   }
                                                }
                                             }
                                          }
                                       } else {
                                          var66 = 0;
                                          IsoSpriteGrid[] var76 = new IsoSpriteGrid[var16.length];

                                          int var43;
                                          IsoSprite var45;
                                          for(var70 = 0; var70 < var16.length; ++var70) {
                                             ArrayList var73 = (ArrayList)var15.get(var16[var70]);
                                             if (var73.size() > 0) {
                                                if (var66 == 0) {
                                                   var66 = var73.size();
                                                }

                                                if (var66 != var73.size()) {
                                                   DebugLog.log("MOVABLES: Sprite count mismatch for multi sprite movable, group: (" + var62 + ") sheet = " + var26);
                                                   var69 = false;
                                                   break;
                                                }

                                                var56.clear();
                                                var79 = -1;
                                                var43 = -1;
                                                Iterator var44 = var73.iterator();

                                                String var46;
                                                String[] var47;
                                                int var48;
                                                int var49;
                                                while(var44.hasNext()) {
                                                   var45 = (IsoSprite)var44.next();
                                                   var46 = var45.getProperties().Val("SpriteGridPos");
                                                   if (!var56.contains(var46)) {
                                                      var56.add(var46);
                                                      var47 = var46.split(",");
                                                      if (var47.length == 2) {
                                                         var48 = Integer.parseInt(var47[0]);
                                                         var49 = Integer.parseInt(var47[1]);
                                                         if (var48 > var79) {
                                                            var79 = var48;
                                                         }

                                                         if (var49 > var43) {
                                                            var43 = var49;
                                                         }
                                                         continue;
                                                      }

                                                      DebugLog.log("MOVABLES: SpriteGrid position error for multi sprite movable, group: (" + var62 + ") sheet = " + var26);
                                                      var69 = false;
                                                      break;
                                                   }

                                                   DebugLog.log("MOVABLES: double SpriteGrid position (" + var46 + ") for multi sprite movable, group: (" + var62 + ") sheet = " + var26);
                                                   var69 = false;
                                                   break;
                                                }

                                                if (var79 == -1 || var43 == -1 || (var79 + 1) * (var43 + 1) != var73.size()) {
                                                   DebugLog.log("MOVABLES: SpriteGrid dimensions error for multi sprite movable, group: (" + var62 + ") sheet = " + var26);
                                                   var69 = false;
                                                   break;
                                                }

                                                if (!var69) {
                                                   break;
                                                }

                                                var76[var70] = new IsoSpriteGrid(var79 + 1, var43 + 1);
                                                var44 = var73.iterator();

                                                while(var44.hasNext()) {
                                                   var45 = (IsoSprite)var44.next();
                                                   var46 = var45.getProperties().Val("SpriteGridPos");
                                                   var47 = var46.split(",");
                                                   var48 = Integer.parseInt(var47[0]);
                                                   var49 = Integer.parseInt(var47[1]);
                                                   var76[var70].setSprite(var48, var49, var45);
                                                }

                                                if (!var76[var70].validate()) {
                                                   DebugLog.log("MOVABLES: SpriteGrid didn't validate for multi sprite movable, group: (" + var62 + ") sheet = " + var26);
                                                   var69 = false;
                                                   break;
                                                }
                                             }
                                          }

                                          if (var69 && var66 != 0) {
                                             ++var22;

                                             for(var70 = 0; var70 < var16.length; ++var70) {
                                                IsoSpriteGrid var75 = var76[var70];
                                                if (var75 != null) {
                                                   IsoSprite[] var81 = var75.getSprites();
                                                   var43 = var81.length;

                                                   for(int var82 = 0; var82 < var43; ++var82) {
                                                      var45 = var81[var82];
                                                      var45.setSpriteGrid(var75);

                                                      for(int var84 = 0; var84 < var16.length; ++var84) {
                                                         if (var84 != var70 && var76[var84] != null) {
                                                            var45.getProperties().Set(var16[var84] + "offset", Integer.toString(var13.indexOf(var76[var84].getAnchorSprite()) - var13.indexOf(var45)));
                                                         }
                                                      }
                                                   }
                                                }
                                             }
                                          } else {
                                             DebugLog.log("MOVABLES: Error in multi sprite movable, group: (" + var62 + ") sheet = " + var26);
                                          }
                                       }
                                    }
                                 }
                              }
                           }

                           var33 = (IsoSprite)var59.next();
                           if (var33.getProperties().Is("StopCar")) {
                              var33.setType(IsoObjectType.isMoveAbleObject);
                           }
                        } while(!var33.getProperties().Is("IsMoveAble"));

                        if (var33.getProperties().Is("CustomName") && !var33.getProperties().Val("CustomName").equals("")) {
                           ++var19;
                           if (var33.getProperties().Is("GroupName")) {
                              String var10000 = var33.getProperties().Val("GroupName");
                              var62 = var10000 + " " + var33.getProperties().Val("CustomName");
                              if (!var14.containsKey(var62)) {
                                 var14.put(var62, new ArrayList());
                              }

                              ((ArrayList)var14.get(var62)).add(var33);
                              var23.add(var62);
                           } else {
                              if (!var18.containsKey(var26)) {
                                 var18.put(var26, new ArrayList());
                              }

                              if (!((ArrayList)var18.get(var26)).contains(var33.getProperties().Val("CustomName"))) {
                                 ((ArrayList)var18.get(var26)).add(var33.getProperties().Val("CustomName"));
                              }

                              ++var20;
                              var23.add(var33.getProperties().Val("CustomName"));
                           }
                        } else {
                           DebugLog.log("[IMPORTANT] MOVABLES: Object has no custom name defined: sheet = " + var26);
                        }
                     }
                  }
               }
            } catch (Throwable var53) {
               try {
                  var6.close();
               } catch (Throwable var51) {
                  var53.addSuppressed(var51);
               }

               throw var53;
            }

            var6.close();
         } catch (Throwable var54) {
            try {
               var5.close();
            } catch (Throwable var50) {
               var54.addSuppressed(var50);
            }

            throw var54;
         }

         var5.close();
      } catch (Exception var55) {
         ExceptionLogger.logException(var55);
      }

   }

   private void GenerateTilePropertyLookupTables() {
      TilePropertyAliasMap.instance.Generate(PropertyValueMap);
      PropertyValueMap.clear();
   }

   public void LoadTileDefinitionsPropertyStrings(IsoSpriteManager var1, String var2, int var3) {
      DebugLog.log("tiledef: loading " + var2);
      if (!GameServer.bServer) {
         Thread.yield();
         Core.getInstance().DoFrameReady();
      }

      try {
         FileInputStream var4 = new FileInputStream(var2);

         try {
            BufferedInputStream var5 = new BufferedInputStream(var4);

            try {
               int var6 = readInt((InputStream)var5);
               int var7 = readInt((InputStream)var5);
               int var8 = readInt((InputStream)var5);
               SharedStrings var9 = new SharedStrings();

               for(int var10 = 0; var10 < var8; ++var10) {
                  String var11 = readString((InputStream)var5);
                  String var12 = var11.trim();
                  String var13 = readString((InputStream)var5);
                  int var14 = readInt((InputStream)var5);
                  int var15 = readInt((InputStream)var5);
                  int var16 = readInt((InputStream)var5);
                  int var17 = readInt((InputStream)var5);

                  for(int var18 = 0; var18 < var17; ++var18) {
                     int var19 = readInt((InputStream)var5);

                     for(int var20 = 0; var20 < var19; ++var20) {
                        var11 = readString((InputStream)var5);
                        String var21 = var11.trim();
                        var11 = readString((InputStream)var5);
                        String var22 = var11.trim();
                        IsoObjectType var23 = IsoObjectType.FromString(var21);
                        var21 = var9.get(var21);
                        ArrayList var24 = null;
                        if (PropertyValueMap.containsKey(var21)) {
                           var24 = (ArrayList)PropertyValueMap.get(var21);
                        } else {
                           var24 = new ArrayList();
                           PropertyValueMap.put(var21, var24);
                        }

                        if (!var24.contains(var22)) {
                           var24.add(var22);
                        }
                     }
                  }
               }
            } catch (Throwable var27) {
               try {
                  var5.close();
               } catch (Throwable var26) {
                  var27.addSuppressed(var26);
               }

               throw var27;
            }

            var5.close();
         } catch (Throwable var28) {
            try {
               var4.close();
            } catch (Throwable var25) {
               var28.addSuppressed(var25);
            }

            throw var28;
         }

         var4.close();
      } catch (Exception var29) {
         Logger.getLogger(GameWindow.class.getName()).log(Level.SEVERE, (String)null, var29);
      }

   }

   private void SetCustomPropertyValues() {
      ((ArrayList)PropertyValueMap.get("WindowN")).add("WindowN");
      ((ArrayList)PropertyValueMap.get("WindowW")).add("WindowW");
      ((ArrayList)PropertyValueMap.get("DoorWallN")).add("DoorWallN");
      ((ArrayList)PropertyValueMap.get("DoorWallW")).add("DoorWallW");
      ((ArrayList)PropertyValueMap.get("WallSE")).add("WallSE");
      ArrayList var1 = new ArrayList();

      for(int var2 = -96; var2 <= 96; ++var2) {
         String var3 = Integer.toString(var2);
         var1.add(var3);
      }

      PropertyValueMap.put("Noffset", var1);
      PropertyValueMap.put("Soffset", var1);
      PropertyValueMap.put("Woffset", var1);
      PropertyValueMap.put("Eoffset", var1);
      ((ArrayList)PropertyValueMap.get("tree")).add("5");
      ((ArrayList)PropertyValueMap.get("tree")).add("6");
      ((ArrayList)PropertyValueMap.get("lightR")).add("0");
      ((ArrayList)PropertyValueMap.get("lightG")).add("0");
      ((ArrayList)PropertyValueMap.get("lightB")).add("0");
   }

   private void saveMovableStats(Map var1, int var2, int var3, int var4, int var5, int var6) throws FileNotFoundException, IOException {
      File var7 = new File(ZomboidFileSystem.instance.getCacheDir());
      if (var7.exists() && var7.isDirectory()) {
         String var10002 = ZomboidFileSystem.instance.getCacheDir();
         File var8 = new File(var10002 + File.separator + "movables_stats_" + var2 + ".txt");

         try {
            FileWriter var9 = new FileWriter(var8, false);

            try {
               var9.write("### Movable objects ###" + System.lineSeparator());
               var9.write("Single Face: " + var3 + System.lineSeparator());
               var9.write("Multi Face: " + var4 + System.lineSeparator());
               var9.write("Multi Face & Multi Sprite: " + var5 + System.lineSeparator());
               var9.write("Total objects : " + (var3 + var4 + var5) + System.lineSeparator());
               var9.write(" " + System.lineSeparator());
               var9.write("Total sprites : " + var6 + System.lineSeparator());
               var9.write(" " + System.lineSeparator());
               Iterator var10 = var1.entrySet().iterator();

               while(var10.hasNext()) {
                  Entry var11 = (Entry)var10.next();
                  String var10001 = (String)var11.getKey();
                  var9.write(var10001 + System.lineSeparator());
                  Iterator var12 = ((ArrayList)var11.getValue()).iterator();

                  while(var12.hasNext()) {
                     String var13 = (String)var12.next();
                     var9.write("\t" + var13 + System.lineSeparator());
                  }
               }
            } catch (Throwable var15) {
               try {
                  var9.close();
               } catch (Throwable var14) {
                  var15.addSuppressed(var14);
               }

               throw var15;
            }

            var9.close();
         } catch (Exception var16) {
            var16.printStackTrace();
         }
      }

   }

   private void addJumboTreeTileset(IsoSpriteManager var1, int var2, String var3, int var4, int var5, int var6) {
      byte var7 = 2;

      for(int var8 = 0; var8 < var5; ++var8) {
         for(int var9 = 0; var9 < var7; ++var9) {
            String var10 = "e_" + var3 + "JUMBO_1";
            int var11 = var8 * var7 + var9;
            IsoSprite var12 = var1.AddSprite(var10 + "_" + var11, var2 * 512 * 512 + var4 * 512 + var11);

            assert GameServer.bServer || !var12.CurrentAnim.Frames.isEmpty() && ((IsoDirectionFrame)var12.CurrentAnim.Frames.get(0)).getTexture(IsoDirections.N) != null;

            var12.setName(var10 + "_" + var11);
            var12.setType(IsoObjectType.tree);
            var12.getProperties().Set("tree", var9 == 0 ? "5" : "6");
            var12.getProperties().UnSet(IsoFlagType.solid);
            var12.getProperties().Set(IsoFlagType.blocksight);
            var12.getProperties().CreateKeySet();
            var12.moveWithWind = true;
            var12.windType = var6;
         }
      }

   }

   private void JumboTreeDefinitions(IsoSpriteManager var1, int var2) {
      this.addJumboTreeTileset(var1, var2, "americanholly", 1, 2, 3);
      this.addJumboTreeTileset(var1, var2, "americanlinden", 2, 6, 2);
      this.addJumboTreeTileset(var1, var2, "canadianhemlock", 3, 2, 3);
      this.addJumboTreeTileset(var1, var2, "carolinasilverbell", 4, 6, 1);
      this.addJumboTreeTileset(var1, var2, "cockspurhawthorn", 5, 6, 2);
      this.addJumboTreeTileset(var1, var2, "dogwood", 6, 6, 2);
      this.addJumboTreeTileset(var1, var2, "easternredbud", 7, 6, 2);
      this.addJumboTreeTileset(var1, var2, "redmaple", 8, 6, 2);
      this.addJumboTreeTileset(var1, var2, "riverbirch", 9, 6, 1);
      this.addJumboTreeTileset(var1, var2, "virginiapine", 10, 2, 1);
      this.addJumboTreeTileset(var1, var2, "yellowwood", 11, 6, 2);
      byte var3 = 12;
      byte var4 = 0;
      IsoSprite var5 = var1.AddSprite("jumbo_tree_01_" + var4, var2 * 512 * 512 + var3 * 512 + var4);
      var5.setName("jumbo_tree_01_" + var4);
      var5.setType(IsoObjectType.tree);
      var5.getProperties().Set("tree", "4");
      var5.getProperties().UnSet(IsoFlagType.solid);
      var5.getProperties().Set(IsoFlagType.blocksight);
   }

   public boolean LoadPlayerForInfo() throws FileNotFoundException, IOException {
      if (GameClient.bClient) {
         return ClientPlayerDB.getInstance().loadNetworkPlayerInfo(1);
      } else {
         File var1 = ZomboidFileSystem.instance.getFileInCurrentSave("map_p.bin");
         if (!var1.exists()) {
            PlayerDB.getInstance().importPlayersFromVehiclesDB();
            return PlayerDB.getInstance().loadLocalPlayerInfo(1);
         } else {
            FileInputStream var2 = new FileInputStream(var1);
            BufferedInputStream var3 = new BufferedInputStream(var2);
            synchronized(SliceY.SliceBufferLock) {
               SliceY.SliceBuffer.clear();
               int var5 = var3.read(SliceY.SliceBuffer.array());
               SliceY.SliceBuffer.limit(var5);
               var3.close();
               byte var6 = SliceY.SliceBuffer.get();
               byte var7 = SliceY.SliceBuffer.get();
               byte var8 = SliceY.SliceBuffer.get();
               byte var9 = SliceY.SliceBuffer.get();
               int var10 = -1;
               if (var6 == 80 && var7 == 76 && var8 == 89 && var9 == 82) {
                  var10 = SliceY.SliceBuffer.getInt();
               } else {
                  SliceY.SliceBuffer.rewind();
               }

               if (var10 >= 69) {
                  String var11 = GameWindow.ReadString(SliceY.SliceBuffer);
                  if (GameClient.bClient && var10 < 71) {
                     var11 = ServerOptions.instance.ServerPlayerID.getValue();
                  }

                  if (GameClient.bClient && !IsoPlayer.isServerPlayerIDValid(var11)) {
                     GameLoadingState.GameLoadingString = Translator.getText("IGUI_MP_ServerPlayerIDMismatch");
                     GameLoadingState.playerWrongIP = true;
                     return false;
                  }
               } else if (GameClient.bClient && ServerOptions.instance.ServerPlayerID.getValue().isEmpty()) {
                  GameLoadingState.GameLoadingString = Translator.getText("IGUI_MP_ServerPlayerIDMissing");
                  GameLoadingState.playerWrongIP = true;
                  return false;
               }

               WorldX = SliceY.SliceBuffer.getInt();
               WorldY = SliceY.SliceBuffer.getInt();
               IsoChunkMap.WorldXA = SliceY.SliceBuffer.getInt();
               IsoChunkMap.WorldYA = SliceY.SliceBuffer.getInt();
               IsoChunkMap.WorldZA = SliceY.SliceBuffer.getInt();
               IsoChunkMap.WorldXA += 300 * saveoffsetx;
               IsoChunkMap.WorldYA += 300 * saveoffsety;
               IsoChunkMap.SWorldX[0] = WorldX;
               IsoChunkMap.SWorldY[0] = WorldY;
               int[] var10000 = IsoChunkMap.SWorldX;
               var10000[0] += 30 * saveoffsetx;
               var10000 = IsoChunkMap.SWorldY;
               var10000[0] += 30 * saveoffsety;
               return true;
            }
         }
      }
   }

   public void init() throws FileNotFoundException, IOException, WorldDictionaryException {
      if (!Core.bTutorial) {
         this.randomizedBuildingList.add(new RBSafehouse());
         this.randomizedBuildingList.add(new RBBurnt());
         this.randomizedBuildingList.add(new RBOther());
         this.randomizedBuildingList.add(new RBLooted());
         this.randomizedBuildingList.add(new RBBurntFireman());
         this.randomizedBuildingList.add(new RBBurntCorpse());
         this.randomizedBuildingList.add(new RBShopLooted());
         this.randomizedBuildingList.add(new RBKateAndBaldspot());
         this.randomizedBuildingList.add(new RBStripclub());
         this.randomizedBuildingList.add(new RBSchool());
         this.randomizedBuildingList.add(new RBSpiffo());
         this.randomizedBuildingList.add(new RBPizzaWhirled());
         this.randomizedBuildingList.add(new RBPileOCrepe());
         this.randomizedBuildingList.add(new RBCafe());
         this.randomizedBuildingList.add(new RBBar());
         this.randomizedBuildingList.add(new RBOffice());
         this.randomizedBuildingList.add(new RBHairSalon());
         this.randomizedBuildingList.add(new RBClinic());
         this.randomizedVehicleStoryList.add(new RVSUtilityVehicle());
         this.randomizedVehicleStoryList.add(new RVSConstructionSite());
         this.randomizedVehicleStoryList.add(new RVSBurntCar());
         this.randomizedVehicleStoryList.add(new RVSPoliceBlockadeShooting());
         this.randomizedVehicleStoryList.add(new RVSPoliceBlockade());
         this.randomizedVehicleStoryList.add(new RVSCarCrash());
         this.randomizedVehicleStoryList.add(new RVSAmbulanceCrash());
         this.randomizedVehicleStoryList.add(new RVSCarCrashCorpse());
         this.randomizedVehicleStoryList.add(new RVSChangingTire());
         this.randomizedVehicleStoryList.add(new RVSFlippedCrash());
         this.randomizedVehicleStoryList.add(new RVSBanditRoad());
         this.randomizedVehicleStoryList.add(new RVSTrailerCrash());
         this.randomizedVehicleStoryList.add(new RVSCrashHorde());
         this.randomizedZoneList.add(new RZSForestCamp());
         this.randomizedZoneList.add(new RZSForestCampEaten());
         this.randomizedZoneList.add(new RZSBuryingCamp());
         this.randomizedZoneList.add(new RZSBeachParty());
         this.randomizedZoneList.add(new RZSFishingTrip());
         this.randomizedZoneList.add(new RZSBBQParty());
         this.randomizedZoneList.add(new RZSHunterCamp());
         this.randomizedZoneList.add(new RZSSexyTime());
         this.randomizedZoneList.add(new RZSTrapperCamp());
         this.randomizedZoneList.add(new RZSBaseball());
         this.randomizedZoneList.add(new RZSMusicFestStage());
         this.randomizedZoneList.add(new RZSMusicFest());
      }

      zombie.randomizedWorld.randomizedBuilding.RBBasic.getUniqueRDSSpawned().clear();
      if (!GameClient.bClient && !GameServer.bServer) {
         BodyDamageSync.instance = null;
      } else {
         BodyDamageSync.instance = new BodyDamageSync();
      }

      if (GameServer.bServer) {
         Core.GameSaveWorld = GameServer.ServerName;
         LuaManager.GlobalObject.createWorld(Core.GameSaveWorld);
      }

      SavedWorldVersion = this.readWorldVersion();
      int var4;
      if (!GameServer.bServer) {
         File var1 = ZomboidFileSystem.instance.getFileInCurrentSave("map_ver.bin");

         try {
            FileInputStream var2 = new FileInputStream(var1);

            try {
               DataInputStream var3 = new DataInputStream(var2);

               try {
                  var4 = var3.readInt();
                  if (var4 >= 25) {
                     String var5 = GameWindow.ReadString(var3);
                     if (!GameClient.bClient) {
                        Core.GameMap = var5;
                     }
                  }

                  if (var4 >= 74) {
                     this.setDifficulty(GameWindow.ReadString(var3));
                  }
               } catch (Throwable var28) {
                  try {
                     var3.close();
                  } catch (Throwable var25) {
                     var28.addSuppressed(var25);
                  }

                  throw var28;
               }

               var3.close();
            } catch (Throwable var29) {
               try {
                  var2.close();
               } catch (Throwable var24) {
                  var29.addSuppressed(var24);
               }

               throw var29;
            }

            var2.close();
         } catch (FileNotFoundException var30) {
         }
      }

      if (!GameServer.bServer || System.getProperty("softreset") == null) {
         this.MetaGrid.CreateStep1();
      }

      LuaEventManager.triggerEvent("OnPreDistributionMerge");
      LuaEventManager.triggerEvent("OnDistributionMerge");
      LuaEventManager.triggerEvent("OnPostDistributionMerge");
      ItemPickerJava.Parse();
      VehiclesDB2.instance.init();
      LuaEventManager.triggerEvent("OnInitWorld");
      if (!GameClient.bClient) {
         SandboxOptions.instance.load();
      }

      ZomboidGlobals.toLua();
      ItemPickerJava.InitSandboxLootSettings();
      this.SurvivorDescriptors.clear();
      IsoSpriteManager.instance.Dispose();
      if (GameClient.bClient && ServerOptions.instance.DoLuaChecksum.getValue()) {
         try {
            NetChecksum.comparer.beginCompare();
            GameLoadingState.GameLoadingString = Translator.getText("IGUI_MP_Checksum");
            long var31 = System.currentTimeMillis();
            long var34 = var31;

            while(!GameClient.checksumValid) {
               if (GameWindow.bServerDisconnected) {
                  return;
               }

               if (System.currentTimeMillis() > var31 + 8000L) {
                  DebugLog.log("checksum: timed out waiting for the server to respond");
                  GameClient.connection.forceDisconnect();
                  GameWindow.bServerDisconnected = true;
                  GameWindow.kickReason = Translator.getText("UI_GameLoad_TimedOut");
                  return;
               }

               if (System.currentTimeMillis() > var34 + 1000L) {
                  DebugLog.log("checksum: waited one second");
                  var34 += 1000L;
               }

               NetChecksum.comparer.update();
               if (GameClient.checksumValid) {
                  break;
               }

               Thread.sleep(100L);
            }
         } catch (Exception var27) {
            var27.printStackTrace();
         }
      }

      GameLoadingState.GameLoadingString = Translator.getText("IGUI_MP_LoadTileDef");
      IsoSpriteManager var32 = IsoSpriteManager.instance;
      ZomboidFileSystem var33 = ZomboidFileSystem.instance;
      this.LoadTileDefinitionsPropertyStrings(var32, var33.getMediaPath("tiledefinitions.tiles"), 0);
      this.LoadTileDefinitionsPropertyStrings(var32, var33.getMediaPath("newtiledefinitions.tiles"), 1);
      this.LoadTileDefinitionsPropertyStrings(var32, var33.getMediaPath("tiledefinitions_erosion.tiles"), 2);
      this.LoadTileDefinitionsPropertyStrings(var32, var33.getMediaPath("tiledefinitions_apcom.tiles"), 3);
      this.LoadTileDefinitionsPropertyStrings(var32, var33.getMediaPath("tiledefinitions_overlays.tiles"), 4);
      this.LoadTileDefinitionsPropertyStrings(var32, var33.getMediaPath("tiledefinitions_noiseworks.patch.tiles"), -1);
      ZomboidFileSystem.instance.loadModTileDefPropertyStrings();
      this.SetCustomPropertyValues();
      this.GenerateTilePropertyLookupTables();
      this.LoadTileDefinitions(var32, var33.getMediaPath("tiledefinitions.tiles"), 0);
      this.LoadTileDefinitions(var32, var33.getMediaPath("newtiledefinitions.tiles"), 1);
      this.LoadTileDefinitions(var32, var33.getMediaPath("tiledefinitions_erosion.tiles"), 2);
      this.LoadTileDefinitions(var32, var33.getMediaPath("tiledefinitions_apcom.tiles"), 3);
      this.LoadTileDefinitions(var32, var33.getMediaPath("tiledefinitions_overlays.tiles"), 4);
      this.LoadTileDefinitions(var32, var33.getMediaPath("tiledefinitions_noiseworks.patch.tiles"), -1);
      this.JumboTreeDefinitions(var32, 5);
      ZomboidFileSystem.instance.loadModTileDefs();
      GameLoadingState.GameLoadingString = "";
      var32.AddSprite("media/ui/missing-tile.png");
      LuaEventManager.triggerEvent("OnLoadedTileDefinitions", var32);
      if (GameServer.bServer && System.getProperty("softreset") != null) {
         WorldConverter.instance.softreset();
      }

      try {
         WeatherFxMask.init();
      } catch (Exception var23) {
         System.out.print(var23.getStackTrace());
      }

      IsoRegions.init();
      ObjectRenderEffects.init();
      WorldConverter.instance.convert(Core.GameSaveWorld, var32);
      if (!GameLoadingState.build23Stop) {
         SandboxOptions.instance.handleOldZombiesFile2();
         GameTime.getInstance().init();
         GameTime.getInstance().load();
         ZomboidRadio.getInstance().Init(SavedWorldVersion);
         GlobalModData.instance.init();
         if (GameServer.bServer && Core.getInstance().getPoisonousBerry() == null) {
            Core.getInstance().initPoisonousBerry();
         }

         if (GameServer.bServer && Core.getInstance().getPoisonousMushroom() == null) {
            Core.getInstance().initPoisonousMushroom();
         }

         ErosionGlobals.Boot(var32);
         WorldDictionary.init();
         WorldMarkers.instance.init();
         if (GameServer.bServer) {
            SharedDescriptors.initSharedDescriptors();
         }

         PersistentOutfits.instance.init();
         VirtualZombieManager.instance.init();
         VehicleIDMap.instance.Reset();
         VehicleManager.instance = new VehicleManager();
         GameLoadingState.GameLoadingString = Translator.getText("IGUI_MP_InitMap");
         this.MetaGrid.CreateStep2();
         ClimateManager.getInstance().init(this.MetaGrid);
         SafeHouse.init();
         if (!GameClient.bClient) {
            StashSystem.init();
         }

         LuaEventManager.triggerEvent("OnLoadMapZones");
         this.MetaGrid.load();
         this.MetaGrid.loadZones();
         this.MetaGrid.processZones();
         if (GameServer.bServer) {
            ServerMap.instance.init(this.MetaGrid);
         }

         boolean var35 = false;
         boolean var36 = false;
         if (GameClient.bClient) {
            if (ClientPlayerDB.getInstance().clientLoadNetworkPlayer() && ClientPlayerDB.getInstance().isAliveMainNetworkPlayer()) {
               var36 = true;
            }
         } else {
            var36 = PlayerDBHelper.isPlayerAlive(ZomboidFileSystem.instance.getGameModeCacheDir() + Core.GameSaveWorld, 1);
         }

         if (GameServer.bServer) {
            ServerPlayerDB.setAllow(true);
         }

         if (!GameClient.bClient && !GameServer.bServer) {
            PlayerDB.setAllow(true);
         }

         boolean var37 = false;
         boolean var6 = false;
         boolean var7 = false;
         SafeHouse var9;
         int var39;
         if (var36) {
            var35 = true;
            if (!this.LoadPlayerForInfo()) {
               return;
            }

            WorldX = IsoChunkMap.SWorldX[IsoPlayer.getPlayerIndex()];
            WorldY = IsoChunkMap.SWorldY[IsoPlayer.getPlayerIndex()];
            var39 = IsoChunkMap.WorldXA;
            int var40 = IsoChunkMap.WorldYA;
            int var42 = IsoChunkMap.WorldZA;
         } else {
            var35 = false;
            if (GameClient.bClient && !ServerOptions.instance.SpawnPoint.getValue().isEmpty()) {
               String[] var8 = ServerOptions.instance.SpawnPoint.getValue().split(",");
               if (var8.length == 3) {
                  try {
                     IsoChunkMap.MPWorldXA = new Integer(var8[0].trim());
                     IsoChunkMap.MPWorldYA = new Integer(var8[1].trim());
                     IsoChunkMap.MPWorldZA = new Integer(var8[2].trim());
                  } catch (NumberFormatException var22) {
                     DebugLog.log("ERROR: SpawnPoint must be x,y,z, got \"" + ServerOptions.instance.SpawnPoint.getValue() + "\"");
                     IsoChunkMap.MPWorldXA = 0;
                     IsoChunkMap.MPWorldYA = 0;
                     IsoChunkMap.MPWorldZA = 0;
                  }
               } else {
                  DebugLog.log("ERROR: SpawnPoint must be x,y,z, got \"" + ServerOptions.instance.SpawnPoint.getValue() + "\"");
               }
            }

            if (this.getLuaSpawnCellX() >= 0 && (!GameClient.bClient || IsoChunkMap.MPWorldXA == 0 && IsoChunkMap.MPWorldYA == 0)) {
               IsoChunkMap.WorldXA = this.getLuaPosX() + 300 * this.getLuaSpawnCellX();
               IsoChunkMap.WorldYA = this.getLuaPosY() + 300 * this.getLuaSpawnCellY();
               IsoChunkMap.WorldZA = this.getLuaPosZ();
               if (GameClient.bClient && ServerOptions.instance.SafehouseAllowRespawn.getValue()) {
                  for(int var43 = 0; var43 < SafeHouse.getSafehouseList().size(); ++var43) {
                     var9 = (SafeHouse)SafeHouse.getSafehouseList().get(var43);
                     if (var9.getPlayers().contains(GameClient.username) && var9.isRespawnInSafehouse(GameClient.username)) {
                        IsoChunkMap.WorldXA = var9.getX() + var9.getH() / 2;
                        IsoChunkMap.WorldYA = var9.getY() + var9.getW() / 2;
                        IsoChunkMap.WorldZA = 0;
                     }
                  }
               }

               WorldX = IsoChunkMap.WorldXA / 10;
               WorldY = IsoChunkMap.WorldYA / 10;
            } else if (GameClient.bClient) {
               IsoChunkMap.WorldXA = IsoChunkMap.MPWorldXA;
               IsoChunkMap.WorldYA = IsoChunkMap.MPWorldYA;
               IsoChunkMap.WorldZA = IsoChunkMap.MPWorldZA;
               WorldX = IsoChunkMap.WorldXA / 10;
               WorldY = IsoChunkMap.WorldYA / 10;
            }
         }

         Core.getInstance();
         KahluaTable var44 = (KahluaTable)LuaManager.env.rawget("selectedDebugScenario");
         int var10;
         int var11;
         int var12;
         if (var44 != null) {
            KahluaTable var45 = (KahluaTable)var44.rawget("startLoc");
            var10 = ((Double)var45.rawget("x")).intValue();
            var11 = ((Double)var45.rawget("y")).intValue();
            var12 = ((Double)var45.rawget("z")).intValue();
            IsoChunkMap.WorldXA = var10;
            IsoChunkMap.WorldYA = var11;
            IsoChunkMap.WorldZA = var12;
            WorldX = IsoChunkMap.WorldXA / 10;
            WorldY = IsoChunkMap.WorldYA / 10;
         }

         MapCollisionData.instance.init(instance.getMetaGrid());
         ZombiePopulationManager.instance.init(instance.getMetaGrid());
         PolygonalMap2.instance.init(instance.getMetaGrid());
         GlobalObjectLookup.init(instance.getMetaGrid());
         if (!GameServer.bServer) {
            SpawnPoints.instance.initSinglePlayer();
         }

         WorldStreamer.instance.create();
         this.CurrentCell = CellLoader.LoadCellBinaryChunk(var32, WorldX, WorldY);
         ClimateManager.getInstance().postCellLoadSetSnow();
         GameLoadingState.GameLoadingString = Translator.getText("IGUI_MP_LoadWorld");
         MapCollisionData.instance.start();
         MapItem.LoadWorldMap();

         while(WorldStreamer.instance.isBusy()) {
            try {
               Thread.sleep(100L);
            } catch (InterruptedException var21) {
               var21.printStackTrace();
            }
         }

         ArrayList var46 = new ArrayList();
         var46.addAll(IsoChunk.loadGridSquare);
         Iterator var47 = var46.iterator();

         while(var47.hasNext()) {
            IsoChunk var48 = (IsoChunk)var47.next();
            this.CurrentCell.ChunkMap[0].setChunkDirect(var48, false);
         }

         IsoChunk.bDoServerRequests = true;
         if (var35 && SystemDisabler.doPlayerCreation) {
            this.CurrentCell.LoadPlayer(SavedWorldVersion);
            if (GameClient.bClient) {
               IsoPlayer.getInstance().setUsername(GameClient.username);
            }
         } else {
            var9 = null;
            if (IsoPlayer.numPlayers == 0) {
               IsoPlayer.numPlayers = 1;
            }

            var10 = IsoChunkMap.WorldXA;
            var11 = IsoChunkMap.WorldYA;
            var12 = IsoChunkMap.WorldZA;
            if (GameClient.bClient && !ServerOptions.instance.SpawnPoint.getValue().isEmpty()) {
               String[] var13 = ServerOptions.instance.SpawnPoint.getValue().split(",");
               if (var13.length != 3) {
                  DebugLog.log("ERROR: SpawnPoint must be x,y,z, got \"" + ServerOptions.instance.SpawnPoint.getValue() + "\"");
               } else {
                  try {
                     int var14 = new Integer(var13[0].trim());
                     int var15 = new Integer(var13[1].trim());
                     int var16 = new Integer(var13[2].trim());
                     if (GameClient.bClient && ServerOptions.instance.SafehouseAllowRespawn.getValue()) {
                        for(int var17 = 0; var17 < SafeHouse.getSafehouseList().size(); ++var17) {
                           SafeHouse var18 = (SafeHouse)SafeHouse.getSafehouseList().get(var17);
                           if (var18.getPlayers().contains(GameClient.username) && var18.isRespawnInSafehouse(GameClient.username)) {
                              var14 = var18.getX() + var18.getH() / 2;
                              var15 = var18.getY() + var18.getW() / 2;
                              var16 = 0;
                           }
                        }
                     }

                     if (this.CurrentCell.getGridSquare(var14, var15, var16) != null) {
                        var10 = var14;
                        var11 = var15;
                        var12 = var16;
                     }
                  } catch (NumberFormatException var26) {
                     DebugLog.log("ERROR: SpawnPoint must be x,y,z, got \"" + ServerOptions.instance.SpawnPoint.getValue() + "\"");
                  }
               }
            }

            IsoGridSquare var49 = this.CurrentCell.getGridSquare(var10, var11, var12);
            if (SystemDisabler.doPlayerCreation && !GameServer.bServer) {
               if (var49 != null && var49.isFree(false) && var49.getRoom() != null) {
                  IsoGridSquare var50 = var49;
                  var49 = var49.getRoom().getFreeTile();
                  if (var49 == null) {
                     var49 = var50;
                  }
               }

               IsoPlayer var51 = null;
               if (this.getLuaPlayerDesc() != null) {
                  if (GameClient.bClient && ServerOptions.instance.SafehouseAllowRespawn.getValue()) {
                     var49 = this.CurrentCell.getGridSquare(IsoChunkMap.WorldXA, IsoChunkMap.WorldYA, IsoChunkMap.WorldZA);
                     if (var49 != null && var49.isFree(false) && var49.getRoom() != null) {
                        IsoGridSquare var52 = var49;
                        var49 = var49.getRoom().getFreeTile();
                        if (var49 == null) {
                           var49 = var52;
                        }
                     }
                  }

                  if (var49 == null) {
                     throw new RuntimeException("can't create player at x,y,z=" + var10 + "," + var11 + "," + var12 + " because the square is null");
                  }

                  WorldSimulation.instance.create();
                  var51 = new IsoPlayer(instance.CurrentCell, this.getLuaPlayerDesc(), var49.getX(), var49.getY(), var49.getZ());
                  if (GameClient.bClient) {
                     var51.setUsername(GameClient.username);
                  }

                  var51.setDir(IsoDirections.SE);
                  var51.sqlID = 1;
                  IsoPlayer.players[0] = var51;
                  IsoPlayer.setInstance(var51);
                  IsoCamera.CamCharacter = var51;
               }

               IsoPlayer var53 = IsoPlayer.getInstance();
               var53.applyTraits(this.getLuaTraits());
               ProfessionFactory.Profession var54 = ProfessionFactory.getProfession(var53.getDescriptor().getProfession());
               Iterator var55;
               String var56;
               if (var54 != null && !var54.getFreeRecipes().isEmpty()) {
                  var55 = var54.getFreeRecipes().iterator();

                  while(var55.hasNext()) {
                     var56 = (String)var55.next();
                     var53.getKnownRecipes().add(var56);
                  }
               }

               var55 = this.getLuaTraits().iterator();

               label332:
               while(true) {
                  TraitFactory.Trait var57;
                  do {
                     do {
                        if (!var55.hasNext()) {
                           if (var49 != null && var49.getRoom() != null) {
                              var49.getRoom().def.setExplored(true);
                              var49.getRoom().building.setAllExplored(true);
                              if (!GameServer.bServer && !GameClient.bClient) {
                                 ZombiePopulationManager.instance.playerSpawnedAt(var49.getX(), var49.getY(), var49.getZ());
                              }
                           }

                           var53.createKeyRing();
                           if (!GameClient.bClient) {
                              Core.getInstance().initPoisonousBerry();
                              Core.getInstance().initPoisonousMushroom();
                           }

                           LuaEventManager.triggerEvent("OnNewGame", var51, var49);
                           break label332;
                        }

                        var56 = (String)var55.next();
                        var57 = TraitFactory.getTrait(var56);
                     } while(var57 == null);
                  } while(var57.getFreeRecipes().isEmpty());

                  Iterator var19 = var57.getFreeRecipes().iterator();

                  while(var19.hasNext()) {
                     String var20 = (String)var19.next();
                     var53.getKnownRecipes().add(var20);
                  }
               }
            }
         }

         if (PlayerDB.isAllow()) {
            PlayerDB.getInstance().m_canSavePlayers = true;
         }

         if (ClientPlayerDB.isAllow()) {
            ClientPlayerDB.getInstance().canSavePlayers = true;
         }

         TutorialManager.instance.ActiveControlZombies = false;
         ReanimatedPlayers.instance.loadReanimatedPlayers();
         if (IsoPlayer.getInstance() != null) {
            if (GameClient.bClient) {
               int var38 = (int)IsoPlayer.getInstance().getX();
               var4 = (int)IsoPlayer.getInstance().getY();
               var39 = (int)IsoPlayer.getInstance().getZ();

               while(var39 > 0) {
                  IsoGridSquare var41 = this.CurrentCell.getGridSquare(var38, var4, var39);
                  if (var41 != null && var41.TreatAsSolidFloor()) {
                     break;
                  }

                  --var39;
                  IsoPlayer.getInstance().setZ((float)var39);
               }
            }

            IsoPlayer.getInstance().setCurrent(this.CurrentCell.getGridSquare((int)IsoPlayer.getInstance().getX(), (int)IsoPlayer.getInstance().getY(), (int)IsoPlayer.getInstance().getZ()));
         }

         if (!this.bLoaded) {
            if (!this.CurrentCell.getBuildingList().isEmpty()) {
            }

            if (!this.bLoaded) {
               this.PopulateCellWithSurvivors();
            }
         }

         if (IsoPlayer.players[0] != null && !this.CurrentCell.getObjectList().contains(IsoPlayer.players[0])) {
            this.CurrentCell.getObjectList().add(IsoPlayer.players[0]);
         }

         LightingThread.instance.create();
         GameLoadingState.GameLoadingString = "";
         initMessaging();
         WorldDictionary.onWorldLoaded();
      }
   }

   int readWorldVersion() {
      File var1;
      FileInputStream var2;
      DataInputStream var3;
      if (GameServer.bServer) {
         var1 = ZomboidFileSystem.instance.getFileInCurrentSave("map_t.bin");

         try {
            var2 = new FileInputStream(var1);

            label107: {
               int var8;
               try {
                  label118: {
                     var3 = new DataInputStream(var2);

                     try {
                        byte var21 = var3.readByte();
                        byte var5 = var3.readByte();
                        byte var6 = var3.readByte();
                        byte var7 = var3.readByte();
                        if (var21 == 71 && var5 == 77 && var6 == 84 && var7 == 77) {
                           var8 = var3.readInt();
                           break label118;
                        }
                     } catch (Throwable var17) {
                        try {
                           var3.close();
                        } catch (Throwable var12) {
                           var17.addSuppressed(var12);
                        }

                        throw var17;
                     }

                     var3.close();
                     break label107;
                  }

                  var3.close();
               } catch (Throwable var18) {
                  try {
                     var2.close();
                  } catch (Throwable var11) {
                     var18.addSuppressed(var11);
                  }

                  throw var18;
               }

               var2.close();
               return var8;
            }

            var2.close();
         } catch (FileNotFoundException var19) {
         } catch (IOException var20) {
            ExceptionLogger.logException(var20);
         }

         return -1;
      } else {
         var1 = ZomboidFileSystem.instance.getFileInCurrentSave("map_ver.bin");

         try {
            var2 = new FileInputStream(var1);

            int var4;
            try {
               var3 = new DataInputStream(var2);

               try {
                  var4 = var3.readInt();
               } catch (Throwable var13) {
                  try {
                     var3.close();
                  } catch (Throwable var10) {
                     var13.addSuppressed(var10);
                  }

                  throw var13;
               }

               var3.close();
            } catch (Throwable var14) {
               try {
                  var2.close();
               } catch (Throwable var9) {
                  var14.addSuppressed(var9);
               }

               throw var14;
            }

            var2.close();
            return var4;
         } catch (FileNotFoundException var15) {
         } catch (IOException var16) {
            ExceptionLogger.logException(var16);
         }

         return -1;
      }
   }

   public ArrayList getLuaTraits() {
      if (this.luatraits == null) {
         this.luatraits = new ArrayList();
      }

      return this.luatraits;
   }

   public void addLuaTrait(String var1) {
      this.getLuaTraits().add(var1);
   }

   public SurvivorDesc getLuaPlayerDesc() {
      return this.luaDesc;
   }

   public void setLuaPlayerDesc(SurvivorDesc var1) {
      this.luaDesc = var1;
   }

   public void KillCell() {
      this.helicopter.deactivate();
      CollisionManager.instance.ContactMap.clear();
      IsoDeadBody.Reset();
      FliesSound.instance.Reset();
      IsoObjectPicker.Instance.Init();
      IsoChunkMap.SharedChunks.clear();
      SoundManager.instance.StopMusic();
      WorldSoundManager.instance.KillCell();
      ZombieGroupManager.instance.Reset();
      this.CurrentCell.Dispose();
      IsoSpriteManager.instance.Dispose();
      this.CurrentCell = null;
      CellLoader.wanderRoom = null;
      IsoLot.Dispose();
      IsoGameCharacter.getSurvivorMap().clear();
      IsoPlayer.getInstance().setCurrent((IsoGridSquare)null);
      IsoPlayer.getInstance().setLast((IsoGridSquare)null);
      IsoPlayer.getInstance().square = null;
      RainManager.reset();
      IsoFireManager.Reset();
      IsoWaterFlow.Reset();
      this.MetaGrid.Dispose();
      instance = new IsoWorld();
   }

   public void setDrawWorld(boolean var1) {
      this.bDrawWorld = var1;
   }

   public void sceneCullZombies() {
      this.zombieWithModel.clear();
      this.zombieWithoutModel.clear();

      int var1;
      for(var1 = 0; var1 < this.CurrentCell.getZombieList().size(); ++var1) {
         IsoZombie var2 = (IsoZombie)this.CurrentCell.getZombieList().get(var1);
         boolean var3 = false;

         for(int var4 = 0; var4 < IsoPlayer.numPlayers; ++var4) {
            IsoPlayer var5 = IsoPlayer.players[var4];
            if (var5 != null && var2.current != null) {
               float var6 = (float)var2.getScreenProperX(var4);
               float var7 = (float)var2.getScreenProperY(var4);
               if (!(var6 < -100.0F) && !(var7 < -100.0F) && !(var6 > (float)(Core.getInstance().getOffscreenWidth(var4) + 100)) && !(var7 > (float)(Core.getInstance().getOffscreenHeight(var4) + 100)) && (var2.getAlpha(var4) != 0.0F && var2.legsSprite.def.alpha != 0.0F || var2.current.isCouldSee(var4))) {
                  var3 = true;
                  break;
               }
            }
         }

         if (var3 && var2.isCurrentState(FakeDeadZombieState.instance())) {
            var3 = false;
         }

         if (var3) {
            this.zombieWithModel.add(var2);
         } else {
            this.zombieWithoutModel.add(var2);
         }
      }

      Collections.sort(this.zombieWithModel, compScoreToPlayer);
      var1 = 0;
      int var8 = 0;
      int var9 = 0;
      short var10 = 510;
      PerformanceSettings.AnimationSkip = 0;

      int var11;
      IsoZombie var12;
      for(var11 = 0; var11 < this.zombieWithModel.size(); ++var11) {
         var12 = (IsoZombie)this.zombieWithModel.get(var11);
         if (var9 < var10) {
            if (!var12.Ghost) {
               ++var8;
               ++var9;
               var12.setSceneCulled(false);
               if (var12.legsSprite != null && var12.legsSprite.modelSlot != null) {
                  if (var8 > PerformanceSettings.ZombieAnimationSpeedFalloffCount) {
                     ++var1;
                     var8 = 0;
                  }

                  if (var9 < PerformanceSettings.ZombieBonusFullspeedFalloff) {
                     var12.legsSprite.modelSlot.model.setInstanceSkip(var8 / PerformanceSettings.ZombieBonusFullspeedFalloff);
                     var8 = 0;
                  } else {
                     var12.legsSprite.modelSlot.model.setInstanceSkip(var1 + PerformanceSettings.AnimationSkip);
                  }

                  if (var12.legsSprite.modelSlot.model.AnimPlayer != null) {
                     if (var9 >= PerformanceSettings.numberZombiesBlended) {
                        var12.legsSprite.modelSlot.model.AnimPlayer.bDoBlending = false;
                     } else {
                        var12.legsSprite.modelSlot.model.AnimPlayer.bDoBlending = !var12.isAlphaAndTargetZero(0) || !var12.isAlphaAndTargetZero(1) || !var12.isAlphaAndTargetZero(2) || !var12.isAlphaAndTargetZero(3);
                     }
                  }
               }
            }
         } else {
            var12.setSceneCulled(true);
            if (var12.hasAnimationPlayer()) {
               var12.getAnimationPlayer().bDoBlending = false;
            }
         }
      }

      for(var11 = 0; var11 < this.zombieWithoutModel.size(); ++var11) {
         var12 = (IsoZombie)this.zombieWithoutModel.get(var11);
         if (var12.hasActiveModel()) {
            var12.setSceneCulled(true);
         }

         if (var12.hasAnimationPlayer()) {
            var12.getAnimationPlayer().bDoBlending = false;
         }
      }

   }

   public void render() {
      IsoWorld.s_performance.isoWorldRender.invokeAndMeasure(this, IsoWorld::renderInternal);
   }

   private void renderInternal() {
      if (this.bDrawWorld) {
         if (IsoCamera.CamCharacter != null) {
            SpriteRenderer.instance.doCoreIntParam(0, IsoCamera.CamCharacter.x);
            SpriteRenderer.instance.doCoreIntParam(1, IsoCamera.CamCharacter.y);
            SpriteRenderer.instance.doCoreIntParam(2, IsoCamera.CamCharacter.z);

            try {
               this.sceneCullZombies();
            } catch (Throwable var3) {
               ExceptionLogger.logException(var3);
            }

            try {
               WeatherFxMask.initMask();
               DeadBodyAtlas.instance.render();
               this.CurrentCell.render();
               this.DrawIsoCursorHelper();
               DeadBodyAtlas.instance.renderDebug();
               PolygonalMap2.instance.render();
               WorldSoundManager.instance.render();
               WorldFlares.debugRender();
               WorldMarkers.instance.debugRender();
               LineDrawer.render();
               WeatherFxMask.renderFxMask(IsoCamera.frameState.playerIndex);
               if (GameClient.bClient) {
                  ClientServerMap.render(IsoCamera.frameState.playerIndex);
                  PassengerMap.render(IsoCamera.frameState.playerIndex);
               }

               SkyBox.getInstance().render();
            } catch (Throwable var2) {
               ExceptionLogger.logException(var2);
            }

         }
      }
   }

   private void DrawIsoCursorHelper() {
      if (Core.getInstance().getOffscreenBuffer() == null) {
         IsoPlayer var1 = IsoPlayer.getInstance();
         if (var1 != null && !var1.isDead() && var1.isAiming() && var1.PlayerIndex == 0 && var1.JoypadBind == -1) {
            if (!GameTime.isGamePaused()) {
               float var2 = 0.05F;
               switch(Core.getInstance().getIsoCursorVisibility()) {
               case 0:
                  return;
               case 1:
                  var2 = 0.05F;
                  break;
               case 2:
                  var2 = 0.1F;
                  break;
               case 3:
                  var2 = 0.15F;
                  break;
               case 4:
                  var2 = 0.3F;
                  break;
               case 5:
                  var2 = 0.5F;
                  break;
               case 6:
                  var2 = 0.75F;
               }

               if (Core.getInstance().isFlashIsoCursor()) {
                  if (this.flashIsoCursorInc) {
                     this.flashIsoCursorA += 0.1F;
                     if (this.flashIsoCursorA >= 1.0F) {
                        this.flashIsoCursorInc = false;
                     }
                  } else {
                     this.flashIsoCursorA -= 0.1F;
                     if (this.flashIsoCursorA <= 0.0F) {
                        this.flashIsoCursorInc = true;
                     }
                  }

                  var2 = this.flashIsoCursorA;
               }

               Texture var3 = Texture.getSharedTexture("media/ui/isocursor.png");
               int var4 = (int)((float)(var3.getWidth() * Core.TileScale) / 2.0F);
               int var5 = (int)((float)(var3.getHeight() * Core.TileScale) / 2.0F);
               SpriteRenderer.instance.setDoAdditive(true);
               SpriteRenderer.instance.renderi(var3, Mouse.getX() - var4 / 2, Mouse.getY() - var5 / 2, var4, var5, var2, var2, var2, var2, (Consumer)null);
               SpriteRenderer.instance.setDoAdditive(false);
            }
         }
      }
   }

   public void update() {
      IsoWorld.s_performance.isoWorldUpdate.invokeAndMeasure(this, IsoWorld::updateInternal);
   }

   private void updateInternal() {
      ++this.m_frameNo;

      try {
         if (GameServer.bServer) {
            VehicleManager.instance.serverUpdate();
         }
      } catch (Exception var8) {
         var8.printStackTrace();
      }

      WorldSimulation.instance.update();
      ImprovedFog.update();
      this.helicopter.update();
      long var1 = System.currentTimeMillis();
      if (var1 - this.emitterUpdateMS >= 30L) {
         this.emitterUpdateMS = var1;
         this.emitterUpdate = true;
      } else {
         this.emitterUpdate = false;
      }

      int var3;
      for(var3 = 0; var3 < this.currentEmitters.size(); ++var3) {
         BaseSoundEmitter var4 = (BaseSoundEmitter)this.currentEmitters.get(var3);
         if (this.emitterUpdate || var4.hasSoundsToStart()) {
            var4.tick();
         }

         if (var4.isEmpty()) {
            FMODSoundEmitter var5 = (FMODSoundEmitter)Type.tryCastTo(var4, FMODSoundEmitter.class);
            if (var5 != null) {
               var5.clearParameters();
            }

            this.currentEmitters.remove(var3);
            this.freeEmitters.push(var4);
            IsoObject var6 = (IsoObject)this.emitterOwners.remove(var4);
            if (var6 != null && var6.emitter == var4) {
               var6.emitter = null;
            }

            --var3;
         }
      }

      if (!GameClient.bClient && !GameServer.bServer) {
         IsoMetaCell var9 = this.MetaGrid.getCurrentCellData();
         if (var9 != null) {
            var9.checkTriggers();
         }
      }

      WorldSoundManager.instance.initFrame();
      ZombieGroupManager.instance.preupdate();
      OnceEvery.update();
      CollisionManager.instance.initUpdate();

      for(var3 = 0; var3 < this.CurrentCell.getBuildingList().size(); ++var3) {
         ((IsoBuilding)this.CurrentCell.getBuildingList().get(var3)).update();
      }

      ClimateManager.getInstance().update();
      ObjectRenderEffects.updateStatic();
      this.CurrentCell.update();
      IsoRegions.update();
      HaloTextHelper.update();
      CollisionManager.instance.ResolveContacts();

      for(var3 = 0; var3 < this.AddCoopPlayers.size(); ++var3) {
         AddCoopPlayer var10 = (AddCoopPlayer)this.AddCoopPlayers.get(var3);
         var10.update();
         if (var10.isFinished()) {
            this.AddCoopPlayers.remove(var3--);
         }
      }

      try {
         if (PlayerDB.isAvailable()) {
            PlayerDB.getInstance().updateMain();
         }

         if (ClientPlayerDB.isAvailable()) {
            ClientPlayerDB.getInstance().updateMain();
         }

         VehiclesDB2.instance.updateMain();
      } catch (Exception var7) {
         ExceptionLogger.logException(var7);
      }

      m_animationRecorderDiscard = false;
   }

   public IsoCell getCell() {
      return this.CurrentCell;
   }

   private void PopulateCellWithSurvivors() {
   }

   public int getWorldSquareY() {
      return this.CurrentCell.ChunkMap[IsoPlayer.getPlayerIndex()].WorldY * 10;
   }

   public int getWorldSquareX() {
      return this.CurrentCell.ChunkMap[IsoPlayer.getPlayerIndex()].WorldX * 10;
   }

   public IsoMetaChunk getMetaChunk(int var1, int var2) {
      return this.MetaGrid.getChunkData(var1, var2);
   }

   public IsoMetaChunk getMetaChunkFromTile(int var1, int var2) {
      return this.MetaGrid.getChunkDataFromTile(var1, var2);
   }

   public float getGlobalTemperature() {
      return ClimateManager.getInstance().getTemperature();
   }

   /** @deprecated */
   @Deprecated
   public void setGlobalTemperature(float var1) {
   }

   public String getWeather() {
      return this.weather;
   }

   public void setWeather(String var1) {
      this.weather = var1;
   }

   public int getLuaSpawnCellX() {
      return this.luaSpawnCellX;
   }

   public void setLuaSpawnCellX(int var1) {
      this.luaSpawnCellX = var1;
   }

   public int getLuaSpawnCellY() {
      return this.luaSpawnCellY;
   }

   public void setLuaSpawnCellY(int var1) {
      this.luaSpawnCellY = var1;
   }

   public int getLuaPosX() {
      return this.luaPosX;
   }

   public void setLuaPosX(int var1) {
      this.luaPosX = var1;
   }

   public int getLuaPosY() {
      return this.luaPosY;
   }

   public void setLuaPosY(int var1) {
      this.luaPosY = var1;
   }

   public int getLuaPosZ() {
      return this.luaPosZ;
   }

   public void setLuaPosZ(int var1) {
      this.luaPosZ = var1;
   }

   public String getWorld() {
      return Core.GameSaveWorld;
   }

   public void transmitWeather() {
      if (GameServer.bServer) {
         GameServer.sendWeather();
      }
   }

   public boolean isValidSquare(int var1, int var2, int var3) {
      return var3 >= 0 && var3 < 8 ? this.MetaGrid.isValidSquare(var1, var2) : false;
   }

   public ArrayList getRandomizedZoneList() {
      return this.randomizedZoneList;
   }

   public ArrayList getRandomizedBuildingList() {
      return this.randomizedBuildingList;
   }

   public ArrayList getRandomizedVehicleStoryList() {
      return this.randomizedVehicleStoryList;
   }

   public RandomizedVehicleStoryBase getRandomizedVehicleStoryByName(String var1) {
      for(int var2 = 0; var2 < this.randomizedVehicleStoryList.size(); ++var2) {
         RandomizedVehicleStoryBase var3 = (RandomizedVehicleStoryBase)this.randomizedVehicleStoryList.get(var2);
         if (var3.getName().equalsIgnoreCase(var1)) {
            return var3;
         }
      }

      return null;
   }

   public RandomizedBuildingBase getRBBasic() {
      return this.RBBasic;
   }

   public String getDifficulty() {
      return Core.getDifficulty();
   }

   public void setDifficulty(String var1) {
      Core.setDifficulty(var1);
   }

   public static boolean getZombiesDisabled() {
      return NoZombies || !SystemDisabler.doZombieCreation || SandboxOptions.instance.Zombies.getValue() == 6;
   }

   public static boolean getZombiesEnabled() {
      return !getZombiesDisabled();
   }

   public ClimateManager getClimateManager() {
      return ClimateManager.getInstance();
   }

   public IsoPuddles getPuddlesManager() {
      return IsoPuddles.getInstance();
   }

   public static int getWorldVersion() {
      return 186;
   }

   public HashMap getSpawnedZombieZone() {
      return this.spawnedZombieZone;
   }

   public int getTimeSinceLastSurvivorInHorde() {
      return this.timeSinceLastSurvivorInHorde;
   }

   public void setTimeSinceLastSurvivorInHorde(int var1) {
      this.timeSinceLastSurvivorInHorde = var1;
   }

   public float getWorldAgeDays() {
      float var1 = (float)GameTime.getInstance().getWorldAgeHours() / 24.0F;
      var1 += (float)((SandboxOptions.instance.TimeSinceApo.getValue() - 1) * 30);
      return var1;
   }

   public HashMap getAllTiles() {
      return this.allTiles;
   }

   public ArrayList getAllTilesName() {
      ArrayList var1 = new ArrayList();
      Iterator var2 = this.allTiles.keySet().iterator();

      while(var2.hasNext()) {
         var1.add((String)var2.next());
      }

      Collections.sort(var1);
      return var1;
   }

   public ArrayList getAllTiles(String var1) {
      return (ArrayList)this.allTiles.get(var1);
   }

   private static class CompScoreToPlayer implements Comparator {
      public int compare(IsoZombie var1, IsoZombie var2) {
         float var3 = this.getScore(var1);
         float var4 = this.getScore(var2);
         if (var3 < var4) {
            return 1;
         } else {
            return var3 > var4 ? -1 : 0;
         }
      }

      public float getScore(IsoZombie var1) {
         float var2 = Float.MIN_VALUE;

         for(int var3 = 0; var3 < 4; ++var3) {
            IsoPlayer var4 = IsoPlayer.players[var3];
            if (var4 != null && var4.current != null) {
               float var5 = var4.getZombieRelevenceScore(var1);
               var2 = Math.max(var2, var5);
            }
         }

         return var2;
      }
   }

   private static class s_performance {
      static final PerformanceProfileProbe isoWorldUpdate = new PerformanceProfileProbe("IsoWorld.update");
      static final PerformanceProfileProbe isoWorldRender = new PerformanceProfileProbe("IsoWorld.render");
   }

   private static class CompDistToPlayer implements Comparator {
      public float px;
      public float py;

      public int compare(IsoZombie var1, IsoZombie var2) {
         float var3 = IsoUtils.DistanceManhatten((float)((int)var1.x), (float)((int)var1.y), this.px, this.py);
         float var4 = IsoUtils.DistanceManhatten((float)((int)var2.x), (float)((int)var2.y), this.px, this.py);
         if (var3 < var4) {
            return -1;
         } else {
            return var3 > var4 ? 1 : 0;
         }
      }
   }

   public class Frame {
      public ArrayList xPos = new ArrayList();
      public ArrayList yPos = new ArrayList();
      public ArrayList Type = new ArrayList();

      public Frame() {
         Iterator var2 = IsoWorld.instance.CurrentCell.getObjectList().iterator();

         while(var2 != null && var2.hasNext()) {
            IsoMovingObject var3 = (IsoMovingObject)var2.next();
            boolean var4 = true;
            byte var5;
            if (var3 instanceof IsoPlayer) {
               var5 = 0;
            } else if (var3 instanceof IsoSurvivor) {
               var5 = 1;
            } else {
               if (!(var3 instanceof IsoZombie) || ((IsoZombie)var3).Ghost) {
                  continue;
               }

               var5 = 2;
            }

            this.xPos.add((int)var3.getX());
            this.yPos.add((int)var3.getY());
            this.Type.add(Integer.valueOf(var5));
         }

      }
   }

   public static class MetaCell {
      public int x;
      public int y;
      public int zombieCount;
      public IsoDirections zombieMigrateDirection;
      public int[][] from = new int[3][3];
   }
}

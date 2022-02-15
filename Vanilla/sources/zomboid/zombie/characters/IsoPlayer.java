package zombie.characters;

import fmod.fmod.BaseSoundListener;
import fmod.fmod.DummySoundListener;
import fmod.fmod.FMODSoundEmitter;
import fmod.fmod.SoundListener;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;
import org.joml.Vector3f;
import se.krka.kahlua.vm.KahluaTable;
import zombie.DebugFileWatcher;
import zombie.GameSounds;
import zombie.GameTime;
import zombie.GameWindow;
import zombie.PredicatedFileWatcher;
import zombie.SandboxOptions;
import zombie.SoundManager;
import zombie.SystemDisabler;
import zombie.ZomboidFileSystem;
import zombie.ZomboidGlobals;
import zombie.Lua.LuaEventManager;
import zombie.ai.State;
import zombie.ai.sadisticAIDirector.SleepingEvent;
import zombie.ai.states.BumpedState;
import zombie.ai.states.ClimbDownSheetRopeState;
import zombie.ai.states.ClimbOverFenceState;
import zombie.ai.states.ClimbOverWallState;
import zombie.ai.states.ClimbSheetRopeState;
import zombie.ai.states.ClimbThroughWindowState;
import zombie.ai.states.CloseWindowState;
import zombie.ai.states.CollideWithWallState;
import zombie.ai.states.FakeDeadZombieState;
import zombie.ai.states.FishingState;
import zombie.ai.states.FitnessState;
import zombie.ai.states.ForecastBeatenPlayerState;
import zombie.ai.states.IdleState;
import zombie.ai.states.OpenWindowState;
import zombie.ai.states.PathFindState;
import zombie.ai.states.PlayerActionsState;
import zombie.ai.states.PlayerAimState;
import zombie.ai.states.PlayerEmoteState;
import zombie.ai.states.PlayerExtState;
import zombie.ai.states.PlayerFallDownState;
import zombie.ai.states.PlayerFallingState;
import zombie.ai.states.PlayerGetUpState;
import zombie.ai.states.PlayerHitReactionPVPState;
import zombie.ai.states.PlayerHitReactionState;
import zombie.ai.states.PlayerKnockedDown;
import zombie.ai.states.PlayerOnGroundState;
import zombie.ai.states.PlayerSitOnGroundState;
import zombie.ai.states.PlayerStrafeState;
import zombie.ai.states.SmashWindowState;
import zombie.ai.states.StaggerBackState;
import zombie.ai.states.SwipeStatePlayer;
import zombie.audio.BaseSoundEmitter;
import zombie.audio.DummySoundEmitter;
import zombie.audio.FMODParameterList;
import zombie.audio.GameSound;
import zombie.audio.parameters.ParameterCharacterMovementSpeed;
import zombie.audio.parameters.ParameterFootstepMaterial;
import zombie.audio.parameters.ParameterFootstepMaterial2;
import zombie.audio.parameters.ParameterLocalPlayer;
import zombie.audio.parameters.ParameterMeleeHitSurface;
import zombie.audio.parameters.ParameterPlayerHealth;
import zombie.audio.parameters.ParameterShoeType;
import zombie.audio.parameters.ParameterVehicleHitLocation;
import zombie.characters.AttachedItems.AttachedItems;
import zombie.characters.BodyDamage.BodyDamage;
import zombie.characters.BodyDamage.BodyPart;
import zombie.characters.BodyDamage.BodyPartType;
import zombie.characters.BodyDamage.Fitness;
import zombie.characters.BodyDamage.Nutrition;
import zombie.characters.CharacterTimedActions.BaseAction;
import zombie.characters.Moodles.MoodleType;
import zombie.characters.Moodles.Moodles;
import zombie.characters.action.ActionContext;
import zombie.characters.action.ActionGroup;
import zombie.characters.skills.PerkFactory;
import zombie.core.Color;
import zombie.core.Core;
import zombie.core.Rand;
import zombie.core.Translator;
import zombie.core.logger.ExceptionLogger;
import zombie.core.logger.LoggerManager;
import zombie.core.math.PZMath;
import zombie.core.network.ByteBufferWriter;
import zombie.core.opengl.Shader;
import zombie.core.profiling.PerformanceProfileProbe;
import zombie.core.skinnedmodel.ModelManager;
import zombie.core.skinnedmodel.advancedanimation.AnimEvent;
import zombie.core.skinnedmodel.advancedanimation.AnimLayer;
import zombie.core.skinnedmodel.animation.AnimationPlayer;
import zombie.core.skinnedmodel.visual.BaseVisual;
import zombie.core.skinnedmodel.visual.HumanVisual;
import zombie.core.skinnedmodel.visual.IHumanVisual;
import zombie.core.skinnedmodel.visual.ItemVisuals;
import zombie.core.textures.ColorInfo;
import zombie.debug.DebugLog;
import zombie.debug.DebugOptions;
import zombie.debug.DebugType;
import zombie.gameStates.MainScreenState;
import zombie.input.GameKeyboard;
import zombie.input.JoypadManager;
import zombie.input.Mouse;
import zombie.inventory.InventoryItem;
import zombie.inventory.InventoryItemFactory;
import zombie.inventory.types.Clothing;
import zombie.inventory.types.DrainableComboItem;
import zombie.inventory.types.HandWeapon;
import zombie.inventory.types.WeaponType;
import zombie.iso.IsoCamera;
import zombie.iso.IsoCell;
import zombie.iso.IsoChunk;
import zombie.iso.IsoDirections;
import zombie.iso.IsoGridSquare;
import zombie.iso.IsoMetaGrid;
import zombie.iso.IsoMovingObject;
import zombie.iso.IsoObject;
import zombie.iso.IsoPhysicsObject;
import zombie.iso.IsoUtils;
import zombie.iso.IsoWorld;
import zombie.iso.SliceY;
import zombie.iso.Vector2;
import zombie.iso.SpriteDetails.IsoFlagType;
import zombie.iso.SpriteDetails.IsoObjectType;
import zombie.iso.areas.SafeHouse;
import zombie.iso.objects.IsoBarricade;
import zombie.iso.objects.IsoCurtain;
import zombie.iso.objects.IsoDeadBody;
import zombie.iso.objects.IsoDoor;
import zombie.iso.objects.IsoThumpable;
import zombie.iso.objects.IsoWindow;
import zombie.iso.objects.IsoWindowFrame;
import zombie.iso.weather.ClimateManager;
import zombie.network.BodyDamageSync;
import zombie.network.GameClient;
import zombie.network.GameServer;
import zombie.network.PacketTypes;
import zombie.network.PassengerMap;
import zombie.network.ReplayManager;
import zombie.network.ServerLOS;
import zombie.network.ServerMap;
import zombie.network.ServerOptions;
import zombie.network.ServerWorldDatabase;
import zombie.network.packets.EventPacket;
import zombie.network.packets.hit.AttackVars;
import zombie.network.packets.hit.HitInfo;
import zombie.savefile.ClientPlayerDB;
import zombie.savefile.PlayerDB;
import zombie.scripting.objects.VehicleScript;
import zombie.ui.TutorialManager;
import zombie.ui.UIManager;
import zombie.util.StringUtils;
import zombie.util.Type;
import zombie.util.list.PZArrayUtil;
import zombie.vehicles.BaseVehicle;
import zombie.vehicles.PathFindBehavior2;
import zombie.vehicles.PolygonalMap2;
import zombie.vehicles.VehiclePart;
import zombie.vehicles.VehicleWindow;
import zombie.vehicles.VehiclesDB2;
import zombie.world.WorldDictionary;

public final class IsoPlayer extends IsoLivingCharacter implements IHumanVisual {
   private String attackType;
   public static String DEATH_MUSIC_NAME = "PlayerDied";
   private boolean allowSprint;
   private boolean allowRun;
   public static boolean isTestAIMode = false;
   public static final boolean NoSound = false;
   private static final float TIME_RIGHT_PRESSED_SECONDS = 0.15F;
   public static int assumedPlayer = 0;
   public static int numPlayers = 1;
   public static final short MAX = 4;
   public static final IsoPlayer[] players = new IsoPlayer[4];
   private static IsoPlayer instance;
   private static final Object instanceLock = "IsoPlayer.instance Lock";
   private static final Vector2 testHitPosition = new Vector2();
   private static int FollowDeadCount = 240;
   private static final Stack StaticTraits = new Stack();
   private boolean ignoreAutoVault;
   public int remoteSneakLvl;
   public int remoteStrLvl;
   public int remoteFitLvl;
   public boolean canSeeAll;
   public boolean canHearAll;
   public boolean MoodleCantSprint;
   private static final Vector2 tempo = new Vector2();
   private static final Vector2 tempVector2 = new Vector2();
   private static final String forwardStr = "Forward";
   private static final String backwardStr = "Backward";
   private static final String leftStr = "Left";
   private static final String rightStr = "Right";
   private static boolean CoopPVP = false;
   private boolean ignoreContextKey;
   private boolean ignoreInputsForDirection;
   private boolean showMPInfos;
   public boolean spottedByPlayer;
   private HashMap spottedPlayerTimer;
   private float extUpdateCount;
   private static final int s_randomIdleFidgetInterval = 5000;
   public boolean attackStarted;
   private static final PredicatedFileWatcher m_isoPlayerTriggerWatcher;
   private final PredicatedFileWatcher m_setClothingTriggerWatcher;
   private static Vector2 tempVector2_1;
   private static Vector2 tempVector2_2;
   protected final HumanVisual humanVisual;
   protected final ItemVisuals itemVisuals;
   public boolean targetedByZombie;
   public float lastTargeted;
   public float TimeSinceOpenDoor;
   public boolean bRemote;
   public int TimeSinceLastNetData;
   public String accessLevel;
   public String tagPrefix;
   public boolean showTag;
   public boolean factionPvp;
   public short OnlineID;
   public int OnlineChunkGridWidth;
   public boolean bJoypadMovementActive;
   public boolean bJoypadIgnoreAimUntilCentered;
   public boolean bJoypadIgnoreChargingRT;
   protected boolean bJoypadBDown;
   protected boolean bJoypadSprint;
   public boolean mpTorchCone;
   public float mpTorchDist;
   public float mpTorchStrength;
   public int PlayerIndex;
   public int serverPlayerIndex;
   public float useChargeDelta;
   public int JoypadBind;
   public float ContextPanic;
   public float numNearbyBuildingsRooms;
   public boolean isCharging;
   public boolean isChargingLT;
   private boolean bLookingWhileInVehicle;
   private boolean climbOverWallSuccess;
   private boolean climbOverWallStruggle;
   private boolean JustMoved;
   public boolean L3Pressed;
   public float maxWeightDelta;
   public float CurrentSpeed;
   public float MaxSpeed;
   public boolean bDeathFinished;
   public boolean isSpeek;
   public boolean isVoiceMute;
   public final Vector2 playerMoveDir;
   public BaseSoundListener soundListener;
   public String username;
   public boolean dirtyRecalcGridStack;
   public float dirtyRecalcGridStackTime;
   public float runningTime;
   public float timePressedContext;
   public float chargeTime;
   public float useChargeTime;
   public boolean bPressContext;
   public float closestZombie;
   public final Vector2 lastAngle;
   public String SaveFileName;
   public boolean bBannedAttacking;
   public int sqlID;
   protected int ClearSpottedTimer;
   protected float timeSinceLastStab;
   protected Stack LastSpotted;
   protected boolean bChangeCharacterDebounce;
   protected int followID;
   protected final Stack FollowCamStack;
   protected boolean bSeenThisFrame;
   protected boolean bCouldBeSeenThisFrame;
   protected float AsleepTime;
   protected final Stack spottedList;
   protected int TicksSinceSeenZombie;
   protected boolean Waiting;
   protected IsoSurvivor DragCharacter;
   protected float heartDelay;
   protected float heartDelayMax;
   protected long heartEventInstance;
   protected long worldAmbianceInstance;
   protected String Forname;
   protected String Surname;
   protected int DialogMood;
   protected int ping;
   protected IsoMovingObject DragObject;
   private double lastSeenZombieTime;
   private BaseSoundEmitter testemitter;
   private int checkSafehouse;
   private boolean attackFromBehind;
   private float TimeRightPressed;
   private long aimKeyDownMS;
   private long runKeyDownMS;
   private long sprintKeyDownMS;
   private int hypothermiaCache;
   private int hyperthermiaCache;
   private float ticksSincePressedMovement;
   private boolean flickTorch;
   private float checkNearbyRooms;
   private boolean bUseVehicle;
   private boolean bUsedVehicle;
   private float useVehicleDuration;
   private static final Vector3f tempVector3f;
   private final IsoPlayer.InputState inputState;
   private boolean isWearingNightVisionGoggles;
   /** @deprecated */
   @Deprecated
   private Integer transactionID;
   private float MoveSpeed;
   private int offSetXUI;
   private int offSetYUI;
   private float combatSpeed;
   private double HoursSurvived;
   private boolean bSentDeath;
   private boolean noClip;
   private boolean authorizeMeleeAction;
   private boolean authorizeShoveStomp;
   private boolean blockMovement;
   private Nutrition nutrition;
   private Fitness fitness;
   private boolean forceOverrideAnim;
   private boolean initiateAttack;
   private final ColorInfo tagColor;
   private String displayName;
   private boolean seeNonPvpZone;
   private final HashMap mechanicsItem;
   private int sleepingPillsTaken;
   private long lastPillsTaken;
   private long heavyBreathInstance;
   private String heavyBreathSoundName;
   private boolean allChatMuted;
   private boolean forceAim;
   private boolean forceRun;
   private boolean forceSprint;
   private boolean bMultiplayer;
   private String SaveFileIP;
   private BaseVehicle vehicle4testCollision;
   private long steamID;
   private final IsoPlayer.VehicleContainerData vehicleContainerData;
   private boolean isWalking;
   private int footInjuryTimer;
   private boolean bSneakDebounce;
   private float m_turnDelta;
   protected boolean m_isPlayerMoving;
   private float m_walkSpeed;
   private float m_walkInjury;
   private float m_runSpeed;
   private float m_idleSpeed;
   private float m_deltaX;
   private float m_deltaY;
   private float m_windspeed;
   private float m_windForce;
   private float m_IPX;
   private float m_IPY;
   private float pressedRunTimer;
   private boolean pressedRun;
   private boolean m_meleePressed;
   private boolean m_lastAttackWasShove;
   private boolean m_isPerformingAnAction;
   private ArrayList alreadyReadBook;
   public byte bleedingLevel;
   public final NetworkPlayerAI networkAI;
   public ReplayManager replay;
   private boolean pathfindRun;
   private static final IsoPlayer.MoveVars s_moveVars;
   int atkTimer;
   private static final ArrayList s_targetsProne;
   private static final ArrayList s_targetsStanding;
   private boolean bReloadButtonDown;
   private boolean bRackButtonDown;
   private boolean bReloadKeyDown;
   private boolean bRackKeyDown;
   private long AttackAnimThrowTimer;
   String WeaponT;
   private final ParameterCharacterMovementSpeed parameterCharacterMovementSpeed;
   private final ParameterFootstepMaterial parameterFootstepMaterial;
   private final ParameterFootstepMaterial2 parameterFootstepMaterial2;
   private final ParameterLocalPlayer parameterLocalPlayer;
   private final ParameterMeleeHitSurface parameterMeleeHitSurface;
   private final ParameterPlayerHealth parameterPlayerHealth;
   private final ParameterVehicleHitLocation parameterVehicleHitLocation;
   private final ParameterShoeType parameterShoeType;

   public IsoPlayer(IsoCell var1) {
      this(var1, (SurvivorDesc)null, 0, 0, 0);
   }

   public IsoPlayer(IsoCell var1, SurvivorDesc var2, int var3, int var4, int var5) {
      super(var1, (float)var3, (float)var4, (float)var5);
      this.attackType = null;
      this.allowSprint = true;
      this.allowRun = true;
      this.ignoreAutoVault = false;
      this.remoteSneakLvl = 0;
      this.remoteStrLvl = 0;
      this.remoteFitLvl = 0;
      this.canSeeAll = false;
      this.canHearAll = false;
      this.MoodleCantSprint = false;
      this.ignoreContextKey = false;
      this.ignoreInputsForDirection = false;
      this.showMPInfos = false;
      this.spottedByPlayer = false;
      this.spottedPlayerTimer = new HashMap();
      this.extUpdateCount = 0.0F;
      this.attackStarted = false;
      this.humanVisual = new HumanVisual(this);
      this.itemVisuals = new ItemVisuals();
      this.targetedByZombie = false;
      this.lastTargeted = 1.0E8F;
      this.TimeSinceLastNetData = 0;
      this.accessLevel = "";
      this.tagPrefix = "";
      this.showTag = true;
      this.factionPvp = false;
      this.OnlineID = 1;
      this.bJoypadMovementActive = true;
      this.bJoypadIgnoreChargingRT = false;
      this.bJoypadBDown = false;
      this.bJoypadSprint = false;
      this.mpTorchCone = false;
      this.mpTorchDist = 0.0F;
      this.mpTorchStrength = 0.0F;
      this.PlayerIndex = 0;
      this.serverPlayerIndex = 1;
      this.useChargeDelta = 0.0F;
      this.JoypadBind = -1;
      this.ContextPanic = 0.0F;
      this.numNearbyBuildingsRooms = 0.0F;
      this.isCharging = false;
      this.isChargingLT = false;
      this.bLookingWhileInVehicle = false;
      this.JustMoved = false;
      this.L3Pressed = false;
      this.maxWeightDelta = 1.0F;
      this.CurrentSpeed = 0.0F;
      this.MaxSpeed = 0.09F;
      this.bDeathFinished = false;
      this.playerMoveDir = new Vector2(0.0F, 0.0F);
      this.username = "Bob";
      this.dirtyRecalcGridStack = true;
      this.dirtyRecalcGridStackTime = 10.0F;
      this.runningTime = 0.0F;
      this.timePressedContext = 0.0F;
      this.chargeTime = 0.0F;
      this.useChargeTime = 0.0F;
      this.bPressContext = false;
      this.closestZombie = 1000000.0F;
      this.lastAngle = new Vector2();
      this.bBannedAttacking = false;
      this.sqlID = -1;
      this.ClearSpottedTimer = -1;
      this.timeSinceLastStab = 0.0F;
      this.LastSpotted = new Stack();
      this.bChangeCharacterDebounce = false;
      this.followID = 0;
      this.FollowCamStack = new Stack();
      this.bSeenThisFrame = false;
      this.bCouldBeSeenThisFrame = false;
      this.AsleepTime = 0.0F;
      this.spottedList = new Stack();
      this.TicksSinceSeenZombie = 9999999;
      this.Waiting = true;
      this.DragCharacter = null;
      this.heartDelay = 30.0F;
      this.heartDelayMax = 30.0F;
      this.Forname = "Bob";
      this.Surname = "Smith";
      this.DialogMood = 1;
      this.ping = 0;
      this.DragObject = null;
      this.lastSeenZombieTime = 2.0D;
      this.checkSafehouse = 200;
      this.attackFromBehind = false;
      this.TimeRightPressed = 0.0F;
      this.aimKeyDownMS = 0L;
      this.runKeyDownMS = 0L;
      this.sprintKeyDownMS = 0L;
      this.hypothermiaCache = -1;
      this.hyperthermiaCache = -1;
      this.ticksSincePressedMovement = 0.0F;
      this.flickTorch = false;
      this.checkNearbyRooms = 0.0F;
      this.bUseVehicle = false;
      this.inputState = new IsoPlayer.InputState();
      this.isWearingNightVisionGoggles = false;
      this.transactionID = 0;
      this.MoveSpeed = 0.06F;
      this.offSetXUI = 0;
      this.offSetYUI = 0;
      this.combatSpeed = 1.0F;
      this.HoursSurvived = 0.0D;
      this.noClip = false;
      this.authorizeMeleeAction = true;
      this.authorizeShoveStomp = true;
      this.blockMovement = false;
      this.forceOverrideAnim = false;
      this.initiateAttack = false;
      this.tagColor = new ColorInfo(1.0F, 1.0F, 1.0F, 1.0F);
      this.displayName = null;
      this.seeNonPvpZone = false;
      this.mechanicsItem = new HashMap();
      this.sleepingPillsTaken = 0;
      this.lastPillsTaken = 0L;
      this.heavyBreathInstance = 0L;
      this.heavyBreathSoundName = null;
      this.allChatMuted = false;
      this.forceAim = false;
      this.forceRun = false;
      this.forceSprint = false;
      this.vehicle4testCollision = null;
      this.vehicleContainerData = new IsoPlayer.VehicleContainerData();
      this.isWalking = false;
      this.footInjuryTimer = 0;
      this.m_turnDelta = 0.0F;
      this.m_isPlayerMoving = false;
      this.m_walkSpeed = 0.0F;
      this.m_walkInjury = 0.0F;
      this.m_runSpeed = 0.0F;
      this.m_idleSpeed = 0.0F;
      this.m_deltaX = 0.0F;
      this.m_deltaY = 0.0F;
      this.m_windspeed = 0.0F;
      this.m_windForce = 0.0F;
      this.m_IPX = 0.0F;
      this.m_IPY = 0.0F;
      this.pressedRunTimer = 0.0F;
      this.pressedRun = false;
      this.m_meleePressed = false;
      this.m_lastAttackWasShove = false;
      this.m_isPerformingAnAction = false;
      this.alreadyReadBook = new ArrayList();
      this.bleedingLevel = 0;
      this.replay = null;
      this.pathfindRun = false;
      this.atkTimer = 0;
      this.bReloadButtonDown = false;
      this.bRackButtonDown = false;
      this.bReloadKeyDown = false;
      this.bRackKeyDown = false;
      this.AttackAnimThrowTimer = System.currentTimeMillis();
      this.WeaponT = null;
      this.parameterCharacterMovementSpeed = new ParameterCharacterMovementSpeed(this);
      this.parameterFootstepMaterial = new ParameterFootstepMaterial(this);
      this.parameterFootstepMaterial2 = new ParameterFootstepMaterial2(this);
      this.parameterLocalPlayer = new ParameterLocalPlayer(this);
      this.parameterMeleeHitSurface = new ParameterMeleeHitSurface(this);
      this.parameterPlayerHealth = new ParameterPlayerHealth(this);
      this.parameterVehicleHitLocation = new ParameterVehicleHitLocation();
      this.parameterShoeType = new ParameterShoeType(this);
      this.registerVariableCallbacks();
      this.Traits.addAll(StaticTraits);
      StaticTraits.clear();
      this.dir = IsoDirections.W;
      this.nutrition = new Nutrition(this);
      this.fitness = new Fitness(this);
      this.initWornItems("Human");
      this.initAttachedItems("Human");
      this.clothingWetness = new ClothingWetness(this);
      if (var2 != null) {
         this.descriptor = var2;
      } else {
         this.descriptor = new SurvivorDesc();
      }

      this.setFemale(this.descriptor.isFemale());
      this.Dressup(this.descriptor);
      this.getHumanVisual().copyFrom(this.descriptor.humanVisual);
      this.InitSpriteParts(this.descriptor);
      LuaEventManager.triggerEvent("OnCreateLivingCharacter", this, this.descriptor);
      if (!GameClient.bClient && !GameServer.bServer) {
      }

      this.descriptor.Instance = this;
      this.SpeakColour = new Color(Rand.Next(135) + 120, Rand.Next(135) + 120, Rand.Next(135) + 120, 255);
      if (GameClient.bClient) {
         if (Core.getInstance().getMpTextColor() != null) {
            this.SpeakColour = new Color(Core.getInstance().getMpTextColor().r, Core.getInstance().getMpTextColor().g, Core.getInstance().getMpTextColor().b, 1.0F);
         } else {
            Core.getInstance().setMpTextColor(new ColorInfo(this.SpeakColour.r, this.SpeakColour.g, this.SpeakColour.b, 1.0F));

            try {
               Core.getInstance().saveOptions();
            } catch (IOException var7) {
               var7.printStackTrace();
            }
         }
      }

      if (Core.GameMode.equals("LastStand")) {
         this.Traits.add("Strong");
      }

      if (this.Traits.Strong.isSet()) {
         this.maxWeightDelta = 1.5F;
      }

      if (this.Traits.Weak.isSet()) {
         this.maxWeightDelta = 0.75F;
      }

      if (this.Traits.Feeble.isSet()) {
         this.maxWeightDelta = 0.9F;
      }

      if (this.Traits.Stout.isSet()) {
         this.maxWeightDelta = 1.25F;
      }

      this.descriptor.temper = 5.0F;
      if (this.Traits.ShortTemper.isSet()) {
         this.descriptor.temper = 7.5F;
      } else if (this.Traits.Patient.isSet()) {
         this.descriptor.temper = 2.5F;
      }

      if (this.Traits.Injured.isSet()) {
         this.getBodyDamage().AddRandomDamage();
      }

      this.bMultiplayer = GameServer.bServer || GameClient.bClient;
      this.vehicle4testCollision = null;
      if (Core.bDebug && DebugOptions.instance.CheatPlayerStartInvisible.getValue()) {
         this.setGhostMode(true);
         this.setGodMod(true);
      }

      this.actionContext.setGroup(ActionGroup.getActionGroup("player"));
      this.initializeStates();
      DebugFileWatcher.instance.add(m_isoPlayerTriggerWatcher);
      this.m_setClothingTriggerWatcher = new PredicatedFileWatcher(ZomboidFileSystem.instance.getMessagingDirSub("Trigger_SetClothing.xml"), TriggerXmlFile.class, this::onTrigger_setClothingToXmlTriggerFile);
      this.networkAI = new NetworkPlayerAI(this);
      this.initFMODParameters();
   }

   public void setOnlineID(short var1) {
      this.OnlineID = var1;
   }

   private void registerVariableCallbacks() {
      this.setVariable("CombatSpeed", () -> {
         return this.combatSpeed;
      }, (var1) -> {
         this.combatSpeed = var1;
      });
      this.setVariable("TurnDelta", () -> {
         return this.m_turnDelta;
      }, (var1) -> {
         this.m_turnDelta = var1;
      });
      this.setVariable("sneaking", this::isSneaking, this::setSneaking);
      this.setVariable("initiateAttack", () -> {
         return this.initiateAttack;
      }, this::setInitiateAttack);
      this.setVariable("isMoving", this::isPlayerMoving);
      this.setVariable("isRunning", this::isRunning, this::setRunning);
      this.setVariable("isSprinting", this::isSprinting, this::setSprinting);
      this.setVariable("run", this::isRunning, this::setRunning);
      this.setVariable("sprint", this::isSprinting, this::setSprinting);
      this.setVariable("isStrafing", this::isStrafing);
      this.setVariable("WalkSpeed", () -> {
         return this.m_walkSpeed;
      }, (var1) -> {
         this.m_walkSpeed = var1;
      });
      this.setVariable("WalkInjury", () -> {
         return this.m_walkInjury;
      }, (var1) -> {
         this.m_walkInjury = var1;
      });
      this.setVariable("RunSpeed", () -> {
         return this.m_runSpeed;
      }, (var1) -> {
         this.m_runSpeed = var1;
      });
      this.setVariable("IdleSpeed", () -> {
         return this.m_idleSpeed;
      }, (var1) -> {
         this.m_idleSpeed = var1;
      });
      this.setVariable("DeltaX", () -> {
         return this.m_deltaX;
      }, (var1) -> {
         this.m_deltaX = var1;
      });
      this.setVariable("DeltaY", () -> {
         return this.m_deltaY;
      }, (var1) -> {
         this.m_deltaY = var1;
      });
      this.setVariable("Windspeed", () -> {
         return this.m_windspeed;
      }, (var1) -> {
         this.m_windspeed = var1;
      });
      this.setVariable("WindForce", () -> {
         return this.m_windForce;
      }, (var1) -> {
         this.m_windForce = var1;
      });
      this.setVariable("IPX", () -> {
         return this.m_IPX;
      }, (var1) -> {
         this.m_IPX = var1;
      });
      this.setVariable("IPY", () -> {
         return this.m_IPY;
      }, (var1) -> {
         this.m_IPY = var1;
      });
      this.setVariable("attacktype", () -> {
         return this.attackType;
      });
      this.setVariable("aim", this::isAiming);
      this.setVariable("bdead", () -> {
         return (!GameClient.bClient || this.bSentDeath) && this.isDead() || GameClient.bClient && this.bRemote && this.isDead();
      });
      this.setVariable("bdoshove", () -> {
         return this.bDoShove;
      });
      this.setVariable("bfalling", () -> {
         return this.z > 0.0F && this.fallTime > 2.0F;
      });
      this.setVariable("baimatfloor", this::isAimAtFloor);
      this.setVariable("attackfrombehind", () -> {
         return this.attackFromBehind;
      });
      this.setVariable("bundervehicle", this::isUnderVehicle);
      this.setVariable("reanimatetimer", this::getReanimateTimer);
      this.setVariable("isattacking", this::isAttacking);
      this.setVariable("beensprintingfor", this::getBeenSprintingFor);
      this.setVariable("bannedAttacking", () -> {
         return this.bBannedAttacking;
      });
      this.setVariable("meleePressed", () -> {
         return this.m_meleePressed;
      });
      this.setVariable("AttackAnim", this::isAttackAnim, this::setAttackAnim);
      this.setVariable("Weapon", this::getWeaponType, this::setWeaponType);
      this.setVariable("BumpFall", false);
      this.setVariable("bClient", () -> {
         return GameClient.bClient;
      });
      this.setVariable("IsPerformingAnAction", this::isPerformingAnAction, this::setPerformingAnAction);
   }

   public Vector2 getDeferredMovement(Vector2 var1) {
      super.getDeferredMovement(var1);
      if (DebugOptions.instance.CheatPlayerInvisibleSprint.getValue() && this.isGhostMode() && (this.IsRunning() || this.isSprinting()) && !this.isCurrentState(ClimbOverFenceState.instance()) && !this.isCurrentState(ClimbThroughWindowState.instance())) {
         if (this.getPath2() == null && !this.pressedMovement(false)) {
            return var1.set(0.0F, 0.0F);
         }

         if (this.getCurrentBuilding() != null) {
            var1.scale(2.5F);
            return var1;
         }

         var1.scale(7.5F);
      }

      return var1;
   }

   public float getTurnDelta() {
      return !DebugOptions.instance.CheatPlayerInvisibleSprint.getValue() || !this.isGhostMode() || !this.isRunning() && !this.isSprinting() ? super.getTurnDelta() : 10.0F;
   }

   public void setPerformingAnAction(boolean var1) {
      this.m_isPerformingAnAction = var1;
   }

   public boolean isPerformingAnAction() {
      return this.m_isPerformingAnAction;
   }

   public boolean isAttacking() {
      return !StringUtils.isNullOrWhitespace(this.getAttackType());
   }

   public boolean shouldBeTurning() {
      if (this.isPerformingAnAction()) {
      }

      return super.shouldBeTurning();
   }

   public static void invokeOnPlayerInstance(Runnable var0) {
      synchronized(instanceLock) {
         if (instance != null) {
            var0.run();
         }

      }
   }

   public static IsoPlayer getInstance() {
      return instance;
   }

   public static void setInstance(IsoPlayer var0) {
      synchronized(instanceLock) {
         instance = var0;
      }
   }

   public static boolean hasInstance() {
      return instance != null;
   }

   private static void onTrigger_ResetIsoPlayerModel(String var0) {
      if (instance != null) {
         DebugLog.log(DebugType.General, "DebugFileWatcher Hit. Resetting player model: " + var0);
         instance.resetModel();
      } else {
         DebugLog.log(DebugType.General, "DebugFileWatcher Hit. Player instance null : " + var0);
      }

   }

   public static Stack getStaticTraits() {
      return StaticTraits;
   }

   public static int getFollowDeadCount() {
      return FollowDeadCount;
   }

   public static void setFollowDeadCount(int var0) {
      FollowDeadCount = var0;
   }

   public static ArrayList getAllFileNames() {
      ArrayList var0 = new ArrayList();
      String var10000 = ZomboidFileSystem.instance.getGameModeCacheDir();
      String var1 = var10000 + File.separator + Core.GameSaveWorld;

      for(int var2 = 1; var2 < 100; ++var2) {
         File var3 = new File(var1 + File.separator + "map_p" + var2 + ".bin");
         if (var3.exists()) {
            var0.add("map_p" + var2 + ".bin");
         }
      }

      return var0;
   }

   public static String getUniqueFileName() {
      int var0 = 0;
      String var10000 = ZomboidFileSystem.instance.getGameModeCacheDir();
      String var1 = var10000 + File.separator + Core.GameSaveWorld;

      for(int var2 = 1; var2 < 100; ++var2) {
         File var3 = new File(var1 + File.separator + "map_p" + var2 + ".bin");
         if (var3.exists()) {
            var0 = var2;
         }
      }

      ++var0;
      return ZomboidFileSystem.instance.getFileNameInCurrentSave("map_p" + var0 + ".bin");
   }

   public static ArrayList getAllSavedPlayers() {
      ArrayList var0;
      if (GameClient.bClient) {
         var0 = ClientPlayerDB.getInstance().getAllNetworkPlayers();
      } else {
         var0 = PlayerDB.getInstance().getAllLocalPlayers();
      }

      for(int var1 = var0.size() - 1; var1 >= 0; --var1) {
         if (((IsoPlayer)var0.get(var1)).isDead()) {
            var0.remove(var1);
         }
      }

      return var0;
   }

   public static boolean isServerPlayerIDValid(String var0) {
      if (GameClient.bClient) {
         String var1 = ServerOptions.instance.ServerPlayerID.getValue();
         return var1 != null && !var1.isEmpty() ? var1.equals(var0) : true;
      } else {
         return true;
      }
   }

   public static int getPlayerIndex() {
      return instance == null ? assumedPlayer : instance.PlayerIndex;
   }

   public static boolean allPlayersDead() {
      for(int var0 = 0; var0 < numPlayers; ++var0) {
         if (players[var0] != null && !players[var0].isDead()) {
            return false;
         }
      }

      return IsoWorld.instance == null || IsoWorld.instance.AddCoopPlayers.isEmpty();
   }

   public static ArrayList getPlayers() {
      return new ArrayList(Arrays.asList(players));
   }

   public static boolean allPlayersAsleep() {
      int var0 = 0;
      int var1 = 0;

      for(int var2 = 0; var2 < numPlayers; ++var2) {
         if (players[var2] != null && !players[var2].isDead()) {
            ++var0;
            if (players[var2] != null && players[var2].isAsleep()) {
               ++var1;
            }
         }
      }

      return var0 > 0 && var0 == var1;
   }

   public static boolean getCoopPVP() {
      return CoopPVP;
   }

   public static void setCoopPVP(boolean var0) {
      CoopPVP = var0;
   }

   public void TestZombieSpotPlayer(IsoMovingObject var1) {
      if (GameServer.bServer && var1 instanceof IsoZombie && ((IsoZombie)var1).target != this && ((IsoZombie)var1).isLeadAggro(this)) {
         GameServer.updateZombieControl((IsoZombie)var1, (short)1, this.OnlineID);
      } else {
         var1.spotted(this, false);
         if (var1 instanceof IsoZombie) {
            float var2 = var1.DistTo(this);
            if (var2 < this.closestZombie && !var1.isOnFloor()) {
               this.closestZombie = var2;
            }
         }

      }
   }

   public float getPathSpeed() {
      float var1 = this.getMoveSpeed() * 0.9F;
      switch(this.Moodles.getMoodleLevel(MoodleType.Endurance)) {
      case 1:
         var1 *= 0.95F;
         break;
      case 2:
         var1 *= 0.9F;
         break;
      case 3:
         var1 *= 0.8F;
         break;
      case 4:
         var1 *= 0.6F;
      }

      if (this.stats.enduranceRecharging) {
         var1 *= 0.85F;
      }

      if (this.getMoodles().getMoodleLevel(MoodleType.HeavyLoad) > 0) {
         float var2 = this.getInventory().getCapacityWeight();
         float var3 = (float)this.getMaxWeight();
         float var4 = Math.min(2.0F, var2 / var3) - 1.0F;
         var1 *= 0.65F + 0.35F * (1.0F - var4);
      }

      return var1;
   }

   public boolean isGhostMode() {
      return this.isInvisible();
   }

   public void setGhostMode(boolean var1) {
      this.setInvisible(var1);
   }

   public boolean isSeeEveryone() {
      return Core.bDebug && DebugOptions.instance.CheatPlayerSeeEveryone.getValue();
   }

   public boolean zombiesSwitchOwnershipEachUpdate() {
      return SystemDisabler.zombiesSwitchOwnershipEachUpdate;
   }

   public Vector2 getPlayerMoveDir() {
      return this.playerMoveDir;
   }

   public void setPlayerMoveDir(Vector2 var1) {
      this.playerMoveDir.set(var1);
   }

   public void MoveUnmodded(Vector2 var1) {
      if (this.getSlowFactor() > 0.0F) {
         var1.x *= 1.0F - this.getSlowFactor();
         var1.y *= 1.0F - this.getSlowFactor();
      }

      super.MoveUnmodded(var1);
   }

   public void nullifyAiming() {
      this.isCharging = false;
      this.setIsAiming(false);
   }

   public boolean isAimKeyDown() {
      if (this.PlayerIndex != 0) {
         return false;
      } else {
         int var1 = Core.getInstance().getKey("Aim");
         boolean var2 = GameKeyboard.isKeyDown(var1);
         if (!var2) {
            return false;
         } else {
            boolean var3 = var1 == 29 || var1 == 157;
            return !var3 || !UIManager.isMouseOverInventory();
         }
      }
   }

   private void initializeStates() {
      HashMap var1 = this.getStateUpdateLookup();
      var1.clear();
      if (this.getVehicle() == null) {
         var1.put("actions", PlayerActionsState.instance());
         var1.put("aim", PlayerAimState.instance());
         var1.put("climbfence", ClimbOverFenceState.instance());
         var1.put("climbdownrope", ClimbDownSheetRopeState.instance());
         var1.put("climbrope", ClimbSheetRopeState.instance());
         var1.put("climbwall", ClimbOverWallState.instance());
         var1.put("climbwindow", ClimbThroughWindowState.instance());
         var1.put("emote", PlayerEmoteState.instance());
         var1.put("ext", PlayerExtState.instance());
         var1.put("sitext", PlayerExtState.instance());
         var1.put("falldown", PlayerFallDownState.instance());
         var1.put("falling", PlayerFallingState.instance());
         var1.put("getup", PlayerGetUpState.instance());
         var1.put("idle", IdleState.instance());
         var1.put("melee", SwipeStatePlayer.instance());
         var1.put("shove", SwipeStatePlayer.instance());
         var1.put("ranged", SwipeStatePlayer.instance());
         var1.put("onground", PlayerOnGroundState.instance());
         var1.put("knockeddown", PlayerKnockedDown.instance());
         var1.put("openwindow", OpenWindowState.instance());
         var1.put("closewindow", CloseWindowState.instance());
         var1.put("smashwindow", SmashWindowState.instance());
         var1.put("fishing", FishingState.instance());
         var1.put("fitness", FitnessState.instance());
         var1.put("hitreaction", PlayerHitReactionState.instance());
         var1.put("hitreactionpvp", PlayerHitReactionPVPState.instance());
         var1.put("hitreaction-hit", PlayerHitReactionPVPState.instance());
         var1.put("collide", CollideWithWallState.instance());
         var1.put("bumped", BumpedState.instance());
         var1.put("bumped-bump", BumpedState.instance());
         var1.put("sitonground", PlayerSitOnGroundState.instance());
         var1.put("strafe", PlayerStrafeState.instance());
      } else {
         var1.put("aim", PlayerAimState.instance());
         var1.put("idle", IdleState.instance());
         var1.put("melee", SwipeStatePlayer.instance());
         var1.put("shove", SwipeStatePlayer.instance());
         var1.put("ranged", SwipeStatePlayer.instance());
      }

   }

   public ActionContext getActionContext() {
      return this.actionContext;
   }

   protected void onAnimPlayerCreated(AnimationPlayer var1) {
      super.onAnimPlayerCreated(var1);
      var1.addBoneReparent("Bip01_L_Thigh", "Bip01");
      var1.addBoneReparent("Bip01_R_Thigh", "Bip01");
      var1.addBoneReparent("Bip01_L_Clavicle", "Bip01_Spine1");
      var1.addBoneReparent("Bip01_R_Clavicle", "Bip01_Spine1");
      var1.addBoneReparent("Bip01_Prop1", "Bip01_R_Hand");
      var1.addBoneReparent("Bip01_Prop2", "Bip01_L_Hand");
   }

   public String GetAnimSetName() {
      return this.getVehicle() == null ? "player" : "player-vehicle";
   }

   public boolean IsInMeleeAttack() {
      return this.isCurrentState(SwipeStatePlayer.instance());
   }

   public void load(ByteBuffer var1, int var2, boolean var3) throws IOException {
      byte var4 = var1.get();
      byte var5 = var1.get();
      super.load(var1, var2, var3);
      this.setHoursSurvived(var1.getDouble());
      SurvivorDesc var6 = this.descriptor;
      this.setFemale(var6.isFemale());
      this.InitSpriteParts(var6);
      this.SpeakColour = new Color(Rand.Next(135) + 120, Rand.Next(135) + 120, Rand.Next(135) + 120, 255);
      if (GameClient.bClient) {
         if (Core.getInstance().getMpTextColor() != null) {
            this.SpeakColour = new Color(Core.getInstance().getMpTextColor().r, Core.getInstance().getMpTextColor().g, Core.getInstance().getMpTextColor().b, 1.0F);
         } else {
            Core.getInstance().setMpTextColor(new ColorInfo(this.SpeakColour.r, this.SpeakColour.g, this.SpeakColour.b, 1.0F));

            try {
               Core.getInstance().saveOptions();
            } catch (IOException var15) {
               var15.printStackTrace();
            }
         }
      }

      this.setZombieKills(var1.getInt());
      ArrayList var7 = this.savedInventoryItems;
      byte var8 = var1.get();

      short var11;
      for(int var9 = 0; var9 < var8; ++var9) {
         String var10 = GameWindow.ReadString(var1);
         var11 = var1.getShort();
         if (var11 >= 0 && var11 < var7.size() && this.wornItems.getBodyLocationGroup().getLocation(var10) != null) {
            this.wornItems.setItem(var10, (InventoryItem)var7.get(var11));
         }
      }

      short var16 = var1.getShort();
      if (var16 >= 0 && var16 < var7.size()) {
         this.leftHandItem = (InventoryItem)var7.get(var16);
      }

      var16 = var1.getShort();
      if (var16 >= 0 && var16 < var7.size()) {
         this.rightHandItem = (InventoryItem)var7.get(var16);
      }

      this.setVariable("Weapon", WeaponType.getWeaponType((IsoGameCharacter)this).type);
      this.setSurvivorKills(var1.getInt());
      this.initSpritePartsEmpty();
      this.nutrition.load(var1);
      this.setAllChatMuted(var1.get() == 1);
      this.tagPrefix = GameWindow.ReadString(var1);
      this.setTagColor(new ColorInfo(var1.getFloat(), var1.getFloat(), var1.getFloat(), 1.0F));
      this.setDisplayName(GameWindow.ReadString(var1));
      this.showTag = var1.get() == 1;
      this.factionPvp = var1.get() == 1;
      if (var2 >= 176) {
         this.noClip = var1.get() == 1;
      }

      if (var1.get() == 1) {
         this.savedVehicleX = var1.getFloat();
         this.savedVehicleY = var1.getFloat();
         this.savedVehicleSeat = (short)var1.get();
         this.savedVehicleRunning = var1.get() == 1;
         this.z = 0.0F;
      }

      int var17 = var1.getInt();

      int var18;
      for(var18 = 0; var18 < var17; ++var18) {
         this.mechanicsItem.put(var1.getLong(), var1.getLong());
      }

      this.fitness.load(var1, var2);
      int var12;
      if (var2 >= 184) {
         var11 = var1.getShort();

         for(var12 = 0; var12 < var11; ++var12) {
            short var13 = var1.getShort();
            String var14 = WorldDictionary.getItemTypeFromID(var13);
            if (var14 != null) {
               this.alreadyReadBook.add(var14);
            }
         }
      } else if (var2 >= 182) {
         var18 = var1.getInt();

         for(var12 = 0; var12 < var18; ++var12) {
            this.alreadyReadBook.add(GameWindow.ReadString(var1));
         }
      }

   }

   public void save(ByteBuffer var1, boolean var2) throws IOException {
      IsoPlayer var3 = instance;
      instance = this;

      try {
         super.save(var1, var2);
      } finally {
         instance = var3;
      }

      var1.putDouble(this.getHoursSurvived());
      var1.putInt(this.getZombieKills());
      if (this.wornItems.size() > 127) {
         throw new RuntimeException("too many worn items");
      } else {
         var1.put((byte)this.wornItems.size());
         this.wornItems.forEach((var2x) -> {
            GameWindow.WriteString(var1, var2x.getLocation());
            var1.putShort((short)this.savedInventoryItems.indexOf(var2x.getItem()));
         });
         var1.putShort((short)this.savedInventoryItems.indexOf(this.getPrimaryHandItem()));
         var1.putShort((short)this.savedInventoryItems.indexOf(this.getSecondaryHandItem()));
         var1.putInt(this.getSurvivorKills());
         this.nutrition.save(var1);
         var1.put((byte)(this.isAllChatMuted() ? 1 : 0));
         GameWindow.WriteString(var1, this.tagPrefix);
         var1.putFloat(this.getTagColor().r);
         var1.putFloat(this.getTagColor().g);
         var1.putFloat(this.getTagColor().b);
         GameWindow.WriteString(var1, this.displayName);
         var1.put((byte)(this.showTag ? 1 : 0));
         var1.put((byte)(this.factionPvp ? 1 : 0));
         var1.put((byte)(this.isNoClip() ? 1 : 0));
         if (this.vehicle != null) {
            var1.put((byte)1);
            var1.putFloat(this.vehicle.x);
            var1.putFloat(this.vehicle.y);
            var1.put((byte)this.vehicle.getSeat(this));
            var1.put((byte)(this.vehicle.isEngineRunning() ? 1 : 0));
         } else {
            var1.put((byte)0);
         }

         var1.putInt(this.mechanicsItem.size());
         Iterator var4 = this.mechanicsItem.keySet().iterator();

         while(var4.hasNext()) {
            Long var5 = (Long)var4.next();
            var1.putLong(var5);
            var1.putLong((Long)this.mechanicsItem.get(var5));
         }

         this.fitness.save(var1);
         var1.putShort((short)this.alreadyReadBook.size());

         for(int var8 = 0; var8 < this.alreadyReadBook.size(); ++var8) {
            var1.putShort(WorldDictionary.getItemRegistryID((String)this.alreadyReadBook.get(var8)));
         }

      }
   }

   public void save() throws IOException {
      synchronized(SliceY.SliceBufferLock) {
         ByteBuffer var2 = SliceY.SliceBuffer;
         var2.clear();
         var2.put((byte)80);
         var2.put((byte)76);
         var2.put((byte)89);
         var2.put((byte)82);
         var2.putInt(186);
         GameWindow.WriteString(var2, this.bMultiplayer ? ServerOptions.instance.ServerPlayerID.getValue() : "");
         var2.putInt((int)(this.x / 10.0F));
         var2.putInt((int)(this.y / 10.0F));
         var2.putInt((int)this.x);
         var2.putInt((int)this.y);
         var2.putInt((int)this.z);
         this.save(var2);
         String var10002 = ZomboidFileSystem.instance.getGameModeCacheDir();
         File var3 = new File(var10002 + Core.GameSaveWorld + File.separator + "map_p.bin");
         if (!Core.getInstance().isNoSave()) {
            FileOutputStream var4 = new FileOutputStream(var3);

            try {
               BufferedOutputStream var5 = new BufferedOutputStream(var4);

               try {
                  var5.write(var2.array(), 0, var2.position());
               } catch (Throwable var11) {
                  try {
                     var5.close();
                  } catch (Throwable var10) {
                     var11.addSuppressed(var10);
                  }

                  throw var11;
               }

               var5.close();
            } catch (Throwable var12) {
               try {
                  var4.close();
               } catch (Throwable var9) {
                  var12.addSuppressed(var9);
               }

               throw var12;
            }

            var4.close();
         }

         if (this.getVehicle() != null && !GameClient.bClient) {
            VehiclesDB2.instance.updateVehicleAndTrailer(this.getVehicle());
         }

      }
   }

   public void save(String var1) throws IOException {
      this.SaveFileName = var1;
      synchronized(SliceY.SliceBufferLock) {
         SliceY.SliceBuffer.clear();
         SliceY.SliceBuffer.putInt(186);
         GameWindow.WriteString(SliceY.SliceBuffer, this.bMultiplayer ? ServerOptions.instance.ServerPlayerID.getValue() : "");
         this.save(SliceY.SliceBuffer);
         File var3 = (new File(var1)).getAbsoluteFile();
         FileOutputStream var4 = new FileOutputStream(var3);

         try {
            BufferedOutputStream var5 = new BufferedOutputStream(var4);

            try {
               var5.write(SliceY.SliceBuffer.array(), 0, SliceY.SliceBuffer.position());
            } catch (Throwable var11) {
               try {
                  var5.close();
               } catch (Throwable var10) {
                  var11.addSuppressed(var10);
               }

               throw var11;
            }

            var5.close();
         } catch (Throwable var12) {
            try {
               var4.close();
            } catch (Throwable var9) {
               var12.addSuppressed(var9);
            }

            throw var12;
         }

         var4.close();
      }
   }

   public void load(String var1) throws IOException {
      File var2 = (new File(var1)).getAbsoluteFile();
      if (var2.exists()) {
         this.SaveFileName = var1;
         FileInputStream var3 = new FileInputStream(var2);

         try {
            BufferedInputStream var4 = new BufferedInputStream(var3);

            try {
               synchronized(SliceY.SliceBufferLock) {
                  SliceY.SliceBuffer.clear();
                  int var6 = var4.read(SliceY.SliceBuffer.array());
                  SliceY.SliceBuffer.limit(var6);
                  int var7 = SliceY.SliceBuffer.getInt();
                  if (var7 >= 69) {
                     this.SaveFileIP = GameWindow.ReadStringUTF(SliceY.SliceBuffer);
                     if (var7 < 71) {
                        this.SaveFileIP = ServerOptions.instance.ServerPlayerID.getValue();
                     }
                  } else if (GameClient.bClient) {
                     this.SaveFileIP = ServerOptions.instance.ServerPlayerID.getValue();
                  }

                  this.load(SliceY.SliceBuffer, var7);
               }
            } catch (Throwable var12) {
               try {
                  var4.close();
               } catch (Throwable var10) {
                  var12.addSuppressed(var10);
               }

               throw var12;
            }

            var4.close();
         } catch (Throwable var13) {
            try {
               var3.close();
            } catch (Throwable var9) {
               var13.addSuppressed(var9);
            }

            throw var13;
         }

         var3.close();
      }
   }

   public void setVehicle4TestCollision(BaseVehicle var1) {
      this.vehicle4testCollision = var1;
   }

   public boolean isSaveFileInUse() {
      for(int var1 = 0; var1 < numPlayers; ++var1) {
         IsoPlayer var2 = players[var1];
         if (var2 != null) {
            if (this.sqlID != -1 && this.sqlID == var2.sqlID) {
               return true;
            }

            if (this.SaveFileName != null && this.SaveFileName.equals(var2.SaveFileName)) {
               return true;
            }
         }
      }

      return false;
   }

   public void removeSaveFile() {
      try {
         if (PlayerDB.isAvailable()) {
            PlayerDB.getInstance().saveLocalPlayersForce();
         }

         if (this.isNPC() && this.SaveFileName != null) {
            File var1 = (new File(this.SaveFileName)).getAbsoluteFile();
            if (var1.exists()) {
               var1.delete();
            }
         }
      } catch (Exception var2) {
         ExceptionLogger.logException(var2);
      }

   }

   public boolean isSaveFileIPValid() {
      return isServerPlayerIDValid(this.SaveFileIP);
   }

   public String getObjectName() {
      return "Player";
   }

   public int getJoypadBind() {
      return this.JoypadBind;
   }

   public boolean isLBPressed() {
      return this.JoypadBind == -1 ? false : JoypadManager.instance.isLBPressed(this.JoypadBind);
   }

   public Vector2 getControllerAimDir(Vector2 var1) {
      if (GameWindow.ActivatedJoyPad != null && this.JoypadBind != -1 && this.bJoypadMovementActive) {
         float var2 = JoypadManager.instance.getAimingAxisX(this.JoypadBind);
         float var3 = JoypadManager.instance.getAimingAxisY(this.JoypadBind);
         if (this.bJoypadIgnoreAimUntilCentered) {
            if (var1.set(var2, var3).getLengthSquared() > 0.0F) {
               return var1.set(0.0F, 0.0F);
            }

            this.bJoypadIgnoreAimUntilCentered = false;
         }

         if (var1.set(var2, var3).getLength() < 0.3F) {
            var3 = 0.0F;
            var2 = 0.0F;
         }

         if (var2 == 0.0F && var3 == 0.0F) {
            return var1.set(0.0F, 0.0F);
         }

         var1.set(var2, var3);
         var1.normalize();
         var1.rotate(-0.7853982F);
      }

      return var1;
   }

   public Vector2 getMouseAimVector(Vector2 var1) {
      int var2 = Mouse.getX();
      int var3 = Mouse.getY();
      var1.x = IsoUtils.XToIso((float)var2, (float)var3 + 55.0F * this.def.getScaleY(), this.getZ()) - this.getX();
      var1.y = IsoUtils.YToIso((float)var2, (float)var3 + 55.0F * this.def.getScaleY(), this.getZ()) - this.getY();
      var1.normalize();
      return var1;
   }

   public Vector2 getAimVector(Vector2 var1) {
      return this.JoypadBind == -1 ? this.getMouseAimVector(var1) : this.getControllerAimDir(var1);
   }

   public float getGlobalMovementMod(boolean var1) {
      return !this.isGhostMode() && !this.isNoClip() ? super.getGlobalMovementMod(var1) : 1.0F;
   }

   public boolean isInTrees2(boolean var1) {
      return !this.isGhostMode() && !this.isNoClip() ? super.isInTrees2(var1) : false;
   }

   public float getMoveSpeed() {
      float var1 = 1.0F;

      for(int var2 = BodyPartType.ToIndex(BodyPartType.UpperLeg_L); var2 <= BodyPartType.ToIndex(BodyPartType.Foot_R); ++var2) {
         BodyPart var3 = this.getBodyDamage().getBodyPart(BodyPartType.FromIndex(var2));
         float var4 = 1.0F;
         if (var3.getFractureTime() > 20.0F) {
            var4 = 0.4F;
            if (var3.getFractureTime() > 50.0F) {
               var4 = 0.3F;
            }

            if (var3.getSplintFactor() > 0.0F) {
               var4 += var3.getSplintFactor() / 10.0F;
            }
         }

         if (var3.getFractureTime() < 20.0F && var3.getSplintFactor() > 0.0F) {
            var4 = 0.8F;
         }

         if (var4 > 0.7F && var3.getDeepWoundTime() > 0.0F) {
            var4 = 0.7F;
            if (var3.bandaged()) {
               var4 += 0.2F;
            }
         }

         if (var4 < var1) {
            var1 = var4;
         }
      }

      if (var1 != 1.0F) {
         return this.MoveSpeed * var1;
      } else if (this.getMoodles().getMoodleLevel(MoodleType.Panic) >= 4 && this.Traits.AdrenalineJunkie.isSet()) {
         float var5 = 1.0F;
         int var6 = this.getMoodles().getMoodleLevel(MoodleType.Panic) + 1;
         var5 += (float)var6 / 50.0F;
         return this.MoveSpeed * var5;
      } else {
         return this.MoveSpeed;
      }
   }

   public void setMoveSpeed(float var1) {
      this.MoveSpeed = var1;
   }

   public float getTorchStrength() {
      if (this.bRemote) {
         return this.mpTorchStrength;
      } else {
         InventoryItem var1 = this.getActiveLightItem();
         return var1 != null ? var1.getLightStrength() : 0.0F;
      }
   }

   public float getInvAimingMod() {
      int var1 = this.getPerkLevel(PerkFactory.Perks.Aiming);
      if (var1 == 1) {
         return 0.9F;
      } else if (var1 == 2) {
         return 0.86F;
      } else if (var1 == 3) {
         return 0.82F;
      } else if (var1 == 4) {
         return 0.74F;
      } else if (var1 == 5) {
         return 0.7F;
      } else if (var1 == 6) {
         return 0.66F;
      } else if (var1 == 7) {
         return 0.62F;
      } else if (var1 == 8) {
         return 0.58F;
      } else if (var1 == 9) {
         return 0.54F;
      } else {
         return var1 == 10 ? 0.5F : 0.9F;
      }
   }

   public float getAimingMod() {
      int var1 = this.getPerkLevel(PerkFactory.Perks.Aiming);
      if (var1 == 1) {
         return 1.1F;
      } else if (var1 == 2) {
         return 1.14F;
      } else if (var1 == 3) {
         return 1.18F;
      } else if (var1 == 4) {
         return 1.22F;
      } else if (var1 == 5) {
         return 1.26F;
      } else if (var1 == 6) {
         return 1.3F;
      } else if (var1 == 7) {
         return 1.34F;
      } else if (var1 == 8) {
         return 1.36F;
      } else if (var1 == 9) {
         return 1.4F;
      } else {
         return var1 == 10 ? 1.5F : 1.0F;
      }
   }

   public float getReloadingMod() {
      int var1 = this.getPerkLevel(PerkFactory.Perks.Reloading);
      return 3.5F - (float)var1 * 0.25F;
   }

   public float getAimingRangeMod() {
      int var1 = this.getPerkLevel(PerkFactory.Perks.Aiming);
      if (var1 == 1) {
         return 1.2F;
      } else if (var1 == 2) {
         return 1.28F;
      } else if (var1 == 3) {
         return 1.36F;
      } else if (var1 == 4) {
         return 1.42F;
      } else if (var1 == 5) {
         return 1.5F;
      } else if (var1 == 6) {
         return 1.58F;
      } else if (var1 == 7) {
         return 1.66F;
      } else if (var1 == 8) {
         return 1.72F;
      } else if (var1 == 9) {
         return 1.8F;
      } else {
         return var1 == 10 ? 2.0F : 1.1F;
      }
   }

   public boolean isPathfindRunning() {
      return this.pathfindRun;
   }

   public void setPathfindRunning(boolean var1) {
      this.pathfindRun = var1;
   }

   public boolean isBannedAttacking() {
      return this.bBannedAttacking;
   }

   public void setBannedAttacking(boolean var1) {
      this.bBannedAttacking = var1;
   }

   public float getInvAimingRangeMod() {
      int var1 = this.getPerkLevel(PerkFactory.Perks.Aiming);
      if (var1 == 1) {
         return 0.8F;
      } else if (var1 == 2) {
         return 0.7F;
      } else if (var1 == 3) {
         return 0.62F;
      } else if (var1 == 4) {
         return 0.56F;
      } else if (var1 == 5) {
         return 0.45F;
      } else if (var1 == 6) {
         return 0.38F;
      } else if (var1 == 7) {
         return 0.31F;
      } else if (var1 == 8) {
         return 0.24F;
      } else if (var1 == 9) {
         return 0.17F;
      } else {
         return var1 == 10 ? 0.1F : 0.8F;
      }
   }

   private void updateCursorVisibility() {
      if (this.isAiming()) {
         if (this.PlayerIndex == 0 && this.JoypadBind == -1 && !this.isDead()) {
            if (!Core.getInstance().getOptionShowCursorWhileAiming()) {
               if (Core.getInstance().getIsoCursorVisibility() != 0) {
                  if (!UIManager.isForceCursorVisible()) {
                     int var1 = Mouse.getXA();
                     int var2 = Mouse.getYA();
                     if (var1 >= IsoCamera.getScreenLeft(0) && var1 <= IsoCamera.getScreenLeft(0) + IsoCamera.getScreenWidth(0)) {
                        if (var2 >= IsoCamera.getScreenTop(0) && var2 <= IsoCamera.getScreenTop(0) + IsoCamera.getScreenHeight(0)) {
                           Mouse.setCursorVisible(false);
                        }
                     }
                  }
               }
            }
         }
      }
   }

   public void render(float var1, float var2, float var3, ColorInfo var4, boolean var5, boolean var6, Shader var7) {
      if (DebugOptions.instance.Character.Debug.Render.DisplayRoomAndZombiesZone.getValue()) {
         String var8 = "";
         if (this.getCurrentRoomDef() != null) {
            var8 = this.getCurrentRoomDef().name;
         }

         IsoMetaGrid.Zone var9 = ZombiesZoneDefinition.getDefinitionZoneAt((int)var1, (int)var2, (int)var3);
         if (var9 != null) {
            var8 = var8 + " - " + var9.name + " / " + var9.type;
         }

         this.Say(var8);
      }

      if (!getInstance().checkCanSeeClient(this)) {
         this.setTargetAlpha(0.0F);
         getInstance().spottedPlayerTimer.remove(this.getRemoteID());
      } else {
         this.setTargetAlpha(1.0F);
      }

      super.render(var1, var2, var3, var4, var5, var6, var7);
   }

   public void renderlast() {
      super.renderlast();
   }

   public float doBeatenVehicle(float var1) {
      if (GameClient.bClient && this.isLocalPlayer()) {
         this.changeState(ForecastBeatenPlayerState.instance());
         return 0.0F;
      } else if (!GameClient.bClient && !this.isLocalPlayer()) {
         return 0.0F;
      } else {
         float var2 = this.getDamageFromHitByACar(var1);
         if (this.isAlive()) {
            if (GameClient.bClient) {
               if (this.isCurrentState(PlayerSitOnGroundState.instance())) {
                  this.setKnockedDown(true);
                  this.setReanimateTimer(20.0F);
               } else if (!this.isOnFloor() && !(var1 > 15.0F) && !this.isCurrentState(PlayerHitReactionState.instance()) && !this.isCurrentState(PlayerGetUpState.instance()) && !this.isCurrentState(PlayerOnGroundState.instance())) {
                  this.setHitReaction("HitReaction");
                  this.actionContext.reportEvent("washit");
                  this.setVariable("hitpvp", false);
               } else {
                  this.setHitReaction("HitReaction");
                  this.actionContext.reportEvent("washit");
                  this.setVariable("hitpvp", false);
                  this.setKnockedDown(true);
                  this.setReanimateTimer(20.0F);
               }
            } else if (this.getCurrentState() != PlayerHitReactionState.instance() && this.getCurrentState() != PlayerFallDownState.instance() && this.getCurrentState() != PlayerOnGroundState.instance() && !this.isKnockedDown()) {
               if (var2 > 15.0F) {
                  this.setKnockedDown(true);
                  this.setReanimateTimer((float)(20 + Rand.Next(60)));
               }

               this.setHitReaction("HitReaction");
               this.actionContext.reportEvent("washit");
            }
         }

         return var2;
      }
   }

   public void update() {
      IsoPlayer.s_performance.update.invokeAndMeasure(this, IsoPlayer::updateInternal1);
   }

   private void updateInternal1() {
      if (this.replay != null) {
         this.replay.update();
      }

      boolean var1 = this.updateInternal2();
      GameClient.instance.sendPlayer2(this);
      if (var1) {
         if (!this.bRemote) {
            this.updateLOS();
         }

         super.update();
      }

   }

   private void setBeenMovingSprinting() {
      if (this.isJustMoved()) {
         this.setBeenMovingFor(this.getBeenMovingFor() + 1.25F * GameTime.getInstance().getMultiplier());
      } else {
         this.setBeenMovingFor(this.getBeenMovingFor() - 0.625F * GameTime.getInstance().getMultiplier());
      }

      if (this.isJustMoved() && this.isSprinting()) {
         this.setBeenSprintingFor(this.getBeenSprintingFor() + 1.25F * GameTime.getInstance().getMultiplier());
      } else {
         this.setBeenSprintingFor(0.0F);
      }

   }

   private boolean updateInternal2() {
      if (isTestAIMode) {
         this.isNPC = true;
      }

      if (!this.attackStarted) {
         this.setInitiateAttack(false);
         this.setAttackType((String)null);
      }

      if ((this.isRunning() || this.isSprinting()) && this.getDeferredMovement(tempo).getLengthSquared() > 0.0F) {
         this.runningTime += GameTime.getInstance().getMultiplier() / 1.6F;
      } else {
         this.runningTime = 0.0F;
      }

      if (this.getLastCollideTime() > 0.0F) {
         this.setLastCollideTime(this.getLastCollideTime() - GameTime.getInstance().getMultiplier() / 1.6F);
      }

      this.updateDeathDragDown();
      this.updateGodModeKey();
      if (GameClient.bClient) {
         this.networkAI.update();
      }

      this.doDeferredMovement();
      if (GameServer.bServer) {
         this.vehicle4testCollision = null;
      } else if (GameClient.bClient) {
         if (this.vehicle4testCollision != null) {
            if (!this.isLocal()) {
               this.vehicle4testCollision.updateHitByVehicle(this);
            }

            this.vehicle4testCollision = null;
         }
      } else {
         this.updateHitByVehicle();
         this.vehicle4testCollision = null;
      }

      this.updateEmitter();
      this.updateMechanicsItems();
      this.updateHeavyBreathing();
      this.updateTemperatureCheck();
      this.updateAimingStance();
      if (SystemDisabler.doCharacterStats) {
         this.nutrition.update();
      }

      this.fitness.update();
      this.updateSoundListener();
      if (GameClient.bClient && this.isLocalPlayer() && this.getSafetyCooldown() > 0.0F) {
         this.setSafetyCooldown(this.getSafetyCooldown() - GameTime.instance.getRealworldSecondsSinceLastUpdate());
      }

      if (!GameClient.bClient && !GameServer.bServer && this.bDeathFinished) {
         return false;
      } else {
         if (!GameClient.bClient && this.getCurrentBuildingDef() != null && !this.isInvisible()) {
            this.getCurrentBuildingDef().setHasBeenVisited(true);
         }

         if (this.checkSafehouse > 0 && GameServer.bServer) {
            --this.checkSafehouse;
            if (this.checkSafehouse == 0) {
               this.checkSafehouse = 200;
               SafeHouse var1 = SafeHouse.isSafeHouse(this.getCurrentSquare(), (String)null, false);
               if (var1 != null) {
                  var1.updateSafehouse(this);
               }
            }
         }

         if (this.bRemote && this.TimeSinceLastNetData > 600) {
            IsoWorld.instance.CurrentCell.getObjectList().remove(this);
            if (this.movingSq != null) {
               this.movingSq.getMovingObjects().remove(this);
            }
         }

         this.TimeSinceLastNetData = (int)((float)this.TimeSinceLastNetData + GameTime.instance.getMultiplier());
         this.TimeSinceOpenDoor += GameTime.instance.getMultiplier();
         this.lastTargeted += GameTime.instance.getMultiplier();
         this.targetedByZombie = false;
         this.checkActionGroup();
         if (this.updateRemotePlayer()) {
            if (this.updateWhileDead()) {
               return true;
            } else {
               this.updateHeartSound();
               this.checkIsNearWall();
               this.updateExt();
               this.setBeenMovingSprinting();
               return true;
            }
         } else {
            assert !GameServer.bServer;

            assert !this.bRemote;

            assert !GameClient.bClient || this.isLocalPlayer();

            IsoCamera.CamCharacter = this;
            instance = this;
            if (this.isLocalPlayer()) {
               IsoCamera.cameras[this.PlayerIndex].update();
               if (UIManager.getMoodleUI((double)this.PlayerIndex) != null) {
                  UIManager.getMoodleUI((double)this.PlayerIndex).setCharacter(this);
               }
            }

            if (this.closestZombie > 1.2F) {
               this.slowTimer = -1.0F;
               this.slowFactor = 0.0F;
            }

            this.ContextPanic -= 1.5F * GameTime.instance.getTimeDelta();
            if (this.ContextPanic < 0.0F) {
               this.ContextPanic = 0.0F;
            }

            this.lastSeenZombieTime += (double)(GameTime.instance.getGameWorldSecondsSinceLastUpdate() / 60.0F / 60.0F);
            LuaEventManager.triggerEvent("OnPlayerUpdate", this);
            if (this.pressedMovement(false)) {
               this.ContextPanic = 0.0F;
               this.ticksSincePressedMovement = 0.0F;
            } else {
               this.ticksSincePressedMovement += GameTime.getInstance().getMultiplier() / 1.6F;
            }

            this.setVariable("pressedMovement", this.pressedMovement(true));
            if (this.updateWhileDead()) {
               return true;
            } else {
               this.updateHeartSound();
               this.updateWorldAmbiance();
               this.updateSneakKey();
               this.checkIsNearWall();
               this.updateExt();
               this.updateInteractKeyPanic();
               if (this.isAsleep()) {
                  this.m_isPlayerMoving = false;
               }

               if ((this.getVehicle() == null || !this.getVehicle().isDriver(this) || !this.getVehicle().hasHorn() || Core.getInstance().getKey("Shout") != Core.getInstance().getKey("VehicleHorn")) && !this.isAsleep() && this.PlayerIndex == 0 && !this.Speaking && GameKeyboard.isKeyDown(Core.getInstance().getKey("Shout")) && !this.isNPC) {
               }

               if (!this.getIgnoreMovement() && !this.isAsleep()) {
                  if (this.checkActionsBlockingMovement()) {
                     if (this.getVehicle() != null && this.getVehicle().getDriver() == this && this.getVehicle().getController() != null) {
                        this.getVehicle().getController().clientControls.reset();
                        this.getVehicle().updatePhysics();
                     }

                     return true;
                  } else {
                     this.enterExitVehicle();
                     this.checkActionGroup();
                     this.checkReloading();
                     if (this.checkActionsBlockingMovement()) {
                        return true;
                     } else if (this.getVehicle() != null) {
                        this.updateWhileInVehicle();
                        return true;
                     } else {
                        this.checkVehicleContainers();
                        this.setCollidable(true);
                        this.updateCursorVisibility();
                        this.bSeenThisFrame = false;
                        this.bCouldBeSeenThisFrame = false;
                        if (IsoCamera.CamCharacter == null && GameClient.bClient) {
                           IsoCamera.CamCharacter = instance;
                        }

                        if (this.updateUseKey()) {
                           return true;
                        } else {
                           this.updateEnableModelsKey();
                           this.updateChangeCharacterKey();
                           boolean var12 = false;
                           boolean var2 = false;
                           this.setRunning(false);
                           this.setSprinting(false);
                           this.useChargeTime = this.chargeTime;
                           if (!this.isBlockMovement() && !this.isNPC) {
                              if (!this.isCharging && !this.isChargingLT) {
                                 this.chargeTime = 0.0F;
                              } else {
                                 this.chargeTime += 1.0F * GameTime.instance.getMultiplier();
                              }

                              this.UpdateInputState(this.inputState);
                              var2 = this.inputState.bMelee;
                              var12 = this.inputState.isAttacking;
                              this.setRunning(this.inputState.bRunning);
                              this.setSprinting(this.inputState.bSprinting);
                              if (this.isSprinting() && !this.isJustMoved()) {
                                 this.setSprinting(false);
                              }

                              if (this.isSprinting()) {
                                 this.setRunning(false);
                              }

                              this.setIsAiming(this.inputState.isAiming);
                              this.isCharging = this.inputState.isCharging;
                              this.isChargingLT = this.inputState.isChargingLT;
                              this.updateMovementRates();
                              if (this.isAiming()) {
                                 this.StopAllActionQueueAiming();
                              }

                              if (var12) {
                                 this.setIsAiming(true);
                              }

                              this.Waiting = false;
                              if (this.isAiming()) {
                                 this.setMoving(false);
                                 this.setRunning(false);
                                 this.setSprinting(false);
                              }

                              ++this.TicksSinceSeenZombie;
                           }

                           if ((double)this.playerMoveDir.x == 0.0D && (double)this.playerMoveDir.y == 0.0D) {
                              this.setForceRun(false);
                              this.setForceSprint(false);
                           }

                           this.movementLastFrame.x = this.playerMoveDir.x;
                           this.movementLastFrame.y = this.playerMoveDir.y;
                           if (this.stateMachine.getCurrent() != StaggerBackState.instance() && this.stateMachine.getCurrent() != FakeDeadZombieState.instance() && UIManager.speedControls != null) {
                              if (GameKeyboard.isKeyDown(88) && Translator.debug) {
                                 Translator.loadFiles();
                              }

                              this.setJustMoved(false);
                              IsoPlayer.MoveVars var3 = s_moveVars;
                              this.updateMovementFromInput(var3);
                              if (!this.JustMoved && this.hasPath() && !this.getPathFindBehavior2().bStopping) {
                                 this.JustMoved = true;
                              }

                              float var4 = var3.strafeX;
                              float var5 = var3.strafeY;
                              if (this.isJustMoved() && !this.isNPC && !this.hasPath()) {
                                 if (UIManager.getSpeedControls().getCurrentGameSpeed() > 1) {
                                    UIManager.getSpeedControls().SetCurrentGameSpeed(1);
                                 }
                              } else if (this.stats.endurance < this.stats.endurancedanger && Rand.Next((int)(300.0F * GameTime.instance.getInvMultiplier())) == 0) {
                                 this.xp.AddXP(PerkFactory.Perks.Fitness, 1.0F);
                              }

                              this.setBeenMovingSprinting();
                              float var6 = 1.0F;
                              float var7 = 0.0F;
                              if (this.isJustMoved() && !this.isNPC) {
                                 if (!this.isRunning() && !this.isSprinting()) {
                                    var7 = 1.0F;
                                 } else {
                                    var7 = 1.5F;
                                 }
                              }

                              var6 *= var7;
                              if (var6 > 1.0F) {
                                 var6 *= this.getSprintMod();
                              }

                              if (var6 > 1.0F && this.Traits.Athletic.isSet()) {
                                 var6 *= 1.2F;
                              }

                              if (var6 > 1.0F) {
                                 if (this.Traits.Overweight.isSet()) {
                                    var6 *= 0.99F;
                                 }

                                 if (this.Traits.Obese.isSet()) {
                                    var6 *= 0.85F;
                                 }

                                 if (this.getNutrition().getWeight() > 120.0F) {
                                    var6 *= 0.97F;
                                 }

                                 if (this.Traits.OutOfShape.isSet()) {
                                    var6 *= 0.99F;
                                 }

                                 if (this.Traits.Unfit.isSet()) {
                                    var6 *= 0.8F;
                                 }
                              }

                              this.updateEndurance(var6);
                              if (this.isAiming() && this.isJustMoved()) {
                                 var6 *= 0.7F;
                              }

                              if (this.isAiming()) {
                                 var6 *= this.getNimbleMod();
                              }

                              this.isWalking = false;
                              if (var6 > 0.0F && !this.isNPC) {
                                 this.isWalking = true;
                                 LuaEventManager.triggerEvent("OnPlayerMove", this);
                              }

                              if (this.isJustMoved()) {
                                 this.sprite.Animate = true;
                              }

                              if (this.isNPC && this.GameCharacterAIBrain != null) {
                                 var2 = this.GameCharacterAIBrain.HumanControlVars.bMelee;
                                 this.bBannedAttacking = this.GameCharacterAIBrain.HumanControlVars.bBannedAttacking;
                              }

                              this.m_meleePressed = var2;
                              if (var2) {
                                 if (!this.m_lastAttackWasShove) {
                                    this.setMeleeDelay(Math.min(this.getMeleeDelay(), 2.0F));
                                 }

                                 if (!this.bBannedAttacking && this.isAuthorizeShoveStomp() && this.CanAttack() && this.getMeleeDelay() <= 0.0F) {
                                    this.setDoShove(true);
                                    if (!this.isCharging && !this.isChargingLT) {
                                       this.setIsAiming(false);
                                    }

                                    this.AttemptAttack(this.useChargeTime);
                                    this.useChargeTime = 0.0F;
                                    this.chargeTime = 0.0F;
                                 }
                              } else if (this.isAiming() && this.CanAttack()) {
                                 if (this.DragCharacter != null) {
                                    this.DragObject = null;
                                    this.DragCharacter.Dragging = false;
                                    this.DragCharacter = null;
                                 }

                                 if (var12 && !this.bBannedAttacking) {
                                    this.sprite.Animate = true;
                                    if (this.getRecoilDelay() <= 0.0F && this.getMeleeDelay() <= 0.0F) {
                                       this.AttemptAttack(this.useChargeTime);
                                    }

                                    this.useChargeTime = 0.0F;
                                    this.chargeTime = 0.0F;
                                 }
                              }

                              if (this.isAiming() && !this.isNPC) {
                                 if (this.JoypadBind != -1 && !this.bJoypadMovementActive) {
                                    if (this.getForwardDirection().getLengthSquared() > 0.0F) {
                                       this.DirectionFromVector(this.getForwardDirection());
                                    }
                                 } else {
                                    Vector2 var8 = tempVector2.set(0.0F, 0.0F);
                                    if (GameWindow.ActivatedJoyPad != null && this.JoypadBind != -1) {
                                       this.getControllerAimDir(var8);
                                    } else {
                                       this.getMouseAimVector(var8);
                                    }

                                    if (var8.getLengthSquared() > 0.0F) {
                                       this.DirectionFromVector(var8);
                                       this.setForwardDirection(var8);
                                    }
                                 }

                                 var3.NewFacing = this.dir;
                              }

                              if (this.getForwardDirection().x == 0.0F && this.getForwardDirection().y == 0.0F) {
                                 this.setForwardDirection(this.dir.ToVector());
                              }

                              if (this.lastAngle.x != this.getForwardDirection().x || this.lastAngle.y != this.getForwardDirection().y) {
                                 this.lastAngle.x = this.getForwardDirection().x;
                                 this.lastAngle.y = this.getForwardDirection().y;
                                 this.dirtyRecalcGridStackTime = 2.0F;
                              }

                              this.stats.endurance = PZMath.clamp(this.stats.endurance, 0.0F, 1.0F);
                              AnimationPlayer var13 = this.getAnimationPlayer();
                              if (var13 != null && var13.isReady()) {
                                 float var9 = var13.getAngle() + var13.getTwistAngle();
                                 this.dir = IsoDirections.fromAngle(tempVector2.setLengthAndDirection(var9, 1.0F));
                              } else if (!this.bFalling && !this.isAiming() && !var12) {
                                 this.dir = var3.NewFacing;
                              }

                              if (this.isAiming() && (GameWindow.ActivatedJoyPad == null || this.JoypadBind == -1)) {
                                 this.playerMoveDir.x = var3.moveX;
                                 this.playerMoveDir.y = var3.moveY;
                              }

                              if (!this.isAiming() && this.isJustMoved()) {
                                 this.playerMoveDir.x = this.getForwardDirection().x;
                                 this.playerMoveDir.y = this.getForwardDirection().y;
                              }

                              if (this.isJustMoved()) {
                                 if (this.isSprinting()) {
                                    this.CurrentSpeed = 1.5F;
                                 } else if (this.isRunning()) {
                                    this.CurrentSpeed = 1.0F;
                                 } else {
                                    this.CurrentSpeed = 0.5F;
                                 }
                              } else {
                                 this.CurrentSpeed = 0.0F;
                              }

                              boolean var14 = this.IsInMeleeAttack();
                              if (!this.CharacterActions.isEmpty()) {
                                 BaseAction var10 = (BaseAction)this.CharacterActions.get(0);
                                 if (var10.overrideAnimation) {
                                    var14 = true;
                                 }
                              }

                              if (!var14 && !this.isForceOverrideAnim()) {
                                 if (this.getPath2() == null) {
                                    if (this.CurrentSpeed > 0.0F && (!this.bClimbing || this.lastFallSpeed > 0.0F)) {
                                       if (!this.isRunning() && !this.isSprinting()) {
                                          this.StopAllActionQueueWalking();
                                       } else {
                                          this.StopAllActionQueueRunning();
                                       }
                                    }
                                 } else {
                                    this.StopAllActionQueueWalking();
                                 }
                              }

                              if (this.slowTimer > 0.0F) {
                                 this.slowTimer -= GameTime.instance.getRealworldSecondsSinceLastUpdate();
                                 this.CurrentSpeed *= 1.0F - this.slowFactor;
                                 this.slowFactor -= GameTime.instance.getMultiplier() / 100.0F;
                                 if (this.slowFactor < 0.0F) {
                                    this.slowFactor = 0.0F;
                                 }
                              } else {
                                 this.slowFactor = 0.0F;
                              }

                              this.playerMoveDir.setLength(this.CurrentSpeed);
                              if (this.playerMoveDir.x != 0.0F || this.playerMoveDir.y != 0.0F) {
                                 this.dirtyRecalcGridStackTime = 10.0F;
                              }

                              if (this.getPath2() != null && this.current != this.last) {
                                 this.dirtyRecalcGridStackTime = 10.0F;
                              }

                              this.closestZombie = 1000000.0F;
                              this.weight = 0.3F;
                              this.separate();
                              this.updateSleepingPillsTaken();
                              this.updateTorchStrength();
                              if (this.isNPC && this.GameCharacterAIBrain != null) {
                                 this.GameCharacterAIBrain.postUpdateHuman(this);
                                 this.setInitiateAttack(this.GameCharacterAIBrain.HumanControlVars.initiateAttack);
                                 this.setRunning(this.GameCharacterAIBrain.HumanControlVars.bRunning);
                                 var4 = this.GameCharacterAIBrain.HumanControlVars.strafeX;
                                 var5 = this.GameCharacterAIBrain.HumanControlVars.strafeY;
                                 this.setJustMoved(this.GameCharacterAIBrain.HumanControlVars.JustMoved);
                                 this.updateMovementRates();
                              }

                              this.m_isPlayerMoving = this.isJustMoved() || this.getPath2() != null && !this.getPathFindBehavior2().bStopping;
                              boolean var15 = this.isInTrees();
                              float var11;
                              if (var15) {
                                 var11 = "parkranger".equals(this.getDescriptor().getProfession()) ? 1.3F : 1.0F;
                                 var11 = "lumberjack".equals(this.getDescriptor().getProfession()) ? 1.15F : var11;
                                 if (this.isRunning()) {
                                    var11 *= 1.1F;
                                 }

                                 this.setVariable("WalkSpeedTrees", var11);
                              }

                              if ((var15 || this.m_walkSpeed < 0.4F || this.m_walkInjury > 0.5F) && this.isSprinting() && !this.isGhostMode()) {
                                 if ((double)this.runSpeedModifier < 1.0D) {
                                    this.setMoodleCantSprint(true);
                                 }

                                 this.setSprinting(false);
                                 this.setForceSprint(false);
                                 if (this.isInTreesNoBush()) {
                                    this.setBumpType("left");
                                    this.setVariable("BumpDone", false);
                                    this.setVariable("BumpFall", true);
                                    this.setVariable("TripObstacleType", "tree");
                                    this.actionContext.reportEvent("wasBumped");
                                 }
                              }

                              this.m_deltaX = var4;
                              this.m_deltaY = var5;
                              this.m_windspeed = ClimateManager.getInstance().getWindSpeedMovement();
                              var11 = this.getForwardDirection().getDirectionNeg();
                              this.m_windForce = ClimateManager.getInstance().getWindForceMovement(this, var11);
                              return true;
                           } else {
                              return true;
                           }
                        }
                     }
                  }
               } else {
                  return true;
               }
            }
         }
      }
   }

   private void updateMovementFromInput(IsoPlayer.MoveVars var1) {
      var1.moveX = 0.0F;
      var1.moveY = 0.0F;
      var1.strafeX = 0.0F;
      var1.strafeY = 0.0F;
      var1.NewFacing = this.dir;
      if (!TutorialManager.instance.StealControl) {
         if (!this.isBlockMovement()) {
            if (!this.isNPC) {
               if (!MPDebugAI.updateMovementFromInput(this, var1)) {
                  if (!(this.fallTime > 2.0F)) {
                     if (GameWindow.ActivatedJoyPad != null && this.JoypadBind != -1) {
                        this.updateMovementFromJoypad(var1);
                     }

                     if (this.PlayerIndex == 0 && this.JoypadBind == -1) {
                        this.updateMovementFromKeyboardMouse(var1);
                     }

                     if (this.isJustMoved()) {
                        this.getForwardDirection().normalize();
                        UIManager.speedControls.SetCurrentGameSpeed(1);
                     }

                  }
               }
            }
         }
      }
   }

   private void updateMovementFromJoypad(IsoPlayer.MoveVars var1) {
      this.playerMoveDir.x = 0.0F;
      this.playerMoveDir.y = 0.0F;
      this.getJoypadAimVector(tempVector2);
      float var2 = tempVector2.x;
      float var3 = tempVector2.y;
      Vector2 var4 = this.getJoypadMoveVector(tempVector2);
      if (var4.getLength() > 1.0F) {
         var4.setLength(1.0F);
      }

      float var5 = var4.x;
      float var6 = var4.y;
      Vector2 var10000;
      if (Math.abs(var5) > 0.0F) {
         var10000 = this.playerMoveDir;
         var10000.x += 0.04F * var5;
         var10000 = this.playerMoveDir;
         var10000.y -= 0.04F * var5;
         this.setJustMoved(true);
      }

      if (Math.abs(var6) > 0.0F) {
         var10000 = this.playerMoveDir;
         var10000.y += 0.04F * var6;
         var10000 = this.playerMoveDir;
         var10000.x += 0.04F * var6;
         this.setJustMoved(true);
      }

      if (JoypadManager.instance.isL3Pressed(this.JoypadBind)) {
         if (!this.L3Pressed) {
            this.setSneaking(!this.isSneaking());
            this.L3Pressed = true;
         }
      } else {
         this.L3Pressed = false;
      }

      this.playerMoveDir.setLength(0.05F * (float)Math.pow((double)var4.getLength(), 9.0D));
      if (var2 == 0.0F && var3 == 0.0F) {
         if ((var5 != 0.0F || var6 != 0.0F) && this.playerMoveDir.getLengthSquared() > 0.0F) {
            var4 = tempVector2.set(this.playerMoveDir);
            var4.normalize();
            var1.NewFacing = IsoDirections.fromAngle(var4);
         }
      } else {
         Vector2 var7 = tempVector2.set(var2, var3);
         var7.normalize();
         var1.NewFacing = IsoDirections.fromAngle(var7);
      }

      PathFindBehavior2 var9 = this.getPathFindBehavior2();
      if (this.playerMoveDir.x == 0.0F && this.playerMoveDir.y == 0.0F && this.getPath2() != null && var9.isStrafing() && !var9.bStopping) {
         this.playerMoveDir.set(var9.getTargetX() - this.x, var9.getTargetY() - this.y);
         this.playerMoveDir.normalize();
      }

      if (this.playerMoveDir.x != 0.0F || this.playerMoveDir.y != 0.0F) {
         if (this.isStrafing()) {
            tempo.set(this.playerMoveDir.x, -this.playerMoveDir.y);
            tempo.normalize();
            float var8 = this.legsSprite.modelSlot.model.AnimPlayer.getRenderedAngle();
            if ((double)var8 > 6.283185307179586D) {
               var8 = (float)((double)var8 - 6.283185307179586D);
            }

            if (var8 < 0.0F) {
               var8 = (float)((double)var8 + 6.283185307179586D);
            }

            tempo.rotate(var8);
            var1.strafeX = tempo.x;
            var1.strafeY = tempo.y;
            this.m_IPX = this.playerMoveDir.x;
            this.m_IPY = this.playerMoveDir.y;
         } else {
            var1.moveX = this.playerMoveDir.x;
            var1.moveY = this.playerMoveDir.y;
            tempo.set(this.playerMoveDir);
            tempo.normalize();
            this.setForwardDirection(tempo);
         }
      }

   }

   private void updateMovementFromKeyboardMouse(IsoPlayer.MoveVars var1) {
      int var2 = Core.getInstance().getKey("Left");
      int var3 = Core.getInstance().getKey("Right");
      int var4 = Core.getInstance().getKey("Forward");
      int var5 = Core.getInstance().getKey("Backward");
      boolean var6 = GameKeyboard.isKeyDown(var2);
      boolean var7 = GameKeyboard.isKeyDown(var3);
      boolean var8 = GameKeyboard.isKeyDown(var4);
      boolean var9 = GameKeyboard.isKeyDown(var5);
      if (!var6 && !var7 && !var8 && !var9 || var2 != 30 && var3 != 30 && var4 != 30 && var5 != 30 || !GameKeyboard.isKeyDown(29) && !GameKeyboard.isKeyDown(157) || !UIManager.isMouseOverInventory() || !Core.getInstance().isSelectingAll()) {
         if (!this.isIgnoreInputsForDirection()) {
            if (Core.bAltMoveMethod) {
               if (var6 && !var7) {
                  var1.moveX -= 0.04F;
                  var1.NewFacing = IsoDirections.W;
               }

               if (var7 && !var6) {
                  var1.moveX += 0.04F;
                  var1.NewFacing = IsoDirections.E;
               }

               if (var8 && !var9) {
                  var1.moveY -= 0.04F;
                  if (var1.NewFacing == IsoDirections.W) {
                     var1.NewFacing = IsoDirections.NW;
                  } else if (var1.NewFacing == IsoDirections.E) {
                     var1.NewFacing = IsoDirections.NE;
                  } else {
                     var1.NewFacing = IsoDirections.N;
                  }
               }

               if (var9 && !var8) {
                  var1.moveY += 0.04F;
                  if (var1.NewFacing == IsoDirections.W) {
                     var1.NewFacing = IsoDirections.SW;
                  } else if (var1.NewFacing == IsoDirections.E) {
                     var1.NewFacing = IsoDirections.SE;
                  } else {
                     var1.NewFacing = IsoDirections.S;
                  }
               }
            } else {
               if (var6) {
                  var1.moveX = -1.0F;
               } else if (var7) {
                  var1.moveX = 1.0F;
               }

               if (var8) {
                  var1.moveY = 1.0F;
               } else if (var9) {
                  var1.moveY = -1.0F;
               }

               if (var1.moveX != 0.0F || var1.moveY != 0.0F) {
                  tempo.set(var1.moveX, var1.moveY);
                  tempo.normalize();
                  var1.NewFacing = IsoDirections.fromAngle(tempo);
               }
            }
         }

         PathFindBehavior2 var10 = this.getPathFindBehavior2();
         if (var1.moveX == 0.0F && var1.moveY == 0.0F && this.getPath2() != null && (var10.isStrafing() || this.isAiming()) && !var10.bStopping) {
            Vector2 var11 = tempo.set(var10.getTargetX() - this.x, var10.getTargetY() - this.y);
            Vector2 var12 = tempo2.set(-1.0F, 0.0F);
            float var13 = 1.0F;
            float var14 = var11.dot(var12);
            float var15 = var14 / var13;
            var12 = tempo2.set(0.0F, -1.0F);
            var14 = var11.dot(var12);
            float var16 = var14 / var13;
            tempo.set(var16, var15);
            tempo.normalize();
            tempo.rotate(0.7853982F);
            var1.moveX = tempo.x;
            var1.moveY = tempo.y;
         }

         if (var1.moveX != 0.0F || var1.moveY != 0.0F) {
            if (this.stateMachine.getCurrent() == PathFindState.instance()) {
               this.setDefaultState();
            }

            this.setJustMoved(true);
            this.setMoveDelta(1.0F);
            if (this.isStrafing()) {
               tempo.set(var1.moveX, var1.moveY);
               tempo.normalize();
               float var17 = this.legsSprite.modelSlot.model.AnimPlayer.getRenderedAngle();
               var17 = (float)((double)var17 + 0.7853981633974483D);
               if ((double)var17 > 6.283185307179586D) {
                  var17 = (float)((double)var17 - 6.283185307179586D);
               }

               if (var17 < 0.0F) {
                  var17 = (float)((double)var17 + 6.283185307179586D);
               }

               tempo.rotate(var17);
               var1.strafeX = tempo.x;
               var1.strafeY = tempo.y;
               this.m_IPX = var1.moveX;
               this.m_IPY = var1.moveY;
            } else {
               tempo.set(var1.moveX, -var1.moveY);
               tempo.normalize();
               tempo.rotate(-0.7853982F);
               this.setForwardDirection(tempo);
            }
         }

      }
   }

   private void updateAimingStance() {
      if (this.isVariable("LeftHandMask", "RaiseHand")) {
         this.clearVariable("LeftHandMask");
      }

      if (this.isAiming() && !this.isCurrentState(SwipeStatePlayer.instance())) {
         HandWeapon var1 = (HandWeapon)Type.tryCastTo(this.getPrimaryHandItem(), HandWeapon.class);
         var1 = var1 == null ? this.bareHands : var1;
         SwipeStatePlayer.instance().calcValidTargets(this, var1, true, s_targetsProne, s_targetsStanding);
         HitInfo var2 = s_targetsStanding.isEmpty() ? null : (HitInfo)s_targetsStanding.get(0);
         HitInfo var3 = s_targetsProne.isEmpty() ? null : (HitInfo)s_targetsProne.get(0);
         if (SwipeStatePlayer.instance().isProneTargetBetter(this, var2, var3)) {
            var2 = null;
         }

         boolean var4 = this.isAttackAnim() || this.getVariableBoolean("ShoveAnim") || this.getVariableBoolean("StompAnim");
         if (!var4) {
            this.setAimAtFloor(false);
         }

         if (var2 != null) {
            if (!var4) {
               this.setAimAtFloor(false);
            }
         } else if (var3 != null && !var4) {
            this.setAimAtFloor(true);
         }

         if (var2 != null) {
            boolean var5 = !this.isAttackAnim() && var1.getSwingAnim() != null && var1.CloseKillMove != null && var2.distSq < var1.getMinRange() * var1.getMinRange();
            if (var5 && (this.getSecondaryHandItem() == null || this.getSecondaryHandItem().getItemReplacementSecondHand() == null)) {
               this.setVariable("LeftHandMask", "RaiseHand");
            }
         }

         SwipeStatePlayer.instance().hitInfoPool.release((List)s_targetsStanding);
         SwipeStatePlayer.instance().hitInfoPool.release((List)s_targetsProne);
         s_targetsStanding.clear();
         s_targetsProne.clear();
      }
   }

   protected void calculateStats() {
      if (!this.bRemote) {
         super.calculateStats();
      }
   }

   protected void updateStats_Sleeping() {
      float var1 = 2.0F;
      if (allPlayersAsleep()) {
         var1 *= GameTime.instance.getDeltaMinutesPerDay();
      }

      Stats var10000 = this.stats;
      var10000.endurance = (float)((double)var10000.endurance + ZomboidGlobals.ImobileEnduranceReduce * SandboxOptions.instance.getEnduranceRegenMultiplier() * (double)this.getRecoveryMod() * (double)GameTime.instance.getMultiplier() * (double)var1);
      if (this.stats.endurance > 1.0F) {
         this.stats.endurance = 1.0F;
      }

      float var2;
      float var3;
      if (this.stats.fatigue > 0.0F) {
         var2 = 1.0F;
         if (this.Traits.Insomniac.isSet()) {
            var2 *= 0.5F;
         }

         if (this.Traits.NightOwl.isSet()) {
            var2 *= 1.4F;
         }

         var3 = 1.0F;
         if ("goodBed".equals(this.getBedType())) {
            var3 = 1.1F;
         }

         if ("badBed".equals(this.getBedType())) {
            var3 = 0.9F;
         }

         if ("floor".equals(this.getBedType())) {
            var3 = 0.6F;
         }

         float var4 = 1.0F / GameTime.instance.getMinutesPerDay() / 60.0F * GameTime.instance.getMultiplier() / 2.0F;
         this.timeOfSleep += var4;
         if (this.timeOfSleep > this.delayToActuallySleep) {
            float var5 = 1.0F;
            if (this.Traits.NeedsLessSleep.isSet()) {
               var5 *= 0.75F;
            } else if (this.Traits.NeedsMoreSleep.isSet()) {
               var5 *= 1.18F;
            }

            float var6 = 1.0F;
            if (this.stats.fatigue <= 0.3F) {
               var6 = 7.0F * var5;
               var10000 = this.stats;
               var10000.fatigue -= var4 / var6 * 0.3F * var2 * var3;
            } else {
               var6 = 5.0F * var5;
               var10000 = this.stats;
               var10000.fatigue -= var4 / var6 * 0.7F * var2 * var3;
            }
         }

         if (this.stats.fatigue < 0.0F) {
            this.stats.fatigue = 0.0F;
         }
      }

      if (this.Moodles.getMoodleLevel(MoodleType.FoodEaten) == 0) {
         var2 = this.getAppetiteMultiplier();
         var10000 = this.stats;
         var10000.hunger = (float)((double)var10000.hunger + ZomboidGlobals.HungerIncreaseWhileAsleep * SandboxOptions.instance.getStatsDecreaseMultiplier() * (double)var2 * (double)GameTime.instance.getMultiplier() * (double)GameTime.instance.getDeltaMinutesPerDay() * this.getHungerMultiplier());
      } else {
         var10000 = this.stats;
         var10000.hunger += (float)(ZomboidGlobals.HungerIncreaseWhenWellFed * SandboxOptions.instance.getStatsDecreaseMultiplier() * ZomboidGlobals.HungerIncreaseWhileAsleep * SandboxOptions.instance.getStatsDecreaseMultiplier() * (double)GameTime.instance.getMultiplier() * this.getHungerMultiplier() * (double)GameTime.instance.getDeltaMinutesPerDay());
      }

      if (this.ForceWakeUpTime == 0.0F) {
         this.ForceWakeUpTime = 9.0F;
      }

      var2 = GameTime.getInstance().getTimeOfDay();
      var3 = GameTime.getInstance().getLastTimeOfDay();
      if (var3 > var2) {
         if (var3 < this.ForceWakeUpTime) {
            var2 += 24.0F;
         } else {
            var3 -= 24.0F;
         }
      }

      boolean var7 = var2 >= this.ForceWakeUpTime && var3 < this.ForceWakeUpTime;
      if (this.getAsleepTime() > 16.0F) {
         var7 = true;
      }

      if (GameClient.bClient || numPlayers > 1) {
         var7 = var7 || this.pressedAim() || this.pressedMovement(false);
      }

      if (this.ForceWakeUp) {
         var7 = true;
      }

      if (this.Asleep && var7) {
         this.ForceWakeUp = false;
         SoundManager.instance.setMusicWakeState(this, "WakeNormal");
         SleepingEvent.instance.wakeUp(this);
         this.ForceWakeUpTime = -1.0F;
         if (GameClient.bClient) {
            GameClient.instance.sendPlayer(this);
         }

         this.dirtyRecalcGridStackTime = 20.0F;
      }

   }

   private void updateEndurance(float var1) {
      Stats var10000;
      float var2;
      if (this.isSitOnGround()) {
         var2 = (float)ZomboidGlobals.SittingEnduranceMultiplier;
         var2 *= 1.0F - this.stats.fatigue;
         var2 *= GameTime.instance.getMultiplier();
         var10000 = this.stats;
         var10000.endurance = (float)((double)var10000.endurance + ZomboidGlobals.ImobileEnduranceReduce * SandboxOptions.instance.getEnduranceRegenMultiplier() * (double)this.getRecoveryMod() * (double)var2);
         this.stats.endurance = PZMath.clamp(this.stats.endurance, 0.0F, 1.0F);
      } else {
         var2 = 1.0F;
         if (this.isSneaking()) {
            var2 = 1.5F;
         }

         float var5;
         float var8;
         if (!(this.CurrentSpeed > 0.0F) || !this.isRunning() && !this.isSprinting()) {
            if (this.CurrentSpeed > 0.0F && this.Moodles.getMoodleLevel(MoodleType.HeavyLoad) > 2) {
               var8 = 0.7F;
               if (this.Traits.Asthmatic.isSet()) {
                  var8 = 1.4F;
               }

               float var4 = 1.4F;
               if (this.Traits.Overweight.isSet()) {
                  var4 = 2.9F;
               }

               if (this.Traits.Athletic.isSet()) {
                  var4 = 0.8F;
               }

               var4 *= 3.0F;
               var4 *= this.getPacingMod();
               var4 *= this.getHyperthermiaMod();
               var5 = 2.8F;
               switch(this.Moodles.getMoodleLevel(MoodleType.HeavyLoad)) {
               case 2:
                  var5 = 1.5F;
                  break;
               case 3:
                  var5 = 1.9F;
                  break;
               case 4:
                  var5 = 2.3F;
               }

               var10000 = this.stats;
               var10000.endurance = (float)((double)var10000.endurance - ZomboidGlobals.RunningEnduranceReduce * (double)var4 * 0.5D * (double)var8 * (double)var2 * (double)GameTime.instance.getMultiplier() * (double)var5 / 2.0D);
            }
         } else {
            double var3 = ZomboidGlobals.RunningEnduranceReduce;
            if (this.isSprinting()) {
               var3 = ZomboidGlobals.SprintingEnduranceReduce;
            }

            var5 = 1.4F;
            if (this.Traits.Overweight.isSet()) {
               var5 = 2.9F;
            }

            if (this.Traits.Athletic.isSet()) {
               var5 = 0.8F;
            }

            var5 *= 2.3F;
            var5 *= this.getPacingMod();
            var5 *= this.getHyperthermiaMod();
            float var6 = 0.7F;
            if (this.Traits.Asthmatic.isSet()) {
               var6 = 1.4F;
            }

            if (this.Moodles.getMoodleLevel(MoodleType.HeavyLoad) == 0) {
               var10000 = this.stats;
               var10000.endurance = (float)((double)var10000.endurance - var3 * (double)var5 * 0.5D * (double)var6 * (double)GameTime.instance.getMultiplier() * (double)var2);
            } else {
               float var7 = 2.8F;
               switch(this.Moodles.getMoodleLevel(MoodleType.HeavyLoad)) {
               case 1:
                  var7 = 1.5F;
                  break;
               case 2:
                  var7 = 1.9F;
                  break;
               case 3:
                  var7 = 2.3F;
               }

               var10000 = this.stats;
               var10000.endurance = (float)((double)var10000.endurance - var3 * (double)var5 * 0.5D * (double)var6 * (double)GameTime.instance.getMultiplier() * (double)var7 * (double)var2);
            }
         }

         switch(this.Moodles.getMoodleLevel(MoodleType.Endurance)) {
         case 1:
            var1 *= 0.95F;
            break;
         case 2:
            var1 *= 0.9F;
            break;
         case 3:
            var1 *= 0.8F;
            break;
         case 4:
            var1 *= 0.6F;
         }

         if (this.stats.enduranceRecharging) {
            var1 *= 0.85F;
         }

         if (!this.isPlayerMoving()) {
            var8 = 1.0F;
            var8 *= 1.0F - this.stats.fatigue;
            var8 *= GameTime.instance.getMultiplier();
            if (this.Moodles.getMoodleLevel(MoodleType.HeavyLoad) <= 1) {
               var10000 = this.stats;
               var10000.endurance = (float)((double)var10000.endurance + ZomboidGlobals.ImobileEnduranceReduce * SandboxOptions.instance.getEnduranceRegenMultiplier() * (double)this.getRecoveryMod() * (double)var8);
            }
         }

         if (!this.isSprinting() && !this.isRunning() && this.CurrentSpeed > 0.0F) {
            var8 = 1.0F;
            var8 *= 1.0F - this.stats.fatigue;
            var8 *= GameTime.instance.getMultiplier();
            if (this.getMoodles().getMoodleLevel(MoodleType.Endurance) < 2) {
               if (this.Moodles.getMoodleLevel(MoodleType.HeavyLoad) <= 1) {
                  var10000 = this.stats;
                  var10000.endurance = (float)((double)var10000.endurance + ZomboidGlobals.ImobileEnduranceReduce / 4.0D * SandboxOptions.instance.getEnduranceRegenMultiplier() * (double)this.getRecoveryMod() * (double)var8);
               }
            } else {
               var10000 = this.stats;
               var10000.endurance = (float)((double)var10000.endurance - ZomboidGlobals.RunningEnduranceReduce / 7.0D * (double)var2);
            }
         }

      }
   }

   private boolean checkActionsBlockingMovement() {
      if (this.CharacterActions.isEmpty()) {
         return false;
      } else {
         BaseAction var1 = (BaseAction)this.CharacterActions.get(0);
         return var1.blockMovementEtc;
      }
   }

   private void updateInteractKeyPanic() {
      if (this.PlayerIndex == 0) {
         if (GameKeyboard.isKeyPressed(Core.getInstance().getKey("Interact"))) {
            this.ContextPanic += 0.6F;
         }

      }
   }

   private void updateSneakKey() {
      if (this.PlayerIndex != 0) {
         this.bSneakDebounce = false;
      } else {
         if (!this.isBlockMovement() && GameKeyboard.isKeyDown(Core.getInstance().getKey("Crouch"))) {
            if (!this.bSneakDebounce) {
               this.setSneaking(!this.isSneaking());
               this.bSneakDebounce = true;
            }
         } else {
            this.bSneakDebounce = false;
         }

      }
   }

   private void updateChangeCharacterKey() {
      if (Core.bDebug) {
         if (this.PlayerIndex == 0 && GameKeyboard.isKeyDown(22)) {
            if (!this.bChangeCharacterDebounce) {
               this.FollowCamStack.clear();
               this.bChangeCharacterDebounce = true;

               for(int var2 = 0; var2 < this.getCell().getObjectList().size(); ++var2) {
                  IsoMovingObject var1 = (IsoMovingObject)this.getCell().getObjectList().get(var2);
                  if (var1 instanceof IsoSurvivor) {
                     this.FollowCamStack.add((IsoSurvivor)var1);
                  }
               }

               if (!this.FollowCamStack.isEmpty()) {
                  if (this.followID >= this.FollowCamStack.size()) {
                     this.followID = 0;
                  }

                  IsoCamera.SetCharacterToFollow((IsoGameCharacter)this.FollowCamStack.get(this.followID));
                  ++this.followID;
               }

            }
         } else {
            this.bChangeCharacterDebounce = false;
         }
      }
   }

   private void updateEnableModelsKey() {
      if (Core.bDebug) {
         if (this.PlayerIndex == 0 && GameKeyboard.isKeyPressed(Core.getInstance().getKey("ToggleModelsEnabled"))) {
            ModelManager.instance.bDebugEnableModels = !ModelManager.instance.bDebugEnableModels;
         }

      }
   }

   private void updateDeathDragDown() {
      if (!this.isDead()) {
         if (this.isDeathDragDown()) {
            if (this.isGodMod()) {
               this.setDeathDragDown(false);
            } else if (!"EndDeath".equals(this.getHitReaction())) {
               for(int var1 = -1; var1 <= 1; ++var1) {
                  for(int var2 = -1; var2 <= 1; ++var2) {
                     IsoGridSquare var3 = this.getCell().getGridSquare((int)this.x + var2, (int)this.y + var1, (int)this.z);
                     if (var3 != null) {
                        for(int var4 = 0; var4 < var3.getMovingObjects().size(); ++var4) {
                           IsoMovingObject var5 = (IsoMovingObject)var3.getMovingObjects().get(var4);
                           IsoZombie var6 = (IsoZombie)Type.tryCastTo(var5, IsoZombie.class);
                           if (var6 != null && var6.isAlive() && !var6.isOnFloor()) {
                              this.setAttackedBy(var6);
                              this.setHitReaction("EndDeath");
                              this.setBlockMovement(true);
                              return;
                           }
                        }
                     }
                  }
               }

               this.setDeathDragDown(false);
            }
         }
      }
   }

   private void updateGodModeKey() {
      if (Core.bDebug) {
         if (GameKeyboard.isKeyPressed(Core.getInstance().getKey("ToggleGodModeInvisible"))) {
            IsoPlayer var1 = null;

            for(int var2 = 0; var2 < numPlayers; ++var2) {
               if (players[var2] != null && !players[var2].isDead()) {
                  var1 = players[var2];
                  break;
               }
            }

            if (this == var1) {
               boolean var4 = !var1.isGodMod();
               DebugLog.General.println("Toggle GodMode: %s", var4 ? "ON" : "OFF");
               var1.setInvisible(var4);
               var1.setGhostMode(var4);
               var1.setGodMod(var4);

               for(int var3 = 0; var3 < numPlayers; ++var3) {
                  if (players[var3] != null && players[var3] != var1) {
                     players[var3].setInvisible(var4);
                     players[var3].setGhostMode(var4);
                     players[var3].setGodMod(var4);
                  }
               }

               if (GameClient.bClient) {
                  GameClient.sendPlayerExtraInfo(var1);
               }
            }

         }
      }
   }

   private void checkReloading() {
      HandWeapon var1 = (HandWeapon)Type.tryCastTo(this.getPrimaryHandItem(), HandWeapon.class);
      if (var1 != null && var1.isReloadable(this)) {
         boolean var2 = false;
         boolean var3 = false;
         boolean var4;
         if (this.JoypadBind != -1 && this.bJoypadMovementActive) {
            var4 = JoypadManager.instance.isRBPressed(this.JoypadBind);
            if (var4) {
               var2 = !this.bReloadButtonDown;
            }

            this.bReloadButtonDown = var4;
            var4 = JoypadManager.instance.isLBPressed(this.JoypadBind);
            if (var4) {
               var3 = !this.bRackButtonDown;
            }

            this.bRackButtonDown = var4;
         }

         if (this.PlayerIndex == 0) {
            var4 = GameKeyboard.isKeyDown(Core.getInstance().getKey("ReloadWeapon"));
            if (var4) {
               var2 = !this.bReloadKeyDown;
            }

            this.bReloadKeyDown = var4;
            var4 = GameKeyboard.isKeyDown(Core.getInstance().getKey("Rack Firearm"));
            if (var4) {
               var3 = !this.bRackKeyDown;
            }

            this.bRackKeyDown = var4;
         }

         if (var2) {
            this.setVariable("WeaponReloadType", var1.getWeaponReloadType());
            LuaEventManager.triggerEvent("OnPressReloadButton", this, var1);
         } else if (var3) {
            this.setVariable("WeaponReloadType", var1.getWeaponReloadType());
            LuaEventManager.triggerEvent("OnPressRackButton", this, var1);
         }

      }
   }

   public void postupdate() {
      IsoPlayer.s_performance.postUpdate.invokeAndMeasure(this, IsoPlayer::postupdateInternal);
   }

   private void postupdateInternal() {
      boolean var1 = this.hasHitReaction();
      super.postupdate();
      if (var1 && this.hasHitReaction() && !this.isCurrentState(PlayerHitReactionState.instance()) && !this.isCurrentState(PlayerHitReactionPVPState.instance())) {
         this.setHitReaction("");
      }

      this.highlightRangedTargets();
      if (this.isNPC) {
         GameTime var2 = GameTime.getInstance();
         float var3 = 1.0F / var2.getMinutesPerDay() / 60.0F * var2.getMultiplier() / 2.0F;
         if (Core.bLastStand) {
            var3 = 1.0F / var2.getMinutesPerDay() / 60.0F * var2.getUnmoddedMultiplier() / 2.0F;
         }

         this.setHoursSurvived(this.getHoursSurvived() + (double)var3);
      }

      this.getBodyDamage().setBodyPartsLastState();
   }

   private void highlightRangedTargets() {
      if (this.isLocalPlayer() && !this.isNPC) {
         if (this.isAiming()) {
            if (Core.getInstance().getOptionAimOutline() != 1) {
               IsoPlayer.s_performance.highlightRangedTargets.invokeAndMeasure(this, IsoPlayer::highlightRangedTargetsInternal);
            }
         }
      }
   }

   private void highlightRangedTargetsInternal() {
      HandWeapon var1 = (HandWeapon)Type.tryCastTo(this.getPrimaryHandItem(), HandWeapon.class);
      if (var1 == null || var1.getSwingAnim() == null || var1.getCondition() <= 0) {
         var1 = this.bareHands;
      }

      if (Core.getInstance().getOptionAimOutline() != 2 || var1.isRanged()) {
         AttackVars var2 = new AttackVars();
         ArrayList var3 = new ArrayList();
         boolean var4 = this.bDoShove;
         HandWeapon var5 = this.getUseHandWeapon();
         this.setDoShove(false);
         this.setUseHandWeapon(var1);
         SwipeStatePlayer.instance().CalcAttackVars(this, var2);
         SwipeStatePlayer.instance().CalcHitList(this, false, var2, var3);

         for(int var6 = 0; var6 < var3.size(); ++var6) {
            HitInfo var7 = (HitInfo)var3.get(var6);
            IsoMovingObject var8 = var7.getObject();
            if (var8 instanceof IsoZombie || var8 instanceof IsoPlayer) {
               float var9 = 1.0F - (float)var7.chance / 100.0F;
               float var10 = (float)var7.chance / 100.0F;
               float var11 = 0.4F;
               if ((double)var10 < 0.7D) {
                  var11 = 0.36F;
               }

               var8.bOutline[this.PlayerIndex] = true;
               if (var8.outlineColor[this.PlayerIndex] == null) {
                  var8.outlineColor[this.PlayerIndex] = new ColorInfo();
               }

               var8.outlineColor[this.PlayerIndex].set(var9 * 0.75F, var10 * var11, 0.0F, 1.0F);
            }

            if (var7.window.getObject() != null) {
               var7.window.getObject().setHighlightColor(0.8F, 0.1F, 0.1F, 0.5F);
               var7.window.getObject().setHighlighted(true);
            }
         }

         this.setDoShove(var4);
         this.setUseHandWeapon(var5);
      }
   }

   public boolean isSolidForSeparate() {
      return this.isGhostMode() ? false : super.isSolidForSeparate();
   }

   public boolean isPushableForSeparate() {
      if (this.isCurrentState(PlayerHitReactionState.instance())) {
         return false;
      } else {
         return this.isCurrentState(SwipeStatePlayer.instance()) ? false : super.isPushableForSeparate();
      }
   }

   public boolean isPushedByForSeparate(IsoMovingObject var1) {
      if (!this.isPlayerMoving() && var1.isZombie() && ((IsoZombie)var1).isAttacking()) {
         return false;
      } else {
         return !GameClient.bClient || this.isLocalPlayer() && this.isJustMoved() ? super.isPushedByForSeparate(var1) : false;
      }
   }

   private void updateExt() {
      if (!this.isSneaking()) {
         this.extUpdateCount += GameTime.getInstance().getMultiplier() / 0.8F;
         if (!this.getAdvancedAnimator().containsAnyIdleNodes() && !this.isSitOnGround()) {
            this.extUpdateCount = 0.0F;
         }

         if (!(this.extUpdateCount <= 5000.0F)) {
            this.extUpdateCount = 0.0F;
            if (this.stats.NumVisibleZombies == 0 && this.stats.NumChasingZombies == 0) {
               if (Rand.NextBool(3)) {
                  if (this.getAdvancedAnimator().containsAnyIdleNodes() || this.isSitOnGround()) {
                     this.onIdlePerformFidgets();
                     this.reportEvent("EventDoExt");
                  }
               }
            }
         }
      }
   }

   private void onIdlePerformFidgets() {
      Moodles var1 = this.getMoodles();
      BodyDamage var2 = this.getBodyDamage();
      if (var1.getMoodleLevel(MoodleType.Hypothermia) > 0 && Rand.NextBool(7)) {
         this.setVariable("Ext", "Shiver");
      } else if (var1.getMoodleLevel(MoodleType.Hyperthermia) > 0 && Rand.NextBool(7)) {
         this.setVariable("Ext", "WipeBrow");
      } else {
         int var10002;
         if (var1.getMoodleLevel(MoodleType.Sick) > 0 && Rand.NextBool(7)) {
            if (Rand.NextBool(4)) {
               this.setVariable("Ext", "Cough");
            } else {
               var10002 = Rand.Next(2);
               this.setVariable("Ext", "PainStomach" + (var10002 + 1));
            }

         } else if (var1.getMoodleLevel(MoodleType.Endurance) > 2 && Rand.NextBool(10)) {
            if (Rand.NextBool(5) && !this.isSitOnGround()) {
               this.setVariable("Ext", "BentDouble");
            } else {
               this.setVariable("Ext", "WipeBrow");
            }

         } else if (var1.getMoodleLevel(MoodleType.Tired) > 2 && Rand.NextBool(10)) {
            if (Rand.NextBool(7)) {
               this.setVariable("Ext", "TiredStretch");
            } else if (Rand.NextBool(7)) {
               this.setVariable("Ext", "Sway");
            } else {
               this.setVariable("Ext", "Yawn");
            }

         } else if (var2.doBodyPartsHaveInjuries(BodyPartType.Head, BodyPartType.Neck) && Rand.NextBool(7)) {
            if (var2.areBodyPartsBleeding(BodyPartType.Head, BodyPartType.Neck) && Rand.NextBool(2)) {
               this.setVariable("Ext", "WipeHead");
            } else {
               var10002 = Rand.Next(2);
               this.setVariable("Ext", "PainHead" + (var10002 + 1));
            }

         } else if (var2.doBodyPartsHaveInjuries(BodyPartType.UpperArm_L, BodyPartType.ForeArm_L) && Rand.NextBool(7)) {
            if (var2.areBodyPartsBleeding(BodyPartType.UpperArm_L, BodyPartType.ForeArm_L) && Rand.NextBool(2)) {
               this.setVariable("Ext", "WipeArmL");
            } else {
               this.setVariable("Ext", "PainArmL");
            }

         } else if (var2.doBodyPartsHaveInjuries(BodyPartType.UpperArm_R, BodyPartType.ForeArm_R) && Rand.NextBool(7)) {
            if (var2.areBodyPartsBleeding(BodyPartType.UpperArm_R, BodyPartType.ForeArm_R) && Rand.NextBool(2)) {
               this.setVariable("Ext", "WipeArmR");
            } else {
               this.setVariable("Ext", "PainArmR");
            }

         } else if (var2.doesBodyPartHaveInjury(BodyPartType.Hand_L) && Rand.NextBool(7)) {
            this.setVariable("Ext", "PainHandL");
         } else if (var2.doesBodyPartHaveInjury(BodyPartType.Hand_R) && Rand.NextBool(7)) {
            this.setVariable("Ext", "PainHandR");
         } else if (!this.isSitOnGround() && var2.doBodyPartsHaveInjuries(BodyPartType.UpperLeg_L, BodyPartType.LowerLeg_L) && Rand.NextBool(7)) {
            if (var2.areBodyPartsBleeding(BodyPartType.UpperLeg_L, BodyPartType.LowerLeg_L) && Rand.NextBool(2)) {
               this.setVariable("Ext", "WipeLegL");
            } else {
               this.setVariable("Ext", "PainLegL");
            }

         } else if (!this.isSitOnGround() && var2.doBodyPartsHaveInjuries(BodyPartType.UpperLeg_R, BodyPartType.LowerLeg_R) && Rand.NextBool(7)) {
            if (var2.areBodyPartsBleeding(BodyPartType.UpperLeg_R, BodyPartType.LowerLeg_R) && Rand.NextBool(2)) {
               this.setVariable("Ext", "WipeLegR");
            } else {
               this.setVariable("Ext", "PainLegR");
            }

         } else if (var2.doBodyPartsHaveInjuries(BodyPartType.Torso_Upper, BodyPartType.Torso_Lower) && Rand.NextBool(7)) {
            if (var2.areBodyPartsBleeding(BodyPartType.Torso_Upper, BodyPartType.Torso_Lower) && Rand.NextBool(2)) {
               var10002 = Rand.Next(2);
               this.setVariable("Ext", "WipeTorso" + (var10002 + 1));
            } else {
               this.setVariable("Ext", "PainTorso");
            }

         } else if (WeaponType.getWeaponType((IsoGameCharacter)this) != WeaponType.barehand) {
            var10002 = Rand.Next(5);
            this.setVariable("Ext", (var10002 + 1).makeConcatWithConstants<invokedynamic>(var10002 + 1));
         } else if (Rand.NextBool(10)) {
            this.setVariable("Ext", "ChewNails");
         } else if (Rand.NextBool(10)) {
            this.setVariable("Ext", "ShiftWeight");
         } else if (Rand.NextBool(10)) {
            this.setVariable("Ext", "PullAtColar");
         } else if (Rand.NextBool(10)) {
            this.setVariable("Ext", "BridgeNose");
         } else {
            var10002 = Rand.Next(5);
            this.setVariable("Ext", (var10002 + 1).makeConcatWithConstants<invokedynamic>(var10002 + 1));
         }
      }
   }

   private boolean updateUseKey() {
      if (GameServer.bServer) {
         return false;
      } else if (!this.isLocalPlayer()) {
         return false;
      } else if (this.PlayerIndex != 0) {
         return false;
      } else {
         this.timePressedContext += GameTime.instance.getRealworldSecondsSinceLastUpdate();
         boolean var1 = GameKeyboard.isKeyDown(Core.getInstance().getKey("Interact"));
         if (var1 && this.timePressedContext < 0.5F) {
            this.bPressContext = true;
         } else {
            if (this.bPressContext && (Core.CurrentTextEntryBox != null && Core.CurrentTextEntryBox.DoingTextEntry || !GameKeyboard.doLuaKeyPressed)) {
               this.bPressContext = false;
            }

            if (this.bPressContext && this.doContext(this.dir)) {
               this.timePressedContext = 0.0F;
               this.bPressContext = false;
               return true;
            }

            if (!var1) {
               this.bPressContext = false;
               this.timePressedContext = 0.0F;
            }
         }

         return false;
      }
   }

   private void updateHitByVehicle() {
      if (!GameServer.bServer) {
         if (this.isLocalPlayer()) {
            if (this.vehicle4testCollision != null && this.ulBeatenVehicle.Check() && SandboxOptions.instance.DamageToPlayerFromHitByACar.getValue() > 1) {
               BaseVehicle var1 = this.vehicle4testCollision;
               this.vehicle4testCollision = null;
               if (var1.isEngineRunning() && this.getVehicle() != var1) {
                  float var2 = var1.jniLinearVelocity.x;
                  float var3 = var1.jniLinearVelocity.z;
                  if (GameClient.bClient && this.isLocalPlayer()) {
                     var2 = var1.netLinearVelocity.x;
                     var3 = var1.netLinearVelocity.z;
                  }

                  float var4 = (float)Math.sqrt((double)(var2 * var2 + var3 * var3));
                  Vector2 var5 = (Vector2)((BaseVehicle.Vector2ObjectPool)BaseVehicle.TL_vector2_pool.get()).alloc();
                  Vector2 var6 = var1.testCollisionWithCharacter(this, 0.20000002F, var5);
                  if (var6 != null && var6.x != -1.0F) {
                     var6.x = (var6.x - var1.x) * var4 * 1.0F + this.x;
                     var6.y = (var6.y - var1.y) * var4 * 1.0F + this.x;
                     if (this.isOnFloor()) {
                        int var7 = var1.testCollisionWithProneCharacter(this, false);
                        if (var7 > 0) {
                           this.doBeatenVehicle(Math.max(var4 * 6.0F, 5.0F));
                        }

                        this.doBeatenVehicle(0.0F);
                     } else if (this.getCurrentState() != PlayerFallDownState.instance() && var4 > 0.1F) {
                        this.doBeatenVehicle(Math.max(var4 * 2.0F, 5.0F));
                     }
                  }

                  ((BaseVehicle.Vector2ObjectPool)BaseVehicle.TL_vector2_pool.get()).release(var5);
               }
            }
         }
      }
   }

   private void updateSoundListener() {
      if (!GameServer.bServer) {
         if (this.isLocalPlayer()) {
            if (this.soundListener == null) {
               this.soundListener = (BaseSoundListener)(Core.SoundDisabled ? new DummySoundListener(this.PlayerIndex) : new SoundListener(this.PlayerIndex));
            }

            this.soundListener.setPos(this.x, this.y, this.z);
            this.checkNearbyRooms -= GameTime.getInstance().getMultiplier() / 1.6F;
            if (this.checkNearbyRooms <= 0.0F) {
               this.checkNearbyRooms = 30.0F;
               this.numNearbyBuildingsRooms = (float)IsoWorld.instance.MetaGrid.countNearbyBuildingsRooms(this);
            }

            if (this.testemitter == null) {
               this.testemitter = (BaseSoundEmitter)(Core.SoundDisabled ? new DummySoundEmitter() : new FMODSoundEmitter());
               this.testemitter.setPos(this.x, this.y, this.z);
            }

            this.soundListener.tick();
            this.testemitter.tick();
         }
      }
   }

   public void updateMovementRates() {
      this.calculateWalkSpeed();
      this.m_idleSpeed = this.calculateIdleSpeed();
      this.updateFootInjuries();
   }

   public void pressedAttack(boolean var1) {
      boolean var2 = GameClient.bClient && !this.isLocalPlayer();
      boolean var3 = this.isSprinting();
      this.setSprinting(false);
      this.setForceSprint(false);
      if (!this.attackStarted && !this.isCurrentState(PlayerHitReactionState.instance())) {
         if (!GameClient.bClient || !this.isCurrentState(PlayerHitReactionPVPState.instance()) || ServerOptions.instance.PVPMeleeWhileHitReaction.getValue()) {
            if (this.primaryHandModel != null && !StringUtils.isNullOrEmpty(this.primaryHandModel.maskVariableValue) && this.secondaryHandModel != null && !StringUtils.isNullOrEmpty(this.secondaryHandModel.maskVariableValue)) {
               this.setDoShove(false);
               this.setForceShove(false);
               this.setInitiateAttack(false);
               this.attackStarted = false;
               this.setAttackType((String)null);
            } else if (this.getPrimaryHandItem() != null && this.getPrimaryHandItem().getItemReplacementPrimaryHand() != null && this.getSecondaryHandItem() != null && this.getSecondaryHandItem().getItemReplacementSecondHand() != null) {
               this.setDoShove(false);
               this.setForceShove(false);
               this.setInitiateAttack(false);
               this.attackStarted = false;
               this.setAttackType((String)null);
            } else {
               if (!this.attackStarted) {
                  this.setVariable("StartedAttackWhileSprinting", var3);
               }

               this.setInitiateAttack(true);
               this.attackStarted = true;
               if (!var2) {
                  this.setCriticalHit(false);
               }

               this.setAttackFromBehind(false);
               WeaponType var4 = WeaponType.getWeaponType((IsoGameCharacter)this);
               if (!GameClient.bClient || this.isLocalPlayer()) {
                  this.setAttackType((String)PZArrayUtil.pickRandom(var4.possibleAttack));
               }

               if (!GameClient.bClient || this.isLocalPlayer()) {
                  this.combatSpeed = this.calculateCombatSpeed();
               }

               if (var1) {
                  SwipeStatePlayer.instance().CalcAttackVars(this, this.attackVars);
               }

               String var5 = this.getVariableString("Weapon");
               if (var5 != null && var5.equals("throwing") && !this.attackVars.bDoShove) {
                  this.setAttackAnimThrowTimer(2000L);
                  this.setIsAiming(true);
               }

               if (var2) {
                  this.attackVars.bDoShove = this.isDoShove();
                  this.attackVars.bAimAtFloor = this.isAimAtFloor();
               }

               if (this.attackVars.bDoShove && !this.isAuthorizeShoveStomp()) {
                  this.setDoShove(false);
                  this.setForceShove(false);
                  this.setInitiateAttack(false);
                  this.attackStarted = false;
                  this.setAttackType((String)null);
               } else {
                  this.useHandWeapon = this.attackVars.getWeapon(this);
                  this.setAimAtFloor(this.attackVars.bAimAtFloor);
                  this.setDoShove(this.attackVars.bDoShove);
                  this.targetOnGround = (IsoGameCharacter)this.attackVars.targetOnGround.getMovingObject();
                  if (this.attackVars.getWeapon(this) != null && !StringUtils.isNullOrEmpty(this.attackVars.getWeapon(this).getFireMode())) {
                     this.setVariable("FireMode", this.attackVars.getWeapon(this).getFireMode());
                  } else {
                     this.clearVariable("FireMode");
                  }

                  int var6;
                  if (this.useHandWeapon != null && var4.isRanged && !this.bDoShove) {
                     var6 = this.useHandWeapon.getRecoilDelay();
                     Float var7 = (float)var6 * (1.0F - (float)this.getPerkLevel(PerkFactory.Perks.Aiming) / 30.0F);
                     this.setRecoilDelay((float)var7.intValue());
                  }

                  var6 = Rand.Next(0, 3);
                  if (var6 == 0) {
                     this.setVariable("AttackVariationX", Rand.Next(-1.0F, -0.5F));
                  }

                  if (var6 == 1) {
                     this.setVariable("AttackVariationX", 0.0F);
                  }

                  if (var6 == 2) {
                     this.setVariable("AttackVariationX", Rand.Next(0.5F, 1.0F));
                  }

                  this.setVariable("AttackVariationY", 0.0F);
                  if (var1) {
                     SwipeStatePlayer.instance().CalcHitList(this, true, this.attackVars, this.hitList);
                  }

                  IsoGameCharacter var11 = null;
                  if (!this.hitList.isEmpty()) {
                     var11 = (IsoGameCharacter)Type.tryCastTo(((HitInfo)this.hitList.get(0)).getObject(), IsoGameCharacter.class);
                  }

                  if (var11 == null) {
                     if (this.isAiming() && !this.m_meleePressed && this.useHandWeapon != this.bareHands) {
                        this.setDoShove(false);
                        this.setForceShove(false);
                     }

                     this.m_lastAttackWasShove = this.bDoShove;
                     if (var4.canMiss && !this.isAimAtFloor() && (!GameClient.bClient || this.isLocalPlayer())) {
                        this.setAttackType("miss");
                     }

                     if (this.isAiming() && this.bDoShove) {
                        this.setVariable("bShoveAiming", true);
                     } else {
                        this.clearVariable("bShoveAiming");
                     }

                  } else {
                     if (!GameClient.bClient || this.isLocalPlayer()) {
                        this.setAttackFromBehind(this.isBehind(var11));
                     }

                     float var8 = IsoUtils.DistanceTo(var11.x, var11.y, this.x, this.y);
                     this.setVariable("TargetDist", var8);
                     int var9 = this.calculateCritChance(var11);
                     if (var11 instanceof IsoZombie) {
                        IsoZombie var10 = this.getClosestZombieToOtherZombie((IsoZombie)var11);
                        if (!this.attackVars.bAimAtFloor && (double)var8 > 1.25D && var4 == WeaponType.spear && (var10 == null || (double)IsoUtils.DistanceTo(var11.x, var11.y, var10.x, var10.y) > 1.7D)) {
                           if (!GameClient.bClient || this.isLocalPlayer()) {
                              this.setAttackType("overhead");
                           }

                           var9 += 30;
                        }
                     }

                     if (this.isLocalPlayer() && !var11.isOnFloor()) {
                        var11.setHitFromBehind(this.isAttackFromBehind());
                     }

                     if (this.isAttackFromBehind()) {
                        if (var11 instanceof IsoZombie && ((IsoZombie)var11).target == null) {
                           var9 += 30;
                        } else {
                           var9 += 5;
                        }
                     }

                     if (var11 instanceof IsoPlayer && var4.isRanged && !this.bDoShove) {
                        var9 = (int)(this.attackVars.getWeapon(this).getStopPower() * (1.0F + (float)this.getPerkLevel(PerkFactory.Perks.Aiming) / 15.0F));
                     }

                     if (!GameClient.bClient || this.isLocalPlayer()) {
                        this.setCriticalHit(Rand.Next(100) < var9);
                        if (DebugOptions.instance.MultiplayerCriticalHit.getValue()) {
                           this.setCriticalHit(true);
                        }

                        if (this.isAttackFromBehind() && this.attackVars.bCloseKill && var11 instanceof IsoZombie && ((IsoZombie)var11).target == null) {
                           this.setCriticalHit(true);
                        }

                        if (this.isCriticalHit() && !this.attackVars.bCloseKill && !this.bDoShove && var4 == WeaponType.knife) {
                           this.setCriticalHit(false);
                        }

                        this.setAttackWasSuperAttack(false);
                        if (this.stats.NumChasingZombies > 1 && this.attackVars.bCloseKill && !this.bDoShove && var4 == WeaponType.knife) {
                           this.setCriticalHit(false);
                        }
                     }

                     if (this.isCriticalHit()) {
                        this.combatSpeed *= 1.1F;
                     }

                     if (DebugLog.isEnabled(DebugType.Combat)) {
                        DebugLog.Combat.debugln("Hit zombie dist: " + var8 + " crit: " + this.isCriticalHit() + " (" + var9 + "%) from behind: " + this.isAttackFromBehind());
                     }

                     if (this.isAiming() && this.bDoShove) {
                        this.setVariable("bShoveAiming", true);
                     } else {
                        this.clearVariable("bShoveAiming");
                     }

                     if (this.useHandWeapon != null && var4.isRanged) {
                        this.setRecoilDelay((float)(this.useHandWeapon.getRecoilDelay() - this.getPerkLevel(PerkFactory.Perks.Aiming) * 2));
                     }

                     this.m_lastAttackWasShove = this.bDoShove;
                  }
               }
            }
         }
      }
   }

   public void setAttackAnimThrowTimer(long var1) {
      this.AttackAnimThrowTimer = System.currentTimeMillis() + var1;
   }

   public boolean isAttackAnimThrowTimeOut() {
      return this.AttackAnimThrowTimer <= System.currentTimeMillis();
   }

   private boolean getAttackAnim() {
      return false;
   }

   private String getWeaponType() {
      return !this.isAttackAnimThrowTimeOut() ? "throwing" : this.WeaponT;
   }

   private void setWeaponType(String var1) {
      this.WeaponT = var1;
   }

   public int calculateCritChance(IsoGameCharacter var1) {
      if (this.bDoShove) {
         int var7 = 35;
         if (var1 instanceof IsoPlayer) {
            IsoPlayer var8 = (IsoPlayer)var1;
            var7 = 20;
            if (GameClient.bClient && !var8.isLocalPlayer()) {
               var7 = (int)((double)var7 - (double)var8.remoteStrLvl * 1.5D);
               if (var8.getNutrition().getWeight() < 80.0F) {
                  var7 = (int)((float)var7 + Math.abs((var8.getNutrition().getWeight() - 80.0F) / 2.0F));
               } else {
                  var7 = (int)((float)var7 - (var8.getNutrition().getWeight() - 80.0F) / 2.0F);
               }
            }
         }

         var7 -= this.getMoodles().getMoodleLevel(MoodleType.Endurance) * 5;
         var7 -= this.getMoodles().getMoodleLevel(MoodleType.HeavyLoad) * 5;
         var7 = (int)((double)var7 - (double)this.getMoodles().getMoodleLevel(MoodleType.Panic) * 1.3D);
         var7 += this.getPerkLevel(PerkFactory.Perks.Strength) * 2;
         return var7;
      } else if (this.bDoShove && var1.getStateMachine().getCurrent() == StaggerBackState.instance() && var1 instanceof IsoZombie) {
         return 100;
      } else if (this.getPrimaryHandItem() != null && this.getPrimaryHandItem() instanceof HandWeapon) {
         HandWeapon var2 = (HandWeapon)this.getPrimaryHandItem();
         int var3 = (int)var2.getCriticalChance();
         if (var2.isAlwaysKnockdown()) {
            return 100;
         } else {
            WeaponType var4 = WeaponType.getWeaponType((IsoGameCharacter)this);
            if (var4.isRanged) {
               var3 = (int)((float)var3 + (float)var2.getAimingPerkCritModifier() * ((float)this.getPerkLevel(PerkFactory.Perks.Aiming) / 2.0F));
               if (this.getBeenMovingFor() > (float)(var2.getAimingTime() + this.getPerkLevel(PerkFactory.Perks.Aiming) * 2)) {
                  var3 = (int)((float)var3 - (this.getBeenMovingFor() - (float)(var2.getAimingTime() + this.getPerkLevel(PerkFactory.Perks.Aiming) * 2)));
               }

               var3 += this.getPerkLevel(PerkFactory.Perks.Aiming) * 3;
               if (this.DistTo(var1) < 4.0F) {
                  var3 = (int)((float)var3 + (3.0F - this.DistTo(var1)) * 7.0F);
               } else if (this.DistTo(var1) >= 4.0F) {
                  var3 = (int)((float)var3 - (4.0F - this.DistTo(var1)) * 7.0F);
               }
            } else {
               if (var2.isTwoHandWeapon() && (this.getPrimaryHandItem() != var2 || this.getSecondaryHandItem() != var2)) {
                  var3 -= var3 / 3;
               }

               if (this.chargeTime < 2.0F) {
                  var3 -= var3 / 5;
               }

               int var5 = this.getPerkLevel(PerkFactory.Perks.Blunt);
               if (var2.getCategories().contains("Axe")) {
                  var5 = this.getPerkLevel(PerkFactory.Perks.Axe);
               }

               if (var2.getCategories().contains("LongBlade")) {
                  var5 = this.getPerkLevel(PerkFactory.Perks.LongBlade);
               }

               if (var2.getCategories().contains("Spear")) {
                  var5 = this.getPerkLevel(PerkFactory.Perks.Spear);
               }

               if (var2.getCategories().contains("SmallBlade")) {
                  var5 = this.getPerkLevel(PerkFactory.Perks.SmallBlade);
               }

               if (var2.getCategories().contains("SmallBlunt")) {
                  var5 = this.getPerkLevel(PerkFactory.Perks.SmallBlunt);
               }

               var3 += var5 * 3;
               if (var1 instanceof IsoPlayer) {
                  IsoPlayer var6 = (IsoPlayer)var1;
                  if (GameClient.bClient && !var6.isLocalPlayer()) {
                     var3 = (int)((double)var3 - (double)var6.remoteStrLvl * 1.5D);
                     if (var6.getNutrition().getWeight() < 80.0F) {
                        var3 = (int)((float)var3 + Math.abs((var6.getNutrition().getWeight() - 80.0F) / 2.0F));
                     } else {
                        var3 = (int)((float)var3 - (var6.getNutrition().getWeight() - 80.0F) / 2.0F);
                     }
                  }
               }
            }

            var3 -= this.getMoodles().getMoodleLevel(MoodleType.Endurance) * 5;
            var3 -= this.getMoodles().getMoodleLevel(MoodleType.HeavyLoad) * 5;
            var3 = (int)((double)var3 - (double)this.getMoodles().getMoodleLevel(MoodleType.Panic) * 1.3D);
            if (SandboxOptions.instance.Lore.Toughness.getValue() == 1) {
               var3 -= 6;
            }

            if (SandboxOptions.instance.Lore.Toughness.getValue() == 3) {
               var3 += 6;
            }

            if (var3 < 10) {
               var3 = 10;
            }

            if (var3 > 90) {
               var3 = 90;
            }

            return var3;
         }
      } else {
         return 0;
      }
   }

   private void checkJoypadIgnoreAimUntilCentered() {
      if (this.bJoypadIgnoreAimUntilCentered) {
         if (GameWindow.ActivatedJoyPad != null && this.JoypadBind != -1 && this.bJoypadMovementActive) {
            float var1 = JoypadManager.instance.getAimingAxisX(this.JoypadBind);
            float var2 = JoypadManager.instance.getAimingAxisY(this.JoypadBind);
            if (var1 * var1 + var2 + var2 <= 0.0F) {
               this.bJoypadIgnoreAimUntilCentered = false;
            }
         }

      }
   }

   public boolean isAimControlActive() {
      if (this.isForceAim()) {
         return true;
      } else if (this.isAimKeyDown()) {
         return true;
      } else {
         return GameWindow.ActivatedJoyPad != null && this.JoypadBind != -1 && this.bJoypadMovementActive && this.getJoypadAimVector(tempo).getLengthSquared() > 0.0F;
      }
   }

   private Vector2 getJoypadAimVector(Vector2 var1) {
      if (this.bJoypadIgnoreAimUntilCentered) {
         return var1.set(0.0F, 0.0F);
      } else {
         float var2 = JoypadManager.instance.getAimingAxisY(this.JoypadBind);
         float var3 = JoypadManager.instance.getAimingAxisX(this.JoypadBind);
         float var4 = JoypadManager.instance.getDeadZone(this.JoypadBind, 0);
         if (var3 * var3 + var2 * var2 < var4 * var4) {
            var2 = 0.0F;
            var3 = 0.0F;
         }

         return var1.set(var3, var2);
      }
   }

   private Vector2 getJoypadMoveVector(Vector2 var1) {
      float var2 = JoypadManager.instance.getMovementAxisY(this.JoypadBind);
      float var3 = JoypadManager.instance.getMovementAxisX(this.JoypadBind);
      float var4 = JoypadManager.instance.getDeadZone(this.JoypadBind, 0);
      if (var3 * var3 + var2 * var2 < var4 * var4) {
         var2 = 0.0F;
         var3 = 0.0F;
      }

      var1.set(var3, var2);
      if (this.isIgnoreInputsForDirection()) {
         var1.set(0.0F, 0.0F);
      }

      return var1;
   }

   private void updateToggleToAim() {
      if (this.PlayerIndex == 0) {
         if (!Core.getInstance().isToggleToAim()) {
            this.setForceAim(false);
         } else {
            boolean var1 = this.isAimKeyDown();
            long var2 = System.currentTimeMillis();
            if (var1) {
               if (this.aimKeyDownMS == 0L) {
                  this.aimKeyDownMS = var2;
               }
            } else {
               if (this.aimKeyDownMS != 0L && var2 - this.aimKeyDownMS < 500L) {
                  this.toggleForceAim();
               } else if (this.isForceAim()) {
                  if (this.aimKeyDownMS != 0L) {
                     this.toggleForceAim();
                  } else {
                     int var4 = Core.getInstance().getKey("Aim");
                     boolean var5 = var4 == 29 || var4 == 157;
                     if (var5 && UIManager.isMouseOverInventory()) {
                        this.toggleForceAim();
                     }
                  }
               }

               this.aimKeyDownMS = 0L;
            }

         }
      }
   }

   private void UpdateInputState(IsoPlayer.InputState var1) {
      var1.bMelee = false;
      if (!MPDebugAI.updateInputState(this, var1)) {
         if (GameWindow.ActivatedJoyPad != null && this.JoypadBind != -1) {
            if (this.bJoypadMovementActive) {
               var1.isAttacking = this.isCharging;
               if (this.bJoypadIgnoreChargingRT) {
                  var1.isAttacking = false;
               }

               if (this.bJoypadIgnoreAimUntilCentered) {
                  float var2 = JoypadManager.instance.getAimingAxisX(this.JoypadBind);
                  float var3 = JoypadManager.instance.getAimingAxisY(this.JoypadBind);
                  if (var2 == 0.0F && var3 == 0.0F) {
                     this.bJoypadIgnoreAimUntilCentered = false;
                  }
               }
            }

            if (this.isChargingLT) {
               var1.bMelee = true;
               var1.isAttacking = false;
            }
         } else {
            var1.isAttacking = this.isCharging && Mouse.isButtonDownUICheck(0);
            if (GameKeyboard.isKeyDown(Core.getInstance().getKey("Melee")) && this.authorizeMeleeAction) {
               var1.bMelee = true;
               var1.isAttacking = false;
            }
         }

         boolean var4;
         if (GameWindow.ActivatedJoyPad != null && this.JoypadBind != -1) {
            if (this.bJoypadMovementActive) {
               var1.isCharging = JoypadManager.instance.isRTPressed(this.JoypadBind);
               var1.isChargingLT = JoypadManager.instance.isLTPressed(this.JoypadBind);
               if (this.bJoypadIgnoreChargingRT && !var1.isCharging) {
                  this.bJoypadIgnoreChargingRT = false;
               }
            }

            var1.isAiming = false;
            var1.bRunning = false;
            var1.bSprinting = false;
            Vector2 var9 = this.getJoypadAimVector(tempVector2);
            if (var9.x == 0.0F && var9.y == 0.0F) {
               var1.isCharging = false;
               Vector2 var11 = this.getJoypadMoveVector(tempVector2);
               if (var11.x != 0.0F || var11.y != 0.0F) {
                  if (this.isAllowRun()) {
                     var1.bRunning = JoypadManager.instance.isRTPressed(this.JoypadBind);
                  }

                  var1.isAttacking = false;
                  var1.bMelee = false;
                  this.bJoypadIgnoreChargingRT = true;
                  var1.isCharging = false;
                  var4 = JoypadManager.instance.isBPressed(this.JoypadBind);
                  if (var1.bRunning && var4 && !this.bJoypadBDown) {
                     this.bJoypadSprint = !this.bJoypadSprint;
                  }

                  this.bJoypadBDown = var4;
                  var1.bSprinting = this.bJoypadSprint;
               }
            } else {
               var1.isAiming = true;
            }

            if (!var1.bRunning) {
               this.bJoypadBDown = false;
               this.bJoypadSprint = false;
            }
         } else {
            var1.isAiming = (this.isAimKeyDown() || Mouse.isButtonDownUICheck(1) && this.TimeRightPressed >= 0.15F) && this.getPlayerNum() == 0 && StringUtils.isNullOrEmpty(this.getVariableString("BumpFallType"));
            if (Mouse.isButtonDown(1)) {
               this.TimeRightPressed += GameTime.getInstance().getRealworldSecondsSinceLastUpdate();
            } else {
               this.TimeRightPressed = 0.0F;
            }

            if (!this.isCharging) {
               var1.isCharging = Mouse.isButtonDownUICheck(1) && this.TimeRightPressed >= 0.15F || this.isAimKeyDown();
            } else {
               var1.isCharging = Mouse.isButtonDown(1) || this.isAimKeyDown();
            }

            int var8 = Core.getInstance().getKey("Run");
            int var10 = Core.getInstance().getKey("Sprint");
            if (this.isAllowRun()) {
               var1.bRunning = GameKeyboard.isKeyDown(var8);
            }

            if (this.isAllowSprint()) {
               if (!Core.OptiondblTapJogToSprint) {
                  if (GameKeyboard.isKeyDown(var10)) {
                     var1.bSprinting = true;
                     this.pressedRunTimer = 1.0F;
                  } else {
                     var1.bSprinting = false;
                  }
               } else {
                  if (!GameKeyboard.wasKeyDown(var8) && GameKeyboard.isKeyDown(var8) && this.pressedRunTimer < 30.0F && this.pressedRun) {
                     var1.bSprinting = true;
                  }

                  if (GameKeyboard.wasKeyDown(var8) && !GameKeyboard.isKeyDown(var8)) {
                     var1.bSprinting = false;
                     this.pressedRun = true;
                  }

                  if (!var1.bRunning) {
                     var1.bSprinting = false;
                  }

                  if (this.pressedRun) {
                     ++this.pressedRunTimer;
                  }

                  if (this.pressedRunTimer > 30.0F) {
                     this.pressedRunTimer = 0.0F;
                     this.pressedRun = false;
                  }
               }
            }

            this.updateToggleToAim();
            if (var1.bRunning || var1.bSprinting) {
               this.setForceAim(false);
            }

            boolean var5;
            long var6;
            if (this.PlayerIndex == 0 && Core.getInstance().isToggleToRun()) {
               var4 = GameKeyboard.isKeyDown(var8);
               var5 = GameKeyboard.wasKeyDown(var8);
               var6 = System.currentTimeMillis();
               if (var4 && !var5) {
                  this.runKeyDownMS = var6;
               } else if (!var4 && var5 && var6 - this.runKeyDownMS < 500L) {
                  this.toggleForceRun();
               }
            }

            if (this.PlayerIndex == 0 && Core.getInstance().isToggleToSprint()) {
               var4 = GameKeyboard.isKeyDown(var10);
               var5 = GameKeyboard.wasKeyDown(var10);
               var6 = System.currentTimeMillis();
               if (var4 && !var5) {
                  this.sprintKeyDownMS = var6;
               } else if (!var4 && var5 && var6 - this.sprintKeyDownMS < 500L) {
                  this.toggleForceSprint();
               }
            }

            if (this.isForceAim()) {
               var1.isAiming = true;
               var1.isCharging = true;
            }

            if (this.isForceRun()) {
               var1.bRunning = true;
            }

            if (this.isForceSprint()) {
               var1.bSprinting = true;
            }
         }

      }
   }

   public IsoZombie getClosestZombieToOtherZombie(IsoZombie var1) {
      IsoZombie var2 = null;
      ArrayList var3 = new ArrayList();
      ArrayList var4 = IsoWorld.instance.CurrentCell.getObjectList();

      for(int var5 = 0; var5 < var4.size(); ++var5) {
         IsoMovingObject var6 = (IsoMovingObject)var4.get(var5);
         if (var6 != var1 && var6 instanceof IsoZombie) {
            var3.add((IsoZombie)var6);
         }
      }

      float var9 = 0.0F;

      for(int var10 = 0; var10 < var3.size(); ++var10) {
         IsoZombie var7 = (IsoZombie)var3.get(var10);
         float var8 = IsoUtils.DistanceTo(var7.x, var7.y, var1.x, var1.y);
         if (var2 == null || var8 < var9) {
            var2 = var7;
            var9 = var8;
         }
      }

      return var2;
   }

   /** @deprecated */
   @Deprecated
   public IsoGameCharacter getClosestZombieDist() {
      float var1 = 0.4F;
      boolean var2 = false;
      testHitPosition.x = this.x + this.getForwardDirection().x * var1;
      testHitPosition.y = this.y + this.getForwardDirection().y * var1;
      HandWeapon var3 = this.getWeapon();
      ArrayList var4 = new ArrayList();

      for(int var5 = (int)testHitPosition.x - (int)var3.getMaxRange(); var5 <= (int)testHitPosition.x + (int)var3.getMaxRange(); ++var5) {
         for(int var6 = (int)testHitPosition.y - (int)var3.getMaxRange(); var6 <= (int)testHitPosition.y + (int)var3.getMaxRange(); ++var6) {
            IsoGridSquare var7 = IsoWorld.instance.CurrentCell.getGridSquare((double)var5, (double)var6, (double)this.z);
            if (var7 != null && var7.getMovingObjects().size() > 0) {
               for(int var8 = 0; var8 < var7.getMovingObjects().size(); ++var8) {
                  IsoMovingObject var9 = (IsoMovingObject)var7.getMovingObjects().get(var8);
                  if (var9 instanceof IsoZombie) {
                     Vector2 var10 = tempVector2_1.set(this.getX(), this.getY());
                     Vector2 var11 = tempVector2_2.set(var9.getX(), var9.getY());
                     var11.x -= var10.x;
                     var11.y -= var10.y;
                     Vector2 var12 = this.getForwardDirection();
                     var11.normalize();
                     var12.normalize();
                     Float var13 = var11.dot(var12);
                     if (var13 >= var3.getMinAngle() || var9.isOnFloor()) {
                        var2 = true;
                     }

                     if (var2 && ((IsoZombie)var9).Health > 0.0F) {
                        ((IsoZombie)var9).setHitFromBehind(this.isBehind((IsoZombie)var9));
                        ((IsoZombie)var9).setHitAngle(((IsoZombie)var9).getForwardDirection());
                        ((IsoZombie)var9).setPlayerAttackPosition(((IsoZombie)var9).testDotSide(this));
                        float var14 = IsoUtils.DistanceTo(var9.x, var9.y, this.x, this.y);
                        if (var14 < var3.getMaxRange()) {
                           var4.add((IsoZombie)var9);
                        }
                     }
                  }
               }
            }
         }
      }

      if (!var4.isEmpty()) {
         Collections.sort(var4, new Comparator() {
            public int compare(IsoGameCharacter var1, IsoGameCharacter var2) {
               float var3 = IsoUtils.DistanceTo(var1.x, var1.y, IsoPlayer.testHitPosition.x, IsoPlayer.testHitPosition.y);
               float var4 = IsoUtils.DistanceTo(var2.x, var2.y, IsoPlayer.testHitPosition.x, IsoPlayer.testHitPosition.y);
               if (var3 > var4) {
                  return 1;
               } else {
                  return var4 > var3 ? -1 : 0;
               }
            }
         });
         return (IsoGameCharacter)var4.get(0);
      } else {
         return null;
      }
   }

   public void hitConsequences(HandWeapon var1, IsoGameCharacter var2, boolean var3, float var4, boolean var5) {
      String var6 = var2.getVariableString("ZombieHitReaction");
      if ("Shot".equals(var6)) {
         var2.setCriticalHit(Rand.Next(100) < ((IsoPlayer)var2).calculateCritChance(this));
      }

      this.setKnockedDown(var2.isCriticalHit());
      if (var2 instanceof IsoPlayer) {
         if (!StringUtils.isNullOrEmpty(this.getHitReaction())) {
            this.actionContext.reportEvent("washitpvpagain");
         }

         this.actionContext.reportEvent("washitpvp");
         this.setVariable("hitpvp", true);
      } else {
         this.actionContext.reportEvent("washit");
      }

      String var7;
      if (var3) {
         if (!GameServer.bServer) {
            var2.xp.AddXP(PerkFactory.Perks.Strength, 2.0F);
            this.setHitForce(Math.min(0.5F, this.getHitForce()));
            this.setHitReaction("HitReaction");
            var7 = this.testDotSide(var2);
            this.setHitFromBehind("BEHIND".equals(var7));
         }
      } else {
         if (!GameServer.bServer && (!GameClient.bClient || this.isLocalPlayer())) {
            this.BodyDamage.DamageFromWeapon(var1);
         } else if (!GameServer.bServer && !this.isLocalPlayer()) {
            this.BodyDamage.splatBloodFloorBig();
         }

         if ("Bite".equals(var6)) {
            var7 = this.testDotSide(var2);
            boolean var8 = var7.equals("FRONT");
            boolean var9 = var7.equals("BEHIND");
            if (var7.equals("RIGHT")) {
               var6 = var6 + "LEFT";
            }

            if (var7.equals("LEFT")) {
               var6 = var6 + "RIGHT";
            }

            if (var6 != null && !"".equals(var6)) {
               this.setHitReaction(var6);
            }
         } else if (!this.isKnockedDown()) {
            this.setHitReaction("HitReaction");
         }

      }
   }

   private HandWeapon getWeapon() {
      if (this.getPrimaryHandItem() instanceof HandWeapon) {
         return (HandWeapon)this.getPrimaryHandItem();
      } else {
         return this.getSecondaryHandItem() instanceof HandWeapon ? (HandWeapon)this.getSecondaryHandItem() : (HandWeapon)InventoryItemFactory.CreateItem("BareHands");
      }
   }

   private void updateMechanicsItems() {
      if (!GameServer.bServer && !this.mechanicsItem.isEmpty()) {
         Iterator var1 = this.mechanicsItem.keySet().iterator();
         ArrayList var2 = new ArrayList();

         while(var1.hasNext()) {
            Long var3 = (Long)var1.next();
            Long var4 = (Long)this.mechanicsItem.get(var3);
            if (GameTime.getInstance().getCalender().getTimeInMillis() > var4 + 86400000L) {
               var2.add(var3);
            }
         }

         for(int var5 = 0; var5 < var2.size(); ++var5) {
            this.mechanicsItem.remove(var2.get(var5));
         }

      }
   }

   private void enterExitVehicle() {
      boolean var1 = this.PlayerIndex == 0 && GameKeyboard.isKeyDown(Core.getInstance().getKey("Interact"));
      if (var1) {
         this.bUseVehicle = true;
         this.useVehicleDuration += GameTime.instance.getRealworldSecondsSinceLastUpdate();
      }

      if (!this.bUsedVehicle && this.bUseVehicle && (!var1 || this.useVehicleDuration > 0.5F)) {
         this.bUsedVehicle = true;
         if (this.getVehicle() != null) {
            LuaEventManager.triggerEvent("OnUseVehicle", this, this.getVehicle(), this.useVehicleDuration > 0.5F);
         } else {
            for(int var2 = 0; var2 < this.getCell().vehicles.size(); ++var2) {
               BaseVehicle var3 = (BaseVehicle)this.getCell().vehicles.get(var2);
               if (var3.getUseablePart(this) != null) {
                  LuaEventManager.triggerEvent("OnUseVehicle", this, var3, this.useVehicleDuration > 0.5F);
                  break;
               }
            }
         }
      }

      if (!var1) {
         this.bUseVehicle = false;
         this.bUsedVehicle = false;
         this.useVehicleDuration = 0.0F;
      }

   }

   private void checkActionGroup() {
      ActionGroup var1 = this.actionContext.getGroup();
      ActionGroup var2;
      if (this.getVehicle() == null) {
         var2 = ActionGroup.getActionGroup("player");
         if (var1 != var2) {
            this.advancedAnimator.OnAnimDataChanged(false);
            this.initializeStates();
            this.actionContext.setGroup(var2);
            this.clearVariable("bEnteringVehicle");
            this.clearVariable("EnterAnimationFinished");
            this.clearVariable("bExitingVehicle");
            this.clearVariable("ExitAnimationFinished");
            this.clearVariable("bSwitchingSeat");
            this.clearVariable("SwitchSeatAnimationFinished");
            this.setHitReaction("");
         }
      } else {
         var2 = ActionGroup.getActionGroup("player-vehicle");
         if (var1 != var2) {
            this.advancedAnimator.OnAnimDataChanged(false);
            this.initializeStates();
            this.actionContext.setGroup(var2);
         }
      }

   }

   public BaseVehicle getUseableVehicle() {
      if (this.getVehicle() != null) {
         return null;
      } else {
         int var1 = ((int)this.x - 4) / 10 - 1;
         int var2 = ((int)this.y - 4) / 10 - 1;
         int var3 = (int)Math.ceil((double)((this.x + 4.0F) / 10.0F)) + 1;
         int var4 = (int)Math.ceil((double)((this.y + 4.0F) / 10.0F)) + 1;

         for(int var5 = var2; var5 < var4; ++var5) {
            for(int var6 = var1; var6 < var3; ++var6) {
               IsoChunk var7 = GameServer.bServer ? ServerMap.instance.getChunk(var6, var5) : IsoWorld.instance.CurrentCell.getChunkForGridSquare(var6 * 10, var5 * 10, 0);
               if (var7 != null) {
                  for(int var8 = 0; var8 < var7.vehicles.size(); ++var8) {
                     BaseVehicle var9 = (BaseVehicle)var7.vehicles.get(var8);
                     if (var9.getUseablePart(this) != null || var9.getBestSeat(this) != -1) {
                        return var9;
                     }
                  }
               }
            }
         }

         return null;
      }
   }

   public Boolean isNearVehicle() {
      if (this.getVehicle() != null) {
         return false;
      } else {
         int var1 = ((int)this.x - 4) / 10 - 1;
         int var2 = ((int)this.y - 4) / 10 - 1;
         int var3 = (int)Math.ceil((double)((this.x + 4.0F) / 10.0F)) + 1;
         int var4 = (int)Math.ceil((double)((this.y + 4.0F) / 10.0F)) + 1;

         for(int var5 = var2; var5 < var4; ++var5) {
            for(int var6 = var1; var6 < var3; ++var6) {
               IsoChunk var7 = GameServer.bServer ? ServerMap.instance.getChunk(var6, var5) : IsoWorld.instance.CurrentCell.getChunkForGridSquare(var6 * 10, var5 * 10, 0);
               if (var7 != null) {
                  for(int var8 = 0; var8 < var7.vehicles.size(); ++var8) {
                     BaseVehicle var9 = (BaseVehicle)var7.vehicles.get(var8);
                     if ((double)var9.DistTo(this) < 3.5D) {
                        return true;
                     }
                  }
               }
            }
         }

         return false;
      }
   }

   public BaseVehicle getNearVehicle() {
      if (this.getVehicle() != null) {
         return null;
      } else {
         int var1 = ((int)this.x - 4) / 10 - 1;
         int var2 = ((int)this.y - 4) / 10 - 1;
         int var3 = (int)Math.ceil((double)((this.x + 4.0F) / 10.0F)) + 1;
         int var4 = (int)Math.ceil((double)((this.y + 4.0F) / 10.0F)) + 1;

         for(int var5 = var2; var5 < var4; ++var5) {
            for(int var6 = var1; var6 < var3; ++var6) {
               IsoChunk var7 = GameServer.bServer ? ServerMap.instance.getChunk(var6, var5) : IsoWorld.instance.CurrentCell.getChunk(var6, var5);
               if (var7 != null) {
                  for(int var8 = 0; var8 < var7.vehicles.size(); ++var8) {
                     BaseVehicle var9 = (BaseVehicle)var7.vehicles.get(var8);
                     if ((int)this.getZ() == (int)var9.getZ() && (!this.isLocalPlayer() || var9.getTargetAlpha(this.PlayerIndex) != 0.0F) && !(this.DistToSquared((float)((int)var9.x), (float)((int)var9.y)) >= 16.0F) && PolygonalMap2.instance.intersectLineWithVehicle(this.x, this.y, this.x + this.getForwardDirection().x * 4.0F, this.y + this.getForwardDirection().y * 4.0F, var9, tempVector2) && !PolygonalMap2.instance.lineClearCollide(this.x, this.y, tempVector2.x, tempVector2.y, (int)this.z, var9, false, true)) {
                        return var9;
                     }
                  }
               }
            }
         }

         return null;
      }
   }

   private void updateWhileInVehicle() {
      this.bLookingWhileInVehicle = false;
      ActionGroup var1 = this.actionContext.getGroup();
      ActionGroup var2 = ActionGroup.getActionGroup("player-vehicle");
      if (var1 != var2) {
         this.advancedAnimator.OnAnimDataChanged(false);
         this.initializeStates();
         this.actionContext.setGroup(var2);
      }

      if (GameClient.bClient && this.getVehicle().getSeat(this) == -1) {
         DebugLog.log("forced " + this.getUsername() + " out of vehicle seat -1");
         this.setVehicle((BaseVehicle)null);
      } else {
         this.dirtyRecalcGridStackTime = 10.0F;
         if (this.getVehicle().isDriver(this)) {
            this.getVehicle().updatePhysics();
            boolean var3 = true;
            if (this.isAiming()) {
               WeaponType var4 = WeaponType.getWeaponType((IsoGameCharacter)this);
               if (var4.equals(WeaponType.firearm)) {
                  var3 = false;
               }
            }

            if (this.getVariableBoolean("isLoading")) {
               var3 = false;
            }

            if (var3) {
               this.getVehicle().updateControls();
            }
         } else if (GameClient.connection != null) {
            PassengerMap.updatePassenger(this);
         }

         this.fallTime = 0.0F;
         this.bSeenThisFrame = false;
         this.bCouldBeSeenThisFrame = false;
         this.closestZombie = 1000000.0F;
         this.setBeenMovingFor(this.getBeenMovingFor() - 0.625F * GameTime.getInstance().getMultiplier());
         if (!this.Asleep) {
            float var10 = (float)ZomboidGlobals.SittingEnduranceMultiplier;
            var10 *= 1.0F - this.stats.fatigue;
            var10 *= GameTime.instance.getMultiplier();
            Stats var10000 = this.stats;
            var10000.endurance = (float)((double)var10000.endurance + ZomboidGlobals.ImobileEnduranceReduce * SandboxOptions.instance.getEnduranceRegenMultiplier() * (double)this.getRecoveryMod() * (double)var10);
            this.stats.endurance = PZMath.clamp(this.stats.endurance, 0.0F, 1.0F);
         }

         this.updateToggleToAim();
         if (this.vehicle != null) {
            Vector3f var12 = this.vehicle.getForwardVector(tempVector3f);
            boolean var11 = this.isAimControlActive();
            if (this.PlayerIndex == 0) {
               if (Mouse.isButtonDown(1)) {
                  this.TimeRightPressed += GameTime.getInstance().getRealworldSecondsSinceLastUpdate();
               } else {
                  this.TimeRightPressed = 0.0F;
               }

               var11 |= Mouse.isButtonDownUICheck(1) && this.TimeRightPressed >= 0.15F;
            }

            if (!var11 && this.isCurrentState(IdleState.instance())) {
               this.setForwardDirection(var12.x, var12.z);
               this.getForwardDirection().normalize();
            }

            if (this.lastAngle.x != this.getForwardDirection().x || this.lastAngle.y != this.getForwardDirection().y) {
               this.dirtyRecalcGridStackTime = 10.0F;
            }

            this.DirectionFromVector(this.getForwardDirection());
            AnimationPlayer var5 = this.getAnimationPlayer();
            if (var5 != null && var5.isReady()) {
               var5.SetForceDir(this.getForwardDirection());
               float var6 = var5.getAngle() + var5.getTwistAngle();
               this.dir = IsoDirections.fromAngle(tempVector2.setLengthAndDirection(var6, 1.0F));
            }

            boolean var13 = false;
            int var7 = this.vehicle.getSeat(this);
            VehiclePart var8 = this.vehicle.getPassengerDoor(var7);
            if (var8 != null) {
               VehicleWindow var9 = var8.findWindow();
               if (var9 != null && !var9.isHittable()) {
                  var13 = true;
               }
            }

            if (var13) {
               this.attackWhileInVehicle();
            } else if (var11) {
               this.bLookingWhileInVehicle = true;
               this.setAngleFromAim();
            } else {
               this.checkJoypadIgnoreAimUntilCentered();
               this.setIsAiming(false);
            }
         }

         this.updateCursorVisibility();
      }
   }

   private void attackWhileInVehicle() {
      this.setIsAiming(false);
      boolean var1 = false;
      boolean var2 = false;
      if (GameWindow.ActivatedJoyPad != null && this.JoypadBind != -1) {
         if (!this.bJoypadMovementActive) {
            return;
         }

         if (this.isChargingLT && !JoypadManager.instance.isLTPressed(this.JoypadBind)) {
            var2 = true;
         } else {
            var1 = this.isCharging && !JoypadManager.instance.isRTPressed(this.JoypadBind);
         }

         float var5 = JoypadManager.instance.getAimingAxisX(this.JoypadBind);
         float var4 = JoypadManager.instance.getAimingAxisY(this.JoypadBind);
         if (this.bJoypadIgnoreAimUntilCentered) {
            if (var5 == 0.0F && var4 == 0.0F) {
               this.bJoypadIgnoreAimUntilCentered = false;
            } else {
               var4 = 0.0F;
               var5 = 0.0F;
            }
         }

         this.setIsAiming(var5 * var5 + var4 * var4 >= 0.09F);
         this.isCharging = this.isAiming() && JoypadManager.instance.isRTPressed(this.JoypadBind);
         this.isChargingLT = this.isAiming() && JoypadManager.instance.isLTPressed(this.JoypadBind);
      } else {
         boolean var3 = this.isAimKeyDown();
         this.setIsAiming(var3 || Mouse.isButtonDownUICheck(1) && this.TimeRightPressed >= 0.15F);
         if (this.isCharging) {
            this.isCharging = var3 || Mouse.isButtonDown(1);
         } else {
            this.isCharging = var3 || Mouse.isButtonDownUICheck(1) && this.TimeRightPressed >= 0.15F;
         }

         if (this.isForceAim()) {
            this.setIsAiming(true);
            this.isCharging = true;
         }

         if (GameKeyboard.isKeyDown(Core.getInstance().getKey("Melee")) && this.authorizeMeleeAction) {
            var2 = true;
         } else {
            var1 = this.isCharging && Mouse.isButtonDownUICheck(0);
            if (var1) {
               this.setIsAiming(true);
            }
         }
      }

      if (!this.isCharging && !this.isChargingLT) {
         this.chargeTime = 0.0F;
      }

      if (this.isAiming() && !this.bBannedAttacking && this.CanAttack()) {
         this.chargeTime += GameTime.instance.getMultiplier();
         this.useChargeTime = this.chargeTime;
         this.m_meleePressed = var2;
         this.setAngleFromAim();
         if (var2) {
            this.sprite.Animate = true;
            this.setDoShove(true);
            this.AttemptAttack(this.useChargeTime);
            this.useChargeTime = 0.0F;
            this.chargeTime = 0.0F;
         } else if (var1) {
            this.sprite.Animate = true;
            if (this.getRecoilDelay() <= 0.0F) {
               this.AttemptAttack(this.useChargeTime);
            }

            this.useChargeTime = 0.0F;
            this.chargeTime = 0.0F;
         }

      }
   }

   private void setAngleFromAim() {
      Vector2 var1 = tempVector2;
      if (GameWindow.ActivatedJoyPad != null && this.JoypadBind != -1) {
         this.getControllerAimDir(var1);
      } else {
         var1.set(this.getX(), this.getY());
         int var2 = Mouse.getX();
         int var3 = Mouse.getY();
         var1.x -= IsoUtils.XToIso((float)var2, (float)var3 + 55.0F * this.def.getScaleY(), this.getZ());
         var1.y -= IsoUtils.YToIso((float)var2, (float)var3 + 55.0F * this.def.getScaleY(), this.getZ());
         var1.x = -var1.x;
         var1.y = -var1.y;
      }

      if (var1.getLengthSquared() > 0.0F) {
         var1.normalize();
         this.DirectionFromVector(var1);
         this.setForwardDirection(var1);
         if (this.lastAngle.x != var1.x || this.lastAngle.y != var1.y) {
            this.lastAngle.x = var1.x;
            this.lastAngle.y = var1.y;
            this.dirtyRecalcGridStackTime = 10.0F;
         }
      }

   }

   private void updateTorchStrength() {
      if (this.getTorchStrength() > 0.0F || this.flickTorch) {
         DrainableComboItem var1 = (DrainableComboItem)Type.tryCastTo(this.getActiveLightItem(), DrainableComboItem.class);
         if (var1 == null) {
            return;
         }

         if (Rand.Next(600 - (int)(0.4D / (double)var1.getUsedDelta() * 100.0D)) == 0) {
            this.flickTorch = true;
         }

         this.flickTorch = false;
         if (this.flickTorch) {
            if (Rand.Next(6) == 0) {
               var1.setActivated(false);
            } else {
               var1.setActivated(true);
            }

            if (Rand.Next(40) == 0) {
               this.flickTorch = false;
               var1.setActivated(true);
            }
         }
      }

   }

   public IsoCell getCell() {
      return IsoWorld.instance.CurrentCell;
   }

   public void calculateContext() {
      float var1 = this.x;
      float var2 = this.y;
      float var3 = this.x;
      IsoGridSquare[] var4 = new IsoGridSquare[4];
      if (this.dir == IsoDirections.N) {
         var4[2] = this.getCell().getGridSquare((double)(var1 - 1.0F), (double)(var2 - 1.0F), (double)var3);
         var4[1] = this.getCell().getGridSquare((double)var1, (double)(var2 - 1.0F), (double)var3);
         var4[3] = this.getCell().getGridSquare((double)(var1 + 1.0F), (double)(var2 - 1.0F), (double)var3);
      } else if (this.dir == IsoDirections.NE) {
         var4[2] = this.getCell().getGridSquare((double)var1, (double)(var2 - 1.0F), (double)var3);
         var4[1] = this.getCell().getGridSquare((double)(var1 + 1.0F), (double)(var2 - 1.0F), (double)var3);
         var4[3] = this.getCell().getGridSquare((double)(var1 + 1.0F), (double)var2, (double)var3);
      } else if (this.dir == IsoDirections.E) {
         var4[2] = this.getCell().getGridSquare((double)(var1 + 1.0F), (double)(var2 - 1.0F), (double)var3);
         var4[1] = this.getCell().getGridSquare((double)(var1 + 1.0F), (double)var2, (double)var3);
         var4[3] = this.getCell().getGridSquare((double)(var1 + 1.0F), (double)(var2 + 1.0F), (double)var3);
      } else if (this.dir == IsoDirections.SE) {
         var4[2] = this.getCell().getGridSquare((double)(var1 + 1.0F), (double)var2, (double)var3);
         var4[1] = this.getCell().getGridSquare((double)(var1 + 1.0F), (double)(var2 + 1.0F), (double)var3);
         var4[3] = this.getCell().getGridSquare((double)var1, (double)(var2 + 1.0F), (double)var3);
      } else if (this.dir == IsoDirections.S) {
         var4[2] = this.getCell().getGridSquare((double)(var1 + 1.0F), (double)(var2 + 1.0F), (double)var3);
         var4[1] = this.getCell().getGridSquare((double)var1, (double)(var2 + 1.0F), (double)var3);
         var4[3] = this.getCell().getGridSquare((double)(var1 - 1.0F), (double)(var2 + 1.0F), (double)var3);
      } else if (this.dir == IsoDirections.SW) {
         var4[2] = this.getCell().getGridSquare((double)var1, (double)(var2 + 1.0F), (double)var3);
         var4[1] = this.getCell().getGridSquare((double)(var1 - 1.0F), (double)(var2 + 1.0F), (double)var3);
         var4[3] = this.getCell().getGridSquare((double)(var1 - 1.0F), (double)var2, (double)var3);
      } else if (this.dir == IsoDirections.W) {
         var4[2] = this.getCell().getGridSquare((double)(var1 - 1.0F), (double)(var2 + 1.0F), (double)var3);
         var4[1] = this.getCell().getGridSquare((double)(var1 - 1.0F), (double)var2, (double)var3);
         var4[3] = this.getCell().getGridSquare((double)(var1 - 1.0F), (double)(var2 - 1.0F), (double)var3);
      } else if (this.dir == IsoDirections.NW) {
         var4[2] = this.getCell().getGridSquare((double)(var1 - 1.0F), (double)var2, (double)var3);
         var4[1] = this.getCell().getGridSquare((double)(var1 - 1.0F), (double)(var2 - 1.0F), (double)var3);
         var4[3] = this.getCell().getGridSquare((double)var1, (double)(var2 - 1.0F), (double)var3);
      }

      var4[0] = this.current;

      for(int var5 = 0; var5 < 4; ++var5) {
         IsoGridSquare var6 = var4[var5];
         if (var6 == null) {
         }
      }

   }

   public boolean isSafeToClimbOver(IsoDirections var1) {
      IsoGridSquare var2 = null;
      switch(var1) {
      case N:
         var2 = this.getCell().getGridSquare((double)this.x, (double)(this.y - 1.0F), (double)this.z);
         break;
      case S:
         var2 = this.getCell().getGridSquare((double)this.x, (double)(this.y + 1.0F), (double)this.z);
         break;
      case W:
         var2 = this.getCell().getGridSquare((double)(this.x - 1.0F), (double)this.y, (double)this.z);
         break;
      case E:
         var2 = this.getCell().getGridSquare((double)(this.x + 1.0F), (double)this.y, (double)this.z);
         break;
      default:
         return false;
      }

      if (var2 == null) {
         return false;
      } else if (var2.Is(IsoFlagType.water)) {
         return false;
      } else {
         return !var2.TreatAsSolidFloor() ? var2.HasStairsBelow() : true;
      }
   }

   public boolean doContext(IsoDirections var1) {
      if (this.isIgnoreContextKey()) {
         return false;
      } else if (this.isBlockMovement()) {
         return false;
      } else {
         for(int var2 = 0; var2 < this.getCell().vehicles.size(); ++var2) {
            BaseVehicle var3 = (BaseVehicle)this.getCell().vehicles.get(var2);
            if (var3.getUseablePart(this) != null) {
               return false;
            }
         }

         float var7 = this.x - (float)((int)this.x);
         float var8 = this.y - (float)((int)this.y);
         IsoDirections var4 = IsoDirections.Max;
         IsoDirections var5 = IsoDirections.Max;
         if (var1 == IsoDirections.NW) {
            if (var8 < var7) {
               if (this.doContextNSWE(IsoDirections.N)) {
                  return true;
               }

               if (this.doContextNSWE(IsoDirections.W)) {
                  return true;
               }

               var4 = IsoDirections.S;
               var5 = IsoDirections.E;
            } else {
               if (this.doContextNSWE(IsoDirections.W)) {
                  return true;
               }

               if (this.doContextNSWE(IsoDirections.N)) {
                  return true;
               }

               var4 = IsoDirections.E;
               var5 = IsoDirections.S;
            }
         } else if (var1 == IsoDirections.NE) {
            var7 = 1.0F - var7;
            if (var8 < var7) {
               if (this.doContextNSWE(IsoDirections.N)) {
                  return true;
               }

               if (this.doContextNSWE(IsoDirections.E)) {
                  return true;
               }

               var4 = IsoDirections.S;
               var5 = IsoDirections.W;
            } else {
               if (this.doContextNSWE(IsoDirections.E)) {
                  return true;
               }

               if (this.doContextNSWE(IsoDirections.N)) {
                  return true;
               }

               var4 = IsoDirections.W;
               var5 = IsoDirections.S;
            }
         } else if (var1 == IsoDirections.SE) {
            var7 = 1.0F - var7;
            var8 = 1.0F - var8;
            if (var8 < var7) {
               if (this.doContextNSWE(IsoDirections.S)) {
                  return true;
               }

               if (this.doContextNSWE(IsoDirections.E)) {
                  return true;
               }

               var4 = IsoDirections.N;
               var5 = IsoDirections.W;
            } else {
               if (this.doContextNSWE(IsoDirections.E)) {
                  return true;
               }

               if (this.doContextNSWE(IsoDirections.S)) {
                  return true;
               }

               var4 = IsoDirections.W;
               var5 = IsoDirections.N;
            }
         } else if (var1 == IsoDirections.SW) {
            var8 = 1.0F - var8;
            if (var8 < var7) {
               if (this.doContextNSWE(IsoDirections.S)) {
                  return true;
               }

               if (this.doContextNSWE(IsoDirections.W)) {
                  return true;
               }

               var4 = IsoDirections.N;
               var5 = IsoDirections.E;
            } else {
               if (this.doContextNSWE(IsoDirections.W)) {
                  return true;
               }

               if (this.doContextNSWE(IsoDirections.S)) {
                  return true;
               }

               var4 = IsoDirections.E;
               var5 = IsoDirections.N;
            }
         } else {
            if (this.doContextNSWE(var1)) {
               return true;
            }

            var4 = var1.RotLeft(4);
         }

         IsoObject var6;
         if (var4 != IsoDirections.Max) {
            var6 = this.getContextDoorOrWindowOrWindowFrame(var4);
            if (var6 != null) {
               this.doContextDoorOrWindowOrWindowFrame(var4, var6);
               return true;
            }
         }

         if (var5 != IsoDirections.Max) {
            var6 = this.getContextDoorOrWindowOrWindowFrame(var5);
            if (var6 != null) {
               this.doContextDoorOrWindowOrWindowFrame(var5, var6);
               return true;
            }
         }

         return false;
      }
   }

   private boolean doContextNSWE(IsoDirections var1) {
      assert var1 == IsoDirections.N || var1 == IsoDirections.S || var1 == IsoDirections.W || var1 == IsoDirections.E;

      if (this.current == null) {
         return false;
      } else if (var1 == IsoDirections.N && this.current.Is(IsoFlagType.climbSheetN) && this.canClimbSheetRope(this.current)) {
         this.climbSheetRope();
         return true;
      } else if (var1 == IsoDirections.S && this.current.Is(IsoFlagType.climbSheetS) && this.canClimbSheetRope(this.current)) {
         this.climbSheetRope();
         return true;
      } else if (var1 == IsoDirections.W && this.current.Is(IsoFlagType.climbSheetW) && this.canClimbSheetRope(this.current)) {
         this.climbSheetRope();
         return true;
      } else if (var1 == IsoDirections.E && this.current.Is(IsoFlagType.climbSheetE) && this.canClimbSheetRope(this.current)) {
         this.climbSheetRope();
         return true;
      } else {
         IsoGridSquare var2 = this.current.nav[var1.index()];
         boolean var3 = IsoWindow.isTopOfSheetRopeHere(var2) && this.canClimbDownSheetRope(var2);
         IsoObject var4 = this.getContextDoorOrWindowOrWindowFrame(var1);
         if (var4 != null) {
            this.doContextDoorOrWindowOrWindowFrame(var1, var4);
            return true;
         } else {
            if (GameKeyboard.isKeyDown(42) && this.current != null && this.ticksSincePressedMovement > 15.0F) {
               IsoObject var5 = this.current.getDoor(true);
               if (var5 instanceof IsoDoor && ((IsoDoor)var5).isFacingSheet(this)) {
                  ((IsoDoor)var5).toggleCurtain();
                  return true;
               }

               IsoObject var6 = this.current.getDoor(false);
               if (var6 instanceof IsoDoor && ((IsoDoor)var6).isFacingSheet(this)) {
                  ((IsoDoor)var6).toggleCurtain();
                  return true;
               }

               IsoGridSquare var7;
               IsoObject var8;
               if (var1 == IsoDirections.E) {
                  var7 = IsoWorld.instance.CurrentCell.getGridSquare((double)(this.x + 1.0F), (double)this.y, (double)this.z);
                  var8 = var7 != null ? var7.getDoor(true) : null;
                  if (var8 instanceof IsoDoor && ((IsoDoor)var8).isFacingSheet(this)) {
                     ((IsoDoor)var8).toggleCurtain();
                     return true;
                  }
               }

               if (var1 == IsoDirections.S) {
                  var7 = IsoWorld.instance.CurrentCell.getGridSquare((double)this.x, (double)(this.y + 1.0F), (double)this.z);
                  var8 = var7 != null ? var7.getDoor(false) : null;
                  if (var8 instanceof IsoDoor && ((IsoDoor)var8).isFacingSheet(this)) {
                     ((IsoDoor)var8).toggleCurtain();
                     return true;
                  }
               }
            }

            boolean var9 = this.isSafeToClimbOver(var1);
            if (this.z > 0.0F && var3) {
               var9 = true;
            }

            if (this.timePressedContext < 0.5F && !var9) {
               return false;
            } else if (this.ignoreAutoVault) {
               return false;
            } else if (var1 == IsoDirections.N && this.getCurrentSquare().Is(IsoFlagType.HoppableN)) {
               this.climbOverFence(var1);
               return true;
            } else if (var1 == IsoDirections.W && this.getCurrentSquare().Is(IsoFlagType.HoppableW)) {
               this.climbOverFence(var1);
               return true;
            } else if (var1 == IsoDirections.S && IsoWorld.instance.CurrentCell.getGridSquare((double)this.x, (double)(this.y + 1.0F), (double)this.z) != null && IsoWorld.instance.CurrentCell.getGridSquare((double)this.x, (double)(this.y + 1.0F), (double)this.z).Is(IsoFlagType.HoppableN)) {
               this.climbOverFence(var1);
               return true;
            } else if (var1 == IsoDirections.E && IsoWorld.instance.CurrentCell.getGridSquare((double)(this.x + 1.0F), (double)this.y, (double)this.z) != null && IsoWorld.instance.CurrentCell.getGridSquare((double)(this.x + 1.0F), (double)this.y, (double)this.z).Is(IsoFlagType.HoppableW)) {
               this.climbOverFence(var1);
               return true;
            } else {
               return this.climbOverWall(var1);
            }
         }
      }
   }

   public IsoObject getContextDoorOrWindowOrWindowFrame(IsoDirections var1) {
      if (this.current != null && var1 != null) {
         IsoGridSquare var2 = this.current.nav[var1.index()];
         IsoObject var3 = null;
         switch(var1) {
         case N:
            var3 = this.current.getOpenDoor(var1);
            if (var3 != null) {
               return var3;
            }

            var3 = this.current.getDoorOrWindowOrWindowFrame(var1, true);
            if (var3 != null) {
               return var3;
            }

            var3 = this.current.getDoor(true);
            if (var3 != null) {
               return var3;
            }

            if (var2 != null && !this.current.isBlockedTo(var2)) {
               var3 = var2.getOpenDoor(IsoDirections.S);
            }
            break;
         case S:
            var3 = this.current.getOpenDoor(var1);
            if (var3 != null) {
               return var3;
            }

            if (var2 != null) {
               boolean var4 = this.current.isBlockedTo(var2);
               var3 = var2.getDoorOrWindowOrWindowFrame(IsoDirections.N, var4);
               if (var3 != null) {
                  return var3;
               }

               var3 = var2.getDoor(true);
            }
            break;
         case W:
            var3 = this.current.getOpenDoor(var1);
            if (var3 != null) {
               return var3;
            }

            var3 = this.current.getDoorOrWindowOrWindowFrame(var1, true);
            if (var3 != null) {
               return var3;
            }

            var3 = this.current.getDoor(false);
            if (var3 != null) {
               return var3;
            }

            if (var2 != null && !this.current.isBlockedTo(var2)) {
               var3 = var2.getOpenDoor(IsoDirections.E);
            }
            break;
         case E:
            var3 = this.current.getOpenDoor(var1);
            if (var3 != null) {
               return var3;
            }

            if (var2 != null) {
               boolean var5 = this.current.isBlockedTo(var2);
               var3 = var2.getDoorOrWindowOrWindowFrame(IsoDirections.W, var5);
               if (var3 != null) {
                  return var3;
               }

               var3 = var2.getDoor(false);
            }
         }

         return var3;
      } else {
         return null;
      }
   }

   private void doContextDoorOrWindowOrWindowFrame(IsoDirections var1, IsoObject var2) {
      IsoGridSquare var3 = this.current.nav[var1.index()];
      boolean var4 = IsoWindow.isTopOfSheetRopeHere(var3) && this.canClimbDownSheetRope(var3);
      if (var2 instanceof IsoDoor) {
         IsoDoor var5 = (IsoDoor)var2;
         if (GameKeyboard.isKeyDown(42) && var5.HasCurtains() != null && var5.isFacingSheet(this) && this.ticksSincePressedMovement > 15.0F) {
            var5.toggleCurtain();
         } else if (this.timePressedContext >= 0.5F) {
            if (var5.isHoppable() && !this.isIgnoreAutoVault()) {
               this.climbOverFence(var1);
            } else {
               var5.ToggleDoor(this);
            }
         } else {
            var5.ToggleDoor(this);
         }
      } else {
         IsoThumpable var8;
         if (var2 instanceof IsoThumpable && ((IsoThumpable)var2).isDoor()) {
            var8 = (IsoThumpable)var2;
            if (this.timePressedContext >= 0.5F) {
               if (var8.isHoppable() && !this.isIgnoreAutoVault()) {
                  this.climbOverFence(var1);
               } else {
                  var8.ToggleDoor(this);
               }
            } else {
               var8.ToggleDoor(this);
            }
         } else {
            IsoCurtain var6;
            if (var2 instanceof IsoWindow && !var2.getSquare().getProperties().Is(IsoFlagType.makeWindowInvincible)) {
               IsoWindow var9 = (IsoWindow)var2;
               if (GameKeyboard.isKeyDown(42)) {
                  var6 = var9.HasCurtains();
                  if (var6 != null && this.current != null && !var6.getSquare().isBlockedTo(this.current)) {
                     var6.ToggleDoor(this);
                  }
               } else if (this.timePressedContext >= 0.5F) {
                  if (var9.canClimbThrough(this)) {
                     this.climbThroughWindow(var9);
                  } else if (!var9.PermaLocked && !var9.isBarricaded() && !var9.IsOpen()) {
                     this.openWindow(var9);
                  }
               } else if (var9.Health > 0 && !var9.isDestroyed()) {
                  IsoBarricade var10 = var9.getBarricadeForCharacter(this);
                  if (!var9.open && var10 == null) {
                     this.openWindow(var9);
                  } else if (var10 == null) {
                     this.closeWindow(var9);
                  }
               } else if (var9.isGlassRemoved()) {
                  if (!this.isSafeToClimbOver(var1) && !var2.getSquare().haveSheetRope && !var4) {
                     return;
                  }

                  if (!var9.isBarricaded()) {
                     this.climbThroughWindow(var9);
                  }
               }
            } else if (var2 instanceof IsoThumpable && !var2.getSquare().getProperties().Is(IsoFlagType.makeWindowInvincible)) {
               var8 = (IsoThumpable)var2;
               if (GameKeyboard.isKeyDown(42)) {
                  var6 = var8.HasCurtains();
                  if (var6 != null && this.current != null && !var6.getSquare().isBlockedTo(this.current)) {
                     var6.ToggleDoor(this);
                  }
               } else if (this.timePressedContext >= 0.5F) {
                  if (var8.canClimbThrough(this)) {
                     this.climbThroughWindow(var8);
                  }
               } else {
                  if (!this.isSafeToClimbOver(var1) && !var2.getSquare().haveSheetRope && !var4) {
                     return;
                  }

                  if (var8.canClimbThrough(this)) {
                     this.climbThroughWindow(var8);
                  }
               }
            } else if (IsoWindowFrame.isWindowFrame(var2)) {
               if (GameKeyboard.isKeyDown(42)) {
                  IsoCurtain var7 = IsoWindowFrame.getCurtain(var2);
                  if (var7 != null && this.current != null && !var7.getSquare().isBlockedTo(this.current)) {
                     var7.ToggleDoor(this);
                  }
               } else if ((this.timePressedContext >= 0.5F || this.isSafeToClimbOver(var1) || var4) && IsoWindowFrame.canClimbThrough(var2, this)) {
                  this.climbThroughWindowFrame(var2);
               }
            }
         }
      }

   }

   public boolean hopFence(IsoDirections var1, boolean var2) {
      float var4 = this.x - (float)((int)this.x);
      float var5 = this.y - (float)((int)this.y);
      if (var1 == IsoDirections.NW) {
         if (var5 < var4) {
            return this.hopFence(IsoDirections.N, var2) ? true : this.hopFence(IsoDirections.W, var2);
         } else {
            return this.hopFence(IsoDirections.W, var2) ? true : this.hopFence(IsoDirections.N, var2);
         }
      } else if (var1 == IsoDirections.NE) {
         var4 = 1.0F - var4;
         if (var5 < var4) {
            return this.hopFence(IsoDirections.N, var2) ? true : this.hopFence(IsoDirections.E, var2);
         } else {
            return this.hopFence(IsoDirections.E, var2) ? true : this.hopFence(IsoDirections.N, var2);
         }
      } else if (var1 == IsoDirections.SE) {
         var4 = 1.0F - var4;
         var5 = 1.0F - var5;
         if (var5 < var4) {
            return this.hopFence(IsoDirections.S, var2) ? true : this.hopFence(IsoDirections.E, var2);
         } else {
            return this.hopFence(IsoDirections.E, var2) ? true : this.hopFence(IsoDirections.S, var2);
         }
      } else if (var1 == IsoDirections.SW) {
         var5 = 1.0F - var5;
         if (var5 < var4) {
            return this.hopFence(IsoDirections.S, var2) ? true : this.hopFence(IsoDirections.W, var2);
         } else {
            return this.hopFence(IsoDirections.W, var2) ? true : this.hopFence(IsoDirections.S, var2);
         }
      } else if (this.current == null) {
         return false;
      } else {
         IsoGridSquare var6 = this.current.nav[var1.index()];
         if (var6 != null && !var6.Is(IsoFlagType.water)) {
            if (var1 == IsoDirections.N && this.getCurrentSquare().Is(IsoFlagType.HoppableN)) {
               if (var2) {
                  return true;
               } else {
                  this.climbOverFence(var1);
                  return true;
               }
            } else if (var1 == IsoDirections.W && this.getCurrentSquare().Is(IsoFlagType.HoppableW)) {
               if (var2) {
                  return true;
               } else {
                  this.climbOverFence(var1);
                  return true;
               }
            } else if (var1 == IsoDirections.S && IsoWorld.instance.CurrentCell.getGridSquare((double)this.x, (double)(this.y + 1.0F), (double)this.z) != null && IsoWorld.instance.CurrentCell.getGridSquare((double)this.x, (double)(this.y + 1.0F), (double)this.z).Is(IsoFlagType.HoppableN)) {
               if (var2) {
                  return true;
               } else {
                  this.climbOverFence(var1);
                  return true;
               }
            } else if (var1 == IsoDirections.E && IsoWorld.instance.CurrentCell.getGridSquare((double)(this.x + 1.0F), (double)this.y, (double)this.z) != null && IsoWorld.instance.CurrentCell.getGridSquare((double)(this.x + 1.0F), (double)this.y, (double)this.z).Is(IsoFlagType.HoppableW)) {
               if (var2) {
                  return true;
               } else {
                  this.climbOverFence(var1);
                  return true;
               }
            } else {
               return false;
            }
         } else {
            return false;
         }
      }
   }

   public boolean canClimbOverWall(IsoDirections var1) {
      if (this.isSprinting()) {
         return false;
      } else if (this.isSafeToClimbOver(var1) && this.current != null) {
         if (this.current.haveRoof) {
            return false;
         } else if (this.current.getBuilding() != null) {
            return false;
         } else {
            IsoGridSquare var2 = IsoWorld.instance.CurrentCell.getGridSquare(this.current.x, this.current.y, this.current.z + 1);
            if (var2 != null && var2.HasSlopedRoof()) {
               return false;
            } else {
               IsoGridSquare var3 = this.current.nav[var1.index()];
               if (var3.haveRoof) {
                  return false;
               } else if (!var3.isSolid() && !var3.isSolidTrans()) {
                  if (var3.getBuilding() != null) {
                     return false;
                  } else {
                     IsoGridSquare var4 = IsoWorld.instance.CurrentCell.getGridSquare(var3.x, var3.y, var3.z + 1);
                     if (var4 != null && var4.HasSlopedRoof()) {
                        return false;
                     } else {
                        switch(var1) {
                        case N:
                           if (this.current.Is(IsoFlagType.CantClimb)) {
                              return false;
                           }

                           if (!this.current.Has(IsoObjectType.wall)) {
                              return false;
                           }

                           if (!this.current.Is(IsoFlagType.collideN)) {
                              return false;
                           }

                           if (this.current.Is(IsoFlagType.HoppableN)) {
                              return false;
                           }

                           if (var2 != null && var2.Is(IsoFlagType.collideN)) {
                              return false;
                           }
                           break;
                        case S:
                           if (var3.Is(IsoFlagType.CantClimb)) {
                              return false;
                           }

                           if (!var3.Has(IsoObjectType.wall)) {
                              return false;
                           }

                           if (!var3.Is(IsoFlagType.collideN)) {
                              return false;
                           }

                           if (var3.Is(IsoFlagType.HoppableN)) {
                              return false;
                           }

                           if (var4 != null && var4.Is(IsoFlagType.collideN)) {
                              return false;
                           }
                           break;
                        case W:
                           if (this.current.Is(IsoFlagType.CantClimb)) {
                              return false;
                           }

                           if (!this.current.Has(IsoObjectType.wall)) {
                              return false;
                           }

                           if (!this.current.Is(IsoFlagType.collideW)) {
                              return false;
                           }

                           if (this.current.Is(IsoFlagType.HoppableW)) {
                              return false;
                           }

                           if (var2 != null && var2.Is(IsoFlagType.collideW)) {
                              return false;
                           }
                           break;
                        case E:
                           if (var3.Is(IsoFlagType.CantClimb)) {
                              return false;
                           }

                           if (!var3.Has(IsoObjectType.wall)) {
                              return false;
                           }

                           if (!var3.Is(IsoFlagType.collideW)) {
                              return false;
                           }

                           if (var3.Is(IsoFlagType.HoppableW)) {
                              return false;
                           }

                           if (var4 != null && var4.Is(IsoFlagType.collideW)) {
                              return false;
                           }
                           break;
                        default:
                           return false;
                        }

                        return IsoWindow.canClimbThroughHelper(this, this.current, var3, var1 == IsoDirections.N || var1 == IsoDirections.S);
                     }
                  }
               } else {
                  return false;
               }
            }
         }
      } else {
         return false;
      }
   }

   public boolean climbOverWall(IsoDirections var1) {
      if (!this.canClimbOverWall(var1)) {
         return false;
      } else {
         this.dropHeavyItems();
         ClimbOverWallState.instance().setParams(this, var1);
         this.actionContext.reportEvent("EventClimbWall");
         return true;
      }
   }

   private void updateSleepingPillsTaken() {
      if (this.getSleepingPillsTaken() > 0 && this.lastPillsTaken > 0L && GameTime.instance.Calender.getTimeInMillis() - this.lastPillsTaken > 7200000L) {
         this.setSleepingPillsTaken(this.getSleepingPillsTaken() - 1);
      }

   }

   public boolean AttemptAttack() {
      return this.DoAttack(this.useChargeTime);
   }

   public boolean DoAttack(float var1) {
      return this.DoAttack(var1, false, (String)null);
   }

   public boolean DoAttack(float var1, boolean var2, String var3) {
      if (!this.authorizeMeleeAction) {
         return false;
      } else {
         this.setForceShove(var2);
         this.setClickSound(var3);
         this.pressedAttack(true);
         return false;
      }
   }

   public int getPlayerNum() {
      return this.PlayerIndex;
   }

   public void updateLOS() {
      this.spottedList.clear();
      this.stats.NumVisibleZombies = 0;
      this.stats.LastNumChasingZombies = this.stats.NumChasingZombies;
      this.stats.NumChasingZombies = 0;
      this.stats.MusicZombiesTargeting = 0;
      this.stats.MusicZombiesVisible = 0;
      this.NumSurvivorsInVicinity = 0;
      if (this.getCurrentSquare() != null) {
         boolean var1 = GameServer.bServer;
         boolean var2 = GameClient.bClient;
         int var3 = this.PlayerIndex;
         IsoPlayer var4 = getInstance();
         float var5 = this.getX();
         float var6 = this.getY();
         float var7 = this.getZ();
         int var8 = 0;
         int var9 = 0;
         int var10 = this.getCell().getObjectList().size();

         for(int var11 = 0; var11 < var10; ++var11) {
            IsoMovingObject var12 = (IsoMovingObject)this.getCell().getObjectList().get(var11);
            if (!(var12 instanceof IsoPhysicsObject) && !(var12 instanceof BaseVehicle)) {
               if (var12 == this) {
                  this.spottedList.add(var12);
               } else {
                  float var13 = var12.getX();
                  float var14 = var12.getY();
                  float var15 = var12.getZ();
                  float var16 = IsoUtils.DistanceTo(var13, var14, var5, var6);
                  if (var16 < 20.0F) {
                     ++var8;
                  }

                  IsoGridSquare var17 = var12.getCurrentSquare();
                  if (var17 != null) {
                     if (this.isSeeEveryone()) {
                        var12.setAlphaAndTarget(var3, 1.0F);
                     } else {
                        IsoGameCharacter var18 = (IsoGameCharacter)Type.tryCastTo(var12, IsoGameCharacter.class);
                        IsoPlayer var19 = (IsoPlayer)Type.tryCastTo(var18, IsoPlayer.class);
                        IsoZombie var20 = (IsoZombie)Type.tryCastTo(var18, IsoZombie.class);
                        if (var4 != null && var12 != var4 && var18 != null && var18.isInvisible() && var4.accessLevel.isEmpty()) {
                           var18.setAlphaAndTarget(var3, 0.0F);
                        } else {
                           float var21 = this.getSeeNearbyCharacterDistance();
                           boolean var22;
                           if (var1) {
                              var22 = ServerLOS.instance.isCouldSee(this, var17);
                           } else {
                              var22 = var17.isCouldSee(var3);
                           }

                           boolean var23;
                           if (var2 && var19 != null) {
                              var23 = true;
                           } else if (!var1) {
                              var23 = var17.isCanSee(var3);
                           } else {
                              var23 = var22;
                           }

                           if (!this.isAsleep() && (var23 || var16 < var21 && var22)) {
                              this.TestZombieSpotPlayer(var12);
                              if (var18 != null && var18.IsVisibleToPlayer[var3]) {
                                 if (var18 instanceof IsoSurvivor) {
                                    ++this.NumSurvivorsInVicinity;
                                 }

                                 if (var20 != null) {
                                    this.lastSeenZombieTime = 0.0D;
                                    if (var15 >= var7 - 1.0F && var16 < 7.0F && !var20.Ghost && !var20.isFakeDead() && var17.getRoom() == this.getCurrentSquare().getRoom()) {
                                       this.TicksSinceSeenZombie = 0;
                                       ++this.stats.NumVisibleZombies;
                                    }

                                    if (var16 < 3.0F) {
                                       ++var9;
                                    }

                                    if (!var20.isSceneCulled()) {
                                       ++this.stats.MusicZombiesVisible;
                                       if (var20.target == this) {
                                          ++this.stats.MusicZombiesTargeting;
                                       }
                                    }
                                 }

                                 this.spottedList.add(var18);
                                 if (!(var19 instanceof IsoPlayer) && !this.bRemote) {
                                    if (var19 != null && var19 != var4) {
                                       var19.setTargetAlpha(var3, 1.0F);
                                    } else {
                                       var18.setTargetAlpha(var3, 1.0F);
                                    }
                                 }

                                 float var24 = 4.0F;
                                 if (this.stats.NumVisibleZombies > 4) {
                                    var24 = 7.0F;
                                 }

                                 if (var16 < var24 && var18 instanceof IsoZombie && (int)var15 == (int)var7 && !this.isGhostMode() && !var2) {
                                    GameTime.instance.setMultiplier(1.0F);
                                    if (!var1) {
                                       UIManager.getSpeedControls().SetCurrentGameSpeed(1);
                                    }
                                 }

                                 if (var16 < var24 && var18 instanceof IsoZombie && (int)var15 == (int)var7 && !this.LastSpotted.contains(var18)) {
                                    Stats var10000 = this.stats;
                                    var10000.NumVisibleZombies += 2;
                                 }
                              }
                           } else {
                              if (var12 != instance) {
                                 var12.setTargetAlpha(var3, 0.0F);
                              }

                              if (var22) {
                                 this.TestZombieSpotPlayer(var12);
                              }
                           }

                           if (var16 < 2.0F && var12.getTargetAlpha(var3) == 1.0F && !this.bRemote) {
                              var12.setAlpha(var3, 1.0F);
                           }
                        }
                     }
                  }
               }
            }
         }

         if (this.isAlive() && var9 > 0 && this.stats.LastVeryCloseZombies == 0 && this.stats.NumVisibleZombies > 0 && this.stats.LastNumVisibleZombies == 0 && this.timeSinceLastStab >= 600.0F) {
            this.timeSinceLastStab = 0.0F;
            this.getEmitter().playSoundImpl("ZombieSurprisedPlayer", (IsoObject)null);
         }

         if (this.stats.NumVisibleZombies > 0) {
            this.timeSinceLastStab = 0.0F;
         }

         if (this.timeSinceLastStab < 600.0F) {
            this.timeSinceLastStab += GameTime.getInstance().getMultiplier() / 1.6F;
         }

         float var25 = (float)var8 / 20.0F;
         if (var25 > 1.0F) {
            var25 = 1.0F;
         }

         var25 *= 0.6F;
         SoundManager.instance.BlendVolume(MainScreenState.ambient, var25);
         int var26 = 0;

         for(int var27 = 0; var27 < this.spottedList.size(); ++var27) {
            if (!this.LastSpotted.contains(this.spottedList.get(var27))) {
               this.LastSpotted.add((IsoMovingObject)this.spottedList.get(var27));
            }

            if (this.spottedList.get(var27) instanceof IsoZombie) {
               ++var26;
            }
         }

         if (this.ClearSpottedTimer <= 0 && var26 == 0) {
            this.LastSpotted.clear();
            this.ClearSpottedTimer = 1000;
         } else {
            --this.ClearSpottedTimer;
         }

         this.stats.LastNumVisibleZombies = this.stats.NumVisibleZombies;
         this.stats.LastVeryCloseZombies = var9;
      }
   }

   public float getSeeNearbyCharacterDistance() {
      return 3.5F - this.stats.getFatigue();
   }

   private boolean checkSpottedPLayerTimer(IsoPlayer var1) {
      if (!var1.spottedByPlayer) {
         return false;
      } else {
         if (this.spottedPlayerTimer.containsKey(var1.getRemoteID())) {
            this.spottedPlayerTimer.put(var1.getRemoteID(), (Integer)this.spottedPlayerTimer.get(var1.getRemoteID()) + 1);
         } else {
            this.spottedPlayerTimer.put(var1.getRemoteID(), 1);
         }

         if ((Integer)this.spottedPlayerTimer.get(var1.getRemoteID()) > 100) {
            var1.spottedByPlayer = false;
            var1.doRenderShadow = false;
            return false;
         } else {
            return true;
         }
      }
   }

   public boolean checkCanSeeClient(IsoPlayer var1) {
      var1.doRenderShadow = true;
      Vector2 var2 = tempVector2_1.set(this.getX(), this.getY());
      Vector2 var3 = tempVector2_2.set(var1.getX(), var1.getY());
      var3.x -= var2.x;
      var3.y -= var2.y;
      Vector2 var4 = this.getForwardDirection();
      var3.normalize();
      var4.normalize();
      var4.normalize();
      float var5 = var3.dot(var4);
      if (GameClient.bClient && var1 != this && this.isLocalPlayer()) {
         if (!this.getAccessLevel().equals("None") && this.canSeeAll) {
            var1.spottedByPlayer = true;
            return true;
         } else {
            float var6 = var1.getCurrentSquare().DistTo(this.getCurrentSquare());
            if (var6 <= 2.0F) {
               var1.spottedByPlayer = true;
               return true;
            } else if (ServerOptions.getInstance().HidePlayersBehindYou.getValue() && (double)var5 < -0.5D) {
               return this.checkSpottedPLayerTimer(var1);
            } else if (var1.isGhostMode() && this.getAccessLevel().equals("None")) {
               var1.doRenderShadow = false;
               var1.spottedByPlayer = false;
               return false;
            } else {
               IsoGridSquare.ILighting var7 = var1.getCurrentSquare().lighting[this.getPlayerNum()];
               if (!var7.bCouldSee()) {
                  return this.checkSpottedPLayerTimer(var1);
               } else if (var1.isSneaking() && !var1.isSprinting()) {
                  if (var6 > 30.0F) {
                     var1.spottedByPlayer = false;
                  }

                  if (var1.spottedByPlayer) {
                     return true;
                  } else {
                     var1.doRenderShadow = true;
                     float var8 = (float)(Math.pow((double)Math.max(40.0F - var6, 0.0F), 3.0D) / 12000.0D);
                     float var9 = (float)(1.0D - (double)((float)var1.remoteSneakLvl / 10.0F) * 0.9D + 0.3D);
                     float var10 = 1.0F;
                     if ((double)var5 < 0.8D) {
                        var10 = 0.3F;
                     }

                     if ((double)var5 < 0.6D) {
                        var10 = 0.05F;
                     }

                     float var11 = (var7.lightInfo().getR() + var7.lightInfo().getG() + var7.lightInfo().getB()) / 3.0F;
                     float var12 = (float)((1.0D - (double)((float)this.getMoodles().getMoodleLevel(MoodleType.Tired) / 5.0F)) * 0.7D + 0.3D);
                     float var13 = 0.1F;
                     if (var1.isPlayerMoving()) {
                        var13 = 0.35F;
                     }

                     if (var1.isRunning()) {
                        var13 = 1.0F;
                     }

                     ArrayList var14 = PolygonalMap2.instance.getPointInLine(var1.getX(), var1.getY(), this.getX(), this.getY(), (int)this.getZ());
                     IsoGridSquare var15 = null;
                     float var16 = 0.0F;
                     float var17 = 0.0F;
                     boolean var18 = false;

                     float var21;
                     for(int var19 = 0; var19 < var14.size(); ++var19) {
                        PolygonalMap2.Point var20 = (PolygonalMap2.Point)var14.get(var19);
                        var15 = IsoCell.getInstance().getGridSquare((double)var20.x, (double)var20.y, (double)this.getZ());
                        var21 = var15.getGridSneakModifier(false);
                        if (var21 > 1.0F) {
                           var18 = true;
                           break;
                        }

                        for(int var22 = 0; var22 < var15.getObjects().size(); ++var22) {
                           IsoObject var23 = (IsoObject)var15.getObjects().get(var22);
                           if (var23.getSprite().getProperties().Is(IsoFlagType.solidtrans) || var23.getSprite().getProperties().Is(IsoFlagType.solid) || var23.getSprite().getProperties().Is(IsoFlagType.windowW) || var23.getSprite().getProperties().Is(IsoFlagType.windowN)) {
                              var18 = true;
                              break;
                           }
                        }

                        if (var18) {
                           break;
                        }
                     }

                     if (var18) {
                        var16 = var15.DistTo(var1.getCurrentSquare());
                        var17 = var15.DistTo(this.getCurrentSquare());
                     }

                     float var24 = var17 < 2.0F ? 5.0F : Math.min(var16, 5.0F);
                     var24 = Math.max(0.0F, var24 - 1.0F);
                     var24 = (float)((double)var24 / 5.0D * 0.9D + 0.1D);
                     float var25 = Math.max(0.1F, 1.0F - ClimateManager.getInstance().getFogIntensity());
                     var21 = var10 * var8 * var11 * var9 * var12 * var13 * var24 * var25;
                     if (var21 >= 1.0F) {
                        var1.spottedByPlayer = true;
                        return true;
                     } else {
                        float var26 = var21 * 1.0F;
                        var21 = (float)(1.0D - Math.pow((double)(1.0F - var21), (double)GameTime.getInstance().getMultiplier()));
                        var21 *= 0.5F;
                        boolean var27 = Rand.Next(0.0F, 1.0F) < var21;
                        var1.spottedByPlayer = var27;
                        if (!var27) {
                           var1.doRenderShadow = false;
                        }

                        return var27;
                     }
                  }
               } else {
                  var1.spottedByPlayer = true;
                  return true;
               }
            }
         }
      } else {
         return true;
      }
   }

   public String getTimeSurvived() {
      String var1 = "";
      int var2 = (int)this.getHoursSurvived();
      int var4 = var2 / 24;
      int var3 = var2 % 24;
      int var5 = var4 / 30;
      var4 %= 30;
      int var6 = var5 / 12;
      var5 %= 12;
      String var7 = Translator.getText("IGUI_Gametime_day");
      String var8 = Translator.getText("IGUI_Gametime_year");
      String var9 = Translator.getText("IGUI_Gametime_hour");
      String var10 = Translator.getText("IGUI_Gametime_month");
      if (var6 != 0) {
         if (var6 > 1) {
            var8 = Translator.getText("IGUI_Gametime_years");
         }

         if (var1.length() > 0) {
            var1 = var1 + ", ";
         }

         var1 = var1 + var6 + " " + var8;
      }

      if (var5 != 0) {
         if (var5 > 1) {
            var10 = Translator.getText("IGUI_Gametime_months");
         }

         if (var1.length() > 0) {
            var1 = var1 + ", ";
         }

         var1 = var1 + var5 + " " + var10;
      }

      if (var4 != 0) {
         if (var4 > 1) {
            var7 = Translator.getText("IGUI_Gametime_days");
         }

         if (var1.length() > 0) {
            var1 = var1 + ", ";
         }

         var1 = var1 + var4 + " " + var7;
      }

      if (var3 != 0) {
         if (var3 > 1) {
            var9 = Translator.getText("IGUI_Gametime_hours");
         }

         if (var1.length() > 0) {
            var1 = var1 + ", ";
         }

         var1 = var1 + var3 + " " + var9;
      }

      if (var1.isEmpty()) {
         int var11 = (int)(this.HoursSurvived * 60.0D);
         var1 = var11 + " " + Translator.getText("IGUI_Gametime_minutes");
      }

      return var1;
   }

   public boolean IsUsingAimWeapon() {
      if (this.leftHandItem == null) {
         return false;
      } else if (!(this.leftHandItem instanceof HandWeapon)) {
         return false;
      } else {
         return !this.isAiming() ? false : ((HandWeapon)this.leftHandItem).bIsAimedFirearm;
      }
   }

   private boolean IsUsingAimHandWeapon() {
      if (this.leftHandItem == null) {
         return false;
      } else if (!(this.leftHandItem instanceof HandWeapon)) {
         return false;
      } else {
         return !this.isAiming() ? false : ((HandWeapon)this.leftHandItem).bIsAimedHandWeapon;
      }
   }

   private boolean DoAimAnimOnAiming() {
      return this.IsUsingAimWeapon();
   }

   public int getSleepingPillsTaken() {
      return this.sleepingPillsTaken;
   }

   public void setSleepingPillsTaken(int var1) {
      this.sleepingPillsTaken = var1;
      if (this.getStats().Drunkenness > 10.0F) {
         ++this.sleepingPillsTaken;
      }

      this.lastPillsTaken = GameTime.instance.Calender.getTimeInMillis();
   }

   public boolean isOutside() {
      return this.getCurrentSquare() != null && this.getCurrentSquare().getRoom() == null && !this.isInARoom();
   }

   public double getLastSeenZomboidTime() {
      return this.lastSeenZombieTime;
   }

   public float getPlayerClothingTemperature() {
      float var1 = 0.0F;
      if (this.getClothingItem_Feet() != null) {
         var1 += ((Clothing)this.getClothingItem_Feet()).getTemperature();
      }

      if (this.getClothingItem_Hands() != null) {
         var1 += ((Clothing)this.getClothingItem_Hands()).getTemperature();
      }

      if (this.getClothingItem_Head() != null) {
         var1 += ((Clothing)this.getClothingItem_Head()).getTemperature();
      }

      if (this.getClothingItem_Legs() != null) {
         var1 += ((Clothing)this.getClothingItem_Legs()).getTemperature();
      }

      if (this.getClothingItem_Torso() != null) {
         var1 += ((Clothing)this.getClothingItem_Torso()).getTemperature();
      }

      return var1;
   }

   public float getPlayerClothingInsulation() {
      float var1 = 0.0F;
      if (this.getClothingItem_Feet() != null) {
         var1 += ((Clothing)this.getClothingItem_Feet()).getInsulation() * 0.1F;
      }

      if (this.getClothingItem_Hands() != null) {
         var1 += ((Clothing)this.getClothingItem_Hands()).getInsulation() * 0.0F;
      }

      if (this.getClothingItem_Head() != null) {
         var1 += ((Clothing)this.getClothingItem_Head()).getInsulation() * 0.0F;
      }

      if (this.getClothingItem_Legs() != null) {
         var1 += ((Clothing)this.getClothingItem_Legs()).getInsulation() * 0.3F;
      }

      if (this.getClothingItem_Torso() != null) {
         var1 += ((Clothing)this.getClothingItem_Torso()).getInsulation() * 0.6F;
      }

      return var1;
   }

   public InventoryItem getActiveLightItem() {
      if (this.rightHandItem != null && this.rightHandItem.isEmittingLight()) {
         return this.rightHandItem;
      } else if (this.leftHandItem != null && this.leftHandItem.isEmittingLight()) {
         return this.leftHandItem;
      } else {
         AttachedItems var1 = this.getAttachedItems();

         for(int var2 = 0; var2 < var1.size(); ++var2) {
            InventoryItem var3 = var1.getItemByIndex(var2);
            if (var3.isEmittingLight()) {
               return var3;
            }
         }

         return null;
      }
   }

   public boolean isTorchCone() {
      if (this.bRemote) {
         return this.mpTorchCone;
      } else {
         InventoryItem var1 = this.getActiveLightItem();
         return var1 != null && var1.isTorchCone();
      }
   }

   public float getTorchDot() {
      if (this.bRemote) {
      }

      InventoryItem var1 = this.getActiveLightItem();
      return var1 != null ? var1.getTorchDot() : 0.0F;
   }

   public float getLightDistance() {
      if (this.bRemote) {
         return this.mpTorchDist;
      } else {
         InventoryItem var1 = this.getActiveLightItem();
         return var1 != null ? (float)var1.getLightDistance() : 0.0F;
      }
   }

   public boolean pressedMovement(boolean var1) {
      if (this.isNPC) {
         return false;
      } else if (GameClient.bClient && !this.isLocal()) {
         return this.networkAI.isPressedMovement();
      } else {
         this.setVariable("pressedRunButton", GameKeyboard.isKeyDown(Core.getInstance().getKey("Run")));
         if (!var1 && (this.isBlockMovement() || this.isIgnoreInputsForDirection())) {
            if (GameClient.bClient && this.isLocal()) {
               this.networkAI.setPressedMovement(false);
            }

            return false;
         } else if (this.PlayerIndex != 0 || !GameKeyboard.isKeyDown(Core.getInstance().getKey("Left")) && !GameKeyboard.isKeyDown(Core.getInstance().getKey("Right")) && !GameKeyboard.isKeyDown(Core.getInstance().getKey("Forward")) && !GameKeyboard.isKeyDown(Core.getInstance().getKey("Backward"))) {
            if (this.JoypadBind != -1) {
               float var2 = JoypadManager.instance.getMovementAxisY(this.JoypadBind);
               float var3 = JoypadManager.instance.getMovementAxisX(this.JoypadBind);
               float var4 = JoypadManager.instance.getDeadZone(this.JoypadBind, 0);
               if (Math.abs(var2) > var4 || Math.abs(var3) > var4) {
                  if (GameClient.bClient && this.isLocal()) {
                     this.networkAI.setPressedMovement(true);
                  }

                  return true;
               }
            }

            if (GameClient.bClient && this.isLocal()) {
               this.networkAI.setPressedMovement(false);
            }

            return false;
         } else {
            if (GameClient.bClient && this.isLocal()) {
               this.networkAI.setPressedMovement(true);
            }

            return true;
         }
      }
   }

   public boolean pressedCancelAction() {
      if (this.isNPC) {
         return false;
      } else if (GameClient.bClient && !this.isLocal()) {
         return this.networkAI.isPressedCancelAction();
      } else if (this.PlayerIndex == 0 && GameKeyboard.isKeyDown(Core.getInstance().getKey("CancelAction"))) {
         if (GameClient.bClient && this.isLocal()) {
            this.networkAI.setPressedCancelAction(true);
         }

         return true;
      } else if (this.JoypadBind != -1) {
         boolean var1 = JoypadManager.instance.isBButtonStartPress(this.JoypadBind);
         if (GameClient.bClient && this.isLocal()) {
            this.networkAI.setPressedCancelAction(var1);
         }

         return var1;
      } else {
         if (GameClient.bClient && this.isLocal()) {
            this.networkAI.setPressedCancelAction(false);
         }

         return false;
      }
   }

   public boolean pressedAim() {
      if (this.isNPC) {
         return false;
      } else {
         if (this.PlayerIndex == 0) {
            if (this.isAimKeyDown()) {
               return true;
            }

            if (Mouse.isButtonDownUICheck(1)) {
               return true;
            }
         }

         if (this.JoypadBind == -1) {
            return false;
         } else {
            float var1 = JoypadManager.instance.getAimingAxisY(this.JoypadBind);
            float var2 = JoypadManager.instance.getAimingAxisX(this.JoypadBind);
            return Math.abs(var1) > 0.1F || Math.abs(var2) > 0.1F;
         }
      }
   }

   public boolean isDoingActionThatCanBeCancelled() {
      if (this.isDead()) {
         return false;
      } else if (!this.getCharacterActions().isEmpty()) {
         return true;
      } else {
         State var1 = this.getCurrentState();
         if (var1 != null && var1.isDoingActionThatCanBeCancelled()) {
            return true;
         } else {
            for(int var2 = 0; var2 < this.stateMachine.getSubStateCount(); ++var2) {
               var1 = this.stateMachine.getSubStateAt(var2);
               if (var1 != null && var1.isDoingActionThatCanBeCancelled()) {
                  return true;
               }
            }

            return false;
         }
      }
   }

   public long getSteamID() {
      return this.steamID;
   }

   public void setSteamID(long var1) {
      this.steamID = var1;
   }

   public boolean isTargetedByZombie() {
      return this.targetedByZombie;
   }

   public boolean isMaskClicked(int var1, int var2, boolean var3) {
      return this.sprite == null ? false : this.sprite.isMaskClicked(this.dir, var1, var2, var3);
   }

   public int getOffSetXUI() {
      return this.offSetXUI;
   }

   public void setOffSetXUI(int var1) {
      this.offSetXUI = var1;
   }

   public int getOffSetYUI() {
      return this.offSetYUI;
   }

   public void setOffSetYUI(int var1) {
      this.offSetYUI = var1;
   }

   public String getUsername() {
      return this.getUsername(false);
   }

   public String getUsername(Boolean var1) {
      String var2 = this.username;
      if (var1 && GameClient.bClient && ServerOptions.instance.ShowFirstAndLastName.getValue() && "None".equals(this.getAccessLevel())) {
         String var10000 = this.getDescriptor().getForename();
         var2 = var10000 + " " + this.getDescriptor().getSurname();
         var2 = var2 + " (" + this.username + ")";
      }

      return var2;
   }

   public void setUsername(String var1) {
      this.username = var1;
   }

   public void updateUsername() {
      if (!GameClient.bClient && !GameServer.bServer) {
         String var10001 = this.getDescriptor().getForename();
         this.username = var10001 + this.getDescriptor().getSurname();
      }
   }

   public short getOnlineID() {
      return this.OnlineID;
   }

   public boolean isLocalPlayer() {
      if (GameServer.bServer) {
         return false;
      } else {
         for(int var1 = 0; var1 < numPlayers; ++var1) {
            if (players[var1] == this) {
               return true;
            }
         }

         return false;
      }
   }

   public static void setLocalPlayer(int var0, IsoPlayer var1) {
      players[var0] = var1;
   }

   public boolean isOnlyPlayerAsleep() {
      if (!this.isAsleep()) {
         return false;
      } else {
         for(int var1 = 0; var1 < numPlayers; ++var1) {
            if (players[var1] != null && !players[var1].isDead() && players[var1] != this && players[var1].isAsleep()) {
               return false;
            }
         }

         return true;
      }
   }

   public void OnDeath() {
      super.OnDeath();
      this.advancedAnimator.SetState("death");
      if (!GameServer.bServer) {
         this.StopAllActionQueue();
         if (!GameClient.bClient) {
            this.dropHandItems();
         }

         if (allPlayersDead()) {
            SoundManager.instance.playMusic(DEATH_MUSIC_NAME);
         }

         if (this.isLocalPlayer()) {
            LuaEventManager.triggerEvent("OnPlayerDeath", this);
         }

         if (this.isLocalPlayer() && this.getVehicle() != null) {
            this.getVehicle().exit(this);
         }

         this.removeSaveFile();
         if (this.shouldBecomeZombieAfterDeath()) {
            this.forceAwake();
         }

         this.getMoodles().Update();
         this.getCell().setDrag((KahluaTable)null, this.getPlayerNum());
      }
   }

   public boolean isNoClip() {
      return this.noClip;
   }

   public void setNoClip(boolean var1) {
      this.noClip = var1;
   }

   public void setAuthorizeMeleeAction(boolean var1) {
      this.authorizeMeleeAction = var1;
   }

   public boolean isAuthorizeMeleeAction() {
      return this.authorizeMeleeAction;
   }

   public void setAuthorizeShoveStomp(boolean var1) {
      this.authorizeShoveStomp = var1;
   }

   public boolean isAuthorizeShoveStomp() {
      return this.authorizeShoveStomp;
   }

   public boolean isBlockMovement() {
      return this.blockMovement;
   }

   public void setBlockMovement(boolean var1) {
      this.blockMovement = var1;
   }

   public void startReceivingBodyDamageUpdates(IsoPlayer var1) {
      if (GameClient.bClient && var1 != null && var1 != this && this.isLocalPlayer() && !var1.isLocalPlayer()) {
         var1.resetBodyDamageRemote();
         BodyDamageSync.instance.startReceivingUpdates(var1.getOnlineID());
      }

   }

   public void stopReceivingBodyDamageUpdates(IsoPlayer var1) {
      if (GameClient.bClient && var1 != null && var1 != this && !var1.isLocalPlayer()) {
         BodyDamageSync.instance.stopReceivingUpdates(var1.getOnlineID());
      }

   }

   public Nutrition getNutrition() {
      return this.nutrition;
   }

   public Fitness getFitness() {
      return this.fitness;
   }

   private boolean updateRemotePlayer() {
      if (!this.bRemote) {
         return false;
      } else {
         if (GameServer.bServer) {
            ServerLOS.instance.doServerZombieLOS(this);
            ServerLOS.instance.updateLOS(this);
            if (this.isDead()) {
               return true;
            }

            this.removeFromSquare();
            this.setX(this.realx);
            this.setY(this.realy);
            this.setZ((float)this.realz);
            this.setLx(this.realx);
            this.setLy(this.realy);
            this.setLz((float)this.realz);
            this.ensureOnTile();
            if (this.slowTimer > 0.0F) {
               this.slowTimer -= GameTime.instance.getRealworldSecondsSinceLastUpdate();
               this.slowFactor -= GameTime.instance.getMultiplier() / 100.0F;
               if (this.slowFactor < 0.0F) {
                  this.slowFactor = 0.0F;
               }
            } else {
               this.slowFactor = 0.0F;
            }
         }

         if (GameClient.bClient) {
            if (this.isCurrentState(BumpedState.instance())) {
               return true;
            }

            float var1;
            float var2;
            float var3;
            if (!this.networkAI.isCollisionEnabled() && !this.networkAI.isNoCollisionTimeout()) {
               this.setCollidable(false);
               var1 = this.realx;
               var2 = this.realy;
               var3 = (float)this.realz;
            } else {
               this.setCollidable(true);
               var1 = this.networkAI.targetX;
               var2 = this.networkAI.targetY;
               var3 = (float)this.networkAI.targetZ;
            }

            this.updateMovementRates();
            PathFindBehavior2 var4 = this.getPathFindBehavior2();
            boolean var5 = false;
            if (!this.networkAI.events.isEmpty()) {
               Iterator var6 = this.networkAI.events.iterator();

               while(var6.hasNext()) {
                  EventPacket var7 = (EventPacket)var6.next();
                  if (var7.process(this)) {
                     this.m_isPlayerMoving = this.networkAI.moving = false;
                     this.setJustMoved(false);
                     if (this.networkAI.usePathFind) {
                        var4.reset();
                        this.setPath2((PolygonalMap2.Path)null);
                        this.networkAI.usePathFind = false;
                     }

                     if (Core.bDebug) {
                        DebugLog.log(DebugType.Multiplayer, String.format("Event processed (%d) : %s", this.networkAI.events.size(), var7.getDescription()));
                     }

                     var6.remove();
                     return true;
                  }

                  if (!var7.isMovableEvent()) {
                     tempo.set(var7.x - this.x, var7.y - this.y);
                     var1 = var7.x;
                     var2 = var7.y;
                     var3 = var7.z;
                     var5 = true;
                  }

                  if (var7.isTimeout()) {
                     this.m_isPlayerMoving = this.networkAI.moving = false;
                     this.setJustMoved(false);
                     if (this.networkAI.usePathFind) {
                        var4.reset();
                        this.setPath2((PolygonalMap2.Path)null);
                        this.networkAI.usePathFind = false;
                     }

                     if (Core.bDebug) {
                        DebugLog.log(DebugType.Multiplayer, String.format("Event timeout (%d) : %s", this.networkAI.events.size(), var7.getDescription()));
                     }

                     var6.remove();
                     return true;
                  }
               }
            }

            if (!var5 && this.networkAI.collidePointX > -1.0F && this.networkAI.collidePointY > -1.0F && ((int)this.x != (int)this.networkAI.collidePointX || (int)this.y != (int)this.networkAI.collidePointY)) {
               var1 = this.networkAI.collidePointX;
               var2 = this.networkAI.collidePointY;
               DebugLog.log(DebugType.ActionSystem, "Player " + this.username + ": collide point (" + var1 + ", " + var2 + ") has not been reached, so move to it");
            }

            if (DebugOptions.instance.MultiplayerShowPlayerPrediction.getValue()) {
               this.networkAI.targetX = var1;
               this.networkAI.targetY = var2;
            }

            if (!this.networkAI.forcePathFinder && this.isCollidedThisFrame() && IsoUtils.DistanceManhatten(var1, var2, this.x, this.y) > 3.0F) {
               this.networkAI.forcePathFinder = true;
            }

            if (this.networkAI.forcePathFinder && !PolygonalMap2.instance.lineClearCollide(this.x, this.y, var1, var2, (int)this.z, this.vehicle, false, true) && IsoUtils.DistanceManhatten(var1, var2, this.x, this.y) < 2.0F || this.getCurrentState() == ClimbOverFenceState.instance() || this.getCurrentState() == ClimbThroughWindowState.instance() || this.getCurrentState() == ClimbOverWallState.instance()) {
               this.networkAI.forcePathFinder = false;
            }

            float var11;
            if (!this.networkAI.needToMovingUsingPathFinder && !this.networkAI.forcePathFinder) {
               if (this.networkAI.usePathFind) {
                  var4.reset();
                  this.setPath2((PolygonalMap2.Path)null);
                  this.networkAI.usePathFind = false;
               }

               var4.walkingOnTheSpot.reset(this.x, this.y);
               this.getDeferredMovement(tempVector2_2);
               if (this.getCurrentState() != ClimbOverWallState.instance() && this.getCurrentState() != ClimbOverFenceState.instance()) {
                  var11 = IsoUtils.DistanceTo(this.x, this.y, this.networkAI.targetX, this.networkAI.targetY) / IsoUtils.DistanceTo(this.realx, this.realy, this.networkAI.targetX, this.networkAI.targetY);
                  float var13 = 0.8F + 0.4F * IsoUtils.smoothstep(0.8F, 1.2F, var11);
                  var4.moveToPoint(var1, var2, var13);
               } else {
                  this.MoveUnmodded(tempVector2_2);
               }

               this.m_isPlayerMoving = !var5 && IsoUtils.DistanceManhatten(var1, var2, this.x, this.y) > 0.2F || (int)var1 != (int)this.x || (int)var2 != (int)this.y || (int)this.z != (int)var3;
               if (!this.m_isPlayerMoving) {
                  this.DirectionFromVector(this.networkAI.direction);
                  this.setForwardDirection(this.networkAI.direction);
                  this.networkAI.forcePathFinder = false;
                  if (this.networkAI.usePathFind) {
                     var4.reset();
                     this.setPath2((PolygonalMap2.Path)null);
                     this.networkAI.usePathFind = false;
                  }
               }

               this.setJustMoved(this.m_isPlayerMoving);
               this.m_deltaX = 0.0F;
               this.m_deltaY = 0.0F;
            } else {
               if (!this.networkAI.usePathFind || var1 != var4.getTargetX() || var2 != var4.getTargetY()) {
                  var4.pathToLocationF(var1, var2, var3);
                  var4.walkingOnTheSpot.reset(this.x, this.y);
                  this.networkAI.usePathFind = true;
               }

               PathFindBehavior2.BehaviorResult var10 = var4.update();
               if (var10 == PathFindBehavior2.BehaviorResult.Failed) {
                  this.setPathFindIndex(-1);
                  if (this.networkAI.forcePathFinder) {
                     this.networkAI.forcePathFinder = false;
                  } else if (NetworkTeleport.teleport(this, NetworkTeleport.Type.teleportation, var1, var2, (byte)((int)var3), 1.0F)) {
                     DebugLog.Multiplayer.warn(String.format("Player %d teleport from (%.2f, %.2f, %.2f) to (%.2f, %.2f %.2f)", this.getOnlineID(), this.x, this.y, this.z, var1, var2, var3));
                  }
               } else if (var10 == PathFindBehavior2.BehaviorResult.Succeeded) {
                  int var12 = (int)var4.getTargetX();
                  int var8 = (int)var4.getTargetY();
                  if (GameServer.bServer) {
                     ServerMap.instance.getChunk(var12 / 10, var8 / 10);
                  } else {
                     IsoWorld.instance.CurrentCell.getChunkForGridSquare(var12, var8, 0);
                  }

                  this.m_isPlayerMoving = true;
                  this.setJustMoved(true);
               }

               this.m_deltaX = 0.0F;
               this.m_deltaY = 0.0F;
            }

            if (!this.m_isPlayerMoving || this.isAiming()) {
               this.DirectionFromVector(this.networkAI.direction);
               this.setForwardDirection(this.networkAI.direction);
               tempo.set(var1 - this.nx, -(var2 - this.ny));
               tempo.normalize();
               var11 = this.legsSprite.modelSlot.model.AnimPlayer.getRenderedAngle();
               if ((double)var11 > 6.283185307179586D) {
                  var11 = (float)((double)var11 - 6.283185307179586D);
               }

               if (var11 < 0.0F) {
                  var11 = (float)((double)var11 + 6.283185307179586D);
               }

               tempo.rotate(var11);
               tempo.setLength(Math.min(IsoUtils.DistanceTo(var1, var2, this.x, this.y), 1.0F));
               this.m_deltaX = tempo.x;
               this.m_deltaY = tempo.y;
            }
         }

         return true;
      }
   }

   private boolean updateWhileDead() {
      if (GameServer.bServer) {
         return false;
      } else if (!this.isLocalPlayer()) {
         return false;
      } else if (!this.isDead()) {
         return false;
      } else {
         this.setVariable("bPathfind", false);
         this.setMoving(false);
         this.m_isPlayerMoving = false;
         if (this.getVehicle() != null) {
            this.getVehicle().exit(this);
         }

         if (this.heartEventInstance != 0L) {
            this.getEmitter().stopSound(this.heartEventInstance);
            this.heartEventInstance = 0L;
         }

         if (GameClient.bClient && !this.bRemote && !this.bSentDeath) {
            this.dropHandItems();
            if (DebugOptions.instance.MultiplayerPlayerZombie.getValue()) {
               this.getBodyDamage().setInfectionLevel(100.0F);
            }

            GameClient.instance.sendPlayerDeath(this);
            ClientPlayerDB.getInstance().clientSendNetworkPlayerInt(this);
            this.bSentDeath = true;
         }

         return true;
      }
   }

   private void initFMODParameters() {
      FMODParameterList var1 = this.getFMODParameters();
      var1.add(this.parameterCharacterMovementSpeed);
      var1.add(this.parameterFootstepMaterial);
      var1.add(this.parameterFootstepMaterial2);
      var1.add(this.parameterLocalPlayer);
      var1.add(this.parameterMeleeHitSurface);
      var1.add(this.parameterPlayerHealth);
      var1.add(this.parameterShoeType);
      var1.add(this.parameterVehicleHitLocation);
   }

   public ParameterCharacterMovementSpeed getParameterCharacterMovementSpeed() {
      return this.parameterCharacterMovementSpeed;
   }

   public void setMeleeHitSurface(ParameterMeleeHitSurface.Material var1) {
      this.parameterMeleeHitSurface.setMaterial(var1);
   }

   public void setMeleeHitSurface(String var1) {
      try {
         this.parameterMeleeHitSurface.setMaterial(ParameterMeleeHitSurface.Material.valueOf(var1));
      } catch (IllegalArgumentException var3) {
         this.parameterMeleeHitSurface.setMaterial(ParameterMeleeHitSurface.Material.Default);
      }

   }

   public void setVehicleHitLocation(BaseVehicle var1) {
      ParameterVehicleHitLocation.HitLocation var2 = ParameterVehicleHitLocation.calculateLocation(var1, this.getX(), this.getY(), this.getZ());
      this.parameterVehicleHitLocation.setLocation(var2);
   }

   private void updateHeartSound() {
      if (!GameServer.bServer) {
         if (this.isLocalPlayer()) {
            GameSound var1 = GameSounds.getSound("HeartBeat");
            boolean var2 = var1 != null && var1.getUserVolume() > 0.0F && this.stats.Panic > 0.0F;
            if (!this.Asleep && var2 && GameTime.getInstance().getTrueMultiplier() == 1.0F) {
               this.heartDelay -= GameTime.getInstance().getMultiplier() / 1.6F;
               if (this.heartEventInstance == 0L || !this.getEmitter().isPlaying(this.heartEventInstance)) {
                  this.heartEventInstance = this.getEmitter().playSoundImpl("HeartBeat", (IsoObject)null);
                  this.getEmitter().setVolume(this.heartEventInstance, 0.0F);
               }

               if (this.heartDelay <= 0.0F) {
                  this.heartDelayMax = (float)((int)((1.0F - this.stats.Panic / 100.0F * 0.7F) * 25.0F) * 2);
                  this.heartDelay = this.heartDelayMax;
                  if (this.heartEventInstance != 0L) {
                     this.getEmitter().setVolume(this.heartEventInstance, this.stats.Panic / 100.0F);
                  }
               }
            } else if (this.heartEventInstance != 0L) {
               this.getEmitter().setVolume(this.heartEventInstance, 0.0F);
            }

         }
      }
   }

   private void updateWorldAmbiance() {
      if (!GameServer.bServer) {
         if (this.isLocalPlayer()) {
            if (this.getPlayerNum() == 0 && (this.worldAmbianceInstance == 0L || !this.getEmitter().isPlaying(this.worldAmbianceInstance))) {
               this.worldAmbianceInstance = this.getEmitter().playSoundImpl("WorldAmbiance", (IsoObject)null);
               this.getEmitter().setVolume(this.worldAmbianceInstance, 1.0F);
            }

         }
      }
   }

   public void DoFootstepSound(String var1) {
      ParameterCharacterMovementSpeed.MovementType var2 = ParameterCharacterMovementSpeed.MovementType.Walk;
      float var3 = 0.5F;
      byte var5 = -1;
      switch(var1.hashCode()) {
      case -940878112:
         if (var1.equals("sneak_run")) {
            var5 = 1;
         }
         break;
      case -895679974:
         if (var1.equals("sprint")) {
            var5 = 5;
         }
         break;
      case -891993841:
         if (var1.equals("strafe")) {
            var5 = 2;
         }
         break;
      case 113291:
         if (var1.equals("run")) {
            var5 = 4;
         }
         break;
      case 3641801:
         if (var1.equals("walk")) {
            var5 = 3;
         }
         break;
      case 897679380:
         if (var1.equals("sneak_walk")) {
            var5 = 0;
         }
      }

      switch(var5) {
      case 0:
         var3 = 0.25F;
         var2 = ParameterCharacterMovementSpeed.MovementType.SneakWalk;
         break;
      case 1:
         var3 = 0.25F;
         var2 = ParameterCharacterMovementSpeed.MovementType.SneakRun;
         break;
      case 2:
         var3 = 0.5F;
         var2 = ParameterCharacterMovementSpeed.MovementType.Strafe;
         break;
      case 3:
         var3 = 0.5F;
         var2 = ParameterCharacterMovementSpeed.MovementType.Walk;
         break;
      case 4:
         var3 = 0.75F;
         var2 = ParameterCharacterMovementSpeed.MovementType.Run;
         break;
      case 5:
         var3 = 1.0F;
         var2 = ParameterCharacterMovementSpeed.MovementType.Sprint;
      }

      this.parameterCharacterMovementSpeed.setMovementType(var2);
      super.DoFootstepSound(var3);
   }

   private void updateHeavyBreathing() {
   }

   private void checkVehicleContainers() {
      ArrayList var1 = this.vehicleContainerData.tempContainers;
      var1.clear();
      int var2 = (int)this.getX() - 4;
      int var3 = (int)this.getY() - 4;
      int var4 = (int)this.getX() + 4;
      int var5 = (int)this.getY() + 4;
      int var6 = var2 / 10;
      int var7 = var3 / 10;
      int var8 = (int)Math.ceil((double)((float)var4 / 10.0F));
      int var9 = (int)Math.ceil((double)((float)var5 / 10.0F));

      int var10;
      for(var10 = var7; var10 < var9; ++var10) {
         for(int var11 = var6; var11 < var8; ++var11) {
            IsoChunk var12 = GameServer.bServer ? ServerMap.instance.getChunk(var11, var10) : IsoWorld.instance.CurrentCell.getChunkForGridSquare(var11 * 10, var10 * 10, 0);
            if (var12 != null) {
               for(int var13 = 0; var13 < var12.vehicles.size(); ++var13) {
                  BaseVehicle var14 = (BaseVehicle)var12.vehicles.get(var13);
                  VehicleScript var15 = var14.getScript();
                  if (var15 != null) {
                     for(int var16 = 0; var16 < var15.getPartCount(); ++var16) {
                        VehicleScript.Part var17 = var15.getPart(var16);
                        if (var17.container != null && var17.area != null && var14.isInArea(var17.area, this)) {
                           IsoPlayer.VehicleContainer var18 = this.vehicleContainerData.freeContainers.isEmpty() ? new IsoPlayer.VehicleContainer() : (IsoPlayer.VehicleContainer)this.vehicleContainerData.freeContainers.pop();
                           var1.add(var18.set(var14, var16));
                        }
                     }
                  }
               }
            }
         }
      }

      if (var1.size() != this.vehicleContainerData.containers.size()) {
         this.vehicleContainerData.freeContainers.addAll(this.vehicleContainerData.containers);
         this.vehicleContainerData.containers.clear();
         this.vehicleContainerData.containers.addAll(var1);
         LuaEventManager.triggerEvent("OnContainerUpdate");
      } else {
         for(var10 = 0; var10 < var1.size(); ++var10) {
            IsoPlayer.VehicleContainer var19 = (IsoPlayer.VehicleContainer)var1.get(var10);
            IsoPlayer.VehicleContainer var20 = (IsoPlayer.VehicleContainer)this.vehicleContainerData.containers.get(var10);
            if (!var19.equals(var20)) {
               this.vehicleContainerData.freeContainers.addAll(this.vehicleContainerData.containers);
               this.vehicleContainerData.containers.clear();
               this.vehicleContainerData.containers.addAll(var1);
               LuaEventManager.triggerEvent("OnContainerUpdate");
               break;
            }
         }
      }

   }

   public void setJoypadIgnoreAimUntilCentered(boolean var1) {
      this.bJoypadIgnoreAimUntilCentered = var1;
   }

   public boolean canSeePlayerStats() {
      return this.accessLevel != "";
   }

   public ByteBufferWriter createPlayerStats(ByteBufferWriter var1, String var2) {
      PacketTypes.PacketType.ChangePlayerStats.doPacket(var1);
      var1.putShort(this.getOnlineID());
      var1.putUTF(var2);
      var1.putUTF(this.getDisplayName());
      var1.putUTF(this.getDescriptor().getForename());
      var1.putUTF(this.getDescriptor().getSurname());
      var1.putUTF(this.getDescriptor().getProfession());
      var1.putUTF(this.accessLevel);
      if (!StringUtils.isNullOrEmpty(this.getTagPrefix())) {
         var1.putByte((byte)1);
         var1.putUTF(this.getTagPrefix());
      } else {
         var1.putByte((byte)0);
      }

      if (this.accessLevel.equals("")) {
         this.setGhostMode(false);
         this.setInvisible(false);
         this.setGodMod(false);
      }

      var1.putBoolean(this.isAllChatMuted());
      var1.putFloat(this.getTagColor().r);
      var1.putFloat(this.getTagColor().g);
      var1.putFloat(this.getTagColor().b);
      var1.putByte((byte)(this.showTag ? 1 : 0));
      var1.putByte((byte)(this.factionPvp ? 1 : 0));
      return var1;
   }

   public String setPlayerStats(ByteBuffer var1, String var2) {
      String var3 = GameWindow.ReadString(var1);
      String var4 = GameWindow.ReadString(var1);
      String var5 = GameWindow.ReadString(var1);
      String var6 = GameWindow.ReadString(var1);
      String var7 = GameWindow.ReadString(var1);
      String var8 = "";
      if (var1.get() == 1) {
         var8 = GameWindow.ReadString(var1);
      }

      boolean var9 = var1.get() == 1;
      float var10 = var1.getFloat();
      float var11 = var1.getFloat();
      float var12 = var1.getFloat();
      String var13 = "";
      this.setTagColor(new ColorInfo(var10, var11, var12, 1.0F));
      this.setTagPrefix(var8);
      this.showTag = var1.get() == 1;
      this.factionPvp = var1.get() == 1;
      if (!var4.equals(this.getDescriptor().getForename())) {
         if (GameServer.bServer) {
            var13 = var2 + " Changed " + var3 + " forname in " + var4;
         } else {
            var13 = "Changed your forname in " + var4;
         }
      }

      this.getDescriptor().setForename(var4);
      if (!var5.equals(this.getDescriptor().getSurname())) {
         if (GameServer.bServer) {
            var13 = var2 + " Changed " + var3 + " surname in " + var5;
         } else {
            var13 = "Changed your surname in " + var5;
         }
      }

      this.getDescriptor().setSurname(var5);
      if (!var6.equals(this.getDescriptor().getProfession())) {
         if (GameServer.bServer) {
            var13 = var2 + " Changed " + var3 + " profession to " + var6;
         } else {
            var13 = "Changed your profession in " + var6;
         }
      }

      this.getDescriptor().setProfession(var6);
      if (!this.accessLevel.equals(var7)) {
         if (GameServer.bServer) {
            var2.makeConcatWithConstants<invokedynamic>(var2, this.getDisplayName(), var7);

            try {
               ServerWorldDatabase.instance.setAccessLevel(this.username, var7);
            } catch (SQLException var15) {
               var15.printStackTrace();
            }
         } else if (GameClient.bClient && GameClient.username.equals(this.username)) {
            GameClient.accessLevel = var7;
            GameClient.connection.accessLevel = var7;
         }

         if (var7.equals("")) {
            this.setGhostMode(false);
            this.setInvisible(false);
            this.setGodMod(false);
         }

         var13 = "Changed access level to " + var7;
         this.accessLevel = var7;
      }

      if (!this.getDisplayName().equals(var3)) {
         if (GameServer.bServer) {
            var13 = var2 + " Changed display name \"" + this.getDisplayName() + "\" to \"" + var3 + "\"";
            ServerWorldDatabase.instance.updateDisplayName(this.username, var3);
         } else {
            var13 = "Changed your display name to " + var3;
         }

         this.setDisplayName(var3);
      }

      if (var9 != this.isAllChatMuted()) {
         if (var9) {
            if (GameServer.bServer) {
               var13 = var2 + " Banned " + var3 + " from using /all chat";
            } else {
               var13 = "Banned you from using /all chat";
            }
         } else if (GameServer.bServer) {
            var13 = var2 + " Allowed " + var3 + " to use /all chat";
         } else {
            var13 = "Now allowed you to use /all chat";
         }
      }

      this.setAllChatMuted(var9);
      if (GameServer.bServer && !"".equals(var13)) {
         LoggerManager.getLogger("admin").write(var13);
      }

      if (GameClient.bClient) {
         LuaEventManager.triggerEvent("OnMiniScoreboardUpdate");
      }

      return var13;
   }

   public boolean isAllChatMuted() {
      return this.allChatMuted;
   }

   public void setAllChatMuted(boolean var1) {
      this.allChatMuted = var1;
   }

   public String getAccessLevel() {
      String var1 = this.accessLevel;
      byte var2 = -1;
      switch(var1.hashCode()) {
      case -2004703995:
         if (var1.equals("moderator")) {
            var2 = 1;
         }
         break;
      case 3302:
         if (var1.equals("gm")) {
            var2 = 3;
         }
         break;
      case 92668751:
         if (var1.equals("admin")) {
            var2 = 0;
         }
         break;
      case 348607190:
         if (var1.equals("observer")) {
            var2 = 4;
         }
         break;
      case 530022739:
         if (var1.equals("overseer")) {
            var2 = 2;
         }
      }

      switch(var2) {
      case 0:
         return "Admin";
      case 1:
         return "Moderator";
      case 2:
         return "Overseer";
      case 3:
         return "GM";
      case 4:
         return "Observer";
      default:
         return "None";
      }
   }

   public void setAccessLevel(String var1) {
      this.accessLevel = var1;
   }

   public void addMechanicsItem(String var1, VehiclePart var2, Long var3) {
      byte var4 = 1;
      byte var5 = 1;
      if (this.mechanicsItem.get(Long.parseLong(var1)) == null) {
         if (var2.getTable("uninstall") != null && var2.getTable("uninstall").rawget("skills") != null) {
            String[] var6 = ((String)var2.getTable("uninstall").rawget("skills")).split(";");
            String[] var7 = var6;
            int var8 = var6.length;

            for(int var9 = 0; var9 < var8; ++var9) {
               String var10 = var7[var9];
               if (var10.contains("Mechanics")) {
                  int var11 = Integer.parseInt(var10.split(":")[1]);
                  if (var11 >= 6) {
                     var4 = 3;
                     var5 = 7;
                  } else if (var11 >= 4) {
                     var4 = 3;
                     var5 = 5;
                  } else if (var11 >= 2) {
                     var4 = 2;
                     var5 = 4;
                  } else if (Rand.Next(3) == 0) {
                     var4 = 2;
                     var5 = 2;
                  }
               }
            }
         }

         this.getXp().AddXP(PerkFactory.Perks.Mechanics, (float)Rand.Next(var4, var5));
      }

      this.mechanicsItem.put(Long.parseLong(var1), var3);
   }

   public void setPosition(float var1, float var2, float var3) {
      this.setX(var1);
      this.setY(var2);
      this.setZ(var3);
   }

   private void updateTemperatureCheck() {
      int var1 = this.Moodles.getMoodleLevel(MoodleType.Hypothermia);
      if (this.hypothermiaCache == -1 || this.hypothermiaCache != var1) {
         if (var1 >= 3 && var1 > this.hypothermiaCache && this.isAsleep() && !this.ForceWakeUp) {
            this.forceAwake();
         }

         this.hypothermiaCache = var1;
      }

      int var2 = this.Moodles.getMoodleLevel(MoodleType.Hyperthermia);
      if (this.hyperthermiaCache == -1 || this.hyperthermiaCache != var2) {
         if (var2 >= 3 && var2 > this.hyperthermiaCache && this.isAsleep() && !this.ForceWakeUp) {
            this.forceAwake();
         }

         this.hyperthermiaCache = var2;
      }

   }

   public float getZombieRelevenceScore(IsoZombie var1) {
      if (var1.getCurrentSquare() == null) {
         return -10000.0F;
      } else {
         float var2 = 0.0F;
         if (var1.getCurrentSquare().getCanSee(this.PlayerIndex)) {
            var2 += 100.0F;
         } else if (var1.getCurrentSquare().isCouldSee(this.PlayerIndex)) {
            var2 += 10.0F;
         }

         if (var1.getCurrentSquare().getRoom() != null && this.current.getRoom() == null) {
            var2 -= 20.0F;
         }

         if (var1.getCurrentSquare().getRoom() == null && this.current.getRoom() != null) {
            var2 -= 20.0F;
         }

         if (var1.getCurrentSquare().getRoom() != this.current.getRoom()) {
            var2 -= 20.0F;
         }

         float var3 = var1.DistTo(this);
         var2 -= var3;
         if (var3 < 20.0F) {
            var2 += 300.0F;
         }

         if (var3 < 15.0F) {
            var2 += 300.0F;
         }

         if (var3 < 10.0F) {
            var2 += 1000.0F;
         }

         if (var1.getTargetAlpha() < 1.0F && var2 > 0.0F) {
            var2 *= var1.getTargetAlpha();
         }

         return var2;
      }
   }

   public BaseVisual getVisual() {
      return this.humanVisual;
   }

   public HumanVisual getHumanVisual() {
      return this.humanVisual;
   }

   public ItemVisuals getItemVisuals() {
      return this.itemVisuals;
   }

   public void getItemVisuals(ItemVisuals var1) {
      if (!this.bRemote) {
         this.getWornItems().getItemVisuals(var1);
      } else {
         var1.clear();
         var1.addAll(this.itemVisuals);
      }

   }

   public void dressInNamedOutfit(String var1) {
      this.getHumanVisual().dressInNamedOutfit(var1, this.itemVisuals);
      this.onClothingOutfitPreviewChanged();
   }

   public void dressInClothingItem(String var1) {
      this.getHumanVisual().dressInClothingItem(var1, this.itemVisuals);
      this.onClothingOutfitPreviewChanged();
   }

   private void onClothingOutfitPreviewChanged() {
      if (this.isLocalPlayer()) {
         this.getInventory().clear();
         this.wornItems.setFromItemVisuals(this.itemVisuals);
         this.wornItems.addItemsToItemContainer(this.getInventory());
         this.itemVisuals.clear();
         this.resetModel();
         this.onWornItemsChanged();
      }
   }

   public void onWornItemsChanged() {
      this.parameterShoeType.setShoeType((ParameterShoeType.ShoeType)null);
   }

   public void actionStateChanged(ActionContext var1) {
      super.actionStateChanged(var1);
   }

   public Vector2 getLastAngle() {
      return this.lastAngle;
   }

   public void setLastAngle(Vector2 var1) {
      this.lastAngle.set(var1);
   }

   public int getDialogMood() {
      return this.DialogMood;
   }

   public void setDialogMood(int var1) {
      this.DialogMood = var1;
   }

   public int getPing() {
      return this.ping;
   }

   public void setPing(int var1) {
      this.ping = var1;
   }

   public IsoMovingObject getDragObject() {
      return this.DragObject;
   }

   public void setDragObject(IsoMovingObject var1) {
      this.DragObject = var1;
   }

   public float getAsleepTime() {
      return this.AsleepTime;
   }

   public void setAsleepTime(float var1) {
      this.AsleepTime = var1;
   }

   public Stack getSpottedList() {
      return this.spottedList;
   }

   public int getTicksSinceSeenZombie() {
      return this.TicksSinceSeenZombie;
   }

   public void setTicksSinceSeenZombie(int var1) {
      this.TicksSinceSeenZombie = var1;
   }

   public boolean isWaiting() {
      return this.Waiting;
   }

   public void setWaiting(boolean var1) {
      this.Waiting = var1;
   }

   public IsoSurvivor getDragCharacter() {
      return this.DragCharacter;
   }

   public void setDragCharacter(IsoSurvivor var1) {
      this.DragCharacter = var1;
   }

   public float getHeartDelay() {
      return this.heartDelay;
   }

   public void setHeartDelay(float var1) {
      this.heartDelay = var1;
   }

   public float getHeartDelayMax() {
      return this.heartDelayMax;
   }

   public void setHeartDelayMax(int var1) {
      this.heartDelayMax = (float)var1;
   }

   public double getHoursSurvived() {
      return this.HoursSurvived;
   }

   public void setHoursSurvived(double var1) {
      this.HoursSurvived = var1;
   }

   public float getMaxWeightDelta() {
      return this.maxWeightDelta;
   }

   public void setMaxWeightDelta(float var1) {
      this.maxWeightDelta = var1;
   }

   public String getForname() {
      return this.Forname;
   }

   public void setForname(String var1) {
      this.Forname = var1;
   }

   public String getSurname() {
      return this.Surname;
   }

   public void setSurname(String var1) {
      this.Surname = var1;
   }

   public boolean isbChangeCharacterDebounce() {
      return this.bChangeCharacterDebounce;
   }

   public void setbChangeCharacterDebounce(boolean var1) {
      this.bChangeCharacterDebounce = var1;
   }

   public int getFollowID() {
      return this.followID;
   }

   public void setFollowID(int var1) {
      this.followID = var1;
   }

   public boolean isbSeenThisFrame() {
      return this.bSeenThisFrame;
   }

   public void setbSeenThisFrame(boolean var1) {
      this.bSeenThisFrame = var1;
   }

   public boolean isbCouldBeSeenThisFrame() {
      return this.bCouldBeSeenThisFrame;
   }

   public void setbCouldBeSeenThisFrame(boolean var1) {
      this.bCouldBeSeenThisFrame = var1;
   }

   public float getTimeSinceLastStab() {
      return this.timeSinceLastStab;
   }

   public void setTimeSinceLastStab(float var1) {
      this.timeSinceLastStab = var1;
   }

   public Stack getLastSpotted() {
      return this.LastSpotted;
   }

   public void setLastSpotted(Stack var1) {
      this.LastSpotted = var1;
   }

   public int getClearSpottedTimer() {
      return this.ClearSpottedTimer;
   }

   public void setClearSpottedTimer(int var1) {
      this.ClearSpottedTimer = var1;
   }

   public boolean IsRunning() {
      return this.isRunning();
   }

   public void InitSpriteParts() {
   }

   public boolean IsAiming() {
      return this.isAiming();
   }

   public String getTagPrefix() {
      return this.tagPrefix;
   }

   public void setTagPrefix(String var1) {
      this.tagPrefix = var1;
   }

   public ColorInfo getTagColor() {
      return this.tagColor;
   }

   public void setTagColor(ColorInfo var1) {
      this.tagColor.set(var1);
   }

   /** @deprecated */
   @Deprecated
   public Integer getTransactionID() {
      return this.transactionID;
   }

   /** @deprecated */
   @Deprecated
   public void setTransactionID(Integer var1) {
      this.transactionID = var1;
   }

   public String getDisplayName() {
      if (GameClient.bClient) {
         if (this.displayName == null || this.displayName.equals("")) {
            this.displayName = this.getUsername();
         }
      } else if (!GameServer.bServer) {
         this.displayName = this.getUsername();
      }

      return this.displayName;
   }

   public void setDisplayName(String var1) {
      this.displayName = var1;
   }

   public boolean isSeeNonPvpZone() {
      return this.seeNonPvpZone;
   }

   public void setSeeNonPvpZone(boolean var1) {
      this.seeNonPvpZone = var1;
   }

   public boolean isShowTag() {
      return this.showTag;
   }

   public void setShowTag(boolean var1) {
      this.showTag = var1;
   }

   public boolean isFactionPvp() {
      return this.factionPvp;
   }

   public void setFactionPvp(boolean var1) {
      this.factionPvp = var1;
   }

   public boolean isForceAim() {
      return this.forceAim;
   }

   public void setForceAim(boolean var1) {
      this.forceAim = var1;
   }

   public boolean toggleForceAim() {
      this.forceAim = !this.forceAim;
      return this.forceAim;
   }

   public boolean isForceSprint() {
      return this.forceSprint;
   }

   public void setForceSprint(boolean var1) {
      this.forceSprint = var1;
   }

   public boolean toggleForceSprint() {
      this.forceSprint = !this.forceSprint;
      return this.forceSprint;
   }

   public boolean isForceRun() {
      return this.forceRun;
   }

   public void setForceRun(boolean var1) {
      this.forceRun = var1;
   }

   public boolean toggleForceRun() {
      this.forceRun = !this.forceRun;
      return this.forceRun;
   }

   public boolean isDeaf() {
      return this.Traits.Deaf.isSet();
   }

   public boolean isForceOverrideAnim() {
      return this.forceOverrideAnim;
   }

   public void setForceOverrideAnim(boolean var1) {
      this.forceOverrideAnim = var1;
   }

   public Long getMechanicsItem(String var1) {
      return (Long)this.mechanicsItem.get(Long.parseLong(var1));
   }

   public boolean isWearingNightVisionGoggles() {
      return this.isWearingNightVisionGoggles;
   }

   public void setWearingNightVisionGoggles(boolean var1) {
      this.isWearingNightVisionGoggles = var1;
   }

   public void OnAnimEvent(AnimLayer var1, AnimEvent var2) {
      super.OnAnimEvent(var1, var2);
      if (!this.CharacterActions.isEmpty()) {
         BaseAction var3 = (BaseAction)this.CharacterActions.get(0);
         var3.OnAnimEvent(var2);
      }
   }

   public void onCullStateChanged(ModelManager var1, boolean var2) {
      super.onCullStateChanged(var1, var2);
      if (!var2) {
         DebugFileWatcher.instance.add(this.m_setClothingTriggerWatcher);
      } else {
         DebugFileWatcher.instance.remove(this.m_setClothingTriggerWatcher);
      }

   }

   public boolean isTimedActionInstant() {
      return (GameClient.bClient || GameServer.bServer) && "None".equals(this.getAccessLevel()) ? false : super.isTimedActionInstant();
   }

   public boolean isSkeleton() {
      return false;
   }

   public void addWorldSoundUnlessInvisible(int var1, int var2, boolean var3) {
      if (!this.isGhostMode()) {
         super.addWorldSoundUnlessInvisible(var1, var2, var3);
      }
   }

   private void updateFootInjuries() {
      InventoryItem var1 = this.getWornItems().getItem("Shoes");
      if (var1 == null || var1.getCondition() <= 0) {
         if (this.getCurrentSquare() != null) {
            if (this.getCurrentSquare().getBrokenGlass() != null) {
               BodyPartType var2 = BodyPartType.FromIndex(Rand.Next(BodyPartType.ToIndex(BodyPartType.Foot_L), BodyPartType.ToIndex(BodyPartType.Foot_R) + 1));
               BodyPart var3 = this.getBodyDamage().getBodyPart(var2);
               var3.generateDeepShardWound();
            }

            byte var7 = 0;
            boolean var8 = false;
            if (this.getCurrentSquare().getZone() != null && (this.getCurrentSquare().getZone().getType().equals("Forest") || this.getCurrentSquare().getZone().getType().equals("DeepForest"))) {
               var8 = true;
            }

            IsoObject var4 = this.getCurrentSquare().getFloor();
            if (var4 != null && var4.getSprite() != null && var4.getSprite().getName() != null) {
               String var5 = var4.getSprite().getName();
               if (var5.contains("blends_natural_01") && var8) {
                  var7 = 2;
               } else if (!var5.contains("blends_natural_01") && this.getCurrentSquare().getBuilding() == null) {
                  var7 = 1;
               }
            }

            if (var7 != 0) {
               if (this.isWalking && !this.isRunning() && !this.isSprinting()) {
                  this.footInjuryTimer += var7;
               } else if (this.isRunning() && !this.isSprinting()) {
                  this.footInjuryTimer += var7 + 2;
               } else {
                  if (!this.isSprinting()) {
                     if (this.footInjuryTimer > 0 && Rand.Next(3) == 0) {
                        --this.footInjuryTimer;
                     }

                     return;
                  }

                  this.footInjuryTimer += var7 + 5;
               }

               if (Rand.Next(Rand.AdjustForFramerate(8500 - this.footInjuryTimer)) <= 0) {
                  this.footInjuryTimer = 0;
                  BodyPartType var9 = BodyPartType.FromIndex(Rand.Next(BodyPartType.ToIndex(BodyPartType.Foot_L), BodyPartType.ToIndex(BodyPartType.Foot_R) + 1));
                  BodyPart var6 = this.getBodyDamage().getBodyPart(var9);
                  if (var6.getScratchTime() > 30.0F) {
                     if (!var6.isCut()) {
                        var6.setCut(true);
                        var6.setCutTime(Rand.Next(1.0F, 3.0F));
                     } else {
                        var6.setCutTime(var6.getCutTime() + Rand.Next(1.0F, 3.0F));
                     }
                  } else {
                     if (!var6.scratched()) {
                        var6.setScratched(true, true);
                        var6.setScratchTime(Rand.Next(1.0F, 3.0F));
                     } else {
                        var6.setScratchTime(var6.getScratchTime() + Rand.Next(1.0F, 3.0F));
                     }

                     if (var6.getScratchTime() > 20.0F && var6.getBleedingTime() == 0.0F) {
                        var6.setBleedingTime(Rand.Next(3.0F, 10.0F));
                     }
                  }
               }

            }
         }
      }
   }

   public int getMoodleLevel(MoodleType var1) {
      return this.getMoodles().getMoodleLevel(var1);
   }

   public boolean isAttackStarted() {
      return this.attackStarted;
   }

   public boolean isBehaviourMoving() {
      return this.hasPath() || super.isBehaviourMoving();
   }

   public boolean isJustMoved() {
      return this.JustMoved;
   }

   public void setJustMoved(boolean var1) {
      this.JustMoved = var1;
   }

   public boolean isPlayerMoving() {
      return this.m_isPlayerMoving;
   }

   public float getTimedActionTimeModifier() {
      return this.getBodyDamage().getThermoregulator() != null ? this.getBodyDamage().getThermoregulator().getTimedActionTimeModifier() : 1.0F;
   }

   public boolean isLookingWhileInVehicle() {
      return this.getVehicle() != null && this.bLookingWhileInVehicle;
   }

   public void setInitiateAttack(boolean var1) {
      this.initiateAttack = var1;
   }

   public boolean isIgnoreInputsForDirection() {
      return this.ignoreInputsForDirection;
   }

   public void setIgnoreInputsForDirection(boolean var1) {
      this.ignoreInputsForDirection = var1;
   }

   public boolean isIgnoreContextKey() {
      return this.ignoreContextKey;
   }

   public void setIgnoreContextKey(boolean var1) {
      this.ignoreContextKey = var1;
   }

   public boolean isIgnoreAutoVault() {
      return this.ignoreAutoVault;
   }

   public void setIgnoreAutoVault(boolean var1) {
      this.ignoreAutoVault = var1;
   }

   public boolean isAllowSprint() {
      return this.allowSprint;
   }

   public void setAllowSprint(boolean var1) {
      this.allowSprint = var1;
   }

   public boolean isAllowRun() {
      return this.allowRun;
   }

   public void setAllowRun(boolean var1) {
      this.allowRun = var1;
   }

   public String getAttackType() {
      return this.attackType;
   }

   public void setAttackType(String var1) {
      this.attackType = var1;
   }

   public void clearNetworkEvents() {
      this.networkAI.events.clear();
      this.clearVariable("PerformingAction");
      this.clearVariable("IsPerformingAnAction");
      this.overridePrimaryHandModel = null;
      this.overrideSecondaryHandModel = null;
      this.resetModelNextFrame();
   }

   public boolean isCanSeeAll() {
      return this.canSeeAll;
   }

   public void setCanSeeAll(boolean var1) {
      this.canSeeAll = var1;
   }

   public boolean isNetworkTeleportEnabled() {
      return NetworkTeleport.enable;
   }

   public void setNetworkTeleportEnabled(boolean var1) {
      NetworkTeleport.enable = var1;
   }

   public boolean isCheatPlayerSeeEveryone() {
      return DebugOptions.instance.CheatPlayerSeeEveryone.getValue();
   }

   public float getRelevantAndDistance(float var1, float var2, float var3) {
      return Math.abs(this.x - var1) <= var3 * 10.0F && Math.abs(this.y - var2) <= var3 * 10.0F ? IsoUtils.DistanceTo(this.x, this.y, var1, var2) : Float.POSITIVE_INFINITY;
   }

   public boolean isCanHearAll() {
      return this.canHearAll;
   }

   public void setCanHearAll(boolean var1) {
      this.canHearAll = var1;
   }

   public ArrayList getAlreadyReadBook() {
      return this.alreadyReadBook;
   }

   public void setMoodleCantSprint(boolean var1) {
      this.MoodleCantSprint = var1;
   }

   public void setAttackFromBehind(boolean var1) {
      this.attackFromBehind = var1;
   }

   public boolean isAttackFromBehind() {
      return this.attackFromBehind;
   }

   public float getDamageFromHitByACar(float var1) {
      float var2 = 1.0F;
      switch(SandboxOptions.instance.DamageToPlayerFromHitByACar.getValue()) {
      case 1:
         var2 = 0.0F;
         break;
      case 2:
         var2 = 0.5F;
      case 3:
      default:
         break;
      case 4:
         var2 = 2.0F;
         break;
      case 5:
         var2 = 5.0F;
      }

      float var3 = var1 * var2;
      if (DebugOptions.instance.MultiplayerCriticalHit.getValue()) {
         var3 += 10.0F;
      }

      if (var3 > 0.0F) {
         int var4 = (int)(2.0F + var3 * 0.07F);

         for(int var5 = 0; var5 < var4; ++var5) {
            int var6 = Rand.Next(BodyPartType.ToIndex(BodyPartType.Hand_L), BodyPartType.ToIndex(BodyPartType.MAX));
            BodyPart var7 = this.getBodyDamage().getBodyPart(BodyPartType.FromIndex(var6));
            float var8 = Math.max(Rand.Next(var3 - 15.0F, var3), 5.0F);
            if (this.Traits.FastHealer.isSet()) {
               var8 = (float)((double)var8 * 0.8D);
            } else if (this.Traits.SlowHealer.isSet()) {
               var8 = (float)((double)var8 * 1.2D);
            }

            switch(SandboxOptions.instance.InjurySeverity.getValue()) {
            case 1:
               var8 *= 0.5F;
               break;
            case 3:
               var8 *= 1.5F;
            }

            var8 = (float)((double)var8 * 0.9D);
            var7.AddDamage(var8);
            if (var8 > 40.0F && Rand.Next(12) == 0) {
               var7.generateDeepWound();
            }

            if (var8 > 10.0F && Rand.Next(100) <= 10 && SandboxOptions.instance.BoneFracture.getValue()) {
               var7.setFractureTime(Rand.Next(Rand.Next(10.0F, var8 + 10.0F), Rand.Next(var8 + 20.0F, var8 + 30.0F)));
            }

            if (var8 > 30.0F && Rand.Next(100) <= 80 && SandboxOptions.instance.BoneFracture.getValue() && var6 == BodyPartType.ToIndex(BodyPartType.Head)) {
               var7.setFractureTime(Rand.Next(Rand.Next(10.0F, var8 + 10.0F), Rand.Next(var8 + 20.0F, var8 + 30.0F)));
            }

            if (var8 > 10.0F && Rand.Next(100) <= 60 && SandboxOptions.instance.BoneFracture.getValue() && var6 > BodyPartType.ToIndex(BodyPartType.Groin)) {
               var7.setFractureTime(Rand.Next(Rand.Next(10.0F, var8 + 20.0F), Rand.Next(var8 + 30.0F, var8 + 40.0F)));
            }
         }

         this.getBodyDamage().Update();
      }

      this.addBlood(var1);
      if (GameClient.bClient && this.isLocal()) {
         this.updateMovementRates();
         GameClient.sendPlayerInjuries(this);
         GameClient.sendPlayerDamage(this);
      }

      return var3;
   }

   public float Hit(BaseVehicle var1, float var2, boolean var3, float var4, float var5) {
      float var6 = this.doBeatenVehicle(var2);
      super.Hit(var1, var2, var3, var4, var5);
      return var6;
   }

   public void Kill(IsoGameCharacter var1) {
      if (!this.isOnKillDone()) {
         super.Kill(var1);
         this.getBodyDamage().setOverallBodyHealth(0.0F);
         if (var1 == null) {
            this.DoDeath((HandWeapon)null, (IsoGameCharacter)null);
         } else {
            this.DoDeath(var1.getUseHandWeapon(), var1);
         }

      }
   }

   public void becomeCorpse() {
      if (!this.isOnDeathDone()) {
         if (this.shouldBecomeCorpse()) {
            super.becomeCorpse();
            IsoDeadBody var1 = new IsoDeadBody(this);
            if (this.shouldBecomeZombieAfterDeath()) {
               var1.reanimateLater();
            }

         }
      }
   }

   public void preupdate() {
      if (GameClient.bClient) {
         this.networkAI.updateHitVehicle();
         if (!this.isLocal() && this.isKnockedDown() && !this.isOnFloor()) {
            HitReactionNetworkAI var1 = this.getHitReactionNetworkAI();
            if (var1.isSetup() && !var1.isStarted()) {
               var1.start();
               if (Core.bDebug) {
                  DebugLog.log(DebugType.Multiplayer, "Fall start (update): " + var1.getDescription());
               }
            }
         }
      }

      super.preupdate();
   }

   public HitReactionNetworkAI getHitReactionNetworkAI() {
      return this.networkAI.hitReaction;
   }

   public NetworkCharacterAI getNetworkCharacterAI() {
      return this.networkAI;
   }

   public void setFitnessSpeed() {
      this.clearVariable("FitnessStruggle");
      float var1 = (float)this.getPerkLevel(PerkFactory.Perks.Fitness) / 5.0F / 1.1F - (float)this.getMoodleLevel(MoodleType.Endurance) / 20.0F;
      if (var1 > 1.5F) {
         var1 = 1.5F;
      }

      if (var1 < 0.85F) {
         var1 = 1.0F;
         this.setVariable("FitnessStruggle", true);
      }

      this.setVariable("FitnessSpeed", var1);
   }

   public boolean isLocal() {
      return super.isLocal() || this.isLocalPlayer();
   }

   public boolean isClimbOverWallSuccess() {
      return this.climbOverWallSuccess;
   }

   public void setClimbOverWallSuccess(boolean var1) {
      this.climbOverWallSuccess = var1;
   }

   public boolean isClimbOverWallStruggle() {
      return this.climbOverWallStruggle;
   }

   public void setClimbOverWallStruggle(boolean var1) {
      this.climbOverWallStruggle = var1;
   }

   public boolean isVehicleCollisionActive(BaseVehicle var1) {
      if (!super.isVehicleCollisionActive(var1)) {
         return false;
      } else if (this.isGodMod()) {
         return false;
      } else if (!SwipeStatePlayer.checkPVP(this.vehicle4testCollision.getDriver(), this)) {
         return false;
      } else if (SandboxOptions.instance.DamageToPlayerFromHitByACar.getValue() < 1) {
         return false;
      } else if (this.getVehicle() == var1) {
         return false;
      } else {
         return !this.isCurrentState(PlayerFallDownState.instance()) && !this.isCurrentState(PlayerFallingState.instance()) && !this.isCurrentState(PlayerKnockedDown.instance());
      }
   }

   public boolean isShowMPInfos() {
      return this.showMPInfos;
   }

   public void setShowMPInfos(boolean var1) {
      this.showMPInfos = var1;
   }

   static {
      m_isoPlayerTriggerWatcher = new PredicatedFileWatcher(ZomboidFileSystem.instance.getMessagingDirSub("Trigger_ResetIsoPlayerModel.xml"), IsoPlayer::onTrigger_ResetIsoPlayerModel);
      tempVector2_1 = new Vector2();
      tempVector2_2 = new Vector2();
      tempVector3f = new Vector3f();
      s_moveVars = new IsoPlayer.MoveVars();
      s_targetsProne = new ArrayList();
      s_targetsStanding = new ArrayList();
   }

   static class InputState {
      public boolean bMelee;
      public boolean isAttacking;
      public boolean bRunning;
      public boolean bSprinting;
      boolean isAiming;
      boolean isCharging;
      boolean isChargingLT;
   }

   private static class VehicleContainerData {
      ArrayList tempContainers = new ArrayList();
      ArrayList containers = new ArrayList();
      Stack freeContainers = new Stack();
   }

   private static class s_performance {
      static final PerformanceProfileProbe postUpdate = new PerformanceProfileProbe("IsoPlayer.postUpdate");
      static final PerformanceProfileProbe highlightRangedTargets = new PerformanceProfileProbe("IsoPlayer.highlightRangedTargets");
      static final PerformanceProfileProbe update = new PerformanceProfileProbe("IsoPlayer.update");
   }

   static final class MoveVars {
      float moveX;
      float moveY;
      float strafeX;
      float strafeY;
      IsoDirections NewFacing;
   }

   private static class VehicleContainer {
      BaseVehicle vehicle;
      int containerIndex;

      public IsoPlayer.VehicleContainer set(BaseVehicle var1, int var2) {
         this.vehicle = var1;
         this.containerIndex = var2;
         return this;
      }

      public boolean equals(Object var1) {
         return var1 instanceof IsoPlayer.VehicleContainer && this.vehicle == ((IsoPlayer.VehicleContainer)var1).vehicle && this.containerIndex == ((IsoPlayer.VehicleContainer)var1).containerIndex;
      }
   }
}

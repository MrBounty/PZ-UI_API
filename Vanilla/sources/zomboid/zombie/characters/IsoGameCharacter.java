package zombie.characters;

import fmod.fmod.FMODManager;
import fmod.fmod.FMOD_STUDIO_PARAMETER_DESCRIPTION;
import fmod.fmod.IFMODParameterUpdater;
import gnu.trove.map.hash.THashMap;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Stack;
import java.util.UUID;
import java.util.Map.Entry;
import java.util.function.Consumer;
import org.joml.Vector3f;
import se.krka.kahlua.vm.KahluaTable;
import zombie.AmbientStreamManager;
import zombie.DebugFileWatcher;
import zombie.GameTime;
import zombie.GameWindow;
import zombie.PersistentOutfits;
import zombie.PredicatedFileWatcher;
import zombie.SandboxOptions;
import zombie.SoundManager;
import zombie.SystemDisabler;
import zombie.VirtualZombieManager;
import zombie.WorldSoundManager;
import zombie.ZomboidFileSystem;
import zombie.ZomboidGlobals;
import zombie.Lua.LuaEventManager;
import zombie.Lua.LuaHookManager;
import zombie.Lua.LuaManager;
import zombie.ai.GameCharacterAIBrain;
import zombie.ai.MapKnowledge;
import zombie.ai.State;
import zombie.ai.StateMachine;
import zombie.ai.astar.AStarPathFinder;
import zombie.ai.astar.AStarPathFinderResult;
import zombie.ai.sadisticAIDirector.SleepingEventData;
import zombie.ai.states.AttackNetworkState;
import zombie.ai.states.AttackState;
import zombie.ai.states.BumpedState;
import zombie.ai.states.ClimbDownSheetRopeState;
import zombie.ai.states.ClimbOverFenceState;
import zombie.ai.states.ClimbOverWallState;
import zombie.ai.states.ClimbSheetRopeState;
import zombie.ai.states.ClimbThroughWindowState;
import zombie.ai.states.CloseWindowState;
import zombie.ai.states.CollideWithWallState;
import zombie.ai.states.FakeDeadZombieState;
import zombie.ai.states.IdleState;
import zombie.ai.states.LungeNetworkState;
import zombie.ai.states.LungeState;
import zombie.ai.states.OpenWindowState;
import zombie.ai.states.PathFindState;
import zombie.ai.states.PlayerFallDownState;
import zombie.ai.states.PlayerGetUpState;
import zombie.ai.states.PlayerHitReactionPVPState;
import zombie.ai.states.PlayerHitReactionState;
import zombie.ai.states.PlayerKnockedDown;
import zombie.ai.states.PlayerOnGroundState;
import zombie.ai.states.SmashWindowState;
import zombie.ai.states.StaggerBackState;
import zombie.ai.states.SwipeStatePlayer;
import zombie.ai.states.ThumpState;
import zombie.ai.states.WalkTowardState;
import zombie.ai.states.ZombieFallDownState;
import zombie.ai.states.ZombieFallingState;
import zombie.ai.states.ZombieHitReactionState;
import zombie.ai.states.ZombieOnGroundState;
import zombie.audio.BaseSoundEmitter;
import zombie.audio.FMODParameter;
import zombie.audio.FMODParameterList;
import zombie.audio.GameSoundClip;
import zombie.audio.parameters.ParameterZombieState;
import zombie.characterTextures.BloodBodyPartType;
import zombie.characterTextures.BloodClothingType;
import zombie.characters.AttachedItems.AttachedItem;
import zombie.characters.AttachedItems.AttachedItems;
import zombie.characters.AttachedItems.AttachedLocationGroup;
import zombie.characters.AttachedItems.AttachedLocations;
import zombie.characters.BodyDamage.BodyDamage;
import zombie.characters.BodyDamage.BodyPart;
import zombie.characters.BodyDamage.BodyPartLast;
import zombie.characters.BodyDamage.BodyPartType;
import zombie.characters.BodyDamage.Metabolics;
import zombie.characters.BodyDamage.Nutrition;
import zombie.characters.CharacterTimedActions.BaseAction;
import zombie.characters.CharacterTimedActions.LuaTimedActionNew;
import zombie.characters.Moodles.MoodleType;
import zombie.characters.Moodles.Moodles;
import zombie.characters.WornItems.BodyLocationGroup;
import zombie.characters.WornItems.BodyLocations;
import zombie.characters.WornItems.WornItem;
import zombie.characters.WornItems.WornItems;
import zombie.characters.action.ActionContext;
import zombie.characters.action.ActionStateSnapshot;
import zombie.characters.action.IActionStateChanged;
import zombie.characters.skills.PerkFactory;
import zombie.characters.traits.TraitCollection;
import zombie.characters.traits.TraitFactory;
import zombie.chat.ChatElement;
import zombie.chat.ChatElementOwner;
import zombie.chat.ChatManager;
import zombie.chat.ChatMessage;
import zombie.core.BoxedStaticValues;
import zombie.core.Color;
import zombie.core.Colors;
import zombie.core.Core;
import zombie.core.PerformanceSettings;
import zombie.core.Rand;
import zombie.core.SpriteRenderer;
import zombie.core.Translator;
import zombie.core.logger.ExceptionLogger;
import zombie.core.logger.LoggerManager;
import zombie.core.logger.ZLogger;
import zombie.core.math.PZMath;
import zombie.core.opengl.Shader;
import zombie.core.profiling.PerformanceProfileProbe;
import zombie.core.skinnedmodel.ModelManager;
import zombie.core.skinnedmodel.advancedanimation.AdvancedAnimator;
import zombie.core.skinnedmodel.advancedanimation.AnimEvent;
import zombie.core.skinnedmodel.advancedanimation.AnimLayer;
import zombie.core.skinnedmodel.advancedanimation.AnimState;
import zombie.core.skinnedmodel.advancedanimation.AnimationSet;
import zombie.core.skinnedmodel.advancedanimation.AnimationVariableHandle;
import zombie.core.skinnedmodel.advancedanimation.AnimationVariableSlotCallbackBool;
import zombie.core.skinnedmodel.advancedanimation.AnimationVariableSlotCallbackFloat;
import zombie.core.skinnedmodel.advancedanimation.AnimationVariableSlotCallbackInt;
import zombie.core.skinnedmodel.advancedanimation.AnimationVariableSlotCallbackString;
import zombie.core.skinnedmodel.advancedanimation.AnimationVariableSource;
import zombie.core.skinnedmodel.advancedanimation.AnimationVariableType;
import zombie.core.skinnedmodel.advancedanimation.IAnimEventCallback;
import zombie.core.skinnedmodel.advancedanimation.IAnimatable;
import zombie.core.skinnedmodel.advancedanimation.IAnimationVariableMap;
import zombie.core.skinnedmodel.advancedanimation.IAnimationVariableSlot;
import zombie.core.skinnedmodel.advancedanimation.LiveAnimNode;
import zombie.core.skinnedmodel.advancedanimation.debug.AnimatorDebugMonitor;
import zombie.core.skinnedmodel.animation.AnimationMultiTrack;
import zombie.core.skinnedmodel.animation.AnimationPlayer;
import zombie.core.skinnedmodel.animation.AnimationTrack;
import zombie.core.skinnedmodel.animation.debug.AnimationPlayerRecorder;
import zombie.core.skinnedmodel.model.Model;
import zombie.core.skinnedmodel.model.ModelInstance;
import zombie.core.skinnedmodel.model.ModelInstanceTextureCreator;
import zombie.core.skinnedmodel.population.BeardStyle;
import zombie.core.skinnedmodel.population.BeardStyles;
import zombie.core.skinnedmodel.population.ClothingItem;
import zombie.core.skinnedmodel.population.ClothingItemReference;
import zombie.core.skinnedmodel.population.HairStyle;
import zombie.core.skinnedmodel.population.HairStyles;
import zombie.core.skinnedmodel.population.IClothingItemListener;
import zombie.core.skinnedmodel.population.Outfit;
import zombie.core.skinnedmodel.population.OutfitManager;
import zombie.core.skinnedmodel.population.OutfitRNG;
import zombie.core.skinnedmodel.visual.BaseVisual;
import zombie.core.skinnedmodel.visual.HumanVisual;
import zombie.core.skinnedmodel.visual.IHumanVisual;
import zombie.core.skinnedmodel.visual.ItemVisual;
import zombie.core.skinnedmodel.visual.ItemVisuals;
import zombie.core.textures.ColorInfo;
import zombie.core.utils.UpdateLimit;
import zombie.core.znet.SteamUtils;
import zombie.debug.DebugLog;
import zombie.debug.DebugOptions;
import zombie.debug.DebugType;
import zombie.debug.LineDrawer;
import zombie.debug.LogSeverity;
import zombie.gameStates.IngameState;
import zombie.input.Mouse;
import zombie.interfaces.IUpdater;
import zombie.inventory.InventoryItem;
import zombie.inventory.InventoryItemFactory;
import zombie.inventory.ItemContainer;
import zombie.inventory.types.Clothing;
import zombie.inventory.types.Drainable;
import zombie.inventory.types.Food;
import zombie.inventory.types.HandWeapon;
import zombie.inventory.types.InventoryContainer;
import zombie.inventory.types.Literature;
import zombie.inventory.types.Radio;
import zombie.inventory.types.WeaponType;
import zombie.iso.BuildingDef;
import zombie.iso.IsoCamera;
import zombie.iso.IsoCell;
import zombie.iso.IsoChunk;
import zombie.iso.IsoDirections;
import zombie.iso.IsoGridSquare;
import zombie.iso.IsoLightSource;
import zombie.iso.IsoMovingObject;
import zombie.iso.IsoObject;
import zombie.iso.IsoObjectPicker;
import zombie.iso.IsoRoofFixer;
import zombie.iso.IsoUtils;
import zombie.iso.IsoWorld;
import zombie.iso.LightingJNI;
import zombie.iso.LosUtil;
import zombie.iso.RoomDef;
import zombie.iso.Vector2;
import zombie.iso.Vector3;
import zombie.iso.SpriteDetails.IsoFlagType;
import zombie.iso.SpriteDetails.IsoObjectType;
import zombie.iso.areas.IsoBuilding;
import zombie.iso.areas.IsoRoom;
import zombie.iso.objects.IsoBall;
import zombie.iso.objects.IsoDeadBody;
import zombie.iso.objects.IsoFallingClothing;
import zombie.iso.objects.IsoFireManager;
import zombie.iso.objects.IsoMolotovCocktail;
import zombie.iso.objects.IsoThumpable;
import zombie.iso.objects.IsoTree;
import zombie.iso.objects.IsoWindow;
import zombie.iso.objects.IsoWindowFrame;
import zombie.iso.objects.IsoZombieGiblets;
import zombie.iso.objects.RainManager;
import zombie.iso.objects.interfaces.BarricadeAble;
import zombie.iso.sprite.IsoSprite;
import zombie.iso.sprite.IsoSpriteInstance;
import zombie.iso.sprite.IsoSpriteManager;
import zombie.network.GameClient;
import zombie.network.GameServer;
import zombie.network.NetworkVariables;
import zombie.network.ServerGUI;
import zombie.network.ServerMap;
import zombie.network.ServerOptions;
import zombie.network.chat.ChatServer;
import zombie.network.chat.ChatType;
import zombie.network.packets.hit.AttackVars;
import zombie.network.packets.hit.HitInfo;
import zombie.popman.ObjectPool;
import zombie.profanity.ProfanityFilter;
import zombie.radio.ZomboidRadio;
import zombie.scripting.ScriptManager;
import zombie.scripting.objects.Item;
import zombie.scripting.objects.Recipe;
import zombie.scripting.objects.VehicleScript;
import zombie.ui.ActionProgressBar;
import zombie.ui.TextDrawObject;
import zombie.ui.TextManager;
import zombie.ui.TutorialManager;
import zombie.ui.UIFont;
import zombie.ui.UIManager;
import zombie.util.IPooledObject;
import zombie.util.Pool;
import zombie.util.StringUtils;
import zombie.util.Type;
import zombie.util.list.PZArrayUtil;
import zombie.vehicles.BaseVehicle;
import zombie.vehicles.PathFindBehavior2;
import zombie.vehicles.PolygonalMap2;
import zombie.vehicles.VehicleLight;
import zombie.vehicles.VehiclePart;

public abstract class IsoGameCharacter extends IsoMovingObject implements Talker, ChatElementOwner, IAnimatable, IAnimationVariableMap, IClothingItemListener, IActionStateChanged, IAnimEventCallback, IFMODParameterUpdater, ILuaVariableSource, ILuaGameCharacter {
   private boolean ignoreAimingInput = false;
   public boolean doRenderShadow = true;
   private boolean doDeathSound = true;
   private boolean canShout = true;
   public boolean doDirtBloodEtc = true;
   private static int IID = 0;
   public static final int RENDER_OFFSET_X = 1;
   public static final int RENDER_OFFSET_Y = -89;
   public static final float s_maxPossibleTwist = 70.0F;
   private static final HashMap SurvivorMap = new HashMap();
   private static final int[] LevelUpLevels = new int[]{25, 75, 150, 225, 300, 400, 500, 600, 700, 800, 900, 1000, 1200, 1400, 1600, 1800, 2000, 2200, 2400, 2600, 2800, 3000, 3200, 3400, 3600, 3800, 4000, 4400, 4800, 5200, 5600, 6000};
   protected static final Vector2 tempo = new Vector2();
   protected static final ColorInfo inf = new ColorInfo();
   public long vocalEvent;
   public long removedFromWorldMS = 0L;
   private boolean bSneaking = false;
   protected static final Vector2 tempo2 = new Vector2();
   private static final Vector2 tempVector2_1 = new Vector2();
   private static final Vector2 tempVector2_2 = new Vector2();
   private static String sleepText = null;
   protected final ArrayList savedInventoryItems = new ArrayList();
   private final String instancename;
   protected GameCharacterAIBrain GameCharacterAIBrain;
   public final ArrayList amputations = new ArrayList();
   public ModelInstance hair;
   public ModelInstance beard;
   public ModelInstance primaryHandModel;
   public ModelInstance secondaryHandModel;
   public final ActionContext actionContext = new ActionContext(this);
   public final BaseCharacterSoundEmitter emitter;
   private final FMODParameterList fmodParameters = new FMODParameterList();
   private final AnimationVariableSource m_GameVariables = new AnimationVariableSource();
   private AnimationVariableSource m_PlaybackGameVariables = null;
   private boolean bRunning = false;
   private boolean bSprinting = false;
   private boolean m_godMod = false;
   private boolean m_invisible = false;
   private boolean m_avoidDamage = false;
   public boolean callOut = false;
   public IsoGameCharacter ReanimatedCorpse;
   public int ReanimatedCorpseID = -1;
   private AnimationPlayer m_animPlayer = null;
   public final AdvancedAnimator advancedAnimator;
   public final HashMap StateMachineParams = new HashMap();
   public long clientIgnoreCollision = 0L;
   private boolean isCrit = false;
   private boolean bKnockedDown = false;
   public int bumpNbr = 0;
   public boolean upKillCount = true;
   private final ArrayList PerkList = new ArrayList();
   private final Vector2 m_forwardDirection = new Vector2();
   public boolean Asleep = false;
   public boolean blockTurning = false;
   public float speedMod = 1.0F;
   public IsoSprite legsSprite;
   private boolean bFemale = true;
   public float knockbackAttackMod = 1.0F;
   public final boolean[] IsVisibleToPlayer = new boolean[4];
   public float savedVehicleX;
   public float savedVehicleY;
   public short savedVehicleSeat = -1;
   public boolean savedVehicleRunning;
   private static final float RecoilDelayDecrease = 0.625F;
   protected static final float BeenMovingForIncrease = 1.25F;
   protected static final float BeenMovingForDecrease = 0.625F;
   private IsoGameCharacter FollowingTarget = null;
   private final ArrayList LocalList = new ArrayList();
   private final ArrayList LocalNeutralList = new ArrayList();
   private final ArrayList LocalGroupList = new ArrayList();
   private final ArrayList LocalRelevantEnemyList = new ArrayList();
   private float dangerLevels = 0.0F;
   private static final Vector2 tempVector2 = new Vector2();
   private float leaveBodyTimedown = 0.0F;
   protected boolean AllowConversation = true;
   private float ReanimateTimer;
   private int ReanimAnimFrame;
   private int ReanimAnimDelay;
   private boolean Reanim = false;
   private boolean VisibleToNPCs = true;
   private int DieCount = 0;
   private float llx = 0.0F;
   private float lly = 0.0F;
   private float llz = 0.0F;
   protected int RemoteID = -1;
   protected int NumSurvivorsInVicinity = 0;
   private float LevelUpMultiplier = 2.5F;
   protected IsoGameCharacter.XP xp = null;
   private int LastLocalEnemies = 0;
   private final ArrayList VeryCloseEnemyList = new ArrayList();
   private final HashMap LastKnownLocation = new HashMap();
   protected IsoGameCharacter AttackedBy = null;
   protected boolean IgnoreStaggerBack = false;
   protected boolean AttackWasSuperAttack = false;
   private int TimeThumping = 0;
   private int PatienceMax = 150;
   private int PatienceMin = 20;
   private int Patience = 20;
   protected final Stack CharacterActions = new Stack();
   private int ZombieKills = 0;
   private int SurvivorKills = 0;
   private int LastZombieKills = 0;
   protected boolean superAttack = false;
   protected float ForceWakeUpTime = -1.0F;
   private float fullSpeedMod = 1.0F;
   protected float runSpeedModifier = 1.0F;
   private float walkSpeedModifier = 1.0F;
   private float combatSpeedModifier = 1.0F;
   private boolean bRangedWeaponEmpty = false;
   public ArrayList bagsWorn;
   protected boolean ForceWakeUp;
   protected final BodyDamage BodyDamage;
   private BodyDamage BodyDamageRemote = null;
   private State defaultState;
   protected WornItems wornItems = null;
   protected AttachedItems attachedItems = null;
   protected ClothingWetness clothingWetness = null;
   protected SurvivorDesc descriptor;
   private final Stack FamiliarBuildings = new Stack();
   protected final AStarPathFinderResult finder = new AStarPathFinderResult();
   private float FireKillRate = 0.0038F;
   private int FireSpreadProbability = 6;
   protected float Health = 1.0F;
   protected boolean bDead = false;
   protected boolean bKill = false;
   protected boolean bPlayingDeathSound = false;
   private boolean bDeathDragDown = false;
   protected String hurtSound = "MaleZombieHurt";
   protected ItemContainer inventory = new ItemContainer();
   protected InventoryItem leftHandItem;
   private int NextWander = 200;
   private boolean OnFire = false;
   private int pathIndex = 0;
   protected InventoryItem rightHandItem;
   protected Color SpeakColour = new Color(1.0F, 1.0F, 1.0F, 1.0F);
   protected float slowFactor = 0.0F;
   protected float slowTimer = 0.0F;
   protected boolean bUseParts = false;
   protected boolean Speaking = false;
   private float SpeakTime = 0.0F;
   private float staggerTimeMod = 1.0F;
   protected final StateMachine stateMachine;
   protected final Moodles Moodles;
   protected final Stats stats = new Stats();
   private final Stack UsedItemsOn = new Stack();
   protected HandWeapon useHandWeapon = null;
   protected IsoGridSquare attackTargetSquare;
   private float BloodImpactX = 0.0F;
   private float BloodImpactY = 0.0F;
   private float BloodImpactZ = 0.0F;
   private IsoSprite bloodSplat;
   private boolean bOnBed = false;
   private final Vector2 moveForwardVec = new Vector2();
   protected boolean pathing = false;
   protected ChatElement chatElement;
   private final Stack LocalEnemyList = new Stack();
   protected final Stack EnemyList = new Stack();
   public final IsoGameCharacter.CharacterTraits Traits = new IsoGameCharacter.CharacterTraits();
   private int maxWeight = 8;
   private int maxWeightBase = 8;
   private float SleepingTabletDelta = 1.0F;
   private float BetaEffect = 0.0F;
   private float DepressEffect = 0.0F;
   private float SleepingTabletEffect = 0.0F;
   private float BetaDelta = 0.0F;
   private float DepressDelta = 0.0F;
   private float DepressFirstTakeTime = -1.0F;
   private float PainEffect = 0.0F;
   private float PainDelta = 0.0F;
   private boolean bDoDefer = true;
   private float haloDispTime = 128.0F;
   protected TextDrawObject userName;
   private TextDrawObject haloNote;
   private final HashMap namesPrefix = new HashMap();
   private static final String namePvpSuffix = " [img=media/ui/Skull.png]";
   private static final String nameCarKeySuffix = " [img=media/ui/CarKey.png";
   private static final String voiceSuffix = "[img=media/ui/voiceon.png] ";
   private static final String voiceMuteSuffix = "[img=media/ui/voicemuted.png] ";
   protected IsoPlayer isoPlayer = null;
   private boolean hasInitTextObjects = false;
   private boolean canSeeCurrent = false;
   private boolean drawUserName = false;
   private final IsoGameCharacter.Location LastHeardSound = new IsoGameCharacter.Location(-1, -1, -1);
   private float lrx = 0.0F;
   private float lry = 0.0F;
   protected boolean bClimbing = false;
   private boolean lastCollidedW = false;
   private boolean lastCollidedN = false;
   protected float fallTime = 0.0F;
   protected float lastFallSpeed = 0.0F;
   protected boolean bFalling = false;
   protected BaseVehicle vehicle = null;
   boolean isNPC = false;
   private long lastBump = 0L;
   private IsoGameCharacter bumpedChr = null;
   private boolean m_isCulled = true;
   private int age = 25;
   private int lastHitCount = 0;
   private boolean safety = true;
   private float safetyCooldown = 0.0F;
   private float meleeDelay = 0.0F;
   private float RecoilDelay = 0.0F;
   private float BeenMovingFor = 0.0F;
   private float BeenSprintingFor = 0.0F;
   private boolean forceShove = false;
   private String clickSound = null;
   private float reduceInfectionPower = 0.0F;
   private final List knownRecipes = new ArrayList();
   private int lastHourSleeped = 0;
   protected float timeOfSleep = 0.0F;
   protected float delayToActuallySleep = 0.0F;
   private String bedType = "averageBed";
   private IsoObject bed = null;
   private boolean isReading = false;
   private float timeSinceLastSmoke = 0.0F;
   private boolean wasOnStairs = false;
   private ChatMessage lastChatMessage;
   private String lastSpokenLine;
   private boolean unlimitedEndurance = false;
   private boolean unlimitedCarry = false;
   private boolean buildCheat = false;
   private boolean farmingCheat = false;
   private boolean healthCheat = false;
   private boolean mechanicsCheat = false;
   private boolean movablesCheat = false;
   private boolean timedActionInstantCheat = false;
   private boolean showAdminTag = true;
   private long isAnimForecasted = 0L;
   private boolean fallOnFront = false;
   private boolean hitFromBehind = false;
   private String hitReaction = "";
   private String bumpType = "";
   private boolean m_isBumpDone = false;
   private boolean m_bumpFall = false;
   private boolean m_bumpStaggered = false;
   private String m_bumpFallType = "";
   private int sleepSpeechCnt = 0;
   private Radio equipedRadio;
   private InventoryItem leftHandCache;
   private InventoryItem rightHandCache;
   private final ArrayList ReadBooks = new ArrayList();
   private final IsoGameCharacter.LightInfo lightInfo = new IsoGameCharacter.LightInfo();
   private final IsoGameCharacter.LightInfo lightInfo2 = new IsoGameCharacter.LightInfo();
   private PolygonalMap2.Path path2;
   private final MapKnowledge mapKnowledge = new MapKnowledge();
   public final AttackVars attackVars = new AttackVars();
   public final ArrayList hitList = new ArrayList();
   private final PathFindBehavior2 pfb2 = new PathFindBehavior2(this);
   private final InventoryItem[] cacheEquiped = new InventoryItem[2];
   private boolean bAimAtFloor = false;
   protected int m_persistentOutfitId = 0;
   protected boolean m_bPersistentOutfitInit = false;
   private boolean bUpdateModelTextures = false;
   private ModelInstanceTextureCreator textureCreator = null;
   public boolean bUpdateEquippedTextures = false;
   private final ArrayList readyModelData = new ArrayList();
   private boolean sitOnGround = false;
   private boolean ignoreMovement = false;
   private boolean hideWeaponModel = false;
   private boolean isAiming = false;
   private float beardGrowTiming = -1.0F;
   private float hairGrowTiming = -1.0F;
   private float m_moveDelta = 1.0F;
   protected float m_turnDeltaNormal = 1.0F;
   protected float m_turnDeltaRunning = 0.8F;
   protected float m_turnDeltaSprinting = 0.75F;
   private float m_maxTwist = 15.0F;
   private boolean m_isMoving = false;
   private boolean m_isTurning = false;
   private boolean m_isTurningAround = false;
   private boolean m_isTurning90 = false;
   public long lastAutomaticShoot = 0L;
   public int shootInARow = 0;
   private boolean invincible = false;
   private float lungeFallTimer = 0.0F;
   private SleepingEventData m_sleepingEventData;
   private final int HAIR_GROW_TIME = 20;
   private final int BEARD_GROW_TIME = 5;
   public float realx = 0.0F;
   public float realy = 0.0F;
   public byte realz = 0;
   public NetworkVariables.ZombieState realState;
   public IsoDirections realdir;
   public String overridePrimaryHandModel;
   public String overrideSecondaryHandModel;
   public boolean forceNullOverride;
   protected final UpdateLimit ulBeatenVehicle;
   private float m_momentumScalar;
   private final HashMap m_stateUpdateLookup;
   private boolean attackAnim;
   private NetworkTeleport teleport;
   public ArrayList invRadioFreq;
   private final PredicatedFileWatcher m_animStateTriggerWatcher;
   private final AnimationPlayerRecorder m_animationRecorder;
   private final String m_UID;
   private boolean m_bDebugVariablesRegistered;
   private float effectiveEdibleBuffTimer;
   private float m_shadowFM;
   private float m_shadowBM;
   private long shadowTick;
   private static final ItemVisuals tempItemVisuals = new ItemVisuals();
   private static final ArrayList movingStatic = new ArrayList();
   private long m_muzzleFlash;
   private static final IsoGameCharacter.Bandages s_bandages = new IsoGameCharacter.Bandages();
   private static final Vector3 tempVector = new Vector3();
   private static final Vector3 tempVectorBonePos = new Vector3();
   public final NetworkCharacter networkCharacter;

   public IsoGameCharacter(IsoCell var1, float var2, float var3, float var4) {
      super(var1, false);
      this.realState = NetworkVariables.ZombieState.Idle;
      this.realdir = IsoDirections.fromIndex(0);
      this.overridePrimaryHandModel = null;
      this.overrideSecondaryHandModel = null;
      this.forceNullOverride = false;
      this.ulBeatenVehicle = new UpdateLimit(200L);
      this.m_momentumScalar = 0.0F;
      this.m_stateUpdateLookup = new HashMap();
      this.attackAnim = false;
      this.teleport = null;
      this.invRadioFreq = new ArrayList();
      this.m_bDebugVariablesRegistered = false;
      this.effectiveEdibleBuffTimer = 0.0F;
      this.m_shadowFM = 0.0F;
      this.m_shadowBM = 0.0F;
      this.shadowTick = -1L;
      this.m_muzzleFlash = -1L;
      this.networkCharacter = new NetworkCharacter();
      this.m_UID = String.format("%s-%s", this.getClass().getSimpleName(), UUID.randomUUID().toString());
      this.registerVariableCallbacks();
      this.instancename = "Character" + IID;
      ++IID;
      if (!(this instanceof IsoSurvivor)) {
         this.emitter = (BaseCharacterSoundEmitter)(!Core.SoundDisabled && !GameServer.bServer ? new CharacterSoundEmitter(this) : new DummyCharacterSoundEmitter(this));
      } else {
         this.emitter = null;
      }

      if (var2 != 0.0F || var3 != 0.0F || var4 != 0.0F) {
         if (this.getCell().isSafeToAdd()) {
            this.getCell().getObjectList().add(this);
         } else {
            this.getCell().getAddList().add(this);
         }
      }

      if (this.def == null) {
         this.def = IsoSpriteInstance.get(this.sprite);
      }

      if (this instanceof IsoPlayer) {
         this.BodyDamage = new BodyDamage(this);
         this.Moodles = new Moodles(this);
         this.xp = new IsoGameCharacter.XP(this);
      } else {
         this.BodyDamage = null;
         this.Moodles = null;
         this.xp = null;
      }

      this.Patience = Rand.Next(this.PatienceMin, this.PatienceMax);
      this.x = var2 + 0.5F;
      this.y = var3 + 0.5F;
      this.z = var4;
      this.scriptnx = this.lx = this.nx = var2;
      this.scriptny = this.ly = this.ny = var3;
      if (var1 != null) {
         this.current = this.getCell().getGridSquare((int)var2, (int)var3, (int)var4);
      }

      this.offsetY = 0.0F;
      this.offsetX = 0.0F;
      this.stateMachine = new StateMachine(this);
      this.setDefaultState(IdleState.instance());
      this.inventory.parent = this;
      this.inventory.setExplored(true);
      this.chatElement = new ChatElement(this, 1, "character");
      if (GameClient.bClient || GameServer.bServer) {
         this.namesPrefix.put("admin", "[col=255,0,0]Admin[/] ");
         this.namesPrefix.put("moderator", "[col=0,128,47]Moderator[/] ");
         this.namesPrefix.put("overseer", "[col=26,26,191]Overseer[/] ");
         this.namesPrefix.put("gm", "[col=213,123,23]GM[/] ");
         this.namesPrefix.put("observer", "[col=128,128,128]Observer[/] ");
      }

      this.m_animationRecorder = new AnimationPlayerRecorder(this);
      this.advancedAnimator = new AdvancedAnimator();
      this.advancedAnimator.init(this);
      this.advancedAnimator.animCallbackHandlers.add(this);
      this.advancedAnimator.SetAnimSet(AnimationSet.GetAnimationSet(this.GetAnimSetName(), false));
      this.advancedAnimator.setRecorder(this.m_animationRecorder);
      this.actionContext.onStateChanged.add(this);
      this.m_animStateTriggerWatcher = new PredicatedFileWatcher(ZomboidFileSystem.instance.getMessagingDirSub("Trigger_SetAnimState.xml"), AnimStateTriggerXmlFile.class, this::onTrigger_setAnimStateToTriggerFile);
   }

   private void registerVariableCallbacks() {
      this.setVariable("hitreaction", this::getHitReaction, this::setHitReaction);
      this.setVariable("collidetype", this::getCollideType, this::setCollideType);
      this.setVariable("footInjuryType", this::getFootInjuryType);
      this.setVariable("bumptype", this::getBumpType, this::setBumpType);
      this.setVariable("sitonground", this::isSitOnGround, this::setSitOnGround);
      this.setVariable("canclimbdownrope", this::canClimbDownSheetRopeInCurrentSquare);
      this.setVariable("frombehind", this::isHitFromBehind, this::setHitFromBehind);
      this.setVariable("fallonfront", this::isFallOnFront, this::setFallOnFront);
      this.setVariable("hashitreaction", this::hasHitReaction);
      this.setVariable("intrees", this::isInTreesNoBush);
      this.setVariable("bumped", this::isBumped);
      this.setVariable("BumpDone", false, this::isBumpDone, this::setBumpDone);
      this.setVariable("BumpFall", false, this::isBumpFall, this::setBumpFall);
      this.setVariable("BumpFallType", "", this::getBumpFallType, this::setBumpFallType);
      this.setVariable("BumpStaggered", false, this::isBumpStaggered, this::setBumpStaggered);
      this.setVariable("bonfloor", this::isOnFloor, this::setOnFloor);
      this.setVariable("rangedweaponempty", this::isRangedWeaponEmpty, this::setRangedWeaponEmpty);
      this.setVariable("footInjury", this::hasFootInjury);
      this.setVariable("ChopTreeSpeed", 1.0F, this::getChopTreeSpeed);
      this.setVariable("MoveDelta", 1.0F, this::getMoveDelta, this::setMoveDelta);
      this.setVariable("TurnDelta", 1.0F, this::getTurnDelta, this::setTurnDelta);
      this.setVariable("angle", this::getDirectionAngle, this::setDirectionAngle);
      this.setVariable("animAngle", this::getAnimAngle);
      this.setVariable("twist", this::getTwist);
      this.setVariable("targetTwist", this::getTargetTwist);
      this.setVariable("maxTwist", this.m_maxTwist, this::getMaxTwist, this::setMaxTwist);
      this.setVariable("shoulderTwist", this::getShoulderTwist);
      this.setVariable("excessTwist", this::getExcessTwist);
      this.setVariable("angleStepDelta", this::getAnimAngleStepDelta);
      this.setVariable("angleTwistDelta", this::getAnimAngleTwistDelta);
      this.setVariable("isTurning", false, this::isTurning, this::setTurning);
      this.setVariable("isTurning90", false, this::isTurning90, this::setTurning90);
      this.setVariable("isTurningAround", false, this::isTurningAround, this::setTurningAround);
      this.setVariable("bMoving", false, this::isMoving, this::setMoving);
      this.setVariable("beenMovingFor", this::getBeenMovingFor);
      this.setVariable("previousState", this::getPreviousActionContextStateName);
      this.setVariable("momentumScalar", this::getMomentumScalar, this::setMomentumScalar);
      this.setVariable("hasTimedActions", this::hasTimedActions);
      if (DebugOptions.instance.Character.Debug.RegisterDebugVariables.getValue()) {
         this.registerDebugGameVariables();
      }

      this.setVariable("CriticalHit", this::isCriticalHit, this::setCriticalHit);
      this.setVariable("bKnockedDown", this::isKnockedDown, this::setKnockedDown);
   }

   public void updateRecoilVar() {
      this.setVariable("recoilVarY", 0.0F);
      this.setVariable("recoilVarX", 0.0F + (float)this.getPerkLevel(PerkFactory.Perks.Aiming) / 10.0F);
   }

   private void registerDebugGameVariables() {
      for(int var1 = 0; var1 < 2; ++var1) {
         for(int var2 = 0; var2 < 9; ++var2) {
            this.dbgRegisterAnimTrackVariable(var1, var2);
         }
      }

      this.setVariable("dbg.anm.dx", () -> {
         return this.getDeferredMovement(tempo).x / GameTime.instance.getMultiplier();
      });
      this.setVariable("dbg.anm.dy", () -> {
         return this.getDeferredMovement(tempo).y / GameTime.instance.getMultiplier();
      });
      this.setVariable("dbg.anm.da", () -> {
         return this.getDeferredAngleDelta() / GameTime.instance.getMultiplier();
      });
      this.setVariable("dbg.anm.daw", this::getDeferredRotationWeight);
      this.setVariable("dbg.forward", () -> {
         float var10000 = this.getForwardDirection().x;
         return var10000 + "; " + this.getForwardDirection().y;
      });
      this.setVariable("dbg.anm.blend.fbx_x", () -> {
         return DebugOptions.instance.Animation.BlendUseFbx.getValue() ? 1.0F : 0.0F;
      });
      this.m_bDebugVariablesRegistered = true;
   }

   private void dbgRegisterAnimTrackVariable(int var1, int var2) {
      this.setVariable(String.format("dbg.anm.track%d%d", var1, var2), () -> {
         return this.dbgGetAnimTrackName(var1, var2);
      });
      this.setVariable(String.format("dbg.anm.t.track%d%d", var1, var2), () -> {
         return this.dbgGetAnimTrackTime(var1, var2);
      });
      this.setVariable(String.format("dbg.anm.w.track%d%d", var1, var2), () -> {
         return this.dbgGetAnimTrackWeight(var1, var2);
      });
   }

   public float getMomentumScalar() {
      return this.m_momentumScalar;
   }

   public void setMomentumScalar(float var1) {
      this.m_momentumScalar = var1;
   }

   public Vector2 getDeferredMovement(Vector2 var1) {
      if (this.m_animPlayer == null) {
         var1.set(0.0F, 0.0F);
         return var1;
      } else {
         this.m_animPlayer.getDeferredMovement(var1);
         return var1;
      }
   }

   public float getDeferredAngleDelta() {
      return this.m_animPlayer == null ? 0.0F : this.m_animPlayer.getDeferredAngleDelta() * 57.295776F;
   }

   public float getDeferredRotationWeight() {
      return this.m_animPlayer == null ? 0.0F : this.m_animPlayer.getDeferredRotationWeight();
   }

   public boolean isStrafing() {
      return this.getPath2() != null && this.pfb2.isStrafing() ? true : this.isAiming();
   }

   public AnimationTrack dbgGetAnimTrack(int var1, int var2) {
      if (this.m_animPlayer == null) {
         return null;
      } else {
         AnimationPlayer var3 = this.m_animPlayer;
         AnimationMultiTrack var4 = var3.getMultiTrack();
         List var5 = var4.getTracks();
         AnimationTrack var6 = null;
         int var7 = 0;
         int var8 = 0;

         for(int var9 = var5.size(); var7 < var9; ++var7) {
            AnimationTrack var10 = (AnimationTrack)var5.get(var7);
            int var11 = var10.getLayerIdx();
            if (var11 == var1) {
               if (var8 == var2) {
                  var6 = var10;
                  break;
               }

               ++var8;
            }
         }

         return var6;
      }
   }

   public String dbgGetAnimTrackName(int var1, int var2) {
      AnimationTrack var3 = this.dbgGetAnimTrack(var1, var2);
      return var3 != null ? var3.name : "";
   }

   public float dbgGetAnimTrackTime(int var1, int var2) {
      AnimationTrack var3 = this.dbgGetAnimTrack(var1, var2);
      return var3 != null ? var3.getCurrentTime() : 0.0F;
   }

   public float dbgGetAnimTrackWeight(int var1, int var2) {
      AnimationTrack var3 = this.dbgGetAnimTrack(var1, var2);
      return var3 != null ? var3.BlendDelta : 0.0F;
   }

   public float getTwist() {
      return this.m_animPlayer != null ? 57.295776F * this.m_animPlayer.getTwistAngle() : 0.0F;
   }

   public float getShoulderTwist() {
      return this.m_animPlayer != null ? 57.295776F * this.m_animPlayer.getShoulderTwistAngle() : 0.0F;
   }

   public float getMaxTwist() {
      return this.m_maxTwist;
   }

   public void setMaxTwist(float var1) {
      this.m_maxTwist = var1;
   }

   public float getExcessTwist() {
      return this.m_animPlayer != null ? 57.295776F * this.m_animPlayer.getExcessTwistAngle() : 0.0F;
   }

   public float getAbsoluteExcessTwist() {
      return Math.abs(this.getExcessTwist());
   }

   public float getAnimAngleTwistDelta() {
      return this.m_animPlayer != null ? this.m_animPlayer.angleTwistDelta : 0.0F;
   }

   public float getAnimAngleStepDelta() {
      return this.m_animPlayer != null ? this.m_animPlayer.angleStepDelta : 0.0F;
   }

   public float getTargetTwist() {
      return this.m_animPlayer != null ? 57.295776F * this.m_animPlayer.getTargetTwistAngle() : 0.0F;
   }

   public boolean isRangedWeaponEmpty() {
      return this.bRangedWeaponEmpty;
   }

   public void setRangedWeaponEmpty(boolean var1) {
      this.bRangedWeaponEmpty = var1;
   }

   public boolean hasFootInjury() {
      return !StringUtils.isNullOrWhitespace(this.getFootInjuryType());
   }

   public boolean isInTrees2(boolean var1) {
      if (this.isCurrentState(BumpedState.instance())) {
         return false;
      } else {
         IsoGridSquare var2 = this.getCurrentSquare();
         if (var2 == null) {
            return false;
         } else {
            if (var2.Has(IsoObjectType.tree)) {
               IsoTree var3 = var2.getTree();
               if (var3 == null || var1 && var3.getSize() > 2 || !var1) {
                  return true;
               }
            }

            String var4 = var2.getProperties().Val("Movement");
            if (!"HedgeLow".equalsIgnoreCase(var4) && !"HedgeHigh".equalsIgnoreCase(var4)) {
               return !var1 && var2.getProperties().Is("Bush");
            } else {
               return true;
            }
         }
      }
   }

   public boolean isInTreesNoBush() {
      return this.isInTrees2(true);
   }

   public boolean isInTrees() {
      return this.isInTrees2(false);
   }

   public static HashMap getSurvivorMap() {
      return SurvivorMap;
   }

   public static int[] getLevelUpLevels() {
      return LevelUpLevels;
   }

   public static Vector2 getTempo() {
      return tempo;
   }

   public static ColorInfo getInf() {
      return inf;
   }

   public GameCharacterAIBrain getBrain() {
      return this.GameCharacterAIBrain;
   }

   public boolean getIsNPC() {
      return this.isNPC;
   }

   public void setIsNPC(boolean var1) {
      this.isNPC = var1;
   }

   public BaseCharacterSoundEmitter getEmitter() {
      return this.emitter;
   }

   public void updateEmitter() {
      this.getFMODParameters().update();
      if (IsoWorld.instance.emitterUpdate || this.emitter.hasSoundsToStart()) {
         this.emitter.set(this.x, this.y, this.z);
         this.emitter.tick();
      }
   }

   protected void doDeferredMovement() {
      if (GameClient.bClient && this.getHitReactionNetworkAI() != null) {
         if (this.getHitReactionNetworkAI().isStarted()) {
            this.getHitReactionNetworkAI().move();
            return;
         }

         if (this.isDead() && this.getHitReactionNetworkAI().isDoSkipMovement()) {
            return;
         }
      }

      AnimationPlayer var1 = this.getAnimationPlayer();
      if (var1 != null) {
         if (this.getPath2() != null && !this.isCurrentState(ClimbOverFenceState.instance()) && !this.isCurrentState(ClimbThroughWindowState.instance())) {
            if (this.isCurrentState(WalkTowardState.instance())) {
               DebugLog.General.warn("WalkTowardState but path2 != null");
               this.setPath2((PolygonalMap2.Path)null);
            }

         } else {
            if (GameClient.bClient) {
               if (this instanceof IsoZombie && ((IsoZombie)this).isRemoteZombie()) {
                  if (this.getCurrentState() != ClimbOverFenceState.instance() && this.getCurrentState() != ClimbThroughWindowState.instance() && this.getCurrentState() != ClimbOverWallState.instance() && this.getCurrentState() != StaggerBackState.instance() && this.getCurrentState() != ZombieHitReactionState.instance() && this.getCurrentState() != ZombieFallDownState.instance() && this.getCurrentState() != ZombieFallingState.instance() && this.getCurrentState() != ZombieOnGroundState.instance() && this.getCurrentState() != AttackNetworkState.instance()) {
                     return;
                  }
               } else if (this instanceof IsoPlayer && !((IsoPlayer)this).isLocalPlayer() && !this.isCurrentState(CollideWithWallState.instance()) && !this.isCurrentState(PlayerGetUpState.instance()) && !this.isCurrentState(BumpedState.instance())) {
                  return;
               }
            }

            Vector2 var2 = tempo;
            this.getDeferredMovement(var2);
            if (GameClient.bClient && this instanceof IsoZombie && this.isCurrentState(StaggerBackState.instance())) {
               float var3 = var2.getLength();
               var2.set(this.getHitDir());
               var2.setLength(var3);
            }

            this.MoveUnmodded(var2);
         }
      }
   }

   public ActionContext getActionContext() {
      return null;
   }

   public String getPreviousActionContextStateName() {
      ActionContext var1 = this.getActionContext();
      return var1 == null ? "" : var1.getPreviousStateName();
   }

   public String getCurrentActionContextStateName() {
      ActionContext var1 = this.getActionContext();
      return var1 == null ? "" : var1.getCurrentStateName();
   }

   public boolean hasAnimationPlayer() {
      return this.m_animPlayer != null;
   }

   public AnimationPlayer getAnimationPlayer() {
      Model var1 = ModelManager.instance.getBodyModel(this);
      boolean var2 = false;
      if (this.m_animPlayer != null && this.m_animPlayer.getModel() != var1) {
         var2 = this.m_animPlayer.getMultiTrack().getTrackCount() > 0;
         this.m_animPlayer = (AnimationPlayer)Pool.tryRelease((IPooledObject)this.m_animPlayer);
      }

      if (this.m_animPlayer == null) {
         this.m_animPlayer = AnimationPlayer.alloc(var1);
         this.onAnimPlayerCreated(this.m_animPlayer);
         if (var2) {
            this.getAdvancedAnimator().OnAnimDataChanged(false);
         }
      }

      return this.m_animPlayer;
   }

   public void releaseAnimationPlayer() {
      this.m_animPlayer = (AnimationPlayer)Pool.tryRelease((IPooledObject)this.m_animPlayer);
   }

   protected void onAnimPlayerCreated(AnimationPlayer var1) {
      var1.setRecorder(this.m_animationRecorder);
      var1.setTwistBones("Bip01_Pelvis", "Bip01_Spine", "Bip01_Spine1", "Bip01_Neck", "Bip01_Head");
      var1.setCounterRotationBone("Bip01");
   }

   protected void updateAnimationRecorderState() {
      if (this.m_animPlayer != null) {
         if (IsoWorld.isAnimRecorderDiscardTriggered()) {
            this.m_animPlayer.discardRecording();
         }

         boolean var1 = IsoWorld.isAnimRecorderActive();
         boolean var2 = var1 && !this.isSceneCulled();
         if (var2) {
            this.getAnimationPlayerRecorder().logCharacterPos();
         }

         this.m_animPlayer.setRecording(var2);
      }
   }

   public AdvancedAnimator getAdvancedAnimator() {
      return this.advancedAnimator;
   }

   public ModelInstance getModelInstance() {
      if (this.legsSprite == null) {
         return null;
      } else {
         return this.legsSprite.modelSlot == null ? null : this.legsSprite.modelSlot.model;
      }
   }

   public String getCurrentStateName() {
      return this.stateMachine.getCurrent() == null ? null : this.stateMachine.getCurrent().getName();
   }

   public String getPreviousStateName() {
      return this.stateMachine.getPrevious() == null ? null : this.stateMachine.getPrevious().getName();
   }

   public String getAnimationDebug() {
      if (this.advancedAnimator != null) {
         String var10000 = this.instancename;
         return var10000 + "\n" + this.advancedAnimator.GetDebug();
      } else {
         return this.instancename + "\n - No Animator";
      }
   }

   public String getTalkerType() {
      return this.chatElement.getTalkerType();
   }

   public boolean isAnimForecasted() {
      return System.currentTimeMillis() < this.isAnimForecasted;
   }

   public void setAnimForecasted(int var1) {
      this.isAnimForecasted = System.currentTimeMillis() + (long)var1;
   }

   public void resetModel() {
      ModelManager.instance.Reset(this);
   }

   public void resetModelNextFrame() {
      ModelManager.instance.ResetNextFrame(this);
   }

   protected void onTrigger_setClothingToXmlTriggerFile(TriggerXmlFile var1) {
      OutfitManager.Reload();
      if (!StringUtils.isNullOrWhitespace(var1.outfitName)) {
         String var2 = var1.outfitName;
         DebugLog.Clothing.debugln("Desired outfit name: " + var2);
         Outfit var3;
         if (var1.isMale) {
            var3 = OutfitManager.instance.FindMaleOutfit(var2);
         } else {
            var3 = OutfitManager.instance.FindFemaleOutfit(var2);
         }

         if (var3 == null) {
            DebugLog.Clothing.error("Could not find outfit: " + var2);
            return;
         }

         if (this.bFemale == var1.isMale && this instanceof IHumanVisual) {
            ((IHumanVisual)this).getHumanVisual().clear();
         }

         this.bFemale = !var1.isMale;
         if (this.descriptor != null) {
            this.descriptor.setFemale(this.bFemale);
         }

         this.dressInNamedOutfit(var3.m_Name);
         this.advancedAnimator.OnAnimDataChanged(false);
         if (this instanceof IsoPlayer) {
            LuaEventManager.triggerEvent("OnClothingUpdated", this);
         }
      } else if (!StringUtils.isNullOrWhitespace(var1.clothingItemGUID)) {
         this.dressInClothingItem(var1.clothingItemGUID);
         if (this instanceof IsoPlayer) {
            LuaEventManager.triggerEvent("OnClothingUpdated", this);
         }
      }

      ModelManager.instance.Reset(this);
   }

   protected void onTrigger_setAnimStateToTriggerFile(AnimStateTriggerXmlFile var1) {
      String var2 = this.GetAnimSetName();
      if (!StringUtils.equalsIgnoreCase(var2, var1.animSet)) {
         this.setVariable("dbgForceAnim", false);
         this.restoreAnimatorStateToActionContext();
      } else {
         DebugOptions.instance.Animation.AnimLayer.AllowAnimNodeOverride.setValue(var1.forceAnim);
         if (this.advancedAnimator.containsState(var1.stateName)) {
            this.setVariable("dbgForceAnim", var1.forceAnim);
            this.setVariable("dbgForceAnimStateName", var1.stateName);
            this.setVariable("dbgForceAnimNodeName", var1.nodeName);
            this.setVariable("dbgForceAnimScalars", var1.setScalarValues);
            this.setVariable("dbgForceScalar", var1.scalarValue);
            this.setVariable("dbgForceScalar2", var1.scalarValue2);
            this.advancedAnimator.SetState(var1.stateName);
         } else {
            DebugLog.Animation.error("State not found: " + var1.stateName);
            this.restoreAnimatorStateToActionContext();
         }

      }
   }

   private void restoreAnimatorStateToActionContext() {
      if (this.actionContext.getCurrentState() != null) {
         this.advancedAnimator.SetState(this.actionContext.getCurrentStateName(), PZArrayUtil.listConvert(this.actionContext.getChildStates(), (var0) -> {
            return var0.name;
         }));
      }

   }

   public void clothingItemChanged(String var1) {
      if (this.wornItems != null) {
         for(int var2 = 0; var2 < this.wornItems.size(); ++var2) {
            InventoryItem var3 = this.wornItems.getItemByIndex(var2);
            ClothingItem var4 = var3.getClothingItem();
            if (var4 != null && var4.isReady() && var4.m_GUID.equals(var1)) {
               ClothingItemReference var5 = new ClothingItemReference();
               var5.itemGUID = var1;
               var5.randomize();
               var3.getVisual().synchWithOutfit(var5);
               var3.synchWithVisual();
               this.resetModelNextFrame();
            }
         }
      }

   }

   public void reloadOutfit() {
      ModelManager.instance.Reset(this);
   }

   public boolean isSceneCulled() {
      return this.m_isCulled;
   }

   public void setSceneCulled(boolean var1) {
      if (this.isSceneCulled() != var1) {
         try {
            if (var1) {
               ModelManager.instance.Remove(this);
            } else {
               ModelManager.instance.Add(this);
            }
         } catch (Exception var3) {
            System.err.println("Error in IsoGameCharacter.setSceneCulled(" + var1 + "):");
            ExceptionLogger.logException(var3);
            ModelManager.instance.Remove(this);
            this.legsSprite.modelSlot = null;
         }

      }
   }

   public void onCullStateChanged(ModelManager var1, boolean var2) {
      this.m_isCulled = var2;
      if (!var2) {
         this.restoreAnimatorStateToActionContext();
         DebugFileWatcher.instance.add(this.m_animStateTriggerWatcher);
         OutfitManager.instance.addClothingItemListener(this);
      } else {
         DebugFileWatcher.instance.remove(this.m_animStateTriggerWatcher);
         OutfitManager.instance.removeClothingItemListener(this);
      }

   }

   public void dressInRandomOutfit() {
      if (DebugLog.isEnabled(DebugType.Clothing)) {
         DebugLog.Clothing.println("IsoGameCharacter.dressInRandomOutfit>");
      }

      Outfit var1 = OutfitManager.instance.GetRandomOutfit(this.isFemale());
      if (var1 != null) {
         this.dressInNamedOutfit(var1.m_Name);
      }

   }

   public void dressInNamedOutfit(String var1) {
   }

   public void dressInPersistentOutfit(String var1) {
      int var2 = PersistentOutfits.instance.pickOutfit(var1, this.isFemale());
      this.dressInPersistentOutfitID(var2);
   }

   public void dressInPersistentOutfitID(int var1) {
   }

   public String getOutfitName() {
      if (this instanceof IHumanVisual) {
         HumanVisual var1 = ((IHumanVisual)this).getHumanVisual();
         Outfit var2 = var1.getOutfit();
         return var2 == null ? null : var2.m_Name;
      } else {
         return null;
      }
   }

   public void dressInClothingItem(String var1) {
   }

   public Outfit getRandomDefaultOutfit() {
      IsoGridSquare var1 = this.getCurrentSquare();
      IsoRoom var2 = var1 == null ? null : var1.getRoom();
      String var3 = var2 == null ? null : var2.getName();
      return ZombiesZoneDefinition.getRandomDefaultOutfit(this.isFemale(), var3);
   }

   public ModelInstance getModel() {
      return this.legsSprite != null && this.legsSprite.modelSlot != null ? this.legsSprite.modelSlot.model : null;
   }

   public boolean hasActiveModel() {
      return this.legsSprite != null && this.legsSprite.hasActiveModel();
   }

   public boolean hasItems(String var1, int var2) {
      int var3 = this.inventory.getItemCount(var1);
      return var2 <= var3;
   }

   public int getLevelUpLevels(int var1) {
      return LevelUpLevels.length <= var1 ? LevelUpLevels[LevelUpLevels.length - 1] : LevelUpLevels[var1];
   }

   public int getLevelMaxForXp() {
      return LevelUpLevels.length;
   }

   public int getXpForLevel(int var1) {
      return var1 < LevelUpLevels.length ? (int)((float)LevelUpLevels[var1] * this.LevelUpMultiplier) : (int)((float)(LevelUpLevels[LevelUpLevels.length - 1] + (var1 - LevelUpLevels.length + 1) * 400) * this.LevelUpMultiplier);
   }

   public void DoDeath(HandWeapon var1, IsoGameCharacter var2) {
      this.DoDeath(var1, var2, true);
   }

   public void DoDeath(HandWeapon var1, IsoGameCharacter var2, boolean var3) {
      this.OnDeath();
      if (this.getAttackedBy() instanceof IsoPlayer && GameServer.bServer && this instanceof IsoPlayer) {
         String var4 = "";
         String var5 = "";
         if (SteamUtils.isSteamModeEnabled()) {
            var4 = " (" + ((IsoPlayer)this.getAttackedBy()).getSteamID() + ") ";
            var5 = " (" + ((IsoPlayer)this).getSteamID() + ") ";
         }

         LoggerManager.getLogger("pvp").write("user " + ((IsoPlayer)this.getAttackedBy()).username + var4 + " killed " + ((IsoPlayer)this).username + var5 + " " + LoggerManager.getPlayerCoords((IsoPlayer)this), "IMPORTANT");
         if (ServerOptions.instance.AnnounceDeath.getValue()) {
            ChatServer.getInstance().sendMessageToServerChat(((IsoPlayer)this.getAttackedBy()).username + " killed " + ((IsoPlayer)this).username + ".");
         }

         ChatServer.getInstance().sendMessageToAdminChat("user " + ((IsoPlayer)this.getAttackedBy()).username + " killed " + ((IsoPlayer)this).username);
      } else {
         if (GameServer.bServer && this instanceof IsoPlayer) {
            ZLogger var10000 = LoggerManager.getLogger("user");
            String var10001 = ((IsoPlayer)this).username;
            var10000.write("user " + var10001 + " died at " + LoggerManager.getPlayerCoords((IsoPlayer)this) + " (non pvp)");
         }

         if (ServerOptions.instance.AnnounceDeath.getValue() && this instanceof IsoPlayer && GameServer.bServer) {
            ChatServer.getInstance().sendMessageToServerChat(((IsoPlayer)this).username + " is dead.");
         }
      }

      if (this.isDead()) {
         float var9 = 0.5F;
         if (this.isZombie() && (((IsoZombie)this).bCrawling || this.getCurrentState() == ZombieOnGroundState.instance())) {
            var9 = 0.2F;
         }

         if (GameServer.bServer && var3) {
            boolean var10 = this.isOnFloor() && var2 instanceof IsoPlayer && var1 != null && "BareHands".equals(var1.getType());
            GameServer.sendBloodSplatter(var1, this.getX(), this.getY(), this.getZ() + var9, this.getHitDir(), this.isCloseKilled(), var10);
         }

         int var6;
         int var11;
         if (var1 != null && SandboxOptions.instance.BloodLevel.getValue() > 1 && var3) {
            var11 = var1.getSplatNumber();
            if (var11 < 1) {
               var11 = 1;
            }

            if (Core.bLastStand) {
               var11 *= 3;
            }

            switch(SandboxOptions.instance.BloodLevel.getValue()) {
            case 2:
               var11 /= 2;
            case 3:
            default:
               break;
            case 4:
               var11 *= 2;
               break;
            case 5:
               var11 *= 5;
            }

            for(var6 = 0; var6 < var11; ++var6) {
               this.splatBlood(3, 0.3F);
            }
         }

         if (var1 != null && SandboxOptions.instance.BloodLevel.getValue() > 1 && var3) {
            this.splatBloodFloorBig();
         }

         if (var2 != null && var2.xp != null) {
            var2.xp.AddXP(var1, 3);
         }

         if (SandboxOptions.instance.BloodLevel.getValue() > 1 && this.isOnFloor() && var2 instanceof IsoPlayer && var1 == ((IsoPlayer)var2).bareHands && var3) {
            this.playBloodSplatterSound();

            for(var11 = -1; var11 <= 1; ++var11) {
               for(var6 = -1; var6 <= 1; ++var6) {
                  if (var11 != 0 || var6 != 0) {
                     new IsoZombieGiblets(IsoZombieGiblets.GibletType.A, this.getCell(), this.getX(), this.getY(), this.getZ() + var9, (float)var11 * Rand.Next(0.25F, 0.5F), (float)var6 * Rand.Next(0.25F, 0.5F));
                  }
               }
            }

            new IsoZombieGiblets(IsoZombieGiblets.GibletType.Eye, this.getCell(), this.getX(), this.getY(), this.getZ() + var9, this.getHitDir().x * 0.8F, this.getHitDir().y * 0.8F);
         } else if (SandboxOptions.instance.BloodLevel.getValue() > 1 && var3) {
            this.playBloodSplatterSound();
            new IsoZombieGiblets(IsoZombieGiblets.GibletType.A, this.getCell(), this.getX(), this.getY(), this.getZ() + var9, this.getHitDir().x * 1.5F, this.getHitDir().y * 1.5F);
            tempo.x = this.getHitDir().x;
            tempo.y = this.getHitDir().y;
            byte var13 = 3;
            byte var12 = 0;
            byte var7 = 1;
            switch(SandboxOptions.instance.BloodLevel.getValue()) {
            case 1:
               var7 = 0;
               break;
            case 2:
               var7 = 1;
               var13 = 5;
               var12 = 2;
            case 3:
            default:
               break;
            case 4:
               var7 = 3;
               var13 = 2;
               break;
            case 5:
               var7 = 10;
               var13 = 0;
            }

            for(int var8 = 0; var8 < var7; ++var8) {
               if (Rand.Next(this.isCloseKilled() ? 8 : var13) == 0) {
                  new IsoZombieGiblets(IsoZombieGiblets.GibletType.A, this.getCell(), this.getX(), this.getY(), this.getZ() + var9, this.getHitDir().x * 1.5F, this.getHitDir().y * 1.5F);
               }

               if (Rand.Next(this.isCloseKilled() ? 8 : var13) == 0) {
                  new IsoZombieGiblets(IsoZombieGiblets.GibletType.A, this.getCell(), this.getX(), this.getY(), this.getZ() + var9, this.getHitDir().x * 1.5F, this.getHitDir().y * 1.5F);
               }

               if (Rand.Next(this.isCloseKilled() ? 8 : var13) == 0) {
                  new IsoZombieGiblets(IsoZombieGiblets.GibletType.A, this.getCell(), this.getX(), this.getY(), this.getZ() + var9, this.getHitDir().x * 1.8F, this.getHitDir().y * 1.8F);
               }

               if (Rand.Next(this.isCloseKilled() ? 8 : var13) == 0) {
                  new IsoZombieGiblets(IsoZombieGiblets.GibletType.A, this.getCell(), this.getX(), this.getY(), this.getZ() + var9, this.getHitDir().x * 1.9F, this.getHitDir().y * 1.9F);
               }

               if (Rand.Next(this.isCloseKilled() ? 4 : var12) == 0) {
                  new IsoZombieGiblets(IsoZombieGiblets.GibletType.A, this.getCell(), this.getX(), this.getY(), this.getZ() + var9, this.getHitDir().x * 3.5F, this.getHitDir().y * 3.5F);
               }

               if (Rand.Next(this.isCloseKilled() ? 4 : var12) == 0) {
                  new IsoZombieGiblets(IsoZombieGiblets.GibletType.A, this.getCell(), this.getX(), this.getY(), this.getZ() + var9, this.getHitDir().x * 3.8F, this.getHitDir().y * 3.8F);
               }

               if (Rand.Next(this.isCloseKilled() ? 4 : var12) == 0) {
                  new IsoZombieGiblets(IsoZombieGiblets.GibletType.A, this.getCell(), this.getX(), this.getY(), this.getZ() + var9, this.getHitDir().x * 3.9F, this.getHitDir().y * 3.9F);
               }

               if (Rand.Next(this.isCloseKilled() ? 4 : var12) == 0) {
                  new IsoZombieGiblets(IsoZombieGiblets.GibletType.A, this.getCell(), this.getX(), this.getY(), this.getZ() + var9, this.getHitDir().x * 1.5F, this.getHitDir().y * 1.5F);
               }

               if (Rand.Next(this.isCloseKilled() ? 4 : var12) == 0) {
                  new IsoZombieGiblets(IsoZombieGiblets.GibletType.A, this.getCell(), this.getX(), this.getY(), this.getZ() + var9, this.getHitDir().x * 3.8F, this.getHitDir().y * 3.8F);
               }

               if (Rand.Next(this.isCloseKilled() ? 4 : var12) == 0) {
                  new IsoZombieGiblets(IsoZombieGiblets.GibletType.A, this.getCell(), this.getX(), this.getY(), this.getZ() + var9, this.getHitDir().x * 3.9F, this.getHitDir().y * 3.9F);
               }

               if (Rand.Next(this.isCloseKilled() ? 9 : 6) == 0) {
                  new IsoZombieGiblets(IsoZombieGiblets.GibletType.Eye, this.getCell(), this.getX(), this.getY(), this.getZ() + var9, this.getHitDir().x * 0.8F, this.getHitDir().y * 0.8F);
               }
            }
         }
      }

      if (this.isDoDeathSound()) {
         this.playDeadSound();
      }

      this.setDoDeathSound(false);
   }

   private boolean TestIfSeen(int var1) {
      IsoPlayer var2 = IsoPlayer.players[var1];
      if (var2 != null && this != var2 && !GameServer.bServer) {
         float var3 = this.DistToProper(var2);
         if (var3 > GameTime.getInstance().getViewDist()) {
            return false;
         } else {
            boolean var4 = this.current.isCanSee(var1);
            if (!var4 && this.current.isCouldSee(var1)) {
               var4 = var3 < var2.getSeeNearbyCharacterDistance();
            }

            if (!var4) {
               return false;
            } else {
               ColorInfo var5 = this.getCurrentSquare().lighting[var1].lightInfo();
               float var6 = (var5.r + var5.g + var5.b) / 3.0F;
               if (var6 > 0.6F) {
                  var6 = 1.0F;
               }

               float var7 = 1.0F - var3 / GameTime.getInstance().getViewDist();
               if (var6 == 1.0F && var7 > 0.3F) {
                  var7 = 1.0F;
               }

               float var8 = var2.getDotWithForwardDirection(this.getX(), this.getY());
               if (var8 < 0.5F) {
                  var8 = 0.5F;
               }

               var6 *= var8;
               if (var6 < 0.0F) {
                  var6 = 0.0F;
               }

               if (var3 <= 1.0F) {
                  var7 = 1.0F;
                  var6 *= 2.0F;
               }

               var6 *= var7;
               var6 *= 100.0F;
               return var6 > 0.025F;
            }
         }
      } else {
         return false;
      }
   }

   private void DoLand() {
      if (!(this.fallTime < 20.0F) && !this.isClimbing()) {
         if (this instanceof IsoPlayer) {
            if (GameServer.bServer) {
               return;
            }

            if (GameClient.bClient && ((IsoPlayer)this).bRemote) {
               return;
            }

            if (((IsoPlayer)this).isGhostMode()) {
               return;
            }
         }

         if (this.isZombie()) {
            if (this.fallTime > 50.0F) {
               this.hitDir.x = this.hitDir.y = 0.0F;
               if (!((IsoZombie)this).bCrawling && (Rand.Next(100) < 80 || this.fallTime > 80.0F)) {
                  this.setVariable("bHardFall", true);
               }

               this.playHurtSound();
               this.Health -= 0.075F * this.fallTime / 50.0F;
               this.setAttackedBy((IsoGameCharacter)null);
            }

         } else {
            boolean var1 = Rand.Next(80) == 0;
            float var2 = this.fallTime;
            var2 *= Math.min(1.8F, this.getInventory().getCapacityWeight() / this.getInventory().getMaxWeight());
            if (this.getCurrentSquare().getFloor() != null && this.getCurrentSquare().getFloor().getSprite().getName() != null && this.getCurrentSquare().getFloor().getSprite().getName().startsWith("blends_natural")) {
               var2 *= 0.8F;
               if (!var1) {
                  var1 = Rand.Next(65) == 0;
               }
            }

            if (!var1) {
               if (this.Traits.Obese.isSet() || this.Traits.Emaciated.isSet()) {
                  var2 *= 1.4F;
               }

               if (this.Traits.Overweight.isSet() || this.Traits.VeryUnderweight.isSet()) {
                  var2 *= 1.2F;
               }

               var2 *= Math.max(0.1F, 1.0F - (float)this.getPerkLevel(PerkFactory.Perks.Fitness) * 0.1F);
               if (this.fallTime > 135.0F) {
                  var2 = 1000.0F;
               }

               this.BodyDamage.ReduceGeneralHealth(var2);
               if (this.fallTime > 70.0F) {
                  int var3 = 100 - (int)((double)this.fallTime * 0.6D);
                  if (this.getInventory().getMaxWeight() - this.getInventory().getCapacityWeight() < 2.0F) {
                     var3 = (int)((float)var3 - this.getInventory().getCapacityWeight() / this.getInventory().getMaxWeight() * 100.0F / 5.0F);
                  }

                  if (this.Traits.Obese.isSet() || this.Traits.Emaciated.isSet()) {
                     var3 -= 20;
                  }

                  if (this.Traits.Overweight.isSet() || this.Traits.VeryUnderweight.isSet()) {
                     var3 -= 10;
                  }

                  if (this.getPerkLevel(PerkFactory.Perks.Fitness) > 4) {
                     var3 += (this.getPerkLevel(PerkFactory.Perks.Fitness) - 4) * 3;
                  }

                  if (Rand.Next(100) >= var3) {
                     if (!SandboxOptions.instance.BoneFracture.getValue()) {
                        return;
                     }

                     float var4 = (float)Rand.Next(50, 80);
                     if (this.Traits.FastHealer.isSet()) {
                        var4 = (float)Rand.Next(30, 50);
                     } else if (this.Traits.SlowHealer.isSet()) {
                        var4 = (float)Rand.Next(80, 150);
                     }

                     switch(SandboxOptions.instance.InjurySeverity.getValue()) {
                     case 1:
                        var4 *= 0.5F;
                        break;
                     case 3:
                        var4 *= 1.5F;
                     }

                     this.getBodyDamage().getBodyPart(BodyPartType.FromIndex(Rand.Next(BodyPartType.ToIndex(BodyPartType.UpperLeg_L), BodyPartType.ToIndex(BodyPartType.Foot_R) + 1))).setFractureTime(var4);
                  } else if (Rand.Next(100) >= var3 - 10) {
                     this.getBodyDamage().getBodyPart(BodyPartType.FromIndex(Rand.Next(BodyPartType.ToIndex(BodyPartType.UpperLeg_L), BodyPartType.ToIndex(BodyPartType.Foot_R) + 1))).generateDeepWound();
                  }
               }

            }
         }
      }
   }

   public IsoGameCharacter getFollowingTarget() {
      return this.FollowingTarget;
   }

   public void setFollowingTarget(IsoGameCharacter var1) {
      this.FollowingTarget = var1;
   }

   public ArrayList getLocalList() {
      return this.LocalList;
   }

   public ArrayList getLocalNeutralList() {
      return this.LocalNeutralList;
   }

   public ArrayList getLocalGroupList() {
      return this.LocalGroupList;
   }

   public ArrayList getLocalRelevantEnemyList() {
      return this.LocalRelevantEnemyList;
   }

   public float getDangerLevels() {
      return this.dangerLevels;
   }

   public void setDangerLevels(float var1) {
      this.dangerLevels = var1;
   }

   public ArrayList getPerkList() {
      return this.PerkList;
   }

   public float getLeaveBodyTimedown() {
      return this.leaveBodyTimedown;
   }

   public void setLeaveBodyTimedown(float var1) {
      this.leaveBodyTimedown = var1;
   }

   public boolean isAllowConversation() {
      return this.AllowConversation;
   }

   public void setAllowConversation(boolean var1) {
      this.AllowConversation = var1;
   }

   public float getReanimateTimer() {
      return this.ReanimateTimer;
   }

   public void setReanimateTimer(float var1) {
      this.ReanimateTimer = var1;
   }

   public int getReanimAnimFrame() {
      return this.ReanimAnimFrame;
   }

   public void setReanimAnimFrame(int var1) {
      this.ReanimAnimFrame = var1;
   }

   public int getReanimAnimDelay() {
      return this.ReanimAnimDelay;
   }

   public void setReanimAnimDelay(int var1) {
      this.ReanimAnimDelay = var1;
   }

   public boolean isReanim() {
      return this.Reanim;
   }

   public void setReanim(boolean var1) {
      this.Reanim = var1;
   }

   public boolean isVisibleToNPCs() {
      return this.VisibleToNPCs;
   }

   public void setVisibleToNPCs(boolean var1) {
      this.VisibleToNPCs = var1;
   }

   public int getDieCount() {
      return this.DieCount;
   }

   public void setDieCount(int var1) {
      this.DieCount = var1;
   }

   public float getLlx() {
      return this.llx;
   }

   public void setLlx(float var1) {
      this.llx = var1;
   }

   public float getLly() {
      return this.lly;
   }

   public void setLly(float var1) {
      this.lly = var1;
   }

   public float getLlz() {
      return this.llz;
   }

   public void setLlz(float var1) {
      this.llz = var1;
   }

   public int getRemoteID() {
      return this.RemoteID;
   }

   public void setRemoteID(int var1) {
      this.RemoteID = var1;
   }

   public int getNumSurvivorsInVicinity() {
      return this.NumSurvivorsInVicinity;
   }

   public void setNumSurvivorsInVicinity(int var1) {
      this.NumSurvivorsInVicinity = var1;
   }

   public float getLevelUpMultiplier() {
      return this.LevelUpMultiplier;
   }

   public void setLevelUpMultiplier(float var1) {
      this.LevelUpMultiplier = var1;
   }

   public IsoGameCharacter.XP getXp() {
      return this.xp;
   }

   public void setXp(IsoGameCharacter.XP var1) {
      this.xp = var1;
   }

   public int getLastLocalEnemies() {
      return this.LastLocalEnemies;
   }

   public void setLastLocalEnemies(int var1) {
      this.LastLocalEnemies = var1;
   }

   public ArrayList getVeryCloseEnemyList() {
      return this.VeryCloseEnemyList;
   }

   public HashMap getLastKnownLocation() {
      return this.LastKnownLocation;
   }

   public IsoGameCharacter getAttackedBy() {
      return this.AttackedBy;
   }

   public void setAttackedBy(IsoGameCharacter var1) {
      this.AttackedBy = var1;
   }

   public boolean isIgnoreStaggerBack() {
      return this.IgnoreStaggerBack;
   }

   public void setIgnoreStaggerBack(boolean var1) {
      this.IgnoreStaggerBack = var1;
   }

   public boolean isAttackWasSuperAttack() {
      return this.AttackWasSuperAttack;
   }

   public void setAttackWasSuperAttack(boolean var1) {
      this.AttackWasSuperAttack = var1;
   }

   public int getTimeThumping() {
      return this.TimeThumping;
   }

   public void setTimeThumping(int var1) {
      this.TimeThumping = var1;
   }

   public int getPatienceMax() {
      return this.PatienceMax;
   }

   public void setPatienceMax(int var1) {
      this.PatienceMax = var1;
   }

   public int getPatienceMin() {
      return this.PatienceMin;
   }

   public void setPatienceMin(int var1) {
      this.PatienceMin = var1;
   }

   public int getPatience() {
      return this.Patience;
   }

   public void setPatience(int var1) {
      this.Patience = var1;
   }

   public Stack getCharacterActions() {
      return this.CharacterActions;
   }

   public boolean hasTimedActions() {
      return !this.CharacterActions.isEmpty() || this.getVariableBoolean("IsPerformingAnAction");
   }

   public Vector2 getForwardDirection() {
      return this.m_forwardDirection;
   }

   public void setForwardDirection(Vector2 var1) {
      if (var1 != null) {
         this.setForwardDirection(var1.x, var1.y);
      }
   }

   public void setForwardDirection(float var1, float var2) {
      this.m_forwardDirection.x = var1;
      this.m_forwardDirection.y = var2;
   }

   public void zeroForwardDirectionX() {
      this.setForwardDirection(0.0F, 1.0F);
   }

   public void zeroForwardDirectionY() {
      this.setForwardDirection(1.0F, 0.0F);
   }

   public float getDirectionAngle() {
      return 57.295776F * this.getForwardDirection().getDirection();
   }

   public void setDirectionAngle(float var1) {
      float var2 = 0.017453292F * var1;
      Vector2 var3 = this.getForwardDirection();
      var3.setDirection(var2);
   }

   public float getAnimAngle() {
      return this.m_animPlayer != null && this.m_animPlayer.isReady() && !this.m_animPlayer.isBoneTransformsNeedFirstFrame() ? 57.295776F * this.m_animPlayer.getAngle() : this.getDirectionAngle();
   }

   public float getAnimAngleRadians() {
      return this.m_animPlayer != null && this.m_animPlayer.isReady() && !this.m_animPlayer.isBoneTransformsNeedFirstFrame() ? this.m_animPlayer.getAngle() : this.m_forwardDirection.getDirection();
   }

   public Vector2 getAnimVector(Vector2 var1) {
      return var1.setLengthAndDirection(this.getAnimAngleRadians(), 1.0F);
   }

   public float getLookAngleRadians() {
      return this.m_animPlayer != null && this.m_animPlayer.isReady() ? this.m_animPlayer.getAngle() + this.m_animPlayer.getTwistAngle() : this.getForwardDirection().getDirection();
   }

   public Vector2 getLookVector(Vector2 var1) {
      return var1.setLengthAndDirection(this.getLookAngleRadians(), 1.0F);
   }

   public float getDotWithForwardDirection(Vector3 var1) {
      return this.getDotWithForwardDirection(var1.x, var1.y);
   }

   public float getDotWithForwardDirection(float var1, float var2) {
      Vector2 var3 = IsoGameCharacter.L_getDotWithForwardDirection.v1.set(var1 - this.getX(), var2 - this.getY());
      var3.normalize();
      Vector2 var4 = this.getLookVector(IsoGameCharacter.L_getDotWithForwardDirection.v2);
      var4.normalize();
      return var3.dot(var4);
   }

   public boolean isAsleep() {
      return this.Asleep;
   }

   public void setAsleep(boolean var1) {
      this.Asleep = var1;
   }

   public int getZombieKills() {
      return this.ZombieKills;
   }

   public void setZombieKills(int var1) {
      this.ZombieKills = var1;
   }

   public int getLastZombieKills() {
      return this.LastZombieKills;
   }

   public void setLastZombieKills(int var1) {
      this.LastZombieKills = var1;
   }

   public boolean isSuperAttack() {
      return this.superAttack;
   }

   public void setSuperAttack(boolean var1) {
      this.superAttack = var1;
   }

   public float getForceWakeUpTime() {
      return this.ForceWakeUpTime;
   }

   public void setForceWakeUpTime(float var1) {
      this.ForceWakeUpTime = var1;
   }

   public void forceAwake() {
      if (this.isAsleep()) {
         this.ForceWakeUp = true;
      }

   }

   public BodyDamage getBodyDamage() {
      return this.BodyDamage;
   }

   public BodyDamage getBodyDamageRemote() {
      if (this.BodyDamageRemote == null) {
         this.BodyDamageRemote = new BodyDamage((IsoGameCharacter)null);
      }

      return this.BodyDamageRemote;
   }

   public void resetBodyDamageRemote() {
      this.BodyDamageRemote = null;
   }

   public State getDefaultState() {
      return this.defaultState;
   }

   public void setDefaultState(State var1) {
      this.defaultState = var1;
   }

   public SurvivorDesc getDescriptor() {
      return this.descriptor;
   }

   public void setDescriptor(SurvivorDesc var1) {
      this.descriptor = var1;
   }

   public String getFullName() {
      return this.descriptor != null ? this.descriptor.forename + " " + this.descriptor.surname : "Bob Smith";
   }

   public BaseVisual getVisual() {
      throw new RuntimeException("subclasses must implement this");
   }

   public ItemVisuals getItemVisuals() {
      throw new RuntimeException("subclasses must implement this");
   }

   public void getItemVisuals(ItemVisuals var1) {
      this.getWornItems().getItemVisuals(var1);
   }

   public boolean isUsingWornItems() {
      return this.wornItems != null;
   }

   public Stack getFamiliarBuildings() {
      return this.FamiliarBuildings;
   }

   public AStarPathFinderResult getFinder() {
      return this.finder;
   }

   public float getFireKillRate() {
      return this.FireKillRate;
   }

   public void setFireKillRate(float var1) {
      this.FireKillRate = var1;
   }

   public int getFireSpreadProbability() {
      return this.FireSpreadProbability;
   }

   public void setFireSpreadProbability(int var1) {
      this.FireSpreadProbability = var1;
   }

   public float getHealth() {
      return this.Health;
   }

   public void setHealth(float var1) {
      this.Health = var1;
   }

   public boolean isOnDeathDone() {
      return this.bDead;
   }

   public void setOnDeathDone(boolean var1) {
      this.bDead = var1;
   }

   public boolean isOnKillDone() {
      return this.bKill;
   }

   public void setOnKillDone(boolean var1) {
      this.bKill = var1;
   }

   public boolean isDeathDragDown() {
      return this.bDeathDragDown;
   }

   public void setDeathDragDown(boolean var1) {
      this.bDeathDragDown = var1;
   }

   public boolean isPlayingDeathSound() {
      return this.bPlayingDeathSound;
   }

   public void setPlayingDeathSound(boolean var1) {
      this.bPlayingDeathSound = var1;
   }

   public String getHurtSound() {
      return this.hurtSound;
   }

   public void setHurtSound(String var1) {
      this.hurtSound = var1;
   }

   /** @deprecated */
   @Deprecated
   public boolean isIgnoreMovementForDirection() {
      return false;
   }

   public ItemContainer getInventory() {
      return this.inventory;
   }

   public void setInventory(ItemContainer var1) {
      var1.parent = this;
      this.inventory = var1;
      this.inventory.setExplored(true);
   }

   public boolean isPrimaryEquipped(String var1) {
      if (this.leftHandItem == null) {
         return false;
      } else {
         return this.leftHandItem.getFullType().equals(var1) || this.leftHandItem.getType().equals(var1);
      }
   }

   public InventoryItem getPrimaryHandItem() {
      return this.leftHandItem;
   }

   public void setPrimaryHandItem(InventoryItem var1) {
      this.setEquipParent(this.leftHandItem, var1);
      this.leftHandItem = var1;
      if (GameClient.bClient && this instanceof IsoPlayer && ((IsoPlayer)this).isLocalPlayer()) {
         GameClient.instance.equip((IsoPlayer)this, 0);
      }

      LuaEventManager.triggerEvent("OnEquipPrimary", this, var1);
      this.resetEquippedHandsModels();
      this.setVariable("Weapon", WeaponType.getWeaponType(this).type);
      if (var1 != null && var1 instanceof HandWeapon && !StringUtils.isNullOrEmpty(((HandWeapon)var1).getFireMode())) {
         this.setVariable("FireMode", ((HandWeapon)var1).getFireMode());
      } else {
         this.clearVariable("FireMode");
      }

   }

   protected void setEquipParent(InventoryItem var1, InventoryItem var2) {
      if (var1 != null) {
         var1.setEquipParent((IsoGameCharacter)null);
      }

      if (var2 != null) {
         var2.setEquipParent(this);
      }

   }

   public void initWornItems(String var1) {
      BodyLocationGroup var2 = BodyLocations.getGroup(var1);
      this.wornItems = new WornItems(var2);
   }

   public WornItems getWornItems() {
      return this.wornItems;
   }

   public void setWornItems(WornItems var1) {
      this.wornItems = new WornItems(var1);
   }

   public InventoryItem getWornItem(String var1) {
      return this.wornItems.getItem(var1);
   }

   public void setWornItem(String var1, InventoryItem var2) {
      this.setWornItem(var1, var2, true);
   }

   public void setWornItem(String var1, InventoryItem var2, boolean var3) {
      InventoryItem var4 = this.wornItems.getItem(var1);
      IsoCell var5 = IsoWorld.instance.CurrentCell;
      if (var4 != null && var5 != null) {
         var5.addToProcessItemsRemove(var4);
      }

      this.wornItems.setItem(var1, var2);
      if (var2 != null && var5 != null) {
         if (var2.getContainer() != null) {
            var2.getContainer().parent = this;
         }

         var5.addToProcessItems(var2);
      }

      if (var3 && var4 != null && this instanceof IsoPlayer && !this.getInventory().hasRoomFor(this, var4)) {
         IsoGridSquare var6 = this.getCurrentSquare();
         var6 = this.getSolidFloorAt(var6.x, var6.y, var6.z);
         if (var6 != null) {
            float var7 = Rand.Next(0.1F, 0.9F);
            float var8 = Rand.Next(0.1F, 0.9F);
            float var9 = var6.getApparentZ(var7, var8) - (float)var6.getZ();
            var6.AddWorldInventoryItem(var4, var7, var8, var9);
            this.getInventory().Remove(var4);
         }
      }

      this.resetModelNextFrame();
      if (this.clothingWetness != null) {
         this.clothingWetness.changed = true;
      }

      if (GameClient.bClient && this instanceof IsoPlayer && ((IsoPlayer)this).isLocalPlayer()) {
         GameClient.instance.sendClothing((IsoPlayer)this, var1, var2);
      }

      this.onWornItemsChanged();
   }

   public void removeWornItem(InventoryItem var1) {
      this.removeWornItem(var1, true);
   }

   public void removeWornItem(InventoryItem var1, boolean var2) {
      String var3 = this.wornItems.getLocation(var1);
      if (var3 != null) {
         this.setWornItem(var3, (InventoryItem)null, var2);
      }
   }

   public void clearWornItems() {
      if (this.wornItems != null) {
         this.wornItems.clear();
         if (this.clothingWetness != null) {
            this.clothingWetness.changed = true;
         }

         this.onWornItemsChanged();
      }
   }

   public BodyLocationGroup getBodyLocationGroup() {
      return this.wornItems == null ? null : this.wornItems.getBodyLocationGroup();
   }

   public void onWornItemsChanged() {
   }

   public void initAttachedItems(String var1) {
      AttachedLocationGroup var2 = AttachedLocations.getGroup(var1);
      this.attachedItems = new AttachedItems(var2);
   }

   public AttachedItems getAttachedItems() {
      return this.attachedItems;
   }

   public void setAttachedItems(AttachedItems var1) {
      this.attachedItems = new AttachedItems(var1);
   }

   public InventoryItem getAttachedItem(String var1) {
      return this.attachedItems.getItem(var1);
   }

   public void setAttachedItem(String var1, InventoryItem var2) {
      InventoryItem var3 = this.attachedItems.getItem(var1);
      IsoCell var4 = IsoWorld.instance.CurrentCell;
      if (var3 != null && var4 != null) {
         var4.addToProcessItemsRemove(var3);
      }

      this.attachedItems.setItem(var1, var2);
      if (var2 != null && var4 != null) {
         InventoryContainer var5 = (InventoryContainer)Type.tryCastTo(var2, InventoryContainer.class);
         if (var5 != null && var5.getInventory() != null) {
            var5.getInventory().parent = this;
         }

         var4.addToProcessItems(var2);
      }

      this.resetEquippedHandsModels();
      IsoPlayer var6 = (IsoPlayer)Type.tryCastTo(this, IsoPlayer.class);
      if (GameClient.bClient && var6 != null && var6.isLocalPlayer()) {
         GameClient.instance.sendAttachedItem(var6, var1, var2);
      }

      if (!GameServer.bServer && var6 != null && var6.isLocalPlayer()) {
         LuaEventManager.triggerEvent("OnClothingUpdated", this);
      }

   }

   public void removeAttachedItem(InventoryItem var1) {
      String var2 = this.attachedItems.getLocation(var1);
      if (var2 != null) {
         this.setAttachedItem(var2, (InventoryItem)null);
      }
   }

   public void clearAttachedItems() {
      if (this.attachedItems != null) {
         this.attachedItems.clear();
      }
   }

   public AttachedLocationGroup getAttachedLocationGroup() {
      return this.attachedItems == null ? null : this.attachedItems.getGroup();
   }

   public ClothingWetness getClothingWetness() {
      return this.clothingWetness;
   }

   public InventoryItem getClothingItem_Head() {
      return this.getWornItem("Hat");
   }

   public void setClothingItem_Head(InventoryItem var1) {
      this.setWornItem("Hat", var1);
   }

   public InventoryItem getClothingItem_Torso() {
      return this.getWornItem("Tshirt");
   }

   public void setClothingItem_Torso(InventoryItem var1) {
      this.setWornItem("Tshirt", var1);
   }

   public InventoryItem getClothingItem_Back() {
      return this.getWornItem("Back");
   }

   public void setClothingItem_Back(InventoryItem var1) {
      this.setWornItem("Back", var1);
   }

   public InventoryItem getClothingItem_Hands() {
      return this.getWornItem("Hands");
   }

   public void setClothingItem_Hands(InventoryItem var1) {
      this.setWornItem("Hands", var1);
   }

   public InventoryItem getClothingItem_Legs() {
      return this.getWornItem("Pants");
   }

   public void setClothingItem_Legs(InventoryItem var1) {
      this.setWornItem("Pants", var1);
   }

   public InventoryItem getClothingItem_Feet() {
      return this.getWornItem("Shoes");
   }

   public void setClothingItem_Feet(InventoryItem var1) {
      this.setWornItem("Shoes", var1);
   }

   public int getNextWander() {
      return this.NextWander;
   }

   public void setNextWander(int var1) {
      this.NextWander = var1;
   }

   public boolean isOnFire() {
      return this.OnFire;
   }

   public void setOnFire(boolean var1) {
      this.OnFire = var1;
   }

   public int getPathIndex() {
      return this.pathIndex;
   }

   public void setPathIndex(int var1) {
      this.pathIndex = var1;
   }

   public int getPathTargetX() {
      return (int)this.getPathFindBehavior2().getTargetX();
   }

   public int getPathTargetY() {
      return (int)this.getPathFindBehavior2().getTargetY();
   }

   public int getPathTargetZ() {
      return (int)this.getPathFindBehavior2().getTargetZ();
   }

   public InventoryItem getSecondaryHandItem() {
      return this.rightHandItem;
   }

   public void setSecondaryHandItem(InventoryItem var1) {
      this.setEquipParent(this.rightHandItem, var1);
      this.rightHandItem = var1;
      if (GameClient.bClient && this instanceof IsoPlayer && ((IsoPlayer)this).isLocalPlayer()) {
         GameClient.instance.equip((IsoPlayer)this, 1);
      }

      LuaEventManager.triggerEvent("OnEquipSecondary", this, var1);
      this.resetEquippedHandsModels();
      this.setVariable("Weapon", WeaponType.getWeaponType(this).type);
   }

   public boolean isHandItem(InventoryItem var1) {
      return this.isPrimaryHandItem(var1) || this.isSecondaryHandItem(var1);
   }

   public boolean isPrimaryHandItem(InventoryItem var1) {
      return var1 != null && this.getPrimaryHandItem() == var1;
   }

   public boolean isSecondaryHandItem(InventoryItem var1) {
      return var1 != null && this.getSecondaryHandItem() == var1;
   }

   public boolean isItemInBothHands(InventoryItem var1) {
      return this.isPrimaryHandItem(var1) && this.isSecondaryHandItem(var1);
   }

   public boolean removeFromHands(InventoryItem var1) {
      boolean var2 = true;
      if (this.isPrimaryHandItem(var1)) {
         this.setPrimaryHandItem((InventoryItem)null);
      }

      if (this.isSecondaryHandItem(var1)) {
         this.setSecondaryHandItem((InventoryItem)null);
      }

      return var2;
   }

   public Color getSpeakColour() {
      return this.SpeakColour;
   }

   public void setSpeakColour(Color var1) {
      this.SpeakColour = var1;
   }

   public void setSpeakColourInfo(ColorInfo var1) {
      this.SpeakColour = new Color(var1.r, var1.g, var1.b, 1.0F);
   }

   public float getSlowFactor() {
      return this.slowFactor;
   }

   public void setSlowFactor(float var1) {
      this.slowFactor = var1;
   }

   public float getSlowTimer() {
      return this.slowTimer;
   }

   public void setSlowTimer(float var1) {
      this.slowTimer = var1;
   }

   public boolean isbUseParts() {
      return this.bUseParts;
   }

   public void setbUseParts(boolean var1) {
      this.bUseParts = var1;
   }

   public boolean isSpeaking() {
      return this.IsSpeaking();
   }

   public void setSpeaking(boolean var1) {
      this.Speaking = var1;
   }

   public float getSpeakTime() {
      return this.SpeakTime;
   }

   public void setSpeakTime(int var1) {
      this.SpeakTime = (float)var1;
   }

   public float getSpeedMod() {
      return this.speedMod;
   }

   public void setSpeedMod(float var1) {
      this.speedMod = var1;
   }

   public float getStaggerTimeMod() {
      return this.staggerTimeMod;
   }

   public void setStaggerTimeMod(float var1) {
      this.staggerTimeMod = var1;
   }

   public StateMachine getStateMachine() {
      return this.stateMachine;
   }

   public Moodles getMoodles() {
      return this.Moodles;
   }

   public Stats getStats() {
      return this.stats;
   }

   public Stack getUsedItemsOn() {
      return this.UsedItemsOn;
   }

   public HandWeapon getUseHandWeapon() {
      return this.useHandWeapon;
   }

   public void setUseHandWeapon(HandWeapon var1) {
      this.useHandWeapon = var1;
   }

   public IsoSprite getLegsSprite() {
      return this.legsSprite;
   }

   public void setLegsSprite(IsoSprite var1) {
      this.legsSprite = var1;
   }

   public IsoGridSquare getAttackTargetSquare() {
      return this.attackTargetSquare;
   }

   public void setAttackTargetSquare(IsoGridSquare var1) {
      this.attackTargetSquare = var1;
   }

   public float getBloodImpactX() {
      return this.BloodImpactX;
   }

   public void setBloodImpactX(float var1) {
      this.BloodImpactX = var1;
   }

   public float getBloodImpactY() {
      return this.BloodImpactY;
   }

   public void setBloodImpactY(float var1) {
      this.BloodImpactY = var1;
   }

   public float getBloodImpactZ() {
      return this.BloodImpactZ;
   }

   public void setBloodImpactZ(float var1) {
      this.BloodImpactZ = var1;
   }

   public IsoSprite getBloodSplat() {
      return this.bloodSplat;
   }

   public void setBloodSplat(IsoSprite var1) {
      this.bloodSplat = var1;
   }

   public boolean isbOnBed() {
      return this.bOnBed;
   }

   public void setbOnBed(boolean var1) {
      this.bOnBed = var1;
   }

   public Vector2 getMoveForwardVec() {
      return this.moveForwardVec;
   }

   public void setMoveForwardVec(Vector2 var1) {
      this.moveForwardVec.set(var1);
   }

   public boolean isPathing() {
      return this.pathing;
   }

   public void setPathing(boolean var1) {
      this.pathing = var1;
   }

   public Stack getLocalEnemyList() {
      return this.LocalEnemyList;
   }

   public Stack getEnemyList() {
      return this.EnemyList;
   }

   public TraitCollection getTraits() {
      return this.getCharacterTraits();
   }

   public IsoGameCharacter.CharacterTraits getCharacterTraits() {
      return this.Traits;
   }

   public int getMaxWeight() {
      return this.maxWeight;
   }

   public void setMaxWeight(int var1) {
      this.maxWeight = var1;
   }

   public int getMaxWeightBase() {
      return this.maxWeightBase;
   }

   public void setMaxWeightBase(int var1) {
      this.maxWeightBase = var1;
   }

   public float getSleepingTabletDelta() {
      return this.SleepingTabletDelta;
   }

   public void setSleepingTabletDelta(float var1) {
      this.SleepingTabletDelta = var1;
   }

   public float getBetaEffect() {
      return this.BetaEffect;
   }

   public void setBetaEffect(float var1) {
      this.BetaEffect = var1;
   }

   public float getDepressEffect() {
      return this.DepressEffect;
   }

   public void setDepressEffect(float var1) {
      this.DepressEffect = var1;
   }

   public float getSleepingTabletEffect() {
      return this.SleepingTabletEffect;
   }

   public void setSleepingTabletEffect(float var1) {
      this.SleepingTabletEffect = var1;
   }

   public float getBetaDelta() {
      return this.BetaDelta;
   }

   public void setBetaDelta(float var1) {
      this.BetaDelta = var1;
   }

   public float getDepressDelta() {
      return this.DepressDelta;
   }

   public void setDepressDelta(float var1) {
      this.DepressDelta = var1;
   }

   public float getPainEffect() {
      return this.PainEffect;
   }

   public void setPainEffect(float var1) {
      this.PainEffect = var1;
   }

   public float getPainDelta() {
      return this.PainDelta;
   }

   public void setPainDelta(float var1) {
      this.PainDelta = var1;
   }

   public boolean isbDoDefer() {
      return this.bDoDefer;
   }

   public void setbDoDefer(boolean var1) {
      this.bDoDefer = var1;
   }

   public IsoGameCharacter.Location getLastHeardSound() {
      return this.LastHeardSound;
   }

   public void setLastHeardSound(int var1, int var2, int var3) {
      this.LastHeardSound.x = var1;
      this.LastHeardSound.y = var2;
      this.LastHeardSound.z = var3;
   }

   public float getLrx() {
      return this.lrx;
   }

   public void setLrx(float var1) {
      this.lrx = var1;
   }

   public float getLry() {
      return this.lry;
   }

   public void setLry(float var1) {
      this.lry = var1;
   }

   public boolean isClimbing() {
      return this.bClimbing;
   }

   public void setbClimbing(boolean var1) {
      this.bClimbing = var1;
   }

   public boolean isLastCollidedW() {
      return this.lastCollidedW;
   }

   public void setLastCollidedW(boolean var1) {
      this.lastCollidedW = var1;
   }

   public boolean isLastCollidedN() {
      return this.lastCollidedN;
   }

   public void setLastCollidedN(boolean var1) {
      this.lastCollidedN = var1;
   }

   public float getFallTime() {
      return this.fallTime;
   }

   public void setFallTime(float var1) {
      this.fallTime = var1;
   }

   public float getLastFallSpeed() {
      return this.lastFallSpeed;
   }

   public void setLastFallSpeed(float var1) {
      this.lastFallSpeed = var1;
   }

   public boolean isbFalling() {
      return this.bFalling;
   }

   public void setbFalling(boolean var1) {
      this.bFalling = var1;
   }

   public IsoBuilding getCurrentBuilding() {
      if (this.current == null) {
         return null;
      } else {
         return this.current.getRoom() == null ? null : this.current.getRoom().building;
      }
   }

   public BuildingDef getCurrentBuildingDef() {
      if (this.current == null) {
         return null;
      } else if (this.current.getRoom() == null) {
         return null;
      } else {
         return this.current.getRoom().building != null ? this.current.getRoom().building.def : null;
      }
   }

   public RoomDef getCurrentRoomDef() {
      if (this.current == null) {
         return null;
      } else {
         return this.current.getRoom() != null ? this.current.getRoom().def : null;
      }
   }

   public float getTorchStrength() {
      return 0.0F;
   }

   public void OnAnimEvent(AnimLayer var1, AnimEvent var2) {
      if (var2.m_EventName != null) {
         if (var2.m_EventName.equalsIgnoreCase("SetVariable") && var2.m_SetVariable1 != null) {
            this.setVariable(var2.m_SetVariable1, var2.m_SetVariable2);
         }

         if (var2.m_EventName.equalsIgnoreCase("ClearVariable")) {
            this.clearVariable(var2.m_ParameterValue);
         }

         if (var2.m_EventName.equalsIgnoreCase("PlaySound")) {
            this.getEmitter().playSoundImpl(var2.m_ParameterValue, this);
         }

         if (var2.m_EventName.equalsIgnoreCase("Footstep")) {
            this.DoFootstepSound(var2.m_ParameterValue);
         }

         if (var2.m_EventName.equalsIgnoreCase("DamageWhileInTrees")) {
            this.damageWhileInTrees();
         }

         int var3 = var1.getDepth();
         this.actionContext.reportEvent(var3, var2.m_EventName);
         this.stateMachine.stateAnimEvent(var3, var2);
      }
   }

   private void damageWhileInTrees() {
      if (!this.isZombie() && !"Tutorial".equals(Core.GameMode)) {
         int var1 = 50;
         int var2 = Rand.Next(0, BodyPartType.ToIndex(BodyPartType.MAX));
         if (this.isRunning()) {
            var1 = 30;
         }

         if (this.Traits.Outdoorsman.isSet()) {
            var1 += 50;
         }

         var1 += (int)this.getBodyPartClothingDefense(var2, false, false);
         if (Rand.NextBool(var1)) {
            this.addHole(BloodBodyPartType.FromIndex(var2));
            var1 = 6;
            if (this.Traits.ThickSkinned.isSet()) {
               var1 += 7;
            }

            if (this.Traits.ThinSkinned.isSet()) {
               var1 -= 3;
            }

            if (Rand.NextBool(var1) && (int)this.getBodyPartClothingDefense(var2, false, false) < 100) {
               BodyPart var3 = (BodyPart)this.getBodyDamage().getBodyParts().get(var2);
               if (Rand.NextBool(var1 + 10)) {
                  var3.setCut(true, true);
               } else {
                  var3.setScratched(true, true);
               }
            }
         }

      }
   }

   public float getHammerSoundMod() {
      int var1 = this.getPerkLevel(PerkFactory.Perks.Woodwork);
      if (var1 == 2) {
         return 0.8F;
      } else if (var1 == 3) {
         return 0.6F;
      } else if (var1 == 4) {
         return 0.4F;
      } else {
         return var1 >= 5 ? 0.4F : 1.0F;
      }
   }

   public float getWeldingSoundMod() {
      int var1 = this.getPerkLevel(PerkFactory.Perks.MetalWelding);
      if (var1 == 2) {
         return 0.8F;
      } else if (var1 == 3) {
         return 0.6F;
      } else if (var1 == 4) {
         return 0.4F;
      } else {
         return var1 >= 5 ? 0.4F : 1.0F;
      }
   }

   public float getBarricadeTimeMod() {
      int var1 = this.getPerkLevel(PerkFactory.Perks.Woodwork);
      if (var1 == 1) {
         return 0.8F;
      } else if (var1 == 2) {
         return 0.7F;
      } else if (var1 == 3) {
         return 0.62F;
      } else if (var1 == 4) {
         return 0.56F;
      } else if (var1 == 5) {
         return 0.5F;
      } else if (var1 == 6) {
         return 0.42F;
      } else if (var1 == 7) {
         return 0.36F;
      } else if (var1 == 8) {
         return 0.3F;
      } else if (var1 == 9) {
         return 0.26F;
      } else {
         return var1 == 10 ? 0.2F : 0.7F;
      }
   }

   public float getMetalBarricadeStrengthMod() {
      switch(this.getPerkLevel(PerkFactory.Perks.MetalWelding)) {
      case 2:
         return 1.1F;
      case 3:
         return 1.14F;
      case 4:
         return 1.18F;
      case 5:
         return 1.22F;
      case 6:
         return 1.16F;
      case 7:
         return 1.3F;
      case 8:
         return 1.34F;
      case 9:
         return 1.4F;
      case 10:
         return 1.5F;
      default:
         int var1 = this.getPerkLevel(PerkFactory.Perks.Woodwork);
         if (var1 == 2) {
            return 1.1F;
         } else if (var1 == 3) {
            return 1.14F;
         } else if (var1 == 4) {
            return 1.18F;
         } else if (var1 == 5) {
            return 1.22F;
         } else if (var1 == 6) {
            return 1.26F;
         } else if (var1 == 7) {
            return 1.3F;
         } else if (var1 == 8) {
            return 1.34F;
         } else if (var1 == 9) {
            return 1.4F;
         } else {
            return var1 == 10 ? 1.5F : 1.0F;
         }
      }
   }

   public float getBarricadeStrengthMod() {
      int var1 = this.getPerkLevel(PerkFactory.Perks.Woodwork);
      if (var1 == 2) {
         return 1.1F;
      } else if (var1 == 3) {
         return 1.14F;
      } else if (var1 == 4) {
         return 1.18F;
      } else if (var1 == 5) {
         return 1.22F;
      } else if (var1 == 6) {
         return 1.26F;
      } else if (var1 == 7) {
         return 1.3F;
      } else if (var1 == 8) {
         return 1.34F;
      } else if (var1 == 9) {
         return 1.4F;
      } else {
         return var1 == 10 ? 1.5F : 1.0F;
      }
   }

   public float getSneakSpotMod() {
      int var1 = this.getPerkLevel(PerkFactory.Perks.Sneak);
      float var2 = 0.95F;
      if (var1 == 1) {
         var2 = 0.9F;
      }

      if (var1 == 2) {
         var2 = 0.8F;
      }

      if (var1 == 3) {
         var2 = 0.75F;
      }

      if (var1 == 4) {
         var2 = 0.7F;
      }

      if (var1 == 5) {
         var2 = 0.65F;
      }

      if (var1 == 6) {
         var2 = 0.6F;
      }

      if (var1 == 7) {
         var2 = 0.55F;
      }

      if (var1 == 8) {
         var2 = 0.5F;
      }

      if (var1 == 9) {
         var2 = 0.45F;
      }

      if (var1 == 10) {
         var2 = 0.4F;
      }

      var2 *= 1.2F;
      return var2;
   }

   public float getNimbleMod() {
      int var1 = this.getPerkLevel(PerkFactory.Perks.Nimble);
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
         return 1.38F;
      } else if (var1 == 9) {
         return 1.42F;
      } else {
         return var1 == 10 ? 1.5F : 1.0F;
      }
   }

   public float getFatigueMod() {
      int var1 = this.getPerkLevel(PerkFactory.Perks.Fitness);
      if (var1 == 1) {
         return 0.95F;
      } else if (var1 == 2) {
         return 0.92F;
      } else if (var1 == 3) {
         return 0.89F;
      } else if (var1 == 4) {
         return 0.87F;
      } else if (var1 == 5) {
         return 0.85F;
      } else if (var1 == 6) {
         return 0.83F;
      } else if (var1 == 7) {
         return 0.81F;
      } else if (var1 == 8) {
         return 0.79F;
      } else if (var1 == 9) {
         return 0.77F;
      } else {
         return var1 == 10 ? 0.75F : 1.0F;
      }
   }

   public float getLightfootMod() {
      int var1 = this.getPerkLevel(PerkFactory.Perks.Lightfoot);
      if (var1 == 1) {
         return 0.9F;
      } else if (var1 == 2) {
         return 0.79F;
      } else if (var1 == 3) {
         return 0.71F;
      } else if (var1 == 4) {
         return 0.65F;
      } else if (var1 == 5) {
         return 0.59F;
      } else if (var1 == 6) {
         return 0.52F;
      } else if (var1 == 7) {
         return 0.45F;
      } else if (var1 == 8) {
         return 0.37F;
      } else if (var1 == 9) {
         return 0.3F;
      } else {
         return var1 == 10 ? 0.2F : 0.99F;
      }
   }

   public float getPacingMod() {
      int var1 = this.getPerkLevel(PerkFactory.Perks.Fitness);
      if (var1 == 1) {
         return 0.8F;
      } else if (var1 == 2) {
         return 0.75F;
      } else if (var1 == 3) {
         return 0.7F;
      } else if (var1 == 4) {
         return 0.65F;
      } else if (var1 == 5) {
         return 0.6F;
      } else if (var1 == 6) {
         return 0.57F;
      } else if (var1 == 7) {
         return 0.53F;
      } else if (var1 == 8) {
         return 0.49F;
      } else if (var1 == 9) {
         return 0.46F;
      } else {
         return var1 == 10 ? 0.43F : 0.9F;
      }
   }

   public float getHyperthermiaMod() {
      float var1 = 1.0F;
      if (this.getMoodles().getMoodleLevel(MoodleType.Hyperthermia) > 1) {
         var1 = 1.0F;
         if (this.getMoodles().getMoodleLevel(MoodleType.Hyperthermia) == 4) {
            var1 = 2.0F;
         }
      }

      return var1;
   }

   public float getHittingMod() {
      int var1 = this.getPerkLevel(PerkFactory.Perks.Strength);
      if (var1 == 1) {
         return 0.8F;
      } else if (var1 == 2) {
         return 0.85F;
      } else if (var1 == 3) {
         return 0.9F;
      } else if (var1 == 4) {
         return 0.95F;
      } else if (var1 == 5) {
         return 1.0F;
      } else if (var1 == 6) {
         return 1.05F;
      } else if (var1 == 7) {
         return 1.1F;
      } else if (var1 == 8) {
         return 1.15F;
      } else if (var1 == 9) {
         return 1.2F;
      } else {
         return var1 == 10 ? 1.25F : 0.75F;
      }
   }

   public float getShovingMod() {
      int var1 = this.getPerkLevel(PerkFactory.Perks.Strength);
      if (var1 == 1) {
         return 0.8F;
      } else if (var1 == 2) {
         return 0.85F;
      } else if (var1 == 3) {
         return 0.9F;
      } else if (var1 == 4) {
         return 0.95F;
      } else if (var1 == 5) {
         return 1.0F;
      } else if (var1 == 6) {
         return 1.05F;
      } else if (var1 == 7) {
         return 1.1F;
      } else if (var1 == 8) {
         return 1.15F;
      } else if (var1 == 9) {
         return 1.2F;
      } else {
         return var1 == 10 ? 1.25F : 0.75F;
      }
   }

   public float getRecoveryMod() {
      int var1 = this.getPerkLevel(PerkFactory.Perks.Fitness);
      float var2 = 0.0F;
      if (var1 == 0) {
         var2 = 0.7F;
      }

      if (var1 == 1) {
         var2 = 0.8F;
      }

      if (var1 == 2) {
         var2 = 0.9F;
      }

      if (var1 == 3) {
         var2 = 1.0F;
      }

      if (var1 == 4) {
         var2 = 1.1F;
      }

      if (var1 == 5) {
         var2 = 1.2F;
      }

      if (var1 == 6) {
         var2 = 1.3F;
      }

      if (var1 == 7) {
         var2 = 1.4F;
      }

      if (var1 == 8) {
         var2 = 1.5F;
      }

      if (var1 == 9) {
         var2 = 1.55F;
      }

      if (var1 == 10) {
         var2 = 1.6F;
      }

      if (this.Traits.Obese.isSet()) {
         var2 = (float)((double)var2 * 0.4D);
      }

      if (this.Traits.Overweight.isSet()) {
         var2 = (float)((double)var2 * 0.7D);
      }

      if (this.Traits.VeryUnderweight.isSet()) {
         var2 = (float)((double)var2 * 0.7D);
      }

      if (this.Traits.Emaciated.isSet()) {
         var2 = (float)((double)var2 * 0.3D);
      }

      if (this instanceof IsoPlayer) {
         if (((IsoPlayer)this).getNutrition().getLipids() < -1500.0F) {
            var2 = (float)((double)var2 * 0.2D);
         } else if (((IsoPlayer)this).getNutrition().getLipids() < -1000.0F) {
            var2 = (float)((double)var2 * 0.5D);
         }

         if (((IsoPlayer)this).getNutrition().getProteins() < -1500.0F) {
            var2 = (float)((double)var2 * 0.2D);
         } else if (((IsoPlayer)this).getNutrition().getProteins() < -1000.0F) {
            var2 = (float)((double)var2 * 0.5D);
         }
      }

      return var2;
   }

   public float getWeightMod() {
      int var1 = this.getPerkLevel(PerkFactory.Perks.Strength);
      if (var1 == 1) {
         return 0.9F;
      } else if (var1 == 2) {
         return 1.07F;
      } else if (var1 == 3) {
         return 1.24F;
      } else if (var1 == 4) {
         return 1.41F;
      } else if (var1 == 5) {
         return 1.58F;
      } else if (var1 == 6) {
         return 1.75F;
      } else if (var1 == 7) {
         return 1.92F;
      } else if (var1 == 8) {
         return 2.09F;
      } else if (var1 == 9) {
         return 2.26F;
      } else {
         return var1 == 10 ? 2.5F : 0.8F;
      }
   }

   public int getHitChancesMod() {
      int var1 = this.getPerkLevel(PerkFactory.Perks.Aiming);
      if (var1 == 1) {
         return 1;
      } else if (var1 == 2) {
         return 1;
      } else if (var1 == 3) {
         return 2;
      } else if (var1 == 4) {
         return 2;
      } else if (var1 == 5) {
         return 3;
      } else if (var1 == 6) {
         return 3;
      } else if (var1 == 7) {
         return 4;
      } else if (var1 == 8) {
         return 4;
      } else if (var1 == 9) {
         return 5;
      } else {
         return var1 == 10 ? 5 : 1;
      }
   }

   public float getSprintMod() {
      int var1 = this.getPerkLevel(PerkFactory.Perks.Sprinting);
      if (var1 == 1) {
         return 1.1F;
      } else if (var1 == 2) {
         return 1.15F;
      } else if (var1 == 3) {
         return 1.2F;
      } else if (var1 == 4) {
         return 1.25F;
      } else if (var1 == 5) {
         return 1.3F;
      } else if (var1 == 6) {
         return 1.35F;
      } else if (var1 == 7) {
         return 1.4F;
      } else if (var1 == 8) {
         return 1.45F;
      } else if (var1 == 9) {
         return 1.5F;
      } else {
         return var1 == 10 ? 1.6F : 0.9F;
      }
   }

   public int getPerkLevel(PerkFactory.Perk var1) {
      IsoGameCharacter.PerkInfo var2 = this.getPerkInfo(var1);
      return var2 != null ? var2.level : 0;
   }

   public void setPerkLevelDebug(PerkFactory.Perk var1, int var2) {
      IsoGameCharacter.PerkInfo var3 = this.getPerkInfo(var1);
      if (var3 != null) {
         var3.level = var2;
      }

      if (GameClient.bClient && this instanceof IsoPlayer) {
         GameClient.sendPerks((IsoPlayer)this);
      }

   }

   public void LoseLevel(PerkFactory.Perk var1) {
      IsoGameCharacter.PerkInfo var2 = this.getPerkInfo(var1);
      if (var2 != null) {
         --var2.level;
         if (var2.level < 0) {
            var2.level = 0;
         }

         LuaEventManager.triggerEvent("LevelPerk", this, var1, var2.level, false);
         if (var1 == PerkFactory.Perks.Sneak && GameClient.bClient && this instanceof IsoPlayer) {
            GameClient.sendPerks((IsoPlayer)this);
         }

      } else {
         LuaEventManager.triggerEvent("LevelPerk", this, var1, 0, false);
      }
   }

   public void LevelPerk(PerkFactory.Perk var1, boolean var2) {
      Objects.requireNonNull(var1, "perk is null");
      if (var1 == PerkFactory.Perks.MAX) {
         throw new IllegalArgumentException("perk == Perks.MAX");
      } else {
         IsoGameCharacter.PerkInfo var3 = this.getPerkInfo(var1);
         if (var3 != null) {
            ++var3.level;
            if (this instanceof IsoPlayer && !var1.isPassiv() && !"Tutorial".equals(Core.GameMode) && this.getHoursSurvived() > 1.0D) {
               HaloTextHelper.addTextWithArrow((IsoPlayer)this, "+1 " + var1.getName(), true, HaloTextHelper.getColorGreen());
            }

            if (var3.level > 10) {
               var3.level = 10;
            }

            if (GameClient.bClient && this instanceof IsoPlayer) {
               GameClient.instance.sendSyncXp((IsoPlayer)this);
            }

            LuaEventManager.triggerEventGarbage("LevelPerk", this, var1, var3.level, true);
            if (GameClient.bClient && this instanceof IsoPlayer) {
               GameClient.sendPerks((IsoPlayer)this);
            }

         } else {
            var3 = new IsoGameCharacter.PerkInfo();
            var3.perk = var1;
            var3.level = 1;
            this.PerkList.add(var3);
            if (GameClient.bClient && this instanceof IsoPlayer) {
               GameClient.instance.sendSyncXp((IsoPlayer)this);
            }

            LuaEventManager.triggerEvent("LevelPerk", this, var1, var3.level, true);
         }
      }
   }

   public void LevelPerk(PerkFactory.Perk var1) {
      this.LevelPerk(var1, true);
   }

   public void level0(PerkFactory.Perk var1) {
      IsoGameCharacter.PerkInfo var2 = this.getPerkInfo(var1);
      if (var2 != null) {
         var2.level = 0;
      }

   }

   public IsoGameCharacter.Location getLastKnownLocationOf(String var1) {
      return this.LastKnownLocation.containsKey(var1) ? (IsoGameCharacter.Location)this.LastKnownLocation.get(var1) : null;
   }

   public void ReadLiterature(Literature var1) {
      Stats var10000 = this.stats;
      var10000.stress += var1.getStressChange();
      this.getBodyDamage().JustReadSomething(var1);
      if (var1.getTeachedRecipes() != null) {
         for(int var2 = 0; var2 < var1.getTeachedRecipes().size(); ++var2) {
            if (!this.getKnownRecipes().contains(var1.getTeachedRecipes().get(var2))) {
               this.getKnownRecipes().add((String)var1.getTeachedRecipes().get(var2));
            }
         }
      }

      var1.Use();
   }

   public void OnDeath() {
      LuaEventManager.triggerEvent("OnCharacterDeath", this);
   }

   public void splatBloodFloorBig() {
      if (this.getCurrentSquare() != null && this.getCurrentSquare().getChunk() != null) {
         this.getCurrentSquare().getChunk().addBloodSplat(this.x, this.y, this.z, Rand.Next(20));
      }

   }

   public void splatBloodFloor() {
      if (this.getCurrentSquare() != null) {
         if (this.getCurrentSquare().getChunk() != null) {
            if (this.isDead() && Rand.Next(10) == 0) {
               this.getCurrentSquare().getChunk().addBloodSplat(this.x, this.y, this.z, Rand.Next(20));
            }

            if (Rand.Next(14) == 0) {
               this.getCurrentSquare().getChunk().addBloodSplat(this.x, this.y, this.z, Rand.Next(8));
            }

            if (Rand.Next(50) == 0) {
               this.getCurrentSquare().getChunk().addBloodSplat(this.x, this.y, this.z, Rand.Next(20));
            }

         }
      }
   }

   public int getThreatLevel() {
      int var1 = this.LocalRelevantEnemyList.size();
      var1 += this.VeryCloseEnemyList.size() * 10;
      if (var1 > 20) {
         return 3;
      } else if (var1 > 10) {
         return 2;
      } else {
         return var1 > 0 ? 1 : 0;
      }
   }

   public boolean isDead() {
      return this.Health <= 0.0F || this.BodyDamage != null && this.BodyDamage.getHealth() <= 0.0F;
   }

   public boolean isAlive() {
      return !this.isDead();
   }

   public void Seen(Stack var1) {
      synchronized(this.LocalList) {
         this.LocalList.clear();
         this.LocalList.addAll(var1);
      }
   }

   public boolean CanSee(IsoMovingObject var1) {
      return LosUtil.lineClear(this.getCell(), (int)this.getX(), (int)this.getY(), (int)this.getZ(), (int)var1.getX(), (int)var1.getY(), (int)var1.getZ(), false) != LosUtil.TestResults.Blocked;
   }

   public IsoGridSquare getLowDangerInVicinity(int var1, int var2) {
      float var3 = -1000000.0F;
      IsoGridSquare var4 = null;

      for(int var5 = 0; var5 < var1; ++var5) {
         float var6 = 0.0F;
         int var7 = Rand.Next(-var2, var2);
         int var8 = Rand.Next(-var2, var2);
         IsoGridSquare var9 = this.getCell().getGridSquare((int)this.getX() + var7, (int)this.getY() + var8, (int)this.getZ());
         if (var9 != null && var9.isFree(true)) {
            float var10 = (float)var9.getMovingObjects().size();
            if (var9.getE() != null) {
               var10 += (float)var9.getE().getMovingObjects().size();
            }

            if (var9.getS() != null) {
               var10 += (float)var9.getS().getMovingObjects().size();
            }

            if (var9.getW() != null) {
               var10 += (float)var9.getW().getMovingObjects().size();
            }

            if (var9.getN() != null) {
               var10 += (float)var9.getN().getMovingObjects().size();
            }

            var6 -= var10 * 1000.0F;
            if (var6 > var3) {
               var3 = var6;
               var4 = var9;
            }
         }
      }

      return var4;
   }

   public void Anger(int var1) {
      float var2 = 10.0F;
      if ((float)Rand.Next(100) < var2) {
         var1 *= 2;
      }

      var1 = (int)((float)var1 * (this.stats.getStress() + 1.0F));
      var1 = (int)((float)var1 * (this.BodyDamage.getUnhappynessLevel() / 100.0F + 1.0F));
      Stats var10000 = this.stats;
      var10000.Anger += (float)var1 / 100.0F;
   }

   public boolean hasEquipped(String var1) {
      if (var1.contains(".")) {
         var1 = var1.split("\\.")[1];
      }

      if (this.leftHandItem != null && this.leftHandItem.getType().equals(var1)) {
         return true;
      } else {
         return this.rightHandItem != null && this.rightHandItem.getType().equals(var1);
      }
   }

   public boolean hasEquippedTag(String var1) {
      if (this.leftHandItem != null && this.leftHandItem.hasTag(var1)) {
         return true;
      } else {
         return this.rightHandItem != null && this.rightHandItem.hasTag(var1);
      }
   }

   public void setDir(IsoDirections var1) {
      this.dir = var1;
      this.getVectorFromDirection(this.m_forwardDirection);
   }

   public void Callout(boolean var1) {
      if (this.isCanShout()) {
         this.Callout();
         if (var1) {
            this.playEmote("shout");
         }

      }
   }

   public void Callout() {
      String var1 = "";
      byte var2 = 30;
      if (Core.getInstance().getGameMode().equals("Tutorial")) {
         var1 = Translator.getText("IGUI_PlayerText_CalloutTutorial");
      } else if (this.isSneaking()) {
         var2 = 6;
         switch(Rand.Next(3)) {
         case 0:
            var1 = Translator.getText("IGUI_PlayerText_Callout1Sneak");
            break;
         case 1:
            var1 = Translator.getText("IGUI_PlayerText_Callout2Sneak");
            break;
         case 2:
            var1 = Translator.getText("IGUI_PlayerText_Callout3Sneak");
         }
      } else {
         switch(Rand.Next(3)) {
         case 0:
            var1 = Translator.getText("IGUI_PlayerText_Callout1New");
            break;
         case 1:
            var1 = Translator.getText("IGUI_PlayerText_Callout2New");
            break;
         case 2:
            var1 = Translator.getText("IGUI_PlayerText_Callout3New");
         }
      }

      WorldSoundManager.instance.addSound(this, (int)this.x, (int)this.y, (int)this.z, var2, var2);
      this.SayShout(var1);
      this.callOut = true;
   }

   public void load(ByteBuffer var1, int var2, boolean var3) throws IOException {
      super.load(var1, var2, var3);
      this.getVectorFromDirection(this.m_forwardDirection);
      if (var1.get() == 1) {
         this.descriptor = new SurvivorDesc(true);
         this.descriptor.load(var1, var2, this);
         this.bFemale = this.descriptor.isFemale();
      }

      this.getVisual().load(var1, var2);
      ArrayList var4 = this.inventory.load(var1, var2);
      this.savedInventoryItems.clear();

      for(int var5 = 0; var5 < var4.size(); ++var5) {
         this.savedInventoryItems.add((InventoryItem)var4.get(var5));
      }

      this.Asleep = var1.get() == 1;
      this.ForceWakeUpTime = var1.getFloat();
      int var6;
      if (!this.isZombie()) {
         this.stats.load(var1, var2);
         this.BodyDamage.load(var1, var2);
         this.xp.load(var1, var2);
         ArrayList var9 = this.inventory.IncludingObsoleteItems;
         var6 = var1.getInt();
         if (var6 >= 0 && var6 < var9.size()) {
            this.leftHandItem = (InventoryItem)var9.get(var6);
         }

         var6 = var1.getInt();
         if (var6 >= 0 && var6 < var9.size()) {
            this.rightHandItem = (InventoryItem)var9.get(var6);
         }

         this.setEquipParent((InventoryItem)null, this.leftHandItem);
         this.setEquipParent((InventoryItem)null, this.rightHandItem);
      }

      boolean var10 = var1.get() == 1;
      if (var10) {
         this.SetOnFire();
      }

      this.DepressEffect = var1.getFloat();
      this.DepressFirstTakeTime = var1.getFloat();
      this.BetaEffect = var1.getFloat();
      this.BetaDelta = var1.getFloat();
      this.PainEffect = var1.getFloat();
      this.PainDelta = var1.getFloat();
      this.SleepingTabletEffect = var1.getFloat();
      this.SleepingTabletDelta = var1.getFloat();
      var6 = var1.getInt();

      int var7;
      for(var7 = 0; var7 < var6; ++var7) {
         IsoGameCharacter.ReadBook var8 = new IsoGameCharacter.ReadBook();
         var8.fullType = GameWindow.ReadString(var1);
         var8.alreadyReadPages = var1.getInt();
         this.ReadBooks.add(var8);
      }

      this.reduceInfectionPower = var1.getFloat();
      var7 = var1.getInt();

      for(int var11 = 0; var11 < var7; ++var11) {
         this.knownRecipes.add(GameWindow.ReadString(var1));
      }

      this.lastHourSleeped = var1.getInt();
      this.timeSinceLastSmoke = var1.getFloat();
      this.beardGrowTiming = var1.getFloat();
      this.hairGrowTiming = var1.getFloat();
      this.setUnlimitedCarry(var1.get() == 1);
      this.setBuildCheat(var1.get() == 1);
      this.setHealthCheat(var1.get() == 1);
      this.setMechanicsCheat(var1.get() == 1);
      if (var2 >= 176) {
         this.setMovablesCheat(var1.get() == 1);
         this.setFarmingCheat(var1.get() == 1);
         this.setTimedActionInstantCheat(var1.get() == 1);
         this.setUnlimitedEndurance(var1.get() == 1);
      }

      if (var2 >= 161) {
         this.setSneaking(var1.get() == 1);
         this.setDeathDragDown(var1.get() == 1);
      }

   }

   public void save(ByteBuffer var1, boolean var2) throws IOException {
      super.save(var1, var2);
      if (this.descriptor == null) {
         var1.put((byte)0);
      } else {
         var1.put((byte)1);
         this.descriptor.save(var1);
      }

      this.getVisual().save(var1);
      ArrayList var3 = this.inventory.save(var1, this);
      this.savedInventoryItems.clear();

      int var4;
      for(var4 = 0; var4 < var3.size(); ++var4) {
         this.savedInventoryItems.add((InventoryItem)var3.get(var4));
      }

      var1.put((byte)(this.Asleep ? 1 : 0));
      var1.putFloat(this.ForceWakeUpTime);
      if (!this.isZombie()) {
         this.stats.save(var1);
         this.BodyDamage.save(var1);
         this.xp.save(var1);
         if (this.leftHandItem != null) {
            var1.putInt(this.inventory.getItems().indexOf(this.leftHandItem));
         } else {
            var1.putInt(-1);
         }

         if (this.rightHandItem != null) {
            var1.putInt(this.inventory.getItems().indexOf(this.rightHandItem));
         } else {
            var1.putInt(-1);
         }
      }

      var1.put((byte)(this.OnFire ? 1 : 0));
      var1.putFloat(this.DepressEffect);
      var1.putFloat(this.DepressFirstTakeTime);
      var1.putFloat(this.BetaEffect);
      var1.putFloat(this.BetaDelta);
      var1.putFloat(this.PainEffect);
      var1.putFloat(this.PainDelta);
      var1.putFloat(this.SleepingTabletEffect);
      var1.putFloat(this.SleepingTabletDelta);
      var1.putInt(this.ReadBooks.size());

      for(var4 = 0; var4 < this.ReadBooks.size(); ++var4) {
         IsoGameCharacter.ReadBook var5 = (IsoGameCharacter.ReadBook)this.ReadBooks.get(var4);
         GameWindow.WriteString(var1, var5.fullType);
         var1.putInt(var5.alreadyReadPages);
      }

      var1.putFloat(this.reduceInfectionPower);
      var1.putInt(this.knownRecipes.size());

      for(var4 = 0; var4 < this.knownRecipes.size(); ++var4) {
         String var6 = (String)this.knownRecipes.get(var4);
         GameWindow.WriteString(var1, var6);
      }

      var1.putInt(this.lastHourSleeped);
      var1.putFloat(this.timeSinceLastSmoke);
      var1.putFloat(this.beardGrowTiming);
      var1.putFloat(this.hairGrowTiming);
      var1.put((byte)(this.isUnlimitedCarry() ? 1 : 0));
      var1.put((byte)(this.isBuildCheat() ? 1 : 0));
      var1.put((byte)(this.isHealthCheat() ? 1 : 0));
      var1.put((byte)(this.isMechanicsCheat() ? 1 : 0));
      var1.put((byte)(this.isMovablesCheat() ? 1 : 0));
      var1.put((byte)(this.isFarmingCheat() ? 1 : 0));
      var1.put((byte)(this.isTimedActionInstantCheat() ? 1 : 0));
      var1.put((byte)(this.isUnlimitedEndurance() ? 1 : 0));
      var1.put((byte)(this.isSneaking() ? 1 : 0));
      var1.put((byte)(this.isDeathDragDown() ? 1 : 0));
   }

   public ChatElement getChatElement() {
      return this.chatElement;
   }

   public void StartAction(BaseAction var1) {
      this.CharacterActions.clear();
      this.CharacterActions.push(var1);
      if (var1.valid()) {
         var1.waitToStart();
      }

   }

   public void QueueAction(BaseAction var1) {
   }

   public void StopAllActionQueue() {
      if (!this.CharacterActions.isEmpty()) {
         BaseAction var1 = (BaseAction)this.CharacterActions.get(0);
         if (var1.bStarted) {
            var1.stop();
         }

         this.CharacterActions.clear();
         if (this == IsoPlayer.players[0] || this == IsoPlayer.players[1] || this == IsoPlayer.players[2] || this == IsoPlayer.players[3]) {
            UIManager.getProgressBar((double)((IsoPlayer)this).getPlayerNum()).setValue(0.0F);
         }

      }
   }

   public void StopAllActionQueueRunning() {
      if (!this.CharacterActions.isEmpty()) {
         BaseAction var1 = (BaseAction)this.CharacterActions.get(0);
         if (var1.StopOnRun) {
            if (var1.bStarted) {
               var1.stop();
            }

            this.CharacterActions.clear();
            if (this == IsoPlayer.players[0] || this == IsoPlayer.players[1] || this == IsoPlayer.players[2] || this == IsoPlayer.players[3]) {
               UIManager.getProgressBar((double)((IsoPlayer)this).getPlayerNum()).setValue(0.0F);
            }

         }
      }
   }

   public void StopAllActionQueueAiming() {
      if (this.CharacterActions.size() != 0) {
         BaseAction var1 = (BaseAction)this.CharacterActions.get(0);
         if (var1.StopOnAim) {
            if (var1.bStarted) {
               var1.stop();
            }

            this.CharacterActions.clear();
            if (this == IsoPlayer.players[0] || this == IsoPlayer.players[1] || this == IsoPlayer.players[2] || this == IsoPlayer.players[3]) {
               UIManager.getProgressBar((double)((IsoPlayer)this).getPlayerNum()).setValue(0.0F);
            }

         }
      }
   }

   public void StopAllActionQueueWalking() {
      if (this.CharacterActions.size() != 0) {
         BaseAction var1 = (BaseAction)this.CharacterActions.get(0);
         if (var1.StopOnWalk) {
            if (var1.bStarted) {
               var1.stop();
            }

            this.CharacterActions.clear();
            if (this == IsoPlayer.players[0] || this == IsoPlayer.players[1] || this == IsoPlayer.players[2] || this == IsoPlayer.players[3]) {
               UIManager.getProgressBar((double)((IsoPlayer)this).getPlayerNum()).setValue(0.0F);
            }

         }
      }
   }

   public String GetAnimSetName() {
      return "Base";
   }

   public void SleepingTablet(float var1) {
      this.SleepingTabletEffect = 6600.0F;
      this.SleepingTabletDelta += var1;
   }

   public void BetaBlockers(float var1) {
      this.BetaEffect = 6600.0F;
      this.BetaDelta += var1;
   }

   public void BetaAntiDepress(float var1) {
      if (this.DepressEffect == 0.0F) {
         this.DepressFirstTakeTime = 10000.0F;
      }

      this.DepressEffect = 6600.0F;
      this.DepressDelta += var1;
   }

   public void PainMeds(float var1) {
      this.PainEffect = 5400.0F;
      this.PainDelta += var1;
   }

   public void initSpritePartsEmpty() {
      this.InitSpriteParts(this.descriptor);
   }

   public void InitSpriteParts(SurvivorDesc var1) {
      this.sprite.AnimMap.clear();
      this.sprite.AnimStack.clear();
      this.sprite.CurrentAnim = null;
      this.legsSprite = this.sprite;
      this.legsSprite.name = var1.torso;
      this.bUseParts = true;
   }

   public boolean HasTrait(String var1) {
      return this.Traits.contains(var1);
   }

   public void ApplyInBedOffset(boolean var1) {
      if (var1) {
         if (!this.bOnBed) {
            this.offsetX -= 20.0F;
            this.offsetY += 21.0F;
            this.bOnBed = true;
         }
      } else if (this.bOnBed) {
         this.offsetX += 20.0F;
         this.offsetY -= 21.0F;
         this.bOnBed = false;
      }

   }

   public void Dressup(SurvivorDesc var1) {
      if (!this.isZombie()) {
         if (this.wornItems != null) {
            ItemVisuals var2 = new ItemVisuals();
            var1.getItemVisuals(var2);
            this.wornItems.setFromItemVisuals(var2);
            this.wornItems.addItemsToItemContainer(this.inventory);
            var1.wornItems.clear();
            this.onWornItemsChanged();
         }
      }
   }

   public void PlayAnim(String var1) {
   }

   public void PlayAnimWithSpeed(String var1, float var2) {
   }

   public void PlayAnimUnlooped(String var1) {
   }

   public void DirectionFromVector(Vector2 var1) {
      this.dir = IsoDirections.fromAngle(var1);
   }

   public void DoFootstepSound(String var1) {
      float var2 = 1.0F;
      byte var4 = -1;
      switch(var1.hashCode()) {
      case -940878112:
         if (var1.equals("sneak_run")) {
            var4 = 1;
         }
         break;
      case -895679974:
         if (var1.equals("sprint")) {
            var4 = 5;
         }
         break;
      case -891993841:
         if (var1.equals("strafe")) {
            var4 = 2;
         }
         break;
      case 113291:
         if (var1.equals("run")) {
            var4 = 4;
         }
         break;
      case 3641801:
         if (var1.equals("walk")) {
            var4 = 3;
         }
         break;
      case 897679380:
         if (var1.equals("sneak_walk")) {
            var4 = 0;
         }
      }

      switch(var4) {
      case 0:
         var2 = 0.2F;
         break;
      case 1:
         var2 = 0.5F;
         break;
      case 2:
         var2 = this.bSneaking ? 0.2F : 0.3F;
         break;
      case 3:
         var2 = 0.5F;
         break;
      case 4:
         var2 = 1.3F;
         break;
      case 5:
         var2 = 1.8F;
      }

      this.DoFootstepSound(var2);
   }

   public void DoFootstepSound(float var1) {
      IsoPlayer var2 = (IsoPlayer)Type.tryCastTo(this, IsoPlayer.class);
      if (GameClient.bClient && var2 != null && var2.networkAI != null) {
         var2.networkAI.footstepSoundRadius = 0;
      }

      if (var2 == null || !var2.isGhostMode() || DebugOptions.instance.Character.Debug.PlaySoundWhenInvisible.getValue()) {
         if (this.getCurrentSquare() != null) {
            if (!(var1 <= 0.0F)) {
               float var3 = var1;
               var1 *= 1.4F;
               if (this.Traits.Graceful.isSet()) {
                  var1 *= 0.6F;
               }

               if (this.Traits.Clumsy.isSet()) {
                  var1 *= 1.2F;
               }

               if (this.getWornItem("Shoes") == null) {
                  var1 *= 0.5F;
               }

               var1 *= this.getLightfootMod();
               var1 *= 2.0F - this.getNimbleMod();
               if (this.bSneaking) {
                  var1 *= this.getSneakSpotMod();
               }

               if (var1 > 0.0F) {
                  this.emitter.playFootsteps("HumanFootstepsCombined", var3);
                  if (var2 != null && var2.isGhostMode()) {
                     return;
                  }

                  int var4 = (int)Math.ceil((double)(var1 * 10.0F));
                  if (this.bSneaking) {
                     var4 = Math.max(1, var4);
                  }

                  if (this.getCurrentSquare().getRoom() != null) {
                     var4 = (int)((float)var4 * 0.5F);
                  }

                  int var5 = 2;
                  if (this.bSneaking) {
                     var5 = Math.min(12, 4 + this.getPerkLevel(PerkFactory.Perks.Lightfoot));
                  }

                  if (GameClient.bClient && var2 != null && var2.networkAI != null) {
                     var2.networkAI.footstepSoundRadius = (byte)var4;
                  }

                  if (Rand.Next(var5) == 0) {
                     WorldSoundManager.instance.addSound(this, (int)this.getX(), (int)this.getY(), (int)this.getZ(), var4, var4, false, 0.0F, 1.0F, false, false, false);
                  }
               }

            }
         }
      }
   }

   public boolean Eat(InventoryItem var1, float var2) {
      Food var3 = (Food)Type.tryCastTo(var1, Food.class);
      if (var3 == null) {
         return false;
      } else {
         var2 = PZMath.clamp(var2, 0.0F, 1.0F);
         if (var3.getRequireInHandOrInventory() != null) {
            InventoryItem var4 = null;

            for(int var5 = 0; var5 < var3.getRequireInHandOrInventory().size(); ++var5) {
               String var6 = (String)var3.getRequireInHandOrInventory().get(var5);
               var4 = this.getInventory().FindAndReturn(var6);
               if (var4 != null) {
                  var4.Use();
                  break;
               }
            }
         }

         float var9;
         if (var3.getBaseHunger() != 0.0F && var3.getHungChange() != 0.0F) {
            float var7 = var3.getBaseHunger() * var2;
            var9 = var7 / var3.getHungChange();
            var9 = PZMath.clamp(var9, 0.0F, 1.0F);
            var2 = var9;
         }

         if (var3.getHungChange() < 0.0F && var3.getHungChange() * (1.0F - var2) > -0.01F) {
            var2 = 1.0F;
         }

         if (var3.getHungChange() == 0.0F && var3.getThirstChange() < 0.0F && var3.getThirstChange() * (1.0F - var2) > -0.01F) {
            var2 = 1.0F;
         }

         Stats var10000 = this.stats;
         var10000.thirst += var3.getThirstChange() * var2;
         if (this.stats.thirst < 0.0F) {
            this.stats.thirst = 0.0F;
         }

         var10000 = this.stats;
         var10000.hunger += var3.getHungerChange() * var2;
         var10000 = this.stats;
         var10000.endurance += var3.getEnduranceChange() * var2;
         var10000 = this.stats;
         var10000.stress += var3.getStressChange() * var2;
         var10000 = this.stats;
         var10000.fatigue += var3.getFatigueChange() * var2;
         IsoPlayer var8 = (IsoPlayer)Type.tryCastTo(this, IsoPlayer.class);
         if (var8 != null) {
            Nutrition var10 = var8.getNutrition();
            var10.setCalories(var10.getCalories() + var3.getCalories() * var2);
            var10.setCarbohydrates(var10.getCarbohydrates() + var3.getCarbohydrates() * var2);
            var10.setProteins(var10.getProteins() + var3.getProteins() * var2);
            var10.setLipids(var10.getLipids() + var3.getLipids() * var2);
         }

         this.BodyDamage.setPainReduction(this.BodyDamage.getPainReduction() + var3.getPainReduction() * var2);
         this.BodyDamage.setColdReduction(this.BodyDamage.getColdReduction() + (float)var3.getFluReduction() * var2);
         float var11;
         if (this.BodyDamage.getFoodSicknessLevel() > 0.0F && (float)var3.getReduceFoodSickness() > 0.0F && this.effectiveEdibleBuffTimer <= 0.0F) {
            var9 = this.BodyDamage.getFoodSicknessLevel();
            this.BodyDamage.setFoodSicknessLevel(this.BodyDamage.getFoodSicknessLevel() - (float)var3.getReduceFoodSickness() * var2);
            if (this.BodyDamage.getFoodSicknessLevel() < 0.0F) {
               this.BodyDamage.setFoodSicknessLevel(0.0F);
            }

            var11 = this.BodyDamage.getPoisonLevel();
            this.BodyDamage.setPoisonLevel(this.BodyDamage.getPoisonLevel() - (float)var3.getReduceFoodSickness() * var2);
            if (this.BodyDamage.getPoisonLevel() < 0.0F) {
               this.BodyDamage.setPoisonLevel(0.0F);
            }

            if (this.Traits.IronGut.isSet()) {
               this.effectiveEdibleBuffTimer = Rand.Next(80.0F, 150.0F);
            } else if (this.Traits.WeakStomach.isSet()) {
               this.effectiveEdibleBuffTimer = Rand.Next(120.0F, 230.0F);
            } else {
               this.effectiveEdibleBuffTimer = Rand.Next(200.0F, 280.0F);
            }
         }

         this.BodyDamage.JustAteFood(var3, var2);
         if (GameClient.bClient && this instanceof IsoPlayer && ((IsoPlayer)this).isLocalPlayer()) {
            GameClient.instance.eatFood((IsoPlayer)this, var3, var2);
         }

         if (var3.getOnEat() != null) {
            Object var12 = LuaManager.getFunctionObject(var3.getOnEat());
            if (var12 != null) {
               LuaManager.caller.pcallvoid(LuaManager.thread, var12, var1, this, BoxedStaticValues.toDouble((double)var2));
            }
         }

         if (var2 == 1.0F) {
            var3.setHungChange(0.0F);
            var3.UseItem();
         } else {
            var9 = var3.getHungChange();
            var11 = var3.getThirstChange();
            var3.multiplyFoodValues(1.0F - var2);
            if (var9 < 0.0F && (double)var3.getHungerChange() > -0.01D) {
               var3.setHungChange(0.0F);
               var3.UseItem();
               return true;
            }

            if (var9 == 0.0F && var11 < 0.0F && var3.getThirstChange() > -0.01F) {
               var3.setHungChange(0.0F);
               var3.UseItem();
               return true;
            }
         }

         return true;
      }
   }

   public boolean Eat(InventoryItem var1) {
      return this.Eat(var1, 1.0F);
   }

   public void FireCheck() {
      if (!this.OnFire) {
         if (!GameServer.bServer || !(this instanceof IsoPlayer)) {
            if (!GameClient.bClient || !this.isZombie() || !(this instanceof IsoZombie) || !((IsoZombie)this).isRemoteZombie()) {
               if (this.isZombie() && VirtualZombieManager.instance.isReused((IsoZombie)this)) {
                  DebugLog.log(DebugType.Zombie, "FireCheck running on REUSABLE ZOMBIE - IGNORED " + this);
               } else if (this.getVehicle() == null) {
                  if (this.square != null && !GameServer.bServer && (!GameClient.bClient || this instanceof IsoPlayer && ((IsoPlayer)this).isLocalPlayer() || this instanceof IsoZombie && !((IsoZombie)this).isRemoteZombie()) && this.square.getProperties().Is(IsoFlagType.burning)) {
                     if ((!(this instanceof IsoPlayer) || Rand.Next(Rand.AdjustForFramerate(70)) != 0) && !this.isZombie()) {
                        if (!(this instanceof IsoPlayer)) {
                           this.Health -= this.FireKillRate * GameTime.instance.getMultiplier() / 2.0F;
                           this.setAttackedBy((IsoGameCharacter)null);
                        } else {
                           float var1 = this.FireKillRate * GameTime.instance.getMultiplier() * GameTime.instance.getMinutesPerDay() / 1.6F / 2.0F;
                           this.BodyDamage.ReduceGeneralHealth(var1);
                           this.BodyDamage.OnFire(true);
                           this.forceAwake();
                        }

                        if (this.isDead()) {
                           IsoFireManager.RemoveBurningCharacter(this);
                        }
                     } else {
                        this.SetOnFire();
                     }
                  }

               }
            }
         }
      }
   }

   public String getPrimaryHandType() {
      return this.leftHandItem == null ? null : this.leftHandItem.getType();
   }

   public float getGlobalMovementMod(boolean var1) {
      return this.getCurrentState() != ClimbOverFenceState.instance() && this.getCurrentState() != ClimbThroughWindowState.instance() && this.getCurrentState() != ClimbOverWallState.instance() ? super.getGlobalMovementMod(var1) : 1.0F;
   }

   public float getMoveSpeed() {
      tempo2.x = this.getX() - this.getLx();
      tempo2.y = this.getY() - this.getLy();
      return tempo2.getLength();
   }

   public String getSecondaryHandType() {
      return this.rightHandItem == null ? null : this.rightHandItem.getType();
   }

   public boolean HasItem(String var1) {
      if (var1 == null) {
         return true;
      } else {
         return var1.equals(this.getSecondaryHandType()) || var1.equals(this.getPrimaryHandType()) || this.inventory.contains(var1);
      }
   }

   public void changeState(State var1) {
      this.stateMachine.changeState(var1, (Iterable)null);
   }

   public State getCurrentState() {
      return this.stateMachine.getCurrent();
   }

   public boolean isCurrentState(State var1) {
      if (this.stateMachine.isSubstate(var1)) {
         return true;
      } else {
         return this.stateMachine.getCurrent() == var1;
      }
   }

   public HashMap getStateMachineParams(State var1) {
      return (HashMap)this.StateMachineParams.computeIfAbsent(var1, (var0) -> {
         return new HashMap();
      });
   }

   public void setStateMachineLocked(boolean var1) {
      this.stateMachine.setLocked(var1);
   }

   public float Hit(HandWeapon var1, IsoGameCharacter var2, float var3, boolean var4, float var5) {
      return this.Hit(var1, var2, var3, var4, var5, false);
   }

   public float Hit(HandWeapon var1, IsoGameCharacter var2, float var3, boolean var4, float var5, boolean var6) {
      if (var2 != null && var1 != null) {
         if (!var4 && this.isZombie()) {
            IsoZombie var7 = (IsoZombie)this;
            var7.setHitTime(var7.getHitTime() + 1);
            if (var7.getHitTime() >= 4 && !var6) {
               var3 = (float)((double)var3 * (double)(var7.getHitTime() - 2) * 1.5D);
            }
         }

         if (var2 instanceof IsoPlayer && ((IsoPlayer)var2).bDoShove && !((IsoPlayer)var2).isAimAtFloor()) {
            var4 = true;
            var5 *= 1.5F;
         }

         LuaEventManager.triggerEvent("OnWeaponHitCharacter", var2, this, var1, var3);
         if (LuaHookManager.TriggerHook("WeaponHitCharacter", var2, this, var1, var3)) {
            return 0.0F;
         } else if (this.m_avoidDamage) {
            this.m_avoidDamage = false;
            return 0.0F;
         } else {
            if (this.noDamage) {
               var4 = true;
               this.noDamage = false;
            }

            if (this instanceof IsoSurvivor && !this.EnemyList.contains(var2)) {
               this.EnemyList.add(var2);
            }

            this.staggerTimeMod = var1.getPushBackMod() * var1.getKnockbackMod(var2) * var2.getShovingMod();
            if (this.isZombie() && Rand.Next(3) == 0 && GameServer.bServer) {
            }

            var2.addWorldSoundUnlessInvisible(5, 1, false);
            this.hitDir.x = this.getX();
            this.hitDir.y = this.getY();
            Vector2 var10000 = this.hitDir;
            var10000.x -= var2.getX();
            var10000 = this.hitDir;
            var10000.y -= var2.getY();
            this.getHitDir().normalize();
            var10000 = this.hitDir;
            var10000.x *= var1.getPushBackMod();
            var10000 = this.hitDir;
            var10000.y *= var1.getPushBackMod();
            this.hitDir.rotate(var1.HitAngleMod);
            this.setAttackedBy(var2);
            float var12 = var3;
            if (!var6) {
               var12 = this.processHitDamage(var1, var2, var3, var4, var5);
            }

            float var8 = 0.0F;
            if (var1.isTwoHandWeapon() && (var2.getPrimaryHandItem() != var1 || var2.getSecondaryHandItem() != var1)) {
               var8 = var1.getWeight() / 1.5F / 10.0F;
            }

            float var9 = (var1.getWeight() * 0.28F * var1.getFatigueMod(var2) * this.getFatigueMod() * var1.getEnduranceMod() * 0.3F + var8) * 0.04F;
            if (var2 instanceof IsoPlayer && var2.isAimAtFloor() && ((IsoPlayer)var2).bDoShove) {
               var9 *= 2.0F;
            }

            float var10;
            if (var1.isAimedFirearm()) {
               var10 = var12 * 0.7F;
            } else {
               var10 = var12 * 0.15F;
            }

            if (this.getHealth() < var12) {
               var10 = this.getHealth();
            }

            float var11 = var10 / var1.getMaxDamage();
            if (var11 > 1.0F) {
               var11 = 1.0F;
            }

            if (this.isCloseKilled()) {
               var11 = 0.2F;
            }

            if (var1.isUseEndurance()) {
               Stats var13 = var2.getStats();
               var13.endurance -= var9 * var11;
            }

            this.hitConsequences(var1, var2, var4, var12, var6);
            return var12;
         }
      } else {
         return 0.0F;
      }
   }

   public float processHitDamage(HandWeapon var1, IsoGameCharacter var2, float var3, boolean var4, float var5) {
      float var6 = var3 * var5;
      float var7 = var6;
      if (var4) {
         var7 = var6 / 2.7F;
      }

      float var8 = var7 * var2.getShovingMod();
      if (var8 > 1.0F) {
         var8 = 1.0F;
      }

      this.setHitForce(var8);
      if (var2.Traits.Strong.isSet() && !var1.isRanged()) {
         this.setHitForce(this.getHitForce() * 1.4F);
      }

      if (var2.Traits.Weak.isSet() && !var1.isRanged()) {
         this.setHitForce(this.getHitForce() * 0.6F);
      }

      float var9 = IsoUtils.DistanceTo(var2.getX(), var2.getY(), this.getX(), this.getY());
      var9 -= var1.getMinRange();
      var9 /= var1.getMaxRange(var2);
      var9 = 1.0F - var9;
      if (var9 > 1.0F) {
         var9 = 1.0F;
      }

      float var10 = var2.stats.endurance;
      var10 *= var2.knockbackAttackMod;
      if (var10 < 0.5F) {
         var10 *= 1.3F;
         if (var10 < 0.4F) {
            var10 = 0.4F;
         }

         this.setHitForce(this.getHitForce() * var10);
      }

      if (!var1.isRangeFalloff()) {
         var9 = 1.0F;
      }

      if (!var1.isShareDamage()) {
         var3 = 1.0F;
      }

      if (var2 instanceof IsoPlayer && !var4) {
         this.setHitForce(this.getHitForce() * 2.0F);
      }

      if (var2 instanceof IsoPlayer && !((IsoPlayer)var2).bDoShove) {
         Vector2 var11 = tempVector2_1.set(this.getX(), this.getY());
         Vector2 var12 = tempVector2_2.set(var2.getX(), var2.getY());
         var11.x -= var12.x;
         var11.y -= var12.y;
         Vector2 var13 = this.getVectorFromDirection(tempVector2_2);
         var11.normalize();
         float var14 = var11.dot(var13);
         if (var14 > -0.3F) {
            var6 *= 1.5F;
         }
      }

      if (this instanceof IsoPlayer) {
         var6 *= 0.4F;
      } else {
         var6 *= 1.5F;
      }

      int var15 = var2.getWeaponLevel();
      switch(var15) {
      case -1:
         var6 *= 0.3F;
         break;
      case 0:
         var6 *= 0.3F;
         break;
      case 1:
         var6 *= 0.4F;
         break;
      case 2:
         var6 *= 0.5F;
         break;
      case 3:
         var6 *= 0.6F;
         break;
      case 4:
         var6 *= 0.7F;
         break;
      case 5:
         var6 *= 0.8F;
         break;
      case 6:
         var6 *= 0.9F;
         break;
      case 7:
         var6 *= 1.0F;
         break;
      case 8:
         var6 *= 1.1F;
         break;
      case 9:
         var6 *= 1.2F;
         break;
      case 10:
         var6 *= 1.3F;
      }

      if (var2 instanceof IsoPlayer && var2.isAimAtFloor() && !var4 && !((IsoPlayer)var2).bDoShove) {
         var6 *= Math.max(5.0F, var1.getCritDmgMultiplier());
      }

      if (var2.isCriticalHit() && !var4) {
         var6 *= Math.max(2.0F, var1.getCritDmgMultiplier());
      }

      if (var1.isTwoHandWeapon() && !var2.isItemInBothHands(var1)) {
         var6 *= 0.5F;
      }

      return var6;
   }

   public void hitConsequences(HandWeapon var1, IsoGameCharacter var2, boolean var3, float var4, boolean var5) {
      if (!var3) {
         if (var1.isAimedFirearm()) {
            this.Health -= var4 * 0.7F;
         } else {
            this.Health -= var4 * 0.15F;
         }
      }

      if (this.isDead()) {
         if (!this.isOnKillDone() && this.shouldDoInventory()) {
            this.Kill(var2);
         }

         if (this instanceof IsoZombie && ((IsoZombie)this).upKillCount) {
            var2.setZombieKills(var2.getZombieKills() + 1);
         }

      } else {
         if (var1.isSplatBloodOnNoDeath()) {
            this.splatBlood(2, 0.2F);
         }

         if (var1.isKnockBackOnNoDeath() && var2.xp != null) {
            var2.xp.AddXP(PerkFactory.Perks.Strength, 2.0F);
         }

      }
   }

   public boolean IsAttackRange(float var1, float var2, float var3) {
      float var4 = 1.0F;
      float var5 = 0.0F;
      if (this.leftHandItem != null) {
         InventoryItem var6 = this.leftHandItem;
         if (var6 instanceof HandWeapon) {
            var4 = ((HandWeapon)var6).getMaxRange(this);
            var5 = ((HandWeapon)var6).getMinRange();
            var4 *= ((HandWeapon)this.leftHandItem).getRangeMod(this);
         }
      }

      if (Math.abs(var3 - this.getZ()) > 0.3F) {
         return false;
      } else {
         float var7 = IsoUtils.DistanceTo(var1, var2, this.getX(), this.getY());
         return var7 < var4 && var7 > var5;
      }
   }

   public boolean IsAttackRange(HandWeapon var1, IsoMovingObject var2, Vector3 var3, boolean var4) {
      if (var1 == null) {
         return false;
      } else {
         float var5 = Math.abs(var2.getZ() - this.getZ());
         if (!var1.isRanged() && var5 >= 0.5F) {
            return false;
         } else if (var5 > 3.3F) {
            return false;
         } else {
            float var6 = var1.getMaxRange(this);
            var6 *= var1.getRangeMod(this);
            float var7 = IsoUtils.DistanceToSquared(this.x, this.y, var3.x, var3.y);
            if (var4) {
               IsoZombie var8 = (IsoZombie)Type.tryCastTo(var2, IsoZombie.class);
               if (var8 != null && var7 < 4.0F && var8.target == this && (var8.isCurrentState(LungeState.instance()) || var8.isCurrentState(LungeNetworkState.instance()))) {
                  ++var6;
               }
            }

            return var7 < var6 * var6;
         }
      }
   }

   public boolean IsSpeaking() {
      return this.chatElement.IsSpeaking();
   }

   public void MoveForward(float var1, float var2, float var3, float var4) {
      if (!this.isCurrentState(SwipeStatePlayer.instance())) {
         float var5 = GameTime.instance.getMultiplier();
         this.setNx(this.getNx() + var2 * var1 * var5);
         this.setNy(this.getNy() + var3 * var1 * var5);
         this.DoFootstepSound(var1);
         if (!this.isZombie()) {
         }

      }
   }

   private void pathToAux(float var1, float var2, float var3) {
      boolean var4 = true;
      if ((int)var3 == (int)this.getZ() && IsoUtils.DistanceManhatten(var1, var2, this.x, this.y) <= 30.0F) {
         int var5 = (int)var1 / 10;
         int var6 = (int)var2 / 10;
         IsoChunk var7 = GameServer.bServer ? ServerMap.instance.getChunk(var5, var6) : IsoWorld.instance.CurrentCell.getChunkForGridSquare((int)var1, (int)var2, (int)var3);
         if (var7 != null) {
            byte var8 = 1;
            int var9 = var8 | 2;
            if (!this.isZombie()) {
               var9 |= 4;
            }

            var4 = !PolygonalMap2.instance.lineClearCollide(this.getX(), this.getY(), var1, var2, (int)var3, this.getPathFindBehavior2().getTargetChar(), var9);
         }
      }

      if (var4 && this.current != null && this.current.HasStairs() && !this.current.isSameStaircase((int)var1, (int)var2, (int)var3)) {
         var4 = false;
      }

      if (var4) {
         this.setVariable("bPathfind", false);
         this.setMoving(true);
      } else {
         this.setVariable("bPathfind", true);
         this.setMoving(false);
      }

   }

   public void pathToCharacter(IsoGameCharacter var1) {
      this.getPathFindBehavior2().pathToCharacter(var1);
      this.pathToAux(var1.getX(), var1.getY(), var1.getZ());
   }

   public void pathToLocation(int var1, int var2, int var3) {
      this.getPathFindBehavior2().pathToLocation(var1, var2, var3);
      this.pathToAux((float)var1 + 0.5F, (float)var2 + 0.5F, (float)var3);
   }

   public void pathToLocationF(float var1, float var2, float var3) {
      this.getPathFindBehavior2().pathToLocationF(var1, var2, var3);
      this.pathToAux(var1, var2, var3);
   }

   public void pathToSound(int var1, int var2, int var3) {
      this.getPathFindBehavior2().pathToSound(var1, var2, var3);
      this.pathToAux((float)var1 + 0.5F, (float)var2 + 0.5F, (float)var3);
   }

   public boolean CanAttack() {
      if (!this.isAttackAnim() && !this.getVariableBoolean("IsRacking") && !this.getVariableBoolean("IsUnloading") && StringUtils.isNullOrEmpty(this.getVariableString("RackWeapon"))) {
         if (GameClient.bClient && this instanceof IsoPlayer && ((IsoPlayer)this).isLocalPlayer() && (this.isCurrentState(PlayerHitReactionState.instance()) || this.isCurrentState(PlayerHitReactionPVPState.instance()))) {
            return false;
         } else if (this.isSitOnGround()) {
            return false;
         } else {
            InventoryItem var1 = this.leftHandItem;
            if (var1 instanceof HandWeapon && var1.getSwingAnim() != null) {
               this.useHandWeapon = (HandWeapon)var1;
            }

            if (this.useHandWeapon == null) {
               return true;
            } else if (this.useHandWeapon.getCondition() <= 0) {
               this.useHandWeapon = null;
               if (this.rightHandItem == this.leftHandItem) {
                  this.setSecondaryHandItem((InventoryItem)null);
               }

               this.setPrimaryHandItem((InventoryItem)null);
               if (this.getInventory() != null) {
                  this.getInventory().setDrawDirty(true);
               }

               return false;
            } else {
               float var2 = 12.0F;
               int var3 = this.Moodles.getMoodleLevel(MoodleType.Endurance);
               return !this.useHandWeapon.isCantAttackWithLowestEndurance() || var3 != 4;
            }
         }
      } else {
         return false;
      }
   }

   public void ReduceHealthWhenBurning() {
      if (this.OnFire) {
         if (this.isGodMod()) {
            this.StopBurning();
         } else if (!GameClient.bClient || !this.isZombie() || !(this instanceof IsoZombie) || !((IsoZombie)this).isRemoteZombie()) {
            if (!GameClient.bClient || !(this instanceof IsoPlayer) || !((IsoPlayer)this).bRemote) {
               if (this.isAlive()) {
                  if (!(this instanceof IsoPlayer)) {
                     if (this.isZombie()) {
                        this.Health -= this.FireKillRate / 20.0F * GameTime.instance.getMultiplier();
                        this.setAttackedBy((IsoGameCharacter)null);
                     } else {
                        this.Health -= this.FireKillRate * GameTime.instance.getMultiplier();
                     }
                  } else {
                     float var1 = this.FireKillRate * GameTime.instance.getMultiplier() * GameTime.instance.getMinutesPerDay() / 1.6F;
                     this.BodyDamage.ReduceGeneralHealth(var1);
                     this.BodyDamage.OnFire(true);
                  }

                  if (this.isDead()) {
                     IsoFireManager.RemoveBurningCharacter(this);
                     if (this.isZombie()) {
                        LuaEventManager.triggerEvent("OnZombieDead", this);
                        if (GameClient.bClient) {
                           this.setAttackedBy(IsoWorld.instance.CurrentCell.getFakeZombieForHit());
                        }
                     }
                  }
               }

               if (this instanceof IsoPlayer && Rand.Next(Rand.AdjustForFramerate(((IsoPlayer)this).IsRunning() ? 150 : 400)) == 0) {
                  this.StopBurning();
               }

            }
         }
      }
   }

   public void DrawSneezeText() {
      if (this.BodyDamage.IsSneezingCoughing() > 0) {
         String var1 = null;
         if (this.BodyDamage.IsSneezingCoughing() == 1) {
            var1 = Translator.getText("IGUI_PlayerText_Sneeze");
         }

         if (this.BodyDamage.IsSneezingCoughing() == 2) {
            var1 = Translator.getText("IGUI_PlayerText_Cough");
         }

         if (this.BodyDamage.IsSneezingCoughing() == 3) {
            var1 = Translator.getText("IGUI_PlayerText_SneezeMuffled");
         }

         if (this.BodyDamage.IsSneezingCoughing() == 4) {
            var1 = Translator.getText("IGUI_PlayerText_CoughMuffled");
         }

         float var2 = this.sx;
         float var3 = this.sy;
         var2 = (float)((int)var2);
         var3 = (float)((int)var3);
         var2 -= (float)((int)IsoCamera.getOffX());
         var3 -= (float)((int)IsoCamera.getOffY());
         var3 -= 48.0F;
         if (var1 != null) {
            TextManager.instance.DrawStringCentre(UIFont.Dialogue, (double)((int)var2), (double)((int)var3), var1, (double)this.SpeakColour.r, (double)this.SpeakColour.g, (double)this.SpeakColour.b, (double)this.SpeakColour.a);
         }
      }

   }

   public IsoSpriteInstance getSpriteDef() {
      if (this.def == null) {
         this.def = new IsoSpriteInstance();
      }

      return this.def;
   }

   public void render(float var1, float var2, float var3, ColorInfo var4, boolean var5, boolean var6, Shader var7) {
      if (!this.isAlphaAndTargetZero()) {
         if (!this.isSeatedInVehicle() || this.getVehicle().showPassenger(this)) {
            if (!this.isSpriteInvisible()) {
               if (!this.isAlphaZero()) {
                  if (!this.bUseParts && this.def == null) {
                     this.def = new IsoSpriteInstance(this.sprite);
                  }

                  SpriteRenderer.instance.glDepthMask(true);
                  IsoGridSquare var8;
                  if (this.bDoDefer && var3 - (float)((int)var3) > 0.2F) {
                     var8 = this.getCell().getGridSquare((int)var1, (int)var2, (int)var3 + 1);
                     if (var8 != null) {
                        var8.addDeferredCharacter(this);
                     }
                  }

                  var8 = this.getCurrentSquare();
                  if (PerformanceSettings.LightingFrameSkip < 3 && var8 != null) {
                     var8.interpolateLight(inf, var1 - (float)var8.getX(), var2 - (float)var8.getY());
                  } else {
                     inf.r = var4.r;
                     inf.g = var4.g;
                     inf.b = var4.b;
                     inf.a = var4.a;
                  }

                  if (Core.bDebug && DebugOptions.instance.PathfindRenderWaiting.getValue() && this.hasActiveModel()) {
                     if (this.getCurrentState() == PathFindState.instance() && this.finder.progress == AStarPathFinder.PathFindProgress.notyetfound) {
                        this.legsSprite.modelSlot.model.tintR = 1.0F;
                        this.legsSprite.modelSlot.model.tintG = 0.0F;
                        this.legsSprite.modelSlot.model.tintB = 0.0F;
                     } else {
                        this.legsSprite.modelSlot.model.tintR = 1.0F;
                        this.legsSprite.modelSlot.model.tintG = 1.0F;
                        this.legsSprite.modelSlot.model.tintB = 1.0F;
                     }
                  }

                  if (this.dir == IsoDirections.Max) {
                     this.dir = IsoDirections.N;
                  }

                  if (this.sprite != null && !this.legsSprite.hasActiveModel()) {
                     this.checkDrawWeaponPre(var1, var2, var3, var4);
                  }

                  lastRenderedRendered = lastRendered;
                  lastRendered = this;
                  if (this.bUpdateModelTextures && this.hasActiveModel()) {
                     this.bUpdateModelTextures = false;
                     this.textureCreator = ModelInstanceTextureCreator.alloc();
                     this.textureCreator.init(this);
                  }

                  if (this.bUpdateEquippedTextures && this.hasActiveModel()) {
                     this.bUpdateEquippedTextures = false;
                     if (this.primaryHandModel != null && this.primaryHandModel.getTextureInitializer() != null) {
                        this.primaryHandModel.getTextureInitializer().setDirty();
                     }

                     if (this.secondaryHandModel != null && this.secondaryHandModel.getTextureInitializer() != null) {
                        this.secondaryHandModel.getTextureInitializer().setDirty();
                     }
                  }

                  float var9 = (float)Core.TileScale;
                  float var10 = this.offsetX + 1.0F * var9;
                  float var11 = this.offsetY + -89.0F * var9;
                  if (this.sprite != null) {
                     this.def.setScale(var9, var9);
                     if (!this.bUseParts) {
                        this.sprite.render(this.def, this, var1, var2, var3, this.dir, var10, var11, inf, true);
                     } else if (this.legsSprite.hasActiveModel()) {
                        this.legsSprite.renderActiveModel();
                     } else if (!this.renderTextureInsteadOfModel(var1, var2)) {
                        this.def.Flip = false;
                        inf.r = 1.0F;
                        inf.g = 1.0F;
                        inf.b = 1.0F;
                        inf.a = this.def.alpha * 0.4F;
                        this.legsSprite.renderCurrentAnim(this.def, this, var1, var2, var3, this.dir, var10, var11, inf, false, (Consumer)null);
                     }
                  }

                  int var12;
                  if (this.AttachedAnimSprite != null) {
                     for(var12 = 0; var12 < this.AttachedAnimSprite.size(); ++var12) {
                        IsoSpriteInstance var13 = (IsoSpriteInstance)this.AttachedAnimSprite.get(var12);
                        var13.update();
                        float var14 = inf.a;
                        inf.a = var13.alpha;
                        var13.SetTargetAlpha(this.getTargetAlpha());
                        var13.render(this, var1, var2, var3, this.dir, var10, var11, inf);
                        inf.a = var14;
                     }
                  }

                  for(var12 = 0; var12 < this.inventory.Items.size(); ++var12) {
                     InventoryItem var15 = (InventoryItem)this.inventory.Items.get(var12);
                     if (var15 instanceof IUpdater) {
                        ((IUpdater)var15).render();
                     }
                  }

               }
            }
         }
      }
   }

   public void renderServerGUI() {
      if (this instanceof IsoPlayer) {
         this.setSceneCulled(false);
      }

      if (this.bUpdateModelTextures && this.hasActiveModel()) {
         this.bUpdateModelTextures = false;
         this.textureCreator = ModelInstanceTextureCreator.alloc();
         this.textureCreator.init(this);
      }

      float var1 = (float)Core.TileScale;
      float var2 = this.offsetX + 1.0F * var1;
      float var3 = this.offsetY + -89.0F * var1;
      if (this.sprite != null) {
         this.def.setScale(var1, var1);
         inf.r = 1.0F;
         inf.g = 1.0F;
         inf.b = 1.0F;
         inf.a = this.def.alpha * 0.4F;
         if (!this.isbUseParts()) {
            this.sprite.render(this.def, this, this.x, this.y, this.z, this.dir, var2, var3, inf, true);
         } else {
            this.def.Flip = false;
            this.legsSprite.render(this.def, this, this.x, this.y, this.z, this.dir, var2, var3, inf, true);
         }
      }

      if (Core.bDebug && this.hasActiveModel()) {
         if (this instanceof IsoZombie) {
            int var4 = (int)IsoUtils.XToScreenExact(this.x, this.y, this.z, 0);
            int var5 = (int)IsoUtils.YToScreenExact(this.x, this.y, this.z, 0);
            TextManager.instance.DrawString((double)var4, (double)var5, "ID: " + this.getOnlineID());
            TextManager.instance.DrawString((double)var4, (double)(var5 + 10), "State: " + this.getCurrentStateName());
            TextManager.instance.DrawString((double)var4, (double)(var5 + 20), "Health: " + this.getHealth());
         }

         Vector2 var6 = tempo;
         this.getDeferredMovement(var6);
         this.drawDirectionLine(var6, 1000.0F * var6.getLength() / GameTime.instance.getMultiplier() * 2.0F, 1.0F, 0.5F, 0.5F);
      }

   }

   protected float getAlphaUpdateRateMul() {
      float var1 = super.getAlphaUpdateRateMul();
      if (IsoCamera.CamCharacter.Traits.ShortSighted.isSet()) {
         var1 /= 2.0F;
      }

      if (IsoCamera.CamCharacter.Traits.EagleEyed.isSet()) {
         var1 *= 1.5F;
      }

      return var1;
   }

   protected boolean isUpdateAlphaEnabled() {
      return !this.isTeleporting();
   }

   protected boolean isUpdateAlphaDuringRender() {
      return false;
   }

   public boolean isSeatedInVehicle() {
      return this.vehicle != null && this.vehicle.getSeat(this) != -1;
   }

   public void renderObjectPicker(float var1, float var2, float var3, ColorInfo var4) {
      if (!this.bUseParts) {
         this.sprite.renderObjectPicker(this.def, this, this.dir);
      } else {
         this.legsSprite.renderObjectPicker(this.def, this, this.dir);
      }

   }

   static Vector2 closestpointonline(double var0, double var2, double var4, double var6, double var8, double var10, Vector2 var12) {
      double var13 = var6 - var2;
      double var15 = var0 - var4;
      double var17 = (var6 - var2) * var0 + (var0 - var4) * var2;
      double var19 = -var15 * var8 + var13 * var10;
      double var21 = var13 * var13 - -var15 * var15;
      double var23;
      double var25;
      if (var21 != 0.0D) {
         var23 = (var13 * var17 - var15 * var19) / var21;
         var25 = (var13 * var19 - -var15 * var17) / var21;
      } else {
         var23 = var8;
         var25 = var10;
      }

      return var12.set((float)var23, (float)var25);
   }

   public void renderShadow(float var1, float var2, float var3) {
      if (this.doRenderShadow) {
         if (!this.isAlphaAndTargetZero()) {
            if (!this.isSeatedInVehicle()) {
               IsoGridSquare var4 = this.getCurrentSquare();
               if (var4 != null) {
                  int var5 = IsoCamera.frameState.playerIndex;
                  Vector3f var6 = IsoGameCharacter.L_renderShadow.forward;
                  Vector2 var7 = this.getAnimVector(tempo2);
                  var6.set(var7.x, var7.y, 0.0F);
                  float var8 = 0.45F;
                  float var9 = 1.4F;
                  float var10 = 1.125F;
                  float var11 = this.getAlpha(var5);
                  if (this.hasActiveModel() && this.hasAnimationPlayer() && this.getAnimationPlayer().isReady()) {
                     AnimationPlayer var12 = this.getAnimationPlayer();
                     Vector3 var13 = IsoGameCharacter.L_renderShadow.v1;
                     Model.BoneToWorldCoords(this, var12.getSkinningBoneIndex("Bip01_Head", -1), var13);
                     float var14 = var13.x;
                     float var15 = var13.y;
                     Model.BoneToWorldCoords(this, var12.getSkinningBoneIndex("Bip01_L_Foot", -1), var13);
                     float var16 = var13.x;
                     float var17 = var13.y;
                     Model.BoneToWorldCoords(this, var12.getSkinningBoneIndex("Bip01_R_Foot", -1), var13);
                     float var18 = var13.x;
                     float var19 = var13.y;
                     Vector3f var20 = IsoGameCharacter.L_renderShadow.v3;
                     float var21 = 0.0F;
                     float var22 = 0.0F;
                     Vector2 var23 = closestpointonline((double)var1, (double)var2, (double)(var1 + var6.x), (double)(var2 + var6.y), (double)var14, (double)var15, tempo);
                     float var24 = var23.x;
                     float var25 = var23.y;
                     float var26 = var23.set(var24 - var1, var25 - var2).getLength();
                     if (var26 > 0.001F) {
                        var20.set(var24 - var1, var25 - var2, 0.0F).normalize();
                        if (var6.dot(var20) > 0.0F) {
                           var21 = Math.max(var21, var26);
                        } else {
                           var22 = Math.max(var22, var26);
                        }
                     }

                     var23 = closestpointonline((double)var1, (double)var2, (double)(var1 + var6.x), (double)(var2 + var6.y), (double)var16, (double)var17, tempo);
                     var24 = var23.x;
                     var25 = var23.y;
                     var26 = var23.set(var24 - var1, var25 - var2).getLength();
                     if (var26 > 0.001F) {
                        var20.set(var24 - var1, var25 - var2, 0.0F).normalize();
                        if (var6.dot(var20) > 0.0F) {
                           var21 = Math.max(var21, var26);
                        } else {
                           var22 = Math.max(var22, var26);
                        }
                     }

                     var23 = closestpointonline((double)var1, (double)var2, (double)(var1 + var6.x), (double)(var2 + var6.y), (double)var18, (double)var19, tempo);
                     var24 = var23.x;
                     var25 = var23.y;
                     var26 = var23.set(var24 - var1, var25 - var2).getLength();
                     if (var26 > 0.001F) {
                        var20.set(var24 - var1, var25 - var2, 0.0F).normalize();
                        if (var6.dot(var20) > 0.0F) {
                           var21 = Math.max(var21, var26);
                        } else {
                           var22 = Math.max(var22, var26);
                        }
                     }

                     var9 = (var21 + 0.35F) * 1.35F;
                     var10 = (var22 + 0.35F) * 1.35F;
                     float var27 = 0.1F * (GameTime.getInstance().getMultiplier() / 1.6F);
                     var27 = PZMath.clamp(var27, 0.0F, 1.0F);
                     if (this.shadowTick != IngameState.instance.numberTicks - 1L) {
                        this.m_shadowFM = var9;
                        this.m_shadowBM = var10;
                     }

                     this.shadowTick = IngameState.instance.numberTicks;
                     this.m_shadowFM = PZMath.lerp(this.m_shadowFM, var9, var27);
                     var9 = this.m_shadowFM;
                     this.m_shadowBM = PZMath.lerp(this.m_shadowBM, var10, var27);
                     var10 = this.m_shadowBM;
                  } else if (this.isZombie() && this.isCurrentState(FakeDeadZombieState.instance())) {
                     var11 = 1.0F;
                  } else if (this.isSceneCulled()) {
                     return;
                  }

                  ColorInfo var28 = var4.lighting[var5].lightInfo();
                  IsoDeadBody.renderShadow(var1, var2, var3, var6, var8, var9, var10, var28, var11);
               }
            }
         }
      }
   }

   public boolean isMaskClicked(int var1, int var2, boolean var3) {
      if (this.sprite == null) {
         return false;
      } else {
         return !this.bUseParts ? super.isMaskClicked(var1, var2, var3) : this.legsSprite.isMaskClicked(this.dir, var1, var2, var3);
      }
   }

   public void setHaloNote(String var1) {
      this.setHaloNote(var1, this.haloDispTime);
   }

   public void setHaloNote(String var1, float var2) {
      this.setHaloNote(var1, 0, 255, 0, var2);
   }

   public void setHaloNote(String var1, int var2, int var3, int var4, float var5) {
      if (this.haloNote != null && var1 != null) {
         this.haloDispTime = var5;
         this.haloNote.setDefaultColors(var2, var3, var4);
         this.haloNote.ReadString(var1);
         this.haloNote.setInternalTickClock(this.haloDispTime);
      }

   }

   public float getHaloTimerCount() {
      return this.haloNote != null ? this.haloNote.getInternalClock() : 0.0F;
   }

   public void DoSneezeText() {
      if (this.BodyDamage != null) {
         if (this.BodyDamage.IsSneezingCoughing() > 0) {
            String var1 = null;
            int var10002;
            if (this.BodyDamage.IsSneezingCoughing() == 1) {
               var1 = Translator.getText("IGUI_PlayerText_Sneeze");
               var10002 = Rand.Next(2);
               this.setVariable("Ext", "Sneeze" + (var10002 + 1));
            }

            if (this.BodyDamage.IsSneezingCoughing() == 2) {
               var1 = Translator.getText("IGUI_PlayerText_Cough");
               this.setVariable("Ext", "Cough");
            }

            if (this.BodyDamage.IsSneezingCoughing() == 3) {
               var1 = Translator.getText("IGUI_PlayerText_SneezeMuffled");
               var10002 = Rand.Next(2);
               this.setVariable("Ext", "Sneeze" + (var10002 + 1));
            }

            if (this.BodyDamage.IsSneezingCoughing() == 4) {
               var1 = Translator.getText("IGUI_PlayerText_CoughMuffled");
               this.setVariable("Ext", "Cough");
            }

            if (var1 != null) {
               this.Say(var1);
               this.reportEvent("EventDoExt");
            }
         }

      }
   }

   public String getSayLine() {
      return this.chatElement.getSayLine();
   }

   public void setSayLine(String var1) {
      this.Say(var1);
   }

   public ChatMessage getLastChatMessage() {
      return this.lastChatMessage;
   }

   public void setLastChatMessage(ChatMessage var1) {
      this.lastChatMessage = var1;
   }

   public String getLastSpokenLine() {
      return this.lastSpokenLine;
   }

   public void setLastSpokenLine(String var1) {
      this.lastSpokenLine = var1;
   }

   protected void doSleepSpeech() {
      ++this.sleepSpeechCnt;
      if ((float)this.sleepSpeechCnt > (float)(250 * PerformanceSettings.getLockFPS()) / 30.0F) {
         this.sleepSpeechCnt = 0;
         if (sleepText == null) {
            sleepText = "ZzzZZZzzzz";
            ChatElement.addNoLogText(sleepText);
         }

         this.SayWhisper(sleepText);
      }

   }

   public void SayDebug(String var1) {
      this.chatElement.SayDebug(0, var1);
   }

   public void SayDebug(int var1, String var2) {
      this.chatElement.SayDebug(var1, var2);
   }

   public int getMaxChatLines() {
      return this.chatElement.getMaxChatLines();
   }

   public void Say(String var1) {
      if (!this.isZombie()) {
         this.ProcessSay(var1, this.SpeakColour.r, this.SpeakColour.g, this.SpeakColour.b, 30.0F, "default");
      }
   }

   public void Say(String var1, float var2, float var3, float var4, UIFont var5, float var6, String var7) {
      this.ProcessSay(var1, var2, var3, var4, var6, var7);
   }

   public void SayWhisper(String var1) {
      this.ProcessSay(var1, this.SpeakColour.r, this.SpeakColour.g, this.SpeakColour.b, 10.0F, "whisper");
   }

   public void SayShout(String var1) {
      this.ProcessSay(var1, this.SpeakColour.r, this.SpeakColour.g, this.SpeakColour.b, 60.0F, "shout");
   }

   private void ProcessSay(String var1, float var2, float var3, float var4, float var5, String var6) {
      if (this.AllowConversation) {
         if (TutorialManager.instance.ProfanityFilter) {
            var1 = ProfanityFilter.getInstance().filterString(var1);
         }

         if (var6.equals("default")) {
            ChatManager.getInstance().showInfoMessage(((IsoPlayer)this).getUsername(), var1);
            this.lastSpokenLine = var1;
         } else if (var6.equals("whisper")) {
            this.lastSpokenLine = var1;
         } else if (var6.equals("shout")) {
            ChatManager.getInstance().sendMessageToChat(((IsoPlayer)this).getUsername(), ChatType.shout, var1);
            this.lastSpokenLine = var1;
         } else if (var6.equals("radio")) {
            UIFont var7 = UIFont.Medium;
            boolean var8 = true;
            boolean var9 = true;
            boolean var10 = true;
            boolean var11 = false;
            boolean var12 = false;
            boolean var13 = true;
            this.chatElement.addChatLine(var1, var2, var3, var4, var7, var5, var6, var8, var9, var10, var11, var12, var13);
            if (ZomboidRadio.isStaticSound(var1)) {
               ChatManager.getInstance().showStaticRadioSound(var1);
            } else {
               ChatManager.getInstance().showRadioMessage(var1);
            }
         }

      }
   }

   public void addLineChatElement(String var1) {
      this.addLineChatElement(var1, 1.0F, 1.0F, 1.0F);
   }

   public void addLineChatElement(String var1, float var2, float var3, float var4) {
      this.addLineChatElement(var1, var2, var3, var4, UIFont.Dialogue, 30.0F, "default");
   }

   public void addLineChatElement(String var1, float var2, float var3, float var4, UIFont var5, float var6, String var7) {
      this.addLineChatElement(var1, var2, var3, var4, var5, var6, var7, false, false, false, false, false, true);
   }

   public void addLineChatElement(String var1, float var2, float var3, float var4, UIFont var5, float var6, String var7, boolean var8, boolean var9, boolean var10, boolean var11, boolean var12, boolean var13) {
      this.chatElement.addChatLine(var1, var2, var3, var4, var5, var6, var7, var8, var9, var10, var11, var12, var13);
   }

   protected boolean playerIsSelf() {
      return IsoPlayer.getInstance() == this;
   }

   public int getUserNameHeight() {
      if (!GameClient.bClient) {
         return 0;
      } else {
         return this.userName != null ? this.userName.getHeight() : 0;
      }
   }

   protected void initTextObjects() {
      this.hasInitTextObjects = true;
      if (this instanceof IsoPlayer) {
         this.chatElement.setMaxChatLines(5);
         if (IsoPlayer.getInstance() != null) {
            System.out.println("FirstNAME:" + IsoPlayer.getInstance().username);
         }

         this.isoPlayer = (IsoPlayer)this;
         if (this.isoPlayer.username != null) {
            this.userName = new TextDrawObject();
            this.userName.setAllowAnyImage(true);
            this.userName.setDefaultFont(UIFont.Small);
            this.userName.setDefaultColors(255, 255, 255, 255);
            this.updateUserName();
         }

         if (this.haloNote == null) {
            this.haloNote = new TextDrawObject();
            this.haloNote.setDefaultFont(UIFont.Small);
            this.haloNote.setDefaultColors(0, 255, 0);
            this.haloNote.setDrawBackground(true);
            this.haloNote.setAllowImages(true);
            this.haloNote.setAllowAnyImage(true);
            this.haloNote.setOutlineColors(0.0F, 0.0F, 0.0F, 0.33F);
         }
      }

   }

   protected void updateUserName() {
      if (this.userName != null && this.isoPlayer != null) {
         String var1 = this.isoPlayer.getUsername(true);
         if (this != IsoPlayer.getInstance() && this.isInvisible() && IsoPlayer.getInstance() != null && IsoPlayer.getInstance().accessLevel.equals("") && (!Core.bDebug || !DebugOptions.instance.CheatPlayerSeeEveryone.getValue())) {
            this.userName.ReadString("");
            return;
         }

         Faction var2 = Faction.getPlayerFaction(this.isoPlayer);
         if (var2 != null) {
            if (!this.isoPlayer.showTag && this.isoPlayer != IsoPlayer.getInstance() && Faction.getPlayerFaction(IsoPlayer.getInstance()) != var2) {
               this.isoPlayer.tagPrefix = "";
            } else {
               this.isoPlayer.tagPrefix = var2.getTag();
               if (var2.getTagColor() != null) {
                  this.isoPlayer.setTagColor(var2.getTagColor());
               }
            }
         } else {
            this.isoPlayer.tagPrefix = "";
         }

         boolean var3 = this.isoPlayer != null && this.isoPlayer.bRemote || Core.getInstance().isShowYourUsername();
         boolean var10000;
         if (IsoCamera.CamCharacter instanceof IsoPlayer && !((IsoPlayer)IsoCamera.CamCharacter).accessLevel.equals("")) {
            var10000 = true;
         } else {
            var10000 = false;
         }

         boolean var5 = IsoCamera.CamCharacter instanceof IsoPlayer && ((IsoPlayer)IsoCamera.CamCharacter).canSeeAll;
         if (!ServerOptions.instance.DisplayUserName.getValue() && !var5) {
            var3 = false;
         }

         if (!var3) {
            var1 = "";
         }

         if (var3 && this.isoPlayer.tagPrefix != null && !this.isoPlayer.tagPrefix.equals("")) {
            var1 = "[col=" + (new Float(this.isoPlayer.getTagColor().r * 255.0F)).intValue() + "," + (new Float(this.isoPlayer.getTagColor().g * 255.0F)).intValue() + "," + (new Float(this.isoPlayer.getTagColor().b * 255.0F)).intValue() + "][" + this.isoPlayer.tagPrefix + "][/] " + var1;
         }

         if (var3 && !this.isoPlayer.accessLevel.equals("") && this.isoPlayer.isShowAdminTag()) {
            String var8 = (String)this.namesPrefix.get(this.isoPlayer.accessLevel);
            var1 = var8 + var1;
         }

         if (var3 && !this.isoPlayer.isSafety() && ServerOptions.instance.ShowSafety.getValue()) {
            var1 = var1 + " [img=media/ui/Skull.png]";
         }

         if (this.isoPlayer.isSpeek && !this.isoPlayer.isVoiceMute) {
            var1 = "[img=media/ui/voiceon.png] " + var1;
         }

         if (this.isoPlayer.isVoiceMute) {
            var1 = "[img=media/ui/voicemuted.png] " + var1;
         }

         BaseVehicle var6 = IsoCamera.CamCharacter == this.isoPlayer ? this.isoPlayer.getNearVehicle() : null;
         if (this.getVehicle() == null && var6 != null && (this.isoPlayer.getInventory().haveThisKeyId(var6.getKeyId()) != null || var6.isHotwired() || SandboxOptions.getInstance().VehicleEasyUse.getValue())) {
            Color var7 = Color.HSBtoRGB(var6.colorHue, var6.colorSaturation * 0.5F, var6.colorValue);
            int var9 = var7.getRedByte();
            var1 = " [img=media/ui/CarKey.png," + var9 + "," + var7.getGreenByte() + "," + var7.getBlueByte() + "]" + var1;
         }

         if (!var1.equals(this.userName.getOriginal())) {
            this.userName.ReadString(var1);
         }
      }

   }

   public void updateTextObjects() {
      if (!GameServer.bServer) {
         if (!this.hasInitTextObjects) {
            this.initTextObjects();
         }

         if (!this.Speaking) {
            this.DoSneezeText();
            if (this.isAsleep() && this.getCurrentSquare() != null && this.getCurrentSquare().getCanSee(0)) {
               this.doSleepSpeech();
            }
         }

         if (this.isoPlayer != null) {
            this.radioEquipedCheck();
         }

         this.Speaking = false;
         this.drawUserName = false;
         this.canSeeCurrent = false;
         if (this.haloNote != null && this.haloNote.getInternalClock() > 0.0F) {
            this.haloNote.updateInternalTickClock();
         }

         this.legsSprite.PlayAnim("ZombieWalk1");
         this.chatElement.update();
         this.Speaking = this.chatElement.IsSpeaking();
         if (!this.Speaking || this.isDead()) {
            this.Speaking = false;
            this.callOut = false;
         }

      }
   }

   public void renderlast() {
      super.renderlast();
      int var1 = IsoCamera.frameState.playerIndex;
      float var2 = this.x;
      float var3 = this.y;
      if (this.sx == 0.0F && this.def != null) {
         this.sx = IsoUtils.XToScreen(var2 + this.def.offX, var3 + this.def.offY, this.z + this.def.offZ, 0);
         this.sy = IsoUtils.YToScreen(var2 + this.def.offX, var3 + this.def.offY, this.z + this.def.offZ, 0);
         this.sx -= this.offsetX - 8.0F;
         this.sy -= this.offsetY - 60.0F;
      }

      float var4;
      float var5;
      float var6;
      float var22;
      if (this.hasInitTextObjects && this.isoPlayer != null || this.chatElement.getHasChatToDisplay()) {
         var4 = IsoUtils.XToScreen(var2, var3, this.getZ(), 0);
         var5 = IsoUtils.YToScreen(var2, var3, this.getZ(), 0);
         var4 = var4 - IsoCamera.getOffX() - this.offsetX;
         var5 = var5 - IsoCamera.getOffY() - this.offsetY;
         var5 -= (float)(128 / (2 / Core.TileScale));
         var6 = Core.getInstance().getZoom(var1);
         var4 /= var6;
         var5 /= var6;
         this.canSeeCurrent = true;
         this.drawUserName = false;
         if (this.isoPlayer != null && (this == IsoCamera.frameState.CamCharacter || this.getCurrentSquare() != null && this.getCurrentSquare().getCanSee(var1)) || IsoPlayer.getInstance().isCanSeeAll()) {
            if (this == IsoPlayer.getInstance()) {
               this.canSeeCurrent = true;
            }

            if (GameClient.bClient && this.userName != null && this.doRenderShadow) {
               this.drawUserName = false;
               if (ServerOptions.getInstance().MouseOverToSeeDisplayName.getValue() && this != IsoPlayer.getInstance() && !IsoPlayer.getInstance().isCanSeeAll()) {
                  IsoObjectPicker.ClickObject var7 = IsoObjectPicker.Instance.ContextPick(Mouse.getXA(), Mouse.getYA());
                  if (var7 != null && var7.tile != null) {
                     for(int var8 = var7.tile.square.getX() - 1; var8 < var7.tile.square.getX() + 2; ++var8) {
                        for(int var9 = var7.tile.square.getY() - 1; var9 < var7.tile.square.getY() + 2; ++var9) {
                           IsoGridSquare var10 = IsoCell.getInstance().getGridSquare(var8, var9, var7.tile.square.getZ());
                           if (var10 != null) {
                              for(int var11 = 0; var11 < var10.getMovingObjects().size(); ++var11) {
                                 IsoMovingObject var12 = (IsoMovingObject)var10.getMovingObjects().get(var11);
                                 if (var12 instanceof IsoPlayer && this == var12) {
                                    this.drawUserName = true;
                                    break;
                                 }
                              }

                              if (this.drawUserName) {
                                 break;
                              }
                           }

                           if (this.drawUserName) {
                              break;
                           }
                        }
                     }
                  }
               } else {
                  this.drawUserName = true;
               }

               if (this.drawUserName) {
                  this.updateUserName();
               }
            }

            if (!GameClient.bClient && this.isoPlayer != null && this.isoPlayer.getVehicle() == null) {
               String var16 = "";
               BaseVehicle var21 = this.isoPlayer.getNearVehicle();
               if (this.getVehicle() == null && var21 != null && var21.getPartById("Engine") != null && (this.isoPlayer.getInventory().haveThisKeyId(var21.getKeyId()) != null || var21.isHotwired() || SandboxOptions.getInstance().VehicleEasyUse.getValue()) && UIManager.VisibleAllUI) {
                  Color var27 = Color.HSBtoRGB(var21.colorHue, var21.colorSaturation * 0.5F, var21.colorValue, IsoGameCharacter.L_renderLast.color);
                  int var10000 = var27.getRedByte();
                  var16 = " [img=media/ui/CarKey.png," + var10000 + "," + var27.getGreenByte() + "," + var27.getBlueByte() + "]";
               }

               if (!var16.equals("")) {
                  this.userName.ReadString(var16);
                  this.drawUserName = true;
               }
            }
         }

         if (this.isoPlayer != null && this.hasInitTextObjects && (this.playerIsSelf() || this.canSeeCurrent)) {
            if (this.canSeeCurrent && this.drawUserName) {
               var5 -= (float)this.userName.getHeight();
               this.userName.AddBatchedDraw((double)((int)var4), (double)((int)var5), true);
            }

            if (this.playerIsSelf()) {
               ActionProgressBar var20 = UIManager.getProgressBar((double)var1);
               if (var20 != null && var20.isVisible()) {
                  var5 -= (float)(var20.getHeight().intValue() + 2);
               }
            }

            if (this.playerIsSelf() && this.haloNote != null && this.haloNote.getInternalClock() > 0.0F) {
               var22 = this.haloNote.getInternalClock() / (this.haloDispTime / 4.0F);
               var22 = PZMath.min(var22, 1.0F);
               var5 -= (float)(this.haloNote.getHeight() + 2);
               this.haloNote.AddBatchedDraw((double)((int)var4), (double)((int)var5), true, var22);
            }
         }

         boolean var24 = false;
         if (IsoPlayer.getInstance() != this && this.equipedRadio != null && this.equipedRadio.getDeviceData() != null && this.equipedRadio.getDeviceData().getHeadphoneType() >= 0) {
            var24 = true;
         }

         if (this.equipedRadio != null && this.equipedRadio.getDeviceData() != null && !this.equipedRadio.getDeviceData().getIsTurnedOn()) {
            var24 = true;
         }

         boolean var23 = GameClient.bClient && IsoCamera.CamCharacter instanceof IsoPlayer && !((IsoPlayer)IsoCamera.CamCharacter).accessLevel.equals("");
         if (!this.m_invisible || this == IsoCamera.frameState.CamCharacter || var23) {
            this.chatElement.renderBatched(IsoPlayer.getPlayerIndex(), (int)var4, (int)var5, var24);
         }
      }

      Vector2 var13;
      AnimationPlayer var19;
      if (Core.bDebug && DebugOptions.instance.Character.Debug.Render.Angle.getValue() && this.hasActiveModel()) {
         var13 = tempo;
         var19 = this.getAnimationPlayer();
         var13.set(this.dir.ToVector());
         this.drawDirectionLine(var13, 2.4F, 0.0F, 1.0F, 0.0F);
         var13.setLengthAndDirection(this.getLookAngleRadians(), 1.0F);
         this.drawDirectionLine(var13, 2.0F, 1.0F, 1.0F, 1.0F);
         var13.setLengthAndDirection(this.getAnimAngleRadians(), 1.0F);
         this.drawDirectionLine(var13, 2.0F, 1.0F, 1.0F, 0.0F);
         var6 = this.getForwardDirection().getDirection();
         var13.setLengthAndDirection(var6, 1.0F);
         this.drawDirectionLine(var13, 2.0F, 0.0F, 0.0F, 1.0F);
      }

      if (Core.bDebug && DebugOptions.instance.Character.Debug.Render.DeferredMovement.getValue() && this.hasActiveModel()) {
         var13 = tempo;
         var19 = this.getAnimationPlayer();
         this.getDeferredMovement(var13);
         this.drawDirectionLine(var13, 1000.0F * var13.getLength() / GameTime.instance.getMultiplier() * 2.0F, 1.0F, 0.5F, 0.5F);
      }

      if (Core.bDebug && DebugOptions.instance.Character.Debug.Render.DeferredAngles.getValue() && this.hasActiveModel()) {
         var13 = tempo;
         var19 = this.getAnimationPlayer();
         this.getDeferredMovement(var13);
         this.drawDirectionLine(var13, 1000.0F * var13.getLength() / GameTime.instance.getMultiplier() * 2.0F, 1.0F, 0.5F, 0.5F);
      }

      if (Core.bDebug && DebugOptions.instance.Character.Debug.Render.AimCone.getValue()) {
         this.debugAim();
      }

      if (Core.bDebug && DebugOptions.instance.Character.Debug.Render.TestDotSide.getValue()) {
         this.debugTestDotSide();
      }

      if (Core.bDebug && DebugOptions.instance.Character.Debug.Render.Vision.getValue()) {
         this.debugVision();
      }

      if (Core.bDebug) {
         IsoZombie var14;
         Color var25;
         if (DebugOptions.instance.MultiplayerShowZombieMultiplier.getValue() && this instanceof IsoZombie) {
            var14 = (IsoZombie)this;
            byte var15 = var14.canHaveMultipleHits();
            if (var15 == 0) {
               var25 = Colors.Green;
            } else if (var15 == 1) {
               var25 = Colors.Yellow;
            } else {
               var25 = Colors.Red;
            }

            LineDrawer.DrawIsoCircle(this.x, this.y, this.z, 0.45F, 4, var25.r, var25.g, var25.b, 0.5F);
            TextManager.instance.DrawStringCentre(UIFont.DebugConsole, (double)IsoUtils.XToScreenExact(this.x + 0.4F, this.y + 0.4F, this.z, 0), (double)IsoUtils.YToScreenExact(this.x + 0.4F, this.y - 1.4F, this.z, 0), String.valueOf(var14.OnlineID), (double)var25.r, (double)var25.g, (double)var25.b, (double)var25.a);
         }

         if (DebugOptions.instance.MultiplayerShowZombieOwner.getValue() && this instanceof IsoZombie) {
            var14 = (IsoZombie)this;
            if (var14.isDead()) {
               var25 = Colors.Yellow;
            } else if (var14.isRemoteZombie()) {
               var25 = Colors.OrangeRed;
            } else {
               var25 = Colors.Chartreuse;
            }

            LineDrawer.DrawIsoCircle(this.x, this.y, this.z, 0.45F, 4, var25.r, var25.g, var25.b, 0.5F);
            TextManager.instance.DrawStringCentre(UIFont.DebugConsole, (double)IsoUtils.XToScreenExact(this.x + 0.4F, this.y + 0.4F, this.z, 0), (double)IsoUtils.YToScreenExact(this.x + 0.4F, this.y - 1.4F, this.z, 0), String.valueOf(var14.OnlineID), (double)var25.r, (double)var25.g, (double)var25.b, (double)var25.a);
         }

         if (DebugOptions.instance.MultiplayerShowZombiePrediction.getValue() && this instanceof IsoZombie) {
            var14 = (IsoZombie)this;
            LineDrawer.DrawIsoTransform(this.realx, this.realy, this.z, this.realdir.ToVector().x, this.realdir.ToVector().y, 0.35F, 16, Colors.Blue.r, Colors.Blue.g, Colors.Blue.b, 0.35F, 1);
            if (var14.networkAI.DebugInterfaceActive) {
               LineDrawer.DrawIsoCircle(this.x, this.y, this.z, 0.4F, 4, 1.0F, 0.1F, 0.1F, 0.35F);
            } else if (!var14.isRemoteZombie()) {
               LineDrawer.DrawIsoCircle(this.x, this.y, this.z, 0.3F, 3, Colors.Magenta.r, Colors.Magenta.g, Colors.Magenta.b, 0.35F);
            } else {
               LineDrawer.DrawIsoCircle(this.x, this.y, this.z, 0.3F, 5, Colors.Magenta.r, Colors.Magenta.g, Colors.Magenta.b, 0.35F);
            }

            LineDrawer.DrawIsoTransform(var14.networkAI.targetX, var14.networkAI.targetY, this.z, 1.0F, 0.0F, 0.4F, 16, Colors.LimeGreen.r, Colors.LimeGreen.g, Colors.LimeGreen.b, 0.35F, 1);
            LineDrawer.DrawIsoLine(this.x, this.y, this.z, var14.networkAI.targetX, var14.networkAI.targetY, this.z, Colors.LimeGreen.r, Colors.LimeGreen.g, Colors.LimeGreen.b, 0.35F, 1);
            if (IsoUtils.DistanceToSquared(this.x, this.y, this.realx, this.realy) > 4.5F) {
               LineDrawer.DrawIsoLine(this.realx, this.realy, this.z, this.x, this.y, this.z, Colors.Magenta.r, Colors.Magenta.g, Colors.Magenta.b, 0.35F, 1);
            } else {
               LineDrawer.DrawIsoLine(this.realx, this.realy, this.z, this.x, this.y, this.z, Colors.Blue.r, Colors.Blue.g, Colors.Blue.b, 0.35F, 1);
            }
         }

         if (DebugOptions.instance.MultiplayerShowZombieDesync.getValue() && this instanceof IsoZombie) {
            var14 = (IsoZombie)this;
            var5 = IsoUtils.DistanceTo(this.getX(), this.getY(), this.realx, this.realy);
            if (var14.isRemoteZombie() && var5 > 1.0F) {
               LineDrawer.DrawIsoLine(this.realx, this.realy, this.z, this.x, this.y, this.z, Colors.Blue.r, Colors.Blue.g, Colors.Blue.b, 0.9F, 1);
               LineDrawer.DrawIsoTransform(this.realx, this.realy, this.z, this.realdir.ToVector().x, this.realdir.ToVector().y, 0.35F, 16, Colors.Blue.r, Colors.Blue.g, Colors.Blue.b, 0.9F, 1);
               LineDrawer.DrawIsoCircle(this.x, this.y, this.z, 0.4F, 4, 1.0F, 1.0F, 1.0F, 0.9F);
               var6 = IsoUtils.DistanceTo(this.realx, this.realy, var14.networkAI.targetX, var14.networkAI.targetY);
               var22 = IsoUtils.DistanceTo(this.x, this.y, var14.networkAI.targetX, var14.networkAI.targetY) / var6;
               float var26 = IsoUtils.XToScreenExact(this.x, this.y, this.z, 0);
               float var29 = IsoUtils.YToScreenExact(this.x, this.y, this.z, 0);
               TextManager.instance.DrawStringCentre(UIFont.DebugConsole, (double)var26, (double)var29, String.format("dist:%f scale1:%f", var5, var22), (double)Colors.NavajoWhite.r, (double)Colors.NavajoWhite.g, (double)Colors.NavajoWhite.b, 0.8999999761581421D);
            }
         }

         if (DebugOptions.instance.MultiplayerShowHit.getValue() && this.getHitReactionNetworkAI() != null && this.getHitReactionNetworkAI().isSetup()) {
            LineDrawer.DrawIsoLine(this.x, this.y, this.z, this.x + this.getHitDir().getX(), this.y + this.getHitDir().getY(), this.z, Colors.BlueViolet.r, Colors.BlueViolet.g, Colors.BlueViolet.b, 0.8F, 1);
            LineDrawer.DrawIsoLine(this.getHitReactionNetworkAI().startPosition.x, this.getHitReactionNetworkAI().startPosition.y, this.z, this.getHitReactionNetworkAI().finalPosition.x, this.getHitReactionNetworkAI().finalPosition.y, this.z, Colors.Salmon.r, Colors.Salmon.g, Colors.Salmon.b, 0.8F, 1);
            float var10007 = Colors.Salmon.r - 0.2F;
            float var10008 = Colors.Salmon.g + 0.2F;
            LineDrawer.DrawIsoTransform(this.getHitReactionNetworkAI().startPosition.x, this.getHitReactionNetworkAI().startPosition.y, this.z, this.getHitReactionNetworkAI().startDirection.x, this.getHitReactionNetworkAI().startDirection.y, 0.4F, 16, var10007, var10008, Colors.Salmon.b, 0.8F, 1);
            var10008 = Colors.Salmon.g - 0.2F;
            LineDrawer.DrawIsoTransform(this.getHitReactionNetworkAI().finalPosition.x, this.getHitReactionNetworkAI().finalPosition.y, this.z, this.getHitReactionNetworkAI().finalDirection.x, this.getHitReactionNetworkAI().finalDirection.y, 0.4F, 16, Colors.Salmon.r, var10008, Colors.Salmon.b, 0.8F, 1);
         }

         if (DebugOptions.instance.MultiplayerShowPlayerPrediction.getValue() && this instanceof IsoPlayer) {
            if (this.isoPlayer != null && this.isoPlayer.networkAI != null && this.isoPlayer.networkAI.footstepSoundRadius != 0) {
               LineDrawer.DrawIsoCircle(this.x, this.y, this.z, (float)this.isoPlayer.networkAI.footstepSoundRadius, 32, Colors.Violet.r, Colors.Violet.g, Colors.Violet.b, 0.5F);
            }

            if (this.isoPlayer != null && this.isoPlayer.bRemote) {
               LineDrawer.DrawIsoCircle(this.x, this.y, this.z, 0.3F, 16, Colors.OrangeRed.r, Colors.OrangeRed.g, Colors.OrangeRed.b, 0.5F);
               tempo.set(this.realdir.ToVector());
               LineDrawer.DrawIsoTransform(this.realx, this.realy, this.z, tempo.x, tempo.y, 0.35F, 16, Colors.Blue.r, Colors.Blue.g, Colors.Blue.b, 0.5F, 1);
               LineDrawer.DrawIsoLine(this.realx, this.realy, this.z, this.x, this.y, this.z, Colors.Blue.r, Colors.Blue.g, Colors.Blue.b, 0.5F, 1);
               tempo.set(((IsoPlayer)this).networkAI.targetX, ((IsoPlayer)this).networkAI.targetY);
               LineDrawer.DrawIsoTransform(tempo.x, tempo.y, this.z, 1.0F, 0.0F, 0.4F, 16, Colors.LimeGreen.r, Colors.LimeGreen.g, Colors.LimeGreen.b, 0.5F, 1);
               LineDrawer.DrawIsoLine(this.x, this.y, this.z, tempo.x, tempo.y, this.z, Colors.LimeGreen.r, Colors.LimeGreen.g, Colors.LimeGreen.b, 0.5F, 1);
            }
         }

         if (DebugOptions.instance.MultiplayerShowTeleport.getValue() && this.getNetworkCharacterAI() != null) {
            NetworkTeleport.NetworkTeleportDebug var17 = this.getNetworkCharacterAI().getTeleportDebug();
            if (var17 != null) {
               LineDrawer.DrawIsoLine(var17.lx, var17.ly, var17.lz, var17.nx, var17.ny, var17.nz, Colors.NavajoWhite.r, Colors.NavajoWhite.g, Colors.NavajoWhite.b, 0.7F, 3);
               LineDrawer.DrawIsoCircle(var17.nx, var17.ny, var17.nz, 0.2F, 16, Colors.NavajoWhite.r, Colors.NavajoWhite.g, Colors.NavajoWhite.b, 0.7F);
               var5 = IsoUtils.XToScreenExact(var17.lx, var17.ly, var17.lz, 0);
               var6 = IsoUtils.YToScreenExact(var17.lx, var17.ly, var17.lz, 0);
               TextManager.instance.DrawStringCentre(UIFont.DebugConsole, (double)var5, (double)var6, String.format("%s id=%d", this instanceof IsoPlayer ? ((IsoPlayer)this).getUsername() : this.getClass().getSimpleName(), var17.id), (double)Colors.NavajoWhite.r, (double)Colors.NavajoWhite.g, (double)Colors.NavajoWhite.b, 0.699999988079071D);
               TextManager.instance.DrawStringCentre(UIFont.DebugConsole, (double)var5, (double)(var6 + 10.0F), var17.type.name(), (double)Colors.NavajoWhite.r, (double)Colors.NavajoWhite.g, (double)Colors.NavajoWhite.b, 0.699999988079071D);
            }
         } else if (this.getNetworkCharacterAI() != null) {
            this.getNetworkCharacterAI().clearTeleportDebug();
         }

         if (DebugOptions.instance.MultiplayerShowZombieStatus.getValue() && this instanceof IsoZombie || DebugOptions.instance.MultiplayerShowPlayerStatus.getValue() && this instanceof IsoPlayer && !this.isGodMod()) {
            var4 = IsoUtils.XToScreenExact(this.x, this.y, this.z, 0);
            var5 = IsoUtils.YToScreenExact(this.x, this.y, this.z, 0);
            var6 = 10.0F;
            Color var33 = Colors.GreenYellow;
            TextManager.instance.DrawStringCentre(UIFont.DebugConsole, (double)var4, (double)(var5 + (var6 += 11.0F)), String.format("%d: %.03f / %.03f", this.getOnlineID(), this.getHealth(), this instanceof IsoZombie ? 0.0F : this.getBodyDamage().getOverallBodyHealth()), (double)var33.r, (double)var33.g, (double)var33.b, (double)var33.a);
            TextManager.instance.DrawStringCentre(UIFont.DebugConsole, (double)var4, (double)(var5 + (var6 += 11.0F)), "x=" + this.x + "  y=" + this.y + "  z=" + this.z, (double)var33.r, (double)var33.g, (double)var33.b, (double)var33.a);
            if (this instanceof IsoPlayer) {
               IsoPlayer var34 = (IsoPlayer)this;
               Color var28 = Colors.NavajoWhite;
               TextManager.instance.DrawStringCentre(UIFont.DebugConsole, (double)var4, (double)(var5 + (var6 += 18.0F)), String.format("IdleSpeed: %s , targetDist: %s ", var34.getVariableString("IdleSpeed"), var34.getVariableString("targetDist")), (double)var28.r, (double)var28.g, (double)var28.b, 1.0D);
               TextManager.instance.DrawStringCentre(UIFont.DebugConsole, (double)var4, (double)(var5 + (var6 += 11.0F)), String.format("WalkInjury: %s , WalkSpeed: %s", var34.getVariableString("WalkInjury"), var34.getVariableString("WalkSpeed")), (double)var28.r, (double)var28.g, (double)var28.b, 1.0D);
               TextManager.instance.DrawStringCentre(UIFont.DebugConsole, (double)var4, (double)(var5 + (var6 += 11.0F)), String.format("DeltaX: %s , DeltaY: %s", var34.getVariableString("DeltaX"), var34.getVariableString("DeltaY")), (double)var28.r, (double)var28.g, (double)var28.b, 1.0D);
               TextManager.instance.DrawStringCentre(UIFont.DebugConsole, (double)var4, (double)(var5 + (var6 += 11.0F)), String.format("AttackVariationX: %s , AttackVariationY: %s", var34.getVariableString("AttackVariationX"), var34.getVariableString("AttackVariationY")), (double)var28.r, (double)var28.g, (double)var28.b, 1.0D);
               TextManager.instance.DrawStringCentre(UIFont.DebugConsole, (double)var4, (double)(var5 + (var6 += 11.0F)), String.format("autoShootVarX: %s , autoShootVarY: %s", var34.getVariableString("autoShootVarX"), var34.getVariableString("autoShootVarY")), (double)var28.r, (double)var28.g, (double)var28.b, 1.0D);
               TextManager.instance.DrawStringCentre(UIFont.DebugConsole, (double)var4, (double)(var5 + (var6 += 11.0F)), String.format("recoilVarX: %s , recoilVarY: %s", var34.getVariableString("recoilVarX"), var34.getVariableString("recoilVarY")), (double)var28.r, (double)var28.g, (double)var28.b, 1.0D);
               TextManager.instance.DrawStringCentre(UIFont.DebugConsole, (double)var4, (double)(var5 + (var6 += 11.0F)), String.format("ShoveAimX: %s , ShoveAimY: %s", var34.getVariableString("ShoveAimX"), var34.getVariableString("ShoveAimY")), (double)var28.r, (double)var28.g, (double)var28.b, 1.0D);
            }

            var33 = Colors.Yellow;
            TextManager.instance.DrawStringCentre(UIFont.DebugConsole, (double)var4, (double)(var5 + (var6 += 18.0F)), String.format("isHitFromBehind=%b/%b", this.isHitFromBehind(), this.getVariableBoolean("frombehind")), (double)var33.r, (double)var33.g, (double)var33.b, 1.0D);
            TextManager.instance.DrawStringCentre(UIFont.DebugConsole, (double)var4, (double)(var5 + (var6 += 11.0F)), String.format("bKnockedDown=%b/%b", this.isKnockedDown(), this.getVariableBoolean("bknockeddown")), (double)var33.r, (double)var33.g, (double)var33.b, 1.0D);
            TextManager.instance.DrawStringCentre(UIFont.DebugConsole, (double)var4, (double)(var5 + (var6 += 11.0F)), String.format("isFallOnFront=%b/%b", this.isFallOnFront(), this.getVariableBoolean("fallonfront")), (double)var33.r, (double)var33.g, (double)var33.b, 1.0D);
            TextManager.instance.DrawStringCentre(UIFont.DebugConsole, (double)var4, (double)(var5 + (var6 += 11.0F)), String.format("isOnFloor=%b/%b", this.isOnFloor(), this.getVariableBoolean("bonfloor")), (double)var33.r, (double)var33.g, (double)var33.b, 1.0D);
            TextManager.instance.DrawStringCentre(UIFont.DebugConsole, (double)var4, (double)(var5 + (var6 += 11.0F)), String.format("isDead=%b/%b", this.isDead(), this.getVariableBoolean("bdead")), (double)var33.r, (double)var33.g, (double)var33.b, 1.0D);
            if (this.advancedAnimator.getRootLayer() != null) {
               var33 = Colors.Pink;
               TextManager.instance.DrawStringCentre(UIFont.DebugConsole, (double)var4, (double)(var5 + (var6 += 18.0F)), "Animation set: " + this.advancedAnimator.animSet.m_Name, (double)var33.r, (double)var33.g, (double)var33.b, 1.0D);
               TextManager.instance.DrawStringCentre(UIFont.DebugConsole, (double)var4, (double)(var5 + (var6 += 11.0F)), "Animation state: " + this.advancedAnimator.getCurrentStateName(), (double)var33.r, (double)var33.g, (double)var33.b, 1.0D);
               TextManager.instance.DrawStringCentre(UIFont.DebugConsole, (double)var4, (double)(var5 + (var6 += 11.0F)), "Animation node: " + this.advancedAnimator.getRootLayer().getDebugNodeName(), (double)var33.r, (double)var33.g, (double)var33.b, 1.0D);
            }

            var33 = Colors.LightBlue;
            TextManager.instance.DrawStringCentre(UIFont.DebugConsole, (double)var4, (double)(var5 + (var6 += 11.0F)), String.format("Previous state: %s ( %s )", this.getPreviousStateName(), this.getPreviousActionContextStateName()), (double)var33.r, (double)var33.g, (double)var33.b, 1.0D);
            TextManager.instance.DrawStringCentre(UIFont.DebugConsole, (double)var4, (double)(var5 + (var6 += 11.0F)), String.format("Current state: %s ( %s )", this.getCurrentStateName(), this.getCurrentActionContextStateName()), (double)var33.r, (double)var33.g, (double)var33.b, 1.0D);
            TextManager.instance.DrawStringCentre(UIFont.DebugConsole, (double)var4, (double)(var5 + (var6 += 11.0F)), String.format("Child state: %s", this.getActionContext() != null && this.getActionContext().getChildStates() != null && this.getActionContext().getChildStates().size() > 0 && this.getActionContext().getChildStateAt(0) != null ? this.getActionContext().getChildStateAt(0).getName() : "\"\""), (double)var33.r, (double)var33.g, (double)var33.b, 1.0D);
            if (this.CharacterActions != null) {
               TextManager.instance.DrawStringCentre(UIFont.DebugConsole, (double)var4, (double)(var5 + (var6 += 11.0F)), String.format("Character actions: %d", this.CharacterActions.size()), (double)var33.r, (double)var33.g, (double)var33.b, 1.0D);
               Iterator var30 = this.CharacterActions.iterator();

               while(var30.hasNext()) {
                  BaseAction var32 = (BaseAction)var30.next();
                  if (var32 instanceof LuaTimedActionNew) {
                     TextManager.instance.DrawStringCentre(UIFont.DebugConsole, (double)var4, (double)(var5 + (var6 += 11.0F)), String.format("Action: %s", ((LuaTimedActionNew)var32).getMetaType()), (double)var33.r, (double)var33.g, (double)var33.b, 1.0D);
                  }
               }
            }

            if (this instanceof IsoZombie) {
               var33 = Colors.GreenYellow;
               IsoZombie var31 = (IsoZombie)this;
               TextManager.instance.DrawStringCentre(UIFont.DebugConsole, (double)var4, (double)(var5 + (var6 += 18.0F)), "Prediction: " + this.getNetworkCharacterAI().predictionType, (double)var33.r, (double)var33.g, (double)var33.b, 1.0D);
               TextManager.instance.DrawStringCentre(UIFont.DebugConsole, (double)var4, (double)(var5 + (var6 += 11.0F)), String.format("Real state: %s", var31.realState), (double)var33.r, (double)var33.g, (double)var33.b, 1.0D);
               if (var31.target instanceof IsoPlayer) {
                  TextManager.instance.DrawStringCentre(UIFont.DebugConsole, (double)var4, (double)(var5 + (var6 += 11.0F)), "Target: " + ((IsoPlayer)var31.target).username + "  =" + var31.vectorToTarget.getLength(), (double)var33.r, (double)var33.g, (double)var33.b, 1.0D);
               } else {
                  TextManager.instance.DrawStringCentre(UIFont.DebugConsole, (double)var4, (double)(var5 + (var6 += 11.0F)), "Target: " + var31.target + "  =" + var31.vectorToTarget.getLength(), (double)var33.r, (double)var33.g, (double)var33.b, 1.0D);
               }
            }
         }
      }

      if (this.inventory != null) {
         int var18;
         for(var18 = 0; var18 < this.inventory.Items.size(); ++var18) {
            InventoryItem var35 = (InventoryItem)this.inventory.Items.get(var18);
            if (var35 instanceof IUpdater) {
               ((IUpdater)var35).renderlast();
            }
         }

         if (Core.bDebug && DebugOptions.instance.PathfindRenderPath.getValue() && this.pfb2 != null) {
            this.pfb2.render();
         }

         if (Core.bDebug && DebugOptions.instance.CollideWithObstaclesRenderRadius.getValue()) {
            var4 = 0.3F;
            var5 = 1.0F;
            var6 = 1.0F;
            var22 = 1.0F;
            if (!this.isCollidable()) {
               var22 = 0.0F;
            }

            if ((int)this.z != (int)IsoCamera.frameState.CamCharacterZ) {
               var22 = 0.5F;
               var6 = 0.5F;
               var5 = 0.5F;
            }

            LineDrawer.DrawIsoCircle(this.x, this.y, this.z, var4, 16, var5, var6, var22, 1.0F);
         }

         if (DebugOptions.instance.Animation.Debug.getValue() && this.hasActiveModel()) {
            var18 = (int)IsoUtils.XToScreenExact(this.x, this.y, this.z, 0);
            int var36 = (int)IsoUtils.YToScreenExact(this.x, this.y, this.z, 0);
            TextManager.instance.DrawString((double)var18, (double)var36, this.getAnimationDebug());
         }

         if (this.getIsNPC() && this.GameCharacterAIBrain != null) {
            this.GameCharacterAIBrain.renderlast();
         }

      }
   }

   protected boolean renderTextureInsteadOfModel(float var1, float var2) {
      return false;
   }

   public void drawDirectionLine(Vector2 var1, float var2, float var3, float var4, float var5) {
      float var6 = this.x + var1.x * var2;
      float var7 = this.y + var1.y * var2;
      float var8 = IsoUtils.XToScreenExact(this.x, this.y, this.z, 0);
      float var9 = IsoUtils.YToScreenExact(this.x, this.y, this.z, 0);
      float var10 = IsoUtils.XToScreenExact(var6, var7, this.z, 0);
      float var11 = IsoUtils.YToScreenExact(var6, var7, this.z, 0);
      LineDrawer.drawLine(var8, var9, var10, var11, var3, var4, var5, 0.5F, 1);
   }

   public Radio getEquipedRadio() {
      return this.equipedRadio;
   }

   private void radioEquipedCheck() {
      if (this.leftHandItem != this.leftHandCache) {
         this.leftHandCache = this.leftHandItem;
         if (this.leftHandItem != null && (this.equipedRadio == null || this.equipedRadio != this.rightHandItem) && this.leftHandItem instanceof Radio) {
            this.equipedRadio = (Radio)this.leftHandItem;
         } else if (this.equipedRadio != null && this.equipedRadio != this.rightHandItem) {
            if (this.equipedRadio.getDeviceData() != null) {
               this.equipedRadio.getDeviceData().cleanSoundsAndEmitter();
            }

            this.equipedRadio = null;
         }
      }

      if (this.rightHandItem != this.rightHandCache) {
         this.rightHandCache = this.rightHandItem;
         if (this.rightHandItem != null && this.rightHandItem instanceof Radio) {
            this.equipedRadio = (Radio)this.rightHandItem;
         } else if (this.equipedRadio != null && this.equipedRadio != this.leftHandItem) {
            if (this.equipedRadio.getDeviceData() != null) {
               this.equipedRadio.getDeviceData().cleanSoundsAndEmitter();
            }

            this.equipedRadio = null;
         }
      }

   }

   private void debugAim() {
      if (this == IsoPlayer.getInstance()) {
         IsoPlayer var1 = (IsoPlayer)this;
         if (var1.IsAiming()) {
            HandWeapon var2 = (HandWeapon)Type.tryCastTo(this.getPrimaryHandItem(), HandWeapon.class);
            if (var2 == null) {
               var2 = var1.bareHands;
            }

            float var3 = var2.getMaxRange(var1) * var2.getRangeMod(var1);
            float var4 = this.getLookAngleRadians();
            LineDrawer.drawDirectionLine(this.x, this.y, this.z, var3, var4, 1.0F, 1.0F, 1.0F, 0.5F, 1);
            float var5 = var2.getMinAngle();
            var5 -= var2.getAimingPerkMinAngleModifier() * ((float)this.getPerkLevel(PerkFactory.Perks.Aiming) / 2.0F);
            LineDrawer.drawDotLines(this.x, this.y, this.z, var3, var4, var5, 1.0F, 1.0F, 1.0F, 0.5F, 1);
            float var6 = var2.getMinRange();
            LineDrawer.drawArc(this.x, this.y, this.z, var6, var4, var5, 6, 1.0F, 1.0F, 1.0F, 0.5F);
            if (var6 != var3) {
               LineDrawer.drawArc(this.x, this.y, this.z, var3, var4, var5, 6, 1.0F, 1.0F, 1.0F, 0.5F);
            }

            float var7 = PZMath.min(var3 + 1.0F, 2.0F);
            LineDrawer.drawArc(this.x, this.y, this.z, var7, var4, var5, 6, 0.75F, 0.75F, 0.75F, 0.5F);
            float var8 = Core.getInstance().getIgnoreProneZombieRange();
            if (var8 > 0.0F) {
               LineDrawer.drawArc(this.x, this.y, this.z, var8, var4, 0.0F, 12, 0.0F, 0.0F, 1.0F, 0.25F);
               LineDrawer.drawDotLines(this.x, this.y, this.z, var8, var4, 0.0F, 0.0F, 0.0F, 1.0F, 0.25F, 1);
            }

            AttackVars var9 = new AttackVars();
            ArrayList var10 = new ArrayList();
            SwipeStatePlayer.instance().CalcAttackVars((IsoLivingCharacter)this, var9);
            SwipeStatePlayer.instance().CalcHitList(this, false, var9, var10);
            HitInfo var11;
            if (var9.targetOnGround.getMovingObject() != null) {
               var11 = (HitInfo)var9.targetsProne.get(0);
               LineDrawer.DrawIsoCircle(var11.x, var11.y, var11.z, 0.1F, 8, 1.0F, 1.0F, 0.0F, 1.0F);
            } else if (var9.targetsStanding.size() > 0) {
               var11 = (HitInfo)var9.targetsStanding.get(0);
               LineDrawer.DrawIsoCircle(var11.x, var11.y, var11.z, 0.1F, 8, 1.0F, 1.0F, 0.0F, 1.0F);
            }

            for(int var29 = 0; var29 < var10.size(); ++var29) {
               HitInfo var12 = (HitInfo)var10.get(var29);
               IsoMovingObject var13 = var12.getObject();
               if (var13 != null) {
                  int var14 = var12.chance;
                  float var15 = 1.0F - (float)var14 / 100.0F;
                  float var16 = 1.0F - var15;
                  float var17 = Math.max(0.2F, (float)var14 / 100.0F) / 2.0F;
                  float var18 = IsoUtils.XToScreenExact(var13.x - var17, var13.y + var17, var13.z, 0);
                  float var19 = IsoUtils.YToScreenExact(var13.x - var17, var13.y + var17, var13.z, 0);
                  float var20 = IsoUtils.XToScreenExact(var13.x - var17, var13.y - var17, var13.z, 0);
                  float var21 = IsoUtils.YToScreenExact(var13.x - var17, var13.y - var17, var13.z, 0);
                  float var22 = IsoUtils.XToScreenExact(var13.x + var17, var13.y - var17, var13.z, 0);
                  float var23 = IsoUtils.YToScreenExact(var13.x + var17, var13.y - var17, var13.z, 0);
                  float var24 = IsoUtils.XToScreenExact(var13.x + var17, var13.y + var17, var13.z, 0);
                  float var25 = IsoUtils.YToScreenExact(var13.x + var17, var13.y + var17, var13.z, 0);
                  SpriteRenderer.instance.renderPoly(var18, var19, var20, var21, var22, var23, var24, var25, var15, var16, 0.0F, 0.5F);
                  UIFont var26 = UIFont.DebugConsole;
                  TextManager.instance.DrawStringCentre(var26, (double)var24, (double)var25, String.valueOf(var12.dot), 1.0D, 1.0D, 1.0D, 1.0D);
                  TextManager.instance.DrawStringCentre(var26, (double)var24, (double)(var25 + (float)TextManager.instance.getFontHeight(var26)), var12.chance + "%", 1.0D, 1.0D, 1.0D, 1.0D);
                  var15 = 1.0F;
                  var16 = 1.0F;
                  float var27 = 1.0F;
                  float var28 = PZMath.sqrt(var12.distSq);
                  if (var28 < var2.getMinRange()) {
                     var27 = 0.0F;
                     var15 = 0.0F;
                  }

                  TextManager.instance.DrawStringCentre(var26, (double)var24, (double)(var25 + (float)(TextManager.instance.getFontHeight(var26) * 2)), "DIST: " + var28, (double)var15, (double)var16, (double)var27, 1.0D);
               }

               if (var12.window.getObject() != null) {
                  var12.window.getObject().setHighlighted(true);
               }
            }

         }
      }
   }

   private void debugTestDotSide() {
      if (this == IsoPlayer.getInstance()) {
         float var1 = this.getLookAngleRadians();
         float var2 = 2.0F;
         float var3 = 0.7F;
         LineDrawer.drawDotLines(this.x, this.y, this.z, var2, var1, var3, 1.0F, 1.0F, 1.0F, 0.5F, 1);
         var3 = -0.5F;
         LineDrawer.drawDotLines(this.x, this.y, this.z, var2, var1, var3, 1.0F, 1.0F, 1.0F, 0.5F, 1);
         LineDrawer.drawArc(this.x, this.y, this.z, var2, var1, -1.0F, 16, 1.0F, 1.0F, 1.0F, 0.5F);
         ArrayList var4 = this.getCell().getZombieList();

         for(int var5 = 0; var5 < var4.size(); ++var5) {
            IsoMovingObject var6 = (IsoMovingObject)var4.get(var5);
            if (this.DistToSquared(var6) < var2 * var2) {
               LineDrawer.DrawIsoCircle(var6.x, var6.y, var6.z, 0.3F, 1.0F, 1.0F, 1.0F, 1.0F);
               float var7 = 0.2F;
               float var8 = IsoUtils.XToScreenExact(var6.x + var7, var6.y + var7, var6.z, 0);
               float var9 = IsoUtils.YToScreenExact(var6.x + var7, var6.y + var7, var6.z, 0);
               UIFont var10 = UIFont.DebugConsole;
               int var11 = TextManager.instance.getFontHeight(var10);
               TextManager.instance.DrawStringCentre(var10, (double)var8, (double)(var9 + (float)var11), "SIDE: " + this.testDotSide(var6), 1.0D, 1.0D, 1.0D, 1.0D);
               Vector2 var12 = this.getLookVector(tempo2);
               Vector2 var13 = tempo.set(var6.x - this.x, var6.y - this.y);
               var13.normalize();
               float var14 = PZMath.wrap(var13.getDirection() - var12.getDirection(), 0.0F, 6.2831855F);
               TextManager.instance.DrawStringCentre(var10, (double)var8, (double)(var9 + (float)(var11 * 2)), "ANGLE (0-360): " + PZMath.radToDeg(var14), 1.0D, 1.0D, 1.0D, 1.0D);
               var14 = (float)Math.acos((double)this.getDotWithForwardDirection(var6.x, var6.y));
               TextManager.instance.DrawStringCentre(var10, (double)var8, (double)(var9 + (float)(var11 * 3)), "ANGLE (0-180): " + PZMath.radToDeg(var14), 1.0D, 1.0D, 1.0D, 1.0D);
            }
         }

      }
   }

   private void debugVision() {
      if (this == IsoPlayer.getInstance()) {
         float var1 = LightingJNI.calculateVisionCone(this);
         LineDrawer.drawDotLines(this.x, this.y, this.z, GameTime.getInstance().getViewDist(), this.getLookAngleRadians(), -var1, 1.0F, 1.0F, 1.0F, 0.5F, 1);
         LineDrawer.drawArc(this.x, this.y, this.z, GameTime.getInstance().getViewDist(), this.getLookAngleRadians(), -var1, 16, 1.0F, 1.0F, 1.0F, 0.5F);
         float var2 = 3.5F - this.stats.getFatigue();
         LineDrawer.drawArc(this.x, this.y, this.z, var2, this.getLookAngleRadians(), -1.0F, 32, 1.0F, 1.0F, 1.0F, 0.5F);
      }
   }

   public void setDefaultState() {
      this.stateMachine.changeState(this.defaultState, (Iterable)null);
   }

   public void SetOnFire() {
      if (!this.OnFire) {
         this.OnFire = true;
         float var1 = (float)Core.TileScale;
         this.AttachAnim("Fire", "01", 4, IsoFireManager.FireAnimDelay, (int)(-(this.offsetX + 1.0F * var1)) + (8 - Rand.Next(16)), (int)(-(this.offsetY + -89.0F * var1)) + (int)((float)(10 + Rand.Next(20)) * var1), true, 0, false, 0.7F, IsoFireManager.FireTintMod);
         IsoFireManager.AddBurningCharacter(this);
         int var2 = Rand.Next(BodyPartType.ToIndex(BodyPartType.Hand_L), BodyPartType.ToIndex(BodyPartType.MAX));
         if (this instanceof IsoPlayer) {
            ((BodyPart)this.getBodyDamage().getBodyParts().get(var2)).setBurned();
         }

         if (var1 == 2.0F) {
            int var3 = this.AttachedAnimSprite.size() - 1;
            ((IsoSpriteInstance)this.AttachedAnimSprite.get(var3)).setScale(var1, var1);
         }

         if (!this.getEmitter().isPlaying("BurningFlesh")) {
            this.getEmitter().playSound("BurningFlesh");
         }

      }
   }

   public void StopBurning() {
      if (this.OnFire) {
         IsoFireManager.RemoveBurningCharacter(this);
         this.setOnFire(false);
         if (this.AttachedAnimSprite != null) {
            this.AttachedAnimSprite.clear();
         }

         this.getEmitter().stopOrTriggerSoundByName("BurningFlesh");
      }
   }

   public void sendStopBurning() {
      if (GameClient.bClient) {
         if (this instanceof IsoPlayer) {
            IsoPlayer var1 = (IsoPlayer)this;
            if (var1.isLocalPlayer()) {
               this.StopBurning();
            } else {
               GameClient.sendStopFire((IsoGameCharacter)var1);
            }
         }

         if (this.isZombie()) {
            IsoZombie var2 = (IsoZombie)this;
            GameClient.sendStopFire((IsoGameCharacter)var2);
         }
      }

   }

   public void SpreadFire() {
      if (this.OnFire) {
         if (!GameServer.bServer || !(this instanceof IsoPlayer)) {
            if (!GameClient.bClient || !this.isZombie() || !(this instanceof IsoZombie) || !((IsoZombie)this).isRemoteZombie()) {
               if (!GameClient.bClient || !(this instanceof IsoPlayer) || !((IsoPlayer)this).bRemote) {
                  if (SandboxOptions.instance.FireSpread.getValue()) {
                     if (this.square != null && !this.square.getProperties().Is(IsoFlagType.burning) && Rand.Next(Rand.AdjustForFramerate(3000)) < this.FireSpreadProbability) {
                        IsoFireManager.StartFire(this.getCell(), this.square, false, 80);
                     }

                  }
               }
            }
         }
      }
   }

   public void Throw(HandWeapon var1) {
      if (this instanceof IsoPlayer && ((IsoPlayer)this).getJoypadBind() != -1) {
         Vector2 var2 = tempo.set(this.m_forwardDirection);
         var2.setLength(var1.getMaxRange());
         this.attackTargetSquare = this.getCell().getGridSquare((double)(this.getX() + var2.getX()), (double)(this.getY() + var2.getY()), (double)this.getZ());
      }

      float var5 = (float)this.attackTargetSquare.getX() - this.getX();
      if (var5 > 0.0F) {
         if ((float)this.attackTargetSquare.getX() - this.getX() > var1.getMaxRange()) {
            var5 = var1.getMaxRange();
         }
      } else if ((float)this.attackTargetSquare.getX() - this.getX() < -var1.getMaxRange()) {
         var5 = -var1.getMaxRange();
      }

      float var3 = (float)this.attackTargetSquare.getY() - this.getY();
      if (var3 > 0.0F) {
         if ((float)this.attackTargetSquare.getY() - this.getY() > var1.getMaxRange()) {
            var3 = var1.getMaxRange();
         }
      } else if ((float)this.attackTargetSquare.getY() - this.getY() < -var1.getMaxRange()) {
         var3 = -var1.getMaxRange();
      }

      if (var1.getPhysicsObject().equals("Ball")) {
         new IsoBall(this.getCell(), this.getX(), this.getY(), this.getZ() + 0.6F, var5 * 0.4F, var3 * 0.4F, var1, this);
      } else {
         new IsoMolotovCocktail(this.getCell(), this.getX(), this.getY(), this.getZ() + 0.6F, var5 * 0.4F, var3 * 0.4F, var1, this);
      }

      if (this instanceof IsoPlayer) {
         ((IsoPlayer)this).setAttackAnimThrowTimer(0L);
      }

   }

   public void serverRemoveItemFromZombie(String var1) {
      if (GameServer.bServer) {
         IsoZombie var2 = (IsoZombie)Type.tryCastTo(this, IsoZombie.class);
         this.getItemVisuals(tempItemVisuals);

         for(int var3 = 0; var3 < tempItemVisuals.size(); ++var3) {
            ItemVisual var4 = (ItemVisual)tempItemVisuals.get(var3);
            Item var5 = var4.getScriptItem();
            if (var5 != null && var5.name.equals(var1)) {
               tempItemVisuals.remove(var3--);
               var2.itemVisuals.clear();
               var2.itemVisuals.addAll(tempItemVisuals);
            }
         }

      }
   }

   public boolean helmetFall(boolean var1) {
      return this.helmetFall(var1, (String)null);
   }

   public boolean helmetFall(boolean var1, String var2) {
      IsoPlayer var3 = (IsoPlayer)Type.tryCastTo(this, IsoPlayer.class);
      boolean var4 = false;
      InventoryItem var5 = null;
      IsoZombie var6 = (IsoZombie)Type.tryCastTo(this, IsoZombie.class);
      int var7;
      IsoFallingClothing var12;
      if (var6 != null && !var6.isUsingWornItems()) {
         this.getItemVisuals(tempItemVisuals);

         for(var7 = 0; var7 < tempItemVisuals.size(); ++var7) {
            ItemVisual var13 = (ItemVisual)tempItemVisuals.get(var7);
            Item var14 = var13.getScriptItem();
            if (var14 != null && var14.getType() == Item.Type.Clothing && var14.getChanceToFall() > 0) {
               int var15 = var14.getChanceToFall();
               if (var1) {
                  var15 += 40;
               }

               if (var14.name.equals(var2)) {
                  var15 = 100;
               }

               if (Rand.Next(100) > var15) {
                  InventoryItem var16 = InventoryItemFactory.CreateItem(var14.getFullName());
                  if (var16 != null) {
                     if (var16.getVisual() != null) {
                        var16.getVisual().copyFrom(var13);
                        var16.synchWithVisual();
                     }

                     var12 = new IsoFallingClothing(this.getCell(), this.getX(), this.getY(), PZMath.min(this.getZ() + 0.4F, (float)((int)this.getZ()) + 0.95F), 0.2F, 0.2F, var16);
                     if (!StringUtils.isNullOrEmpty(var2)) {
                        var12.addWorldItem = false;
                     }

                     tempItemVisuals.remove(var7--);
                     var6.itemVisuals.clear();
                     var6.itemVisuals.addAll(tempItemVisuals);
                     this.resetModelNextFrame();
                     this.onWornItemsChanged();
                     var4 = true;
                     var5 = var16;
                  }
               }
            }
         }
      } else if (this.getWornItems() != null && !this.getWornItems().isEmpty()) {
         for(var7 = 0; var7 < this.getWornItems().size(); ++var7) {
            WornItem var8 = this.getWornItems().get(var7);
            InventoryItem var9 = var8.getItem();
            String var10 = var8.getLocation();
            if (var9 instanceof Clothing) {
               int var11 = ((Clothing)var9).getChanceToFall();
               if (var1) {
                  var11 += 40;
               }

               if (var9.getType().equals(var2)) {
                  var11 = 100;
               }

               if (((Clothing)var9).getChanceToFall() > 0 && Rand.Next(100) <= var11) {
                  var12 = new IsoFallingClothing(this.getCell(), this.getX(), this.getY(), PZMath.min(this.getZ() + 0.4F, (float)((int)this.getZ()) + 0.95F), Rand.Next(-0.2F, 0.2F), Rand.Next(-0.2F, 0.2F), var9);
                  if (!StringUtils.isNullOrEmpty(var2)) {
                     var12.addWorldItem = false;
                  }

                  this.getInventory().Remove(var9);
                  this.getWornItems().remove(var9);
                  var5 = var9;
                  this.resetModelNextFrame();
                  this.onWornItemsChanged();
                  var4 = true;
                  if (GameClient.bClient && var3 != null && var3.isLocalPlayer() && StringUtils.isNullOrEmpty(var2)) {
                     GameClient.instance.sendClothing(var3, var10, (InventoryItem)null);
                  }
               }
            }
         }
      }

      if (var4 && GameClient.bClient && StringUtils.isNullOrEmpty(var2) && IsoPlayer.getInstance().isLocalPlayer()) {
         GameClient.sendZombieHelmetFall(IsoPlayer.getInstance(), this, var5);
      }

      if (var4 && var3 != null && var3.isLocalPlayer()) {
         LuaEventManager.triggerEvent("OnClothingUpdated", this);
      }

      if (var4 && this.isZombie()) {
         PersistentOutfits.instance.setFallenHat(this, true);
      }

      return var4;
   }

   public void smashCarWindow(VehiclePart var1) {
      HashMap var2 = this.getStateMachineParams(SmashWindowState.instance());
      var2.clear();
      var2.put(0, var1.getWindow());
      var2.put(1, var1.getVehicle());
      var2.put(2, var1);
      this.actionContext.reportEvent("EventSmashWindow");
   }

   public void smashWindow(IsoWindow var1) {
      if (!var1.isInvincible()) {
         HashMap var2 = this.getStateMachineParams(SmashWindowState.instance());
         var2.clear();
         var2.put(0, var1);
         this.actionContext.reportEvent("EventSmashWindow");
      }

   }

   public void openWindow(IsoWindow var1) {
      if (!var1.isInvincible()) {
         OpenWindowState.instance().setParams(this, var1);
         this.actionContext.reportEvent("EventOpenWindow");
      }

   }

   public void closeWindow(IsoWindow var1) {
      if (!var1.isInvincible()) {
         HashMap var2 = this.getStateMachineParams(CloseWindowState.instance());
         var2.clear();
         var2.put(0, var1);
         this.actionContext.reportEvent("EventCloseWindow");
      }

   }

   public void climbThroughWindow(IsoWindow var1) {
      if (var1.canClimbThrough(this)) {
         float var2 = this.x - (float)((int)this.x);
         float var3 = this.y - (float)((int)this.y);
         byte var4 = 0;
         byte var5 = 0;
         if (var1.getX() > this.x && !var1.north) {
            var4 = -1;
         }

         if (var1.getY() > this.y && var1.north) {
            var5 = -1;
         }

         this.x = var1.getX() + var2 + (float)var4;
         this.y = var1.getY() + var3 + (float)var5;
         ClimbThroughWindowState.instance().setParams(this, var1);
         this.actionContext.reportEvent("EventClimbWindow");
      }

   }

   public void climbThroughWindow(IsoWindow var1, Integer var2) {
      if (var1.canClimbThrough(this)) {
         ClimbThroughWindowState.instance().setParams(this, var1);
         this.actionContext.reportEvent("EventClimbWindow");
      }

   }

   public boolean isClosingWindow(IsoWindow var1) {
      if (var1 == null) {
         return false;
      } else if (!this.isCurrentState(CloseWindowState.instance())) {
         return false;
      } else {
         return CloseWindowState.instance().getWindow(this) == var1;
      }
   }

   public boolean isClimbingThroughWindow(IsoWindow var1) {
      if (var1 == null) {
         return false;
      } else if (!this.isCurrentState(ClimbThroughWindowState.instance())) {
         return false;
      } else if (!this.getVariableBoolean("BlockWindow")) {
         return false;
      } else {
         return ClimbThroughWindowState.instance().getWindow(this) == var1;
      }
   }

   public void climbThroughWindowFrame(IsoObject var1) {
      if (IsoWindowFrame.canClimbThrough(var1, this)) {
         ClimbThroughWindowState.instance().setParams(this, var1);
         this.actionContext.reportEvent("EventClimbWindow");
      }

   }

   public void climbSheetRope() {
      if (this.canClimbSheetRope(this.current)) {
         HashMap var1 = this.getStateMachineParams(ClimbSheetRopeState.instance());
         var1.clear();
         this.actionContext.reportEvent("EventClimbRope");
      }
   }

   public void climbDownSheetRope() {
      if (this.canClimbDownSheetRope(this.current)) {
         this.dropHeavyItems();
         HashMap var1 = this.getStateMachineParams(ClimbDownSheetRopeState.instance());
         var1.clear();
         this.actionContext.reportEvent("EventClimbDownRope");
      }
   }

   public boolean canClimbSheetRope(IsoGridSquare var1) {
      if (var1 == null) {
         return false;
      } else {
         for(int var2 = var1.getZ(); var1 != null; var1 = this.getCell().getGridSquare(var1.getX(), var1.getY(), var1.getZ() + 1)) {
            if (!IsoWindow.isSheetRopeHere(var1)) {
               return false;
            }

            if (!IsoWindow.canClimbHere(var1)) {
               return false;
            }

            if (var1.TreatAsSolidFloor() && var1.getZ() > var2) {
               return false;
            }

            if (IsoWindow.isTopOfSheetRopeHere(var1)) {
               return true;
            }
         }

         return false;
      }
   }

   public boolean canClimbDownSheetRopeInCurrentSquare() {
      return this.canClimbDownSheetRope(this.current);
   }

   public boolean canClimbDownSheetRope(IsoGridSquare var1) {
      if (var1 == null) {
         return false;
      } else {
         for(int var2 = var1.getZ(); var1 != null; var1 = this.getCell().getGridSquare(var1.getX(), var1.getY(), var1.getZ() - 1)) {
            if (!IsoWindow.isSheetRopeHere(var1)) {
               return false;
            }

            if (!IsoWindow.canClimbHere(var1)) {
               return false;
            }

            if (var1.TreatAsSolidFloor()) {
               return var1.getZ() < var2;
            }
         }

         return false;
      }
   }

   public void climbThroughWindow(IsoThumpable var1) {
      if (var1.canClimbThrough(this)) {
         float var2 = this.x - (float)((int)this.x);
         float var3 = this.y - (float)((int)this.y);
         byte var4 = 0;
         byte var5 = 0;
         if (var1.getX() > this.x && !var1.north) {
            var4 = -1;
         }

         if (var1.getY() > this.y && var1.north) {
            var5 = -1;
         }

         this.x = var1.getX() + var2 + (float)var4;
         this.y = var1.getY() + var3 + (float)var5;
         ClimbThroughWindowState.instance().setParams(this, var1);
         this.actionContext.reportEvent("EventClimbWindow");
      }

   }

   public void climbThroughWindow(IsoThumpable var1, Integer var2) {
      if (var1.canClimbThrough(this)) {
         ClimbThroughWindowState.instance().setParams(this, var1);
         this.actionContext.reportEvent("EventClimbWindow");
      }

   }

   public void climbOverFence(IsoDirections var1) {
      if (this.current != null) {
         IsoGridSquare var2 = this.current.nav[var1.index()];
         if (IsoWindow.canClimbThroughHelper(this, this.current, var2, var1 == IsoDirections.N || var1 == IsoDirections.S)) {
            ClimbOverFenceState.instance().setParams(this, var1);
            this.actionContext.reportEvent("EventClimbFence");
         }
      }
   }

   public boolean isAboveTopOfStairs() {
      if (this.z != 0.0F && !((double)(this.z - (float)((int)this.z)) > 0.01D) && (this.current == null || !this.current.TreatAsSolidFloor())) {
         IsoGridSquare var1 = this.getCell().getGridSquare((double)this.x, (double)this.y, (double)(this.z - 1.0F));
         return var1 != null && (var1.Has(IsoObjectType.stairsTN) || var1.Has(IsoObjectType.stairsTW));
      } else {
         return false;
      }
   }

   public void preupdate() {
      super.preupdate();
      if (!this.m_bDebugVariablesRegistered && DebugOptions.instance.Character.Debug.RegisterDebugVariables.getValue()) {
         this.registerDebugGameVariables();
      }

      this.updateAnimationRecorderState();
      if (this.isAnimationRecorderActive()) {
         int var1 = IsoWorld.instance.getFrameNo();
         this.m_animationRecorder.beginLine(var1);
      }

   }

   public void setTeleport(NetworkTeleport var1) {
      this.teleport = var1;
   }

   public NetworkTeleport getTeleport() {
      return this.teleport;
   }

   public boolean isTeleporting() {
      return this.teleport != null;
   }

   public void update() {
      IsoGameCharacter.s_performance.update.invokeAndMeasure(this, IsoGameCharacter::updateInternal);
   }

   private void updateInternal() {
      if (this.current != null) {
         if (this.teleport != null) {
            this.teleport.process(IsoPlayer.getPlayerIndex());
         }

         this.updateAlpha();
         if (this.isNPC) {
            if (this.GameCharacterAIBrain == null) {
               this.GameCharacterAIBrain = new GameCharacterAIBrain(this);
            }

            this.GameCharacterAIBrain.update();
         }

         if (this.sprite != null) {
            this.legsSprite = this.sprite;
         }

         if (!this.isDead() || this.current != null && this.current.getMovingObjects().contains(this)) {
            if (!GameClient.bClient && !this.m_invisible && this.getCurrentSquare().getTrapPositionX() > -1 && this.getCurrentSquare().getTrapPositionY() > -1 && this.getCurrentSquare().getTrapPositionZ() > -1) {
               this.getCurrentSquare().explodeTrap();
            }

            float var1;
            if (this.getBodyDamage() != null && this.getCurrentBuilding() != null && this.getCurrentBuilding().isToxic()) {
               var1 = GameTime.getInstance().getMultiplier() / 1.6F;
               if (this.getStats().getFatigue() < 1.0F) {
                  this.getStats().setFatigue(this.getStats().getFatigue() + 1.0E-4F * var1);
               }

               if ((double)this.getStats().getFatigue() > 0.8D) {
                  this.getBodyDamage().getBodyPart(BodyPartType.Head).ReduceHealth(0.1F * var1);
               }

               this.getBodyDamage().getBodyPart(BodyPartType.Torso_Upper).ReduceHealth(0.1F * var1);
            }

            if (this.lungeFallTimer > 0.0F) {
               this.lungeFallTimer -= GameTime.getInstance().getMultiplier() / 1.6F;
            }

            if (this.getMeleeDelay() > 0.0F) {
               this.setMeleeDelay(this.getMeleeDelay() - 0.625F * GameTime.getInstance().getMultiplier());
            }

            if (this.getRecoilDelay() > 0.0F) {
               this.setRecoilDelay(this.getRecoilDelay() - 0.625F * GameTime.getInstance().getMultiplier());
            }

            this.sx = 0.0F;
            this.sy = 0.0F;
            if (this.current.getRoom() != null && this.current.getRoom().building.def.bAlarmed && (!this.isZombie() || Core.bTutorial) && !GameClient.bClient) {
               boolean var5 = false;
               if (this instanceof IsoPlayer && (((IsoPlayer)this).isInvisible() || ((IsoPlayer)this).isGhostMode())) {
                  var5 = true;
               }

               if (!var5) {
                  AmbientStreamManager.instance.doAlarm(this.current.getRoom().def);
               }
            }

            this.updateSeenVisibility();
            this.llx = this.getLx();
            this.lly = this.getLy();
            this.setLx(this.getX());
            this.setLy(this.getY());
            this.setLz(this.getZ());
            this.updateBeardAndHair();
            this.updateFalling();
            if (this.descriptor != null) {
               this.descriptor.Instance = this;
            }

            int var2;
            int var7;
            if (!this.isZombie()) {
               Stats var10000;
               if (this.Traits.Agoraphobic.isSet() && this.getCurrentSquare().getRoom() == null) {
                  var10000 = this.stats;
                  var10000.Panic += 0.5F * (GameTime.getInstance().getMultiplier() / 1.6F);
               }

               if (this.Traits.Claustophobic.isSet() && this.getCurrentSquare().getRoom() != null) {
                  var1 = 1.0F;
                  var2 = this.getCurrentSquare().getRoom().def.getH() * this.getCurrentSquare().getRoom().def.getW();
                  var1 = 1.0F - (float)var2 / 70.0F;
                  if (var1 < 0.0F) {
                     var1 = 0.0F;
                  }

                  float var3 = 0.6F * var1 * (GameTime.getInstance().getMultiplier() / 1.6F);
                  if (var3 > 0.6F) {
                     var3 = 0.6F;
                  }

                  var10000 = this.stats;
                  var10000.Panic += var3;
               }

               if (this.Moodles != null) {
                  this.Moodles.Update();
               }

               if (this.Asleep) {
                  this.BetaEffect = 0.0F;
                  this.SleepingTabletEffect = 0.0F;
                  this.StopAllActionQueue();
               }

               if (this.BetaEffect > 0.0F) {
                  this.BetaEffect -= GameTime.getInstance().getMultiplier() / 1.6F;
                  var10000 = this.stats;
                  var10000.Panic -= 0.6F * (GameTime.getInstance().getMultiplier() / 1.6F);
                  if (this.stats.Panic < 0.0F) {
                     this.stats.Panic = 0.0F;
                  }
               } else {
                  this.BetaDelta = 0.0F;
               }

               if (this.DepressFirstTakeTime > 0.0F || this.DepressEffect > 0.0F) {
                  this.DepressFirstTakeTime -= GameTime.getInstance().getMultiplier() / 1.6F;
                  if (this.DepressFirstTakeTime < 0.0F) {
                     this.DepressFirstTakeTime = -1.0F;
                     this.DepressEffect -= GameTime.getInstance().getMultiplier() / 1.6F;
                     this.getBodyDamage().setUnhappynessLevel(this.getBodyDamage().getUnhappynessLevel() - 0.03F * (GameTime.getInstance().getMultiplier() / 1.6F));
                     if (this.getBodyDamage().getUnhappynessLevel() < 0.0F) {
                        this.getBodyDamage().setUnhappynessLevel(0.0F);
                     }
                  }
               }

               if (this.DepressEffect < 0.0F) {
                  this.DepressEffect = 0.0F;
               }

               if (this.SleepingTabletEffect > 0.0F) {
                  this.SleepingTabletEffect -= GameTime.getInstance().getMultiplier() / 1.6F;
                  var10000 = this.stats;
                  var10000.fatigue += 0.0016666667F * this.SleepingTabletDelta * (GameTime.getInstance().getMultiplier() / 1.6F);
               } else {
                  this.SleepingTabletDelta = 0.0F;
               }

               var7 = this.Moodles.getMoodleLevel(MoodleType.Panic);
               if (var7 == 2) {
                  var10000 = this.stats;
                  var10000.Sanity -= 3.2E-7F;
               } else if (var7 == 3) {
                  var10000 = this.stats;
                  var10000.Sanity -= 4.8000004E-7F;
               } else if (var7 == 4) {
                  var10000 = this.stats;
                  var10000.Sanity -= 8.0E-7F;
               } else if (var7 == 0) {
                  var10000 = this.stats;
                  var10000.Sanity += 1.0E-7F;
               }

               var2 = this.Moodles.getMoodleLevel(MoodleType.Tired);
               if (var2 == 4) {
                  var10000 = this.stats;
                  var10000.Sanity -= 2.0E-6F;
               }

               if (this.stats.Sanity < 0.0F) {
                  this.stats.Sanity = 0.0F;
               }

               if (this.stats.Sanity > 1.0F) {
                  this.stats.Sanity = 1.0F;
               }
            }

            if (!this.CharacterActions.isEmpty()) {
               BaseAction var8 = (BaseAction)this.CharacterActions.get(0);
               boolean var6 = var8.valid();
               if (var6 && !var8.bStarted) {
                  var8.waitToStart();
               } else if (var6 && !var8.finished() && !var8.forceComplete && !var8.forceStop) {
                  var8.update();
               }

               if (!var6 || var8.finished() || var8.forceComplete || var8.forceStop) {
                  if (var8.finished() || var8.forceComplete) {
                     var8.perform();
                     var6 = true;
                  }

                  if (var8.finished() && !var8.loopAction || var8.forceComplete || var8.forceStop || !var6) {
                     if (var8.bStarted && (var8.forceStop || !var6)) {
                        var8.stop();
                     }

                     this.CharacterActions.removeElement(var8);
                     if (this == IsoPlayer.players[0] || this == IsoPlayer.players[1] || this == IsoPlayer.players[2] || this == IsoPlayer.players[3]) {
                        UIManager.getProgressBar((double)((IsoPlayer)this).getPlayerNum()).setValue(0.0F);
                     }
                  }
               }

               for(int var9 = 0; var9 < this.EnemyList.size(); ++var9) {
                  IsoGameCharacter var4 = (IsoGameCharacter)this.EnemyList.get(var9);
                  if (var4.isDead()) {
                     this.EnemyList.remove(var4);
                     --var9;
                  }
               }
            }

            if (SystemDisabler.doCharacterStats && this.BodyDamage != null) {
               this.BodyDamage.Update();
               this.updateBandages();
            }

            if (this == IsoPlayer.getInstance()) {
               if (this.leftHandItem != null && this.leftHandItem.getUses() <= 0) {
                  this.leftHandItem = null;
               }

               if (this.rightHandItem != null && this.rightHandItem.getUses() <= 0) {
                  this.rightHandItem = null;
               }
            }

            if (SystemDisabler.doCharacterStats) {
               this.calculateStats();
            }

            this.moveForwardVec.x = 0.0F;
            this.moveForwardVec.y = 0.0F;
            if (!this.Asleep || !(this instanceof IsoPlayer)) {
               this.setLx(this.getX());
               this.setLy(this.getY());
               this.setLz(this.getZ());
               this.square = this.getCurrentSquare();
               if (this.sprite != null) {
                  if (!this.bUseParts) {
                     this.sprite.update(this.def);
                  } else {
                     this.legsSprite.update(this.def);
                  }
               }

               this.setStateEventDelayTimer(this.getStateEventDelayTimer() - GameTime.getInstance().getMultiplier() / 1.6F);
            }

            this.stateMachine.update();
            if (this.isZombie() && VirtualZombieManager.instance.isReused((IsoZombie)this)) {
               DebugLog.log(DebugType.Zombie, "Zombie added to ReusableZombies after stateMachine.update - RETURNING " + this);
            } else {
               if (this instanceof IsoPlayer) {
                  this.ensureOnTile();
               }

               if ((this instanceof IsoPlayer || this instanceof IsoSurvivor) && this.RemoteID == -1 && this instanceof IsoPlayer && ((IsoPlayer)this).isLocalPlayer()) {
                  RainManager.SetPlayerLocation(((IsoPlayer)this).getPlayerNum(), this.getCurrentSquare());
               }

               this.FireCheck();
               this.SpreadFire();
               this.ReduceHealthWhenBurning();
               this.updateTextObjects();
               if (this.stateMachine.getCurrent() == StaggerBackState.instance()) {
                  if (this.getStateEventDelayTimer() > 20.0F) {
                     this.BloodImpactX = this.getX();
                     this.BloodImpactY = this.getY();
                     this.BloodImpactZ = this.getZ();
                  }
               } else {
                  this.BloodImpactX = this.getX();
                  this.BloodImpactY = this.getY();
                  this.BloodImpactZ = this.getZ();
               }

               if (!this.isZombie()) {
                  this.recursiveItemUpdater(this.inventory);
               }

               this.LastZombieKills = this.ZombieKills;
               if (this.AttachedAnimSprite != null) {
                  var7 = this.AttachedAnimSprite.size();

                  for(var2 = 0; var2 < var7; ++var2) {
                     IsoSpriteInstance var11 = (IsoSpriteInstance)this.AttachedAnimSprite.get(var2);
                     IsoSprite var10 = var11.parentSprite;
                     var11.update();
                     var11.Frame += var11.AnimFrameIncrease * GameTime.instance.getMultipliedSecondsSinceLastUpdate() * 60.0F;
                     if ((int)var11.Frame >= var10.CurrentAnim.Frames.size() && var10.Loop && var11.Looped) {
                        var11.Frame = 0.0F;
                     }
                  }
               }

               if (this.isGodMod()) {
                  this.getStats().setFatigue(0.0F);
                  this.getStats().setEndurance(1.0F);
                  this.getBodyDamage().setTemperature(37.0F);
                  this.getStats().setHunger(0.0F);
               }

               this.updateMovementMomentum();
               if (this.effectiveEdibleBuffTimer > 0.0F) {
                  this.effectiveEdibleBuffTimer -= GameTime.getInstance().getMultiplier() * 0.015F;
                  if (this.effectiveEdibleBuffTimer < 0.0F) {
                     this.effectiveEdibleBuffTimer = 0.0F;
                  }
               }

               if (!GameServer.bServer || GameClient.bClient) {
                  this.updateDirt();
               }

            }
         }
      }
   }

   private void updateSeenVisibility() {
      for(int var1 = 0; var1 < IsoPlayer.numPlayers; ++var1) {
         this.updateSeenVisibility(var1);
      }

   }

   private void updateSeenVisibility(int var1) {
      IsoPlayer var2 = IsoPlayer.players[var1];
      if (var2 != null) {
         this.IsVisibleToPlayer[var1] = this.TestIfSeen(var1);
         if (!this.IsVisibleToPlayer[var1]) {
            if (!(this instanceof IsoPlayer)) {
               if (!var2.isSeeEveryone()) {
                  this.setTargetAlpha(var1, 0.0F);
               }
            }
         }
      }
   }

   private void recursiveItemUpdater(ItemContainer var1) {
      for(int var2 = 0; var2 < var1.Items.size(); ++var2) {
         InventoryItem var3 = (InventoryItem)var1.Items.get(var2);
         if (var3 instanceof InventoryContainer) {
            this.recursiveItemUpdater((InventoryContainer)var3);
         }

         if (var3 instanceof IUpdater) {
            var3.update();
         }
      }

   }

   private void recursiveItemUpdater(InventoryContainer var1) {
      for(int var2 = 0; var2 < var1.getInventory().getItems().size(); ++var2) {
         InventoryItem var3 = (InventoryItem)var1.getInventory().getItems().get(var2);
         if (var3 instanceof InventoryContainer) {
            this.recursiveItemUpdater((InventoryContainer)var3);
         }

         if (var3 instanceof IUpdater) {
            var3.update();
         }
      }

   }

   private void updateDirt() {
      if (!this.isZombie() && this.getBodyDamage() != null) {
         int var1 = 0;
         if (this.isRunning() && Rand.NextBool(Rand.AdjustForFramerate(3500))) {
            var1 = 1;
         }

         if (this.isSprinting() && Rand.NextBool(Rand.AdjustForFramerate(2500))) {
            var1 += Rand.Next(1, 3);
         }

         if (this.getBodyDamage().getTemperature() > 37.0F && Rand.NextBool(Rand.AdjustForFramerate(5000))) {
            ++var1;
         }

         if (this.getBodyDamage().getTemperature() > 38.0F && Rand.NextBool(Rand.AdjustForFramerate(3000))) {
            ++var1;
         }

         float var2 = this.square == null ? 0.0F : this.square.getPuddlesInGround();
         if (this.isMoving() && var2 > 0.09F && Rand.NextBool(Rand.AdjustForFramerate(1500))) {
            ++var1;
         }

         if (var1 > 0) {
            this.addDirt((BloodBodyPartType)null, var1, true);
         }

         IsoPlayer var3 = (IsoPlayer)Type.tryCastTo(this, IsoPlayer.class);
         if (var3 != null && var3.isPlayerMoving() || var3 == null && this.isMoving()) {
            var1 = 0;
            if (var2 > 0.09F && Rand.NextBool(Rand.AdjustForFramerate(1500))) {
               ++var1;
            }

            if (this.isInTrees() && Rand.NextBool(Rand.AdjustForFramerate(1500))) {
               ++var1;
            }

            if (var1 > 0) {
               this.addDirt((BloodBodyPartType)null, var1, false);
            }
         }

      }
   }

   protected void updateMovementMomentum() {
      float var1 = GameTime.instance.getTimeDelta();
      float var2;
      float var3;
      float var4;
      if (this.isPlayerMoving() && !this.isAiming()) {
         var2 = this.m_momentumScalar * 0.55F;
         if (var2 >= 0.55F) {
            this.m_momentumScalar = 1.0F;
            return;
         }

         var3 = var2 + var1;
         var4 = var3 / 0.55F;
         this.m_momentumScalar = PZMath.clamp(var4, 0.0F, 1.0F);
      } else {
         var2 = (1.0F - this.m_momentumScalar) * 0.25F;
         if (var2 >= 0.25F) {
            this.m_momentumScalar = 0.0F;
            return;
         }

         var3 = var2 + var1;
         var4 = var3 / 0.25F;
         float var5 = PZMath.clamp(var4, 0.0F, 1.0F);
         this.m_momentumScalar = 1.0F - var5;
      }

   }

   public double getHoursSurvived() {
      return GameTime.instance.getWorldAgeHours();
   }

   private void updateBeardAndHair() {
      if (!this.isZombie()) {
         float var1 = (float)this.getHoursSurvived();
         if (this.beardGrowTiming < 0.0F || this.beardGrowTiming > var1) {
            this.beardGrowTiming = var1;
         }

         if (this.hairGrowTiming < 0.0F || this.hairGrowTiming > var1) {
            this.hairGrowTiming = var1;
         }

         boolean var2 = !GameClient.bClient && !GameServer.bServer || ServerOptions.instance.SleepAllowed.getValue() && ServerOptions.instance.SleepNeeded.getValue();
         boolean var3 = false;
         int var5;
         ArrayList var6;
         int var7;
         if ((this.isAsleep() || !var2) && var1 - this.beardGrowTiming > 120.0F) {
            this.beardGrowTiming = var1;
            BeardStyle var4 = BeardStyles.instance.FindStyle(((HumanVisual)this.getVisual()).getBeardModel());
            var5 = 1;
            if (var4 != null) {
               var5 = var4.level;
            }

            var6 = BeardStyles.instance.getAllStyles();

            for(var7 = 0; var7 < var6.size(); ++var7) {
               if (((BeardStyle)var6.get(var7)).growReference && ((BeardStyle)var6.get(var7)).level == var5 + 1) {
                  ((HumanVisual)this.getVisual()).setBeardModel(((BeardStyle)var6.get(var7)).name);
                  var3 = true;
                  break;
               }
            }
         }

         if ((this.isAsleep() || !var2) && var1 - this.hairGrowTiming > 480.0F) {
            this.hairGrowTiming = var1;
            HairStyle var9 = HairStyles.instance.FindMaleStyle(((HumanVisual)this.getVisual()).getHairModel());
            if (this.isFemale()) {
               var9 = HairStyles.instance.FindFemaleStyle(((HumanVisual)this.getVisual()).getHairModel());
            }

            var5 = 1;
            if (var9 != null) {
               var5 = var9.level;
            }

            var6 = HairStyles.instance.m_MaleStyles;
            if (this.isFemale()) {
               var6 = HairStyles.instance.m_FemaleStyles;
            }

            for(var7 = 0; var7 < var6.size(); ++var7) {
               HairStyle var8 = (HairStyle)var6.get(var7);
               if (var8.growReference && var8.level == var5 + 1) {
                  ((HumanVisual)this.getVisual()).setHairModel(var8.name);
                  ((HumanVisual)this.getVisual()).setNonAttachedHair((String)null);
                  var3 = true;
                  break;
               }
            }
         }

         if (var3) {
            this.resetModelNextFrame();
            LuaEventManager.triggerEvent("OnClothingUpdated", this);
            if (GameClient.bClient) {
               GameClient.instance.sendVisual((IsoPlayer)this);
            }
         }

      }
   }

   private void updateFalling() {
      if (this instanceof IsoPlayer && !this.isClimbing()) {
         IsoRoofFixer.FixRoofsAt(this.current);
      }

      if (this.isSeatedInVehicle()) {
         this.fallTime = 0.0F;
         this.lastFallSpeed = 0.0F;
         this.bFalling = false;
         this.wasOnStairs = false;
      } else {
         if (this.z > 0.0F) {
            IsoDirections var1 = IsoDirections.Max;
            if (!this.isZombie() && this.isClimbing()) {
               if (this.current.Is(IsoFlagType.climbSheetW) || this.current.Is(IsoFlagType.climbSheetTopW)) {
                  var1 = IsoDirections.W;
               }

               if (this.current.Is(IsoFlagType.climbSheetE) || this.current.Is(IsoFlagType.climbSheetTopE)) {
                  var1 = IsoDirections.E;
               }

               if (this.current.Is(IsoFlagType.climbSheetN) || this.current.Is(IsoFlagType.climbSheetTopN)) {
                  var1 = IsoDirections.N;
               }

               if (this.current.Is(IsoFlagType.climbSheetS) || this.current.Is(IsoFlagType.climbSheetTopS)) {
                  var1 = IsoDirections.S;
               }
            }

            float var2 = 0.125F * (GameTime.getInstance().getMultiplier() / 1.6F);
            if (this.bClimbing) {
               var2 = 0.0F;
            }

            if (this.getCurrentState() == ClimbOverFenceState.instance() || this.getCurrentState() == ClimbThroughWindowState.instance()) {
               this.fallTime = 0.0F;
               var2 = 0.0F;
            }

            this.lastFallSpeed = var2;
            if (!this.current.TreatAsSolidFloor()) {
               if (var1 != IsoDirections.Max) {
                  this.dir = var1;
               }

               this.fallTime += 6.0F * (GameTime.getInstance().getMultiplier() / 1.6F);
               if (var1 != IsoDirections.Max) {
                  this.fallTime = 0.0F;
               }

               if (this.fallTime < 20.0F && this.isAboveTopOfStairs()) {
                  this.fallTime = 0.0F;
               }

               this.setZ(this.getZ() - var2);
            } else if (!(this.getZ() > (float)((int)this.getZ())) && !(var2 < 0.0F)) {
               this.DoLand();
               this.fallTime = 0.0F;
               this.bFalling = false;
            } else {
               if (var1 != IsoDirections.Max) {
                  this.dir = var1;
               }

               if (!this.current.HasStairs()) {
                  if (!this.wasOnStairs) {
                     this.fallTime += 6.0F * (GameTime.getInstance().getMultiplier() / 1.6F);
                     if (var1 != IsoDirections.Max) {
                        this.fallTime = 0.0F;
                     }

                     this.setZ(this.getZ() - var2);
                     if (this.z < (float)((int)this.llz)) {
                        this.z = (float)((int)this.llz);
                        this.DoLand();
                        this.fallTime = 0.0F;
                        this.bFalling = false;
                     }
                  } else {
                     this.wasOnStairs = false;
                  }
               } else {
                  this.fallTime = 0.0F;
                  this.bFalling = false;
                  this.wasOnStairs = true;
               }
            }
         } else {
            this.DoLand();
            this.fallTime = 0.0F;
            this.bFalling = false;
         }

         this.llz = this.lz;
      }
   }

   protected void updateMovementRates() {
   }

   protected float calculateIdleSpeed() {
      float var1 = 0.01F;
      var1 = (float)((double)var1 + (double)this.getMoodles().getMoodleLevel(MoodleType.Endurance) * 2.5D / 10.0D);
      var1 *= GameTime.getAnimSpeedFix();
      return var1;
   }

   public float calculateBaseSpeed() {
      float var1 = 0.8F;
      float var2 = 1.0F;
      if (this.getMoodles() != null) {
         var1 -= (float)this.getMoodles().getMoodleLevel(MoodleType.Endurance) * 0.15F;
         var1 -= (float)this.getMoodles().getMoodleLevel(MoodleType.HeavyLoad) * 0.15F;
      }

      int var3;
      if (this.getMoodles().getMoodleLevel(MoodleType.Panic) >= 3 && this.Traits.AdrenalineJunkie.isSet()) {
         var3 = this.getMoodles().getMoodleLevel(MoodleType.Panic) + 1;
         var1 += (float)var3 / 20.0F;
      }

      for(var3 = BodyPartType.ToIndex(BodyPartType.Torso_Upper); var3 < BodyPartType.ToIndex(BodyPartType.Neck) + 1; ++var3) {
         BodyPart var4 = this.getBodyDamage().getBodyPart(BodyPartType.FromIndex(var3));
         if (var4.HasInjury()) {
            var1 -= 0.1F;
         }

         if (var4.bandaged()) {
            var1 += 0.05F;
         }
      }

      BodyPart var6 = this.getBodyDamage().getBodyPart(BodyPartType.UpperLeg_L);
      if (var6.getAdditionalPain(true) > 20.0F) {
         var1 -= (var6.getAdditionalPain(true) - 20.0F) / 100.0F;
      }

      for(int var7 = 0; var7 < this.bagsWorn.size(); ++var7) {
         InventoryContainer var5 = (InventoryContainer)this.bagsWorn.get(var7);
         var2 += this.calcRunSpeedModByBag(var5);
      }

      if (this.getPrimaryHandItem() != null && this.getPrimaryHandItem() instanceof InventoryContainer) {
         var2 += this.calcRunSpeedModByBag((InventoryContainer)this.getPrimaryHandItem());
      }

      if (this.getSecondaryHandItem() != null && this.getSecondaryHandItem() instanceof InventoryContainer) {
         var2 += this.calcRunSpeedModByBag((InventoryContainer)this.getSecondaryHandItem());
      }

      this.fullSpeedMod = this.runSpeedModifier + (var2 - 1.0F);
      return var1 * (1.0F - Math.abs(1.0F - this.fullSpeedMod) / 2.0F);
   }

   private float calcRunSpeedModByBag(InventoryContainer var1) {
      float var2 = var1.getScriptItem().runSpeedModifier - 1.0F;
      float var3 = var1.getContentsWeight() / (float)var1.getEffectiveCapacity(this);
      var2 *= 1.0F + var3 / 2.0F;
      return var2;
   }

   protected float calculateCombatSpeed() {
      float var1 = 1.0F;
      HandWeapon var2 = null;
      if (this.getPrimaryHandItem() != null && this.getPrimaryHandItem() instanceof HandWeapon) {
         var2 = (HandWeapon)this.getPrimaryHandItem();
         var1 *= ((HandWeapon)this.getPrimaryHandItem()).getBaseSpeed();
      }

      WeaponType var3 = WeaponType.getWeaponType(this);
      if (var2 != null && var2.isTwoHandWeapon() && this.getSecondaryHandItem() != var2) {
         var1 *= 0.77F;
      }

      if (var2 != null && this.Traits.Axeman.isSet() && var2.getCategories().contains("Axe")) {
         var1 *= this.getChopTreeSpeed();
      }

      var1 -= (float)this.getMoodles().getMoodleLevel(MoodleType.Endurance) * 0.07F;
      var1 -= (float)this.getMoodles().getMoodleLevel(MoodleType.HeavyLoad) * 0.07F;
      var1 += (float)this.getWeaponLevel() * 0.03F;
      var1 += (float)this.getPerkLevel(PerkFactory.Perks.Fitness) * 0.02F;
      if (this.getSecondaryHandItem() != null && this.getSecondaryHandItem() instanceof InventoryContainer) {
         var1 *= 0.95F;
      }

      var1 *= Rand.Next(1.1F, 1.2F);
      var1 *= this.combatSpeedModifier;
      var1 *= this.getArmsInjurySpeedModifier();
      if (this.getBodyDamage() != null && this.getBodyDamage().getThermoregulator() != null) {
         var1 *= this.getBodyDamage().getThermoregulator().getCombatModifier();
      }

      var1 = Math.min(1.6F, var1);
      var1 = Math.max(0.8F, var1);
      if (var2 != null && var2.isTwoHandWeapon() && var3.type.equalsIgnoreCase("heavy")) {
         var1 *= 1.2F;
      }

      return var1 * GameTime.getAnimSpeedFix();
   }

   private float getArmsInjurySpeedModifier() {
      float var1 = 1.0F;
      float var2 = 0.0F;
      BodyPart var3 = this.getBodyDamage().getBodyPart(BodyPartType.Hand_R);
      var2 = this.calculateInjurySpeed(var3, true);
      if (var2 > 0.0F) {
         var1 -= var2;
      }

      var3 = this.getBodyDamage().getBodyPart(BodyPartType.ForeArm_R);
      var2 = this.calculateInjurySpeed(var3, true);
      if (var2 > 0.0F) {
         var1 -= var2;
      }

      var3 = this.getBodyDamage().getBodyPart(BodyPartType.UpperArm_R);
      var2 = this.calculateInjurySpeed(var3, true);
      if (var2 > 0.0F) {
         var1 -= var2;
      }

      return var1;
   }

   private float getFootInjurySpeedModifier() {
      float var1 = 0.0F;
      boolean var2 = true;
      float var3 = 0.0F;
      float var4 = 0.0F;

      for(int var5 = BodyPartType.ToIndex(BodyPartType.UpperLeg_L); var5 < BodyPartType.ToIndex(BodyPartType.MAX); ++var5) {
         var1 = this.calculateInjurySpeed(this.getBodyDamage().getBodyPart(BodyPartType.FromIndex(var5)), false);
         if (var2) {
            var3 += var1;
         } else {
            var4 += var1;
         }

         var2 = !var2;
      }

      if (var3 > var4) {
         return -(var3 + var4);
      } else {
         return var3 + var4;
      }
   }

   private float calculateInjurySpeed(BodyPart var1, boolean var2) {
      float var3 = var1.getScratchSpeedModifier();
      float var4 = var1.getCutSpeedModifier();
      float var5 = var1.getBurnSpeedModifier();
      float var6 = var1.getDeepWoundSpeedModifier();
      float var7 = 0.0F;
      if ((var1.getType() == BodyPartType.Foot_L || var1.getType() == BodyPartType.Foot_R) && (var1.getBurnTime() > 5.0F || var1.getBiteTime() > 0.0F || var1.deepWounded() || var1.isSplint() || var1.getFractureTime() > 0.0F || var1.haveGlass())) {
         var7 = 1.0F;
         if (var1.bandaged()) {
            var7 = 0.7F;
         }

         if (var1.getFractureTime() > 0.0F) {
            var7 = this.calcFractureInjurySpeed(var1);
         }
      }

      if (var1.haveBullet()) {
         return 1.0F;
      } else {
         if (var1.getScratchTime() > 2.0F || var1.getCutTime() > 5.0F || var1.getBurnTime() > 0.0F || var1.getDeepWoundTime() > 0.0F || var1.isSplint() || var1.getFractureTime() > 0.0F || var1.getBiteTime() > 0.0F) {
            var7 += var1.getScratchTime() / var3 + var1.getCutTime() / var4 + var1.getBurnTime() / var5 + var1.getDeepWoundTime() / var6;
            var7 += var1.getBiteTime() / 20.0F;
            if (var1.bandaged()) {
               var7 /= 2.0F;
            }

            if (var1.getFractureTime() > 0.0F) {
               var7 = this.calcFractureInjurySpeed(var1);
            }
         }

         if (var2 && var1.getPain() > 20.0F) {
            var7 += var1.getPain() / 10.0F;
         }

         return var7;
      }
   }

   private float calcFractureInjurySpeed(BodyPart var1) {
      float var2 = 0.4F;
      if (var1.getFractureTime() > 10.0F) {
         var2 = 0.7F;
      }

      if (var1.getFractureTime() > 20.0F) {
         var2 = 1.0F;
      }

      if (var1.getSplintFactor() > 0.0F) {
         var2 -= 0.2F;
         var2 -= Math.min(var1.getSplintFactor() / 10.0F, 0.8F);
      }

      return Math.max(0.0F, var2);
   }

   protected void calculateWalkSpeed() {
      if (!(this instanceof IsoPlayer) || ((IsoPlayer)this).isLocalPlayer()) {
         float var1 = 0.0F;
         float var2 = this.getFootInjurySpeedModifier();
         this.setVariable("WalkInjury", var2);
         var1 = this.calculateBaseSpeed();
         if (!this.bRunning && !this.bSprinting) {
            var1 *= this.walkSpeedModifier;
         } else {
            var1 -= 0.15F;
            var1 *= this.fullSpeedMod;
            var1 += (float)this.getPerkLevel(PerkFactory.Perks.Sprinting) / 20.0F;
            var1 = (float)((double)var1 - Math.abs((double)var2 / 1.5D));
            if ("Tutorial".equals(Core.GameMode)) {
               var1 = Math.max(1.0F, var1);
            }
         }

         if (this.getSlowFactor() > 0.0F) {
            var1 *= 0.05F;
         }

         var1 = Math.min(1.0F, var1);
         if (this.getBodyDamage() != null && this.getBodyDamage().getThermoregulator() != null) {
            var1 *= this.getBodyDamage().getThermoregulator().getMovementModifier();
         }

         if (this.isAiming()) {
            float var3 = Math.min(0.9F + (float)this.getPerkLevel(PerkFactory.Perks.Nimble) / 10.0F, 1.5F);
            float var4 = Math.min(var1 * 2.5F, 1.0F);
            var3 *= var4;
            var3 = Math.max(var3, 0.6F);
            this.setVariable("StrafeSpeed", var3 * GameTime.getAnimSpeedFix());
         }

         if (this.isInTreesNoBush()) {
            IsoGridSquare var5 = this.getCurrentSquare();
            if (var5 != null && var5.Has(IsoObjectType.tree)) {
               IsoTree var6 = var5.getTree();
               if (var6 != null) {
                  var1 *= var6.getSlowFactor(this);
               }
            }
         }

         this.setVariable("WalkSpeed", var1 * GameTime.getAnimSpeedFix());
      }
   }

   public void updateSpeedModifiers() {
      this.runSpeedModifier = 1.0F;
      this.walkSpeedModifier = 1.0F;
      this.combatSpeedModifier = 1.0F;
      this.bagsWorn = new ArrayList();

      for(int var1 = 0; var1 < this.getWornItems().size(); ++var1) {
         InventoryItem var2 = this.getWornItems().getItemByIndex(var1);
         if (var2 instanceof Clothing) {
            Clothing var3 = (Clothing)var2;
            this.combatSpeedModifier += var3.getCombatSpeedModifier() - 1.0F;
         }

         if (var2 instanceof InventoryContainer) {
            InventoryContainer var5 = (InventoryContainer)var2;
            this.combatSpeedModifier += var5.getScriptItem().combatSpeedModifier - 1.0F;
            this.bagsWorn.add(var5);
         }
      }

      InventoryItem var4 = this.getWornItems().getItem("Shoes");
      if (var4 == null || var4.getCondition() == 0) {
         this.runSpeedModifier *= 0.85F;
         this.walkSpeedModifier *= 0.85F;
      }

   }

   public void DoFloorSplat(IsoGridSquare var1, String var2, boolean var3, float var4, float var5) {
      if (var1 != null) {
         var1.DirtySlice();
         IsoObject var6 = null;

         for(int var7 = 0; var7 < var1.getObjects().size(); ++var7) {
            IsoObject var8 = (IsoObject)var1.getObjects().get(var7);
            if (var8.sprite != null && var8.sprite.getProperties().Is(IsoFlagType.solidfloor) && var6 == null) {
               var6 = var8;
            }
         }

         if (var6 != null && var6.sprite != null && (var6.sprite.getProperties().Is(IsoFlagType.vegitation) || var6.sprite.getProperties().Is(IsoFlagType.solidfloor))) {
            IsoSprite var9 = IsoSprite.getSprite(IsoSpriteManager.instance, (String)var2, 0);
            if (var9 == null) {
               return;
            }

            if (var6.AttachedAnimSprite.size() > 7) {
               return;
            }

            IsoSpriteInstance var10 = IsoSpriteInstance.get(var9);
            var6.AttachedAnimSprite.add(var10);
            ((IsoSpriteInstance)var6.AttachedAnimSprite.get(var6.AttachedAnimSprite.size() - 1)).Flip = var3;
            ((IsoSpriteInstance)var6.AttachedAnimSprite.get(var6.AttachedAnimSprite.size() - 1)).tintr = 0.5F + (float)Rand.Next(100) / 2000.0F;
            ((IsoSpriteInstance)var6.AttachedAnimSprite.get(var6.AttachedAnimSprite.size() - 1)).tintg = 0.7F + (float)Rand.Next(300) / 1000.0F;
            ((IsoSpriteInstance)var6.AttachedAnimSprite.get(var6.AttachedAnimSprite.size() - 1)).tintb = 0.7F + (float)Rand.Next(300) / 1000.0F;
            ((IsoSpriteInstance)var6.AttachedAnimSprite.get(var6.AttachedAnimSprite.size() - 1)).SetAlpha(0.4F * var5 * 0.6F);
            ((IsoSpriteInstance)var6.AttachedAnimSprite.get(var6.AttachedAnimSprite.size() - 1)).SetTargetAlpha(0.4F * var5 * 0.6F);
            ((IsoSpriteInstance)var6.AttachedAnimSprite.get(var6.AttachedAnimSprite.size() - 1)).offZ = -var4;
            ((IsoSpriteInstance)var6.AttachedAnimSprite.get(var6.AttachedAnimSprite.size() - 1)).offX = 0.0F;
         }

      }
   }

   void DoSplat(IsoGridSquare var1, String var2, boolean var3, IsoFlagType var4, float var5, float var6, float var7) {
      if (var1 != null) {
         var1.DoSplat(var2, var3, var4, var5, var6, var7);
      }
   }

   public boolean onMouseLeftClick(int var1, int var2) {
      if (IsoCamera.CamCharacter != IsoPlayer.getInstance() && Core.bDebug) {
         IsoCamera.CamCharacter = this;
      }

      return super.onMouseLeftClick(var1, var2);
   }

   protected void calculateStats() {
      if (GameServer.bServer) {
         this.stats.fatigue = 0.0F;
      } else if (GameClient.bClient && (!ServerOptions.instance.SleepAllowed.getValue() || !ServerOptions.instance.SleepNeeded.getValue())) {
         this.stats.fatigue = 0.0F;
      }

      if (!LuaHookManager.TriggerHook("CalculateStats", this)) {
         this.updateEndurance();
         this.updateTripping();
         this.updateThirst();
         this.updateStress();
         this.updateStats_WakeState();
         this.stats.endurance = PZMath.clamp(this.stats.endurance, 0.0F, 1.0F);
         this.stats.hunger = PZMath.clamp(this.stats.hunger, 0.0F, 1.0F);
         this.stats.stress = PZMath.clamp(this.stats.stress, 0.0F, 1.0F);
         this.stats.fatigue = PZMath.clamp(this.stats.fatigue, 0.0F, 1.0F);
         this.updateMorale();
         this.updateFitness();
      }
   }

   protected void updateStats_WakeState() {
      if (IsoPlayer.getInstance() == this && this.Asleep) {
         this.updateStats_Sleeping();
      } else {
         this.updateStats_Awake();
      }

   }

   protected void updateStats_Sleeping() {
   }

   protected void updateStats_Awake() {
      Stats var10000 = this.stats;
      var10000.stress = (float)((double)var10000.stress - ZomboidGlobals.StressReduction * (double)GameTime.instance.getMultiplier() * (double)GameTime.instance.getDeltaMinutesPerDay());
      float var1 = 1.0F - this.stats.endurance;
      if (var1 < 0.3F) {
         var1 = 0.3F;
      }

      float var2 = 1.0F;
      if (this.Traits.NeedsLessSleep.isSet()) {
         var2 = 0.7F;
      }

      if (this.Traits.NeedsMoreSleep.isSet()) {
         var2 = 1.3F;
      }

      double var3 = SandboxOptions.instance.getStatsDecreaseMultiplier();
      if (var3 < 1.0D) {
         var3 = 1.0D;
      }

      var10000 = this.stats;
      var10000.fatigue = (float)((double)var10000.fatigue + ZomboidGlobals.FatigueIncrease * SandboxOptions.instance.getStatsDecreaseMultiplier() * (double)var1 * (double)GameTime.instance.getMultiplier() * (double)GameTime.instance.getDeltaMinutesPerDay() * (double)var2 * this.getFatiqueMultiplier());
      float var5 = this.getAppetiteMultiplier();
      if ((!(this instanceof IsoPlayer) || !((IsoPlayer)this).IsRunning()) && !this.isCurrentState(SwipeStatePlayer.instance())) {
         if (this.Moodles.getMoodleLevel(MoodleType.FoodEaten) == 0) {
            var10000 = this.stats;
            var10000.hunger = (float)((double)var10000.hunger + ZomboidGlobals.HungerIncrease * SandboxOptions.instance.getStatsDecreaseMultiplier() * (double)var5 * (double)GameTime.instance.getMultiplier() * (double)GameTime.instance.getDeltaMinutesPerDay() * this.getHungerMultiplier());
         } else {
            var10000 = this.stats;
            var10000.hunger = (float)((double)var10000.hunger + (double)((float)ZomboidGlobals.HungerIncreaseWhenWellFed) * SandboxOptions.instance.getStatsDecreaseMultiplier() * (double)GameTime.instance.getMultiplier() * (double)GameTime.instance.getDeltaMinutesPerDay() * this.getHungerMultiplier());
         }
      } else if (this.Moodles.getMoodleLevel(MoodleType.FoodEaten) == 0) {
         var10000 = this.stats;
         var10000.hunger = (float)((double)var10000.hunger + ZomboidGlobals.HungerIncreaseWhenExercise / 3.0D * SandboxOptions.instance.getStatsDecreaseMultiplier() * (double)var5 * (double)GameTime.instance.getMultiplier() * (double)GameTime.instance.getDeltaMinutesPerDay() * this.getHungerMultiplier());
      } else {
         var10000 = this.stats;
         var10000.hunger = (float)((double)var10000.hunger + ZomboidGlobals.HungerIncreaseWhenExercise * SandboxOptions.instance.getStatsDecreaseMultiplier() * (double)var5 * (double)GameTime.instance.getMultiplier() * (double)GameTime.instance.getDeltaMinutesPerDay() * this.getHungerMultiplier());
      }

      if (this.getCurrentSquare() == this.getLastSquare() && !this.isReading()) {
         var10000 = this.stats;
         var10000.idleboredom += 5.0E-5F * GameTime.instance.getMultiplier() * GameTime.instance.getDeltaMinutesPerDay();
         var10000 = this.stats;
         var10000.idleboredom += 0.00125F * GameTime.instance.getMultiplier() * GameTime.instance.getDeltaMinutesPerDay();
      }

      if (this.getCurrentSquare() != null && this.getLastSquare() != null && this.getCurrentSquare().getRoom() == this.getLastSquare().getRoom() && this.getCurrentSquare().getRoom() != null && !this.isReading()) {
         var10000 = this.stats;
         var10000.idleboredom += 1.0E-4F * GameTime.instance.getMultiplier() * GameTime.instance.getDeltaMinutesPerDay();
         var10000 = this.stats;
         var10000.idleboredom += 0.00125F * GameTime.instance.getMultiplier() * GameTime.instance.getDeltaMinutesPerDay();
      }

   }

   private void updateMorale() {
      float var1 = 1.0F - this.stats.getStress() - 0.5F;
      var1 *= 1.0E-4F;
      if (var1 > 0.0F) {
         var1 += 0.5F;
      }

      Stats var10000 = this.stats;
      var10000.morale += var1;
      this.stats.morale = PZMath.clamp(this.stats.morale, 0.0F, 1.0F);
   }

   private void updateFitness() {
      this.stats.fitness = (float)this.getPerkLevel(PerkFactory.Perks.Fitness) / 5.0F - 1.0F;
      if (this.stats.fitness > 1.0F) {
         this.stats.fitness = 1.0F;
      }

      if (this.stats.fitness < -1.0F) {
         this.stats.fitness = -1.0F;
      }

   }

   private void updateTripping() {
      Stats var10000;
      if (this.stats.Tripping) {
         var10000 = this.stats;
         var10000.TrippingRotAngle += 0.06F;
      } else {
         var10000 = this.stats;
         var10000.TrippingRotAngle += 0.0F;
      }

   }

   protected float getAppetiteMultiplier() {
      float var1 = 1.0F - this.stats.hunger;
      if (this.Traits.HeartyAppitite.isSet()) {
         var1 *= 1.5F;
      }

      if (this.Traits.LightEater.isSet()) {
         var1 *= 0.75F;
      }

      return var1;
   }

   private void updateStress() {
      float var1 = 1.0F;
      if (this.Traits.Cowardly.isSet()) {
         var1 = 2.0F;
      }

      if (this.Traits.Brave.isSet()) {
         var1 = 0.3F;
      }

      if (this.stats.Panic > 100.0F) {
         this.stats.Panic = 100.0F;
      }

      Stats var10000 = this.stats;
      var10000.stress = (float)((double)var10000.stress + (double)WorldSoundManager.instance.getStressFromSounds((int)this.getX(), (int)this.getY(), (int)this.getZ()) * ZomboidGlobals.StressFromSoundsMultiplier);
      if (this.BodyDamage.getNumPartsBitten() > 0) {
         var10000 = this.stats;
         var10000.stress = (float)((double)var10000.stress + ZomboidGlobals.StressFromBiteOrScratch * (double)GameTime.instance.getMultiplier() * (double)GameTime.instance.getDeltaMinutesPerDay());
      }

      if (this.BodyDamage.getNumPartsScratched() > 0) {
         var10000 = this.stats;
         var10000.stress = (float)((double)var10000.stress + ZomboidGlobals.StressFromBiteOrScratch * (double)GameTime.instance.getMultiplier() * (double)GameTime.instance.getDeltaMinutesPerDay());
      }

      if (this.BodyDamage.IsInfected() || this.BodyDamage.IsFakeInfected()) {
         var10000 = this.stats;
         var10000.stress = (float)((double)var10000.stress + ZomboidGlobals.StressFromBiteOrScratch * (double)GameTime.instance.getMultiplier() * (double)GameTime.instance.getDeltaMinutesPerDay());
      }

      if (this.Traits.Hemophobic.isSet()) {
         var10000 = this.stats;
         var10000.stress = (float)((double)var10000.stress + (double)this.getTotalBlood() * ZomboidGlobals.StressFromHemophobic * (double)(GameTime.instance.getMultiplier() / 0.8F) * (double)GameTime.instance.getDeltaMinutesPerDay());
      }

      if (this.Traits.Brooding.isSet()) {
         var10000 = this.stats;
         var10000.Anger = (float)((double)var10000.Anger - ZomboidGlobals.AngerDecrease * ZomboidGlobals.BroodingAngerDecreaseMultiplier * (double)GameTime.instance.getMultiplier() * (double)GameTime.instance.getDeltaMinutesPerDay());
      } else {
         var10000 = this.stats;
         var10000.Anger = (float)((double)var10000.Anger - ZomboidGlobals.AngerDecrease * (double)GameTime.instance.getMultiplier() * (double)GameTime.instance.getDeltaMinutesPerDay());
      }

      this.stats.Anger = PZMath.clamp(this.stats.Anger, 0.0F, 1.0F);
   }

   private void updateEndurance() {
      this.stats.endurance = PZMath.clamp(this.stats.endurance, 0.0F, 1.0F);
      this.stats.endurancelast = this.stats.endurance;
      if (this.isUnlimitedEndurance()) {
         this.stats.endurance = 1.0F;
      }

   }

   private void updateThirst() {
      float var1 = 1.0F;
      if (this.Traits.HighThirst.isSet()) {
         var1 = (float)((double)var1 * 2.0D);
      }

      if (this.Traits.LowThirst.isSet()) {
         var1 = (float)((double)var1 * 0.5D);
      }

      if (IsoPlayer.getInstance() == this && !IsoPlayer.getInstance().isGhostMode()) {
         Stats var10000;
         if (this.Asleep) {
            var10000 = this.stats;
            var10000.thirst = (float)((double)var10000.thirst + ZomboidGlobals.ThirstSleepingIncrease * SandboxOptions.instance.getStatsDecreaseMultiplier() * (double)GameTime.instance.getMultiplier() * (double)GameTime.instance.getDeltaMinutesPerDay() * (double)var1);
         } else {
            var10000 = this.stats;
            var10000.thirst = (float)((double)var10000.thirst + ZomboidGlobals.ThirstIncrease * SandboxOptions.instance.getStatsDecreaseMultiplier() * (double)GameTime.instance.getMultiplier() * this.getRunningThirstReduction() * (double)GameTime.instance.getDeltaMinutesPerDay() * (double)var1 * this.getThirstMultiplier());
         }

         if (this.stats.thirst > 1.0F) {
            this.stats.thirst = 1.0F;
         }
      }

      this.autoDrink();
   }

   private double getRunningThirstReduction() {
      return this == IsoPlayer.getInstance() && IsoPlayer.getInstance().IsRunning() ? 1.2D : 1.0D;
   }

   public void faceLocation(float var1, float var2) {
      tempo.x = var1 + 0.5F;
      tempo.y = var2 + 0.5F;
      Vector2 var10000 = tempo;
      var10000.x -= this.getX();
      var10000 = tempo;
      var10000.y -= this.getY();
      this.DirectionFromVector(tempo);
      this.getVectorFromDirection(this.m_forwardDirection);
      AnimationPlayer var3 = this.getAnimationPlayer();
      if (var3 != null && var3.isReady()) {
         var3.UpdateDir(this);
      }

   }

   public void faceLocationF(float var1, float var2) {
      tempo.x = var1;
      tempo.y = var2;
      Vector2 var10000 = tempo;
      var10000.x -= this.getX();
      var10000 = tempo;
      var10000.y -= this.getY();
      if (tempo.getLengthSquared() != 0.0F) {
         this.DirectionFromVector(tempo);
         tempo.normalize();
         this.m_forwardDirection.set(tempo.x, tempo.y);
         AnimationPlayer var3 = this.getAnimationPlayer();
         if (var3 != null && var3.isReady()) {
            var3.UpdateDir(this);
         }

      }
   }

   public boolean isFacingLocation(float var1, float var2, float var3) {
      Vector2 var4 = BaseVehicle.allocVector2().set(var1 - this.getX(), var2 - this.getY());
      var4.normalize();
      Vector2 var5 = this.getLookVector(BaseVehicle.allocVector2());
      float var6 = var4.dot(var5);
      BaseVehicle.releaseVector2(var4);
      BaseVehicle.releaseVector2(var5);
      return var6 >= var3;
   }

   public boolean isFacingObject(IsoObject var1, float var2) {
      Vector2 var3 = BaseVehicle.allocVector2();
      var1.getFacingPosition(var3);
      boolean var4 = this.isFacingLocation(var3.x, var3.y, var2);
      BaseVehicle.releaseVector2(var3);
      return var4;
   }

   private void checkDrawWeaponPre(float var1, float var2, float var3, ColorInfo var4) {
      if (this.sprite != null) {
         if (this.sprite.CurrentAnim != null) {
            if (this.sprite.CurrentAnim.name != null) {
               if (this.dir != IsoDirections.S && this.dir != IsoDirections.SE && this.dir != IsoDirections.E && this.dir != IsoDirections.NE && this.dir != IsoDirections.SW) {
                  if (this.sprite.CurrentAnim.name.contains("Attack_")) {
                     ;
                  }
               }
            }
         }
      }
   }

   public void splatBlood(int var1, float var2) {
      if (this.getCurrentSquare() != null) {
         this.getCurrentSquare().splatBlood(var1, var2);
      }
   }

   public boolean isOutside() {
      return this.getCurrentSquare() == null ? false : this.getCurrentSquare().isOutside();
   }

   public boolean isFemale() {
      return this.bFemale;
   }

   public void setFemale(boolean var1) {
      this.bFemale = var1;
   }

   public boolean isZombie() {
      return false;
   }

   public int getLastHitCount() {
      return this.lastHitCount;
   }

   public void setLastHitCount(int var1) {
      this.lastHitCount = var1;
   }

   public int getSurvivorKills() {
      return this.SurvivorKills;
   }

   public void setSurvivorKills(int var1) {
      this.SurvivorKills = var1;
   }

   public int getAge() {
      return this.age;
   }

   public void setAge(int var1) {
      this.age = var1;
   }

   public void exert(float var1) {
      if (this.Traits.PlaysFootball.isSet()) {
         var1 *= 0.9F;
      }

      if (this.Traits.Jogger.isSet()) {
         var1 *= 0.9F;
      }

      Stats var10000 = this.stats;
      var10000.endurance -= var1;
   }

   public IsoGameCharacter.PerkInfo getPerkInfo(PerkFactory.Perk var1) {
      for(int var2 = 0; var2 < this.PerkList.size(); ++var2) {
         IsoGameCharacter.PerkInfo var3 = (IsoGameCharacter.PerkInfo)this.PerkList.get(var2);
         if (var3.perk == var1) {
            return var3;
         }
      }

      return null;
   }

   public void HitSilence(HandWeapon var1, IsoGameCharacter var2, boolean var3, float var4) {
      if (var2 != null && var1 != null) {
         if (this.getStateMachine().getCurrent() != StaggerBackState.instance()) {
            if (this.isOnFloor()) {
               var4 = 2.0F;
               var3 = false;
               this.setReanimateTimer(this.getReanimateTimer() + 38.0F);
            }

            if (var1.getName().contains("Bare Hands") || var2 instanceof IsoPlayer && ((IsoPlayer)var2).bDoShove) {
               var3 = true;
               this.noDamage = true;
            }

            this.staggerTimeMod = var1.getPushBackMod() * var1.getKnockbackMod(var2) * var2.getShovingMod();
            float var5 = 0.0F;
            float var6;
            if (var2 instanceof IsoPlayer && !var1.bIsAimedFirearm) {
               var6 = ((IsoPlayer)var2).useChargeDelta;
               if (var6 > 1.0F) {
                  var6 = 1.0F;
               }

               if (var6 < 0.0F) {
                  var6 = 0.0F;
               }

               var5 = var1.getMinDamage() + (var1.getMaxDamage() - var1.getMinDamage()) * var6;
            } else {
               var5 = (float)Rand.Next((int)((var1.getMaxDamage() - var1.getMinDamage()) * 1000.0F)) / 1000.0F + var1.getMinDamage();
            }

            var5 *= var4;
            var6 = var5 * var1.getKnockbackMod(var2) * var2.getShovingMod();
            if (var6 > 1.0F) {
               var6 = 1.0F;
            }

            this.setHitForce(var6);
            this.setAttackedBy(var2);
            float var7 = IsoUtils.DistanceTo(var2.getX(), var2.getY(), this.getX(), this.getY());
            var7 -= var1.getMinRange();
            var7 /= var1.getMaxRange(var2);
            var7 = 1.0F - var7;
            if (var7 > 1.0F) {
               var7 = 1.0F;
            }

            this.hitDir.x = this.getX();
            this.hitDir.y = this.getY();
            Vector2 var10000 = this.hitDir;
            var10000.x -= var2.getX();
            var10000 = this.hitDir;
            var10000.y -= var2.getY();
            this.getHitDir().normalize();
            var10000 = this.hitDir;
            var10000.x *= var1.getPushBackMod();
            var10000 = this.hitDir;
            var10000.y *= var1.getPushBackMod();
            this.hitDir.rotate(var1.HitAngleMod);
            float var8 = var2.stats.endurance;
            var8 *= var2.knockbackAttackMod;
            if (var8 < 0.5F) {
               var8 *= 1.3F;
               if (var8 < 0.4F) {
                  var8 = 0.4F;
               }

               this.setHitForce(this.getHitForce() * var8);
            }

            if (!var1.isRangeFalloff()) {
               var7 = 1.0F;
            }

            if (var2 instanceof IsoPlayer) {
               this.setHitForce(this.getHitForce() * 2.0F);
            }

            Vector2 var9 = tempVector2_1.set(this.getX(), this.getY());
            Vector2 var10 = tempVector2_2.set(var2.getX(), var2.getY());
            var9.x -= var10.x;
            var9.y -= var10.y;
            Vector2 var11 = this.getVectorFromDirection(tempVector2_2);
            var9.normalize();
            float var12 = var9.dot(var11);
            if (var12 > -0.3F) {
               var5 *= 1.5F;
            }

            if (var2.isCriticalHit()) {
               var5 *= 10.0F;
            }

            if (!this.isOnFloor() && var1.getScriptItem().Categories.contains("Axe")) {
               var5 *= 2.0F;
            }

            if (!var3) {
               if (var1.isAimedFirearm()) {
                  this.Health -= var5 * 0.7F;
               } else {
                  this.Health -= var5 * 0.15F;
               }
            }

            float var13 = 12.0F;
            if (var2 instanceof IsoPlayer) {
               int var14 = ((IsoPlayer)var2).Moodles.getMoodleLevel(MoodleType.Endurance);
               if (var14 == 4) {
                  var13 = 50.0F;
               } else if (var14 == 3) {
                  var13 = 35.0F;
               } else if (var14 == 2) {
                  var13 = 24.0F;
               } else if (var14 == 1) {
                  var13 = 16.0F;
               }
            }

            if (var1.getKnockdownMod() <= 0.0F) {
               var1.setKnockdownMod(1.0F);
            }

            var13 /= var1.getKnockdownMod();
            if (var2 instanceof IsoPlayer && !var1.isAimedHandWeapon()) {
               var13 *= 2.0F - ((IsoPlayer)var2).useChargeDelta;
            }

            if (var13 < 1.0F) {
               var13 = 1.0F;
            }

            boolean var15 = Rand.Next((int)var13) == 0;
            if (this.isDead() || (var1.isAlwaysKnockdown() || var15) && this.isZombie()) {
               this.DoDeathSilence(var1, var2);
            }

         }
      }
   }

   protected void DoDeathSilence(HandWeapon var1, IsoGameCharacter var2) {
      if (this.isDead()) {
         if (var1 != null) {
            int var3 = var1.getSplatNumber();
            if (var3 < 1) {
               var3 = 1;
            }

            if (Core.bLastStand) {
               var3 *= 3;
            }

            for(int var4 = 0; var4 < var3; ++var4) {
               this.splatBlood(3, 0.3F);
            }
         }

         this.splatBloodFloorBig();
         if (var2 != null && var2.xp != null) {
            var2.xp.AddXP(var1, 3);
         }

         tempo.x = this.getHitDir().x;
         tempo.y = this.getHitDir().y;
      }

      if (this.isDead() && this.getCurrentSquare() != null) {
         if (GameServer.bServer && this.isZombie()) {
            GameServer.sendZombieDeath((IsoZombie)this);
         }

         new IsoDeadBody(this);
      }

      this.setStateMachineLocked(true);
   }

   public boolean isEquipped(InventoryItem var1) {
      return this.isEquippedClothing(var1) || this.isHandItem(var1);
   }

   public boolean isEquippedClothing(InventoryItem var1) {
      return this.wornItems.contains(var1);
   }

   public boolean isAttachedItem(InventoryItem var1) {
      return this.getAttachedItems().contains(var1);
   }

   public void faceThisObject(IsoObject var1) {
      if (var1 != null) {
         Vector2 var2 = tempo;
         BaseVehicle var3 = (BaseVehicle)Type.tryCastTo(var1, BaseVehicle.class);
         BarricadeAble var4 = (BarricadeAble)Type.tryCastTo(var1, BarricadeAble.class);
         if (var3 != null) {
            var3.getFacingPosition(this, var2);
            var2.x -= this.getX();
            var2.y -= this.getY();
            this.DirectionFromVector(var2);
            var2.normalize();
            this.m_forwardDirection.set(var2.x, var2.y);
         } else if (var4 != null && this.current == var4.getSquare()) {
            this.dir = var4.getNorth() ? IsoDirections.N : IsoDirections.W;
            this.getVectorFromDirection(this.m_forwardDirection);
         } else if (var4 != null && this.current == var4.getOppositeSquare()) {
            this.dir = var4.getNorth() ? IsoDirections.S : IsoDirections.E;
            this.getVectorFromDirection(this.m_forwardDirection);
         } else {
            var1.getFacingPosition(var2);
            var2.x -= this.getX();
            var2.y -= this.getY();
            this.DirectionFromVector(var2);
            this.getVectorFromDirection(this.m_forwardDirection);
         }

         AnimationPlayer var5 = this.getAnimationPlayer();
         if (var5 != null && var5.isReady()) {
            var5.UpdateDir(this);
         }

      }
   }

   public void facePosition(int var1, int var2) {
      tempo.x = (float)var1;
      tempo.y = (float)var2;
      Vector2 var10000 = tempo;
      var10000.x -= this.getX();
      var10000 = tempo;
      var10000.y -= this.getY();
      this.DirectionFromVector(tempo);
      this.getVectorFromDirection(this.m_forwardDirection);
      AnimationPlayer var3 = this.getAnimationPlayer();
      if (var3 != null && var3.isReady()) {
         var3.UpdateDir(this);
      }

   }

   public void faceThisObjectAlt(IsoObject var1) {
      if (var1 != null) {
         var1.getFacingPositionAlt(tempo);
         Vector2 var10000 = tempo;
         var10000.x -= this.getX();
         var10000 = tempo;
         var10000.y -= this.getY();
         this.DirectionFromVector(tempo);
         this.getVectorFromDirection(this.m_forwardDirection);
         AnimationPlayer var2 = this.getAnimationPlayer();
         if (var2 != null && var2.isReady()) {
            var2.UpdateDir(this);
         }

      }
   }

   public void setAnimated(boolean var1) {
      this.legsSprite.Animate = true;
   }

   public void playHurtSound() {
      this.getEmitter().playVocals(this.getHurtSound());
   }

   public void playDeadSound() {
      if (this.isCloseKilled()) {
         this.getEmitter().playSoundImpl("HeadStab", this);
      } else {
         this.getEmitter().playSoundImpl("HeadSmash", this);
      }

      if (this.isZombie()) {
         ((IsoZombie)this).parameterZombieState.setState(ParameterZombieState.State.Death);
      }

   }

   public void saveChange(String var1, KahluaTable var2, ByteBuffer var3) {
      super.saveChange(var1, var2, var3);
      if ("addItem".equals(var1)) {
         if (var2 != null && var2.rawget("item") instanceof InventoryItem) {
            InventoryItem var4 = (InventoryItem)var2.rawget("item");

            try {
               var4.saveWithSize(var3, false);
            } catch (Exception var6) {
               var6.printStackTrace();
            }
         }
      } else if ("addItemOfType".equals(var1)) {
         if (var2 != null && var2.rawget("type") instanceof String) {
            GameWindow.WriteStringUTF(var3, (String)var2.rawget("type"));
            if (var2.rawget("count") instanceof Double) {
               var3.putShort(((Double)var2.rawget("count")).shortValue());
            } else {
               var3.putShort((short)1);
            }
         }
      } else if ("AddRandomDamageFromZombie".equals(var1)) {
         if (var2 != null && var2.rawget("zombie") instanceof Double) {
            var3.putShort(((Double)var2.rawget("zombie")).shortValue());
         }
      } else if (!"AddZombieKill".equals(var1)) {
         if ("DamageFromWeapon".equals(var1)) {
            if (var2 != null && var2.rawget("weapon") instanceof String) {
               GameWindow.WriteStringUTF(var3, (String)var2.rawget("weapon"));
            }
         } else if ("removeItem".equals(var1)) {
            if (var2 != null && var2.rawget("item") instanceof Double) {
               var3.putInt(((Double)var2.rawget("item")).intValue());
            }
         } else if ("removeItemID".equals(var1)) {
            if (var2 != null && var2.rawget("id") instanceof Double) {
               var3.putInt(((Double)var2.rawget("id")).intValue());
            }

            if (var2 != null && var2.rawget("type") instanceof String) {
               GameWindow.WriteStringUTF(var3, (String)var2.rawget("type"));
            } else {
               GameWindow.WriteStringUTF(var3, (String)null);
            }
         } else if ("removeItemType".equals(var1)) {
            if (var2 != null && var2.rawget("type") instanceof String) {
               GameWindow.WriteStringUTF(var3, (String)var2.rawget("type"));
               if (var2.rawget("count") instanceof Double) {
                  var3.putShort(((Double)var2.rawget("count")).shortValue());
               } else {
                  var3.putShort((short)1);
               }
            }
         } else if ("removeOneOf".equals(var1)) {
            if (var2 != null && var2.rawget("type") instanceof String) {
               GameWindow.WriteStringUTF(var3, (String)var2.rawget("type"));
            }
         } else if ("reanimatedID".equals(var1)) {
            if (var2 != null && var2.rawget("ID") instanceof Double) {
               int var7 = ((Double)var2.rawget("ID")).intValue();
               var3.putInt(var7);
            }
         } else if ("Shove".equals(var1)) {
            if (var2 != null && var2.rawget("hitDirX") instanceof Double && var2.rawget("hitDirY") instanceof Double && var2.rawget("force") instanceof Double) {
               var3.putFloat(((Double)var2.rawget("hitDirX")).floatValue());
               var3.putFloat(((Double)var2.rawget("hitDirY")).floatValue());
               var3.putFloat(((Double)var2.rawget("force")).floatValue());
            }
         } else if ("addXp".equals(var1)) {
            if (var2 != null && var2.rawget("perk") instanceof Double && var2.rawget("xp") instanceof Double) {
               var3.putInt(((Double)var2.rawget("perk")).intValue());
               var3.putInt(((Double)var2.rawget("xp")).intValue());
               Object var8 = var2.rawget("noMultiplier");
               var3.put((byte)(Boolean.TRUE.equals(var8) ? 1 : 0));
            }
         } else if (!"wakeUp".equals(var1) && "mechanicActionDone".equals(var1) && var2 != null) {
            var3.put((byte)((Boolean)var2.rawget("success") ? 1 : 0));
            var3.putInt(((Double)var2.rawget("vehicleId")).intValue());
            GameWindow.WriteString(var3, (String)var2.rawget("partId"));
            var3.put((byte)((Boolean)var2.rawget("installing") ? 1 : 0));
            var3.putLong(((Double)var2.rawget("itemId")).longValue());
         }
      }

   }

   public void loadChange(String var1, ByteBuffer var2) {
      super.loadChange(var1, var2);
      if ("addItem".equals(var1)) {
         try {
            InventoryItem var3 = InventoryItem.loadItem(var2, 186);
            if (var3 != null) {
               this.getInventory().AddItem(var3);
            }
         } catch (Exception var9) {
            var9.printStackTrace();
         }
      } else {
         short var4;
         int var5;
         String var10;
         if ("addItemOfType".equals(var1)) {
            var10 = GameWindow.ReadStringUTF(var2);
            var4 = var2.getShort();

            for(var5 = 0; var5 < var4; ++var5) {
               this.getInventory().AddItem(var10);
            }
         } else if ("AddRandomDamageFromZombie".equals(var1)) {
            short var11 = var2.getShort();
            IsoZombie var12 = GameClient.getZombie(var11);
            if (var12 != null && !this.isDead()) {
               this.getBodyDamage().AddRandomDamageFromZombie(var12, (String)null);
               this.getBodyDamage().Update();
               if (this.isDead()) {
                  if (this.isFemale()) {
                     var12.getEmitter().playSound("FemaleBeingEatenDeath");
                  } else {
                     var12.getEmitter().playSound("MaleBeingEatenDeath");
                  }
               }
            }
         } else if ("AddZombieKill".equals(var1)) {
            this.setZombieKills(this.getZombieKills() + 1);
         } else {
            InventoryItem var14;
            if ("DamageFromWeapon".equals(var1)) {
               var10 = GameWindow.ReadStringUTF(var2);
               var14 = InventoryItemFactory.CreateItem(var10);
               if (var14 instanceof HandWeapon) {
                  this.getBodyDamage().DamageFromWeapon((HandWeapon)var14);
               }
            } else if ("exitVehicle".equals(var1)) {
               BaseVehicle var13 = this.getVehicle();
               if (var13 != null) {
                  var13.exit(this);
                  this.setVehicle((BaseVehicle)null);
               }
            } else {
               int var15;
               if ("removeItem".equals(var1)) {
                  var15 = var2.getInt();
                  if (var15 >= 0 && var15 < this.getInventory().getItems().size()) {
                     var14 = (InventoryItem)this.getInventory().getItems().get(var15);
                     this.removeFromHands(var14);
                     this.getInventory().Remove(var14);
                  }
               } else if ("removeItemID".equals(var1)) {
                  var15 = var2.getInt();
                  String var16 = GameWindow.ReadStringUTF(var2);
                  InventoryItem var17 = this.getInventory().getItemWithID(var15);
                  if (var17 != null && var17.getFullType().equals(var16)) {
                     this.removeFromHands(var17);
                     this.getInventory().Remove(var17);
                  }
               } else if ("removeItemType".equals(var1)) {
                  var10 = GameWindow.ReadStringUTF(var2);
                  var4 = var2.getShort();

                  for(var5 = 0; var5 < var4; ++var5) {
                     this.getInventory().RemoveOneOf(var10);
                  }
               } else if ("removeOneOf".equals(var1)) {
                  var10 = GameWindow.ReadStringUTF(var2);
                  this.getInventory().RemoveOneOf(var10);
               } else if ("reanimatedID".equals(var1)) {
                  this.ReanimatedCorpseID = var2.getInt();
               } else if (!"Shove".equals(var1)) {
                  if ("StopBurning".equals(var1)) {
                     this.StopBurning();
                  } else {
                     int var19;
                     if ("addXp".equals(var1)) {
                        PerkFactory.Perk var18 = PerkFactory.Perks.fromIndex(var2.getInt());
                        var19 = var2.getInt();
                        boolean var21 = var2.get() == 1;
                        if (var21) {
                           this.getXp().AddXPNoMultiplier(var18, (float)var19);
                        } else {
                           this.getXp().AddXP(var18, (float)var19);
                        }
                     } else if ("wakeUp".equals(var1)) {
                        if (this.isAsleep()) {
                           this.Asleep = false;
                           this.ForceWakeUpTime = -1.0F;
                           TutorialManager.instance.StealControl = false;
                           if (this instanceof IsoPlayer && ((IsoPlayer)this).isLocalPlayer()) {
                              UIManager.setFadeBeforeUI(((IsoPlayer)this).getPlayerNum(), true);
                              UIManager.FadeIn((double)((IsoPlayer)this).getPlayerNum(), 2.0D);
                              GameClient.instance.sendPlayer((IsoPlayer)this);
                           }
                        }
                     } else if ("mechanicActionDone".equals(var1)) {
                        boolean var20 = var2.get() == 1;
                        var19 = var2.getInt();
                        String var22 = GameWindow.ReadString(var2);
                        boolean var6 = var2.get() == 1;
                        long var7 = var2.getLong();
                        LuaEventManager.triggerEvent("OnMechanicActionDone", this, var20, var19, var22, var7, var6);
                     } else if ("vehicleNoKey".equals(var1)) {
                        this.SayDebug(" [img=media/ui/CarKey_none.png]");
                     }
                  }
               }
            }
         }
      }

   }

   public int getAlreadyReadPages(String var1) {
      for(int var2 = 0; var2 < this.ReadBooks.size(); ++var2) {
         IsoGameCharacter.ReadBook var3 = (IsoGameCharacter.ReadBook)this.ReadBooks.get(var2);
         if (var3.fullType.equals(var1)) {
            return var3.alreadyReadPages;
         }
      }

      return 0;
   }

   public void setAlreadyReadPages(String var1, int var2) {
      for(int var3 = 0; var3 < this.ReadBooks.size(); ++var3) {
         IsoGameCharacter.ReadBook var4 = (IsoGameCharacter.ReadBook)this.ReadBooks.get(var3);
         if (var4.fullType.equals(var1)) {
            var4.alreadyReadPages = var2;
            return;
         }
      }

      IsoGameCharacter.ReadBook var5 = new IsoGameCharacter.ReadBook();
      var5.fullType = var1;
      var5.alreadyReadPages = var2;
      this.ReadBooks.add(var5);
   }

   public void updateLightInfo() {
      if (GameServer.bServer) {
         if (!this.isZombie()) {
            synchronized(this.lightInfo) {
               this.lightInfo.square = this.movingSq;
               if (this.lightInfo.square == null) {
                  this.lightInfo.square = this.getCell().getGridSquare((int)this.x, (int)this.y, (int)this.z);
               }

               if (this.ReanimatedCorpse != null) {
                  this.lightInfo.square = this.getCell().getGridSquare((int)this.x, (int)this.y, (int)this.z);
               }

               this.lightInfo.x = this.getX();
               this.lightInfo.y = this.getY();
               this.lightInfo.z = this.getZ();
               this.lightInfo.angleX = this.getForwardDirection().getX();
               this.lightInfo.angleY = this.getForwardDirection().getY();
               this.lightInfo.torches.clear();
               this.lightInfo.night = GameTime.getInstance().getNight();
            }
         }
      }
   }

   public IsoGameCharacter.LightInfo initLightInfo2() {
      synchronized(this.lightInfo) {
         for(int var2 = 0; var2 < this.lightInfo2.torches.size(); ++var2) {
            IsoGameCharacter.TorchInfo.release((IsoGameCharacter.TorchInfo)this.lightInfo2.torches.get(var2));
         }

         this.lightInfo2.initFrom(this.lightInfo);
         return this.lightInfo2;
      }
   }

   public IsoGameCharacter.LightInfo getLightInfo2() {
      return this.lightInfo2;
   }

   public void postupdate() {
      IsoGameCharacter.s_performance.postUpdate.invokeAndMeasure(this, IsoGameCharacter::postUpdateInternal);
   }

   private void postUpdateInternal() {
      super.postupdate();
      AnimationPlayer var1 = this.getAnimationPlayer();
      var1.UpdateDir(this);
      boolean var2 = this.shouldBeTurning();
      this.setTurning(var2);
      boolean var3 = this.shouldBeTurning90();
      this.setTurning90(var3);
      boolean var4 = this.shouldBeTurningAround();
      this.setTurningAround(var4);
      this.actionContext.update();
      if (this.getCurrentSquare() != null) {
         this.advancedAnimator.update();
      }

      this.actionContext.clearEvent("ActiveAnimFinished");
      this.actionContext.clearEvent("ActiveAnimFinishing");
      this.actionContext.clearEvent("ActiveAnimLooped");
      var1 = this.getAnimationPlayer();
      if (var1 != null) {
         MoveDeltaModifiers var15 = IsoGameCharacter.L_postUpdate.moveDeltas;
         var15.moveDelta = this.getMoveDelta();
         var15.turnDelta = this.getTurnDelta();
         var3 = this.hasPath();
         var4 = this instanceof IsoPlayer;
         if (var4 && var3 && this.isRunning()) {
            var15.turnDelta = Math.max(var15.turnDelta, 2.0F);
         }

         State var5 = this.getCurrentState();
         if (var5 != null) {
            var5.getDeltaModifiers(this, var15);
         }

         if (var15.twistDelta == -1.0F) {
            var15.twistDelta = var15.turnDelta * 1.8F;
         }

         if (!this.isTurning()) {
            var15.turnDelta = 0.0F;
         }

         float var6 = Math.max(1.0F - var15.moveDelta / 2.0F, 0.0F);
         var1.angleStepDelta = var6 * var15.turnDelta;
         var1.angleTwistDelta = var6 * var15.twistDelta;
         var1.setMaxTwistAngle(0.017453292F * this.getMaxTwist());
      }

      if (this.hasActiveModel()) {
         try {
            ModelManager.ModelSlot var14 = this.legsSprite.modelSlot;
            var14.Update();
         } catch (Throwable var13) {
            ExceptionLogger.logException(var13);
         }
      } else {
         var1 = this.getAnimationPlayer();
         var1.bUpdateBones = false;
         var2 = PerformanceSettings.InterpolateAnims;
         PerformanceSettings.InterpolateAnims = false;

         try {
            var1.UpdateDir(this);
            var1.Update();
         } catch (Throwable var11) {
            ExceptionLogger.logException(var11);
         } finally {
            var1.bUpdateBones = true;
            PerformanceSettings.InterpolateAnims = var2;
         }
      }

      this.updateLightInfo();
      if (this.isAnimationRecorderActive()) {
         this.m_animationRecorder.logVariables(this);
         this.m_animationRecorder.endLine();
      }

   }

   public boolean shouldBeTurning() {
      float var1 = this.getTargetTwist();
      float var2 = PZMath.abs(var1);
      boolean var3 = var2 > 1.0F;
      if (this.isZombie() && this.getCurrentState() == ZombieFallDownState.instance()) {
         return false;
      } else if (this.blockTurning) {
         return false;
      } else if (this.isBehaviourMoving()) {
         return var3;
      } else if (this.isPlayerMoving()) {
         return var3;
      } else if (this.isAttacking()) {
         return !this.bAimAtFloor;
      } else {
         float var4 = this.getAbsoluteExcessTwist();
         if (var4 > 1.0F) {
            return true;
         } else {
            return this.isTurning() ? var3 : false;
         }
      }
   }

   public boolean shouldBeTurning90() {
      if (!this.isTurning()) {
         return false;
      } else if (this.isTurning90()) {
         return true;
      } else {
         float var1 = this.getTargetTwist();
         float var2 = Math.abs(var1);
         return var2 > 65.0F;
      }
   }

   public boolean shouldBeTurningAround() {
      if (!this.isTurning()) {
         return false;
      } else if (this.isTurningAround()) {
         return true;
      } else {
         float var1 = this.getTargetTwist();
         float var2 = Math.abs(var1);
         return var2 > 110.0F;
      }
   }

   private boolean isTurning() {
      return this.m_isTurning;
   }

   private void setTurning(boolean var1) {
      this.m_isTurning = var1;
   }

   private boolean isTurningAround() {
      return this.m_isTurningAround;
   }

   private void setTurningAround(boolean var1) {
      this.m_isTurningAround = var1;
   }

   private boolean isTurning90() {
      return this.m_isTurning90;
   }

   private void setTurning90(boolean var1) {
      this.m_isTurning90 = var1;
   }

   public boolean hasPath() {
      return this.getPath2() != null;
   }

   public boolean isAnimationRecorderActive() {
      return this.m_animationRecorder != null && this.m_animationRecorder.isRecording();
   }

   public AnimationPlayerRecorder getAnimationPlayerRecorder() {
      return this.m_animationRecorder;
   }

   public boolean isSafety() {
      return this.safety;
   }

   public void setSafety(boolean var1) {
      this.safety = var1;
   }

   public float getSafetyCooldown() {
      return this.safetyCooldown;
   }

   public void setSafetyCooldown(float var1) {
      this.safetyCooldown = Math.max(var1, 0.0F);
   }

   public float getMeleeDelay() {
      return this.meleeDelay;
   }

   public void setMeleeDelay(float var1) {
      this.meleeDelay = Math.max(var1, 0.0F);
   }

   public float getRecoilDelay() {
      return this.RecoilDelay;
   }

   public void setRecoilDelay(float var1) {
      if (var1 < 0.0F) {
         var1 = 0.0F;
      }

      this.RecoilDelay = var1;
   }

   public float getBeenMovingFor() {
      return this.BeenMovingFor;
   }

   public void setBeenMovingFor(float var1) {
      if (var1 < 0.0F) {
         var1 = 0.0F;
      }

      if (var1 > 70.0F) {
         var1 = 70.0F;
      }

      this.BeenMovingFor = var1;
   }

   public boolean isForceShove() {
      return this.forceShove;
   }

   public void setForceShove(boolean var1) {
      this.forceShove = var1;
   }

   public String getClickSound() {
      return this.clickSound;
   }

   public void setClickSound(String var1) {
      this.clickSound = var1;
   }

   public int getMeleeCombatMod() {
      int var1 = this.getWeaponLevel();
      if (var1 == 1) {
         return -2;
      } else if (var1 == 2) {
         return 0;
      } else if (var1 == 3) {
         return 1;
      } else if (var1 == 4) {
         return 2;
      } else if (var1 == 5) {
         return 3;
      } else if (var1 == 6) {
         return 4;
      } else if (var1 == 7) {
         return 5;
      } else if (var1 == 8) {
         return 5;
      } else if (var1 == 9) {
         return 6;
      } else {
         return var1 == 10 ? 7 : -5;
      }
   }

   public int getWeaponLevel() {
      WeaponType var1 = WeaponType.getWeaponType(this);
      int var2 = -1;
      if (var1 != null && var1 != WeaponType.barehand) {
         if (((HandWeapon)this.getPrimaryHandItem()).getCategories().contains("Axe")) {
            var2 = this.getPerkLevel(PerkFactory.Perks.Axe);
         }

         if (((HandWeapon)this.getPrimaryHandItem()).getCategories().contains("Spear")) {
            var2 += this.getPerkLevel(PerkFactory.Perks.Spear);
         }

         if (((HandWeapon)this.getPrimaryHandItem()).getCategories().contains("SmallBlade")) {
            var2 += this.getPerkLevel(PerkFactory.Perks.SmallBlade);
         }

         if (((HandWeapon)this.getPrimaryHandItem()).getCategories().contains("LongBlade")) {
            var2 += this.getPerkLevel(PerkFactory.Perks.LongBlade);
         }

         if (((HandWeapon)this.getPrimaryHandItem()).getCategories().contains("Blunt")) {
            var2 += this.getPerkLevel(PerkFactory.Perks.Blunt);
         }

         if (((HandWeapon)this.getPrimaryHandItem()).getCategories().contains("SmallBlunt")) {
            var2 += this.getPerkLevel(PerkFactory.Perks.SmallBlunt);
         }
      }

      return var2 == -1 ? 0 : var2;
   }

   public int getMaintenanceMod() {
      int var1 = this.getPerkLevel(PerkFactory.Perks.Maintenance);
      var1 += this.getWeaponLevel() / 2;
      return var1 / 2;
   }

   public BaseVehicle getVehicle() {
      return this.vehicle;
   }

   public void setVehicle(BaseVehicle var1) {
      this.vehicle = var1;
   }

   public boolean isUnderVehicle() {
      int var1 = ((int)this.x - 4) / 10;
      int var2 = ((int)this.y - 4) / 10;
      int var3 = (int)Math.ceil((double)((this.x + 4.0F) / 10.0F));
      int var4 = (int)Math.ceil((double)((this.y + 4.0F) / 10.0F));
      Vector2 var5 = (Vector2)((BaseVehicle.Vector2ObjectPool)BaseVehicle.TL_vector2_pool.get()).alloc();

      for(int var6 = var2; var6 < var4; ++var6) {
         for(int var7 = var1; var7 < var3; ++var7) {
            IsoChunk var8 = GameServer.bServer ? ServerMap.instance.getChunk(var7, var6) : IsoWorld.instance.CurrentCell.getChunkForGridSquare(var7 * 10, var6 * 10, 0);
            if (var8 != null) {
               for(int var9 = 0; var9 < var8.vehicles.size(); ++var9) {
                  BaseVehicle var10 = (BaseVehicle)var8.vehicles.get(var9);
                  Vector2 var11 = var10.testCollisionWithCharacter(this, 0.3F, var5);
                  if (var11 != null && var11.x != -1.0F) {
                     ((BaseVehicle.Vector2ObjectPool)BaseVehicle.TL_vector2_pool.get()).release(var5);
                     return true;
                  }
               }
            }
         }
      }

      ((BaseVehicle.Vector2ObjectPool)BaseVehicle.TL_vector2_pool.get()).release(var5);
      return false;
   }

   public boolean isProne() {
      return this.isOnFloor();
   }

   public boolean isBeingSteppedOn() {
      if (!this.isOnFloor()) {
         return false;
      } else {
         for(int var1 = -1; var1 <= 1; ++var1) {
            for(int var2 = -1; var2 <= 1; ++var2) {
               IsoGridSquare var3 = this.getCell().getGridSquare((int)this.x + var2, (int)this.y + var1, (int)this.z);
               if (var3 != null) {
                  ArrayList var4 = var3.getMovingObjects();

                  for(int var5 = 0; var5 < var4.size(); ++var5) {
                     IsoMovingObject var6 = (IsoMovingObject)var4.get(var5);
                     if (var6 != this) {
                        IsoGameCharacter var7 = (IsoGameCharacter)Type.tryCastTo(var6, IsoGameCharacter.class);
                        if (var7 != null && var7.getVehicle() == null && !var6.isOnFloor() && ZombieOnGroundState.isCharacterStandingOnOther(var7, this)) {
                           return true;
                        }
                     }
                  }
               }
            }
         }

         return false;
      }
   }

   public float getTemperature() {
      return this.getBodyDamage().getTemperature();
   }

   public void setTemperature(float var1) {
      this.getBodyDamage().setTemperature(var1);
   }

   public float getReduceInfectionPower() {
      return this.reduceInfectionPower;
   }

   public void setReduceInfectionPower(float var1) {
      this.reduceInfectionPower = var1;
   }

   public float getInventoryWeight() {
      if (this.getInventory() == null) {
         return 0.0F;
      } else {
         float var1 = 0.0F;
         ArrayList var2 = this.getInventory().getItems();

         for(int var3 = 0; var3 < var2.size(); ++var3) {
            InventoryItem var4 = (InventoryItem)var2.get(var3);
            if (var4.getAttachedSlot() > -1 && !this.isEquipped(var4)) {
               var1 += var4.getHotbarEquippedWeight();
            } else if (this.isEquipped(var4)) {
               var1 += var4.getEquippedWeight();
            } else {
               var1 += var4.getUnequippedWeight();
            }
         }

         return var1;
      }
   }

   public void dropHandItems() {
      if (!"Tutorial".equals(Core.GameMode)) {
         if (!(this instanceof IsoPlayer) || ((IsoPlayer)this).isLocalPlayer()) {
            this.dropHeavyItems();
            IsoGridSquare var1 = this.getCurrentSquare();
            if (var1 != null) {
               InventoryItem var2 = this.getPrimaryHandItem();
               InventoryItem var3 = this.getSecondaryHandItem();
               if (var2 != null || var3 != null) {
                  var1 = this.getSolidFloorAt(var1.x, var1.y, var1.z);
                  if (var1 != null) {
                     float var4 = Rand.Next(0.1F, 0.9F);
                     float var5 = Rand.Next(0.1F, 0.9F);
                     float var6 = var1.getApparentZ(var4, var5) - (float)var1.getZ();
                     boolean var7 = false;
                     if (var3 == var2) {
                        var7 = true;
                     }

                     if (var2 != null) {
                        this.setPrimaryHandItem((InventoryItem)null);
                        this.getInventory().DoRemoveItem(var2);
                        var1.AddWorldInventoryItem(var2, var4, var5, var6);
                        LuaEventManager.triggerEvent("OnContainerUpdate");
                        LuaEventManager.triggerEvent("onItemFall", var2);
                     }

                     if (var3 != null) {
                        this.setSecondaryHandItem((InventoryItem)null);
                        if (!var7) {
                           this.getInventory().DoRemoveItem(var3);
                           var1.AddWorldInventoryItem(var3, var4, var5, var6);
                           LuaEventManager.triggerEvent("OnContainerUpdate");
                           LuaEventManager.triggerEvent("onItemFall", var3);
                        }
                     }

                     this.resetEquippedHandsModels();
                  }
               }
            }
         }
      }
   }

   public boolean shouldBecomeZombieAfterDeath() {
      float var10000;
      BodyDamage var10001;
      boolean var1;
      switch(SandboxOptions.instance.Lore.Transmission.getValue()) {
      case 1:
         if (!this.getBodyDamage().IsFakeInfected()) {
            var10000 = this.getBodyDamage().getInfectionLevel();
            var10001 = this.BodyDamage;
            if (var10000 >= 0.001F) {
               var1 = true;
               return var1;
            }
         }

         var1 = false;
         return var1;
      case 2:
         if (!this.getBodyDamage().IsFakeInfected()) {
            var10000 = this.getBodyDamage().getInfectionLevel();
            var10001 = this.BodyDamage;
            if (var10000 >= 0.001F) {
               var1 = true;
               return var1;
            }
         }

         var1 = false;
         return var1;
      case 3:
         return true;
      case 4:
         return false;
      default:
         return false;
      }
   }

   public void applyTraits(ArrayList var1) {
      if (var1 != null) {
         HashMap var2 = new HashMap();
         var2.put(PerkFactory.Perks.Fitness, 5);
         var2.put(PerkFactory.Perks.Strength, 5);

         for(int var3 = 0; var3 < var1.size(); ++var3) {
            String var4 = (String)var1.get(var3);
            if (var4 != null && !var4.isEmpty()) {
               TraitFactory.Trait var5 = TraitFactory.getTrait(var4);
               if (var5 != null) {
                  if (!this.HasTrait(var4)) {
                     this.getTraits().add(var4);
                  }

                  HashMap var6 = var5.getXPBoostMap();
                  PerkFactory.Perk var9;
                  int var10;
                  if (var6 != null) {
                     for(Iterator var7 = var6.entrySet().iterator(); var7.hasNext(); var2.put(var9, var10)) {
                        Entry var8 = (Entry)var7.next();
                        var9 = (PerkFactory.Perk)var8.getKey();
                        var10 = (Integer)var8.getValue();
                        if (var2.containsKey(var9)) {
                           var10 += (Integer)var2.get(var9);
                        }
                     }
                  }
               }
            }
         }

         if (this instanceof IsoPlayer) {
            ((IsoPlayer)this).getNutrition().applyWeightFromTraits();
         }

         HashMap var11 = this.getDescriptor().getXPBoostMap();

         Iterator var12;
         Entry var13;
         PerkFactory.Perk var14;
         int var15;
         for(var12 = var11.entrySet().iterator(); var12.hasNext(); var2.put(var14, var15)) {
            var13 = (Entry)var12.next();
            var14 = (PerkFactory.Perk)var13.getKey();
            var15 = (Integer)var13.getValue();
            if (var2.containsKey(var14)) {
               var15 += (Integer)var2.get(var14);
            }
         }

         var12 = var2.entrySet().iterator();

         while(var12.hasNext()) {
            var13 = (Entry)var12.next();
            var14 = (PerkFactory.Perk)var13.getKey();
            var15 = (Integer)var13.getValue();
            var15 = Math.max(0, var15);
            var15 = Math.min(10, var15);
            this.getDescriptor().getXPBoostMap().put(var14, Math.min(3, var15));

            for(int var16 = 0; var16 < var15; ++var16) {
               this.LevelPerk(var14);
            }

            this.getXp().setXPToLevel(var14, this.getPerkLevel(var14));
         }

      }
   }

   public void createKeyRing() {
      InventoryItem var1 = this.getInventory().AddItem("Base.KeyRing");
      if (var1 != null && var1 instanceof InventoryContainer) {
         InventoryContainer var2 = (InventoryContainer)var1;
         var2.setName(Translator.getText("IGUI_KeyRingName", this.getDescriptor().getForename(), this.getDescriptor().getSurname()));
         if (Rand.Next(100) < 40) {
            RoomDef var3 = IsoWorld.instance.MetaGrid.getRoomAt((int)this.getX(), (int)this.getY(), (int)this.getZ());
            if (var3 != null && var3.getBuilding() != null) {
               int var10000 = Rand.Next(5);
               String var4 = "Base.Key" + (var10000 + 1);
               InventoryItem var5 = var2.getInventory().AddItem(var4);
               var5.setKeyId(var3.getBuilding().getKeyId());
            }
         }

      }
   }

   public void autoDrink() {
      if (!GameServer.bServer) {
         if (!GameClient.bClient || ((IsoPlayer)this).isLocalPlayer()) {
            if (Core.getInstance().getOptionAutoDrink()) {
               if (!LuaHookManager.TriggerHook("AutoDrink", this)) {
                  if (!(this.stats.thirst <= 0.1F)) {
                     InventoryItem var1 = this.getWaterSource(this.getInventory().getItems());
                     if (var1 != null) {
                        Stats var10000 = this.stats;
                        var10000.thirst -= 0.1F;
                        if (GameClient.bClient) {
                           GameClient.instance.drink((IsoPlayer)this, 0.1F);
                        }

                        var1.Use();
                     }

                  }
               }
            }
         }
      }
   }

   public InventoryItem getWaterSource(ArrayList var1) {
      InventoryItem var2 = null;
      new ArrayList();

      for(int var4 = 0; var4 < var1.size(); ++var4) {
         InventoryItem var5 = (InventoryItem)var1.get(var4);
         if (var5.isWaterSource() && !var5.isBeingFilled() && !var5.isTaintedWater()) {
            if (var5 instanceof Drainable) {
               if (((Drainable)var5).getUsedDelta() > 0.0F) {
                  var2 = var5;
                  break;
               }
            } else if (!(var5 instanceof InventoryContainer)) {
               var2 = var5;
               break;
            }
         }
      }

      return var2;
   }

   public List getKnownRecipes() {
      return this.knownRecipes;
   }

   public boolean isRecipeKnown(Recipe var1) {
      if (DebugOptions.instance.CheatRecipeKnowAll.getValue()) {
         return true;
      } else {
         return !var1.needToBeLearn() || this.getKnownRecipes().contains(var1.getOriginalname());
      }
   }

   public boolean isRecipeKnown(String var1) {
      Recipe var2 = ScriptManager.instance.getRecipe(var1);
      if (var2 == null) {
         return DebugOptions.instance.CheatRecipeKnowAll.getValue() ? true : this.getKnownRecipes().contains(var1);
      } else {
         return this.isRecipeKnown(var2);
      }
   }

   public boolean learnRecipe(String var1) {
      if (!this.isRecipeKnown(var1)) {
         this.getKnownRecipes().add(var1);
         return true;
      } else {
         return false;
      }
   }

   public boolean isMoving() {
      return this instanceof IsoPlayer && !((IsoPlayer)this).isAttackAnimThrowTimeOut() ? false : this.m_isMoving;
   }

   public boolean isBehaviourMoving() {
      State var1 = this.getCurrentState();
      return var1 != null && var1.isMoving(this);
   }

   public boolean isPlayerMoving() {
      return false;
   }

   public void setMoving(boolean var1) {
      this.m_isMoving = var1;
      if (GameClient.bClient && this instanceof IsoPlayer && ((IsoPlayer)this).bRemote) {
         ((IsoPlayer)this).m_isPlayerMoving = var1;
         ((IsoPlayer)this).setJustMoved(var1);
      }

   }

   private boolean isFacingNorthWesterly() {
      return this.dir == IsoDirections.W || this.dir == IsoDirections.NW || this.dir == IsoDirections.N || this.dir == IsoDirections.NE;
   }

   public boolean isAttacking() {
      return false;
   }

   public boolean isZombieAttacking() {
      return false;
   }

   public boolean isZombieAttacking(IsoMovingObject var1) {
      return false;
   }

   private boolean isZombieThumping() {
      if (this.isZombie()) {
         return this.getCurrentState() == ThumpState.instance();
      } else {
         return false;
      }
   }

   public int compareMovePriority(IsoGameCharacter var1) {
      if (var1 == null) {
         return 1;
      } else if (this.isZombieThumping() && !var1.isZombieThumping()) {
         return 1;
      } else if (!this.isZombieThumping() && var1.isZombieThumping()) {
         return -1;
      } else if (var1 instanceof IsoPlayer) {
         return GameClient.bClient && this.isZombieAttacking(var1) ? -1 : 0;
      } else if (this.isZombieAttacking() && !var1.isZombieAttacking()) {
         return 1;
      } else if (!this.isZombieAttacking() && var1.isZombieAttacking()) {
         return -1;
      } else if (this.isBehaviourMoving() && !var1.isBehaviourMoving()) {
         return 1;
      } else if (!this.isBehaviourMoving() && var1.isBehaviourMoving()) {
         return -1;
      } else if (this.isFacingNorthWesterly() && !var1.isFacingNorthWesterly()) {
         return 1;
      } else {
         return !this.isFacingNorthWesterly() && var1.isFacingNorthWesterly() ? -1 : 0;
      }
   }

   public long playSound(String var1) {
      return this.getEmitter().playSound(var1);
   }

   public void stopOrTriggerSound(long var1) {
      this.getEmitter().stopOrTriggerSound(var1);
   }

   public void addWorldSoundUnlessInvisible(int var1, int var2, boolean var3) {
      if (!this.isInvisible()) {
         WorldSoundManager.instance.addSound(this, (int)this.getX(), (int)this.getY(), (int)this.getZ(), var1, var2, var3);
      }
   }

   public boolean isKnownPoison(InventoryItem var1) {
      if (var1 instanceof Food) {
         Food var2 = (Food)var1;
         if (var2.getPoisonPower() <= 0) {
            return false;
         } else if (var2.getHerbalistType() != null && !var2.getHerbalistType().isEmpty()) {
            return this.isRecipeKnown("Herbalist");
         } else if (var2.getPoisonDetectionLevel() >= 0 && this.getPerkLevel(PerkFactory.Perks.Cooking) >= 10 - var2.getPoisonDetectionLevel()) {
            return true;
         } else {
            return var2.getPoisonLevelForRecipe() != null;
         }
      } else {
         return false;
      }
   }

   public int getLastHourSleeped() {
      return this.lastHourSleeped;
   }

   public void setLastHourSleeped(int var1) {
      this.lastHourSleeped = var1;
   }

   public void setTimeOfSleep(float var1) {
      this.timeOfSleep = var1;
   }

   public void setDelayToSleep(float var1) {
      this.delayToActuallySleep = var1;
   }

   public String getBedType() {
      return this.bedType;
   }

   public void setBedType(String var1) {
      this.bedType = var1;
   }

   public void enterVehicle(BaseVehicle var1, int var2, Vector3f var3) {
      if (this.vehicle != null) {
         this.vehicle.exit(this);
      }

      if (var1 != null) {
         var1.enter(var2, this, var3);
      }

   }

   public float Hit(BaseVehicle var1, float var2, boolean var3, float var4, float var5) {
      this.setHitFromBehind(var3);
      this.setAttackedBy(var1.getDriver());
      this.getHitDir().set(var4, var5);
      if (!this.isKnockedDown()) {
         this.setHitForce(Math.max(0.5F, var2 * 0.15F));
      } else {
         this.setHitForce(Math.min(2.5F, var2 * 0.15F));
      }

      if (GameClient.bClient) {
         HitReactionNetworkAI.CalcHitReactionVehicle(this, var1);
      }

      if (Core.bDebug) {
         DebugLog.log(DebugType.Multiplayer, String.format("Vehicle id=%d hit %s id=%d: speed=%f force=%f hitDir=%s", var1.getId(), this.getClass().getSimpleName(), this.getOnlineID(), var2, this.getHitForce(), this.getHitDir()));
      }

      return this.getHealth();
   }

   public PolygonalMap2.Path getPath2() {
      return this.path2;
   }

   public void setPath2(PolygonalMap2.Path var1) {
      this.path2 = var1;
   }

   public PathFindBehavior2 getPathFindBehavior2() {
      return this.pfb2;
   }

   public MapKnowledge getMapKnowledge() {
      return this.mapKnowledge;
   }

   public IsoObject getBed() {
      return this.isAsleep() ? this.bed : null;
   }

   public void setBed(IsoObject var1) {
      this.bed = var1;
   }

   public boolean avoidDamage() {
      return this.m_avoidDamage;
   }

   public void setAvoidDamage(boolean var1) {
      this.m_avoidDamage = var1;
   }

   public boolean isReading() {
      return this.isReading;
   }

   public void setReading(boolean var1) {
      this.isReading = var1;
   }

   public float getTimeSinceLastSmoke() {
      return this.timeSinceLastSmoke;
   }

   public void setTimeSinceLastSmoke(float var1) {
      this.timeSinceLastSmoke = PZMath.clamp(var1, 0.0F, 10.0F);
   }

   public boolean isInvisible() {
      return this.m_invisible;
   }

   public void setInvisible(boolean var1) {
      this.m_invisible = var1;
   }

   public boolean isDriving() {
      return this.getVehicle() != null && this.getVehicle().getDriver() == this && Math.abs(this.getVehicle().getCurrentSpeedKmHour()) > 1.0F;
   }

   public boolean isInARoom() {
      return this.square != null && this.square.isInARoom();
   }

   public boolean isGodMod() {
      return this.m_godMod;
   }

   public void setGodMod(boolean var1) {
      this.m_godMod = var1;
      if (this instanceof IsoPlayer && GameClient.bClient && ((IsoPlayer)this).isLocalPlayer()) {
         this.updateMovementRates();
         GameClient.sendPlayerInjuries((IsoPlayer)this);
         GameClient.sendPlayerDamage((IsoPlayer)this);
      }

   }

   public boolean isUnlimitedCarry() {
      return this.unlimitedCarry;
   }

   public void setUnlimitedCarry(boolean var1) {
      this.unlimitedCarry = var1;
   }

   public boolean isBuildCheat() {
      return this.buildCheat;
   }

   public void setBuildCheat(boolean var1) {
      this.buildCheat = var1;
   }

   public boolean isFarmingCheat() {
      return this.farmingCheat;
   }

   public void setFarmingCheat(boolean var1) {
      this.farmingCheat = var1;
   }

   public boolean isHealthCheat() {
      return this.healthCheat;
   }

   public void setHealthCheat(boolean var1) {
      this.healthCheat = var1;
   }

   public boolean isMechanicsCheat() {
      return this.mechanicsCheat;
   }

   public void setMechanicsCheat(boolean var1) {
      this.mechanicsCheat = var1;
   }

   public boolean isMovablesCheat() {
      return this.movablesCheat;
   }

   public void setMovablesCheat(boolean var1) {
      this.movablesCheat = var1;
   }

   public boolean isTimedActionInstantCheat() {
      return this.timedActionInstantCheat;
   }

   public void setTimedActionInstantCheat(boolean var1) {
      this.timedActionInstantCheat = var1;
   }

   public boolean isTimedActionInstant() {
      return Core.bDebug && DebugOptions.instance.CheatTimedActionInstant.getValue() ? true : this.isTimedActionInstantCheat();
   }

   public boolean isShowAdminTag() {
      return this.showAdminTag;
   }

   public void setShowAdminTag(boolean var1) {
      this.showAdminTag = var1;
   }

   public IAnimationVariableSlot getVariable(AnimationVariableHandle var1) {
      return this.getGameVariablesInternal().getVariable(var1);
   }

   public IAnimationVariableSlot getVariable(String var1) {
      return this.getGameVariablesInternal().getVariable(var1);
   }

   public IAnimationVariableSlot getOrCreateVariable(String var1) {
      return this.getGameVariablesInternal().getOrCreateVariable(var1);
   }

   public void setVariable(IAnimationVariableSlot var1) {
      this.getGameVariablesInternal().setVariable(var1);
   }

   public void setVariable(String var1, String var2) {
      this.getGameVariablesInternal().setVariable(var1, var2);
   }

   public void setVariable(String var1, boolean var2) {
      this.getGameVariablesInternal().setVariable(var1, var2);
   }

   public void setVariable(String var1, float var2) {
      this.getGameVariablesInternal().setVariable(var1, var2);
   }

   protected void setVariable(String var1, AnimationVariableSlotCallbackBool.CallbackGetStrongTyped var2) {
      this.getGameVariablesInternal().setVariable(var1, var2);
   }

   protected void setVariable(String var1, AnimationVariableSlotCallbackBool.CallbackGetStrongTyped var2, AnimationVariableSlotCallbackBool.CallbackSetStrongTyped var3) {
      this.getGameVariablesInternal().setVariable(var1, var2, var3);
   }

   protected void setVariable(String var1, AnimationVariableSlotCallbackString.CallbackGetStrongTyped var2) {
      this.getGameVariablesInternal().setVariable(var1, var2);
   }

   protected void setVariable(String var1, AnimationVariableSlotCallbackString.CallbackGetStrongTyped var2, AnimationVariableSlotCallbackString.CallbackSetStrongTyped var3) {
      this.getGameVariablesInternal().setVariable(var1, var2, var3);
   }

   protected void setVariable(String var1, AnimationVariableSlotCallbackFloat.CallbackGetStrongTyped var2) {
      this.getGameVariablesInternal().setVariable(var1, var2);
   }

   protected void setVariable(String var1, AnimationVariableSlotCallbackFloat.CallbackGetStrongTyped var2, AnimationVariableSlotCallbackFloat.CallbackSetStrongTyped var3) {
      this.getGameVariablesInternal().setVariable(var1, var2, var3);
   }

   protected void setVariable(String var1, AnimationVariableSlotCallbackInt.CallbackGetStrongTyped var2) {
      this.getGameVariablesInternal().setVariable(var1, var2);
   }

   protected void setVariable(String var1, AnimationVariableSlotCallbackInt.CallbackGetStrongTyped var2, AnimationVariableSlotCallbackInt.CallbackSetStrongTyped var3) {
      this.getGameVariablesInternal().setVariable(var1, var2, var3);
   }

   public void setVariable(String var1, boolean var2, AnimationVariableSlotCallbackBool.CallbackGetStrongTyped var3) {
      this.getGameVariablesInternal().setVariable(var1, var2, var3);
   }

   public void setVariable(String var1, boolean var2, AnimationVariableSlotCallbackBool.CallbackGetStrongTyped var3, AnimationVariableSlotCallbackBool.CallbackSetStrongTyped var4) {
      this.getGameVariablesInternal().setVariable(var1, var2, var3, var4);
   }

   public void setVariable(String var1, String var2, AnimationVariableSlotCallbackString.CallbackGetStrongTyped var3) {
      this.getGameVariablesInternal().setVariable(var1, var2, var3);
   }

   public void setVariable(String var1, String var2, AnimationVariableSlotCallbackString.CallbackGetStrongTyped var3, AnimationVariableSlotCallbackString.CallbackSetStrongTyped var4) {
      this.getGameVariablesInternal().setVariable(var1, var2, var3, var4);
   }

   public void setVariable(String var1, float var2, AnimationVariableSlotCallbackFloat.CallbackGetStrongTyped var3) {
      this.getGameVariablesInternal().setVariable(var1, var2, var3);
   }

   public void setVariable(String var1, float var2, AnimationVariableSlotCallbackFloat.CallbackGetStrongTyped var3, AnimationVariableSlotCallbackFloat.CallbackSetStrongTyped var4) {
      this.getGameVariablesInternal().setVariable(var1, var2, var3, var4);
   }

   public void setVariable(String var1, int var2, AnimationVariableSlotCallbackInt.CallbackGetStrongTyped var3) {
      this.getGameVariablesInternal().setVariable(var1, var2, var3);
   }

   public void setVariable(String var1, int var2, AnimationVariableSlotCallbackInt.CallbackGetStrongTyped var3, AnimationVariableSlotCallbackInt.CallbackSetStrongTyped var4) {
      this.getGameVariablesInternal().setVariable(var1, var2, var3, var4);
   }

   public void clearVariable(String var1) {
      this.getGameVariablesInternal().clearVariable(var1);
   }

   public void clearVariables() {
      this.getGameVariablesInternal().clearVariables();
   }

   public String getVariableString(String var1) {
      return this.getGameVariablesInternal().getVariableString(var1);
   }

   private String getFootInjuryType() {
      if (!(this instanceof IsoPlayer)) {
         return "";
      } else {
         BodyPart var1 = this.getBodyDamage().getBodyPart(BodyPartType.Foot_L);
         BodyPart var2 = this.getBodyDamage().getBodyPart(BodyPartType.Foot_R);
         if (!this.bRunning) {
            if (var1.haveBullet() || var1.getBurnTime() > 5.0F || var1.bitten() || var1.deepWounded() || var1.isSplint() || var1.getFractureTime() > 0.0F || var1.haveGlass()) {
               return "leftheavy";
            }

            if (var2.haveBullet() || var2.getBurnTime() > 5.0F || var2.bitten() || var2.deepWounded() || var2.isSplint() || var2.getFractureTime() > 0.0F || var2.haveGlass()) {
               return "rightheavy";
            }
         }

         if (!(var1.getScratchTime() > 5.0F) && !(var1.getCutTime() > 7.0F) && !(var1.getBurnTime() > 0.0F)) {
            if (!(var2.getScratchTime() > 5.0F) && !(var2.getCutTime() > 7.0F) && !(var2.getBurnTime() > 0.0F)) {
               return "";
            } else {
               return "rightlight";
            }
         } else {
            return "leftlight";
         }
      }
   }

   public float getVariableFloat(String var1, float var2) {
      return this.getGameVariablesInternal().getVariableFloat(var1, var2);
   }

   public boolean getVariableBoolean(String var1) {
      return this.getGameVariablesInternal().getVariableBoolean(var1);
   }

   public boolean isVariable(String var1, String var2) {
      return this.getGameVariablesInternal().isVariable(var1, var2);
   }

   public boolean containsVariable(String var1) {
      return this.getGameVariablesInternal().containsVariable(var1);
   }

   public Iterable getGameVariables() {
      return this.getGameVariablesInternal().getGameVariables();
   }

   private AnimationVariableSource getGameVariablesInternal() {
      return this.m_PlaybackGameVariables != null ? this.m_PlaybackGameVariables : this.m_GameVariables;
   }

   public AnimationVariableSource startPlaybackGameVariables() {
      if (this.m_PlaybackGameVariables != null) {
         DebugLog.General.error("Error! PlaybackGameVariables is already active.");
         return this.m_PlaybackGameVariables;
      } else {
         AnimationVariableSource var1 = new AnimationVariableSource();
         Iterator var2 = this.getGameVariables().iterator();

         while(var2.hasNext()) {
            IAnimationVariableSlot var3 = (IAnimationVariableSlot)var2.next();
            AnimationVariableType var4 = var3.getType();
            switch(var4) {
            case String:
               var1.setVariable(var3.getKey(), var3.getValueString());
               break;
            case Float:
               var1.setVariable(var3.getKey(), var3.getValueFloat());
               break;
            case Boolean:
               var1.setVariable(var3.getKey(), var3.getValueBool());
            case Void:
               break;
            default:
               DebugLog.General.error("Error! Variable type not handled: %s", var4.toString());
            }
         }

         this.m_PlaybackGameVariables = var1;
         return this.m_PlaybackGameVariables;
      }
   }

   public void endPlaybackGameVariables(AnimationVariableSource var1) {
      if (this.m_PlaybackGameVariables != var1) {
         DebugLog.General.error("Error! Playback GameVariables do not match.");
      }

      this.m_PlaybackGameVariables = null;
   }

   public void playbackSetCurrentStateSnapshot(ActionStateSnapshot var1) {
      if (this.actionContext != null) {
         this.actionContext.setPlaybackStateSnapshot(var1);
      }
   }

   public ActionStateSnapshot playbackRecordCurrentStateSnapshot() {
      return this.actionContext == null ? null : this.actionContext.getPlaybackStateSnapshot();
   }

   public String GetVariable(String var1) {
      return this.getVariableString(var1);
   }

   public void SetVariable(String var1, String var2) {
      this.setVariable(var1, var2);
   }

   public void ClearVariable(String var1) {
      this.clearVariable(var1);
   }

   public void actionStateChanged(ActionContext var1) {
      ArrayList var2 = IsoGameCharacter.L_actionStateChanged.stateNames;
      PZArrayUtil.listConvert(var1.getChildStates(), var2, (var0) -> {
         return var0.name;
      });
      this.advancedAnimator.SetState(var1.getCurrentStateName(), var2);

      try {
         ++this.stateMachine.activeStateChanged;
         State var3 = (State)this.m_stateUpdateLookup.get(var1.getCurrentStateName().toLowerCase());
         if (var3 == null) {
            var3 = this.defaultState;
         }

         ArrayList var4 = IsoGameCharacter.L_actionStateChanged.states;
         PZArrayUtil.listConvert(var1.getChildStates(), var4, this.m_stateUpdateLookup, (var0, var1x) -> {
            return (State)var1x.get(var0.name.toLowerCase());
         });
         this.stateMachine.changeState(var3, var4);
      } finally {
         --this.stateMachine.activeStateChanged;
      }

   }

   public boolean isFallOnFront() {
      return this.fallOnFront;
   }

   public void setFallOnFront(boolean var1) {
      this.fallOnFront = var1;
   }

   public boolean isHitFromBehind() {
      return this.hitFromBehind;
   }

   public void setHitFromBehind(boolean var1) {
      this.hitFromBehind = var1;
   }

   public void reportEvent(String var1) {
      this.actionContext.reportEvent(var1);
   }

   public void StartTimedActionAnim(String var1) {
      this.StartTimedActionAnim(var1, (String)null);
   }

   public void StartTimedActionAnim(String var1, String var2) {
      this.reportEvent(var1);
      if (var2 != null) {
         this.setVariable("TimedActionType", var2);
      }

      this.resetModelNextFrame();
   }

   public void StopTimedActionAnim() {
      this.clearVariable("TimedActionType");
      this.reportEvent("Event_TA_Exit");
      this.resetModelNextFrame();
   }

   public boolean hasHitReaction() {
      return !StringUtils.isNullOrEmpty(this.getHitReaction());
   }

   public String getHitReaction() {
      return this.hitReaction;
   }

   public void setHitReaction(String var1) {
      this.hitReaction = var1;
   }

   public void CacheEquipped() {
      this.cacheEquiped[0] = this.getPrimaryHandItem();
      this.cacheEquiped[1] = this.getSecondaryHandItem();
   }

   public InventoryItem GetPrimaryEquippedCache() {
      return this.cacheEquiped[0] != null && this.inventory.contains(this.cacheEquiped[0]) ? this.cacheEquiped[0] : null;
   }

   public InventoryItem GetSecondaryEquippedCache() {
      return this.cacheEquiped[1] != null && this.inventory.contains(this.cacheEquiped[1]) ? this.cacheEquiped[1] : null;
   }

   public void ClearEquippedCache() {
      this.cacheEquiped[0] = null;
      this.cacheEquiped[1] = null;
   }

   public boolean isBehind(IsoGameCharacter var1) {
      Vector2 var2 = tempVector2_1.set(this.getX(), this.getY());
      Vector2 var3 = tempVector2_2.set(var1.getX(), var1.getY());
      var3.x -= var2.x;
      var3.y -= var2.y;
      Vector2 var4 = var1.getForwardDirection();
      var3.normalize();
      var4.normalize();
      float var5 = var3.dot(var4);
      return (double)var5 > 0.6D;
   }

   public void resetEquippedHandsModels() {
      if (!GameServer.bServer || ServerGUI.isCreated()) {
         if (this.hasActiveModel()) {
            ModelManager.instance.ResetEquippedNextFrame(this);
         }
      }
   }

   public AnimatorDebugMonitor getDebugMonitor() {
      return this.advancedAnimator.getDebugMonitor();
   }

   public void setDebugMonitor(AnimatorDebugMonitor var1) {
      this.advancedAnimator.setDebugMonitor(var1);
   }

   public boolean isAimAtFloor() {
      return this.bAimAtFloor;
   }

   public void setAimAtFloor(boolean var1) {
      this.bAimAtFloor = var1;
   }

   public String testDotSide(IsoMovingObject var1) {
      Vector2 var2 = this.getLookVector(IsoGameCharacter.l_testDotSide.v1);
      Vector2 var3 = IsoGameCharacter.l_testDotSide.v2.set(this.getX(), this.getY());
      Vector2 var4 = IsoGameCharacter.l_testDotSide.v3.set(var1.x - var3.x, var1.y - var3.y);
      var4.normalize();
      float var5 = Vector2.dot(var4.x, var4.y, var2.x, var2.y);
      if ((double)var5 > 0.7D) {
         return "FRONT";
      } else if (var5 < 0.0F && (double)var5 < -0.5D) {
         return "BEHIND";
      } else {
         float var6 = var1.x;
         float var7 = var1.y;
         float var8 = var3.x;
         float var9 = var3.y;
         float var10 = var3.x + var2.x;
         float var11 = var3.y + var2.y;
         float var12 = (var6 - var8) * (var11 - var9) - (var7 - var9) * (var10 - var8);
         return var12 > 0.0F ? "RIGHT" : "LEFT";
      }
   }

   public void addBasicPatch(BloodBodyPartType var1) {
      if (this instanceof IHumanVisual) {
         if (var1 == null) {
            var1 = BloodBodyPartType.FromIndex(Rand.Next(0, BloodBodyPartType.MAX.index()));
         }

         HumanVisual var2 = ((IHumanVisual)this).getHumanVisual();
         this.getItemVisuals(tempItemVisuals);
         BloodClothingType.addBasicPatch(var1, var2, tempItemVisuals);
         this.bUpdateModelTextures = true;
         this.bUpdateEquippedTextures = true;
         if (!GameServer.bServer && this instanceof IsoPlayer && ((IsoPlayer)this).isLocalPlayer()) {
            LuaEventManager.triggerEvent("OnClothingUpdated", this);
         }

      }
   }

   public void addHole(BloodBodyPartType var1) {
      this.addHole(var1, false);
   }

   public void addHole(BloodBodyPartType var1, boolean var2) {
      if (this instanceof IHumanVisual) {
         if (var1 == null) {
            var1 = BloodBodyPartType.FromIndex(OutfitRNG.Next(0, BloodBodyPartType.MAX.index()));
         }

         HumanVisual var3 = ((IHumanVisual)this).getHumanVisual();
         this.getItemVisuals(tempItemVisuals);
         BloodClothingType.addHole(var1, var3, tempItemVisuals, var2);
         this.bUpdateModelTextures = true;
         if (!GameServer.bServer && this instanceof IsoPlayer && ((IsoPlayer)this).isLocalPlayer()) {
            LuaEventManager.triggerEvent("OnClothingUpdated", this);
            if (GameClient.bClient) {
               GameClient.instance.sendClothing((IsoPlayer)this, "", (InventoryItem)null);
            }
         }

      }
   }

   public void addDirt(BloodBodyPartType var1, Integer var2, boolean var3) {
      HumanVisual var4 = ((IHumanVisual)this).getHumanVisual();
      if (var2 == null) {
         var2 = OutfitRNG.Next(5, 10);
      }

      boolean var5 = false;
      if (var1 == null) {
         var5 = true;
      }

      this.getItemVisuals(tempItemVisuals);

      for(int var6 = 0; var6 < var2; ++var6) {
         if (var5) {
            var1 = BloodBodyPartType.FromIndex(OutfitRNG.Next(0, BloodBodyPartType.MAX.index()));
         }

         BloodClothingType.addDirt(var1, var4, tempItemVisuals, var3);
      }

      this.bUpdateModelTextures = true;
      if (!GameServer.bServer && this instanceof IsoPlayer && ((IsoPlayer)this).isLocalPlayer()) {
         LuaEventManager.triggerEvent("OnClothingUpdated", this);
      }

   }

   public void addBlood(BloodBodyPartType var1, boolean var2, boolean var3, boolean var4) {
      HumanVisual var5 = ((IHumanVisual)this).getHumanVisual();
      int var6 = 1;
      boolean var7 = false;
      if (var1 == null) {
         var7 = true;
      }

      if (this.getPrimaryHandItem() instanceof HandWeapon) {
         var6 = ((HandWeapon)this.getPrimaryHandItem()).getSplatNumber();
         if (OutfitRNG.Next(15) < this.getWeaponLevel()) {
            --var6;
         }
      }

      if (var3) {
         var6 = 20;
      }

      if (var2) {
         var6 = 5;
      }

      if (this.isZombie()) {
         var6 += 8;
      }

      this.getItemVisuals(tempItemVisuals);

      for(int var8 = 0; var8 < var6; ++var8) {
         if (var7) {
            var1 = BloodBodyPartType.FromIndex(OutfitRNG.Next(0, BloodBodyPartType.MAX.index()));
            if (this.getPrimaryHandItem() != null && this.getPrimaryHandItem() instanceof HandWeapon) {
               HandWeapon var9 = (HandWeapon)this.getPrimaryHandItem();
               if (var9.getBloodLevel() < 1.0F) {
                  float var10 = var9.getBloodLevel() + 0.02F;
                  var9.setBloodLevel(var10);
                  this.bUpdateEquippedTextures = true;
               }
            }
         }

         BloodClothingType.addBlood(var1, var5, tempItemVisuals, var4);
      }

      this.bUpdateModelTextures = true;
      if (!GameServer.bServer && this instanceof IsoPlayer && ((IsoPlayer)this).isLocalPlayer()) {
         LuaEventManager.triggerEvent("OnClothingUpdated", this);
      }

   }

   public float getBodyPartClothingDefense(Integer var1, boolean var2, boolean var3) {
      float var4 = 0.0F;
      this.getItemVisuals(tempItemVisuals);

      for(int var5 = tempItemVisuals.size() - 1; var5 >= 0; --var5) {
         ItemVisual var6 = (ItemVisual)tempItemVisuals.get(var5);
         Item var7 = var6.getScriptItem();
         if (var7 != null) {
            ArrayList var8 = var7.getBloodClothingType();
            if (var8 != null) {
               ArrayList var9 = BloodClothingType.getCoveredParts(var8);
               if (var9 != null) {
                  InventoryItem var10 = var6.getInventoryItem();
                  if (var10 == null) {
                     var10 = InventoryItemFactory.CreateItem(var6.getItemType());
                     if (var10 == null) {
                        continue;
                     }
                  }

                  for(int var11 = 0; var11 < var9.size(); ++var11) {
                     if (var10 instanceof Clothing && ((BloodBodyPartType)var9.get(var11)).index() == var1 && var6.getHole((BloodBodyPartType)var9.get(var11)) == 0.0F) {
                        Clothing var12 = (Clothing)var10;
                        var4 += var12.getDefForPart((BloodBodyPartType)var9.get(var11), var2, var3);
                        break;
                     }
                  }
               }
            }
         }
      }

      var4 = Math.min(100.0F, var4);
      return var4;
   }

   public boolean isBumped() {
      return !StringUtils.isNullOrWhitespace(this.getBumpType());
   }

   public boolean isBumpDone() {
      return this.m_isBumpDone;
   }

   public void setBumpDone(boolean var1) {
      this.m_isBumpDone = var1;
   }

   public boolean isBumpFall() {
      return this.m_bumpFall;
   }

   public void setBumpFall(boolean var1) {
      this.m_bumpFall = var1;
   }

   public boolean isBumpStaggered() {
      return this.m_bumpStaggered;
   }

   public void setBumpStaggered(boolean var1) {
      this.m_bumpStaggered = var1;
   }

   public String getBumpType() {
      return this.bumpType;
   }

   public void setBumpType(String var1) {
      if (StringUtils.equalsIgnoreCase(this.bumpType, var1)) {
         this.bumpType = var1;
      } else {
         boolean var2 = this.isBumped();
         this.bumpType = var1;
         boolean var3 = this.isBumped();
         if (var3 != var2) {
            this.setBumpStaggered(var3);
         }

      }
   }

   public String getBumpFallType() {
      return this.m_bumpFallType;
   }

   public void setBumpFallType(String var1) {
      this.m_bumpFallType = var1;
   }

   public IsoGameCharacter getBumpedChr() {
      return this.bumpedChr;
   }

   public void setBumpedChr(IsoGameCharacter var1) {
      this.bumpedChr = var1;
   }

   public long getLastBump() {
      return this.lastBump;
   }

   public void setLastBump(long var1) {
      this.lastBump = var1;
   }

   public boolean isSitOnGround() {
      return this.sitOnGround;
   }

   public void setSitOnGround(boolean var1) {
      this.sitOnGround = var1;
   }

   public String getUID() {
      return this.m_UID;
   }

   protected HashMap getStateUpdateLookup() {
      return this.m_stateUpdateLookup;
   }

   public boolean isRunning() {
      return this.getMoodles() != null && this.getMoodles().getMoodleLevel(MoodleType.Endurance) >= 3 ? false : this.bRunning;
   }

   public void setRunning(boolean var1) {
      this.bRunning = var1;
   }

   public boolean isSprinting() {
      return this.bSprinting && !this.canSprint() ? false : this.bSprinting;
   }

   public void setSprinting(boolean var1) {
      this.bSprinting = var1;
   }

   public boolean canSprint() {
      if (this instanceof IsoPlayer && !((IsoPlayer)this).isAllowSprint()) {
         return false;
      } else if ("Tutorial".equals(Core.GameMode)) {
         return true;
      } else {
         InventoryItem var1 = this.getPrimaryHandItem();
         if (var1 != null && var1.isEquippedNoSprint()) {
            return false;
         } else {
            var1 = this.getSecondaryHandItem();
            if (var1 != null && var1.isEquippedNoSprint()) {
               return false;
            } else {
               return this.getMoodles() == null || this.getMoodles().getMoodleLevel(MoodleType.Endurance) < 2;
            }
         }
      }
   }

   public void postUpdateModelTextures() {
      this.bUpdateModelTextures = true;
   }

   public ModelInstanceTextureCreator getTextureCreator() {
      return this.textureCreator;
   }

   public void setTextureCreator(ModelInstanceTextureCreator var1) {
      this.textureCreator = var1;
   }

   public void postUpdateEquippedTextures() {
      this.bUpdateEquippedTextures = true;
   }

   public ArrayList getReadyModelData() {
      return this.readyModelData;
   }

   public boolean getIgnoreMovement() {
      return this.ignoreMovement;
   }

   public void setIgnoreMovement(boolean var1) {
      if (this instanceof IsoPlayer && var1) {
         ((IsoPlayer)this).networkAI.needToUpdate();
      }

      this.ignoreMovement = var1;
   }

   public boolean isSneaking() {
      return this.getVariableFloat("WalkInjury", 0.0F) > 0.5F ? false : this.bSneaking;
   }

   public void setSneaking(boolean var1) {
      this.bSneaking = var1;
   }

   public GameCharacterAIBrain getGameCharacterAIBrain() {
      return this.GameCharacterAIBrain;
   }

   public float getMoveDelta() {
      return this.m_moveDelta;
   }

   public void setMoveDelta(float var1) {
      this.m_moveDelta = var1;
   }

   public float getTurnDelta() {
      if (this.isSprinting()) {
         return this.m_turnDeltaSprinting;
      } else {
         return this.isRunning() ? this.m_turnDeltaRunning : this.m_turnDeltaNormal;
      }
   }

   public void setTurnDelta(float var1) {
      this.m_turnDeltaNormal = var1;
   }

   public float getChopTreeSpeed() {
      return (this.Traits.Axeman.isSet() ? 1.25F : 1.0F) * GameTime.getAnimSpeedFix();
   }

   public boolean testDefense(IsoZombie var1) {
      if (this.testDotSide(var1).equals("FRONT") && !var1.bCrawling && this.getSurroundingAttackingZombies() <= 3) {
         int var2 = 0;
         if ("KnifeDeath".equals(this.getVariableString("ZombieHitReaction"))) {
            var2 += 30;
         }

         var2 += this.getWeaponLevel() * 3;
         var2 += this.getPerkLevel(PerkFactory.Perks.Fitness) * 2;
         var2 += this.getPerkLevel(PerkFactory.Perks.Strength) * 2;
         var2 -= this.getSurroundingAttackingZombies() * 5;
         var2 -= this.getMoodles().getMoodleLevel(MoodleType.Endurance) * 2;
         var2 -= this.getMoodles().getMoodleLevel(MoodleType.HeavyLoad) * 2;
         var2 -= this.getMoodles().getMoodleLevel(MoodleType.Tired) * 3;
         if (SandboxOptions.instance.Lore.Strength.getValue() == 1) {
            var2 -= 7;
         }

         if (SandboxOptions.instance.Lore.Strength.getValue() == 3) {
            var2 += 7;
         }

         if (Rand.Next(100) < var2) {
            this.setAttackedBy(var1);
            this.setHitReaction(var1.getVariableString("PlayerHitReaction") + "Defended");
            return true;
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   public int getSurroundingAttackingZombies() {
      movingStatic.clear();
      IsoGridSquare var1 = this.getCurrentSquare();
      if (var1 == null) {
         return 0;
      } else {
         movingStatic.addAll(var1.getMovingObjects());
         if (var1.n != null) {
            movingStatic.addAll(var1.n.getMovingObjects());
         }

         if (var1.s != null) {
            movingStatic.addAll(var1.s.getMovingObjects());
         }

         if (var1.e != null) {
            movingStatic.addAll(var1.e.getMovingObjects());
         }

         if (var1.w != null) {
            movingStatic.addAll(var1.w.getMovingObjects());
         }

         if (var1.nw != null) {
            movingStatic.addAll(var1.nw.getMovingObjects());
         }

         if (var1.sw != null) {
            movingStatic.addAll(var1.sw.getMovingObjects());
         }

         if (var1.se != null) {
            movingStatic.addAll(var1.se.getMovingObjects());
         }

         if (var1.ne != null) {
            movingStatic.addAll(var1.ne.getMovingObjects());
         }

         int var2 = 0;

         for(int var3 = 0; var3 < movingStatic.size(); ++var3) {
            IsoZombie var4 = (IsoZombie)Type.tryCastTo((IsoMovingObject)movingStatic.get(var3), IsoZombie.class);
            if (var4 != null && var4.target == this && !(this.DistToSquared(var4) >= 0.80999994F) && (var4.isCurrentState(AttackState.instance()) || var4.isCurrentState(AttackNetworkState.instance()) || var4.isCurrentState(LungeState.instance()) || var4.isCurrentState(LungeNetworkState.instance()))) {
               ++var2;
            }
         }

         return var2;
      }
   }

   public float checkIsNearWall() {
      if (this.bSneaking && this.getCurrentSquare() != null) {
         IsoGridSquare var1 = this.getCurrentSquare().nav[IsoDirections.N.index()];
         IsoGridSquare var2 = this.getCurrentSquare().nav[IsoDirections.S.index()];
         IsoGridSquare var3 = this.getCurrentSquare().nav[IsoDirections.E.index()];
         IsoGridSquare var4 = this.getCurrentSquare().nav[IsoDirections.W.index()];
         float var5 = 0.0F;
         float var6 = 0.0F;
         if (var1 != null) {
            var5 = var1.getGridSneakModifier(true);
            if (var5 > 1.0F) {
               this.setVariable("nearWallCrouching", true);
               return var5;
            }
         }

         if (var2 != null) {
            var5 = var2.getGridSneakModifier(false);
            var6 = var2.getGridSneakModifier(true);
            if (var5 > 1.0F || var6 > 1.0F) {
               this.setVariable("nearWallCrouching", true);
               return var5 > 1.0F ? var5 : var6;
            }
         }

         if (var3 != null) {
            var5 = var3.getGridSneakModifier(false);
            var6 = var3.getGridSneakModifier(true);
            if (var5 > 1.0F || var6 > 1.0F) {
               this.setVariable("nearWallCrouching", true);
               return var5 > 1.0F ? var5 : var6;
            }
         }

         if (var4 != null) {
            var5 = var4.getGridSneakModifier(false);
            var6 = var4.getGridSneakModifier(true);
            if (var5 > 1.0F || var6 > 1.0F) {
               this.setVariable("nearWallCrouching", true);
               return var5 > 1.0F ? var5 : var6;
            }
         }

         var5 = this.getCurrentSquare().getGridSneakModifier(false);
         if (var5 > 1.0F) {
            this.setVariable("nearWallCrouching", true);
            return var5;
         } else if (this instanceof IsoPlayer && ((IsoPlayer)this).isNearVehicle()) {
            this.setVariable("nearWallCrouching", true);
            return 6.0F;
         } else {
            this.setVariable("nearWallCrouching", false);
            return 0.0F;
         }
      } else {
         this.setVariable("nearWallCrouching", false);
         return 0.0F;
      }
   }

   public float getBeenSprintingFor() {
      return this.BeenSprintingFor;
   }

   public void setBeenSprintingFor(float var1) {
      if (var1 < 0.0F) {
         var1 = 0.0F;
      }

      if (var1 > 100.0F) {
         var1 = 100.0F;
      }

      this.BeenSprintingFor = var1;
   }

   public boolean isHideWeaponModel() {
      return this.hideWeaponModel;
   }

   public void setHideWeaponModel(boolean var1) {
      if (this.hideWeaponModel != var1) {
         this.hideWeaponModel = var1;
         this.resetEquippedHandsModels();
      }

   }

   public void setIsAiming(boolean var1) {
      if (this.ignoreAimingInput) {
         var1 = false;
      }

      if (this instanceof IsoPlayer && !((IsoPlayer)this).isAttackAnimThrowTimeOut() || this.isAttackAnim() || this.getVariableBoolean("ShoveAnim")) {
         var1 = true;
      }

      this.isAiming = var1;
   }

   public boolean isAiming() {
      if (GameClient.bClient && this instanceof IsoPlayer && ((IsoPlayer)this).isLocalPlayer() && DebugOptions.instance.MultiplayerAttackPlayer.getValue()) {
         return false;
      } else {
         return this.isNPC ? this.NPCGetAiming() : this.isAiming;
      }
   }

   public void resetBeardGrowingTime() {
      this.beardGrowTiming = (float)this.getHoursSurvived();
      if (GameClient.bClient && this instanceof IsoPlayer) {
         GameClient.instance.sendVisual((IsoPlayer)this);
      }

   }

   public void resetHairGrowingTime() {
      this.hairGrowTiming = (float)this.getHoursSurvived();
      if (GameClient.bClient && this instanceof IsoPlayer) {
         GameClient.instance.sendVisual((IsoPlayer)this);
      }

   }

   public void fallenOnKnees() {
      if (!(this instanceof IsoPlayer) || ((IsoPlayer)this).isLocalPlayer()) {
         if (!this.isInvincible()) {
            this.helmetFall(false);
            BloodBodyPartType var1 = BloodBodyPartType.FromIndex(Rand.Next(BloodBodyPartType.Hand_L.index(), BloodBodyPartType.Torso_Upper.index()));
            if (Rand.NextBool(2)) {
               var1 = BloodBodyPartType.FromIndex(Rand.Next(BloodBodyPartType.UpperLeg_L.index(), BloodBodyPartType.Back.index()));
            }

            for(int var2 = 0; var2 < 4; ++var2) {
               BloodBodyPartType var3 = BloodBodyPartType.FromIndex(Rand.Next(BloodBodyPartType.Hand_L.index(), BloodBodyPartType.Torso_Upper.index()));
               if (Rand.NextBool(2)) {
                  var3 = BloodBodyPartType.FromIndex(Rand.Next(BloodBodyPartType.UpperLeg_L.index(), BloodBodyPartType.Back.index()));
               }

               this.addDirt(var3, Rand.Next(2, 6), false);
            }

            if (Rand.NextBool(2)) {
               if (Rand.NextBool(4)) {
                  this.dropHandItems();
               }

               this.addHole(var1);
               this.addBlood(var1, true, false, false);
               BodyPart var4 = this.getBodyDamage().getBodyPart(BodyPartType.FromIndex(var1.index()));
               if (var4.scratched()) {
                  var4.generateDeepWound();
               } else {
                  var4.setScratched(true, true);
               }

            }
         }
      }
   }

   public void addVisualDamage(String var1) {
      this.addBodyVisualFromItemType("Base." + var1);
   }

   protected ItemVisual addBodyVisualFromItemType(String var1) {
      Item var2 = ScriptManager.instance.getItem(var1);
      return var2 != null && !StringUtils.isNullOrWhitespace(var2.getClothingItem()) ? this.addBodyVisualFromClothingItemName(var2.getClothingItem()) : null;
   }

   protected ItemVisual addBodyVisualFromClothingItemName(String var1) {
      IHumanVisual var2 = (IHumanVisual)Type.tryCastTo(this, IHumanVisual.class);
      if (var2 == null) {
         return null;
      } else {
         String var3 = ScriptManager.instance.getItemTypeForClothingItem(var1);
         if (var3 == null) {
            return null;
         } else {
            Item var4 = ScriptManager.instance.getItem(var3);
            if (var4 == null) {
               return null;
            } else {
               ClothingItem var5 = var4.getClothingItemAsset();
               if (var5 == null) {
                  return null;
               } else {
                  ClothingItemReference var6 = new ClothingItemReference();
                  var6.itemGUID = var5.m_GUID;
                  var6.randomize();
                  ItemVisual var7 = new ItemVisual();
                  var7.setItemType(var3);
                  var7.synchWithOutfit(var6);
                  if (!this.isDuplicateBodyVisual(var7)) {
                     ItemVisuals var8 = var2.getHumanVisual().getBodyVisuals();
                     var8.add(var7);
                     return var7;
                  } else {
                     return null;
                  }
               }
            }
         }
      }
   }

   protected boolean isDuplicateBodyVisual(ItemVisual var1) {
      IHumanVisual var2 = (IHumanVisual)Type.tryCastTo(this, IHumanVisual.class);
      if (var2 == null) {
         return false;
      } else {
         ItemVisuals var3 = var2.getHumanVisual().getBodyVisuals();

         for(int var4 = 0; var4 < var3.size(); ++var4) {
            ItemVisual var5 = (ItemVisual)var3.get(var4);
            if (var1.getClothingItemName().equals(var5.getClothingItemName()) && var1.getTextureChoice() == var5.getTextureChoice() && var1.getBaseTexture() == var5.getBaseTexture()) {
               return true;
            }
         }

         return false;
      }
   }

   public boolean isCriticalHit() {
      return this.isCrit;
   }

   public void setCriticalHit(boolean var1) {
      this.isCrit = var1;
   }

   public float getRunSpeedModifier() {
      return this.runSpeedModifier;
   }

   public void startMuzzleFlash() {
      float var1 = GameTime.getInstance().getNight() * 0.8F;
      var1 = Math.max(var1, 0.2F);
      IsoLightSource var2 = new IsoLightSource((int)this.getX(), (int)this.getY(), (int)this.getZ(), 0.8F * var1, 0.8F * var1, 0.6F * var1, 18, 6);
      IsoWorld.instance.CurrentCell.getLamppostPositions().add(var2);
      this.m_muzzleFlash = System.currentTimeMillis();
   }

   public boolean isMuzzleFlash() {
      if (Core.bDebug && DebugOptions.instance.ModelRenderMuzzleflash.getValue()) {
         return true;
      } else {
         return this.m_muzzleFlash > System.currentTimeMillis() - 50L;
      }
   }

   public boolean isNPC() {
      return this.isNPC;
   }

   public void setNPC(boolean var1) {
      if (var1 && this.GameCharacterAIBrain == null) {
         this.GameCharacterAIBrain = new GameCharacterAIBrain(this);
      }

      this.isNPC = var1;
   }

   public void NPCSetRunning(boolean var1) {
      this.GameCharacterAIBrain.HumanControlVars.bRunning = var1;
   }

   public boolean NPCGetRunning() {
      return this.GameCharacterAIBrain.HumanControlVars.bRunning;
   }

   public void NPCSetJustMoved(boolean var1) {
      this.GameCharacterAIBrain.HumanControlVars.JustMoved = var1;
   }

   public void NPCSetAiming(boolean var1) {
      this.GameCharacterAIBrain.HumanControlVars.bAiming = var1;
   }

   public boolean NPCGetAiming() {
      return this.GameCharacterAIBrain.HumanControlVars.bAiming;
   }

   public void NPCSetAttack(boolean var1) {
      this.GameCharacterAIBrain.HumanControlVars.initiateAttack = var1;
   }

   public void NPCSetMelee(boolean var1) {
      this.GameCharacterAIBrain.HumanControlVars.bMelee = var1;
   }

   public void setMetabolicTarget(Metabolics var1) {
      if (var1 != null) {
         this.setMetabolicTarget(var1.getMet());
      }

   }

   public void setMetabolicTarget(float var1) {
      if (this.getBodyDamage() != null && this.getBodyDamage().getThermoregulator() != null) {
         this.getBodyDamage().getThermoregulator().setMetabolicTarget(var1);
      }

   }

   public double getThirstMultiplier() {
      return this.getBodyDamage() != null && this.getBodyDamage().getThermoregulator() != null ? this.getBodyDamage().getThermoregulator().getFluidsMultiplier() : 1.0D;
   }

   public double getHungerMultiplier() {
      return 1.0D;
   }

   public double getFatiqueMultiplier() {
      return this.getBodyDamage() != null && this.getBodyDamage().getThermoregulator() != null ? this.getBodyDamage().getThermoregulator().getFatigueMultiplier() : 1.0D;
   }

   public float getTimedActionTimeModifier() {
      return 1.0F;
   }

   public boolean addHoleFromZombieAttacks(BloodBodyPartType var1, boolean var2) {
      this.getItemVisuals(tempItemVisuals);
      ItemVisual var3 = null;

      for(int var4 = tempItemVisuals.size() - 1; var4 >= 0; --var4) {
         ItemVisual var5 = (ItemVisual)tempItemVisuals.get(var4);
         Item var6 = var5.getScriptItem();
         if (var6 != null) {
            ArrayList var7 = var6.getBloodClothingType();
            if (var7 != null) {
               ArrayList var8 = BloodClothingType.getCoveredParts(var7);

               for(int var9 = 0; var9 < var8.size(); ++var9) {
                  BloodBodyPartType var10 = (BloodBodyPartType)var8.get(var9);
                  if (var1 == var10) {
                     var3 = var5;
                     break;
                  }
               }

               if (var3 != null) {
                  break;
               }
            }
         }
      }

      float var11 = 0.0F;
      boolean var12 = false;
      if (var3 != null && var3.getInventoryItem() != null && var3.getInventoryItem() instanceof Clothing) {
         Clothing var13 = (Clothing)var3.getInventoryItem();
         var13.getPatchType(var1);
         var11 = Math.max(30.0F, 100.0F - var13.getDefForPart(var1, !var2, false) / 1.5F);
      }

      if ((float)Rand.Next(100) < var11) {
         this.addHole(var1);
         var12 = true;
      }

      return var12;
   }

   protected void updateBandages() {
      s_bandages.update(this);
   }

   public float getTotalBlood() {
      float var1 = 0.0F;
      if (this.getWornItems() == null) {
         return var1;
      } else {
         for(int var2 = 0; var2 < this.getWornItems().size(); ++var2) {
            InventoryItem var3 = this.getWornItems().get(var2).getItem();
            if (var3 instanceof Clothing) {
               var1 += ((Clothing)var3).getBloodlevel();
            }
         }

         var1 += ((HumanVisual)this.getVisual()).getTotalBlood();
         return var1;
      }
   }

   public void attackFromWindowsLunge(IsoZombie var1) {
      if (!(this.lungeFallTimer > 0.0F) && (int)this.getZ() == (int)var1.getZ() && !var1.isDead() && !this.getCurrentSquare().isDoorBlockedTo(var1.getCurrentSquare()) && !this.getCurrentSquare().isWallTo(var1.getCurrentSquare()) && !this.getCurrentSquare().isWindowTo(var1.getCurrentSquare())) {
         if (this.getVehicle() == null) {
            boolean var2 = this.DoSwingCollisionBoneCheck(var1, var1.getAnimationPlayer().getSkinningBoneIndex("Bip01_R_Hand", -1), 1.0F);
            if (var2) {
               this.lungeFallTimer = 200.0F;
               this.setIsAiming(false);
               boolean var3 = false;
               byte var4 = 30;
               int var6 = var4 + this.getMoodles().getMoodleLevel(MoodleType.Endurance) * 3;
               var6 += this.getMoodles().getMoodleLevel(MoodleType.HeavyLoad) * 5;
               var6 -= this.getPerkLevel(PerkFactory.Perks.Fitness) * 2;
               BodyPart var5 = this.getBodyDamage().getBodyPart(BodyPartType.Torso_Lower);
               if (var5.getAdditionalPain(true) > 20.0F) {
                  var6 = (int)((float)var6 + (var5.getAdditionalPain(true) - 20.0F) / 10.0F);
               }

               if (this.Traits.Clumsy.isSet()) {
                  var6 += 10;
               }

               if (this.Traits.Graceful.isSet()) {
                  var6 -= 10;
               }

               if (this.Traits.VeryUnderweight.isSet()) {
                  var6 += 20;
               }

               if (this.Traits.Underweight.isSet()) {
                  var6 += 10;
               }

               if (this.Traits.Obese.isSet()) {
                  var6 -= 10;
               }

               if (this.Traits.Overweight.isSet()) {
                  var6 -= 5;
               }

               var6 = Math.max(5, var6);
               this.clearVariable("BumpFallType");
               this.setBumpType("stagger");
               if (Rand.Next(100) < var6) {
                  var3 = true;
               }

               this.setBumpDone(false);
               this.setBumpFall(var3);
               if (var1.isBehind(this)) {
                  this.setBumpFallType("pushedBehind");
               } else {
                  this.setBumpFallType("pushedFront");
               }

               this.actionContext.reportEvent("wasBumped");
            }
         }
      }
   }

   public boolean DoSwingCollisionBoneCheck(IsoGameCharacter var1, int var2, float var3) {
      Model.BoneToWorldCoords(var1, var2, tempVectorBonePos);
      float var4 = IsoUtils.DistanceToSquared(tempVectorBonePos.x, tempVectorBonePos.y, this.x, this.y);
      return var4 < var3 * var3;
   }

   public boolean isInvincible() {
      return this.invincible;
   }

   public void setInvincible(boolean var1) {
      this.invincible = var1;
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
                     if ((int)this.getZ() == (int)var9.getZ() && (!(this instanceof IsoPlayer) || !((IsoPlayer)this).isLocalPlayer() || var9.getTargetAlpha(((IsoPlayer)this).PlayerIndex) != 0.0F) && !(this.DistToSquared((float)((int)var9.x), (float)((int)var9.y)) >= 16.0F)) {
                        return var9;
                     }
                  }
               }
            }
         }

         return null;
      }
   }

   private IsoGridSquare getSolidFloorAt(int var1, int var2, int var3) {
      while(var3 >= 0) {
         IsoGridSquare var4 = this.getCell().getGridSquare(var1, var2, var3);
         if (var4 != null && var4.TreatAsSolidFloor()) {
            return var4;
         }

         --var3;
      }

      return null;
   }

   public void dropHeavyItems() {
      IsoGridSquare var1 = this.getCurrentSquare();
      if (var1 != null) {
         InventoryItem var2 = this.getPrimaryHandItem();
         InventoryItem var3 = this.getSecondaryHandItem();
         if (var2 != null || var3 != null) {
            var1 = this.getSolidFloorAt(var1.x, var1.y, var1.z);
            if (var1 != null) {
               boolean var4 = var2 == var3;
               float var5;
               float var6;
               float var7;
               if (this.isHeavyItem(var2)) {
                  var5 = Rand.Next(0.1F, 0.9F);
                  var6 = Rand.Next(0.1F, 0.9F);
                  var7 = var1.getApparentZ(var5, var6) - (float)var1.getZ();
                  this.setPrimaryHandItem((InventoryItem)null);
                  this.getInventory().DoRemoveItem(var2);
                  var1.AddWorldInventoryItem(var2, var5, var6, var7);
                  LuaEventManager.triggerEvent("OnContainerUpdate");
                  LuaEventManager.triggerEvent("onItemFall", var2);
               }

               if (this.isHeavyItem(var3)) {
                  this.setSecondaryHandItem((InventoryItem)null);
                  if (!var4) {
                     var5 = Rand.Next(0.1F, 0.9F);
                     var6 = Rand.Next(0.1F, 0.9F);
                     var7 = var1.getApparentZ(var5, var6) - (float)var1.getZ();
                     this.getInventory().DoRemoveItem(var3);
                     var1.AddWorldInventoryItem(var3, var5, var6, var7);
                     LuaEventManager.triggerEvent("OnContainerUpdate");
                     LuaEventManager.triggerEvent("onItemFall", var3);
                  }
               }

            }
         }
      }
   }

   public boolean isHeavyItem(InventoryItem var1) {
      if (var1 == null) {
         return false;
      } else if (var1.getItemReplacementSecondHand() != null) {
         return true;
      } else {
         return !var1.getType().equals("CorpseMale") && !var1.getType().equals("CorpseFemale") ? var1.getType().equals("Generator") : true;
      }
   }

   public boolean isCanShout() {
      return this.canShout;
   }

   public void setCanShout(boolean var1) {
      this.canShout = var1;
   }

   public boolean isUnlimitedEndurance() {
      return this.unlimitedEndurance;
   }

   public void setUnlimitedEndurance(boolean var1) {
      this.unlimitedEndurance = var1;
   }

   private void addActiveLightItem(InventoryItem var1, ArrayList var2) {
      if (var1 != null && var1.isEmittingLight() && !var2.contains(var1)) {
         var2.add(var1);
      }

   }

   public ArrayList getActiveLightItems(ArrayList var1) {
      this.addActiveLightItem(this.getSecondaryHandItem(), var1);
      this.addActiveLightItem(this.getPrimaryHandItem(), var1);
      AttachedItems var2 = this.getAttachedItems();

      for(int var3 = 0; var3 < var2.size(); ++var3) {
         InventoryItem var4 = var2.getItemByIndex(var3);
         this.addActiveLightItem(var4, var1);
      }

      return var1;
   }

   public SleepingEventData getOrCreateSleepingEventData() {
      if (this.m_sleepingEventData == null) {
         this.m_sleepingEventData = new SleepingEventData();
      }

      return this.m_sleepingEventData;
   }

   public void playEmote(String var1) {
      this.setVariable("emote", var1);
      this.actionContext.reportEvent("EventEmote");
   }

   public String getAnimationStateName() {
      return this.advancedAnimator.getCurrentStateName();
   }

   public String getActionStateName() {
      return this.actionContext.getCurrentStateName();
   }

   public boolean shouldWaitToStartTimedAction() {
      if (this.isSitOnGround()) {
         AdvancedAnimator var1 = this.getAdvancedAnimator();
         if (var1.getRootLayer() == null) {
            return false;
         } else if (var1.animSet != null && var1.animSet.containsState("sitonground")) {
            AnimState var2 = var1.animSet.GetState("sitonground");
            if (!PZArrayUtil.contains(var2.m_Nodes, (var0) -> {
               return "sit_action".equalsIgnoreCase(var0.m_Name);
            })) {
               return false;
            } else {
               LiveAnimNode var3 = (LiveAnimNode)PZArrayUtil.find(var1.getRootLayer().getLiveAnimNodes(), (var0) -> {
                  return var0.isActive() && "sit_action".equalsIgnoreCase(var0.getName());
               });
               return var3 == null || !var3.isMainAnimActive();
            }
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   public void setPersistentOutfitID(int var1) {
      this.setPersistentOutfitID(var1, false);
   }

   public void setPersistentOutfitID(int var1, boolean var2) {
      this.m_persistentOutfitId = var1;
      this.m_bPersistentOutfitInit = var2;
   }

   public int getPersistentOutfitID() {
      return this.m_persistentOutfitId;
   }

   public boolean isPersistentOutfitInit() {
      return this.m_bPersistentOutfitInit;
   }

   public boolean isDoingActionThatCanBeCancelled() {
      return false;
   }

   public boolean isDoDeathSound() {
      return this.doDeathSound;
   }

   public void setDoDeathSound(boolean var1) {
      this.doDeathSound = var1;
   }

   public void updateEquippedRadioFreq() {
      this.invRadioFreq.clear();

      int var1;
      for(var1 = 0; var1 < this.getInventory().getItems().size(); ++var1) {
         InventoryItem var2 = (InventoryItem)this.getInventory().getItems().get(var1);
         if (var2 instanceof Radio) {
            Radio var3 = (Radio)var2;
            if (var3.getDeviceData() != null && var3.getDeviceData().getIsTurnedOn() && !var3.getDeviceData().getMicIsMuted() && !this.invRadioFreq.contains(var3.getDeviceData().getChannel())) {
               this.invRadioFreq.add(var3.getDeviceData().getChannel());
            }
         }
      }

      for(var1 = 0; var1 < this.invRadioFreq.size(); ++var1) {
         System.out.println(this.invRadioFreq.get(var1));
      }

      if (this instanceof IsoPlayer && GameClient.bClient) {
         GameClient.sendEquippedRadioFreq((IsoPlayer)this);
      }

   }

   public FMODParameterList getFMODParameters() {
      return this.fmodParameters;
   }

   public void startEvent(long var1, GameSoundClip var3, BitSet var4) {
      FMODParameterList var5 = this.getFMODParameters();
      ArrayList var6 = var3.eventDescription.parameters;

      for(int var7 = 0; var7 < var6.size(); ++var7) {
         FMOD_STUDIO_PARAMETER_DESCRIPTION var8 = (FMOD_STUDIO_PARAMETER_DESCRIPTION)var6.get(var7);
         if (!var4.get(var8.globalIndex)) {
            FMODParameter var9 = var5.get(var8);
            if (var9 != null) {
               var9.startEventInstance(var1);
            }
         }
      }

   }

   public void updateEvent(long var1, GameSoundClip var3) {
   }

   public void stopEvent(long var1, GameSoundClip var3, BitSet var4) {
      FMODParameterList var5 = this.getFMODParameters();
      ArrayList var6 = var3.eventDescription.parameters;

      for(int var7 = 0; var7 < var6.size(); ++var7) {
         FMOD_STUDIO_PARAMETER_DESCRIPTION var8 = (FMOD_STUDIO_PARAMETER_DESCRIPTION)var6.get(var7);
         if (!var4.get(var8.globalIndex)) {
            FMODParameter var9 = var5.get(var8);
            if (var9 != null) {
               var9.stopEventInstance(var1);
            }
         }
      }

   }

   public void playBloodSplatterSound() {
      if (this.getEmitter().isPlaying("BloodSplatter")) {
      }

      this.getEmitter().playSoundImpl("BloodSplatter", this);
   }

   public void setIgnoreAimingInput(boolean var1) {
      this.ignoreAimingInput = var1;
   }

   public void addBlood(float var1) {
      if (!((float)Rand.Next(10) > var1)) {
         if (SandboxOptions.instance.BloodLevel.getValue() > 1) {
            int var2 = Rand.Next(4, 10);
            if (var2 < 1) {
               var2 = 1;
            }

            if (Core.bLastStand) {
               var2 *= 3;
            }

            switch(SandboxOptions.instance.BloodLevel.getValue()) {
            case 2:
               var2 /= 2;
            case 3:
            default:
               break;
            case 4:
               var2 *= 2;
               break;
            case 5:
               var2 *= 5;
            }

            for(int var3 = 0; var3 < var2; ++var3) {
               this.splatBlood(2, 0.3F);
            }
         }

         if (SandboxOptions.instance.BloodLevel.getValue() > 1) {
            this.splatBloodFloorBig();
            this.playBloodSplatterSound();
         }

      }
   }

   public boolean isKnockedDown() {
      return this.bKnockedDown;
   }

   public void setKnockedDown(boolean var1) {
      this.bKnockedDown = var1;
   }

   public void writeInventory(ByteBuffer var1) {
      String var2 = this.isFemale() ? "inventoryfemale" : "inventorymale";
      GameWindow.WriteString(var1, var2);
      if (this.getInventory() != null) {
         var1.put((byte)1);

         try {
            ArrayList var3 = this.getInventory().save(var1);
            WornItems var5 = this.getWornItems();
            int var6;
            if (var5 == null) {
               byte var11 = 0;
               var1.put((byte)var11);
            } else {
               int var4 = var5.size();
               if (var4 > 127) {
                  DebugLog.Multiplayer.warn("Too many worn items");
                  var4 = 127;
               }

               var1.put((byte)var4);

               for(var6 = 0; var6 < var4; ++var6) {
                  WornItem var7 = var5.get(var6);
                  GameWindow.WriteString(var1, var7.getLocation());
                  var1.putShort((short)var3.indexOf(var7.getItem()));
               }
            }

            AttachedItems var13 = this.getAttachedItems();
            if (var13 == null) {
               boolean var12 = false;
               var1.put((byte)0);
            } else {
               var6 = var13.size();
               if (var6 > 127) {
                  DebugLog.Multiplayer.warn("Too many attached items");
                  var6 = 127;
               }

               var1.put((byte)var6);

               for(int var8 = 0; var8 < var6; ++var8) {
                  AttachedItem var9 = var13.get(var8);
                  GameWindow.WriteString(var1, var9.getLocation());
                  var1.putShort((short)var3.indexOf(var9.getItem()));
               }
            }
         } catch (IOException var10) {
            DebugLog.Multiplayer.printException(var10, "WriteInventory error for character " + this.getOnlineID(), LogSeverity.Error);
         }
      } else {
         var1.put((byte)0);
      }

   }

   public String readInventory(ByteBuffer var1) {
      String var2 = GameWindow.ReadString(var1);
      boolean var3 = var1.get() == 1;
      if (var3) {
         try {
            ArrayList var4 = this.getInventory().load(var1, IsoWorld.getWorldVersion());
            byte var5 = var1.get();

            for(int var6 = 0; var6 < var5; ++var6) {
               String var7 = GameWindow.ReadStringUTF(var1);
               short var8 = var1.getShort();
               if (var8 >= 0 && var8 < var4.size() && this.getBodyLocationGroup().getLocation(var7) != null) {
                  this.getWornItems().setItem(var7, (InventoryItem)var4.get(var8));
               }
            }

            byte var11 = var1.get();

            for(int var12 = 0; var12 < var11; ++var12) {
               String var13 = GameWindow.ReadStringUTF(var1);
               short var9 = var1.getShort();
               if (var9 >= 0 && var9 < var4.size() && this.getAttachedLocationGroup().getLocation(var13) != null) {
                  this.getAttachedItems().setItem(var13, (InventoryItem)var4.get(var9));
               }
            }
         } catch (IOException var10) {
            DebugLog.Multiplayer.printException(var10, "ReadInventory error for character " + this.getOnlineID(), LogSeverity.Error);
         }
      }

      return var2;
   }

   public void Kill(IsoGameCharacter var1) {
      if (Core.bDebug) {
         DebugLog.log(DebugType.Death, String.format("%s.Kill id=%d", this.getClass().getSimpleName(), this.getOnlineID()));
      }

      this.setAttackedBy(var1);
      this.setHealth(0.0F);
      this.setOnKillDone(true);
   }

   public boolean shouldDoInventory() {
      return true;
   }

   public void becomeCorpse() {
      if (Core.bDebug) {
         DebugLog.log(DebugType.Death, String.format("%s.BecomeCorpse id=%d", this.getClass().getSimpleName(), this.getOnlineID()));
      }

      this.Kill(this.getAttackedBy());
      this.setOnDeathDone(true);
   }

   public boolean shouldBecomeCorpse() {
      if (GameClient.bClient || GameServer.bServer) {
         if (this.getHitReactionNetworkAI().isSetup() || this.getHitReactionNetworkAI().isStarted()) {
            return false;
         }

         if (GameServer.bServer) {
            return this.getNetworkCharacterAI().isSetDeadBody();
         }

         if (GameClient.bClient) {
            return this.isCurrentState(ZombieOnGroundState.instance()) || this.isCurrentState(PlayerOnGroundState.instance());
         }
      }

      return true;
   }

   public HitReactionNetworkAI getHitReactionNetworkAI() {
      return null;
   }

   public NetworkCharacterAI getNetworkCharacterAI() {
      return null;
   }

   public boolean isLocal() {
      return !GameClient.bClient && !GameServer.bServer;
   }

   public boolean isVehicleCollisionActive(BaseVehicle var1) {
      if (!GameClient.bClient) {
         return false;
      } else if (!this.isAlive()) {
         return false;
      } else if (var1 == null) {
         return false;
      } else if (!var1.shouldCollideWithCharacters()) {
         return false;
      } else if (!var1.isEngineRunning()) {
         return false;
      } else if (var1.getDriver() == null) {
         return false;
      } else if (!(Math.abs(var1.x - this.x) < 0.01F) && !(Math.abs(var1.y - this.y) < 0.01F)) {
         return (!this.isKnockedDown() || this.isOnFloor()) && (this.getHitReactionNetworkAI() == null || !this.getHitReactionNetworkAI().isStarted());
      } else {
         return false;
      }
   }

   public void doHitByVehicle(BaseVehicle var1, BaseVehicle.HitVars var2) {
      if (GameClient.bClient) {
         if (var1.getDriver() instanceof IsoPlayer) {
            if (var1.getDriver().isLocal()) {
               SoundManager.instance.PlayWorldSound("VehicleHitCharacter", this.getCurrentSquare(), 0.0F, 20.0F, 0.9F, true);
               float var3 = this.Hit(var1, var2.hitSpeed, var2.isTargetHitFromBehind, -var2.targetImpulse.x, -var2.targetImpulse.z);
               GameClient.sendHitVehicle((IsoPlayer)var1.getDriver(), this, var1, var3, var2.isTargetHitFromBehind, var2.vehicleDamage, var2.hitSpeed, var2.isVehicleHitFromFront);
            } else {
               this.getNetworkCharacterAI().resetVehicleHitTimeout();
            }
         }
      } else if (!GameServer.bServer) {
         BaseSoundEmitter var6 = IsoWorld.instance.getFreeEmitter(this.x, this.y, this.z);
         long var4 = var6.playSound("VehicleHitCharacter");
         var6.setParameterValue(var4, FMODManager.instance.getParameterDescription("VehicleSpeed"), var1.getCurrentSpeedKmHour());
         this.Hit(var1, var2.hitSpeed, var2.isTargetHitFromBehind, -var2.targetImpulse.x, -var2.targetImpulse.z);
      }

   }

   public boolean isSkipResolveCollision() {
      return this instanceof IsoZombie && (this.isCurrentState(ZombieHitReactionState.instance()) || this.isCurrentState(ZombieFallDownState.instance()) || this.isCurrentState(ZombieOnGroundState.instance()) || this.isCurrentState(StaggerBackState.instance())) || this instanceof IsoPlayer && !this.isLocal() && (this.isCurrentState(PlayerFallDownState.instance()) || this.isCurrentState(BumpedState.instance()) || this.isCurrentState(PlayerKnockedDown.instance()) || this.isCurrentState(PlayerHitReactionState.instance()) || this.isCurrentState(PlayerHitReactionPVPState.instance()) || this.isCurrentState(PlayerOnGroundState.instance()));
   }

   public boolean isAttackAnim() {
      return this.attackAnim;
   }

   public void setAttackAnim(boolean var1) {
      this.attackAnim = var1;
   }

   public Float calcHitDir(IsoGameCharacter var1, HandWeapon var2, Vector2 var3) {
      var3.x = this.getX();
      var3.y = this.getY();
      var3.x -= var1.getX();
      var3.y -= var1.getY();
      var3.normalize();
      var3.x *= var2.getPushBackMod();
      var3.y *= var2.getPushBackMod();
      var3.rotate(var2.HitAngleMod);
      var3.setLength(this.getHitForce() * 0.1F);
      return null;
   }

   public void calcHitDir(Vector2 var1) {
      var1.set(this.getHitDir());
      var1.setLength(this.getHitForce());
   }

   public class XP {
      public int level = 0;
      public int lastlevel = 0;
      public float TotalXP = 0.0F;
      public HashMap XPMap = new HashMap();
      public HashMap XPMapMultiplier = new HashMap();
      IsoGameCharacter chr = null;

      public XP(IsoGameCharacter var2) {
         this.chr = var2;
      }

      public void addXpMultiplier(PerkFactory.Perk var1, float var2, int var3, int var4) {
         IsoGameCharacter.XPMultiplier var5 = (IsoGameCharacter.XPMultiplier)this.XPMapMultiplier.get(var1);
         if (var5 == null) {
            var5 = new IsoGameCharacter.XPMultiplier();
         }

         var5.multiplier = var2;
         var5.minLevel = var3;
         var5.maxLevel = var4;
         this.XPMapMultiplier.put(var1, var5);
      }

      public HashMap getMultiplierMap() {
         return this.XPMapMultiplier;
      }

      public float getMultiplier(PerkFactory.Perk var1) {
         IsoGameCharacter.XPMultiplier var2 = (IsoGameCharacter.XPMultiplier)this.XPMapMultiplier.get(var1);
         return var2 == null ? 0.0F : var2.multiplier;
      }

      public int getPerkBoost(PerkFactory.Perk var1) {
         return IsoGameCharacter.this.getDescriptor().getXPBoostMap().get(var1) != null ? (Integer)IsoGameCharacter.this.getDescriptor().getXPBoostMap().get(var1) : 0;
      }

      public int getLevel() {
         return this.level;
      }

      public void setLevel(int var1) {
         this.level = var1;
      }

      public float getTotalXp() {
         return this.TotalXP;
      }

      public void AddXP(PerkFactory.Perk var1, float var2) {
         this.AddXP(var1, var2, true);
      }

      public void AddXPNoMultiplier(PerkFactory.Perk var1, float var2) {
         IsoGameCharacter.XPMultiplier var3 = (IsoGameCharacter.XPMultiplier)this.getMultiplierMap().remove(var1);

         try {
            this.AddXP(var1, var2);
         } finally {
            if (var3 != null) {
               this.getMultiplierMap().put(var1, var3);
            }

         }

      }

      public void AddXP(PerkFactory.Perk var1, float var2, boolean var3) {
         this.AddXP(var1, var2, var3, true);
      }

      public void AddXP(PerkFactory.Perk var1, float var2, boolean var3, boolean var4) {
         this.AddXP(var1, var2, var3, var4, true, false);
      }

      public void AddXP(PerkFactory.Perk var1, float var2, boolean var3, boolean var4, boolean var5, boolean var6) {
         if (!var6 && GameClient.bClient && this.chr instanceof IsoPlayer) {
            GameClient.instance.sendAddXpFromPlayerStatsUI((IsoPlayer)this.chr, var1, (int)var2, var4, false);
         }

         PerkFactory.Perk var7 = null;

         for(int var8 = 0; var8 < PerkFactory.PerkList.size(); ++var8) {
            PerkFactory.Perk var9 = (PerkFactory.Perk)PerkFactory.PerkList.get(var8);
            if (var9.getType() == var1) {
               var7 = var9;
               break;
            }
         }

         if (var7.getType() != PerkFactory.Perks.Fitness || !(this.chr instanceof IsoPlayer) || ((IsoPlayer)this.chr).getNutrition().canAddFitnessXp()) {
            if (var7.getType() == PerkFactory.Perks.Strength && this.chr instanceof IsoPlayer) {
               if (((IsoPlayer)this.chr).getNutrition().getProteins() > 50.0F && ((IsoPlayer)this.chr).getNutrition().getProteins() < 300.0F) {
                  var2 = (float)((double)var2 * 1.5D);
               }

               if (((IsoPlayer)this.chr).getNutrition().getProteins() < -300.0F) {
                  var2 = (float)((double)var2 * 0.7D);
               }
            }

            float var16 = this.getXP(var1);
            float var17 = var7.getTotalXpForLevel(10);
            if (!(var2 >= 0.0F) || !(var16 >= var17)) {
               float var10 = 1.0F;
               float var19;
               if (var5) {
                  boolean var11 = false;
                  Iterator var12 = IsoGameCharacter.this.getDescriptor().getXPBoostMap().entrySet().iterator();

                  label184:
                  while(true) {
                     while(true) {
                        Entry var13;
                        do {
                           if (!var12.hasNext()) {
                              if (!var11 && !this.isSkillExcludedFromSpeedReduction(var7.getType())) {
                                 var10 = 0.25F;
                              }

                              if (IsoGameCharacter.this.Traits.FastLearner.isSet() && !this.isSkillExcludedFromSpeedIncrease(var7.getType())) {
                                 var10 *= 1.3F;
                              }

                              if (IsoGameCharacter.this.Traits.SlowLearner.isSet() && !this.isSkillExcludedFromSpeedReduction(var7.getType())) {
                                 var10 *= 0.7F;
                              }

                              if (IsoGameCharacter.this.Traits.Pacifist.isSet()) {
                                 if (var7.getType() != PerkFactory.Perks.SmallBlade && var7.getType() != PerkFactory.Perks.SmallBlunt && var7.getType() != PerkFactory.Perks.Spear && var7.getType() != PerkFactory.Perks.Maintenance && var7.getType() != PerkFactory.Perks.Blunt && var7.getType() != PerkFactory.Perks.Axe) {
                                    if (var7.getType() == PerkFactory.Perks.Aiming) {
                                       var10 *= 0.75F;
                                    }
                                 } else {
                                    var10 *= 0.75F;
                                 }
                              }

                              var2 *= var10;
                              var19 = this.getMultiplier(var1);
                              if (var19 > 1.0F) {
                                 var2 *= var19;
                              }

                              if (!var7.isPassiv()) {
                                 var2 = (float)((double)var2 * SandboxOptions.instance.XpMultiplier.getValue());
                              }
                              break label184;
                           }

                           var13 = (Entry)var12.next();
                        } while(var13.getKey() != var7.getType());

                        var11 = true;
                        if ((Integer)var13.getValue() == 0 && !this.isSkillExcludedFromSpeedReduction((PerkFactory.Perk)var13.getKey())) {
                           var10 *= 0.25F;
                        } else if ((Integer)var13.getValue() == 1 && var13.getKey() == PerkFactory.Perks.Sprinting) {
                           var10 = (float)((double)var10 * 1.25D);
                        } else if ((Integer)var13.getValue() == 1) {
                           var10 = (float)((double)var10 * 1.0D);
                        } else if ((Integer)var13.getValue() == 2 && !this.isSkillExcludedFromSpeedIncrease((PerkFactory.Perk)var13.getKey())) {
                           var10 = (float)((double)var10 * 1.33D);
                        } else if ((Integer)var13.getValue() >= 3 && !this.isSkillExcludedFromSpeedIncrease((PerkFactory.Perk)var13.getKey())) {
                           var10 = (float)((double)var10 * 1.66D);
                        }
                     }
                  }
               }

               float var18 = var16 + var2;
               if (var18 < 0.0F) {
                  var18 = 0.0F;
                  var2 = -var16;
               }

               if (var18 > var17) {
                  var18 = var17;
                  var2 = var17 - var16;
               }

               this.XPMap.put(var1, var18);

               for(var19 = var7.getTotalXpForLevel(this.chr.getPerkLevel(var7) + 1); var16 < var19 && var18 >= var19; var19 = var7.getTotalXpForLevel(this.chr.getPerkLevel(var7) + 1)) {
                  IsoGameCharacter.this.LevelPerk(var1);
                  if (this.chr instanceof IsoPlayer && ((IsoPlayer)this.chr).isLocalPlayer() && !this.chr.getEmitter().isPlaying("GainExperienceLevel")) {
                     this.chr.getEmitter().playSoundImpl("GainExperienceLevel", (IsoObject)null);
                  }

                  if (this.chr.getPerkLevel(var7) >= 10) {
                     break;
                  }
               }

               IsoGameCharacter.XPMultiplier var20 = (IsoGameCharacter.XPMultiplier)this.getMultiplierMap().get(var7);
               if (var20 != null) {
                  float var14 = var7.getTotalXpForLevel(var20.minLevel - 1);
                  float var15 = var7.getTotalXpForLevel(var20.maxLevel);
                  if (var16 >= var14 && var18 < var14 || var16 < var15 && var18 >= var15) {
                     this.getMultiplierMap().remove(var7);
                  }
               }

               if (var3) {
                  LuaEventManager.triggerEventGarbage("AddXP", this.chr, var1, var2);
               }

            }
         }
      }

      private boolean isSkillExcludedFromSpeedReduction(PerkFactory.Perk var1) {
         if (var1 == PerkFactory.Perks.Sprinting) {
            return true;
         } else if (var1 == PerkFactory.Perks.Fitness) {
            return true;
         } else {
            return var1 == PerkFactory.Perks.Strength;
         }
      }

      private boolean isSkillExcludedFromSpeedIncrease(PerkFactory.Perk var1) {
         if (var1 == PerkFactory.Perks.Fitness) {
            return true;
         } else {
            return var1 == PerkFactory.Perks.Strength;
         }
      }

      public float getXP(PerkFactory.Perk var1) {
         return this.XPMap.containsKey(var1) ? (Float)this.XPMap.get(var1) : 0.0F;
      }

      public void AddXP(HandWeapon var1, int var2) {
      }

      public void setTotalXP(float var1) {
         this.TotalXP = var1;
      }

      private void savePerk(ByteBuffer var1, PerkFactory.Perk var2) throws IOException {
         GameWindow.WriteStringUTF(var1, var2 == null ? "" : var2.getId());
      }

      private PerkFactory.Perk loadPerk(ByteBuffer var1, int var2) throws IOException {
         PerkFactory.Perk var4;
         if (var2 >= 152) {
            String var5 = GameWindow.ReadStringUTF(var1);
            var4 = PerkFactory.Perks.FromString(var5);
            return var4 == PerkFactory.Perks.MAX ? null : var4;
         } else {
            int var3 = var1.getInt();
            if (var3 >= 0 && var3 < PerkFactory.Perks.MAX.index()) {
               var4 = PerkFactory.Perks.fromIndex(var3);
               return var4 == PerkFactory.Perks.MAX ? null : var4;
            } else {
               return null;
            }
         }
      }

      public void load(ByteBuffer var1, int var2) throws IOException {
         int var3 = var1.getInt();
         this.chr.Traits.clear();

         int var4;
         for(var4 = 0; var4 < var3; ++var4) {
            String var5 = GameWindow.ReadString(var1);
            if (TraitFactory.getTrait(var5) != null) {
               if (!this.chr.Traits.contains(var5)) {
                  this.chr.Traits.add(var5);
               }
            } else {
               DebugLog.General.error("unknown trait \"" + var5 + "\"");
            }
         }

         this.TotalXP = var1.getFloat();
         this.level = var1.getInt();
         this.lastlevel = var1.getInt();
         this.XPMap.clear();
         var4 = var1.getInt();

         int var12;
         for(var12 = 0; var12 < var4; ++var12) {
            PerkFactory.Perk var6 = this.loadPerk(var1, var2);
            float var7 = var1.getFloat();
            if (var6 != null) {
               this.XPMap.put(var6, var7);
            }
         }

         int var13;
         if (var2 < 162) {
            var12 = var1.getInt();

            for(var13 = 0; var13 < var12; ++var13) {
               this.loadPerk(var1, var2);
            }
         }

         IsoGameCharacter.this.PerkList.clear();
         var12 = var1.getInt();

         for(var13 = 0; var13 < var12; ++var13) {
            PerkFactory.Perk var14 = this.loadPerk(var1, var2);
            int var8 = var1.getInt();
            if (var14 != null) {
               IsoGameCharacter.PerkInfo var9 = IsoGameCharacter.this.new PerkInfo();
               var9.perk = var14;
               var9.level = var8;
               IsoGameCharacter.this.PerkList.add(var9);
            }
         }

         var13 = var1.getInt();

         for(int var15 = 0; var15 < var13; ++var15) {
            PerkFactory.Perk var16 = this.loadPerk(var1, var2);
            float var17 = var1.getFloat();
            byte var10 = var1.get();
            byte var11 = var1.get();
            if (var16 != null) {
               this.addXpMultiplier(var16, var17, var10, var11);
            }
         }

         if (this.TotalXP > (float)IsoGameCharacter.this.getXpForLevel(this.getLevel() + 1)) {
            this.setTotalXP((float)this.chr.getXpForLevel(this.getLevel()));
         }

      }

      public void save(ByteBuffer var1) throws IOException {
         var1.putInt(this.chr.Traits.size());

         for(int var2 = 0; var2 < this.chr.Traits.size(); ++var2) {
            GameWindow.WriteString(var1, this.chr.Traits.get(var2));
         }

         var1.putFloat(this.TotalXP);
         var1.putInt(this.level);
         var1.putInt(this.lastlevel);
         var1.putInt(this.XPMap.size());
         Iterator var5 = this.XPMap.entrySet().iterator();

         while(var5 != null && var5.hasNext()) {
            Entry var3 = (Entry)var5.next();
            this.savePerk(var1, (PerkFactory.Perk)var3.getKey());
            var1.putFloat((Float)var3.getValue());
         }

         var1.putInt(IsoGameCharacter.this.PerkList.size());

         for(int var6 = 0; var6 < IsoGameCharacter.this.PerkList.size(); ++var6) {
            IsoGameCharacter.PerkInfo var4 = (IsoGameCharacter.PerkInfo)IsoGameCharacter.this.PerkList.get(var6);
            this.savePerk(var1, var4.perk);
            var1.putInt(var4.level);
         }

         var1.putInt(this.XPMapMultiplier.size());
         Iterator var7 = this.XPMapMultiplier.entrySet().iterator();

         while(var7 != null && var7.hasNext()) {
            Entry var8 = (Entry)var7.next();
            this.savePerk(var1, (PerkFactory.Perk)var8.getKey());
            var1.putFloat(((IsoGameCharacter.XPMultiplier)var8.getValue()).multiplier);
            var1.put((byte)((IsoGameCharacter.XPMultiplier)var8.getValue()).minLevel);
            var1.put((byte)((IsoGameCharacter.XPMultiplier)var8.getValue()).maxLevel);
         }

      }

      public void setXPToLevel(PerkFactory.Perk var1, int var2) {
         PerkFactory.Perk var3 = null;

         for(int var4 = 0; var4 < PerkFactory.PerkList.size(); ++var4) {
            PerkFactory.Perk var5 = (PerkFactory.Perk)PerkFactory.PerkList.get(var4);
            if (var5.getType() == var1) {
               var3 = var5;
               break;
            }
         }

         if (var3 != null) {
            this.XPMap.put(var1, var3.getTotalXpForLevel(var2));
         }

      }
   }

   public class CharacterTraits extends TraitCollection {
      public final TraitCollection.TraitSlot Obese = this.getTraitSlot("Obese");
      public final TraitCollection.TraitSlot Athletic = this.getTraitSlot("Athletic");
      public final TraitCollection.TraitSlot Overweight = this.getTraitSlot("Overweight");
      public final TraitCollection.TraitSlot Unfit = this.getTraitSlot("Unfit");
      public final TraitCollection.TraitSlot Emaciated = this.getTraitSlot("Emaciated");
      public final TraitCollection.TraitSlot Graceful = this.getTraitSlot("Graceful");
      public final TraitCollection.TraitSlot Clumsy = this.getTraitSlot("Clumsy");
      public final TraitCollection.TraitSlot Strong = this.getTraitSlot("Strong");
      public final TraitCollection.TraitSlot Weak = this.getTraitSlot("Weak");
      public final TraitCollection.TraitSlot VeryUnderweight = this.getTraitSlot("Very Underweight");
      public final TraitCollection.TraitSlot Underweight = this.getTraitSlot("Underweight");
      public final TraitCollection.TraitSlot FastHealer = this.getTraitSlot("FastHealer");
      public final TraitCollection.TraitSlot SlowHealer = this.getTraitSlot("SlowHealer");
      public final TraitCollection.TraitSlot ShortSighted = this.getTraitSlot("ShortSighted");
      public final TraitCollection.TraitSlot EagleEyed = this.getTraitSlot("EagleEyed");
      public final TraitCollection.TraitSlot Agoraphobic = this.getTraitSlot("Agoraphobic");
      public final TraitCollection.TraitSlot Claustophobic = this.getTraitSlot("Claustophobic");
      public final TraitCollection.TraitSlot AdrenalineJunkie = this.getTraitSlot("AdrenalineJunkie");
      public final TraitCollection.TraitSlot OutOfShape = this.getTraitSlot("Out of Shape");
      public final TraitCollection.TraitSlot HighThirst = this.getTraitSlot("HighThirst");
      public final TraitCollection.TraitSlot LowThirst = this.getTraitSlot("LowThirst");
      public final TraitCollection.TraitSlot HeartyAppitite = this.getTraitSlot("HeartyAppitite");
      public final TraitCollection.TraitSlot LightEater = this.getTraitSlot("LightEater");
      public final TraitCollection.TraitSlot Cowardly = this.getTraitSlot("Cowardly");
      public final TraitCollection.TraitSlot Brave = this.getTraitSlot("Brave");
      public final TraitCollection.TraitSlot Brooding = this.getTraitSlot("Brooding");
      public final TraitCollection.TraitSlot Insomniac = this.getTraitSlot("Insomniac");
      public final TraitCollection.TraitSlot NeedsLessSleep = this.getTraitSlot("NeedsLessSleep");
      public final TraitCollection.TraitSlot NeedsMoreSleep = this.getTraitSlot("NeedsMoreSleep");
      public final TraitCollection.TraitSlot Asthmatic = this.getTraitSlot("Asthmatic");
      public final TraitCollection.TraitSlot PlaysFootball = this.getTraitSlot("PlaysFootball");
      public final TraitCollection.TraitSlot Jogger = this.getTraitSlot("Jogger");
      public final TraitCollection.TraitSlot NightVision = this.getTraitSlot("NightVision");
      public final TraitCollection.TraitSlot FastLearner = this.getTraitSlot("FastLearner");
      public final TraitCollection.TraitSlot SlowLearner = this.getTraitSlot("SlowLearner");
      public final TraitCollection.TraitSlot Pacifist = this.getTraitSlot("Pacifist");
      public final TraitCollection.TraitSlot Feeble = this.getTraitSlot("Feeble");
      public final TraitCollection.TraitSlot Stout = this.getTraitSlot("Stout");
      public final TraitCollection.TraitSlot ShortTemper = this.getTraitSlot("ShortTemper");
      public final TraitCollection.TraitSlot Patient = this.getTraitSlot("Patient");
      public final TraitCollection.TraitSlot Injured = this.getTraitSlot("Injured");
      public final TraitCollection.TraitSlot Inconspicuous = this.getTraitSlot("Inconspicuous");
      public final TraitCollection.TraitSlot Conspicuous = this.getTraitSlot("Conspicuous");
      public final TraitCollection.TraitSlot Desensitized = this.getTraitSlot("Desensitized");
      public final TraitCollection.TraitSlot NightOwl = this.getTraitSlot("NightOwl");
      public final TraitCollection.TraitSlot Hemophobic = this.getTraitSlot("Hemophobic");
      public final TraitCollection.TraitSlot Burglar = this.getTraitSlot("Burglar");
      public final TraitCollection.TraitSlot KeenHearing = this.getTraitSlot("KeenHearing");
      public final TraitCollection.TraitSlot Deaf = this.getTraitSlot("Deaf");
      public final TraitCollection.TraitSlot HardOfHearing = this.getTraitSlot("HardOfHearing");
      public final TraitCollection.TraitSlot ThinSkinned = this.getTraitSlot("ThinSkinned");
      public final TraitCollection.TraitSlot ThickSkinned = this.getTraitSlot("ThickSkinned");
      public final TraitCollection.TraitSlot Marksman = this.getTraitSlot("Marksman");
      public final TraitCollection.TraitSlot Outdoorsman = this.getTraitSlot("Outdoorsman");
      public final TraitCollection.TraitSlot Lucky = this.getTraitSlot("Lucky");
      public final TraitCollection.TraitSlot Unlucky = this.getTraitSlot("Unlucky");
      public final TraitCollection.TraitSlot Nutritionist = this.getTraitSlot("Nutritionist");
      public final TraitCollection.TraitSlot Nutritionist2 = this.getTraitSlot("Nutritionist2");
      public final TraitCollection.TraitSlot Organized = this.getTraitSlot("Organized");
      public final TraitCollection.TraitSlot Disorganized = this.getTraitSlot("Disorganized");
      public final TraitCollection.TraitSlot Axeman = this.getTraitSlot("Axeman");
      public final TraitCollection.TraitSlot IronGut = this.getTraitSlot("IronGut");
      public final TraitCollection.TraitSlot WeakStomach = this.getTraitSlot("WeakStomach");
      public final TraitCollection.TraitSlot HeavyDrinker = this.getTraitSlot("HeavyDrinker");
      public final TraitCollection.TraitSlot LightDrinker = this.getTraitSlot("LightDrinker");
      public final TraitCollection.TraitSlot Resilient = this.getTraitSlot("Resilient");
      public final TraitCollection.TraitSlot ProneToIllness = this.getTraitSlot("ProneToIllness");
      public final TraitCollection.TraitSlot SpeedDemon = this.getTraitSlot("SpeedDemon");
      public final TraitCollection.TraitSlot SundayDriver = this.getTraitSlot("SundayDriver");
      public final TraitCollection.TraitSlot Smoker = this.getTraitSlot("Smoker");
      public final TraitCollection.TraitSlot Hypercondriac = this.getTraitSlot("Hypercondriac");
      public final TraitCollection.TraitSlot Illiterate = this.getTraitSlot("Illiterate");

      public boolean isIlliterate() {
         return this.Illiterate.isSet();
      }
   }

   public static class Location {
      public int x;
      public int y;
      public int z;

      public Location(int var1, int var2, int var3) {
         this.x = var1;
         this.y = var2;
         this.z = var3;
      }

      public boolean equals(Object var1) {
         if (!(var1 instanceof IsoGameCharacter.Location)) {
            return false;
         } else {
            IsoGameCharacter.Location var2 = (IsoGameCharacter.Location)var1;
            return this.x == var2.x && this.y == var2.y && this.z == var2.z;
         }
      }
   }

   public static class LightInfo {
      public IsoGridSquare square;
      public float x;
      public float y;
      public float z;
      public float angleX;
      public float angleY;
      public ArrayList torches = new ArrayList();
      public long time;
      public float night;
      public float rmod;
      public float gmod;
      public float bmod;

      public void initFrom(IsoGameCharacter.LightInfo var1) {
         this.square = var1.square;
         this.x = var1.x;
         this.y = var1.y;
         this.z = var1.z;
         this.angleX = var1.angleX;
         this.angleY = var1.angleY;
         this.torches.clear();
         this.torches.addAll(var1.torches);
         this.time = (long)((double)System.nanoTime() / 1000000.0D);
         this.night = var1.night;
         this.rmod = var1.rmod;
         this.gmod = var1.gmod;
         this.bmod = var1.bmod;
      }
   }

   private static final class L_getDotWithForwardDirection {
      static final Vector2 v1 = new Vector2();
      static final Vector2 v2 = new Vector2();
   }

   public class PerkInfo {
      public int level = 0;
      public PerkFactory.Perk perk;

      public int getLevel() {
         return this.level;
      }
   }

   private static class ReadBook {
      String fullType;
      int alreadyReadPages;
   }

   private static final class L_renderShadow {
      static final Vector3f forward = new Vector3f();
      static final Vector3 v1 = new Vector3();
      static final Vector3f v3 = new Vector3f();
   }

   private static final class L_renderLast {
      static final Color color = new Color();
   }

   private static class s_performance {
      static final PerformanceProfileProbe postUpdate = new PerformanceProfileProbe("IsoGameCharacter.postUpdate");
      public static PerformanceProfileProbe update = new PerformanceProfileProbe("IsoGameCharacter.update");
   }

   public static class TorchInfo {
      private static final ObjectPool TorchInfoPool = new ObjectPool(IsoGameCharacter.TorchInfo::new);
      private static final Vector3f tempVector3f = new Vector3f();
      public int id;
      public float x;
      public float y;
      public float z;
      public float angleX;
      public float angleY;
      public float dist;
      public float strength;
      public boolean bCone;
      public float dot;
      public int focusing;

      public static IsoGameCharacter.TorchInfo alloc() {
         return (IsoGameCharacter.TorchInfo)TorchInfoPool.alloc();
      }

      public static void release(IsoGameCharacter.TorchInfo var0) {
         TorchInfoPool.release((Object)var0);
      }

      public IsoGameCharacter.TorchInfo set(IsoPlayer var1, InventoryItem var2) {
         this.x = var1.getX();
         this.y = var1.getY();
         this.z = var1.getZ();
         Vector2 var3 = var1.getLookVector(IsoGameCharacter.tempVector2);
         this.angleX = var3.x;
         this.angleY = var3.y;
         this.dist = (float)var2.getLightDistance();
         this.strength = var2.getLightStrength();
         this.bCone = var2.isTorchCone();
         this.dot = var2.getTorchDot();
         this.focusing = 0;
         return this;
      }

      public IsoGameCharacter.TorchInfo set(VehiclePart var1) {
         BaseVehicle var2 = var1.getVehicle();
         VehicleLight var3 = var1.getLight();
         VehicleScript var4 = var2.getScript();
         Vector3f var5 = tempVector3f;
         var5.set(var3.offset.x * var4.getExtents().x / 2.0F, 0.0F, var3.offset.y * var4.getExtents().z / 2.0F);
         var2.getWorldPos(var5, var5);
         this.x = var5.x;
         this.y = var5.y;
         this.z = var5.z;
         var5 = var2.getForwardVector(var5);
         this.angleX = var5.x;
         this.angleY = var5.z;
         this.dist = var1.getLightDistance();
         this.strength = var1.getLightIntensity();
         this.bCone = true;
         this.dot = var3.dot;
         this.focusing = (int)var1.getLightFocusing();
         return this;
      }
   }

   private static class L_postUpdate {
      static final MoveDeltaModifiers moveDeltas = new MoveDeltaModifiers();
   }

   private static final class L_actionStateChanged {
      static final ArrayList stateNames = new ArrayList();
      static final ArrayList states = new ArrayList();
   }

   protected static final class l_testDotSide {
      static final Vector2 v1 = new Vector2();
      static final Vector2 v2 = new Vector2();
      static final Vector2 v3 = new Vector2();
   }

   private static final class Bandages {
      final HashMap bandageTypeMap = new HashMap();
      final THashMap itemMap = new THashMap();

      String getBloodBandageType(String var1) {
         String var2 = (String)this.bandageTypeMap.get(var1);
         if (var2 == null) {
            this.bandageTypeMap.put(var1, var2 = var1 + "_Blood");
         }

         return var2;
      }

      void update(IsoGameCharacter var1) {
         if (!GameServer.bServer) {
            BodyDamage var2 = var1.getBodyDamage();
            WornItems var3 = var1.getWornItems();
            if (var2 != null && var3 != null) {
               assert !(var1 instanceof IsoZombie);

               this.itemMap.clear();

               int var4;
               for(var4 = 0; var4 < var3.size(); ++var4) {
                  InventoryItem var5 = var3.getItemByIndex(var4);
                  if (var5 != null) {
                     this.itemMap.put(var5.getFullType(), var5);
                  }
               }

               for(var4 = 0; var4 < BodyPartType.ToIndex(BodyPartType.MAX); ++var4) {
                  BodyPart var10 = var2.getBodyPart(BodyPartType.FromIndex(var4));
                  BodyPartLast var6 = var2.getBodyPartsLastState(BodyPartType.FromIndex(var4));
                  String var7 = var10.getType().getBandageModel();
                  if (!StringUtils.isNullOrWhitespace(var7)) {
                     String var8 = this.getBloodBandageType(var7);
                     if (var10.bandaged() != var6.bandaged()) {
                        if (var10.bandaged()) {
                           if (var10.isBandageDirty()) {
                              this.removeBandageModel(var1, var7);
                              this.addBandageModel(var1, var8);
                           } else {
                              this.removeBandageModel(var1, var8);
                              this.addBandageModel(var1, var7);
                           }
                        } else {
                           this.removeBandageModel(var1, var7);
                           this.removeBandageModel(var1, var8);
                        }
                     }

                     String var9;
                     if (var10.bitten() != var6.bitten()) {
                        if (var10.bitten()) {
                           var9 = var10.getType().getBiteWoundModel(var1.isFemale());
                           if (StringUtils.isNullOrWhitespace(var9)) {
                              continue;
                           }

                           this.addBandageModel(var1, var9);
                        } else {
                           this.removeBandageModel(var1, var10.getType().getBiteWoundModel(var1.isFemale()));
                        }
                     }

                     if (var10.scratched() != var6.scratched()) {
                        if (var10.scratched()) {
                           var9 = var10.getType().getScratchWoundModel(var1.isFemale());
                           if (StringUtils.isNullOrWhitespace(var9)) {
                              continue;
                           }

                           this.addBandageModel(var1, var9);
                        } else {
                           this.removeBandageModel(var1, var10.getType().getScratchWoundModel(var1.isFemale()));
                        }
                     }

                     if (var10.isCut() != var6.isCut()) {
                        if (var10.isCut()) {
                           var9 = var10.getType().getCutWoundModel(var1.isFemale());
                           if (!StringUtils.isNullOrWhitespace(var9)) {
                              this.addBandageModel(var1, var9);
                           }
                        } else {
                           this.removeBandageModel(var1, var10.getType().getCutWoundModel(var1.isFemale()));
                        }
                     }
                  }
               }

            }
         }
      }

      protected void addBandageModel(IsoGameCharacter var1, String var2) {
         if (!this.itemMap.containsKey(var2)) {
            InventoryItem var3 = InventoryItemFactory.CreateItem(var2);
            if (var3 instanceof Clothing) {
               Clothing var4 = (Clothing)var3;
               var1.getInventory().addItem(var4);
               var1.setWornItem(var4.getBodyLocation(), var4);
               var1.resetModelNextFrame();
            }
         }
      }

      protected void removeBandageModel(IsoGameCharacter var1, String var2) {
         InventoryItem var3 = (InventoryItem)this.itemMap.get(var2);
         if (var3 != null) {
            var1.getWornItems().remove(var3);
            var1.getInventory().Remove(var3);
            var1.resetModelNextFrame();
            var1.onWornItemsChanged();
            if (GameClient.bClient && var1 instanceof IsoPlayer && ((IsoPlayer)var1).isLocalPlayer()) {
               GameClient.instance.sendClothing((IsoPlayer)var1, var3.getBodyLocation(), var3);
            }

         }
      }
   }

   public static class XPMultiplier {
      public float multiplier;
      public int minLevel;
      public int maxLevel;
   }

   public static enum BodyLocation {
      Head,
      Leg,
      Arm,
      Chest,
      Stomach,
      Foot,
      Hand;

      // $FF: synthetic method
      private static IsoGameCharacter.BodyLocation[] $values() {
         return new IsoGameCharacter.BodyLocation[]{Head, Leg, Arm, Chest, Stomach, Foot, Hand};
      }
   }
}

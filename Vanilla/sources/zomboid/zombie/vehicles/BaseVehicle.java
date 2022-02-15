package zombie.vehicles;

import fmod.fmod.FMODManager;
import fmod.fmod.FMODSoundEmitter;
import fmod.fmod.FMOD_STUDIO_PARAMETER_DESCRIPTION;
import fmod.fmod.IFMODParameterUpdater;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import org.joml.Matrix3fc;
import org.joml.Matrix4f;
import org.joml.Matrix4fc;
import org.joml.Quaternionf;
import org.joml.Quaternionfc;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector3fc;
import org.joml.Vector4f;
import se.krka.kahlua.j2se.KahluaTableImpl;
import se.krka.kahlua.vm.KahluaTable;
import zombie.GameTime;
import zombie.GameWindow;
import zombie.SandboxOptions;
import zombie.SoundManager;
import zombie.SystemDisabler;
import zombie.WorldSoundManager;
import zombie.Lua.LuaEventManager;
import zombie.Lua.LuaManager;
import zombie.ai.states.StaggerBackState;
import zombie.ai.states.ZombieFallDownState;
import zombie.audio.BaseSoundEmitter;
import zombie.audio.DummySoundEmitter;
import zombie.audio.FMODParameter;
import zombie.audio.FMODParameterList;
import zombie.audio.GameSoundClip;
import zombie.audio.parameters.ParameterVehicleBrake;
import zombie.audio.parameters.ParameterVehicleEngineCondition;
import zombie.audio.parameters.ParameterVehicleGear;
import zombie.audio.parameters.ParameterVehicleLoad;
import zombie.audio.parameters.ParameterVehicleRPM;
import zombie.audio.parameters.ParameterVehicleRoadMaterial;
import zombie.audio.parameters.ParameterVehicleSkid;
import zombie.audio.parameters.ParameterVehicleSpeed;
import zombie.audio.parameters.ParameterVehicleSteer;
import zombie.audio.parameters.ParameterVehicleTireMissing;
import zombie.characters.IsoGameCharacter;
import zombie.characters.IsoPlayer;
import zombie.characters.IsoZombie;
import zombie.characters.BodyDamage.BodyPart;
import zombie.characters.BodyDamage.BodyPartType;
import zombie.core.Color;
import zombie.core.Core;
import zombie.core.Rand;
import zombie.core.SpriteRenderer;
import zombie.core.Translator;
import zombie.core.math.PZMath;
import zombie.core.network.ByteBufferWriter;
import zombie.core.opengl.Shader;
import zombie.core.physics.Bullet;
import zombie.core.physics.CarController;
import zombie.core.physics.Transform;
import zombie.core.physics.WorldSimulation;
import zombie.core.raknet.UdpConnection;
import zombie.core.skinnedmodel.ModelManager;
import zombie.core.skinnedmodel.animation.AnimationMultiTrack;
import zombie.core.skinnedmodel.animation.AnimationPlayer;
import zombie.core.skinnedmodel.animation.AnimationTrack;
import zombie.core.skinnedmodel.model.Model;
import zombie.core.skinnedmodel.model.SkinningData;
import zombie.core.skinnedmodel.model.VehicleModelInstance;
import zombie.core.skinnedmodel.model.VehicleSubModelInstance;
import zombie.core.textures.ColorInfo;
import zombie.core.textures.Texture;
import zombie.core.textures.TextureID;
import zombie.core.utils.UpdateLimit;
import zombie.debug.DebugLog;
import zombie.debug.DebugOptions;
import zombie.debug.DebugType;
import zombie.debug.LineDrawer;
import zombie.input.GameKeyboard;
import zombie.inventory.CompressIdenticalItems;
import zombie.inventory.InventoryItem;
import zombie.inventory.InventoryItemFactory;
import zombie.inventory.ItemContainer;
import zombie.inventory.ItemPickerJava;
import zombie.inventory.types.DrainableComboItem;
import zombie.inventory.types.HandWeapon;
import zombie.inventory.types.InventoryContainer;
import zombie.inventory.types.Key;
import zombie.iso.IsoCamera;
import zombie.iso.IsoCell;
import zombie.iso.IsoChunk;
import zombie.iso.IsoChunkMap;
import zombie.iso.IsoDirections;
import zombie.iso.IsoGridSquare;
import zombie.iso.IsoLightSource;
import zombie.iso.IsoMovingObject;
import zombie.iso.IsoObject;
import zombie.iso.IsoUtils;
import zombie.iso.IsoWorld;
import zombie.iso.Vector2;
import zombie.iso.SpriteDetails.IsoObjectType;
import zombie.iso.objects.IsoDeadBody;
import zombie.iso.objects.IsoTree;
import zombie.iso.objects.IsoWindow;
import zombie.iso.objects.IsoWorldInventoryObject;
import zombie.iso.objects.RainManager;
import zombie.iso.objects.RenderEffectType;
import zombie.iso.objects.interfaces.Thumpable;
import zombie.iso.weather.ClimateManager;
import zombie.network.ClientServerMap;
import zombie.network.GameClient;
import zombie.network.GameServer;
import zombie.network.PacketTypes;
import zombie.network.PassengerMap;
import zombie.network.ServerOptions;
import zombie.popman.ObjectPool;
import zombie.radio.ZomboidRadio;
import zombie.scripting.ScriptManager;
import zombie.scripting.objects.ModelAttachment;
import zombie.scripting.objects.ModelScript;
import zombie.scripting.objects.VehicleScript;
import zombie.ui.TextManager;
import zombie.ui.UIManager;
import zombie.util.IPooledObject;
import zombie.util.Pool;
import zombie.util.StringUtils;
import zombie.util.Type;
import zombie.util.list.PZArrayUtil;

public final class BaseVehicle extends IsoMovingObject implements Thumpable, IFMODParameterUpdater {
   private long lastCrashTime = 0L;
   public static final float RADIUS = 0.3F;
   public static final int FADE_DISTANCE = 15;
   public static final int RANDOMIZE_CONTAINER_CHANCE = 100;
   public static final byte authorizationOnServer = 0;
   public static final byte authorizationSimulation = 1;
   public static final byte authorizationServerSimulation = 2;
   public static final byte authorizationOwner = 3;
   public static final byte authorizationServerOwner = 4;
   private static final Vector3f _UNIT_Y = new Vector3f(0.0F, 1.0F, 0.0F);
   private static final PolygonalMap2.VehiclePoly tempPoly = new PolygonalMap2.VehiclePoly();
   public static final boolean YURI_FORCE_FIELD = false;
   public static boolean RENDER_TO_TEXTURE = false;
   public static float CENTER_OF_MASS_MAGIC = 0.7F;
   private static final float[] wheelParams = new float[24];
   private static final float[] physicsParams = new float[27];
   static final byte POSITION_ORIENTATION_PACKET_SIZE = 102;
   public static Texture vehicleShadow = null;
   public int justBreakConstraintTimer = 0;
   public BaseVehicle wasTowedBy = null;
   protected static final ColorInfo inf = new ColorInfo();
   private static final float[] lowRiderParam = new float[4];
   private final BaseVehicle.VehicleImpulse impulseFromServer = new BaseVehicle.VehicleImpulse();
   private final BaseVehicle.VehicleImpulse[] impulseFromSquishedZombie = new BaseVehicle.VehicleImpulse[4];
   private final ArrayList impulseFromHitZombie = new ArrayList();
   private final int netPlayerTimeoutMax = 30;
   private final Vector4f tempVector4f = new Vector4f();
   public final ArrayList models = new ArrayList();
   public IsoChunk chunk;
   public boolean polyDirty = true;
   private boolean polyGarageCheck = true;
   private float radiusReductionInGarage = 0.0F;
   public short VehicleID = -1;
   public int sqlID = -1;
   public boolean serverRemovedFromWorld = false;
   public boolean trace = false;
   public VehicleInterpolation interpolation = null;
   public boolean waitFullUpdate;
   public float throttle = 0.0F;
   public double engineSpeed;
   public TransmissionNumber transmissionNumber;
   public final UpdateLimit transmissionChangeTime = new UpdateLimit(1000L);
   public boolean hasExtendOffset = true;
   public boolean hasExtendOffsetExiting = false;
   public float savedPhysicsZ = Float.NaN;
   public final Quaternionf savedRot = new Quaternionf();
   public final Transform jniTransform = new Transform();
   public float jniSpeed;
   public boolean jniIsCollide;
   public final Vector3f jniLinearVelocity = new Vector3f();
   public final Vector3f netLinearVelocity = new Vector3f();
   public byte netPlayerAuthorization = 0;
   public short netPlayerId = 0;
   public int netPlayerTimeout = 0;
   public int authSimulationHash = 0;
   public long authSimulationTime = 0L;
   public int frontEndDurability = 100;
   public int rearEndDurability = 100;
   public float rust = 0.0F;
   public float colorHue = 0.0F;
   public float colorSaturation = 0.0F;
   public float colorValue = 0.0F;
   public int currentFrontEndDurability = 100;
   public int currentRearEndDurability = 100;
   public float collideX = -1.0F;
   public float collideY = -1.0F;
   public final PolygonalMap2.VehiclePoly shadowCoord = new PolygonalMap2.VehiclePoly();
   public BaseVehicle.engineStateTypes engineState;
   public long engineLastUpdateStateTime;
   public static final int MAX_WHEELS = 4;
   public static final int PHYSICS_PARAM_COUNT = 27;
   public final BaseVehicle.WheelInfo[] wheelInfo;
   public boolean skidding;
   public long skidSound;
   public long ramSound;
   public long ramSoundTime;
   private VehicleEngineRPM vehicleEngineRPM;
   public final long[] new_EngineSoundId;
   private long combinedEngineSound;
   public int engineSoundIndex;
   public BaseSoundEmitter hornemitter;
   public float startTime;
   public boolean headlightsOn;
   public boolean stoplightsOn;
   public boolean windowLightsOn;
   public boolean soundHornOn;
   public boolean soundBackMoveOn;
   public final LightbarLightsMode lightbarLightsMode;
   public final LightbarSirenMode lightbarSirenMode;
   private final IsoLightSource leftLight1;
   private final IsoLightSource leftLight2;
   private final IsoLightSource rightLight1;
   private final IsoLightSource rightLight2;
   private int leftLightIndex;
   private int rightLightIndex;
   public final BaseVehicle.ServerVehicleState[] connectionState;
   protected BaseVehicle.Passenger[] passengers;
   protected String scriptName;
   protected VehicleScript script;
   protected final ArrayList parts;
   protected VehiclePart battery;
   protected int engineQuality;
   protected int engineLoudness;
   protected int enginePower;
   protected long engineCheckTime;
   protected final ArrayList lights;
   protected boolean createdModel;
   protected final Vector3f lastLinearVelocity;
   protected int skinIndex;
   protected CarController physics;
   protected boolean bCreated;
   protected final PolygonalMap2.VehiclePoly poly;
   protected final PolygonalMap2.VehiclePoly polyPlusRadius;
   protected boolean bDoDamageOverlay;
   protected boolean loaded;
   protected short updateFlags;
   protected long updateLockTimeout;
   final UpdateLimit limitPhysicSend;
   final UpdateLimit limitPhysicValid;
   public boolean addedToWorld;
   boolean removedFromWorld;
   private float polyPlusRadiusMinX;
   private float polyPlusRadiusMinY;
   private float polyPlusRadiusMaxX;
   private float polyPlusRadiusMaxY;
   private float maxSpeed;
   private boolean keyIsOnDoor;
   private boolean hotwired;
   private boolean hotwiredBroken;
   private boolean keysInIgnition;
   private long soundHorn;
   private long soundScrapePastPlant;
   private long soundBackMoveSignal;
   public long soundSirenSignal;
   private final HashMap choosenParts;
   private String type;
   private String respawnZone;
   private float mass;
   private float initialMass;
   private float brakingForce;
   private float baseQuality;
   private float currentSteering;
   private boolean isBraking;
   private int mechanicalID;
   private boolean needPartsUpdate;
   private boolean alarmed;
   private int alarmTime;
   private float alarmAccumulator;
   private double sirenStartTime;
   private boolean mechanicUIOpen;
   private boolean isGoodCar;
   private InventoryItem currentKey;
   private boolean doColor;
   private float brekingSlowFactor;
   private final ArrayList brekingObjectsList;
   private final UpdateLimit limitUpdate;
   public byte keySpawned;
   public final Matrix4f vehicleTransform;
   public final Matrix4f renderTransform;
   private final Matrix4f tempMatrix4fLWJGL_1;
   private final Quaternionf tempQuat4f;
   private final Transform tempTransform;
   private final Transform tempTransform2;
   private final Transform tempTransform3;
   private BaseSoundEmitter emitter;
   private float brakeBetweenUpdatesSpeed;
   public long physicActiveCheck;
   private long constraintChangedTime;
   private AnimationPlayer m_animPlayer;
   public String specificDistributionId;
   private boolean bAddThumpWorldSound;
   private final SurroundVehicle m_surroundVehicle;
   private boolean regulator;
   private float regulatorSpeed;
   private static final HashMap s_PartToMaskMap = new HashMap();
   private static final Byte BYTE_ZERO = 0;
   private final HashMap bloodIntensity;
   private boolean OptionBloodDecals;
   private long createPhysicsTime;
   private BaseVehicle vehicleTowing;
   private BaseVehicle vehicleTowedBy;
   public int constraintTowing;
   private int vehicleTowingID;
   private int vehicleTowedByID;
   private String towAttachmentSelf;
   private String towAttachmentOther;
   private float towConstraintZOffset;
   private final ParameterVehicleBrake parameterVehicleBrake;
   private final ParameterVehicleEngineCondition parameterVehicleEngineCondition;
   private final ParameterVehicleGear parameterVehicleGear;
   private final ParameterVehicleLoad parameterVehicleLoad;
   private final ParameterVehicleRoadMaterial parameterVehicleRoadMaterial;
   private final ParameterVehicleRPM parameterVehicleRPM;
   private final ParameterVehicleSkid parameterVehicleSkid;
   private final ParameterVehicleSpeed parameterVehicleSpeed;
   private final ParameterVehicleSteer parameterVehicleSteer;
   private final ParameterVehicleTireMissing parameterVehicleTireMissing;
   private final FMODParameterList fmodParameters;
   public static final ThreadLocal TL_vector2_pool = ThreadLocal.withInitial(BaseVehicle.Vector2ObjectPool::new);
   public static final ThreadLocal TL_vector2f_pool = ThreadLocal.withInitial(BaseVehicle.Vector2fObjectPool::new);
   public static final ThreadLocal TL_vector3f_pool = ThreadLocal.withInitial(BaseVehicle.Vector3fObjectPool::new);
   public static final ThreadLocal TL_matrix4f_pool = ThreadLocal.withInitial(BaseVehicle.Matrix4fObjectPool::new);
   public static final ThreadLocal TL_quaternionf_pool = ThreadLocal.withInitial(BaseVehicle.QuaternionfObjectPool::new);
   public static final float PHYSICS_Z_SCALE = 0.82F;
   public static float PLUS_RADIUS = 0.15F;
   private int zombiesHits;
   private long zombieHitTimestamp;
   public static final int MASK1_FRONT = 0;
   public static final int MASK1_REAR = 4;
   public static final int MASK1_DOOR_RIGHT_FRONT = 8;
   public static final int MASK1_DOOR_RIGHT_REAR = 12;
   public static final int MASK1_DOOR_LEFT_FRONT = 1;
   public static final int MASK1_DOOR_LEFT_REAR = 5;
   public static final int MASK1_WINDOW_RIGHT_FRONT = 9;
   public static final int MASK1_WINDOW_RIGHT_REAR = 13;
   public static final int MASK1_WINDOW_LEFT_FRONT = 2;
   public static final int MASK1_WINDOW_LEFT_REAR = 6;
   public static final int MASK1_WINDOW_FRONT = 10;
   public static final int MASK1_WINDOW_REAR = 14;
   public static final int MASK1_GUARD_RIGHT_FRONT = 3;
   public static final int MASK1_GUARD_RIGHT_REAR = 7;
   public static final int MASK1_GUARD_LEFT_FRONT = 11;
   public static final int MASK1_GUARD_LEFT_REAR = 15;
   public static final int MASK2_ROOF = 0;
   public static final int MASK2_LIGHT_RIGHT_FRONT = 4;
   public static final int MASK2_LIGHT_LEFT_FRONT = 8;
   public static final int MASK2_LIGHT_RIGHT_REAR = 12;
   public static final int MASK2_LIGHT_LEFT_REAR = 1;
   public static final int MASK2_BRAKE_RIGHT = 5;
   public static final int MASK2_BRAKE_LEFT = 9;
   public static final int MASK2_LIGHTBAR_RIGHT = 13;
   public static final int MASK2_LIGHTBAR_LEFT = 2;
   public static final int MASK2_HOOD = 6;
   public static final int MASK2_BOOT = 10;
   public float forcedFriction;
   protected final BaseVehicle.HitVars hitVars;

   public int getSqlId() {
      return this.sqlID;
   }

   public static Vector2 allocVector2() {
      return (Vector2)((BaseVehicle.Vector2ObjectPool)TL_vector2_pool.get()).alloc();
   }

   public static void releaseVector2(Vector2 var0) {
      ((BaseVehicle.Vector2ObjectPool)TL_vector2_pool.get()).release(var0);
   }

   private static Vector3f allocVector3f() {
      return (Vector3f)((BaseVehicle.Vector3fObjectPool)TL_vector3f_pool.get()).alloc();
   }

   private static void releaseVector3f(Vector3f var0) {
      ((BaseVehicle.Vector3fObjectPool)TL_vector3f_pool.get()).release(var0);
   }

   public BaseVehicle(IsoCell var1) {
      super(var1, false);
      this.engineState = BaseVehicle.engineStateTypes.Idle;
      this.wheelInfo = new BaseVehicle.WheelInfo[4];
      this.skidding = false;
      this.vehicleEngineRPM = null;
      this.new_EngineSoundId = new long[8];
      this.combinedEngineSound = 0L;
      this.engineSoundIndex = 0;
      this.hornemitter = null;
      this.startTime = 0.0F;
      this.headlightsOn = false;
      this.stoplightsOn = false;
      this.windowLightsOn = false;
      this.soundHornOn = false;
      this.soundBackMoveOn = false;
      this.lightbarLightsMode = new LightbarLightsMode();
      this.lightbarSirenMode = new LightbarSirenMode();
      this.leftLight1 = new IsoLightSource(0, 0, 0, 1.0F, 0.0F, 0.0F, 8);
      this.leftLight2 = new IsoLightSource(0, 0, 0, 1.0F, 0.0F, 0.0F, 8);
      this.rightLight1 = new IsoLightSource(0, 0, 0, 0.0F, 0.0F, 1.0F, 8);
      this.rightLight2 = new IsoLightSource(0, 0, 0, 0.0F, 0.0F, 1.0F, 8);
      this.leftLightIndex = -1;
      this.rightLightIndex = -1;
      this.connectionState = new BaseVehicle.ServerVehicleState[512];
      this.passengers = new BaseVehicle.Passenger[1];
      this.parts = new ArrayList();
      this.lights = new ArrayList();
      this.createdModel = false;
      this.lastLinearVelocity = new Vector3f();
      this.skinIndex = -1;
      this.poly = new PolygonalMap2.VehiclePoly();
      this.polyPlusRadius = new PolygonalMap2.VehiclePoly();
      this.bDoDamageOverlay = false;
      this.loaded = false;
      this.updateLockTimeout = 0L;
      this.limitPhysicSend = new UpdateLimit(100L);
      this.limitPhysicValid = new UpdateLimit(1000L);
      this.addedToWorld = false;
      this.removedFromWorld = false;
      this.polyPlusRadiusMinX = -123.0F;
      this.keyIsOnDoor = false;
      this.hotwired = false;
      this.hotwiredBroken = false;
      this.keysInIgnition = false;
      this.soundHorn = -1L;
      this.soundScrapePastPlant = -1L;
      this.soundBackMoveSignal = -1L;
      this.soundSirenSignal = -1L;
      this.choosenParts = new HashMap();
      this.type = "";
      this.mass = 0.0F;
      this.initialMass = 0.0F;
      this.brakingForce = 0.0F;
      this.baseQuality = 0.0F;
      this.currentSteering = 0.0F;
      this.isBraking = false;
      this.mechanicalID = 0;
      this.needPartsUpdate = false;
      this.alarmed = false;
      this.alarmTime = -1;
      this.sirenStartTime = 0.0D;
      this.mechanicUIOpen = false;
      this.isGoodCar = false;
      this.currentKey = null;
      this.doColor = true;
      this.brekingSlowFactor = 0.0F;
      this.brekingObjectsList = new ArrayList();
      this.limitUpdate = new UpdateLimit(333L);
      this.keySpawned = 0;
      this.vehicleTransform = new Matrix4f();
      this.renderTransform = new Matrix4f();
      this.tempMatrix4fLWJGL_1 = new Matrix4f();
      this.tempQuat4f = new Quaternionf();
      this.tempTransform = new Transform();
      this.tempTransform2 = new Transform();
      this.tempTransform3 = new Transform();
      this.brakeBetweenUpdatesSpeed = 0.0F;
      this.physicActiveCheck = -1L;
      this.constraintChangedTime = -1L;
      this.m_animPlayer = null;
      this.specificDistributionId = null;
      this.bAddThumpWorldSound = false;
      this.m_surroundVehicle = new SurroundVehicle(this);
      this.regulator = false;
      this.regulatorSpeed = 0.0F;
      this.bloodIntensity = new HashMap();
      this.OptionBloodDecals = false;
      this.createPhysicsTime = -1L;
      this.vehicleTowing = null;
      this.vehicleTowedBy = null;
      this.constraintTowing = -1;
      this.vehicleTowingID = -1;
      this.vehicleTowedByID = -1;
      this.towAttachmentSelf = null;
      this.towAttachmentOther = null;
      this.towConstraintZOffset = 0.0F;
      this.parameterVehicleBrake = new ParameterVehicleBrake(this);
      this.parameterVehicleEngineCondition = new ParameterVehicleEngineCondition(this);
      this.parameterVehicleGear = new ParameterVehicleGear(this);
      this.parameterVehicleLoad = new ParameterVehicleLoad(this);
      this.parameterVehicleRoadMaterial = new ParameterVehicleRoadMaterial(this);
      this.parameterVehicleRPM = new ParameterVehicleRPM(this);
      this.parameterVehicleSkid = new ParameterVehicleSkid(this);
      this.parameterVehicleSpeed = new ParameterVehicleSpeed(this);
      this.parameterVehicleSteer = new ParameterVehicleSteer(this);
      this.parameterVehicleTireMissing = new ParameterVehicleTireMissing(this);
      this.fmodParameters = new FMODParameterList();
      this.zombiesHits = 0;
      this.zombieHitTimestamp = 0L;
      this.forcedFriction = -1.0F;
      this.hitVars = new BaseVehicle.HitVars();
      this.setCollidable(false);
      this.respawnZone = new String("");
      this.scriptName = "Base.PickUpTruck";
      this.passengers[0] = new BaseVehicle.Passenger();
      this.waitFullUpdate = false;
      this.savedRot.w = 1.0F;

      int var2;
      for(var2 = 0; var2 < this.wheelInfo.length; ++var2) {
         this.wheelInfo[var2] = new BaseVehicle.WheelInfo();
      }

      if (GameClient.bClient) {
         this.interpolation = new VehicleInterpolation(VehicleManager.physicsDelay);
      }

      this.setKeyId(Rand.Next(100000000));
      this.engineSpeed = 0.0D;
      this.transmissionNumber = TransmissionNumber.N;
      this.rust = (float)Rand.Next(0, 2);
      this.jniIsCollide = false;

      for(var2 = 0; var2 < 4; ++var2) {
         lowRiderParam[var2] = 0.0F;
      }

      this.fmodParameters.add(this.parameterVehicleBrake);
      this.fmodParameters.add(this.parameterVehicleEngineCondition);
      this.fmodParameters.add(this.parameterVehicleGear);
      this.fmodParameters.add(this.parameterVehicleLoad);
      this.fmodParameters.add(this.parameterVehicleRPM);
      this.fmodParameters.add(this.parameterVehicleRoadMaterial);
      this.fmodParameters.add(this.parameterVehicleSkid);
      this.fmodParameters.add(this.parameterVehicleSpeed);
      this.fmodParameters.add(this.parameterVehicleSteer);
      this.fmodParameters.add(this.parameterVehicleTireMissing);
   }

   public static void LoadAllVehicleTextures() {
      DebugLog.General.println("BaseVehicle.LoadAllVehicleTextures...");
      ArrayList var0 = ScriptManager.instance.getAllVehicleScripts();
      Iterator var1 = var0.iterator();

      while(var1.hasNext()) {
         VehicleScript var2 = (VehicleScript)var1.next();
         LoadVehicleTextures(var2);
      }

   }

   public static void LoadVehicleTextures(VehicleScript var0) {
      if (SystemDisabler.doVehiclesWithoutTextures) {
         VehicleScript.Skin var1 = var0.getSkin(0);
         var1.textureData = LoadVehicleTexture(var1.texture);
         var1.textureDataMask = LoadVehicleTexture("vehicles_placeholder_mask");
         var1.textureDataDamage1Overlay = LoadVehicleTexture("vehicles_placeholder_damage1overlay");
         var1.textureDataDamage1Shell = LoadVehicleTexture("vehicles_placeholder_damage1shell");
         var1.textureDataDamage2Overlay = LoadVehicleTexture("vehicles_placeholder_damage2overlay");
         var1.textureDataDamage2Shell = LoadVehicleTexture("vehicles_placeholder_damage2shell");
         var1.textureDataLights = LoadVehicleTexture("vehicles_placeholder_lights");
         var1.textureDataRust = LoadVehicleTexture("vehicles_placeholder_rust");
      } else {
         for(int var3 = 0; var3 < var0.getSkinCount(); ++var3) {
            VehicleScript.Skin var2 = var0.getSkin(var3);
            var2.copyMissingFrom(var0.getTextures());
            LoadVehicleTextures(var2);
         }
      }

   }

   private static void LoadVehicleTextures(VehicleScript.Skin var0) {
      var0.textureData = LoadVehicleTexture(var0.texture);
      if (var0.textureMask != null) {
         var0.textureDataMask = LoadVehicleTexture(var0.textureMask, 0);
      }

      var0.textureDataDamage1Overlay = LoadVehicleTexture(var0.textureDamage1Overlay);
      var0.textureDataDamage1Shell = LoadVehicleTexture(var0.textureDamage1Shell);
      var0.textureDataDamage2Overlay = LoadVehicleTexture(var0.textureDamage2Overlay);
      var0.textureDataDamage2Shell = LoadVehicleTexture(var0.textureDamage2Shell);
      var0.textureDataLights = LoadVehicleTexture(var0.textureLights);
      var0.textureDataRust = LoadVehicleTexture(var0.textureRust);
      var0.textureDataShadow = LoadVehicleTexture(var0.textureShadow);
   }

   public static Texture LoadVehicleTexture(String var0) {
      byte var1 = 0;
      int var2 = var1 | (TextureID.bUseCompression ? 4 : 0);
      return LoadVehicleTexture(var0, var2);
   }

   public static Texture LoadVehicleTexture(String var0, int var1) {
      return StringUtils.isNullOrWhitespace(var0) ? null : Texture.getSharedTexture("media/textures/" + var0 + ".png", var1);
   }

   public void setNetPlayerAuthorization(byte var1) {
      this.netPlayerAuthorization = var1;
   }

   public static float getFakeSpeedModifier() {
      if (!GameClient.bClient && !GameServer.bServer) {
         return 1.0F;
      } else {
         float var0 = (float)ServerOptions.instance.SpeedLimit.getValue();
         return 120.0F / Math.min(var0, 120.0F);
      }
   }

   public boolean isLocalPhysicSim() {
      if (GameServer.bServer) {
         return this.netPlayerAuthorization == 0;
      } else {
         return this.netPlayerAuthorization == 1 || this.netPlayerAuthorization == 3;
      }
   }

   public void addImpulse(Vector3f var1, Vector3f var2) {
      if (!this.impulseFromServer.enable) {
         this.impulseFromServer.enable = true;
         this.impulseFromServer.impulse.set((Vector3fc)var1);
         this.impulseFromServer.rel_pos.set((Vector3fc)var2);
      } else if (this.impulseFromServer.impulse.length() < var1.length()) {
         this.impulseFromServer.impulse.set((Vector3fc)var1);
         this.impulseFromServer.rel_pos.set((Vector3fc)var2);
         this.impulseFromServer.enable = false;
         this.impulseFromServer.release();
      }

   }

   public double getEngineSpeed() {
      return this.engineSpeed;
   }

   public String getTransmissionNumberLetter() {
      return this.transmissionNumber.getString();
   }

   public int getTransmissionNumber() {
      return this.transmissionNumber.getIndex();
   }

   public void setClientForce(float var1) {
      this.physics.clientForce = var1;
   }

   public float getClientForce() {
      return this.physics.clientForce;
   }

   private void doVehicleColor() {
      if (!this.isDoColor()) {
         this.colorSaturation = 0.1F;
         this.colorValue = 0.9F;
      } else {
         this.colorHue = Rand.Next(0.0F, 0.0F);
         this.colorSaturation = 0.5F;
         this.colorValue = Rand.Next(0.3F, 0.6F);
         int var1 = Rand.Next(100);
         if (var1 < 20) {
            this.colorHue = Rand.Next(0.0F, 0.03F);
            this.colorSaturation = Rand.Next(0.85F, 1.0F);
            this.colorValue = Rand.Next(0.55F, 0.85F);
         } else if (var1 < 32) {
            this.colorHue = Rand.Next(0.55F, 0.61F);
            this.colorSaturation = Rand.Next(0.85F, 1.0F);
            this.colorValue = Rand.Next(0.65F, 0.75F);
         } else if (var1 < 67) {
            this.colorHue = 0.15F;
            this.colorSaturation = Rand.Next(0.0F, 0.1F);
            this.colorValue = Rand.Next(0.7F, 0.8F);
         } else if (var1 < 89) {
            this.colorHue = Rand.Next(0.0F, 1.0F);
            this.colorSaturation = Rand.Next(0.0F, 0.1F);
            this.colorValue = Rand.Next(0.1F, 0.25F);
         } else {
            this.colorHue = Rand.Next(0.0F, 1.0F);
            this.colorSaturation = Rand.Next(0.6F, 0.75F);
            this.colorValue = Rand.Next(0.3F, 0.7F);
         }

         if (this.getScript() != null) {
            if (this.getScript().getForcedHue() > -1.0F) {
               this.colorHue = this.getScript().getForcedHue();
            }

            if (this.getScript().getForcedSat() > -1.0F) {
               this.colorSaturation = this.getScript().getForcedSat();
            }

            if (this.getScript().getForcedVal() > -1.0F) {
               this.colorValue = this.getScript().getForcedVal();
            }
         }

      }
   }

   public String getObjectName() {
      return "Vehicle";
   }

   public boolean Serialize() {
      return true;
   }

   public void createPhysics() {
      if (!GameClient.bClient && this.VehicleID == -1) {
         this.VehicleID = VehicleIDMap.instance.allocateID();
         if (GameServer.bServer) {
            VehicleManager.instance.registerVehicle(this);
         } else {
            VehicleIDMap.instance.put(this.VehicleID, this);
         }
      }

      if (this.script == null) {
         this.setScript(this.scriptName);
      }

      if (this.script != null) {
         if (this.skinIndex == -1) {
            this.setSkinIndex(Rand.Next(this.getSkinCount()));
         }

         WorldSimulation.instance.create();
         this.jniTransform.origin.set(this.getX() - WorldSimulation.instance.offsetX, Float.isNaN(this.savedPhysicsZ) ? this.getZ() : this.savedPhysicsZ, this.getY() - WorldSimulation.instance.offsetY);
         this.physics = new CarController(this);
         this.savedPhysicsZ = Float.NaN;
         this.createPhysicsTime = System.currentTimeMillis();
         if (!this.bCreated) {
            this.bCreated = true;
            byte var1 = 30;
            if (SandboxOptions.getInstance().RecentlySurvivorVehicles.getValue() == 1) {
               var1 = 10;
            }

            if (SandboxOptions.getInstance().RecentlySurvivorVehicles.getValue() == 3) {
               var1 = 50;
            }

            if (Rand.Next(100) < var1) {
               this.setGoodCar(true);
            }
         }

         this.createParts();
         this.initParts();
         if (!this.createdModel) {
            ModelManager.instance.addVehicle(this);
            this.createdModel = true;
         }

         this.updateTransform();
         this.lights.clear();

         for(int var3 = 0; var3 < this.parts.size(); ++var3) {
            VehiclePart var2 = (VehiclePart)this.parts.get(var3);
            if (var2.getLight() != null) {
               this.lights.add(var2);
            }
         }

         this.setMaxSpeed(this.getScript().maxSpeed);
         this.setInitialMass(this.getScript().getMass());
         if (!this.getCell().getVehicles().contains(this) && !this.getCell().addVehicles.contains(this)) {
            this.getCell().addVehicles.add(this);
         }

         this.square = this.getCell().getGridSquare((double)this.x, (double)this.y, (double)this.z);
         this.randomizeContainers();
         if (this.engineState == BaseVehicle.engineStateTypes.Running) {
            this.engineDoRunning();
         }

         this.updateTotalMass();
         this.bDoDamageOverlay = true;
         this.updatePartStats();
         this.mechanicalID = Rand.Next(100000);
      }
   }

   public int getKeyId() {
      return this.keyId;
   }

   public boolean getKeySpawned() {
      return this.keySpawned != 0;
   }

   public void putKeyToZombie(IsoZombie var1) {
      InventoryItem var2 = this.createVehicleKey();
      var1.getInventory().AddItem(var2);
   }

   public void putKeyToContainer(ItemContainer var1, IsoGridSquare var2, IsoObject var3) {
      InventoryItem var4 = this.createVehicleKey();
      var1.AddItem(var4);
      if (GameServer.bServer) {
         for(int var5 = 0; var5 < GameServer.udpEngine.connections.size(); ++var5) {
            UdpConnection var6 = (UdpConnection)GameServer.udpEngine.connections.get(var5);
            if (var6.RelevantTo((float)var3.square.x, (float)var3.square.y)) {
               ByteBufferWriter var7 = var6.startPacket();
               PacketTypes.PacketType.AddInventoryItemToContainer.doPacket(var7);
               var7.putShort((short)2);
               var7.putInt((int)var3.getX());
               var7.putInt((int)var3.getY());
               var7.putInt((int)var3.getZ());
               int var8 = var2.getObjects().indexOf(var3);
               var7.putByte((byte)var8);
               var7.putByte((byte)var3.getContainerIndex(var1));

               try {
                  CompressIdenticalItems.save(var7.bb, var4);
               } catch (Exception var10) {
                  var10.printStackTrace();
               }

               PacketTypes.PacketType.AddInventoryItemToContainer.send(var6);
            }
         }
      }

   }

   public void putKeyToWorld(IsoGridSquare var1) {
      InventoryItem var2 = this.createVehicleKey();
      var1.AddWorldInventoryItem(var2, 0.0F, 0.0F, 0.0F);
   }

   public void addKeyToWorld() {
      if (this.haveOneDoorUnlocked() && Rand.Next(100) < 30) {
         if (Rand.Next(5) == 0) {
            this.keyIsOnDoor = true;
            this.currentKey = this.createVehicleKey();
         } else {
            this.addKeyToGloveBox();
         }

      } else if (this.haveOneDoorUnlocked() && Rand.Next(100) < 30) {
         this.keysInIgnition = true;
         this.currentKey = this.createVehicleKey();
      } else {
         if (Rand.Next(100) < 50) {
            IsoGridSquare var1 = this.getCell().getGridSquare((double)this.x, (double)this.y, (double)this.z);
            if (var1 != null) {
               this.addKeyToSquare(var1);
               return;
            }
         }

      }
   }

   public void addKeyToGloveBox() {
      if (this.keySpawned == 0) {
         if (this.getPartById("GloveBox") != null) {
            VehiclePart var1 = this.getPartById("GloveBox");
            InventoryItem var2 = this.createVehicleKey();
            var1.container.addItem(var2);
            this.keySpawned = 1;
         }

      }
   }

   public InventoryItem createVehicleKey() {
      InventoryItem var1 = InventoryItemFactory.CreateItem("CarKey");
      var1.setKeyId(this.getKeyId());
      var1.setName(Translator.getText("IGUI_CarKey", Translator.getText("IGUI_VehicleName" + this.getScript().getName())));
      Color var2 = Color.HSBtoRGB(this.colorHue, this.colorSaturation * 0.5F, this.colorValue);
      var1.setColor(var2);
      var1.setCustomColor(true);
      return var1;
   }

   public boolean addKeyToSquare(IsoGridSquare var1) {
      boolean var2 = false;
      IsoGridSquare var3 = null;

      int var4;
      int var5;
      for(var4 = 0; var4 < 3; ++var4) {
         for(var5 = var1.getX() - 10; var5 < var1.getX() + 10; ++var5) {
            for(int var6 = var1.getY() - 10; var6 < var1.getY() + 10; ++var6) {
               var3 = IsoWorld.instance.getCell().getGridSquare(var5, var6, var4);
               if (var3 != null) {
                  int var7;
                  for(var7 = 0; var7 < var3.getObjects().size(); ++var7) {
                     IsoObject var8 = (IsoObject)var3.getObjects().get(var7);
                     if (var8.container != null && (var8.container.type.equals("counter") || var8.container.type.equals("officedrawers") || var8.container.type.equals("shelves") || var8.container.type.equals("desk"))) {
                        this.putKeyToContainer(var8.container, var3, var8);
                        var2 = true;
                        break;
                     }
                  }

                  for(var7 = 0; var7 < var3.getMovingObjects().size(); ++var7) {
                     if (var3.getMovingObjects().get(var7) instanceof IsoZombie) {
                        ((IsoZombie)var3.getMovingObjects().get(var7)).addItemToSpawnAtDeath(this.createVehicleKey());
                        var2 = true;
                        break;
                     }
                  }
               }

               if (var2) {
                  break;
               }
            }

            if (var2) {
               break;
            }
         }

         if (var2) {
            break;
         }
      }

      if (Rand.Next(10) < 6) {
         while(!var2) {
            var4 = var1.getX() - 10 + Rand.Next(20);
            var5 = var1.getY() - 10 + Rand.Next(20);
            var3 = IsoWorld.instance.getCell().getGridSquare((double)var4, (double)var5, (double)this.z);
            if (var3 != null && !var3.isSolid() && !var3.isSolidTrans() && !var3.HasTree()) {
               this.putKeyToWorld(var3);
               var2 = true;
               break;
            }
         }
      }

      return var2;
   }

   public void toggleLockedDoor(VehiclePart var1, IsoGameCharacter var2, boolean var3) {
      if (var3) {
         if (!this.canLockDoor(var1, var2)) {
            return;
         }

         var1.getDoor().setLocked(true);
      } else {
         if (!this.canUnlockDoor(var1, var2)) {
            return;
         }

         var1.getDoor().setLocked(false);
      }

   }

   public boolean canLockDoor(VehiclePart var1, IsoGameCharacter var2) {
      if (var1 == null) {
         return false;
      } else if (var2 == null) {
         return false;
      } else {
         VehicleDoor var3 = var1.getDoor();
         if (var3 == null) {
            return false;
         } else if (var3.lockBroken) {
            return false;
         } else if (var3.locked) {
            return false;
         } else if (this.getSeat(var2) != -1) {
            return true;
         } else if (var2.getInventory().haveThisKeyId(this.getKeyId()) != null) {
            return true;
         } else {
            VehiclePart var4 = var1.getChildWindow();
            if (var4 != null && var4.getInventoryItem() == null) {
               return true;
            } else {
               VehicleWindow var5 = var4 == null ? null : var4.getWindow();
               return var5 != null && (var5.isOpen() || var5.isDestroyed());
            }
         }
      }
   }

   public boolean canUnlockDoor(VehiclePart var1, IsoGameCharacter var2) {
      if (var1 == null) {
         return false;
      } else if (var2 == null) {
         return false;
      } else {
         VehicleDoor var3 = var1.getDoor();
         if (var3 == null) {
            return false;
         } else if (var3.lockBroken) {
            return false;
         } else if (!var3.locked) {
            return false;
         } else if (this.getSeat(var2) != -1) {
            return true;
         } else if (var2.getInventory().haveThisKeyId(this.getKeyId()) != null) {
            return true;
         } else {
            VehiclePart var4 = var1.getChildWindow();
            if (var4 != null && var4.getInventoryItem() == null) {
               return true;
            } else {
               VehicleWindow var5 = var4 == null ? null : var4.getWindow();
               return var5 != null && (var5.isOpen() || var5.isDestroyed());
            }
         }
      }
   }

   private void initParts() {
      for(int var1 = 0; var1 < this.parts.size(); ++var1) {
         VehiclePart var2 = (VehiclePart)this.parts.get(var1);
         String var3 = var2.getLuaFunction("init");
         if (var3 != null) {
            this.callLuaVoid(var3, this, var2);
         }
      }

   }

   public void setGeneralPartCondition(float var1, float var2) {
      for(int var3 = 0; var3 < this.parts.size(); ++var3) {
         VehiclePart var4 = (VehiclePart)this.parts.get(var3);
         var4.setGeneralCondition((InventoryItem)null, var1, var2);
      }

   }

   private void createParts() {
      for(int var1 = 0; var1 < this.parts.size(); ++var1) {
         VehiclePart var2 = (VehiclePart)this.parts.get(var1);
         ArrayList var3 = var2.getItemType();
         if (var2.bCreated && var3 != null && !var3.isEmpty() && var2.getInventoryItem() == null && var2.getTable("install") == null) {
            var2.bCreated = false;
         } else if ((var3 == null || var3.isEmpty()) && var2.getInventoryItem() != null) {
            var2.item = null;
         }

         if (!var2.bCreated) {
            var2.bCreated = true;
            String var4 = var2.getLuaFunction("create");
            if (var4 == null) {
               var2.setRandomCondition((InventoryItem)null);
            } else {
               this.callLuaVoid(var4, this, var2);
               if (var2.getCondition() == -1) {
                  var2.setRandomCondition((InventoryItem)null);
               }
            }
         }
      }

      if (this.hasLightbar() && this.getScript().rightSirenCol != null && this.getScript().leftSirenCol != null) {
         this.leftLight1.r = this.leftLight2.r = this.getScript().leftSirenCol.r;
         this.leftLight1.g = this.leftLight2.g = this.getScript().leftSirenCol.g;
         this.leftLight1.b = this.leftLight2.b = this.getScript().leftSirenCol.b;
         this.rightLight1.r = this.rightLight2.r = this.getScript().rightSirenCol.r;
         this.rightLight1.g = this.rightLight2.g = this.getScript().rightSirenCol.g;
         this.rightLight1.b = this.rightLight2.b = this.getScript().rightSirenCol.b;
      }

   }

   public CarController getController() {
      return this.physics;
   }

   public SurroundVehicle getSurroundVehicle() {
      return this.m_surroundVehicle;
   }

   public int getSkinCount() {
      return this.script.getSkinCount();
   }

   public int getSkinIndex() {
      return this.skinIndex;
   }

   public void setSkinIndex(int var1) {
      if (var1 >= 0 && var1 <= this.getSkinCount()) {
         this.skinIndex = var1;
      }
   }

   public Texture getShadowTexture() {
      if (this.getScript() != null) {
         VehicleScript.Skin var1 = this.getScript().getTextures();
         if (this.getSkinIndex() >= 0 && this.getSkinIndex() < this.getScript().getSkinCount()) {
            var1 = this.getScript().getSkin(this.getSkinIndex());
         }

         if (var1.textureDataShadow != null) {
            return var1.textureDataShadow;
         }
      }

      if (vehicleShadow == null) {
         byte var2 = 0;
         int var3 = var2 | (TextureID.bUseCompression ? 4 : 0);
         vehicleShadow = Texture.getSharedTexture("media/vehicleShadow.png", var3);
      }

      return vehicleShadow;
   }

   public VehicleScript getScript() {
      return this.script;
   }

   public void setScript(String var1) {
      if (!StringUtils.isNullOrWhitespace(var1)) {
         this.scriptName = var1;
         boolean var2 = this.script != null;
         this.script = ScriptManager.instance.getVehicle(this.scriptName);
         ArrayList var4;
         int var5;
         if (this.script == null) {
            ArrayList var3 = ScriptManager.instance.getAllVehicleScripts();
            if (!var3.isEmpty()) {
               var4 = new ArrayList();

               for(var5 = 0; var5 < var3.size(); ++var5) {
                  VehicleScript var6 = (VehicleScript)var3.get(var5);
                  if (var6.getWheelCount() == 0) {
                     var4.add(var6);
                     var3.remove(var5--);
                  }
               }

               boolean var12 = this.loaded && this.parts.isEmpty() || this.scriptName.contains("Burnt");
               if (var12 && !var4.isEmpty()) {
                  this.script = (VehicleScript)var4.get(Rand.Next(var4.size()));
               } else if (!var3.isEmpty()) {
                  this.script = (VehicleScript)var3.get(Rand.Next(var3.size()));
               }

               if (this.script != null) {
                  this.scriptName = this.script.getFullName();
               }
            }
         }

         this.battery = null;
         this.models.clear();
         if (this.script != null) {
            this.scriptName = this.script.getFullName();
            BaseVehicle.Passenger[] var10 = this.passengers;
            this.passengers = new BaseVehicle.Passenger[this.script.getPassengerCount()];

            for(int var11 = 0; var11 < this.passengers.length; ++var11) {
               if (var11 < var10.length) {
                  this.passengers[var11] = var10[var11];
               } else {
                  this.passengers[var11] = new BaseVehicle.Passenger();
               }
            }

            var4 = new ArrayList();
            var4.addAll(this.parts);
            this.parts.clear();

            for(var5 = 0; var5 < this.script.getPartCount(); ++var5) {
               VehicleScript.Part var13 = this.script.getPart(var5);
               VehiclePart var7 = null;

               for(int var8 = 0; var8 < var4.size(); ++var8) {
                  VehiclePart var9 = (VehiclePart)var4.get(var8);
                  if (var9.getScriptPart() != null && var13.id.equals(var9.getScriptPart().id)) {
                     var7 = var9;
                     break;
                  }

                  if (var9.partId != null && var13.id.equals(var9.partId)) {
                     var7 = var9;
                     break;
                  }
               }

               if (var7 == null) {
                  var7 = new VehiclePart(this);
               }

               var7.setScriptPart(var13);
               var7.category = var13.category;
               var7.specificItem = var13.specificItem;
               if (var13.container != null && var13.container.contentType == null) {
                  if (var7.getItemContainer() == null) {
                     ItemContainer var15 = new ItemContainer(var13.id, (IsoGridSquare)null, this);
                     var7.setItemContainer(var15);
                     var15.ID = 0;
                  }

                  var7.getItemContainer().Capacity = var13.container.capacity;
               } else {
                  var7.setItemContainer((ItemContainer)null);
               }

               if (var13.door == null) {
                  var7.door = null;
               } else if (var7.door == null) {
                  var7.door = new VehicleDoor(var7);
                  var7.door.init(var13.door);
               }

               if (var13.window == null) {
                  var7.window = null;
               } else if (var7.window == null) {
                  var7.window = new VehicleWindow(var7);
                  var7.window.init(var13.window);
               } else {
                  var7.window.openable = var13.window.openable;
               }

               var7.parent = null;
               if (var7.children != null) {
                  var7.children.clear();
               }

               this.parts.add(var7);
               if ("Battery".equals(var7.getId())) {
                  this.battery = var7;
               }
            }

            VehiclePart var14;
            for(var5 = 0; var5 < this.script.getPartCount(); ++var5) {
               var14 = (VehiclePart)this.parts.get(var5);
               VehicleScript.Part var16 = var14.getScriptPart();
               if (var16.parent != null) {
                  var14.parent = this.getPartById(var16.parent);
                  if (var14.parent != null) {
                     var14.parent.addChild(var14);
                  }
               }
            }

            if (!var2 && !this.loaded) {
               this.frontEndDurability = this.rearEndDurability = 99999;
            }

            this.frontEndDurability = Math.min(this.frontEndDurability, this.script.getFrontEndHealth());
            this.rearEndDurability = Math.min(this.rearEndDurability, this.script.getRearEndHealth());
            this.currentFrontEndDurability = this.frontEndDurability;
            this.currentRearEndDurability = this.rearEndDurability;

            for(var5 = 0; var5 < this.script.getPartCount(); ++var5) {
               var14 = (VehiclePart)this.parts.get(var5);
               var14.setInventoryItem(var14.item);
            }
         }

         if (!this.loaded || this.colorHue == 0.0F && this.colorSaturation == 0.0F && this.colorValue == 0.0F) {
            this.doVehicleColor();
         }

         this.m_surroundVehicle.reset();
      }
   }

   public String getScriptName() {
      return this.scriptName;
   }

   public void setScriptName(String var1) {
      assert var1 == null || var1.contains(".");

      this.scriptName = var1;
   }

   public void setScript() {
      this.setScript(this.scriptName);
   }

   public void scriptReloaded() {
      this.tempTransform2.setIdentity();
      if (this.physics != null) {
         this.getWorldTransform(this.tempTransform2);
         this.tempTransform2.basis.getUnnormalizedRotation(this.savedRot);
         this.breakConstraint(false, false);
         Bullet.removeVehicle(this.VehicleID);
         this.physics = null;
      }

      if (this.createdModel) {
         ModelManager.instance.Remove(this);
         this.createdModel = false;
      }

      this.vehicleEngineRPM = null;

      int var1;
      for(var1 = 0; var1 < this.parts.size(); ++var1) {
         VehiclePart var2 = (VehiclePart)this.parts.get(var1);
         var2.setInventoryItem((InventoryItem)null);
         var2.bCreated = false;
      }

      this.setScript(this.scriptName);
      this.createPhysics();
      if (this.script != null) {
         for(var1 = 0; var1 < this.passengers.length; ++var1) {
            BaseVehicle.Passenger var4 = this.passengers[var1];
            if (var4 != null && var4.character != null) {
               VehicleScript.Position var3 = this.getPassengerPosition(var1, "inside");
               if (var3 != null) {
                  var4.offset.set((Vector3fc)var3.offset);
               }
            }
         }
      }

      this.polyDirty = true;
      if (this.isEngineRunning()) {
         this.engineDoShuttingDown();
         this.engineState = BaseVehicle.engineStateTypes.Idle;
      }

      if (this.addedToWorld) {
         PolygonalMap2.instance.removeVehicleFromWorld(this);
         PolygonalMap2.instance.addVehicleToWorld(this);
      }

   }

   public String getSkin() {
      if (this.script != null && this.script.getSkinCount() != 0) {
         if (this.skinIndex < 0 || this.skinIndex >= this.script.getSkinCount()) {
            this.skinIndex = Rand.Next(this.script.getSkinCount());
         }

         return this.script.getSkin(this.skinIndex).texture;
      } else {
         return "BOGUS";
      }
   }

   protected BaseVehicle.ModelInfo setModelVisible(VehiclePart var1, VehicleScript.Model var2, boolean var3) {
      for(int var4 = 0; var4 < this.models.size(); ++var4) {
         BaseVehicle.ModelInfo var5 = (BaseVehicle.ModelInfo)this.models.get(var4);
         if (var5.part == var1 && var5.scriptModel == var2) {
            if (var3) {
               return var5;
            }

            if (var5.m_animPlayer != null) {
               var5.m_animPlayer.reset();
               var5.m_animPlayer = null;
            }

            this.models.remove(var4);
            if (this.createdModel) {
               ModelManager.instance.Remove(this);
               ModelManager.instance.addVehicle(this);
            }

            var1.updateFlags = (short)(var1.updateFlags | 64);
            this.updateFlags = (short)(this.updateFlags | 64);
            return null;
         }
      }

      if (var3) {
         BaseVehicle.ModelInfo var6 = new BaseVehicle.ModelInfo();
         var6.part = var1;
         var6.scriptModel = var2;
         var6.modelScript = ScriptManager.instance.getModelScript(var2.file);
         var6.wheelIndex = var1.getWheelIndex();
         this.models.add(var6);
         if (this.createdModel) {
            ModelManager.instance.Remove(this);
            ModelManager.instance.addVehicle(this);
         }

         var1.updateFlags = (short)(var1.updateFlags | 64);
         this.updateFlags = (short)(this.updateFlags | 64);
         return var6;
      } else {
         return null;
      }
   }

   protected BaseVehicle.ModelInfo getModelInfoForPart(VehiclePart var1) {
      for(int var2 = 0; var2 < this.models.size(); ++var2) {
         BaseVehicle.ModelInfo var3 = (BaseVehicle.ModelInfo)this.models.get(var2);
         if (var3.part == var1) {
            return var3;
         }
      }

      return null;
   }

   protected VehicleScript.Passenger getScriptPassenger(int var1) {
      if (this.getScript() == null) {
         return null;
      } else {
         return var1 >= 0 && var1 < this.getScript().getPassengerCount() ? this.getScript().getPassenger(var1) : null;
      }
   }

   public int getMaxPassengers() {
      return this.passengers.length;
   }

   public boolean setPassenger(int var1, IsoGameCharacter var2, Vector3f var3) {
      if (var1 >= 0 && var1 < this.passengers.length) {
         if (var1 == 0) {
            this.setNeedPartsUpdate(true);
         }

         this.passengers[var1].character = var2;
         this.passengers[var1].offset.set((Vector3fc)var3);
         return true;
      } else {
         return false;
      }
   }

   public boolean clearPassenger(int var1) {
      if (var1 >= 0 && var1 < this.passengers.length) {
         this.passengers[var1].character = null;
         this.passengers[var1].offset.set(0.0F, 0.0F, 0.0F);
         return true;
      } else {
         return false;
      }
   }

   public BaseVehicle.Passenger getPassenger(int var1) {
      return var1 >= 0 && var1 < this.passengers.length ? this.passengers[var1] : null;
   }

   public IsoGameCharacter getCharacter(int var1) {
      BaseVehicle.Passenger var2 = this.getPassenger(var1);
      return var2 != null ? var2.character : null;
   }

   public int getSeat(IsoGameCharacter var1) {
      for(int var2 = 0; var2 < this.getMaxPassengers(); ++var2) {
         if (this.getCharacter(var2) == var1) {
            return var2;
         }
      }

      return -1;
   }

   public boolean isDriver(IsoGameCharacter var1) {
      return this.getSeat(var1) == 0;
   }

   public Vector3f getWorldPos(Vector3f var1, Vector3f var2, VehicleScript var3) {
      return this.getWorldPos(var1.x, var1.y, var1.z, var2, var3);
   }

   public Vector3f getWorldPos(float var1, float var2, float var3, Vector3f var4, VehicleScript var5) {
      Transform var6 = this.getWorldTransform(this.tempTransform);
      var6.origin.set(0.0F, 0.0F, 0.0F);
      var4.set(var1, var2, var3);
      var6.transform(var4);
      float var7 = this.jniTransform.origin.x + WorldSimulation.instance.offsetX;
      float var8 = this.jniTransform.origin.z + WorldSimulation.instance.offsetY;
      float var9 = this.jniTransform.origin.y / 2.46F;
      var4.set(var7 + var4.x, var8 + var4.z, var9 + var4.y);
      return var4;
   }

   public Vector3f getWorldPos(Vector3f var1, Vector3f var2) {
      return this.getWorldPos(var1.x, var1.y, var1.z, var2, this.getScript());
   }

   public Vector3f getWorldPos(float var1, float var2, float var3, Vector3f var4) {
      return this.getWorldPos(var1, var2, var3, var4, this.getScript());
   }

   public Vector3f getLocalPos(Vector3f var1, Vector3f var2) {
      return this.getLocalPos(var1.x, var1.y, var1.z, var2);
   }

   public Vector3f getLocalPos(float var1, float var2, float var3, Vector3f var4) {
      Transform var5 = this.getWorldTransform(this.tempTransform);
      var5.inverse();
      var4.set(var1 - WorldSimulation.instance.offsetX, 0.0F, var2 - WorldSimulation.instance.offsetY);
      var5.transform(var4);
      return var4;
   }

   public Vector3f getPassengerLocalPos(int var1, Vector3f var2) {
      BaseVehicle.Passenger var3 = this.getPassenger(var1);
      return var3 == null ? null : var2.set((Vector3fc)this.script.getModel().getOffset()).add(var3.offset);
   }

   public Vector3f getPassengerWorldPos(int var1, Vector3f var2) {
      BaseVehicle.Passenger var3 = this.getPassenger(var1);
      return var3 == null ? null : this.getPassengerPositionWorldPos(var3.offset.x, var3.offset.y, var3.offset.z, var2);
   }

   public Vector3f getPassengerPositionWorldPos(VehicleScript.Position var1, Vector3f var2) {
      return this.getPassengerPositionWorldPos(var1.offset.x, var1.offset.y, var1.offset.z, var2);
   }

   public Vector3f getPassengerPositionWorldPos(float var1, float var2, float var3, Vector3f var4) {
      var4.set((Vector3fc)this.script.getModel().offset);
      var4.add(var1, var2, var3);
      this.getWorldPos(var4.x, var4.y, var4.z, var4);
      var4.z = (float)((int)this.getZ());
      return var4;
   }

   public VehicleScript.Anim getPassengerAnim(int var1, String var2) {
      VehicleScript.Passenger var3 = this.getScriptPassenger(var1);
      if (var3 == null) {
         return null;
      } else {
         for(int var4 = 0; var4 < var3.anims.size(); ++var4) {
            VehicleScript.Anim var5 = (VehicleScript.Anim)var3.anims.get(var4);
            if (var2.equals(var5.id)) {
               return var5;
            }
         }

         return null;
      }
   }

   public VehicleScript.Position getPassengerPosition(int var1, String var2) {
      VehicleScript.Passenger var3 = this.getScriptPassenger(var1);
      return var3 == null ? null : var3.getPositionById(var2);
   }

   public VehiclePart getPassengerDoor(int var1) {
      VehicleScript.Passenger var2 = this.getScriptPassenger(var1);
      return var2 == null ? null : this.getPartById(var2.door);
   }

   public VehiclePart getPassengerDoor2(int var1) {
      VehicleScript.Passenger var2 = this.getScriptPassenger(var1);
      return var2 == null ? null : this.getPartById(var2.door2);
   }

   public boolean isPositionOnLeftOrRight(float var1, float var2) {
      Vector3f var3 = (Vector3f)((BaseVehicle.Vector3fObjectPool)TL_vector3f_pool.get()).alloc();
      this.getLocalPos(var1, var2, 0.0F, var3);
      var1 = var3.x;
      ((BaseVehicle.Vector3fObjectPool)TL_vector3f_pool.get()).release(var3);
      Vector3f var4 = this.script.getExtents();
      Vector3f var5 = this.script.getCenterOfMassOffset();
      float var6 = var5.x - var4.x / 2.0F;
      float var7 = var5.x + var4.x / 2.0F;
      return var1 < var6 * 0.98F || var1 > var7 * 0.98F;
   }

   public boolean haveOneDoorUnlocked() {
      for(int var1 = 0; var1 < this.getPartCount(); ++var1) {
         VehiclePart var2 = this.getPartByIndex(var1);
         if (var2.getDoor() != null && (var2.getId().contains("Left") || var2.getId().contains("Right")) && (!var2.getDoor().isLocked() || var2.getDoor().isOpen())) {
            return true;
         }
      }

      return false;
   }

   public String getPassengerArea(int var1) {
      VehicleScript.Passenger var2 = this.getScriptPassenger(var1);
      return var2 == null ? null : var2.area;
   }

   public void playPassengerAnim(int var1, String var2) {
      IsoGameCharacter var3 = this.getCharacter(var1);
      this.playPassengerAnim(var1, var2, var3);
   }

   public void playPassengerAnim(int var1, String var2, IsoGameCharacter var3) {
      if (var3 != null) {
         VehicleScript.Anim var4 = this.getPassengerAnim(var1, var2);
         if (var4 != null) {
            this.playCharacterAnim(var3, var4, true);
         }
      }
   }

   public void playPassengerSound(int var1, String var2) {
      VehicleScript.Anim var3 = this.getPassengerAnim(var1, var2);
      if (var3 != null && var3.sound != null) {
         this.playSound(var3.sound);
      }
   }

   public void playPartAnim(VehiclePart var1, String var2) {
      if (this.parts.contains(var1)) {
         VehicleScript.Anim var3 = var1.getAnimById(var2);
         if (var3 != null && !StringUtils.isNullOrWhitespace(var3.anim)) {
            BaseVehicle.ModelInfo var4 = this.getModelInfoForPart(var1);
            if (var4 != null) {
               AnimationPlayer var5 = var4.getAnimationPlayer();
               if (var5 != null && var5.isReady()) {
                  if (var5.getMultiTrack().getIndexOfTrack(var4.m_track) != -1) {
                     var5.getMultiTrack().removeTrack(var4.m_track);
                  }

                  var4.m_track = null;
                  SkinningData var6 = var5.getSkinningData();
                  if (var6 == null || var6.AnimationClips.containsKey(var3.anim)) {
                     AnimationTrack var7 = var5.play(var3.anim, var3.bLoop);
                     var4.m_track = var7;
                     if (var7 != null) {
                        var7.setLayerIdx(0);
                        var7.BlendDelta = 1.0F;
                        var7.SpeedDelta = var3.rate;
                        var7.IsPlaying = var3.bAnimate;
                        var7.reverse = var3.bReverse;
                        if (!var4.modelScript.boneWeights.isEmpty()) {
                           var7.setBoneWeights(var4.modelScript.boneWeights);
                           var7.initBoneWeights(var6);
                        }

                        if (var1.getWindow() != null) {
                           var7.setCurrentTimeValue(var7.getDuration() * var1.getWindow().getOpenDelta());
                        }
                     }

                  }
               }
            }
         }
      }
   }

   public void playActorAnim(VehiclePart var1, String var2, IsoGameCharacter var3) {
      if (var3 != null) {
         if (this.parts.contains(var1)) {
            VehicleScript.Anim var4 = var1.getAnimById("Actor" + var2);
            if (var4 != null) {
               this.playCharacterAnim(var3, var4, !"EngineDoor".equals(var1.getId()));
            }
         }
      }
   }

   private void playCharacterAnim(IsoGameCharacter var1, VehicleScript.Anim var2, boolean var3) {
      var1.PlayAnimUnlooped(var2.anim);
      var1.getSpriteDef().setFrameSpeedPerFrame(var2.rate);
      var1.getLegsSprite().Animate = true;
      Vector3f var4 = this.getForwardVector((Vector3f)((BaseVehicle.Vector3fObjectPool)TL_vector3f_pool.get()).alloc());
      if (var2.angle.lengthSquared() != 0.0F) {
         Matrix4f var5 = (Matrix4f)((BaseVehicle.Matrix4fObjectPool)TL_matrix4f_pool.get()).alloc();
         var5.rotationXYZ((float)Math.toRadians((double)var2.angle.x), (float)Math.toRadians((double)var2.angle.y), (float)Math.toRadians((double)var2.angle.z));
         var4.rotate(var5.getNormalizedRotation(this.tempQuat4f));
         ((BaseVehicle.Matrix4fObjectPool)TL_matrix4f_pool.get()).release(var5);
      }

      Vector2 var6 = (Vector2)((BaseVehicle.Vector2ObjectPool)TL_vector2_pool.get()).alloc();
      var6.set(var4.x, var4.z);
      var1.DirectionFromVector(var6);
      ((BaseVehicle.Vector2ObjectPool)TL_vector2_pool.get()).release(var6);
      var1.setForwardDirection(var4.x, var4.z);
      if (var1.getAnimationPlayer() != null) {
         var1.getAnimationPlayer().setTargetAngle(var1.getForwardDirection().getDirection());
         if (var3) {
            var1.getAnimationPlayer().setAngleToTarget();
         }
      }

      ((BaseVehicle.Vector3fObjectPool)TL_vector3f_pool.get()).release(var4);
   }

   public void playPartSound(VehiclePart var1, String var2) {
      if (this.parts.contains(var1)) {
         VehicleScript.Anim var3 = var1.getAnimById(var2);
         if (var3 != null && var3.sound != null) {
            this.playSound(var3.sound);
         }
      }
   }

   public void setCharacterPosition(IsoGameCharacter var1, int var2, String var3) {
      VehicleScript.Passenger var4 = this.getScriptPassenger(var2);
      if (var4 != null) {
         VehicleScript.Position var5 = var4.getPositionById(var3);
         if (var5 != null) {
            if (this.getCharacter(var2) == var1) {
               this.passengers[var2].offset.set((Vector3fc)var5.offset);
            } else {
               Vector3f var6 = (Vector3f)((BaseVehicle.Vector3fObjectPool)TL_vector3f_pool.get()).alloc();
               if (var5.area == null) {
                  this.getPassengerPositionWorldPos(var5, var6);
               } else {
                  VehicleScript.Area var7 = this.script.getAreaById(var5.area);
                  Vector2 var8 = (Vector2)((BaseVehicle.Vector2ObjectPool)TL_vector2_pool.get()).alloc();
                  Vector2 var9 = this.areaPositionWorld4PlayerInteract(var7, var8);
                  var6.x = var9.x;
                  var6.y = var9.y;
                  var6.z = 0.0F;
                  ((BaseVehicle.Vector2ObjectPool)TL_vector2_pool.get()).release(var8);
               }

               var1.setX(var6.x);
               var1.setY(var6.y);
               var1.setZ(0.0F);
               ((BaseVehicle.Vector3fObjectPool)TL_vector3f_pool.get()).release(var6);
            }

            if (var1 instanceof IsoPlayer && ((IsoPlayer)var1).isLocalPlayer()) {
               ((IsoPlayer)var1).dirtyRecalcGridStackTime = 10.0F;
            }

         }
      }
   }

   public void transmitCharacterPosition(int var1, String var2) {
      if (GameClient.bClient) {
         VehicleManager.instance.sendPassengerPosition(this, var1, var2);
      }

   }

   public void setCharacterPositionToAnim(IsoGameCharacter var1, int var2, String var3) {
      VehicleScript.Anim var4 = this.getPassengerAnim(var2, var3);
      if (var4 != null) {
         if (this.getCharacter(var2) == var1) {
            this.passengers[var2].offset.set((Vector3fc)var4.offset);
         } else {
            Vector3f var5 = this.getWorldPos(var4.offset, (Vector3f)((BaseVehicle.Vector3fObjectPool)TL_vector3f_pool.get()).alloc());
            var1.setX(var5.x);
            var1.setY(var5.y);
            var1.setZ(0.0F);
            ((BaseVehicle.Vector3fObjectPool)TL_vector3f_pool.get()).release(var5);
         }

      }
   }

   public int getPassengerSwitchSeatCount(int var1) {
      VehicleScript.Passenger var2 = this.getScriptPassenger(var1);
      return var2 == null ? -1 : var2.switchSeats.size();
   }

   public VehicleScript.Passenger.SwitchSeat getPassengerSwitchSeat(int var1, int var2) {
      VehicleScript.Passenger var3 = this.getScriptPassenger(var1);
      if (var3 == null) {
         return null;
      } else {
         return var2 >= 0 && var2 < var3.switchSeats.size() ? (VehicleScript.Passenger.SwitchSeat)var3.switchSeats.get(var2) : null;
      }
   }

   private VehicleScript.Passenger.SwitchSeat getSwitchSeat(int var1, int var2) {
      VehicleScript.Passenger var3 = this.getScriptPassenger(var1);
      if (var3 == null) {
         return null;
      } else {
         for(int var4 = 0; var4 < var3.switchSeats.size(); ++var4) {
            VehicleScript.Passenger.SwitchSeat var5 = (VehicleScript.Passenger.SwitchSeat)var3.switchSeats.get(var4);
            if (var5.seat == var2 && this.getPartForSeatContainer(var2) != null && this.getPartForSeatContainer(var2).getInventoryItem() != null) {
               return var5;
            }
         }

         return null;
      }
   }

   public String getSwitchSeatAnimName(int var1, int var2) {
      VehicleScript.Passenger.SwitchSeat var3 = this.getSwitchSeat(var1, var2);
      return var3 == null ? null : var3.anim;
   }

   public float getSwitchSeatAnimRate(int var1, int var2) {
      VehicleScript.Passenger.SwitchSeat var3 = this.getSwitchSeat(var1, var2);
      return var3 == null ? 0.0F : var3.rate;
   }

   public String getSwitchSeatSound(int var1, int var2) {
      VehicleScript.Passenger.SwitchSeat var3 = this.getSwitchSeat(var1, var2);
      return var3 == null ? null : var3.sound;
   }

   public boolean canSwitchSeat(int var1, int var2) {
      VehicleScript.Passenger.SwitchSeat var3 = this.getSwitchSeat(var1, var2);
      return var3 != null;
   }

   public void switchSeat(IsoGameCharacter var1, int var2) {
      int var3 = this.getSeat(var1);
      if (var3 != -1) {
         this.clearPassenger(var3);
         VehicleScript.Position var4 = this.getPassengerPosition(var2, "inside");
         if (var4 == null) {
            Vector3f var5 = (Vector3f)((BaseVehicle.Vector3fObjectPool)TL_vector3f_pool.get()).alloc();
            var5.set(0.0F, 0.0F, 0.0F);
            this.setPassenger(var2, var1, var5);
            ((BaseVehicle.Vector3fObjectPool)TL_vector3f_pool.get()).release(var5);
         } else {
            this.setPassenger(var2, var1, var4.offset);
         }

         VehicleManager.instance.sendSwichSeat(this, var2, var1);
      }
   }

   public void switchSeatRSync(IsoGameCharacter var1, int var2) {
      int var3 = this.getSeat(var1);
      if (var3 != -1) {
         this.clearPassenger(var3);
         VehicleScript.Position var4 = this.getPassengerPosition(var2, "inside");
         if (var4 == null) {
            Vector3f var5 = (Vector3f)((BaseVehicle.Vector3fObjectPool)TL_vector3f_pool.get()).alloc();
            var5.set(0.0F, 0.0F, 0.0F);
            this.setPassenger(var2, var1, var5);
            ((BaseVehicle.Vector3fObjectPool)TL_vector3f_pool.get()).release(var5);
         } else {
            this.setPassenger(var2, var1, var4.offset);
         }

      }
   }

   public void playSwitchSeatAnim(int var1, int var2) {
      IsoGameCharacter var3 = this.getCharacter(var1);
      if (var3 != null) {
         VehicleScript.Passenger.SwitchSeat var4 = this.getSwitchSeat(var1, var2);
         if (var4 != null) {
            var3.PlayAnimUnlooped(var4.anim);
            var3.getSpriteDef().setFrameSpeedPerFrame(var4.rate);
            var3.getLegsSprite().Animate = true;
         }
      }
   }

   public boolean isSeatOccupied(int var1) {
      VehiclePart var2 = this.getPartForSeatContainer(var1);
      if (var2 != null && var2.getItemContainer() != null && !var2.getItemContainer().getItems().isEmpty()) {
         return true;
      } else {
         return this.getCharacter(var1) != null;
      }
   }

   public boolean isSeatInstalled(int var1) {
      VehiclePart var2 = this.getPartForSeatContainer(var1);
      return var2 != null && var2.getInventoryItem() != null;
   }

   public int getBestSeat(IsoGameCharacter var1) {
      if ((int)this.getZ() != (int)var1.getZ()) {
         return -1;
      } else if (var1.DistTo(this) > 5.0F) {
         return -1;
      } else {
         VehicleScript var2 = this.getScript();
         if (var2 == null) {
            return -1;
         } else {
            Vector3f var3 = (Vector3f)((BaseVehicle.Vector3fObjectPool)TL_vector3f_pool.get()).alloc();
            Vector3f var4 = (Vector3f)((BaseVehicle.Vector3fObjectPool)TL_vector3f_pool.get()).alloc();
            Vector2 var5 = (Vector2)((BaseVehicle.Vector2ObjectPool)TL_vector2_pool.get()).alloc();

            for(int var6 = 0; var6 < var2.getPassengerCount(); ++var6) {
               if (!this.isEnterBlocked(var1, var6) && !this.isSeatOccupied(var6)) {
                  VehicleScript.Position var7 = this.getPassengerPosition(var6, "outside");
                  float var8;
                  float var9;
                  float var10;
                  if (var7 != null) {
                     this.getPassengerPositionWorldPos(var7, var3);
                     var8 = var3.x;
                     var9 = var3.y;
                     this.getPassengerPositionWorldPos(0.0F, var7.offset.y, var7.offset.z, var4);
                     var5.set(var4.x - var1.getX(), var4.y - var1.getY());
                     var5.normalize();
                     var10 = var5.dot(var1.getForwardDirection());
                     if (var10 > 0.5F && IsoUtils.DistanceTo(var1.getX(), var1.getY(), var8, var9) < 1.0F) {
                        ((BaseVehicle.Vector3fObjectPool)TL_vector3f_pool.get()).release(var3);
                        ((BaseVehicle.Vector3fObjectPool)TL_vector3f_pool.get()).release(var4);
                        ((BaseVehicle.Vector2ObjectPool)TL_vector2_pool.get()).release(var5);
                        return var6;
                     }
                  }

                  var7 = this.getPassengerPosition(var6, "outside2");
                  if (var7 != null) {
                     this.getPassengerPositionWorldPos(var7, var3);
                     var8 = var3.x;
                     var9 = var3.y;
                     this.getPassengerPositionWorldPos(0.0F, var7.offset.y, var7.offset.z, var4);
                     var5.set(var4.x - var1.getX(), var4.y - var1.getY());
                     var5.normalize();
                     var10 = var5.dot(var1.getForwardDirection());
                     if (var10 > 0.5F && IsoUtils.DistanceTo(var1.getX(), var1.getY(), var8, var9) < 1.0F) {
                        ((BaseVehicle.Vector3fObjectPool)TL_vector3f_pool.get()).release(var3);
                        ((BaseVehicle.Vector3fObjectPool)TL_vector3f_pool.get()).release(var4);
                        ((BaseVehicle.Vector2ObjectPool)TL_vector2_pool.get()).release(var5);
                        return var6;
                     }
                  }
               }
            }

            ((BaseVehicle.Vector3fObjectPool)TL_vector3f_pool.get()).release(var3);
            ((BaseVehicle.Vector3fObjectPool)TL_vector3f_pool.get()).release(var4);
            ((BaseVehicle.Vector2ObjectPool)TL_vector2_pool.get()).release(var5);
            return -1;
         }
      }
   }

   public void updateHasExtendOffsetForExit(IsoGameCharacter var1) {
      this.hasExtendOffsetExiting = true;
      this.updateHasExtendOffset(var1);
      this.getPoly();
   }

   public void updateHasExtendOffsetForExitEnd(IsoGameCharacter var1) {
      this.hasExtendOffsetExiting = false;
      this.updateHasExtendOffset(var1);
      this.getPoly();
   }

   public void updateHasExtendOffset(IsoGameCharacter var1) {
      this.hasExtendOffset = false;
      this.hasExtendOffsetExiting = false;
   }

   public VehiclePart getUseablePart(IsoGameCharacter var1) {
      return this.getUseablePart(var1, true);
   }

   public VehiclePart getUseablePart(IsoGameCharacter var1, boolean var2) {
      if (var1.getVehicle() != null) {
         return null;
      } else if ((int)this.getZ() != (int)var1.getZ()) {
         return null;
      } else if (var1.DistTo(this) > 6.0F) {
         return null;
      } else {
         VehicleScript var3 = this.getScript();
         if (var3 == null) {
            return null;
         } else {
            Vector3f var4 = var3.getExtents();
            Vector3f var5 = var3.getCenterOfMassOffset();
            float var6 = var5.z - var4.z / 2.0F;
            float var7 = var5.z + var4.z / 2.0F;
            Vector2 var8 = (Vector2)((BaseVehicle.Vector2ObjectPool)TL_vector2_pool.get()).alloc();
            Vector3f var9 = (Vector3f)((BaseVehicle.Vector3fObjectPool)TL_vector3f_pool.get()).alloc();

            for(int var10 = 0; var10 < this.parts.size(); ++var10) {
               VehiclePart var11 = (VehiclePart)this.parts.get(var10);
               if (var11.getArea() != null && this.isInArea(var11.getArea(), var1)) {
                  String var12 = var11.getLuaFunction("use");
                  if (var12 != null && !var12.equals("")) {
                     VehicleScript.Area var13 = var3.getAreaById(var11.getArea());
                     if (var13 != null) {
                        Vector2 var14 = this.areaPositionLocal(var13, var8);
                        if (var14 != null) {
                           float var15 = 0.0F;
                           float var16 = 0.0F;
                           float var17 = 0.0F;
                           if (!(var14.y >= var7) && !(var14.y <= var6)) {
                              var17 = var14.y;
                           } else {
                              var15 = var14.x;
                           }

                           if (!var2) {
                              return var11;
                           }

                           this.getWorldPos(var15, var16, var17, var9);
                           var8.set(var9.x - var1.getX(), var9.y - var1.getY());
                           var8.normalize();
                           float var19 = var8.dot(var1.getForwardDirection());
                           if (var19 > 0.5F && !PolygonalMap2.instance.lineClearCollide(var1.x, var1.y, var9.x, var9.y, (int)var1.z, this, false, true)) {
                              ((BaseVehicle.Vector2ObjectPool)TL_vector2_pool.get()).release(var8);
                              ((BaseVehicle.Vector3fObjectPool)TL_vector3f_pool.get()).release(var9);
                              return var11;
                           }
                           break;
                        }
                     }
                  }
               }
            }

            ((BaseVehicle.Vector2ObjectPool)TL_vector2_pool.get()).release(var8);
            ((BaseVehicle.Vector3fObjectPool)TL_vector3f_pool.get()).release(var9);
            return null;
         }
      }
   }

   public VehiclePart getClosestWindow(IsoGameCharacter var1) {
      if ((int)this.getZ() != (int)var1.getZ()) {
         return null;
      } else if (var1.DistTo(this) > 5.0F) {
         return null;
      } else {
         Vector3f var2 = this.script.getExtents();
         Vector3f var3 = this.script.getCenterOfMassOffset();
         float var4 = var3.z - var2.z / 2.0F;
         float var5 = var3.z + var2.z / 2.0F;
         Vector2 var6 = (Vector2)((BaseVehicle.Vector2ObjectPool)TL_vector2_pool.get()).alloc();
         Vector3f var7 = (Vector3f)((BaseVehicle.Vector3fObjectPool)TL_vector3f_pool.get()).alloc();

         for(int var8 = 0; var8 < this.parts.size(); ++var8) {
            VehiclePart var9 = (VehiclePart)this.parts.get(var8);
            if (var9.getWindow() != null && var9.getArea() != null && this.isInArea(var9.getArea(), var1)) {
               VehicleScript.Area var10 = this.script.getAreaById(var9.getArea());
               if (!(var10.y >= var5) && !(var10.y <= var4)) {
                  var7.set(0.0F, 0.0F, var10.y);
               } else {
                  var7.set(var10.x, 0.0F, 0.0F);
               }

               this.getWorldPos(var7, var7);
               var6.set(var7.x - var1.getX(), var7.y - var1.getY());
               var6.normalize();
               float var12 = var6.dot(var1.getForwardDirection());
               if (var12 > 0.5F) {
                  ((BaseVehicle.Vector2ObjectPool)TL_vector2_pool.get()).release(var6);
                  ((BaseVehicle.Vector3fObjectPool)TL_vector3f_pool.get()).release(var7);
                  return var9;
               }
               break;
            }
         }

         ((BaseVehicle.Vector2ObjectPool)TL_vector2_pool.get()).release(var6);
         ((BaseVehicle.Vector3fObjectPool)TL_vector3f_pool.get()).release(var7);
         return null;
      }
   }

   public void getFacingPosition(IsoGameCharacter var1, Vector2 var2) {
      Vector3f var3 = this.getLocalPos(var1.getX(), var1.getY(), var1.getZ(), (Vector3f)((BaseVehicle.Vector3fObjectPool)TL_vector3f_pool.get()).alloc());
      Vector3f var4 = this.script.getExtents();
      Vector3f var5 = this.script.getCenterOfMassOffset();
      float var6 = var5.x - var4.x / 2.0F;
      float var7 = var5.x + var4.x / 2.0F;
      float var8 = var5.z - var4.z / 2.0F;
      float var9 = var5.z + var4.z / 2.0F;
      float var10 = 0.0F;
      float var11 = 0.0F;
      if (var3.x <= 0.0F && var3.z >= var8 && var3.z <= var9) {
         var11 = var3.z;
      } else if (var3.x > 0.0F && var3.z >= var8 && var3.z <= var9) {
         var11 = var3.z;
      } else if (var3.z <= 0.0F && var3.x >= var6 && var3.x <= var7) {
         var10 = var3.x;
      } else if (var3.z > 0.0F && var3.x >= var6 && var3.x <= var7) {
         var10 = var3.x;
      }

      this.getWorldPos(var10, 0.0F, var11, var3);
      var2.set(var3.x, var3.y);
      ((BaseVehicle.Vector3fObjectPool)TL_vector3f_pool.get()).release(var3);
   }

   public boolean enter(int var1, IsoGameCharacter var2, Vector3f var3) {
      if (!GameClient.bClient) {
         VehiclesDB2.instance.updateVehicleAndTrailer(this);
      }

      if (var2 == null) {
         return false;
      } else if (var2.getVehicle() != null && !var2.getVehicle().exit(var2)) {
         return false;
      } else if (this.setPassenger(var1, var2, var3)) {
         var2.setVehicle(this);
         var2.setCollidable(false);
         if (GameClient.bClient) {
            VehicleManager.instance.sendEnter(this, var1, var2);
         }

         if (var2 instanceof IsoPlayer && ((IsoPlayer)var2).isLocalPlayer()) {
            ((IsoPlayer)var2).dirtyRecalcGridStackTime = 10.0F;
         }

         return true;
      } else {
         return false;
      }
   }

   public boolean enter(int var1, IsoGameCharacter var2) {
      if (this.getPartForSeatContainer(var1) != null && this.getPartForSeatContainer(var1).getInventoryItem() != null) {
         VehicleScript.Position var3 = this.getPassengerPosition(var1, "outside");
         return var3 != null ? this.enter(var1, var2, var3.offset) : false;
      } else {
         return false;
      }
   }

   public boolean enterRSync(int var1, IsoGameCharacter var2, BaseVehicle var3) {
      if (var2 == null) {
         return false;
      } else {
         VehicleScript.Position var4 = this.getPassengerPosition(var1, "inside");
         if (var4 != null) {
            if (this.setPassenger(var1, var2, var4.offset)) {
               var2.setVehicle(var3);
               var2.setCollidable(false);
               if (GameClient.bClient) {
                  LuaEventManager.triggerEvent("OnContainerUpdate");
               }

               return true;
            } else {
               return false;
            }
         } else {
            return false;
         }
      }
   }

   public boolean exit(IsoGameCharacter var1) {
      if (!GameClient.bClient) {
         VehiclesDB2.instance.updateVehicleAndTrailer(this);
      }

      if (var1 == null) {
         return false;
      } else {
         int var2 = this.getSeat(var1);
         if (var2 == -1) {
            return false;
         } else if (this.clearPassenger(var2)) {
            this.enginePower = (int)this.getScript().getEngineForce();
            var1.setVehicle((BaseVehicle)null);
            var1.savedVehicleSeat = -1;
            var1.setCollidable(true);
            if (GameClient.bClient) {
               VehicleManager.instance.sendExit(this, var1);
            }

            if (this.getDriver() == null && this.soundHornOn) {
               this.onHornStop();
            }

            this.polyGarageCheck = true;
            this.polyDirty = true;
            return true;
         } else {
            return false;
         }
      }
   }

   public boolean exitRSync(IsoGameCharacter var1) {
      if (var1 == null) {
         return false;
      } else {
         int var2 = this.getSeat(var1);
         if (var2 == -1) {
            return false;
         } else if (this.clearPassenger(var2)) {
            var1.setVehicle((BaseVehicle)null);
            var1.setCollidable(true);
            if (GameClient.bClient) {
               LuaEventManager.triggerEvent("OnContainerUpdate");
            }

            return true;
         } else {
            return false;
         }
      }
   }

   public boolean hasRoof(int var1) {
      VehicleScript.Passenger var2 = this.getScriptPassenger(var1);
      return var2 == null ? false : var2.hasRoof;
   }

   public boolean showPassenger(int var1) {
      VehicleScript.Passenger var2 = this.getScriptPassenger(var1);
      return var2 == null ? false : var2.showPassenger;
   }

   public boolean showPassenger(IsoGameCharacter var1) {
      int var2 = this.getSeat(var1);
      return this.showPassenger(var2);
   }

   public void save(ByteBuffer var1, boolean var2) throws IOException {
      if (this.square != null) {
         float var3 = 5.0E-4F;
         this.x = PZMath.clamp(this.x, (float)this.square.x + var3, (float)(this.square.x + 1) - var3);
         this.y = PZMath.clamp(this.y, (float)this.square.y + var3, (float)(this.square.y + 1) - var3);
      }

      super.save(var1, var2);
      Quaternionf var7 = this.savedRot;
      Transform var4 = this.getWorldTransform(this.tempTransform);
      var1.putFloat(var4.origin.y);
      var4.getRotation(var7);
      var1.putFloat(var7.x);
      var1.putFloat(var7.y);
      var1.putFloat(var7.z);
      var1.putFloat(var7.w);
      GameWindow.WriteStringUTF(var1, this.scriptName);
      var1.putInt(this.skinIndex);
      var1.put((byte)(this.isEngineRunning() ? 1 : 0));
      var1.putInt(this.frontEndDurability);
      var1.putInt(this.rearEndDurability);
      var1.putInt(this.currentFrontEndDurability);
      var1.putInt(this.currentRearEndDurability);
      var1.putInt(this.engineLoudness);
      var1.putInt(this.engineQuality);
      var1.putInt(this.keyId);
      var1.put(this.keySpawned);
      var1.put((byte)(this.headlightsOn ? 1 : 0));
      var1.put((byte)(this.bCreated ? 1 : 0));
      var1.put((byte)(this.soundHornOn ? 1 : 0));
      var1.put((byte)(this.soundBackMoveOn ? 1 : 0));
      var1.put((byte)this.lightbarLightsMode.get());
      var1.put((byte)this.lightbarSirenMode.get());
      var1.putShort((short)this.parts.size());

      for(int var5 = 0; var5 < this.parts.size(); ++var5) {
         VehiclePart var6 = (VehiclePart)this.parts.get(var5);
         var6.save(var1);
      }

      var1.put((byte)(this.keyIsOnDoor ? 1 : 0));
      var1.put((byte)(this.hotwired ? 1 : 0));
      var1.put((byte)(this.hotwiredBroken ? 1 : 0));
      var1.put((byte)(this.keysInIgnition ? 1 : 0));
      var1.putFloat(this.rust);
      var1.putFloat(this.colorHue);
      var1.putFloat(this.colorSaturation);
      var1.putFloat(this.colorValue);
      var1.putInt(this.enginePower);
      var1.putShort(this.VehicleID);
      GameWindow.WriteString((ByteBuffer)var1, (String)null);
      var1.putInt(this.mechanicalID);
      var1.put((byte)(this.alarmed ? 1 : 0));
      var1.putDouble(this.sirenStartTime);
      if (this.getCurrentKey() != null) {
         var1.put((byte)1);
         this.getCurrentKey().saveWithSize(var1, false);
      } else {
         var1.put((byte)0);
      }

      var1.put((byte)this.bloodIntensity.size());
      Iterator var8 = this.bloodIntensity.entrySet().iterator();

      while(var8.hasNext()) {
         Entry var9 = (Entry)var8.next();
         GameWindow.WriteStringUTF(var1, (String)var9.getKey());
         var1.put((Byte)var9.getValue());
      }

      if (this.vehicleTowingID != -1) {
         var1.put((byte)1);
         var1.putInt(this.vehicleTowingID);
         GameWindow.WriteStringUTF(var1, this.towAttachmentSelf);
         GameWindow.WriteStringUTF(var1, this.towAttachmentOther);
         var1.putFloat(this.towConstraintZOffset);
      } else {
         var1.put((byte)0);
      }

   }

   public void load(ByteBuffer var1, int var2, boolean var3) throws IOException {
      super.load(var1, var2, var3);
      if (this.z < 0.0F) {
         this.z = 0.0F;
      }

      if (var2 >= 173) {
         this.savedPhysicsZ = PZMath.clamp(var1.getFloat(), 0.0F, (float)((int)this.z) + 2.4477F);
      }

      float var4 = var1.getFloat();
      float var5 = var1.getFloat();
      float var6 = var1.getFloat();
      float var7 = var1.getFloat();
      this.savedRot.set(var4, var5, var6, var7);
      this.jniTransform.origin.set(this.getX() - WorldSimulation.instance.offsetX, Float.isNaN(this.savedPhysicsZ) ? this.z : this.savedPhysicsZ, this.getY() - WorldSimulation.instance.offsetY);
      this.jniTransform.setRotation(this.savedRot);
      this.scriptName = GameWindow.ReadStringUTF(var1);
      this.skinIndex = var1.getInt();
      boolean var8 = var1.get() == 1;
      if (var8) {
         this.engineState = BaseVehicle.engineStateTypes.Running;
      }

      this.frontEndDurability = var1.getInt();
      this.rearEndDurability = var1.getInt();
      this.currentFrontEndDurability = var1.getInt();
      this.currentRearEndDurability = var1.getInt();
      this.engineLoudness = var1.getInt();
      this.engineQuality = var1.getInt();
      this.engineQuality = PZMath.clamp(this.engineQuality, 0, 100);
      this.keyId = var1.getInt();
      this.keySpawned = var1.get();
      this.headlightsOn = var1.get() == 1;
      this.bCreated = var1.get() == 1;
      this.soundHornOn = var1.get() == 1;
      this.soundBackMoveOn = var1.get() == 1;
      this.lightbarLightsMode.set(var1.get());
      this.lightbarSirenMode.set(var1.get());
      short var9 = var1.getShort();

      for(int var10 = 0; var10 < var9; ++var10) {
         VehiclePart var11 = new VehiclePart(this);
         var11.load(var1, var2);
         this.parts.add(var11);
      }

      if (var2 >= 112) {
         this.keyIsOnDoor = var1.get() == 1;
         this.hotwired = var1.get() == 1;
         this.hotwiredBroken = var1.get() == 1;
         this.keysInIgnition = var1.get() == 1;
      }

      if (var2 >= 116) {
         this.rust = var1.getFloat();
         this.colorHue = var1.getFloat();
         this.colorSaturation = var1.getFloat();
         this.colorValue = var1.getFloat();
      }

      if (var2 >= 117) {
         this.enginePower = var1.getInt();
      }

      if (var2 >= 120) {
         var1.getShort();
      }

      if (var2 >= 122) {
         String var15 = GameWindow.ReadString(var1);
         this.mechanicalID = var1.getInt();
      }

      if (var2 >= 124) {
         this.alarmed = var1.get() == 1;
      }

      if (var2 >= 129) {
         this.sirenStartTime = var1.getDouble();
      }

      if (var2 >= 133 && var1.get() == 1) {
         InventoryItem var16 = null;

         try {
            var16 = InventoryItem.loadItem(var1, var2);
         } catch (Exception var14) {
            var14.printStackTrace();
         }

         if (var16 != null) {
            this.setCurrentKey(var16);
         }
      }

      if (var2 >= 165) {
         byte var18 = var1.get();

         for(int var17 = 0; var17 < var18; ++var17) {
            String var12 = GameWindow.ReadStringUTF(var1);
            byte var13 = var1.get();
            this.bloodIntensity.put(var12, var13);
         }
      }

      if (var2 >= 174) {
         if (var1.get() == 1) {
            this.vehicleTowingID = var1.getInt();
            this.towAttachmentSelf = GameWindow.ReadStringUTF(var1);
            this.towAttachmentOther = GameWindow.ReadStringUTF(var1);
            this.towConstraintZOffset = var1.getFloat();
         }
      } else if (var2 >= 172) {
         this.vehicleTowingID = var1.getInt();
      }

      this.loaded = true;
   }

   public void softReset() {
      this.keySpawned = 0;
      this.keyIsOnDoor = false;
      this.keysInIgnition = false;
      this.currentKey = null;
      this.engineState = BaseVehicle.engineStateTypes.Idle;
      this.randomizeContainers();
   }

   public void trySpawnKey() {
      if (!GameClient.bClient) {
         if (this.script != null && this.script.getPartById("Engine") != null) {
            if (this.keySpawned != 1) {
               if (SandboxOptions.getInstance().VehicleEasyUse.getValue()) {
                  this.addKeyToGloveBox();
               } else {
                  VehicleType var1 = VehicleType.getTypeFromName(this.getVehicleType());
                  int var2 = var1 == null ? 70 : var1.getChanceToSpawnKey();
                  if (Rand.Next(100) <= var2) {
                     this.addKeyToWorld();
                  }

                  this.keySpawned = 1;
               }
            }
         }
      }
   }

   public boolean shouldCollideWithCharacters() {
      if (this.vehicleTowedBy != null) {
         return this.vehicleTowedBy.shouldCollideWithCharacters();
      } else {
         float var1 = this.getSpeed2D();
         return this.isEngineRunning() ? var1 > 0.05F : var1 > 1.0F;
      }
   }

   public boolean shouldCollideWithObjects() {
      return this.vehicleTowedBy != null ? this.vehicleTowedBy.shouldCollideWithObjects() : this.isEngineRunning();
   }

   public void brekingObjects() {
      boolean var1 = this.shouldCollideWithCharacters();
      boolean var2 = this.shouldCollideWithObjects();
      if (var1 || var2) {
         Vector3f var3 = this.script.getExtents();
         Vector2 var4 = (Vector2)((BaseVehicle.Vector2ObjectPool)TL_vector2_pool.get()).alloc();
         float var5 = Math.max(var3.x / 2.0F, var3.z / 2.0F) + 0.3F + 1.0F;
         int var6 = (int)Math.ceil((double)var5);

         int var8;
         for(int var7 = -var6; var7 < var6; ++var7) {
            for(var8 = -var6; var8 < var6; ++var8) {
               IsoGridSquare var9 = this.getCell().getGridSquare((double)(this.x + (float)var8), (double)(this.y + (float)var7), (double)this.z);
               if (var9 != null) {
                  int var10;
                  if (var2) {
                     for(var10 = 0; var10 < var9.getObjects().size(); ++var10) {
                        IsoObject var11 = (IsoObject)var9.getObjects().get(var10);
                        if (!(var11 instanceof IsoWorldInventoryObject)) {
                           Vector2 var12 = null;
                           if (!this.brekingObjectsList.contains(var11) && var11 != null && var11.getProperties() != null) {
                              if (var11.getProperties().Is("CarSlowFactor")) {
                                 var12 = this.testCollisionWithObject(var11, 0.3F, var4);
                              }

                              if (var12 != null) {
                                 this.brekingObjectsList.add(var11);
                                 if (!GameClient.bClient) {
                                    var11.Collision(var12, this);
                                 }
                              }

                              if (var11.getProperties().Is("HitByCar")) {
                                 var12 = this.testCollisionWithObject(var11, 0.3F, var4);
                              }

                              if (var12 != null && !GameClient.bClient) {
                                 var11.Collision(var12, this);
                              }

                              this.checkCollisionWithPlant(var9, var11, var4);
                           }
                        }
                     }
                  }

                  IsoMovingObject var16;
                  if (var1) {
                     for(var10 = 0; var10 < var9.getMovingObjects().size(); ++var10) {
                        var16 = (IsoMovingObject)var9.getMovingObjects().get(var10);
                        IsoZombie var18 = (IsoZombie)Type.tryCastTo(var16, IsoZombie.class);
                        if (var18 != null) {
                           if (var18.isProne()) {
                              this.testCollisionWithProneCharacter(var18, false);
                           }

                           var18.setVehicle4TestCollision(this);
                        }

                        if (var16 instanceof IsoPlayer && var16 != this.getDriver()) {
                           IsoPlayer var13 = (IsoPlayer)var16;
                           var13.setVehicle4TestCollision(this);
                        }
                     }
                  }

                  if (var2) {
                     for(var10 = 0; var10 < var9.getStaticMovingObjects().size(); ++var10) {
                        var16 = (IsoMovingObject)var9.getStaticMovingObjects().get(var10);
                        IsoDeadBody var19 = (IsoDeadBody)Type.tryCastTo(var16, IsoDeadBody.class);
                        if (var19 != null) {
                           this.testCollisionWithCorpse(var19, true);
                        }
                     }
                  }
               }
            }
         }

         float var14 = -999.0F;

         for(var8 = 0; var8 < this.brekingObjectsList.size(); ++var8) {
            IsoObject var15 = (IsoObject)this.brekingObjectsList.get(var8);
            Vector2 var17 = this.testCollisionWithObject(var15, 1.0F, var4);
            if (var17 != null && var15.getSquare().getObjects().contains(var15)) {
               if (var14 < var15.GetVehicleSlowFactor(this)) {
                  var14 = var15.GetVehicleSlowFactor(this);
               }
            } else {
               this.brekingObjectsList.remove(var15);
               var15.UnCollision(this);
            }
         }

         if (var14 != -999.0F) {
            this.brekingSlowFactor = PZMath.clamp(var14, 0.0F, 34.0F);
         } else {
            this.brekingSlowFactor = 0.0F;
         }

         ((BaseVehicle.Vector2ObjectPool)TL_vector2_pool.get()).release(var4);
      }
   }

   private void updateVelocityMultiplier() {
      if (this.physics != null && this.getScript() != null) {
         Vector3f var1 = this.getLinearVelocity((Vector3f)((BaseVehicle.Vector3fObjectPool)TL_vector3f_pool.get()).alloc());
         var1.y = 0.0F;
         float var2 = var1.length();
         float var3 = 100000.0F;
         float var4 = 1.0F;
         if (this.getScript().getWheelCount() > 0) {
            if (var2 > 0.0F && var2 > 34.0F - this.brekingSlowFactor) {
               var3 = 34.0F - this.brekingSlowFactor;
               var4 = (34.0F - this.brekingSlowFactor) / var2;
            }
         } else if (this.getVehicleTowedBy() == null) {
            var3 = 0.0F;
            var4 = 0.1F;
         }

         Bullet.setVehicleVelocityMultiplier(this.VehicleID, var3, var4);
         ((BaseVehicle.Vector3fObjectPool)TL_vector3f_pool.get()).release(var1);
      }
   }

   private void playScrapePastPlantSound(IsoGridSquare var1) {
      if (this.emitter != null && !this.emitter.isPlaying(this.soundScrapePastPlant)) {
         this.emitter.setPos((float)var1.x + 0.5F, (float)var1.y + 0.5F, (float)var1.z);
         this.soundScrapePastPlant = this.emitter.playSoundImpl("VehicleScrapePastPlant", var1);
      }

   }

   private void checkCollisionWithPlant(IsoGridSquare var1, IsoObject var2, Vector2 var3) {
      IsoTree var4 = (IsoTree)Type.tryCastTo(var2, IsoTree.class);
      if (var4 != null || var2.getProperties().Is("Bush")) {
         float var5 = Math.abs(this.getCurrentSpeedKmHour());
         if (!(var5 <= 1.0F)) {
            Vector2 var6 = this.testCollisionWithObject(var2, 0.3F, var3);
            if (var6 != null) {
               if (var4 != null && var4.getSize() == 1) {
                  this.ApplyImpulse4Break(var2, 0.025F);
                  this.playScrapePastPlantSound(var1);
               } else if (this.isPositionOnLeftOrRight(var6.x, var6.y)) {
                  this.ApplyImpulse4Break(var2, 0.025F);
                  this.playScrapePastPlantSound(var1);
               } else if (var5 < 10.0F) {
                  this.ApplyImpulse4Break(var2, 0.025F);
                  this.playScrapePastPlantSound(var1);
               } else {
                  this.ApplyImpulse4Break(var2, 0.1F);
                  this.playScrapePastPlantSound(var1);
               }
            }
         }
      }
   }

   public void damageObjects(float var1) {
      if (this.isEngineRunning()) {
         Vector3f var2 = this.script.getExtents();
         Vector2 var3 = (Vector2)((BaseVehicle.Vector2ObjectPool)TL_vector2_pool.get()).alloc();
         float var4 = Math.max(var2.x / 2.0F, var2.z / 2.0F) + 0.3F + 1.0F;
         int var5 = (int)Math.ceil((double)var4);

         for(int var6 = -var5; var6 < var5; ++var6) {
            for(int var7 = -var5; var7 < var5; ++var7) {
               IsoGridSquare var8 = this.getCell().getGridSquare((double)(this.x + (float)var7), (double)(this.y + (float)var6), (double)this.z);
               if (var8 != null) {
                  for(int var9 = 0; var9 < var8.getObjects().size(); ++var9) {
                     IsoObject var10 = (IsoObject)var8.getObjects().get(var9);
                     Vector2 var11 = null;
                     if (var10 instanceof IsoTree) {
                        var11 = this.testCollisionWithObject(var10, 2.0F, var3);
                        if (var11 != null) {
                           var10.setRenderEffect(RenderEffectType.Hit_Tree_Shudder);
                        }
                     }

                     if (var11 == null && var10 instanceof IsoWindow) {
                        var11 = this.testCollisionWithObject(var10, 1.0F, var3);
                     }

                     if (var11 == null && var10.sprite != null && (var10.sprite.getProperties().Is("HitByCar") || var10.sprite.getProperties().Is("CarSlowFactor"))) {
                        var11 = this.testCollisionWithObject(var10, 1.0F, var3);
                     }

                     IsoGridSquare var12;
                     if (var11 == null) {
                        var12 = this.getCell().getGridSquare((double)(this.x + (float)var7), (double)(this.y + (float)var6), 1.0D);
                        if (var12 != null && var12.getHasTypes().isSet(IsoObjectType.lightswitch)) {
                           var11 = this.testCollisionWithObject(var10, 1.0F, var3);
                        }
                     }

                     if (var11 == null) {
                        var12 = this.getCell().getGridSquare((double)(this.x + (float)var7), (double)(this.y + (float)var6), 0.0D);
                        if (var12 != null && var12.getHasTypes().isSet(IsoObjectType.lightswitch)) {
                           var11 = this.testCollisionWithObject(var10, 1.0F, var3);
                        }
                     }

                     if (var11 != null) {
                        var10.Hit(var11, this, var1);
                     }
                  }
               }
            }
         }

         ((BaseVehicle.Vector2ObjectPool)TL_vector2_pool.get()).release(var3);
      }
   }

   public void update() {
      if (this.removedFromWorld) {
         DebugLog.log("vehicle update() removedFromWorld=true id=" + this.VehicleID);
      } else if (!this.getCell().vehicles.contains(this)) {
         DebugLog.log("vehicle update() not in cell.vehicles list x,y=" + this.x + "," + this.y + " " + this);
         this.getCell().getRemoveList().add(this);
      } else {
         if (this.chunk == null) {
            DebugLog.log("vehicle update() chunk=null x,y=" + this.x + "," + this.y + " id=" + this.VehicleID);
         } else if (!this.chunk.vehicles.contains(this)) {
            DebugLog.log("vehicle update() not in chunk.vehicles list x,y=" + this.x + "," + this.y + " id=" + this.VehicleID);
            if (GameClient.bClient) {
               VehicleManager.instance.sendReqestGetPosition(this.VehicleID);
            }
         } else if (!GameServer.bServer && this.chunk.refs.isEmpty()) {
            DebugLog.log("vehicle update() chunk was unloaded id=" + this.VehicleID);
            this.removeFromWorld();
            return;
         }

         super.update();
         if (GameClient.bClient && (this.netPlayerAuthorization == 1 || this.netPlayerAuthorization == 3) && GameClient.connection != null) {
            this.updatePhysicsNetwork();
         }

         float var1;
         if (this.getVehicleTowing() != null && this.getDriver() != null) {
            var1 = 2.5F;
            if (this.getVehicleTowing().getPartCount() == 0) {
               var1 = 12.0F;
            }
         }

         if (this.getVehicleTowedBy() != null && this.getDriver() != null) {
            var1 = 2.5F;
            if (this.getVehicleTowedBy().getPartCount() == 0) {
               var1 = 12.0F;
            }
         }

         if (this.physics != null && this.vehicleTowingID != -1 && this.vehicleTowing == null) {
            this.tryReconnectToTowedVehicle();
         }

         boolean var18 = false;
         boolean var2 = false;
         if (this.getVehicleTowedBy() != null && this.getVehicleTowedBy().getController() != null) {
            var18 = this.getVehicleTowedBy() != null && this.getVehicleTowedBy().getController().isEnable;
            var2 = this.getVehicleTowing() != null && this.getVehicleTowing().getDriver() != null;
         }

         if (this.physics != null) {
            boolean var3 = this.getDriver() != null || var18 || var2;
            long var4 = System.currentTimeMillis();
            if (this.constraintChangedTime != -1L) {
               if (this.constraintChangedTime + 3500L < var4) {
                  this.constraintChangedTime = -1L;
                  if (!var3 && this.physicActiveCheck < var4) {
                     this.setPhysicsActive(false);
                  }
               }
            } else {
               if (this.physicActiveCheck != -1L && (var3 || !this.physics.isEnable)) {
                  this.physicActiveCheck = -1L;
               }

               if (!var3 && this.physics.isEnable && this.physicActiveCheck != -1L && this.physicActiveCheck < var4) {
                  this.physicActiveCheck = -1L;
                  this.setPhysicsActive(false);
               }
            }

            if (this.getVehicleTowedBy() != null && this.getScript().getWheelCount() > 0) {
               this.physics.updateTrailer();
            } else if (this.getDriver() == null) {
               this.physics.checkShouldBeActive();
            }

            this.doAlarm();
            BaseVehicle.VehicleImpulse var6 = this.impulseFromServer;
            if (var6 != null && var6.enable) {
               var6.enable = false;
               float var7 = 1.0F;
               Bullet.applyCentralForceToVehicle(this.VehicleID, var6.impulse.x * var7, var6.impulse.y * var7, var6.impulse.z * var7);
               Vector3f var8 = var6.rel_pos.cross(var6.impulse, (Vector3f)((BaseVehicle.Vector3fObjectPool)TL_vector3f_pool.get()).alloc());
               Bullet.applyTorqueToVehicle(this.VehicleID, var8.x * var7, var8.y * var7, var8.z * var7);
               ((BaseVehicle.Vector3fObjectPool)TL_vector3f_pool.get()).release(var8);
            }

            this.applyImpulseFromHitZombies();
            this.applyImpulseFromProneCharacters();
            short var26 = 1000;
            int var11;
            if (System.currentTimeMillis() - this.engineCheckTime > (long)var26 && !GameClient.bClient) {
               this.engineCheckTime = System.currentTimeMillis();
               if (!GameClient.bClient) {
                  if (this.engineState != BaseVehicle.engineStateTypes.Idle) {
                     int var27 = (int)((double)this.engineLoudness * this.engineSpeed / 2500.0D);
                     double var9 = Math.min(this.getEngineSpeed(), 2000.0D);
                     var27 = (int)((double)var27 * (1.0D + var9 / 4000.0D));
                     var11 = 120;
                     if (GameServer.bServer) {
                        var11 = (int)((double)var11 * ServerOptions.getInstance().CarEngineAttractionModifier.getValue());
                        var27 = (int)((double)var27 * ServerOptions.getInstance().CarEngineAttractionModifier.getValue());
                     }

                     if (Rand.Next((int)((float)var11 * GameTime.instance.getInvMultiplier())) == 0) {
                        WorldSoundManager.instance.addSoundRepeating(this, (int)this.getX(), (int)this.getY(), (int)this.getZ(), Math.max(8, var27), var27 / 40, false);
                     }

                     if (Rand.Next((int)((float)(var11 - 85) * GameTime.instance.getInvMultiplier())) == 0) {
                        WorldSoundManager.instance.addSoundRepeating(this, (int)this.getX(), (int)this.getY(), (int)this.getZ(), Math.max(8, var27 / 2), var27 / 40, false);
                     }

                     if (Rand.Next((int)((float)(var11 - 110) * GameTime.instance.getInvMultiplier())) == 0) {
                        WorldSoundManager.instance.addSoundRepeating(this, (int)this.getX(), (int)this.getY(), (int)this.getZ(), Math.max(8, var27 / 4), var27 / 40, false);
                     }

                     WorldSoundManager.instance.addSoundRepeating(this, (int)this.getX(), (int)this.getY(), (int)this.getZ(), Math.max(8, var27 / 6), var27 / 40, false);
                  }

                  if (this.lightbarSirenMode.isEnable() && this.getBatteryCharge() > 0.0F) {
                     WorldSoundManager.instance.addSoundRepeating(this, (int)this.getX(), (int)this.getY(), (int)this.getZ(), 100, 60, false);
                  }
               }

               if (this.engineState == BaseVehicle.engineStateTypes.Running && !this.isEngineWorking()) {
                  this.shutOff();
               }

               if (this.engineState == BaseVehicle.engineStateTypes.Running && this.getPartById("Engine").getCondition() < 50 && Rand.Next(this.getPartById("Engine").getCondition() * 12) == 0) {
                  this.shutOff();
               }

               if (this.engineState == BaseVehicle.engineStateTypes.Starting) {
                  this.updateEngineStarting();
               }

               if (this.engineState == BaseVehicle.engineStateTypes.RetryingStarting && System.currentTimeMillis() - this.engineLastUpdateStateTime > 10L) {
                  this.engineDoStarting();
               }

               if (this.engineState == BaseVehicle.engineStateTypes.StartingSuccess && System.currentTimeMillis() - this.engineLastUpdateStateTime > 500L) {
                  this.engineDoRunning();
               }

               if (this.engineState == BaseVehicle.engineStateTypes.StartingFailed && System.currentTimeMillis() - this.engineLastUpdateStateTime > 500L) {
                  this.engineDoIdle();
               }

               if (this.engineState == BaseVehicle.engineStateTypes.StartingFailedNoPower && System.currentTimeMillis() - this.engineLastUpdateStateTime > 500L) {
                  this.engineDoIdle();
               }

               if (this.engineState == BaseVehicle.engineStateTypes.Stalling && System.currentTimeMillis() - this.engineLastUpdateStateTime > 3000L) {
                  this.engineDoIdle();
               }

               if (this.engineState == BaseVehicle.engineStateTypes.ShutingDown && System.currentTimeMillis() - this.engineLastUpdateStateTime > 2000L) {
                  this.engineDoIdle();
               }
            }

            if (this.getDriver() == null && !var18) {
               this.getController().park();
            }

            this.setX(this.jniTransform.origin.x + WorldSimulation.instance.offsetX);
            this.setY(this.jniTransform.origin.z + WorldSimulation.instance.offsetY);
            this.setZ(0.0F);
            IsoGridSquare var32 = this.getCell().getGridSquare((double)this.x, (double)this.y, (double)this.z);
            if (var32 == null && !this.chunk.refs.isEmpty()) {
               float var28 = 5.0E-4F;
               int var10 = this.chunk.wx * 10;
               var11 = this.chunk.wy * 10;
               int var12 = var10 + 10;
               int var13 = var11 + 10;
               float var14 = this.x;
               float var15 = this.y;
               this.x = Math.max(this.x, (float)var10 + var28);
               this.x = Math.min(this.x, (float)var12 - var28);
               this.y = Math.max(this.y, (float)var11 + var28);
               this.y = Math.min(this.y, (float)var13 - var28);
               this.z = 0.2F;
               Transform var16 = this.tempTransform;
               Transform var17 = this.tempTransform2;
               this.getWorldTransform(var16);
               var17.basis.set((Matrix3fc)var16.basis);
               var17.origin.set(this.x - WorldSimulation.instance.offsetX, this.z, this.y - WorldSimulation.instance.offsetY);
               this.setWorldTransform(var17);
               this.current = this.getCell().getGridSquare((double)this.x, (double)this.y, (double)this.z);
            }

            if (this.current != null && this.current.chunk != null) {
               if (this.current.getChunk() != this.chunk) {
                  assert this.chunk.vehicles.contains(this);

                  this.chunk.vehicles.remove(this);
                  this.chunk = this.current.getChunk();
                  if (!GameServer.bServer && this.chunk.refs.isEmpty()) {
                     DebugLog.log("BaseVehicle.update() added to unloaded chunk id=" + this.VehicleID);
                  }

                  assert !this.chunk.vehicles.contains(this);

                  this.chunk.vehicles.add(this);
                  IsoChunk.addFromCheckedVehicles(this);
               }
            } else {
               boolean var29 = false;
            }

            this.updateTransform();
            if (this.jniIsCollide) {
               this.jniIsCollide = false;
               Vector3f var30 = (Vector3f)((BaseVehicle.Vector3fObjectPool)TL_vector3f_pool.get()).alloc();
               if (GameServer.bServer) {
                  var30.set((Vector3fc)this.netLinearVelocity);
               } else {
                  var30.set((Vector3fc)this.jniLinearVelocity);
               }

               var30.negate();
               var30.add(this.lastLinearVelocity);
               var30.y = 0.0F;
               float var31 = Math.abs(var30.length());
               if (var31 > 2.0F) {
                  if (this.lastLinearVelocity.length() < 6.0F) {
                     var31 /= 3.0F;
                  }

                  this.jniTransform.getRotation(this.tempQuat4f);
                  this.tempQuat4f.invert(this.tempQuat4f);
                  if (this.lastLinearVelocity.rotate(this.tempQuat4f).z < 0.0F) {
                     var31 *= -1.0F;
                  }

                  if (Core.bDebug) {
                     short var10000 = this.VehicleID;
                     DebugLog.log("CRASH ID=" + var10000 + ", lastSpeed=" + this.lastLinearVelocity.length() + " speed=" + var30 + " delta=" + var31 + " netLinearVelocity=" + this.netLinearVelocity.length());
                  }

                  Vector3f var33 = this.getForwardVector((Vector3f)((BaseVehicle.Vector3fObjectPool)TL_vector3f_pool.get()).alloc());
                  float var34 = var30.normalize().dot(var33);
                  ((BaseVehicle.Vector3fObjectPool)TL_vector3f_pool.get()).release(var33);
                  this.crash(Math.abs(var31 * 3.0F), var34 > 0.0F);
                  this.damageObjects(Math.abs(var31) * 30.0F);
               }

               ((BaseVehicle.Vector3fObjectPool)TL_vector3f_pool.get()).release(var30);
            }

            if (GameServer.bServer) {
               this.lastLinearVelocity.set((Vector3fc)this.netLinearVelocity);
            } else {
               this.lastLinearVelocity.set((Vector3fc)this.jniLinearVelocity);
            }
         }

         if (this.soundHornOn && this.hornemitter != null) {
            this.hornemitter.setPos(this.getX(), this.getY(), this.getZ());
         }

         int var19;
         for(var19 = 0; var19 < this.impulseFromSquishedZombie.length; ++var19) {
            BaseVehicle.VehicleImpulse var20 = this.impulseFromSquishedZombie[var19];
            if (var20 != null) {
               var20.enable = false;
            }
         }

         this.updateSounds();
         this.brekingObjects();
         if (this.bAddThumpWorldSound) {
            this.bAddThumpWorldSound = false;
            WorldSoundManager.instance.addSound(this, (int)this.x, (int)this.y, (int)this.z, 20, 20, true);
         }

         if (this.script.getLightbar().enable && this.lightbarLightsMode.isEnable() && this.getBatteryCharge() > 0.0F) {
            this.lightbarLightsMode.update();
         }

         this.updateWorldLights();

         for(var19 = 0; var19 < IsoPlayer.numPlayers; ++var19) {
            if (this.current == null || !this.current.lighting[var19].bCanSee()) {
               this.setTargetAlpha(var19, 0.0F);
            }

            IsoPlayer var21 = IsoPlayer.players[var19];
            if (var21 != null && this.DistToSquared(var21) < 225.0F) {
               this.setTargetAlpha(var19, 1.0F);
            }
         }

         for(var19 = 0; var19 < this.getScript().getPassengerCount(); ++var19) {
            if (this.getCharacter(var19) != null) {
               Vector3f var22 = this.getPassengerWorldPos(var19, (Vector3f)((BaseVehicle.Vector3fObjectPool)TL_vector3f_pool.get()).alloc());
               this.getCharacter(var19).setX(var22.x);
               this.getCharacter(var19).setY(var22.y);
               this.getCharacter(var19).setZ(var22.z * 1.0F);
               ((BaseVehicle.Vector3fObjectPool)TL_vector3f_pool.get()).release(var22);
            }
         }

         if (!this.needPartsUpdate() && !this.isMechanicUIOpen()) {
            this.drainBatteryUpdateHack();
         } else {
            this.updateParts();
         }

         if (this.engineState == BaseVehicle.engineStateTypes.Running || var18) {
            this.updateBulletStats();
         }

         if (this.bDoDamageOverlay) {
            this.bDoDamageOverlay = false;
            this.doDamageOverlay();
         }

         if (GameClient.bClient) {
            this.checkPhysicsValidWithServer();
         }

         VehiclePart var23 = this.getPartById("GasTank");
         if (var23 != null && var23.getContainerContentAmount() > (float)var23.getContainerCapacity()) {
            var23.setContainerContentAmount((float)var23.getContainerCapacity());
         }

         boolean var24 = false;

         for(int var5 = 0; var5 < this.getMaxPassengers(); ++var5) {
            BaseVehicle.Passenger var25 = this.getPassenger(var5);
            if (var25.character != null) {
               var24 = true;
               break;
            }
         }

         if (var24) {
            this.m_surroundVehicle.update();
         }

         if (this.physics != null) {
            Bullet.setVehicleMass(this.VehicleID, this.getFudgedMass());
         }

         this.updateVelocityMultiplier();
      }
   }

   private void updateEngineStarting() {
      if (this.getBatteryCharge() <= 0.1F) {
         this.engineDoStartingFailedNoPower();
      } else {
         VehiclePart var1 = this.getPartById("GasTank");
         if (var1 != null && var1.getContainerContentAmount() <= 0.0F) {
            this.engineDoStartingFailed();
         } else {
            int var2 = 0;
            float var3 = ClimateManager.getInstance().getAirTemperatureForSquare(this.getSquare());
            if (this.engineQuality < 65 && var3 <= 2.0F) {
               var2 = Math.min((2 - (int)var3) * 2, 30);
            }

            if (!SandboxOptions.instance.VehicleEasyUse.getValue() && this.engineQuality < 100 && Rand.Next(this.engineQuality + 50 - var2) <= 30) {
               this.engineDoStartingFailed();
            } else {
               if (Rand.Next(this.engineQuality) != 0) {
                  this.engineDoStartingSuccess();
               } else {
                  this.engineDoRetryingStarting();
               }

            }
         }
      }
   }

   private void applyImpulseFromHitZombies() {
      if (!this.impulseFromHitZombie.isEmpty()) {
         Vector3f var1 = ((Vector3f)((BaseVehicle.Vector3fObjectPool)TL_vector3f_pool.get()).alloc()).set(0.0F, 0.0F, 0.0F);
         Vector3f var2 = ((Vector3f)((BaseVehicle.Vector3fObjectPool)TL_vector3f_pool.get()).alloc()).set(0.0F, 0.0F, 0.0F);
         Vector3f var3 = ((Vector3f)((BaseVehicle.Vector3fObjectPool)TL_vector3f_pool.get()).alloc()).set(0.0F, 0.0F, 0.0F);
         int var4 = this.impulseFromHitZombie.size();

         for(int var5 = 0; var5 < var4; ++var5) {
            BaseVehicle.VehicleImpulse var6 = (BaseVehicle.VehicleImpulse)this.impulseFromHitZombie.get(var5);
            var1.add(var6.impulse);
            var2.add(var6.rel_pos.cross(var6.impulse, var3));
            var6.release();
            var6.enable = false;
         }

         this.impulseFromHitZombie.clear();
         float var7 = 7.0F * this.getFudgedMass();
         if (var1.lengthSquared() > var7 * var7) {
            var1.mul(var7 / var1.length());
         }

         float var8 = 30.0F;
         Bullet.applyCentralForceToVehicle(this.VehicleID, var1.x * var8, var1.y * var8, var1.z * var8);
         Bullet.applyTorqueToVehicle(this.VehicleID, var2.x * var8, var2.y * var8, var2.z * var8);
         if (GameServer.bServer) {
         }

         ((BaseVehicle.Vector3fObjectPool)TL_vector3f_pool.get()).release(var1);
         ((BaseVehicle.Vector3fObjectPool)TL_vector3f_pool.get()).release(var2);
         ((BaseVehicle.Vector3fObjectPool)TL_vector3f_pool.get()).release(var3);
      }
   }

   private void applyImpulseFromProneCharacters() {
      boolean var1 = PZArrayUtil.contains((Object[])this.impulseFromSquishedZombie, (var0) -> {
         return var0 != null && var0.enable;
      });
      if (var1) {
         Vector3f var2 = ((Vector3f)((BaseVehicle.Vector3fObjectPool)TL_vector3f_pool.get()).alloc()).set(0.0F, 0.0F, 0.0F);
         Vector3f var3 = ((Vector3f)((BaseVehicle.Vector3fObjectPool)TL_vector3f_pool.get()).alloc()).set(0.0F, 0.0F, 0.0F);
         Vector3f var4 = (Vector3f)((BaseVehicle.Vector3fObjectPool)TL_vector3f_pool.get()).alloc();

         for(int var5 = 0; var5 < this.impulseFromSquishedZombie.length; ++var5) {
            BaseVehicle.VehicleImpulse var6 = this.impulseFromSquishedZombie[var5];
            if (var6 != null && var6.enable) {
               var2.add(var6.impulse);
               var3.add(var6.rel_pos.cross(var6.impulse, var4));
               var6.enable = false;
               var6.release();
            }
         }

         if (var2.lengthSquared() > 0.0F) {
            float var7 = this.getFudgedMass() * 0.15F;
            if (var2.lengthSquared() > var7 * var7) {
               var2.mul(var7 / var2.length());
            }

            float var8 = 30.0F;
            Bullet.applyCentralForceToVehicle(this.VehicleID, var2.x * var8, var2.y * var8, var2.z * var8);
            Bullet.applyTorqueToVehicle(this.VehicleID, var3.x * var8, var3.y * var8, var3.z * var8);
         }

         ((BaseVehicle.Vector3fObjectPool)TL_vector3f_pool.get()).release(var2);
         ((BaseVehicle.Vector3fObjectPool)TL_vector3f_pool.get()).release(var3);
         ((BaseVehicle.Vector3fObjectPool)TL_vector3f_pool.get()).release(var4);
      }
   }

   public float getFudgedMass() {
      if (this.getScriptName().contains("Trailer")) {
         return this.getMass();
      } else {
         BaseVehicle var1 = this.getVehicleTowedBy();
         if (var1 != null && var1.getDriver() != null && var1.isEngineRunning()) {
            float var2 = Math.max(250.0F, var1.getMass() / 3.7F);
            if (this.getScript().getWheelCount() == 0) {
               var2 = Math.min(var2, 200.0F);
            }

            return var2;
         } else {
            return this.getMass();
         }
      }
   }

   private boolean isNullChunk(int var1, int var2) {
      if (!IsoWorld.instance.getMetaGrid().isValidChunk(var1, var2)) {
         return false;
      } else if (GameClient.bClient && !ClientServerMap.isChunkLoaded(var1, var2)) {
         return true;
      } else if (GameClient.bClient && !PassengerMap.isChunkLoaded(this, var1, var2)) {
         return true;
      } else {
         return this.getCell().getChunk(var1, var2) == null;
      }
   }

   public boolean isInvalidChunkAround() {
      Vector3f var1 = this.getLinearVelocity((Vector3f)((BaseVehicle.Vector3fObjectPool)TL_vector3f_pool.get()).alloc());
      float var2 = Math.abs(var1.x);
      float var3 = Math.abs(var1.z);
      boolean var4 = var1.x < 0.0F && var2 > var3;
      boolean var5 = var1.x > 0.0F && var2 > var3;
      boolean var6 = var1.z < 0.0F && var3 > var2;
      boolean var7 = var1.z > 0.0F && var3 > var2;
      ((BaseVehicle.Vector3fObjectPool)TL_vector3f_pool.get()).release(var1);
      return this.isInvalidChunkAround(var4, var5, var6, var7);
   }

   public boolean isInvalidChunkAhead() {
      Vector3f var1 = this.getForwardVector((Vector3f)((BaseVehicle.Vector3fObjectPool)TL_vector3f_pool.get()).alloc());
      boolean var2 = var1.x < -0.5F;
      boolean var3 = var1.z > 0.5F;
      boolean var4 = var1.x > 0.5F;
      boolean var5 = var1.z < -0.5F;
      return this.isInvalidChunkAround(var2, var4, var5, var3);
   }

   public boolean isInvalidChunkBehind() {
      Vector3f var1 = this.getForwardVector((Vector3f)((BaseVehicle.Vector3fObjectPool)TL_vector3f_pool.get()).alloc());
      boolean var2 = var1.x < -0.5F;
      boolean var3 = var1.z > 0.5F;
      boolean var4 = var1.x > 0.5F;
      boolean var5 = var1.z < -0.5F;
      return this.isInvalidChunkAround(var4, var2, var3, var5);
   }

   public boolean isInvalidChunkAround(boolean var1, boolean var2, boolean var3, boolean var4) {
      if (IsoChunkMap.ChunkGridWidth <= 7) {
         if (IsoChunkMap.ChunkGridWidth <= 4) {
            return false;
         } else if (var2 && this.isNullChunk(this.chunk.wx + 1, this.chunk.wy)) {
            return true;
         } else if (var1 && this.isNullChunk(this.chunk.wx - 1, this.chunk.wy)) {
            return true;
         } else if (var4 && this.isNullChunk(this.chunk.wx, this.chunk.wy + 1)) {
            return true;
         } else if (var3 && this.isNullChunk(this.chunk.wx, this.chunk.wy - 1)) {
            return true;
         } else {
            return false;
         }
      } else if (!var2 || !this.isNullChunk(this.chunk.wx + 1, this.chunk.wy) && !this.isNullChunk(this.chunk.wx + 2, this.chunk.wy)) {
         if (!var1 || !this.isNullChunk(this.chunk.wx - 1, this.chunk.wy) && !this.isNullChunk(this.chunk.wx - 2, this.chunk.wy)) {
            if (var4 && (this.isNullChunk(this.chunk.wx, this.chunk.wy + 1) || this.isNullChunk(this.chunk.wx, this.chunk.wy + 2))) {
               return true;
            } else if (!var3 || !this.isNullChunk(this.chunk.wx, this.chunk.wy - 1) && !this.isNullChunk(this.chunk.wx, this.chunk.wy - 2)) {
               return false;
            } else {
               return true;
            }
         } else {
            return true;
         }
      } else {
         return true;
      }
   }

   public void postupdate() {
      this.current = this.getCell().getGridSquare((int)this.x, (int)this.y, 0);
      int var1;
      if (this.current == null) {
         for(var1 = (int)this.z; var1 >= 0; --var1) {
            this.current = this.getCell().getGridSquare((int)this.x, (int)this.y, var1);
            if (this.current != null) {
               break;
            }
         }
      }

      if (this.movingSq != null) {
         this.movingSq.getMovingObjects().remove(this);
         this.movingSq = null;
      }

      if (this.current != null && !this.current.getMovingObjects().contains(this)) {
         this.current.getMovingObjects().add(this);
         this.movingSq = this.current;
      }

      this.square = this.current;
      if (this.sprite.hasActiveModel()) {
         this.updateAnimationPlayer(this.getAnimationPlayer(), (VehiclePart)null);

         for(var1 = 0; var1 < this.models.size(); ++var1) {
            BaseVehicle.ModelInfo var2 = (BaseVehicle.ModelInfo)this.models.get(var1);
            this.updateAnimationPlayer(var2.getAnimationPlayer(), var2.part);
         }
      }

   }

   protected void updateAnimationPlayer(AnimationPlayer var1, VehiclePart var2) {
      if (var1 != null && var1.isReady()) {
         AnimationMultiTrack var3 = var1.getMultiTrack();
         float var4 = 0.016666668F;
         var4 *= 0.8F;
         var4 *= GameTime.instance.getUnmoddedMultiplier();
         var1.Update(var4);

         for(int var5 = 0; var5 < var3.getTrackCount(); ++var5) {
            AnimationTrack var6 = (AnimationTrack)var3.getTracks().get(var5);
            if (var6.IsPlaying && var6.isFinished()) {
               var3.removeTrackAt(var5);
               --var5;
            }
         }

         if (var2 != null) {
            BaseVehicle.ModelInfo var8 = this.getModelInfoForPart(var2);
            if (var8.m_track != null && var3.getIndexOfTrack(var8.m_track) == -1) {
               var8.m_track = null;
            }

            if (var8.m_track != null) {
               VehicleWindow var10 = var2.getWindow();
               if (var10 != null) {
                  AnimationTrack var11 = var8.m_track;
                  var11.setCurrentTimeValue(var11.getDuration() * var10.getOpenDelta());
               }

            } else {
               VehicleDoor var9 = var2.getDoor();
               if (var9 != null) {
                  this.playPartAnim(var2, var9.isOpen() ? "Opened" : "Closed");
               }

               VehicleWindow var7 = var2.getWindow();
               if (var7 != null) {
                  this.playPartAnim(var2, "ClosedToOpen");
               }

            }
         }
      }
   }

   public void saveChange(String var1, KahluaTable var2, ByteBuffer var3) {
      super.saveChange(var1, var2, var3);
   }

   public void loadChange(String var1, ByteBuffer var2) {
      super.loadChange(var1, var2);
   }

   public void authorizationClientForecast(boolean var1) {
      if (var1 && this.getDriver() == null) {
         this.setNetPlayerAuthorization((byte)1);
      }

   }

   public void authorizationServerUpdate() {
      if (this.netPlayerAuthorization == 1 && this.netPlayerTimeout-- < 1) {
         this.setNetPlayerAuthorization((byte)0);
         this.netPlayerId = -1;
         this.netPlayerTimeout = 0;
      }

   }

   public void authorizationServerCollide(short var1, boolean var2) {
      if (this.netPlayerAuthorization != 3) {
         if (var2) {
            this.setNetPlayerAuthorization((byte)1);
            this.netPlayerId = var1;
            this.netPlayerTimeout = 30;
         } else {
            this.setNetPlayerAuthorization((byte)0);
            this.netPlayerId = -1;
            this.netPlayerTimeout = 0;
         }

      }
   }

   public void authorizationServerOnSeat() {
      if (this.getDriver() != null) {
         if (this.getVehicleTowedBy() != null) {
            if (this.getVehicleTowedBy().getDriver() != null) {
               this.setNetPlayerAuthorization((byte)3);
               this.netPlayerId = this.getVehicleTowedBy().getDriver().getOnlineID();
               this.netPlayerTimeout = 30;
            } else {
               this.setNetPlayerAuthorization((byte)0);
               this.netPlayerId = -1;
            }
         } else {
            this.setNetPlayerAuthorization((byte)3);
            this.netPlayerId = ((IsoPlayer)this.getDriver()).OnlineID;
            this.netPlayerTimeout = 30;
         }
      } else {
         this.setNetPlayerAuthorization((byte)0);
         this.netPlayerId = -1;
      }

   }

   public boolean authorizationServerOnOwnerData(UdpConnection var1) {
      boolean var2 = false;
      if (this.netPlayerAuthorization == 0) {
         return false;
      } else {
         for(int var3 = 0; var3 < var1.players.length; ++var3) {
            if (var1.players[var3] != null && var1.players[var3].OnlineID == this.netPlayerId) {
               var2 = true;
               break;
            }
         }

         if (this.getDriver() != null) {
            this.netPlayerTimeout = 30;
         }

         return var2;
      }
   }

   public void netPlayerServerSendAuthorisation(ByteBuffer var1) {
      var1.put(this.netPlayerAuthorization);
      var1.putShort(this.netPlayerId);
   }

   public void netPlayerFromServerUpdate(byte var1, short var2) {
      if (IsoPlayer.getPlayerIndex() >= 0 && IsoPlayer.players[IsoPlayer.getPlayerIndex()] != null && (var1 != this.netPlayerAuthorization || this.netPlayerId != var2)) {
         if (var1 == 3) {
            if (var2 == IsoPlayer.players[IsoPlayer.getPlayerIndex()].OnlineID) {
               this.setNetPlayerAuthorization((byte)3);
               this.netPlayerId = var2;
               Bullet.setVehicleStatic(this.VehicleID, false);
            } else if (this.netPlayerAuthorization != 4) {
               this.setNetPlayerAuthorization((byte)4);
               this.netPlayerId = var2;
               Bullet.setVehicleStatic(this.VehicleID, true);
            }
         } else if (var1 == 1) {
            if (var2 == IsoPlayer.players[IsoPlayer.getPlayerIndex()].OnlineID) {
               this.setNetPlayerAuthorization((byte)1);
               this.netPlayerId = var2;
               Bullet.setVehicleStatic(this.VehicleID, false);
            } else if (this.netPlayerAuthorization != 2) {
               this.setNetPlayerAuthorization((byte)2);
               this.netPlayerId = var2;
               Bullet.setVehicleStatic(this.VehicleID, true);
            }
         } else {
            this.setNetPlayerAuthorization((byte)0);
            this.netPlayerId = -1;
            Bullet.setVehicleStatic(this.VehicleID, false);
         }
      }
   }

   public Transform getWorldTransform(Transform var1) {
      var1.set(this.jniTransform);
      return var1;
   }

   public void setWorldTransform(Transform var1) {
      this.jniTransform.set(var1);
      Quaternionf var2 = this.tempQuat4f;
      var1.getRotation(var2);
      Bullet.teleportVehicle(this.VehicleID, var1.origin.x + WorldSimulation.instance.offsetX, var1.origin.z + WorldSimulation.instance.offsetY, var1.origin.y, var2.x, var2.y, var2.z, var2.w);
   }

   public void flipUpright() {
      Transform var1 = this.tempTransform;
      var1.set(this.jniTransform);
      Quaternionf var2 = this.tempQuat4f;
      var2.setAngleAxis(0.0F, _UNIT_Y.x, _UNIT_Y.y, _UNIT_Y.z);
      var1.setRotation(var2);
      this.setWorldTransform(var1);
   }

   public void setAngles(float var1, float var2, float var3) {
      if ((int)var1 != (int)this.getAngleX() || (int)var2 != (int)this.getAngleY() || var3 != (float)((int)this.getAngleZ())) {
         this.polyDirty = true;
         float var4 = var1 * 0.017453292F;
         float var5 = var2 * 0.017453292F;
         float var6 = var3 * 0.017453292F;
         this.tempQuat4f.rotationXYZ(var4, var5, var6);
         this.tempTransform.set(this.jniTransform);
         this.tempTransform.setRotation(this.tempQuat4f);
         this.setWorldTransform(this.tempTransform);
      }
   }

   public float getAngleX() {
      Vector3f var1 = (Vector3f)((BaseVehicle.Vector3fObjectPool)TL_vector3f_pool.get()).alloc();
      this.jniTransform.getRotation(this.tempQuat4f).getEulerAnglesXYZ(var1);
      float var2 = var1.x * 57.295776F;
      ((BaseVehicle.Vector3fObjectPool)TL_vector3f_pool.get()).release(var1);
      return var2;
   }

   public float getAngleY() {
      Vector3f var1 = (Vector3f)((BaseVehicle.Vector3fObjectPool)TL_vector3f_pool.get()).alloc();
      this.jniTransform.getRotation(this.tempQuat4f).getEulerAnglesXYZ(var1);
      float var2 = var1.y * 57.295776F;
      ((BaseVehicle.Vector3fObjectPool)TL_vector3f_pool.get()).release(var1);
      return var2;
   }

   public float getAngleZ() {
      Vector3f var1 = (Vector3f)((BaseVehicle.Vector3fObjectPool)TL_vector3f_pool.get()).alloc();
      this.jniTransform.getRotation(this.tempQuat4f).getEulerAnglesXYZ(var1);
      float var2 = var1.z * 57.295776F;
      ((BaseVehicle.Vector3fObjectPool)TL_vector3f_pool.get()).release(var1);
      return var2;
   }

   public void setDebugZ(float var1) {
      this.tempTransform.set(this.jniTransform);
      this.tempTransform.origin.y = PZMath.clamp(var1, 0.0F, 1.0F) * 3.0F * 0.82F;
      this.setWorldTransform(this.tempTransform);
   }

   public void setPhysicsActive(boolean var1) {
      if (this.physics != null && var1 != this.physics.isEnable) {
         this.physics.isEnable = var1;
         Bullet.setVehicleActive(this.VehicleID, var1);
         if (var1) {
            this.physicActiveCheck = System.currentTimeMillis() + 3000L;
         }

      }
   }

   public float getDebugZ() {
      return this.jniTransform.origin.y / 2.46F;
   }

   public PolygonalMap2.VehiclePoly getPoly() {
      if (this.polyDirty) {
         if (this.polyGarageCheck && this.square != null) {
            if (this.square.getRoom() != null && this.square.getRoom().RoomDef != null && this.square.getRoom().RoomDef.contains("garagestorage")) {
               this.radiusReductionInGarage = -0.3F;
            } else {
               this.radiusReductionInGarage = 0.0F;
            }

            this.polyGarageCheck = false;
         }

         this.poly.init(this, 0.0F);
         this.polyPlusRadius.init(this, PLUS_RADIUS + this.radiusReductionInGarage);
         this.polyDirty = false;
         this.polyPlusRadiusMinX = -123.0F;
         this.initShadowPoly();
      }

      return this.poly;
   }

   public PolygonalMap2.VehiclePoly getPolyPlusRadius() {
      if (this.polyDirty) {
         if (this.polyGarageCheck && this.square != null) {
            if (this.square.getRoom() != null && this.square.getRoom().RoomDef != null && this.square.getRoom().RoomDef.contains("garagestorage")) {
               this.radiusReductionInGarage = -0.3F;
            } else {
               this.radiusReductionInGarage = 0.0F;
            }

            this.polyGarageCheck = false;
         }

         this.poly.init(this, 0.0F);
         this.polyPlusRadius.init(this, PLUS_RADIUS + this.radiusReductionInGarage);
         this.polyDirty = false;
         this.polyPlusRadiusMinX = -123.0F;
         this.initShadowPoly();
      }

      return this.polyPlusRadius;
   }

   private void initShadowPoly() {
      this.getWorldTransform(this.tempTransform);
      Quaternionf var1 = this.tempTransform.getRotation(this.tempQuat4f);
      Vector2f var2 = this.script.getShadowExtents();
      Vector2f var3 = this.script.getShadowOffset();
      float var4 = var2.x / 2.0F;
      float var5 = var2.y / 2.0F;
      Vector3f var6 = (Vector3f)((BaseVehicle.Vector3fObjectPool)TL_vector3f_pool.get()).alloc();
      if (var1.x < 0.0F) {
         this.getWorldPos(var3.x - var4, 0.0F, var3.y + var5, var6);
         this.shadowCoord.x1 = var6.x;
         this.shadowCoord.y1 = var6.y;
         this.getWorldPos(var3.x + var4, 0.0F, var3.y + var5, var6);
         this.shadowCoord.x2 = var6.x;
         this.shadowCoord.y2 = var6.y;
         this.getWorldPos(var3.x + var4, 0.0F, var3.y - var5, var6);
         this.shadowCoord.x3 = var6.x;
         this.shadowCoord.y3 = var6.y;
         this.getWorldPos(var3.x - var4, 0.0F, var3.y - var5, var6);
         this.shadowCoord.x4 = var6.x;
         this.shadowCoord.y4 = var6.y;
      } else {
         this.getWorldPos(var3.x - var4, 0.0F, var3.y + var5, var6);
         this.shadowCoord.x1 = var6.x;
         this.shadowCoord.y1 = var6.y;
         this.getWorldPos(var3.x + var4, 0.0F, var3.y + var5, var6);
         this.shadowCoord.x2 = var6.x;
         this.shadowCoord.y2 = var6.y;
         this.getWorldPos(var3.x + var4, 0.0F, var3.y - var5, var6);
         this.shadowCoord.x3 = var6.x;
         this.shadowCoord.y3 = var6.y;
         this.getWorldPos(var3.x - var4, 0.0F, var3.y - var5, var6);
         this.shadowCoord.x4 = var6.x;
         this.shadowCoord.y4 = var6.y;
      }

      ((BaseVehicle.Vector3fObjectPool)TL_vector3f_pool.get()).release(var6);
   }

   private void initPolyPlusRadiusBounds() {
      if (this.polyPlusRadiusMinX == -123.0F) {
         PolygonalMap2.VehiclePoly var1 = this.getPolyPlusRadius();
         Vector3f var10 = (Vector3f)((BaseVehicle.Vector3fObjectPool)TL_vector3f_pool.get()).alloc();
         Vector3f var11 = this.getLocalPos(var1.x1, var1.y1, var1.z, var10);
         float var2 = (float)((int)(var11.x * 100.0F)) / 100.0F;
         float var3 = (float)((int)(var11.z * 100.0F)) / 100.0F;
         var11 = this.getLocalPos(var1.x2, var1.y2, var1.z, var10);
         float var4 = (float)((int)(var11.x * 100.0F)) / 100.0F;
         float var5 = (float)((int)(var11.z * 100.0F)) / 100.0F;
         var11 = this.getLocalPos(var1.x3, var1.y3, var1.z, var10);
         float var6 = (float)((int)(var11.x * 100.0F)) / 100.0F;
         float var7 = (float)((int)(var11.z * 100.0F)) / 100.0F;
         var11 = this.getLocalPos(var1.x4, var1.y4, var1.z, var10);
         float var8 = (float)((int)(var11.x * 100.0F)) / 100.0F;
         float var9 = (float)((int)(var11.z * 100.0F)) / 100.0F;
         this.polyPlusRadiusMinX = Math.min(var2, Math.min(var4, Math.min(var6, var8)));
         this.polyPlusRadiusMaxX = Math.max(var2, Math.max(var4, Math.max(var6, var8)));
         this.polyPlusRadiusMinY = Math.min(var3, Math.min(var5, Math.min(var7, var9)));
         this.polyPlusRadiusMaxY = Math.max(var3, Math.max(var5, Math.max(var7, var9)));
         ((BaseVehicle.Vector3fObjectPool)TL_vector3f_pool.get()).release(var10);
      }
   }

   public Vector3f getForwardVector(Vector3f var1) {
      byte var2 = 2;
      return this.jniTransform.basis.getColumn(var2, var1);
   }

   public Vector3f getUpVector(Vector3f var1) {
      byte var2 = 1;
      return this.jniTransform.basis.getColumn(var2, var1);
   }

   public float getUpVectorDot() {
      Vector3f var1 = this.getUpVector((Vector3f)((BaseVehicle.Vector3fObjectPool)TL_vector3f_pool.get()).alloc());
      float var2 = var1.dot(_UNIT_Y);
      ((BaseVehicle.Vector3fObjectPool)TL_vector3f_pool.get()).release(var1);
      return var2;
   }

   public float getCurrentSpeedKmHour() {
      return this.jniSpeed;
   }

   public Vector3f getLinearVelocity(Vector3f var1) {
      if (GameServer.bServer) {
         var1.set((Vector3fc)this.netLinearVelocity);
         return var1;
      } else {
         var1.set((Vector3fc)this.jniLinearVelocity);
         return var1;
      }
   }

   public float getSpeed2D() {
      float var1 = GameServer.bServer ? this.netLinearVelocity.x : this.jniLinearVelocity.x;
      float var2 = GameServer.bServer ? this.netLinearVelocity.z : this.jniLinearVelocity.z;
      return (float)Math.sqrt((double)(var1 * var1 + var2 * var2));
   }

   public boolean isAtRest() {
      if (this.physics == null) {
         return true;
      } else {
         float var1 = GameServer.bServer ? this.netLinearVelocity.y : this.jniLinearVelocity.y;
         return Math.abs(this.physics.EngineForce) < 0.01F && this.getSpeed2D() < 0.02F && Math.abs(var1) < 0.5F;
      }
   }

   protected void updateTransform() {
      if (this.sprite.modelSlot != null) {
         float var1 = this.getScript().getModelScale();
         float var2 = 1.0F;
         if (this.sprite.modelSlot != null && this.sprite.modelSlot.model.scale != 1.0F) {
            var2 = this.sprite.modelSlot.model.scale;
         }

         Transform var3 = this.getWorldTransform(this.tempTransform);
         Quaternionf var4 = (Quaternionf)((BaseVehicle.QuaternionfObjectPool)TL_quaternionf_pool.get()).alloc();
         Quaternionf var5 = (Quaternionf)((BaseVehicle.QuaternionfObjectPool)TL_quaternionf_pool.get()).alloc();
         Matrix4f var6 = (Matrix4f)((BaseVehicle.Matrix4fObjectPool)TL_matrix4f_pool.get()).alloc();
         var3.getRotation(var4);
         var4.y *= -1.0F;
         var4.z *= -1.0F;
         Matrix4f var7 = var4.get(var6);
         float var8 = 1.0F;
         if (this.sprite.modelSlot.model.m_modelScript != null) {
            var8 = this.sprite.modelSlot.model.m_modelScript.invertX ? -1.0F : 1.0F;
         }

         Vector3f var9 = this.script.getModel().getOffset();
         Vector3f var10 = this.getScript().getModel().getRotate();
         var5.rotationXYZ(var10.x * 0.017453292F, var10.y * 0.017453292F, var10.z * 0.017453292F);
         this.renderTransform.translationRotateScale(var9.x * -1.0F, var9.y, var9.z, var5.x, var5.y, var5.z, var5.w, var1 * var2 * var8, var1 * var2, var1 * var2);
         var7.mul((Matrix4fc)this.renderTransform, this.renderTransform);
         this.vehicleTransform.translationRotateScale(var9.x * -1.0F, var9.y, var9.z, 0.0F, 0.0F, 0.0F, 1.0F, var1);
         var7.mul((Matrix4fc)this.vehicleTransform, this.vehicleTransform);

         for(int var21 = 0; var21 < this.models.size(); ++var21) {
            BaseVehicle.ModelInfo var22 = (BaseVehicle.ModelInfo)this.models.get(var21);
            VehicleScript.Model var23 = var22.scriptModel;
            var10 = var23.getOffset();
            Vector3f var11 = var23.getRotate();
            float var12 = var23.scale;
            var2 = 1.0F;
            float var13 = 1.0F;
            if (var22.modelScript != null) {
               var2 = var22.modelScript.scale;
               var13 = var22.modelScript.invertX ? -1.0F : 1.0F;
            }

            var5.rotationXYZ(var11.x * 0.017453292F, var11.y * 0.017453292F, var11.z * 0.017453292F);
            if (var22.wheelIndex == -1) {
               var22.renderTransform.translationRotateScale(var10.x * -1.0F, var10.y, var10.z, var5.x, var5.y, var5.z, var5.w, var12 * var2 * var13, var12 * var2, var12 * var2);
               this.vehicleTransform.mul((Matrix4fc)var22.renderTransform, var22.renderTransform);
            } else {
               BaseVehicle.WheelInfo var14 = this.wheelInfo[var22.wheelIndex];
               float var15 = var14.steering;
               float var16 = var14.rotation;
               VehicleScript.Wheel var17 = this.getScript().getWheel(var22.wheelIndex);
               BaseVehicle.VehicleImpulse var18 = var22.wheelIndex < this.impulseFromSquishedZombie.length ? this.impulseFromSquishedZombie[var22.wheelIndex] : null;
               float var19 = var18 != null && var18.enable ? 0.05F : 0.0F;
               if (var14.suspensionLength == 0.0F) {
                  var6.translation(var17.offset.x / var1 * -1.0F, var17.offset.y / var1, var17.offset.z / var1);
               } else {
                  var6.translation(var17.offset.x / var1 * -1.0F, (var17.offset.y + this.script.getSuspensionRestLength() - var14.suspensionLength) / var1 + var19 * 0.5F, var17.offset.z / var1);
               }

               var22.renderTransform.identity();
               var22.renderTransform.mul((Matrix4fc)var6);
               var22.renderTransform.rotateY(var15 * -1.0F);
               var22.renderTransform.rotateX(var16);
               var6.translationRotateScale(var10.x * -1.0F, var10.y, var10.z, var5.x, var5.y, var5.z, var5.w, var12 * var2 * var13, var12 * var2, var12 * var2);
               var22.renderTransform.mul((Matrix4fc)var6);
               this.vehicleTransform.mul((Matrix4fc)var22.renderTransform, var22.renderTransform);
            }
         }

         ((BaseVehicle.Matrix4fObjectPool)TL_matrix4f_pool.get()).release(var6);
         ((BaseVehicle.QuaternionfObjectPool)TL_quaternionf_pool.get()).release(var4);
         ((BaseVehicle.QuaternionfObjectPool)TL_quaternionf_pool.get()).release(var5);
      }
   }

   public void serverUpdateSimulatorState() {
      if (Math.abs(this.physics.clientForce) > 0.01F && !this.physics.isEnable) {
         Bullet.setVehicleActive(this.VehicleID, true);
         this.physics.isEnable = true;
      }

      if (this.physics.isEnable && Math.abs(this.physics.clientForce) < 0.01F && this.isAtRest()) {
         Bullet.setVehicleActive(this.VehicleID, false);
         this.physics.isEnable = false;
      }

   }

   public void updatePhysics() {
      this.physics.update();
   }

   public void updatePhysicsNetwork() {
      if (this.limitPhysicSend.Check()) {
         VehicleManager.instance.sendPhysic(this);
      }

   }

   public void checkPhysicsValidWithServer() {
      if (this.limitPhysicValid.Check()) {
         float[] var1 = physicsParams;
         float var2 = 0.05F;
         if (Bullet.getOwnVehiclePhysics(this.VehicleID, var1) == 0 && (Math.abs(var1[0] - this.x) > var2 || Math.abs(var1[1] - this.y) > var2)) {
            VehicleManager.instance.sendReqestGetPosition(this.VehicleID);
         }
      }

   }

   public void updateControls() {
      if (this.getController() != null) {
         if (this.isOperational()) {
            IsoPlayer var1 = (IsoPlayer)Type.tryCastTo(this.getDriver(), IsoPlayer.class);
            if (var1 == null || !var1.isBlockMovement()) {
               this.getController().updateControls();
            }
         }
      }
   }

   public boolean isKeyboardControlled() {
      IsoGameCharacter var1 = this.getCharacter(0);
      return var1 != null && var1 == IsoPlayer.players[0] && this.getVehicleTowedBy() == null;
   }

   public int getJoypad() {
      IsoGameCharacter var1 = this.getCharacter(0);
      return var1 != null && var1 instanceof IsoPlayer ? ((IsoPlayer)var1).JoypadBind : -1;
   }

   public void Damage(float var1) {
      this.crash(var1, true);
   }

   public void HitByVehicle(BaseVehicle var1, float var2) {
      this.crash(var2, true);
   }

   public void crash(float var1, boolean var2) {
      if (GameClient.bClient && GameTime.getInstance().getCalender().getTimeInMillis() - this.lastCrashTime > 5000L) {
         this.lastCrashTime = GameTime.getInstance().getCalender().getTimeInMillis();
         SoundManager.instance.PlayWorldSound("VehicleCrash", this.square, 1.0F, 20.0F, 1.0F, true);
         GameClient.instance.sendClientCommandV((IsoPlayer)null, "vehicle", "crash", "vehicle", this.getId(), "amount", var1, "front", var2);
      } else {
         float var3 = 1.3F;
         float var4 = var1;
         switch(SandboxOptions.instance.CarDamageOnImpact.getValue()) {
         case 1:
            var3 = 1.9F;
            break;
         case 2:
            var3 = 1.6F;
         case 3:
         default:
            break;
         case 4:
            var3 = 1.1F;
            break;
         case 5:
            var3 = 0.9F;
         }

         var1 = Math.abs(var1) / var3;
         if (var2) {
            this.addDamageFront((int)var1);
         } else {
            this.addDamageRear((int)Math.abs(var1 / var3));
         }

         this.damagePlayers(Math.abs(var4));
         if (var4 < 5.0F) {
            SoundManager.instance.PlayWorldSound("VehicleCrash1", this.square, 1.0F, 20.0F, 1.0F, true);
         } else if (var4 < 30.0F) {
            SoundManager.instance.PlayWorldSound("VehicleCrash2", this.square, 1.0F, 20.0F, 1.0F, true);
         } else {
            SoundManager.instance.PlayWorldSound("VehicleCrash", this.square, 1.0F, 20.0F, 1.0F, true);
         }

      }
   }

   public void addDamageFrontHitAChr(int var1) {
      if (var1 >= 4 || !Rand.NextBool(7)) {
         VehiclePart var2 = this.getPartById("EngineDoor");
         if (var2 != null && var2.getInventoryItem() != null) {
            var2.damage(Rand.Next(Math.max(1, var1 - 10), var1 + 3));
         }

         if (var2 != null && var2.getCondition() <= 0 && Rand.NextBool(5)) {
            var2 = this.getPartById("Engine");
            if (var2 != null) {
               var2.damage(Rand.Next(1, 3));
            }
         }

         if (var1 > 12) {
            var2 = this.getPartById("Windshield");
            if (var2 != null && var2.getInventoryItem() != null) {
               var2.damage(Rand.Next(Math.max(1, var1 - 10), var1 + 3));
            }
         }

         if (Rand.Next(5) < var1) {
            if (Rand.NextBool(2)) {
               var2 = this.getPartById("TireFrontLeft");
            } else {
               var2 = this.getPartById("TireFrontRight");
            }

            if (var2 != null && var2.getInventoryItem() != null) {
               var2.damage(Rand.Next(1, 3));
            }
         }

         if (Rand.Next(7) < var1) {
            this.damageHeadlight("HeadlightLeft", Rand.Next(1, 4));
         }

         if (Rand.Next(7) < var1) {
            this.damageHeadlight("HeadlightRight", Rand.Next(1, 4));
         }

         float var3 = this.getBloodIntensity("Front");
         this.setBloodIntensity("Front", var3 + 0.01F);
      }
   }

   public void addDamageRearHitAChr(int var1) {
      if (var1 >= 4 || !Rand.NextBool(7)) {
         VehiclePart var2 = this.getPartById("TruckBed");
         if (var2 != null && var2.getInventoryItem() != null) {
            var2.setCondition(var2.getCondition() - Rand.Next(Math.max(1, var1 - 10), var1 + 3));
            var2.doInventoryItemStats(var2.getInventoryItem(), 0);
            this.transmitPartCondition(var2);
         }

         var2 = this.getPartById("DoorRear");
         if (var2 != null && var2.getInventoryItem() != null) {
            var2.damage(Rand.Next(Math.max(1, var1 - 10), var1 + 3));
         }

         var2 = this.getPartById("TrunkDoor");
         if (var2 != null && var2.getInventoryItem() != null) {
            var2.damage(Rand.Next(Math.max(1, var1 - 10), var1 + 3));
         }

         if (var1 > 12) {
            var2 = this.getPartById("WindshieldRear");
            if (var2 != null && var2.getInventoryItem() != null) {
               var2.damage(var1);
            }
         }

         if (Rand.Next(5) < var1) {
            if (Rand.NextBool(2)) {
               var2 = this.getPartById("TireRearLeft");
            } else {
               var2 = this.getPartById("TireRearRight");
            }

            if (var2 != null && var2.getInventoryItem() != null) {
               var2.damage(Rand.Next(1, 3));
            }
         }

         if (Rand.Next(7) < var1) {
            this.damageHeadlight("HeadlightRearLeft", Rand.Next(1, 4));
         }

         if (Rand.Next(7) < var1) {
            this.damageHeadlight("HeadlightRearRight", Rand.Next(1, 4));
         }

         if (Rand.Next(6) < var1) {
            var2 = this.getPartById("GasTank");
            if (var2 != null && var2.getInventoryItem() != null) {
               var2.damage(Rand.Next(1, 3));
            }
         }

         float var3 = this.getBloodIntensity("Rear");
         this.setBloodIntensity("Rear", var3 + 0.01F);
      }
   }

   private void addDamageFront(int var1) {
      this.currentFrontEndDurability -= var1;
      VehiclePart var2 = this.getPartById("EngineDoor");
      if (var2 != null && var2.getInventoryItem() != null) {
         var2.damage(Rand.Next(Math.max(1, var1 - 5), var1 + 5));
      }

      if (var2 == null || var2.getInventoryItem() == null || var2.getCondition() < 25) {
         var2 = this.getPartById("Engine");
         if (var2 != null) {
            var2.damage(Rand.Next(Math.max(1, var1 - 3), var1 + 3));
         }
      }

      var2 = this.getPartById("Windshield");
      if (var2 != null && var2.getInventoryItem() != null) {
         var2.damage(Rand.Next(Math.max(1, var1 - 5), var1 + 5));
      }

      if (Rand.Next(4) == 0) {
         var2 = this.getPartById("DoorFrontLeft");
         if (var2 != null && var2.getInventoryItem() != null) {
            var2.damage(Rand.Next(Math.max(1, var1 - 5), var1 + 5));
         }

         var2 = this.getPartById("WindowFrontLeft");
         if (var2 != null && var2.getInventoryItem() != null) {
            var2.damage(Rand.Next(Math.max(1, var1 - 5), var1 + 5));
         }
      }

      if (Rand.Next(4) == 0) {
         var2 = this.getPartById("DoorFrontRight");
         if (var2 != null && var2.getInventoryItem() != null) {
            var2.damage(Rand.Next(Math.max(1, var1 - 5), var1 + 5));
         }

         var2 = this.getPartById("WindowFrontRight");
         if (var2 != null && var2.getInventoryItem() != null) {
            var2.damage(Rand.Next(Math.max(1, var1 - 5), var1 + 5));
         }
      }

      if (Rand.Next(20) < var1) {
         this.damageHeadlight("HeadlightLeft", var1);
      }

      if (Rand.Next(20) < var1) {
         this.damageHeadlight("HeadlightRight", var1);
      }

   }

   private void addDamageRear(int var1) {
      this.currentRearEndDurability -= var1;
      VehiclePart var2 = this.getPartById("TruckBed");
      if (var2 != null && var2.getInventoryItem() != null) {
         var2.setCondition(var2.getCondition() - Rand.Next(Math.max(1, var1 - 5), var1 + 5));
         var2.doInventoryItemStats(var2.getInventoryItem(), 0);
         this.transmitPartCondition(var2);
      }

      var2 = this.getPartById("DoorRear");
      if (var2 != null && var2.getInventoryItem() != null) {
         var2.damage(Rand.Next(Math.max(1, var1 - 5), var1 + 5));
      }

      var2 = this.getPartById("TrunkDoor");
      if (var2 != null && var2.getInventoryItem() != null) {
         var2.damage(Rand.Next(Math.max(1, var1 - 5), var1 + 5));
      }

      var2 = this.getPartById("WindshieldRear");
      if (var2 != null && var2.getInventoryItem() != null) {
         var2.damage(var1);
      }

      if (Rand.Next(4) == 0) {
         var2 = this.getPartById("DoorRearLeft");
         if (var2 != null && var2.getInventoryItem() != null) {
            var2.damage(Rand.Next(Math.max(1, var1 - 5), var1 + 5));
         }

         var2 = this.getPartById("WindowRearLeft");
         if (var2 != null && var2.getInventoryItem() != null) {
            var2.damage(Rand.Next(Math.max(1, var1 - 5), var1 + 5));
         }
      }

      if (Rand.Next(4) == 0) {
         var2 = this.getPartById("DoorRearRight");
         if (var2 != null && var2.getInventoryItem() != null) {
            var2.damage(Rand.Next(Math.max(1, var1 - 5), var1 + 5));
         }

         var2 = this.getPartById("WindowRearRight");
         if (var2 != null && var2.getInventoryItem() != null) {
            var2.damage(Rand.Next(Math.max(1, var1 - 5), var1 + 5));
         }
      }

      if (Rand.Next(20) < var1) {
         this.damageHeadlight("HeadlightRearLeft", var1);
      }

      if (Rand.Next(20) < var1) {
         this.damageHeadlight("HeadlightRearRight", var1);
      }

      if (Rand.Next(20) < var1) {
         var2 = this.getPartById("Muffler");
         if (var2 != null && var2.getInventoryItem() != null) {
            var2.damage(Rand.Next(Math.max(1, var1 - 5), var1 + 5));
         }
      }

   }

   private void damageHeadlight(String var1, int var2) {
      VehiclePart var3 = this.getPartById(var1);
      if (var3 != null && var3.getInventoryItem() != null) {
         var3.damage(var2);
         if (var3.getCondition() <= 0) {
            var3.setInventoryItem((InventoryItem)null);
            this.transmitPartItem(var3);
         }
      }

   }

   private float clamp(float var1, float var2, float var3) {
      if (var1 < var2) {
         var1 = var2;
      }

      if (var1 > var3) {
         var1 = var3;
      }

      return var1;
   }

   public boolean isCharacterAdjacentTo(IsoGameCharacter var1) {
      if ((int)var1.z != (int)this.z) {
         return false;
      } else {
         Transform var2 = this.getWorldTransform(this.tempTransform);
         var2.inverse();
         Vector3f var3 = (Vector3f)((BaseVehicle.Vector3fObjectPool)TL_vector3f_pool.get()).alloc();
         var3.set(var1.x - WorldSimulation.instance.offsetX, 0.0F, var1.y - WorldSimulation.instance.offsetY);
         var2.transform(var3);
         Vector3f var4 = this.script.getExtents();
         Vector3f var5 = this.script.getCenterOfMassOffset();
         float var6 = var5.x - var4.x / 2.0F;
         float var7 = var5.x + var4.x / 2.0F;
         float var8 = var5.z - var4.z / 2.0F;
         float var9 = var5.z + var4.z / 2.0F;
         if (var3.x >= var6 - 0.5F && var3.x < var7 + 0.5F && var3.z >= var8 - 0.5F && var3.z < var9 + 0.5F) {
            ((BaseVehicle.Vector3fObjectPool)TL_vector3f_pool.get()).release(var3);
            return true;
         } else {
            ((BaseVehicle.Vector3fObjectPool)TL_vector3f_pool.get()).release(var3);
            return false;
         }
      }
   }

   public Vector2 testCollisionWithCharacter(IsoGameCharacter var1, float var2, Vector2 var3) {
      if (this.physics == null) {
         return null;
      } else {
         Vector3f var4 = this.script.getExtents();
         Vector3f var5 = this.script.getCenterOfMassOffset();
         if (this.DistToProper(var1) > Math.max(var4.x / 2.0F, var4.z / 2.0F) + var2 + 1.0F) {
            return null;
         } else {
            Vector3f var6 = (Vector3f)((BaseVehicle.Vector3fObjectPool)TL_vector3f_pool.get()).alloc();
            this.getLocalPos(var1.nx, var1.ny, 0.0F, var6);
            float var7 = var5.x - var4.x / 2.0F;
            float var8 = var5.x + var4.x / 2.0F;
            float var9 = var5.z - var4.z / 2.0F;
            float var10 = var5.z + var4.z / 2.0F;
            float var11;
            float var12;
            float var13;
            float var14;
            if (var6.x > var7 && var6.x < var8 && var6.z > var9 && var6.z < var10) {
               var11 = var6.x - var7;
               var12 = var8 - var6.x;
               var13 = var6.z - var9;
               var14 = var10 - var6.z;
               Vector3f var18 = (Vector3f)((BaseVehicle.Vector3fObjectPool)TL_vector3f_pool.get()).alloc();
               if (var11 < var12 && var11 < var13 && var11 < var14) {
                  var18.set(var7 - var2 - 0.015F, 0.0F, var6.z);
               } else if (var12 < var11 && var12 < var13 && var12 < var14) {
                  var18.set(var8 + var2 + 0.015F, 0.0F, var6.z);
               } else if (var13 < var11 && var13 < var12 && var13 < var14) {
                  var18.set(var6.x, 0.0F, var9 - var2 - 0.015F);
               } else if (var14 < var11 && var14 < var12 && var14 < var13) {
                  var18.set(var6.x, 0.0F, var10 + var2 + 0.015F);
               }

               ((BaseVehicle.Vector3fObjectPool)TL_vector3f_pool.get()).release(var6);
               Transform var19 = this.getWorldTransform(this.tempTransform);
               var19.origin.set(0.0F, 0.0F, 0.0F);
               var19.transform(var18);
               var18.x += this.getX();
               var18.z += this.getY();
               this.collideX = var18.x;
               this.collideY = var18.z;
               var3.set(var18.x, var18.z);
               ((BaseVehicle.Vector3fObjectPool)TL_vector3f_pool.get()).release(var18);
               return var3;
            } else {
               var11 = this.clamp(var6.x, var7, var8);
               var12 = this.clamp(var6.z, var9, var10);
               var13 = var6.x - var11;
               var14 = var6.z - var12;
               ((BaseVehicle.Vector3fObjectPool)TL_vector3f_pool.get()).release(var6);
               float var15 = var13 * var13 + var14 * var14;
               if (var15 < var2 * var2) {
                  if (var13 == 0.0F && var14 == 0.0F) {
                     return var3.set(-1.0F, -1.0F);
                  } else {
                     Vector3f var16 = (Vector3f)((BaseVehicle.Vector3fObjectPool)TL_vector3f_pool.get()).alloc();
                     var16.set(var13, 0.0F, var14);
                     var16.normalize();
                     var16.mul(var2 + 0.015F);
                     var16.x += var11;
                     var16.z += var12;
                     Transform var17 = this.getWorldTransform(this.tempTransform);
                     var17.origin.set(0.0F, 0.0F, 0.0F);
                     var17.transform(var16);
                     var16.x += this.getX();
                     var16.z += this.getY();
                     this.collideX = var16.x;
                     this.collideY = var16.z;
                     var3.set(var16.x, var16.z);
                     ((BaseVehicle.Vector3fObjectPool)TL_vector3f_pool.get()).release(var16);
                     return var3;
                  }
               } else {
                  return null;
               }
            }
         }
      }
   }

   public int testCollisionWithProneCharacter(IsoGameCharacter var1, boolean var2) {
      Vector2 var3 = var1.getAnimVector((Vector2)((BaseVehicle.Vector2ObjectPool)TL_vector2_pool.get()).alloc());
      int var4 = this.testCollisionWithProneCharacter(var1, var3.x, var3.y, var2);
      ((BaseVehicle.Vector2ObjectPool)TL_vector2_pool.get()).release(var3);
      return var4;
   }

   public int testCollisionWithCorpse(IsoDeadBody var1, boolean var2) {
      float var3 = (float)Math.cos((double)var1.getAngle());
      float var4 = (float)Math.sin((double)var1.getAngle());
      int var5 = this.testCollisionWithProneCharacter(var1, var3, var4, var2);
      return var5;
   }

   public int testCollisionWithProneCharacter(IsoMovingObject var1, float var2, float var3, boolean var4) {
      if (this.physics == null) {
         return 0;
      } else if (GameServer.bServer) {
         return 0;
      } else {
         Vector3f var5 = this.script.getExtents();
         float var6 = 0.3F;
         if (this.DistToProper(var1) > Math.max(var5.x / 2.0F, var5.z / 2.0F) + var6 + 1.0F) {
            return 0;
         } else if (Math.abs(this.jniSpeed) < 3.0F) {
            return 0;
         } else {
            float var7 = var1.x + var2 * 0.65F;
            float var8 = var1.y + var3 * 0.65F;
            float var9 = var1.x - var2 * 0.65F;
            float var10 = var1.y - var3 * 0.65F;
            int var11 = 0;
            Vector3f var12 = (Vector3f)((BaseVehicle.Vector3fObjectPool)TL_vector3f_pool.get()).alloc();
            Vector3f var13 = (Vector3f)((BaseVehicle.Vector3fObjectPool)TL_vector3f_pool.get()).alloc();

            for(int var14 = 0; var14 < this.script.getWheelCount(); ++var14) {
               VehicleScript.Wheel var15 = this.script.getWheel(var14);
               boolean var16 = true;

               for(int var17 = 0; var17 < this.models.size(); ++var17) {
                  BaseVehicle.ModelInfo var18 = (BaseVehicle.ModelInfo)this.models.get(var17);
                  if (var18.wheelIndex == var14) {
                     this.getWorldPos(var15.offset.x, var15.offset.y - this.wheelInfo[var14].suspensionLength, var15.offset.z, var12);
                     if (var12.z > this.script.getWheel(var14).radius + 0.05F) {
                        var16 = false;
                     }
                     break;
                  }
               }

               if (var16) {
                  this.getWorldPos(var15.offset.x, var15.offset.y, var15.offset.z, var13);
                  float var21 = var13.x;
                  float var22 = var13.y;
                  double var23 = (double)((var21 - var9) * (var7 - var9) + (var22 - var10) * (var8 - var10)) / (Math.pow((double)(var7 - var9), 2.0D) + Math.pow((double)(var8 - var10), 2.0D));
                  float var25;
                  float var26;
                  if (var23 <= 0.0D) {
                     var25 = var9;
                     var26 = var10;
                  } else if (var23 >= 1.0D) {
                     var25 = var7;
                     var26 = var8;
                  } else {
                     var25 = var9 + (var7 - var9) * (float)var23;
                     var26 = var10 + (var8 - var10) * (float)var23;
                  }

                  if (!(IsoUtils.DistanceToSquared(var13.x, var13.y, var25, var26) > var15.radius * var15.radius)) {
                     if (var4 && Math.abs(this.jniSpeed) > 10.0F) {
                        if (GameServer.bServer && var1 instanceof IsoZombie) {
                           ((IsoZombie)var1).setThumpFlag(1);
                        } else {
                           SoundManager.instance.PlayWorldSound("VehicleRunOverBody", var1.getCurrentSquare(), 0.0F, 20.0F, 0.9F, true);
                        }

                        var4 = false;
                     }

                     if (var14 < this.impulseFromSquishedZombie.length) {
                        if (this.impulseFromSquishedZombie[var14] == null) {
                           this.impulseFromSquishedZombie[var14] = new BaseVehicle.VehicleImpulse();
                        }

                        this.impulseFromSquishedZombie[var14].impulse.set(0.0F, 1.0F, 0.0F);
                        float var27 = Math.max(Math.abs(this.jniSpeed), 20.0F) / 20.0F;
                        this.impulseFromSquishedZombie[var14].impulse.mul(0.065F * this.getFudgedMass() * var27);
                        this.impulseFromSquishedZombie[var14].rel_pos.set(var13.x - this.x, 0.0F, var13.y - this.y);
                        this.impulseFromSquishedZombie[var14].enable = true;
                        ++var11;
                     }
                  }
               }
            }

            ((BaseVehicle.Vector3fObjectPool)TL_vector3f_pool.get()).release(var12);
            ((BaseVehicle.Vector3fObjectPool)TL_vector3f_pool.get()).release(var13);
            return var11;
         }
      }
   }

   public Vector2 testCollisionWithObject(IsoObject var1, float var2, Vector2 var3) {
      if (this.physics == null) {
         return null;
      } else if (var1.square == null) {
         return null;
      } else {
         float var4 = this.getObjectX(var1);
         float var5 = this.getObjectY(var1);
         Vector3f var6 = this.script.getExtents();
         Vector3f var7 = this.script.getCenterOfMassOffset();
         float var8 = Math.max(var6.x / 2.0F, var6.z / 2.0F) + var2 + 1.0F;
         if (this.DistToSquared(var4, var5) > var8 * var8) {
            return null;
         } else {
            Vector3f var9 = (Vector3f)((BaseVehicle.Vector3fObjectPool)TL_vector3f_pool.get()).alloc();
            this.getLocalPos(var4, var5, 0.0F, var9);
            float var10 = var7.x - var6.x / 2.0F;
            float var11 = var7.x + var6.x / 2.0F;
            float var12 = var7.z - var6.z / 2.0F;
            float var13 = var7.z + var6.z / 2.0F;
            float var14;
            float var15;
            float var16;
            float var17;
            if (var9.x > var10 && var9.x < var11 && var9.z > var12 && var9.z < var13) {
               var14 = var9.x - var10;
               var15 = var11 - var9.x;
               var16 = var9.z - var12;
               var17 = var13 - var9.z;
               Vector3f var21 = (Vector3f)((BaseVehicle.Vector3fObjectPool)TL_vector3f_pool.get()).alloc();
               if (var14 < var15 && var14 < var16 && var14 < var17) {
                  var21.set(var10 - var2 - 0.015F, 0.0F, var9.z);
               } else if (var15 < var14 && var15 < var16 && var15 < var17) {
                  var21.set(var11 + var2 + 0.015F, 0.0F, var9.z);
               } else if (var16 < var14 && var16 < var15 && var16 < var17) {
                  var21.set(var9.x, 0.0F, var12 - var2 - 0.015F);
               } else if (var17 < var14 && var17 < var15 && var17 < var16) {
                  var21.set(var9.x, 0.0F, var13 + var2 + 0.015F);
               }

               ((BaseVehicle.Vector3fObjectPool)TL_vector3f_pool.get()).release(var9);
               Transform var22 = this.getWorldTransform(this.tempTransform);
               var22.origin.set(0.0F, 0.0F, 0.0F);
               var22.transform(var21);
               var21.x += this.getX();
               var21.z += this.getY();
               this.collideX = var21.x;
               this.collideY = var21.z;
               var3.set(var21.x, var21.z);
               ((BaseVehicle.Vector3fObjectPool)TL_vector3f_pool.get()).release(var21);
               return var3;
            } else {
               var14 = this.clamp(var9.x, var10, var11);
               var15 = this.clamp(var9.z, var12, var13);
               var16 = var9.x - var14;
               var17 = var9.z - var15;
               ((BaseVehicle.Vector3fObjectPool)TL_vector3f_pool.get()).release(var9);
               float var18 = var16 * var16 + var17 * var17;
               if (var18 < var2 * var2) {
                  if (var16 == 0.0F && var17 == 0.0F) {
                     return var3.set(-1.0F, -1.0F);
                  } else {
                     Vector3f var19 = (Vector3f)((BaseVehicle.Vector3fObjectPool)TL_vector3f_pool.get()).alloc();
                     var19.set(var16, 0.0F, var17);
                     var19.normalize();
                     var19.mul(var2 + 0.015F);
                     var19.x += var14;
                     var19.z += var15;
                     Transform var20 = this.getWorldTransform(this.tempTransform);
                     var20.origin.set(0.0F, 0.0F, 0.0F);
                     var20.transform(var19);
                     var19.x += this.getX();
                     var19.z += this.getY();
                     this.collideX = var19.x;
                     this.collideY = var19.z;
                     var3.set(var19.x, var19.z);
                     ((BaseVehicle.Vector3fObjectPool)TL_vector3f_pool.get()).release(var19);
                     return var3;
                  }
               } else {
                  return null;
               }
            }
         }
      }
   }

   public boolean testCollisionWithVehicle(BaseVehicle var1) {
      VehicleScript var2 = this.script;
      if (var2 == null) {
         var2 = ScriptManager.instance.getVehicle(this.scriptName);
      }

      VehicleScript var3 = var1.script;
      if (var3 == null) {
         var3 = ScriptManager.instance.getVehicle(var1.scriptName);
      }

      if (var2 != null && var3 != null) {
         Vector2[] var4 = BaseVehicle.L_testCollisionWithVehicle.testVecs1;
         Vector2[] var5 = BaseVehicle.L_testCollisionWithVehicle.testVecs2;
         if (var4[0] == null) {
            for(int var6 = 0; var6 < var4.length; ++var6) {
               var4[var6] = new Vector2();
               var5[var6] = new Vector2();
            }
         }

         Vector3f var12 = var2.getExtents();
         Vector3f var7 = var2.getCenterOfMassOffset();
         Vector3f var8 = var3.getExtents();
         Vector3f var9 = var3.getCenterOfMassOffset();
         Vector3f var10 = BaseVehicle.L_testCollisionWithVehicle.worldPos;
         float var11 = 0.5F;
         this.getWorldPos(var7.x + var12.x * var11, 0.0F, var7.z + var12.z * var11, var10, var2);
         var4[0].set(var10.x, var10.y);
         this.getWorldPos(var7.x - var12.x * var11, 0.0F, var7.z + var12.z * var11, var10, var2);
         var4[1].set(var10.x, var10.y);
         this.getWorldPos(var7.x - var12.x * var11, 0.0F, var7.z - var12.z * var11, var10, var2);
         var4[2].set(var10.x, var10.y);
         this.getWorldPos(var7.x + var12.x * var11, 0.0F, var7.z - var12.z * var11, var10, var2);
         var4[3].set(var10.x, var10.y);
         var1.getWorldPos(var9.x + var8.x * var11, 0.0F, var9.z + var8.z * var11, var10, var3);
         var5[0].set(var10.x, var10.y);
         var1.getWorldPos(var9.x - var8.x * var11, 0.0F, var9.z + var8.z * var11, var10, var3);
         var5[1].set(var10.x, var10.y);
         var1.getWorldPos(var9.x - var8.x * var11, 0.0F, var9.z - var8.z * var11, var10, var3);
         var5[2].set(var10.x, var10.y);
         var1.getWorldPos(var9.x + var8.x * var11, 0.0F, var9.z - var8.z * var11, var10, var3);
         var5[3].set(var10.x, var10.y);
         return QuadranglesIntersection.IsQuadranglesAreIntersected(var4, var5);
      } else {
         return false;
      }
   }

   protected float getObjectX(IsoObject var1) {
      return var1 instanceof IsoMovingObject ? var1.getX() : (float)var1.getSquare().getX() + 0.5F;
   }

   protected float getObjectY(IsoObject var1) {
      return var1 instanceof IsoMovingObject ? var1.getY() : (float)var1.getSquare().getY() + 0.5F;
   }

   public void ApplyImpulse(IsoObject var1, float var2) {
      float var3 = this.getObjectX(var1);
      float var4 = this.getObjectY(var1);
      BaseVehicle.VehicleImpulse var5 = BaseVehicle.VehicleImpulse.alloc();
      var5.impulse.set(this.x - var3, 0.0F, this.y - var4);
      var5.impulse.normalize();
      var5.impulse.mul(var2);
      var5.rel_pos.set(var3 - this.x, 0.0F, var4 - this.y);
      this.impulseFromHitZombie.add(var5);
   }

   public void ApplyImpulse4Break(IsoObject var1, float var2) {
      float var3 = this.getObjectX(var1);
      float var4 = this.getObjectY(var1);
      BaseVehicle.VehicleImpulse var5 = BaseVehicle.VehicleImpulse.alloc();
      this.getLinearVelocity(var5.impulse);
      var5.impulse.mul(-var2 * this.getFudgedMass());
      var5.rel_pos.set(var3 - this.x, 0.0F, var4 - this.y);
      this.impulseFromHitZombie.add(var5);
   }

   public void hitCharacter(IsoZombie var1) {
      IsoPlayer var2 = (IsoPlayer)Type.tryCastTo(var1, IsoPlayer.class);
      IsoZombie var3 = (IsoZombie)Type.tryCastTo(var1, IsoZombie.class);
      if (var1.getCurrentState() != StaggerBackState.instance() && var1.getCurrentState() != ZombieFallDownState.instance()) {
         if (!(Math.abs(var1.x - this.x) < 0.01F) && !(Math.abs(var1.y - this.y) < 0.01F)) {
            float var4 = 15.0F;
            Vector3f var5 = this.getLinearVelocity((Vector3f)((BaseVehicle.Vector3fObjectPool)TL_vector3f_pool.get()).alloc());
            var5.y = 0.0F;
            float var6 = var5.length();
            var6 = Math.min(var6, var4);
            if (var6 < 0.05F) {
               ((BaseVehicle.Vector3fObjectPool)TL_vector3f_pool.get()).release(var5);
            } else {
               Vector3f var7 = (Vector3f)((BaseVehicle.Vector3fObjectPool)TL_vector3f_pool.get()).alloc();
               var7.set(this.x - var1.x, 0.0F, this.y - var1.y);
               var7.normalize();
               var5.normalize();
               float var8 = var5.dot(var7);
               ((BaseVehicle.Vector3fObjectPool)TL_vector3f_pool.get()).release(var5);
               if (var8 < 0.0F && !GameServer.bServer) {
                  this.ApplyImpulse(var1, this.getFudgedMass() * 7.0F * var6 / var4 * Math.abs(var8));
               }

               var7.normalize();
               var7.mul(3.0F * var6 / var4);
               Vector2 var9 = (Vector2)((BaseVehicle.Vector2ObjectPool)TL_vector2_pool.get()).alloc();
               float var10 = var6 + this.physics.clientForce / this.getFudgedMass();
               if (var2 != null) {
                  var2.setVehicleHitLocation(this);
               } else if (var3 != null) {
                  var3.setVehicleHitLocation(this);
               }

               BaseSoundEmitter var11 = IsoWorld.instance.getFreeEmitter(var1.x, var1.y, var1.z);
               long var12 = var11.playSound("VehicleHitCharacter");
               var11.setParameterValue(var12, FMODManager.instance.getParameterDescription("VehicleSpeed"), this.getCurrentSpeedKmHour());
               var1.Hit(this, var10, var8 > 0.0F, var9.set(-var7.x, -var7.z));
               ((BaseVehicle.Vector2ObjectPool)TL_vector2_pool.get()).release(var9);
               ((BaseVehicle.Vector3fObjectPool)TL_vector3f_pool.get()).release(var7);
               long var17 = System.currentTimeMillis();
               long var13 = (var17 - this.zombieHitTimestamp) / 1000L;
               this.zombiesHits = Math.max(this.zombiesHits - (int)var13, 0);
               if (var17 - this.zombieHitTimestamp > 700L) {
                  this.zombieHitTimestamp = var17;
                  ++this.zombiesHits;
                  this.zombiesHits = Math.min(this.zombiesHits, 20);
               }

               if (var6 >= 5.0F || this.zombiesHits > 10) {
                  var6 = this.getCurrentSpeedKmHour() / 5.0F;
                  Vector3f var15 = (Vector3f)((BaseVehicle.Vector3fObjectPool)TL_vector3f_pool.get()).alloc();
                  this.getLocalPos(var1.x, var1.y, var1.z, var15);
                  int var16;
                  if (var15.z > 0.0F) {
                     var16 = this.caclulateDamageWithBodies(true);
                     this.addDamageFrontHitAChr(var16);
                  } else {
                     var16 = this.caclulateDamageWithBodies(false);
                     this.addDamageRearHitAChr(var16);
                  }

                  ((BaseVehicle.Vector3fObjectPool)TL_vector3f_pool.get()).release(var15);
               }

            }
         }
      }
   }

   private int caclulateDamageWithBodies(boolean var1) {
      boolean var2 = this.getCurrentSpeedKmHour() > 0.0F;
      float var3 = Math.abs(this.getCurrentSpeedKmHour());
      float var4 = var3 / 160.0F;
      var4 = PZMath.clamp(var4 * var4, 0.0F, 1.0F);
      float var5 = 60.0F * var4;
      float var6 = PZMath.max(1.0F, (float)this.zombiesHits / 3.0F);
      if (!var1 && !var2) {
         var6 = 1.0F;
      }

      if (this.zombiesHits > 10 && var5 < Math.abs(this.getCurrentSpeedKmHour()) / 5.0F) {
         var5 = Math.abs(this.getCurrentSpeedKmHour()) / 5.0F;
      }

      return (int)(var6 * var5);
   }

   public int calculateDamageWithCharacter(IsoGameCharacter var1) {
      Vector3f var2 = (Vector3f)((BaseVehicle.Vector3fObjectPool)TL_vector3f_pool.get()).alloc();
      this.getLocalPos(var1.x, var1.y, var1.z, var2);
      int var3;
      if (var2.z > 0.0F) {
         var3 = this.caclulateDamageWithBodies(true);
      } else {
         var3 = -1 * this.caclulateDamageWithBodies(false);
      }

      ((BaseVehicle.Vector3fObjectPool)TL_vector3f_pool.get()).release(var2);
      return var3;
   }

   public boolean blocked(int var1, int var2, int var3) {
      if (!this.removedFromWorld && this.current != null && this.getController() != null) {
         if (this.getController() == null) {
            return false;
         } else if (var3 != (int)this.getZ()) {
            return false;
         } else if (IsoUtils.DistanceTo2D((float)var1 + 0.5F, (float)var2 + 0.5F, this.x, this.y) > 5.0F) {
            return false;
         } else {
            float var4 = 0.3F;
            Transform var5 = this.tempTransform3;
            this.getWorldTransform(var5);
            var5.inverse();
            Vector3f var6 = (Vector3f)((BaseVehicle.Vector3fObjectPool)TL_vector3f_pool.get()).alloc();
            var6.set((float)var1 + 0.5F - WorldSimulation.instance.offsetX, 0.0F, (float)var2 + 0.5F - WorldSimulation.instance.offsetY);
            var5.transform(var6);
            Vector3f var7 = this.script.getExtents();
            Vector3f var8 = this.script.getCenterOfMassOffset();
            float var9 = this.clamp(var6.x, var8.x - var7.x / 2.0F, var8.x + var7.x / 2.0F);
            float var10 = this.clamp(var6.z, var8.z - var7.z / 2.0F, var8.z + var7.z / 2.0F);
            float var11 = var6.x - var9;
            float var12 = var6.z - var10;
            ((BaseVehicle.Vector3fObjectPool)TL_vector3f_pool.get()).release(var6);
            float var13 = var11 * var11 + var12 * var12;
            return var13 < var4 * var4;
         }
      } else {
         return false;
      }
   }

   public boolean isIntersectingSquare(int var1, int var2, int var3) {
      if (var3 != (int)this.getZ()) {
         return false;
      } else if (!this.removedFromWorld && this.current != null && this.getController() != null) {
         tempPoly.x1 = tempPoly.x4 = (float)var1;
         tempPoly.y1 = tempPoly.y2 = (float)var2;
         tempPoly.x2 = tempPoly.x3 = (float)(var1 + 1);
         tempPoly.y3 = tempPoly.y4 = (float)(var2 + 1);
         return PolyPolyIntersect.intersects(tempPoly, this.getPoly());
      } else {
         return false;
      }
   }

   public boolean isIntersectingSquareWithShadow(int var1, int var2, int var3) {
      if (var3 != (int)this.getZ()) {
         return false;
      } else if (!this.removedFromWorld && this.current != null && this.getController() != null) {
         tempPoly.x1 = tempPoly.x4 = (float)var1;
         tempPoly.y1 = tempPoly.y2 = (float)var2;
         tempPoly.x2 = tempPoly.x3 = (float)(var1 + 1);
         tempPoly.y3 = tempPoly.y4 = (float)(var2 + 1);
         return PolyPolyIntersect.intersects(tempPoly, this.shadowCoord);
      } else {
         return false;
      }
   }

   public boolean circleIntersects(float var1, float var2, float var3, float var4) {
      if (this.getController() == null) {
         return false;
      } else if ((int)var3 != (int)this.getZ()) {
         return false;
      } else if (IsoUtils.DistanceTo2D(var1, var2, this.x, this.y) > 5.0F) {
         return false;
      } else {
         Vector3f var5 = this.script.getExtents();
         Vector3f var6 = this.script.getCenterOfMassOffset();
         Vector3f var7 = (Vector3f)((BaseVehicle.Vector3fObjectPool)TL_vector3f_pool.get()).alloc();
         this.getLocalPos(var1, var2, var3, var7);
         float var8 = var6.x - var5.x / 2.0F;
         float var9 = var6.x + var5.x / 2.0F;
         float var10 = var6.z - var5.z / 2.0F;
         float var11 = var6.z + var5.z / 2.0F;
         if (var7.x > var8 && var7.x < var9 && var7.z > var10 && var7.z < var11) {
            return true;
         } else {
            float var12 = this.clamp(var7.x, var8, var9);
            float var13 = this.clamp(var7.z, var10, var11);
            float var14 = var7.x - var12;
            float var15 = var7.z - var13;
            ((BaseVehicle.Vector3fObjectPool)TL_vector3f_pool.get()).release(var7);
            float var16 = var14 * var14 + var15 * var15;
            return var16 < var4 * var4;
         }
      }
   }

   public void updateLights() {
      VehicleModelInstance var1 = (VehicleModelInstance)this.sprite.modelSlot.model;
      var1.textureRustA = this.rust;
      if (this.script.getWheelCount() == 0) {
         var1.textureRustA = 0.0F;
      }

      var1.painColor.x = this.colorHue;
      var1.painColor.y = this.colorSaturation;
      var1.painColor.z = this.colorValue;
      boolean var2 = false;
      boolean var3 = false;
      boolean var4 = false;
      boolean var5 = false;
      boolean var6 = false;
      boolean var7 = false;
      boolean var8 = false;
      boolean var9 = false;
      if (this.windowLightsOn) {
         VehiclePart var10 = this.getPartById("Windshield");
         var2 = var10 != null && var10.getInventoryItem() != null;
         var10 = this.getPartById("WindshieldRear");
         var3 = var10 != null && var10.getInventoryItem() != null;
         var10 = this.getPartById("WindowFrontLeft");
         var4 = var10 != null && var10.getInventoryItem() != null;
         var10 = this.getPartById("WindowMiddleLeft");
         var5 = var10 != null && var10.getInventoryItem() != null;
         var10 = this.getPartById("WindowRearLeft");
         var6 = var10 != null && var10.getInventoryItem() != null;
         var10 = this.getPartById("WindowFrontRight");
         var7 = var10 != null && var10.getInventoryItem() != null;
         var10 = this.getPartById("WindowMiddleRight");
         var8 = var10 != null && var10.getInventoryItem() != null;
         var10 = this.getPartById("WindowRearRight");
         var9 = var10 != null && var10.getInventoryItem() != null;
      }

      var1.textureLightsEnables1[10] = var2 ? 1.0F : 0.0F;
      var1.textureLightsEnables1[14] = var3 ? 1.0F : 0.0F;
      var1.textureLightsEnables1[2] = var4 ? 1.0F : 0.0F;
      var1.textureLightsEnables1[6] = var5 | var6 ? 1.0F : 0.0F;
      var1.textureLightsEnables1[9] = var7 ? 1.0F : 0.0F;
      var1.textureLightsEnables1[13] = var8 | var9 ? 1.0F : 0.0F;
      boolean var18 = false;
      boolean var11 = false;
      boolean var12 = false;
      boolean var13 = false;
      if (this.headlightsOn && this.getBatteryCharge() > 0.0F) {
         VehiclePart var14 = this.getPartById("HeadlightLeft");
         if (var14 != null && var14.getInventoryItem() != null) {
            var18 = true;
         }

         var14 = this.getPartById("HeadlightRight");
         if (var14 != null && var14.getInventoryItem() != null) {
            var11 = true;
         }

         var14 = this.getPartById("HeadlightRearLeft");
         if (var14 != null && var14.getInventoryItem() != null) {
            var13 = true;
         }

         var14 = this.getPartById("HeadlightRearRight");
         if (var14 != null && var14.getInventoryItem() != null) {
            var12 = true;
         }
      }

      var1.textureLightsEnables2[4] = var11 ? 1.0F : 0.0F;
      var1.textureLightsEnables2[8] = var18 ? 1.0F : 0.0F;
      var1.textureLightsEnables2[12] = var12 ? 1.0F : 0.0F;
      var1.textureLightsEnables2[1] = var13 ? 1.0F : 0.0F;
      boolean var19 = this.stoplightsOn && this.getBatteryCharge() > 0.0F;
      if (this.scriptName.contains("Trailer") && this.vehicleTowedBy != null && this.vehicleTowedBy.stoplightsOn && this.vehicleTowedBy.getBatteryCharge() > 0.0F) {
         var19 = true;
      }

      if (var19) {
         var1.textureLightsEnables2[5] = 1.0F;
         var1.textureLightsEnables2[9] = 1.0F;
      } else {
         var1.textureLightsEnables2[5] = 0.0F;
         var1.textureLightsEnables2[9] = 0.0F;
      }

      if (this.script.getLightbar().enable) {
         if (this.lightbarLightsMode.isEnable() && this.getBatteryCharge() > 0.0F) {
            switch(this.lightbarLightsMode.getLightTexIndex()) {
            case 0:
               var1.textureLightsEnables2[13] = 0.0F;
               var1.textureLightsEnables2[2] = 0.0F;
               break;
            case 1:
               var1.textureLightsEnables2[13] = 0.0F;
               var1.textureLightsEnables2[2] = 1.0F;
               break;
            case 2:
               var1.textureLightsEnables2[13] = 1.0F;
               var1.textureLightsEnables2[2] = 0.0F;
               break;
            default:
               var1.textureLightsEnables2[13] = 0.0F;
               var1.textureLightsEnables2[2] = 0.0F;
            }
         } else {
            var1.textureLightsEnables2[13] = 0.0F;
            var1.textureLightsEnables2[2] = 0.0F;
         }
      }

      if (DebugOptions.instance.VehicleCycleColor.getValue()) {
         float var15 = (float)(System.currentTimeMillis() % 2000L);
         float var16 = (float)(System.currentTimeMillis() % 7000L);
         float var17 = (float)(System.currentTimeMillis() % 11000L);
         var1.painColor.x = var15 / 2000.0F;
         var1.painColor.y = var16 / 7000.0F;
         var1.painColor.z = var17 / 11000.0F;
      }

      if (DebugOptions.instance.VehicleRenderBlood0.getValue()) {
         Arrays.fill(var1.matrixBlood1Enables1, 0.0F);
         Arrays.fill(var1.matrixBlood1Enables2, 0.0F);
         Arrays.fill(var1.matrixBlood2Enables1, 0.0F);
         Arrays.fill(var1.matrixBlood2Enables2, 0.0F);
      }

      if (DebugOptions.instance.VehicleRenderBlood50.getValue()) {
         Arrays.fill(var1.matrixBlood1Enables1, 0.5F);
         Arrays.fill(var1.matrixBlood1Enables2, 0.5F);
         Arrays.fill(var1.matrixBlood2Enables1, 1.0F);
         Arrays.fill(var1.matrixBlood2Enables2, 1.0F);
      }

      if (DebugOptions.instance.VehicleRenderBlood100.getValue()) {
         Arrays.fill(var1.matrixBlood1Enables1, 1.0F);
         Arrays.fill(var1.matrixBlood1Enables2, 1.0F);
         Arrays.fill(var1.matrixBlood2Enables1, 1.0F);
         Arrays.fill(var1.matrixBlood2Enables2, 1.0F);
      }

      if (DebugOptions.instance.VehicleRenderDamage0.getValue()) {
         Arrays.fill(var1.textureDamage1Enables1, 0.0F);
         Arrays.fill(var1.textureDamage1Enables2, 0.0F);
         Arrays.fill(var1.textureDamage2Enables1, 0.0F);
         Arrays.fill(var1.textureDamage2Enables2, 0.0F);
      }

      if (DebugOptions.instance.VehicleRenderDamage1.getValue()) {
         Arrays.fill(var1.textureDamage1Enables1, 1.0F);
         Arrays.fill(var1.textureDamage1Enables2, 1.0F);
         Arrays.fill(var1.textureDamage2Enables1, 0.0F);
         Arrays.fill(var1.textureDamage2Enables2, 0.0F);
      }

      if (DebugOptions.instance.VehicleRenderDamage2.getValue()) {
         Arrays.fill(var1.textureDamage1Enables1, 0.0F);
         Arrays.fill(var1.textureDamage1Enables2, 0.0F);
         Arrays.fill(var1.textureDamage2Enables1, 1.0F);
         Arrays.fill(var1.textureDamage2Enables2, 1.0F);
      }

      if (DebugOptions.instance.VehicleRenderRust0.getValue()) {
         var1.textureRustA = 0.0F;
      }

      if (DebugOptions.instance.VehicleRenderRust50.getValue()) {
         var1.textureRustA = 0.5F;
      }

      if (DebugOptions.instance.VehicleRenderRust100.getValue()) {
         var1.textureRustA = 1.0F;
      }

      var1.refBody = 0.3F;
      var1.refWindows = 0.4F;
      if (this.rust > 0.8F) {
         var1.refBody = 0.1F;
         var1.refWindows = 0.2F;
      }

   }

   private void updateWorldLights() {
      if (!this.script.getLightbar().enable) {
         this.removeWorldLights();
      } else if (this.lightbarLightsMode.isEnable() && !(this.getBatteryCharge() <= 0.0F)) {
         if (this.lightbarLightsMode.getLightTexIndex() == 0) {
            this.removeWorldLights();
         } else {
            this.leftLight1.radius = this.leftLight2.radius = this.rightLight1.radius = this.rightLight2.radius = 8;
            Vector3f var1;
            int var2;
            int var3;
            int var4;
            int var5;
            IsoLightSource var6;
            if (this.lightbarLightsMode.getLightTexIndex() == 1) {
               var1 = this.getWorldPos(0.4F, 0.0F, 0.0F, (Vector3f)((BaseVehicle.Vector3fObjectPool)TL_vector3f_pool.get()).alloc());
               var2 = (int)var1.x;
               var3 = (int)var1.y;
               var4 = (int)(this.getZ() + 1.0F);
               ((BaseVehicle.Vector3fObjectPool)TL_vector3f_pool.get()).release(var1);
               var5 = this.leftLightIndex;
               if (var5 == 1 && this.leftLight1.x == var2 && this.leftLight1.y == var3 && this.leftLight1.z == var4) {
                  return;
               }

               if (var5 == 2 && this.leftLight2.x == var2 && this.leftLight2.y == var3 && this.leftLight2.z == var4) {
                  return;
               }

               this.removeWorldLights();
               if (var5 == 1) {
                  var6 = this.leftLight2;
                  this.leftLightIndex = 2;
               } else {
                  var6 = this.leftLight1;
                  this.leftLightIndex = 1;
               }

               var6.life = -1;
               var6.x = var2;
               var6.y = var3;
               var6.z = var4;
               IsoWorld.instance.CurrentCell.addLamppost(var6);
            } else {
               var1 = this.getWorldPos(-0.4F, 0.0F, 0.0F, (Vector3f)((BaseVehicle.Vector3fObjectPool)TL_vector3f_pool.get()).alloc());
               var2 = (int)var1.x;
               var3 = (int)var1.y;
               var4 = (int)(this.getZ() + 1.0F);
               ((BaseVehicle.Vector3fObjectPool)TL_vector3f_pool.get()).release(var1);
               var5 = this.rightLightIndex;
               if (var5 == 1 && this.rightLight1.x == var2 && this.rightLight1.y == var3 && this.rightLight1.z == var4) {
                  return;
               }

               if (var5 == 2 && this.rightLight2.x == var2 && this.rightLight2.y == var3 && this.rightLight2.z == var4) {
                  return;
               }

               this.removeWorldLights();
               if (var5 == 1) {
                  var6 = this.rightLight2;
                  this.rightLightIndex = 2;
               } else {
                  var6 = this.rightLight1;
                  this.rightLightIndex = 1;
               }

               var6.life = -1;
               var6.x = var2;
               var6.y = var3;
               var6.z = var4;
               IsoWorld.instance.CurrentCell.addLamppost(var6);
            }

         }
      } else {
         this.removeWorldLights();
      }
   }

   public void fixLightbarModelLighting(IsoLightSource var1, Vector3f var2) {
      if (var1 != this.leftLight1 && var1 != this.leftLight2) {
         if (var1 == this.rightLight1 || var1 == this.rightLight2) {
            var2.set(-1.0F, 0.0F, 0.0F);
         }
      } else {
         var2.set(1.0F, 0.0F, 0.0F);
      }

   }

   private void removeWorldLights() {
      if (this.leftLightIndex == 1) {
         IsoWorld.instance.CurrentCell.removeLamppost(this.leftLight1);
         this.leftLightIndex = -1;
      }

      if (this.leftLightIndex == 2) {
         IsoWorld.instance.CurrentCell.removeLamppost(this.leftLight2);
         this.leftLightIndex = -1;
      }

      if (this.rightLightIndex == 1) {
         IsoWorld.instance.CurrentCell.removeLamppost(this.rightLight1);
         this.rightLightIndex = -1;
      }

      if (this.rightLightIndex == 2) {
         IsoWorld.instance.CurrentCell.removeLamppost(this.rightLight2);
         this.rightLightIndex = -1;
      }

   }

   public void doDamageOverlay() {
      if (this.sprite.modelSlot != null) {
         this.doDoorDamage();
         this.doWindowDamage();
         this.doOtherBodyWorkDamage();
         this.doBloodOverlay();
      }
   }

   private void checkDamage(VehiclePart var1, int var2, boolean var3) {
      if (var3 && var1 != null && var1.getId().startsWith("Window") && var1.getScriptModelById("Default") != null) {
         var3 = false;
      }

      VehicleModelInstance var4 = (VehicleModelInstance)this.sprite.modelSlot.model;

      try {
         var4.textureDamage1Enables1[var2] = 0.0F;
         var4.textureDamage2Enables1[var2] = 0.0F;
         var4.textureUninstall1[var2] = 0.0F;
         if (var1 != null && var1.getInventoryItem() != null) {
            if (var1.getInventoryItem().getCondition() < 60 && var1.getInventoryItem().getCondition() >= 40) {
               var4.textureDamage1Enables1[var2] = 1.0F;
            }

            if (var1.getInventoryItem().getCondition() < 40) {
               var4.textureDamage2Enables1[var2] = 1.0F;
            }

            if (var1.window != null && var1.window.isOpen() && var3) {
               var4.textureUninstall1[var2] = 1.0F;
            }
         } else if (var1 != null && var3) {
            var4.textureUninstall1[var2] = 1.0F;
         }
      } catch (Exception var6) {
         var6.printStackTrace();
      }

   }

   private void checkDamage2(VehiclePart var1, int var2, boolean var3) {
      VehicleModelInstance var4 = (VehicleModelInstance)this.sprite.modelSlot.model;

      try {
         var4.textureDamage1Enables2[var2] = 0.0F;
         var4.textureDamage2Enables2[var2] = 0.0F;
         var4.textureUninstall2[var2] = 0.0F;
         if (var1 != null && var1.getInventoryItem() != null) {
            if (var1.getInventoryItem().getCondition() < 60 && var1.getInventoryItem().getCondition() >= 40) {
               var4.textureDamage1Enables2[var2] = 1.0F;
            }

            if (var1.getInventoryItem().getCondition() < 40) {
               var4.textureDamage2Enables2[var2] = 1.0F;
            }

            if (var1.window != null && var1.window.isOpen() && var3) {
               var4.textureUninstall2[var2] = 1.0F;
            }
         } else if (var1 != null && var3) {
            var4.textureUninstall2[var2] = 1.0F;
         }
      } catch (Exception var6) {
         var6.printStackTrace();
      }

   }

   private void checkUninstall2(VehiclePart var1, int var2) {
      VehicleModelInstance var3 = (VehicleModelInstance)this.sprite.modelSlot.model;

      try {
         var3.textureUninstall2[var2] = 0.0F;
         if (var1 != null && var1.getInventoryItem() == null) {
            var3.textureUninstall2[var2] = 1.0F;
         }
      } catch (Exception var5) {
         var5.printStackTrace();
      }

   }

   private void doOtherBodyWorkDamage() {
      this.checkDamage(this.getPartById("EngineDoor"), 0, false);
      this.checkDamage(this.getPartById("EngineDoor"), 3, false);
      this.checkDamage(this.getPartById("EngineDoor"), 11, false);
      this.checkDamage2(this.getPartById("EngineDoor"), 6, true);
      this.checkDamage(this.getPartById("TruckBed"), 4, false);
      this.checkDamage(this.getPartById("TruckBed"), 7, false);
      this.checkDamage(this.getPartById("TruckBed"), 15, false);
      VehiclePart var1 = this.getPartById("TrunkDoor");
      if (var1 != null) {
         this.checkDamage2(var1, 10, true);
         if (var1.scriptPart.hasLightsRear) {
            this.checkUninstall2(var1, 12);
            this.checkUninstall2(var1, 1);
            this.checkUninstall2(var1, 5);
            this.checkUninstall2(var1, 9);
         }
      } else {
         var1 = this.getPartById("DoorRear");
         if (var1 != null) {
            this.checkDamage2(var1, 10, true);
            if (var1.scriptPart.hasLightsRear) {
               this.checkUninstall2(var1, 12);
               this.checkUninstall2(var1, 1);
               this.checkUninstall2(var1, 5);
               this.checkUninstall2(var1, 9);
            }
         }
      }

   }

   private void doWindowDamage() {
      this.checkDamage(this.getPartById("WindowFrontLeft"), 2, true);
      this.checkDamage(this.getPartById("WindowFrontRight"), 9, true);
      VehiclePart var1 = this.getPartById("WindowRearLeft");
      if (var1 != null) {
         this.checkDamage(var1, 6, true);
      } else {
         var1 = this.getPartById("WindowMiddleLeft");
         if (var1 != null) {
            this.checkDamage(var1, 6, true);
         }
      }

      var1 = this.getPartById("WindowRearRight");
      if (var1 != null) {
         this.checkDamage(var1, 13, true);
      } else {
         var1 = this.getPartById("WindowMiddleRight");
         if (var1 != null) {
            this.checkDamage(var1, 13, true);
         }
      }

      this.checkDamage(this.getPartById("Windshield"), 10, true);
      this.checkDamage(this.getPartById("WindshieldRear"), 14, true);
   }

   private void doDoorDamage() {
      this.checkDamage(this.getPartById("DoorFrontLeft"), 1, true);
      this.checkDamage(this.getPartById("DoorFrontRight"), 8, true);
      VehiclePart var1 = this.getPartById("DoorRearLeft");
      if (var1 != null) {
         this.checkDamage(var1, 5, true);
      } else {
         var1 = this.getPartById("DoorMiddleLeft");
         if (var1 != null) {
            this.checkDamage(var1, 5, true);
         }
      }

      var1 = this.getPartById("DoorRearRight");
      if (var1 != null) {
         this.checkDamage(var1, 12, true);
      } else {
         var1 = this.getPartById("DoorMiddleRight");
         if (var1 != null) {
            this.checkDamage(var1, 12, true);
         }
      }

   }

   public float getBloodIntensity(String var1) {
      return (float)((Byte)this.bloodIntensity.getOrDefault(var1, BYTE_ZERO) & 255) / 100.0F;
   }

   public void setBloodIntensity(String var1, float var2) {
      byte var3 = (byte)((int)(PZMath.clamp(var2, 0.0F, 1.0F) * 100.0F));
      if (!this.bloodIntensity.containsKey(var1) || var3 != (Byte)this.bloodIntensity.get(var1)) {
         this.bloodIntensity.put(var1, var3);
         this.doBloodOverlay();
         this.transmitBlood();
      }
   }

   public void transmitBlood() {
      if (GameServer.bServer) {
         this.updateFlags = (short)(this.updateFlags | 4096);
      }
   }

   public void doBloodOverlay() {
      if (this.sprite.modelSlot != null) {
         VehicleModelInstance var1 = (VehicleModelInstance)this.sprite.modelSlot.model;
         Arrays.fill(var1.matrixBlood1Enables1, 0.0F);
         Arrays.fill(var1.matrixBlood1Enables2, 0.0F);
         Arrays.fill(var1.matrixBlood2Enables1, 0.0F);
         Arrays.fill(var1.matrixBlood2Enables2, 0.0F);
         if (Core.getInstance().getOptionBloodDecals() != 0) {
            this.doBloodOverlayFront(var1.matrixBlood1Enables1, var1.matrixBlood1Enables2, this.getBloodIntensity("Front"));
            this.doBloodOverlayRear(var1.matrixBlood1Enables1, var1.matrixBlood1Enables2, this.getBloodIntensity("Rear"));
            this.doBloodOverlayLeft(var1.matrixBlood1Enables1, var1.matrixBlood1Enables2, this.getBloodIntensity("Left"));
            this.doBloodOverlayRight(var1.matrixBlood1Enables1, var1.matrixBlood1Enables2, this.getBloodIntensity("Right"));
            Iterator var2 = this.bloodIntensity.entrySet().iterator();

            while(var2.hasNext()) {
               Entry var3 = (Entry)var2.next();
               Integer var4 = (Integer)s_PartToMaskMap.get(var3.getKey());
               if (var4 != null) {
                  var1.matrixBlood1Enables1[var4] = (float)((Byte)var3.getValue() & 255) / 100.0F;
               }
            }

            this.doBloodOverlayAux(var1.matrixBlood2Enables1, var1.matrixBlood2Enables2, 1.0F);
         }
      }
   }

   private void doBloodOverlayAux(float[] var1, float[] var2, float var3) {
      var1[0] = var3;
      var2[6] = var3;
      var2[4] = var3;
      var2[8] = var3;
      var1[4] = var3;
      var1[7] = var3;
      var1[15] = var3;
      var2[10] = var3;
      var2[12] = var3;
      var2[1] = var3;
      var2[5] = var3;
      var2[9] = var3;
      var1[3] = var3;
      var1[8] = var3;
      var1[12] = var3;
      var1[11] = var3;
      var1[1] = var3;
      var1[5] = var3;
      var2[0] = var3;
      var1[10] = var3;
      var1[14] = var3;
      var1[9] = var3;
      var1[13] = var3;
      var1[2] = var3;
      var1[6] = var3;
   }

   private void doBloodOverlayFront(float[] var1, float[] var2, float var3) {
      var1[0] = var3;
      var2[6] = var3;
      var2[4] = var3;
      var2[8] = var3;
      var1[10] = var3;
   }

   private void doBloodOverlayRear(float[] var1, float[] var2, float var3) {
      var1[4] = var3;
      var2[10] = var3;
      var2[12] = var3;
      var2[1] = var3;
      var2[5] = var3;
      var2[9] = var3;
      var1[14] = var3;
   }

   private void doBloodOverlayLeft(float[] var1, float[] var2, float var3) {
      var1[11] = var3;
      var1[1] = var3;
      var1[5] = var3;
      var1[15] = var3;
      var1[2] = var3;
      var1[6] = var3;
   }

   private void doBloodOverlayRight(float[] var1, float[] var2, float var3) {
      var1[3] = var3;
      var1[8] = var3;
      var1[12] = var3;
      var1[7] = var3;
      var1[9] = var3;
      var1[13] = var3;
   }

   public void render(float var1, float var2, float var3, ColorInfo var4, boolean var5, boolean var6, Shader var7) {
      if (this.script != null) {
         if (this.physics != null) {
            this.physics.debug();
         }

         int var8 = IsoCamera.frameState.playerIndex;
         boolean var9 = IsoCamera.CamCharacter != null && IsoCamera.CamCharacter.getVehicle() == this;
         if (var9 || this.square.lighting[var8].bSeen()) {
            if (!var9 && !this.square.lighting[var8].bCouldSee()) {
               this.setTargetAlpha(var8, 0.0F);
            } else {
               this.setTargetAlpha(var8, 1.0F);
            }

            if (this.sprite.hasActiveModel()) {
               this.updateLights();
               boolean var10 = Core.getInstance().getOptionBloodDecals() != 0;
               if (this.OptionBloodDecals != var10) {
                  this.OptionBloodDecals = var10;
                  this.doBloodOverlay();
               }

               var4.a = this.getAlpha(var8);
               inf.a = var4.a;
               inf.r = var4.r;
               inf.g = var4.g;
               inf.b = var4.b;
               this.sprite.renderVehicle(this.def, this, var1, var2, 0.0F, 0.0F, 0.0F, inf, true);
            }

            this.updateAlpha(var8);
            if (Core.bDebug && DebugOptions.instance.VehicleRenderArea.getValue()) {
               this.renderAreas();
            }

            if (Core.bDebug && DebugOptions.instance.VehicleRenderAttackPositions.getValue()) {
               this.m_surroundVehicle.render();
            }

            if (Core.bDebug && DebugOptions.instance.VehicleRenderExit.getValue()) {
               this.renderExits();
            }

            if (Core.bDebug && DebugOptions.instance.VehicleRenderIntersectedSquares.getValue()) {
               this.renderIntersectedSquares();
            }

            if (Core.bDebug && DebugOptions.instance.VehicleRenderAuthorizations.getValue()) {
               this.renderAuthorizations();
            }

            if (DebugOptions.instance.VehicleRenderTrailerPositions.getValue()) {
               this.renderTrailerPositions();
            }

            this.renderUsableArea();
         }
      }
   }

   public void renderlast() {
      int var1 = IsoCamera.frameState.playerIndex;

      for(int var2 = 0; var2 < this.parts.size(); ++var2) {
         VehiclePart var3 = (VehiclePart)this.parts.get(var2);
         if (var3.chatElement != null && var3.chatElement.getHasChatToDisplay()) {
            if (var3.getDeviceData() != null && !var3.getDeviceData().getIsTurnedOn()) {
               var3.chatElement.clear(var1);
            } else {
               float var4 = IsoUtils.XToScreen(this.getX(), this.getY(), this.getZ(), 0);
               float var5 = IsoUtils.YToScreen(this.getX(), this.getY(), this.getZ(), 0);
               var4 = var4 - IsoCamera.getOffX() - this.offsetX;
               var5 = var5 - IsoCamera.getOffY() - this.offsetY;
               var4 += (float)(32 * Core.TileScale);
               var5 += (float)(20 * Core.TileScale);
               var4 /= Core.getInstance().getZoom(var1);
               var5 /= Core.getInstance().getZoom(var1);
               var3.chatElement.renderBatched(var1, (int)var4, (int)var5);
            }
         }
      }

   }

   public void renderShadow() {
      if (this.physics != null) {
         if (this.script != null) {
            int var1 = IsoCamera.frameState.playerIndex;
            if (this.square.lighting[var1].bSeen()) {
               if (this.square.lighting[var1].bCouldSee()) {
                  this.setTargetAlpha(var1, 1.0F);
               } else {
                  this.setTargetAlpha(var1, 0.0F);
               }

               Texture var2 = this.getShadowTexture();
               if (var2 != null && this.getCurrentSquare() != null) {
                  float var3 = 0.6F * this.getAlpha(var1);
                  ColorInfo var4 = this.getCurrentSquare().lighting[var1].lightInfo();
                  var3 *= (var4.r + var4.g + var4.b) / 3.0F;
                  if (this.polyDirty) {
                     this.getPoly();
                  }

                  SpriteRenderer.instance.renderPoly(var2, (float)((int)IsoUtils.XToScreenExact(this.shadowCoord.x2, this.shadowCoord.y2, 0.0F, 0)), (float)((int)IsoUtils.YToScreenExact(this.shadowCoord.x2, this.shadowCoord.y2, 0.0F, 0)), (float)((int)IsoUtils.XToScreenExact(this.shadowCoord.x1, this.shadowCoord.y1, 0.0F, 0)), (float)((int)IsoUtils.YToScreenExact(this.shadowCoord.x1, this.shadowCoord.y1, 0.0F, 0)), (float)((int)IsoUtils.XToScreenExact(this.shadowCoord.x4, this.shadowCoord.y4, 0.0F, 0)), (float)((int)IsoUtils.YToScreenExact(this.shadowCoord.x4, this.shadowCoord.y4, 0.0F, 0)), (float)((int)IsoUtils.XToScreenExact(this.shadowCoord.x3, this.shadowCoord.y3, 0.0F, 0)), (float)((int)IsoUtils.YToScreenExact(this.shadowCoord.x3, this.shadowCoord.y3, 0.0F, 0)), 1.0F, 1.0F, 1.0F, 0.8F * var3);
               }

            }
         }
      }
   }

   public boolean isEnterBlocked(IsoGameCharacter var1, int var2) {
      return this.isExitBlocked(var2);
   }

   public boolean isExitBlocked(int var1) {
      VehicleScript.Position var2 = this.getPassengerPosition(var1, "inside");
      VehicleScript.Position var3 = this.getPassengerPosition(var1, "outside");
      if (var2 != null && var3 != null) {
         Vector3f var4 = this.getPassengerPositionWorldPos(var3, (Vector3f)((BaseVehicle.Vector3fObjectPool)TL_vector3f_pool.get()).alloc());
         if (var3.area != null) {
            Vector2 var5 = (Vector2)((BaseVehicle.Vector2ObjectPool)TL_vector2_pool.get()).alloc();
            VehicleScript.Area var6 = this.script.getAreaById(var3.area);
            Vector2 var7 = this.areaPositionWorld4PlayerInteract(var6, var5);
            if (var7 != null) {
               var4.x = var7.x;
               var4.y = var7.y;
            }

            ((BaseVehicle.Vector2ObjectPool)TL_vector2_pool.get()).release(var5);
         }

         var4.z = 0.0F;
         Vector3f var8 = this.getPassengerPositionWorldPos(var2, (Vector3f)((BaseVehicle.Vector3fObjectPool)TL_vector3f_pool.get()).alloc());
         boolean var9 = PolygonalMap2.instance.lineClearCollide(var8.x, var8.y, var4.x, var4.y, (int)this.z, this, false, false);
         ((BaseVehicle.Vector3fObjectPool)TL_vector3f_pool.get()).release(var4);
         ((BaseVehicle.Vector3fObjectPool)TL_vector3f_pool.get()).release(var8);
         return var9;
      } else {
         return true;
      }
   }

   public boolean isPassengerUseDoor2(IsoGameCharacter var1, int var2) {
      VehicleScript.Position var3 = this.getPassengerPosition(var2, "outside2");
      if (var3 != null) {
         Vector3f var4 = this.getPassengerPositionWorldPos(var3, (Vector3f)((BaseVehicle.Vector3fObjectPool)TL_vector3f_pool.get()).alloc());
         var4.sub(var1.x, var1.y, var1.z);
         float var5 = var4.length();
         ((BaseVehicle.Vector3fObjectPool)TL_vector3f_pool.get()).release(var4);
         if (var5 < 2.0F) {
            return true;
         }
      }

      return false;
   }

   public boolean isEnterBlocked2(IsoGameCharacter var1, int var2) {
      return this.isExitBlocked2(var2);
   }

   public boolean isExitBlocked2(int var1) {
      VehicleScript.Position var2 = this.getPassengerPosition(var1, "inside");
      VehicleScript.Position var3 = this.getPassengerPosition(var1, "outside2");
      if (var2 != null && var3 != null) {
         Vector3f var4 = this.getPassengerPositionWorldPos(var3, (Vector3f)((BaseVehicle.Vector3fObjectPool)TL_vector3f_pool.get()).alloc());
         var4.z = 0.0F;
         Vector3f var5 = this.getPassengerPositionWorldPos(var2, (Vector3f)((BaseVehicle.Vector3fObjectPool)TL_vector3f_pool.get()).alloc());
         boolean var6 = PolygonalMap2.instance.lineClearCollide(var5.x, var5.y, var4.x, var4.y, (int)this.z, this, false, false);
         ((BaseVehicle.Vector3fObjectPool)TL_vector3f_pool.get()).release(var4);
         ((BaseVehicle.Vector3fObjectPool)TL_vector3f_pool.get()).release(var5);
         return var6;
      } else {
         return true;
      }
   }

   private void renderExits() {
      int var1 = Core.TileScale;
      Vector3f var2 = (Vector3f)((BaseVehicle.Vector3fObjectPool)TL_vector3f_pool.get()).alloc();
      Vector3f var3 = (Vector3f)((BaseVehicle.Vector3fObjectPool)TL_vector3f_pool.get()).alloc();

      for(int var4 = 0; var4 < this.getMaxPassengers(); ++var4) {
         VehicleScript.Position var5 = this.getPassengerPosition(var4, "inside");
         VehicleScript.Position var6 = this.getPassengerPosition(var4, "outside");
         if (var5 != null && var6 != null) {
            float var7 = 0.3F;
            this.getPassengerPositionWorldPos(var6, var2);
            this.getPassengerPositionWorldPos(var5, var3);
            int var8 = (int)Math.floor((double)(var2.x - var7));
            int var9 = (int)Math.floor((double)(var2.x + var7));
            int var10 = (int)Math.floor((double)(var2.y - var7));
            int var11 = (int)Math.floor((double)(var2.y + var7));

            for(int var12 = var10; var12 <= var11; ++var12) {
               for(int var13 = var8; var13 <= var9; ++var13) {
                  int var14 = (int)IsoUtils.XToScreenExact((float)var13, (float)(var12 + 1), (float)((int)this.z), 0);
                  int var15 = (int)IsoUtils.YToScreenExact((float)var13, (float)(var12 + 1), (float)((int)this.z), 0);
                  SpriteRenderer.instance.renderPoly((float)var14, (float)var15, (float)(var14 + 32 * var1), (float)(var15 - 16 * var1), (float)(var14 + 64 * var1), (float)var15, (float)(var14 + 32 * var1), (float)(var15 + 16 * var1), 1.0F, 1.0F, 1.0F, 0.5F);
               }
            }

            float var16 = 1.0F;
            float var17 = 1.0F;
            float var18 = 1.0F;
            if (this.isExitBlocked(var4)) {
               var18 = 0.0F;
               var17 = 0.0F;
            }

            this.getController().drawCircle(var3.x, var3.y, var7, 0.0F, 0.0F, 1.0F, 1.0F);
            this.getController().drawCircle(var2.x, var2.y, var7, var16, var17, var18, 1.0F);
         }
      }

      ((BaseVehicle.Vector3fObjectPool)TL_vector3f_pool.get()).release(var2);
      ((BaseVehicle.Vector3fObjectPool)TL_vector3f_pool.get()).release(var3);
   }

   private Vector2 areaPositionLocal(VehicleScript.Area var1) {
      return this.areaPositionLocal(var1, new Vector2());
   }

   private Vector2 areaPositionLocal(VehicleScript.Area var1, Vector2 var2) {
      Vector2 var3 = this.areaPositionWorld(var1, var2);
      Vector3f var4 = (Vector3f)((BaseVehicle.Vector3fObjectPool)TL_vector3f_pool.get()).alloc();
      this.getLocalPos(var3.x, var3.y, 0.0F, var4);
      var3.set(var4.x, var4.z);
      ((BaseVehicle.Vector3fObjectPool)TL_vector3f_pool.get()).release(var4);
      return var3;
   }

   public Vector2 areaPositionWorld(VehicleScript.Area var1) {
      return this.areaPositionWorld(var1, new Vector2());
   }

   public Vector2 areaPositionWorld(VehicleScript.Area var1, Vector2 var2) {
      if (var1 == null) {
         return null;
      } else {
         Vector3f var3 = this.getWorldPos(var1.x, 0.0F, var1.y, (Vector3f)((BaseVehicle.Vector3fObjectPool)TL_vector3f_pool.get()).alloc());
         var2.set(var3.x, var3.y);
         ((BaseVehicle.Vector3fObjectPool)TL_vector3f_pool.get()).release(var3);
         return var2;
      }
   }

   public Vector2 areaPositionWorld4PlayerInteract(VehicleScript.Area var1) {
      return this.areaPositionWorld4PlayerInteract(var1, new Vector2());
   }

   public Vector2 areaPositionWorld4PlayerInteract(VehicleScript.Area var1, Vector2 var2) {
      Vector3f var3 = this.script.getExtents();
      Vector3f var4 = this.script.getCenterOfMassOffset();
      Vector2 var5 = this.areaPositionWorld(var1, var2);
      Vector3f var6 = this.getLocalPos(var5.x, var5.y, 0.0F, (Vector3f)((BaseVehicle.Vector3fObjectPool)TL_vector3f_pool.get()).alloc());
      if (!(var1.x > var4.x + var3.x / 2.0F) && !(var1.x < var4.x - var3.x / 2.0F)) {
         if (var1.y > 0.0F) {
            var6.z -= var1.h * 0.3F;
         } else {
            var6.z += var1.h * 0.3F;
         }
      } else if (var1.x > 0.0F) {
         var6.x -= var1.w * 0.3F;
      } else {
         var6.x += var1.w * 0.3F;
      }

      this.getWorldPos(var6, var6);
      var2.set(var6.x, var6.y);
      ((BaseVehicle.Vector3fObjectPool)TL_vector3f_pool.get()).release(var6);
      return var2;
   }

   private void renderAreas() {
      if (this.getScript() != null) {
         Vector3f var1 = this.getForwardVector((Vector3f)((BaseVehicle.Vector3fObjectPool)TL_vector3f_pool.get()).alloc());
         Vector2 var2 = (Vector2)((BaseVehicle.Vector2ObjectPool)TL_vector2_pool.get()).alloc();

         for(int var3 = 0; var3 < this.parts.size(); ++var3) {
            VehiclePart var4 = (VehiclePart)this.parts.get(var3);
            if (var4.getArea() != null) {
               VehicleScript.Area var5 = this.getScript().getAreaById(var4.getArea());
               if (var5 != null) {
                  Vector2 var6 = this.areaPositionWorld(var5, var2);
                  if (var6 != null) {
                     boolean var7 = this.isInArea(var5.id, IsoPlayer.getInstance());
                     this.getController().drawRect(var1, var6.x - WorldSimulation.instance.offsetX, var6.y - WorldSimulation.instance.offsetY, var5.w, var5.h / 2.0F, var7 ? 0.0F : 0.65F, var7 ? 1.0F : 0.65F, var7 ? 1.0F : 0.65F);
                     var6 = this.areaPositionWorld4PlayerInteract(var5, var2);
                     this.getController().drawRect(var1, var6.x - WorldSimulation.instance.offsetX, var6.y - WorldSimulation.instance.offsetY, 0.1F, 0.1F, 1.0F, 0.0F, 0.0F);
                  }
               }
            }
         }

         ((BaseVehicle.Vector3fObjectPool)TL_vector3f_pool.get()).release(var1);
         ((BaseVehicle.Vector2ObjectPool)TL_vector2_pool.get()).release(var2);
         LineDrawer.drawLine(IsoUtils.XToScreenExact(this.poly.x1, this.poly.y1, 0.0F, 0), IsoUtils.YToScreenExact(this.poly.x1, this.poly.y1, 0.0F, 0), IsoUtils.XToScreenExact(this.poly.x2, this.poly.y2, 0.0F, 0), IsoUtils.YToScreenExact(this.poly.x2, this.poly.y2, 0.0F, 0), 1.0F, 0.5F, 0.5F, 1.0F, 0);
         LineDrawer.drawLine(IsoUtils.XToScreenExact(this.poly.x2, this.poly.y2, 0.0F, 0), IsoUtils.YToScreenExact(this.poly.x2, this.poly.y2, 0.0F, 0), IsoUtils.XToScreenExact(this.poly.x3, this.poly.y3, 0.0F, 0), IsoUtils.YToScreenExact(this.poly.x3, this.poly.y3, 0.0F, 0), 1.0F, 0.5F, 0.5F, 1.0F, 0);
         LineDrawer.drawLine(IsoUtils.XToScreenExact(this.poly.x3, this.poly.y3, 0.0F, 0), IsoUtils.YToScreenExact(this.poly.x3, this.poly.y3, 0.0F, 0), IsoUtils.XToScreenExact(this.poly.x4, this.poly.y4, 0.0F, 0), IsoUtils.YToScreenExact(this.poly.x4, this.poly.y4, 0.0F, 0), 1.0F, 0.5F, 0.5F, 1.0F, 0);
         LineDrawer.drawLine(IsoUtils.XToScreenExact(this.poly.x4, this.poly.y4, 0.0F, 0), IsoUtils.YToScreenExact(this.poly.x4, this.poly.y4, 0.0F, 0), IsoUtils.XToScreenExact(this.poly.x1, this.poly.y1, 0.0F, 0), IsoUtils.YToScreenExact(this.poly.x1, this.poly.y1, 0.0F, 0), 1.0F, 0.5F, 0.5F, 1.0F, 0);
         LineDrawer.drawLine(IsoUtils.XToScreenExact(this.shadowCoord.x1, this.shadowCoord.y1, 0.0F, 0), IsoUtils.YToScreenExact(this.shadowCoord.x1, this.shadowCoord.y1, 0.0F, 0), IsoUtils.XToScreenExact(this.shadowCoord.x2, this.shadowCoord.y2, 0.0F, 0), IsoUtils.YToScreenExact(this.shadowCoord.x2, this.shadowCoord.y2, 0.0F, 0), 0.5F, 1.0F, 0.5F, 1.0F, 0);
         LineDrawer.drawLine(IsoUtils.XToScreenExact(this.shadowCoord.x2, this.shadowCoord.y2, 0.0F, 0), IsoUtils.YToScreenExact(this.shadowCoord.x2, this.shadowCoord.y2, 0.0F, 0), IsoUtils.XToScreenExact(this.shadowCoord.x3, this.shadowCoord.y3, 0.0F, 0), IsoUtils.YToScreenExact(this.shadowCoord.x3, this.shadowCoord.y3, 0.0F, 0), 0.5F, 1.0F, 0.5F, 1.0F, 0);
         LineDrawer.drawLine(IsoUtils.XToScreenExact(this.shadowCoord.x3, this.shadowCoord.y3, 0.0F, 0), IsoUtils.YToScreenExact(this.shadowCoord.x3, this.shadowCoord.y3, 0.0F, 0), IsoUtils.XToScreenExact(this.shadowCoord.x4, this.shadowCoord.y4, 0.0F, 0), IsoUtils.YToScreenExact(this.shadowCoord.x4, this.shadowCoord.y4, 0.0F, 0), 0.5F, 1.0F, 0.5F, 1.0F, 0);
         LineDrawer.drawLine(IsoUtils.XToScreenExact(this.shadowCoord.x4, this.shadowCoord.y4, 0.0F, 0), IsoUtils.YToScreenExact(this.shadowCoord.x4, this.shadowCoord.y4, 0.0F, 0), IsoUtils.XToScreenExact(this.shadowCoord.x1, this.shadowCoord.y1, 0.0F, 0), IsoUtils.YToScreenExact(this.shadowCoord.x1, this.shadowCoord.y1, 0.0F, 0), 0.5F, 1.0F, 0.5F, 1.0F, 0);
      }
   }

   private void renderAuthorizations() {
      float var1 = 0.3F;
      float var2 = 0.3F;
      float var3 = 0.3F;
      if (this.netPlayerAuthorization == 0) {
         var1 = 1.0F;
      }

      if (this.netPlayerAuthorization == 1) {
         var3 = 1.0F;
      }

      if (this.netPlayerAuthorization == 3) {
         var2 = 1.0F;
      }

      if (this.netPlayerAuthorization == 4) {
         var2 = 1.0F;
         var1 = 1.0F;
      }

      if (this.netPlayerAuthorization == 2) {
         var3 = 1.0F;
         var1 = 1.0F;
      }

      LineDrawer.drawLine(IsoUtils.XToScreenExact(this.poly.x1, this.poly.y1, 0.0F, 0), IsoUtils.YToScreenExact(this.poly.x1, this.poly.y1, 0.0F, 0), IsoUtils.XToScreenExact(this.poly.x2, this.poly.y2, 0.0F, 0), IsoUtils.YToScreenExact(this.poly.x2, this.poly.y2, 0.0F, 0), var1, var2, var3, 1.0F, 0);
      LineDrawer.drawLine(IsoUtils.XToScreenExact(this.poly.x2, this.poly.y2, 0.0F, 0), IsoUtils.YToScreenExact(this.poly.x2, this.poly.y2, 0.0F, 0), IsoUtils.XToScreenExact(this.poly.x3, this.poly.y3, 0.0F, 0), IsoUtils.YToScreenExact(this.poly.x3, this.poly.y3, 0.0F, 0), var1, var2, var3, 1.0F, 0);
      LineDrawer.drawLine(IsoUtils.XToScreenExact(this.poly.x3, this.poly.y3, 0.0F, 0), IsoUtils.YToScreenExact(this.poly.x3, this.poly.y3, 0.0F, 0), IsoUtils.XToScreenExact(this.poly.x4, this.poly.y4, 0.0F, 0), IsoUtils.YToScreenExact(this.poly.x4, this.poly.y4, 0.0F, 0), var1, var2, var3, 1.0F, 0);
      LineDrawer.drawLine(IsoUtils.XToScreenExact(this.poly.x4, this.poly.y4, 0.0F, 0), IsoUtils.YToScreenExact(this.poly.x4, this.poly.y4, 0.0F, 0), IsoUtils.XToScreenExact(this.poly.x1, this.poly.y1, 0.0F, 0), IsoUtils.YToScreenExact(this.poly.x1, this.poly.y1, 0.0F, 0), var1, var2, var3, 1.0F, 0);
      TextManager.instance.DrawString((double)IsoUtils.XToScreenExact(this.x, this.y, 0.0F, 0), (double)IsoUtils.YToScreenExact(this.x, this.y, 0.0F, 0), "Authorizations VID:" + this.VehicleID + "\n auth:" + this.netPlayerAuthorization + "\n authPID:" + this.netPlayerId + "\n authTimeout:" + this.netPlayerTimeout);
   }

   private void renderUsableArea() {
      if (this.getScript() != null && UIManager.VisibleAllUI) {
         VehiclePart var1 = this.getUseablePart(IsoPlayer.getInstance());
         if (var1 != null) {
            VehicleScript.Area var2 = this.getScript().getAreaById(var1.getArea());
            if (var2 != null) {
               Vector2 var3 = (Vector2)((BaseVehicle.Vector2ObjectPool)TL_vector2_pool.get()).alloc();
               Vector2 var4 = this.areaPositionWorld(var2, var3);
               if (var4 == null) {
                  ((BaseVehicle.Vector2ObjectPool)TL_vector2_pool.get()).release(var3);
               } else {
                  Vector3f var5 = this.getForwardVector((Vector3f)((BaseVehicle.Vector3fObjectPool)TL_vector3f_pool.get()).alloc());
                  float var6 = 0.0F;
                  float var7 = 1.0F;
                  float var8 = 0.0F;
                  this.getController().drawRect(var5, var4.x - WorldSimulation.instance.offsetX, var4.y - WorldSimulation.instance.offsetY, var2.w, var2.h / 2.0F, var6, var7, var8);
                  var5.x *= var2.h / this.script.getModelScale();
                  var5.z *= var2.h / this.script.getModelScale();
                  if (var1.getDoor() != null && (var1.getId().contains("Left") || var1.getId().contains("Right"))) {
                     if (var1.getId().contains("Front")) {
                        this.getController().drawRect(var5, var4.x - WorldSimulation.instance.offsetX + var5.x * var2.h / 2.0F, var4.y - WorldSimulation.instance.offsetY + var5.z * var2.h / 2.0F, var2.w, var2.h / 8.0F, var6, var7, var8);
                     } else if (var1.getId().contains("Rear")) {
                        this.getController().drawRect(var5, var4.x - WorldSimulation.instance.offsetX - var5.x * var2.h / 2.0F, var4.y - WorldSimulation.instance.offsetY - var5.z * var2.h / 2.0F, var2.w, var2.h / 8.0F, var6, var7, var8);
                     }
                  }

                  ((BaseVehicle.Vector2ObjectPool)TL_vector2_pool.get()).release(var3);
                  ((BaseVehicle.Vector3fObjectPool)TL_vector3f_pool.get()).release(var5);
               }
            }
         }
      }
   }

   private void renderIntersectedSquares() {
      PolygonalMap2.VehiclePoly var1 = this.getPoly();
      float var2 = Math.min(var1.x1, Math.min(var1.x2, Math.min(var1.x3, var1.x4)));
      float var3 = Math.min(var1.y1, Math.min(var1.y2, Math.min(var1.y3, var1.y4)));
      float var4 = Math.max(var1.x1, Math.max(var1.x2, Math.max(var1.x3, var1.x4)));
      float var5 = Math.max(var1.y1, Math.max(var1.y2, Math.max(var1.y3, var1.y4)));

      for(int var6 = (int)var3; var6 < (int)Math.ceil((double)var5); ++var6) {
         for(int var7 = (int)var2; var7 < (int)Math.ceil((double)var4); ++var7) {
            if (this.isIntersectingSquare(var7, var6, (int)this.z)) {
               LineDrawer.addLine((float)var7, (float)var6, (float)((int)this.z), (float)(var7 + 1), (float)(var6 + 1), (float)((int)this.z), 1.0F, 1.0F, 1.0F, (String)null, false);
            }
         }
      }

   }

   private void renderTrailerPositions() {
      if (this.script != null && this.physics != null) {
         Vector3f var1 = (Vector3f)((BaseVehicle.Vector3fObjectPool)TL_vector3f_pool.get()).alloc();
         Vector3f var2 = (Vector3f)((BaseVehicle.Vector3fObjectPool)TL_vector3f_pool.get()).alloc();
         Vector3f var3 = this.getTowingWorldPos("trailer", var2);
         if (var3 != null) {
            this.physics.drawCircle(var3.x, var3.y, 0.3F, 1.0F, 1.0F, 1.0F, 1.0F);
         }

         Vector3f var4 = this.getPlayerTrailerLocalPos("trailer", false, var1);
         boolean var5;
         if (var4 != null) {
            this.getWorldPos(var4, var4);
            var5 = PolygonalMap2.instance.lineClearCollide(var2.x, var2.y, var4.x, var4.y, (int)this.z, this, false, false);
            this.physics.drawCircle(var4.x, var4.y, 0.3F, 1.0F, 1.0F, 1.0F, 1.0F);
            if (var5) {
               LineDrawer.addLine(var4.x, var4.y, 0.0F, var2.x, var2.y, 0.0F, 1.0F, 0.0F, 0.0F, 1.0F);
            }
         }

         var4 = this.getPlayerTrailerLocalPos("trailer", true, var1);
         if (var4 != null) {
            this.getWorldPos(var4, var4);
            var5 = PolygonalMap2.instance.lineClearCollide(var2.x, var2.y, var4.x, var4.y, (int)this.z, this, false, false);
            this.physics.drawCircle(var4.x, var4.y, 0.3F, 1.0F, var5 ? 0.0F : 1.0F, var5 ? 0.0F : 1.0F, 1.0F);
            if (var5) {
               LineDrawer.addLine(var4.x, var4.y, 0.0F, var2.x, var2.y, 0.0F, 1.0F, 0.0F, 0.0F, 1.0F);
            }
         }

         ((BaseVehicle.Vector3fObjectPool)TL_vector3f_pool.get()).release(var1);
         ((BaseVehicle.Vector3fObjectPool)TL_vector3f_pool.get()).release(var2);
      }
   }

   public void getWheelForwardVector(int var1, Vector3f var2) {
      BaseVehicle.WheelInfo var3 = this.wheelInfo[var1];
      Matrix4f var4 = (Matrix4f)((BaseVehicle.Matrix4fObjectPool)TL_matrix4f_pool.get()).alloc();
      var4.rotationY(var3.steering);
      Matrix4f var5 = this.jniTransform.getMatrix((Matrix4f)((BaseVehicle.Matrix4fObjectPool)TL_matrix4f_pool.get()).alloc());
      var5.setTranslation(0.0F, 0.0F, 0.0F);
      var4.mul((Matrix4fc)var5, var4);
      ((BaseVehicle.Matrix4fObjectPool)TL_matrix4f_pool.get()).release(var5);
      ((BaseVehicle.Matrix4fObjectPool)TL_matrix4f_pool.get()).release(var4);
      var4.getColumn(2, (Vector4f)this.tempVector4f);
      var2.set(this.tempVector4f.x, 0.0F, this.tempVector4f.z);
   }

   public void tryStartEngine(boolean var1) {
      if (this.getDriver() == null || !(this.getDriver() instanceof IsoPlayer) || !((IsoPlayer)this.getDriver()).isBlockMovement()) {
         if (this.engineState == BaseVehicle.engineStateTypes.Idle) {
            if ((!Core.bDebug || !DebugOptions.instance.CheatVehicleStartWithoutKey.getValue()) && !SandboxOptions.instance.VehicleEasyUse.getValue() && !this.isKeysInIgnition() && !var1 && this.getDriver().getInventory().haveThisKeyId(this.getKeyId()) == null && !this.isHotwired()) {
               if (GameServer.bServer) {
                  this.getDriver().sendObjectChange("vehicleNoKey");
               } else {
                  this.getDriver().SayDebug(" [img=media/ui/CarKey_none.png]");
               }
            } else {
               this.engineDoStarting();
            }

         }
      }
   }

   public void tryStartEngine() {
      this.tryStartEngine(false);
   }

   public void engineDoIdle() {
      this.engineState = BaseVehicle.engineStateTypes.Idle;
      this.engineLastUpdateStateTime = System.currentTimeMillis();
      this.transmitEngine();
   }

   public void engineDoStarting() {
      this.engineState = BaseVehicle.engineStateTypes.Starting;
      this.engineLastUpdateStateTime = System.currentTimeMillis();
      this.transmitEngine();
      this.setKeysInIgnition(true);
   }

   public boolean isStarting() {
      return this.engineState == BaseVehicle.engineStateTypes.Starting || this.engineState == BaseVehicle.engineStateTypes.StartingFailed || this.engineState == BaseVehicle.engineStateTypes.StartingSuccess || this.engineState == BaseVehicle.engineStateTypes.StartingFailedNoPower;
   }

   private String getEngineSound() {
      return this.getScript() != null && this.getScript().getSounds().engine != null ? this.getScript().getSounds().engine : "VehicleEngineDefault";
   }

   private String getEngineStartSound() {
      return this.getScript() != null && this.getScript().getSounds().engineStart != null ? this.getScript().getSounds().engineStart : "VehicleStarted";
   }

   private String getEngineTurnOffSound() {
      return this.getScript() != null && this.getScript().getSounds().engineTurnOff != null ? this.getScript().getSounds().engineTurnOff : "VehicleTurnedOff";
   }

   private String getIgnitionFailSound() {
      return this.getScript() != null && this.getScript().getSounds().ignitionFail != null ? this.getScript().getSounds().ignitionFail : "VehicleFailingToStart";
   }

   private String getIgnitionFailNoPowerSound() {
      return this.getScript() != null && this.getScript().getSounds().ignitionFailNoPower != null ? this.getScript().getSounds().ignitionFailNoPower : "VehicleFailingToStartNoPower";
   }

   public void engineDoRetryingStarting() {
      this.getEmitter().stopSoundByName(this.getIgnitionFailSound());
      this.getEmitter().playSoundImpl(this.getIgnitionFailSound(), (IsoObject)null);
      this.engineState = BaseVehicle.engineStateTypes.RetryingStarting;
      this.engineLastUpdateStateTime = System.currentTimeMillis();
      this.transmitEngine();
   }

   public void engineDoStartingSuccess() {
      this.getEmitter().stopSoundByName(this.getIgnitionFailSound());
      this.engineState = BaseVehicle.engineStateTypes.StartingSuccess;
      this.engineLastUpdateStateTime = System.currentTimeMillis();
      if (this.getEngineStartSound().equals(this.getEngineSound())) {
         if (!this.getEmitter().isPlaying(this.combinedEngineSound)) {
            this.combinedEngineSound = this.emitter.playSoundImpl(this.getEngineSound(), (IsoObject)null);
         }
      } else {
         this.getEmitter().playSoundImpl(this.getEngineStartSound(), (IsoObject)null);
      }

      this.transmitEngine();
      this.setKeysInIgnition(true);
   }

   public void engineDoStartingFailed() {
      this.getEmitter().stopSoundByName(this.getIgnitionFailSound());
      this.getEmitter().playSoundImpl(this.getIgnitionFailSound(), (IsoObject)null);
      this.stopEngineSounds();
      this.engineState = BaseVehicle.engineStateTypes.StartingFailed;
      this.engineLastUpdateStateTime = System.currentTimeMillis();
      this.transmitEngine();
   }

   public void engineDoStartingFailedNoPower() {
      this.getEmitter().stopSoundByName(this.getIgnitionFailNoPowerSound());
      this.getEmitter().playSoundImpl(this.getIgnitionFailNoPowerSound(), (IsoObject)null);
      this.stopEngineSounds();
      this.engineState = BaseVehicle.engineStateTypes.StartingFailedNoPower;
      this.engineLastUpdateStateTime = System.currentTimeMillis();
      this.transmitEngine();
   }

   public void engineDoRunning() {
      this.setNeedPartsUpdate(true);
      this.engineState = BaseVehicle.engineStateTypes.Running;
      this.engineLastUpdateStateTime = System.currentTimeMillis();
      this.transmitEngine();
   }

   public void engineDoStalling() {
      this.getEmitter().playSoundImpl("VehicleRunningOutOfGas", (IsoObject)null);
      this.engineState = BaseVehicle.engineStateTypes.Stalling;
      this.engineLastUpdateStateTime = System.currentTimeMillis();
      this.stopEngineSounds();
      this.engineSoundIndex = 0;
      this.transmitEngine();
      if (!Core.getInstance().getOptionLeaveKeyInIgnition()) {
         this.setKeysInIgnition(false);
      }

   }

   public void engineDoShuttingDown() {
      if (!this.getEngineTurnOffSound().equals(this.getEngineSound())) {
         this.getEmitter().playSoundImpl(this.getEngineTurnOffSound(), (IsoObject)null);
      }

      this.stopEngineSounds();
      this.engineSoundIndex = 0;
      this.engineState = BaseVehicle.engineStateTypes.ShutingDown;
      this.engineLastUpdateStateTime = System.currentTimeMillis();
      this.transmitEngine();
      if (!Core.getInstance().getOptionLeaveKeyInIgnition()) {
         this.setKeysInIgnition(false);
      }

      VehiclePart var1 = this.getHeater();
      if (var1 != null) {
         var1.getModData().rawset("active", false);
      }

   }

   public void shutOff() {
      if (this.getPartById("GasTank").getContainerContentAmount() == 0.0F) {
         this.engineDoStalling();
      } else {
         this.engineDoShuttingDown();
      }
   }

   public void resumeRunningAfterLoad() {
      if (GameClient.bClient) {
         IsoGameCharacter var1 = this.getDriver();
         if (var1 != null) {
            Boolean var2 = this.getDriver().getInventory().haveThisKeyId(this.getKeyId()) != null ? Boolean.TRUE : Boolean.FALSE;
            GameClient.instance.sendClientCommandV((IsoPlayer)this.getDriver(), "vehicle", "startEngine", "haveKey", var2);
         }
      } else if (this.isEngineWorking()) {
         this.getEmitter();
         this.engineDoStartingSuccess();
      }
   }

   public boolean isEngineStarted() {
      return this.engineState == BaseVehicle.engineStateTypes.Starting || this.engineState == BaseVehicle.engineStateTypes.StartingFailed || this.engineState == BaseVehicle.engineStateTypes.StartingSuccess || this.engineState == BaseVehicle.engineStateTypes.RetryingStarting;
   }

   public boolean isEngineRunning() {
      return this.engineState == BaseVehicle.engineStateTypes.Running;
   }

   public boolean isEngineWorking() {
      for(int var1 = 0; var1 < this.parts.size(); ++var1) {
         VehiclePart var2 = (VehiclePart)this.parts.get(var1);
         String var3 = var2.getLuaFunction("checkEngine");
         if (var3 != null && !Boolean.TRUE.equals(this.callLuaBoolean(var3, this, var2))) {
            return false;
         }
      }

      return true;
   }

   public boolean isOperational() {
      for(int var1 = 0; var1 < this.parts.size(); ++var1) {
         VehiclePart var2 = (VehiclePart)this.parts.get(var1);
         String var3 = var2.getLuaFunction("checkOperate");
         if (var3 != null && !Boolean.TRUE.equals(this.callLuaBoolean(var3, this, var2))) {
            return false;
         }
      }

      return true;
   }

   public boolean isDriveable() {
      if (!this.isEngineWorking()) {
         return false;
      } else {
         return this.isOperational();
      }
   }

   public BaseSoundEmitter getEmitter() {
      if (this.emitter == null) {
         if (!Core.SoundDisabled && !GameServer.bServer) {
            FMODSoundEmitter var1 = new FMODSoundEmitter();
            var1.parameterUpdater = this;
            this.emitter = var1;
         } else {
            this.emitter = new DummySoundEmitter();
         }
      }

      return this.emitter;
   }

   public long playSoundImpl(String var1, IsoObject var2) {
      return this.getEmitter().playSoundImpl(var1, var2);
   }

   public int stopSound(long var1) {
      return this.getEmitter().stopSound(var1);
   }

   public void playSound(String var1) {
      this.getEmitter().playSound(var1);
   }

   public void updateSounds() {
      if (!GameServer.bServer) {
         if (this.getBatteryCharge() > 0.0F) {
            if (this.lightbarSirenMode.isEnable() && this.soundSirenSignal == -1L) {
               this.setLightbarSirenMode(this.lightbarSirenMode.get());
            }
         } else if (this.soundSirenSignal != -1L) {
            this.getEmitter().stopSound(this.soundSirenSignal);
            this.soundSirenSignal = -1L;
         }
      }

      IsoPlayer var1 = null;
      float var2 = Float.MAX_VALUE;

      for(int var3 = 0; var3 < IsoPlayer.numPlayers; ++var3) {
         IsoPlayer var4 = IsoPlayer.players[var3];
         if (var4 != null && var4.getCurrentSquare() != null) {
            float var5 = var4.getX();
            float var6 = var4.getY();
            float var7 = IsoUtils.DistanceToSquared(var5, var6, this.x, this.y);
            if (var4.Traits.HardOfHearing.isSet()) {
               var7 *= 4.5F;
            }

            if (var4.Traits.Deaf.isSet()) {
               var7 = Float.MAX_VALUE;
            }

            if (var7 < var2) {
               var1 = var4;
               var2 = var7;
            }
         }
      }

      if (var1 == null) {
         if (this.emitter != null) {
            this.emitter.setPos(this.x, this.y, this.z);
            if (!this.emitter.isEmpty()) {
               this.emitter.tick();
            }
         }

      } else {
         if (!GameServer.bServer) {
            if (!this.getEmitter().isPlaying("VehicleAmbiance")) {
               this.emitter.playAmbientLoopedImpl("VehicleAmbiance");
            }

            float var8 = var2;
            if (var2 > 1200.0F) {
               this.stopEngineSounds();
               if (this.emitter != null && !this.emitter.isEmpty()) {
                  this.emitter.setPos(this.x, this.y, this.z);
                  this.emitter.tick();
               }

               return;
            }

            for(int var10 = 0; var10 < this.new_EngineSoundId.length; ++var10) {
               if (this.new_EngineSoundId[var10] != 0L) {
                  this.getEmitter().setVolume(this.new_EngineSoundId[var10], 1.0F - var8 / 1200.0F);
               }
            }
         }

         this.startTime -= GameTime.instance.getMultiplier();
         if (this.getController() != null) {
            if (!GameServer.bServer) {
               if (this.emitter == null) {
                  if (this.engineState != BaseVehicle.engineStateTypes.Running) {
                     return;
                  }

                  this.getEmitter();
               }

               boolean var9 = this.isAnyListenerInside();
               float var11 = Math.abs(this.getCurrentSpeedKmHour());
               if (this.startTime <= 0.0F && this.engineState == BaseVehicle.engineStateTypes.Running && !this.getEmitter().isPlaying(this.combinedEngineSound)) {
                  this.combinedEngineSound = this.emitter.playSoundImpl(this.getEngineSound(), (IsoObject)null);
                  if (this.getEngineSound().equals(this.getEngineStartSound())) {
                     this.emitter.setTimelinePosition(this.combinedEngineSound, "idle");
                  }
               }

               boolean var12 = false;
               if (!GameClient.bClient || this.isLocalPhysicSim()) {
                  for(int var13 = 0; var13 < this.script.getWheelCount(); ++var13) {
                     if (this.wheelInfo[var13].skidInfo < 0.15F) {
                        var12 = true;
                        break;
                     }
                  }
               }

               if (this.getDriver() == null) {
                  var12 = false;
               }

               if (var12 != this.skidding) {
                  if (var12) {
                     this.skidSound = this.getEmitter().playSoundImpl("VehicleSkid", (IsoObject)null);
                  } else if (this.skidSound != 0L) {
                     this.emitter.stopSound(this.skidSound);
                     this.skidSound = 0L;
                  }

                  this.skidding = var12;
               }

               if (this.soundBackMoveSignal != -1L && this.emitter != null) {
                  this.emitter.set3D(this.soundBackMoveSignal, !var9);
               }

               if (this.soundHorn != -1L && this.emitter != null) {
                  this.emitter.set3D(this.soundHorn, !var9);
               }

               if (this.soundSirenSignal != -1L && this.emitter != null) {
                  this.emitter.set3D(this.soundSirenSignal, !var9);
               }

               if (this.emitter != null && (this.engineState != BaseVehicle.engineStateTypes.Idle || !this.emitter.isEmpty())) {
                  this.getFMODParameters().update();
                  this.emitter.setPos(this.x, this.y, this.z);
                  this.emitter.tick();
               }

            }
         }
      }
   }

   private boolean updatePart(VehiclePart var1) {
      var1.updateSignalDevice();
      VehicleLight var2 = var1.getLight();
      if (var2 != null && var1.getId().contains("Headlight")) {
         var1.setLightActive(this.getHeadlightsOn() && var1.getInventoryItem() != null && this.getBatteryCharge() > 0.0F);
      }

      String var3 = var1.getLuaFunction("update");
      if (var3 == null) {
         return false;
      } else {
         float var4 = (float)GameTime.getInstance().getWorldAgeHours();
         if (var1.getLastUpdated() < 0.0F) {
            var1.setLastUpdated(var4);
         } else if (var1.getLastUpdated() > var4) {
            var1.setLastUpdated(var4);
         }

         float var5 = var4 - var1.getLastUpdated();
         if ((int)(var5 * 60.0F) > 0) {
            var1.setLastUpdated(var4);
            this.callLuaVoid(var3, this, var1, (double)(var5 * 60.0F));
            return true;
         } else {
            return false;
         }
      }
   }

   public void updateParts() {
      if (!GameClient.bClient) {
         boolean var4 = false;

         for(int var5 = 0; var5 < this.getPartCount(); ++var5) {
            VehiclePart var3 = this.getPartByIndex(var5);
            if (this.updatePart(var3) && !var4) {
               var4 = true;
            }

            if (var5 == this.getPartCount() - 1 && var4) {
               this.brakeBetweenUpdatesSpeed = 0.0F;
            }
         }

      } else {
         for(int var1 = 0; var1 < this.getPartCount(); ++var1) {
            VehiclePart var2 = this.getPartByIndex(var1);
            var2.updateSignalDevice();
         }

      }
   }

   public void drainBatteryUpdateHack() {
      boolean var1 = this.isEngineRunning();
      if (!var1) {
         for(int var2 = 0; var2 < this.parts.size(); ++var2) {
            VehiclePart var3 = (VehiclePart)this.parts.get(var2);
            if (var3.getDeviceData() != null && var3.getDeviceData().getIsTurnedOn()) {
               this.updatePart(var3);
            } else if (var3.getLight() != null && var3.getLight().getActive()) {
               this.updatePart(var3);
            }
         }

         if (this.hasLightbar() && (this.lightbarLightsMode.isEnable() || this.lightbarSirenMode.isEnable()) && this.getBattery() != null) {
            this.updatePart(this.getBattery());
         }

      }
   }

   public boolean getHeadlightsOn() {
      return this.headlightsOn;
   }

   public void setHeadlightsOn(boolean var1) {
      if (this.headlightsOn != var1) {
         this.headlightsOn = var1;
         if (GameServer.bServer) {
            this.updateFlags = (short)(this.updateFlags | 8);
         }

      }
   }

   public boolean getWindowLightsOn() {
      return this.windowLightsOn;
   }

   public void setWindowLightsOn(boolean var1) {
      this.windowLightsOn = var1;
   }

   public boolean getHeadlightCanEmmitLight() {
      if (this.getBatteryCharge() <= 0.0F) {
         return false;
      } else {
         VehiclePart var1 = this.getPartById("HeadlightLeft");
         if (var1 != null && var1.getInventoryItem() != null) {
            return true;
         } else {
            var1 = this.getPartById("HeadlightRight");
            return var1 != null && var1.getInventoryItem() != null;
         }
      }
   }

   public boolean getStoplightsOn() {
      return this.stoplightsOn;
   }

   public void setStoplightsOn(boolean var1) {
      if (this.stoplightsOn != var1) {
         this.stoplightsOn = var1;
         if (GameServer.bServer) {
            this.updateFlags = (short)(this.updateFlags | 8);
         }

      }
   }

   public boolean hasHeadlights() {
      return this.getLightCount() > 0;
   }

   public void addToWorld() {
      if (this.addedToWorld) {
         DebugLog.General.error("added vehicle twice " + this + " id=" + this.VehicleID);
      } else {
         VehiclesDB2.instance.setVehicleLoaded(this);
         this.addedToWorld = true;
         this.removedFromWorld = false;
         super.addToWorld();
         this.createPhysics();

         for(int var1 = 0; var1 < this.parts.size(); ++var1) {
            VehiclePart var2 = (VehiclePart)this.parts.get(var1);
            if (var2.getItemContainer() != null) {
               var2.getItemContainer().addItemsToProcessItems();
            }

            if (var2.getDeviceData() != null && !GameServer.bServer) {
               ZomboidRadio.getInstance().RegisterDevice(var2);
            }
         }

         if (this.lightbarSirenMode.isEnable()) {
            this.setLightbarSirenMode(this.lightbarSirenMode.get());
            if (this.sirenStartTime <= 0.0D) {
               this.sirenStartTime = GameTime.instance.getWorldAgeHours();
            }
         }

         if (this.chunk != null && this.chunk.jobType != IsoChunk.JobType.SoftReset) {
            PolygonalMap2.instance.addVehicleToWorld(this);
         }

         if (this.engineState != BaseVehicle.engineStateTypes.Idle) {
            this.engineSpeed = this.getScript() == null ? 1000.0D : (double)this.getScript().getEngineIdleSpeed();
         }

         if (this.chunk != null && this.chunk.jobType != IsoChunk.JobType.SoftReset) {
            this.trySpawnKey();
         }

         if (this.emitter != null) {
            SoundManager.instance.registerEmitter(this.emitter);
         }

      }
   }

   public void removeFromWorld() {
      this.breakConstraint(false, false);
      VehiclesDB2.instance.setVehicleUnloaded(this);

      int var1;
      for(var1 = 0; var1 < this.passengers.length; ++var1) {
         if (this.getPassenger(var1).character != null) {
            for(int var2 = 0; var2 < 4; ++var2) {
               if (this.getPassenger(var1).character == IsoPlayer.players[var2]) {
                  return;
               }
            }
         }
      }

      IsoChunk.removeFromCheckedVehicles(this);
      if (this.trace) {
         DebugLog.log("BaseVehicle.removeFromWorld() " + this + " id=" + this.VehicleID);
      }

      if (!this.removedFromWorld) {
         if (!this.addedToWorld) {
            DebugLog.log("ERROR: removing vehicle but addedToWorld=false " + this + " id=" + this.VehicleID);
         }

         this.removedFromWorld = true;
         this.addedToWorld = false;

         for(var1 = 0; var1 < this.parts.size(); ++var1) {
            VehiclePart var3 = (VehiclePart)this.parts.get(var1);
            if (var3.getItemContainer() != null) {
               var3.getItemContainer().removeItemsFromProcessItems();
            }

            if (var3.getDeviceData() != null && !GameServer.bServer) {
               ZomboidRadio.getInstance().UnRegisterDevice(var3);
            }
         }

         if (this.emitter != null) {
            this.emitter.stopAll();
            SoundManager.instance.unregisterEmitter(this.emitter);
            this.emitter = null;
         }

         if (this.hornemitter != null && this.soundHorn != -1L) {
            this.hornemitter.stopAll();
            this.soundHorn = -1L;
         }

         if (this.createdModel) {
            ModelManager.instance.Remove(this);
            this.createdModel = false;
         }

         this.releaseAnimationPlayers();
         if (this.getController() != null) {
            Bullet.removeVehicle(this.VehicleID);
            this.physics = null;
         }

         if (!GameServer.bServer && !GameClient.bClient) {
            if (this.VehicleID != -1) {
               VehicleIDMap.instance.remove(this.VehicleID);
            }
         } else {
            VehicleManager.instance.removeFromWorld(this);
         }

         IsoWorld.instance.CurrentCell.addVehicles.remove(this);
         IsoWorld.instance.CurrentCell.vehicles.remove(this);
         PolygonalMap2.instance.removeVehicleFromWorld(this);
         if (GameClient.bClient) {
            this.chunk.vehicles.remove(this);
         }

         this.m_surroundVehicle.reset();
         this.removeWorldLights();
         super.removeFromWorld();
      }
   }

   public void permanentlyRemove() {
      for(int var1 = 0; var1 < this.getMaxPassengers(); ++var1) {
         IsoGameCharacter var2 = this.getCharacter(var1);
         if (var2 != null) {
            if (GameServer.bServer) {
               var2.sendObjectChange("exitVehicle");
            }

            this.exit(var2);
         }
      }

      this.breakConstraint(true, false);
      this.removeFromWorld();
      this.removeFromSquare();
      if (this.chunk != null) {
         this.chunk.vehicles.remove(this);
      }

      VehiclesDB2.instance.removeVehicle(this);
   }

   public VehiclePart getBattery() {
      return this.battery;
   }

   public void setEngineFeature(int var1, int var2, int var3) {
      this.engineQuality = PZMath.clamp(var1, 0, 100);
      this.engineLoudness = (int)((float)var2 / 2.7F);
      this.enginePower = var3;
   }

   public int getEngineQuality() {
      return this.engineQuality;
   }

   public int getEngineLoudness() {
      return this.engineLoudness;
   }

   public int getEnginePower() {
      return this.enginePower;
   }

   public float getBatteryCharge() {
      VehiclePart var1 = this.getBattery();
      return var1 != null && var1.getInventoryItem() instanceof DrainableComboItem ? ((DrainableComboItem)var1.getInventoryItem()).getUsedDelta() : 0.0F;
   }

   public int getPartCount() {
      return this.parts.size();
   }

   public VehiclePart getPartByIndex(int var1) {
      return var1 >= 0 && var1 < this.parts.size() ? (VehiclePart)this.parts.get(var1) : null;
   }

   public VehiclePart getPartById(String var1) {
      if (var1 == null) {
         return null;
      } else {
         for(int var2 = 0; var2 < this.parts.size(); ++var2) {
            VehiclePart var3 = (VehiclePart)this.parts.get(var2);
            VehicleScript.Part var4 = var3.getScriptPart();
            if (var4 != null && var1.equals(var4.id)) {
               return var3;
            }
         }

         return null;
      }
   }

   public int getNumberOfPartsWithContainers() {
      if (this.getScript() == null) {
         return 0;
      } else {
         int var1 = 0;

         for(int var2 = 0; var2 < this.getScript().getPartCount(); ++var2) {
            if (this.getScript().getPart(var2).container != null) {
               ++var1;
            }
         }

         return var1;
      }
   }

   public VehiclePart getPartForSeatContainer(int var1) {
      if (this.getScript() != null && var1 >= 0 && var1 < this.getMaxPassengers()) {
         for(int var2 = 0; var2 < this.getPartCount(); ++var2) {
            VehiclePart var3 = this.getPartByIndex(var2);
            if (var3.getContainerSeatNumber() == var1) {
               return var3;
            }
         }

         return null;
      } else {
         return null;
      }
   }

   public void transmitPartCondition(VehiclePart var1) {
      if (GameServer.bServer) {
         if (this.parts.contains(var1)) {
            var1.updateFlags = (short)(var1.updateFlags | 2048);
            this.updateFlags = (short)(this.updateFlags | 2048);
         }
      }
   }

   public void transmitPartItem(VehiclePart var1) {
      if (GameServer.bServer) {
         if (this.parts.contains(var1)) {
            var1.updateFlags = (short)(var1.updateFlags | 128);
            this.updateFlags = (short)(this.updateFlags | 128);
         }
      }
   }

   public void transmitPartModData(VehiclePart var1) {
      if (GameServer.bServer) {
         if (this.parts.contains(var1)) {
            var1.updateFlags = (short)(var1.updateFlags | 16);
            this.updateFlags = (short)(this.updateFlags | 16);
         }
      }
   }

   public void transmitPartUsedDelta(VehiclePart var1) {
      if (GameServer.bServer) {
         if (this.parts.contains(var1)) {
            if (var1.getInventoryItem() instanceof DrainableComboItem) {
               var1.updateFlags = (short)(var1.updateFlags | 32);
               this.updateFlags = (short)(this.updateFlags | 32);
            }
         }
      }
   }

   public void transmitPartDoor(VehiclePart var1) {
      if (GameServer.bServer) {
         if (this.parts.contains(var1)) {
            if (var1.getDoor() != null) {
               var1.updateFlags = (short)(var1.updateFlags | 512);
               this.updateFlags = (short)(this.updateFlags | 512);
            }
         }
      }
   }

   public void transmitPartWindow(VehiclePart var1) {
      if (GameServer.bServer) {
         if (this.parts.contains(var1)) {
            if (var1.getWindow() != null) {
               var1.updateFlags = (short)(var1.updateFlags | 256);
               this.updateFlags = (short)(this.updateFlags | 256);
            }
         }
      }
   }

   public int getLightCount() {
      return this.lights.size();
   }

   public VehiclePart getLightByIndex(int var1) {
      return var1 >= 0 && var1 < this.lights.size() ? (VehiclePart)this.lights.get(var1) : null;
   }

   public String getZone() {
      return this.respawnZone;
   }

   public void setZone(String var1) {
      this.respawnZone = var1;
   }

   public boolean isInArea(String var1, IsoGameCharacter var2) {
      if (var1 != null && this.getScript() != null) {
         VehicleScript.Area var3 = this.getScript().getAreaById(var1);
         if (var3 == null) {
            return false;
         } else {
            Vector2 var4 = (Vector2)((BaseVehicle.Vector2ObjectPool)TL_vector2_pool.get()).alloc();
            Vector2 var5 = this.areaPositionLocal(var3, var4);
            if (var5 == null) {
               ((BaseVehicle.Vector2ObjectPool)TL_vector2_pool.get()).release(var4);
               return false;
            } else {
               Vector3f var6 = (Vector3f)((BaseVehicle.Vector3fObjectPool)TL_vector3f_pool.get()).alloc();
               this.getLocalPos(var2.x, var2.y, this.z, var6);
               float var7 = var5.x - var3.w / 2.0F;
               float var8 = var5.y - var3.h / 2.0F;
               float var9 = var5.x + var3.w / 2.0F;
               float var10 = var5.y + var3.h / 2.0F;
               ((BaseVehicle.Vector2ObjectPool)TL_vector2_pool.get()).release(var4);
               boolean var11 = var6.x >= var7 && var6.x < var9 && var6.z >= var8 && var6.z < var10;
               ((BaseVehicle.Vector3fObjectPool)TL_vector3f_pool.get()).release(var6);
               return var11;
            }
         }
      } else {
         return false;
      }
   }

   public float getAreaDist(String var1, IsoGameCharacter var2) {
      if (var1 != null && this.getScript() != null) {
         VehicleScript.Area var3 = this.getScript().getAreaById(var1);
         if (var3 != null) {
            Vector3f var4 = this.getLocalPos(var2.x, var2.y, this.z, (Vector3f)((BaseVehicle.Vector3fObjectPool)TL_vector3f_pool.get()).alloc());
            float var5 = Math.abs(var3.x - var3.w / 2.0F);
            float var6 = Math.abs(var3.y - var3.h / 2.0F);
            float var7 = Math.abs(var3.x + var3.w / 2.0F);
            float var8 = Math.abs(var3.y + var3.h / 2.0F);
            float var9 = Math.abs(var4.x + var5) + Math.abs(var4.z + var6);
            ((BaseVehicle.Vector3fObjectPool)TL_vector3f_pool.get()).release(var4);
            return var9;
         } else {
            return 999.0F;
         }
      } else {
         return 999.0F;
      }
   }

   public Vector2 getAreaCenter(String var1) {
      return this.getAreaCenter(var1, new Vector2());
   }

   public Vector2 getAreaCenter(String var1, Vector2 var2) {
      if (var1 != null && this.getScript() != null) {
         VehicleScript.Area var3 = this.getScript().getAreaById(var1);
         return var3 == null ? null : this.areaPositionWorld(var3, var2);
      } else {
         return null;
      }
   }

   public boolean isInBounds(float var1, float var2) {
      return this.getPoly().containsPoint(var1, var2);
   }

   public boolean canAccessContainer(int var1, IsoGameCharacter var2) {
      VehiclePart var3 = this.getPartByIndex(var1);
      if (var3 == null) {
         return false;
      } else {
         VehicleScript.Part var4 = var3.getScriptPart();
         if (var4 == null) {
            return false;
         } else if (var4.container == null) {
            return false;
         } else if (var3.getItemType() != null && var3.getInventoryItem() == null && var4.container.capacity == 0) {
            return false;
         } else {
            return var4.container.luaTest != null && !var4.container.luaTest.isEmpty() ? Boolean.TRUE.equals(this.callLuaBoolean(var4.container.luaTest, this, var3, var2)) : true;
         }
      }
   }

   public boolean canInstallPart(IsoGameCharacter var1, VehiclePart var2) {
      if (!this.parts.contains(var2)) {
         return false;
      } else {
         KahluaTable var3 = var2.getTable("install");
         return var3 != null && var3.rawget("test") instanceof String ? Boolean.TRUE.equals(this.callLuaBoolean((String)var3.rawget("test"), this, var2, var1)) : false;
      }
   }

   public boolean canUninstallPart(IsoGameCharacter var1, VehiclePart var2) {
      if (!this.parts.contains(var2)) {
         return false;
      } else {
         KahluaTable var3 = var2.getTable("uninstall");
         return var3 != null && var3.rawget("test") instanceof String ? Boolean.TRUE.equals(this.callLuaBoolean((String)var3.rawget("test"), this, var2, var1)) : false;
      }
   }

   private void callLuaVoid(String var1, Object var2, Object var3) {
      Object var4 = LuaManager.getFunctionObject(var1);
      if (var4 != null) {
         LuaManager.caller.protectedCallVoid(LuaManager.thread, var4, var2, var3);
      }
   }

   private void callLuaVoid(String var1, Object var2, Object var3, Object var4) {
      Object var5 = LuaManager.getFunctionObject(var1);
      if (var5 != null) {
         LuaManager.caller.protectedCallVoid(LuaManager.thread, var5, var2, var3, var4);
      }
   }

   private Boolean callLuaBoolean(String var1, Object var2, Object var3) {
      Object var4 = LuaManager.getFunctionObject(var1);
      return var4 == null ? null : LuaManager.caller.protectedCallBoolean(LuaManager.thread, var4, var2, var3);
   }

   private Boolean callLuaBoolean(String var1, Object var2, Object var3, Object var4) {
      Object var5 = LuaManager.getFunctionObject(var1);
      return var5 == null ? null : LuaManager.caller.protectedCallBoolean(LuaManager.thread, var5, var2, var3, var4);
   }

   public short getId() {
      return this.VehicleID;
   }

   public void setTireInflation(int var1, float var2) {
   }

   public void setTireRemoved(int var1, boolean var2) {
      Bullet.setTireRemoved(this.VehicleID, var1, var2);
   }

   public Vector3f chooseBestAttackPosition(IsoGameCharacter var1, IsoGameCharacter var2, Vector3f var3) {
      Vector2f var4 = (Vector2f)((BaseVehicle.Vector2fObjectPool)TL_vector2f_pool.get()).alloc();
      Vector2f var5 = var1.getVehicle().getSurroundVehicle().getPositionForZombie((IsoZombie)var2, var4);
      float var6 = var4.x;
      float var7 = var4.y;
      ((BaseVehicle.Vector2fObjectPool)TL_vector2f_pool.get()).release(var4);
      return var5 != null ? var3.set(var6, var7, this.z) : null;
   }

   public BaseVehicle.MinMaxPosition getMinMaxPosition() {
      BaseVehicle.MinMaxPosition var1 = new BaseVehicle.MinMaxPosition();
      float var2 = this.getX();
      float var3 = this.getY();
      Vector3f var4 = this.getScript().getExtents();
      float var5 = var4.x;
      float var6 = var4.z;
      IsoDirections var7 = this.getDir();
      switch(var7) {
      case E:
      case W:
         var1.minX = var2 - var5 / 2.0F;
         var1.maxX = var2 + var5 / 2.0F;
         var1.minY = var3 - var6 / 2.0F;
         var1.maxY = var3 + var6 / 2.0F;
         break;
      case N:
      case S:
         var1.minX = var2 - var6 / 2.0F;
         var1.maxX = var2 + var6 / 2.0F;
         var1.minY = var3 - var5 / 2.0F;
         var1.maxY = var3 + var5 / 2.0F;
         break;
      default:
         return null;
      }

      return var1;
   }

   public String getVehicleType() {
      return this.type;
   }

   public void setVehicleType(String var1) {
      this.type = var1;
   }

   public float getMaxSpeed() {
      return this.maxSpeed;
   }

   public void setMaxSpeed(float var1) {
      this.maxSpeed = var1;
   }

   public void lockServerUpdate(long var1) {
      this.updateLockTimeout = System.currentTimeMillis() + var1;
   }

   public void changeTransmission(TransmissionNumber var1) {
      this.transmissionNumber = var1;
   }

   public void tryHotwire(int var1) {
      int var2 = Math.max(100 - this.getEngineQuality(), 5);
      var2 = Math.min(var2, 50);
      int var3 = var1 * 4;
      int var4 = var2 + var3;
      boolean var5 = false;
      String var6 = null;
      if (Rand.Next(100) <= var4) {
         this.setHotwired(true);
         var5 = true;
         var6 = "VehicleHotwireSuccess";
      } else if (Rand.Next(100) <= 10 - var1) {
         this.setHotwiredBroken(true);
         var5 = true;
         var6 = "VehicleHotwireFail";
      } else {
         var6 = "VehicleHotwireFail";
      }

      if (var6 != null) {
         if (GameServer.bServer) {
            LuaManager.GlobalObject.playServerSound(var6, this.square);
         } else if (this.getDriver() != null) {
            this.getDriver().getEmitter().playSound(var6);
         }
      }

      if (var5 && GameServer.bServer) {
         this.updateFlags = (short)(this.updateFlags | 4096);
      }

   }

   public void cheatHotwire(boolean var1, boolean var2) {
      if (var1 != this.hotwired || var2 != this.hotwiredBroken) {
         this.hotwired = var1;
         this.hotwiredBroken = var2;
         if (GameServer.bServer) {
            this.updateFlags = (short)(this.updateFlags | 4096);
         }
      }

   }

   public boolean isKeyIsOnDoor() {
      return this.keyIsOnDoor;
   }

   public void setKeyIsOnDoor(boolean var1) {
      this.keyIsOnDoor = var1;
   }

   public boolean isHotwired() {
      return this.hotwired;
   }

   public void setHotwired(boolean var1) {
      this.hotwired = var1;
   }

   public boolean isHotwiredBroken() {
      return this.hotwiredBroken;
   }

   public void setHotwiredBroken(boolean var1) {
      this.hotwiredBroken = var1;
   }

   public IsoGameCharacter getDriver() {
      BaseVehicle.Passenger var1 = this.getPassenger(0);
      return var1 == null ? null : var1.character;
   }

   public boolean isKeysInIgnition() {
      return this.keysInIgnition;
   }

   public void setKeysInIgnition(boolean var1) {
      IsoGameCharacter var2 = this.getDriver();
      if (var2 != null) {
         this.setAlarmed(false);
         if (!GameClient.bClient || var2 instanceof IsoPlayer && ((IsoPlayer)var2).isLocalPlayer()) {
            if (!this.isHotwired()) {
               InventoryItem var3;
               if (!GameServer.bServer && var1 && !this.keysInIgnition) {
                  var3 = this.getDriver().getInventory().haveThisKeyId(this.getKeyId());
                  if (var3 != null) {
                     this.setCurrentKey(var3);
                     InventoryItem var4 = var3.getContainer().getContainingItem();
                     if (var4 instanceof InventoryContainer && "KeyRing".equals(var4.getType())) {
                        var3.getModData().rawset("keyRing", (double)var4.getID());
                     } else if (var3.hasModData()) {
                        var3.getModData().rawset("keyRing", (Object)null);
                     }

                     var3.getContainer().DoRemoveItem(var3);
                     this.keysInIgnition = var1;
                     if (GameClient.bClient) {
                        GameClient.instance.sendClientCommandV((IsoPlayer)this.getDriver(), "vehicle", "putKeyInIgnition", "key", var3);
                     }
                  }
               }

               if (!var1 && this.keysInIgnition && !GameServer.bServer) {
                  if (this.currentKey == null) {
                     this.currentKey = this.createVehicleKey();
                  }

                  var3 = this.getCurrentKey();
                  ItemContainer var7 = this.getDriver().getInventory();
                  if (var3.hasModData() && var3.getModData().rawget("keyRing") instanceof Double) {
                     Double var5 = (Double)var3.getModData().rawget("keyRing");
                     InventoryItem var6 = var7.getItemWithID(var5.intValue());
                     if (var6 instanceof InventoryContainer && "KeyRing".equals(var6.getType())) {
                        var7 = ((InventoryContainer)var6).getInventory();
                     }

                     var3.getModData().rawset("keyRing", (Object)null);
                  }

                  var7.addItem(var3);
                  this.setCurrentKey((InventoryItem)null);
                  this.keysInIgnition = var1;
                  if (GameClient.bClient) {
                     GameClient.instance.sendClientCommand((IsoPlayer)this.getDriver(), "vehicle", "removeKeyFromIgnition", (KahluaTable)null);
                  }
               }
            }

         }
      }
   }

   public void putKeyInIgnition(InventoryItem var1) {
      if (GameServer.bServer) {
         if (var1 instanceof Key) {
            if (!this.keysInIgnition) {
               this.keysInIgnition = true;
               this.keyIsOnDoor = false;
               this.currentKey = var1;
               this.updateFlags = (short)(this.updateFlags | 4096);
            }
         }
      }
   }

   public void removeKeyFromIgnition() {
      if (GameServer.bServer) {
         if (this.keysInIgnition) {
            this.keysInIgnition = false;
            this.currentKey = null;
            this.updateFlags = (short)(this.updateFlags | 4096);
         }
      }
   }

   public void putKeyOnDoor(InventoryItem var1) {
      if (GameServer.bServer) {
         if (var1 instanceof Key) {
            if (!this.keyIsOnDoor) {
               this.keyIsOnDoor = true;
               this.keysInIgnition = false;
               this.currentKey = var1;
               this.updateFlags = (short)(this.updateFlags | 4096);
            }
         }
      }
   }

   public void removeKeyFromDoor() {
      if (GameServer.bServer) {
         if (this.keyIsOnDoor) {
            this.keyIsOnDoor = false;
            this.currentKey = null;
            this.updateFlags = (short)(this.updateFlags | 4096);
         }
      }
   }

   public void syncKeyInIgnition(boolean var1, boolean var2, InventoryItem var3) {
      if (GameClient.bClient) {
         if (!(this.getDriver() instanceof IsoPlayer) || !((IsoPlayer)this.getDriver()).isLocalPlayer()) {
            this.keysInIgnition = var1;
            this.keyIsOnDoor = var2;
            this.currentKey = var3;
         }
      }
   }

   private void randomizeContainers() {
      if (!GameClient.bClient) {
         boolean var1 = true;
         String var2 = this.getScriptName().substring(this.getScriptName().indexOf(46) + 1);
         ItemPickerJava.VehicleDistribution var3 = (ItemPickerJava.VehicleDistribution)ItemPickerJava.VehicleDistributions.get(var2 + this.getSkinIndex());
         if (var3 != null) {
            var1 = false;
         } else {
            var3 = (ItemPickerJava.VehicleDistribution)ItemPickerJava.VehicleDistributions.get(var2);
         }

         if (var3 == null) {
            for(int var7 = 0; var7 < this.parts.size(); ++var7) {
               VehiclePart var8 = (VehiclePart)this.parts.get(var7);
               if (var8.getItemContainer() != null) {
                  DebugLog.log("VEHICLE MISSING CONT DISTRIBUTION: " + var2);
                  return;
               }
            }

         } else {
            ItemPickerJava.ItemPickerRoom var4;
            if (var1 && Rand.Next(100) <= 8 && !var3.Specific.isEmpty()) {
               var4 = (ItemPickerJava.ItemPickerRoom)PZArrayUtil.pickRandom((List)var3.Specific);
            } else {
               var4 = var3.Normal;
            }

            int var5;
            if (!StringUtils.isNullOrWhitespace(this.specificDistributionId)) {
               for(var5 = 0; var5 < var3.Specific.size(); ++var5) {
                  ItemPickerJava.ItemPickerRoom var6 = (ItemPickerJava.ItemPickerRoom)var3.Specific.get(var5);
                  if (this.specificDistributionId.equals(var6.specificId)) {
                     var4 = var6;
                     break;
                  }
               }
            }

            for(var5 = 0; var5 < this.parts.size(); ++var5) {
               VehiclePart var9 = (VehiclePart)this.parts.get(var5);
               if (var9.getItemContainer() != null && !var9.getItemContainer().bExplored) {
                  var9.getItemContainer().clear();
                  if (Rand.Next(100) <= 100) {
                     this.randomizeContainer(var9, var4);
                  }

                  var9.getItemContainer().setExplored(true);
               }
            }

         }
      }
   }

   private void randomizeContainer(VehiclePart var1, ItemPickerJava.ItemPickerRoom var2) {
      if (!GameClient.bClient) {
         if (var2 != null) {
            if (!var1.getId().contains("Seat") && !var2.Containers.containsKey(var1.getId())) {
               String var10000 = var1.getId();
               DebugLog.log("NO CONT DISTRIB FOR PART: " + var10000 + " CAR: " + this.getScriptName().replaceFirst("Base.", ""));
            }

            ItemPickerJava.fillContainerType(var2, var1.getItemContainer(), "", (IsoGameCharacter)null);
            if (GameServer.bServer && !var1.getItemContainer().getItems().isEmpty()) {
            }

         }
      }
   }

   public boolean hasHorn() {
      return this.script.getSounds().hornEnable;
   }

   public boolean hasLightbar() {
      VehiclePart var1 = this.getPartById("lightbar");
      return var1 != null && var1.getCondition() > 0;
   }

   public void onHornStart() {
      this.soundHornOn = true;
      if (GameServer.bServer) {
         this.updateFlags = (short)(this.updateFlags | 1024);
         if (this.script.getSounds().hornEnable) {
            WorldSoundManager.instance.addSound(this, (int)this.getX(), (int)this.getY(), (int)this.getZ(), 150, 150, false);
         }

      } else {
         if (this.soundHorn != -1L) {
            this.hornemitter.stopSound(this.soundHorn);
         }

         if (this.script.getSounds().hornEnable) {
            this.hornemitter = IsoWorld.instance.getFreeEmitter(this.getX(), this.getY(), (float)((int)this.getZ()));
            this.soundHorn = this.hornemitter.playSoundLoopedImpl(this.script.getSounds().horn);
            this.hornemitter.set3D(this.soundHorn, !this.isAnyListenerInside());
            this.hornemitter.setVolume(this.soundHorn, 1.0F);
            this.hornemitter.setPitch(this.soundHorn, 1.0F);
            if (!GameClient.bClient) {
               WorldSoundManager.instance.addSound(this, (int)this.getX(), (int)this.getY(), (int)this.getZ(), 150, 150, false);
            }
         }

      }
   }

   public void onHornStop() {
      this.soundHornOn = false;
      if (GameServer.bServer) {
         this.updateFlags = (short)(this.updateFlags | 1024);
      } else {
         if (this.script.getSounds().hornEnable && this.soundHorn != -1L) {
            this.hornemitter.stopSound(this.soundHorn);
            this.soundHorn = -1L;
         }

      }
   }

   public boolean hasBackSignal() {
      return this.script != null && this.script.getSounds().backSignalEnable;
   }

   public boolean isBackSignalEmitting() {
      return this.soundBackMoveSignal != -1L;
   }

   public void onBackMoveSignalStart() {
      this.soundBackMoveOn = true;
      if (GameServer.bServer) {
         this.updateFlags = (short)(this.updateFlags | 1024);
      } else {
         if (this.soundBackMoveSignal != -1L) {
            this.emitter.stopSound(this.soundBackMoveSignal);
         }

         if (this.script.getSounds().backSignalEnable) {
            this.soundBackMoveSignal = this.emitter.playSoundLoopedImpl(this.script.getSounds().backSignal);
            this.emitter.set3D(this.soundBackMoveSignal, !this.isAnyListenerInside());
         }

      }
   }

   public void onBackMoveSignalStop() {
      this.soundBackMoveOn = false;
      if (GameServer.bServer) {
         this.updateFlags = (short)(this.updateFlags | 1024);
      } else {
         if (this.script.getSounds().backSignalEnable && this.soundBackMoveSignal != -1L) {
            this.emitter.stopSound(this.soundBackMoveSignal);
            this.soundBackMoveSignal = -1L;
         }

      }
   }

   public int getLightbarLightsMode() {
      return this.lightbarLightsMode.get();
   }

   public void setLightbarLightsMode(int var1) {
      this.lightbarLightsMode.set(var1);
      if (GameServer.bServer) {
         this.updateFlags = (short)(this.updateFlags | 1024);
      }
   }

   public int getLightbarSirenMode() {
      return this.lightbarSirenMode.get();
   }

   public void setLightbarSirenMode(int var1) {
      if (this.soundSirenSignal != -1L) {
         this.getEmitter().stopSound(this.soundSirenSignal);
         this.soundSirenSignal = -1L;
      }

      this.lightbarSirenMode.set(var1);
      if (GameServer.bServer) {
         this.updateFlags = (short)(this.updateFlags | 1024);
      } else {
         if (this.lightbarSirenMode.isEnable() && this.getBatteryCharge() > 0.0F) {
            this.soundSirenSignal = this.getEmitter().playSoundLoopedImpl(this.lightbarSirenMode.getSoundName(this.script.getLightbar()));
            this.getEmitter().set3D(this.soundSirenSignal, !this.isAnyListenerInside());
         }

      }
   }

   public HashMap getChoosenParts() {
      return this.choosenParts;
   }

   public float getMass() {
      return this.mass;
   }

   public void setMass(float var1) {
      this.mass = var1;
   }

   public float getInitialMass() {
      return this.initialMass;
   }

   public void setInitialMass(float var1) {
      this.initialMass = var1;
   }

   public void updateTotalMass() {
      float var1 = 0.0F;

      for(int var2 = 0; var2 < this.parts.size(); ++var2) {
         VehiclePart var3 = (VehiclePart)this.parts.get(var2);
         if (var3.getItemContainer() != null) {
            var1 += var3.getItemContainer().getCapacityWeight();
         }

         if (var3.getInventoryItem() != null) {
            var1 += var3.getInventoryItem().getWeight();
         }
      }

      this.setMass((float)Math.round(this.getInitialMass() + var1));
      if (this.physics != null) {
         Bullet.setVehicleMass(this.VehicleID, this.getMass());
      }

   }

   public float getBrakingForce() {
      return this.brakingForce;
   }

   public void setBrakingForce(float var1) {
      this.brakingForce = var1;
   }

   public float getBaseQuality() {
      return this.baseQuality;
   }

   public void setBaseQuality(float var1) {
      this.baseQuality = var1;
   }

   public float getCurrentSteering() {
      return this.currentSteering;
   }

   public void setCurrentSteering(float var1) {
      this.currentSteering = var1;
   }

   public boolean isDoingOffroad() {
      if (this.getCurrentSquare() == null) {
         return false;
      } else {
         IsoObject var1 = this.getCurrentSquare().getFloor();
         if (var1 != null && var1.getSprite() != null) {
            String var2 = var1.getSprite().getName();
            if (var2 == null) {
               return false;
            } else {
               return !var2.contains("carpentry_02") && !var2.contains("blends_street") && !var2.contains("floors_exterior_street");
            }
         } else {
            return false;
         }
      }
   }

   public boolean isBraking() {
      return this.isBraking;
   }

   public void setBraking(boolean var1) {
      this.isBraking = var1;
      if (var1 && this.brakeBetweenUpdatesSpeed == 0.0F) {
         this.brakeBetweenUpdatesSpeed = Math.abs(this.getCurrentSpeedKmHour());
      }

   }

   public void updatePartStats() {
      this.setBrakingForce(0.0F);
      this.engineLoudness = (int)((double)this.getScript().getEngineLoudness() * SandboxOptions.instance.ZombieAttractionMultiplier.getValue() / 2.0D);
      boolean var1 = false;

      for(int var2 = 0; var2 < this.getPartCount(); ++var2) {
         VehiclePart var3 = this.getPartByIndex(var2);
         if (var3.getInventoryItem() != null) {
            float var4;
            if (var3.getInventoryItem().getBrakeForce() > 0.0F) {
               var4 = VehiclePart.getNumberByCondition(var3.getInventoryItem().getBrakeForce(), (float)var3.getInventoryItem().getCondition(), 5.0F);
               var4 += var4 / 50.0F * (float)var3.getMechanicSkillInstaller();
               this.setBrakingForce(this.getBrakingForce() + var4);
            }

            if (var3.getInventoryItem().getWheelFriction() > 0.0F) {
               var3.setWheelFriction(0.0F);
               var4 = VehiclePart.getNumberByCondition(var3.getInventoryItem().getWheelFriction(), (float)var3.getInventoryItem().getCondition(), 0.2F);
               var4 += 0.1F * (float)var3.getMechanicSkillInstaller();
               var4 = Math.min(2.3F, var4);
               var3.setWheelFriction(var4);
            }

            if (var3.getInventoryItem().getSuspensionCompression() > 0.0F) {
               var3.setSuspensionCompression(VehiclePart.getNumberByCondition(var3.getInventoryItem().getSuspensionCompression(), (float)var3.getInventoryItem().getCondition(), 0.6F));
               var3.setSuspensionDamping(VehiclePart.getNumberByCondition(var3.getInventoryItem().getSuspensionDamping(), (float)var3.getInventoryItem().getCondition(), 0.6F));
            }

            if (var3.getInventoryItem().getEngineLoudness() > 0.0F) {
               var3.setEngineLoudness(VehiclePart.getNumberByCondition(var3.getInventoryItem().getEngineLoudness(), (float)var3.getInventoryItem().getCondition(), 10.0F));
               this.engineLoudness = (int)((float)this.engineLoudness * (1.0F + (100.0F - var3.getEngineLoudness()) / 100.0F));
               var1 = true;
            }
         }
      }

      if (!var1) {
         this.engineLoudness *= 2;
      }

   }

   public void transmitEngine() {
      if (GameServer.bServer) {
         this.updateFlags = (short)(this.updateFlags | 4);
      }
   }

   public void setRust(float var1) {
      this.rust = PZMath.clamp(var1, 0.0F, 1.0F);
   }

   public float getRust() {
      return this.rust;
   }

   public void transmitRust() {
      if (GameServer.bServer) {
         this.updateFlags = (short)(this.updateFlags | 4096);
      }
   }

   public void updateBulletStats() {
      if (!this.getScriptName().contains("Burnt") && WorldSimulation.instance.created) {
         float[] var1 = wheelParams;
         double var4 = 2.4D;
         byte var6 = 5;
         double var2;
         float var7;
         if (this.isInForest() && this.isDoingOffroad() && Math.abs(this.getCurrentSpeedKmHour()) > 1.0F) {
            var2 = (double)Rand.Next(0.08F, 0.18F);
            var7 = 0.7F;
            var6 = 3;
         } else if (this.isDoingOffroad() && Math.abs(this.getCurrentSpeedKmHour()) > 1.0F) {
            var2 = (double)Rand.Next(0.05F, 0.15F);
            var7 = 0.7F;
         } else {
            if (Math.abs(this.getCurrentSpeedKmHour()) > 1.0F && Rand.Next(100) < 10) {
               var2 = (double)Rand.Next(0.05F, 0.15F);
            } else {
               var2 = 0.0D;
            }

            var7 = 1.0F;
         }

         if (RainManager.isRaining()) {
            var7 -= 0.3F;
         }

         Vector3f var8 = (Vector3f)((BaseVehicle.Vector3fObjectPool)TL_vector3f_pool.get()).alloc();

         for(int var9 = 0; var9 < this.script.getWheelCount(); ++var9) {
            this.updateBulletStatsWheel(var9, var1, var8, var7, var6, var4, var2);
         }

         ((BaseVehicle.Vector3fObjectPool)TL_vector3f_pool.get()).release(var8);
         if (SystemDisabler.getdoVehicleLowRider() && this.isKeyboardControlled()) {
            float var11 = 1.6F;
            float var10 = 1.0F;
            float[] var10000;
            if (GameKeyboard.isKeyDown(79)) {
               var10000 = lowRiderParam;
               var10000[0] += (var11 - lowRiderParam[0]) * var10;
            } else {
               var10000 = lowRiderParam;
               var10000[0] += (0.0F - lowRiderParam[0]) * 0.05F;
            }

            if (GameKeyboard.isKeyDown(80)) {
               var10000 = lowRiderParam;
               var10000[1] += (var11 - lowRiderParam[1]) * var10;
            } else {
               var10000 = lowRiderParam;
               var10000[1] += (0.0F - lowRiderParam[1]) * 0.05F;
            }

            if (GameKeyboard.isKeyDown(75)) {
               var10000 = lowRiderParam;
               var10000[2] += (var11 - lowRiderParam[2]) * var10;
            } else {
               var10000 = lowRiderParam;
               var10000[2] += (0.0F - lowRiderParam[2]) * 0.05F;
            }

            if (GameKeyboard.isKeyDown(76)) {
               var10000 = lowRiderParam;
               var10000[3] += (var11 - lowRiderParam[3]) * var10;
            } else {
               var10000 = lowRiderParam;
               var10000[3] += (0.0F - lowRiderParam[3]) * 0.05F;
            }

            var1[23] = lowRiderParam[0];
            var1[22] = lowRiderParam[1];
            var1[21] = lowRiderParam[2];
            var1[20] = lowRiderParam[3];
         }

         Bullet.setVehicleParams(this.VehicleID, var1);
      }
   }

   private void updateBulletStatsWheel(int var1, float[] var2, Vector3f var3, float var4, int var5, double var6, double var8) {
      int var10 = var1 * 6;
      VehicleScript.Wheel var11 = this.script.getWheel(var1);
      Vector3f var12 = this.getWorldPos(var11.offset.x, var11.offset.y, var11.offset.z, var3);
      VehiclePart var13 = this.getPartById("Tire" + var11.getId());
      VehiclePart var14 = this.getPartById("Suspension" + var11.getId());
      if (var13 != null && var13.getInventoryItem() != null) {
         var2[var10 + 0] = 1.0F;
         var2[var10 + 1] = Math.min(var13.getContainerContentAmount() / (float)(var13.getContainerCapacity() - 10), 1.0F);
         var2[var10 + 2] = var4 * var13.getWheelFriction();
         if (var14 != null && var14.getInventoryItem() != null) {
            var2[var10 + 3] = var14.getSuspensionDamping();
            var2[var10 + 4] = var14.getSuspensionCompression();
         } else {
            var2[var10 + 3] = 0.1F;
            var2[var10 + 4] = 0.1F;
         }

         if (Rand.Next(var5) == 0) {
            var2[var10 + 5] = (float)(Math.sin(var6 * (double)var12.x()) * Math.sin(var6 * (double)var12.y()) * var8);
         } else {
            var2[var10 + 5] = 0.0F;
         }
      } else {
         var2[var10 + 0] = 0.0F;
         var2[var10 + 1] = 30.0F;
         var2[var10 + 2] = 0.0F;
         var2[var10 + 3] = 2.88F;
         var2[var10 + 4] = 3.83F;
         if (Rand.Next(var5) == 0) {
            var2[var10 + 5] = (float)(Math.sin(var6 * (double)var12.x()) * Math.sin(var6 * (double)var12.y()) * var8);
         } else {
            var2[var10 + 5] = 0.0F;
         }
      }

      if (this.forcedFriction > -1.0F) {
         var2[var10 + 2] = this.forcedFriction;
      }

   }

   public void setActiveInBullet(boolean var1) {
      if (var1 || !this.isEngineRunning()) {
         ;
      }
   }

   public boolean areAllDoorsLocked() {
      for(int var1 = 0; var1 < this.getMaxPassengers(); ++var1) {
         VehiclePart var2 = this.getPassengerDoor(var1);
         if (var2 != null && var2.getDoor() != null && !var2.getDoor().isLocked()) {
            return false;
         }
      }

      return true;
   }

   public boolean isAnyDoorLocked() {
      for(int var1 = 0; var1 < this.getMaxPassengers(); ++var1) {
         VehiclePart var2 = this.getPassengerDoor(var1);
         if (var2 != null && var2.getDoor() != null && var2.getDoor().isLocked()) {
            return true;
         }
      }

      return false;
   }

   public float getRemainingFuelPercentage() {
      VehiclePart var1 = this.getPartById("GasTank");
      return var1 == null ? 0.0F : var1.getContainerContentAmount() / (float)var1.getContainerCapacity() * 100.0F;
   }

   public int getMechanicalID() {
      return this.mechanicalID;
   }

   public void setMechanicalID(int var1) {
      this.mechanicalID = var1;
   }

   public boolean needPartsUpdate() {
      return this.needPartsUpdate;
   }

   public void setNeedPartsUpdate(boolean var1) {
      this.needPartsUpdate = var1;
   }

   public VehiclePart getHeater() {
      return this.getPartById("Heater");
   }

   public int windowsOpen() {
      int var1 = 0;

      for(int var2 = 0; var2 < this.getPartCount(); ++var2) {
         VehiclePart var3 = this.getPartByIndex(var2);
         if (var3.window != null && var3.window.open) {
            ++var1;
         }
      }

      return var1;
   }

   public boolean isAlarmed() {
      return this.alarmed;
   }

   public void setAlarmed(boolean var1) {
      this.alarmed = var1;
   }

   public void triggerAlarm() {
      if (this.alarmed) {
         this.alarmed = false;
         this.alarmTime = Rand.Next(1500, 3000);
         this.alarmAccumulator = 0.0F;
      }
   }

   private void doAlarm() {
      if (this.alarmTime > 0) {
         if (this.getBatteryCharge() <= 0.0F) {
            if (this.soundHornOn) {
               this.onHornStop();
            }

            this.alarmTime = -1;
            return;
         }

         this.alarmAccumulator += GameTime.instance.getMultiplier() / 1.6F;
         if (this.alarmAccumulator >= (float)this.alarmTime) {
            this.onHornStop();
            this.setHeadlightsOn(false);
            this.alarmTime = -1;
            return;
         }

         int var1 = (int)this.alarmAccumulator / 20;
         if (!this.soundHornOn && var1 % 2 == 0) {
            this.onHornStart();
            this.setHeadlightsOn(true);
         }

         if (this.soundHornOn && var1 % 2 == 1) {
            this.onHornStop();
            this.setHeadlightsOn(false);
         }
      }

   }

   public boolean isMechanicUIOpen() {
      return this.mechanicUIOpen;
   }

   public void setMechanicUIOpen(boolean var1) {
      this.mechanicUIOpen = var1;
   }

   public void damagePlayers(float var1) {
      if (SandboxOptions.instance.PlayerDamageFromCrash.getValue()) {
         if (!GameClient.bClient) {
            for(int var2 = 0; var2 < this.passengers.length; ++var2) {
               if (this.getPassenger(var2).character != null) {
                  IsoGameCharacter var3 = this.getPassenger(var2).character;
                  if (GameServer.bServer && var3 instanceof IsoPlayer) {
                     GameServer.sendPlayerDamagedByCarCrash((IsoPlayer)var3, var1);
                  } else {
                     this.addRandomDamageFromCrash(var3, var1);
                  }
               }
            }

         }
      }
   }

   public void addRandomDamageFromCrash(IsoGameCharacter var1, float var2) {
      int var3 = 1;
      if (var2 > 40.0F) {
         var3 = Rand.Next(1, 3);
      }

      if (var2 > 70.0F) {
         var3 = Rand.Next(2, 4);
      }

      int var4 = 0;

      int var5;
      for(var5 = 0; var5 < var1.getVehicle().getPartCount(); ++var5) {
         VehiclePart var6 = var1.getVehicle().getPartByIndex(var5);
         if (var6.window != null && var6.getCondition() < 15) {
            ++var4;
         }
      }

      for(var5 = 0; var5 < var3; ++var5) {
         int var9 = Rand.Next(BodyPartType.ToIndex(BodyPartType.Hand_L), BodyPartType.ToIndex(BodyPartType.MAX));
         BodyPart var7 = var1.getBodyDamage().getBodyPart(BodyPartType.FromIndex(var9));
         float var8 = Math.max(Rand.Next(var2 - 15.0F, var2), 5.0F);
         if (var1.Traits.FastHealer.isSet()) {
            var8 = (float)((double)var8 * 0.8D);
         } else if (var1.Traits.SlowHealer.isSet()) {
            var8 = (float)((double)var8 * 1.2D);
         }

         switch(SandboxOptions.instance.InjurySeverity.getValue()) {
         case 1:
            var8 *= 0.5F;
            break;
         case 3:
            var8 *= 1.5F;
         }

         var8 *= this.getScript().getPlayerDamageProtection();
         var8 = (float)((double)var8 * 0.9D);
         var7.AddDamage(var8);
         if (var8 > 40.0F && Rand.Next(12) == 0) {
            var7.generateDeepWound();
         } else if (var8 > 50.0F && Rand.Next(10) == 0 && SandboxOptions.instance.BoneFracture.getValue()) {
            if (var7.getType() != BodyPartType.Neck && var7.getType() != BodyPartType.Groin) {
               var7.setFractureTime(Rand.Next(Rand.Next(10.0F, var8 + 10.0F), Rand.Next(var8 + 20.0F, var8 + 30.0F)));
            } else {
               var7.generateDeepWound();
            }
         }

         if (var8 > 30.0F && Rand.Next(12 - var4) == 0) {
            var7 = var1.getBodyDamage().setScratchedWindow();
            if (Rand.Next(5) == 0) {
               var7.generateDeepWound();
               var7.setHaveGlass(true);
            }
         }
      }

   }

   public void hitVehicle(IsoGameCharacter var1, HandWeapon var2) {
      float var3 = 1.0F;
      if (var2 == null) {
         var2 = (HandWeapon)InventoryItemFactory.CreateItem("Base.BareHands");
      }

      var3 = (float)var2.getDoorDamage();
      if (var1.isCriticalHit()) {
         var3 *= 10.0F;
      }

      VehiclePart var4 = this.getNearestBodyworkPart(var1);
      if (var4 != null) {
         VehicleWindow var5 = var4.getWindow();

         for(int var6 = 0; var6 < var4.getChildCount(); ++var6) {
            VehiclePart var7 = var4.getChild(var6);
            if (var7.getWindow() != null) {
               var5 = var7.getWindow();
               break;
            }
         }

         if (var5 != null && var5.getHealth() > 0) {
            var5.damage((int)var3);
            this.transmitPartWindow(var4);
            if (var5.getHealth() == 0) {
               VehicleManager.sendSoundFromServer(this, (byte)1);
            }
         } else {
            var4.setCondition(var4.getCondition() - (int)var3);
            this.transmitPartItem(var4);
         }

         var4.updateFlags = (short)(var4.updateFlags | 2048);
         this.updateFlags = (short)(this.updateFlags | 2048);
      } else {
         Vector3f var8 = (Vector3f)((BaseVehicle.Vector3fObjectPool)TL_vector3f_pool.get()).alloc();
         this.getLocalPos(var1.getX(), var1.getY(), var1.getZ(), var8);
         boolean var9 = var8.x > 0.0F;
         ((BaseVehicle.Vector3fObjectPool)TL_vector3f_pool.get()).release(var8);
         if (var9) {
            this.addDamageFront((int)var3);
         } else {
            this.addDamageRear((int)var3);
         }

         this.updateFlags = (short)(this.updateFlags | 2048);
      }

   }

   public boolean isTrunkLocked() {
      VehiclePart var1 = this.getPartById("TrunkDoor");
      if (var1 == null) {
         var1 = this.getPartById("DoorRear");
      }

      return var1 != null && var1.getDoor() != null && var1.getInventoryItem() != null ? var1.getDoor().isLocked() : false;
   }

   public void setTrunkLocked(boolean var1) {
      VehiclePart var2 = this.getPartById("TrunkDoor");
      if (var2 == null) {
         var2 = this.getPartById("DoorRear");
      }

      if (var2 != null && var2.getDoor() != null && var2.getInventoryItem() != null) {
         var2.getDoor().setLocked(var1);
         if (GameServer.bServer) {
            this.transmitPartDoor(var2);
         }
      }

   }

   public VehiclePart getNearestBodyworkPart(IsoGameCharacter var1) {
      for(int var2 = 0; var2 < this.getPartCount(); ++var2) {
         VehiclePart var3 = this.getPartByIndex(var2);
         if (("door".equals(var3.getCategory()) || "bodywork".equals(var3.getCategory())) && this.isInArea(var3.getArea(), var1) && var3.getCondition() > 0) {
            return var3;
         }
      }

      return null;
   }

   public double getSirenStartTime() {
      return this.sirenStartTime;
   }

   public void setSirenStartTime(double var1) {
      this.sirenStartTime = var1;
   }

   public boolean sirenShutoffTimeExpired() {
      double var1 = SandboxOptions.instance.SirenShutoffHours.getValue();
      if (var1 <= 0.0D) {
         return false;
      } else {
         double var3 = GameTime.instance.getWorldAgeHours();
         if (this.sirenStartTime > var3) {
            this.sirenStartTime = var3;
         }

         return this.sirenStartTime + var1 < var3;
      }
   }

   public void repair() {
      for(int var1 = 0; var1 < this.getPartCount(); ++var1) {
         VehiclePart var2 = this.getPartByIndex(var1);
         var2.repair();
      }

      this.rust = 0.0F;
      this.transmitRust();
      this.bloodIntensity.clear();
      this.transmitBlood();
      this.doBloodOverlay();
   }

   public boolean isAnyListenerInside() {
      for(int var1 = 0; var1 < this.getMaxPassengers(); ++var1) {
         IsoGameCharacter var2 = this.getCharacter(var1);
         if (var2 instanceof IsoPlayer && ((IsoPlayer)var2).isLocalPlayer() && !var2.Traits.Deaf.isSet()) {
            return true;
         }
      }

      return false;
   }

   public boolean couldCrawlerAttackPassenger(IsoGameCharacter var1) {
      int var2 = this.getSeat(var1);
      return var2 == -1 ? false : false;
   }

   public boolean isGoodCar() {
      return this.isGoodCar;
   }

   public void setGoodCar(boolean var1) {
      this.isGoodCar = var1;
   }

   public InventoryItem getCurrentKey() {
      return this.currentKey;
   }

   public void setCurrentKey(InventoryItem var1) {
      this.currentKey = var1;
   }

   public boolean isInForest() {
      return this.getSquare() != null && this.getSquare().getZone() != null && ("Forest".equals(this.getSquare().getZone().getType()) || "DeepForest".equals(this.getSquare().getZone().getType()) || "FarmLand".equals(this.getSquare().getZone().getType()));
   }

   public float getOffroadEfficiency() {
      return this.isInForest() ? this.script.getOffroadEfficiency() * 1.5F : this.script.getOffroadEfficiency() * 2.0F;
   }

   public void doChrHitImpulse(IsoObject var1) {
      float var2 = 22.0F;
      Vector3f var3 = this.getLinearVelocity((Vector3f)((BaseVehicle.Vector3fObjectPool)TL_vector3f_pool.get()).alloc());
      var3.y = 0.0F;
      Vector3f var4 = (Vector3f)((BaseVehicle.Vector3fObjectPool)TL_vector3f_pool.get()).alloc();
      var4.set(this.x - var1.getX(), 0.0F, this.z - var1.getY());
      var4.normalize();
      var3.mul((Vector3fc)var4);
      ((BaseVehicle.Vector3fObjectPool)TL_vector3f_pool.get()).release(var4);
      float var5 = var3.length();
      var5 = Math.min(var5, var2);
      if (var5 < 0.05F) {
         ((BaseVehicle.Vector3fObjectPool)TL_vector3f_pool.get()).release(var3);
      } else {
         if (GameServer.bServer) {
            if (var1 instanceof IsoZombie) {
               ((IsoZombie)var1).setThumpFlag(1);
            }
         } else {
            SoundManager.instance.PlayWorldSound("ZombieThumpGeneric", var1.square, 0.0F, 20.0F, 0.9F, true);
         }

         Vector3f var6 = (Vector3f)((BaseVehicle.Vector3fObjectPool)TL_vector3f_pool.get()).alloc();
         var6.set(this.x - var1.getX(), 0.0F, this.y - var1.getY());
         var6.normalize();
         var3.normalize();
         float var7 = var3.dot(var6);
         ((BaseVehicle.Vector3fObjectPool)TL_vector3f_pool.get()).release(var3);
         ((BaseVehicle.Vector3fObjectPool)TL_vector3f_pool.get()).release(var6);
         this.ApplyImpulse(var1, this.getFudgedMass() * 3.0F * var5 / var2 * Math.abs(var7));
      }
   }

   public boolean isDoColor() {
      return this.doColor;
   }

   public void setDoColor(boolean var1) {
      this.doColor = var1;
   }

   public float getBrakeSpeedBetweenUpdate() {
      return this.brakeBetweenUpdatesSpeed;
   }

   public IsoGridSquare getSquare() {
      return this.getCell().getGridSquare((double)this.x, (double)this.y, (double)this.z);
   }

   public void setColor(float var1, float var2, float var3) {
      this.colorValue = var1;
      this.colorSaturation = var2;
      this.colorHue = var3;
   }

   public void setColorHSV(float var1, float var2, float var3) {
      this.colorHue = var1;
      this.colorSaturation = var2;
      this.colorValue = var3;
   }

   public float getColorHue() {
      return this.colorHue;
   }

   public float getColorSaturation() {
      return this.colorSaturation;
   }

   public float getColorValue() {
      return this.colorValue;
   }

   public boolean isRemovedFromWorld() {
      return this.removedFromWorld;
   }

   public float getInsideTemperature() {
      VehiclePart var1 = this.getPartById("PassengerCompartment");
      float var2 = 0.0F;
      if (var1 != null && var1.getModData() != null) {
         if (var1.getModData().rawget("temperature") != null) {
            var2 += ((Double)var1.getModData().rawget("temperature")).floatValue();
         }

         if (var1.getModData().rawget("windowtemperature") != null) {
            var2 += ((Double)var1.getModData().rawget("windowtemperature")).floatValue();
         }
      }

      return var2;
   }

   public AnimationPlayer getAnimationPlayer() {
      String var1 = this.getScript().getModel().file;
      Model var2 = ModelManager.instance.getLoadedModel(var1);
      if (var2 != null && !var2.bStatic) {
         if (this.m_animPlayer != null && this.m_animPlayer.getModel() != var2) {
            this.m_animPlayer = (AnimationPlayer)Pool.tryRelease((IPooledObject)this.m_animPlayer);
         }

         if (this.m_animPlayer == null) {
            this.m_animPlayer = AnimationPlayer.alloc(var2);
         }

         return this.m_animPlayer;
      } else {
         return null;
      }
   }

   public void releaseAnimationPlayers() {
      this.m_animPlayer = (AnimationPlayer)Pool.tryRelease((IPooledObject)this.m_animPlayer);
      PZArrayUtil.forEach((List)this.models, BaseVehicle.ModelInfo::releaseAnimationPlayer);
   }

   public void setAddThumpWorldSound(boolean var1) {
      this.bAddThumpWorldSound = var1;
   }

   public void Thump(IsoMovingObject var1) {
      VehiclePart var2 = this.getPartById("lightbar");
      if (var2 != null) {
         if (var2.getCondition() <= 0) {
            var1.setThumpTarget((Thumpable)null);
         }

         VehiclePart var3 = this.getUseablePart((IsoGameCharacter)var1);
         if (var3 != null) {
            var3.setCondition(var3.getCondition() - Rand.Next(1, 5));
         }

         var2.setCondition(var2.getCondition() - Rand.Next(1, 5));
      }
   }

   public void WeaponHit(IsoGameCharacter var1, HandWeapon var2) {
   }

   public Thumpable getThumpableFor(IsoGameCharacter var1) {
      return null;
   }

   public float getThumpCondition() {
      return 1.0F;
   }

   public boolean isRegulator() {
      return this.regulator;
   }

   public void setRegulator(boolean var1) {
      this.regulator = var1;
   }

   public float getRegulatorSpeed() {
      return this.regulatorSpeed;
   }

   public void setRegulatorSpeed(float var1) {
      this.regulatorSpeed = var1;
   }

   public void setVehicleTowing(BaseVehicle var1, String var2, String var3, float var4) {
      this.vehicleTowing = var1;
      this.vehicleTowingID = this.vehicleTowing == null ? -1 : this.vehicleTowing.getSqlId();
      this.towAttachmentSelf = var2;
      this.towAttachmentOther = var3;
      this.towConstraintZOffset = var4;
   }

   public void setVehicleTowedBy(BaseVehicle var1, String var2, String var3, float var4) {
      this.vehicleTowedBy = var1;
      this.vehicleTowedByID = this.vehicleTowedBy == null ? -1 : this.vehicleTowedBy.getSqlId();
      this.towAttachmentSelf = var3;
      this.towAttachmentOther = var2;
      this.towConstraintZOffset = var4;
   }

   public BaseVehicle getVehicleTowing() {
      return this.vehicleTowing;
   }

   public BaseVehicle getVehicleTowedBy() {
      return this.vehicleTowedBy;
   }

   public boolean attachmentExist(String var1) {
      VehicleScript var2 = this.getScript();
      if (var2 == null) {
         return false;
      } else {
         ModelAttachment var3 = var2.getAttachmentById(var1);
         return var3 != null;
      }
   }

   public Vector3f getAttachmentLocalPos(String var1, Vector3f var2) {
      VehicleScript var3 = this.getScript();
      if (var3 == null) {
         return null;
      } else {
         ModelAttachment var4 = var3.getAttachmentById(var1);
         if (var4 == null) {
            return null;
         } else {
            var2.set((Vector3fc)var4.getOffset());
            return var3.getModel() == null ? var2 : var2.add(var3.getModel().getOffset());
         }
      }
   }

   public Vector3f getAttachmentWorldPos(String var1, Vector3f var2) {
      var2 = this.getAttachmentLocalPos(var1, var2);
      return var2 == null ? null : this.getWorldPos(var2, var2);
   }

   public Vector3f getTowingLocalPos(String var1, Vector3f var2) {
      return this.getAttachmentLocalPos(var1, var2);
   }

   public Vector3f getTowedByLocalPos(String var1, Vector3f var2) {
      return this.getAttachmentLocalPos(var1, var2);
   }

   public Vector3f getTowingWorldPos(String var1, Vector3f var2) {
      var2 = this.getTowingLocalPos(var1, var2);
      return var2 == null ? null : this.getWorldPos(var2, var2);
   }

   public Vector3f getTowedByWorldPos(String var1, Vector3f var2) {
      var2 = this.getTowedByLocalPos(var1, var2);
      return var2 == null ? null : this.getWorldPos(var2, var2);
   }

   public Vector3f getPlayerTrailerLocalPos(String var1, boolean var2, Vector3f var3) {
      ModelAttachment var4 = this.getScript().getAttachmentById(var1);
      if (var4 == null) {
         return null;
      } else {
         Vector3f var5 = this.getScript().getExtents();
         Vector3f var6 = this.getScript().getCenterOfMassOffset();
         float var7 = var6.x + var5.x / 2.0F + 0.3F + 0.05F;
         if (!var2) {
            var7 *= -1.0F;
         }

         return var4.getOffset().z > 0.0F ? var3.set(var7, 0.0F, var6.z + var5.z / 2.0F + 0.3F + 0.05F) : var3.set(var7, 0.0F, var6.z - (var5.z / 2.0F + 0.3F + 0.05F));
      }
   }

   public Vector3f getPlayerTrailerWorldPos(String var1, boolean var2, Vector3f var3) {
      var3 = this.getPlayerTrailerLocalPos(var1, var2, var3);
      if (var3 == null) {
         return null;
      } else {
         this.getWorldPos(var3, var3);
         var3.z = (float)((int)this.z);
         Vector3f var4 = this.getTowingWorldPos(var1, (Vector3f)((BaseVehicle.Vector3fObjectPool)TL_vector3f_pool.get()).alloc());
         boolean var5 = PolygonalMap2.instance.lineClearCollide(var3.x, var3.y, var4.x, var4.y, (int)this.z, this, false, false);
         ((BaseVehicle.Vector3fObjectPool)TL_vector3f_pool.get()).release(var4);
         return var5 ? null : var3;
      }
   }

   public void addHingeConstraint(BaseVehicle var1, String var2, String var3) {
      this.breakConstraint(true, false);
      var1.breakConstraint(true, false);
      BaseVehicle.Vector3fObjectPool var5 = (BaseVehicle.Vector3fObjectPool)TL_vector3f_pool.get();
      Vector3f var6 = this.getTowingLocalPos(var2, (Vector3f)var5.alloc());
      Vector3f var7 = var1.getTowedByLocalPos(var3, (Vector3f)var5.alloc());
      if (var6 != null && var7 != null) {
         this.constraintTowing = Bullet.addHingeConstraint(this.VehicleID, var1.VehicleID, var6.x, var6.y, var6.z, var7.x, var7.y, var7.z);
         var1.constraintTowing = this.constraintTowing;
         this.setVehicleTowing(var1, var2, var3, 0.0F);
         var1.setVehicleTowedBy(this, var2, var3, 0.0F);
         var5.release(var6);
         var5.release(var7);
         this.constraintChanged();
         var1.constraintChanged();
      } else {
         if (var6 != null) {
            var5.release(var6);
         }

         if (var7 != null) {
            var5.release(var7);
         }

      }
   }

   private void drawTowingRope() {
      BaseVehicle var1 = this.getVehicleTowing();
      if (var1 != null) {
         BaseVehicle.Vector3fObjectPool var2 = (BaseVehicle.Vector3fObjectPool)TL_vector3f_pool.get();
         this.getAttachmentWorldPos("trailer", (Vector3f)var2.alloc());
         Vector3f var4 = this.getAttachmentWorldPos("trailerfront", (Vector3f)var2.alloc());
         ModelAttachment var5 = this.script.getAttachmentById("trailerfront");
         if (var5 != null) {
            var4.set((Vector3fc)var5.getOffset());
         }

         Vector2 var6 = new Vector2();
         var6.x = var1.x;
         var6.y = var1.y;
         var6.x -= this.x;
         var6.y -= this.y;
         var6.setLength(2.0F);
         this.drawDirectionLine(var6, var6.getLength(), 1.0F, 0.5F, 0.5F);
      }
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

   public void updateConstraint(BaseVehicle var1) {
      if (!this.getScriptName().contains("Trailer") && !var1.getScriptName().contains("Trailer")) {
         ModelAttachment var3 = this.script.getAttachmentById(this.towAttachmentSelf);
         if (var3 != null && var3.isUpdateConstraint()) {
            Vector3f var4 = this.getForwardVector((Vector3f)((BaseVehicle.Vector3fObjectPool)TL_vector3f_pool.get()).alloc());
            Vector3f var5 = this.getLinearVelocity((Vector3f)((BaseVehicle.Vector3fObjectPool)TL_vector3f_pool.get()).alloc());
            float var6 = var5.dot(var4);
            if (var5.lengthSquared() < 0.25F) {
               var6 = 0.0F;
            }

            boolean var7 = var6 > 0.0F;
            boolean var8 = var6 < 0.0F;
            float var9 = this.towConstraintZOffset;
            float var10 = GameTime.getInstance().getMultiplier() / 0.8F;
            if (var3.getZOffset() > 0.0F) {
               if ((var7 && this.isBraking || this.getController().EngineForce < 0.0F) && var9 < 1.0F) {
                  var9 = Math.min(1.0F, var9 + 0.015F * var10);
               } else if ((var8 && this.isBraking || this.getController().EngineForce > 0.0F) && var9 > 0.1F) {
                  var9 = Math.max(0.1F, var9 - 0.01F * var10);
               }
            } else if (var3.getZOffset() < 0.0F) {
               if ((var7 && this.isBraking || this.getController().EngineForce < 0.0F) && var9 < 0.1F) {
                  var9 = Math.min(0.1F, var9 + 0.015F * var10);
               } else if ((var8 && this.isBraking || this.getController().EngineForce > 0.0F) && var9 > -1.0F) {
                  var9 = Math.max(-1.0F, var9 - 0.01F * var10);
               }
            }

            if (var9 != this.towConstraintZOffset) {
               this.addPointConstraint(var1, this.towAttachmentSelf, var1.towAttachmentSelf, var9, true);
            }

            ((BaseVehicle.Vector3fObjectPool)TL_vector3f_pool.get()).release(var4);
            ((BaseVehicle.Vector3fObjectPool)TL_vector3f_pool.get()).release(var5);
         }
      }
   }

   public void addPointConstraint(BaseVehicle var1, String var2, String var3) {
      this.addPointConstraint(var1, var2, var3, (Float)null, false);
   }

   public void addPointConstraint(BaseVehicle var1, String var2, String var3, Float var4, Boolean var5) {
      this.breakConstraint(true, true);
      var1.breakConstraint(true, true);
      BaseVehicle.Vector3fObjectPool var7 = (BaseVehicle.Vector3fObjectPool)TL_vector3f_pool.get();
      Vector3f var8 = this.getTowingLocalPos(var2, (Vector3f)var7.alloc());
      Vector3f var9 = var1.getTowedByLocalPos(var3, (Vector3f)var7.alloc());
      if (var8 != null && var9 != null) {
         ModelAttachment var10 = this.script.getAttachmentById(var2);
         float var11 = 0.0F;
         float var12 = 0.0F;
         if (var10 != null && var10.getZOffset() != 0.0F) {
            var11 = var10.getZOffset();
         }

         if (var4 != null) {
            var11 = var4;
         }

         if (this.getScriptName().contains("Trailer") || var1.getScriptName().contains("Trailer")) {
            var11 = 0.0F;
         }

         this.constraintTowing = Bullet.addPointConstraint(this.VehicleID, var1.VehicleID, var8.x, var8.y, var8.z + var11, var9.x, var9.y, var9.z + var12);
         var1.constraintTowing = this.constraintTowing;
         this.setVehicleTowing(var1, var2, var3, var11);
         var1.setVehicleTowedBy(this, var2, var3, var12);
         var7.release(var8);
         var7.release(var9);
         this.constraintChanged();
         var1.constraintChanged();
         if (GameClient.bClient && !var5) {
            VehicleManager.instance.sendTowing(this, var1, var2, var3, var4);
         }

      } else {
         if (var8 != null) {
            var7.release(var8);
         }

         if (var9 != null) {
            var7.release(var9);
         }

      }
   }

   public void constraintChanged() {
      long var1 = System.currentTimeMillis();
      this.setPhysicsActive(true);
      this.constraintChangedTime = var1;
   }

   public void breakConstraint(boolean var1, boolean var2) {
      if (this.constraintTowing != -1) {
         Bullet.removeConstraint(this.constraintTowing);
         this.constraintTowing = -1;
         this.constraintChanged();
         if (GameClient.bClient && !var2) {
            VehicleManager.instance.sendDetachTowing(this.vehicleTowing, this.vehicleTowedBy);
         }

         if (this.vehicleTowing != null) {
            this.vehicleTowing.constraintChanged();
            this.vehicleTowing.vehicleTowedBy = null;
            this.vehicleTowing.constraintTowing = -1;
            if (var1) {
               this.vehicleTowingID = -1;
               this.vehicleTowing.vehicleTowedByID = -1;
            }

            this.vehicleTowing = null;
         }

         if (this.vehicleTowedBy != null) {
            this.vehicleTowedBy.constraintChanged();
            this.vehicleTowedBy.vehicleTowing = null;
            this.vehicleTowedBy.constraintTowing = -1;
            if (var1) {
               this.vehicleTowedBy.vehicleTowingID = -1;
               this.vehicleTowedByID = -1;
            }

            this.vehicleTowedBy = null;
         }

      }
   }

   public boolean canAttachTrailer(BaseVehicle var1, String var2, String var3) {
      if (this != var1 && this.physics != null && this.constraintTowing == -1) {
         if (var1 != null && var1.physics != null && var1.constraintTowing == -1) {
            if (!GameClient.bClient) {
               float var5 = GameServer.bServer ? this.netLinearVelocity.y : this.jniLinearVelocity.y;
               if (Math.abs(var5) > 0.2F) {
                  return false;
               }

               float var6 = GameServer.bServer ? var1.netLinearVelocity.y : var1.jniLinearVelocity.y;
               if (Math.abs(var6) > 0.2F) {
                  return false;
               }
            }

            long var13 = System.currentTimeMillis();
            if (this.createPhysicsTime + 1000L > var13) {
               return false;
            } else if (var1.createPhysicsTime + 1000L > var13) {
               return false;
            } else {
               BaseVehicle.Vector3fObjectPool var7 = (BaseVehicle.Vector3fObjectPool)TL_vector3f_pool.get();
               Vector3f var8 = this.getTowingWorldPos(var2, (Vector3f)var7.alloc());
               Vector3f var9 = var1.getTowedByWorldPos(var3, (Vector3f)var7.alloc());
               if (var8 != null && var9 != null) {
                  float var10 = IsoUtils.DistanceToSquared(var8.x, var8.y, var8.z, var9.x, var9.y, var9.z);
                  var7.release(var8);
                  var7.release(var9);
                  ModelAttachment var11 = this.script.getAttachmentById(var2);
                  ModelAttachment var12 = var1.script.getAttachmentById(var3);
                  if (var11 != null && var11.getCanAttach() != null && !var11.getCanAttach().contains(var3)) {
                     return false;
                  } else if (var12 != null && var12.getCanAttach() != null && !var12.getCanAttach().contains(var2)) {
                     return false;
                  } else {
                     return var10 < 2.0F;
                  }
               } else {
                  return false;
               }
            }
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   private void tryReconnectToTowedVehicle() {
      if (!GameClient.bClient) {
         if (this.vehicleTowing == null) {
            if (this.vehicleTowingID != -1) {
               BaseVehicle var1 = null;
               ArrayList var2 = IsoWorld.instance.CurrentCell.getVehicles();

               for(int var3 = 0; var3 < var2.size(); ++var3) {
                  BaseVehicle var4 = (BaseVehicle)var2.get(var3);
                  if (var4.getSqlId() == this.vehicleTowingID) {
                     var1 = var4;
                     break;
                  }
               }

               if (var1 != null) {
                  if (this.canAttachTrailer(var1, this.towAttachmentSelf, this.towAttachmentOther)) {
                     this.addPointConstraint(var1, this.towAttachmentSelf, this.towAttachmentOther, this.towConstraintZOffset, false);
                  }
               }
            }
         }
      }
   }

   public void positionTrailer(BaseVehicle var1) {
      if (var1 != null) {
         BaseVehicle.Vector3fObjectPool var2 = (BaseVehicle.Vector3fObjectPool)TL_vector3f_pool.get();
         Vector3f var3 = this.getTowingWorldPos("trailer", (Vector3f)var2.alloc());
         Vector3f var4 = var1.getTowedByWorldPos("trailer", (Vector3f)var2.alloc());
         if (var3 != null && var4 != null) {
            var4.sub(var1.x, var1.y, var1.z);
            var3.sub(var4);
            Transform var5 = var1.getWorldTransform(this.tempTransform);
            var5.origin.set(var3.x - WorldSimulation.instance.offsetX, var1.jniTransform.origin.y, var3.y - WorldSimulation.instance.offsetY);
            var1.setWorldTransform(var5);
            var1.setX(var3.x);
            var1.setLx(var3.x);
            var1.setY(var3.y);
            var1.setLy(var3.y);
            var1.setCurrent(this.getCell().getGridSquare((double)var3.x, (double)var3.y, 0.0D));
            this.addPointConstraint(var1, "trailer", "trailer");
            var2.release(var3);
            var2.release(var4);
         }
      }
   }

   public String getTowAttachmentSelf() {
      return this.towAttachmentSelf;
   }

   public String getTowAttachmentOther() {
      return this.towAttachmentOther;
   }

   public VehicleEngineRPM getVehicleEngineRPM() {
      if (this.vehicleEngineRPM == null) {
         this.vehicleEngineRPM = ScriptManager.instance.getVehicleEngineRPM(this.getScript().getEngineRPMType());
         if (this.vehicleEngineRPM == null) {
            DebugLog.General.warn("unknown vehicleEngineRPM \"%s\"", this.getScript().getEngineRPMType());
            this.vehicleEngineRPM = new VehicleEngineRPM();
         }
      }

      return this.vehicleEngineRPM;
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

   private void stopEngineSounds() {
      if (this.emitter != null) {
         for(int var1 = 0; var1 < this.new_EngineSoundId.length; ++var1) {
            if (this.new_EngineSoundId[var1] != 0L) {
               this.getEmitter().stopSound(this.new_EngineSoundId[var1]);
               this.new_EngineSoundId[var1] = 0L;
            }
         }

         if (this.combinedEngineSound != 0L) {
            if (this.getEmitter().hasSustainPoints(this.combinedEngineSound)) {
               this.getEmitter().triggerCue(this.combinedEngineSound);
            } else {
               this.getEmitter().stopSound(this.combinedEngineSound);
            }

            this.combinedEngineSound = 0L;
         }

      }
   }

   public void debugSetStatic(boolean var1) {
      Bullet.setVehicleStatic(this.getId(), var1);
   }

   public BaseVehicle setSmashed(String var1) {
      return this.setSmashed(var1, false);
   }

   public BaseVehicle setSmashed(String var1, boolean var2) {
      String var3 = null;
      Integer var4 = null;
      KahluaTableImpl var5 = (KahluaTableImpl)LuaManager.env.rawget("SmashedCarDefinitions");
      if (var5 != null) {
         KahluaTableImpl var6 = (KahluaTableImpl)var5.rawget("cars");
         if (var6 != null) {
            KahluaTableImpl var7 = (KahluaTableImpl)var6.rawget(this.getScriptName());
            if (var7 != null) {
               var3 = var7.rawgetStr(var1.toLowerCase());
               var4 = var7.rawgetInt("skin");
               if (var4 == -1) {
                  var4 = this.getSkinIndex();
               }
            }
         }
      }

      if (var3 != null) {
         this.removeFromWorld();
         this.permanentlyRemove();
         BaseVehicle var8 = new BaseVehicle(IsoWorld.instance.CurrentCell);
         var8.setScriptName(var3);
         var8.setScript();
         var8.setSkinIndex(var4);
         var8.setX(this.x);
         var8.setY(this.y);
         var8.setZ(this.z);
         var8.setDir(this.getDir());
         var8.savedRot.set((Quaternionfc)this.savedRot);
         if (var2) {
            float var9 = this.getAngleY();
            var8.savedRot.rotationXYZ(0.0F, var9 * 0.017453292F, 3.1415927F);
         }

         var8.jniTransform.setRotation(var8.savedRot);
         if (IsoChunk.doSpawnedVehiclesInInvalidPosition(var8)) {
            var8.setSquare(this.square);
            var8.square.chunk.vehicles.add(var8);
            var8.chunk = var8.square.chunk;
            var8.addToWorld();
            VehiclesDB2.instance.addVehicle(var8);
         }

         var8.setGeneralPartCondition(0.5F, 60.0F);
         VehiclePart var10 = var8.getPartById("Engine");
         if (var10 != null) {
            var10.setCondition(0);
         }

         var8.engineQuality = 0;
         return var8;
      } else {
         return this;
      }
   }

   public boolean isCollided(IsoGameCharacter var1) {
      if (GameClient.bClient && this.getDriver() != null && !this.getDriver().isLocal()) {
         return true;
      } else {
         Vector2 var2 = this.testCollisionWithCharacter(var1, 0.20000002F, this.hitVars.collision);
         return var2 != null && var2.x != -1.0F;
      }
   }

   public BaseVehicle.HitVars checkCollision(IsoGameCharacter var1) {
      if (var1.isProne()) {
         int var2 = this.testCollisionWithProneCharacter(var1, true);
         if (var2 > 0) {
            this.hitVars.calc(var1, this);
            this.hitCharacter(var1, this.hitVars);
            return this.hitVars;
         } else {
            return null;
         }
      } else {
         this.hitVars.calc(var1, this);
         this.hitCharacter(var1, this.hitVars);
         return this.hitVars;
      }
   }

   public boolean updateHitByVehicle(IsoGameCharacter var1) {
      if (var1.isVehicleCollisionActive(this) && (this.isCollided(var1) || var1.isCollidedWithVehicle()) && this.physics != null) {
         BaseVehicle.HitVars var2 = this.checkCollision(var1);
         if (var2 != null) {
            var1.doHitByVehicle(this, var2);
            return true;
         }
      }

      return false;
   }

   public void hitCharacter(IsoGameCharacter var1, BaseVehicle.HitVars var2) {
      if (var2.dot < 0.0F && !GameServer.bServer) {
         this.ApplyImpulse(var1, var2.vehicleImpulse);
      }

      long var3 = System.currentTimeMillis();
      long var5 = (var3 - this.zombieHitTimestamp) / 1000L;
      this.zombiesHits = Math.max(this.zombiesHits - (int)var5, 0);
      if (var3 - this.zombieHitTimestamp > 700L) {
         this.zombieHitTimestamp = var3;
         ++this.zombiesHits;
         this.zombiesHits = Math.min(this.zombiesHits, 20);
      }

      if (var1 instanceof IsoPlayer) {
         ((IsoPlayer)var1).setVehicleHitLocation(this);
      } else if (var1 instanceof IsoZombie) {
         ((IsoZombie)var1).setVehicleHitLocation(this);
      }

      if (var2.vehicleSpeed >= 5.0F || this.zombiesHits > 10) {
         var2.vehicleSpeed = this.getCurrentSpeedKmHour() / 5.0F;
         Vector3f var7 = (Vector3f)((BaseVehicle.Vector3fObjectPool)TL_vector3f_pool.get()).alloc();
         this.getLocalPos(var1.x, var1.y, var1.z, var7);
         int var8;
         if (var7.z > 0.0F) {
            var8 = this.caclulateDamageWithBodies(true);
            if (!GameClient.bClient) {
               this.addDamageFrontHitAChr(var8);
            }

            if (Core.bDebug) {
               DebugLog.log(DebugType.Damage, "Damage car front hits=" + this.zombiesHits + " damage=" + var8);
            }

            var2.vehicleDamage = var8;
            var2.isVehicleHitFromFront = true;
         } else {
            var8 = this.caclulateDamageWithBodies(false);
            if (!GameClient.bClient) {
               this.addDamageRearHitAChr(var8);
            }

            if (Core.bDebug) {
               DebugLog.log(DebugType.Damage, "Damage car rear hits=" + this.zombiesHits + " damage=" + var8);
            }

            var2.vehicleDamage = var8;
            var2.isVehicleHitFromFront = false;
         }

         ((BaseVehicle.Vector3fObjectPool)TL_vector3f_pool.get()).release(var7);
      }

   }

   protected static class UpdateFlags {
      public static final short Full = 1;
      public static final short PositionOrientation = 2;
      public static final short Engine = 4;
      public static final short Lights = 8;
      public static final short PartModData = 16;
      public static final short PartUsedDelta = 32;
      public static final short PartModels = 64;
      public static final short PartItem = 128;
      public static final short PartWindow = 256;
      public static final short PartDoor = 512;
      public static final short Sounds = 1024;
      public static final short PartCondition = 2048;
      public static final short UpdateCarProperties = 4096;
      public static final short EngineSound = 8192;
      public static final short Authorization = 16384;
      public static final short AllPartFlags = 19440;
   }

   public static final class Vector2ObjectPool extends ObjectPool {
      int allocated = 0;

      Vector2ObjectPool() {
         super(Vector2::new);
      }

      protected Vector2 makeObject() {
         ++this.allocated;
         return (Vector2)super.makeObject();
      }
   }

   public static final class Vector3fObjectPool extends ObjectPool {
      int allocated = 0;

      Vector3fObjectPool() {
         super(Vector3f::new);
      }

      protected Vector3f makeObject() {
         ++this.allocated;
         return (Vector3f)super.makeObject();
      }
   }

   private static final class VehicleImpulse {
      static final ArrayDeque pool = new ArrayDeque();
      final Vector3f impulse = new Vector3f();
      final Vector3f rel_pos = new Vector3f();
      boolean enable = false;

      static BaseVehicle.VehicleImpulse alloc() {
         return pool.isEmpty() ? new BaseVehicle.VehicleImpulse() : (BaseVehicle.VehicleImpulse)pool.pop();
      }

      void release() {
         pool.push(this);
      }
   }

   public static enum engineStateTypes {
      Idle,
      Starting,
      RetryingStarting,
      StartingSuccess,
      StartingFailed,
      Running,
      Stalling,
      ShutingDown,
      StartingFailedNoPower;

      public static final BaseVehicle.engineStateTypes[] Values = values();

      // $FF: synthetic method
      private static BaseVehicle.engineStateTypes[] $values() {
         return new BaseVehicle.engineStateTypes[]{Idle, Starting, RetryingStarting, StartingSuccess, StartingFailed, Running, Stalling, ShutingDown, StartingFailedNoPower};
      }
   }

   public static final class WheelInfo {
      public float steering;
      public float rotation;
      public float skidInfo;
      public float suspensionLength;
   }

   public static final class ServerVehicleState {
      private static final float delta = 0.01F;
      public float x = -1.0F;
      public float y;
      public float z;
      public Quaternionf orient = new Quaternionf();
      public short flags = 0;
      public byte netPlayerAuthorization = 0;
      public int netPlayerId = 0;

      public void setAuthorization(BaseVehicle var1) {
         this.netPlayerAuthorization = var1.netPlayerAuthorization;
         this.netPlayerId = var1.netPlayerId;
      }

      public boolean shouldSend(BaseVehicle var1) {
         if (var1.getController() == null) {
            return false;
         } else if (var1.updateLockTimeout > System.currentTimeMillis()) {
            return false;
         } else {
            this.flags = (short)(this.flags & 1);
            if (Math.abs(this.x - var1.x) > 0.01F || Math.abs(this.y - var1.y) > 0.01F || Math.abs(this.z - var1.jniTransform.origin.y) > 0.01F || Math.abs(this.orient.x - var1.savedRot.x) > 0.01F || Math.abs(this.orient.y - var1.savedRot.y) > 0.01F || Math.abs(this.orient.z - var1.savedRot.z) > 0.01F || Math.abs(this.orient.w - var1.savedRot.w) > 0.01F) {
               this.flags = (short)(this.flags | 2);
            }

            if (this.netPlayerAuthorization != var1.netPlayerAuthorization || this.netPlayerId != var1.netPlayerId) {
               this.flags = (short)(this.flags | 16384);
            }

            this.flags |= var1.updateFlags;
            return this.flags != 0;
         }
      }
   }

   public static final class Passenger {
      public IsoGameCharacter character;
      final Vector3f offset = new Vector3f();
   }

   public static class HitVars {
      private static final float speedCap = 10.0F;
      private final Vector3f velocity = new Vector3f();
      private final Vector2 collision = new Vector2();
      private float dot;
      protected float vehicleImpulse;
      protected float vehicleSpeed;
      public final Vector3f targetImpulse = new Vector3f();
      public boolean isVehicleHitFromFront;
      public boolean isTargetHitFromBehind;
      public int vehicleDamage;
      public float hitSpeed;

      public void calc(IsoGameCharacter var1, BaseVehicle var2) {
         var2.getLinearVelocity(this.velocity);
         this.velocity.y = 0.0F;
         if (var1 instanceof IsoZombie) {
            this.vehicleSpeed = Math.min(this.velocity.length(), 10.0F);
            this.hitSpeed = this.vehicleSpeed + var2.getClientForce() / var2.getFudgedMass();
         } else {
            this.vehicleSpeed = (float)Math.sqrt((double)(this.velocity.x * this.velocity.x + this.velocity.z * this.velocity.z));
            if (var1.isOnFloor()) {
               this.hitSpeed = Math.max(this.vehicleSpeed * 6.0F, 5.0F);
            } else {
               this.hitSpeed = Math.max(this.vehicleSpeed * 2.0F, 5.0F);
            }
         }

         this.targetImpulse.set(var2.x - var1.x, 0.0F, var2.y - var1.y);
         this.targetImpulse.normalize();
         this.velocity.normalize();
         this.dot = this.velocity.dot(this.targetImpulse);
         this.targetImpulse.normalize();
         this.targetImpulse.mul(3.0F * this.vehicleSpeed / 10.0F);
         this.targetImpulse.set(this.targetImpulse.x, this.targetImpulse.y, this.targetImpulse.z);
         this.vehicleImpulse = var2.getFudgedMass() * 7.0F * this.vehicleSpeed / 10.0F * Math.abs(this.dot);
         this.isTargetHitFromBehind = "BEHIND".equals(var1.testDotSide(var2));
      }
   }

   public static final class ModelInfo {
      public VehiclePart part;
      public VehicleScript.Model scriptModel;
      public ModelScript modelScript;
      public int wheelIndex;
      public final Matrix4f renderTransform = new Matrix4f();
      public VehicleSubModelInstance modelInstance;
      public AnimationPlayer m_animPlayer;
      public AnimationTrack m_track;

      public AnimationPlayer getAnimationPlayer() {
         if (this.part != null && this.part.getParent() != null) {
            BaseVehicle.ModelInfo var1 = this.part.getVehicle().getModelInfoForPart(this.part.getParent());
            if (var1 != null) {
               return var1.getAnimationPlayer();
            }
         }

         String var3 = this.scriptModel.file;
         Model var2 = ModelManager.instance.getLoadedModel(var3);
         if (var2 != null && !var2.bStatic) {
            if (this.m_animPlayer != null && this.m_animPlayer.getModel() != var2) {
               this.m_animPlayer = (AnimationPlayer)Pool.tryRelease((IPooledObject)this.m_animPlayer);
            }

            if (this.m_animPlayer == null) {
               this.m_animPlayer = AnimationPlayer.alloc(var2);
            }

            return this.m_animPlayer;
         } else {
            return null;
         }
      }

      public void releaseAnimationPlayer() {
         this.m_animPlayer = (AnimationPlayer)Pool.tryRelease((IPooledObject)this.m_animPlayer);
      }
   }

   public static final class Matrix4fObjectPool extends ObjectPool {
      int allocated = 0;

      Matrix4fObjectPool() {
         super(Matrix4f::new);
      }

      protected Matrix4f makeObject() {
         ++this.allocated;
         return (Matrix4f)super.makeObject();
      }
   }

   public static final class QuaternionfObjectPool extends ObjectPool {
      int allocated = 0;

      QuaternionfObjectPool() {
         super(Quaternionf::new);
      }

      protected Quaternionf makeObject() {
         ++this.allocated;
         return (Quaternionf)super.makeObject();
      }
   }

   private static final class L_testCollisionWithVehicle {
      static final Vector2[] testVecs1 = new Vector2[4];
      static final Vector2[] testVecs2 = new Vector2[4];
      static final Vector3f worldPos = new Vector3f();
   }

   public static final class Vector2fObjectPool extends ObjectPool {
      int allocated = 0;

      Vector2fObjectPool() {
         super(Vector2f::new);
      }

      protected Vector2f makeObject() {
         ++this.allocated;
         return (Vector2f)super.makeObject();
      }
   }

   public static final class MinMaxPosition {
      public float minX;
      public float maxX;
      public float minY;
      public float maxY;
   }
}

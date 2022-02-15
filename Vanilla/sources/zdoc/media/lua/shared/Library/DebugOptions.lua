---@class DebugOptions : zombie.debug.DebugOptions
---@field public VERSION int
---@field public instance DebugOptions
---@field private options ArrayList|Unknown
---@field private m_options ArrayList|Unknown
---@field public AssetSlowLoad BooleanDebugOption
---@field public MultiplayerShowZombieMultiplier BooleanDebugOption
---@field public MultiplayerShowZombieOwner BooleanDebugOption
---@field public MultiplayerShowPosition BooleanDebugOption
---@field public MultiplayerShowTeleport BooleanDebugOption
---@field public MultiplayerShowHit BooleanDebugOption
---@field public MultiplayerLogPrediction BooleanDebugOption
---@field public MultiplayerShowPlayerPrediction BooleanDebugOption
---@field public MultiplayerShowPlayerStatus BooleanDebugOption
---@field public MultiplayerShowZombiePrediction BooleanDebugOption
---@field public MultiplayerShowZombieDesync BooleanDebugOption
---@field public MultiplayerShowZombieStatus BooleanDebugOption
---@field public MultiplayerLightAmbient BooleanDebugOption
---@field public MultiplayerCriticalHit BooleanDebugOption
---@field public MultiplayerTorsoHit BooleanDebugOption
---@field public MultiplayerZombieCrawler BooleanDebugOption
---@field public MultiplayerSpawnZombie BooleanDebugOption
---@field public MultiplayerPlayerZombie BooleanDebugOption
---@field public MultiplayerAttackPlayer BooleanDebugOption
---@field public MultiplayerFollowPlayer BooleanDebugOption
---@field public MultiplayerAutoEquip BooleanDebugOption
---@field public MultiplayerPing BooleanDebugOption
---@field public CheatClockVisible BooleanDebugOption
---@field public CheatDoorUnlock BooleanDebugOption
---@field public CheatPlayerStartInvisible BooleanDebugOption
---@field public CheatPlayerInvisibleSprint BooleanDebugOption
---@field public CheatPlayerSeeEveryone BooleanDebugOption
---@field public CheatUnlimitedAmmo BooleanDebugOption
---@field public CheatRecipeKnowAll BooleanDebugOption
---@field public CheatTimedActionInstant BooleanDebugOption
---@field public CheatVehicleMechanicsAnywhere BooleanDebugOption
---@field public CheatVehicleStartWithoutKey BooleanDebugOption
---@field public CheatWindowUnlock BooleanDebugOption
---@field public CollideWithObstaclesRenderRadius BooleanDebugOption
---@field public CollideWithObstaclesRenderObstacles BooleanDebugOption
---@field public CollideWithObstaclesRenderNormals BooleanDebugOption
---@field public DeadBodyAtlasRender BooleanDebugOption
---@field public DebugScenarioForceLaunch BooleanDebugOption
---@field public MechanicsRenderHitbox BooleanDebugOption
---@field public JoypadRenderUI BooleanDebugOption
---@field public ModelRenderAttachments BooleanDebugOption
---@field public ModelRenderAxis BooleanDebugOption
---@field public ModelRenderBones BooleanDebugOption
---@field public ModelRenderBounds BooleanDebugOption
---@field public ModelRenderLights BooleanDebugOption
---@field public ModelRenderMuzzleflash BooleanDebugOption
---@field public ModelRenderSkipVehicles BooleanDebugOption
---@field public ModelRenderWeaponHitPoint BooleanDebugOption
---@field public ModelRenderWireframe BooleanDebugOption
---@field public ModelSkeleton BooleanDebugOption
---@field public ModRenderLoaded BooleanDebugOption
---@field public PathfindPathToMouseAllowCrawl BooleanDebugOption
---@field public PathfindPathToMouseAllowThump BooleanDebugOption
---@field public PathfindPathToMouseEnable BooleanDebugOption
---@field public PathfindPathToMouseIgnoreCrawlCost BooleanDebugOption
---@field public PathfindRenderPath BooleanDebugOption
---@field public PathfindRenderWaiting BooleanDebugOption
---@field public PhysicsRender BooleanDebugOption
---@field public PolymapRenderClusters BooleanDebugOption
---@field public PolymapRenderConnections BooleanDebugOption
---@field public PolymapRenderCrawling BooleanDebugOption
---@field public PolymapRenderLineClearCollide BooleanDebugOption
---@field public PolymapRenderNodes BooleanDebugOption
---@field public TooltipInfo BooleanDebugOption
---@field public TooltipModName BooleanDebugOption
---@field public TranslationPrefix BooleanDebugOption
---@field public UIRenderOutline BooleanDebugOption
---@field public UIDebugConsoleStartVisible BooleanDebugOption
---@field public UIDebugConsoleDebugLog BooleanDebugOption
---@field public UIDebugConsoleEchoCommand BooleanDebugOption
---@field public VehicleCycleColor BooleanDebugOption
---@field public VehicleRenderBlood0 BooleanDebugOption
---@field public VehicleRenderBlood50 BooleanDebugOption
---@field public VehicleRenderBlood100 BooleanDebugOption
---@field public VehicleRenderDamage0 BooleanDebugOption
---@field public VehicleRenderDamage1 BooleanDebugOption
---@field public VehicleRenderDamage2 BooleanDebugOption
---@field public VehicleRenderRust0 BooleanDebugOption
---@field public VehicleRenderRust50 BooleanDebugOption
---@field public VehicleRenderRust100 BooleanDebugOption
---@field public VehicleRenderOutline BooleanDebugOption
---@field public VehicleRenderArea BooleanDebugOption
---@field public VehicleRenderAuthorizations BooleanDebugOption
---@field public VehicleRenderAttackPositions BooleanDebugOption
---@field public VehicleRenderExit BooleanDebugOption
---@field public VehicleRenderIntersectedSquares BooleanDebugOption
---@field public VehicleRenderTrailerPositions BooleanDebugOption
---@field public VehicleSpawnEverywhere BooleanDebugOption
---@field public WorldSoundRender BooleanDebugOption
---@field public LightingRender BooleanDebugOption
---@field public SkyboxShow BooleanDebugOption
---@field public WorldStreamerSlowLoad BooleanDebugOption
---@field public DebugDraw_SkipVBODraw BooleanDebugOption
---@field public DebugDraw_SkipDrawNonSkinnedModel BooleanDebugOption
---@field public DebugDraw_SkipWorldShading BooleanDebugOption
---@field public GameProfilerEnabled BooleanDebugOption
---@field public GameTimeSpeedHalf BooleanDebugOption
---@field public GameTimeSpeedQuarter BooleanDebugOption
---@field public ThreadCrash_Enabled BooleanDebugOption
---@field public ThreadCrash_GameThread BooleanDebugOption[]
---@field public ThreadCrash_GameLoadingThread BooleanDebugOption[]
---@field public ThreadCrash_RenderThread BooleanDebugOption[]
---@field public WorldChunkMap5x5 BooleanDebugOption
---@field public ZombieRenderCanCrawlUnderVehicle BooleanDebugOption
---@field public ZombieRenderFakeDead BooleanDebugOption
---@field public ZombieRenderMemory BooleanDebugOption
---@field public ZombieOutfitRandom BooleanDebugOption
---@field public Checks DebugOptions.Checks
---@field public IsoSprite IsoSprite
---@field public Network Network
---@field public OffscreenBuffer OffscreenBuffer
---@field public Terrain Terrain
---@field public Weather Weather
---@field public Animation Animation
---@field public Character Character
---@field private m_triggerWatcher PredicatedFileWatcher
DebugOptions = {}

---@private
---@param arg0 String
---@param arg1 boolean
---@return BooleanDebugOption
function DebugOptions:newDebugOnlyOption(arg0, arg1) end

---@private
---@param arg0 IDebugOptionGroup
---@return void
function DebugOptions:addDescendantOptions(arg0) end

---@private
---@param arg0 IDebugOptionGroup
---@return IDebugOptionGroup
function DebugOptions:newOptionGroup(arg0) end

---@public
---@return int
function DebugOptions:getOptionCount() end

---@public
---@param arg0 IDebugOption
---@return void
function DebugOptions:onDescendantAdded(arg0) end

---@public
---@return IDebugOptionGroup
function DebugOptions:getParent() end

---@public
---@param arg0 String
---@return boolean
function DebugOptions:getBoolean(arg0) end

---@public
---@param arg0 int
---@return void
function DebugOptions:testThreadCrash(arg0) end

---@private
---@return void
function DebugOptions:initMessaging() end

---@private
---@param arg0 String
---@param arg1 boolean
---@return BooleanDebugOption
function DebugOptions:newOption(arg0, arg1) end

---@public
---@return void
function DebugOptions:save() end

---@public
---@param arg0 IDebugOption
---@return void
function DebugOptions:onChildAdded(arg0) end

---@private
---@param arg0 IDebugOption
---@return void
function DebugOptions:addOption(arg0) end

---@private
---@param arg0 int
---@return void
function DebugOptions:testThreadCrashInternal(arg0) end

---@public
---@return Iterable|Unknown
function DebugOptions:getChildren() end

---@public
---@param arg0 IDebugOption
---@return void
function DebugOptions:addChild(arg0) end

---@public
---@param arg0 String
---@return BooleanDebugOption
function DebugOptions:getOptionByName(arg0) end

---@public
---@return void
function DebugOptions:init() end

---@private
---@param arg0 String
---@return void
function DebugOptions:onTrigger_SetDebugOptions(arg0) end

---@public
---@param arg0 String
---@param arg1 boolean
---@return void
function DebugOptions:setBoolean(arg0, arg1) end

---@public
---@param arg0 int
---@return BooleanDebugOption
function DebugOptions:getOptionByIndex(arg0) end

---@public
---@param arg0 IDebugOptionGroup
---@return void
function DebugOptions:setParent(arg0) end

---@public
---@return String
function DebugOptions:getName() end

---@public
---@return void
function DebugOptions:load() end

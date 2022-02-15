---@class SandboxOptions : zombie.SandboxOptions
---@field public instance SandboxOptions
---@field public Speed int
---@field public Zombies SandboxOptions.EnumSandboxOption
---@field public Distribution SandboxOptions.EnumSandboxOption
---@field public DayLength SandboxOptions.EnumSandboxOption
---@field public StartYear SandboxOptions.EnumSandboxOption
---@field public StartMonth SandboxOptions.EnumSandboxOption
---@field public StartDay SandboxOptions.EnumSandboxOption
---@field public StartTime SandboxOptions.EnumSandboxOption
---@field public WaterShut SandboxOptions.EnumSandboxOption
---@field public ElecShut SandboxOptions.EnumSandboxOption
---@field public WaterShutModifier SandboxOptions.IntegerSandboxOption
---@field public ElecShutModifier SandboxOptions.IntegerSandboxOption
---@field public FoodLoot SandboxOptions.EnumSandboxOption
---@field public LiteratureLoot SandboxOptions.EnumSandboxOption
---@field public MedicalLoot SandboxOptions.EnumSandboxOption
---@field public SurvivalGearsLoot SandboxOptions.EnumSandboxOption
---@field public CannedFoodLoot SandboxOptions.EnumSandboxOption
---@field public WeaponLoot SandboxOptions.EnumSandboxOption
---@field public RangedWeaponLoot SandboxOptions.EnumSandboxOption
---@field public AmmoLoot SandboxOptions.EnumSandboxOption
---@field public MechanicsLoot SandboxOptions.EnumSandboxOption
---@field public OtherLoot SandboxOptions.EnumSandboxOption
---@field public Temperature SandboxOptions.EnumSandboxOption
---@field public Rain SandboxOptions.EnumSandboxOption
---@field public ErosionSpeed SandboxOptions.EnumSandboxOption
---@field public ErosionDays SandboxOptions.IntegerSandboxOption
---@field public XpMultiplier SandboxOptions.DoubleSandboxOption
---@field public Farming SandboxOptions.EnumSandboxOption
---@field public CompostTime SandboxOptions.EnumSandboxOption
---@field public StatsDecrease SandboxOptions.EnumSandboxOption
---@field public NatureAbundance SandboxOptions.EnumSandboxOption
---@field public Alarm SandboxOptions.EnumSandboxOption
---@field public LockedHouses SandboxOptions.EnumSandboxOption
---@field public StarterKit SandboxOptions.BooleanSandboxOption
---@field public Nutrition SandboxOptions.BooleanSandboxOption
---@field public FoodRotSpeed SandboxOptions.EnumSandboxOption
---@field public FridgeFactor SandboxOptions.EnumSandboxOption
---@field public LootRespawn SandboxOptions.EnumSandboxOption
---@field public SeenHoursPreventLootRespawn SandboxOptions.IntegerSandboxOption
---@field public WorldItemRemovalList SandboxOptions.StringSandboxOption
---@field public HoursForWorldItemRemoval SandboxOptions.DoubleSandboxOption
---@field public ItemRemovalListBlacklistToggle SandboxOptions.BooleanSandboxOption
---@field public TimeSinceApo SandboxOptions.EnumSandboxOption
---@field public PlantResilience SandboxOptions.EnumSandboxOption
---@field public PlantAbundance SandboxOptions.EnumSandboxOption
---@field public EndRegen SandboxOptions.EnumSandboxOption
---@field public Helicopter SandboxOptions.EnumSandboxOption
---@field public MetaEvent SandboxOptions.EnumSandboxOption
---@field public SleepingEvent SandboxOptions.EnumSandboxOption
---@field public GeneratorFuelConsumption SandboxOptions.DoubleSandboxOption
---@field public GeneratorSpawning SandboxOptions.EnumSandboxOption
---@field public SurvivorHouseChance SandboxOptions.EnumSandboxOption
---@field public AnnotatedMapChance SandboxOptions.EnumSandboxOption
---@field public CharacterFreePoints SandboxOptions.IntegerSandboxOption
---@field public ConstructionBonusPoints SandboxOptions.EnumSandboxOption
---@field public NightDarkness SandboxOptions.EnumSandboxOption
---@field public BoneFracture SandboxOptions.BooleanSandboxOption
---@field public InjurySeverity SandboxOptions.EnumSandboxOption
---@field public HoursForCorpseRemoval SandboxOptions.DoubleSandboxOption
---@field public DecayingCorpseHealthImpact SandboxOptions.EnumSandboxOption
---@field public BloodLevel SandboxOptions.EnumSandboxOption
---@field public ClothingDegradation SandboxOptions.EnumSandboxOption
---@field public FireSpread SandboxOptions.BooleanSandboxOption
---@field public DaysForRottenFoodRemoval SandboxOptions.IntegerSandboxOption
---@field public AllowExteriorGenerator SandboxOptions.BooleanSandboxOption
---@field public MaxFogIntensity SandboxOptions.EnumSandboxOption
---@field public MaxRainFxIntensity SandboxOptions.EnumSandboxOption
---@field public EnableSnowOnGround SandboxOptions.BooleanSandboxOption
---@field public AttackBlockMovements SandboxOptions.BooleanSandboxOption
---@field public VehicleStoryChance SandboxOptions.EnumSandboxOption
---@field public ZoneStoryChance SandboxOptions.EnumSandboxOption
---@field public AllClothesUnlocked SandboxOptions.BooleanSandboxOption
---@field public EnableVehicles SandboxOptions.BooleanSandboxOption
---@field public CarSpawnRate SandboxOptions.EnumSandboxOption
---@field public ZombieAttractionMultiplier SandboxOptions.DoubleSandboxOption
---@field public VehicleEasyUse SandboxOptions.BooleanSandboxOption
---@field public InitialGas SandboxOptions.EnumSandboxOption
---@field public FuelStationGas SandboxOptions.EnumSandboxOption
---@field public LockedCar SandboxOptions.EnumSandboxOption
---@field public CarGasConsumption SandboxOptions.DoubleSandboxOption
---@field public CarGeneralCondition SandboxOptions.EnumSandboxOption
---@field public CarDamageOnImpact SandboxOptions.EnumSandboxOption
---@field public DamageToPlayerFromHitByACar SandboxOptions.EnumSandboxOption
---@field public TrafficJam SandboxOptions.BooleanSandboxOption
---@field public CarAlarm SandboxOptions.EnumSandboxOption
---@field public PlayerDamageFromCrash SandboxOptions.BooleanSandboxOption
---@field public SirenShutoffHours SandboxOptions.DoubleSandboxOption
---@field public ChanceHasGas SandboxOptions.EnumSandboxOption
---@field public RecentlySurvivorVehicles SandboxOptions.EnumSandboxOption
---@field public MultiHitZombies SandboxOptions.BooleanSandboxOption
---@field public RearVulnerability SandboxOptions.EnumSandboxOption
---@field protected options ArrayList|Unknown
---@field protected optionByName HashMap|Unknown|Unknown
---@field public Map SandboxOptions.Map
---@field public Lore SandboxOptions.ZombieLore
---@field public zombieConfig SandboxOptions.ZombieConfig
---@field public FIRST_YEAR int
---@field private SANDBOX_VERSION int
---@field private m_customOptions ArrayList|Unknown
SandboxOptions = {}

---@public
---@return void
---@overload fun(input:ByteBuffer)
function SandboxOptions:load() end

---throws java.io.IOException
---@public
---@param input ByteBuffer
---@return void
function SandboxOptions:load(input) end

---@public
---@return int
function SandboxOptions:getDayLengthMinutes() end

---@public
---@return void
function SandboxOptions:handleOldZombiesFile2() end

---throws java.io.IOException
---@public
---@param output ByteBuffer
---@return void
function SandboxOptions:save(output) end

---@public
---@return int
function SandboxOptions:getFirstYear() end

---@private
---@param arg0 String
---@return boolean
function SandboxOptions:readLuaFile(arg0) end

---@public
---@param arg0 CustomSandboxOption
---@return void
function SandboxOptions:newCustomOption(arg0) end

---@private
---@param arg0 String
---@param arg1 int
---@return String
function SandboxOptions:upgradeOptionName(arg0, arg1) end

---@public
---@return void
function SandboxOptions:handleOldZombiesFile1() end

---@public
---@return double
function SandboxOptions:getStatsDecreaseMultiplier() end

---@public
---@return SandboxOptions
function SandboxOptions:newCopy() end

---@public
---@return void
function SandboxOptions:Reset() end

---@private
---@param arg0 SandboxOptions.SandboxOption
---@param arg1 CustomSandboxOption
---@return void
function SandboxOptions:addCustomOption(arg0, arg1) end

---@private
---@param arg0 String
---@param arg1 int
---@param arg2 int
---@param arg3 int
---@return SandboxOptions.IntegerSandboxOption
function SandboxOptions:newIntegerOption(arg0, arg1, arg2, arg3) end

---@public
---@return int
function SandboxOptions:getFoodLootModifier() end

---@private
---@param arg0 String
---@param arg1 boolean
---@return SandboxOptions.BooleanSandboxOption
function SandboxOptions:newBooleanOption(arg0, arg1) end

---@private
---@return void
function SandboxOptions:removeCustomOptions() end

---@private
---@param arg0 String
---@param arg1 boolean
---@return boolean
function SandboxOptions:readTextFile(arg0, arg1) end

---@public
---@return int
function SandboxOptions:getElecShutModifier() end

---@public
---@param arg0 String
---@return boolean
function SandboxOptions:loadPresetFile(arg0) end

---@public
---@return int
function SandboxOptions:getRainModifier() end

---@public
---@param arg0 String
---@return boolean
function SandboxOptions:saveServerLuaFile(arg0) end

---@public
---@return int
function SandboxOptions:getWaterShutModifier() end

---@public
---@return int
function SandboxOptions:getOtherLootModifier() end

---@public
---@return void
function SandboxOptions:resetToDefault() end

---@public
---@return int
function SandboxOptions:getTemperatureModifier() end

---@public
---@param arg0 String
---@return boolean
function SandboxOptions:isValidPresetName(arg0) end

---@public
---@return int
function SandboxOptions:getTimeSinceApo() end

---@public
---@return void
function SandboxOptions:toLua() end

---@private
---@return void
function SandboxOptions:saveCurrentGameBinFile() end

---@public
---@param arg0 String
---@return boolean
function SandboxOptions:loadGameFile(arg0) end

---@public
---@return int
function SandboxOptions:getErosionSpeed() end

---@public
---@param arg0 String
---@return boolean
function SandboxOptions:loadServerLuaFile(arg0) end

---@public
---@return int
function SandboxOptions:getWeaponLootModifier() end

---@public
---@return double
function SandboxOptions:getEnduranceRegenMultiplier() end

---@public
---@return void
function SandboxOptions:handleOldServerZombiesFile() end

---@public
---@return void
function SandboxOptions:loadCurrentGameBinFile() end

---@public
---@return void
function SandboxOptions:initSandboxVars() end

---@private
---@param arg0 String
---@param arg1 int
---@param arg2 int
---@return SandboxOptions.EnumSandboxOption
function SandboxOptions:newEnumOption(arg0, arg1, arg2) end

---@private
---@param arg0 String
---@return String[]
function SandboxOptions:parseName(arg0) end

---@public
---@param arg0 String
---@return SandboxOptions.SandboxOption
function SandboxOptions:getOptionByName(arg0) end

---@private
---@param arg0 String
---@param arg1 int
---@return boolean
function SandboxOptions:writeTextFile(arg0, arg1) end

---@public
---@return void
function SandboxOptions:sendToServer() end

---@public
---@return boolean
function SandboxOptions:getAllClothesUnlocked() end

---@public
---@return int
function SandboxOptions:getCompostHours() end

---@public
---@return SandboxOptions
function SandboxOptions:getInstance() end

---Random the number of day for the selectricity shut off
---@public
---@param electricityShutoffModifier int
---@return int
function SandboxOptions:randomElectricityShut(electricityShutoffModifier) end

---@public
---@return void
function SandboxOptions:applySettings() end

---@private
---@param arg0 String
---@param arg1 String
---@param arg2 int
---@return String
function SandboxOptions:upgradeOptionValue(arg0, arg1, arg2) end

---@public
---@return int
function SandboxOptions:getNumOptions() end

---Random the number of day for the water shut off
---@public
---@param waterShutoffModifier int
---@return int
function SandboxOptions:randomWaterShut(waterShutoffModifier) end

---@public
---@param arg0 String
---@return boolean
function SandboxOptions:saveGameFile(arg0) end

---@private
---@param arg0 String
---@param arg1 KahluaTable
---@param arg2 int
---@return KahluaTable
function SandboxOptions:upgradeLuaTable(arg0, arg1, arg2) end

---@protected
---@param arg0 SandboxOptions.SandboxOption
---@return SandboxOptions
function SandboxOptions:addOption(arg0) end

---@private
---@param arg0 String
---@param arg1 double
---@param arg2 double
---@param arg3 double
---@return SandboxOptions.DoubleSandboxOption
function SandboxOptions:newDoubleOption(arg0, arg1, arg2, arg3) end

---@public
---@param arg0 String
---@return boolean
function SandboxOptions:loadServerZombiesFile(arg0) end

---@public
---@return void
function SandboxOptions:updateFromLua() end

---@public
---@param arg0 String
---@return boolean
function SandboxOptions:loadServerTextFile(arg0) end

---@public
---@param arg0 String
---@return boolean
function SandboxOptions:savePresetFile(arg0) end

---@public
---@param arg0 String
---@param arg1 Object
---@return void
function SandboxOptions:set(arg0, arg1) end

---@public
---@return void
function SandboxOptions:setDefaultsToCurrentValues() end

---@public
---@param arg0 int
---@return SandboxOptions.SandboxOption
function SandboxOptions:getOptionByIndex(arg0) end

---@public
---@param arg0 SandboxOptions
---@return void
function SandboxOptions:copyValuesFrom(arg0) end

---@private
---@param arg0 String
---@param arg1 String
---@return SandboxOptions.StringSandboxOption
function SandboxOptions:newStringOption(arg0, arg1) end

---@private
---@param arg0 String
---@param arg1 boolean
---@return boolean
function SandboxOptions:writeLuaFile(arg0, arg1) end

---@public
---@return int
function SandboxOptions:getDayLengthMinutesDefault() end

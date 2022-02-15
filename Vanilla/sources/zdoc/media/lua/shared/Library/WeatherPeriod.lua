---@class WeatherPeriod : zombie.iso.weather.WeatherPeriod
---@field public STAGE_START int
---@field public STAGE_SHOWERS int
---@field public STAGE_HEAVY_PRECIP int
---@field public STAGE_STORM int
---@field public STAGE_CLEARING int
---@field public STAGE_MODERATE int
---@field public STAGE_DRIZZLE int
---@field public STAGE_BLIZZARD int
---@field public STAGE_TROPICAL_STORM int
---@field public STAGE_INTERMEZZO int
---@field public STAGE_MODDED int
---@field public STAGE_KATEBOB_STORM int
---@field public STAGE_MAX int
---@field public FRONT_STRENGTH_THRESHOLD float
---@field private climateManager ClimateManager
---@field private frontCache ClimateManager.AirFront
---@field private startTime double
---@field private duration double
---@field private currentTime double
---@field private currentStage WeatherPeriod.WeatherStage
---@field private weatherStages ArrayList|Unknown
---@field private weatherStageIndex int
---@field private stagesPool Stack|Unknown
---@field private isRunning boolean
---@field private totalProgress float
---@field private stageProgress float
---@field private weatherNoise float
---@field private maxTemperatureInfluence float
---@field private temperatureInfluence float
---@field private currentStrength float
---@field private rainThreshold float
---@field private windAngleDirMod float
---@field private isThunderStorm boolean
---@field private isTropicalStorm boolean
---@field private isBlizzard boolean
---@field private precipitationFinal float
---@field private thunderStorm ThunderStorm
---@field private cloudColor ClimateColorInfo
---@field private cloudColorReddish ClimateColorInfo
---@field private cloudColorGreenish ClimateColorInfo
---@field private cloudColorBlueish ClimateColorInfo
---@field private cloudColorPurplish ClimateColorInfo
---@field private cloudColorTropical ClimateColorInfo
---@field private cloudColorBlizzard ClimateColorInfo
---@field private PRINT_STUFF boolean
---@field private kateBobStormProgress float
---@field private kateBobStormX int
---@field private kateBobStormY int
---@field private seededRandom Random
---@field private climateValues ClimateValues
---@field private isDummy boolean
---@field private hasStartedInit boolean
---@field private cache HashMap|Unknown|Unknown
WeatherPeriod = {}

---@public
---@param arg0 ByteBuffer
---@return void
function WeatherPeriod:readNetWeatherData(arg0) end

---@public
---@return float
function WeatherPeriod:getTotalProgress() end

---@public
---@return float
function WeatherPeriod:getTotalStrength() end

---@public
---@return float
function WeatherPeriod:getRainThreshold() end

---@private
---@param arg0 float
---@return float
---@overload fun(arg0:int)
---@overload fun(arg0:int, arg1:int)
---@overload fun(arg0:float, arg1:float)
function WeatherPeriod:RandNext(arg0) end

---@private
---@param arg0 int
---@return int
function WeatherPeriod:RandNext(arg0) end

---@private
---@param arg0 int
---@param arg1 int
---@return int
function WeatherPeriod:RandNext(arg0, arg1) end

---@private
---@param arg0 float
---@param arg1 float
---@return float
function WeatherPeriod:RandNext(arg0, arg1) end

---@public
---@return boolean
function WeatherPeriod:isThunderStorm() end

---@public
---@return double
function WeatherPeriod:getWeatherNoise() end

---@public
---@param arg0 float
---@return void
function WeatherPeriod:setKateBobStormProgress(arg0) end

---@public
---@return ClimateColorInfo
function WeatherPeriod:getCloudColorPurplish() end

---@public
---@return boolean
function WeatherPeriod:hasStorm() end

---@public
---@return boolean
function WeatherPeriod:isBlizzard() end

---@public
---@return boolean
function WeatherPeriod:isRunning() end

---@public
---@param arg0 int
---@param arg1 double
---@return WeatherPeriod.WeatherStage
---@overload fun(arg0:int, arg1:double, arg2:String)
function WeatherPeriod:createAndAddStage(arg0, arg1) end

---@private
---@param arg0 int
---@param arg1 double
---@param arg2 String
---@return WeatherPeriod.WeatherStage
function WeatherPeriod:createAndAddStage(arg0, arg1, arg2) end

---@public
---@param arg0 double
---@return void
function WeatherPeriod:update(arg0) end

---@public
---@return ClimateColorInfo
function WeatherPeriod:getCloudColorBlueish() end

---@public
---@param arg0 ClimateColorInfo
---@return void
function WeatherPeriod:setCloudColor(arg0) end

---@public
---@param arg0 ByteBuffer
---@return void
function WeatherPeriod:writeNetWeatherData(arg0) end

---@public
---@return ClimateColorInfo
function WeatherPeriod:getCloudColorBlizzard() end

---@public
---@param arg0 ClimateManager.AirFront
---@param arg1 double
---@return void
---@overload fun(arg0:ClimateManager.AirFront, arg1:double, arg2:int, arg3:float)
function WeatherPeriod:initSimulationDebug(arg0, arg1) end

---@public
---@param arg0 ClimateManager.AirFront
---@param arg1 double
---@param arg2 int
---@param arg3 float
---@return void
function WeatherPeriod:initSimulationDebug(arg0, arg1, arg2, arg3) end

---@public
---@return float
function WeatherPeriod:getStageProgress() end

---@public
---@return float
function WeatherPeriod:getCurrentStrength() end

---@private
---@return void
function WeatherPeriod:updateCurrentStage() end

---@public
---@return boolean
function WeatherPeriod:hasBlizzard() end

---@public
---@return ClimateColorInfo
function WeatherPeriod:getCloudColor() end

---@public
---@return int
function WeatherPeriod:getFrontType() end

---@public
---@return float
function WeatherPeriod:getPrecipitationFinal() end

---@public
---@return boolean
function WeatherPeriod:hasHeavyRain() end

---@public
---@return double
function WeatherPeriod:getDuration() end

---@public
---@param arg0 boolean
---@return void
function WeatherPeriod:setPrintStuff(arg0) end

---@public
---@param arg0 double
---@return WeatherPeriod.WeatherStage
function WeatherPeriod:getStageForWorldAge(arg0) end

---@public
---@return float
function WeatherPeriod:getMaxTemperatureInfluence() end

---@public
---@param arg0 boolean
---@return void
function WeatherPeriod:setDummy(arg0) end

---@private
---@return void
function WeatherPeriod:linkWeatherStages() end

---@public
---@return float
function WeatherPeriod:getWindAngleDegrees() end

---@public
---@return ClimateColorInfo
function WeatherPeriod:getCloudColorTropical() end

---@private
---@param arg0 int
---@param arg1 double
---@return WeatherPeriod.WeatherStage
---@overload fun(arg0:int, arg1:double, arg2:String)
function WeatherPeriod:createStage(arg0, arg1) end

---@private
---@param arg0 int
---@param arg1 double
---@param arg2 String
---@return WeatherPeriod.WeatherStage
function WeatherPeriod:createStage(arg0, arg1, arg2) end

---@public
---@return boolean
function WeatherPeriod:getPrintStuff() end

---@private
---@return boolean
function WeatherPeriod:endInit() end

---@public
---@return WeatherPeriod.WeatherStage
function WeatherPeriod:getCurrentStage() end

---@public
---@param arg0 DataOutputStream
---@return void
function WeatherPeriod:save(arg0) end

---@public
---@return void
function WeatherPeriod:stopWeatherPeriod() end

---@public
---@return ClimateColorInfo
function WeatherPeriod:getCloudColorGreenish() end

---@public
---@param arg0 boolean
---@param arg1 float
---@param arg2 float
---@return boolean
function WeatherPeriod:startCreateModdedPeriod(arg0, arg1, arg2) end

---@protected
---@param arg0 int
---@param arg1 int
---@param arg2 int
---@return void
function WeatherPeriod:reseed(arg0, arg1, arg2) end

---@public
---@param arg0 int
---@param arg1 int
---@return void
function WeatherPeriod:setKateBobStormCoords(arg0, arg1) end

---@public
---@return ClimateColorInfo
function WeatherPeriod:getCloudColorReddish() end

---@private
---@return void
function WeatherPeriod:resetClimateManagerOverrides() end

---@public
---@return boolean
function WeatherPeriod:endCreateModdedPeriod() end

---@public
---@return boolean
function WeatherPeriod:isTropicalStorm() end

---@protected
---@param arg0 ClimateManager.AirFront
---@param arg1 double
---@param arg2 int
---@param arg3 int
---@param arg4 int
---@return void
---@overload fun(arg0:ClimateManager.AirFront, arg1:double, arg2:int, arg3:int, arg4:int, arg5:int, arg6:float)
function WeatherPeriod:init(arg0, arg1, arg2, arg3, arg4) end

---@protected
---@param arg0 ClimateManager.AirFront
---@param arg1 double
---@param arg2 int
---@param arg3 int
---@param arg4 int
---@param arg5 int
---@param arg6 float
---@return void
function WeatherPeriod:init(arg0, arg1, arg2, arg3, arg4, arg5, arg6) end

---@public
---@return int
function WeatherPeriod:getCurrentStageID() end

---@public
---@return ClimateManager.AirFront
function WeatherPeriod:getFrontCache() end

---@private
---@return void
function WeatherPeriod:createWeatherPattern() end

---@public
---@param arg0 DataInputStream
---@param arg1 int
---@return void
function WeatherPeriod:load(arg0, arg1) end

---@private
---@param arg0 int
---@param arg1 float
---@return void
function WeatherPeriod:createSingleStage(arg0, arg1) end

---@private
---@param arg0 ClimateManager.AirFront
---@param arg1 double
---@return boolean
function WeatherPeriod:startInit(arg0, arg1) end

---@public
---@param arg0 String
---@param arg1 double
---@return WeatherPeriod.WeatherStage
function WeatherPeriod:createAndAddModdedStage(arg0, arg1) end

---@private
---@return void
function WeatherPeriod:clearCurrentWeatherStages() end

---@public
---@return ArrayList|Unknown
function WeatherPeriod:getWeatherStages() end

---@public
---@return boolean
function WeatherPeriod:hasTropical() end

---@private
---@param arg0 String
---@return void
function WeatherPeriod:print(arg0) end

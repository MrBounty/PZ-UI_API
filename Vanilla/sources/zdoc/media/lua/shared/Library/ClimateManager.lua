---@class ClimateManager : zombie.iso.weather.ClimateManager
---@field private DISABLE_SIMULATION boolean
---@field private DISABLE_FX_UPDATE boolean
---@field private DISABLE_WEATHER_GENERATION boolean
---@field public FRONT_COLD int
---@field public FRONT_STATIONARY int
---@field public FRONT_WARM int
---@field public MAX_WINDSPEED_KPH float
---@field public MAX_WINDSPEED_MPH float
---@field private season ErosionSeason
---@field private lastMinuteStamp long
---@field private modDataTable KahluaTable
---@field private airMass float
---@field private airMassDaily float
---@field private airMassTemperature float
---@field private baseTemperature float
---@field private snowFall float
---@field private snowStrength float
---@field private snowMeltStrength float
---@field private snowFracNow float
---@field canDoWinterSprites boolean
---@field private windPower float
---@field private weatherPeriod WeatherPeriod
---@field private thunderStorm ThunderStorm
---@field private simplexOffsetA double
---@field private simplexOffsetB double
---@field private simplexOffsetC double
---@field private simplexOffsetD double
---@field private dayDoFog boolean
---@field private dayFogStrength float
---@field private gt GameTime
---@field private worldAgeHours double
---@field private tickIsClimateTick boolean
---@field private tickIsDayChange boolean
---@field private lastHourStamp int
---@field private tickIsHourChange boolean
---@field private tickIsTenMins boolean
---@field private currentFront ClimateManager.AirFront
---@field private colDay ClimateColorInfo
---@field private colDusk ClimateColorInfo
---@field private colDawn ClimateColorInfo
---@field private colNight ClimateColorInfo
---@field private colNightNoMoon ClimateColorInfo
---@field private colNightMoon ClimateColorInfo
---@field private colTemp ClimateColorInfo
---@field private colFog ClimateColorInfo
---@field private colFogLegacy ClimateColorInfo
---@field private colFogNew ClimateColorInfo
---@field private fogTintStorm ClimateColorInfo
---@field private fogTintTropical ClimateColorInfo
---@field private instance ClimateManager
---@field public WINTER_IS_COMING boolean
---@field public THE_DESCENDING_FOG boolean
---@field public A_STORM_IS_COMING boolean
---@field private climateValues ClimateValues
---@field private climateForecaster ClimateForecaster
---@field private climateHistory ClimateHistory
---@field dayLightLagged float
---@field nightLagged float
---@field protected desaturation ClimateManager.ClimateFloat
---@field protected globalLightIntensity ClimateManager.ClimateFloat
---@field protected nightStrength ClimateManager.ClimateFloat
---@field protected precipitationIntensity ClimateManager.ClimateFloat
---@field protected temperature ClimateManager.ClimateFloat
---@field protected fogIntensity ClimateManager.ClimateFloat
---@field protected windIntensity ClimateManager.ClimateFloat
---@field protected windAngleIntensity ClimateManager.ClimateFloat
---@field protected cloudIntensity ClimateManager.ClimateFloat
---@field protected ambient ClimateManager.ClimateFloat
---@field protected viewDistance ClimateManager.ClimateFloat
---@field protected dayLightStrength ClimateManager.ClimateFloat
---@field protected humidity ClimateManager.ClimateFloat
---@field protected globalLight ClimateManager.ClimateColor
---@field protected colorNewFog ClimateManager.ClimateColor
---@field protected precipitationIsSnow ClimateManager.ClimateBool
---@field public FLOAT_DESATURATION int
---@field public FLOAT_GLOBAL_LIGHT_INTENSITY int
---@field public FLOAT_NIGHT_STRENGTH int
---@field public FLOAT_PRECIPITATION_INTENSITY int
---@field public FLOAT_TEMPERATURE int
---@field public FLOAT_FOG_INTENSITY int
---@field public FLOAT_WIND_INTENSITY int
---@field public FLOAT_WIND_ANGLE_INTENSITY int
---@field public FLOAT_CLOUD_INTENSITY int
---@field public FLOAT_AMBIENT int
---@field public FLOAT_VIEW_DISTANCE int
---@field public FLOAT_DAYLIGHT_STRENGTH int
---@field public FLOAT_HUMIDITY int
---@field public FLOAT_MAX int
---@field private climateFloats ClimateManager.ClimateFloat[]
---@field public COLOR_GLOBAL_LIGHT int
---@field public COLOR_NEW_FOG int
---@field public COLOR_MAX int
---@field private climateColors ClimateManager.ClimateColor[]
---@field public BOOL_IS_SNOW int
---@field public BOOL_MAX int
---@field private climateBooleans ClimateManager.ClimateBool[]
---@field public AVG_FAV_AIR_TEMPERATURE float
---@field private windNoiseOffset double
---@field private windNoiseBase double
---@field private windNoiseFinal double
---@field private windTickFinal double
---@field private colFlare ClimateColorInfo
---@field private flareLaunched boolean
---@field private flareIntensity SteppedUpdateFloat
---@field private flareIntens float
---@field private flareMaxLifeTime float
---@field private flareLifeTime float
---@field private nextRandomTargetIntens int
---@field fogLerpValue float
---@field private seasonColorDawn ClimateManager.SeasonColor
---@field private seasonColorDay ClimateManager.SeasonColor
---@field private seasonColorDusk ClimateManager.SeasonColor
---@field private previousDay ClimateManager.DayInfo
---@field private currentDay ClimateManager.DayInfo
---@field private nextDay ClimateManager.DayInfo
---@field public PacketUpdateClimateVars byte
---@field public PacketWeatherUpdate byte
---@field public PacketThunderEvent byte
---@field public PacketFlare byte
---@field public PacketAdminVarsUpdate byte
---@field public PacketRequestAdminVars byte
---@field public PacketClientChangedAdminVars byte
---@field public PacketClientChangedWeather byte
---@field private networkLerp float
---@field private networkUpdateStamp long
---@field private networkLerpTime float
---@field private networkLerpTimeBase float
---@field private networkAdjustVal float
---@field private networkPrint boolean
---@field private netInfo ClimateManager.ClimateNetInfo
---@field private climateValuesFronts ClimateValues
---@field private windAngles float[]
---@field private windAngleStr String[]
ClimateManager = {}

---@public
---@return double
function ClimateManager:getWorldAgeHours() end

---@public
---@param arg0 boolean
---@return void
function ClimateManager:setPrecipitationIsSnow(arg0) end

---@public
---@param arg0 int
---@return ClimateManager.ClimateColor
function ClimateManager:getClimateColor(arg0) end

---@public
---@param arg0 int
---@param arg1 int
---@param arg2 float
---@param arg3 float
---@param arg4 float
---@param arg5 float
---@param arg6 boolean
---@return void
function ClimateManager:setSeasonColorDawn(arg0, arg1, arg2, arg3, arg4, arg5, arg6) end

---@public
---@return float
function ClimateManager:getSeasonStrength() end

---@public
---@return void
function ClimateManager:stopWeatherAndThunder() end

---@protected
---@param arg0 ClimateManager.ClimateNetAuth
---@param arg1 byte
---@param arg2 UdpConnection
---@return void
function ClimateManager:transmitClimatePacket(arg0, arg1, arg2) end

---@public
---@param arg0 IsoGameCharacter
---@return float
---@overload fun(arg0:IsoGameCharacter, arg1:boolean)
function ClimateManager:getAirTemperatureForCharacter(arg0) end

---@public
---@param arg0 IsoGameCharacter
---@param arg1 boolean
---@return float
function ClimateManager:getAirTemperatureForCharacter(arg0, arg1) end

---@public
---@return float
function ClimateManager:getWindIntensity() end

---@public
---@return void
function ClimateManager:updateEveryTenMins() end

---@public
---@return float
function ClimateManager:getAirMassDaily() end

---@public
---@return double
function ClimateManager:getSimplexOffsetA() end

---@private
---@return void
function ClimateManager:initSeasonColors() end

---@public
---@param arg0 boolean
---@return void
function ClimateManager:setEnabledSimulation(arg0) end

---@protected
---@param arg0 ClimateManager.DayInfo
---@param arg1 int
---@param arg2 int
---@param arg3 int
---@param arg4 int
---@return void
function ClimateManager:setDayInfo(arg0, arg1, arg2, arg3, arg4) end

---@public
---@return void
function ClimateManager:update() end

---@public
---@return ClimateManager
function ClimateManager:getInstance() end

---@private
---@return void
function ClimateManager:serverReceiveClientChangeAdminVars() end

---@private
---@return void
function ClimateManager:updateOnTick() end

---@public
---@return void
---@overload fun(arg0:int)
function ClimateManager:execute_Simulation() end

---@public
---@param arg0 int
---@return void
function ClimateManager:execute_Simulation(arg0) end

---@public
---@return void
function ClimateManager:triggerWinterIsComingStorm() end

---@public
---@param arg0 IsoMetaGrid
---@return void
function ClimateManager:init(arg0) end

---@private
---@return void
function ClimateManager:updateWindTick() end

---@public
---@return float
function ClimateManager:getCloudIntensity() end

---@public
---@return float
function ClimateManager:getDayMeanTemperature() end

---@protected
---@param arg0 float
---@param arg1 float
---@param arg2 float
---@return float
---@overload fun(arg0:float, arg1:float, arg2:float, arg3:boolean)
function ClimateManager:getTimeLerp(arg0, arg1, arg2) end

---@protected
---@param arg0 float
---@param arg1 float
---@param arg2 float
---@param arg3 boolean
---@return float
function ClimateManager:getTimeLerp(arg0, arg1, arg2, arg3) end

---@public
---@param arg0 boolean
---@return void
function ClimateManager:setEnabledWeatherGeneration(arg0) end

---@public
---@return float
function ClimateManager:getWindAngleRadians() end

---@public
---@param arg0 int
---@return ClimateManager.ClimateBool
function ClimateManager:getClimateBool(arg0) end

---@public
---@param arg0 boolean
---@return void
function ClimateManager:setEnabledFxUpdate(arg0) end

---@public
---@return void
function ClimateManager:launchFlare() end

---@protected
---@param arg0 float
---@param arg1 float
---@param arg2 float
---@return float
---@overload fun(arg0:float, arg1:float, arg2:float, arg3:boolean)
function ClimateManager:getTimeLerpHours(arg0, arg1, arg2) end

---@protected
---@param arg0 float
---@param arg1 float
---@param arg2 float
---@param arg3 boolean
---@return float
function ClimateManager:getTimeLerpHours(arg0, arg1, arg2, arg3) end

---@public
---@return float
function ClimateManager:getSnowIntensity() end

---@public
---@param arg0 UdpConnection
---@return void
function ClimateManager:sendInitialState(arg0) end

---@private
---@return void
function ClimateManager:updateSnowOLD() end

---@public
---@return void
function ClimateManager:transmitRequestAdminVars() end

---@public
---@param arg0 float
---@return void
function ClimateManager:transmitServerTriggerStorm(arg0) end

---@public
---@param arg0 int
---@return ClimateManager.ClimateFloat
function ClimateManager:getClimateFloat(arg0) end

---@public
---@param arg0 float
---@return float
function ClimateManager:ToMph(arg0) end

---@public
---@param arg0 int
---@param arg1 int
---@param arg2 float
---@param arg3 float
---@param arg4 float
---@param arg5 float
---@param arg6 boolean
---@return void
function ClimateManager:setSeasonColorDusk(arg0, arg1, arg2, arg3, arg4, arg5, arg6) end

---@private
---@param arg0 int
---@param arg1 String
---@return ClimateManager.ClimateBool
function ClimateManager:initClimateBool(arg0, arg1) end

---@public
---@return float
function ClimateManager:getAmbient() end

---@public
---@return float
function ClimateManager:getWindspeedKph() end

---@protected
---@param arg0 int
---@return float
function ClimateManager:getRainTimeMultiplierMod(arg0) end

---@public
---@return ClimateManager.DayInfo
function ClimateManager:getCurrentDay() end

---@public
---@param arg0 float
---@return float
function ClimateManager:posToPosNegRange(arg0) end

---@public
---@param arg0 float
---@param arg1 boolean
---@return boolean
function ClimateManager:triggerCustomWeather(arg0, arg1) end

---@private
---@return void
function ClimateManager:updateViewDistance() end

---@public
---@return String
function ClimateManager:getSeasonName() end

---@public
---@param arg0 int
---@param arg1 int
---@param arg2 int
---@param arg3 ClimateManager.AirFront
---@return void
function ClimateManager:CalculateWeatherFrontStrength(arg0, arg1, arg2, arg3) end

---@public
---@param arg0 float
---@return void
function ClimateManager:setDayLightStrength(arg0) end

---@public
---@param arg0 IsoGridSquare
---@return float
---@overload fun(arg0:IsoGridSquare, arg1:BaseVehicle)
---@overload fun(arg0:IsoGridSquare, arg1:BaseVehicle, arg2:boolean)
function ClimateManager:getAirTemperatureForSquare(arg0) end

---@public
---@param arg0 IsoGridSquare
---@param arg1 BaseVehicle
---@return float
function ClimateManager:getAirTemperatureForSquare(arg0, arg1) end

---@public
---@param arg0 IsoGridSquare
---@param arg1 BaseVehicle
---@param arg2 boolean
---@return float
function ClimateManager:getAirTemperatureForSquare(arg0, arg1, arg2) end

---@public
---@param arg0 float
---@return void
function ClimateManager:transmitTriggerBlizzard(arg0) end

---@public
---@return boolean
function ClimateManager:isRaining() end

---@public
---@param arg0 IsoGameCharacter
---@param arg1 float
---@return float
function ClimateManager:getWindForceMovement(arg0, arg1) end

---@private
---@param arg0 int
---@param arg1 int
---@param arg2 int
---@return void
function ClimateManager:updateDayInfo(arg0, arg1, arg2) end

---@public
---@return float
function ClimateManager:getWeatherInterference() end

---@public
---@return void
function ClimateManager:forceDayInfoUpdate() end

---@public
---@return float
function ClimateManager:getAirMassTemperature() end

---@public
---@param arg0 float
---@return void
function ClimateManager:setNightStrength(arg0) end

---@public
---@return boolean
function ClimateManager:getEnabledFxUpdate() end

---@public
---@return float
function ClimateManager:getSeasonProgression() end

---@public
---@return ClimateColorInfo
function ClimateManager:getColNightMoon() end

---@public
---@return double
function ClimateManager:getWindTickFinal() end

---@public
---@param arg0 DataInputStream
---@param arg1 int
---@return void
function ClimateManager:load(arg0, arg1) end

---@public
---@param arg0 float
---@param arg1 float
---@param arg2 float
---@return float
function ClimateManager:lerp(arg0, arg1, arg2) end

---@public
---@return ClimateValues
function ClimateManager:getClimateValuesCopy() end

---@public
---@param arg0 float
---@return String
function ClimateManager:getWindAngleString(arg0) end

---@public
---@param arg0 ClimateValues
---@return void
function ClimateManager:CopyClimateValues(arg0) end

---@public
---@return ClimateColorInfo
function ClimateManager:getColFogNew() end

---@public
---@return void
function ClimateManager:transmitServerStopWeather() end

---@public
---@param arg0 float
---@return float
function ClimateManager:clamp01(arg0) end

---@public
---@return float
function ClimateManager:getMaxWindspeedKph() end

---@public
---@return float
function ClimateManager:getBaseTemperature() end

---@private
---@return void
function ClimateManager:updateTestFlare() end

---@public
---@return ErosionSeason
function ClimateManager:getSeason() end

---@public
---@param arg0 float
---@return float
function ClimateManager:ToKph(arg0) end

---@public
---@return int
function ClimateManager:getBoolMax() end

---@public
---@param arg0 float
---@param arg1 float
---@param arg2 float
---@return float
function ClimateManager:clerp(arg0, arg1, arg2) end

---@public
---@return int
function ClimateManager:getFloatMax() end

---@public
---@param arg0 float
---@return void
function ClimateManager:transmitTriggerStorm(arg0) end

---@protected
---@param arg0 int
---@return double
function ClimateManager:getAirMassNoiseFrequencyMod(arg0) end

---@public
---@return double
function ClimateManager:getSimplexOffsetD() end

---@public
---@return boolean
function ClimateManager:getIsThunderStorming() end

---@public
---@param arg0 float
---@return void
function ClimateManager:setAmbient(arg0) end

---@public
---@return void
function ClimateManager:resetModded() end

---@private
---@param arg0 int
---@param arg1 String
---@return ClimateManager.ClimateFloat
function ClimateManager:initClimateFloat(arg0, arg1) end

---@public
---@return ClimateColorInfo
function ClimateManager:getColFog() end

---@public
---@return float
function ClimateManager:getNightStrength() end

---@public
---@return float
function ClimateManager:getAirMass() end

---@public
---@param arg0 ByteBuffer
---@param arg1 UdpConnection
---@return void
function ClimateManager:receiveClimatePacket(arg0, arg1) end

---@public
---@return float
function ClimateManager:getWindSpeedMovement() end

---@public
---@return ClimateColorInfo
function ClimateManager:getFogTintStorm() end

---@private
---@param arg0 ByteBuffer
---@param arg1 byte
---@param arg2 UdpConnection
---@return boolean
function ClimateManager:readPacketContents(arg0, arg1, arg2) end

---@public
---@return boolean
function ClimateManager:getEnabledWeatherGeneration() end

---@public
---@return void
function ClimateManager:updateOLD() end

---@public
---@return double
function ClimateManager:getSimplexOffsetB() end

---@public
---@return float
function ClimateManager:getDayLightStrength() end

---@public
---@return float
function ClimateManager:getPrecipitationIntensity() end

---@private
---@return void
function ClimateManager:updateFx() end

---@public
---@return double
function ClimateManager:getWindNoiseBase() end

---@public
---@return float
function ClimateManager:getHumidity() end

---@public
---@return float
function ClimateManager:getCorrectedWindAngleIntensity() end

---@public
---@return int
function ClimateManager:getColorMax() end

---@public
---@return ClimateColorInfo
function ClimateManager:getColorNewFog() end

---@public
---@param arg0 float
---@return void
function ClimateManager:transmitTriggerTropical(arg0) end

---@private
---@return void
function ClimateManager:serverReceiveClientChangeWeather() end

---@public
---@return ClimateColorInfo
function ClimateManager:getGlobalLight() end

---@public
---@param arg0 float
---@param arg1 float
---@return float
function ClimateManager:normalizeRange(arg0, arg1) end

---@public
---@return boolean
function ClimateManager:getPrecipitationIsSnow() end

---@public
---@return ClimateColorInfo
function ClimateManager:getColNightNoMoon() end

---@private
---@return void
function ClimateManager:setup() end

---@public
---@return boolean
function ClimateManager:isSnowing() end

---@public
---@param arg0 int
---@param arg1 int
---@param arg2 int
---@return ClimateColorInfo
function ClimateManager:getSeasonColor(arg0, arg1, arg2) end

---@public
---@param arg0 int
---@param arg1 int
---@param arg2 int
---@return int
---@overload fun(arg0:float, arg1:float, arg2:float)
function ClimateManager:clamp(arg0, arg1, arg2) end

---@public
---@param arg0 float
---@param arg1 float
---@param arg2 float
---@return float
function ClimateManager:clamp(arg0, arg1, arg2) end

---@public
---@return ClimateForecaster
function ClimateManager:getClimateForecaster() end

---@public
---@return float
function ClimateManager:getSnowStrength() end

---@public
---@param arg0 DataOutputStream
---@return void
function ClimateManager:save(arg0) end

---@public
---@param arg0 int
---@param arg1 int
---@param arg2 double
---@param arg3 float
---@param arg4 float
---@param arg5 float
---@param arg6 float
---@return void
---@overload fun(arg0:int, arg1:int, arg2:double, arg3:float, arg4:float, arg5:float, arg6:float, arg7:ClimateColorInfo)
function ClimateManager:triggerKateBobIntroStorm(arg0, arg1, arg2, arg3, arg4, arg5, arg6) end

---@public
---@param arg0 int
---@param arg1 int
---@param arg2 double
---@param arg3 float
---@param arg4 float
---@param arg5 float
---@param arg6 float
---@param arg7 ClimateColorInfo
---@return void
function ClimateManager:triggerKateBobIntroStorm(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7) end

---@public
---@return float
function ClimateManager:getRainIntensity() end

---@public
---@return ClimateManager.DayInfo
function ClimateManager:getPreviousDay() end

---@public
---@return void
function ClimateManager:resetAdmin() end

---@public
---@param arg0 int
---@param arg1 float
---@return boolean
function ClimateManager:triggerCustomWeatherStage(arg0, arg1) end

---@public
---@return void
function ClimateManager:transmitServerStopRain() end

---@public
---@return ThunderStorm
function ClimateManager:getThunderStorm() end

---@public
---@return float
function ClimateManager:getSnowFracNow() end

---@private
---@param arg0 int
---@param arg1 String
---@return ClimateManager.ClimateColor
function ClimateManager:initClimateColor(arg0, arg1) end

---@public
---@param arg0 float
---@param arg1 int
---@return void
function ClimateManager:transmitGenerateWeather(arg0, arg1) end

---@public
---@return float
function ClimateManager:getDesaturation() end

---@public
---@return float
function ClimateManager:getFogIntensity() end

---@public
---@return WeatherPeriod
function ClimateManager:getWeatherPeriod() end

---@public
---@return ClimateColorInfo
function ClimateManager:getColNight() end

---@private
---@return void
function ClimateManager:updateSnow() end

---@public
---@return void
function ClimateManager:transmitStopWeather() end

---@public
---@return Color
function ClimateManager:getGlobalLightInternal() end

---@public
---@return boolean
function ClimateManager:getEnabledSimulation() end

---@public
---@return void
function ClimateManager:postCellLoadSetSnow() end

---@public
---@return ClimateColorInfo
function ClimateManager:getFogTintTropical() end

---@public
---@return float
function ClimateManager:getWindAngleIntensity() end

---@public
---@return float
function ClimateManager:getFrontStrength() end

---@public
---@param arg0 float
---@return void
function ClimateManager:transmitServerStartRain(arg0) end

---@public
---@return double
function ClimateManager:getWindNoiseFinal() end

---@public
---@return ClimateManager.DayInfo
function ClimateManager:getNextDay() end

---@public
---@return float
function ClimateManager:getGlobalLightIntensity() end

---@public
---@param arg0 int
---@param arg1 int
---@param arg2 float
---@param arg3 float
---@param arg4 float
---@param arg5 float
---@param arg6 boolean
---@return void
function ClimateManager:setSeasonColorDay(arg0, arg1, arg2, arg3, arg4, arg5, arg6) end

---@public
---@return double
function ClimateManager:getSimplexOffsetC() end

---@public
---@return float
function ClimateManager:getTemperature() end

---@public
---@return float
function ClimateManager:getWindPower() end

---@public
---@param arg0 float
---@return void
function ClimateManager:setDesaturation(arg0) end

---@public
---@param arg0 float
---@return void
function ClimateManager:setViewDistance(arg0) end

---@private
---@param arg0 UdpConnection
---@param arg1 byte
---@return boolean
function ClimateManager:writePacketContents(arg0, arg1) end

---@public
---@return float
function ClimateManager:getMaxWindspeedMph() end

---@public
---@return void
function ClimateManager:transmitClientChangeAdminVars() end

---@public
---@return float
function ClimateManager:getViewDistance() end

---@public
---@return void
function ClimateManager:resetOverrides() end

---@private
---@return void
function ClimateManager:updateValues() end

---@public
---@return ClimateColorInfo
function ClimateManager:getColFogLegacy() end

---@public
---@param arg0 ClimateManager
---@return void
function ClimateManager:setInstance(arg0) end

---@public
---@return KahluaTable
function ClimateManager:getModData() end

---@public
---@return float
function ClimateManager:getWindAngleDegrees() end

---@public
---@return ClimateHistory
function ClimateManager:getClimateHistory() end

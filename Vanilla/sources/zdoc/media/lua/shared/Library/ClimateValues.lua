---@class ClimateValues : zombie.iso.weather.ClimateValues
---@field private simplexOffsetA double
---@field private simplexOffsetB double
---@field private simplexOffsetC double
---@field private simplexOffsetD double
---@field private clim ClimateManager
---@field private gt GameTime
---@field private time float
---@field private dawn float
---@field private dusk float
---@field private noon float
---@field private dayMeanTemperature float
---@field private airMassNoiseFrequencyMod double
---@field private noiseAirmass float
---@field private airMassTemperature float
---@field private baseTemperature float
---@field private dayLightLagged float
---@field private nightLagged float
---@field private temperature float
---@field private temperatureIsSnow boolean
---@field private humidity float
---@field private windIntensity float
---@field private windAngleIntensity float
---@field private windAngleDegrees float
---@field private nightStrength float
---@field private dayLightStrength float
---@field private ambient float
---@field private desaturation float
---@field private dayLightStrengthBase float
---@field private lerpNight float
---@field private cloudyT float
---@field private cloudIntensity float
---@field private airFrontAirmass float
---@field private dayDoFog boolean
---@field private dayFogStrength float
---@field private dayFogDuration float
---@field private testCurrentDay ClimateManager.DayInfo
---@field private testNextDay ClimateManager.DayInfo
---@field private cacheWorldAgeHours double
---@field private cacheYear int
---@field private cacheMonth int
---@field private cacheDay int
---@field private seededRandom Random
ClimateValues = {}

---@public
---@return int
function ClimateValues:getCacheDay() end

---@public
---@return float
function ClimateValues:getNoiseAirmass() end

---@public
---@return float
function ClimateValues:getAirMassTemperature() end

---@public
---@return float
function ClimateValues:getWindIntensity() end

---@public
---@return float
function ClimateValues:getDayFogStrength() end

---@public
---@return float
function ClimateValues:getWindAngleIntensity() end

---@public
---@return float
function ClimateValues:getWindAngleDegrees() end

---@public
---@return float
function ClimateValues:getCloudyT() end

---@public
---@return float
function ClimateValues:getDayLightLagged() end

---@public
---@return float
function ClimateValues:getDayFogDuration() end

---@public
---@return float
function ClimateValues:getNightStrength() end

---@public
---@return boolean
function ClimateValues:isDayDoFog() end

---@public
---@return float
function ClimateValues:getLerpNight() end

---@public
---@return float
function ClimateValues:getTemperature() end

---@public
---@return float
function ClimateValues:getDusk() end

---@public
---@return float
function ClimateValues:getDayLightStrengthBase() end

---@public
---@return int
function ClimateValues:getCacheYear() end

---@public
---@param arg0 GregorianCalendar
---@return void
---@overload fun(arg0:int, arg1:int, arg2:int)
---@overload fun(arg0:int, arg1:int, arg2:int, arg3:int)
---@overload fun(arg0:int, arg1:int, arg2:int, arg3:int, arg4:int)
function ClimateValues:pollDate(arg0) end

---@public
---@param arg0 int
---@param arg1 int
---@param arg2 int
---@return void
function ClimateValues:pollDate(arg0, arg1, arg2) end

---@public
---@param arg0 int
---@param arg1 int
---@param arg2 int
---@param arg3 int
---@return void
function ClimateValues:pollDate(arg0, arg1, arg2, arg3) end

---@public
---@param arg0 int
---@param arg1 int
---@param arg2 int
---@param arg3 int
---@param arg4 int
---@return void
function ClimateValues:pollDate(arg0, arg1, arg2, arg3, arg4) end

---@public
---@return float
function ClimateValues:getDesaturation() end

---@public
---@return float
function ClimateValues:getNoon() end

---@public
---@return double
function ClimateValues:getAirMassNoiseFrequencyMod() end

---@public
---@return boolean
function ClimateValues:isTemperatureIsSnow() end

---@public
---@return float
function ClimateValues:getDayLightStrength() end

---@public
---@param arg0 ClimateValues
---@return void
function ClimateValues:CopyValues(arg0) end

---@public
---@return double
function ClimateValues:getCacheWorldAgeHours() end

---@public
---@return float
function ClimateValues:getBaseTemperature() end

---@public
---@return float
function ClimateValues:getHumidity() end

---@public
---@return float
function ClimateValues:getCloudIntensity() end

---@public
---@return float
function ClimateValues:getAmbient() end

---@public
---@return int
function ClimateValues:getCacheMonth() end

---@protected
---@param arg0 double
---@param arg1 float
---@param arg2 ClimateManager.DayInfo
---@param arg3 ClimateManager.DayInfo
---@return void
function ClimateValues:updateValues(arg0, arg1, arg2, arg3) end

---@public
---@return float
function ClimateValues:getDawn() end

---@public
---@return void
function ClimateValues:print() end

---@public
---@return float
function ClimateValues:getDayMeanTemperature() end

---@public
---@return float
function ClimateValues:getTime() end

---@public
---@return float
function ClimateValues:getAirFrontAirmass() end

---@public
---@return ClimateValues
function ClimateValues:getCopy() end

---@public
---@return float
function ClimateValues:getNightLagged() end

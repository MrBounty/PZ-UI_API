---@class ClimateForecaster.DayForecast : zombie.iso.weather.ClimateForecaster.DayForecast
---@field private indexOffset int
---@field private name String
---@field private weatherPeriod WeatherPeriod
---@field private temperature ClimateForecaster.ForecastValue
---@field private humidity ClimateForecaster.ForecastValue
---@field private windDirection ClimateForecaster.ForecastValue
---@field private windPower ClimateForecaster.ForecastValue
---@field private cloudiness ClimateForecaster.ForecastValue
---@field private weatherStarts boolean
---@field private weatherStartTime float
---@field private weatherEndTime float
---@field private chanceOnSnow boolean
---@field private airFrontString String
---@field private hasFog boolean
---@field private fogStrength float
---@field private fogDuration float
---@field private airFront ClimateManager.AirFront
---@field private weatherOverlap ClimateForecaster.DayForecast
---@field private hasHeavyRain boolean
---@field private hasStorm boolean
---@field private hasTropicalStorm boolean
---@field private hasBlizzard boolean
---@field private dawn float
---@field private dusk float
---@field private dayLightHours float
---@field private weatherStages ArrayList|Unknown
ClimateForecaster_DayForecast = {}

---@public
---@return ClimateManager.AirFront
function ClimateForecaster_DayForecast:getAirFront() end

---@public
---@return ClimateForecaster.ForecastValue
function ClimateForecaster_DayForecast:getCloudiness() end

---@public
---@return boolean
function ClimateForecaster_DayForecast:isHasTropicalStorm() end

---@public
---@return float
function ClimateForecaster_DayForecast:getDawn() end

---@public
---@return String
function ClimateForecaster_DayForecast:getAirFrontString() end

---@public
---@return boolean
function ClimateForecaster_DayForecast:isHasStorm() end

---@public
---@return ClimateForecaster.ForecastValue
function ClimateForecaster_DayForecast:getWindDirection() end

---@public
---@return ClimateForecaster.ForecastValue
function ClimateForecaster_DayForecast:getHumidity() end

---@public
---@return float
function ClimateForecaster_DayForecast:getWeatherEndTime() end

---@public
---@return String
function ClimateForecaster_DayForecast:getMeanWindAngleString() end

---@public
---@return float
function ClimateForecaster_DayForecast:getWeatherStartTime() end

---@public
---@return float
function ClimateForecaster_DayForecast:getDusk() end

---@public
---@return float
function ClimateForecaster_DayForecast:getFogStrength() end

---@public
---@return ArrayList|Unknown
function ClimateForecaster_DayForecast:getWeatherStages() end

---@public
---@return boolean
function ClimateForecaster_DayForecast:isChanceOnSnow() end

---@public
---@return ClimateForecaster.ForecastValue
function ClimateForecaster_DayForecast:getWindPower() end

---@public
---@return boolean
function ClimateForecaster_DayForecast:isHasFog() end

---@public
---@return float
function ClimateForecaster_DayForecast:getFogDuration() end

---@public
---@return WeatherPeriod
function ClimateForecaster_DayForecast:getWeatherPeriod() end

---@public
---@return boolean
function ClimateForecaster_DayForecast:isHasBlizzard() end

---@public
---@return boolean
function ClimateForecaster_DayForecast:isWeatherStarts() end

---@public
---@return ClimateForecaster.ForecastValue
function ClimateForecaster_DayForecast:getTemperature() end

---@public
---@return String
function ClimateForecaster_DayForecast:getName() end

---@public
---@return int
function ClimateForecaster_DayForecast:getIndexOffset() end

---@public
---@return boolean
function ClimateForecaster_DayForecast:isHasHeavyRain() end

---@public
---@return ClimateForecaster.DayForecast
function ClimateForecaster_DayForecast:getWeatherOverlap() end

---@private
---@return void
function ClimateForecaster_DayForecast:reset() end

---@public
---@return float
function ClimateForecaster_DayForecast:getDayLightHours() end

---@class ClimateForecaster : zombie.iso.weather.ClimateForecaster
---@field private OffsetToday int
---@field private climateValues ClimateValues
---@field private forecasts ClimateForecaster.DayForecast[]
---@field private forecastList ArrayList|Unknown
ClimateForecaster = {}

---@public
---@return ClimateForecaster.DayForecast
---@overload fun(arg0:int)
function ClimateForecaster:getForecast() end

---@public
---@param arg0 int
---@return ClimateForecaster.DayForecast
function ClimateForecaster:getForecast(arg0) end

---@public
---@return ArrayList|Unknown
function ClimateForecaster:getForecasts() end

---@protected
---@param arg0 ClimateManager
---@return void
function ClimateForecaster:init(arg0) end

---@protected
---@param arg0 ClimateManager
---@param arg1 ClimateForecaster.DayForecast
---@param arg2 int
---@return void
function ClimateForecaster:sampleDay(arg0, arg1, arg2) end

---@public
---@return int
function ClimateForecaster:getDaysTillFirstWeather() end

---@protected
---@param arg0 ClimateManager
---@return void
function ClimateForecaster:updateDayChange(arg0) end

---@private
---@param arg0 int
---@param arg1 float
---@return ClimateForecaster.DayForecast
function ClimateForecaster:getWeatherOverlap(arg0, arg1) end

---@private
---@return void
function ClimateForecaster:populateForecastList() end

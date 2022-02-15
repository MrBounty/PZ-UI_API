---@class WeatherPeriod.StrLerpVal : zombie.iso.weather.WeatherPeriod.StrLerpVal
---@field public Entry WeatherPeriod.StrLerpVal
---@field public Target WeatherPeriod.StrLerpVal
---@field public NextTarget WeatherPeriod.StrLerpVal
---@field public None WeatherPeriod.StrLerpVal
---@field private value int
WeatherPeriod_StrLerpVal = {}

---@public
---@param arg0 int
---@return WeatherPeriod.StrLerpVal
function WeatherPeriod_StrLerpVal:fromValue(arg0) end

---@public
---@param arg0 String
---@return WeatherPeriod.StrLerpVal
function WeatherPeriod_StrLerpVal:valueOf(arg0) end

---@public
---@return int
function WeatherPeriod_StrLerpVal:getValue() end

---@public
---@return WeatherPeriod.StrLerpVal[]
function WeatherPeriod_StrLerpVal:values() end

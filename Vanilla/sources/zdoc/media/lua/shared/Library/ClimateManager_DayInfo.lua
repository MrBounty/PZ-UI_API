---@class ClimateManager.DayInfo : zombie.iso.weather.ClimateManager.DayInfo
---@field public day int
---@field public month int
---@field public year int
---@field public hour int
---@field public minutes int
---@field public dateValue long
---@field public calendar GregorianCalendar
---@field public season ErosionSeason
ClimateManager_DayInfo = {}

---@public
---@return int
function ClimateManager_DayInfo:getDay() end

---@public
---@param arg0 int
---@param arg1 int
---@param arg2 int
---@return void
function ClimateManager_DayInfo:set(arg0, arg1, arg2) end

---@public
---@return int
function ClimateManager_DayInfo:getYear() end

---@public
---@return int
function ClimateManager_DayInfo:getHour() end

---@public
---@return int
function ClimateManager_DayInfo:getMinutes() end

---@public
---@return int
function ClimateManager_DayInfo:getMonth() end

---@public
---@return long
function ClimateManager_DayInfo:getDateValue() end

---@public
---@return ErosionSeason
function ClimateManager_DayInfo:getSeason() end

---@class ClimateMoon : zombie.iso.weather.ClimateMoon
---@field private day_year int[]
---@field private moon_phase_name String[]
---@field private units float[]
---@field private last_year int
---@field private last_month int
---@field private last_day int
---@field private current_phase int
---@field private current_float float
---@field private instance ClimateMoon
ClimateMoon = {}

---@private
---@param arg0 int
---@param arg1 int
---@return int
function ClimateMoon:daysInMonth(arg0, arg1) end

---@public
---@return String
function ClimateMoon:getPhaseName() end

---@public
---@param arg0 int
---@param arg1 int
---@param arg2 int
---@return void
function ClimateMoon:updatePhase(arg0, arg1, arg2) end

---@public
---@return ClimateMoon
function ClimateMoon:getInstance() end

---@private
---@param arg0 int
---@return boolean
function ClimateMoon:isLeapYearP(arg0) end

---@private
---@param arg0 int
---@param arg1 int
---@param arg2 int
---@return int
function ClimateMoon:getMoonPhase(arg0, arg1, arg2) end

---@public
---@return float
function ClimateMoon:getMoonFloat() end

---@public
---@return int
function ClimateMoon:getCurrentMoonPhase() end

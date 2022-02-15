---@class PZCalendar : zombie.util.PZCalendar
---@field private calendar Calendar
PZCalendar = {}

---@public
---@return PZCalendar
function PZCalendar:getInstance() end

---@public
---@return long
function PZCalendar:getTimeInMillis() end

---@public
---@param arg0 int
---@param arg1 int
---@param arg2 int
---@param arg3 int
---@param arg4 int
---@return void
function PZCalendar:set(arg0, arg1, arg2, arg3, arg4) end

---@public
---@param arg0 int
---@return boolean
function PZCalendar:isLeapYear(arg0) end

---@public
---@return Date
function PZCalendar:getTime() end

---@public
---@param arg0 int
---@return int
function PZCalendar:get(arg0) end

---@public
---@param arg0 long
---@return void
function PZCalendar:setTimeInMillis(arg0) end

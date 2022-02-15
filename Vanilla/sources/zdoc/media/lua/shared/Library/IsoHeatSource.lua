---@class IsoHeatSource : zombie.iso.IsoHeatSource
---@field private x int
---@field private y int
---@field private z int
---@field private radius int
---@field private temperature int
IsoHeatSource = {}

---@public
---@return int
function IsoHeatSource:getZ() end

---@public
---@return int
function IsoHeatSource:getRadius() end

---@public
---@return boolean
---@overload fun(arg0:int, arg1:int, arg2:int, arg3:int)
function IsoHeatSource:isInBounds() end

---@public
---@param arg0 int
---@param arg1 int
---@param arg2 int
---@param arg3 int
---@return boolean
function IsoHeatSource:isInBounds(arg0, arg1, arg2, arg3) end

---@public
---@return int
function IsoHeatSource:getX() end

---@public
---@return int
function IsoHeatSource:getY() end

---@public
---@param arg0 int
---@return void
function IsoHeatSource:setRadius(arg0) end

---@public
---@return int
function IsoHeatSource:getTemperature() end

---@public
---@param arg0 int
---@return void
function IsoHeatSource:setTemperature(arg0) end

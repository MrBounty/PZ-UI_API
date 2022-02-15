---@class DataPoint : zombie.radio.StorySounds.DataPoint
---@field protected time float
---@field protected intensity float
DataPoint = {}

---@public
---@return float
function DataPoint:getTime() end

---@public
---@return float
function DataPoint:getIntensity() end

---@public
---@param arg0 float
---@return void
function DataPoint:setIntensity(arg0) end

---@public
---@param arg0 float
---@return void
function DataPoint:setTime(arg0) end

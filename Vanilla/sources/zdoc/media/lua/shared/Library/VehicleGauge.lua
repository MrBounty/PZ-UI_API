---@class VehicleGauge : zombie.ui.VehicleGauge
---@field protected needleX int
---@field protected needleY int
---@field protected minAngle float
---@field protected maxAngle float
---@field protected value float
---@field protected texture Texture
---@field protected needleWidth int
VehicleGauge = {}

---@public
---@param arg0 float
---@return void
function VehicleGauge:setValue(arg0) end

---@public
---@return void
function VehicleGauge:render() end

---@public
---@param arg0 Texture
---@return void
function VehicleGauge:setTexture(arg0) end

---@public
---@param arg0 int
---@return void
function VehicleGauge:setNeedleWidth(arg0) end

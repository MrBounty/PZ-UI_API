---@class VehicleLight : zombie.vehicles.VehicleLight
---@field public active boolean
---@field public offset Vector3f
---@field public dist float
---@field public intensity float
---@field public dot float
---@field public focusing int
VehicleLight = {}

---@public
---@param arg0 ByteBuffer
---@param arg1 int
---@return void
function VehicleLight:load(arg0, arg1) end

---@public
---@param arg0 ByteBuffer
---@return void
function VehicleLight:save(arg0) end

---@public
---@param arg0 boolean
---@return void
function VehicleLight:setActive(arg0) end

---@public
---@return boolean
function VehicleLight:canFocusingDown() end

---@public
---@return int
function VehicleLight:getFocusing() end

---@public
---@return void
function VehicleLight:setFocusingUp() end

---@public
---@return float
function VehicleLight:getIntensity() end

---@public
---@return void
function VehicleLight:setFocusingDown() end

---@public
---@return float
function VehicleLight:getDistanization() end

---@public
---@return boolean
function VehicleLight:canFocusingUp() end

---@public
---@return boolean
function VehicleLight:getActive() end

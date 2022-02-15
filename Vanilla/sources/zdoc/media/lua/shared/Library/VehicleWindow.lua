---@class VehicleWindow : zombie.vehicles.VehicleWindow
---@field protected part VehiclePart
---@field protected health int
---@field protected openable boolean
---@field protected open boolean
---@field protected openDelta float
VehicleWindow = {}

---@public
---@return int
function VehicleWindow:getHealth() end

---@public
---@return boolean
function VehicleWindow:isOpen() end

---@public
---@param arg0 boolean
---@return void
function VehicleWindow:setOpen(arg0) end

---@public
---@param arg0 IsoGameCharacter
---@return void
function VehicleWindow:hit(arg0) end

---@public
---@return boolean
function VehicleWindow:isHittable() end

---@public
---@param arg0 VehicleScript.Window
---@return void
function VehicleWindow:init(arg0) end

---@public
---@return boolean
function VehicleWindow:isOpenable() end

---@public
---@param arg0 float
---@return void
function VehicleWindow:setOpenDelta(arg0) end

---@public
---@return float
function VehicleWindow:getOpenDelta() end

---@public
---@param arg0 ByteBuffer
---@return void
function VehicleWindow:save(arg0) end

---@public
---@param arg0 ByteBuffer
---@param arg1 int
---@return void
function VehicleWindow:load(arg0, arg1) end

---@public
---@param arg0 int
---@return void
function VehicleWindow:setHealth(arg0) end

---@public
---@return boolean
function VehicleWindow:isDestroyed() end

---@public
---@param arg0 int
---@return void
function VehicleWindow:damage(arg0) end

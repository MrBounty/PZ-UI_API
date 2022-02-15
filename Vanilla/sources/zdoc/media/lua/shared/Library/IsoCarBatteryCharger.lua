---@class IsoCarBatteryCharger : zombie.iso.objects.IsoCarBatteryCharger
---@field protected item InventoryItem
---@field protected battery InventoryItem
---@field protected activated boolean
---@field protected lastUpdate float
---@field protected chargeRate float
---@field protected chargerSprite IsoSprite
---@field protected batterySprite IsoSprite
---@field protected sound long
IsoCarBatteryCharger = {}

---@public
---@param arg0 ByteBuffer
---@param arg1 int
---@param arg2 boolean
---@return void
function IsoCarBatteryCharger:load(arg0, arg1, arg2) end

---@public
---@return String
function IsoCarBatteryCharger:getObjectName() end

---@public
---@param arg0 boolean
---@return void
function IsoCarBatteryCharger:setActivated(arg0) end

---@public
---@param arg0 ByteBufferWriter
---@return void
function IsoCarBatteryCharger:syncIsoObjectSend(arg0) end

---@public
---@return void
function IsoCarBatteryCharger:addToWorld() end

---@public
---@return void
function IsoCarBatteryCharger:sync() end

---@public
---@param arg0 InventoryItem
---@return void
function IsoCarBatteryCharger:setBattery(arg0) end

---@public
---@param arg0 ByteBuffer
---@param arg1 boolean
---@return void
function IsoCarBatteryCharger:save(arg0, arg1) end

---@private
---@param arg0 InventoryItem
---@param arg1 IsoSprite
---@return IsoSprite
function IsoCarBatteryCharger:configureSprite(arg0, arg1) end

---@public
---@param arg0 boolean
---@param arg1 byte
---@param arg2 UdpConnection
---@param arg3 ByteBuffer
---@return void
function IsoCarBatteryCharger:syncIsoObject(arg0, arg1, arg2, arg3) end

---@private
---@return void
function IsoCarBatteryCharger:startChargingSound() end

---@public
---@param arg0 float
---@return void
function IsoCarBatteryCharger:setChargeRate(arg0) end

---@public
---@return boolean
function IsoCarBatteryCharger:isActivated() end

---@public
---@return InventoryItem
function IsoCarBatteryCharger:getBattery() end

---@public
---@return void
function IsoCarBatteryCharger:removeFromWorld() end

---@public
---@return void
function IsoCarBatteryCharger:update() end

---@public
---@return float
function IsoCarBatteryCharger:getChargeRate() end

---@public
---@return InventoryItem
function IsoCarBatteryCharger:getItem() end

---@public
---@param arg0 float
---@param arg1 float
---@param arg2 float
---@param arg3 ColorInfo
---@param arg4 boolean
---@param arg5 boolean
---@param arg6 Shader
---@return void
function IsoCarBatteryCharger:render(arg0, arg1, arg2, arg3, arg4, arg5, arg6) end

---@public
---@param arg0 float
---@param arg1 float
---@param arg2 float
---@param arg3 ColorInfo
---@return void
function IsoCarBatteryCharger:renderObjectPicker(arg0, arg1, arg2, arg3) end

---@private
---@return void
function IsoCarBatteryCharger:stopChargingSound() end

---@class IsoGenerator : zombie.iso.objects.IsoGenerator
---@field public fuel float
---@field public activated boolean
---@field public condition int
---@field private lastHour int
---@field public connected boolean
---@field private numberOfElectricalItems int
---@field private updateSurrounding boolean
---@field private itemsPowered HashMap|Unknown|Unknown
---@field private totalPowerUsing float
---@field private AllGenerators ArrayList|Unknown
---@field private GENERATOR_RADIUS int
IsoGenerator = {}

---@public
---@return float
function IsoGenerator:getTotalPowerUsing() end

---@public
---@return int
function IsoGenerator:getCondition() end

---Overrides:
---
---addToWorld in class IsoObject
---@public
---@return void
function IsoGenerator:addToWorld() end

---@public
---@return void
function IsoGenerator:failToStart() end

---@private
---@return void
---@overload fun(arg0:IsoObject)
---@overload fun(arg0:IsoGridSquare)
function IsoGenerator:updateFridgeFreezerItems() end

---@private
---@param arg0 IsoObject
---@return void
function IsoGenerator:updateFridgeFreezerItems(arg0) end

---@private
---@param arg0 IsoGridSquare
---@return void
function IsoGenerator:updateFridgeFreezerItems(arg0) end

---@public
---@param arg0 IsoChunk
---@return void
function IsoGenerator:chunkLoaded(arg0) end

---@private
---@param arg0 IsoChunk
---@return boolean
function IsoGenerator:touchesChunk(arg0) end

---@public
---@param condition int
---@return void
function IsoGenerator:setCondition(condition) end

---@public
---@param arg0 boolean
---@param arg1 byte
---@param arg2 UdpConnection
---@param arg3 ByteBuffer
---@return void
function IsoGenerator:syncIsoObject(arg0, arg1, arg2, arg3) end

---@public
---@param arg0 float
---@return void
function IsoGenerator:setTotalPowerUsing(arg0) end

---@public
---@return void
function IsoGenerator:updateSurroundingNow() end

---@public
---@return void
function IsoGenerator:removeFromWorld() end

---@public
---@param arg0 int
---@param arg1 int
---@param arg2 int
---@param arg3 int
---@param arg4 int
---@param arg5 int
---@return boolean
function IsoGenerator:isPoweringSquare(arg0, arg1, arg2, arg3, arg4, arg5) end

---@public
---@return void
function IsoGenerator:remove() end

---@public
---@param arg0 ByteBuffer
---@param arg1 int
---@param arg2 boolean
---@return void
function IsoGenerator:load(arg0, arg1, arg2) end

---Overrides:
---
---getObjectName in class IsoObject
---@public
---@return String
function IsoGenerator:getObjectName() end

---@public
---@param arg0 ByteBufferWriter
---@return void
function IsoGenerator:syncIsoObjectSend(arg0) end

---@public
---@param item InventoryItem
---@return void
function IsoGenerator:setInfoFromItem(item) end

---@public
---@param arg0 ByteBuffer
---@param arg1 boolean
---@return void
function IsoGenerator:save(arg0, arg1) end

---@public
---@return ArrayList|Unknown
function IsoGenerator:getItemsPowered() end

---@public
---@param arg0 float
---@param arg1 int
---@param arg2 boolean
---@param arg3 boolean
---@return void
function IsoGenerator:sync(arg0, arg1, arg2, arg3) end

---@private
---@param arg0 IsoObject
---@param arg1 float
---@return void
function IsoGenerator:addPoweredItem(arg0, arg1) end

---@public
---@return boolean
function IsoGenerator:isConnected() end

---@public
---@param activated boolean
---@return void
function IsoGenerator:setActivated(activated) end

---@public
---@return void
function IsoGenerator:Reset() end

---@public
---@return boolean
function IsoGenerator:isActivated() end

---@public
---@return float
function IsoGenerator:getFuel() end

---Overrides:
---
---update in class IsoObject
---@public
---@return void
function IsoGenerator:update() end

---@public
---@param arg0 IsoGridSquare
---@return void
function IsoGenerator:updateGenerator(arg0) end

---@public
---@return void
function IsoGenerator:setSurroundingElectricity() end

---@public
---@param connected boolean
---@return void
function IsoGenerator:setConnected(connected) end

---@public
---@param arg0 float
---@return void
function IsoGenerator:setFuel(arg0) end

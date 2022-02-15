---@class IsoLightSwitch : zombie.iso.objects.IsoLightSwitch
---@field Activated boolean
---@field public lights ArrayList|IsoLightSource
---@field public lightRoom boolean
---@field public RoomID int
---@field public bStreetLight boolean
---@field private canBeModified boolean
---@field private useBattery boolean
---@field private hasBattery boolean
---@field private bulbItem String
---@field private power float
---@field private delta float
---@field private primaryR float
---@field private primaryG float
---@field private primaryB float
---@field protected lastMinuteStamp long
---@field protected bulbBurnMinutes int
---@field protected lastMin int
---@field protected nextBreakUpdate int
IsoLightSwitch = {}

---@public
---@param arg0 IsoGameCharacter
---@return DrainableComboItem
function IsoLightSwitch:removeBattery(arg0) end

---@public
---@return boolean
function IsoLightSwitch:hasLightBulb() end

---@public
---@return ArrayList|Unknown
function IsoLightSwitch:getLights() end

---@private
---@param arg0 UdpConnection
---@return void
function IsoLightSwitch:writeCustomizedSettingsPacket(arg0) end

---Overrides:
---
---update in class IsoObject
---@public
---@return void
function IsoLightSwitch:update() end

---@private
---@param arg0 ByteBuffer
---@return void
function IsoLightSwitch:readCustomizedSettingsPacket(arg0) end

---@private
---@param arg0 ByteBufferWriter
---@param arg1 byte
---@return void
function IsoLightSwitch:writeLightSwitchObjectHeader(arg0, arg1) end

---@public
---@param arg0 boolean
---@return void
function IsoLightSwitch:setUseBattery(arg0) end

---@public
---@return float
function IsoLightSwitch:getPrimaryG() end

---@public
---@return boolean
function IsoLightSwitch:getCanBeModified() end

---@public
---@return boolean
function IsoLightSwitch:getUseBattery() end

---@public
---@param arg0 float
---@return void
function IsoLightSwitch:setDelta(arg0) end

---@public
---@return boolean
function IsoLightSwitch:toggle() end

---Overrides:
---
---removeFromWorld in class IsoObject
---@public
---@return void
function IsoLightSwitch:removeFromWorld() end

---@public
---@param arg0 float
---@return void
function IsoLightSwitch:setPrimaryG(arg0) end

---@public
---@param active boolean
---@return boolean
---@overload fun(arg0:boolean, arg1:boolean)
---@overload fun(arg0:boolean, arg1:boolean, arg2:boolean)
function IsoLightSwitch:setActive(active) end

---@public
---@param arg0 boolean
---@param arg1 boolean
---@return boolean
function IsoLightSwitch:setActive(arg0, arg1) end

---@public
---@param arg0 boolean
---@param arg1 boolean
---@param arg2 boolean
---@return boolean
function IsoLightSwitch:setActive(arg0, arg1, arg2) end

---@public
---@param arg0 IsoGameCharacter
---@return InventoryItem
function IsoLightSwitch:removeLightBulb(arg0) end

---@public
---@param arg0 IsoGameCharacter
---@param arg1 InventoryItem
---@return void
function IsoLightSwitch:addBattery(arg0, arg1) end

---Overrides:
---
---getObjectName in class IsoObject
---@public
---@return String
function IsoLightSwitch:getObjectName() end

---Overrides:
---
---addToWorld in class IsoObject
---@public
---@return void
function IsoLightSwitch:addToWorld() end

---@public
---@return float
function IsoLightSwitch:getPrimaryR() end

---@public
---@param arg0 ByteBuffer
---@param arg1 int
---@param arg2 boolean
---@return void
function IsoLightSwitch:load(arg0, arg1, arg2) end

---@public
---@param arg0 float
---@return void
function IsoLightSwitch:setPower(arg0) end

---@public
---@param arg0 UdpConnection
---@return void
function IsoLightSwitch:syncCustomizedSettings(arg0) end

---Overrides:
---
---onMouseLeftClick in class IsoObject
---@public
---@param x int
---@param y int
---@return boolean
function IsoLightSwitch:onMouseLeftClick(x, y) end

---@public
---@param arg0 InventoryItem
---@return void
function IsoLightSwitch:getCustomSettingsFromItem(arg0) end

---Overrides:
---
---syncIsoObject in class IsoObject
---@public
---@param bRemote boolean
---@param val byte
---@param source UdpConnection
---@return void
---@overload fun(arg0:boolean, arg1:byte, arg2:UdpConnection, arg3:ByteBuffer)
function IsoLightSwitch:syncIsoObject(bRemote, val, source) end

---@public
---@param arg0 boolean
---@param arg1 byte
---@param arg2 UdpConnection
---@param arg3 ByteBuffer
---@return void
function IsoLightSwitch:syncIsoObject(arg0, arg1, arg2, arg3) end

---@private
---@return IsoLightSource
function IsoLightSwitch:getPrimaryLight() end

---@public
---@return float
function IsoLightSwitch:getPrimaryB() end

---@public
---@return boolean
function IsoLightSwitch:isActivated() end

---@public
---@return void
function IsoLightSwitch:addLightSourceFromSprite() end

---@public
---@param arg0 boolean
---@return void
function IsoLightSwitch:setHasBatteryRaw(arg0) end

---@public
---@param arg0 String
---@return void
function IsoLightSwitch:setBulbItemRaw(arg0) end

---@public
---@param arg0 IsoGameCharacter
---@param arg1 InventoryItem
---@return void
function IsoLightSwitch:addLightBulb(arg0, arg1) end

---@public
---@return boolean
function IsoLightSwitch:getHasBattery() end

---@public
---@param arg0 boolean
---@return void
function IsoLightSwitch:switchLight(arg0) end

---@public
---@param arg0 float
---@return void
function IsoLightSwitch:setPrimaryB(arg0) end

---@public
---@param arg0 InventoryItem
---@return void
function IsoLightSwitch:setCustomSettingsToItem(arg0) end

---@public
---@param arg0 ByteBuffer
---@param arg1 UdpConnection
---@return void
function IsoLightSwitch:receiveSyncCustomizedSettings(arg0, arg1) end

---@public
---@return String
function IsoLightSwitch:getBulbItem() end

---@public
---@return float
function IsoLightSwitch:getPower() end

---@public
---@param arg0 ByteBufferWriter
---@return void
function IsoLightSwitch:syncIsoObjectSend(arg0) end

---@public
---@return boolean
function IsoLightSwitch:canSwitchLight() end

---@public
---@param arg0 ByteBuffer
---@param arg1 boolean
---@return void
function IsoLightSwitch:save(arg0, arg1) end

---@public
---@param chunk IsoChunk
---@return void
function IsoLightSwitch:chunkLoaded(chunk) end

---@public
---@param arg0 float
---@return void
function IsoLightSwitch:setPrimaryR(arg0) end

---@public
---@return float
function IsoLightSwitch:getDelta() end

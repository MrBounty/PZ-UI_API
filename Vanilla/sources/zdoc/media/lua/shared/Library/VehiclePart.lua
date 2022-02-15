---@class VehiclePart : zombie.vehicles.VehiclePart
---@field protected vehicle BaseVehicle
---@field protected bCreated boolean
---@field protected partId String
---@field protected scriptPart VehicleScript.Part
---@field protected container ItemContainer
---@field protected item InventoryItem
---@field protected modData KahluaTable
---@field protected lastUpdated float
---@field protected updateFlags short
---@field protected parent VehiclePart
---@field protected door VehicleDoor
---@field protected window VehicleWindow
---@field protected children ArrayList|Unknown
---@field protected category String
---@field protected condition int
---@field protected specificItem boolean
---@field protected wheelFriction float
---@field protected mechanicSkillInstaller int
---@field private suspensionDamping float
---@field private suspensionCompression float
---@field private engineLoudness float
---@field protected light VehicleLight
---@field protected deviceData DeviceData
---@field protected chatElement ChatElement
---@field protected hasPlayerInRange boolean
VehiclePart = {}

---@public
---@return float
function VehiclePart:getLightFocusing() end

---@public
---@param arg0 InventoryItem
---@return void
function VehiclePart:setRandomCondition(arg0) end

---@public
---@param arg0 int
---@return void
function VehiclePart:setMechanicSkillInstaller(arg0) end

---@public
---@return int
function VehiclePart:getContainerSeatNumber() end

---@public
---@return int
---@overload fun(arg0:IsoGameCharacter)
function VehiclePart:getContainerCapacity() end

---@public
---@param arg0 IsoGameCharacter
---@return int
function VehiclePart:getContainerCapacity(arg0) end

---@public
---@param arg0 float
---@return void
---@overload fun(arg0:float, arg1:boolean, arg2:boolean)
function VehiclePart:setContainerContentAmount(arg0) end

---@public
---@param arg0 float
---@param arg1 boolean
---@param arg2 boolean
---@return void
function VehiclePart:setContainerContentAmount(arg0, arg1, arg2) end

---@public
---@return String
function VehiclePart:getId() end

---@public
---@return String
function VehiclePart:getCategory() end

---@public
---@return VehicleDoor
function VehiclePart:getDoor() end

---@public
---@return InventoryItem
function VehiclePart:getInventoryItem() end

---@public
---@return boolean
function VehiclePart:HasPlayerInRange() end

---@public
---@param arg0 String
---@return KahluaTable
function VehiclePart:getTable(arg0) end

---@public
---@return VehicleWindow
function VehiclePart:getWindow() end

---@public
---@param arg0 float
---@return void
function VehiclePart:setWheelFriction(arg0) end

---@public
---@param arg0 String
---@param arg1 boolean
---@return void
function VehiclePart:setModelVisible(arg0, arg1) end

---@public
---@param arg0 InventoryItem
---@param arg1 int
---@return void
function VehiclePart:doInventoryItemStats(arg0, arg1) end

---@public
---@param arg0 float
---@return void
function VehiclePart:setSuspensionCompression(arg0) end

---@public
---@param arg0 VehicleScript.Part
---@return void
function VehiclePart:setScriptPart(arg0) end

---@public
---@return int
function VehiclePart:getChildCount() end

---@public
---@return float
function VehiclePart:getContainerContentAmount() end

---@public
---@param arg0 DeviceData
---@return void
function VehiclePart:setDeviceData(arg0) end

---@public
---@return ArrayList|Unknown
function VehiclePart:getItemType() end

---@public
---@return float
function VehiclePart:getEngineLoudness() end

---@public
---@return VehicleLight
function VehiclePart:getLight() end

---@public
---@return float
function VehiclePart:getSuspensionDamping() end

---@public
---@return VehicleScript.Part
function VehiclePart:getScriptPart() end

---@public
---@param arg0 float
---@return void
function VehiclePart:setSuspensionDamping(arg0) end

---@public
---@return float
function VehiclePart:getDelta() end

---@public
---@return KahluaTable
function VehiclePart:getModData() end

---@public
---@param arg0 InventoryItem
---@param arg1 float
---@param arg2 float
---@return void
function VehiclePart:setGeneralCondition(arg0, arg1, arg2) end

---@public
---@param arg0 float
---@param arg1 float
---@param arg2 float
---@param arg3 float
---@param arg4 float
---@param arg5 int
---@return void
function VehiclePart:createSpotLight(arg0, arg1, arg2, arg3, arg4, arg5) end

---@public
---@return boolean
function VehiclePart:hasModData() end

---@public
---@return boolean
function VehiclePart:hasDevicePower() end

---@public
---@return void
function VehiclePart:updateSignalDevice() end

---@public
---@param arg0 String
---@return VehicleScript.Anim
function VehiclePart:getAnimById(arg0) end

---@public
---@return DeviceData
function VehiclePart:getDeviceData() end

---@public
---@return DeviceData
function VehiclePart:createSignalDevice() end

---@public
---@return int
function VehiclePart:getIndex() end

---@public
---@return boolean
function VehiclePart:isSpecificItem() end

---@public
---@return boolean
function VehiclePart:isContainer() end

---@public
---@return VehiclePart
function VehiclePart:getChildWindow() end

---@public
---@param arg0 InventoryItem
---@return void
---@overload fun(arg0:InventoryItem, arg1:int)
function VehiclePart:setInventoryItem(arg0) end

---@public
---@param arg0 InventoryItem
---@param arg1 int
---@return void
function VehiclePart:setInventoryItem(arg0, arg1) end

---@public
---@param arg0 int
---@return void
function VehiclePart:setContainerCapacity(arg0) end

---@public
---@param arg0 VehiclePart
---@return void
function VehiclePart:addChild(arg0) end

---@protected
---@param arg0 String
---@return VehicleScript.Model
function VehiclePart:getScriptModelById(arg0) end

---@public
---@return float
function VehiclePart:getY() end

---@public
---@return int
function VehiclePart:getMechanicSkillInstaller() end

---@public
---@return float
function VehiclePart:getLightIntensity() end

---@public
---@return float
function VehiclePart:getWheelFriction() end

---@public
---@return int
function VehiclePart:getCondition() end

---@public
---@param arg0 float
---@return void
function VehiclePart:setLastUpdated(arg0) end

---@public
---@param arg0 int
---@return void
function VehiclePart:setCondition(arg0) end

---@public
---@return float
function VehiclePart:getLastUpdated() end

---@public
---@return BaseVehicle
function VehiclePart:getVehicle() end

---@public
---@param arg0 String
---@param arg1 float
---@param arg2 float
---@param arg3 float
---@param arg4 String
---@param arg5 int
---@return void
function VehiclePart:AddDeviceText(arg0, arg1, arg2, arg3, arg4, arg5) end

---@public
---@param arg0 boolean
---@return void
function VehiclePart:setLightActive(arg0) end

---@public
---@return void
function VehiclePart:repair() end

---@public
---@return float
function VehiclePart:getX() end

---@public
---@return VehicleWindow
function VehiclePart:findWindow() end

---@public
---@return int
function VehiclePart:getWheelIndex() end

---@public
---@return String
function VehiclePart:getContainerContentType() end

---@public
---@param arg0 ByteBuffer
---@param arg1 int
---@return void
function VehiclePart:load(arg0, arg1) end

---@public
---@return VehiclePart
function VehiclePart:getParent() end

---@public
---@param arg0 int
---@return VehiclePart
function VehiclePart:getChild(arg0) end

---@public
---@param arg0 float
---@return void
function VehiclePart:setDelta(arg0) end

---@public
---@param arg0 String
---@return void
function VehiclePart:setCategory(arg0) end

---@public
---@param arg0 ByteBuffer
---@return void
function VehiclePart:save(arg0) end

---@public
---@param arg0 int
---@return void
function VehiclePart:damage(arg0) end

---@public
---@return float
function VehiclePart:getLightDistance() end

---@public
---@param arg0 String
---@return String
function VehiclePart:getLuaFunction(arg0) end

---@public
---@param arg0 float
---@param arg1 float
---@param arg2 float
---@return float
function VehiclePart:getNumberByCondition(arg0, arg1, arg2) end

---@private
---@param arg0 IsoPlayer
---@param arg1 float
---@return boolean
function VehiclePart:playerWithinBounds(arg0, arg1) end

---@public
---@return String
function VehiclePart:getArea() end

---@public
---@param arg0 ItemContainer
---@return void
function VehiclePart:setItemContainer(arg0) end

---@public
---@return float
function VehiclePart:getZ() end

---@public
---@return ItemContainer
function VehiclePart:getItemContainer() end

---@public
---@return float
function VehiclePart:getSuspensionCompression() end

---@public
---@return IsoGridSquare
function VehiclePart:getSquare() end

---@public
---@param arg0 float
---@return void
function VehiclePart:setEngineLoudness(arg0) end

---@public
---@param arg0 boolean
---@return void
function VehiclePart:setSpecificItem(arg0) end

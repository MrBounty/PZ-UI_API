---@class IsoWorldInventoryObject : zombie.iso.objects.IsoWorldInventoryObject
---@field public item InventoryItem
---@field public xoff float
---@field public yoff float
---@field public zoff float
---@field public removeProcess boolean
---@field public dropTime double
---@field public ignoreRemoveSandbox boolean
IsoWorldInventoryObject = {}

---@public
---@param arg0 String
---@param arg1 ByteBuffer
---@return void
function IsoWorldInventoryObject:loadChange(arg0, arg1) end

---@public
---@return void
function IsoWorldInventoryObject:addToWorld() end

---Overrides:
---
---Serialize in class IsoObject
---@public
---@return boolean
function IsoWorldInventoryObject:Serialize() end

---Overrides:
---
---removeFromSquare in class IsoObject
---@public
---@return void
function IsoWorldInventoryObject:removeFromSquare() end

---@public
---@return int
function IsoWorldInventoryObject:getWaterAmount() end

---@public
---@param arg0 int
---@return void
function IsoWorldInventoryObject:setWaterAmount(arg0) end

---@public
---@param arg0 boolean
---@return void
function IsoWorldInventoryObject:setIgnoreRemoveSandbox(arg0) end

---@public
---@return int
function IsoWorldInventoryObject:getWaterMax() end

---@public
---@param arg0 float
---@param arg1 float
---@param arg2 float
---@param arg3 ColorInfo
---@param arg4 boolean
---@param arg5 boolean
---@param arg6 Shader
---@return void
function IsoWorldInventoryObject:render(arg0, arg1, arg2, arg3, arg4, arg5, arg6) end

---Overrides:
---
---HasTooltip in class IsoObject
---@public
---@return boolean
function IsoWorldInventoryObject:HasTooltip() end

---@public
---@return boolean
function IsoWorldInventoryObject:isIgnoreRemoveSandbox() end

---@private
---@param arg0 float
---@param arg1 float
---@param arg2 float
---@return void
function IsoWorldInventoryObject:debugDrawLocation(arg0, arg1, arg2) end

---@public
---@return boolean
function IsoWorldInventoryObject:finishupdate() end

---@public
---@return float
function IsoWorldInventoryObject:getWorldPosX() end

---@public
---@param arg0 int
---@return float
function IsoWorldInventoryObject:getScreenPosX(arg0) end

---Overrides:
---
---onMouseLeftClick in class IsoObject
---@public
---@param x int
---@param y int
---@return boolean
function IsoWorldInventoryObject:onMouseLeftClick(x, y) end

---@private
---@return void
function IsoWorldInventoryObject:debugHitTest() end

---@private
---@return boolean
function IsoWorldInventoryObject:isWaterSource() end

---@public
---@param arg0 ByteBuffer
---@param arg1 int
---@param arg2 boolean
---@return void
function IsoWorldInventoryObject:load(arg0, arg1, arg2) end

---Overrides:
---
---getObjectName in class IsoObject
---@public
---@return String
function IsoWorldInventoryObject:getObjectName() end

---@public
---@param arg0 InventoryItem
---@return void
function IsoWorldInventoryObject:swapItem(arg0) end

---@public
---@param arg0 ByteBuffer
---@param arg1 boolean
---@return void
function IsoWorldInventoryObject:save(arg0, arg1) end

---@public
---@return float
function IsoWorldInventoryObject:getWorldPosY() end

---@public
---@return InventoryItem
function IsoWorldInventoryObject:getItem() end

---@public
---@return void
function IsoWorldInventoryObject:removeFromWorld() end

---@public
---@param arg0 boolean
---@return void
function IsoWorldInventoryObject:setTaintedWater(arg0) end

---@public
---@return boolean
function IsoWorldInventoryObject:isTaintedWater() end

---Overrides:
---
---renderObjectPicker in class IsoObject
---@public
---@param x float
---@param y float
---@param z float
---@param lightInfo ColorInfo
---@return void
function IsoWorldInventoryObject:renderObjectPicker(x, y, z, lightInfo) end

---@public
---@return void
function IsoWorldInventoryObject:softReset() end

---@public
---@param arg0 String
---@param arg1 KahluaTable
---@param arg2 ByteBuffer
---@return void
function IsoWorldInventoryObject:saveChange(arg0, arg1, arg2) end

---@public
---@return float
function IsoWorldInventoryObject:getWorldPosZ() end

---Overrides:
---
---DoTooltip in class IsoObject
---@public
---@param tooltipUI ObjectTooltip
---@return void
function IsoWorldInventoryObject:DoTooltip(tooltipUI) end

---@public
---@param arg0 int
---@return float
function IsoWorldInventoryObject:getScreenPosY(arg0) end

---@public
---@return void
function IsoWorldInventoryObject:update() end

---@public
---@return void
function IsoWorldInventoryObject:updateSprite() end

---@class IsoClothingWasher : zombie.iso.objects.IsoClothingWasher
---@field private bActivated boolean
---@field private soundInstance long
---@field private lastUpdate float
---@field private cycleFinished boolean
---@field private startTime float
---@field private cycleLengthMinutes float
---@field private alreadyExecuted boolean
---@field private coveredParts ArrayList|Unknown
IsoClothingWasher = {}

---@public
---@param arg0 ByteBuffer
---@param arg1 int
---@param arg2 boolean
---@return void
function IsoClothingWasher:load(arg0, arg1, arg2) end

---@public
---@return String
function IsoClothingWasher:getObjectName() end

---@public
---@return void
function IsoClothingWasher:addToWorld() end

---@public
---@param arg0 String
---@param arg1 ByteBuffer
---@return void
function IsoClothingWasher:loadChange(arg0, arg1) end

---@public
---@param arg0 boolean
---@return void
function IsoClothingWasher:setActivated(arg0) end

---@public
---@param arg0 ByteBuffer
---@param arg1 boolean
---@return void
function IsoClothingWasher:save(arg0, arg1) end

---@private
---@return boolean
function IsoClothingWasher:cycleFinished() end

---@public
---@return void
function IsoClothingWasher:update() end

---@public
---@param arg0 ItemContainer
---@param arg1 InventoryItem
---@return boolean
function IsoClothingWasher:isRemoveItemAllowedFromContainer(arg0, arg1) end

---@public
---@param arg0 ItemContainer
---@param arg1 InventoryItem
---@return boolean
function IsoClothingWasher:isItemAllowedInContainer(arg0, arg1) end

---@public
---@return void
function IsoClothingWasher:removeFromWorld() end

---@public
---@param arg0 String
---@param arg1 KahluaTable
---@param arg2 ByteBuffer
---@return void
function IsoClothingWasher:saveChange(arg0, arg1, arg2) end

---@private
---@param arg0 Clothing
---@param arg1 float
---@return void
function IsoClothingWasher:removeBlood(arg0, arg1) end

---@private
---@param arg0 Clothing
---@param arg1 float
---@return void
function IsoClothingWasher:removeDirt(arg0, arg1) end

---@private
---@return void
function IsoClothingWasher:updateSound() end

---@public
---@return boolean
function IsoClothingWasher:isActivated() end

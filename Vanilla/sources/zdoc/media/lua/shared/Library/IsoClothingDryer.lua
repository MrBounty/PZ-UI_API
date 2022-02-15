---@class IsoClothingDryer : zombie.iso.objects.IsoClothingDryer
---@field private bActivated boolean
---@field private SoundInstance long
---@field private lastUpdate float
---@field private cycleFinished boolean
---@field private startTime float
---@field private cycleLengthMinutes float
---@field private alreadyExecuted boolean
IsoClothingDryer = {}

---@public
---@return void
function IsoClothingDryer:update() end

---@public
---@param arg0 ItemContainer
---@param arg1 InventoryItem
---@return boolean
function IsoClothingDryer:isRemoveItemAllowedFromContainer(arg0, arg1) end

---@public
---@param arg0 ItemContainer
---@param arg1 InventoryItem
---@return boolean
function IsoClothingDryer:isItemAllowedInContainer(arg0, arg1) end

---@public
---@param arg0 String
---@param arg1 KahluaTable
---@param arg2 ByteBuffer
---@return void
function IsoClothingDryer:saveChange(arg0, arg1, arg2) end

---@private
---@return void
function IsoClothingDryer:updateSound() end

---@public
---@return boolean
function IsoClothingDryer:isActivated() end

---@public
---@return void
function IsoClothingDryer:addToWorld() end

---@private
---@return boolean
function IsoClothingDryer:cycleFinished() end

---@public
---@param arg0 boolean
---@return void
function IsoClothingDryer:setActivated(arg0) end

---@public
---@return void
function IsoClothingDryer:removeFromWorld() end

---@public
---@return String
function IsoClothingDryer:getObjectName() end

---@public
---@param arg0 String
---@param arg1 ByteBuffer
---@return void
function IsoClothingDryer:loadChange(arg0, arg1) end

---@public
---@param arg0 ByteBuffer
---@param arg1 int
---@param arg2 boolean
---@return void
function IsoClothingDryer:load(arg0, arg1, arg2) end

---@public
---@param arg0 ByteBuffer
---@param arg1 boolean
---@return void
function IsoClothingDryer:save(arg0, arg1) end

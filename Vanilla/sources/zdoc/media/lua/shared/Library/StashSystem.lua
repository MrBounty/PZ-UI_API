---@class StashSystem : zombie.core.stash.StashSystem
---@field public allStashes ArrayList|Unknown
---@field public possibleStashes ArrayList|Unknown
---@field public buildingsToDo ArrayList|Unknown
---@field private possibleTrap ArrayList|Unknown
---@field private alreadyReadMap ArrayList|Unknown
StashSystem = {}

---@public
---@param arg0 ByteBuffer
---@return void
function StashSystem:save(arg0) end

---@public
---@param arg0 BuildingDef
---@return void
function StashSystem:doBuildingStash(arg0) end

---@private
---@param arg0 Stash
---@return void
function StashSystem:removeFromPossibleStash(arg0) end

---@private
---@param arg0 Stash
---@param arg1 InventoryItem
---@return boolean
function StashSystem:checkSpecificSpawnProperties(arg0, arg1) end

---@public
---@param arg0 InventoryItem
---@return void
function StashSystem:checkStashItem(arg0) end

---@public
---@return void
function StashSystem:reinit() end

---@public
---@param arg0 String
---@return void
function StashSystem:prepareBuildingStash(arg0) end

---@private
---@param arg0 Stash
---@param arg1 BuildingDef
---@return void
function StashSystem:doSpecificBuildingProperties(arg0, arg1) end

---@public
---@return void
function StashSystem:initAllStashes() end

---@public
---@param arg0 ByteBuffer
---@param arg1 int
---@return void
function StashSystem:load(arg0, arg1) end

---@public
---@return void
function StashSystem:init() end

---@public
---@param arg0 BuildingDef
---@return void
function StashSystem:visitedBuilding(arg0) end

---@public
---@return void
function StashSystem:Reset() end

---@public
---@param arg0 String
---@return Stash
function StashSystem:getStash(arg0) end

---@public
---@return ArrayList|Unknown
function StashSystem:getPossibleStashes() end

---@public
---@param arg0 Stash
---@param arg1 InventoryItem
---@return void
function StashSystem:doStashItem(arg0, arg1) end

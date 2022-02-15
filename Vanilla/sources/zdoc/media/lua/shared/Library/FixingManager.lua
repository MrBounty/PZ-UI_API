---@class FixingManager : zombie.inventory.FixingManager
FixingManager = {}

---@private
---@param arg0 IsoGameCharacter
---@param arg1 Fixing.Fixer
---@return void
function FixingManager:addXp(arg0, arg1) end

---@public
---@param brokenItem InventoryItem
---@param chr IsoGameCharacter
---@param fixing Fixing
---@param fixer Fixing.Fixer
---@return InventoryItem
function FixingManager:fixItem(brokenItem, chr, fixing, fixer) end

---@public
---@param brokenItem InventoryItem
---@param chr IsoGameCharacter
---@param fixing Fixing
---@param fixer Fixing.Fixer
---@return double
function FixingManager:getCondRepaired(brokenItem, chr, fixing, fixer) end

---@public
---@param brokenItem InventoryItem
---@param chr IsoGameCharacter
---@param fixing Fixing
---@param fixer Fixing.Fixer
---@return double
function FixingManager:getChanceOfFail(brokenItem, chr, fixing, fixer) end

---@public
---@param arg0 InventoryItem
---@return ArrayList|Unknown
function FixingManager:getFixes(arg0) end

---@public
---@param chr IsoGameCharacter
---@param fixer Fixing.Fixer
---@param brokenItem InventoryItem
---@return void
function FixingManager:useFixer(chr, fixer, brokenItem) end

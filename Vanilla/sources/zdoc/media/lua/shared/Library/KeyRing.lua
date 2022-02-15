---@class KeyRing : zombie.inventory.types.KeyRing
---@field private keys ArrayList|Unknown
KeyRing = {}

---@public
---@param key Key
---@return void
function KeyRing:addKey(key) end

---Overrides:
---
---getCategory in class InventoryItem
---@public
---@return String
function KeyRing:getCategory() end

---@public
---@param keys ArrayList|Key
---@return void
function KeyRing:setKeys(keys) end

---@public
---@return int
function KeyRing:getSaveType() end

---@public
---@param keyId int
---@return boolean
function KeyRing:containsKeyId(keyId) end

---@public
---@return ArrayList|Key
function KeyRing:getKeys() end

---@class WornItem : zombie.characters.WornItems.WornItem
---@field protected location String
---@field protected item InventoryItem
WornItem = {}

---@public
---@return InventoryItem
function WornItem:getItem() end

---@public
---@return String
function WornItem:getLocation() end

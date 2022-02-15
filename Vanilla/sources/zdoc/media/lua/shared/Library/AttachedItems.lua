---@class AttachedItems : zombie.characters.AttachedItems.AttachedItems
---@field protected group AttachedLocationGroup
---@field protected items ArrayList|Unknown
AttachedItems = {}

---@public
---@return AttachedLocationGroup
function AttachedItems:getGroup() end

---@public
---@param arg0 String
---@param arg1 InventoryItem
---@return void
function AttachedItems:setItem(arg0, arg1) end

---@public
---@param arg0 Consumer|Unknown
---@return void
function AttachedItems:forEach(arg0) end

---@public
---@param arg0 String
---@return InventoryItem
function AttachedItems:getItem(arg0) end

---@public
---@param arg0 AttachedItems
---@return void
function AttachedItems:copyFrom(arg0) end

---@public
---@return void
function AttachedItems:clear() end

---@public
---@return boolean
function AttachedItems:isEmpty() end

---@public
---@return int
function AttachedItems:size() end

---@public
---@param arg0 int
---@return InventoryItem
function AttachedItems:getItemByIndex(arg0) end

---@private
---@param arg0 InventoryItem
---@return int
---@overload fun(arg0:String)
function AttachedItems:indexOf(arg0) end

---@private
---@param arg0 String
---@return int
function AttachedItems:indexOf(arg0) end

---@public
---@param arg0 InventoryItem
---@return String
function AttachedItems:getLocation(arg0) end

---@public
---@param arg0 int
---@return AttachedItem
function AttachedItems:get(arg0) end

---@public
---@param arg0 InventoryItem
---@return void
function AttachedItems:remove(arg0) end

---@public
---@param arg0 InventoryItem
---@return boolean
function AttachedItems:contains(arg0) end

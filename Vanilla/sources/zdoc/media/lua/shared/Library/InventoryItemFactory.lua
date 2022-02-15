---@class InventoryItemFactory : zombie.inventory.InventoryItemFactory
InventoryItemFactory = {}

---@public
---@param itemType String
---@return InventoryItem
---@overload fun(arg0:short)
---@overload fun(arg0:String, arg1:Food)
---@overload fun(itemType:String, useDelta:float)
---@overload fun(itemType:String, useDelta:float, param:String)
---@overload fun(module:String, name:String, type:String, tex:String)
function InventoryItemFactory:CreateItem(itemType) end

---@public
---@param arg0 short
---@return InventoryItem
function InventoryItemFactory:CreateItem(arg0) end

---@public
---@param arg0 String
---@param arg1 Food
---@return InventoryItem
function InventoryItemFactory:CreateItem(arg0, arg1) end

---@public
---@param itemType String
---@param useDelta float
---@return InventoryItem
function InventoryItemFactory:CreateItem(itemType, useDelta) end

---@public
---@param itemType String
---@param useDelta float
---@param param String
---@return InventoryItem
function InventoryItemFactory:CreateItem(itemType, useDelta, param) end

---@public
---@param module String
---@param name String
---@param type String
---@param tex String
---@return InventoryItem
function InventoryItemFactory:CreateItem(module, name, type, tex) end

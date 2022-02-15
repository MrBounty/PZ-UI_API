---@class ItemRecipe : zombie.scripting.objects.ItemRecipe
---@field public name String
---@field public use Integer
---@field public cooked Boolean
---@field private module String
ItemRecipe = {}

---@public
---@return String
function ItemRecipe:getModule() end

---@public
---@return String
function ItemRecipe:getName() end

---@public
---@param arg0 String
---@return void
function ItemRecipe:setModule(arg0) end

---@public
---@return Integer
function ItemRecipe:getUse() end

---@public
---@return String
function ItemRecipe:getFullType() end

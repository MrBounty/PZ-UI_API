---@class EvolvedRecipe : zombie.scripting.objects.EvolvedRecipe
---@field public name String
---@field public DisplayName String
---@field private originalname String
---@field public maxItems int
---@field public itemsList Map|String|ItemRecipe
---@field public resultItem String
---@field public baseItem String
---@field public cookable boolean
---@field public addIngredientIfCooked boolean
---@field public canAddSpicesEmpty boolean
---@field public addIngredientSound String
EvolvedRecipe = {}

---@public
---@return String
function EvolvedRecipe:getFullResultItem() end

---@public
---@return ArrayList|Unknown
function EvolvedRecipe:getPossibleItems() end

---@public
---@return int
function EvolvedRecipe:getMaxItems() end

---@public
---@param arg0 InventoryItem
---@return boolean
function EvolvedRecipe:isResultItem(arg0) end

---@private
---@param arg0 InventoryItem
---@return void
function EvolvedRecipe:checkUniqueRecipe(arg0) end

---@public
---@return String
function EvolvedRecipe:getOriginalname() end

---@public
---@param arg0 InventoryItem
---@return boolean
function EvolvedRecipe:needToBeCooked(arg0) end

---@public
---@return boolean
function EvolvedRecipe:isCookable() end

---@private
---@param arg0 Food
---@param arg1 Food
---@param arg2 float
---@param arg3 int
---@return void
function EvolvedRecipe:useSpice(arg0, arg1, arg2, arg3) end

---@public
---@param arg0 IsoGameCharacter
---@param arg1 InventoryItem
---@param arg2 ArrayList|Unknown
---@return ArrayList|Unknown
function EvolvedRecipe:getItemsCanBeUse(arg0, arg1, arg2) end

---@private
---@param arg0 InventoryItem
---@param arg1 InventoryItem
---@param arg2 IsoGameCharacter
---@return void
function EvolvedRecipe:addPoison(arg0, arg1, arg2) end

---@public
---@param arg0 InventoryItem
---@param arg1 InventoryItem
---@return boolean
function EvolvedRecipe:isSpiceAdded(arg0, arg1) end

---@public
---@param baseItem InventoryItem
---@param usedItem InventoryItem
---@param chr IsoGameCharacter
---@return InventoryItem
function EvolvedRecipe:addItem(baseItem, usedItem, chr) end

---@public
---@return String
function EvolvedRecipe:getBaseItem() end

---Overrides:
---
---Load in class BaseScriptObject
---@public
---@param name String
---@param strArray String[]
---@return void
function EvolvedRecipe:Load(name, strArray) end

---@public
---@return String
function EvolvedRecipe:getAddIngredientSound() end

---@public
---@param usedItem InventoryItem
---@return ItemRecipe
function EvolvedRecipe:getItemRecipe(usedItem) end

---@public
---@return String
function EvolvedRecipe:getUntranslatedName() end

---@private
---@param arg0 ItemContainer
---@param arg1 String
---@param arg2 InventoryItem
---@param arg3 int
---@param arg4 ArrayList|Unknown
---@return void
function EvolvedRecipe:checkItemCanBeUse(arg0, arg1, arg2, arg3, arg4) end

---@public
---@return String
function EvolvedRecipe:getName() end

---@public
---@return Map|Unknown|Unknown
function EvolvedRecipe:getItemsList() end

---@public
---@return String
function EvolvedRecipe:getResultItem() end

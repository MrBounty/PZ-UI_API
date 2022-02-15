---@class Fixing : zombie.scripting.objects.Fixing
---@field private name String
---@field private require ArrayList|Unknown
---@field private fixers LinkedList|Unknown
---@field private globalItem Fixing.Fixer
---@field private conditionModifier float
---@field private s_PredicateRequired Fixing.PredicateRequired
---@field private s_InventoryItems ArrayList|Unknown
Fixing = {}

---@public
---@return String
function Fixing:getName() end

---@public
---@param itemType InventoryItem
---@param chr IsoGameCharacter
---@return Fixing.Fixer
function Fixing:usedInFixer(itemType, chr) end

---@public
---@param arg0 Fixing.Fixer
---@return void
function Fixing:setGlobalItem(arg0) end

---@public
---@param name String
---@return void
function Fixing:setName(name) end

---Overrides:
---
---Load in class BaseScriptObject
---@public
---@param name String
---@param strArray String[]
---@return void
function Fixing:Load(name, strArray) end

---@public
---@param arg0 IsoGameCharacter
---@param arg1 Fixing.Fixer
---@param arg2 InventoryItem
---@return ArrayList|Unknown
function Fixing:getRequiredItems(arg0, arg1, arg2) end

---@public
---@param arg0 IsoGameCharacter
---@param arg1 Fixing.Fixer
---@param arg2 InventoryItem
---@param arg3 ArrayList|Unknown
---@return ArrayList|Unknown
function Fixing:getRequiredFixerItems(arg0, arg1, arg2, arg3) end

---@private
---@param arg0 InventoryItem
---@return int
---@overload fun(chr:IsoGameCharacter, fixer:Fixing.Fixer, brokenObject:InventoryItem)
function Fixing:countUses(arg0) end

---@public
---@param chr IsoGameCharacter
---@param fixer Fixing.Fixer
---@param brokenObject InventoryItem
---@return int
function Fixing:countUses(chr, fixer, brokenObject) end

---@public
---@return LinkedList|Fixing.Fixer
function Fixing:getFixers() end

---@public
---@param chr IsoGameCharacter
---@param fixer Fixing.Fixer
---@param brokenObject InventoryItem
---@return InventoryItem
function Fixing:haveThisFixer(chr, fixer, brokenObject) end

---@public
---@return float
function Fixing:getConditionModifier() end

---@public
---@param arg0 String
---@return void
function Fixing:addRequiredItem(arg0) end

---@public
---@return ArrayList|Unknown
function Fixing:getRequiredItem() end

---@public
---@return Fixing.Fixer
function Fixing:getGlobalItem() end

---@public
---@param arg0 float
---@return void
function Fixing:setConditionModifier(arg0) end

---@public
---@param arg0 IsoGameCharacter
---@return InventoryItem
function Fixing:haveGlobalItem(arg0) end

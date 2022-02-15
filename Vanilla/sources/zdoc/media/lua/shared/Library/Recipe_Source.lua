---@class Recipe.Source : zombie.scripting.objects.Recipe.Source
---@field public keep boolean
---@field private items ArrayList|Unknown
---@field public destroy boolean
---@field public count float
---@field public use float
Recipe_Source = {}

---@public
---@return ArrayList|Unknown
function Recipe_Source:getItems() end

---@public
---@return boolean
function Recipe_Source:isDestroy() end

---@public
---@param arg0 float
---@return void
function Recipe_Source:setCount(arg0) end

---@public
---@return float
function Recipe_Source:getCount() end

---@public
---@param arg0 boolean
---@return void
function Recipe_Source:setDestroy(arg0) end

---@public
---@param arg0 boolean
---@return void
function Recipe_Source:setKeep(arg0) end

---@public
---@param arg0 float
---@return void
function Recipe_Source:setUse(arg0) end

---@public
---@return String
function Recipe_Source:getOnlyItem() end

---@public
---@return float
function Recipe_Source:getUse() end

---@public
---@return boolean
function Recipe_Source:isKeep() end

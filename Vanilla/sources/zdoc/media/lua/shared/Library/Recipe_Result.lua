---@class Recipe.Result : zombie.scripting.objects.Recipe.Result
---@field public module String
---@field public type String
---@field public count int
---@field public drainableCount int
Recipe_Result = {}

---@public
---@return int
function Recipe_Result:getDrainableCount() end

---@public
---@return String
function Recipe_Result:getType() end

---@public
---@param arg0 int
---@return void
function Recipe_Result:setDrainableCount(arg0) end

---@public
---@param arg0 String
---@return void
function Recipe_Result:setModule(arg0) end

---@public
---@param arg0 int
---@return void
function Recipe_Result:setCount(arg0) end

---@public
---@return String
function Recipe_Result:getFullType() end

---@public
---@param arg0 String
---@return void
function Recipe_Result:setType(arg0) end

---@public
---@return int
function Recipe_Result:getCount() end

---@public
---@return String
function Recipe_Result:getModule() end

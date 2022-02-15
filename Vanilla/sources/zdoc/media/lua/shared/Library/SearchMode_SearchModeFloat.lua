---@class SearchMode.SearchModeFloat : zombie.iso.SearchMode.SearchModeFloat
---@field private min float
---@field private max float
---@field private stepsize float
---@field private exterior float
---@field private targetExterior float
---@field private interior float
---@field private targetInterior float
SearchMode_SearchModeFloat = {}

---@public
---@return float
function SearchMode_SearchModeFloat:getTargetInterior() end

---@public
---@param arg0 float
---@return void
function SearchMode_SearchModeFloat:setExterior(arg0) end

---@public
---@return void
function SearchMode_SearchModeFloat:equalise() end

---@public
---@param arg0 float
---@return void
function SearchMode_SearchModeFloat:setInterior(arg0) end

---@public
---@param arg0 float
---@return void
function SearchMode_SearchModeFloat:setTargetInterior(arg0) end

---@public
---@return float
function SearchMode_SearchModeFloat:getExterior() end

---@public
---@return float
function SearchMode_SearchModeFloat:getMin() end

---@public
---@param arg0 float
---@return void
function SearchMode_SearchModeFloat:update(arg0) end

---@public
---@return float
function SearchMode_SearchModeFloat:getTargetExterior() end

---@public
---@param arg0 float
---@param arg1 float
---@return void
function SearchMode_SearchModeFloat:setTargets(arg0, arg1) end

---@public
---@return void
function SearchMode_SearchModeFloat:reset() end

---@public
---@return float
function SearchMode_SearchModeFloat:getInterior() end

---@public
---@param arg0 float
---@return void
function SearchMode_SearchModeFloat:setTargetExterior(arg0) end

---@public
---@param arg0 float
---@param arg1 float
---@param arg2 float
---@param arg3 float
---@return void
function SearchMode_SearchModeFloat:set(arg0, arg1, arg2, arg3) end

---@public
---@return float
function SearchMode_SearchModeFloat:getMax() end

---@public
---@return float
function SearchMode_SearchModeFloat:getStepsize() end

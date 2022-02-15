---@class IsoPuddles.PuddlesFloat : zombie.iso.IsoPuddles.PuddlesFloat
---@field protected finalValue float
---@field private isAdminOverride boolean
---@field private adminValue float
---@field private min float
---@field private max float
---@field private delta float
---@field private ID int
---@field private name String
IsoPuddles_PuddlesFloat = {}

---@public
---@param arg0 float
---@return void
function IsoPuddles_PuddlesFloat:interpolateFinalValue(arg0) end

---@public
---@return float
function IsoPuddles_PuddlesFloat:getMin() end

---@public
---@return float
function IsoPuddles_PuddlesFloat:getAdminValue() end

---@public
---@return float
function IsoPuddles_PuddlesFloat:getMax() end

---@public
---@param arg0 float
---@param arg1 float
---@return void
function IsoPuddles_PuddlesFloat:addFinalValueForMax(arg0, arg1) end

---@public
---@param arg0 int
---@param arg1 String
---@return IsoPuddles.PuddlesFloat
function IsoPuddles_PuddlesFloat:init(arg0, arg1) end

---@public
---@param arg0 boolean
---@return void
function IsoPuddles_PuddlesFloat:setEnableAdmin(arg0) end

---@public
---@param arg0 float
---@return void
function IsoPuddles_PuddlesFloat:setFinalValue(arg0) end

---@public
---@return boolean
function IsoPuddles_PuddlesFloat:isEnableAdmin() end

---@public
---@return int
function IsoPuddles_PuddlesFloat:getID() end

---@public
---@param arg0 float
---@return void
function IsoPuddles_PuddlesFloat:addFinalValue(arg0) end

---@public
---@return float
function IsoPuddles_PuddlesFloat:getFinalValue() end

---@private
---@return void
function IsoPuddles_PuddlesFloat:calculate() end

---@public
---@return String
function IsoPuddles_PuddlesFloat:getName() end

---@public
---@param arg0 float
---@return void
function IsoPuddles_PuddlesFloat:setAdminValue(arg0) end

---@class UITransition : zombie.ui.UITransition
---@field private duration float
---@field private elapsed float
---@field private frac float
---@field private fadeOut boolean
---@field private bIgnoreUpdateTime boolean
---@field private updateTimeMS long
---@field private currentTimeMS long
---@field private elapsedTimeMS long
UITransition = {}

---@public
---@param arg0 float
---@param arg1 boolean
---@return void
function UITransition:init(arg0, arg1) end

---@public
---@param arg0 float
---@return void
function UITransition:setElapsed(arg0) end

---@public
---@param arg0 boolean
---@return void
function UITransition:setIgnoreUpdateTime(arg0) end

---@public
---@return void
function UITransition:UpdateAll() end

---@public
---@return void
function UITransition:update() end

---@public
---@return void
function UITransition:reset() end

---@public
---@return float
function UITransition:fraction() end

---@public
---@return float
function UITransition:getElapsed() end

---@public
---@param arg0 boolean
---@return void
function UITransition:setFadeIn(arg0) end

---@class ZombieSittingState : zombie.ai.states.ZombieSittingState
---@field private _instance ZombieSittingState
ZombieSittingState = {}

---@public
---@param arg0 IsoGameCharacter
---@return void
function ZombieSittingState:enter(arg0) end

---@public
---@return ZombieSittingState
function ZombieSittingState:instance() end

---@public
---@param arg0 IsoGameCharacter
---@return void
function ZombieSittingState:execute(arg0) end

---@public
---@param arg0 IsoGameCharacter
---@return void
function ZombieSittingState:exit(arg0) end

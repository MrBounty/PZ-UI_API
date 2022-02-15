---@class ZombieFallDownState : zombie.ai.states.ZombieFallDownState
---@field private _instance ZombieFallDownState
ZombieFallDownState = {}

---@public
---@param arg0 IsoGameCharacter
---@return void
function ZombieFallDownState:enter(arg0) end

---@public
---@param arg0 IsoGameCharacter
---@return void
function ZombieFallDownState:execute(arg0) end

---@public
---@return ZombieFallDownState
function ZombieFallDownState:instance() end

---@public
---@param arg0 IsoGameCharacter
---@return void
function ZombieFallDownState:exit(arg0) end

---@public
---@param arg0 IsoGameCharacter
---@param arg1 AnimEvent
---@return void
function ZombieFallDownState:animEvent(arg0, arg1) end

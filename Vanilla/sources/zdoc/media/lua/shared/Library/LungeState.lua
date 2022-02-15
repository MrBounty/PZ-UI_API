---@class LungeState : zombie.ai.states.LungeState
---@field private _instance LungeState
---@field private temp JVector2
---@field private PARAM_TICK_COUNT Integer
LungeState = {}

---@public
---@param arg0 IsoGameCharacter
---@return boolean
function LungeState:isMoving(arg0) end

---Overrides:
---
---execute in class State
---@public
---@param owner IsoGameCharacter
---@return void
function LungeState:execute(owner) end

---@public
---@param arg0 IsoGameCharacter
---@return void
function LungeState:enter(arg0) end

---@public
---@return LungeState
function LungeState:instance() end

---@public
---@param arg0 IsoGameCharacter
---@return void
function LungeState:exit(arg0) end

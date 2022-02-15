---@class WalkTowardState : zombie.ai.states.WalkTowardState
---@field private _instance WalkTowardState
---@field private PARAM_IGNORE_OFFSET Integer
---@field private PARAM_IGNORE_TIME Integer
---@field private PARAM_TICK_COUNT Integer
---@field private temp JVector2
---@field private worldPos Vector3f
WalkTowardState = {}

---Overrides:
---
---execute in class State
---@public
---@param owner IsoGameCharacter
---@return void
function WalkTowardState:execute(owner) end

---@private
---@param arg0 IsoGameCharacter
---@param arg1 float
---@param arg2 float
---@param arg3 float
---@return boolean
function WalkTowardState:isPathClear(arg0, arg1, arg2, arg3) end

---@public
---@param arg0 IsoGameCharacter
---@return boolean
function WalkTowardState:isMoving(arg0) end

---Overrides:
---
---enter in class State
---@public
---@param owner IsoGameCharacter
---@return void
function WalkTowardState:enter(owner) end

---@public
---@param arg0 IsoZombie
---@param arg1 JVector2
---@return boolean
function WalkTowardState:calculateTargetLocation(arg0, arg1) end

---@public
---@return WalkTowardState
function WalkTowardState:instance() end

---@public
---@param arg0 IsoGameCharacter
---@return void
function WalkTowardState:exit(arg0) end

---@public
---@param arg0 IsoGameCharacter
---@param arg1 AnimEvent
---@return void
function WalkTowardState:animEvent(arg0, arg1) end

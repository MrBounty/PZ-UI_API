---@class ThumpState : zombie.ai.states.ThumpState
---@field private _instance ThumpState
ThumpState = {}

---@public
---@param arg0 IsoGameCharacter
---@param arg1 AnimEvent
---@return void
function ThumpState:animEvent(arg0, arg1) end

---@private
---@param arg0 int
---@param arg1 int
---@param arg2 int
---@param arg3 int
---@param arg4 int
---@return IsoPlayer
function ThumpState:findPlayer(arg0, arg1, arg2, arg3, arg4) end

---@public
---@return int
function ThumpState:getFastForwardDamageMultiplier() end

---@private
---@param arg0 IsoGameCharacter
---@param arg1 Thumpable
---@return boolean
function ThumpState:isThumpTargetValid(arg0, arg1) end

---Overrides:
---
---exit in class State
---@public
---@param owner IsoGameCharacter
---@return void
function ThumpState:exit(owner) end

---@private
---@param arg0 IsoZombie
---@param arg1 IsoGridSquare
---@param arg2 IsoGridSquare
---@return boolean
function ThumpState:lungeThroughDoor(arg0, arg1, arg2) end

---Overrides:
---
---enter in class State
---@public
---@param owner IsoGameCharacter
---@return void
function ThumpState:enter(owner) end

---Overrides:
---
---execute in class State
---@public
---@param owner IsoGameCharacter
---@return void
function ThumpState:execute(owner) end

---@public
---@return ThumpState
function ThumpState:instance() end

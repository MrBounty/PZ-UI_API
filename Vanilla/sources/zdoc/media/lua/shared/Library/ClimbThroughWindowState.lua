---@class ClimbThroughWindowState : zombie.ai.states.ClimbThroughWindowState
---@field private _instance ClimbThroughWindowState
---@field PARAM_START_X Integer
---@field PARAM_START_Y Integer
---@field PARAM_Z Integer
---@field PARAM_OPPOSITE_X Integer
---@field PARAM_OPPOSITE_Y Integer
---@field PARAM_DIR Integer
---@field PARAM_ZOMBIE_ON_FLOOR Integer
---@field PARAM_PREV_STATE Integer
---@field PARAM_SCRATCH Integer
---@field PARAM_COUNTER Integer
---@field PARAM_SOLID_FLOOR Integer
---@field PARAM_SHEET_ROPE Integer
---@field PARAM_END_X Integer
---@field PARAM_END_Y Integer
ClimbThroughWindowState = {}

---@public
---@param arg0 IsoGameCharacter
---@param arg1 int
---@param arg2 int
---@param arg3 IsoDirections
---@return boolean
function ClimbThroughWindowState:isPastInnerEdgeOfSquare(arg0, arg1, arg2, arg3) end

---@private
---@param arg0 IsoGridSquare
---@return boolean
function ClimbThroughWindowState:isObstacleSquare(arg0) end

---@public
---@param arg0 IsoGameCharacter
---@param arg1 int
---@param arg2 int
---@param arg3 int
---@param arg4 int
---@param arg5 int
---@param arg6 int
---@return boolean
function ClimbThroughWindowState:isIgnoreCollide(arg0, arg1, arg2, arg3, arg4, arg5, arg6) end

---Overrides:
---
---exit in class State
---@public
---@param owner IsoGameCharacter
---@return void
function ClimbThroughWindowState:exit(owner) end

---@public
---@param arg0 IsoGameCharacter
---@param arg1 AnimEvent
---@return void
function ClimbThroughWindowState:animEvent(arg0, arg1) end

---Overrides:
---
---execute in class State
---@public
---@param owner IsoGameCharacter
---@return void
function ClimbThroughWindowState:execute(owner) end

---@public
---@param arg0 IsoGameCharacter
---@param arg1 int
---@param arg2 int
---@param arg3 IsoDirections
---@return boolean
function ClimbThroughWindowState:isPastOuterEdgeOfSquare(arg0, arg1, arg2, arg3) end

---Overrides:
---
---enter in class State
---@public
---@param owner IsoGameCharacter
---@return void
function ClimbThroughWindowState:enter(owner) end

---@public
---@param arg0 IsoGameCharacter
---@param arg1 MoveDeltaModifiers
---@return void
function ClimbThroughWindowState:getDeltaModifiers(arg0, arg1) end

---@public
---@param arg0 IsoGameCharacter
---@param arg1 float
---@return void
function ClimbThroughWindowState:slideX(arg0, arg1) end

---@public
---@param arg0 IsoGameCharacter
---@param arg1 IsoObject
---@return void
function ClimbThroughWindowState:setParams(arg0, arg1) end

---@private
---@param arg0 IsoGridSquare
---@param arg1 IsoGameCharacter
---@return void
function ClimbThroughWindowState:checkForFallingFront(arg0, arg1) end

---@private
---@param arg0 IsoGridSquare
---@param arg1 IsoGameCharacter
---@return void
function ClimbThroughWindowState:checkForFallingBack(arg0, arg1) end

---@private
---@param arg0 IsoZombie
---@return void
function ClimbThroughWindowState:setLungeXVars(arg0) end

---@private
---@param arg0 IsoGridSquare
---@param arg1 IsoDirections
---@return IsoGridSquare
function ClimbThroughWindowState:getFreeSquareAfterObstacles(arg0, arg1) end

---@private
---@param arg0 IsoGridSquare
---@return boolean
function ClimbThroughWindowState:isFreeSquare(arg0) end

---@public
---@param arg0 IsoGameCharacter
---@return boolean
function ClimbThroughWindowState:isWindowClosing(arg0) end

---@public
---@param arg0 IsoGameCharacter
---@param arg1 float
---@return void
function ClimbThroughWindowState:slideY(arg0, arg1) end

---@public
---@return ClimbThroughWindowState
function ClimbThroughWindowState:instance() end

---@public
---@param arg0 IsoGameCharacter
---@return IsoObject
function ClimbThroughWindowState:getWindow(arg0) end

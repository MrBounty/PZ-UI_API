---@class ClimbOverFenceState : zombie.ai.states.ClimbOverFenceState
---@field private _instance ClimbOverFenceState
---@field PARAM_START_X Integer
---@field PARAM_START_Y Integer
---@field PARAM_Z Integer
---@field PARAM_END_X Integer
---@field PARAM_END_Y Integer
---@field PARAM_DIR Integer
---@field PARAM_ZOMBIE_ON_FLOOR Integer
---@field PARAM_PREV_STATE Integer
---@field PARAM_SCRATCH Integer
---@field PARAM_COUNTER Integer
---@field PARAM_SOLID_FLOOR Integer
---@field PARAM_SHEET_ROPE Integer
---@field PARAM_RUN Integer
---@field PARAM_SPRINT Integer
---@field PARAM_COLLIDABLE Integer
---@field FENCE_TYPE_WOOD int
---@field FENCE_TYPE_METAL int
---@field FENCE_TYPE_SANDBAG int
---@field FENCE_TYPE_GRAVELBAG int
---@field FENCE_TYPE_BARBWIRE int
---@field TRIP_WOOD int
---@field TRIP_METAL int
---@field TRIP_SANDBAG int
---@field TRIP_GRAVELBAG int
---@field TRIP_BARBWIRE int
---@field public TRIP_TREE int
---@field public TRIP_ZOMBIE int
---@field public COLLIDE_WITH_WALL int
ClimbOverFenceState = {}

---@private
---@param arg0 IsoZombie
---@return void
function ClimbOverFenceState:setLungeXVars(arg0) end

---@private
---@param arg0 IsoGameCharacter
---@param arg1 float
---@return void
function ClimbOverFenceState:slideY(arg0, arg1) end

---@private
---@param arg0 IsoObject
---@return int
function ClimbOverFenceState:getTripType(arg0) end

---@private
---@param arg0 IsoObject
---@return int
---@overload fun(arg0:IsoObject, arg1:IsoGridSquare)
function ClimbOverFenceState:countZombiesClimbingOver(arg0) end

---@private
---@param arg0 IsoObject
---@param arg1 IsoGridSquare
---@return int
function ClimbOverFenceState:countZombiesClimbingOver(arg0, arg1) end

---@private
---@param arg0 IsoObject
---@return int
function ClimbOverFenceState:getFenceType(arg0) end

---@public
---@param arg0 IsoGameCharacter
---@param arg1 IsoDirections
---@return void
function ClimbOverFenceState:setParams(arg0, arg1) end

---@private
---@param arg0 IsoObject
---@return boolean
function ClimbOverFenceState:isMetalFence(arg0) end

---Overrides:
---
---enter in class State
---@public
---@param owner IsoGameCharacter
---@return void
function ClimbOverFenceState:enter(owner) end

---Overrides:
---
---exit in class State
---@public
---@param owner IsoGameCharacter
---@return void
function ClimbOverFenceState:exit(owner) end

---@public
---@param arg0 IsoGameCharacter
---@param arg1 MoveDeltaModifiers
---@return void
function ClimbOverFenceState:getDeltaModifiers(arg0, arg1) end

---@private
---@param arg0 IsoGameCharacter
---@return IsoObject
function ClimbOverFenceState:getFence(arg0) end

---@public
---@return ClimbOverFenceState
function ClimbOverFenceState:instance() end

---@public
---@param arg0 IsoGameCharacter
---@param arg1 AnimEvent
---@return void
function ClimbOverFenceState:animEvent(arg0, arg1) end

---Overrides:
---
---execute in class State
---@public
---@param owner IsoGameCharacter
---@return void
function ClimbOverFenceState:execute(owner) end

---@public
---@param arg0 IsoGameCharacter
---@param arg1 int
---@param arg2 int
---@param arg3 int
---@param arg4 int
---@param arg5 int
---@param arg6 int
---@return boolean
function ClimbOverFenceState:isIgnoreCollide(arg0, arg1, arg2, arg3, arg4, arg5, arg6) end

---@private
---@param arg0 IsoGameCharacter
---@param arg1 float
---@return void
function ClimbOverFenceState:slideX(arg0, arg1) end

---@private
---@param arg0 IsoGameCharacter
---@return boolean
function ClimbOverFenceState:shouldFallAfterVaultOver(arg0) end

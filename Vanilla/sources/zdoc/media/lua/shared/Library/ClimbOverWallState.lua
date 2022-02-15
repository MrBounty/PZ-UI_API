---@class ClimbOverWallState : zombie.ai.states.ClimbOverWallState
---@field private _instance ClimbOverWallState
---@field PARAM_START_X Integer
---@field PARAM_START_Y Integer
---@field PARAM_Z Integer
---@field PARAM_END_X Integer
---@field PARAM_END_Y Integer
---@field PARAM_DIR Integer
---@field FENCE_TYPE_WOOD int
---@field FENCE_TYPE_METAL int
ClimbOverWallState = {}

---@private
---@param arg0 IsoGridSquare
---@return IsoObject
function ClimbOverWallState:getClimbableWallN(arg0) end

---@public
---@param arg0 IsoGameCharacter
---@param arg1 IsoDirections
---@return void
function ClimbOverWallState:setParams(arg0, arg1) end

---@private
---@param arg0 IsoGridSquare
---@return IsoObject
function ClimbOverWallState:getClimbableWallW(arg0) end

---@public
---@param arg0 IsoGameCharacter
---@param arg1 AnimEvent
---@return void
function ClimbOverWallState:animEvent(arg0, arg1) end

---@public
---@param arg0 IsoGameCharacter
---@param arg1 int
---@param arg2 int
---@param arg3 int
---@param arg4 int
---@param arg5 int
---@param arg6 int
---@return boolean
function ClimbOverWallState:isIgnoreCollide(arg0, arg1, arg2, arg3, arg4, arg5, arg6) end

---@public
---@param arg0 IsoGameCharacter
---@return void
function ClimbOverWallState:execute(arg0) end

---@private
---@param arg0 IsoObject
---@return int
function ClimbOverWallState:getFenceType(arg0) end

---@public
---@param arg0 IsoGameCharacter
---@return void
function ClimbOverWallState:enter(arg0) end

---@public
---@return ClimbOverWallState
function ClimbOverWallState:instance() end

---@private
---@param arg0 IsoGameCharacter
---@return IsoObject
function ClimbOverWallState:getFence(arg0) end

---@public
---@param arg0 IsoGameCharacter
---@return void
function ClimbOverWallState:exit(arg0) end

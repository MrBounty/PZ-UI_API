---@class PathFindState2 : zombie.vehicles.PathFindState2
---@field private PARAM_TICK_COUNT Integer
PathFindState2 = {}

---@public
---@param arg0 IsoGameCharacter
---@return void
function PathFindState2:enter(arg0) end

---@public
---@param arg0 IsoGameCharacter
---@return boolean
function PathFindState2:isMoving(arg0) end

---@public
---@param arg0 IsoGameCharacter
---@param arg1 AnimEvent
---@return void
function PathFindState2:animEvent(arg0, arg1) end

---@public
---@param arg0 IsoGameCharacter
---@return void
function PathFindState2:exit(arg0) end

---@public
---@param arg0 IsoGameCharacter
---@return void
function PathFindState2:execute(arg0) end

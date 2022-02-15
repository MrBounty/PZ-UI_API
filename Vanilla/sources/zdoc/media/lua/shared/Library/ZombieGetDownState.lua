---@class ZombieGetDownState : zombie.ai.states.ZombieGetDownState
---@field private _instance ZombieGetDownState
---@field PARAM_PREV_STATE Integer
---@field PARAM_WAIT_TIME Integer
---@field PARAM_START_X Integer
---@field PARAM_START_Y Integer
ZombieGetDownState = {}

---@public
---@param arg0 IsoGameCharacter
---@return void
function ZombieGetDownState:exit(arg0) end

---@public
---@param arg0 IsoGameCharacter
---@param arg1 AnimEvent
---@return void
function ZombieGetDownState:animEvent(arg0, arg1) end

---@public
---@return ZombieGetDownState
function ZombieGetDownState:instance() end

---@public
---@param arg0 IsoGameCharacter
---@return void
function ZombieGetDownState:setParams(arg0) end

---@public
---@param arg0 IsoGameCharacter
---@return boolean
function ZombieGetDownState:isNearStartXY(arg0) end

---@public
---@param arg0 IsoGameCharacter
---@return void
function ZombieGetDownState:enter(arg0) end

---@public
---@param arg0 IsoGameCharacter
---@return void
function ZombieGetDownState:execute(arg0) end

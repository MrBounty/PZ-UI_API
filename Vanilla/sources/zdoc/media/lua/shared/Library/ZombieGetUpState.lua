---@class ZombieGetUpState : zombie.ai.states.ZombieGetUpState
---@field private _instance ZombieGetUpState
---@field PARAM_STANDING Integer
---@field PARAM_PREV_STATE Integer
ZombieGetUpState = {}

---@public
---@return ZombieGetUpState
function ZombieGetUpState:instance() end

---@public
---@param arg0 IsoGameCharacter
---@return void
function ZombieGetUpState:execute(arg0) end

---@public
---@param arg0 IsoGameCharacter
---@return void
function ZombieGetUpState:enter(arg0) end

---@public
---@param arg0 IsoGameCharacter
---@param arg1 AnimEvent
---@return void
function ZombieGetUpState:animEvent(arg0, arg1) end

---@public
---@param arg0 IsoGameCharacter
---@return void
function ZombieGetUpState:exit(arg0) end

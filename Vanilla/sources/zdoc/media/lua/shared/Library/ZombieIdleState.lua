---@class ZombieIdleState : zombie.ai.states.ZombieIdleState
---@field private _instance ZombieIdleState
---@field private PARAM_TICK_COUNT Integer
ZombieIdleState = {}

---@public
---@param arg0 IsoGameCharacter
---@return void
function ZombieIdleState:exit(arg0) end

---@private
---@return float
function ZombieIdleState:pickRandomWanderInterval() end

---@public
---@param arg0 IsoGameCharacter
---@return void
function ZombieIdleState:execute(arg0) end

---@public
---@param arg0 IsoGameCharacter
---@param arg1 AnimEvent
---@return void
function ZombieIdleState:animEvent(arg0, arg1) end

---@public
---@return ZombieIdleState
function ZombieIdleState:instance() end

---@public
---@param arg0 IsoGameCharacter
---@return void
function ZombieIdleState:enter(arg0) end

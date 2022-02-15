---@class ZombieReanimateState : zombie.ai.states.ZombieReanimateState
---@field private _instance ZombieReanimateState
ZombieReanimateState = {}

---@public
---@param arg0 IsoGameCharacter
---@param arg1 AnimEvent
---@return void
function ZombieReanimateState:animEvent(arg0, arg1) end

---@public
---@param arg0 IsoGameCharacter
---@return void
function ZombieReanimateState:execute(arg0) end

---@public
---@param arg0 IsoGameCharacter
---@return void
function ZombieReanimateState:enter(arg0) end

---@public
---@param arg0 IsoGameCharacter
---@return void
function ZombieReanimateState:exit(arg0) end

---@public
---@return ZombieReanimateState
function ZombieReanimateState:instance() end

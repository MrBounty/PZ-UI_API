---@class FakeDeadAttackState : zombie.ai.states.FakeDeadAttackState
---@field private _instance FakeDeadAttackState
FakeDeadAttackState = {}

---@public
---@param arg0 IsoGameCharacter
---@return void
function FakeDeadAttackState:exit(arg0) end

---@public
---@param arg0 IsoGameCharacter
---@param arg1 AnimEvent
---@return void
function FakeDeadAttackState:animEvent(arg0, arg1) end

---@public
---@param arg0 IsoGameCharacter
---@return void
function FakeDeadAttackState:execute(arg0) end

---@public
---@return FakeDeadAttackState
function FakeDeadAttackState:instance() end

---@public
---@param arg0 IsoGameCharacter
---@return void
function FakeDeadAttackState:enter(arg0) end

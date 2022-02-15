---@class FakeDeadZombieState : zombie.ai.states.FakeDeadZombieState
---@field private _instance FakeDeadZombieState
FakeDeadZombieState = {}

---@public
---@return FakeDeadZombieState
function FakeDeadZombieState:instance() end

---Overrides:
---
---exit in class State
---@public
---@param owner IsoGameCharacter
---@return void
function FakeDeadZombieState:exit(owner) end

---Overrides:
---
---execute in class State
---@public
---@param owner IsoGameCharacter
---@return void
function FakeDeadZombieState:execute(owner) end

---Overrides:
---
---enter in class State
---@public
---@param owner IsoGameCharacter
---@return void
function FakeDeadZombieState:enter(owner) end

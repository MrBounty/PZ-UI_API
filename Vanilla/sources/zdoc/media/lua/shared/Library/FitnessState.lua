---@class FitnessState : zombie.ai.states.FitnessState
---@field private _instance FitnessState
FitnessState = {}

---@public
---@param arg0 IsoGameCharacter
---@return void
function FitnessState:exit(arg0) end

---@public
---@param arg0 IsoGameCharacter
---@param arg1 AnimEvent
---@return void
function FitnessState:animEvent(arg0, arg1) end

---@public
---@return FitnessState
function FitnessState:instance() end

---@public
---@param arg0 IsoGameCharacter
---@return void
function FitnessState:enter(arg0) end

---@public
---@param arg0 IsoGameCharacter
---@return void
function FitnessState:execute(arg0) end

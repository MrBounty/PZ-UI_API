---@class FishingState : zombie.ai.states.FishingState
---@field private _instance FishingState
---@field pauseTime float
---@field private stage String
FishingState = {}

---@public
---@return FishingState
function FishingState:instance() end

---@public
---@param arg0 IsoGameCharacter
---@param arg1 AnimEvent
---@return void
function FishingState:animEvent(arg0, arg1) end

---@public
---@param arg0 IsoGameCharacter
---@return void
function FishingState:execute(arg0) end

---@public
---@param arg0 IsoGameCharacter
---@return void
function FishingState:enter(arg0) end

---@public
---@param arg0 IsoGameCharacter
---@return void
function FishingState:exit(arg0) end

---@class PlayerEmoteState : zombie.ai.states.PlayerEmoteState
---@field private _instance PlayerEmoteState
PlayerEmoteState = {}

---@public
---@param arg0 IsoGameCharacter
---@return void
function PlayerEmoteState:exit(arg0) end

---@public
---@param arg0 IsoGameCharacter
---@param arg1 AnimEvent
---@return void
function PlayerEmoteState:animEvent(arg0, arg1) end

---@public
---@param arg0 IsoGameCharacter
---@return void
function PlayerEmoteState:execute(arg0) end

---@public
---@return boolean
function PlayerEmoteState:isDoingActionThatCanBeCancelled() end

---@public
---@return PlayerEmoteState
function PlayerEmoteState:instance() end

---@public
---@param arg0 IsoGameCharacter
---@return void
function PlayerEmoteState:enter(arg0) end

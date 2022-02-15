---@class PlayerActionsState : zombie.ai.states.PlayerActionsState
---@field private _instance PlayerActionsState
PlayerActionsState = {}

---@public
---@param arg0 IsoGameCharacter
---@return void
function PlayerActionsState:exit(arg0) end

---@public
---@return PlayerActionsState
function PlayerActionsState:instance() end

---@public
---@param arg0 IsoGameCharacter
---@return void
function PlayerActionsState:enter(arg0) end

---@public
---@param arg0 IsoGameCharacter
---@return void
function PlayerActionsState:execute(arg0) end

---@public
---@param arg0 IsoGameCharacter
---@param arg1 AnimEvent
---@return void
function PlayerActionsState:animEvent(arg0, arg1) end

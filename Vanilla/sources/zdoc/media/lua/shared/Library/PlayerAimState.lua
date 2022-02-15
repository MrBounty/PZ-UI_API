---@class PlayerAimState : zombie.ai.states.PlayerAimState
---@field private _instance PlayerAimState
PlayerAimState = {}

---@public
---@param arg0 IsoGameCharacter
---@param arg1 AnimEvent
---@return void
function PlayerAimState:animEvent(arg0, arg1) end

---@public
---@param arg0 IsoGameCharacter
---@return void
function PlayerAimState:exit(arg0) end

---@public
---@return PlayerAimState
function PlayerAimState:instance() end

---@public
---@param arg0 IsoGameCharacter
---@return void
function PlayerAimState:enter(arg0) end

---@public
---@param arg0 IsoGameCharacter
---@return void
function PlayerAimState:execute(arg0) end

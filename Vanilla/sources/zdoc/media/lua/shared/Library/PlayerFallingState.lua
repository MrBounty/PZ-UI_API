---@class PlayerFallingState : zombie.ai.states.PlayerFallingState
---@field private _instance PlayerFallingState
PlayerFallingState = {}

---@public
---@param arg0 IsoGameCharacter
---@return void
function PlayerFallingState:execute(arg0) end

---@public
---@return PlayerFallingState
function PlayerFallingState:instance() end

---@public
---@param arg0 IsoGameCharacter
---@return void
function PlayerFallingState:exit(arg0) end

---@public
---@param arg0 IsoGameCharacter
---@return void
function PlayerFallingState:enter(arg0) end

---@class PlayerFallDownState : zombie.ai.states.PlayerFallDownState
---@field private _instance PlayerFallDownState
PlayerFallDownState = {}

---@public
---@param arg0 IsoGameCharacter
---@return void
function PlayerFallDownState:enter(arg0) end

---@public
---@return PlayerFallDownState
function PlayerFallDownState:instance() end

---@public
---@param arg0 IsoGameCharacter
---@return void
function PlayerFallDownState:exit(arg0) end

---@public
---@param arg0 IsoGameCharacter
---@return void
function PlayerFallDownState:execute(arg0) end

---@public
---@param arg0 IsoGameCharacter
---@param arg1 AnimEvent
---@return void
function PlayerFallDownState:animEvent(arg0, arg1) end

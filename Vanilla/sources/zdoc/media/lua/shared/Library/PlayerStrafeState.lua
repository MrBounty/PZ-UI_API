---@class PlayerStrafeState : zombie.ai.states.PlayerStrafeState
---@field private _instance PlayerStrafeState
PlayerStrafeState = {}

---@public
---@param arg0 IsoGameCharacter
---@return void
function PlayerStrafeState:execute(arg0) end

---@public
---@param arg0 IsoGameCharacter
---@param arg1 AnimEvent
---@return void
function PlayerStrafeState:animEvent(arg0, arg1) end

---@public
---@param arg0 IsoGameCharacter
---@return void
function PlayerStrafeState:exit(arg0) end

---@public
---@return PlayerStrafeState
function PlayerStrafeState:instance() end

---@public
---@param arg0 IsoGameCharacter
---@return void
function PlayerStrafeState:enter(arg0) end

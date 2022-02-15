---@class PlayerHitReactionPVPState : zombie.ai.states.PlayerHitReactionPVPState
---@field private _instance PlayerHitReactionPVPState
PlayerHitReactionPVPState = {}

---@public
---@param arg0 IsoGameCharacter
---@return void
function PlayerHitReactionPVPState:execute(arg0) end

---@public
---@param arg0 IsoGameCharacter
---@return void
function PlayerHitReactionPVPState:enter(arg0) end

---@public
---@return PlayerHitReactionPVPState
function PlayerHitReactionPVPState:instance() end

---@public
---@param arg0 IsoGameCharacter
---@param arg1 AnimEvent
---@return void
function PlayerHitReactionPVPState:animEvent(arg0, arg1) end

---@public
---@param arg0 IsoGameCharacter
---@return void
function PlayerHitReactionPVPState:exit(arg0) end

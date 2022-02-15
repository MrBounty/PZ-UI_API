---@class PlayerHitReactionState : zombie.ai.states.PlayerHitReactionState
---@field private _instance PlayerHitReactionState
PlayerHitReactionState = {}

---@public
---@param arg0 IsoGameCharacter
---@return void
function PlayerHitReactionState:exit(arg0) end

---@public
---@return PlayerHitReactionState
function PlayerHitReactionState:instance() end

---@public
---@param arg0 IsoGameCharacter
---@return void
function PlayerHitReactionState:enter(arg0) end

---@public
---@param arg0 IsoGameCharacter
---@return void
function PlayerHitReactionState:execute(arg0) end

---@public
---@param arg0 IsoGameCharacter
---@param arg1 AnimEvent
---@return void
function PlayerHitReactionState:animEvent(arg0, arg1) end

---@class PlayerGetUpState : zombie.ai.states.PlayerGetUpState
---@field private _instance PlayerGetUpState
PlayerGetUpState = {}

---@public
---@param arg0 IsoGameCharacter
---@return void
function PlayerGetUpState:enter(arg0) end

---@public
---@param arg0 IsoGameCharacter
---@return void
function PlayerGetUpState:execute(arg0) end

---@public
---@return PlayerGetUpState
function PlayerGetUpState:instance() end

---@public
---@param arg0 IsoGameCharacter
---@return void
function PlayerGetUpState:exit(arg0) end

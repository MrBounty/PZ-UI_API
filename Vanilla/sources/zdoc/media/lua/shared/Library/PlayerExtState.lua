---@class PlayerExtState : zombie.ai.states.PlayerExtState
---@field private _instance PlayerExtState
PlayerExtState = {}

---@public
---@param arg0 IsoGameCharacter
---@return void
function PlayerExtState:enter(arg0) end

---@public
---@param arg0 IsoGameCharacter
---@return void
function PlayerExtState:execute(arg0) end

---@public
---@param arg0 IsoGameCharacter
---@param arg1 AnimEvent
---@return void
function PlayerExtState:animEvent(arg0, arg1) end

---@public
---@param arg0 IsoGameCharacter
---@return void
function PlayerExtState:exit(arg0) end

---@public
---@return PlayerExtState
function PlayerExtState:instance() end

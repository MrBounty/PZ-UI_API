---@class PlayerSitOnGroundState : zombie.ai.states.PlayerSitOnGroundState
---@field private _instance PlayerSitOnGroundState
---@field private RAND_EXT int
---@field private PARAM_FIRE Integer
---@field private PARAM_SITGROUNDANIM Integer
---@field private PARAM_CHECK_FIRE Integer
---@field private PARAM_CHANGE_ANIM Integer
PlayerSitOnGroundState = {}

---@public
---@param arg0 IsoGameCharacter
---@return void
function PlayerSitOnGroundState:execute(arg0) end

---@public
---@param arg0 IsoGameCharacter
---@param arg1 AnimEvent
---@return void
function PlayerSitOnGroundState:animEvent(arg0, arg1) end

---@public
---@param arg0 IsoGameCharacter
---@return void
function PlayerSitOnGroundState:exit(arg0) end

---@private
---@param arg0 IsoGameCharacter
---@return boolean
function PlayerSitOnGroundState:checkFire(arg0) end

---@public
---@return PlayerSitOnGroundState
function PlayerSitOnGroundState:instance() end

---@public
---@param arg0 IsoGameCharacter
---@return void
function PlayerSitOnGroundState:enter(arg0) end

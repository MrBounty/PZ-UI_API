---@class ZombieOnGroundState : zombie.ai.states.ZombieOnGroundState
---@field private _instance ZombieOnGroundState
---@field tempVector Vector3
---@field tempVectorBonePos Vector3
ZombieOnGroundState = {}

---@public
---@param arg0 IsoGameCharacter
---@return void
function ZombieOnGroundState:exit(arg0) end

---@public
---@param arg0 IsoGameCharacter
---@param arg1 IsoGameCharacter
---@return boolean
function ZombieOnGroundState:isCharacterStandingOnOther(arg0, arg1) end

---@private
---@param arg0 IsoZombie
---@return void
function ZombieOnGroundState:becomeCorpse(arg0) end

---@public
---@param arg0 IsoGameCharacter
---@return void
function ZombieOnGroundState:enter(arg0) end

---@public
---@param arg0 IsoGameCharacter
---@return void
function ZombieOnGroundState:execute(arg0) end

---@public
---@return ZombieOnGroundState
function ZombieOnGroundState:instance() end

---@private
---@param arg0 IsoGameCharacter
---@param arg1 IsoGameCharacter
---@param arg2 int
---@param arg3 float
---@return int
function ZombieOnGroundState:DoCollisionBoneCheck(arg0, arg1, arg2, arg3) end

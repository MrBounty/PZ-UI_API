---@class GameCharacterAIBrain : zombie.ai.GameCharacterAIBrain
---@field private character IsoGameCharacter
---@field public spottedCharacters ArrayList|Unknown
---@field public StepBehaviors boolean
---@field public stance Stance
---@field public controlledByAdvancedPathfinder boolean
---@field public isInMeta boolean
---@field public BlockedMemories HashMap|Unknown|Unknown
---@field public AIFocusPoint JVector2
---@field public nextPathTarget Vector3
---@field public aiTarget IsoMovingObject
---@field public NextPathNodeInvalidated boolean
---@field public HumanControlVars AIBrainPlayerControlVars
---@field order String
---@field public teammateChasingZombies ArrayList|Unknown
---@field public chasingZombies ArrayList|Unknown
---@field public allowLongTermTick boolean
---@field public isAI boolean
---@field tempZombies ArrayList|Unknown
---@field compare IsoGameCharacter
---@field private Vectors Stack|Unknown
GameCharacterAIBrain = {}

---@public
---@param arg0 int
---@return ArrayList|Unknown
function GameCharacterAIBrain:getClosestChasingZombies(arg0) end

---@public
---@return IsoZombie
---@overload fun(arg0:boolean)
function GameCharacterAIBrain:getClosestChasingZombie() end

---@public
---@param arg0 boolean
---@return IsoZombie
function GameCharacterAIBrain:getClosestChasingZombie(arg0) end

---@public
---@return void
function GameCharacterAIBrain:update() end

---@public
---@return void
function GameCharacterAIBrain:renderlast() end

---@public
---@param arg0 String
---@return void
function GameCharacterAIBrain:setOrder(arg0) end

---@public
---@return int
function GameCharacterAIBrain:getCloseZombieCount() end

---@public
---@return IsoGameCharacter
function GameCharacterAIBrain:getCharacter() end

---@public
---@param arg0 int
---@param arg1 int
---@param arg2 int
---@param arg3 int
---@param arg4 int
---@param arg5 int
---@return boolean
function GameCharacterAIBrain:HasBlockedMemory(arg0, arg1, arg2, arg3, arg4, arg5) end

---@public
---@param arg0 int
---@param arg1 int
---@param arg2 int
---@return void
function GameCharacterAIBrain:AddBlockedMemory(arg0, arg1, arg2) end

---@public
---@param arg0 IsoPlayer
---@return void
function GameCharacterAIBrain:postUpdateHuman(arg0) end

---@public
---@return String
function GameCharacterAIBrain:getOrder() end

---@public
---@return SurvivorGroup
function GameCharacterAIBrain:getGroup() end

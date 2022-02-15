---@class CrawlingZombieTurnState : zombie.ai.states.CrawlingZombieTurnState
---@field private _instance CrawlingZombieTurnState
---@field private tempVector2_1 JVector2
---@field private tempVector2_2 JVector2
CrawlingZombieTurnState = {}

---Overrides:
---
---enter in class State
---@public
---@param owner IsoGameCharacter
---@return void
function CrawlingZombieTurnState:enter(owner) end

---@public
---@return CrawlingZombieTurnState
function CrawlingZombieTurnState:instance() end

---@public
---@param arg0 IsoGameCharacter
---@return void
function CrawlingZombieTurnState:exit(arg0) end

---@public
---@param arg0 IsoGameCharacter
---@param arg1 IsoDirections
---@return boolean
function CrawlingZombieTurnState:calculateDir(arg0, arg1) end

---@public
---@param arg0 IsoGameCharacter
---@param arg1 AnimEvent
---@return void
function CrawlingZombieTurnState:animEvent(arg0, arg1) end

---Overrides:
---
---execute in class State
---@public
---@param owner IsoGameCharacter
---@return void
function CrawlingZombieTurnState:execute(owner) end

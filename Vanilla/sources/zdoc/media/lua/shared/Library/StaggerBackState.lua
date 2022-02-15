---@class StaggerBackState : zombie.ai.states.StaggerBackState
---@field private _instance StaggerBackState
StaggerBackState = {}

---Overrides:
---
---enter in class State
---@public
---@param owner IsoGameCharacter
---@return void
function StaggerBackState:enter(owner) end

---Overrides:
---
---execute in class State
---@public
---@param owner IsoGameCharacter
---@return void
function StaggerBackState:execute(owner) end

---@public
---@return StaggerBackState
function StaggerBackState:instance() end

---Overrides:
---
---exit in class State
---@public
---@param owner IsoGameCharacter
---@return void
function StaggerBackState:exit(owner) end

---@public
---@param arg0 IsoGameCharacter
---@param arg1 AnimEvent
---@return void
function StaggerBackState:animEvent(arg0, arg1) end

---@private
---@param arg0 IsoGameCharacter
---@return float
function StaggerBackState:getMaxStaggerTime(arg0) end

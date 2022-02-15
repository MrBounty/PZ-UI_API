---@class SmashWindowState : zombie.ai.states.SmashWindowState
---@field private _instance SmashWindowState
SmashWindowState = {}

---@public
---@return boolean
function SmashWindowState:isDoingActionThatCanBeCancelled() end

---@public
---@param arg0 IsoGameCharacter
---@return void
function SmashWindowState:enter(arg0) end

---@public
---@param arg0 IsoGameCharacter
---@return void
function SmashWindowState:execute(arg0) end

---@public
---@return SmashWindowState
function SmashWindowState:instance() end

---@public
---@param arg0 IsoGameCharacter
---@return void
function SmashWindowState:exit(arg0) end

---@public
---@param arg0 IsoGameCharacter
---@param arg1 AnimEvent
---@return void
function SmashWindowState:animEvent(arg0, arg1) end

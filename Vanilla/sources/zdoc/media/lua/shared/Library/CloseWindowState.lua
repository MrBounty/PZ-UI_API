---@class CloseWindowState : zombie.ai.states.CloseWindowState
---@field private _instance CloseWindowState
CloseWindowState = {}

---@public
---@param arg0 IsoGameCharacter
---@return void
function CloseWindowState:exit(arg0) end

---@public
---@return boolean
function CloseWindowState:isDoingActionThatCanBeCancelled() end

---@public
---@param arg0 IsoGameCharacter
---@return void
function CloseWindowState:enter(arg0) end

---@public
---@param arg0 IsoGameCharacter
---@return IsoWindow
function CloseWindowState:getWindow(arg0) end

---@private
---@param arg0 IsoGameCharacter
---@param arg1 IsoWindow
---@return void
function CloseWindowState:onSuccess(arg0, arg1) end

---@public
---@return CloseWindowState
function CloseWindowState:instance() end

---@private
---@param arg0 IsoGameCharacter
---@return void
function CloseWindowState:exert(arg0) end

---@public
---@param arg0 IsoGameCharacter
---@return void
function CloseWindowState:execute(arg0) end

---@public
---@param arg0 IsoGameCharacter
---@param arg1 AnimEvent
---@return void
function CloseWindowState:animEvent(arg0, arg1) end

---@private
---@param arg0 IsoGameCharacter
---@param arg1 IsoWindow
---@return void
function CloseWindowState:onAttemptFinished(arg0, arg1) end

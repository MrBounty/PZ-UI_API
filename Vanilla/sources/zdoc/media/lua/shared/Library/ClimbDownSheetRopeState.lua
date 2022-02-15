---@class ClimbDownSheetRopeState : zombie.ai.states.ClimbDownSheetRopeState
---@field public CLIMB_DOWN_SPEED float
---@field private CLIMB_DOWN_SLOWDOWN float
---@field private _instance ClimbDownSheetRopeState
ClimbDownSheetRopeState = {}

---@public
---@return ClimbDownSheetRopeState
function ClimbDownSheetRopeState:instance() end

---Overrides:
---
---execute in class State
---@public
---@param owner IsoGameCharacter
---@return void
function ClimbDownSheetRopeState:execute(owner) end

---Overrides:
---
---enter in class State
---@public
---@param owner IsoGameCharacter
---@return void
function ClimbDownSheetRopeState:enter(owner) end

---@public
---@param arg0 IsoGameCharacter
---@return float
function ClimbDownSheetRopeState:getClimbDownSheetRopeSpeed(arg0) end

---@public
---@param arg0 IsoGameCharacter
---@return void
function ClimbDownSheetRopeState:exit(arg0) end

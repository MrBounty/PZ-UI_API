---@class ClimbSheetRopeState : zombie.ai.states.ClimbSheetRopeState
---@field public CLIMB_SPEED float
---@field private CLIMB_SLOWDOWN float
---@field private _instance ClimbSheetRopeState
ClimbSheetRopeState = {}

---@public
---@return ClimbSheetRopeState
function ClimbSheetRopeState:instance() end

---Overrides:
---
---enter in class State
---@public
---@param owner IsoGameCharacter
---@return void
function ClimbSheetRopeState:enter(owner) end

---@public
---@param arg0 IsoGameCharacter
---@return float
function ClimbSheetRopeState:getClimbSheetRopeSpeed(arg0) end

---@public
---@param arg0 IsoGameCharacter
---@return void
function ClimbSheetRopeState:exit(arg0) end

---Overrides:
---
---execute in class State
---@public
---@param owner IsoGameCharacter
---@return void
function ClimbSheetRopeState:execute(owner) end

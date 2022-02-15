---@class SpeedControls : zombie.ui.SpeedControls
---@field public instance SpeedControls
---@field public CurrentSpeed int
---@field public SpeedBeforePause int
---@field public MultiBeforePause float
---@field alpha float
---@field MouseOver boolean
---@field public Play HUDButton
---@field public Pause HUDButton
---@field public FastForward HUDButton
---@field public FasterForward HUDButton
---@field public Wait HUDButton
SpeedControls = {}

---@public
---@param arg0 double
---@param arg1 double
---@return Boolean
function SpeedControls:onMouseMove(arg0, arg1) end

---@public
---@param NewSpeed int
---@return void
function SpeedControls:SetCurrentGameSpeed(NewSpeed) end

---Overrides:
---
---render in class UIElement
---@public
---@return void
function SpeedControls:render() end

---Overrides:
---
---update in class UIElement
---@public
---@return void
function SpeedControls:update() end

---Overrides:
---
---ButtonClicked in class UIElement
---@public
---@param name String
---@return void
function SpeedControls:ButtonClicked(name) end

---@public
---@return void
function SpeedControls:SetCorrectIconStates() end

---@public
---@param arg0 double
---@param arg1 double
---@return void
function SpeedControls:onMouseMoveOutside(arg0, arg1) end

---@public
---@return int
function SpeedControls:getCurrentGameSpeed() end

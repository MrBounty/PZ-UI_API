--***********************************************************
--**                    THE INDIE STONE                    **
--***********************************************************

require 'ISUI/Maps/Editor/WorldMapEditorMode'

local FONT_HGT_SMALL = getTextManager():getFontHeight(UIFont.Small)
local FONT_HGT_MEDIUM = getTextManager():getFontHeight(UIFont.Medium)

---@class WorldMapEditorMode_Bounds : WorldMapEditorMode
WorldMapEditorMode_Bounds = WorldMapEditorMode:derive("WorldMapEditorMode_Bounds")

-- WorldMap bounds includes the bottom and right edges.
-- Offset WorldMapResizer bounds by this amount.
local function DXY() return 1 end

function WorldMapEditorMode_Bounds:createChildren()
	local buttonHgt = FONT_HGT_MEDIUM + 8

	local label = ISLabel:new(10, 80, FONT_HGT_MEDIUM, "x,y,x,y", 1, 1, 1, 1.0, UIFont.Medium, true)
	label:setColor(0, 0, 0)
	self:addChild(label)
	self.labelBounds = label

	local button = ISButton:new(10, label:getBottom() + 10, 80, buttonHgt, "DRAW NEW BOUNDS", self, self.onDrawBounds)
	self:addChild(button)

	button = ISButton:new(10, button:getBottom() + 10, 80, buttonHgt, "RESET", self, self.onReset)
	self:addChild(button)

	self.snapButtons = {}

	button = ISButton:new(10, button:getBottom() + 10, 80, buttonHgt, "SNAP TO CELL", self, self.onChangeSnapMode)
	button.internal = "cell"
	button.textColor.a = 0.5
	self:addChild(button)
	self.snapButtons.cell = button

	button = ISButton:new(10, button:getBottom() + 10, 80, buttonHgt, "SNAP TO CHUNK", self, self.onChangeSnapMode)
	button.internal = "chunk"
--	button.textColor.a = 0.5
	self:addChild(button)
	self.snapButtons.chunk = button

	button = ISButton:new(10, button:getBottom() + 10, 80, buttonHgt, "SNAP TO SQUARE", self, self.onChangeSnapMode)
	button.internal = "square"
	button.textColor.a = 0.5
	self:addChild(button)
	self.snapButtons.square = button
end

function WorldMapEditorMode_Bounds:render()
	self.labelBounds:setName(string.format("%d,%d,%d,%d", self.resizer.x1, self.resizer.y1, self.resizer.x2 - DXY(), self.resizer.y2 - DXY()))
	self.resizer:render()

	if self.mode == "StartDrawingBounds" then
		local mx = self.mapUI:getMouseX()
		local my = self.mapUI:getMouseY()
		local worldX = self.mapAPI:uiToWorldX(mx, my)
		local worldY = self.mapAPI:uiToWorldY(mx, my)
		worldX = self.resizer:snap(worldX)
		worldY = self.resizer:snap(worldY)
		mx = self.mapAPI:worldToUIX(worldX, worldY)
		my = self.mapAPI:worldToUIY(worldX, worldY)
		self.mapUI:drawRectBorder(mx - 10, my - 10, 20, 20, 1, 0, 0, 1)
		self.mapUI:drawRectBorder(mx - 10 - 1, my - 10 - 1, 20 + 2, 20 + 2, 1, 0, 0, 1)
	end
end

function WorldMapEditorMode_Bounds:display()
	WorldMapEditorMode.display(self)
	if self.editor.bounds then
		self.resizer:setBounds(self.editor.bounds.x1, self.editor.bounds.y1, self.editor.bounds.x2 + DXY(), self.editor.bounds.y2 + DXY())
	end
end

function WorldMapEditorMode_Bounds:undisplay()
	WorldMapEditorMode.undisplay(self)
	self:cancelResize()
end

function WorldMapEditorMode_Bounds:onMouseDown(x, y)
	if self.editor.bounds then
		local resizeMode = self.resizer:hitTest(x, y)
		if not resizeMode then return false end
		self.resizer:startResizing()
		self.mode = "Resize"
		self.resizeMode = resizeMode
		return true
	else
		self.mode = "InitBounds"
		local worldX = self.mapAPI:uiToWorldX(x, y)
		local worldY = self.mapAPI:uiToWorldY(x, y)
		worldX = self.resizer:snap(worldX)
		worldY = self.resizer:snap(worldY)
		self.resizer:setBounds(worldX, worldY, worldX, worldY)
		self.resizer:startResizing()
		return true
	end
	return false -- allow clicks in the map
end

function WorldMapEditorMode_Bounds:onMouseUp(x, y)
	if self.mode == "Resize" then
		self.mode = nil
		self.resizeMode = nil
		self:setBounds(self.resizer.x1, self.resizer.y1, self.resizer.x2 - DXY(), self.resizer.y2 - DXY())
		self.resizer:endResizing()
		return true
	end
	if self.mode == "InitBounds" then
		self.mode = nil
		self:setBounds(self.resizer.x1, self.resizer.y1, self.resizer.x2 - DXY(), self.resizer.y2 - DXY())
		self.resizer:endResizing()
		return true
	end
	return false -- allow clicks in the map
end

function WorldMapEditorMode_Bounds:onMouseUpOutside(x, y)
	return self:onMouseUp(x, y)
end

function WorldMapEditorMode_Bounds:onMouseMove(dx, dy)
	if self.mode == "Resize" then
		local mx = self:getMouseX()
		local my = self:getMouseY()
		self.resizer:onMouseMove(mx, my, self.resizeMode)
		return true
	end
	if self.mode == "InitBounds" then
		local mx = self:getMouseX()
		local my = self:getMouseY()
		self.resizer:onMouseMove(mx, my, "BottomRight")
		return true
	end
	if self.mode then
		return true
	end
	return false -- allow clicks in the map
end

function WorldMapEditorMode_Bounds:onRightMouseDown(x, y)
	return self:cancelResize()
end

function WorldMapEditorMode_Bounds:onKeyPress(key)
	if key == Keyboard.KEY_ESCAPE then
		return self:cancelResize()
	end
	return false
end

function WorldMapEditorMode_Bounds:loadSettingsFromMap()
	self:setBounds(self.mapAPI:getMinXInSquares(),self.mapAPI:getMinYInSquares(), self.mapAPI:getMaxXInSquares(), self.mapAPI:getMaxYInSquares())
	self.resizer:setBounds(self.editor.bounds.x1, self.editor.bounds.y1, self.editor.bounds.x2 + DXY(), self.editor.bounds.y2 + DXY())
end

function WorldMapEditorMode_Bounds:cancelResize()
	if self.mode == "StartDrawingBounds" then
		self.mode = nil
		self:loadSettingsFromMap()
		return true
	end
	if self.mode == "InitBounds" then
		self.mode = nil
		self.resizer:endResizing()
		self:loadSettingsFromMap()
		return true
	end
	if self.mode == "Resize" then
		self.mode = nil
		self.resizeMode = nil
		self.resizer:cancelResize()
		return true
	end
	return false
end

function WorldMapEditorMode_Bounds:setBounds(x1, y1, x2, y2)
	self.editor.bounds = self.editor.bounds or {}
	local bounds = self.editor.bounds
	bounds.x1 = math.min(x1, x2)
	bounds.y1 = math.min(y1, y2)
	bounds.x2 = math.max(x1, x2)
	bounds.y2 = math.max(y1, y2)
	self.mapAPI:setBoundsInSquares(bounds.x1, bounds.y1, bounds.x2, bounds.y2)
end

function WorldMapEditorMode_Bounds:onChangeSnapMode(button)
	self.snapButtons[self.snapMode].textColor.a = 0.5
	button.textColor.a = 1.0
	self.snapMode = button.internal
	self.resizer.snapMode = button.internal
end

function WorldMapEditorMode_Bounds:snap(xy)
	if self.snapMode == "cell" then
		return round(xy / 300) * 300
	end
	if self.snapMode == "chunk" then
		return round(xy / 10) * 10
	end
	return round(xy)
end

function WorldMapEditorMode_Bounds:onDrawBounds()
	if self.mode == "StartDrawingBounds" then
		self.mode = nil
		self:loadSettingsFromMap()
		return
	end
	self.mode = "StartDrawingBounds"
	self.editor.bounds = nil
	self.resizer:setBounds(0, 0, 0, 0)
end

function WorldMapEditorMode_Bounds:onReset()
	self.mode = nil
	self.mapAPI:setBoundsFromData()
	local x1 = self.mapAPI:getMinXInSquares()
	local y1 = self.mapAPI:getMinYInSquares()
	local x2 = self.mapAPI:getMaxXInSquares()
	local y2 = self.mapAPI:getMaxYInSquares()
	self:setBounds(x1, y1, x2, y2)
	self.resizer:setBounds(x1, y1, x2 + DXY(), y2 + DXY())
end

function WorldMapEditorMode_Bounds:generateLuaScript()
	local script = "local mapAPI = mapUI.javaObject:getAPIv1()\n"
	local mapAPI = self.mapAPI
	script = string.format("%smapAPI:setBoundsInSquares(%d, %d, %d, %d)\n", script, mapAPI:getMinXInSquares(), mapAPI:getMinYInSquares(), mapAPI:getMaxXInSquares(), mapAPI:getMaxYInSquares())
	return script
end

function WorldMapEditorMode_Bounds:new(editor)
	local o = WorldMapEditorMode.new(self, editor)
	o.snapMode = "chunk" -- "cell" or "chunk" or "square"
	o.resizer = WorldMapEditorResizer:new(editor)
	o.resizer.snapMode = o.snapMode
	return o
end


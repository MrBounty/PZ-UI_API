--***********************************************************
--**                    THE INDIE STONE                    **
--***********************************************************

-- A resizable-rectangle control
---@class WorldMapEditorLocationControl : ISBaseObject
WorldMapEditorLocationControl = ISBaseObject:derive("WorldMapEditorLocationControl")

function WorldMapEditorLocationControl:setLocation(x, y)
	self.x = x
	self.y = y
end

function WorldMapEditorLocationControl:hitTest(x, y)
	local worldX = self.x
	local worldY = self.y
	local uiX = self.mapAPI:worldToUIX(worldX, worldY)
	local uiY = self.mapAPI:worldToUIY(worldX, worldY)
	if math.abs(x - uiX) < 20 and math.abs(y - uiY) < 20 then
		return true
	end
	return false
end

function WorldMapEditorLocationControl:startDrag()
	self.originalX = self.x
	self.originalY = self.y
end

function WorldMapEditorLocationControl:onMouseMove(mx, my)
	local worldX = self.mapAPI:uiToWorldX(mx, my)
	local worldY = self.mapAPI:uiToWorldY(mx, my)
	worldX = self:snap(worldX)
	worldY = self:snap(worldY)
	self:setLocation(worldX, worldY)
end

function WorldMapEditorLocationControl:cancelDrag()
	self:setLocation(self.originalX, self.originalY)
end

function WorldMapEditorLocationControl:snap(xy)
	if self.snapMode == "cell" then
		return round(xy / 300) * 300
	end
	if self.snapMode == "chunk" then
		return round(xy / 10) * 10
	end
	return round(xy)
end

function WorldMapEditorLocationControl:render()
	local uiX = self.mapAPI:worldToUIX(self.x, self.y)
	local uiY = self.mapAPI:worldToUIY(self.x, self.y)
	local r,g,b = 0,0,0
	if self:hitTest(self.mapUI:getMouseX(), self.mapUI:getMouseY()) then
		r,g,b = 0,0,1
	end
	self.mapUI:drawRect(uiX - 1, uiY - 100, 2, 200, 1, r, g, b)
	self.mapUI:drawRect(uiX - 100, uiY - 1, 200, 2, 1, r, g, b)
end

function WorldMapEditorLocationControl:new(editor)
	local o = ISBaseObject.new(self)
	o.editor = editor
	o.mapUI = editor
	o.mapAPI = editor.mapAPI
	o.x = 0
	o.y = 0
	o.snapMode = "square"
	return o
end


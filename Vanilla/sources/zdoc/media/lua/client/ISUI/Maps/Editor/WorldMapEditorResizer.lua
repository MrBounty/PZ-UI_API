--***********************************************************
--**                    THE INDIE STONE                    **
--***********************************************************

-- A resizable-rectangle control
---@class WorldMapEditorResizer : ISBaseObject
WorldMapEditorResizer = ISBaseObject:derive("WorldMapEditorResizer")

function WorldMapEditorResizer:setBounds(x1, y1, x2, y2)
	self.x1 = math.min(x1, x2)
	self.y1 = math.min(y1, y2)
	self.x2 = math.max(x1, x2)
	self.y2 = math.max(y1, y2)
end

function WorldMapEditorResizer:hitTest(x, y)
	local worldX1 = self.x1
	local worldY1 = self.y1
	local worldX2 = self.x2
	local worldY2 = self.y2
	local uiX1 = self.mapAPI:worldToUIX(worldX1, worldY1)
	local uiY1 = self.mapAPI:worldToUIY(worldX1, worldY1)
	local uiX2 = self.mapAPI:worldToUIX(worldX2, worldY1)
	local uiY2 = self.mapAPI:worldToUIY(worldX2, worldY1)
	local uiX3 = self.mapAPI:worldToUIX(worldX2, worldY2)
	local uiY3 = self.mapAPI:worldToUIY(worldX2, worldY2)
	local uiX4 = self.mapAPI:worldToUIX(worldX1, worldY2)
	local uiY4 = self.mapAPI:worldToUIY(worldX1, worldY2)
	if math.abs(x - uiX1) < 10 and math.abs(y - uiY1) < 10 then
		return "TopLeft"
	end
	if math.abs(x - uiX2) < 10 and math.abs(y - uiY2) < 10 then
		return "TopRight"
	end
	if math.abs(x - uiX3) < 10 and math.abs(y - uiY3) < 10 then
		return "BottomRight"
	end
	if math.abs(x - uiX4) < 10 and math.abs(y - uiY4) < 10 then
		return "BottomLeft"
	end
	if math.abs(x - uiX1) < 10 and y > uiY1 and y < uiY3 then
		return "Left"
	end
	if math.abs(x - uiX2) < 10 and y > uiY1 and y < uiY3 then
		return "Right"
	end
	if math.abs(y - uiY1) < 10 and x > uiX1 and x < uiX2 then
		return "Top"
	end
	if math.abs(y - uiY3) < 10 and x > uiX1 and x < uiX2 then
		return "Bottom"
	end
	return nil
end

function WorldMapEditorResizer:startResizing()
	self.resizing = true
	self.originalBounds.x1 = self.x1
	self.originalBounds.y1 = self.y1
	self.originalBounds.x2 = self.x2
	self.originalBounds.y2 = self.y2
end

function WorldMapEditorResizer:endResizing()
	self.resizing = false
end

function WorldMapEditorResizer:onMouseMove(mx, my, hitTest)
	local worldX = self.mapAPI:uiToWorldX(mx, my)
	local worldY = self.mapAPI:uiToWorldY(mx, my)
	worldX = self:snap(worldX)
	worldY = self:snap(worldY)
	local bounds = self.originalBounds
	local width = bounds.x2 - bounds.x1
	local height = bounds.y2 - bounds.y1
	if hitTest == "TopLeft" then
		if self.fixedSize then
			self:setBounds(worldX, worldY, worldX + width, worldY + height)
			return true
		end
		self:setBounds(worldX, worldY, bounds.x2, bounds.y2)
		return true
	end
	if hitTest == "TopRight" then
		if self.fixedSize then
			self:setBounds(worldX - width, worldY, worldX, worldY + height)
			return true
		end
		self:setBounds(worldX, worldY, bounds.x1, bounds.y2)
		return true
	end
	if hitTest == "BottomRight" then
		if self.fixedSize then
			self:setBounds(worldX - width, worldY - height, worldX, worldY)
			return true
		end
		self:setBounds(worldX, worldY, bounds.x1, bounds.y1)
		return true
	end
	if hitTest == "BottomLeft" then
		if self.fixedSize then
			self:setBounds(worldX, worldY - height, worldX + width, worldY)
			return true
		end
		self:setBounds(worldX, worldY, bounds.x2, bounds.y1)
		return true
	end
	if hitTest == "Left" then
		if self.fixedSize then
			self:setBounds(worldX, bounds.y1, worldX + width, bounds.y2)
			return true
		end
		self:setBounds(worldX, bounds.y1, bounds.x2, bounds.y2)
		return true
	end
	if hitTest == "Right" then
		if self.fixedSize then
			self:setBounds(worldX - width, bounds.y1, worldX, bounds.y2)
			return true
		end
		self:setBounds(bounds.x1, bounds.y1, worldX, bounds.y2)
		return true
	end
	if hitTest == "Top" then
		if self.fixedSize then
			self:setBounds(bounds.x1, worldY, bounds.x2, worldY + height)
			return true
		end
		self:setBounds(bounds.x1, worldY, bounds.x2, bounds.y2)
		return true
	end
	if hitTest == "Bottom" then
		if self.fixedSize then
			self:setBounds(bounds.x1, worldY - height, bounds.x2, worldY)
			return true
		end
		self:setBounds(bounds.x1, bounds.y1, bounds.x2, worldY)
		return true
	end
end

function WorldMapEditorResizer:cancelResize()
	self:endResizing()
	self:setBounds(self.originalBounds.x1, self.originalBounds.y1, self.originalBounds.x2, self.originalBounds.y2)
end

function WorldMapEditorResizer:snap(xy)
	if self.snapMode == "cell" then
		return round(xy / 300) * 300
	end
	if self.snapMode == "chunk" then
		return round(xy / 10) * 10
	end
	return round(xy)
end

function WorldMapEditorResizer:render()
	local worldX1 = self.x1
	local worldY1 = self.y1
	local worldX2 = self.x2
	local worldY2 = self.y2
	local uiX1 = self.mapAPI:worldToUIX(worldX1, worldY1)
	local uiY1 = self.mapAPI:worldToUIY(worldX1, worldY1)
	local uiX2 = self.mapAPI:worldToUIX(worldX2, worldY1)
	local uiY2 = self.mapAPI:worldToUIY(worldX2, worldY1)
	local uiX3 = self.mapAPI:worldToUIX(worldX2, worldY2)
	local uiY3 = self.mapAPI:worldToUIY(worldX2, worldY2)
	local uiX4 = self.mapAPI:worldToUIX(worldX1, worldY2)
	local uiY4 = self.mapAPI:worldToUIY(worldX1, worldY2)
	local THICK = 3
	self.mapUI:drawRect(uiX1 - THICK, uiY1 - THICK, uiX2 - uiX1 + THICK * 2, THICK, 1, 0, 0, 0)
	self.mapUI:drawRect(uiX2, uiY2 - THICK, THICK, uiY3 - uiY2 + THICK * 2, 1, 0, 0, 0)
	self.mapUI:drawRect(uiX4 - THICK, uiY4, uiX3 - uiX4 + THICK * 2, THICK, 1, 0, 0, 0)
	self.mapUI:drawRect(uiX1 - THICK, uiY1 - THICK, THICK, uiY4 - uiY1 + THICK * 2, 1, 0, 0, 0)

	if self.resizing then return end

	local mx = self.mapUI:getMouseX()
	local my = self.mapUI:getMouseY()
	local test = self:hitTest(mx, my)
	if not test then return end

	if self.fixedSize then
		self.mapUI:drawRectBorder(uiX1 - 10, uiY1 - 10, uiX3 - uiX1 + 20, uiY3 - uiY1 + 20, 1, 0, 0, 1)
		self.mapUI:drawRectBorder(uiX1 - 10 - 1, uiY1 - 10 - 1, uiX2 - uiX1 + 20 + 2, uiY3 - uiY1 + 20 + 2, 1, 0, 0, 1)
	end
	
	local boxX1 = nil
	local boxY1 = nil
	local boxX2 = nil
	local boxY2 = nil
	if test == "TopLeft" then
		boxX1,boxY1,boxX2,boxY2 = uiX1,uiY1,uiX1,uiY1
	elseif test == "TopRight" then
		boxX1,boxY1,boxX2,boxY2 = uiX2,uiY2,uiX2,uiY2
	elseif test == "BottomRight" then
		boxX1,boxY1,boxX2,boxY2 = uiX3,uiY3,uiX3,uiY3
	elseif test == "BottomLeft" then
		boxX1,boxY1,boxX2,boxY2 = uiX4,uiY4,uiX4,uiY4
	elseif test == "Top" then
		boxX1,boxY1,boxX2,boxY2 = uiX1,uiY1,uiX2,uiY2
	elseif test == "Bottom" then
		boxX1,boxY1,boxX2,boxY2 = uiX4,uiY4,uiX3,uiY3
	elseif test == "Left" then
		boxX1,boxY1,boxX2,boxY2 = uiX1,uiY1,uiX4,uiY4
	elseif test == "Right" then
		boxX1,boxY1,boxX2,boxY2 = uiX2,uiY2,uiX3,uiY3
	end
	if not boxX1 then return end
	self.mapUI:drawRectBorder(boxX1 - 10, boxY1 - 10, boxX2 - boxX1 + 20, boxY2 - boxY1 + 20, 1, 0, 0, 1)
	self.mapUI:drawRectBorder(boxX1 - 10 - 1, boxY1 - 10 - 1, boxX2 - boxX1 + 20 + 2, boxY2 - boxY1 + 20 + 2, 1, 0, 0, 1)
end

function WorldMapEditorResizer:new(editor)
	local o = ISBaseObject.new(self)
	o.editor = editor
	o.mapUI = editor
	o.mapAPI = editor.mapAPI
	o.x1 = 0
	o.y1 = 0
	o.x2 = 0
	o.y2 = 0
	o.snapMode = "square"
	o.originalBounds = {}
	return o
end


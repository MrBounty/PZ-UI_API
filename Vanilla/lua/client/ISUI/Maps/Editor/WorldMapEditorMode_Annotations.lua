--***********************************************************
--**                    THE INDIE STONE                    **
--***********************************************************

require 'ISUI/Maps/Editor/WorldMapEditorMode'

local FONT_HGT_SMALL = getTextManager():getFontHeight(UIFont.Small)
local FONT_HGT_MEDIUM = getTextManager():getFontHeight(UIFont.Medium)
local FONT_HGT_HANDWRITTEN = getTextManager():getFontHeight(UIFont.Handwritten)

WorldMapEditorMode_Annotations = WorldMapEditorMode:derive("WorldMapEditorMode_Annotations")

-----

function WorldMapEditorMode_Annotations:createChildren()
	self.symbolsUI = ISWorldMapSymbols:new(10, 80, 200, 200, self.editor)
	self.symbolsUI.showTranslationOption = true
	self:addChild(self.symbolsUI)
end

function WorldMapEditorMode_Annotations:onMouseDown(x, y)
	if self.symbolsUI:onMouseDownMap(x, y) then
		return true
	end
	self.dragging = true
	self.dragMoved = false
	self.dragStartX = x
	self.dragStartY = y
	return false -- allow clicks in the map
end

function WorldMapEditorMode_Annotations:onMouseUp(x, y)
	if self.dragging then
		self.dragging = false
	end
	if self.symbolsUI:onMouseUpMap(x, y) then
		return true
	end
	return false -- allow clicks in the map
end

function WorldMapEditorMode_Annotations:onMouseUpOutside(x, y)
	self.dragging = false
	if self.symbolsUI:onMouseUpMap(x, y) then
		return true
	end
	return false -- allow clicks in the map
end

function WorldMapEditorMode_Annotations:onMouseMove(dx, dy)
	if self.symbolsUI:onMouseMoveMap(dx, dy) then
		return true
	end
	if self.dragging then
		local mouseX = self:getMouseX()
		local mouseY = self:getMouseY()
		if not self.dragMoved and math.abs(mouseX - self.dragStartX) <= 4 and math.abs(mouseY - self.dragStartY) <= 4 then
			return false
		end
		self.dragMoved = true
	end
	return false -- allow clicks in the map
end

function WorldMapEditorMode_Annotations:onRightMouseDown(x, y)
    if self.symbolsUI:onRightMouseDownMap(x, y) then
        return true
    end
    return false
end

function WorldMapEditorMode_Annotations:prerender()
	ISPanel.prerender(self)
end

function WorldMapEditorMode_Annotations:render()
	ISPanel.render(self)
end

function WorldMapEditorMode_Annotations:undisplay()
	WorldMapEditorMode.undisplay(self)
	self.symbolsUI:undisplay()
end

function WorldMapEditorMode_Annotations:isKeyConsumed(key)
	if self.symbolsUI:isKeyConsumed(key) then
		return true
	end
	return false
end

function WorldMapEditorMode_Annotations:onKeyPress(key)
	if self.symbolsUI:onKeyPress(key) then
		return true
	end
	return false
end

function WorldMapEditorMode_Annotations:onKeyRelease(key)
	if self.symbolsUI:onKeyRelease(key) then
		return true
	end
	return false
end

function WorldMapEditorMode_Annotations:generateLuaScript()
	local symbolsAPI = self.symbolsAPI
	local script = ""
	for i=1,symbolsAPI:getSymbolCount() do
		local symbol = symbolsAPI:getSymbolByIndex(i-1)
		local worldX = symbol:getWorldX()
		local worldY = symbol:getWorldY()
		local r = symbol:getRed()
		local g = symbol:getGreen()
		local b = symbol:getBlue()
		if symbol:isText() then
			local text = symbol:getUntranslatedText()
			script = string.format("%sstashMap:addStamp(nil, \"%s\", %d, %d, %d, %d, %d)\n", script, text or "", worldX, worldY, r, g, b)
		end
		if symbol:isTexture() then
			local symbolID = symbol:getSymbolID()
			script = string.format("%sstashMap:addStamp(\"%s\", nil, %d, %d, %d, %d, %d)\n", script, symbolID, worldX, worldY, r, g, b)
		end
	end
	return script
end

function WorldMapEditorMode_Annotations:new(editor)
	local o = WorldMapEditorMode.new(self, editor)
	return o
end


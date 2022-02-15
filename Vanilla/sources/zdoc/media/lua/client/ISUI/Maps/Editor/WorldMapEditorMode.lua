--***********************************************************
--**                    THE INDIE STONE                    **
--***********************************************************

---@class WorldMapEditorMode : ISPanel
WorldMapEditorMode = ISPanel:derive("WorldMapEditorMode")

function WorldMapEditorMode:display()
	self:setVisible(true)
end

function WorldMapEditorMode:undisplay()
	self:setVisible(false)
end

function WorldMapEditorMode:onMouseDown(x, y)
	return false -- allow clicks in the map
end

function WorldMapEditorMode:onMouseUp(x, y)
	return false -- allow clicks in the map
end

function WorldMapEditorMode:onMouseMove(dx, dy)
	return false -- allow clicks in the map
end

function WorldMapEditorMode:onMouseWheel(del)
	if self:isMouseOver() then
		local children = self:getChildren()
		for _,child in pairs(children) do
			if child:isMouseOver() then
				return false
			end
		end
		self.mapAPI:zoomAt(self:getMouseX(), self:getMouseY(), del)
	end
	return true
end

function WorldMapEditorMode:isKeyConsumed(key)
	return false
end

function WorldMapEditorMode:onKeyPress(key)
	return false
end

function WorldMapEditorMode:onKeyRelease(key)
	return false
end

function WorldMapEditorMode:loadSettingsFromMap()
end

function WorldMapEditorMode:generateLuaScript()
	return ""
end

function WorldMapEditorMode:new(editor)
	local o = ISPanel.new(self, 0, 0, editor.width, editor.height)
	o:setAnchorRight(true)
	o:setAnchorBottom(true)
	o:noBackground()
	o.editor = editor
	o.mapUI = editor
	o.mapAPI = editor.mapAPI
	o.styleAPI = editor.styleAPI
	o.symbolsAPI = editor.symbolsAPI
	return o
end


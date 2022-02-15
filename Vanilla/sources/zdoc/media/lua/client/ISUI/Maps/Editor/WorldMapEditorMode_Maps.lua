--***********************************************************
--**                    THE INDIE STONE                    **
--***********************************************************

require 'ISUI/Maps/Editor/WorldMapEditorMode'

local FONT_HGT_SMALL = getTextManager():getFontHeight(UIFont.Small)
local FONT_HGT_MEDIUM = getTextManager():getFontHeight(UIFont.Medium)

---@class WorldMapEditorMode_Maps : WorldMapEditorMode
WorldMapEditorMode_Maps = WorldMapEditorMode:derive("WorldMapEditorMode_Maps")

-----

function WorldMapEditorMode_Maps:createChildren()
	self.listbox = ISScrollingListBox:new(10, 80, 400, 200)
	self.listbox:setFont(UIFont.Small, 4)
	self:addChild(self.listbox)

	local buttonHgt = FONT_HGT_MEDIUM + 8
	local button = ISButton:new(10, self.listbox:getBottom() + 10, 80, buttonHgt, "LOAD", self, self.onLoadMap)
	self:addChild(button)
end

function WorldMapEditorMode_Maps:onMouseDown(x, y)
	return false -- allow clicks in the map
end

function WorldMapEditorMode_Maps:onMouseUp(x, y)
	return false -- allow clicks in the map
end

function WorldMapEditorMode_Maps:onMouseUpOutside(x, y)
	return false -- allow clicks in the map
end

function WorldMapEditorMode_Maps:onMouseMove(dx, dy)
	return false -- allow clicks in the map
end

function WorldMapEditorMode_Maps:prerender()
	ISPanel.prerender(self)
end

function WorldMapEditorMode_Maps:render()
	ISPanel.render(self)
end

function WorldMapEditorMode_Maps:display()
	WorldMapEditorMode.display(self)
	self:fillList()
end

function WorldMapEditorMode_Maps:undisplay()
	WorldMapEditorMode.undisplay(self)
end

function WorldMapEditorMode_Maps:isKeyConsumed(key)
	return false
end

function WorldMapEditorMode_Maps:onKeyPress(key)
	return false
end

function WorldMapEditorMode_Maps:onKeyRelease(key)
	return false
end

function WorldMapEditorMode_Maps:fillList()
	local index = self.listbox.selected
	self.listbox:clear()
	if not LootMaps or not LootMaps.Init then return end
	for key,value in pairs(LootMaps.Init) do
		self.listbox:addItem(key, key)
	end
	self.listbox:sort()
	if index >= 1 and index < self.listbox:size() then
		self.listbox.selected = index
	end
end

function WorldMapEditorMode_Maps:onLoadMap()
	local item = self.listbox.items[self.listbox.selected]
	if not item then return end
	self.symbolsAPI:clear()
	local mapID = item.item
	if not LootMaps.Init[mapID] then return end
	self.editor.mapItem:setMapID(mapID)
	LootMaps.callLua("Init", self.mapUI)
	self.editor:loadSettingsFromMap()
	return false
end

function WorldMapEditorMode_Maps:new(editor)
	local o = WorldMapEditorMode.new(self, editor)
	return o
end


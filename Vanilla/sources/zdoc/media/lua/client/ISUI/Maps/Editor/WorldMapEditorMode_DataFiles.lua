--***********************************************************
--**                    THE INDIE STONE                    **
--***********************************************************

require 'ISUI/Maps/Editor/WorldMapEditorMode'

local FONT_HGT_SMALL = getTextManager():getFontHeight(UIFont.Small)
local FONT_HGT_MEDIUM = getTextManager():getFontHeight(UIFont.Medium)

---@class WorldMapEditorMode_DataFiles : WorldMapEditorMode
WorldMapEditorMode_DataFiles = WorldMapEditorMode:derive("WorldMapEditorMode_DataFiles")

function WorldMapEditorMode_DataFiles:createChildren()
	self.listbox = WorldMapEditorListBox:new(10, 80, 400, 200, self.onListboxButton, self)
	self:addChild(self.listbox)

    local entryHgt = FONT_HGT_SMALL + 2 * 2
	self.fileNameEntry = ISTextEntryBox:new("", self.listbox.x, self.listbox:getBottom() + 10, self.listbox.width, entryHgt)
	self.fileNameEntry.onCommandEntered = function(entry) self:onFileNameEntered() end
	self:addChild(self.fileNameEntry)

	local buttonHgt = FONT_HGT_MEDIUM + 8
end

function WorldMapEditorMode_DataFiles:undisplay()
	WorldMapEditorMode.undisplay(self)
	self.fileNameEntry:unfocus()
end

function WorldMapEditorMode_DataFiles:loadSettingsFromMap()
	self.listbox:clear()
	for i=1,self.mapAPI:getDataCount() do
		self.listbox:addItem(self.mapAPI:getDataFileByIndex(i-1))
	end
end

function WorldMapEditorMode_DataFiles:onListboxButton(action, arg1)
	if action == "add" then
		self:onAddFile()
	elseif action == "remove" then
		self:onRemoveFile()
	elseif action == "up" then
		self:onMoveUp()
	elseif action == "down" then
		self:onMoveDown()
	end
end

function WorldMapEditorMode_DataFiles:onAddFile()
	local text = self.fileNameEntry:getText()
	self.listbox:addItem(text)
	self.listbox:setSelectedIndex(self.listbox:size())
	self:updateData()
end

function WorldMapEditorMode_DataFiles:onRemoveFile()
	local item = self.listbox:getSelectedItem()
	if not item then return end
	self.listbox:removeItemByIndex(self.listbox:getSelectedIndex())
	self:updateData()
end

function WorldMapEditorMode_DataFiles:onMoveUp()
	local item = self.listbox:getSelectedItem()
	if not item then return end
	local index = self.listbox:getSelectedIndex()
	if index == 1 then return end
	local text = item.text
	self.listbox:removeItemByIndex(index)
	self.listbox:insertItem(index - 1, text, nil)
	self.listbox:setSelectedIndex(index - 1)
	self:updateData()
end

function WorldMapEditorMode_DataFiles:onMoveDown()
	local item = self.listbox:getSelectedItem()
	if not item then return end
	local index = self.listbox:getSelectedIndex()
	if index == self.listbox:size() then return end
	local text = item.text
	self.listbox:removeItemByIndex(index)
	self.listbox:insertItem(index + 1, text, nil)
	self.listbox:setSelectedIndex(index + 1)
	self:updateData()
end

function WorldMapEditorMode_DataFiles:onFileNameEntered()
	local item = self.listbox:getSelectedItem()
	if not item then return end
	item.text = self.fileNameEntry:getText()
	self:updateData()
end

function WorldMapEditorMode_DataFiles:update()
	-- TODO: enable/disable buttons
	local item = self.listbox:getSelectedItem()
	if item ~= self.selectedItem then
		self.selectedItem = item
		local text = item and item.text or ""
		self.fileNameEntry:setText(text)
	end
end

function WorldMapEditorMode_DataFiles:updateData()
	local fileNames = {}
	for i=1,self.listbox:size() do
		table.insert(fileNames, self.listbox:getItemByIndex(i).text)
	end
	self.editor:setDataFiles(fileNames)
end

function WorldMapEditorMode_DataFiles:generateLuaScript()
	local script = "local mapAPI = mapUI.javaObject:getAPIv1()\n"
	local mapAPI = self.mapAPI
	for i=1,self.mapAPI:getDataCount() do
		local filePath = mapAPI:getDataFileByIndex(i-1)
		script = string.format("%smapAPI:addData(\"%s\")\n", script, filePath)
	end
	return script
end

function WorldMapEditorMode_DataFiles:new(editor)
	local o = WorldMapEditorMode.new(self, editor)
	return o
end

--***********************************************************
--**                    THE INDIE STONE                    **
--***********************************************************

require 'ISUI/ISScrollingListBox'

local FONT_HGT_SMALL = getTextManager():getFontHeight(UIFont.Small)
local FONT_HGT_MEDIUM = getTextManager():getFontHeight(UIFont.Medium)

-- A listbox with Add, Remove, Move Up and Move Down buttons

---@class WorldMapEditorListBox : ISPanel
WorldMapEditorListBox = ISPanel:derive("WorldMapEditorListBox")

function WorldMapEditorListBox:createChildren()
	local buttonWid = FONT_HGT_MEDIUM + 8
	local buttonHgt = FONT_HGT_MEDIUM + 8

	local PADY = 4
	local ROWS = 6
	self.listbox = ISScrollingListBox:new(0, 0, self.width, (FONT_HGT_SMALL + PADY * 2) * ROWS)
	self.listbox:setFont(UIFont.Small, PADY)
	self:addChild(self.listbox)

	local button = ISButton:new(0, self.listbox:getBottom() + 10, buttonWid, buttonHgt, "+", self, self.onAddItem)
	self:addChild(button)
	self.buttonAdd = button

	button = ISButton:new(button:getRight() + 10, button.y, buttonWid, buttonHgt, "-", self, self.onRemoveItem)
	self:addChild(button)
	self.buttonRemove = button

	button = ISButton:new(button:getRight() + 10, button.y, buttonWid, buttonHgt, "/\\", self, self.onMoveUp)
	self:addChild(button)
	self.buttonMoveUp = button

	button = ISButton:new(button:getRight() + 10, button.y, buttonWid, buttonHgt, "\\/", self, self.onMoveDown)
	self:addChild(button)
	self.buttonMoveDown = button

	self:setHeight(button:getBottom())
end

function WorldMapEditorListBox:clear()
	self.listbox:clear()
end

function WorldMapEditorListBox:addItem(text, data)
	self.listbox:addItem(text, data)
end

function WorldMapEditorListBox:insertItem(index, text, data)
	self.listbox:insertItem(index, text, data)
end

function WorldMapEditorListBox:removeItemByIndex(index)
	self.listbox:removeItemByIndex(index)
end

function WorldMapEditorListBox:size()
	return self.listbox:size()
end

function WorldMapEditorListBox:getItemByIndex(index)
	return self.listbox.items[index]
end

function WorldMapEditorListBox:setSelectedIndex(index)
	self.listbox.selected = index
end

function WorldMapEditorListBox:getSelectedIndex()
	local index = self.listbox.selected
	if index >= 1 and index <= self.listbox:size() then
		return index
	end
	return -1
end

function WorldMapEditorListBox:getSelectedItem()
	return self.listbox.items[self.listbox.selected]
end

function WorldMapEditorListBox:onAddItem()
	self.callback(self.target, "add", self.arg1, self.arg2, self.arg3, self.arg4)
end

function WorldMapEditorListBox:onRemoveItem()
	local selected = self.listbox.selected
	local item = self.listbox.items[selected]
	if not item then return end
	self.callback(self.target, "remove", self.arg1, self.arg2, self.arg3, self.arg4)
end

function WorldMapEditorListBox:onMoveUp()
	local selected = self.listbox.selected
	local item = self.listbox.items[selected]
	if not item then return end
	if self.listbox.selected == 1 then return end
	self.callback(self.target, "up", self.arg1, self.arg2, self.arg3, self.arg4)
end

function WorldMapEditorListBox:onMoveDown()
	local selected = self.listbox.selected
	local item = self.listbox.items[selected]
	if not item then return end
	if selected == self.listbox:size() then return end
	self.callback(self.target, "down", self.arg1, self.arg2, self.arg3, self.arg4)
end

function WorldMapEditorListBox:getListBox()
	return self.listbox
end

function WorldMapEditorListBox:prerender()
	local selected = self.listbox.selected
	local item = self.listbox.items[selected]
	self.buttonAdd:setEnable(true)
	self.buttonRemove:setEnable(item ~= nil)
	self.buttonMoveUp:setEnable(selected > 1)
	self.buttonMoveDown:setEnable(selected < self.listbox:size())
	if item ~= self.selectedItem then
		self.selectedItem = item
		self.callback(self.target, "select", self.arg1, self.arg1, self.arg3, self.arg4)
	end
end

function WorldMapEditorListBox:new(x, y, width, height, callback, target, arg1, arg2, arg3, arg4)
	local o = ISPanel.new(self, x, y, width, height)
	o:noBackground()
	o.callback = callback
	o.target = target
	o.arg1 = arg1
	o.arg2 = arg2
	o.arg3 = arg3
	o.arg4 = arg4
	o.selectedItem = -1
	return o
end


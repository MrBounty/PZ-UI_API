--***********************************************************
--**                    THE INDIE STONE                    **
--***********************************************************

require "ISUI/ISCollapsableWindow"
require "ISUI/ISScrollingListBox"
require "ISUI/ISTickBox"

---@class DebugOptionsWindow : ISCollapsableWindow
DebugOptionsWindow = ISCollapsableWindow:derive("DebugOptionsWindow")

local FONT_HGT_SMALL = getTextManager():getFontHeight(UIFont.Small)

-----

local TickBox = ISTickBox:derive("DebugOptionsWindow_TickBox")

function TickBox:getTextColor(index, color)
	if self.optionData[index] and self.optionData[index]:getValue() ~= self.optionData[index]:getDefaultValue() then
		color.r = 1.0
		color.g = 1.0
		color.b = 0.0
		color.a = self.choicesColor.a
	else
		ISTickBox.getTextColor(self, index, color)
	end
end

-----

local ListBox = ISScrollingListBox:derive("DebugOptionsWindow_ListBox")

function ListBox:doDrawItem(y, item, alt)
	if not item.height then item.height = self.itemheight end
	if self.selected == item.index then
		self:drawRect(0, (y), self:getWidth(), item.height-1, 0.3, 0.7, 0.35, 0.15)
	end
	self:drawRectBorder(0, (y), self:getWidth(), item.height, 0.5, self.borderColor.r, self.borderColor.g, self.borderColor.b)

	local r,g,b = 0.9,0.9,0.9
	local category = item.item
	local options = self.parent.categoryMap[category]
	for _,option in ipairs(options) do
		if option:getValue() ~= option:getDefaultValue() then
			r = 1.0
			g = 1.0
			b = 0.0
			break
		end
	end
	
	local itemPadY = self.itemPadY or (item.height - self.fontHgt) / 2
	self:drawText(item.text, 15, (y)+itemPadY, r, g, b, 1.0, self.font)
	y = y + item.height
	return y
end

-----

function DebugOptionsWindow:onTickBox(index, selected, option)
	option:setValue(selected)
	getDebugOptions():save()
end

function DebugOptionsWindow:createChildren()
	local th = self:titleBarHeight()
	
	self.categoryList = ListBox:new(0, th, 200, self.height - th)
	self.categoryList:initialise()
	self.categoryList:instantiate()
	self.categoryList:setAnchorBottom(true)
	self.categoryList:setFont(UIFont.Small, 2)
	self.categoryList.itemheight = math.max(FONT_HGT_SMALL + 4, 22)
	self.categoryList.drawBorder = true
	self.categoryList:setOnMouseDownFunction(self, self.onCategorySelected)
	self:addChild(self.categoryList)

	local categories = {}
	self.categoryMap = {}
	local options = getDebugOptions()
	for i=1,options:getOptionCount() do
		local option = options:getOptionByIndex(i-1)
		local category = string.split(option:getName(), "\\.")[1]
		if not self.categoryMap[category] then
			self.categoryMap[category] = {}
			table.insert(categories, category)
		end
		table.insert(self.categoryMap[category], option)
	end
	table.sort(categories)
	for _,category in ipairs(categories) do
		self.categoryList:addItem(category, category)
	end

	self.tickBoxes = {}

	local height = th + #categories * self.categoryList.itemheight
	height = math.min(getCore():getScreenHeight() - 20 - self.y, height)
	if self.height < height then
		self:setHeight(height)
	end

	self:onCategorySelected(categories[1])
end

function DebugOptionsWindow:onCategorySelected(category)
	for _,tickBox in ipairs(self.tickBoxes) do
		self:removeChild(tickBox)
	end
	self.tickBoxes = {}

	local options = self.categoryMap[category]
	local x = self.categoryList:getRight() + 10
	local y = self.categoryList.y + 5
	local rowHgt = FONT_HGT_SMALL + 2 * 2
	for _,option in ipairs(options) do
		local tickBox = TickBox:new(x, y, 100, 100, "", self, self.onTickBox, option)
		tickBox:initialise()
		tickBox:addOption(option:getName(), option)
		tickBox:setSelected(1, option:getValue())
		tickBox:setWidthToFit()
		self:addChild(tickBox)
		y = y + rowHgt
		table.insert(self.tickBoxes, tickBox)
		if y + rowHgt > self.height then
			local width,height = self:calcTickBoxBounds()
			x = width + 20
			y = self.categoryList.y + 5
		end
	end

	local width,height = self:calcTickBoxBounds()
	local width = math.max(width, self.categoryList:getRight() + 300)
	self:setWidth(width + 12)
--	self:setHeight(height + self:resizeWidgetHeight())
end

function DebugOptionsWindow:calcTickBoxBounds()
	local width = 0
	local height = 0
	for _,child in pairs(self.tickBoxes) do
		width = math.max(width, child:getRight())
		height = math.max(height, child:getBottom())
	end
	return width,height
end

function DebugOptionsWindow:onMouseDownOutside(x, y)
	-- This is called when clicking in our child ISScrollingListBox.
	if self:isMouseOver() then return end
	self:setVisible(false)
	self:removeFromUIManager()
end

function DebugOptionsWindow:new(x, y, width, height)
	local o = ISCollapsableWindow:new(x, y, width, height)
	setmetatable(o, self)
	self.__index = self
	o.backgroundColor = {r=0, g=0, b=0, a=1.0}
	o.resizable = false
	return o
end


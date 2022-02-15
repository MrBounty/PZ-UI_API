--***********************************************************
--**                    THE INDIE STONE                    **
--***********************************************************

require "ISUI/ISCollapsableWindow"

---@class DebugLogSettings : ISCollapsableWindow
DebugLogSettings = ISCollapsableWindow:derive("DebugLogSettings")

local FONT_HGT_SMALL = getTextManager():getFontHeight(UIFont.Small)

function DebugLogSettings:onTickBox(index, selected, debugType)
	DebugLog.setLogEnabled(debugType, selected)
	DebugLog:save()
end

function DebugLogSettings:createChildren()
	local fontHgt = getTextManager():getFontFromEnum(UIFont.Small):getLineHeight()
	local entryHgt = fontHgt + 2 * 2

	local x = 12
	local y = self:titleBarHeight() + 6
	local maxWidth = 0

	local debugTypes = DebugLog.getDebugTypes()
	for i=1,debugTypes:size() do
		local debugType = debugTypes:get(i-1)

		local tickBox = ISTickBox:new(x, y, self.width, entryHgt, "", self, self.onTickBox, debugType)
		tickBox:initialise()
		tickBox:addOption(debugType:name(), debugType)
		tickBox:setSelected(1, DebugLog.isEnabled(debugType))
		tickBox:setWidthToFit()
		self:addChild(tickBox)
		maxWidth = math.max(maxWidth, tickBox:getRight())

		y = y + entryHgt + 6
		if self.y + y + entryHgt + 6 >= getCore():getScreenHeight() then
			x = x + maxWidth
			y = self:titleBarHeight() + 6
			maxWidth = 0
		end
	end

	local width = 0
	local height = 0
	for _,child in pairs(self:getChildren()) do
		width = math.max(width, child:getRight())
		height = math.max(height, child:getBottom())
	end
	self:setWidth(width + 12)
	self:setHeight(height + self:resizeWidgetHeight())
end

function DebugLogSettings:onMouseDownOutside(x, y)
	self:setVisible(false)
	self:removeFromUIManager()
end

function DebugLogSettings:new(x, y, width, height)
	local o = ISCollapsableWindow:new(x, y, width, height)
	setmetatable(o, self)
	self.__index = self
	o.backgroundColor = {r=0, g=0, b=0, a=1.0}
	o.resizable = false
	return o
end


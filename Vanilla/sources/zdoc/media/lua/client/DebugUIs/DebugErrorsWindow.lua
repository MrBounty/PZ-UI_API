--***********************************************************
--**                    THE INDIE STONE                    **
--***********************************************************

require "ISUI/ISCollapsableWindow"

---@class DebugErrorsWindow : ISCollapsableWindow
DebugErrorsWindow = ISCollapsableWindow:derive("DebugErrorsWindow")

local FONT_HGT_SMALL = getTextManager():getFontHeight(UIFont.Small)

function DebugErrorsWindow:createChildren()
	ISCollapsableWindow.createChildren(self)
	
	local th = self:titleBarHeight()
	local rh = self:resizeWidgetHeight()
	local textBox = ISTextEntryBox:new("", 0, 0 + th, self.width, self.height - th - rh)
	textBox:initialise()
	textBox:setAnchorRight(true)
	textBox:setAnchorBottom(true)
	self:addChild(textBox)
	textBox:setEditable(false)
	textBox:setMultipleLine(true)
	textBox:addScrollBars()
	self.textBox = textBox
end

function DebugErrorsWindow:refresh()
	local text = ""
	local errors = getLuaDebuggerErrors()
	for i = 1,errors:size() do
		local str = errors:get(i-1)
		str = str:gsub("\t", "    ")
		text = text .. str .. "\n"
	end
	self.textBox:setText(text)
end

function DebugErrorsWindow:prerender()
	if self.errorCount ~= getLuaDebuggerErrorCount() then
		self.errorCount = getLuaDebuggerErrorCount()
		self:refresh()
	end
	ISCollapsableWindow.prerender(self)
end

function DebugErrorsWindow:new(x, y, width, height)
	local o = ISCollapsableWindow.new(self, x, y, width, height)
	o.title = "Errors"
	o.errorCount = -1
	return o
end


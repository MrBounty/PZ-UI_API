--***********************************************************
--**                    THE INDIE STONE                    **
--***********************************************************

require "ISUI/ISContextMenu"

---@class DebuggerContextMenu : ISContextMenu
DebuggerContextMenu = ISContextMenu:derive("DebuggerContextMenu")

-- Override ISContextMenu
function DebuggerContextMenu:topmostMenuWithMouse(x, y)
	local menu = nil
	if self == getDebuggerContextMenu() then
		if x >= self.x and x < self.x + self.width and y >= self.y and y < self.y + self.height then
			menu = self
		end
	end
	for i=1,#getDebuggerContextMenu().instanceMap do
		local m = getDebuggerContextMenu().instanceMap[i]
		if m:getIsVisible() and x >= m.x and x < m.x + m.width and y >= m.y and y < m.y + m.height then
			menu = m
		end
	end
	return menu
end

function DebuggerContextMenu:new(x, y, width, height)
	local o = ISContextMenu.new(self, x, y, width, height)
	return o
end

local CONTEXT_MENU = nil

function getDebuggerContextMenu()
	if not CONTEXT_MENU then
		CONTEXT_MENU = DebuggerContextMenu:new(0, 0, 200, 200, 1.0)
		CONTEXT_MENU:setVisible(false)
		CONTEXT_MENU:addToUIManager()
	end
	return CONTEXT_MENU
end


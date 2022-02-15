--***********************************************************
--**                    THE INDIE STONE                    **
--***********************************************************

---@class ISJoypadDebugUI : ISUIElement
ISJoypadDebugUI = ISUIElement:derive("ISJoypadDebugUI");

local FONT_HGT_DEBUG = getTextManager():getFontHeight(UIFont.Debug)

function ISJoypadDebugUI:createChildren()
end

function ISJoypadDebugUI:update()
	self:bringToTop()
end

function ISJoypadDebugUI:prerender()
	ISUIElement.prerender(self)
end

local function uiToString(ui)
    return ui and ui:toString() or "nil"
end

function ISJoypadDebugUI:render()
	ISUIElement.prerender(self)
	local x1 = getCore():getScreenWidth() - 300
	local y = 20
	local x2 = x1 + 130
	self:drawRect(x1 - 10, y - 10, 300, FONT_HGT_DEBUG * 31 + 10 + 10, 0.75, 0.0, 0.0, 0.0)
	for i,joypadData in ipairs(JoypadState.joypads) do
		y = self:addLine(x1, x2, y, "Joypad " .. i .. ":", nil)
		y = self:addLine(x1 + 15, x2, y, "connected:", tostring(joypadData.controller and joypadData.controller.connected or false))
		y = self:addLine(x1 + 15, x2, y, "id: ", tostring(joypadData.id))
		y = self:addLine(x1 + 15, x2, y, "player: ", tostring(joypadData.player))
		y = self:addLine(x1 + 15, x2, y, "focus: ", uiToString(joypadData.focus))
		y = self:addLine(x1 + 15, x2, y, "lastfocus: ", uiToString(joypadData.lastfocus))
		y = self:addLine(x1 + 15, x2, y, "activeWhilePaused: ", tostring(joypadData.activeWhilePaused))
		y = y + FONT_HGT_DEBUG
	end
end

function ISJoypadDebugUI:addLine(x1, x2, y, label, value)
	self:drawText(label, x1, y, 1, 1, 1, 1, UIFont.DebugConsole, true)
	if value then
		self:drawText(value, x2, y, 1, 1, 1, 1, UIFont.DebugConsole, true)
	end
	return y + FONT_HGT_DEBUG
end

function ISJoypadDebugUI:new()
	o = ISUIElement.new(self, 0, 0, 1, 1)
	return o
end

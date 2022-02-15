--***********************************************************
--**                    THE INDIE STONE                    **
--***********************************************************

require "ISUI/ISModalDialog"

---@class ISConfirmMonitorSettingsDialog : ISModalDialog
ISConfirmMonitorSettingsDialog = ISModalDialog:derive("ISConfirmMonitorSettings")

local FONT_HGT_SMALL = getTextManager():getFontHeight(UIFont.Small)

function ISConfirmMonitorSettingsDialog:initialise()
	ISModalDialog.initialise(self)
	self:setHeight(20 + FONT_HGT_SMALL + 10 + FONT_HGT_SMALL + 10 + self.yes:getHeight() + 10)
end

function ISConfirmMonitorSettingsDialog:update()
	ISModalDialog.update(self)
	local timeRemaining = 20 - (getTimestamp() - self.startTime)
	if timeRemaining <= 0 then
		self:onClick(self.no)
	end
end

function ISConfirmMonitorSettingsDialog:render()
	ISModalDialog.render(self)
	local timeRemaining = math.max(0, 20 - (getTimestamp() - self.startTime))
	self:drawTextCentre(getText("UI_ConfirmMonitorSettings_RevertIn", math.ceil(timeRemaining)), self:getWidth() / 2, 20 + FONT_HGT_SMALL + 10, 1, 1, 1, 1, UIFont.Small)
end

function ISConfirmMonitorSettingsDialog:new(x, y, width, height, callback, param1, param2)
	local o = ISModalDialog.new(self, x, y, width, height, getText("UI_ConfirmMonitorSettings_Prompt"),
		true, param1, callback, nil, param2)
	o.startTime = getTimestamp()
	return o
end

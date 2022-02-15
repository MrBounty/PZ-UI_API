--***********************************************************
--**                    THE INDIE STONE                    **
--***********************************************************

require "TimedActions/ISBaseTimedAction"
require "ISUI/ISLayoutManager"

---@class ISFireplaceInfoAction : ISBaseTimedAction
ISFireplaceInfoAction = ISBaseTimedAction:derive("ISFireplaceInfoAction")

function ISFireplaceInfoAction:isValid()
	return self.fireplace:getObjectIndex() ~= -1
end

function ISFireplaceInfoAction:perform()
	local window = ISFireplaceInfoWindow.windows[self.character]
	if window then
		window:setObject(self.fireplace)
	else
		local x = getPlayerScreenLeft(self.playerNum)
		local y = getPlayerScreenTop(self.playerNum)
		local w = getPlayerScreenWidth(self.playerNum)
		local h = getPlayerScreenHeight(self.playerNum)
		window = ISFireplaceInfoWindow:new(x + 70, y + 50, self.character, self.fireplace)
		window:initialise()
		window:addToUIManager()
		ISFireplaceInfoWindow.windows[self.character] = window
		if self.character:getPlayerNum() == 0 then
			ISLayoutManager.RegisterWindow('fireplace', ISCollapsableWindow, window)
		end
	end
	window:setVisible(true)
	window:addToUIManager()
	local joypadData = JoypadState.players[self.playerNum+1]
	if joypadData then
		joypadData.focus = window
	end
	-- needed to remove from queue / start next.
	ISBaseTimedAction.perform(self)
end

function ISFireplaceInfoAction:new(character, fireplace)
	local o = {}
	setmetatable(o, self)
	self.__index = self
	o.maxTime = 0
	o.stopOnWalk = true
	o.stopOnRun = true
	o.character = character
	o.playerNum = character:getPlayerNum()
	o.fireplace = fireplace
	return o
end

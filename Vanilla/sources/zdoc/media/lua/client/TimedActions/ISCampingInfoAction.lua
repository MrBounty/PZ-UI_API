--***********************************************************
--**                    THE INDIE STONE                    **
--***********************************************************

require "TimedActions/ISBaseTimedAction"
require "ISUI/ISLayoutManager"

---@class ISCampingInfoAction : ISBaseTimedAction
ISCampingInfoAction = ISBaseTimedAction:derive("IsCampfireInfoAction")

function ISCampingInfoAction:isValid()
	return self.campfire:getObjectIndex() ~= -1
end

function ISCampingInfoAction:perform()
	local window = ISCampingInfoWindow.windows[self.character]
	if window then
		window:setObject(self.campfire)
	else
		local x = getPlayerScreenLeft(self.playerNum)
		local y = getPlayerScreenTop(self.playerNum)
		local w = getPlayerScreenWidth(self.playerNum)
		local h = getPlayerScreenHeight(self.playerNum)
		window = ISCampingInfoWindow:new(x + 70, y + 50, self.character, self.campfire, self.campfireTable)
		window:initialise()
		window:addToUIManager()
		ISCampingInfoWindow.windows[self.character] = window
		if self.character:getPlayerNum() == 0 then
			ISLayoutManager.RegisterWindow('Campfire', ISCollapsableWindow, window)
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

function ISCampingInfoAction:new(character, campfireObject, campfire)
	local o = {}
	setmetatable(o, self)
	self.__index = self
	o.maxTime = 0
	o.stopOnWalk = true
	o.stopOnRun = true
	o.character = character
	o.playerNum = character:getPlayerNum()
	o.campfire = campfireObject
	o.campfireTable = campfire
	return o
end

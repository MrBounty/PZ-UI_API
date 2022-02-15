--***********************************************************
--**                    THE INDIE STONE                    **
--***********************************************************

require "TimedActions/ISBaseTimedAction"
require "ISUI/ISLayoutManager"

---@class ISFurnaceInfoAction : ISBaseTimedAction
ISFurnaceInfoAction = ISBaseTimedAction:derive("ISFurnaceInfoAction")

function ISFurnaceInfoAction:isValid()
	return self.object:getObjectIndex() ~= -1
end

function ISFurnaceInfoAction:perform()
	local window = ISFurnaceInfoWindow.windows[self.character]
	if window then
		window:setObject(self.object)
	else
		local x = getPlayerScreenLeft(self.playerNum)
		local y = getPlayerScreenTop(self.playerNum)
		local w = getPlayerScreenWidth(self.playerNum)
		local h = getPlayerScreenHeight(self.playerNum)
		window = ISFurnaceInfoWindow:new(x + 70, y + 50, self.character, self.object)
		window:initialise()
		window:addToUIManager()
        ISFurnaceInfoWindow.windows[self.character] = window
		if self.character:getPlayerNum() == 0 then
			ISLayoutManager.RegisterWindow('furnace', ISCollapsableWindow, window)
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

function ISFurnaceInfoAction:new(character, object)
	local o = {}
	setmetatable(o, self)
	self.__index = self
	o.maxTime = 0
	o.stopOnWalk = true
	o.stopOnRun = true
	o.character = character
	o.playerNum = character:getPlayerNum()
	o.object = object
	return o
end

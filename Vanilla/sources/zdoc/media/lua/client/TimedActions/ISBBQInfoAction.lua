--***********************************************************
--**                    THE INDIE STONE                    **
--***********************************************************

require "TimedActions/ISBaseTimedAction"
require "ISUI/ISLayoutManager"

---@class ISBBQInfoAction : ISBaseTimedAction
ISBBQInfoAction = ISBaseTimedAction:derive("ISBBQInfoAction")

function ISBBQInfoAction:isValid()
	return self.bbq:getObjectIndex() ~= -1
end

function ISBBQInfoAction:waitToStart()
	self.character:faceThisObject(self.bbq)
	return self.character:shouldBeTurning()
end

function ISBBQInfoAction:perform()
	local window = ISBBQInfoWindow.windows[self.character]
	if window then
		window:setObject(self.bbq)
	else
		local x = getPlayerScreenLeft(self.playerNum)
		local y = getPlayerScreenTop(self.playerNum)
		local w = getPlayerScreenWidth(self.playerNum)
		local h = getPlayerScreenHeight(self.playerNum)
		window = ISBBQInfoWindow:new(x + 70, y + 50, self.character, self.bbq)
		window:initialise()
		window:addToUIManager()
		ISBBQInfoWindow.windows[self.character] = window
		if self.character:getPlayerNum() == 0 then
			ISLayoutManager.RegisterWindow('bbq', ISCollapsableWindow, window)
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

function ISBBQInfoAction:new(character, bbq)
	local o = {}
	setmetatable(o, self)
	self.__index = self
	o.maxTime = 0
	o.stopOnWalk = true
	o.stopOnRun = true
	o.character = character
	o.playerNum = character:getPlayerNum()
	o.bbq = bbq
	return o
end

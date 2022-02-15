--***********************************************************
--**                    ROBERT JOHNSON                     **
--***********************************************************

require "TimedActions/ISBaseTimedAction"
require "ISUI/ISLayoutManager"

---@class ISPlantInfoAction : ISBaseTimedAction
ISPlantInfoAction = ISBaseTimedAction:derive("ISPlantInfoAction");

function ISPlantInfoAction:isValid()
	self.plant:updateFromIsoObject()
	return self.plant:getIsoObject() ~= nil
end

function ISPlantInfoAction:waitToStart()
	self.character:faceThisObject(self.plant:getIsoObject())
	return self.character:shouldBeTurning()
end

function ISPlantInfoAction:update()
end

function ISPlantInfoAction:start()
end

function ISPlantInfoAction:stop()
	ISBaseTimedAction.stop(self);
end

function ISPlantInfoAction:perform()
	local info = ISFarmingMenu.info[self.character]
	if info then
		info.farmingPanel:setPlant(self.plant);
	else
		local x = getPlayerScreenLeft(self.playerNum)
		local y = getPlayerScreenTop(self.playerNum)
		local w = getPlayerScreenWidth(self.playerNum)
		local h = getPlayerScreenHeight(self.playerNum)
		info = ISFarmingWindow:new(x + 70, y + 50,320,240, self.character, self.plant);
		info:initialise();
		info:addToUIManager();
		ISFarmingMenu.info[self.character] = info
		if self.character:getPlayerNum() == 0 then
			ISLayoutManager.RegisterWindow('farming', ISCollapsableWindow, info)
		end
	end
	info:setVisible(true);
	local joypadData = JoypadState.players[self.playerNum+1]
	if joypadData then
		joypadData.focus = info.farmingPanel
	end
    -- needed to remove from queue / start next.
	ISBaseTimedAction.perform(self);
end

function ISPlantInfoAction:new(character, plant)
	local o = {}
	setmetatable(o, self)
	self.__index = self
	o.character = character;
	o.playerNum = character:getPlayerNum()
    o.plant = plant;
	o.maxTime = 0;
	o.stopOnWalk = true;
	o.stopOnRun = true;
	return o;
end

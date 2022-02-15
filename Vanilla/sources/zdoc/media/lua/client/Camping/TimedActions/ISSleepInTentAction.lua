--***********************************************************
--**                    ROBERT JOHNSON                     **
--***********************************************************

require "TimedActions/ISBaseTimedAction"

---@class ISSleepInTentAction : ISBaseTimedAction
ISSleepInTentAction = ISBaseTimedAction:derive("ISSleepInTentAction");

function ISSleepInTentAction:isValid()
	return true;
end

function ISSleepInTentAction:update()
end

function ISSleepInTentAction:start()
end

function ISSleepInTentAction:stop()
    ISBaseTimedAction.stop(self);
end

function ISSleepInTentAction:perform()
	ISWorldObjectContextMenu.onSleep(self.tent, self.character:getPlayerNum());
--	camping.sleep();
	ISBaseTimedAction.perform(self);
end

function ISSleepInTentAction:new (character, tent, time)
	local o = {}
	setmetatable(o, self)
	self.__index = self
	o.character = character;
	o.tent = tent
	o.stopOnWalk = true;
	o.stopOnRun = true;
	o.maxTime = 0;
	return o
end

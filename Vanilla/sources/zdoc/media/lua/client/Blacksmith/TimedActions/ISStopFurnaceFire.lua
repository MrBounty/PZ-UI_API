--***********************************************************
--**                    ROBERT JOHNSON                     **
--***********************************************************

require "TimedActions/ISBaseTimedAction"

---@class ISStopFurnaceFire : ISBaseTimedAction
ISStopFurnaceFire = ISBaseTimedAction:derive("ISStopFurnaceFire");

function ISStopFurnaceFire:isValid()
	return true;
end

function ISStopFurnaceFire:update()
end

function ISStopFurnaceFire:start()
end

function ISStopFurnaceFire:stop()
	ISBaseTimedAction.stop(self);
end

function ISStopFurnaceFire:perform()
	ISBaseTimedAction.perform(self);
    self.furnace:setFireStarted(false);
end

function ISStopFurnaceFire:new(furnace, character)
	local o = {}
	setmetatable(o, self)
	self.__index = self
	o.stopOnWalk = true;
	o.stopOnRun = true;
    o.character = character;
	o.maxTime = 30;
	o.furnace = furnace
	return o;
end

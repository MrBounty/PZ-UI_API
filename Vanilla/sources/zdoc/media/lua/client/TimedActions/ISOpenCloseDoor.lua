--***********************************************************
--**                    ROBERT JOHNSON                     **
--***********************************************************

require "TimedActions/ISBaseTimedAction"

---@class ISOpenCloseDoor : ISBaseTimedAction
ISOpenCloseDoor = ISBaseTimedAction:derive("ISOpenCloseDoor");

function ISOpenCloseDoor:isValid()
	return true;
end

function ISOpenCloseDoor:update()
	self.character:faceThisObject(self.item)
end

function ISOpenCloseDoor:start()
end

function ISOpenCloseDoor:stop()
    ISBaseTimedAction.stop(self);
end

function ISOpenCloseDoor:perform()
	self.item:ToggleDoor(self.character);
    -- needed to remove from queue / start next.
	ISBaseTimedAction.perform(self);
end

function ISOpenCloseDoor:new(character, item, time)
	local o = {}
	setmetatable(o, self)
	self.__index = self
	o.character = character;
	o.item = item;
	--print(item);
	o.stopOnWalk = true;
	o.stopOnRun = true;
	o.maxTime = time;
	return o;
end

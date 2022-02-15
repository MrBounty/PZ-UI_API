--***********************************************************
--**                    ROBERT JOHNSON                     **
--***********************************************************

require "TimedActions/ISBaseTimedAction"

---@class ISOpenCloseCurtain : ISBaseTimedAction
ISOpenCloseCurtain = ISBaseTimedAction:derive("ISOpenCloseCurtain");

function ISOpenCloseCurtain:isValid()
	return true;
end

function ISOpenCloseCurtain:waitToStart()
	self.character:faceThisObjectAlt(self.item)
	return self.character:shouldBeTurning()
end

function ISOpenCloseCurtain:update()
	self.character:faceThisObjectAlt(self.item)
end

function ISOpenCloseCurtain:start()
end

function ISOpenCloseCurtain:stop()
    ISBaseTimedAction.stop(self);
end

function ISOpenCloseCurtain:perform()
	if instanceof(self.item, "IsoDoor") then
		self.item:toggleCurtain()
	else
		self.item:ToggleDoor(self.character);
	end
    -- needed to remove from queue / start next.
	ISBaseTimedAction.perform(self);
end

function ISOpenCloseCurtain:new(character, item, time)
	local o = {}
	setmetatable(o, self)
	self.__index = self
	o.character = character;
	o.item = item;
	o.stopOnWalk = true;
	o.stopOnRun = true;
	o.maxTime = time;
	if o.character:isTimedActionInstant() then o.maxTime = 1; end
	return o;
end

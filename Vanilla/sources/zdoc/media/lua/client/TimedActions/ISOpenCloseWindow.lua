--***********************************************************
--**                    ROBERT JOHNSON                     **
--***********************************************************

require "TimedActions/ISBaseTimedAction"

---@class ISOpenCloseWindow : ISBaseTimedAction
ISOpenCloseWindow = ISBaseTimedAction:derive("ISOpenCloseWindow");

function ISOpenCloseWindow:isValid()
	return true;
end

function ISOpenCloseWindow:update()
end

function ISOpenCloseWindow:start()
end

function ISOpenCloseWindow:stop()
    ISBaseTimedAction.stop(self);
end

function ISOpenCloseWindow:perform()
	if self.item:IsOpen() then
		self.character:closeWindow(self.item)
	else
		self.character:openWindow(self.item)
	end
    -- needed to remove from queue / start next.
	ISBaseTimedAction.perform(self);
end

function ISOpenCloseWindow:new(character, item, time)
	local o = {}
	setmetatable(o, self)
	self.__index = self
	o.character = character;
	o.item = item;
	o.stopOnWalk = true;
	o.stopOnRun = true;
	o.maxTime = time;
	return o;
end

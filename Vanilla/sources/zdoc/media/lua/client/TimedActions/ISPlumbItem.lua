--***********************************************************
--**                    ROBERT JOHNSON                     **
--***********************************************************

require "TimedActions/ISBaseTimedAction"

---@class ISPlumbItem : ISBaseTimedAction
ISPlumbItem = ISBaseTimedAction:derive("ISPlumbItem");

function ISPlumbItem:isValid()
	return self.character:isEquipped(self.wrench);
--	return true;
end

function ISPlumbItem:update()
	self.character:faceThisObject(self.itemToPipe)

    self.character:setMetabolicTarget(Metabolics.MediumWork);
end

function ISPlumbItem:start()
end

function ISPlumbItem:stop()
    ISBaseTimedAction.stop(self);
end

function ISPlumbItem:perform()
	local obj = self.itemToPipe
	local args = { x=obj:getX(), y=obj:getY(), z=obj:getZ(), index=obj:getObjectIndex() }
	sendClientCommand(self.character, 'object', 'plumbObject', args)
    
    -- needed to remove from queue / start next.
	ISBaseTimedAction.perform(self);
end

function ISPlumbItem:new(character, itemToPipe, wrench, time)
	local o = {}
	setmetatable(o, self)
	self.__index = self
	o.character = character;
    o.itemToPipe = itemToPipe;
	self.wrench = wrench;
	o.stopOnWalk = true;
	o.stopOnRun = true;
	o.maxTime = time;
	if o.character:isTimedActionInstant() then o.maxTime = 1; end
	return o;
end

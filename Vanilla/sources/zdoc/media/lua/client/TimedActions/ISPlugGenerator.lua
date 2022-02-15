--***********************************************************
--**                    ROBERT JOHNSON                     **
--***********************************************************

require "TimedActions/ISBaseTimedAction"

---@class ISPlugGenerator : ISBaseTimedAction
ISPlugGenerator = ISBaseTimedAction:derive("ISPlugGenerator");

function ISPlugGenerator:isValid()
	return self.generator:getObjectIndex() ~= -1 and
		self.generator:isConnected() ~= self.plug
end

function ISPlugGenerator:waitToStart()
	self.character:faceThisObject(self.generator)
	return self.character:shouldBeTurning()
end

function ISPlugGenerator:update()
	self.character:faceThisObject(self.generator)
end

function ISPlugGenerator:start()
	self:setActionAnim("Loot")
	self.character:SetVariable("LootPosition", "Low")
	self.character:reportEvent("EventLootItem")
end

function ISPlugGenerator:stop()
    ISBaseTimedAction.stop(self);
end

function ISPlugGenerator:perform()
    self.generator:setConnected(self.plug);
    -- needed to remove from queue / start next.
	ISBaseTimedAction.perform(self);
end

function ISPlugGenerator:new(character, generator, plug, time)
	local o = {}
	setmetatable(o, self)
	self.__index = self
	o.character = getSpecificPlayer(character);
    o.plug = plug;
	o.generator = generator;
	o.stopOnWalk = true;
	o.stopOnRun = true;
	o.maxTime = time;
	if o.character:isTimedActionInstant() then o.maxTime = 1; end
	return o;
end

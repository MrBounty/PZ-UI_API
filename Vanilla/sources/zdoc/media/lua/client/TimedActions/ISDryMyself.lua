--***********************************************************
--**                    ROBERT JOHNSON                     **
--***********************************************************

require "TimedActions/ISBaseTimedAction"

---@class ISDryMyself : ISBaseTimedAction
ISDryMyself = ISBaseTimedAction:derive("ISDryMyself");

function ISDryMyself:isValid()
	return self.character:getInventory():contains(self.item) and self.character:getBodyDamage():getWetness() > 0 and self.item:getUsedDelta() > 0;
end

function ISDryMyself:update()
	self.tick = self.tick + 1;
	if self.tick >= self.timer then
		self.tick = 0;
		--self.character:getBodyDamage():setWetness(self.character:getBodyDamage():getWetness() - (self.character:getBodyDamage():getWetness() / 20));
        self.character:getBodyDamage():decreaseBodyWetness( self.character:getBodyDamage():getWetness() / 20 );
		self.item:Use();
    end
    self.character:setMetabolicTarget(Metabolics.LightDomestic);
end

function ISDryMyself:start()
end

function ISDryMyself:stop()
    ISBaseTimedAction.stop(self);
end

function ISDryMyself:perform()
	if self.item then
		self.item:Use();
	end
    -- needed to remove from queue / start next.
	ISBaseTimedAction.perform(self);
end

function ISDryMyself:new(character, item, time)
	local o = {}
	setmetatable(o, self)
	self.__index = self
	o.character = character;
	o.item = item;
	o.stopOnWalk = true;
	o.stopOnRun = true;
	o.maxTime = time;
	if o.character:isTimedActionInstant() then o.maxTime = 1; end
	o.timer = time / 20;
	o.tick = 0;
    o.caloriesModifier = 4;
	return o;
end

--***********************************************************
--**                    ROBERT JOHNSON                     **
--***********************************************************

require "TimedActions/ISBaseTimedAction"

---@class ISDyeHair : ISBaseTimedAction
ISDyeHair = ISBaseTimedAction:derive("ISDyeHair");

function ISDyeHair:isValid()
	return self.character:getInventory():contains(self.hairDye);
end

function ISDyeHair:update()
end

function ISDyeHair:start()
--	self:setActionAnim(CharacterActionAnims.Pour)
end

function ISDyeHair:stop()
    ISBaseTimedAction.stop(self);
end

function ISDyeHair:perform()
	if self.beard then
		self.character:getHumanVisual():setBeardColor(ImmutableColor.new(self.hairDye:getR(), self.hairDye:getG(), self.hairDye:getB()));
	else
		self.character:getHumanVisual():setHairColor(ImmutableColor.new(self.hairDye:getR(), self.hairDye:getG(), self.hairDye:getB()));
	end
	self.hairDye:Use();
	self.character:resetModel();
	sendVisual(self.character);
	triggerEvent("OnClothingUpdated", self.character)
	
    -- needed to remove from queue / start next.
	ISBaseTimedAction.perform(self);
end

function ISDyeHair:new(character, hairDye, beard, time)
	local o = {}
	setmetatable(o, self)
	self.__index = self
	o.character = character;
    o.hairDye = hairDye;
	o.beard = beard;
	o.stopOnWalk = true;
	o.stopOnRun = true;
	o.maxTime = time;
	if o.character:isTimedActionInstant() then o.maxTime = 1; end
	return o;
end

--***********************************************************
--**                    THE INDIE STONE                    **
--***********************************************************

require "TimedActions/ISBaseTimedAction"

---@class ISFireplaceAddFuel : ISBaseTimedAction
ISFireplaceAddFuel = ISBaseTimedAction:derive("ISFireplaceAddFuel");

function ISFireplaceAddFuel:isValid()
	return self.fireplace:getObjectIndex() ~= -1 and
		self.character:getInventory():contains(self.item)
end

function ISFireplaceAddFuel:waitToStart()
	self.character:faceThisObject(self.fireplace)
	return self.character:shouldBeTurning()
end

function ISFireplaceAddFuel:update()
	self.character:faceThisObject(self.fireplace)
	self.item:setJobDelta(self:getJobDelta());

    self.character:setMetabolicTarget(Metabolics.HeavyDomestic);
end

function ISFireplaceAddFuel:start()
	self.item:setJobType(campingText.addFuel);
	self.item:setJobDelta(0.0);
	self:setActionAnim("Loot")
	self.character:SetVariable("LootPosition", "Low")
end

function ISFireplaceAddFuel:stop()
	ISBaseTimedAction.stop(self);
    self.item:setJobDelta(0.0);
end

function ISFireplaceAddFuel:perform()
	self.item:setJobDelta(0.0);

	if self.item:IsDrainable() then
		self.item:Use()
	else
		self.character:removeFromHands(self.item)
		self.character:getInventory():Remove(self.item)
	end

	local fp = self.fireplace
	local args = { x = fp:getX(), y = fp:getY(), z = fp:getZ(), fuelAmt = self.fuelAmt }
	sendClientCommand(self.character, 'fireplace', 'addFuel', args)

    -- needed to remove from queue / start next.
	ISBaseTimedAction.perform(self);
end

function ISFireplaceAddFuel:new(character, fireplace, item, fuelAmt, time)
	local o = {}
	setmetatable(o, self)
	self.__index = self
	o.character = character;
	o.stopOnWalk = true;
	o.stopOnRun = true;
	o.maxTime = time;
	if o.character:isTimedActionInstant() then o.maxTime = 1; end
	-- custom fields
	o.fireplace = fireplace
	o.fuelAmt = fuelAmt
	o.item = item;
	return o;
end

--***********************************************************
--**                    THE INDIE STONE                    **
--***********************************************************

require "TimedActions/ISBaseTimedAction"

---@class ISBBQAddFuel : ISBaseTimedAction
ISBBQAddFuel = ISBaseTimedAction:derive("ISBBQAddFuel");

function ISBBQAddFuel:isValid()
	return self.fireplace:getObjectIndex() ~= -1 and
		self.character:getInventory():contains(self.item)
end

function ISBBQAddFuel:waitToStart()
	self.character:faceThisObject(self.fireplace)
	return self.character:shouldBeTurning()
end

function ISBBQAddFuel:update()
	self.character:faceThisObject(self.fireplace)
	self.item:setJobDelta(self:getJobDelta());

    self.character:setMetabolicTarget(Metabolics.HeavyDomestic);
end

function ISBBQAddFuel:start()
	self.item:setJobType(campingText.addFuel);
	self.item:setJobDelta(0.0);
	self:setActionAnim("Loot")
	self.character:SetVariable("LootPosition", "Mid")
end

function ISBBQAddFuel:stop()
	ISBaseTimedAction.stop(self);
    self.item:setJobDelta(0.0);
end

function ISBBQAddFuel:perform()
	self.item:setJobDelta(0.0);

	if self.item:IsDrainable() then
		self.item:Use()
	else
		self.character:removeFromHands(self.item)
		self.character:getInventory():Remove(self.item)
	end

	local fp = self.fireplace
	local args = { x = fp:getX(), y = fp:getY(), z = fp:getZ(), fuelAmt = self.fuelAmt }
	sendClientCommand(self.character, 'bbq', 'addFuel', args)

    -- needed to remove from queue / start next.
	ISBaseTimedAction.perform(self);
end

function ISBBQAddFuel:new(character, fireplace, item, fuelAmt, time)
	local o = {}
	setmetatable(o, self)
	self.__index = self
	o.character = character;
	o.stopOnWalk = true;
	o.stopOnRun = true;
	o.maxTime = time;
	-- custom fields
	o.fireplace = fireplace
	o.fuelAmt = fuelAmt
	o.item = item;
	return o;
end

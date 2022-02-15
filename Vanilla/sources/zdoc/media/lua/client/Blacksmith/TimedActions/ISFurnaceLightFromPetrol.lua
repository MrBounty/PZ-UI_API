--***********************************************************
--**                    ROBERT JOHNSON                     **
--***********************************************************

require "TimedActions/ISBaseTimedAction"

---@class ISFurnaceLightFromPetrol : ISBaseTimedAction
ISFurnaceLightFromPetrol = ISBaseTimedAction:derive("ISFurnaceLightFromPetrol");

function ISFurnaceLightFromPetrol:isValid()
--	camping.updateClientCampfire(self.campfire)
	local playerInv = self.character:getInventory()
	return playerInv:contains(self.petrol) and playerInv:contains(self.lighter) and
			self.lighter:getUsedDelta() > 0 and
			self.petrol:getUsedDelta() > 0 and
			self.furnace ~= nil and
			not self.furnace:isFireStarted() and
			self.furnace:getFuelAmount() > 0
end

function ISFurnaceLightFromPetrol:update()
	self.petrol:setJobDelta(self:getJobDelta());
end

function ISFurnaceLightFromPetrol:start()
	self.petrol:setJobType("Lit up");
	self.petrol:setJobDelta(0.0);
end

function ISFurnaceLightFromPetrol:stop()
	ISBaseTimedAction.stop(self);
    self.petrol:setJobDelta(0.0);
end

function ISFurnaceLightFromPetrol:perform()
	self.petrol:getContainer():setDrawDirty(true);
    self.petrol:setJobDelta(0.0);
    self.furnace:setFireStarted(true);
--	camping.lightMyFire(self.campfire, self.character, self.lighter, self.petrol)
    -- needed to remove from queue / start next.
	ISBaseTimedAction.perform(self);
end

function ISFurnaceLightFromPetrol:new(character, furnace, lighter, petrol, time)
	local o = {}
	setmetatable(o, self)
	self.__index = self
	o.character = character;
	o.stopOnWalk = true;
	o.stopOnRun = true;
	o.maxTime = time;
	-- custom fields
	o.furnace = furnace
	o.lighter = lighter
	o.petrol = petrol
	return o;
end

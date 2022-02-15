--***********************************************************
--**                    ROBERT JOHNSON                     **
--***********************************************************

require "TimedActions/ISBaseTimedAction"

---@class ISDrumLightFromPetrol : ISBaseTimedAction
ISDrumLightFromPetrol = ISBaseTimedAction:derive("ISDrumLightFromPetrol");

function ISDrumLightFromPetrol:isValid()
	self.metalDrum:updateFromIsoObject()
	local playerInv = self.character:getInventory()
	return playerInv:contains(self.petrol) and playerInv:contains(self.lighter) and
			self.lighter:getUsedDelta() > 0 and
			self.petrol:getUsedDelta() > 0 and
			self.metalDrum:getIsoObject() ~= nil

end

function ISDrumLightFromPetrol:update()
	self.petrol:setJobDelta(self:getJobDelta());
	self.character:faceThisObject(self.metalDrum:getIsoObject())
end

function ISDrumLightFromPetrol:start()
	self.petrol:setJobType(getText("ContextMenu_LitDrum"));
	self.petrol:setJobDelta(0.0);
end

function ISDrumLightFromPetrol:stop()
	ISBaseTimedAction.stop(self);
    self.petrol:setJobDelta(0.0);
end

function ISDrumLightFromPetrol:perform()
	self.petrol:getContainer():setDrawDirty(true);
    self.petrol:setJobDelta(0.0);

	local md = self.metalDrum
	local args = { x = md.x, y = md.y, z = md.z }
	CMetalDrumSystem.instance:sendCommand(self.character, 'lightFire', args)

    -- needed to remove from queue / start next.
	ISBaseTimedAction.perform(self);
end

function ISDrumLightFromPetrol:new(character, metalDrum, lighter, petrol, time)
	local o = {}
	setmetatable(o, self)
	self.__index = self
	o.character = character;
	o.stopOnWalk = true;
	o.stopOnRun = true;
	o.maxTime = time;
	-- custom fields
	o.metalDrum = metalDrum
	o.lighter = lighter
	o.petrol = petrol
	return o;
end

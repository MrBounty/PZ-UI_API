--***********************************************************
--**                    ROBERT JOHNSON                     **
--***********************************************************

require "TimedActions/ISBaseTimedAction"

---@class ISDrumLightFromLiterature : ISBaseTimedAction
ISDrumLightFromLiterature = ISBaseTimedAction:derive("ISDrumLightFromLiterature");

function ISDrumLightFromLiterature:isValid()
	self.metalDrum:updateFromIsoObject()
	return self.metalDrum:getIsoObject() ~= nil and
		self.character:getInventory():contains(self.lighter) and
		self.character:getInventory():contains(self.item)
end

function ISDrumLightFromLiterature:update()
	self.item:setJobDelta(self:getJobDelta());
	self.character:faceThisObject(self.metalDrum:getIsoObject())
end

function ISDrumLightFromLiterature:start()
	self.item:setJobType("Lit up");
	self.item:setJobDelta(0.0);
end

function ISDrumLightFromLiterature:stop()
	ISBaseTimedAction.stop(self);
    self.item:setJobDelta(0.0);
end

function ISDrumLightFromLiterature:perform()
	self.item:getContainer():setDrawDirty(true);
    self.item:setJobDelta(0.0);
	self.character:getInventory():Remove(self.item);
	self.lighter:Use();

	local fuelAmt = self.fuelAmt
	local md = self.metalDrum
	local args = { x = md.x, y = md.y, z = md.z, fuelAmt = fuelAmt }
	CMetalDrumSystem.instance:sendCommand(self.character, 'lightFire', args)

    -- needed to remove from queue / start next.
	ISBaseTimedAction.perform(self);
end

function ISDrumLightFromLiterature:new(character, item, lighter, metalDrum, fuelAmt, time)
	local o = {}
	setmetatable(o, self)
	self.__index = self
	o.character = character;
	o.metalDrum = metalDrum;
	o.item = item;
	o.lighter = lighter;
	o.fuelAmt = fuelAmt;
	o.stopOnWalk = true;
	o.stopOnRun = true;
	o.maxTime = time;
	return o;
end

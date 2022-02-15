--***********************************************************
--**                    ROBERT JOHNSON                     **
--***********************************************************

require "TimedActions/ISBaseTimedAction"

---@class ISDrumLightFromKindle : ISBaseTimedAction
ISDrumLightFromKindle = ISBaseTimedAction:derive("ISDrumLightFromKindle");

function ISDrumLightFromKindle:isValid()
	self.metalDrum:updateFromIsoObject()
	return self.metalDrum:getIsoObject() ~= nil and self.item ~= nil and
			self.character:getInventory():contains(self.item) and
			self.character:getInventory():contains(self.plank) and
			not self.metalDrum.isLit and
			self.character:getStats():getEndurance() > 0
end

function ISDrumLightFromKindle:update()
	self.item:setJobDelta(self:getJobDelta());
	self.plank:setJobDelta(self:getJobDelta());
	self.character:faceThisObject(self.metalDrum:getIsoObject())
	-- every tick we lower the endurance of the player, he also have a chance to light the fire or broke the kindle
	local endurance = self.character:getStats():getEndurance() - 0.0001 * getGameTime():getMultiplier()
	self.character:getStats():setEndurance(endurance);
	if self:getJobDelta() < 0.2 then return end
	local randNumber = 300;
	local randBrokeNumber = 300;
	if self.isOutdoorsMan then
		randNumber = 150;
		randBrokeNumber = 450;
	end
	if ZombRand(randNumber) == 0 then
		local cf = self.metalDrum
		local args = { x = cf.x, y = cf.y, z = cf.z }
		CMetalDrumSystem.instance:sendCommand(self.character, 'lightFire', args)
	else
		-- fail ? Maybe the wood kit will broke...
		if ZombRand(randBrokeNumber) == 0 then
			self.character:getInventory():Remove(self.item);
		end
	end
end

function ISDrumLightFromKindle:start()
	self.item:setJobType(getText("ContextMenu_LitDrum"));
	self.item:setJobDelta(0.0);
	self.plank:setJobType(getText("ContextMenu_LitDrum"));
	self.plank:setJobDelta(0.0);
end

function ISDrumLightFromKindle:stop()
	ISBaseTimedAction.stop(self);
	if self.item then
		self.item:setJobDelta(0.0);
	end
	self.plank:setJobDelta(0.0);
end

function ISDrumLightFromKindle:perform()
	if self.item then
		self.item:getContainer():setDrawDirty(true);
		self.item:setJobDelta(0.0);
	end
	self.plank:setJobDelta(0.0);
    -- needed to remove from queue / start next.
	ISBaseTimedAction.perform(self);
end

function ISDrumLightFromKindle:new(character, plank, stickOrBranch, metalDrum, time)
	local o = {}
	setmetatable(o, self)
	self.__index = self
	o.character = character;
    o.item = stickOrBranch;
    o.plank = plank;
	o.stopOnWalk = true;
	o.stopOnRun = true;
	o.metalDrum = metalDrum;
	-- if you are a outdoorsman (ranger) you can light the fire faster
	o.isOutdoorsMan = character:HasTrait("Outdoorsman");
	o.maxTime = time;
    o.caloriesModifier = 8;
	return o;
end

--***********************************************************
--**                    ROBERT JOHNSON                     **
--***********************************************************

require "TimedActions/ISBaseTimedAction"

---@class ISFurnaceLightFromKindle : ISBaseTimedAction
ISFurnaceLightFromKindle = ISBaseTimedAction:derive("ISFurnaceLightFromKindle");

function ISFurnaceLightFromKindle:isValid()
--	camping.updateClientCampfire(self.campfire)
	return self.furnace ~= nil and self.item ~= nil and
			self.character:getInventory():contains(self.item) and
			self.character:getInventory():contains(self.plank) and
			not self.furnace:isFireStarted() and
			self.character:getStats():getEndurance() > 0
end

function ISFurnaceLightFromKindle:update()
	self.item:setJobDelta(self:getJobDelta());
	self.plank:setJobDelta(self:getJobDelta());
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
		if isClient() then
			local cf = self.furnace;
			local args = { x = cf.x, y = cf.y, z = cf.z }
--			sendClientCommand(self.character, 'camping', 'lightFire', args)
        else
        self.furnace:setFireStarted(true);
--			camping.lightFire(self.campfire)
		end
	else
		-- fail ? Maybe the wood kit will broke...
		if ZombRand(randBrokeNumber) == 0 then
--~ 			self.character:Say("I broke my kindling...");
			self.character:getInventory():Remove(self.item);
--			self.item = self.character:getInventory():FindAndReturn("WoodenStick");
		end
	end
end

function ISFurnaceLightFromKindle:start()
	self.item:setJobType("Lit up");
	self.item:setJobDelta(0.0);
	self.plank:setJobType("Lit up");
	self.plank:setJobDelta(0.0);
end

function ISFurnaceLightFromKindle:stop()
	ISBaseTimedAction.stop(self);
	if self.item then
		self.item:setJobDelta(0.0);
	end
	self.plank:setJobDelta(0.0);
end

function ISFurnaceLightFromKindle:perform()
	if self.item then
		self.item:getContainer():setDrawDirty(true);
		self.item:setJobDelta(0.0);
	end
	self.plank:setJobDelta(0.0);
    -- needed to remove from queue / start next.
	ISBaseTimedAction.perform(self);
end

function ISFurnaceLightFromKindle:new(character, plank, stickOrBranch, furnace, time)
	local o = {}
	setmetatable(o, self)
	self.__index = self
	o.character = character;
    o.item = stickOrBranch;
    o.plank = plank;
	o.stopOnWalk = true;
	o.stopOnRun = true;
	o.furnace = furnace;
	-- if you are a outdoorsman (ranger) you can light the fire faster
	o.isOutdoorsMan = character:HasTrait("Outdoorsman");
	o.maxTime = time;
    o.caloriesModifier = 8;
	return o;
end

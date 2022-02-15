--***********************************************************
--**                    ROBERT JOHNSON                     **
--***********************************************************

require "TimedActions/ISBaseTimedAction"

---@class ISSeedAction : ISBaseTimedAction
ISSeedAction = ISBaseTimedAction:derive("ISSeedAction");

function ISSeedAction:isValid()
	for _,seed in ipairs(self.seeds) do
		if not self.character:getInventory():contains(seed) then
			return false
		end
	end
	self.plant:updateFromIsoObject()
	return self.plant:getIsoObject() ~= nil
end

function ISSeedAction:waitToStart()
	self.character:faceThisObject(self.plant:getObject())
	return self.character:shouldBeTurning()
end

function ISSeedAction:update()
	self.character:faceThisObject(self.plant:getObject())
    self.character:setMetabolicTarget(Metabolics.HeavyDomestic);
end

function ISSeedAction:start()
	self:setActionAnim("Loot")
	self.character:SetVariable("LootPosition", "Low")
	
	-- used to send loot position
	self.character:reportEvent("EventLootItem");

	self.sound = self.character:playSound("SowSeeds");
end

function ISSeedAction:stop()
	if self.sound and self.sound ~= 0 then
		self.character:getEmitter():stopOrTriggerSound(self.sound)
	end
    ISBaseTimedAction.stop(self);
end

function ISSeedAction:perform()
	if self.sound and self.sound ~= 0 then
		self.character:getEmitter():stopOrTriggerSound(self.sound)
	end

	for i=1, self.nbOfSeed do
		local seed = self.seeds[i];
		self.character:getInventory():Remove(seed);
	end

	local sq = self.plant:getSquare()
	local args = { x = sq:getX(), y = sq:getY(), z = sq:getZ(), typeOfSeed = self.typeOfSeed }
	CFarmingSystem.instance:sendCommand(self.character, 'seed', args)

    -- needed to remove from queue / start next.
	ISBaseTimedAction.perform(self);
end

function ISSeedAction:new(character, seeds, nbOfSeed, typeOfSeed, plant, time)
	local o = {}
	setmetatable(o, self)
	self.__index = self
	o.character = character;
	o.seeds = seeds;
	o.nbOfSeed = nbOfSeed;
	o.typeOfSeed = typeOfSeed;
    o.plant = plant;
	o.stopOnWalk = true;
	o.stopOnRun = true;
	o.maxTime = time;
	if character:isTimedActionInstant() then
		o.maxTime = 1;
	end
	return o;
end

--***********************************************************
--**                    ROBERT JOHNSON                     **
--***********************************************************

require "TimedActions/ISBaseTimedAction"

---@class ISHarvestPlantAction : ISBaseTimedAction
ISHarvestPlantAction = ISBaseTimedAction:derive("ISHarvestPlantAction");

function ISHarvestPlantAction:isValid()
	self.plant:updateFromIsoObject()
	return self.plant:getObject() and self.plant:canHarvest()
end

function ISHarvestPlantAction:waitToStart()
	self.character:faceThisObject(self.plant:getObject())
	return self.character:shouldBeTurning()
end

function ISHarvestPlantAction:update()
	self.character:faceThisObject(self.plant:getObject())
    self.character:setMetabolicTarget(Metabolics.LightWork);
end

function ISHarvestPlantAction:start()
	self:setActionAnim("Loot")
	self.character:SetVariable("LootPosition", "Low")
	
	self.character:reportEvent("EventLootItem");

	self.sound = self.character:playSound("HarvestCrops");
end

function ISHarvestPlantAction:stop()
	if self.sound and self.sound ~= 0 then
		self.character:getEmitter():stopOrTriggerSound(self.sound)
	end
	ISBaseTimedAction.stop(self);
end

function ISHarvestPlantAction:perform()
	if self.sound and self.sound ~= 0 then
		self.character:getEmitter():stopOrTriggerSound(self.sound)
	end

	local sq = self.plant:getSquare()
	local args = { x = sq:getX(), y = sq:getY(), z = sq:getZ() }
	CFarmingSystem.instance:sendCommand(self.character, 'harvest', args)

	-- we successfull harvest our plant, we may gain xp !
	CFarmingSystem.instance:gainXp(self.character, self.plant)

    -- needed to remove from queue / start next.
	ISBaseTimedAction.perform(self);
end

function ISHarvestPlantAction:new(character, plant, time)
	local o = {}
	setmetatable(o, self)
	self.__index = self
	o.character = character;
    o.plant = plant;
	o.maxTime = time;
	o.stopOnWalk = true;
	o.stopOnRun = true;
    o.caloriesModifier = 4;
	if character:isTimedActionInstant() then
		o.maxTime = 1;
	end
	return o;
end

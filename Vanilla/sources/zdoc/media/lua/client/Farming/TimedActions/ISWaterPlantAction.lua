--***********************************************************
--**                    ROBERT JOHNSON                     **
--***********************************************************

require "TimedActions/ISBaseTimedAction"

---@class ISWaterPlantAction : ISBaseTimedAction
ISWaterPlantAction = ISBaseTimedAction:derive("ISWaterPlantAction");

function ISWaterPlantAction:isValid()
	local plant = CFarmingSystem.instance:getLuaObjectOnSquare(self.sq)
	if not plant or plant.waterLvl >= 100 then return false end
	if not self.character:getInventory():contains(self.item) then return false end
	local uses = math.floor(self.item:getUsedDelta() / self.item:getUseDelta())
	if uses > 20 then uses = 20 end
	if uses < self.uses then return false end
	return true;
end

function ISWaterPlantAction:waitToStart()
	self.character:faceLocation(self.sq:getX(), self.sq:getY())
	return self.character:shouldBeTurning()
end

function ISWaterPlantAction:update()
	self.item:setJobDelta(self:getJobDelta());
	self.character:faceLocation(self.sq:getX(), self.sq:getY())
    self.character:setMetabolicTarget(Metabolics.LightWork);
end

function ISWaterPlantAction:start()
	self.item:setJobType(getText("ContextMenu_Water"));
	self.item:setJobDelta(0.0);
	self:setActionAnim(CharacterActionAnims.Pour);
	self:setAnimVariable("FoodType", self.item:getEatType());
	self:setOverrideHandModels(self.item, nil);
	self.sound = self.character:playSound("WaterCrops");
end

function ISWaterPlantAction:stop()
	if self.sound and self.sound ~= 0 then
		self.character:getEmitter():stopOrTriggerSound(self.sound)
	end
	self.item:setJobDelta(0.0);
	ISBaseTimedAction.stop(self);
end

function ISWaterPlantAction:perform()
	self.item:getContainer():setDrawDirty(true);
	self.item:setJobDelta(0.0);

	if self.sound and self.sound ~= 0 then
		self.character:getEmitter():stopOrTriggerSound(self.sound)
	end

	local args = { x = self.sq:getX(), y = self.sq:getY(), z = self.sq:getZ(), uses = self.uses }
	CFarmingSystem.instance:sendCommand(self.character, 'water', args)

	-- Hack: use the water, too hard to get the server to update the client's inventory
	local plant = CFarmingSystem.instance:getLuaObjectOnSquare(self.sq)
	local waterLvl = plant.waterLvl
	for i=1,self.uses do
		if(waterLvl < 100) then
			if self.item:getUsedDelta() > 0 then
				self.item:Use()
			end
			waterLvl = waterLvl + 5
			if(waterLvl > 100) then
				waterLvl = 100
			end
		end
	end

    -- needed to remove from queue / start next.
	ISBaseTimedAction.perform(self);
end

function ISWaterPlantAction:new(character, item, uses, sq, time)
	local o = {}
	setmetatable(o, self)
	self.__index = self
	o.character = character;
	o.item = item;
	o.uses = uses;
	o.stopOnWalk = true;
	o.stopOnRun = true;
	o.maxTime = time;
	if character:isTimedActionInstant() then
		o.maxTime = 1;
	end
    o.sq = sq;
	return o;
end

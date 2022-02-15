--***********************************************************
--**                    ROBERT JOHNSON                     **
--***********************************************************

require "TimedActions/ISBaseTimedAction"

---@class ISFertilizeAction : ISBaseTimedAction
ISFertilizeAction = ISBaseTimedAction:derive("ISFertilizeAction");

function ISFertilizeAction:isValid()
	self.plant:updateFromIsoObject()
	return self.plant:getIsoObject() ~= nil
end

function ISFertilizeAction:waitToStart()
	self.character:faceThisObject(self.plant:getObject())
	return self.character:shouldBeTurning()
end

function ISFertilizeAction:update()
	self.item:setJobDelta(self:getJobDelta());
	self.character:faceThisObject(self.plant:getObject())
    self.character:setMetabolicTarget(Metabolics.LightWork);
end

function ISFertilizeAction:start()
	self.item:setJobType(getText("ContextMenu_Fertilize"));
	self.item:setJobDelta(0.0);
	self:setActionAnim("Loot")
	self.character:SetVariable("LootPosition", "Low")
	
	-- used to send loot position
	self.character:reportEvent("EventLootItem");
end

function ISFertilizeAction:stop()
    ISBaseTimedAction.stop(self);
    self.item:setJobDelta(0.0);
end

function ISFertilizeAction:perform()
	self.item:getContainer():setDrawDirty(true);
	self.item:setJobDelta(0.0);
	
	local sq = self.plant:getSquare()
	local args = { x = sq:getX(), y = sq:getY(), z = sq:getZ() }
	CFarmingSystem.instance:sendCommand(self.character, 'fertilize', args)

	-- MP shouldn't do this directly
	self.item:Use()
	self.character:getInventory():Remove("FertilizerEmpty")

    -- needed to remove from queue / start next.
	ISBaseTimedAction.perform(self);
end

function ISFertilizeAction:new(character, item, plant, time)
	local o = {}
	setmetatable(o, self)
	self.__index = self
	o.character = character;
	o.item = item;
    o.plant = plant;
	o.stopOnWalk = true;
	o.stopOnRun = true;
	o.maxTime = time;
	return o
end

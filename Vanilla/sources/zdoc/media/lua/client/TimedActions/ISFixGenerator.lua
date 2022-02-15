--***********************************************************
--**                    ROBERT JOHNSON                     **
--***********************************************************

require "TimedActions/ISBaseTimedAction"

---@class ISFixGenerator : ISBaseTimedAction
ISFixGenerator = ISBaseTimedAction:derive("ISFixGenerator");

function ISFixGenerator:isValid()
	return self.generator:getObjectIndex() ~= -1 and
		not self.generator:isActivated() and
		self.generator:getCondition() < 100 and
		self.character:getInventory():containsTypeRecurse("ElectronicsScrap")
end

function ISFixGenerator:waitToStart()
	self.character:faceThisObject(self.generator)
	return self.character:shouldBeTurning()
end

function ISFixGenerator:update()
	self.character:faceThisObject(self.generator)

    self.character:setMetabolicTarget(Metabolics.UsingTools);
end

function ISFixGenerator:start()
	self:setActionAnim("Loot")
	self.character:SetVariable("LootPosition", "Low")
	self.character:reportEvent("EventLootItem")
end

function ISFixGenerator:stop()
    ISBaseTimedAction.stop(self);
end

function ISFixGenerator:perform()
    -- needed to remove from queue / start next.
	ISBaseTimedAction.perform(self);

	local scrapItem = self.character:getInventory():getFirstTypeRecurse("ElectronicsScrap");

	if not scrapItem then return; end;
	self.character:removeFromHands(scrapItem);
	self.character:getInventory():Remove(scrapItem);

	self.generator:setCondition(self.generator:getCondition() + 4 + (1*(self.character:getPerkLevel(Perks.Electricity))/2))
	self.character:getXp():AddXP(Perks.Electricity, 5);

	if self.generator:getCondition() < 100 and self.character:getInventory():getFirstTypeRecurse("ElectronicsScrap") then
		ISInventoryPaneContextMenu.transferIfNeeded(self.character, scrapItem);
		ISTimedActionQueue.add(ISFixGenerator:new(self.character, self.generator, 150));
	end
	
end

function ISFixGenerator:new(character, generator, time)
	local o = {}
	setmetatable(o, self)
	self.__index = self
	o.character = character;
	o.generator = generator;
	o.stopOnWalk = true;
	o.stopOnRun = true;
	o.maxTime = time - (o.character:getPerkLevel(Perks.Electricity) * 3);
	if o.character:isTimedActionInstant() then o.maxTime = 1; end
    o.caloriesModifier = 4;
	return o;
end

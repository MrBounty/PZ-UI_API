--***********************************************************
--**                    ROBERT JOHNSON                     **
--***********************************************************

require "TimedActions/ISBaseTimedAction"

---@class ISTakeGenerator : ISBaseTimedAction
ISTakeGenerator = ISBaseTimedAction:derive("ISTakeGenerator");

function ISTakeGenerator:isValid()
	return self.generator:getObjectIndex() ~= -1 and
		not self.generator:isConnected()
end

function ISTakeGenerator:waitToStart()
	self.character:faceThisObject(self.generator)
	return self.character:shouldBeTurning()
end

function ISTakeGenerator:update()
	self.character:faceThisObject(self.generator)

    self.character:setMetabolicTarget(Metabolics.HeavyWork);
end

function ISTakeGenerator:start()
	self:setActionAnim("Loot")
	self.character:SetVariable("LootPosition", "Low")
	self.character:reportEvent("EventLootItem")
end

function ISTakeGenerator:stop()
    ISBaseTimedAction.stop(self);
end

function ISTakeGenerator:perform()
    forceDropHeavyItems(self.character)
    local item = self.character:getInventory():AddItem("Base.Generator");
    item:setCondition(self.generator:getCondition());
    self.character:setPrimaryHandItem(item);
    self.character:setSecondaryHandItem(item);
    if self.generator:getFuel() > 0 then
        item:getModData()["fuel"] = self.generator:getFuel();
    end
    self.character:getInventory():setDrawDirty(true);
    self.generator:remove();

    -- needed to remove from queue / start next.
	ISBaseTimedAction.perform(self);
end

function ISTakeGenerator:new(character, generator, time)
	local o = {}
	setmetatable(o, self)
	self.__index = self
	o.character = getSpecificPlayer(character);
	o.generator = generator;
	o.stopOnWalk = true;
	o.stopOnRun = true;
	o.maxTime = time;
	if o.character:isTimedActionInstant() then o.maxTime = 1; end
	return o;
end

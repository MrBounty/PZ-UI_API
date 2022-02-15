--***********************************************************
--**                    ROBERT JOHNSON                     **
--***********************************************************

require "TimedActions/ISBaseTimedAction"

---@class ISPlaceTrap : ISBaseTimedAction
ISPlaceTrap = ISBaseTimedAction:derive("ISPlaceTrap");

function ISPlaceTrap:isValid()
	return self.character:getInventory():contains(self.weapon);
end

function ISPlaceTrap:update()
	self.weapon:setJobDelta(self:getJobDelta());

    self.character:setMetabolicTarget(Metabolics.UsingTools);
end

function ISPlaceTrap:start()
	self.weapon:setJobType(getText("ContextMenu_TrapPlace", self.weapon:getName()));
	self.weapon:setJobDelta(0.0);
	self:setOverrideHandModels(nil, nil)
	self:setActionAnim("Loot")
	self.character:SetVariable("LootPosition", "Low")
end

function ISPlaceTrap:stop()
	self.weapon:setJobDelta(0.0);
    ISBaseTimedAction.stop(self);
end

function ISPlaceTrap:perform()
	self.weapon:setJobDelta(0.0);

    local trap = IsoTrap.new(self.weapon, self.square:getCell(), self.square);
    self.square:AddTileObject(trap);
    if isClient() then
        self.square:syncIsoTrap(self.weapon);
    end

	self.character:removeFromHands(self.weapon)
    self.character:getInventory():Remove(self.weapon);
    -- needed to remove from queue / start next.
	ISBaseTimedAction.perform(self);
end

function ISPlaceTrap:new(character, weapon, time)
	local o = {}
	setmetatable(o, self)
	self.__index = self
	o.character = character;
    o.square = character:getCurrentSquare();
	o.weapon = weapon;
	o.stopOnWalk = true;
	o.stopOnRun = true;
	o.maxTime = time;
	if o.character:isTimedActionInstant() then o.maxTime = 1; end
	return o;
end

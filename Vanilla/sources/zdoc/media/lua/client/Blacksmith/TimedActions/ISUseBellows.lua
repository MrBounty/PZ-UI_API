--***********************************************************
--**                    ROBERT JOHNSON                     **
--***********************************************************

require "TimedActions/ISBaseTimedAction"

---@class ISUseBellows : ISBaseTimedAction
ISUseBellows = ISBaseTimedAction:derive("ISUseBellows");

function ISUseBellows:isValid()
	local playerInv = self.character:getInventory()
	return playerInv:contains(self.bellows)
end

function ISUseBellows:update()
	self.bellows:setJobDelta(self:getJobDelta());
    self.furnace:setHeat(self.furnace:getHeat() + 0.3);
    self.character:getStats():setEndurance(self.character:getStats():getEndurance() - 0.0002);

    self.character:setMetabolicTarget(Metabolics.HeavyWork);
end

function ISUseBellows:start()
	self.bellows:setJobType(getText("ContextMenu_UseBellows"));
	self.bellows:setJobDelta(0.0);
end

function ISUseBellows:stop()
	ISBaseTimedAction.stop(self);
    self.bellows:setJobDelta(0.0);
    self.furnace:syncFurnace();
end

function ISUseBellows:perform()
	self.bellows:getContainer():setDrawDirty(true);
    self.bellows:setJobDelta(0.0);
    self.furnace:syncFurnace();
    -- needed to remove from queue / start next.
	ISBaseTimedAction.perform(self);
end

function ISUseBellows:new(furnace, bellows, character)
	local o = {}
	setmetatable(o, self)
	self.__index = self
	o.character = character;
	o.stopOnWalk = true;
	o.stopOnRun = true;
	o.maxTime = 300;
	-- custom fields
	o.furnace = furnace
	o.bellows = bellows;
	return o;
end

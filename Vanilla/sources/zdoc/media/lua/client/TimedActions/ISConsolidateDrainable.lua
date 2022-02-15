--***********************************************************
--**                    THE INDIE STONE                    **
--***********************************************************

require "TimedActions/ISBaseTimedAction"

---@class ISConsolidateDrainable : ISBaseTimedAction
ISConsolidateDrainable = ISBaseTimedAction:derive("ISConsolidateDrainable");

function ISConsolidateDrainable:isValid()
    return self.character:getInventory():contains(self.intoItem) and self.character:getInventory():contains(self.drainable);
end

function ISConsolidateDrainable:update()
	self.drainable:setJobDelta(self:getJobDelta());
	self.intoItem:setJobDelta(self:getJobDelta());
	local fromDelta = self.fromStart + (self.fromTarget - self.fromStart) * self:getJobDelta()
	self.drainable:setUsedDelta(fromDelta)
	local intoDelta = self.intoStart + (self.intoTarget - self.intoStart) * self:getJobDelta()
	self.intoItem:setUsedDelta(intoDelta)
end

function ISConsolidateDrainable:start()
	local jobType = getText("IGUI_JobType_PourIn");
	if self.intoItem:canConsolidate() and self.intoItem:getConsolidateOption() then
		jobType = getText(self.intoItem:getConsolidateOption());
	end
	self.drainable:setJobType(jobType);
	self.intoItem:setJobType(jobType);
	self.fromStart = self.drainable:getUsedDelta()
	self.intoStart = self.intoItem:getUsedDelta()
	local maxAdd = 1 - self.intoItem:getUsedDelta()
	local maxTake = self.drainable:getUsedDelta()
	local amount = math.min(maxAdd, maxTake)
	self.fromTarget = self.fromStart - amount
	self.intoTarget = self.intoStart + amount
end

function ISConsolidateDrainable:stop()
	self.drainable:setJobDelta(0.0);
	self.intoItem:setJobDelta(0.0);
    ISBaseTimedAction.stop(self);
end

function ISConsolidateDrainable:perform()
	self.drainable:setJobDelta(0.0);
	self.intoItem:setJobDelta(0.0);
    if self.intoItem:getUsedDelta() > self.intoStart and self.drainable:isTaintedWater() then
        self.intoItem:setTaintedWater(true);
    end
	if self.drainable:getUsedDelta() <= 0.0001 then
		self.drainable:Use()
	end
    -- needed to remove from queue / start next.
    ISBaseTimedAction.perform(self);
end

function ISConsolidateDrainable:new(character, drainable, intoItem, time)
	local o = {}
	setmetatable(o, self)
	self.__index = self
	o.character = getSpecificPlayer(character);
	o.drainable = drainable;
	o.intoItem = intoItem;
	o.stopOnWalk = true;
	o.stopOnRun = true;
	o.maxTime = time;
	return o;
end

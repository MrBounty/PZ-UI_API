--***********************************************************
--**                    ROBERT JOHNSON                     **
--***********************************************************

require "TimedActions/ISBaseTimedAction"

---@class ISPlaceCampfireAction : ISBaseTimedAction
ISPlaceCampfireAction = ISBaseTimedAction:derive("ISPlaceCampfireAction");

function ISPlaceCampfireAction:isValid()
	return self.character:getInventory():contains(self.item)
end

function ISPlaceCampfireAction:waitToStart()
	self.character:faceLocation(self.sq:getX(), self.sq:getY())
	return self.character:shouldBeTurning()
end

function ISPlaceCampfireAction:update()
	self.item:setJobDelta(self:getJobDelta());
	self.character:faceLocation(self.sq:getX(), self.sq:getY())
    self.character:setMetabolicTarget(Metabolics.HeavyDomestic);
end

function ISPlaceCampfireAction:start()
	self.item:setJobType(campingText.placeCampfire);
	self.item:setJobDelta(0.0);
end

function ISPlaceCampfireAction:stop()
    ISBaseTimedAction.stop(self);
    self.item:setJobDelta(0.0);
end

function ISPlaceCampfireAction:perform()
	self.character:removeFromHands(self.item)
	self.item:getContainer():setDrawDirty(true);
    self.item:setJobDelta(0.0);
	self.character:getInventory():Remove("CampfireKit");

	local args = { x = self.sq:getX(), y = self.sq:getY(), z = self.sq:getZ() }
	CCampfireSystem.instance:sendCommand(self.character, 'addCampfire', args)

	-- needed to remove from queue / start next.
	ISBaseTimedAction.perform(self);
end

function ISPlaceCampfireAction:new (character, sq, item, time)
	local o = {}
	setmetatable(o, self)
	self.__index = self
	o.character = character;
	o.item = item;
	o.stopOnWalk = true;
	o.stopOnRun = true;
	o.maxTime = time;
    o.sq = sq;
	return o
end

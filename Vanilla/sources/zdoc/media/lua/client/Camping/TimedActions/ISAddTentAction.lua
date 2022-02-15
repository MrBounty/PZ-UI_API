--***********************************************************
--**                    ROBERT JOHNSON                     **
--***********************************************************

require "TimedActions/ISBaseTimedAction"

---@class ISAddTentAction : ISBaseTimedAction
ISAddTentAction = ISBaseTimedAction:derive("ISAddTentAction");

function ISAddTentAction:isValid()
	return true;
end

function ISAddTentAction:update()
	self.item:setJobDelta(self:getJobDelta());
    self.character:setMetabolicTarget(Metabolics.LightWork);
end

function ISAddTentAction:start()
	self.item:setJobType(campingText.addTent);
	self.item:setJobDelta(0.0);
end

function ISAddTentAction:stop()
    ISBaseTimedAction.stop(self);
    self.item:setJobDelta(0.0);
end

function ISAddTentAction:perform()
	self.item:getContainer():setDrawDirty(true);
    self.item:setJobDelta(0.0);
	self.character:getInventory():Remove("CampingTentKit");
	local args = { x = self.sq:getX(), y = self.sq:getY(), z = self.sq:getZ(), sprite = self.sprite }
	sendClientCommand(self.character, 'camping', 'addTent', args)

	ISBaseTimedAction.perform(self);
end

function ISAddTentAction:new (character, sq,item, sprite, time)
	local o = {}
	setmetatable(o, self)
	self.__index = self
	o.character = character;
	o.item = item;
	o.stopOnWalk = true;
	o.stopOnRun = true;
	o.maxTime = time;
	o.sprite = sprite
    o.sq = sq;
	return o
end

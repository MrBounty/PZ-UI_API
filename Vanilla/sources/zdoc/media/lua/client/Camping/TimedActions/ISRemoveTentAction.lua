--***********************************************************
--**                    ROBERT JOHNSON                     **
--***********************************************************

require "TimedActions/ISBaseTimedAction"

---@class ISRemoveTentAction : ISBaseTimedAction
ISRemoveTentAction = ISBaseTimedAction:derive("ISRemoveTentAction");

function ISRemoveTentAction:isValid()
	return self.tent and self.tent:getObjectIndex() >= 0
end

function ISRemoveTentAction:update()
	self.character:faceThisObject(self.tent)
    self.character:setMetabolicTarget(Metabolics.LightWork);
end

function ISRemoveTentAction:start()
	self:setActionAnim("Loot")
	self.character:SetVariable("LootPosition", "Mid")
	self:setOverrideHandModels(nil, nil)
end

function ISRemoveTentAction:stop()
    ISBaseTimedAction.stop(self);
end

function ISRemoveTentAction:perform()
	local sq = self.tent:getSquare()
	local args = { x = sq:getX(), y = sq:getY(), z = sq:getZ() }
	sendClientCommand(self.character, 'camping', 'removeTent', args)

	-- needed to remove from queue / start next.
	ISBaseTimedAction.perform(self);
end

function ISRemoveTentAction:new(character, tent, time)
	local o = {}
	setmetatable(o, self)
	self.__index = self
	o.character = character;
	o.stopOnWalk = true;
	o.stopOnRun = true;
	o.maxTime = time;
	o.tent = tent;
	return o
end

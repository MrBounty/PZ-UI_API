--***********************************************************
--**                    ROBERT JOHNSON                     **
--***********************************************************

require "TimedActions/ISBaseTimedAction"

---@class ISCheckTrapAction : ISBaseTimedAction
ISCheckTrapAction = ISBaseTimedAction:derive("ISCheckTrapAction");

function ISCheckTrapAction:isValid()
	self.trap:updateFromIsoObject()
	return self.trap:getIsoObject() ~= nil and self.trap.animal.type ~= nil;
end

function ISCheckTrapAction:update()
	self.character:faceThisObject(self.trap:getIsoObject())
    self.character:setMetabolicTarget(Metabolics.HeavyDomestic);
end

function ISCheckTrapAction:start()
	self:setActionAnim("Loot")
	self.character:SetVariable("LootPosition", "Low")
	self:setOverrideHandModels(nil, nil)
end

function ISCheckTrapAction:stop()
    ISBaseTimedAction.stop(self);
end

function ISCheckTrapAction:perform()
	local sq = self.trap:getSquare()
	local args = { x = sq:getX(), y = sq:getY(), z = sq:getZ() }
	CTrapSystem.instance:sendCommand(self.character, 'removeAnimal', args)

	ISBaseTimedAction.perform(self);
end

function ISCheckTrapAction:new(character, trap, time)
	local o = {}
	setmetatable(o, self)
	self.__index = self
	o.character = character;
	o.trap = trap;
    o.maxTime = time;
	return o;
end

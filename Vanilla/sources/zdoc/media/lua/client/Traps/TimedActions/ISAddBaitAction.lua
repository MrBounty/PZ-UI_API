--***********************************************************
--**                    ROBERT JOHNSON                     **
--***********************************************************

require "TimedActions/ISBaseTimedAction"

---@class ISAddBaitAction : ISBaseTimedAction
ISAddBaitAction = ISBaseTimedAction:derive("ISAddBaitAction");

function ISAddBaitAction:isValid()
	self.trap:updateFromIsoObject()
	return self.trap:getIsoObject() ~= nil
end

function ISAddBaitAction:waitToStart()
	self.character:faceThisObject(self.trap:getIsoObject())
	return self.character:shouldBeTurning()
end

function ISAddBaitAction:update()
	self.character:faceThisObject(self.trap:getIsoObject())
    self.character:setMetabolicTarget(Metabolics.LightDomestic);
end

function ISAddBaitAction:start()
	self:setActionAnim("Loot")
	self.character:SetVariable("LootPosition", "Low")
	self:setOverrideHandModels(nil, nil)
end

function ISAddBaitAction:stop()
    ISBaseTimedAction.stop(self);
end

function ISAddBaitAction:perform()
	local sq = self.trap:getSquare()

	local bait = self.bait
	local baitAmountMulti = bait:getHungChange();
	bait:multiplyFoodValues(1.0 - math.min(-0.05 / bait:getHungChange(), 1.0))
	if bait:getHungerChange() > -0.01 then
		bait:Use();
	end
	baitAmountMulti = math.min(baitAmountMulti - bait:getHungChange(), 0);

	local args = { x = sq:getX(), y = sq:getY(), z = sq:getZ(), bait = self.bait:getFullType(), age = self.bait:getAge(), baitAmountMulti = baitAmountMulti };
	CTrapSystem.instance:sendCommand(self.character, 'addBait', args)

	ISBaseTimedAction.perform(self);
end

function ISAddBaitAction:new(character, bait, trap, time)
	local o = {}
	setmetatable(o, self)
	self.__index = self
	o.character = character;
	o.trap = trap;
    o.bait = bait;
    o.maxTime = time;
	return o;
end

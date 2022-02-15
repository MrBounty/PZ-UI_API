--***********************************************************
--**                    ROBERT JOHNSON                     **
--***********************************************************

require "TimedActions/ISBaseTimedAction"

---@class ISRemoveBaitAction : ISBaseTimedAction
ISRemoveBaitAction = ISBaseTimedAction:derive("ISRemoveBaitAction");

function ISRemoveBaitAction:isValid()
	self.trap:updateFromIsoObject()
	return self.trap:getIsoObject() ~= nil and self.trap.bait ~= nil
end

function ISRemoveBaitAction:waitToStart()
	self.character:faceThisObject(self.trap:getIsoObject())
	return self.character:shouldBeTurning()
end

function ISRemoveBaitAction:update()
	self.character:faceThisObject(self.trap:getIsoObject())
    self.character:setMetabolicTarget(Metabolics.LightDomestic);
end

function ISRemoveBaitAction:start()
	self:setActionAnim("Loot")
	self.character:SetVariable("LootPosition", "Low")
	self:setOverrideHandModels(nil, nil)
end

function ISRemoveBaitAction:stop()
    ISBaseTimedAction.stop(self);
end

function ISRemoveBaitAction:perform()
	local sq = self.trap:getSquare()
	local args = { x = sq:getX(), y = sq:getY(), z = sq:getZ() }
	CTrapSystem.instance:sendCommand(self.character, 'removeBait', args)

	if self.trap.bait and self.trap.baitAmountMulti then
		if self.trap.baitAmountMulti < 0 then
			local bait = self.character:getInventory():AddItem(self.trap.bait);
			local worldAge = getGameTime():getWorldAgeHours();
			local baitAge = self.trap.trapBaitDay + (math.abs((worldAge / 24) - self.trap.lastUpdate));
			bait:setAge(baitAge);
			bait:setLastAged(worldAge);
			local baitMultiplier = math.min(self.trap.baitAmountMulti / bait:getHungChange(), 1.0);
			bait:multiplyFoodValues(baitMultiplier);
		end;
	end;

	ISBaseTimedAction.perform(self);
end

function ISRemoveBaitAction:new(character, trap, time)
	local o = {}
	setmetatable(o, self)
	self.__index = self
	o.character = character;
	o.trap = trap;
    o.maxTime = time;
	return o;
end

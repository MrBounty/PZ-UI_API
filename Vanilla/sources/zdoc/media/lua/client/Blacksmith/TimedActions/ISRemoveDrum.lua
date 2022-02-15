--***********************************************************
--**                    ROBERT JOHNSON                     **
--***********************************************************

require "TimedActions/ISBaseTimedAction"

---@class ISRemoveDrum : ISBaseTimedAction
ISRemoveDrum = ISBaseTimedAction:derive("ISRemoveDrum");

function ISRemoveDrum:isValid()
	return true;
end

function ISRemoveDrum:update()
    self.character:setMetabolicTarget(Metabolics.HeavyDomestic);
end

function ISRemoveDrum:start()
end

function ISRemoveDrum:stop()
	ISBaseTimedAction.stop(self);
end

function ISRemoveDrum:perform()
	ISBaseTimedAction.perform(self);
    self.metalDrum:getSquare():RemoveTileObject(self.metalDrum);
    self.character:getInventory():AddItem("Base.MetalDrum");
end

function ISRemoveDrum:new(character, metalDrum)
	local o = {}
	setmetatable(o, self)
	self.__index = self
	o.stopOnWalk = true;
	o.stopOnRun = true;
	o.maxTime = 30;
    o.metalDrum = metalDrum;
	o.character  = character;
	return o;
end

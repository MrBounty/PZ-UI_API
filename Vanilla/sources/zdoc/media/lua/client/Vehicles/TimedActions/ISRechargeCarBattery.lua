--***********************************************************
--**                    THE INDIE STONE                    **
--***********************************************************

require "TimedActions/ISBaseTimedAction"

---@class ISRechargeCarBattery : ISBaseTimedAction
ISRechargeCarBattery = ISBaseTimedAction:derive("ISRechargeCarBattery")

function ISRechargeCarBattery:isValid()
	return self.character:getInventory():contains(self.charger) and self.character:getInventory():contains(self.battery) and self.battery:getUsedDelta() <= 1;
end

function ISRechargeCarBattery:update()
	self.battery:setUsedDelta(self.battery:getUsedDelta() + (0.00033*getGameTime():getMultiplier()));
end

function ISRechargeCarBattery:start()
	
end

function ISRechargeCarBattery:stop()
	ISBaseTimedAction.stop(self)
end

function ISRechargeCarBattery:perform()
	self.battery:setUsedDelta(1);
	-- needed to remove from queue / start next.
	ISBaseTimedAction.perform(self)
end

function ISRechargeCarBattery:new(battery, carCharger, chr)
	local o = {}
	setmetatable(o, self)
	self.__index = self
	o.character = chr;
	o.battery = battery;
	o.charger = carCharger;
	o.maxTime = (1-battery:getUsedDelta()) * 3000;
	return o
end


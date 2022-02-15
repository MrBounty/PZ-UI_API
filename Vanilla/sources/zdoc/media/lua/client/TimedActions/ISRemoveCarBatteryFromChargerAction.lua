--***********************************************************
--**                    THE INDIE STONE                    **
--***********************************************************

require "TimedActions/ISBaseTimedAction"

---@class ISRemoveCarBatteryFromChargerAction : ISBaseTimedAction
ISRemoveCarBatteryFromChargerAction = ISBaseTimedAction:derive("ISRemoveCarBatteryFromChargerAction")

function ISRemoveCarBatteryFromChargerAction:isValid()
	return self.charger:getObjectIndex() ~= -1 and
		self.charger:getBattery() ~= nil
end

function ISRemoveCarBatteryFromChargerAction:waitToStart()
	self.character:faceThisObject(self.charger)
	return self.character:shouldBeTurning()
end

function ISRemoveCarBatteryFromChargerAction:start()
	self:setActionAnim("Loot")
	self.character:SetVariable("LootPosition", "Low")
end

function ISRemoveCarBatteryFromChargerAction:update()
	self.character:faceThisObject(self.charger)
end

function ISRemoveCarBatteryFromChargerAction:stop()
	ISBaseTimedAction.stop(self)
end

function ISRemoveCarBatteryFromChargerAction:perform()
	local sq = self.charger:getSquare()
	local args = { x = sq:getX(), y = sq:getY(), z = sq:getZ() }
	sendClientCommand(self.character, 'carBatteryCharger', 'removeBattery', args)

	-- needed to remove from queue / start next.
	ISBaseTimedAction.perform(self)
end

function ISRemoveCarBatteryFromChargerAction:new(character, charger, time)
	local o = ISBaseTimedAction.new(self, character)
	o.stopOnWalk = true
	o.stopOnRun = true
	o.maxTime = time
	o.charger = charger
	return o
end

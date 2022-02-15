--***********************************************************
--**                    THE INDIE STONE                    **
--***********************************************************

require "TimedActions/ISBaseTimedAction"

---@class ISPlaceCarBatteryChargerAction : ISBaseTimedAction
ISPlaceCarBatteryChargerAction = ISBaseTimedAction:derive("ISPlaceCarBatteryChargerAction")

function ISPlaceCarBatteryChargerAction:isValid()
	return self.character:getInventory():contains(self.charger)
end

function ISPlaceCarBatteryChargerAction:waitToStart()
	local sq = self.character:getSquare()
	self.character:faceLocation(sq:getX(), sq:getY())
	return self.character:shouldBeTurning()
end

function ISPlaceCarBatteryChargerAction:start()
	self:setActionAnim("Loot")
	self.character:SetVariable("LootPosition", "Low")
end

function ISPlaceCarBatteryChargerAction:update()
	local sq = self.character:getSquare()
	self.character:faceLocation(sq:getX(), sq:getY())
end

function ISPlaceCarBatteryChargerAction:stop()
	ISBaseTimedAction.stop(self)
end

function ISPlaceCarBatteryChargerAction:perform()
	local sq = self.character:getSquare()
	local args = { x = sq:getX(), y = sq:getY(), z = sq:getZ() }
	args.item = self.charger
	sendClientCommand(self.character, 'carBatteryCharger', 'place', args)

	-- needed to remove from queue / start next.
	ISBaseTimedAction.perform(self)
end

function ISPlaceCarBatteryChargerAction:new(character, charger, time)
	local o = ISBaseTimedAction.new(self, character)
	o.stopOnWalk = true
	o.stopOnRun = true
	o.maxTime = time
	o.charger = charger
	return o
end

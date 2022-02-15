--***********************************************************
--**                    THE INDIE STONE                    **
--***********************************************************

require "TimedActions/ISBaseTimedAction"

---@class ISConnectCarBatteryToChargerAction : ISBaseTimedAction
ISConnectCarBatteryToChargerAction = ISBaseTimedAction:derive("ISConnectCarBatteryToChargerAction")

function ISConnectCarBatteryToChargerAction:isValid()
	return self.charger:getObjectIndex() ~= -1 and
		self.character:getInventory():contains(self.battery)
end

function ISConnectCarBatteryToChargerAction:waitToStart()
	self.character:faceThisObject(self.charger)
	return self.character:shouldBeTurning()
end

function ISConnectCarBatteryToChargerAction:start()
	self.battery:setJobType(getText("ContextMenu_AddBattery"))
	self.battery:setJobDelta(0.0)
	self:setActionAnim("Loot")
	self.character:SetVariable("LootPosition", "Low")
end

function ISConnectCarBatteryToChargerAction:update()
	self.character:faceThisObject(self.charger)
	self.battery:setJobDelta(self:getJobDelta())
end

function ISConnectCarBatteryToChargerAction:stop()
	self.battery:setJobDelta(0.0)
	ISBaseTimedAction.stop(self)
end

function ISConnectCarBatteryToChargerAction:perform()
	self.battery:setJobDelta(0.0)
	local sq = self.charger:getSquare()
	local args = { x = sq:getX(), y = sq:getY(), z = sq:getZ() }
	args.battery = self.battery
	sendClientCommand(self.character, 'carBatteryCharger', 'connectBattery', args)

	-- needed to remove from queue / start next.
	ISBaseTimedAction.perform(self)
end

function ISConnectCarBatteryToChargerAction:new(character, charger, battery, time)
	local o = ISBaseTimedAction.new(self, character)
	o.stopOnWalk = true
	o.stopOnRun = true
	o.maxTime = time
	o.character = character
	o.charger = charger
	o.battery = battery
	return o
end

--***********************************************************
--**                    THE INDIE STONE                    **
--***********************************************************

require "TimedActions/ISBaseTimedAction"

---@class ISTakeCarBatteryChargerAction : ISBaseTimedAction
ISTakeCarBatteryChargerAction = ISBaseTimedAction:derive("ISTakeCarBatteryChargerAction")

function ISTakeCarBatteryChargerAction:isValid()
	return self.charger:getObjectIndex() ~= -1 and self.charger:getBattery() == nil
end

function ISTakeCarBatteryChargerAction:waitToStart()
	self.character:faceThisObject(self.charger)
	return self.character:shouldBeTurning()
end

function ISTakeCarBatteryChargerAction:update()
	self.character:faceThisObject(self.charger)
end

function ISTakeCarBatteryChargerAction:start()
	self:setActionAnim("Loot")
	self.character:SetVariable("LootPosition", "Low")
end

function ISTakeCarBatteryChargerAction:stop()
	ISBaseTimedAction.stop(self)
end

function ISTakeCarBatteryChargerAction:perform()
	local sq = self.charger:getSquare()
	local args = { x = sq:getX(), y = sq:getY(), z = sq:getZ() }
	sendClientCommand(self.character, 'carBatteryCharger', 'take', args)

	-- needed to remove from queue / start next.
	ISBaseTimedAction.perform(self)
end

function ISTakeCarBatteryChargerAction:new(character, charger, time)
	local o = ISBaseTimedAction.new(self, character)
	o.stopOnWalk = true
	o.stopOnRun = true
	o.maxTime = time
	o.charger = charger
	return o
end	

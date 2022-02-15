--***********************************************************
--**                    THE INDIE STONE                    **
--***********************************************************

require "TimedActions/ISBaseTimedAction"

---@class ISActivateCarBatteryChargerAction : ISBaseTimedAction
ISActivateCarBatteryChargerAction = ISBaseTimedAction:derive("ISActivateCarBatteryChargerAction")

function ISActivateCarBatteryChargerAction:isValid()
	return self.charger:getObjectIndex() ~= -1 and
		self.charger:isActivated() ~= self.activate
end

function ISActivateCarBatteryChargerAction:waitToStart()
	self.character:faceThisObject(self.charger)
	return self.character:shouldBeTurning()
end

function ISActivateCarBatteryChargerAction:start()
	self:setActionAnim("Loot")
	self.character:SetVariable("LootPosition", "Low")
end

function ISActivateCarBatteryChargerAction:update()
	self.character:faceThisObject(self.charger)
end

function ISActivateCarBatteryChargerAction:stop()
	ISBaseTimedAction.stop(self)
end

function ISActivateCarBatteryChargerAction:perform()
	local sq = self.charger:getSquare()
	local args = { x = sq:getX(), y = sq:getY(), z = sq:getZ() }
	args.activate = self.activate
	sendClientCommand(self.character, 'carBatteryCharger', 'activate', args)

	sq:playSound("LightSwitch")

	-- needed to remove from queue / start next.
	ISBaseTimedAction.perform(self)
end

function ISActivateCarBatteryChargerAction:new(character, charger, activate, time)
	local o = ISBaseTimedAction.new(self, character)
	o.stopOnWalk = true
	o.stopOnRun = true
	o.maxTime = time
	o.charger = charger
	o.activate = activate
	return o
end

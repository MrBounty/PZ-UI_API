--***********************************************************
--**                    THE INDIE STONE                    **
--***********************************************************

require "TimedActions/ISBaseTimedAction"

---@class ISHotwireVehicle : ISBaseTimedAction
ISHotwireVehicle = ISBaseTimedAction:derive("ISHotwireVehicle")

function ISHotwireVehicle:isValid()
	local vehicle = self.character:getVehicle()
	return vehicle ~= nil and
--		vehicle:isEngineWorking() and
		vehicle:isDriver(self.character) and
		not vehicle:isEngineRunning() and 
		not vehicle:isEngineStarted()
end

function ISHotwireVehicle:update()
    self.character:setMetabolicTarget(Metabolics.HeavyDomestic);
end

function ISHotwireVehicle:start()
	self.sound = self.character:getEmitter():playSound("VehicleHotwireStart")
end

function ISHotwireVehicle:stop()
	self:stopSound()
	ISBaseTimedAction.stop(self)
end

function ISHotwireVehicle:perform()
	self:stopSound()

	sendClientCommand(self.character, 'vehicle', 'hotwireEngine', {electricSkill=self.character:getPerkLevel(Perks.Electricity)})

	-- needed to remove from queue / start next.
	ISBaseTimedAction.perform(self)
end

function ISHotwireVehicle:stopSound()
	if self.sound and self.character:getEmitter():isPlaying(self.sound) then
		self.character:stopOrTriggerSound(self.sound);
	end
end

function ISHotwireVehicle:new(character)
	local o = {}
	setmetatable(o, self)
	self.__index = self
	o.character = character
	o.maxTime = 200 - (character:getPerkLevel(Perks.Electricity) * 3);
	return o
end


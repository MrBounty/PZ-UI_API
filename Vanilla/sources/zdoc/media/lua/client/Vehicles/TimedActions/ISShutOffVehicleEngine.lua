--***********************************************************
--**                    THE INDIE STONE                    **
--***********************************************************

require "TimedActions/ISBaseTimedAction"

---@class ISShutOffVehicleEngine : ISBaseTimedAction
ISShutOffVehicleEngine = ISBaseTimedAction:derive("ISShutOffVehicleEngine")

function ISShutOffVehicleEngine:isValid()
	local vehicle = self.character:getVehicle()
	return vehicle ~= nil and
		vehicle:isEngineRunning() and
		vehicle:isDriver(self.character)
end

function ISShutOffVehicleEngine:update()
	self:forceComplete()
end

function ISShutOffVehicleEngine:start()
end

function ISShutOffVehicleEngine:stop()
	ISBaseTimedAction.stop(self)
end

function ISShutOffVehicleEngine:perform()
	local vehicle = self.character:getVehicle()
	sendClientCommand(self.character, 'vehicle', 'shutOff', {})

	-- needed to remove from queue / start next.
	ISBaseTimedAction.perform(self)
end

function ISShutOffVehicleEngine:new(character)
	local o = {}
	setmetatable(o, self)
	self.__index = self
	o.character = character
	o.maxTime = -1
	return o
end


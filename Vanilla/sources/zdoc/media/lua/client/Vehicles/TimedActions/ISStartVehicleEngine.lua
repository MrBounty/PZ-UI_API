--***********************************************************
--**                    THE INDIE STONE                    **
--***********************************************************

require "TimedActions/ISBaseTimedAction"

---@class ISStartVehicleEngine : ISBaseTimedAction
ISStartVehicleEngine = ISBaseTimedAction:derive("ISStartVehicleEngine")

function ISStartVehicleEngine:isValid()
	local vehicle = self.character:getVehicle()
	return vehicle ~= nil and
--		vehicle:isEngineWorking() and
		vehicle:isDriver(self.character) and
		not vehicle:isEngineRunning() and 
		not vehicle:isEngineStarted()
end

function ISStartVehicleEngine:update()
	self:forceComplete()
end

function ISStartVehicleEngine:start()
end

function ISStartVehicleEngine:stop()
	ISBaseTimedAction.stop(self)
end

function ISStartVehicleEngine:perform()
	local vehicle = self.character:getVehicle()
	local haveKey = false;
	if self.character:getInventory():haveThisKeyId(vehicle:getKeyId()) then
		haveKey = true;
	end
	sendClientCommand(self.character, 'vehicle', 'startEngine', {haveKey=haveKey})

	-- needed to remove from queue / start next.
	ISBaseTimedAction.perform(self)
end

function ISStartVehicleEngine:new(character)
	local o = {}
	setmetatable(o, self)
	self.__index = self
	o.character = character
	o.maxTime = -1
	return o
end


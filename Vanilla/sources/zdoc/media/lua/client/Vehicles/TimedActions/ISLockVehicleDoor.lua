--***********************************************************
--**                    THE INDIE STONE                    **
--***********************************************************

require "TimedActions/ISBaseTimedAction"

---@class ISLockVehicleDoor : ISBaseTimedAction
ISLockVehicleDoor = ISBaseTimedAction:derive("ISLockVehicleDoor")

function ISLockVehicleDoor:isValid()
	return self.part:getDoor() and not self.part:getDoor():isLocked()
end

function ISLockVehicleDoor:update()
	if not self.character:getVehicle() then
		self.character:faceThisObject(self.vehicle)
	end
end

function ISLockVehicleDoor:start()
	self.vehicle:playPartSound(self.part, "Lock")
end

function ISLockVehicleDoor:stop()
	ISBaseTimedAction.stop(self)
end

function ISLockVehicleDoor:perform()
	local args = { vehicle = self.vehicle:getId(), part = self.part:getId(), locked = true }
	sendClientCommand(self.character, 'vehicle', 'setDoorLocked', args)

	-- needed to remove from queue / start next.
	ISBaseTimedAction.perform(self)
end

function ISLockVehicleDoor:new(character, part)
	local o = {}
	setmetatable(o, self)
	self.__index = self
	o.character = character
	o.vehicle = part:getVehicle()
	o.part = part
	o.maxTime = 0
	return o
end


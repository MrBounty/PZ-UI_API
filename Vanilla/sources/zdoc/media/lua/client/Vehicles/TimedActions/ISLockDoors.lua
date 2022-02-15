--***********************************************************
--**                    THE INDIE STONE                    **
--***********************************************************

require "TimedActions/ISBaseTimedAction"

---@class ISLockDoors : ISBaseTimedAction
ISLockDoors = ISBaseTimedAction:derive("ISLockDoors")

function ISLockDoors:isValid()
	return self.character:getVehicle() == self.vehicle;
end

function ISLockDoors:update()
	
end

function ISLockDoors:start()
	for seat=1,self.vehicle:getMaxPassengers() do
		local part = self.vehicle:getPassengerDoor(seat-1)
		if part then
			self.vehicle:playPartSound(part, self.locked and "Lock" or "Unlock")
			break
		end
	end
end

function ISLockDoors:stop()
	ISBaseTimedAction.stop(self)
end

function ISLockDoors:perform()
	for seat=1,self.vehicle:getMaxPassengers() do
		local part = self.vehicle:getPassengerDoor(seat-1)
		if part then
			local args = { vehicle = self.vehicle:getId(), part = part:getId(), locked = self.locked }
			sendClientCommand(self.character, 'vehicle', 'setDoorLocked', args)
		end
	end
	if JoypadState.players[self.character:getPlayerNum()+1] then
		-- Hack: Mouse players click the trunk icon in the dashboard.
		sendClientCommand(self.character, 'vehicle', 'setTrunkLocked', { locked = self.locked })
	end
	-- needed to remove from queue / start next.
	ISBaseTimedAction.perform(self)
end

function ISLockDoors:new(character, vehicle, locked, time)
	local o = {}
	setmetatable(o, self)
	self.__index = self
	o.character = character
	o.vehicle = vehicle
	o.locked = locked
	o.maxTime = time
	return o
end


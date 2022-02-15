--***********************************************************
--**                    THE INDIE STONE                    **
--***********************************************************

require "TimedActions/ISBaseTimedAction"

---@class ISUnlockVehicleDoor : ISBaseTimedAction
ISUnlockVehicleDoor = ISBaseTimedAction:derive("ISUnlockVehicleDoor")

function ISUnlockVehicleDoor:isValid()
	--print("ISUnlockVehicleDoor:isValid()")
	--print(self.part:getDoor() and self.part:getDoor():isLocked())
	return self.part:getDoor() and (self.forceValid or self.part:getDoor():isLocked())
end

function ISUnlockVehicleDoor:update()
	if not self.character:getVehicle() then
		self.character:faceThisObject(self.vehicle)
	end
	--print("ISUnlockVehicleDoor:update()")
	-- TODO: drunk/panic = fumble
end

function ISUnlockVehicleDoor:start()
	if not self.character:getVehicle() then
		self.character:faceThisObject(self.vehicle)
	end
	self.vehicle:toggleLockedDoor(self.part, self.character, false)
	if self.part:getDoor():isLocked() then
		if self.part:getDoor():isLockBroken() then
			self.character:Say(getText("IGUI_PlayerText_VehicleLockIsBroken"))
		end
		self.vehicle:playPartSound(self.part, "IsLocked");
		self:forceStop();
        return;
	end
	self.vehicle:playPartSound(self.part, "Unlock")
	if isClient() then
		local args = { vehicle = self.vehicle:getId(), part = self.part:getId(), locked = false }
		sendClientCommand(self.character, 'vehicle', 'setDoorLocked', args)
	end
	-- isValid() will return false since the door isn't locked now
	self.forceValid = true
	self:forceComplete()
end

function ISUnlockVehicleDoor:stop()
	ISBaseTimedAction.stop(self)
end

function ISUnlockVehicleDoor:perform()
	-- needed to remove from queue / start next.
	ISBaseTimedAction.perform(self)
end

function ISUnlockVehicleDoor:new(character, part, seat)
	local o = {}
	setmetatable(o, self)
	self.__index = self
	o.character = character
	o.vehicle = part:getVehicle()
	o.part = part
	o.seat = seat
	o.forceValid = false
	o.maxTime = -1
	return o
end


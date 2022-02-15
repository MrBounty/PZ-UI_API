--***********************************************************
--**                    THE INDIE STONE                    **
--***********************************************************

require "TimedActions/ISBaseTimedAction"

---@class ISCloseVehicleDoor : ISBaseTimedAction
ISCloseVehicleDoor = ISBaseTimedAction:derive("ISCloseVehicleDoor")

function ISCloseVehicleDoor:isValid()
	return self.part and self.part:getDoor() and self.part:getDoor():isOpen()
end

function ISCloseVehicleDoor:update()
--	if self.door:isAnimationFinished() then
	self.character:PlayAnim("Idle")
	if self.character:getSpriteDef():isFinished() then
		self:forceComplete()
	end
end

function ISCloseVehicleDoor:start()
	-- TODO: sync part animation + sound
	self.vehicle:playPartAnim(self.part, "Close")
	self.vehicle:playPartSound(self.part, "Close")
	self.action:setOverrideAnimation(true)
	if self.seat then
		self.vehicle:playPassengerAnim(self.seat, "closeDoor", self.character)
	else
		-- TODO: move player to exact position so player/door animations line up
		self.vehicle:playActorAnim(self.part, "Close", self.character)
	end
	-- Set this here to negate the effects of injuries, negative moodles, etc.
	self.action:setTime(4)
end

function ISCloseVehicleDoor:stop()
	-- TODO: interrupted, close door again?
	ISBaseTimedAction.stop(self)
end

function ISCloseVehicleDoor:perform()
	local args = { vehicle = self.vehicle:getId(), part = self.part:getId(), open = false }
	sendClientCommand(self.character, 'vehicle', 'setDoorOpen', args)
	-- FIXME: due to network delay, we should wait until the server tells the client the door is closed before finishing
	self.part:getDoor():setOpen(false)
	triggerEvent("OnContainerUpdate")
	if self.character:getVehicle() and self.seat then
		self.vehicle:playPassengerAnim(self.seat, "idle")
	else
		self.character:PlayAnim("Idle")
	end
	-- needed to remove from queue / start next.
	ISBaseTimedAction.perform(self)
end

function ISCloseVehicleDoor:new(character, vehicle, partOrSeat)
	local o = {}
	setmetatable(o, self)
	self.__index = self
	o.character = character
	o.vehicle = vehicle
	if instanceof(partOrSeat, "VehiclePart") then
		o.part = partOrSeat
	else
		o.seat = partOrSeat
		o.part = vehicle:getPassengerDoor(partOrSeat)
	end
	o.maxTime = -1
	return o
end


--***********************************************************
--**                    THE INDIE STONE                    **
--***********************************************************

require "TimedActions/ISBaseTimedAction"

---@class ISAttachTrailerToVehicle : ISBaseTimedAction
ISAttachTrailerToVehicle = ISBaseTimedAction:derive("ISAttachTrailerToVehicle")

function ISAttachTrailerToVehicle:isValid()
	return self.vehicleA:getVehicleTowing() == nil
end

function ISAttachTrailerToVehicle:waitToStart()
	self.vehicleA:getTowingWorldPos(self.attachmentA, self.hitchPos) 
	self.character:faceLocationF(self.hitchPos:x(), self.hitchPos:y())
	return self.character:shouldBeTurning()
end

function ISAttachTrailerToVehicle:update()
	self.vehicleA:getTowingWorldPos(self.attachmentA, self.hitchPos) 
	self.character:faceLocationF(self.hitchPos:x(), self.hitchPos:y())
	self.character:setMetabolicTarget(Metabolics.HeavyDomestic);
end

function ISAttachTrailerToVehicle:start()
	self:setActionAnim("VehicleTrailer")
end

function ISAttachTrailerToVehicle:stop()
	ISBaseTimedAction.stop(self)
end

function ISAttachTrailerToVehicle:perform()
	local square = self.vehicleA:getCurrentSquare()
	local vehicleB = ISVehicleTrailerUtils.getTowableVehicleNear(square, self.vehicleA, self.attachmentA, self.attachmentB)
	if vehicleB == self.vehicleB then
		self.vehicleA:addPointConstraint(self.vehicleB, self.attachmentA, self.attachmentB)
	end
	-- needed to remove from queue / start next.
	ISBaseTimedAction.perform(self)
end

function ISAttachTrailerToVehicle:new(character, vehicleA, vehicleB, attachmentA, attachmentB)
	local o = ISBaseTimedAction.new(self, character)
	o.stopOnWalk = true
	o.stopOnRun = true
	o.maxTime = 100
	o.vehicleA = vehicleA
	o.vehicleB = vehicleB
	o.attachmentA = attachmentA
	o.attachmentB = attachmentB
	o.hitchPos = Vector3f.new()
	if character:isTimedActionInstant() then
		o.maxTime = 1
	end
	return o
end


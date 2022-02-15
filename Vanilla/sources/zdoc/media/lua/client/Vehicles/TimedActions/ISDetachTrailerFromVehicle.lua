--***********************************************************
--**                    THE INDIE STONE                    **
--***********************************************************

require "TimedActions/ISBaseTimedAction"

---@class ISDetachTrailerFromVehicle : ISBaseTimedAction
ISDetachTrailerFromVehicle = ISBaseTimedAction:derive("ISDetachTrailerFromVehicle")

function ISDetachTrailerFromVehicle:isValid()
	return self.vehicle:getVehicleTowing() ~= nil
end

function ISDetachTrailerFromVehicle:waitToStart()
	self.vehicle:getTowingWorldPos(self.attachment, self.hitchPos) 
	self.character:faceLocationF(self.hitchPos:x(), self.hitchPos:y())
	return self.character:shouldBeTurning()
end

function ISDetachTrailerFromVehicle:update()
	self.vehicle:getTowingWorldPos(self.attachment, self.hitchPos) 
	self.character:faceLocationF(self.hitchPos:x(), self.hitchPos:y())
	self.character:setMetabolicTarget(Metabolics.HeavyDomestic);
end

function ISDetachTrailerFromVehicle:start()
	self:setActionAnim("VehicleTrailer")
end

function ISDetachTrailerFromVehicle:stop()
	ISBaseTimedAction.stop(self)
end

function ISDetachTrailerFromVehicle:perform()
	self.vehicle:breakConstraint(true, false)
	-- needed to remove from queue / start next.
	ISBaseTimedAction.perform(self)
end

function ISDetachTrailerFromVehicle:new(character, vehicle, attachment)
	local o = ISBaseTimedAction.new(self, character)
	o.stopOnWalk = true
	o.stopOnRun = true
	o.maxTime = 100
	o.vehicle = vehicle
	o.attachment = attachment
	o.hitchPos = Vector3f.new()
	if character:isTimedActionInstant() then
		o.maxTime = 1
	end
	return o
end


--***********************************************************
--**                    THE INDIE STONE                    **
--***********************************************************

require "TimedActions/ISBaseTimedAction"

---@class ISDeflateTire : ISBaseTimedAction
ISDeflateTire = ISBaseTimedAction:derive("ISDeflateTire")

function ISDeflateTire:isValid()
--	return self.vehicle:isInArea(self.part:getArea(), self.character)
	return true;
end

function ISDeflateTire:update()
	self.character:faceThisObject(self.vehicle)
	local psi = self.psiStart + (self.psiTarget - self.psiStart) * self:getJobDelta()
	if math.floor(psi) ~= self.psiSent then
		local args = { vehicle = self.vehicle:getId(), part = self.part:getId(), psi = psi }
		sendClientCommand(self.character, 'vehicle', 'setTirePressure', args)
		self.psiSent = math.floor(psi)
	end
end

function ISDeflateTire:start()
	if isClient() then
		self.psiSent = math.floor(self.psiStart)
	end
end

function ISDeflateTire:stop()
	ISBaseTimedAction.stop(self)
end

function ISDeflateTire:perform()
	local args = { vehicle = self.vehicle:getId(), part = self.part:getId(), psi = self.psiTarget }
	sendClientCommand(self.character, 'vehicle', 'setTirePressure', args)

	-- needed to remove from queue / start next.
	ISBaseTimedAction.perform(self)
end

function ISDeflateTire:new(character, part, psi, time)
	local o = {}
	setmetatable(o, self)
	self.__index = self
	o.character = character
	o.maxTime = time
	o.vehicle = part:getVehicle()
	o.part = part
	o.psiStart = part:getContainerContentAmount()
	o.psiTarget = psi
	o.jobType = getText("IGUI_JobType_DeflateTire")
	return o
end


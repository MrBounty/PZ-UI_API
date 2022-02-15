--***********************************************************
--**                    THE INDIE STONE                    **
--***********************************************************

require "TimedActions/ISBaseTimedAction"

---@class ISInflateTire : ISBaseTimedAction
ISInflateTire = ISBaseTimedAction:derive("ISInflateTire")

function ISInflateTire:isValid()
--	return self.vehicle:isInArea(self.part:getArea(), self.character)
	-- The tire might explode while inflating.
	return self.part:getInventoryItem() ~= nil
end

function ISInflateTire:update()
	self.character:faceThisObject(self.vehicle)
	self.item:setJobDelta(self:getJobDelta())
	self.item:setJobType(getText("IGUI_JobType_InflateTire"))
	local psi = self.psiStart + (self.psiTarget - self.psiStart) * self:getJobDelta();
	self.totalPsi = psi;
	if math.floor(psi) ~= self.psiSent then
		local args = { vehicle = self.vehicle:getId(), part = self.part:getId(), psi = psi }
		sendClientCommand(self.character, 'vehicle', 'setTirePressure', args)
		self.psiSent = math.floor(psi)
	end
end

function ISInflateTire:start()
	self.psiSent = math.floor(self.psiStart)
end

function ISInflateTire:stop()
	if self.item then self.item:setJobDelta(0) end
	ISBaseTimedAction.stop(self)
end

function ISInflateTire:perform()
	self.item:setJobDelta(0)
	local args = { vehicle = self.vehicle:getId(), part = self.part:getId(), psi = self.totalPsi }
	sendClientCommand(self.character, 'vehicle', 'setTirePressure', args)

	-- needed to remove from queue / start next.
	ISBaseTimedAction.perform(self)
end

function ISInflateTire:new(character, part, item, psi, time)
	local o = {}
	setmetatable(o, self)
	self.__index = self
	o.character = character
	o.maxTime = time
	o.vehicle = part:getVehicle()
	o.part = part
	o.item = item
	o.psiStart = part:getContainerContentAmount()
	o.psiTarget = psi
	o.jobType = getText("IGUI_JobType_InflateTire")
	return o
end


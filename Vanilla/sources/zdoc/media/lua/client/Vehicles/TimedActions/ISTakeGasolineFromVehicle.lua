--***********************************************************
--**                    THE INDIE STONE                    **
--***********************************************************

require "TimedActions/ISBaseTimedAction"

---@class ISTakeGasolineFromVehicle : ISBaseTimedAction
ISTakeGasolineFromVehicle = ISBaseTimedAction:derive("ISTakeGasolineFromVehicle")

function ISTakeGasolineFromVehicle:isValid()
--	return self.vehicle:isInArea(self.part:getArea(), self.character)
	return true;
end

function ISTakeGasolineFromVehicle:waitToStart()
	self.character:faceThisObject(self.vehicle)
	return self.character:shouldBeTurning()
end

function ISTakeGasolineFromVehicle:update()
	self.character:faceThisObject(self.vehicle)
	self.item:setJobDelta(self:getJobDelta())
	self.item:setJobType(getText("ContextMenu_VehicleSiphonGas"))
	local litres = self.tankStart + (self.tankTarget - self.tankStart) * self:getJobDelta()
	litres = math.ceil(litres)
	if litres ~= self.amountSent then
		local args = { vehicle = self.vehicle:getId(), part = self.part:getId(), amount = litres }
		sendClientCommand(self.character, 'vehicle', 'setContainerContentAmount', args)
		self.amountSent = litres
	end
	local litresTaken = self.tankStart - litres
	local usedDelta = self.itemStart + litresTaken / Vehicles.JerryCanLitres
	self.item:setUsedDelta(usedDelta)

    self.character:setMetabolicTarget(Metabolics.HeavyDomestic);
end

function ISTakeGasolineFromVehicle:start()
	if self.item:getType() == "EmptyPetrolCan" then
		local wasPrimary = self.character:getPrimaryHandItem() == self.item
		local wasSecondary = self.character:getSecondaryHandItem() == self.item
		self.character:getInventory():DoRemoveItem(self.item)
		self.item = self.character:getInventory():AddItem("Base.PetrolCan")
		self.item:setUsedDelta(0)
		if wasPrimary then
			self.character:setPrimaryHandItem(self.item)
		end
		if wasSecondary then
			self.character:setSecondaryHandItem(self.item)
		end
	end
	self.tankStart = self.part:getContainerContentAmount()
	self.itemStart = self.item:getUsedDelta()
	local add = (1.0 - self.itemStart) * Vehicles.JerryCanLitres
	local take = math.min(add, self.tankStart)
	self.tankTarget = self.tankStart - take
	self.itemTarget = self.itemStart + take / Vehicles.JerryCanLitres
	self.amountSent = math.ceil(self.tankStart)

	self.action:setTime(take * 50)

	self:setActionAnim("TakeGasFromVehicle")
	self:setOverrideHandModels(nil, self.item:getStaticModel())
end

function ISTakeGasolineFromVehicle:stop()
	self.item:setJobDelta(0)
	ISBaseTimedAction.stop(self)
end

function ISTakeGasolineFromVehicle:perform()
	self.item:setJobDelta(0)
	self.item:setUsedDelta(self.itemTarget)
	local args = { vehicle = self.vehicle:getId(), part = self.part:getId(), amount = self.tankTarget }
	sendClientCommand(self.character, 'vehicle', 'setContainerContentAmount', args)
	print('take fluid level=' .. self.part:getContainerContentAmount() .. ' usedDelta=' .. self.item:getUsedDelta())
	-- needed to remove from queue / start next.
	ISBaseTimedAction.perform(self)
end

function ISTakeGasolineFromVehicle:new(character, part, item, time)
	local o = {}
	setmetatable(o, self)
	self.__index = self
	o.character = character
	o.vehicle = part:getVehicle()
	o.part = part
	o.item = item
	o.maxTime = time
	return o
end


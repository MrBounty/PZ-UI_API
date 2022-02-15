--***********************************************************
--**                   THE INDIE STONE                     **
--***********************************************************

require "TimedActions/ISBaseTimedAction"

---@class ISAddWaterFromItemAction : ISBaseTimedAction
ISAddWaterFromItemAction = ISBaseTimedAction:derive("ISAddWaterFromItemAction")

function ISAddWaterFromItemAction:isValid()
	return self.itemFrom:isWaterSource() and self.character:getInventory():contains(self.itemFrom) and
		self.objectTo:getObjectIndex() ~= -1 and
		self.objectTo:getWaterAmount() < self.objectTo:getWaterMax()
end

function ISAddWaterFromItemAction:waitToStart()
	self.character:faceThisObject(self.objectTo)
	return self.character:shouldBeTurning()
end

function ISAddWaterFromItemAction:update()
	self.character:faceThisObject(self.objectTo)
	self.itemFrom:setJobDelta(self:getJobDelta())
	local unitsSoFar = math.floor(self.addUnits * self:getJobDelta())
	self.itemFrom:setUsedDelta(self.itemFromStartDelta - unitsSoFar * self.itemFrom:getUseDelta())

    self.character:setMetabolicTarget(Metabolics.LightDomestic);
end

function ISAddWaterFromItemAction:start()
	self.itemFrom:setJobType(getText("IGUI_JobType_PourOut"))
	self.itemFrom:setJobDelta(0.0)
	self.itemFromStartDelta = self.itemFrom:getUsedDelta()
	local waterAvailable = self.itemFrom:getUsedDelta() / self.itemFrom:getUseDelta()
	if waterAvailable - math.floor(waterAvailable) > 0.99 then
		waterAvailable = math.ceil(waterAvailable)
	end
	local destCapacity = self.objectTo:getWaterMax() - self.objectTo:getWaterAmount()
	self.addUnits = math.min(destCapacity, waterAvailable)

	-- The time should be the same as ISTakeWaterAction.
	self.action:setTime(math.max(6, self.addUnits) * 7)

	self:setAnimVariable("FoodType", self.itemFrom:getEatType());
	self:setActionAnim("fill_container_tap");
	if not self.itemFrom:getEatType() then
		self:setOverrideHandModels(nil, self.itemFrom:getStaticModel())
	else
		self:setOverrideHandModels(self.itemFrom:getStaticModel(), nil)
	end

	self.sound = self.character:playSound("PourWaterIntoObject")

	self.character:reportEvent("EventTakeWater");
end

function ISAddWaterFromItemAction:stop()
	self:stopSound()
	self.itemFrom:setJobDelta(0.0)
	-- Possibly start() wasn't called yet (because isValid() returned false)
	if self.addUnits and self.addUnits > 0 then
		local unitsSoFar = math.floor(self.addUnits * self:getJobDelta())
		self.itemFrom:setUsedDelta(self.itemFromStartDelta - unitsSoFar * self.itemFrom:getUseDelta())
		if self.itemFrom:getUsedDelta() < 0.0001 then
			self.itemFrom:Use()
		end
		self.objectTo:setWaterAmount(self.objectTo:getWaterAmount() + unitsSoFar)
		if self.itemFrom:isTaintedWater() then
			self.objectTo:setTaintedWater(true)
		end
		self.objectTo:transmitModData()
	end
	ISBaseTimedAction.stop(self)
end

function ISAddWaterFromItemAction:perform()
	self:stopSound()
	self.itemFrom:getContainer():setDrawDirty(true)
	self.itemFrom:setJobDelta(0.0)

	self.itemFrom:setUsedDelta(self.itemFromStartDelta - self.addUnits * self.itemFrom:getUseDelta())

	if self.itemFrom:getUsedDelta() < 0.0001 then
		self.itemFrom:Use()
	end

	if self.itemFrom:isTaintedWater() then
		self.objectTo:setTaintedWater(true)
	end
	self.objectTo:setWaterAmount(self.objectTo:getWaterAmount() + self.addUnits)
	if instanceof(self.objectTo, "IsoWorldInventoryObject") then
		-- TODO: Sync usedDelta
	else
		self.objectTo:transmitModData()
	end

	-- needed to remove from queue / start next.
	ISBaseTimedAction.perform(self)
end

function ISAddWaterFromItemAction:stopSound()
	if self.sound and self.character:getEmitter():isPlaying(self.sound) then
		self.character:stopOrTriggerSound(self.sound);
	end
end

function ISAddWaterFromItemAction:new(character, itemFrom, objectTo)
	local o = {}
	setmetatable(o, self)
	self.__index = self
	o.character = character
	o.itemFrom = itemFrom
	o.objectTo = objectTo
	o.stopOnWalk = true
	o.stopOnRun = true
	o.maxTime = 10 -- will set this in start()
	return o
end    	

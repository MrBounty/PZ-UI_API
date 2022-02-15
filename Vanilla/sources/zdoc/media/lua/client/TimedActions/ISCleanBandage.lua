--***********************************************************
--**                    THE INDIE STONE                    **
--***********************************************************

require "TimedActions/ISBaseTimedAction"

---@class ISCleanBandage : ISBaseTimedAction
ISCleanBandage = ISBaseTimedAction:derive("ISCleanBandage")

function ISCleanBandage:isValid()
	if self.item:getContainer() ~= self.character:getInventory() then return false end
	return self.waterObject:hasWater()
end

function ISCleanBandage:waitToStart()
	self.character:faceThisObject(self.waterObject)
	return self.character:shouldBeTurning()
end

function ISCleanBandage:update()
	self.item:setJobDelta(self:getJobDelta())
	self.character:faceThisObject(self.waterObject)
end

function ISCleanBandage:start()
	self.item:setJobType(self.recipe:getName())
	self:setActionAnim("Craft")
end

function ISCleanBandage:stop()
	self.item:setJobDelta(0.0)
	ISBaseTimedAction.stop(self)
end

function ISCleanBandage:perform()
	local primary = self.character:isPrimaryHandItem(self.item)
	local secondary = self.character:isSecondaryHandItem(self.item)
	self.character:getInventory():Remove(self.item)
	local item = self.character:getInventory():AddItem(self.result)
	if primary then
		self.character:setPrimaryHandItem(item)
	end
	if secondary then
		self.character:setSecondaryHandItem(item)
	end

	ISTakeWaterAction.SendTakeWaterCommand(self.character, self.waterObject, 1)

	-- needed to remove from queue / start next.
	ISBaseTimedAction.perform(self)
end

function ISCleanBandage:new(character, item, waterObject, recipe)
	local o = ISBaseTimedAction.new(self, character)
	o.stopOnWalk = true
	o.stopOnRun = true
	o.maxTime = recipe:getTimeToMake()
	if o.character:isTimedActionInstant() then o.maxTime = 1; end
	o.item = item
	o.result = recipe:getResult():getType()
	o.waterObject = waterObject
	o.recipe = recipe
	return o
end    	

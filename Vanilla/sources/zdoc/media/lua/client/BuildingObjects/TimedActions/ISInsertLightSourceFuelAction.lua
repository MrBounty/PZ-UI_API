--***********************************************************
--**                    THE INDIE STONE                    **
--***********************************************************

require "TimedActions/ISBaseTimedAction"

---@class ISInsertLightSourceFuelAction : ISBaseTimedAction
ISInsertLightSourceFuelAction = ISBaseTimedAction:derive("ISInsertLightSourceFuelAction");

function ISInsertLightSourceFuelAction:isValid()
	return self.character:getInventory():contains(self.fuel)
end

function ISInsertLightSourceFuelAction:start()
	self.fuel:setJobType("Insert")
	self.fuel:setJobDelta(0.0)
end

function ISInsertLightSourceFuelAction:update()
	self.fuel:setJobDelta(self:getJobDelta())
end

function ISInsertLightSourceFuelAction:stop()
	self.fuel:setJobDelta(0.0)
	ISBaseTimedAction.stop(self)
end

function ISInsertLightSourceFuelAction:perform()
	self.fuel:setJobDelta(0.0)
	local sq = self.lightSource:getSquare()
	local args = { x = sq:getX(), y = sq:getY(), z = sq:getZ() }
	args.itemID = self.fuel:getID()
	args.fuel = self.fuel:getFullType()
	args.usedDelta = self.fuel:getUsedDelta()
	sendClientCommand(self.character, 'object', 'insertFuel', args)

	-- needed to remove from queue / start next.
	ISBaseTimedAction.perform(self)
end

function ISInsertLightSourceFuelAction:new(character, lightSource, fuel, time)
	local o = ISBaseTimedAction.new(self, character)
	o.stopOnWalk = true
	o.stopOnRun = true
	o.maxTime = time
	o.lightSource = lightSource
	o.fuel = fuel
	return o
end

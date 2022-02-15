--***********************************************************
--**                    THE INDIE STONE                    **
--***********************************************************

require "TimedActions/ISBaseTimedAction"

---@class ISRemoveLightSourceFuelAction : ISBaseTimedAction
ISRemoveLightSourceFuelAction = ISBaseTimedAction:derive("ISRemoveLightSourceFuelAction");

function ISRemoveLightSourceFuelAction:isValid()
	return self.lightSource:haveFuel()
end

function ISRemoveLightSourceFuelAction:start()
end

function ISRemoveLightSourceFuelAction:update()
end

function ISRemoveLightSourceFuelAction:stop()
	ISBaseTimedAction.stop(self)
end

function ISRemoveLightSourceFuelAction:perform()
	local sq = self.lightSource:getSquare()
	local args = { x = sq:getX(), y = sq:getY(), z = sq:getZ() }
	sendClientCommand(self.character, 'object', 'removeFuel', args)

	-- needed to remove from queue / start next.
	ISBaseTimedAction.perform(self)
end

function ISRemoveLightSourceFuelAction:new(character, lightSource, time)
	local o = ISBaseTimedAction.new(self, character)
	o.stopOnWalk = true
	o.stopOnRun = true
	o.maxTime = time
	o.lightSource = lightSource
	return o
end

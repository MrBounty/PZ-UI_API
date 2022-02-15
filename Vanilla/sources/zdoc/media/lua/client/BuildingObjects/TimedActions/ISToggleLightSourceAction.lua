--***********************************************************
--**                    THE INDIE STONE                    **
--***********************************************************

require "TimedActions/ISBaseTimedAction"

---@class ISToggleLightSourceAction : ISBaseTimedAction
ISToggleLightSourceAction = ISBaseTimedAction:derive("ISToggleLightSourceAction");

function ISToggleLightSourceAction:isValid()
	return self.lightSource:haveFuel()
end

function ISToggleLightSourceAction:start()
end

function ISToggleLightSourceAction:update()
end

function ISToggleLightSourceAction:stop()
	ISBaseTimedAction.stop(self)
end

function ISToggleLightSourceAction:perform()
	local sq = self.lightSource:getSquare()
	local args = { x = sq:getX(), y = sq:getY(), z = sq:getZ() }
	sendClientCommand(self.character, 'object', 'toggleLight', args)

	-- needed to remove from queue / start next.
	ISBaseTimedAction.perform(self)
end

function ISToggleLightSourceAction:new(character, lightSource, time)
	local o = ISBaseTimedAction.new(self, character)
	o.stopOnWalk = true
	o.stopOnRun = true
	o.maxTime = time
	o.lightSource = lightSource
	return o
end

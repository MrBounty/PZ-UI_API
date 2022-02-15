--***********************************************************
--**                    THE INDIE STONE                    **
--***********************************************************

require "TimedActions/ISBaseTimedAction"

---@class ISBBQToggle : ISBaseTimedAction
ISBBQToggle = ISBaseTimedAction:derive("ISBBQToggle");

function ISBBQToggle:isValid()
	return self.bbq:isPropaneBBQ() and self.bbq:hasFuel()
end

function ISBBQToggle:waitToStart()
	self.character:faceThisObject(self.bbq)
	return self.character:shouldBeTurning()
end

function ISBBQToggle:start()
end

function ISBBQToggle:update()
	self.character:faceThisObject(self.bbq)
end

function ISBBQToggle:stop()
	ISBaseTimedAction.stop(self)
end

function ISBBQToggle:perform()
	local sq = self.bbq:getSquare()
	local args = { x = sq:getX(), y = sq:getY(), z = sq:getZ() }
	sendClientCommand(self.character, 'bbq', 'toggle', args)

	-- needed to remove from queue / start next.
	ISBaseTimedAction.perform(self)
end

function ISBBQToggle:new(character, bbq, time)
	local o = ISBaseTimedAction.new(self, character)
	o.stopOnWalk = true
	o.stopOnRun = true
	o.maxTime = time
	o.bbq = bbq
	return o
end

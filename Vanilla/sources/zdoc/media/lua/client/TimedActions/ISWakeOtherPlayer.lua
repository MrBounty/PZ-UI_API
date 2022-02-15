--***********************************************************
--**                    THE INDIE STONE                    **
--***********************************************************

require "TimedActions/ISBaseTimedAction"

---@class ISWakeOtherPlayer : ISBaseTimedAction
ISWakeOtherPlayer = ISBaseTimedAction:derive("ISWakeOtherPlayer");

function ISWakeOtherPlayer:isValid()
	return self.otherPlayer:isAlive() and self.otherPlayer:isAsleep()
end

function ISWakeOtherPlayer:start()
end

function ISWakeOtherPlayer:update()
	self.character:faceThisObject(self.otherPlayer)
end

function ISWakeOtherPlayer:stop()
	ISBaseTimedAction.stop(self)
end

function ISWakeOtherPlayer:perform()
	local args = { id=self.otherPlayer:getOnlineID() }
	sendClientCommand(self.character, 'player', 'wakeOther', args)

	-- needed to remove from queue / start next.
	ISBaseTimedAction.perform(self)
end

function ISWakeOtherPlayer:new(character, other)
	local o = ISBaseTimedAction.new(self, character)
	o.stopOnWalk = true
	o.stopOnRun = true
	o.maxTime = 30
	o.otherPlayer = other
	return o
end

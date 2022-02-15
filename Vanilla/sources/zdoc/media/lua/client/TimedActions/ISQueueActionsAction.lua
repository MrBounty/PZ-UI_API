--***********************************************************
--**                    THE INDIE STONE                    **
--***********************************************************

require "TimedActions/ISBaseTimedAction"

---@class ISQueueActionsAction : ISBaseTimedAction
ISQueueActionsAction = ISBaseTimedAction:derive("ISQueueActionsAction")

function ISQueueActionsAction:isValid()
	return true
end

function ISQueueActionsAction:waitToStart()
	return false
end

function ISQueueActionsAction:update()
	-- An error occurred in start()
	self.isAddingActions = false
	self:forceStop()
end

function ISQueueActionsAction:start()
	self.isAddingActions = true
	self.indexToAdd = 1 + 1
	self.addActionsFunction(self.character, unpack(self.args))
	self.isAddingActions = false
	if self.indexToAdd == 1 + 1 then
		-- No actions were added, so stop.
		self:forceStop()
	else
		self:forceComplete()
	end
end

function ISQueueActionsAction:stop()
	ISBaseTimedAction.stop(self)
end

function ISQueueActionsAction:perform()
	ISBaseTimedAction.perform(self)
end

function ISQueueActionsAction:new(character, addActionsFunction, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10)
	local o = ISBaseTimedAction.new(self, character)
	o.character = character
	o.stopOnAim = false
	o.stopOnWalk = false
	o.stopOnRun = false
	o.maxTime = -1
	o.addActionsFunction = addActionsFunction
	o.args = { arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10 }
	return o
end


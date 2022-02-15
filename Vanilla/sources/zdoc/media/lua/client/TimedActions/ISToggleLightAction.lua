--***********************************************************
--**                    THE INDIE STONE                    **
--***********************************************************

require "TimedActions/ISBaseTimedAction"

---@class ISToggleLightAction : ISBaseTimedAction
ISToggleLightAction = ISBaseTimedAction:derive("ISToggleLightAction");

function ISToggleLightAction:isValid()
	return true
end

function ISToggleLightAction:update()
end

function ISToggleLightAction:start()
	self.character:faceThisObject(self.object)
end

function ISToggleLightAction:stop()
	ISBaseTimedAction.stop(self)
end

function ISToggleLightAction:perform()
	self.object:toggle()
	-- needed to remove from queue / start next.
	ISBaseTimedAction.perform(self)
end

function ISToggleLightAction:new(character, object)
	local o = {}
	setmetatable(o, self)
	self.__index = self
	o.character = character
	o.stopOnWalk = false
	o.stopOnRun = false
	o.maxTime = 0
	-- custom fields
	o.object = object
	return o
end

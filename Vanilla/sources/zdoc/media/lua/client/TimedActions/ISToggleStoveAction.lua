--***********************************************************
--**                   THE INDIE STONE                     **
--***********************************************************

require "TimedActions/ISBaseTimedAction"

---@class ISToggleStoveAction : ISBaseTimedAction
ISToggleStoveAction = ISBaseTimedAction:derive("ISToggleStoveAction");

function ISToggleStoveAction:isValid()
	return self.object:getObjectIndex() ~= -1
end

function ISToggleStoveAction:update()
	self.character:faceThisObject(self.object)
end

function ISToggleStoveAction:start()
end

function ISToggleStoveAction:stop()
    ISBaseTimedAction.stop(self)
end

function ISToggleStoveAction:perform()
	self.object:Toggle()
	-- needed to remove from queue / start next.
	ISBaseTimedAction.perform(self)
end

function ISToggleStoveAction:new(character, object)
	local o = {}
	setmetatable(o, self)
	self.__index = self
	o.character = character
	o.object = object
	o.stopOnWalk = true
	o.stopOnRun = true
	o.maxTime = 0
	return o
end

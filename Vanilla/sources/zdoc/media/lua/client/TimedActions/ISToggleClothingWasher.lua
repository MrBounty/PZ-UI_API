--***********************************************************
--**                   THE INDIE STONE                     **
--***********************************************************

require "TimedActions/ISBaseTimedAction"

---@class ISToggleClothingWasher : ISBaseTimedAction
ISToggleClothingWasher = ISBaseTimedAction:derive("ISToggleClothingWasher");

function ISToggleClothingWasher:isValid()
	return self.object:getObjectIndex() ~= -1
end

function ISToggleClothingWasher:update()
	self.character:faceThisObject(self.object)
end

function ISToggleClothingWasher:start()
end

function ISToggleClothingWasher:stop()
	ISBaseTimedAction.stop(self)
end

function ISToggleClothingWasher:perform()
	local obj = self.object
	local args = { x = obj:getX(), y = obj:getY(), z = obj:getZ() }
	sendClientCommand(self.character, 'clothingWasher', 'toggle', args)

	-- needed to remove from queue / start next.
	ISBaseTimedAction.perform(self)
end

function ISToggleClothingWasher:new(character, object)
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


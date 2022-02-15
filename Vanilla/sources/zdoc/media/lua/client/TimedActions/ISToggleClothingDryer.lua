--***********************************************************
--**                   THE INDIE STONE                     **
--***********************************************************

require "TimedActions/ISBaseTimedAction"

---@class ISToggleClothingDryer : ISBaseTimedAction
ISToggleClothingDryer = ISBaseTimedAction:derive("ISToggleClothingDryer");

function ISToggleClothingDryer:isValid()
	return self.object:getObjectIndex() ~= -1
end

function ISToggleClothingDryer:update()
	self.character:faceThisObject(self.object)
end

function ISToggleClothingDryer:start()
end

function ISToggleClothingDryer:stop()
	ISBaseTimedAction.stop(self)
end

function ISToggleClothingDryer:perform()
	local obj = self.object
	local args = { x = obj:getX(), y = obj:getY(), z = obj:getZ() }
	sendClientCommand(self.character, 'clothingDryer', 'toggle', args)

	-- needed to remove from queue / start next.
	ISBaseTimedAction.perform(self)
end

function ISToggleClothingDryer:new(character, object)
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


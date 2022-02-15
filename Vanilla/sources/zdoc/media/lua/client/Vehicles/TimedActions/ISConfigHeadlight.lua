--***********************************************************
--**                    THE INDIE STONE                    **
--***********************************************************

require "TimedActions/ISBaseTimedAction"

---@class ISConfigHeadlight : ISBaseTimedAction
ISConfigHeadlight = ISBaseTimedAction:derive("ISConfigHeadlight")

function ISConfigHeadlight:isValid()
--	return self.vehicle:isInArea(self.part:getArea(), self.character)
	return true;
end

function ISConfigHeadlight:update()
	self.character:faceThisObject(self.vehicle)
end

function ISConfigHeadlight:start()

end

function ISConfigHeadlight:stop()
	ISBaseTimedAction.stop(self)
end

function ISConfigHeadlight:perform()
	ISBaseTimedAction.perform(self)
	
	if isClient() then
		local args = { vehicle = self.vehicle:getId(), part = self.part:getId(), dir = self.dir }
		sendClientCommand(self.character, 'vehicle', 'configHeadlight', args)
	end
	if self.dir == 1 then
		self.part:getLight():setFocusingUp()
	else
		self.part:getLight():setFocusingDown()
	end
end

function ISConfigHeadlight:new(character, part, dir, time)
	local o = {}
	setmetatable(o, self)
	self.__index = self
	o.character = character
	o.vehicle = part:getVehicle()
	o.part = part
	o.dir = dir
	o.maxTime = time
	return o
end


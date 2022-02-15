--***********************************************************
--**                    THE INDIE STONE                    **
--***********************************************************

require "TimedActions/ISBaseTimedAction"

---@class ISHorn : ISBaseTimedAction
ISHorn = ISBaseTimedAction:derive("ISHorn")

function ISHorn:isValid()
	return true
end

function ISHorn:update()
	if (getTimestampMs() - self.t >1500) then
		ISBaseTimedAction.forceComplete(self)
	end
end

function ISHorn:start()
	self.t = getTimestampMs()
	ISVehicleMenu.onHornStart(self.character)
end

function ISHorn:stop()
	--print "stop"
	ISVehicleMenu.onHornStop(self.character)
    ISBaseTimedAction.stop(self)
end

function ISHorn:perform()
	--print "perform"
	ISVehicleMenu.onHornStop(self.character)
	ISBaseTimedAction.perform(self)
end

function ISHorn:new(character)
	local o = {}
	setmetatable(o, self)
	self.__index = self
	o.character = character
	o.maxTime = -1
	o.t = getTimestampMs()
	return o
end


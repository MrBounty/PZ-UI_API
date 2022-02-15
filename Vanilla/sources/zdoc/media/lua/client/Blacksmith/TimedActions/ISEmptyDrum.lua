--***********************************************************
--**                    ROBERT JOHNSON                     **
--***********************************************************

require "TimedActions/ISBaseTimedAction"

---@class ISEmptyDrum : ISBaseTimedAction
ISEmptyDrum = ISBaseTimedAction:derive("ISEmptyDrum");

function ISEmptyDrum:isValid()
	self.metalDrum:updateFromIsoObject()
	return self.metalDrum:getIsoObject() ~= nil and self.metalDrum.waterAmount > 0
end

function ISEmptyDrum:update()
	self.character:faceThisObject(self.metalDrum:getIsoObject())
    self.character:setMetabolicTarget(Metabolics.LightWork);
end

function ISEmptyDrum:start()
end

function ISEmptyDrum:stop()
	ISBaseTimedAction.stop(self);
end

function ISEmptyDrum:perform()
	local md = self.metalDrum
	local args = { x = md.x, y = md.y, z = md.z }
	CMetalDrumSystem.instance:sendCommand(self.character, 'removeWater', args)

	ISBaseTimedAction.perform(self);
end

function ISEmptyDrum:new(character, metalDrum)
	local o = {}
	setmetatable(o, self)
	self.__index = self
	o.character = character
	o.stopOnWalk = true;
	o.stopOnRun = true;
	o.maxTime = 70;
    o.metalDrum = metalDrum;
	return o;
end

--***********************************************************
--**                    ROBERT JOHNSON                     **
--***********************************************************

require "TimedActions/ISBaseTimedAction"

---@class ISRemoveCharcoal : ISBaseTimedAction
ISRemoveCharcoal = ISBaseTimedAction:derive("ISRemoveCharcoal");

function ISRemoveCharcoal:isValid()
	self.metalDrum:updateFromIsoObject()
	return self.metalDrum:getIsoObject() ~= nil and self.metalDrum.haveCharcoal
end

function ISRemoveCharcoal:update()
    self.character:setMetabolicTarget(Metabolics.HeavyDomestic);
end

function ISRemoveCharcoal:start()
end

function ISRemoveCharcoal:stop()
	ISBaseTimedAction.stop(self);
end

function ISRemoveCharcoal:perform()
	local md = self.metalDrum
	local args = { x = md.x, y = md.y, z = md.z }
	CMetalDrumSystem.instance:sendCommand(self.character, 'removeCharcoal', args)

	ISBaseTimedAction.perform(self);
end

function ISRemoveCharcoal:new(character, metalDrum)
	local o = {}
	setmetatable(o, self)
	self.__index = self
	o.stopOnWalk = true;
	o.stopOnRun = true;
	o.maxTime = 70;
    o.metalDrum = metalDrum;
	o.character  = character;
	return o;
end

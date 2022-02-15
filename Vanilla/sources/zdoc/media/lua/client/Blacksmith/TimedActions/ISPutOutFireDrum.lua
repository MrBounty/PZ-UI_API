--***********************************************************
--**                    ROBERT JOHNSON                     **
--***********************************************************

require "TimedActions/ISBaseTimedAction"

---@class ISPutOutFireDrum : ISBaseTimedAction
ISPutOutFireDrum = ISBaseTimedAction:derive("ISPutOutFireDrum");

function ISPutOutFireDrum:isValid()
	self.metalDrum:updateFromIsoObject()
	return self.metalDrum:getIsoObject() ~= nil and self.metalDrum.isLit
end

function ISPutOutFireDrum:update()
	self.character:faceThisObject(self.metalDrum:getIsoObject())
end

function ISPutOutFireDrum:start()
end

function ISPutOutFireDrum:stop()
	ISBaseTimedAction.stop(self);
end

function ISPutOutFireDrum:perform()
	local md = self.metalDrum
	local args = { x = md.x, y = md.y, z = md.z }
	CMetalDrumSystem.instance:sendCommand(self.character, 'putOutFire', args)

	ISBaseTimedAction.perform(self);
end

function ISPutOutFireDrum:new(character, metalDrum)
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

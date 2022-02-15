--***********************************************************
--**                    ROBERT JOHNSON                     **
--***********************************************************

require "TimedActions/ISBaseTimedAction"

---@class ISAddLogsInDrum : ISBaseTimedAction
ISAddLogsInDrum = ISBaseTimedAction:derive("ISAddLogsInDrum");

function ISAddLogsInDrum:isValid()
	self.metalDrum:updateFromIsoObject()
	return self.metalDrum:getIsoObject() ~= nil and (self.add ~= self.metalDrum.haveLogs)
end

function ISAddLogsInDrum:update()
	self.character:faceThisObject(self.metalDrum:getIsoObject())

    self.character:setMetabolicTarget(Metabolics.HeavyWork);
end

function ISAddLogsInDrum:start()
end

function ISAddLogsInDrum:stop()
	ISBaseTimedAction.stop(self);
end

function ISAddLogsInDrum:perform()
	local md = self.metalDrum
	local args = { x = md.x, y = md.y, z = md.z }
	if self.add then
		CMetalDrumSystem.instance:sendCommand(self.character, 'addLogs', args)
	else
		CMetalDrumSystem.instance:sendCommand(self.character, 'removeLogs', args)
	end

	ISBaseTimedAction.perform(self);
end

function ISAddLogsInDrum:new(character, metalDrum, add)
	local o = {}
	setmetatable(o, self)
	self.__index = self
	o.stopOnWalk = true;
	o.stopOnRun = true;
	o.maxTime = 30;
    o.metalDrum = metalDrum;
	o.character  = character;
    o.add = add;
	return o;
end

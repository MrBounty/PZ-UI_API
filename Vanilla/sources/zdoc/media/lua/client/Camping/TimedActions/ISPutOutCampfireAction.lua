--***********************************************************
--**                    THE INDIE STONE                    **
--***********************************************************

require "TimedActions/ISBaseTimedAction"

---@class ISPutOutCampfireAction : ISBaseTimedAction
ISPutOutCampfireAction = ISBaseTimedAction:derive("ISPutOutCampfireAction");

function ISPutOutCampfireAction:isValid()
	self.campfire:updateFromIsoObject()
	return self.campfire:getObject() ~= nil and self.campfire.isLit
end

function ISPutOutCampfireAction:waitToStart()
	self.character:faceThisObject(self.campfire:getObject())
	return self.character:shouldBeTurning()
end

function ISPutOutCampfireAction:update()
	self.character:faceThisObject(self.campfire:getObject())
end

function ISPutOutCampfireAction:start()
end

function ISPutOutCampfireAction:stop()
    ISBaseTimedAction.stop(self);
end

function ISPutOutCampfireAction:perform()
	local cf = self.campfire
	local args = { x = cf.x, y = cf.y, z = cf.z }
	CCampfireSystem.instance:sendCommand(self.character, 'putOutCampfire', args)

	-- needed to remove from queue / start next.
	ISBaseTimedAction.perform(self);
end

function ISPutOutCampfireAction:new (character, campfire, time)
	local o = {}
	setmetatable(o, self)
	self.__index = self
	o.character = character;
	o.stopOnWalk = true;
	o.stopOnRun = true;
	o.maxTime = time;
	-- custom fields
	o.campfire = campfire
	return o
end

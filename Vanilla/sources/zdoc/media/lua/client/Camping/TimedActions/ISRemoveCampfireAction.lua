--***********************************************************
--**                    ROBERT JOHNSON                     **
--***********************************************************

require "TimedActions/ISBaseTimedAction"

---@class ISRemoveCampfireAction : ISBaseTimedAction
ISRemoveCampfireAction = ISBaseTimedAction:derive("ISRemoveCampfireAction");

function ISRemoveCampfireAction:isValid()
	self.campfire:updateFromIsoObject()
	return self.campfire:getObject() ~= nil
end

function ISRemoveCampfireAction:waitToStart()
	self.character:faceThisObject(self.campfire:getObject())
	return self.character:shouldBeTurning()
end

function ISRemoveCampfireAction:update()
	self.character:faceThisObject(self.campfire:getObject())
    self.character:setMetabolicTarget(Metabolics.HeavyDomestic);
end

function ISRemoveCampfireAction:start()
	self:setActionAnim("Loot")
	self.character:SetVariable("LootPosition", "Low")
	self:setOverrideHandModels(nil, nil)
	self.character:reportEvent("EventLootItem");
end

function ISRemoveCampfireAction:stop()
    ISBaseTimedAction.stop(self);
end

function ISRemoveCampfireAction:perform()
	local cf = self.campfire
	local args = { x = cf.x, y = cf.y, z = cf.z }
	CCampfireSystem.instance:sendCommand(self.character, 'removeCampfire', args)

	-- needed to remove from queue / start next.
	ISBaseTimedAction.perform(self);
end

function ISRemoveCampfireAction:new (character, campfire, time)
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

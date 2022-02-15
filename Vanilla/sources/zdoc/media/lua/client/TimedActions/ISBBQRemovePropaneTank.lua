--***********************************************************
--**                    THE INDIE STONE                    **
--***********************************************************

require "TimedActions/ISBaseTimedAction"

---@class ISBBQRemovePropaneTank : ISBaseTimedAction
ISBBQRemovePropaneTank = ISBaseTimedAction:derive("ISBBQRemovePropaneTank");

function ISBBQRemovePropaneTank:isValid()
	return self.bbq:getObjectIndex() ~= -1 and self.bbq:hasPropaneTank()
end

function ISBBQRemovePropaneTank:waitToStart()
	self.character:faceThisObject(self.bbq)
	return self.character:shouldBeTurning()
end

function ISBBQRemovePropaneTank:update()
	self.character:faceThisObject(self.bbq)

    self.character:setMetabolicTarget(Metabolics.MediumWork);
end

function ISBBQRemovePropaneTank:start()
	self:setActionAnim("Loot")
	self.character:SetVariable("LootPosition", "Low")
	self:setOverrideHandModels(nil, nil)
end

function ISBBQRemovePropaneTank:stop()
    ISBaseTimedAction.stop(self);
end

function ISBBQRemovePropaneTank:perform()
	local bbq = self.bbq
	local args = { x = bbq:getX(), y = bbq:getY(), z = bbq:getZ() }
	sendClientCommand(self.character, 'bbq', 'removePropaneTank', args)

	-- needed to remove from queue / start next.
	ISBaseTimedAction.perform(self)
end

function ISBBQRemovePropaneTank:new (character, bbq, time)
	local o = {}
	setmetatable(o, self)
	self.__index = self
	o.character = character
	o.stopOnWalk = true
	o.stopOnRun = true
	o.maxTime = time
	-- custom fields
	o.bbq = bbq
	return o
end

--***********************************************************
--**                    THE INDIE STONE                    **
--***********************************************************

require "TimedActions/ISBaseTimedAction"

---@class ISBBQExtinguish : ISBaseTimedAction
ISBBQExtinguish = ISBaseTimedAction:derive("ISBBQExtinguish");

function ISBBQExtinguish:isValid()
	return self.bbq:getObjectIndex() ~= -1 and self.bbq:isLit()
end

function ISBBQExtinguish:waitToStart()
	self.character:faceThisObject(self.bbq)
	return self.character:shouldBeTurning()
end

function ISBBQExtinguish:update()
	self.character:faceThisObject(self.bbq)
end

function ISBBQExtinguish:start()
	self:setActionAnim("Loot")
	self.character:SetVariable("LootPosition", "Low")
end

function ISBBQExtinguish:stop()
    ISBaseTimedAction.stop(self);
end

function ISBBQExtinguish:perform()
	local bbq = self.bbq
	local args = { x = bbq:getX(), y = bbq:getY(), z = bbq:getZ() }
	sendClientCommand(self.character, 'bbq', 'extinguish', args)

	-- needed to remove from queue / start next.
	ISBaseTimedAction.perform(self)
end

function ISBBQExtinguish:new (character, bbq, time)
	local o = {}
	setmetatable(o, self)
	self.__index = self
	o.character = character
	o.stopOnWalk = true
	o.stopOnRun = true
	o.maxTime = time
	-- custom fields
	o.bbq = bbq
    if character:isTimedActionInstant() then
        o.maxTime = 1;
    end
	return o
end

--***********************************************************
--**                    THE INDIE STONE                    **
--***********************************************************

require "TimedActions/ISBaseTimedAction"

---@class ISClimbOverFence : ISBaseTimedAction
ISClimbOverFence = ISBaseTimedAction:derive("ISClimbOverFence")

function ISClimbOverFence:isValid()
	return self.item:getObjectIndex() ~= -1
end

function ISClimbOverFence:update()
    self.character:setMetabolicTarget(Metabolics.JumpFence);
end

function ISClimbOverFence:start()
end

function ISClimbOverFence:stop()
	ISBaseTimedAction.stop(self)
end

function ISClimbOverFence:perform()
	local dir = nil
	local square = self.item:getSquare()
	local north = square:Is(IsoFlagType.HoppableN)
	if north then
		if self.character:getY() < square:getY() then
			dir = IsoDirections.S
		else
			dir = IsoDirections.N
		end
	else
		if self.character:getX() < square:getX() then
			dir = IsoDirections.E
		else
			dir = IsoDirections.W
		end
	end
	self.character:climbOverFence(dir)
	-- needed to remove from queue / start next.
	ISBaseTimedAction.perform(self)
end

function ISClimbOverFence:new(character, item)
	local o = {}
	setmetatable(o, self)
	self.__index = self
	o.character = character
	o.item = item
	o.stopOnWalk = true
	o.stopOnRun = true
	o.maxTime = 0
	return o
end	

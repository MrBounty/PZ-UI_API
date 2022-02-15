--***********************************************************
--**                    THE INDIE STONE                    **
--***********************************************************

require "TimedActions/ISBaseTimedAction"

---@class ISClimbSheetRopeAction : ISBaseTimedAction
ISClimbSheetRopeAction = ISBaseTimedAction:derive("ISClimbSheetRopeAction");

function ISClimbSheetRopeAction:isValid()
	if self.down then
		return self.character:canClimbDownSheetRope(self.character:getCurrentSquare())
	end
	return self.character:canClimbSheetRope(self.character:getCurrentSquare())
end

function ISClimbSheetRopeAction:update()
    self.character:setMetabolicTarget(Metabolics.ClimbRope);
end

function ISClimbSheetRopeAction:start()
end

function ISClimbSheetRopeAction:stop()
	ISBaseTimedAction.stop(self);
end

function ISClimbSheetRopeAction:perform()
	if self.down then
		self.character:climbDownSheetRope()
	else
		self.character:climbSheetRope()
	end
	-- needed to remove from queue / start next.
	ISBaseTimedAction.perform(self);
end

function ISClimbSheetRopeAction:new(character, down)
	local o = {}
	setmetatable(o, self)
	self.__index = self
	o.character = character
	o.stopOnWalk = true
	o.stopOnRun = true
	o.maxTime = 0
	-- custom fields
	o.down = down
	return o
end

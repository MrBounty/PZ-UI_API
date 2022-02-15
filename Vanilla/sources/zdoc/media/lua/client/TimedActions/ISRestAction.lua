--***********************************************************
--**                    ROBERT JOHNSON                     **
--***********************************************************

require "TimedActions/ISBaseTimedAction"

---@class ISRestAction : ISBaseTimedAction
ISRestAction = ISBaseTimedAction:derive("ISRestAction");

function ISRestAction:isValid()
	return self.character:getStats():getEndurance() < 1;
end

function ISRestAction:update()
    if self.character then
        local endurance = self.character:getStats():getEndurance() + ((ZomboidGlobals.ImobileEnduranceIncrease * self.character:getRecoveryMod() * self.mul) * getGameTime():getMultiplier())
        if endurance > 1 then endurance = 1 end
        if endurance < 0 then endurance = 0 end
        self.character:getStats():setEndurance(endurance)

        self.character:setMetabolicTarget(Metabolics.SeatedResting);
    end
end

function ISRestAction:start()
	self.character:setVariable("ExerciseStarted", false);
	self.character:setVariable("ExerciseEnded", true);
end

function ISRestAction:stop()
    ISBaseTimedAction.stop(self);
end

function ISRestAction:perform()
	ISBaseTimedAction.perform(self);
end

function ISRestAction:new(character)
	local o = {}
	setmetatable(o, self)
	self.__index = self
	o.character = character;
	o.stopOnWalk = true;
	o.stopOnRun = true;
	o.forceProgressBar = true;
--    o.mul = 1;
--    o.maxTime = (1 - character:getStats():getEndurance()) * 32000;
--    if isClient() then
        o.mul = 2;
        o.maxTime = (1 - character:getStats():getEndurance()) * 16000;
--    end
    o.caloriesModifier = 0.5;
	return o;
end

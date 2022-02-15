--***********************************************************
--**                    ROBERT JOHNSON                     **
--***********************************************************

require "TimedActions/ISBaseTimedAction"

---@class ISCheckFishingNetAction : ISBaseTimedAction
ISCheckFishingNetAction = ISBaseTimedAction:derive("ISCheckFishingNetAction");

function ISCheckFishingNetAction:isValid()
	return true;
end

function ISCheckFishingNetAction:update()
    self.character:setMetabolicTarget(Metabolics.MediumWork);
end

function ISCheckFishingNetAction:start()
--    getSoundManager():PlayWorldSound("getFish", false, self.character:getSquare(), 1, 20, 1, false)
    self.character:getSquare():playSound("CheckFishingNet");
    addSound(self.character, self.character:getX(), self.character:getY(), self.character:getZ(), 20, 1)
end

function ISCheckFishingNetAction:stop()
    ISBaseTimedAction.stop(self);
end

function ISCheckFishingNetAction:perform()
    fishingNet.checkTrap(self.character, self.trap, self.hours);
	self.character:getXp():AddXP(Perks.Fishing, 1);
    -- needed to remove from queue / start next.
	ISBaseTimedAction.perform(self);
end

function ISCheckFishingNetAction:new(player, trap, hours)
	local o = {}
	setmetatable(o, self)
	self.__index = self
	o.character = player;
	o.trap = trap;
    o.hours = hours;
	o.stopOnWalk = true;
	o.stopOnRun = true;
	o.maxTime = hours * 13;
    if o.maxTime > 150 then
        o.maxTime = 150;
    end
	return o;
end

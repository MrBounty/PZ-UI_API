--***********************************************************
--**                    ROBERT JOHNSON                     **
--***********************************************************

require "TimedActions/ISBaseTimedAction"

---@class ISTakePillAction : ISBaseTimedAction
ISTakePillAction = ISBaseTimedAction:derive("ISTakePillAction");

function ISTakePillAction:isValid()
	return self.character:getInventory():contains(self.item);
end

function ISTakePillAction:update()
	self.item:setJobDelta(self:getJobDelta());
end

function ISTakePillAction:start()
	self.item:setJobType(getText("ContextMenu_Take_pills"));
	self.item:setJobDelta(0.0);
	self:setActionAnim(CharacterActionAnims.TakePills);
	self:setOverrideHandModels(nil, self.item);
end

function ISTakePillAction:stop()
    ISBaseTimedAction.stop(self);
    self.item:setJobDelta(0.0);

end

function ISTakePillAction:perform()
    self.item:getContainer():setDrawDirty(true);
    self.item:setJobDelta(0.0);
	self.character:getBodyDamage():JustTookPill(self.item);
    -- needed to remove from queue / start next.
	ISBaseTimedAction.perform(self);
end

function ISTakePillAction:new (character, item, time)
	local o = {}
	setmetatable(o, self)
	self.__index = self
	o.character = character;
	o.item = item;
	o.stopOnWalk = false;
	o.stopOnRun = false;
	o.maxTime = time;
	return o
end

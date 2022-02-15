--***********************************************************
--**                    Erasmus Crowley                    **
--***********************************************************

require "TimedActions/ISBaseTimedAction"

---@class ISDumpWaterAction : ISBaseTimedAction
ISDumpWaterAction = ISBaseTimedAction:derive("ISDumpWaterAction");

function ISDumpWaterAction:isValid()
	return self.character:getInventory():contains(self.item);
end

function ISDumpWaterAction:start()
    if self.item ~= nil then
	    self.item:setJobType(getText("IGUI_JobType_PourOut"));
	    self.item:setJobDelta(0.0);
		self.startUsedDelta = self.item:getUsedDelta()
	
		self:setActionAnim(CharacterActionAnims.Pour);
		self:setAnimVariable("FoodType", self.item:getEatType());
		self:setOverrideHandModels(self.item, nil);
	
		self.character:reportEvent("EventTakeWater");

		self.sound = self.character:playSound("PourLiquidOnGround")
    end
end

function ISDumpWaterAction:update()
	if self.item ~= nil then
        self.item:setJobDelta(self:getJobDelta());
        self.item:setUsedDelta(self.startUsedDelta * (1 - self:getJobDelta()));
    end
end

function ISDumpWaterAction:stop()
	self:stopSound()
    if self.item ~= nil then
        self.item:setJobDelta(0.0);
     end
    ISBaseTimedAction.stop(self);
end

function ISDumpWaterAction:perform()
	self:stopSound()
    if self.item ~= nil then
        self.item:getContainer():setDrawDirty(true);
        self.item:setJobDelta(0.0);
        self.item:setUsedDelta(0.0);
        self.item:Use();
    end
    -- needed to remove from queue / start next.
	ISBaseTimedAction.perform(self);
end

function ISDumpWaterAction:stopSound()
	if self.sound and self.character:getEmitter():isPlaying(self.sound) then
		self.character:stopOrTriggerSound(self.sound);
	end
end

function ISDumpWaterAction:new (character, item)
	local o = {}
	setmetatable(o, self)
	self.__index = self
	o.character = character;
	o.item = item;
	o.stopOnWalk = false;
	o.stopOnRun = false;
	o.maxTime = (item:getUsedDelta() / item:getUseDelta()) * 10;
	if o.maxTime > 150 then
		o.maxTime = 150;
	end
	if o.maxTime < 30 then
		o.maxTime = 30;
	end
	if o.character:isTimedActionInstant() then o.maxTime = 1; end
	return o
end

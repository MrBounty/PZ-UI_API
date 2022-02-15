--***********************************************************
--**                    ROBERT JOHNSON                     **
--***********************************************************

require "TimedActions/ISBaseTimedAction"

---@class ISTrimBeard : ISBaseTimedAction
ISTrimBeard = ISBaseTimedAction:derive("ISTrimBeard");

function ISTrimBeard:isValid()
	return self.character:getInventory():contains(self.item);
end

function ISTrimBeard:update()
    self.character:setMetabolicTarget(Metabolics.LightDomestic);
end

function ISTrimBeard:start()
	self:setActionAnim(CharacterActionAnims.Shave)
	self:setOverrideHandModels("DisposableRazor", nil)
end

function ISTrimBeard:stop()
    ISBaseTimedAction.stop(self);
end

function ISTrimBeard:perform()
	self.character:getHumanVisual():setBeardModel(self.beardStyle);
	self.character:resetModel();
	self.character:resetBeardGrowingTime();
	triggerEvent("OnClothingUpdated", self.character)

    -- needed to remove from queue / start next.
	ISBaseTimedAction.perform(self);
end

function ISTrimBeard:new(character, beardStyle, item, time)
	local o = {}
	setmetatable(o, self)
	self.__index = self
	o.character = character;
	o.beardStyle = beardStyle or "";
	o.stopOnWalk = true;
	o.stopOnRun = true;
	o.item = item;
	o.maxTime = time;
	if o.character:isTimedActionInstant() then
		o.maxTime = 1;
	end
	return o;
end

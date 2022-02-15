--***********************************************************
--**                    ROBERT JOHNSON                     **
--***********************************************************

require "TimedActions/ISBaseTimedAction"

---@class ISRemoveSheetAction : ISBaseTimedAction
ISRemoveSheetAction = ISBaseTimedAction:derive("ISRemoveSheetAction");

function ISRemoveSheetAction:isValid()
	if self.item:getObjectIndex() == -1 then return false end
	return instanceof(self.item, "IsoCurtain") or (self.item:HasCurtains() ~= nil)
end

function ISRemoveSheetAction:waitToStart()
	self.character:faceThisObjectAlt(self.item)
	return self.character:shouldBeTurning()
end

function ISRemoveSheetAction:update()
	self.character:faceThisObjectAlt(self.item)

    self.character:setMetabolicTarget(Metabolics.HeavyDomestic);
end

function ISRemoveSheetAction:start()
	self:setActionAnim("RemoveCurtain")
	self:setOverrideHandModels(nil, nil)
	self.character:playSound("CurtainSheetRemove")
end

function ISRemoveSheetAction:stop()
    ISBaseTimedAction.stop(self);
end

function ISRemoveSheetAction:perform()
	local obj = self.item
	local index = obj:getObjectIndex()
	local args = { x=obj:getX(), y=obj:getY(), z=obj:getZ(), index=index }
	sendClientCommand(self.character, 'object', 'removeSheet', args)

    -- needed to remove from queue / start next.
	ISBaseTimedAction.perform(self);
end

function ISRemoveSheetAction:new(character, item, time)
	local o = {}
	setmetatable(o, self)
	self.__index = self
	o.character = character;
	o.item = item;
	o.stopOnWalk = true;
	o.stopOnRun = true;
	o.maxTime = time;
	if o.character:isTimedActionInstant() then o.maxTime = 1; end
	return o;
end

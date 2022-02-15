--***********************************************************
--**                    ROBERT JOHNSON                     **
--***********************************************************

require "TimedActions/ISBaseTimedAction"

---@class ISAddSheetAction : ISBaseTimedAction
ISAddSheetAction = ISBaseTimedAction:derive("ISAddSheetAction");

function ISAddSheetAction:isValid()
	if IsoWindowFrame.isWindowFrame(self.item) then
		if IsoWindowFrame.getCurtain(self.item) then
			return false
		end
	else
		if self.item:HasCurtains() then return false end
	end
	return self.character:getInventory():contains("Sheet");
end

function ISAddSheetAction:waitToStart()
	self.character:faceThisObjectAlt(self.item)
	return self.character:shouldBeTurning()
end

function ISAddSheetAction:update()
	self.character:faceThisObjectAlt(self.item)
    self.character:setMetabolicTarget(Metabolics.LightDomestic);
end

function ISAddSheetAction:start()
	self:setActionAnim("Loot")
	self.character:SetVariable("LootPosition", "High")
	self:setOverrideHandModels(nil, nil)
	self.character:playSound("CurtainSheetAdd")
end

function ISAddSheetAction:stop()
    ISBaseTimedAction.stop(self);
end

function ISAddSheetAction:perform()
	local obj = self.item
	local index = obj:getObjectIndex()
	local args = { x=obj:getX(), y=obj:getY(), z=obj:getZ(), index=index }
	sendClientCommand(self.character, 'object', 'addSheet', args)

    -- needed to remove from queue / start next.
	ISBaseTimedAction.perform(self);
end

function ISAddSheetAction:new(character, item, time)
	local o = {}
	setmetatable(o, self)
	self.__index = self
	o.character = character;
	o.item = item;
	o.stopOnWalk = true;
	o.stopOnRun = true;
	o.maxTime = time;
	return o;
end

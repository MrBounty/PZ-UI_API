--***********************************************************
--**                    THE INDIE STONE                    **
--***********************************************************

require "TimedActions/ISBaseTimedAction"

---@class ISDetachItemHotbar : ISBaseTimedAction
ISDetachItemHotbar = ISBaseTimedAction:derive("ISDetachItemHotbar");

function ISDetachItemHotbar:isValid()
	return self.character:getInventory():contains(self.item);
end

function ISDetachItemHotbar:update()
	
end

function ISDetachItemHotbar:start()
	self.character:setVariable("AttachItemSpeed", self.animSpeed)
	self:setActionAnim("DetachItem")
	self.character:reportEvent("EventAttachItem");
end

function ISDetachItemHotbar:stop()
    ISBaseTimedAction.stop(self);
end

function ISDetachItemHotbar:perform()
	self.hotbar.chr:removeAttachedItem(self.item);
	self.item:setAttachedSlot(-1);
	self.item:setAttachedSlotType(nil);
	self.item:setAttachedToModel(nil);
	
	self.hotbar:reloadIcons();

	ISInventoryPage.renderDirty = true

    -- needed to remove from queue / start next.
	ISBaseTimedAction.perform(self);
end

function ISDetachItemHotbar:animEvent(event, parameter)
	if event == 'detachConnect' then
		local hotbar = getPlayerHotbar(self.character:getPlayerNum());
		hotbar.chr:removeAttachedItem(self.item);
		self:setOverrideHandModels(self.item, nil)
		if self.maxTime == -1 then
			self:forceComplete()
		end
	end
end

function ISDetachItemHotbar:new(character, item)
	local o = {}
	setmetatable(o, self)
	self.__index = self
	o.character = character;
	o.item = item;
	o.stopOnWalk = false;
	o.stopOnRun = true;
	o.equipped = character:isEquipped(item);
	o.hotbar = getPlayerHotbar(o.character:getPlayerNum());
	o.fromHotbar = true;
	o.useProgressBar = false;
	o.ignoreHandsWounds = true;
	o.maxTime = 25;
	if o.equipped then
		o.maxTime = 1;
	end
	if o.character:isTimedActionInstant() then
		o.maxTime = 1
	end
	if o.maxTime > 1 then
		o.animSpeed = o.maxTime / o:adjustMaxTime(o.maxTime)
		o.maxTime = -1
	else
		o.animSpeed = 1.0
	end
	return o;
end

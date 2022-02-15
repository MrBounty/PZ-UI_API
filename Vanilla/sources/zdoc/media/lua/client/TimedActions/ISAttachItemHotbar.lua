--***********************************************************
--**                    THE INDIE STONE                    **
--***********************************************************

require "TimedActions/ISBaseTimedAction"

---@class ISAttachItemHotbar : ISBaseTimedAction
ISAttachItemHotbar = ISBaseTimedAction:derive("ISAttachItemHotbar");

function ISAttachItemHotbar:isValid()
	return self.character:getInventory():contains(self.item);
end

function ISAttachItemHotbar:update()
	
end

function ISAttachItemHotbar:start()
	self.character:setVariable("AttachItemSpeed", self.animSpeed)
	self:setActionAnim("AttachItem")
	self:setOverrideHandModels(self.item, nil)
	self.character:reportEvent("EventAttachItem");
end

function ISAttachItemHotbar:stop()
	if self.hotbar.attachedItems[self.slotIndex] ~= self.item and
			self.character:getAttachedItem(self.slot) == self.item then
		-- Action was cancelled after the 'attachConnect' event.
		self.character:removeAttachedItem(self.item)
	end
	ISBaseTimedAction.stop(self);
end

function ISAttachItemHotbar:perform()
	-- remove previous item
	if self.hotbar.attachedItems[self.slotIndex] then
		self.hotbar.chr:removeAttachedItem(self.hotbar.attachedItems[self.slotIndex]);
		self.hotbar.attachedItems[self.slotIndex]:setAttachedSlot(-1);
		self.hotbar.attachedItems[self.slotIndex]:setAttachedSlotType(nil);
		self.hotbar.attachedItems[self.slotIndex]:setAttachedToModel(nil);
	end
	-- add new item
	-- if the item need to be attached elsewhere than its original emplacement because of a bag for example
	if self.hotbar.replacements and self.hotbar.replacements[self.item:getAttachmentType()] then
		self.slot = self.hotbar.replacements[self.item:getAttachmentType()];
		if self.slot == "null" then
			self.hotbar:removeItem(self.item);
			return;
		end
	end
	
	self.hotbar.chr:setAttachedItem(self.slot, self.item);
	self.item:setAttachedSlot(self.slotIndex);
	self.item:setAttachedSlotType(self.slotDef.type);
	self.item:setAttachedToModel(self.slot);
	
	self.hotbar:reloadIcons();

	ISInventoryPage.renderDirty = true

    -- needed to remove from queue / start next.
	ISBaseTimedAction.perform(self);
end

function ISAttachItemHotbar:animEvent(event, parameter)
	if event == 'attachConnect' then
		local hotbar = getPlayerHotbar(self.character:getPlayerNum());
		if self.equipped then
			self.character:removeFromHands(self.item)
		end
		hotbar.chr:setAttachedItem(self.slot, self.item);
		self:setOverrideHandModels(nil, nil)
		if self.maxTime == -1 then
			self:forceComplete()
		end
	end
end

function ISAttachItemHotbar:new(character, item, slot, slotIndex, slotDef)
	local o = {}
	setmetatable(o, self)
	self.__index = self
	o.character = character;
	o.item = item;
	o.stopOnWalk = false;
	o.stopOnRun = true;
	o.slotIndex = slotIndex;
	o.slot = slot;
	o.slotDef = slotDef;
	o.fromHotbar = true;
	o.equipped = character:isEquipped(item);
	o.maxTime = 30;
	o.hotbar = getPlayerHotbar(character:getPlayerNum());
	o.useProgressBar = false;
	o.ignoreHandsWounds = true;
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

--***********************************************************
--**                    ROBERT JOHNSON                     **
--***********************************************************

require "TimedActions/ISBaseTimedAction"

---@class ISUnequipAction : ISBaseTimedAction
ISUnequipAction = ISBaseTimedAction:derive("ISUnequipAction");

function ISUnequipAction:isValid()
	return true;
end

function ISUnequipAction:update()
	self.item:setJobDelta(self:getJobDelta());
end

function ISUnequipAction:start()
	self.item:setJobType(getText("ContextMenu_Unequip") .. " " .. self.item:getName());
	self.item:setJobDelta(0.0);
	if self.fromHotbar then
		self.character:setVariable("AttachItemSpeed", self.animSpeed)
		self.hotbar:setAttachAnim(self.item);
		self:setActionAnim("AttachItem")
		self:setOverrideHandModels(self.item, nil)
		self.character:reportEvent("EventAttachItem");
	elseif self.item:IsClothing() then
		self:setActionAnim("WearClothing");
		local location = self.item:getBodyLocation()
		self:setAnimVariable("WearClothingLocation", WearClothingAnimations[location] or "")
		self.character:reportEvent("EventWearClothing");
	elseif self.item:IsInventoryContainer() and self.item:canBeEquipped() ~= "" then
		self:setActionAnim("WearClothing");
		local location = self.item:canBeEquipped()
		self:setAnimVariable("WearClothingLocation", WearClothingAnimations[location] or "")
	else
		self:setActionAnim("UnequipItem");
	end
	if self.item:getUnequipSound() then
		self.sound = self.character:getEmitter():playSound(self.item:getUnequipSound())
	end
end

function ISUnequipAction:stop()
	if self.sound then
		self.character:getEmitter():stopSound(self.sound)
	end
    self.item:setJobDelta(0.0);
    ISBaseTimedAction.stop(self);
end

function ISUnequipAction:animEvent(event, parameter)
	if event == 'attachConnect' then
		local hotbar = getPlayerHotbar(self.character:getPlayerNum());
		hotbar.chr:setAttachedItem(self.item:getAttachedToModel(), self.item);
		self:setOverrideHandModels(nil, nil)
		if self.maxTime == -1 then
			self:forceComplete()
		end
	end
end

function ISUnequipAction:perform()
	if self.sound then
		self.character:getEmitter():stopSound(self.sound)
	end

    self.item:getContainer():setDrawDirty(true);
    self.item:setJobDelta(0.0);
    self.character:removeWornItem(self.item)

	if self.fromHotbar then
		local hotbar = getPlayerHotbar(self.character:getPlayerNum());
		hotbar.chr:setAttachedItem(self.item:getAttachedToModel(), self.item);
		self:setOverrideHandModels(nil, nil)
	end

    if self.item == self.character:getPrimaryHandItem() then
        if (self.item:isTwoHandWeapon() or self.item:isRequiresEquippedBothHands()) and self.item == self.character:getSecondaryHandItem() then
            self.character:setSecondaryHandItem(nil);
        end
		self.character:setPrimaryHandItem(nil);
    end
    if self.item == self.character:getSecondaryHandItem() then
        if (self.item:isTwoHandWeapon() or self.item:isRequiresEquippedBothHands()) and self.item == self.character:getPrimaryHandItem() then
            self.character:setPrimaryHandItem(nil);
        end
		self.character:setSecondaryHandItem(nil);
    end
	triggerEvent("OnClothingUpdated", self.character)
	if isForceDropHeavyItem(self.item) then
		self.character:getInventory():Remove(self.item);
		local dropX,dropY,dropZ = ISInventoryTransferAction.GetDropItemOffset(self.character, self.character:getCurrentSquare(), self.item)
		self.character:getCurrentSquare():AddWorldInventoryItem(self.item, dropX, dropY, dropZ)
	end
	ISInventoryPage.renderDirty = true

    -- needed to remove from queue / start next.
	ISBaseTimedAction.perform(self);
end

function ISUnequipAction:new(character, item, time)
	local o = ISBaseTimedAction.new(self, character);
	o.item = item;
	o.stopOnAim = false;
	o.stopOnWalk = false;
	o.stopOnRun = true;
	o.maxTime = time;
	o.ignoreHandsWounds = true;

	o.hotbar = getPlayerHotbar(character:getPlayerNum());
	if o.hotbar then
		o.fromHotbar = o.hotbar:isItemAttached(item);
	else
		o.fromHotbar = false;
	end
	o.useProgressBar = not o.fromHotbar;
	if o.character:isTimedActionInstant() then
		o.maxTime = 1;
	end
	if o.maxTime > 1 and o.fromHotbar then
		o.animSpeed = o.maxTime / o:adjustMaxTime(o.maxTime)
		o.maxTime = -1
	else
		o.animSpeed = 1.0
	end
	return o;
end

--***********************************************************
--**                    ROBERT JOHNSON                     **
--***********************************************************

require "TimedActions/ISBaseTimedAction"

---@class ISEquipWeaponAction : ISBaseTimedAction
ISEquipWeaponAction = ISBaseTimedAction:derive("ISEquipWeaponAction");

function ISEquipWeaponAction:isValid()
	return self.character:getInventory():contains(self.item);
end

function ISEquipWeaponAction:update()
	self.item:setJobDelta(self:getJobDelta());
end

function ISEquipWeaponAction:start()
	if self:isAlreadyEquipped(self.item) then
		self:forceComplete()
		return
	end
    if self.primary then
	    self.item:setJobType(getText("ContextMenu_Equip_Primary") .. " " .. self.item:getName());
    else
        self.item:setJobType(getText("ContextMenu_Equip_Secondary") .. " " .. self.item:getName());
    end
    if self.twoHands then
        self.item:setJobType(getText("ContextMenu_Equip_Two_Hands") .. " " .. self.item:getName());
    end
	self.item:setJobDelta(0.0);
	
	if self.fromHotbar then
		self.character:setVariable("AttachItemSpeed", self.animSpeed)
		self.hotbar:setAttachAnim(self.item);
		self:setActionAnim("DetachItem")
		self.character:reportEvent("EventAttachItem");
	else
		self:setActionAnim("EquipItem");
		self.character:reportEvent("EventAttachItem");
	end
	if self.item:getEquipSound() then
		self.sound = self.character:getEmitter():playSound(self.item:getEquipSound())
	end
end

function ISEquipWeaponAction:animEvent(event, parameter)
	if event == 'detachConnect' then
		local hotbar = getPlayerHotbar(self.character:getPlayerNum());
		hotbar.chr:removeAttachedItem(self.item);
		self:setOverrideHandModels(self.item, nil)
		if self.maxTime == -1 then
			self:forceComplete()
		end
	end
end

function ISEquipWeaponAction:stop()
	if self.sound then
		self.character:getEmitter():stopSound(self.sound)
	end
    self.item:setJobDelta(0.0);
    ISBaseTimedAction.stop(self);
end

function isForceDropHeavyItem(item)
    return (item ~= nil) and (item:getType() == "Generator" or item:getType() == "CorpseMale" or item:getType() == "CorpseFemale")
end

function forceDropHeavyItems(character)
    if not character or not character:getCurrentSquare() then return end
    local primary = character:getPrimaryHandItem()
    if isForceDropHeavyItem(primary) then
        character:getInventory():Remove(primary)
        local dropX,dropY,dropZ = ISInventoryTransferAction.GetDropItemOffset(character, character:getCurrentSquare(), primary)
        character:getCurrentSquare():AddWorldInventoryItem(primary, dropX, dropY, dropZ)
        character:removeFromHands(primary)
        ISInventoryPage.renderDirty = true -- for corpses
    end
    local secondary = character:getSecondaryHandItem()
    if isForceDropHeavyItem(secondary) then
        character:getInventory():Remove(secondary)
        local dropX,dropY,dropZ = ISInventoryTransferAction.GetDropItemOffset(character, character:getCurrentSquare(), primary)
        character:getCurrentSquare():AddWorldInventoryItem(secondary, dropX, dropY, dropZ)
        character:setSecondaryHandItem(nil)
        ISInventoryPage.renderDirty = true -- for corpses
    end
end

function ISEquipWeaponAction:isAlreadyEquipped()
	local primaryItem = self.character:getPrimaryHandItem()
	local secondaryItem = self.character:getSecondaryHandItem()
	if self.twoHands then
		return (primaryItem == self.item) and (secondaryItem == self.item)
	end
	if self.primary then
		return (primaryItem == self.item) and (secondaryItem ~= self.item)
	end
	return (secondaryItem == self.item) and (primaryItem ~= self.item)
end

function ISEquipWeaponAction:perform()
	if self.sound then
		self.character:getEmitter():stopSound(self.sound)
	end

    self.item:setJobDelta(0.0);

	if self:isAlreadyEquipped(self.item) then
		ISBaseTimedAction.perform(self);
		return
	end
	
	if self.character:isEquippedClothing(self.item) then
		self.character:removeWornItem(self.item)
		triggerEvent("OnClothingUpdated", self.character)
	end

    self.item:getContainer():setDrawDirty(true);
    forceDropHeavyItems(self.character)

	if self.fromHotbar then
		local hotbar = getPlayerHotbar(self.character:getPlayerNum());
		hotbar.chr:removeAttachedItem(self.item);
		self:setOverrideHandModels(self.item, nil)
	end

	if not self.twoHands then
		-- equip primary weapon
		if(self.primary) then
            -- if the previous weapon need to be equipped in both hands, we then remove it
            if self.character:getSecondaryHandItem() and self.character:getSecondaryHandItem():isRequiresEquippedBothHands() then
                self.character:setSecondaryHandItem(nil);
            end
			-- if this weapon is already equiped in the 2nd hand, we remove it
			if(self.character:getSecondaryHandItem() == self.item or self.character:getSecondaryHandItem() == self.character:getPrimaryHandItem()) then
                self.character:setSecondaryHandItem(nil);
            end
            if not self.character:getPrimaryHandItem() or self.character:getPrimaryHandItem() ~= self.item then
			    self.character:setPrimaryHandItem(nil);
			    self.character:setPrimaryHandItem(self.item);
            end
		else -- second hand weapon
            -- if the previous weapon need to be equipped in both hands, we then remove it
            if self.character:getPrimaryHandItem() and self.character:getPrimaryHandItem():isRequiresEquippedBothHands() then
                self.character:setPrimaryHandItem(nil);
            end
			-- if this weapon is already equiped in the 1st hand, we remove it
			if(self.character:getPrimaryHandItem() == self.item or self.character:getSecondaryHandItem() == self.character:getPrimaryHandItem()) then
                self.character:setPrimaryHandItem(nil);
            end
            if not self.character:getSecondaryHandItem() or self.character:getSecondaryHandItem() ~= self.item then
                self.character:setSecondaryHandItem(nil);
			    self.character:setSecondaryHandItem(self.item);
            end
		end
    else
        self.character:setPrimaryHandItem(nil);
        self.character:setSecondaryHandItem(nil);

		self.character:setPrimaryHandItem(self.item);
		self.character:setSecondaryHandItem(self.item);
	end

	--if self.item:canBeActivated() and ((instanceof("Drainable", self.item) and self.item:getUsedDelta() > 0) or not instanceof("Drainable", self.item)) then
	if self.item:canBeActivated() then
		self.item:setActivated(true);
	end
	getPlayerInventory(self.character:getPlayerNum()):refreshBackpacks();

    -- needed to remove from queue / start next.
	ISBaseTimedAction.perform(self);
end

function ISEquipWeaponAction:new (character, item, time, primary, twoHands)
	local o = ISBaseTimedAction.new(self, character);
	o.item = item;
	o.stopOnAim = false;
	o.stopOnWalk = false;
	o.stopOnRun = true;
	o.maxTime = time;
	o.primary = primary;
	o.twoHands = twoHands;
	o.ignoreHandsWounds = true;

	o.hotbar = getPlayerHotbar(character:getPlayerNum());
	o.fromHotbar = o.hotbar and o.hotbar:isItemAttached(item);
	o.useProgressBar = not o.fromHotbar;

    if instanceof(item, "HandWeapon") and not item:isTwoHandWeapon() then
        o.twoHands = false;
    end
    if item:isRequiresEquippedBothHands() then
        o.twoHands = true;
    end
    if character:getSecondaryHandItem() and character:getSecondaryHandItem() == item then
        o.maxTime = 0;
    end
    if character:getPrimaryHandItem() and character:getPrimaryHandItem() == item then
        o.maxTime = 0;
    end
    if character:isTimedActionInstant() then
        o.maxTime = 1;
	end
	if o.twoHands then
		o.jobType = getText("ContextMenu_Equip_Two_Hands") .. " " .. item:getName()
	elseif o.primary then
		o.jobType = getText("ContextMenu_Equip_Primary") .. " " .. item:getName()
	else
		o.jobType = getText("ContextMenu_Equip_Secondary") .. " " .. item:getName()
	end
	if o.maxTime > 1 and o.fromHotbar then
		o.animSpeed = o.maxTime / o:adjustMaxTime(o.maxTime)
		o.maxTime = -1
	else
		o.animSpeed = 1.0
	end
	return o
end

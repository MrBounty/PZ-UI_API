--
-- Created by IntelliJ IDEA.
-- User: RJ
-- To change this template use File | Settings | File Templates.
--

require "ISUI/ISPanelJoypad"

---@class ISHotbar : ISPanelJoypad
ISHotbar = ISPanelJoypad:derive("ISHotbar");
local FONT_HGT_SMALL = getTextManager():getFontHeight(UIFont.Small)

--************************************************************************--
--** ISHotbar:initialise
--**
--************************************************************************--

function ISHotbar:render()
	if (self.playerNum > 0) or JoypadState.players[self.playerNum+1] then
		self:setVisible(false);
		-- Don't remove this from the screen, we need update() to call refresh().
--		self:removeFromUIManager();
	end
	self:drawRectBorderStatic(0, 0, self.width, self.height, self.borderColor.a, self.borderColor.r, self.borderColor.g, self.borderColor.b);

	local mouseOverSlotIndex = self:getSlotIndexAt(self:getMouseX(), self:getMouseY())

	local draggedItem = nil
	if ISMouseDrag.dragging and (mouseOverSlotIndex ~= -1) then
		local dragging = ISInventoryPane.getActualItems(ISMouseDrag.dragging)
		local slot = self.availableSlot[mouseOverSlotIndex]
		for _,item in ipairs(dragging) do
			if self:canBeAttached(slot, item) then
				draggedItem = item
				break
			end
		end
	end

	local slotX = self.margins;

	for i, slot in pairs(self.availableSlot) do
		self:drawRectBorderStatic(slotX, self.margins, self.slotWidth, self.slotHeight, self.borderColor.a, self.borderColor.r, self.borderColor.g, self.borderColor.b);
		self:drawText(tostring(i), slotX + 3, self.margins + 1, self.textColor.r, self.textColor.g, self.textColor.b, self.textColor.a, self.font);
		local item = self.attachedItems[i]
		if i == mouseOverSlotIndex then
			local r,g,b = 1,1,1
			if draggedItem then
				item = draggedItem
			elseif ISMouseDrag.dragging then
				r,g,b = 1,0,0
			end
			self:drawRect(slotX, self.margins, self.slotWidth, self.slotHeight, 0.2, r, g, b, 1);
			local slotName = getTextOrNull("IGUI_HotbarAttachment_" .. slot.slotType) or slot.name;
			local textWid = getTextManager():MeasureStringX(UIFont.Small, slotName)
			self:drawText(slotName, slotX + (self.slotWidth - textWid) / 2, 0 - FONT_HGT_SMALL, self.textColor.r, self.textColor.g, self.textColor.b, self.textColor.a, self.font);
		elseif item == draggedItem then
			item = nil
		end
		if item then
			local tex = item:getTexture()
			self:drawTexture(tex, slotX + (tex:getWidth() / 2), (self.height - tex:getHeight()) / 2, 1, 1, 1, 1)
			
			--local n = math.floor(((item:getCondition() / item:getConditionMax()) * 5));
			
			--if(item:getCondition() > 0 and n == 0) then
			--	n = 1;
			--end
			
			--self:drawTexture(self.qualityStars[n], slotX + self.slotWidth - 15, self.margins + 3,1,1,1,1);

			if item:isEquipped() then
				tex = self.equippedItemIcon
				self:drawTexture(tex, slotX + self.slotWidth - tex:getWidth() - 5, self.height - self.margins - tex:getHeight() - 5,1,1,1,1);
			end
		elseif slot.texture then
			self:drawTexture(slot.texture, slotX + slot.texture:getWidth() / 2, (self.height - slot.texture:getHeight()) / 2, 0.25, 1.0, 1.0, 1.0)
		end
		slotX = slotX + self.slotWidth + self.slotPad;
	end
end

function ISHotbar:getSlotDef(slot)
	for i,v in ipairs(ISHotbarAttachDefinition) do
		if slot == v.type then
			return v;
		end
	end
	return nil
end

function ISHotbar:getSlotDefReplacement(slot)
	for i,v in ipairs(ISHotbarAttachDefinition.replacements) do
		if slot == v.type then
			return v;
		end
	end
	return slot;
end

function ISHotbar:doMenu(slotIndex)
	if UIManager.getSpeedControls():getCurrentGameSpeed() == 0 then
		return;
	end

	local slot = self.availableSlot[slotIndex];
	local slotDef = slot.def;
	local context = ISContextMenu.get(self.playerNum, getMouseX(), getMouseY());
	local found = false;
	
	-- first check for remove
	if self.attachedItems[slotIndex] then
		context = ISInventoryPaneContextMenu.createMenu(self.chr:getPlayerNum(), true, {self.attachedItems[slotIndex]}, getMouseX(), getMouseY());
--		context:addOptionOnTop("Remove " .. self.attachedItems[slotIndex]:getDisplayName(), self, ISHotbar.removeItem, self.attachedItems[slotIndex], true);
		found = true;
	end
	
	local subMenuAttach;
	-- fetch all items in our inventory to check what can be added there
	for i=0, self.chr:getInventory():getItems():size()-1 do
		local item = self.chr:getInventory():getItems():get(i);
		if item:getAttachmentType() and item:getAttachedSlot() == -1 and not item:isBroken() then
			for i,v in pairs(slotDef.attachments) do
				if item:getAttachmentType() == i then
					local doIt = true;
					if self.replacements and self.replacements[item:getAttachmentType()] then
						slot = self.replacements[item:getAttachmentType()];
						if slot == "null" then
							doIt = false;
						end
					end
					if doIt then
						if not subMenuAttach then
							local subOption = context:addOptionOnTop(getText("ContextMenu_Attach"), nil);
							subMenuAttach = context:getNew(context);
							context:addSubMenu(subOption, subMenuAttach);
						end
						subMenuAttach:addOption(item:getDisplayName(), self, ISHotbar.attachItem, item, v, slotIndex, slotDef, true);
						found = true;
					end
				end
			end
		end
	end
	
	if not found then
		local option = context:addOption(getText("ContextMenu_NoWeaponsAvailable"));
		option.notAvailable = true;
	end
end

function ISHotbar.doMenuFromInventory(playerNum, item, context)
	local self = getPlayerHotbar(playerNum);
	if self == nil then return end
	if self:isInHotbar(item) and item:getAttachmentType() and item:getAttachedSlot() ~= -1 then
		local slot = self.availableSlot[item:getAttachedSlot()]
		local slotName = getTextOrNull("IGUI_HotbarAttachment_" .. slot.slotType) or slot.name;
		context:addOptionOnTop(getText("ContextMenu_RemoveFromHotbar", self.attachedItems[item:getAttachedSlot()]:getDisplayName(), slotName), self, ISHotbar.removeItem, self.attachedItems[item:getAttachedSlot()], true);
	end
	if item:getAttachmentType() and not self:isInHotbar(item) and not item:isBroken() and self.replacements[item:getAttachmentType()] ~= "null" then
		local subOption = context:addOptionOnTop(getText("ContextMenu_Attach"), nil);
		local subMenuAttach = context:getNew(context);
		context:addSubMenu(subOption, subMenuAttach);
		
		local found = false;
		for slotIndex, slot in pairs(self.availableSlot) do
			local slotDef = slot.def;
			for i, v in pairs(slotDef.attachments) do
				if item:getAttachmentType() == i then
					local doIt = true;
					local name = getTextOrNull("IGUI_HotbarAttachment_" .. slot.slotType) or slot.name;
					if self.replacements and self.replacements[item:getAttachmentType()] then
						slot = self.replacements[item:getAttachmentType()];
						if slot == "null" then
							doIt = false;
						end
					end
					if doIt then
						local option = subMenuAttach:addOption(name, self, ISHotbar.attachItem, item, v, slotIndex, slotDef, true);
						if self.attachedItems[slotIndex] then
							local tooltip = ISWorldObjectContextMenu.addToolTip();
							tooltip.description = tooltip.description .. getText("Tooltip_ReplaceWornItems") .. " <LINE> <INDENT:20> "
							tooltip.description = tooltip.description .. self.attachedItems[slotIndex]:getDisplayName()
							option.toolTip = tooltip
						end 
						found = true;
					end
				end
			end
		end
		-- didn't found anything to it, gonna add the possibilities as a tooltip
		if not found then
			subOption.notAvailable = true;
			local tooltip = ISWorldObjectContextMenu.addToolTip();
			local text = getText("Tooltip_CanBeAttached") .. " <LINE> <INDENT:20> ";
			for i,v in pairs(ISHotbarAttachDefinition) do
				if v.attachments then
					for type,atch in pairs(v.attachments) do
						if type == item:getAttachmentType() then
							text = text .. getText("IGUI_HotbarAttachment_" .. v.type) .. " <LINE> "
						end
					end
				end
			end
			subOption.subOption = nil;
			tooltip.description = text;
			subOption.toolTip = tooltip;
		end
	end
end

function ISHotbar:isInHotbar(item)
	if not self.attachedItems then return false; end
	for i, equipped in pairs(self.attachedItems) do
		if equipped == item then
			return true;
		end
	end
	return false;
end

function ISHotbar:setSizeAndPosition()
	local width = #self.availableSlot * self.slotWidth;
	width = width + (#self.availableSlot - 1) * self.slotPad;
	self:setWidth(width + self.margins * 2);

	local screenX = getPlayerScreenLeft(self.playerNum)
	local screenY = getPlayerScreenTop(self.playerNum)
	local screenW = getPlayerScreenWidth(self.playerNum)
	local screenH = getPlayerScreenHeight(self.playerNum)

	self:setX(screenX + (screenW - self.width) / 2);
	self:setY(screenY + screenH - self.height);
end

function ISHotbar:update()

	local moodleUI = UIManager.getMoodleUI(self.playerNum)
	if not self:isVisible() and moodleUI and moodleUI:isVisible() and not JoypadState.players[self.playerNum+1] then
		-- Controller unplugged
		self:setVisible(true)
	end

	if self.needsRefresh then
		self:refresh()
	end

	self:setSizeAndPosition()

	-- don't update during other actions (to avoid flicking during equipping etc.)
	local queue = ISTimedActionQueue.queues[self.character];
	if queue and #queue.queue > 0 then
		return;
	end
	-- check if we need to remove item from the hotbar or attached model on the player
	for i, item in pairs(self.attachedItems) do
		local slot = self.availableSlot[item:getAttachedSlot()]
		if not slot or not self:canBeAttached(slot, item) or not self.chr:getInventory():contains(item) or item:isBroken() then
			self:removeItem(item, false);
			self.chr:removeAttachedItem(item);
		else
			local slotDef = slot.def;
			if self.chr:isEquipped(item) then
				self.chr:removeAttachedItem(item);
			elseif not self.chr:getAttachedItem(item:getAttachedToModel()) then -- ensure it's attached
--				self.chr:setAttachedItem(slotDef.attachments[item:getAttachmentType()], item);
				self:attachItem(item, slotDef.attachments[item:getAttachmentType()], item:getAttachedSlot(), slotDef, false)
			end
		end
	end
end

function ISHotbar:onRightMouseUp(x, y)
	local clickedSlot = math.floor(x / (self.slotWidth));
	if clickedSlot >= #self.availableSlot then
		clickedSlot = #self.availableSlot - 1;
	end
	
	clickedSlot = clickedSlot + 1;

	self:doMenu(clickedSlot);
end

function ISHotbar:canBeAttached(slot, item)
	local slotDef = slot.def;
	for i,v in pairs(slotDef.attachments) do
		if item:getAttachmentType() == i then
			local doIt = true;
			if self.replacements and self.replacements[item:getAttachmentType()] then
				slot = self.replacements[item:getAttachmentType()];
				if slot == "null" then
					doIt = false;
				end
			end
			if doIt then
				return true;
			end
		end
	end
	return false;
end

-- Set the variable depending on where the item is (to trigger either back, holster or belt anim..)
function ISHotbar:setAttachAnim(item, slot)
	if slot then
		self.chr:SetVariable("AttachAnim", slot.animset);
		return;
	end
	for i,slot in pairs(self.availableSlot) do
		if slot.def.type == item:getAttachedSlotType() then
			self.chr:SetVariable("AttachAnim", slot.def.animset);
			break;
		end
	end
end

-- remove an item from the hotbar
function ISHotbar:removeItem(item, doAnim)
	if doAnim then
		self:setAttachAnim(item);
		ISTimedActionQueue.add(ISDetachItemHotbar:new(self.chr, item));
	else
		self.chr:removeAttachedItem(item);
		item:setAttachedSlot(-1);
		item:setAttachedSlotType(nil);
		item:setAttachedToModel(nil);
		
		self:reloadIcons();
	end
end

function ISHotbar:isItemAttached(item)
	for i, attached in pairs(self.attachedItems) do
		if attached == item then
			return true;
		end
	end
	return false;
end

function ISHotbar:attachItem (item, slot, slotIndex, slotDef, doAnim)
	if doAnim then
		if self.replacements and self.replacements[item:getAttachmentType()] then
			slot = self.replacements[item:getAttachmentType()];
		end
		self:setAttachAnim(item, slotDef);
		ISInventoryPaneContextMenu.transferIfNeeded(self.chr, item)
		-- first remove the current equipped one if needed
		if self.attachedItems[slotIndex] then
			ISTimedActionQueue.add(ISDetachItemHotbar:new(self.chr, self.attachedItems[slotIndex]));
		end
		ISTimedActionQueue.add(ISAttachItemHotbar:new(self.chr, item, slot, slotIndex, slotDef));
	else
		-- add new item
		-- if the item need to be attached elsewhere than its original emplacement because of a bag for example
		if self.replacements and self.replacements[item:getAttachmentType()] then
			slot = self.replacements[item:getAttachmentType()];
			if slot == "null" then
				self:removeItem(item, false);
				return;
			end
		end
		self.chr:setAttachedItem(slot, item);
		item:setAttachedSlot(slotIndex);
		item:setAttachedSlotType(slotDef.type);
		item:setAttachedToModel(slot);
		
		self:reloadIcons();
	end
end

function ISHotbar:reloadIcons()
	self.attachedItems = {};
	for i=0, self.chr:getInventory():getItems():size()-1 do
		local item = self.chr:getInventory():getItems():get(i);
		if item:getAttachedSlot() > -1 then
			self.attachedItems[item:getAttachedSlot()] = item;
		end
	end
end

function ISHotbar:haveThisSlot(slotType, list)
	if not list then
		list = self.availableSlot;
	end
	for i,v in pairs(list) do
		if v.slotType == slotType then
			return true;
		end
	end
	return false;
end

function ISHotbar:compareWornItems()
	local wornItems = self.chr:getWornItems()
	if #self.wornItems ~= wornItems:size() then
		return true
	end
	for index,item in ipairs(self.wornItems) do
		if item ~= wornItems:getItemByIndex(index-1) then
			return true
		end
	end
	return false
end

-- redo our slots when clothing has changed
function ISHotbar:refresh()
	self.needsRefresh = false

	-- the clothingUpdate is called quite often, we check if we changed any clothing to be sure we need to refresh
	-- as it can be called also when adding blood/holes..
	local refresh = false;

	if not self.wornItems then
		self.wornItems = {};
		refresh = true;
	elseif self:compareWornItems() then
		refresh = true;
	end
	
	if not refresh then
		return;
	end

	local previousSlot = self.availableSlot;
	local newSlots = {};
	local newIndex = 2;
	local slotIndex = #self.availableSlot + 1;

	-- always have a back attachment
	local slotDef = self:getSlotDef("Back");
	newSlots[1] = {slotType = slotDef.type, name = slotDef.name, def = slotDef};
	
	self.replacements = {};
	table.wipe(self.wornItems)
	
	-- check to add new availableSlot if we have new equipped clothing that gives some
	-- we first do this so we keep our order in hotkeys (equipping new emplacement will make them goes on last position)
	for i=0, self.chr:getWornItems():size()-1 do
		local item = self.chr:getWornItems():getItemByIndex(i);
		table.insert(self.wornItems, item)
		-- Skip bags in hands
		if item and self.chr:isHandItem(item) then
			item = nil
		end
		-- item gives some attachments
		if item and item:getAttachmentsProvided() then
			for j=0, item:getAttachmentsProvided():size()-1 do
				local slotDef = self:getSlotDef(item:getAttachmentsProvided():get(j));
				if slotDef then
					newSlots[newIndex] = {slotType = slotDef.type, name = slotDef.name, def = slotDef};
					newIndex = newIndex + 1;
					if not self:haveThisSlot(slotDef.type) then
						self.availableSlot[slotIndex] = {slotType = slotDef.type, name = slotDef.name, def = slotDef, texture = item:getTexture()};
						slotIndex = slotIndex + 1;
						self:savePosition();
					else
						-- This sets the slot texture after loadPosition().
						for i2,slot in pairs(self.availableSlot) do
							if slot.slotType == slotDef.type then
								slot.texture = item:getTexture()
								break
							end
						end
					end
				end
			end
		end
		if item and item:getAttachmentReplacement() then -- item has a replacement
			local replacementDef = self:getSlotDefReplacement(item:getAttachmentReplacement());
			if replacementDef then
				for type, model in pairs(replacementDef.replacement) do
					self.replacements[type] = model;
				end
			end
		end
	end

	-- check if we're missing slots
	if #self.availableSlot ~= #newSlots then
		local removed = 0;
		if #self.availableSlot > #newSlots then
			removed = #self.availableSlot - #newSlots;
		end
		for i,v in pairs(self.availableSlot) do
			if not self:haveThisSlot(v.slotType, newSlots) then
				-- remove the attached items that was in a slot we just lost
				if self.attachedItems[i] then
					self:removeItem(self.attachedItems[i], false);
					self.attachedItems[i] = nil;
				end
				-- we gonna check if we had an item in a slot that has a bigger index and was removed to move it
				if self.attachedItems[i + removed] then
					self.attachedItems[i] = self.attachedItems[i + removed];
					self.attachedItems[i]:setAttachedSlot(i);
					self.attachedItems[i + removed] = nil;
				end
				self.availableSlot[i] = nil;
			end
		end
		
		self:savePosition();
	end
	
	newSlots = {};
	-- now we redo our correct order
	local currentIndex = 1;
	for i,v in pairs(self.availableSlot) do
		newSlots[currentIndex] = v;
		currentIndex = currentIndex + 1;
	end
	
	self.availableSlot = newSlots;
	
	-- we re attach out items, if we added a bag for example, we need to redo the correct attachment
	for i, item in pairs(self.attachedItems) do
		local slot = self.availableSlot[item:getAttachedSlot()];
		local slotDef = slot.def;
		local slotIndex = item:getAttachedSlot();
		self:removeItem(item, false);
		-- we get back what model it should be on, as it can change if we remove a replacement (have a bag + something on your back, remove bag, we need to get the original attached definition)
		if self.chr:getInventory():contains(item) and not item:isBroken() then
			self:attachItem(item, slotDef.attachments[item:getAttachmentType()], slotIndex, self:getSlotDef(slot.slotType), false);
		end
	end
	
	local width = #self.availableSlot * self.slotWidth;
	width = width + (#self.availableSlot - 1) * 2;
	self:setWidth(width + 10);

	self:reloadIcons();
end

function ISHotbar:activateSlot(slotIndex)
	local item = self.attachedItems[slotIndex]
	if not item then return end
	if item:getAttachedSlot() ~= slotIndex then
		error "item:getAttachedSlot() ~= slotIndex"
	end
	if item:canBeActivated() then
		item:setActivated(not item:isActivated())
		return
	end
	self:equipItem(item)
end

function ISHotbar:equipItem(item)
	ISInventoryPaneContextMenu.transferIfNeeded(self.chr, item)
	
	local equip = true;
	if self.chr:getPrimaryHandItem() == item then
		ISTimedActionQueue.add(ISUnequipAction:new(self.chr, item, 20));
		equip = false;
	end
	if equip and self.chr:getSecondaryHandItem() == item then
		ISTimedActionQueue.add(ISUnequipAction:new(self.chr, item, 20));
		equip = false;
	end
	
	if equip then
		local primary = self.chr:getPrimaryHandItem()
		if primary and self:isInHotbar(primary) then
			ISTimedActionQueue.add(ISUnequipAction:new(self.chr, primary, 20));
		end
		-- Drop corpse or generator
		if isForceDropHeavyItem(primary) then
			ISTimedActionQueue.add(ISUnequipAction:new(self.chr, primary, 50));
		end
		ISTimedActionQueue.add(ISEquipWeaponAction:new(self.chr, item, 20, true, item:isTwoHandWeapon()));
	end
	
	self.chr:getInventory():setDrawDirty(true);
	getPlayerData(self.chr:getPlayerNum()).playerInventory:refreshBackpacks();
--	self:refresh();
end

function ISHotbar:getSlotIndexAt(x, y)
	if x >= 0 and x < self.width and y >= 0 and y < self.height then
		local index = math.floor((x - self.margins) / (self.slotWidth + self.slotPad)) + 1
		index = math.max(index, 1)
		return math.min(index, #self.availableSlot)
	end
	return -1
end

function ISHotbar:onMouseUp(x, y)
	local counta = 1;
	if ISMouseDrag.dragging then
		local clickedSlot = self:getSlotIndexAt(x, y);
		local slot = self.availableSlot[clickedSlot];
		local dragging = ISInventoryPane.getActualItems(ISMouseDrag.dragging);
		for i,v in ipairs(dragging) do
			if (v ~= self.attachedItems[clickedSlot]) and self:canBeAttached(slot, v) then
				local weapon = v;
				self:attachItem(weapon, slot.def.attachments[weapon:getAttachmentType()], clickedSlot, slot.def, true);
				break;
			end
		end
	end
end

-- load our position to be sure everything stay the same and we don't attach something where it shouldn't
function ISHotbar:loadPosition()
	local modData = self.chr:getModData();
	if modData["hotbar"] then
		for i,v in pairs(modData["hotbar"]) do
			local slotDef = self:getSlotDef(v);
			if slotDef then
				self.availableSlot[i] = {slotType = slotDef.type, name = slotDef.name, def = slotDef};
			end
		end
	else
		local slotDef = self:getSlotDef("Back");
		self.availableSlot[1] = {slotType = slotDef.type, name = slotDef.name, def = slotDef};
	end
end

function ISHotbar:savePosition()
	local modData = self.chr:getModData();
	modData["hotbar"] = {};
	for i,v in pairs(self.availableSlot) do
		modData["hotbar"][i] = v.slotType;
	end
end

function ISHotbar:getSlotForKey(key)
	if key == getCore():getKey("Hotbar 1") then return 1; end
	if key == getCore():getKey("Hotbar 2") then return 2; end
	if key == getCore():getKey("Hotbar 3") then return 3; end
	if key == getCore():getKey("Hotbar 4") then return 4; end
	if key == getCore():getKey("Hotbar 5") then return 5; end
	return -1
end

ISHotbar.onKeyStartPressed = function(key)
	local playerObj = getSpecificPlayer(0)
	if not getPlayerHotbar(0) or not playerObj or playerObj:isDead() then
		return
	end
	if UIManager.getSpeedControls() and (UIManager.getSpeedControls():getCurrentGameSpeed() == 0) then
		return
	end
	if JoypadState.players[1] then
		return
	end
	local self = getPlayerHotbar(0)
	local slotToCheck = self:getSlotForKey(key)
	if slotToCheck == -1 then return end
	local radialMenu = getPlayerRadialMenu(0)
	if getCore():getOptionRadialMenuKeyToggle() and radialMenu:isReallyVisible() then
		getPlayerHotbar(0).radialWasVisible = true
		radialMenu:removeFromUIManager()
		return
	end
	getPlayerHotbar(0).keyPressedMS = getTimestampMs()
	getPlayerHotbar(0).radialWasVisible = false
end

ISHotbar.onKeyPressed = function(key)
	local playerObj = getSpecificPlayer(0)
	if not getPlayerHotbar(0) or not playerObj or playerObj:isDead() then
		return
	end
	if UIManager.getSpeedControls() and (UIManager.getSpeedControls():getCurrentGameSpeed() == 0) then
		return
	end
	if JoypadState.players[1] then
		return
	end
	local self = getPlayerHotbar(0);
	local slotToCheck = self:getSlotForKey(key)
	if slotToCheck == -1 then return end
	local radialMenu = getPlayerRadialMenu(0)
	if radialMenu:isReallyVisible() or getPlayerHotbar(0).radialWasVisible then
		if not getCore():getOptionRadialMenuKeyToggle() then
			radialMenu:removeFromUIManager()
		end
		return
	end
	if playerObj:isAttacking() then
		return;
	end
	
	-- don't do hotkey if you're doing action
	local queue = ISTimedActionQueue.queues[playerObj];
	if queue and #queue.queue > 0 then
		return;
	end

	self:activateSlot(slotToCheck);
end

function ISHotbar:onRadialAttach(item, slotIndex, v)
	local slot = self.availableSlot[slotIndex]
	self:attachItem(item, v, slotIndex, slot.def, true)
end

function ISHotbar:onRadialRemove(item)
	self:removeItem(item, true)
end

ISHotbar.onKeyKeepPressed = function(key)
	local playerObj = getSpecificPlayer(0)
	if not getPlayerHotbar(0) or not playerObj or playerObj:isDead() then
		return
	end
	if UIManager.getSpeedControls() and (UIManager.getSpeedControls():getCurrentGameSpeed() == 0) then
		return
	end
	if JoypadState.players[1] then
		return
	end
	if playerObj:isAttacking() then
		return
	end
	local queue = ISTimedActionQueue.queues[playerObj]
	if queue and #queue.queue > 0 then
		return
	end
	if getPlayerHotbar(0).radialWasVisible then
		return
	end
	local self = getPlayerHotbar(0);
	local slotToCheck = self:getSlotForKey(key)
	if slotToCheck == -1 then
		return
	end
	local radialMenu = getPlayerRadialMenu(0)
	if self.availableSlot[slotToCheck] and (getTimestampMs() - self.keyPressedMS > 500) and not radialMenu:isReallyVisible() then
		radialMenu:clear()
		local inv = playerObj:getInventory():getItems()
		for i=1,inv:size() do
			local item = inv:get(i-1)
			if self:isItemAttached(item) then
				
			elseif item:getAttachmentType() and item:getCondition() > 0 and self.replacements[item:getAttachmentType()] ~= "null" then
				local slot = self.availableSlot[slotToCheck]
				local slotDef = slot.def
				for type,v in pairs(slotDef.attachments) do
					if item:getAttachmentType() == type then
						radialMenu:addSlice(item:getDisplayName(), item:getTex(), ISHotbar.onRadialAttach, self, item, slotToCheck, v)
						break
					end
				end
			end
		end
		if self.attachedItems[slotToCheck] then
			local item = self.attachedItems[slotToCheck]
			radialMenu:addSlice(getText("ContextMenu_HotbarRadialRemove", item:getDisplayName()), getTexture("media/ui/ZoomOut.png"), ISHotbar.onRadialRemove, self, item)
		end
		radialMenu:setX(getPlayerScreenLeft(0) + getPlayerScreenWidth(0) / 2 - radialMenu:getWidth() / 2)
		radialMenu:setY(getPlayerScreenTop(0) + getPlayerScreenHeight(0) / 2 - radialMenu:getHeight() / 2)
		radialMenu:addToUIManager()
	end
end

ISHotbar.onClothingUpdated = function(player)
	local hotbar = getPlayerHotbar(player:getPlayerNum());
	if hotbar == nil then return end -- player is dead
--	hotbar:refresh();
	hotbar.needsRefresh = true
end

function ISHotbar:new(character)
	local o = {}
	local width = 400;
	local slotWidth = 60;
	local slotHeight = 60;
	local height = slotHeight + 10;
	local x = getCore():getScreenWidth() / 2;
	local y = getCore():getScreenHeight() - slotHeight - 10;

	o = ISPanelJoypad:new(x, y, width, height);
	setmetatable(o, self)
	self.__index = self

	o.slotWidth = slotWidth;
	o.slotHeight = slotHeight;
	o.slotPad = 4
	o.margins = 5
	o.availableSlot = {};
	o.character = character;
	o.chr = character;
	o:loadPosition();
	o.playerNum = character:getPlayerNum()
	o.borderColor = {r=0.8, g=0.8, b=0.8, a=0.8};
	o.textColor = {r=0.8, g=0.8, b=0.8, a=0.8};
	o.width = width;
	o.height = height;
--	o:noBackground();
	o.anchorLeft = true;
	o.anchorRight = true;
	o.anchorTop = true;
	o.anchorBottom = true;
	o.attachedItems = {};
	o.font = UIFont.Small;
	o.needsRefresh = false
	o:refresh();
	--o.qualityStars = {};
	--o.qualityStars[0] = getTexture("media/ui/QualityStar_0.png");
	--o.qualityStars[1] = getTexture("media/ui/QualityStar_1.png");
	--o.qualityStars[2] = getTexture("media/ui/QualityStar_2.png");
	--o.qualityStars[3] = getTexture("media/ui/QualityStar_3.png");
	--o.qualityStars[4] = getTexture("media/ui/QualityStar_4.png");
	--o.qualityStars[5] = getTexture("media/ui/QualityStar_5.png");
	o.equippedItemIcon = getTexture("media/ui/icon.png");
	return o;
end

local function OnGameStart()
--	Events.OnSave.Add(ISHotbar.onSave);
	Events.OnClothingUpdated.Add(ISHotbar.onClothingUpdated);
	Events.OnKeyStartPressed.Add(ISHotbar.onKeyStartPressed);
	Events.OnKeyPressed.Add(ISHotbar.onKeyPressed);
	Events.OnKeyKeepPressed.Add(ISHotbar.onKeyKeepPressed);
end

Events.OnGameStart.Add(OnGameStart);


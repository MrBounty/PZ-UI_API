--***********************************************************
--**                    ROBERT JOHNSON                     **
--***********************************************************

require "ISUI/ISCollapsableWindow"

ISGarmentUI = ISCollapsableWindow:derive("ISGarmentUI");
ISGarmentUI.windows = {}

local FONT_HGT_SMALL = getTextManager():getFontHeight(UIFont.Small)
local FONT_HGT_MEDIUM = getTextManager():getFontHeight(UIFont.Medium)

function ISGarmentUI:update()
	ISCollapsableWindow.update(self);
	
	if not self.clothing or not self.clothing:isInPlayerInventory() then
		self:close();
	end
end

function ISGarmentUI:initialise()
	ISCollapsableWindow.initialise(self);
	self:create();
	
	self.listbox = ISScrollingListBox:new(self.bodyPartColumn, 30 + (FONT_HGT_SMALL * 2), self.width - self.bodyPartColumn, self.height - 30);
	self.listbox:initialise();
	self.listbox:instantiate();
	self.listbox:setAnchorLeft(true);
	self.listbox:setAnchorRight(true);
	self.listbox:setAnchorTop(true);
	self.listbox:setAnchorBottom(false);
	self.listbox.itemheight = 128;
	self.listbox.drawBorder = false
	self.listbox.backgroundColor.a = 0
	self.listbox.doDrawItem = ISGarmentUI.doDrawItem;
	self.listbox.onRightMouseUp = ISGarmentUI.onBodyPartListRightMouseUp
	self:addChild(self.listbox)

	self.parts = {};
	
	local minX = 1000
	local minY = 1000
	local maxX = -1000
	local maxY = -1000

	for i=0, self.clothing:getCoveredParts():size() - 1 do
		local part = self.clothing:getCoveredParts():get(i);
		local texture = self.textures[part:toString()]
		if texture then
			table.insert(self.parts, part);
			self.listbox:addItem('dummy', part)
			texture = texture.texture
			if texture then
				minX = math.min(minX, texture:getOffsetX())
				minY = math.min(minY, texture:getOffsetY())
				maxX = math.max(maxX, texture:getOffsetX() + texture:getWidth())
				maxY = math.max(maxY, texture:getOffsetY() + texture:getHeight())
			end
		end
	end

	self.texturesY = 30

	-- Leave room for "Can't be repaired." above the textures
	if not self.clothing:getFabricType() then
		self.texturesY = self.texturesY + FONT_HGT_SMALL
	end

	self.texturesYOffset = self.texturesY - minY
	self.texturesHeight = maxY - minY

	self:calculateHeight(true)
end

function ISGarmentUI:calculateHeight(doListHeight)
	if doListHeight then
		local y = 0
		for _,item in ipairs(self.listbox.items) do
			local part = item.item
			y = y + FONT_HGT_SMALL
			if self.clothing:getVisual():getHole(part) > 0 then
				y = y + FONT_HGT_SMALL
			end
			if self.clothing:getBloodlevelForPart(part) > 0 then
				y = y + FONT_HGT_SMALL
			end
			local patch = self.clothing:getPatchType(part)
			if patch then
				y = y + FONT_HGT_SMALL
			end
		end
		self.listbox.listHeight = y
	end
	self.listbox:setHeight(self.listbox.listHeight)
	local height = math.max(self.texturesY + self.texturesHeight, self.listbox:getBottom())
	height = height + 20 + FONT_HGT_SMALL + self.progressHeight + 8
	self:setHeight(height);
end

function ISGarmentUI:onBodyPartListRightMouseUp(x, y)
	local row = self:rowAt(x, y)
	if row < 1 or row > #self.items then return end
	self.parent:doContextMenu(self.items[row].item, getMouseX(), getMouseY())
end

function ISGarmentUI:doPatch(fabric, thread, needle, part, context, submenu)
	if not self.clothing:getFabricType() then
		return;
	end
	
	local hole = self.clothing:getVisual():getHole(part) > 0;
	local patch = self.clothing:getPatchType(part);
	
	local text;
	local allText;

	if hole then
		text = getText("ContextMenu_PatchHole");
		allText = getText("ContextMenu_PatchAllHoles") .. fabric:getDisplayName();
	elseif not patch then
		text = getText("ContextMenu_AddPadding");
		allText = getText("ContextMenu_AddPaddingAll") .. fabric:getDisplayName();
	else
		error "patch ~= nil"
	end
	
	if not submenu then -- after the 2nd iteration we have a submenu, we simply add our different fabric to it
		local option = context:addOption(text);
		submenu = context:getNew(context);
		context:addSubMenu(option, submenu);
	end

	local option = submenu:addOption(fabric:getDisplayName(), self.chr, ISInventoryPaneContextMenu.repairClothing, self.clothing, part, fabric, thread, needle)
	local tooltip = ISInventoryPaneContextMenu.addToolTip();
	if self.clothing:canFullyRestore(self.chr, part, fabric) then
		tooltip.description = getText("IGUI_perks_Tailoring") .. " :" .. self.chr:getPerkLevel(Perks.Tailoring) .. " <LINE> <RGB:0,1,0> " .. getText("Tooltip_FullyRestore");
	else
		tooltip.description = getText("IGUI_perks_Tailoring") .. " :" .. self.chr:getPerkLevel(Perks.Tailoring) .. " <LINE> <RGB:0,1,0> " .. getText("Tooltip_ScratchDefense")  .. " +" .. Clothing.getScratchDefenseFromItem(self.chr, fabric) .. " <LINE> " .. getText("Tooltip_BiteDefense") .. " +" .. Clothing.getBiteDefenseFromItem(self.chr, fabric);
	end
	option.toolTip = tooltip;

	-- Patch/Add pad all
	local allOption;
	local allTooltip = ISInventoryPaneContextMenu.addToolTip();

	if(self.chr:getInventory():getItemCount(fabric:getType(), true) > 1) then
		if hole and (self.clothing:getHolesNumber() > 1) then
			allOption = submenu:addOption(allText, self.chr, ISInventoryPaneContextMenu.repairAllClothing, self.clothing, self.parts, fabric, thread, needle, true)
			allTooltip.description = getText("Tooltip_PatchAllHoles") .. fabric:getDisplayName();
			allOption.toolTip = allTooltip;
		elseif not hole and not patch and (ISGarmentUI:getPaddablePartsNumber(self.clothing, self.parts) > 1) then
			allOption = submenu:addOption(allText, self.chr, ISInventoryPaneContextMenu.repairAllClothing, self.clothing, self.parts, fabric, thread, needle, false)
			allTooltip.description = getText("Tooltip_AddPaddingToAll") .. fabric:getDisplayName();
			allOption.toolTip = allTooltip;
		end
	end

	return submenu;
end

function ISGarmentUI:getPaddablePartsNumber(clothing, parts)
	local count = 0;

	for i=1, #parts do
		local part = parts[i];
		local hole = clothing:getVisual():getHole(part) > 0;
		local patch = clothing:getPatchType(part);
		if(hole == false and patch == nil) then
			count = count + 1;
		end
	end

	return count;
end

function ISGarmentUI:doContextMenu(part, x, y)
	local context = ISContextMenu.get(self.chr:getPlayerNum(), x, y);
	
	-- you need thread and needle
	local thread = self.chr:getInventory():getItemFromType("Thread", true, true);
	local needle = self.chr:getInventory():getItemFromType("Needle", true, true);
	local fabric1 = self.chr:getInventory():getItemFromType("RippedSheets", true, true);
	local fabric2 = self.chr:getInventory():getItemFromType("DenimStrips", true, true);
	local fabric3 = self.chr:getInventory():getItemFromType("LeatherStrips", true, true);

	-- Require a needle to remove a patch.  Maybe scissors or a knife instead?
	local patch = self.clothing:getPatchType(part)
	if patch then
		-- Remove specific patch
		local removeOption = context:addOption(getText("ContextMenu_RemovePatch"), self.chr, ISInventoryPaneContextMenu.removePatch, self.clothing, part, needle)
		local tooltip = ISInventoryPaneContextMenu.addToolTip();
		removeOption.toolTip = tooltip;

		-- Remove all patches
		local patchesCount = self.clothing:getPatchesNumber();
		local removeAllOption;
		local removeAllTooltip;
		if (patchesCount > 1) then
			removeAllOption = context:addOption(getText("ContextMenu_RemoveAllPatches"), self.chr, ISInventoryPaneContextMenu.removeAllPatches, self.clothing, self.parts, needle);
			removeAllTooltip = ISInventoryPaneContextMenu.addToolTip();
			removeAllOption.toolTip = removeAllTooltip;
		end

		if needle then
			tooltip.description = getText("Tooltip_GetPatchBack", ISRemovePatch.chanceToGetPatchBack(self.chr)) .. " <LINE> <RGB:1,0,0> " .. getText("Tooltip_ScratchDefense")  .. " -" .. patch:getScratchDefense() .. " <LINE> " .. getText("Tooltip_BiteDefense") .. " -" .. patch:getBiteDefense();
			if(removeAllTooltip ~= nil) then
				removeAllTooltip.description = getText("Tooltip_GetPatchesBack", ISRemovePatch.chanceToGetPatchBack(self.chr)) .. " <LINE> <RGB:1,0,0> " .. getText("Tooltip_ScratchDefense")  .. " -" .. (patch:getScratchDefense() * patchesCount) .. " <LINE> " .. getText("Tooltip_BiteDefense") .. " -" .. (patch:getBiteDefense() * patchesCount);
			end
		else
			tooltip.description = getText("ContextMenu_CantRemovePatch");
			removeOption.notAvailable = true
			if(removeAllTooltip ~= nil) then
				removeAllTooltip.description = getText("ContextMenu_CantRemovePatch");
				removeAllOption.notAvailable = true;
			end
		end
		return context;
	end

	-- Cannot patch without thread, needle and fabric
	if not thread or not needle or (not fabric1 and not fabric2 and not fabric3) then
		local patchOption = context:addOption(getText("ContextMenu_Patch"));
		patchOption.notAvailable = true;
		local tooltip = ISInventoryPaneContextMenu.addToolTip();
		tooltip.description = getText("ContextMenu_CantRepair");
		patchOption.toolTip = tooltip;
		return context;
	end
	
	local submenu;
	local allSubmenu;
	if fabric1 then
		submenu = self:doPatch(fabric1, thread, needle, part, context, submenu);
	end
	if fabric2 then
		submenu = self:doPatch(fabric2, thread, needle, part, context, submenu);
	end
	if fabric3 then
		submenu = self:doPatch(fabric3, thread, needle, part, context, submenu);
	end

	return context;
end

function ISGarmentUI:doDrawItem(y, item, alt)
	local part = item.item;
	
	if item.itemindex == self.selected then
		self:drawRect(0, y, self:getWidth(), item.height, 0.1, 1.0, 1.0, 1.0);
	elseif item.itemindex == self.mouseoverselected then
		self:drawRect(0, y, self:getWidth(), item.height, 0.05, 1.0, 1.0, 1.0);
	end

	self:drawText(part:getDisplayName(), 0, y, 1, 1, 1, 1, UIFont.Small)
	self:drawText(self.parent.clothing:getDefForPart(part, true, false) .. "%", self.parent.biteColumn - self.x, y, 1, 1, 1, 1, UIFont.Small)
	self:drawText(self.parent.clothing:getDefForPart(part, false, false) .. "%", self.parent.scratchColumn - self.x, y, 1, 1, 1, 1, UIFont.Small)
	self:drawText(self.parent.clothing:getDefForPart(part, false, true) .. "%", self.parent.bulletColumn - self.x, y, 1, 1, 1, 1, UIFont.Small)

	if self.parent.clothing:getVisual():getHole(part) > 0 then
		y = y + FONT_HGT_SMALL;
		self:drawText(getText("IGUI_garment_Hole"), 10, y, 1, 0.3, 0.3, 1, UIFont.Small)
	end
	
	if self.parent.clothing:getBloodlevelForPart(part) > 0 then
		y = y + FONT_HGT_SMALL;
		self:drawText(getText("IGUI_garment_Blood") .. round(self.parent.clothing:getBloodlevelForPart(part) * 100, 0) .. "%", 10, y, 1, 0.3, 0.3, 1, UIFont.Small)
	end
	
	local patch = self.parent.clothing:getPatchType(part);
	if patch then
		y = y + FONT_HGT_SMALL;
		self:drawText("- " .. getText("IGUI_TypeOfPatch", patch:getFabricTypeName()), 10, y, 0.7, 1, 0.7, 1, UIFont.Small)
	end
	
	return y + FONT_HGT_SMALL;
end

function ISGarmentUI:render()
	-- TODO: HELPING DEBUG TO REMOVE!
--				self.listbox.doDrawItem = ISGarmentUI.doDrawItem;
--				self.listbox.onRightMouseUp = ISGarmentUI.onBodyPartListRightMouseUp

	ISCollapsableWindow.render(self)
	local y = 30;
	
	self:drawText(getText("IGUI_garment_BodyPart"), self.bodyPartColumn, y, 1, 1, 1, 1, UIFont.Small)
	self:drawText(getText("IGUI_health_Scratch"), self.scratchColumn, y, 1, 1, 1, 1, UIFont.Small)
	self:drawText(getText("IGUI_health_Bite"), self.biteColumn, y, 1, 1, 1, 1, UIFont.Small)
	self:drawText(getText("IGUI_health_Bullet"), self.bulletColumn, y, 1, 1, 1, 1, UIFont.Small)
	y = y + (FONT_HGT_SMALL * 2);
	for i,part in ipairs(self.parts) do
		self:drawTexture(self.textures[part:toString()].texture, 5, self.texturesYOffset, 1, 1, 1, 1);
		if self.clothing:getVisual():getHole(part) > 0 and self.textures[part:toString()].hole then
			self:drawTexture(self.textures[part:toString()].hole, 5, self.texturesYOffset, 1, 1, 1, 1);
		end
		if self.clothing:getBloodlevelForPart(part) > 0 and self.textures[part:toString()].blood then
			self:drawTexture(self.textures[part:toString()].blood, 5, self.texturesYOffset, self.clothing:getBloodlevelForPart(part) + 0.1, 1, 1, 1);
		end
		if self.clothing:getPatchType(part) then
			self:drawTexture(self.textures[part:toString()].patch, 5, self.texturesYOffset, 1, 1, 1, 1);
		end
	end

	self:calculateHeight(false)

	self.progressY = self.height - 8 - self.progressHeight

	-- bottom blood/dirtyness level
	y = self.progressY - FONT_HGT_SMALL;
	local dirtX = self.bodyPartColumn + getTextManager():MeasureStringX(UIFont.Small, getText("IGUI_garment_GlbBlood")) + 20;
	self:drawText(getText("IGUI_invpanel_Condition"), self.progressX1, y, 1, 1, 1, 1, UIFont.Small)
	self:drawText(getText("IGUI_garment_GlbBlood"), self.progressX2, y, 1, 1, 1, 1, UIFont.Small)
	self:drawText(getText("IGUI_garment_GlbDirt"), self.progressX3, y, 1, 1, 1, 1, UIFont.Small)
	y = self.progressY;
	self:drawProgressBar(self.progressX1, y, self.progressWidth, self.progressHeight, self.clothing:getCondition() / self.clothing:getConditionMax(), self.fgBar)
	self:drawProgressBar(self.progressX2, y, self.progressWidth, self.progressHeight, self.clothing:getBloodlevel() / 100, self.fgBar)
	self:drawProgressBar(self.progressX3, y, self.progressWidth, self.progressHeight, self.clothing:getDirtyness() / 100, self.fgBar)
	y = y + FONT_HGT_SMALL;
	
	if not self.clothing:getFabricType() then
		local x = (self.bodyPartColumn - self.noRepairWidth) / 2
		self:drawText(getText("IGUI_garment_CantRepair"), x, 22, 1, 0, 0, 1, UIFont.Small)
	end
end

function ISGarmentUI:close()
	ISGarmentUI.windows[self.playerNum] = nil;
	self:removeFromUIManager()
	if JoypadState.players[self.playerNum+1] then
		setJoypadFocus(self.playerNum, self.prevFocus)
	end
end

function ISGarmentUI:addTextures(type, textureName, overlayName)
	self.textures[type] = {};
	self.textures[type].texture = getTexture("media/ui/BodyParts/bps_" .. self.sex .. textureName .. ".png")
	self.textures[type].hole = getTexture("media/ui/BodyParts/overlays/" .. self.sex .. "_clothing_overlays_holes" .. overlayName .. ".png")
	self.textures[type].blood = getTexture("media/ui/BodyParts/overlays/" .. self.sex .. "_clothing_overlays_blood" .. overlayName .. ".png")
	self.textures[type].patch = getTexture("media/ui/BodyParts/overlays/" .. self.sex .. "_clothing_overlays_patches" .. overlayName .. ".png")
if self.textures[type].texture then
	print(self.textures[type].texture:getHeight(),self.textures[type].texture:getHeightOrig())
end
end

function ISGarmentUI:create()
	self.textures = {};

	self:addTextures("Hand_L", "_left-hand", "_left_hand");
	self:addTextures("Hand_R", "_right-hand", "_right_hand");
	self:addTextures("ForeArm_L", "_lower-left-arm", "_lower_left_arm");
	self:addTextures("ForeArm_R", "_lower-right-arm", "_lower_right_arm");
	self:addTextures("UpperArm_L", "_upper-left-arm", "_upper_left_arm");
	self:addTextures("UpperArm_R", "_upper-right-arm", "_upper_right_arm");
	self:addTextures("Torso_Upper", "_chest", "_chest");
	self:addTextures("Torso_Lower", "_abdomen", "_abdomen");
	self:addTextures("Head", "_head", "_head");
	self:addTextures("Neck", "_neck", "_neck");
	self:addTextures("Groin", "_groin", "_groin");
	self:addTextures("UpperLeg_L", "_left-thigh", "_left_thigh");
	self:addTextures("UpperLeg_R", "_right-thigh", "_right_thigh");
	self:addTextures("LowerLeg_L", "_left-calf", "_left_calf");
	self:addTextures("LowerLeg_R", "_right-calf", "_right_calf");
	self:addTextures("Foot_L", "_left-foot", "_left_foot");
	self:addTextures("Foot_R", "_right-foot", "_right_foot");
end

function ISGarmentUI:calcProgressWidths()
	local width1 = getTextManager():MeasureStringX(UIFont.Small, getText("IGUI_invpanel_Condition"))
	local width2 = getTextManager():MeasureStringX(UIFont.Small, getText("IGUI_garment_GlbBlood"))
	local width3 = getTextManager():MeasureStringX(UIFont.Small, getText("IGUI_garment_GlbDirt"))
	self.progressWidth1 = math.max(width1, self.progressWidth)
	self.progressWidth2 = math.max(width2, self.progressWidth)
	self.progressWidth3 = math.max(width3, self.progressWidth)

	local padX = 20
	self.progressWidthTotal = self.progressWidth1 + padX + self.progressWidth2 + padX + self.progressWidth3
end

function ISGarmentUI:calcProgressPositions()
	local padX = 20
	self.progressX1 = (self.width - self.progressWidthTotal) / 2
	self.progressX2 = self.progressX1 + self.progressWidth1 + padX
	self.progressX3 = self.progressX2 + self.progressWidth2 + padX
end

function ISGarmentUI:calcColumnWidths()
	local partColumnWidth = 0
	for i=1,BloodBodyPartType.MAX:index() do
		local part = BloodBodyPartType.FromIndex(i-1)
		local width = getTextManager():MeasureStringX(UIFont.Small, part:getDisplayName())
		partColumnWidth = math.max(partColumnWidth, width)
	end
	local partSize = getTextManager():MeasureStringX(UIFont.Small, getText("IGUI_garment_BodyPart"))
	local biteSize = getTextManager():MeasureStringX(UIFont.Small, getText("IGUI_health_Bite"))
	local scratchSize = getTextManager():MeasureStringX(UIFont.Small, getText("IGUI_health_Scratch"))
	local bulletSize = getTextManager():MeasureStringX(UIFont.Small, getText("IGUI_health_Bullet"))
	partColumnWidth = math.max(partColumnWidth, partSize)
	self.biteColumn = self.bodyPartColumn + partColumnWidth + 10
	self.scratchColumn = math.max(self.biteColumn + 10 + biteSize)
	self.bulletColumn = math.max(self.scratchColumn + 10 + scratchSize)

	local scrollbarWidth = 17
	local listRight = self.bulletColumn + bulletSize + scrollbarWidth
	local progressWidth = self.progressWidthTotal + 20 * 2
	self:setWidth(math.max(listRight, progressWidth))
end

function ISGarmentUI:onGainJoypadFocus(joypadData)
	self.drawJoypadFocus = true
end

function ISGarmentUI:onJoypadDown(button, joypadData)
	if button == Joypad.AButton then
		local row = self.listbox.selected
		if self.listbox.items[row] then
			local context = self:doContextMenu(self.listbox.items[row].item,
				self:getAbsoluteX() + 50, self:getAbsoluteY() + 50)
			if context:getIsVisible() then
				context.mouseOver = 1
				context.origin = self
				joypadData.focus = context
				updateJoypadFocus(joypadData)
			end
		end
	end
	if button == Joypad.BButton then
		self:close()
	end
end

function ISGarmentUI:onJoypadDirUp(joypadData)
	self.listbox:onJoypadDirUp(joypadData)
end

function ISGarmentUI:onJoypadDirDown(joypadData)
	self.listbox:onJoypadDirDown(joypadData)
end

function ISGarmentUI:new(x, y, character, clothing)
	local playerNum = character:getPlayerNum()
	local width = 460
	if x == -1 then
		x = getPlayerScreenLeft(playerNum) + (getPlayerScreenWidth(playerNum) - width) / 2
	end
	local o = ISCollapsableWindow.new(self, x, y, width, 300);
	o.chr = character;
	o.playerNum = playerNum;
	o.title = clothing:getDisplayName();
	o.clothing = clothing;
	o.borderColor = {r=0.4, g=0.4, b=0.4, a=1};
	o.backgroundColor = {r=0, g=0, b=0, a=0.8};
	o.sex = "male";
	o.fgBar = {r=0, g=0.6, b=0, a=0.7 }
	o:setResizable(false)
	o.progressWidth = 100;
	o.progressHeight = 10;
	o.noRepairWidth = getTextManager():MeasureStringX(UIFont.Small, getText("IGUI_garment_CantRepair"))
	o.bodyPartColumn = math.max(160, 10 + o.noRepairWidth + 10);
	o:calcProgressWidths()
	o:calcColumnWidths()
	o:calcProgressPositions()
	o.texturesYOffset = 0;
	o.texturesHeight = 0;
	o.addedHeight = 0;
	return o;
end

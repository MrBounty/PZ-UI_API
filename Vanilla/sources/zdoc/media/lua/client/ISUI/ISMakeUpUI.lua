require "ISUI/ISCollapsableWindowJoypad"

---@class ISMakeUpUI : ISCollapsableWindowJoypad
ISMakeUpUI = ISCollapsableWindowJoypad:derive("ISMakeUpUI")
ISMakeUpUI.windows = {}

function ISMakeUpUI:createChildren()
	ISCollapsableWindowJoypad.createChildren(self)
	
	local btnWid = 100
	local btnHgt = 25
	local padBottom = 20
	local titleBarHeight = self:titleBarHeight()
	
	self.avatarX = 16
	self.avatarY = titleBarHeight + 25
	self.avatarWidth = 128
	self.avatarHeight = 128
	self.avatarPanel = ISUI3DModel:new(self.avatarX, self.avatarY, self.avatarWidth, self.avatarHeight)
	self.avatarPanel:setVisible(true)
	self:addChild(self.avatarPanel)
	self.avatarPanel:setOutfitName("Foreman", false, false)
	self.avatarPanel:setState("idle")
	self.avatarPanel:setDirection(IsoDirections.S)
	self.avatarPanel:setIsometric(false)
	
	self.avatarBackgroundTexture = getTexture("media/ui/avatarBackground.png")

	self.leftPanel = ISPanel:new(self.avatarPanel:getRight() + 20, self.avatarPanel.y, 120, self.height);
	self.leftPanel:noBackground();
	self:addChild(self.leftPanel);

	self.rightPanel = ISPanel:new(self.leftPanel:getRight() + 20, self.avatarPanel.y, 120, self.height);
	self.rightPanel:noBackground();
	self:addChild(self.rightPanel);
	
	self.addMakeupLbl = ISLabel:new(0, 0, 20, getText("IGUI_AddMakeUp"), 1, 1, 1, 1, UIFont.Medium, true)
	self.leftPanel:addChild(self.addMakeupLbl);
		
	self.location = ISComboBox:new(0, self.addMakeupLbl:getBottom() + 10, 120, 20, self, ISMakeUpUI.onSelectLocation)
	self.location.font = UIFont.Small
	self.location:initialise()
	self.location:instantiate()
	self.leftPanel:addChild(self.location)
	
	self:initLocationCombo();
	
	self.comboMakeup = ISComboBox:new(0, self.location:getBottom() + 10, 120, 20, self, ISMakeUpUI.onSelectMakeUp)
	self.comboMakeup.font = UIFont.Small
	self.comboMakeup:initialise()
	self.comboMakeup:instantiate()
	self.leftPanel:addChild(self.comboMakeup)
	
	self.comboMakeup:addOption(getText("IGUI_SelectBodyLocation"));
	self.comboMakeup.disabled = true;
	
	self.add = ISButton:new(0, self.comboMakeup:getBottom() + 10, btnWid, btnHgt, getText("UI_btn_apply"), self, ISMakeUpUI.onApplyMakeUp);
	self.add.anchorTop = false
	self.add.anchorBottom = true
	self.add:initialise();
	self.add:instantiate();
	self.add.borderColor = {r=1, g=1, b=1, a=0.1};
	self.add.enable = false;
	self.leftPanel:addChild(self.add);
	
	
	self.removeMakeupLbl = ISLabel:new(0, 0, 20, getText("IGUI_RemoveMakeUp"), 1, 1, 1, 1, UIFont.Medium, true)
	self.rightPanel:addChild(self.removeMakeupLbl);
	
	self.removeMakeupCombo = ISComboBox:new(0, self.removeMakeupLbl:getBottom() + 10, 120, 20, self, ISMakeUpUI.onSelectRemoveMakeUp)
	self.removeMakeupCombo.font = UIFont.Small
	self.removeMakeupCombo:initialise()
	self.removeMakeupCombo:instantiate()
	self.rightPanel:addChild(self.removeMakeupCombo)
	
	self.remove = ISButton:new(0, self.removeMakeupCombo:getBottom() + 10, btnWid, btnHgt, getText("UI_btn_remove"), self, ISMakeUpUI.onRemoveMakeUp);
	self.remove.anchorTop = false
	self.remove.anchorBottom = true
	self.remove:initialise();
	self.remove:instantiate();
	self.remove.borderColor = {r=1, g=1, b=1, a=0.1};
	self.remove.enable = false;
	self.rightPanel:addChild(self.remove);

	self:initRemoveMakeUpCombo();

	self:updateLayout();
end

function ISMakeUpUI:onSelectRemoveMakeUp()
	self.remove.enable = false;
	self:displayBodyPart(nil);
	local selected = self.removeMakeupCombo.options[self.removeMakeupCombo.selected].data;
	if not selected then
		return;
	end
	-- take the category to zoom in
	for i,v in pairs(MakeUpDefinitions.categories) do
		if v.category == selected.makeup.category then
			self:displayBodyPart(v);
			break;
		end
	end
	self:displayBodyPart(selected.makeup);
	self.remove.enable = true;
end

function ISMakeUpUI:onRemoveMakeUp()
	local selected = self.removeMakeupCombo.options[self.removeMakeupCombo.selected].data;
	self.character:removeWornItem(selected.item);
	self.character:getInventory():Remove(selected.item);
	self:reinitCombos();
	self.needsUpdateAvatar = true;
end

function ISMakeUpUI:initRemoveMakeUpCombo()
	self.remove.enable = false;
	self.removeMakeupCombo:clear();
	self.removeMakeupCombo.selected = 1;
	self.removeMakeupCombo.disabled = true;
	
	self.removeMakeupCombo:addOptionWithData(getText("IGUI_SelectMakeUp"), nil);
	for i=0, self.character:getWornItems():size()-1 do
		local item = self.character:getWornItems():get(i):getItem();
		if luautils.stringStarts(item:getBodyLocation(), "MakeUp") then
			-- we found makeup, take the corresponding definition
			for _,makeup in ipairs(MakeUpDefinitions.makeup) do
				if makeup.item == item:getFullType() then
					local name = getText("MakeUpType_" .. makeup.name)
					self.removeMakeupCombo:addOptionWithData(name, {item=item, makeup=makeup});
					break;
				end
			end
		end
	end
	if #self.removeMakeupCombo.options > 1 then
		self.removeMakeupCombo.disabled = false;
	end
	self.removeMakeupCombo:setWidthToOptions(120);
end

function ISMakeUpUI:initLocationCombo()
	self.location:clear();
	self.location.selected = 1;
	self.location:addOptionWithData(getText("IGUI_SelectBodyLocation"), nil);
	for i,v in pairs(MakeUpDefinitions.categories) do
		-- check we have at least one make up for this category available for our selected make up type (lipstick, eyes..)
		local add = false;
		for j,makeup in ipairs(MakeUpDefinitions.makeup) do
			if makeup.category == v.category and makeup.makeuptypes[self.item:getMakeUpType()] then
				add = true;
				break;
			end
		end
		if add then
			local name = getText("MakeUpCategory_" .. v.name)
			self.location:addOptionWithData(name, v);
		end
	end
	self.location:setWidthToOptions(120);
end

function ISMakeUpUI:reinit()
	if self.makeUpSelected then
		self.character:removeWornItem(self.makeUpSelected);
		self.makeUpSelected = nil;
	end
	-- add previous makeup if we had one at this location
	if self.previousMakeUp then
		self.character:setWornItem(self.previousMakeUp:getBodyLocation(), self.previousMakeUp);
	end
	self:reinitCombos();
	self.needsUpdateAvatar = true;
	self.needsUpdateLayout = true;
end

function ISMakeUpUI:reinitCombos()
	self:initLocationCombo();
	self.comboMakeup:clear();
	self.comboMakeup.selected = 1;
	self.comboMakeup.disabled = true;
	self:displayBodyPart(nil);
	self.comboMakeup:addOption(getText("IGUI_SelectBodyLocation"));
	self.comboMakeup:setWidthToOptions(120);
	self.location.selected = 1;
	
	self:initRemoveMakeUpCombo();
end

function ISMakeUpUI:setWidthToChildren(panel, minWidth)
	local maxWidth = minWidth
	for _,child in pairs(panel:getChildren()) do
		maxWidth = math.max(maxWidth, child:getRight())
	end
	panel:setWidth(maxWidth)
end

function ISMakeUpUI:updateLayout()
	self.location:setWidthToOptions(120);
	self.comboMakeup:setWidthToOptions(120);
	self:setWidthToChildren(self.leftPanel, 120);

	self.removeMakeupCombo:setWidthToOptions(120);
	self:setWidthToChildren(self.rightPanel, 120);
	self.rightPanel:setX(self.leftPanel:getRight() + 20);

	self:setWidth(self.rightPanel:getRight() + 20);
end

function ISMakeUpUI:onApplyMakeUp()
	self.add.enable = false;
	self.character:getInventory():AddItem(self.makeUpSelected);
	if self.previousMakeUp then
		self.character:getInventory():Remove(self.previousMakeUp);
	end
	self.previousMakeUp = nil;
	self.makeUpSelected = nil;

	self:reinitCombos();
end

function ISMakeUpUI:onSelectMakeUp()
	-- add previous makeup if we had one at this location
	if self.previousMakeUp then
		self.character:setWornItem(self.previousMakeUp:getBodyLocation(), self.previousMakeUp);
		self.needsUpdateAvatar = true;
	end
	self.add.enable = false;
	-- remove previous makeup
	if self.makeUpSelected then
		self.character:removeWornItem(self.makeUpSelected);
		self.needsUpdateAvatar = true;
		self.makeUpSelected = nil;
	end
	local selected = self.comboMakeup.options[self.comboMakeup.selected].data;
	if not selected then
		return;
	end
	local makeup = InventoryItemFactory.CreateItem(selected.item)
	-- backup previous makeup at this location in case we close
	self.previousMakeUp = self.character:getWornItem(makeup:getBodyLocation());
	self.character:setWornItem(makeup:getBodyLocation(), makeup);
	self.makeUpSelected = makeup;
	self.add.enable = true;
	self.needsUpdateAvatar = true;
end

function ISMakeUpUI:onSelectLocation()
	-- remove previous selected makeup
	if self.makeUpSelected then
		self.character:removeWornItem(self.makeUpSelected);
		self.needsUpdateAvatar = true;
		self.makeUpSelected = nil;
	end
	self.comboMakeup:clear();
	self.comboMakeup.selected = 1;
	local selected = self.location.options[self.location.selected].data;
	-- selected the default "Select a body location" option
	if not selected then
		self.comboMakeup.disabled = true;
		self:displayBodyPart(nil);
		self.comboMakeup:addOption(getText("IGUI_SelectBodyLocation"));
		self.comboMakeup:setWidthToOptions(120);
		return;
	end
	
	self:displayBodyPart(selected);
	-- do the makeup combo depending on item used to make makeup
	self.comboMakeup:addOption(getText("IGUI_SelectMakeUp"));
	for i,v in ipairs(MakeUpDefinitions.makeup) do
		if v.category == selected.category and v.makeuptypes[self.item:getMakeUpType()] then
			local name = getText("MakeUpType_" .. v.name)
			self.comboMakeup:addOptionWithData(name, v);
		end
	end
	self.comboMakeup.selected = 1;
	self.comboMakeup.disabled = false;
	self.comboMakeup:setWidthToOptions(120);
end

function ISMakeUpUI:updateAvatar()
	if self.needsUpdateAvatar then
		self.needsUpdateAvatar = false
		self.avatarPanel:setCharacter(self.character)
		triggerEvent("OnClothingUpdated", self.character)
	end
end

function ISMakeUpUI:prerender()
	ISCollapsableWindowJoypad.prerender(self)

	local x,y,w,h = self.avatarX, self.avatarY, self.avatarWidth, self.avatarHeight
	self:drawRectBorder(x - 2, y - 2, w + 4, h + 4, 1, 0.3, 0.3, 0.3);
	self:drawTextureScaled(self.avatarBackgroundTexture, x, y, w, h, 1, 1, 1, 1);
end

function ISMakeUpUI:update()
	ISCollapsableWindowJoypad.update(self)
	self:updateAvatar()
	if self.needsUpdateLayout then
		self.needsUpdateLayout = false
		self:updateLayout()
	end
end

function ISMakeUpUI:onGainJoypadFocus(joypadData)
	self.drawJoypadFocus = true
	self:insertNewLineOfButtons(self.location, self.removeMakeupCombo)
	self:insertNewLineOfButtons(self.comboMakeup, self.remove)
	self:insertNewLineOfButtons(self.add)

	self.joypadIndexY = 1
	self.joypadIndex = 1
	self.joypadButtons = self.joypadButtonsY[self.joypadIndexY]
	self.joypadButtons[self.joypadIndex]:setJoypadFocused(true)
end

function ISMakeUpUI:onJoypadDown(button)
	local children = self:getVisibleChildren(self.joypadIndexY)
	local child = children[self.joypadIndex]

	if button == Joypad.BButton and child and child.isCombobox and child.expanded then
		child.expanded = false
		child:hidePopup()
		return
	end

	if button == Joypad.BButton then
		self:close()
		return
	end

	ISCollapsableWindowJoypad.onJoypadDown(self, button)
end

function ISMakeUpUI:close()
	-- remove previous makeup
	if self.makeUpSelected then
		self.character:removeWornItem(self.makeUpSelected);
		self.makeUpSelected = nil;
		triggerEvent("OnClothingUpdated", self.character);
	end
	-- add previous makeup if we had one at this location
	if self.previousMakeUp then
		self.character:setWornItem(self.previousMakeUp:getBodyLocation(), self.previousMakeUp);
		triggerEvent("OnClothingUpdated", self.character);
	end
	self:removeFromUIManager()
	ISMakeUpUI.windows[self.playerNum+1] = nil;
	if JoypadState.players[self.playerNum+1] then
		setJoypadFocus(self.playerNum, self.prevFocus)
	end
end

function ISMakeUpUI:displayBodyPart(cat)
	if not cat then
		self.avatarPanel:setZoom(0);
		self.avatarPanel:setYOffset(0);
		self.avatarPanel:setXOffset(0);
	else
		if cat.avatarZoom then
			self.avatarPanel:setZoom(cat.avatarZoom);
		end
		if cat.avatarYOffset then
			self.avatarPanel:setYOffset(cat.avatarYOffset);
		end
		if cat.avatarXOffset then
			self.avatarPanel:setXOffset(cat.avatarXOffset);
		end
	end
end

function ISMakeUpUI:new(x, y, item, character)
	local width = 550
	local height = 200
	local playerNum = character:getPlayerNum()
	if y == 0 then
		y = getPlayerScreenTop(playerNum) + (getPlayerScreenHeight(playerNum) - height) / 2
		y = y + 200;
	end
	if x == 0 then
		x = getPlayerScreenLeft(playerNum) + (getPlayerScreenWidth(playerNum) - width) / 2
	end
	local o = ISCollapsableWindowJoypad.new(self, x, y, width, height)
	o.character = character
	o.item = item
	o.title = getText("IGUI_MakeUp");
	o.desc = character:getDescriptor();
	o.playerNum = playerNum
	o.needsUpdateAvatar = true
	o.needsUpdateLayout = true
	o:setResizable(false)
	return o
end

function ISMakeUpUI.OnPlayerDeath(playerObj)
	local ui = ISMakeUpUI.windows[playerObj:getPlayerNum()+1]
	if ui then
		ui:removeFromUIManager()
		ISMakeUpUI.windows[playerObj:getPlayerNum()+1] = nil
	end
end

Events.OnPlayerDeath.Add(ISMakeUpUI.OnPlayerDeath)


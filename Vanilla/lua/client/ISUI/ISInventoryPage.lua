--***********************************************************
--**               LEMMY/ROBERT JOHNSON                    **
--***********************************************************

require "ISUI/ISPanel"
require "ISUI/ISButton"
require "ISUI/ISInventoryPane"
require "ISUI/ISResizeWidget"
require "ISUI/ISMouseDrag"
require "ISUI/ISLayoutManager"

require "Definitions/ContainerButtonIcons"

require "defines"

ISInventoryPage = ISPanel:derive("ISInventoryPage");

ISInventoryPage.bagSoundDelay = 5
ISInventoryPage.bagSoundTime = 0

--************************************************************************--
--** ISInventoryPage:initialise
--**
--************************************************************************--

function ISInventoryPage:initialise()
	ISPanel.initialise(self);
end

function ISInventoryPage:onChangeFilter(selected)

end

function ISInventoryPage:titleBarHeight(selected)
	return math.max(16, self.titleFontHgt + 1)
end

--************************************************************************--
--** ISPanel:instantiate
--**
--************************************************************************--
function ISInventoryPage:createChildren()

    self.minimumHeight = 100;
    -- This must be 32 pixels wider than InventoryPane's minimum width
    -- TODO: parent widgets respect min size of child widgets.
    self.minimumWidth = 256+32;

    local titleBarHeight = self:titleBarHeight()
    local closeBtnSize = titleBarHeight
    local lootButtonHeight = titleBarHeight

    self.render3DItemRot = 0;

    local panel2 = ISInventoryPane:new(0, titleBarHeight, self.width-32, self.height-titleBarHeight-9, self.inventory, self.zoom);
    panel2.anchorBottom = true;
	panel2.anchorRight = true;
    panel2.player = self.player;
	panel2:initialise();

    panel2:setMode("details");

    panel2.inventoryPage = self;
	self:addChild(panel2);

	self.inventoryPane = panel2;

	-- FIXME: It is wrong to have both self.transferAll and ISInventoryPage.transferAll (button and function with the same name).

    local textWid = getTextManager():MeasureStringX(UIFont.Small, getText("IGUI_invpage_Transfer_all"))
    local weightWid = getTextManager():MeasureStringX(UIFont.Small, "99.99 / 99")
    self.transferAll = ISButton:new(self.width - 3 - closeBtnSize - math.max(90, weightWid + 10) - textWid, 0, textWid, lootButtonHeight, getText("IGUI_invpage_Transfer_all"), self, ISInventoryPage.transferAll);
    self.transferAll:initialise();
    self.transferAll.borderColor.a = 0.0;
    self.transferAll.backgroundColor.a = 0.0;
    self.transferAll.backgroundColorMouseOver.a = 0.7;
    self:addChild(self.transferAll);
    self.transferAll:setVisible(false);

    if not self.onCharacter then
        self.lootAll = ISButton:new(3 + closeBtnSize * 2 + 1, 0, 50, lootButtonHeight, getText("IGUI_invpage_Loot_all"), self, ISInventoryPage.lootAll);
        self.lootAll:initialise();
        self.lootAll.borderColor.a = 0.0;
        self.lootAll.backgroundColor.a = 0.0;
        self.lootAll.backgroundColorMouseOver.a = 0.7;
        self:addChild(self.lootAll);
        self.lootAll:setVisible(false);
        
        self.removeAll = ISButton:new(self.lootAll:getRight() + 16, 0, 50, lootButtonHeight, getText("IGUI_invpage_RemoveAll"), self, ISInventoryPage.removeAll);
        self.removeAll:initialise();
        self.removeAll.borderColor.a = 0.0;
        self.removeAll.backgroundColor.a = 0.0;
        self.removeAll.backgroundColorMouseOver.a = 0.7;
        self:addChild(self.removeAll);
        self.removeAll:setVisible(false);

        self.toggleStove = ISButton:new(self.lootAll:getRight() + 16, 0, 50, lootButtonHeight, getText("ContextMenu_Turn_On"), self, ISInventoryPage.toggleStove);
        self.toggleStove:initialise();
        self.toggleStove.borderColor.a = 0.0;
        self.toggleStove.backgroundColor.a = 0.0;
        self.toggleStove.backgroundColorMouseOver.a = 0.7;
        self:addChild(self.toggleStove);
        self.toggleStove:setVisible(false);
    end

    --	local filter = ISRadioOption:new(0, 15, 150, 150, "Filter", self, ISInventoryPage.onChangeFilter);
--	filter:addOption("All");
--	filter:addOption("Weapons/Ammo");
--	filter:addOption("Food/Cooking");
--	filter:addOption("Clothing");
--	filter:addOption("Building");
--	self:addChild(filter);

    -- Do corner x + y widget
	local resizeWidget = ISResizeWidget:new(self.width-10, self.height-10, 10, 10, self);
	resizeWidget:initialise();
	self:addChild(resizeWidget);

	self.resizeWidget = resizeWidget;

    -- Do bottom y widget
    resizeWidget = ISResizeWidget:new(0, self.height-10, self.width-10, 10, self, true);
    resizeWidget.anchorLeft = true;
    resizeWidget.anchorRight = true;
    resizeWidget:initialise();
    self:addChild(resizeWidget);

    self.resizeWidget2 = resizeWidget;


    self.closeButton = ISButton:new(3, 0, closeBtnSize, closeBtnSize, "", self, ISInventoryPage.close);
    self.closeButton:initialise();
    self.closeButton.borderColor.a = 0.0;
    self.closeButton.backgroundColor.a = 0;
    self.closeButton.backgroundColorMouseOver.a = 0;
    self.closeButton:setImage(self.closebutton);
    self:addChild(self.closeButton);
    if getCore():getGameMode() == "Tutorial" then
        self.closeButton:setVisible(false)
    end

    self.infoButton = ISButton:new(self.closeButton:getRight() + 1, 0, closeBtnSize, closeBtnSize, "", self, ISInventoryPage.onInfo);
    self.infoButton:initialise();
    self.infoButton.borderColor.a = 0.0;
    self.infoButton.backgroundColor.a = 0.0;
    self.infoButton.backgroundColorMouseOver.a = 0.7;
    self.infoButton:setImage(self.infoBtn);
    self:addChild(self.infoButton);
    self.infoButton:setVisible(false);

    --  --print("adding pin button");
    self.pinButton = ISButton:new(self.width - closeBtnSize - 3, 0, closeBtnSize, closeBtnSize, "", self, ISInventoryPage.setPinned);
    self.pinButton.anchorRight = true;
    self.pinButton.anchorLeft = false;
  --  --print("initialising pin button");
    self.pinButton:initialise();
    self.pinButton.borderColor.a = 0.0;
    self.pinButton.backgroundColor.a = 0;
    self.pinButton.backgroundColorMouseOver.a = 0;
   -- --print("setting pin button image");
    self.pinButton:setImage(self.pinbutton);
  --  --print("adding pin button to panel");
    self:addChild(self.pinButton);
  --  --print("set pin button invisible.");
    self.pinButton:setVisible(false);

   -- --print("adding collapse button");
    self.collapseButton = ISButton:new(self.pinButton:getX(), 0, closeBtnSize, closeBtnSize, "", self, ISInventoryPage.collapse);
    self.collapseButton.anchorRight = true;
    self.collapseButton.anchorLeft = false;
    self.collapseButton:initialise();
    self.collapseButton.borderColor.a = 0.0;
    self.collapseButton.backgroundColor.a = 0;
    self.collapseButton.backgroundColorMouseOver.a = 0;
    self.collapseButton:setImage(self.collapsebutton);
    self:addChild(self.collapseButton);
    if getCore():getGameMode() == "Tutorial" then
        self.collapseButton:setVisible(false);
    end
	-- load the current weight of the container
	self.totalWeight =  ISInventoryPage.loadWeight(self.inventory);

    self:refreshBackpacks();

    self:collapse();
end

function ISInventoryPage:refreshWeight()
	return;
--~ 	for i,v in ipairs(self.backpacks) do
--~ 		v:setOverlayText(ISInventoryPage.loadWeight(v.inventory) .. "/" .. v.capacity);
--~ 	end
end

function ISInventoryPage:lootAll()
    self.inventoryPane:lootAll();
end

function ISInventoryPage:transferAll()
    self.inventoryPane:transferAll();
end

local TurnOnOff = {
	ClothingDryer = {
		isPowered = function(object)
			return object:getContainer() and object:getContainer():isPowered() or false
		end,
		isActivated = function(object)
			return object:isActivated()
		end,
		toggle = function(object)
			local args = { x = object:getX(), y = object:getY(), z = object:getZ() }
			sendClientCommand('clothingDryer', 'toggle', args)
		end
	},
	ClothingWasher = {
		isPowered = function(object)
			if object:getWaterAmount() <= 0 then return false end
			return object:getContainer() and object:getContainer():isPowered() or false
		end,
		isActivated = function(object)
			return object:isActivated()
		end,
		toggle = function(object)
			local args = { x = object:getX(), y = object:getY(), z = object:getZ() }
			sendClientCommand('clothingWasher', 'toggle', args)
		end
	},
	Stove = {
		isPowered = function(object)
			return object:getContainer() and object:getContainer():isPowered() or false
		end,
		isActivated = function(object)
			return object:Activated()
		end,
		toggle = function(object)
			object:Toggle()
		end
	}
}

function ISInventoryPage:toggleStove()
	if UIManager.getSpeedControls() and UIManager.getSpeedControls():getCurrentGameSpeed() == 0 then
		return
	end

	local object = self.inventoryPane.inventory:getParent()
	if not object then return end
	local className = object:getObjectName()
	TurnOnOff[className].toggle(object)
end

function ISInventoryPage:syncToggleStove()
	if self.onCharacter then return end
	local isVisible = self.toggleStove:getIsVisible()
	local shouldBeVisible = false
	local stove = nil
	if self.inventoryPane.inventory then
		stove = self.inventoryPane.inventory:getParent()
		if stove then
			local className = stove:getObjectName()
			if TurnOnOff[className] and TurnOnOff[className].isPowered(stove) then
				shouldBeVisible = true
			end
		end
	end
	local containerButton
	for _,cb in ipairs(self.backpacks) do
		if cb.inventory == self.inventoryPane.inventory then
			containerButton = cb
			break
		end
	end
	if not containerButton then
		shouldBeVisible = false
	end
	if isVisible ~= shouldBeVisible and getCore():getGameMode() ~= "Tutorial" then
		self.toggleStove:setVisible(shouldBeVisible)
	end
	if shouldBeVisible then
		local className = stove:getObjectName()
		if TurnOnOff[className].isActivated(stove) then
			self.toggleStove:setTitle(getText("ContextMenu_Turn_Off"))
		else
			self.toggleStove:setTitle(getText("ContextMenu_Turn_On"))
		end
	end
end

function ISInventoryPage:setInfo(text)
    self.infoButton:setVisible(true);
    self.infoText = text;
end

function ISInventoryPage:onInfo()
    if not self.infoRichText then
        self.infoRichText = ISModalRichText:new(getCore():getScreenWidth()/2-300,getCore():getScreenHeight()/2-100,600,200,self.infoText, false);
        self.infoRichText:initialise();
        self.infoRichText.backgroundColor = {r=0, g=0, b=0, a=0.9};
        self.infoRichText.chatText:paginate();
        self.infoRichText:setHeightToContents()
        self.infoRichText:setY(getCore():getScreenHeight() / 2-self.infoRichText:getHeight() / 2);
        self.infoRichText:setVisible(true);
        self.infoRichText:addToUIManager();
    elseif self.infoRichText:isReallyVisible() then
        self.infoRichText:removeFromUIManager()
    else
        self.infoRichText:setVisible(true)
        self.infoRichText:addToUIManager()
    end
--    self.infoRichText:paginate();
end

function ISInventoryPage:collapse()
    if ISMouseDrag.dragging and #ISMouseDrag.dragging > 0 then
        return;
    end
    self.pin = false;
    self.collapseButton:setVisible(false);
    self.pinButton:setVisible(true);
    self.pinButton:bringToTop();
end

function ISInventoryPage:setPinned()
    self.pin = true;
    self.collapseButton:setVisible(true);
    self.pinButton:setVisible(false);
    self.collapseButton:bringToTop();
end

function ISInventoryPage:isRemoveButtonVisible()
	if self.onCharacter then return false end
	if self.inventory:isEmpty() then return false end
	if isClient() and not getServerOptions():getBoolean("TrashDeleteAll") then return false end
	local obj = self.inventory:getParent()
	if not instanceof(obj, "IsoObject") then return false end
	local sprite = obj:getSprite()
	return sprite and sprite:getProperties() and sprite:getProperties():Is("IsTrashCan")
end

function ISInventoryPage:update()
	
    if self.coloredInv and (self.inventory ~= self.coloredInv or self.isCollapsed) then
        if self.coloredInv:getParent() then
            self.coloredInv:getParent():setHighlighted(false)
        end
        self.coloredInv = nil;
    end

    if not self.isCollapsed then
--        print(self.inventory:getParent());
        if self.inventory:getParent() and (instanceof(self.inventory:getParent(), "IsoObject") or instanceof(self.inventory:getParent(), "IsoDeadBody")) then
            self.inventory:getParent():setHighlighted(true, false);
            self.inventory:getParent():setHighlightColor(getCore():getObjectHighlitedColor());
--             self.inventory:getParent():setHighlightColor(ColorInfo.new(0.3,0.3,0.3,1));
            --            self.inventory:getParent():setBlink(true);
--            self.inventory:getParent():setCustomColor(0.98,0.56,0.11,1);
            self.coloredInv = self.inventory;
        end
	end
	
    if (ISMouseDrag.dragging ~= nil and #ISMouseDrag.dragging > 0) or self.pin then
        self.collapseCounter = 0;
        if isClient() and self.isCollapsed then
            self.inventoryPane.inventory:requestSync();
        end
        self.isCollapsed = false;
        self:clearMaxDrawHeight();
        self.collapseCounter = 0;
    end

    if not self.onCharacter then
        -- add "remove all" button for trash can/bins
        self.removeAll:setVisible(self:isRemoveButtonVisible())

        local playerObj = getSpecificPlayer(self.player)
        if self.lastDir ~= playerObj:getDir() then
            self.lastDir = playerObj:getDir()
            self:refreshBackpacks()
        elseif self.lastSquare ~= playerObj:getCurrentSquare() then
            self.lastSquare = playerObj:getCurrentSquare()
            self:refreshBackpacks()
        end

        -- If the currently-selected container is locked to the player, select another container.
        local object = self.inventory and self.inventory:getParent() or nil
        if #self.backpacks > 1 and instanceof(object, "IsoThumpable") and object:isLockedToCharacter(playerObj) then
            local currentIndex = self:getCurrentBackpackIndex()
            local unlockedIndex = self:prevUnlockedContainer(currentIndex, false)
            if unlockedIndex == -1 then
                unlockedIndex = self:nextUnlockedContainer(currentIndex, false)
            end
            if unlockedIndex ~= -1 then
                self:selectContainer(self.backpacks[unlockedIndex])
                if playerObj:getJoypadBind() ~= -1 then
                    self.backpackChoice = unlockedIndex
                end
            end
        end
    end

	self:syncToggleStove()
end

function ISInventoryPage:setBlinkingContainer(blinking, containerType)
	if blinking then
		self.blinkContainer = true;
		self.blinkContainerType = containerType;
	else
		self.blinkContainer = false;
		self.blinkContainerType = nil;
		for i,v in ipairs(self.backpacks) do
			if v.inventory == self.inventoryPane.inventory then
				v:setBackgroundRGBA(0.7, 0.7, 0.7, 1.0)
			else
				v:setBackgroundRGBA(0.0, 0.0, 0.0, 0.0)
			end
		end
	end
end

function ISInventoryPage:setForceSelectedContainer(container)
	self.forceSelectedContainer = container
	self.forceSelectedContainerTime = getTimestampMs() + 1000
end

--************************************************************************--
--** ISInventoryPage:prerender
--**
--************************************************************************--
function ISInventoryPage:prerender()

    if self.blinkContainer then
        if not self.blinkAlphaContainer then self.blinkAlphaContainer = 0.7; self.blinkAlphaIncreaseContainer = false; end
        if not self.blinkAlphaIncreaseContainer then
            self.blinkAlphaContainer = self.blinkAlphaContainer - 0.04 * (UIManager.getMillisSinceLastRender() / 33.3);
            if self.blinkAlphaContainer < 0.3 then
                self.blinkAlphaContainer = 0.3;
                self.blinkAlphaIncreaseContainer = true;
            end
        else
            self.blinkAlphaContainer = self.blinkAlphaContainer + 0.04 * (UIManager.getMillisSinceLastRender() / 33.3);
            if self.blinkAlphaContainer > 0.7 then
                self.blinkAlphaContainer = 0.7;
                self.blinkAlphaIncreaseContainer = false;
            end
        end
        for i,v in ipairs(self.backpacks) do
			if (self.blinkContainerType and v.inventory:getType() == self.blinkContainerType) or not self.blinkContainerType then
            	v:setBackgroundRGBA(1, 0, 0, self.blinkAlphaContainer);
			end
        end
	end

    local titleBarHeight = self:titleBarHeight()
    local height = self:getHeight();
    if self.isCollapsed then
        height = titleBarHeight;
    end

	self:drawRect(0, 0, self:getWidth(), height, self.backgroundColor.a, self.backgroundColor.r, self.backgroundColor.g, self.backgroundColor.b);

    if not self.blink then
        self:drawTextureScaled(self.titlebarbkg, 2, 1, self:getWidth() - 4, titleBarHeight - 2, 1, 1, 1, 1);
    else
        if not self.blinkAlpha then self.blinkAlpha = 1; end
        self:drawRect(2, 1, self:getWidth() - 4, 14, self.blinkAlpha, 1, 1, 1);
--        self:drawTextureScaled(self.titlebarbkg, 2, 1, self:getWidth() - 4, 14, self.blinkAlpha, 1, 1, 1);

        if not self.blinkAlphaIncrease then
            self.blinkAlpha = self.blinkAlpha - 0.1 * (UIManager.getMillisSinceLastRender() / 33.3);
            if self.blinkAlpha < 0 then
                self.blinkAlpha = 0;
                self.blinkAlphaIncrease = true;
            end
        else
            self.blinkAlpha = self.blinkAlpha + 0.1 * (UIManager.getMillisSinceLastRender() / 33.3);
            if self.blinkAlpha > 1 then
                self.blinkAlpha = 1;
                self.blinkAlphaIncrease = false;
            end
        end
    end
    self:drawRectBorder(0, 0, self:getWidth(), titleBarHeight, self.borderColor.a, self.borderColor.r, self.borderColor.g, self.borderColor.b);

    if not self.isCollapsed then
        -- Draw border for backpack area...
        self:drawRect(self:getWidth()-32, titleBarHeight, 32, height-titleBarHeight-7,  self.backgroundColor.a, self.backgroundColor.r, self.backgroundColor.g, self.backgroundColor.b);
    end

--~ 	if not self.title then
--~ 		self.title = getSpecificPlayer(self.player):getDescriptor():getForename().." "..getSpecificPlayer(self.player):getDescriptor():getSurname().."'s Inventory";
--~ 	end

    if self.title and self.onCharacter then
        self:drawText(self.title, self.infoButton:getRight() + 1, 0, 1,1,1,1);
    end

	-- load the current weight of the container
	self.totalWeight = ISInventoryPage.loadWeight(self.inventoryPane.inventory);

    local roundedWeight = round(self.totalWeight, 2)
	if self.capacity then
		if self.inventoryPane.inventory == getSpecificPlayer(self.player):getInventory() then
			self:drawTextRight(roundedWeight .. " / " .. getSpecificPlayer(self.player):getMaxWeight(), self.pinButton:getX(), 0, 1,1,1,1);
		else
			self:drawTextRight(roundedWeight .. " / " .. self.capacity, self.pinButton:getX(), 0, 1,1,1,1);
		end
	else
		self:drawTextRight(roundedWeight .. "", self.width - 20, 0, 1,1,1,1);
    end
    
	local weightWid = getTextManager():MeasureStringX(UIFont.Small, "99.99 / 99")
	weightWid = math.max(90, weightWid + 10)
    self.transferAll:setX(self.pinButton:getX() - weightWid - getTextManager():MeasureStringX(UIFont.Small, getText("IGUI_invpage_Transfer_all")));
    if not self.onCharacter or self.width < 370 then
        self.transferAll:setVisible(false)
    elseif not "Tutorial" == getCore():getGameMode() then
        self.transferAll:setVisible(true)
    end
    
    if self.title and not self.onCharacter then
        local fontHgt = getTextManager():getFontHeight(self.font)
        self:drawTextRight(self.title, self.width - 20 - weightWid, (titleBarHeight - fontHgt) / 2, 1,1,1,1);
    end

    -- self:drawRectBorder(self:getWidth()-32, 15, 32, self:getHeight()-16-6, self.borderColor.a, self.borderColor.r, self.borderColor.g, self.borderColor.b);
    self:setStencilRect(0,0,self.width+1, height);
    
    if ISInventoryPage.renderDirty then
        ISInventoryPage.renderDirty = false;
        ISInventoryPage.dirtyUI();
    end
end

function ISInventoryPage:close()
	ISPanel.close(self)
	if JoypadState.players[self.player+1] then
		setJoypadFocus(self.player, nil)
		local playerObj = getSpecificPlayer(self.player)
		playerObj:setBannedAttacking(false)
	end
end

function ISInventoryPage:onLoseJoypadFocus(joypadData)
    ISPanel.onLoseJoypadFocus(self, joypadData)

    self.inventoryPane.doController = false;
    local inv = getPlayerInventory(self.player);
	if not inv then
        return;
    end
    local loot = getPlayerLoot(self.player);
    if inv.joyfocus or loot.joyfocus then
      --  self.inventoryPane.doController = false;
        return;
    end

    if getFocusForPlayer(self.player) == nil then
        inv:setVisible(false);
        loot:setVisible(false);
        local playerObj = getSpecificPlayer(self.player)
        playerObj:setBannedAttacking(false)
        if playerObj:getVehicle() and playerObj:getVehicle():isDriver(playerObj) then
            getPlayerVehicleDashboard(self.player):addToUIManager()
        end
      --  self.inventoryPane.doController = false;
    end

end

function ISInventoryPage:onGainJoypadFocus(joypadData)
    ISPanel.onGainJoypadFocus(self, joypadData)

    local inv = getPlayerInventory(self.player);
    local loot = getPlayerLoot(self.player);
    inv:setVisible(true);
    loot:setVisible(true);
    getPlayerVehicleDashboard(self.player):removeFromUIManager()
    self.inventoryPane.doController = true;
end

function ISInventoryPage:getCurrentBackpackIndex()
    for index,backpack in ipairs(self.backpacks) do
        if backpack.inventory == self.inventory then
            return index
        end
    end
    return -1
end

function ISInventoryPage:prevUnlockedContainer(index, wrap)
    local playerObj = getSpecificPlayer(self.player)
    for i=index-1,1,-1 do
        local backpack = self.backpacks[i]
        local object = backpack.inventory:getParent()
        if not (instanceof(object, "IsoThumpable") and object:isLockedToCharacter(playerObj)) then
            return i
        end
    end
    return wrap and self:prevUnlockedContainer(#self.backpacks + 1, false) or -1
end

function ISInventoryPage:nextUnlockedContainer(index, wrap)
    if index < 0 then -- User clicked a container that isn't displayed
        return wrap and self:nextUnlockedContainer(0, false) or -1
    end
    local playerObj = getSpecificPlayer(self.player)
    for i=index+1,#self.backpacks do
        local backpack = self.backpacks[i]
        local object = backpack.inventory:getParent()
        if not (instanceof(object, "IsoThumpable") and object:isLockedToCharacter(playerObj)) then
            return i
        end
    end
    return wrap and self:nextUnlockedContainer(0, false) or -1
end

function ISInventoryPage:selectPrevContainer()
    local currentIndex = self:getCurrentBackpackIndex()
    local unlockedIndex = self:prevUnlockedContainer(currentIndex, true)
    if unlockedIndex == -1 then
        return
    end
    local playerObj = getSpecificPlayer(self.player)
    if playerObj and playerObj:getJoypadBind() ~= -1 then
        self.backpackChoice = unlockedIndex
    end
    self:selectContainer(self.backpacks[unlockedIndex])
end

function ISInventoryPage:selectNextContainer()
    local currentIndex = self:getCurrentBackpackIndex()
    local unlockedIndex = self:nextUnlockedContainer(currentIndex, true)
    if unlockedIndex == -1 then
        return
    end
    local playerObj = getSpecificPlayer(self.player)
    if playerObj and playerObj:getJoypadBind() ~= -1 then
        self.backpackChoice = unlockedIndex
    end
    self:selectContainer(self.backpacks[unlockedIndex])
end

function ISInventoryPage:onJoypadDown(button)
    ISContextMenu.globalPlayerContext = self.player;
    local playerObj = getSpecificPlayer(self.player)
    
    if button == Joypad.AButton then
        self.inventoryPane:doContextOnJoypadSelected();
    end

    if button == Joypad.BButton then
        if isPlayerDoingActionThatCanBeCancelled(playerObj) then
            stopDoingActionThatCanBeCancelled(playerObj)
            return
        end
        self.inventoryPane:doJoypadExpandCollapse()
    end
    if button == Joypad.XButton and not JoypadState.disableGrab then
        self.inventoryPane:doGrabOnJoypadSelected();
    end
    if button == Joypad.YButton and not JoypadState.disableYInventory then
        setJoypadFocus(self.player, nil);
    end

    -- 1: left button affects inventory, right button affects loot
    -- 2: both buttons affect same window
    -- 3: left + d-pad affects inventory, right + dpad affects loot
    local shoulderSwitch = getCore():getOptionShoulderButtonContainerSwitch()
    if button == Joypad.LBumper then
        if shoulderSwitch == 1 then
            getPlayerInventory(self.player):selectNextContainer()
        elseif shoulderSwitch == 2 then
            self:selectPrevContainer()
        elseif shoulderSwitch == 3 then
            setJoypadFocus(self.player, getPlayerInventory(self.player))
        end
    end
    if button == Joypad.RBumper then
        if shoulderSwitch == 1 then
            getPlayerLoot(self.player):selectNextContainer()
        elseif shoulderSwitch == 2 then
            self:selectNextContainer()
        elseif shoulderSwitch == 3 then
            setJoypadFocus(self.player, getPlayerLoot(self.player))
        end
    end
end

function ISInventoryPage:onJoypadDirUp(joypadData)
    local shoulderSwitch = getCore():getOptionShoulderButtonContainerSwitch()
    if shoulderSwitch == 3 then
        if isJoypadPressed(joypadData.id, Joypad.LBumper) then
            getPlayerInventory(self.player):selectPrevContainer()
            return
        end
        if isJoypadPressed(joypadData.id, Joypad.RBumper) then
            getPlayerLoot(self.player):selectPrevContainer()
            return
        end
    end
    self.inventoryPane.joyselection = self.inventoryPane.joyselection - 1;
    self:ensureVisible(self.inventoryPane.joyselection + 1)
end

function ISInventoryPage:onJoypadDirDown(joypadData)
    local shoulderSwitch = getCore():getOptionShoulderButtonContainerSwitch()
    if shoulderSwitch == 3 then
        if isJoypadPressed(joypadData.id, Joypad.LBumper) then
            getPlayerInventory(self.player):selectNextContainer()
            return
        end
        if isJoypadPressed(joypadData.id, Joypad.RBumper) then
            getPlayerLoot(self.player):selectNextContainer()
            return
        end
    end
    self.inventoryPane.joyselection = self.inventoryPane.joyselection + 1;
    self:ensureVisible(self.inventoryPane.joyselection + 1)
end

function ISInventoryPage:ensureVisible(index)
	local lb = self.inventoryPane
	-- Wrap index same as ISInventoryPane:renderdetails does
    if index < 1 then index = #lb.items end
    if index > #lb.items then index = 1 end
    local headerHgt = 17
    local y = headerHgt + lb.itemHgt * (index - 1)
    local height = lb.itemHgt
	if y < 0-lb:getYScroll() + headerHgt then
		lb:setYScroll(0 - y + headerHgt)
	elseif y + height > 0 - lb:getYScroll() + (lb.height - headerHgt) then
		lb:setYScroll(0 - (y + height - lb.height))
	end
end

function ISInventoryPage:onJoypadDirLeft()
    local inv = getPlayerInventory(self.player);
    local loot = getPlayerLoot(self.player);

    if self == loot then
        setJoypadFocus(self.player, inv);
    elseif self == inv then
        setJoypadFocus(self.player, loot);
    end
end

function ISInventoryPage:onJoypadDirRight()
    local inv = getPlayerInventory(self.player);
    local loot = getPlayerLoot(self.player);

    if self == loot then
        setJoypadFocus(self.player, inv);
    elseif self == inv then
        setJoypadFocus(self.player, loot);
    end
end



function ISInventoryPage:render()
	local titleBarHeight = self:titleBarHeight()
    local height = self:getHeight();
    if self.isCollapsed then
        height = titleBarHeight
    end
    -- Draw backpack border over backpacks....
    if not self.isCollapsed then
        self:drawRectBorder(self:getWidth()-32, titleBarHeight - 1, 32, height-titleBarHeight-7, self.borderColor.a, self.borderColor.r, self.borderColor.g, self.borderColor.b);
        --self:drawRect(0, 0, self.width-32, self.height, 1, 1, 1, 1);
        self:drawRectBorder(0, height-9, self:getWidth(), 9, self.borderColor.a, self.borderColor.r, self.borderColor.g, self.borderColor.b);
        self:drawTextureScaled(self.statusbarbkg, 2,  height-7, self:getWidth() - 4, 6, 1, 1, 1, 1);
        self:drawTexture(self.resizeimage, self:getWidth()-9, height-8, 1, 1, 1, 1);
    end

    self:clearStencilRect();
    self:drawRectBorder(0, 0, self:getWidth(), height, self.borderColor.a, self.borderColor.r, self.borderColor.g, self.borderColor.b);


    if self.joyfocus then
        self:drawRectBorder(0, 0, self:getWidth(), self:getHeight(), 0.4, 0.2, 1.0, 1.0);
        self:drawRectBorder(1, 1, self:getWidth()-2, self:getHeight()-2, 0.4, 0.2, 1.0, 1.0);
    end

    if self.render3DItems and #self.render3DItems > 0 then
        self:render3DItemPreview();
    end
end
function ISInventoryPage:selectContainer(button)
	local playerObj = getSpecificPlayer(self.player)
	if self.player == 0 and ISMouseDrag.dragging ~= nil then
		if getCore():getGameMode() ~= "Tutorial" then
            if self:canPutIn() then
                local doWalk = true
                local items = {}
                local dragging = ISInventoryPane.getActualItems(ISMouseDrag.dragging)
                for i,v in ipairs(dragging) do
                    local transfer = v:getContainer() and not button.inventory:isInside(v)
                    if v:isFavorite() and not button.inventory:isInCharacterInventory(playerObj) then
                        transfer = false
                    end
                    if not button.inventory:isItemAllowed(v) then
                        transfer = false
                    end
                    if transfer then
                        -- only walk for the first item
                        if doWalk then
                            if not luautils.walkToContainer(button.inventory, self.player) then
                                break
                            end
                            doWalk = false
                        end
                        table.insert(items, v)
                    end
                end
                self.inventoryPane:transferItemsByWeight(items, button.inventory)
                self.inventoryPane.selected = {};
                getPlayerLoot(self.player).inventoryPane.selected = {};
                getPlayerInventory(self.player).inventoryPane.selected = {};
            end
        end

        if ISMouseDrag.draggingFocus then
            ISMouseDrag.draggingFocus:onMouseUp(0,0);
            ISMouseDrag.draggingFocus = nil;
            ISMouseDrag.dragging = nil;
        end
		self:refreshWeight();
		return;
	end
	--

    if button.inventory ~= self.inventoryPane.lastinventory then
        if button.inventory:getOpenSound() then
            if ISInventoryPage.bagSoundTime + ISInventoryPage.bagSoundDelay < getTimestamp() then
                ISInventoryPage.bagSoundTime = getTimestamp()
                getSpecificPlayer(self.player):getEmitter():playSound(button.inventory:getOpenSound())
            end
        end

        if not button.inventory:getOpenSound() and self.inventoryPane.lastinventory:getCloseSound() then
            if ISInventoryPage.bagSoundTime + ISInventoryPage.bagSoundDelay < getTimestamp() then
                ISInventoryPage.bagSoundTime = getTimestamp()
                getSpecificPlayer(self.player):getEmitter():playSound(self.inventoryPane.lastinventory:getCloseSound())
            end
        end
    end

    self.inventoryPane.lastinventory = button.inventory;
    self.inventoryPane.inventory = button.inventory;
    self.inventoryPane.selected = {}
    if not button.inventory:isExplored() then
        if not isClient() then
			ItemPicker.fillContainer(button.inventory, playerObj);
        else
            button.inventory:requestServerItemsForContainer();
        end
        button.inventory:setExplored(true);
    end

	self.title = button.name;

	self.capacity = button.capacity;

    self:refreshBackpacks();
    --self:refreshBackpacks();
--~ 	self.inventoryPane.selected = {};
--~ 	getPlayerLoot(self.player).inventoryPane.selected = {};
--~ 	getPlayerInventory(self.player).inventoryPane.selected = {};
end

function ISInventoryPage:setNewContainer(inventory)
    self.inventoryPane.inventory = inventory;
    self.inventory = inventory;
    self.inventoryPane:refreshContainer();

    local playerObj = getSpecificPlayer(self.player)
    self.capacity = inventory:getEffectiveCapacity(playerObj);

    -- highlight the container if it is in the list
	for i,containerButton in ipairs(self.backpacks) do
		if containerButton.inventory == inventory then
			containerButton:setBackgroundRGBA(0.7, 0.7, 0.7, 1.0)
			self.title = containerButton.name;
		else
			containerButton:setBackgroundRGBA(0.0, 0.0, 0.0, 0.0)
		end
	end

	self:syncToggleStove()
end

function ISInventoryPage:selectButtonForContainer(container)
	if self.inventoryPane.inventory == container then
		return
	end
	for index,containerButton in ipairs(self.backpacks) do
		if containerButton.inventory == container then
			self:selectContainer(containerButton)
			local playerObj = getSpecificPlayer(self.player)
			if playerObj and playerObj:getJoypadBind() ~= -1 then
				self.backpackChoice = index
			end
			return
		end
	end
end

function ISInventoryPage.loadWeight(inv)
    if inv == nil then return 0; end;

	return inv:getCapacityWeight();
end

--************************************************************************--
--** ISButton:onMouseMove
--**
--************************************************************************--
function ISInventoryPage:onMouseMove(dx, dy)
	self.mouseOver = true;

--    print(self:getMouseX(), self:getMouseY(), self:getWidth(), self:getHeight())

	if self.moving then
		self:setX(self.x + dx);
		self:setY(self.y + dy);

    end

    if not isGamePaused() then
        if self.isCollapsed and self.player and getSpecificPlayer(self.player) and getSpecificPlayer(self.player):isAiming() then
            return
        end
    end

    local panCameraKey = getCore():getKey("PanCamera")
    if self.isCollapsed and panCameraKey ~= 0 and isKeyDown(panCameraKey) then
        return
    end

    if not isMouseButtonDown(0) and not isMouseButtonDown(1) and not isMouseButtonDown(2) then

        self.collapseCounter = 0;
        if self.isCollapsed and self:getMouseY() < self:titleBarHeight() then
           self.isCollapsed = false;
		   	if isClient() and not self.onCharacter then
				self.inventoryPane.inventory:requestSync();
			end
           self:clearMaxDrawHeight();
           self.collapseCounter = 0;
        end
    end
end

--************************************************************************--
--** ISButton:onMouseMoveOutside
--**
--************************************************************************--
function ISInventoryPage:onMouseMoveOutside(dx, dy)
	self.mouseOver = false;

	if self.moving then
		self:setX(self.x + dx);
		self:setY(self.y + dy);
    end

    if ISMouseDrag.dragging ~= true and not self.pin and (self:getMouseX() < 0 or self:getMouseY() < 0 or self:getMouseX() > self:getWidth() or self:getMouseY() > self:getHeight()) then
--        if ISMouseDrag.dragging and #ISMouseDrag.dragging == 1 then
--            local dragging = ISInventoryPane.getActualItems(ISMouseDrag.dragging)
--            for i,v in ipairs(dragging) do
--                self.render3DItem = v;
--                break;
--            end
--        else
--            self.render3DItem = nil;
--        end
        self.collapseCounter = self.collapseCounter + getGameTime():getMultiplier() / 0.8;
        local bDo = false;
        if ISMouseDrag.dragging == nil then
            bDo = true;
        else
            for i, k in ipairs(ISMouseDrag.dragging) do
               bDo = true;
               break;
            end
        end
        local playerObj = getSpecificPlayer(self.player)
        if playerObj and playerObj:isAiming() then
            self.collapseCounter = 1000
        end
        if ISMouseDrag.dragging and #ISMouseDrag.dragging > 0 then
            bDo = false;
        end
        if self.collapseCounter > 120 and not self.isCollapsed and bDo then

            self.isCollapsed = true;
            self:setMaxDrawHeight(self:titleBarHeight());

        end
    end
end

--************************************************************************--
--** ISButton:onMouseUp
--**
--************************************************************************--
function ISInventoryPage:onMouseUp(x, y)
	if not self:getIsVisible() then
		return;
	end
--~ 	print ("Mouse up on inventory page. Uhoh");

--	ISMouseDrag = {}
	self.moving = false;
	self:setCapture(false);
end

function ISInventoryPage:onMouseDown(x, y)

	if not self:getIsVisible() then
		return;
	end

	getSpecificPlayer(self.player):nullifyAiming();

	self.downX = self:getMouseX();
	self.downY = self:getMouseY();
	self.moving = true;
	self:setCapture(true);
end

function ISInventoryPage:onRightMouseDownOutside(x, y)
    if((self:getMouseX() < 0 or self:getMouseY() < 0 or self:getMouseX() > self:getWidth() or self:getMouseY() > self:getHeight()) and  not self.pin) then
        self.isCollapsed = true;
        self:setMaxDrawHeight(self:titleBarHeight());
    end
end
function ISInventoryPage:onMouseDownOutside(x, y)
    if((self:getMouseX() < 0 or self:getMouseY() < 0 or self:getMouseX() > self:getWidth() or self:getMouseY() > self:getHeight()) and  not self.pin) then
        self.isCollapsed = true;
        self:setMaxDrawHeight(self:titleBarHeight());
    end
end

function ISInventoryPage:onMouseUpOutside(x, y)
	if not self:getIsVisible() then
		return;
	end

--	ISMouseDrag = {}
	self.moving = false;
	self:setCapture(false);
end

function ISInventoryPage:isCycleContainerKeyDown()
	local keyName = getCore():getOptionCycleContainerKey()
	if keyName == "control" then
		return isCtrlKeyDown()
	end
	if keyName == "shift" then
		return isShiftKeyDown()
	end
	if keyName == "control+shift" then
		return isCtrlKeyDown() and isShiftKeyDown()
	end
	error "unknown cycle container key"
end

function ISInventoryPage:onMouseWheel(del)
	if self:getMouseX() < self:getWidth() - 32 and not self:isCycleContainerKeyDown() then
		return false
	end
	local currentIndex = self:getCurrentBackpackIndex()
	local unlockedIndex = -1
	if del < 0 then
		unlockedIndex = self:prevUnlockedContainer(currentIndex, true)
	else
		unlockedIndex = self:nextUnlockedContainer(currentIndex, true)
	end
	if unlockedIndex ~= -1 then
		self:selectContainer(self.backpacks[unlockedIndex])
		local playerObj = getSpecificPlayer(self.player)
		if playerObj and playerObj:getJoypadBind() ~= -1 then
			self.backpackChoice = unlockedIndex
		end
	end
	return true
end

ISInventoryPage.dirtyUI = function ()
   -- ISInventoryPage.playerInventory.inventoryPane:refreshContainer();
	for i=0, getNumActivePlayers() -1 do
		local pdata = getPlayerData(i)
		if pdata and pdata.playerInventory then
			pdata.playerInventory:refreshBackpacks()
			pdata.lootInventory:refreshBackpacks()
		end
	end
end

function ISInventoryPage:onBackpackMouseDown(button, x, y)
	ISMouseDrag = {}
	if not isKeyDown(getCore():getKey("Melee")) then
	    getSpecificPlayer(self.player):nullifyAiming();
    end
end

function ISInventoryPage:onBackpackRightMouseDown(x, y)
	local page = self.parent
	if not page.onCharacter then return end
	local container = self.inventory
	local item = container:getContainingItem()
	local context = ISContextMenu.get(page.player, getMouseX(), getMouseY())
	if item then
		context = ISInventoryPaneContextMenu.createMenu(page.player, true, {item}, getMouseX(), getMouseY())
		return
	end
	if context:isReallyVisible() then
		context:closeAll()
	end
end

local sqsContainers = {}
local sqsVehicles = {}

function ISInventoryPage:addContainerButton(container, texture, name, tooltip)
    local titleBarHeight = self:titleBarHeight()
	local playerObj = getSpecificPlayer(self.player)
	local c = #self.backpacks + 1
	local x = self.width - 32
	local y = ((c - 1) * 32) + titleBarHeight - 1
	local button
	if #self.buttonPool > 0 then
		button = table.remove(self.buttonPool, 1)
		button:setX(x)
		button:setY(y)
	else
		button = ISButton:new(x, y, 32, 32, "", self, ISInventoryPage.selectContainer, ISInventoryPage.onBackpackMouseDown, true)
		button.anchorLeft = false
		button.anchorTop = false
		button.anchorRight = true
		button.anchorBottom = false
		button:initialise()
		button:forceImageSize(30, 30)
	end
	button:setBackgroundRGBA(0.0, 0.0, 0.0, 0.0)
	button:setBackgroundColorMouseOverRGBA(0.3, 0.3, 0.3, 1.0)
	button:setBorderRGBA(0.7, 0.7, 0.7, 0.0)
	button:setTextureRGBA(1.0, 1.0, 1.0, 1.0)
	button.textureOverride = nil
	button.inventory = container
	button.onclick = ISInventoryPage.selectContainer
	button.onmousedown = ISInventoryPage.onBackpackMouseDown
	button.onRightMouseDown = ISInventoryPage.onBackpackRightMouseDown
	button:setOnMouseOverFunction(ISInventoryPage.onMouseOverButton)
	button:setOnMouseOutFunction(ISInventoryPage.onMouseOutButton)
	button:setSound("activate", nil)
	button.capacity = container:getEffectiveCapacity(playerObj)
	if instanceof(texture, "Texture") then
		button:setImage(texture)
    else
		if ContainerButtonIcons[container:getType()] ~= nil then
			button:setImage(ContainerButtonIcons[container:getType()])
		else
			button:setImage(self.conDefault)
		end
	end
	button.name = name
	button.tooltip = tooltip
	self:addChild(button)
	self.backpacks[c] = button
	return button
end

function ISInventoryPage:checkExplored(container, playerObj)
	if container:isExplored() then
		return
	end
	if isClient() then
		container:requestServerItemsForContainer()
	else
		ItemPicker.fillContainer(container, playerObj)
	end
	container:setExplored(true)
end

function ISInventoryPage.GetFloorContainer(playerNum)
	if ISInventoryPage.floorContainer == nil then
		ISInventoryPage.floorContainer = {}
	end
	if ISInventoryPage.floorContainer[playerNum+1] == nil then
		ISInventoryPage.floorContainer[playerNum+1] = ItemContainer.new("floor", nil, nil, 10, 10)
		ISInventoryPage.floorContainer[playerNum+1]:setExplored(true)
	end
	return ISInventoryPage.floorContainer[playerNum+1]
end

function ISInventoryPage:refreshBackpacks()
	self.buttonPool = self.buttonPool or {}
	for i,v in ipairs(self.backpacks) do
		self:removeChild(v)
		table.insert(self.buttonPool, i, v)
	end

	local floorContainer = ISInventoryPage.GetFloorContainer(self.player)

	self.inventoryPane.lastinventory = self.inventoryPane.inventory

	self.inventoryPane:hideButtons()

	local oldNumBackpacks = #self.backpacks
	table.wipe(self.backpacks)

	local containerButton = nil

	local playerObj = getSpecificPlayer(self.player)

	triggerEvent("OnRefreshInventoryWindowContainers", self, "begin")

	if self.onCharacter then
		local name = getText("IGUI_InventoryName", playerObj:getDescriptor():getForename(), playerObj:getDescriptor():getSurname())
		containerButton = self:addContainerButton(playerObj:getInventory(), self.invbasic, name, nil)
		containerButton.capacity = self.inventory:getMaxWeight()
		if not self.capacity then
			self.capacity = containerButton.capacity
		end
		local it = playerObj:getInventory():getItems()
		for i = 0, it:size()-1 do
			local item = it:get(i)
			if item:getCategory() == "Container" and playerObj:isEquipped(item) or item:getType() == "KeyRing" then
				-- found a container, so create a button for it...
				containerButton = self:addContainerButton(item:getInventory(), item:getTex(), item:getName(), item:getName())
				if(item:getVisual() and item:getClothingItem()) then
					local tint = item:getVisual():getTint(item:getClothingItem());
					containerButton:setTextureRGBA(tint:getRedFloat(), tint:getGreenFloat(), tint:getBlueFloat(), 1.0);
				end
			end
		end
	elseif playerObj:getVehicle() then
		local vehicle = playerObj:getVehicle()
		for partIndex=1,vehicle:getPartCount() do
			local vehiclePart = vehicle:getPartByIndex(partIndex-1)
			if vehiclePart:getItemContainer() and vehicle:canAccessContainer(partIndex-1, playerObj) then
				local tooltip = getText("IGUI_VehiclePart" .. vehiclePart:getItemContainer():getType())
				containerButton = self:addContainerButton(vehiclePart:getItemContainer(), nil, tooltip, nil)
				self:checkExplored(containerButton.inventory, playerObj)
			end
		end
	else
		local cx = playerObj:getX()
		local cy = playerObj:getY()
		local cz = playerObj:getZ()

		-- Do floor
		local container = floorContainer
		container:removeItemsFromProcessItems()
		container:clear()

		local sqs = sqsContainers
		table.wipe(sqs)

		local dir = playerObj:getDir()
		local lookSquare = nil
		if self.lookDir ~= dir then
			self.lookDir = dir
			local dx,dy = 0,0
			if dir == IsoDirections.NW or dir == IsoDirections.W or dir == IsoDirections.SW then
				dx = -1
			end
			if dir == IsoDirections.NE or dir == IsoDirections.E or dir == IsoDirections.SE then
				dx = 1
			end
			if dir == IsoDirections.NW or dir == IsoDirections.N or dir == IsoDirections.NE then
				dy = -1
			end
			if dir == IsoDirections.SW or dir == IsoDirections.S or dir == IsoDirections.SE then
				dy = 1
			end
			lookSquare = getCell():getGridSquare(cx + dx, cy + dy, cz)
		end

		local vehicleContainers = sqsVehicles
		table.wipe(vehicleContainers)

		for dy=-1,1 do
			for dx=-1,1 do
				local square = getCell():getGridSquare(cx + dx, cy + dy, cz)
				if square then
					table.insert(sqs, square)
				end
			end
		end

		for _,gs in ipairs(sqs) do
			-- stop grabbing thru walls...
			local currentSq = playerObj:getCurrentSquare()
			if gs ~= currentSq and currentSq and currentSq:isBlockedTo(gs) then
				gs = nil
			end

			-- don't show containers in safehouse if you're not allowed
			if gs and isClient() and SafeHouse.isSafeHouse(gs, playerObj:getUsername(), true) and not getServerOptions():getBoolean("SafehouseAllowLoot") then
				gs = nil
			end

			if gs ~= nil then
				local numButtons = #self.backpacks

				local wobs = gs:getWorldObjects()
				for i = 0, wobs:size()-1 do
					local o = wobs:get(i)
					-- FIXME: An item can be in only one container in coop the item won't be displayed for every player.
					floorContainer:AddItem(o:getItem())
					if o:getItem() and o:getItem():getCategory() == "Container" then
						local item = o:getItem()
						containerButton = self:addContainerButton(item:getInventory(), item:getTex(), item:getName(), nil)
						if item:getVisual() and item:getClothingItem() then
							local tint = item:getVisual():getTint(item:getClothingItem());
							containerButton:setTextureRGBA(tint:getRedFloat(), tint:getGreenFloat(), tint:getBlueFloat(), 1.0);
						end
					end
				end

				local sobs = gs:getStaticMovingObjects()
				for i = 0, sobs:size()-1 do
					local so = sobs:get(i)
					if so:getContainer() ~= nil then
						local title = getTextOrNull("IGUI_ContainerTitle_" .. so:getContainer():getType()) or ""
						containerButton = self:addContainerButton(so:getContainer(), nil, title, nil)
						self:checkExplored(containerButton.inventory, playerObj)
					end
				end

				local obs = gs:getObjects()
				for i = 0, obs:size()-1 do
					local o = obs:get(i)
					for containerIndex = 1,o:getContainerCount() do
						local container = o:getContainerByIndex(containerIndex-1)
						local title = getTextOrNull("IGUI_ContainerTitle_" .. container:getType()) or ""
						containerButton = self:addContainerButton(container, nil, title, nil)
						if instanceof(o, "IsoThumpable") and o:isLockedToCharacter(playerObj) then
							containerButton.onclick = nil
							containerButton.onmousedown = nil
							containerButton:setOnMouseOverFunction(nil)
							containerButton:setOnMouseOutFunction(nil)
							containerButton.textureOverride = getTexture("media/ui/lock.png")
						end

						if instanceof(o, "IsoThumpable") and o:isLockedByPadlock() and playerObj:getInventory():haveThisKeyId(o:getKeyId()) then
							containerButton.textureOverride = getTexture("media/ui/lockOpen.png")
						end

						self:checkExplored(containerButton.inventory, playerObj)
					end
				end

				local vehicle = gs:getVehicleContainer()
				if vehicle and not vehicleContainers[vehicle] then
					vehicleContainers[vehicle] = true
					for partIndex=1,vehicle:getPartCount() do
						local vehiclePart = vehicle:getPartByIndex(partIndex-1)
						if vehiclePart:getItemContainer() and vehicle:canAccessContainer(partIndex-1, playerObj) then
							local tooltip = getText("IGUI_VehiclePart" .. vehiclePart:getItemContainer():getType())
							containerButton = self:addContainerButton(vehiclePart:getItemContainer(), nil, tooltip, nil)
							self:checkExplored(containerButton.inventory, playerObj)
						end
					end
				end

				if (numButtons < #self.backpacks) and (gs == lookSquare) then
					self.inventoryPane.inventory = self.backpacks[numButtons + 1].inventory
				end
			end
		end

		triggerEvent("OnRefreshInventoryWindowContainers", self, "beforeFloor")

		local title = getTextOrNull("IGUI_ContainerTitle_floor") or ""
		containerButton = self:addContainerButton(floorContainer, ContainerButtonIcons.floor, title, nil)
		containerButton.capacity = floorContainer:getMaxWeight()
	end

	triggerEvent("OnRefreshInventoryWindowContainers", self, "buttonsAdded")

	local found = false
	local foundIndex = -1
	for index,containerButton in ipairs(self.backpacks) do
		if containerButton.inventory == self.inventoryPane.inventory then
			foundIndex = index
			found = true
			break
		end
	end

	self.inventoryPane.inventory = self.inventoryPane.lastinventory
	self.inventory = self.inventoryPane.inventory
	if self.backpackChoice ~= nil and playerObj:getJoypadBind() ~= -1 then
		if not self.onCharacter and oldNumBackpacks == 1 and #self.backpacks > 1 then
			self.backpackChoice = 1
		end
		if self.backpackChoice > #self.backpacks then
			self.backpackChoice = 1
		end
		if self.backpacks[self.backpackChoice] ~= nil then
			self.inventoryPane.inventory = self.backpacks[self.backpackChoice].inventory
			self.capacity = self.backpacks[self.backpackChoice].capacity
		end
	else
		if not self.onCharacter and oldNumBackpacks == 1 and #self.backpacks > 1 then
			self.inventoryPane.inventory = self.backpacks[1].inventory
			self.capacity = self.backpacks[1].capacity
		elseif found then
			self.inventoryPane.inventory = self.backpacks[foundIndex].inventory
			self.capacity = self.backpacks[foundIndex].capacity
		elseif not found and #self.backpacks > 0 then
			if self.backpacks[1] and self.backpacks[1].inventory then
				self.inventoryPane.inventory = self.backpacks[1].inventory
				self.capacity = self.backpacks[1].capacity
			end
		elseif self.inventoryPane.lastinventory ~= nil then
			self.inventoryPane.inventory = self.inventoryPane.lastinventory
		end
	end

	-- ISInventoryTransferAction sometimes turns the player to face a container.
	-- Which container is selected changes as the player changes direction.
	-- Although ISInventoryTransferAction forces a container to be selected,
	-- sometimes the action completes before the player finishes turning.
	if self.forceSelectedContainer then
		if self.forceSelectedContainerTime > getTimestampMs() then
			for _,containerButton in ipairs(self.backpacks) do
				if containerButton.inventory == self.forceSelectedContainer then
					self.inventoryPane.inventory = containerButton.inventory
					self.capacity = containerButton.capacity
					break
				end
			end
		else
			self.forceSelectedContainer = nil
		end
	end

	if isClient() and (not self.isCollapsed) and (self.inventoryPane.inventory ~= self.inventoryPane.lastinventory) then
		self.inventoryPane.inventory:requestSync()
	end

	self.inventoryPane:bringToTop()
	self.resizeWidget2:bringToTop()
	self.resizeWidget:bringToTop()

	self.inventory = self.inventoryPane.inventory

	self.title = nil
	for k,containerButton in ipairs(self.backpacks) do
		if containerButton.inventory == self.inventory then
            self.selectedButton = containerButton;
			containerButton:setBackgroundRGBA(0.7, 0.7, 0.7, 1.0)
			self.title = containerButton.name
		else
			containerButton:setBackgroundRGBA(0.0, 0.0, 0.0, 0.0)
		end
	end

	if self.inventoryPane ~= nil then
		self.inventoryPane:refreshContainer()
	end

	self:refreshWeight()

	self:syncToggleStove()

	triggerEvent("OnRefreshInventoryWindowContainers", self, "end")
end

--************************************************************************--
--** ISInventoryPage:new
--**
--************************************************************************--
function ISInventoryPage:new (x, y, width, height, inventory, onCharacter, zoom)
	local o = {}
	--o.data = {}
	o = ISPanel:new(x, y, width, height);
    setmetatable(o, self)
    self.__index = self
	o.x = x;
	o.y = y;
    o.anchorLeft = true;
    o.anchorRight = true;
    o.anchorTop = true;
    o.anchorBottom = true;
	o.borderColor = {r=0.4, g=0.4, b=0.4, a=1};
	o.backgroundColor = {r=0, g=0, b=0, a=0.8};
	o.width = width;
	o.height = height;
	o.anchorLeft = true;
    o.backpackChoice = 1;
    o.zoom = zoom;
    o.isCollapsed = true;
    if o.zoom == nil then o.zoom = 1; end

	o.inventory = inventory;
    o.onCharacter = onCharacter;
    o.titlebarbkg = getTexture("media/ui/Panel_TitleBar.png");
    o.infoBtn = getTexture("media/ui/Panel_info_button.png");
    o.statusbarbkg = getTexture("media/ui/Panel_StatusBar.png");
    o.resizeimage = getTexture("media/ui/Panel_StatusBar_Resize.png");
    o.invbasic = getTexture("media/ui/Icon_InventoryBasic.png");
    o.closebutton = getTexture("media/ui/Dialog_Titlebar_CloseIcon.png");
    o.collapsebutton = getTexture("media/ui/Panel_Icon_Collapse.png");
    o.pinbutton = getTexture("media/ui/Panel_Icon_Pin.png");

    o.conDefault = getTexture("media/ui/Container_Shelf.png");
    o.highlightColors = {r=0.98,g=0.56,b=0.11};

    o.containerIconMaps = ContainerButtonIcons

    o.pin = true;
    o.isCollapsed = false;
    o.backpacks = {}
    o.collapseCounter = 0;
	o.title = nil;
	o.titleFont = UIFont.Small
	o.titleFontHgt = getTextManager():getFontHeight(o.titleFont)
   return o
end

function ISInventoryPage:onMouseOverButton(button,x,y)
	self.mouseOverButton = button;
end

function ISInventoryPage:onMouseOutButton(button,x,y)
	self.mouseOverButton = nil;
end

--[[
function ISInventoryPage:canPutIn(doMain)
    if not self.mouseOverButton or self.mouseOverButton.inventory == nil then
        return false;
    end
    if self.mouseOverButton.inventory:getType() == "floor" then
       return true;
    end

	-- if we're not over a container
	if not self.mouseOverButton then
		return true;
	end
	-- we count the total weight of our container
	local totalWeight = ISInventoryPage.loadWeight(self.mouseOverButton.inventory);
--~ 	self.mouseOverButton.inventory:getWeight();
	local inventoryItem = nil;
	for i,v in ipairs(ISMouseDrag.dragging) do
		if instanceof(v, "InventoryItem") then

            if self.mouseOverButton.inventory == v:getContainer() then
                return true;
            end

            if not self.mouseOverButton.inventory:isItemAllowed(v) then
                return false;
            end

                -- you can't draw the container in himself
--~ 			print(self.mouseOverButton.inventory);
--~ 			print(v);

			if not inventoryItem then
				inventoryItem = v;
				totalWeight = totalWeight + v:getActualWeight();
			elseif inventoryItem ~= v then
				totalWeight = totalWeight + v:getActualWeight();
			end
--~ 			totalWeight = totalWeight + v:getActualWeight();
		else
			for i2,v2 in ipairs(v.items) do
--~ 				print("notinstanceitem");
--~ 				print(v2);
--~ 				print(self.mouseOverButton.inventory);
				-- you can't draw the container in himself
				if (self.mouseOverButton.inventory:isInside(v2)) then
					return false;
                end

                if self.mouseOverButton.inventory == v2:getContainer() then
                    return true;
                end

                if not self.mouseOverButton.inventory:isItemAllowed(v2) then
                    return false;
                end

                -- first is a dummy
				if not inventoryItem then
					inventoryItem = v2;
					totalWeight = totalWeight + v2:getActualWeight();
				elseif inventoryItem ~= v2 then
					totalWeight = totalWeight + v2:getActualWeight();
				end
			end
		end
--~ 		print(#v.items .. " " .. totalWeight);
	end

		return true;

end
--]]

function ISInventoryPage:canPutIn()
    local playerObj = getSpecificPlayer(self.player)
    local container = self.mouseOverButton and self.mouseOverButton.inventory or nil
    if not container then
        return false
    end
    local items = {}
    local minWeight = 100000
    local dragging = ISInventoryPane.getActualItems(ISMouseDrag.dragging)
    for i,item in ipairs(dragging) do
        local itemOK = true
        if item:isFavorite() and not container:isInCharacterInventory(playerObj) then
            itemOK = false
        end
        if container:isInside(item) then
            itemOK = false
        end
        if container:getType() == "floor" and item:getWorldItem() then
            itemOK = false
        end
        if item:getContainer() == container then
            itemOK = false
        end
        if not container:isItemAllowed(item) then
            itemOK = false
        end
        if itemOK then
            table.insert(items, item)
        end
        if item:getUnequippedWeight() < minWeight then
            minWeight = item:getUnequippedWeight()
        end
    end
    if #items == 1 then
        return container:hasRoomFor(playerObj, items[1])
    elseif #items > 0 then
        return container:hasRoomFor(playerObj, minWeight)
    end
    return false
end

function ISInventoryPage:RestoreLayout(name, layout)
    ISLayoutManager.DefaultRestoreWindow(self, layout)
    if layout.pin == 'true' then
        self:setPinned()
    end
    self.inventoryPane:RestoreLayout(name, layout)
end

function ISInventoryPage:SaveLayout(name, layout)
    ISLayoutManager.DefaultSaveWindow(self, layout)
    if self.pin then layout.pin = 'true' else layout.pin = 'false' end
    self.inventoryPane:SaveLayout(name, layout)
end

ISInventoryPage.onKeyPressed = function(key)
	if key == getCore():getKey("Toggle Inventory") and getSpecificPlayer(0) and getGameSpeed() > 0 and getPlayerInventory(0) and getCore():getGameMode() ~= "Tutorial" then
        getPlayerInventory(0):setVisible(not getPlayerInventory(0):getIsVisible());
        getPlayerLoot(0):setVisible(getPlayerInventory(0):getIsVisible());
    end
end

ISInventoryPage.toggleInventory = function()
	if ISInventoryPage.playerInventory:getIsVisible() then
		ISInventoryPage.playerInventory:setVisible(false);
	else
		ISInventoryPage.playerInventory:setVisible(true);
	end
end

ISInventoryPage.onInventoryFontChanged = function()
    for i=1,getNumActivePlayers() do
        local pdata = getPlayerData(i-1)
        if pdata then
            pdata.playerInventory.inventoryPane:onInventoryFontChanged()
            pdata.lootInventory.inventoryPane:onInventoryFontChanged()
        end
    end
end

-- Called when an object with a container is added/removed from the world.
-- Added this to handle campfire containers.
ISInventoryPage.OnContainerUpdate = function(object)
    ISInventoryPage.renderDirty = true
end

ISInventoryPage.ongamestart = function()
    ISInventoryPage.renderDirty = true;
end

function ISInventoryPage:removeAll()
	self.inventoryPane:removeAll(self.player);
end

function ISInventoryPage:render3DItemPreview()
    if isKeyDown(getCore():getKey("Rotate building")) then
        if not self.render3DItemRot then
            self.render3DItemRot = 0;
        end
        local rot = self.render3DItemRot;
        if isKeyDown(Keyboard.KEY_LSHIFT) then
            rot = rot -10;
        else
            rot = rot + 10;
        end
        if rot < 0 then
            rot = 360;
        end
        if rot > 360 then
            rot = 0;
        end
        self.render3DItemRot = rot;
    end
    local playerObj = getSpecificPlayer(self.player)
    --        print(self.player, getMouseX())
    local worldX = screenToIsoX(self.player, getMouseX(), getMouseY(), playerObj:getZ())
    local worldY = screenToIsoY(self.player, getMouseX(), getMouseY(), playerObj:getZ())
    local sq = getSquare(worldX, worldY, playerObj:getZ());
    if not sq then
        return;
    end
    self.render3DItemXOffset = worldX - sq:getX();
    self.render3DItemYOffset = worldY - sq:getY();
    self.render3DItemZOffset = 0;
    -- check if we have a surface, so we can do a z offset to make items goes on this surface
    for i=0,sq:getObjects():size()-1 do
        local object = sq:getObjects():get(i);
        if object:getProperties():getSurface() and object:getProperties():getSurface() > 0 then
            -- the surface is in pixel, set for the 1X texture, so we *2 (192 pixels is 1X texture height)
            self.render3DItemZOffset = (object:getProperties():getSurface() / 192) * 2;
            break;
        end
    end
    self.selectedSqDrop = sq;
    if self.render3DItems then
        for i,v in ipairs(self.render3DItems) do
            Render3DItem(v, sq, worldX, worldY, self.render3DItemZOffset, self.render3DItemRot);
        end
    end
    --        print("gonna try to render ", self.render3DItem, worldX, playerObj:getX())
--    Render3DItem(self.render3DItem, sq, worldX, worldY, self.render3DItemZOffset, self.render3DItemRot);
end

Events.OnKeyPressed.Add(ISInventoryPage.onKeyPressed);
Events.OnContainerUpdate.Add(ISInventoryPage.OnContainerUpdate)

--Events.OnCreateUI.Add(testInventory);

Events.OnGameStart.Add(ISInventoryPage.ongamestart);

--***********************************************************
--**                    ROBERT JOHNSON                     **
--***********************************************************

ISEquippedItem = ISPanel:derive("ISEquippedItem");
ISEquippedItem.text = nil;

local FONT_HGT_SMALL = getTextManager():getFontHeight(UIFont.Small)
local FONT_HGT_MEDIUM = getTextManager():getFontHeight(UIFont.Medium)
local FONT_HGT_LARGE = getTextManager():getFontHeight(UIFont.Large)

function ISEquippedItem:prerender()
--	self:drawTexture(self.HandSecondaryTexture, -1, 50, 1, 1, 1, 1);

    if self.invBtn == nil then
        return;
    end
	if self.inventory ~= nil and self.inventory:getIsVisible() then
		self.invBtn:setImage(self.inventoryTextureOn);
	else
		self.invBtn:setImage(self.inventoryTexture);
    end
    if getPlayerCraftingUI(0) and getPlayerCraftingUI(0):getIsVisible() then
        self.craftingBtn:setImage(self.craftingIconOn);
    else
        self.craftingBtn:setImage(self.craftingIcon);
    end
    if getPlayerInfoPanel(0) and getPlayerInfoPanel(0):getIsVisible() then
        self.healthBtn:setImage(self.heartIconOn);
    else
        self.healthBtn:setImage(self.heartIcon);
    end

    if self.movableBtn:isMouseOver() then
        self.movableTooltip:setVisible(false);
        self.movableBtn:setVisible(false);
        self.movablePopup:setVisible(true);
    elseif self.movablePopup:isMouseOver() then
        --
    elseif getCell():getDrag(0) and getCell():getDrag(0).isMoveableCursor then
        if getCell():getDrag(0):getMoveableMode() == "pickup" then
            self.movableBtn:setImage(self.movableIconPickup);
        elseif getCell():getDrag(0):getMoveableMode() == "place" then
            self.movableBtn:setImage(self.movableIconPlace);
        elseif getCell():getDrag(0):getMoveableMode() == "rotate" then
            self.movableBtn:setImage(self.movableIconRotate);
        elseif getCell():getDrag(0):getMoveableMode() == "scrap" then
            self.movableBtn:setImage(self.movableIconScrap);
        end
        self.movableTooltip:setVisible(true);
        self.movableBtn:setVisible(true);
        self.movablePopup:setVisible(false);
    else
        self.movableTooltip:setVisible(false);
        self.movableBtn:setImage(self.movableIcon);
        self.movableBtn:setVisible(true);
        self.movablePopup:setVisible(false);
    end

    ----
    if self.searchBtn then
        if ISSearchManager.players[self.chr] and ISSearchManager.players[self.chr].isSearchMode then
            self.searchBtn:setImage(self.searchIconOn);
        else
            self.searchBtn:setImage(self.searchIconOff);
        end;
    end;
    ----

    if self.mapPopup then
        -- The check for ISWorldMap_instance:isVisible() is to prevent the popup becoming visible again
        -- after clicking it to display the world map.  UIManager.render() happens after UIManager.update().
        -- The world map is displayed by calling UIElement:addToUIManager(), but that doesn't take effect
        -- until the *next* UIManager.update() call.  So WorldMap is visible but not yet in UIManager's
        -- list of top-level widgets.
        if self.mapBtn:isMouseOver() and (not ISWorldMap_instance or not ISWorldMap_instance:isVisible()) then
            self.mapPopup:setVisible(true)
        elseif self.mapPopup:isMouseOver() then
            --
        else
            self.mapPopup:setVisible(false)
        end
    end

    if self.debugBtn then
        if ISDebugMenu.instance then
            self.debugBtn:setImage(self.debugIconOn);
        else
            self.debugBtn:setImage(self.debugIcon);
        end
    end
    
    if self.clientBtn then
        if ISUserPanelUI.instance then
            self.clientBtn:setImage(self.clientIconOn);
        else
            self.clientBtn:setImage(self.clientIcon);
        end
    end
    
    if self.adminBtn then
        self.adminBtn:setVisible(self.chr:getAccessLevel() ~= "" and self.chr:getAccessLevel() ~= "None")
        if ISAdminPanelUI.instance then
            self.adminBtn:setImage(self.adminIconOn);
        else
            self.adminBtn:setImage(self.adminIcon);
        end
    end

    if "Tutorial" == getCore():getGameMode() then
        self.movableBtn:setVisible(false);
        self.invBtn:setVisible(false);
        self.craftingBtn:setVisible(false);
        self.healthBtn:setY(self.invBtn:getY());
    end

    local playerID = self.chr:getPlayerNum()
    if getCell():getDrag(playerID) and getCell():getDrag(playerID).isPlace3DCursor then -- draw some help text when placing a 3D item in the world
        local cursor = getCell():getDrag(playerID);
        cursor:drawPrompt(playerID, self);
    end

	if isClient() then

		local maxY = getCore():getScreenHeight();
		local maxX = getCore():getScreenWidth();
		local versionInfo = getGameVersionInfo()

		--self:drawTextRight(versionInfo.buildDate.."  "..versionInfo.buildTime, maxX-50, maxY-80, 0.8, 0.8, 0.8, 1, UIFont.NewMedium);
		self:drawTextRight(versionInfo.svnRevision.." - "..versionInfo.version, maxX-50, maxY-60, 0.8, 0.8, 0.8, 1, UIFont.NewMedium);

		local ping = getPing()

		if ping.enabled or self.chr:isShowMPInfos() then
			local lastPing = tonumber(ping.lastPing)
			local r = 0.8
			local g = 0.8
			local b = 0.8
			if lastPing > 300 then
				r = 1
				g = 0.5
				b = 0.2
			elseif lastPing < 0 then
				lastPing = 0
				r = 0.5
				g = 0.5
				b = 0.5
			end
			self:drawTextRight("Ping "..tostring(lastPing).." ms", maxX-50, maxY-40 , r, g, b, 1, UIFont.NewMedium);
		end
	end

    -- Display the various admin power you have
    if isClient() and self.chr:getAccessLevel() and self.chr:getAccessLevel() ~= "None" then
        local y = 20;
        self:drawText("ADMIN POWER:", 80,y,1,1,1,1,UIFont.NewLarge);
        y = y + 18;
        local onOff = "Off";
        if self.chr:isInvincible() then
            onOff = "On";
        end
        self:drawText("GodMode: " .. onOff, 80,y,1,1,1,1,UIFont.NewMedium);
        y = y + 15;
        onOff = "Off";
        if self.chr:isInvisible() then
            onOff = "On";
        end
        self:drawText("Invisible: " .. onOff, 80,y,1,1,1,1,UIFont.NewMedium);
        y = y + 15;
        onOff = "Off";
        if self.chr:isNoClip() then
            onOff = "On";
        end
        self:drawText("NoClip: " .. onOff, 80,y,1,1,1,1,UIFont.NewMedium);
        y = y + 15;
        onOff = "Off";
        if self.chr:isCanSeeAll() then
            onOff = "On";
        end
        self:drawText("CanSeeEveryone: " .. onOff, 80,y,1,1,1,1,UIFont.NewMedium);
        y = y + 15;
        onOff = "Off";
        if self.chr:isNetworkTeleportEnabled() then
            onOff = "On";
        end
        self:drawText("isNetworkTeleportEnabled: " .. onOff, 80,y,1,1,1,1,UIFont.NewMedium);
        y = y + 15;
        onOff = "Off";
        if self.chr:isSeeEveryone() then
            onOff = "On";
        end
        self:drawText("isSeeEveryone: " .. onOff, 80,y,1,1,1,1,UIFont.NewMedium);
	y = y + 15;
        onOff = "Off";
        if self.chr:isCheatPlayerSeeEveryone() then
            onOff = "On";
        end
        self:drawText("isCheatPlayerSeeEveryone: " .. onOff, 80,y,1,1,1,1,UIFont.NewMedium);
	y = y + 15;
        onOff = "Off";
        if self.chr:isZombiesDontAttack() then
            onOff = "On";
        end
        self:drawText("zombiesDontAttack: " .. onOff, 80,y,1,1,1,1,UIFont.NewMedium);
	y = y + 15;
        onOff = "Off";
        if self.chr:zombiesSwitchOwnershipEachUpdate() then
            onOff = "On";
        end
        self:drawText("zombiesSwitchOwnershipEachUpdate: " .. onOff, 80,y,1,1,1,1,UIFont.NewMedium);
	y = y + 15;
        onOff = "Off";
        if self.chr:isCanHearAll() then
            onOff = "On";
        end
        self:drawText("CanHearEveryone: " .. onOff, 80,y,1,1,1,1,UIFont.NewMedium);

    end
end

function ISEquippedItem:getDraggedEquippableItem()
    if "Tutorial" == getCore():getGameMode() then
        return
    end
    local dragging = ISInventoryPane.getActualItems(ISMouseDrag.dragging);
    for _,item in ipairs(dragging) do
        -- TODO: ISInventoryPaneContextMenu.doEquipOption() checks hand injuries.
        if item:getScriptItem():getReplaceWhenUnequip() then
            -- not allowed
        elseif item:IsWeapon() then
            if item:getCondition() > 0 then
                return item;
            end
        elseif item:IsFood() then
            -- not allowed
        elseif item:IsClothing() then
            -- not allowed
        else
            return item
        end
    end
    return nil;
end

function ISEquippedItem:getDraggedEquippableItems()
    local primaryItem = nil;
    local secondaryItem = nil;
    local mouseY = self:getMouseY()
    if (mouseY >= self.mainHand:getBottom() - 4) and (mouseY < self.offHand:getY() + 4) then
        local item = self:getDraggedEquippableItem()
        if item and (item:isTwoHandWeapon() or item:isRequiresEquippedBothHands()) then
            primaryItem = item;
            secondaryItem = item;
        end
    elseif mouseY < self.mainHand:getBottom() then
        local item = self:getDraggedEquippableItem()
        if item then
            primaryItem = item;
            if item:isRequiresEquippedBothHands() then
                secondaryItem = item;
            end
        end
    elseif mouseY < self.offHand:getBottom() then
        local item = self:getDraggedEquippableItem()
        if item then
            secondaryItem = item;
            if item:isRequiresEquippedBothHands() then
                primaryItem = item;
            end
        end
    end
    return primaryItem,secondaryItem
end

function ISEquippedItem:render()
	local primaryItem = self.chr:getPrimaryHandItem();
	local secondaryItem = self.chr:getSecondaryHandItem();

	if ISMouseDrag.dragging and self:isMouseOver() then
		local item1,item2 = self:getDraggedEquippableItems()
		if item1 and secondaryItem and (primaryItem == secondaryItem or item1 == secondaryItem) then
			secondaryItem = nil;
		end
		if item2 and primaryItem and (primaryItem == secondaryItem or primaryItem == item2) then
			primaryItem = nil;
		end
		primaryItem = item1 or primaryItem
		secondaryItem = item2 or secondaryItem
	end

	if primaryItem then
		local item = primaryItem
		if item:getTex() and item:getTex():getWidth() <= 32 and item:getTex():getHeight() <= 32 then
			self:drawTextureScaled(item:getTex(),(self.handMainTexture:getWidth() / 2) - (item:getTex():getWidth() / 2),(self.handMainTexture:getHeight() / 2) - (item:getTex():getHeight() / 2),item:getTex():getWidth(), item:getTex():getHeight(), item:getA(),item:getR(),item:getG(),item:getB());
		else
			self:drawTextureScaledAspect(item:getTex(), self.handMainTexture:getWidth() / 2 - 16, self.handMainTexture:getHeight() / 2 - 16, 32, 32, item:getA(),item:getR(),item:getG(),item:getB());
		end
        -- This handles the condition star. commented out in case it's to be readded later
--		if instanceof(item,"HandWeapon") then
--			local n = math.floor(((item:getCondition() / item:getConditionMax()) * 5));

--			if(item:getCondition() > 0 and n == 0) then
--				n = 1;
--			end

--			self:drawTexture(getTexture("media/ui/QualityStar_" .. n .. ".png"),5,10,1,1,1,1);
--		end
	end
	if secondaryItem then
		local item = secondaryItem
		local width = 24
		local height = 24
		if item:getTex() and item:getTex():getWidth() <= width and item:getTex():getHeight() <= height then
			width = self.HandSecondaryTexture:getWidthOrig()
			height = self.HandSecondaryTexture:getHeightOrig()
			self:drawTextureScaled(item:getTex(), (width - item:getTex():getWidth()) / 2, 50 + (height - item:getTex():getHeight()) / 2, item:getTex():getWidth(), item:getTex():getHeight(), item:getA(),item:getR(),item:getG(),item:getB());
		else
			self:drawTextureScaledAspect(item:getTex(), 0 + (self.HandSecondaryTexture:getWidthOrig() - width) / 2, 50 + (self.HandSecondaryTexture:getHeightOrig() - height) / 2, width, height, item:getA(),item:getR(),item:getG(),item:getB());
		end
	end

    if self.chr:getBodyDamage():getHealth() ~= self.previousHealth then
        if self.previousHealth > self.chr:getBodyDamage():getHealth() then
            self.healthIconOscillatorLevel = 1;
        end
        self.previousHealth = self.chr:getBodyDamage():getHealth()
    end

    --code for the oscillation of the heart icon when attacked
    if not self.healthBtn then
        -- Player 1/2/3
    elseif self.healthIconOscillatorLevel > 0.01 then
        local fpsFrac = PerformanceSettings.getLockFPS() / 30.0;
        self.healthIconOscillatorLevel = self.healthIconOscillatorLevel * self.healthIconOscillatorDecelerator
        self.healthIconOscillatorLevel = self.healthIconOscillatorLevel - (self.healthIconOscillatorLevel * (1 - self.healthIconOscillatorDecelerator) / fpsFrac)
        self.healthIconOscillatorStep = self.healthIconOscillatorStep + self.healthIconOscillatorRate / fpsFrac
        self.healthIconOscillator = math.sin(self.healthIconOscillatorStep)
        self.healthBtn:setX(self.healthIconOscillator * self.healthIconOscillatorLevel * self.healthIconOscillatorScalar)
    elseif self.healthIconOscillatorLevel < 0.01 then
        self.healthIconOscillatorLevel = 0
        self.healthBtn:setX(self.healthIconOscillator * self.healthIconOscillatorLevel * self.healthIconOscillatorScalar)
    end

    if self.invBtn == nil then
        return;
    end

	if ISEquippedItem.text then
		self:drawText(ISEquippedItem.text, 50,0,1,1,1,1,UIFont.Medium);
    end

    self:checkToolTip();

    self:renderFPS();
end

function ISEquippedItem:renderFPS()
    if not ISFPS or not ISFPS.start then return end
    local second = getTimestamp()
    if (ISFPS.lastSec ~= second) or (ISEquippedItem.text == nil) then
        ISEquippedItem.text = "FPS: " .. getAverageFPS()
        ISFPS.lastSec = second
    end
end

function ISEquippedItem:onOptionMouseDown(button, x, y)
	if button.internal == "INVENTORY" then
        self.inventory:setVisible(not self.inventory:getIsVisible());
        self.loot:setVisible(self.inventory:getIsVisible());

    elseif button.internal == "HEALTH" then
	--	xpUpdate.toggleCharacterInfo(self.chr);
		self.infopanel:toggleView(getText("IGUI_XP_Health"));
    elseif button.internal == "CRAFTING" then
        ISCraftingUI.toggleCraftingUI();
    elseif button.internal == "MAP" then
        ISWorldMap.ToggleWorldMap(self.chr:getPlayerNum())
    elseif button.internal == "DEBUG" and (getCore():getDebug() or ISDebugMenu.forceEnable) then
        if ISDebugMenu.instance then
            ISDebugMenu.instance:close();
        else
            ISDebugMenu.OnOpenPanel();
        end
    elseif button.internal == "USERPANEL" then
        if ISUserPanelUI.instance then
            ISUserPanelUI.instance:close()
        else
            local modal = ISUserPanelUI:new(200, 200, 350, 250, self.chr)
            modal:initialise();
            modal:addToUIManager();
        end
    elseif button.internal == "ADMINPANEL" then
        if ISAdminPanelUI.instance then
            ISAdminPanelUI.instance:close()
        else
            local modal = ISAdminPanelUI:new(200, 200, 350, 400)
            modal:initialise();
            modal:addToUIManager();
        end
    elseif button.internal == "SEARCH" then
        ISSearchWindow.players[self.chr]:setVisible(not ISSearchWindow.players[self.chr]:getIsVisible());
        --ISSearchManager.players[self.chr].isSearchMode = not ISSearchManager.players[self.chr].isSearchMode;
    end

end

local activateCounter = 0;
local activateTicks = 120; -- the actual value * 2;
local lastId = 0;
function ISEquippedItem:checkToolTip()
    local mx, my = getMouseX(), getMouseY();
    local mouseOverID, tooltiptext = -1, nil;
    if self.mouseOverList ~= nil then
        for k,v in ipairs(self.mouseOverList) do
            if self:checkBounds(v.object, mx, my) then
                mouseOverID = k;
                tooltiptext = v.displayString;
            end
        end
    end
    local activate = false;
    if mouseOverID > 0 then
        if activateCounter < activateTicks then
            activateCounter = activateCounter+1;
        end
        if activateCounter > activateTicks/2 then
            activate = true;
        elseif mouseOverID~=lastId then
            --reset counting
            activateCounter = 0;
        end
        lastId = mouseOverID;
    elseif activateCounter > 0 then
        activateCounter = activateCounter-1;
    end
    self:doToolTip(activate, tooltiptext);
end

function ISEquippedItem:doToolTip( _state, _text )
    if _state then
        if self.toolTip == nil then
            self.toolTip = ISToolTip:new(item);
            self.toolTip:initialise();
            self.toolTip:addToUIManager();
            self.toolTip:setOwner(self);
            self.toolTip:setWidth(100);
            self.toolTip.description = _text;
            self.toolTip:doLayout();
            self.toolTip:setVisible(true);
        else
            if self.toolTip then
                if self.toolTip:getIsVisible()==false then
                    self.toolTip:setVisible(true);
                    self.toolTip:addToUIManager();
                end
                if self.toolTip.description ~= _text then
                    self.toolTip.description = _text;
                    self.toolTip:doLayout();
                end

                self.toolTip:bringToTop();
            end
        end
    else
        if self.toolTip and self.toolTip:getIsVisible() then
            self.toolTip:removeFromUIManager();
            self.toolTip:setVisible(false);
        end
    end
end

function ISEquippedItem:checkBounds( _boundsItem, _x, _y)
    if _boundsItem~=nil and _x>=_boundsItem:getX() and _x<=_boundsItem:getX()+_boundsItem:getWidth() and _y>=_boundsItem:getY() and _y<=_boundsItem:getY()+_boundsItem:getHeight() then
        return true;
    end
    return false;
end

function ISEquippedItem:new (x, y, width, height, chr)
	local o = {}
	--o.data = {}
	o = ISPanel:new(x, y, width, height);
	setmetatable(o, self)
    self.__index = self
	o.x = x;
	o.y = y;
    o.borderColor = {r=0.4, g=0.4, b=0.4, a=1};
    o.backgroundColor = {r=0, g=0, b=0, a=0.5};
	o.width = width;
	o.height = height;
    o.inventory = getPlayerInventory(chr:getPlayerNum());
    o.loot = getPlayerLoot(chr:getPlayerNum());
    o.infopanel = getPlayerInfoPanel(chr:getPlayerNum());
    o.anchorLeft = true;
    o.chr = chr;
	o.anchorRight = false;
	o.anchorTop = true;
	o.anchorBottom = false;
	o.handMainTexture = getTexture("media/ui/HandMain2_Off.png");
	o.HandSecondaryTexture = getTexture("media/ui/HandSecondary2_Off.png");
	o.inventoryTexture = getTexture("media/ui/Inventory2_Off.png");
	o.inventoryTextureOn = getTexture("media/ui/Inventory2_On.png");
    o.craftingIcon = getTexture("media/ui/Carpentry_Off.png");
    o.craftingIconOn = getTexture("media/ui/Carpentry_On.png");
	o.heartIcon = getTexture("media/ui/Heart2_Off.png");
	o.heartIconOn = getTexture("media/ui/Heart2_On.png");
    o.movableIcon = getTexture("media/ui/Furniture_Off2.png");
    o.movableIconPickup = getTexture("media/ui/Furniture_Pickup.png");
    o.movableIconPlace = getTexture("media/ui/Furniture_Place.png");
    o.movableIconRotate = getTexture("media/ui/Furniture_Rotate.png");
    o.movableIconScrap = getTexture("media/ui/Furniture_Disassemble.png");
    o.mapIconOff = getTexture("media/textures/worldMap/Map_Off.png");
    o.mapIconOn = getTexture("media/textures/worldMap/Map_On.png");
    o.debugIcon = getTexture("media/ui/Debug_Icon_Off.png");
    o.debugIconOn = getTexture("media/ui/Debug_Icon_On.png");
    o.clientIcon = getTexture("media/ui/Client_Icon_Off.png");
    o.clientIconOn = getTexture("media/ui/Client_Icon_On.png");
    o.adminIcon = getTexture("media/ui/Admin_Icon.png");
    o.adminIconOn = getTexture("media/ui/Admin_Icon_On.png");
    o.searchIconOn = getTexture("media/ui/Search_Icon_On.png");
    o.searchIconOff = getTexture("media/ui/Search_Icon_Off.png");
    o.healthIconOscillatorLevel = 0.0;
    o.healthIconOscillator = 0.0;
    o.healthIconOscillatorDecelerator = 0.96;
    o.healthIconOscillatorRate = 0.8;
    o.healthIconOscillatorScalar = 15.6;
    o.healthIconOscillatorStartLevel = 1.0;
    o.healthIconOscillatorStep = 0.0;
    o.previousHealth = 0.0;
    ISEquippedItem.instance = o;
	return o;
end

local function createFakeObject(_x,_y,_w,_h)
    local self = {};
    self.x = _x;
    self.y = _y;
    self.w = _w;
    self.h = _h;
    function self:getX() return self.x end
    function self:getY() return self.y end
    function self:getWidth() return self.w end
    function self:getHeight() return self.h end
    return self;
end

function ISEquippedItem:addMouseOverToolTipItem( _object, _displayString )
    if self.mouseOverList == nil then
        self.mouseOverList = {};
    end
    if _object~=nil and _object.getX and _object.getY and _object.getWidth and _object.getHeight then
        table.insert(self.mouseOverList, { object = _object, displayString =  _displayString } );
    end
end

function ISEquippedItem:initialise()

	ISPanel.initialise(self);

    if self.chr:getPlayerNum() == 0 then
        self:addMouseOverToolTipItem(createFakeObject(11,11,44,44), getText("IGUI_PrimaryTooltip") );
        self:addMouseOverToolTipItem(createFakeObject(15,60,33,33), getText("IGUI_SecondaryTooltip") );
        -- inv btn
        self.invBtn = ISButton:new(0, 90, self.inventoryTexture:getWidthOrig(), self.inventoryTexture:getHeightOrig(), "", self, ISEquippedItem.onOptionMouseDown);
        self.invBtn:setImage(self.inventoryTexture);
        self.invBtn.internal = "INVENTORY";
        self.invBtn:initialise();
        self.invBtn:instantiate();
        self.invBtn:setDisplayBackground(false);

        self.invBtn.borderColor = {r=1, g=1, b=1, a=0.1};
        self.invBtn:ignoreWidthChange();
        self.invBtn:ignoreHeightChange();
        self:addChild(self.invBtn);
        self:addMouseOverToolTipItem(self.invBtn, getText("IGUI_InventoryTooltip") );
        -- health btn
        self.healthBtn = ISButton:new(0, self.invBtn:getY() + self.inventoryTexture:getHeightOrig() + 5, self.heartIcon:getWidthOrig(), self.heartIcon:getHeightOrig(), "", self, ISEquippedItem.onOptionMouseDown);
        self.healthBtn:setImage(self.heartIcon);
        self.healthBtn.internal = "HEALTH";
        self.healthBtn:initialise();
        self.healthBtn:instantiate();
        self.healthBtn:setDisplayBackground(false);

        self.healthBtn.borderColor = {r=1, g=1, b=1, a=0.1};
        self.healthBtn:ignoreWidthChange();
        self.healthBtn:ignoreHeightChange();
        self:addChild(self.healthBtn);
        self:addMouseOverToolTipItem(self.healthBtn, getText("IGUI_HealthTooltip") );

        self.craftingBtn = ISButton:new(0, self.healthBtn:getY() + self.heartIcon:getHeightOrig() + 5, self.craftingIcon:getWidthOrig(), self.craftingIcon:getHeightOrig(), "", self, ISEquippedItem.onOptionMouseDown);
        self.craftingBtn:setImage(self.craftingIcon);
        self.craftingBtn.internal = "CRAFTING";
        self.craftingBtn:initialise();
        self.craftingBtn:instantiate();
        self.craftingBtn:setDisplayBackground(false);

        self.craftingBtn.borderColor = {r=1, g=1, b=1, a=0.1};
        self.craftingBtn:ignoreWidthChange();
        self.craftingBtn:ignoreHeightChange();
        self:addChild(self.craftingBtn);
        self:addMouseOverToolTipItem(self.craftingBtn, getText("IGUI_CraftingTooltip") );

        self.movableBtn = ISButton:new(0, self.craftingBtn:getY() + self.craftingIcon:getHeightOrig() + 5, self.movableIcon:getWidthOrig(), self.movableIcon:getHeightOrig(), "", self, ISEquippedItem.onOptionMouseDown);
        self.movableBtn:setImage(self.movableIcon);
        self.movableBtn.internal = "MOVABLE";
        self.movableBtn:initialise();
        self.movableBtn:instantiate();
        self.movableBtn:setDisplayBackground(false);

        self.movableBtn.borderColor = {r=1, g=1, b=1, a=0.1};
        self.movableBtn:ignoreWidthChange();
        self.movableBtn:ignoreHeightChange();

        self.movableTooltip = ISMoveablesIconToolTip:new (8+(self.movableIcon:getWidthOrig()/2), self.craftingBtn:getY() + self.craftingIcon:getHeightOrig() + 5 + 3, 120, self.movableIcon:getHeightOrig()-6,self.movableIcon:getWidthOrig()/2);

        local texWid = self.movableIconPickup:getWidthOrig()
        local texHgt = self.movableIconPickup:getHeightOrig()
        self.movablePopup = ISMoveablesIconPopup:new(10 + self.movableBtn:getX(), 10 + self.movableBtn:getY(), texWid * 5, texHgt)
        self.movablePopup.owner = self
        self.movablePopup:addToUIManager()
        self.movablePopup:setVisible(false)

        self:addChild(self.movableTooltip);
        self:addChild(self.movableBtn);
        self:addMouseOverToolTipItem(self.movableBtn, getText("IGUI_MovableTooltip") );

        local y = self.movableBtn:getBottom() + 5

        ----
        self.searchBtn = ISButton:new(0, y, self.searchIconOff:getWidthOrig(), self.searchIconOff:getHeightOrig(), "", self, ISEquippedItem.onOptionMouseDown);
        self.searchBtn:setImage(self.searchIconOff);
        self.searchBtn.internal = "SEARCH";
        self.searchBtn:initialise();
        self.searchBtn:instantiate();
        self.searchBtn:setDisplayBackground(false);

        self.searchBtn.borderColor = {r=1, g=1, b=1, a=0.1};
        self.searchBtn:ignoreWidthChange();
        self.searchBtn:ignoreHeightChange();
        self:addChild(self.searchBtn);
        self:addMouseOverToolTipItem(self.searchBtn, getText("UI_investigate_area_window_title") );

        y = self.searchBtn:getY() + self.searchIconOff:getHeightOrig() + 5
        ----

        if ISWorldMap.IsAllowed() then
            self.mapBtn = ISButton:new(0, y, self.mapIconOff:getWidthOrig(), self.mapIconOff:getHeightOrig(), "", self, ISEquippedItem.onOptionMouseDown);
            self.mapBtn:setImage(self.mapIconOff);
            self.mapBtn.internal = "MAP";
            self.mapBtn:initialise();
            self.mapBtn:instantiate();
            self.mapBtn:setDisplayBackground(false);
            self.mapBtn:ignoreWidthChange();
            self.mapBtn:ignoreHeightChange();
            self:addChild(self.mapBtn);

            if ISMiniMap.IsAllowed() then
                self.mapPopup = ISMapPopup:new(10 + self.mapBtn:getX(), 10 + self.mapBtn:getY(), self.mapBtn.width * 2, self.mapBtn.height)
                self.mapPopup.owner = self
                self.mapPopup:addToUIManager()
                self.mapPopup:setVisible(false)
            end

            y = self.mapBtn:getBottom() + 5
        end

        if getCore():getDebug() or (ISDebugMenu.forceEnable and not isClient()) then
            local texWid = self.debugIcon:getWidthOrig()
            local texHgt = self.debugIcon:getHeightOrig()
            self.debugBtn = ISButton:new(5, y, texWid, texHgt, "", self, ISEquippedItem.onOptionMouseDown);
            self.debugBtn:setImage(self.debugIcon);
            self.debugBtn.internal = "DEBUG";
            self.debugBtn:initialise();
            self.debugBtn:instantiate();
            self.debugBtn:setDisplayBackground(false);

            self.debugBtn.borderColor = {r=1, g=1, b=1, a=0.1};
            self.debugBtn:ignoreWidthChange();
            self.debugBtn:ignoreHeightChange();

            self:addChild(self.debugBtn);

            self:setHeight(self.debugBtn:getBottom())
            y = self.debugBtn:getY() + self.debugIcon:getHeightOrig() + 10
        elseif self.mapBtn then
            self:setHeight(self.mapBtn:getBottom());
        elseif self.searchBtn then
            self:setHeight(self.searchBtn:getBottom());
        else
            self:setHeight(self.movableBtn:getBottom());
        end;
        
        if isClient() then
            local texWid = self.clientIcon:getWidthOrig()
            local texHgt = self.clientIcon:getHeightOrig()
            self.clientBtn = ISButton:new(5, y, texWid, texHgt, "", self, ISEquippedItem.onOptionMouseDown);
            self.clientBtn:setImage(self.clientIcon);
            self.clientBtn.internal = "USERPANEL";
            self.clientBtn:initialise();
            self.clientBtn:instantiate();
            self.clientBtn:setDisplayBackground(false);
    
            self.clientBtn.borderColor = {r=1, g=1, b=1, a=0.1};
            self.clientBtn:ignoreWidthChange();
            self.clientBtn:ignoreHeightChange();
    
            self:addChild(self.clientBtn);
    
            self:setHeight(self.clientBtn:getBottom())
            y = self.clientBtn:getY() + self.clientIcon:getHeightOrig() + 10
        
            self.adminBtn = ISButton:new(5, y, texWid, texHgt, "", self, ISEquippedItem.onOptionMouseDown);
            self.adminBtn:setImage(self.adminIcon);
            self.adminBtn.internal = "ADMINPANEL";
            self.adminBtn:initialise();
            self.adminBtn:instantiate();
            self.adminBtn:setDisplayBackground(false);

            self.adminBtn.borderColor = {r=1, g=1, b=1, a=0.1};
            self.adminBtn:ignoreWidthChange();
            self.adminBtn:ignoreHeightChange();

            self:addChild(self.adminBtn);

            self:setHeight(self.adminBtn:getBottom())
    
            self.adminBtn:setVisible(false);
        end
    end


	self.mainHand = ISImage:new((50 - 46) / 2, 0, self.handMainTexture:getWidthOrig(), self.handMainTexture:getHeight(), self.handMainTexture);
	self.mainHand:initialise();
	self.mainHand.onMouseUp = self.onMouseUpPrimary;
    self.mainHand.onRightMouseUp = self.rightClickPrimary;
    self.mainHand.parent = self;
	self:addChild(self.mainHand);
    
    
--    self:drawTexture(self.HandSecondaryTexture, -1, 50, 1, 1, 1, 1);
    self.offHand = ISImage:new(0, 50, self.HandSecondaryTexture:getWidthOrig(), self.HandSecondaryTexture:getHeight(), self.HandSecondaryTexture);
    self.offHand:initialise();
	self.offHand.onMouseUp = self.onMouseUpSecondary;
    self.offHand.onRightMouseUp = self.rightClickSecondary;
    self.offHand.parent = self;
    self:addChild(self.offHand);
end

function ISEquippedItem:onMouseUp(x, y)
    if ISMouseDrag.dragging then
        local item1,item2 = self:getDraggedEquippableItems()
        if isForceDropHeavyItem(item1) then
            ISInventoryPaneContextMenu.equipHeavyItem(self.chr, item1)
        elseif item1 and (item1 == item2) then
            ISInventoryPaneContextMenu.equipWeapon(item1, true, true, self.chr:getPlayerNum())
        elseif item1 then
            ISInventoryPaneContextMenu.equipWeapon(item1, true, item1:isRequiresEquippedBothHands(), self.chr:getPlayerNum())
        elseif item2 then
            ISInventoryPaneContextMenu.equipWeapon(item2, false, item2:isRequiresEquippedBothHands(), self.chr:getPlayerNum())
        end
    end
end

function ISEquippedItem:onMouseUpPrimary(x, y)
    if ISMouseDrag.dragging then
        getPlayerInventory(self.parent.chr:getPlayerNum()).render3DItems = {};
        getPlayerLoot(self.parent.chr:getPlayerNum()).render3DItems = {};
        local item1,item2 = self.parent:getDraggedEquippableItems()
        if item1 and item2 then
            return self.parent:onMouseUp(x, y)
        end
        local item = item1 or item2
        if item then
            if isForceDropHeavyItem(item) then
                ISInventoryPaneContextMenu.equipHeavyItem(self.parent.chr, item)
            else
                ISInventoryPaneContextMenu.equipWeapon(item, true, item:isRequiresEquippedBothHands(), self.parent.chr:getPlayerNum())
            end
        end
    end
end

function ISEquippedItem:onMouseUpSecondary(x, y)
    if ISMouseDrag.dragging then
        getPlayerInventory(self.parent.chr:getPlayerNum()).render3DItems = {};
        getPlayerLoot(self.parent.chr:getPlayerNum()).render3DItems = {};
        local item1,item2 = self.parent:getDraggedEquippableItems()
        if item1 and item2 then
            return self.parent:onMouseUp(x, y)
        end
        local item = item1 or item2
        if item then
            if isForceDropHeavyItem(item) then
                ISInventoryPaneContextMenu.equipHeavyItem(self.parent.chr, item)
            else
                ISInventoryPaneContextMenu.equipWeapon(item, false, item:isRequiresEquippedBothHands(), self.parent.chr:getPlayerNum())
            end
        end
    end
end

function ISEquippedItem:rightClickPrimary(x, y)
    local context = ISContextMenu.get(self.parent.chr:getPlayerNum(), getMouseX(), getMouseY());
    context = ISInventoryPaneContextMenu.createMenu(self.parent.chr:getPlayerNum(), true, {self.parent.chr:getPrimaryHandItem()}, getMouseX(), getMouseY());
end

function ISEquippedItem:rightClickSecondary(x, y)
    local context = ISContextMenu.get(self.parent.chr:getPlayerNum(), getMouseX(), getMouseY());
    context = ISInventoryPaneContextMenu.createMenu(self.parent.chr:getPlayerNum(), true, {self.parent.chr:getSecondaryHandItem()}, getMouseX(), getMouseY());
end

function ISEquippedItem:removeFromUIManager()
	if self.movablePopup then
		self.movablePopup:removeFromUIManager()
	end
	if self.mapPopup then
        self.mapPopup:removeFromUIManager()
    end
	ISPanel.removeFromUIManager(self)
end

-----

ISMoveablesIconPopup = ISPanel:derive("ISMoveablesIconPopup")

function ISMoveablesIconPopup:prerender()
    self:setAlwaysOnTop(true)
end

function ISMoveablesIconPopup:render()

    local playerID = self.owner.chr:getPlayerNum()
    local mode = nil
    if getCell():getDrag(playerID) and getCell():getDrag(playerID).isMoveableCursor then
        mode = getCell():getDrag(playerID):getMoveableMode()
    end

    local fontHgt = getTextManager():getFontFromEnum(UIFont.Small):getLineHeight()
    self:drawRect(0, 0, self.width, self.height + fontHgt + 2 * 2, 0.80, 0, 0, 0)

    local index = math.floor(self:getMouseX() / 50)
    if index > 0 or mode then
        self:drawRect(index * 50, 0, 50, self.height, 0.15, 1, 1, 1)
    end
    
    local texts = { getText("IGUI_Exit"), getText("IGUI_Pickup"), getText("IGUI_Place"), getText("IGUI_Rotate"), getText("IGUI_Scrap") }
    if not mode then
        texts[1] = ""
    end
    local text = texts[index+1]
    self:drawText(text, 2, self.height + 2, 1.0, 0.85, 0.05, 1.0, UIFont.Small)

    local x = 0
    local y = 0
    local tex = self.owner.movableIcon
    self:drawTexture(tex, x, y, 1, 1, 1, 1)

    if mode == "pickup" then
        self:drawRectBorder(x + 50, 0, 50, self.height, 0.5, 1, 1, 1)
    end
    tex = self.owner.movableIconPickup
    self:drawTexture(tex, x + 50, y, 1, 1, 1, 1)

    if mode == "place" then
        self:drawRectBorder(x + 50 * 2, 0, 50, self.height, 0.5, 1, 1, 1)
    end
    tex = self.owner.movableIconPlace
    self:drawTexture(tex, x + 50 * 2, y, 1, 1, 1, 1)

    if mode == "rotate" then
        self:drawRectBorder(x + 50 * 3, 0, 50, self.height, 0.5, 1, 1, 1)
    end
    tex = self.owner.movableIconRotate
    self:drawTexture(tex, x + 50 * 3, y, 1, 1, 1, 1)

    if mode == "scrap" then
        self:drawRectBorder(x + 50 * 4, 0, 50, self.height, 0.5, 1, 1, 1)
    end
    tex = self.owner.movableIconScrap
    self:drawTexture(tex, x + 50 * 4, y, 1, 1, 1, 1)
end

function ISMoveablesIconPopup:onMouseDown(x, y)
    return true
end

function ISMoveablesIconPopup:onMouseUp(x, y)
    local playerID = self.owner.chr:getPlayerNum()
    local cursor
    if getCell():getDrag(playerID) and getCell():getDrag(playerID).isMoveableCursor then
        cursor = getCell():getDrag(playerID)
    end

    local index = math.floor(x / 50)
    local mode = nil
    if index == 0 then
        if cursor then
            cursor:exitCursor()
        end
        return
    elseif index == 1 then
        mode = "pickup"
    elseif index == 2 then
        mode = "place"
    elseif index == 3 then
        mode = "rotate"
    elseif index == 4 then
        mode = "scrap"
    end
    if not cursor then
        cursor = ISMoveableCursor:new(self.owner.chr)
        getCell():setDrag(cursor, cursor.player)
    end
    cursor:setMoveableMode(mode)
    self:setVisible(false)
    return true
end

function ISMoveablesIconPopup:new (x, y, width, height)
    local o = ISPanel:new(x, y, width, height);
    setmetatable(o, self)
    self.__index = self
    return o
end

-----

ISMapPopup = ISPanel:derive("ISMapPopup")

function ISMapPopup:prerender()
    self:setAlwaysOnTop(true)
end

function ISMapPopup:render()
    self:drawRect(0, 0, self.width, self.height, 0.80, 0, 0, 0)

    local index = math.floor(self:getMouseX() / 50)
    if index > 0 then
        self:drawRect(index * 50, 0, 50, self.height, 0.15, 1, 1, 1)
    end

    local x = 0
    local y = 0

    local tex = self.texMap
    self:drawTexture(tex, x + (50 - tex:getWidthOrig()) / 2, y + (self.height - tex:getHeightOrig()) / 2, 1, 1, 1, 1)

    tex = self.texMiniMap
    self:drawTexture(tex, x + 50 + (50 - tex:getWidthOrig()) / 2, y + (self.height - tex:getHeightOrig()) / 2, 1, 1, 1, 1)
end

function ISMapPopup:onMouseDown(x, y)
    return true
end

function ISMapPopup:onMouseUp(x, y)
    self:setVisible(false)
    local playerNum = self.owner.chr:getPlayerNum()
    local index = math.floor(x / 50)
    local mode = nil
    if index == 0 then
        ISWorldMap.ToggleWorldMap(playerNum)
    elseif index == 1 then
        ISMiniMap.ToggleMiniMap(playerNum)
    end
    return true
end

function ISMapPopup:new(x, y, width, height)
    local o = ISPanel.new(self, x, y, width, height)
    o.texMap = getTexture("media/textures/worldMap/Map_On.png")
    o.texMiniMap = getTexture("media/textures/worldMap/Map_On.png")
    return o
end

-----

function launchEquippedItem(playerObj)
	local playerNum = playerObj:getPlayerNum()
	local x = getPlayerScreenLeft(playerNum)
    local y = getPlayerScreenTop(playerNum)
	local panel = ISEquippedItem:new(x + 10, y + 10, 100, 250, playerObj);
    panel:initialise();
	panel:addToUIManager();
    return panel;
end

--Events.OnCreateUI.Add(launchEquippedItem);

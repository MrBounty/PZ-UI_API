--
-- Created by IntelliJ IDEA.
-- User: RJ
-- Date: 20/03/2017
-- Time: 12:09
-- To change this template use File | Settings | File Templates.
--

ISFishingUI = ISPanelJoypad:derive("ISFishingUI");
ISFishingUI.instance = {};
ISFishingUI.messages = {};

local FONT_HGT_SMALL = getTextManager():getFontHeight(UIFont.Small)
local FONT_HGT_MEDIUM = getTextManager():getFontHeight(UIFont.Medium)

local function noise(str)
    if getDebug() then print(str) end
end

local function predicateFishingLure(item)
    return item:isFishingLure()
end

local function predicateFishingRodOrSpear(item, playerObj)
    if item:isBroken() then return false end
    if not item:hasTag("FishingRod") and not item:hasTag("FishingSpear") then return false end
    return true
end

--************************************************************************--
--** ISFishingUI:initialise
--**
--************************************************************************--

function ISFishingUI:setVisible(bVisible)
    if self.javaObject == nil then
        self:instantiate();
    end
    self.javaObject:setVisible(bVisible);
    if self.visibleTarget and self.visibleFunction then
        self.visibleFunction(self.visibleTarget, self);
    end
end

function ISFishingUI:initialise()
    ISPanelJoypad.initialise(self);
    local btnWid = 100
    local btnHgt = math.max(FONT_HGT_SMALL + 3 * 2, 25)
    local padBottom = 10

    self.ok = ISButton:new(10, self:getHeight() - padBottom - btnHgt, btnWid, btnHgt, getText("UI_Ok"), self, ISFishingUI.onClick);
    self.ok.internal = "OK";
    self.ok.anchorTop = false
    self.ok.anchorBottom = true
    self.ok:initialise();
    self.ok:instantiate();
    self.ok.borderColor = self.buttonBorderColor;
    self:addChild(self.ok);
    
    self.cancel = ISButton:new(self.ok:getRight() + 5, self:getHeight() - padBottom - btnHgt, btnWid, btnHgt, getText("UI_Cancel"), self, ISFishingUI.onClick);
    self.cancel.internal = "CANCEL";
    self.cancel.anchorTop = false
    self.cancel.anchorBottom = true
    self.cancel:initialise();
    self.cancel:instantiate();
    self.cancel.borderColor = self.buttonBorderColor;
    self:addChild(self.cancel);
    
    self.close = ISButton:new(self:getWidth() - btnWid - 10, self:getHeight() - padBottom - btnHgt, btnWid, btnHgt, getText("UI_Close"), self, ISFishingUI.onClick);
    self.close.internal = "CLOSE";
    self.close.anchorLeft = false
    self.close.anchorRight = true
    self.close.anchorTop = false
    self.close.anchorBottom = true
    self.close:initialise();
    self.close:instantiate();
    self.close.borderColor = self.buttonBorderColor;
    self:addChild(self.close);

    -- rod options
    self.rods = ISRadioButtons:new(10, 50, 150, 20, self, ISFishingUI.clickedRod)
    self.rods.choicesColor = {r=1, g=1, b=1, a=1}
    self.rods:initialise()
    self.rods.autoWidth = true;
    self:addChild(self.rods)
    self:updateRods();

    -- lure options
    self.lures = ISRadioButtons:new(10, self.rods:getBottom() + 20, 150, 20)
    self.lures.choicesColor = {r=1, g=1, b=1, a=1}
    self.lures:initialise()
    self.lures.autoWidth = true;
    self:addChild(self.lures)
    self:updateLures();
    
    -- Destination container options
    local bagsX = math.max(self.rods:getRight(), self.lures:getRight()) + 10
    --self.bagOptions = ISRadioButtons:new(bagsX, self.rods.y, 100, 20)
    --self.bagOptions.choicesColor = {r=1, g=1, b=1, a=1}
    --self.bagOptions:initialise()
    --self.bagOptions.autoWidth = true;
    --self:addChild(self.bagOptions)
    self.containersLbl = ISLabel:new(bagsX, self.rods.y, 10, getText("IGUI_FishingUI_CatchBag"),1,1,1,1,UIFont.Small, true);
    self:addChild(self.containersLbl);

    self.containerSelector = ISComboBox:new(bagsX, self.rods.y + 20, 200, 20);
    self.containerSelector:initialise();
    self:addChild(self.containerSelector);
    self:doBagOptions();


    self.barPadY = 4
    self.btnHgt = btnHgt
    self.padBottom = padBottom
    self.barY = self.lures:getBottom() + self.barPadY

    self:insertNewLineOfButtons(self.rods)
	self:insertNewLineOfButtons(self.lures)
	self:insertNewLineOfButtons(self.ok, self.cancel, self.close)

    self:updateSize();
    self:updateZoneProgress();
end

function ISFishingUI:updateSize()
    local bagsX = math.max(self.rods:getRight(), self.lures:getRight()) + 10
    self.containerSelector:setX(bagsX)
    self:setWidth(self.containerSelector:getRight() + 10)
    self.lures:setY(self.rods:getBottom() + 20)
    self.barY = self.lures:getBottom() + self.barPadY

    local fishY = self.containerSelector:getBottom() + 10
    local fishHgt = FONT_HGT_SMALL * self.maxFish
    self.barY = math.max(self.barY, fishY + fishHgt + self.barPadY)

    self:setHeight(self.barY + self.barHgt + self.barPadY + self.barHgt + self.barPadY + self.btnHgt + self.padBottom)
end

function ISFishingUI:clickedRod(buttons, index)
    self:updateButtons();
    self:updateLures();
    self.usingSpear = false;
    if WeaponType.getWeaponType(self.selectedRod) == WeaponType.spear then
        self.usingSpear = true;
    end
end

function ISFishingUI:updateRods()
    self.rods:clear();
    local rods = {};
    local sorted = {}
    self.items_array:clear()
    self.player:getInventory():getAllEvalRecurse(predicateFishingRodOrSpear, self.items_array)
    for i=1,self.items_array:size() do
        local item = self.items_array:get(i-1)
        if not rods[item:getType()] then
            local rod = {};
            rod.item = item;
            rod.equipped = item:isEquipped();
            -- disable the option if not lure for a rod (spears don't need them tho)
            if not ISWorldObjectContextMenu.getFishingLure(self.player, item) then
                rod.disabled = true;
            end
            rods[item:getType()] = rod;
            table.insert(sorted, rod);
        end
    end
    table.sort(sorted, function(a,b) return not string.sort(a.item:getDisplayName(), b.item:getDisplayName()) end)
    local index = 1;
    local indexToSelect = -1;
    for _,v in ipairs(sorted) do
        self.rods:addOption(v.item:getDisplayName(), v.item, v.item:getTexture());
        if v.equipped then
            indexToSelect = index
        end
        if v.disabled then
            self.rods:setOptionEnabled(index, false);
        else
            if indexToSelect == -1 then
                indexToSelect = index;
            end
        end
        index = index + 1;
    end
    if indexToSelect > -1 then
        self.selectedRod = self.rods:getOptionData(indexToSelect);
        self.rods:setSelected(indexToSelect);
    
        if WeaponType.getWeaponType(self.selectedRod) == WeaponType.spear then
            self.usingSpear = true;
        end
    end
end

function ISFishingUI:updateSelectedRod()
    for i=1,#self.rods.options do
        if self.rods:isSelected(i) then
            self.selectedRod = self.rods:getOptionData(i);
            return self.selectedRod;
        end
    end
end

function ISFishingUI:updateLures()
    self:updateSelectedRod();
    self.lures:clear();
    self.luresEnabled = self.selectedRod and (WeaponType.getWeaponType(self.selectedRod) ~= WeaponType.spear);
--    if not self.selectedRod then return; end
    local lures = {};
    local sorted = {};
    self.items_array:clear()
    self.player:getInventory():getAllEvalRecurse(predicateFishingLure, self.items_array)
    for i=1,self.items_array:size() do
        local item = self.items_array:get(i-1);
        if not lures[item:getType()] then
            local lure = {};
            lure = {};
            lure.item = item;
            lure.nbr = 1;
            lures[item:getType()] = lure;
            table.insert(sorted, lure);
        else
            lures[item:getType()].nbr = lures[item:getType()].nbr + 1;
        end
    end
    table.sort(sorted, function(a,b) return not string.sort(a.item:getDisplayName(), b.item:getDisplayName()) end)
    local index = 1;
    local indexToSelect = -1;
    for _,v in ipairs(sorted) do
        self.lures:addOption(v.item:getDisplayName(), v.item, v.item:getTexture());
        -- spears don't need lures, we disable all options
        if self.luresEnabled then
            if v.item:isEquipped() then
                indexToSelect = index;
            elseif indexToSelect == -1 then
                indexToSelect = index;
            end
        else
            self.lures:setOptionEnabled(index, false);
        end
        index = index + 1;
    end
    if indexToSelect ~= -1 then
        self.lures:setSelected(indexToSelect);
    end
end

function ISFishingUI:render()
	ISPanelJoypad.render(self);
    local actionQueue = ISTimedActionQueue.getTimedActionQueue(self.player)
    local currentAction = actionQueue.queue[1]
    self:updateButtons(currentAction);

    -- display no rods found
    self:updateSelectedRod();
    if not self.selectedRod and self.rods:isEmpty() then
        self:drawText(getText("IGUI_FishingUI_NoRod"), self.rods:getX(), self.rods:getY(), 1,0.3,0.3,1, UIFont.Small);
    end
    
    if self.lures:isEmpty() and ((self.selectedRod and WeaponType.getWeaponType(self.selectedRod) ~= WeaponType.spear) or not self.selectedRod) then
        self:drawText(getText("IGUI_FishingUI_NoLure"), self.rods:getX(), self.lures:getY(), 1,0.3,0.3,1, UIFont.Small);
    end

    -- Fishes zone items left
    local zoneProgressBar = self.fgBar;
    if self.zoneProgress < 60 then
        zoneProgressBar = self.fgBarOrange;
    end
    if self.zoneProgress < 30 then
        zoneProgressBar = self.fgBarRed;
    end
    local barY = self.barY + self.barHgt + self.barPadY
    self:drawProgressBar(10, barY, self.width - 20, self.barHgt, self.zoneProgress / 100, zoneProgressBar)
    self:drawTextCentre(getText("IGUI_FishingUI_FishAbundance") .. self.zoneProgress .. "%", self.width/2, barY, 1,1,1,1, UIFont.Small);
    -- Display current action progress bar
    if currentAction and (currentAction.Type == "ISFishingAction") and currentAction.action then
        self:drawProgressBar(10, self.barY, self.width - 20, self.barHgt, currentAction.action:getJobDelta(), self.fgBar)
        self:drawTextCentre(getText("ContextMenu_Fishing"), self.width/2, self.barY, 1,1,1,1, UIFont.Small);
    end

    local listY = self.containerSelector.y + self.containerSelector.height + 10;
    local texWH = FONT_HGT_SMALL
    local numFishes = #self.fishes
    local minFish = math.max(numFishes - self.maxFish + 1, 1)
    for i=numFishes,1,-1 do
        local v = self.fishes[i];
        self:drawTextureScaled(v.texture, self.containerSelector:getX(), listY, texWH, texWH, v.alpha, 1, 1, 1);
        self:drawText(v.item:getDisplayName(), self.containerSelector:getX() + texWH + 2, listY, 1,1,1, v.alpha, UIFont.Small);
        -- Fade over time
        --v.alphaTimer = v.alphaTimer - (UIManager.getMillisSinceLastRender() / 33.3);
        --if v.alphaTimer <= 0 then
        --    v.alphaTimer = 0;
        --    v.alpha = v.alpha - 0.01;
        --    if v.alpha < 0 then
        --        table.remove(self.fishes, i);
        --    end
        --end
        listY = listY + FONT_HGT_SMALL;
    end
end

function ISFishingUI:prerender()
    local z = 20;
    local splitPoint = 100;
    local x = 10;
    self:drawRect(0, 0, self.width, self.height, self.backgroundColor.a, self.backgroundColor.r, self.backgroundColor.g, self.backgroundColor.b);
    self:drawRectBorder(0, 0, self.width, self.height, self.borderColor.a, self.borderColor.r, self.borderColor.g, self.borderColor.b);
    self:drawTextCentre(getText("ContextMenu_Fishing"), self.width/2, self.titleY, 1,1,1,1, UIFont.Medium);
    if self.joyfocus and self:getJoypadFocus() == self.ok and self.joyfocus.isActive then
        self:setISButtonForA(self.ok)
    elseif self.ok.isJoypad then
        self.ISButtonA = nil
        self.ok.isJoypad = false
    end
    local now = getTimestampMs()
    if now - (self.checkStuffTime or 0) > 500 then
        self.checkStuffTime = now
        self:checkInventory()
        self:checkPlayerPosition()
    end
end

function ISFishingUI:checkInventory()
    if self:checkInventoryLures() or
            self:checkInventoryRods() or
            self:checkInventoryBags() then
        self:updateRods()
        self:updateSelectedRod()
        self:updateLures()
        self:doBagOptions()
        self:updateSize()
    end
end

function ISFishingUI:checkInventoryRods()
    local playerInv = self.player:getInventory()
    local hasRod = {}
    for i=1,self.rods:getNumOptions() do
        local item = self.rods:getOptionData(i)
        if not playerInv:containsRecursive(item) then
            noise('checkInventoryRods')
            return true
        end
        hasRod[item:getType()] = true
    end
    self.items_array:clear()
    local items = playerInv:getAllEvalRecurse(predicateFishingRodOrSpear, self.items_array)
    for i=1,items:size() do
        if not hasRod[items:get(i-1):getType()] then
            noise('checkInventoryRods')
            return true
        end
    end
    return false
end

function ISFishingUI:checkInventoryLures()
    local luresEnabled = self.selectedRod and (WeaponType.getWeaponType(self.selectedRod) ~= WeaponType.spear);
    if luresEnabled ~= self.luresEnabled then
        noise('checkInventoryLures')
        return true
    end

    local playerInv = self.player:getInventory()
    local hasLure = {}
    for i=1,self.lures:getNumOptions() do
        local item = self.lures:getOptionData(i)
        if not playerInv:containsRecursive(item) then
            noise('checkInventoryLures')
            return true
        end
        hasLure[item:getType()] = true
    end
    self.items_array:clear()
    local items = playerInv:getAllEvalRecurse(predicateFishingLure, self.items_array)
    for i=1,items:size() do
        if not hasLure[items:get(i-1):getType()] then
            noise('checkInventoryLures')
            return true
        end
    end
    return false
end

local function testBag(item)
    return instanceof(item, "InventoryContainer") and item or nil
end

function ISFishingUI:checkInventoryBags()
    --[[local playerInv = self.player:getInventory()

    local itemPrimary = testBag(self.player:getPrimaryHandItem())
    local itemSecondary = testBag(self.player:getSecondaryHandItem())
    local itemBack = testBag(self.player:getClothingItem_Back())

    -- A bag could change from primary to secondary hand, so can't just check if still holding it.
    if (itemPrimary ~= self.bagPrimary) or (itemSecondary ~= self.bagSecondary) or (itemBack ~= self.bagBack) then
        noise('checkInventoryBags')
        return true
    end--]]

    local newContainers = self.player:getInventory():getItemsFromCategory("Container");
    if(newContainers:size() ~= self.containers:size()) then noise('checkInventoryBags'); return true; end
    for i=0, self.containers:size() - 1 do
        local bag = self.containers:get(i);
        local bagNew = newContainers:get(i);
        if(bag ~= bagNew) then noise('checkInventoryBags'); return true; end
    end

    return false
end

function ISFishingUI:getSelectedBag()
    return self.containerSelector.options[self.containerSelector.selected].data;
end

function ISFishingUI:updateButtons(currentAction)
    self.ok.enable = false;
    self.cancel.enable = false;
    local lure = false;
    self.selectedRod = nil;
    if (currentAction and (currentAction.Type == "ISFishingAction") and currentAction.action) then
        self.cancel.enable = true;
        self.ok.enable = false;
        return;
    end
    for i=1,self.rods:getNumOptions() do
        if self.rods:isSelected(i) then
            self.selectedRod = self.rods:getOptionData(i);
        end
    end
    if self.selectedRod then
        local checkLure = true;
        -- spear doesn't need lures!
        if WeaponType.getWeaponType(self.selectedRod) == WeaponType.spear then
            self.ok.enable = true;
            checkLure = false;
        end
        if checkLure then
            for i=1,#self.lures.options do
                if self.lures:isSelected(i) then
                    self.ok.enable = true;
                end
            end
        end
    end
    self.ok.tooltip = nil;
    if not self.canFishDistance then
        self.ok.enable = false;
        self.ok.tooltip = getText("IGUI_GetCloserToWater");
    end
end

function ISFishingUI:onClick(button)
    if button.internal == "OK" then
        self:equipItems();
        local action = ISFishingAction:new(self.player, self.clickedSquare, self.selectedRod, self.selectedLure, self);
        ISTimedActionQueue.add(action);
    elseif button.internal == "CLOSE" then
        self:setVisible(false);
        self:removeFromUIManager();
        local playerNum = self.player:getPlayerNum()
        if JoypadState.players[playerNum+1] then
            setJoypadFocus(playerNum, nil)
        end
    elseif button.internal == "CANCEL" then
        local actionQueue = ISTimedActionQueue.getTimedActionQueue(self.player)
        local currentAction = actionQueue.queue[1]
        if currentAction and (currentAction.Type == "ISFishingAction") and currentAction.action then
            currentAction.action:forceStop()
        end
    end
end

function ISFishingUI:onGainJoypadFocus(joypadData)
    ISPanelJoypad.onGainJoypadFocus(self, joypadData)
    self.joypadIndexY = 3
    self.joypadIndex = 1
    self.joypadButtons = self.joypadButtonsY[self.joypadIndexY]
    self.joypadButtons[self.joypadIndex]:setJoypadFocused(true)
    self:setISButtonForB(self.cancel)
    self:setISButtonForY(self.close)
end

function ISFishingUI:onJoypadBeforeDeactivate(joypadData)
    self.cancel:clearJoypadButton()
    self.close:clearJoypadButton()
    self:clearJoypadFocus(joypadData)
end

function ISFishingUI:onJoypadBeforeReactivate(joypadData)
    self:setISButtonForB(self.cancel)
    self:setISButtonForY(self.close)
    self:restoreJoypadFocus(joypadData)
end

function ISFishingUI:onJoypadDown(button)
    ISPanelJoypad.onJoypadDown(self, button)
    if button == Joypad.BButton then
        self:onClick(self.cancel)
    end
end

function ISFishingUI:setFish(item)
    self:updateRods();
    self:updateLures();
    self:updateSize();
    if not item then return; end
    local newItem = {};
    newItem.item = item;
    newItem.texture = item:getTexture();
    newItem.alpha = 1;
    newItem.alphaTimer = 40;

    local fishesNum = #self.fishes;
    if (fishesNum > 2) then
        table.remove(self.fishes, 1);
    end
    
    table.insert(self.fishes, newItem);
end

function ISFishingUI:doBagOptions()
    --[[self.bagOptions:clear();
    self.bagPrimary = nil;
    self.bagSecondary = nil;
    self.bagBack = nil;
    self.bagOptions:addOption(getText("IGUI_ScavengeUI_PutItemInPlayerInv"), self.player:getInventory());
    if self.player:getPrimaryHandItem() and instanceof(self.player:getPrimaryHandItem(), "InventoryContainer") then
        self.bagOptions:addOption(getText("IGUI_ScavengeUI_PutItemInBag", self.player:getPrimaryHandItem():getDisplayName(), getText("IGUI_ScavengeUI_1stHand")), self.player:getPrimaryHandItem():getInventory());
        self.bagPrimary = self.player:getPrimaryHandItem();
    end
    if self.player:getSecondaryHandItem() and instanceof(self.player:getSecondaryHandItem(), "InventoryContainer") then
        self.bagOptions:addOption(getText("IGUI_ScavengeUI_PutItemInBag", self.player:getSecondaryHandItem():getDisplayName(), getText("IGUI_ScavengeUI_2ndHand")), self.player:getSecondaryHandItem():getInventory());
        self.bagSecondary = self.player:getSecondaryHandItem();
    end
    if self.player:getClothingItem_Back() and instanceof(self.player:getClothingItem_Back(), "InventoryContainer") then
        self.bagOptions:addOption(getText("IGUI_ScavengeUI_PutItemInBag", self.player:getClothingItem_Back():getDisplayName(), getText("IGUI_ScavengeUI_Back")), self.player:getClothingItem_Back():getInventory());
        self.bagBack = self.player:getClothingItem_Back();
    end
    self.bagOptions:setSelected(1, true);--]]

    self.containerSelector:clear();
    -- First option is player's main inventory
    self.containerSelector:addOptionWithData("Main Inventory", self.player:getInventory());
    -- Then we fill the ComboBox with the containers on player's inventory
    self.containers = self.player:getInventory():getItemsFromCategory("Container");
    for i=0, self.containers:size() - 1 do
        local bag = self.containers:get(i);
        if (bag:getType() ~= "KeyRing") then
            self.containerSelector:addOptionWithData(bag:getName(), bag);
        end
    end
    --self.containerSelector:setSelected(1, true);
end

function ISFishingUI:updateZoneProgress(zoneClicked)
    self.zoneProgress = 100;
    if not zoneClicked then
        zoneClicked = ISFishingAction.getFishingZoneFixed(self.clickedSquareX, self.clickedSquareY, self.clickedSquareZ);
    end
    if zoneClicked and zoneClicked:getName() == "0" then
        self.zoneProgress = 0;
    end
    if zoneClicked then
        local currentFish = tonumber(zoneClicked:getName());
        local totalFish = tonumber(zoneClicked:getOriginalName());
        if not currentFish or not totalFish or currentFish <= 0 or totalFish <= 0 then
            self.zoneProgress = 0;
        else
            self.zoneProgress = math.floor((currentFish / totalFish) * 100);
        end
    end
end

function ISFishingUI:equipItems()
    self.selectedLure = nil;
    for i=1,#self.rods.options do
        if self.rods:isSelected(i) then
            self.selectedRod = self.rods:getOptionData(i);
            local twohands = false;
            if WeaponType.getWeaponType(self.selectedRod) == WeaponType.spear then
                twohands = true;
            end
            self.selectedRod = ISWorldObjectContextMenu.equip(self.player, self.player:getPrimaryHandItem(), self.selectedRod:getType(), true, twohands);
        end
    end
    if WeaponType.getWeaponType(self.selectedRod) ~= WeaponType.spear then
        for i=1,#self.lures.options do
            if self.lures:isSelected(i) then
                self.selectedLure = self.lures:getOptionData(i);
                self.selectedLure = ISWorldObjectContextMenu.equip(self.player, self.player:getSecondaryHandItem(), self.selectedLure:getType(), false);
            end
        end
    end
end

function ISFishingUI:checkPlayerPosition()
    local playerObj = self.player;
    local plyrX = math.floor(playerObj:getX());
    local plyrY = math.floor(playerObj:getY());
    if plyrX == self.checkPlayerX and plyrY == self.checkPlayerY then
        return
    end
    noise('checkPlayerPosition')
    self.checkPlayerX = plyrX;
    self.checkPlayerY = plyrY;
    self.canFishDistance = false;
    local dist = 4;
    if self.usingSpear then
        dist = 1;
    end
    if playerObj:getZ() > 1 then
        return;
    end

    -- This zone is nil where the player has never caught anything.
    local zoneClicked = ISFishingAction.getFishingZoneFixed(self.clickedSquareX, self.clickedSquareY, self.clickedSquareZ);

    self.squareWithSameZone = self.squareWithSameZone or {}
    self.squareWithOtherZone = self.squareWithOtherZone or {}
    self.squareWithoutZone = self.squareWithoutZone or {}
    table.wipe(self.squareWithSameZone)
    table.wipe(self.squareWithOtherZone)
    table.wipe(self.squareWithoutZone)

    local z = playerObj:getZ()
    for x=plyrX - dist, plyrX + dist do
        for y=plyrY - dist, plyrY + dist do
            local sq = getSquare(x, y, z);
            if sq and sq:getFloor() and sq:getFloor():getSprite() then
                if sq:getFloor():getSprite():getProperties():Is(IsoFlagType.water) then
                    local zone = ISFishingAction.getFishingZoneFixed(x, y, z)
                    if zone ~= nil and zone == zoneClicked then
                        table.insert(self.squareWithSameZone, sq)
                    elseif zone ~= nil then
                        table.insert(self.squareWithOtherZone, sq)
                    else
                        table.insert(self.squareWithoutZone, sq)
                    end
                end
            end
        end
    end

    local bestSquare = nil

    if #self.squareWithSameZone > 0 then
        bestSquare = self:pickBestSquare(self.squareWithSameZone)
    elseif #self.squareWithOtherZone > 0 then
        bestSquare = self:pickBestSquare(self.squareWithOtherZone)
    elseif #self.squareWithoutZone > 0 then
        bestSquare = self:pickBestSquare(self.squareWithoutZone)
    end

    if bestSquare then
        self.canFishDistance = true
        if bestSquare:getFloor() ~= self.clickedSquare then
            noise(string.format('clickedSquare %d,%d,%d', bestSquare:getX(), bestSquare:getY(), bestSquare:getZ()))
            self.clickedSquare = bestSquare:getFloor()
            self.clickedSquareX = bestSquare:getX()
            self.clickedSquareY = bestSquare:getY()
            self.clickedSquareZ = bestSquare:getZ()
        end
        self:updateZoneProgress()
    else
        self.zoneProgress = 0
    end
end

function ISFishingUI:pickBestSquare(squares)
    local playerObj = self.player
    local bestDot = -1
    local bestSquare = nil
    local bestDist = 10000
    for _,square in ipairs(squares) do
        local dot = playerObj:getDotWithForwardDirection(square:getX() + 0.5, square:getY() + 0.5)
        local dist = playerObj:DistToSquared(square:getX() + 0.5, square:getY() + 0.5)
        if not bestSquare or (dot > bestDot) or (dot == bestDot and dist < bestDist) then
            bestSquare = square
            bestDot = dot
            bestDist = dist
        end
    end
    return bestSquare
end

--************************************************************************--
--** ISFishingUI:new
--**
--************************************************************************--
function ISFishingUI:new(x, y, width, height, player, clickedSquare)
    local o = {}
    if y == 0 then
        y = getPlayerScreenTop(player:getPlayerNum()) + (getPlayerScreenHeight(player:getPlayerNum()) - height) / 2
        y = y + 200;
    end
    if x == 0 then
        x = getPlayerScreenLeft(player:getPlayerNum()) + (getPlayerScreenWidth(player:getPlayerNum()) - width) / 2
    end
    local o = ISPanelJoypad.new(self, x, y, width, height);
    o.borderColor = {r=0.4, g=0.4, b=0.4, a=1};
    o.backgroundColor = {r=0, g=0, b=0, a=0.6};
    o.width = width;
    o.titleY = 10
    o.barHgt = FONT_HGT_SMALL;
    o.height = height;
    o.player = player;
    o.fgBar = {r=0, g=0.6, b=0, a=0.7 }
    o.fgBarOrange = {r=1, g=0.3, b=0, a=0.7 }
    o.fgBarRed = {r=1, g=0, b=0, a=0.7 }
    o.moveWithMouse = true;
    o.clickedSquare = clickedSquare;
    o.clickedSquareX = clickedSquare:getX()
    o.clickedSquareY = clickedSquare:getY()
    o.clickedSquareZ = clickedSquare:getZ()
    o.buttonBorderColor = {r=0.7, g=0.7, b=0.7, a=0.5};
    o.fishes = {};
    o.maxFish = 4;
    o.rod = rod;
    o.zoneProgress = 100;
    o.items_array = ArrayList.new();
    ISFishingUI.instance[player:getPlayerNum()+1] = o;
    return o;
end

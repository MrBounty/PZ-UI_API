--
-- Created by IntelliJ IDEA.
-- User: RJ
-- Date: 20/03/2017
-- Time: 12:09
-- To change this template use File | Settings | File Templates.
--

---@class ISScavengeUI : ISPanelJoypad
ISScavengeUI = ISPanelJoypad:derive("ISScavengeUI");
ISScavengeUI.messages = {};
ISScavengeUI.windows = {}

local FONT_HGT_SMALL = getTextManager():getFontHeight(UIFont.Small)
local FONT_HGT_MEDIUM = getTextManager():getFontHeight(UIFont.Medium)

--************************************************************************--
--** ISScavengeUI:initialise
--**
--************************************************************************--

function ISScavengeUI:initialise()
    ISPanelJoypad.initialise(self);
    local btnWid = 100
    local btnHgt = math.max(FONT_HGT_SMALL + 3 * 2, 25)
    local padBottom = 10

    self.options = ISTickBox:new(10, self.titleY + FONT_HGT_MEDIUM + 10, 150, 20, "")
    self.options.choicesColor = {r=1, g=1, b=1, a=1}
    self.options:initialise()
    self.options.autoWidth = true;
    self.options:addOption(getText("IGUI_ScavengeUI_Materials"), "ForestGoods");
    self.options:addOption(getText("IGUI_ScavengeUI_Mushrooms"), "Mushrooms");
    self.options:addOption(getText("IGUI_ScavengeUI_Berries"), "Berries");
    self.options:addOption(getText("IGUI_ScavengeUI_Animals"), "Insects");
    self.options:addOption(getText("IGUI_ScavengeUI_FishBait"), "FishBaits");
    local option = self.options:addOption(getText("IGUI_ScavengeUI_MedicinalPlants"), "MedicinalPlants");
    if savedScavengeOptions then
        for i=1,#self.options.optionData do
            if savedScavengeOptions[self.options.optionData[i]] then
                self.options:setSelected(i, true);
            end
        end
    else
        for i=1,#self.options.options do
            self.options:setSelected(i, true);
        end
    end
    if not self.player:isRecipeKnown("Herbalist") then
        self.options:disableOption(getText("IGUI_ScavengeUI_MedicinalPlants"), true);
        self.options:setSelected(option, false);
    end
    self:addChild(self.options)

    -- Tick box to put items inside equipped bag directly
    self.bagOptions = ISRadioButtons:new(self.options:getRight(), self.options.y, 100, 20)
    self.bagOptions.choicesColor = {r=1, g=1, b=1, a=1}
    self.bagOptions:initialise()
    self.bagOptions.autoWidth = true;
    self:addChild(self.bagOptions)
    self:doBagOptions();

    self.ok = ISButton:new(10, self:getHeight() - padBottom - btnHgt, btnWid, btnHgt, getText("UI_Ok"), self, ISScavengeUI.onClick);
    self.ok.internal = "OK";
    self.ok.anchorTop = false
    self.ok.anchorBottom = true
    self.ok:initialise();
    self.ok:instantiate();
    self.ok.borderColor = self.buttonBorderColor;
    self:addChild(self.ok);

    self.cancel = ISButton:new(self.ok:getRight() + 5, self:getHeight() - padBottom - btnHgt, btnWid, btnHgt, getText("UI_Cancel"), self, ISScavengeUI.onClick);
    self.cancel.internal = "CANCEL";
    self.cancel.anchorTop = false
    self.cancel.anchorBottom = true
    self.cancel:initialise();
    self.cancel:instantiate();
    self.cancel.borderColor = self.buttonBorderColor;
    self:addChild(self.cancel);

    self.close = ISButton:new(self:getWidth() - btnWid - 10, self:getHeight() - padBottom - btnHgt, btnWid, btnHgt, getText("UI_Close"), self, ISScavengeUI.onClick);
    self.close.internal = "CLOSE";
    self.close.anchorTop = false
    self.close.anchorBottom = true
    self.close:initialise();
    self.close:instantiate();
    self.close.borderColor = self.buttonBorderColor;
    self:addChild(self.close);

    self.barPadY = 4
    self.barY = self.options:getBottom() + self.barPadY
    self:setHeight(self.barY + self.barHgt + self.barPadY + self.barHgt + self.barPadY + btnHgt + padBottom)

    self:insertNewLineOfButtons(self.options, self.bagOptions)
    self:insertNewLineOfButtons(self.ok, self.cancel, self.close)

    self:updateZoneProgress();
end


function ISScavengeUI:render()
    ISPanelJoypad.render(self);
    local actionQueue = ISTimedActionQueue.getTimedActionQueue(self.player)
    local currentAction = actionQueue.queue[1]
    self:updateButtons(currentAction);

    -- Forage zone items left
    local zoneProgressBar = self.fgBar;
    if self.zoneProgress < 60 then
        zoneProgressBar = self.fgBarOrange;
    end
    if self.zoneProgress < 30 then
        zoneProgressBar = self.fgBarRed;
    end
    local barY = self.barY + self.barHgt + self.barPadY
    self:drawProgressBar(10, barY, self.width - 20, self.barHgt, self.zoneProgress / 100, zoneProgressBar)
    self:drawTextCentre(getText("IGUI_ScavengeUI_LootAbundance") .. self.zoneProgress .. "%", self.width/2, barY, 1,1,1,1, UIFont.Small);
    -- Display current action progress bar
    if currentAction and (currentAction.Type == "ISScavengeAction") and currentAction.action then
        self:drawProgressBar(10, self.barY, self.width - 20, self.barHgt, currentAction.action:getJobDelta(), self.fgBar)
        self:drawTextCentre(getText("ContextMenu_Forage"), self.width/2, self.barY, 1,1,1,1, UIFont.Small);
    end

    local listY = self.bagOptions.y + self.bagOptions.height + 10;
    local texWH = FONT_HGT_SMALL
    local numItem = #self.itemsScavenged
    local minItem = math.max(numItem - self.maxItem + 1, 1)
    for i=numItem,1,-1 do
        local v = self.itemsScavenged[i]
        self:drawTextureScaledAspect(v.texture, self.options:getRight(), listY, texWH, texWH, v.alpha, 1, 1, 1);
        self:drawText(v.item:getDisplayName() .. " x" .. v.nbr, self.options:getRight() + texWH + 2, listY, 1,1,1, v.alpha, UIFont.Small);
        v.alphaTimer = v.alphaTimer - (UIManager.getMillisSinceLastRender() / 33.3);
        if v.alphaTimer <= 0 then
            v.alphaTimer = 0;
            v.alpha = v.alpha - 0.01;
            if v.alpha < 0 then
                table.remove(self.itemsScavenged, i);
            end
        end
        listY = listY + FONT_HGT_SMALL;
    end
end

function ISScavengeUI:prerender()
    local splitPoint = 100;
    local x = 10;
    self:drawRect(0, 0, self.width, self.height, self.backgroundColor.a, self.backgroundColor.r, self.backgroundColor.g, self.backgroundColor.b);
    self:drawRectBorder(0, 0, self.width, self.height, self.borderColor.a, self.borderColor.r, self.borderColor.g, self.borderColor.b);
    self:drawTextCentre(getText("IGUI_ScavengeUI_Title"), self.width/2, self.titleY, 1,1,1,1, UIFont.Medium);
    if self.joyfocus and self:getJoypadFocus() == self.ok then
        self:setISButtonForA(self.ok)
    else
        self.ISButtonA = nil
        self.ok.isJoypad = false
    end

    if self.player:getVehicle() then
        self.close:forceClick()
    end
end

function ISScavengeUI:updateButtons(currentAction)
    self.ok.enable = false;
    self.cancel.enable = false;
    if self.zoneProgress <= 0 then
        return;
    end
    if (currentAction and (currentAction.Type == "ISScavengeAction") and currentAction.action) then
        self.cancel.enable = true;
        return;
    end
    for i=1,#self.options.options do
        if self.options:isSelected(i) then
            self.ok.enable = true;
            break;
        end
    end
end

function ISScavengeUI:onClick(button)
    if button.internal == "OK" then
        if luautils.walkAdj(self.player, self.clickedSquare) then
            savedScavengeOptions = {};
            for i=1,#self.options.options do
                if self.options:isSelected(i) then
                    savedScavengeOptions[self.options.optionData[i]] = true;
                end
            end

            local action = ISScavengeAction:new(self.player, self.zone, savedScavengeOptions, self);
            ISTimedActionQueue.add(action);
        end
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
        if currentAction and (currentAction.Type == "ISScavengeAction") and currentAction.action then
            currentAction.action:forceStop()
        end
    end
end

function ISScavengeUI:onGainJoypadFocus(joypadData)
    ISPanelJoypad.onGainJoypadFocus(self, joypadData)
    self.joypadIndexY = 2
    self.joypadIndex = 1
    self.joypadButtons = self.joypadButtonsY[self.joypadIndexY]
    self.joypadButtons[self.joypadIndex]:setJoypadFocused(true)
    self:setISButtonForB(self.cancel)
    self:setISButtonForY(self.close)
end

function ISScavengeUI:onJoypadDown(button)
    ISPanelJoypad.onJoypadDown(self, button)
    if button == Joypad.BButton then
        self:onClick(self.cancel)
    end
end

function ISScavengeUI:setItemScavenged(item)
    local newItem = {};
    newItem.nbr = item:size();
    item = item:get(0);
    newItem.item = item;
    newItem.texture = item:getTexture();
    newItem.alpha = 1;
    newItem.alphaTimer = 40;
    table.insert(self.itemsScavenged, newItem);
end

function ISScavengeUI:doBagOptions()
    self.bagOptions:clear();
    self.bagOptions:addOption(getText("IGUI_ScavengeUI_PutItemInPlayerInv"), self.player:getInventory());
    if self.player:getPrimaryHandItem() and instanceof(self.player:getPrimaryHandItem(), "InventoryContainer") then
        self.bagOptions:addOption(getText("IGUI_ScavengeUI_PutItemInBag", self.player:getPrimaryHandItem():getDisplayName(), getText("IGUI_ScavengeUI_1stHand")), self.player:getPrimaryHandItem():getInventory());
    end
    if self.player:getSecondaryHandItem() and instanceof(self.player:getSecondaryHandItem(), "InventoryContainer") then
        self.bagOptions:addOption(getText("IGUI_ScavengeUI_PutItemInBag", self.player:getSecondaryHandItem():getDisplayName(), getText("IGUI_ScavengeUI_2ndHand")), self.player:getSecondaryHandItem():getInventory());
    end
    if self.player:getClothingItem_Back() and instanceof(self.player:getClothingItem_Back(), "InventoryContainer") then
        self.bagOptions:addOption(getText("IGUI_ScavengeUI_PutItemInBag", self.player:getClothingItem_Back():getDisplayName(), getText("IGUI_ScavengeUI_Back")), self.player:getClothingItem_Back():getInventory());
    end
    self.bagOptions:setSelected(1, true);
end

function ISScavengeUI:updateZoneProgress(zoneClicked)
    if not zoneClicked then
        zoneClicked = ISScavengeAction.getScavengingZone(self.clickedSquare:getX(), self.clickedSquare:getY());
    end
    if zoneClicked and zoneClicked:getName() == "0" then
        self.zoneProgress = 0;
    end
    if zoneClicked and zoneClicked:getName() ~= "0" then
        self.zoneProgress = math.floor((tonumber(zoneClicked:getName()) / tonumber(zoneClicked:getOriginalName())) * 100);
    end
end

--************************************************************************--
--** ISScavengeUI:new
--**
--************************************************************************--
function ISScavengeUI:new(x, y, width, height, player, zone, clickedSquare)
    local o = {}
    if y == 0 then
        y = getPlayerScreenTop(player) + (getPlayerScreenHeight(player) - height) / 2
        y = y + 200;
    end
    if x == 0 then
        x = getPlayerScreenLeft(player) + (getPlayerScreenWidth(player) - width) / 2
    end
    o = ISPanelJoypad:new(x, y, width, height);
    setmetatable(o, self)
    self.__index = self
    o.borderColor = {r=0.4, g=0.4, b=0.4, a=1};
    o.backgroundColor = {r=0, g=0, b=0, a=0.6};
    o.width = width;
    o.titleY = 10
    o.barHgt = FONT_HGT_SMALL;
    o.height = height;
    o.player = getSpecificPlayer(player);
    o.fgBar = {r=0, g=0.6, b=0, a=0.7 }
    o.fgBarOrange = {r=1, g=0.3, b=0, a=0.7 }
    o.fgBarRed = {r=1, g=0, b=0, a=0.7 }
    o.zone = zone;
    o.clickedSquare = clickedSquare;
    o.moveWithMouse = true;
    o.buttonBorderColor = {r=0.7, g=0.7, b=0.7, a=0.5};
    o.itemsScavenged = {};
    o.maxItem = 4
    --    o.itemScavenged = nil;
    --    o.itemScavengedAlpha = 1;
    --    o.itemScavengedTimer = 0;
    o.zoneProgress = 100;
    return o;
end

function ISScavengeUI.OnPlayerDeath(playerObj)
    local ui = ISScavengeUI.windows[playerObj:getPlayerNum()+1]
    if ui then
        ui:removeFromUIManager()
        ISScavengeUI.windows[playerObj:getPlayerNum()+1] = nil
    end
end

Events.OnPlayerDeath.Add(ISScavengeUI.OnPlayerDeath)


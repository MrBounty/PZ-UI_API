require "Tutorial/TutorialStep"

local FLOOR_HIGHLIGHT_COLOR = ColorInfo.new(0,1,0,1.0);
local OBJECT_HIGHLIGHT_COLOR = ColorInfo.new(0,1,0,1);

---@class TutorialTests
TutorialTests = {}
TutorialTests.klight_x = 160;
TutorialTests.klight_y = 156;

TutorialTests.llight_x = 151;
TutorialTests.llight_y = 151;

TutorialTests.addHoming = function(sq, yoffset, xoffset, color)
    if not color then
        color = {r=0.8,g=0.8,b=0};
    end
    if not TutorialTests.homing1 then
        TutorialTests.homing1 = Tutorial1.marker:addPlayerHomingPoint(getPlayer(), sq:getX(), sq:getY(), color.r, color.g, color.b, 1);
        if yoffset then
            TutorialTests.homing1:setYOffsetScaled(yoffset);
        end
        if xoffset then
            TutorialTests.homing1:setXOffsetScaled(xoffset);
        end
    elseif not TutorialTests.homing2 then
        TutorialTests.homing2 = Tutorial1.marker:addPlayerHomingPoint(getPlayer(), sq:getX(), sq:getY(), color.r, color.g, color.b, 1);
        if yoffset then
            TutorialTests.homing2:setYOffsetScaled(yoffset);
        end
        if xoffset then
            TutorialTests.homing2:setXOffsetScaled(xoffset);
        end
    end
end

TutorialTests.addMarker = function(sq, size)
    if not TutorialTests.marker1 and not TutorialTests.homing1 then
        TutorialTests.marker1 = Tutorial1.marker:addGridSquareMarker("circle_center", "circle_only_highlight", sq, 0.8, 0.8,0,true, size, 0.02, 0.2, 1.0);
        TutorialTests.homing1 = Tutorial1.marker:addPlayerHomingPoint(getPlayer(), sq:getX(), sq:getY(), 0.8, 0.8, 0, 1);
    end
end

TutorialTests.stopHighlight = function(obj)
    obj:setHighlighted(false);
    obj:setBlink(false);
    obj:setOutlineHighlight(false);
    obj:setOutlineHlBlink(false);
end

TutorialTests.highlight = function(obj, thickness)
    obj:setHighlighted(true, false);
    obj:setHighlightColor(OBJECT_HIGHLIGHT_COLOR);
    obj:setBlink(true);
    obj:setOutlineHighlight(true);
    obj:setOutlineHlBlink(true);
    obj:setOutlineHighlightCol(1.0, 1.0, 1.0, 1.0);
    if thickness then
        obj:setOutlineThickness(thickness);
    end
end

TutorialTests.RemoveMarkers = function()
    if TutorialTests.homing1 then
        TutorialTests.homing1:remove();
        TutorialTests.homing1 = nil;
    end
    if TutorialTests.homing2 then
        TutorialTests.homing2:remove();
        TutorialTests.homing2 = nil;
    end
    if TutorialTests.marker1 then
        TutorialTests.marker1:remove();
        TutorialTests.marker1 = nil;
    end
end

TutorialTests.ZoomedIn = function()
    ISBackButtonWheel.disableZoomOut = true;
    ISBackButtonWheel.disableZoomIn = false;
    local complete = false;
    if JoypadState.players[1] then
        complete = getCore():getZoom(0) == 1;
    else
        complete = getCore():getZoom(0) == getCore():getMinZoom();
    end
    if complete then
        ISBackButtonWheel.disableZoomOut = true;
        ISBackButtonWheel.disableZoomIn = true;
        ISBackButtonWheel.disablePlayerInfo = false;
    end
    return complete;
end

TutorialTests.ZoomedOut = function()
    ISBackButtonWheel.disableZoomOut = false;
    local complete = getCore():getZoom(0) == getCore():getMaxZoom();
    if JoypadState.players[1] then
        complete = TutorialTests.currentZoom ~= getCore():getZoom(0);
    end
    if complete then
        TutorialTests.currentZoom = getCore():getZoom(0);
    end
    return complete;
end

TutorialTests.PlayerInfoOpen = function()
    ISBackButtonWheel.disablePlayerInfo = false;
    local complete = false;
    if ISCharacterInfoWindow.instance ~= nil and ISCharacterInfoWindow.instance:isReallyVisible() then
        complete = true;
    
        local characterInfo = getPlayerInfoPanel(0);
        characterInfo:toggleView(xpSystemText.health);
        setJoypadFocus(0, characterInfo.panel:getActiveView())
        updateJoypadFocus(JoypadState.players[1])
    end
    
    return complete;
end

TutorialTests.HealthOpen = function()
--    if JoypadState.players[1] then
--        local focus = getPlayerInfoPanel(0).panel:getActiveView()
--        setJoypadFocus(0, focus)
--    end
    ISEquippedItem.instance.healthBtn:setVisible(true);
    ISCharacterInfoWindow.instance.closeButton:setVisible(false);
    ISEquippedItem.instance.healthBtn.blinkImage = true;
    if ISHealthPanel.instance ~= nil and ISHealthPanel.instance:isReallyVisible() then
        ISEquippedItem.instance.healthBtn.blinkImage = false;
        ISEquippedItem.instance.healthBtn:setVisible(false);
        return true;
    end
    return false;
end

TutorialTests.SkillsPage = function()
    local focus = getPlayerInfoPanel(0).panel:getActiveView()
    if JoypadState.players[1] and getJoypadFocus(0) ~= getPlayerInfoPanel(0).panel:getActiveView() then
        Tutorial1.disableMsgFocus = true;
        setJoypadFocus(0, focus)
    end
    if ISCharacterInfoWindow.instance then
        ISCharacterInfoWindow.instance.panel.blinkTab = xpSystemText.skills;
    end
    if ISCharacterInfo.instance ~= nil and ISCharacterInfo.instance:isReallyVisible() then
        ISCharacterInfoWindow.instance.panel.blinkTab = nil;
        Tutorial1.disableMsgFocus = false;
        return true;
    end
    return false;
end

TutorialTests.NotSkillsPage = function()
    local focus = getPlayerInfoPanel(0).panel:getActiveView()
    if JoypadState.players[1] and getJoypadFocus(0) ~= getPlayerInfoPanel(0).panel:getActiveView() then
        Tutorial1.disableMsgFocus = true;
        setJoypadFocus(0, focus)
    end
    ISCharacterInfoWindow.instance.closeButton:setVisible(true);
    if ISCharacterInfoWindow.instance then
        ISCharacterInfoWindow.instance.closeButton.blinkImage = true;
    end
    if not ISCharacterInfoWindow.instance or not ISCharacterInfoWindow.instance:isReallyVisible() then
        if ISCharacterInfoWindow.instance then
            ISCharacterInfoWindow.instance.closeButton.blinkImage = false;
        end
        Tutorial1.disableMsgFocus = false;
        if JoypadState.players[1] then
            setJoypadFocus(0, nil);
        end
        return true;
    end
    return false;
end

TutorialTests.LookedAround = function()
    JoypadState.disableMovement = false;
    if getPlayer(0):getDir() == IsoDirections.W then return true; end
    return false;
end


WelcomeStep = TutorialStep:derive("WelcomeStep");
function WelcomeStep:new () local o = {} setmetatable(o, self)    self.__index = self    return o end

function WelcomeStep:begin()
    getPlayer():setAllowRun(false);
    ISEquippedItem.instance.healthBtn:setVisible(false);
    local klightSquare = getCell():getGridSquare(TutorialTests.klight_x, TutorialTests.klight_y, 0);
    local llightSquare = getCell():getGridSquare(TutorialTests.llight_x, TutorialTests.llight_y, 0);
    klightSquare:switchLight(true);
    llightSquare:switchLight(false);
    
    getPlayer():setIgnoreAutoVault(true);
    getPlayer():setAuthorizeMeleeAction(false);
    getPlayer():setIgnoreInputsForDirection(true);
    getPlayer():setIgnoreContextKey(true);

    self:addMessage(getText("IGUI_Tutorial1_Welcome1"), getCore():getScreenWidth()/2, getCore():getScreenHeight()/2, 500, 160, true);
    
    if JoypadState.players[1] then
        self:addMessage(getText("IGUI_Tutorial1_Welcome3Joypad"), getCore():getScreenWidth()/2, 400, 500, 500, false, TutorialTests.PlayerInfoOpen);
        self:addMessage(getText("IGUI_Tutorial1_Welcome4"), 870, 460, 410, 170, true);
        self:addMessage(getText("IGUI_Tutorial1_Welcome5Joypad"), 870, 300, 290, 90, false, TutorialTests.SkillsPage);
        self:addMessage(getText("IGUI_Tutorial1_Welcome6"), 870, 300, 340, 120, true);
        self:addMessage(getText("IGUI_Tutorial1_Welcome7"), 870, 300, 310, 210, true);
        self:addMessage(getText("IGUI_Tutorial1_Welcome8Joypad"), 870, 300, 300, 100, false, TutorialTests.NotSkillsPage);
        self:addMessage(getText("IGUI_Tutorial1_Welcome9Joypad"), 300, 620, 420, 100, false, TutorialTests.LookedAround);
    else
        self:addMessage(getText("IGUI_Tutorial1_Welcome2",string.lower(getKeyName(getCore():getKey("Zoom in"))),string.lower(getKeyName(getCore():getKey("Zoom out")))), 300, 620, 420, 150, false, TutorialTests.ZoomedOut);
        self:addMessage(getText("IGUI_Tutorial1_Welcome2bis"), 300, 620, 420, 80, false, TutorialTests.ZoomedIn);
        self:addMessage(getText("IGUI_Tutorial1_Welcome3"), 250, 100, 300, 130, false, TutorialTests.HealthOpen);
        self:addMessage(getText("IGUI_Tutorial1_Welcome4"), 670, 460, 410, 170, true);
        self:addMessage(getText("IGUI_Tutorial1_Welcome5"), 650, 300, 290, 90, false, TutorialTests.SkillsPage);
        self:addMessage(getText("IGUI_Tutorial1_Welcome6"), 660, 290, 340, 120, true);
        self:addMessage(getText("IGUI_Tutorial1_Welcome7"), 580, 450, 310, 210, true);
        self:addMessage(getText("IGUI_Tutorial1_Welcome8"), 580, 420, 300, 100, false, TutorialTests.NotSkillsPage);
        self:addMessage(getText("IGUI_Tutorial1_Welcome9"), 300, 620, 420, 100, false, TutorialTests.LookedAround);
    end

    self:addMessage(getText("IGUI_Tutorial1_Welcome10"), 300, 620, 420, 160, true);

    self:doMessage();
end

function WelcomeStep:isComplete()
    return TutorialStep.isComplete(self);
end

function WelcomeStep:finish()
    WelcomeStep.finished = true;
    TutorialStep.finish(self);
end


WalkToAdjacent = TutorialStep:derive("WalkToAdjacent");
function WalkToAdjacent:new () local o = {} setmetatable(o, self)    self.__index = self    return o end

WalkToAdjacent.otherRoomInLocX = 152;
WalkToAdjacent.otherRoomInLocY = 153;
WalkToAdjacent.otherRoomY1 = 156;
WalkToAdjacent.otherRoomX1 = 153;
WalkToAdjacent.otherRoomY1 = 156;
WalkToAdjacent.otherRoomX2 = 155;
WalkToAdjacent.otherRoomY2 = 158;
WalkToAdjacent.highlightFloor = nil
WalkToAdjacent.lastPlayerX = -1;
WalkToAdjacent.lastPlayerY = -1;
WalkToAdjacent.runned = false;
WalkToAdjacent.x2 = 157;
WalkToAdjacent.y2 = 153;
WalkToAdjacent.sneaked = false;
WalkToAdjacent.appleContainer = nil;
WalkToAdjacent.z = 0;

function WalkToAdjacent:begin()
    getPlayer():setAllowRun(false);
    if not WelcomeStep.finished and getCore():getDebug() then
        JoypadState.disableControllerPrompt = true;
        JoypadState.disableMovement = false;
        getPlayer():setAllowRun(true);
    end
    getPlayer():setIgnoreInputsForDirection(false);
    getPlayer():setAuthorizeMeleeAction(false);
    ISEquippedItem.instance.healthBtn:setVisible(true);
    
    TutorialTests.addMarker(getSquare(WalkToAdjacent.otherRoomInLocX, WalkToAdjacent.otherRoomInLocY, 0), 2);
    
    if JoypadState.players[1] then
        self:addMessage(getText("IGUI_Tutorial1_WalkTo1Joypad"),  300, 620, 420, 130, false, WalkToAdjacent.inLoc);
        self:addMessage(getText("IGUI_Tutorial1_WalkTo2Joypad"),  300, 620, 420, 130, false, WalkToAdjacent.strafed);
    else
        self:addMessage(getText("IGUI_Tutorial1_WalkTo1", Tutorial1.moveKeys),  300, 620, 420, 130, false, WalkToAdjacent.inLoc);
        self:addMessage(getText("IGUI_Tutorial1_WalkTo2", Tutorial1.moveKeys),  300, 620, 420, 130, false, WalkToAdjacent.strafed);
    end
    self:addMessage(getText("IGUI_Tutorial1_WalkTo3"),  300, 620, 420, 130, false, WalkToAdjacent.inLoc2);

    local shiftKey = getKeyName(getCore():getKey("Run"))
    

    local llightSquare = getCell():getGridSquare(TutorialTests.llight_x, TutorialTests.llight_y, 0);
    llightSquare:switchLight(true);

    self:doMessage();
end
function WalkToAdjacent:inLoc()
    local complete = getPlayer():getCurrentSquare():getX() == WalkToAdjacent.otherRoomInLocX and getPlayer():getCurrentSquare():getY() == WalkToAdjacent.otherRoomInLocY and getPlayer():getZ() == 0;
    if complete then
        TutorialTests.RemoveMarkers();
    end
    return complete;
end

function WalkToAdjacent:strafed()
    if not WalkToAdjacent.sneaked and getPlayer():isAiming() then
        WalkToAdjacent.sneaked = true;
        WalkToAdjacent.lastPlayerX = getPlayer():getX();
        WalkToAdjacent.lastPlayerY = getPlayer():getY();
    end
    
    if getPlayer():isAiming() or WalkToAdjacent.sneaked then
        getPlayer():setIgnoreInputsForDirection(false);
    else
        getPlayer():setIgnoreInputsForDirection(true);
    end

    -- highlight the corner the player has to stand near
    if not WalkToAdjacent.appleContainer and (math.abs(WalkToAdjacent.lastPlayerX - getPlayer():getX()) > 1 or math.abs(WalkToAdjacent.lastPlayerY - getPlayer():getY()) > 1) and getPlayer():isAiming() then
        local sq = getCell():getGridSquare(156, 154, 0);
        local objs = sq:getObjects();
        for i = 0, objs:size()-1 do
            local o = objs:get(i);
            local c = o:getContainer();
            if c ~= nil then
                WalkToAdjacent.appleContainer = o;
                TutorialTests.highlight(WalkToAdjacent.appleContainer, 0.3);
            end
        end
        getSoundManager():PlayWorldSoundImpl("TutorialZombie", false, FightStep.zombieMomSpawnX, FightStep.zombieMomSpawnY, 0, 0, 20, 1, false);
    
        TutorialTests.addHoming(getSquare(156, 154, 0), -50);
    
        return true;
    end
    return false;
end

function WalkToAdjacent:inLoc2()
    local complete = math.abs(getPlayer():getX() - WalkToAdjacent.x2) > 0 and math.abs(getPlayer():getX() - WalkToAdjacent.x2) < 0.9 and math.abs(getPlayer():getCurrentSquare():getY() - WalkToAdjacent.y2) <= 1  and getPlayer():getZ() == 0;
    if complete then
        TutorialTests.RemoveMarkers();
        TutorialTests.stopHighlight(WalkToAdjacent.appleContainer);
        getPlayer():setIgnoreInputsForDirection(true);
    end
    return complete;
end

function WalkToAdjacent:isComplete()
    return TutorialStep.isComplete(self);
end

function WalkToAdjacent:finish()
    WalkToAdjacent.finished = true;
    TutorialStep.finish(self);
end

InventoryLootingStep = TutorialStep:derive("InventoryLootingStep");
InventoryLootingStep.itemToEat = "DeadMouse";
function InventoryLootingStep:new () local o = {} setmetatable(o, self)    self.__index = self    return o end

function InventoryLootingStep:begin()
    if not WalkToAdjacent.finished and getCore():getDebug() then
        JoypadState.disableMovement = false;
        getPlayer():setAllowRun(false);
        getPlayer():setAuthorizeMeleeAction(false);
        getPlayer():setX(157);
        getPlayer():setY(154);
    end
    ISInventoryPaneContextMenu.dontCreateMenu = true;

    getPlayer():setDir(IsoDirections.W);
    
    local sq = getCell():getGridSquare(156, 154, 0);
    if sq ~= nil then
        local objs = sq:getObjects();
        for i = 0, objs:size()-1 do
            local o = objs:get(i);
            local c = o:getContainer();
            if c ~= nil then
                local mouse = c:AddItem("Base.DeadMouse");
                mouse:setAge(17);
                Tutorial1.DeadMouse = mouse;
                c:AddItem("Base.WaterBottleEmpty");
                InventoryLootingStep.container = o
                getPlayerLoot(0).inventoryPane.highlightItem = "DeadMouse";
                break;
            end
        end
    end
    
    
    if JoypadState.players[1] then
        JoypadState.disableYInventory = false;
        JoypadState.disableControllerPrompt = false;
        self:addMessage(getText("IGUI_Tutorial1_InvLoot1Joypad"),  700, 120, 520, 80, false, InventoryLootingStep.openInventoryJoypad);
        self:addMessage(getText("IGUI_Tutorial1_InvLoot2Joypad"),  700, 420, 420, 100, false, InventoryLootingStep.focusCorrectPanel);
        self:addMessage(getText("IGUI_Tutorial1_InvLoot3Joypad"),  700, 420, 420, 100, false, InventoryLootingStep.haveItem);
        self:addMessage(getText("IGUI_Tutorial1_InvLoot4Joypad"),  700, 420, 420, 100, false, InventoryLootingStep.haveWater);
    else
        self:addMessage(getText("IGUI_Tutorial1_InvLoot1"),  700, 120, 520, 80, false, InventoryLootingStep.focusLootingPanel);
        self:addMessage(getText("IGUI_Tutorial1_InvLoot2"),  700, 420, 420, 100, false, InventoryLootingStep.haveItem);
        self:addMessage(getText("IGUI_Tutorial1_InvLoot3"),  700, 420, 420, 90, false, InventoryLootingStep.haveWater);
        getPlayerInventory(0):setVisible(true);
        getPlayerLoot(0):setVisible(true);
        getPlayerLoot(0).blink = true;
    end


    self:doMessage();
end

function InventoryLootingStep:openInventoryJoypad()
    local complete = getPlayerLoot(0) ~= nil and getPlayerLoot(0):isVisible();
    if complete then
        getPlayerLoot(0):setBlinkingContainer(true);
        JoypadState.disableYInventory = true;
        JoypadState.disableControllerPrompt = true;
    end
    return complete;
end

function InventoryLootingStep:focusCorrectPanel()
    for i,v in ipairs(getPlayerLoot(0).backpacks) do
        if v.inventory:contains(InventoryLootingStep.itemToEat) and getPlayerLoot(0).inventoryPane.inventory == v.inventory then
            return true;
        end
    end
    return false;
end

function InventoryLootingStep:focusLootingPanel()
    local isOpen = getPlayerLoot(0) ~= nil and not getPlayerLoot(0).isCollapsed;
    if isOpen then
        getPlayerLoot(0).blink = false;
        if not JoypadState.players[1] then
            getPlayerLoot(0):setPinned();
        end
        for i,v in ipairs(getPlayerLoot(0).backpacks) do
            if v.inventory:contains(InventoryLootingStep.itemToEat) then
                getPlayerLoot(0).inventoryPane.inventory = v.inventory;
                getPlayerLoot(0).title = v.name;
                getPlayerLoot(0).capacity = v.capacity;
                getPlayerLoot(0):refreshBackpacks();
            end
        end
    end
    return isOpen;
end

function InventoryLootingStep:haveItem()
    getPlayer():setIgnoreAimingInput(true);
    getPlayer():faceThisObject(Tutorial1.DeadMouseContainer);
    JoypadState.disableGrab = false;
    getPlayerLoot(0).inventoryPane.highlightItem = InventoryLootingStep.itemToEat;
    if getPlayer():getInventory():contains(InventoryLootingStep.itemToEat) then
        if not JoypadState.players[1] then
            getPlayerInventory(0):setPinned();
        end
        getPlayerLoot(0).inventoryPane.highlightItem = "";
        getPlayerLoot(0).inventoryPane.selected = {};
        return true;
    end
    return false;
end

function InventoryLootingStep:haveWater()
    getPlayer():faceThisObject(Tutorial1.DeadMouseContainer);
    getPlayerLoot(0).inventoryPane.highlightItem = "WaterBottleEmpty";
    if getPlayer():getInventory():contains("WaterBottleEmpty") then
        getPlayerLoot(0).inventoryPane.highlightItem = "";
        getPlayerLoot(0).inventoryPane.selected = {};
        getPlayer():setIgnoreInputsForDirection(false);
        getPlayer():setIgnoreAimingInput(false);
        return true;
    end
    return false;
end

function InventoryLootingStep:isComplete()
    return TutorialStep.isComplete(self);
end

function InventoryLootingStep:finish()
    TutorialStep.finish(self);
    InventoryLootingStep.finished = true;
end

InventoryUseStep = TutorialStep:derive("InventoryUseStep");
InventoryUseStep.sinkX = 156;
InventoryUseStep.sinkY = 154;
InventoryUseStep.sink = nil;
InventoryUseStep.lastInventory = nil;
InventoryUseStep.clickedOnInventory = false;
InventoryUseStep.panContainer = nil;
function InventoryUseStep:new () local o = {} setmetatable(o, self)    self.__index = self    return o end

function InventoryUseStep:begin()
    if getCore():getDebug() and not InventoryLootingStep.finished then
        getPlayer():setAuthorizeMeleeAction(false);
        getPlayer():setIgnoreInputsForDirection(false);
        Tutorial1.DeadMouse = getPlayer():getInventory():AddItem("Base.DeadMouse");
        getPlayer():getInventory():AddItem("Base.WaterBottleEmpty");
        if not JoypadState.players[1] then
            getPlayerInventory(0):setVisible(true);
            getPlayerLoot(0):setVisible(true);
        end
        getPlayerLoot(0).lootAll:setVisible(false);
    end
    ISInventoryPaneContextMenu.dontCreateMenu = false;
    Tutorial1.contextMenuEat = true;
    JoypadState.disableGrab = true;
    
    if JoypadState.players[1] then
        JoypadState.disableYInventory = false;
        JoypadState.disableControllerPrompt = false;
        JoypadState.disableInvInteraction = false;
        self:addMessage(getText("IGUI_Tutorial1_InvUse1Joypad"),  getCore():getScreenWidth() - 430, 160, 420, 130, false, InventoryUseStep.selectInventory);
        self:addMessage(getText("IGUI_Tutorial1_InvUse1BisJoypad"),  getCore():getScreenWidth() - 430, 160, 420, 130, false, InventoryUseStep.eat);
        self:addMessage(getText("IGUI_Tutorial1_InvUse2Joypad"),  getCore():getScreenWidth() - 430, 160, 260, 80, true);
        self:addMessage(getText("IGUI_Tutorial1_InvUse2BisJoypad"),  300, 520, 320, 130, false, InventoryUseStep.fillBottle);
        self:addMessage(getText("IGUI_Tutorial1_InvUse3Joypad"),  300, 520, 470, 110, false, InventoryUseStep.InLocJoypad);
        self:addMessage(getText("IGUI_Tutorial1_InvUse3BisJoypad"),  300, 520, 470, 110, false, InventoryLootingStep.openInventoryJoypad);
        self:addMessage(getText("IGUI_Tutorial1_InvUse4Joypad"),  300, 520, 320, 130, false, InventoryUseStep.seeWeapon);
        self:addMessage(getText("IGUI_Tutorial1_InvUse5Joypad"),  500, 450, 320, 100, false, InventoryUseStep.lootWeapon);
    else
        self:addMessage(getText("IGUI_Tutorial1_InvUse1"),  getCore():getScreenWidth() - 430, 160, 420, 130, false, InventoryUseStep.eat);
        self:addMessage(getText("IGUI_Tutorial1_InvUse2"),  getCore():getScreenWidth() - 430, 160, 260, 80, true);
        self:addMessage(getText("IGUI_Tutorial1_InvUse2Bis"),  300, 520, 320, 130, false, InventoryUseStep.fillBottle);
        self:addMessage(getText("IGUI_Tutorial1_InvUse3"),  300, 520, 470, 110, false, InventoryUseStep.focusLootingPanel);
        self:addMessage(getText("IGUI_Tutorial1_InvUse4"),  300, 520, 320, 130, false, InventoryUseStep.seeWeapon);
        self:addMessage(getText("IGUI_Tutorial1_InvUse5"),  500, 450, 320, 100, false, InventoryUseStep.lootWeapon);
    end

    getPlayer():getStats():setHunger(0.2);

    self:doMessage();
end

function InventoryUseStep:InLocJoypad()
    local complete = getPlayer():getCurrentSquare():getX() == 159 and getPlayer():getCurrentSquare():getY() == 153 and getPlayer():getZ() == 0;
    if complete then
        TutorialTests.RemoveMarkers();
        JoypadState.disableYInventory = false;
    end
    return complete;
end

function InventoryUseStep:selectInventory()
    local complete = getJoypadFocus(0) == getPlayerInventory(0);
    if complete then
        JoypadState.disableInvInteraction = false;
    end
    return complete;
end

function InventoryUseStep:focusLootingPanel()
    local isOpen = getPlayerLoot(0) ~= nil and not getPlayerLoot(0).isCollapsed;
    if isOpen then
        getPlayerLoot(0).blink = false;
        if not JoypadState.players[1] then
            getPlayerLoot(0):setPinned();
        end
    else
        getPlayerLoot(0).blink = true;
    end
    return isOpen;
end

function InventoryUseStep:eat()
    getCore():setBlinkingMoodle("Hungry");
    ISInventoryPaneContextMenu.blinkOption = getText("ContextMenu_Eat");
    getPlayer():getBodyDamage():setBoredomLevel(0);
    getPlayer():getBodyDamage():setUnhappynessLevel(0);
    local itemToEat = getPlayer():getInventory():FindAndReturn(InventoryLootingStep.itemToEat);
    if not InventoryUseStep.dontTurn and getPlayer():getVariableString("PerformingAction") == "Eat" then
        SneakStep.setZoom(0.25);
        getPlayer():setDir(IsoDirections.SE);
        InventoryUseStep.dontTurn = true;
    end
    -- add BLOOD!
    if getPlayer():getVariableString("PerformingAction") == "Eat" then
        if itemToEat:getJobDelta() > 0.12 and not InventoryUseStep.bloodMouse1 then
            getPlayer():addBlood(BloodBodyPartType.Head, true, true, false);
            getPlayer():addBlood(BloodBodyPartType.Neck, true, true, false);
            getPlayer():addBlood(BloodBodyPartType.Hand_R, true, true, false);
            getPlayer():addBlood(BloodBodyPartType.Hand_L, true, true, false);
            getPlayer():addBlood(BloodBodyPartType.Torso_Upper, true, true, false);
            InventoryUseStep.bloodMouse1 = true;
        end
    end
    if itemToEat and InventoryUseStep.dontTurn then
       if math.abs(itemToEat:getHungChange()) < 0.09 then -- eat 1/4
           if TutorialMessage.instance then
               TutorialMessage.instance.richtext.text = getText("IGUI_Tutorial1_InvUse6");
               TutorialMessage.instance.richtext:paginate();
           end
       end
       if math.abs(itemToEat:getHungChange()) < 0.051 then -- eat 1/2
           if TutorialMessage.instance then
               TutorialMessage.instance.richtext.text = getText("IGUI_Tutorial1_InvUse7");
               TutorialMessage.instance.richtext:paginate();
           end
       end
    end
    local isOpen = getPlayerInventory(0) ~= nil and not getPlayerInventory(0).isCollapsed;
    if isOpen then
        getPlayerInventory(0).blink = false;
    else
        getPlayerInventory(0).blink = true;
    end
    getPlayerInventory(0).inventoryPane.highlightItem = InventoryLootingStep.itemToEat;
    local complete = not itemToEat;
    if complete then
        Tutorial1.closeBothInv();
        JoypadState.disableYInventory = true;
        getPlayer():getBodyDamage():setFoodSicknessLevel(40);
        getCore():setBlinkingMoodle("Sick");
    end
    return complete;
end

function InventoryUseStep:fillBottle()
    if not TutorialTests.homing1 then
        TutorialTests.addHoming(getSquare(InventoryUseStep.sinkX, InventoryUseStep.sinkY, 0), -55);
    end
    getCore():setBlinkingMoodle(nil);
    Tutorial1.contextMenuFillBottle = true;
    ISInventoryPaneContextMenu.blinkOption = nil;
    local emptybottle = getPlayer():getInventory():FindAndReturn("WaterBottleEmpty") or getPlayer():getInventory():FindAndReturn("WaterBottleFull");
    if emptybottle then
        ISWorldObjectContextMenu.blinkOption = getText("ContextMenu_Fill") .. emptybottle:getName();
    end
    if not InventoryUseStep.sink then
        local sq = getCell():getGridSquare(InventoryUseStep.sinkX, InventoryUseStep.sinkY, 0);
        local objs = sq:getObjects();
        for i = 0, objs:size()-1 do
            local o = objs:get(i);
            if o:getSprite():getName() == "fixtures_sinks_01_16" then
                InventoryUseStep.sink = o;
                TutorialTests.highlight(InventoryUseStep.sink, 0.1);
                break
            end
        end
    end


    local bottle = getPlayer():getInventory():FindAndReturn("WaterBottleFull");
    if bottle and bottle:getUsedDelta() == 1 then
        InventoryUseStep.lastInventory = getPlayerLoot(0).inventoryPane.inventory;
        Tutorial1.contextMenuFillBottle = false;
        TutorialTests.RemoveMarkers();
        TutorialTests.stopHighlight(InventoryUseStep.sink);
        local sq = getCell():getGridSquare(159, 152, 0);
        if sq ~= nil then
            local objs = sq:getObjects();
            for i = 0, objs:size()-1 do
                local o = objs:get(i);
                local c = o:getContainer();
                if c ~= nil then
                    c:AddItem(InventoryUseStep.spawnPan());
                    InventoryUseStep.panContainer = o;
                    getPlayerLoot(0).inventoryPane.highlightItem = "Pan";
                    TutorialTests.addHoming(getSquare(159, 152, 0), -50);
                    TutorialTests.highlight(InventoryUseStep.panContainer, 0.3);
                    break;
                end
            end
        end
        return true;
    end
    return false;
end

function InventoryUseStep.spawnPan()
    local pan = InventoryItemFactory.CreateItem("Base.Pan");
    pan:setMinDamage(0.1);
    pan:setMaxDamage(0.1);
    pan:setCondition(3);
    return pan;
end

function InventoryUseStep:seeWeapon()
    ISWorldObjectContextMenu.blinkOption = nil;
    Tutorial1.contextMenuEquipPrimary = true;
    local isOpen = getPlayerLoot(0) ~= nil and not getPlayerLoot(0).isCollapsed;
    if isOpen then
        getPlayerLoot(0).blink = false;
--        if not InventoryUseStep.clickedOnInventory then
            getPlayerLoot(0):setBlinkingContainer(true);
--        end
    else
        getPlayerLoot(0).blink = true;
    end
    local complete = getPlayerLoot(0).inventoryPane.inventory:contains("Pan") and not getPlayerLoot(0).isCollapsed;
    if complete then
--        TutorialTests.RemoveMarkers();
--        TutorialTests.stopHighlight(InventoryUseStep.panContainer);
    end
    return complete;
end

function InventoryUseStep:lootWeapon()
    ISInventoryPaneContextMenu.blinkOption = getText("ContextMenu_Equip_Primary");
    local playerLootOpen = not getPlayerLoot(0).isCollapsed and getPlayerLoot(0).inventoryPane.inventory:contains("Pan");
    local playerInvOpen = not getPlayerInventory(0).isCollapsed;
    getPlayerLoot(0).inventoryPane.highlightItem = "Pan";
    getPlayerInventory(0).inventoryPane.highlightItem = "Pan";
    getPlayerLoot(0):setBlinkingContainer(false);
    if playerLootOpen then
        getPlayerLoot(0).blink = false;
    elseif getPlayerLoot(0).inventoryPane.inventory:contains("Pan") then
        getPlayerLoot(0).blink = true;
    end
    if playerInvOpen then
        getPlayerInventory(0).blink = false;
    elseif getPlayerInventory(0).inventoryPane.inventory:contains("Pan") then
        getPlayerInventory(0).blink = true;
    end
    if getPlayer():getInventory():contains("Pan") then
        TutorialTests.RemoveMarkers();
        TutorialTests.stopHighlight(InventoryUseStep.panContainer);
    end
    if getPlayer():getPrimaryHandItem() and getPlayer():getPrimaryHandItem():getType() == "Pan" then
        getPlayerLoot(0).inventoryPane.highlightItem = nil;
        getPlayerInventory(0).inventoryPane.highlightItem = nil;
        TutorialTests.stopHighlight(InventoryUseStep.panContainer);
        Tutorial1.closeBothInv();
        JoypadState.disableYInventory = false;
        
        return true;
    end
    return false;
end

function InventoryUseStep:isComplete()
    return TutorialStep.isComplete(self);
end

function InventoryUseStep:finish()
    InventoryUseStep.finished = true;
    ISInventoryPaneContextMenu.blinkOption = nil;
    TutorialStep.finish(self);
end

FightStep = TutorialStep:derive("FightStep");
FightStep.windowX = 162;
FightStep.windowY = 154;
FightStep.window = nil;
FightStep.climbThrough = false;
FightStep.zombieMomSpawnX = 165;
FightStep.zombieMomSpawnY = 154;
FightStep.momzombie = nil;
FightStep.zombieSawYou = false;
FightStep.highlightFloor = nil;
function FightStep:new () local o = {} setmetatable(o, self)    self.__index = self    return o end

function FightStep:begin()
    JoypadState.disableControllerPrompt = false;
    JoypadState.disableMovement = false;
    getPlayer():setIgnoreAutoVault(true);
    getPlayer():setAuthorizeMeleeAction(false);
    getPlayer():setIgnoreContextKey(true);
    JoypadState.disableInvInteraction = false;
    if getCore():getDebug() and not InventoryUseStep.finished then
        getPlayer():setIgnoreInputsForDirection(false);
        local pan = getPlayer():getInventory():AddItem(InventoryUseStep.spawnPan());
        getPlayer():setPrimaryHandItem(pan);
        if not JoypadState.players[1] then
            getPlayerInventory(0):setVisible(true);
            getPlayerLoot(0):setVisible(true);
        end
        getPlayerLoot(0).lootAll:setVisible(false);
    end
    
    FightStep:spawnMom();
    
    if JoypadState.players[1] then
        self:addMessage(getText("IGUI_Tutorial1_Fight1"),  300, 500, 520, 110, false, FightStep.WalkToWindow);
        self:addMessage(getText("IGUI_Tutorial1_Fight2Joypad"),  300, 520, 520, 100, false, FightStep.OpenWindow);
        self:addMessage(getText("IGUI_Tutorial1_Fight3Joypad"),  getCore():getScreenWidth()/2, getCore():getScreenHeight()/2 + 250, 520, 85, false, FightStep.ClimbThroughWindow);
        self:addMessage(getText("IGUI_Tutorial1_Fight4Joypad"),  getCore():getScreenWidth()/2, getCore():getScreenHeight()/2 + 350, 520, 120, false, FightStep.IsAiming);
        self:addMessage(getText("IGUI_Tutorial1_Fight5Joypad"),  getCore():getScreenWidth()/2, getCore():getScreenHeight()/2 + 350, 520, 90, false, FightStep.HitZombie);
        self:addMessage(getText("IGUI_Tutorial1_Fight7Joypad"),  getCore():getScreenWidth()/2, getCore():getScreenHeight()/2 + 350, 520, 100, false, FightStep.KillZombie);
        self:addMessage(getText("IGUI_Tutorial1_Fight8NewJoypad"),  getCore():getScreenWidth()/2, getCore():getScreenHeight()/2 + 350, 520, 100, false, FightStep.LootKnife);
    else
        self:addMessage(getText("IGUI_Tutorial1_Fight1"),  300, 500, 520, 110, false, FightStep.WalkToWindow);
        self:addMessage(getText("IGUI_Tutorial1_Fight2", getKeyName(getCore():getKey("Interact"))),  300, 520, 520, 100, false, FightStep.OpenWindow);
        self:addMessage(getText("IGUI_Tutorial1_Fight3", getKeyName(getCore():getKey("Interact"))),  getCore():getScreenWidth()/2, getCore():getScreenHeight()/2 + 250, 520, 85, false, FightStep.ClimbThroughWindow);
        self:addMessage(getText("IGUI_Tutorial1_Fight4"),  getCore():getScreenWidth()/2, getCore():getScreenHeight()/2 + 350, 520, 120, false, FightStep.IsAiming);
        self:addMessage(getText("IGUI_Tutorial1_Fight5", Tutorial1.moveKeys),  getCore():getScreenWidth()/2, getCore():getScreenHeight()/2 + 350, 520, 90, false, FightStep.HitZombie);
        self:addMessage(getText("IGUI_Tutorial1_Fight7", getKeyName(getCore():getKey("Melee"))),  getCore():getScreenWidth()/2, getCore():getScreenHeight()/2 + 350, 520, 100, true);
        self:addMessage(getText("IGUI_Tutorial1_Fight7B"),  getCore():getScreenWidth()/2, getCore():getScreenHeight()/2 + 350, 520, 100, false, FightStep.KillZombie);
        self:addMessage(getText("IGUI_Tutorial1_Fight8New"),  getCore():getScreenWidth()/2, getCore():getScreenHeight()/2 + 350, 520, 100, false, FightStep.LootKnife);
    end

    local sq = getCell():getGridSquare(FightStep.windowX, FightStep.windowY, 0);
    if sq ~= nil then
        local objs = sq:getObjects();
        for i = 0, objs:size()-1 do
            local o = objs:get(i);
            if instanceof(o, "IsoWindow") then
                FightStep.window = o;
                TutorialTests.highlight(FightStep.window, 0.1);
                FightStep.floor = o:getOppositeSquare():getFloor();
                break;
            end
        end
    end
    
    TutorialTests.addHoming(getSquare(162, 154, 0), -60, -20);

    self:doMessage();
end

function FightStep:WalkToWindow()
    -- OpenWindowState will slide the player to the center of the window
    local complete = math.abs(getPlayer():getX() - FightStep.windowX) <= 0.6 and (math.abs(getPlayer():getY() - FightStep.windowY) > 0.20 and math.abs(getPlayer():getY() - FightStep.windowY) < 0.8);
    if complete then
        TutorialTests.RemoveMarkers();
--        TutorialTests.addHoming(getSquare(161, 154, 0), true);
    end
    return complete;
end

function FightStep:spawnMom()
    FightStep.momzombie = addZombiesInOutfit(FightStep.zombieMomSpawnX, FightStep.zombieMomSpawnY, 0, 1, "TutorialMom", 100):get(0);
    FightStep.momzombie:setDir(IsoDirections.E)
    FightStep.momzombie:getVisual():setHairModel("Bob");
    local immutableColor = ImmutableColor.new(0.805, 0.750, 0.850, 1)
    FightStep.momzombie:getVisual():setHairColor(immutableColor)
    FightStep.momzombie:getVisual():setSkinTextureIndex(2);
    local knife = InventoryItemFactory.CreateItem("Base.HuntingKnife");
    FightStep.momzombie:setAttachedItem("Knife in Back", knife);
    FightStep.momzombie:resetModelNextFrame();
    
    Events.OnZombieDead.Add(FightStep.OnMomDead);

    for i=0, 10 do
        FightStep.momzombie:addBlood(nil, false, true, false);
        FightStep.momzombie:addHole(nil);
    end
    
    FightStep.momzombie:setUseless(true);
    FightStep.momzombie:setNoDamage(true);
end

function FightStep:OpenWindow()
    getPlayer():setDir(IsoDirections.E);
    getPlayer():setIgnoreContextKey(false);
    if FightStep.window:IsOpen() then
        FightStep.window:setHighlighted(false)
        FightStep.floor:setHighlighted(false)
        return true;
    end
    return false;
end

FightStep.wasOpen = true;

function FightStep:ClimbThroughWindow()
    if not FightStep.climbThrough then
        getPlayer():setIgnoreInputsForDirection(true);
        getPlayer():setDir(IsoDirections.E);
    else
        getPlayer():setIgnoreInputsForDirection(false);
    end
    if not FightStep.window:IsOpen() then
        FightStep.wasOpen = false;
        if TutorialMessage.instance then
            if JoypadState.players[1] then
                TutorialMessage.instance.richtext.text = getText("IGUI_Tutorial1_Fight3BisJoypad");
            else
                TutorialMessage.instance.richtext.text = getText("IGUI_Tutorial1_Fight3Bis", getKeyName(getCore():getKey("Interact")));
            end
            TutorialMessage.instance.richtext:paginate();
        end
    end
    if not FightStep.wasOpen and FightStep.window:IsOpen() then
        FightStep.wasOpen = true;
        TutorialMessage.instance.richtext.text = getText("IGUI_Tutorial1_Fight3", getKeyName(getCore():getKey("Interact")));
        TutorialMessage.instance.richtext:paginate();
    end
    if getPlayer():getCurrentState():equals(ClimbThroughWindowState.instance()) then
        FightStep.climbThrough = true;
    end
    if FightStep.climbThrough and getPlayer():getCurrentState():equals(IdleState.instance()) and getPlayer():getCurrentSquare():getX() == 162 and getPlayer():getCurrentSquare():getY() == 154 then
        TutorialTests.RemoveMarkers();
        TutorialTests.stopHighlight(FightStep.window);
        return true;
    end
    FightStep.playerX = getPlayer():getX();
    FightStep.playerY = getPlayer():getY();
    return false;
end

function FightStep:IsAiming()
    getPlayer():setIgnoreContextKey(true);
    local complete = getPlayer():isAiming();
    if complete then
        TutorialTests.addHoming(FightStep.momzombie:getCurrentSquare(), -20);
    end
    return complete;
end

function FightStep:OnMomDead(zed)
    Events.OnZombieDead.Remove(FightStep.OnMomDead);
    FightStep.momDead = true;
end

function FightStep:HitZombie()
    getPlayer():setBannedAttacking(false);
    getPlayer():setAuthorizeMeleeAction(true);
    getPlayer():setAuthorizeShoveStomp(false);
    FightStep.momzombie:setReanimateTimer(999);
    FightStep.momzombie:setAlwaysKnockedDown(true);
    FightStep.momzombie:setImmortalTutorialZombie(false);
    if (FightStep.momzombie:getCurrentState() == nil) then
        return false;
    end
    local complete = FightStep.momzombie:getCurrentState() == ZombieOnGroundState.instance();
    if FightStep.momzombie:getHitReaction() ~= "" then
        getPlayer():setAuthorizeMeleeAction(false);
    end
    if complete then
        getPlayer():setAuthorizeMeleeAction(false);
        FightStep.momzombie:setImmortalTutorialZombie(true);
        local floor = getCell():getGridSquare(164, 154, 0):getFloor();
        floor:setHighlighted(false);
    end
    return complete;
end

function FightStep:KillZombie()
    if not getPlayer():getPrimaryHandItem() then
        for i=0,getPlayer():getInventory():getItems():size()-1 do
            local pan = getPlayer():getInventory():getItems():get(i);
            if pan:getType() == "Pan" then
                getPlayer():getInventory():Remove(pan);
            end;
        end;
        local pan = getPlayer():getInventory():AddItem(InventoryUseStep.spawnPan());
        getPlayer():setPrimaryHandItem(pan);
    end;
    getPlayer():getPrimaryHandItem():setCondition(3);
    getPlayer():getPrimaryHandItem():setMinDamage(3);
    getPlayer():getPrimaryHandItem():setMaxDamage(3);
    local floor = getCell():getGridSquare(164, 154, 0):getFloor();
    floor:setHighlighted(false);
    FightStep.momzombie:setImmortalTutorialZombie(false);
    FightStep.momzombie:setNoDamage(false);
    getCore():setFlashIsoCursor(true);
    getPlayer():setAuthorizeMeleeAction(true);
    FightStep.stopHighlight = true;
    FightStep.momzombie:setReanimateTimer(300);
    local complete = FightStep.momDead;
    if complete then
        getCore():setFlashIsoCursor(false);
        for i=0,getPlayer():getInventory():getItems():size()-1 do
            local pan = getPlayer():getInventory():getItems():get(i);
            if pan:getType() == "Pan" then
                pan:setCondition(0);
                getSoundManager():PlayWorldSoundImpl("BreakMetalItem", false, FightStep.zombieMomSpawnX, FightStep.zombieMomSpawnY, 0, 0, 20, 1, false);
                break;
            end
        end
        for i=0, 7 do
            getPlayer():addBlood(nil, true, false, false);
        end
        getPlayer():setAuthorizeMeleeAction(false);
        getPlayer():setAuthorizeShoveStomp(true);
        JoypadState.disableYInventory = false;
    end
    return complete;
end

function FightStep:LootKnife()
--    print(FightStep.momzombie:isDead(), FightStep.momzombie:getHealth(), FightStep.momzombie:getCurrentState())
    getPlayerLoot(0).inventoryPane.highlightItem = "HuntingKnife";
    getPlayerLoot(0):setBlinkingContainer(true, "inventoryfemale");
    Tutorial1.contextMenuEquipPrimary = true;
    if not JoypadState.players[1] then
        if not FightStep.pinInv then
            FightStep.pinInv = true;
            getPlayerLoot(0):setPinned();
        end
        getPlayerLoot(0).blink = true;
        getPlayerInventory(0).inventoryPane.highlightItem = "Pan";
    end
    
    local complete = getPlayer():getPrimaryHandItem() and getPlayer():getPrimaryHandItem():getType() == "HuntingKnife";
    
    if getPlayer():getInventory():contains("HuntingKnife", false, false) then
        getPlayerInventory(0).inventoryPane.highlightItem = "HuntingKnife";
        if not JoypadState.players[1] then
            Tutorial1.collapseInv(getPlayerLoot(0));
            getPlayerInventory(0).blink = true;
            getPlayerInventory(0):setPinned();
            getPlayerLoot(0).blink = false;
        end
    end

    if complete then
        Tutorial1.contextMenuEquipPrimary = false;
        Tutorial1.closeBothInv();
        getPlayerLoot(0).inventoryPane.highlightItem = nil;
        getPlayerLoot(0).blink = false;
        getPlayerInventory(0).inventoryPane.highlightItem = nil;
        getPlayerLoot(0):setBlinkingContainer(false);
    
        getSoundManager():PlayWorldSoundImpl("UnlockDoor", false, FightStep.lockedDoor:getX(), FightStep.lockedDoor:getY(), 0, 0, 20, 3, false);
        FightStep.lockedDoor:setLockedByKey(false);
        FightStep.lockedDoor2:setLockedByKey(false);
        if not JoypadState.players[1] then
            Tutorial1.collapseInv(getPlayerLoot(0));
            Tutorial1.collapseInv(getPlayerInventory(0));
        end
        getPlayerInventory(0).blink = false;
        TutorialTests.RemoveMarkers();
    end
    return complete;
end

function FightStep:isComplete()
    return TutorialStep.isComplete(self);
end

function FightStep:finish()
    FightStep.finished = true;
    TutorialStep.finish(self);
end

SneakStep = TutorialStep:derive("SneakStep");
SneakStep.zombieDadSpawnX = 166;
SneakStep.zombieDadSpawnY = 147;
--SneakStep.gateX =

function SneakStep:new () local o = {} setmetatable(o, self)    self.__index = self    return o end

function SneakStep:begin()
    Tutorial1.contextMenuEquipPrimary = false;
    getPlayer():setAuthorizeMeleeAction(false);
    getPlayer():setIgnoreContextKey(true);
    JoypadState.disableControllerPrompt = false;
    JoypadState.disableMovement = false;
    JoypadState.disableYInventory = false;
    JoypadState.disableInvInteraction = false;
    if getCore():getDebug() and not FightStep.finished then
        getPlayer():setX(165);
        getPlayer():setY(154);
        getPlayer():setIgnoreAutoVault(true);
        getPlayer():setIgnoreInputsForDirection(false);
        local pan = getPlayer():getInventory():AddItem("Base.HuntingKnife");
        getPlayer():setPrimaryHandItem(pan);
        if not JoypadState.players[1] then
            getPlayerInventory(0):setVisible(true);
            getPlayerLoot(0):setVisible(true);
        end
        getPlayerLoot(0).lootAll:setVisible(false);
        getPlayer():setZombieKills(1);
    end
    
    FightStep.lockedDoor:setLockedByKey(false);
    FightStep.lockedDoor:setLocked(false);
    FightStep.lockedDoor2:setLocked(false);
    FightStep.lockedDoor2:setLockedByKey(false);

    
    local sq = getCell():getGridSquare(163, 152, 0);
    FightStep.floor1SQ = sq;
    sq = getCell():getGridSquare(164, 152, 0);
    FightStep.floor2SQ = sq;
    
    getSoundManager():PlayWorldSoundImpl("MaleZombieAttack", false, FightStep.zombieMomSpawnX, FightStep.zombieMomSpawnY, 0, 0, 20, 1, false);

    SneakStep:spawnDad();
    
    local zombie = createZombie(SneakStep.zombieDadSpawnX - 1, SneakStep.zombieDadSpawnY, 0, nil, 0,IsoDirections.S);
    while not zombie:isFemale() do
        zombie:removeFromWorld();
        zombie:removeFromSquare();
        zombie = createZombie(SneakStep.zombieDadSpawnX - 1, SneakStep.zombieDadSpawnY, 0, nil, 0,IsoDirections.S);
    end
    zombie:dressInRandomOutfit();
    zombie:DoZombieInventory();
    local body = IsoDeadBody.new(zombie, false);
    body:setX(SneakStep.dadzombie:getX() - 0.4);
    body:setY(SneakStep.dadzombie:getY());
    SneakStep.dadzombie:setForceEatingAnimation(true);
    SneakStep.dadzombie:setOnlyJawStab(true);
    
    for i=0, 7 do
        SneakStep.dadzombie:addBlood(nil, false, true, false);
        SneakStep.dadzombie:addHole(nil);
    end
    
    SneakStep.dadzombie:setUseless(true);
    
    getPlayer():setDir(IsoDirections.NW);

    TutorialTests.addHoming(FightStep.floor1SQ);
    TutorialTests.highlight(FightStep.lockedDoor, 0.2);
    TutorialTests.highlight(FightStep.lockedDoor2, 0.2);
    
    if JoypadState.players[1] then
        self:addMessage(getText("IGUI_Tutorial1_Sneak1Joypad"),  300, 500, 320, 250, false, SneakStep.GoThroughDoor);
        self:addMessage(getText("IGUI_Tutorial1_Sneak1BisJoypad"),  300, 500, 520, 110, false, SneakStep.Sneak);
        self:addMessage(getText("IGUI_Tutorial1_Sneak2"),  getCore():getScreenWidth() - 400, 200, 520, 110, false, SneakStep.SneakingGate);
        self:addMessage(getText("IGUI_Tutorial1_Sneak3Joypad"), getCore():getScreenWidth() - 400, 400, 520, 110, false, SneakStep.OpenGate);
        self:addMessage(getText("IGUI_Tutorial1_Sneak4Joypad"),  getCore():getScreenWidth()/2, 250 + getCore():getScreenHeight()/2, 520, 110, false, SneakStep.DadDead);
        self:addMessage(getText("IGUI_Tutorial1_Sneak5Joypad"),  getCore():getScreenWidth()/2, 250 + getCore():getScreenHeight()/2, 520, 110, false, SneakStep.EquippedBag);
        self:addMessage(getText("IGUI_Tutorial1_Sneak6Joypad"),  getCore():getScreenWidth()/2, 250 + getCore():getScreenHeight()/2, 520, 110, false, SneakStep.CheckBag);
        self:addMessage(getText("IGUI_Tutorial1_Sneak7Joypad"),  getCore():getScreenWidth()/2, 250 + getCore():getScreenHeight()/2, 520, 110, false, SneakStep.EquipShotgun);
    else
        self:addMessage(getText("IGUI_Tutorial1_Sneak1", getKeyName(getCore():getKey("Interact"))),  300, 500, 320, 250, false, SneakStep.GoThroughDoor);
        self:addMessage(getText("IGUI_Tutorial1_Sneak1Bis", getKeyName(getCore():getKey("Crouch"))),  300, 500, 520, 110, false, SneakStep.Sneak);
        self:addMessage(getText("IGUI_Tutorial1_Sneak2"),  getCore():getScreenWidth() - 400, 200, 520, 110, false, SneakStep.SneakingGate);
        self:addMessage(getText("IGUI_Tutorial1_Sneak3", getKeyName(getCore():getKey("Interact"))), getCore():getScreenWidth() - 400, 400, 520, 110, false, SneakStep.OpenGate);
        self:addMessage(getText("IGUI_Tutorial1_Sneak4"),  getCore():getScreenWidth()/2, 250 + getCore():getScreenHeight()/2, 520, 110, false, SneakStep.DadDead);
        self:addMessage(getText("IGUI_Tutorial1_Sneak5"),  getCore():getScreenWidth()/2, 250 + getCore():getScreenHeight()/2, 520, 110, false, SneakStep.EquippedBag);
        self:addMessage(getText("IGUI_Tutorial1_Sneak6"),  getCore():getScreenWidth()/2, 250 + getCore():getScreenHeight()/2, 520, 110, false, SneakStep.CheckBag);
        self:addMessage(getText("IGUI_Tutorial1_Sneak7"),  getCore():getScreenWidth()/2, 250 + getCore():getScreenHeight()/2, 520, 110, false, SneakStep.EquipShotgun);
    end
    self:doMessage();
end

function SneakStep:spawnDad()
    SneakStep.dadzombie = addZombiesInOutfit(SneakStep.zombieDadSpawnX, SneakStep.zombieDadSpawnY, 0, 1, "TutorialDad", 0):get(0);
    SneakStep.dadzombie:setDir(IsoDirections.W);
--    SneakStep.loopedSound = SneakStep.dadzombie:getEmitter():playSound("MaleZombieCombined");
    
    Events.OnZombieDead.Add(SneakStep.OnDadDead);

    --    zombie.getEmitter().playSound(soundName);
    SneakStep.dadzombie:setDressInRandomOutfit(false);
    SneakStep.dadzombie:dressInNamedOutfit("TutorialDad");
    SneakStep.dadzombie:getVisual():setHairModel("Baldspot");
    SneakStep.dadzombie:getVisual():setBeardModel("Full");
    local immutableColor = ImmutableColor.new(0.105, 0.09, 0.086, 1)
    SneakStep.dadzombie:getVisual():setHairColor(immutableColor)
    SneakStep.dadzombie:getVisual():setBeardColor(immutableColor)
    SneakStep.dadzombie:getVisual():setSkinTextureIndex(2);
    SneakStep.dadzombie:setAlwaysKnockedDown(true);
    SneakStep.dadzombie:resetModelNextFrame();
    SneakStep.dadzombie:setX(SneakStep.dadzombie:getX() + 0.5);
end

function SneakStep:OnDadDead()
    SneakStep.isDadDead = true;
    Events.OnZombieDead.Remove(SneakStep.OnDadDead);
end

function SneakStep:GoThroughDoor()
    getPlayer():setIgnoreContextKey(false);
    
    if FightStep.lockedDoor:IsOpen() and not FightStep.markerDone then
        FightStep.markerDone = true;
        getPlayer():setIgnoreContextKey(true);
        TutorialTests.stopHighlight(FightStep.lockedDoor);
        TutorialTests.stopHighlight(FightStep.lockedDoor2);
        TutorialTests.RemoveMarkers();
        TutorialTests.addMarker(getSquare(163, 150, 0), 2);
--        FightStep.lockedDoor:setHighlighted(false);
--        FightStep.lockedDoor2:setHighlighted(false);
    elseif FightStep.markerDone then
        getPlayer():setIgnoreContextKey(false);
    end
    
    local complete = getPlayer():getCurrentSquare() == FightStep.floor1SQ or getPlayer():getCurrentSquare() == FightStep.floor2SQ;
    if complete then
        TutorialTests.RemoveMarkers();
        TutorialTests.stopHighlight(FightStep.lockedDoor);
        TutorialTests.stopHighlight(FightStep.lockedDoor2);
        getPlayer():setDir(IsoDirections.NE);
        getPlayer():setIgnoreContextKey(true);
        SneakStep.setZoom(1.5);
    end
    return complete;
end

function SneakStep.setZoom(depth)
    local mod = (depth - getCore():getZoom(0)) / 0.25;
    local unzoom = true;
    if mod < 0 then
        mod = math.abs(mod);
        unzoom = false;
    end
    for i=1, mod do
        if unzoom then
            getCore():doZoomScroll(0, 1);
        else
            getCore():doZoomScroll(0, -1);
        end
    end
end

function SneakStep.Sneak()
    getPlayer():setIgnoreInputsForDirection(true);
    if getPlayer():isSneaking() then
        getPlayer():setIgnoreInputsForDirection(false);
        return true;
    end
    return false;
end

function SneakStep:SneakingGate()
    TutorialTests.highlight(SneakStep.fenceGate, 0.2);
    SneakStep.sqGate = getCell():getGridSquare(169, 144, 0);
    TutorialTests.addHoming(SneakStep.sqGate);

    if not getPlayer():isSneaking() and (SneakStep.wasSneaking or getPlayer():getCurrentSquare():getY() < 148) then
        if TutorialMessage.instance then
            if JoypadState.players[1] then
                TutorialMessage.instance.richtext.text = getText("IGUI_Tutorial1_Sneak2BisJoypad", getKeyName(getCore():getKey("Crouch")));
            else
                TutorialMessage.instance.richtext.text = getText("IGUI_Tutorial1_Sneak2Bis", getKeyName(getCore():getKey("Crouch")));
            end
            TutorialMessage.instance.richtext:paginate();
            TutorialMessage.instance:setX(300 - getCore():getScreenWidth()/2);
            TutorialMessage.instance:setY(80 + getCore():getScreenHeight()/2);
        end
    elseif getPlayer():isSneaking() then
        SneakStep.wasSneaking = true;
        SneakStep.fenceGate:setLockedByKey(false);
        getPlayer():setIgnoreContextKey(false);
    end
    
    if not getPlayer():isSneaking() then
        SneakStep.fenceGate:setLockedByKey(true);
        getPlayer():setIgnoreInputsForDirection(true);
    else
        getPlayer():setIgnoreInputsForDirection(false);
    end

    local complete = getPlayer():getCurrentSquare() == SneakStep.sqGate;

    return complete;
end

function SneakStep:OpenGate()
    getPlayer():setIgnoreContextKey(false);
    local complete = SneakStep.fenceGate:IsOpen();
    if complete then
        TutorialTests.stopHighlight(SneakStep.fenceGate);
        TutorialTests.RemoveMarkers();
        getPlayer():setIgnoreContextKey(true);
        SneakStep.setZoom(0.5);
        Events.OnWeaponHitXp.Add(SneakStep.OnSwingAtDad);
        TutorialTests.addHoming(SneakStep.dadzombie:getCurrentSquare(), -20);
    end
    return complete;
end

function SneakStep.OnSwingAtDad(owner, weapon, zed, dmg)
    if not zed:isCloseKilled() then
        if TutorialMessage.instance then
            if JoypadState.players[1] then
                TutorialMessage.instance.richtext.text = getText("IGUI_Tutorial1_Sneak4BisJoypad");
            else
                TutorialMessage.instance.richtext.text = getText("IGUI_Tutorial1_Sneak4Bis");
            end
            TutorialMessage.instance.richtext:paginate();
            TutorialMessage.instance:setX(20 - getCore():getScreenWidth()/2);
            TutorialMessage.instance:setY(80 + getCore():getScreenHeight()/2);
        end
    end
end

function SneakStep:DadDead()
    TutorialTests.addMarker(SneakStep.dadzombie:getCurrentSquare(), 1);
    SneakStep.dadzombie:setForceEatingAnimation(false);
    getPlayer():setAuthorizeMeleeAction(true);
    getPlayer():setAuthorizeShoveStomp(false);
    SneakStep.fenceGate:setHighlighted(false);
    SneakStep.dadzombie:setReanimateTimer(30);
    local complete = SneakStep.isDadDead;
    if complete then
        Events.OnWeaponHitXp.Remove(SneakStep.OnSwingAtDad);
--        SneakStep.dadzombie:getEmitter():stopSound(SneakStep.loopedSound)
        getPlayer():setAuthorizeMeleeAction(false);
        getPlayer():setAuthorizeShoveStomp(true);
        SneakStep.fenceGate:ToggleDoorSilent();
        SneakStep.fenceGate:setLocked(true);
        SneakStep.fenceGate:setLockedByKey(true);
        for i=0, 7 do
            getPlayer():addBlood(nil, true, false, false);
        end
        if not JoypadState.players[1] then
            getPlayerLoot(0):setPinned();
        end
    end
    return complete;
end

function SneakStep:EquippedBag()
    if not getPlayer():getInventory():contains("Bag_ALICEpack") then
        getPlayerLoot(0).blink = true;
        Tutorial1.contextMenuEquipBag = true;
        getPlayerLoot(0):setBlinkingContainer(true, "inventorymale");
        getPlayerLoot(0).inventoryPane.highlightItem = "Bag_ALICEpack";
    else
        TutorialTests.RemoveMarkers();
        getPlayerLoot(0).blink = false;
        getPlayerInventory(0).blink = true;
        Tutorial1.contextMenuEquipBag = true;
        getPlayerLoot(0).inventoryPane.highlightItem = nil;
        getPlayerInventory(0).inventoryPane.highlightItem = "Bag_ALICEpack";
        getPlayerLoot(0):setBlinkingContainer(false);
        if not SneakStep.pinInv and not JoypadState.players[1] then
            SneakStep.pinInv = true;
            getPlayerInventory(0):setPinned();
        end
    end
    local isOpen = getPlayerLoot(0) ~= nil and not getPlayerLoot(0).isCollapsed;
    if isOpen and not SneakStep.bag then
        getPlayerLoot(0).blink = false;
        for i,v in ipairs(getPlayerLoot(0).backpacks) do
            local bag = v.inventory:FindAndReturn("Bag_ALICEpack")
            if bag then
                SneakStep.bag = bag;
                bag:getItemContainer():AddItem(SneakStep.spawnShotgun());
                bag:getItemContainer():AddItem("Base.Bandage");
                return;
            end
        end
    end
    if SneakStep.bag and getPlayer():isEquipped(SneakStep.bag) then
        getPlayerLoot(0).inventoryPane.highlightItem = nil;
        getPlayerLoot(0).blink = false;
        getPlayerInventory(0).inventoryPane.highlightItem = nil;
        getPlayerInventory(0).blink = false;
        Tutorial1.contextMenuEquipBag = false;
        getPlayerLoot(0):setBlinkingContainer(false);
        getPlayerLoot(0):setVisible(false);
        if not JoypadState.players[1] then
            Tutorial1.collapseInv(getPlayerInventory(0));
            Tutorial1.collapseInv(getPlayerLoot(0));
        end
        return true;
    end
    return false;
end

function SneakStep:CheckBag()
    getPlayerInventory(0).blink = true;
    if not JoypadState.players[1] then
        getPlayerInventory(0):setPinned();
    end
    getPlayerInventory(0):setBlinkingContainer(true, "Bag_ALICEpack");
    
    if not getPlayerInventory(0).isCollapsed then
        getPlayerInventory(0).blink = false;
        if getPlayerInventory(0).inventory == SneakStep.bag:getItemContainer() then
            getPlayerInventory(0):setBlinkingContainer(false);
            return true;
        end
    end
    return false;
end

function SneakStep:EquipShotgun()
    Tutorial1.contextMenuEquip2Hand = true;
    getPlayerInventory(0).inventoryPane.highlightItem = "Shotgun";
    if getPlayer():getPrimaryHandItem() == SneakStep.shotgun then
        Tutorial1.contextMenuEquip2Hand = false;
        getPlayer():LevelPerk(Perks.Aiming);getPlayer():LevelPerk(Perks.Aiming);getPlayer():LevelPerk(Perks.Aiming);getPlayer():LevelPerk(Perks.Aiming);getPlayer():LevelPerk(Perks.Aiming);getPlayer():LevelPerk(Perks.Aiming);getPlayer():LevelPerk(Perks.Aiming);getPlayer():LevelPerk(Perks.Aiming);
        Tutorial1.contextMenuEquip2Hand = false;
        Tutorial1.closeBothInv();
        SneakStep.setZoom(1.5);
        getPlayer():setIgnoreInputsForDirection(false);
        return true;
    end
    return false;
end

function SneakStep.spawnShotgun()
    local shotgun = InventoryItemFactory.CreateItem("Base.Shotgun");
    shotgun:setCurrentAmmoCount(6);
    shotgun:setRoundChambered(true);
    shotgun:setMinDamage(1);
    shotgun:setMaxDamage(2);
    shotgun:setMaxRange(20);
    shotgun:setActualWeight(0.5);
    SneakStep.shotgun = shotgun;
    
    return shotgun;
end

function SneakStep:isComplete()
    return TutorialStep.isComplete(self);
end

function SneakStep:finish()
    SneakStep.finished = true;
    TutorialStep.finish(self);
end

BandageStep = TutorialStep:derive("BandageStep");
BandageStep.brotherX = 182;
BandageStep.brotherY = 147;

function BandageStep:new () local o = {} setmetatable(o, self)    self.__index = self    return o end

function BandageStep:begin()
    getPlayerLoot(0):setVisible(true);
    JoypadState.disableControllerPrompt = false;
    JoypadState.disableMovement = false;
    JoypadState.disableYInventory = false;
    ISBackButtonWheel.disablePlayerInfo = false;
    JoypadState.disableInvInteraction = false;
    if getCore():getDebug() and not SneakStep.finished then
        getPlayer():setAuthorizeMeleeAction(false);
        getPlayer():setIgnoreInputsForDirection(false);
        local bag = InventoryItemFactory.CreateItem("Base.Bag_ALICEpack");
        SneakStep.bag = bag;
        local shotgun = SneakStep.spawnShotgun();
        getPlayer():getInventory():AddItem(shotgun);
        getPlayer():getInventory():AddItem(bag);
        bag:getItemContainer():AddItem("Base.Bandage");
        getPlayer():setWornItem("Back", bag);
        getPlayer():setPrimaryHandItem(shotgun);
        getPlayer():setSecondaryHandItem(shotgun);
    
        getPlayer():setX(166);
        getPlayer():setY(147);
    
        if not JoypadState.players[1] then
            getPlayerInventory(0):setVisible(true);
            getPlayerLoot(0):setVisible(true);
        end
        getPlayer():setZombieKills(2);
    end
    
    getPlayer():setDir(IsoDirections.E);
    getPlayer():setIgnoreContextKey(true);
    getPlayer():setIgnoreAutoVault(false);
    getPlayer():setAllowRun(true);
    
    getSoundManager():PlayWorldSoundImpl("SmashWindow", false, getPlayer():getX(), getPlayer():getY(), 0, 0, 20, 1, false);
    
    local sq = getSquare(175, 150, 0);
    for i=0, sq:getObjects():size() -1 do
        local obj = sq:getObjects():get(i);
        if instanceof(obj, "IsoWindow") then
            obj:smashWindow(false, false);
            obj:addBrokenGlass(getPlayer());
            BandageStep.window = obj;
            break;
        end
    end
    
    BandageStep.fences = {};
    for y=145, 152 do
        sq = getSquare(173, y, 0);
        for i=0, sq:getObjects():size() -1 do
            local obj = sq:getObjects():get(i);
            obj:setHighlighted(true, false);
            obj:setBlink(true);
            obj:setHighlightColor(OBJECT_HIGHLIGHT_COLOR);
            table.insert(BandageStep.fences, obj);
        end
        sq:getFloor():setHighlightColor(FLOOR_HIGHLIGHT_COLOR);
    end
    
    TutorialTests.addHoming(getSquare(173, 149, 0));
    
    if JoypadState.players[1] then
        self:addMessage(getText("IGUI_Tutorial1_Bandage1Joypad"),  100, 150 + getCore():getScreenHeight()/2, 420, 110, false, BandageStep.Vault);
        self:addMessage(getText("IGUI_Tutorial1_Bandage2Joypad"), 400, 200, 420, 110, false, BandageStep.ThroughWindow);
        self:addMessage(getText("IGUI_Tutorial1_Bandage3Joypad"), 300, getCore():getScreenHeight() - 200, 500, 110, false, TutorialTests.PlayerInfoOpen);
        self:addMessage(getText("IGUI_Tutorial1_Bandage4Joypad"), 300, 300 + getCore():getScreenHeight()/2, 420, 110, false, BandageStep.BandageYourself);
        self:addMessage(getText("IGUI_Tutorial1_Bandage5"), getCore():getScreenWidth() - 600, getCore():getScreenHeight() - 400, 320, 110, false, BandageStep.CheckWindow);
        self:addMessage(getText("IGUI_Tutorial1_Bandage6Joypad"), getCore():getScreenWidth() - 500, getCore():getScreenHeight() - 500, 320, 110, false, BandageStep.OpenCurtain);
        self:addMessage(getText("IGUI_Tutorial1_Bandage7"), 300, getCore():getScreenHeight() - 200, 420, 110, true);
        self:addMessage(getText("IGUI_Tutorial1_Bandage8"), 300, getCore():getScreenHeight() - 200, 420, 110, false, BandageStep.ThroughDoor);
    else
        self:addMessage(getText("IGUI_Tutorial1_Bandage1", getKeyName(getCore():getKey("Run")), Tutorial1.moveKeys),  100, 150 + getCore():getScreenHeight()/2, 420, 110, false, BandageStep.Vault);
        self:addMessage(getText("IGUI_Tutorial1_Bandage2", getKeyName(getCore():getKey("Interact"))), 400, 200, 420, 110, false, BandageStep.ThroughWindow);
        self:addMessage(getText("IGUI_Tutorial1_Bandage3", getKeyName(getCore():getKey("Toggle Health Panel"))), 300, getCore():getScreenHeight() - 200, 500, 110, false, BandageStep.HealthOpen);
        self:addMessage(getText("IGUI_Tutorial1_Bandage4"), 300, 300 + getCore():getScreenHeight()/2, 420, 110, false, BandageStep.BandageYourself);
        self:addMessage(getText("IGUI_Tutorial1_Bandage5"), getCore():getScreenWidth() - 600, getCore():getScreenHeight() - 400, 320, 110, false, BandageStep.CheckWindow);
        self:addMessage(getText("IGUI_Tutorial1_Bandage6"), getCore():getScreenWidth() - 500, getCore():getScreenHeight() - 500, 320, 110, false, BandageStep.OpenCurtain);
        self:addMessage(getText("IGUI_Tutorial1_Bandage7"), 300, getCore():getScreenHeight() - 200, 420, 110, true);
        self:addMessage(getText("IGUI_Tutorial1_Bandage8"), 300, getCore():getScreenHeight() - 200, 420, 110, false, BandageStep.ThroughDoor);
    end
    
    self:doMessage();
end

function BandageStep.Vault()
    getPlayer():setSneaking(false);
    if not getPlayer():getCurrentState() then -- for when debug force step
        return false;
    end
    
    if JoypadState.players[1] then
        if not BandageStep.runned and isJoypadRTPressed(0) then
            getPlayer():setIgnoreInputsForDirection(false);
            BandageStep.runned = true;
        end
    else
        if not BandageStep.runned and isKeyDown(getCore():getKey("Run")) then
            getPlayer():setIgnoreInputsForDirection(false);
            BandageStep.runned = true;
        end
    end
    if not BandageStep.runned then
        getPlayer():setIgnoreInputsForDirection(true);
    end

    local complete = false;
    if not BandageStep.vaulted and getPlayer():getCurrentState():equals(ClimbOverFenceState.instance()) then
        BandageStep.vaulted = true;
    end
    if BandageStep.vaulted and getPlayer():getCurrentState():equals(IdleState.instance()) and getPlayer():getCurrentSquare():getX() ~= 173 then
        BandageStep.vaultedWrong = true;
        complete = true;
    end
    if BandageStep.vaulted and getPlayer():getCurrentSquare():getX() == 173 then
        complete = true;
    end
    if complete then
        TutorialTests.RemoveMarkers();
        TutorialTests.highlight(BandageStep.window, 0.1);
        TutorialTests.addHoming(BandageStep.window:getSquare(), -60, -20);
        getPlayer():setIgnoreAutoVault(true);
        for i,v in ipairs(BandageStep.fences) do
            v:setHighlighted(false);
        end
        BandageStep.fences = {};
    end
    return complete;
end

function BandageStep.ThroughWindow()
    if BandageStep.vaultedWrong and TutorialMessage.instance then
        getPlayer():setIgnoreAutoVault(false);
        if JoypadState.players[1] then
            TutorialMessage.instance.richtext.text = getText("IGUI_Tutorial1_Bandage2BisJoypad");
        else
            TutorialMessage.instance.richtext.text = getText("IGUI_Tutorial1_Bandage2Bis", getKeyName(getCore():getKey("Interact")));
        end
        TutorialMessage.instance.richtext:paginate();
        if not BandageStep.vaultedWrongZoom then
            SneakStep.setZoom(2);
            getPlayer():setDir(IsoDirections.E);
            BandageStep.vaultedWrongZoom = true;
        end
    end
    getPlayer():setSneaking(false);
    getPlayer():setIgnoreContextKey(false);
    
    if not BandageStep.vaultedWindow and getPlayer():isCurrentState(ClimbThroughWindowState.instance()) then
        BandageStep.vaultedWindow = true;
        getPlayer():setIgnoreAutoVault(true);
        BandageStep.spawnBrothers();
    end

    if BandageStep.vaultedWindow and not getPlayer():isCurrentState(ClimbThroughWindowState.instance()) and getPlayer():getCurrentSquare() == BandageStep.window:getSquare() then
        getPlayer():getBodyDamage():RestoreToFullHealth();
        getPlayer():getBodyDamage():getBodyPart(BodyPartType.Hand_L):setScratched(true, true);
        getPlayer():addBlood(BloodBodyPartType.Hand_L, false, true, false);
        BandageStep.window:setHighlighted(false);
        BandageStep.window:removeBrokenGlass();
        getPlayer():setIgnoreContextKey(true);
        SneakStep.setZoom(0.5);
        BandageStep.extTimer = 10;
        getSoundManager():PlayWorldSoundImpl("WalkOnBrokenGlass", false, getPlayer():getX(), getPlayer():getY(), 0, 0, 20, 1, false);
    
        getPlayer():setIgnoreContextKey(true);
        getPlayer():setIgnoreInputsForDirection(true);
        TutorialTests.stopHighlight(BandageStep.window);
        TutorialTests.RemoveMarkers();
    
        return true;
    end
    
    return false;
end

function BandageStep.CheckWindow()
    getPlayer():setSneaking(false);
    
    local complete = getPlayer():getCurrentSquare() == BandageStep.sqWindow;
    if complete then
        getPlayer():setDir(IsoDirections.E);
        TutorialTests.RemoveMarkers();
        TutorialTests.addHoming(BandageStep.barricadedWindow1:getSquare(), -60, -20);
        TutorialTests.stopHighlight(BandageStep.barricadedWindow1);
        TutorialTests.highlight(BandageStep.curtain, 0.1);
    end
    
    return complete;
end

function BandageStep.OpenCurtain()
    getPlayer():setIgnoreContextKey(false);
    getPlayer():setDir(IsoDirections.E);
    Tutorial1.contextMenuOpenCurtain = true;
    ISInventoryPaneContextMenu.blinkOption = nil;
    getPlayer():setIgnoreInputsForDirection(true);
    local complete = BandageStep.curtain:IsOpen();
    if complete then
        Tutorial1.contextMenuOpenCurtain = false;
        getPlayer():setIgnoreContextKey(false);
        TutorialTests.RemoveMarkers();
        TutorialTests.stopHighlight(BandageStep.curtain);
        getPlayer():getBodyDamage():IncreasePanic(10);
        getPlayer():playSound("ZombieSurprisedPlayer");
    end
    return complete;
end

BandageStep.HealthOpen = function()
    ISEquippedItem.instance.healthBtn.blinkImage = true;
    getPlayer():setIgnoreInputsForDirection(true);
    BandageStep.extTimer = BandageStep.extTimer - 1;
    if BandageStep.extTimer == 0 then
        BandageStep.extTimer = 50;
        getPlayer():setVariable("Ext", "PainHandL");
        getPlayer():reportEvent("EventDoExt");
    end
    if ISHealthPanel.instance ~= nil and ISHealthPanel.instance:isReallyVisible() then
        ISEquippedItem.instance.healthBtn.blinkImage = false;
        ISEquippedItem.instance.healthBtn:setVisible(false);
        return true;
    end
    return false;
end

function BandageStep:OnBrothersDead()
    if BandageStep.oneDead then
        BandageStep.isBrothersDead = true;
        Events.OnZombieDead.Remove(BandageStep.OnBrothersDead);
    else
        BandageStep.oneDead = true;
    end
end

function BandageStep.spawnBrothers()
    BandageStep.brother1 = addZombiesInOutfit(BandageStep.brotherX, BandageStep.brotherY, 0, 1, "TutorialBrother1", 0):get(0);
    BandageStep.brother1:setDir(IsoDirections.W);

    BandageStep.brother1:setDressInRandomOutfit(false);
--    BandageStep.brother1:dressInNamedOutfit("TutorialBrother1");
    BandageStep.brother1:getVisual():setSkinTextureIndex(2);
    BandageStep.brother1:resetModelNextFrame();
    BandageStep.brother1:setForceEatingAnimation(true);
    BandageStep.brother1:setImmortalTutorialZombie(true);
    
    for i=0, 7 do
        BandageStep.brother1:addBlood(nil, false, true, false);
        BandageStep.brother1:addHole(nil);
    end
    
    BandageStep.brother2 = addZombiesInOutfit(BandageStep.brotherX - 1, BandageStep.brotherY, 0, 1, "TutorialBrother2", 0):get(0);
    BandageStep.brother2:setDir(IsoDirections.E);

    BandageStep.brother2:setDressInRandomOutfit(false);
--    BandageStep.brother2:dressInNamedOutfit("TutorialBrother2");
    BandageStep.brother2:getVisual():setSkinTextureIndex(2);
    BandageStep.brother2:resetModelNextFrame();
    BandageStep.brother2:setForceEatingAnimation(true);
    BandageStep.brother2:setX(BandageStep.brother1:getX() - 1);
    BandageStep.brother2:setY(BandageStep.brother1:getY());
    BandageStep.brother2:setImmortalTutorialZombie(true);
    
    BandageStep.brother1:setUseless(true);
    BandageStep.brother2:setUseless(true);
    BandageStep.brother1:setNoTeeth(true);
    BandageStep.brother2:setNoTeeth(true);

    for i=0, 7 do
        BandageStep.brother2:addBlood(nil, false, true, false);
        BandageStep.brother2:addHole(nil);
    end
    
    local zombie = createZombie(BandageStep.brotherX - 1, BandageStep.brotherY, 0, nil, 0,IsoDirections.S);
    while not zombie:isFemale() do
        zombie:removeFromWorld();
        zombie:removeFromSquare();
        zombie = createZombie(BandageStep.brotherX - 1, BandageStep.brotherY, 0, nil, 0,IsoDirections.S);
    end
    zombie:setDressInRandomOutfit(false);
    zombie:dressInNamedOutfit("DressLong");
    zombie:getVisual():setSkinTextureIndex(2);
    zombie:resetModelNextFrame();
    
    zombie:DoZombieInventory();
    for i=0, 15 do
        zombie:addBlood(nil, false, true, false);
    end
    local body = IsoDeadBody.new(zombie, false);
    body:setX(BandageStep.brother1:getX() - 0.3);
    body:setY(BandageStep.brother1:getY() + 0.5);
    
    Events.OnZombieDead.Add(BandageStep.OnBrothersDead);
end

function BandageStep.BandageYourself()
    BandageStep.extTimer = BandageStep.extTimer - 1;
    if BandageStep.extTimer == 0 then
        BandageStep.extTimer = 50;
        getPlayer():setVariable("Ext", "PainHandL");
        getPlayer():reportEvent("EventDoExt");
    end
    getPlayer():setIgnoreInputsForDirection(true);
    getPlayer():setSneaking(false);
    if getPlayer():getBodyDamage():getBodyPart(BodyPartType.Hand_L):getBandageLife() > 0 then
        getSoundManager():PlayWorldSoundImpl("MaleZombieAttack", false, getPlayer():getX() + 2, getPlayer():getY() + 1, 0, 0, 20, 1, false);
    
        BandageStep.sqDoor = getSquare(176, 153, 0);
    
        local charInfo = getPlayerInfoPanel(getPlayer():getPlayerNum());
        if charInfo then
            charInfo:setVisible(false);
            if JoypadState.players[1] then
                setJoypadFocus(0, nil)
                updateJoypadFocus(JoypadState.players[1])
            end
        end
        SneakStep.setZoom(1);
        getPlayer():setIgnoreInputsForDirection(false);
        getPlayer():setDir(IsoDirections.E);
    
        BandageStep.sqWindow = getCell():getGridSquare(178, 147, 0);
        TutorialTests.addMarker(BandageStep.sqWindow, 1);
        TutorialTests.highlight(BandageStep.barricadedWindow1, 0.1);
    
        return true;
    end
    
    return false;
end

function BandageStep.ThroughDoor()
    getPlayer():setIgnoreInputsForDirection(false);
    getPlayer():setIgnoreContextKey(false);
    if not BandageStep.spawnedItems then
        TutorialTests.highlight(BandageStep.lockedDoor, 0.3);
        TutorialTests.addHoming(BandageStep.lockedDoor:getSquare(), -60);
        
        BandageStep.lockedDoor:setLockedByKey(false);
        BandageStep.lockedDoor:setLocked(false);
        getPlayer():setIgnoreContextKey(false);
    
        BandageStep.containers = {};
        Tutorial1.contextMenuWear = true;
        local sq = getSquare(175, 147, 0);
        for i=0, sq:getObjects():size() -1 do
            local obj = sq:getObjects():get(i);
            if obj:getContainer() then
                TutorialTests.highlight(obj, 0.3);
                TutorialTests.addHoming(sq, nil, nil, {r=0,g=1,b=0});
                obj:getContainer():AddItem("Base.Hat_BaseballCap_Reverse");
                obj:getContainer():AddItem("Base.Hat_Bandana");
                obj:getContainer():AddItem("Base.Trousers_CamoDesert");
                obj:getContainer():AddItem("Base.Shorts_CamoGreenLong");
                obj:getContainer():AddItem("Base.Vest_Hunting_Orange");
                obj:getContainer():AddItem("Base.Dungarees");
                obj:getContainer():AddItem("Base.Tshirt_Rock");
                obj:getContainer():AddItem("Base.Shirt_Denim");
                obj:getContainer():AddItem("Base.Shirt_Lumberjack");
                obj:getContainer():AddItem("Base.Jacket_Black");
                obj:getContainer():AddItem("Base.HoodieUP_WhiteTINT");
                obj:getContainer():AddItem("Base.Shoes_ArmyBoots");
                table.insert(BandageStep.containers, obj);
            end
        end
        BandageStep.spawnedItems = true;
    end
    
    getPlayer():setSneaking(false);
    local complete = BandageStep.lockedDoor:IsOpen() and getPlayer():getCurrentSquare() == BandageStep.sqDoor;
    if BandageStep.lockedDoor:IsOpen() and not BandageStep.blink then
        BandageStep.blink = true;
        TutorialTests.RemoveMarkers();
        TutorialTests.stopHighlight(BandageStep.lockedDoor);
        for i,v in ipairs(BandageStep.containers) do
            TutorialTests.stopHighlight(v);
        end
        TutorialTests.addMarker(getSquare(176, 154, 0), 1)
    end
    
    if complete then
        BandageStep.lockedDoor:ToggleDoorSilent();
        TutorialTests.RemoveMarkers();
    
        getPlayer():setIgnoreContextKey(true);
    
        SneakStep.setZoom(1.5);
        getPlayer():setDir(IsoDirections.E);
    
        getPlayerInventory(0).isCollapsed = true;
        getPlayerInventory(0):setMaxDrawHeight(getPlayerInventory(0):titleBarHeight());
        getPlayerLoot(0).isCollapsed = true;
        getPlayerLoot(0):setMaxDrawHeight(getPlayerInventory(0):titleBarHeight());
    end
    
    return complete;
end

function BandageStep:isComplete()
    return TutorialStep.isComplete(self);
end

function BandageStep:finish()
    BandageStep.finished = true;
    TutorialStep.finish(self);
end

ShotgunStep = TutorialStep:derive("ShotgunStep");
ShotgunStep.tickBeforeHordeSpawn = 0;
ShotgunStep.hassprintedTimer = 0;
ShotgunStep.soundTimer = 0;

function ShotgunStep:new () local o = {} setmetatable(o, self)    self.__index = self    return o end

function ShotgunStep:begin()
    JoypadState.disableControllerPrompt = false;
    JoypadState.disableMovement = false;
    JoypadState.disableYInventory = false;
    ISBackButtonWheel.disablePlayerInfo = false;
    JoypadState.disableInvInteraction = false;
    JoypadState.disableClimbOver = false;
    if getCore():getDebug() and not BandageStep.finished then
        getPlayer():setAuthorizeMeleeAction(false);
        getPlayer():setIgnoreInputsForDirection(false);
        local bag = InventoryItemFactory.CreateItem("Base.Bag_ALICEpack");
        SneakStep.bag = bag;
        local shotgun = SneakStep.spawnShotgun();
        getPlayer():getInventory():AddItem(shotgun);
        getPlayer():getInventory():AddItem(bag);
        bag:getItemContainer():AddItem("Base.Bandage");
        getPlayer():setWornItem("Back", bag);
        getPlayer():setPrimaryHandItem(shotgun);
        getPlayer():setSecondaryHandItem(shotgun);
    
        BandageStep.spawnBrothers();
        
        getPlayer():setX(180);
        getPlayer():setY(155);
    
        if not JoypadState.players[1] then
            getPlayerInventory(0):setVisible(true);
            getPlayerLoot(0):setVisible(true);
        end
        getPlayer():setZombieKills(2);
    end
    
    getPlayer():setAllowRun(true);
    getPlayer():setIgnoreContextKey(true);
    ShotgunStep.lockedX = getPlayer():getX();
    ShotgunStep.lockedY = getPlayer():getY();
    getPlayer():LevelPerk(Perks.Fitness);getPlayer():LevelPerk(Perks.Fitness);getPlayer():LevelPerk(Perks.Fitness);getPlayer():LevelPerk(Perks.Fitness);getPlayer():LevelPerk(Perks.Fitness);getPlayer():LevelPerk(Perks.Fitness);
    
    ShotgunStep.squares = {};
    for x=183, 185 do
        local sq = getSquare(x, 153, 0);
        local objs = sq:getObjects();
        local obj = sq:getWall(true);
        if obj then
            TutorialTests.highlight(obj, 0.1);
        end
        table.insert(ShotgunStep.squares, sq);
    end
    
    TutorialTests.addMarker(getSquare(184, 154, 0), 2);

    ShotgunStep.outhouseSQ = {};
    for x=178, 184 do
        for y=162, 168 do
            local sq = getSquare(x, y, 0);
            table.insert(ShotgunStep.outhouseSQ, sq);
        end
    end
    
    if JoypadState.players[1] then
        self:addMessage(getText("IGUI_Tutorial1_Shotgun1Joypad"),  getCore():getScreenWidth()/2, 250 + getCore():getScreenHeight()/2, 420, 110, false, ShotgunStep.OnSquare);
        self:addMessage(getText("IGUI_Tutorial1_Shotgun1bJoypad"),  getCore():getScreenWidth()/2, 250 + getCore():getScreenHeight()/2, 420, 110, false, ShotgunStep.ClimbedFence);
        self:addMessage(getText("IGUI_Tutorial1_Shotgun2Joypad"),  getCore():getScreenWidth()/2, 150 + getCore():getScreenHeight()/2, 420, 110, false, ShotgunStep.Aiming);
        self:addMessage(getText("IGUI_Tutorial1_Shotgun3Joypad"),  getCore():getScreenWidth()/2, 250 + getCore():getScreenHeight()/2, 420, 110, false, ShotgunStep.BrothersDead);
        self:addMessage(getText("IGUI_Tutorial1_Shotgun4Joypad"),  getCore():getScreenWidth()/2, 300 + getCore():getScreenHeight()/2, 420, 110, false, ShotgunStep.BackOverFence);
        self:addMessage(getText("IGUI_Tutorial1_Shotgun5Joypad"),  getCore():getScreenWidth()/2, 150 + getCore():getScreenHeight()/2, 420, 110, false, ShotgunStep.Sprinted);
        self:addMessage(getText("IGUI_Tutorial1_Shotgun6Joypad"),  getCore():getScreenWidth()/2, 250 + getCore():getScreenHeight()/2, 420, 110, true);
        self:addMessage(getText("IGUI_Tutorial1_Shotgun7Joypad"),  getCore():getScreenWidth()/2, 250 + getCore():getScreenHeight()/2, 420, 110, false, ShotgunStep.isPlayedDeadJoypad);
        self:addMessage(getText("IGUI_Tutorial1_Shotgun8Joypad"),  getCore():getScreenWidth()/2, 50 + getCore():getScreenHeight()/2, 420, 110, false, ShotgunStep.TheEnd);
    else
        self:addMessage(getText("IGUI_Tutorial1_Shotgun1", getKeyName(getCore():getKey("Crouch"))),  getCore():getScreenWidth()/2, 250 + getCore():getScreenHeight()/2, 420, 110, false, ShotgunStep.OnSquare);
        self:addMessage(getText("IGUI_Tutorial1_Shotgun1b", getKeyName(getCore():getKey("Interact"))),  getCore():getScreenWidth()/2, 250 + getCore():getScreenHeight()/2, 420, 110, false, ShotgunStep.ClimbedFence);
        self:addMessage(getText("IGUI_Tutorial1_Shotgun2"),  getCore():getScreenWidth()/2, 150 + getCore():getScreenHeight()/2, 420, 110, false, ShotgunStep.Aiming);
        self:addMessage(getText("IGUI_Tutorial1_Shotgun3"),  getCore():getScreenWidth()/2, 250 + getCore():getScreenHeight()/2, 420, 110, false, ShotgunStep.BrothersDead);
        self:addMessage(getText("IGUI_Tutorial1_Shotgun4", getKeyName(getCore():getKey("Interact"))),  getCore():getScreenWidth()/2, 300 + getCore():getScreenHeight()/2, 420, 110, false, ShotgunStep.BackOverFence);
        local sprintText = getText("IGUI_Tutorial1_Shotgun5", Tutorial1.moveKeys, getKeyName(getCore():getKey("Sprint")))
        if getCore():getKey("Sprint") == 0 then
            if getCore():isOptiondblTapJogToSprint() then
                sprintText = getText("IGUI_Tutorial1_Shotgun5a", Tutorial1.moveKeys, getKeyName(getCore():getKey("Run")))
            else
                -- impossible to sprint
            end
        end
        self:addMessage(sprintText,  getCore():getScreenWidth()/2, 150 + getCore():getScreenHeight()/2, 420, 110, false, ShotgunStep.Sprinted);
        self:addMessage(getText("IGUI_Tutorial1_Shotgun6", getKeyName(getCore():getKey("Shout"))),  getCore():getScreenWidth()/2, 250 + getCore():getScreenHeight()/2, 420, 110, false, ShotgunStep.Shout);
        self:addMessage(getText("IGUI_Tutorial1_Shotgun7"),  getCore():getScreenWidth()/2, 250 + getCore():getScreenHeight()/2, 420, 110, false, ShotgunStep.isPlayedDead);
        self:addMessage(getText("IGUI_Tutorial1_Shotgun8", getKeyName(getCore():getKey("Toggle Survival Guide"))),  getCore():getScreenWidth()/2, 50 + getCore():getScreenHeight()/2, 420, 110, false, ShotgunStep.TheEnd);
    end
    self:doMessage();
end

function ShotgunStep.TheEnd()
    if not ShotgunStep.timeOfDeath then
        ShotgunStep.timeOfDeath = getTimestamp()
    end
    if not ShotgunStep.soundDone and ShotgunStep.timeOfDeath + 15 > getTimestamp() then
        addSound(getPlayer(), getPlayer():getX() + 20, getPlayer():getY() + 20, 0, 100, 100); -- Move the zed away to see the reanimation
        ShotgunStep.soundDone = true;
    end
end

function ShotgunStep.Outhouse()
    return false;
end

function ShotgunStep.BackOverFence()
    getPlayer():setIgnoreContextKey(false);
    if not ShotgunStep.vaulted and getPlayer():getCurrentState():equals(ClimbOverWallState.instance()) then
        ShotgunStep.vaulted = true;
    end
    local complete = ShotgunStep.vaulted and (getPlayer():getCurrentSquare():getY() == 153 or getPlayer():getCurrentSquare():getY() == 154) and getPlayer():getCurrentState():equals(IdleState.instance());
    if complete then
        SneakStep.setZoom(1.5);
        for x,v in ipairs(ShotgunStep.squares) do
            local objs = v:getObjects();
            for i = 0, objs:size()-1 do
                TutorialTests.stopHighlight(objs:get(i));
            end
        end
    
        TutorialTests.RemoveMarkers();
        TutorialTests.addMarker(getSquare(181, 165, 0), 4)
    end
    return complete;
end

function ShotgunStep.ClimbedFence()
    getPlayer():setIgnoreAutoVault(false);
    getPlayer():setIgnoreContextKey(false);
--    if getPlayer():getDir() ~= IsoDirections.N then
--        getPlayer():setIgnoreInputsForDirection(false);
--        getPlayer():setDir(IsoDirections.N);
--    else
--        getPlayer():setIgnoreInputsForDirection(true);
--        ShotgunStep.block = true;
--    end
--    if ShotgunStep.block then
--        getPlayer():setIgnoreInputsForDirection(true);
--    end
    if not ShotgunStep.vaulted and getPlayer():getCurrentState():equals(ClimbOverWallState.instance()) then
        ShotgunStep.vaulted = true;
    end
    local complete = ShotgunStep.vaulted and (getPlayer():getCurrentSquare():getY() == 152 or getPlayer():getCurrentSquare():getY() == 151) and getPlayer():getCurrentState():equals(IdleState.instance());
    if complete then
        getPlayer():setIgnoreContextKey(true);
        getPlayer():setSneaking(false);
        for x,v in ipairs(ShotgunStep.squares) do
            local objs = v:getObjects();
            for i = 0, objs:size()-1 do
                TutorialTests.stopHighlight(objs:get(i));
            end
        end
        TutorialTests.RemoveMarkers();
        BandageStep.brother1:setForceEatingAnimation(false);
    end
    return complete;
end

function ShotgunStep.Sprinted()
    getPlayer():setSneaking(false);
    getPlayer():setIgnoreContextKey(true);
    getPlayer():setAllowSprint(true);
    getPlayer():setIgnoreInputsForDirection(false);
    local complete = getPlayer():isSprinting();
    if complete then
        ShotgunStep.hassprintedTimer = ShotgunStep.hassprintedTimer + 1;
    end
    local onSQ = false;
    if ShotgunStep.hassprintedTimer >= 5 then
        for i,v in ipairs(ShotgunStep.outhouseSQ) do
            if v == getPlayer():getCurrentSquare() then
                onSQ = true;
                break;
            end
        end
    end
    
    if onSQ then
        SneakStep.setZoom(1);
        getPlayer():setIgnoreContextKey(false);
        TutorialTests.RemoveMarkers();
        getPlayer():setIgnoreInputsForDirection(true);
    end

    return onSQ;
end

function ShotgunStep.OnSquare()
    if getPlayer():isSneaking() then
        getPlayer():setIgnoreInputsForDirection(false);
        ShotgunStep.sneaked = true;
    end
    if not ShotgunStep.sneaked then
        getPlayer():setIgnoreInputsForDirection(true);
    end

    local complete = false;
    for i,v in ipairs(ShotgunStep.squares) do
        if getPlayer():getSquare() == v then
            complete = true;
            break;
        end
    end
    
    if complete then
        TutorialTests.RemoveMarkers();
        getPlayer():setDir(IsoDirections.N);
    end
    
    return complete;
end

function ShotgunStep:Aiming()
    local complete = getPlayer():isAiming();
    getPlayer():setSneaking(false);
    if complete then
        getSoundManager():playMusic("NewMusic_Surrounded");
        BandageStep.brother2:setForceEatingAnimation(false);
        getPlayer():setIgnoreInputsForDirection(false);
        ShotgunStep.brotherWakeupTimer = 25;
        BandageStep.brother1:setImmortalTutorialZombie(false);
        BandageStep.brother2:setImmortalTutorialZombie(false);
    end
    
    return complete;
end

function ShotgunStep.BrothersDead()
    ShotgunStep.brotherWakeupTimer = ShotgunStep.brotherWakeupTimer-1;
    if ShotgunStep.brotherWakeupTimer == 0 then
        BandageStep.brother1:setUseless(false);
        BandageStep.brother2:setUseless(false);
        getCore():setCollideZombies(true);
        addSound(getPlayer(), getPlayer():getX(), getPlayer():getY(), 0, 100, 100);
    end
    
    getPlayer():setSneaking(false);
    getPlayer():setAuthorizeMeleeAction(true);
    SneakStep.shotgun:setCurrentAmmoCount(6);
    SneakStep.shotgun:setCondition(SneakStep.shotgun:getConditionMax());
    local complete = BandageStep.isBrothersDead;
    if complete then
        getPlayer():setAuthorizeMeleeAction(false);
        getCore():setCollideZombies(false);
        getPlayer():setDir(IsoDirections.S);
    
        ShotgunStep.squares = {};
        for x=183, 185 do
            local sq = getSquare(x, 153, 0);
            local objs = sq:getObjects();
            local obj = sq:getWall(true);
            if obj then
                TutorialTests.highlight(obj, 0.2);
            end
            table.insert(ShotgunStep.squares, sq);
        end
    
        TutorialTests.addMarker(getSquare(184, 154, 0), 2);
    
        for i=0, 8 do
            getPlayer():addBlood(nil, false, true, false);
        end
        
    end
    return complete;
end


function ShotgunStep:Shout()
    getPlayer():setSneaking(false);
    getPlayer():setCanShout(true);
    getPlayer():setIgnoreInputsForDirection(true);
    local complete = getPlayer():isSpeaking() or ShotgunStep.forceSpawnHorde;
    if complete then
        spawnHorde(200, 141, 210, 165, 0, 100);
        spawnHorde(176, 190, 202, 202, 0, 100);
        spawnHorde(169, 130, 195, 115, 0, 100);
        spawnHorde(154, 164, 162, 186, 0, 100);
        
        getPlayer():setInvincible(false);
        getPlayer():setUnlimitedEndurance(false);
        -- open easter egges door
        Tutorial1.openDoor(193, 194, 0, true)
        Tutorial1.openDoor(192, 199, 0, false)
        Tutorial1.openDoor(106, 128, 0, true)
        Tutorial1.openDoor(103, 124, 0, true)
        addZombiesInOutfitArea(103, 120, 105, 122, 0, 3, "Doctor", nil);
        addZombiesInOutfitArea(193, 197, 196, 201, 0, 2, "HospitalPatient", nil);
        
        getPlayer():setIgnoreInputsForDirection(false);
        getPlayer():setAuthorizeMeleeAction(true);
        getPlayer():setIgnoreContextKey(false);
        getPlayer():setIgnoreAutoVault(false);
        getCore():setCollideZombies(true);
        getCore():setTutorialDone(true);
        getCore():saveOptions();
    
        Tutorial1.unlockDoor(182, 165, 0);
    
        JoypadState.disableClimbOver = false;
        JoypadState.disableSmashWindow = false;
        JoypadState.disableReload = false;
        JoypadState.disableGrab = false;
        JoypadState.disableInvInteraction = false;
        JoypadState.disableYInventory = false;
        JoypadState.disableControllerPrompt = false;
        JoypadState.disableMovement = false;
        ISBackButtonWheel.disablePlayerInfo = false;
        ISBackButtonWheel.disableCrafting = false;
        ISBackButtonWheel.disableTime = false;
        ISBackButtonWheel.disableMoveable = false;
        ISBackButtonWheel.disableZoomOut = false;
        ISBackButtonWheel.disableZoomIn = false;
        --        end
    end
    return complete;
end

function ShotgunStep.SurvivalGuideOpen()
    getPlayer():setSneaking(false);
    ShotgunStep.soundTimer = ShotgunStep.soundTimer + 1;
    if ShotgunStep.soundTimer == 100 then
        addSound(getPlayer(), getPlayer():getX(), getPlayer():getY(), 0, 100, 100);
        ShotgunStep.soundTimer = 0;
    end
    getPlayer():setSprinting(false);
    SurvivalGuideManager.blockSurvivalGuide = false;
    if SurvivalGuideManager.instance and SurvivalGuideManager.instance.panel:isVisible() then
        return true;
    end
    
    return false;
end
    
function ShotgunStep:isComplete()
    return TutorialStep.isComplete(self);
end

function ShotgunStep:finish()
    ShotgunStep.finished = true;
    TutorialStep.finish(self);
end

function ShotgunStep:isPlayedDead()
    ShotgunStep.soundTimer = ShotgunStep.soundTimer + 1;
    if ShotgunStep.soundTimer == 100 then
        addSound(getPlayer(), getPlayer():getX(), getPlayer():getY(), 0, 100, 100);
        ShotgunStep.soundTimer = 0;
    end
    getPlayer():setIgnoreContextKey(false);
    getPlayer():setIgnoreAutoVault(false);
    if getPlayer():isDead() then
--        if FightStep.timeOfDeath + 200 > getTimestamp() then
--            TutorialMessage.instance:setVisible(false)
--        end
        return true;
    else
        addSound(getPlayer(), getPlayer():getX(), getPlayer():getY(), 0, 100, 100);
    end
    return false;
end

function ShotgunStep:isPlayedDeadJoypad()
    getPlayer():setCanShout(true);
    if not ShotgunStep.spawnedHorde then
        getPlayer():Callout();
        ShotgunStep.forceSpawnHorde = true;
        ShotgunStep:Shout();
        ShotgunStep.spawnedHorde = true;
    end
    ShotgunStep.soundTimer = ShotgunStep.soundTimer + 1;
    if ShotgunStep.soundTimer == 100 then
        addSound(getPlayer(), getPlayer():getX(), getPlayer():getY(), 0, 100, 100);
        ShotgunStep.soundTimer = 0;
    end
    getPlayer():setIgnoreContextKey(false);
    getPlayer():setIgnoreAutoVault(false);
    if getPlayer():isDead() then
        --        if FightStep.timeOfDeath + 200 > getTimestamp() then
        --            TutorialMessage.instance:setVisible(false)
        --        end
        return true;
    else
        addSound(getPlayer(), getPlayer():getX(), getPlayer():getY(), 0, 100, 100);
    end
    return false;
end

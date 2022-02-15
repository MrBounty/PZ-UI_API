-------------------------------------------------
-------------------------------------------------
--
-- ISSearchWindow
--
-- eris
--
-------------------------------------------------
-------------------------------------------------
require "ISUI/ISCollapsableWindow";
require "Foraging/ISSearchManager";
require "Foraging/ISZoneDisplay";
-------------------------------------------------
-------------------------------------------------
---@class ISSearchWindow : ISCollapsableWindow
ISSearchWindow = ISCollapsableWindow:derive("ISSearchWindow");
ISSearchWindow.players = {};
-------------------------------------------------
-------------------------------------------------
function ISSearchWindow:update()
    if self.manager.isSearchMode then
       self.toggleSearchMode.title = getText("UI_disable_search_mode");
    else
        self.toggleSearchMode.title = getText("UI_enable_search_mode");
    end;
    if ISSearchManager.showDebug and self.manager.isSearchMode then
        local currentZone = self.manager.currentZone;
        if currentZone and currentZone.name then
            local title = "DEBUG: " .. currentZone.name;
            local itemsLeft = " - ICONS: (" .. currentZone.itemsLeft .. " / " .. currentZone.itemsTotal .. ")";
            self:setTitle(title .. itemsLeft);
        else
            self:setTitle("No Zone Here");
        end;
    else
        self:setTitle(getText("UI_investigate_area_window_title"));
    end;
end
-------------------------------------------------
-------------------------------------------------
function ISSearchWindow:initialise()
    ISPanel.initialise(self);
    self.zoneDisplay = ISZoneDisplay:new(self);
    self:addChild(self.zoneDisplay);
    self.toggleSearchMode = ISButton:new(0, self.zoneDisplay:getBottom() + 2, 300, 20, getText("UI_enable_search_mode"), self.manager, ISSearchManager.toggleSearchMode);
    self:addChild(self.toggleSearchMode);
    self:addToUIManager();
    self:setVisible(false);
    self:update();
    self:setHeight(self.toggleSearchMode:getBottom());
    self:bringToTop();

    self:setInfo(getText("UI_SearchWindows_Tooltip"));

    ISLayoutManager.RegisterWindow('ISSearchWindow', ISSearchWindow, self);
end
-------------------------------------------------
-------------------------------------------------
function ISSearchWindow:new(_manager)
    local o = ISCollapsableWindow:new(0, 0, 300, 0);
    setmetatable(o, self);
    self.__index = self;

    o.x = 120;
    o.y = 300;
    o.width = 300;
    o.height = 170;

    o.showBackground = true;
    o.showBorder = true;
    o.backgroundColor = {r=0, g=0, b=0, a=1};
    o.borderColor = {r=0.4, g=0.4, b=0.4, a=0};

    o.manager = _manager;
    o.character = _manager.character;
    o.player = _manager.player;
    o.gameTime = getGameTime();
    o.climateManager = getClimateManager();

    o.title = getText("UI_investigate_area_window_title");

    o:setResizable(false);
    o:setDrawFrame(true);

    o:initialise();

    return o;
end
-------------------------------------------------
-------------------------------------------------
function ISSearchWindow.showWindow(_character)
    if not ISSearchWindow.players[_character] then ISSearchWindow.createUI(_character:getPlayerNum()); end;
    local searchWindow = ISSearchWindow.players[_character];
    if searchWindow then
        searchWindow:setVisible(true);
        searchWindow:bringToTop();
    end;
end
-------------------------------------------------
-------------------------------------------------
function ISSearchWindow.createUI(_player)
    local character = getSpecificPlayer(_player);
    if not ISSearchWindow.players[character] then
        ISSearchWindow.players[character] = ISSearchWindow:new(ISSearchManager.getManager(character));
        ISSearchWindow.players[character]:setVisible(false);
        print("[ISSearchWindow] created UI for player " .. _player);
    end;
end

function ISSearchWindow.destroyUI(_character)
    if ISSearchWindow.players[_character] then
        ISSearchWindow.players[_character]:setVisible(false);
        ISSearchWindow.players[_character]:removeFromUIManager();
        ISSearchWindow.players[_character] = nil;
        print("[ISSearchWindow] removed UI for player " .. _character:getPlayerNum());
    end;
end

Events.OnCreatePlayer.Add(ISSearchWindow.createUI);
Events.OnPlayerDeath.Add(ISSearchWindow.destroyUI);

function ISSearchWindow.onFillWorldObjectContextMenu(_player, _context)
    local character = getSpecificPlayer(_player);
    local manager = ISSearchWindow.players[character];
    if manager and (not manager:getIsVisible()) then
        _context:addOption(getText("UI_investigate_area_window_title"), character, ISSearchWindow.showWindow);
    end;
end

Events.OnFillWorldObjectContextMenu.Add(ISSearchWindow.onFillWorldObjectContextMenu);
-------------------------------------------------
-------------------------------------------------

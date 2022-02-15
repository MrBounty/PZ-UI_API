--***********************************************************
--**                    THE INDIE STONE                    **
--**				  Author: turbotutone				   **
--***********************************************************

require "ISUI/ISCollapsableWindow"

---@class ISStorySoundsDebug : ISCollapsableWindow
ISStorySoundsDebug = ISCollapsableWindow:derive("ISStorySoundsDebug");
ISStorySoundsDebug.instance = nil;

function ISStorySoundsDebug:createChildren()
    ISCollapsableWindow.createChildren(self);

    local height = self:titleBarHeight();
    local widget = self:resizeWidgetHeight();
    self.tabpanel = ISTabPanel:new(0, height, self.width, self.height-height-widget);
    self.tabpanel:initialise();

    self.managerTab = ISSLManager:new(0, height, self.width, self.height-height-widget-self.tabpanel.tabHeight);
    self.managerTab:initialise();
    self.tabpanel:addView("SL Manager", self.managerTab);

    self.eventTab = ISSLEvent:new(0, height, self.width, self.height-height-widget-self.tabpanel.tabHeight);
    self.eventTab:initialise();
    self.tabpanel:addView("SL Event", self.eventTab);

    self.eventTab = ISSLSounds:new(0, height, self.width, self.height-height-widget-self.tabpanel.tabHeight);
    self.eventTab:initialise();
    self.tabpanel:addView("SL Sounds", self.eventTab);

    self.tabpanel:activateView("SL Manager");

    self:addChild(self.tabpanel);
end

function ISStorySoundsDebug:update()
    ISCollapsableWindow.update(self);
end

function ISStorySoundsDebug:prerender()
    ISCollapsableWindow.prerender(self);
end


function ISStorySoundsDebug:render()
    ISCollapsableWindow.render(self);
end

function ISStorySoundsDebug:onResize()
    ISUIElement.onResize(self);
    print("resizing");
    local height = self:titleBarHeight();
    local widget = self:resizeWidgetHeight();
    self.tabpanel:setWidth(self.width);
    self.tabpanel:setHeight(self.height-height-widget);
    for k,v in ipairs(self.tabpanel.viewList) do
        if v.view ~=nil then
            v.view:setWidth(self.width);
            v.view:setHeight(self.height-height-widget-self.tabpanel.tabHeight);
        end
    end
end

function ISStorySoundsDebug:new (x, y, width, height)
    local o = ISCollapsableWindow:new(x, y, width, height);
    setmetatable(o, self)
    self.__index = self
    o.minimumWidth = 700;
    o.minimumHeight = 500;
    o.x = x;
    o.y = y;
    o.borderColor = {r=0.4, g=0.4, b=0.4, a=1};
    o.backgroundColor = {r=0, g=0, b=0, a=0.8};
    o.width = width >= o.minimumWidth and width or o.minimumWidth;
    o.height = height >= o.minimumHeight and height or o.minimumHeight;
    o.anchorLeft = true;
    o.anchorRight = false;
    o.anchorTop = true;
    o.anchorBottom = false;
    o.pin = true;
    o.isCollapsed = false;
    o.collapseCounter = 0;
    o.title = "Storyline Sounds debug";
    --o.viewList = {}
    o.resizable = true;
    o.drawFrame = true;

    o.panels = {};
    return o
end

local function activateStorySoundsDebugUI()
    if ISStorySoundsDebug.instance == nil then
        ISStorySoundsDebug.instance = ISStorySoundsDebug:new(50,50,0,0);
    end
    ISStorySoundsDebug.instance:setVisible(true);
    ISStorySoundsDebug.instance:addToUIManager();
end

Events.OnGameStart.Add( function()
    local enable = false;
    if enable and getSLSoundManager():getLuaDebug() then
        local button = ISButton:new((getCore():getScreenWidth()/2)-50, getCore():getScreenHeight()-40, 100,30,"Story Sound Debug",nil, activateStorySoundsDebugUI);
        button:initialise();
        button.backgroundColor = {r=0, g=0, b=0, a=0.0};
        button.backgroundColorMouseOver = {r=1.0, g=1.0, b=1.0, a=0.1};
        button.borderColor = {r=1.0, g=1.0, b=1.0, a=0.3};
        button:addToUIManager();
    end
end);

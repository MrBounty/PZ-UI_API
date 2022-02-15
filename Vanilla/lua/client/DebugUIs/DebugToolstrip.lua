require "ISUI/ISPanel"

local FONT_HGT_SMALL = getTextManager():getFontHeight(UIFont.Small)

DebugToolstrip = ISPanel:derive("DebugToolstrip");

function DebugToolstrip:onMapClick()
    self.mapWindow = StreamMapWindow:new(150, 150, 700+200, 700);
    self.mapWindow:initialise();
    self.mapWindow:addToUIManager();
end

function DebugToolstrip:onOptionsClick()
    self.settingsWindow = DebugOptionsWindow:new(self.debugOptions:getX(), self.debugOptions:getBottom(), 300, 400)
    self.settingsWindow:initialise()
    self.settingsWindow:addToUIManager()
end

function DebugToolstrip:onDebugLog()
    self.debugLogWindow = DebugLogSettings:new(self.debugLog:getX(), self.debugLog:getBottom(), 300, 400)
    self.debugLogWindow:initialise()
    self.debugLogWindow:addToUIManager()
end

function DebugToolstrip:onToggleBreak(index, selected)
    UIManager.setShowLuaDebuggerOnError(selected)
end

function DebugToolstrip:onShowErrors()
    if self.errorsWindow == nil then
        self.errorsWindow = DebugErrorsWindow:new(100, 100, 600, 400)
        self.errorsWindow:initialise()
        self.errorsWindow:instantiate()
    end
    self.errorsWindow:setVisible(true)
    self.errorsWindow:addToUIManager()
end

function DebugToolstrip:prerender()
    ISPanel.prerender(self)
    self.showDebuggerOnError:setSelected(1, UIManager.isShowLuaDebuggerOnError())
end

function DebugToolstrip:createChildren()
    local x = 24;
    self.mapView = ISButton:new(x, 12, 48, 28, "Map", self, DebugToolstrip.onMapClick);
    self.mapView:initialise();
    self:addChild(self.mapView);

    self.debugOptions = ISButton:new(self.mapView:getRight() + 24, 12, 48, 28, "Options", self, DebugToolstrip.onOptionsClick)
    self.debugOptions:initialise()
    self:addChild(self.debugOptions)

    self.debugLog = ISButton:new(self.debugOptions:getRight() + 24, 12, 48, 28, "DebugLog", self, DebugToolstrip.onDebugLog)
    self.debugLog:initialise()
    self:addChild(self.debugLog)

    self.errors = ISButton:new(self.debugLog:getRight() + 24, 12, 48, 28, "Errors", self, DebugToolstrip.onShowErrors)
    self.errors:initialise()
    self:addChild(self.errors)

    local tickBox = ISTickBox:new(self.errors:getRight() + 24, 12, 150, FONT_HGT_SMALL, "", self, DebugToolstrip.onToggleBreak)
    self:addChild(tickBox)
    tickBox:addOption("Break On Error", nil)
    self.showDebuggerOnError = tickBox
end

function DebugToolstrip:new (x, y, width, height)
    local o = ISPanel:new(x, y, width, height);
    setmetatable(o, self)
    self.__index = self
    o.x = x;
    o.y = y;
    o.backgroundColor = {r=0.2, g=0.3, b=0.4, a=0.3};
    o.borderColor = {r=1, g=1, b=1, a=0.7};

    return o
end

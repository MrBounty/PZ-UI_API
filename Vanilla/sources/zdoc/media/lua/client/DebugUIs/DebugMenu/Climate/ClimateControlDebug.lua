--***********************************************************
--**                    THE INDIE STONE                    **
--**				  Author: turbotutone				   **
--***********************************************************
require "DebugUIs/DebugMenu/Base/ISDebugPanelBase";

---@class ClimateControlDebug : ISDebugPanelBase
ClimateControlDebug = ISDebugPanelBase:derive("ClimateControlDebug");
ClimateControlDebug.instance = nil;


function ClimateControlDebug.OnOpenPanel()
    return ISDebugPanelBase.OnOpenPanel(ClimateControlDebug, 100, 100, 800, 600, "CLIMATE CONTROL");
end

function ClimateControlDebug:new(x, y, width, height, title)
    x = getCore():getScreenWidth() / 2 - (width / 2);
    y = getCore():getScreenHeight() / 2 - (height / 2);
    local o = ISDebugPanelBase:new(x, y, width, height, title);
    setmetatable(o, self);
    self.__index = self;
    ISDebugMenu.RegisterClass(self);
    return o;
end

function ClimateControlDebug:initialise()
    ISPanel.initialise(self);
    self:registerPanel("Climate",ClimateOptionsDebug);
    self:registerPanel("New fog", NewFogDebug);
    self:registerPanel("Colors",ClimateColorsDebug);
    self:registerPanel("Weather",ISAdmPanelWeather);
    self:registerPanel("Puddles", PuddlesControl);
    self:registerPanel("Other debuggers",ClimDebuggersPanel);
end


--***********************************************************
--**                    THE INDIE STONE                    **
--**				  Author: turbotutone				   **
--***********************************************************

require "DebugUIs/DebugMenu/Base/ISDebugPanelBase";

ISGeneralDebug = ISDebugPanelBase:derive("ISGeneralDebug");
ISGeneralDebug.instance = nil;

function ISGeneralDebug.OnOpenPanel()
    return ISDebugPanelBase.OnOpenPanel(ISGeneralDebug, 100, 100, 800, 600, "GENERAL DEBUGGERS");
end

function ISGeneralDebug:new(x, y, width, height, title)
    x = getCore():getScreenWidth() / 2 - (width / 2);
    y = getCore():getScreenHeight() / 2 - (height / 2);
    local o = ISDebugPanelBase:new(x, y, width, height, title);
    setmetatable(o, self);
    self.__index = self;
    return o;
end

function ISGeneralDebug:initialise()
    ISPanel.initialise(self);
    self:registerPanel("Game",ISGameDebugPanel);
    self:registerPanel("Moodles and Body",ISStatsAndBody);
    self:registerPanel("General Cheats",ISGeneralCheats);
    self:registerPanel("Blood", ISDebugBlood);
    self:registerPanel("SearchMode", ISSearchMode);
end


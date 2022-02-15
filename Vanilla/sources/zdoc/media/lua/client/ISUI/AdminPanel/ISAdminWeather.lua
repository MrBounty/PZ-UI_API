--***********************************************************
--**                    THE INDIE STONE                    **
--**				  Author: turbotutone				   **
--***********************************************************

require "DebugUIs/DebugMenu/Base/ISDebugPanelBase";

---@class ISAdminWeather : ISDebugPanelBase
ISAdminWeather = ISDebugPanelBase:derive("ISAdminWeather");
ISAdminWeather.instance = nil;

function ISAdminWeather.OnOpenPanel()
    return ISDebugPanelBase.OnOpenPanel(ISAdminWeather, 100, 100, 800, 600, getText("IGUI_Adm_Weather_ClimateControl"));
end

function ISAdminWeather:new(x, y, width, height, title)
    x = getCore():getScreenWidth() / 2 - (width / 2);
    y = getCore():getScreenHeight() / 2 - (height / 2);
    local o = ISDebugPanelBase:new(x, y, width, height, title);
    setmetatable(o, self);
    self.__index = self;
    return o;
end

function ISAdminWeather:initialise()
    ISPanel.initialise(self);
    self:registerPanel(getText("IGUI_Adm_Weather_Climate"),ISAdmPanelClimate);
    self:registerPanel(getText("IGUI_Adm_Weather_Weather"),ISAdmPanelWeather);
end




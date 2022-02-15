--***********************************************************
--**                    THE INDIE STONE                    **
--**				  Author: turbotutone				   **
--***********************************************************

require "DebugUIs/DebugMenu/Base/ISDebugSubPanelBase";

ClimDebuggersPanel = ISDebugSubPanelBase:derive("ClimDebuggersPanel");

function ClimDebuggersPanel:initialise()
    ISPanel.initialise(self);
    self:addButtonInfo("Forecaster", ForecasterDebug.OnOpenPanel);
    self:addButtonInfo("WeatherFx Panel", WeatherFXDebug.OnOpenPanel);
    self:addButtonInfo("Player Temperature", PlayerClimateDebug.OnOpenPanel);
    self:addButtonInfo("Daily values", DailyValuesDebug.OnOpenPanel);
    self:addButtonInfo("Climate plotter", ClimateDebug.OnOpenPanel);
    self:addButtonInfo("Weather plotter", WeatherPeriodDebug.OnOpenPanel);
    self:addButtonInfo("Thunderbug", ThunderDebug.OnOpenPanel);
    self:addButtonInfo("WindTickDebug", WindDebug.OnOpenPanel, 20);
    self:addButtonInfo("Perform simulation test (CURRENT SETTINGS) Note: causes small freeze", ClimDebuggersPanel.OnSimulationButton, 20);
    self:addButtonInfo("Perform simulation test (MOST DRY)", ClimDebuggersPanel.OnSimulationButtonOverride, 0, 1);
    self:addButtonInfo("Perform simulation test (DRY)", ClimDebuggersPanel.OnSimulationButtonOverride, 0, 2);
    self:addButtonInfo("Perform simulation test (NORMAL)", ClimDebuggersPanel.OnSimulationButtonOverride, 0, 3);
    self:addButtonInfo("Perform simulation test (WET)", ClimDebuggersPanel.OnSimulationButtonOverride, 0, 4);
    self:addButtonInfo("Perform simulation test (MOST WET)", ClimDebuggersPanel.OnSimulationButtonOverride, 0, 5);
end

function ClimDebuggersPanel:addButtonInfo(_title, _func, _marginBot, _arg)
    self.buttons = self.buttons or {};

    table.insert(self.buttons, { title = _title, func = _func, arg = _arg, marginBot = (_marginBot or 0) })
end

function ClimDebuggersPanel:createChildren()
    ISPanel.createChildren(self);

    local v, obj;

    local x,y,w,margin = 10,10,self.width-30,5;

    self:initHorzBars(x,w);

    local h = 20;
    --y, obj = ISDebugUtils.addButton(self,"TriggerStorm",x+100,rowY+10,w-200,20,getText("IGUI_climate_TriggerStorm"), ISAdmPanelWeather.onClick);
    if self.buttons then
        for k,v in ipairs(self.buttons)  do
            y, obj = ISDebugUtils.addButton(self,v,x,y+margin,w,h,v.title,ClimDebuggersPanel.onClick);
            if v.marginBot and v.marginBot>0 then
                y = y+v.marginBot;
            end
        end
    end

    self:setScrollHeight(y+10);
end



function ClimDebuggersPanel:prerender()
    ISDebugSubPanelBase.prerender(self);
end

function ClimDebuggersPanel:onClick(_button)
    if _button.customData.func then
        if _button.customData.arg then
            _button.customData.func(_button.customData.arg);
        else
            _button.customData.func();
        end
    end
end

function ClimDebuggersPanel:onSliderChange(_newval, _slider)
end

function ClimDebuggersPanel:onTicked(_index, _selected, _arg1, _arg2, _tickbox)
end

function ClimDebuggersPanel:onTickedValue(_index, _selected, _arg1, _arg2, _tickbox)
end

function ClimDebuggersPanel:update()
    ISPanel.update(self);
end

function ClimDebuggersPanel.OnSimulationButton()
    getClimateManager():execute_Simulation();
end

function ClimDebuggersPanel.OnSimulationButtonOverride(_rainModOverride)
    getClimateManager():execute_Simulation(_rainModOverride);
end

function ClimDebuggersPanel:new(x, y, width, height, doStencil)
    local o = {};
    o = ISDebugSubPanelBase:new(x, y, width, height, doStencil);
    setmetatable(o, self);
    self.__index = self;
    return o;
end


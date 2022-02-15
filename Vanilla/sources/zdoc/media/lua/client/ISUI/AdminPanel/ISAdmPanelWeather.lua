--***********************************************************
--**                    THE INDIE STONE                    **
--**				  Author: turbotutone				   **
--***********************************************************

require "DebugUIs/DebugMenu/Base/ISDebugSubPanelBase";

---@class ISAdmPanelWeather : ISDebugSubPanelBase
ISAdmPanelWeather = ISDebugSubPanelBase:derive("ISAdmPanelWeather");

--************************************************************************--
--** ISAdmPanelWeather:initialise
--**
--************************************************************************--

function ISAdmPanelWeather:createChildren() --:initialise()
    ISPanel.createChildren(self); --initialise(self);

    local x,y = 20,10;

    local w = self.width-40;

    self:initHorzBars(x,w);

    local obj, rowY;
    local lbl;

    y, obj = ISDebugUtils.addLabel(self,"CurrentWeather",x+(w/2),y+5,getText("Current weather"));
    obj.center = true;
    self.lblCurrentWeather = obj;

    rowY = ISDebugUtils.addHorzBar(self,y+5)+4;

    self.bars = {};
    self.bar1 = {
        x = x,
        y = rowY+5,
        w = w,
        h = 20,
        progress = 0.85,
        text = getText("IGUI_climate_total_progress"),
        back = {r=0.05, g=0.05, b=0.05, a=1},
        fill = {r=0, g=0.4, b=0, a=1},
        border ={r=0.5, g=0.5, b=0.5, a=1},
    }
    table.insert(self.bars, self.bar1);

    rowY = self.bar1.y + self.bar1.h;
    self.bar2 = {
        x = x,
        y = rowY+5,
        w = w,
        h = 20,
        progress = 0.3,
        text = getText("IGUI_climate_segment_progress"),
        back = {r=0.05, g=0.05, b=0.05, a=1},
        fill = {r=0, g=0.4, b=0, a=1},
        border ={r=0.5, g=0.5, b=0.5, a=1},
    }
    table.insert(self.bars, self.bar2);

    rowY = self.bar2.y + self.bar2.h;

    rowY, obj = ISDebugUtils.addButton(self,"StopWeather",x+100,rowY+5,w-200,20,getText("IGUI_climate_StopWeather"), ISAdmPanelWeather.onClick);

    rowY = ISDebugUtils.addHorzBar(self,rowY+8)+4;

    rowY, lbl = ISDebugUtils.addLabel(self,"TriggerInfo",x+(w/2),rowY+5,getText("IGUI_climate_TriggerInfo"));
    lbl.center = true;
    rowY, lbl = ISDebugUtils.addLabel(self,"DurationVal",x+(w/2),rowY+5,"0");
    lbl.center = true;
    rowY, obj = ISDebugUtils.addSlider(self,"DurationSlider",x+100,rowY+4,w-200, 18, ISAdmPanelWeather.onSliderChange);
    obj.pretext = getText("IGUI_climate_TriggerPretext");
    obj.valueLabel = lbl;
    obj:setValues(4, 24*10, 1, 1);
    obj:setCurrentValue(0);
    self.sliderDurationSlider = obj;

    rowY, obj = ISDebugUtils.addButton(self,"TriggerStorm",x+100,rowY+10,w-200,20,getText("IGUI_climate_TriggerStorm"), ISAdmPanelWeather.onClick);
    rowY, obj = ISDebugUtils.addButton(self,"TriggerTropical",x+100,rowY+10,w-200,20,getText("IGUI_climate_TriggerTropical"), ISAdmPanelWeather.onClick);
    rowY, obj = ISDebugUtils.addButton(self,"TriggerBlizzard",x+100,rowY+10,w-200,20,getText("IGUI_climate_TriggerBlizzard"), ISAdmPanelWeather.onClick);

    rowY = ISDebugUtils.addHorzBar(self,rowY+8)+4;

    rowY, lbl = ISDebugUtils.addLabel(self,"CustomInfo",x+(w/2),rowY+5,getText("IGUI_climate_CustomInfo"));
    lbl.center = true;
    rowY, lbl = ISDebugUtils.addLabel(self,"CustomStrVal",x+(w/2),rowY+5,"0");
    lbl.center = true;
    rowY, obj = ISDebugUtils.addSlider(self,"CustomStrSlider",x+100,rowY+4,w-200, 18, ISAdmPanelWeather.onSliderChange);
    obj.pretext = getText("IGUI_climate_StrengthPretext");
    obj.valueLabel = lbl;
    obj:setValues(0.1, 1, 0.01, 0.01);
    obj:setCurrentValue(0.8);
    self.sliderCustomStrSlider = obj;

    local tickOptions = {};
    table.insert(tickOptions, { text = getText("IGUI_climate_WarmFront"), ticked = true });
    table.insert(tickOptions, { text = getText("IGUI_climate_ColdFront"), ticked = false });
    rowY, obj = ISDebugUtils.addTickBox(self,"FrontType",x+100,rowY+8,w-200,ISDebugUtils.FONT_HGT_SMALL,"tickbox", tickOptions, ISAdmPanelWeather.onTicked);
    self.tickBoxFrontType = obj;
    self.tickBoxFrontType.selected[1] = true;

    rowY, obj = ISDebugUtils.addButton(self,"Generate",x+100,rowY+10,w-200,20,getText("IGUI_climate_Generate"), ISAdmPanelWeather.onClick);
    rowY = ISDebugUtils.addHorzBar(self,rowY+8)+4;

    self.totalY = rowY;
    --y, obj = self:addButton("Apply",self.width-110,self.height-30,100,20,getText("IGUI_PlayerStats_ReloadOptions"));
end

function ISAdmPanelWeather:prerender()
    ISDebugSubPanelBase.prerender(self);

    local wp = getClimateManager():getWeatherPeriod();

    if (not wp) or (not wp:isRunning()) then
        self.lblCurrentWeather:setName(getText("IGUI_climate_weather_none"));
    else
        self.lblCurrentWeather:setName(getText("IGUI_climate_weather_dur") .. ISDebugUtils.printval(wp:getDuration(),2) );
    end

    for k,v in ipairs(self.bars) do
        if (not wp) or (not wp:isRunning()) then
            v.progress = 0;
        else
            if k==1 then
                v.progress = wp:getTotalProgress();
            else
                v.progress = wp:getStageProgress();
            end
        end
        self:drawRect(v.x, v.y, v.w, v.h, v.back.a, v.back.r, v.back.g, v.back.b);
        self:drawRect(v.x, v.y, v.w*v.progress, v.h, v.fill.a, v.fill.r, v.fill.g, v.fill.b);
        self:drawRectBorder(v.x, v.y, v.w, v.h, v.border.a, v.border.r, v.border.g, v.border.b);

        local xx = (v.x+(v.w/2)) - (getTextManager():MeasureStringX(UIFont.Small, v.text) / 2);
        self:drawText(v.text, xx-1, v.y+4, 0,0,0,1, UIFont.Small);
        self:drawText(v.text, xx-1, v.y+2, 0,0,0,1, UIFont.Small);
        self:drawText(v.text, xx+1, v.y+2, 0,0,0,1, UIFont.Small);
        self:drawText(v.text, xx+1, v.y+4, 0,0,0,1, UIFont.Small);
        self:drawText(v.text, xx, v.y+3, 0.6,0.6,0.6,1, UIFont.Small);
    end
end


function ISAdmPanelWeather:close()
    ISPanel.close(self);
end

function ISAdmPanelWeather:onSliderChange(_newval, _slider)
    if _slider.valueLabel then
        if _slider.pretext then
            if _slider.valueLabel.customData=="DurationVal" then
                --adds 2 hours to display cause system adds a intro and outro period of each 1 hour
                _slider.valueLabel:setName(_slider.pretext..ISDebugUtils.printval(_newval+2,3));
            else
                _slider.valueLabel:setName(_slider.pretext..ISDebugUtils.printval(_newval,3));
            end
        else
            _slider.valueLabel:setName(ISDebugUtils.printval(_newval,3));
        end
        --_slider.valueLabel:setName(ISDebugUtils.printval(_newval,3));
    end
end

function ISAdmPanelWeather:onTicked(_index, _selected, _arg1, _arg2, _tickbox)
    if _tickbox.customData == "FrontType" then
        --print("selected = "..tostring(_index)..", option = "..tostring(_selected))
        _tickbox.selected[1] = _index==1;
        _tickbox.selected[2] = _index==2;
    end
end

function ISAdmPanelWeather:onClick(_button)
    if _button.customData == "StopWeather" then
        if isClient() then
            getClimateManager():transmitStopWeather();
        else
            getClimateManager():stopWeatherAndThunder()
        end
    elseif _button.customData == "TriggerStorm" then
        local dur = self.sliderDurationSlider:getCurrentValue();
        if isClient() then
            getClimateManager():transmitTriggerStorm(dur);
        else
            getClimateManager():triggerCustomWeatherStage(WeatherPeriod.STAGE_STORM,dur);
        end
    elseif _button.customData == "TriggerTropical" then
        local dur = self.sliderDurationSlider:getCurrentValue();
        if isClient() then
            getClimateManager():transmitTriggerTropical(dur);
        else
            getClimateManager():triggerCustomWeatherStage(WeatherPeriod.STAGE_TROPICAL_STORM,dur);
        end
    elseif _button.customData == "TriggerBlizzard" then
        local dur = self.sliderDurationSlider:getCurrentValue();
        if isClient() then
            getClimateManager():transmitTriggerBlizzard(dur);
        else
            getClimateManager():triggerCustomWeatherStage(WeatherPeriod.STAGE_BLIZZARD,dur);
        end
    elseif _button.customData == "Generate" then
        local str = self.sliderCustomStrSlider:getCurrentValue();
        local front = self.tickBoxFrontType.selected[2] and 1 or 0;
        if isClient() then
            getClimateManager():transmitGenerateWeather(str, front);
        else
            getClimateManager():triggerCustomWeather(str, front==0);
        end
    end
end

--************************************************************************--
--** ISAdmPanelWeather:new
--**
--************************************************************************--
function ISAdmPanelWeather:new(x, y, width, height, player)
    local o = {}
    o = ISDebugSubPanelBase:new(x, y, width, height);
    setmetatable(o, self)
    self.__index = self
    o.borderColor = {r=0.4, g=0.4, b=0.4, a=1};
    o.backgroundColor = {r=0, g=0, b=0, a=0.8};
    o.width = width;
    o.height = height;
    o.player = player;
    return o;
end




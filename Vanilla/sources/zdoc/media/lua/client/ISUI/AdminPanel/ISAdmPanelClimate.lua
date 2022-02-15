--***********************************************************
--**                    THE INDIE STONE                    **
--**				  Author: turbotutone				   **
--***********************************************************

require "DebugUIs/DebugMenu/Base/ISDebugSubPanelBase";

---@class ISAdmPanelClimate : ISDebugSubPanelBase
ISAdmPanelClimate = ISDebugSubPanelBase:derive("ISAdmPanelClimate");

local FONT_HGT_MED = getTextManager():getFontHeight(UIFont.Medium)
local FONT_HGT_SMALL = getTextManager():getFontHeight(UIFont.Small)

--************************************************************************--
--** ISAdmPanelClimate:initialise
--**
--************************************************************************--

function ISAdmPanelClimate:createChildren()
    ISPanel.createChildren(self);

    local clim = getClimateManager();

    local x,y = 20,10;

    local tickOptions = {};
    table.insert(tickOptions, { text = getText("IGUI_Adm_Weather_ClimateEnabled"), ticked = false });
    local obj;

    --y, obj = self:addTickBox("ClimEnabled",x,y,self.width-40,ISDebugUtils.FONT_HGT_SMALL,"climate enabled", tickOptions);
    y, obj = ISDebugUtils.addLabel(self,"TickboxInfo",x,y+5,getText("IGUI_Adm_Weather_ClimateEnabledInfo"), UIFont.Small);

    local w = self.width-40;
    local colW = (w/3);
    local col0_x, col1_x, col2_x = 20, 20+colW, 20+(colW*2);

    self:initHorzBars(x,w);
    --self.horzBarX = x;
    --self.horzBarW = w;

    local rowY = y+10;
    local oTitle, oValue, oUi;

    local tickOptions = {};
    table.insert(tickOptions, { text = getText("IGUI_climate_Windspeed"), ticked = false });
    y, oTitle = ISDebugUtils.addTickBox(self,"Wind",col0_x,rowY,colW,ISDebugUtils.FONT_HGT_SMALL,"tickbox", tickOptions,ISAdmPanelClimate.onTicked);
    --y, oTitle = self:addLabel("Wind",col0_x,rowY,"Windspeed");
    y, oValue = ISDebugUtils.addLabel(self,"WindVal",col2_x-20,rowY,"0 kph", UIFont.Small, false);
    y, oUi = ISDebugUtils.addSlider(self,"WindSlider",col2_x,rowY,colW-10, 18,ISAdmPanelClimate.onSliderChange);
    oUi.valueLabel = oValue;
    oUi:setValues(0, clim:getMaxWindspeedKph(), 1, 1);
    oUi:setCurrentValue(20);
    self:addUI("Wind",oTitle,oValue,oUi);

    rowY = ISDebugUtils.addHorzBar(self,y+5)+4;
    local tickOptions = {};
    table.insert(tickOptions, { text = getText("IGUI_climate_Clouds"), ticked = false });
    y, oTitle = ISDebugUtils.addTickBox(self,"Clouds",col0_x,rowY,colW,ISDebugUtils.FONT_HGT_SMALL,"tickbox", tickOptions,ISAdmPanelClimate.onTicked);
    --y, oTitle = self:addLabel("Clouds",col0_x,rowY,"Clouds");
    y, oValue = ISDebugUtils.addLabel(self,"CloudsVal",col2_x-20,rowY,"0", UIFont.Small, false);
    y, oUi = ISDebugUtils.addSlider(self,"CloudsSlider",col2_x,rowY,colW-10, 18,ISAdmPanelClimate.onSliderChange);
    oUi.valueLabel = oValue;
    oUi:setValues(0, 0.35, 0.01, 0.01);
    oUi:setCurrentValue(0);
    self:addUI("Clouds",oTitle,oValue,oUi);

    rowY = ISDebugUtils.addHorzBar(self,y+5)+4;
    local tickOptions = {};
    table.insert(tickOptions, { text = getText("IGUI_climate_Fog"), ticked = false });
    y, oTitle = ISDebugUtils.addTickBox(self,"Fog",col0_x,rowY,colW,ISDebugUtils.FONT_HGT_SMALL,"tickbox", tickOptions,ISAdmPanelClimate.onTicked);
    --y, oTitle = self:addLabel("Fog",col0_x,rowY,"Fog");
    y, oValue = ISDebugUtils.addLabel(self,"FogVal",col2_x-20,rowY,"0", UIFont.Small, false);
    y, oUi = ISDebugUtils.addSlider(self,"FogSlider",col2_x,rowY,colW-10, 18,ISAdmPanelClimate.onSliderChange);
    oUi.valueLabel = oValue;
    oUi:setValues(0, 1, 0.01, 0.01);
    oUi:setCurrentValue(0);
    self:addUI("Fog",oTitle,oValue,oUi);

    rowY = ISDebugUtils.addHorzBar(self,y+5)+4;
    local tickOptions = {};
    table.insert(tickOptions, { text = getText("IGUI_climate_Precipitation"), ticked = false });
    y, oTitle = ISDebugUtils.addTickBox(self,"Precip",col0_x,rowY,colW,ISDebugUtils.FONT_HGT_SMALL,"tickbox", tickOptions,ISAdmPanelClimate.onTicked);
    --y, oTitle = self:addLabel("Precip",col0_x,rowY,"Precipitation");
    y, oValue = ISDebugUtils.addLabel(self,"PrecipVal",col2_x-20,rowY,"0", UIFont.Small, false);
    y, oUi = ISDebugUtils.addSlider(self,"PrecipSlider",col2_x,rowY,colW-10, 18,ISAdmPanelClimate.onSliderChange);
    oUi.valueLabel = oValue;
    oUi:setValues(0, 1, 0.01, 0.01);
    oUi:setCurrentValue(0);
    self:addUI("Precip",oTitle,oValue,oUi);

    rowY = y+5;
    local tickOptions = {};
    table.insert(tickOptions, { text = getText("IGUI_climate_PrecipitationIsSnow"), ticked = false });
    y, oTitle = ISDebugUtils.addTickBox(self,"PrecipIsSnow",col2_x,rowY,colW-10,ISDebugUtils.FONT_HGT_SMALL,"tickbox", tickOptions,ISAdmPanelClimate.onTicked);
    self:addUI("PrecipIsSnow",oTitle,nil,nil);

    self.tempSliderMod = 40;
    rowY = ISDebugUtils.addHorzBar(self,y+5)+4;
    local tickOptions = {};
    table.insert(tickOptions, { text = getText("IGUI_climate_Temperature"), ticked = false });
    y, oTitle = ISDebugUtils.addTickBox(self,"Temp",col0_x,rowY,colW,ISDebugUtils.FONT_HGT_SMALL,"tickbox", tickOptions,ISAdmPanelClimate.onTicked);
    --y, oTitle = self:addLabel("Temp",col0_x,rowY,"Temperature");
    y, oValue = ISDebugUtils.addLabel(self,"TempVal",col2_x-20,rowY,"0", UIFont.Small, false);
    y, oUi = ISDebugUtils.addSlider(self,"TempSlider",col2_x,rowY,colW-10, 18,ISAdmPanelClimate.onSliderChange);
    oUi.valueLabel = oValue;
    oUi:setValues(0, 80, 1, 1);
    oUi:setCurrentValue(0);
    self:addUI("Temp",oTitle,oValue,oUi);

    rowY = ISDebugUtils.addHorzBar(self,y+5)+4;
    local tickOptions = {};
    table.insert(tickOptions, { text = getText("IGUI_climate_Darkness"), ticked = false });
    y, oTitle = ISDebugUtils.addTickBox(self,"Darkness",col0_x,rowY,colW,ISDebugUtils.FONT_HGT_SMALL,"tickbox", tickOptions,ISAdmPanelClimate.onTicked);
    y, oValue = ISDebugUtils.addLabel(self,"DarknessVal",col2_x-20,rowY,"0", UIFont.Small, false);
    y, oUi = ISDebugUtils.addSlider(self,"DarknessSlider",col2_x,rowY,colW-10, 18,ISAdmPanelClimate.onSliderChange);
    oUi.valueLabel = oValue;
    oUi:setValues(0, 1, 0.01, 0.01);
    oUi:setCurrentValue(0);
    self:addUI("Darkness",oTitle,oValue,oUi);

    ----------------------------------- EXTERIOR -----------------------------------

    rowY = ISDebugUtils.addHorzBar(self,y+5)+4;
    local tickOptions = {};
    table.insert(tickOptions, { text = getText("IGUI_climate_Daylight"), ticked = false });
    y, oTitle = ISDebugUtils.addTickBox(self,"Light",col0_x,rowY,colW,ISDebugUtils.FONT_HGT_SMALL,"tickbox", tickOptions,ISAdmPanelClimate.onTicked);
    y, oValue = ISDebugUtils.addLabel(self,"LightValR_ext",col2_x-20,rowY,"0", UIFont.Small, false);
    y, oUi = ISDebugUtils.addSlider(self,"LightSliderR_ext",col2_x,rowY,colW-10, 18,ISAdmPanelClimate.onSliderChange);
    oUi.pretext = "R_exterior: ";
    oUi.valueLabel = oValue;
    oUi:setValues(0, 255, 1, 1, true);
    oUi.currentValue = 0;
    --oUi:setCurrentValue(0);
    self:addUI("LightR_ext",oTitle,oValue,oUi);

    rowY = y+5;
    y, oValue = ISDebugUtils.addLabel(self,"LightValG_ext",col2_x-20,rowY,"0", UIFont.Small, false);
    y, oUi = ISDebugUtils.addSlider(self,"LightSliderG_ext",col2_x,rowY,colW-10, 18,ISAdmPanelClimate.onSliderChange);
    oUi.pretext = "G_exterior: ";
    oUi.valueLabel = oValue;
    oUi:setValues(0, 255, 1, 1, true);
    oUi.currentValue = 0;
    --oUi:setCurrentValue(0);
    self:addUI("LightG_ext",nil,oValue,oUi);

    rowY = y+5;
    y, oValue = ISDebugUtils.addLabel(self,"LightValB_ext",col2_x-20,rowY,"0", UIFont.Small, false);
    y, oUi = ISDebugUtils.addSlider(self,"LightSliderB_ext",col2_x,rowY,colW-10, 18,ISAdmPanelClimate.onSliderChange);
    oUi.pretext = "B_exterior: ";
    oUi.valueLabel = oValue;
    oUi:setValues(0, 255, 1, 1, true);
    oUi.currentValue = 0;
    --oUi:setCurrentValue(0);
    self:addUI("LightB_ext",nil,oValue,oUi);

    rowY = y+5;
    self.LightColorPanel_ext = ISPanel:new(col2_x,rowY,colW-10,15);
    self.LightColorPanel_ext:initialise();
    self.LightColorPanel_ext.backgroundColor = {r=0.0,g=0.0,b=0.0,a=1.0};
    self:addChild(self.LightColorPanel_ext);

    y = self.LightColorPanel_ext:getY() + self.LightColorPanel_ext:getHeight();

    rowY = y+5;
    y, oValue = ISDebugUtils.addLabel(self,"LightValA_ext",col2_x-20,rowY,"0", UIFont.Small, false);
    y, oUi = ISDebugUtils.addSlider(self,"LightSliderA_ext",col2_x,rowY,colW-10, 18,ISAdmPanelClimate.onSliderChange);
    oUi.pretext = "A_exterior: "; --"Intensity: ";
    oUi.valueLabel = oValue;
    oUi:setValues(0, 255, 1, 1, true);
    oUi.currentValue = 0;
    --oUi:setCurrentValue(0);
    self:addUI("LightA_ext",nil,oValue,oUi);

    rowY = y+5;
    self.LightColorPanelAlpha_ext = ISPanel:new(col2_x,rowY,colW-10,15);
    self.LightColorPanelAlpha_ext:initialise();
    self.LightColorPanelAlpha_ext.backgroundColor = {r=0.0,g=0.0,b=0.0,a=1.0};
    self:addChild(self.LightColorPanelAlpha_ext);

    y = self.LightColorPanelAlpha_ext:getY() + self.LightColorPanelAlpha_ext:getHeight();

    ----------------------------------- INTERIOR -----------------------------------

    rowY = y+5;
    y, oValue = ISDebugUtils.addLabel(self,"LightValR_int",col2_x-20,rowY,"0", UIFont.Small, false);
    y, oUi = ISDebugUtils.addSlider(self,"LightSliderR_int",col2_x,rowY,colW-10, 18,ISAdmPanelClimate.onSliderChange);
    oUi.pretext = "R_interior: ";
    oUi.valueLabel = oValue;
    oUi:setValues(0, 255, 1, 1, true);
    oUi.currentValue = 0;
    --oUi:setCurrentValue(0);
    self:addUI("LightR_int",oTitle,oValue,oUi);

    rowY = y+5;
    y, oValue = ISDebugUtils.addLabel(self,"LightValG_int",col2_x-20,rowY,"0", UIFont.Small, false);
    y, oUi = ISDebugUtils.addSlider(self,"LightSliderG_int",col2_x,rowY,colW-10, 18,ISAdmPanelClimate.onSliderChange);
    oUi.pretext = "G_interior: ";
    oUi.valueLabel = oValue;
    oUi:setValues(0, 255, 1, 1, true);
    oUi.currentValue = 0;
    --oUi:setCurrentValue(0);
    self:addUI("LightG_int",nil,oValue,oUi);

    rowY = y+5;
    y, oValue = ISDebugUtils.addLabel(self,"LightValB_int",col2_x-20,rowY,"0", UIFont.Small, false);
    y, oUi = ISDebugUtils.addSlider(self,"LightSliderB_int",col2_x,rowY,colW-10, 18,ISAdmPanelClimate.onSliderChange);
    oUi.pretext = "B_interior: ";
    oUi.valueLabel = oValue;
    oUi:setValues(0, 255, 1, 1, true);
    oUi.currentValue = 0;
    --oUi:setCurrentValue(0);
    self:addUI("LightB_int",nil,oValue,oUi);

    rowY = y+5;
    self.LightColorPanel_int = ISPanel:new(col2_x,rowY,colW-10,15);
    self.LightColorPanel_int:initialise();
    self.LightColorPanel_int.backgroundColor = {r=0.0,g=0.0,b=0.0,a=1.0};
    self:addChild(self.LightColorPanel_int);

    y = self.LightColorPanel_int:getY() + self.LightColorPanel_int:getHeight();

    rowY = y+5;
    y, oValue = ISDebugUtils.addLabel(self,"LightValA_int",col2_x-20,rowY,"0", UIFont.Small, false);
    y, oUi = ISDebugUtils.addSlider(self,"LightSliderA_int",col2_x,rowY,colW-10, 18,ISAdmPanelClimate.onSliderChange);
    oUi.pretext = "A_interior: "; --"Intensity: ";
    oUi.valueLabel = oValue;
    oUi:setValues(0, 255, 1, 1, true);
    oUi.currentValue = 0;
    --oUi:setCurrentValue(0);
    self:addUI("LightA_int",nil,oValue,oUi);

    rowY = y+5;
    self.LightColorPanelAlpha_int = ISPanel:new(col2_x,rowY,colW-10,15);
    self.LightColorPanelAlpha_int:initialise();
    self.LightColorPanelAlpha_int.backgroundColor = {r=0.0,g=0.0,b=0.0,a=1.0};
    self:addChild(self.LightColorPanelAlpha_int);

    y = self.LightColorPanelAlpha_int:getY() + self.LightColorPanelAlpha_int:getHeight();

    ----------------------------------------------------------------------

    rowY = ISDebugUtils.addHorzBar(self,y+5)+4;
    local tickOptions = {};
    table.insert(tickOptions, { text = getText("IGUI_climate_Desaturation"), ticked = false });
    y, oTitle = ISDebugUtils.addTickBox(self,"Desaturation",col0_x,rowY,colW,ISDebugUtils.FONT_HGT_SMALL,"tickbox", tickOptions,ISAdmPanelClimate.onTicked);
    y, oValue = ISDebugUtils.addLabel(self,"DesaturationVal",col2_x-20,rowY,"0", UIFont.Small, false);
    y, oUi = ISDebugUtils.addSlider(self,"DesaturationSlider",col2_x,rowY,colW-10, 18,ISAdmPanelClimate.onSliderChange);
    oUi.valueLabel = oValue;
    oUi:setValues(0, 1, 0.01, 0.01);
    oUi:setCurrentValue(0);
    self:addUI("Desaturation",oTitle,oValue,oUi);


    y, obj = ISDebugUtils.addButton(self,"Apply",self.width-110,self.height-30,100,20,getText("IGUI_PlayerStats_ReloadOptions"), ISAdmPanelClimate.onClick);
end

function ISAdmPanelClimate:addUI(_name, _tickbox, _label, _slider)
    self["tickBox".._name] = _tickbox;
    self["lbl".._name.."Val"] = _label;
    self["slider".._name.."Slider"] = _slider;
end

--[[function ISAdmPanelClimate:addHorzBar(_y)
    if not self.horzBars then
        self.horzBars = {};
    end
    table.insert(self.horzBars,_y);
    return _y;
end--]]

--[[function ISAdmPanelClimate:addSlider(_id,_x,_y, _w, _h, _title)
    self["slider".._id] = ISSliderPanel:new(_x, _y, _w, _h, self, ISAdmPanelClimate.onSliderChange );
    self["slider".._id]:initialise();
    self["slider".._id]:instantiate();
    self["slider".._id].valueLabel = false;
    self["slider".._id].uiID = _id;
    self:addChild(self["slider".._id]);
    return self["slider".._id]:getY() + self["slider".._id]:getHeight(), self["slider".._id];
end

function ISAdmPanelClimate:addTickBox(_id,_x,_y,_w,_h,_title, options)
    self["tickBox".._id] = ISTickBox:new(_x, _y, _w, _h, _title, self, ISAdmPanelClimate.onTicked);
    self["tickBox".._id].choicesColor = {r=1, g=1, b=1, a=1};
    self["tickBox".._id].backgroundColor = {r=0, g=0, b=0, a=0};
    self["tickBox".._id]:initialise();
    self["tickBox".._id]:instantiate();
    self["tickBox".._id].uiID = _id;
    for k,v in ipairs(options) do
        self["tickBox".._id].selected[1] = v.ticked;
        self["tickBox".._id]:addOption(v.text);
    end
    self:addChild(self["tickBox".._id]);
    return self["tickBox".._id]:getY() + self["tickBox".._id]:getHeight(), self["tickBox".._id];
end

function ISAdmPanelClimate:addButton(_id,_x,_y,_w,_h,_title)
    self["btn".._id] = ISButton:new(_x, _y, _w, _h, _title, self, ISAdmPanelClimate.onClick);
    self["btn".._id]:initialise();
    self["btn".._id].backgroundColor = {r=0, g=0, b=0, a=1.0};
    self["btn".._id].backgroundColorMouseOver = {r=0.5, g=0.5, b=0.5, a=1};
    self["btn".._id].borderColor = {r=1.0, g=1.0, b=1.0, a=0.3};
    self["btn".._id].uiID = _id;
    self:addChild(self["btn".._id]);
    return self["btn".._id]:getY() + self["btn".._id]:getHeight(), self["btn".._id];
end

function ISAdmPanelClimate:addLabel(_id,_x,_y,_title,_bLeft)
    self["lbl".._id] = ISLabel:new(_x, _y, ISDebugUtils.FONT_HGT_SMALL, _title, 1, 1, 1, 1.0, UIFont.Small, _bLeft==nil and true or _bLeft);
    self["lbl".._id]:initialise();
    self["lbl".._id]:instantiate();
    self["lbl".._id].uiID = _id;
    self:addChild(self["lbl".._id]);
    return self["lbl".._id]:getY() + self["lbl".._id]:getHeight(), self["lbl".._id];
end--]]

local FLOAT_DESATURATION = 0;
local FLOAT_GLOBAL_LIGHT_INTENSITY = 1;
local FLOAT_NIGHT_STRENGTH = 2;
local FLOAT_PRECIPITATION_INTENSITY = 3;
local FLOAT_TEMPERATURE = 4;
local FLOAT_FOG_INTENSITY = 5;
local FLOAT_WIND_INTENSITY = 6;
local FLOAT_WIND_ANGLE_INTENSITY = 7;
local FLOAT_CLOUD_INTENSITY = 8;
local FLOAT_AMBIENT = 9;
local FLOAT_VIEW_DISTANCE = 10;
local FLOAT_DAYLIGHT_STRENGTH = 11;
local FLOAT_HUMIDITY = 12;
local FLOAT_MAX = 13;

local COLOR_GLOBAL_LIGHT = 0;
local COLOR_NEW_FOG = 1;
local COLOR_MAX = 2;

local BOOL_IS_SNOW = 0;
local BOOL_MAX = 1;


function ISAdmPanelClimate:prerender()
    ISDebugSubPanelBase.prerender(self);

    local clim = getClimateManager();
    if clim then
        local var = clim:getClimateFloat(FLOAT_WIND_INTENSITY);
        self.tickBoxWind.selected[1] = var:isEnableAdmin();
        self.sliderWindSlider:setCurrentValue(var:getAdminValue()*clim:getMaxWindspeedKph());

        var = clim:getClimateFloat(FLOAT_CLOUD_INTENSITY);
        self.tickBoxClouds.selected[1] = var:isEnableAdmin();
        self.sliderCloudsSlider:setCurrentValue(var:getAdminValue());

        var = clim:getClimateFloat(FLOAT_FOG_INTENSITY);
        self.tickBoxFog.selected[1] = var:isEnableAdmin();
        self.sliderFogSlider:setCurrentValue(var:getAdminValue());

        var = clim:getClimateFloat(FLOAT_PRECIPITATION_INTENSITY);
        self.tickBoxPrecip.selected[1] = var:isEnableAdmin();
        self.sliderPrecipSlider:setCurrentValue(var:getAdminValue());

        var = clim:getClimateBool(BOOL_IS_SNOW);
        self.tickBoxPrecipIsSnow.selected[1] = var:isEnableAdmin() and var:getAdminValue(); --clim:issetAdminBool(BOOL_IS_SNOW) and clim:getAdminBool(BOOL_IS_SNOW);

        var = clim:getClimateFloat(FLOAT_TEMPERATURE);
        self.tickBoxTemp.selected[1] = var:isEnableAdmin();
        self.sliderTempSlider:setCurrentValue(var:getAdminValue()+self.tempSliderMod);

        var = clim:getClimateFloat(FLOAT_DAYLIGHT_STRENGTH);
        self.tickBoxDarkness.selected[1] = var:isEnableAdmin();
        self.sliderDarknessSlider:setCurrentValue(1-var:getAdminValue());

        var = clim:getClimateFloat(FLOAT_DESATURATION);
        self.tickBoxDesaturation.selected[1] = var:isEnableAdmin();
        self.sliderDesaturationSlider:setCurrentValue(var:getAdminValue());

        var = clim:getClimateFloat(FLOAT_GLOBAL_LIGHT_INTENSITY);
        self.tickBoxLightR_ext.selected[1] = var:isEnableAdmin();
        --self.sliderLightIntensitySlider:setCurrentValue(var:getAdminValue());

        local col = clim:getClimateColor(COLOR_GLOBAL_LIGHT):getAdminValue():getExterior();
        self.sliderLightR_extSlider:setCurrentValue(col:getRedFloat()*255);
        self.sliderLightG_extSlider:setCurrentValue(col:getGreenFloat()*255);
        self.sliderLightB_extSlider:setCurrentValue(col:getBlueFloat()*255);
        self.sliderLightA_extSlider:setCurrentValue(col:getAlphaFloat()*255);

        col = clim:getClimateColor(COLOR_GLOBAL_LIGHT):getAdminValue():getInterior();
        self.sliderLightR_intSlider:setCurrentValue(col:getRedFloat()*255);
        self.sliderLightG_intSlider:setCurrentValue(col:getGreenFloat()*255);
        self.sliderLightB_intSlider:setCurrentValue(col:getBlueFloat()*255);
        self.sliderLightA_intSlider:setCurrentValue(col:getAlphaFloat()*255);
    end
end


function ISAdmPanelClimate:close()
    ISPanel.close(self);
end

local last = 0;
function ISAdmPanelClimate:onSliderChange(_newval, _slider)
    if _slider.valueLabel then
        if _slider==self.sliderWindSlider then
            _slider.valueLabel:setName(tostring(_newval).." kph");
        elseif _slider==self.sliderTempSlider then
            _slider.valueLabel:setName(tostring(_newval-self.tempSliderMod).." C / "..tostring(Temperature.CelsiusToFahrenheit(_newval-self.tempSliderMod)).." F");
        elseif _slider.pretext then
            _slider.valueLabel:setName(_slider.pretext..ISDebugUtils.printval(_newval,3));
        else
            _slider.valueLabel:setName(ISDebugUtils.printval(_newval,3));
        end
    end
    if self.LightColorPanel_ext and (_slider.customData=="LightSliderR_ext" or _slider.customData=="LightSliderG_ext" or _slider.customData=="LightSliderB_ext") then
        --local r,g,b = self.sliderLightSliderR:getCurrentValue(),self.sliderLightSliderG:getCurrentValue(),self.sliderLightSliderB:getCurrentValue();
        self.LightColorPanel_ext.backgroundColor.r = self.sliderLightR_extSlider:getCurrentValue()/255;
        self.LightColorPanel_ext.backgroundColor.g = self.sliderLightG_extSlider:getCurrentValue()/255;
        self.LightColorPanel_ext.backgroundColor.b = self.sliderLightB_extSlider:getCurrentValue()/255;
    end
    if self.LightColorPanelAlpha_ext and (_slider.customData=="LightSliderA_ext") then
        if last~=self.sliderLightA_extSlider:getCurrentValue() then
            last = self.sliderLightA_extSlider:getCurrentValue();
            print("A = "..tostring(self.sliderLightA_extSlider:getCurrentValue()/255));
        end
        self.LightColorPanelAlpha_ext.backgroundColor.r = self.sliderLightA_extSlider:getCurrentValue()/255;
        self.LightColorPanelAlpha_ext.backgroundColor.g = self.sliderLightA_extSlider:getCurrentValue()/255;
        self.LightColorPanelAlpha_ext.backgroundColor.b = self.sliderLightA_extSlider:getCurrentValue()/255;
    end

    if self.LightColorPanel_int and (_slider.customData=="LightSliderR_int" or _slider.customData=="LightSliderG_int" or _slider.customData=="LightSliderB_int") then
        --local r,g,b = self.sliderLightSliderR:getCurrentValue(),self.sliderLightSliderG:getCurrentValue(),self.sliderLightSliderB:getCurrentValue();
        self.LightColorPanel_int.backgroundColor.r = self.sliderLightR_intSlider:getCurrentValue()/255;
        self.LightColorPanel_int.backgroundColor.g = self.sliderLightG_intSlider:getCurrentValue()/255;
        self.LightColorPanel_int.backgroundColor.b = self.sliderLightB_intSlider:getCurrentValue()/255;
    end
    if self.LightColorPanelAlpha_int and (_slider.customData=="LightSliderA_int") then
        self.LightColorPanelAlpha_int.backgroundColor.r = self.sliderLightA_intSlider:getCurrentValue()/255;
        self.LightColorPanelAlpha_int.backgroundColor.g = self.sliderLightA_intSlider:getCurrentValue()/255;
        self.LightColorPanelAlpha_int.backgroundColor.b = self.sliderLightA_intSlider:getCurrentValue()/255;
    end

    local clim = getClimateManager();
    if _slider.customData=="WindSlider" and clim:getClimateFloat(FLOAT_WIND_INTENSITY) then
        clim:getClimateFloat(FLOAT_WIND_INTENSITY):setAdminValue(_slider:getCurrentValue()/clim:getMaxWindspeedKph());
    elseif _slider.customData=="CloudsSlider" and clim:getClimateFloat(FLOAT_CLOUD_INTENSITY) then
        clim:getClimateFloat(FLOAT_CLOUD_INTENSITY):setAdminValue(_slider:getCurrentValue());
    elseif _slider.customData=="FogSlider" and clim:getClimateFloat(FLOAT_FOG_INTENSITY) then
        clim:getClimateFloat(FLOAT_FOG_INTENSITY):setAdminValue(_slider:getCurrentValue());
    elseif _slider.customData=="PrecipSlider" and clim:getClimateFloat(FLOAT_PRECIPITATION_INTENSITY) then
        clim:getClimateFloat(FLOAT_PRECIPITATION_INTENSITY):setAdminValue(_slider:getCurrentValue());
    elseif _slider.customData=="TempSlider" and clim:getClimateFloat(FLOAT_TEMPERATURE) then
        clim:getClimateFloat(FLOAT_TEMPERATURE):setAdminValue(_slider:getCurrentValue()-self.tempSliderMod);
    elseif _slider.customData=="DarknessSlider" and clim:getClimateFloat(FLOAT_DAYLIGHT_STRENGTH) then
        clim:getClimateFloat(FLOAT_DAYLIGHT_STRENGTH):setAdminValue(1-_slider:getCurrentValue());
        clim:getClimateFloat(FLOAT_NIGHT_STRENGTH):setAdminValue(_slider:getCurrentValue());
        clim:getClimateFloat(FLOAT_AMBIENT):setAdminValue(1-_slider:getCurrentValue());
    elseif _slider.customData=="DesaturationSlider" and clim:getClimateFloat(FLOAT_DESATURATION) then
        clim:getClimateFloat(FLOAT_DESATURATION):setAdminValue(_slider:getCurrentValue());
    --elseif _slider.customData=="LightSliderIntensity" and clim:getClimateFloat(FLOAT_GLOBAL_LIGHT_INTENSITY) then
        --clim:getClimateFloat(FLOAT_GLOBAL_LIGHT_INTENSITY):setAdminValue(_slider:getCurrentValue());
    elseif (_slider.customData=="LightSliderR_ext" or _slider.customData=="LightSliderG_ext" or _slider.customData=="LightSliderB_ext" or _slider.customData=="LightSliderA_ext") then
        local c = clim:getClimateColor(COLOR_GLOBAL_LIGHT):getAdminValue():getExterior();
        local r = _slider.customData=="LightSliderR_ext" and self.sliderLightR_extSlider:getCurrentValue()/255 or c:getRedFloat();
        local g = _slider.customData=="LightSliderG_ext" and self.sliderLightG_extSlider:getCurrentValue()/255 or c:getGreenFloat();
        local b = _slider.customData=="LightSliderB_ext" and self.sliderLightB_extSlider:getCurrentValue()/255 or c:getBlueFloat();
        local a = _slider.customData=="LightSliderA_ext" and self.sliderLightA_extSlider:getCurrentValue()/255 or c:getAlphaFloat();
        clim:getClimateColor(COLOR_GLOBAL_LIGHT):setAdminValueExterior(r,g,b,a);
    elseif (_slider.customData=="LightSliderR_int" or _slider.customData=="LightSliderG_int" or _slider.customData=="LightSliderB_int" or _slider.customData=="LightSliderA_int") then
        local c = clim:getClimateColor(COLOR_GLOBAL_LIGHT):getAdminValue():getInterior();
        local r = _slider.customData=="LightSliderR_int" and self.sliderLightR_intSlider:getCurrentValue()/255 or c:getRedFloat();
        local g = _slider.customData=="LightSliderG_int" and self.sliderLightG_intSlider:getCurrentValue()/255 or c:getGreenFloat();
        local b = _slider.customData=="LightSliderB_int" and self.sliderLightB_intSlider:getCurrentValue()/255 or c:getBlueFloat();
        local a = _slider.customData=="LightSliderA_int" and self.sliderLightA_intSlider:getCurrentValue()/255 or c:getAlphaFloat();
        clim:getClimateColor(COLOR_GLOBAL_LIGHT):setAdminValueInterior(r,g,b,a);
    end
end

function ISAdmPanelClimate:onTicked(_index, _selected, _arg1, _arg2, _tickbox)
    local clim = getClimateManager();
    if _tickbox.customData == "Wind" then
        if clim:getClimateFloat(FLOAT_WIND_INTENSITY):isEnableAdmin() then
            clim:getClimateFloat(FLOAT_WIND_INTENSITY):setEnableAdmin(false);
        else
            local val = self.sliderWindSlider:getCurrentValue();
            clim:getClimateFloat(FLOAT_WIND_INTENSITY):setEnableAdmin(true);
            clim:getClimateFloat(FLOAT_WIND_INTENSITY):setAdminValue(val/clim:getMaxWindspeedKph());
        end
        --self.tickBoxFrontType.selected[1] = _index==1;
    elseif _tickbox.customData == "Clouds" then
        if clim:getClimateFloat(FLOAT_CLOUD_INTENSITY):isEnableAdmin() then
            clim:getClimateFloat(FLOAT_CLOUD_INTENSITY):setEnableAdmin(false);
        else
            local val = self.sliderCloudsSlider:getCurrentValue();
            clim:getClimateFloat(FLOAT_CLOUD_INTENSITY):setEnableAdmin(true);
            clim:getClimateFloat(FLOAT_CLOUD_INTENSITY):setAdminValue(val);
        end
    elseif _tickbox.customData == "Fog" then
        if clim:getClimateFloat(FLOAT_FOG_INTENSITY):isEnableAdmin() then
            clim:getClimateFloat(FLOAT_FOG_INTENSITY):setEnableAdmin(false);
        else
            local val = self.sliderFogSlider:getCurrentValue();
            clim:getClimateFloat(FLOAT_FOG_INTENSITY):setEnableAdmin(true);
            clim:getClimateFloat(FLOAT_FOG_INTENSITY):setAdminValue(val);
        end
    elseif _tickbox.customData == "Precip" then
        if clim:getClimateFloat(FLOAT_PRECIPITATION_INTENSITY):isEnableAdmin() then
            clim:getClimateFloat(FLOAT_PRECIPITATION_INTENSITY):setEnableAdmin(false);
            clim:getClimateFloat(BOOL_IS_SNOW):setEnableAdmin(false);
        else
            local val = self.sliderPrecipSlider:getCurrentValue();
            clim:getClimateFloat(FLOAT_PRECIPITATION_INTENSITY):setEnableAdmin(true);
            clim:getClimateFloat(FLOAT_PRECIPITATION_INTENSITY):setAdminValue(val);
            val = self.tickBoxPrecipIsSnow.selected[1];
            clim:getClimateBool(BOOL_IS_SNOW):setAdminValue(val);
        end
    elseif _tickbox.customData == "PrecipIsSnow" then
        if clim:getClimateFloat(FLOAT_PRECIPITATION_INTENSITY):isEnableAdmin() then
            local val = self.tickBoxPrecipIsSnow.selected[1];
            --print("set admin snow: "..tostring(val))
            clim:getClimateBool(BOOL_IS_SNOW):setEnableAdmin(true);
            clim:getClimateBool(BOOL_IS_SNOW):setAdminValue(val);
        end
    elseif _tickbox.customData == "Temp" then
        if clim:getClimateFloat(FLOAT_TEMPERATURE):isEnableAdmin() then
            clim:getClimateFloat(FLOAT_TEMPERATURE):setEnableAdmin(false);
        else
            local val = self.sliderTempSlider:getCurrentValue()-self.tempSliderMod;
            clim:getClimateFloat(FLOAT_TEMPERATURE):setEnableAdmin(true);
            clim:getClimateFloat(FLOAT_TEMPERATURE):setAdminValue(val);
        end
    elseif _tickbox.customData == "Darkness" then
        if clim:getClimateFloat(FLOAT_DAYLIGHT_STRENGTH):isEnableAdmin() then
            clim:getClimateFloat(FLOAT_DAYLIGHT_STRENGTH):setEnableAdmin(false);
            clim:getClimateFloat(FLOAT_NIGHT_STRENGTH):setEnableAdmin(false);
            clim:getClimateFloat(FLOAT_AMBIENT):setEnableAdmin(false);
        else
            local val = self.sliderDarknessSlider:getCurrentValue();
            clim:getClimateFloat(FLOAT_DAYLIGHT_STRENGTH):setEnableAdmin(true);
            clim:getClimateFloat(FLOAT_DAYLIGHT_STRENGTH):setAdminValue(1-val);
            clim:getClimateFloat(FLOAT_NIGHT_STRENGTH):setEnableAdmin(true);
            clim:getClimateFloat(FLOAT_NIGHT_STRENGTH):setAdminValue(val);
            clim:getClimateFloat(FLOAT_AMBIENT):setEnableAdmin(true);
            clim:getClimateFloat(FLOAT_AMBIENT):setAdminValue(1-val);
        end
    elseif _tickbox.customData == "Desaturation" then
        if clim:getClimateFloat(FLOAT_DESATURATION):isEnableAdmin() then
            clim:getClimateFloat(FLOAT_DESATURATION):setEnableAdmin(false);
        else
            local val = self.sliderDesaturationSlider:getCurrentValue();
            clim:getClimateFloat(FLOAT_DESATURATION):setEnableAdmin(true);
            clim:getClimateFloat(FLOAT_DESATURATION):setAdminValue(val);
        end
    elseif _tickbox.customData == "Light" then
        if clim:getClimateFloat(FLOAT_GLOBAL_LIGHT_INTENSITY):isEnableAdmin() then
            clim:getClimateFloat(FLOAT_GLOBAL_LIGHT_INTENSITY):setEnableAdmin(false);
            clim:getClimateColor(COLOR_GLOBAL_LIGHT):setEnableAdmin(false);
        else
            clim:getClimateFloat(FLOAT_GLOBAL_LIGHT_INTENSITY):setEnableAdmin(true);
            clim:getClimateColor(COLOR_GLOBAL_LIGHT):setEnableAdmin(true);
            --local val = self.sliderLightIntensitySlider:getCurrentValue();
            --clim:setAdminFloat(FLOAT_GLOBAL_LIGHT_INTENSITY,val);
            local r = self.sliderLightR_extSlider:getCurrentValue()/255;
            local g = self.sliderLightG_extSlider:getCurrentValue()/255;
            local b = self.sliderLightB_extSlider:getCurrentValue()/255;
            local a = self.sliderLightA_extSlider:getCurrentValue()/255;
            clim:getClimateColor(COLOR_GLOBAL_LIGHT):setAdminValueExterior(r,g,b,a);
            r = self.sliderLightR_intSlider:getCurrentValue()/255;
            g = self.sliderLightG_intSlider:getCurrentValue()/255;
            b = self.sliderLightB_intSlider:getCurrentValue()/255;
            a = self.sliderLightA_intSlider:getCurrentValue()/255;
            clim:getClimateColor(COLOR_GLOBAL_LIGHT):setAdminValueInterior(r,g,b,a);
            --clim:setAdminColor(COLOR_GLOBAL_LIGHT, r,g,b);
        end
    end
end

function ISAdmPanelClimate:onClick(_button)
    if _button.customData == "Apply" then
        getClimateManager():transmitClientChangeAdminVars();
    end
end

function ISAdmPanelClimate:onMadeActive()
    getClimateManager():transmitRequestAdminVars();
end

--************************************************************************--
--** ISAdmPanelClimate:new
--**
--************************************************************************--
function ISAdmPanelClimate:new(x, y, width, height, player)
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


--***********************************************************
--**                    THE INDIE STONE                    **
--**				  Author: turbotutone				   **
--***********************************************************

require "DebugUIs/DebugMenu/Base/ISDebugSubPanelBase";

local PLR_INDEX = 0;
local ID_FADE_IN = 1;
local ID_FADE_OUT = 2;

ISSearchMode = ISDebugSubPanelBase:derive("ISSearchMode");

function ISSearchMode:initialise()
    ISPanel.initialise(self);
end

function ISSearchMode:createChildren()
    ISPanel.createChildren(self);

    self.allOptions = {};
    self.floatOptions = {};
    self.boolOptions = {};
    self.buttons = {};

    local obj;

    local butH = 20;
    local x,y,w,margin = 10,10,self.width-30,5;

    self:initHorzBars(x,w);

    y, obj = ISDebugUtils.addLabel(self,"game_title",x+(w/2),y,getText("IGUI_SearchMode_Debug_Title"), UIFont.Medium);
    obj.center = true;
    y = ISDebugUtils.addHorzBar(self,y+5)+5;

    local barMod = 3;

    local sm = getSearchMode();
    local psm = sm:getSearchModeForPlayer(0);
    local blur = psm:getBlur();
    local desat = psm:getDesat();
    local radius = psm:getRadius();
    local gradient = psm:getGradientWidth();
    local dark = psm:getDarkness();

    y, obj = self:addBoolOption(getText("IGUI_SearchMode_Debug_Overlay"),x,y,w, "searchmode_enabled");
    y = ISDebugUtils.addHorzBar(self,y+barMod)+barMod;

    y, obj = self:addBoolOption(getText("IGUI_SearchMode_Debug_Mode_Activated"),x,y,w, "searchmode_search_enabled");
    y = ISDebugUtils.addHorzBar(self,y+barMod)+barMod;

    y, obj = self:addBoolOption(getText("IGUI_SearchMode_Debug_Icons"),x,y,w, "searchmode_debug_enabled");
    y = ISDebugUtils.addHorzBar(self,y+barMod)+barMod;

    y, obj = self:addBoolOption(getText("IGUI_SearchMode_Debug_Icons_Extended"),x,y,w, "searchmode_debug_extended_enabled");
    y = ISDebugUtils.addHorzBar(self,y+barMod)+barMod;

    y, obj = self:addBoolOption(getText("IGUI_SearchMode_Debug_Icons_Vision"),x,y,w, "searchmode_debug_vision_enabled");
    y = ISDebugUtils.addHorzBar(self,y+barMod)+barMod;

    --y, obj = self:addButton("Fade in SearchMode",x,y+margin,w,butH,margin,ID_FADE_IN);
    --y, obj = self:addButton("Fade out SearchMode",x,y+margin,w,butH,margin,ID_FADE_OUT);

    y, obj = ISDebugUtils.addLabel(self,"title2",x+(w/2),y,getText("IGUI_SearchMode_Debug_General_Title"), UIFont.Small);
    obj.center = true;
    y = ISDebugUtils.addHorzBar(self,y+5)+5;

    y, obj = self:addFloatOption(getText("IGUI_SearchMode_Debug_General_Fade_Time"),x,y,w,sm, 0, 5, 0.1, "getFadeTime", "setFadeTime", false);
    y = ISDebugUtils.addHorzBar(self,y+barMod)+barMod;

    --y, obj = self:addFloatOption("Shader Max Desaturation",x,y,w,sm, 0, 1, 0.01, "getMaxDesaturation", "setMaxDesaturation", false);
    --y = ISDebugUtils.addHorzBar(self,y+barMod)+barMod;

    y, obj = ISDebugUtils.addLabel(self,"title3",x+(w/2),y,getText("IGUI_SearchMode_Debug_Overlay_Config_Title"), UIFont.Small);
    obj.center = true;
    y = ISDebugUtils.addHorzBar(self,y+5)+5;

    y, obj = self:addFloatOption(getText("IGUI_SearchMode_Debug_Overlay_Config_Radius_Exterior"),x,y,w,radius, radius:getMin(), radius:getMax(), radius:getStepsize(), "getTargetExterior", "setTargetExterior", false);
    --y = ISDebugUtils.addHorzBar(self,y+barMod)+barMod;

    y, obj = self:addFloatOption(getText("IGUI_SearchMode_Debug_Overlay_Config_Radius_Interior"),x,y,w,radius, radius:getMin(), radius:getMax(), radius:getStepsize(), "getTargetInterior", "setTargetInterior", false);
    y = ISDebugUtils.addHorzBar(self,y+barMod)+barMod;

    y, obj = self:addFloatOption(getText("IGUI_SearchMode_Debug_Overlay_Config_Gradient_Exterior"),x,y,w,gradient, gradient:getMin(), gradient:getMax(), gradient:getStepsize(), "getTargetExterior", "setTargetExterior", false);
    --y = ISDebugUtils.addHorzBar(self,y+barMod)+barMod;

    y, obj = self:addFloatOption(getText("IGUI_SearchMode_Debug_Overlay_Config_Gradient_Interior"),x,y,w,gradient, gradient:getMin(), gradient:getMax(), gradient:getStepsize(), "getTargetInterior", "setTargetInterior", false);
    y = ISDebugUtils.addHorzBar(self,y+barMod)+barMod;

    y, obj = self:addFloatOption(getText("IGUI_SearchMode_Debug_Overlay_Config_Blur_Exterior"),x,y,w,blur, blur:getMin(), blur:getMax(), blur:getStepsize(), "getTargetExterior", "setTargetExterior", false);
    --y = ISDebugUtils.addHorzBar(self,y+barMod)+barMod;

    y, obj = self:addFloatOption(getText("IGUI_SearchMode_Debug_Overlay_Config_Blur_Interior"),x,y,w,blur, blur:getMin(), blur:getMax(), blur:getStepsize(), "getTargetInterior", "setTargetInterior", false);
    y = ISDebugUtils.addHorzBar(self,y+barMod)+barMod;

    y, obj = self:addFloatOption(getText("IGUI_SearchMode_Debug_Overlay_Config_Desaturation_Exterior"),x,y,w,desat, desat:getMin(), desat:getMax(), desat:getStepsize(), "getTargetExterior", "setTargetExterior", false);
    --y = ISDebugUtils.addHorzBar(self,y+barMod)+barMod;

    y, obj = self:addFloatOption(getText("IGUI_SearchMode_Debug_Overlay_Config_Desaturation_Interior"),x,y,w,desat, desat:getMin(), desat:getMax(), desat:getStepsize(), "getTargetInterior", "setTargetInterior", false);
    y = ISDebugUtils.addHorzBar(self,y+barMod)+barMod;

    y, obj = self:addFloatOption(getText("IGUI_SearchMode_Debug_Overlay_Config_Darkness_Exterior"),x,y,w,dark, dark:getMin(), dark:getMax(), dark:getStepsize(), "getTargetExterior", "setTargetExterior", false);
    --y = ISDebugUtils.addHorzBar(self,y+barMod)+barMod;

    y, obj = self:addFloatOption(getText("IGUI_SearchMode_Debug_Overlay_Config_Darkness_Interior"),x,y,w,dark, dark:getMin(), dark:getMax(), dark:getStepsize(), "getTargetInterior", "setTargetInterior", false);
    y = ISDebugUtils.addHorzBar(self,y+barMod)+barMod;

    y, obj = ISDebugUtils.addLabel(self,"title3",x+(w/2),y,getText("IGUI_SearchMode_Debug_Overlay_Override_Title"), UIFont.Small);
    obj.center = true;
    y, obj = ISDebugUtils.addLabel(self,"title4",x+(w/2),y,getText("IGUI_SearchMode_Debug_Overlay_Override_Description"), UIFont.Small);
    obj.center = true;
    y = ISDebugUtils.addHorzBar(self,y+5)+5;

    y, obj = self:addBoolOption("Override",x,y,w, "searchmode_override");
    y=y+10;
    y = ISDebugUtils.addHorzBar(self,y+barMod)+barMod;

    y, obj = self:addFloatOption(getText("IGUI_SearchMode_Debug_Overlay_Override_Radius_Exterior"),x,y,w,radius, radius:getMin(), radius:getMax(), radius:getStepsize(), "getExterior", "setExterior", false);
    --y = ISDebugUtils.addHorzBar(self,y+barMod)+barMod;

    y, obj = self:addFloatOption(getText("IGUI_SearchMode_Debug_Overlay_Override_Radius_Interior"),x,y,w,radius, radius:getMin(), radius:getMax(), radius:getStepsize(), "getInterior", "setInterior", false);
    y = ISDebugUtils.addHorzBar(self,y+barMod)+barMod;

    y, obj = self:addFloatOption(getText("IGUI_SearchMode_Debug_Overlay_Override_Gradient_Exterior"),x,y,w,gradient, gradient:getMin(), gradient:getMax(), gradient:getStepsize(), "getExterior", "setExterior", false);
    --y = ISDebugUtils.addHorzBar(self,y+barMod)+barMod;

    y, obj = self:addFloatOption(getText("IGUI_SearchMode_Debug_Overlay_Override_Gradient_Interior"),x,y,w,gradient, gradient:getMin(), gradient:getMax(), gradient:getStepsize(), "getInterior", "setInterior", false);
    y = ISDebugUtils.addHorzBar(self,y+barMod)+barMod;

    y, obj = self:addFloatOption(getText("IGUI_SearchMode_Debug_Overlay_Override_Blur_Exterior"),x,y,w,blur, blur:getMin(), blur:getMax(), blur:getStepsize(), "getExterior", "setExterior", false);
    --y = ISDebugUtils.addHorzBar(self,y+barMod)+barMod;

    y, obj = self:addFloatOption(getText("IGUI_SearchMode_Debug_Overlay_Override_Blur_Interior"),x,y,w,blur, blur:getMin(), blur:getMax(), blur:getStepsize(), "getInterior", "setInterior", false);
    y = ISDebugUtils.addHorzBar(self,y+barMod)+barMod;

    y, obj = self:addFloatOption(getText("IGUI_SearchMode_Debug_Overlay_Override_Desaturation_Exterior"),x,y,w,desat, desat:getMin(), desat:getMax(), desat:getStepsize(), "getExterior", "setExterior", false);
    --y = ISDebugUtils.addHorzBar(self,y+barMod)+barMod;

    y, obj = self:addFloatOption(getText("IGUI_SearchMode_Debug_Overlay_Override_Desaturation_Interior"),x,y,w,desat, desat:getMin(), desat:getMax(), desat:getStepsize(), "getInterior", "setInterior", false);
    y = ISDebugUtils.addHorzBar(self,y+barMod)+barMod;

    y, obj = self:addFloatOption(getText("IGUI_SearchMode_Debug_Overlay_Override_Darkness_Exterior"),x,y,w,dark, dark:getMin(), dark:getMax(), dark:getStepsize(), "getExterior", "setExterior", false);
    --y = ISDebugUtils.addHorzBar(self,y+barMod)+barMod;

    y, obj = self:addFloatOption(getText("IGUI_SearchMode_Debug_Overlay_Override_Darkness_Interior"),x,y,w,dark, dark:getMin(), dark:getMax(), dark:getStepsize(), "getInterior", "setInterior", false);
    y = ISDebugUtils.addHorzBar(self,y+barMod)+barMod;

    self:setScrollHeight(y+10);
end

function ISSearchMode:addButton(_id,_x,_y,_w,_h,_margin,_command,_marginBot)
    local y, obj = ISDebugUtils.addButton(self,_id,_x,_y+_margin,_w,_h,_id,ISSearchMode.onClick);
    if _marginBot and _marginBot>0 then
        y = y+_marginBot;
    end
    local v = { button = obj, command = _command };
    self.buttons[_id] = v;
    obj.customData = v;

    return y, obj;
end

function ISSearchMode:addBoolOption(_id,_x,_y,_w,_tag)
    local y,obj = ISDebugUtils.addLabel(self,_id,_x,_y,_id, UIFont.Small, true);

    local tickOptions = {};
    table.insert(tickOptions, { text = getText("IGUI_SearchMode_Debug_TickBox_Enabled"), ticked = false });
    local y2,obj2 = ISDebugUtils.addTickBox(self,_id,_x+(_w/2),_y,_w/2,ISDebugUtils.FONT_HGT_SMALL,_id,tickOptions,ISSearchMode.onTickedValue);
    --obj2.changeOptionMethod = ClimateOptionsDebug.onTickedValue;

    local v = { title = obj, tickboxValue = obj2, tag=_tag };
    self.boolOptions[_id] = v;
    self.allOptions[_id] = v;
    obj2.customData = v;
    return y, obj2;
end

function ISSearchMode:addFloatOption(_id,_x,_y,_w,_java,_min,_max,_stepsize, _get, _set, _reqIdx)
    local y,obj = ISDebugUtils.addLabel(self,_id,_x,_y,_id, UIFont.Small, true);
    local y2,obj2 = ISDebugUtils.addLabel(self,_id,_x+(_w/2)-20,_y,"0", UIFont.Small, false);
    local y3, obj3 = ISDebugUtils.addSlider(self,_id,_x+(_w/2),_y,_w/2, 18,ISSearchMode.onSliderChange);
    obj3.valueLabel = obj2;
    --obj3:setCurrentValue(0);

    local v = {
        title = obj,
        labelValue = obj2,
        slider = obj3,
        java = _java,
        var = _id,
        min = _min,
        max = _max,
        step = _stepsize or 0.01,
        get = _get or "get".._id,
        set = _set or "set".._id,
        applyMod = 0,
        requiresIndex = _reqIdx,
    };
    self.floatOptions[_id] = v;
    self.allOptions[_id] = v;
    local val;
    if v.requiresIndex then
        val = v.java[v.get](v.java, PLR_INDEX) + v.applyMod;
    else
        val = v.java[v.get](v.java) + v.applyMod;
    end
    --print(v.var.." = "..tostring(val))
    obj3.customData = v;
    obj3:setValues(_min or 0, _max or 1, _stepsize or 0.01, _stepsize or 0.01);
    obj3:setCurrentValue(val);

    return (y2>y3 and y2 or y3), obj3;
end

function ISSearchMode:onClick(_button)
    if _button.customData and _button.customData.command then
        local c = _button.customData.command
        if c==ID_FADE_IN then
            --getSearchMode():FadeIn(PLR_INDEX);
        elseif c==ID_FADE_OUT then
            --getSearchMode():FadeOut(PLR_INDEX);
        end
    end
end

function ISSearchMode:prerender()
    ISDebugSubPanelBase.prerender(self);

    local val;
    for k,v in pairs(self.floatOptions) do
        if v.requiresIndex then
            val = v.java[v.get](v.java, PLR_INDEX);
        else
            val = v.java[v.get](v.java);
        end
        v.slider.currentValue = val + v.applyMod;

        if v.slider.pretext then
            v.labelValue:setName(v.slider.pretext..ISDebugUtils.printval(val,3));
        else
            v.labelValue:setName(ISDebugUtils.printval(val,3));
        end
    end

    for k,v in pairs(self.boolOptions) do
        if v.tag=="searchmode_override" then
            v.tickboxValue.selected[1] = getSearchMode():isOverride(PLR_INDEX);
        elseif v.tag=="searchmode_enabled" then
            v.tickboxValue.selected[1] = getSearchMode():isEnabled(PLR_INDEX);
        elseif v.tag=="searchmode_search_enabled" then
            v.tickboxValue.selected[1] = ISSearchManager.players[getSpecificPlayer(PLR_INDEX)].isSearchMode;
        elseif v.tag=="searchmode_debug_enabled" then
            v.tickboxValue.selected[1] = ISSearchManager.showDebug;
        elseif v.tag=="searchmode_debug_extended_enabled" then
            v.tickboxValue.selected[1] = ISSearchManager.showDebugExtended;
        elseif v.tag=="searchmode_debug_vision_enabled" then
            v.tickboxValue.selected[1] = ISSearchManager.showDebugVision;
        end
    end
end

function ISSearchMode:onSliderChange(_newval, _slider)
    local v = _slider.customData;

    if v.var=="GameSpeed" then
        if _newval<1 then
            _newval = 1;
        end
    end
    if v.requiresIndex then
        v.java[v.set](v.java,PLR_INDEX,_newval-v.applyMod);
    else
        v.java[v.set](v.java,_newval-v.applyMod);
    end
end

function ISSearchMode:onTicked(_index, _selected, _arg1, _arg2, _tickbox)
end

function ISSearchMode:onTickedValue(_index, _selected, _arg1, _arg2, _tickbox)
    local v = _tickbox.customData;
    if v.tag=="searchmode_override" then
        getSearchMode():setOverride(PLR_INDEX, _tickbox.selected[1]);
    elseif v.tag=="searchmode_enabled" then
        getSearchMode():setEnabled(PLR_INDEX, _tickbox.selected[1]);
    elseif v.tag=="searchmode_search_enabled" then
        ISSearchManager.players[getSpecificPlayer(PLR_INDEX)].isSearchMode = _tickbox.selected[1];
    elseif v.tag=="searchmode_debug_enabled" then
        ISSearchManager.showDebug = _tickbox.selected[1];
    elseif v.tag=="searchmode_debug_extended_enabled" then
        ISSearchManager.showDebugExtended = _tickbox.selected[1];
    elseif v.tag=="searchmode_debug_vision_enabled" then
        ISSearchManager.showDebugVision = _tickbox.selected[1];
    end
end

function ISSearchMode:update()
    ISPanel.update(self);
end

function ISSearchMode:new(x, y, width, height, doStencil)
    local o = {};
    o = ISDebugSubPanelBase:new(x, y, width, height, doStencil);
    setmetatable(o, self);
    self.__index = self;
    return o;
end

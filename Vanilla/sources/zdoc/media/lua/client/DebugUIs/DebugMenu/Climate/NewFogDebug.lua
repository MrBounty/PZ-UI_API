--***********************************************************
--**                    THE INDIE STONE                    **
--**				  Author: turbotutone				   **
--***********************************************************

require "DebugUIs/DebugMenu/Base/ISDebugSubPanelBase";

---@class NewFogDebug : ISDebugSubPanelBase
NewFogDebug = ISDebugSubPanelBase:derive("NewFogDebug");

function NewFogDebug:initialise()
    ISPanel.initialise(self);
end

function NewFogDebug:createChildren()
    ISPanel.createChildren(self);

    local clim = getClimateManager();
    self.clim = clim;
    self.newFogID = 5;

    self.allOptions = {};
    self.floats = {};
    self.colors = {};
    self.bools = {};
    self.floatOptions = {};

    local v, obj;

    local x,y,w = 10,10,self.width-30;

    self:initHorzBars(x,w);

    y, obj = ISDebugUtils.addLabel(self,"float_title",x+(w/2),y,"Fog intensity", UIFont.Medium);
    obj.center = true;
    y = ISDebugUtils.addHorzBar(self,y+5)+5;

    local barMod = 3;
    --print("w = "..tostring(w))
    v = clim:getClimateFloat(self.newFogID);
    y, obj = self:addFloatOptionAuto(v:getName(),v,x,y,w)
    --print(v:getName());
    y = ISDebugUtils.addHorzBar(self,y+barMod)+barMod;

    y, obj = self:addBoolOption("RenderOnlyOneRow",x,y,w);
    y = ISDebugUtils.addHorzBar(self,y+barMod)+barMod;

    y, obj = self:addBoolOption("RenderDebugColors",x,y,w);
    y = ISDebugUtils.addHorzBar(self,y+barMod)+barMod;

    y, obj = self:addBoolOption("RenderCurrentLayerOnly",x,y,w);
    y = ISDebugUtils.addHorzBar(self,y+barMod)+barMod;

    y, obj = self:addFloatOption("ColorR",x,y,w, 0,1, 0.05);
    y = ISDebugUtils.addHorzBar(self,y+barMod)+barMod;
    y, obj = self:addFloatOption("ColorG",x,y,w, 0,1, 0.05);
    y = ISDebugUtils.addHorzBar(self,y+barMod)+barMod;
    y, obj = self:addFloatOption("ColorB",x,y,w, 0,1, 0.05);
    y = ISDebugUtils.addHorzBar(self,y+barMod)+barMod;

    y, obj = self:addBoolOption("HighQuality",x,y,w);
    y = ISDebugUtils.addHorzBar(self,y+barMod)+barMod;

    y, obj = self:addBoolOption("EnableLockedEditing",x,y,w);
    y = ISDebugUtils.addHorzBar(self,y+barMod)+barMod;

    y, obj = ISDebugUtils.addLabel(self,"float_title2",x+(w/2),y,"Locked params:", UIFont.Medium);
    obj.center = true;
    y = ISDebugUtils.addHorzBar(self,y+5)+5;

    y, obj = self:addFloatOption("BaseAlpha",x,y,w, 0,1, 0.01);
    y = ISDebugUtils.addHorzBar(self,y+barMod)+barMod;

    y, obj = self:addFloatOption("RenderXrow",x,y,w, 1,10, 1);
    y = ISDebugUtils.addHorzBar(self,y+barMod)+barMod;

    y, obj = self:addFloatOption("RenderXrowFromCenter",x,y,w, 0,20, 1);
    y = ISDebugUtils.addHorzBar(self,y+barMod)+barMod;

    y, obj = self:addFloatOption("SecondLayerAlpha",x,y,w, 0,1, 0.05);
    y = ISDebugUtils.addHorzBar(self,y+barMod)+barMod;

    y, obj = self:addFloatOption("TopAlphaHeight",x,y,w, 0.0,1.0, 0.01);
    y = ISDebugUtils.addHorzBar(self,y+barMod)+barMod;

    y, obj = self:addFloatOption("BottomAlphaHeight",x,y,w, 0.0,1.0, 0.01);
    y = ISDebugUtils.addHorzBar(self,y+barMod)+barMod;

    --y, obj = self:addFloatOption("Octaves",x,y,w, 1,6, 1);
    --y = ISDebugUtils.addHorzBar(self,y+barMod)+barMod;

    y, obj = self:addFloatOption("CircleAlpha",x,y,w, 0,1, 0.01);
    y = ISDebugUtils.addHorzBar(self,y+barMod)+barMod;

    y, obj = self:addFloatOption("CircleRadius",x,y,w, 0,4, 0.01);
    y = ISDebugUtils.addHorzBar(self,y+barMod)+barMod;

    y, obj = ISDebugUtils.addLabel(self,"float_title3",x+(w/2),y,"Other debug:", UIFont.Medium);
    obj.center = true;
    y = ISDebugUtils.addHorzBar(self,y+5)+5;

    y, obj = self:addFloatOption("minXOffset",x,y,w, -20,20, 1);
    y = ISDebugUtils.addHorzBar(self,y+barMod)+barMod;
    y, obj = self:addFloatOption("maxXOffset",x,y,w, -20,20, 1);
    y = ISDebugUtils.addHorzBar(self,y+barMod)+barMod;
    y, obj = self:addFloatOption("maxYOffset",x,y,w, -20,20, 1);
    y = ISDebugUtils.addHorzBar(self,y+barMod)+barMod;
    y, obj = self:addBoolOption("RenderEndOnly",x,y,w);
    y = ISDebugUtils.addHorzBar(self,y+barMod)+barMod;

    self:setScrollHeight(y+10);
end

function NewFogDebug:addFloatOption(_id,_x,_y,_w,_min,_max,_stepsize)
    local y,obj = ISDebugUtils.addLabel(self,_id,_x,_y,_id, UIFont.Small, true);
    local y2,obj2 = ISDebugUtils.addLabel(self,_id,_x+(_w/2)-20,_y,"0", UIFont.Small, false);
    local y3, obj3 = ISDebugUtils.addSlider(self,_id,_x+(_w/2),_y,_w/2, 18,NewFogDebug.onSliderChange);
    obj3.valueLabel = obj2;
    obj3:setValues(_min or 0, _max or 1, _stepsize or 0.01, _stepsize or 0.01);
    obj3:setCurrentValue(0);

    self.floatOptions[_id] = { title = obj, label = obj2, slider = obj3 };
    self.allOptions[_id] = self.floats[_id];

    return y2>y3 and y2 or y3;
end

function NewFogDebug:addBoolOption(_id,_x,_y,_w)
    local y,obj = ISDebugUtils.addLabel(self,_id,_x,_y,_id, UIFont.Small, true);

    local tickOptions = {};
    table.insert(tickOptions, { text = "Enabled", ticked = false });
    local y2,obj2 = ISDebugUtils.addTickBox(self,_id,_x+(_w/2),_y,_w/2,ISDebugUtils.FONT_HGT_SMALL,_id,tickOptions,NewFogDebug.onTickedValue);
    --obj2.changeOptionMethod = ClimateOptionsDebug.onTickedValue;

    self.bools[_id] = { title = obj, tickboxValue = obj2 };
    self.allOptions[_id] = self.bools[_id];
    return y;
end

function NewFogDebug:addFloatOptionAuto(_id,_float,_x,_y,_w)
    local tickOptions = {};
    table.insert(tickOptions, { text = _id, ticked = false });

    local y, obj = ISDebugUtils.addTickBox(self,_id,_x,_y,(_w/2)-30,ISDebugUtils.FONT_HGT_SMALL,_id,tickOptions,NewFogDebug.onTicked);
    local y2,obj2 = ISDebugUtils.addLabel(self,_id,_x+(_w/2)-20,_y,"0", UIFont.Small, false);
    local y3, obj3 = ISDebugUtils.addSlider(self,_id,_x+(_w/2),_y,_w/2, 18,NewFogDebug.onSliderChange);
    obj3.valueLabel = obj2;
    obj3:setValues(_float:getMin(), _float:getMax(), 0.01, 0.01);
    obj3:setCurrentValue(0);

    self.floats[_id] = { option = _float, tickbox = obj, label = obj2, slider = obj3 };
    self.allOptions[_id] = self.floats[_id];

    return y>y3 and y or y3;
end

function NewFogDebug:prerender()
    ISDebugSubPanelBase.prerender(self);

    for k,o in pairs(self.floats) do
        o.tickbox.selected[1] = o.option:isEnableAdmin();
        o.slider:setCurrentValue(o.option:getAdminValue());
    end
    for k,o in pairs(self.floatOptions) do
        if k=="BaseAlpha" then
            o.slider:setCurrentValue(ImprovedFog.getBaseAlpha());
        elseif k=="RenderXrow" then
            o.slider:setCurrentValue(ImprovedFog.getRenderEveryXRow());
        elseif k=="RenderXrowFromCenter" then
            o.slider:setCurrentValue(ImprovedFog.getRenderXRowsFromCenter());
        elseif k=="SecondLayerAlpha" then
            o.slider:setCurrentValue(ImprovedFog.getSecondLayerAlpha());
        elseif k=="TopAlphaHeight" then
            o.slider:setCurrentValue(ImprovedFog.getTopAlphaHeight());
        elseif k=="BottomAlphaHeight" then
            o.slider:setCurrentValue(ImprovedFog.getBottomAlphaHeight());
        elseif k=="Octaves" then
            o.slider:setCurrentValue(ImprovedFog.getOctaves());
        elseif k=="CircleAlpha" then
            o.slider:setCurrentValue(ImprovedFog.getAlphaCircleAlpha());
        elseif k=="CircleRadius" then
            o.slider:setCurrentValue(ImprovedFog.getAlphaCircleRad());
        elseif k=="ColorR" then
            o.slider:setCurrentValue(ImprovedFog.getColorR());
        elseif k=="ColorG" then
            o.slider:setCurrentValue(ImprovedFog.getColorG());
        elseif k=="ColorB" then
            o.slider:setCurrentValue(ImprovedFog.getColorB());
        elseif k=="minXOffset" then
            o.slider:setCurrentValue(ImprovedFog.getMinXOffset());
        elseif k=="maxXOffset" then
            o.slider:setCurrentValue(ImprovedFog.getMaxXOffset());
        elseif k=="maxYOffset" then
            o.slider:setCurrentValue(ImprovedFog.getMaxYOffset());
        end
    end
    for k,o in pairs(self.bools) do
        if k=="RenderOnlyOneRow" then
            o.tickboxValue.selected[1] = ImprovedFog.isRenderOnlyOneRow();
        elseif k=="RenderDebugColors" then
            o.tickboxValue.selected[1] = ImprovedFog.isDrawDebugColors();
        elseif k=="RenderCurrentLayerOnly" then
            o.tickboxValue.selected[1] = ImprovedFog.isRenderCurrentLayerOnly();
        elseif k=="EnableLockedEditing" then
            o.tickboxValue.selected[1] = ImprovedFog.isEnableEditing();
        elseif k=="HighQuality" then
            o.tickboxValue.selected[1] = ImprovedFog.isHighQuality();
        elseif k=="RenderEndOnly" then
            o.tickboxValue.selected[1] = ImprovedFog.isRenderEndOnly();
        end
    end
end

function NewFogDebug:onSliderChange(_newval, _slider)
    local s = self.floatOptions[_slider.customData];
    if s then
        if s.slider.pretext then
            s.label:setName(s.slider.pretext..ISDebugUtils.printval(_newval,3));
        else
            s.label:setName(ISDebugUtils.printval(_newval,3));
        end
        if _slider.customData=="BaseAlpha" then
            ImprovedFog.setBaseAlpha(s.slider:getCurrentValue());
        elseif _slider.customData=="RenderXrow" then
            ImprovedFog.setRenderEveryXRow(s.slider:getCurrentValue());
        elseif _slider.customData=="RenderXrowFromCenter" then
            ImprovedFog.setRenderXRowsFromCenter(s.slider:getCurrentValue());
        elseif _slider.customData=="SecondLayerAlpha" then
            ImprovedFog.setSecondLayerAlpha(s.slider:getCurrentValue());
        elseif _slider.customData=="TopAlphaHeight" then
            ImprovedFog.setTopAlphaHeight(s.slider:getCurrentValue());
        elseif _slider.customData=="BottomAlphaHeight" then
            ImprovedFog.setBottomAlphaHeight(s.slider:getCurrentValue());
        elseif _slider.customData=="Octaves" then
            ImprovedFog.setOctaves(s.slider:getCurrentValue());
        elseif _slider.customData=="CircleAlpha" then
            ImprovedFog.setAlphaCircleAlpha(s.slider:getCurrentValue());
        elseif _slider.customData=="CircleRadius" then
            ImprovedFog.setAlphaCircleRad(s.slider:getCurrentValue());
        elseif _slider.customData=="ColorR" then
            ImprovedFog.setColorR(s.slider:getCurrentValue());
        elseif _slider.customData=="ColorG" then
            ImprovedFog.setColorG(s.slider:getCurrentValue());
        elseif _slider.customData=="ColorB" then
            ImprovedFog.setColorB(s.slider:getCurrentValue());
        elseif _slider.customData=="minXOffset" then
            ImprovedFog.setMinXOffset(s.slider:getCurrentValue());
        elseif _slider.customData=="maxXOffset" then
            ImprovedFog.setMaxXOffset(s.slider:getCurrentValue());
        elseif _slider.customData=="maxYOffset" then
            ImprovedFog.setMaxYOffset(s.slider:getCurrentValue());
        end
        return;
    end
    local s = self.floats[_slider.customData];
    if s then
        if s.slider.pretext then
            s.label:setName(s.slider.pretext..ISDebugUtils.printval(_newval,3));
        else
            s.label:setName(ISDebugUtils.printval(_newval,3));
        end
        s.option:setAdminValue(s.slider:getCurrentValue());
        return;
    end
    s = self.colors[_slider.customData];
    if s then
        s.option:setAdminValue(s.sliderR:getCurrentValue(),s.sliderG:getCurrentValue(),s.sliderB:getCurrentValue(),s.sliderA:getCurrentValue(),
                s.sliderR_int:getCurrentValue(),s.sliderG_int:getCurrentValue(),s.sliderB_int:getCurrentValue(),s.sliderA_int:getCurrentValue());
        s.labelR:setName(s.sliderR.pretext..ISDebugUtils.printval(s.sliderR:getCurrentValue(),3));
        s.labelG:setName(s.sliderG.pretext..ISDebugUtils.printval(s.sliderG:getCurrentValue(),3));
        s.labelB:setName(s.sliderB.pretext..ISDebugUtils.printval(s.sliderB:getCurrentValue(),3));
        s.labelA:setName(s.sliderA.pretext..ISDebugUtils.printval(s.sliderA:getCurrentValue(),3));

        s.labelR_int:setName(s.sliderR_int.pretext..ISDebugUtils.printval(s.sliderR_int:getCurrentValue(),3));
        s.labelG_int:setName(s.sliderG_int.pretext..ISDebugUtils.printval(s.sliderG_int:getCurrentValue(),3));
        s.labelB_int:setName(s.sliderB_int.pretext..ISDebugUtils.printval(s.sliderB_int:getCurrentValue(),3));
        s.labelA_int:setName(s.sliderA_int.pretext..ISDebugUtils.printval(s.sliderA_int:getCurrentValue(),3));
        return;
    end
end

function NewFogDebug:onTicked(_index, _selected, _arg1, _arg2, _tickbox)
    local s = self.allOptions[_tickbox.customData];

    if s.option:isEnableAdmin() then
        s.option:setEnableAdmin(false);
    else
        s.option:setEnableAdmin(true);
    end
end

function NewFogDebug:onTickedValue(_index, _selected, _arg1, _arg2, _tickbox)
    local s = self.allOptions[_tickbox.customData];

    if s then
        if _tickbox.customData=="RenderOnlyOneRow" then
            ImprovedFog.setRenderOnlyOneRow(not ImprovedFog.isRenderOnlyOneRow());
        elseif _tickbox.customData=="RenderDebugColors" then
            ImprovedFog.setDrawDebugColors(not ImprovedFog.isDrawDebugColors());
        elseif _tickbox.customData=="RenderCurrentLayerOnly" then
            ImprovedFog.setRenderCurrentLayerOnly(not ImprovedFog.isRenderCurrentLayerOnly());
        elseif _tickbox.customData=="EnableLockedEditing" then
            ImprovedFog.setEnableEditing(not ImprovedFog.isEnableEditing());
        elseif _tickbox.customData=="HighQuality" then
            ImprovedFog.setHighQuality(not ImprovedFog.isHighQuality());
        elseif _tickbox.customData=="RenderEndOnly" then
            ImprovedFog.setRenderEndOnly(not ImprovedFog.isRenderEndOnly());
        end
    end
end

function NewFogDebug:update()
    ISPanel.update(self);
end

function NewFogDebug:new(x, y, width, height, doStencil)
    local o = {};
    o = ISDebugSubPanelBase:new(x, y, width, height, doStencil);
    setmetatable(o, self);
    self.__index = self;
    return o;
end

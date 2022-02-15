--***********************************************************
--**                    THE INDIE STONE                    **
--**				  Author: turbotutone				   **
--**		  Author: Yuri yuri(a)generalarcade.com        **
--***********************************************************

require "DebugUIs/DebugMenu/Base/ISDebugSubPanelBase";

local FONT_HGT_MED = getTextManager():getFontHeight(UIFont.Medium)
local FONT_HGT_SMALL = getTextManager():getFontHeight(UIFont.Small)

local function roundNum(num, numDecimalPlaces)
    local mult = 10^(numDecimalPlaces or 0)
    return math.floor(num * mult + 0.5) / mult
end

local function clamp01(_n)
    if _n<0 then _n=0; end
    if _n>1 then _n=1; end
    return _n;
end

local function printval(_v, _d)
    return tostring(roundNum(_v,_d));
end

---@class PuddlesControl : ISDebugSubPanelBase
PuddlesControl = ISDebugSubPanelBase:derive("PuddlesControl");

function PuddlesControl:initialise()
    ISPanel.initialise(self);
end

function PuddlesControl:createChildren()
    ISPanel.createChildren(self);

    local puddles = getPuddlesManager();
    self.puddles = puddles;
    self.allOptions = {};
    self.floats = {};
    self.colors = {};
    self.bools = {};

    local v, obj;

    local x,y,w = 10,10,self.width-30;

    self.horzBarX = x;
    self.horzBarW = w;

    y, obj = self:addLabel("float_title",x+(w/2),y,"Puddles floats", UIFont.Medium);
    obj.center = true;
    y = self:addHorzBar(y+5)+5;

    local barMod = 3;
    --print("w = "..tostring(w))
    for i=0,puddles:getFloatMax()-1 do
        v = puddles:getPuddlesFloat(i);
        y, obj = self:addFloatOption(v:getName(),v,x,y,w)
        print(v:getName());
        y = self:addHorzBar(y+barMod)+barMod;
    end

    self:setScrollHeight(y+10);
end


function PuddlesControl:addFloatOption(_id,_float,_x,_y,_w)
    local tickOptions = {};
    table.insert(tickOptions, { text = _id, ticked = false });

    local y, obj = self:addTickBox(_id,_x,_y,(_w/2)-30,FONT_HGT_SMALL,_id,tickOptions);
    local y2,obj2 = self:addLabel(_id,_x+(_w/2)-20,_y,"0", UIFont.Small, false);
    local y3, obj3 = self:addSlider(_id,_x+(_w/2),_y,_w/2, 18);
    obj3.valueLabel = obj2;
    obj3:setValues(_float:getMin(), _float:getMax(), 0.01, 0.01);
    obj3:setCurrentValue(0);

    self.floats[_id] = { option = _float, tickbox = obj, label = obj2, slider = obj3 };
    self.allOptions[_id] = self.floats[_id];

    return y>y3 and y or y3;
end

function PuddlesControl:addTickBox(_id,_x,_y,_w,_h,_title, options)
    local tickBox = ISTickBox:new(_x, _y, _w, _h, _title, self, PuddlesControl.onTicked);
    --self["tickBox".._id] = tickBox;
    tickBox.choicesColor = {r=1, g=1, b=1, a=1};
    tickBox.backgroundColor = {r=0, g=0, b=0, a=0};
    tickBox:initialise();
    tickBox:instantiate();
    tickBox.uiID = _id;
    for k,v in ipairs(options) do
        tickBox.selected[1] = v.ticked;
        tickBox:addOption(v.text);
    end
    self:addChild(tickBox);
    return tickBox:getY() + tickBox:getHeight(), tickBox;
end

function PuddlesControl:addSlider(_id,_x,_y, _w, _h)
    local slider = ISSliderPanel:new(_x, _y, _w, _h, self, PuddlesControl.onSliderChange );
    --self["slider".._id] = slider;
    slider:initialise();
    slider:instantiate();
    slider.valueLabel = false;
    slider.uiID = _id;
    self:addChild(slider);
    return slider:getY() + slider:getHeight(), slider;
end

function PuddlesControl:addLabel(_id,_x,_y,_title, _font,_bLeft)
    local FONT_HGT = getTextManager():getFontHeight(_font);
    local label = ISLabel:new(_x, _y, FONT_HGT, _title, 1, 1, 1, 1.0, _font, _bLeft==nil and true or _bLeft);
    --self["lbl".._id] = label;
    label:initialise();
    label:instantiate();
    label.uiID = _id;
    self:addChild(label);
    return label:getY() + label:getHeight(), label;
end

function PuddlesControl:addButton(_id,_x,_y,_w,_h,_title)
    local button = ISButton:new(_x, _y, _w, _h, _title, self, PuddlesControl.onClick);
    self["btn".._id] = button;
    button:initialise();
    button.backgroundColor = {r=0, g=0, b=0, a=1.0};
    button.backgroundColorMouseOver = {r=0.5, g=0.5, b=0.5, a=1};
    button.borderColor = {r=1.0, g=1.0, b=1.0, a=0.3};
    button.uiID = _id;
    self:addChild(button);
    return button:getY() + button:getHeight(), button;
end

function PuddlesControl:addHorzBar(_y)
    if not self.horzBars then
        self.horzBars = {};
    end
    table.insert(self.horzBars,_y);
    return _y;
end

function PuddlesControl:prerender()
    ISPanel.prerender(self);

    for k,v in ipairs(self.horzBars) do
        self:drawRect(self.horzBarX, v, self.horzBarW, 1, 1.0, 0.2, 0.2, 0.2);
    end

    for k,o in pairs(self.floats) do
        o.tickbox.selected[1] = o.option:isEnableAdmin();
        o.slider:setCurrentValue(o.option:getAdminValue());
    end

    for k,o in pairs(self.colors) do
        o.tickbox.selected[1] = o.option:isEnableAdmin();
        local c = o.option:getAdminValue();
        o.sliderR:setCurrentValue(c:getRedFloat());
        o.sliderG:setCurrentValue(c:getGreenFloat());
        o.sliderB:setCurrentValue(c:getBlueFloat());
        o.colorbox.backgroundColor.r = c:getRedFloat();
        o.colorbox.backgroundColor.g = c:getGreenFloat();
        o.colorbox.backgroundColor.b = c:getBlueFloat();
    end

    for k,o in pairs(self.bools) do
        o.tickbox.selected[1] = o.option:isEnableAdmin();
        o.tickboxValue.selected[1] = o.option:getAdminValue();
    end

    --[[local v,s;
    if self.puddles then
        for i=0,self.puddles:getFloatMax()-1 do
            v = self.puddles:getpuddlesateFloat(i);

            s = self.floats[v:getName()];

            if s then
                s.tickbox.selected[1] = self.puddles:issetAdminFloat(i);
                s.slider:setCurrentValue(self.puddles:getAdminFloat(i));
            end
        end
    end--]]
end

function PuddlesControl:onSliderChange(_newval, _slider)
    local s = self.floats[_slider.uiID];
    if s then
        if s.slider.pretext then
            s.label:setName(s.slider.pretext..printval(_newval,3));
        else
            s.label:setName(printval(_newval,3));
        end
        s.option:setAdminValue(s.slider:getCurrentValue());
    end
    s = self.colors[_slider.uiID];
    if s then
        s.option:setAdminValue(s.sliderR:getCurrentValue(),s.sliderG:getCurrentValue(),s.sliderB:getCurrentValue());
        s.labelR:setName(s.sliderR.pretext..printval(s.sliderR:getCurrentValue(),3));
        s.labelG:setName(s.sliderG.pretext..printval(s.sliderG:getCurrentValue(),3));
        s.labelB:setName(s.sliderB.pretext..printval(s.sliderB:getCurrentValue(),3));
    end
end

function PuddlesControl:onTicked(_index, _selected, _arg1, _arg2, _tickbox)
    local s = self.allOptions[_tickbox.uiID];

    if s.option:isEnableAdmin() then
        s.option:setEnableAdmin(false);
    else
        s.option:setEnableAdmin(true);
    end
end

function PuddlesControl:onTickedValue(_index, _selected, _arg1, _arg2, _tickbox)
    local s = self.allOptions[_tickbox.uiID];

    s.option:setAdminValue(not s.option:getAdminValue());
end

function PuddlesControl:update()
    ISPanel.update(self);
end

function PuddlesControl:close()
    --self:setVisible(false);
    --self:removeFromUIManager();
end

function PuddlesControl:new(x, y, width, height)
    local o = {};
    o = ISPanel:new(x, y, width, height);
    setmetatable(o, self);
    self.__index = self;
    o.variableColor={r=0.9, g=0.55, b=0.1, a=1};
    o.borderColor = {r=0.4, g=0.4, b=0.4, a=1};
    o.backgroundColor = {r=0, g=0, b=0, a=0.8};
    o.buttonBorderColor = {r=0.7, g=0.7, b=0.7, a=0.5};
    return o;
end


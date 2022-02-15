--***********************************************************
--**                    THE INDIE STONE                    **
--**				  Author: turbotutone				   **
--***********************************************************

---@class ISDebugUtils
ISDebugUtils = {};

ISDebugUtils.FONT_HGT_MED = getTextManager():getFontHeight(UIFont.Medium)
ISDebugUtils.FONT_HGT_SMALL = getTextManager():getFontHeight(UIFont.Small)

function ISDebugUtils.roundNum(num, numDecimalPlaces)
    local mult = 10^(numDecimalPlaces or 0)
    return math.floor(num * mult + 0.5) / mult
end

function ISDebugUtils.clamp01(_n)
    if _n<0 then _n=0; end
    if _n>1 then _n=1; end
    return _n;
end

function ISDebugUtils.printval(_v, _d)
    return tostring(ISDebugUtils.roundNum(_v,_d));
end

function ISDebugUtils.onMouseWheel(self, del)
    if self:getScrollHeight() > 0 then
        self:setYScroll(self:getYScroll() - (del * 40))
        return true
    end
    return false
end

function ISDebugUtils.addButton(_self, _data, _x, _y, _w, _h, _title, _func)
    local button = ISButton:new(_x, _y, _w, _h, _title, _self, _func);
    button:initialise();
    button.backgroundColor = {r=0, g=0, b=0, a=1.0};
    button.backgroundColorMouseOver = {r=0.5, g=0.5, b=0.5, a=1};
    button.borderColor = {r=1.0, g=1.0, b=1.0, a=0.3};
    button.customData = _data;
    _self:addChild(button);
    return button:getY() + button:getHeight(), button;
end

function ISDebugUtils.addComboBox(_self, _data, _x, _y, _w, _font, _func)
    local FONT_HGT = getTextManager():getFontHeight(_font);
    local comboBox = ISComboBox:new(_x, _y, _w, FONT_HGT, _self, _func);
    comboBox:initialise();
    comboBox.customData = _data;
    _self:addChild(comboBox);
    return comboBox:getY() + comboBox:getHeight(), comboBox;
end

function ISDebugUtils.addTextEntryBox(_self, _data, _title, _x, _y, _w, _h)
    local entryBox = ISTextEntryBox:new(_title, _x, _y, _w, _h);
    entryBox:initialise();
    entryBox:instantiate();
    entryBox:setText("");
    entryBox.customData = _data;
    _self:addChild(entryBox);
    return entryBox:getY() + entryBox:getHeight(), entryBox;
end

function ISDebugUtils.addLabel(_self, _data, _x, _y, _title, _font, _bLeft)
    local FONT_HGT = getTextManager():getFontHeight(_font);
    local label = ISLabel:new(_x, _y, FONT_HGT, _title, 1, 1, 1, 1.0, _font, _bLeft==nil and true or _bLeft);
    label:initialise();
    label:instantiate();
    label.customData = _data;
    _self:addChild(label);
    return label:getY() + label:getHeight(), label;
end

function ISDebugUtils.addTickBox(_self, _data, _x, _y, _w, _h, _title, options, _func)
    local tickBox = ISTickBox:new(_x, _y, _w, _h, _title, _self, _func);
    tickBox.choicesColor = {r=1, g=1, b=1, a=1};
    tickBox.backgroundColor = {r=0, g=0, b=0, a=0};
    tickBox:initialise();
    tickBox:instantiate();
    tickBox.customData = _data;
    -- Must addChild *before* addOption() or ISUIElement:getKeepOnScreen() will restrict y-position to screen height
    _self:addChild(tickBox);
    for k,v in ipairs(options) do
        tickBox.selected[1] = v.ticked;
        tickBox:addOption(v.text);
    end
    return tickBox:getY() + tickBox:getHeight(), tickBox;
end

function ISDebugUtils.addSlider(_self, _data, _x, _y, _w, _h, _func)
    local slider = ISSliderPanel:new(_x, _y, _w, _h, _self, _func );
    slider:initialise();
    slider:instantiate();
    slider.valueLabel = false;
    slider.customData = _data;
    _self:addChild(slider);
    return slider:getY() + slider:getHeight(), slider;
end

function ISDebugUtils.initHorzBars(_self, _x, _width)
    _self.horzBarX = _x;
    _self.horzBarW = _width;
end

function ISDebugUtils.addHorzBar(_self, _y)
    if not _self.horzBars then
        _self.horzBars = {};
    end
    table.insert(_self.horzBars,_y);
    return _y;
end

function ISDebugUtils.renderHorzBars(_self)
    if (not _self.horzBars) or (not _self.horzBarX) or (not _self.horzBarW) then
        return;
    end
    for k,v in ipairs(_self.horzBars) do
        _self:drawRect(_self.horzBarX, v, _self.horzBarW, 1, 1.0, 0.2, 0.2, 0.2);
    end
end


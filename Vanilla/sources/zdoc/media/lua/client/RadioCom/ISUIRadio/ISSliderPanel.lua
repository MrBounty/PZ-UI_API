--***********************************************************
--**                    THE INDIE STONE                    **
--**				  Author: turbotutone				   **
--***********************************************************
require "ISUI/ISPanel"

---@class ISSliderPanel : ISPanel
ISSliderPanel = ISPanel:derive("ISSliderPanel");

function ISSliderPanel:round(num, idp)
    local mult = 10^(idp or 0);
    return math.floor(num * mult + 0.5) / mult;
end

function ISSliderPanel:initialise()
    ISPanel.initialise(self)
end

function ISSliderPanel:createChildren()
    self:paginate();
end

function ISSliderPanel:paginate()
    if self.target and self.customPaginate then
        self.customPaginate(self.target);
        return;
    end

    if self.doButtons then
        self.btnLeftDim = { x = 0, y = 0, w = 10, h = self:getHeight() };
        self.btnRightDim = { x = self:getWidth()-10, y = 0, w = 10, h = self:getHeight() };

        self.sliderBarDim = { x = 10+5, y = (self:getHeight()/2)-1, w = self:getWidth()-(20+10), h = 2 };
    else
        self.sliderBarDim = { x = 5, y = (self:getHeight()/2)-1, w = self:getWidth()-5, h = 2 };
    end
    self.sliderDim = { x = 2, y = 0, w = 5, h = self:getHeight() } --used a bit differently, see render
end

function ISSliderPanel:onMouseDown(_x, _y)
    if not self:getIsVisible() then return; end

    self.leftPressed = false;
    self.rightPressed = false;
    if self.doButtons then
        local stepVal = isShiftKeyDown() and self.shiftValue or self.stepValue;
        if _x < self.btnLeftDim.w then
            self.leftPressed = true;
            self:setCurrentValue( self.currentValue - stepVal );
        elseif _x > self.btnRightDim.x then
            self.rightPressed = true;
            self:setCurrentValue( self.currentValue + stepVal );
        end
    end
    if _x >= self.sliderBarDim.x and _x <= self.sliderBarDim.x+self.sliderBarDim.w then
        _x = _x - self.sliderBarDim.x;
        local xP = _x/(self.sliderBarDim.w);
        local newVal = self.minValue + ((self.maxValue-self.minValue)*xP);
        self.dragInside = true;
        self.dragClickX = _x;
        self:setCurrentValue(newVal);
    end
end

function ISSliderPanel:onMouseMove(_x, _y)
    if not self:getIsVisible() then
        return;
    end
    _x = self:getMouseX();

    if self.doButtons then
        if (_x > self.btnLeftDim.x and _x < self.btnLeftDim.x + self.btnLeftDim.w) or (_x > self.btnRightDim.x and _x < self.btnRightDim.x + self.btnRightDim.w) then
            self:activateToolTip();
        else
            self:deactivateToolTip();
        end
    end

    if self.dragInside then
        if _x >= self.sliderBarDim.x or _x <= self.sliderBarDim.x+self.sliderBarDim.w then
            _x = _x - self.sliderBarDim.x;
            local xP = _x/(self.sliderBarDim.w);
            local newVal = self.minValue + ((self.maxValue-self.minValue)*xP);
            self.dragInside = true;
            self.dragClickX = _x;
            self:setCurrentValue(newVal);
        end
    end
end

function ISSliderPanel:onMouseMoveOutside(_x, _y)
    if not self:getIsVisible() then return; end
    self:onMouseMove(_x, _y);
    if self.doButtons then self:deactivateToolTip(); end
end

function ISSliderPanel:onMouseUpOutside(x, y)
    self.dragInside = false;
    self.leftPressed = false;
    self.rightPressed = false;
    if not self:getIsVisible() then return; end
end

function ISSliderPanel:onMouseUp(x, y)
    self.dragInside = false;
    self.leftPressed = false;
    self.rightPressed = false;
    if not self:getIsVisible() then return; end
end

function ISSliderPanel:update()
    ISPanel.update(self);
end

function ISSliderPanel:prerender()
    ISPanel.prerender(self);
end

function ISSliderPanel:render()
    ISPanel.render(self);

    if self.doButtons then
        local c = self.buttonColor;
        if self.leftPressed then c = self.buttonMouseOverColor; end
        self:drawTextureScaled(self.texBtnLeft, self.btnLeftDim.x, self.btnLeftDim.y, self.btnLeftDim.w, self.btnLeftDim.h, c.a, c.r, c.g, c.b);
        if self.rightPressed then c = self.buttonMouseOverColor; else c = self.buttonColor; end
        self:drawTextureScaled(self.texBtnRight, self.btnRightDim.x, self.btnRightDim.y, self.btnRightDim.w, self.btnRightDim.h, c.a, c.r, c.g, c.b);
    end

    self:drawRect(self.sliderBarDim.x, self.sliderBarDim.y, self.sliderBarDim.w, self.sliderBarDim.h, self.sliderBarColor.a, self.sliderBarColor.r, self.sliderBarColor.g, self.sliderBarColor.b);
    self:drawRectBorder(self.sliderBarDim.x, self.sliderBarDim.y-1, self.sliderBarDim.w, self.sliderBarDim.h+2, self.sliderBarBorderColor.a, self.sliderBarBorderColor.r, self.sliderBarBorderColor.g, self.sliderBarBorderColor.b);

    local c = self.sliderColor;
    if self.dragInside then c = self.sliderMouseOverColor; end

    local valP = (self.currentValue-self.minValue)/(self.maxValue-self.minValue);
    local posx = self.sliderBarDim.x+(self.sliderBarDim.w*valP);
    self:drawRect(posx-self.sliderDim.x, self.sliderDim.y, self.sliderDim.w, self.sliderDim.h, c.a, c.r, c.g, c.b);
    self:drawRectBorder(posx-self.sliderDim.x-1, self.sliderDim.y, self.sliderDim.w+2, self.sliderDim.h, self.sliderBorderColor.a, self.sliderBorderColor.r, self.sliderBorderColor.g, self.sliderBorderColor.b);
end

function ISSliderPanel:doOnValueChange( _newvalue )
    if self.target and self.onValueChange then
        self.onValueChange(self.target, _newvalue, self);
    end
end

function ISSliderPanel:setValues( _min, _max, _step, _shift, _ignoreCurVal ) --set all slider values at once
    self.minValue = _min;
    self.maxValue = _max;
    self.stepValue = _step;
    self.shiftValue = _shift;
    if not _ignoreCurVal then
        self:setCurrentValue(self.currentValue);
    end
end

function ISSliderPanel:setDoButtons( _b )
    self.doButtons = _b;
    self:paginate();
end

function ISSliderPanel:setCurrentValue( _v, _ignoreOnChange )
    local stepmod = 1/self.stepValue;
    _v = self:round(_v*stepmod,0) / stepmod;
    if _v < self.minValue then _v = self.minValue; end
    if _v > self.maxValue then _v = self.maxValue; end
    self.currentValue = _v;
    if not _ignoreOnChange then
        self:doOnValueChange( _v )
    end
end

function ISSliderPanel:getCurrentValue()
    return self.currentValue;
end

function ISSliderPanel:activateToolTip()
    if self.doToolTip and self.stepValue~= self.shiftValue then
        if self.toolTip ~= nil then
            self.toolTip:setVisible(true);
            self.toolTip:addToUIManager();
            self.toolTip:bringToTop()
        else
            self.toolTip = ISToolTip:new(item);
            self.toolTip:initialise();
            self.toolTip:addToUIManager();
            self.toolTip:setOwner(self);
            self.toolTip.description = self.toolTipText; --"Drag a battery in here, or rightclick to remove it.";
            self.toolTip:doLayout();
        end
    end
end
function ISSliderPanel:deactivateToolTip()
    if self.toolTip then
        self.toolTip:removeFromUIManager();
        self.toolTip:setVisible(false);
        self.toolTip = nil;
    end
end


function ISSliderPanel:new (x, y, width, height, target, onValueChange, customPaginate)
    local o = ISPanel:new(x, y, width, height);
    setmetatable(o, self)
    self.__index = self
    o.x = x;
    o.y = y;
    o.background = false;
    o.backgroundColor = {r=0, g=0, b=0, a=0.0};
    o.borderColor = {r=0.4, g=0.4, b=0.4, a=1};
    o.width = width;
    o.height = height;
    o.anchorLeft = true;
    o.anchorRight = false;
    o.anchorTop = true;
    o.anchorBottom = false;
    o.target = target;
    o.onValueChange = onValueChange;
    o.customPaginate = customPaginate;
    o.texBtnLeft = getTexture("media/ui/ArrowLeft.png");
    o.texBtnRight = getTexture("media/ui/ArrowRight.png");

    o.currentValue = 50;
    o.minValue = 0;
    o.maxValue = 100;
    o.stepValue = 1;
    o.shiftValue = 10;
    o.doButtons = true;

    o.buttonColor = {r=0.6, g=0.6, b=0.6, a=1.0};
    o.buttonMouseOverColor = {r=1.0, g=1.0, b=1.0, a=1.0};

    o.sliderColor = {r=0.6, g=0.6, b=0.6, a=1.0};
    o.sliderMouseOverColor = {r=1.0, g=1.0, b=1.0, a=1.0};
    o.sliderBorderColor = {r=1.0, g=1.0, b=1.0, a=1};

    o.sliderBarColor = {r=0.2, g=0.2, b=0.2, a=1.0};
    o.sliderBarBorderColor = {r=0.4, g=0.4, b=0.4, a=1};

    o.dragInside = false;

    o.doToolTip = true;
    o.toolTipText = getText("UI_Radio_IncreaseStepSize")
    o.isSliderPanel = true;
    --o.fontheight = getTextManager():MeasureStringY(UIFont.Small, "AbdfghijklpqtyZ")+2;
    return o
end


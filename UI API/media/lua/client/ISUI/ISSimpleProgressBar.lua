require "ISUI/ISUIElement"

ISSimpleProgressBar = ISUIElement:derive("ISSimpleProgressBar");

function ISSimpleProgressBar:render()
    if      self.value < self.min then self.value = self.min
    elseif  self.value > self.max then self.value = self.max
    end

    local pct = (self.value - self.min) / (self.max - self.min);
    self:drawRect(self.barMarginW, self.barMarginH, (self.pxlW-self.barMarginW*2) * pct, self.pxlH - self.barMarginH*2, self.a, self.r, self.g, self.b);

    if self.border then
        self:drawRectBorder(0, 0, self:getWidth(), self:getHeight(), 0.5, 1, 1, 1);
    end
end

function ISSimpleProgressBar:setPositionAndSize()
    self.pxlW = self.parentUI.elemW[self.line][self.column];
    self.pxlX = self.parentUI.elemX[self.line][self.column];
    self.pxlH = self.parentUI.elemH[self.line];

    self:setX(self.pxlX);
    self:setY(self.pxlY);
    self:setWidth(self.pxlW);
    self:setHeight(self.pxlH);
end

function ISSimpleProgressBar:new(parentUI, value, min, max)
    local o = {};
    o = ISUIElement:new(0, 0, 1, 1);
    setmetatable(o, self);
    self.__index = self;

    -- Parent and position
    o.parentUI = parentUI;
    o.line = parentUI.lineAct;
    o.column = parentUI.columnAct;
    o.pxlY = parentUI.yAct;

    -- Color
    o.a = 1;
    o.r = 1;
    o.g = 1;
    o.b = 1;
    o.backgroundColor = {r=0, g=0, b=0, a=1};

    o.anchorLeft = true;
	o.anchorRight = false;
	o.anchorTop = true;
	o.anchorBottom = false;

    -- For this element
    o.value = value;
    o.min = min;
    o.max = max;
    o.barMarginW = 0;
    o.barMarginH = 0;

    return o;
end

-- Commun function
function ISSimpleProgressBar:setBorder(v)
    self.border = v;
end

function ISSimpleProgressBar:setWidthPercent(w)
    self.isWidthForce = true;
    self.pxlW = w * getCore():getScreenWidth();
end

function ISSimpleProgressBar:setWidthPixel(w)
    self.isWidthForce = true;
    self.pxlW = w;
end

-- For element

function ISSimpleProgressBar:setColor(a, r, g, b)
    self.a = a;
    self.r = r;
    self.g = g;
    self.b = b;
end

function ISSimpleProgressBar:setValue(v)
    self.value = v;
end

function ISSimpleProgressBar:setMinMax(min, max)
    self.min = min;
    self.max = max;
end

function ISSimpleProgressBar:setMarginPercent(pctW, pctH)
    self.barMarginW = pctW * getCore():getScreenWidth();
    self.barMarginH = pctH * getCore():getScreenHeight();
end

function ISSimpleProgressBar:setMarginPixel(pxlW, pxlH)
    self.barMarginW = pxlW;
    self.barMarginH = pxlH;
end
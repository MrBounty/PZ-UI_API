require "ISUI/ISUIElement"

ISSimpleEmpty = ISUIElement:derive("ISSimpleEmpty");

function ISSimpleEmpty:render()
    self:drawRect(0, 0, self.width, self.height, self.backgroundColor.a, self.backgroundColor.r, self.backgroundColor.g, self.backgroundColor.b);

    if self.border then
        self:drawRectBorder(0, 0, self:getWidth(), self:getHeight(), 0.5, 1, 1, 1);
    end
end

function ISSimpleEmpty:setPositionAndSize()
    self.pxlW = self.parentUI.elemW[self.line][self.column];
    self.pxlX = self.parentUI.elemX[self.line][self.column];
    self.pxlH = self.parentUI.elemH[self.line];

    self:setX(self.pxlX);
    self:setY(self.pxlY);
    self:setWidth(self.pxlW);
    self:setHeight(self.pxlH);
end

function ISSimpleEmpty:new(parentUI)
    local o = {};
    o = ISUIElement:new(0, 0, 1, 1);
    setmetatable(o, self);
    self.__index = self;

    -- Parent and position
    o.parentUI = parentUI;
    o.line = parentUI.lineAct;
    o.column = parentUI.columnAct;
    o.pxlY = parentUI.yAct;
    o.isEmptyElement = true;

    o.backgroundColor = {r=0, g=0, b=0, a=1};

    return o;
end

-- Commun function
function ISSimpleEmpty:setBorder(v)
    self.border = v;
end

function ISSimpleEmpty:setWidthPercent(w)
    self.isWidthForce = true;
    self.pxlW = w * getCore():getScreenWidth();
end

function ISSimpleEmpty:setWidthPixel(w)
    self.isWidthForce = true;
    self.pxlW = w;
end
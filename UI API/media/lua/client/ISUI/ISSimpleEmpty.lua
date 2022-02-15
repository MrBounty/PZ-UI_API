require "ISUI/ISUIElement"

ISSimpleEmpty = ISUIElement:derive("ISSimpleEmpty");

function ISSimpleEmpty:render()
    self:drawRect(0, 0, self.width, self.height, self.backgroundColor.a, self.backgroundColor.r, self.backgroundColor.g, self.backgroundColor.b);

    if self.border then
        self:drawRectBorder(0, 0, self:getWidth(), self:getHeight(), 0.5, 1, 1, 1);
    end
end

function ISSimpleEmpty:setPositionAndSize()
    local nbElement = self.parentUI.lineColumnCount[self.line]
    self.maxW = self.parentUI.pxlW / nbElement;
    self.pxlX = self.maxW * (self.column - 1);
    self.pxlH = self.parentUI.lineH[self.line];

    self:setX(self.pxlX);
    self:setY(self.pxlY);
    self:setWidth(self.maxW);
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

    return o;
end

-- Commun function
function ISSimpleEmpty:addBorder()
    self.border = true;
end

function ISSimpleEmpty:removeBorder()
    self.border = false;
end
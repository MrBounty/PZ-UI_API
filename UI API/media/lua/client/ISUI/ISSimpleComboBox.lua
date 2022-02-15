require "ISUI/ISComboBox"

ISSimpleComboBox = ISComboBox:derive("ISSimpleComboBox");

function ISSimpleComboBox:setPositionAndSize()
    local nbElement = self.parentUI.lineColumnCount[self.line]
    self.maxW = self.parentUI.pxlW / nbElement;
    self.pxlX = self.maxW * (self.column - 1);

    self:setX(self.pxlX);
    self:setY(self.pxlY);
    self:setWidth(self.maxW);
    self:setHeight(self.parentUI.lineH[self.line])

    for index, value in ipairs(self.items) do
        self:addItem(value);
    end
end

function ISSimpleComboBox:render()
    ISComboBox.render(self)
    if self.border then
        self:drawRectBorder(0, 0, self:getWidth(), self:getHeight(), 0.5, 1, 1, 1);
    end
end

function ISSimpleComboBox:new(parentUI, items)
    local o = {};
    o = ISComboBox:new(text, 0, 0, 1, 1);
    setmetatable(o, self);
    self.__index = self;

    o.parentUI = parentUI;
    o.line = line;
    o.column = column;

    -- Parent and position
    o.parentUI = parentUI;
    o.line = parentUI.lineAct;
    o.column = parentUI.columnAct;
    o.pxlY = parentUI.yAct;
    o.items = items;

    return o;
end

-- Commun function

function ISSimpleComboBox:addBorder()
    self.border = true;
end

function ISSimpleComboBox:removeBorder()
    self.border = false;
end

-- Simple element function
function ISSimpleComboBox:getValue()
    return self:getSelectedText();
end

function ISSimpleComboBox:setitems(v)
    self:clear();
    self.items = v;
    for index, value in ipairs(self.items) do
        self:addItem(value);
    end
end
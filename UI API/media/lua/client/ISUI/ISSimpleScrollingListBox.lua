require "ISUI/ISScrollingListBox"

ISSimpleScrollingListBox = ISScrollingListBox:derive("ISSimpleScrollingListBox");

function ISSimpleScrollingListBox:setPositionAndSize()
    self.pxlW = self.parentUI.elemW[self.line][self.column];
    self.pxlX = self.parentUI.elemX[self.line][self.column];
    self.pxlH = self.parentUI.elemH[self.line];

    self:setX(self.pxlX);
    self:setY(self.pxlY);
    self:setWidth(self.pxlW);
    self:setHeight(self.pxlH);

    for index, value in ipairs(self.simpleItems) do
        self:addItem(value);
    end
end

function ISSimpleScrollingListBox:render()
    ISComboBox.render(self)
    if self.border then
        self:drawRectBorder(0, 0, self:getWidth(), self:getHeight(), 0.5, 1, 1, 1);
    end
end

function ISSimpleScrollingListBox:new(parentUI, simpleItems)
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
    o.simpleItems = simpleItems;

    return o;
end

-- Commun function

function ISSimpleScrollingListBox:addBorder()
    self.border = true;
end

function ISSimpleScrollingListBox:removeBorder()
    self.border = false;
end

-- Simple element function
function ISSimpleScrollingListBox:getValue()
    return self:getSelectedText();
end

function ISSimpleScrollingListBox:setitems(v)
    self:clear();
    self.simpleItems = v;
    for index, value in ipairs(self.simpleItems) do
        self:addItem(value);
    end
end

function ISSimpleScrollingListBox:putBack()
    self:setVisible(true);
end

function ISSimpleScrollingListBox:remove()
    self:setVisible(false);
end

function ISSimpleScrollingListBox:toggle()
    if self:getIsVisible() then
        self:setVisible(true);
    else
        self:setVisible(false);
    end;
end

function ISSimpleScrollingListBox:setWidthPercent(w)
    self.isWidthForce = true;
    self.pxlW = w * getCore():getScreenWidth();
end

function ISSimpleScrollingListBox:setWidthPixel(w)
    self.isWidthForce = true;
    self.pxlW = w;
end
require "ISUI/ISComboBox"

ISSimpleComboBox = ISComboBox:derive("ISSimpleComboBox");

function ISSimpleComboBox:setPositionAndSize()
    self.pxlW = self.parentUI.elemW[self.line][self.column];
    self.pxlX = self.parentUI.elemX[self.line][self.column];
    self.pxlH = self.parentUI.elemH[self.line];

    self:setX(self.pxlX);
    self:setY(self.pxlY);
    self:setWidth(self.pxlW);
    self:setHeight(self.pxlH);
    self.baseHeight = self.pxlH;

    if self.simpleItems[1] ~= nil then
        for name, item in ipairs(self.simpleItems) do
            self:addOption(item);
        end
    else
        for name, item in pairs(self.simpleItems) do
            self:addOptionWithData(name, item);
        end
    end
end

function ISSimpleComboBox:render()
    ISComboBox.render(self)
    if self.border then
        self:drawRectBorder(0, 0, self:getWidth(), self:getHeight(), 0.5, 1, 1, 1);
    end
end

function ISSimpleComboBox:new(parentUI, simpleItems)
    local o = {};
    o = ISComboBox:new(0, 0, 1, 1);
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

function ISSimpleComboBox:setBorder(v)
    self.border = v;
end

-- Simple element function
function ISSimpleComboBox:getValue()
    return self:getSelectedText();
end

function ISSimpleComboBox:setitems(v)
    self:clear();
    self.simpleItems = v;
    for index, value in ipairs(self.simpleItems) do
        self:addItem(value);
    end
end

function ISSimpleComboBox:setWidthPercent(w)
    self.isWidthForce = true;
    self.pxlW = w * getCore():getScreenWidth();
end

function ISSimpleComboBox:setWidthPixel(w)
    self.isWidthForce = true;
    self.pxlW = w;
end
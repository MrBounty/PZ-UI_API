require "ISUI/ISTextEntryBox"

ISSimpleEntry = ISTextEntryBox:derive("ISSimpleEntry");

function ISSimpleEntry:setText(text)
    self:setTitle(text);
end

function ISSimpleEntry:setPositionAndSize()
    local nbElement = self.parentUI.lineColumnCount[self.line]
    self.maxW = self.parentUI.pxlW / nbElement;
    self.pxlX = self.maxW * (self.column - 1);

    self:setX(self.pxlX);
    self:setY(self.pxlY);
    self:setWidth(self.maxW);
    self:setHeight(self.parentUI.lineH[self.line])
end

function ISSimpleEntry:render()
    ISTextEntryBox.render(self)
    if self.border then
        self:drawRectBorder(0, 0, self:getWidth(), self:getHeight(), 0.5, 1, 1, 1);
    end
end

function ISSimpleEntry:new(parentUI, text, isNumber)
    local o = {};
    o = ISTextEntryBox:new(text, 0, 0, 1, 1);
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
    o.textOriginal = text;
    o.isNumber = isNumber;

    return o;
end

-- Commun function

function ISSimpleEntry:addBorder()
    self.border = true;
end

function ISSimpleEntry:removeBorder()
    self.border = false;
end

-- Simple element function
function ISSimpleEntry:getValue()
    if self.isNumber then
        return tonumber(self:getInternalText());
    else
        return self:getInternalText();
    end
end

function ISSimpleEntry:setValue(v)
    if self.isNumber then
        self:setText(tostring(v));
    else
        self:setText(v);
    end
end
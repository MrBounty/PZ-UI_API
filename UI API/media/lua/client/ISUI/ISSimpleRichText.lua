require "ISUI/ISRichTextBox"

ISSimpleRichText = ISRichTextBox:derive("ISSimpleRichText");

function ISSimpleRichText:setText(text)
    self:setTitle(text);
end

function ISSimpleRichText:setPositionAndSize()
    local nbElement = self.parentUI.lineColumnCount[self.line]
    self.maxW = self.parentUI.pxlW / nbElement;
    self.pxlX = self.maxW * (self.column - 1);
    self:setX(self.pxlX);
    self:setY(self.pxlY);
    self:setWidth(self.maxW);
    self:setHeight(self.parentUI.lineH[self.line])
    self:setTitle(self.text);
    self:setOnClick(self.func);
end

function ISSimpleRichText:new(parentUI, text)
    local o = {};
    o = ISRichTextPanel:new(0, 0, 1, 1, text, false);
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
    o.func = func;
    o.text = text;
    return o;
end

-- Commun function
function ISSimpleRichText:addBorder()
    self.border = true;
end
function ISSimpleRichText:removeBorder()
    self.border = false;
end
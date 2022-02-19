require "ISUI/ISTextEntryBox"

ISSimpleEntry = ISTextEntryBox:derive("ISSimpleEntry");

function ISSimpleEntry:setText(text)
    self:setTitle(text);
end

function ISSimpleEntry:setPositionAndSize()
    self.pxlW = self.parentUI.elemW[self.line][self.column];
    self.pxlX = self.parentUI.elemX[self.line][self.column];
    self.pxlH = self.parentUI.elemH[self.line];

    self:setX(self.pxlX);
    self:setY(self.pxlY);
    self:setWidth(self.pxlW);
    self:setHeight(self.pxlH);
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

function ISSimpleEntry:setBorder(v)
    self.border = v;
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

function ISSimpleEntry:setWidthPercent(w)
    self.isWidthForce = true;
    self.pxlW = w * getCore():getScreenWidth();
end

function ISSimpleEntry:setWidthPixel(w)
    self.isWidthForce = true;
    self.pxlW = w;
end
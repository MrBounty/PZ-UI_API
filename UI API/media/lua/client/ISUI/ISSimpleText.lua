require "ISUI/ISUIElement"

ISSimpleText = ISUIElement:derive("ISSimpleText");

function ISSimpleText:update()
    ISButton:update()
end

function ISSimpleText:setText(txt)
    self.textToDisplay = txt;
end

function ISSimpleText:setFont(font)
    self.font = UIFont[font]
end

function ISSimpleText:setColor(a, r, g, b)
    self.a = a;
    self.r = r;
    self.g = g;
    self.b = b;
end

function ISSimpleText:setPosition(position)
    self.position = position
end

function ISSimpleText:render(txt)
    ISUIElement.render(self);

    if position == "Left" then
        self:drawText(self.textToDisplay, 0, 0, self.r, self.g, self.b, self.a, self.font);
    elseif position == "Right" then
        self:drawTextRight(self.textToDisplay, 0, 0, self.r, self.g, self.b, self.a, self.font);
    elseif position == "Center" then
        self:drawTextCentre(self.textToDisplay, 0, 0, self.r, self.g, self.b, self.a, self.font);
    end
end

function ISSimpleText:setPositionAndSize()
    local nbElement = self.parentUI.lineColumnCount[self.line]
    self.maxW = (self.parentUI.pxlW - self.parentUI.dx * (nbElement+1)) / nbElement
    self.pxlX = self.maxW * (self.column - 1);

    self:setX(self.pxlX);
    self:setY(self.pxlY);
    self.setWidth(self.maxW);
    self.setHeight(self.parentUI.lineH[self.line])
end

function ISSimpleText:new(parentUI, txt, font)
    local o = {};
    o = ISButton:new(0, 0, 1, 1);
    setmetatable(o, self);
    self.__index = self;

    o.parentUI = parentUI;
    o.line = parentUI.lineAct;
    o.column = parentUI.columnAct;
    o.pxlY = parentUI.yAct;
    o.position = "Left";

    o.a = 1;
    o.r = 1;
    o.g = 1;
    o.b = 1;

    o.textToDisplay = txt;
    o.font = UIFont[font] 

    return o;
end
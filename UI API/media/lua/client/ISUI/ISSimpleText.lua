require "ISUI/ISUIElement"

ISSimpleText = ISUIElement:derive("ISSimpleText");

function ISSimpleText:setText(txt)
    self.textOriginal = txt;
    self.textToDisplay, self.textW = cutTextToLong(self.textOriginal, self:getWidth(), self.font);
end

function ISSimpleText:setFont(font)
    self.font = UIFont[font]
    self.textToDisplay, self.textW = cutTextToLong(self.textOriginal, self:getWidth(), self.font);
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

function ISSimpleText:render()
    self:drawRect(0, 0, self.width, self.height, self.backgroundColor.a, self.backgroundColor.r, self.backgroundColor.g, self.backgroundColor.b);

    if self.position == "Left" then
        self:drawText(self.textToDisplay, 0, 0, self.r, self.g, self.b, self.a, self.font);
    elseif self.position == "Right" then
        self:drawTextRight(self.textToDisplay, self:getWidth()-self.textW, 0, self.r, self.g, self.b, self.a, self.font);
    elseif self.position == "Center" then
        self:drawTextCentre(self.textToDisplay, self:getWidth()/2, 0, self.r, self.g, self.b, self.a, self.font);
    else
        self:drawText(self.textToDisplay, 0, 0, self.r, self.g, self.b, self.a, self.font);
    end

    if self.border then
        self:drawRectBorder(0, 0, self:getWidth(), self:getHeight(), 0.5, 1, 1, 1);
    end
end

function ISSimpleText:setPositionAndSize()
    local nbElement = self.parentUI.lineColumnCount[self.line]
    self.maxW = self.parentUI.pxlW / nbElement;
    self.pxlX = self.maxW * (self.column - 1);
    self.pxlH = self.parentUI.lineH[self.line];
    self.textH = getTextManager():getFontHeight(self.font);

    self:setX(self.pxlX);
    self:setY(self.pxlY - (self.pxlH - self.textH)/2);
    self:setWidth(self.maxW);
    self:setHeight(self.pxlH)

    self.textToDisplay, self.textW = cutTextToLong(self.textOriginal, self:getWidth(), self.font);
end

function ISSimpleText:new(parentUI, text, font, position)
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
    if text then
        o.textOriginal = text;
    else
        o.textOriginal = "";
    end

    if font then
        o.font = UIFont[font];
    else
        o.font = UIFont.Small;
    end

    if position then
        o.position = position;
    else
        o.position = "Left";
    end

    return o;
end

-- Commun function
function ISSimpleText:addBorder()
    self.border = true;
end

function ISSimpleText:removeBorder()
    self.border = false;
end
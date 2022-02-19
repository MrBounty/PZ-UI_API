require "ISUI/ISUIElement"

ISSimpleText = ISUIElement:derive("ISSimpleText");

function ISSimpleText:render()
    self:drawRect(0, 0, self.width, self.height, self.backgroundColor.a, self.backgroundColor.r, self.backgroundColor.g, self.backgroundColor.b);
    local y = (self.pxlH - self.textH)/2;

    if self.position == "Left" then
        self:drawText(self.textToDisplay, 0, y, self.r, self.g, self.b, self.a, self.font);
    elseif self.position == "Right" then
        self:drawTextRight(self.textToDisplay, self:getWidth()-self.textW, y, self.r, self.g, self.b, self.a, self.font);
    elseif self.position == "Center" then
        self:drawTextCentre(self.textToDisplay, self:getWidth()/2, y, self.r, self.g, self.b, self.a, self.font);
    else
        self:drawText(self.textToDisplay, 0, y, self.r, self.g, self.b, self.a, self.font);
    end

    if self.border then
        self:drawRectBorder(0, 0, self:getWidth(), self:getHeight(), 0.5, 1, 1, 1);
    end
end

function ISSimpleText:setPositionAndSize()
    self.pxlW = self.parentUI.elemW[self.line][self.column];
    self.pxlX = self.parentUI.elemX[self.line][self.column];
    self.pxlH = self.parentUI.elemH[self.line];
    self.textH = getTextManager():getFontHeight(self.font);

    self:setX(self.pxlX);
    self:setY(self.pxlY);
    self:setWidth(self.pxlW);
    self:setHeight(self.pxlH);

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
function ISSimpleText:setBorder(v)
    self.border = v;
end

function ISSimpleText:setWidthPercent(w)
    self.isWidthForce = true;
    self.pxlW = w * getCore():getScreenWidth();
end

function ISSimpleText:setWidthPixel(w)
    self.isWidthForce = true;
    self.pxlW = w;
end

-- For element
function ISSimpleText:setText(txt)
    self.textOriginal = txt;
    self.textToDisplay, self.textW = cutTextToLong(self.textOriginal, self:getWidth(), self.font);
end

function ISSimpleText:setPosition(position)
    self.position = position
end

function ISSimpleText:setColor(a, r, g, b)
    self.a = a;
    self.r = r;
    self.g = g;
    self.b = b;
end
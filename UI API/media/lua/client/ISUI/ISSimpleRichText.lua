require "ISUI/ISUIElement"

ISSimpleRichText = ISUIElement:derive("ISSimpleRichText");

function ISSimpleRichText:setPositionAndSize()
    self.pxlW = self.parentUI.elemW[self.line][self.column];
    self.pxlX = self.parentUI.elemX[self.line][self.column];
    self.pxlH = self.parentUI.elemH[self.line];

    self.richText:setWidth(self.pxlW);
    self.richText:setText(self.text);
    self.richText:initialise();
    self.richText:paginate();

    self:setX(self.pxlX);
    self:setY(self.pxlY);
    self:setWidth(self.pxlW);
    self:setHeight(self.pxlH);
end

function ISSimpleRichText:initialise()
    ISUIElement.initialise(self);
    self.richText = ISRichTextLayout:new(self.width);
    self.richText.marginLeft = 0;
    self.richText.marginTop = 0;
    self.richText.marginRight = 0;
    self.richText.marginBottom = 0;
end

function ISSimpleRichText:prerender()
    self.richText:render(0, self:getYScroll(), self);
    if self.border then
        self:drawRectBorder(0, 0, self:getWidth(), self:getHeight(), 0.5, 1, 1, 1);
    end
end

function ISSimpleRichText:onMouseWheel(del)
	self:setYScroll(self:getYScroll() - (del*18));
    return true;
end

function ISSimpleRichText:new(parentUI, text)
    local o = {};
    o = ISUIElement:new(0, 0, 1, 1);
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
    o.text = text;
    return o;
end

-- Commun function
function ISSimpleRichText:setBorder(v)
    self.border = v;
end

function ISSimpleRichText:setText(text)
    self.richText:setText(text);
    self.richText:paginate();
end

function ISSimpleRichText:setWidthPercent(w)
    self.isWidthForce = true;
    self.pxlW = w * getCore():getScreenWidth();
end

function ISSimpleRichText:setWidthPixel(w)
    self.isWidthForce = true;
    self.pxlW = w;
end
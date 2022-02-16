require "ISUI/ISUIElement"

ISSimpleTickBox = ISUIElement:derive("ISSimpleTickBox");

-- Vanilla element function
function ISSimpleTickBox:setPositionAndSize()
    self.pxlW = self.parentUI.elemW[self.line][self.column];
    self.pxlX = self.parentUI.elemX[self.line][self.column];
    self.pxlH = self.parentUI.elemH[self.line];

    self:setX(self.pxlX);
    self:setY(self.pxlY);
    self:setWidth(self.pxlW);
    self:setHeight(self.pxlH);

    self.tickBox:setX(self.pxlX + (self.tickSize+self.pxlW)/2);
    self.tickBox:setY(self.pxlY + (self.tickSize+pxlH)/2);
    self.tickBox:setWidth(self.tickSize);
    self.tickBox:setHeight(self.tickSize);
end

function ISSimpleTickBox:createChildren()
    self.tickBox = ISTickBox(0, 0, 1, 1);
    self.tickBox:initialize();
    self.tickBox:instantiate();
    self.tickBox:addOption("");
    self.addChild(self.tickBox);
end

function ISSimpleTickBox:onMouseWheel(del)
	self:setYScroll(self:getYScroll() - (del*18));
    return true;
end

function ISSimpleTickBox:render()
    ISUIElement.render(self)
    if self.border then
        self:drawRectBorder(0, 0, self:getWidth(), self:getHeight(), 0.5, 1, 1, 1);
    end
end

-- New
function ISSimpleTickBox:new(parentUI)
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
    return o;
end

-- Commun function
function ISSimpleTickBox:addBorder()
    self.border = true;
end

function ISSimpleTickBox:removeBorder()
    self.border = false;
end

-- Simple element function
function ISSimpleTickBox:getValue()
    return self.tickBox.selected[1];
end

function ISSimpleTickBox:setValue(v)
    self.tickBox.selected[1] = v;
end

function ISSimpleTickBox:putBack()
    self:setVisible(true);
end

function ISSimpleTickBox:remove()
    self:setVisible(false);
end

function ISSimpleTickBox:toggle()
    if self:getIsVisible() then
        self:setVisible(true);
    else
        self:setVisible(false);
    end;
end

function ISSimpleTickBox:setWidthPercent(w)
    self.isWidthForce = true;
    self.pxlW = w * getCore():getScreenWidth();
end

function ISSimpleTickBox:setWidthPixel(w)
    self.isWidthForce = true;
    self.pxlW = w;
end
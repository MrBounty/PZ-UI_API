require "ISUI/ISImage"

ISSimpleImage = ISImage:derive("ISSimpleImage");

function ISSimpleImage:setPositionAndSize()
    self.pxlW = self.parentUI.elemW[self.line][self.column];
    self.pxlX = self.parentUI.elemX[self.line][self.column];
    self.pxlH = self.parentUI.elemH[self.line];

    self:setX(self.pxlX);
    self:setY(self.pxlY);
    self:setWidth(self.pxlW);
    self:setHeight(self.pxlH);
end

function ISSimpleImage:render()
    ISSimpleImage.render(self)
    if self.border then
        self:drawRectBorder(0, 0, self:getWidth(), self:getHeight(), 0.5, 1, 1, 1);
    end
end

function ISSimpleImage:new(parentUI, path)
    local o = {};
    if not getTexture(path) then
        print("UI API - ERROR : Texture at the path "..path.." not found for image. Changed to default texture.")
        path = "ui/emotes/no.png";
    end
    o = ISImage:new(0, 0, 1, 1, getTexture(path));
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

function ISSimpleImage:addBorder()
    self.border = true;
end

function ISSimpleImage:removeBorder()
    self.border = false;
end

function ISSimpleImage:putBack()
    self:setVisible(true);
end

function ISSimpleImage:remove()
    self:setVisible(false);
end

function ISSimpleImage:toggle()
    if self:getIsVisible() then
        self:setVisible(true);
    else
        self:setVisible(false);
    end;
end

function ISSimpleImage:setWidthPercent(w)
    self.isWidthForce = true;
    self.pxlW = w * getCore():getScreenWidth();
end

function ISSimpleImage:setWidthPixel(w)
    self.isWidthForce = true;
    self.pxlW = w;
end
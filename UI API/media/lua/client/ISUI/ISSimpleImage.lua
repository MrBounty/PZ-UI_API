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
    self.scaledWidth = self.pxlW;
    self.scaledHeight = self.pxlH;
end

function ISSimpleImage:render()
    if self.border then
        self:drawRectBorder(0, 0, self:getWidth(), self:getHeight(), 0.5, 1, 1, 1);
    end
end

function ISSimpleImage:new(parentUI, path)
    local o = {};
    if not getTexture(path) then
        print("UI API - ERROR : Texture at the path ".. path .." not found for image. Changed to default texture.")
        path = "ui/emotes/no.png";
    end
    o = ISImage:new(0, 0, 1, 1, getTexture(path));
    setmetatable(o, self);
    self.__index = self;

    -- Parent and position
    o.parentUI = parentUI;
    o.line = parentUI.lineAct;
    o.column = parentUI.columnAct;
    o.pxlY = parentUI.yAct;
    o.textOriginal = text;
    o.path = path;
    o.isImage = true;

    o.origW = o.texture:getWidthOrig();
    o.origH = o.texture:getWidthOrig();
    o.ratio = o.origH / o.origW;
    o.border = false;

    return o;
end

-- Commun function

function ISSimpleImage:setBorder(v)
    self.border = v;
end

function ISSimpleImage:setWidthPercent(w)
    self.isWidthForce = true;
    self.pxlW = w * getCore():getScreenWidth();
end

function ISSimpleImage:setWidthPixel(w)
    self.isWidthForce = true;
    self.pxlW = w;
end

function ISSimpleImage:setPath(path)
    self.path = path;
    if not getTexture(path) then
        print("UI API - ERROR : Texture at the path ".. path .." not found for image. Changed to default texture.")
        self.path = "ui/emotes/no.png";
    end
    self.texture = getTexture(path);
end
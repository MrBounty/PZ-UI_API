require "ISUI/ISButton"

ISSimpleImageButton = ISButton:derive("ISSimpleImageButton");

function ISSimpleImageButton:setText(text)
    self:setTitle(text);
end

function ISSimpleImageButton:onMouseUp(x, y)
    if not self:getIsVisible() then
        return;
    end
    local process = false;
    if self.pressed == true then
        process = true;
    end
    self.pressed = false;
     if self.onclick == nil then
        return;
    end
    if self.enable and (process or self.allowMouseUpProcessing) then
        getSoundManager():playUISound(self.sounds.activate)
        self.onclick(self, self.args);
    end
end

function ISSimpleImageButton:setPositionAndSize()
    self.pxlW = self.parentUI.elemW[self.line][self.column];
    self.pxlX = self.parentUI.elemX[self.line][self.column];
    self.pxlH = self.parentUI.elemH[self.line];

    self:setX(self.pxlX);
    self:setY(self.pxlY);
    self:setWidth(self.pxlW);
    self:setHeight(self.pxlH);
    self:setOnClick(self.func);
    self:setImage(self.texture);
end

function ISSimpleImageButton:render()
    ISButton.render(self)
    if self.border then
        self:drawRectBorder(0, 0, self:getWidth(), self:getHeight(), 0.5, 1, 1, 1);
    end
end

function ISSimpleImageButton:new(parentUI, path, func)
    local o = {};
    o = ISButton:new(0, 0, 1, 1, title);
    setmetatable(o, self);
    self.__index = self;

    o.parentUI = parentUI;
    o.line = line;
    o.column = column;
    if not getTexture(path) then
        print("UI API - ERROR : Texture at the path "..path.." not found for image button. Changed to default texture.")
        path = "ui/emotes/no.png";
    end
    o.path = path;
    o.texture = getTexture(path);

    -- Parent and position
    o.parentUI = parentUI;
    o.line = parentUI.lineAct;
    o.column = parentUI.columnAct;
    o.pxlY = parentUI.yAct;

    o.func = func;
    o.args = {};

    return o;
end

-- Commun function

function ISSimpleImageButton:addBorder()
    self.border = true;
end

function ISSimpleImageButton:removeBorder()
    self.border = false;
end

function ISSimpleImageButton:addArg(name, value)
    self.args[name] = value;
end

function ISSimpleImageButton:putBack()
    self:setVisible(true);
end

function ISSimpleImageButton:remove()
    self:setVisible(false);
end

function ISSimpleImageButton:toggle()
    if self:getIsVisible() then
        self:setVisible(true);
    else
        self:setVisible(false);
    end;
end

function ISSimpleImageButton:setWidthPercent(w)
    self.isWidthForce = true;
    self.pxlW = w * getCore():getScreenWidth();
end

function ISSimpleImageButton:setWidthPixel(w)
    self.isWidthForce = true;
    self.pxlW = w;
end
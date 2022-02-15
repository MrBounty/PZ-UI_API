require "ISUI/ISButton"

ISSimpleButton = ISButton:derive("ISSimpleButton");

function ISSimpleButton:setText(text)
    self:setTitle(text);
end

function ISSimpleButton:onMouseUp(x, y)

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

function ISSimpleButton:setPositionAndSize()
    local nbElement = self.parentUI.lineColumnCount[self.line]
    self.maxW = self.parentUI.pxlW / nbElement;
    self.pxlX = self.maxW * (self.column - 1);

    self:setX(self.pxlX);
    self:setY(self.pxlY);
    self:setWidth(self.maxW);
    self:setHeight(self.parentUI.lineH[self.line])
    self:setOnClick(self.func);
end

function ISSimpleButton:render()
    ISButton.render(self)
    if self.border then
        self:drawRectBorder(0, 0, self:getWidth(), self:getHeight(), 0.5, 1, 1, 1);
    end
end

function ISSimpleButton:new(parentUI, title, func)
    local o = {};
    o = ISButton:new(0, 0, 1, 1, title);
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
    o.args = {};

    return o;
end

-- Commun function

function ISSimpleButton:addBorder()
    self.border = true;
end

function ISSimpleButton:removeBorder()
    self.border = false;
end

function ISSimpleButton:addArg(name, value)
    self.args[name] = value;
end
require "ISUI/ISButton"

ISSimpleButton = ISButton:derive("ISSimpleButton");

function ISSimpleButton:update()
    ISButton:update()
end

function ISSimpleButton:setText(text)
    self:setTitle(text);
end

function ISSimpleButton:setPositionAndSize()
    local nbElement = self.parentUI.lineColumnCount[self.line]
    local x = 1
end

function ISSimpleButton:new(parentUI, line, column)
    local o = {};
    o = ISButton:new(0, 0, 1, 1);
    setmetatable(o, self);
    self.__index = self;

    o.parentUI = parentUI;
    o.line = line;
    o.column = column;

    return o;
end
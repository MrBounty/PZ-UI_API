--***********************************************************
--**                    THE INDIE STONE                    **
--***********************************************************

require "ISUI/ISUIElement"

---@class ISRadialProgressBar : ISUIElement
ISRadialProgressBar = ISUIElement:derive("ISRadialProgressBar")

function ISRadialProgressBar:instantiate()
    self.javaObject = RadialProgressBar.new(self, self.texture)
    self.javaObject:setX(self.x)
    self.javaObject:setY(self.y)
    self.javaObject:setWidth(self.width)
    self.javaObject:setHeight(self.height)
    self.javaObject:setAnchorLeft(self.anchorLeft)
    self.javaObject:setAnchorRight(self.anchorRight)
    self.javaObject:setAnchorTop(self.anchorTop)
    self.javaObject:setAnchorBottom(self.anchorBottom)
end

function ISRadialProgressBar:prerender()
    ISUIElement.prerender(self)
end

function ISRadialProgressBar:render()
    ISUIElement.render(self)
end

function ISRadialProgressBar:setValue(_val)
    if self.javaObject then
        self.javaObject:setValue(_val);
    end
end

function ISRadialProgressBar:getValue()
    if self.javaObject then
        return self.javaObject:getValue();
    end
end

function ISRadialProgressBar:setTexture(_tex)
    if self.javaObject then
        self.javaObject:setTexture(_tex);
    end
end

function ISRadialProgressBar:getTexture()
    if self.javaObject then
        return self.javaObject:getTexture();
    end
end

function ISRadialProgressBar:new(x, y, width, height, texturePath)
    local o = {};
    o = ISUIElement:new(x, y, width, height)
    setmetatable(o, self);
    self.__index = self
    o.texture = getTexture(texturePath);
    return o;
end

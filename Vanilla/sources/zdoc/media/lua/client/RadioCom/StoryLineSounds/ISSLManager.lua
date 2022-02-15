--***********************************************************
--**                    THE INDIE STONE                    **
--**				  Author: turbotutone				   **
--***********************************************************
require "ISUI/ISPanel"

---@class ISSLManager : ISPanel
ISSLManager = ISPanel:derive("ISSLManager");

function ISSLManager:initialise()
    ISPanel.initialise(self)
end

function ISSLManager:createChildren()
end

function ISSLManager:update()
    ISPanel.update(self);
end

function ISSLManager:prerender()
    ISPanel.prerender(self);
end

function ISSLManager:render()
    ISPanel.render(self);
end


function ISSLManager:new (x, y, width, height)
    local o = ISPanel:new(x, y, width, height);
    setmetatable(o, self)
    self.__index = self
    o.x = x;
    o.y = y;
    o.background = true;
    o.backgroundColor = {r=1, g=0, b=0, a=1};
    o.borderColor = {r=0.4, g=0.4, b=0.4, a=1};
    o.width = width;
    o.height = height;
    o.anchorLeft = true;
    o.anchorRight = false;
    o.anchorTop = true;
    o.anchorBottom = false;
    --o.fontheight = getTextManager():MeasureStringY(UIFont.Small, "AbdfghijklpqtyZ")+2;
    return o
end

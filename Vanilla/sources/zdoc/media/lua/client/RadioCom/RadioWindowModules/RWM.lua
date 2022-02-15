--***********************************************************
--**                    THE INDIE STONE                    **
--**				  Author: turbotutone				   **
--***********************************************************
require "ISUI/ISPanel"

---@class RWM : ISPanel
RWM = ISPanel:derive("RWM");

function RWM:initialise()
    ISPanel.initialise(self)
end

function RWM:createChildren()
end

function RWM:readFromObject( _player, _deviceObject )
    self.player = _player;
    self.device = _deviceObject;
end

function RWM:prerender()
    ISPanel.prerender(self);
end


function RWM:render()
    ISPanel.render(self);
end


function RWM:new (x, y, width, height)
    local o = ISPanel:new(x, y, width, height);
    setmetatable(o, self)
    self.__index = self
    o.x = x;
    o.y = y;
    o.background = true;
    o.backgroundColor = {r=0, g=0, b=0, a=0.0};
    o.borderColor = {r=0.4, g=0.4, b=0.4, a=1};
    o.width = width;
    o.height = height;
    o.anchorLeft = true;
    o.anchorRight = false;
    o.anchorTop = true;
    o.anchorBottom = false;
    o.fontheight = getTextManager():MeasureStringY(UIFont.Small, "AbdfghijklpqtyZ")+2;
    return o
end

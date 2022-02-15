--***********************************************************
--**                    THE INDIE STONE                    **
--**				  Author: turbotutone				   **
--***********************************************************
require "ISUI/ISPanel"

---@class ISMoveablesIconToolTip : ISPanel
ISMoveablesIconToolTip = ISPanel:derive("ISMoveablesIconToolTip");

function ISMoveablesIconToolTip:initialise()
    ISPanel.initialise(self)
end

function ISMoveablesIconToolTip:createChildren()
    self:setWidth(self.marginOffset + getTextManager():MeasureStringX(UIFont.Small, self.subText)+20 )
end

function ISMoveablesIconToolTip:prerender()
    ISPanel.prerender(self);
end


function ISMoveablesIconToolTip:render()
    ISPanel.render(self);
    if getCell():getDrag(0) and getCell():getDrag(0).isMoveableCursor then
        self.subText = getText("IGUI_ToggleMode", Keyboard.getKeyName(getCore():getKey("Toggle mode")) ); --"Press '"..Keyboard.getKeyName(getCore():getKey("Toggle mode")).."' to change mode.";
        local text = getCell():getDrag(0):getMoveableMode():gsub("^%l", string.upper)
        local textY = (self:getHeight() - self.fontheight * 2) / 2
        self:drawText(getText("IGUI_" .. text), self.marginOffset+5,textY,1.0,0.85,0.05,1.0,UIFont.Small);
        self:drawText(self.subText, self.marginOffset+5,textY+self.fontheight,0.75,0.75,0.75,1.0,UIFont.Small);
    end
end


function ISMoveablesIconToolTip:new (x, y, width, height, marginOffset)
    local o = ISPanel:new(x, y, width, height);
    setmetatable(o, self)
    self.__index = self
    o.x = x;
    o.y = y;
    o.marginOffset = marginOffset or 0;
    o.background = true;
    o.subText = "Press '"..Keyboard.getKeyName(getCore():getKey("Toggle mode")).."' to change mode.";
    o.backgroundColor = {r=0, g=0, b=0, a=0.35};
    o.borderColor = {r=0.80, g=0.80, b=0.80, a=0};
    o.width = width;
    o.height = height;
    o.anchorLeft = true;
    o.anchorRight = false;
    o.anchorTop = true;
    o.anchorBottom = false;
    o.fontheight = getTextManager():MeasureStringY(UIFont.Small, "AbdfghijklpqtyZ")+2;
    return o
end

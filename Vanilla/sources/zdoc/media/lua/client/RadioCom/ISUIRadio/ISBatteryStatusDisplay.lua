--***********************************************************
--**                    THE INDIE STONE                    **
--**				  Author: turbotutone				   **
--***********************************************************
require "ISUI/ISPanel"

---@class ISBatteryStatusDisplay : ISPanel
ISBatteryStatusDisplay = ISPanel:derive("ISBatteryStatusDisplay");

function ISBatteryStatusDisplay:initialise()
    ISPanel.initialise(self)
end

function ISBatteryStatusDisplay:createChildren()
    if self.doLed then
        self.led = ISLedLight:new (10, (self.height-10)/2, 10, 10);
        self.led:initialise();
        self.led:getLedIsOn(false);
        self:addChild(self.led);
    end
end

function ISBatteryStatusDisplay:round(num, idp)
    local mult = 10^(idp or 0)
    return math.floor(num * mult + 0.5) / mult
end

function ISBatteryStatusDisplay:setPower( _power )
    if _power > 1.0 then _power = 1.0; end
    if _power < 0.0 then _power = 0.0; end
    self.power = _power;
    self.powerInt = self:round(100*_power, 0);
    self.powerDisp = tostring(self.powerInt).."%";
end

function ISBatteryStatusDisplay:update()
    ISPanel.update(self);
    if self.power > 0 and self.power < 0.25 then
        self.led:setIsBlinking( true );
    else
        self.led:setIsBlinking( false );
    end
end

function ISBatteryStatusDisplay:prerender()
    ISPanel.prerender(self);
end


function ISBatteryStatusDisplay:render()
    ISPanel.render(self);
    local offx = 0;
    if self.doLed then
        offx = self.led:getX() + self.led:getWidth();
    end

    local headW = (self:getWidth()-(offx+5))*0.02;
    if headW<2 then headW = 2; end
    local x,y,w,h = offx+5, 0, self:getWidth()-(offx+5+headW), self:getHeight();
    local c = self.borderColor;

    self:drawTextureScaled(self.backTexture, x, y, w, h, 0.7, 1.0, 1.0, 1.0);
    self:drawRect(x+(w*self.power), y, w-(w*self.power), h, 0.7, 0.0, 0.0, 0.0);
    self:drawRectBorder(x, y, w, h, c.a, c.r, c.g, c.b);

    self:drawTextCentre(self.powerDisp, x+(w/2)-1, y+((h/2)-(self.powerDispH/2))-1, 0.0, 0.0, 0.0, 1.0, UIFont.Small);
    self:drawTextCentre(self.powerDisp, x+(w/2)+1, y+((h/2)-(self.powerDispH/2))+1, 0.0, 0.0, 0.0, 1.0, UIFont.Small);
    self:drawTextCentre(self.powerDisp, x+(w/2)-1, y+((h/2)-(self.powerDispH/2))+1, 0.0, 0.0, 0.0, 1.0, UIFont.Small);
    self:drawTextCentre(self.powerDisp, x+(w/2)+1, y+((h/2)-(self.powerDispH/2))-1, 0.0, 0.0, 0.0, 1.0, UIFont.Small);
    self:drawTextCentre(self.powerDisp, x+(w/2), y+((h/2)-(self.powerDispH/2)), 1.0, 1.0, 1.0, 1.0, UIFont.Small);

    self:drawRect(x+w, y+(h*0.25), headW, h*0.5, c.a, c.r, c.g, c.b);
end


function ISBatteryStatusDisplay:new (x, y, width, height, addAlertLed)
    local o = ISPanel:new(x, y, width, height);
    setmetatable(o, self)
    self.__index = self
    o.x = x;
    o.y = y;
    o.background = false;
    o.backgroundColor = {r=0, g=0, b=0, a=0.0};
    o.borderColor = {r=0.8, g=0.8, b=0.8, a=1};
    o.width = width;
    o.height = height;
    o.anchorLeft = true;
    o.anchorRight = false;
    o.anchorTop = true;
    o.anchorBottom = false;
    o.fontheight = getTextManager():MeasureStringY(UIFont.Small, "AbdfghijklpqtyZ")+2;
    o.doLed = addAlertLed;
    o.backTexture = getTexture("Radio_ConditionGradient");
    o.power = 0.6;
    o.powerInt = 60;
    o.powerDisp = "60%";
    o.powerDispH = getTextManager():MeasureStringY(UIFont.Small, "60%");
    return o
end


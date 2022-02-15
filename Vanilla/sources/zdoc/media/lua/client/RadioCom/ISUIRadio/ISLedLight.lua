--***********************************************************
--**                    THE INDIE STONE                    **
--**				  Author: turbotutone				   **
--***********************************************************
require "ISUI/ISPanel"

---@class ISLedLight : ISPanel
ISLedLight = ISPanel:derive("ISLedLight");

function ISLedLight:initialise()
    ISPanel.initialise(self);
end

function ISLedLight:createChildren()
end

function ISLedLight:update()
    ISPanel.update(self);

    if self.doLedBlink then
        self.ledCntr = self.ledCntr + 1;
        if self.ledCntr > self.ledBlinkSpeed then
            self.ledIsOn = not self.ledIsOn;
            self.ledCntr = 0;
        end
    else
        self.ledIsOn = self.oldState;
    end
end

function ISLedLight:prerender()
    ISPanel.prerender(self);
end


function ISLedLight:render()
    ISPanel.render(self);

    if self.ledBackTexture and self.ledTexture then
        self:drawTextureScaled(self.ledBackTexture, 0, 0, self:getWidth(), self:getHeight(), 1.0, 1.0, 1.0, 1.0);

        local iw = math.floor(self:getWidth()/4);
        local ih = math.floor(self:getHeight()/4);

        if iw < 1 then iw = 1 end
        if ih < 1 then ih = 1 end

        local c = self.ledColOff;
        if self.ledIsOn then c = self.ledCol end
        self:drawTextureScaled(self.ledTexture, iw, ih, self:getWidth()-(iw*2), self:getHeight()-(ih*2), c.a, c.r, c.g, c.b);
    end
end

function ISLedLight:getLedIsOn()
    return self.ledIsOn;
end

function ISLedLight:setLedIsOn( _b )
    self.ledIsOn = _b;
    self.oldState = self.ledIsOn;
end

function ISLedLight:setBlinkingSpeed( _i ) --in ticks
    self.ledBlinkSpeed = _i;
end

function ISLedLight:setIsBlinking( _b )
    if _b == true and self.doLedBlink ~= _b then
        self.oldState = self.ledIsOn;
    end
    self.doLedBlink = _b;
end

function ISLedLight:setLedColor( _a, _r, _g, _b )
    if _a and _r and _g and _b then
        self.ledCol = {r=_r, g=_g, b=_b, a =_a};
    end
end

function ISLedLight:setLedColorOff( _a, _r, _g, _b )
    if _a and _r and _g and _b then
        self.ledColOff = {r=_r, g=_g, b=_b, a =_a};
    end
end


function ISLedLight:new (x, y, width, height)
    local o = ISPanel:new(x, y, width, height);
    setmetatable(o, self)
    self.__index = self
    o.x = x;
    o.y = y;
    o.background = false;
    o.backgroundColor = {r=1, g=1, b=1, a=1.0};
    o.borderColor = {r=0.4, g=0.4, b=0.4, a=1};
    o.width = width;
    o.height = height;
    o.anchorLeft = true;
    o.anchorRight = false;
    o.anchorTop = true;
    o.anchorBottom = false;
    o.ledBackTexture = getTexture("Radio_ledBackground");
    o.ledTexture = getTexture("Radio_ledLight");
    o.ledCol = {r=1.0, g=0.0, b=0.0, a =1.0};
    o.ledColOff = {r=0.3, g=0.0, b=0.0, a =1.0};
    --o.ledSize = 8;
    o.doLedBlink = false;
    o.ledIsOn = false;
    o.ledBlinkSpeed = 10;
    o.ledCntr = 0;
    o.oldState = o.ledIsOn;
    return o
end


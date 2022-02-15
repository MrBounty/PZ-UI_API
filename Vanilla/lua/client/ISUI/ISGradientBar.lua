--***********************************************************
--**                    THE INDIE STONE                    **
--**				  Author: turbotutone				   **
--***********************************************************
require "ISUI/ISPanel"

ISGradientBar = ISPanel:derive("ISGradientBar");

function ISGradientBar:initialise()
    ISPanel.initialise(self)
end

function ISGradientBar:createChildren()
    --instanceItem("item");
end

function ISGradientBar:prerender()
    ISPanel.prerender(self);
end


function ISGradientBar:render()
    ISPanel.render(self);

    local w,h = self:getWidth(), self:getHeight();
    local radius = self.settings.radius;
    local valOffset;
    if self.settings.doKnob then
        --valOffset = PZMath.clamp(self.value * w, self.settings.radius, w-radius);
        valOffset = PZMath.clamp(self.value * w, 0, w);
    else
        --valOffset = PZMath.clamp(self.value * w, self.settings.radius+3, w-radius-3);
        valOffset = PZMath.clamp(self.value * w, 3, w-3);
    end

    self:drawTextureScaled(self.gradientTex, 0,0,w,h, 1.0, 1.0, 1.0, 1.0 );

    if self.settings.darkAlpha > 0 then
        if valOffset > radius then
            self:drawTextureScaled(nil, 0,0,valOffset-radius,h, self.settings.darkAlpha, 0.0, 0.0, 0.0 );
        end
        if valOffset < w-radius then
            self:drawTextureScaled(nil, valOffset+radius,0,w-(valOffset+radius),h, self.settings.darkAlpha, 0.0, 0.0, 0.0 );
        end
        self:drawTextureScaled(self.highlightTex, valOffset-radius,0,radius*2,h, self.settings.darkAlpha, 0.0, 0.0, 0.0 );
    end

    local c = self.settings.colBorder;
    self:drawRectBorder(0, 0, w, h, c.a, c.r, c.g, c.b);

    c = self.settings.colBorderInner;
    self:drawRectBorder(1, 1, w-2, h-2, c.a, c.r, c.g, c.b);

    if self.settings.doKnob then
        c = self.settings.colBorderInner;
        self:drawRectBorder(valOffset-radius-3, 0, (radius*2)+6, h, c.a, c.r, c.g, c.b);
        self:drawRectBorder(valOffset-radius-1, 2, (radius*2)+2, h-4, c.a, c.r, c.g, c.b);

        c = self.settings.colBorder;
        self:drawRectBorder(valOffset-radius-2, 1, (radius*2)+4, h-2, c.a, c.r, c.g, c.b);
    end
end

function ISGradientBar:setGradientTexture(_tex)
    self.gradientTex = _tex;
end

function ISGradientBar:setHighlightRadius(_rad)
    self.settings.radius = _rad;
end

function ISGradientBar:setDarkAlpha(_alpha)
    self.settings.darkAlpha = _alpha;
end

function ISGradientBar:setBorderColor(_a, _r, _g, _b)
    self.settings.colBorder.a = _a;
    self.settings.colBorder.r = _r;
    self.settings.colBorder.g = _g;
    self.settings.colBorder.b = _b;
end

function ISGradientBar:setBorderInnerColor(_a, _r, _g, _b)
    self.settings.colBorderInner.a = _a;
    self.settings.colBorderInner.r = _r;
    self.settings.colBorderInner.g = _g;
    self.settings.colBorderInner.b = _b;
end

function ISGradientBar:setDoKnob(_b)
    self.settings.doKnob = _b;
end

function ISGradientBar:setValue(_v)
    self.value = PZMath.clamp(_v, 0, 1);
end

function ISGradientBar:new (x, y, width, height)
    local o = ISPanel:new(x, y, width, height);
    setmetatable(o, self)
    self.__index = self
    o.x = x;
    o.y = y;
    o.background = true;
    o.backgroundColor = {r=0, g=0, b=0, a=1.0};
    o.borderColor = {r=0.4, g=0.4, b=0.4, a=1};

    o.width = width;
    o.height = height;
    o.anchorLeft = true;
    o.anchorRight = false;
    o.anchorTop = true;
    o.anchorBottom = false;

    o.value = 0;
    o.gradientTex = getTexture("media/ui/BodyInsulation/heatbar_horz");
    o.highlightTex = getTexture("media/ui/BodyInsulation/gradient_highlight");
    o.highlightTex_L = getTexture("media/ui/BodyInsulation/gradient_highlight_left");
    o.highlightTex_R = getTexture("media/ui/BodyInsulation/gradient_highlight_right");
    o.settings = {};
    o.settings.radius = 3;
    o.settings.darkAlpha = 0.70;
    o.settings.colBorder = { a=1.0, r=0.45, g=0.45, b=0.45};
    o.settings.colBorderInner = { a=1.0, r=0.10, g=0.10, b=0.10};
    o.settings.doKnob = true;
    return o
end


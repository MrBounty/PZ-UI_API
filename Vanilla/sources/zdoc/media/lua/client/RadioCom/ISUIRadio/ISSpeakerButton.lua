--***********************************************************
--**                    THE INDIE STONE                    **
--**				  Author: turbotutone				   **
--***********************************************************
require "ISUI/ISPanel"

---@class ISSpeakerButton : ISPanel
ISSpeakerButton = ISPanel:derive("ISSpeakerButton");

function ISSpeakerButton:initialise()
    ISPanel.initialise(self)
end

function ISSpeakerButton:createChildren()
end

function ISSpeakerButton:onMouseMove(x, y)
    if not self:getIsVisible() then
        return;
    end
    self.hover = true;
end

function ISSpeakerButton:onMouseMoveOutside(x, y)
    self.hover = false;
end

function ISSpeakerButton:onMouseUp(x, y)
    if not self:getIsVisible() or (not self.enableControls) then
        return;
    end
    --self.isMute = not self.isMute;
    if self.onclick and self.onclickTarget then
        self.onclick(self.onclickTarget, not self.isMute);
    end
end

function ISSpeakerButton:prerender()
    ISPanel.prerender(self);
end


function ISSpeakerButton:render()
    ISPanel.render(self);
    if self.speakerTexture then
        -- texture box
        if self.hover then
            self:drawRect(0, 0, self:getWidth(), self:getHeight(), 1.0, 0.3, 0.3, 0.3);
        end
        self:drawRectBorder(0, 0, self:getWidth(), self:getHeight(), 1.0, 0.8, 0.8, 0.8)
        -- texture
        --self:drawTextureScaled(self.speakerTexture, self.marginLeft+self.speakerTexture:getOffsetX(), self.marginTop+self.speakerTexture:getOffsetY(), self.textureSize, self.textureSize, 1.0, 1.0, 1.0, 1.0);
        self:drawTextureScaled(self.speakerTexture, 2, 2, self:getWidth()-4, self:getHeight()-4, 1.0, 1.0, 1.0, 1.0);
        if self.isMute then
            self:drawTextureScaled(self.muteTexture, 2, 2, self:getWidth()-4, self:getHeight()-4, 1.0, 1.0, 0.0, 0.0);
        end
        --self:drawTexture(self.speakerTexture, self.marginLeft+self.speakerTexture:getOffsetX(), self.marginTop+self.speakerTexture:getOffsetY(), 1.0, 1.0, 1.0, 1.0);
    end
end

function ISSpeakerButton:setEnableControls(_b)
    self.enableControls = _b;
end
function ISSpeakerButton:getEnableControls()
    return self.enableControls;
end


function ISSpeakerButton:new (x, y, width, height, onclick, onclickTarget)
    local o = ISPanel:new(x, y, width, height);
    setmetatable(o, self)
    self.__index = self
    o.x = x;
    o.y = y;
    o.background = false;
    o.backgroundColor = {r=0, g=0, b=0, a=0.0};
    o.borderColor = {r=0.4, g=0.4, b=0.4, a=1};
    o.width = width;
    o.height = height;
    o.anchorLeft = true;
    o.anchorRight = false;
    o.anchorTop = true;
    o.anchorBottom = false;
    o.isMute = false;
    o.onclick = onclick;
    o.onclickTarget = onclickTarget;
    o.speakerTexture = getTexture("Icon_Radio_Speaker");
    o.muteTexture = getTexture("Icon_Radio_Forbidden");
    o.enableControls = true;
    return o
end

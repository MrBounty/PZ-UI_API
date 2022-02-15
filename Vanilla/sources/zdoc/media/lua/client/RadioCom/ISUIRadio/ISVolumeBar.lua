--***********************************************************
--**                    THE INDIE STONE                    **
--**				  Author: turbotutone				   **
--***********************************************************
require "ISUI/ISPanel"

---@class ISVolumeBar : ISPanel
ISVolumeBar = ISPanel:derive("ISVolumeBar");

function ISVolumeBar:initialise()
    ISPanel.initialise(self)
end

function ISVolumeBar:createChildren()
end

--function ISVolumeBar:readFromObject( _player, _deviceObject )
    --self.player = _player;
    --self.device = _deviceObject;
--end

function ISVolumeBar:isDragging()
    if not self:getIsVisible() or self.enableControls == false or self.mouseEnabled == false then
        return false;
    end
    return self.dragInside;
end

function ISVolumeBar:onMouseDown(x, y)
    if not self:getIsVisible() or self.enableControls == false or self.mouseEnabled == false then
        return;
    end
    self.dragInside = true;
end

--function ISVolumeBar:onMouseDownOutside(x, y)
    --self.dragInside = false;
--end

function ISVolumeBar:onMouseUpOutside(x, y)
    if not self:getIsVisible() or self.enableControls == false or self.mouseEnabled == false then
        self.dragInside = false;
        return;
    end
    --self.dragInside = false;
    self:onMouseUp(self:getMouseX(),y);

    --self.hoverVolume = -1;
end

function ISVolumeBar:resetHoverVolume()
    --self.dragInside = false;
    self.hoverVolume = -1;
end
--function ISVolumeBar:onMouseUp(x, y)
    --self.dragInside = false;
--end

function ISVolumeBar:onMouseMove(x, y)
    if not self:getIsVisible() or self.enableControls == false or self.dragInside == false or self.mouseEnabled == false then
        return;
    end
    local newVol = self:getVolumeFromXPosition( self:getMouseX() );
    if newVol then
        self.hoverVolume = newVol;
    end
end

function ISVolumeBar:onMouseMoveOutside(x, y)
    --self.hoverVolume = -1;
    self:onMouseMove(x,y);
end

function ISVolumeBar:onMouseUp(x, y)
    --self.dragInside = false;
    --self.hoverVolume = -1;
    if not self:getIsVisible() or self.enableControls == false or self.dragInside == false or self.mouseEnabled == false then
        return;
    end
    self.dragInside = false;
    print(x);
    local newVol = self:getVolumeFromXPosition( x );
    if newVol then
        --self.volume = newVol;
        self.hoverVolume = newVol;
        if self.onVolumeChange and self.onVolumeChangeTarget then
            self.onVolumeChange(self.onVolumeChangeTarget, self.hoverVolume); --self.volume);
        end
    end
end

function ISVolumeBar:getVolumeFromXPosition( _x )
    local cellwidth = (self:getWidth() / self.volumeSteps);
    if _x <=0 then return 1 end
    for i = 0, self.volumeSteps-1 do
        if _x > i*cellwidth and _x < (i*cellwidth)+cellwidth then
            return i+1;
        end
    end
    return self.volumeSteps;
end

function ISVolumeBar:setEnableControls(b)
    self.enableControls = b;
end

function ISVolumeBar:getHoverVolume()
    return self.hoverVolume;
end

function ISVolumeBar:setHoverVolume(_vol)
    self.hoverVolume = _vol;
end

function ISVolumeBar:getVolume()
    return self.volume;
end

function ISVolumeBar:setVolumeJoypad(_up)
    if self:getIsVisible() and self.enableControls then
        local newvol = self.hoverVolume==-1 and self.volume or self.hoverVolume;
        if(_up) then newvol = newvol+1 else newvol = newvol-1 end
        if newvol < 1 then newvol = 1 end
        if newvol > self.volumeSteps then newvol = self.volumeSteps end
        if self.hoverVolume ~= newvol then
            self.hoverVolume = newvol;
            self.dragInside = false;
            self.joyMeter = 60;
        end
    end
end

function ISVolumeBar:setVolume(vol)
    self.volume = vol;
    if self.volume == self.hoverVolume then
        --print("newVolume: ",self.volume);
        self.hoverVolume = -1;
    end
end

function ISVolumeBar:getVolumeSteps()
    return self.volumeSteps;
end

function ISVolumeBar:setVolumeSteps(vol)
    self.volumeSteps = vol;
end

function ISVolumeBar:update()
    ISPanel.update(self);
    if self.joyMeter > 0 then
        if self.joyMeter == 1 then
            if self.hoverVolume>=1 and self.onVolumeChange and self.onVolumeChangeTarget then
                self.onVolumeChange(self.onVolumeChangeTarget, self.hoverVolume); --self.volume);
            end
        end
        self.joyMeter = self.joyMeter-1;
    end
end

function ISVolumeBar:prerender()
    ISPanel.prerender(self);
end


function ISVolumeBar:render()
    ISPanel.render(self);
    local cellwidth = (self:getWidth() / self.volumeSteps);
    for i = 0, self.volumeSteps-1 do
        local c = self.elBackgroundColor;
        local c2 = self.elBorderColor;
        local curVol = i+1;
        if curVol <= self.volume then c = self.elHighlightColor; end

        if self.hoverVolume and self.hoverVolume >= 1 and self.hoverVolume <= self.volumeSteps then
            if curVol == self.hoverVolume then
                c2 = self.elBorderHighlightColor;
            end

            if self.hoverVolume <= self.volume then
                if curVol > self.hoverVolume and curVol <= self.volume then
                    c = self.elHoverColor;
                end
            else
                if curVol > self.volume and curVol <= self.hoverVolume then
                    c = self.elHoverColor;
                end
            end
        end

        if not self.enableControls then
            c = self.greyCol;
        end

        local hhalf = self:getHeight()/2;
        self:drawRect((i*cellwidth)+self.innerMargin, hhalf-(hhalf/2), cellwidth-self.innerMargin, hhalf, c.a, c.r, c.g, c.b);
        self:drawRectBorder((i*cellwidth)+self.innerMargin, hhalf-(hhalf/2), cellwidth-self.innerMargin, hhalf, c2.a, c2.r, c2.g, c2.b);
    end
end

function ISVolumeBar:setX(x)
    self.posChange = true;
    ISPanel.setX(self, x);
end
function ISVolumeBar:setY(y)
    self.posChange = true;
    ISPanel.setY(self, y);
end
function ISVolumeBar:setWidth(w)
    self.dimChange = true;
    ISPanel.setWidth(self, w);
end
function ISVolumeBar:setHeight(h)
    self.dimChange = true;
    ISPanel.setHeight(self, h);
end


function ISVolumeBar:new (x, y, width, height, onVolumeChange, onVolumeChangeTarget)
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
    o.fontheight = getTextManager():MeasureStringY(UIFont.Small, "AbdfghijklpqtyZ")+2;
    o.volumeSteps = 10;
    o.volume = 6;
    o.hoverVolume = -1;
    o.dimChange = false;
    o.posChange = false;
    o.innerMargin = 4;
    o.greyCol = { r=0.4,g=0.4,b=0.4,a=1};
    o.elBackgroundColor = {r=0, g=0.1, b=0, a=1.0};
    o.elHighlightColor = {r=0, g=1.0, b=0, a=1.0};
    o.elHoverColor = {r=0, g=0.5, b=0, a=1.0};
    o.elBorderColor = {r=0.8, g=0.8, b=0.8, a=1};
    o.elBorderHighlightColor = {r=1, g=1.0, b=1, a=1.0};
    o.onVolumeChange = onVolumeChange;
    o.onVolumeChangeTarget = onVolumeChangeTarget;
    o.enableControls = true;
    o.dragInside = false;
    o.joyMeter = 0;
    --toggle mouse functionallity
    o.mouseEnabled = true;
    return o
end

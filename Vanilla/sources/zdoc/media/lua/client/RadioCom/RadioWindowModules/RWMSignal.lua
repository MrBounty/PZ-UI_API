--***********************************************************
--**                    THE INDIE STONE                    **
--**				  Author: turbotutone				   **
--***********************************************************

require "RadioCom/RadioWindowModules/RWMPanel"

---@class RWMSignal : RWMPanel
RWMSignal = RWMPanel:derive("RWMSignal");

function RWMSignal:initialise()
    ISPanel.initialise(self)
end

function RWMSignal:createChildren()

    self.sineWaveDisplay = ISSineWaveDisplay:new (10, 4, self:getWidth()-20, 24);
    self.sineWaveDisplay:initialise();
    self:addChild(self.sineWaveDisplay);
    self:setDefaultWave();

    local yoffset = self.sineWaveDisplay:getY()+self.sineWaveDisplay:getHeight()+5;
    if self.addTestButton then
        self.testButton = ISButton:new(10, yoffset, self:getWidth()-20,18,"Test incoming signal",self, RWMSignal.doSignal);
        self.testButton:initialise();
        self.testButton.backgroundColor = {r=0, g=0, b=0, a=0.0};
        self.testButton.backgroundColorMouseOver = {r=1.0, g=1.0, b=1.0, a=0.1};
        self.testButton.borderColor = {r=1.0, g=1.0, b=1.0, a=0.3};
        self:addChild(self.testButton);

        yoffset = yoffset+self.testButton:getHeight()+4;
    end

    self:setHeight(yoffset);
    self.cacheHeight = yoffset;
end

function RWMSignal:setDefaultWave()
    self.sineWaveDisplay:setWaveParameters(0.3, 1.0, 0.1, 0.3, 30, 150, 5, 15);
end

function RWMSignal:doSignal( _time )
    self.sineWaveDisplay:setWaveParameters(0.15, 0.5, 0.3, 1.0, 1, 10, 1, 6);
    self.updCntr = not _time and 150 or 0;
    --self.incomingSignal = true;
end

function RWMSignal:clear()
    RWMPanel.clear(self);
end

function RWMSignal:readFromObject( _player, _deviceObject, _deviceData, _deviceType )
    if _deviceData:getIsTelevision() or _deviceData:isNoTransmit() then
        return false;
    end
    local valid = RWMPanel.readFromObject(self, _player, _deviceObject, _deviceData, _deviceType );
    if valid and self.deviceData and self.deviceData:getIsHighTier() then
        self:setHeight(self.cacheHeight+self.fontheight+2);
        self.distanceText = getText("IGUI_RadioDistance")..": ~ "..getText("IGUI_RadioMeter");
        self.distanceStrWidth = getTextManager():MeasureStringX(UIFont.Small, self.distanceText);
        self.drawDistance = true;
    else
        self:setHeight(self.cacheHeight);
    end
    return valid;
end

function RWMSignal:update()
    ISPanel.update(self);

    if self.updCntr > 0 then
        self.updCntr = self.updCntr - 1;
        if self.updCntr <= 0 then
            self.updCntr = 0;
            self:setDefaultWave();
        end
    end

    if self.deviceData then
        if self.deviceData:isReceivingSignal()==true and self.incomingSignal==false then
            self:doSignal(true);
            self.incomingSignal = true;
            if self.deviceData:getLastRecordedDistance() >= 0 then
                self.distanceText = getText("IGUI_RadioDistance")..": "..tostring(self.deviceData:getLastRecordedDistance()).." "..getText("IGUI_RadioMeter");
            else
                self.distanceText = getText("IGUI_RadioDistance")..": ~ "..getText("IGUI_RadioMeter");
            end
            self.distanceStrWidth = getTextManager():MeasureStringX(UIFont.Small, self.distanceText);
        elseif self.deviceData:isReceivingSignal()==false and self.incomingSignal==true then
            self:setDefaultWave();
            self.incomingSignal = false;
            self.distanceText = getText("IGUI_RadioDistance")..": ~ "..getText("IGUI_RadioMeter");
            self.distanceStrWidth = getTextManager():MeasureStringX(UIFont.Small, self.distanceText);
        end

        if self.deviceData:getIsTurnedOn() then
            self.sineWaveDisplay:toggleOn( true );
        else
            self.sineWaveDisplay:toggleOn( false );
        end
    end
end

function RWMSignal:prerender()
    ISPanel.prerender(self);
end

function RWMSignal:render()
    ISPanel.render(self);
    if self.drawDistance then
        local x = (self:getWidth()/2) - (self.distanceStrWidth/2);
        self:drawText(self.distanceText, x, self.cacheHeight, 1,1,1,1, UIFont.Small);
    end
end

function RWMSignal:new (x, y, width, height)
    local o = {}
    --o.data = {}
    o = RWMPanel:new(x, y, width, height);
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
    o.fontheight = getTextManager():MeasureStringY(UIFont.Small, "AbdfghijklpqtyZ");
    o.incomingSignal = false;
    o.updCntr = 0;
    o.addTestButton = false;
    o.distanceText = "";
    o.drawDistance = false;
    o.distanceStrWidth = 0;
    return o
end

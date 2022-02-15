--***********************************************************
--**                    THE INDIE STONE                    **
--**				  Author: turbotutone				   **
--***********************************************************

require "RadioCom/RadioWindowModules/RWMPanel"

---@class RWMGeneral : RWMPanel
RWMGeneral = RWMPanel:derive("RWMGeneral");

function RWMGeneral:initialise()
    ISPanel.initialise(self)
end

function RWMGeneral:createChildren()
    --[[
    self.labelName = ISLabel:new(4,2,self.fontheight+2,"device",1,1,1,1,UIFont.Small, true);
    self:addChild(self.labelName);

    self.descriptionPanel = ISRichTextPanel:new(0, 0, 260, 0);
    self.descriptionPanel.marginRight = 0
    self.descriptionPanel:setMargins(2, 2, 4, 4);
    self.descriptionPanel:initialise();
    self.descriptionPanel:instantiate();
    self.descriptionPanel:noBackground();
    self.descriptionPanel.backgroundColor = {r=1, g=0, b=0, a=0.3};
    self.descriptionPanel.borderColor = {r=1, g=1, b=1, a=0.1};
    --self.descriptionPanel:setMargins(2, 2, 4, 4);
    self.descriptionPanel.autosetheight = true;
    --self.descriptionPanel:setY(self:getAbsoluteY() + y);
    --self.descriptionPanel:setX();
    --]]
end

function RWMGeneral:clear()
    RWMPanel.clear(self);
end

function RWMGeneral:readFromObject( _player, _deviceObject, _deviceData, _deviceType )
    RWMPanel.readFromObject(self, _player, _deviceObject, _deviceData, _deviceType );

    if _deviceData:isNoTransmit() then
        return false;
    end

    --self.itemTexture = self.device:getTexture();
    if self.deviceType == "InventoryItem" and self.device.getTexture then
        self.itemTexture = self.device:getTexture();
        self.isoTexture = false;
    elseif self.deviceType == "IsoObject" and self.device.getSprite and self.device:getSprite() and self.device:getSprite():getName() then
        self.itemTexture = getTexture(self.device:getSprite():getName());
        self.isoTexture = true;
    else
        --self:clear();
        --return false;
    end
--    self.itemName = self.device:getName();

    self:setInfoLines()

    local h = 10+32;
    local h2 = 10 + (#self.infoLines * (self.fontheight));
    h = h2>h and h2 or h;

    self:setHeight(h);
    return true;
end

function RWMGeneral:setInfoLines()
    self.infoLines = {};
    if self.deviceData then
        self.isTv = self.deviceData:getIsTelevision();
        if self.isTv then
            self:addInfoLine(getText("IGUI_RadioDevice")..": ", self.deviceData:getDeviceName());
            local zomboidRadio = getZomboidRadio();
            if zomboidRadio then
                local channelName = zomboidRadio:getChannelName(self.deviceData:getChannel()) or getText("IGUI_RadioUknownChannel");
                self:addInfoLine(getText("IGUI_RadioChannel")..": ", channelName);
            end
        else
            local zomboidRadio = getZomboidRadio();
            if zomboidRadio then
                local channelName = zomboidRadio:getChannelName(self.deviceData:getChannel()) or getText("IGUI_RadioUknownChannel");
                self:addInfoLine(getText("IGUI_RadioChannel")..": ", channelName);
            end
            --self:addInfoLine("Device: ", self.deviceData:getDeviceName());
            self:addInfoLine(getText("IGUI_RadioFrequency")..": ", tostring(self.deviceData:getChannel()/1000).." MHz");
            self:addInfoLine(getText("IGUI_RadioFreqRange")..": ", tostring(self.deviceData:getMinChannelRange()/1000).." MHz - " .. tostring(self.deviceData:getMaxChannelRange()/1000).." MHz");
            self:addInfoLine(getText("IGUI_RadioTwoway")..": ", self.deviceData:getIsTwoWay() and getText("IGUI_RadioYes") or getText("IGUI_RadioNo"));
            if self.deviceData:getIsTwoWay() then
                self:addInfoLine(getText("IGUI_RadioStrength")..": ", tostring(self.deviceData:getTransmitRange()).." "..getText("IGUI_RadioMeter"));
            end
        end
    end
end

function RWMGeneral:addInfoLine( _prefix, _line )
    table.insert(self.infoLines, { prefix= _prefix, line = _line });
end

function RWMGeneral:recalulateDim()
end

function RWMGeneral:update()
    ISPanel.update(self);

    if self.deviceData and self.deviceData:getChannel() ~= self.deviceFrequency then
        self.deviceFrequency = self.deviceData:getChannel();
        self:setInfoLines();
    end
end

function RWMGeneral:prerender()
    ISPanel.prerender(self);
    --ISUIElement:drawTextureScaled(texture, x, y, w, h, a, r, g, b)
end


function RWMGeneral:render()
    --ISUIElement:drawTextureScaledAspect(texture, x, y, w, h, a, r, g, b)
    ISPanel.render(self);
        --self:drawRect(0, 0, self.width, self.height, 0.5, 0.0, 1.0, 0.0)
        --self:drawRectBorder(0, 0, self.width, self.height, 0.9, 0.0, 1.0, 0.0)
    if self.itemTexture then
        -- texture box
        self:drawRect(10, 5, 32, 32, 1.0, 0.0, 0.0, 0.0)
        self:drawRectBorder(10, 5, 32, 32, 1.0, 0.8, 0.8, 0.8)
        -- texture
        self:drawTextureScaledAspect2(self.itemTexture, 11, 6, 30, 30, 1.0, 1.0, 1.0, 1.0)
        --self:drawTexture(self.itemTexture, 4+self.itemTexture:getOffsetX(), self.fontheight+4+self.itemTexture:getOffsetY(), 1.0, 1.0, 1.0, 1.0);
    end

    local x, y = 42 + ((self:getWidth()-42)/3),5;
    for i,v in ipairs(self.infoLines) do
        local ii =i-1;
        self:drawTextRight(v.prefix, x, y+(ii*(self.fontheight)), 1,1,1,1, UIFont.Small);
        self:drawText(v.line, x, y+(ii*(self.fontheight)), 1,1,1,1, UIFont.Small);
    end

    --self.descriptionPanel:setY(self:getAbsoluteY() + self.fontheight+4)
    --self.descriptionPanel:setX(self:getAbsoluteX() + 4 + 32 + 4);
        --local dm = ISRichTextPanel.drawMargins;
        --ISRichTextPanel.drawMargins = true;
    --self.descriptionPanel:prerender();
    --self.descriptionPanel:render();
        --ISRichTextPanel.drawMargins = dm;
end


function RWMGeneral:new (x, y, width, height)
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
    o.fontheight = getTextManager():MeasureStringY(UIFont.Small, "AbdfghijklpqtyZ")+2;
    o.infoLines = {};
    o.deviceFrequency = 0;
    return o
end

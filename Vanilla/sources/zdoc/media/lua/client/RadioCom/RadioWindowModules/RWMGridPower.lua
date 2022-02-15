--***********************************************************
--**                    THE INDIE STONE                    **
--**				  Author: turbotutone				   **
--***********************************************************
require "RadioCom/RadioWindowModules/RWMPanel"

---@class RWMGridPower : RWMPanel
RWMGridPower = RWMPanel:derive("RWMGridPower");

local FONT_HGT_SMALL = getTextManager():getFontHeight(UIFont.Small)

function RWMGridPower:initialise()
    ISPanel.initialise(self)
end

function RWMGridPower:createChildren()
    self:setHeight(32);

    local xoff = 0;

    self.led = ISLedLight:new (10, (self.height-10)/2, 10, 10);
    self.led:initialise();
    self.led:setLedColor( 1, 0, 1, 0 );
    self.led:setLedColorOff( 1, 0, 0.3, 0 );
    self:addChild(self.led);

    xoff = self.led:getX() + self.led:getWidth();

    local buttonW = getTextManager():MeasureStringX(UIFont.Small, getText("ContextMenu_Turn_Off"))+10;
    self.toggleOnOffButton = ISButton:new(xoff+10, 4, buttonW,self.height-8,getText("ContextMenu_Turn_On"),self, RWMGridPower.toggleOnOff);
    self.toggleOnOffButton:initialise();
    self.toggleOnOffButton.backgroundColor = {r=0, g=0, b=0, a=0.0};
    self.toggleOnOffButton.backgroundColorMouseOver = {r=1.0, g=1.0, b=1.0, a=0.1};
    self.toggleOnOffButton.borderColor = {r=1.0, g=1.0, b=1.0, a=0.3};
    self:addChild(self.toggleOnOffButton);

    --[[
    xoff = xoff + (self.toggleOnOffButton:getX()-xoff) + self.toggleOnOffButton:getWidth();

    self.itemDropBox = ISItemDropBox:new (xoff+10, 4, self.height-8, self.height-8, false, self, RWMGridPower.addBattery, RWMGridPower.removeBattery, RWMGridPower.verifyItem, nil );
    self.itemDropBox:initialise();
    self.itemDropBox:setBackDropTex( getTexture("Item_Battery"), 0.2, 1,1,1 );
    self.itemDropBox:setDoBackDropTex( true );
    self.itemDropBox:setToolTip( true, "Drag a battery in here, or rightclick to remove it." );
    self:addChild(self.itemDropBox);

    xoff = xoff + (self.itemDropBox:getX()-xoff) + self.itemDropBox:getWidth();

    self.batteryStatus = ISBatteryStatusDisplay:new ( xoff+10, 4, self.width-(20+ xoff), self.height-8, true);
    self.batteryStatus:initialise();
    self:addChild(self.batteryStatus);
    --]]
end

function RWMGridPower:toggleOnOff()
    if self:doWalkTo() then
        ISTimedActionQueue.add(ISRadioAction:new("ToggleOnOff",self.player, self.device ));
    end
end

--[[
function RWMGridPower:removeBattery()
    ISTimedActionQueue.add(ISRadioAction:new("RemoveBattery",self.player, self.device ));
end

function RWMGridPower:addBattery( _items )
    local item;
    local pbuff = 0;

    for _,i in ipairs(_items) do
        if i:getDelta() > pbuff then
            item = i;
            pbuff = i:getDelta()
        end
    end

    if item then
        ISTimedActionQueue.add(ISRadioAction:new("AddBattery",self.player, self.device, item ));
    end
end

function RWMGridPower:verifyItem( _item )
    if _item:getFullType() == "Base.Battery" then
        return true;
    end
end
--]]

function RWMGridPower:clear()
    RWMPanel.clear(self);
    --self.player = nil;
    --self.device = nil;
    --self.deviceData = nil;
end

function RWMGridPower:readFromObject( _player, _deviceObject, _deviceData, _deviceType )
    if _deviceData:getIsBatteryPowered() then
        return false;
    end
    return RWMPanel.readFromObject(self, _player, _deviceObject, _deviceData, _deviceType );
    --self.player = _player;
    --self.device = _deviceObject;
    --self.deviceData = _deviceData;

    --self.stateCache = { isOn = self.device:IsTurnedOn(),  }
end

function RWMGridPower:update()
    ISPanel.update(self);

    if self.player and self.device and self.deviceData then
        local isOn = self.deviceData:getIsTurnedOn();
        self.led:setLedIsOn(isOn);

        if isOn then
            self.toggleOnOffButton:setTitle(getText("ContextMenu_Turn_Off"));
        else
            self.toggleOnOffButton:setTitle(getText("ContextMenu_Turn_On"));
        end

        --[[
        self.batteryStatus:setPower( self.deviceData:getPower() );

        if self.deviceData:getHasBattery() then
            self.itemDropBox:setStoredItemFake( self.batteryTex );
        else
            self.itemDropBox:setStoredItemFake( nil );
        end
        --]]
    end
end

function RWMGridPower:prerender()
    ISPanel.prerender(self);
end


function RWMGridPower:render()
    ISPanel.render(self);
    if self.deviceData then
        local x = self.toggleOnOffButton:getX()+self.toggleOnOffButton:getWidth()+5
        local y = (self.height - FONT_HGT_SMALL) / 2
        if self.deviceData:canBePoweredHere() then
            self:drawText(getText("IGUI_RadioPowerNearby"), x, y, 0,1,0,1, UIFont.Small);
        else
            self:drawText(getText("IGUI_RadioRequiresPowerNearby"), x, y, 1,0,0,1, UIFont.Small);
        end
    end
end

function RWMGridPower:onJoypadDown(button)
    if button == Joypad.AButton then
        self:toggleOnOff()
    end
end

function RWMGridPower:getAPrompt()
    if self.deviceData:getIsTurnedOn() then
        return getText("ContextMenu_Turn_Off");
    else
        return getText("ContextMenu_Turn_On");
    end
end
function RWMGridPower:getBPrompt()
    return nil;
end
function RWMGridPower:getXPrompt()
    return nil;
end
function RWMGridPower:getYPrompt()
    return nil;
end


function RWMGridPower:new (x, y, width, height)
    local o = RWMPanel:new(x, y, width, height);
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
    --o.fontheight = getTextManager():MeasureStringY(UIFont.Small, "AbdfghijklpqtyZ")+2;
    --o.batteryTex = getTexture("Item_Battery");
    return o
end


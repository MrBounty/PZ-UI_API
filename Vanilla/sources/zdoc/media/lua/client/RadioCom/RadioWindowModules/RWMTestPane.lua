--***********************************************************
--**                    THE INDIE STONE                    **
--**				  Author: turbotutone				   **
--***********************************************************
require "RadioCom/RadioWindowModules/RWMPanel"

---@class RWMTestPane : RWMPanel
RWMTestPane = RWMPanel:derive("RWMTestPane");

function RWMTestPane:initialise()
    ISPanel.initialise(self)
end

function RWMTestPane:createChildren()
    self:setHeight(32);

    --ISItemDropBox:new (x, y, width, height, storeItem, target, onItemDropped, onItemRemove, onVerifyItem, onDragSelf)
    self.itemDropBox = ISItemDropBox:new (10, 4, self.height-8, self.height-8, false, self, nil, nil, RWMTestPane.verifyItem, nil );
    self.itemDropBox:initialise();
    self.itemDropBox:setBackDropTex( getTexture("Item_Battery"), 0.2, 1,1,1 );
    self.itemDropBox:setDoBackDropTex( true );
    self.itemDropBox:setToolTip( true, "Drag a battery in here, or rightclick to remove it." );
    self:addChild(self.itemDropBox);

    self.led = ISLedLight:new (10+5+(self.height-8), (self.height-10)/2, 10, 10);
    self.led:initialise();
    self.led:setIsBlinking( true );
    self:addChild(self.led);
end

function RWMTestPane:verifyItem( _item )
    if _item:getFullType() == "Base.Battery" then
        return true;
    end
end

function RWMTestPane:clear()
    self.player = nil;
    self.device = nil;
    self.deviceData = nil;
end

function RWMTestPane:readFromObject( _player, _deviceObject, _deviceData, _deviceType )
    RWMPanel.readFromObject(self, _player, _deviceObject, _deviceData, _deviceType);
end

function RWMTestPane:prerender()
    ISPanel.prerender(self);
end


function RWMTestPane:render()
    ISPanel.render(self);
end


function RWMTestPane:new (x, y, width, height)
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
    return o
end


--***********************************************************
--**                    THE INDIE STONE                    **
--**				  Author: turbotutone				   **
--***********************************************************
require "ISUI/ISPanel"

---@class RWMPanel : ISPanelJoypad
RWMPanel = ISPanelJoypad:derive("RWMPanel");

function RWMPanel:initialise()
    ISPanel.initialise(self)
end

function RWMPanel:createChildren()
end

function RWMPanel:clear()
    self.player = nil;
    self.device = nil;
    self.deviceData = nil;
    self.deviceType = nil;

    if self.focusElement then
        self.focusElement = nil
    end
end

function RWMPanel:readFromObject( _player, _deviceObject, _deviceData, _deviceType  )
    self.player = _player;
    self.device = _deviceObject;
    self.deviceData = _deviceData;
    self.deviceType = _deviceType;
    return true;
end

function RWMPanel:update()
    ISPanel.update(self);
end

function RWMPanel:prerender()
    ISPanel.prerender(self);
end

function RWMPanel:render()
    ISPanel.render(self);
end

function RWMPanel:doWalkTo()
    if self.player and self.device and self.deviceType then
        if self.deviceType == "InventoryItem" then
            return true;
        elseif self.deviceType == "IsoObject" and self.device:getSquare() then
            return luautils.walkAdj( self.player, self.device:getSquare(), false );
        elseif self.deviceType == "VehiclePart" and self.player:getVehicle() == self.device:getVehicle() then
            return true;
        end
    end
end

function RWMPanel:setFocus(_playerNum, _radioParent, _parent)
    --print("setFocus RMWPanel");
    self.playerNum = _playerNum;
    self.radioParent = _radioParent;
    self.wrapParent = _parent;
    --setJoypadFocus(self.playerNum, self);
end

--[[
function RWMPanel:onGainJoypadFocus(joypadData)
    self.wrapParent.drawJoypadFocus = true;
end

function RWMPanel:onLoseJoypadFocus(joypadData)
    self.wrapParent.drawJoypadFocus = false;
end

function RWMPanel:onJoypadDirUp()
    self.radioParent:focusNext(true);
end

function RWMPanel:onJoypadDirDown()
    self.radioParent:focusNext(false);
end

function RWMPanel:onJoypadDirLeft()
    self.wrapParent:setExpanded(false);
end

function RWMPanel:onJoypadDirRight()
    self.wrapParent:setExpanded(true);
end
--]]

function RWMPanel:clearJoypadFocus(joypadData)
end

function RWMPanel:onJoypadDown(button)
end

function RWMPanel:getAPrompt()
    return nil;
end
function RWMPanel:getBPrompt()
    return nil;
end
function RWMPanel:getXPrompt()
    return nil;
end
function RWMPanel:getYPrompt()
    return nil;
end

function RWMPanel:isValidPrompt()
    return (self.player and self.device and self.deviceData)
end
--[[
function RWMPanel:getLBPrompt()
    return "Release focus";
end
function RWMPanel:getRBPrompt()
    return "Select outer";
end
--]]

function RWMPanel:new (x, y, width, height)
    local o = ISPanel:new(x, y, width, height);
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
    o.overrideBPrompt = true;
    --o.fontheight = getTextManager():MeasureStringY(UIFont.Small, "AbdfghijklpqtyZ")+2;
    return o
end


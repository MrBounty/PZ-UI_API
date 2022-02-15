--***********************************************************
--**                    THE INDIE STONE                    **
--**				  Author: turbotutone				   **
--***********************************************************

require "RadioCom/RadioWindowModules/RWMPanel"

---@class RWMVolume : RWMPanel
RWMVolume = RWMPanel:derive("RWMVolume");

function RWMVolume:initialise()
    ISPanel.initialise(self)
end

function RWMVolume:createChildren()
    self:setHeight(self.innerHeight+self.marginTop+self.marginBottom);

    self.speakerButton = ISSpeakerButton:new (self.marginLeft+2, self.marginTop+2, 20, 20, RWMVolume.onSpeakerButton, self);
    self.speakerButton:initialise();
    self:addChild(self.speakerButton);

    local xoffset = self.marginLeft + self.marginRight + 10 + self.textureSize;
    local remWidth = self:getWidth() - xoffset - 10;

    self.volumeBar = ISVolumeBar:new(xoffset, self.marginTop, remWidth, self.innerHeight, RWMVolume.onVolumeChange, self);
    self.volumeBar:initialise();
    self:addChild(self.volumeBar);

    self.itemDropBox = ISItemDropBox:new (0, self.marginTop, self.innerHeight, self.innerHeight, false, self, RWMVolume.addHeadphone, RWMVolume.removeHeadphone, RWMVolume.verifyItem, nil );
    self.itemDropBox:initialise();
    self.itemDropBox:setBackDropTex( getTexture("Item_Headphones"), 0.4, 1,1,1 );
    self.itemDropBox:setDoBackDropTex( true );
    self.itemDropBox:setToolTip( true, getText("IGUI_RadioDragHeadphones") );

    self.hasEnabledHeadphones = false;
    --self:addChild(self.itemDropBox);
end

function RWMVolume:toggleHeaphoneSupport(enable)
    if self.hasEnabledHeadphones ~= enable then
        if not enable then
            self.volumeBar:setWidth(self.volumeBar:getWidth() + self.itemDropBox:getWidth() + 10);
            self:removeChild(self.itemDropBox);
        else
            local x = self.volumeBar:getX() + (self.volumeBar:getWidth()-self.itemDropBox:getWidth());
            self.itemDropBox:setX(x);
            self.volumeBar:setWidth(self.volumeBar:getWidth() - (self.itemDropBox:getWidth() + 10));
            self:addChild(self.itemDropBox);
        end
    end
    self.hasEnabledHeadphones = enable;
end

function RWMVolume:addHeadphone( _items )
    local item;
    local pbuff = 0;

    for _,i in ipairs(_items) do
        --if i:getDelta() > pbuff then
            item = i;
        break;
            --pbuff = i:getDelta()
        --end
    end

    if item then
        if self:doWalkTo() then
            ISTimedActionQueue.add(ISRadioAction:new("AddHeadphones",self.player, self.device, item ));
        end
    end
end

function RWMVolume:removeHeadphone()
    if self:doWalkTo() then
        ISTimedActionQueue.add(ISRadioAction:new("RemoveHeadphones",self.player, self.device ));
    end
end

function RWMVolume:verifyItem(_item)
    if _item:getFullType() == "Base.Headphones" or _item:getFullType() == "Base.Earbuds" then
        return true;
    end
end

function RWMVolume:round(num, idp)
    local mult = 10^(idp or 0);
    return math.floor(num * mult + 0.5) / mult;
end

function RWMVolume:onVolumeChange( _newVol )
    self.volume = _newVol/self.volumeBar:getVolumeSteps(); --self:round((_newVol/self.volumeBar:getVolumeSteps())*10,0)/10;
    --print("onvolumeChange", self.volume);
    if self.deviceData then
        if self:doWalkTo() then
            ISTimedActionQueue.add(ISRadioAction:new("SetVolume",self.player, self.device, self.volume ));
        end
        --self.deviceData:setDeviceVolume(self.volume);
    end
end

function RWMVolume:onSpeakerButton( _ismute )
    self.isMute = _ismute;
    if self.isMute == true then
        if self.deviceData then
            if self:doWalkTo() then
                ISTimedActionQueue.add(ISRadioAction:new("SetVolume",self.player, self.device, 0 ));
            end
        end
        self.volumeBar:setEnableControls(false);
    else
        if self.deviceData then
            if self:doWalkTo() then
                ISTimedActionQueue.add(ISRadioAction:new("SetVolume",self.player, self.device, self.volume~=0 and self.volume or 0.1 ));
            end
        end
        self.volumeBar:setEnableControls(true);
    end
end

function RWMVolume:clear()
    RWMPanel.clear(self);
end

function RWMVolume:readFromObject( _player, _deviceObject, _deviceData, _deviceType )
    RWMPanel.readFromObject(self, _player, _deviceObject, _deviceData, _deviceType );
    self.volume = self.deviceData:getDeviceVolume();
    self.volumeBar:setVolume(math.floor(self.volume*self.volumeBar:getVolumeSteps()));
    if self.deviceData:getIsPortable() and self.deviceData:getIsTelevision()==false then
        self:toggleHeaphoneSupport(true);
    else
        self:toggleHeaphoneSupport(false);
    end

    if self.player then
        self.itemDropBox.mouseEnabled = true;
        self.volumeBar.mouseEnabled = true;
        if JoypadState.players[self.player:getPlayerNum()+1] then
            self.itemDropBox.mouseEnabled = false;
            self.volumeBar.mouseEnabled = false;
        end
    end

    return true;
end

function RWMVolume:update()
    ISPanel.update(self);

    if self.deviceData then
        self.speakerButton:setEnableControls(self.deviceData:getIsTurnedOn());
        self.speakerButton.isMute = self.deviceData:getDeviceVolume()<=0;
        self.volumeBar:setEnableControls(self.deviceData:getIsTurnedOn() and not self.speakerButton.isMute);
        local devVol = self.deviceData:getDeviceVolume()+0.05; --hack for decimal/float precision thingy
        self.volumeBar:setVolume(math.floor(devVol*self.volumeBar:getVolumeSteps()));

        if self.deviceData:getHeadphoneType() >= 0 then
            if self.deviceData:getHeadphoneType() == 0 then
                self.itemDropBox:setStoredItemFake( self.headphonesTex );
            elseif self.deviceData:getHeadphoneType() == 1 then
                self.itemDropBox:setStoredItemFake( self.earbudsTex );
            end
        else
            self.itemDropBox:setStoredItemFake( nil );
        end
    end
end

function RWMVolume:prerender()
    ISPanel.prerender(self);
end


function RWMVolume:render()
    ISPanel.render(self);
end

function RWMVolume:onJoypadDown(button)
    if button == Joypad.AButton then
        --if self.volumeBar:getVolume() < self.volumeBar:getVolumeSteps() then
            self.volumeBar:setVolumeJoypad(true)
        --end
    elseif button == Joypad.BButton then
        --if self.volumeBar:getVolume() > 1 then
            self.volumeBar:setVolumeJoypad(false)
        --end
    elseif button == Joypad.XButton then
        if self.deviceData:getHeadphoneType() >= 0 then
            self:removeHeadphone();
        else
            local tab = {};
            local inventory = self.player:getInventory();
            local list = inventory:FindAll("Base.Headphones");
            if list and list:size()>0 then
                for i=0,list:size()-1 do
                    table.insert(tab, list:get(i));
                end
            end
            list = inventory:FindAll("Base.Earbuds");
            if list and list:size()>0 then
                for i=0,list:size()-1 do
                    table.insert(tab, list:get(i));
                end
            end
            self:addHeadphone( tab );
        end
    elseif button == Joypad.YButton then
        self:onSpeakerButton( not self.speakerButton.isMute );
    end
end

function RWMVolume:getAPrompt()
    return getText("IGUI_RadioVolUp");
end
function RWMVolume:getBPrompt()
    return getText("IGUI_RadioVolDown");
end
function RWMVolume:getXPrompt()
    if self.deviceData:getHeadphoneType() >= 0 then
        return getText("IGUI_RadioRemoveHeadphones");
    else
        local has = false;
        local inventory = self.player:getInventory();
        local list = inventory:FindAll("Base.Headphones");
        if list and list:size()>0 then has = true; end
        if not has then
            list = inventory:FindAll("Base.Earbuds");
            if list and list:size()>0 then has = true; end
        end
        if has then
            return getText("IGUI_RadioAddHeadphones");
        end
    end
    return nil
end
function RWMVolume:getYPrompt()
    if self.speakerButton.isMute then
        return getText("IGUI_RadioUnmuteSpeaker");
    else
        return getText("IGUI_RadioMuteSpeaker");
    end
end


function RWMVolume:new (x, y, width, height)
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
    o.fontheight = getTextManager():MeasureStringY(UIFont.Small, "AbdfghijklpqtyZ")+2;
    o.textureSize = 16;
    o.isMute = false;
    o.volume = 6;
    o.marginLeft = 4;
    o.marginRight = 4;
    o.marginTop = 4;
    o.marginBottom = 4;
    o.innerHeight = 24;
    o.headphonesTex = getTexture("Item_Headphones");
    o.earbudsTex = getTexture("Item_Earbuds");
    return o
end



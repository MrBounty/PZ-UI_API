--***********************************************************
--**                    THE INDIE STONE                    **
--**				  Author: turbotutone				   **
--***********************************************************
require "RadioCom/RadioWindowModules/RWMPanel"

---@class RWMMedia : RWMPanel
RWMMedia = RWMPanel:derive("RWMMedia");

local FONT_HGT_SMALL = getTextManager():getFontHeight(UIFont.Small)

function RWMMedia:initialise()
    ISPanel.initialise(self)
end

function RWMMedia:createChildren()
    --self:setHeight(32);

    local y = 4;
    local ww = math.floor((self:getWidth()-20)/ISLcdBar.charW);
    local charWidth = ww;
    local lcdw = ww*ISLcdBar.charW;
    local x = ((self:getWidth()/2)-(lcdw/2))-2;

    self.lcd = ISLcdBar:new(x,y,charWidth);
    self.lcd:initialise();
    self.lcd:setTextMode(false);
    self:addChild(self.lcd);

    y = self.lcd:getY() + self.lcd:getHeight() + 5;

    x = (self:getWidth()/2)-(24/2);
    self.itemDropBox = ISItemDropBox:new (x, y, 24, 24, false, self, RWMMedia.addMedia, RWMMedia.removeMedia, RWMMedia.verifyItem, nil );
    self.itemDropBox:initialise();
    self.itemDropBox:setBackDropTex( getTexture("Item_Battery"), 0.4, 1,1,1 );
    self.itemDropBox:setDoBackDropTex( true );
    self.itemDropBox:setToolTip( true, getText("IGUI_RadioDragBattery") );
    self:addChild(self.itemDropBox);

    y = self.itemDropBox:getY() + self.itemDropBox:getHeight() + 5;

    local btnHgt = FONT_HGT_SMALL + 1 * 2

    self.toggleOnOffButton = ISButton:new(10, y, self:getWidth()-20, btnHgt, getText("ContextMenu_Turn_On"),self, RWMMedia.togglePlayMedia);
    self.toggleOnOffButton:initialise();
    self.toggleOnOffButton.backgroundColor = {r=0, g=0, b=0, a=0.0};
    self.toggleOnOffButton.backgroundColorMouseOver = {r=1.0, g=1.0, b=1.0, a=0.1};
    self.toggleOnOffButton.borderColor = {r=1.0, g=1.0, b=1.0, a=0.3};
    self:addChild(self.toggleOnOffButton);

    y = self.toggleOnOffButton:getY() + self.toggleOnOffButton:getHeight() + 10;

    self:setHeight(y);
end

function RWMMedia:togglePlayMedia()
    if self:doWalkTo() then
        ISTimedActionQueue.add(ISRadioAction:new("TogglePlayMedia",self.player, self.device ));
    end
end

function RWMMedia:removeMedia()
    if self:doWalkTo() then
        ISTimedActionQueue.add(ISRadioAction:new("RemoveMedia",self.player, self.device ));
    end
end

function RWMMedia:addMedia( _items )
    if self.player:getJoypadBind() == -1 then
        self:addMediaAux(_items[1])
        return
    end
    local playerNum = self.player:getPlayerNum()
    local context = ISContextMenu.get(playerNum, self:getAbsoluteX(), self:getAbsoluteY())
    for _,item in ipairs(_items) do
        context:addOption(item:getDisplayName(), self, self.addMediaAux, item)
    end
    context.mouseOver = 1
    if JoypadState.players[playerNum+1] then
        context.origin = JoypadState.players[playerNum+1].focus
        setJoypadFocus(playerNum, context)
    end
end

function RWMMedia:addMediaAux(item)
    if self:doWalkTo() then
        ISTimedActionQueue.add(ISRadioAction:new("AddMedia",self.player, self.device, item ));
    end
end

function RWMMedia:verifyItem( _item )
    if _item:isRecordedMedia() and _item:getMediaType()==self.deviceData:getMediaType() then
        return true;
    end
end

function RWMMedia:clear()
    RWMPanel.clear(self);
end

function RWMMedia:readFromObject( _player, _deviceObject, _deviceData, _deviceType )
    if _deviceData:getMediaType()<0 then
        return false;
    end
    self.mediaIndex = -9999;

    if _deviceData:getMediaType()==0 then
        self.itemDropBox:setBackDropTex( self.cdTex, 0.4, 1,1,1 );
        self.itemDropBox:setToolTip( true, getText("IGUI_media_dragCD") );
        self.lcd.ledColor = self.lcdBlue.back;
        self.lcd.ledTextColor = self.lcdBlue.text;
    end
    if _deviceData:getMediaType()==1 then
        self.itemDropBox:setBackDropTex( self.tapeTex, 0.4, 1,1,1 );
        self.itemDropBox:setToolTip( true, getText("IGUI_media_dragVHS") );
        self.lcd.ledColor = self.lcdGreen.back;
        self.lcd.ledTextColor = self.lcdGreen.text;
    end

    local read =  RWMPanel.readFromObject(self, _player, _deviceObject, _deviceData, _deviceType );

    if self.player then
        self.itemDropBox.mouseEnabled = true;
        if JoypadState.players[self.player:getPlayerNum()+1] then
            self.itemDropBox.mouseEnabled = false;
        end
    end

    return read;
end

function RWMMedia:getMediaText()
    local text = "";
    local addedSegment = false;
    if self.deviceData:hasMedia() then
        local media = self.deviceData:getMediaData();
        if media then
            if media:getTitleEN() then
                addedSegment = true;
                text = media:getTitleEN();
            end
            if media:getSubtitleEN() then
                addedSegment = true;
                text = text .. (addedSegment and " - " or "") .. media:getSubtitleEN();
            end
            if media:getAuthorEN() then
                addedSegment = true;
                text = text .. (addedSegment and " - " or "") .. media:getAuthorEN();
            end
        end
    end
    if addedSegment then
        return text.." *** ";
    end
    return self.deviceData:getMediaType()==1 and self.textNoTape or self.textNoCD;
end

function RWMMedia:update()
    ISPanel.update(self);

    if self.player and self.device and self.deviceData then
        local isOn = self.deviceData:getIsTurnedOn();

        self.lcd:toggleOn(isOn);

        if (not isOn) and self.deviceData:isPlayingMedia() then
            self.deviceData:StopPlayMedia();
        end

        self.toggleOnOffButton:setEnable(self.deviceData:hasMedia())
        if self.deviceData:isPlayingMedia() then
            self.toggleOnOffButton:setTitle(self.textStop);
        else
            self.toggleOnOffButton:setTitle(self.textPlay);
        end

        if self.mediaIndex~=self.deviceData:getMediaIndex() then
            --print("RWMMedia setting text = "..tostring(self:getMediaText()))
            self.mediaIndex = self.deviceData:getMediaIndex();
            self.mediaText = self:getMediaText();
            --self.lcd:setText(self:getMediaText());
        end

        if self.deviceData:hasMedia() then
            if self.deviceData:getMediaType()==0 then
                self.itemDropBox:setStoredItemFake( self.cdTex );
            end
            if self.deviceData:getMediaType()==1 then
                self.itemDropBox:setStoredItemFake( self.tapeTex );
            end

            if self.deviceData:isPlayingMedia() then
                self.lcd:setText(self.mediaText);
                self.lcd:setDoScroll(true);
            else
                self.lcd:setText(self.idleText);
                self.lcd:setDoScroll(false);
            end
        else
            self.itemDropBox:setStoredItemFake( nil );

            self.lcd:setText(self.mediaText);
            self.lcd:setDoScroll(false);
        end
    end
end

function RWMMedia:prerender()
    ISPanel.prerender(self);
end


function RWMMedia:render()
    ISPanel.render(self);
end

function RWMMedia:onJoypadDown(button)
    if button == Joypad.AButton then
        self:togglePlayMedia()
    elseif button == Joypad.BButton then
        if self.deviceData:hasMedia() then
            self:removeMedia();
        else
            local inv = self.player:getInventory();
            local type = self.deviceData:getMediaType();
            local medias = {};
            if type==0 then
                local list = inv:FindAll("Base.Disc_Retail");
                for i=0,list:size()-1 do
                    table.insert(medias, list:get(i));
                end
            end
            if type==1 then
                local list = inv:FindAll("Base.VHS_Retail");
                for i=0,list:size()-1 do
                    table.insert(medias, list:get(i));
                end
                local list = inv:FindAll("Base.VHS_Home");
                for i=0,list:size()-1 do
                    table.insert(medias, list:get(i));
                end
            end

            if #medias>0 then
                self:addMedia( medias );
            end
        end
    end
end

function RWMMedia:getAPrompt()
    if self.deviceData:isPlayingMedia() then
        return self.textStop;
    else
        return self.textPlay;
    end
end
function RWMMedia:getBPrompt()
    if self.deviceData:hasMedia() then
        return getText("IGUI_media_removeMedia");
    else
        local inv = self.player:getInventory();
        local type = self.deviceData:getMediaType();
        local medias = {};
        if type==0 then
            local list = inv:FindAll("Base.Disc_Retail");
            for i=0,list:size()-1 do
                table.insert(medias, list:get(i));
            end
        end
        if type==1 then
            local list = inv:FindAll("Base.VHS_Retail");
            for i=0,list:size()-1 do
                table.insert(medias, list:get(i));
            end
            local list = inv:FindAll("Base.VHS_Home");
            for i=0,list:size()-1 do
                table.insert(medias, list:get(i));
            end
        end

        if #medias>0 then
            return getText("IGUI_media_addMedia");
        end
    end
    return nil;
end
function RWMMedia:getXPrompt()
    return nil;
end
function RWMMedia:getYPrompt()
    return nil;
end


function RWMMedia:new (x, y, width, height)
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
    o.cdTex = getTexture("Item_Disc");
    o.tapeTex = getTexture("Item_Cassette3");
    o.mediaIndex = -9999;
    o.mediaText = "";
    o.idleText = getText("IGUI_media_idle");
    o.lcdBlue = {
        text = { r=0.039, g=0.180, b=0.2, a=1.0 },
        back = { r=0.172, g=0.686, b=0.764, a=1.0 }
    };
    o.lcdGreen = {
        text = { r=0.180, g=0.2, b=0.039, a=1.0 },
        back = { r=0.686, g=0.764, b=0.172, a=1.0 },
    };
    o.textPlay = getText("IGUI_media_play");
    o.textStop = getText("IGUI_media_stop");
    o.textNoCD = getText("IGUI_media_nocd");
    o.textNoTape = getText("IGUI_media_notape");
    return o
end


--***********************************************************
--**                    THE INDIE STONE                    **
--**				  Author: turbotutone				   **
--***********************************************************
local enabled = false;

require "ISUI/ISCollapsableWindow"

---@class DeviceInspector : ISCollapsableWindow
DeviceInspector = ISCollapsableWindow:derive("DeviceInspector");
DeviceInspector.instance = nil;

function DeviceInspector.onMouseButtonUp(_object, _x, _y)
    if DeviceInspector.instance==nil then
        DeviceInspector.instance = DeviceInspector:new (100, 100, 800, 600, getPlayer());
        DeviceInspector.instance:initialise();
        DeviceInspector.instance:instantiate();
    end
    if _object~=nil then
        DeviceInspector.instance:readObject( _object );
    end

    DeviceInspector.instance:addToUIManager();
    DeviceInspector.instance:setVisible(true);
    return DeviceInspector.instance;
end

function DeviceInspector:initialise()
    ISCollapsableWindow.initialise(self);
end

function DeviceInspector:close()
    ISCollapsableWindow.close(self)
    if JoypadState.players[self.playerNum+1] then
        setJoypadFocus(self.playerNum, nil)
    end
end

function DeviceInspector:createChildren()
    ISCollapsableWindow.createChildren(self);

    local th = self:titleBarHeight();

    self.richtext = ISRichTextPanel:new(0, 0+th, self.width, self.height-(th+10));
    self.richtext:initialise();

    self:addChild(self.richtext);

    self.richtext.background = false;
    self.richtext.autosetheight = false;
    self.richtext.clip = true
    self.richtext:addScrollBars();

    self.richtext.text = "";
    self.richtext:paginate();
end

function DeviceInspector:onResize()
    ISUIElement.onResize(self);
    local th = self:titleBarHeight();
    self.richtext:setWidth(self.width);
    self.richtext:setHeight(self.height-(th+10));
end

function DeviceInspector:update()
    ISCollapsableWindow.update(self);
end

function DeviceInspector:prerender()
    self:stayOnSplitScreen();
    ISCollapsableWindow.prerender(self);
end

function DeviceInspector:stayOnSplitScreen()
    ISUIElement.stayOnSplitScreen(self, self.playerNum)
end


function DeviceInspector:render()
    ISCollapsableWindow.render(self);

    self.richtext:clearStencilRect();
end


function DeviceInspector:close()
    ISCollapsableWindow.close(self);
    self:removeFromUIManager();
    self:clear();
end

function DeviceInspector:clear()
    self.currentTile = nil;
end

-- Go go gadget analyzer
function DeviceInspector:readObject( _object )
    self:clear();

    if _object~=nil and self.richtext~=nil then
        self.tmpTxt = "";

        if instanceof(_object, "IsoObject") then
            self:addTitle("IsoObject");
            self:addLine("Name",_object:getName());
            if _object:getSprite() then
                self:addLine("SpriteName",_object:getSprite():getName());
                self:addLine("IsMoveWithWind",_object:getSprite():isMoveWithWind());
            end
        end
        if instanceof(_object, "IsoWaveSignal") then
            self:addTitle("General infos");
            self:addLine("NightsSurvived",getGameTime():getNightsSurvived());
            self:addLine("Electric shutdown",getSandboxOptions():getElecShutModifier());

            self:addTitle("Found object:");
            local type = instanceof(_object, "IsoTelevision") and "TV" or "RADIO";
            self:addLine("Type",type);
            self:addLine("Name",_object:getName());

            local square = _object:getSquare();
            self:addLine("Square",square~=nil and "valid" or "null");

            if square~=nil then
                self:addTitle("Square Info");
                self:addLine("Has electricity",square:haveElectricity());
                self:addLine("Has room",square:getRoom()~=nil and "true" or "false");
            end

            local dd = _object:getDeviceData();
            if dd~=nil then
                self:addTitle("Device Data");
                self:addLine("Base volume range",dd:getBaseVolumeRange());
                self:addLine("Channel",dd:getChannel());
                self:addLine("Device name",dd:getDeviceName());
                self:addLine("Device sound range",dd:getDeviceSoundVolumeRange());
                self:addLine("Device volume",dd:getDeviceVolume());
                self:addLine("Device volume range",dd:getDeviceVolumeRange());
                self:addLine("Has battery",dd:getHasBattery());
                self:addLine("Headphone type",dd:getHeadphoneType());
                self:addLine("Is battery powered",dd:getIsBatteryPowered());
                self:addLine("Is high tier",dd:getIsHighTier());
                self:addLine("Is portable",dd:getIsPortable());
                self:addLine("Is television",dd:getIsTelevision());
                self:addLine("Is turned on",dd:getIsTurnedOn());
                self:addLine("Is twoway",dd:getIsTwoWay());
                self:addLine("Last recored distance",dd:getLastRecordedDistance());
                self:addLine("Min channel range",dd:getMinChannelRange());
                self:addLine("Max channel range",dd:getMaxChannelRange());
                self:addLine("Mic muted",dd:getMicIsMuted());
                self:addLine("Mic range",dd:getMicRange());
                self:addLine("Power",dd:getPower());
                self:addLine("Transmit range",dd:getTransmitRange());
                self:addLine("Use delta",dd:getUseDelta());
                self:addLine("Can be powered here",dd:canBePoweredHere());
            end

        end

        self.richtext.text = self.tmpTxt;
        self.richtext:paginate();
    end
end

function DeviceInspector:addTitle(_title)
    self.tmpTxt = self.tmpTxt .. " <H2> <ORANGE> "..tostring(_title).." <LINE> ";
end

function DeviceInspector:addLine(_prefix, _line)
    --if _prefix:len()<40 then
    --_prefix = _prefix .. string.rep(" ",40-_prefix:len());
    --end
    self.tmpTxt = self.tmpTxt .. " <TEXT> "..tostring(_prefix)..": "..tostring(_line).." <LINE> ";
end

function DeviceInspector:addLineEnd()
    --if _prefix:len()<40 then
    --_prefix = _prefix .. string.rep(" ",40-_prefix:len());
    --end
    self.tmpTxt = self.tmpTxt .." <LINE> ";
end

function DeviceInspector:new (x, y, width, height, player)
    local o = {}
    --o.data = {}
    o = ISCollapsableWindow:new(x, y, width, height);
    setmetatable(o, self)
    self.__index = self
    o.x = x;
    o.y = y;
    o.player = player;
    o.playerNum = player:getPlayerNum();
    o.borderColor = {r=0.4, g=0.4, b=0.4, a=1};
    o.backgroundColor = {r=0, g=0, b=0, a=0.8};
    o.width = width;
    o.height = height;
    o.anchorLeft = true;
    o.anchorRight = false;
    o.anchorTop = true;
    o.anchorBottom = false;
    o.pin = true;
    o.isCollapsed = false;
    o.collapseCounter = 0;
    o.title = "Device Inspection Gadget";
    --o.viewList = {}
    o.resizable = true;
    o.drawFrame = true;

    o.currentTile = nil;
    o.richtext = nil;
    o.overrideBPrompt = true;
    o.subFocus = nil;
    o.hotKeyPanels = {};
    o.isJoypadWindow = false;
    return o
end

--[[
function DeviceInspector:OnGameStart()
        getCore():setForceSnow(true);
        forceSnowCheck();
end--]]

if enabled then
    --Events.OnGameStart.Add(DeviceInspector.OnGameStart);
    Events.OnObjectLeftMouseButtonUp.Add(DeviceInspector.onMouseButtonUp);
end

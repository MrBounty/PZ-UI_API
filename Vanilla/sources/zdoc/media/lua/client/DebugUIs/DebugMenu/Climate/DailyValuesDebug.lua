--***********************************************************
--**                    THE INDIE STONE                    **
--**				  Author: turbotutone				   **
--***********************************************************


require "ISUI/ISCollapsableWindow"

---@class DailyValuesDebug : ISCollapsableWindow
DailyValuesDebug = ISCollapsableWindow:derive("DailyValuesDebug");
DailyValuesDebug.instance = nil;
DailyValuesDebug.shiftDown = 0;

--[[
local enabled = true; --getDebug();

function DailyValuesDebug.OnKeepKeyDown(key)
    --backspace 13, shift 42, 54
    --print("KeyKeepDown = "..tostring(key));
    if key==42 or key==54 then
        DailyValuesDebug.shiftDown = 4;
    end
end

function DailyValuesDebug.OnKeyDown(key)
    --backspace 13, shift 42, 54
    --print("KeyDown = "..tostring(key));
    if DailyValuesDebug.shiftDown>0 and key ==13 then
        DailyValuesDebug.OnOpenPanel();
    end
end--]]

function DailyValuesDebug.OnOpenPanel()
    DailyValuesDebug.fx = getCell():getWeatherFX();
    DailyValuesDebug.cm = getClimateManager();

    if DailyValuesDebug.instance==nil then
        DailyValuesDebug.instance = DailyValuesDebug:new (100, 100, 400, 600, getPlayer());
        DailyValuesDebug.instance:initialise();
        DailyValuesDebug.instance:instantiate();
    end

    DailyValuesDebug.instance:addToUIManager();
    DailyValuesDebug.instance:setVisible(true);

    return DailyValuesDebug.instance;
end

function DailyValuesDebug:initialise()
    ISCollapsableWindow.initialise(self);
end

function DailyValuesDebug:createChildren()
    ISCollapsableWindow.createChildren(self);

    local th = self:titleBarHeight();

    local y = th;

    self.labels = {};
    self.tempColor = {r=1.0,g=1.0,b=1.0,a=1.0};

    --[[*******************************************************************************************--]]
    y = self:addLabel(y,"season","Season:","");
    y = self:addLabelValue(y,"value","seasonProgression","Progression:",0);
    y = self:addLabelValue(y,"value","seasonStrength","Strength:",0);
    --y = self:addLabelValue(y,"value","seasonMidpoint","Midpoint:",0);

    y = y+8;

    y = self:addLabel(y,"temperatureInfo","");
    y = self:addLabel(y,"skyInfo","");

    y = y+8;

    y = self:addLabelValue(y,"value","temperature","Temperature:",0);
    y = self:addLabelValue(y,"value","meantemperature","Day mean temperature:",0);
    y = self:addLabelValue(y,"value","wind","Wind intensity:",0);
    y = self:addLabelValue(y,"value","clouds","Cloud intensity:",0);
    y = self:addLabelValue(y,"value","precipitation","Precipitation:",0);
    y = self:addLabelValue(y,"value","weatherstrength","Weather strength:",0);
    y = self:addLabelValue(y,"color","currentColor","Color influence:");
    y = self:addLabelValue(y,"color","weatherColor","Weather color influence:");
    y = self:addLabelValue(y,"color","finalColor","Final Color:");

    y = self:addLabelValue(y,"value","desatvalue","Desaturation value:",0);
    y = self:addLabelValue(y,"color","desaturation","Desaturation:");


    self:setHeight(y+self:resizeWidgetHeight()+4);
end

function DailyValuesDebug:addLabel(_curY, _labelID, _title)
    if not self.labels[_labelID] then
        local label = {};
        label.titleLabel = ISLabel:new(2, _curY, 16, _title, 1, 1, 1, 1.0, UIFont.Small, true);
        label.titleLabel:initialise();
        label.titleLabel:instantiate();
        self:addChild(label.titleLabel);

        self.labels[_labelID] = label;

        _curY = label.titleLabel:getY() + label.titleLabel:getHeight();
    else
        print("Cannot add label: "..tostring(_labelID));
    end
    return _curY;
end

function DailyValuesDebug:addLabelValue(_curY, _type, _labelID, _title, _defaultVal)
    if not self.labels[_labelID] then
        local label = {};
        label.titleLabel = ISLabel:new(2, _curY, 16, _title, 1, 1, 1, 1.0, UIFont.Small, true);
        label.titleLabel:initialise();
        label.titleLabel:instantiate();
        self:addChild(label.titleLabel);

        if _type=="value" then
            label.valueLabel = ISLabel:new(self.width-(self.width/3), _curY, 16, tostring(_defaultVal), 1, 1, 1, 1.0, UIFont.Small, true);
            label.valueLabel:initialise();
            label.valueLabel:instantiate();
            self:addChild(label.valueLabel);
        elseif _type=="color" then
            label.valueLabel = ISPanel:new(self.width-(self.width/3), _curY+4, (self.width/3)-4, 10);
            label.valueLabel:initialise();
            label.valueLabel.backgroundColor = _defaultVal or {r=1.0,g=1.0,b=1.0,a=1.0};
            self:addChild(label.valueLabel);
        end

        self.labels[_labelID] = label;

        _curY = label.titleLabel:getY() + label.titleLabel:getHeight();
    else
        print("Cannot add label: "..tostring(_labelID));
    end
    return _curY;
end

function DailyValuesDebug:getTitleLabel(_labelID)
    if self.labels[_labelID] then
        return self.labels[_labelID].titleLabel;
    end
end

function DailyValuesDebug:getValueLabel(_labelID)
    if self.labels[_labelID] then
        return self.labels[_labelID].valueLabel;
    end
end


function DailyValuesDebug:onResize()
    ISUIElement.onResize(self);
    local th = self:titleBarHeight();
    --self.richtext:setWidth(self.width);
    --self.richtext:setHeight(self.height-(th+10));
end

local function round(num, numDecimalPlaces)
    local mult = 10^(numDecimalPlaces or 0)
    return math.floor(num * mult + 0.5) / mult
end

function DailyValuesDebug:update()
    ISCollapsableWindow.update(self);

    if DailyValuesDebug.shiftDown>0 then
        DailyValuesDebug.shiftDown = DailyValuesDebug.shiftDown-1;
    end

    local wp = DailyValuesDebug.cm:getWeatherPeriod();

    self:getTitleLabel("season").name = tostring(DailyValuesDebug.cm:getSeasonName());
    self:getValueLabel("seasonProgression").name = ""..tostring(round(DailyValuesDebug.cm:getSeasonProgression(),2));
    self:getValueLabel("seasonStrength").name = ""..tostring(round(DailyValuesDebug.cm:getSeasonStrength(),2));

    local airTemp = DailyValuesDebug.cm:getAirMassTemperature();
    if airTemp>0.7 then
        self:getTitleLabel("temperatureInfo").name = "Relatively very warm";
    elseif airTemp>0.35 then
        self:getTitleLabel("temperatureInfo").name = "Relatively warm";
    elseif airTemp>0 then
        self:getTitleLabel("temperatureInfo").name = "Relatively slightly warm";
    elseif airTemp>-0.35 then
        self:getTitleLabel("temperatureInfo").name = "Relatively slightly cold";
    elseif airTemp>-0.7 then
        self:getTitleLabel("temperatureInfo").name = "Relatively cold";
    else
        self:getTitleLabel("temperatureInfo").name = "Relatively very cold";
    end

    local clouds = DailyValuesDebug.cm:getCloudIntensity();
    local cloudStr = "";
    if clouds>0.8 then
        cloudStr = "Extremely cloudy.";
    elseif clouds>0.6 then
        cloudStr = "Very cloudy.";
    elseif clouds>0.4 then
        cloudStr = "Cloudy.";
    elseif clouds>0.2 then
        cloudStr = "Slightly cloudy";
    else
        cloudStr = "Mostly clear sky.";
    end

    local wind = DailyValuesDebug.cm:getWindPower();
    local windStr = "";
    if wind>0.65 then
        windStr = "Extremely windy, ";
    elseif wind>0.45 then
        windStr = "Very strong wind, ";
    elseif wind>0.3 then
        windStr = "Very windy, ";
    elseif wind>0.2 then
        windStr = "Windy, ";
    elseif wind>0.1 then
        windStr = "Slightly windy, ";
    else
        windStr = "Minor breezes, ";
    end

    self:getTitleLabel("skyInfo").name = windStr..cloudStr;

    self:getValueLabel("temperature").name = ""..tostring(round(DailyValuesDebug.cm:getTemperature(),1));
    self:getValueLabel("meantemperature").name = ""..tostring(round(DailyValuesDebug.cm:getDayMeanTemperature(),1));
    self:getValueLabel("wind").name = ""..tostring(round(wind,2));
    self:getValueLabel("clouds").name = ""..tostring(round(clouds,2));
    self:getValueLabel("precipitation").name = ""..tostring(round(DailyValuesDebug.cm:getPrecipitationIntensity(),2));
    self:getValueLabel("weatherstrength").name = ""..tostring(round(wp:getCurrentStrength(),2));
    --weatherStrength

    local colClim = DailyValuesDebug.cm:getGlobalLightInternal();
    local col = self:getValueLabel("currentColor").backgroundColor;
    col.r = colClim:getRedFloat();
    col.g = colClim:getGreenFloat();
    col.b = colClim:getBlueFloat();

    colClim = wp:getCloudColor():getExterior();
    col = self:getValueLabel("weatherColor").backgroundColor;
    if wp:isRunning() then
        col.r = colClim:getRedFloat();
        col.g = colClim:getGreenFloat();
        col.b = colClim:getBlueFloat();
    else
        col.r = 0;
        col.g = 0;
        col.b = 0;
    end

    colClim = DailyValuesDebug.cm:getGlobalLight():getExterior();
    col = self:getValueLabel("finalColor").backgroundColor;
    col.r = colClim:getRedFloat();
    col.g = colClim:getGreenFloat();
    col.b = colClim:getBlueFloat();

    self:getValueLabel("desatvalue").name = ""..tostring(round(DailyValuesDebug.cm:getDesaturation(),2));

    local desat = 1-DailyValuesDebug.cm:getDesaturation();
    col = self:getValueLabel("desaturation").backgroundColor;
    col.r = desat;
    col.g = desat;
    col.b = desat;

end

function DailyValuesDebug:prerender()
    self:stayOnSplitScreen();
    ISCollapsableWindow.prerender(self);
end

function DailyValuesDebug:stayOnSplitScreen()
    ISUIElement.stayOnSplitScreen(self, self.playerNum)
end


function DailyValuesDebug:render()
    ISCollapsableWindow.render(self);

    --self.richtext:clearStencilRect();
end


function DailyValuesDebug:close()
    ISCollapsableWindow.close(self);
    if JoypadState.players[self.playerNum+1] then
        setJoypadFocus(self.playerNum, nil)
    end
    self:removeFromUIManager();
    self:clear();
end

function DailyValuesDebug:clear()
    self.currentTile = nil;
end


function DailyValuesDebug:new (x, y, width, height, player)
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
    o.backgroundColor = {r=0, g=0, b=0, a=0.25};
    o.width = width;
    o.height = height;
    o.anchorLeft = true;
    o.anchorRight = false;
    o.anchorTop = true;
    o.anchorBottom = false;
    o.pin = true;
    o.isCollapsed = false;
    o.collapseCounter = 0;
    o.title = "Climate values";
    --o.viewList = {}
    o.resizable = true;
    o.drawFrame = true;

    o.currentTile = nil;
    o.richtext = nil;
    o.overrideBPrompt = true;
    o.subFocus = nil;
    o.hotKeyPanels = {};
    o.isJoypadWindow = false;
    ISDebugMenu.RegisterClass(self);
    return o
end

--[[
if enabled then
    Events.OnCustomUIKey.Add(DailyValuesDebug.OnKeyDown);
    Events.OnKeyKeepPressed.Add(DailyValuesDebug.OnKeepKeyDown);
    --Events.OnObjectLeftMouseButtonUp.Add(DailyValuesDebug.onMouseButtonUp);
end--]]
